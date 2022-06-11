package core.component;

import java.awt.*;
import java.awt.image.BufferedImage;
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
        name = Constants.OPTION_ADVENTURE;
        // image = loadImage("../resources/graphics/Cards/" + name + ".png");
        orig_image = Tool.loadImage("resources/graphics/Screen/" + name + "_0.png",scale, Constants.BLACK);
        image = orig_image;
        width = image.getWidth();
        height = image.getHeight();
    }

    public boolean checkMouseClick(int x_, int y_) { //确认点击
        if (x_ >= x && x_ <= (x + width) && y_ >= y && y_ <= (y + height))
            return true;
        else
            return false;
    }

    public boolean canSelect() { //返回是否可以选中
        return select;
    }

    public void setSelect(boolean can_select) { //设置选中后图标的透明度
        select = can_select;
        if (can_select)
            image = Tool.adjustBrightness(orig_image, 255);
        else
            image = Tool.adjustBrightness(orig_image, 128);
    }

    public void paintObject(Graphics g) { //绘图
        g.drawImage(image, x, y, null);
    }

    
}

