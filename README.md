# gkd-tool 

---

### gkd-excel-tool Excel导出工具

web项目中经常遇到Excel文件导出功能，为了使开发更有效率和方便，基于阿里的easyexcel进行了二次封装，编写相关代码的时候更优雅、方便、快捷。

#### 当前实现的功能：

- 带有映射model类的导出（见示例）；
- 如果导出excel表头列比较少，可通过代码自定义表头，无需定义model类，省时省力；

##### 待增加功能

- 数据量大时，拆分sheet；
- 数据量大时，拆分多excel，并打zip合并导出；
- 多线程。。。


##### demo1：带有映射model类的导出

```
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
```
##### 导出效果：
![image](https://github.com/nidagewcc/images/blob/master/excel1.png?raw=true)


##### demo2：自定义表头


```

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
```
##### 导出效果：同上


---

### gkd-image-merge-tool 图片拼接/合并工具


##### 合并效果：

![image](https://github.com/nidagewcc/images/blob/master/最终.png?raw=true)