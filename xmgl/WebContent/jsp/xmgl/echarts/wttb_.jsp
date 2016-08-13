<!DOCTYPE html>
<html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@page import="org.apache.commons.codec.binary.*"%>
<%@ page import="com.ccthanking.framework.service.impl.*"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.framework.*"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

<head>
<title>问题统计</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/xmgl/echarts/bootstrap/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/xmgl/echarts/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/FixTable.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/echarts/asset/js/esl/esl.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/convertJson.js"></script>


<link rel="stylesheet" href="${pageContext.request.contextPath }/jsp/xmgl/echarts/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/jsp/xmgl/echarts/bootstrap/css/bootstrap-theme.min.css">
<script type="text/javascript">			
var controllername = "${pageContext.request.contextPath }/bmgk/GcHtBmgkController.do";
var controllernamecommon = "${pageContext.request.contextPath}/bmgk/ZjBmgkController.do";
var controllername1= "${pageContext.request.contextPath }/wttb/WttbBmgkController.do"; 
var p_chartNum=1;
$(function() {

});

require.config({
	paths:{ 
		'echarts' : '${pageContext.request.contextPath }/echarts/js/echarts',
		'echarts/chart/pie' : '${pageContext.request.contextPath }/echarts/js/echarts',
        'echarts/chart/bar' : '${pageContext.request.contextPath }/echarts/js/echarts-map',
        'echarts/chart/line': '${pageContext.request.contextPath }/echarts/js/echarts-map'
	}
});
var p_ec = null; 
function doInit() {
	var now = new Date();
	var year = now.getFullYear();
	queryWtjjcdEChart(year);
	queryZbbmEChart(year);
	queryZbldEChart(year);
	queryNdwttbqkEChart(year);
}

//解决程度
function queryWtjjcdEChart(nd) {
	var action = controllername1 + "?queryWtjjcdEChart&nd="+nd;
	$.ajax({
		url : action,
		dataType:"json",
		async:false,
		success: function(result){
			var resObj = convertJson.string2json1(result.msg);
			var myChart = p_ec.init(document.getElementById("main3"));
			myChart.setOption(resObj);
		}
	});
}
//各部门问题接受情况
function queryZbbmEChart(nd) {
	var action = controllername1 + "?queryZbbmEChart&nd="+nd;
	$.ajax({
		url : action,
		dataType:"json",
		async:false,
		success: function(result){
			var resObj = convertJson.string2json1(result.msg);
			var myChart = p_ec.init(document.getElementById("main4"));
			myChart.setOption(resObj);
		}
	});
}
//中心领导问题接收情况
function queryZbldEChart(nd) {
	var action = controllername1 + "?queryZbldEChart&nd="+nd;
	$.ajax({
		url : action,
		dataType:"json",
		async:false,
		success: function(result){
			var resObj = convertJson.string2json1(result.msg);
			var myChart = p_ec.init(document.getElementById("main41"));
			myChart.setOption(resObj);
		}
	});
}

//问题情况阅读分部
function queryNdwttbqkEChart(nd) {
	var action = controllername1 + "?queryNdwttbqkEChart&nd="+nd;
	$.ajax({
		url : action,
		dataType:"json",
		async:false,
		success: function(result){
			var resObj = convertJson.string2json1(result.msg);
			var myChart = p_ec.init(document.getElementById("main5"));
			myChart.setOption(resObj);
		}
	});
}
</script>
<style type="text/css">
    .buttomDiv {
    position :absolute;
    left : 0px;
    top :  600px;
    }
    
    div.footer{
		border:1px solid #CCC;background:#222222;
		position:fixed;height:80%;width:100%;
		z-index:100000;left:0%;top:90%;
	}
	a{
		color:#FFF;
		}
		.footer .toolbars {
			border-left:2px solid #FFF;
			height:50px;
			line-height:50px;
			text-align:center;
			color:#FFF;
			font-size:10px;
			font-family: Microsoft YaHei;
		}
			
		.footer .firsttoolbars {
			height:50px;
			line-height:50px;
			text-align:center;
			color:#FFF;
			font-size:10px;
			font-family: Microsoft YaHei;
		}
    </style>
</head>
<body>
<app:dialogs />

	<div class="test">
	    <div id="main3" style="height:300px;border:1px solid #ccc;padding:10px;"></div>
	    <div id="main4" style="height:500px;border:1px solid #ccc;padding:10px;"></div>
	    <div id="main41" style="height:400px;border:1px solid #ccc;padding:10px;"></div>
	    <div id="main5" style="height:400px;border:1px solid #ccc;padding:10px;"></div>
    </div>
    <p style="height:30px"></p>
	<div class="footer">
    	<div class="container">
				<div class="row">
					<div class="col-xs-3 .col-sm-3 col-md-3 firsttoolbars" onclick="main333()">
						<span><a href="javascript:void(0);" style="color:#FFF;">解决程度</a></span>
					</div>
					<div class="col-xs-3 .col-sm-3 col-md-3 toolbars" onclick="main444()">
						<span><a href="javascript:void(0);" style="color:#FFF;">部门接收情况</a></span>
					</div>
					<div class="col-xs-3 .col-sm-3 col-md-3 toolbars" onclick="main441()">
						<span><a href="javascript:void(0);" style="color:#FFF;">领导接收情况</a></span>
					</div>
					<div class="col-xs-3 .col-sm-3 col-md-3 toolbars" onclick="main555()">
						<span><a href="javascript:void(0);" style="color:#FFF;">月度分布</a></span>
					</div>
				</div>
			</div>
	</div>
	
	<script type="text/javascript">
		function main555() {
	        $("html,body").animate({scrollTop:$("#main5").offset().top},400);
	    }
	    function main333() {
	        $("html,body").animate({scrollTop:$("#main3").offset().top},400);
	    }
	    function main444() {
	        $("html,body").animate({scrollTop:$("#main4").offset().top},400);
	    }
	    function main441() {
	        $("html,body").animate({scrollTop:$("#main41").offset().top},400);
	    }
		// 使用
		require(
		    [
		        'echarts',
		        'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
		    ],
		    function (ec) {
		    	p_ec = ec;
		    	doInit();
		    }
		);
	</script>
</body>
</html>