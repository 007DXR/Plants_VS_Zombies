package core.zombies;
import java.io.File;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import core.game.GamePlay;
import core.plants.Plant;
import core.Constants;
import core.json.FileUtils;
import core.json.JSONException;
import core.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;

// public class Demo {
//     public static void main(String args[]) throws IOException {

//         File file=new File(""resources/data/entity/zombie.json"");
//         String content= FileUtils.readFileToString(file,"UTF-8");
//         JSONObject jsonObject=new JSONObject(content);
//         System.out.println("姓名是："+jsonObject.getString("name"));
//         System.out.println("年龄："+jsonObject.getDouble("age"));
//         System.out.println("学到的技能："+jsonObject.getJSONArray("major"));
//         System.out.println("国家："+jsonObject.getJSONObject("Nativeplace").getString("country"));

//     }
// }

public abstract class Zombie {
    // 位置
    int x;
    int y;
    // 大小
    int width;
    int height;
    // String state_name;
    String name;
    // BufferedImage [] frames;
    ArrayList<BufferedImage> frames;
    // ArrayList<String> frames=new ArrayList<String>();
    int frame_index=0;
    int frame_num;

    BufferedImage image;
    int centerx;
    int bottom;
    int health;
    int damage;
    boolean dead=false;
    boolean losHead=false;
    boolean helmet=false;
    List<ZombieHead> head_group;

    int walk_timer = 0;
    int animate_timer = 0;
    int attack_timer = 0;
    String state=Constants.WALK;
    int animate_interval = 150;
    int ice_slow_ratio = 1;
    int ice_slow_timer = 0;
    int hit_timer = 0;
    int speed = 1;
    boolean is_hypno = false;
    
    // -----------------------------------
    int freeze_timer = 0;
    int current_time;
    Plant prey;
    String ice_trap_image;

    static JSONObject toolZombieRect;
    static HashMap GFX=new HashMap<String,ArrayList<BufferedImage>>();
    // ArrayList<BufferedImage> die_frames;
    // String die_name;

    public Zombie(int x, int y, String name, int health, List<ZombieHead> head_group, int damage) {

        this.name = name;
        this.toolZombie();
        this.loadImages();
        this.frame_num = this.frames.size();
        
        // this.loadFrames(this.die_frames, this.die_name, toolZombieRect.getJSONObject(name).getInt("x"),Constants.BLACK);

        this.image = this.frames.get(this.frame_index);
        // this.rect = this.image.get_rect();
        this.centerx = x;
        this.bottom = y;

        this.health = health;
        this.damage = damage;
       
        this.head_group = head_group;
        this.load_all_gfx();
        // -----------------------
        
    }
    
    
    // this.helmet_attack_frames = []
    // this.walk_frames = []
    // this.attack_frames = []
    // this.losthead_walk_frames = []
    // this.losthead_attack_frames = []

    // this.boomdie_frames = []

