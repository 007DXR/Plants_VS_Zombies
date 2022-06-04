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

        loadFrames(idle_frames, idle_name, Tool.PLANT_RECT.getJSONObject(idle_name).getInt("x"), Constants.BLACK);
        loadFrames(sleep_frames, sleep_name, Tool.PLANT_RECT.getJSONObject(sleep_name).getInt("x"), Constants.BLACK);
        loadFrames(big_frames, big_name, Tool.PLANT_RECT.getJSONObject(big_name).getInt("x"), Constants.BLACK);

        this.frames = idle_frames;

    }
    
}
