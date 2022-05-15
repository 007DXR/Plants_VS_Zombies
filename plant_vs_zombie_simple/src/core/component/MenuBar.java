package core.component;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


import java.awt.*;

public class MenuBar {
    public int x = 10;
    public int y = 0;
    public int width;
    public int height;
    public int sun_value;
    public int card_offset_x = 32;
    public BufferedImage image;
    public int current_time;
    public ArrayList<Card> card_list;
    public int value_x;
    public int value_y;

    public MenuBar(int[] card_list, int sun_value) {
        String root = "plant_vs_zombie_simple/resources/graphics/Screen/";

        String menu_image_path = root + c.MENUBAR_BACKGROUND + ".png";
        image = Card.loadImage(menu_image_path, 1.0, c.WHITE);
        width = image.getWidth();
        height = image.getHeight();

        this.sun_value = sun_value;
        setupCards(card_list);
    }
    
    // 更新状态
    public void update(int current_time) {
        this.current_time = current_time;
        for (Card card : card_list) {
            card.update(sun_value, current_time);
        }
    }

    // 设置卡片
    public void setupCards(int[] card_list) {
        int x = card_offset_x;
        int y = 8;
        for (int index : card_list) {
            x += 55;
            this.card_list.add(new Card(x, y, index, 0.78));
        }
    }
    
    // 判断点击到卡片
    public Card checkCardClick(int x, int y) {
        Card result = null;
        for (Card card : card_list) {
            if (card.checkMouseClick(x, y)) {
                if (card.canClick(sun_value, current_time)) {
                    result = card;
                }
                break;
            }
        }
        return result;
    }

    // 判断点击到菜单
    public boolean checkMenuBarClick(int x, int y) {
        if (x >= this.x && x <= (this.x + width) && y >= this.y && y <= (this.y + height))
            return true;
        else
            return false;
    }
    
    // 减少阳光
    public void decreaseSunValue(int value)
    {
        sun_value -= value;
    }

    // 增加阳光
    public void increaseSunValue(int value) {
        sun_value += value;
    }

    // 设置卡片冷却
    public void setCardFrozenTime(String plant_name) {
        for (Card card : card_list) {
            if (c.plant_name_list[card.name_index].equals(plant_name)) {
                card.setFrozenTime(current_time);
                break;
            }
        }
    }

    // 画阳光数值
    public void drawSunValue() {
        BufferedImage value_image = Panel.getSunValueImage(sun_value);
        value_x = 21;
        value_y = y + height - 21;
        value_image.getGraphics().drawImage(value_image, value_x, value_y, null);
    }

    // 绘画
    public void paintObject(Graphics g) {
        drawSunValue();
        g.drawImage(image, x, y, null);
        for(Card card : card_list)
            card.paintObject(g);
    }
}
