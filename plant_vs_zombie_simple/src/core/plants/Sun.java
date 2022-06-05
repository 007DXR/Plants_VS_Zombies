package core.plants;

import core.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Sun extends Plant{
    int dst_x;
    int dst_y;

    public int sun_value;
    long die_timer;

    double move_speed = 1;
    boolean is_big;

    public ArrayList<BufferedImage> small_frames;
    public ArrayList<BufferedImage> big_frames;
    
    public Sun(int x, int y, int dst_x, int dst_y, double scale){
        super(0, x, y, Constants.SUN, scale);
        if(scale == Constants.BIG_SUN_SCALE){
            //System.out.println("produce_sun");
            is_big = true;
            sun_value = Constants.SUN_VALUE;
        }
        else{
            //System.out.println("produce_small_sun");
            is_big = false;
            sun_value = Constants.SMALL_SUN_VALUE;
        }

        this.loadImages(Constants.SUN, scale);

        this.dst_x = dst_x;
        this.dst_y = dst_y;

        die_timer = 0;

    }

    public void loadImages(String name, double scale){
        small_frames = new ArrayList<BufferedImage>();
        big_frames = new ArrayList<BufferedImage>();
        loadFrames(big_frames, name, Constants.BLACK, Constants.BIG_SUN_SCALE);
        loadFrames(small_frames, name, Constants.BLACK, Constants.SMALL_SUN_SCALE);
        
        
        if(this.is_big)
            frames = big_frames;
        else{
            frames = small_frames;
        }
    }

    @Override
    public void handleState(){
        
        if(this.rect.left < dst_x)
            this.rect.left += move_speed;
        else if(this.rect.left > dst_x)
        this.rect.left -= move_speed;
        
        if(this.rect.top < dst_y)
            this.rect.top += move_speed;
        else if(this.rect.top > dst_y)
        this.rect.top -= move_speed;

        if(this.rect.left == dst_x && this.rect.top == dst_y){
            if(die_timer == 0)
                die_timer = current_time;
            else if(current_time - die_timer > Constants.SUN_LIVE_TIME){
                setState(Constants.DIE);
                this.kill();
            } 
        }
    }

    
    public boolean checkMouseClick(int x_, int y_){
        if(getState().equals(Constants.DIE))
            return false;
        if (x_ >= this.rect.left && x_ <= (this.rect.left + this.rect.width()) &&
         y_ <= this.rect.bottom() && y_ >= (this.rect.top)){
            setState(Constants.DIE);
            //阳光面板值++
            this.kill();
            return true;
        } 
        else
            return false;
    }



  
}
