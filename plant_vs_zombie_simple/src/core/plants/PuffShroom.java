package core.plants;

import core.zombies.Zombie;
import core.bullets.Bullet;
import core.game.Group;
import core.Constants;

public class PuffShroom extends Plant{
    private long shoot_timer = 0; //上次发射时间
    private Group bullet_group;  //子弹组
    boolean can_sleep = true; //可以睡觉
    public PuffShroom(int x, int y, Group g){
        super(Constants.PLANT_HEALTH, x, y+20, Constants.PUFFSHROOM, 1);
        this.shoot_timer = 0; 
        this.bullet_group = g;
    }


    public void attacking(){
        this.current_time = (int)System.currentTimeMillis(); //同步时间
        if (this.current_time - this.shoot_timer > 3000){ //射击间隔
            Bullet bullet = new Bullet(this.rect.left + this.rect.width(), this.rect.bottom() -Constants.MAP_OFFSET_Y + 65, this.rect.bottom() -Constants.MAP_OFFSET_Y + 65, Constants.BULLET_MUSHROOM, Constants.BULLET_DAMAGE_NORMAL, true); 
            this.bullet_group.add(bullet);  //往子弹组里添加子弹
            this.shoot_timer = this.current_time;  //更新射击时间
        }
    }

    public boolean canAttack(Zombie zombie){ //判断僵尸是否在攻击范围内
        if (this.rect.centerx() <= zombie.rect.left + zombie.rect.width() && (this.rect.centerx() + c.GRID_X_SIZE * 4 >= zombie.rect.centerx())){return true;}
        return false; 
    }

    public void loadImages(String name, double scale){ //读取不同状态图片
        loadFrames(this.frames, name, Constants.BLACK, scale);
        loadFrames(this.sleep_frames, name+"Sleep", Constants.BLACK, scale);
    }
}
