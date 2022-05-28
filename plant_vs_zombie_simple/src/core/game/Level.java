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
    public int mapY;
    public ZombieListItem(int time_, String name_, int mapY_) {
        time = time_;
        name = name_;
        mapY = mapY_;
    }
}

interface PaintItf {
    public void paintObject(Graphics g);
}

class Surface extends JPanel {
    LinkedList<PaintItf> list;
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for(PaintItf item: list) {
            item.paintObject(g);
        }
    }
}

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
    public void paintObject(Graphics g) {
        g.drawImage(image, x, y, null);
    }
    public Rect getRect() {
        return new Rect(image.getWidth(), image.getHeight(), x, y);
    }
}

/// 接口类，指示传入判断碰撞的函数类型
interface CollidedFunc {
    /// param指示碰撞区域缩放的比例
    /// return true表示发生碰撞
    public boolean collid(Sprite x, Sprite y, double param);
}

/// 圆形碰撞发生函数
class CircleCollidedFunc
    implements CollidedFunc
{
    public boolean collid(Sprite x, Sprite y, double param) {
        return (x.radius + y.radius) * (x.radius + y.radius) > 
        (x.rect.centerx() - y.rect.centerx()) * ((x.rect.centerx() - y.rect.centerx()))
        + (x.rect.centery() - y.rect.centery()) * (x.rect.centery() - y.rect.centery());
    }
}
/// 矩形碰撞发生函数
class RectCollidedFunc
    implements CollidedFunc
{
    public boolean collid(Sprite x, Sprite y, double param) {
        return x.rect.intersect(y.rect);
    }
}

abstract class Sprite {
    //矩形碰撞体积
    public Rect rect;
    /// a pointer to delete elements in group when kill is ready
    public LinkedList<Group> ptr;
    /// 可选参数以表明原型的碰撞体积
    public double radius;
    public Sprite(Rect rect) {
        this.rect = rect;
        ptr = new LinkedList<>();
    }
    /// 检查一个精灵是否和一个组中的元素相交，若相交，返回检查到的第一个
    /// param 默认情况可以为1.0,表示相交区域的缩放程度
    public Sprite spritecollideany(Group group, CollidedFunc collidedFunc, double param) {
        for(Sprite s: group.list) {
            if (collidedFunc.collid(this, s, param)) return s;
        }
        // 找不到
        return null;
    }
    public void update(ArrayList<Object> args) {}
    public void added(Group g) {
        ptr.add(g);
    }
    public void kill() {
        for(Group g: ptr) {
            g.remove(this);
        }
    }
}

class Group {
    public LinkedList<Sprite> list;
    public Group() {
        list = new LinkedList<>();
    }
    public void update(ArrayList<Object> args) {
        for (Sprite g: list) {
            g.update(args);
        }
    }
    public void add(Sprite item) {
        list.add(item);
        item.added(this);
    }
    public void remove(Sprite item) {
        list.remove(item);
    }
}

class Rect {
    public int width;
    public int height;
    public int left;
    public int top;
    public Rect(int width_, int height_) {
        width = width_;
        height = height_;
        /// default;
        left = 0;
        top = 0;
    }
    public Rect(int width_, int height_, int left_, int top_) {
        width = width_;
        height = height_;
        left = left_;
        top = top_;
    }
    public int bottom() {
        return top + height;
    }
    public int centerx() {
        return (left + width) / 2;
    }
    public int centery() {
        return (top + height) / 2;
    }
    /// assume width and height right, adjust left and top
    public void adjust(int centerx, int bottom) {
        left = centerx * 2 - width;
        top = bottom - height;
    }
    public boolean intersect(Rect y) {
        int xa,ya,wxa,wya;
        int xb,yb,wxb,wyb;
        if (width > height) {
            xa = width;
            ya = height;
            wxa = centerx();
            wya = centery();
        } else {
            xa = height;
            ya = width;
            wxa = centery();
            wya = centerx();
        }
        if (y.width > y.height) {
            xb = y.width;
            xb = y.height;
            wxb = y.centerx();
            wyb = y.centery();
        } else {
            xb = y.height;
            yb = y.width;
            wxb = y.centery();
            wyb = y.centerx();
        }
        return Math.abs(wxa - wxb) < (xa + xb) / 2 &&
            Math.abs(wya - wyb) < (ya + yb) / 2;
    }
}

