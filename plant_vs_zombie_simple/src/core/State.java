package core;
import java.util.Map;

public abstract class State {
    private double startTime;
    private double currentTime;
    private boolean done;
    private State next;
    private double[] persist;
    
    public void State() {
        startTime = 0.0;
        currentTime = 0.0;
        done = false;
        next = null;
        //not initialize persist
    }

    abstract public void startUp(double currentTime, Map<String, Double> persist);
    public double[] cleanUp() {
        done = false;
        return persist;
    }
//    public void update(surface, keys, current_time)
}
