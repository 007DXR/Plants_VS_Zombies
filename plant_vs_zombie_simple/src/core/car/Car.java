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
    public int current_time;
    public int map_y;

    public int frame_index = 0;
    public ArrayList<BufferedImage> frames;

    public Car(int x, int y, int map_y) {
        TreeSet<Tool.Img> frame_list = (TreeSet<Tool.Img>) 
            Tool.GFX.get(Constants.CAR);

        for (Tool.Img frame : frame_list) {
            BufferedImage rect = frame.image;
            int width = rect.getWidth();
            int height = rect.getHeight();
            width -= x;
            // frames.add(Tool.adjustAlpha( frame.image,Constants.BLACK));
            frames.add(frame.image.getSubimage(0, 0, width, height));
                // tool.get_image(frame, image_x, 0, width, height, colorkey));
        }
        this.rect = new Rect(this.frames.get(this.frame_index), x, y);
        this.rect.adjustbt(y);
        this.map_y = map_y;
        this.state = Constants.IDLE;
        this.dead = false;
    }

    public void update(int current_time) {
        this.current_time = current_time;
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