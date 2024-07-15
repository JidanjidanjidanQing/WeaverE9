package com.api.zx.interfaces;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;


public class DemoData {

    @ExcelProperty("字符串标题")
    public String STtile;

    @ExcelProperty("日期标题")
    public String SDate;

    @ExcelProperty("数字标题")
    public Double DData;
    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    public String ignore;


    // Getters and setters
    public void setString(String string) {
        this.STtile = STtile;
    }

    public void setDate(String date) {
        this.SDate = SDate;
    }


    public void setDoubleData(Double doubleData) {
        this.DData = DData;
    }


}