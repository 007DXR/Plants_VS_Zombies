package core;
import java.util.ArrayList;
import java.util.Map;
import java.awt.Graphics;

import core.json.JSONObject;

public class State {
    public long start_time;
    public long current_time;
    public long end_time;
    public boolean done;
    public String next;
    public JSONObject persist;
    public JSONObject game_info;

    
    public  State() {
        start_time = 0;
        current_time = 0;
        done = false;
        next = null;
        persist = new JSONObject();
        //not initialize persist
    }
    
    public void update(Graphics g, int time, ArrayList<Integer> mousePos, ArrayList<Boolean> mouseClick) {}

    public void startUp(long currentTime, JSONObject persist) {
    }
    public void draw(Graphics g){}
    public JSONObject cleanUp() {
        done = false;
        return persist;
    }
//    public void update(surface, keys, current_time)
}
