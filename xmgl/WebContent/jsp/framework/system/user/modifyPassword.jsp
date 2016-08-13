<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<%
	String id = request.getParameter("id");
	String xx = request.getParameter("xx");
%>
<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/userController.do";
	
	$(function() {
		var btn = $("#save");
		btn.click(function() {
			
			if(!validePassword()){return;}

			var data = Form2Json.formToJSON(demoForm);
			var data1 = defaultJson.packSaveJson(data);
			defaultJson.doUpdateJson(controllername + "?modifyPassword",
					data1, null);
			
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
	
</script>
</head>
<body>
	<app:dialogs />
	<div class="container-fluid">
		</p>
		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise"
				style="text-aglin: center">
				<form method="post" id="demoForm">
					<table class="B-table" id="DT1">
						<tr>
							<th width="30%" class="right-border bottom-border  text-right">旧密码</th>
							<td class="right-border bottom-border"><input
								type="password" class="span12" name="oldpassword"
								id="oldpassword" fieldname="oldpassword"></td>
						</tr>
						<tr>
							<th width="30%" class="right-border bottom-border  text-right">新密码</th>
							<td class="right-border bottom-border"><input
								type="password" class="span12" name="newpassword" maxlength="30"
								id="newpassword" fieldname="newpassword"></td>
						</tr>
						<tr>
							<th width="30%" class="right-border bottom-border  text-right">请再次输入新密码</th>
							<td class="right-border bottom-border"><input
								type="password" class="span12" name="newpassword2"
								maxlength="30" id="newpassword2" fieldname="newpassword2">
							</td>
							<span class="pull-right">
								<button id="save" class="btn" type="button">保存</button>
							</span>
						</tr>

					</table>
				</form>
			</div>
		</div>
	</div>
	<div align="center">
		<FORM name="frmPost" method="post" style="display: none"
			target="_blank" id="frmPost">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML" /> <input
				type="hidden" name="txtXML" id="txtXML" /> <input type="hidden"
				name="txtFilter" id="txtFilter" /> <input
				type="hidden" name="resultXML" id="resultXML" /> <input
				type="hidden" id="queryResult" />
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData" />
		</FORM>
	</div>
</body>
</html>

