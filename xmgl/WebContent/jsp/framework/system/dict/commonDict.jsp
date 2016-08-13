<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%
	String category = request.getParameter("category");
	String field = request.getParameter("field");//操作字段名称
%>
<app:base/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>	
<script type="text/javascript" charset="UTF-8">

	var autocompleteController= "${pageContext.request.contextPath }/commonDictController.do";

	var category = "<%=category%>";
	var field = "<%=field%>";
  	var controllername= "${pageContext.request.contextPath }/commonDictController.do";
  	function init() {
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		defaultJson.doQueryJsonList(controllername+"?queryCommonDict", data, DT1);
	}
  	
	//页面默认参数
	$(document).ready(function() {
		autoCommonDict("queryLwdw",autocompleteController+"?autoComplete"); 
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		defaultJson.doQueryJsonList(controllername+"?queryCommonDict", data, DT1,"empty",true);
	}); 
	
	function empty(){
		var type = $(window).manhuaDialog.getParentObj().$("#"+field)[0].tagName;
    	if(type == "SELECT"){
	  		reloadSelectTableDic($(window).manhuaDialog.getParentObj().$("#"+field));
	  	}
	}
	
	$(function() {
		var btn = $("#queryBtn");
		btn.click(function() {
			var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
			defaultJson.doQueryJsonList(controllername+"?queryCommonDict", data, DT1);
		});
		
		$("#btnQd").click(function() {
	        if($("#DT1").getSelectedRowIndex()==-1)
			 {
	    		xInfoMsg("请选择一条记录!");
			    return;
			 }
	    	var obj = $("#DT1").getSelectedRowJsonObj();
		   	var type = $(window).manhuaDialog.getParentObj().$("#"+field)[0].tagName;
	    	if(type == "INPUT"){
		        $(window).manhuaDialog.getParentObj().$("#"+field).val(obj.DICT_NAME);
		        $(window).manhuaDialog.getParentObj().$("#"+field).attr("code", obj.DICT_ID);
	    	}else{
	    		$(window).manhuaDialog.getParentObj().$("#"+field).val(obj.DICT_ID);
	    	}
	    	if(category=="zjgl_tqk_rzzt"){
	    		$(window).manhuaDialog.getParentObj().setTqkName();
		    }
	        $(window).manhuaDialog.close();
	    });
		
		
		$("#btnAdd").click(function() {
	    	$("#DICT_ID").val("");
	    	$("#DICT_NAME").val("");
	    	$("#PXH").val("999");
	    	$("#myModalLabel").html("新增");
			
			$("#myModal").modal("show");
	    });
		
		$('#myModal').on('shown', function () {
			$("#DICT_NAME").focus();
		});
		
	});
	
	$(function() {
		//按钮绑定事件(保存)
		$("#saveInfo").click(function() {
			if($("#executeFrm").validationButton()){
			    var data = Form2Json.formToJSON(executeFrm);
			    var data1 = defaultJson.packSaveJson(data);
			  	var operatorSign = $("#DICT_ID").val()?"2":"1";
			  	if(operatorSign == "2"){
			  		defaultJson.doUpdateJson(controllername + "?executeCommonDict&operatorSign="+operatorSign, data1,DT1,"empty");
			  	}else{
					defaultJson.doInsertJson(controllername + "?executeCommonDict&operatorSign="+operatorSign, data1,DT1,"afterAdd");
			  	}
			  	$("#myModal").modal("hide");
				//init();
			}else{
				requireFormMsg();
			  	return;
			}
		});
	});
	
	function afterAdd(){
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(0);
		var type = $(window).manhuaDialog.getParentObj().$("#"+field)[0].tagName;
    	if(type == "SELECT"){
	  		reloadSelectTableDic($(window).manhuaDialog.getParentObj().$("#"+field));
	  	}
	}
	
	function editfunc(obj){
		var data = JSON.stringify(obj);
		var showHtml = "<a href='javascript:void(0)' onclick='modifyDict("+data+")' title='修改'><i class='icon-pencil'></i></a>";
		return showHtml;
	}
	
	function pxhfunc(obj){
		var data = JSON.stringify(obj);
		var showHtml = "<a href='javascript:void(0)' onclick='pxDict("+data+")' title='置顶'><i class='icon-circle-arrow-up'></i></a>";
		return showHtml;
	}
	
	function modifyDict(obj){
		$("#executeFrm").setFormValues(obj);
		$("#myModalLabel").html("修改");
		$("#myModal").modal("show");
	}
	
	function pxDict(obj){
		var data = JSON.stringify(obj);
		var data1 = defaultJson.packSaveJson(data);
		defaultJson.doUpdateJson(controllername + "?sortOne", data1,DT1,"init");
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
	      		<button id="btnAdd" class="btn" type="button">新增</button>
	      		<button id="btnQd" class="btn" type="button">确定</button>
	      	</span>
		</h4>
		<form method="post" id="queryForm"  >
		<table class="B-table" style="width:100%">
		<!--可以再此处加入hidden域作为过滤条件 -->
			<TR  style="display:none;">
				<TD class="right-border bottom-border"></TD>
				<TD class="right-border bottom-border">
					<input type="text" class="span12" kind="text" id="DICT_CATEGORY" name="DICT_CATEGORY" fieldname="DICT_CATEGORY" value="<%=category%>" operation="=" logic="and" >
				</TD>
			</TR>
		<!--可以再此处加入hidden域作为过滤条件 -->
			<tr>
				<th class="right-border bottom-border" style="width:50px;">名称</th>
				<td width="50%" class="right-border bottom-border">
					<input class="span12" type="text" fieldname="DICT_NAME" operation="like" logic="and" id="queryLwdw" category="<%=category%>"></td>
				
				<td class="text-left bottom-border text-right">
					<button	id="queryBtn" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
                    <button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
	            </td>	
			</tr>
		</table>
		</form>
	
	<div style="height:5px;"> </div>		
	<div class="overFlowX"> 
		<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" pageNum="6">
			<thead>
				<tr>
					<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
					<th fieldname="DICT_NAME" >&nbsp;名称&nbsp;</th>
					<th width = "10" fieldname="DICT_ID" tdalign="center" CustomFunction="editfunc">&nbsp;修改&nbsp;</th>
					<th width = "10" fieldname="PXH" tdalign="center" CustomFunction="pxhfunc">&nbsp;置顶&nbsp;</th>
				</tr>
			</thead>
		    <tbody></tbody>
		</table>
		</div>
		</div>
	</div>
	
</div>


<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header" style="background:#0866c6;">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					<i class="icon-remove icon-white"></i>
				</button>
				<h3 id="myModalLabel" style="color:white;">
					新增/修改
				</h3>
			</div>
			<div class="modal-body">
				<form method="post" id="executeFrm">
					<table class="B-table" style="width:100%">
						<TR style="display:none;">
							<TD class="right-border bottom-border"></TD>
							<TD class="right-border bottom-border">
							<input type="text" class="span12" kind="text" fieldname="DICT_CATEGORY" value="<%=category%>"/>
							<input type="text" class="span12" kind="text" id="DICT_ID" fieldname="DICT_ID" value=""/>
							</TD>
						</TR>
						<tr>
							<th style="width:80px;" class="right-border bottom-border">
								名称
							</th>
							<td class="right-border bottom-border">
								<input type="text" placeholder="必填" check-type="required"
									maxlength="32" fieldname="DICT_NAME" name="DICT_NAME" id="DICT_NAME">
							</td>
						</tr>
						<tr>
							<th style="width:80px;" class="right-border bottom-border">
								排序号
							</th>
							<td class="right-border bottom-border">
								<input type="text" placeholder="必填" check-type="required"
									maxlength="32" fieldname="PXH" name="PXH" id="PXH">
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div class="modal-footer">
				<button class="btn" id="saveInfo">
					保存
				</button>
			</div>
		</div>


<div align="center">
	<FORM name="frmPost" method="post" style="display:none;" target="_blank" id ="frmPost">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id="queryXML">
		<input type="hidden" name="txtXML" id="txtXML">
		<input type="hidden" name="txtFilter" order="asc" fieldname="PXH" id="txtFilter">
		<input type="hidden" name="resultXML" id="resultXML">
		<input type="hidden" name="queryResult" id="queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">
	</FORM>
</div>

</body>
</html>