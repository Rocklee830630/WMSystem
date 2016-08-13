<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%@ page import="com.ccthanking.framework.util.Pub"%>
		<app:base />
		<title>计划管理-统筹计划跟踪</title>
		<%
			String ywlx = request.getParameter("ywlx");
			String bmjkLx = request.getParameter("bmjkLx");
			String nd = request.getParameter("nd");
			String tableName = request.getParameter("tableName");
			String xmglgs = request.getParameter("xmglgs");
			java.util.Calendar cal = java.util.Calendar.getInstance();
			String sysdate = Pub.getDate("yyyy-MM-dd");
		%>
		<script
			src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
		<style>
		.label label-important {
			background-color: #FF8888;
			width: 100%;
			height: 100%;
			display: inline-block;
			margin: 0;
			padding: 0;
		}
		</style>
		<script
			src="${pageContext.request.contextPath }/js/common/FixTable.js"></script>
		<script type="text/javascript" charset="utf-8">
var controllername= "${pageContext.request.contextPath }/bmjkCommonController.do";
var ywlx = "<%=ywlx %>";
var bmjkLx = "<%=bmjkLx%>";
var nd = "<%=nd%>";
var tableName = "<%=tableName%>";
var xmglgs = "<%=xmglgs%>";
var sysdate = '<%=sysdate %>';


function setPageHeight(){
	var getHeight=getDivStyleHeight();
	var height = getHeight-pageTopHeight-pageTitle-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#tcjhList").attr("pageNum",pageNum);
}
//页面初始化
$(function() {
	setPageHeight();
	//自动完成项目名称模糊查询
	showAutoComplete("QXMMC",controllername+"?xmmcAutoQuery","getXmmcQueryCondition"); 
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});

	//按钮绑定事件（导出）
	$("#btnExp").click(function() {
		xAlert("提示信息","导出");
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
        //其他处理放在下面
    });
  	//按钮绑定事件（导出）
	$("#btnExp").click(function() {
		 if(exportRequireQuery($("#tcjhList"))){//该方法需传入表格的jquery对象
		      printTabList("tcjhList");
		  }
	});
	
});
//页面默认参数
function init(){
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
	
}
//甘特图
function showGtt(id){
	$(window).manhuaDialog({"title":"项目甘特图","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/tcjh/tcjh_gtt.jsp?id="+id,"modal":"1"});
}
//设置甘特图字段样式
function doGtt(obj){
	var id = obj.GC_JH_SJ_ID;
	return "<div style=\"text-align:center\"><a href=\"javascript:void(0);\"><i title=\"查看甘特图\" class=\"icon-tasks\" onclick=\"showGtt('"+id+"')\"></i></a></div>";
	//return "<a href=\"#\" onclick=\"showGtt(\'"+id+"\')\">干特图</a>";
}

//列表项<项目地址>加图标
function doDz(obj){
	var xmdz = obj.XMDZ;
	var xmid = obj.XMID;
	var bdid = obj.BDID;
	/* if(xmdz != ""){
		if(xmdz.length>15){
			xmdz = '<abbr title="'+obj.XMDZ+'">'+xmdz.substring(0,15)+'...&nbsp;<a href="javascript:void(0);" onclick="selectDz()"><i title="查看" class="pull-right icon-map-marker"></i></a></abbr>';
		}else{
			xmdz = xmdz+'&nbsp;<a href="javascript:void(0);" onclick="selectDz()"><i title="查看" class="pull-right icon-map-marker"></a></i>';
		}
		return xmdz;
	} */
	if(xmdz != ""){
		return "<a href=\"javascript:void(0);\" onclick=\"selectDz('"+xmid+"','"+bdid+"')\"><i title=\"查看\" class=\"pull-right icon-map-marker\"></a></i>";
	}
	
}
//点击项目地址图标
function selectDz(xmid,bdid){
	var url = "${pageContext.request.contextPath }/jsp/xmgl/xmcbk/baidu_view.jsp?xmbh="+xmid+"&bdbh="+bdid;
	$(window).manhuaDialog({"title":"项目地图","type":"text","content":url,"modal":"1"});
}

