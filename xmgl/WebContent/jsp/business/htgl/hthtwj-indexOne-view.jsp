<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>合同文件-维护</title>
<%
	String type=request.getParameter("type");
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtWjController.do";
var controllernamehtsj= "${pageContext.request.contextPath }/htgl/gcHtglHtsjController.do";
var type ="<%=type%>";
//页面初始化
$(function() {
	init();
    
    <%
    if(type.equals("detail")){
	%>
	//置所有input 为disabled
	$(":input").each(function(i){
	   $(this).attr("disabled", "true");
	 });
	<%
		}
	%>
	
});
//页面默认参数
function init(){
	var parentIObj = $(this).manhuaDialog.getParentObj();
	var rowValue = parentIObj.$("#resultXML").val();
	var tempJson = eval("("+rowValue+")");
	var id =tempJson.ID;
	$("#HTMC").val(tempJson.HTMC);
	$("#HTBM").val(tempJson.HTBM);
	
	if($("#addFileSpan").attr("disabled")){
		$("#addFileSpan").attr("disabled", false);
		$("#addFileSpan").bind("click", function() {
			doSelectFile($(this));
		});
	}
	
	setFileData(id,"","","");
	queryFileData(id,"","","");
<%--	for (var i=0;i<row;i++)//会清空，无法把全部列出来--%>
<%--	{--%>
<%--		var rJson = table.getRowJsonObjByIndex(i);--%>
<%--		queryFileData(rJson.ID,"","","");--%>
<%--	}--%>
}


//点击行事件
function tr_click(obj){
	//alert(JSON.stringify(obj));
	$("#gcHtglHtWjForm").setFormValues(obj);
	if($("#addFileSpan").attr("disabled")){
		$("#addFileSpan").attr("disabled", false);
		$("#addFileSpan").bind("click", function() {
			doSelectFile($(this));
		});
	}
	
	setFileData(obj.ID,"","","");
	queryFileData(obj.ID,"","","");
	
	$("#btnSave").attr("disabled", false);
}

//弹出区域回调
getWinData = function(data){
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#XMBH").val(JSON.parse(data).XMBH);
	$("#GC_TCJH_XMXDK_ID").val(JSON.parse(data).GC_TCJH_XMXDK_ID);
};
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
     <form method="post" id="queryForm"  style="display:none;">
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<INPUT type="text" class="span12" kind="text" id="currHtid" name="HTID" fieldname="HTID" value="" operation="="/>
			</TD>
        </TR>
         <tr>
			<td class="text-left bottom-border text-right">
	        	<button id="btnQuery" class="btn btn-link"  type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-search"></i>查询</button>
<%--	        	<button id="btnClear" class="btn btn-link" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-trash"></i>清空</button>--%>
	        </td>
		</tr>
      </table>
      </form>
	</div>
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">合同文件
      	<span class="pull-right">
      	</span>
      </h4>
     <form method="post" id="gcHtglHtWjForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
	  	<input type="hidden" id="HTSJID" name="HTSJID"  fieldname="HTSJID" >
	  	
        <tr>
			<th width="8%" class="right-border bottom-border text-right">合同名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="HTMC" type="text" placeholder="必填" check-type="required" fieldname="HTMC" name = "HTMC"  disabled />
<%--          		<button class="btn btn-link"  type="button" onclick="selectHt()"><i class="icon-edit"></i></button>--%>
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right">合同编码</th>
       		<td class="bottom-border right-border"width="15%">
         		<input class="span12" style="width:100%" id="HTBM" type="text" fieldname="HTBM" name = "HTBM"  disabled/>
         	</td>
        </tr>
        <tr>
        	<th width="8%" class="right-border bottom-border text-right">相关附件</th>
        	<td colspan="7" class="bottom-border right-border">
				<div>
					<span class="btn btn-fileUpload" id="addFileSpan" disabled="disabled" fjlb="0029">
						<span>添加文件...</span>
					</span>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0029" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
					</table>
				</div>
			</td>
        </tr>
      </table>
      </form>
    </div>
   </div>
  </div>
    <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "ghh2.LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
<script>
</script>
</html>