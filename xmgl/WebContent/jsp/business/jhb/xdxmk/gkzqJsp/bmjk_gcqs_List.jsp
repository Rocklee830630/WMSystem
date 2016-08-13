<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>工程洽商-工程洽商管理</title>
<%
	String nd = request.getParameter("nd");
	String index = request.getParameter("index");
	if(Pub.empty(index)){
		index = "";
	}
	String tcjhztid = request.getParameter("tcjhztid");
	if(Pub.empty(tcjhztid)){
		tcjhztid = "";
	}
	String xmglgs = request.getParameter("xmglgs");
	if(Pub.empty(xmglgs)){
		xmglgs = "";
	}
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/sjzqController.do";
var nd = '<%=nd%>';
var index = '<%=index%>';
var tcjhztid = '<%=tcjhztid%>';
var xmglgs = '<%=xmglgs%>';
var rowIndex;


//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#gcqsList").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	
	setPageHeight();
	init();
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 if(exportRequireQuery($("#gcqsList"))){//该方法需传入表格的jquery对象
		      printTabList("gcqsList");
		  }
	});
	
});
//页面默认参数
function init(){

	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}

function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcqsList);
	//调用ajax插入
	//defaultJson.doQueryJsonList(controllername+"?query",data,gcqsList);
	defaultJson.doQueryJsonList(controllername+"?query_gcqs&nd="+nd+"&index="+index+"&xmglgs="+xmglgs,data,gcqsList,"aa",false);
	/* $("#sp_btn")[0].innerText = "呈请审批";
	$("#btnUpdate").removeAttr("disabled"); */
	
}
function aa(){
	
}
//详细信息
function rowView(index){
	var obj = $("#gcqsList").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).XMID;
	$(window).manhuaDialog(xmscUrl(id));
}
//标段图标样式
function doBdmc(obj){
	if(obj.XMBS == "0"){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}

//标段编号图标样式
function doBdbh(obj){
	if(obj.BDID == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDBH;
	}
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">工程洽商
      	<span class="pull-right">
        	<button id="btnExpExcel" class="btn"  type="button">导出</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
      </table>
      </form>
<div style="height:5px;"> </div>
<div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="gcqsList" width="100%" type="single" pageNum="10">
		<thead>
			<tr>
				<th  name="XH" id="_XH" >&nbsp;#&nbsp;</th>
				<th fieldname="QSBT" maxlength="15">&nbsp;洽商标题&nbsp;</th>
				<th fieldname="XMBH" hasLink="true" linkFunction="rowView" >&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDBH" CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" maxlength="15" CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
				<th fieldname="SJDW" maxlength="15">&nbsp;设计单位&nbsp;</th>
				<th fieldname="JLDW" maxlength="15">&nbsp;监理单位&nbsp;</th>
				<th fieldname="SGDW" maxlength="15">&nbsp;施工单位&nbsp;</th>
				<th fieldname="GSZJ" tdalign="right">&nbsp;估算造价&nbsp;</th>
				<th fieldname="SFSJPQ" tdalign="center">&nbsp;是否涉及排迁&nbsp;</th>
				<th fieldname="QSTCRQ" tdalign="center">&nbsp;洽商提出日期&nbsp;</th>
			</tr>
		</thead>
		<tbody>
           </tbody>
	</table>
</div>
		</div>
	</div>
   </div>
    
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="DESC" fieldname = "t.LRSJ"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>