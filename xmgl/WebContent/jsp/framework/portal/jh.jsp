<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.lang.StringBuffer"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="com.ccthanking.framework.plugin.AppInit"%>
<%@ page import="java.math.*"%>
<html>
<head>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>计划概况</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name=ProgId content=Excel.Sheet>
</head>
<%
    Connection conn = DBUtil.getConnection();//定义连接
    StringBuffer sbSql = null;//sql语句字符串
    String sql = "";//查询参数字符串
    QueryTable table = null;//查询类
    String result = "";//用于记录统计数
    String sumResult = "";//用于记录总数
    String year = "";//年度
    //取当前年
    sbSql = new StringBuffer();
    sbSql.append("SELECT MAX(ND) FROM GC_TCJH_XMCBK WHERE SFYX = '1' AND ISXD = '1'");
    sql = sbSql.toString();
    String[][] ndArray = DBUtil.query(conn, sql);
    if(Pub.emptyArray(ndArray)){
   		year = ndArray[0][0].toString();
    }
    //String year = Pub.getDate("yyyy", new Date());
    //String year ="2013";
    String bfwc = "";
    String ybz = "";
    String qbwc = "";
    String wbz = "";
    String zjds = "";//总节点数(计划)
    int bfb = 0;//百分比
    String bfwc_bd = "";
    String ybz_bd = "";
    String qbwc_bd = "";
    String wbz_bd = "";
%> 

<script type="text/javascript" charset="utf-8">
var nd = '<%=year%>';
var bfwc,bfwcBfb;
//页面初始化
$(function() {
	var width = document.documentElement.clientWidth;
	var width = parseInt(width,10) - 7 - 242;
	$(".bzgkDiv").width(width);
	
	$("#printButton").click(function(){
		$(this).hide();
		$("div.QuickLinks").hide();
		window.print();
		$("div.QuickLinks").show();
		$(this).show();
	});
	
});
function queryTcjh(str){
	$(window).manhuaDialog({"title":"统筹计划>项目查询","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/portal/tcjh_list_query.jsp?str="+str+"&nd="+nd});
}

function popPage(target, url, title) {
	if(target == 'pagearea') {
		parent.menutree_click('',url,'','pagearea');
	} else if(target == 'fullscreen') {
		$(window).manhuaDialog({"title":title,"type":"text","content":"${pageContext.request.contextPath}/"+url});
	}
}

</script>
<body>
<div class="bzgkDiv">
<p></p>
	<h3>计划管理业务概况
	<span class="pull-right">
		<button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
	</span>
	</h3>
    <h4>统筹计划概况</h4>
    <p class="bzgk-gk">项目下达数<span>
     <a href="javascript:void(0);" onclick="queryTcjh(0)">
    	 		<%
    	         sbSql = new StringBuffer();
    	         sbSql.append("SELECT COUNT(DISTINCT XMID)AS XMZS FROM GC_JH_SJ WHERE ND ='"+year+"' AND SFYX = '1'");
    	         sql = sbSql.toString();
    	         String[][] xmzs = DBUtil.query(conn, sql);
    	         if(Pub.emptyArray(xmzs)){
    	        	 sumResult = xmzs[0][0].toString();
    	        	 out.println(sumResult);
    	         }
    	 		%> 
    	 	</a> 
     </span>，其中：应急<span>
	 	<%
	       sbSql = new StringBuffer();
	       sbSql.append("SELECT COUNT(DISTINCT XMID)AS YJXM FROM GC_JH_SJ WHERE XMSX = '2' AND ND = '"+year+"' AND SFYX = '1'");
	       sql = sbSql.toString();
	       String[][] yjxm = DBUtil.query(conn, sql);
	       if(Pub.emptyArray(yjxm)){
	      	 result = yjxm[0][0].toString();
	      	 if(result.equals("0")){
        		 out.println(result);
        	 }else{
        %>
        	<a href="javascript:void(0);" onclick="queryTcjh(4)">
        		<%
        			out.println(result);
        		%>
        	</a> 
        <%
        	 }
        	 
         }
 		%> 
      </span>
      	<%
    	if(Integer.parseInt(sumResult) == 0){
    		out.println("[0%]");
    	}else if(Integer.parseInt(result) == 0){
    		out.println("[0%]");
    	}else {
    		out.println("["+Pub.DecimalsFormat(String.valueOf(MathTool.div(Double.parseDouble(result)*100, Double.parseDouble(sumResult), 2)))+"%]" );
    	}
        %> ，稳定<span>
		<%
         sbSql = new StringBuffer();
         sbSql.append("SELECT COUNT(DISTINCT XMID)AS WD FROM GC_JH_SJ T,GC_TCJH_XMXDK T1 WHERE T1.WDD = '1' AND T.XMID = T1.GC_TCJH_XMXDK_ID AND T.ND = '"+year+"' AND T.SFYX = '1'");
         sql = sbSql.toString();
         String[][] wd = DBUtil.query(conn, sql);
         if(Pub.emptyArray(wd)){
        	 result = wd[0][0].toString();
        	 if(result.equals("0")){
        		 out.println(result);
        	 }else{
        %>
        	<a href="javascript:void(0);" onclick="queryTcjh(1)">
        		<%
        			out.println(result);
        		%>
        	</a> 
        <%
        	 }
        	 
         }
 		%> 
       </span>
        <%
       	if(Integer.parseInt(sumResult) == 0){
   			out.println("[0%]");
   		}else if(Integer.parseInt(result) == 0){
   			out.println("[0%]");
   		}else{
   			out.println("["+Pub.DecimalsFormat(String.valueOf(MathTool.div(Double.parseDouble(result)*100, Double.parseDouble(sumResult), 2)))+"%]" );
	    }
        %> ，基本稳定<span>       
 		<%
         sbSql = new StringBuffer();
         sbSql.append("SELECT COUNT(DISTINCT XMID)AS WD FROM GC_JH_SJ T,GC_TCJH_XMXDK T1 WHERE T1.WDD = '2' AND T.XMID = T1.GC_TCJH_XMXDK_ID AND T.ND = '"+year+"' AND T.SFYX = '1'");
         sql = sbSql.toString();
         String[][] jbwd = DBUtil.query(conn, sql);
         if(Pub.emptyArray(jbwd)){
        	 result = jbwd[0][0].toString();
        	 if(result.equals("0")){
        		 out.println(result);
        	 }else{
        %>
        	<a href="javascript:void(0);" onclick="queryTcjh(2)">
        		<%
        			out.println(result);
        		%>
        	</a> 
        <%
        	 }
        	 
         }
 		%> 
        </span>
        <%
       	if(Integer.parseInt(sumResult) == 0){
   			out.println("[0%]");
   		}else if(Integer.parseInt(result) == 0){
   			out.println("[0%]");
   		}else{
   			out.println("["+Pub.DecimalsFormat(String.valueOf(MathTool.div(Double.parseDouble(result)*100, Double.parseDouble(sumResult), 2)))+"%]" );
	    }
        %> ，不稳定<span>
 	 	<%
         sbSql = new StringBuffer();
         sbSql.append("SELECT COUNT(DISTINCT XMID)AS WD FROM GC_JH_SJ T,GC_TCJH_XMXDK T1 WHERE T1.WDD = '3' AND T.XMID = T1.GC_TCJH_XMXDK_ID AND T.ND = '"+year+"' AND T.SFYX = '1'");
         sql = sbSql.toString();
         String[][] bwd = DBUtil.query(conn, sql);
         if(Pub.emptyArray(bwd)){
        	 result = bwd[0][0].toString();
        	 if(result.equals("0")){
        		 out.println(result);
        	 }else{
        %>
        	<a href="javascript:void(0);" onclick="queryTcjh(3)">
        		<%
        			out.println(result);
        		%>
        	</a> 
        <%
        	 }
        	 
         }
 		%> 
		</span>
		<%
       	if(Integer.parseInt(sumResult) == 0){
   			out.println("[0%]");
   		}else if(Integer.parseInt(result) == 0){
   			out.println("[0%]");
   		}else{
   			out.println("["+Pub.DecimalsFormat(String.valueOf(MathTool.div(Double.parseDouble(result)*100, Double.parseDouble(sumResult), 2)))+"%]" );
	    }
        %>
