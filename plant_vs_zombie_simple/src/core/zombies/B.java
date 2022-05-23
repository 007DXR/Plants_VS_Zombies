package core.zombies;

import java.io.File;
import java.io.IOException;

import core.json.FileUtils;
import core.json.JSONException;
import core.json.JSONObject;
public class B
{
    public void toolZombie() throws IOException{
        File file=new File("resources/data/entity/zombie.json" );

        String content= FileUtils.readFileToString(file,"UTF-8");
        JSONObject jsonObject=new JSONObject(content);
        JSONObject toolZombieRect=jsonObject.getJSONObject("zombie_image_rect");
        System.out.println(toolZombieRect.getJSONObject("BoomDie").getInt("x"));
    }
    public static void main(String[] args) throws IOException {
        B dxr=new B();
        dxr.toolZombie();
    }
}