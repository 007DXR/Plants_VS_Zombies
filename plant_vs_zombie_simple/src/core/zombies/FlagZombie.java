package core.zombies;
import core.*;
import java.util.*;
import java.awt.image.BufferedImage;
import core.game.*;
/*
 * 红旗僵尸
 */
public class FlagZombie extends Zombie{
    public FlagZombie(int x,int y, Group head_group){
        super(x,y,c.FLAG_ZOMBIE, c.FLAG_HEALTH, head_group,1);

    }
    public void loadImages(){
        walk_frames = new ArrayList<BufferedImage>();
        attack_frames = new ArrayList<BufferedImage>();
        losthead_walk_frames = new ArrayList<BufferedImage>();
        losthead_attack_frames = new ArrayList<BufferedImage>();
        die_frames = new ArrayList<BufferedImage>();
        boomdie_frames = new ArrayList<BufferedImage>();

        loadFrames(walk_frames,name,Tool.ZOMBIE_RECT.getJSONObject(name).getInt("x"),c.BLACK);
        loadFrames(attack_frames,name + "Attack",Tool.ZOMBIE_RECT.getJSONObject(name + "Attack").getInt("x"),c.BLACK);
        loadFrames(losthead_walk_frames,name+ "LostHead",Tool.ZOMBIE_RECT.getJSONObject(name+ "LostHead").getInt("x"),c.BLACK);
        loadFrames(losthead_attack_frames,name+ "LostHeadAttack",Tool.ZOMBIE_RECT.getJSONObject(name+ "LostHeadAttack").getInt("x"),c.BLACK);
        loadFrames(die_frames,c.NORMAL_ZOMBIE+ "Die",Tool.ZOMBIE_RECT.getJSONObject(c.NORMAL_ZOMBIE+ "Die").getInt("x"),c.BLACK);
        loadFrames(boomdie_frames,c.BOOMDIE,Tool.ZOMBIE_RECT.getJSONObject(c.BOOMDIE).getInt("x"),c.BLACK);
        
        frames = walk_frames;
    }
}
