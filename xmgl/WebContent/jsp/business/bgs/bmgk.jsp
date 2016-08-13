
<%@page import="com.mysql.jdbc.PreparedStatement"%><!DOCTYPE html>
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
	String year = "";
	int xmglgsZs=0;
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName= dept.getDept_Name();
	Date d=new Date();//获取时间
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//转换格式
	SimpleDateFormat sdf1=new SimpleDateFormat("yyyy");//转换格式
	String today=sdf.format(d);
	year =request.getParameter("nd");
	if(Pub.empty(year))
	{	
		year=sdf1.format(d);
		String maxNdSql = "select max(ND) from GC_JH_SJ where SFYX='1'";
		String[][] maxNdSqlArr = DBUtil.query(conn, maxNdSql);
		if(maxNdSqlArr!=null&&maxNdSqlArr.length>0){
			year = maxNdSqlArr[0][0];
		}else{
			year=sdf1.format(d);
		}
	}
		sbSql = new StringBuffer();
		sbSql.append("select count(t.row_id)as xmglgsCount from VIEW_YW_XMGLGS t ");
		sql = sbSql.toString();
		String[][] xmglgsCountResult1 = DBUtil.query(conn, sql);
		xmglgsZs = Integer.parseInt(xmglgsCountResult1[0][0].toString());
		 sbSql = new StringBuffer();
		 sbSql.append("select t.row_id,t.bmjc from VIEW_YW_XMGLGS t ");
	     sql = sbSql.toString();
	     String[][] xmglgsValue1 = DBUtil.query(conn, sql);
	     StringBuffer aaa=new StringBuffer();
	     for(int i=0;i<xmglgsZs;i++){
	    	 aaa.append(xmglgsValue1[i][0].toString()+",");
	    	
	     }
%>
	<app:base />
	<head>
		<title>部长概况</title>
		<script type="text/javascript"
			src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/FixTable.js"></script> 
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
	border-top: #fff solid 1px;
	border-bottom: #fff solid 1px;
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
.tableTrEvenColor{
	background:  #E7E7E7;
}
.tableList td{
	font-size:14px;
	color:#000;
}
.tableTheadTr{
	border-bottom: #fff solid 1px; 
	border-top: #fff solid 1px;
	color: #fff;
	background:#36648B;
}
.tableList a,.tableList a:focus, .tableList a:hover, .tableList a:active{
	color:#000;
	font-size:14px;
}
.bmjkTitleLine{
	background: none;
	color: #000; 
	border-bottom: #5E5E5E solid 1px;
	margin-bottom:3px;
	font-size:20px;
	padding-bottom:5px;
}
</style>
<script type="text/javascript">			
var controllername= "${pageContext.request.contextPath }/BgsBmgk.do";

//页面初始化
function init(){
	var nd1=<%=year%>;
	$("#ND").val(nd1);
	faWenQingKuang(nd1);
	$("#printButton").click(function(){
		$(this).hide();
		window.print();
		$(this).show();
	});
	}
$(function(){
	init();
	});
