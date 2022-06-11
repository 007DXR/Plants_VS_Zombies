package core.plants;

import core.Constants;

import java.util.ArrayList;


public class SunShroom extends Plant{
    boolean can_sleep = true;
    boolean is_big = false;

    long sun_timer = 0;
    long change_timer = 0;
    long animate_interval = 200;

    ArrayList<Sun> sun_group;
    
    public SunShroom(int x, int y, ArrayList<Sun> sunGroup){
        super(Constants.PLANT_HEALTH, x, y, Constants.SUNSHROOM, Constants.SMALL_SUNSHROOM_SCALE);  
        this.sun_group = sunGroup;
    }

    @Override
    public void loadImages(String name, double scale){
        String idle_name = name;
        String big_name = name + "Big";
        String sleep_name = name + "Sleep";

        loadFrames(idle_frames, idle_name,  Constants.WHITE, 1);
        loadFrames(sleep_frames, sleep_name, Constants.WHITE, 1);
        loadFrames(big_frames, big_name,  Constants.WHITE, 1);

        this.frames = idle_frames;
    }

    @Override
    public void idling(){
        if(is_big == false){
            if(change_timer == 0)
                change_timer = current_time;
            else if(current_time - change_timer > 60000){
                changeFrames(big_frames);
                is_big = true;
            }
        }

        if(sun_timer == 0)
            sun_timer = current_time - (Constants.FLOWER_SUN_INTERVAL - 6000);
        else if(current_time - sun_timer > Constants.FLOWER_SUN_INTERVAL){
            if(is_big){
                this.sun_group.add(new Sun(this.rect.centerx(), this.rect.bottom(), this.rect.right(), this.rect.bottom(), Constants.BIG_SUN_SCALE));
            }
            else{
                this.sun_group.add(new Sun(this.rect.centerx(), this.rect.bottom(), this.rect.right(), this.rect.bottom(), Constants.SMALL_SUN_SCALE));
            }
            sun_timer = current_time;
        }
    }
    
}
