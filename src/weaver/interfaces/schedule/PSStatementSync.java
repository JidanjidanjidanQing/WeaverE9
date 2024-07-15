package weaver.interfaces.schedule;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
//财务运营数据 从四班同步至泛微
public class PSStatementSync extends BaseCronJob {
    public PSStatementSync() {
    }

    public void execute() {
        RecordSet rs = new RecordSet();
        BaseBean log = new BaseBean();



        log.writeLog("");



        String sql=null;




        rs.execute(sql);

    }

}