//根据年度查询
function queryND()
{
	var nd = $("#ND").val();
	window.location.href="${pageContext.request.contextPath }/jsp/business/bgs/bmgk.jsp?nd="+nd;
}
//收发文情况
function faWenQingKuang(nd)
{
	//存在问题情况
	var data = combineQuery.getQueryCombineData(queryForm1,frmPost,tcjhTable);
	defaultJson.doQueryJsonList(controllername+"?tongChouJiHuaQingKuang&nd="+nd,data,tcjhTable,null,true);
	//发文情况
	var data = combineQuery.getQueryCombineData(queryForm1,frmPost,FWTable);
	defaultJson.doQueryJsonList(controllername+"?faWenQingKuang&nd="+nd,data,FWTable,null,true);
	//收文情况
	var data = combineQuery.getQueryCombineData(queryForm1,frmPost,SwTable);
	defaultJson.doQueryJsonList(controllername+"?shouWenQingKuang&nd="+nd,data,SwTable,null,true);
	//流程执行情况
	var data = combineQuery.getQueryCombineData(queryForm1,frmPost,LczxqkTable);
	defaultJson.doQueryJsonList(controllername+"?liuChengQingKuang&nd="+nd,data,LczxqkTable,null,true);
	//存在问题情况
	var data = combineQuery.getQueryCombineData(queryForm1,frmPost,czwtqkTable);
	defaultJson.doQueryJsonList(controllername+"?cunZaiWenTiQingKuang&nd="+nd,data,czwtqkTable,null,true);
	tableClass("tcjhTable");
	tableClass("FWTable");
	tableClass("SwTable");
	tableClass("LczxqkTable");
	tableClass("czwtqkTable");
	tableClass("HtList");
}
function tableClass(tableID){
	$("#"+tableID).removeClass();
	$($("#"+tableID+" tbody>tr:even")).addClass("tableTrEvenColor");
	$("#"+tableID).addClass("table3 tableList");
	$($("#"+tableID+" thead tr:eq(0)")).addClass("tableTheadTr");
	$($("#"+tableID+" thead tr:eq(1)")).addClass("tableTheadTr");
	$($($("#"+tableID+" thead tr:eq(0)")).find("th")).addClass("tableTdAlign");
}
//统筹计划节点完成情况 -总数
function tcjhjdZsView(obj){
	if(obj.ZS == 0){
		return '0';
	}else{
		return '<a href="javascript:void(0);" onclick="queryTCJHJDList(\'020001\',\'BMJK_BGS_ZS'+obj.IDNUM+'\')">'+obj.ZS+'</a>';
	}
}
//统筹计划节点完成情况 -完成
function tcjhjdwcView(obj){
	if(obj.WC == 0){
		return '0';
	}else{
		return '<a href="javascript:void(0);" onclick="queryTCJHJDList(\'020001\',\'BMJK_BGS_WC'+obj.IDNUM+'\')">'+obj.WC+'</a>';
	}
}
//统筹计划节点完成情况 -正常办理中
function tcjhjdzcblzView(obj){
	if(obj.ZC == 0){
		return '0';
	}else{
		return '<a href="javascript:void(0);" onclick="queryTCJHJDList(\'020001\',\'BMJK_BGS_ZC'+obj.IDNUM+'\')">'+obj.ZC+'</a>';
	}
}
//统筹计划节点完成情况 -预警
function tcjhjdyjView(obj){
	if(obj.YJ == 0){
		return '0';
	}else{
		return '<a href="javascript:void(0);" onclick="queryTCJHJDList(\'020001\',\'BMJK_BGS_YJ'+obj.IDNUM+'\')">'+obj.YJ+'</a>';
	}
}
//统筹计划节点完成情况 - 超期完成
function tcjhjdcqwcView(obj){
	if(obj.CQWC == 0){
		return '0';
	}else{
		return '<a href="javascript:void(0);" onclick="queryTCJHJDList(\'020001\',\'BMJK_BGS_CQWC'+obj.IDNUM+'\')">'+obj.CQWC+'</a>';
	}
}
//统筹计划节点完成情况 - 未完成已反馈 
function tcjhjdwwcyfkView(obj){
	if(obj.WWYFK == 0){
		return '0';
	}else{
		return '<a href="javascript:void(0);" onclick="queryTCJHJDList(\'020001\',\'BMJK_BGS_WWYFK'+obj.IDNUM+'\')">'+obj.WWYFK+'</a>';
	}
}
//统筹计划节点完成情况 - 未完成未反馈 
function tcjhjdwwcwfkView(obj){
	if(obj.WWWFK == 0){
		return '0';
	}else{
		return '<a href="javascript:void(0);" onclick="queryTCJHJDList(\'020001\',\'BMJK_BGS_WWWFK'+obj.IDNUM+'\')">'+obj.WWWFK+'</a>';
	}
}

