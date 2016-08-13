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
	String id = request.getParameter("id");
	Connection conn = DBUtil.getConnection();//定义连接
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String currYear = (new SimpleDateFormat("yyyy")).format(new Date());
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
</style>
		<script type="text/javascript"><!--
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtsjController.do";
//检查次数
$(function() {
	init();
		
});
var nd = '<%=currYear%>';
var xmid = '<%=id%>';
function init(){
	
	
	
	var action1 = "${pageContext.request.contextPath }/TztjServlet?nd="+nd+"&xmid="+xmid;

	//资金执行情况图表
	$.ajax({
		url : action1,
		//contentType:'application/json;charset=UTF-8',	    
		success: function(xml){ 
			var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSLine.swf", "myChartIdxmsl1", "100%", "100%"); 
			myChart.setDataXML(xml);  
			myChart.render("tzqk");  
		},
		error : function(xml) {
			var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSLine.swf", "myChartIdxmsl1", "100%", "100%");
			myChart.setDataXML("");  
			myChart.render("tzqk");  
		}
	});

	//项目总支付情况
	$.ajax({	
		url : controllername+"?queryTzxxk_zjzfqktj&xmid="+xmid,	
		cache : false,	
		async :	false,	
		dataType : "json",
		type : 'post',		
		success : function(response) {	
			$("#resultXML").val(response.msg);	
			var resultobj = defaultJson.dealResultJson(response.msg);	
			if(resultobj!=null&&resultobj.ZZF!=null&&resultobj.ZZF!=""){
				$("#zzf").text(resultobj.ZZF_SV);
			}
		}
	});

	//项目总支付情况 - 累计至上年
	$.ajax({	
		url : controllername+"?queryTzxxk_zjzfqktj_zsn&xmid="+xmid,	
		cache : false,	
		async :	false,	
		dataType : "json",
		type : 'post',		
		success : function(response) {	
			$("#resultXML").val(response.msg);	
			var resultobj = defaultJson.dealResultJson(response.msg);	
			if(resultobj!=null&&resultobj.ZZF!=null&&resultobj.ZZF!=""){
				$("#zsn").text(resultobj.ZZF_SV);
			}
		}
	});

	//项目总支付情况 - 本年度支付
	$.ajax({	
		url : controllername+"?queryTzxxk_zjzfqktj_bnd&xmid="+xmid,	
		cache : false,	
		async :	false,	
		dataType : "json",
		type : 'post',		
		success : function(response) {	
			$("#resultXML").val(response.msg);	
			var resultobj = defaultJson.dealResultJson(response.msg);	
			if(resultobj!=null&&resultobj.ZZF!=null&&resultobj.ZZF!=""){
				$("#bnd").text(resultobj.ZZF_SV);
			}
		}
	});
		
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?queryTzxxk_zxqk&nd="+nd+"&xmid="+xmid,data,DT1,null,true);

	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT2);
	defaultJson.doQueryJsonList(controllername+"?queryTzxxk_zfqk&nd="+nd+"&xmid="+xmid,data,DT2,null,true);
	
}



