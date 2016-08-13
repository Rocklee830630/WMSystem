<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<app:base/>
<title>工程洽商-工程洽商管理</title>
<%
	String nd = request.getParameter("nd");
	String sqlname= request.getParameter("sqlname");
	String xmglgs= request.getParameter("xmglgs");
	if(Pub.empty(xmglgs))
	{
		xmglgs="";
	}	
	Date d=new Date();//获取时间
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//转换格式
	String today=sdf.format(d);

	//String flag = request.getParameter("flag");
%>
<%
%>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zlaqgk/zlaqgkController.do";
<%-- var flag=<%=flag%>; --%>

//计算本页表格分页数
function setPageHeight() {
	var xHeight = parent.document.documentElement.clientHeight;
	var height = xHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(Math.abs(height)/pageTableOne,10);
	$("#DT1").attr("pageNum", pageNum);
}

//页面初始化
$(function() {
	
	setPageHeight();
	
	//自动完成项目名称模糊查询
	//showAutoComplete("QXMMC",controllername+"?xmmcAutoCompleteByYw","getXmmcQueryCondition"); 
	init();
	//按钮绑定事件（查询）
	/* $("#btnQuery").click(function() {
		queryList();
	}); */
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
		      printTabList("DT1");
		  }
	});
	
	/* //按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });	 */
	
});
//页面默认参数
function init(){
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}


function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入	
	defaultJson.doQueryJsonList(controllername+"?zlaq_gk&nd=<%=nd%>&sqlname=<%=sqlname%>&xmglgs=<%=xmglgs%>",data,DT1);
}
/* //项目名称自动模糊查询参数
function getXmmcQueryCondition(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	return data;
} */
//点击行事件
function tr_click(obj){
	
}
//详细信息
function rowView(index){
	var obj = $("#DT1").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).XDKID;
	$(window).manhuaDialog(xmscUrl(id));
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
			zt = '<span class="label background-color: gray;" title=未发送整改通知>通知未发送</span>';	
			break;
		case '2':
			if('<%=today%>'>obj.XGRQ)
			{
				zt = '<span class="label label-important-orange" title=发送日期："'+obj.TZRQ+'"&nbsp;&nbsp;当前状态：已发送>限改超期'+DateDiff('<%=today%>',obj.XGRQ)+'天</span>';	
			}
			else
			{
				zt = '<span class="label background-color: gray;" title=发送日期："'+obj.TZRQ+'"&nbsp;&nbsp;当前状态：已发送>限改剩余'+DateDiff(obj.XGRQ,'<%=today%>')+'天</span>';
	
			}
			break;
		case '3':
			if('<%=today%>'>obj.XGRQ)
			{
				zt = '<span class="label label-important-orange" title=发送日期："'+obj.TZRQ+'"&nbsp;&nbsp;当前状态：回复中>限改超期'+DateDiff('<%=today%>',obj.XGRQ)+'天</span>';	
			}
			else
			{
				zt = '<span class="label background-color: gray;" title=发送日期："'+obj.TZRQ+'"&nbsp;&nbsp;当前状态：回复中>限改剩余'+DateDiff(obj.XGRQ,'<%=today%>')+'天</span>';
			}
			break;
		case '4':
			zt = '<span class="label background-color: gray;" title=回复日期："'+obj.HFRQ+'">回复待复查</span>';				
			break;
		case '5':
			if(obj.FCJL==1)
			{
				zt = '<span class="label background-color: gray;" title=复查日期："'+obj.FCRQ+'">复查已通过</span>';
			}	
			else
			{
				zt = '<span class="label label-important" title=复查日期："'+obj.FCRQ+'">复查未通过</span>';
			}	
			break;
	}
	return zt;
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


/* //操作图标
function cztb(obj)
{
	var id,zt,json;
	id=obj.GC_ZLAQ_JCB_ID;
	zt=obj.ZT;
	json=JSON.stringify(obj);
	json=encodeURI(json);  
	$("#resultXML_form").append('<input class=\"span12\" type=\"hidden\" name=\"resultXML\"  form =\"resultXML_form" value=\"'+json+'\" id=\"'+id+'\"/>');
	return '<a href="javascript:void(0);"><i title="综合信息" class="icon-file showXmxxkInfo" onclick="xxk(\''+id+'\',\''+zt+'\')"></i></a>';
}


//信息卡
function xxk(id,zt)
{
	$(window).manhuaDialog({"title":"质量安全>综合信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zlaq/zhxx.jsp?jcbid="+id+"&zt="+zt+"&flag=1","modal":"1"});
} */


//标段图标样式
function doBdmc(obj){
	if(obj.BDID == ''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}



//标段编号图标样式
function doBdbh(obj){
	if(obj.BDID==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDID_SV;
	}
}

//操作图标
function cztb(obj)
{
	var id,zt,json;
	id=obj.GC_ZLAQ_JCB_ID;
	zt=obj.ZT;
	json=JSON.stringify(obj);
	//json=encodeURI(json); 
	$("#resultXML").val(json);
	$("#resultXML_form").append('<input class=\"span12\" type=\"hidden\" name=\"resultXML\"  form =\"resultXML_form" value=\"'+json+'\" id=\"'+id+'\"/>');
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
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">质量安全
      	<span class="pull-right">
      		<button id="btnExpExcel" class="btn"  type="button">导出</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"></form>
	<div style="height:5px;"> </div>
		<div class="overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
                <thead>
                    <tr>
                     	<th id="_XH" name="XH" tdalign="center">&nbsp;#&nbsp;</th>
                     	<th fieldname="GC_ZLAQ_JCB_ID" tdalign="center" noprint="true" CustomFunction="cztb"></th>
						<th fieldname="ISCZWT" tdalign="center" CustomFunction="doZg" noprint="true">&nbsp;需要整改&nbsp;</th>
						<th fieldname="ZT" tdalign="center" CustomFunction="doZt" noprint="true">&nbsp;当前状态&nbsp;</th>
						<th fieldname="XMBH" hasLink="true" tdalign="center" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>								
						<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
						<th fieldname="BDID" maxlength="15" CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
						<th fieldname="BDMC" maxlength="15" CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
						<th fieldname="XMGLGS" tdalign="center">&nbsp;项管公司&nbsp;</th>
						<th fieldname="JCRQ">&nbsp;检查日期&nbsp;</th>
						<th fieldname="JCLX" tdalign="center">&nbsp;检查类型&nbsp;</th>
						<th fieldname="JCGM" tdalign="center">&nbsp;检查方式&nbsp;</th>
						<th fieldname="JCNR" maxlength="15">&nbsp;检查内容&nbsp;</th>
                    </tr>
                </thead> 
              	<tbody></tbody>
           </table>
		</div>
	</div>
</div>
    
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="DESC" fieldname = "t.LRSJ"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
	<FORM id="resultXML_form"></FORM>
</div>
</body>
</html>