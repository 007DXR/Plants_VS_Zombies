package core;
import java.util.Map;

import core.json.JSONObject;

public abstract class State {
    public double startTime;
    public double currentTime;
    public boolean done;
    public State next;
    public double[] persist;
    
    public void State() {
        startTime = 0.0;
        currentTime = 0.0;
        done = false;
        next = null;
        //not initialize persist
    }

    abstract public void startUp(double currentTime, JSONObject persist);
    public double[] cleanUp() {
        done = false;
        return persist;
    }
//    public void update(surface, keys, current_time)
}
