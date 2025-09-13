package com.xxx.fetchurlimage;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 读取归巢数据listener
 */
@Slf4j
public class ReadLocalExcelListener implements ReadListener<LocalExcelModel> {

    private final List<LocalExcelModel> list;

    public ReadLocalExcelListener(List<LocalExcelModel> list) {
        this.list = list;
    }

    @Override
    public void invoke(LocalExcelModel model, AnalysisContext analysisContext) {
        list.add(model);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("读取Excel完毕，总共读取到[{}]条记录", list.size());
    }
}
