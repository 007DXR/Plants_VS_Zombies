package core.plants;

import core.*;
import core.zombies.*;
import core.Constants;
import core.game.Group;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;

public class Chomper extends Plant {
    long animate_interval = 250;
    long digest_timer = 0;
    long digest_interval = 15000;
    Zombie attack_zombie = null;
    Group zombie_group = null;
    ArrayList<BufferedImage> digest_frames;
    ArrayList<BufferedImage> attack_frames;

    public Chomper(int x, int y){
        super(Constants.PLANT_HEALTH, x, y-50, Constants.CHOMPER, 1);
    }


    public void loadImages(String name, double scale){
        digest_frames = new ArrayList<BufferedImage>();
        attack_frames = new ArrayList<BufferedImage>();
        String idle_name = name;
        String attack_name = name + "Attack";
        String digest_name = name + "Digest";

        loadFrames(idle_frames, idle_name, Constants.BLACK, 1);
        loadFrames(attack_frames, attack_name, Constants.BLACK, 1);
        loadFrames(digest_frames, digest_name,Constants.BLACK, 1);

        this.frames = idle_frames;
    }

    @Override
    public boolean canAttack(Zombie zombie){
        if(getState().equals(Constants.IDLE) && !zombie.state.equals(Constants.DIGEST) &&
        this.rect.left <= zombie.rect.left+zombie.rect.height()&&
        this.rect.left + this.rect.width() + Constants.GRID_X_SIZE/3 >= zombie.rect.left)
            return true;
        return false;
    }

    @Override
    public void setIdle(){
        setState(Constants.IDLE);
        changeFrames(idle_frames);
    }

   
    public void setAttack(Zombie zombie, Group zombie_group){
        attack_zombie = zombie;
        this.zombie_group = zombie_group;
        setState(Constants.ATTACK);
        changeFrames(attack_frames);
    }

    public void setDigest(){
        setState(Constants.DIGEST);
        changeFrames(digest_frames);
    }

    @Override
    public void attacking(){
        if(frame_index == frame_num - 3){
            this.zombie_group.remove(attack_zombie);
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
            attack_zombie.kill();
            setIdle();
        }
    }
}

