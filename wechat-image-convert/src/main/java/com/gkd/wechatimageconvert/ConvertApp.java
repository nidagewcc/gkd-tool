package com.gkd.wechatimageconvert;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class ConvertApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // 加载FXML文件
        Parent root = FXMLLoader.load(Objects.requireNonNull(ConvertApp.class.getResource("convert.fxml")));
        stage.setTitle("WeChat .dat文件批量转换工具");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
