package core.game;
import core.json.FileUtils;
import core.json.JSONObject;
import core.plants.Plant;
import core.zombies.Zombie;
import core.json.JSONArray;
import core.State;
import core.Tool;
import core.bullets.Bullet;
import core.Constants;
import core.component.MenuBar;
import core.component.Panel;
import core.game.*;

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

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

class c extends Constants {}

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

interface PaintItf {
    public void paintObject(Graphics g);
}

/// 画图类，简单的图片加左上角绘图的对象可以直接使用这个类
class PaintItem implements PaintItf{
    /// left
    int x;
    /// top
    int y;
    BufferedImage image;
    public PaintItem(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }
    public void adjust(Rect rect) {
        this.x = rect.left;
        this.y = rect.top;
    }
    public void paintObject(Graphics g) {
        g.drawImage(image, x, y, null);
    }
    public Rect getRect() {
        return new Rect(image, x, y);
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
        xr = x.radius * ratio;
        yr = y.radius * ratio;
        return (xr + yr) * (xr + yr) > 
        (x.rect.centerx() - y.rect.centerx()) * ((x.rect.centerx() - y.rect.centerx()))
        + (x.rect.centery() - y.rect.centery()) * (x.rect.centery() - y.rect.centery());
    }
}
/// 矩形碰撞发生函数
class RectCollidedFunc extends CollidedFunc
{
    public RectCollidedFunc(double ratio) {
        super(ratio);
    }
    public boolean collid(Sprite x, Sprite y) {
        return x.rect.intersect(y.rect, ratio);
    }
}

public class Level extends State {
    ///temporary;
    int mouseX,mouseY;
    JSONObject gameInfo;
    JSONObject persist;
    int map_y_len;
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
    JFrame window;
    /// elements added into surface
    Surface surface;

    /// work in map directory
    private Path rootDir = Paths.get("resources/data/map");
    
