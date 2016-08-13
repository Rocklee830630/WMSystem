<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>招投标合同部-中标信息</title>
<%
	String id = request.getParameter("id");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/cjdw/cjdwController.do";
var id = '<%=id%>';

function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#cjdwList").attr("pageNum",pageNum);
}


//页面初始化
$(function() {
	
	setPageHeight();
	$("#DSFJGID").val(id);
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        //其他处理放在下面
    });
	
});
//页面默认参数
function init(){
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}
//查询列表
function queryList(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,ztbList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryZtbList",data,ztbList,"queryZbtCallback",false);
}
//查询列表回调函数
function queryZbtCallback(){
	
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">中标信息
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<input type="text" id="DSFJGID" fieldname="Z.DSFJGID" keep="true" operation="="/>
				<input type="text" id="XQZT" fieldname="Z.XQZT" keep="true" value="2" operation="="/>
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        
      </table>
      </form>
   <div style="height:5px;"> </div>
	<table class="table-hover table-activeTd B-table" id="ztbList" width="100%" type="single" pageNum="10">
		<thead>
			<tr>
				<th  name="XH" id="_XH">&nbsp;#&nbsp;</th>
				<th fieldname="ZBBH" tdalign="center">&nbsp;招标编号&nbsp;</th>
				<th fieldname="ZTBMC">&nbsp;招投标名称&nbsp;</th>
				<th fieldname="ZBTZSBH" tdalign="center">&nbsp;中标通知书编号&nbsp;</th>
				<th fieldname="ZZBJ" tdalign="right">&nbsp;总中标价&nbsp;</th>
				<th fieldname="KBRQ" tdalign="center">&nbsp;开标日期&nbsp;</th>
				<th fieldname="HTMC">&nbsp;合同名称&nbsp;</th>
				<th fieldname="HTBM" tdalign="center">&nbsp;合同编码&nbsp;</th>
				<th fieldname="ZHTQDJ" tdalign="right">&nbsp;总合同签订价(元)&nbsp;</th>
			</tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
</div>
</div>
    
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "Z.LRSJ"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>