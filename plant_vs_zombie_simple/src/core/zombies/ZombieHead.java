package core.zombies;
import core.Constants;
import java.util.*;
import java.awt.image.BufferedImage;
import core.game.*;
public class ZombieHead extends Zombie{
    // ArrayList<BufferedImage> die_frames; 
    // String die_name ;
    public ZombieHead(int x,int y, Group head_group){
        super(x, y, Constants.ZOMBIE_HEAD, 0 ,head_group,1);
        this.state = Constants.DIE;

    }
    public void loadImages(){
        // this.die_frames = new ArrayList<BufferedImage>();
        // this.die_name =  this.name;
        this.loadFrames(this.die_frames, name, 0,Constants.BLACK);
        this.frames = this.die_frames;
    }
    public void setWalk(){
        this.animate_interval = 100;
    }
}
