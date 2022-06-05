package core.bullets;

import java.io.File;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import core.zombies.Zombie;
import core.plants.Plant;
import core.game.Rect;
import core.game.Sprite; 

import core.Constants;
import core.*;


public class Bullet extends Sprite{
    public int damage; 
    public boolean ice; 
    //public int x;
    //public int y;
    public int dest_y; 
    public int y_vel; 
    public int x_vel; 
    public String state;
    public String name;
    public int explode_timer; 
    int current_time = 0;
    public int frame_index=0;
    public int frame_num;

    public ArrayList<BufferedImage> frames = new ArrayList<BufferedImage>();
    ArrayList<BufferedImage> fly_frames = new ArrayList<BufferedImage>();
    ArrayList<BufferedImage> explode_frames = new ArrayList<BufferedImage>();

    public Bullet(int x, int y, int dest_y, String name, int damage, boolean ice){
        this.dest_y = dest_y; 
        this.state = Constants.FLY;
        this.current_time = 0; 
        this.name = name; 
        this.ice = ice; 

        this.frame_num = this.frames.size();
        this.rect = new Rect(this.frames.get(this.frame_index),x,y);
    }

    public void loadImages(String name, double scale){
        //loadFrames(frames, name, int image_x,Color colorkey, scale);
        this.fly_frames.clear();
        this.explode_frames.clear();; 

        String flyname = this.name; 
        String explodename; 
        if (this.name.equals(Constants.BULLET_MUSHROOM)){
            explodename = "BulletMushRoomExplode"; 
        }
        else{
            explodename = "PeaNormalExplode";
        }
        this.loadFrames(this.fly_frames, flyname);
        this.loadFrames(this.explode_frames, explodename);
    }

    public void loadFrames(ArrayList<BufferedImage>frames,String name){
        TreeSet<Tool.Img> frame_list=(TreeSet<Tool.Img>) Tool.GFX.get(name);
        for (Tool.Img frame : frame_list) {
            BufferedImage rect = frame.image;
            int width = rect.getWidth();
            int height = rect.getHeight();
            width -= x_vel;
            frames.add(frame.image.getSubimage(x_vel, 0, width, height));
            // tool.get_image(frame, image_x, 0, width, height, colorkey));
        }
    }

    public void loadFrames(ArrayList<BufferedImage> frames, String name, Color colorkey) {
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
            frames.add(Tool.adjustAlpha(frame.image.getSubimage(image_x, 0, width, height),colorkey));
            // tool.get_image(frame, image_x, 0, width, height, colorkey));
        }
    }


    public void update(){
        this.current_time = (int)System.currentTimeMillis();; 
        if (this.state.equals(Constants.FLY )){ // 在飞
            if (this.rect.top != this.dest_y){
                this.rect.top += this.y_vel; 
                if (this.y_vel * (this.dest_y - this.rect.top) < 0){
                    this.rect.top = this.dest_y; 
                }
            }
            this.rect.left += this.x_vel;
            if (this.rect.left > Constants.SCREEN_WIDTH){
                this.state = Constants.DIE; //死亡
                this.kill(); 
            }
        }
        else if(this.state.equals(Constants.EXPLODE)){
            if (this.current_time - this.explode_timer > 500){
                this.state = Constants.DIE; 
                this.kill(); 
            }
        }
    }

    public void setExplode(){
        this.state = Constants.EXPLODE; 
        this.explode_timer = this.current_time; 
        this.frames = this.explode_frames; 
    }

}
