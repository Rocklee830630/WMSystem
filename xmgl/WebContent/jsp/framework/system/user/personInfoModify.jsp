<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<app:base />
<%
	User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
	String id = request.getParameter("id");
	String xx = request.getParameter("xx");
	String account=user.getAccount();
%>
<script type="text/javascript" charset="utf-8">
var controllername= "${pageContext.request.contextPath }/userController.do";
var account='<%= account%>';
//页面默认参数
$(document).ready(function() {
	$("#ywid").val("");
	queryUserFile("0038",account,"","");
	setFileData(account,"","","");
	setValue();
});
function setValue(){
	var value=getValue();
	$("#SEX").val(value.SEX);
	$("#SJHM").val(value.SJHM);
	$("#IDCARD").val(value.IDCARD);
}
function getValue() {
	 var value=null;
	 $.ajax({
		url:controllername + "?personInfo&account="+account,
		data:"",
		dataType:"json",
		async:false,
		success:function(result){
			value=result.msg;
		}
	 });
	 var tempJson = convertJson.string2json1(value);
	 var str = tempJson.response.data[0];
	 return str;
 };
//----------------------------------------
//-查询文件信息，并插入到表格(这个是用户和签名特殊要求的)
//-@param ywid 业务ID，可以暂时理解为批次编号
//-@param glid1 项目ID
//-@param sjbh	事件编号
//-@param ywlx	业务类型
//----------------------------------------
function queryUserFile(fjlb,glid1,sjbh,ywlx,url){
	var obj = new Object();
	obj.FSLB = fjlb;
	obj.ACCOUNT = glid1;
	var data = JSON.stringify(obj);
		var data1 = defaultJson.packSaveJson(data);
		$.ajax({
		url : controllername+"?queryUserFile",
		data : data1,
		cache : false,
		async :	false,
		dataType : "json",
		success : function(result) {
			insertFileTab(result.msg);
		}
	});
}

// 点击保存按钮
$(function() {
	$("#saveInfo").click(function() {
		if($("#infoForm").validationButton()) {
			//生成json串
			var data = Form2Json.formToJSON(infoForm);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			defaultJson.doUpdateJson(controllername + "?executeUser&id="+account+"&update=2", data1, DT1,"refreshUserImg");
		}else{
			defaultJson.clearTxtXML();
		}
	});
});
	//------------------------------------
	//-刷新当前登录用户显示的头像
	//------------------------------------
	function refreshUserImg(){
		var newSrc = $(window.parent.document).find(".imgBox img").attr("src");
		$(window.parent.document).find(".imgBox img").attr("src",newSrc);
	}
	/* 修改密码页面内容说明 */
	//密码修改初始化
	$(function() {
		var btn = $("#save");
		btn.click(function() {
			if(!validePassword()){return;}
			var data = Form2Json.formToJSON(passForm);
			var data1 = defaultJson.packSaveJson(data);
			defaultJson.doUpdateJson(controllername + "?modifyPassword",data1, null);
		});
	});

	function valid(str) {
		if (/[^\w]+/.test(str))
			return false;
		return true;
	}

	//弹出区域回调
	getWinData = function(data) {

	};
	
	
	function validePassword(){
		if ($("#newpassword").val() == "") {
			$("#newpassword").attr("title", "请填写新密码");
			$("#newpassword").css("border", "1px solid red");
			return false;
		} else {
			$("#newpassword").attr("title", "");
			$("#newpassword").css("border", "1px solid #cccccc");
		}

		if ($("#newpassword").val().length < 6
				|| $("#newpassword").val().length > 18) {
			$("#newpassword").attr("title", "新密码长度应在6个字符和8个字符之间");
			$("#newpassword").css("border", "1px solid red");
			return false;
		} else {
			$("#newpassword").attr("title", "");
			$("#newpassword").css("border", "1px solid #cccccc");
		}

		if (!valid($("#newpassword").val())) {
			$("#newpassword").attr("title", "您的新密码中含有非法字符！请重新输入！");
			$("#newpassword").css("border", "1px solid red");
			return false;
		} else {
			$("#newpassword").attr("title", "");
			$("#newpassword").css("border", "1px solid #cccccc");
		}

		if ($("#newpassword").val() != $("#newpassword2").val()) {
			$("#newpassword2").attr("title", "您两次输入的新密码不一致！请重新输入！");
			$("#newpassword2").css("border", "1px solid red");
			return false;
		}else {
			$("#newpassword2").attr("title", "");
			$("#newpassword2").css("border", "1px solid #cccccc");
		}
		return true;
	}
	function changeButton(id){
		if(id=='tabPage0'){
			$("#saveInfo").show();
			$("#save").hide();
		}else{
			$("#save").show();
			$("#saveInfo").hide();
		}
	}
