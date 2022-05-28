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



/// 精灵类，处理碰撞，实现绘图接口
public abstract class Sprite 
    implements PaintItf
{
    //矩形碰撞体积
    public Rect rect;
    /// a pointer to delete elements in group when kill is ready
    public LinkedList<Group> ptr;
    /// 可选参数以表明原型的碰撞体积
    public double radius;

    /// 将图片画出来
    public void paintObject(Graphics g) {
        g.drawImage(rect.image, rect.left, rect.top, null);
    }
    public Sprite(){}
    public Sprite(Rect rect) {
        this.rect = rect;
        ptr = new LinkedList<>();
    }
    public Sprite(BufferedImage image) {
        this.rect.image = image;
        ptr = new LinkedList<>();
    }
    /// 检查一个精灵是否和一个组中的元素相交，若相交，返回检查到的第一个
    /// param 默认情况可以为1.0,表示相交区域的缩放程度
    public Sprite spritecollideany(Group group, CollidedFunc collidedFunc) {
        for(Sprite s: group.list) {
            if (collidedFunc.collid(this, s)) return s;
        }
        // 找不到
        return null;
    }
    public void update(ArrayList<Object> args) {

    }
    public void added(Group g) {
        ptr.add(g);
    }
    public void kill() {
        for(Group g: ptr) {
            g.remove(this);
        }
    }
}
