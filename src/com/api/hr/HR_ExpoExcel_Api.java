package com.api.hr;


import net.sf.json.JSONObject;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;




@Path("/hr/expoexcel")
public class HR_ExpoExcel_Api {
    BaseBean bb = new BaseBean();
    public HR_ExpoExcel_Api() {
    }

    /**
     * 基于POI向Excel文件写入数据
     * @throws Exception
     */
    @POST
    @Path("/orderalert")
    @Produces({"text/plain"})
    public String write(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {

        //获取当前模块id
        String id = request.getParameter("id");

        this.bb.writeLog("ApachePOI开始执行 id = "+id);

        JSONObject result = new JSONObject();
        result.put("code",1);
        result.put("path","Expo Excel for HR Order Alert Failed");

        //根据ids判断，导出的是哪个报表
        switch(id)
        {
            //入库2天内未领用工时订单
            case "155": return ExpoExcel155();

            //入库6天未关闭订单
            case "156": return ExpoExcel156();

            //修改过工时的订单
            case "157": return ExpoExcel157();

            //修改过领料的订单 158
            case "158": return ExpoExcel158();

            default: return result.toString();
        }
    }


    //入库2天内未领用工时订单
    public String ExpoExcel155() throws IOException {

        this.bb.writeLog("执行ExpoExcel155");

        JSONObject result = new JSONObject();

        //excel行指针
        int rowIndex = 1;

        //excel列指针
        int columnIndex;

        String CJ = "";
        String RKRQ = "";
        String GSLYRQ = "";
        String LYSL = "";
        String LRGSJCPRKRQ = "";
        String BZTS = "";
        String DDH = "";
        String CZYDM = "";
        String WLBM = "";
        String WLMC = "";
        String WLMC_GBK = "";
        String DDSL = "";
        String JSSL = "";


        //调用外部数据源
        RecordSetDataSource rs = new RecordSetDataSource("FourthShift");

        String sql = "SELECT [GLOS_DES]  车间\n" +
                "      ,a.[TransactionDate] 入库日期\n" +
                "\t    ,b.[TransactionDate] 工时领用日期\n" +
                "\t    ,b.[IssuedQuantity] 领用数量\n" +
                "\t    ,(DATEDIFF(dd,a.[TransactionDate],b.[TransactionDate])) as 领入工时距成品入库天数\n" +
                "\t    ,2 标准天数\n" +
                "\t    ,a.[MONumber] 订单号\n" +
                "      ,[Planner] 操作员代码\n" +
                "      ,a.[ItemNumber] 物料编码\n" +
                "      ,[ItemDescription] 物料名称\n" +
                "      ,[ItemOrderedQuantity] 订单数量\n" +
                "      ,[ReceiptQuantity] 接收数量 \n" +
                "  FROM [FSDB].[dbo].[HR_LastMonthMorv] a\n" +
                "  left join [FSDB].[dbo].HR_LastMonthPick b ON a.[MONumber]=b.[OrderNumber] and a.[MOLineNumber]=b.[LineNumber]\n" +
                "  where (DATEDIFF(dd,a.[TransactionDate],b.[TransactionDate]))>2  \n" +
                "  order by [GLOS_DES],a.[MONumber]";

        rs.executeSql(sql);

        if (!rs.next()) {
            this.bb.writeLog("ApachePOI读取数据失败 - 入库2天内未领用工时订单");
        }
        else{
            this.bb.writeLog("ApachePOI读取数据成功 - 入库2天内未领用工时订单");
        }

        //在内存中创建一个Excel文件对象
        XSSFWorkbook excel = new XSSFWorkbook();

        //创建Sheet页
        XSSFSheet sheet = excel.createSheet("sheet1");

        //在Sheet页中创建行，0表示第1行
        XSSFRow row1 = sheet.createRow(0);

        //创建单元格并在单元格中设置值，单元格编号也是从0开始，1表示第2个单元格
        row1.createCell(0).setCellValue("车间");
        row1.createCell(1).setCellValue("入库日期");
        row1.createCell(2).setCellValue("工时领用日期");
        row1.createCell(3).setCellValue("领用数量");
        row1.createCell(4).setCellValue("领入工时距成品入库天数");
        row1.createCell(5).setCellValue("标准天数");
        row1.createCell(6).setCellValue("订单号");
        row1.createCell(7).setCellValue("操作员代码");
        row1.createCell(8).setCellValue("物料编码");
        row1.createCell(9).setCellValue("物料名称");
        row1.createCell(10).setCellValue("订单数量");
        row1.createCell(11).setCellValue("接收数量 ");

        while(rs.next())
        {
            XSSFRow row = sheet.createRow(rowIndex);
//
            CJ = rs.getString("车间");
            RKRQ = rs.getString("入库日期");
            GSLYRQ = rs.getString("工时领用日期");
            LYSL = rs.getString("领用数量");
            LRGSJCPRKRQ = rs.getString("领入工时距成品入库天数");
            BZTS = rs.getString("标准天数");
            DDH = rs.getString("订单号");
            CZYDM = rs.getString("操作员代码");
            WLBM = rs.getString("物料编码");

            WLMC = rs.getString("物料名称");
            WLMC_GBK = ISO2GBK(WLMC);

            DDSL = rs.getString("订单数量");
            JSSL = rs.getString("接收数量");

            row.createCell(0).setCellValue(CJ);
            row.createCell(1).setCellValue(RKRQ);
            row.createCell(2).setCellValue(GSLYRQ);
            row.createCell(3).setCellValue(LYSL);
            row.createCell(4).setCellValue(LRGSJCPRKRQ);
            row.createCell(5).setCellValue(BZTS);
            row.createCell(6).setCellValue(DDH);
            row.createCell(7).setCellValue(CZYDM);
            row.createCell(8).setCellValue(WLBM);
            row.createCell(9).setCellValue(WLMC_GBK);
            row.createCell(10).setCellValue(DDSL);
            row.createCell(11).setCellValue(JSSL);

            rowIndex++;
        }


//        String filename = "O:\\WEAVER\\ecology\\filesystem\\cutsomTempFile\\RKLTNWLYGSDD.xlsx";
        String filename = "D:\\weaver\\ecology\\filesystem\\cutsomTempFile\\RKLTNWLYGSDD.xlsx";


        FileOutputStream out = new FileOutputStream(new File(filename));
        //通过输出流将内存中的Excel文件写入到磁盘上
        excel.write(out);

        //关闭资源
        out.flush();
        out.close();
        excel.close();

        this.bb.writeLog("ApachePOI执行成功 - 入库2天内未领用工时订单");


        result.put("code", 0);
        result.put("path", "\\filesystem\\cutsomTempFile\\RKLTNWLYGSDD.xlsx");

        return result.toString();
    }

    //入库6天未关闭订单
    public String ExpoExcel156() throws IOException {

        this.bb.writeLog("执行ExpoExcel156");

        JSONObject result = new JSONObject();

        //excel行指针
        int rowIndex = 1;

        //excel列指针
        int columnIndex;

        String CJ = "";
        String RKRQ = "";
        String DDGBRQ = "";
        String DDGBJCPRKTS = "";
        String BZTS = "";
        String DDH = "";
        String CCYDM = "";
        String WLBM = "";
        String WLMC = "";
        String WLMC_GBK = "";
        String DDSL = "";
        String JSSL = "";

        //调用外部数据源
        RecordSetDataSource rs = new RecordSetDataSource("FourthShift");

        String sql = "SELECT [GLOS_DES]  车间\n" +
                "      ,a.[TransactionDate] 入库日期\n" +
                "\t  ,b.[TransactionDate] 订单关闭日期\n" +
                "\t  ,(DATEDIFF(dd,a.[TransactionDate],b.[TransactionDate])) as 订单关闭距成品入库天数\n" +
                "\t  ,6 标准天数\n" +
                "\t   ,a.[MONumber] 订单号\n" +
                "      ,[Planner] 操作员代码\n" +
                "      ,a.[ItemNumber] 物料编码\n" +
                "      ,[ItemDescription] 物料名称\n" +
                "      ,[ItemOrderedQuantity] 订单数量\n" +
                "      ,[ReceiptQuantity] 接收数量 \n" +
                "  FROM [FSDB].[dbo].[HR_LastMonthMorv] a\n" +
                "  left join [FSDB].[dbo].[HR_LastMOClose] b ON a.[MONumber]=b.[MONumber] and a.[MOLineNumber]=b.[MOLineNumber]\n" +
                "  where (DATEDIFF(dd,a.[TransactionDate],b.[TransactionDate]))>6  \n" +
                "  order by [GLOS_DES],a.[MONumber]";

        rs.executeSql(sql);

        if (!rs.next()) {
            this.bb.writeLog("ApachePOI读取数据失败 - 入库6天未关闭订单");
        }
        else{
            this.bb.writeLog("ApachePOI读取数据成功 - 入库6天未关闭订单");
        }

        //在内存中创建一个Excel文件对象
        XSSFWorkbook excel = new XSSFWorkbook();

        //创建Sheet页
        XSSFSheet sheet = excel.createSheet("sheet1");

        //在Sheet页中创建行，0表示第1行
        XSSFRow row1 = sheet.createRow(0);

        //创建单元格并在单元格中设置值，单元格编号也是从0开始，1表示第2个单元格
        row1.createCell(0).setCellValue("车间");
        row1.createCell(1).setCellValue("入库日期");
        row1.createCell(2).setCellValue("订单关闭日期");
        row1.createCell(3).setCellValue("订单关闭距成品入库天数");
        row1.createCell(4).setCellValue("标准天数");
        row1.createCell(5).setCellValue("订单号");
        row1.createCell(6).setCellValue("操作员代码");
        row1.createCell(7).setCellValue("物料编码");
        row1.createCell(8).setCellValue("物料名称");
        row1.createCell(9).setCellValue("订单数量");
        row1.createCell(10).setCellValue("接收数量");

        while(rs.next())
        {
            XSSFRow row = sheet.createRow(rowIndex);
//
            CJ = rs.getString("车间");
            RKRQ = rs.getString("入库日期");
            DDGBRQ = rs.getString("订单关闭日期");
            DDGBJCPRKTS = rs.getString("订单关闭距成品入库天数");
            BZTS = rs.getString("标准天数");
            DDH = rs.getString("订单号");
            CCYDM = rs.getString("操作员代码");
            WLBM = rs.getString("物料编码");
            WLMC = rs.getString("物料名称");
            WLMC_GBK = ISO2GBK(WLMC);
            DDSL = rs.getString("订单数量");
            JSSL = rs.getString("接收数量");

            row.createCell(0).setCellValue(CJ);
            row.createCell(1).setCellValue(RKRQ);
            row.createCell(2).setCellValue(DDGBRQ);
            row.createCell(3).setCellValue(DDGBJCPRKTS);
            row.createCell(4).setCellValue(BZTS);
            row.createCell(5).setCellValue(DDH);
            row.createCell(6).setCellValue(CCYDM);
            row.createCell(7).setCellValue(WLBM);
            row.createCell(8).setCellValue(WLMC_GBK);
            row.createCell(9).setCellValue(DDSL);
            row.createCell(10).setCellValue(JSSL);

            rowIndex++;
        }


//        String filename = "O:\\WEAVER\\ecology\\filesystem\\cutsomTempFile\\RKLTWGBDD.xlsx";
        String filename = "D:\\weaver\\ecology\\filesystem\\cutsomTempFile\\RKLTWGBDD.xlsx";


        FileOutputStream out = new FileOutputStream(new File(filename));
        //通过输出流将内存中的Excel文件写入到磁盘上
        excel.write(out);

        //关闭资源
        out.flush();
        out.close();
        excel.close();

        this.bb.writeLog("ApachePOI执行成功 - 入库6天未关闭订单");

        result.put("code", 0);
        result.put("path", "\\filesystem\\cutsomTempFile\\RKLTWGBDD.xlsx");

        return result.toString();
    }


    //修改过工时的订单
    public String ExpoExcel157() throws IOException {

        this.bb.writeLog("执行ExpoExcel157");

        JSONObject result = new JSONObject();

        //excel行指针
        int rowIndex = 1;

        //excel列指针
        int columnIndex;

        String CJ = "";
        String DDH = "";
        String FXHM = "";
        String WLMS = "";
        String WLMS_GBK = "";
        String ZXHM = "";
        String ZXMS = "";
        String ZXMS_GBK = "";
        String LYCS = "";

        //调用外部数据源
        RecordSetDataSource rs = new RecordSetDataSource("WeaverView");

        String sql = "select * from HR_修改过工时的订单 where left([子项号码],2)='WC'";

        rs.executeSql(sql);

        if (!rs.next()) {
            this.bb.writeLog("ApachePOI读取数据失败 - 修改过工时的订单");
        }
        else{
            this.bb.writeLog("ApachePOI读取数据成功 - 修改过工时的订单");
        }

        //在内存中创建一个Excel文件对象
        XSSFWorkbook excel = new XSSFWorkbook();

        //创建Sheet页
        XSSFSheet sheet = excel.createSheet("sheet1");

        //在Sheet页中创建行，0表示第1行
        XSSFRow row1 = sheet.createRow(0);

        //创建单元格并在单元格中设置值，单元格编号也是从0开始，1表示第2个单元格
        row1.createCell(0).setCellValue("车间");
        row1.createCell(1).setCellValue("订单号");
        row1.createCell(2).setCellValue("父项号码");
        row1.createCell(3).setCellValue("物料描述");
        row1.createCell(4).setCellValue("子项号码");
        row1.createCell(5).setCellValue("子项描述");
        row1.createCell(6).setCellValue("领用次数");

        while(rs.next())
        {
            XSSFRow row = sheet.createRow(rowIndex);
//
            CJ = rs.getString("车间");
            DDH = rs.getString("订单号");
            FXHM = rs.getString("父项号码");
            WLMS = rs.getString("MatName");
            WLMS_GBK = ISO2GBK(WLMS);
            ZXHM = rs.getString("子项号码");
            ZXMS = rs.getString("ChildMatName");
            ZXMS_GBK = ISO2GBK(ZXMS);
            LYCS = rs.getString("领用次数");

            row.createCell(0).setCellValue(CJ);
            row.createCell(1).setCellValue(DDH);
            row.createCell(2).setCellValue(FXHM);
            row.createCell(3).setCellValue(WLMS_GBK);
            row.createCell(4).setCellValue(ZXHM);
            row.createCell(5).setCellValue(ZXMS_GBK);
            row.createCell(6).setCellValue(LYCS);

            rowIndex++;
        }

//        String filename = "O:\\WEAVER\\ecology\\filesystem\\cutsomTempFile\\XGGGSDDD.xlsx";
        String filename = "D:\\weaver\\ecology\\filesystem\\cutsomTempFile\\XGGGSDDD.xlsx";

        FileOutputStream out = new FileOutputStream(new File(filename));
        //通过输出流将内存中的Excel文件写入到磁盘上
        excel.write(out);

        //关闭资源
        out.flush();
        out.close();
        excel.close();

        this.bb.writeLog("ApachePOI执行成功 - 修改过工时的订单");

        result.put("code", 0);
        result.put("path", "\\filesystem\\cutsomTempFile\\XGGGSDDD.xlsx");

        return result.toString();
    }


    //修改过领料的订单
    public String ExpoExcel158() throws IOException {

        this.bb.writeLog("执行ExpoExcel158");

        JSONObject result = new JSONObject();

        //excel行指针
        int rowIndex = 1;

        //excel列指针
        int columnIndex;

        String CJ = "";
        String DDH = "";
        String FXHM = "";
        String WLMS = "";
        String WLMS_GBK = "";
        String ZXHM = "";
        String ZXMS = "";
        String ZXMS_GBK = "";
        String LYCS = "";

        //调用外部数据源
        RecordSetDataSource rs = new RecordSetDataSource("WeaverView");

        String sql = "select * from HR_修改过领料的订单 where left([子项号码],2)='WC'";

        rs.executeSql(sql);

        if (!rs.next()) {
            this.bb.writeLog("ApachePOI读取数据失败 - 修改过领料订单");
        }
        else{
            this.bb.writeLog("ApachePOI读取数据成功 - 修改过领料订单");
        }

        //在内存中创建一个Excel文件对象
        XSSFWorkbook excel = new XSSFWorkbook();

        //创建Sheet页
        XSSFSheet sheet = excel.createSheet("sheet1");

        //在Sheet页中创建行，0表示第1行
        XSSFRow row1 = sheet.createRow(0);

        //创建单元格并在单元格中设置值，单元格编号也是从0开始，1表示第2个单元格
        row1.createCell(0).setCellValue("车间");
        row1.createCell(1).setCellValue("订单号");
        row1.createCell(2).setCellValue("父项号码");
        row1.createCell(3).setCellValue("物料描述");
        row1.createCell(4).setCellValue("子项号码");
        row1.createCell(5).setCellValue("子项描述");
        row1.createCell(6).setCellValue("领用次数");

        while(rs.next())
        {
            XSSFRow row = sheet.createRow(rowIndex);
//
            CJ = rs.getString("车间");
            DDH = rs.getString("订单号");
            FXHM = rs.getString("父项号码");
            WLMS = rs.getString("MatName");
            WLMS_GBK = ISO2GBK(WLMS);
            ZXHM = rs.getString("子项号码");
            ZXMS = rs.getString("ChildMatName");
            ZXMS_GBK = ISO2GBK(ZXMS);
            LYCS = rs.getString("领用次数");

            row.createCell(0).setCellValue(CJ);
            row.createCell(1).setCellValue(DDH);
            row.createCell(2).setCellValue(FXHM);
            row.createCell(3).setCellValue(WLMS_GBK);
            row.createCell(4).setCellValue(ZXHM);
            row.createCell(5).setCellValue(ZXMS_GBK);
            row.createCell(6).setCellValue(LYCS);

            rowIndex++;
        }


//        String filename = "O:\\WEAVER\\ecology\\filesystem\\cutsomTempFile\\XGGLYDDD.xlsx";
        String filename = "D:\\weaver\\ecology\\filesystem\\cutsomTempFile\\XGGLYDDD.xlsx";


        FileOutputStream out = new FileOutputStream(new File(filename));
        //通过输出流将内存中的Excel文件写入到磁盘上
        excel.write(out);

        //关闭资源
        out.flush();
        out.close();
        excel.close();

        this.bb.writeLog("ApachePOI执行成功 - 修改过领料订单");


        result.put("code", 0);
        result.put("path", "\\filesystem\\cutsomTempFile\\XGGLYDDD.xlsx");

        return result.toString();
    }


    public static String ISO2GBK(String str) throws UnsupportedEncodingException {
        String msg = new String(str.getBytes("ISO-8859-1"), "windows-949");

        return new String(msg.getBytes("windows-949"), "GBK");
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String msg = "102³µ¼ä×¼±¸Ê±¼ä";

//        String msg0 = new String(msg.getBytes("ISO-8859-1"), "windows-949");
//
//        String msg3 = new String(msg0.getBytes("windows-949"), "GBK");

        System.out.println(ISO2GBK(msg));
    }
}