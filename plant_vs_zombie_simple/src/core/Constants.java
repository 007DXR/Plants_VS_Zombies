package core;
import java.awt.*;


public class Constants {
    // 游戏开始状态
    public static int START_LEVEL_NUM = 1;
    public static String ORIGINAL_CAPTION = "Plant VS Zombies Game";

    // 颜色
    public static Color WHITE = new Color(255, 255, 255);
    public static Color NAVYBLUE = new Color( 60,  60, 100);
    public static Color SKY_BLUE = new Color( 39, 145, 251);
    public static Color BLACK = new Color(  0,   0,   0);
    public static Color LIGHTYELLOW = new Color(234, 233, 171);
    public static Color RED   = new Color(255,   0,   0);
    public static Color PURPLE = new Color(255,   0, 255);
    public static Color GOLD  = new Color(255, 215,   0);
    public static Color GREEN = new Color(  0, 255,   0);

    // 游戏界面大小
    public static int SCREEN_WIDTH = 816;
    public static int SCREEN_HEIGHT = 6380;

    // 草坪数据
    public static int GRID_X_LEN = 9;
    public static int GRID_Y_LEN = 5;
    public static int GRID_X_SIZE = 80;
    public static int GRID_Y_SIZE = 100;

    // 游戏信息字典关键字
    public static String CURRENT_TIME = "current time";
    public static String LEVEL_NUM = "level num";

    // 游戏状态
    public static String MAIN_MENU = "main menu";
    public static String LOAD_SCREEN = "load screen";
    public static String GAME_LOSE = "game los";
    public static String GAME_VICTORY = "game victory";
    public static String LEVEL = "level";

    public static String MAIN_MENU_IMAGE = "MainMenu";
    public static String OPTION_ADVENTURE = "Adventure";
    public static String GAME_LOOSE_IMAGE = "GameLoose";
    public static String GAME_VICTORY_IMAGE = "GameVictory";

    // 地图信息
    public static String BACKGROUND_NAME = "Background";
    public static String BACKGROUND_TYPE = "background_type";
    public static String INIT_SUN_NAME = "init_sun_value";
    public static String ZOMBIE_LIST = "zombie_list";

    public static int MAP_EMPTY = 0;
    public static int MAP_EXIST = 1;

    public static int BACKGROUND_OFFSET_X = 220;
    public static int MAP_OFFSET_X = 35;
    public static int MAP_OFFSET_Y = 100;

    // 菜单栏信息
    public static String CHOOSEBAR_TYPE = "choosebar_type";
    public static int CHOOSEBAR_STATIC = 0;
    public static int CHOOSEBAR_MOVE = 1;
    public static int CHOSSEBAR_BOWLING = 2;
    public static String MENUBAR_BACKGROUND = "ChooserBackground";
    public static String MOVEBAR_BACKGROUND = "MoveBackground";
    public static String PANEL_BACKGROUND = "PanelBackground";
    public static String START_BUTTON = "StartButton";
    public static String CARD_POOL = "card_pool";

    public static int MOVEBAR_CARD_FRESH_TIME = 6000;
    public static int CARD_MOVE_TIME = 60;

    // 植物信息
    public static String PLANT_IMAGE_RECT = "plant_image_rect";
    public static String CAR = "car";
    public static String SUN = "Sun";
    public static String SUNFLOWER = "SunFlower";
    public static String PEASHOOTER = "Peashooter";
    public static String SNOWPEASHOOTER = "SnowPea";
    public static String WALLNUT = "WallNut";
    public static String CHERRYBOMB = "CherryBomb";
    public static String THREEPEASHOOTER = "Threepeater";
    public static String REPEATERPEA = "RepeaterPea";
    public static String CHOMPER = "Chomper";
    public static String CHERRY_BOOM_IMAGE = "Boom";
    public static String PUFFSHROOM = "PuffShroom";
    public static String POTATOMINE = "PotatoMine";
    public static String SQUASH = "Squash";
    public static String SPIKEWEED = "Spikeweed";
    public static String JALAPENO = "Jalapeno";
    public static String SCAREDYSHROOM = "ScaredyShroom";
    public static String SUNSHROOM = "SunShroom";
    public static String ICESHROOM = "IceShroom";
    public static String HYPNOSHROOM = "HypnoShroom";
    public static String WALLNUTBOWLING = "WallNutBowling";
    public static String REDWALLNUTBOWLING = "RedWallNutBowling";

    public static int PLANT_HEALTH = 5;
    public static int WALLNUT_HEALTH = 30;
    public static int WALLNUT_CRACKED1_HEALTH = 20;
    public static int WALLNUT_CRACKED2_HEALTH = 10;
    public static int WALLNUT_BOWLING_DAMAGE = 10;
    
    public static int PRODUCE_SUN_INTERVAL = 7000;
    public static int FLOWER_SUN_INTERVAL = 22000;
    public static int SUN_LIVE_TIME = 7000;
    public static int SUN_VALUE = 25;

    public static int ICE_SLOW_TIME = 2000;

    public static int FREEZE_TIME = 7500;
    public static String ICETRAP = "IceTrap";
    
    // 增加的植物状态
    public static int SMALL_SUN_VALUE = 12;
    public static double SMALL_SUN_SCALE = 0.6;
    public static double BIG_SUN_SCALE = 0.9;
    public static double SMALL_SUNSHROOM_SCALE = 0.6;
    public static double BIG_SUNSHROOM_SCALE = 0.6;
    

