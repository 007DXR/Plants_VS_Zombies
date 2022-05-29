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

class RepeaterPea extends Plant{
    private int shoot_timer = 0; 
    private List<Bullet> bullet_group; 
    public RepeaterPea(int x, int y){
        super(Constants.PLANT_HEALTH, x, y, Constants.RepeaterPea, 1); 
        this.shoot_timer = 0; 
    }

    public void attacking(){
        if (this.current_time - this.shoot_timer > 2000){
            this.bullet_group.add(Bullet(this.rect.left + this.rect.width(), this.rect.centery(), this.rect.centery(), Constants.BULLET_PEA, Constants.BULLET_DAMAGE_NORMAL, false)); 
            this.bullet_group.add(Bullet(this.rect.left + this.rect.width() + 40, this.rect.centery(), this.rect.centery(), Constants.BULLET_PEA, Constants.BULLET_DAMAGE_NORMAL, false)); 
            this.shoot_timer = this.current_time; 
        }
    }
}
