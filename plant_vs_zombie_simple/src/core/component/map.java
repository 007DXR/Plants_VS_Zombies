package core.component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

/// map实例类，x坐标代表宽度，y坐标代表高度
/// 命名为map_x风格的变量指示map中相应格子的坐标
/// 名为为x风格的变量指示了图像单位的坐标
public class map {
    public int width;
    public int height;
    /// 0/1 以指示相应格子上是否存在植物
    public int[][] map;
    public BufferedImage image;
    /// 创建新的地图实例，长宽为格子数量
    public map(int width_, int height_) {
        width = width_;
        height = height_;
        try {
            image = ImageIO
                .read(new File("plant_vs_zombie_simple\\resources\\graphics\\Items\\Background\\Background_0.jpg"));
            width = image.getWidth();
            height = image.getHeight();
        } catch(Exception e) {}
        map = new int[height][width];
        for(int i = 0; i < height; ++i)
        for(int j = 0; j < width; ++j)
            map[i][j] = 0;
    }
    /// 校验传入的坐标是否合法
    public boolean isValid(int map_x, int map_y) {
        return (map_x >= 0 && map_x < width) && (map_y >= 0 && map_y < height);
    }
    public boolean isMovable(int map_x, int map_y) {
        return map[map_y][map_x] == c.MAP_EMPTY;
    }
    /// 传入图像单位，传出相应格子位置，返回ArrayList, [0]保存x坐标格子，[1]保存y坐标格子
    public ArrayList<Integer> getMapIndex(int x, int y) {
        x -= c.MAP_OFFSET_X;
        y -= c.MAP_OFFSET_Y;
        ArrayList<Integer> list = new ArrayList<>();
        list.add(x / c.GRID_X_SIZE);
        list.add(y / c.GRID_Y_SIZE);
        return list;
    }
    /// 传入格子坐标，传出图像单位
    public ArrayList<Integer> getMapGridPos(int map_x, int map_y) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(map_x * c.GRID_X_SIZE + c.GRID_X_SIZE / 2 + c.MAP_OFFSET_X);
        list.add(map_y * c.GRID_Y_SIZE + c.GRID_Y_SIZE / 5 * 3 + c.MAP_OFFSET_Y);
        return list;
    }
    /// 传入格子，并指示是否存在植物 0/1
    public void setMapGridType(int map_x, int map_y, int type) {
        map[map_y][map_x] = type;
    }
    /// 传出随机的格子
    public ArrayList<Integer> getRandomMapIndex() {
        Random random = new Random();
        int map_x = random.nextInt(0, width - 1);
        int map_y = random.nextInt(0, height - 1);
        ArrayList<Integer> list = new ArrayList<>();
        list.add(map_x);
        list.add(map_y);
        return list;
    }
    /// 传入图像坐标，修正对应坐标到格子中心点以用于显示植物
    public ArrayList<Integer> showPlant(int x, int y) {
        ArrayList<Integer> pos = new ArrayList<>();
        ArrayList<Integer> tmp;
        int map_x,map_y;
        tmp = getMapIndex(x, y);
        map_x = tmp.get(0);
        map_y = tmp.get(1);
        if (isValid(map_x, map_y) && isMovable(map_x, map_y)) {
            pos = getMapGridPos(map_x, map_y);
        }
        return pos;
    }
}
