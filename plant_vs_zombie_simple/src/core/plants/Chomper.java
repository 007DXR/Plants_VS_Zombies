package core.plants;

import core.*;
import core.zombies.*;
import core.Constants;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;

public class Chomper extends Plant {
    long animate_interval = 250;
    long digest_timer = 0;
    long digest_interval = 15000;
    Zombie attack_zombie = null;
    Zombie [] zombie_group = null;
    ArrayList<BufferedImage> digest_frames;
    ArrayList<BufferedImage> attack_frames;

    public Chomper(int x, int y){
        super(Constants.PLANT_HEALTH, x, y, Constants.CHOMPER, 1);
    }

    public void loadImages(String name, double scale){
        String idle_name = name;
        String attack_name = name + "Attack";
        String digest_name = name + "Digest";

        /*loadFrames(idle_frames, idle_name, int image_x,Color colorkey);
        loadFrames(attack_frames, attack_name, int image_x,Color colorkey);
        loadFrames(digest_frames, digest_name, int image_x,Color colorkey);*/

        this.frames = idle_frames;
    }

    @Override
    public boolean canAttack(Zombie zombie){
        if(getState() == Constants.IDLE)
            return true;
        return false;
    }

    @Override
    public void setIdle(){
        setState(Constants.IDLE);
        changeFrames(idle_frames);
    }

   
    public void setAttack(Zombie zombie, Zombie [] zombie_group){
        attack_zombie = zombie;
        this.zombie_group = zombie_group;
        setAttack();
        changeFrames(attack_frames);
    }

    public void setDigest(){
        setState(Constants.DIGEST);
        changeFrames(digest_frames);
    }

    @Override
    public void attacking(){
        if(frame_index == frame_num - 3){
            //zombie_group.remove(attack_zombie);
        }
            
        if(frame_index + 1 == frame_num)
            setDigest();
    }

    @Override
    public void digest(){
        if(digest_timer == 0)
            digest_timer = current_time;
        else if(current_time - digest_timer > digest_interval){
            digest_timer = 0;
            //attack_zombie.kill();
            setIdle();
        }
    }
}

