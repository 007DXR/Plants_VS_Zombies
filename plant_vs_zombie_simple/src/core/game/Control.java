package core.game;

import core.Constants;
import core.State;
import core.json.JSONArray;
import core.json.JSONObject;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Control extends JPanel{
    public int state_index;
    public boolean done;
    public int clock;
    public int fps;
    public ArrayList<Integer> mousePos;
    // pair, [0]get left pressed state, [1] get right pressed state
    public ArrayList<Boolean> mouseClick;
    public int current_time;
    public JSONObject state_dict;
    /// state name array
    public ArrayList<String> state_array;
    public String state_name;
    public State state;
    public JSONObject game_info;

    public Control() {
        this.state_index = 0;
        this.done = false;
        this.clock = (int)System.currentTimeMillis();
        this.fps = 60;
//        this.keys = pg.key.get_pressed();
        // value:[left mouse click, right mouse click];
        this.mousePos = null;
        this.mouseClick = null;
        this.current_time = this.clock;
        this.state_dict = new JSONObject();
        this.state_array = new ArrayList<>();
        this.state_name = null;
        this.state = null;
        
        this.game_info = new JSONObject();
        game_info.put(c.CURRENT_TIME, 0);
        game_info.put(c.LEVEL_NUM, c.START_LEVEL_NUM);
    }
 
    public void setup_states(JSONObject state_dict, ArrayList<String> state_array, String start_state) {
        this.state_dict = state_dict;
        this.state_array = state_array;
        this.state_name = start_state;
        this.state = (State) this.state_dict.get(this.state_name);
        this.state.startup(this.current_time, this.game_info);
    }

    public void update() {
        this.current_time = (int)System.currentTimeMillis();
        if (this.state.done) {
            this.flip_state();
        }
        this.state.update(this.current_time, this.mousePos, this.mouseClick);
        this.mousePos = null;
        mouseClick = new ArrayList<>();
        mouseClick.add(false);
        mouseClick.add(false);
    }

    public void flip_state() {
        this.state_name = this.state_array.get(++state_index);
        JSONObject persist = this.state.cleanUp();
        this.state = (State)this.state_dict.get(this.state_name);
        this.state.startup(this.current_time, persist);
    }

    // 更新鼠标状况
    public void event_loop(int x, int y, boolean left_click, boolean right_click) {
        mousePos.set(0, x);
        mousePos.set(1, y);
        mouseClick.set(0, left_click);
        mouseClick.set(1, right_click);
    }

//     public void main() {
//         while (!this.done) {
//             // this.event_loop();
//             this.update();
//             screen.repaint();
// //            this.clock.tick(this.fps);
//         }
//         System.out.println("game over");
//     }
}

class c extends Constants {};