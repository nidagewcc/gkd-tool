package com.gkd.excel;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Weishuo Zhang
 * @date 2019/9/18
 * @description
 */
public class ExcelExBuilder {

    /**
     * 文件名称
     */
    private String name;

    /**
     * 通过model映射表头的数据list
     */
    private List<? extends BaseRowModel> list;

    /**
     * 映射model类的class类型
     */
    private Class<? extends BaseRowModel> clazz;

    /**
     * sheet no
     */
    private int sheetNo;

    /**
     * sheet名称
     */
    private String sheetName;


    /**
     * 单纯的表头list
     */
    private List<List<String>> columnList;

    /**
     * 数据list
     */
    private List<List<Object>> tableList;

    private HttpServletRequest request;
    private HttpServletResponse response;

    public ExcelExBuilder() {
    }

    public ExcelExBuilder(String name, List<? extends BaseRowModel> list) {
        this.name = name;
        this.list = list;
    }

    public ExcelExBuilder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ExcelExBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public List<? extends BaseRowModel> getList() {
        return list;
    }

    public ExcelExBuilder setModelList(List<? extends BaseRowModel> list) {
        this.list = list;
        return this;
    }

    public ExcelExBuilder setObjectList(List<List<Object>> list) {
        this.tableList = list;
        return this;
    }

    public ExcelExBuilder configHttpServlet(HttpServletRequest request, HttpServletResponse resp) {
        this.request = request;
        this.response = resp;
        return this;
    }

    public ExcelExBuilder withHeadModel(Class<? extends BaseRowModel> clazz) {
        this.clazz = clazz;
        return this;
    }

    public ExcelExBuilder withColumn(String columnName, int index) {
        List<String> list = new LinkedList<>();
        list.add(columnName);

        if (CollectionUtils.isEmpty(this.columnList)) {
            this.columnList = new LinkedList<>();
            this.columnList.add(index, list);
        } else {
            this.columnList.add(index, list);
        }
        return this;
    }

    public ExcelExBuilder setSheet(String sheetName, int sheetNo) {
        this.sheetName = sheetName;
        this.sheetNo = sheetNo;
        return this;
    }

    public static ExcelExBuilder create() {
        return new ExcelExBuilder();
    }

    public static ExcelExBuilder create(String fileName, List<? extends BaseRowModel> list) {
        return new ExcelExBuilder(fileName, list);
    }

    public static ExcelExBuilder create(String fileName) {
        return new ExcelExBuilder(fileName);
    }

    public void doExport() {
        try (ServletOutputStream out = response.getOutputStream()) {
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");

            // 文件名编码
            String filename = encodeDownloadFilename(name, request.getHeader("User-Agent"));
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");

            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
            if (clazz == null) {
                Sheet sheet = new Sheet(sheetNo, 0);
                sheet.setSheetName(sheetName);
                sheet.setHead(columnList);
                writer.write1(tableList, sheet);
                writer.finish();
            } else {
                Sheet sheet = new Sheet(sheetNo, 0, clazz);
                sheet.setSheetName(sheetName);
                writer.write(list, sheet);
                writer.finish();
            }

            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 编译下载的文件名
     *
     * @param filename
     * @param agent
     * @return
     * @throws IOException
     */
    private static String encodeDownloadFilename(String filename, String agent) throws UnsupportedEncodingException {

        // 火狐浏览器
        if (agent.contains("Firefox")) {
            filename = "=?UTF-8?B?" + new BASE64Encoder().encode(filename.getBytes("utf-8")) + "?=";
            filename = filename.replaceAll("\r\n", "");
        } else { // IE及其他浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        return filename;
    }

}
