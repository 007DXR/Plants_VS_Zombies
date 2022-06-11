package core.screen;
import java.awt.Graphics;
import core.*;
import core.game.*;
import core.json.JSONObject;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;
import core.Tool;
public abstract class Screen extends State {
    String name;
    Rect rect;
    public Screen() {
        super();
        this.end_time = 6000;
    }

    public void startUp(long current_time, JSONObject persist){
        this.start_time = current_time;
        this.next = c.LEVEL;
        this.persist = persist;
        this.game_info = persist;
        name = this.getImageName();
        this.setupImage(name);
        this.next = this.set_next_state();
    }
  
        public abstract String getImageName();

        public abstract String set_next_state();
    
        public void setupImage(String name) {
            TreeSet<Tool.Img> frame_list = (TreeSet<Tool.Img>) Tool.GFX.get(name);
            BufferedImage image = frame_list.first().image;
            this.rect = new Rect(image, 0, 0);
        }
    
    // ArrayList<Integer> mouse_pos,Boolean mouse_click
    public void update(Graphics g, int time, ArrayList<Integer> mousePos, ArrayList<Boolean> mouseClick) {
        if (time - this.start_time < this.end_time) {
            // surface.fill(c.WHITE)
            // surface.blit(this.image, this.rect);
            this.done = false;
            // Sprite sprite = new Sprite(this.rect.image);
            // sprite.paintObject(g);

        } else
            this.done = true;
    }
        
    public void draw(Graphics g) {
        if (!done) {
            Sprite sprite = new Sprite(Tool.resize(this.rect.image, 1.1));
            sprite.paintObject(g);
        }
    }
}
class c extends Constants {};