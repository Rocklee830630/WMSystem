<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>工程洽商-工程洽商管理</title>
<%
	String nd = request.getParameter("nd");
	String flag = request.getParameter("flag");
	String xmglgs = request.getParameter("xmglgs");
	if(Pub.empty(xmglgs))
	{
		xmglgs="";
	}	
%>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/gcbgk/gcbgkController.do";
var flag=<%=flag%>;
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
		 if(exportRequireQuery($("#gcqsList"))){//该方法需传入表格的jquery对象
		      printTabList("gcqsList");
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
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcqsList);
	//调用ajax插入
	if(flag==1)
	{
		defaultJson.doQueryJsonList(controllername+"?gcqs_zq_nd&nd=<%=nd%>&xmglgs=<%=xmglgs%>",data,gcqsList);
	}
	else
	{
		defaultJson.doQueryJsonList(controllername+"?gcqs_zq_by&nd=<%=nd%>&xmglgs=<%=xmglgs%>",data,gcqsList);
	}	
	
	
}
/* //项目名称自动模糊查询参数
function getXmmcQueryCondition(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcqsList);
	return data;
} */
//点击行事件
function tr_click(obj){
	
}
//详细信息
function rowView(index){
	var obj = $("#gcqsList").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).XMID;
	$(window).manhuaDialog(xmscUrl(id));
}
//标段图标样式
function doBdmc(obj){
	if(obj.XMBS == "0"){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}



//标段编号图标样式
function doBdbh(obj){
	if(obj.BDID == ''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDBH;
	}
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">工程洽商
      	<span class="pull-right">
      		<button id="btnExpExcel" class="btn"  type="button">导出</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <!-- <table class="B-table" width="100%">
      可以再此处加入hidden域作为过滤条件
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<input type="text" id="XMGLGS" name="XMGLGS" fieldname="XMGLGS" keep="true" operation="=" />
			</TD>
        </TR>
        可以再此处加入hidden域作为过滤条件
        <tr>
          <th width="5%" class="right-border bottom-border text-right">年度</th>
          <td class="right-border bottom-border" width="6%">
            <select class="span12 year" id="qnd" name = "QND" defaultMemo="全部" fieldname = "t.ND" operation="=" kind="dic" src="T#GC_GCGL_GCQS:distinct ND:ND:SFYX='1'">
            </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">项目名称</th>
          <td class="right-border bottom-border" width="20%"><input
			class="span12" type="text" placeholder="" name="QXMMC"
			fieldname="t.XMMC" operation="like" id="QXMMC"
			autocomplete="off" tablePrefix="t" tableName="GC_GCGL_GCQS"></td>
          <td class="text-left bottom-border text-right">
           <button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
           <button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
          </td>
         </tr>
      </table> -->
      </form>
	<div style="height:5px;"> </div>
		<div class="overFlowX">
			<table class="table-hover table-activeTd B-table" id="gcqsList" width="100%" type="single" pageNum="10">
				<thead>
					<tr>
						<th  name="XH" id="_XH" >&nbsp;#&nbsp;</th>
						<!-- <th fieldname="EVENTSJBH" tdalign="center">&nbsp;当前状态&nbsp;</th> -->
						<th fieldname="BH" hasLink="true" linkFunction="rowView" >&nbsp;项目编号&nbsp;</th>
						<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
						<th fieldname="BDID" CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
						<th fieldname="BDMC" maxlength="15" CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
						<th fieldname="XMGLGS" tdalign="center">&nbsp;项目管理公司&nbsp;</th>
						<th fieldname="XMJL" tdalign="center">&nbsp;项目经理&nbsp;</th>
						<th fieldname="SJDW" maxlength="15">&nbsp;设计单位&nbsp;</th>
						<th fieldname="JLDW" maxlength="15">&nbsp;监理单位&nbsp;</th>
						<th fieldname="SGDW" maxlength="15">&nbsp;施工单位&nbsp;</th>
						<th fieldname="QSTCRQ" tdalign="center">&nbsp;洽商提出日期&nbsp;</th>
					</tr>
				</thead>
				<tbody> </tbody>
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
</div>
</body>
</html>