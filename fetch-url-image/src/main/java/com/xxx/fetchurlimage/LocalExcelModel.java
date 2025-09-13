package com.xxx.fetchurlimage;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalExcelModel {

    @ExcelProperty("设备图片地址")
    private String url;

}
