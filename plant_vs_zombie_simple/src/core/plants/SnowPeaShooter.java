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

public class SnowPeaShooter extends Plant{
    private long shoot_timer = 0; 
    private Group bullet_group; 
    public SnowPeaShooter(int x, int y, Group g){
        super(Constants.PLANT_HEALTH, x, y, Constants.SNOWPEASHOOTER, 1); 
        this.shoot_timer = 0;
        this.bullet_group = g; 
    }

    public void attacking(){
        this.current_time = (int)System.currentTimeMillis();
        if (this.current_time - this.shoot_timer > 2000){
            Bullet bullet = new Bullet(this.rect.left + this.rect.width(), this.rect.bottom() -Constants.MAP_OFFSET_Y, this.rect.bottom() -Constants.MAP_OFFSET_Y, Constants.BULLET_PEA_ICE, Constants.BULLET_DAMAGE_NORMAL, true); 
            this.bullet_group.add(bullet); 
            this.shoot_timer = this.current_time; 
        }
    }

    public void loadImages(String name, double scale){
        loadFrames(this.frames, name, Constants.BLACK);
    }
}