</p>
<div class="bzgk-table">
	<table width="98%">
		<thead>
			<tr class="bzgk-table-tr-Border">
     			<th rowspan="2" align="center" valign="middle">批次</th>
       			<th rowspan="2" align="center" valign="middle">最新版本</th>
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
          		<th align="center" valign="middle">总投资（万元）</th>
			</tr>
        	<tr>
				<th valign="middle">总数</th>
	           	<th valign="middle">应急</th>
	           	<th valign="middle">BT</th>
	           	<th valign="middle">调整</th>
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
           		<th valign="middle"></th>
			</tr>
		</thead>
	<tbody>
        <%
        sbSql = new StringBuffer();
        sbSql.append("SELECT * FROM ");
        sbSql.append("(SELECT DISTINCT T.JHPCH,");
        sbSql.append("(decode(MQBB,0,'-',MQBB))as MQBB,");
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
        sbSql.append("(decode(MQBB,0,'-',MQBB))as MQBB,");
        sbSql.append("TO_CHAR(T.XDRQ, 'yyyy-MM-dd') as XDRQ,");
        sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE JHID = T.GC_JH_ZT_ID AND ND ='"+year+"' AND XMBS = '0' AND SFYX = '1') AS ZS,");
        sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE JHID = T.GC_JH_ZT_ID AND XMSX = '2' AND ND = '"+year+"' AND XMBS = '0' AND SFYX = '1') AS YJ,");
        sbSql.append("(SELECT COUNT(GC_TCJH_XMXDK_ID) as bt FROM GC_TCJH_XMXDK WHERE ISBT = '1' AND GC_TCJH_XMXDK_ID in(select DISTINCT XMID from gc_jh_sj where nd =  '"+year+"' AND JHID =T.GC_JH_ZT_ID)) AS BT,");
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
        %>   
        <%
        	int hj[] = new int[9+xmglgsCoount];//定义整体数组，用于合计计算
        	String ztz = "0";//合计金额，特殊处理
        	int cellCount = 9+xmglgsCoount;//总列数
        	if(Pub.emptyArray(jhpcResult)){
        		for(int i=0;i<jhpcResult.length;i++){
        			for(int j = 3;j < cellCount-1;j++){
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
              %>
              	<td align="center"><%=jhpcResult[i][z]%></td>
              <% } %>
                <td align="center" style="text-align:right;padding-right:35px;">
                	<%=Pub.NumberAddDec(Pub.NumberToThousand(jhpcResult[i][cellCount-1]))%>
                </td>
              </tr>
          </tbody>
  		<% }}%>
          <tfoot>
              <tr class="bzgk-table-tr-Border">
                <td align="center">合计</td>
                <td align="center">-</td>
                <td align="center">-</td>
                <%
                	for(int i =3;i<cellCount-1;i++){
                %>
                <td align="center"><%=hj[i]%></td>
                <% } %>
                <td align="center" style="text-align:right;padding-right:35px;"><%=Pub.NumberAddDec(Pub.NumberToThousand(ztz))%></td>
              </tr>
            </tfoot>
          </table>
    </div>
    <h4>统筹计划跟踪情况</h4>
    <div class="bzgk-table">
   	  <table width="98%">
      <tr>
        <th width="28" align="center" valign="middle">项目<br>
        概况</th>
        <td><div class="test">
        计划编制项目数：应编制 <span class="textUnderline">
        <%
			sbSql = new StringBuffer();
       		sbSql.append("SELECT COUNT(GC_JH_SJ_ID)AS YBZ FROM GC_JH_SJ WHERE ND ='"+year+"' AND XMBS = '0' AND SFYX = '1'");
       		sql = sbSql.toString();
       		String[][] ybzArray = DBUtil.query(conn, sql);
       		if(Pub.emptyArray(ybzArray)){
       			ybz = ybzArray[0][0].toString();
        		out.println(ybz);
         	}
		%>
        </span>，其中：全部完成 <span class="textUnderline">
        <%
    		sbSql = new StringBuffer();
   	        sbSql.append("SELECT COUNT(GC_JH_SJ_ID)AS QBWC ");
   	        sbSql.append("FROM GC_JH_SJ T ");
   	        sbSql.append("WHERE ");
   	        sbSql.append("((T.ISKGSJ='1' AND T.KGSJ IS NOT NULL) OR (T.ISKGSJ = '0')) ");
   	     	sbSql.append("AND ((T.ISWGSJ = '1' AND T.WGSJ IS NOT NULL) OR (T.ISWGSJ = '0')) ");
   	      	sbSql.append("AND ((T.ISKYPF = '1' AND T.KYPF IS NOT NULL) OR (T.ISKYPF = '0')) ");
   	   		sbSql.append("AND ((T.ISHPJDS = '1' AND T.HPJDS IS NOT NULL) OR (T.ISHPJDS ='0')) ");
	       	sbSql.append("AND ((T.ISGCXKZ = '1' AND T.GCXKZ IS NOT NULL) OR (T.ISGCXKZ = '0')) ");
	       	sbSql.append("AND ((T.ISSGXK = '1' AND T.SGXK IS NOT NULL) OR (T.ISSGXK ='0')) ");
	       	sbSql.append("AND ((T.ISCBSJPF = '1' AND T.CBSJPF IS NOT NULL) OR (T.ISCBSJPF = '0')) ");
	       	sbSql.append("AND ((T.ISCQT = '1' AND T.CQT IS NOT NULL) OR (T.ISCQT = '0')) ");
	       	sbSql.append("AND ((T.ISPQT = '1' AND T.PQT IS NOT NULL) OR (T.ISPQT = '0')) ");
	       	sbSql.append("AND ((T.ISSGT = '1' AND T.SGT IS NOT NULL) OR (T.ISSGT = '0')) ");
	       	sbSql.append("AND ((T.ISTBJ = '1' AND T.TBJ IS NOT NULL) OR (T.ISTBJ = '0')) ");
	       	sbSql.append("AND ((T.ISCS = '1' AND T.CS IS NOT NULL) OR (T.ISCS = '0')) ");
	       	sbSql.append("AND ((T.ISJLDW = '1' AND T.JLDW IS NOT NULL) OR (T.ISJLDW  = '0')) ");
	       	sbSql.append("AND ((T.ISSGDW = '1' AND T.SGDW IS NOT NULL) OR (T.ISSGDW = '0')) ");
	       	sbSql.append("AND ((T.ISZC = '1' AND T.ZC IS NOT NULL) OR (T.ISZC = '0')) ");
	       	sbSql.append("AND ((T.ISPQ = '1' AND T.PQ IS NOT NULL) OR (T.ISPQ = '0')) ");
	       	sbSql.append("AND ((T.ISJG = '1' AND T.JG IS NOT NULL) OR (T.ISJG = '0')) ");
	       	sbSql.append("AND T.ND = '"+year+"' AND T.SFYX = '1' AND XMBS='0'");
       		sql = sbSql.toString();
       		int qbwc_bfb = 0;
       		String[][] qbwcArray = DBUtil.query(conn, sql);
       		if(Pub.emptyArray(qbwcArray)){
	       	 	qbwc = qbwcArray[0][0].toString();
	       	 	out.println(qbwc);
    	    }
    	%>
        </span>
        <%
        if(Integer.parseInt(ybz) == 0){
   			out.print("[0%]");
   		}else if(Integer.parseInt(qbwc) == 0){
   			out.print("[0%]");
   		}else{
   			qbwc_bfb = Pub.myPercent(Integer.parseInt(qbwc), Integer.parseInt(ybz));
   			out.print("["+qbwc_bfb+"%]");
	    }
        %>，部分完成
		<span class="textUnderline" id="bfwc">
        </span><font id="bfwcbfb"></font>，未编制 <span class="textUnderline">
        <%
			sbSql = new StringBuffer();
			sbSql.append("SELECT COUNT(GC_JH_SJ_ID)AS WBZ ");
			sbSql.append("FROM GC_JH_SJ T ");
			sbSql.append("WHERE ");
			sbSql.append("T.KGSJ IS NULL  ");
			sbSql.append("AND T.WGSJ IS NULL ");
			sbSql.append("AND T.KYPF IS NULL ");
			sbSql.append("AND T.HPJDS IS NULL ");
			sbSql.append("AND T.GCXKZ IS NULL ");
			sbSql.append("AND T.SGXK IS NULL ");
			sbSql.append("AND T.CBSJPF IS NULL ");
			sbSql.append("AND T.CQT IS NULL ");
			sbSql.append("AND T.PQT IS NULL ");
			sbSql.append("AND T.SGT IS NULL ");
			sbSql.append("AND T.TBJ IS NULL ");
			sbSql.append("AND T.CS IS NULL ");
			sbSql.append("AND T.JLDW IS NULL ");
			sbSql.append("AND T.SGDW IS NULL ");
			sbSql.append("AND T.ZC IS NULL ");
			sbSql.append("AND T.PQ IS NULL ");
			sbSql.append("AND T.JG IS NULL ");
			sbSql.append("AND T.ND = '"+year+"' AND T.SFYX = '1' AND XMBS='0'");
     		sql = sbSql.toString();
     		int wbz_bfb = 0;
     		String[][] wbzArray = DBUtil.query(conn, sql);
     		if(Pub.emptyArray(wbzArray)){
		      	 wbz = wbzArray[0][0].toString();
		      	 out.println(wbz);
		       }
		%>
        </span>
        <%
        if(Integer.parseInt(ybz) == 0){
   			out.println("[0%]");
   		}else if(Integer.parseInt(wbz) == 0){
   			out.println("[0%]");
   		}else{
   			wbz_bfb = Pub.myPercent(Integer.parseInt(wbz), Integer.parseInt(ybz));
   			out.println("["+wbz_bfb+"%]" );
	    }
        %>
<br>
<script type="text/javascript" charset="utf-8">
	if(<%=Integer.parseInt(ybz)%> == 0){
			$("#bfwc")[0].innerHTML = "0[0%]";
		}else{
			bfwc = '<%=Integer.parseInt(ybz) - Integer.parseInt(wbz) - Integer.parseInt(qbwc)%>';
			if(bfwc == 0){
				$("#bfwc")[0].innerHTML = "0[0%]";
			}else{
				var bfwc_bfb = <%=100-wbz_bfb-qbwc_bfb %>;
				$("#bfwc")[0].innerHTML =bfwc;
				$("#bfwcbfb")[0].innerHTML = "["+bfwc_bfb+"%]";
			}
		}
