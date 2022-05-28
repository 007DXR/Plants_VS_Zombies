package core.game;

import java.util.ArrayList;
import java.util.LinkedList;

/// 精灵类的容器
public class Group {
    public LinkedList<Sprite> list;
    public Group() {
        list = new LinkedList<>();
    }
    public void update(ArrayList<Object> args) {
        for (Sprite g: list) {
            g.update(args);
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