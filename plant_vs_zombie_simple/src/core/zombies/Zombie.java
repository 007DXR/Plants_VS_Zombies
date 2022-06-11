package core.zombies;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;
import core.*;
import core.plants.Plant;
import core.game.*;

import java.awt.Graphics;
/*
 * 僵尸超类，继承精灵类Sprite
 */
public abstract class Zombie extends Sprite{

    public String old_state;
    public String name;
    public int frame_index = 0;
    public int frame_num;
    public BufferedImage ice_trap_image;
    public int health;
    int damage;
    boolean dead = false;
    boolean losHead = false;
    boolean helmet = false;

    // 时间----------------------------------------
    long walk_timer = 0;
    long animate_timer = 0;
    long attack_timer = 0;
    long ice_slow_timer = 0;
    long hit_timer = 0;
    long freeze_timer = 0;
    long current_time;
// ----------------------------------------
    public String state=Constants.WALK;
    int animate_interval = 150;
    int ice_slow_ratio = 1;

    double speed = 1;
    boolean is_hypno = false;
    boolean prey_is_plant = true;
    Plant prey;

    // 僵尸状态
    ArrayList<BufferedImage> frames;    
    ArrayList<BufferedImage> helmet_attack_frames;  //带帽攻击
    ArrayList<BufferedImage> die_frames;            //死亡
    ArrayList<BufferedImage> walk_frames;           //正常行走
    ArrayList<BufferedImage> attack_frames;         //正常攻击
    ArrayList<BufferedImage> losthead_walk_frames;  //掉脑袋行走
    ArrayList<BufferedImage> losthead_attack_frames; //掉脑袋攻击
    ArrayList<BufferedImage> boomdie_frames;        //被炸掉
    ArrayList<BufferedImage> helmet_walk_frames;     //带帽行走
    Group head_group;

    public Zombie(int x, int y, String name, int health, Group head_group,int damage) {

        super();
        this.name = name;
        this.loadImages();
        this.frame_num = this.frames.size();
        this.rect = new Rect(this.frames.get(this.frame_index),x,y);
        this.head_group=head_group;
        this.health = health;
        this.damage = damage;

    }

