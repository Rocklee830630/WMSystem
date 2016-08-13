<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>征收资金管理首页</title>
<%
	String nd = request.getParameter("nd");
	String lb = request.getParameter("lb");
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglBmgkController.do";

var flag, qyID="", cYear;

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
	  	 $(window).manhuaDialog({"title":"导出","type":"text","content":"${pageContext.request.contextPath }/jsp/framework/print/TabListEXP.jsp?tabId=DT1","modal":"3"});
	});
	
});

//页面默认参数
function init(){
	//cYear = $("#QND").val();
	setPageHeight();
	cYear = '<%= nd%>';
	$("#QND").val(cYear);
	
	g_bAlertWhenNoResult = false;
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryQyList&nd="+$("#QND").val(),data,DT1, null, true);
	g_bAlertWhenNoResult = true;
}

//点击获取行对象
function tr_click(obj,tabListid){
	//alert(JSON.stringify(obj));
	qyID = obj.JHSJID;
}


//详细信息
function rowView1(t){
	if($("#DT1").getSelectedRowIndex()==-1)
	 {
		requireSelectedOneRow();
	    return
	 }
	var rowValue = $("#DT1").getSelectedRow();
	var tempJson = convertJson.string2json1(rowValue);
	var shidvar = tempJson.JHSJID;
	$(window).manhuaDialog({"title":"征收资金>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/zszjxx-view.jsp?type=detail&lb=<%=lb%>&nd=<%=nd%>&jhsjid="+shidvar,"modal":"2"});
}


function doRandering_BSBF(obj){
	var showHtml = "";
	if(obj.BSBF!=""){
		showHtml = obj.BSBF_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_JEBF(obj){
	var showHtml = "";
	if(obj.JEBF!=""){
		showHtml = obj.JEBF_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_BSSY(obj){
	var showHtml = "";
	if(obj.BSSY!=""){
		showHtml = obj.BSSY;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_JESY(obj){
	var showHtml = "";
	if(obj.JESY!=""){
		showHtml = obj.JESY_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}


<%--function closeParentCloseFunction(){--%>
<%--	//刷新--%>
<%--	g_bAlertWhenNoResult = false;--%>
<%--	//生成json串--%>
<%--	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);--%>
<%--	//调用ajax插入--%>
<%--	defaultJson.doQueryJsonList(controllername+"?queryQyList",data,DT1);--%>
<%--	g_bAlertWhenNoResult = true;--%>
<%--}--%>

function doRandering(obj){
	var showHtml = "";
	showHtml = "<a href='javascript:rowView1(this);' title='查看详细信息'><i class='icon-file showXmxxkInfo' jhsjid='"+obj.ID+"'></i></a>";
	return showHtml;
}
function doBDMC(obj){
	if(obj.BDMC==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
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
				<% if(!"".equals(nd)){out.print(nd+"年度");}%>征收资金<%if("BF".equals(lb)){out.print("拨付");}else if("SY".equals(lb)){out.print("使用");} %>管理
				
				<span class="pull-right">
      				<button id="btnExpExcel"  noprint="true" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">导出</button>
				</span>
			</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<input class="span12" type="text" id="QND" >
						</TD>
					</TR>
				</table>
			</form>
			<div style="height:5px;"> </div>	
			<div class="overFlowX">
			<%
				if("BF".equals(lb)){
					%>
						<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
			                <thead>
			                	<tr>
			                		<th rowspan="2"  name="XH" id="_XH" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
			                		<th rowspan="2" fieldname="JHSJID" colindex=2 tdalign="center" CustomFunction="doRandering" noprint="true"> &nbsp;&nbsp;</th>
									<th rowspan="2" fieldname="XMMC" colindex=3 tdalign="left" maxlength="40">&nbsp;项目名称&nbsp;</th>
									<th rowspan="2" fieldname="BDMC" colindex=4 tdalign="left" maxlength="40" CustomFunction="doBDMC">&nbsp;标段名称&nbsp;</th>
									<th colspan="2">&nbsp;资金拨付&nbsp;</th>
			                	</tr>
			                	<tr>
			                		<th fieldname="BSBF" colindex=5 tdalign="center" CustomFunction="doRandering_BSBF">&nbsp;笔数&nbsp;</th>
									<th fieldname="JEBF" colindex=6 tdalign="right" CustomFunction="doRandering_JEBF">&nbsp;拨入(元)&nbsp;</th>
			                	</tr>
			                </thead>
			              	<tbody></tbody>
			           </table>
					<%
				}else if("SY".equals(lb)){
					%>
						<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" noPage="true" pageNum="18">
			                <thead>
			                	<tr>
			                		<th rowspan="2"  name="XH" id="_XH" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
			                		<th rowspan="2" fieldname="JHSJID" colindex=2 tdalign="center" CustomFunction="doRandering" noprint="true"> &nbsp;&nbsp;</th>
									<th rowspan="2" fieldname="XMMC" colindex=3 tdalign="left" maxlength="40">&nbsp;项目名称&nbsp;</th>
									<th rowspan="2" fieldname="BDMC" colindex=4 tdalign="left" maxlength="40" CustomFunction="doBDMC">&nbsp;标段名称&nbsp;</th>
									<th colspan="2">&nbsp;资金使用&nbsp;</th>
			                	</tr>
			                	<tr>
									<th fieldname="BSSY" colindex=5 tdalign="center" CustomFunction="doRandering_BSSY">&nbsp;笔数&nbsp;</th>
									<th fieldname="JESY" colindex=6 tdalign="right" CustomFunction="doRandering_JESY">&nbsp;使用(元)&nbsp;</th>
			                	</tr>
			                </thead>
			              	<tbody></tbody>
			           </table>
					<%
				}
			%>
	            
	       </div>
	 	</div>
	</div>     
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
<%--		<input type="hidden" name="txtFilter" order="desc" fieldname="fdt.LRSJ" id="txtFilter"/>--%>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
</div>
</body>
</html>