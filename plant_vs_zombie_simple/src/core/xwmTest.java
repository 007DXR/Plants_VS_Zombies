package core;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;

import core.component.*;


// 供熊伟民测试第一部分的代码，注意最后统一文件路径
public class test extends JPanel {
    static boolean click = false;
    static int x;
    static int y;
    static Panel panel = new Panel(c.all_card_list, 50);
    public static void main(String[] args) {
        test b = new test();
        JFrame c = new JFrame();
        c.add(b);
        c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c.setSize(816, 638);
        c.setVisible(true);
        c.repaint();
        MouseAdapter l =new MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent e) {
                click = true;
                x = e.getX();
                y = e.getY();
                // System.out.println(x);
                // System.out.println(y);
            };
        };
        b.addMouseListener(l);
        Timer timer = new Timer();
        int interval = 10;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (click) {
                    panel.checkCardClick(x, y);
                }
                click = false;
                c.repaint();
            }
        }, interval, interval);

        
    }

    @Override
    public void paint(Graphics g) {
        try{
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
        BufferedImage k = ImageIO
                .read(new File("resources\\graphics\\Items\\Background\\Background_0.jpg"));
        g.drawImage(k, 0, 0, null);
        panel.paintObject(g);
        // g.drawString("30", 10, 20);
        // g.drawImage(a.adjustAlpha(a.image, 128), 0, 0, null);
        }
        catch(Exception e){}
    }
}

class c extends Constants{}
