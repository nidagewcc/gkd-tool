package com.gkd.image.merge;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Weishuo Zhang
 * @date 2019/9/18
 * @description
 */
public class MergeController {

    private static final String DOT_SYMBOL = ".";


    public static void main(String[] args) {
        String imagePath = "D:\\BaiduNetdiskDownload\\大杂烩壁纸200";

        // 生成拼图目录
        String targetPath = "D:\\BaiduNetdiskDownload\\大杂烩壁纸200\\target";

        File file = new File(imagePath);
        System.out.println(file.getPath());
        System.out.println(file.getName());
        System.out.println(file.isDirectory());

        String types = Arrays.toString(ImageIO.getReaderFormatNames()).toLowerCase();
        System.out.println(types);

        File[] images = file.listFiles(f ->
                types.contains(f.getName().substring(f.getName().lastIndexOf(".") + 1).toLowerCase()));

        if (images != null) {
            Arrays.stream(images).forEach(img -> System.out.println(img.getName()));
        } else {
            System.err.printf("该目录[%s]下不存图片类型文件！！！\r\n", imagePath);
        }

        File targetFile = new File(targetPath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
            System.out.printf("target目录不存在，创建目录[%s]", targetPath);
        }

        // 取集合中前两张照片测试下拼接效果
        try {
            // 自己实现缩略图方法
//            BufferedImage image1 = thumbnail(images[0].getPath(), 300, 300, false);
//            BufferedImage image2 = thumbnail(images[1].getPath(), 300, 300, false);
//            BufferedImage image3 = thumbnail(images[2].getPath(), 300, 300, false);

            // 使用google的Thumbnailator，竟然没有自己定义的方法生成的缩略图清楚！！！！
            assert images != null;
            BufferedImage image1 = Thumbnails.of(images[0]).size(300, 300).keepAspectRatio(false).asBufferedImage();
            BufferedImage image2 = Thumbnails.of(images[1]).size(300, 300).keepAspectRatio(false).asBufferedImage();
            BufferedImage image3 = Thumbnails.of(images[2]).size(300, 300).keepAspectRatio(false).asBufferedImage();

            if (image1 != null && image2 != null && image3 != null) {

                // 水平合并
                int w = image1.getWidth() + image2.getWidth();
                int h = image1.getHeight() + image3.getHeight();
                BufferedImage destImage1 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

                int[] imageArrayOne1 = new int[image1.getWidth() * image1.getHeight()];
                int[] imageArrayOne2 = new int[image2.getWidth() * image2.getHeight()];
                int[] imageArrayOne3 = new int[image3.getWidth() * image3.getHeight()];
                imageArrayOne1 = image1.getRGB(0, 0, image1.getWidth(), image1.getHeight(), imageArrayOne1, 0, image1.getWidth());
                imageArrayOne2 = image2.getRGB(0, 0, image2.getWidth(), image2.getHeight(), imageArrayOne2, 0, image2.getWidth());
                imageArrayOne3 = image3.getRGB(0, 0, image3.getWidth(), image3.getHeight(), imageArrayOne3, 0, image3.getWidth());

                destImage1.setRGB(0, 0, image1.getWidth(), image1.getHeight(), imageArrayOne1, 0, image1.getWidth());
                destImage1.setRGB(image1.getWidth(), 0, image2.getWidth(), image2.getHeight(), imageArrayOne2, 0, image2.getWidth());
                destImage1.setRGB(0, image1.getHeight(), image3.getWidth(), image3.getHeight(), imageArrayOne3, 0, image3.getWidth());

                File targetImg = new File(targetPath + "\\水平和纵向合并_thumbnailator.jpg");

                ImageIO.write(destImage1, "jpg", targetImg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 生成缩略图
     *
     * @param imagePath 图片路径
     * @param w         缩略图宽
     * @param h         缩略图高
     * @param force     是否强制按照宽高设定值生成缩略图（false：则生成最佳比例缩略图）
     */
    private static BufferedImage thumbnail(String imagePath, int w, int h, boolean force) throws IOException {

        // 检查图片文件是否存在
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            System.err.printf("图片[%s]不存在！！！\r\n", imagePath);
            return null;
        }

        // 检查图片文件后缀是否合法
        if (imageFile.getName().contains(DOT_SYMBOL)) {
            String suffix = imageFile.getName().substring(imageFile.getName().lastIndexOf(DOT_SYMBOL) + 1);
            System.out.printf("图片为[%s]类型\r\n", suffix);

            String types = Arrays.toString(ImageIO.getReaderFormatNames());
            if (!types.toLowerCase().contains(suffix.toLowerCase())) {
                System.err.printf("图片类型[%s]不合法！！！", suffix);
                return null;
            }
        }

        Image img = ImageIO.read(imageFile);
        if (!force) {
            // 依据原图与要求的缩略图比例，找到最合适的缩略图比例
            int width = img.getWidth(null);
            int height = img.getHeight(null);
            if ((width * 1.0) / w < (height * 1.0) / h) {
                if (width > w) {
                    h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w / (width * 1.0)));
                }
            } else {
                if (height > h) {
                    w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h / (height * 1.0)));
                }
            }
        }
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
        g.dispose();

        return bi;
    }


    /**
     * 合并任数量的图片成一张图片
     *
     * @param isHorizontal true代表水平合并，fasle代表垂直合并
     * @param imgs         待合并的图片数组
     * @return
     */
    private BufferedImage merge(boolean isHorizontal, BufferedImage[] imgs) {
        // 生成新图片
        BufferedImage destImage = null;
        // 计算新图片的长和高
        int allw = 0, allh = 0, allwMax = 0, allhMax = 0;
        // 获取总长、总宽、最长、最宽
        for (int i = 0; i < imgs.length; i++) {
            BufferedImage img = imgs[i];
            allw += img.getWidth();
            allh += img.getHeight();
            if (img.getWidth() > allwMax) {
                allwMax = img.getWidth();
            }
            if (img.getHeight() > allhMax) {
                allhMax = img.getHeight();
            }
        }
        // 创建新图片
        if (isHorizontal) {
            destImage = new BufferedImage(allw, allhMax, BufferedImage.TYPE_INT_RGB);
        } else {
            destImage = new BufferedImage(allwMax, allh, BufferedImage.TYPE_INT_RGB);
        }
        // 合并所有子图片到新图片
        int wx = 0, wy = 0;
        for (int i = 0; i < imgs.length; i++) {
            BufferedImage img = imgs[i];
            int w1 = img.getWidth();
            int h1 = img.getHeight();
            // 从图片中读取RGB
            int[] ImageArrayOne = new int[w1 * h1];
            ImageArrayOne = img.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 逐行扫描图像中各个像素的RGB到数组中
            if (isHorizontal) { // 水平方向合并
                destImage.setRGB(wx, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            } else { // 垂直方向合并
                destImage.setRGB(0, wy, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            }
            wx += w1;
            wy += h1;
        }
        return destImage;
    }


}
