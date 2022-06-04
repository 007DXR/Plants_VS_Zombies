package core.plants;

import core.*;
import core.Constants;
import core.zombies.Zombie;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;
import core.game.Group;

public class Squash extends Plant{
    long aim_timer = 0;
    int [] orig_pos;
    boolean squashing = false;
    ArrayList<BufferedImage> aim_frames;
    ArrayList<BufferedImage> attack_frames;

    Zombie attack_zombie = null;
    Group zombie_group = null;

    public Squash(int x, int y){
        super(Constants.PLANT_HEALTH, x, y, Constants.SQUASH, 1);
        orig_pos = new int[]{x,y};
    }

    @Override
    public void loadImages(String name, double scale){
        aim_frames = new ArrayList<BufferedImage>();
        attack_frames = new ArrayList<BufferedImage>();

        String idle_name = name;
        String aim_name = name + "Aim";
        String attack_name = name + "Attack";

        loadFrames(idle_frames, idle_name, Constants.BLACK);
        loadFrames(aim_frames, aim_name, Constants.BLACK);
        loadFrames(attack_frames, attack_name, Constants.BLACK);
        
        this.frames = idle_frames;
    }

    @Override
    public boolean canAttack(Zombie zombie){
        if(getState().equals(Constants.IDLE) &&
        this.rect.left < zombie.rect.left + zombie.rect.width() &&
        this.rect.left+this.rect.width() >=zombie.rect.left )
            return true;
        return false;
    }


    public void setAttack(Zombie zombie, Group zombie_group){
        this.attack_zombie = zombie;
        this.zombie_group = zombie_group;
        setState(Constants.ATTACK);
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
            this.rect.adjust(this.attack_zombie.rect.centerx(), this.rect.bottom());
            squashing = true;
            animate_interval = 300;
        }
    }

    public int [] getPosition(){
        return orig_pos;
    }
}
