package core.plants;

import java.io.File;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import core.zombies.Zombie;
import core.bullets.Bullet;
import java.util.List;

import core.Constants;
import core.*;

class PuffShroom extends Plant{
    private long shoot_timer = 0; 
    private ArrayList<Bullet> bullet_group = new ArrayList<Bullet>(); 
    boolean can_sleep = true;
    public PuffShroom(int x, int y){
        super(Constants.PLANT_HEALTH, x, y, Constants.PUFFSHROOM, 1);
        this.shoot_timer = 0; 
    }


    public void attacking(){
        if (this.current_time - this.shoot_timer > 3000){
            Bullet bullet = new Bullet(this.rect.left + this.rect.width(), this.rect.centery() + 10, this.rect.centery() + 10, Constants.BULLET_MUSHROOM, Constants.BULLET_DAMAGE_NORMAL, true); 
            this.bullet_group.add(bullet); 
            this.shoot_timer = this.current_time; 
        }
    }

    public boolean canAttack(Zombie zombie){
        if (this.rect.centerx() <= zombie.rect.left + zombie.rect.width() && (this.rect.centerx() + c.GRID_X_SIZE * 4 >= zombie.rect.centerx())){return true;}
        return false; 
    }

    public void loadImages(String name, double scale){
        String idle_name = name;
        String sleep_name = name + "Sleep";
        // this.idle_frames.clear();;
        // this.sleep_frames.clear();
        // ArrayList<BufferedImage> frame_list;
        // frame_list.addAll(this.idle_frames);
        // frame_list.addAll(this.sleep_frames);
        // ArrayList<String> name_list;
        // name_list.add(idle_name);
        // name_list.add(sleep_name); 
        // for (int i = 0; i < name_list.size(); i++){
        //     loadFrames(frame_list.get(i), name_list.get(i), 1); 
        // }
        // this.frames = this.idle_frames; 
    }
}