    public Level() {
        super();
    }
    /// 初始化
    public void startUp(double currentTime, JSONObject persist) {
        //activate window
        window = new JFrame();
        window.add(surface);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(816, 638);

        gameInfo = persist;
        this.persist = persist;
        gameInfo.remove(c.CURRENT_TIME);
        gameInfo.put(c.CURRENT_TIME, currentTime);
        map_y_len = c.GRID_Y_LEN;
        map = new GameMap(c.GRID_X_LEN, map_y_len);
        mapData = new JSONObject();
        loadMap();
        setupBackgroud();
        initState();

        window.setVisible(true);
        window.repaint();
    }
    /// 读入map文件信息
    public void loadMap() {
        String filePath = "level" + (int)gameInfo.get(c.LEVEL_NUM) + ".json";
        mapData = loadJsonFile(filePath);
    }
    Rect bgRect;
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
        bgRect = new Rect(background.image);
        /*
        level = pg.Surface((bg_rect.w, bg_rect.h)).convert()
        viewport = tool.SCREEN.get_rect(bottom=bg_rect.bottom)
        viewport.x += c.BACKGROUND_OFFSET_X*/
    }
    Group sunGroup;
    Group headGroup;
    ArrayList<Group> plantGroups;
    ArrayList<Group> zombieGroups;
    ArrayList<Group> hypnoZombieGroups;
    ArrayList<Group> bulletGroups;
    public void setupGroups() {
        sunGroup = new Group();
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
    ArrayList<Car> cars;
    public void setupCars() {
        cars = new ArrayList<>();
        for (int i = 0; i < map_y_len; ++i) {
            ArrayList<Integer> pos = map.getMapGridPos(0, i);
            int y = pos.get(1);
            cars.add(new Car(-25, y+20, i));
        }
    }
    public void update(int time,ArrayList<Integer> mousePos, ArrayList<Integer> mouseClick) {
        currentTime = time;
        gameInfo.put(c.CURRENT_TIME, time);
        if (state == c.CHOOSE) {
            choose(mousePos, mouseClick);
        } else if(state == c.PLAY) {
            play(mousePos, mouseClick);
        }

        window.repaint();
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
        hintImage = null;
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
                createZombie(data.name, data.map_y);
                zombieList.remove(data);
            }
        }
        ArrayList<Object> list = new ArrayList<>();
        list.add(gameInfo);
        for (int i = 0; i < map_y_len; ++i) {
            bulletGroups.get(i).update(list);
            plantGroups.get(i).update(list);
            zombieGroups.get(i).update(list);
            hypnoZombieGroups.get(i).update(list);
            //迷惑的功能，暂时还没什么用
            /*
            for (zombie in hypno_zombie_groups[i]) {
                if zombie.rect.x > c.SCREEN_WIDTH:
                    zombie.kill()
            }*/
        }
        headGroup.update(list);
        sunGroup.update(list);
        
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
                sunGroup.add(new Sun(mapPos.get(0), 0, mapPos.get(0), mapPos.get(1)));
            }
        }
        if (!dragPlant && !mousePos.isEmpty() && mouseClick.size() > 0) {
            //检查碰撞
            /*for sun in sun_group:
                if sun.checkCollision(mouse_pos[0], mouse_pos[1]):
                    menubar.increaseSunValue(sun.sun_value)*/
        }
        for (Car car:cars):
            car.update(list);

        //危险
        menubar.update((int)currentTime);

        checkBulletCollisions();
        checkZombieCollisions();
        checkPlants();
        checkCarCollisions();
        checkGameState();
    }
    public void createZombie(String name, int map_y) {
        ArrayList<Integer> Pos = map.getMapGridPos(0, map_y);
        int x = Pos.get(0);
        int y = Pos.get(1);
        if (name == c.NORMAL_ZOMBIE) {
            zombieGroups.get(map_y).add(new NormalZombie(c.ZOMBIE_START_X, y, headGroup));
        }
        else if (name == c.CONEHEAD_ZOMBIE) {
            zombieGroups.get(map_y).add(new ConeHeadZombie(c.ZOMBIE_START_X, y, headGroup));
        }
        else if (name == c.BUCKETHEAD_ZOMBIE) {
            zombieGroups.get(map_y).add(new BucketHeadZombie(c.ZOMBIE_START_X, y, headGroup));
        }
        else if (name == c.FLAG_ZOMBIE) {
            zombieGroups.get(map_y).add(new FlagZombie(c.ZOMBIE_START_X, y, headGroup));
        }
        else if (name == c.NEWSPAPER_ZOMBIE) {
            zombieGroups.get(map_y).add(new NewspaperZombie(c.ZOMBIE_START_X, y, headGroup));
        }
    }
    PaintItem hintImage;
    Rect hintRect;
    Plant newPlant;
    public ArrayList<Integer> canSeedPlant() {
        return map.showPlant(mouseX, mouseY);
    }
    public void addPlant() {
        ArrayList<Integer> pos = canSeedPlant();
        if (pos.isEmpty()) {
            return;
        }
        if (hintImage == null) {
            setupHintImage();
        }
        int x = hintRect.centerx();
        int y = hintRect.bottom();
        ArrayList<Integer> mapIndex = map.getMapIndex(x, y);
        int map_x = mapIndex.get(0);
        int map_y = mapIndex.get(1);
        if (plantName == c.SUNFLOWER) {
            newPlant = new SunFlower(x, y, sun_group);
        }
        else if (plantName == c.PEASHOOTER) {
            newPlant = new PeaShooter(x, y, bullet_groups[map_y]);
        }
        else if (plantName == c.SNOWPEASHOOTER) {
            newPlant = new SnowPeaShooter(x, y, bullet_groups[map_y]);
        }
        else if (plantName == c.WALLNUT) {
            newPlant = new WallNut(x, y);
        }
        else if (plantName == c.CHERRYBOMB) {
            newPlant = new CherryBomb(x, y);
        }
        else if (plantName == c.THREEPEASHOOTER) {
            newPlant = new ThreePeaShooter(x, y, bullet_groups, map_y);
        }
        else if (plantName == c.REPEATERPEA) {
            newPlant = new RepeaterPea(x, y, bullet_groups[map_y]);
        }
        else if (plantName == c.CHOMPER) {
            newPlant = new Chomper(x, y);
        }
        else if (plantName == c.PUFFSHROOM) {
            newPlant = new PuffShroom(x, y, bullet_groups[map_y]);
        }
        else if (plantName == c.POTATOMINE) {
            newPlant = new PotatoMine(x, y);
        }
        else if (plantName == c.SQUASH) {
            newPlant = new Squash(x, y);
        }
        else if (plantName == c.SPIKEWEED) {
            newPlant = new Spikeweed(x, y);
        }
        else if (plantName == c.JALAPENO) {
            newPlant = new Jalapeno(x, y);
        }
        else if (plantName == c.SCAREDYSHROOM) {
            newPlant = new ScaredyShroom(x, y, bullet_groups[map_y]);
        }
        else if (plantName == c.SUNSHROOM) {
            newPlant = new SunShroom(x, y, sun_group);
        }
        else if (plantName == c.ICESHROOM) {
            newPlant = new IceShroom(x, y);
        }
        else if (plantName == c.HYPNOSHROOM) {
            newPlant = new HypnoShroom(x, y);
        }
        else if (plantName == c.WALLNUTBOWLING) {
            newPlant = new WallNutBowling(x, y, map_y, self);
        }
        else if (plantName == c.REDWALLNUTBOWLING) {
            newPlant = new RedWallNutBowling(x, y);
        }

        if (newPlant.canSleep && backgroundType == c.BACKGROUND_DAY) {
            newPlant.setSleep();
        }
        plantGroups.get(map_y).add(newPlant);
        if (barType == c.CHOOSEBAR_STATIC) {
            menubar.decreaseSunValue(selectPlant.sun_cost);
            menubar.setCardFrozenTime(plantName);
        }
        else {
            menubar.deleateCard(selectPlant);
        }

        if (barType != c.CHOSSEBAR_BOWLING) {
            map.setMapGridType(map_x, map_y, c.MAP_EXIST);
        }
        removeMouseImage();

    }
    public void setupHintImage() {
        ArrayList<Integer> pos = canSeedPlant();
        if (!pos.isEmpty() && mouseImage != null) {
            if (hintImage != null && pos.get(0) == hintRect.width() &&
                pos.get(1) == hintRect.height()) {
                return;
            }
            int width = mouseRect.width();
            int height = mouseRect.height();
            //画图并保存属性;
            surface.add(hintImage);
            hintImage = new PaintItem(0, 0, mouseImage.image);
//            Tool.setColorkey(c.BLACK)
            Tool.adjustAlpha(hintImage.image, new Color(128));
            hintRect = hintImage.getRect();
            hintRect.adjust(pos.get(0), pos.get(1));
            hintPlant = true;
        }
        else{
            hintPlant = false;
        }
    }
    PaintItem mouseImage;
    Rect mouseRect;
    String plantName;
    Card selectPlant;
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

        if (plantName == c.POTATOMINE || plantName == c.SQUASH ||
            plantName == c.SPIKEWEED || plantName == c.JALAPENO ||
            plantName == c.SCAREDYSHROOM || plantName == c.SUNSHROOM ||
            plantName == c.ICESHROOM || plantName == c.HYPNOSHROOM ||
            plantName == c.WALLNUTBOWLING || plantName == c.REDWALLNUTBOWLING) {
            color = c.WHITE;
        }
        else {
            color = c.BLACK;
        }
        //要把图片贴上去，这里主要是实现绘图
