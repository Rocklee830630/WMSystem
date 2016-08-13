 <!DOCTYPE html>
<%@page import="java.sql.Blob"%>
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
<%
	String id = request.getParameter("id");
	Connection conn = DBUtil.getConnection();//定义连接
	StringBuffer sbSql = null;//sql语句字符串
	String sql = "";
	BaseResultSet bs = null;
  	ResultSet rs = null;
  	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	Boolean wgFlag = true;
	String flagText = "";//用于记录哪个节点延期了
	
	
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/xmscController.do";
var id = "<%=id%>";
var bmid = '<%=deptId%>';
//页面初始化
$(function() {
	init();
});
	
//页面默认参数
function init(){
	queryJdzxList();
	queryJdjhjzxList();
}

//双击textarea时动态设置行数
function changeRows(obj){
	if($(obj).attr("rows") == "20"){
		$(obj).attr("rows","3");
		$(obj).attr("style","border-style:none;background:#FFF;box-shadow:none;overflow:hidden");
		$("#gcjdms_div").attr("style","height:165px;");
	}else{
		$(obj).attr("rows","20");
		$(obj).attr("style","border-style:none;background:#FFF;box-shadow:none;overflow:hidden");
		$("#gcjdms_div").attr("style","height:480px;");
	}
}

//<进度执行>查询方法
function queryJdzxList(){
	var data = combineQuery.getQueryCombineData(queryFormJdzx,frmPost,jdzxList);
	defaultJson.doQueryJsonList(controllername+"?queryJdzx&id="+id,data,jdzxList,"callBackJdzx",true);
}

//<进度进度及执行>查询方法
function queryJdjhjzxList(){
	var data = combineQuery.getQueryCombineData(queryFormJdjhjzx,frmPost,jdjhjzxList);
	defaultJson.doQueryJsonList(controllername+"?queryJdjhjzx&id="+id,data,jdjhjzxList,"callBackJdjhjzx",true);
}
//<进度进度及执行>回调函数
function callBackJdjhjzx(){
	var rows = $("tbody tr" ,$("#jdjhjzxList"));
    for(var t =0;t<rows.size();t++){
    	var index = 1;
    	if(t%3 == 0){
    		
    	}else{
    		index--;
    	}
     	var $tr_obj = $(rows[t]);
        var td_obj = $tr_obj.find("td").eq(index);
     	td_obj.css({"background":"#f6f6f6"});
     	td_obj.css({"font-weight":"bold"});
     	td_obj.css({"width":"20px"});
    } 
}
//<进度执行>回调函数
function callBackJdzx(){
	/* var rows = $("tbody tr" ,$("#jdzxList"));
    for(var t =0;t<rows.size();t++){
    	var index = 1;
    	if(t%2 == 0){
    		
    	}else{
    		index--;
    	}
     	var $tr_obj = $(rows[t]);
        var td_obj = $tr_obj.find("td").eq(index);
     	td_obj.css({"background":"#f6f6f6"});
     	td_obj.css({"font-weight":"bold"});
    } */
	
}

//<进度执行>开工样式
function doKG(obj){
	
	if(obj.ISKGSJ == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.KGSJ;
	}
}
//<进度执行>开工实际样式
function doKG_SJ(obj){
	if(obj.ISKGSJ == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.KGSJ_SJ;
	}
}
//<进度执行>完工样式
function doWG(obj){
	if(obj.ISWGSJ == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.WGSJ;
	}
}

//<进度执行>完工实际样式
function doWG_SJ(obj){
	if(obj.ISWGSJ == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.WGSJ_SJ;
	}
}
//<进度执行>征拆样式
function doZC(obj){
	if(obj.ISZC == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.ZC;
	}
}
//<进度执行>征拆实际样式
function doZC_SJ(obj){
	if(obj.ISZC == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.ZC_SJ;
	}
}
//<进度执行>排迁样式
function doPQ(obj){
	if(obj.ISPQ == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.PQ;
	}
}

//<进度执行>排迁实际样式
function doPQ_SJ(obj){
	if(obj.ISPQ == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.PQ_SJ;
	}
}

//<进度计划及执行>可研批复样式
function doKYPF(obj){
	if(obj.ISKYPF == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.KYPF;
	}
}
//<进度计划及执行>划拨决定书样式
function doHPJDS(obj){
	if(obj.ISHPJDS == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.HPJDS;
	}
}

