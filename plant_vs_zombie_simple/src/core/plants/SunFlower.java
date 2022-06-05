package core.plants;

import core.*;
import core.plants.Sun;
import core.game.Group;

public class SunFlower extends Plant{
    long sun_timer = 0;
    Group sun_group;
    
    public SunFlower(int x, int y, Group sun_group){
        super(Constants.PLANT_HEALTH, x, y, Constants.SUNFLOWER, 1);    
        sun_timer = 0;
        this.sun_group = sun_group;   
    }

    public void loadImages(String name, double scale){
        loadFrames(frames, name,  Constants.BLACK);
    }

    public void idling(){
        if(sun_timer == 0)
            sun_timer = current_time - (Constants.FLOWER_SUN_INTERVAL - 6000);
        else if(current_time - sun_timer > Constants.FLOWER_SUN_INTERVAL){
            //this.sun_group.add(Sun(this.rect.centerx(), this.rect.bottom(), this.rect.right(), this.rect.bottom(), Constants.BIG_SUN_SCALE));
            sun_timer = current_time;
        }
    }
}
