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

public class RepeaterPea extends Plant{
    private long shoot_timer = 0; 
    private Group bullet_group; 
    public RepeaterPea(int x, int y, Group g){
        super(Constants.PLANT_HEALTH, x, y, Constants.REPEATERPEA, 1); 
        this.shoot_timer = 0; 
        this.bullet_group = g;
    }

    public void attacking(){
        this.current_time = (int)System.currentTimeMillis();
        if (this.current_time - this.shoot_timer > 2000){
            Bullet bullet1 = new Bullet(this.rect.left + this.rect.width(), this.rect.bottom() -Constants.MAP_OFFSET_Y, this.rect.bottom() -Constants.MAP_OFFSET_Y, Constants.BULLET_PEA, Constants.BULLET_DAMAGE_NORMAL, false); 
            this.bullet_group.add(bullet1); 
            Bullet bullet2 = new Bullet(this.rect.left + this.rect.width() + 40, this.rect.bottom() -Constants.MAP_OFFSET_Y, this.rect.bottom() -Constants.MAP_OFFSET_Y, Constants.BULLET_PEA, Constants.BULLET_DAMAGE_NORMAL, false); 
            this.bullet_group.add(bullet2); 
            this.shoot_timer = this.current_time; 
        }
    }

    public void loadImages(String name, double scale){
        loadFrames(this.frames, name, Constants.BLACK, scale);
    }
}
