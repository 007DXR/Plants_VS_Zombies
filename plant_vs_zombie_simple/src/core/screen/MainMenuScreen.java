package core.screen;

import core.*;
import core.json.JSONObject;

public class MainMenuScreen extends Screen {
    public MainMenuScreen() {
        super();
    }
    
    public String getImageName() {
        return c.MAIN_MENU_IMAGE;
    }
    
    public String set_next_state() {
        return c.MAIN_MENU;
    }


}

class c extends Constants {};