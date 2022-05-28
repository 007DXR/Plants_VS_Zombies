package core.zombies;

import java.io.File;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import core.*;
import core.plants.Plant;
import core.Constants;

public abstract class Zombie {

    // 大小
    public int width;
    public int height;
    public String old_state;
    public String name;

    public int frame_index = 0;
    public int frame_num;

    public BufferedImage image;
    public BufferedImage ice_trap_image;
    // 位置----------------------------------------
    public int x;
    public int y;
    // public int centerx;
    // public int bottom;
    // public int ice_centerx;
    // public int ice_bottom;
    // ----------------------------------------
    int health;
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

    // -----------------------------------
    public ArrayList<BufferedImage> frames;
    ArrayList<BufferedImage> helmet_attack_frames;
    ArrayList<BufferedImage> die_frames;
    ArrayList<BufferedImage> walk_frames;
    ArrayList<BufferedImage> attack_frames;
    ArrayList<BufferedImage> losthead_walk_frames;
    ArrayList<BufferedImage> losthead_attack_frames;
    ArrayList<BufferedImage> boomdie_frames;

    // String walk_name;
    // String attack_name;
    // String losthead_walk_name;
    // String losthead_attack_name;
    // String die_name;
    // String boomdie_name;

    public Zombie(int x, int y, String name, int health, int damage) {

        this.name = name;
        this.loadImages();
        this.frame_num = this.frames.size();
        this.image = this.frames.get(this.frame_index);
        // this.rect = this.image.get_rect();
        this.x = x;
        this.y = y;

        this.health = health;
        this.damage = damage;

    }

    // 判断鼠标点击
    public boolean checkMouseClick(int x_, int y_) {
        if (x_ >= x && x_ <= (x + width) && y_ >= y && y_ <= (y + height))
            return true;
        else
            return false;
    }

    public abstract void loadImages();

    // the zombie is hypo&&attack other zombies when it ate a HypnoShroom

    public void loadFrames(ArrayList<BufferedImage> frames, String name, int image_x, Color colorkey) {
        TreeSet<Tool.Img> frame_list = (TreeSet<Tool.Img>) Tool.GFX.get(name);
        for (Tool.Img frame : frame_list) {
            BufferedImage rect = frame.image;
            int width = rect.getWidth();
            int height = rect.getHeight();
            width -= image_x;
            // frames.add(Tool.adjustAlpha( frame.image,Constants.BLACK));
            frames.add(Tool.adjustAlpha( frame.image.getSubimage(image_x, 0, width, height),colorkey));
            // tool.get_image(frame, image_x, 0, width, height, colorkey));
        }
    }

    public void update() {
        // this.current_time = game_info[Constants.CURRENT_TIME];
        this.current_time = System.currentTimeMillis();
        this.handleState();
        this.updateIceSlow();
        this.animation();
    }

    public void handleState() {
        if (this.state == Constants.WALK)
            this.walking();
        if (this.state == Constants.ATTACK)
            this.attacking();
        if (this.state == Constants.DIE)
            this.dying();
        if (this.state == Constants.FREEZE)
            this.freezing();
    }

    public void walking() {
        if (this.health <= 0) {
            this.setDie();
        } else if (this.health <= Constants.LOSTHEAD_HEALTH && this.losHead == false) {
            this.changeFrames(this.losthead_walk_frames);
            this.setLostHead();
        } else if (this.health <= Constants.NORMAL_HEALTH && this.helmet) {
            this.changeFrames(this.walk_frames);
            this.helmet = false;
            if (this.name == Constants.NEWSPAPER_ZOMBIE) {
                this.speed = 2;
            }
        }
        if (this.current_time - this.walk_timer > Constants.ZOMBIE_WALK_INTERVAL *
                this.getTimeRatio()) {
            this.walk_timer = this.current_time;
            if (this.is_hypno) {
                this.x += this.speed;
            } else {
                this.x -= this.speed;
            }
        }
    }

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
        if (this.current_time - this.attack_timer > Constants.ATTACK_INTERVAL * this.getTimeRatio()) {
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

    public void dying() {

    }

    public void freezing() {
        if (this.health <= 0) {
            this.setDie();
        } else if (this.health <= Constants.LOSTHEAD_HEALTH && this.losHead == false) {
            if (this.old_state == Constants.WALK) {
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

    public void setLostHead() {
        this.losHead = true;
        // if (this.head_group.size()!=0) {
        // this.head_group.add(new ZombieHead(this.centerx, this.bottom));
        // }
    }

    public void changeFrames(ArrayList<BufferedImage> frames) {
        // '''change image frames&&modify rect position'''
        this.frames = frames;
        this.frame_num = this.frames.size();
        this.frame_index = 0;
        this.image = this.frames.get(this.frame_index);

    }

    public void animation() {
        if (this.state == Constants.FREEZE) {

            this.image=Tool.adjustBrightness(this.image, 192);
            return;
        }
        if (this.current_time - this.animate_timer > this.animate_interval *
                this.getTimeRatio()) {
            this.frame_index += 1;
            if (this.frame_index >= this.frame_num) {
                if (this.state == Constants.DIE) {
                    // this.kill();
                    return;
                }
                this.frame_index = 0;
            }
            this.animate_timer = this.current_time;
        }
        this.image = this.frames.get(this.frame_index);
        if (this.is_hypno) {
            // this.image = pg.transform.flip(this.image, true, false)
        }
        if (this.current_time - this.hit_timer >= 200) {

            this.image=Tool.adjustBrightness(this.image, 255);
        } else {

            this.image=Tool.adjustBrightness(this.image, 192);
        }
    }

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
            if (this.current_time - this.ice_slow_timer > Constants.ICE_SLOW_TIME) {
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
    // public void setAttack(Plant prey,Boolean is_plant) {
    // this.prey = prey;
    // // # prey can be plant||other zombies
    // this.prey_is_plant = is_plant;
    // this.state = Constants.ATTACK;
    // this.attack_timer = this.current_time;
    // this.animate_interval = 100

    // if (this.helmet) {
    // this.changeFrames(this.helmet_attack_frames);
    // }else if (this.losHead) {
    // this.changeFrames(this.losthead_attack_frames);
    // }else {
    // this.changeFrames(this.attack_frames);
    // }
    // }
    public void setDie() {
        this.state = Constants.DIE;
        this.animate_interval = 200;
        this.changeFrames(this.die_frames);
    }

    public void setBoomDie() {
        this.state = Constants.DIE;
        this.animate_interval = 200;
        this.changeFrames(this.boomdie_frames);
    }

    public void setFreeze(BufferedImage ice_trap_image) {
        this.old_state = this.state;
        this.state = Constants.FREEZE;
        this.freeze_timer = this.current_time;
        this.ice_trap_image = ice_trap_image;
        // this.ice_trap_rect = ice_trap_image.get_rect()
        // this.ice_centerx = this.centerx;
        // this.ice_bottom = this.bottom;

    }

    // public void drawFreezeTrap() {
    // if (this.state == Constants.FREEZE) {

    // surface.blit(this.ice_trap_image, this.ice_trap_rect);
    // }
    // }
    public void setHypno() {
        this.is_hypno = true;
        this.setWalk();
    }

}
