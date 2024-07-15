package com.api.zx.interfaces;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class ExpoExcel {

//    private static List<DemoData> getData(){
//        List<DemoData> list=new ArrayList<>();
//        for (int i = 0; i <10 ; i++) {
//            DemoData data=new DemoData();
//            data.string(i);
//            data.date("Tom"+i);
//            list.add(data);
//        }
//        return list;
//    }

    private static List<DemoData> data() {
        List<DemoData> list = ListUtils.newArrayList();

        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();

//            data.setString("字符串" + i);
//            data.setDate("2024-07-08");
//            data.setDoubleData(0.56);
//            list.add(data);

            data.STtile="字符串" + i;
            data.SDate="2024-07-08";
            data.DData=0.56;
            list.add(data);


            System.out.println(data.STtile);
            System.out.println(data.SDate);
            System.out.println(data.DData);
        }


//        for (int i = 0; i < 10; i++) {
//            System.out.println(list.get(i).STtile);
//            System.out.println(list.get(i).SDate);
//            System.out.println(list.get(i).DData);
//        }

        return list;
    }


    public static void writeData(){
        List<DemoData> list = data();

        for (int i = 0; i < 3; i++) {
            EasyExcel.write("test.xlsx",DemoData.class).sheet("sheet1").doWrite(list);
        }

    }

//    private static List<List<String>> getHead(){
//        List<List<String>> list = new ArrayList<List<String>>();
//
//        List<String> head0 = new ArrayList<String>();
//        head0.add("字符串");
//
//        List<String> head1 = new ArrayList<String>();
//        head1.add("数字");
//
//        List<String> head2 = new ArrayList<String>();
//        head2.add("日期");
//
//        List<String> head3 = new ArrayList<String>();
//        head3.add("名字");
//
//        list.add(head0);
//        list.add(head1);
//        list.add(head2);
//        list.add(head3);
//
//        return list;
//    }

//    public void simpleWrite() {
//        String fileName = "write" + System.currentTimeMillis() + ".xlsx";
//
//        // 这里 需要指定写用哪个class去读，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
//        // 如果这里想使用03 则 传入excelType参数即可
//        EasyExcel.write(fileName, DemoData.class).sheet("sheet1").doWrite(data());
//    }

    public static void main(String[] args){
        List<DemoData> list = data();

//        EasyExcel.write("test.xlsx",DemoData.class).sheet("模板").doWrite(list);

//        writeData();



        EasyExcel.write("test.xls", DemoData.class)
                .sheet("模板")
                .doWrite(() -> {
                    // 分页查询数据
                    return data();
                });


        System.out.println("Hello,world");
    }
}
