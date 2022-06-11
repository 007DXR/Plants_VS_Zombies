package core.zombies;
import core.Constants;
import core.game.*;
/*
 * 死去的僵尸
 */
public class ZombieHead extends Zombie{

    public ZombieHead(int x,int y, Group head_group){
        super(x, y, Constants.ZOMBIE_HEAD, 0 ,head_group,1);
        this.state = Constants.DIE;

    }
    public void loadImages(){
        this.loadFrames(this.die_frames, name, 0,Constants.BLACK);
        this.frames = this.die_frames;
    }
    public void setWalk(){
        this.animate_interval = 100;
    }
}
