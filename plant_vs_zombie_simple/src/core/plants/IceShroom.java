package core.plants;

import core.*;
import core.Constants;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;

public class IceShroom extends Plant {
    boolean can_sleep = true;
    boolean start_freeze = false;
    int [] orig_pos;
    ArrayList<BufferedImage> snow_frames;
    public ArrayList<BufferedImage> trap_frames;
    
    
    public IceShroom(int x, int y){
        super(Constants.PLANT_HEALTH, x, y, Constants.ICESHROOM, 1);
        orig_pos = new int[]{x,y};
    }

    @Override
    public void loadImages(String name, double scale){
        snow_frames = new ArrayList<BufferedImage>();
        trap_frames = new ArrayList<BufferedImage>();
        
        String idle_name = name;
        String snow_name = name + "Snow";
        String sleep_name = name + "Sleep";
        String trap_name = name + "Trap";
        
        loadFrames(idle_frames, idle_name, Tool.PLANT_RECT.getJSONObject(idle_name).getInt("x"), Constants.BLACK);
        loadFrames(sleep_frames, sleep_name, Tool.PLANT_RECT.getJSONObject(sleep_name).getInt("x"), Constants.BLACK);
        loadFrames(snow_frames, snow_name, Tool.PLANT_RECT.getJSONObject(snow_name).getInt("x"), Constants.BLACK);
        loadFrames(trap_frames, trap_name, Tool.PLANT_RECT.getJSONObject(trap_name).getInt("x"), Constants.BLACK);

        if(getState().equals(Constants.SLEEP))
            frames = sleep_frames;
        else
            frames = idle_frames;
    }

    public void setFreeze(){
        changeFrames(snow_frames);
        animate_timer = current_time;
        //移出屏幕使其消失？？？？
        this.rect.left = Constants.MAP_OFFSET_X;
        this.rect.top = Constants.MAP_OFFSET_Y;
        start_freeze = true;
    }

    @Override
    public void animation(){
        if(start_freeze == true){
            if(current_time - animate_timer > 500){
                frame_index +=1;
                if(frame_index >= frame_num){
                    setDamage(1000);
                    return;
                }
                animate_timer = current_time;                 
            }
        }
        else{
            if(current_time - animate_timer > 100){
                frame_index+=1;
                if(frame_index >= frame_num){
                    if(getState().equals(Constants.SLEEP))
                        frame_index = 0;
                    else{
                        setFreeze();
                        return;
                    }
                }
                animate_timer = current_time;
            }
        }
        image = frames.get(frame_index);
    }
    

    public int [] getPosition(){
        return orig_pos;
    }

}
