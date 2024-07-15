
package weaver.formmode.customjavacode.modeexpand;

import weaver.conn.RecordSet;
import weaver.formmode.customjavacode.AbstractModeExpandJavaCodeNew;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.soa.workflow.request.RequestInfo;

import java.util.HashMap;
import java.util.Map;

/**
 *用于重新计算模块139的 “今年累计数据” 部分
 *
 * */
public class ReCalAccuData139 extends AbstractModeExpandJavaCodeNew {

    BaseBean bb = new BaseBean();

    /**
     * 执行模块扩展动作
     * @param param
     *  param包含(但不限于)以下数据
     *  user 当前用户
     *  importtype 导入方式(仅在批量导入的接口动作会传输) 1 追加，2覆盖,3更新，获取方式(int)param.get("importtype")
     *  导入链接中拼接的特殊参数(仅在批量导入的接口动作会传输)，比如a=1，可通过param.get("a")获取参数值
     *  页面链接拼接的参数，比如b=2,可以通过param.get("b")来获取参数
     * @return
     */
    public Map<String, String> doModeExpand(Map<String, Object> param) {

        Map<String, String> result = new HashMap<String, String>();

        try {
            User user = (User)param.get("user");

            //CacheFactory cache = CacheFactory.getInstance();
            //String[] tablenames= {"uf_FN_MainOptData"};

            int billid = -1;//数据id
            int modeid = -1;//模块id

            RequestInfo requestInfo = (RequestInfo)param.get("RequestInfo");

            if(requestInfo!=null){

                //获取数据id
                billid = Util.getIntValue(requestInfo.getRequestid());

                //获取模块id
                modeid = Util.getIntValue(requestInfo.getWorkflowid());

                //this.bb.writeLog("-------testbillid:"+billid);
                //this.bb.writeLog("-------testmodeid:"+modeid);

                if(billid>0&&modeid>0){
                    //------请在下面编写业务逻辑代码------
                    RecordSet rs = new RecordSet();

                    //String SQL1="select nf,yf,JLR,LRZE from uf_FN_MainProfit where id="+billid;
                    String SQL1="select * from uf_FN_MainOptData where id="+billid;

                    this.bb.writeLog(SQL1);

                    rs.execute(SQL1);
                    rs.next();

                    String NF=rs.getString("nf");
                    String GSLB=rs.getString("gslb");

                    //this.bb.writeLog("-------NF:"+NF);
                    //this.bb.writeLog("-------GSLB:"+GSLB);

                    String SQL2="DECLARE @nf int = "+NF;
                    String SQL3="DECLARE @gslb int = "+GSLB;
                    String SQL4="DECLARE @yf int = 1\n" +
                            "WHILE @yf <= 12\n" +
                            "BEGIN\n" +
                            "    update uf_FN_MainOptData set \n" +
                            "\t\tAccuXSSR=(select sum(XSSR) from uf_FN_MainOptData where YEAR(modedatacreatedate)=@nf and MONTH(modedatacreatedate)<=@yf and gslb=@gslb and formmodeid=139 group by YEAR(modedatacreatedate),gslb),\n" +
                            "\t\tAccuXSCB=(select sum(XSCB) from uf_FN_MainOptData where YEAR(modedatacreatedate)=@nf and MONTH(modedatacreatedate)<=@yf and gslb=@gslb and formmodeid=139 group by YEAR(modedatacreatedate),gslb),\n" +
                            "\t\tAccuXSFY=(select sum(XSFY) from uf_FN_MainOptData where YEAR(modedatacreatedate)=@nf and MONTH(modedatacreatedate)<=@yf and gslb=@gslb and formmodeid=139 group by YEAR(modedatacreatedate),gslb),\n" +
                            "\t\tAccuHK=(select sum(HK) from uf_FN_MainOptData where YEAR(modedatacreatedate)=@nf and MONTH(modedatacreatedate)<=@yf and gslb=@gslb and formmodeid=139 group by YEAR(modedatacreatedate),gslb),\n" +
                            "\t\tAccuFH=(select sum(FH) from uf_FN_MainOptData where YEAR(modedatacreatedate)=@nf and MONTH(modedatacreatedate)<=@yf and gslb=@gslb and formmodeid=139 group by YEAR(modedatacreatedate),gslb),\n" +
                            "\t\tAccuYBF=(select sum(YBF) from uf_FN_MainOptData where YEAR(modedatacreatedate)=@nf and MONTH(modedatacreatedate)<=@yf and gslb=@gslb and formmodeid=139 group by YEAR(modedatacreatedate),gslb),\n" +
                            "\t\tAccuYJ=(select sum(YJ) from uf_FN_MainOptData where YEAR(modedatacreatedate)=@nf and MONTH(modedatacreatedate)<=@yf and gslb=@gslb and formmodeid=139 group by YEAR(modedatacreatedate),gslb)\n" +
                            "\twhere YEAR(modedatacreatedate)=@nf and MONTH(modedatacreatedate)=@yf and gslb=@gslb and formmodeid=139 \n" +
                            "    SET @yf = @yf + 1\n" +
                            "END";

                    rs.execute(SQL2+SQL3+SQL4);
                    rs.next();

                    this.bb.writeLog("Mode 139 Recalculated Accu Data");

                    //清除对应表单的缓存
                    //cache.removeCache(tablenames);
                }
            }
        } catch (Exception e) {
            result.put("errmsg","自定义出错信息");
            result.put("flag", "false");
        }
        return result;
    }
}
