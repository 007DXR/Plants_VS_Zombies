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

class  ScaredyShroom extends Plant{
    private int shoot_timer = 0; 
    private List<Bullet> bullet_group; 
    boolean can_sleep = true;
    private int cry_x_range = c.GRID_X_SIZE * 2;
    public  ScaredyShroom(int x, int y, boolean day){
        super(Constants.PLANT_HEALTH, x, y, Constants.ScaredyShroom, 1);
        this.shoot_timer = 0; 
        if(day == true){setSleep();};
    }

    public void setCry(){
        this.state = Constants.CRY;
        changeFrames(this.cry_frames);
    }

    public void setAttack(Zombie zombie, Group zombie_group){
        this.state = Constants.ATTACK;
        changeFrames(this.idle_frames);
    }

    public void setIdle(){
        this.state = Constants.IDLE;
        changeFrames(this.idle_frames);
    }

    public void attacking(){
        if (this.current_time - this.shoot_timer > 2000){
            this.bullet_group.add(Bullet(this.rect.left + this.rect.width(), this.rect.centery() +40, this.rect.centery() + 40, Constants.BULLET_MUSHROOM, Constants.BULLET_DAMAGE_NORMAL, true)); 
            this.shoot_timer = this.current_time; 
        }
    }

    public boolean needCry(Zombie zombie){
        if (zombie.state != Constants.DIE && this.rect.centerx() <= zombie.rect.left + zombie.rect.width() &&
            this.rect.centerx() + this.cry_x_range > zombie.rect.centerx()){return true;}
        return false; 
    }

    public void loadImages(String name, double scale){
        String idle_name = name;
        String cry_name = name + "Cry"; 
        String sleep_name = name + "Sleep";
        this.idle_frames.clear();
        this.sleep_frames.clear();
        this.cry_frames.clear();
        ArrayList<BufferedImage> frame_list;
        frame_list.addAll(this.idle_frames);
        frame_list.addAll(this.cry_frames);
        frame_list.addAll(this.sleep_frames);
        List<String> name_list;
        name_list.add(idle_name);
        name_list.add(cry_name);
        name_list.add(sleep_name); 
        for (int i = 0; i < name_list.getItemCount; i++){
            loadFrames(frame_list.get(i), name_list[i], 1,Constants.WHITE); 
        }
        this.frames = this.idle_frames; 
    }

}
