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
<%
	Connection conn = DBUtil.getConnection();//定义连接
	StringBuffer sbSql = null;//sql语句字符串
	String sql = "";//查询参数字符串
	String year = Pub.getDate("yyyy", new Date());
%>
<app:base/>
<head>
<title>部长概况</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<style type="text/css">
body {font-size:14px;}
h2 {display:inline; line-height:2em;}
.table2 {
	/* border-left: #000 solid 1px;
	border-top: #000 solid 1px; */
	margin:10px auto;
}
.marginBottom15px {margin-bottom:15px;}
.table2 tr td,.table2 tr th {
	line-height: 1.5em;
	padding: 4px;
	/* border-right: #000 solid 1px; */
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
.table3 tr th {/* border-top: #000 solid 1px; border-bottom: #000 solid 1px; */}
.table4 {
	/* border: #000 solid 1px; */
}
.table4 tr td,.table4 tr th {
	line-height: 1.5em;
	padding: 4px;
	border-right: #000 solid 0;
	border-bottom: #000 solid 0;
}
.bmjkTitleLine{
	background: none;
	color: #000; 
	border-bottom: #5E5E5E solid 1px;
	margin-bottom:3px;
	font-size:20px;
	padding-bottom:5px;
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
	border-top: #ccc solid 1px;
	color: #fff;
	background:#36648B;
}
</style>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/ShowChart/ShowChartController_zlaq.do";
var controllername_jc="${pageContext.request.contextPath }/zlaq/jcxxController.do";
//检查次数
$(function() {
	$("#printButton").click(function(){
		$(this).hide();
		window.print();
		$(this).show();
	});
	generateNd($("#ND"));
	setDefaultNd("ND");
	var nd = $("#ND").val(); 
	jc_zg_xxtj(nd);
	zlaqqk(nd);
	sgdw_top(nd);
	jldw_top(nd);
	
	var action1 = controllername + "?single_chartData_zlaq&nd="+nd;
	$.ajax({
		url : action1,  
		success: function(xml){
			var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Column2D.swf", "myChartIdxmsl1", "100%", "100%");
	     	myChart.setDataXML(xml);  
	     	myChart.render("jdzxqk1");  
		}
	});
	
	
    //监听年度变化
    $("#ND").change(function() {
    	var nd = $("#ND").val(); 
    	jc_zg_xxtj(nd);
    	zlaqqk(nd);
    	sgdw_top(nd);
    	jldw_top(nd);
    	var action1 = controllername + "?single_chartData_zlaq&nd="+nd;
    	$.ajax({
    		url : action1,  
    		success: function(xml){
    			var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Column2D.swf", "myChartIdxmsl1", "100%", "100%");
    	     	myChart.setDataXML(xml);  
    	     	myChart.render("jdzxqk1");  
    		}
    	});
   	
    }); 
	
});


//检查整改信息统计
function jc_zg_xxtj(nd)
{
	var action1 = controllername_jc + "?jc_zg_xxtj&nd="+nd;
	$.ajax({
		url : action1,
		success: function(result)
		{
			 insertTable(result);
		}
	});
}


//根据结果放入数字
function insertTable(result)
{
	var resultmsgobj = convertJson.string2json1(result);
	var resultobj = convertJson.string2json1(resultmsgobj.msg);
	var subresultmsgobj = resultobj.response.data[0];
   $("span").each(function(i){
		var str = $(this).attr("bzfieldname");
		var jspname = $(this).attr("jspname");
		if(str!=''&&str!=undefined)
		{
			if($(subresultmsgobj).attr(str)!=0)
			{
				 $(this).html('<a href="javascript:void(0);" onclick="zdyzq(\''+jspname+'\',\'BMJK_ZLAQ_'+str+'\')",\'1\'>'+$(subresultmsgobj).attr(str)+'</a>');			
			}
			else
			{
				 $(this).html($(subresultmsgobj).attr(str));
			}	
		}
	});
}


//自定义钻取
function zdyzq(name,sqlname){
	var nd = $("#ND").val();
	var  xmsc = {"title":name,"type":"text","content":g_sAppName+"/jsp/business/zlaq/zlaq_zq.jsp?nd="+nd+"&sqlname="+sqlname,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}


//将结果放入表格
function zlaqqk(nd)
{
	$("#zlaqqk tr td").remove();
	var action1 = controllername_jc + "?zlaqqk&nd="+nd;
	$.ajax({
		url : action1,
		success: function(result)
		{
			var resultmsgobj = convertJson.string2json1(result);
			var resultobj = convertJson.string2json1(resultmsgobj.msg);
			var len = resultobj.response.data.length;
			var showHtml='';
			for(var i=0;i<len;i++){
				showHtml +="<tr>";
				$("#zlaqqk tr th").each(function(j)
				{
					var subresultmsgobj = resultobj.response.data[i];
					var str = $(this).attr("bzfieldname");
					showHtml+="<td style=\"text-align:center;\">"+$(subresultmsgobj).attr(str)+" </td>";	
				});
				showHtml+="</tr>";
			}
			$("#zlaqqk").append(showHtml);
			$($("#zlaqqk tbody>tr:even")).addClass("tableTrEvenColor");
			$("#zlaqqk").addClass("tableList");
			$($("#zlaqqk thead tr:eq(0)")).addClass("tableTheadTr");
		}
	});
}


//施工单位排名
function sgdw_top(nd)
{
	$("#sgdw_top tr td").remove();
	var action1 = controllername_jc + "?sgdw_top&nd="+nd;
	$.ajax({
		url : action1,
		success: function(result)
		{
			var resultmsgobj = convertJson.string2json1(result);
			if(resultmsgobj.msg==0)
			{
				return;
			}	
			var resultobj = convertJson.string2json1(resultmsgobj.msg);
			var len = resultobj.response.data.length;
			var showHtml='';
			for(var i=0;i<len;i++){
				showHtml +="<tr>";
				$("#sgdw_top tr th").each(function(j)
				{
					var subresultmsgobj = resultobj.response.data[i];
					var str = $(this).attr("bzfieldname");
					showHtml+="<td>"+$(subresultmsgobj).attr(str)+" </td>";	
				});
				showHtml+="</tr>";
			}
			$("#sgdw_top").append(showHtml);
		}
	});
}


//监理单位排名
function jldw_top(nd)
{
	$("#jldw_top tr td").remove();
	var action1 = controllername_jc + "?jldw_top&nd="+nd;
	$.ajax({
		url : action1,
		success: function(result)
		{
			var resultmsgobj = convertJson.string2json1(result);
			var resultobj = convertJson.string2json1(resultmsgobj.msg);
			if(resultmsgobj.msg==0)
			{
				return;
			}	
			var len = resultobj.response.data.length;
			var showHtml='';
			for(var i=0;i<len;i++){
				showHtml +="<tr>";
				$("#jldw_top tr th").each(function(j)
				{
					var subresultmsgobj = resultobj.response.data[i];
					var str = $(this).attr("bzfieldname");
					showHtml+="<td>"+$(subresultmsgobj).attr(str)+" </td>";	
				});
				showHtml+="</tr>";
			}
			$("#jldw_top").append(showHtml);
		}
	});
}


//年份查询
function generateNd(ndObj){
 	ndObj.attr("src","T#GC_ZLAQ_JCB:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(new Date().getFullYear());
}
</script>
</head>
<body>
<app:dialogs/>
    <span class="pull-right">
    	<button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
    </span>
    <form method="post" id="queryForm" style="margin:1px;">
			<h4  class="bmjkTitleLine">质量安全检查概要信息&nbsp;&nbsp;
			<select	id="ND" class="span12 year" noNullSelect ="true" style="width:7%;" name="ND" fieldname="ND" defaultMemo="全部" operation="=" ></select>
			</h4>
	</form>
    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table2">
    <tr>
        <td width="100%">
        	<table width="100%" border="0" cellspacing="0" class="table1" cellpadding="0">
	            <tr>
	                <td width="54%" id="jdzxqk1">图表</td>
	                <div style="position:absolute;top:120px;width:20px;font-size:13px;">检查次数</div>
	                <td width="2%"></td>
	                <td width="44%"><!-- <h4 id="new_nd1"></h4> -->
						总检查次数：&nbsp;<span bzfieldname="JC_NUM" jspname="所有检查项目"></span>
	  					<br>
	                    <table width="100%" border="0" class="table4" cellspacing="0" cellpadding="0">
	                        <tr>
	                            <td width="33%">需要整改：&nbsp;<span bzfieldname="ZG_SUM" jspname="需要整改项目"></span></td>
	                            <td width="33%">综合检查次数：&nbsp;<span bzfieldname="JCZH_NUM" jspname="综合检查项目"></span></td>
	                            <td width="33%">省市联检次数：&nbsp;<span bzfieldname="JCSS_NUM" jspname="省市联检项目"></span></td>
	                        </tr>
	                        <tr>
	                            <td>整改通知发出：&nbsp;<span bzfieldname="ZGTZ_NUM" jspname="发出整改通知项目"></span></td>
	                            <td>质量检查次数：&nbsp;<span bzfieldname="JCZL_NUM" jspname="质量检查项目"></span></td>	                           
	                            <td>市级联检次数：&nbsp;<span bzfieldname="JCSJ_NUM" jspname="市级联检项目"></span></td>
	                        </tr>
	                        <tr>
	                            <td>整改回复：&nbsp;<span bzfieldname="ZGHF_NUM" jspname="整改回复项目"></span></td>
	                             <td>安全检查次数：&nbsp;<span bzfieldname="JCAQ_NUM" jspname="安全检查项目"></span></td>	                            
	                            <td>例行检查次数：&nbsp;<span bzfieldname="JCLX_NUM" jspname="例行检查项目"></span></td>
	                        </tr>
	                        <tr>
	                            <td>待复查：&nbsp;<span bzfieldname="DFC_SUM" jspname="待复查项目"></span></td>
	                            <!-- <td>待复查：&nbsp;<span bzfieldname="ZGHF_NUM" ></span></td> -->
	                            <td>检测检查次数：&nbsp;<span bzfieldname="JCJC_NUM" jspname="检测检查项目"></span></td>	                          
	                            <td>&nbsp;</td>
	                        </tr>
	                        <tr>
	                            <td>已复查：<span bzfieldname="ZGFC_NUM" jspname="已复查项目"></span></td>
	                            <td>预验收检查数：&nbsp;<span bzfieldname="JCYYS_NUM" jspname="预验收检查项目"></span></td>	                            
	                            <td>&nbsp;</td>
	                        </tr>
	                        <tr>
	                            <td><span style="color:#900;">超期未整改：<span bzfieldname="ZGCQ_NUM" jspname="超期未整改项目"></span></td>
	                            <td>&nbsp;</td>
	                            <td>&nbsp;</td>
	                        </tr>
	                    </table>
	            	</td>
	            </tr>
        	</table>
        </td>
    </tr>
</table>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table2">
    <tr>
        <td width="53%" align="left" style="border-right:none;"><h4 class="bmjkTitleLine">各项目公司质量安全情况</h4></td>
        <td width="1%" style="border-bottom: #fff solid 1px;"></td>
        <td width="46%" align="left"  ><h4 class="bmjkTitleLine">参建单位质量安全情况</h4></td>
        <!-- <td width="20%"></td> -->
    </tr>
    <tr>
        <td colspan="3">
        	<table width="100%" border="0" cellspacing="0" class="" cellpadding="0">
	            <tr>
	                <td width="53%" valign="top">
	                	<table  id="zlaqqk" width="100%" border="0" cellpadding="0" cellspacing="0" class="">
	                	<thead>
		                    <tr style="border-bottom: #ccc solid 1px;">
				                <th  bzfieldname="XMGLGS"><div style="text-align: center;">&nbsp;</div></th>
				                <th  bzfieldname="JC_SUM"><div style="text-align: center;">总检查次数</div></th>
				                <th  bzfieldname="ZG_SUM"><div style="text-align: center;">需要整改</div></th>
				                <th  bzfieldname="ZGTZ_NUM"><div style="text-align: center;">整改通知发出</div></th>
				                <th  bzfieldname="ZGHF_NUM"><div style="text-align: center;">整改回复</div></th>
				                <th  bzfieldname="DFC_SUM"><div style="text-align: center;">待复查</div></th>
				                <th  bzfieldname="ZGFC_NUM"><div style="text-align: center;">已复查</div></th>
				                <th  bzfieldname="ZGCQ_NUM"><div style="text-align: center;">超期未整改</div></th>
		                    </tr>
		                    </thead>
	                	</table>
	                </td>
	                <td width="1%"></td>
	                <td width="23%" valign="top"><font style="line-height: 1.5em;padding: 4px;font-weight: bold;">涉及整改最多的施工单位(TOP5)</font>
	                    <table width="100%" border="0" class="table1" cellspacing="0" cellpadding="0" id="sgdw_top">
		                    <tr style="border-bottom: #ccc solid 1px;">
				                <th  bzfieldname="SGDW" width="82%"></th>
				                 <th  bzfieldname="SGDW_NUM" width="18%"></th>
		                    </tr>
	                	</table>
	                </td>
					<td width="23%" valign="top"><font style="line-height: 1.5em;padding: 4px;font-weight: bold;">涉及整改最多的监理单位(TOP5)</font>         
	                     <table width="100%" border="0" class="table1" cellspacing="0" cellpadding="0" id="jldw_top">
		                    <tr style="border-bottom: #ccc solid 1px;">
				                <th  bzfieldname="JLDW" width="82%"></th>
		                    	<th  bzfieldname="JLDW_NUM" width="18%"></th>
		                    </tr>
	                	 </table>
	                </td>	                  		
	            </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>