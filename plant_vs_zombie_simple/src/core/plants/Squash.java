package core.plants;

import core.*;
import core.Constants;
import core.zombies.Zombie;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;

public class Squash extends Plant{
    long aim_timer = 0;
    int [] orig_pos;
    boolean squashing = false;
    ArrayList<BufferedImage> aim_frames;
    ArrayList<BufferedImage> attack_frames;

    Zombie attack_zombie = null;
    Zombie []zombie_group = null;

    public Squash(int x, int y){
        super(Constants.PLANT_HEALTH, x, y, Constants.SQUASH, 1);
        orig_pos = new int[]{x,y};
    }

    @Override
    public void loadImages(String name, double scale){
        String idle_name = name;
        String aim_name = name + "Aim";
        String attack_name = name + "Attack";

        /*
        loadFrames(idle_frames, idle_name, int image_x,Color colorkey, 1);
        loadFrames(aim_frames, aim_name, int image_x,Color colorkey, 1);
        loadFrames(attack_frames, attack_name, int image_x,Color colorkey, 1);
        */
        this.frames = idle_frames;
    }

    @Override
    public boolean canAttack(Zombie zombie){
        if(getState() == Constants.IDLE && x > zombie.x && 
        (x+Constants.GRID_X_SIZE >= zombie.x))
            return true;
        return false;
    }

    @Override
    public void attacking(){
        if(squashing == true){
            if(frame_index == 2)
                //zombie_group.remove(attack_zombie);
            if(frame_index + 1 == frame_num){
                //attack_zombie.kill();
                setDamage(1000);
            }
        }
        else if(aim_timer == 0){
            aim_timer = current_time;
            changeFrames(aim_frames);
        }
        else if(current_time - aim_timer > 1000){
            changeFrames(attack_frames);
            x = attack_zombie.x;
            squashing = true;
            animate_interval = 300;
        }
    }


    @Override
    public int [] getPosition(){
        return orig_pos;
    }
}
