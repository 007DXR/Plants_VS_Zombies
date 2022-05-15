package core.component;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import core.constants;

public class Card {
    // 卡片在画布中的位置
    public int x;
    public int y;
    public int width;
    public int height;
    // 卡片的名字序号
    public int name_index;
    public int sun_cost;
    public int frozen_time;
    public int frozen_timer;
    public int refresh_timer = 0;
    public boolean select = true;
    public String name;
    public BufferedImage image;
    public BufferedImage orig_image;
    
    public Card(int x_, int y_, int name_index_, double scale) {
        x = x_;
        y = y_;
        name_index = name_index_;
        sun_cost = c.plant_sun_list[name_index_];
        frozen_time = c.plant_frozen_time_list[name_index_];
        frozen_timer = -frozen_time;
        name = c.card_name_list[name_index];
        // image = loadImage("../resources/graphics/Cards/" + name + ".png");
        orig_image = loadImage("plant_vs_zombie_simple/resources/graphics/Cards/" + name + ".png",scale);
        image = orig_image;
        width = image.getWidth();
        height = image.getHeight();
    }
    
    // 判断鼠标点击
    public boolean checkMouseClick(int x_, int y_) {
        if (x_ >= x && x_ <= (x + width) && y_ >= y && y_ <= (y + height))
            return true;
        else
            return false;
    }
    
    // 加载图片
    public BufferedImage loadImage(String filename, double scale) {
        try {
            BufferedImage img = ImageIO.read(new File(filename));
            img = resize(img, scale);
            return img;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    
    // 判断能否选中
    public boolean canClick(int sun_value, int current_time) {
        if (sun_cost <= sun_value && (current_time - frozen_timer) > frozen_time)
            return true;
        else
            return false;
    }
    
    public boolean canSelect() {
        return select;
    }

    // 设置选中状态
    public void setSelect(boolean can_select) {
        select = can_select;
        if (can_select)
            image = adjustAlpha(image, 255);
        else
            image = adjustAlpha(image, 128);
    }
    
    // 进入冷却
    public void setFrozenTime(int current_time) {
        frozen_timer = current_time;
    }

    // 冷却中的图片生成
    public BufferedImage createShowImage(int sun_value, int current_time) {
        int time = current_time - frozen_timer;
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        if (time < frozen_time) {
            int frozen_height = (int) ((frozen_time - time) * 1.0 / frozen_time * height);
            for (int j = 0; j < height; ++j) {
                for (int i = 0; i < width; ++i) {
                    int rgb = orig_image.getRGB(i, j);
                    if (j < frozen_height) {
                        int R, G, B;
                        R = ((rgb >> 16) & 0xff) / 2;
                        G = ((rgb >> 8) & 0xff) / 2;
                        B = (rgb & 0xff) / 2;
                        rgb = ((255 & 0xff) << 24) | ((R & 0xff) << 16) | ((G & 0xff) << 8) | ((B & 0xff));
                        output.setRGB(i, j, rgb);
                    } else
                        output.setRGB(i, j, rgb);
                }
            }
        } else if (sun_cost > sun_value) {
            output = adjustAlpha(orig_image, 192);
        } else {
            output = orig_image;
        }
        return output;
    }
    
    // 更新状态
    public void update(int sun_value, int current_time) {
        if ((current_time - refresh_timer) >= 250) {
            image = createShowImage(sun_value, current_time);
            refresh_timer = current_time;
        }
    }

    public BufferedImage adjustAlpha(BufferedImage image_, int alpha) {
        int width = image_.getWidth();
        int height = image_.getHeight();
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int j1 = 0; j1 < height; ++j1) {
            for (int j2 = 0; j2 < width; ++j2) {
                int rgb = image_.getRGB(j2, j1);
                int R, G, B;
                R = ((rgb >> 16) & 0xff) * alpha / 256;
                G = ((rgb >> 8) & 0xff) * alpha / 256;
                B = (rgb & 0xff) * alpha / 256;
                rgb = ((255 & 0xff) << 24) | ((R & 0xff) << 16) | ((G & 0xff) << 8) | ((B & 0xff));
                output.setRGB(j2, j1, rgb);
            }
        }
        return output;
    }
    
    // 画图片
    public void paintObject(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    // 图片缩放
    public BufferedImage resize(BufferedImage image_, double scale) {
        int width = image_.getWidth();
        int height = image_.getHeight();
        int nwidth = (int) (width * scale);
        int nheight = (int) (height * scale);

        Image img2 = image_.getScaledInstance(nwidth, nheight, Image.SCALE_DEFAULT);
        BufferedImage output = new BufferedImage(nwidth, nheight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = output.createGraphics();
        graphics.drawImage(img2, 0, 0, null);
        graphics.dispose();
        return output;
    }
}

class c extends constants{}