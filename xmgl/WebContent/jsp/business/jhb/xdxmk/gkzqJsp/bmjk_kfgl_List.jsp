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
<title>工程部-开复工令管理</title>
<%
	//获取当前用户信息
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
	String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/sjzqController.do";
var nd = '<%=nd%>';
var index = '<%=index%>';
var tcjhztid = '<%=tcjhztid%>';
var xmglgs = '<%=xmglgs%>';
//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#kfglList").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	
	setPageHeight();
	
	init();
	
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 if(exportRequireQuery($("#kfglList"))){//该方法需传入表格的jquery对象
		      //printTabList("kfglList");
		      printTabList("kfglList","kfgl.xls","XMBH,XMMC,BDMC,XMLX,M_KGSJ,M_FGSJ,M_TGSJ","3,1");
		  }
	});
	
   
});
//页面默认参数
function init(){
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}
//详细信息
function rowView(index){
	var obj = $("#kfglList").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).GC_TCJH_XMXDK_ID;
	$(window).manhuaDialog(xmscUrl(id));
}

//查询列表
function queryList(){
	if(tcjhztid != null && tcjhztid != ""){
		$("#jhid").val(tcjhztid);
	}
	if(index != null && index != ""){
		if(index == "1" || index =="2" || index =="3" || index == "4" || index == "5" || index =="6"){
			$("#xmbs").val("0");
		}
	}
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,kfglList);
	//调用ajax插入
	//defaultJson.doQueryJsonList(controllername+"?query&xmglgs="+xmglgs,data,kfglList);	
	defaultJson.doQueryJsonList(controllername+"?query_kfgl&nd="+nd+"&index="+index+"&xmglgs="+xmglgs,data,kfglList,"aa",false);
}
function aa(){
	
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
	if(obj.BDBH == ""){
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
      <h4 class="title">开复工令管理
      	<span class="pull-right">
      		<button id="btnExpExcel" class="btn"  type="button">导出</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			</TD>
        </TR>
      </table>
      </form>
    <div style="height:5px;"> </div>
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="kfglList" width="100%" type="single" pageNum="10" editable="0">
		<thead>
			<tr>
				<th  name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
				<th fieldname="XMBH" rowspan="2" colindex=2 hasLink="true" linkFunction="rowView" rowMerge="true">&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" maxlength="20" rowspan="2" colindex=3 rowMerge="true">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDBH" rowspan="2" colindex=4 CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" maxlength="20" rowspan="2" colindex=5 CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
				<th fieldname="XMLX" rowspan="2" colindex=6 tdalign="center">&nbsp;项目类型&nbsp;</th>
				<th colspan="2">开工</th>
				<th colspan="2">复工</th>
				<th colspan="2">停工</th>
			</tr>
			<tr>
				<th fieldname="M_KGSJ" colindex=7 tdalign="center">&nbsp;办理时间&nbsp;</th>
				<th fieldname="M_KGFJ" colindex=8 tdalign="center">&nbsp;附件&nbsp;</th>
				<th fieldname="M_FGSJ" colindex=9 tdalign="center">&nbsp;办理时间&nbsp;</th>
				<th fieldname="M_FGFJ" colindex=10 tdalign="center">&nbsp;附件&nbsp;</th>
				<th fieldname="M_TGSJ" colindex=11 tdalign="center">&nbsp;办理时间&nbsp;</th>
				<th fieldname="M_TGFJ" colindex=12 tdalign="center">&nbsp;附件&nbsp;</th>
			</tr>
		</thead>
		<tbody></tbody>
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
       <input type="hidden" name="txtFilter"  order="asc" fieldname = "t.xmbh,t1.xmbs,t1.pxh"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>