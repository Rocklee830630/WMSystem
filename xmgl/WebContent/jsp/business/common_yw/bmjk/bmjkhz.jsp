<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>项目手册</title>
<style type="text/css">
.tab-content{
	padding:2px;
}
</style>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var iframeid = "frame0";

//页面初始化
$(function() {
	$("iframe").height(getDivStyleHeight()-getTableTh(2)-10);
	init();
	$('#tabPage0').on('show', function (e) {
		$("#example_query").removeAttr("disabled");
		var src = "${pageContext.request.contextPath }/jsp/business/xmcbk/bmgk/jcBmgk.jsp";
		$("#frame0").attr("src",src);
		iframeid = "frame0";

	});
	$('#tabPage1').on('show', function (e) {
		$("#example_query").removeAttr("disabled");
		var src = "${pageContext.request.contextPath }/jsp/char/bmjk.jsp";
		$("#frame1").attr("src",src);
		iframeid = "frame1";

	});
	$('#tabPage2').on('show', function (e) {
		$("#example_query").removeAttr("disabled");
		var src = "${pageContext.request.contextPath }/jsp/business/qqsx/qqsxbz/qqsxbzjm.jsp"; 
		$("#frame2").attr("src",src);
		iframeid = "frame2";

	});
	$('#tabPage3').on('show', function (e) {
		$("#example_query").removeAttr("disabled");
		var src = "${pageContext.request.contextPath }/jsp/business/sjgl/sjbz/sjbzjm.jsp"; 
		$("#frame3").attr("src",src);
		iframeid = "frame3";

	});
	$('#tabPage4').on('show', function (e) {
		$("#example_query").attr("disabled","disabled");
		var src = "${pageContext.request.contextPath }/jsp/business/zjb/bzjk/zjbzjm.jsp"; 
		$("#frame4").attr("src",src);
		iframeid = "frame4";

	});
	$('#tabPage5').on('show', function (e) {
		$("#example_query").removeAttr("disabled");
		var src = "${pageContext.request.contextPath }/jsp/business/htgl/bmgk.jsp"; 
		$("#frame5").attr("src",src);
		iframeid = "frame5";

	});
	//工程部
	$('#tabPage6').on('show', function (e) {
		$("#example_query").removeAttr("disabled");
		var src = "${pageContext.request.contextPath }/jsp/business/gcb/gcbgk.jsp"; 
		$("#frame6").attr("src",src);
		iframeid = "frame6";

	});
	//质量安全
	$('#tabPage7').on('show', function (e) {
		$("#example_query").removeAttr("disabled");
		var src = "${pageContext.request.contextPath }/jsp/business/zlaq/bmgk.jsp"; 
		$("#frame7").attr("src",src);
		iframeid = "frame7";

	});
	//排迁
	$('#tabPage8').on('show', function (e) {
		$("#example_query").removeAttr("disabled");
		var src = "${pageContext.request.contextPath }/jsp/business/pqgl/bmgk/pqBmgk.jsp"; 
		$("#frame8").attr("src",src);
		iframeid = "frame8";

	});
	//征拆
	$('#tabPage9').on('show', function (e) {
		$("#example_query").removeAttr("disabled");
		var src = "${pageContext.request.contextPath }/jsp/business/zsb/zcbz/zcbzjm.jsp"; 
		$("#frame9").attr("src",src);
		iframeid = "frame9";

	});
});

	
//页面默认参数
function init(){
	$("#frame0").attr("src","${pageContext.request.contextPath }/jsp/business/xmcbk/bmgk/jcBmgk.jsp");
}
function printDiv()
{
	window.frames[iframeid].print();
	
}


</script>      
    
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p class="text-right tabsRightButtonP">
			&nbsp;<br/>&nbsp;
	</p>