</script>
        执行情况：  已完成 <span class="textUnderline">
        <%
       		sbSql = new StringBuffer();
      	    sbSql.append("SELECT COUNT(GC_JH_SJ_ID)AS QBWC ");
      	    sbSql.append("FROM GC_JH_SJ T ");
      	    sbSql.append("WHERE ");
      	    sbSql.append("((T.ISKGSJ = '1' AND T.KGSJ_SJ IS NOT NULL) OR (T.ISKGSJ = '0')) ");
      	  	sbSql.append("AND ((T.ISWGSJ = '1' AND T.WGSJ_SJ IS NOT NULL) OR (T.ISWGSJ = '0'))  ");
      	    sbSql.append("AND ((T.ISKYPF = '1' AND T.KYPF_SJ IS NOT NULL) OR (T.ISKYPF = '0')) ");
      	   	sbSql.append("AND ((T.ISHPJDS = '1' AND T.HPJDS_SJ IS NOT NULL) OR (T.ISHPJDS ='0')) ");
	       	sbSql.append("AND ((T.ISGCXKZ = '1' AND T.GCXKZ_SJ IS NOT NULL) OR (T.ISGCXKZ = '0')) ");
	       	sbSql.append("AND ((T.ISSGXK = '1' AND T.SGXK_SJ IS NOT NULL) OR (T.ISSGXK ='0')) ");
	       	sbSql.append("AND ((T.ISCBSJPF = '1' AND T.CBSJPF_SJ IS NOT NULL) OR (T.ISCBSJPF = '0')) ");
	       	sbSql.append("AND ((T.ISCQT = '1' AND T.CQT_SJ IS NOT NULL) OR (T.ISCQT = '0')) ");
	       	sbSql.append("AND ((T.ISPQT = '1' AND T.PQT_SJ IS NOT NULL) OR (T.ISPQT = '0')) ");
	       	sbSql.append("AND ((T.ISSGT = '1' AND T.SGT_SJ IS NOT NULL) OR (T.ISSGT = '0')) ");
	       	sbSql.append("AND ((T.ISTBJ = '1' AND T.TBJ_SJ IS NOT NULL) OR (T.ISTBJ = '0')) ");
	       	sbSql.append("AND ((T.ISCS = '1' AND T.CS_SJ IS NOT NULL) OR (T.ISCS = '0')) ");
	       	sbSql.append("AND ((T.ISJLDW = '1' AND T.JLDW_SJ IS NOT NULL) OR (T.ISJLDW  = '0')) ");
	       	sbSql.append("AND ((T.ISSGDW = '1' AND T.SGDW_SJ IS NOT NULL) OR (T.ISSGDW = '0')) ");
	       	sbSql.append("AND ((T.ISZC = '1' AND T.ZC_SJ IS NOT NULL) OR (T.ISZC = '0')) ");
	       	sbSql.append("AND ((T.ISPQ = '1' AND T.PQ_SJ IS NOT NULL) OR (T.ISPQ = '0')) ");
	       	sbSql.append("AND ((T.ISJG = '1' AND T.JG_SJ IS NOT NULL) OR (T.ISJG = '0')) ");
	       	sbSql.append("AND T.ND = '"+year+"' AND T.SFYX = '1' AND T.XMBS = '0'");
       		sql = sbSql.toString();
        		String[][] ywcArray = DBUtil.query(conn, sql);
        		if(Pub.emptyArray(ywcArray)){
	        	 result = ywcArray[0][0].toString();
	        	 out.println(result);
	         }
		%>
        </span>
         <%
         	String bdzs = "";
         	sbSql = new StringBuffer();
	   	    sbSql.append("SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ T ");
	   	    sbSql.append("WHERE ");
	   	 	sbSql.append("T.ND = '"+year+"' AND T.SFYX = '1' AND T.XMBS='0'");
    		sql = sbSql.toString();
    		String[][] bdzsArray = DBUtil.query(conn, sql);
    		if(Pub.emptyArray(bdzsArray)){
    			bdzs = String.valueOf(Integer.parseInt(bdzsArray[0][0].toString()));
    		}
	         if(Integer.parseInt(bdzs) == 0){
	    			out.println("[0%]");
	    		}else if(Integer.parseInt(result) == 0){
	    			out.println("[0%]");
	    		}else{
	    			out.println("["+Pub.myPercent(Integer.parseInt(result), Integer.parseInt(bdzs))+"%]" );
	 	    	}
        %>，正常执行 <span class="textUnderline">
        <%
			sbSql = new StringBuffer();
			sbSql.append("SELECT COUNT(GC_JH_SJ_ID) AS ZCZX ");
			sbSql.append("FROM GC_JH_SJ T ");
			sbSql.append("WHERE ");
			sbSql.append("((T.ISKGSJ ='1' AND  TRUNC(SYSDATE) <= T.KGSJ) OR (T.ISKGSJ = '0')) ");
			sbSql.append("AND ((T.ISWGSJ ='1' AND  TRUNC(SYSDATE) <= T.WGSJ) OR (T.ISWGSJ = '0')) ");
			sbSql.append("AND ((T.ISKYPF = '1' AND trunc(SYSDATE) <= T.KYPF) OR (T.ISKYPF = '0')) ");
			sbSql.append("AND ((T.ISHPJDS = '1' AND trunc(SYSDATE) <= T.HPJDS) OR (T.ISHPJDS = '0')) ");
			sbSql.append("AND ((T.ISGCXKZ = '1' AND trunc(SYSDATE) <= T.GCXKZ) OR (T.ISGCXKZ = '0')) ");
			sbSql.append("AND ((T.ISSGXK = '1' AND trunc(SYSDATE) <= T.SGXK) OR (T.ISSGXK = '0')) ");
			sbSql.append("AND ((T.ISCBSJPF = '1' AND trunc(SYSDATE) <= T.CBSJPF) OR (T.ISCBSJPF = '0')) ");
			sbSql.append("AND ((T.ISCQT = '1' AND trunc(SYSDATE) <= T.CQT) OR (T.ISCQT = '0')) ");
			sbSql.append("AND ((T.ISPQT = '1' AND trunc(SYSDATE) <= T.PQT) OR (T.ISPQT = '0')) ");
			sbSql.append("AND ((T.ISSGT = '1' AND trunc(SYSDATE) <= T.SGT) OR (T.ISSGT = '0')) ");
			sbSql.append("AND ((T.ISTBJ = '1' AND trunc(SYSDATE) <= T.TBJ) OR (T.ISTBJ = '0')) ");
			sbSql.append("AND ((T.ISCS = '1' AND trunc(SYSDATE) <= T.CS) OR (T.ISCS = '0')) ");
			sbSql.append("AND ((T.ISJLDW = '1' AND trunc(SYSDATE) <= T.JLDW) OR (T.ISJLDW = '0')) ");
			sbSql.append("AND ((T.ISSGDW = '1' AND trunc(SYSDATE) <= T.SGDW) OR (T.ISSGDW = '0')) ");
			sbSql.append("AND ((T.ISZC = '1' AND trunc(SYSDATE) <= T.ZC) OR (T.ISZC = '0')) ");
			sbSql.append("AND ((T.ISPQ = '1' AND trunc(SYSDATE) <= T.PQ) OR (T.ISPQ = '0')) ");
			sbSql.append("AND ((T.ISJG = '1' AND trunc(SYSDATE) <= T.JG) OR (T.ISJG = '0')) ");
			sbSql.append("AND T.ND = '"+year+"' AND T.SFYX = '1' AND T.XMBS = '0'");
			sql = sbSql.toString();
        	String[][] zczxArray = DBUtil.query(conn, sql);
        	if(Pub.emptyArray(zczxArray)){
	        	 result = zczxArray[0][0].toString();
	        	 out.println(result);
	         }
		%>
        </span>
        <%
        if(Integer.parseInt(bdzs) == 0){
			out.println("[0%]");
		}else if(Integer.parseInt(result) == 0){
			out.println("[0%]");
		}else{
			out.println("["+Pub.myPercent(Integer.parseInt(result), Integer.parseInt(bdzs))+"%]" );
	    	}
        %>，延期 <span class="textUnderline">
        <%
			sbSql = new StringBuffer();
			sbSql.append("SELECT COUNT(GC_JH_SJ_ID) AS YQ ");
			sbSql.append("FROM GC_JH_SJ T ");
			sbSql.append("WHERE ");
			sbSql.append("((T.ISKGSJ='1' AND T.KGSJ_SJ IS NULL AND (trunc(SYSDATE)-T.KGSJ>5 AND trunc(SYSDATE)-T.KGSJ<=10)) ");
			sbSql.append("OR (T.ISWGSJ = '1' AND T.WGSJ_SJ IS NULL AND (trunc(SYSDATE)-T.KGSJ>5 AND trunc(SYSDATE)-T.KGSJ<=10))  ");
			sbSql.append("OR (T.ISZC = '1' AND T.ZC_SJ IS NULL AND (trunc(SYSDATE)-T.ZC>5 AND trunc(SYSDATE)-T.ZC<=10)) ");
			sbSql.append("OR (T.ISPQ = '1' AND T.PQ_SJ IS NULL AND (trunc(SYSDATE)-T.PQ>5 AND trunc(SYSDATE)-T.PQ<=10)) ");
			sbSql.append("OR (T.ISKYPF = '1' AND T.KYPF_SJ IS NULL AND (trunc(SYSDATE)-T.KYPF>5 AND trunc(SYSDATE)-T.KYPF<=10)) ");
			sbSql.append("OR (T.ISHPJDS = '1' AND T.HPJDS_SJ IS NULL AND (trunc(SYSDATE)-T.HPJDS>5 AND trunc(SYSDATE)-T.HPJDS<=10)) ");
			sbSql.append("OR (T.ISGCXKZ = '1' AND T.GCXKZ_SJ IS NULL AND (trunc(SYSDATE)-T.GCXKZ>5 AND trunc(SYSDATE)-T.GCXKZ<=10)) ");
			sbSql.append("OR (T.ISSGXK = '1' AND T.SGXK_SJ IS NULL AND (trunc(SYSDATE)-T.SGXK>5 AND trunc(SYSDATE)-T.SGXK<=10)) ");
			sbSql.append("OR (T.ISCBSJPF = '1' AND T.CBSJPF_SJ IS NULL AND (trunc(SYSDATE)-T.CBSJPF>5 AND trunc(SYSDATE)-T.CBSJPF<=10)) ");
			sbSql.append("OR (T.ISCQT = '1' AND T.CQT_SJ IS NULL AND (trunc(SYSDATE)-T.CQT>5 AND trunc(SYSDATE)-T.CQT<=10)) ");
			sbSql.append("OR (T.ISPQT = '1' AND T.PQT_SJ IS NULL AND (trunc(SYSDATE)-T.PQT>5 AND trunc(SYSDATE)-T.PQT<=10)) ");
			sbSql.append("OR (T.ISSGT = '1' AND T.SGT_SJ IS NULL AND (trunc(SYSDATE)-T.SGT>5 AND trunc(SYSDATE)-T.SGT<=10)) ");
			sbSql.append("OR (T.ISTBJ = '1' AND T.TBJ_SJ IS NULL AND (trunc(SYSDATE)-T.TBJ>5 AND trunc(SYSDATE)-T.TBJ<=10)) ");
			sbSql.append("OR (T.ISCS = '1' AND T.CS_SJ IS NULL AND (trunc(SYSDATE)-T.CS>5 AND trunc(SYSDATE)-T.CS<=10)) ");
			sbSql.append("OR (T.ISJLDW = '1' AND T.JLDW_SJ IS NULL AND (trunc(SYSDATE)-T.JLDW>5 AND trunc(SYSDATE)-T.JLDW<=10)) ");
			sbSql.append("OR (T.ISSGDW = '1' AND T.SGDW_SJ IS NULL AND (trunc(SYSDATE)-T.SGDW>5 AND trunc(SYSDATE)-T.SGDW<=10)) ");
			sbSql.append("OR (T.ISJG = '1' AND T.JG_SJ IS NULL AND (trunc(SYSDATE)-T.JG>5 AND trunc(SYSDATE)-T.JG<=10))) ");
			sbSql.append("AND T.ND = '"+year+"' AND T.SFYX = '1' AND T.XMBS = '0'");
     		sql = sbSql.toString();
     		String[][] yqArray = DBUtil.query(conn, sql);
     		if(Pub.emptyArray(yqArray)){
		      	 result = yqArray[0][0].toString();
		      	 out.println(result);
		       }
		%>
        </span>
        <%
        if(Integer.parseInt(bdzs) == 0){
			out.println("[0%]");
		}else if(Integer.parseInt(result) == 0){
			out.println("[0%]");
		}else{
			out.println("["+Pub.myPercent(Integer.parseInt(result), Integer.parseInt(bdzs))+"%]" );
	    	}
        %>，严重延期 <span class="textUnderline">
        <%
	    	sbSql = new StringBuffer();
	   	    sbSql.append("SELECT COUNT(GC_JH_SJ_ID) AS YZCQ ");
	   	    sbSql.append("FROM GC_JH_SJ T ");
	   	    sbSql.append("WHERE ");
	   	 	sbSql.append("((T.ISKGSJ ='1' AND T.KGSJ_SJ IS NULL AND trunc(SYSDATE)-T.KGSJ>10) ");
			sbSql.append("OR (T.ISWGSJ = '1' AND T.WGSJ_SJ IS NULL AND trunc(SYSDATE)-T.KGSJ>10)  ");
			sbSql.append("OR (T.ISZC = '1' AND T.ZC_SJ IS NULL AND trunc(SYSDATE)-T.ZC>10) ");
			sbSql.append("OR (T.ISPQ = '1' AND T.PQ_SJ IS NULL AND trunc(SYSDATE)-T.PQ>10) ");
			sbSql.append("OR (T.ISKYPF = '1' AND T.KYPF_SJ IS NULL AND trunc(SYSDATE)-T.KYPF>10) ");
			sbSql.append("OR (T.ISHPJDS = '1' AND T.HPJDS_SJ IS NULL AND trunc(SYSDATE)-T.HPJDS>10) ");
			sbSql.append("OR (T.ISGCXKZ = '1' AND T.GCXKZ_SJ IS NULL AND trunc(SYSDATE)-T.GCXKZ>10) ");
			sbSql.append("OR (T.ISSGXK = '1' AND T.SGXK_SJ IS NULL AND trunc(SYSDATE)-T.SGXK>10) ");
			sbSql.append("OR (T.ISCBSJPF = '1' AND T.CBSJPF_SJ IS NULL AND trunc(SYSDATE)-T.CBSJPF>10) ");
			sbSql.append("OR (T.ISCQT = '1' AND T.CQT_SJ IS NULL AND trunc(SYSDATE)-T.CQT>10) ");
			sbSql.append("OR (T.ISPQT = '1' AND T.PQT_SJ IS NULL AND trunc(SYSDATE)-T.PQT>10) ");
			sbSql.append("OR (T.ISSGT = '1' AND T.SGT_SJ IS NULL AND trunc(SYSDATE)-T.SGT>10) ");
			sbSql.append("OR (T.ISTBJ = '1' AND T.TBJ_SJ IS NULL AND trunc(SYSDATE)-T.TBJ>10) ");
			sbSql.append("OR (T.ISCS = '1' AND T.CS_SJ IS NULL AND trunc(SYSDATE)-T.CS>10) ");
			sbSql.append("OR (T.ISJLDW = '1' AND T.JLDW_SJ IS NULL AND trunc(SYSDATE)-T.JLDW>10) ");
			sbSql.append("OR (T.ISSGDW = '1' AND T.SGDW_SJ IS NULL AND trunc(SYSDATE)-T.SGDW>10) ");
			sbSql.append("OR (T.ISJG = '1' AND T.JG_SJ IS NULL AND trunc(SYSDATE)-T.JG>10)) ");
			sbSql.append("AND T.ND = '"+year+"' AND T.SFYX = '1' AND T.XMBS = '0'");
       		sql = sbSql.toString();
 	        String[][] yzcqArray = DBUtil.query(conn, sql);
 	        if(Pub.emptyArray(yzcqArray)){
   	        	 result = yzcqArray[0][0].toString();
   	        	 out.println(result);
   	         }
  			%>
        </span>
        <%
        if(Integer.parseInt(bdzs) == 0){
			out.println("[0%]");
		}else if(Integer.parseInt(result) == 0){
			out.println("[0%]");
		}else{
			out.println("["+Pub.myPercent(Integer.parseInt(result), Integer.parseInt(bdzs))+"%]" );
	    	}
        %>
