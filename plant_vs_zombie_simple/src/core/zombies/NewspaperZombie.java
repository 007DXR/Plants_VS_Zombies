package core.zombies;
import core.*;
import java.util.*;
import java.awt.image.BufferedImage;
import core.game.*;
public class NewspaperZombie extends Zombie{
    boolean helmet;

    public NewspaperZombie(int x, int y, Group head_group) {
        super(x, y, c.NEWSPAPER_ZOMBIE, c.NEWSPAPER_HEALTH, head_group, 1);
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

        loadFrames(helmet_walk_frames,name,Tool.ZOMBIE_RECT.getJSONObject(name).getInt("x"),c.WHITE);
        loadFrames(helmet_attack_frames,name + "Attack",Tool.ZOMBIE_RECT.getJSONObject(name + "Attack").getInt("x"),c.WHITE);
        loadFrames(walk_frames,name + "NoPaper",Tool.ZOMBIE_RECT.getJSONObject(name + "NoPaper").getInt("x"),c.WHITE);
        loadFrames(attack_frames,name + "NoPaperAttack",Tool.ZOMBIE_RECT.getJSONObject(name + "NoPaperAttack").getInt("x"),c.WHITE);
        loadFrames(losthead_walk_frames,name+ "LostHead",Tool.ZOMBIE_RECT.getJSONObject(name+ "LostHead").getInt("x"),c.WHITE);
        loadFrames(losthead_attack_frames,name+ "LostHeadAttack",Tool.ZOMBIE_RECT.getJSONObject(name+ "LostHeadAttack").getInt("x"),c.WHITE);
        loadFrames(die_frames,name+ "Die",Tool.ZOMBIE_RECT.getJSONObject(name+ "Die").getInt("x"),c.WHITE);
        loadFrames(boomdie_frames,c.BOOMDIE,Tool.ZOMBIE_RECT.getJSONObject(c.BOOMDIE).getInt("x"),c.BLACK);
        
        
        frames = helmet_walk_frames;
    }
}