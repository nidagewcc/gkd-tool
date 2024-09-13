package com.gkd.wechatimageconvert;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WeChatDatDecrypt {

    // 定义异或操作的密钥
    private static final byte XOR_KEY = 0x75;

    public static void decryptDatFile(String inputFilePath, String outputFilePath) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            // 打开加密的 .dat 文件
            fis = new FileInputStream(inputFilePath);
            // 创建输出文件流，用于保存解密后的图片
            fos = new FileOutputStream(outputFilePath);

            int b;
            // 读取每个字节，进行异或解密，并写入输出文件
            while ((b = fis.read()) != -1) {
                fos.write(b ^ XOR_KEY);  // 解密每个字节
            }

            System.out.println("文件解密成功！保存到: " + outputFilePath);

        } catch (IOException e) {
            System.err.println("解密过程中出现错误: " + e.getMessage());
        } finally {
            // 确保文件流在完成操作后正确关闭
            try {
                if (fis != null)
                    fis.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                System.err.println("关闭文件流时出错: " + e.getMessage());
            }
        }
    }
}
