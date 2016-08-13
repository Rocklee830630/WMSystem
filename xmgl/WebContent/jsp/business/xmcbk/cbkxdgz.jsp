<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>储备库下达跟踪</title>
<%
String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script type="text/javascript" charset="UTF-8">
 
var controllername= "${pageContext.request.contextPath }/xmcbk/xmcbkwhController.do";
var controllernameXM= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
var sysdate = '<%=sysdate %>';
//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
}
//页面初始化
$(function() {
	setPageHeight();
	//自动完成项目名称模糊查询
	showAutoComplete("QXMMC",controllernameXM+"?xmmcAutoCompleteToXmxdk","getXmmcQueryCondition"); 
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件（导出）
	$("#btnExp").click(function() {
		 if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
		      printTabList("DT1");
		  }
	});

	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
    });
    //监听年度变化事件
    $("#qnd").change(function() {
    	generatePc($("#PCH"));
    	//$("#xdnd").val($("#ND").val());
    });  
});
//页面默认参数
function init(){
	getNd();
	generatePc($("#PCH"));
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
	
}
//批次查询
function generatePc(pcObj){
	pcObj.attr("src","T#GC_TCJH_XMCBK:PCH:pch pch_px: SFYX='1' and isxd='1' and pch is not null and nd='"+$("#qnd").val()+"'  group by pch order by pch_px asc");
	pcObj.attr("kind","dic");
	pcObj.html('');
	reloadSelectTableDic(pcObj);
} 

