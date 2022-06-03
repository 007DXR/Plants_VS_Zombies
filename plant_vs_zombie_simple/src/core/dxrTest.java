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
import core.zombies.NormalZombie;
import core.zombies.Zombie;



public class dxrTest extends JPanel {
    static boolean click = false;
    static int x;
    static int y;
    static Zombie zombie=new NormalZombie(816, 300,null);
    // =new NormalZombie(x, y);
    // public void paintObject(Graphics g) {
    //     g.drawImage(zombie.image, zombie.x, zombie.y, null);
    // }
    // public TestDXR(){
    //     zombie=new NormalZombie(x, y);
    // }
    public static void main(String[] args) {
        dxrTest b = new dxrTest();
        JFrame c = new JFrame();
        c.add(b);
        c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c.setSize(816, 638);
        c.setVisible(true);
        c.repaint();
        // MouseAdapter l =new MouseAdapter(){
        //     public void mouseClicked(java.awt.event.MouseEvent e) {
        //         click = true;
        //         x = e.getX();
        //         y = e.getY();
        //         // System.out.println(x);
        //         // System.out.println(y);
        //     };
        // };
        
        Timer timer = new Timer();
        int interval = 10;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // if (click) {
                //     panel.checkCardClick(x, y);
                // }
                // click = false;
                // c.zombie.update( ) ;
                zombie.update();
                c.repaint();
            }
        }, interval, interval);
    }

    @Override
    public void paint(Graphics g) {
        try{
        // TODO Auto-generated method stub
        super.paint(g);
        g.drawImage(zombie.rect.image, zombie.rect.left, zombie.rect.top, null);

        }
        catch(Exception e){}
    }
}

class c extends Constants{}