    public abstract void loadImages();


// 载入图片
    public void loadFrames(ArrayList<BufferedImage> frames, String name, int image_x, Color colorkey) {
        
        TreeSet<Tool.Img> frame_list = (TreeSet<Tool.Img>) Tool.GFX.get(name);
        for (Tool.Img frame : frame_list) {
            BufferedImage rect = frame.image;
            int width = rect.getWidth();
            int height = rect.getHeight();
            width -= image_x;

            frames.add(Tool.adjustAlpha( frame.image.getSubimage(image_x, 0, width, height),colorkey));
        }
    }
// 更新僵尸状态
    public void update() {
        this.current_time = System.currentTimeMillis();
        this.handleState();
        this.updateIceSlow();
        this.animation();
    }
// 画面渲染
    public void handleState() {
        if (this.state.equals(Constants.WALK))
            this.walking();
        if (this.state.equals( Constants.ATTACK))
            this.attacking();
        if (this.state.equals( Constants.DIE))
            this.dying();
        if (this.state.equals( Constants.FREEZE))
            this.freezing();
    }
// 行走状态
    public void walking() {
        if (this.health <= 0) {
            this.setDie();
        } else if (this.health <= Constants.LOSTHEAD_HEALTH && this.losHead == false) {
            this.changeFrames(this.losthead_walk_frames);
            this.setLostHead();
        } else if (this.health <= Constants.NORMAL_HEALTH && this.helmet) {
            this.changeFrames(this.walk_frames);
            this.helmet = false;
            if (this.name.equals( Constants.NEWSPAPER_ZOMBIE)) {
                this.speed = 2;
            }
        }
        if (this.current_time - this.walk_timer > Constants.ZOMBIE_WALK_INTERVAL *
                this.getTimeRatio()) {
            this.walk_timer = this.current_time;
            if (this.is_hypno) {
                this.rect.left += this.speed;
            } else {
                this.rect.left -= this.speed;
            }
        }
    }
// 攻击状态
    public void attacking() {
        if (this.health <= 0) {
            this.setDie();
        } else if (this.health <= Constants.LOSTHEAD_HEALTH && this.losHead == false) {
            this.changeFrames(this.losthead_attack_frames);
            this.setLostHead();
        } else if (this.health <= Constants.NORMAL_HEALTH && this.helmet) {
            this.changeFrames(this.attack_frames);
            this.helmet = false;
        }
        if (this.health > Constants.LOSTHEAD_HEALTH && this.current_time - this.attack_timer > Constants.ATTACK_INTERVAL * this.getTimeRatio()) {
            if (this.prey.health > 0) {
                if (this.prey_is_plant) {
                    this.prey.setDamage(this.damage);
                }
                // else {
                // this.prey.setDamage(this.damage);
                // }
            }
            this.attack_timer = this.current_time;
        }
        if (this.prey.health <= 0) {
            this.prey = null;
            this.setWalk();
        }
    }
// 死亡状态
    public void dying() {
    }
// 结冰状态
    public void freezing() {
        if (this.health <= 0) {
            this.setDie();
        } else if (this.health <= Constants.LOSTHEAD_HEALTH && this.losHead == false) {
            if (this.old_state.equals( Constants.WALK)) {
                this.changeFrames(this.losthead_walk_frames);
            } else {
                this.changeFrames(this.losthead_attack_frames);
            }
            this.setLostHead();
        }
        if ((this.current_time - this.freeze_timer) > Constants.FREEZE_TIME) {
            this.setWalk();
        }
    }
// 掉头状态
    public void setLostHead() {
        this.losHead = true;
        // if (this.head_group.size()!=0) {
        // this.head_group.add(new ZombieHead(this.centerx, this.bottom));
        // }
    }
// 改变状态控制器
    public void changeFrames(ArrayList<BufferedImage> frames) {
        // '''change image frames&&modify rect position'''
        this.frames = frames;
        this.frame_num = this.frames.size();
        this.frame_index = 0;
        this.rect.image = this.frames.get(this.frame_index);

    }
// 渲染动画
    public void animation() {
        if (this.state.equals( Constants.FREEZE)) {

            this.rect.image=Tool.adjustBrightness(this.rect.image, 192);
            return;
        }
        if (this.current_time - this.animate_timer > this.animate_interval *
                this.getTimeRatio()) {
            this.frame_index += 1;
            if (this.frame_index >= this.frame_num) {
                if (this.state.equals( Constants.DIE)) {
                    this.kill();
                    return;
                }
                this.frame_index = 0;
            }
            this.animate_timer = this.current_time;
        }
        this.rect.image = this.frames.get(this.frame_index);
        if (this.is_hypno) {
            // this.rect.image = pg.transform.flip(this.rect.image, true, false)
        }
        if (this.current_time - this.hit_timer >= 200) {

            this.rect.image=Tool.adjustBrightness(this.rect.image, 255);
        } else {

            this.rect.image=Tool.adjustBrightness(this.rect.image, 192);
        }
    }
// 获得僵尸行进速度
    public int getTimeRatio() {
        return this.ice_slow_ratio;
    }
// 冰冻，减速
    public void setIceSlow() {
        // '''when get a ice bullet damage, slow the attack||walk speed of the zombie'''
        this.ice_slow_timer = this.current_time;
        this.ice_slow_ratio = 2;
    }
// 解冻，恢复原速
    public void updateIceSlow() {
        if (this.ice_slow_ratio > 1) {
            if (this.current_time - this.ice_slow_timer > Constants.ICE_SLOW_TIME) {
                this.ice_slow_ratio = 1;
            }
        }
    }
// 受到伤害
    public void setDamage(int damage, Boolean ice) {
        this.health -= damage;
        this.hit_timer = this.current_time;
        if (ice) {
            this.setIceSlow();
        }
    }
// 改变行走状态
    public void setWalk() {
        this.state = Constants.WALK;
        this.animate_interval = 150;

        // if (this.helmet) {
        // this.changeFrames(this.helmet_walk_frames);
        // } else
        if (this.losHead) {
            this.changeFrames(this.losthead_walk_frames);

        } else {
            this.changeFrames(this.walk_frames);
        }
    }
// 改变攻击状态
    public void setAttack(Plant prey,Boolean is_plant) {
        this.prey = prey;
        // # prey can be plant||other zombies
        this.prey_is_plant = is_plant;
        this.state = Constants.ATTACK;
        this.attack_timer = this.current_time;
        this.animate_interval = 100;

        if (this.helmet) {
            this.changeFrames(this.helmet_attack_frames);
        }else if (this.losHead) {
            this.changeFrames(this.losthead_attack_frames);
        }else {
            this.changeFrames(this.attack_frames);
        }
    }
// 改变死亡状态 
    public void setDie() {
        this.state = Constants.DIE;
        this.animate_interval = 200;
        this.changeFrames(this.die_frames);
    }
//爆炸死亡
    public void setBoomDie() {
        this.state = Constants.DIE;
        this.animate_interval = 200;
        this.changeFrames(this.boomdie_frames);
    }
//被冰冻
    public void setFreeze(BufferedImage ice_trap_image) {
        this.old_state = this.state;
        this.state = Constants.FREEZE;
        this.freeze_timer = this.current_time;
        this.ice_trap_image = ice_trap_image;
        // this.ice_trap_rect = ice_trap_image.get_rect()
        // this.ice_centerx = this.centerx;
        // this.ice_bottom = this.bottom;

    }
// 渲染冰冻效果
    public void drawFreezeTrap(Graphics g) {
        if (this.state.equals( Constants.FREEZE)) {
            Sprite sprite = new Sprite(this.ice_trap_image);
            sprite.paintObject(g);
        }
    }
    public void setHypno() {
        this.is_hypno = true;
        this.setWalk();
    }

}
class c extends Constants{};
