package core.zombies;
import core.*;
import java.util.*;
import java.awt.image.BufferedImage;
import core.game.*;
public class ConeHeadZombie extends Zombie{
    public ConeHeadZombie(int x, int y, Group head_group) {
        super(x, y, Constants.CONEHEAD_ZOMBIE, Constants.CONEHEAD_HEALTH, head_group, 1);
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

        loadFrames(helmet_walk_frames,name,Tool.ZOMBIE_RECT.getJSONObject(name).getInt("x"),Constants.BLACK);
        loadFrames(helmet_attack_frames,name + "Attack",Tool.ZOMBIE_RECT.getJSONObject(name + "Attack").getInt("x"),Constants.BLACK);
        loadFrames(walk_frames,name,Tool.ZOMBIE_RECT.getJSONObject(Constants.NORMAL_ZOMBIE).getInt("x"),Constants.BLACK);
        loadFrames(attack_frames,name + "Attack",Tool.ZOMBIE_RECT.getJSONObject(Constants.NORMAL_ZOMBIE + "Attack").getInt("x"),Constants.BLACK);
        loadFrames(losthead_walk_frames,name+ "LostHead",Tool.ZOMBIE_RECT.getJSONObject(Constants.NORMAL_ZOMBIE+ "LostHead").getInt("x"),Constants.BLACK);
        loadFrames(losthead_attack_frames,name+ "LostHeadAttack",Tool.ZOMBIE_RECT.getJSONObject(Constants.NORMAL_ZOMBIE+ "LostHeadAttack").getInt("x"),Constants.BLACK);
        loadFrames(die_frames,name+ "Die",Tool.ZOMBIE_RECT.getJSONObject(Constants.NORMAL_ZOMBIE+ "Die").getInt("x"),Constants.BLACK);
        loadFrames(boomdie_frames,Constants.BOOMDIE,Tool.ZOMBIE_RECT.getJSONObject(Constants.BOOMDIE).getInt("x"),Constants.BLACK);
        frames = helmet_walk_frames;
    }
}
