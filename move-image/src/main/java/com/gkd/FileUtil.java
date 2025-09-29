package com.gkd;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FileUtil {

    /**
     * 递归读取目录（含子目录）下所有指定后缀的文件
     *
     * @param rootDir 根目录（如 E:/IPhone相册）
     * @param suffix  文件后缀（如 .jpg，注意带点）
     * @return 符合条件的文件列表
     */
    public static List<File> recursiveListFiles(File rootDir, String suffix) {
        List<File> fileList = new ArrayList<>();
        // 1. 校验根目录是否存在且为目录
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            System.err.println("根目录不存在或不是合法目录：" + rootDir.getAbsolutePath());
            return fileList;
        }

        // 2. 遍历目录下所有文件/子目录
        File[] files = rootDir.listFiles();
        if (files == null) { // 目录无权限访问时返回 null
            System.err.println("无权限访问目录：" + rootDir.getAbsolutePath());
            return fileList;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // 3. 若为子目录，递归读取
                fileList.addAll(recursiveListFiles(file, suffix));
            } else {
                // 4. 若为文件，判断后缀是否匹配
                String fileName = file.getName().toLowerCase(); // 忽略大小写（如 .JPG 也匹配）
                if (fileName.endsWith(suffix.toLowerCase())) {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }

    /**
     * 移动文件（原文件会被删除，若目标文件已存在则覆盖）
     *
     * @param sourceFile 源文件（待移动的 .jpg）
     * @param targetDir  目标目录（如 E:/apple）
     * @throws IOException 移动失败时抛出异常
     */
    public static void moveFile(File sourceFile, File targetDir) throws IOException {
        // 1. 确保目标目录存在（不存在则创建）
        if (!targetDir.exists()) {
            boolean mkdirsSuccess = targetDir.mkdirs(); // 递归创建多级目录
            if (!mkdirsSuccess) {
                throw new IOException("创建目标目录失败：" + targetDir.getAbsolutePath());
            }
        }

        // 2. 构建目标文件路径（保持原文件名，避免重复覆盖可自定义命名规则）
        String targetFileName = sourceFile.getName();
        File targetFile = new File(targetDir, targetFileName);

        // 3. 复制文件流（Java 原生工具类，避免手动处理流关闭）
        FileCopyUtils.copy(new FileInputStream(sourceFile), new FileOutputStream(targetFile));

        System.out.println("成功移动文件：" + sourceFile.getAbsolutePath() + " -> " + targetFile.getAbsolutePath());

        // 4. 复制成功后删除源文件（实现“移动”效果）
        //        boolean deleteSuccess = sourceFile.delete();
        //        if (!deleteSuccess) {
        //            System.warn("源文件复制成功，但删除源文件失败：" + sourceFile.getAbsolutePath());
        //            // 可选：若删除失败，可记录日志或抛出异常
        //            // throw new IOException("删除源文件失败：" + sourceFile.getAbsolutePath());
        //        } else {
        //            System.out.println("成功移动文件：" + sourceFile.getAbsolutePath() + " -> " + targetFile.getAbsolutePath());
        //        }
    }
}
