package core.plants;

import core.*;

public class SunFlower extends Plant{
    long sun_timer = 0;
    Sun []sun_group;
    
    public SunFlower(int x, int y, Sun[] sun_group){
        super(Constants.PLANT_HEALTH, x, y, Constants.SUNFLOWER, 1);    
        sun_timer = 0;
        this.sun_group = sun_group;   
    }

    public void loadImages(String name, double scale){
        loadFrames(frames, name, Tool.PLANT_RECT.getJSONObject(name).getInt("x"), Constants.BLACK);
    }

    public void idling(){
        if(sun_timer == 0)
            sun_timer = current_time - (Constants.FLOWER_SUN_INTERVAL - 6000);
        else if(current_time - sun_timer > Constants.FLOWER_SUN_INTERVAL){
            //sun_group.add(Sun(getX(), getY(), true));
            sun_timer = current_time;
        }
    }
}
