<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>手机短信管理页面</title>

<script type="text/javascript" charset="UTF-8">
 	var id,json,rowindex,rowValue;
 	
 	var sign;
  	var controllername= "${pageContext.request.contextPath }/sjdxController.do";
  	// 查询所有短信
	$(function() {
		var btn = $("#queryBtn");
		btn.click(function() {
	        //生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?querySms", data, DT1);
		});
	});
  	
  	function initAll() {
        //生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?querySms", data, DT1);
  	}

  	// 查询已发送短信
  	$(function() {
		var btn = $("#querySendedBtn");
		btn.click(function() {
	        //生成json串
			var data = combineQuery.getQueryCombineData(querySendedForm, frmSendedPost, DT2);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?querySms&mark=sended", data, DT2);
		});
	});
  	
  	function initSended() {
        //生成json串
		var data = combineQuery.getQueryCombineData(querySendedForm, frmSendedPost, DT2);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?querySms&mark=sended", data, DT2);
  	}

  	// 查询已接收短信
  	$(function() {
		var btn = $("#queryReceiveBtn");
		btn.click(function() {
	        //生成json串
			var data = combineQuery.getQueryCombineData(queryReceiveForm, frmReceivePost, DT3);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?querySms&mark=receive", data, DT3);
		});
	});
  	
  	function initReceive() {
        //生成json串
		var data = combineQuery.getQueryCombineData(queryReceiveForm, frmReceivePost, DT3);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?querySms&mark=receive", data, DT3);
  	}
  	
  	// 清除按钮
  	$(function() {
		$("#query_clear").click(function() {
			$("#queryForm").clearFormResult();
		});
		
		$("#query_clear_Sended").click(function() {
			$("#querySendedForm").clearFormResult();
		});
		
		$("#query_clear_Receive").click(function() {
			$("#queryReceiveForm").clearFormResult();
		});
	});
  	

	//页面默认参数
	$(document).ready(function() { 
        //生成json串
        g_bAlertWhenNoResult = false;
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?querySms", data, DT1);
        g_bAlertWhenNoResult = true;
		sign = "All";
	}); 
	
	function tr_click(obj,tabListid) {
		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引
		rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
		id = obj.SMSID;

		$("#executeFrm").setFormValues(obj);
	//	alert(rowindex);
	}
	
	// 点击添加按钮 清空表单
	$(function() {
		var btn = $("#insertBtn");
		btn.click(function() {
			$("#executeFrm").clearFormResult();
			id = "null";
		});
	});
	
	// 点击添加按钮 清空表单
	$(function() {
		var btn = $("#insertBtn1");
		btn.click(function() {
			$("#executeFrm").clearFormResult();
			id = "null";
		});
	});
	
	// 点击添加按钮 清空表单
	$(function() {
		var btn = $("#insertBtn2");
		btn.click(function() {
			$("#executeFrm").clearFormResult();
			id = "null";
		});
	});
	
	// 点击修改按钮
	$(function() {
		var btn = $("#updateBtn");
		btn.click(function() {
			if($("#DT1").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条要操作的数据！','3');
				// 没有选中结果集行的时候，不显示弹出层，将【修改用户信息】按钮的【data-toggle】属性修改成空即可
				btn.attr("data-toggle","");
			} else {
				// 选中时，需要恢复【修改用户信息】按钮的【data-toggle】属性值
				btn.attr("data-toggle","modal");
				
				// 当修改用户信息时，由于【用户登录名ACCOUNT】是主键，所以不能修改。
				$("#ACCOUNT").attr("disabled","disabled");
			}
		});
	});
	
	// 点击删除按钮
	$(function() {
		var btn = $("#deleteBtn");
		btn.click(function() {
		 	if($("#DT1").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条要操作的数据！','3');
			} else {
				xConfirm("提示信息","是否确认删除！");
				//生成json串
				var data = Form2Json.formToJSON(executeFrm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//通过判断id是否为空来判断是插入还是修改
				$('#ConfirmYesButton').one("click",function(obj) {
					var success = defaultJson.doUpdateJson(controllername + "?executeSms&id="+id+"&update=3", data1, DT1);
					
					if(success == true) {
						$("#DT1").setSelect(rowindex);
				//		var dd = {id:1,name:2};//此处为入参
						var index = $("#DT1").getSelectedRowIndex();//获得选中行的索引
						var value = $("#DT1").getSelectedRow();//获得选中行的json对象

				        $("#DT1").removeResult(index);
					}
				});  
				
			} 
		});
	});
	
	// 点击删除按钮
	$(function() {
		var btn = $("#deleteBtn1");
		btn.click(function() {
		 	if($("#DT2").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条要操作的数据！','3');
			} else {
				xConfirm("提示信息","是否确认删除！");
				//生成json串
				var data = Form2Json.formToJSON(executeFrm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//通过判断id是否为空来判断是插入还是修改
				$('#ConfirmYesButton').one("click",function(obj) {
					var success = defaultJson.doUpdateJson(controllername + "?executeSms&id="+id+"&update=3", data1, DT2);
					
					if(success == true) {
						$("#DT2").setSelect(rowindex);
				//		var dd = {id:1,name:2};//此处为入参
						var index = $("#DT2").getSelectedRowIndex();//获得选中行的索引
						var value = $("#DT2").getSelectedRow();//获得选中行的json对象

				        $("#DT2").removeResult(index);
					}
				});  
				
			} 
		});
	});
	
	// 点击删除按钮
	$(function() {
		var btn = $("#deleteBtn2");
		btn.click(function() {
		 	if($("#DT3").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条要操作的数据！','3');
			} else {
				xConfirm("提示信息","是否确认删除！");
				//生成json串
				var data = Form2Json.formToJSON(executeFrm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//通过判断id是否为空来判断是插入还是修改
				$('#ConfirmYesButton').one("click",function(obj) {
					var success = defaultJson.doUpdateJson(controllername + "?executeSms&id="+id+"&update=3", data1, DT3);
					
					if(success == true) {
						$("#DT3").setSelect(rowindex);
				//		var dd = {id:1,name:2};//此处为入参
						var index = $("#DT3").getSelectedRowIndex();//获得选中行的索引
						var value = $("#DT3").getSelectedRow();//获得选中行的json对象

				        $("#DT3").removeResult(index);
					}
				});  
				
			} 
		});
	});
	
	// 点击保存按钮
	$(function() {
		var saveUserInfoBtn = $("#saveUserInfo");
		saveUserInfoBtn.click(function() {
			if($("#executeFrm").validationButton()) {
				//生成json串
				var data = Form2Json.formToJSON(executeFrm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//通过判断id是否为空来判断是插入还是修改
				
				var success = false;
				if(id == null || id == "null" || id == "indefined") {
					success = defaultJson.doInsertJson(controllername + "?executeSms&id="+id, data1, DT1);
				} else {
					success = defaultJson.doUpdateJson(controllername + "?executeSms&id="+id, data1, DT1);
				}
				
				if(success == true) {
					$("#myModal").modal("hide");
					if(sign == "All") {
						initAll();
					} else if(sign == "Sended") {
						initSended();
					} else if(sign == "Receive") {
						initReceive();
					}
				} else {
					//	失败
				}
			}
		});
	});
	
	$(function() {
		$("#blrBtn").click(function() {
			var actorCode = $("#SEND_JSR").attr("code");
			openUserTree('check', actorCode, 'getWinData');
		});
	});
	
	//父页面接受值方法，接收短信人员的信息
	function getWinData(data) {

		$("#myModal").modal("show");
		/* 
		var jsr = "";
		var sjhm = "";
		var account = "";
		for(var i=0;i<rowsArr.length;i++){
			var tempJson = eval("("+rowsArr[i]+")");//字符串转JSON对象
			jsr += tempJson.NAME + ",";
			sjhm += tempJson.SJHM + ",";
			account += tempJson.ACCOUNT + ",";
		}
		$("#SEND_JSR").val(jsr.substring(0,jsr.length-1));//为字段的code赋值
		$("#SEND_SJHM").val(sjhm.substring(0,sjhm.length-1));//为字段的JSR赋值
		$("#SEND_ACCOUNT").val(account.substring(0,account.length-1));//为字段的JSR赋值
		 */

		var userId = "";
		var userName = "";
		var userTel = "";
		for(var i = 0 ; i < data.length ; i++) {
	 	 var tempObj =data[i];
	 	 if(i < data.length - 1) {
		  userId +=tempObj.ACCOUNT+",";
		  userName +=tempObj.USERNAME+",";
		  userTel +=tempObj.TEL+",";
	 	 } else {
	 	  userId +=tempObj.ACCOUNT;
	 	  userName +=tempObj.USERNAME; 
		  userTel +=tempObj.TEL;
	 	 }
		}
		$("#SEND_JSR").val(userName);
		$("#SEND_JSR").attr("code",userId);
//		$("#SEND_JSR").val(jsr.substring(0,jsr.length-1));//为字段的code赋值
		$("#SEND_SJHM").val(userTel);//为字段的JSR赋值
		$("#SEND_ACCOUNT").val(userId);//为字段的JSR赋值

	}

	
	//父页面调用查询页面的方法
	function showPsnList(){
		$("#myModal").modal("hide");
	//	$(window).manhuaDialog({"title" : "信息中心>手机短信>显示人员列表","type" : "text","content" : "${pageContext.request.contextPath}/jsp/business/wttb/showPsnList.jsp","modal":"2"});
		var actorCode = $("#SEND_JSR").attr("code");
		openUserTree('check', actorCode, 'getWinData');
	}

	$(function() {
		$("#All").click(function() {
			sign = "All";
		});
		$("#Sended").click(function() {
			sign = "Sended";
		});
		$("#Receive").click(function() {
			sign = "Receive";
		});
	});
</script>      
</head>
<body>
<app:dialogs/>
<p></p>
<ul class="nav nav-tabs" id="myTab">
  <li class="active"><a href="#home" data-toggle="tab" id="All">全部发件箱</a></li>
  <li><a href="#profile" data-toggle="tab" id="Sended">我的发件箱</a></li>
  <li><a href="#messages" data-toggle="tab" id="Receive">收件箱</a></li>
</ul>

<div class="tab-content">
	<div class="tab-pane active" id="home">
		<div class="container-fluid">
			<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
				<h4 class="title">查询全部短信 
					<span class="pull-right">
						<a href="#myModal" role="button" class="btn" data-toggle="modal" id="insertBtn">发送信息</a>
						<a href="#myModal" role="button" class="btn" id="deleteBtn">删除信息</a>
					</span>
				</h4>
				<form method="post" id="queryForm"  >
				<table class="B-table">
				<!--可以再此处加入hidden域作为过滤条件 -->
					<TR  style="display:none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<input type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" >
						</TD>
					</TR>
				<!--可以再此处加入hidden域作为过滤条件 -->
					
			        <tr>
			          <th width="6%" class="right-border bottom-border">发送时间</th>
			          <td width="10%" class="right-border bottom-border">
			          	<input class="span12" type="date" placeholder="" name="FSSJ" fieldname="FSSJ" operation=">=" logic="and"  fieldtype="date" fieldformat="YYYY-MM-DD"></td>
			          <th width="3%" class="right-border bottom-border">至</th>
			          <td width="10%" colspan="2" class="bottom-border">
			          	<input class="span12" type="date" placeholder="" name="FSSJ" fieldname="FSSJ" operation="<="  logic="and"  fieldtype="date" fieldformat="YYYY-MM-DD"></td>
			          <th width="6%" class="right-border bottom-border">接收人</th>
			          <td width="10%" class="right-border bottom-border">
			          	<input class="span12" type="text" fieldname="JSR" name="JSR" operation="like"  logic="and" /></td>
			          <th width="5%" class="right-border bottom-border">发送人</th>
			          <td width="10%" colspan="2" class="bottom-border">
			          	<input class="span12" type="text" fieldname="FSR" name="FSR" operation="like"  logic="and" /></td>
			          	
			          	
				        <td width="13%"  class="text-left bottom-border text-right" rowspan="2">
							<button	id="queryBtn" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
		                    <button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
			            </td>	
			        </tr>
			        
				</table>
				</form>
			
			<div style="height:5px;"> </div>		
			<div class="overFlowX"> 
				<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
					<thead>
						<tr>
							<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
							<th fieldname="JSR" >&nbsp;接收人&nbsp;</th>
							<th fieldname="FSXX" maxlength="20">&nbsp;发送信息&nbsp;</th>
							<th fieldname="FSR" >&nbsp;发送人&nbsp;</th>
							<th fieldname="FSSJ"  fieldformat="YYYY-MM-DD">&nbsp;发送时间&nbsp;</th>
						</tr>
					</thead>
				    <tbody></tbody>
				</table>
				            
			</div>
			
				</div>
			</div>
		</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter"  order="desc" fieldname="FSSJ" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id="queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</div>
	
	
	<div class="tab-pane" id="profile">
		<div class="container-fluid">
			<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
				<h4 class="title">查询我的发件箱
					<span class="pull-right">
						<a href="#myModal" role="button" class="btn" data-toggle="modal" id="insertBtn1">发送信息</a>
						<a href="#myModal" role="button" class="btn" id="deleteBtn1">删除信息</a>
					</span>
				</h4>
				<form method="post" id="querySendedForm"  >
				<table class="B-table">
				<!--可以再此处加入hidden域作为过滤条件 -->
					<TR  style="display:none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<input type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" >
						</TD>
					</TR>
				<!--可以再此处加入hidden域作为过滤条件 -->
					
			        <tr>
			          <th width="8%" class="right-border bottom-border">发送时间</th>
			          <td width="15%" class="right-border bottom-border">
			          	<input class="span12" type="date" placeholder="" name="FSSJ" fieldname="FSSJ" operation=">=" logic="and"  fieldtype="date" fieldformat="YYYY-MM-DD"></td>
			          <th width="8%" class="right-border bottom-border">至</th>
			          <td width="15%" colspan="2" class="bottom-border">
			          	<input class="span12" type="date" placeholder="" name="FSSJ" fieldname="FSSJ" operation="<="  logic="and"  fieldtype="date" fieldformat="YYYY-MM-DD"></td>
			          	
				        <td width="13%"  class="text-left bottom-border text-right" rowspan="2">
							<button	id="querySendedBtn" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
			                <button id="query_clear_Sended" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
			            </td>
			        </tr>
				</table>
				</form>
			
			<div style="height:5px;"> </div>		
				<div class="overFlowX"> 
				<table width="100%" class="table-hover table-activeTd B-table" id="DT2" type="single">
					<thead>
						<tr>
							<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
							<th fieldname="JSR" >&nbsp;接收人&nbsp;</th>
							<th fieldname="FSXX" maxlength="20">&nbsp;发送信息&nbsp;</th>
							<th fieldname="FSR" >&nbsp;发送人&nbsp;</th>
							<th fieldname="FSSJ"  fieldformat="YYYY-MM-DD">&nbsp;发送时间&nbsp;</th>
						</tr>
					</thead>
				    <tbody></tbody>
				</table>
				            
				</div>
				
				</div>
			</div>
		</div>
		<div align="center">
			<FORM name="frmSendedPost" method="post" style="display:none" target="_blank" id ="frmSendedPost">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter"  order="desc" fieldname="FSSJ" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id="queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</div>
	
	
	
	<div class="tab-pane" id="messages">
		<div class="container-fluid">
			<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
			<h4 class="title">查询收件箱
				<span class="pull-right">
						<a href="#myModal" role="button" class="btn" data-toggle="modal" id="insertBtn2">发送信息</a>
						<a href="#myModal" role="button" class="btn" id="deleteBtn2">删除信息</a>
				</span>
			</h4>
				<form method="post" id="queryReceiveForm"  >
				<table class="B-table">
				<!--可以再此处加入hidden域作为过滤条件 -->
					<TR  style="display:none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<input type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" >
						</TD>
					</TR>
				<!--可以再此处加入hidden域作为过滤条件 -->
					
			        <tr>
			          <th width="8%" class="right-border bottom-border">发送时间</th>
			          <td width="15%" class="right-border bottom-border">
			          	<input class="span12" type="date" placeholder="" name="FSSJ" fieldname="FSSJ" operation=">=" logic="and"  fieldtype="date" fieldformat="YYYY-MM-DD"></td>
			          <th width="8%" class="right-border bottom-border">至</th>
			          <td width="15%" colspan="2" class="bottom-border">
			          	<input class="span12" type="date" placeholder="" name="FSSJ" fieldname="FSSJ" operation="<="  logic="and"  fieldtype="date" fieldformat="YYYY-MM-DD"></td>
			          
				        <td width="13%"  class="text-left bottom-border text-right">
							<button	id="queryReceiveBtn" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
		                    <button id="query_clear_Receive" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
			            </td>
			        </tr>
				</table>
				</form>
				</div>
			</div>
			
			<div class="row-fluid">
				<div class="B-small-from-table-auto">
				<table width="100%" class="table-hover table-activeTd B-table" id="DT3" type="single">
					<thead>
						<tr>
							<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
							<th fieldname="JSR" >&nbsp;接收人&nbsp;</th>
							<th fieldname="FSXX" maxlength="20">&nbsp;发送信息&nbsp;</th>
							<th fieldname="FSR" >&nbsp;发送人&nbsp;</th>
							<th fieldname="FSSJ"  fieldformat="YYYY-MM-DD">&nbsp;发送时间&nbsp;</th>
						</tr>
					</thead>
				    <tbody></tbody>
				</table>
				            
				</div>
			</div>
		</div>
		<div align="center">
			<FORM name="frmReceivePost" method="post" style="display:none" target="_blank" id ="frmReceivePost">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter"  order="desc" fieldname="FSSJ" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id="queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</div>
</div>
<!-- Modal -->
<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-header" style="background:#0866c6;">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-remove icon-white"></i></button>
    <h3 id="myModalLabel" style="color:white;">发送信息</h3>
  </div>
  <div class="modal-body">
    <form method="post" id="executeFrm" >
      <table class="B-table" >
         <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">SEND_ACCOUNT
				<input type="hidden" fieldname="SJHM" name="SJHM" id="SEND_SJHM">
				<input type="hidden" fieldname="JSRID" name="JSRID" id="SEND_ACCOUNT">
			 	<input type="hidden" kind="text" fieldname="SMSID" name="SMSID" id="SMSID">
			</TD>
        </TR>
        <tr>
          <th width="30%" class="right-border bottom-border">接收人</th>
          <td class="right-border bottom-border">
          	<input type="text" placeholder="必填" readonly="readonly" check-type="required" fieldname="JSR" name="JSR" id="SEND_JSR">
          	<button class="btn btn-link" type="button" id="receiverId" title="点击选择接信人" onclick="showPsnList();"><i class="icon-edit"></i></button></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">发送时间</th>
          <td class="right-border bottom-border">
          	<input type="date" fieldname="FSSJ" name="FSSJ" id="FSSJ" fieldtype="datetime" fieldformat="YYYY-MM-DD HH:MM"/></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">发送信息</th>
          <td class="right-border bottom-border">
          	<textarea type="text" cols="10" rows="5" placeholder="必填" maxlength="500" check-type="required maxlength" fieldname="FSXX" name="FSXX" id="FSXX"></textarea>
          </td>
        </tr>
        
      </table>
    </form>
  </div>
  <div class="modal-footer">
    <button class="btn" id="saveUserInfo">保存</button>
    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
  </div>
</div>

</body>
</html>