package core.plants;

import core.zombies.*;
import core.Constants;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;

public class Spikeweed extends Plant{
    long attack_timer = 0;
    long animate_interval = 200;
    Zombie [] zombie_group;
  
    public Spikeweed(int x, int y){
        super(Constants.PLANT_HEALTH, x, y, Constants.SPIKEWEED, 1);
    }
    

    @Override
    public void loadImages(String name, double scale){
        loadFrames(frames, name, 1, Constants.WHITE, scale);
    }

    @Override
    public void setIdle(){
        animate_interval = 200;
        setState(Constants.IDLE);
    }

    @Override
    public boolean canAttack(Zombie zombie){
        if(x <= zombie.x)
            return true;
        return false;
    }

    
    public void setAttack(Zombie [] zombie_group){
        this.zombie_group = zombie_group;
        animate_interval = 50;
        setState(Constants.ATTACK);
    }

    @Override
    public void attacking(){
        if(current_time - attack_timer > 2000){
            attack_timer = current_time;
            for(Zombie zombie: zombie_group){
                if(canAttack(zombie))
                    zombie.setDamage(1, false);
            }
        }
    }
}
