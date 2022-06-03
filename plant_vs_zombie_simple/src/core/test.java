package core;

import core.game.*;
import core.json.JSONObject;

import java.util.*;

import javax.swing.*;

public class test {
    public static Control game;
    public static JFrame window;
    public static JPanel surface;
    
    public static void main(String[] args) {
        window = new JFrame();
        surface = new JPanel();
        game = new Control();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(816, 638);
        window.add(surface);
        window.setVisible(true);
        window.repaint();
        JSONObject state_dict = new JSONObject();
        // state_dict.put(c.MAIN_MENU, new MainMenu());
        // state_dict.put(c.GAME_VICTORY, new GameVictoryScreen());
        // state_dict.put(c.GAME_LOSE, new GameLoseScreen());
        state_dict.put(c.LEVEL, new Level());
        ArrayList<String> state_array = new ArrayList();
        state_array.add(c.MAIN_MENU);
        state_array.add(c.GAME_VICTORY);
        state_array.add(c.GAME_LOSE);
        state_array.add(c.LEVEL);
        game.setup_states(state_dict, state_array, c.MAIN_MENU);
        game.main();
    }
}