//默认年度
function getNd(){
	setDefaultNd("qnd");
	//setDefaultOption($("#qnd"),new Date().getFullYear());
}
//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryCbkGz&nd="+$("#qnd").val(),data,DT1);
}
//项目名称自动模糊查询参数
function getXmmcQueryCondition(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	return data;
}
//项目状态
function doXmzt(obj){
	var flag = "0";//用于判断开工，完工，征拆，排迁的状态
	var days = 0;//用于获取两个日期之间的差值
	if(obj.ISNATC==0)
	{
		return '<i title="未下达到计划" class="icon-gray"></i>';
	}
	if(flag == "0"){
		if(obj.KGSJ != ""){//开工
			if(obj.KGSJ_SJ != ""){
				days = getDays(obj.KGSJ_SJ,obj.KGSJ);
			}else{
				days = getDays(sysdate,obj.KGSJ);
			}
			if(days <= 5){
				flag = "0";//正常情况
			}else {
				flag = "1";//延期情况
			}
		}
	}
	if(flag == "0"){//完工
		if(obj.WGSJ != ""){
			if(obj.WGSJ_SJ != ""){
				days = getDays(obj.WGSJ_SJ,obj.WGSJ);
			}else{
				days = getDays(sysdate,obj.WGSJ);
			}
			if(days <= 5){
				flag = "0";//正常情况
			}else {
				flag = "1";//延期情况
			}
		}
	}
	if(flag == "0"){
		if(obj.ZC != ""){//征拆
			if(obj.ZC_SJ != ""){
				days = getDays(obj.ZC_SJ,obj.ZC);
			}else{
				days = getDays(sysdate,obj.ZC);
			}
			if(days <= 5){
				flag = "0";//正常情况
			}else {
				flag = "1";//延期情况
			}
		}
	}
	if(flag == "0"){
		if(obj.PQ != ""){//排迁
			if(obj.PQ_SJ != ""){
				days = getDays(obj.PQ_SJ,obj.PQ);
			}else{
				days = getDays(sysdate,obj.PQ);
			}
			if(days <= 5){
				flag = "0";//正常情况
			}else {
				flag = "1";//延期情况
			}
		}
	}
	if(flag == "0"){
		if(obj.KYPF != ""){//可研批复
			if(obj.KYPF_SJ != ""){
				days = getDays(obj.KYPF_SJ,obj.KYPF);
			}else{
				days = getDays(sysdate,obj.KYPF);
			}
			if(days <= 5){
				flag = "0";//正常情况
			}else {
				flag = "1";//延期情况
			}
		}
	}
	if(flag == "0"){
		if(obj.HPJDS != ""){//计划划拨决定书
			if(obj.HPJDS_SJ != ""){
				days = getDays(obj.HPJDS_SJ,obj.HPJDS);
			}else{
				days = getDays(sysdate,obj.HPJDS);
			}
			if(days <= 5){
				flag = "0";//正常情况
			}else {
				flag = "1";//延期情况
			}
		}
	}
	if(flag == "0"){
		if(obj.GCXKZ != ""){//工程规划许可证
			if(obj.GCXKZ_SJ != ""){
				days = getDays(obj.GCXKZ_SJ,obj.GCXKZ);
			}else{
				days = getDays(sysdate,obj.GCXKZ);
			}
			if(days <= 5){
				flag = "0";//正常情况
			}else {
				flag = "1";//延期情况
			}
		}
	}
	if(flag == "0"){
		if(obj.SGXK != ""){//施工许可
			if(obj.SGXK_SJ != ""){
				days = getDays(obj.SGXK_SJ,obj.SGXK);
			}else{
				days = getDays(sysdate,obj.SGXK);
			}
			if(days <= 5){
				flag = "0";//正常情况
			}else {
				flag = "1";//延期情况
			}
		}
	}
	if(flag == "0"){
		if(obj.CBSJPF != ""){//初步设计批复
			if(obj.CBSJPF_SJ != ""){
				days = getDays(obj.CBSJPF_SJ,obj.CBSJPF);
			}else{
				days = getDays(sysdate,obj.CBSJPF);
			}
			if(days <= 5){
				flag = "0";//正常情况
			}else {
				flag = "1";//延期情况
			}
		}
	}
	if(flag == "0"){
		if(obj.CQT != ""){//拆迁图
			if(obj.CQT_SJ != ""){
				days = getDays(obj.CQT_SJ,obj.CQT);
			}else{
				days = getDays(sysdate,obj.CQT);
			}
			if(days <= 5){
				flag = "0";//正常情况
			}else {
				flag = "1";//延期情况
			}
		}
	}
	if(flag == "0"){
		if(obj.PQT != ""){//排迁图
			if(obj.PQT_SJ != ""){
				days = getDays(obj.PQT_SJ,obj.PQT);
			}else{
				days = getDays(sysdate,obj.PQT);
			}
			if(days <= 5){
				flag = "0";//正常情况
			}else {
				flag = "1";//延期情况
			}
		}
	}
	if(flag == "0"){
		if(obj.SGT != ""){//施工图
			if(obj.SGT_SJ != ""){
				days = getDays(obj.SGT_SJ,obj.SGT);
			}else{
				days = getDays(sysdate,obj.SGT);
			}
			if(days <= 5){
				flag = "0";//正常情况
			}else {
				flag = "1";//延期情况
			}
		}
	}
	if(flag == "0"){
		if(obj.TBJ != ""){//提报价
			if(obj.TBJ_SJ != ""){
				days = getDays(obj.TBJ_SJ,obj.TBJ);
			}else{
				days = getDays(sysdate,obj.TBJ);
			}
			if(days <= 5){
				flag = "0";//正常情况
			}else {
				flag = "1";//延期情况
			}
		}
	}
	if(flag == "0"){
		if(obj.CS != ""){//造价财审
			if(obj.CS_SJ != ""){
				days = getDays(obj.CS_SJ,obj.CS);
			}else{
				days = getDays(sysdate,obj.CS);
			}
			if(days <= 5){
				flag = "0";//正常情况
			}else {
				flag = "1";//延期情况
			}
		}
	}
	if(flag == "0"){
		if(obj.JLDW != ""){//招标监理单位
			if(obj.JLDW_SJ != ""){
				days = getDays(obj.JLDW_SJ,obj.JLDW);
			}else{
				days = getDays(sysdate,obj.JLDW);
			}
			if(days <= 5){
				flag = "0";//正常情况
			}else {
				flag = "1";//延期情况
			}
		}
	}
	if(flag == "0"){
		if(obj.SGDW != ""){//招标施工单位
			if(obj.SGDW_SJ != ""){
				days = getDays(obj.SGDW_SJ,obj.SGDW);
			}else{
				days = getDays(sysdate,obj.SGDW);
			}
			if(days <= 5){
				flag = "0";//正常情况
			}else {
				flag = "1";//延期情况
			}
		}
	}
	if(flag == "0"){
		if(obj.JG != ""){//交工
			if(obj.JG_SJ != ""){
				days = getDays(obj.JG_SJ,obj.JG);
			}else{
				days = getDays(sysdate,obj.JG);
			}
			if(days <= 5){
				flag = "0";//正常情况
			}else {
				flag = "1";//延期情况
			}
		}
	}
	
	if(flag == "0"){
		return '<i title="正常执行" class="icon-green"></i>';
	}else if(flag == "1"){
		return '<i title="延期执行" class="icon-red"></i>';
	}else if(flag == "2"){
		return '<i title="部分延期执行" class="icon-yellow"></i>';
	}
	
}

