package com.gkd;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class App implements CommandLineRunner {

    // 1. 配置源目录和目标目录（建议放在配置文件中，此处为简化直接硬编码）
    private static final String SOURCE_DIR_PATH = "E:/IPhone相册"; // 源目录（待读取的图片目录）
    private static final String TARGET_DIR_PATH = "E:/apple";     // 目标目录（图片移动到这里）
    private static final String FILE_SUFFIX = ".jpg";             // 目标文件后缀

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== 开始执行图片移动任务 ===");

        // 2. 初始化源目录和目标目录对象
        File sourceDir = new File(SOURCE_DIR_PATH);
        File targetDir = new File(TARGET_DIR_PATH);

        // 3. 递归读取所有 .jpg 文件
        List<File> jpgFiles = FileUtil.recursiveListFiles(sourceDir, FILE_SUFFIX);
        if (jpgFiles.isEmpty()) {
            System.out.println("未找到任何 " + FILE_SUFFIX + " 文件，任务结束。");
            return;
        }
        System.out.println("共找到 " + jpgFiles.size() + " 个 " + FILE_SUFFIX + " 文件，开始移动...");

        // 4. 批量移动文件（逐个处理，异常不中断整体任务）
        int successCount = 0;
        int failCount = 0;
        for (File jpgFile : jpgFiles) {
            try {
                FileUtil.moveFile(jpgFile, targetDir);
                successCount++;
            } catch (IOException e) {
                failCount++;
                System.err.println("移动文件失败：" + jpgFile.getAbsolutePath() + "，原因：" + e.getMessage());
            }
        }

        // 5. 输出任务结果
        System.out.println("=== 图片移动任务结束 ===");
        System.out.println("总文件数：" + jpgFiles.size() + "，成功：" + successCount + "，失败：" + failCount);
    }

}
