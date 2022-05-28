package core.zombies;

import java.io.File;
import java.io.IOException;

import core.json.FileUtils;
import core.json.JSONException;
import core.json.JSONObject;

// public class Demo {
//     public static void main(String args[]) throws IOException {

//         File file=new File(""resources/data/entity/zombie.json"");
//         String content= FileUtils.readFileToString(file,"UTF-8");
//         JSONObject jsonObject=new JSONObject(content);
//         System.out.println("姓名是："+jsonObject.getString("name"));
//         System.out.println("年龄："+jsonObject.getDouble("age"));
//         System.out.println("学到的技能："+jsonObject.getJSONArray("major"));
//         System.out.println("国家："+jsonObject.getJSONObject("Nativeplace").getString("country"));

//     }
// }

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