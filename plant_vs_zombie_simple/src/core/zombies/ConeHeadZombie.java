package core.zombies;
import core.*;
import java.util.*;
import java.awt.image.BufferedImage;
import core.game.*;
public class ConeHeadZombie extends Zombie{
    public ConeHeadZombie(int x, int y, Group head_group) {
        super(x, y, c.CONEHEAD_ZOMBIE, c.CONEHEAD_HEALTH, head_group, 1);
        this.helmet = true;
    }

    @Override
    public void loadImages() {
        // TODO Auto-generated method stub
        helmet_walk_frames = new ArrayList<BufferedImage>();
        helmet_attack_frames = new ArrayList<BufferedImage>();
        walk_frames = new ArrayList<BufferedImage>();
        attack_frames = new ArrayList<BufferedImage>();
        losthead_walk_frames = new ArrayList<BufferedImage>();
        losthead_attack_frames = new ArrayList<BufferedImage>();
        die_frames = new ArrayList<BufferedImage>();
        boomdie_frames = new ArrayList<BufferedImage>();

        loadFrames(helmet_walk_frames,name,Tool.ZOMBIE_RECT.getJSONObject(name).getInt("x"),c.BLACK);
        loadFrames(helmet_attack_frames,name + "Attack",Tool.ZOMBIE_RECT.getJSONObject(name + "Attack").getInt("x"),c.BLACK);
        loadFrames(walk_frames,c.NORMAL_ZOMBIE,Tool.ZOMBIE_RECT.getJSONObject(c.NORMAL_ZOMBIE).getInt("x"),c.BLACK);
        loadFrames(attack_frames,c.NORMAL_ZOMBIE + "Attack",Tool.ZOMBIE_RECT.getJSONObject(c.NORMAL_ZOMBIE + "Attack").getInt("x"),c.BLACK);
        loadFrames(losthead_walk_frames,c.NORMAL_ZOMBIE+ "LostHead",Tool.ZOMBIE_RECT.getJSONObject(c.NORMAL_ZOMBIE+ "LostHead").getInt("x"),c.BLACK);
        loadFrames(losthead_attack_frames,c.NORMAL_ZOMBIE+ "LostHeadAttack",Tool.ZOMBIE_RECT.getJSONObject(c.NORMAL_ZOMBIE+ "LostHeadAttack").getInt("x"),c.BLACK);
        loadFrames(die_frames,c.NORMAL_ZOMBIE+ "Die",Tool.ZOMBIE_RECT.getJSONObject(c.NORMAL_ZOMBIE+ "Die").getInt("x"),c.BLACK);
        loadFrames(boomdie_frames,c.BOOMDIE,Tool.ZOMBIE_RECT.getJSONObject(c.BOOMDIE).getInt("x"),c.BLACK);
        frames = helmet_walk_frames;
    }
}
