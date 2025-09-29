package com.xxx.fetchurlimage;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

public class FetchMain {

    // 641, 48
    // 788, 304

    public static void main(String[] args) throws FileNotFoundException {

        String targetDir = "D://qrcode/t3";

        // 读取本地D://qrcode目录下的1文件夹中的所有图片
        File file = new File("D://qrcode/3");
        File[] files = file.listFiles();
        for (File f : files) {
            // 然后将图片截取中间的二维码部分
            final String name = f.getName();
            String targetImagePath = targetDir + "/" + name;

            ImgUtil.cut(
                    FileUtil.file(f),
                    FileUtil.file(targetImagePath),
                    new Rectangle(251, 434, 577, 625)//裁剪的矩形区域
            );
            System.out.printf("保存%s成功\r\n", targetImagePath);
        }


    }
}
