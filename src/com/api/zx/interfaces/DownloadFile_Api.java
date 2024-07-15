//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.api.zx.interfaces;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.icbc.api.response.epassipchainrecordResponseV1;
import net.sf.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.docs.webservicesformsg.AESCoder;
import weaver.general.BaseBean;

@Path("/zxDownloadFile")
public class DownloadFile_Api {
    BaseBean bb = new BaseBean();

    public DownloadFile_Api() {
    }

    @POST
    @Path("/downloadFile")
    @Produces({"text/plain"})
    public String downloadFile2(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        JSONObject result = new JSONObject();
        RecordSet rs = new RecordSet();
        String ids = request.getParameter("ids");
        String tableName = request.getParameter("tableName");
        String zjb = "uf_zjb";
        InputStream imagefile = null;
        ZipInputStream zin = null;

        try {
            String sql1 = "select * from " + zjb + " where tzbm= '" + tableName + "'";
            RecordSet rs1 = new RecordSet();
            rs1.execute(sql1);
            String xzwjszzd = "";
            String gllczd = "";
            if (!rs1.next()) {
                result.put("code", 1);
                result.put("message", "未在维护表中查询到该数据表,请联系管理员");
                return result.toString();
            }

            xzwjszzd = rs1.getString("xzwjszzd");
            this.bb.writeLog("当前台账下载文件所属字段:" + xzwjszzd);
            String[] fjzdArr = xzwjszzd.split(",");
            int fjzdArrLength = fjzdArr.length;
            this.bb.writeLog("当前台账关联流程字段数量:" + fjzdArrLength);
            String[] billIds = ids.split(",");
            Map map = new HashMap();
            String[] var18 = billIds;
            int var19 = billIds.length;

            for(int var20 = 0; var20 < var19; ++var20) {
                String billId = var18[var20];
                String sql = "select " + xzwjszzd + " from " + tableName + " where id= " + billId;
                this.bb.writeLog("查询当前台账信息的sql: " + sql);
                rs.execute(sql);
                if (rs.next()) {
                    String fj = "";

                    for(int i = 0; i < fjzdArrLength; ++i) {
                        if (!"".equals(rs.getString(fjzdArr[i]))) {
                            fj = fj + "," + rs.getString(fjzdArr[i]);
                        }
                    }

                    if ("".equals(fj)) {
                        this.bb.writeLog("数据id为：" + billId + "的数据没有附件");
                    } else {
                        fj = fj.substring(1);
                        this.bb.writeLog("查询到的附件id共有: " + fj);
                        String querySql = "select * from docimagefile dif left join imagefile ii on ii.imagefileid=dif.imagefileid where dif.docid in (" + fj + ")";
                        this.bb.writeLog("查询附件相关信息的sql: " + querySql);
                        RecordSet queryRs = new RecordSet();
                        queryRs.execute(querySql);

                        while(queryRs.next()) {
                            String imagefilename = queryRs.getString("imagefilename");
                            String iszip = queryRs.getString("iszip");
                            String isaesencrypt = queryRs.getString("isaesencrypt");
                            String aescode = queryRs.getString("aescode");
                            File filerealpath = new File(queryRs.getString("filerealpath"));
                            if (!"".equals(filerealpath)) {
                                if (iszip.equals("1")) {
                                    zin = new ZipInputStream(new FileInputStream(filerealpath));
                                    if (zin.getNextEntry() != null) {
                                        imagefile = new BufferedInputStream(zin);
                                    }
                                } else {
                                    imagefile = new BufferedInputStream(new FileInputStream(filerealpath));
                                }

                                if (isaesencrypt.equals("1")) {
                                    imagefile = AESCoder.decrypt((InputStream)imagefile, aescode);
                                }

                                map.put(imagefilename, imagefile);
                            }
                        }

                        Iterator<Map.Entry<Integer, Integer>> it = map.entrySet().iterator();

                        while(it.hasNext()) {
                            Map.Entry<Integer, Integer> entry = (Map.Entry)it.next();
                            this.bb.writeLog("key = " + entry.getKey() + ", value = " + entry.getValue());
                        }
                    }
                }
            }

            File zipfile = new File("D:\\weaver\\ecology\\filesystem\\downloadFile.zip");
            zipFiles(map, zipfile);
            this.bb.writeLog("压缩完成:" + zipfile);
            result.put("code", 0);
            result.put("path", "\\filesystem\\downloadFile.zip");
        } catch (Exception var31) {
            var31.printStackTrace();
            this.bb.writeLog("e:" + var31);
            result.put("code", 1);
            result.put("message", "下载附件异常，请联系管理员");
        }

        return result.toString();
    }

    public static void main(String[] args) {

        String filerealpath = "O:\\泛微开发\\e9\\测试.txt";

        try {
            InputStream imagefile = new BufferedInputStream(new FileInputStream(filerealpath));
            Map map = new HashMap();
            map.put("文件1.txt", imagefile);
            File zipfile = new File("O:\\泛微开发\\e9\\downloadFile.zip");
            zipFiles(map, zipfile);
        } catch (FileNotFoundException var5) {
            var5.printStackTrace();
        }

    }

    public static void zipFiles(Map srcfile, File zipfile) {
        byte[] buf = new byte[1024];

        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
            Iterator iterator = srcfile.entrySet().iterator();
            Map.Entry entry = null;
            String name = "";

            while(iterator.hasNext()) {
                entry = (Map.Entry)iterator.next();
                name = (String)entry.getKey();
                InputStream content = (InputStream)entry.getValue();
                out.putNextEntry(new ZipEntry(name));

                int len;
                while((len = content.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                out.closeEntry();
                content.close();
            }

            out.close();
        } catch (Exception var9) {
            var9.printStackTrace();
        }

    }
}
