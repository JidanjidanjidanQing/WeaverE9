package weaver.interfaces.workflow.action;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 在线自定义action接口
 */
public class UpdateReportRcd extends BaseBean implements Action {
    /**
     * 流程路径节点后选择aciton后,会在节点提交后执行此方法。
     */
    public String execute(RequestInfo request) {
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
        String ywy = rs.getString("tbr");
        writeLog("申请人id：" + Integer.parseInt(ywy));

        // 明细表名称 = 主表名称 + "_dt1"
        String tablename_dt = tablename + "_dt1";
        writeLog("明细表名称：" + tablename_dt);

        // 根据主表的id查询明细表数据
        rs.execute("select * from " + tablename_dt + " where mainid =  " + id);
        RecordSet rs1  = new RecordSet();
        while (rs.next()) {
            // 物料编码
            String wlbm = rs.getString("wlbm");
            writeLog("物料编码：" + wlbm);

            //物料编码ID
            String wlbmid=rs.getString("wlms");
            writeLog("物料编码id：" + Integer.parseInt(wlbmid));


            String sql="IF NOT EXISTS (select * from uf_SubmittedQty where wlbm='"+wlbm+"' and ywy="+ywy+") " +
                    "INSERT into uf_SubmittedQty (ywy,wlbm,wlbmid,formmodeid,modedatacreater,modedatacreatedate)" +
                    "values('"+ywy+"','"+wlbm+"','"+wlbmid+"','"+"124"+"','"+ywy+"',CAST(GETDATE() AS DATE))";
            writeLog(sql);
            rs1.execute(sql);

        }
        return SUCCESS;
    }

}
