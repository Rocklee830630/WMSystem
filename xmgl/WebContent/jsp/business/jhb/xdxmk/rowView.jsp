<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.lang.StringBuffer"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="java.sql.ResultSet"%>
<app:base/>
<title>项目手册</title>
<style type="text/css">
.tab-content{
	padding:2px;
}
</style>
<%
	String id = request.getParameter("id");

%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
var id = "<%=id%>";
var iframeid = "frame0";

//页面初始化
$(function() {
	$("iframe").height(getDivStyleHeight()-getTableTh(2)-10);
	init();
	$('#tabPage0').on('show', function (e) {
		$("#example_query").removeAttr("disabled");
		var src = "subTabIframes/xmgyxx_View.jsp?id="+id;
		$("#frame0").attr("src",src);
		iframeid = "frame0";

	});
	$('#tabPage1').on('show', function (e) {
		$("#example_query").removeAttr("disabled");
		var src = "subTabIframes/xmjzxx_View.jsp?id="+id;
		$("#frame1").attr("src",src);
		iframeid = "frame1";

	});
	$('#tabPage2').on('show', function (e) {
		$("#example_query").removeAttr("disabled");
		var src = "subTabIframes/zlaqxxk.jsp?id="+id; 
		$("#frame2").attr("src",src);
		iframeid = "frame2";

	});
	$('#tabPage3').on('show', function (e) {
		$("#example_query").removeAttr("disabled");
		var src = "subTabIframes/htxx_View.jsp?id="+id; 
		$("#frame3").attr("src",src);
		iframeid = "frame3";

	});
	$('#tabPage4').on('show', function (e) {
		$("#example_query").attr("disabled","disabled");
		var src = "subTabIframes/xmwd_View.jsp?id="+id; 
		$("#frame4").attr("src",src);
		iframeid = "frame4";

	});
	$('#tabPage5').on('show', function (e) {
		$("#example_query").removeAttr("disabled");
		var src = "subTabIframes/zjxx_View.jsp?id="+id; 
		$("#frame5").attr("src",src);
		iframeid = "frame5";

	});
	//问题提报项目手册信息卡
	$('#tabPage6').on('show', function (e) {
		$("#example_query").removeAttr("disabled");
		var src = "subTabIframes/wttbXmsc.jsp?id="+id; 
		$("#frame6").attr("src",src);
		iframeid = "frame6";

	});
});

	
//页面默认参数
function init(){
	$("#frame0").attr("src","subTabIframes/xmgyxx_View.jsp?id="+id);
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
			<button id="example_query" class="btn btn-link" type="button" onclick="printDiv()"><i class="icon-print"></i>打印</button>
	</p>
<ul class="nav nav-tabs"> 
	<li class="active"><a href="#xmsc1" data-toggle="tab" id="tabPage0">项目概要信息</a></li> 
	<li class=""><a href="#xmsc2" data-toggle="tab" id="tabPage1">项目进展信息</a></li> 
	<li class=""><a href="#xmsc6" data-toggle="tab" id="tabPage5">投资与资金信息</a></li> 
	<li class=""><a href="#xmsc3" data-toggle="tab" id="tabPage2">质量安全检查</a></li> 
	<li class=""><a href="#xmsc4" data-toggle="tab" id="tabPage3">招标合同信息</a></li> 
	<li class=""><a href="#xmsc7" data-toggle="tab" id="tabPage6">存在问题</a></li> 
	<li class=""><a href="#xmsc5" data-toggle="tab" id="tabPage4">项目文档</a></li> 
	
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