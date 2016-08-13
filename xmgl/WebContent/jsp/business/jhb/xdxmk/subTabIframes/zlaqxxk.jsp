<!DOCTYPE html>
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
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<app:base/>
<title>项目信息卡</title>
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
	
	Date date=new Date();//获取时间
	SimpleDateFormat d=new SimpleDateFormat("yyyy-MM-dd");//转换格式
	String today=d.format(date);

	/* SimpleDateFormat y=new SimpleDateFormat("yyyy");//转换格式
	String year=y.format(date); */
	
	sql="select xmmc from GC_TCJH_XMXDK  where GC_TCJH_XMXDK_ID='"+id+"'";
	String[][] xmmcArray = DBUtil.query(conn, sql);
	String xmmc=xmmcArray[0][0];
	String 	zgfc_num_aq=""	,zg_sum_aq="",zgfc_num=""	,zg_sum ="";


	String ndsql="SELECT nd from gc_tcjh_xmxdk WHERE gc_tcjh_xmxdk_id='"+id+"' AND SFYX = '1'";
	String[][] nd = DBUtil.query(conn, ndsql);
	String year=nd[0][0];

%>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername_jc= "${pageContext.request.contextPath }/zlaq/jcxxController.do";
var id = "<%=id%>";
var bmid = '<%=deptId%>';
//页面初始化
$(function() {
	query();

	setPageHeight();
});


//质量安全部查询
function query()
{
	var data=combineQuery.getQueryCombineData(queryForm_jc,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername_jc+"?query_jc_xxk&jclx=zjbs='1'&id=<%=id%>",data,DT1,"callbackZl",true);
	var data=combineQuery.getQueryCombineData(queryForm_jc,frmPost,DT2);
	defaultJson.doQueryJsonList(controllername_jc+"?query_jc_xxk&jclx=ajbs='1'&id=<%=id%>",data,DT2,"callbackAq",true);
}

function callbackZl(){
	var zljcxx=$("#DT1 tbody tr").length;
	if(zljcxx != "0"){
		$("#zljcxx").html("("+zljcxx+")");
	}
}

function callbackAq(){
	var aqjcxx=$("#DT2 tbody tr").length;
	if(zljcxx != "0"){
		$("#aqjcxx").html("("+aqjcxx+")");
	}
}

//计算本页表格高度
function setPageHeight(){
	num_zl=$("#zl_tbody").height()/pageTableOne;
	if(num_zl>10)
	{
		num_zl=10;
	}
	height=$("#zl_div").height()-$("#zl_tbody").height()
	$("#zl_div").height(pageTableOne*num_zl+height);
	
	
	num_aq=$("#aq_tbody").height()/pageTableOne;
	if(num_aq>10)
	{
		num_aq=10;
	}
	height=$("#aq_div").height()-$("#aq_tbody").height()
	$("#aq_div").height(pageTableOne*num_aq+height);
	
}	
//判断标段是否有值
function pdbd(obj){
	var bdmc = obj.BDMC;
	if(bdmc=='undefinde'||bdmc=='')
	{
		return '<div style="text-align:center">—</div>'	;
	}	
}


//状态提示日期
function doZg(obj){
	var zg = obj.ISCZWT;
	if(zg==1)
	{
		return  '<i class="icon-ok" title=需要整改></i>';	
	}	
	else
	{
		return  '<i title=不需要整改>—</i>';	
	}	
}


