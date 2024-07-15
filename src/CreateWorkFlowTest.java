//package weaver.interfaces.test;
//import ln.TimeUtil;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import weaver.rsa.security.RSA;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//public class CreateWorkFlowTest {
//    public static void main(String[] args){
//        String back =  getRegist();
//        JSONObject object = JSONObject.fromObject(back);
//        String secrit = object.getString("secrit");
//        String spk = object.getString("spk");
//        String bak2 = applytoken(secrit,spk);
//        JSONObject objecttoken = JSONObject.fromObject(bak2);
//        String token = objecttoken.getString("token");
//        Map<String, String> heads=new HashMap<String, String>();
//
//        Map<String, Object> param=new HashMap<String, Object>();
//        RSA rsa = new RSA();
//        String userid = rsa.encrypt(null, "1", null, "utf8", spk, false);
//        heads.put("token", token);
//        heads.put("appid", "EEAA5436-7577-4BE0-8C6C-89E9D88805EA");
//        heads.put("userid", userid);
//
//        String url = "http://221.226.25.34:8991/api/workflow/paService/doCreateRequest";
//        Map inMap = new HashMap();
//
//        //主表参数
//        Map mainMap = new HashMap();
//        List list = new ArrayList();
//        Map fieldmap =  new HashMap();
//        fieldmap.put("fieldName","sm");
//        fieldmap.put("fieldValue","测试创建流程");
//        list.add(fieldmap);
//        Map fieldmap2 =  new HashMap();
//        fieldmap2.put("fieldName","syzy");
//        fieldmap2.put("fieldValue","测试");
//        list.add(fieldmap2);
//        //附件
//        Map fj =new HashMap();
//        fj.put("fieldName","scfj");
//        List fjList = new ArrayList();
//        Map fjmx = new HashMap();
//        fjmx.put("filePath","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1577426926378&di=0084fc19f5cb90fc2286aab5ca8c343e&imgtype=0&src=http%3A%2F%2Fpic.51yuansu.com%2Fpic2%2Fcover%2F00%2F41%2F80%2F581369c715701_610.jpg");
//        fjmx.put("fileName","图片.jpg");
//        fjList.add(fjmx);
//        Map fjmx2 = new HashMap();
//        FileUtil fu = new FileUtil();
//        //  System.out.println(fu.encryptToBase64("G://测试上传.txt"));
//        //一定要以base64 开头 ，还需要关注下产品那边
//        fjmx2.put("filePath","base64:"+fu.encryptToBase64("G://测试上传.txt"));
//        fjmx2.put("fileName","测试上传.txt");
//        fjList.add(fjmx2);
//        fj.put("fieldValue",fjList);
//        list.add(fj);
//        JSONArray arr = JSONArray.fromObject(list);
//        inMap.put("mainData",arr.toString());
//        inMap.put("requestName","测试创建流程"+ TimeUtil.getCurrentTimeString());
//        inMap.put("workflowId","5");
//        Map otherParams = new HashMap();
//        otherParams.put("isnextflow ","1");
//        otherParams.put("delReqFlowFaild ","1");
//        JSONObject otherObject = JSONObject.fromObject(otherParams);
//        inMap.put("otherParams",otherObject.toString());
//
//        //明细参数
//        List dtlist = new ArrayList();
//        Map dtMap = new HashMap();
//        dtMap.put("tableDBName","formtable_main_24_dt1");
//
//        List workflowRequestTableRecords = new ArrayList();
//        Map workflowRequestTableFieldsMap = new HashMap();
//        List workflowRequestTableFields = new ArrayList();
//        Map dtField =new  HashMap();
//        dtField.put("fieldName","xm");
//        dtField.put("fieldValue","张三");
//        workflowRequestTableFields.add(dtField);
//        Map dtField2 =new  HashMap();
//        dtField2.put("fieldName","bh");
//        dtField2.put("fieldValue","001");
//        workflowRequestTableFields.add(dtField2);
//        Map dtField3 =new  HashMap();
//        dtField3.put("fieldName","dz");
//        dtField3.put("fieldValue","南京");
//        workflowRequestTableFields.add(dtField3);
//        workflowRequestTableFieldsMap.put("recordOrder","0");
//        workflowRequestTableFieldsMap.put("workflowRequestTableFields",workflowRequestTableFields);
//        workflowRequestTableRecords.add(workflowRequestTableFieldsMap);
//        dtMap.put("workflowRequestTableRecords",workflowRequestTableRecords);
//        dtlist.add(dtMap);
//        JSONArray dtlistString = JSONArray.fromObject(dtlist);
//
//        inMap.put("detailData",dtlistString.toString());
//
//        JSONObject jsonObject = JSONObject.fromObject(inMap);
//        System.out.println(jsonObject.toString());
//        String back1 = HttpClient.httpPostForm(url,inMap,heads,"utf-8");
//
//        System.out.println(back1);
//
//    }
//    /**
//     * 注册
//     * @return
//     */
//    public static String getRegist() {
//        Map<String, String> heads = new HashMap<String, String>();
//        String cpk = new RSA().getRSA_PUB();
//        heads.put("appid", "EEAA5436-7577-4BE0-8C6C-89E9D88805EA");
//        heads.put("cpk", cpk);
//        String data = HttpClient.httpPostForm("http://221.226.25.34:8991/api/ec/dev/auth/regist", null, heads,"utf-8");
//        return data;
//    }
//    /**
//     * 获取token
//     * @param secrit
//     * @param spk
//     * @return
//     */
//    public static String  applytoken(String secrit,String spk)  {
//        Map<String, String> heads = new HashMap<String, String>();
//        RSA rsa = new RSA();
//        String secret_2 = rsa.encrypt(null, secrit, null, "utf-8", spk, false);
//        heads.put("appid", "EEAA5436-7577-4BE0-8C6C-89E9D88805EA");
//        heads.put("secret", secret_2);
//        String data = HttpClient.httpPostForm("http://221.226.25.34:8991/api/ec/dev/auth/applytoken", null, heads,"utf-8");
//        return data;
//    }
//}