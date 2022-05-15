package core.zombies;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import core.game.GamePlay;
import core.plants.Plant;
import core.constants;

public abstract class Zombie {

    String name;
    int health;
    int damage = 1;
    boolean dead;
    boolean losHead;
    boolean helmet;
    List<ZombieHead> head_group;
    int walk_timer = 0;
    int animate_timer = 0;
    int attack_timer = 0;
    int current_time = 0;
    String state;
    String old_state;
    int animate_interval = 150;
    int ice_slow_ratio = 1;
    int ice_slow_timer = 0;
    int hit_timer = 0;
    int speed = 1;
    boolean is_hypno = false;
    int freeze_timer = 0;
    Plant prey;
    String ice_trap_image;

    public Zombie(int x, int y, String name, int health, List<ZombieHead> head_group, int damage) {

        this.name = name;
        // this.frames = []
        // this.frame_index = 0
        // this.loadImages()
        // this.frame_num = len(this.frames)

        // this.image = this.frames[this.frame_index]
        // this.rect = this.image.get_rect()
        // this.rect.centerx = x
        // this.rect.bottom = y

        this.health = health;
        this.damage = damage;
        this.dead = false;
        this.losHead = false;
        this.helmet = false;
        this.head_group = head_group;

        this.walk_timer = 0;
        this.animate_timer = 0;
        this.attack_timer = 0;
        this.state = constants.WALK;

        this.animate_interval = 150;
        this.ice_slow_ratio = 1;
        this.ice_slow_timer = 0;
        this.hit_timer = 0;
        this.speed = 1;
        this.freeze_timer = 0;
        this.is_hypno = false;

    }
    // the zombie is hypo&&attack other zombies when it ate a HypnoShroom

    // public void loadFrames(frames,String name, image_x, colorkey=constants.BLACK)
    // {}
    // frame_list = tool.GFX[name]
    // rect = frame_list[0].get_rect()
    // width, height = rect.w, rect.h
    // width -= image_x

    // for frame in frame_list {}
    // frames.append(tool.get_image(frame, image_x, 0, width, height, colorkey))

    // public void update( game_info) {}
    // this.current_time = game_info[constants.CURRENT_TIME]
    // this.handleState()
    // this.updateIceSlow()
    // this.animation()

    // public void handleState() {
    // switch (this.state){
    // case constants.WALK:
    // this.walking();
    // break;
    // case constants.ATTACK:
    // this.attacking()
    // break;
    // case constants.DIE :
    // this.dying();
    // break;
    // default:
    // this.freezing();

    // }}
    public void walking() {
        if (this.health <= 0) {
            this.setDie();
        } else if (this.health <= constants.LOSTHEAD_HEALTH && this.losHead == false) {
            // this.changeFrames(this.losthead_walk_frames);
            this.setLostHead();
        } else if (this.health <= constants.NORMAL_HEALTH && this.helmet) {
            // this.changeFrames(this.walk_frames);
            this.helmet = false;
            if (this.name == constants.NEWSPAPER_ZOMBIE) {
                this.speed = 2;
            }
        }
        // if (this.current_time - this.walk_timer > constants.ZOMBIE_WALK_INTERVAL *
        // this.getTimeRatio()) {
        // this.walk_timer = this.current_time;
        // if( this.is_hypno) {
        // this.rect.x += this.speed;
        // }
        // else {
        // this.rect.x -= this.speed;
        // }
        // }
    }

    public void attacking() {
        if (this.health <= 0) {
            this.setDie();
        } else if (this.health <= constants.LOSTHEAD_HEALTH && this.losHead == false) {
            // this.changeFrames(this.losthead_attack_frames);
            this.setLostHead();
        } else if (this.health <= constants.NORMAL_HEALTH && this.helmet) {
            // this.changeFrames(this.attack_frames);
            this.helmet = false;
        }
        if (this.current_time - this.attack_timer > constants.ATTACK_INTERVAL * this.getTimeRatio()) {
            if (this.prey.health > 0) {
                if (this.prey_is_plant) {
                    this.prey.setDamage(this.damage, this);
                } else {
                    this.prey.setDamage(this.damage);
                }
            }
            this.attack_timer = this.current_time;
        }
        if (this.prey.health <= 0) {
            this.prey = null;
            this.setWalk();
        }
    }