//<涉及项目>连接方法
function queryTCJHJDList(ywlx, bmjkLx){
	var nd = $("#ND").val();
	xmxx_bdxxView(ywlx, nd, bmjkLx);
}
//流程执行情况 -总数
function lczxqkzsView(obj){
	if(obj.LCZS == 0){
		return '0';
	}else{
		if(obj.IDNUM=='23'){
				return '<a href="javascript:void(0);" onclick="querylcjkList(\'bgs\',\'BMJK_BGS_LCJK_ZS\',\'\')">'+obj.LCZS+'</a>';
			}else{
				return '<a href="javascript:void(0);" onclick="querylcjkList(\'bgs\',\'BMJK_BGS_LCJK_ZS\','+obj.CJDWDM+')">'+obj.LCZS+'</a>';
			}
		}
}
//流程执行情况 -完成
function lczxqkwcView(obj){
	if(obj.LCWC == 0){
		return '0';
	}else{
		if(obj.IDNUM=='23'){
			return '<a href="javascript:void(0);" onclick="querylcjkList(\'bgs\',\'BMJK_BGS_LCJK_WC\',\'\')">'+obj.LCWC+'</a>';
		}else{
			return '<a href="javascript:void(0);" onclick="querylcjkList(\'bgs\',\'BMJK_BGS_LCJK_WC\','+obj.CJDWDM+')">'+obj.LCWC+'</a>';
		}
		}
}
//流程执行情况 -正常办理中
function lczxqkzcView(obj){
	if(obj.LCZCBLZ == 0){
		return '0';
	}else{
		if(obj.IDNUM=='23'){
			return '<a href="javascript:void(0);" onclick="querylcjkList(\'bgs\',\'BMJK_BGS_LCJK_ZC\',\'\')">'+obj.LCZCBLZ+'</a>';
		}else{
			return '<a href="javascript:void(0);" onclick="querylcjkList(\'bgs\',\'BMJK_BGS_LCJK_ZC\','+obj.CJDWDM+')">'+obj.LCZCBLZ+'</a>';
			}
		}
}
//流程执行情况 -预警
function lczxqkyjView(obj){
	if(obj.LCYJ == 0){
		return '0';
	}else{
		if(obj.IDNUM=='23'){
			return '<a href="javascript:void(0);" onclick="querylcjkList(\'bgs\',\'BMJK_BGS_LCJK_YJ\',\'\')">'+obj.LCYJ+'</a>';
		}else{
			return '<a href="javascript:void(0);" onclick="querylcjkList(\'bgs\',\'BMJK_BGS_LCJK_YJ\','+obj.CJDWDM+')">'+obj.LCYJ+'</a>';
			}
		}
}
//流程执行情况 -超期完成
function lczxqkcqwcView(obj){
	if(obj.LCCQ == 0){
		return '0';
	}else{
		if(obj.IDNUM=='23'){
			return '<a href="javascript:void(0);" onclick="querylcjkList(\'bgs\',\'BMJK_BGS_LCJK_CQWC\',\'\')">'+obj.LCCQ+'</a>';
		}else{
			return '<a href="javascript:void(0);" onclick="querylcjkList(\'bgs\',\'BMJK_BGS_LCJK_CQWC\','+obj.CJDWDM+')">'+obj.LCCQ+'</a>';
			}
		}
}
//流程执行情况 -超期未完成
function lczxqkcqwwcView(obj){
	if(obj.LCCQWWC == 0){
		return '0';
	}else{
		if(obj.IDNUM=='23'){
			return '<a href="javascript:void(0);" onclick="querylcjkList(\'bgs\',\'BMJK_BGS_LCJK_CQWWC\',\'\')">'+obj.LCCQWWC+'</a>';
		}else{
				return '<a href="javascript:void(0);" onclick="querylcjkList(\'bgs\',\'BMJK_BGS_LCJK_CQWWC\','+obj.CJDWDM+')">'+obj.LCCQWWC+'</a>';
				}
		}
}
function querylcjkList(ywlx, bmjkLx,deptid){
	var nd1=$("#ND").val();
	var condition = "ywlx="+ywlx+"&bmjkLx="+bmjkLx+"&deptid="+deptid+"&nd="+nd1;
	var  xmsc = {"title":"流程查询","type":"text","content":g_sAppName+"/jsp/business/bgs/bgsliuccx.jsp?"+condition,"modal":"1"};
	$(window).manhuaDialog(xmsc);
	}
	function tcjhgkzsView(lie,pch)
	{
		var ywlx='020001';
		var nd = $("#ND").val();
		if(lie=='2'){
			var bmjkLx='BMJK_BGS_TCJH_ZS';
			xmxx_bdxxView(ywlx, nd, bmjkLx,pch);
		}if(lie=='3'){
			var bmjkLx='BMJK_BGS_TCJH_YJ';
			xmxx_bdxxView(ywlx, nd, bmjkLx,pch);
		}if(lie=='4'){
			var bmjkLx='BMJK_BGS_TCJH_BT';
			xmxx_bdxxView(ywlx, nd, bmjkLx,pch);
		}if(lie=='5'){
			var bmjkLx='BMJK_BGS_TCJH_TZ';
			xmxx_bdxxView(ywlx, nd, bmjkLx,pch);
		}if(lie>5)
			{
			xmglgs(lie,pch,nd,ywlx);
			}
	}
	function xmglgs(lie,pch,nd,ywlx){
		var a = '<%=aaa%>';
		var b=a.split(",");
		for(var i=0;i<b.length-1;i++){
			if(lie==i+6){
				var bmjkLx='BMJK_BGS_TCJH_XMGLGS';
				xmxx_bdxxView(ywlx, nd, bmjkLx,pch,b[i]);
			}
		}
		if(lie==b.length+5){
			var bmjkLx='BMJK_BGS_TCJH_XMGLGSWZD';
			xmxx_bdxxView(ywlx, nd, bmjkLx,pch);
		}
	}
