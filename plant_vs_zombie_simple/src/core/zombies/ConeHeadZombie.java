package core.zombies;
import core.constants;
import java.util.List;

public class ConeHeadZombie extends Zombie{
    boolean helmet;
    public  ConeHeadZombie(int x,int y,List<ZombieHead> head_group){
    super(x, y, constants.CONEHEAD_ZOMBIE, constants.CONEHEAD_HEALTH, head_group,1);
    this.helmet = true;
}
// public void loadImages(this){}
//     this.helmet_walk_frames = []
//     this.helmet_attack_frames = []
//     this.walk_frames = []
//     this.attack_frames = []
//     this.losthead_walk_frames = []
//     this.losthead_attack_frames = []
//     this.die_frames = []
//     this.boomdie_frames = []
    
//     helmet_walk_name = this.name
//     helmet_attack_name = this.name + 'Attack'
//     walk_name = constants.NORMAL_ZOMBIE
//     attack_name = constants.NORMAL_ZOMBIE + 'Attack'
//     losthead_walk_name = constants.NORMAL_ZOMBIE + 'LostHead'
//     losthead_attack_name = constants.NORMAL_ZOMBIE + 'LostHeadAttack'
//     die_name = constants.NORMAL_ZOMBIE + 'Die'
//     boomdie_name = constants.BOOMDIE

//     frame_list = [this.helmet_walk_frames, this.helmet_attack_frames,
//                   this.walk_frames, this.attack_frames, this.losthead_walk_frames,
//                   this.losthead_attack_frames, this.die_frames, this.boomdie_frames]
//     name_list = [helmet_walk_name, helmet_attack_name,
//                  walk_name, attack_name, losthead_walk_name,
//                  losthead_attack_name, die_name, boomdie_name]
    
//     for i, name in enumerate(name_list){}
//         this.loadFrames(frame_list[i], name, tool.ZOMBIE_RECT[name]['x'])

//     this.frames = this.helmet_walk_frames   
}