//<进度计划及执行>规划许可证样式
function doGCXKZ(obj){
	if(obj.ISGCXKZ == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.GCXKZ;
	}
}

//<进度计划及执行>施工许可样式
function doSGXK(obj){
	if(obj.ISSGXK == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.SGXK;
	}
}

//<进度计划及执行>初步涉及批复样式
function doCBSJPF(obj){
	if(obj.ISCBSJPF == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.CBSJPF;
	}
}

//<进度计划及执行>拆迁图样式
function doCQT(obj){
	if(obj.ISCQT == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.CQT;
	}
}

//<进度计划及执行>排迁图样式
function doPQT(obj){
	if(obj.ISPQT == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.PQT;
	}
}

//<进度计划及执行>施工图样式
function doSGT(obj){
	if(obj.ISSGT == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.SGT;
	}
}

//<进度计划及执行>提报价样式
function doTBJ(obj){
	if(obj.ISTBJ == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.TBJ;
	}
}

//<进度计划及执行>财审样式
function doCS(obj){
	if(obj.ISCS == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.CS;
	}
}

//<进度计划及执行>监理单位样式
function doJLDW(obj){
	if(obj.ISJLDW == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.JLDW;
	}
}

//<进度计划及执行>施工单位样式
function doSGDW(obj){
	if(obj.ISSGDW == "0"){
		return "&nbsp;—&nbsp;";
	}else{
		return obj.SGDW;
	}
}
//判断项目是否完工，完工不显示工程进度描述--------------暂时没用
function hideGcjdms(){
	var data = null;
	return submit(controllername+"?queryXmwgById&id="+id,data,gcjdmsList);
}
//自定义提交方法--------------暂时没用
function submit(actionName, data,tablistID){
	var flag = true;
	$.ajax({
		type : 'post',
		url : actionName,
		data : data,
		cache : false,
		dataType : "json",  
		async :	true,
		success : function(result) {
			if(result.msg == 0){
				flag = false;
			}
			var rowObj = convertJson.string2json1(result.msg).response.data[0];
			if(rowObj.WGSJ_SJ == ""){
				//$("#gcjdms").show();
				flag = true;
			}else{
				//$("#gcjdms").hide();
				flag = false;
			}
			
		}
	
	});
	return flag;
}
</script>      
    
