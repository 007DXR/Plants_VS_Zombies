package core.game;

import java.util.ArrayList;
import java.util.LinkedList;

import java.awt.Graphics;
/// 精灵类的容器
public class Group {
    public LinkedList<Sprite> list;
    public Group() {
        list = new LinkedList<>();
    }
    public void update() {
        for (Sprite g: list) {
            g.update();
        }
    }
    public void paintObject(Graphics g) {
        for (Sprite i: list) {
            i.paintObject(g);
        }
    }
    public int size() {
        return list.size();
    }
    public void add(Sprite item) {
        list.add(item);
        item.added(this);
    }
    public void remove(Sprite item) {
        list.remove(item);
    }
}