</script>
</head>
<body>
<app:dialogs />
<div class="container-fluid" style="width:100%">
			<p class="text-right tabsRightButtonP">
					<button id="saveInfo" class="btn" type="button" name="saveBtn">保存信息</button>
					<button id="save" class="btn" type="button" name="saveBtn" style="display:none">保存密码</button>
			</p> 
		<ul class="nav nav-tabs" id="myTab">
			<li class="active">
				<a href="#tab0" data-toggle="tab" id="tabPage0" onclick="changeButton(this.id)">修改信息</a>
			</li>
			<li class="">
				<a href="#tab1" data-toggle="tab" id="tabPage1" onclick="changeButton(this.id)">修改密码</a>
			</li>
		</ul>
	<div class="tab-content">
	<div class="tab-pane active" id="tab0" style="width:100%">
		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise" style="float:left;width:70%">
				<form method="post" id="infoForm">
					<table class="B-table" id="DT1" width="100%">
						<TR style="display:none;">
							<TD> 
								<input type="text" name="ywid" fieldname="YWID" id=ywid>
								<input type="text" name="account" fieldname="ACCOUNT" id="ACCOUNT" value="<%=account%>">
							</TD>
						</TR>
						<tr>
							<th class="right-border bottom-border">性别</th>
							<td class="right-border bottom-border">
								<select class="span12 3characters" type="text" placeholder="必填" check-type="" fieldname="SEX" name="SEX" id="SEX" kind="dic" src="XB"></select>
							</td>
						</tr>
						<tr>
							<th class="right-border bottom-border">手机号码</th>
							<td class="right-border bottom-border">
								<input type="text" class="span12" placeholder="必填" check-type="required number" maxlength="32" fieldname="SJHM" name="SJHM" id="SJHM">
							</td>
						</tr>
						<tr>
							<th class="right-border bottom-border">身份证号</th>
							<td class="right-border bottom-border">
								<input type="text" class="span12" placeholder="" maxlength="18" fieldname="IDCARD" name="IDCARD" id="IDCARD">
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div class="B-small-from-table-autoConcise" style="float:right;">
				<form method="post" id="infoForm_2">
					<table class="B-table" id="DT1_2">
						<tr>
							<td class="right-border bottom-border" align="right" valign="top">
								<div style="border-style: solid; border-width: 1px;border-color: #DDDDDD;">
									<table role="presentation" class="table table-striped">
										<tbody fjlb="0038" uploadOptions="type:image" showType="type:image;count:1;width:100px;del:false;overflow:hidden;" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery" >
										</tbody>
									</table>
								</div>
								<div>
									<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0038"> <i class="icon-plus"></i>
										<span>更换头像...</span>
									</span>
								</div>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	<div class="tab-pane" id="tab1">
		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise" style="text-aglin: center">
				<form method="post" id="passForm">
					<table class="B-table" id="DT2">
						<tr>
							<th width="30%" class="right-border bottom-border  text-right">旧密码</th>
							<td class="right-border bottom-border">
								<input type="password" class="span12" name="oldpassword" id="oldpassword" fieldname="oldpassword">
							</td>
						</tr>
						<tr>
							<th width="30%" class="right-border bottom-border  text-right">新密码</th>
							<td class="right-border bottom-border">
								<input type="password" class="span12" name="newpassword" maxlength="30" id="newpassword" fieldname="newpassword">
							</td>
						</tr>
						<tr>
							<th width="30%" class="right-border bottom-border  text-right">请再次输入新密码</th>
							<td class="right-border bottom-border">
								<input type="password" class="span12" name="newpassword2" maxlength="30" id="newpassword2" fieldname="newpassword2">
							</td>
							<!-- <span class="pull-right">
								<button id="save" class="btn" type="button">保存</button>
							</span> -->
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	</div>
</div>
	<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true" />
	<div align="center">
		<FORM name="frmPost" method="post" style="display: none" target="_blank" id="frmPost">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML" /> 
			<input type="hidden" name="txtXML" id="txtXML" /> 
			<input type="hidden" name="txtFilter" id = "txtFilter"> 
			<input type="hidden" name="resultXML" id="resultXML" /> 
			<input type="hidden" id="queryResult" />
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData" />
		</FORM>
	</div>
</body>
</html>

