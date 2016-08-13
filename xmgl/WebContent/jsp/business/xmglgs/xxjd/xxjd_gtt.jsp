<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title>项目甘特图</title>
<%
	String id = request.getParameter("id");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/echarts/asset/js/esl/esl.js"></script>


<script src="${pageContext.request.contextPath }/gantt/js/json2.js"></script>
<script src="${pageContext.request.contextPath }/gantt/js/lib.js"></script>
<script src="${pageContext.request.contextPath }/gantt/js/prettify.js"></script>
<%-- <script src="${pageContext.request.contextPath }/gantt/js/Gantt1.js"></script> --%>
<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
var id = "<%=id%>";
 var ganttData;
/* 

 

function closeParentCloseFunction(val){
}
//弹出窗口关闭弹出页面回调空函数
function closeNowCloseFunction(){
}*/ 
function init() {
	var data = null;
	submit(controllername+"?ggtById_xxjd&id="+id,data,null);
	
}
function submit(actionName, data,tablistID){
	$.ajax({
		type : 'post',
		url : actionName,
		data : data,
		cache : false,
		dataType : "json",  
		async :	false,
		success : function(result) {
			//alert(result.msg);
			ganttData= result.msg;
			//alert(result.msg);
			//alert(ganttData.length);
		}
	});
} 
$(function() {
	init();
	//showGtt(ganttData);
	gttview();
 
});

function gttview()
{
	var chartHeight = getDivStyleHeight()-5;
    var chart = new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Gantt.swf", "ChartId", "100%", chartHeight, "0", "0" );
    chart.setXMLData( ganttData );
    chart.render("chartdiv");
}


</script>

</head>
<body>
    <div  id="chartdiv" style="height:100%"></div>
</body>
</html>