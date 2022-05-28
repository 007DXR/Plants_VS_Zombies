package core.game;

import java.awt.image.BufferedImage;

/// 矩形区域指示类
public class Rect {
    BufferedImage image;
    public int left;
    public int top;
    public Rect(BufferedImage image) {
        this.image = image;
        /// default;
        left = 0;
        top = 0;
    }
    public Rect(BufferedImage image, int left_, int top_) {
        this.image = image;
        left = left_;
        top = top_;
    }
    public int height() {
        return image.getHeight();
    }
    public int width() {
        return image.getWidth();
    }
    public int bottom() {
        return top + height();
    }
    public int centerx() {
        return (left + width()) / 2;
    }
    public int centery() {
        return (top + height()) / 2;
    }
    public int centerx(double ratio) {
        return (int)((left + width() * ratio) / 2);
    }
    public int centery(double ratio) {
        return (int)((top + height() * ratio) / 2);
    }
    /// 给定相应属性，假定长宽正确，调整左上角坐标以适应
    public void adjust(int centerx, int bottom) {
        left = centerx * 2 - width();
        top = bottom - height();
    }
    /// 调整中中心位置，假定长宽确定，改变左上角点以适应
    public void adjustxy(int centerx, int centery) {
        left = centerx * 2 - width();
        top = centery * 2 - height();
    }
    /// 调整画图的左上角坐标
    public void adjustlt(int left, int top) {
        this.left = left;
        this.top = top;
    }
    public boolean intersect(Rect y, double ratio) {
        double xa,ya,wxa,wya;
        double xb,yb,wxb,wyb;
        if (width() > height()) {
            xa = width();
            ya = height();
            wxa = centerx(ratio);
            wya = centery(ratio);
        } else {
            xa = height();
            ya = width();
            wxa = centery(ratio);
            wya = centerx(ratio);
        }
        if (y.width() > y.height()) {
            xb = y.width();
            yb = y.height();
            wxb = y.centerx(ratio);
            wyb = y.centery(ratio);
        } else {
            xb = y.height();
            yb = y.width();
            wxb = y.centery(ratio);
            wyb = y.centerx(ratio);
        }
        xa *= ratio;
        ya *= ratio;
        wxa *= ratio;
        wya *= ratio;
        xb *= ratio;
        yb *= ratio;
        wxb *= ratio;
        wyb *= ratio;

        return Math.abs(wxa - wxb) < (xa + xb) / 2 &&
            Math.abs(wya - wyb) < (ya + yb) / 2;
    }
    
}
