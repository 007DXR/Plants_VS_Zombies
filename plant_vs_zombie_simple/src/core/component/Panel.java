package core.component;

import java.util.ArrayList;

import java.awt.image.BufferedImage;
import java.awt.*;
import core.game.PaintItf;

public class Panel 
    implements PaintItf{
    public ArrayList<Card> selected_cards = new ArrayList<>();
    public ArrayList<Card> card_list = new ArrayList<>();
    public int selected_num = 0;
    public BufferedImage menu_image;
    public BufferedImage panel_image;
    public BufferedImage value_image;
    public BufferedImage button_image;
    public int button_x =155;
    public int button_y =547;
    public int button_width;
    public int button_height;
    public int menu_x = 0;
    public int menu_y = 0;
    public int menu_width;
    public int menu_height;
    public int panel_x = 0;
    public int panel_y = c.PANEL_Y_START;
    public int panel_width;
    public int panel_height;
    public int value_x = 21;
    public int value_y;
    public int value_width;
    public int value_height;
    
    public Panel(int[] card_list, int sun_value) {
        loadImage(sun_value);
        setupCards(card_list);
    }
    
    // 载入图片
    public void loadImage(int sun_value) {
        // String root = "../resources/graphics/Cards/";
        String root = "resources/graphics/Screen/";

        String menu_image_path = root + c.MENUBAR_BACKGROUND+".png";
        menu_image = Card.loadImage(menu_image_path, 1.0, c.WHITE);
        menu_width = menu_image.getWidth();
        menu_height = menu_image.getHeight();

        String panel_image_path = root + c.PANEL_BACKGROUND+".png";
        panel_image = Card.loadImage(panel_image_path, 1.0, c.WHITE);
        panel_width = panel_image.getWidth();
        panel_height = panel_image.getHeight();

        value_image = getSunValueImage(sun_value);
        value_y = menu_y + menu_height - 21;
        value_width = value_image.getWidth();
        value_height = value_image.getHeight();

        String button_image_path = root + c.START_BUTTON+".png";
        button_image = Card.loadImage(button_image_path, 1.0, c.WHITE);
        button_width = button_image.getWidth();
        button_height = button_image.getHeight();
    }
    
    public static BufferedImage getSunValueImage(int sun_value){
        BufferedImage e = new BufferedImage(32, 17, BufferedImage.TYPE_INT_ARGB);
        Graphics g = e.getGraphics();
        g.setColor(c.LIGHTYELLOW);
        g.fillRect(0, 0, 32, 17);
        g.setColor(c.NAVYBLUE);
        g.setFont(new Font("楷体", Font.PLAIN, 22));
        g.drawString(String.valueOf(sun_value), 4, 16);
        return e;
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
    
    // 删除卡片
    public void deleteCard(int index) {
        card_list.get(index).setSelect(true);
    }

    // 添加卡片 
    public void addCard(Card card) {
        card.setSelect(false);
        int y = 8;
        int x = 78 + selected_num * 55;
        selected_cards.add(new Card(x, y, card.name_index, 0.78));
        selected_num += 1;
    }

    // 判断点击面板
    public void checkCardClick(int x, int y) {
        Card deleted_card = null;
        for (Card card : selected_cards) {
            if (deleted_card != null)
                card.x -= 55;
            else if (card.checkMouseClick(x, y)) {
                deleteCard(card.name_index);
                deleted_card = card;
            }
        }

        if (deleted_card != null) {
            selected_cards.remove(deleted_card);
            selected_num -= 1;
        }

        if (selected_num == c.CARD_LIST_NUM)
            return;

        for (Card card : card_list) {
            if (card.checkMouseClick(x, y)) {
                if (card.canSelect())
                    addCard(card);
                break;
            }
        }
    }
    
    // 判断点击按钮
    public boolean checkStartButtonClick(int x, int y) {
        if (selected_num < c.CARD_LIST_NUM)
            return false;
        if ((x >= button_x) && (x <= (button_x + button_width)) && (y >= button_y)
                && (y <= (button_y + button_height))) {
            return true;
        } else
            return false;
    }
    
    // 得到选中的卡片
    public int[] getSelectedCards() {
        int[] card_index_list = new int[selected_num];
        int i = 0;
        for (Card card : selected_cards) {
            card_index_list[i] = card.name_index;
            i++;
        }
        return card_index_list;
    }
    
    // 画图
    public void paintObject(Graphics g) {
        menu_image.getGraphics().drawImage(value_image, value_x, value_y, null);
        g.drawImage(menu_image, menu_x, menu_y, null);
        g.drawImage(panel_image, panel_x, panel_y, null);
        for(Card card : card_list)
            card.paintObject(g);
        for(Card card: selected_cards)
            card.paintObject(g);
        if (selected_num == c.CARD_LIST_NUM) {
            g.drawImage(button_image, button_x, button_y, null);
        }
    }
}

