
<%@page import="java.sql.Connection"%>
<%@page import="com.ccthanking.framework.common.DBUtil"%><!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%
			String nd = request.getParameter("nd");
			String htzt = request.getParameter("htzt");
			String htlx = request.getParameter("htlx");
		%>
		<app:base />
		<title>合同首页</title>
<style type="text/css">
.round_normal{width:16px;height:16px;display: inline-block;font-size:20px;line-heigth:16px;text-align:center;color:green;text-decoration:none}
.round_error{width:16px;height:16px;display: inline-block;font-size:20px;line-heigth:16px;text-align:center;color:red;text-decoration:none}
.round_warning{width:16px;height:16px;display: inline-block;font-size:20px;line-heigth:16px;text-align:center;color:yellow;text-decoration:none}
</style>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtController.do";

//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(3)-pageNumHeight;
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
		// {
		//	 xAlert("提示信息","请至少查询出一条记录！");
		//	 return;
		// }
	  	 $(window).manhuaDialog({"title":"导出","type":"text","content":"${pageContext.request.contextPath }/jsp/framework/print/TabListEXP.jsp?tabId=DT1","modal":"3"});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
	
    
    $("#btnView").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		rowView($("#DT1").getSelectedRowIndex());
	});
	
});

//页面默认参数
function init(){
	$("#QQDNF").val('<%=nd %>');
	$("#QHTZT").val('<%=htzt %>');
	$("#QHTLX").val('<%=htlx %>');
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,DT1);
}
//点击获取行对象
function tr_click(obj,tabListid){
	$("#htid").val(obj.ID);
	//alert(JSON.stringify(obj));
	if(obj.HTZT=="0"){
		$("#btnQdsp").removeAttr("disabled");
		//$("#btnUpdate").attr("disabled",true);
	}else if(obj.HTZT=="1"){
		//$("#btnUpdate").removeAttr("disabled");
		$("#btnQdsp").attr("disabled",true);
	}else{
		$("#btnQdsp").attr("disabled",true);
		//$("#btnUpdate").attr("disabled",true);
	}
}

//回调函数--用于修改新增
getWinData = function(data){
	//var subresultmsgobj = defaultJson.dealResultJson(data);
	var data1 = defaultJson.packSaveJson(data);
	if(JSON.parse(data).ID == "" || JSON.parse(data).ID == null){
		defaultJson.doInsertJson(controllername + "?insert", data1,DT1);
	}else{
		defaultJson.doUpdateJson(controllername + "?update", data1,DT1);
	}
	
};

//详细信息
function rowView(index){
	$("#DT1").setSelect(index);
	if($("#DT1").getSelectedRowIndex()==-1)
	 {
		requireSelectedOneRow();
	    return
	 }
	$("#resultXML").val($("#DT1").getSelectedRow());
	$(window).manhuaDialog({"title":"合同>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/ht-view.jsp?type=detail","modal":"1"});
}
//状态颜色判断
function docolor(obj)
{
	var xqzt=obj.HTZT;
	if(xqzt=="-3"){
		return '<span class="label label-warning">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="-2"){
	 	return '<span class="label label-warning">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="-1"){
	 	return '<span class="label label-warning">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="0"){
	 	return '<span class="label label-danger">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="1"){
	  	return '<span class="label label-success">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="2"){
	  	return '<span class="label label-info">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="3"){
	  	return '<span class="label label-info">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="4"){
	  	return '<span class="label label-info">'+obj.HTZT_SV+'</span>';
	}else{
		return obj.HTZT_SV;
	}
	
}


