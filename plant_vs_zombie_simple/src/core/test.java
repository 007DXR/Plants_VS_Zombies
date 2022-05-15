package core;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.component.*;

public class test extends JPanel {
    Card a = new Card(100, 200, 1, 1.0);
    public static void main(String[] args) {
        test c = new test();
        JFrame b = new JFrame();
        b.add(c);
        b.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        b.setSize(500, 500);
        b.setVisible(true);
        c.repaint();
    }

    @Override
    public void paint(Graphics g) {
        // TODO Auto-generated method stub
        super.paint(g);
        BufferedImage e = a.createShowImage(0, -1000);
        g.drawImage(e, 0, 0, null);
        // g.drawImage(a.adjustAlpha(a.image, 128), 0, 0, null);
    }
}
