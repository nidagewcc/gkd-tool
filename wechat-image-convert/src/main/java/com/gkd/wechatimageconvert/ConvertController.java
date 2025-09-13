package com.gkd.wechatimageconvert;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class ConvertController {


    private File inputDirectory;  // 输入目录
    private File outputDirectory;  // 输出目录

    @FXML
    public Label fileCountLabel;
    @FXML
    public Label fileSizeLabel;
    @FXML
    private Label inputDirPathLabel;  // FXML文件中的标签
    @FXML
    private Label outputDirPathLabel;  // FXML文件中的标签

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private ProgressBar progressBar;

    private ExecutorService executorService;  // 线程池

    private final AtomicLong totalFileCount = new AtomicLong(0);
    private final AtomicLong processedFileCount = new AtomicLong(0);

    @FXML
    public void initialize() {
        //        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());  // 创建线程池
        executorService = Executors.newFixedThreadPool(4);  // 创建线程池
        progressBar.setProgress(0);  // 初始化进度条
    }


    // 选择输入目录
    @FXML
    private void chooseInputDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择输入目录");
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if (selectedDirectory != null) {
            inputDirectory = selectedDirectory;
            inputDirPathLabel.setText(selectedDirectory.getAbsolutePath());
        }
    }

    // 选择输出目录
    @FXML
    private void chooseOutputDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择输出目录");
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if (selectedDirectory != null) {
            outputDirectory = selectedDirectory;
            outputDirPathLabel.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void calcFileStorage() {
        progressIndicator.setVisible(true);
        progressIndicator.setProgress(-1);
        // 创建后台任务
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                AtomicLong fileCount = new AtomicLong(0);
                AtomicLong totalSize = new AtomicLong(0);

                // 执行文件计数操作
                countFiles(inputDirectory, fileCount, totalSize);

                // 获取可读的字节数格式
                final String totalStorage = humanReadableByteCount(totalSize.get(), false);

                totalFileCount.set(fileCount.get());

                // 更新 UI 需要在 JavaFX 应用程序线程中进行
                Platform.runLater(() -> {
                    fileCountLabel.setText(String.valueOf(fileCount));
                    fileSizeLabel.setText("文件大小：" + totalStorage);
                    progressIndicator.setVisible(false);  // 隐藏加载指示器
                    progressIndicator.setProgress(1);  // 设置为完成
                });
                return null;
            }
        };

        // 启动后台任务
        new Thread(task).start();
    }

    private void countFiles(File fileDir, AtomicLong fileCount, AtomicLong totalSize) {
        final File[] files = fileDir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                countFiles(file, fileCount, totalSize);
            } else {
                fileCount.getAndIncrement();
                try {
                    totalSize.addAndGet(Files.size(file.toPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 批量转换 .dat 文件为图片
    @FXML
    private void convertFiles() {
        if (inputDirectory != null && outputDirectory != null) {
            convertDatFiles(inputDirectory, outputDirectory);
            //            System.out.println("所有文件已成功转换！");
        } else {
            System.out.println("请先选择输入和输出目录！");
        }
    }

    // 批量转换 .dat 文件为图片
    private void convertDatFiles(File inputDir, File outputDir) {
        progressBar.setProgress(0);  // 重置进度条
        processedFileCount.set(0); // 已处理的文件个数，也先重置为0

        if (totalFileCount.get() == 0) { // 如果没有执行计算文件数量，先计算一遍
            countFiles(inputDir, totalFileCount, new AtomicLong(0));
        }

        convert(inputDir, outputDir);
    }

    private void convert(File inputDir, File outputDir) {
        File[] files = inputDir.listFiles();
        if (files == null || files.length == 0) {
            //            System.out.println("目录:" + inputDir.getAbsolutePath() + "为空，跳过！");
            return;
        }

        List<File> dirs = new ArrayList<>();
        List<File> datFiles = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                dirs.add(file);
            }
            if (file.getName().toLowerCase().endsWith(".dat")) {
                datFiles.add(file);
            }
        }
        //        System.out.println("目录：" + inputDir.getAbsolutePath() + "下有" + dirs.size() + "个子目录，" + datFiles.size() + "个.dat文件！");

        // 优先处理当前目录下的dat文件
        for (File datFile : datFiles) {
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() {
                    String datFileName = datFile.getName().substring(0, datFile.getName().length() - 4); // 去掉 ".dat"
                    final String parent = datFile.getParent();
                    final String dir = parent.substring(parent.indexOf("MsgAttach") + 9);
                    File newOutputDir = new File(outputDir.getAbsoluteFile() + dir);
                    // Step 2: 递归处理文件并更新进度
                    decodeDatFile(datFile, datFileName, newOutputDir); // 解码文件

                    // 处理完之后更新一下进度条
                    final long l = processedFileCount.incrementAndGet();
                    final BigDecimal progress = BigDecimal.valueOf(l).divide(BigDecimal.valueOf(totalFileCount.get()), 2, RoundingMode.CEILING);
                    System.out.println("已处理文件：" + l);
                    System.out.println("总文件个数：" + totalFileCount.get());
                    System.out.println("进度：" + progress);
                    Platform.runLater(() -> progressBar.setProgress(progress.doubleValue()));

                    return null;
                }
            };
            executorService.submit(task);  // 提交任务给线程池
        }

        // 再递归处理子目录
        for (File dir : dirs) {
            convert(dir, outputDir);
            //            System.out.println("目录：" + dir.getAbsolutePath() + "转换完毕！！");
        }
    }

    // 解码 .dat 文件并保存为图片
    private static void decodeDatFile(File datFile, String datFileName, File outputPath) {
        try {
            FileInputStream datInputStream = new FileInputStream(datFile);
            byte[] xorAndFormat = calculateXorAndFormat(datFile); // 计算异或值和格式

            if (xorAndFormat == null) {
                System.out.println("无法计算文件格式：" + datFile.getName());
                return;
            }

            byte xorKey = xorAndFormat[0];
            int format = xorAndFormat[1];
            String extension = format == 1 ? ".png" : (format == 2 ? ".jpg" : ".gif");

            if (!outputPath.exists()) {
                outputPath.mkdirs();
            }

            File outputFile = new File(outputPath, datFileName + extension);
            FileOutputStream imageOutputStream = new FileOutputStream(outputFile);

            int readByte;
            while ((readByte = datInputStream.read()) != -1) {
                imageOutputStream.write(readByte ^ xorKey); // 异或解码
            }

            datInputStream.close();
            imageOutputStream.close();
            //            System.out.println("文件解密成功: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("解密文件过程中出错: " + e.getMessage());
        }
    }

    // 计算异或值和格式（返回一个数组，第一个是异或值，第二个是格式）
    private static byte[] calculateXorAndFormat(File datFile) {
        try (FileInputStream datInputStream = new FileInputStream(datFile)) {
            byte[][] formats = {
                    {(byte) 0x89, (byte) 0x50, (byte) 0x4e},  // PNG
                    {(byte) 0xff, (byte) 0xd8, (byte) 0xff},  // JPG
                    {(byte) 0x47, (byte) 0x49, (byte) 0x46}   // GIF
            };

            byte[] header = new byte[3];
            if (datInputStream.read(header) != 3) {
                return null;
            }

            for (int j = 0; j < formats.length; j++) {
                byte[] formatHeader = formats[j];
                byte[] res = new byte[3];
                for (int i = 0; i < 3; i++) {
                    res[i] = (byte) (header[i] ^ formatHeader[i]);
                }
                if (res[0] == res[1] && res[1] == res[2]) {
                    return new byte[]{res[0], (byte) (j + 1)};
                }
            }
        } catch (IOException e) {
            System.err.println("读取文件头部信息失败: " + e.getMessage());
        }
        return null;
    }

    private String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