<br>
        存在问题：  重大问题 <span class="textUnderline">
        <%
        	String wtzs = "";//取问题总数
        	sbSql = new StringBuffer();
        	sbSql.append("select count(WTTB_INFO_ID)as wtzs from WTTB_INFO where JHSJID is not null and sfyx = '1'");
        	sql = sbSql.toString();
 	        String[][] wtzsArray = DBUtil.query(conn, sql);
 	        if(Pub.emptyArray(wtzsArray)){
 	        	wtzs = wtzsArray[0][0].toString();
 	        }
        %>
        <%
	    	sbSql = new StringBuffer();
	   	    sbSql.append("select count(WTTB_INFO_ID)as zdwt from WTTB_INFO where WTXZ = '13' and JHSJID is not null and sfyx = '1'");
       		sql = sbSql.toString();
 	        String[][] zdwtArray = DBUtil.query(conn, sql);
 	        if(Pub.emptyArray(zdwtArray)){
   	        	 result = zdwtArray[0][0].toString();
   	        	 out.println(result);
   	         }
  			%>
        </span>
        <%
        if(Integer.parseInt(wtzs) == 0){
			out.println("[0%]");
		}else if(Integer.parseInt(result) == 0){
			out.println("[0%]");
		}else{
			out.println("["+Pub.myPercent(Integer.parseInt(result), Integer.parseInt(wtzs))+"%]" );
	    	}
        %>，严重问题 <span class="textUnderline">
        <%
	    	sbSql = new StringBuffer();
	   	    sbSql.append("select count(WTTB_INFO_ID)as yzwt from WTTB_INFO where WTXZ = '12' and JHSJID is not null and sfyx = '1'");
       		sql = sbSql.toString();
 	        String[][] yzwtArray = DBUtil.query(conn, sql);
 	        if(Pub.emptyArray(yzwtArray)){
   	        	 result = yzwtArray[0][0].toString();
   	        	 out.println(result);
   	         }
  			%>
        </span>
        <%
        if(Integer.parseInt(wtzs) == 0){
			out.println("[0%]");
		}else if(Integer.parseInt(result) == 0){
			out.println("[0%]");
		}else{
			out.println("["+Pub.myPercent(Integer.parseInt(result), Integer.parseInt(wtzs))+"%]" );
	    	}
        %>，一般问题 <span class="textUnderline">
        <%
	    	sbSql = new StringBuffer();
	   	    sbSql.append("select count(WTTB_INFO_ID)as ybwt from WTTB_INFO where WTXZ = '11' and JHSJID is not null and sfyx = '1'");
       		sql = sbSql.toString();
 	        String[][] ybwtArray = DBUtil.query(conn, sql);
 	        if(Pub.emptyArray(ybwtArray)){
   	        	 result = ybwtArray[0][0].toString();
   	        	 out.println(result);
   	         }
  			%>
        </span>
        <%
            if(Integer.parseInt(wtzs) == 0){
      			out.println("[0%]");
      		}else if(Integer.parseInt(result) == 0){
      			out.println("[0%]");
      		}else{
      			out.println("["+Pub.myPercent(Integer.parseInt(result), Integer.parseInt(wtzs))+"%]" );
      	    	}
        %></div></td>
      </tr>
      <!-- ================================================================================= -->
      <tr>
        <th width="28" align="center" valign="middle">标段<br>
        概况</th>
        <td><div class="test">
        计划编制标段数：应编制 <span class="textUnderline">
        <%
			sbSql = new StringBuffer();
       		sbSql.append("SELECT COUNT(GC_JH_SJ_ID)AS YBZ FROM GC_JH_SJ WHERE ND ='"+year+"' AND (XMBS = '1' or ISNOBDXM = '1') AND SFYX = '1'");
       		sql = sbSql.toString();
       		String[][] ybzArray_bd = DBUtil.query(conn, sql);
       		if(Pub.emptyArray(ybzArray_bd)){
       			ybz_bd = ybzArray_bd[0][0].toString();
        		out.println(ybz_bd);
         	}
		%>
        </span>，其中：全部完成 <span class="textUnderline">
        <%
    		sbSql = new StringBuffer();
   	        sbSql.append("SELECT COUNT(GC_JH_SJ_ID)AS QBWC ");
   	        sbSql.append("FROM GC_JH_SJ T ");
   	        sbSql.append("WHERE ");
   	        sbSql.append("((T.ISKGSJ = '1' AND T.KGSJ IS NOT NULL) OR (T.ISKGSJ = '0'))");
   	     	sbSql.append("AND ((T.ISWGSJ = '1' AND T.WGSJ IS NOT NULL) OR (T.ISWGSJ = '0'))  ");
   	      	sbSql.append("AND ((T.ISKYPF = '1' AND T.KYPF IS NOT NULL) OR (T.ISKYPF = '0')) ");
   	   		sbSql.append("AND ((T.ISHPJDS = '1' AND T.HPJDS IS NOT NULL) OR (T.ISHPJDS ='0')) ");
	       	sbSql.append("AND ((T.ISGCXKZ = '1' AND T.GCXKZ IS NOT NULL) OR (T.ISGCXKZ = '0')) ");
	       	sbSql.append("AND ((T.ISSGXK = '1' AND T.SGXK IS NOT NULL) OR (T.ISSGXK ='0')) ");
	       	sbSql.append("AND ((T.ISCBSJPF = '1' AND T.CBSJPF IS NOT NULL) OR (T.ISCBSJPF = '0')) ");
	       	sbSql.append("AND ((T.ISCQT = '1' AND T.CQT IS NOT NULL) OR (T.ISCQT = '0')) ");
	       	sbSql.append("AND ((T.ISPQT = '1' AND T.PQT IS NOT NULL) OR (T.ISPQT = '0')) ");
	       	sbSql.append("AND ((T.ISSGT = '1' AND T.SGT IS NOT NULL) OR (T.ISSGT = '0')) ");
	       	sbSql.append("AND ((T.ISTBJ = '1' AND T.TBJ IS NOT NULL) OR (T.ISTBJ = '0')) ");
	       	sbSql.append("AND ((T.ISCS = '1' AND T.CS IS NOT NULL) OR (T.ISCS = '0')) ");
	       	sbSql.append("AND ((T.ISJLDW = '1' AND T.JLDW IS NOT NULL) OR (T.ISJLDW  = '0')) ");
	       	sbSql.append("AND ((T.ISSGDW = '1' AND T.SGDW IS NOT NULL) OR (T.ISSGDW = '0')) ");
	       	sbSql.append("AND ((T.ISZC = '1' AND T.ZC IS NOT NULL) OR (T.ISZC = '0')) ");
	       	sbSql.append("AND ((T.ISPQ = '1' AND T.PQ IS NOT NULL) OR (T.ISPQ = '0')) ");
	       	sbSql.append("AND ((T.ISJG = '1' AND T.JG IS NOT NULL) OR (T.ISJG = '0')) ");
	       	sbSql.append("AND T.ND = '"+year+"' AND T.SFYX = '1' AND (XMBS = '1' or ISNOBDXM = '1')");
       		sql = sbSql.toString();
       		int qbwc_bfb_bd = 0;
       		String[][] qbwcArray_bd = DBUtil.query(conn, sql);
       		if(Pub.emptyArray(qbwcArray_bd)){
	       	 	qbwc_bd = qbwcArray_bd[0][0].toString();
	       	 	out.println(qbwc_bd);
    	    }
    	%>
        </span>
        <%
        if(Integer.parseInt(ybz_bd) == 0){
   			out.print("[0%]");
   		}else if(Integer.parseInt(qbwc_bd) == 0){
   			out.print("[0%]");
   		}else{
   			qbwc_bfb_bd = Pub.myPercent(Integer.parseInt(qbwc_bd), Integer.parseInt(ybz_bd));
   			out.print("["+qbwc_bfb_bd+"%]");
	    }
        %>，部分完成
		<span class="textUnderline" id="bfwc_bd">
        </span><font id="bfwcbfb_bd"></font>，未编制 <span class="textUnderline">
        <%
			sbSql = new StringBuffer();
			sbSql.append("SELECT COUNT(GC_JH_SJ_ID)AS WBZ ");
			sbSql.append("FROM GC_JH_SJ T ");
			sbSql.append("WHERE ");
			sbSql.append("T.KGSJ IS NULL ");
			sbSql.append("AND T.WGSJ IS NULL ");
			sbSql.append("AND T.KYPF IS NULL ");
			sbSql.append("AND T.HPJDS IS NULL ");
			sbSql.append("AND T.GCXKZ IS NULL ");
			sbSql.append("AND T.SGXK IS NULL ");
			sbSql.append("AND T.CBSJPF IS NULL ");
			sbSql.append("AND T.CQT IS NULL ");
			sbSql.append("AND T.PQT IS NULL ");
			sbSql.append("AND T.SGT IS NULL ");
			sbSql.append("AND T.TBJ IS NULL ");
			sbSql.append("AND T.CS IS NULL ");
			sbSql.append("AND T.JLDW IS NULL ");
			sbSql.append("AND T.SGDW IS NULL ");
			sbSql.append("AND T.ZC IS NULL ");
			sbSql.append("AND T.PQ IS NULL ");
			sbSql.append("AND T.JG IS NULL ");
			sbSql.append("AND T.ND = '"+year+"' AND T.SFYX = '1' AND (XMBS = '1' or ISNOBDXM = '1')");
     		sql = sbSql.toString();
     		int wbz_bfb_bd = 0;
     		String[][] wbzArray_bd = DBUtil.query(conn, sql);
     		if(Pub.emptyArray(wbzArray_bd)){
		      	 wbz_bd = wbzArray_bd[0][0].toString();
		      	 out.println(wbz_bd);
		       }
		%>
        </span>
        <%
        if(Integer.parseInt(ybz_bd) == 0){
   			out.println("[0%]");
   		}else if(Integer.parseInt(wbz_bd) == 0){
   			out.println("[0%]");
   		}else{
   			wbz_bfb_bd = Pub.myPercent(Integer.parseInt(wbz_bd), Integer.parseInt(ybz_bd));
   			out.println("["+wbz_bfb_bd+"%]" );
	    }
        %>
<br>
<script type="text/javascript" charset="utf-8">
	if(<%=Integer.parseInt(ybz_bd)%> == 0){
			$("#bfwc_bd")[0].innerHTML = "0[0%]";
		}else{
			var bfwc_bd = '<%=Integer.parseInt(ybz_bd) - Integer.parseInt(wbz_bd) - Integer.parseInt(qbwc_bd)%>';
			if(bfwc_bd == 0){
				$("#bfwc_bd")[0].innerHTML = "0[0%]";
			}else{
				var bfwc_bfb_bd = <%=100-wbz_bfb_bd-qbwc_bfb_bd %>;
				$("#bfwc_bd")[0].innerHTML =bfwc_bd;
				$("#bfwcbfb_bd")[0].innerHTML = "["+bfwc_bfb_bd+"%]";
			}
		}
