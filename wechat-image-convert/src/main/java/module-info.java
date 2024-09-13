module com.gkd.wechatimageconvert {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.gkd.wechatimageconvert to javafx.fxml;
    exports com.gkd.wechatimageconvert;
}