public class Level extends State {
    ///temporary;
    int mouseX,mouseY;
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
        window = new JFrame()；
        window.add(surface);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(816, 638);

        gameInfo = persist;
        this.persist = persist;
        gameInfo.remove(c.CURRENT_TIME);
        gameInfo.put(c.CURRENT_TIME, currentTime);
        mapYLen = c.GRID_Y_LEN;
        map = new GameMap(c.GRID_X_LEN, mapYLen);
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
        bgRect = new Rect(background.image.getWidth(), background.image.getHeight());
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
        for (int i = 0; i < mapYLen; ++i) {
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
        for (int i = 0; i < mapYLen; ++i) {
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
                createZombie(data.name, data.mapY);
                zombieList.remove(data);
            }
        }
        ArrayList<Object> list = new ArrayList<>();
        list.add(gameInfo);
        for (int i = 0; i < mapYLen; ++i) {
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
    public void createZombie(String name, int mapY) {
        ArrayList<Integer> Pos = map.getMapGridPos(0, mapY);
        int x = Pos.get(0);
        int y = Pos.get(1);
        if (name == c.NORMAL_ZOMBIE) {
            zombieGroups.get(mapY).add(new NormalZombie(c.ZOMBIE_START_X, y, headGroup));
        }
        else if (name == c.CONEHEAD_ZOMBIE) {
            zombieGroups.get(mapY).add(new ConeHeadZombie(c.ZOMBIE_START_X, y, headGroup));
        }
        else if (name == c.BUCKETHEAD_ZOMBIE) {
            zombieGroups.get(mapY).add(new BucketHeadZombie(c.ZOMBIE_START_X, y, headGroup));
        }
        else if (name == c.FLAG_ZOMBIE) {
            zombieGroups.get(mapY).add(new FlagZombie(c.ZOMBIE_START_X, y, headGroup));
        }
        else if (name == c.NEWSPAPER_ZOMBIE) {
            zombieGroups.get(mapY).add(new NewspaperZombie(c.ZOMBIE_START_X, y, headGroup));
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
            if (hintImage != null && pos.get(0) == hintRect.width &&
                pos.get(1) == hintRect.height) {
                return;
            }
            int width = mouseRect.width;
            int height = mouseRect.height;
            //画图并保存属性0);
            surface.add(mouseImage);
            hintImage = new PaintItem(0, 0);
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
        mouseRect = new Rect(width,height,x,y);
//        pg.mouse.set_visible(False)
        dragPlant = true;
        this.plantName = plantName;
        this.selectPlant = selectPlant;
    }
    public void removeMouseImage() {
//        pg.mouse.set_visible(True)
        dragPlant = false;
        mouseImage = null;
        hintImage = null;
        hintPlant = false;
    }
    public void checkBulletCollisions() {
        // 0.7倍
        CollidedFunc collidedFunc = new CircleCollidedFunc();
        for (int i = 0; i < mapYLen; ++i) {
            for (Sprite bullet : bulletGroups.get(i).list)
                if ( ((Bullet)bullet).state == c.FLY ) {
                    // 检测碰撞到的僵尸
                    Zombie zombie = (Zombie)bullet.spritecollideany(zombieGroups.get(i).list, collidedFunc, 0.7)
                    if (zombie != null && zombie.state != c.DIE) {
                        zombie.setDamage(bullet.damage, bullet.ice);
                        bullet.setExplode();
                    }
                }
        }
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