package core.car;

import java.io.File;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;
import javax.imageio.ImageIO;

import core.Constants;
import core.*;
import core.zombies.*;
import core.game.*; 

public class Car extends Sprite {
    public String state;
    
    public boolean dead;
    public long current_time;
    public int map_y;

    public int frame_index = 0;
    public ArrayList<BufferedImage> frames;

    public Car(int x, int y, int map_y) {
        TreeSet<Tool.Img> frame_list = (TreeSet<Tool.Img>) 
            Tool.GFX.get(Constants.CAR);

        this.rect = new Rect(Tool.adjustAlpha(frame_list.first().image,Constants.BLACK), x, y);
        this.rect.adjustbt(y);
        this.map_y = map_y;
        this.state = Constants.IDLE;
        this.dead = false;
    }

    public void update() {
        this.current_time = System.currentTimeMillis();
        if (this.state == Constants.WALK) {
            this.rect.left += 4;
        }
        if (this.rect.left > Constants.SCREEN_WIDTH)
            this.dead = true;
    }
    
    public void setWalk() {
        if (this.state == Constants.IDLE)
            this.state = Constants.WALK;
    }
}