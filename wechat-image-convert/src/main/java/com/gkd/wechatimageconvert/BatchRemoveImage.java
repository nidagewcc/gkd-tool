package com.gkd.wechatimageconvert;

import java.io.File;

/**
 * 批量删除指定目录下的所有.jpg文件
 */
public class BatchRemoveImage {

    public static void main(String[] args) {
        // 指定目录路径
        String directoryPath = "D:\\documents\\WeChat Files\\nidagewcc\\FileStorage\\Video\\2024-12";
        File directory = new File(directoryPath);

        // 检查目录是否存在
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("指定的目录不存在或不是一个目录");
            return;
        }

        // 获取目录下的所有文件
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));

        if (files != null) {
            for (File file : files) {
                if (file.delete()) {
                    System.out.println("文件已删除: " + file.getName());
                } else {
                    System.out.println("文件删除失败: " + file.getName());
                }
            }
        } else {
            System.out.println("目录中没有找到任何文件");
        }
    }
}
