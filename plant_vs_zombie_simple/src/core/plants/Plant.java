package core.plants;

import java.io.File;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;
import javax.imageio.ImageIO;

import core.Constants;
import core.*;
import core.zombies.*;
import core.game.Group;
import core.game.Rect;
import core.game.Sprite; 


/**
 * 需要修改的部分
 * 图片加载LoadFrames LoadImages
 * Constant.java上的修改
 */

public class Plant extends Sprite{

    public int explode_y_range;
    public int explode_x_range;
    public double scale;
    public int frame_index=0;
    public int frame_num;
    public Zombie attack_zombie = null;
    public Group zombie_group = null;

    //public int width;
    //public int height;
    public BufferedImage image;

    public int health;
    //public int x;
    //public int y;

    public String state;
    public String name;
    public String old_state;
    //public Zombie kill_zombie;
    public boolean can_sleep = false;//蘑菇是true，全局判断给他setSleep

    long animate_interval = 100;
    long animate_timer = 0;
    long hit_timer = 0;
    long current_time = 0;
    public boolean is_init;

    public ArrayList<BufferedImage> frames  = new ArrayList<BufferedImage>();
    public ArrayList<BufferedImage> framesArrayList = new ArrayList<BufferedImage>();
    public ArrayList<BufferedImage> attack_frames = new ArrayList<BufferedImage>();
    public ArrayList<BufferedImage> sleep_frames = new ArrayList<BufferedImage>();
    public ArrayList<BufferedImage> idle_frames = new ArrayList<BufferedImage>();
    public ArrayList<BufferedImage> cry_frames = new ArrayList<BufferedImage>();
    public ArrayList<BufferedImage> big_frames = new ArrayList<BufferedImage>();


    // 因为继承之后的构造函数只要传入x和y，所以这里没有修改
    public Plant(int health, int x, int y, String name, double scale){
        super(); 
        this.health = health;
        this.state = Constants.IDLE;
        this.name = name;
        this.scale = scale;
        this.loadImages(name, scale);
       
        this.frame_num = this.frames.size();
        this.rect = new Rect(this.frames.get(this.frame_index), x, y);
    }
    
   


    // 判断鼠标点击
    //public boolean checkMouseClick(int x_, int y_) {
    //    if (x_ >= x && x_ <= (x + width) && y_ >= y && y_ <= (y + height))
    //        return true;
    //    else
    //        return false;
    //}
    public void loadImages(String name, double scale){}
    public void loadFrames(ArrayList<BufferedImage> frames, String name, Color colorkey, double scale) {
        int image_x;
        try{
            image_x = Tool.PLANT_RECT.getJSONObject(name).getInt("x");
        }catch(Exception e){
            image_x = 0;
        }

        TreeSet<Tool.Img> frame_list = (TreeSet<Tool.Img>) Tool.GFX.get(name);
      
        for (Tool.Img frame : frame_list) {
            BufferedImage rect = frame.image;
            int width = rect.getWidth();
            int height = rect.getHeight();
            width -= image_x;
            // frames.add(Tool.adjustAlpha( frame.image,Constants.BLACK));
            //frames.add(Tool.resize(Tool.adjustAlpha(frame.image.getSubimage(image_x, 0, width, height),colorkey), scale));
            frames.add(Tool.adjustAlpha(Tool.resize(frame.image.getSubimage(image_x, 0, width, height),scale),colorkey));
            // tool.get_image(frame, image_x, 0, width, height, colorkey));
        }
    }

    public void changeFrames(ArrayList<BufferedImage> frames) {
        // '''change image frames&&modify rect position'''
        this.frames = frames;
        this.frame_num = this.frames.size();
        this.frame_index = 0;
        this.rect.image = this.frames.get(this.frame_index);
       
    }
  


    public void update(){
        // current_time = game_info[Constants.CURRENT_TIME];
        current_time = System.currentTimeMillis();
        handleState();
        animation();
    }

    public void handleState(){
        if(state.equals(Constants.IDLE))
            idling();
        else if(state.equals(Constants.ATTACK))
            attacking();
        else if(state.equals(Constants.DIGEST))
            digest();
    }
    public void idling(){}
    public void attacking(){}
    public void digest(){}
    public boolean needCry(Zombie zombie){return false;}
    public void setCry(){}



    public void animation(){
        if(current_time - animate_timer > animate_interval){
            frame_index = (frame_index+1)%frame_num;
            animate_timer = current_time;
        }

        this.rect.image = this.frames.get(this.frame_index);
        if(this.current_time - this.hit_timer >= 200)
            Tool.adjustBrightness(this.rect.image,255);
        else
            Tool.adjustBrightness(this.rect.image,192);
    }

    
    public boolean canAttack(Zombie zombie){
        if(!this.state.equals(Constants.SLEEP) && !zombie.state.equals(Constants.DIE) &&
        this.rect.left < zombie.rect.left + zombie.rect.width())
            return true;
        return false;
    }

    public void setAttack() {
        state = Constants.ATTACK;
    }

    public void setAttack(Zombie zombie, Group zombie_group){
        this.attack_zombie = zombie;
        this.zombie_group = zombie_group;
        this.state = c.ATTACK;
        this.changeFrames(this.attack_frames);
    }
    
    
    public void setIdle(){
        state = Constants.IDLE;
    }
    public void setSleep(){
        state = Constants.SLEEP;
        changeFrames(sleep_frames);
    }


    public void setDamage(int damage){
        health -= damage;
        hit_timer = current_time;
        if(health <= 0){
            state = Constants.DIE;
            //kill_zombie = zombie;
        }
    }
    
    public int gethealth(){
        return health;
    }

    

    public String getState(){
        return state;
    }

    public void setState(String newState){
       state = newState;
    }
}

class c extends Constants {};