//        mouseImage = tool.get_image(frame_list[0], x, y, width, height, color, 1)
        //暂时使用一个替代的功能;     
        mouseImage = new PaintItem(x, y, frameList.first().image);
        mouseRect = new Rect(mouseImage.image, x, y);
//        pg.mouse.set_visible(false)
        dragPlant = true;
        this.plantName = plantName;
        this.selectPlant = selectPlant;
    }
    public void removeMouseImage() {
//        pg.mouse.set_visible(true)
        dragPlant = false;
        mouseImage = null;
        hintImage = null;
        hintPlant = false;
    }
    public void checkBulletCollisions() {
        // 0.7倍
        CollidedFunc collidedFunc = new CircleCollidedFunc(0.7);
        for (int i = 0; i < map_y_len; ++i) {
            for (Sprite bullet : bulletGroups.get(i).list)
                if ( ((Bullet)bullet).state == c.FLY ) {
                    // 检测碰撞到的僵尸
                    Zombie zombie = (Zombie)bullet.spritecollideany(zombieGroups.get(i), collidedFunc);
                    if (zombie != null && zombie.state != c.DIE) {
                        zombie.setDamage(bullet.damage, bullet.ice);
                        bullet.setExplode();
                    }
                }
        }
    }
    public void checkZombieCollisions() {
        double ratio;
        if (this.barType == c.CHOSSEBAR_BOWLING) {
            ratio = 0.6;
        }
        else {
            ratio = 0.7;
        }
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
                    if (plant.name == c.WALLNUTBOWLING) {
                        if (plant.canHit(i)) {
                            zombie.setDamage(c.WALLNUT_BOWLING_DAMAGE);
                            plant.changeDirection(i);
                        }
                    }
                    else if (plant.name == c.REDWALLNUTBOWLING) {
                        if (plant.state == c.IDLE) {
                            plant.setAttack();
                        }
                    }
                    else if (plant.name != c.SPIKEWEED) {
                        zombie.setAttack(plant);
                    }
                }
            }
