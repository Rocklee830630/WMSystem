<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<title>项目甘特图</title>
<%
	String id = request.getParameter("id");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${pageContext.request.contextPath }/css/bootstrap.css" rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath }/gantt/css/style.css" rel="stylesheet" media="screen">
<script src="${pageContext.request.contextPath }/js/base/jquery.js"></script>
<script src="${pageContext.request.contextPath }/js/base/bootstrap.js"></script>
<script src="${pageContext.request.contextPath }/gantt/js/jquery.fn.gantt.js"></script>
<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
var id = "<%=id%>";
var ganttData;
function init() {
	var data = null;
	submit(controllername+"?ggtById&id="+id,data,null);
	
}
function closeParentCloseFunction(val){
}
//弹出窗口关闭弹出页面回调空函数
function closeNowCloseFunction(){
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
			ganttData= eval(result.msg);
			//alert(result.msg);
			//alert(ganttData.length);
		}
	});
}
$(function() {
	init();
	showGtt(ganttData);
 
});
function showGtt(ganttData){
	$(".gantt").gantt({
		source: ganttData,
		//source:[{name:'工期安排',desc:'开工时间',values:[{from:'/Date(1326785200000)/',to: '/Date(1335785200000)/',label:'三顿饭都是',customClass:'ganttGreen'}]},{name:'工期安排',desc:'开工时间',values:[{from:'/Date(1326785200000)/',to: '/Date(1335785200000)/',label:'三顿饭都是',customClass:'ganttGreen'}]},{name:'工期安排',desc:'开工时间',values:[{from:'/Date(1326785200000)/',to: '/Date(1335785200000)/',label:'三顿饭都是',customClass:'ganttGreen'}]}],
		navigate: "scroll",
		scale: "days",
		maxScale: "months",
		minScale: "days",
		itemsPerPage: 100,
		onItemClick: function(data) {
			//alert(data);
			//alert("Itemclick事件");
		},
		onAddClick: function(dt, rowId) {
			//alert("Empty space clicked - add an item!");
		},
		onRender: function() {
			if (window.console && typeof console.log === "function") {
				console.log("chart rendered");
			}
		}
	});

	$(".gantt").popover({
		selector: ".bar",
		title: "I'm a popover",
		content: "And I'm the content of said popover.",
		trigger: "hover"
	});

	prettyPrint();
}
</script>

</head>
<body>
    <div class="gantt"></div>
</body>
</html>