package core.game;
import core.json.FileUtils;
import core.json.JSONObject;
import core.plants.Plant;
import core.zombies.Zombie;
import core.json.JSONArray;
import core.State;
import core.Tool;
import core.bullets.Bullet;
import core.car.Car;
import core.Constants;
import core.component.MenuBar;
import core.component.Panel;
import core.game.*;
import core.zombies.*;
import core.plants.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import core.component.Card;
import core.component.GameMap;
import core.component.Start;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

class ZombieListItem {
    public int time;
    public String name;
    public int map_y;
    public ZombieListItem(int time_, String name_, int map_y_) {
        time = time_;
        name = name_;
        map_y = map_y_;
    }
}

/// 接口类，指示传入判断碰撞的函数类型
abstract class CollidedFunc {
    /// param指示碰撞区域缩放的比例
    /// return true表示发生碰撞
    double ratio;
    public CollidedFunc(double ratio) {
        this.ratio = ratio;
    }
    abstract public boolean collid(Sprite x, Sprite y);
}

/// 圆形碰撞发生函数
class CircleCollidedFunc extends CollidedFunc
{
    public CircleCollidedFunc(double ratio) {
        super(ratio);
    }
    public boolean collid(Sprite x, Sprite y) {
        double xr,yr;
        xr = x.rect.width() * 0.5 * ratio;
        yr = y.rect.width() * 0.5 * ratio;
        return (xr + yr) * (xr + yr) > 
        (x.rect.centerx() - y.rect.centerx()) * ((x.rect.centerx() - y.rect.centerx()))
        + (x.rect.centery() - y.rect.centery()) * (x.rect.centery() - y.rect.centery());
    }
}
/// 矩形碰撞发生函数，实际上根本没用到过
class RectCollidedFunc extends CollidedFunc
{
    public RectCollidedFunc(double ratio) {
        super(ratio);
    }
    public boolean collid(Sprite x, Sprite y) {
        return x.rect.intersect(y.rect, ratio);
    }
}

// 逻辑类，负责判断游戏状态并调用相应函数
public class Level extends State {
    ///temporary;
    int mouseX,mouseY;
    int map_y_len;
    int backgroundType;
    Sprite background;
    GameMap map;
    JSONObject mapData;
    double zombieStartTime;
    ArrayList<ZombieListItem> zombieList;
    String state;
    int barType;
    ArrayList<Card> cardPool;
    Panel panel;
    MenuBar menubar;
    /// elements added into surface
    JPanel surface;
    Start start; 

    /// work in map directory
    private Path rootDir = Paths.get("resources/data/map");
    
