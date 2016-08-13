
<%@page import="com.ccthanking.framework.common.DBUtil"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.ibm.icu.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%><!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%
			String nd = request.getParameter("nd");
			String jnfs = request.getParameter("jnfs");
			String fhqk = request.getParameter("fhqk");
			String tstj = request.getParameter("tstj");
			SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String currDate = s.format(date);
			Calendar rightNow = Calendar.getInstance();
			rightNow.add(Calendar.MONTH,1);
			String syDate = s.format(rightNow.getTime());
		%>
		<app:base />
		<title>履约保证金首页</title>
		<script
			src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
		<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglLybzjController.do";
var tstj = '<%=tstj%>';
var currDate = '<%=currDate%>';
var syDate = '<%=syDate%>';
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

    
    $("a[id^='view_detail_']").bind("click", function(){
    	rowView1();
	});
  
	
});

//页面默认参数
function init(){
	if(tstj=='1'){
		$("#QBHYXQZ1").val('<%=currDate%>');
		$("#QJNFS").val('BH');
	}else if(tstj=='2'){
		$("#QBHYXQZ1").val('<%=currDate%>');
		$("#QBHYXQZ1").attr('operation','>');
		$("#QBHYXQZ2").val('<%=syDate%>');
		$("#QJNFS").val('BH');
	}else{
		$("#QND").val('<%=nd%>');
		$("#QJNFS").val('<%=jnfs%>');
		$("#QFHQK").val('<%=fhqk%>');
	}
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,DT1, null, true);
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
	var shidvar = tempJson.SJBH;
	$(window).manhuaDialog({"title":"履约保证金>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/lybzj-view.jsp?type=detail&sjbh="+shidvar,"modal":"2"});
}
function doRandering(obj){
	var showHtml = "";
	showHtml = "<a href='javascript:rowView1(this);' title='查看履约保证金信息'><i class='icon-file showXmxxkInfo' jhsjid='"+obj.ID+"'></i></a>";
	return showHtml;
}
//状态颜色判断
function docolor(obj)
{
	var xqzt=obj.FHQK;
	if(xqzt=="0"){
		if(obj.JNFS=="BH"){
			return '<span class="label label-danger">已办理</span>';
		}else{
			return '<span class="label label-danger">'+obj.FHQK_SV+'</span>';
		}
	}else if(xqzt=="1"){
		if(obj.EVENTSJBH=="5" || obj.EVENTSJBH=="6" || obj.EVENTSJBH=="7"){
			return '<span class="label label-success">'+obj.EVENTSJBH_SV+'</span>';
		}else{
			return '<span class="label label-success">'+obj.FHQK_SV+'</span>';
		}
	}else if(xqzt=="2"){
	  	return '<span class="label label-danger">'+obj.FHQK_SV+'</span>';
	}else if(xqzt=="6"){
	 	return '<span class="label label-info">'+obj.FHQK_SV+'</span>';
	}
	
}

