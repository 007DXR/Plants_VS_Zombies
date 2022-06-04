package core.plants;

import core.*;

public class Sun extends Plant{
    int dst_x;
    int dst_y;

    int sun_value;
    long die_timer;

    double move_speed = 1;
    boolean is_big;
    
    public Sun(int x, int y, int dst_x, int dst_y, double scale){
        super(0, x, y, Constants.SUN, scale);
        if(scale == Constants.BIG_SUN_SCALE){
            is_big = true;
            sun_value = Constants.SUN_VALUE;
        }
        else{
            is_big = false;
            sun_value = Constants.SMALL_SUN_VALUE;
        }

        this.dst_x = dst_x;
        this.dst_y = dst_y;

        die_timer = 0;

    }

    public void loadImages(String name, double scale){
        loadFrames(frames, name, Tool.PLANT_RECT.getJSONObject(name).getInt("x"), Constants.BLACK);
    }
    @Override
    public void handleState(){
        if(this.rect.centerx() < dst_x)
            this.rect.left += move_speed;
        else if(this.rect.centerx() > dst_x)
        this.rect.left -= move_speed;
        
        if(this.rect.bottom() < dst_y)
            this.rect.top += move_speed;
        else if(this.rect.bottom() > dst_x)
        this.rect.top -= move_speed;

        if(this.rect.centerx() == dst_x && this.rect.bottom() == dst_y){
            if(die_timer == 0)
                die_timer = current_time;
            else if(current_time - die_timer > Constants.SUN_LIVE_TIME){
                setState(Constants.DIE);
                //kill();
            } 
        }
    }

    
    public boolean checkMouseClick(int x_, int y_){
        if(getState().equals(Constants.DIE))
            return false;

        if (x_ >= this.rect.left && x_ <= (this.rect.left + this.rect.width()) &&
         y_ >= this.rect.bottom() && y_ <= (this.rect.top)){
            setState(Constants.DIE);
            //阳光面板值++
            //kill();
            return true;
        } 
        else
            return false;
    }



  
}
