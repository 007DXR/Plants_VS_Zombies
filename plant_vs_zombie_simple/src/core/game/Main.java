package core.game;
import java.util.ArrayList;

import javax.swing.JFrame;

import core.Constants;
import core.game.Control;
import core.json.JSONObject;

public class Main {
    public static Control game;
    public static JFrame window;
    public static void main() {
        window = new JFrame();
        game = new Control(window);
        JSONObject state_dict = new JSONObject();
        state_dict.put(c.MAIN_MENU, new MainMenu());
        state_dict.put(c.GAME_VICTORY, new GameVictoryScreen());
        state_dict.put(c.GAME_LOSE, new GameLoseScreen());
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
