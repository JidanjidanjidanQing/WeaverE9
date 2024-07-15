
package weaver.interfaces.workflow.action;

import weaver.conn.RecordSetTrans;
import weaver.general.BaseBean;
import weaver.soa.workflow.request.RequestInfo;


/**
 * 汇总业务员提交的物料需求数据
 * 总经理审批前执行
 *
 */

public class CollectDemandOnProductBeforeBoss extends BaseBean implements Action {
    /**
     * 流程路径节点后选择aciton后,会在节点提交后执行此方法。
     */
    public String execute(RequestInfo request) {
        writeLog("总经理审批前");

        // 请求id
        String requestId = request.getRequestid();
        writeLog("请求id：" + requestId);

        // 主表名称
        String tablename = request.getRequestManager().getBillTableName();
        writeLog("主表名称：" + tablename);

        // 事务级sql
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
            rs.execute("INSERT INTO uf_CollDmdOnProd (year, month, matid, fb, amount)" +
                    "SELECT year, month, matid, fb, SUM(amount) AS amount " +
                    "FROM uf_CollDmdOnProd GROUP BY year, month, matid, fb");
            rs.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return SUCCESS;
    }

}
