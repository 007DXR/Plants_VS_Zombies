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
import core.game.Sprite; 

/**
 * 需要修改的部分
 * 图片加载LoadFrames LoadImages
 * 碰撞检测 canAttack
 * cneterx和bottom要不要添加
 */

public class Plant extends Sprite{

    public double scale;
    public int frame_index=0;
    public int frame_num;

    public int width;
    public int height;
    public BufferedImage image;

    public int health;
    //public int x;
    //public int y;

    public String state;
    public String name;
    public String old_state;
    //public Zombie kill_zombie;
    boolean can_sleep = false;//蘑菇是true，全局判断给他setSleep

    long animate_interval = 100;
    long animate_timer = 0;
    long hit_timer = 0;
    long current_time = 0;

    public ArrayList<BufferedImage> frames;
    ArrayList<BufferedImage> sleep_frames;
    ArrayList<BufferedImage> idle_frames;
    ArrayList<BufferedImage> big_frames;

    // 因为继承之后的构造函数只要传入x和y，所以这里没有修改
    public Plant(int health, int x, int y, String name, double scale){
        super(); 
        this.health = health;
        this.loadImages();
        this.state = Constants.IDLE;
        this.name = name;
        this.scale = scale;

        //tool.loadImage();

        //loadFrames(frames, name, image_x, colorkey, scale);
        this.frame_num = this.frames.size();
        this.rect = new Rect(this.frames.get(this.frame_index),x,y);

    }
    
    public void loadImages(String name, double scale){
        //loadFrames(frames, name, int image_x,Color colorkey, scale);
    }


    // 判断鼠标点击
    //public boolean checkMouseClick(int x_, int y_) {
    //    if (x_ >= x && x_ <= (x + width) && y_ >= y && y_ <= (y + height))
    //        return true;
    //    else
    //        return false;
    //}
    
    public void loadFrames(ArrayList<BufferedImage>frames,String name,int image_x,Color colorkey, double scale)
    {   
        TreeSet<Tool.Img> frame_list=(TreeSet<Tool.Img>) Tool.GFX.get(name);
        for (Tool.Img frame : frame_list) {
            BufferedImage rect = frame.image;
            int width = rect.getWidth();
            int height = rect.getHeight();
            width -= image_x;
            frames.add(frame.image.getSubimage(image_x, 0, width, height));
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


    public void update(){
        // current_time = game_info[Constants.CURRENT_TIME];
        current_time = System.currentTimeMillis();
        handleState();
        animation();
    }

    public void handleState(){
        if(state == Constants.DIE)
            idling();
        else if(state == Constants.ATTACK)
            attacking();
        else if(state == Constants.DIGEST)
            digest();
    }
    public void idling(){}
    public void attacking(){}
    public void digest(){}



    public void animation(){
        if(current_time - animate_timer > animate_interval){
            frame_index = (frame_index+1)%frame_num;
            animate_timer = current_time;
        }

        this.rect.image = this.frames.get(this.frame_index);
        if(this.current_time - this.hit_timer >= 200)
            this.adjustBrightness(this.rect.image,255);
        else
            this.adjustBrightness(this.rect.image,192);
    }

    // 蘑菇和大嘴花应该要override这个函数
    // 或者还是像之前那样放在attack里面？因为除了这几种植物其他都是true
    // python代码里判断了碰撞和僵尸状态，但我们是不是可能放在全局里判断，暂时不写
    public boolean canAttack(Zombie zombie){
        return true;
    }

    public void setAttack(){
        state = Constants.ATTACK;
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
