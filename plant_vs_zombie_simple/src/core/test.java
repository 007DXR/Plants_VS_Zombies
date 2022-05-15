package core;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.component.*;

public class test extends JPanel {
    Panel panel = new Panel(c.all_card_list, 50);
    public static void main(String[] args) {
        test b = new test();
        JFrame c = new JFrame();
        c.add(b);
        c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c.setSize(816, 638);
        c.setVisible(true);
        c.repaint();
    }

    @Override
    public void paint(Graphics g) {
        // TODO Auto-generated method stub
        super.paint(g);
        // BufferedImage e = new BufferedImage(32, 17, BufferedImage.TYPE_INT_ARGB);
        // // e.getGraphics().drawString("30", 10, 0);
        // Graphics g2 = e.getGraphics();
        // g2.setColor(new Color(234,233,171));
        // g2.fillRect(0, 0, 32, 17);
        // g2.setColor(new Color(60, 60, 100));
        // g2.setFont(new Font("楷体", Font.PLAIN, 22));
        // g2.drawString("aaa", 0, 12);
        panel.paintObject(g);
        // g.drawString("30", 10, 20);
        // g.drawImage(a.adjustAlpha(a.image, 128), 0, 0, null);
    }
}

class c extends constants{}
