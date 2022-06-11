package core.plants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;

import core.Constants;
import core.*;
import core.zombies.*;
import core.game.Group;
import core.game.Rect;
import core.game.Sprite;
import core.json.JSONObject; 


public class Plant extends Sprite{

    public int explode_y_range;
    public int explode_x_range;
    public double scale;
    public int frame_index=0;
    public int frame_num;
    public Zombie attack_zombie = null;
    public Group zombie_group = null;

    public BufferedImage image;

    public int health;

    public String state;
    public String name;
    public String old_state;

    public boolean can_sleep = false;//蘑菇是true

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

    public void loadImages(String name, double scale){}

    public void loadFrames(ArrayList<BufferedImage> frames, String name, Color colorkey, double scale) {
        /* 加载某状态下的植物所有图片 */
        int image_x, image_y, width, height;
        TreeSet<Tool.Img> frame_list = (TreeSet<Tool.Img>) Tool.GFX.get(name);

        if(Tool.PLANT_RECT.optJSONObject(name) != null){
            JSONObject data = Tool.PLANT_RECT.optJSONObject(name);
            image_x = data.getInt("x");
            image_y = data.getInt("y");
            width = data.getInt("width");
            height = data.getInt("height");
        }else{
            image_x = 0;
            image_y = 0;
            BufferedImage rect = frame_list.first().image;
            width = rect.getWidth();
            height = rect.getHeight();
        }

        width -= image_x;

        for (Tool.Img frame : frame_list) {
            frames.add(Tool.adjustAlpha(Tool.resize(frame.image.getSubimage(image_x, image_y, width, height),scale),colorkey));
        }
    }

    public void changeFrames(ArrayList<BufferedImage> frames) {
        this.frames = frames;
        this.frame_num = this.frames.size();
        this.frame_index = 0;
        this.rect.image = this.frames.get(this.frame_index);
       
    }
  


    public void update(){
        /* 时间状态和动画更新 */
        current_time = System.currentTimeMillis();
        handleState();
        animation();
    }

    public void handleState(){
        /* 根据状态判断植物动作 */
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
        /* 动画，主要实现换图和打击 */
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
        /* 根据状态和范围判断是否可以攻击 */
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
        /* 削减生命值并判断是否死亡 */
        health -= damage;
        hit_timer = current_time;
        if(health <= 0){
            state = Constants.DIE;
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