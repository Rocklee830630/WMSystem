<!DOCTYPE html>
<html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="java.lang.StringBuffer"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.plugin.AppInit"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
	Connection conn = DBUtil.getConnection();//定义连接
	StringBuffer sbSql = null;//sql语句字符串
	String sql = "";//查询参数字符串
	//String year = Pub.getDate("yyyy", new Date());
	//获取当前用户信息
	String deptId = request.getParameter("xmglgs");
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	if(null == deptId){
		deptId = dept.getDeptID();
	}
	Date d=new Date();//获取时间
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//转换格式
	String today=sdf.format(d);
	String jlyf=today.substring(0, 7);
	
	String time=Pub.getDate("yyyy-MM-dd", new Date());//当前日期
	String month=time.substring(5, 7);		
	String depStr = "";//用于记录项目管理公司ID的串
%>
<app:base/>
<head>
<title>部长概况</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style type="text/css">
body {font-size:14px;}
h4{background:none; color:#333; border-bottom:#ccc solid 1px;font-size: 15px}  
h2 {display:inline; line-height:2em;}
.table2 {
/* 	border-left: #000 solid 1px;
	border-top: #000 solid 1px;
 */	margin:10px auto;
}
.marginBottom15px {margin-bottom:15px;}
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
.table3 tr th {border-top: #000 solid 1px; border-bottom: #000 solid 1px;}
.table4 {
	border: #000 solid 1px;
}
.table4 tr td,.table4 tr th {
	line-height: 1.5em;
	padding: 4px;
	border-right: #000 solid 0;
	border-bottom: #000 solid 0;
}
.boldline
{
 background:none; color:#333; border-bottom:#ccc solid 1px;
}
</style>
<script src="${pageContext.request.contextPath }/js/common/FixTable.js"></script>
<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/ShowChart/ShowChartController_xmglgs.do";
var controllername_zb= "${pageContext.request.contextPath }/ShowChart/ShowChartController_zb.do";
var controllername_xmglgsgk= "${pageContext.request.contextPath }/xmglgsgk/xmglgsgkController.do";
var controllername_query= "${pageContext.request.contextPath }/gczb/gczbController.do";
var time = '<%=today%>';//监控日期
<%-- var year = '<%=year%>';//获取本年 --%>
var deptId = '<%=deptId%>';//获取当前登录人所在部门
var flag_xm=true,flag_yd=true,num_xm,num_yd,height_xxxq,height_ydxq;
$(function() {
	generateNd($("#ND"));
	setDefaultNd("ND");
	setTime();
	queryAll();//监控日期
	//switchNd();//切换年度
	//height_xxxq=$("#div_xxxq").height()-$("#t_xm_body").height();
	//height_ydxq=$("#div_ydxq").height()-$("#t_yd_body").height()
	//setPageHeight();//计算本页表格高度
	
	 //监听年度变化
    $("#ND").change(function() {
    	switchNd();
    });
	
	$("#printButton").click(function(){
		$(this).hide();
		window.print();
		$(this).show();
	});
});


//计算本页表格高度
function setPageHeight(){
	num_xm=$("#t_xm_body").height()/pageTableOne;
	if(num_xm>12)
	{
		num_xm=12;
	}
	$("#div_xxxq").height(pageTableOne*num_xm+height_xxxq);
	
	
	num_yd=$("#t_yd_body").height()/pageTableOne;
	if(num_yd>12)
	{
		num_yd=12;
	}	
	$("#div_ydxq").height(pageTableOne*num_yd+height_ydxq);
}

//初始化日期为当前日期
function setTime(){
	$("#KSSJ").val(time);//初始化监控日期为当前日期
}


//更改日期调用所有查询
function queryAll(){
	var nd = $("#ND").val(); 
	zb_jl_tjt(nd);//周报计量统计图
	fzr_ztxx(nd);//负责人及统计数字
	queryBzgzlWcHj();//本周工程量完成合计
	queryByljwc();//本月累计完成合计
	queryXmxqList();//项目详情
	queryYdxqList(nd);//月度详情
	zgxm_xm_top(nd);//整改项目奇数top
	zgxm_bd_top(nd);//整改项目偶数top
	zbxq_bg(nd);//招标需求列表
	xmht_bg(nd);//项目合同列表
	//setPageHeight();
}


//切换年度
function switchNd()
{
	
	queryAll()
	
}


//周报计量统计图
function zb_jl_tjt(nd)
{
	var action1 = controllername_zb + "?single_chartData_zb&xmglgs="+deptId+"&nd="+nd;
	$.ajax({
		url : action1,  
		success: function(xml){
			//alert(xml)
			var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSStackedColumn2D.swf", "myChartIdxms1", "100%", "300");

	     	myChart.setDataXML(xml);  
	     	myChart.render("zb");
		}
	});
	var action2 = controllername + "?single_chartData_jl&xmglgs="+deptId+"&nd="+nd;
	$.ajax({
		url : action2,  
		success: function(xml){
			//alert(xml)
			var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Column2D.swf", "myChartIdxmsl1", "100%", "300");

	     	myChart.setDataXML(xml);  
	     	myChart.render("jl");  
		}
	});
}

//招标需求表格
function zbxq_bg(nd)
{
	$("#zbxq_bg tr td").remove();
	var action1 = controllername_query + "?zbxq_bg&nd="+nd+"&xmglgs="+deptId;
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
				$("#zbxq_bg tr th").each(function(j)
				{
					var subresultmsgobj = resultobj.response.data[i];
					var str = $(this).attr("bzfieldname");
					if(str=='XMGLGS')
					{
						showHtml+="<td>"+$(subresultmsgobj).attr(str)+" </td>";
					}
					else
					{
						showHtml+="<td style=\"text-align:center;\">"+$(subresultmsgobj).attr(str)+" </td>";	
					}	
				});
				showHtml+="</tr>";
			}
			$("#zbxq_bg").append(showHtml);
		}
	});
}


