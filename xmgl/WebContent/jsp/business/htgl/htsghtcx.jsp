<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>项目选择</title>
<script type="text/javascript" charset="utf-8">
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtController.do";


//计算本页表格分页数
function setPageHeight(){
	var getHeight=getDivStyleHeight();
	var height = getHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#htList").attr("pageNum",pageNum);
}

function queryList(){
	g_bAlertWhenNoResult = false;
	setPageHeight();
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,htList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?querySgHt",data,htList);
	g_bAlertWhenNoResult = true;
}

//页面初始化
$(function() {
	initPage();
	queryList();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        //其他处理放在下面
        initPage();
    });
  	//按钮绑定事件（确定）
    $("#btnQd").click(function() {
    	if($("#htList").getSelectedRowIndex()==-1)
		 {
    		requireSelectedOneRow();
		    return;
		 }
        var rowValue = $("#htList").getSelectedRow();//获得选中行的json对象
        $(window).manhuaDialog.setData(rowValue);
        $(window).manhuaDialog.sendData();
        $(window).manhuaDialog.close();
    });
});

function initPage(){
	
	//initCommonQueyPage();
	$("#QHTZT").val("1");
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">

  <p></p>
  <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">项目信息
      <span class="pull-right">
      	<button id="btnQd" class="btn" type="button">确定</button>
      </span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
			<TD class="right-border bottom-border">
				<input type="hidden" id="QHTZT" name="HTZT"  fieldname="HTZT" operation="=" value="1"/>
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
         <tr>
			<td width="12%" class="right-border bottom-border">
				<select class="span12" type="date" id="QND" name="QDNF" fieldname="ghh.QDNF" operation="=" kind="dic" src="XMNF"  defaultMemo="-签订年份-">
			</td>
			<th width="5%" class="right-border bottom-border text-right">项目名称</th>
			<td class="right-border bottom-border" width="20%">
				<input class="span12" type="text" id="QXMMC" name="XMMC" fieldname="gjs.XMMC" operation="like" >
			</td>
			<th width="5%" class="right-border bottom-border text-right">合同名称</th>
			<td class="right-border bottom-border" width="20%">
				<input class="span12" type="text" id="QHTMC" name="HTMC" fieldname="ghh.HTMC" operation="like" >
			</td>
	          <td class="text-left bottom-border text-right">
	        	<button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
	        	<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
	          </td>
         </tr>
      </table>
      </form>
	<div style="height:5px;"> </div>
	<table width="100%" class="table-hover table-activeTd B-table" id="htList" type="single" pageNum="10">
	<thead>
		<tr>
			<th name="XH" id="_XH" style="width:10px">&nbsp;#&nbsp;</th>
			<th fieldname="XMBH" colindex=2 tdalign="center">&nbsp;项目编号&nbsp;</th>
			<th fieldname="XMMC" colindex=2 tdalign="center" maxlength="36">&nbsp;项目名称&nbsp;</th>
			<th fieldname="XMLX" colindex=2 tdalign="center">&nbsp;项目类型&nbsp;</th>
			<th fieldname="BDMC" colindex=3 tdalign="center" maxlength="36">&nbsp;标段名称&nbsp;</th>
			<th fieldname="HTBM" colindex=4 tdalign="center">&nbsp;合同编码&nbsp;</th>
			<th fieldname="HTMC" colindex=4 tdalign="center">&nbsp;合同名称&nbsp;</th>
			<th fieldname="QDNF" colindex=4 tdalign="center">&nbsp;签定年份&nbsp;</th>
			<th fieldname="SGDWMC" colindex=5 tdalign="center" maxlength="36">&nbsp;施工单位&nbsp;</th>
			<th fieldname="JLDWMC" colindex=5 tdalign="center" maxlength="36">&nbsp;监理单位&nbsp;</th>
			<th fieldname="HTQDJ" colindex=6 tdalign="center" maxlength="17">&nbsp;合同签订价(元)&nbsp;</th>
			<th fieldname="HTJQDRQ" colindex=7 tdalign="center">&nbsp;合同签订日期&nbsp;</th>
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
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "ghh.LRSJ"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 </FORM>
 </div>
</body>
</html>