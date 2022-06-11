package core.plants;

import core.*;

import java.util.ArrayList;

public class SunFlower extends Plant{
    long sun_timer = 0;
    ArrayList<Sun> sun_group;
    
    public SunFlower(int x, int y, ArrayList<Sun> sunGroup){
        super(Constants.PLANT_HEALTH, x, y, Constants.SUNFLOWER, 1);
        sun_timer = 0;
        this.sun_group = sunGroup;   
    }

    public void loadImages(String name, double scale){
        loadFrames(frames, name,  Constants.BLACK, 1);
    }

    public void idling(){
        /* 以一定时间间隔生产阳光 */
        if(sun_timer == 0)
            sun_timer = current_time - (Constants.FLOWER_SUN_INTERVAL - 6000);
        else if(current_time - sun_timer > Constants.FLOWER_SUN_INTERVAL){
            this.sun_group.add(new Sun(this.rect.centerx(), this.rect.centery(), this.rect.right(), this.rect.centery(), Constants.BIG_SUN_SCALE));
            sun_timer = current_time;
        }
    }
}
