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

public class PuffShroom extends Plant{
    private long shoot_timer = 0; 
    private Group bullet_group; 
    boolean can_sleep = true;
    public PuffShroom(int x, int y, Group g){
        super(Constants.PLANT_HEALTH, x, y, Constants.PUFFSHROOM, 1);
        this.shoot_timer = 0; 
        this.bullet_group = g;
    }


    public void attacking(){
        this.current_time = (int)System.currentTimeMillis();
        if (this.current_time - this.shoot_timer > 3000){
            Bullet bullet = new Bullet(this.rect.left + this.rect.width(), this.rect.bottom() -Constants.MAP_OFFSET_Y + 45, this.rect.bottom() -Constants.MAP_OFFSET_Y + 45, Constants.BULLET_MUSHROOM, Constants.BULLET_DAMAGE_NORMAL, true); 
            this.bullet_group.add(bullet); 
            this.shoot_timer = this.current_time; 
        }
    }

    public boolean canAttack(Zombie zombie){
        if (this.rect.centerx() <= zombie.rect.left + zombie.rect.width() && (this.rect.centerx() + c.GRID_X_SIZE * 4 >= zombie.rect.centerx())){return true;}
        return false; 
    }

    public void loadImages(String name, double scale){
        loadFrames(this.frames, name, Constants.BLACK, scale);
        loadFrames(this.sleep_frames, name+"Sleep", Constants.BLACK, scale);
    }
}
