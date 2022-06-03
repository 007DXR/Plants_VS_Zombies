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

class SnowPeaShooter extends Plant{
    private long shoot_timer = 0; 
    private List<Bullet> bullet_group; 
    public SnowPeaShooter(int x, int y){
        super(Constants.PLANT_HEALTH, x, y, Constants.SNOWPEASHOOTER, 1); 
        this.shoot_timer = 0; 
    }

    public void attacking(){
        if (this.current_time - this.shoot_timer > 2000){
            Bullet bullet = new Bullet(this.rect.left + this.rect.width(), this.rect.centery(), dest_y, Constants.BULLET_PEA, Constants.BULLET_DAMAGE_NORMAL, false); 
            this.bullet_group.add(bullet); 
            this.shoot_timer = this.current_time; 
        }
    }
}
