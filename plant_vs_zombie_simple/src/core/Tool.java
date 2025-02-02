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
import java.util.TreeSet;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import core.json.FileUtils;
import core.json.JSONObject;
import java.awt.*;
import java.awt.image.BufferedImage;

class FindJavaVisitor extends SimpleFileVisitor<Path> {

    HashMap<String, TreeSet<Tool.Img>> result;
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

    public static boolean isNumeric(String str) {
        return str != null && NUMBER_PATTERN.matcher(str).matches();
    }

    public FindJavaVisitor(HashMap<String, TreeSet<Tool.Img>> result) {
        this.result = result;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        // 注意有两种后缀
        String fileName = file.getFileName().toString();
        int fileIndex = 0;

        fileName = fileName.split("\\.")[0];
        String s[] = fileName.split("_");
        int len = s.length;
        if (len > 1 && isNumeric(s[len - 1])) {
            fileName = s[0];
            for (int i = 1; i < len - 1; ++i)
                fileName += "_"+s[i];

            fileIndex = Integer.valueOf(s[len - 1]);
        }
        System.out.println(fileName+fileIndex);
        TreeSet<Tool.Img> frameList;

        if (result.get(fileName) == null) {
            frameList = new TreeSet<Tool.Img>();
            result.put(fileName, frameList);
        } else {
            frameList = (TreeSet<Tool.Img>) result.get(fileName);
        }
        frameList.add(
                new Tool.Img(fileIndex, Tool.loadImage(file.toAbsolutePath().toString(), 1, Constants.BLACK)));

        return FileVisitResult.CONTINUE;
    }
}

public class Tool {
    public static HashMap<String, TreeSet<Tool.Img>> GFX = load_all_gfx();
    public static JSONObject ZOMBIE_RECT = loadImageRect("zombie");
    public static JSONObject PLANT_RECT = loadImageRect("plant");

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
                rgb = (((rgb >> 24) & 0xff) << 24) | ((R & 0xff) << 16) | ((G & 0xff) << 8) | ((B & 0xff));
                output.setRGB(j2, j1, rgb);
            }
        }
        return output;
    }

    // 调整透明卡片
    public static BufferedImage adjustHint(BufferedImage image_) {
        int width = image_.getWidth();
        int height = image_.getHeight();
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int j1 = 0; j1 < height; ++j1) {
            for (int j2 = 0; j2 < width; ++j2) {
                int rgb = image_.getRGB(j2, j1);
                int R, G, B, alpha;
                R = ((rgb >> 16) & 0xff);
                G = ((rgb >> 8) & 0xff);
                B = (rgb & 0xff);
                alpha = (rgb >> 24) & 0xff;
                if (alpha != 1)
                {
                    rgb = (128 << 24) | ((R & 0xff) << 16) | ((G & 0xff) << 8) | (B & 0xff);
                    output.setRGB(j2, j1, rgb);
                }
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

        HashMap<String, TreeSet<Img>> result = new HashMap<String, TreeSet<Img>>();
        try {
            Files.walkFileTree(dir, new FindJavaVisitor(result));
            return result;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    public static JSONObject loadImageRect(String file_name) {
        System.out.println("Start loadImageRect=====");
        File file = new File("resources/data/entity/" + file_name + ".json");
        try {
            String content = FileUtils.readFileToString(file, "UTF-8");
            JSONObject jsonObject = new JSONObject(content);
            return jsonObject.getJSONObject(file_name + "_image_rect");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            System.out.println("End loadImageRect=====");
        }
        return null;

    }

}
