<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>部门提请款首页</title>
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
	String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/sjzqController.do";
var nd = '<%=nd%>';
var index = '<%=index%>';
var sysdate = '<%=sysdate %>';
var xmglgs = '<%=xmglgs%>';
//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	setPageHeight();
	init();

	
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		// var t = $("#DT1").getTableRows();
		// if(t<=0)
		 //{
		//	 xAlert("提示信息","请至少查询出一条记录！");
		//	 return;
		// }
	  	 $(window).manhuaDialog({"title":"导出","type":"text","content":"${pageContext.request.contextPath }/jsp/framework/print/TabListEXP.jsp?tabId=DT1","modal":"3"});
	});
	
	
});

//页面默认参数
function init(){
	//生成json串
	g_bAlertWhenNoResult = false;
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	//defaultJson.doQueryJsonList(controllername+"?query_tqk",data,DT1,null,true);
	defaultJson.doQueryJsonList(controllername+"?query_tqk&nd="+nd+"&index="+index+"&xmglgs="+xmglgs,data,DT1,"aa",false);
	g_bAlertWhenNoResult = true;
}

function aa(){
	
}


//详细信息
function rowView(index){
	$("#DT1").setSelect(index);
	if($("#DT1").getSelectedRowIndex()==-1)
	 {
		requireSelectedOneRow();
	    return
	 }
	$("#resultXML").val($("#DT1").getSelectedRow());
	$(window).manhuaDialog({"title":"部门提请款>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqkbm-add.jsp?type=detail","modal":"1"});
}
function rowView1(t){
	if($("#DT1").getSelectedRowIndex()==-1)
	 {
		requireSelectedOneRow();
	    return
	 }
	
	var rowValue = $("#DT1").getSelectedRow();
	var tempJson = convertJson.string2json1(rowValue);
	var idvar = tempJson.ID;
	
	$(window).manhuaDialog({"title":"部门提请款>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqkbm-view.jsp?type=detail&id="+idvar,"modal":"1"});
}

function doRandering(obj){
	var showHtml = "";
	showHtml = "<a href='javascript:rowView1(this);' title='查看详细信息'><i class='icon-file showXmxxkInfo'></i></a>";
	return showHtml;
}


function doRandering_bmze(obj){
	var showHtml = "";
	if(obj.BMZE!=""){
		showHtml = obj.BMZE_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_cwze(obj){
	var showHtml = "";
	if(obj.CWZE!=""){
		showHtml = obj.CWZE_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_jcze(obj){
	var showHtml = "";
	if(obj.JCZE!=""){
		showHtml = obj.JCZE_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_CWJLS(obj){
	var showHtml = "";
	if(obj.CWJLS!="0"){
		showHtml = obj.CWJLS;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_JCJLS(obj){
	var showHtml = "";
	if(obj.JCJLS!="0"){
		showHtml = obj.JCJLS;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}

//状态颜色判断
function docolor(obj)
{
	var xqzt=obj.TQKZT;
	if(xqzt=="1"){
		return '<span class="label label-danger">'+obj.TQKZT_SV+'</span>';
	}else if(xqzt=="2"){
	  	return '<span class="label label-warning">'+obj.TQKZT_SV+'</span>';
	}else if(xqzt=="3"){
	  	return '<span class="label label-danger">'+obj.TQKZT_SV+'</span>';
	}else if(xqzt=="4"){
	  	return '<span class="label label-success">'+obj.TQKZT_SV+'</span>';
	}else if(xqzt=="5"){
	  	return '<span class="label label-success">'+obj.TQKZT_SV+'</span>';
	}else if(xqzt=="6"){
	 	return '<span class="label label-success">'+obj.TQKZT_SV+'</span>';
	}else if(xqzt=="7"){
		return '<span class="label label-success">'+obj.TQKZT_SV+'</span>';
	}else{
		return obj.TQKZT_SV;
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
			<h4 class="title">
				部门提请款
				<span class="pull-right">  
      				<button id="btnExpExcel" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">导出</button>
				</span>
			</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
						</TD>
					</TR>
				</table>
			</form>
			<div style="height:5px;"> </div>	
			<div class="overFlowX">
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single"  pageNum="18">
	                <thead>
	                	<tr>
	                		<th rowspan="2"  name="XH" id="_XH"  colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
	                		<th rowspan="2" fieldname="ID" tdalign="center" colindex=2 CustomFunction="doRandering" noprint="true">&nbsp;&nbsp;</th>
	                		<th rowspan="2" fieldname="TQKZT" colindex=3 tdalign="center" CustomFunction="docolor">&nbsp;状态&nbsp;</th>
	                		<th rowspan="2" fieldname="QKNF" colindex=4 tdalign="center" maxlength="30" >&nbsp;年份&nbsp;</th>
	                		<th rowspan="2" fieldname="GCPC" colindex=5 tdalign="center" maxlength="30" >&nbsp;月份&nbsp;</th>
							<th rowspan="2" fieldname="QKLX" colindex=6 tdalign="center" maxlength="30" >&nbsp;请款类型&nbsp;</th>
							<th colspan="2">&nbsp;部门请款情况&nbsp;</th>
							<th colspan="2">&nbsp;财务审核情况&nbsp;</th>
							<th colspan="2">&nbsp;计财审定情况&nbsp;</th>
							<th rowspan="2" fieldname="BZRQ" colindex=13 tdalign="center">&nbsp;编制日期&nbsp;</th>
	                	</tr>
	                	<tr>
	                		<th fieldname="BMJLS" colindex=7 tdalign="center">&nbsp;明细数&nbsp;</th>
							<th fieldname="BMZE" colindex=8 tdalign="right" CustomFunction="doRandering_bmze">&nbsp;总额(元)&nbsp;</th>
							<th fieldname="CWJLS" colindex=9 tdalign="center" CustomFunction="doRandering_CWJLS">&nbsp;明细数&nbsp;</th>
							<th fieldname="CWZE" colindex=10 tdalign="right" CustomFunction="doRandering_cwze">&nbsp;总额(元)&nbsp;</th>
							<th fieldname="JCJLS" colindex=11 tdalign="center" CustomFunction="doRandering_JCJLS">&nbsp;明细数&nbsp;</th>
							<th fieldname="JCZE" colindex=12 tdalign="right" CustomFunction="doRandering_jcze">&nbsp;总额(元)&nbsp;</th>
	                	</tr>
	                </thead>
	              	<tbody></tbody>
	           </table>
	       </div>
	 	</div>
	</div>     
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" fieldname="t.LRSJ" id="txtFilter"/>
<%--		<input type="hidden" name="txtFilter" order="desc" fieldname="ID" id="txtFilter"/>--%>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
</div>
 <form method="post" id="queryFormTqkBmChange">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border">
							<input type="hidden" id="QID" name="ID"  fieldname="ID" >
							<input type="hidden" id="QTQKZT" name="TQKZT"  fieldname="TQKZT" >
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
				</table>
	</form>
</body>
</html>