<ul class="nav nav-tabs"> 
	<li class="active"><a href="#xmsc1" data-toggle="tab" id="tabPage0">计财监控</a></li>
	<li class=""><a href="#xmsc2" data-toggle="tab" id="tabPage1">计划监控</a></li>
	<li class=""><a href="#xmsc3" data-toggle="tab" id="tabPage2">手续监控</a></li>
	<li class=""><a href="#xmsc4" data-toggle="tab" id="tabPage3">设计监控</a></li>
	<li class=""><a href="#xmsc5" data-toggle="tab" id="tabPage4">造价监控</a></li>
	<li class=""><a href="#xmsc6" data-toggle="tab" id="tabPage5">招投标监控</a></li>
	<li class=""><a href="#xmsc7" data-toggle="tab" id="tabPage6">工程监控</a></li>
	<li class=""><a href="#xmsc8" data-toggle="tab" id="tabPage7">质量安全监控</a></li>
	<li class=""><a href="#xmsc9" data-toggle="tab" id="tabPage8">排迁监控</a></li>
	<li class=""><a href="#xmsc10" data-toggle="tab" id="tabPage9">征拆监控</a></li>
	
	<!-- <li class=""><a href="#xmsc8" data-toggle="tab" >质量安全检查</a></li>   -->
</ul>
<div class="tab-content"> 
	<!-- 静态信息tab页 -->
	<div class="tab-pane active" id="xmsc1" style="height:100%"> 
		<iframe 
		width="100%"
		height="85%"
		style="border-style: none;"
		frameboder = "0"
		scrolling="aotu"
		id="frame0"
		>
		</iframe>	
    </div>
    
    <!-- 投资动态tab页 -->
    <div class="tab-pane" id="xmsc2" style="height:100%">
    	<iframe 
		width="100%"
		height="85%"
		style="border-style: none;"
		frameboder = "0"
		scrolling="aotu"
		id="frame1"
		>
		</iframe>
    </div>
    <!-- 质量安全tab页 -->
    <div class="tab-pane" id="xmsc3" style="height:100%">
		<iframe 
		width="100%"
		height="85%"
		style="border-style: none;"
		frameboder = "0"
		scrolling="aotu"
		id="frame2"
		>
		</iframe>	
    </div>
    <!-- 合同信息tab页 -->
    <div class="tab-pane" id="xmsc4" style="height:100%">
    	<iframe 
		width="100%"
		height="85%"
		style="border-style: none;"
		frameboder = "0"
		scrolling="aotu"
		id="frame3"
		>
		</iframe>
    </div>
    <!-- 项目文档tab页 -->
    <div class="tab-pane" id="xmsc5" style="height:100%">
    	<iframe 
		width="100%"
		height="85%"
		style="border-style: none;"
		frameboder = "0"
		scrolling="aotu"
		id="frame4"
		>
		</iframe>
    </div>
    <div class="tab-pane" id="xmsc6" style="height:100%">
    	<iframe 
		width="100%"
		height="85%"
		style="border-style: none;"
		frameboder = "0"
		scrolling="aotu"
		id="frame5"
		>
		</iframe>
    </div>
    <!-- 存在问题tab页 -->
    <div class="tab-pane" id="xmsc7" style="height:100%">
    	<iframe 
		width="100%"
		height="85%"
		style="border-style: none;"
		frameboder = "0"
		scrolling="auto"
		id="frame6"
		>
		</iframe>
    </div>
    <!-- 质量安全监控 -->
    <div class="tab-pane" id="xmsc8" style="height:100%">
    	<iframe 
		width="100%"
		height="85%"
		style="border-style: none;"
		frameboder = "0"
		scrolling="auto"
		id="frame7"
		>
		</iframe>
    </div>
    <!-- 排迁 -->
    <div class="tab-pane" id="xmsc9" style="height:100%">
    	<iframe 
		width="100%"
		height="85%"
		style="border-style: none;"
		frameboder = "0"
		scrolling="auto"
		id="frame8"
		>
		</iframe>
    </div>
    <!-- 征拆 -->
    <div class="tab-pane" id="xmsc10" style="height:100%">
    	<iframe 
		width="100%"
		height="85%"
		style="border-style: none;"
		frameboder = "0"
		scrolling="auto"
		id="frame9"
		>
		</iframe>
    </div>
</div>
</div>

  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>