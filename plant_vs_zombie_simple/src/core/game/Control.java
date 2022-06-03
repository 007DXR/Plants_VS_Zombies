package core.game;

import core.Constants;
import core.State;
import core.json.JSONArray;
import core.json.JSONObject;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Control {
    public int state_index;
    public JFrame screen;
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

    public Control(JFrame g) {
        this.state_index = 0;
        this.screen = g;
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
        this.state.startUp(this.current_time, this.game_info);
    }

    public void update() {
        this.current_time = (int)System.currentTimeMillis();
        if (this.state.done) {
            this.flip_state();
        }
        this.state.update(this.screen, this.current_time, this.mouse_pos, this.mouse_click);
        this.mousePos = null;
        mouseClick = new ArrayList<>();
        mouseClick.add(false);
        mouseClick.add(false);
    }

    public void flip_state() {
        this.state_name = this.state_array.get(++state_index);
        JSONObject persist = this.state.cleanUp();
        this.state = (State)this.state_dict.get(this.state_name);
        this.state.startUp(this.current_time, persist);
    }

    public void event_loop() {
        for event in pg.event.get() {
            if event.type == pg.QUIT {
                this.done = true;
            }
            else if event.type == pg.KEYDOWN {
                this.keys = pg.key.get_pressed();
            }
            else if event.type == pg.KEYUP {
                this.keys = pg.key.get_pressed();
            }
            else if event.type == pg.MOUSEBUTTONDOWN {
                this.mouse_pos = pg.mouse.get_pos();
                this.mouse_click[0], _, this.mouse_click[1] = pg.mouse.get_pressed();
                System.out.println("pos:", this.mouse_pos, " mouse:", this.mouse_click);
            }
        }
    }

    public void main() {
        while (!this.done) {
            this.event_loop();
            this.update();
            screen.repaint();
//            this.clock.tick(this.fps);
        }
        System.out.println("game over");
    }
}

class c extends Constants {};