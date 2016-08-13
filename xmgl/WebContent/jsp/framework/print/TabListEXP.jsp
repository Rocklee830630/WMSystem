<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="com.ccthanking.framework.util.*"%>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>长春市政府投资建设项目管理中心——综合管控中心</title>
<link href="${pageContext.request.contextPath }/css/bootstrap.css" rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath }/css/base.css" rel="stylesheet" media="screen">
<script src="${pageContext.request.contextPath }/js/base/jquery.js"></script>
<script src="${pageContext.request.contextPath }/js/base/bootstrap.js"></script>
<script src="${pageContext.request.contextPath }/js/common/convertJson.js"></script>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script src="${pageContext.request.contextPath }/js/common/TabList.js"></script>

</head>
<%
String tabId = Pub.val(request, "tabId");
String templateName = Pub.val(request, "templateName");
String fieldnames = Pub.val(request, "fieldnames");
String startXY = Pub.val(request, "startXY");
%>
<script type="text/javascript" charset="utf-8">
 var tabId = "<%=tabId%>";
 //弹出窗口关闭弹出页面回调空函数
 function closeNowCloseFunction(){
 }
 function doInit(){
	 var parentobj =$(window).manhuaDialog.getParentObj(); 
	 var tabObj_parent = parentobj.document.getElementById(tabId);
	 var printFileName = $(tabObj_parent).attr("printFileName");
	 if(!printFileName){
		 printFileName = "";
	 }
	 document.getElementById("printFileName").value = printFileName;//打印文件名
	 $("#DT1").html(tabObj_parent.outerHTML);
     var tabObj = document.getElementById("DT1");
	 //alert(tabObj.outerHTML);
     var tablistThead = $(tabObj).find("thead");
     var tabthrows = $("thead tr" ,$(tabObj));
     var tbHeadTh = tablistThead.find('tr th');//获取thead下的tr下的th 
     var throwsize = tabthrows.size();//表头行尺寸
		//获取所有fieldname的记录数
     var k = 0;
     var fielnames= "";
		if(throwsize>=2){//多表头处理
			var firstTh = tbHeadTh.eq(0).text();
		    if(firstTh == "#"){
		    	tbHeadTh.eq(0).html("序号");
		    }
			
			for(var t =0;t<tbHeadTh.size();t++)
				{
				
						if(typeof(tbHeadTh.eq(t).attr("colindex")) != "undefined")
							{
								k++;
							}
					
				}
		if(k>0){
			for(var arr=1;arr<=k;arr++)
			{
				for(var t =0;t<tbHeadTh.size();t++)
				{

					if(typeof(tbHeadTh.eq(t).attr("colindex")) != "undefined"&&typeof(tbHeadTh.eq(t).attr("fieldname")) != "undefined"&&(typeof(tbHeadTh.eq(t).attr("noprint")) == "undefined"||tbHeadTh.eq(t).attr("noprint") != "true"))
					{
						if(tbHeadTh.eq(t).attr("colindex") == arr){
							if(typeof(tbHeadTh.eq(t).attr("colspan")) != "undefined"&&tbHeadTh.eq(t).attr("colspan")>0){
								fielnames +=tbHeadTh.eq(t).attr("fieldname")+"|{\"colspan\":\""+tbHeadTh.eq(t).attr("colspan")+"\"},";
							}else{
								fielnames +=tbHeadTh.eq(t).attr("fieldname")+",";
							}
							break;
						}
					}else if (typeof(tbHeadTh.eq(t).attr("colindex")) != "undefined"&&typeof(tbHeadTh.eq(t).attr("fieldname")) != "undefined"&&(typeof(tbHeadTh.eq(t).attr("noprint")) != "undefined"||tbHeadTh.eq(t).attr("noprint") == "true")){
						tbHeadTh.eq(t).remove();
					}
						
				}
			}
			}
			//
		}else{
			for(var t =0;t<tbHeadTh.size();t++)
			{
				if(typeof(tbHeadTh.eq(t).attr("fieldname")) != "undefined")
				{
					if((typeof(tbHeadTh.eq(t).attr("noprint")) != "undefined"&&tbHeadTh.eq(t).attr("noprint") == "true")){
						tbHeadTh.eq(t).remove();
					}else{
						if(typeof(tbHeadTh.eq(t).attr("colspan")) != "undefined"&&tbHeadTh.eq(t).attr("colspan")>0){
							fielnames +=tbHeadTh.eq(t).attr("fieldname")+"|{\"colspan\":\""+tbHeadTh.eq(t).attr("colspan")+"\"},";
						}else{
							fielnames +=tbHeadTh.eq(t).attr("fieldname")+",";
						}
					}
					
					
				}
			}
		}
		//alert(parent.document.getElementById("queryResult").value);
	  if(tabObj){
	    document.getElementById("tabHtml").value = tabObj_parent.outerHTML;//当前页的查询结果集
	    document.getElementById("querycondition").value = tabObj_parent.getAttribute("queryData");//查询条件
	    document.getElementById("tabThead").value = $(tabObj).find("thead")[0].outerHTML;//打印表格的表头html代码
	    document.getElementById("fieldname").value = fielnames;//所有显示字段，字段名用逗号分割
	    document.getElementById("actionName").value = tabObj_parent.getAttribute("queryPath");//执行查询路径
	  }
	 
 }
   $(function() {
		var btn = $("#onePageExcel");
		btn.click(function() {
			var parentobj =$(window).manhuaDialog.getParentObj(); 
			var tabObj_parent = parentobj.document.getElementById(tabId);
			var s = getTableRowsJsonString();
			document.getElementById("tabHtml").value = s;//parentobj.document.getElementById("queryResult").value;//当前页的查询结果集
			
			var datas = new Object();
			datas["ExpTabListResultValue"] = s;
			datas["ExpTabListQueryCondition"] = document.getElementById("querycondition").value;
			datas["ExpTabListThead"] = document.getElementById("tabThead").value;
			datas["ExpTabListFieldNames"] = document.getElementById("fieldname").value;
			datas["ExpActionName"] = document.getElementById("actionName").value;
			datas["templateName"] = document.getElementById("templateName").value;
			datas["templateName"] = document.getElementById("templateName").value;
			datas["fieldnames"] = document.getElementById("fieldnames").value;
			datas["startXY"] = document.getElementById("startXY").value;
			datas["printFileName"] = document.getElementById("printFileName").value;
			xmlHttp.open(
				"POST",
				"${pageContext.request.contextPath }/servlet/TableExp",
				true
			);
			xmlHttp.onreadystatechange = handleStateChange;
			//req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			xmlHttp.send(JSON.stringify(datas));
			
			//$("#loginForm").submit();
		});
	});
	var xmlHttp = new XMLHttpRequest();
   $(function() {
		var btn1 = $("#morePageExcel");
		btn1.click(function() {
			var actionName = document.getElementById("actionName").value;
			var parentobj =$(window).manhuaDialog.getParentObj(); 
			var querycondition = setTotalPage(document.getElementById("querycondition").value,parentobj.document.getElementById("queryResult").value);
			var data = {
				msg : querycondition
			};
			$.ajax({
				url : actionName,
				data : data,
				cache : false,
				async :	false,
				dataType : "json",  
				type : 'post',
				success : function(result) {
					document.getElementById("tabHtml").value = result.msg;//当前页的查询结果集
					
					//document.getElementById("tabHtml").value = str;
					
					var str = "";
					for(var m=0;m<30;m++){
						str += "1";
					}
					var dataObj = convertJson.string2json1(result.msg);
					var datas = new Object();
					datas["ExpTabListResultValue"] = dataObj;
					datas["ExpTabListQueryCondition"] = document.getElementById("querycondition").value;
					datas["ExpTabListThead"] = document.getElementById("tabThead").value;
					datas["ExpTabListFieldNames"] = document.getElementById("fieldname").value;
					datas["ExpActionName"] = document.getElementById("actionName").value;
					datas["templateName"] = document.getElementById("templateName").value;
					datas["templateName"] = document.getElementById("templateName").value;
					datas["fieldnames"] = document.getElementById("fieldnames").value;
					datas["startXY"] = document.getElementById("startXY").value;
					datas["printFileName"] = document.getElementById("printFileName").value;
					xmlHttp.open(
						"POST",
						"${pageContext.request.contextPath }/servlet/TableExp",
						true
					);
					xmlHttp.onreadystatechange = handleStateChange;
					//req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
					xmlHttp.send(JSON.stringify(datas));
					
					//$("#loginForm").submit();
				}
			});
		});
	});
	function handleStateChange(){
		if(xmlHttp.readyState == 4) {
			if(xmlHttp.status == 200) {
				var str = xmlHttp.responseText;
				//window.open("${pageContext.request.contextPath }/file/ExpDownload/"+str+"/"+$("#printFileName").val()+".xls","_blank"); 
				$("#f2_expID").val(str);
				$("#f2_fileName").val($("#printFileName").val());
				$("#expDownload").submit();
			}
		}
	}
	function  setTotalPage(querycondition,queryResult)
	{
    	var qr= convertJson.string2json1(queryResult);
    	var countrows = qr.pages["countrows"];
    	var qc= convertJson.string2json1(querycondition);
    	 qc.pages["recordsperpage"] = countrows;
        return JSON.stringify(qc);
	}
	function getTableRowsJsonString(){
		var parentobj =$(window).manhuaDialog.getParentObj(); 
		var tabObj_parent = parentobj.document.getElementById(tabId);
		var $this = $(tabObj_parent);
    	var allRowJson = $this.getTabRowsToJsonArray();
    	var temp ;

    	for (var i = 0; i < allRowJson.length; i++)
    	{
    	  for (var j = 0; j < allRowJson.length - i; j++)
    	  {
    		var now = allRowJson[j];
    		var next = allRowJson[j + 1];
    		if(!next) break;
    	    if (now[fieldname] > next[fieldname])
    	    {
    	      temp = allRowJson[j + 1];
    	      allRowJson[j + 1] = allRowJson[j];
    	      allRowJson[j] = temp;
    	     }
    	   }
    	}
    	var rowJsonString = JSON.stringify(allRowJson);
    	rowJsonString ="{response:{data:"+rowJsonString+"}}";
    	return rowJsonString;
	}

 
 
