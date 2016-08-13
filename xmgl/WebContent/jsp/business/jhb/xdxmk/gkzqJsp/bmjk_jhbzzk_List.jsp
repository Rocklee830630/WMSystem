<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>部门 监控-计划编制状况</title>
<%
	String nd = request.getParameter("nd");
	String index = request.getParameter("index");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/sjzqController.do";
var nd = '<%=nd%>';
var index = '<%=index%>';
//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	setPageHeight();
	init();
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
		      printTabList("DT1");
		  }
	});
});
//页面默认参数
function init(){
	$("#nd").val(nd);
	if(index == "1"){
		$("#jhpch").attr("operation","NOT LIKE");
		$("#jhpch").val("%零星%");
	}else if(index == "2"){
		$("#jhpch").attr("operation","LIKE");
		$("#jhpch").val("%零星%");
	}
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}

//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query_pcxx",data,DT1);
}

//详细信息
function rowView(index){
	var obj = $("#DT1").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).GC_JH_ZT_ID;
	$(window).manhuaDialog({"title":"统筹计划管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/gkzqJsp/bmjk_tcjh_List.jsp?tcjhztid="+id+"&nd="+nd});
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">下达批次信息
      	<span class="pull-right">
	  		<button id="btnExpExcel" class="btn"  type="button">导出</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<input type="text" fieldname="ND" id="nd" operation="="/>
				<input type="text" fieldname="SPZT" id="spzt" operation="=" value="3"/>
				<input type="text" fieldname="JHPCH" id="jhpch" operation=""/>
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
      </table>
      </form>
	<div style="height:5px;"> </div>
	<div class="overFlowX">
		<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" pageNum="10" editable="0">
		<thead>
			<tr>
				<th  name="XH" id="_XH" tdalign="center">&nbsp;#&nbsp;</th>
				<th fieldname="JHPCH" tdalign="center" hasLink="true" linkFunction="rowView">&nbsp;计划批次&nbsp;</th>
				<th fieldname="ND" tdalign="center">&nbsp;年度&nbsp;</th>
				<th fieldname="JHMC">&nbsp;计划名称&nbsp;</th>
				<th fieldname="ISXF" tdalign="center">&nbsp;是否下发&nbsp;</th>
				<th fieldname="MQBB"  tdalign="center">&nbsp;目前版本&nbsp;</th>
				<th fieldname="XFRQ"  tdalign="center">&nbsp;下发日期&nbsp;</th>
				<th fieldname="XDRQ"  tdalign="center">&nbsp;下达日期&nbsp;</th>
			</tr>
		</thead>
		<tbody></tbody>
		</table>
		</div>
	</div>
   </div>
 </div>
    
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="asc" fieldname = "JHPCH"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>