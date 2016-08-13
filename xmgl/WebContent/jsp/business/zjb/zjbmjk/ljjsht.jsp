<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>造价监控-累计结算合同</title>
<script type="text/javascript" charset="UTF-8">
<%
	String nd = request.getParameter("nd");
String zjhtlx = request.getParameter("zjhtlx");
%>
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjbmjkController.do";
var nd = "<%=nd%>";
var zjhtlx = "<%=zjhtlx%>";

$(function() {
	init();
});
//页面默认参数
function init() {
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}

//计算本页表格分页数
function setPageHeight() {
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
//	alert("g_iHeight:"+g_iHeight+"|pageTopHeight:"+pageTopHeight+"|pageTitle:"+pageTitle+"|pageQuery:"+pageQuery+"|pageNumHeight:"+pageNumHeight+"|getTableTh(1):"+getTableTh(1));
//	alert(height +"|" + pageTableOne + " | " + pageNum);
	$("#DT1").attr("pageNum", pageNum);
}

//查询列表
function queryList(){
	setPageHeight();
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryLjjsht&nd="+nd+"&zjhtlx="+zjhtlx,data,DT1);
}

function rowView1(t){
	if($("#DT1").getSelectedRowIndex()==-1)
	 {
		requireSelectedOneRow();
	    return
	 }
	$("#resultXML").val($("#DT1").getSelectedRow());
	var rowValue = $("#DT1").getSelectedRow();
	var tempJson = convertJson.string2json1(rowValue);
	var idvar = tempJson.ID;
	$(window).manhuaDialog({"title":"部门合同>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/ht-view.jsp?type=detail","modal":"1"});
}

//详细信息
function doRandering(obj){
	var showHtml = "";
	showHtml = "<a href='javascript:rowView1(this);' title='查看详细信息'><i class='icon-file showXmxxkInfo'></i></a>";
	return showHtml;
}

//按钮绑定事件（导出EXCEL）
$(function() {
	$("#btnExp").click(function() {
		 if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
		      printTabList("DT1");
		  }
	});
});


//状态颜色判断
function docolor(obj)
{
	var xqzt=obj.HTZT;
	if(xqzt=="-3"){
		return '<span class="label label-danger">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="2"){
	  	return '<span class="label label-danger">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="3"){
	  	return '<span class="label label-danger">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="4"){
	  	return '<span class="label label-danger">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="3"){
	  	return '<span class="label label-warning">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="1"){
	  	return '<span class="label label-success">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="0"){
	 	return '<span class="label label-success">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="-2"){
		if(obj.EVENTSJBH=="5" || obj.EVENTSJBH=="6" || obj.EVENTSJBH=="7"){
			return '<span class="label label-success">'+obj.EVENTSJBH_SV+'</span>';
		}else{
			return '<span class="label label-warning">'+obj.HTZT_SV+'</span>';
		}
	}else if(xqzt=="-1"){
	 	return '<span class="label label-danger">'+obj.HTZT_SV+'</span>';
	}else{
		return obj.HTZT_SV;
	}
	
}

function doSFDXM(obj){
	if(obj.SFDXMHT=='2'){
		return '否';	
	}else{
		return obj.SFDXMHT_SV;
	}
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">
      <span class="pull-right">
      	<button id="btnExp" class="btn"  type="button">导出</button>
      </span>
      </h4>
     <form method="post" id="queryForm">
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
		 		<input type="text" id="ND" fieldname="ND" name="ND" operation="="> 
			</TD>
        </TR>
      </table>
      </form>
 	<div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="DT1"  width="100%" editable="0" type="single">
		<thead>
			<tr>
           		<th name="XH" id="_XH" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
           		<th fieldname="ID" tdalign="center" colindex=2 CustomFunction="doRandering" noprint="true">&nbsp;&nbsp;</th>
           		<th fieldname="HTZT" colindex=3 tdalign="center" CustomFunction="docolor">&nbsp;合同状态&nbsp;</th>
				<th fieldname="HTBM" colindex=4 tdalign="left" maxlength="20">&nbsp;合同编码&nbsp;</th>
				<th fieldname="HTMC" colindex=5 tdalign="left" maxlength="30">&nbsp;合同名称&nbsp;</th>
				<th fieldname="HTLX" colindex=6 tdalign="center">&nbsp;合同类型&nbsp;</th>
				<th fieldname="SFXNHT" colindex=7 tdalign="center" >&nbsp;是否虚拟&nbsp;</th>
				<th fieldname="SFDXMHT" colindex=8 tdalign="center" CustomFunction="doSFDXM">&nbsp;单项目&nbsp;</th>
				<th fieldname="SFZFJTZ" colindex=9 tdalign="center" >&nbsp;支付即投资&nbsp;</th>
				<th fieldname="YFDW" colindex=10 tdalign="left" maxlength="36">&nbsp;乙方单位&nbsp;</th>
				<th fieldname="HTJQDRQ" colindex=11 tdalign="center">&nbsp;合同签订日期&nbsp;</th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	</div>
	</div>
        </div>
  </div>
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id="frmPost">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="" fieldname = ""	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>