//列表项<健康状况>状态
function doJkzk(obj){
	
	var daysArray = new Array();
	var textArray = new Array();
	if(obj.KGSJ != "" && obj.KGSJ_SJ == ""){
		daysArray.push(obj.KGSJ);
		textArray.push("开工时间");
	}
	if(obj.WGSJ != "" && obj.WGSJ_SJ == ""){
		daysArray.push(obj.WGSJ);
		textArray.push("完工时间");
	}
	if(obj.KYPF !="" && obj.KYPF_SJ == ""){
		daysArray.push(obj.KYPF);
		textArray.push("可研批复");
	} 
	if(obj.HPJDS != "" && obj.HPJDS_SJ == ""){
		daysArray.push(obj.HPJDS);
		textArray.push("划拔决定书");
	}
	if(obj.GCXKZ != "" && obj.GCXKZ_SJ == ""){
		daysArray.push(obj.GCXKZ);
		textArray.push("工程规划许可证");
	}
	if(obj.SGXK !="" && obj.SGXK_SJ ==""){
		daysArray.push(obj.SGXK);
		textArray.push("施工许可");
	}
	if(obj.CBSJPF != "" && obj.CBSJPF_SJ == ""){
		daysArray.push(obj.CBSJPF);
		textArray.push("初步设计批复");
	}
	if(obj.CQT != "" && obj.CQT_SJ == ""){
		daysArray.push(obj.CQT);
		textArray.push("拆迁图");
	}
	if(obj.PQT != "" && obj.PQT_SJ == ""){
		daysArray.push(obj.PQT);
		textArray.push("排迁图");
	}
	if(obj.SGT != "" && obj.SGT_SJ == ""){
		daysArray.push(obj.SGT);
		textArray.push("施工图");
	}
	if(obj.TBJ != "" && obj.TBJ_SJ == ""){
		daysArray.push(obj.TBJ);
		textArray.push("提报价");
	}
	if(obj.CS != "" && obj.CS_SJ == ""){
		daysArray.push(obj.CS);
		textArray.push("造价财审");
	}
	if(obj.JLDW != "" && obj.JLDW_SJ == ""){
		daysArray.push(obj.JLDW);
		textArray.push("招标监理单位");
	}
	if(obj.SGDW != "" && obj.SGDW_SJ == ""){
		daysArray.push(obj.SGDW);
		textArray.push("招标施工单位");
	}
	if(obj.ZC != "" && obj.ZC_SJ == ""){
		daysArray.push(obj.ZC);
		textArray.push("征拆");
	}
	if(obj.PQ != "" && obj.PQ_SJ == ""){
		daysArray.push(obj.PQ);
		textArray.push("排迁");
	}
	if(obj.JG != "" && obj.JG_SJ == ""){
		daysArray.push(obj.JG);
		textArray.push("交工");
	}
	if(daysArray.length == 0){
		return '<div style="text-align:center"><i title="正常执行" class="icon-green"></i></div>';
	}
	for(var i = 0; i<daysArray.length; i++){
		for(var j = 0; j<daysArray.length - i;j++){
			if(new Date(daysArray[j]) < new Date(daysArray[j+1]) ){
				var temp = daysArray[j];
				daysArray[j] = daysArray[j + 1];
				daysArray[j + 1] = temp;
				var textTemp = textArray[j];
				textArray[j] = textArray[j + 1];
				textArray[j + 1] = textTemp;
				
			}
		}
	}
	var dateVal = getDays(sysdate,daysArray[daysArray.length-1]);
	var textVal = textArray[textArray.length-1];
	if( dateVal<= 5){
		return '<div style="text-align:center"><i title="正常执行" class="icon-green"></i></div>';
	}else if(dateVal >5 && dateVal <=10){
		return '<div style="text-align:center"><i title="<'+textVal+'>截止到今天超期'+dateVal+'天未反馈" class="icon-yellow"></i></div>';
	}else if(dateVal >10){
		return '<div style="text-align:center"><i title="<'+textVal+'>截止到今天超期'+dateVal+'天未反馈" class="icon-red"></i></div>';
	}
}
//详细信息
function rowView(index){
	var obj = $("#tcjhList").getSelectedRowJsonByIndex(index);
	var xmid = convertJson.string2json1(obj).XMID;
	$(window).manhuaDialog(xmscUrl(xmid));
}
//默认年度
function getNd(){
	//$("#qnd").val(new Date().getFullYear());
	generateFyjjhNdMc($("#qnd"),$("#JHID")); 
}

