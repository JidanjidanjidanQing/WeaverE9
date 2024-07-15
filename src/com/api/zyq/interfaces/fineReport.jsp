<%
String cpt = request.getParameter("cpt");
String userid = ""+user.getUID();
String deptid = ""+user.getUserDepartment();
String subcomid = ""+user.getUserSubCompany1();

String isAdmin = "0";
RecordSet rs = new RecordSet();
rs.executeSql("select id from hrmresource where id = '"+userid+"'");

if(rs.next()){
isAdmin ="0";
}else{
    rs.executeSql("select id from hrmresourcemanaper where id = '"+userid+"' and id='1'");
    if(rs.next()){
        isadmin = "1";
    }
}

String paramStr = "";
paramStr += "&userid=" + userid;
paramstr += "&deptid=" + deptid;
paramStr += "&comid="+ subcomid;
paramstr += "&isAdmin=" + isAdmin;

response.sendRedirect("https://webreport.reyoung.cn/WebReport/ReportServer?reportlet="+cpt+paramStr);

%>