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

class RepeaterPea extends Plant{
    private long shoot_timer = 0; 
    private ArrayList<Bullet> bullet_group = new ArrayList<Bullet>(); 
    public RepeaterPea(int x, int y){
        super(Constants.PLANT_HEALTH, x, y, Constants.REPEATERPEA, 1); 
        this.shoot_timer = 0; 
    }

    public void attacking(){
        if (this.current_time - this.shoot_timer > 2000){
            Bullet bullet1 = new Bullet(this.rect.left + this.rect.width(), this.rect.centery(), this.rect.centery(), Constants.BULLET_PEA, Constants.BULLET_DAMAGE_NORMAL, false); 
            this.bullet_group.add(bullet1); 
            Bullet bullet2 = new Bullet(this.rect.left + this.rect.width() + 40, this.rect.centery(), this.rect.centery(), Constants.BULLET_PEA, Constants.BULLET_DAMAGE_NORMAL, false); 
            this.bullet_group.add(bullet2); 
            this.shoot_timer = this.current_time; 
        }
    }
}