//项目合同表格
function xmht_bg(nd)
{
	$("#xmht_bg tr td").remove();
	var action1 = controllername_query + "?xmht_bg&nd="+nd+"&xmglgs="+deptId;
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
				$("#xmht_bg tr th").each(function(j)
				{
					var subresultmsgobj = resultobj.response.data[i];
					var str = $(this).attr("bzfieldname");
					if(str=='XMGLGS')
					{
						showHtml+="<td>"+$(subresultmsgobj).attr(str)+" </td>";
					}
					else
					{
						if(str=='WCZF_SV'||str=='HTQDJ_SV')
						{
							showHtml+="<td style=\"text-align:right;\">"+$(subresultmsgobj).attr(str)+" </td>";
						}
						else
						{
							showHtml+="<td style=\"text-align:center;\">"+$(subresultmsgobj).attr(str)+" </td>";	
						}	
					}	
				});
				showHtml+="</tr>";
			}
			$("#xmht_bg").append(showHtml);
		}
	});
}


//整改项目排名
function zgxm_xm_top(nd)
{
	$("#zgxm_xm_top tr td").remove();
	var action1 = controllername_query + "?zgxm_xm_top&nd="+nd+"&xmglgs="+deptId;
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
				$("#zgxm_xm_top tr th").each(function(j)
				{
					var subresultmsgobj = resultobj.response.data[i];
					var str = $(this).attr("bzfieldname");
					showHtml+="<td>"+$(subresultmsgobj).attr(str)+" </td>";	
				});
				showHtml+="</tr>";
			}
			$("#zgxm_xm_top").append(showHtml);
		}
	});
}

//整改标段排名
function zgxm_bd_top(nd)
{
	$("#zgxm_bd_top tr td").remove();
	var action1 = controllername_query + "?zgxm_bd_top&nd="+nd+"&xmglgs="+deptId;
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
				$("#zgxm_bd_top tr th").each(function(j)
				{
					var subresultmsgobj = resultobj.response.data[i];
					var str = $(this).attr("bzfieldname");
					showHtml+="<td>"+$(subresultmsgobj).attr(str)+" </td>";	
				});
				showHtml+="</tr>";
			}
			$("#zgxm_bd_top").append(showHtml);
		}
	});
}


