<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
String tabId = request.getParameter("tabId");
String path  = request.getParameter("path");
%>
<script type="text/javascript" charset="utf-8">
 var tabId = "<%=tabId%>";
 //弹出窗口关闭弹出页面回调空函数
 function closeNowCloseFunction(){
 }

   $(function() {
		var btn = $("#onePageExcel");
		btn.click(function() {
			var parentobj =$(window).manhuaDialog.getParentObj(); 
			var queryResult = parentobj.document.getElementById("queryResult").value;
			document.getElementById("exportQueryResultCondition").value = queryResult;
			$("#loginForm").submit();
		});
	});
   $(function() {
		var btn = $("#aaaaaa");
		btn.click(function() {
			var parentobj =$(window).manhuaDialog.getParentObj();
			var parentTabObj = parentobj.document.getElementById(tabId);
	//		var tabHTML = parentTabObj.outerHTML
			
			var actionName = parentTabObj.getAttribute("queryPath");
			var queryData = parentTabObj.getAttribute("queryData");
			var queryResult = parentobj.document.getElementById("queryResult").value;
			
			alert("actionName | "+actionName +"\n"
					+ "queryData | "+queryData);
			document.getElementById("bbbbbbb").value = queryResult;
		});
	});
   
   $(function() {
		var btn1 = $("#morePageExcel");
		btn1.click(function() {
			var parentobj = $(window).manhuaDialog.getParentObj();
			var parentTabObj = parentobj.document.getElementById(tabId);
			
			var actionName = parentTabObj.getAttribute("queryPath");
			var queryData = parentTabObj.getAttribute("queryData");
			var queryResult = parentobj.document.getElementById("queryResult").value;
			
			var querycondition = setTotalPage(queryData, queryResult);
			
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
						document.getElementById("exportQueryResultCondition").value = result.msg;//当前页的查询结果集
					}
				});
			
			$("#loginForm").submit();
		});
	});
   
   
	function  setTotalPage(querycondition,queryResult)
	{
    	var qr= convertJson.string2json1(queryResult);
    	var countrows = qr.pages["countrows"];
    	var qc= convertJson.string2json1(querycondition);
    	 qc.pages["recordsperpage"] = countrows;
        return JSON.stringify(qc);
		
	}
 //$(#) outerHTML()
 
</script>
<body>
   <form method="post" action="<%=path%>" id="loginForm" >
    <!-- 
       <button id="onePageExcel" class="btn btn-primary"  type="button">导出当前页excel</button> -->
       <button id="morePageExcel" class="btn btn-primary"  type="button">导出所有页excel</button>
       <!-- 
       <button id="aaaaaa" class="btn btn-primary"  type="button">aaaaa</button>
        -->
       <input type="hidden" id="exportQueryResultCondition" name="exportQueryResultCondition">
   
   </form><!-- 
   <textarea id="bbbbbbb" rows="" cols=""></textarea> -->
</body>
</html>