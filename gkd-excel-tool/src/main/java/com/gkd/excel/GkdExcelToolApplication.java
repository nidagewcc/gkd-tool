package com.gkd.excel;

import com.gkd.excel.model.TestModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class GkdExcelToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(GkdExcelToolApplication.class, args);
    }

    /**
     * 带有映射model类的导出
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/export")
    public String export(HttpServletRequest request, HttpServletResponse response) {
        List<TestModel> list = this.createTestList();

        // 导出带Model映射的excel
        ExcelExBuilder.create("测试exce下载", list).withHeadModel(TestModel.class)
                .configHttpServlet(request, response)
                .setSheet("测试sheet", 1)
                .doExport();

        return "SUCCESS!!!";
    }


    /**
     * 自定义表头的导出
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/export1")
    public String export1(HttpServletRequest request, HttpServletResponse response) {
        List<List<Object>> listObject = this.createTestListObject();

        // 导出Excel，通过withColumn设置表头的excel
        ExcelExBuilder.create("exce文件导出测试").setObjectList(listObject)
                .withColumn("姓名", 0)
                .withColumn("年龄", 1)
                .withColumn("性别", 2)
                .setSheet("一个sheet", 1)
                .configHttpServlet(request, response)
                .doExport();

        return "SUCCESS!!!";
    }


    private List<TestModel> createTestList() {
        List<TestModel> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            TestModel testModel = new TestModel();
            testModel.setName("老张");
            testModel.setAge("123");
            testModel.setSex("男");
            list.add(testModel);
        }
        return list;
    }

    private List<List<Object>> createTestListObject() {
        List<List<Object>> object = new ArrayList<List<Object>>();
        for (int i = 0; i < 1000; i++) {
            List<Object> da = new ArrayList<Object>();
            da.add("老张");
            da.add("28");
            da.add("男");

            object.add(da);
        }
        return object;
    }

}