//计算日期天数
function getDays(strDateStart,strDateEnd){
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
/**
//详细信息
function rowView(index){
	var obj = $("#DT1").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).XMID;
	$(window).manhuaDialog(xmscUrl(id));
}
*/
//详细信息
function rowView(obj){
    //var obj_json=$("#DT1").getSelectedRowJsonByIndex(index);//获取行json串
	//var obj=convertJson.string2json1(obj_json);//获取行对象
	var id_xxxx=obj.XMID;
	var showStr = "";
	if((obj.XMBM).substring(0,5)=="XXXXX"){
		showStr = "<abbr title='"+obj.XMBM+"'>"+obj.XMBH+"</abbr>";
	}else if(obj.XMBM==""){
		showStr = obj.XMBH;
	}else{
		showStr = "<abbr title='"+obj.XMBM+"'>"+(obj.XMBM).substring(0,14)+"</abbr>";
	}
	return "<a href='javascript:void(0);' onclick='showInfoCard(this);' id_xxxx='"+id_xxxx+"'>"+showStr+"</a>";
}
//弹出项目信息卡
function showInfoCard(em){
	var obj = $(em);
	var id_xxxx=obj.attr("id_xxxx");
	$(window).manhuaDialog(xmscUrl(id_xxxx));
}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
		 <h4 class="title">储备库跟踪
      		<span class="pull-right">
		  		<button id="btnExpExcel" class="btn"  type="button">导出</button>
	      	</span>
      	</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>
						<th width="4%" class="right-border bottom-border text-right">年度</th>
						<td class="right-border bottom-border">
							<select class="span12 year" id="qnd" name = "QND"  fieldname = "T1.ND" defaultMemo="全部" operation="=" kind="dic" src="T#GC_TCJH_XMXDK: distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE">
						  	</select>
						</td>
						<th width="5%" class="right-border bottom-border text-right">项目名称</th>
						<td class="right-border bottom-border">
							<input class="span12" type="text" placeholder="" name="QXMMC"
								fieldname="T.XMMC" operation="like" id="QXMMC" autocomplete="off"
								tablePrefix="T"/>
						</td>
				          <th width="5%" class="right-border bottom-border text-right">项目类型</th>
				          <td class="right-border bottom-border">
				            <select class="span12 4characters" name = "QXMLX" fieldname = "T1.XMLX" defaultMemo="全部" operation="=" kind="dic" src="XMLX">
				            </select>
				          </td>
				          <th width="5%" class="right-border bottom-border text-right">项目属性</th>
				          <td class="bottom-border">
				          	<select class="span12 4characters" name = "QXMSX" fieldname = "T1.XMSX" defaultMemo="全部" operation="=" kind="dic" src="XMSX">
				            </select>
				          </td>
						<th width="5%" class="right-border bottom-border">批次号</th>
						<td width="8%" class="right-border bottom-border">
							<select id="PCH"  class="span12 3characters" name="PCH" fieldname="t2.PCH" operation="=" defaultMemo="全部"></select>
						</td>							
			            <td class="text-left bottom-border text-right">
							<button	id="btnQuery" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
	                        <button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
			            </td>							
					</tr>
				</table>
			</form>
			<div style="height:5px;"> </div>		
			<div class="overFlowX"> 
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" pageNum="10" printFileName="储备库跟踪">
	                <thead>		                		
	                    <tr>
	                     	<th  name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>	
	                     	<th fieldname="XMBH" rowspan="2" colindex=2 CustomFunction="rowView">&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" rowspan="2" colindex=3 maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="XMDZ" rowspan="2" colindex=4 maxlength="15">&nbsp;项目地址&nbsp;</th>
							<th fieldname="JSNR" rowspan="2" colindex=5 maxlength="15">&nbsp;建设内容&nbsp;</th>
							<th fieldname="JSYY" rowspan="2" colindex=6 maxlength="15">&nbsp;建设意义&nbsp;</th>
							<th fieldname="XMLX" rowspan="2" colindex=7 tdalign="center">&nbsp;项目类型&nbsp;</th>
							<th colspan="4">&nbsp;计划年度总投资额（万元）&nbsp;</th>
							<th fieldname="XMFR" rowspan="2" colindex=12 tdalign="center">&nbsp;项目法人&nbsp;</th>
							<th fieldname="KYMC" rowspan="2" colindex=13 maxlength="15">&nbsp;可研名称&nbsp;</th>
							<th fieldname="PCH" rowspan="2" colindex=14 tdalign="center">&nbsp;计财批次号&nbsp;</th>
							<th fieldname="XDRQ" rowspan="2" colindex=15 tdalign="center">&nbsp;下达时间&nbsp;</th>
							<th fieldname="XMSX" rowspan="2" colindex=16 tdalign="center">&nbsp;项目属性&nbsp;</th>
							<th fieldname="XMGLGS" rowspan="2" colindex=17 tdalign="center">&nbsp;项目管理公司&nbsp;</th>
							<th colspan="2">&nbsp;工期安排&nbsp;</th>
							<th fieldname="XMZT" rowspan="2" colindex=20 tdalign="center" CustomFunction="doXmzt" noprint="true">&nbsp;项目状态&nbsp;</th>
							<th colspan="4">&nbsp;已完成年度总投资额&nbsp;</th>
	                    </tr>
	                    <tr>
							<th fieldname="GC_JH" colindex=8 tdalign="right">&nbsp;工程&nbsp;</th>
							<th fieldname="ZC_JH" colindex=9 tdalign="right">&nbsp;征拆&nbsp;</th>
							<th fieldname="QT_JH" colindex=10 tdalign="right">&nbsp;其他&nbsp;</th>
							<th fieldname="JHZTZR_JH" colindex=11 tdalign="right">&nbsp;合计&nbsp;</th>
							<th fieldname="KGSJ_SJ" colindex=18 tdalign="center">&nbsp;开工时间&nbsp;</th>
							<th fieldname="WGSJ_SJ" colindex=19 tdalign="center">&nbsp;完工时间&nbsp;</th>
							
							<th fieldname="GC_WC" colindex=21 tdalign="right">&nbsp;工程&nbsp;</th>
							<th fieldname="ZC_WC" colindex=22 tdalign="right">&nbsp;征拆&nbsp;</th>
							<th fieldname="QT_WC" colindex=23 tdalign="right">&nbsp;其他&nbsp;</th>
							<th fieldname="JHZTZE_WC" colindex=24 tdalign="right">&nbsp;合计&nbsp;</th>
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
		<input type="hidden" name="txtFilter" order="desc" fieldname="t.XMBH"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
</div>
</body>
</html>