    // 判断颜色相等
    public static boolean checkColor(int color, int target) {
        int rgb_color = color& 0x00ffffff;
        int rgb_target = target & 0x00ffffff;
        return rgb_color == rgb_target;
    }
     // 指定颜色透明化
    public static BufferedImage adjustAlpha(BufferedImage image_, Color color) {
        int width = image_.getWidth();
        int height = image_.getHeight();
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        for (int j = 0; j < height; ++j) {
            for (int i = 0; i < width; ++i) {
                int rgb = image_.getRGB(i, j);
                if (checkColor(rgb, color.getRGB())) {
                    rgb = (1 << 24) | (rgb & 0x00ffffff);
                }
                output.setRGB(i, j, rgb);
            }
        }
        return output;
    }

  
    // 图片缩放
    public static BufferedImage resize(BufferedImage image_, double scale) {
        int width = image_.getWidth();
        int height = image_.getHeight();
        int nwidth = (int) (width * scale);
        int nheight = (int) (height * scale);

        Image img2 = image_.getScaledInstance(nwidth, nheight, Image.SCALE_DEFAULT);
        BufferedImage output = new BufferedImage(nwidth, nheight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = output.createGraphics();
        graphics.drawImage(img2, 0, 0, null);
        graphics.dispose();
        return output;
    }
     // 判断鼠标点击
     public boolean checkMouseClick(int x_, int y_) {
        if (x_ >= x && x_ <= (x + width) && y_ >= y && y_ <= (y + height))
            return true;
        else
            return false;
    }
    
    // 加载图片
    public static BufferedImage loadImage(String filename, double scale, Color color) {
        try {
            BufferedImage img = ImageIO.read(new File(filename));
            img = resize(img, scale);
            img = adjustAlpha(img, color);
            return img;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    
    // 判断能否选中
    public boolean canClick(int sun_value, int current_time) {
        if (sun_cost <= sun_value && (current_time - frozen_timer) > frozen_time)
            return true;
        else
            return false;
    }
    
    public boolean canSelect() {
        return select;
    }

    // 设置选中状态
    public void setSelect(boolean can_select) {
        select = can_select;
        if (can_select)
            image = adjustBrightness(orig_image, 255);
        else
            image = adjustBrightness(orig_image, 128);
    }
    public void load_all_gfx(){
        Path dir = Paths.get("resources/graphics/Zombies");
        
        List<Path> result = new LinkedList<Path>();
        Files.walkFileTree(dir, new FindJavaVisitor(result));
        System.out.println("result.size()=" + result.size());       
        
    }

    
    private static class FindJavaVisitor extends SimpleFileVisitor<Path>{
        private List<Path> result;
        public FindJavaVisitor(List<Path> result){
            this.result = result;
        }
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
            if(file.toString().endsWith(".png")){
                // result.add(file.getFileName());
                String fileName=file.getFileName().toString();
                // ArrayList< BufferedImage > frameList=GFX.get(fileName)==null?new ArrayList< BufferedImage >():(ArrayList<BufferedImage>) GFX.get(fileName);
                ArrayList< BufferedImage > frameList;
                if (GFX.get(fileName)==null){
                    frameList=new ArrayList< BufferedImage >();
                    GFX.put(fileName,frameList);
                }else{
                    frameList=(ArrayList<BufferedImage>) GFX.get(fileName);
                }
                frameList.add(loadImage(file.toAbsolutePath().toString(),1,Constants.BLACK));
            }
            
            return FileVisitResult.CONTINUE;
        }
    }

    public abstract void loadImages();
    public void toolZombie(){
        File file=new File("resources/data/entity/zombie.json" );

        String content= FileUtils.readFileToString(file,"UTF-8");
        JSONObject jsonObject=new JSONObject(content);
        toolZombieRect=jsonObject.getJSONObject("zombie_image_rect");
    }
   
    // the zombie is hypo&&attack other zombies when it ate a HypnoShroom

    public void loadFrames(ArrayList<BufferedImage>frames,String name,int image_x,Color colorkey)
    {   
        ArrayList<BufferedImage> frame_list=(ArrayList<BufferedImage>)GFX.get(name);
        
        BufferedImage rect = frame_list.get(0);
        int width = rect.getWidth();
        int height=rect.getHeight();
        width -= image_x;
    
        for (BufferedImage frame : frame_list) {
            frames.add(frame.getSubimage(image_x, 0, width, height));
                // tool.get_image(frame, image_x, 0, width, height, colorkey));
        }
    }
    public void update( game_info) {
        this.current_time = game_info[Constants.CURRENT_TIME];
        this.handleState();
        this.updateIceSlow();
        this.animation();
    }
    public void handleState() {
        switch (this.state){
            case Constants.WALK:
            this.walking();
            break;
            case Constants.ATTACK:
            this.attacking();
            break;
            case Constants.DIE :
            this.dying();
            break;
            default:
            this.freezing();

        }
    }
    public void walking() {
        if (this.health <= 0) {
            this.setDie();
        } else if (this.health <= Constants.LOSTHEAD_HEALTH && this.losHead == false) {
            this.changeFrames(this.losthead_walk_frames);
            this.setLostHead();
        } else if (this.health <= Constants.NORMAL_HEALTH && this.helmet) {
            this.changeFrames(this.walk_frames);
            this.helmet = false;
            if (this.name == Constants.NEWSPAPER_ZOMBIE) {
                this.speed = 2;
            }
        }
        if (this.current_time - this.walk_timer > Constants.ZOMBIE_WALK_INTERVAL *
        this.getTimeRatio()) {
        this.walk_timer = this.current_time;
        if( this.is_hypno) {
        this.rect.x += this.speed;
        }
        else {
        this.rect.x -= this.speed;
        }
        }
    }

    public void attacking() {
        if (this.health <= 0) {
            this.setDie();
        } else if (this.health <= Constants.LOSTHEAD_HEALTH && this.losHead == false) {
            this.changeFrames(this.losthead_attack_frames);
            this.setLostHead();
        } else if (this.health <= Constants.NORMAL_HEALTH && this.helmet) {
            this.changeFrames(this.attack_frames);
            this.helmet = false;
        }
        if (this.current_time - this.attack_timer > Constants.ATTACK_INTERVAL * this.getTimeRatio()) {
            if (this.prey.health > 0) {
                if (this.prey_is_plant) {
                    this.prey.setDamage(this.damage, this);
                } else {
                    this.prey.setDamage(this.damage);
                }
            }
            this.attack_timer = this.current_time;
        }
        if (this.prey.health <= 0) {
            this.prey = null;
            this.setWalk();
        }
    }

    public void dying() {

    }
    public void freezing() {
        if (this.health <= 0 ){
          this.setDie();
        }
        else if( this.health <= Constants.LOSTHEAD_HEALTH &&  this.losHead==false) {
            if (this.old_state == Constants.WALK) {
                this.changeFrames(this.losthead_walk_frames);
            }
            else {
                this.changeFrames(this.losthead_attack_frames);
            }
            this.setLostHead();
        }
        if ((this.current_time - this.freeze_timer) > Constants.FREEZE_TIME) {
            this.setWalk();
        }
    }
    public void setLostHead(){
        this.losHead = true;
        if (this.head_group.size()!=0) {
            this.head_group.add(ZombieHead(this.centerx, this.bottom));
        }
    }
    public void changeFrames(ArrayList<BufferedImage> frames) {
        // '''change image frames&&modify rect position'''
        this.frames = frames;
        this.frame_num = this.frames.size();
        this.frame_index = 0;

      
        this.image = this.frames.get(this.frame_index);
       
    }
    // 调整亮度
    public BufferedImage adjustBrightness(BufferedImage image_, int alpha) {
        int width = image_.getWidth();
        int height = image_.getHeight();
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int j1 = 0; j1 < height; ++j1) {
            for (int j2 = 0; j2 < width; ++j2) {
                int rgb = image_.getRGB(j2, j1);
                int R, G, B;
                R = ((rgb >> 16) & 0xff) * alpha / 256;
                G = ((rgb >> 8) & 0xff) * alpha / 256;
                B = (rgb & 0xff) * alpha / 256;
                rgb = ((255 & 0xff) << 24) | ((R & 0xff) << 16) | ((G & 0xff) << 8) | ((B & 0xff));
                output.setRGB(j2, j1, rgb);
            }
        }
        return output;
    }
    public void animation() {
        if (this.state == Constants.FREEZE) {
            // this.image.set_alpha(192);
            this.adjustBrightness(this.image,192);
            return;
        }
        if (this.current_time - this.animate_timer > this.animate_interval *
        this.getTimeRatio()) {
            this.frame_index += 1;
            if (this.frame_index >= this.frame_num) {
                if (this.state == Constants.DIE ){
                    // this.kill();
                    return;
                }
                this.frame_index = 0;
            }
            this.animate_timer = this.current_time;
        }
        this.image = this.frames.get(this.frame_index);
        if (this.is_hypno ){
            this.image = pg.transform.flip(this.image, true, false)
        }
        if(this.current_time - this.hit_timer >= 200){
            // this.image.set_alpha(255);
            this.adjustBrightness(this.image,255);
        }
        else {
            // this.image.set_alpha(192);
            this.adjustBrightness(this.image,192);
        }
    }
    public int getTimeRatio() {
        return this.ice_slow_ratio;
    }

    public void setIceSlow() {
        // '''when get a ice bullet damage, slow the attack||walk speed of the zombie'''
        this.ice_slow_timer = this.current_time;
        this.ice_slow_ratio = 2;
    }

    public void updateIceSlow() {
        if (this.ice_slow_ratio > 1) {
            if (this.current_time - this.ice_slow_timer > Constants.ICE_SLOW_TIME) {
                this.ice_slow_ratio = 1;
            }
        }
    }

    public void setDamage(int damage, Boolean ice) {
        this.health -= damage;
        this.hit_timer = this.current_time;
        if (ice) {
            this.setIceSlow();
        }
    }

    public void setWalk() {
        this.state = Constants.WALK;
        this.animate_interval = 150;

        if (this.helmet) {
        this.changeFrames(this.helmet_walk_frames);
        } else if (this.losHead) {
        this.changeFrames(this.losthead_walk_frames);

        } else {
        this.changeFrames(this.walk_frames);
        }
    }
    public void setAttack( prey, is_plant) {
        this.prey = prey;
        // # prey can be plant||other zombies
        this.prey_is_plant = is_plant;
        this.state = Constants.ATTACK;
        this.attack_timer = this.current_time;
        this.animate_interval = 100

        if (this.helmet) {
            this.changeFrames(this.helmet_attack_frames);
        }else if (this.losHead) {
            this.changeFrames(this.losthead_attack_frames);
        }else {
            this.changeFrames(this.attack_frames);
        }
    }
    public void setDie() {
        this.state = Constants.DIE;
        this.animate_interval = 200;
        this.changeFrames(this.die_frames);
    }

    public void setBoomDie() {
        this.state = Constants.DIE;
        this.animate_interval = 200;
        this.changeFrames(this.boomdie_frames);
    }

    public void setFreeze(String ice_trap_image) {
        this.old_state = this.state;
        this.state = Constants.FREEZE;
        this.freeze_timer = this.current_time;
        this.ice_trap_image = ice_trap_image;
        this.ice_trap_rect = ice_trap_image.get_rect()
        this.ice_trap_rect.centerx = this.rect.centerx
        this.ice_trap_rect.bottom = this.rect.bottom
    }

    public void drawFreezeTrap( surface) {
    if (this.state == Constants.FREEZE) {
    surface.blit(this.ice_trap_image, this.ice_trap_rect)
    }}
    public void setHypno() {
        this.is_hypno = true;
        this.setWalk();
    }

}