//年份查询
function generateNd(ndObj){
	ndObj.attr("src","T#GC_JH_SJ:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(new Date().getFullYear());
}


//根据结果放入数字
function insertTable(result)
{
	var resultmsgobj = convertJson.string2json1(result);
	var resultobj = convertJson.string2json1(resultmsgobj.msg);
	var subresultmsgobj = resultobj.response.data[0];
	$("span").each(function(i){
		var str = $(this).attr("bzfieldname");
		var flag =$(this).attr("flag");
		var sqlname =$(this).attr("sqlname");
		var jspname =$(this).attr("jspname");
		if(str!=''&&str!=undefined)
		{
			var str1=str+"_SV";
			if($(subresultmsgobj).attr(str1)!==''&&$(subresultmsgobj).attr(str1)!=undefined)
			{
	     		 $(this).html($(subresultmsgobj).attr(str1));
			}
			else
			{
				if($(subresultmsgobj).attr(str)!=0)
				{
					switch(flag)
					{
					case 'xm_xdk':
						$(this).html('<a href="javascript:void(0);" onclick="_blank_xm(\'060001\',\'BMJK_XMGLGS_'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
						break;
					case 'bd_xdk':
						$(this).html('<a href="javascript:void(0);" onclick="_blank_bd(\'060001\',\'BMJK_XMGLGS_'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
						break;
					case 'xm':
						$(this).html('<a href="javascript:void(0);" onclick="_blank_xm_bm(\'060001\',\'BMJK_XMGLGS_'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
						break;
					case 'bd':
						$(this).html('<a href="javascript:void(0);" onclick="_blank_bd_bm(\'060001\',\'BMJK_XMGLGS_'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
						break;
					case 'zlaq':
						$(this).html('<a href="javascript:void(0);" onclick="zdyzq_zlaq(\''+jspname+'\',\'BMJK_ZLAQ_'+str+'\',\''+sqlname+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
						break;
					case 'fctg':
						$(this).html('<a href="javascript:void(0);" onclick="zdyzq_zlaq(\''+jspname+'\',\'BMJK_ZLAQ_FCTG\',\''+sqlname+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
						break;
					case 'YTJ_SUM':
						$(this).html('<a href="javascript:void(0);" onclick="zdyzq_gcqs(\'年度工程洽商\',\'1\')">'+$(subresultmsgobj).attr(str)+'</a>');
						break;						
					case 'BYTJ_SUM':
						$(this).html('<a href="javascript:void(0);" onclick="zdyzq_gcqs(\'本月工程洽商\',\'2\')">'+$(subresultmsgobj).attr(str)+'</a>');
						break;
					default:
						$(this).html($(subresultmsgobj).attr(str));	
						break;
					}
				}
				else
				{
				 	$(this).html($(subresultmsgobj).attr(str));					
				}	
			}
		}
	});
}


//本周工作量完成合计
function queryBzgzlWcHj(){
	$("#KSSJ_zb").val($("#KSSJ").val());
	$("#JSSJ_zb").val($("#KSSJ").val());
	var year=$("#KSSJ").val().substring(0,4);
	var action3 = controllername_query + "?query_bz&xmglgs="+deptId+"&year="+year;
	var data = {
			msg : combineQuery.getQueryCombineData(queryForm_zb,frmPost,null)
		};
		$.ajax({
			url : action3,
			data : data,
			dataType : "json",  
			type : 'post',
			success : function(result) {
				var num=$("#bzhj");
				num[0].innerHTML='本周工程量完成合计：'+result.msg+'万元';		
		}
	});
}


//本月累计完成合计
function queryByljwc(){
	var time=$("#KSSJ").val();
	var action4 = controllername_query + "?query_bylj&time="+time+"&xmglgs="+deptId
	var data = {
			msg : combineQuery.getQueryCombineData(queryForm_zb,frmPost,null)
		};
		$.ajax({
			url : action4,
			data : data,
			dataType : "json",  
			type : 'post',
			success : function(result) {
				var num=$("#byljwc");
				num[0].innerHTML='本月累计完成合计：'+result.msg+'元';		
		}
	});
}


//查询项目详情
function queryXmxqList(){
	var time = $("#KSSJ").val();
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xmxqList);
	defaultJson.doQueryJsonList(controllername_query+"?query_gk&xmglgs="+deptId+"&time="+time,data,xmxqList,"queryAfter",true);
}
//固定表头-项目
function queryAfter(){
	var rows = $("tbody tr" ,$("#xmxqList"));
	var tr_obj = rows.eq(0);
     var t = $("#xmxqList").getTableRows();
     var tr_height = $(tr_obj).height();
     var d = t*tr_height;
     if(t<5){
    	 d = d+getTableTh(2)+20;
     }else{
    	 d = 5*tr_height+getTableTh(2)+20;
     }
 	 if(tr_height==null) d = getTableTh(2)+20;
     window_width = document.documentElement.clientWidth;//$("#allDiv").width()
   //  alert(window_width)
	    $("#xmxqList").fixTable({
			fixColumn: 0,//固定列数
			width:window_width-10,//显示宽度
			height:d //显示高度
		});
}

//查询月度详情
function queryYdxqList(nd){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,ydList);
	defaultJson.doQueryJsonList(controllername_query+"?query_yd&xmglgs="+deptId+"&year="+nd,data,ydList,"queryAfter_yd",true);
}

//固定表头-月度
function queryAfter_yd(){
	/* var rows = $(" tbody tr" ,$("#ydList"));
	var tr_obj = rows.eq(0);
     var t = $("#ydList").getTableRows();
     var tr_height = $(tr_obj).height();
     var d = t*tr_height;
     if(t<=5) d = 150;
     if(d>500) d=520; 
 	 if(tr_height==null) d = getTableTh(2)+20;
     window_width = document.documentElement.clientWidth;//$("#allDiv").width()
   //  alert(window_width)
	    $("#ydList").fixTable({
			fixColumn: 0,//固定列数
			width:window_width-10,//显示宽度
			height:d//显示高度
		}); */
}


//显示表格
function showXmTable(){
	$("#t_xm_body").slideToggle("slow");
	if(flag_xm==true)
	{
		$("#div_xxxq").height(height_xxxq)
		var text=document.getElementById("xm");
		text.innerHTML='展开';		
		flag_xm=false;
	}
	else
	{
		$("#div_xxxq").height(pageTableOne*num_xm+height_xxxq);
		var text=document.getElementById("xm");
		text.innerHTML='收缩';		
		flag_xm=true;
	}		
}


//显示表格
function showYdTable(){
	$("#t_yd_body").slideToggle("slow");
	if(flag_yd==true)
	{
		$("#div_ydxq").height(height_ydxq)
		var text=document.getElementById("yd");
		text.innerHTML='展开';		
		flag_yd=false;
	}
	else
	{
		$("#div_ydxq").height(pageTableOne*num_yd+height_ydxq);
		var text=document.getElementById("yd");
		text.innerHTML='收缩';		
		flag_yd=true;
	}	
}


//判断标段是否有值
function pdbd(obj){
	var bdid = obj.BDMC;
	if(bdid=='undefinde'||bdid=='')
	{
		return '<div style="text-align:center">—</div>'
	}	
}


//判断标段编号是否有值
function pdbdbh(obj){
	var bdid = obj.BDID;
	if(bdid=='undefinde'||bdid=='')
	{
		return '<div style="text-align:center">—</div>'
	}	
}


//现场图片图标
function viewimg(obj){
	var isfj=obj.XCZP_SV;
	if(isfj!='' && isfj!=undefined)
	{
		return '<a href="javascript:void(0);"><img src=\"/xmgl/images/icon-annex.png\" title="现场照片" onclick="showPic(\''+obj.XCZP+'\')"></a>';
	}	
}

//现场照片
function showPic(ywid)
{
	$(window).manhuaDialog({"title":"现场照片","type":"text","content":g_sAppName +"/jsp/business/gcb/zbgl/viewimg.jsp?ywid="+ywid+"&lb=0311","modal":"2"});
}


//项目
function _blank_xm(ywlx, bmjkLx,bname) {
	var nd = $("#ND").val();
	var xmglgs='and xdk.xmglgs=\''+deptId+'\'';
	xmxxView(ywlx, nd, bmjkLx,'JHSJ',xmglgs);
}


//计划标段数
function _blank_bd(ywlx, bmjkLx) {
	var nd = $("#ND").val();
	var xmglgs='and xdk.xmglgs=\''+deptId+'\'';
	xmxx_bdxxView(ywlx, nd, bmjkLx,'JHSJ',xmglgs);
}

//项目别名
function _blank_xm_bm(ywlx, bmjkLx) {
	var nd = $("#ND").val();
	var xmglgs='and xmglgs=\''+deptId+'\'';
	xmxxView(ywlx, nd, bmjkLx,'tcjh',xmglgs);
}

//标段别名
function _blank_bd_bm(ywlx, bmjkLx) {
	var nd = $("#ND").val();
	var xmglgs='and xmglgs=\''+deptId+'\'';
	xmxx_bdxxView(ywlx, nd, bmjkLx,'tcjh',xmglgs);
}


//自定义钻取
function zdyzq_zlaq(name,sqlname){
	var nd = $("#ND").val();
	var  xmsc = {"title":name,"type":"text","content":g_sAppName+"/jsp/business/xmglgs/xmglgs_zlaq_zq.jsp?nd="+nd+"&sqlname="+sqlname+"&xmglgs="+deptId,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}
//自定义钻取
function zdyzq_gcqs(name,flag){
	var nd = $("#ND").val();
	var  xmsc = {"title":name,"type":"text","content":g_sAppName+"/jsp/business/gcb/gcqs_zq.jsp?nd="+nd+"&flag="+flag+"&xmglgs="+deptId,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}

//=======================================
//检查整改信息统计
function fzr_ztxx(nd){
	var action = controllername_xmglgsgk + "?fzr_ztxx&nd="+nd+"&xmglgs="+deptId;
	$.ajax({
		url : action,
		success: function(result){
			var obj = convertJson.string2json1(convertJson.string2json1(result).msg).response.data[0];
			//所辖项目数
			if(obj.XM_SUM == "0"){
				$("#XM_SUM").html("0");
			}else{
				$("#XM_SUM").html('<a href="javascript:void(0);" onclick="querySjzq(7)"><span id="XM_SUM">'+obj.XM_SUM+'</span></a>');
			}
			//项目公司负责人
			if(obj.XMGS_FZR != ""){
				$("#XMGS_FZR").html(obj.XMGS_FZR);
			}
			//项目负责人
			if(obj.XM_FZR != ""){
				$("#XM_FZR").html(obj.XM_FZR);
			}
			//标段数
			if(obj.BD_SUM == "0"){
				$("#BD_SUM").html("0");
			}else{
				$("#BD_SUM").html('<a href="javascript:void(0);" onclick="querySjzq(8)"><span id="BD_SUM">'+obj.BD_SUM+'</span></a>');
			}
			//工程总投资
			if(obj.ZTZ_SUM != ""){
				$("#ZTZ_SUM").html(obj.ZTZ_SUM_SV);
			}
			//已开工项目
			if(obj.KGXM_SUM == "0"){
				$("#KGXM_SUM").html("0");
			}else{
				$("#KGXM_SUM").html('<a href="javascript:void(0);" onclick="querySjzq(9)"><span id="KGXM_SUM">'+obj.KGXM_SUM+'</span></a>');
			}
			//标段数
			if(obj.KGBD_SUM == "0"){
				$("#KGBD_SUM").html("0");
			}else{
				$("#KGBD_SUM").html('<a href="javascript:void(0);" onclick="querySjzq(10)"><span id="KGBD_SUM">'+obj.KGBD_SUM+'</span></a>');
			}
			//已完工项目
			if(obj.WGXM_SUM == "0"){
				$("#WGXM_SUM").html("0");
			}else{
				$("#WGXM_SUM").html('<a href="javascript:void(0);" onclick="querySjzq(11)"><span id="WGXM_SUM">'+obj.WGXM_SUM+'</span></a>');
			}
			//标段数
			if(obj.WGBD_SUM == "0"){
				$("#WGBD_SUM").html("0");
			}else{
				$("#WGBD_SUM").html('<a href="javascript:void(0);" onclick="querySjzq(12)"><span id="WGBD_SUM">'+obj.WGBD_SUM+'</span></a>');
			}
			//未反馈项目
			if(obj.WFKXM_SUM == "0"){
				$("#WFKXM_SUM").html("0");
			}else{
				$("#WFKXM_SUM").html('<a href="javascript:void(0);" onclick="querySjzq(13)"><span id="WFKXM_SUM">'+obj.WFKXM_SUM+'</span></a>');
			}
			//标段数
			if(obj.WFKBD_SUM == "0"){
				$("#WFKBD_SUM").html("0");
			}else{
				$("#WFKBD_SUM").html('<a href="javascript:void(0);" onclick="querySjzq(14)"><span id="WFKBD_SUM">'+obj.WFKBD_SUM+'</span></a>');
			}
			//进度拖延项目
			if(obj.YZYQXM == "0"){
				$("#YZYQXM").html("0");
			}else{
				$("#YZYQXM").html('<a href="javascript:void(0);" onclick="querySjzq(15)"><span id="YZYQXM">'+obj.YZYQXM+'</span></a>');
			}
			//标段数
			if(obj.YZYQBD == "0"){
				$("#YZYQBD").html("0");
			}else{
				$("#YZYQBD").html('<a href="javascript:void(0);" onclick="querySjzq(16)"><span id="YZYQBD">'+obj.YZYQBD+'</span></a>');
			}
			//本年度完成合计
			if(obj.BN_SUM != ""){
				$("#BN_SUM").html(obj.BN_SUM_SV);
			}
			//累计完成合计
			if(obj.LJWC_SUM != ""){
				$("#LJWC_SUM").html(obj.LJWC_SUM_SV);
			}
			//开工令办理项目数
			if(obj.KGL_SUM == "0"){
				$("#KGL_SUM").html("0");
			}else{
				$("#KGL_SUM").html('<a href="javascript:void(0);" onclick="querySjzq(17)"><span id="KGL_SUM">'+obj.KGL_SUM+'</span></a>');
			}
			//审批完成数
			if(obj.KGLSP_SUM == "0"){
				$("#KGLSP_SUM").html("0");
			}else{
				$("#KGLSP_SUM").html('<a href="javascript:void(0);" onclick="querySjzq(18)"><span id="KGLSP_SUM">'+obj.KGLSP_SUM+'</span></a>');
			}
			//复工令办理项目数
			if(obj.FGL_SUM == "0"){
				$("#FGL_SUM").html("0");
			}else{
				$("#FGL_SUM").html('<a href="javascript:void(0);" onclick="querySjzq(19)"><span id="FGL_SUM">'+obj.FGL_SUM+'</span></a>');
			}
			//停工令办理项目数
			if(obj.TGL_SUM == "0"){
				$("#TGL_SUM").html("0");
			}else{
				$("#TGL_SUM").html('<a href="javascript:void(0);" onclick="querySjzq(20)"><span id="TGL_SUM">'+obj.TGL_SUM+'</span></a>');
			}
			//周报最新填报时间
			if(obj.MAX_KSSJ != ""){
				$("#MAX_KSSJ").html(obj.MAX_KSSJ);
			}
			//月计量最新填报月份
			if(obj.JLYF != ""){
				$("#JLYF").html(obj.JLYF);
			}
			//年度工程洽商数
			if(obj.YTJ_SUM == "0"){
				$("#YTJ_SUM").html("0");
			}else{
				$("#YTJ_SUM").html('<a href="javascript:void(0);" onclick="querySjzq(26)"><span id="YTJ_SUM">'+obj.YTJ_SUM+'</span></a>');
			}
			//本月提请数
			if(obj.BYTJ_SUM == "0"){
				$("#BYTJ_SUM").html("0");
			}else{
				$("#BYTJ_SUM").html('<a href="javascript:void(0);" onclick="querySjzq(27)"><span id="BYTJ_SUM">'+obj.BYTJ_SUM+'</span></a>');
			}
			
			//接到整改单数
			if(obj.ZGTZ_NUM == "0"){
				$("#ZGTZ_NUM").html("0");
			}else{
				$("#ZGTZ_NUM").html('<a href="javascript:void(0);" onclick="querySjzq(4)"><span id="ZGTZ_NUM">'+obj.ZGTZ_NUM+'</span></a>');
			}
			//已回复
			if(obj.ZGHF_NUM == "0"){
				$("#ZGHF_NUM").html("0");
			}else{
				$("#ZGHF_NUM").html('<a href="javascript:void(0);" onclick="querySjzq(5)"><span id="ZGHF_NUM">'+obj.ZGHF_NUM+'</span></a>');
			}
			//复查已通过
			if(obj.ZGFC_NUM == "0"){
				$("#ZGFC_NUM").html("0");
			}else{
				$("#ZGFC_NUM").html('<a href="javascript:void(0);" onclick="querySjzq(6)"><span id="ZGFC_NUM">'+obj.ZGFC_NUM+'</span></a>');
			}
			//超期未回复
			if(obj.ZGCQ_NUM == "0"){
				$("#ZGCQ_NUM").html("0");
			}else{
				$("#ZGCQ_NUM").html('<a href="javascript:void(0);" onclick="querySjzq(28)"><span id="ZGCQ_NUM">'+obj.ZGCQ_NUM+'</span></a>');
			}
			//已提请款次数
			if(obj.YTQK_SUM == "0"){
				$("#YTQK_SUM").html("0");
			}else{
				$("#YTQK_SUM").html('<a href="javascript:void(0);" onclick="querySjzq(1)"><span id="YTQK_SUM">'+obj.YTQK_SUM+'</span></a>');
			}
			//完成审批次数
			if(obj.SPWC_SUM == "0"){
				$("#SPWC_SUM").html("0");
			}else{
				$("#SPWC_SUM").html('<a href="javascript:void(0);" onclick="querySjzq(2)"><span id="SPWC_SUM">'+obj.SPWC_SUM+'</span></a>');
			}
			//已拨付数
			if(obj.YBF_SUM == "0"){
				$("#YBF_SUM").html("0");
			}else{
				$("#YBF_SUM").html('<a href="javascript:void(0);" onclick="querySjzq(3)"><span id="YBF_SUM">'+obj.YBF_SUM+'</span></a>');
			}
			//提请款总额
			if(obj.TQKZE != ""){
				$("#TQKZE").html(obj.TQKZE_SV);
			}
			//其中审批完成涉及总额
			if(obj.SPWCJE != ""){
				$("#SPWCJE").html(obj.SPWCJE_SV);
			}
			//财务审批总额
			if(obj.CWSPJE != ""){
				$("#CWSPJE").html(obj.CWSPJE_SV);
			}
			//审计审定总额
			if(obj.SJSDE != ""){
				$("#SJSDE").html(obj.SJSDE_SV);
			}
			//拨付总额
			if(obj.BFZE != ""){
				$("#BFZE").html(obj.BFZE_SV);
			}
			
			//
			//insertTable(result);
		}
	});
}

//数字连接
function querySjzq(index){
	var nd = $("#ND").val();
	var xmglgs = deptId;
	if(index == 7){
		$(window).manhuaDialog({"title":"统筹计划管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_tcjh_List.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 8){
		$(window).manhuaDialog({"title":"统筹计划管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_tcjh_List.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 9){
		$(window).manhuaDialog({"title":"形象进度管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_xxjd_list.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 10){
		$(window).manhuaDialog({"title":"形象进度管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_xxjd_list.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 11){
		$(window).manhuaDialog({"title":"形象进度管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_xxjd_list.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 12){
		$(window).manhuaDialog({"title":"形象进度管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_xxjd_list.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 13){
		$(window).manhuaDialog({"title":"形象进度管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_xxjd_list.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 14){
		$(window).manhuaDialog({"title":"形象进度管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_xxjd_list.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 15){
		$(window).manhuaDialog({"title":"形象进度管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_xxjd_list.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 16){
		$(window).manhuaDialog({"title":"形象进度管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_xxjd_list.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 17){
		$(window).manhuaDialog({"title":"开复工令管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_kfgl_List.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 18){
		$(window).manhuaDialog({"title":"开复工令管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_kfgl_List.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 19){
		$(window).manhuaDialog({"title":"开复工令管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_kfgl_List.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 20){
		$(window).manhuaDialog({"title":"开复工令管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_kfgl_List.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 26){
		$(window).manhuaDialog({"title":"工程洽商管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_gcqs_List.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 27){
		$(window).manhuaDialog({"title":"工程洽商管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_gcqs_List.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 1){
		$(window).manhuaDialog({"title":"提请款管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_tqk_List.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 2){
		$(window).manhuaDialog({"title":"提请款管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_tqk_List.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 3){
		$(window).manhuaDialog({"title":"提请款管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_tqk_List.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId});
	}else if(index == 4){
		$(window).manhuaDialog({"title":"质量安全管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zlaq/zlaq_zq.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId+"&sqlname=BMJK_ZLAQ_ZG_SUM"});
	}else if(index == 5){
		$(window).manhuaDialog({"title":"质量安全管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zlaq/zlaq_zq.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId+"&sqlname=BMJK_ZLAQ_ZGHF_NUM"});
	}else if(index == 6){
		$(window).manhuaDialog({"title":"质量安全管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zlaq/zlaq_zq.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId+"&sqlname=BMJK_ZLAQ_FCTG"});
	}else if(index == 28){
		$(window).manhuaDialog({"title":"质量安全管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zlaq/zlaq_zq.jsp?nd="+nd+"&index="+index+"&xmglgs="+deptId+"&sqlname=BMJK_ZLAQ_ZGCQ_NUM"});
	}
}
</script>
</head>
<body>
    <span class="pull-right">
    	<button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
    </span>
<table width="100%" border="0">
	<tr>
		<td width="14%" border="0" nowrap>
			<h4 style="line-height:15px;font-size: 17.5px;font-weight: bold;border-bottom:#ccc solid 0px;"><%=Pub.getDeptNameByID(deptId) %></h4>
		</td>
		<td width="7%">
			<select	id="ND" class="span12 year" style="width:90%;height:28px;margin-top:10px;" noNullSelect ="true" name="ND" fieldname="ND" defaultMemo="全部" operation="="></select>
		</td>
		<td width="52%"></td>
		<td align="right" width="15%">
			<b><font size="3">项目公司负责人：</font></b>
		</td>
		<td width="15%">&nbsp;<span id="XMGS_FZR"></span>&nbsp;&nbsp;</td>
	</tr>
	<tr>
		<td rowspan="2" colspan="2">
			<h3 style="line-height:20px"><b><font color="#2F4F4F" >项目实时状态监控报告</font></b></h3>
		</td>
		<td></td>
		<td align="right">
			<b><font size="3">项目负责人：</font></b>
		</td>
		<td>&nbsp;<span id="XM_FZR"></span>&nbsp;&nbsp;</td>
	</tr>
	<!-- <tr>
	</tr> -->
</table>
<div style="height:1px;width:100%;background:#333;overflow:hidden;"></div>
<h4>总体概况</h4> 
<table border="0" width="100%">
	<tr>
		<td width="27%">所辖项目数：<span id="XM_SUM"></span></td>
		<td width="27%">标段数：<span id="BD_SUM"></span></td>
		<td width="27%">工程总投资：<span id="ZTZ_SUM"></span>&nbsp;&nbsp;亿元&nbsp;</td>
		<td width="19%">已开工项目/标段数： <span id="KGXM_SUM"></span>/<span id="KGBD_SUM"></span></td>
	</tr>
	<tr>
		<td>已完工项目/标段数：<span id="WGXM_SUM"></span>/<span id="WGBD_SUM"></span></td>
		<td>未反馈项目/标段数：<span id="WFKXM_SUM"></span>/<span id="WFKBD_SUM"></span></td>
		<td>进度拖延项目/标段数：<span id="YZYQXM"></span>/<span id="YZYQBD"></span></td>
		<td></td>
	</tr>
	<tr>
		<td>本年度完成合计：<span id="BN_SUM"></span>&nbsp;&nbsp;元&nbsp;</td>
		<td>累计完成合计：<span id="LJWC_SUM"></span>&nbsp;&nbsp;元&nbsp;</td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td>开工令办理项目数（含标段）：<span id="KGL_SUM"></span></td>
		<td>审批完成数：<span id="KGLSP_SUM"></span></td>
		<td>复工令办理项目数：<span id="FGL_SUM"></span></td>
		<td>停工令办理项目数：<span id="TGL_SUM"></span></td>
   	</tr>
</table>
   	</div>
   	<p style="height:5px"></p>
   	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table3" style="margin-top:-10px;">
    	<tr>
	        <td colspan="2" width="50%" align="left" style="border-right:none;">
	        	<h4>项目周报与计量</h4>
	        </td>
    	</tr>
    	<tr>
	    	<td width="50%" align="center" style="border-right:none;">
		    	<b>周报最新填报时间：<span id="MAX_KSSJ"></span></b>
	    	</td>
	    	<td width="50%" align="center" style="border-right:none;">
		    	<b>月计量最新填报月份：<span id="JLYF"></span></b>
	    	</td>
    	</tr>
    </table> 
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="">
	    <tr>
	        <td colspan="2"><table width="100%" border="0" cellspacing="0" class="table1" cellpadding="0">
	            <tr>
					<td width="50%" id="zb"></td>
					<td width="50%" id="jl"></td>
	            </tr>
	        </table></td>
	    </tr>
	</table>
	<div class="B-small-from-table-autoConcise">
		<table style="width:100%;border-bottom:#ccc solid 1px;margin-bottom:10px;" border="0">
			<tr>
				<td width="13%">
					<h4 style="border-bottom:#ccc solid 0px;">项目详情
					<!-- &nbsp;&nbsp;<a href="javascript:void(0)" onClick="showXmTable()"><font size="1" id="xm">收缩</font></a> -->
					</h4>
				</td>
				<td width="25%">
					<b><font style="font-size: 15px;">监控日期：</font></b>
					&nbsp;
					<input style="width:140px;margin-bottom:0px;height:20px;" id="KSSJ" type="date" onchange="queryAll()" fieldtype="date" name="KSSJ" fieldformat="yyyy-MM-dd" operation="<="/> 
					&nbsp;&nbsp;
				</td>
				<td width="22%" id="bzhj" nowrap></td>
				<td width="40%" id="byljwc"></td>
			</tr>
		</table>
		<div class="overFlowX">
			<table class="table-hover table-activeTd B-table" id="xmxqList" width="100%" type="single" noPage="true" pageNum="1000">
				<thead>
					<tr>
						<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
						<th fieldname="XMBH" rowspan="2" colindex=2 tdalign="center" noprint="true">项目编号</th>
						<th fieldname="XMMC"  maxlength="15"  rowspan="2" colindex=3 rowMerge="true">&nbsp;项目名称&nbsp;</th>
						<th fieldname="BDBH" rowspan="2" colindex=4 CustomFunction="pdbdbh">&nbsp;标段编号&nbsp;</th>
						<th fieldname="BDMC"  maxlength="15" rowspan="2" colindex=5 CustomFunction="pdbd">&nbsp;标段名称&nbsp;</th>
						<th fieldname="XCZP" rowspan="2" colindex=6 tdalign="center" CustomFunction="viewimg" noprint="true">&nbsp;现场照片&nbsp;</th>
						<th fieldname="QTWJ" rowspan="2" colindex=7 tdalign="center" noprint="true">&nbsp;其他文件&nbsp;</th>							
						<th colspan="4">&nbsp;周报&nbsp;</th>
						<th colspan="2">&nbsp;月计量&nbsp;</th>
					</tr>
					<tr>
						<th fieldname="KSSJ" colindex=8 tdalign="center">&nbsp;开始时间&nbsp;</th>
						<th fieldname="JSSJ"colindex=9 tdalign="center">&nbsp;结束时间&nbsp;</th>
						<th fieldname="ZJLBZ" colindex=10 tdalign="right">&nbsp;本周完成（万元）&nbsp;</th>
						<th fieldname="ZJLLJWC" colindex=11 tdalign="right">&nbsp;累计完成（万元）&nbsp;</th>
						<th fieldname="JLYF" colindex=12 tdalign="center">&nbsp;月份&nbsp;</th>
						<th fieldname="DYJLSDZ" colindex=13 tdalign="right">&nbsp;当月审定值（元）&nbsp;</th> 							
					</tr>
				</thead>
				<tbody id="t_xm_body"></tbody>
			</table>
		</div>
	 </div> 
<p style="height:5px"></p>
<h4>项目招投标与合同监控</h4>
<div class="gcbDiv" style="border:0px">
	<div class="bzgk-table" style="padding-left:0px">
	    <table width="100%">
	        <tr>
	            <th align="center" valign="middle" nowrap>招标<br>需求</th>
	            <td>
	            	<div class="test">
	                	<table width="100%" id="zbxq_bg" class="xmgsjkTable" border="0" cellspacing="0" cellpadding="0">
	                	<div class="boldline"></div>
		                    <tr>
		                        <th bzfieldname="XMGLGS">&nbsp;</th>
		                        <th bzfieldname="TQ" nowrap>提请数量</th>
		                        <th bzfieldname="YSP" nowrap>已审批数量</th>
		                        <th bzfieldname="YWC" nowrap>已完成招标数量</th>
		                    </tr>
		 		            <tr>
	                			<td colspan="4">
	                				<div class="boldline"></div>
	                			</td>
	                		</tr>
		                </table>
		            </div>
		        </td>
	            <th align="center" valign="middle" nowrap>项目<br>合同<br></th>
                    <td>
                    	<div class="test">
                        	<table width="100%" class="xmgsjkTable" id="xmht_bg" border="0" cellspacing="0" cellpadding="0">
                        	<div class="boldline"></div>
	                            <tr>
	                             	<th bzfieldname="XMGLGS">&nbsp;</th>
	                                <th bzfieldname="HTS" nowrap>已签合同数</th>
	                                <th bzfieldname="HTQDJ_SV" nowrap>合同签订价</th>
	                                <th bzfieldname="ZXHTJ" nowrap>最新合同价</th>
	                                <th bzfieldname="HTZF" nowrap>合同支付</th>
	                                <th bzfieldname="WCZF_SV"  tdalign="right"  nowrap>完成投资</th>
	                            </tr>
								<tr>
		                			<td colspan="6">
		                				<div class="boldline"></div>
		                			</td>
		                		</tr>
	                        </table>
	                    </div>
	            	</td>
                </tr>
                <tr>
                    <th align="center" valign="middle">提请<br> 款</th>
                    <td colspan="3">
                    	<div class="test">
                    		<table width="100%" border="0">
                    			<tr>
                    				<td width="17%">已提请款次数:<span id="YTQK_SUM" style="border-bottom: #000 solid 0px;"></span></td>
                    				<td width="23%">完成审批次数：<span id="SPWC_SUM" style="border-bottom: #000 solid 0px;"></span></td>
                    				<td width="20%">已拨付数：<span id="YBF_SUM" style="border-bottom: #000 solid 0px;"></span>&nbsp;</td>
                    				<td width="20%"></td>
                    			</tr>
	                    		<tr>
	                   				<td>提请款总额：<span id="TQKZE" style="border-bottom: #000 solid 0px;"></span>&nbsp;元</td>
	                   				<td>其中审批完成涉及总额：<span id="SPWCJE" style="border-bottom: #000 solid 0px;"></span>&nbsp;元</td>
	                   				<td>财务审批总额：<span id="CWSPJE" style="border-bottom: #000 solid 0px;"></span>&nbsp;元</td>
	                   				<td>审计审定总额：<span id="SJSDE" style="border-bottom: #000 solid 0px;"></span>&nbsp;元</td>
	                   			</tr>
	                   			<tr>
	                   				<td>拨付总额：<span id="BFZE" style="border-bottom: #000 solid 0px;"></span>&nbsp;元</td>
	                   				<td colspan="3"></td>
	                   			</tr>
                    		</table>
						</div>
					</td>
        		</tr>
    </table>
</div>
<p style="height:5px"></p>
<div class="B-small-from-table-autoConcise">
		<h4>月度详情
		<!-- &nbsp;&nbsp;<a href="javascript:void(0)" onClick="showYdTable()"><font size="1" id="yd">收缩</font></a> -->
		</h4>
		<!-- <small class="pull-right">单位：（元）</small> -->
		<div class="overFlowX">
			<table class="table-hover table-activeTd B-table" id="ydList" width="100%" type="single" noPage="true" pageNum="1000" nopromptmsg="true">
				<thead>
					<tr>
						<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
						<th fieldname="GCPC" rowspan="2" colindex=2 tdalign="center">&nbsp;月份&nbsp;</th>
						<th colspan="2">部门审核情况</th>
						<th colspan="2">财务审核情况</th>
						<th colspan="2">计财审核情况</th>
						<th fieldname="TQKZT" rowspan="2" colindex=9 tdalign="center">&nbsp;状态&nbsp;</th>
						<th fieldname="BZRQ" rowspan="2" colindex=10 tdalign="center">&nbsp;日期&nbsp;</th>
					</tr>
					<tr>
						<th fieldname="BMJLS" colindex=3 tdalign="right">&nbsp;明细数&nbsp;</th>
						<th fieldname="BMZE" colindex=4 tdalign="right">&nbsp;总额（元）&nbsp;</th>
						<th fieldname="CWJLS" colindex=5 tdalign="right">&nbsp;明细数&nbsp;</th>
						<th fieldname="CWZE" colindex=6 tdalign="right">&nbsp;总额（元）&nbsp;</th>
						<th fieldname="JCJLS" colindex=7 tdalign="right">&nbsp;明细数&nbsp;</th>
						<th fieldname="JCZE" colindex=8 tdalign="right">&nbsp;总额（元）&nbsp;</th> 							
					</tr>
				</thead>
				<tbody id="t_yd_body"></tbody>
			</table>
		</div>
	</div>
<p style="height:5px"></p>
<div class="gcbDiv" style="border:0px">
<h4>工程洽商和质量安全整改监控</h4>
	<div class="bzgk-table" style="padding-left:10px">
		<table width="100%">
           <tr>
           		<th align="center" valign="middle" nowrap>工程<br>洽商</th>
            	<td>
               		<div class="test">
               			<table width="100%" border="0">
		               		<tr>
		               			<td width="15%">年度工程洽商数：<span id="YTJ_SUM" style="border-bottom: #000 solid 0px;"></span></td>
		               			<td width="12%">本月提请数：<span id="BYTJ_SUM" style="border-bottom: #000 solid 0px;"></span></td>
		               			<!-- <td width="12%">审批通过数：<span bzfieldname="YSP_SUM" style="border-bottom: #000 solid 0px;"></span></td>
		               			<td width="12%">审批通过数：<span bzfieldname="BYYSP_SUM" style="border-bottom: #000 solid 0px;"></span></td> -->
		               			<td width="73%"></td>
		               		</tr>
               			</table>
					</div>
				</td>
           	</tr>
	        <tr>
	        	<th align="center" valign="middle">质安<br> 整改</th>
	               <td>
	               	<div class="test">
	               		<table width="100%" border="0">
	               			<tr>
	               				<td width="15%">接到整改单数：<span id="ZGTZ_NUM" style="border-bottom: #000 solid 0px;"></span></td>
	               				<td width="12%">已回复：<span id="ZGHF_NUM" style="border-bottom: #000 solid 0px;"></span></td>
	               				<td width="12%">复查已通过：<span id="ZGFC_NUM" style="border-bottom: #000 solid 0px;"></span></td>
	               				<td width="12%">超期未回复：<span id="ZGCQ_NUM" style="border-bottom: #000 solid 0px;"></span></td>
	               				<td width="47%"></td>
	               			</tr>
	               		</table>
	                   	<table width="100%" border = "0">
	                       	<tr>
	                            <td width="24%" align="left" valign="top">涉及整改单最多的项目/标段名称(TOP5):</td>
				                <td valign="top" width="38%">
				                    <table width="100%" border="0" class="table1" cellspacing="0" cellpadding="0" type="single" noPage="true" pageNum="1000" id="zgxm_xm_top">
					                    <tr style="border-bottom: #ccc solid 0px;">
							                <th  bzfieldname="XMMC" width="80%"><div style="text-align: right;"></div></th>
							                <th  bzfieldname="NUM_XM" width="20%"><div style="text-align: left;"></div></th>
					                    </tr>
				                	</table>
				                </td>
				                <td valign="top" width="38%">
				                    <table width="100%" border="0" class="table1" cellspacing="0" cellpadding="0" type="single" noPage="true" pageNum="1000" id="zgxm_bd_top">
					                    <tr style="border-bottom: #ccc solid 0px;">
							                <th  bzfieldname="BDMC" width="80%"><div style="text-align: right;"></div></th>
							                <th  bzfieldname="NUM_BD" width="20%"><div style="text-align: left;"></div></th>
					                    </tr>
				                	</table>
				                </td>
                       		</tr>
	                   	</table>
	               	</div>
	        	</td>
	    	</tr>
		</table>
	</div>
</div>
<form method="post" id="queryForm">
</form>
<form method="post" id="queryForm_zb">
	<%-- <input type="hidden" fieldname="zb.XMGLGS" name="XMGLGS" keep="true" value='<%=deptId%>' operation="="/> --%>
    <input style="display:none;" id="KSSJ_zb" type="date" fieldtype="date" name="KSSJ" fieldname="KSSJ" fieldformat="yyyyMMdd" operation="<="/>
	 <input style="display:none;" id="JSSJ_zb" type="date" fieldtype="date" name="JSSJ" fieldname="JSSJ" fieldformat="yyyyMMdd" operation=">="/>
</form>
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 		<!--系统保留定义区域-->
       	<input type="hidden" name="queryXML" id = "queryXML">
      	<input type="hidden" name="txtXML" id = "txtXML">
      	<input type="hidden" name="txtFilter" order="asc" fieldname="t.xmbh,t.xmbs,t.pxh ,t.bdbh"/>
       	<input type="hidden" name="resultXML" id = "resultXML">
     	<!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
        <input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
</div>	
</body>
</html>