function doRandering_ZWCZF(obj){
	var showHtml = "";
	if(obj.ZWCZF!=0){
		showHtml = obj.ZWCZF_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_ZHTZF(obj){
	var showHtml = "";
	if(obj.ZHTZF!=0){
		showHtml = obj.ZHTZF_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_ZBGJE(obj){
	var showHtml = "";
	if(obj.ZBGJE!=0){
		showHtml = obj.ZBGJE_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doSFDXM(obj){
	if(obj.SFDXMHT=='2'){
		return '否';	
	}else{
		return obj.SFDXMHT_SV;
	}
}
function doZT(obj){
	return '';
}
function doRandering(obj){
	var showHtml = "";
	showHtml = "<a href='javascript:rowView1(this);' title='查看详细信息'><i class='icon-file showXmxxkInfo'></i></a>";
	return showHtml;
}
function rowView1(t){
	if($("#DT1").getSelectedRowIndex()==-1)
	 {
		requireSelectedOneRow();
	    return
	 }
	$("#resultXML").val($("#DT1").getSelectedRow());
	var rowValue = $("#DT1").getSelectedRow();
	var tempJson = convertJson.string2json1(rowValue);
	var idvar = tempJson.ID;
	$(window).manhuaDialog({"title":"已签订合同>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/ht-view.jsp?type=detail","modal":"1"});
}
function doHTBM(obj){
	if(obj.HTBM==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.HTBM;
	}
	
}
function doYFDW(obj){
	if(obj.YFDW==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.YFDW;
	}
	
}
function doZBBM(obj){
	if(obj.ZBBM==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.ZBBM;
	}
	
}
function doHTSJKSRQ(obj){
	if(obj.HTSJKSRQ==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.HTSJKSRQ;
	}
	
}
function doHTJSRQ(obj){
	if(obj.HTJSRQ==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.HTJSRQ;
	}
	
}
function doHTJQDRQ(obj){
	if(obj.HTJQDRQ==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.HTJQDRQ;
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
							</button> 
						</span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<INPUT type="text" class="span12" kind="text" id="num"
										fieldname="rownum" value="1000" operation="<=" />
									<INPUT id="QQDNF" name="QDNF"
										fieldname="ghh.QDNF" operation="=" />
									<INPUT id="QHTZT" name="HTZT"
										fieldname="ghh.HTZT" operation=">" />
									<INPUT id="QHTLX" name="HTLX"
										fieldname="ghh.HTLX" operation="=" />
								</TD>
							</tr>
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
						<table width="100%" class="table-hover table-activeTd B-table"
							id="DT1" type="single" pageNum="18">
							<thead>
								<tr>
									<th rowspan="2" name="XH" id="_XH" style="width: 10px"
										colindex=1 tdalign="center">
										&nbsp;#&nbsp;
									</th>
									<th rowspan="2" fieldname="ID" tdalign="center" colindex=2 
										CustomFunction="doRandering" noprint="true">
										&nbsp;&nbsp;
									</th>
									<th rowspan="2" fieldname="HTZT" colindex=3 tdalign="center"
										CustomFunction="docolor">
										&nbsp;合同状态&nbsp;
									</th>
									<th rowspan="2" fieldname="HTBM" colindex=4 tdalign="left"
										maxlength="30" CustomFunction="doHTBM">
										&nbsp;合同编码&nbsp;
									</th>
									<th rowspan="2" fieldname="HTMC" colindex=5 tdalign="left"
										maxlength="30">
										&nbsp;合同名称&nbsp;
									</th>
									<th rowspan="2" fieldname="HTLX" colindex=6 tdalign="center">
										&nbsp;合同类型&nbsp;
									</th>
									<th colspan="3">
										&nbsp;合同价（元）&nbsp;
									</th>
									<th rowspan="2" fieldname="ZWCZF" colindex=10 tdalign="right"
										CustomFunction="doRandering_ZWCZF">
										&nbsp;完成投资&nbsp;
									</th>
									<th rowspan="2" fieldname="ZHTZF" colindex=11 tdalign="right"
										CustomFunction="doRandering_ZHTZF">
										&nbsp;合同支付&nbsp;
									</th>
									<th rowspan="2" fieldname="SFXNHT" colindex=12 tdalign="center">
										&nbsp;是否虚拟&nbsp;
									</th>
									<th rowspan="2" fieldname="SFDXMHT" colindex=13 tdalign="center" CustomFunction="doSFDXM">
										&nbsp;单项目&nbsp;
									</th>
									<th rowspan="2" fieldname="SFZFJTZ" colindex=14 tdalign="center">
										&nbsp;支付即投资&nbsp;
									</th>
									<th rowspan="2" fieldname="YFDW" colindex=15 tdalign="left"
										maxlength="36" CustomFunction="doYFDW">
										&nbsp;乙方单位&nbsp;
									</th>
									<th rowspan="2" fieldname="ZBBM" colindex=16 tdalign="center"
										maxlength="36" CustomFunction="doZBBM">
										&nbsp;主办部门&nbsp;
									</th>
									<th rowspan="2" fieldname="HTSJKSRQ" colindex=17
										tdalign="center" CustomFunction="doHTSJKSRQ">
										&nbsp;合同开始日期&nbsp;
									</th>
									<th rowspan="2" fieldname="HTJSRQ" colindex=18 tdalign="center" CustomFunction="doHTJSRQ">
										&nbsp;合同结束日期&nbsp;
									</th>
									<th rowspan="2" fieldname="HTJQDRQ" colindex=19
										tdalign="center" CustomFunction="doHTJSRQ">
										&nbsp;合同签订日期&nbsp;
									</th>
								</tr>
								<tr>
									<th fieldname="ZHTQDJ" colindex=7 tdalign="right">
										&nbsp;合同签订价&nbsp;
									</th>
									<th fieldname="ZBGJE" colindex=8 tdalign="right"
										CustomFunction="doRandering_ZBGJE">
										&nbsp;变更金额&nbsp;
									</th>
									<th fieldname="ZZXHTJDO" colindex=9 tdalign="right">
										&nbsp;最新合同价&nbsp;
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
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="to_number(htzt)" id="txtFilter" />
				<input type="hidden" name="resultXML" id="resultXML" />
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData" />
				<input type="hidden" name="queryResult" id="queryResult" />
			</FORM>
		</div>
		<input type="hidden" id="htid" />
	</body>
</html>