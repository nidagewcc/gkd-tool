package com.xxx.fetchurlimage;

import com.alibaba.excel.EasyExcel;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class FetchUrlImageApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(FetchUrlImageApplication.class, args);

        // 首先读取本地D://设备图片链接.xls
        File file = new File("D://设备图片链接.xls");
        // 转换成InputStream
        FileInputStream inputStream = new FileInputStream(file);

        // 使用easyexcel读取即可
        List<LocalExcelModel> list = new ArrayList<>();

        EasyExcel.read(inputStream, LocalExcelModel.class, new ReadLocalExcelListener(list)).sheet()
                .doRead();

        System.out.println(list.size());

        for (LocalExcelModel model : list) {
            final String[] split = model.getUrl().split("sn=");
            String savePath = "D://devices/" + split[1] + ".png";

            // 1. 配置 ChromeDriver 路径（Windows 需指定 exe 路径，Linux/Mac 无需后缀）
            System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64\\chromedriver.exe");

            // 2. 配置 Chrome 选项（无头模式：不弹出浏览器窗口，适合服务器运行）
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new"); // 无头模式（Chrome 112+ 推荐）
            options.addArguments("--window-size=800,800"); // 设置窗口尺寸（确保二维码完整显示）

            // 3. 启动浏览器并加载目标网页
            WebDriver driver = new ChromeDriver(options);
            String targetUrl = model.getUrl();
            driver.get(targetUrl);

            // 4. 等待 JS 渲染二维码（根据网页加载速度调整，建议 1-3 秒）
            Thread.sleep(3000);

            // 5. 全屏截图
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            BufferedImage fullImage = ImageIO.read(screenshotFile);

            // 6. 裁剪二维码区域（需根据实际页面调整坐标和尺寸！）
            // 示例：假设二维码居中，尺寸 300x300，左上角坐标 (250,250)（可通过浏览器开发者工具查看）
            int qrX = 250;    // 二维码左上角 X 坐标
            int qrY = 190;    // 二维码左上角 Y 坐标
            int qrWidth = 267;// 二维码宽度
            int qrHeight = 285;// 二维码高度
            BufferedImage qrImage = fullImage.getSubimage(qrX, qrY, qrWidth, qrHeight);

            // 7. 保存二维码图片到本地
            File qrOutputFile = new File(savePath);
            ImageIO.write(qrImage, "png", qrOutputFile);

            // 8. 关闭浏览器
            driver.quit();
            System.out.println("二维码已保存至：" + qrOutputFile.getAbsolutePath());
        }

    }


}
