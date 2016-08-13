<!DOCTYPE html>
<html>
	<%@ page language="java" pageEncoding="UTF-8"%>
	<%@ taglib uri="/tld/base.tld" prefix="app"%>
	<%@ page import="com.ccthanking.framework.util.*"%>
	<%@ page import="com.ccthanking.framework.common.*"%>
	<%@ page import="java.lang.StringBuffer"%>
	<%@ page import="java.util.*"%>
	<%@ page import="com.ccthanking.framework.Globals"%>
	<%@ page import="com.ccthanking.framework.plugin.AppInit"%>
	<%@ page import="java.sql.Connection"%>
	<%@ page import="java.text.DateFormat"%>
	<%@ page import="java.text.SimpleDateFormat"%>
	<%
	Connection conn = DBUtil.getConnection();//定义连接
	StringBuffer sbSql = null;//sql语句字符串
	String sql = "";//查询参数字符串
	String year = Pub.getDate("yyyy", new Date());
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName= dept.getDept_Name();
	Date d=new Date();//获取时间
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//转换格式
	String today=sdf.format(d);
%>
	<app:base />
	<head>
		<title>部长概况</title>
		<script type="text/javascript"
			src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
		<style type="text/css">
body {
	font-size: 14px;
}

h2 {
	display: inline;
	line-height: 2em;
}

.table2 { /* 	border-left: #000 solid 1px;
	border-top: #000 solid 1px;
 */
	margin: 10px auto;
}

.marginBottom15px {
	margin-bottom: 15px;
}

.table2 tr td,.table2 tr th {
	line-height: 1.5em;
	padding: 4px;
	/* border-right: #000 solid 1px; */
	border-bottom: #ccc solid 1px;
}

input[type='text'] {
	vertical-align: middle;
	height: 20px;
	line-height: 16px;
	padding: 2px;
}

.table1 {
	
}

.table1 tr td,.table1 tr th {
	line-height: 1.5em;
	padding: 4px;
	border-right: #000 solid 0;
	border-bottom: #000 solid 0;
}

.table3 {
	
}

.table3 tr td,.table3 tr th {
	line-height: 1.5em;
	padding: 4px;
	border-right: #000 solid 0;
	border-bottom: #000 solid 0;
}

.table3 tr th {
	border-top: #000 solid 1px;
	border-bottom: #000 solid 1px;
}

.table4 {
	border: #000 solid 1px;
}

.table4 tr td,.table4 tr th {
	line-height: 1.5em;
	padding: 4px;
	border-right: #000 solid 0;
	border-bottom: #000 solid 0;
}

#DT1 tr{
	height:36px;
}
</style>
<script type="text/javascript"><!--
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtsjController.do";
var controllernameHt= "${pageContext.request.contextPath }/htgl/gcHtglHtController.do";

//初始化查询
$(document).ready(function(){
	var fuyemian=$(window).manhuaDialog.getParentObj();
	var rowValue = fuyemian.$("#resultXML").val();
	var tempJson = convertJson.string2json1(rowValue);
	var xmid = tempJson.XMID;
	//alert(xmid);
	$('#QXMID').val(xmid);
	$('#xmmcSpan').text(tempJson.XMMC);
	init();
	
	
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryTtmxForm,frmPost_htmx,htmxList);
		var flag = defaultJson.doQueryJsonList(controllernameHt+"?queryTzjkHtList",data,htmxList,null,true);
		if(flag){
		}
	});
	
 });

function doType(obj){
	var showHtml = '';
	if(obj.TZLX=='gc'){
		showHtml = '工程费用';
	}else if(obj.TZLX=='zc'){
		showHtml = '征拆费用';
	}else if(obj.TZLX=='qt'){
		showHtml = '其他费用';
	}else if(obj.TZLX=='zj'){
		showHtml = '总计';
	}
	return showHtml;
}

function doType_htmx(obj){
	var showHtml = '';
	if(obj.TZLX=='gc'){
		showHtml = '工程费用';
	}else if(obj.TZLX=='zc'){
		showHtml = '征拆费用';
	}else if(obj.TZLX=='qt'){
		showHtml = '其他费用';
	}else if(obj.TZLX=='zj'){
		showHtml = '所有';
	}
	return showHtml;
}

function doType_htmx_lx(obj){
	var showHtml = '';
	if(obj.TZLX=='gc'){
		showHtml = 'SG';
	}else if(obj.TZLX=='zc'){
		showHtml = 'CQ';
	}else if(obj.TZLX=='qt'){
		showHtml = 'QT';
	}else if(obj.TZLX=='zj'){
		showHtml = '';
	}
	return showHtml;
}

