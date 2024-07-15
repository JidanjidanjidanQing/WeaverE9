
package weaver.interfaces.workflow.action;

import weaver.conn.RecordSetTrans;
import weaver.general.BaseBean;
import weaver.soa.workflow.request.RequestInfo;


/**
 * 汇总业务员提交的物料需求数据
 * 大区
 *
 */

public class ColletcDemandOnProductSup6 extends BaseBean implements Action {
    /**
     * 流程路径节点后选择aciton后,会在节点提交后执行此方法。
     */
    public String execute(RequestInfo request) {
        writeLog("Bot节点0420");

        // 请求id
        String requestId = request.getRequestid();
        writeLog("请求id：" + requestId);

        // 主表名称
        String tablename = request.getRequestManager().getBillTableName();
        writeLog("主表名称：" + tablename);

        // 查询主表信息
        RecordSetTrans rs = new RecordSetTrans();
        try {
            rs.execute("select * from " + tablename + " where requestid =  " + requestId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        rs.next();

        // 当前操作者
        String bmsql = rs.getString("bmsql");
        writeLog("当前操作者部门：" + bmsql);

        // 主表id
        String id = rs.getString("id");
        writeLog("主表id：" + id);

        // 流程所属分部
        String fb = rs.getString("fb");
        writeLog("申请人分部：" + Integer.parseInt(fb));


        //数据归属年月
        String year=rs.getString("year");
        String month=rs.getString("month");
        writeLog("数据归属年份：" + Integer.parseInt(year));
        writeLog("数据归属月份：" + Integer.parseInt(month));


        // 明细表名称 = 主表名称 + "_dt1"
        String tablename_dt = tablename + "_dt1";
        writeLog("明细表名称：" + tablename_dt);

        // 根据主表的id查询明细表数据
        try {
            rs.execute("select * from " + tablename_dt + " where mainid = " + id + " and bmtop = " + bmsql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String SQL="";

        while (rs.next()) {

            String detailid = rs.getString("id");
            writeLog("明细detailid：" + detailid);

            // 物料编码
            String wlbm = rs.getString("wlbm");
            writeLog("物料编码：" + wlbm);

            //数量

            String amount = "0";
            amount = rs.getString("Amount");
            writeLog("数量：" + amount);

            //物料编码ID
            String wlbmid=rs.getString("wlms");
            writeLog("物料编码id：" + Integer.parseInt(wlbmid));

            SQL+="IF NOT EXISTS (select * from uf_CollDmdOnProd where year='"+year+"' and month="+month+" and fb="+fb+" and MatID="+wlbmid+") " +
                    "BEGIN " +
                    "INSERT into uf_CollDmdOnProd (Year,Month,fb,wlbm,MatID,Amount,formmodeid,modedatacreatedate)" +
                    "values('"+year+"','"+month+"','"+fb+"','"+wlbm+"','"+wlbmid+"','"+amount+"','"+"127"+"',CAST(GETDATE() AS DATE)) " +
                    "END " +
                    "ELSE " +
                    "BEGIN " +
                    "UPDATE uf_CollDmdOnProd SET Amount=Amount+"+amount+
                    " WHERE year='"+year+"' and month="+month+" and fb="+fb+" and MatID="+wlbmid+
                    " END ";
        }

        //需要完善泛微事务级sql
        try {
            rs.execute(SQL);
            writeLog(SQL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return SUCCESS;
    }

}