function do_view(obj){
	if(obj.JNFS=="BH"){
		return '<span title="'+obj.JNRQ_SV+'">'+obj.BHYXQS_SV+"至"+obj.BHYXQZ_SV+'</span>';
	}else{
		return obj.JNRQ_SV;
	}
}
function do_bhlx(obj){
	if(obj.JNFS=="BH"){
		if(obj.BHLX!=''){
			return obj.BHLX_SV+""+obj.JNFS_SV;
		}else{
			return obj.JNFS_SV;
		}
	}else{
		return obj.JNFS_SV;
	}
}
function doBDMC(obj){
	if(obj.BDMC==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}
function doHTMC(obj){
	if(obj.HTMC==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.HTMC;
	}
}
function doFHRQ(obj){
	if(obj.FHRQ==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.FHRQ;
	}
}
function doHTBM(obj){
	if(obj.HTBM==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.HTBM;
	}
}
function doJNDW(obj){
	if(obj.JNDW==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.JNDW_SV;
	}
}
</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<p></p>
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						<span class="pull-right"> 
								<button id="btnExpExcel" class="btn" type="button"
									style="font-family: SimYou, Microsoft YaHei; font-weight: bold;">
									导出
								</button> </span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<input class="span12" type="text" 
										 id="QND" name="JNRQ" fieldname="to_char(JNRQ,'yyyy')"
										operation="="  >
									<input class="span12" type="text" 
										 id="QJNFS" name="JNFS" fieldname="JNFS"
										operation="="  >
									<input class="span12" type="text" 
										 id="QFHQK" name="FHQK" fieldname="FHQK"
										operation="="  >
									<input class="date" type="date"
										id="QBHYXQZ1" name="BHYXQZ1" fieldname="BHYXQZ"
									 	operation="<"  fieldtype="date" fieldformat="YYYY-MM-DD">
									<input  class="date" type="date" 
										id="QBHYXQZ2" name="BHYXQZ2" fieldname = "BHYXQZ" 
										operation="<" fieldtype="date" fieldformat="YYYY-MM-DD">
									 
<%--								<input class="span12" type="text" --%>
<%--									 id="QFHQK" name="FHQK" fieldname="FHQK"--%>
<%--									operation="="  >--%>
								</TD>
							</TR>
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
						<table width="100%" class="table-hover table-activeTd B-table"
							id="DT1" type="single" pageNum="18">
							<thead>
								<tr>
									<th name="XH" id="_XH" colindex=1 tdalign="center">
										&nbsp;#&nbsp;
									</th>
									<th fieldname="ID" tdalign="center" colindex=2
										CustomFunction="doRandering" noprint="true">
										&nbsp;&nbsp;
									</th>
									<th fieldname="FHQK" colindex=3 tdalign="center"
										CustomFunction="docolor">
										&nbsp;状态&nbsp;
									</th>
									
									<th fieldname="XMMC" maxlength="20" colindex=4  tdalign="left">
										&nbsp;项目名称&nbsp;
									</th>
									<th fieldname="BDMC" maxlength="20" colindex=5 tdalign="left" CustomFunction="doBDMC">
										&nbsp;标段名称&nbsp;
									</th>
									<th fieldname="JNFS" colindex=6 tdalign="center" CustomFunction="do_bhlx">
										&nbsp;交纳方式&nbsp;
									</th>
<%--									<th fieldname="BHLX" colindex=7 tdalign="center" CustomFunction="do_bhlx">--%>
<%--										&nbsp;保函性质&nbsp;--%>
<%--									</th>--%>
									<th fieldname="JE" colindex=8 tdalign="right">
										&nbsp;金额(元)&nbsp;
									</th>
									<th fieldname="JNRQ" colindex=9 tdalign="center" CustomFunction="do_view">
										&nbsp;交纳日期&nbsp;
									</th>
									<th fieldname="FHRQ" colindex=10 tdalign="center" CustomFunction="doFHRQ">
										&nbsp;返还日期&nbsp;
									</th>
									<th fieldname="HTMC" colindex=11 maxlength="20" tdalign="left" CustomFunction="doHTMC">
										&nbsp;合同名称&nbsp;
									</th>
									<th fieldname="HTBM" colindex=12 tdalign="center" CustomFunction="doHTBM">
										&nbsp;合同编码&nbsp;
									</th>
									<th fieldname="JNDW" colindex=13 tdalign="left"
										maxlength="30" CustomFunction="doJNDW">
										&nbsp;交纳单位&nbsp;
									</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" />
				<input type="hidden" name="txtXML" />
				<input type="hidden" name="txtFilter" order="desc"
					fieldname="t.gxsj" id="txtFilter" />
				<%--				<input type="hidden" name="txtFilter" order="desc" fieldname="ID" id="txtFilter"/>--%>
				<input type="hidden" name="resultXML" id="resultXML" />
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData" />
				<input type="hidden" name="queryResult" id="queryResult" />
			</FORM>
		</div>
	</body>
</html>