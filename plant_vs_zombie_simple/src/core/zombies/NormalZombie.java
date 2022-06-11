package core.zombies;
import core.*;
import java.util.*;
import java.awt.image.BufferedImage;
import core.game.*;
/*
 * 普通僵尸
 */
public class NormalZombie extends Zombie{
    public NormalZombie(int x,int y, Group head_group){
        super(x,y,Constants.NORMAL_ZOMBIE, Constants.NORMAL_HEALTH, head_group,1);

    }
    public void loadImages(){
        walk_frames = new ArrayList<BufferedImage>();
        attack_frames = new ArrayList<BufferedImage>();
        losthead_walk_frames = new ArrayList<BufferedImage>();
        losthead_attack_frames = new ArrayList<BufferedImage>();
        die_frames = new ArrayList<BufferedImage>();
        boomdie_frames = new ArrayList<BufferedImage>();

        loadFrames(walk_frames,name,Tool.ZOMBIE_RECT.getJSONObject(name).getInt("x"),Constants.BLACK);
        loadFrames(attack_frames,name + "Attack",Tool.ZOMBIE_RECT.getJSONObject(name + "Attack").getInt("x"),Constants.BLACK);
        loadFrames(losthead_walk_frames,name+ "LostHead",Tool.ZOMBIE_RECT.getJSONObject(name+ "LostHead").getInt("x"),Constants.BLACK);
        loadFrames(losthead_attack_frames,name+ "LostHeadAttack",Tool.ZOMBIE_RECT.getJSONObject(name+ "LostHeadAttack").getInt("x"),Constants.BLACK);
        loadFrames(die_frames,name+ "Die",Tool.ZOMBIE_RECT.getJSONObject(name+ "Die").getInt("x"),Constants.BLACK);
        loadFrames(boomdie_frames,Constants.BOOMDIE,Tool.ZOMBIE_RECT.getJSONObject(Constants.BOOMDIE).getInt("x"),Constants.BLACK);
        
        frames = walk_frames;
    }
}
