package core.plants;

import core.Constants;

public class HypnoShroom extends Plant{
    boolean can_sleep = true;
    long animate_interval = 200;

    public HypnoShroom(int hp, int x, int y, boolean day){
        super(Constants.PLANT_HEALTH, x, y, Constants.HYPNOSHROOM, 1);
        
        if(day == true)
            setSleep();
    }

   
    public void loadImages(String name, double scale){
        String idle_name = name;
        String sleep_name = name + "Sleep";

        /*loadFrames(idle_frames, idle_name, int image_x,Color colorkey);
        loadFrames(sleep_frames, sleep_name, int image_x,Color colorkey);*/
    }
}