    public Level() {
        super();
    }
    /// 初始化
    @Override
    public void startUp(long current_time, JSONObject persist) {
        //activate window
        this.current_time = current_time;
        surface = Main.surface;
        game_info = persist;
        this.persist = persist;
        game_info.remove(c.CURRENT_TIME);
        game_info.put(c.CURRENT_TIME, current_time);
        map_y_len = c.GRID_Y_LEN;
        map = new GameMap(c.GRID_X_LEN, map_y_len);
        mapData = new JSONObject();
        loadMap();
        setupBackgroud();
        // initStart(); 
        initState();
    }
    /// 初始化菜单
    public void menustartUp(long current_time, JSONObject persist) {
        //activate window
        this.current_time = current_time;
        surface = Main.surface;
        game_info = persist;
        this.persist = persist;
        game_info.remove(c.CURRENT_TIME);
        game_info.put(c.CURRENT_TIME, current_time);
        map_y_len = c.GRID_Y_LEN;
        map = new GameMap(c.GRID_X_LEN, map_y_len);
        mapData = new JSONObject();
        // loadMap();
        setupMain();
        initStart(); 
        // initState();
    }
    /// 读入map文件信息
    public void loadMap() {
        String filePath = "./resources/data/map/level_" + (int)game_info.get(c.LEVEL_NUM) + ".json";
        mapData = loadJsonFile(filePath);
    }
    Rect bgRect;
    int viewportLeft;
    int viewportWidth, viewportHeight;
    Sprite level;
    /// 创建背景
    public void setupBackgroud() {
    	int imgIndex = (int)mapData.get(c.BACKGROUND_TYPE);
    	backgroundType = imgIndex;
        TreeSet<Tool.Img> imgSet = Tool.GFX.get(c.BACKGROUND_NAME);
        // TreeSet<Tool.Img> imgSet = Tool.GFX.get("PeaNormal_0");
        // int i = 0;
        System.out.println("-------------------------(imgIndex);"+imgIndex);
        for(Tool.Img img: imgSet) {
            
            if(img.tag == imgIndex) {
                background = new Sprite(img.image);
                break;
            }
        }
        bgRect = background.rect;
//        level = pg.Surface((bg_rect.w, bg_rect.h)).convert();
        viewportLeft = c.BACKGROUND_OFFSET_X;
        viewportWidth = c.SCREEN_WIDTH;
        viewportHeight = c.SCREEN_HEIGHT;
    }
    /// 创建背景和图片偏移量
    public void setupMain() {
    	int imgIndex = 6;
    	backgroundType = imgIndex;
        TreeSet<Tool.Img> imgSet = Tool.GFX.get(c.BACKGROUND_NAME);
        // TreeSet<Tool.Img> imgSet = Tool.GFX.get("PeaNormal_0");
        // int i = 0;
        System.out.println("-------------------------(imgIndex);"+imgIndex);
        for(Tool.Img img: imgSet) {
            
            if(img.tag == imgIndex) {
                background = new Sprite(Tool.resize(img.image, 1.2));
                break;
            }
        }
        bgRect = background.rect;
//        level = pg.Surface((bg_rect.w, bg_rect.h)).convert();
        viewportLeft = c.BACKGROUND_OFFSET_X;
        viewportWidth = c.SCREEN_WIDTH;
        viewportHeight = c.SCREEN_HEIGHT;
    }
    // 声明各个group
    ArrayList<Sun> sunGroup;
    Group headGroup;
    ArrayList<Group> plantGroups;
    ArrayList<Group> zombieGroups;
    ArrayList<Group> hypnoZombieGroups;
    ArrayList<Group> bulletGroups;
    public void setupGroups() {
        sunGroup = new ArrayList<>();
        // sunGroup = new Group();
        headGroup = new Group();
        plantGroups = new ArrayList<>();
        zombieGroups = new ArrayList<>();
        hypnoZombieGroups = new ArrayList<>();
        bulletGroups = new ArrayList<>();
        for (int i = 0; i < map_y_len; ++i) {
            plantGroups.add(new Group());
            zombieGroups.add(new Group());
            hypnoZombieGroups.add(new Group());
            bulletGroups.add(new Group());
        }
    }
    // 创建僵尸
    public void setupZombies() {
        JSONArray dataArray = mapData.getJSONArray(c.ZOMBIE_LIST);
        this.zombieList = new ArrayList<ZombieListItem>();
        for(int i = 0; i < dataArray.length(); ++i) {
            JSONObject object = dataArray.getJSONObject(i);
            ZombieListItem item = new ZombieListItem(
                object.getInt("time"),
                object.getString("name"),
                object.getInt("map_y")
            );
            zombieList.add(item);
        }
        zombieStartTime = 0.0;
    }
    /// 创建小车
    ArrayList<Car> cars;
    public void setupCars() {
        cars = new ArrayList<>();
        for (int i = 0; i < map_y_len; ++i) {
            ArrayList<Integer> pos = map.getMapGridPos(0, i);
            int y = pos.get(1);
            cars.add(new Car(-25, y+20, i));
        }
    }
    /// 判断游戏阶段，更新游戏状态
    @Override
    public void update(Graphics g, int time,ArrayList<Integer> mousePos, ArrayList<Boolean> mouseClick) {
        current_time = time;
        mouseX = Main.x;
        mouseY = Main.y;
        game_info.put(c.CURRENT_TIME, time);
        if (state.equals(c.MAIN_MENU)){
            start(mousePos, mouseClick);
        } else if (state.equals(c.CHOOSE)) {
            choose(mousePos, mouseClick);
        } else if (state.equals(c.PLAY)) {
            play(g, mousePos, mouseClick);
        }
    }
    /// 初始化开始画面
    public void initStart(){
        state = c.MAIN_MENU; 
        start = new Start(330, 140, 2.5);
    }
    /// 初始化游戏阶段
    public void initState() {
        // 尝试获取choosebarType, 为-1则没找到，用默认值
        if (mapData.optInt(c.CHOOSEBAR_TYPE, -1) != -1) {
            barType = mapData.getInt(c.CHOOSEBAR_TYPE);
        } else {
            barType = c.CHOOSEBAR_STATIC;
        }
        if (barType == c.CHOOSEBAR_STATIC) {
            initChoose();
        } else {
            JSONArray data = mapData.getJSONArray(c.CARD_POOL);
            cardPool = new ArrayList<>();
            //get card pool
            /*
            for (int i = 0; i < data.length(); ++i) {
                String plantName = data.getString(i);
                //识别这些名字对应的植物编号
            }*/
            //initPlay(cardPool.toArray(new int[cardPool.size()]));
        }
    }
    /// 初始化选择状态
    public void initChoose() {
        state = c.CHOOSE;
        panel = new Panel(c.all_card_list, mapData.optInt(c.INIT_SUN_NAME, 50));
    }
    /// 选择阶段逻辑
    public void choose(ArrayList<Integer> mousePos, ArrayList<Boolean> mouseClick) {
        if (!mousePos.isEmpty() && mouseClick.get(0)==true) {
            panel.checkCardClick(mousePos.get(0), mousePos.get(1));
            if (panel.checkStartButtonClick(mousePos.get(0), mousePos.get(1))) {
                initPlay(panel.getSelectedCards());
            }
        }
    }
    /// 开始游戏逻辑
    public void start(ArrayList<Integer> mousePos, ArrayList<Boolean> mouseClick) {
        if (!mousePos.isEmpty() && mouseClick.get(0)==true) {
            if (start.checkMouseClick(mousePos.get(0), mousePos.get(1))) {
                loadMap();
                setupBackgroud();
                initState();
            }
        }
    }
    public boolean dragPlant;
    public boolean hintPlant;
    public boolean produceSun;
    public double sunTimer;
    /// 初始化游戏逻辑
    public void initPlay(int[] cardList) {
        state = c.PLAY;
        if (barType == c.CHOOSEBAR_STATIC) {
            menubar = new MenuBar(cardList, mapData.optInt(c.INIT_SUN_NAME, 50));
        } else {
//            menubar = new MoveBar(cardList);
        }
        dragPlant = false;
        hintImage = null;
        hintPlant = false;
        if (backgroundType == c.BACKGROUND_DAY && barType == c.CHOOSEBAR_STATIC) {
            produceSun = true;
        }
        else {
            produceSun = false;
        }
        sunTimer = current_time;

        removeMouseImage();
        setupGroups();
        setupZombies();
        setupCars();
    }
    /// 更新游戏阶段
    public void play(Graphics g, ArrayList<Integer> mousePos, ArrayList<Boolean> mouseClick) {
        if (zombieStartTime == 0.0) {
            zombieStartTime = current_time;
        }
        else if(zombieList.size() > 0) {
            ZombieListItem data = zombieList.get(0);
            if (data.time <= (current_time - zombieStartTime)) {
                createZombie(data.name, data.map_y);
                zombieList.remove(data);
            }
        }
        ArrayList<Object> list = new ArrayList<>();
        list.add(game_info);
        /// 更新各个对象
        for (int i = 0; i < map_y_len; ++i) {
            bulletGroups.get(i).update();
            plantGroups.get(i).update();
            zombieGroups.get(i).update();
            hypnoZombieGroups.get(i).update();
            //迷惑的功能，暂时还没什么用
            /*
            for (zombie in hypno_zombie_groups[i]) {
                if zombie.rect.x > c.SCREEN_WIDTH:
                    zombie.kill()
            }*/
        }
        headGroup.update();
        // sunGroup.update();
        menubar.update((int)current_time);
        // 判断鼠标拖拽情况
        if (!dragPlant && !mousePos.isEmpty() && mouseClick.get(0)==true) {
            Card result = menubar.checkCardClick(mousePos.get(0),mousePos.get(1));
            if (result != null) {
                setupMouseImage(c.plant_name_list[result.name_index], result);
            }
        }
        else if (dragPlant) {
            if (mouseClick.get(1)==true) {
                removeMouseImage();
            }
            else if (mouseClick.get(0)==true) {
                if (menubar.checkMenuBarClick(mousePos.get(0), mousePos.get(1))) {
                    removeMouseImage();
                }
                else {
                    addPlant(g);
                }
            }
            else {
                setupHintImage(g);
            }
        }
        // 检测阳光生成
        if (produceSun) {
            if ((current_time - sunTimer) > c.PRODUCE_SUN_INTERVAL) {
                sunTimer = current_time;
                ArrayList<Integer> mapRandom = map.getRandomMapIndex();
                ArrayList<Integer> mapPos = map.getMapGridPos(
                    mapRandom.get(0), 
                    mapRandom.get(1));
                    
                    // test_scarlyw
               sunGroup.add(new Sun(mapPos.get(0).intValue(), 0, 
               mapPos.get(0).intValue(), mapPos.get(1).intValue(), 0.9));
            }
        }
        // 检查阳光碰撞
        if (!dragPlant && !mousePos.isEmpty() && mouseClick.size() > 0 
            && mouseClick.get(0) == true) {
            for (Sun sun : sunGroup) {
                if (sun.checkMouseClick(mouseX, mouseY)) {
                    System.out.println("get_sun");
                    menubar.increaseSunValue(sun.sun_value);
                }
            }
        }
        for (Sun sun:sunGroup) {
            sun.update();
        }
        for (Car car:cars) {
            car.update();
        }
        // 检查对象之间的碰撞情况
        checkBulletCollisions();
        checkZombieCollisions();
        checkPlants();
        checkCarCollisions();
        checkGameState();
    }
    /// 生成僵尸
    public void createZombie(String name, int map_y) {
        ArrayList<Integer> Pos = map.getMapGridPos(0, map_y);
        int x = Pos.get(0);
        int y = Pos.get(1);
        // 实例化僵尸
        if (name.equals(c.NORMAL_ZOMBIE)) {
            zombieGroups.get(map_y).add(new NormalZombie(c.ZOMBIE_START_X, y-c.MAP_OFFSET_Y+20, headGroup));
        }
        else if (name.equals(c.CONEHEAD_ZOMBIE)) {
            zombieGroups.get(map_y).add(new ConeHeadZombie(c.ZOMBIE_START_X, y-c.MAP_OFFSET_Y+20, headGroup));
        }
        else if (name.equals(c.BUCKETHEAD_ZOMBIE)) {
            zombieGroups.get(map_y).add(new BucketHeadZombie(c.ZOMBIE_START_X, y-c.MAP_OFFSET_Y+20, headGroup));
        }
        else if (name.equals(c.FLAG_ZOMBIE)) {
            zombieGroups.get(map_y).add(new FlagZombie(c.ZOMBIE_START_X, y-c.MAP_OFFSET_Y+20, headGroup));
        }
        else if (name.equals(c.NEWSPAPER_ZOMBIE)) {
            zombieGroups.get(map_y).add(new NewspaperZombie(c.ZOMBIE_START_X, y-c.MAP_OFFSET_Y+20, headGroup));
        }
    }
    Sprite hintImage;
    Rect hintRect;
    Plant newPlant;
    // 检测是否能种植
    public ArrayList<Integer> canSeedPlant() {
        return map.showPlant(mouseX, mouseY);
    }
    // 种植植物
    public void addPlant(Graphics g) {
        ArrayList<Integer> pos = canSeedPlant();
        if (pos.isEmpty()) {
            return;
        }
        if (hintImage == null) {
            setupHintImage(g);
        }
        // int x = hintRect.centerx();
        // int y = hintRect.centery();
        // 校正植物坐标
        ArrayList<Integer> mapIndex = map.getMapIndex(mouseX, mouseY);
        int map_x = mapIndex.get(0);
        int map_y = mapIndex.get(1);
        map.doPlant(map_x, map_y);
        // System.out.println(map_x);
        // System.out.println(map_y);
        // 得到实际坐标
        ArrayList<Integer> actualIndex = map.getMapGridPos(map_x, map_y);
        int x = actualIndex.get(0);
        int y = actualIndex.get(1);
        // 实例化植物
        if (plantName.equals(c.WALLNUT)) {
            newPlant = new WallNut(x, y);
        }
        else if (plantName.equals(c.SQUASH)) {
            newPlant = new Squash(x, y);
        }
        else if (plantName.equals(c.SUNFLOWER)) {
            newPlant = new SunFlower(x, y, sunGroup);
        }
        else if (plantName.equals(c.PEASHOOTER)) {
            newPlant = new PeaShooter(x, y, bulletGroups.get(map_y));
        }
        else if (plantName.equals(c.SNOWPEASHOOTER)) {
            newPlant = new SnowPeaShooter(x, y, bulletGroups.get(map_y));
        }
        else if (plantName.equals(c.CHERRYBOMB)) {
            newPlant = new CherryBomb(x, y);
        }
        else if (plantName.equals(c.THREEPEASHOOTER)) {
            newPlant = new ThreePeaShooter(x, y, bulletGroups, map_y);
        }
        else if (plantName.equals(c.REPEATERPEA)) {
            newPlant = new RepeaterPea(x, y, bulletGroups.get(map_y));
        }
        else if (plantName.equals(c.CHOMPER)) {
            newPlant = new Chomper(x, y);
        }
        else if (plantName.equals(c.PUFFSHROOM)) {
            newPlant = new PuffShroom(x, y, bulletGroups.get(map_y));
        }
        else if (plantName.equals(c.POTATOMINE)){
            newPlant = new PotatoMine(x, y);
        }
        else if (plantName.equals(c.SPIKEWEED)) {
            newPlant = new Spikeweed(x, y);
        }
        else if (plantName.equals(c.JALAPENO)) {
            newPlant = new Jalapeno(x, y);
        }
        else if (plantName.equals(c.SCAREDYSHROOM)) {
            newPlant = new ScaredyShroom(x, y, bulletGroups.get(map_y));
        }
        else if (plantName.equals(c.SUNSHROOM)) {
            newPlant = new SunShroom(x, y, sunGroup);
        }
        else if (plantName.equals(c.ICESHROOM)) {
            newPlant = new IceShroom(x, y);
        }
        else if (plantName.equals(c.HYPNOSHROOM)) {
            newPlant = new HypnoShroom(x, y);
        }

        if (newPlant.can_sleep && backgroundType == c.BACKGROUND_DAY) {
            newPlant.setSleep();
        }
        // 增加植物实例
        plantGroups.get(map_y).add(newPlant);
        if (barType == c.CHOOSEBAR_STATIC) {
            menubar.decreaseSunValue(selectPlant.sun_cost);
            menubar.setCardFrozenTime(plantName);
        }
/*        else {
            menubar.deleateCard(selectPlant);
        }*/
        removeMouseImage();

    }
    // 创建提示对象
    public void setupHintImage(Graphics g) {
        ArrayList<Integer> pos = canSeedPlant();
        if (!pos.isEmpty() && mouseImage != null) {
            if (hintImage != null && pos.get(0) == hintRect.left &&
                pos.get(1) == hintRect.top) {
                return;
            }
            //画图并保存属性;
            hintImage = new Sprite(mouseImage.rect.image);
            hintImage.rect.image=Tool.adjustHint(hintImage.rect.image);
            hintRect = hintImage.rect;
            hintRect.adjustlt(pos.get(0), pos.get(1));
            hintImage.paintObject(g);
            hintPlant = true;
        }
        else{
            hintPlant = false;
        }
    }
    Sprite mouseImage;
    Rect mouseRect;
    String plantName;
    Card selectPlant;
    // 创建鼠标拖拽对象
    public void setupMouseImage(String plantName, Card selectPlant) {
        int x,y,width,height;
        Color color;
        TreeSet<Tool.Img> frameList = Tool.GFX.get(plantName);
        if (Tool.PLANT_RECT.optJSONObject(plantName) != null) {
            JSONObject data = Tool.PLANT_RECT.getJSONObject(plantName);
            x = data.getInt("x");
            y = data.getInt("y");
            width = data.getInt("width");
            height = data.getInt("height");
        }
        else{
            x = 0;
            y = 0;
            BufferedImage rect = frameList.first().image;
            width = rect.getWidth();
            height = rect.getHeight();
        }
        // 去掉图片白边
        if (plantName.equals(c.POTATOMINE) || plantName.equals(c.SQUASH) ||
            plantName.equals(c.SPIKEWEED) || plantName.equals(c.JALAPENO) ||
            plantName.equals(c.SCAREDYSHROOM) || plantName.equals(c.SUNSHROOM) ||
            plantName.equals(c.ICESHROOM) || plantName.equals(c.HYPNOSHROOM)) {
            color = c.WHITE;
        }
        else {
            color = c.BLACK;
        }
        // 创建拖拽对象
        width -= x;
        mouseImage = new Sprite(new Rect(Tool.adjustAlpha(frameList.first().image.getSubimage(x, y, width, height), color), x, y));
        mouseRect = mouseImage.rect;
        dragPlant = true;
        this.plantName = plantName;
        this.selectPlant = selectPlant;
    }
    public void removeMouseImage() {
        dragPlant = false;
        mouseImage = null;
        hintImage = null;
        hintPlant = false;
    }
    // 检查子弹僵尸碰撞情况
    public void checkBulletCollisions() {
        // 0.7倍
        CollidedFunc collidedFunc = new CircleCollidedFunc(0.7);
        for (int i = 0; i < map_y_len; ++i) {
            for (Sprite sprite : bulletGroups.get(i).list) {
                Bullet bullet = (Bullet)sprite;
                // 检查子弹状态
                if ( bullet.state.equals(c.FLY) ) {
                    // 检测碰撞到的僵尸
                    Zombie zombie = (Zombie)bullet.spritecollideany(zombieGroups.get(i), collidedFunc);
                    if (zombie != null && zombie.state != c.DIE) {
                        zombie.setDamage(bullet.damage, bullet.ice);
                        bullet.setExplode();
                    }
                }
            }
        }
    }
    // 检查僵尸碰撞情况，主要用于袭击植物，迷惑菇功能暂时缺失
    public void checkZombieCollisions() {
        double ratio = 0.7;
        CollidedFunc collidedFunc = new CircleCollidedFunc(ratio);
        for (int i = 0; i < map_y_len; ++i) {
            ArrayList<Zombie> hypoZombies = new ArrayList();
            for(Sprite sprite : zombieGroups.get(i).list) {
                Zombie zombie = (Zombie)sprite;
                if (zombie.state != c.WALK) {
                    continue;
                }
                Plant plant = (Plant)zombie.spritecollideany(this.plantGroups.get(i), collidedFunc);
                if (plant != null) {
                    if (plant.name != c.SPIKEWEED) {
                        zombie.setAttack(plant, true);
                    }
                }
            }
/*
            for (Sprite sprite : this.hypnoZombieGroups.get(i).list) {
                Zombie hypno_zombie = (Zombie) sprite;
                if (hypno_zombie.health <= 0) {
                    continue;
                }
                ArrayList<Sprite> zombieList = hypno_zombie.spritecollide(
                               this.zombieGroups.get(i), false,collidedFunc);
                for (Sprite k : zombieList) {
                    Zombie zombie = (Zombie) k;
                    if (zombie.state.equals(c.DIE)) {
                        continue;
                    }
                    if (zombie.state.equals(c.WALK)) {
                        zombie.setAttack(hypno_zombie, false);
                    }
                    if (hypno_zombie.state.equals(c.WALK)) {
                        hypno_zombie.setAttack(zombie, false);
                    }
                }
            } */
        }
    }
    /// 小车和僵尸碰撞情况
    public void checkCarCollisions() {
        CollidedFunc collidedFunc = new CircleCollidedFunc(0.8);
        LinkedList<Sprite> del = new LinkedList<>();
        for (Car car : this.cars) {
            ArrayList<Sprite> sprits = car.spritecollide(this.zombieGroups.get(car.map_y), false, collidedFunc);
            for (Sprite sprite : sprits) {
                Zombie zombie = (Zombie) sprite;
                if (zombie != null && zombie.state != c.DIE) {
                    car.setWalk();
                    zombie.setDie();
                }
            }
            if (car.dead) {
                del.add(car);
            }
        }
        cars.removeAll(del);
    }
    /// 炸弹
    public void boomZombies(int x, int map_y, int y_range, int x_range) {
        for (int i = 0; i < this.map_y_len; ++i) {
            if (Math.abs(i - map_y) > y_range) {
                continue;
            }
            for (Sprite sprite: this.zombieGroups.get(i).list) {
                Zombie zombie = (Zombie)sprite;
                if (Math.abs(zombie.rect.centerx() - x) <= x_range) {
                    zombie.setBoomDie();
                }
            }
        }
    }
    /// 冻结僵尸
    public void freezeZombies(Plant plant) {
        for (int i = 0; i < this.map_y_len; ++i) {
            for (Sprite sprite: this.zombieGroups.get(i).list) {
                Zombie zombie = (Zombie) sprite;
                if (zombie.rect.centerx() < c.SCREEN_WIDTH) {
                    zombie.setFreeze(((IceShroom)plant).trap_frames.get(0));
                }
            }
        }
    }
    /// 移除死亡植物(包括爆炸死亡)
    public void killPlant(Plant plant) {
        int x,y;
        if(plant instanceof Squash) {
            Squash tmp = (Squash) plant;
            x = tmp.orig_pos[0];
            y = tmp.orig_pos[1];
        } else {
            x = plant.rect.centerx();
            y = plant.rect.centery();
        }
        ArrayList<Integer> mapPos = this.map.getMapIndex(x, y);
        int map_x = mapPos.get(0);
        int map_y = mapPos.get(1);
        if (this.barType != c.CHOSSEBAR_BOWLING) {
            this.map.setMapGridType(map_x, map_y, c.MAP_EMPTY);
        }
        // 检测是否是爆炸死亡
        if (plant.name.equals(c.CHERRYBOMB) || plant.name.equals(c.JALAPENO) ||
            (plant.name.equals(c.POTATOMINE) && ! plant.is_init) ) {
            this.boomZombies(plant.rect.centerx(), map_y, plant.explode_y_range,
                            plant.explode_x_range);
        }
        // 检查是否冰冻
        else if (plant.name.equals(c.ICESHROOM) && plant.state.equals(c.SLEEP)) {
            this.freezeZombies(plant);
        }
        // 暂时移除催眠
        /*
        else if (plant.name.equals(c.HYPNOSHROOM) && !plant.state.equals(c.SLEEP)) {
            zombie = plant.kill_zombie
            zombie.setHypno()
            _, map_y = this.map.getMapIndex(zombie.rect.centerx, zombie.rect.bottom)
            this.zombie_groups[map_y].remove(zombie)
            this.hypno_zombie_groups[map_y].add(zombie)
        }*/
        plant.kill();
    }
    /// index i;
    /// 检测某个植物的状态，设置攻击
    public void checkPlant(Plant plant, int i) {
        boolean canAttack;
        boolean needCry;
        int zombieLen = this.zombieGroups.get(i).list.size();
        if (plant.name.equals(c.THREEPEASHOOTER)) {
            if (plant.state.equals(c.IDLE)) {
                if (zombieLen > 0) {
                    plant.setAttack();
                }
                else if ((i - 1) >= 0 && this.zombieGroups.get(i-1).size() > 0) {
                    plant.setAttack();
                }
                else if ((i + 1) < this.map_y_len && this.zombieGroups.get(i+1).size() > 0) {
                    plant.setAttack();
                }
            }
            else if (plant.state.equals(c.ATTACK)) {
                if (zombieLen > 0) {
                    // do nothing
                }
                else if ((i-1) >= 0 && this.zombieGroups.get(i-1).size() > 0) {
                    // do nothing
                }
                else if ((i+1) < this.map_y_len && this.zombieGroups.get(i+1).size() > 0) {
                    // do nothing
                }
                else{
                    plant.setIdle();
                }
            }
        }
        else if (plant.name.equals(c.CHOMPER)) {
            for (Sprite sprite : this.zombieGroups.get(i).list) {
                Zombie zombie = (Zombie)sprite;
                if (plant.canAttack(zombie)) {
                    plant.setAttack(zombie, this.zombieGroups.get(i));
                    break;
                }
            }
        }
        else if (plant.name.equals(c.POTATOMINE)) {
            for (Sprite sprite : this.zombieGroups.get(i).list) {
                Zombie zombie = (Zombie)sprite;
                if (plant.canAttack(zombie)) {
                    plant.setAttack();
                    break;
                }
            }
        }
        else if (plant.name.equals(c.SQUASH)) {
            for (Sprite sprite : this.zombieGroups.get(i).list) {
                Zombie zombie = (Zombie)sprite;
                if (plant.canAttack(zombie)) {
                    plant.setAttack(zombie, this.zombieGroups.get(i));
                    break;
                }
            }
        }
        else if (plant.name.equals(c.SPIKEWEED)) {
            canAttack = false;
            for (Sprite sprite: this.zombieGroups.get(i).list) {
                Zombie zombie = (Zombie)sprite;
                if (plant.canAttack(zombie)) {
                    canAttack = true;
                    break;
                }
            }
            if (plant.state.equals(c.IDLE) && canAttack) {
                plant.setAttack(null, this.zombieGroups.get(i));
            }
            else if (plant.state.equals(c.ATTACK) && !canAttack) {
                plant.setIdle();
            }
        }
        else if (plant.name.equals(c.SCAREDYSHROOM)) {
            needCry = false;
            canAttack = false;
            for (Sprite sprite : this.zombieGroups.get(i).list) {
                Zombie zombie = (Zombie)sprite;
                if (plant.needCry(zombie)) {
                    needCry = true;
                    break;
                }
                else if (plant.canAttack(zombie)) {
                    canAttack = true;
                }
            }
            if (needCry) {
                if (plant.state != c.CRY) {
                    plant.setCry();
                }
            }
            else if (canAttack) {
                if (plant.state != c.ATTACK) {
                    plant.setAttack();
                }
            }
            else if (plant.state != c.IDLE) {
                plant.setIdle();
            }
        }
        else {
            canAttack = false;
            if (plant.state.equals(c.IDLE) && zombieLen > 0) {
                for (Sprite sprite : this.zombieGroups.get(i).list) {
                    Zombie zombie = (Zombie)sprite;
                    if (plant.canAttack(zombie)) {
                        canAttack = true;
                        break;
                    }
                }
            }
            if (plant.state.equals(c.IDLE) && canAttack) {
                plant.setAttack();
            }
            else if (plant.state.equals(c.ATTACK) && ! canAttack) {
                plant.setIdle();
            }
        }
    }
    /// 检测所有植物
    public void checkPlants() {
        for (int i = 0; i < this.map_y_len; ++i) {
            for (Sprite sprite :this.plantGroups.get(i).list) {
                Plant plant = (Plant)sprite;
                if (plant.state != c.SLEEP) {
                    this.checkPlant(plant, i);
                }
                if (plant.health <= 0) {
                    this.killPlant(plant);
                }
            }
        }
    }
    /// 检查胜利条件
    public boolean checkVictory() {
        if (this.zombieList.size() > 0) {
            return false;
        }
        for (int i = 0; i < this.map_y_len; ++i) {
            if (this.zombieGroups.get(i).size() > 0) {
                return false;
            }
        }
        return true;
    }
    /// 检查失败条件
    public boolean checkLose() {
        for (int i = 0; i < this.map_y_len; ++i) {
            for (Sprite sprite :this.zombieGroups.get(i).list) {
                Zombie zombie = (Zombie)sprite;
                if (zombie.rect.right() < 0) {
                    return true;
                }
            }
        }
        return false;
    }
    /// 检查游戏阶段
    public void checkGameState() {
        if (this.checkVictory()) {
            int level = this.game_info.getInt(c.LEVEL_NUM);
            if (level >= 3){
                this.next = c.GAME_VICTORY;
                this.game_info.put(c.LEVEL_NUM, 0);
                this.next = c.GAME_VICTORY;
                this.done = true;
                return; 
            }
            this.game_info.put(c.LEVEL_NUM, level + 1);
            this.next = c.GAME_VICTORY;
            this.done = true;
        }
        else if (this.checkLose()) {
            this.next = c.GAME_LOSE;
            this.done = true;
        }
    }
    /// 创建鼠标拖拽对象
    public void drawMouseShow(Graphics g) {
        if (this.hintPlant) {
            hintImage.paintObject(g);;
        }
        int x = this.mouseX;
        int y = this.mouseY;
        this.mouseImage.rect.adjustcx(x);
        this.mouseImage.rect.adjustcy(y);
        mouseImage.paintObject(g);;
    }
    /// 创建僵尸冻结动画
    public void drawZombieFreezeTrap(Graphics g, int i) {
        for (Sprite sprite :this.zombieGroups.get(i).list) {
            Zombie zombie = (Zombie)sprite;
            zombie.drawFreezeTrap(g);
        }
    }
    /// 绘图过程
    public void draw(Graphics g) {
        // 绘制背景
        Rect level_rect = new Rect(background.rect.image, -viewportLeft, 0);
        level = new Sprite(level_rect);
        level.paintObject(g);

        if (this.state.equals(c.MAIN_MENU)) {
            this.start.paintObject(g);
        }
        else if (this.state.equals(c.CHOOSE)) {
            this.panel.paintObject(g);
        }
        else if (this.state.equals(c.PLAY)) {
            this.menubar.paintObject(g);
            for (int i = 0; i < this.map_y_len; ++i) {
                this.plantGroups.get(i).paintObject(g);
                this.zombieGroups.get(i).paintObject(g);
                this.hypnoZombieGroups.get(i).paintObject(g);
                this.bulletGroups.get(i).paintObject(g);
                this.drawZombieFreezeTrap(g, i);
            }
            for (Sprite sprite: this.cars) {
                Car car = (Car) sprite;
                car.paintObject(g);
            }
            for (Sprite sprite: this.sunGroup) {
                Sun sun = (Sun) sprite;
                if (sun.state != Constants.DIE) sun.paintObject(g);
            }
            this.headGroup.paintObject(g);
            // this.sunGroup.paintObject(g);

            if (this.dragPlant) {
                this.drawMouseShow(g);
            }
        }
    }
    public static void main() {
    	Level level = new Level();
    	level.loadMap();
    }
    /// 传入json文件名，传出String代表json文件内容
    public static JSONObject loadJsonFile(String name) {
        File file = new File(name);
        try {
            String content = FileUtils.readFileToString(file, "UTF-8");
            JSONObject jsonObject = new JSONObject(content);
            return jsonObject;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }
   
}