</script>
        执行情况：  已完成 <span class="textUnderline">
        <%
       		sbSql = new StringBuffer();
      	    sbSql.append("SELECT COUNT(GC_JH_SJ_ID)AS QBWC ");
      	    sbSql.append("FROM GC_JH_SJ T ");
      	    sbSql.append("WHERE ");
      	    sbSql.append("((T.ISKGSJ = '1' AND T.KGSJ_SJ IS NOT NULL) OR (T.ISKGSJ = '0'))");
      	  	sbSql.append("AND ((T.ISWGSJ = '1' AND T.WGSJ_SJ IS NOT NULL) OR (T.ISWGSJ = '0')) ");
      	    sbSql.append("AND ((T.ISKYPF = '1' AND T.KYPF_SJ IS NOT NULL) OR (T.ISKYPF = '0')) ");
      	   	sbSql.append("AND ((T.ISHPJDS = '1' AND T.HPJDS_SJ IS NOT NULL) OR (T.ISHPJDS ='0')) ");
	       	sbSql.append("AND ((T.ISGCXKZ = '1' AND T.GCXKZ_SJ IS NOT NULL) OR (T.ISGCXKZ = '0')) ");
	       	sbSql.append("AND ((T.ISSGXK = '1' AND T.SGXK_SJ IS NOT NULL) OR (T.ISSGXK ='0')) ");
	       	sbSql.append("AND ((T.ISCBSJPF = '1' AND T.CBSJPF_SJ IS NOT NULL) OR (T.ISCBSJPF = '0')) ");
	       	sbSql.append("AND ((T.ISCQT = '1' AND T.CQT_SJ IS NOT NULL) OR (T.ISCQT = '0')) ");
	       	sbSql.append("AND ((T.ISPQT = '1' AND T.PQT_SJ IS NOT NULL) OR (T.ISPQT = '0')) ");
	       	sbSql.append("AND ((T.ISSGT = '1' AND T.SGT_SJ IS NOT NULL) OR (T.ISSGT = '0')) ");
	       	sbSql.append("AND ((T.ISTBJ = '1' AND T.TBJ_SJ IS NOT NULL) OR (T.ISTBJ = '0')) ");
	       	sbSql.append("AND ((T.ISCS = '1' AND T.CS_SJ IS NOT NULL) OR (T.ISCS = '0')) ");
	       	sbSql.append("AND ((T.ISJLDW = '1' AND T.JLDW_SJ IS NOT NULL) OR (T.ISJLDW  = '0')) ");
	       	sbSql.append("AND ((T.ISSGDW = '1' AND T.SGDW_SJ IS NOT NULL) OR (T.ISSGDW = '0')) ");
	       	sbSql.append("AND ((T.ISZC = '1' AND T.ZC_SJ IS NOT NULL) OR (T.ISZC = '0')) ");
	       	sbSql.append("AND ((T.ISPQ = '1' AND T.PQ_SJ IS NOT NULL) OR (T.ISPQ = '0')) ");
	       	sbSql.append("AND ((T.ISJG = '1' AND T.JG_SJ IS NOT NULL) OR (T.ISJG = '0')) ");
	       	sbSql.append("AND T.ND = '"+year+"' AND T.SFYX = '1' AND (t.XMBS = '1' or t.ISNOBDXM = '1')");
       		sql = sbSql.toString();
        		String[][] ywcArray_bd = DBUtil.query(conn, sql);
        		if(Pub.emptyArray(ywcArray_bd)){
	        	 result = ywcArray_bd[0][0].toString();
	        	 out.println(result);
	         }
		%>
        </span>
         <%
         	String bdzs_bd = "";
         	sbSql = new StringBuffer();
	   	    sbSql.append("SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ T ");
	   	    sbSql.append("WHERE ");
	   	 	sbSql.append("T.ND = '"+year+"' AND T.SFYX = '1' AND (t.XMBS = '1' or t.ISNOBDXM = '1')");
    		sql = sbSql.toString();
    		String[][] bdzsArray_bd = DBUtil.query(conn, sql);
    		if(Pub.emptyArray(bdzsArray_bd)){
    			bdzs_bd = String.valueOf(Integer.parseInt(bdzsArray_bd[0][0].toString()));
    		}
	         if(Integer.parseInt(bdzs_bd) == 0){
	    			out.println("[0%]");
	    		}else if(Integer.parseInt(result) == 0){
	    			out.println("[0%]");
	    		}else{
	    			out.println("["+Pub.myPercent(Integer.parseInt(result), Integer.parseInt(bdzs_bd))+"%]" );
	 	    	}
        %>，正常执行 <span class="textUnderline">
        <%
			sbSql = new StringBuffer();
			sbSql.append("SELECT COUNT(GC_JH_SJ_ID) AS ZCZX ");
			sbSql.append("FROM GC_JH_SJ T ");
			sbSql.append("WHERE ");
			sbSql.append("((T.ISKGSJ = '1' AND TRUNC(SYSDATE) <= T.KGSJ) OR (T.ISWGSJ = '0')) ");
			sbSql.append("AND ((T.ISWGSJ = '1' AND TRUNC(SYSDATE) <= T.WGSJ) OR (T.ISWGSJ = '0')) ");
			sbSql.append("AND ((T.ISKYPF = '1' AND trunc(SYSDATE) <= T.KYPF) OR (T.ISKYPF = '0')) ");
			sbSql.append("AND ((T.ISHPJDS = '1' AND trunc(SYSDATE) <= T.HPJDS) OR (T.ISHPJDS = '0')) ");
			sbSql.append("AND ((T.ISGCXKZ = '1' AND trunc(SYSDATE) <= T.GCXKZ) OR (T.ISGCXKZ = '0')) ");
			sbSql.append("AND ((T.ISSGXK = '1' AND trunc(SYSDATE) <= T.SGXK) OR (T.ISSGXK = '0')) ");
			sbSql.append("AND ((T.ISCBSJPF = '1' AND trunc(SYSDATE) <= T.CBSJPF) OR (T.ISCBSJPF = '0')) ");
			sbSql.append("AND ((T.ISCQT = '1' AND trunc(SYSDATE) <= T.CQT) OR (T.ISCQT = '0')) ");
			sbSql.append("AND ((T.ISPQT = '1' AND trunc(SYSDATE) <= T.PQT) OR (T.ISPQT = '0')) ");
			sbSql.append("AND ((T.ISSGT = '1' AND trunc(SYSDATE) <= T.SGT) OR (T.ISSGT = '0')) ");
			sbSql.append("AND ((T.ISTBJ = '1' AND trunc(SYSDATE) <= T.TBJ) OR (T.ISTBJ = '0')) ");
			sbSql.append("AND ((T.ISCS = '1' AND trunc(SYSDATE) <= T.CS) OR (T.ISCS = '0')) ");
			sbSql.append("AND ((T.ISJLDW = '1' AND trunc(SYSDATE) <= T.JLDW) OR (T.ISJLDW = '0')) ");
			sbSql.append("AND ((T.ISSGDW = '1' AND trunc(SYSDATE) <= T.SGDW) OR (T.ISSGDW = '0')) ");
			sbSql.append("AND ((T.ISZC = '1' AND trunc(SYSDATE) <= T.ZC) OR (T.ISZC = '0')) ");
			sbSql.append("AND ((T.ISPQ = '1' AND trunc(SYSDATE) <= T.PQ) OR (T.ISPQ = '0')) ");
			sbSql.append("AND ((T.ISJG = '1' AND trunc(SYSDATE) <= T.JG) OR (T.ISJG = '0')) ");
			sbSql.append("AND T.ND = '"+year+"' AND T.SFYX = '1' AND (t.XMBS = '1' or t.ISNOBDXM = '1')");
			sql = sbSql.toString();
        	String[][] zczxArray_bd = DBUtil.query(conn, sql);
        	if(Pub.emptyArray(zczxArray_bd)){
	        	 result = zczxArray_bd[0][0].toString();
	        	 out.println(result);
	         }
		%>
        </span>
        <%
        if(Integer.parseInt(bdzs_bd) == 0){
			out.println("[0%]");
		}else if(Integer.parseInt(result) == 0){
			out.println("[0%]");
		}else{
			out.println("["+Pub.myPercent(Integer.parseInt(result), Integer.parseInt(bdzs_bd))+"%]" );
	    	}
        %>，延期 <span class="textUnderline">
        <%
			sbSql = new StringBuffer();
			sbSql.append("SELECT COUNT(GC_JH_SJ_ID) AS YQ ");
			sbSql.append("FROM GC_JH_SJ T ");
			sbSql.append("WHERE ");
			sbSql.append("((T.ISKGSJ = '1' AND T.KGSJ_SJ IS NULL AND (trunc(SYSDATE)-T.KGSJ>5 AND trunc(SYSDATE)-T.KGSJ<=10)) ");
			sbSql.append("OR (T.ISWGSJ = '1' AND T.WGSJ_SJ IS NULL AND (trunc(SYSDATE)-T.KGSJ>5 AND trunc(SYSDATE)-T.KGSJ<=10))  ");
			sbSql.append("OR (T.ISZC = '1' AND T.ZC_SJ IS NULL AND (trunc(SYSDATE)-T.ZC>5 AND trunc(SYSDATE)-T.ZC<=10)) ");
			sbSql.append("OR (T.ISPQ = '1' AND T.PQ_SJ IS NULL AND (trunc(SYSDATE)-T.PQ>5 AND trunc(SYSDATE)-T.PQ<=10)) ");
			sbSql.append("OR (T.ISKYPF = '1' AND T.KYPF_SJ IS NULL AND (trunc(SYSDATE)-T.KYPF>5 AND trunc(SYSDATE)-T.KYPF<=10)) ");
			sbSql.append("OR (T.ISHPJDS = '1' AND T.HPJDS_SJ IS NULL AND (trunc(SYSDATE)-T.HPJDS>5 AND trunc(SYSDATE)-T.HPJDS<=10)) ");
			sbSql.append("OR (T.ISGCXKZ = '1' AND T.GCXKZ_SJ IS NULL AND (trunc(SYSDATE)-T.GCXKZ>5 AND trunc(SYSDATE)-T.GCXKZ<=10)) ");
			sbSql.append("OR (T.ISSGXK = '1' AND T.SGXK_SJ IS NULL AND (trunc(SYSDATE)-T.SGXK>5 AND trunc(SYSDATE)-T.SGXK<=10)) ");
			sbSql.append("OR (T.ISCBSJPF = '1' AND T.CBSJPF_SJ IS NULL AND (trunc(SYSDATE)-T.CBSJPF>5 AND trunc(SYSDATE)-T.CBSJPF<=10)) ");
			sbSql.append("OR (T.ISCQT = '1' AND T.CQT_SJ IS NULL AND (trunc(SYSDATE)-T.CQT>5 AND trunc(SYSDATE)-T.CQT<=10)) ");
			sbSql.append("OR (T.ISPQT = '1' AND T.PQT_SJ IS NULL AND (trunc(SYSDATE)-T.PQT>5 AND trunc(SYSDATE)-T.PQT<=10)) ");
			sbSql.append("OR (T.ISSGT = '1' AND T.SGT_SJ IS NULL AND (trunc(SYSDATE)-T.SGT>5 AND trunc(SYSDATE)-T.SGT<=10)) ");
			sbSql.append("OR (T.ISTBJ = '1' AND T.TBJ_SJ IS NULL AND (trunc(SYSDATE)-T.TBJ>5 AND trunc(SYSDATE)-T.TBJ<=10)) ");
			sbSql.append("OR (T.ISCS = '1' AND T.CS_SJ IS NULL AND (trunc(SYSDATE)-T.CS>5 AND trunc(SYSDATE)-T.CS<=10)) ");
			sbSql.append("OR (T.ISJLDW = '1' AND T.JLDW_SJ IS NULL AND (trunc(SYSDATE)-T.JLDW>5 AND trunc(SYSDATE)-T.JLDW<=10)) ");
			sbSql.append("OR (T.ISSGDW = '1' AND T.SGDW_SJ IS NULL AND (trunc(SYSDATE)-T.SGDW>5 AND trunc(SYSDATE)-T.SGDW<=10)) ");
			sbSql.append("OR (T.ISJG = '1' AND T.JG_SJ IS NULL AND (trunc(SYSDATE)-T.JG>5 AND trunc(SYSDATE)-T.JG<=10))) ");
			sbSql.append("AND T.ND = '"+year+"' AND T.SFYX = '1' AND (t.XMBS = '1' or t.ISNOBDXM = '1')");
     		sql = sbSql.toString();
     		String[][] yqArray_bd = DBUtil.query(conn, sql);
     		if(Pub.emptyArray(yqArray_bd)){
		      	 result = yqArray_bd[0][0].toString();
		      	 out.println(result);
		       }
		%>
        </span>
        <%
        if(Integer.parseInt(bdzs_bd) == 0){
			out.println("[0%]");
		}else if(Integer.parseInt(result) == 0){
			out.println("[0%]");
		}else{
			out.println("["+Pub.myPercent(Integer.parseInt(result), Integer.parseInt(bdzs_bd))+"%]" );
	    	}
        %>，严重延期 <span class="textUnderline">
        <%
	    	sbSql = new StringBuffer();
	   	    sbSql.append("SELECT COUNT(GC_JH_SJ_ID) AS YZCQ ");
	   	    sbSql.append("FROM GC_JH_SJ T ");
	   	    sbSql.append("WHERE ");
	   	 	sbSql.append("((T.ISKGSJ = '1' AND T.KGSJ_SJ IS NULL AND trunc(SYSDATE)-T.KGSJ>10) ");
			sbSql.append("OR (T.ISWGSJ = '1' AND T.WGSJ_SJ IS NULL AND trunc(SYSDATE)-T.KGSJ>10)  ");
			sbSql.append("OR (T.ISZC = '1' AND T.ZC_SJ IS NULL AND trunc(SYSDATE)-T.ZC>10) ");
			sbSql.append("OR (T.ISPQ = '1' AND T.PQ_SJ IS NULL AND trunc(SYSDATE)-T.PQ>10) ");
			sbSql.append("OR (T.ISKYPF = '1' AND T.KYPF_SJ IS NULL AND trunc(SYSDATE)-T.KYPF>10) ");
			sbSql.append("OR (T.ISHPJDS = '1' AND T.HPJDS_SJ IS NULL AND trunc(SYSDATE)-T.HPJDS>10) ");
			sbSql.append("OR (T.ISGCXKZ = '1' AND T.GCXKZ_SJ IS NULL AND trunc(SYSDATE)-T.GCXKZ>10) ");
			sbSql.append("OR (T.ISSGXK = '1' AND T.SGXK_SJ IS NULL AND trunc(SYSDATE)-T.SGXK>10) ");
			sbSql.append("OR (T.ISCBSJPF = '1' AND T.CBSJPF_SJ IS NULL AND trunc(SYSDATE)-T.CBSJPF>10) ");
			sbSql.append("OR (T.ISCQT = '1' AND T.CQT_SJ IS NULL AND trunc(SYSDATE)-T.CQT>10) ");
			sbSql.append("OR (T.ISPQT = '1' AND T.PQT_SJ IS NULL AND trunc(SYSDATE)-T.PQT>10) ");
			sbSql.append("OR (T.ISSGT = '1' AND T.SGT_SJ IS NULL AND trunc(SYSDATE)-T.SGT>10) ");
			sbSql.append("OR (T.ISTBJ = '1' AND T.TBJ_SJ IS NULL AND trunc(SYSDATE)-T.TBJ>10) ");
			sbSql.append("OR (T.ISCS = '1' AND T.CS_SJ IS NULL AND trunc(SYSDATE)-T.CS>10) ");
			sbSql.append("OR (T.ISJLDW = '1' AND T.JLDW_SJ IS NULL AND trunc(SYSDATE)-T.JLDW>10) ");
			sbSql.append("OR (T.ISSGDW = '1' AND T.SGDW_SJ IS NULL AND trunc(SYSDATE)-T.SGDW>10) ");
			sbSql.append("OR (T.ISJG = '1' AND T.JG_SJ IS NULL AND trunc(SYSDATE)-T.JG>10)) ");
			sbSql.append("AND T.ND = '"+year+"' AND T.SFYX = '1' AND (t.XMBS = '1' or t.ISNOBDXM = '1')");
       		sql = sbSql.toString();
 	        String[][] yzcqArray_bd = DBUtil.query(conn, sql);
 	        if(Pub.emptyArray(yzcqArray_bd)){
   	        	 result = yzcqArray_bd[0][0].toString();
   	        	 out.println(result);
   	         }
  			%>
        </span>
        <%
        if(Integer.parseInt(bdzs_bd) == 0){
			out.println("[0%]");
		}else if(Integer.parseInt(result) == 0){
			out.println("[0%]");
		}else{
			out.println("["+Pub.myPercent(Integer.parseInt(result), Integer.parseInt(bdzs_bd))+"%]" );
	    	}
        %>

     		</div>
     	</td>
      </tr>
      <!-- ==================================================== -->
      <tr>
        <th align="center" valign="middle">节点<br>
        概况</th>
        <td><div class="test"> 计划总节点数： <span class="textUnderline">
        <%
     		sbSql = new StringBuffer();
    	    sbSql.append("SELECT a+b+c+d+e+f+g+h+i+j+k+l+m+n+o+p+q as ZJDS FROM( ");
    	    sbSql.append("SELECT distinct ");
    	    sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISKGSJ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1') as A, ");
    	    sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISWGSJ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1') as b, ");
    	    sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISKYPF = '1' AND T.ND = '"+year+"' AND T.SFYX = '1') as C, ");
    	    sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISHPJDS = '1' AND T.ND = '"+year+"' AND T.SFYX = '1') as D, ");
    	    sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISGCXKZ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1') as E, ");
    	    sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGXK = '1' AND T.ND = '"+year+"' AND T.SFYX = '1') as F, ");
    	    sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCBSJPF = '1' AND T.ND = '"+year+"' AND T.SFYX = '1') as G, ");
    	    sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCQT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1') as H, ");
    	  	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISPQT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1') as I, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1') as J, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISTBJ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1') as K, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCS = '1' AND T.ND = '"+year+"' AND T.SFYX = '1') as L, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISJLDW = '1' AND T.ND = '"+year+"' AND T.SFYX = '1') as M,");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGDW = '1' AND T.ND = '"+year+"' AND T.SFYX = '1') as N, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISZC = '1' AND T.ND = '"+year+"' AND T.SFYX = '1') as O, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISPQ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1') as P, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISJG = '1' AND T.ND = '"+year+"' AND T.SFYX = '1') as Q ");
	       	sbSql.append("FROM GC_JH_SJ T  where t.nd = '"+year+"'");
	       	sbSql.append(")");
         	sql = sbSql.toString();
       		String[][] zjdsArray = DBUtil.query(conn, sql);
       		if(Pub.emptyArray(zjdsArray)){
       			zjds = zjdsArray[0][0].toString();
        		out.println(zjds);
         	}else{
         		zjds = "0";
        		out.println("0");
         	}
		%>
        </span> ，其中：已完成节点 <span class="textUnderline">
        <%
    		sbSql = new StringBuffer();
   	        sbSql.append("SELECT a+b+c+d+e+f+g+h+i+j+k+l+m+n+o+p+q as YWCJD FROM( ");
   	        sbSql.append("SELECT distinct ");
   	        sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISKGSJ = '1' AND  T.ND = '"+year+"' AND SFYX = '1' AND KGSJ_SJ IS NOT NULL) as a,");
   	        sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISWGSJ = '1' AND  T.ND = '"+year+"' AND SFYX = '1' AND WGSJ_SJ IS NOT NULL) as q, ");
   	        sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISKYPF = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND KYPF_SJ IS NOT NULL) as b, ");
   	      	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISHPJDS = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND HPJDS_SJ IS NOT NULL) as c, ");
   	      	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISGCXKZ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND GCXKZ_SJ IS NOT NULL) as d, ");
   	      	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGXK = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND SGXK_SJ IS NOT NULL) as e, ");
   	      	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCBSJPF = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND CBSJPF_SJ IS NOT NULL) as f, ");
   	     	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCQT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND CQT_SJ IS NOT NULL) as g, ");
   	  		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISPQT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND PQT_SJ IS NOT NULL) as h, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND SGT_SJ IS NOT NULL) as i, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISTBJ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND TBJ_SJ IS NOT NULL) as j, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCS = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND CS_SJ IS NOT NULL) as k, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISJLDW = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND JLDW_SJ IS NOT NULL) as l,");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGDW = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND SGDW_SJ IS NOT NULL) as m, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISZC = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND ZC_SJ IS NOT NULL) as n, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISPQ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND PQ_SJ IS NOT NULL) as o, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISJG = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND JG_SJ IS NOT NULL) as p ");
	       	sbSql.append("FROM GC_JH_SJ T  where t.nd = '"+year+"'");
	       	sbSql.append(")");
       		sql = sbSql.toString();
       		String[][] ywcjdArray = DBUtil.query(conn, sql);
       		if(Pub.emptyArray(ywcjdArray)){
       			result = ywcjdArray[0][0].toString();
        		out.println(result);
        	 }
        %>
        </span>
        <%
        if(Integer.parseInt(zjds) == 0){
  			out.println("[0%]");
  		}else if(Integer.parseInt(result) == 0){
  			out.println("[0%]");
  		}else{
  			bfb = Pub.myPercent(Integer.parseInt(result), Integer.parseInt(zjds));
  			if(bfb<=0){
  				out.println("[0.1%]" );
  			}else{
  				out.println("["+bfb+"%]" );
  			}
  	    }
        %>，预警节点 <span class="textUnderline">
        <%
        
     		sbSql = new StringBuffer();
       		sbSql.append("SELECT a+b+c+d+e+f+g+h+i+j+k+l+m+n+o+p+q as YJJD FROM( ");
       		sbSql.append("SELECT distinct ");
       		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISKGSJ = '1' AND T.ND = '"+year+"' AND SFYX = '1' AND KGSJ_SJ IS NULL AND KGSJ IS NOT NULL AND (trunc(SYSDATE) >= KGSJ)) as a,");
       		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISWGSJ = '1' AND T.ND = '"+year+"' AND SFYX = '1' AND WGSJ_SJ IS NULL AND WGSJ IS NOT NULL AND (trunc(SYSDATE) >= WGSJ)) as q, ");
       		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISKYPF = '1' AND T.ND = '"+year+"' AND T.SFYX = '1'  AND KYPF_SJ IS NULL AND KYPF IS NOT NULL AND (trunc(SYSDATE) >= KYPF) AND ((trunc(SYSDATE) - KYPF)>10)) as b, ");
    		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISHPJDS = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND HPJDS_SJ IS NULL AND HPJDS IS NOT NULL AND (trunc(SYSDATE) >= HPJDS) AND ((trunc(SYSDATE) - HPJDS)>10)) as c, ");
    		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISGCXKZ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND GCXKZ_SJ IS NULL AND GCXKZ IS NOT NULL AND (trunc(SYSDATE) >= GCXKZ) AND ((trunc(SYSDATE) - GCXKZ)>10)) as d, ");
    		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGXK = '1' AND T.ND = '"+year+"' AND T.SFYX = '1'  AND SGXK_SJ IS NULL AND SGXK IS NOT NULL AND (trunc(SYSDATE) >= SGXK) AND ((trunc(SYSDATE) - SGXK)>10)) as e, ");
    		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCBSJPF = '1' AND T.ND = '"+year+"' AND T.SFYX = '1'  AND CBSJPF_SJ IS NULL AND CBSJPF IS NOT NULL AND (trunc(SYSDATE) >= CBSJPF) AND ((trunc(SYSDATE) - CBSJPF)>5)) as f, ");
   			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCQT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1'  AND CQT_SJ IS NULL AND CQT IS NOT NULL AND (trunc(SYSDATE) >= CQT) AND ((trunc(SYSDATE) - CQT)>5)) as g, ");
			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISPQT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1'  AND PQT_SJ IS NULL AND PQT IS NOT NULL AND (trunc(SYSDATE) >= PQT) AND ((trunc(SYSDATE) - PQT)>5)) as h, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND SGT_SJ IS NULL AND SGT IS NOT NULL AND (trunc(SYSDATE) >= SGT) AND ((trunc(SYSDATE) - SGT)>5)) as i, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISTBJ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND TBJ_SJ IS NULL AND TBJ IS NOT NULL AND (trunc(SYSDATE) >= TBJ) AND ((trunc(SYSDATE) - TBJ)>5)) as j, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCS = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND CS_SJ IS NULL AND CS IS NOT NULL AND (trunc(SYSDATE) >= CS) AND ((trunc(SYSDATE) - CS)>5)) as k, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISJLDW = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND JLDW_SJ IS NULL AND JLDW IS NOT NULL AND (trunc(SYSDATE) >= JLDW) AND ((trunc(SYSDATE) - JLDW)>5)) as l,");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGDW = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND SGDW_SJ IS NULL AND SGDW IS NOT NULL AND (trunc(SYSDATE) >= SGDW) AND ((trunc(SYSDATE) - SGDW)>5)) as m, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISZC = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND ZC_SJ IS NULL AND ZC IS NOT NULL AND (trunc(SYSDATE) >= ZC)) as n, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISPQ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND PQ_SJ IS NULL AND PQ IS NOT NULL AND (trunc(SYSDATE) >= PQ)) as o, ");
	       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISJG = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND JG_SJ IS NULL AND JG IS NOT NULL AND (trunc(SYSDATE) >= JG) AND ((trunc(SYSDATE) - JG)>5)) as p ");
	       	sbSql.append("FROM GC_JH_SJ T  where t.nd = '"+year+"'");
	       	sbSql.append(")");
       		sql = sbSql.toString();
       		String[][] yjjdArray = DBUtil.query(conn, sql);
       		if(Pub.emptyArray(yjjdArray)){
       			result = yjjdArray[0][0].toString();
      	        out.println(result);
      	    }
     		%>
        </span>
        <%
        if(Integer.parseInt(zjds) == 0){
  			out.println("[0%]");
  		}else if(Integer.parseInt(result) == 0){
  			out.println("[0%]");
  		}else{
  			bfb = Pub.myPercent(Integer.parseInt(result), Integer.parseInt(zjds));
  			if(bfb<=0){
  				out.println("[0.1%]" );
  			}else{
  				out.println("["+bfb+"%]" );
  			}
  	    	}
        %><br> 节点完成情况：  正常完成 <span class="textUnderline">
        <%
        				sbSql = new StringBuffer();
       	         		sbSql.append("SELECT a+b+c+d+e+f+g+h+i+j+k+l+m+n+o+p+q as ZCWC FROM( ");
       	         		sbSql.append("SELECT distinct ");
       	         		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISKGSJ = '1' AND T.ND = '"+year+"' AND SFYX = '1' AND (KGSJ >= KGSJ_SJ)) as a,");
       	         		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISWGSJ = '1' AND T.ND = '"+year+"' AND SFYX = '1' AND (WGSJ >= WGSJ_SJ)) as q, ");
       	         		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISKYPF = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (KYPF >= KYPF_SJ)) as b, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISHPJDS = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (HPJDS >= HPJDS_SJ)) as c, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISGCXKZ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (GCXKZ >= GCXKZ_SJ)) as d, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGXK = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (SGXK >= SGXK_SJ)) as e, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCBSJPF = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (CBSJPF >= CBSJPF_SJ)) as f, ");
       	     			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCQT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1'  AND (CQT >= CQT_SJ)) as g, ");
       	  				sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISPQT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1'  AND (PQT >= PQT_SJ)) as h, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (SGT >= SGT_SJ)) as i, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISTBJ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (TBJ >= TBJ_SJ)) as j, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCS = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (CS >= CS_SJ)) as k, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISJLDW = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (JLDW >= JLDW_SJ)) as l,");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGDW = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (SGDW >= SGDW_SJ)) as m, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISZC = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (ZC >= ZC_SJ)) as n, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISPQ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (PQ >= PQ_SJ)) as o, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISJG = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (JG >= JG_SJ)) as p ");
				       	sbSql.append("FROM GC_JH_SJ T where t.nd = '"+year+"'");
				       	sbSql.append(")");
       	         		sql = sbSql.toString();
       	         		String[][] zcwcArray = DBUtil.query(conn, sql);
       	         		if(Pub.emptyArray(zcwcArray)){
       	         			result = zcwcArray[0][0].toString();
		       	        	out.println(result);
		       	         }
        			%>
        </span>
        <%
        if(Integer.parseInt(zjds) == 0){
  			out.println("[0%]");
  		}else if(Integer.parseInt(result) == 0){
  			out.println("[0%]");
  		}else{
  			bfb = Pub.myPercent(Integer.parseInt(result), Integer.parseInt(zjds));
  			if(bfb<=0){
  				out.println("[0.1%]" );
  			}else{
  				out.println("["+bfb+"%]" );
  			}
  	    }
        %>，延期完成 <span class="textUnderline">
        <%
        				sbSql = new StringBuffer();
       	         		sbSql.append("SELECT a+b+c+d+e+f+g+h+i+j+k+l+m+n+o+p+q as yqwc FROM( ");
       	         		sbSql.append("SELECT distinct ");
       	         		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISKGSJ = '1' AND  T.ND = '"+year+"' AND SFYX = '1' AND (KGSJ_SJ > KGSJ)) as a,");
       	         		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISWGSJ = '1' AND T.ND = '"+year+"' AND SFYX = '1' AND (WGSJ_SJ >WGSJ)) as q, ");
       	         		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISKYPF = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (KYPF_SJ > KYPF) AND (KYPF_SJ - KYPF) <10) as b, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISHPJDS = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (HPJDS_SJ > HPJDS) AND (HPJDS_SJ - HPJDS) < 10) as c, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISGCXKZ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (GCXKZ_SJ > GCXKZ) AND (GCXKZ_SJ - GCXKZ) < 10) as d, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGXK = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (SGXK_SJ > SGXK) AND (SGXK_SJ - SGXK) < 10) as e, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCBSJPF = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (CBSJPF_SJ > CBSJPF) AND (CBSJPF_SJ - CBSJPF) < 5) as f, ");
       	     			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCQT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (CQT_SJ > CQT) AND (CQT_SJ - CQT) < 5) as g, ");
       	  				sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISPQT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (PQT_SJ > PQT) AND (PQT_SJ - PQT)< 5) as h, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (SGT_SJ > SGT) AND (SGT_SJ - SGT) < 5) as i, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISTBJ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (TBJ_SJ > TBJ) AND (TBJ_SJ - TBJ) < 5) as j, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCS = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (CS_SJ > CS) AND (CS_SJ - CS) < 5) as k, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISJLDW = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (JLDW_SJ > JLDW) AND (JLDW_SJ -JLDW) < 5) as l,");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGDW = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (SGDW_SJ > SGDW) AND (SGDW_SJ - SGDW) <5) as m, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISZC = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (ZC_SJ > ZC)) as n, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISPQ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (PQ_SJ > PQ)) as o, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISJG = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (JG_SJ > JG) AND (JG_SJ - JG) < 5) as p ");
				       	sbSql.append("FROM GC_JH_SJ T");
				       	sbSql.append(")");
       	         		sql = sbSql.toString();
       	         		String[][] yqwcArray = DBUtil.query(conn, sql);
       	         		if(Pub.emptyArray(yqwcArray)){
       	         			result = yqwcArray[0][0].toString();
		       	        	out.println(result);
		       	         }
        			%>
        </span>
        <%
        if(Integer.parseInt(zjds) == 0){
  			out.println("[0%]");
  		}else if(Integer.parseInt(result) == 0){
  			out.println("[0%]");
  		}else{
  			bfb = Pub.myPercent(Integer.parseInt(result), Integer.parseInt(zjds));
  			if(bfb<=0){
  				out.println("[0.1%]" );
  			}else{
  				out.println("["+bfb+"%]" );
  			}
  	    	}
        %>，严重超期完成 <span class="textUnderline">
        <%
        				sbSql = new StringBuffer();
       	         		sbSql.append("SELECT a+b+c+d+e+f+g+h+i+j+k+l+m+n+o+p+q as yzcqwc FROM( ");
       	         		sbSql.append("SELECT distinct ");
       	         		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISKGSJ = '1' AND T.ND = '"+year+"' AND SFYX = '1' AND (KGSJ_SJ > KGSJ)) as a,");
       	         		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISWGSJ = '1' AND  T.ND = '"+year+"' AND SFYX = '1' AND (WGSJ_SJ >WGSJ)) as q, ");
       	         		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISKYPF = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (KYPF_SJ > KYPF) AND (KYPF_SJ - KYPF) >10) as b, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISHPJDS = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (HPJDS_SJ > HPJDS) AND (HPJDS_SJ - HPJDS) > 10) as c, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISGCXKZ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (GCXKZ_SJ > GCXKZ) AND (GCXKZ_SJ - GCXKZ) > 10) as d, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGXK = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (SGXK_SJ > SGXK) AND (SGXK_SJ - SGXK) > 10) as e, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCBSJPF = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (CBSJPF_SJ > CBSJPF) AND (CBSJPF_SJ - CBSJPF) > 5) as f, ");
       	     			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCQT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (CQT_SJ > CQT) AND (CQT_SJ - CQT) > 5) as g, ");
       	  				sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISPQT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (PQT_SJ > PQT) AND (PQT_SJ - PQT) > 5) as h, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (SGT_SJ > SGT) AND (SGT_SJ - SGT) > 5) as i, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISTBJ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (TBJ_SJ > TBJ) AND (TBJ_SJ - TBJ) > 5) as j, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCS = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (CS_SJ > CS) AND (CS_SJ - CS) > 5) as k, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISJLDW = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (JLDW_SJ > JLDW) AND (JLDW_SJ -JLDW) > 5) as l,");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGDW = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (SGDW_SJ > SGDW) AND (SGDW_SJ - SGDW) > 5) as m, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISZC = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (ZC_SJ > ZC)) as n, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISPQ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (PQ_SJ > PQ)) as o, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISJG = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND (JG_SJ > JG) AND (JG_SJ - JG) > 5) as p ");
				       	sbSql.append("FROM GC_JH_SJ T where t.nd = '"+year+"'");
				       	sbSql.append(")");
       	         		sql = sbSql.toString();
       	         		String[][] yzcqwcArray = DBUtil.query(conn, sql);
       	         		if(Pub.emptyArray(yzcqwcArray)){
       	         			result = yzcqwcArray[0][0].toString();
		       	        	out.println(result);
		       	         }
        			%>
        </span>
        <%
        if(Integer.parseInt(zjds) == 0){
  			out.println("[0%]");
  		}else if(Integer.parseInt(result) == 0){
  			out.println("[0%]");
  		}else{
  			bfb = Pub.myPercent(Integer.parseInt(result), Integer.parseInt(zjds));
  			if(bfb<=0){
  				out.println("[0.1%]" );
  			}else{
  				out.println("["+bfb+"%]" );
  			}
  	    	}
        %><br> 节点预警情况：一般延期 <span class="textUnderline">
        <%
        				sbSql = new StringBuffer();
       	         		sbSql.append("SELECT a+b+c+d+e+f+g+h+i+j+k+l+m+n+o+p+q as ybyq FROM( ");
       	         		sbSql.append("SELECT distinct ");
       	         		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISKGSJ = '1' AND T.ND = '"+year+"' AND SFYX = '1' AND KGSJ_SJ IS NULL AND (trunc(SYSDATE) > KGSJ)) as a,");
       	         		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISWGSJ = '1' AND T.ND = '"+year+"' AND SFYX = '1' AND WGSJ_SJ IS NULL AND (trunc(SYSDATE) >WGSJ)) as q, ");
       	         		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISKYPF = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND KYPF_SJ IS NULL AND (trunc(SYSDATE) > KYPF) AND (trunc(SYSDATE) - KYPF)<10) as b, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISHPJDS = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND HPJDS_SJ IS NULL AND (trunc(SYSDATE) > HPJDS) AND (trunc(SYSDATE) - HPJDS) < 10) as c, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISGCXKZ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND GCXKZ_SJ IS NULL AND (trunc(SYSDATE) > GCXKZ) AND (trunc(SYSDATE) - GCXKZ) < 10) as d, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGXK = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND SGXK_SJ IS NULL AND (trunc(SYSDATE) > SGXK) AND (trunc(SYSDATE) - SGXK) < 10) as e, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCBSJPF = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND CBSJPF_SJ IS NULL AND (trunc(SYSDATE) > CBSJPF) AND (trunc(SYSDATE) - CBSJPF) < 5) as f, ");
       	     			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCQT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND CQT_SJ IS NULL AND (trunc(SYSDATE) > CQT) AND (trunc(SYSDATE) - CQT) < 5) as g, ");
       	  				sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISPQT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND PQT_SJ IS NULL AND (trunc(SYSDATE) > PQT) AND (trunc(SYSDATE) - PQT)< 5) as h, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND SGT_SJ IS NULL AND (trunc(SYSDATE) > SGT) AND (trunc(SYSDATE) - SGT) < 5) as i, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISTBJ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND TBJ_SJ IS NULL AND (trunc(SYSDATE) > TBJ) AND (trunc(SYSDATE) - TBJ) < 5) as j, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCS = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND CS_SJ IS NULL AND (trunc(SYSDATE) > CS) AND (trunc(SYSDATE) - CS) < 5) as k, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISJLDW = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND JLDW_SJ IS NULL AND (trunc(SYSDATE) > JLDW) AND (trunc(SYSDATE) -JLDW) < 5) as l,");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGDW = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND SGDW_SJ IS NULL AND (trunc(SYSDATE) > SGDW) AND (trunc(SYSDATE) - SGDW) < 5) as m, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISZC = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND ZC_SJ IS NULL AND (trunc(SYSDATE) > ZC)) as n, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISPQ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND PQ_SJ IS NULL AND (trunc(SYSDATE) > PQ)) as o, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISJG = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND JG_SJ IS NULL AND (trunc(SYSDATE) > JG) AND (trunc(SYSDATE) - JG) < 5) as p ");
				       	sbSql.append("FROM GC_JH_SJ T where t.nd = '"+year+"'");
				       	sbSql.append(")");
       	         		sql = sbSql.toString();
       	         		String[][] ybyqArray = DBUtil.query(conn, sql);
       	         		if(Pub.emptyArray(ybyqArray)){
       	         			result = ybyqArray[0][0].toString();
		       	        	out.println(result);
		       	         }
        			%>
        </span>
        <%
        if(Integer.parseInt(zjds) == 0){
  			out.println("[0%]");
  		}else if(Integer.parseInt(result) == 0){
  			out.println("[0%]");
  		}else{
  			bfb = Pub.myPercent(Integer.parseInt(result), Integer.parseInt(zjds));
  			if(bfb<=0){
  				out.println("[0.1%]" );
  			}else{
  				out.println("["+bfb+"%]" );
  			}
  	    	}
        %>，严重超期 <span class="textUnderline">
        <%
        				sbSql = new StringBuffer();
       	         		sbSql.append("SELECT a+b+c+d+e+f+g+h+i+j+k+l+m+n+o+p+q as yzcq FROM( ");
       	         		sbSql.append("SELECT distinct ");
       	         		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISKGSJ = '1' AND T.ND = '"+year+"' AND SFYX = '1' AND KGSJ_SJ IS NULL AND (trunc(SYSDATE) > KGSJ)) as a,");
       	         		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISWGSJ = '1' AND T.ND = '"+year+"' AND SFYX = '1' AND WGSJ_SJ IS NULL AND (trunc(SYSDATE) >WGSJ)) as q, ");
       	         		sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISKYPF = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND KYPF_SJ IS NULL AND (trunc(SYSDATE) > KYPF) AND (trunc(SYSDATE) - KYPF)>10) as b, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISHPJDS = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND HPJDS_SJ IS NULL AND (trunc(SYSDATE) > HPJDS) AND (trunc(SYSDATE) - HPJDS) > 10) as c, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISGCXKZ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND GCXKZ_SJ IS NULL AND (trunc(SYSDATE) > GCXKZ) AND (trunc(SYSDATE) - GCXKZ) > 10) as d, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGXK = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND SGXK_SJ IS NULL AND (trunc(SYSDATE) > SGXK) AND (trunc(SYSDATE) - SGXK) > 10) as e, ");
       	      			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCBSJPF = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND CBSJPF_SJ IS NULL AND (trunc(SYSDATE) > CBSJPF) AND (trunc(SYSDATE) - CBSJPF) > 5) as f, ");
       	     			sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCQT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND CQT_SJ IS NULL AND (trunc(SYSDATE) > CQT) AND (trunc(SYSDATE) - CQT) > 5) as g, ");
       	  				sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISPQT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND PQT_SJ IS NULL AND (trunc(SYSDATE) > PQT) AND (trunc(SYSDATE) - PQT)> 5) as h, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGT = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND SGT_SJ IS NULL AND (trunc(SYSDATE) > SGT) AND (trunc(SYSDATE) - SGT) > 5) as i, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISTBJ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND TBJ_SJ IS NULL AND (trunc(SYSDATE) > TBJ) AND (trunc(SYSDATE) - TBJ) > 5) as j, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISCS = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND CS_SJ IS NULL AND (trunc(SYSDATE) > CS) AND (trunc(SYSDATE) - CS) > 5) as k, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISJLDW = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND JLDW_SJ IS NULL AND (trunc(SYSDATE) > JLDW) AND (trunc(SYSDATE) -JLDW) > 5) as l,");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISSGDW = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND SGDW_SJ IS NULL AND (trunc(SYSDATE) > SGDW) AND (trunc(SYSDATE) - SGDW) > 5) as m, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISZC = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND ZC_SJ IS NULL AND (trunc(SYSDATE) > ZC)) as n, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISPQ = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND PQ_SJ IS NULL AND (trunc(SYSDATE) > PQ)) as o, ");
				       	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ISJG = '1' AND T.ND = '"+year+"' AND T.SFYX = '1' AND JG_SJ IS NULL AND (trunc(SYSDATE) > JG) AND (trunc(SYSDATE) - JG) > 5) as p ");
				       	sbSql.append("FROM GC_JH_SJ T where t.nd = '"+year+"'");
				       	sbSql.append(")");
       	         		sql = sbSql.toString();
       	         		String[][] yzcqjdArray = DBUtil.query(conn, sql);
       	         		if(Pub.emptyArray(yzcqjdArray)){
       	         			result = yzcqjdArray[0][0].toString();
		       	        	out.println(result);
		       	         }
        			%>
        </span>
        <%
        if(Integer.parseInt(zjds) == 0){
  			out.println("[0%]");
  		}else if(Integer.parseInt(result) == 0){
  			out.println("[0%]");
  		}else{
  			bfb = Pub.myPercent(Integer.parseInt(result), Integer.parseInt(zjds));
  			if(bfb<=0){
  				out.println("[0.1%]" );
  			}else{
  				out.println("["+bfb+"%]" );
  			}
  	    	}
        %>
        </div></td>
        </tr>
        <!-- ========================================================================= -->
      <tr>
        <th align="center" valign="middle">项目<br>
        公司<br>
        概况</th>
        <td><div class="test">
         	<table width="98%">
            <thead>
              <tr class="bzgk-table-tr-Border">
                <th rowspan="2" align="center" valign="middle">项目管理公司</th>
                <th colspan="6" align="center" valign="middle">项目/标段数量</th>
              </tr>
              <tr>
                <th valign="middle">项目数</th>
                <th valign="middle">已拆迁</th>
                <th valign="middle">标段数</th>
                <th valign="middle">已排迁</th>
                <th valign="middle">已开工</th>
                <th valign="middle">已完工</th>
              </tr>
            </thead>
            <tbody>
        
        <%
        sbSql = new StringBuffer();
        for(int i = 0; i < xmglgsCoount; i++){
        	xmglgsId = xmglgsValue[i][0].toString();
        	xmglgsText = xmglgsValue[i][1].toString();
        	sbSql.append("SELECT DISTINCT '"+xmglgsText+"'AS MC, ");
        	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ND = '"+year+"' AND SFYX = '1' AND XMBS = '0' AND XMGLGS = '"+xmglgsId+"')AS XMZS, ");
        	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ND = '"+year+"' AND SFYX = '1' AND XMBS = '0' AND (ISZC ='0' OR ZC_SJ IS NOT NULL) AND XMGLGS = '"+xmglgsId+"')AS ZC, ");
        	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ND = '"+year+"' AND SFYX = '1' AND (XMBS = '1' OR ISNOBDXM='1') AND XMGLGS = '"+xmglgsId+"')AS BDZS, ");
        	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ND = '"+year+"' AND SFYX = '1' AND (XMBS = '1' OR ISNOBDXM='1') AND (ISPQ ='0' OR PQ_SJ IS NOT NULL) AND XMGLGS = '"+xmglgsId+"')AS PQ, ");
        	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ND = '"+year+"' AND SFYX = '1' AND (XMBS = '1' OR ISNOBDXM='1') AND (ISKGSJ = '0' OR KGSJ_SJ IS NOT NULL) AND XMGLGS = '"+xmglgsId+"')AS KG, ");
        	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ND = '"+year+"' AND SFYX = '1' AND (XMBS = '1' OR ISNOBDXM='1') AND (ISWGSJ = '0' OR WGSJ_SJ IS NOT NULL) AND XMGLGS = '"+xmglgsId+"')AS WG ");
        	sbSql.append("FROM GC_TCJH_XMXDK ");
        	sbSql.append(" UNION ALL ");
        }
        sbSql.append("SELECT DISTINCT '未指定'AS MC, ");
        sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ND = '"+year+"' AND SFYX = '1' AND XMBS = '0' AND XMGLGS IS NULL)AS XMZS, ");
    	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ND = '"+year+"' AND SFYX = '1' AND XMBS = '0' AND (ISZC ='0' OR ZC_SJ IS NOT NULL) AND XMGLGS IS NULL)AS ZC, ");
    	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ND = '"+year+"' AND SFYX = '1' AND (XMBS = '1' OR ISNOBDXM='1') AND XMGLGS IS NULL)AS BDZS, ");
    	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ND = '"+year+"' AND SFYX = '1' AND (XMBS = '1' OR ISNOBDXM='1') AND (ISPQ ='0' OR PQ_SJ IS NOT NULL) AND XMGLGS IS NULL)AS PQ, ");
    	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ND = '"+year+"' AND SFYX = '1' AND (XMBS = '1' OR ISNOBDXM='1') AND (ISKGSJ = '0' OR KGSJ_SJ IS NOT NULL) AND XMGLGS IS NULL)AS KG, ");
    	sbSql.append("(SELECT COUNT(GC_JH_SJ_ID) FROM GC_JH_SJ WHERE ND = '"+year+"' AND SFYX = '1' AND (XMBS = '1' OR ISNOBDXM='1') AND (ISWGSJ = '0' OR WGSJ_SJ IS NOT NULL) AND XMGLGS IS NULL)AS WG ");
    	sbSql.append("FROM GC_TCJH_XMXDK ");
    	sql = sbSql.toString();
        String[][] xmglgsResult = DBUtil.query(conn, sql);
        %>   
        <%
        	int xmglgsHj[] = new int[7];//项目管理公司合计列数
        	if(Pub.emptyArray(xmglgsResult)){
        		for(int i=0;i<xmglgsResult.length;i++){
        			for(int j = 1;j<=6;j++){
        				if(xmglgsResult[i][j]==""){
        					xmglgsResult[i][j] = "0";
        				}
        				xmglgsHj[j]+=Integer.parseInt(xmglgsResult[i][j]);
        			}
        %>
        	 <% if(0 == i){%>
              	<tr class="bzgk-table-tr-Border">
              <%}else{%>
             	 <tr>
              <%}%>
              <%
              	for(int z=0;z<7;z++){
              %>
              	<td align="center"><%=xmglgsResult[i][z]%></td>
              <% } %>
              </tr>
              
          </tbody>
  		<% }}%>
          <tfoot>
              <tr class="bzgk-table-tr-Border">
                <td align="center">合计</td>
                <%
                	for(int i=1;i<xmglgsHj.length;i++){
                %>
                	<td align="center"><%=xmglgsHj[i]%></td>
                <% } %>
              </tr>
            </tfoot>
          </table>
        </div></td>
        </tr>
      <tr>
        <th align="center" valign="middle">节点<br>
        详情</th>
        <td><div class="test">
         <table width="98%">
            <thead>
              <tr class="bzgk-table-tr-Border">
                <th rowspan="2" align="center" valign="middle">专业/阶段</th>
                <th colspan="4" align="center" valign="middle">完成节点</th>
                <th colspan="3" align="center" valign="middle">预警</th>
              </tr>
              <tr>
                <th valign="middle">总数</th>
                <th valign="middle">正常</th>
                <th valign="middle">延期</th>
                <th valign="middle">严重延期</th>
                <th valign="middle">总数</th>
                <th valign="middle">超期</th>
                <th valign="middle">严重超期</th>
              </tr>
            </thead>
            <tbody>
        
        <%
        String currentPath = application.getRealPath("/jsp/framework/portal/jh.jsp");
        String dir=new java.io.File(currentPath).getParent(); 
        java.io.InputStream fis = new java.io.FileInputStream(dir+"/jhsql.properties");
        Properties prop = new Properties();
        prop.load(fis);
        fis.close();
        sql = prop.getProperty("jdxqSql");
        Object[] jdxqparas = new Object[112];
        //paras[0] = year;
    	for(int i = 0; i<jdxqparas.length;i++){
    		jdxqparas[i] = year;
    	}
        String[][] jdxqResult = DBUtil.querySql(conn, sql,jdxqparas);
        %>   
        
        <%
        	int wcjdZs = 0;
        	int wcjdZc = 0;
        	int wcjdYq = 0;
        	int wcjdYzyq = 0;
        	int yjZs = 0;
        	int yjCq = 0;
        	int yjYzcq = 0;
        	
        	if(null != jdxqResult && jdxqResult.length > 0){
        		for(int i=0;i<jdxqResult.length;i++){
        			for(int j = 1;j<=7;j++){
        				if(jdxqResult[i][j]==""){
        					jdxqResult[i][j] = "0";
        				}
        			}
        			wcjdZs+=Integer.parseInt(jdxqResult[i][1]);
        			wcjdZc+=Integer.parseInt(jdxqResult[i][2]);
        			wcjdYq+=Integer.parseInt(jdxqResult[i][3]);
        			wcjdYzyq+=Integer.parseInt(jdxqResult[i][4]);
        			yjZs+=Integer.parseInt(jdxqResult[i][5]);
        			yjCq+=Integer.parseInt(jdxqResult[i][6]);
        			yjYzcq+=Integer.parseInt(jdxqResult[i][7]);
        %>
        	 <% if(0 == i){%>
              	<tr class="bzgk-table-tr-Border">
              <%}else{%>
             	 <tr>
              <%}%>
                <td align="center"><%=jdxqResult[i][0]%></td>
                <td align="center"><%=jdxqResult[i][1]%></td>
                <td align="center"><%=jdxqResult[i][2]%></td>
                <td align="center"><%=jdxqResult[i][3]%></td>
                <td align="center"><%=jdxqResult[i][4]%></td>
                <td align="center"><%=jdxqResult[i][5]%></td>
                <td align="center"><%=jdxqResult[i][6]%></td>
                <td align="center"><%=jdxqResult[i][7]%></td>
              </tr>
              
          </tbody>
  		<% }}%>
          <tfoot>
              <tr class="bzgk-table-tr-Border">
                <td align="center">合计</td>
                <td align="center"><%=wcjdZs%></td>
                <td align="center"><%=wcjdZc%></td>
                <td align="center"><%=wcjdYq%></td>
                <td align="center"><%=wcjdYzyq%></td>
                <td align="center"><%=yjZs%></td>
                <td align="center"><%=yjCq%></td>
                <td align="center"><%=yjYzcq%></td>
              </tr>
            </tfoot>
          </table>
        </div></td>
        </tr>
      </table>
    </div>
    <h4>计财计划下达概况</h4>
	<div class="bzgk-table">
    <table width="98%">
            <thead>
              <tr class="bzgk-table-tr-Border">
                <th rowspan="2" align="center" valign="middle">计财批次</th>
                <th rowspan="2" align="center" valign="middle">下达时间</th>
                <th colspan="3" align="center" valign="middle">项目数量</th>
                <th colspan="4" align="center" valign="middle">总投资（万元）</th>
              </tr>
              <tr>
                <th valign="middle">总数</th>
                <th valign="middle">应急</th>
                <th valign="middle">BT</th>
                <th valign="middle">工程费用</th>
                <th valign="middle">征收费用</th>
                <th valign="middle">其他</th>
                <th valign="middle">合计</th>
              </tr>
            </thead>
            <tbody>
        
        <%
        sql = prop.getProperty("jcSql");
        Object[] jcpcparas = new Object[16];
    	for(int i = 0; i<jcpcparas.length;i++){
    		jcpcparas[i] = year;
    	}
        String[][] jcpcResult = DBUtil.querySql(conn, sql,jcpcparas);

        %>   
        
        <%
       
        	String jcxmslZs = "0";
	        String jcxmslYj = "0";
	        String jcxmslBt = "0";
	        String jcztzGc = "0";
	        String jcztzZs = "0";
	        String jcztzQt = "0";
	        String jcztzHj = "0";
        	
        	if(null != jcpcResult && jcpcResult.length > 0){
        		for(int i=0;i<jcpcResult.length;i++){
        			for(int j = 2;j<=8;j++){
        				if(jcpcResult[i][j].equals("")){
        					jcpcResult[i][j] = "0";
        				}
        			}
        			jcxmslZs = MathTool.add(jcxmslZs, jcpcResult[i][2]);
        			jcxmslYj = MathTool.add(jcxmslYj, jcpcResult[i][3]);
        			jcxmslBt = MathTool.add(jcxmslBt, jcpcResult[i][4]);
        			jcztzGc = MathTool.add(jcztzGc, jcpcResult[i][5]);
        			jcztzZs = MathTool.add(jcztzZs, jcpcResult[i][6]);
        			jcztzQt = MathTool.add(jcztzQt, jcpcResult[i][7]);
        			jcztzHj = MathTool.add(jcztzHj, jcpcResult[i][8]);
        			 
        %>
        	 <% if(0 == i){%>
              	<tr class="bzgk-table-tr-Border">
              <%}else{%>
             	 <tr>
              <%}%>
                <td align="center"><%=jcpcResult[i][0]%></td>
                <td align="center"><%=jcpcResult[i][1]%></td>
                <td align="center"><%=jcpcResult[i][2]%></td>
                <td align="center" ><%=jcpcResult[i][3]%></td>
                <td align="center"><%=jcpcResult[i][4]%></td>
                <td align="center" style="text-align:right;padding-right:5px;"><%=Pub.NumberAddDec(Pub.NumberToThousand(jcpcResult[i][5]))%></td>
                <td align="center" style="text-align:right;padding-right:5px;"><%=Pub.NumberAddDec(Pub.NumberToThousand(jcpcResult[i][6]))%></td>
                <td align="center" style="text-align:right;padding-right:5px;"><%=Pub.NumberAddDec(Pub.NumberToThousand(jcpcResult[i][7]))%></td>
                <td align="center" style="text-align:right;"><%=Pub.NumberAddDec(Pub.NumberToThousand(jcpcResult[i][8]))%></td>
              </tr>
              
          </tbody>
  		<% 
  		}}
  		%>
          
          <tfoot>
              <tr class="bzgk-table-tr-Border">
                <td align="center">合计</td>
                <td align="center">-</td>
                <td align="center"><%=jcxmslZs%></td>
                <td align="center"><%=jcxmslYj%></td>
                <td align="center"><%=jcxmslBt%></td>
                <td align="center" style="text-align:right;padding-right:5px;"><%=Pub.NumberAddDec(Pub.NumberToThousand(jcztzGc))%></td>
                <td align="center" style="text-align:right;padding-right:5px;"><%=Pub.NumberAddDec(Pub.NumberToThousand(jcztzZs))%></td>
                <td align="center" style="text-align:right;padding-right:5px;"><%=Pub.NumberAddDec(Pub.NumberToThousand(jcztzQt))%></td>
                <td align="center" style="text-align:right;"><%=Pub.NumberAddDec(Pub.NumberToThousand(jcztzHj))%></td>
              </tr>
            </tfoot>
          </table>
    </div>
</div>
<!-- start 引用bootstrap --> 
<script src="js/bootstrap.js"></script> 
<script src="js/jsBruce.js"></script> 
<!-- end 引用bootstrap -->
<div class="QuickLinks">
			<h3>快速入口</h3>
			<div  style="text-align:center;">
				<app:quickEntry/>
			</div>
			<!-- end 快速入口 -->
		</div>
</body>

</html>