    // 植物卡片信息
    public static String CARD_SUNFLOWER = "card_sunflower";
    public static String CARD_PEASHOOTER = "card_peashooter";
    public static String CARD_SNOWPEASHOOTER = "card_snowpea";
    public static String CARD_WALLNUT = "card_wallnut";
    public static String CARD_CHERRYBOMB = "card_cherrybomb";
    public static String CARD_THREEPEASHOOTER = "card_threepeashooter";
    public static String CARD_REPEATERPEA = "card_repeaterpea";
    public static String CARD_CHOMPER = "card_chomper";
    public static String CARD_PUFFSHROOM = "card_puffshroom";
    public static String CARD_POTATOMINE = "card_potatomine";
    public static String CARD_SQUASH = "card_squash";
    public static String CARD_SPIKEWEED = "card_spikeweed";
    public static String CARD_JALAPENO = "card_jalapeno";
    public static String CARD_SCAREDYSHROOM = "card_scaredyshroom";
    public static String CARD_SUNSHROOM = "card_sunshroom";
    public static String CARD_ICESHROOM = "card_iceshroom";
    public static String CARD_HYPNOSHROOM = "card_hypnoshroom";
    public static String CARD_REDWALLNUT = "card_redwallnut";
    
    // 子弹信息
    public static String BULLET_PEA = "PeaNormal";
    public static String BULLET_PEA_ICE = "PeaIce";
    public static String BULLET_MUSHROOM = "BulletMushRoom";
    public static int BULLET_DAMAGE_NORMAL = 1;

    // 僵尸信息
    public static String ZOMBIE_IMAGE_RECT = "zombie_image_rect";
    public static String ZOMBIE_HEAD = "ZombieHead";
    public static String NORMAL_ZOMBIE = "Zombie";
    public static String CONEHEAD_ZOMBIE = "ConeheadZombie";
    public static String BUCKETHEAD_ZOMBIE = "BucketheadZombie";
    public static String FLAG_ZOMBIE = "FlagZombie";
    public static String NEWSPAPER_ZOMBIE = "NewspaperZombie";
    public static String BOOMDIE = "BoomDie";

    public static int LOSTHEAD_HEALTH = 5;
    public static int NORMAL_HEALTH = 10;
    public static int FLAG_HEALTH = 15;
    public static int CONEHEAD_HEALTH = 20;
    public static int BUCKETHEAD_HEALTH = 30;
    public static int NEWSPAPER_HEALTH = 15;

    public static int ATTACK_INTERVAL = 1000;
    public static int ZOMBIE_WALK_INTERVAL = 70;

    public static int ZOMBIE_START_X = SCREEN_WIDTH + 50;
    
    // 状态
    public static String IDLE = "idle";
    public static String FLY = "fly";
    public static String EXPLODE = "explode";
    public static String ATTACK = "attack";
    public static String ATTACKED = "attacked";
    public static String DIGEST = "digest";
    public static String WALK = "walk";
    public static String DIE = "die";
    public static String CRY = "cry";
    public static String FREEZE = "freeze";
    public static String SLEEP = "sleep";
    
    // 等级状态
    public static String CHOOSE = "choose";
    public static String PLAY = "play";

    // 背景
    public static int BACKGROUND_DAY = 0;
    public static int BACKGROUND_NIGHT = 1;

    // 植物消耗阳光
    public static int[] plant_sun_list = { 50, 100, 175, 50, 150, 325, 200, 150, 0, 25, 50, 100, 125, 25, 25, 75, 75, 0,
            0 };
    // 植物卡片名称
    public static String[] card_name_list = { CARD_SUNFLOWER, CARD_PEASHOOTER, CARD_SNOWPEASHOOTER, CARD_WALLNUT,
        CARD_CHERRYBOMB, CARD_THREEPEASHOOTER, CARD_REPEATERPEA, CARD_CHOMPER,
        CARD_PUFFSHROOM, CARD_POTATOMINE, CARD_SQUASH, CARD_SPIKEWEED,
        CARD_JALAPENO, CARD_SCAREDYSHROOM, CARD_SUNSHROOM, CARD_ICESHROOM,
            CARD_HYPNOSHROOM, CARD_WALLNUT, CARD_REDWALLNUT };
    // 植物名称
    public static String[] plant_name_list = {SUNFLOWER, PEASHOOTER, SNOWPEASHOOTER, WALLNUT,
        CHERRYBOMB, THREEPEASHOOTER, REPEATERPEA, CHOMPER,
        PUFFSHROOM, POTATOMINE, SQUASH, SPIKEWEED,
        JALAPENO, SCAREDYSHROOM, SUNSHROOM, ICESHROOM,
            HYPNOSHROOM, WALLNUTBOWLING, REDWALLNUTBOWLING };
    // 植物冷却时间
    public static int[] plant_frozen_time_list ={7500, 7500, 7500, 30000, 50000, 7500, 7500, 7500, 7500, 30000,
            30000, 7500, 50000, 7500, 7500, 50000, 30000, 0, 0 };
    public static int[] all_card_list = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

    // 选择面板参数
    public static int PANEL_Y_START = 87;
    public static int PANEL_X_START = 22;
    public static int PANEL_Y_INTERNAL = 74;
    public static int PANEL_X_INTERNAL = 53;
    public static int CARD_LIST_NUM = 8;
    
    
}
