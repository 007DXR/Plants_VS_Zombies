package core.game;

import java.util.*;
import java.util.Timer;

import javax.swing.*;

import core.json.JSONObject;
import core.screen.GameLoseScreen;
import core.screen.GameVictoryScreen;
import core.screen.Screen;

import java.awt.Graphics;
import java.awt.event.*;
import java.awt.event.MouseEvent;

public class Main extends JPanel{
    public static Control game;
    public static JFrame window;
    public static JPanel surface;
    public static boolean left_click;
    public static boolean right_click;
    public static int x;
    public static int y;

    
    public static void main(String[] args) {
        window = new JFrame();
        surface = new Main();
        window.add(surface);
        game = new Control();
        JSONObject state_dict=new JSONObject();
        state_dict.put(c.GAME_VICTORY, new GameVictoryScreen());
        state_dict.put(c.GAME_LOSE, new GameLoseScreen());
        state_dict.put(c.LEVEL, new Level());
        game.setup_states(state_dict, c.LEVEL);


        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(816, 638);
        window.add(surface);
        window.setVisible(true);
        window.repaint();
        

        // 鼠标监听事件
        MouseAdapter l = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                if(e.getButton()==MouseEvent.BUTTON1)
                    left_click = true;
                if(e.getButton()==MouseEvent.BUTTON2)
                    right_click = true;
                System.out.printf("%d %d\n",x,y);
            };

            @Override
            public void mouseMoved(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                //System.out.printf("%d %d\n",x,y);
            }
        };
        surface.addMouseListener(l);
        surface.addMouseMotionListener(l);
        // HashMap<String,Object> state_dict = new HashMap<String,Object>();
        // state_dict.put(c.MAIN_MENU, new MainMenu());

        // 定时运行
        Timer timer = new Timer();
        int interval = 10;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // System.out.println(left_click);
                // System.out.println(x);
                // System.out.println(y);
                game.event_loop(x, y, left_click, right_click);
                window.repaint();
                left_click = false;
                right_click = false;
            }
        }, interval, interval);
    }
    
    @Override
    public void paint(Graphics g) {
        // TODO Auto-generated method stub
        game.update(g);
        super.paint(g);
        game.state.draw(g);
    }
}
