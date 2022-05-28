package core.plants;

import core.Constants;
import core.zombies.Zombie;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class CherryBomb extends Plant{  
    boolean start_boom = false;
    long bomb_timer = 0;
    int explode_y_range = 3*Constants.GRID_Y_SIZE;
    int explode_x_range = 3*Constants.GRID_X_SIZE;

    public CherryBomb(int x, int y){
        super(Constants.PLANT_HEALTH, x, y, Constants.CHERRYBOMB, 1);
        setAttack();
    }

    public void setBoom(){
        /**
         * BufferedImage frame = tool.GFX[Constants.CHERRY_BOOM_IMAGE];
         * this.rect = this.image.get_rect();
         * 
         * old_rect = this.rect
         * this.image = tool.get_image(frame, image_x, 0, width, height, colorkey));
         * this.rect = this.image.get_rect();
         * */
        start_boom = true;
    }

    @Override
    public void animation(){
        if(start_boom == true){
            if(bomb_timer == 0)
                bomb_timer = current_time;
            else if(current_time - bomb_timer > 500)
                setDamage(1000);
        }
        else{
            if(current_time - animate_timer > 100){
                frame_index +=1;
                if(frame_index >= frame_num){
                    setBoom();
                    return;
                }
                animate_timer = current_time;
            }
            image = frames.get(frame_index);
        }
    }

    @Override
    public boolean canAttack(Zombie zombie){
        if(start_boom == true && 
        Math.abs(zombie.x - x) <= explode_x_range &&
        Math.abs(zombie.y - y) <= explode_y_range )
            return true;
        else
            return false;
    }
}
