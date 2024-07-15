<script type="text/javascript">
/*
* 请在下面编写JS代码
*/
function downLoad(){
    var tableName = "uf_HR_ResultAplRcd";//这里的是需要下载附件的台账表名
    var billIds = ModeList.getCheckedID();
    var url = "/api/zxDownloadFile/downloadFile?ids="+billIds+"&tableName="+tableName;
    jQuery.ajax({
    url : url,
    type : "post",
    processData : false,
    data : "",
    dataType : "text",
    async : false,//
    success: function(data){
        var result = JSON.parse(data);
        if(result.code==0){
            var path = result.path; 
            try{
                var elemIF = document.createElement("iframe");
                elemIF.src = path;
                elemIF.style.display ="none";
                document.body.appendChild(elemIF);
            }catch(e) {
                alert("下载异常！");
            }
        }else{
            alert(result.message);
        }
    }, error:function(XMLHttpRequest, textStatus, errorThrown){
                 alert("error");
         }                       
    });
}
</script>
