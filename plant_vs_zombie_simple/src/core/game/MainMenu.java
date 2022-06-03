package core.game;

import core.State;
import core.json.JSONObject;

public class MainMenu extends State{
    String next;
    
    public MainMenu () {
        super();
    }
    
    public void startup(int current_time, JSONObject persist) {
        this.next = c.LEVEL;
        this.persist = persist;
        this.game_info = persist;
        
        this.setupBackground();
        this.setupOption();
    }

    public void setupBackground(self) {
        frame_rect = [80, 0, 800, 600]
        this.bg_image = tool.get_image(tool.GFX[c.MAIN_MENU_IMAGE], *frame_rect)
        this.bg_rect = this.bg_image.get_rect()
        this.bg_rect.x = 0
        this.bg_rect.y = 0
    }
        
    public void setupOption(self) {
        this.option_frames = []
        frame_names = [c.OPTION_ADVENTURE + '_0', c.OPTION_ADVENTURE + '_1']
        frame_rect = [0, 0, 165, 77]
        
        for name in frame_names:
            this.option_frames.append(tool.get_image(tool.GFX[name], *frame_rect, c.BLACK, 1.7))
        
        this.option_frame_index = 0
        this.option_image = this.option_frames[this.option_frame_index]
        this.option_rect = this.option_image.get_rect()
        this.option_rect.x = 435
        this.option_rect.y = 75
        
        this.option_start = 0
        this.option_timer = 0
        this.option_clicked = False
    }
    
    public void checkOptionClick(self, mouse_pos) {
        x, y = mouse_pos
        if(x >= this.option_rect.x and x <= this.option_rect.right and
           y >= this.option_rect.y and y <= this.option_rect.bottom):
            this.option_clicked = True
            this.option_timer = this.option_start = this.current_time
        return False
    }
        
    public void update(self, surface, current_time, mouse_pos, mouse_click) {
        this.current_time = this.game_info[c.CURRENT_TIME] = current_time
        
        if not this.option_clicked:
            if mouse_pos:
                this.checkOptionClick(mouse_pos)
        else:
            if(this.current_time - this.option_timer) > 200:
                this.option_frame_index += 1
                if this.option_frame_index >= 2:
                    this.option_frame_index = 0
                this.option_timer = this.current_time
                this.option_image = this.option_frames[this.option_frame_index]
            if(this.current_time - this.option_start) > 1300:
                this.done = True

        surface.blit(this.bg_image, this.bg_rect)
        surface.blit(this.option_image, this.option_rect)
    }
}
