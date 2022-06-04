package core.screen;
import java.awt.Graphics;
import core.*;
import core.game.*;
import core.json.JSONObject;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;
public abstract class Screen extends State {
    String name;
    Rect rect;
    public Screen() {
        super();
        this.end_time = 3000;
    }

    public void startup(long current_time, JSONObject persist){
        this.start_time = current_time;
        this.next = c.LEVEL;
        this.persist = persist;
        this.game_info = persist;
        name = this.getImageName();
        this.setupImage(name);
        this.next = this.set_next_state();
    }
  
        public abstract String getImageName();
        public abstract String set_next_state() ;
    public void setupImage(String name) {
        TreeSet<Tool.Img> frame_list = (TreeSet<Tool.Img>) Tool.GFX.get(name);
        BufferedImage image = frame_list.first().image;
        this.rect = new Rect(image,0,0);
   
    }
    // ,ArrayList<Integer> mouse_pos,Boolean mouse_click
    public void  update(Graphics surface, ArrayList<Integer> mousePos,long current_time){
        if(current_time - this.start_time < this.end_time){
            // surface.fill(c.WHITE)
            // surface.blit(this.image, this.rect);
           
                Sprite sprite = new Sprite(this.rect.image);
                sprite.paintObject(surface);
           
        }
        else
            this.done = true;
        }
}
class c extends Constants {};