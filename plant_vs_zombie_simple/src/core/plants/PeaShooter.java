package core.plants;

import core.bullets.Bullet;
import core.game.Group;
import core.Constants;

public class PeaShooter extends Plant{
    long shoot_timer = 0; //上一次射出子弹的时间
    Group bullet_group;  //子弹组
    public PeaShooter(int x, int y, Group g){
        super(Constants.PLANT_HEALTH, x, y, Constants.PEASHOOTER, 1); 
        this.shoot_timer = 0; 
        this.bullet_group = g;
    }

    public void attacking(){
        this.current_time = (int)System.currentTimeMillis(); //与系统的时间同步
        if (this.current_time - this.shoot_timer > 2000){ //发射子弹
            Bullet bullet = new Bullet(this.rect.left + this.rect.width(), this.rect.bottom() -Constants.MAP_OFFSET_Y+20, this.rect.bottom() -Constants.MAP_OFFSET_Y+20, Constants.BULLET_PEA, Constants.BULLET_DAMAGE_NORMAL, false); 
            this.bullet_group.add(bullet); //在子弹组中添加新建子弹
            this.shoot_timer = this.current_time; //更新射击时间
        }
    }

    public void loadImages(String name, double scale){ //读取图片
        loadFrames(this.frames, name, Constants.BLACK, scale);
    }
}
