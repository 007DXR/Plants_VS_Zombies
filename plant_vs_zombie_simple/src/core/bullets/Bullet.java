package core.bullets;

import java.io.File;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import core.zombies.Zombie;

import core.Constants;
import core.*;


class Bullet{
    public int damage; 
    public boolean ice; 
    public int x;
    public int y;
    public int dest_y; 
    public int y_vel; 
    public int x_vel; 
    public String state;
    public String name;
    public int explode_timer; 
    long current_time = 0;

    public ArrayList<BufferedImage> frames;

    public Bullet(int x, int y, int dest_y, String name, int damage, boolean ice){
        this.hp = hp;
        this.x = x;
        this.y = y;
        this.dest_y = dest_y; 
        this.state = Constants.FLY;
        this.current_time = 0; 
        this.name = name; 
        this.ice = ice; 
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void loadFrames(ArrayList<BufferedImage>frames,String name){
        TreeSet<Tool.Img> frame_list=(TreeSet<Tool.Img>) Tool.GFX.get(name);
        for (Tool.Img frame : frame_list) {
            
            // frames.append(tool.get_image(frame, x, y, width, height))
        }
    }

    public void update(int current_time){
        this.current_time = current_time; 
        if (this.state == Constants.FLY ){ // 在飞
            if (this.y != this.dest_y){
                this.y += this.y_vel; 
                if (this.vel * (this.dest_y - this.y) < 0){
                    this.y = this.dest_y; 
                }
            }
            this.x += this.x_vel;
            if (this.x > Constants.SCRREN_WIDTH){
                this.state = Constants.DIE; //死亡
            }
        }
        else if(this.state == Constants.EXPLODE){
            if (this.current_time - this.eplode_timer > 500){
                this.state = Constants.DIE; 
            }
        }
    }

    public void setExplode(){
        this.state = 1; 
        this.explode_timer = this.current_time; 
    }

}
