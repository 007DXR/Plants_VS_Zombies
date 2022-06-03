package core.game;

import java.util.*;
import java.util.Timer;

import javax.swing.*;

import core.json.JSONObject;
import core.screen.GameLoseScreen;
import core.screen.GameVictoryScreen;

import java.awt.event.*;

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
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(816, 638);
        window.add(surface);
        window.setVisible(true);
        window.repaint();
        JSONObject state_dict = new JSONObject();
        // 计时器
        int current_time = (int)System.currentTimeMillis();
        // state_dict.put(c.MAIN_MENU, new MainMenu());
        state_dict.put(c.GAME_VICTORY, new GameVictoryScreen());
        state_dict.put(c.GAME_LOSE, new GameLoseScreen());

        // 鼠标监听事件
        MouseAdapter l = new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                x = e.getX();
                y = e.getY();
                switch(e.getModifiersEx()) {
                    case InputEvent.BUTTON1_DOWN_MASK: {
                        left_click = true;
                    }
                    case InputEvent.BUTTON3_DOWN_MASK: {
                        right_click = true;
                    }
                }
            };

            @Override
            public void mouseMoved(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        };
        surface.addMouseListener(l);

        state_dict.put(c.LEVEL, new Level());
        ArrayList<String> state_array = new ArrayList<>();
        state_array.add(c.MAIN_MENU);
        state_array.add(c.GAME_VICTORY);
        state_array.add(c.GAME_LOSE);
        state_array.add(c.LEVEL);
        game.setup_states(state_dict, state_array, c.LEVEL);
        
        // while (true) {
        //     left_click = false;
        //     right_click = false;
        //     game.event_loop(x, y, left_click, right_click);
        //     game.update();
        //     window.repaint();

        // }

        // 定时运行
        Timer timer = new Timer();
        int interval = 10;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                left_click = false;
                right_click = false;
                game.event_loop(x, y, left_click, right_click);
                game.update();
                window.repaint();
            }
        }, interval, interval);
    }
}