</head>
<body>
<app:dialogs/>
<div class="row-fluid">
<div style="display:inline-block;">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<form method="post" id="queryFormJdzx"  >
		      <table class="B-table" width="100%">
		      <!--可以再此处加入hidden域作为过滤条件 -->
		      	<TR  style="display:none;">
			        <TD class="right-border bottom-border"></TD>
					<TD class="right-border bottom-border">
					</TD>
		        </TR>
		      </table>
      		</form>
      		<h4 class="title">进度执行
      		<span class="pull">
			<%
				sbSql = new StringBuffer();
				sbSql.append("SELECT kgsj,kgsj_sj,wgsj,wgsj_sj,kypf,kypf_sj,hpjds,hpjds_sj,gcxkz,gcxkz_sj,sgxk,sgxk_sj,cbsjpf,cbsjpf_sj,cqt,cqt_sj,pqt,pqt_sj,sgt,sgt_sj,tbj,tbj_sj,cs,cs_sj,jldw,jldw_sj,sgdw,sgdw_sj,zc,zc_sj,pq,pq_sj,jg,jg_sj ");
				sbSql.append("FROM GC_JH_SJ WHERE XMID = '"+id+"' AND XMBS = '0' AND SFYX = '1'");
				sql = sbSql.toString();
				String[][] jdzxArray_xmxq = DBUtil.query(conn, sql);
				Date date = new Date();//获取当前日期
				String flag = "0";//用于判断开工，完工，征拆，排迁的状态
				int days = 0;//用于获取两个日期之间的差值
				if(Pub.emptyArray(jdzxArray_xmxq)){
					if(flag.equals("0")){
						if(!Pub.empty(jdzxArray_xmxq[0][0])){//开工
							if(!Pub.empty(jdzxArray_xmxq[0][1])){
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][0]),Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][1]));
							}else{
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][0]),date);
							}
							if(days <= 5){
								flag = "0";//正常情况
							}else {
								flag = "1";//延期情况
								flagText = "<开工时间>延期";
							}
						}
					}
					if(flag.equals("0")){//完工
						if(!Pub.empty(jdzxArray_xmxq[0][2])){
							if(!Pub.empty(jdzxArray_xmxq[0][3])){
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][2]),Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][3]));
							}else{
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][2]),date);
							}
							if(days <= 5){
								flag = "0";//正常情况
							}else {
								flag = "1";//延期情况
								flagText = "<完工时间>延期";
							}
						}
					}
					if(flag.equals("0")){
						if(!Pub.empty(jdzxArray_xmxq[0][28])){//征拆
							if(!Pub.empty(jdzxArray_xmxq[0][29])){
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][28]),Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][29]));
							}else{
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][28]),date);
							}
							if(days <= 5){
								flag = "0";//正常情况
							}else {
								flag = "1";//延期情况
								flagText = "<征拆时间>延期";
							}
						}
					}
					if(flag.equals("0")){
						if(!Pub.empty(jdzxArray_xmxq[0][30])){//排迁
							if(!Pub.empty(jdzxArray_xmxq[0][31])){
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][30]),Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][31]));
							}else{
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][30]),date);
							}
							if(days <= 5){
								flag = "0";//正常情况
							}else {
								flag = "1";//延期情况
								flagText = "<排迁时间>延期";
							}
						}
					}
					if(flag.equals("0")){
						if(!Pub.empty(jdzxArray_xmxq[0][4])){//可研批复
							if(!Pub.empty(jdzxArray_xmxq[0][5])){
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][4]),Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][5]));
							}else{
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][4]),date);
							}
							if(days <= 5){
								flag = "0";//正常情况
							}else {
								flag = "2";//延期情况
								flagText = "<可研批复时间>延期";
							}
						}
					} 
					if(flag.equals("0")){
						if(!Pub.empty(jdzxArray_xmxq[0][6])){//计划划拨决定书
							if(!Pub.empty(jdzxArray_xmxq[0][7])){
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][6]),Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][7]));
							}else{
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][6]),date);
							}
							if(days <= 5){
								flag = "0";//正常情况
							}else {
								flag = "2";//延期情况
								flagText = "<划拔决定书时间>延期";
							}
						}
					} 
					if(flag.equals("0")){
						if(!Pub.empty(jdzxArray_xmxq[0][8])){//工程规划许可证
							if(!Pub.empty(jdzxArray_xmxq[0][9])){
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][8]),Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][9]));
							}else{
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][8]),date);
							}
							if(days <= 5){
								flag = "0";//正常情况
							}else {
								flag = "2";//延期情况
								flagText = "<规划许可证时间>延期";
							}
						}
					} 
					if(flag.equals("0")){
						if(!Pub.empty(jdzxArray_xmxq[0][10])){//施工许可
							if(!Pub.empty(jdzxArray_xmxq[0][11])){
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][10]),Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][11]));
							}else{
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][10]),date);
							}
							if(days <= 5){
								flag = "0";//正常情况
							}else {
								flag = "2";//延期情况
								flagText = "<施工许可时间>延期";
							}
						}
					} 
					if(flag.equals("0")){
						if(!Pub.empty(jdzxArray_xmxq[0][12])){//初步设计批复
							if(!Pub.empty(jdzxArray_xmxq[0][13])){
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][12]),Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][13]));
							}else{
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][12]),date);
							}
							if(days <= 5){
								flag = "0";//正常情况
							}else {
								flag = "2";//延期情况
								flagText = "<初步设计批复时间>延期";
							}
						}
					} 
					 if(flag.equals("0")){
						if(!Pub.empty(jdzxArray_xmxq[0][14])){//拆迁图
							if(!Pub.empty(jdzxArray_xmxq[0][15])){
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][14]),Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][15]));
							}else{
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][14]),date);
							}
							if(days <= 5){
								flag = "0";//正常情况
							}else {
								flag = "2";//延期情况
								flagText = "<拆迁图时间>延期";
							}
						}
					} 
					 if(flag.equals("0")){
						if(!Pub.empty(jdzxArray_xmxq[0][16])){//排迁图
							if(!Pub.empty(jdzxArray_xmxq[0][17])){
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][16]),Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][17]));
							}else{
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][16]),date);
							}
							if(days <= 5){
								flag = "0";//正常情况
							}else {
								flag = "2";//延期情况
								flagText = "<排迁图时间>延期";
							}
						}
					} 
					if(flag.equals("0")){
						if(!Pub.empty(jdzxArray_xmxq[0][18])){//施工图
							if(!Pub.empty(jdzxArray_xmxq[0][19])){
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][18]),Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][19]));
							}else{
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][18]),date);
							}
							if(days <= 5){
								flag = "0";//正常情况
							}else {
								flag = "2";//延期情况
								flagText = "<施工图时间>延期";
							}
						}
					} 
					if(flag.equals("0")){
						if(!Pub.empty(jdzxArray_xmxq[0][20])){//提报价
							if(!Pub.empty(jdzxArray_xmxq[0][21])){
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][20]),Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][21]));
							}else{
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][20]),date);
							}
							if(days <= 5){
								flag = "0";//正常情况
							}else {
								flag = "2";//延期情况
								flagText = "<提报价时间>延期";
							}
						}
					} 
					if(flag.equals("0")){
						if(!Pub.empty(jdzxArray_xmxq[0][22])){//造价财审
							if(!Pub.empty(jdzxArray_xmxq[0][23])){
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][22]),Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][23]));
							}else{
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][22]),date);
							}
							if(days <= 5){
								flag = "0";//正常情况
							}else {
								flag = "2";//延期情况
								flagText = "<造价财审时间>延期";
							}
						}
					} 
					if(flag.equals("0")){
						if(!Pub.empty(jdzxArray_xmxq[0][24])){//招标监理单位
							if(!Pub.empty(jdzxArray_xmxq[0][25])){
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][24]),Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][25]));
							}else{
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][24]),date);
							}
							if(days <= 5){
								flag = "0";//正常情况
							}else {
								flag = "2";//延期情况
								flagText = "<监理单位时间>延期";
							}
						}
					} 
					if(flag.equals("0")){
						if(!Pub.empty(jdzxArray_xmxq[0][26])){//招标施工单位
							if(!Pub.empty(jdzxArray_xmxq[0][27])){
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][26]),Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][27]));
							}else{
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][26]),date);
							}
							if(days <= 5){
								flag = "0";//正常情况
							}else {
								flag = "2";//延期情况
								flagText = "<施工单位时间>延期";
							}
						}
					}
					if(flag.equals("0")){
						if(!Pub.empty(jdzxArray_xmxq[0][32])){//交工
							if(!Pub.empty(jdzxArray_xmxq[0][33])){
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][32]),Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][33]));
							}else{
								days = Pub.daysBetween(Pub.toDate("yyyy-MM-dd", jdzxArray_xmxq[0][32]),date);
							}
							if(days <= 5){
								flag = "0";//正常情况
							}else {
								flag = "2";//延期情况
								flagText = "<交工时间>延期";
							}
						}
					}
			%>
			<%
					if(flag.equals("0")){
			%>
				<i title="正常执行" class="icon-green"></i>
			<%
					}else if(flag.equals("1")){
			%>
				<i title="延期执行" class="icon-red"></i>&nbsp;&nbsp;<font size="2"><%=flagText%></font>
			<%
					}else if(flag.equals("2")){
			%>
				<i title="部分延期执行" class="icon-yellow"></i>
			<%
					}
				}
            %>
  				
      		</span>
			</h4>
			<table class="table-hover table-activeTd B-table" id="jdzxList" width="100%" noPage="true" pageNum="1000">
				<thead>
					<tr>
						<th id="_XH" name="XH" tdalign="center" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
						<th fieldname="XMMC" rowspan="2" colindex=2>&nbsp;项目/标段名称&nbsp;</th>
						<th colspan="2">&nbsp;征拆&nbsp;</th>
						<th colspan="2">&nbsp;排迁&nbsp;</th>
						<th colspan="2">&nbsp;开工&nbsp;</th>
						<th colspan="2">&nbsp;完工&nbsp;</th>
						<th fieldname="GQ" tdalign="center" rowspan="2" colindex=11>&nbsp;建设&nbsp;<br>&nbsp;工期&nbsp;</th>
					</tr>
					<tr>
						<th fieldname="ZC" tdalign="center" CustomFunction="doZC" colindex=3 style="width:82px">&nbsp;计划&nbsp;</th>
						<th fieldname="ZC_SJ" tdalign="center" CustomFunction="doZC_SJ" colindex=4 style="width:82px">&nbsp;实际&nbsp;</th>
						
						<th fieldname="PQ" tdalign="center" CustomFunction="doPQ" colindex=5 style="width:82px">&nbsp;计划&nbsp;</th>
						<th fieldname="PQ_SJ" tdalign="center" CustomFunction="doPQ_SJ" colindex=6 style="width:82px">&nbsp;实际&nbsp;</th>
						
						<th fieldname="KGSJ" tdalign="center" CustomFunction="doKG" colindex=7 style="width:82px">&nbsp;计划&nbsp;</th>
						<th fieldname="KGSJ_SJ" tdalign="center" CustomFunction="doKG_SJ" colindex=8 style="width:82px">&nbsp;实际&nbsp;</th>
						
						<th fieldname="WGSJ" tdalign="center" CustomFunction="doWG" colindex=9 style="width:82px">&nbsp;计划&nbsp;</th>
						<th fieldname="WGSJ_SJ" tdalign="center" colindex=10 CustomFunction="doWG_SJ" style="width:82px">&nbsp;实际&nbsp;</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
	</div>
	<br>
	
