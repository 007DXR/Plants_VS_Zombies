package core.zombies;

import core.Constants;

import java.util.LinkedList;
import java.util.List;
import core.game.*;
public class BucketHeadZombie extends Zombie{
    boolean helmet;

    public BucketHeadZombie(int x, int y, LinkedList<Group> head_group) {
        super(x, y, Constants.BUCKETHEAD_ZOMBIE, Constants.BUCKETHEAD_HEALTH, head_group, 1);
        this.helmet = true;
    }

    @Override
    public void loadImages() {
        // TODO Auto-generated method stub
        
    }
}