//判断标段是否有值
function pdbd(obj){
	var bdid = obj.BDMC;
	if(bdid=='undefinde'||bdid=='')
	{
		return '<div style="text-align:center">—</div>'
	}	
}
function doZCHts(obj){
	if(obj.LB == '已签订（履行中）合同'){
		return obj.ZC_SV+"(共"+obj.ZCHTS+"份)";
	}else{
		return obj.ZC_SV;
	}
}
function doPQHts(obj){
	if(obj.LB == '已签订（履行中）合同'){
		return obj.PQ_SV+"(共"+obj.PQHTS+"份)";
	}else{
		return obj.PQ_SV;
	}
}
function doSGHts(obj){
	if(obj.LB == '已签订（履行中）合同'){
		return obj.SG_SV+"(共"+obj.SGHTS+"份)";
	}else{
		return obj.SG_SV;
	}
}
function doJLHts(obj){
	if(obj.LB == '已签订（履行中）合同'){
		return obj.JL_SV+"(共"+obj.JLHTS+"份)";
	}else{
		return obj.JL_SV;
	}
}
function doSJHts(obj){
	if(obj.LB == '已签订（履行中）合同'){
		return obj.SJ_SV+"(共"+obj.SJHTS+"份)";
	}else{
		return obj.SJ_SV;
	}
}
function doQTHts(obj){
	if(obj.LB == '已签订（履行中）合同'){
		return obj.QT_SV+"(共"+obj.QTHTS+"份)";
	}else{
		return obj.QT_SV;
	}
}
function doBL(obj){
	if(obj.BL==''){
		return '<div style="text-align:center">—</div>';
	}
	if(obj.TYPENAME=='完成投资'||obj.TYPENAME=='支付'){
		return obj.BL;
	}else{
		return '<div style="text-align:center">—</div>';
	}
}
</script>
</head>
<body>
<div class="row-fluid">
<%--	<h4 class="title" style="font-size:16px">--%>
<%--		投资信息--%>
<%--	</h4>--%>
<form name="queryForm">
</form>
	<table width="100%">
		<tr>
			<td  style="vertical-align:top;" colspan="2">
				<div class="B-small-from-table-autoConcise">
					<%
						String allXmidSql = "select gc_tcjh_xmxdk_id from GC_TCJH_XMXDK xdk where xdk.xmwybh in " 
								+ "(select xmwybh from GC_TCJH_XMXDK x0 where x0.gc_tcjh_xmxdk_id='"+id+"')";
						String[][] results ;
						String sql ;
						sql = "select sum(decode(g0.jhztze,null,0,g0.jhztze*10000)) ztze from gc_tcjh_xmcbk g0 left join gc_tcjh_xmxdk g1 on g0.xmbh = g1.xmbh where g1.gc_tcjh_xmxdk_id in ("+allXmidSql+")";
						results = DBUtil.query(conn,sql);
					%>
					<h4 class="title">
						投资信息
						<br>
							<font style="font-size: 14px;font-family: SimYou,Microsoft YaHei,Helvetica Neue, Helvetica, Arial, sans-serif;">
								项目总投资：&nbsp;<%=Pub.NumberToThousand(results[0][0])%>
								&nbsp;元
								<%
									sql = "SELECT xdk.gc_tcjh_xmxdk_id,xdk.xj_xmid,xdk.xjxj,xdk.nd,xdk.jhztze,xdk.gc, "
										+" (select  t1.jhztze from gc_tcjh_xmxdk t1 where t1.gc_tcjh_xmxdk_id = xdk.xj_xmid) qnztz"
										+" FROM gc_tcjh_xmxdk xdk where xdk.gc_tcjh_xmxdk_id  in ("+allXmidSql+")";
									results = DBUtil.query(conn,sql);
									if("1".equals(results[0][1])){
										%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											累计至上年：&nbsp;
											<font>
							                	<%=Pub.NumberToThousand(results[0][6])%>
							                </font>          
											&nbsp;元
											&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
											本年度投资计划：&nbsp;
											<font>
							                	<%=Pub.NumberToThousand(results[0][4])%>
							                </font>
											&nbsp;元
											&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											本年度工程总投资：&nbsp;
											<font>
							                	<%=Pub.NumberToThousand(results[0][5])%>
							                </font>
											&nbsp;元
										<%
									}
								%>
								
							</font>
					</h4>
					<div style="height:30px;"> </div>	
					<div id=""> 
					</div>   
				</div>
			</td>
		</tr>
		<tr>
			<td width="45%">
					<div class="B-small-from-table-autoConcise">
						<h4 class="title">
							本年度总投资执行情况
							<br/>
						</h4>
						<div class="">
							<table class="table-hover table-activeTd B-table" id="DT1"	width="100%" type="single" noPage="true">
								<thead>
									<tr>
										<th colindex=1 tdalign="left" fieldname="TYPENAME">
											&nbsp;&nbsp;
										</th>
										<th fieldname="GCZTZ" colindex=2 tdalign="right" >
											&nbsp;工程总投资(元)&nbsp;
										</th>
										<th fieldname="ZCZTZ" colindex=3 tdalign="right">
											&nbsp;征拆总投资(元)&nbsp;
										</th>
										<th fieldname="QTZTZ" colindex=4 tdalign="right">
											&nbsp;其它投资(元)&nbsp;
										</th>
										<th fieldname="HJ" colindex=5 tdalign="right">
											&nbsp;合计(元)&nbsp;
										</th>
										<th fieldname="BL" colindex=6 tdalign="right" CustomFunction="doBL">
											&nbsp;比例(%)&nbsp;
										</th>
									</tr>
								</thead>
								<tbody></tbody>
							</table>
						</div>	
					 </div>
			</td>
			<td width="50%">
					<fieldset class="b_ddd">
						<div class="row-fluid">
							<!-- 使用fushionchar-->
							<div class="span6" style="width:100%; height: 210px;"
								id="tzqk" align="center"></div>
						</div>
					</fieldset>
			</td>		
		</tr>
		<tr>
			<td colspan="2">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						资金信息
						<br>
						<font style="font-size: 14px;font-family: SimYou,Microsoft YaHei,Helvetica Neue, Helvetica, Arial, sans-serif;">
							项目资金总支付：&nbsp;
							<span id="zzf">
							</span>
