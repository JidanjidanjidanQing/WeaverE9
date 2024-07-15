package weaver.interfaces.workflow.action;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.soa.workflow.request.RequestInfo;


/**
 * 汇总业务员提交的物料需求数据
 * 区域
 *
 */

public class CollectDemandOnProductFin extends BaseBean implements Action {
    /**
     * 流程路径节点后选择aciton后,会在节点提交后执行此方法。
     */
    public String execute(RequestInfo request) {
        writeLog("Fin节点");
        // 请求id
        String requestId = request.getRequestid();
        writeLog("请求id：" + requestId);

        // 主表名称
        String tablename = request.getRequestManager().getBillTableName();
        writeLog("主表名称：" + tablename);

        // 查询主表信息
        RecordSet rs = new RecordSet();
        rs.execute("select * from " + tablename + " where requestid =  " + requestId);
        rs.next();

        // 主表id
        String id = rs.getString("id");
        writeLog("主表id：" + id);

        // 业务员
        //Integer ywy = rs.getInt("tbr");
//        String ywy = rs.getString("tbr");
//        writeLog("申请人id：" + Integer.parseInt(ywy));

        // 业务员所属分部
//        String fb = rs.getString("fb");
//        writeLog("申请人分部：" + Integer.parseInt(fb));

//        //业务员直属部门
//        String bm = rs.getString("bm");
//        writeLog("申请人部门：" + Integer.parseInt(bm));
//
//        //业务员所属各级部门（自底向上）
//        String bm1 = rs.getString("bm1");
//        writeLog("申请人部门1：" + Integer.parseInt(bm1));
//
//        String bm2 ="";
//        bm2 = rs.getString("bm2");
//        writeLog("申请人部门2：" + Integer.parseInt(bm2));

        //数据归属年月
        String year=rs.getString("year");
        String month=rs.getString("month");
        writeLog("数据归属年份：" + Integer.parseInt(year));
        writeLog("数据归属月份：" + Integer.parseInt(month));


        // 明细表名称 = 主表名称 + "_dt1"
        String tablename_dt = tablename + "_dt1";
        writeLog("明细表名称：" + tablename_dt);

        // 根据主表的id查询明细表数据
        rs.execute("select * from " + tablename_dt + " where mainid =  " + id);
        RecordSet rs1  = new RecordSet();
        RecordSet rs2  = new RecordSet();

        //String SQL="";
        String SQL="BEGIN TRANSACTION DECLARE @tran_error int SET @tran_error=0 BEGIN TRY ";

        String BelongTo="";
        String amount;
        String wlbmid;
        String fb;

        while (rs.next()) {

            //数量
            amount = rs.getString("Amount");
            writeLog("数量：" + amount);

            //物料编码ID
            wlbmid=rs.getString("wlms");
            writeLog("物料编码id：" + Integer.parseInt(wlbmid));

            BelongTo="SELECT * FROM uf_FinishedProduct WHERE ID="+wlbmid;
            writeLog(BelongTo);
            rs2.execute(BelongTo);
            rs2.next();
            fb=rs2.getString("fb");
            writeLog("数据归属事业部：" + fb);

            SQL+="IF NOT EXISTS (select * from uf_CollDmdOnProd where bm is null and year='"+year+"' and month="+month+" and fb="+fb+" and MatID="+wlbmid+") " +
                    "BEGIN " +
                        "INSERT into uf_CollDmdOnProd (Year,Month,fb,MatID,Amount,formmodeid,modedatacreatedate)" +
                        "values('"+year+"','"+month+"','"+fb+"','"+wlbmid+"','"+amount+"','"+"127"+"',CAST(GETDATE() AS DATE)) " +
                    "END " +
                    "ELSE " +
                    "BEGIN " +
                        "UPDATE uf_CollDmdOnProd SET Amount=Amount+"+amount+
                        " WHERE year='"+year+"' and month="+month+" and fb="+fb+" and MatID="+wlbmid+
                    " END ";



        }

        SQL+="END TRY BEGIN CATCH set @tran_error=@tran_error+1 END CATCH ";
        SQL+="IF(@tran_error>0) BEGIN ROLLBACK END ";
        SQL+="ELSE BEGIN COMMIT END ";

        writeLog(SQL);
        if(rs1.execute(SQL)==true)
        {
            writeLog("销售计划填报流程正常-Fin节点 requestid:"+requestId);
            //writeLog(SQL);
        }
        else
        {
            writeLog("销售计划填报流程出错-Fin节点 requestid:"+requestId);
        }

        return SUCCESS;
    }

}