<!-- 进度计划及执行 -->
	<div class="B-small-from-table-autoConcise">
		<h4 class="title">进度计划及执行</h4>
		<form method="post" id="queryFormJdjhjzx">
	      <table class="B-table" width="100%">
	      <!--可以再此处加入hidden域作为过滤条件 -->
	      	<TR  style="display:none;">
		        <TD class="right-border bottom-border"></TD>
				<TD class="right-border bottom-border">
				</TD>
	        </TR>
	      </table>
     	</form>
		<table class="table-hover table-activeTd B-table" id="jdjhjzxList" width="100%" noPage="true" pageNum="1000">
				<thead>
					<tr>
						<th fieldname="XMMC" rowMerge="true" style="width:120px">&nbsp;项目/标段名称&nbsp;</th>
						<th fieldname="LX" tdalign="center" style="width:38px">&nbsp;&nbsp;</th>
						<th fieldname="KYPF" tdalign="center" CustomFunction="doKYPF" style="width:82px">&nbsp;可研批复&nbsp;</th>
						<th fieldname="HPJDS" tdalign="center" CustomFunction="doHPJDS" style="width:82px">&nbsp;划拨决定书&nbsp;</th>
						<th fieldname="GCXKZ" tdalign="center" CustomFunction="doGCXKZ" style="width:82px">&nbsp;规划许可证&nbsp;</th>
						<th fieldname="SGXK" tdalign="center" CustomFunction="doSGXK" style="width:82px">&nbsp;施工许可&nbsp;</th>
						<th fieldname="CBSJPF" tdalign="center" CustomFunction="doCBSJPF" style="width:82px">&nbsp;初步设计批复&nbsp;</th>
						<th fieldname="CQT" tdalign="center" CustomFunction="doCQT" style="width:82px">&nbsp;拆迁图&nbsp;</th>
						<th fieldname="PQT" tdalign="center" CustomFunction="doPQT" style="width:82px">&nbsp;排迁图&nbsp;</th>
						<th fieldname="SGT" tdalign="center" CustomFunction="doSGT" style="width:82px">&nbsp;施工图&nbsp;</th>
						<th fieldname="TBJ" tdalign="center" CustomFunction="doTBJ" style="width:82px">&nbsp;提报价&nbsp;</th>
						<th fieldname="CS" tdalign="center" CustomFunction="doCS" style="width:82px">&nbsp;财审&nbsp;</th>
						<th fieldname="JLDW" tdalign="center" CustomFunction="doJLDW" style="width:82px">&nbsp;监理单位&nbsp;</th>
						<th fieldname="SGDW" tdalign="center" CustomFunction="doSGDW" style="width:82px">&nbsp;施工单位&nbsp;</th>
					</tr>
					
				</thead>
				<tbody></tbody>
			</table>
	</div>
	<br>
