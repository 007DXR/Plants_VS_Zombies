package core.plants;

import core.Constants;
import core.*;

public class HypnoShroom extends Plant{
    boolean can_sleep = true;
    long animate_interval = 200;

    public HypnoShroom(int x, int y){
        super(Constants.PLANT_HEALTH, x, y, Constants.HYPNOSHROOM, 1);
    }
   
    public void loadImages(String name, double scale){
        
        String idle_name = name;
        String sleep_name = name + "Sleep";
    
        loadFrames(idle_frames, idle_name, Constants.WHITE, 1);
        loadFrames(sleep_frames, sleep_name, Constants.WHITE, 1);

        if(getState().equals(Constants.SLEEP))
            frames = sleep_frames;
        else
            frames = idle_frames;
    }
}
