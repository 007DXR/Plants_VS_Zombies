package core;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import core.json.FileUtils;

import core.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;

class FindJavaVisitor extends SimpleFileVisitor<Path> {
    // private List<Path> result;
    HashMap<String, TreeSet<Tool.Img>> result;

    public FindJavaVisitor(HashMap<String, TreeSet<Tool.Img>> result) {
        this.result = result;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (file.toString().endsWith(".png")) {
            try {
                String s[] = file.getFileName().toString().split("_");
                String fileName = s[0];
                Integer fileIndex = Integer.valueOf((s[1].split("\\."))[0]);
                // System.out.println(fileIndex);
                TreeSet<Tool.Img> frameList;
                // HashMap<Integer,BufferedImage>frameList;

                if (result.get(fileName) == null) {
                    frameList = new TreeSet<Tool.Img>();
                    result.put(fileName, frameList);
                } else {
                    frameList = (TreeSet<Tool.Img>) result.get(fileName);
                }
                frameList.add(
                        new Tool.Img(fileIndex, Tool.loadImage(file.toAbsolutePath().toString(), 1, Constants.BLACK)));
            } catch (Exception e) {
                System.out.print(file.getFileName());

            }
        }

        return FileVisitResult.CONTINUE;
    }
}

public class Tool {
    public static HashMap<String, TreeSet<Tool.Img>> GFX = load_all_gfx();
    public static JSONObject ZOMBIE_RECT = loadImageRect("zombie.json");
    public static JSONObject PLANT_RECT=loadImageRect("plant.json");

    public static class Img implements Comparable<Img> {
        public int tag;
        public BufferedImage image;

        Img(int tag, BufferedImage image) {
            this.tag = tag;
            this.image = image;
        }

        // 按tag从小到大排序
        public int compareTo(Img o) {
            return tag - o.tag;
        }

    }

    // 加载图片
    public static BufferedImage loadImage(String filename, double scale, Color color) {
        try {
            BufferedImage Img = ImageIO.read(new File(filename));
            Img = resize(Img, scale);
            Img = adjustAlpha(Img, color);
            return Img;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // 调整亮度
    public static BufferedImage adjustBrightness(BufferedImage image_, int alpha) {
        int width = image_.getWidth();
        int height = image_.getHeight();
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int j1 = 0; j1 < height; ++j1) {
            for (int j2 = 0; j2 < width; ++j2) {
                int rgb = image_.getRGB(j2, j1);
                int R, G, B;
                R = ((rgb >> 16) & 0xff) * alpha / 256;
                G = ((rgb >> 8) & 0xff) * alpha / 256;
                B = (rgb & 0xff) * alpha / 256;
                rgb = ((255 & 0xff) << 24) | ((R & 0xff) << 16) | ((G & 0xff) << 8) | ((B & 0xff));
                output.setRGB(j2, j1, rgb);
            }
        }
        return output;
    }

    // 图片缩放
    public static BufferedImage resize(BufferedImage image_, double scale) {
        int width = image_.getWidth();
        int height = image_.getHeight();
        int nwidth = (int) (width * scale);
        int nheight = (int) (height * scale);

        Image Img2 = image_.getScaledInstance(nwidth, nheight, Image.SCALE_DEFAULT);
        BufferedImage output = new BufferedImage(nwidth, nheight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = output.createGraphics();
        graphics.drawImage(Img2, 0, 0, null);
        graphics.dispose();
        return output;
    }

    // 指定颜色透明化
    public static BufferedImage adjustAlpha(BufferedImage image_, Color color) {
        int width = image_.getWidth();
        int height = image_.getHeight();
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        for (int j = 0; j < height; ++j) {
            for (int i = 0; i < width; ++i) {
                int rgb = image_.getRGB(i, j);
                if (checkColor(rgb, color.getRGB())) {
                    rgb = (1 << 24) | (rgb & 0x00ffffff);
                }
                output.setRGB(i, j, rgb);
            }
        }
        return output;
    }

    // 判断颜色相等
    public static boolean checkColor(int color, int target) {
        int rgb_color = color & 0x00ffffff;
        int rgb_target = target & 0x00ffffff;
        return rgb_color == rgb_target;
    }
    // 从resources/graphics读取所有图片，用result.get('xxx')可得到某一类的图片
    public static HashMap<String, TreeSet<Img>> load_all_gfx() {
        Path dir = Paths.get("resources/graphics");

        // List<Path> result = new LinkedList<Path>();
        HashMap<String, TreeSet<Img>> result = new HashMap<String, TreeSet<Img>>();
        try {
            Files.walkFileTree(dir, new FindJavaVisitor(result));
            return result;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
        // System.out.println("result.size()=" + result.size());

    }

    public static JSONObject loadImageRect(String file_name) {
        File file = new File("resources/data/entity/"+file_name);
        try {
            String content = FileUtils.readFileToString(file, "UTF-8");
            JSONObject jsonObject = new JSONObject(content);
            return jsonObject.getJSONObject("zombie_image_rect");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    public static void main(String[] args) {
        TreeSet<Tool.Img> frame_list = (TreeSet<Tool.Img>) Tool.GFX.get("BucketheadZombie");
        for (Tool.Img frame : frame_list) {
            BufferedImage rect = frame.image;
            int width = rect.getWidth();
            int height = rect.getHeight();
            System.out.print(width);
            System.out.println(height);
            // width -= image_x;
            // frames.add(frame.image.getSubimage(image_x, 0, width, height));
            // tool.get_image(frame, image_x, 0, width, height, colorkey));
        }
    }

}
