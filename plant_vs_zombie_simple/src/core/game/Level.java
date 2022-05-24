package core.game;
import core.json.FileUtils;
import core.json.JSONObject;
import core.json.JSONArray;
import core.State;
import core.Tool;
import core.Constants;
import core.component.MenuBar;
import core.component.Panel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import core.component.Card;
import core.component.GameMap;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

class c extends Constants {}

class ZombieListItem {
    int time;
    String name;
    int mapY;
    public ZombieListItem(int time_, String name_, int mapY_) {
        time = time_;
        name = name_;
        mapY = mapY_;
    }
}

public class Level extends State {
    JSONObject gameInfo;
    JSONObject persist;
    int mapYLen;
    int backgroundType;
    Tool.Img background;
    GameMap map;
    JSONObject mapData;
    double zombieStartTime;
    ArrayList<ZombieListItem> zombieList;
    String state;
    int barType;
    ArrayList<Card> cardPool;
    Panel panel;
    MenuBar menubar;

    /// work in map directory
    private Path rootDir = Paths.get("resources/data/map");
    
    public Level() {
        super();
    }
    /// 初始化
    public void startUp(double currentTime, JSONObject persist) {
        gameInfo = persist;
        this.persist = persist;
        gameInfo.remove(c.CURRENT_TIME);
        gameInfo.put(c.CURRENT_TIME, currentTime);
        mapYLen = c.GRID_Y_LEN;
        map = new GameMap(c.GRID_X_LEN, mapYLen);
        mapData = new JSONObject();
        loadMap();
//        setupBackgroud();
//        initState();
    }
    /// 读入map文件信息
    public void loadMap() {
        String filePath = "level" + (int)gameInfo.get(c.LEVEL_NUM) + ".json";
        mapData = loadJsonFile(filePath);
    }
    public void setupBackgroud() {
    	int imgIndex = (int)mapData.get(c.BACKGROUND_TYPE);
    	backgroundType = imgIndex;
        TreeSet<Tool.Img> imgSet = Tool.GFX.get(c.BACKGROUND_NAME);
        int i = 0;
        for(Tool.Img img: imgSet) {
            ++i;
            if(i == imgIndex) {
                background = img;
                break;
            }
        }
        /*
        level = pg.Surface((bg_rect.w, bg_rect.h)).convert()
        viewport = tool.SCREEN.get_rect(bottom=bg_rect.bottom)
        viewport.x += c.BACKGROUND_OFFSET_X*/
    }
    public void setupGroups() {
        
    }
    public void setupZombies() {
        JSONArray dataArray = mapData.getJSONArray(c.ZOMBIE_LIST);
        ArrayList<ZombieListItem> zombieList = new ArrayList<>();
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
    public void setupCars() {

    }
    public void update(double time,ArrayList<Integer> mousePos, ArrayList<Integer> mouseClick) {
        currentTime = time;
        gameInfo.put(c.CURRENT_TIME, time);
        if (state == c.CHOOSE) {
            choose(mousePos, mouseClick);
        } else if(state == c.PLAY) {
            play(mousePos, mouseClick);
        }

        //draw(surface);
    }
    public void initBowlingMap() {
        for (int x = 3; x < map.width; ++x)
        for (int y = 0; y < map.height ; ++y) {
            map.setMapGridType(x, y, c.MAP_EXIST);
        }
    }
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
            if (barType == c.CHOSSEBAR_BOWLING) {
                initBowlingMap();
            }
        }
    }
    public void initChoose() {
        state = c.CHOOSE;
        panel = new Panel(c.all_card_list, mapData.optInt(c.INIT_SUN_NAME, 50));
    }
    public void choose(ArrayList<Integer> mousePos, ArrayList<Integer> mouseClick) {
        if (!mousePos.isEmpty() && mouseClick.size() > 0) {
            panel.checkCardClick(mousePos.get(0), mousePos.get(1));
            if (panel.checkStartButtonClick(mousePos.get(0), mousePos.get(1))) {
                initPlay(panel.getSelectedCards());
            }
        }
    }
    public boolean dragPlant;
    public boolean hintPlant;
    public boolean produceSun;
    public double sunTimer;
    public void initPlay(int[] cardList) {
        state = c.PLAY;
        if (barType == c.CHOOSEBAR_STATIC) {
            menubar = new MenuBar(cardList, mapData.optInt(c.INIT_SUN_NAME, 50));
        } else {
//            menubar = new MoveBar(cardList);
        }
        dragPlant = false;
//        hintImage = None;
        hintPlant = false;
        if (backgroundType == c.BACKGROUND_DAY && barType == c.CHOOSEBAR_STATIC) {
            produceSun = true;
        }
        else {
            produceSun = false;
        }
        sunTimer = currentTime;

        removeMouseImage();
        setupGroups();
        setupZombies();
        setupCars();
    }
    
    public void play(ArrayList<Integer> mousePos, ArrayList<Integer> mouseClick) {
        if (zombieStartTime == 0.0) {
            zombieStartTime = currentTime;
        }
        else if(zombieList.size() > 0) {
            ZombieListItem data = zombieList.get(0);
            if (data.time <= (currentTime - zombieStartTime)) {
                createZombie(data.name, data.mapY);
                zombieList.remove(data);
            }
        }
        /*
        for (int i = 0; i < mapYLen; ++i) {
            bullet_groups[i].update(game_info);
            plant_groups[i].update(game_info);
            zombie_groups[i].update(game_info);
            hypno_zombie_groups[i].update(game_info);
            for zombie in hypno_zombie_groups[i] {
                if zombie.rect.x > c.SCREEN_WIDTH:
                    zombie.kill()
            }
        }
        head_group.update(game_info)
        sun_group.update(game_info)*/
        
        if (!dragPlant && !mousePos.isEmpty() && mouseClick.size() > 0) {
            Card result = menubar.checkCardClick(mousePos.get(0),mousePos.get(1));
            if (result != null) {
                setupMouseImage(c.plant_name_list[result.name_index], result);
            }
        }
        else if (dragPlant) {
            if (mouseClick.size() > 1) {
                removeMouseImage();
            }
            else if (mouseClick.size() > 0) {
                if (menubar.checkMenuBarClick(mousePos.get(0), mousePos.get(1))) {
                    removeMouseImage();
                }
                else {
                    addPlant();
                }
            }
            else if (mousePos.isEmpty()) {
                setupHintImage();
            }
        }
        if (produceSun) {
            if ((currentTime - sunTimer) > c.PRODUCE_SUN_INTERVAL) {
                sunTimer = currentTime;
                ArrayList<Integer> mapRandom = map.getRandomMapIndex();
                ArrayList<Integer> mapPos = map
                    .getMapGridPos(mapRandom.get(0), mapRandom.get(1));
                //sunGroup.add(plant.Sun(mapPos.get(0), 0, mapPos.get(0), mapPos.get(1)));
            }
        }
        if (! dragPlant && !mousePos.isEmpty() && mouseClick.size() > 0) {
            /*for sun in sun_group:
                if sun.checkCollision(mouse_pos[0], mouse_pos[1]):
                    menubar.increaseSunValue(sun.sun_value)*/
        }
/*
        for car in cars:
            car.update(game_info)*/

        //危险
        menubar.update((int)currentTime);

        checkBulletCollisions();
        checkZombieCollisions();
        checkPlants();
        checkCarCollisions();
        checkGameState();
    }
    public void createZombie(String name, int mapY) {
        /*
        ArrayList<Integer> mapPos = map.getMapGridPos(0, map_y);
        int x = mapPos.get(0);
        int y = mapPos.get(1);
        if (name == c.NORMAL_ZOMBIE) {
            zombie_groups[map_y].add(zombie.NormalZombie(c.ZOMBIE_START_X, y, head_group));
        }
        else if (name == c.CONEHEAD_ZOMBIE) {
            zombie_groups[map_y].add(zombie.ConeHeadZombie(c.ZOMBIE_START_X, y, head_group));
        }
        else if (name == c.BUCKETHEAD_ZOMBIE) {
            zombie_groups[map_y].add(zombie.BucketHeadZombie(c.ZOMBIE_START_X, y, head_group));
        }
        else if (name == c.FLAG_ZOMBIE) {
            zombie_groups[map_y].add(zombie.FlagZombie(c.ZOMBIE_START_X, y, head_group));
        }
        else if (name == c.NEWSPAPER_ZOMBIE) {
            zombie_groups[map_y].add(zombie.NewspaperZombie(c.ZOMBIE_START_X, y, head_group));
        }*/
    }
    public ArrayList<Integer> canSeedPlant() {
        /* 
        x, y = pg.mouse.get_pos()
        return map.showPlant(x, y)
        */
    }
    public void addPlant() {
        ArrayList<Integer> pos = canSeedPlant();
        if (pos.isEmpty()) {
            return;
        }
        if (hintImage == null) {
            setupHintImage();
        }
        x, y = hint_rect.centerx, hint_rect.bottom
        map_x, map_y = map.getMapIndex(x, y)
        if plant_name == c.SUNFLOWER:
            new_plant = plant.SunFlower(x, y, sun_group)
        elif plant_name == c.PEASHOOTER:
            new_plant = plant.PeaShooter(x, y, bullet_groups[map_y])
        elif plant_name == c.SNOWPEASHOOTER:
            new_plant = plant.SnowPeaShooter(x, y, bullet_groups[map_y])
        elif plant_name == c.WALLNUT:
            new_plant = plant.WallNut(x, y)
        elif plant_name == c.CHERRYBOMB:
            new_plant = plant.CherryBomb(x, y)
        elif plant_name == c.THREEPEASHOOTER:
            new_plant = plant.ThreePeaShooter(x, y, bullet_groups, map_y)
        elif plant_name == c.REPEATERPEA:
            new_plant = plant.RepeaterPea(x, y, bullet_groups[map_y])
        elif plant_name == c.CHOMPER:
            new_plant = plant.Chomper(x, y)
        elif plant_name == c.PUFFSHROOM:
            new_plant = plant.PuffShroom(x, y, bullet_groups[map_y])
        elif plant_name == c.POTATOMINE:
            new_plant = plant.PotatoMine(x, y)
        elif plant_name == c.SQUASH:
            new_plant = plant.Squash(x, y)
        elif plant_name == c.SPIKEWEED:
            new_plant = plant.Spikeweed(x, y)
        elif plant_name == c.JALAPENO:
            new_plant = plant.Jalapeno(x, y)
        elif plant_name == c.SCAREDYSHROOM:
            new_plant = plant.ScaredyShroom(x, y, bullet_groups[map_y])
        elif plant_name == c.SUNSHROOM:
            new_plant = plant.SunShroom(x, y, sun_group)
        elif plant_name == c.ICESHROOM:
            new_plant = plant.IceShroom(x, y)
        elif plant_name == c.HYPNOSHROOM:
            new_plant = plant.HypnoShroom(x, y)
        elif plant_name == c.WALLNUTBOWLING:
            new_plant = plant.WallNutBowling(x, y, map_y, self)
        elif plant_name == c.REDWALLNUTBOWLING:
            new_plant = plant.RedWallNutBowling(x, y)

        if new_plant.can_sleep and background_type == c.BACKGROUND_DAY:
            new_plant.setSleep()
        plant_groups[map_y].add(new_plant)
        if bar_type == c.CHOOSEBAR_STATIC:
            menubar.decreaseSunValue(select_plant.sun_cost)
            menubar.setCardFrozenTime(plant_name)
        else:
            menubar.deleateCard(select_plant)

        if bar_type != c.CHOSSEBAR_BOWLING:
            map.setMapGridType(map_x, map_y, c.MAP_EXIST)
        removeMouseImage()

    }
    public void setupHintImage() {

    }
    public void setupMouseImage(String plantName, Card card) {

    }
    public void removeMouseImage() {

    }
    public void checkBulletCollisions() {

    }
    public void checkZombieCollisions() {

    }
    public void checkCarCollisions() {

    }
    public void boomZombies() {

    }
    public void freezeZombies() {

    }
    public void killPlant() {

    }
    public void checkPlant() {

    }
    public void checkPlants() {

    }
    public void checkVictory() {

    }
    public void checkLose() {

    }
    public void checkGameState() {

    }
    public void drawMouseShow() {

    }
    public void drawZombieFreezeTrap() {

    }
    public void draw() {

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