import weaver.file.ExcelFile;
import weaver.file.ExcelRow;
import weaver.file.ExcelSheet;
import weaver.file.ExcelOut;

class Main {
    public static void main(String[] args) {
        //OA地址/weaver/weaver.file.FileDownload?fileid=&download=1&requestid=


//        User user=new User(387);//当前用户，具体传递实际的用户对象即可
//        int fileid=10899;//表示附件id，对应附件表imagefile中的imagefileid字段值。
//        //该ddcode是有时效的默认是5分钟，如果不要时效控制的话，可以用下面的方式来传参
//        String ddcode = SystemDocUtil.takeddcode(user,fileid+"",null);

//        User user=new User(387);
//        Map params =new HashMap();
//        params.put("timelimit","0");
//        params.put("adminright","1");
//        String ddcode = SystemDocUtil.takeddcode(user,"10899",params);

        weaver.docs.docs.util.DesUtils des = null;

        try
        {
            des = new weaver.docs.docs.util.DesUtils();
        }
        catch(Exception e)
        {}

        String ddcode = 1+"_" + "16157";

        try
        {
            ddcode = des.encrypt(ddcode);
        }
        catch(Exception e)
        {}



        //"/weaver/weaver.file.FileDownload?fileid="+rs.getString("imagefileid")+"&download=1&ddcode="+ getddcode(rs.getInt("imagefileid"))
        //"/weaver/weaver.file.FileDownload?fileid="+"16157"+"&download=1&ddcode="+ SystemDocUtil.getddcode(16157)
        //http://localhost:8008/weaver/weaver.file.FileDownload?fileid=16157&download=1&ddcode=3c6492472bba3670


        System.out.println("ddcode");
        System.out.println(ddcode);
    }
}