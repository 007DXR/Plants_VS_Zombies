package core;
import java.util.Map;

import core.json.JSONObject;

public abstract class State {
    public int startTime;
    public int currentTime;
    public boolean done;
    public State next;
    public JSONObject persist;
    
    public void State() {
        startTime = 0;
        currentTime = 0;
        done = false;
        next = null;
        //not initialize persist
    }

    abstract public void startUp(double currentTime, JSONObject persist);
    public JSONObject cleanUp() {
        done = false;
        return persist;
    }
//    public void update(surface, keys, current_time)
}