</script>
	</head>
	<body>
		<app:dialogs />
					<span class="pull-right">
				    	<button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
				    </span>
				<form method="post" id="queryForm" style="margin:1px;">
					<h4 class="bmjkTitleLine">统筹计划概况&nbsp;&nbsp;
					<select class="span2 year" style="width: 8%" id="ND" onchange="queryND()" name="ND" fieldname="ND"  operation="=" defaultMemo="全部" kind="dic" noNullSelect ="true"  src="T#GC_JH_SJ: distinct ND as NDCODE:ND:SFYX='1' ORDER BY NDCODE asc">
		   			</select></h4>
				</form>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<div class="overFlowX">
						<div class="test">
					<table width="98%"  class="table-hover table-activeTd B-table" id="HtList" type="single" noPage="true" pageNum="1000">
		<thead>
			<tr class="bzgk-table-tr-Border">
     			<th rowspan="2" align="center" valign="middle">批次</th>
       			<th rowspan="2" align="center" valign="middle">下达时间</th>
       			<th colspan="4" align="center" valign="middle">项目数量</th>
				<%
            	//项目管理公司总个数
            	sbSql = new StringBuffer();
          		sbSql.append("select count(t.row_id)as xmglgsCount from VIEW_YW_XMGLGS t ");
				sql = sbSql.toString();
				String[][] xmglgsCountResult = DBUtil.query(conn, sql);
				int xmglgsCoount = 0;//总数
		       	if(Pub.emptyArray(xmglgsCountResult)){
		     		xmglgsCoount = Integer.parseInt(xmglgsCountResult[0][0].toString());
		       	}
            	%>
				<th colspan="<%= xmglgsCoount+1%>" align="center" valign="middle">项目管理公司</th>
          		<th rowspan="2" align="center"  valign="middle" CustomFunction="tcjhgkztzView">总投资（万元）</th>
			</tr>
        	<tr>
				<th valign="middle" >总数</th>
	           	<th valign="middle" >应急</th>
	           	<th valign="middle" >BT</th>
	           	<th valign="middle" >调整</th>
	           	<%
	             sbSql = new StringBuffer();
	        	 sbSql.append("select t.row_id,t.bmjc from VIEW_YW_XMGLGS t ");
		         sql = sbSql.toString();
		         String[][] xmglgsValue = DBUtil.query(conn, sql);
		         String xmglgsId = "";
		         String xmglgsText = "";
		         if(Pub.emptyArray(xmglgsValue)){
		       	 	for(int i = 0; i < xmglgsCoount; i++){
		       			xmglgsText = xmglgsValue[i][1].toString();
		       	%>
				<th valign="middle"><%=xmglgsText %></th>
	        	<%
					}
	      		}
            	%>
				<th valign="middle">未指定</th>
			</tr>
		</thead>
	<tbody>
        <%
        sbSql = new StringBuffer();
        sbSql.append("SELECT * FROM ");
        sbSql.append("(SELECT DISTINCT T.JHPCH,");
        sbSql.append("TO_CHAR(T.XDRQ, 'yyyy-MM-dd') as XDRQ,");
        sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE JHID = T.GC_JH_ZT_ID AND ND ='"+year+"' AND XMBS = '0' AND SFYX = '1') AS ZS,");
        sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE JHID = T.GC_JH_ZT_ID AND XMSX = '2' AND ND = '"+year+"' AND XMBS = '0' AND SFYX = '1') AS YJ,");
        sbSql.append("(SELECT COUNT(GC_TCJH_XMXDK_ID) as bt FROM GC_TCJH_XMXDK WHERE ISBT = '1' AND GC_TCJH_XMXDK_ID in(select DISTINCT XMID from gc_jh_sj where nd =  '"+year+"' AND JHID =T.GC_JH_ZT_ID)),");
        sbSql.append("(SELECT COUNT(DISTINCT XMID) FROM GC_JH_BGXM WHERE T.GC_JH_ZT_ID = JHID AND SFXG = '1' AND T.ND ='"+year+"' AND SFYX = '1') AS TZ,");
        for(int i = 0; i < xmglgsCoount; i++){
        	xmglgsId = xmglgsValue[i][0].toString();
        	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE XMGLGS ='"+xmglgsId+"' AND ND ='"+year+"' AND SFYX = '1' AND XMBS = '0' AND JHID = T.GC_JH_ZT_ID),");
        }
        sbSql.append("(SELECT COUNT(GC_TCJH_XMXDK_ID) FROM GC_TCJH_XMXDK WHERE XMGLGS IS NULL AND GC_TCJH_XMXDK_ID IN(SELECT XMID FROM GC_JH_SJ WHERE JHID = T.GC_JH_ZT_ID AND ND ='"+year+"' AND SFYX = '1'))AS QT,");
        sbSql.append("(SELECT trunc(SUM(JHZTZE)/10000,5) FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID IN (SELECT XMID FROM GC_JH_SJ WHERE JHID = T.GC_JH_ZT_ID AND ND ='"+year+"' AND SFYX = '1')) AS ZTZ ");
        sbSql.append("FROM GC_JH_ZT T, GC_JH_SJ T1 WHERE T1.JHID = T.GC_JH_ZT_ID AND T.ND = '"+year+"' AND t.JHPCH NOT LIKE '零星%' ORDER BY t.JHPCH ASC)");
        sbSql.append(" UNION ALL ");
        sbSql.append("SELECT * FROM ");
        sbSql.append("(SELECT DISTINCT T.JHPCH,");
        sbSql.append("TO_CHAR(T.XDRQ, 'yyyy-MM-dd') as XDRQ,");
        sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE JHID = T.GC_JH_ZT_ID AND ND ='"+year+"' AND XMBS = '0' AND SFYX = '1') AS ZS,");
        sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE JHID = T.GC_JH_ZT_ID AND XMSX = '2' AND ND = '"+year+"' AND XMBS = '0' AND SFYX = '1') AS YJ,");
        sbSql.append("(SELECT COUNT(GC_TCJH_XMXDK_ID) as bt  FROM GC_TCJH_XMXDK  WHERE ISBT = '1'  AND GC_TCJH_XMXDK_ID in  (select DISTINCT XMID  from gc_jh_sj where nd = '"+year+"'  AND JHID = T.GC_JH_ZT_ID)) AS BT,");
        sbSql.append("(SELECT COUNT(DISTINCT XMID) FROM GC_JH_BGXM WHERE T.GC_JH_ZT_ID = JHID AND SFXG = '1' AND T.ND ='"+year+"' AND SFYX = '1') AS TZ,");
        for(int i = 0; i < xmglgsCoount; i++){
        	xmglgsId = xmglgsValue[i][0].toString();
        	sbSql.append("(SELECT COUNT(GC_TCJH_XMXDK_ID) FROM GC_TCJH_XMXDK WHERE XMGLGS ='"+xmglgsId+"' AND GC_TCJH_XMXDK_ID IN(SELECT XMID FROM GC_JH_SJ WHERE JHID = T.GC_JH_ZT_ID AND ND ='"+year+"' AND SFYX = '1')),");
        }
        sbSql.append("(SELECT COUNT(GC_TCJH_XMXDK_ID) FROM GC_TCJH_XMXDK WHERE XMGLGS IS NULL AND GC_TCJH_XMXDK_ID IN(SELECT XMID FROM GC_JH_SJ WHERE JHID = T.GC_JH_ZT_ID AND ND ='"+year+"' AND SFYX = '1'))AS QT,");
        sbSql.append("(SELECT trunc(SUM(JHZTZE)/10000,5) FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID IN (SELECT XMID FROM GC_JH_SJ WHERE JHID = T.GC_JH_ZT_ID AND ND ='"+year+"' AND SFYX = '1')) AS ZTZ ");
        sbSql.append("FROM GC_JH_ZT T, GC_JH_SJ T1 WHERE T1.JHID = T.GC_JH_ZT_ID AND T.ND = '"+year+"' AND t.JHPCH LIKE '零星%' ORDER BY t.JHPCH ASC) WHERE ROWNUM = 1 ");
        sql = sbSql.toString();
        String[][] jhpcResult = DBUtil.query(conn, sql);
        System.out.println("......>"+sql);
        %>   
        <%
        	int hj[] = new int[8+xmglgsCoount];//定义整体数组，用于合计计算
        	String ztz = "0";//合计金额，特殊处理
        	int cellCount = 8+xmglgsCoount;//总列数
        	if(Pub.emptyArray(jhpcResult)){
        		for(int i=0;i<jhpcResult.length;i++){
        			for(int j = 2;j < cellCount-1;j++){
        				if(jhpcResult[i][j].equals("") || jhpcResult[i][j] == null){
        					jhpcResult[i][j] = "0";
        				} 
        				hj[j] += Integer.parseInt(jhpcResult[i][j]);
        			}
        			if(jhpcResult[i][cellCount-1].equals("")){
        				jhpcResult[i][cellCount-1] = "0";
        			}
        			ztz = MathTool.add(ztz, jhpcResult[i][cellCount-1]);
        %>
        	 <% if(0 == i){%>
              	<tr class="bzgk-table-tr-Border">
              <%}else{%>
             	 <tr>
              <%}%>
              <%
              	for(int z = 0;z < cellCount-1 ;z++){
              		 if(jhpcResult[i][z].equals("0")||z==0||z==1){
              %>
              	<td align="center"><%=jhpcResult[i][z]%></td>
              <% }else{ %>
            	  <td align="center"><a href="javascript:void(0);" onclick="tcjhgkzsView(<%=z%>,'<%=jhpcResult[i][0]%>')"> <%=jhpcResult[i][z]%></a></td>
              <%}} %>
                <td align="center" style="text-align:right;padding-right:0px;">
                	<%=Pub.NumberAddDec(Pub.NumberToThousand(jhpcResult[i][cellCount-1]))%>
                </td>
              </tr>
          </tbody>
  		<% }}%>
          <tfoot>
              <tr class="bzgk-table-tr-Border">
                <td align="center"><b>合计</b></td>
                <td align="center">-</td>
                <%
                	for(int i =2;i<cellCount-1;i++){
                		 if(hj[i]==0){
                %>
                <td align="center"><%=hj[i]%></td>
                <%}else{ %>
                 <td align="center"><a href="javascript:void(0);" onclick="tcjhgkzsView(<%=i%>,'')"> <%=hj[i]%></a></td>
                <% } }%>
                <td align="center" style="text-align:right;padding-right:0px;"><%=Pub.NumberAddDec(Pub.NumberToThousand(ztz))%></td>
              </tr>
            </tfoot>
          </table>
    </div>
					</div>
				</div>
			</div>
		</div>
		<div style="height: 20px;">
		</div>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
			<tr>
				<form method="post" id="queryForm2">
					<td style="width: 305px;">
					<h4 class="bmjkTitleLine">统筹计划节点完成情况&nbsp;</h4>
					</td>
				</form>
			</tr>
		</table>
		<div class="container-fluid ">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="tcjhTable"
							width="100%" type="single" noPage="true" pageNum="1000">
							<thead>
								<tr>
									<th fieldname="JD" rowspan="2"  colindex=1 >
										专业/阶段
									</th>
									<th fieldname="ZS" rowspan="2"  CustomFunction="tcjhjdZsView" colindex=2 tdalign="center">
										总数
									</th>
									<th fieldname="WC" rowspan="2" CustomFunction="tcjhjdwcView" colindex=3 tdalign="center">
										完成
									</th>
									<th fieldname="ZC" rowspan="2" CustomFunction="tcjhjdzcblzView" colindex=4 tdalign="center">
										正常办理中
									</th>
									<th fieldname="YJ" rowspan="2" CustomFunction="tcjhjdyjView" colindex=5 tdalign="center">
										预警
									</th>
									<th colspan="3">
										超期
									</th>
								</tr>
								<tr>
									<th fieldname="CQWC" colindex=6 CustomFunction="tcjhjdcqwcView" tdalign="center" >
										&nbsp;超期完成&nbsp;
									</th>
									<th fieldname="WWYFK" colindex=7 CustomFunction="tcjhjdwwcyfkView" tdalign="center" >
										&nbsp;未完成已反馈&nbsp;
									</th>
									<th fieldname="WWWFK" colindex=8 CustomFunction="tcjhjdwwcwfkView" tdalign="center" >
										&nbsp;未完成未反馈&nbsp;
									</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div style="display:inline-block; width: 45%;" >
		<div style="height: 20px;">
		</div>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
			<tr>
				<form method="post" id="queryForm2">
					<td style="width: 305px;">
					<h4 class="bmjkTitleLine">流程执行情况&nbsp;</h4>
					</td>
				</form>
			</tr>
		</table>
		<div class="container-fluid ">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="LczxqkTable" width="100%" type="single" noPage="true" >
							<thead>
									<tr>
									<th fieldname="CJDWDM" rowspan="2" width="30%" colindex=1 >
										处理部门
									</th>
									<th fieldname="LCZS" rowspan="2" CustomFunction="lczxqkzsView" colindex=2 tdalign="center">
										总数
									</th>
									<th fieldname="LCWC" rowspan="2" CustomFunction="lczxqkwcView" colindex=3 tdalign="center">
										完成
									</th>
									<th fieldname="LCZCBLZ" rowspan="2" CustomFunction="lczxqkzcView" colindex=4 tdalign="center">
										正常办理中
									</th>
									<th fieldname="LCYJ" rowspan="2" CustomFunction="lczxqkyjView" colindex=5 tdalign="center">
										预警
									</th>
									<th colspan="2">
										超期
									</th>
								</tr>
								<tr>
									<th fieldname="LCCQ" colindex=6 CustomFunction="lczxqkcqwcView" tdalign="center" >
										&nbsp;完成&nbsp;
									</th>
									<th fieldname="LCCQWWC" colindex=7 CustomFunction="lczxqkcqwwcView" tdalign="center" >
										&nbsp;未完成&nbsp;
									</th>
									
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
			</div>
		</div></div><div style="display:inline-block; width: 55%;vertical-align:top;" >
		<div style="height: 20px;">
		</div>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
			<tr>
				<form method="post" id="queryForm2">
					<td style="width: 305px;">
					<h4 class="bmjkTitleLine">存在问题情况&nbsp;</h4>
					</td>
				</form>
			</tr>
		</table>
		<div class="container-fluid ">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="czwtqkTable"
							width="100%" type="single" noPage="true" pageNum="1000">
							<thead>
								<tr>
									<th fieldname="LRBM" rowspan="2" colindex=1 >
										主办部门
									</th>
									<th colspan="4">
										已解决
									</th>
									<th colspan="4">
										未解决
									</th>
								</tr>
								<tr>
									<th fieldname="YJJZJ" colindex=2 tdalign="center" >
										&nbsp;合计&nbsp;
									</th>
									<th fieldname="YJJYBWT" colindex=3 tdalign="center" >
										&nbsp;一般问题&nbsp;
									</th>
									<th fieldname="YJJYZWT" colindex=4 tdalign="center">
										&nbsp;严重问题&nbsp;
									</th>
									<th fieldname="YJJJQYZWT" colindex=5 tdalign="center">
										&nbsp;极其严重问题&nbsp;
									</th>
									<th fieldname="WYJJZJ" colindex=6 tdalign="center" >
										&nbsp;合计&nbsp;
									</th>
									<th fieldname="WJJYBWT" colindex=7 tdalign="center" >
										&nbsp;一般问题&nbsp;
									</th>
									<th fieldname="WJJYZWT" colindex=8 tdalign="center">
										&nbsp;严重问题&nbsp;
									</th>
									<th fieldname="WJJJQYZWT" colindex=9 tdalign="center">
										&nbsp;极其严重问题&nbsp;
									</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
			</div>
		</div></div>
		<div style="display:inline-block; width: 50%;" >
		<div style="height: 20px;">
		</div>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
			<tr>
				<form method="post" id="queryForm2">
					<td style="width: 305px;">
					<h4 class="bmjkTitleLine">发文情况&nbsp;</h4>
					</td>
				</form>
			</tr>
		</table>
		<div class="container-fluid ">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="FWTable" width="100%" type="single" noPage="true" pageNum="1000">
								<thead>
								<tr>
									<th fieldname="NGDW" rowspan="2" colindex=1  >
										发文部门
									</th>
									<th colspan="5">
										已发
									</th>
								</tr>
								<tr>
								<th fieldname="ZS" colindex=2 tdalign="center" >
										&nbsp;合计&nbsp;
									</th>
									<th fieldname="PJ" colindex=3 tdalign="center" >
										&nbsp;平件&nbsp;
									</th>
									<th fieldname="YJ" colindex=4 tdalign="center">
										&nbsp;要件&nbsp;
									</th>
									<th fieldname="JJ" colindex=5 tdalign="center">
										&nbsp;急件&nbsp;
									</th>
									<th  fieldname="TJ" colindex=6 tdalign="center">
										&nbsp;特急&nbsp;
									</th>
								</tr>
							</thead>
						 <tbody>
					     </tbody>
						</table>
					</div>
				</div>
			</div>
		</div></div><div style="display:inline-block; width: 50%;vertical-align:top;" >
		<div style="height: 20px;">
		</div>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
			<tr>
				<form method="post" id="queryForm2">
					<td style="width: 305px;">
					<h4 class="bmjkTitleLine">收文情况&nbsp;</h4>
					</td>
				</form>
			</tr>
		</table>
		<div class="container-fluid ">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="SwTable"
							width="100%" type="single" noPage="true" pageNum="1000">
							<thead>
								<tr>
									<th fieldname="WZ" rowspan="2" colindex=1 >
										文种
									</th>
									<th colspan="5">
										已收文
									</th>
								</tr>
								<tr>
								<th fieldname="ZS" colindex=2 tdalign="center" >
										&nbsp;合计&nbsp;
									</th>
									<th fieldname="PJ" colindex=3 tdalign="center" >
										&nbsp;平件&nbsp;
									</th>
									<th fieldname="YJ" colindex=4 tdalign="center">
										&nbsp;要件&nbsp;
									</th>
									<th fieldname="JJ" colindex=5 tdalign="center">
										&nbsp;急件&nbsp;
									</th>
									<th fieldname="TJ"  colindex=6 tdalign="center" >
										&nbsp;特急&nbsp;
									</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
			</div>
		</div ></div>
		
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="t.xmbh,t.xmbs,t.pxh ,t.bdbh" />
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
				<input type="hidden" name="queryResult" id="queryResult" />
			</FORM>
			 <form method="post"  id="queryForm1"  >
     			 <table class="B-table" width="100%">
				<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<INPUT id="num" type="text" class="span12" keep="true" kind="text" fieldname="rownum" value="1000" operation="<="/>

			</TD>
        </TR></table></form>
		</div>
	</body>
</html>