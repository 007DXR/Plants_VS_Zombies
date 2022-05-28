package core.plants;

import core.*;
import core.Constants;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;

public class SunShroom extends Plant{
    boolean can_sleep = true;
    boolean is_big = false;

    long sun_timer = 0;
    long change_timer = 0;
    long animate_interval = 200;

    Sun []sun_group;
    
    public SunShroom(int hp, int x, int y, boolean day){
        super(hp, x, y, Constants.SUNSHROOM, Constants.SMALL_SUNSHROOM_SCALE);   
        if(day == true){
            setSleep();
        }        
    }

    @Override
    public void loadImages(String name, double scale){
        String idle_name = name;
        String big_name = name + "Big";
        String sleep_name = name + "Sleep";

        /*loadFrames(idle_frames, idle_name, int image_x,Color colorkey, 1);
        loadFrames(big_frames, big_name, int image_x,Color colorkey, 1);
        loadFrames(sleep_frames, sleep_name, int image_x,Color colorkey, 1);*/

        this.frames = idle_frames;

    }
    
}