<%--							<% --%>
<%--								sql = "select decode(sum(ghh.htzf),null,0,sum(ghh.htzf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where ghh.xmid in ( "--%>
<%--									+" select x1.gc_tcjh_xmxdk_id  from gc_tcjh_xmxdk x1 where x1.xmbh in (select x.xmbh from gc_tcjh_xmxdk x where x.gc_tcjh_xmxdk_id = '"+id+"'))";--%>
<%--								results = DBUtil.query(conn, sql);--%>
<%--								if(results!=null){--%>
<%--									out.print(Pub.NumberToThousand(results[0][0]));--%>
<%--								}else{--%>
<%--									out.print(0);--%>
<%--								}--%>
<%--							%>--%>
							元
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							累计至上年支付&nbsp;
							<font>
							<span id="zsn">
							</span>
<%--			                <%--%>
<%--				                sql = "select decode(sum(ghh.htzf),null,0,sum(ghh.htzf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where ghh.xmid in ( "--%>
<%--									+" select x1.gc_tcjh_xmxdk_id  from gc_tcjh_xmxdk x1 where x1.xmbh in (select x.xmbh from gc_tcjh_xmxdk x where x.gc_tcjh_xmxdk_id = '"+id+"'))"--%>
<%--									+" and to_date(gh.qdnf,'yyyy') < to_date('"+currYear+"','yyyy')";--%>
<%--								results = DBUtil.query(conn, sql);--%>
<%--								if(results!=null){--%>
<%--									out.print(Pub.NumberToThousand(results[0][0]));--%>
<%--								}else{--%>
<%--									out.print(0);--%>
<%--								}--%>
<%--			                %>--%>
			                </font>           
							&nbsp;元
							&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							本年已支付&nbsp;
							<font>
							<span id="bnd">
							</span>
<%--			                <%--%>
<%--				                sql = "select decode(sum(ghh.htzf),null,0,sum(ghh.htzf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where ghh.xmid in ( "--%>
<%--									+" select x1.gc_tcjh_xmxdk_id  from gc_tcjh_xmxdk x1 where x1.xmbh in (select x.xmbh from gc_tcjh_xmxdk x where x.gc_tcjh_xmxdk_id = '"+id+"'))"--%>
<%--									+" and gh.qdnf='"+currYear+"'";--%>
<%--								results = DBUtil.query(conn, sql);--%>
<%--								if(results!=null){--%>
<%--									out.print(Pub.NumberToThousand(results[0][0]));--%>
<%--								}else{--%>
<%--									out.print(0);--%>
<%--								}--%>
<%--								--%>
<%--			                %>--%>
			                </font>
							&nbsp;元
						</font>
					</h4>
					<div style="height:30px;"> </div>								
					<div id=""> 
			            <table width="100%" class="table-hover table-activeTd B-table" id="DT2" type="single" noPage="true">
			                <thead>
			                    <tr>
			                     	<th fieldname="LB">&nbsp;类别&nbsp;</th>
									<th fieldname="ZC" tdalign="right" customfunction="doZCHts">&nbsp;征拆合同(元)&nbsp;</th>
									<th fieldname="PQ" tdalign="right" customfunction="doPQHts">&nbsp;排迁合同(元)&nbsp;</th>						
									<th fieldname="SG" tdalign="right" customfunction="doSGHts">&nbsp;施工合同(元)&nbsp;</th>
									<th fieldname="JL" tdalign="right" customfunction="doJLHts">&nbsp;监理合同(元)&nbsp;</th>
									<th fieldname="SJ" tdalign="right" customfunction="doSJHts">&nbsp;设计合同(元)&nbsp;</th>
									<th fieldname="QT" tdalign="right" customfunction="doQTHts">&nbsp;其它合同(元)&nbsp;</th>
			                    </tr>
			                </thead> 
			              	<tbody></tbody>
			           </table>
			    	</div>
				</div> 
			</td>
		</tr>
	</table>
</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="r0.sort_num" />
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
				<input type="hidden" name="queryResult" id="queryResult" />
			</FORM>
		</div>
	</body>
</html>