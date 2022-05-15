package core.component;

import java.util.ArrayList;

import core.constants;

import java.awt.image.BufferedImage;

public class Panel {
    public ArrayList<Card> selected_cards = new ArrayList<>();
    public ArrayList<Card> card_list = new ArrayList<>();
    public int selected_num = 0;
    public BufferedImage menu_image;
    public BufferedImage panel_image;
    public BufferedImage value_image;
    public BufferedImage button_image;
    
    public Panel(int[] card_list, int sun_value) {

    }
    
    // 初始化卡片列表
    public void setupCards(int[] card_list) {
        int x = c.PANEL_X_START - c.PANEL_X_INTERNAL;
        int y = c.PANEL_Y_START + 43 - c.PANEL_Y_INTERNAL;
        int i = 0;
        for (int index : card_list) {
            if (i % 8 == 0) {
                x = c.PANEL_X_START - c.PANEL_X_INTERNAL;
                y += c.PANEL_Y_INTERNAL;
            }
            x += c.PANEL_X_INTERNAL;
            this.card_list.add(new Card(x, y, index, 0.75));
            i++;
        }
    }
    
    public void checkCardClick(int x, int y) {
        Card deleted_card = null;
        for (Card card : selected_cards) {
            
        }
    }
}

class c extends constants{}
