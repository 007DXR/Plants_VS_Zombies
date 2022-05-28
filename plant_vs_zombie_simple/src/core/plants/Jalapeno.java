package core.plants;

import core.Constants;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Jalapeno extends Plant{
    int [] orig_pos;
    boolean start_explode = false;
    int explode_y_range = 0;
    int explode_x_range = 377;
    ArrayList<BufferedImage> explode_frames;

    public Jalapeno(int hp, int x, int y){
        super(Constants.PLANT_HEALTH, x, y, Constants.JALAPENO, 1);
        orig_pos = new int[]{x,y};
        setAttack();
    }

    @Override
    public void loadImages(String name, double scale){
        String explode_name = name + "Explode";
        //loadFrames(explode_frames, explode_name, image_x, colorkey, 1);
        //loadFrames(frames, name, image_x, colorkey, 1);
    }

    public void setExplode(){
        changeFrames(explode_frames);
        animate_timer = current_time;
        x = Constants.MAP_OFFSET_X;
        start_explode = true;
    }

    @Override
    public void animation(){
        if(start_explode == true){
            if(current_time - animate_timer > 100){
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
                    setExplode();
                    return;
                }
                animate_timer = current_time;
            }
        }
        image = frames.get(frame_index);
    }

    @Override
    public int [] getPosition(){
        return orig_pos;
    }
}