/*
            for hypno_zombie in this.hypno_zombie_groups[i]:
                if hypno_zombie.health <= 0:
                    continue
                zombieList = pg.sprite.spritecollide(hypno_zombie,
                               this.zombie_groups[i], false,collided_func)
                for (Zombie zombie : zombieList) {
                    if (zombie.state == c.DIE) {
                        continue;
                    }
                    if (zombie.state == c.WALK) {
                        zombie.setAttack(hypno_zombie, false);
                    }
                    if (hypno_zombie.state == c.WALK) {
                        hypno_zombie.setAttack(zombie, false);
                    }
                }
            }*/
        }
    }
    public void checkCarCollisions() {

    }
    public void boomZombies() {

    }
    public void freezeZombies() {

    }
    public void killPlant(Plant plant) {
        ArrayList<Integer> pos = plant.getPosition();
        int x = pos.get(0);
        int y = pos.get(1);
        ArrayList<Integer> mapPos = this.map.getMapIndex(x, y);
        int map_x = mapPos.get(0);
        int map_y = mapPos.get(1);
        if (this.barType != c.CHOSSEBAR_BOWLING) {
            this.map.setMapGridType(map_x, map_y, c.MAP_EMPTY);
        }
        if (plant.name == c.CHERRYBOMB || plant.name == c.JALAPENO ||
            (plant.name == c.POTATOMINE && ! plant.is_init) ||
            plant.name == c.REDWALLNUTBOWLING) {
            this.boomZombies(plant.rect.centerx, map_y, plant.explode_y_range,
                            plant.explode_x_range);
        }
        else if (plant.name == c.ICESHROOM && plant.state != c.SLEEP) {
            this.freezeZombies(plant);
        }
        /*
        else if (plant.name == c.HYPNOSHROOM && plant.state != c.SLEEP) {
            zombie = plant.kill_zombie
            zombie.setHypno()
            _, map_y = this.map.getMapIndex(zombie.rect.centerx, zombie.rect.bottom)
            this.zombie_groups[map_y].remove(zombie)
            this.hypno_zombie_groups[map_y].add(zombie)
        }*/
        plant.kill();
    }
    /// index i;
    public void checkPlant(Plant plant, int i) {
        boolean canAttack;
        boolean needCry;
        int zombieLen = this.zombieGroups.get(i).list.size();
        if (plant.name == c.THREEPEASHOOTER) {
            if (plant.state == c.IDLE) {
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
            else if (plant.state == c.ATTACK) {
                if (zombieLen > 0) {
                    // do nothing
                }
                else if ((i-1) >= 0 && this.zombieGroups.get(i-1).size()) > 0) {
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
        else if (plant.name == c.CHOMPER) {
            for (Sprite sprite : this.zombieGroups.get(i).list) {
                Zombie zombie = (Zombie)sprite;
                if (plant.canAttack(zombie)) {
                    plant.setAttack(zombie, this.zombieGroups.get(i));
                    break;
                }
            }
        else if (plant.name == c.POTATOMINE) {
            for (Sprite sprite : this.zombieGroups.get(i).list) {
                Zombie zombie = (Zombie)sprite;
                if plant.canAttack(zombie) {
                    plant.setAttack();
                    break;
                }
            }
        }
        else if (plant.name == c.SQUASH) {
            for (Sprite sprite : this.zombieGroups.get(i).list) {
                Zombie zombie = (Zombie)sprite;
                if plant.canAttack(zombie) {
                    plant.setAttack(zombie, this.zombieGroups[i]);
                    break;
                }
            }
        }
        else if (plant.name == c.SPIKEWEED) {
            canAttack = false;
            for (Sprite sprite : this.zombieGroups.get(i).list) {
                Zombie zombie = (Zombie)sprite;
                if plant.canAttack(zombie) {
                    can_attack = true
                    break;
                }
            }
            if (plant.state == c.IDLE && canAttack) {
                plant.setAttack(this.zombie_groups[i]);
            }
            else if (plant.state == c.ATTACK && !can_attack) {
                plant.setIdle();
            }
        }
        else if (plant.name == c.SCAREDYSHROOM) {
            needCry = false;
            canAttack = false;
            for (Sprite sprite : this.zombieGroups.get(i).list) {
                Zombie zombie = (Zombie)sprite;
                if (plant.needCry(zombie)) {
                    need_cry = true;
                    break;
                }
                else if (plant.canAttack(zombie)) {
                    can_attack = true;
                }
            }
            if (need_cry) {
                if (plant.state != c.CRY) {
                    plant.setCry();
                }
            }
            else if (can_attack) {
                if (plant.state != c.ATTACK) {
                    plant.setAttack();
                }
            }
            else if (plant.state != c.IDLE) {
                plant.setIdle();
            }
        }
        else if(plant.name == c.WALLNUTBOWLING ||
             plant.name == c.REDWALLNUTBOWLING) {
            //do nothing
        }
        else {
            canAttack = false;
            if (plant.state == c.IDLE && zombieLen > 0):
                for (Sprite sprite : this.zombieGroups.get(i).list) {
                    Zombie zombie = (Zombie)sprite;
                    if (plant.canAttack(zombie)) {
                        can_attack = true;
                        break;
                    }
                }
            if (plant.state == c.IDLE && canAttack) {
                plant.setAttack();
            }
            else if (plant.state == c.ATTACK && ! canAttack) {
                plant.setIdle();
            }
        }
    }
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
    public boolean checkLose() {
        for (int i = 0; i < this.map_y_len; ++i) {
            for (Sprite sprite :this.zombieGroups.get(i).list) {
                Zombie zombie = (Zombie)sprite;
                if (zombie.rect.right < 0) {
                    return true;
                }
            }
        }
        return false;
    }
    String next;
    boolean done;
    public void checkGameState() {
        if (this.checkVictory()) {
            int level = this.gameInfo.getInt(c.LEVEL_NUM);
            this.gameInfo.put(c.LEVEL_NUM, level + 1);
            this.next = c.GAME_VICTORY;
            this.done = true;
        }
        else if (this.checkLose()) {
            this.next = c.GAME_LOSE;
            this.done = true;
        }
    }
    public void drawMouseShow() {
        if (this.hintPlant) {
            this.hintImage.adjust(hintRect);
            surface.add(hintImage);
        }
        int x = this.mouseX;
        int y = this.mouseY;
        this.mouseRect.adjustxy(x, y);
        this.mouseImage.adjust(mouseRect);
        surface.add(mouseImage);
    }
    public void drawZombieFreezeTrap(int i) {
        for (Sprite sprite :this.zombieGroups.get(i).list) {
            Zombie zombie = (Zombie)sprite;
//            zombie.drawFreezeTrap(surface);
        }
    }
    public void draw() {
        /*
        this.level.blit(this.background, this.viewport, this.viewport)
        surface.blit(this.level, (0,0), this.viewport)
        if this.state == c.CHOOSE:
            this.panel.draw(surface)
        elif this.state == c.PLAY:
            this.menubar.draw(surface)
            for i in range(this.map_y_len):
                this.plant_groups[i].draw(surface)
                this.zombie_groups[i].draw(surface)
                this.hypno_zombie_groups[i].draw(surface)
                this.bullet_groups[i].draw(surface)
                this.drawZombieFreezeTrap(i, surface)
            for car in this.cars:
                car.draw(surface)
            this.head_group.draw(surface)
            this.sun_group.draw(surface)

            if this.drag_plant:
                this.drawMouseShow(surface)
                */
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