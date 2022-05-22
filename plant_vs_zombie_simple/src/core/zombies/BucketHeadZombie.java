package core.zombies;

import core.Constants;
import java.util.List;

public class BucketHeadZombie extends Zombie{
    boolean helmet;

    public BucketHeadZombie(int x, int y, List<ZombieHead> head_group) {
        super(x, y, Constants.BUCKETHEAD_ZOMBIE, Constants.BUCKETHEAD_HEALTH, head_group, 1);
        this.helmet = true;
    }
}