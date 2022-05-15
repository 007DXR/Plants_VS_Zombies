package core.zombies;
import core.constants ;

public class ZombieHead extends Zombie{
    public ZombieHead(int x,int y){
        super(x, y, constants.ZOMBIE_HEAD, 0,null ,1);
        this.state = constants.DIE;
    }
    // public void loadImages(){}
    //     this.die_frames = []
    //     die_name =  this.name
    //     this.loadFrames(this.die_frames, die_name, 0)
    //     this.frames = this.die_frames

    public void setWalk(){
        this.animate_interval = 100;
    }
}
