package core.plants;

import core.zombies.Zombie;
import core.bullets.Bullet;
import core.game.Group;
import core.Constants;

public class  ScaredyShroom extends Plant{
    private long shoot_timer = 0; 
    private Group bullet_group; 
    boolean can_sleep = true;
    private int cry_x_range = c.GRID_X_SIZE * 2; //两格之内胆小菇遁地
    
    public  ScaredyShroom(int x, int y, Group g){
        super(Constants.PLANT_HEALTH, x, y, Constants.SCAREDYSHROOM, 1);
        this.shoot_timer = 0; 
        this.bullet_group = g;
    }

    public void setCry(){ //设置哭泣状态，改变图片为哭泣状态帧的图片
        this.state = Constants.CRY;
        changeFrames(this.cry_frames);
    }

    public void setAttack(Zombie zombie, Group zombie_group){ //设置状态为攻击，图片变为攻击状态
        this.zombie_group = zombie_group;
        setState(Constants.ATTACK);
        changeFrames(attack_frames);
    }

    public void setIdle(){ //设置状态为闲置，图片变为闲置状态
        this.state = Constants.IDLE;
        changeFrames(this.idle_frames);
    }

    public void attacking(){ //攻击逻辑同豌豆射手
        this.current_time = (int)System.currentTimeMillis();
        if (this.current_time - this.shoot_timer > 2000 && !this.state.equals(Constants.CRY)){
            Bullet bullet = new Bullet(this.rect.left + this.rect.width(), this.rect.bottom() -Constants.MAP_OFFSET_Y +40, this.rect.bottom() -Constants.MAP_OFFSET_Y + 40, Constants.BULLET_MUSHROOM, Constants.BULLET_DAMAGE_NORMAL, true); 
            this.bullet_group.add(bullet); 
            this.shoot_timer = this.current_time; 
        }
    }

    public boolean needCry(Zombie zombie){ //判断是否遁地
        if (!zombie.state.equals(Constants.DIE) && this.rect.centerx() <= zombie.rect.left + zombie.rect.width() &&
            this.rect.centerx() + this.cry_x_range > zombie.rect.centerx()){return true;}
        return false; 
    }

    public void loadImages(String name, double scale){ //加载不同状态的图片帧
        loadFrames(this.frames, name, Constants.WHITE, scale);
        loadFrames(this.idle_frames, name, Constants.WHITE, scale);
        loadFrames(this.cry_frames, name+"Cry", Constants.WHITE, scale);
        loadFrames(this.sleep_frames, name+"Sleep", Constants.WHITE, scale);
    }

}