function init(){
	
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	var flag_c =  defaultJson.doQueryJsonList(controllername+"?queryXmtzmxfx",data,DT1,null,true);
	
	var action1 = "${pageContext.request.contextPath }/XmtzDtfxServlet?xmid="+$("#QXMID").val();

	$.ajax({
		url : action1,
		
		//contentType:'application/json;charset=UTF-8',	    
		success: function(xml){ 
	        var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Column2D.swf", "myChartIdxmsl1", "98%", "98%"); 
	        myChart.setDataXML(xml);  
	      myChart.render("chartdiv");  
	       },
	    error : function(xml) {
	    	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Column2D.swf", "myChartIdxmsl1", "98%", "98%");
	    	myChart.setDataXML("");  
		      myChart.render("chartdiv");  
	    }
	});
	
	if(flag_c){
		//alert("首面");
		//alert($("#DT1 tbody tr").length);
		$("#DT1").setSelect(3);
		//alert($("#DT1").getSelectedRow());
		
		var rowValue = $("#DT1").getSelectedRowJsonByIndex(3);
		var obj = convertJson.string2json1(rowValue);
		var name = doType_htmx(obj);
		var name1=$("#htmx_name_Span");
		name1[0].innerHTML=name;
		var gs=$("#htmx_gs_Span");
		gs[0].innerHTML=obj.HTS;
		
		$("#Q_htmx_XMID").val(obj.XMID);
		//queryHtmx();
		queryHtmx();
	}
}

function queryHtmx(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryTtmxForm,frmPost_htmx,htmxList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllernameHt+"?queryTzjkHtList",data,htmxList,null,true);
}

//点击获取行对象
function tr_click(obj,tabListid){
	//alert(JSON.stringify(obj));
	if("DT1"==tabListid){
		
		var name = doType_htmx(obj);
		var name1=$("#htmx_name_Span");
		name1[0].innerHTML=name;
		var gs=$("#htmx_gs_Span");
		gs[0].innerHTML=obj.HTS;
		
		$("#Q_htmx_XMID").val(obj.XMID);
		
		var htlx = doType_htmx_lx(obj);
		if(htlx=="SG" || htlx=="CQ" ){
			//operation  为 = 
			$("#Q_htmx_HTLX").attr("operation", "=");
			$("#Q_htmx_HTLX").val(htlx);
			$("#Q_htmx_HTLX_1").remove();
		}else if(htlx=="QT"){
			//operation  为  not in
			$("#Q_htmx_HTLX").attr("operation", "!=");
			$("#Q_htmx_HTLX").val("SG");
			$("#Q_htmx_HTLX_2").attr("id", "Q_htmx_HTLX_1").appendTo($("#queryTtmxForm"));
		}else{
			$("#Q_htmx_HTLX").attr("operation", "=");
			$("#Q_htmx_HTLX").val("");
			$("#Q_htmx_HTLX_1").remove();
		}
		
		
		//queryHtmx();
		$("#btnQuery").click();
		
	}else{
		
		//$("#hthtzfDetailForm").setFormValues(obj);
	}
}

function doRandering_ZZWCZF(obj){
	var showHtml = "";
	if(obj.ZZWCZF!=""){
		showHtml = '<div style="width:130px;">'+obj.ZZWCZF_SV+'</div>';
	}else{
		showHtml = '<div style="width:130px;text-align:center">—</div>';
	}
	return showHtml;
}

function doRandering_ZZHTQDJ(obj){
	var showHtml = "";
	if(obj.ZZHTQDJ!=""){
		showHtml = '<div s130px"width:130px;">'+obj.ZZHTQDJ_SV+'</div>';
	}else{
		showHtml = '<div style="width:130px;text-align:center">—</div>';
	}
	return showHtml;
}

function doRandering_ZZHTZF(obj){
	var showHtml = "";
	if(obj.ZZHTZF!=""){
		showHtml = '<div style="width:130px;">'+obj.ZZHTZF_SV+'</div>';
	}else{
		showHtml = '<div style="width:130px;text-align:center">—</div>';
	}
	return showHtml;
}

function doRandering_ZZHTJS(obj){
	var showHtml = "";
	if(obj.ZZHTJS!=""){
		showHtml = '<div style="width:130px;">'+obj.ZZHTJS_SV+'</div>';
	}else{
		showHtml = '<div style="width:130px;text-align:center">—</div>';
	}
	return showHtml;
}

