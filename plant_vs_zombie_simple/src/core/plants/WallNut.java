package core.plants;

import core.*;

import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class WallNut extends Plant{
    int wallNut_state = 0;
    boolean cracked1 = false;
    boolean cracked2 = false;
    public ArrayList<BufferedImage> cracked1_frames;
    public ArrayList<BufferedImage> cracked2_frames;


    public WallNut(int x, int y){
        super(Constants.WALLNUT_HEALTH, x, y, Constants.WALLNUT, 1);
    }
    
    
    public void loadImages(String name, double scale){
        cracked1_frames = new ArrayList<BufferedImage>();
        cracked2_frames = new ArrayList<BufferedImage>();

        String cracked1_frames_name = this.name + "_cracked1";
        String cracked2_frames_name = this.name + "_cracked2";
   
        loadFrames(this.frames, name, Constants.WHITE, 1);
        loadFrames(this.cracked1_frames, cracked1_frames_name, Constants.WHITE, 1);
        loadFrames(this.cracked2_frames, cracked2_frames_name, Constants.WHITE, 1);
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