<!-- 进度计划及执行-end -->
<!-- 工程进度描述 -->
	<div class="row-fluid" id="gcjdms">
		<div class="B-small-from-table-autoConcise">
		<h4 class="title">工程进度描述</h4>
			<div id="gcjdms_div" style="">
			<table class="table-hover table-activeTd B-table" id="gcjdmsList" width="100%" >
				<%
					sbSql = new StringBuffer();
	                sbSql.append("SELECT ISNOBDXM FROM GC_JH_SJ WHERE XMID = '"+id+"' and xmbs = '0'");
	   	          	sql = sbSql.toString();
		   	        String[][] xmOrbdArray_gcjd = DBUtil.query(conn, sql);
		   	        String querySql_gcjd = "";
		   	        String isnobdxm = "";
		   	        String isWhere = "";
		   	        if(Pub.emptyArray(xmOrbdArray_gcjd)){
		   	        	if(xmOrbdArray_gcjd[0][0].equals("0")){
		   	        		isnobdxm = " BDMC||'-'||BDBH ";
		   	        		isWhere = " XMBS = '1' ";
		   	        	}else{
		   	        		isnobdxm = " XMMC ";
		   	        		isWhere = " XMBS = '0' ";
		   	        	}
	   	        		querySql_gcjd = "SELECT"+isnobdxm+"as XMMC,WGSJ_SJ,JG_SJ,JS_SJ,(SELECT LJWC FROM gc_xmglgs_zbb Z WHERE Z.JHSJID = T.GC_JH_SJ_ID "+
	   	    					"AND Z.JSSJ =(SELECT MAX(JSSJ) FROM gc_xmglgs_zbb WHERE SFYX = '1' AND JHSJID = T.GC_JH_SJ_ID)and rownum=1)AS LJWC "+
	   	    					"FROM GC_JH_SJ T "+
	   	    					"WHERE T.XMID = '"+id+"' AND "+isWhere+" "+
	   	    					"ORDER BY GC_JH_SJ_ID ";
	   	        		QuerySet qs = DBUtil.executeQuery(querySql_gcjd, null, conn);
	   	        		if(qs != null && qs.getRowCount()>0){
	   	        			String xmmc = "";//项目名称
	   	        			String LJWC = "";//累计完成（字符串）
	   	        			String WGSJ = "";//实际完工时间
	   	        			String JGSJ = "";//实际交工时间
	   	        			String JSSJ = "";//实际竣工时间
	   	        			Blob gcjz = null;//blob字段值
	   	   					for(int i=1; i<=qs.getRowCount();i++){
	   	   						xmmc = qs.getString(i, "XMMC");
	   	   						WGSJ = qs.getString(i, "WGSJ_SJ");
	   	   						JGSJ = qs.getString(i, "JG_SJ");
	   	   						JSSJ = qs.getString(i, "JS_SJ");
	   	   						if(WGSJ == null || WGSJ == ""){
	   	   							if(JGSJ == null || JGSJ == ""){
	   	   								if(JSSJ == null || JSSJ == ""){
		   	   								gcjz = (Blob)qs.getObject(i, "LJWC");
				   	   						wgFlag = false;//完工/交工/竣工隐藏区域标识
				   	   						if(gcjz != null){
				   	   							int length = (int) gcjz.length();
				   	   							byte[] buffer = gcjz.getBytes(1, length);
				   	   							LJWC = new String(buffer);
				   	   						}else{
				   	   							LJWC = "";
				   	   						}
	   	   								}else{
	   	   									LJWC = "已竣工";
	   	   								}
	   	   							}else{
	   	   								LJWC = "已交工";
	   	   							}
	   	   						}else{
	   	   							LJWC = "已完工";
	   	   						}
	   	   						
	   	   			%>
	   	   			<tr>
						<th class="right-border bottom-border text-right" align="center" width="7%">项目/标段名称</th>
						<td width="20%"><%=xmmc %></td>
						<th width="5%" class="right-border bottom-border text-right" align="center">工程进度</th>
						<td width="68%">
							<textarea id="ljwc" class="span12" rows="3" ondblclick="changeRows(this)" style="border-style:none;background:#FFF;box-shadow:none;overflow:hidden" readOnly><%=LJWC %></textarea>
						</td>
					</tr>
	   	   		<%
	   	   					}
			   	    	 }
		   	        }
				%>
			</table>
			</div>
		</div>
	</div>
	<!-- 工程进度描述-end -->
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
<script type="text/javascript">
var wgFlag = <%=wgFlag%>;
if(wgFlag){
	$("#gcjdms").hide();
}else{
	$("#gcjdms").show();
}
</script>
</html>