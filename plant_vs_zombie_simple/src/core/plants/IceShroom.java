package core.plants;

import core.Constants;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class IceShroom extends Plant {
    boolean can_sleep = true;
    boolean start_freeze = false;
    int [] orig_pos;
    public ArrayList<BufferedImage> snow_frames;
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
        
        loadFrames(idle_frames, idle_name, Constants.WHITE, 1);
        loadFrames(sleep_frames, sleep_name, Constants.WHITE, 1);
        loadFrames(snow_frames, snow_name, Constants.WHITE, 1.5);
        loadFrames(trap_frames, trap_name, Constants.WHITE, 1);


        if(getState().equals(Constants.SLEEP))
            frames = sleep_frames;
        else
            frames = idle_frames;
    }

    public void setFreeze(){
        /* 冻结僵尸 */
        changeFrames(snow_frames);
        animate_timer = current_time;
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
