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

public class ThreePeaShooter extends Plant{
    private long shoot_timer = 0; 
    private int map_y; 
    private ArrayList<Group> bullet_group; 
    public ThreePeaShooter(int x, int y, ArrayList<Group> g, int map_y){
        super(Constants.PLANT_HEALTH, x, y, Constants.THREEPEASHOOTER, 1); 
        this.shoot_timer = 0; 
        this.bullet_group = g;
        this.map_y = map_y; 
    }

    public void attacking(){
        this.current_time = (int)System.currentTimeMillis();
        if (this.current_time - this.shoot_timer > 2000){
            int offset_y = 9; 
            for(int i = 0; i < 3; i++){
                int tmp_y = this.map_y + (i-1); 
                if (tmp_y < 0 || tmp_y >= Constants.GRID_Y_LEN){continue; }
                int dest_y = this.rect.bottom() -Constants.MAP_OFFSET_Y+20 + (i-1) * Constants.GRID_Y_SIZE + offset_y; 
                Bullet bullet = new Bullet(this.rect.left + this.rect.width(), this.rect.bottom() -Constants.MAP_OFFSET_Y+20, dest_y, Constants.BULLET_PEA, Constants.BULLET_DAMAGE_NORMAL, false); 
                this.bullet_group.get(tmp_y).add(bullet); 
            }
            this.shoot_timer = this.current_time; 
        }
    }

    public void loadImages(String name, double scale){
        loadFrames(this.frames, name, Constants.BLACK, scale);
    }
}
