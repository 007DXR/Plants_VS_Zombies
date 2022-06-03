public class GameLoseScreen {
    
    public void GameLoseScreen() {
        super();
    }
    
    public void getImageName(self) {
        return c.GAME_LOOSE_IMAGE;
    }
    
    def set_next_state(self):
        return c.MAIN_MENU
}

class c extends Constants {};