function doRandering_ZZHTZFBL(obj){
	var showHtml = "";
	if(obj.ZZHTZFBL!=""){
		showHtml = '<div style="width:30px;">'+obj.ZZHTZFBL+'</div>';
	}else{
		showHtml = '<div style="width:30px;text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_ZZWCZFBL(obj){
	var showHtml = "";
	if(obj.ZZWCZFBL!=""){
		showHtml = '<div style="width:30px;">'+obj.ZZWCZFBL+'</div>';
	}else{
		showHtml = '<div style="width:30px;text-align:center">—</div>';
	}
	return showHtml;
}

function doJHTZ(obj){
	var showHtml = "";
	if(obj.JHTZ==''||obj.JHTZ=='null'){
		showHtml = '<div style="width:130px;text-align:center">—</div>';
	}else{
			showHtml = '<div style="width:130px;">'+obj.JHTZ_SV+'</div>';
	}
	return showHtml;
}
function doTCTZ(obj){
	var showHtml = "";
	if(obj.TCTZ==''||obj.TCTZ=='null'){
		showHtml = '<div style="width:130px;text-align:center">—</div>';
	}else{
			showHtml = '<div style="width:130px;">'+obj.TCTZ_SV+'</div>';
	}
	return showHtml;
}
function doGS(obj){
	var showHtml = "";
	if(obj.GS==''||obj.GS=='null'){
		showHtml = '<div style="width:130px;text-align:center">—</div>';
	}else{
			showHtml = '<div style="width:130px;">'+obj.GS_SV+'</div>';
	}
	return showHtml;
}
function doHTJ(obj){
	var showHtml = "";
	if(obj.ZXHTJ==''||obj.ZXHTJ=='null'){
		showHtml = '<div style="width:130px;text-align:center">—</div>';
	}else{
			showHtml = '<div style="width:130px;">'+obj.ZXHTJ_SV+'</div>';
	}
	return showHtml;
}
function doTZ(obj){
	var showHtml = "";
	if(obj.WCTZ==''||obj.WCTZ=='null'){
		showHtml = '<div style="width:130px;text-align:center">—</div>';
	}else{
			showHtml = '<div style="width:130px;">'+obj.WCTZ_SV+'</div>';
	}
	return showHtml;
}
function doZF(obj){
	var showHtml = "";
	if(obj.HTZF==''||obj.HTZF=='null'){
		showHtml = '<div style="width:130px;text-align:center">—</div>';
	}else{
			showHtml = '<div style="width:130px;">'+obj.HTZF_SV+'</div>';
	}
	return showHtml;
}
function doJS(obj){
	var showHtml = "";
	if(obj.HTJS==''||obj.HTJS=='null'){
		showHtml = '<div style="width:130px;text-align:center">—</div>';
	}else{
			showHtml = '<div style="width:130px;">'+obj.HTJS_SV+'</div>';
	}
	return showHtml;
}
function doRandering_ZHTJ(obj){
	var showHtml = "";
	if(obj.ZHTQDJ==''||obj.ZHTQDJ=='null'){
		showHtml = '<div style="width:130px;text-align:center">—</div>';
	}else{
			showHtml = '<div style="width:130px;">'+obj.ZHTQDJ_SV+'</div>';
	}
	return showHtml;
}

</script>
	</head>
	<body>
		<input type="hidden" id="Q_htmx_HTLX_2" name="HTLX" fieldname="v.HTLX" operation='!=' logic="or" value="CQ"/>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="table2">
			<tr>
				<form method="post" id="queryForm">
					<input type="hidden" id="QXMID" name="XMID" fieldname="v.xmid" operation='='/>
				</form>
				<form method="post" id="queryTtmxForm">
					<input type="hidden" id="Q_htmx_XMID" name="XMID" fieldname="v.xmid" operation='='/>
					<input type="hidden" id="Q_htmx_HTLX" name="HTLX" fieldname="v.HTLX" operation='=' logic="and"/>
					<button id="btnQuery" class="btn btn-link"  type="button" style="display:none;"><i class="icon-search"></i>查询</button>
				</form>
			</tr>
		</table>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span4" style="width: 65%">
					<div class="B-small-from-table-autoConcise">
						<div><span id='xmmcSpan'></span>投资明细分析</div>
						<div class="overFlowX">
							<table class="table-hover table-activeTd B-table" id="DT1"
								width="100%" type="single" noPage="true" pageNum="1000">
								<thead>
									<tr>
										<th fieldname="TZLX" colindex=1 width="4%"  tdalign="center" customfunction="doType">
											&nbsp;&nbsp;
										</th>
										<th fieldname="JHTZ" colindex=2 width="14%" customfunction="doJHTZ" tdalign="right">
											&nbsp;计财总投资(元)&nbsp;
										</th>
										<th fieldname="TCTZ" colindex=3 width="14%" customfunction="doTCTZ" tdalign="right">
											&nbsp;统筹计划投资(元)&nbsp;
										</th>
										<th fieldname="GS" colindex=4 width="14%" customfunction="doGS" tdalign="right">
											&nbsp;概算(元)&nbsp;
										</th>
										<th fieldname="ZXHTJ" colindex=5 width="12%" customfunction="doHTJ" tdalign="right">
											&nbsp;最新合同价(元)&nbsp;
										</th>
										<th fieldname="WCTZ" colindex=6 width="12%" customfunction="doTZ" tdalign="right">
											&nbsp;完成投资(元)&nbsp;
										</th>
										<th fieldname="TZQDB" colindex=7 width="2%" tdalign="right">
											&nbsp;%&nbsp;
										</th>
										<th fieldname="HTZF" colindex=8 width="14%" customfunction="doZF" tdalign="right">
											&nbsp;支付&nbsp;
										</th>
										<th fieldname="ZFQDB" colindex=9 width="2%" tdalign="right">
											&nbsp;%&nbsp;
										</th>
										<th fieldname="HTJS" colindex=10 width="14%" customfunction="doJS" tdalign="right">
											&nbsp;结算&nbsp;
										</th>
										<th fieldname="HTS" colindex=11 width="2%" tdalign="right">
											&nbsp;个数&nbsp;
										</th>
									</tr>
								</thead>
								<tbody id="t_body"></tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="span4" style="width: 32%">
					<fieldset class="b_ddd">
						<div class="row-fluid">
							<!-- 使用fushionchar-->
							<div class="span6" style="width: 98%; height: 200px;"
								id="chartdiv" align="center"></div>
						</div>
					</fieldset>
				</div>
			</div>
		</div>

		<div style="height: 20px;">
		</div>
		
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span4" style="width: 98%">
					<div class="B-small-from-table-autoConcise">
					<div>本项目<span id='htmx_name_Span'></span>合同列表（共<span id='htmx_gs_Span'></span>个合同）</div>
					<div class="overFlowX">
					<table class="table-hover table-activeTd B-table" id="htmxList"
								width="100%" type="single" noPage="true" pageNum="1000">
	                	 <thead>
		                	<tr>
								<th  fieldname="HTMC" colindex=1 tdalign="left" maxlength="30">&nbsp;合同名称&nbsp;</th>
								<th  fieldname="HTLX" colindex=2 tdalign="center">&nbsp;合同类型&nbsp;</th>
								<th fieldname="ZHTQDJ" colindex=3 tdalign="right" CustomFunction="doRandering_ZHTJ">&nbsp;合同签订价(元)&nbsp;</th>
								<th fieldname="XMNUM" colindex=4 tdalign="right">&nbsp;项目数&nbsp;</th>
								<th fieldname="ZZHTQDJ" colindex=5 tdalign="right" CustomFunction="doRandering_ZZHTQDJ">&nbsp;本项目合同价(元)&nbsp;</th>
								<th  fieldname="ZZWCZF" colindex=6 tdalign="right" CustomFunction="doRandering_ZZWCZF">&nbsp;本项目完成投资(元)&nbsp;</th>
								<th  fieldname="ZZWCZFBL" colindex=7 tdalign="right" CustomFunction="doRandering_ZZWCZFBL">&nbsp;%&nbsp;</th>
								<th  fieldname="ZZHTZF" colindex=8 tdalign="right" CustomFunction="doRandering_ZZHTZF">&nbsp;本项目支付(元)&nbsp;</th>
								<th  fieldname="ZZHTZFBL" colindex=9 tdalign="right" CustomFunction="doRandering_ZZHTZFBL">&nbsp;%&nbsp;</th>
		                		<th fieldname="ZZHTJS" colindex=10 tdalign="right" CustomFunction="doRandering_ZZHTJS">&nbsp;本项目结算价(元)&nbsp;</th>
		                	</tr>
		                </thead>
						<tbody id="t_body"></tbody>
						</table>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="v.sort_num" />
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
				<input type="hidden" name="queryResult" id="queryResult" />
			</FORM>
			<FORM name="frmPost_htmx" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="v.xmid" />
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
				<input type="hidden" name="queryResult" id="queryResult" />
			</FORM>
		</div>
		
	</body>
</html>