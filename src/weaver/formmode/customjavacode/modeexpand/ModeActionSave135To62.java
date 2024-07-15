
package weaver.formmode.customjavacode.modeexpand;
import java.util.HashMap;
import java.util.Map;

import weaver.formmode.customjavacode.AbstractModeExpandJavaCodeNew;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.soa.workflow.request.RequestInfo;
import weaver.conn.RecordSet;

/**
 * 说明
 * 修改时
 * 类名要与文件名保持一致
 * class文件存放位置与路径保持一致。
 * 请把编译后的class文件，放在对应的目录中才能生效
 * 注意 同一路径下java名不能相同。
 * @author Administrator
 *
 */
public class ModeActionSave135To62 extends AbstractModeExpandJavaCodeNew{
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
            int billid = -1;//数据id
            int modeid = -1;//模块id
            this.bb.writeLog("-------user"+user);
            RequestInfo requestInfo = (RequestInfo)param.get("RequestInfo");
            if(requestInfo!=null){

                billid = Util.getIntValue(requestInfo.getRequestid());
                modeid = Util.getIntValue(requestInfo.getWorkflowid());

                this.bb.writeLog("-------testbillid:"+billid);
                this.bb.writeLog("-------testmodeid:"+modeid);

                if(billid>0&&modeid>0){
                    //------请在下面编写业务逻辑代码------
                    RecordSet rs = new RecordSet();

                    //String SQL1="select nf,yf,JLR,LRZE from uf_FN_MainProfit where id="+billid;
                    String SQL1="select * from uf_FN_MainProfit where id="+billid;


                    this.bb.writeLog(SQL1);

                    rs.execute(SQL1);
                    rs.next();

                    String NF=rs.getString("nf");
                    String YF=rs.getString("yf");
                    String JLR=rs.getString("JLR");
                    String LRZE=rs.getString("LRZE");




                    this.bb.writeLog("-------NF:"+NF);
                    this.bb.writeLog("-------YF:"+YF);
                    this.bb.writeLog("-------JLR:"+JLR);
                    this.bb.writeLog("-------LRZE:"+LRZE);

                    String SQL2=
                            "IF NOT EXISTS (select * from uf_HR_MktProfit where nf="+NF+" and yf="+YF+" and fb=2)"
                            +"INSERT into uf_HR_MktProfit(nf,yf,Div_LR,Div_JLR,fb,formmodeid)values("+NF+","+YF+","+LRZE+","+JLR+",2"+",62"+") "
                            +"ELSE "
                            +"UPDATE uf_HR_MktProfit SET Div_LR="+LRZE+", Div_JLR="+JLR+" WHERE nf="+NF+" and yf="+YF+" and fb=2";

                    rs.execute(SQL2);
                    rs.next();

                    this.bb.writeLog(SQL2);



                    //rs.execute("select * from");
                    //rs.next();

                }
            }
        } catch (Exception e) {
            result.put("errmsg","自定义出错信息");
            result.put("flag", "false");
        }
        return result;
    }
}
