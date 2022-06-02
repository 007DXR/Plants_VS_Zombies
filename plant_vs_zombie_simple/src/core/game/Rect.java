package core.game;

import java.awt.image.BufferedImage;

/// 矩形区域指示类
public class Rect {
    public BufferedImage image;
    public int left;
    public int top;
    public Rect(BufferedImage image) {
        this.image = image;
        /// default;
        this.left = 0;
        this.top = 0;
    }
    public Rect(BufferedImage image, int left_, int top_) {
        this.image = image;
        this.left = left_;
        this.top = top_;
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
    public void adjusttop(int top) {
        this.top = top;
    }
    public void adjustleft(int left) {
        this.left = left;
    }
    public void adjustbt(int bottom) {
        this.top = bottom - height();
    }
    public void adjustcx(int centerx) {
        this.left = centerx * 2 - width();
    }
    public void adjustcy(int centery) {
        this.top = centery * 2 - height();
    }
    /// 给定相应属性，假定长宽正确，调整左上角坐标以适应
    public void adjust(int centerx, int bottom) {
        adjustcx(centerx);
        adjustbt(bottom);
    }
    /// 调整中中心位置，假定长宽确定，改变左上角点以适应
    public void adjustxy(int centerx, int centery) {
        adjustcx(centerx);
        adjustcy(centery);
    }
    /// 调整画图的左上角坐标
    public void adjustlt(int left, int top) {
        adjustleft(left);
        adjusttop(top);
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
