package core.plants;

import java.io.File;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import core.zombies.Zombie;
import core.bullets.Bullet;
import core.game.Group;
import java.util.List;

import core.Constants;
import core.*;

class  ScaredyShroom extends Plant{
    private long shoot_timer = 0; 
    private ArrayList<Bullet> bullet_group = new ArrayList<Bullet>(); 
    boolean can_sleep = true;
    ArrayList<BufferedImage> cry_frames;
    private int cry_x_range = c.GRID_X_SIZE * 2;
    
    public  ScaredyShroom(int x, int y, boolean day){
        super(Constants.PLANT_HEALTH, x, y, Constants.SCAREDYSHROOM, 1);
        this.shoot_timer = 0; 
        if(day == true){setSleep();};
    }

    public void setCry(){
        this.state = Constants.CRY;
        changeFrames(this.cry_frames);
    }

    public void setAttack(Zombie zombie, Group zombie_group){
        this.zombie_group = zombie_group;
        setState(Constants.ATTACK);
        changeFrames(attack_frames);
    }

    public void setIdle(){
        this.state = Constants.IDLE;
        changeFrames(this.idle_frames);
    }

    public void attacking(){
        if (this.current_time - this.shoot_timer > 2000){
            Bullet bullet = new Bullet(this.rect.left + this.rect.width(), this.rect.centery() +40, this.rect.centery() + 40, Constants.BULLET_MUSHROOM, Constants.BULLET_DAMAGE_NORMAL, true); 
            this.bullet_group.add(bullet); 
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
        // this.idle_frames.clear();
        // this.sleep_frames.clear();
        // this.cry_frames.clear();
        // ArrayList<BufferedImage> frame_list;
        // frame_list.addAll(this.idle_frames);
        // frame_list.addAll(this.cry_frames);
        // frame_list.addAll(this.sleep_frames);
        // ArrayList<String> name_list;
        // name_list.add(idle_name);
        // name_list.add(cry_name);
        // name_list.add(sleep_name); 
        // for (int i = 0; i < name_list.size(); i++){
        //     loadFrames(frame_list.get(i), name_list.get(i), 1,Constants.WHITE); 
        // }
        // this.frames = this.idle_frames; 
    }

}
