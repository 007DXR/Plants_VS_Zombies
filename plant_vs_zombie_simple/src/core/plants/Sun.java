package core.plants;

import core.Constants;

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

    @Override
    public void handleState(){
        if(x+width/2 < dst_x)
            x += move_speed;
        else if(x+width/2 > dst_x)
            x -= move_speed;
        
        if(y < dst_y)
            y += move_speed;
        else if(y > dst_x)
            y -= move_speed;

        if(x+width/2 == dst_x && y == dst_y){
            if(die_timer == 0)
                die_timer = current_time;
            else if(current_time - die_timer > Constants.SUN_LIVE_TIME){
                setState(Constants.DIE);
                //kill();
            } 
        }
    }

    @Override
    public boolean checkMouseClick(int x_, int y_){
        if(getState() == Constants.DIE)
            return false;

        if (x_ >= x && x_ <= (x + width) && y_ >= y && y_ <= (y + height)){
            setState(Constants.DIE);
            //阳光面板值++
            //kill();
            return true;
        } 
        else
            return false;
    }



  
}
