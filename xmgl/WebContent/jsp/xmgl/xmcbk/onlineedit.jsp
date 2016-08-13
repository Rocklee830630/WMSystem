<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>在线编辑</title>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/demo/DemoController.do";

//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#xdxmkList").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	setPageHeight(); 
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	
});
//页面默认参数
function init(){
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}
//详细信息
function rowView(index){
	var obj = $("#xdxmkList").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).FILEID;
	/* var url = "editoffice.jsp?FileId="+id; */
	var url = "<%=request.getContextPath()%>/editoffice/editoffice.jsp?FileId="+id;
	//alert(url);
	showFileEditPage(url,900,800);
}

function showFileEditPage(URL,tWidth,tHeight)
{
	dlgFeatures = "dialogWidth:"+screen.width+"px;dialogHeight:"+screen.height+"px;top:0;left:0;resizable:no;center:yes;location:no;status:no";
	window.showModalDialog(URL,"",dlgFeatures);
}

//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryAttch",data,xdxmkList);
}
//弹出区域回调
getWinData = function(data){
	queryList();
};
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">在线编辑
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
      </table>
      </form>
	<div style="height:5px;"> </div>
	<div class="overFlowX">
		<table class="table-hover table-activeTd B-table" id="xdxmkList" width="100%" type="single" pageNum="10" editable="0">
		<thead>
			<tr>
				<th  name="XH" id="_XH"  colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
				<th fieldname="FILENAME"  colindex=2 hasLink="true" linkFunction="rowView">&nbsp;附件名称&nbsp;</th>
				<th fieldname="FJLX"  colindex=3 maxlength="15">&nbsp;附件类型&nbsp;</th>
				<th fieldname="FILESIZE"  colindex=4 tdalign="center">&nbsp;附件大小&nbsp;</th>
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
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "PXH,XMBH"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>