    public void dying() {

    }
    // public void freezing() {
    // if (this.health <= 0 ){
    // this.setDie();
    // }
    // else if( this.health <= constants.LOSTHEAD_HEALTH&&not this.losHead) {
    // if (this.old_state == constants.WALK) {
    // this.changeFrames(this.losthead_walk_frames);
    // }
    // else {
    // this.changeFrames(this.losthead_attack_frames);
    // }
    // this.setLostHead();
    // }
    // if ((this.current_time - this.freeze_timer) > constants.FREEZE_TIME) {
    // this.setWalk();
    // }
    // }
    // public void setLostHead(){
    // this.losHead = true;
    // if (this.head_group.size()!=0) {
    // this.head_group.add(ZombieHead(this.rect.centerx, this.rect.bottom))
    // }}
    // public void changeFrames( frames) {}
    // // '''change image frames&&modify rect position'''
    // this.frames = frames;
    // this.frame_num = len(this.frames);
    // this.frame_index = 0;

    // bottom = this.rect.bottom;
    // centerx = this.rect.centerx;
    // this.image = this.frames[this.frame_index];
    // this.rect = this.image.get_rect();
    // this.rect.bottom = bottom;
    // this.rect.centerx = centerx;

    // public void animation() {
    // if (this.state == constants.FREEZE) {
    // this.image.set_alpha(192);
    // return;
    // }
    // if (this.current_time - this.animate_timer > this.animate_interval *
    // this.getTimeRatio()) {
    // this.frame_index += 1;
    // if (this.frame_index >= this.frame_num) {
    // if (this.state == constants.DIE ){
    // this.kill();
    // return;
    // }
    // this.frame_index = 0;
    // }
    // this.animate_timer = this.current_time;
    // }
    // this.image = this.frames[this.frame_index]
    // if (this.is_hypno ){
    // this.image = pg.transform.flip(this.image, true, false)
    // }
    // if(this.current_time - this.hit_timer >= 200){
    // this.image.set_alpha(255);
    // }
    // else {
    // this.image.set_alpha(192);
    // }
    // }
    public int getTimeRatio() {
        return this.ice_slow_ratio;
    }

    public void setIceSlow() {
        // '''when get a ice bullet damage, slow the attack||walk speed of the zombie'''
        this.ice_slow_timer = this.current_time;
        this.ice_slow_ratio = 2;
    }

    public void updateIceSlow() {
        if (this.ice_slow_ratio > 1) {
            if (this.current_time - this.ice_slow_timer > constants.ICE_SLOW_TIME) {
                this.ice_slow_ratio = 1;
            }
        }
    }

    public void setDamage(int damage, Boolean ice) {
        this.health -= damage;
        this.hit_timer = this.current_time;
        if (ice) {
            this.setIceSlow();
        }
    }

    public void setWalk() {
        this.state = constants.WALK;
        this.animate_interval = 150;

        // if (this.helmet) {
        // this.changeFrames(this.helmet_walk_frames);
        // } else if (this.losHead) {
        // this.changeFrames(this.losthead_walk_frames);

        // } else {
        // this.changeFrames(this.walk_frames);
        // }
    }
    // public void setAttack( prey, is_plant) {
    // this.prey = prey
    // // # prey can be plant||other zombies
    // this.prey_is_plant = is_plant
    // this.state = constants.ATTACK
    // this.attack_timer = this.current_time
    // this.animate_interval = 100

    // if (this.helmet) {
    // this.changeFrames(this.helmet_attack_frames)
    // }else if (this.losHead) {
    // this.changeFrames(this.losthead_attack_frames)
    // }else {
    // this.changeFrames(this.attack_frames)
    // }}
    public void setDie() {
        this.state = constants.DIE;
        this.animate_interval = 200;
        // this.changeFrames(this.die_frames);
    }

    public void setBoomDie() {
        this.state = constants.DIE;
        this.animate_interval = 200;
        // this.changeFrames(this.boomdie_frames);
    }

    public void setFreeze(String ice_trap_image) {
        this.old_state = this.state;
        this.state = constants.FREEZE;
        this.freeze_timer = this.current_time;
        this.ice_trap_image = ice_trap_image;
        // this.ice_trap_rect = ice_trap_image.get_rect()
        // this.ice_trap_rect.centerx = this.rect.centerx
        // this.ice_trap_rect.bottom = this.rect.bottom
    }

    // public void drawFreezeTrap( surface) {
    // if (this.state == constants.FREEZE) {
    // surface.blit(this.ice_trap_image, this.ice_trap_rect)
    // }}
    public void setHypno() {
        this.is_hypno = true;
        this.setWalk();
    }

}
