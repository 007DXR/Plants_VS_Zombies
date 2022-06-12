package core.screen;

public class GameLoseScreen extends Screen {
    
    public  GameLoseScreen() {
        super();
    }
    
    public String getImageName() {
        return c.GAME_LOOSE_IMAGE;
    }
    
    public String set_next_state() {
        return c.MAIN_MENU;
    }


}

