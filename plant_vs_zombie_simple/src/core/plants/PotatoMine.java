package core.plants;

import core.*;
import core.Constants;
import core.zombies.Zombie;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;

public class PotatoMine extends Plant{
    boolean is_init = true;
    long animate_interval = 300;
    long init_timer = 0;
    long bomb_timer = 0;
    int explode_y_range = 0;
    int explode_x_range = Constants.GRID_X_SIZE;

    ArrayList<BufferedImage> init_frames;
    ArrayList<BufferedImage> explode_frames;

    public PotatoMine(int x, int y){
        super(Constants.PLANT_HEALTH, x, y, Constants.POTATOMINE, 1);
    }

    @Override
    public void loadImages(String name, double scale){
        init_frames = new ArrayList<BufferedImage>();
        explode_frames = new ArrayList<BufferedImage>();

        String init_name = name + "Init";
        String idle_name = name;
        String explode_name = name + "Explode";

        
        loadFrames(init_frames, init_name, Constants.WHITE, 1);
        loadFrames(idle_frames, idle_name, Constants.WHITE, 1);
        loadFrames(explode_frames, explode_name, Constants.WHITE, 1);
        
        this.frames = init_frames;
    }

    @Override
    public void idling(){
        if(is_init){
            if(init_timer == 0){
                init_timer = current_time;
            }
            else if(current_time - init_timer > 15000){
                changeFrames(idle_frames);
                is_init = false;
            }
        }
    }

    @Override
    public boolean canAttack(Zombie zombie){
        if(is_init == false && 
        zombie.rect.right() >= this.rect.left &&
        zombie.rect.left - this.rect.left<= explode_x_range)
            return true;
        return false;
    }

    @Override
    public void attacking(){
        if(bomb_timer == 0){
            bomb_timer = current_time;
            changeFrames(explode_frames);
        }
        else if(current_time - bomb_timer > 500)
            setDamage(1000);
    }
}
