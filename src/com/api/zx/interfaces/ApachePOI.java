//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.api.zx.interfaces;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;

@Path("/zxDownloadFile")
public class ApachePOI {
    BaseBean bb = new BaseBean();

    public ApachePOI() {
    }

    @POST
    @Path("/expoExcelOrigin")
    @Produces({"text/plain"})
    public void write(@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
        int rowIndex = 1;
        String CJ = "";
        String RKRQ = "";
        String GSLYRQ = "";
        String LYSL = "";
        String LRGSJCPRKRQ = "";
        String BZTS = "";
        String DDH = "";
        String CZYDM = "";
        String WLBM = "";
        String MLMC = "";
        String DDSL = "";
        String JSSL = "";
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(formatter);
        RecordSet rs = new RecordSet();
        String sql = "select * from HR_入库2天内未领用工时订单 where id=1";
        rs.execute(sql);
        if (!rs.next()) {
            System.out.println("读取数据失败:");
        } else {
            System.out.println("读取数据成功:");
        }

        String workshop = rs.getString("requestid");
        System.out.println(workshop);
        XSSFWorkbook excel = new XSSFWorkbook();
        XSSFSheet sheet = excel.createSheet("sheet1");
        XSSFRow row1 = sheet.createRow(0);
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

        XSSFRow row2;
        while(!rs.next()) {
            row2 = sheet.createRow(rowIndex);
            CJ = rs.getString("车间");
            RKRQ = rs.getString("入库日期");
            GSLYRQ = rs.getString("工时领用日期");
            LYSL = rs.getString("领用数量");
            LRGSJCPRKRQ = rs.getString("领入工时距成品入库天数");
            BZTS = rs.getString("标准天数");
            DDH = rs.getString("订单号");
            CZYDM = rs.getString("操作员代码");
            WLBM = rs.getString("物料编码");
            MLMC = rs.getString("物料名称");
            DDSL = rs.getString("订单数量");
            JSSL = rs.getString("接收数量");
            row2.createCell(0).setCellValue(CJ);
            row2.createCell(1).setCellValue(RKRQ);
            row2.createCell(2).setCellValue(GSLYRQ);
            row2.createCell(3).setCellValue(LYSL);
            row2.createCell(4).setCellValue(LRGSJCPRKRQ);
            row2.createCell(5).setCellValue(BZTS);
            row2.createCell(6).setCellValue(DDH);
            row2.createCell(7).setCellValue(CZYDM);
            row2.createCell(8).setCellValue(WLBM);
            row2.createCell(9).setCellValue(MLMC);
            row2.createCell(10).setCellValue(DDSL);
            row2.createCell(11).setCellValue(JSSL);
            ++rowIndex;
        }

        row2 = sheet.createRow(1);
        row2.createCell(1).setCellValue("张三");
        row2.createCell(2).setCellValue("北京");
        XSSFRow row3 = sheet.createRow(2);
        row3.createCell(1).setCellValue("李四");
        row3.createCell(2).setCellValue("上海");
        FileOutputStream out = new FileOutputStream(new File("入库2天内未领用工时订单-" + formattedDate + ".xlsx"));
        excel.write(out);
        out.flush();
        out.close();
        excel.close();
        this.bb.writeLog("ApachePOI执行成功");
    }
}
