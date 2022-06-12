package core.game;

import java.util.ArrayList;
import java.util.LinkedList;

import java.awt.Graphics;
/// 精灵类的容器
public class Group {
    public LinkedList<Sprite> list;
    // 因为java的for给iterator加锁，不能在for的同时删除list的元素，因此需要在这里保存，在update的末尾统一删除
    public LinkedList<Sprite> del;
    public Group() {
        list = new LinkedList<>();
        del = new LinkedList<>();
    }
    // 统一update group中的所有元素
    public void update() {
        for (Sprite g: list) {
            g.update();
        }
        // 在update结束后统一删除所有的元素
        list.removeAll(del);
        del.clear();
    }
    // 统一对group中所有元素画图
    public void paintObject(Graphics g) {
        for (Sprite i: list) {
            i.paintObject(g);
        }
    }
    public int size() {
        return list.size();
    }
    // 增加某个元素
    public void add(Sprite item) {
        list.add(item);
        item.added(this);
    }
    // 删除某个元素
    public void remove(Sprite item) {
        del.add(item);
    }
}