package core.screen;


public class GameVictoryScreen extends Screen {
    public GameVictoryScreen() {
        super();
    }
    
    public String getImageName() {
        return c.GAME_VICTORY_IMAGE;
    }
    
    public String set_next_state() {
        return c.LEVEL;
    }


}

