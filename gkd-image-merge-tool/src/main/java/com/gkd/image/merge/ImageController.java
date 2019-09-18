package com.gkd.image.merge;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Weishuo Zhang
 * @date 2019/9/18
 * @description
 */
public class ImageController {

    private static final String DOT_SYMBOL = ".";


    /**
     * 生成缩略图
     *
     * @param imagePath 图片路径
     * @param w         缩略图宽
     * @param h         缩略图高
     * @param force     是否强制按照宽高设定值生成缩略图（false：则生成最佳比例缩略图）
     */
    public Image thumbnailImage(String imagePath, int w, int h, boolean force) throws IOException {

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

}