</script>
	<body onload="doInit()">
		<div style='display: none' id="DT1">
		</div>
		<form method="post"
			action="${pageContext.request.contextPath }/servlet/TableExp"
			id="loginForm">

			<button id="onePageExcel" class="btn btn-primary" type="button">
				导出当前页excel
			</button>
			<button id="morePageExcel" class="btn btn-primary" type="button">
				导出所有页excel
			</button>

			<textarea type="text" id="tabHtml" style="display: none;"
				name="ExpTabListResultValue"></textarea>
			<textarea type="text" id="querycondition" style="display: none"
				name="ExpTabListQueryCondition"></textarea>
			<textarea type="text" id="tabThead" style="display: none"
				name="ExpTabListThead"></textarea>
			<textarea type="text" id="fieldname" style="display: none"
				name="ExpTabListFieldNames"></textarea>
			<textarea type="text" id="actionName" style="display: none"
				name="ExpActionName"></textarea>
			<textarea type="text" id="templateName" style="display: none"
				name="templateName"><%=templateName%></textarea>
			<textarea type="text" id="fieldnames" style="display: none"
				name="fieldnames"><%=fieldnames%></textarea>
			<textarea type="text" id="startXY" style="display: none"
				name="startXY"><%=startXY%></textarea>
			<textarea type="text" id="printFileName" style="display: none;"
				name="printFileName"></textarea>

		</form>
		<form method="post" 
			action="${pageContext.request.contextPath }/servlet/TableExp?method=expDownload"
			id="expDownload" style="display:none;">
			<input type="hidden" id="f2_expID" name="f2_expID">
			<input type="hidden" id="f2_fileName" name="f2_fileName">
		</form>
	</body>
</html>