//状态提示日期
 function doZt(obj){
 	var zt = obj.ZT;
 	switch(zt)
 	{
 		case '0':
 			zt = '<i title=无需整改>—</i>';	
 			break;
 		case '1':
 			zt = '<span class="label background-color: gray;" title=未发送整改通知>未发送</span>';	
 			break;
 		case '2':
 			if('<%=today%>'>obj.XGRQ)
 			{
 				zt = '<span class="label label-important-orange" title=发送日期："'+obj.TZRQ+'"&nbsp;&nbsp;限改超期：'+DateDiff('<%=today%>',obj.XGRQ)+'天>已发送</span>';	
 			}
 			else
 			{
 				zt = '<span class="label background-color: gray;" title=发送日期："'+obj.TZRQ+'"&nbsp;&nbsp;限改剩余：'+DateDiff(obj.XGRQ,'<%=today%>')+'天>已发送</span>';
 	
 			}
 			break;
 		case '3':
 			if('<%=today%>'>obj.XGRQ)
 			{
 				zt = '<span class="label label-important-orange" title=发送日期："'+obj.TZRQ+'"&nbsp;&nbsp;限改超期：'+DateDiff('<%=today%>',obj.XGRQ)+'天>回复中</span>';	
 			}
 			else
 			{
 				zt = '<span class="label background-color: gray;" title=发送日期："'+obj.TZRQ+'"&nbsp;&nbsp;限改剩余：'+DateDiff(obj.XGRQ,'<%=today%>')+'天>回复中</span>';
 			}
 			break;
 		case '4':
 			zt = '<span class="label background-color: gray;" title=回复日期："'+obj.HFRQ+'">待复查</span>';				
 			break;
 		case '5':
 			zt = '<span class="label background-color: gray;"title=复查日期："'+obj.FCRQ+'">已复查</span>';
 			break;
 	}
 	return zt;
 }


//复查结论
function doJl(obj)
{
	var zg = obj.ISCZWT;
	if(zg==1)
	{
	 	var jl = obj.FCJL;
	 	switch(jl)
	 	{
		 	case '1':
				jl = '<span class="label" style=" background-color: green;">已通过</span>';
			break;
		 	case '2':
				jl = '<span class="label label-important">未通过</span>';
			break;
		 	default:
		 		jl = '<i title=未复查暂无结论>—</i>';
		 	break;
		}	
	}
	else
	{
		jl='<i title=不需要整改>—</i>';	
	}	
	
	 return jl;
}


//回复日期
function hfrq(obj)
{
	var zg = obj.ISCZWT;
	if(zg==1)
	{
		if(obj.HFRQ==''||obj.HFRQ=='undefinde')
		{
			return '<i title=暂未回复>—</i>';
		}		
	}
	else
	{
		return  '<i title=不需要整改>—</i>';	
	}	
}


//复查日期
function fcrq(obj)
{
	var zg = obj.ISCZWT;
	if(zg==1)
	{
		if(obj.FCRQ==''||obj.FCRQ=='undefinde')
		{
			return '<i title=暂未复查>—</i>';
		}		
	}
	else
	{
		return  '<i title=不需要整改>—</i>';	
	}	
}


//两个日期的差值(d1 - d2).
function DateDiff(d1,d2)
{
	var day = 24 * 60 * 60 *1000;
	try{   
	     var dateArr = d1.split("-");
	     var checkDate = new Date();
	     checkDate.setFullYear(parseInt(dateArr[0]),(parseInt(dateArr[1])-1),parseInt(dateArr[2]));
	     var checkTime = checkDate.getTime();
	     var dateArr2 = d2.split("-");
	     var checkDate2 = new Date();
	     checkDate2.setFullYear(parseInt(dateArr2[0]),(parseInt(dateArr2[1])-1),parseInt(dateArr2[2]));
	     var checkTime2 = checkDate2.getTime();
	     var cha = (checkTime - checkTime2)/day; 
	     cha=Math.round(cha); 
	     return cha;
	}
	catch(e)
	{
   	return false;
	}
}


//饼图
function bt_jc(value1,value2,value3)
{
	var resultPie=getFusionChartsJsonStringByResult(value1,value2);
	var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "myChartId3", "100%", "100%");  
	myChart.setJSONData(resultPie);  
	myChart.render(value3); 
}


