package core.screen;

import core.*;
import core.game.*;
import core.json.JSONObject;
import java.awt.image.BufferedImage;
import java.util.TreeSet;
public class Screen extends State {
    String name;
    Rect rect;
    public Screen() {
        super();
        this.end_time = 3000;
    }

    public void startup(int current_time, JSONObject persist){
        this.start_time = current_time;
        this.next = c.LEVEL;
        this.persist = persist;
        this.game_info = persist;
        name = this.getImageName();
        this.setupImage(name);
        this.next = this.set_next_state();
    }
  
        public  String getImageName(){

        }
        public String set_next_state() {
           
        }
    public void setupImage(String name) {
        TreeSet<Tool.Img> frame_list = (TreeSet<Tool.Img>) Tool.GFX.get(name);
        BufferedImage image = frame_list.first().image;
        this.rect = new Rect(image,0,0);
   
    }

    public void  update(Surface surface,int current_time,ArrayList<Integer> mouse_pos,Boolean mouse_click):
        if(current_time - this.start_time < this.end_time){
            surface.fill(c.WHITE)
            surface.blit(this.image, this.rect);
        }
        else
            this.done = True;
}
class c extends Constants {};