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
import core.Constants;
import core.*;

public class PeaShooter extends Plant{
    private long shoot_timer = 0; 
    private Group bullet_group; 
    public PeaShooter(int x, int y, Group g){
        super(Constants.PLANT_HEALTH, x, y, Constants.PEASHOOTER, 1); 
        this.shoot_timer = 0; 
        this.bullet_group = g;
    }

    public void attacking(){
        if (this.current_time - this.shoot_timer > 2000){
            Bullet bullet = new Bullet(this.rect.left + this.rect.width(), this.rect.centery(), this.rect.centery(), Constants.BULLET_PEA, Constants.BULLET_DAMAGE_NORMAL, false); 
            this.bullet_group.add(bullet); 
            this.shoot_timer = this.current_time; 
        }
    }
}
