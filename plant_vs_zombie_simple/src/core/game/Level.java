package core.game;
import core.State;
import core.Constants;
import java.util.Map;
import core.component.GameMap;

class c extends Constants {}

public class Level extends State {
    private Map<String, Double> gameInfo;
    private Map<String, Double> persist;
    private int mapYLen;
    private GameMap map;
    
    public Level() {
        super();
    }
    public void startUp(double currentTime, Map<String, Double> persist) {
        gameInfo = persist;
        this.persist = persist;
        gameInfo.remove(c.CURRENT_TIME);
        gameInfo.put(c.CURRENT_TIME, currentTime);
        mapYLen = c.GRID_Y_LEN;
        map = new GameMap(c.GRID_X_LEN, mapYLen);
        loadMap();
        setupBackgroud();
        initState();
    }
    public void loadMap() {
        String mapFile = "level_" + (gameInfo.get(c.LEVEL_NUM)) + ".json";

    }
    public void setupBackgroud() {

    }
    public void initState() {
        
    }
}