//查询列表
function queryList(){
	setPageHeight();
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,tcjhList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryXmxxBdxx&ywlx="+ywlx+"&bmjkLx="+bmjkLx+"&nd="+nd+"&tableName="+tableName+"&xmglgs="+xmglgs,data,tcjhList);
}

//项目名称自动模糊查询参数
function getXmmcQueryCondition(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,tcjhList);
	return data;
}
/**
 * 根据计划查询非应急批次
 * ndObj 年度的jquery对象
 * mcObj 计划名称jquery对象
 */
function generateFyjjhNdMc(ndObj,mcObj){
	ndObj.attr("src","T#GC_JH_SJ: distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	//ndObj.val(new Date().getFullYear());
	//setDefaultOption(ndObj,new Date().getFullYear());
	setDefaultNd(ndObj.attr("id"));
	if(mcObj){
		mcObj.attr("kind","dic");
		mcObj.attr("src","T#GC_JH_ZT:GC_JH_ZT_ID:jhpch:SFYX='1' AND ISXF='1' AND ND='" + ndObj.val()+"' and GC_JH_ZT_ID in(select JHID from GC_JH_SJ where sfyx='1' and nd='"+ ndObj.val()+"' and XMSX='1') ORDER BY JHPCH ASC");
		mcObj.html('');
		reloadSelectTableDic(mcObj);
		ndObj.change(function() {
			mcObj.html('');
			if(!ndObj.val().length){
			}
			mcObj.attr("src","T#GC_JH_ZT:GC_JH_ZT_ID:jhpch:SFYX='1' AND ISXF='1' AND ND='" + ndObj.val()+"' and GC_JH_ZT_ID in(select JHID from GC_JH_SJ where sfyx='1' and nd='"+ ndObj.val()+"' and XMSX='1') ORDER BY JHPCH ASC");
			reloadSelectTableDic(mcObj);
		});
	}
}
//标段图标样式
function doBdmc(obj){
	if(obj.XMBS == "0"){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}
//开工时间
function doKG(obj){
	return getCellStyle(obj.KGSJ_SJ,obj.KGSJ);
}
//完工时间
function doWG(obj){
	return getCellStyle(obj.WGSJ_SJ,obj.WGSJ);
}
//可研批复
function doKYPF(obj){
	if(obj.ISKYPF == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return getCellStyle(obj.KYPF_SJ,obj.KYPF);
	}
}
//划拔决定书
function doHBJDS(obj){
	if(obj.ISHPJDS == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return getCellStyle(obj.HPJDS_SJ,obj.HPJDS);
	}
	
}
//工程规划许可证
function doGCGHXKZ(obj){
	if(obj.ISGCXKZ == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return getCellStyle(obj.GCXKZ_SJ,obj.GCXKZ);
	}
}
//施工许可
function doSGXK(obj){
	if(obj.ISSGXK == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return getCellStyle(obj.SGXK_SJ,obj.SGXK);
	}
}
//初步设计批复
function doCBSJPF(obj){
	if(obj.ISCBSJPF == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return getCellStyle(obj.CBSJPF_SJ,obj.CBSJPF);
	}
	
}
//拆迁图 
function doCQT(obj){
	if(obj.ISCQT == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return getCellStyle(obj.CQT_SJ,obj.CQT);
	}
	
}
//排迁图
function doPQT(obj){
	if(obj.ISPQT == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return getCellStyle(obj.PQT_SJ,obj.PQT);
	}
	
}
// 施工图
function doSGT(obj){
	if(obj.ISSGT == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return getCellStyle(obj.SGT_SJ,obj.SGT);
	}
	
}
//提报价
function doTBJ(obj){
	if(obj.ISTBJ == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return getCellStyle(obj.TBJ_SJ,obj.TBJ);
	}
	
}
//财审
function doCS(obj){
	if(obj.ISCS == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return getCellStyle(obj.CS_SJ,obj.CS);
	}
	
}
//监理单位
function doJLDW(obj){
	if(obj.ISJLDW == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return getCellStyle(obj.JLDW_SJ,obj.JLDW);
	}
	
}
//施工单位
function doSGDW(obj){
	if(obj.ISSGDW == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return getCellStyle(obj.SGDW_SJ,obj.SGDW);
	}
	
}
//列表项<征拆>状态
function doZC(obj){
	if(obj.ISZC == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return getCellStyle(obj.ZC_SJ,obj.ZC);
	}
}
//列表项<排迁>状态
function doPQ(obj){
	if(obj.ISPQ == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return getCellStyle(obj.PQ_SJ,obj.PQ);
	}
}
//交工
function doJG(obj){
	if(obj.ISJG == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return getCellStyle(obj.JG_SJ,obj.JG);
	}
	
}

//节点样式
function getCellStyle(str1,str2){
	
	if(str1 == "" && str2 != ""){
		if(new Date(sysdate) > new Date(str2)){
			return '<b>'+getDays(str2,sysdate)+'天</b><span class="label background-color: gray;">未反馈</span>';
		}
	}else if(str1!="" && str2 != ""){
		if(new Date(str1) > new Date(str2)){
			return '<span title="超过'+ getDays(str2,str1)+'天完成" class="label label-important">'+str1+'</span>';
		}
	}
}
//计算日期天数
function getDays(strDateStart,strDateEnd){
	if(strDateStart < strDateEnd){
		return 0;
	}
   var strSeparator = "-"; //日期分隔符
   var oDate1;
   var oDate2;
   var iDays;
   oDate1= strDateStart.split(strSeparator);
   oDate2= strDateEnd.split(strSeparator);
   var strDateS = new Date(oDate1[0] + "-" + oDate1[1] + "-" + oDate1[2]);
   var strDateE = new Date(oDate2[0] + "-" + oDate2[1] + "-" + oDate2[2]);
   iDays = parseInt(Math.abs(strDateS - strDateE ) / 1000 / 60 / 60 /24)//把相差的毫秒数转换为天数 

   return iDays ;
}

//标段编号图标样式
function doBdbh(obj){
	if(obj.BDBH == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDBH;
	}
}

function queryAfter(){
	var height = g_iHeight-pageTitle-pageQuery-getTableTh(1);
	var pageNum = parseInt(height/pageTableOne,10);
	
	var rows = $("tbody tr" ,$("#tcjhList"));
	var tr_obj = rows.eq(0);
    var t = $("#tcjhList").getTableRows();
    var tr_height = $(tr_obj).height();
    var d = pageNum*(tr_height);
    if(d<200) d = 150;
    // 当高度大于400时，显示主页面列表的高度
    if(d>400) d = height;
    // 当没有数据的时候，只显示表头
 	if(tr_height==null) d = getTableTh(2)+20;
    window_width = document.documentElement.clientWidth;//$("#allDiv").width()

    $("#tcjhList").fixTable({
		fixColumn: 7,//固定列数
		width:window_width-10,//显示宽度
		height:d//显示高度
	});
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
							<button id="btnExp" class="btn" type="button">
								导出
							</button> </span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<INPUT type="text" class="span12" kind="text" id="ISXF"
										fieldname="t.ISXF" keep="true" value="1" operation="=">
									<input type="text" class="span12" kind="text"
										fieldname="t.XMSX" name="qXMSX" value="1" keep="true"
										operation="=">
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="tcjhList"
							width="100%" type="single" pageNum="1000">
							<thead>
								<tr>
									<th name="XH" id="_XH" rowspan="2" colindex=1 tdalign="center">
										&nbsp;#&nbsp;
									</th>
									<th fieldname="PXH" rowspan="2" colindex=2
										CustomFunction="doGtt" noprint="true">
										甘特图
									</th>
									<th fieldname="KGSJ" rowspan="2" colindex=3
										CustomFunction="doJkzk" noprint="true">
										&nbsp;健康&nbsp;
										<br>
										&nbsp;状况&nbsp;
									</th>
									<th fieldname="XMBH" rowspan="2" colindex=4 hasLink="true"
										linkFunction="rowView" rowMerge="true">
										&nbsp;项目编号&nbsp;
									</th>
									<th fieldname="XMMC" rowspan="2" colindex=5 maxlength="15"
										rowMerge="true">
										&nbsp;项目名称&nbsp;
									</th>
									<th fieldname="BDBH" rowspan="2" colindex=6
										CustomFunction="doBdbh">
										&nbsp;标段编号&nbsp;
									</th>
									<th fieldname="BDMC" rowspan="2" colindex=7 maxlength="15"
										CustomFunction="doBdmc">
										&nbsp;标段名称&nbsp;
									</th>
									<th fieldname="XMDZ" rowspan="2" colindex=8 maxlength="15">
										&nbsp;建设位置&nbsp;
									</th>
									<th fieldname="XMDZ" rowspan="2" colindex=9
										CustomFunction="doDz" noprint="true">
										&nbsp;&nbsp;
									</th>
									<th fieldname="XMGLGS" rowspan="2" colindex=10 maxlength="15"
										tdalign="center">
										&nbsp;项目管理公司&nbsp;
									</th>
									<th fieldname="JSNR" rowspan="2" colindex=11 maxlength="15">
										&nbsp;建设内容及规模&nbsp;
									</th>
									<th fieldname="XMXZ" rowspan="2" colindex=12 tdalign="center">
										&nbsp;项目性质&nbsp;
									</th>
									<th fieldname="JSMB" rowspan="2" maxlength="15" colindex=13>
										&nbsp;年度目标&nbsp;
									</th>
									<th colspan="2">
										&nbsp;工期安排（工程部及项目管理公司）&nbsp;
									</th>
									<th colspan="4">
										&nbsp;手续办理情况&nbsp;
									</th>
									<th colspan="4">
										&nbsp;设计情况&nbsp;
									</th>
									<th colspan="2">
										&nbsp;造价&nbsp;
									</th>
									<th colspan="2">
										&nbsp;招标&nbsp;
									</th>
									<th fieldname="ZC_SJ" rowspan="2" colindex=28 tdalign="center"
										CustomFunction="doZC">
										&nbsp;征拆&nbsp;
									</th>
									<th fieldname="PQ_SJ" rowspan="2" colindex=29 tdalign="center"
										CustomFunction="doPQ">
										&nbsp;排迁&nbsp;
									</th>
									<th fieldname="JG_SJ" rowspan="2" colindex=30 tdalign="center"
										CustomFunction="doJG">
										&nbsp;交工&nbsp;
									</th>
								</tr>
								<tr>
									<th fieldname="KGSJ_SJ" colindex=14 tdalign="center"
										CustomFunction="doKG">
										&nbsp;开工时间&nbsp;
									</th>
									<th fieldname="WGSJ_SJ" colindex=15 tdalign="center"
										CustomFunction="doWG">
										&nbsp;完工时间&nbsp;
									</th>
									<th fieldname="KYPF_SJ" colindex=16 tdalign="center"
										CustomFunction="doKYPF">
										&nbsp;可研批复&nbsp;
									</th>
									<th fieldname="HPJDS_SJ" colindex=17 tdalign="center"
										CustomFunction="doHBJDS">
										&nbsp;划拔决定书&nbsp;
									</th>
									<th fieldname="GCXKZ_SJ" colindex=18 tdalign="center"
										CustomFunction="doGCGHXKZ">
										&nbsp;工程规划许可证&nbsp;
									</th>
									<th fieldname="SGXK_SJ" colindex=19 tdalign="center"
										CustomFunction="doSGXK">
										&nbsp;施工许可&nbsp;
									</th>
									<th fieldname="CBSJPF_SJ" colindex=20 tdalign="center"
										CustomFunction="doCBSJPF">
										&nbsp;初步设计批复&nbsp;
									</th>
									<th fieldname="CQT_SJ" colindex=21 tdalign="center"
										CustomFunction="doCQT">
										&nbsp;拆迁图&nbsp;
									</th>
									<th fieldname="PQT_SJ" colindex=22 tdalign="center"
										CustomFunction="doPQT">
										&nbsp;排迁图&nbsp;
									</th>
									<th fieldname="SGT_SJ" colindex=23 tdalign="center"
										CustomFunction="doSGT">
										&nbsp;施工图&nbsp;
									</th>
									<th fieldname="TBJ_SJ" colindex=24 tdalign="center"
										CustomFunction="doTBJ">
										&nbsp;提报价&nbsp;
									</th>
									<th fieldname="CS_SJ" colindex=25 tdalign="center"
										CustomFunction="doCS">
										&nbsp;财审&nbsp;
									</th>
									<th fieldname="JLDW_SJ" colindex=26 tdalign="center"
										CustomFunction="doJLDW">
										&nbsp;监理单位&nbsp;
									</th>
									<th fieldname="SGDW_SJ" colindex=27 tdalign="center"
										CustomFunction="doSGDW">
										&nbsp;施工单位&nbsp;
									</th>

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
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="t.XMBH,t.XMBS,t.PXH" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id="queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>