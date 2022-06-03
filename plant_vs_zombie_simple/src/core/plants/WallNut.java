package core.plants;

import core.Constants;

import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class WallNut extends Plant{
    int wallNut_state = 0;
    boolean cracked1 = false;
    boolean cracked2 = false;
    ArrayList<BufferedImage> cracked1_frames;
    ArrayList<BufferedImage> cracked2_frames;

    public WallNut(int x, int y){
        super(Constants.WALLNUT_HEALTH, x, y, Constants.WALLNUT, 1);
        this.load_images();
    }
    
    
    public void load_images(){
        String cracked1_frames_name = this.name + "_cracked1";
        String cracked2_frames_name = this.name + "_cracked2";
        //loadFrames(cracked1_frames, cracked1_frames_name, image_x, colorkey, scale);
        //loadFrames(cracked2_frames, cracked2_frames_name, image_x, colorkey, scale);
    }

    public void idling(){
        if(cracked1==false && gethealth() < Constants.WALLNUT_CRACKED1_HEALTH){
            changeFrames(cracked1_frames);
            cracked1 = true;
        }
        else if(cracked2==false && gethealth() < Constants.WALLNUT_CRACKED2_HEALTH){
            changeFrames(cracked2_frames);
            cracked2 = true;
        }
    }
}