//饼图属性
function getFusionChartsJsonStringByResult(value1,value2){
	var jsonString = "";
	var resultObj = new Object();
	//chart属性--------------------开始
	var chartObj = new Object();
	chartObj.pieRadius = "70";
	chartObj.palette = "4";
	chartObj.decimals = "0";
	chartObj.enableSmartLabels = "1";
	chartObj.enableRotation = "1";
	chartObj.bgColor = "#FFFFFF";
	chartObj.bgAngle = "360";
	chartObj.showBorder = "0";
	chartObj.startingAngle = "70";
	chartObj.baseFont = "Arial";
	chartObj.baseFontSize = "12";
	chartObj.xAxisName = "用户";
	chartObj.showFCMenuItem = "0";
	chartObj.formatNumberScale='0'; 
	chartObj.use3DLighting = "0" ;
	chartObj.showShadow = "0" ;

	var dataArray = new Array();

	var labelObj = new Object();
	labelObj.label ='已完成整改'//resultobj.response.data[i].LABEL;
	labelObj.value = value1;//resultobj.response.data[0].ZG_WC;
	labelObj.color='#19bc9c' ;
	dataArray.push(labelObj);
	
	var labelObj1 = new Object();
	labelObj1.label ='未完成整改'//resultobj.response.data[i].LABEL;
	labelObj1.value = value2;//resultobj.response.data[0].ZG_SUM-labelObj.value;
	labelObj1.color='#DDDDDD' ;
	dataArray.push(labelObj1);
				
	//data属性---------------------结束
	//向obj中加入chart属性和data属性
	resultObj.chart = chartObj;
	resultObj.data = dataArray;
	//obj对象转string
	jsonString = JSON.stringify(resultObj);
	return jsonString;
}


//操作图标
function cztb(obj)
{
	var id,zt;
	id=obj.GC_ZLAQ_JCB_ID;
	zt=obj.ZT;
	var jsonstr = JSON.stringify(obj);
	$("#resultXML").val(jsonstr);
	return '<a href="javascript:void(0);"><i title="综合信息" class="icon-file showXmxxkInfo" onclick="xxk(\''+id+'\',\''+zt+'\')"></i></a>';
}
//信息卡
function xxk(id,zt)
{
	$(window).manhuaDialog({"title":"质量安全>综合信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zlaq/zhxx.jsp?jcbid="+id+"&zt="+zt+"&flag=1","modal":"1"});
}
</script>      
    
