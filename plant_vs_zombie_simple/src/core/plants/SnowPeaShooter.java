package core.plants;

import core.bullets.Bullet;
import core.game.Group;
import core.Constants;

public class SnowPeaShooter extends Plant{
    private long shoot_timer = 0; 
    private Group bullet_group; 
    public SnowPeaShooter(int x, int y, Group g){
        super(Constants.PLANT_HEALTH, x, y, Constants.SNOWPEASHOOTER, 1); 
        this.shoot_timer = 0;
        this.bullet_group = g; 
    }

    public void attacking(){ //与豌豆射手类似，除了子弹类型不同
        this.current_time = (int)System.currentTimeMillis();
        if (this.current_time - this.shoot_timer > 2000){
            Bullet bullet = new Bullet(this.rect.left + this.rect.width(), this.rect.bottom() -Constants.MAP_OFFSET_Y+20, this.rect.bottom() -Constants.MAP_OFFSET_Y+20, Constants.BULLET_PEA_ICE, Constants.BULLET_DAMAGE_NORMAL, true); 
            this.bullet_group.add(bullet); 
            this.shoot_timer = this.current_time; 
        }
    }

    public void loadImages(String name, double scale){//读取图片
        loadFrames(this.frames, name, Constants.BLACK, scale);
    }
}
