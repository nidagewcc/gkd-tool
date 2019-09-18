package com.gkd.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * @author Weishuo Zhang
 * @date 2019/8/1
 * @description
 */
public class RecFileModel extends BaseRowModel {

    @ExcelProperty(value = "业务订单号", index = 0)
    private String orderNo;
    @ExcelProperty(value = "公司代码", index = 1)
    private String mercCode;
    @ExcelProperty(value = "商户号", index = 2)
    private String mercId;
    @ExcelProperty(value = "商户类型", index = 3)
    private String mercType;
    @ExcelProperty(value = "公司名称", index = 4)
    private String mercName;
    @ExcelProperty(value = "交易类型", index = 5)
    private String tradeType;
    @ExcelProperty(value = "交易金额", index = 6)
    private String tradeAmt;
    @ExcelProperty(value = "交易日期", index = 7)
    private String tradeDate;
    @ExcelProperty(value = "结算日期", index = 8)
    private String settleDate;
    @ExcelProperty(value = "传输日期", index = 9)
    private String transferDate;
    @ExcelProperty(value = "传输标识", index = 10)
    private String transferFlag;
    @ExcelProperty(value = "备注", index = 11)
    private String remark;

    public String getOrderNo() {
        return orderNo;
    }

    public RecFileModel setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public String getMercCode() {
        return mercCode;
    }

    public RecFileModel setMercCode(String mercCode) {
        this.mercCode = mercCode;
        return this;
    }

    public String getMercId() {
        return mercId;
    }

    public RecFileModel setMercId(String mercId) {
        this.mercId = mercId;
        return this;
    }

    public String getMercType() {
        return mercType;
    }

    public RecFileModel setMercType(String mercType) {
        this.mercType = mercType;
        return this;
    }

    public String getMercName() {
        return mercName;
    }

    public RecFileModel setMercName(String mercName) {
        this.mercName = mercName;
        return this;
    }

    public String getTradeType() {
        return tradeType;
    }

    public RecFileModel setTradeType(String tradeType) {
        this.tradeType = tradeType;
        return this;
    }

    public String getTradeAmt() {
        return tradeAmt;
    }

    public RecFileModel setTradeAmt(String tradeAmt) {
        this.tradeAmt = tradeAmt;
        return this;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public RecFileModel setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
        return this;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public RecFileModel setSettleDate(String settleDate) {
        this.settleDate = settleDate;
        return this;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public RecFileModel setTransferDate(String transferDate) {
        this.transferDate = transferDate;
        return this;
    }

    public String getTransferFlag() {
        return transferFlag;
    }

    public RecFileModel setTransferFlag(String transferFlag) {
        this.transferFlag = transferFlag;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public RecFileModel setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public static RecFileModel builder() {
        return new RecFileModel();
    }

    @Override
    public String toString() {
        return "RecFileModel{" +
                "orderNo='" + orderNo + '\'' +
                ", mercCode='" + mercCode + '\'' +
                ", mercId='" + mercId + '\'' +
                ", mercType='" + mercType + '\'' +
                ", mercName='" + mercName + '\'' +
                ", tradeType='" + tradeType + '\'' +
                ", tradeAmt='" + tradeAmt + '\'' +
                ", tradeDate='" + tradeDate + '\'' +
                ", settleDate='" + settleDate + '\'' +
                ", transferDate='" + transferDate + '\'' +
                ", transferFlag='" + transferFlag + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
