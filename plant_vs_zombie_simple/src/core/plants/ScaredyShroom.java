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

public class  ScaredyShroom extends Plant{
    private long shoot_timer = 0; 
    private Group bullet_group; 
    boolean can_sleep = true;
    ArrayList<BufferedImage> cry_frames;
    private int cry_x_range = c.GRID_X_SIZE * 2;
    
    public  ScaredyShroom(int x, int y, Group g){
        super(Constants.PLANT_HEALTH, x, y, Constants.SCAREDYSHROOM, 1);
        this.shoot_timer = 0; 
        this.bullet_group = g;
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
        if (!zombie.state.equals(Constants.DIE) && this.rect.centerx() <= zombie.rect.left + zombie.rect.width() &&
            this.rect.centerx() + this.cry_x_range > zombie.rect.centerx()){return true;}
        return false; 
    }

    public void loadImages(String name, double scale){
        loadFrames(this.frames, name, Constants.BLACK);
    }

}
