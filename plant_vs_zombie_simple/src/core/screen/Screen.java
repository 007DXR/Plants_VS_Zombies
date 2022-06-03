package core.screen;

import core.State;
import core.Tool;
import core.json.JSONObject;

public class Screen extends State {
    String name;
    JSONObject game_info;
    public Screen() {
        super();
        this.end_time = 3000;
    }

    public void startup(int current_time, JSONObject persist):
        this.start_time = current_time;
        this.next = c.LEVEL;
        this.persist = persist;
        this.game_info = persist;
        name = this.getImageName();
        this.setupImage(name);
        this.next = this.set_next_state();
    
    public void getImageName(){
        //pass
    }

    public void set_next_state() {
        //pass
    }

    public void setupImage(String name) {
        this.image = Tool.loadImage(Tool.GFX[name]);
        this.rect = this.image.get_rect();
        this.rect.x = 0;
        this.rect.y = 0;
    }

    def update(self, surface, current_time, mouse_pos, mouse_click):
        if(current_time - this.start_time) < this.end_time:
            surface.fill(c.WHITE)
            surface.blit(this.image, this.rect)
        else:
            this.done = True
}
