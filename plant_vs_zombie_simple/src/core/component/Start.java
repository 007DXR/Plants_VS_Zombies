package core.component;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import core.Constants;

import core.Tool;

public class Start {
    public int x;
    public int y;
    public int width;
    public int height;
    // 卡片的名字序号
    public boolean select = true;
    public String name;
    public BufferedImage image;
    public BufferedImage orig_image;
    
    public Start(int x_, int y_,  double scale) {
        x = x_;
        y = y_;
        name = c.OPTION_ADVENTURE;
        // image = loadImage("../resources/graphics/Cards/" + name + ".png");
        orig_image = Tool.loadImage("resources/graphics/Screen/" + name + "_0.png",scale, c.BLACK);
        image = orig_image;
        width = image.getWidth();
        height = image.getHeight();
    }

    public boolean checkMouseClick(int x_, int y_) {
        if (x_ >= x && x_ <= (x + width) && y_ >= y && y_ <= (y + height))
            return true;
        else
            return false;
    }

    public boolean canSelect() {
        return select;
    }

    public void setSelect(boolean can_select) {
        select = can_select;
        if (can_select)
            image = Tool.adjustBrightness(orig_image, 255);
        else
            image = Tool.adjustBrightness(orig_image, 128);
    }

    public void paintObject(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    
}

