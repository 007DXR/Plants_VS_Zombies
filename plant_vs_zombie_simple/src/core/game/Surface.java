package core.game;

import java.util.LinkedList;

import javax.swing.JPanel;

import java.awt.Graphics;

/// 画图容器，绘图的组件添加至这个容器以绘图
public class Surface extends JPanel {
    LinkedList<PaintItf> list;
    /// optional add, check first;
    public void add(PaintItf item) {
        if(!list.contains(item))
           list.add(item);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for(PaintItf item: list) {
            item.paintObject(g);
        }
    }
}