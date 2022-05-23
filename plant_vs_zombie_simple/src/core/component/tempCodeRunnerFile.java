 
    // 加载图片
    public static BufferedImage loadImage(String filename, double scale, Color color) {
        try {
            BufferedImage img = ImageIO.read(new File(filename));
            img = resize(img, scale);
            img = adjustAlpha(img, color);
            return img;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    