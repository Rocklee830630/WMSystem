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
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtWjList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,gcHtglHtWjList);
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#gcHtglHtWjForm").validationButton())
		{
		    //生成json串
		    var data = Form2Json.formToJSON(gcHtglHtWjForm);
		  //组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  //调用ajax插入
		    if($("#ID").val() == "" || $("#ID").val() == null){
    			defaultJson.doInsertJson(controllername + "?insert", data1,gcHtglHtWjList);
    			$("#gcHtglHtWjForm").clearFormResult();
    		}else{
    			defaultJson.doUpdateJson(controllername + "?insert", data1,gcHtglHtWjList);
    		}
		}else{
			requireFormMsg();
		  	return;
		}
	});
	
	//按钮绑定事件（新增）
    $("#btnClear_Bins").click(function() {
        $("#gcHtglHtWjForm").clearFormResult();
        $("#gcHtglHtWjForm").cancleSelected();
        
        
        $("#ZFRQ").val(new Date().toLocaleDateString());
        $("#ZFJE").val(0);
        $("#ID").val("");
    });
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
    
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
	var tempJson = parentIObj.$("#ID").val();
	//var tempJson = parent.$("body").find("#ID").val();
	if(tempJson!=""){
		$("#currHtid").val(tempJson);
		//$("#HTID").val(tempJson);
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtWjList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllernamehtsj+"?queryOne",data,gcHtglHtWjList);
	}
	
	var table = $("#gcHtglHtWjList");
	var row = table.getTableRows();
	if(row>0){
		table.setSelect(0);
		var rJson = table.getRowJsonObjByIndex(0);
		queryFileData(rJson.ID,"","","");
	}
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

//选中项目名称弹出页
function selectXm(){
	$(window).manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/xmcx.jsp","modal":"2"});
}
//弹出区域回调
getWinData = function(data){
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#XMBH").val(JSON.parse(data).XMBH);
	$("#GC_TCJH_XMXDK_ID").val(JSON.parse(data).GC_TCJH_XMXDK_ID);
};

//详细信息
function rowView(index){
	var obj = $("#gcHtglHtWjList").getSelectedRowJsonByIndex(index);
	var xmbh = eval("("+obj+")").XMBH;
	$(window).manhuaDialog(xmscUrl(xmbh));
}
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
    <div style="height:5px;"></div>
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="gcHtglHtWjList" width="100%" type="single" pageNum="5">
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
 			    <th fieldname="HTBM" colindex=3 tdalign="center" maxlength="30" >&nbsp;合同编码&nbsp;</th>
				<th fieldname="HTMC" colindex=4 tdalign="center" maxlength="30" >&nbsp;合同名称&nbsp;</th>
				<th fieldname="HTLX" colindex=1 tdalign="center" maxlength="30" >&nbsp;合同类型&nbsp;</th>
				<th fieldname="HTZT" colindex=6 tdalign="center" maxlength="30" >&nbsp;合同状态&nbsp;</th>
				<th fieldname="XMMC" colindex=2 tdalign="center" maxlength="30" >&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDMC" colindex=3 tdalign="center" maxlength="30" >&nbsp;标段名称&nbsp;</th>
				<th fieldname="BZ" colindex=20 tdalign="center" maxlength="30" >&nbsp;备注&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	</div>
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">合同文件
      	<span class="pull-right">
      		<button id="btnClear_Bins" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">保存</button>
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
			<th width="8%" class="right-border bottom-border text-right">项目名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="XMMC" type="text" fieldname="XMMC" name = "XMMC"  disabled />
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right">标段名称</th>
			<td width="25%" class="right-border bottom-border">
				<input id="BDMC" class="span12" name="BDMC" fieldname="BDMC" type="text" disabled/>
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