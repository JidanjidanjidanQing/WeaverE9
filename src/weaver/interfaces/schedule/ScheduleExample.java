//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package weaver.interfaces.schedule;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;

public class ScheduleExample extends BaseCronJob {
    private Log log = LogFactory.getLog(BaseCronJob.class.getName());
    private static int times = 0;
    private Logger logger = LoggerFactory.getLogger(BaseCronJob.class);

    public ScheduleExample() {
    }

    public void execute() {

    }

    public Map execute(Map var1) {
        String var2 = var1.get("test1") + "@test1";
        String var3 = var1.get("test2") + "@test2";
        String var4 = var1.get("test3") + "@test3";
        var1.put("test1", var2);
        var1.put("test2", var3);
        var1.put("test3", var4);
        return var1;
    }
}