</head>
<body>
<app:dialogs/>
<div class="row-fluid">
	<h4 class="title" style="font-size:16px">
		整改情况统计
	</h4>
	<table width="100%">
		<!-- <tr>
			<td width="50%"><div class="span6" style = " margin-left:250px;height:45px;">质量检查</div></td>
			<td width="50%" align="center"><div class="span6" style = "height:45px;"  align="center">安全检查</div></td>						
		</tr> -->
		<tr>
			<td width="50%">
				 <div class="span2">
					<fieldset class="b_ddd" style ="text-align:center;margin-left:100px;border: 0px solid #ccc;">
						<legend style="font-weight:bold;font-size: 14px;font-family: SimYou,Microsoft YaHei,Helvetica Neue, Helvetica, Arial, sans-serif;">质量检查</legend>
						<div class="span6" style = "width:355px;height:245px;" id="chartdiv1"></div> 
					</fieldset>	
				</div>
			</td>
			<td width="50%" align="center">
				<div class="span2">
					<fieldset class="b_ddd" style ="text-align:center;margin-left:100px;border: 0px solid #ccc;">
						<legend style ="font-weight:bold;margin-left:-200px;font-size: 14px;font-family: SimYou,Microsoft YaHei,Helvetica Neue, Helvetica, Arial, sans-serif;">安全检查</legend>
	       				<div class="span6" style = "width:355px;height:245px;" id="chartdiv2" align="center"></div>         
					</fieldset>
				</div>	
			</td>				
		</tr>
		<tr>
			<td width="100%" colspan="2" style="vertical-align:top;">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						质量检查信息<span id="zljcxx"></span>
						<br>
						<font style="font-size: 14px;font-family: SimYou,Microsoft YaHei,Helvetica Neue, Helvetica, Arial, sans-serif;">
							项目名称：<%=xmmc%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							质量检查&nbsp;
							<font>
			                 <%
				     			sbSql = new StringBuffer();
				         		sbSql.append("SELECT COUNT(GC_ZLAQ_JCB_ID)AS jczl_num FROM GC_ZLAQ_JCB WHERE  ZJBS='1' and xdkid='"+id+"' and ND ='"+year+"' AND SFYX = '1'");
				         		//modify by zhangbr@ccthanking.com 去掉年度查询条件
				         		//sbSql.append("SELECT COUNT(GC_ZLAQ_JCB_ID)AS jczl_num FROM GC_ZLAQ_JCB WHERE  ZJBS='1' and xdkid='"+id+"' AND SFYX = '1'");
				         		sql = sbSql.toString();
				         		String[][] jczl_numArray = DBUtil.query(conn, sql);
				         		if(null !=jczl_numArray && jczl_numArray.length>0 && !Pub.empty(jczl_numArray[0][0])){
				         			String jczl_num = jczl_numArray[0][0].toString();
				 	        		out.println(jczl_num);
				 	         	}
			                 %> 
			                 </font>          
							&nbsp;次
							&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
							需整改&nbsp;
							<font>
			                 <%
				     			sbSql = new StringBuffer();
			                    sbSql.append(" select count(gc_zlaq_jcb_id) from GC_ZLAQ_JCB jcb left join "+
			                    			"( select jcbid from (select  jcbid,row_number() OVER(PARTITION BY jcbid ORDER BY lxbs desc) as row_flg from GC_ZLAQ_ZGB t  where sfyx = '1') temp where temp.row_flg = '1') zgb "+
			                    		 	" on gc_zlaq_jcb_id = jcbid where ND = '"+year+"'and zjbs = '1' and isczwt = 1 and xdkid = '"+id+"' and jcb.sfyx = '1'" ); 
								//modify by zhangbr@ccthanking.com 去掉年度查询条件
			                   /* 	sbSql.append(" select count(gc_zlaq_jcb_id) from GC_ZLAQ_JCB jcb left join "+
			                    			"( select jcbid from (select  jcbid,row_number() OVER(PARTITION BY jcbid ORDER BY lxbs desc) as row_flg from GC_ZLAQ_ZGB t  where sfyx = '1') temp where temp.row_flg = '1') zgb "+
			                    		 	" on gc_zlaq_jcb_id = jcbid where zjbs = '1' and isczwt = 1 and xdkid = '"+id+"' and jcb.sfyx = '1'" );  */
				         		sql = sbSql.toString();
				         		String[][] zg_sumArray = DBUtil.query(conn, sql);
				         		if(null !=zg_sumArray && zg_sumArray.length>0 && !Pub.empty(zg_sumArray[0][0])){
				         			zg_sum = zg_sumArray[0][0].toString();
				 	        		out.println(zg_sum);
				 	         	}
			                 %>
			                 </font>
							&nbsp;次
							&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							已完整改&nbsp;
							<font>
			                 <%
				     			sbSql = new StringBuffer();
				         		sbSql.append("select COUNT(GC_ZLAQ_JCB_ID) from GC_ZLAQ_JCB jcb left join GC_ZLAQ_ZGB zgb on jcb.gc_zlaq_jcb_id=zgb.jcbid WHERE ZT=5 and ZJBS='1' and xdkid='"+id+"' and fcjl=1 and zgb.sfyx=1 and jcb.sfyx=1  and ND ='"+year+"'");
				         		//modify by zhangbr@ccthanking.com 去掉年度查询条件
				         		//sbSql.append("select COUNT(GC_ZLAQ_JCB_ID) from GC_ZLAQ_JCB jcb left join GC_ZLAQ_ZGB zgb on jcb.gc_zlaq_jcb_id=zgb.jcbid WHERE ZT=5 and ZJBS='1' and xdkid='"+id+"' and fcjl=1 and zgb.sfyx='1' and jcb.sfyx='1' ");
				         		sql = sbSql.toString();
				         		String[][] zgfc_numArray = DBUtil.query(conn, sql);
				         		if(null !=zgfc_numArray && zgfc_numArray.length>0 && !Pub.empty(zgfc_numArray[0][0])){
				         			zgfc_num = zgfc_numArray[0][0].toString();
				 	        		out.println(zgfc_num);
				 	         	}
			                 %>
			                 </font>
								&nbsp;次
						</font>
					</h4>
					<div style="height:30px;"> </div>								
					<div id=""> 
					<div class="overFlowX">
			            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" noPage="true" pageNum="1000">
			                <thead>
			                    <tr>
			                     	<th id="_XH" name="XH" tdalign="center">&nbsp;#&nbsp;</th>
			                     	<th fieldname="GC_ZLAQ_JCB_ID" tdalign="center" noprint="true" CustomFunction="cztb"></th>
									<th fieldname="BDMC" CustomFunction="pdbd">&nbsp;标段名称&nbsp;</th>
									<th fieldname="ZT" tdalign="center" CustomFunction="doZt" noprint="true">&nbsp;整改状态&nbsp;</th>						
									<th fieldname="JCRQ" tdalign="center">&nbsp;检查日期&nbsp;</th>
									<th fieldname="JCGM" tdalign="center">&nbsp;检查方式&nbsp;</th>
									<th fieldname="JCNR"  maxlength="15" >&nbsp;检查内容&nbsp;</th>
									<th fieldname="ISCZWT" tdalign="center" CustomFunction="doZg" noprint="true">&nbsp;需要整改&nbsp;</th>
									<th fieldname="HFRQ" tdalign="center" CustomFunction="hfrq">&nbsp;整改回复日期&nbsp;</th>
									<th fieldname="FCRQ" tdalign="center" CustomFunction="fcrq">&nbsp;复查日期&nbsp;</th>
									<th fieldname="FCJL" tdalign="center" CustomFunction="doJl">&nbsp;复查结论&nbsp;</th>
			                    </tr>
			                </thead> 
			              	<tbody id="zl_tbody"></tbody>
			           </table>
			           </div>
			       </div>     
				</div>
			</td>
		</tr>
		<tr>
			<td width="100%" colspan="2">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						安全检查信息<span id="aqjcxx"></span>
						<br>
						<font style="font-size: 14px;font-family: SimYou,Microsoft YaHei,Helvetica Neue, Helvetica, Arial, sans-serif;">
							项目名称：<%=xmmc%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							安全检查&nbsp;
							<font>
			                 <%
				     			sbSql = new StringBuffer();
				         		sbSql.append("SELECT COUNT(GC_ZLAQ_JCB_ID)AS jcaq_num FROM GC_ZLAQ_JCB WHERE  AJBS='1' and xdkid='"+id+"' and ND ='"+year+"' AND SFYX = '1'");
				         		//modify by zhangbr@ccthanking.com 去掉年度查询条件
				         		//sbSql.append("SELECT COUNT(GC_ZLAQ_JCB_ID)AS jcaq_num FROM GC_ZLAQ_JCB WHERE  AJBS='1' and xdkid='"+id+"'  AND SFYX = '1'");
				         		sql = sbSql.toString();
				         		String[][] jcaq_numArray = DBUtil.query(conn, sql);
				         		if(null !=jcaq_numArray && jcaq_numArray.length>0 && !Pub.empty(jcaq_numArray[0][0])){
				         			String jcaq_num = jcaq_numArray[0][0].toString();
				 	        		out.println(jcaq_num);
				 	         	}
			                 %>
			                 </font>           
							&nbsp;次
							&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							需整改&nbsp;
							<font>
			                 <%
				     			sbSql = new StringBuffer();
			                    sbSql.append(" select count(gc_zlaq_jcb_id) from GC_ZLAQ_JCB jcb left join "+
		                    			"( select jcbid from (select  jcbid,row_number() OVER(PARTITION BY jcbid ORDER BY lxbs desc) as row_flg from GC_ZLAQ_ZGB t  where sfyx = '1') temp where temp.row_flg = '1') zgb "+
		                    		 	" on gc_zlaq_jcb_id = jcbid where ND = '"+year+"'and ajbs = '1' and isczwt = 1 and xdkid = '"+id+"' and jcb.sfyx = '1'" ); 
				         		//modify by zhangbr@ccthanking.com 去掉年度查询条件
				         		/* sbSql.append(" select count(gc_zlaq_jcb_id) from GC_ZLAQ_JCB jcb left join "+
		                    			"( select jcbid from (select  jcbid,row_number() OVER(PARTITION BY jcbid ORDER BY lxbs desc) as row_flg from GC_ZLAQ_ZGB t  where sfyx = '1') temp where temp.row_flg = '1') zgb "+
		                    		 	" on gc_zlaq_jcb_id = jcbid where  ajbs = '1' and isczwt = 1 and xdkid = '"+id+"' and jcb.sfyx = '1'" );  */
				         		sql = sbSql.toString();
				         		String[][] zg_sumArray_aq = DBUtil.query(conn, sql);
				         		if(null !=zg_sumArray_aq && zg_sumArray_aq.length>0 && !Pub.empty(zg_sumArray_aq[0][0])){
				         			zg_sum_aq = zg_sumArray_aq[0][0].toString();
				 	        		out.println(zg_sum_aq);
				 	         	}
			                 %>
			                 </font>
							&nbsp;次
							&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							已完整改
							&nbsp;
							<font>
			                 <%
				     			sbSql = new StringBuffer();
				         		sbSql.append("select COUNT(GC_ZLAQ_JCB_ID) from GC_ZLAQ_JCB jcb left join GC_ZLAQ_ZGB zgb on jcb.gc_zlaq_jcb_id=zgb.jcbid WHERE ZT=5 and ajbs='1' and fcjl=1 and xdkid='"+id+"' and zgb.sfyx=1 and jcb.sfyx=1  and ND ='"+year+"'");
				         		//modify by zhangbr@ccthanking.com 去掉年度查询条件
				         		//sbSql.append("select COUNT(GC_ZLAQ_JCB_ID) from GC_ZLAQ_JCB jcb left join GC_ZLAQ_ZGB zgb on jcb.gc_zlaq_jcb_id=zgb.jcbid WHERE ZT=5 and ajbs='1' and fcjl=1 and xdkid='"+id+"' and zgb.sfyx='1' and jcb.sfyx='1'");
				         		sql = sbSql.toString();
				         		String[][] zgfc_numArray_aq = DBUtil.query(conn, sql);
				         		if(null !=zgfc_numArray_aq && zgfc_numArray_aq.length>0 && !Pub.empty(zgfc_numArray_aq[0][0])){
				         			zgfc_num_aq = zgfc_numArray_aq[0][0].toString();
				 	        		out.println(zgfc_num_aq);
				 	         	}
			                 %>
							</font>
								&nbsp;次
						</font>
					</h4>
					<div style="height:30px;"> </div>								
					<div id="aq_div"> 
			            <table width="100%" class="table-hover table-activeTd B-table" id="DT2" type="single" noPage="true" pageNum="1000">
			                <thead>
			                    <tr>
			                     	<th id="_XH" name="XH" tdalign="center">&nbsp;#&nbsp;</th>
			                     	<th fieldname="GC_ZLAQ_JCB_ID" tdalign="center" noprint="true" CustomFunction="cztb"></th>
									<th fieldname="BDMC" CustomFunction="pdbd">&nbsp;标段名称&nbsp;</th>
									<th fieldname="ZT" tdalign="center" CustomFunction="doZt" noprint="true">&nbsp;整改状态&nbsp;</th>						
									<th fieldname="JCRQ" tdalign="center">&nbsp;检查日期&nbsp;</th>
									<th fieldname="JCGM" tdalign="center">&nbsp;检查方式&nbsp;</th>
									<th fieldname="JCNR"  maxlength="15">&nbsp;检查内容&nbsp;</th>
									<th fieldname="ISCZWT" tdalign="center" CustomFunction="doZg" noprint="true">&nbsp;需要整改&nbsp;</th>
									<th fieldname="HFRQ" tdalign="center" CustomFunction="hfrq">&nbsp;整改回复日期&nbsp;</th>
									<th fieldname="FCRQ" tdalign="center" CustomFunction="fcrq">&nbsp;复查日期&nbsp;</th>
									<th fieldname="FCJL" tdalign="center" CustomFunction="doJl">&nbsp;复查结论&nbsp;</th>
			                    </tr>
			                </thead> 
			              	<tbody id="aq_tbody"></tbody>
			           </table>
			    	</div>
				</div> 
			</td>
		</tr>
	</table>
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
    <form method="post" id="queryForm_jc"></form>
</div>

<script type="text/javascript">
<%
	int zgwfc_zl_sum=Integer.parseInt(zg_sum)-Integer.parseInt(zgfc_num);
	int zgwfc_aq_sum=Integer.parseInt(zg_sum_aq)-Integer.parseInt(zgfc_num_aq);
%>
bt_jc("<%=zgfc_num%>","<%=zgwfc_zl_sum%>","chartdiv1");	
bt_jc("<%=zgfc_num_aq%>","<%=zgwfc_aq_sum%>","chartdiv2");

</script>
</body>
</html>