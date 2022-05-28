package core.plants;

import java.io.File;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import core.zombies.Zombie;
import core.bullets.Bullet;

import core.Constants;
import core.*;

class PuffShroom extends Plant{
    private int shoot_timer = 0; 
    private List<Bullet> bullet_group; 
    boolean can_sleep = true;
    public PuffShroom(int x, int y, boolean day){
        super(Constants.PLANT_HEALTH, x, y, Constants.PuffShroom, 1);
        this.shoot_timer = 0; 
        if(day == true){setSleep();}
    }


    public void attacking(){
        if (this.current_time - this.shoot_timer > 3000){
            this.bullet_group.add(Bullet(x, y+10, y+10, Constants.BULLET_MUSHROOM, Constants.BULLET_DAMAGE_NORMAL, true)); 
            this.shoot_timer = this.current_time; 
        }
    }

    public boolean canAttack(Zombie zombie){
        if (this.x <= zombie.x && (this.x + c.GRID_X_SIZE * 4 >= zombie.x)){return true;}
        return false; 
    }

    public void loadImages(String name, double scale){
        String idle_name = name;
        String sleep_name = name + "Sleep";
        this.idle_frames.clear();;
        this.sleep_frames.clear();
        ArrayList<BufferedImage> frame_list;
        frame_list.addAll(this.idle_frames);
        frame_list.addAll(this.sleep_frames);
        List<String> name_list;
        name_list.add(idle_name);
        name_list.add(sleep_name); 
        for (int i = 0; i < name_list.getItemCount; i++){
            loadFrames(frame_list.get(i), name_list[i], 1); 
        }
        this.frames = this.idle_frames; 
    }
}
