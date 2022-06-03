package core;
import java.util.ArrayList;
import java.util.Map;
import java.awt.Graphics;

import core.json.JSONObject;

public abstract class State {
    public int start_time;
    public int current_time;
    public int end_time;
    public boolean done;
    public String next;
    public JSONObject persist;
    public JSONObject game_info;

    
    public void State() {
        start_time = 0;
        current_time = 0;
        done = false;
        next = null;
        persist = new JSONObject();
        //not initialize persist
    }
    
    public abstract void  update(Graphics surface, ArrayList<Integer> mousePos, int current_time);
    public abstract void  startup(int currentTime, JSONObject persist);
    public JSONObject cleanUp() {
        done = false;
        return persist;
    }
//    public void update(surface, keys, current_time)
}
