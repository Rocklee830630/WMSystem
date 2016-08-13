<!DOCTYPE HTML>
<html lang="en">
  <head>
	<%@ page language="java" pageEncoding="UTF-8"%>
	<%@ taglib uri="/tld/base.tld" prefix="app"%>
	<app:base />
    <title>显示送办人员列表</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="显示接受问题提报人员列表">
	<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
	<script type="text/javascript" charset="utf-8">
		var controllername= "${pageContext.request.contextPath }/wttb/wttbController.do";
		$(function(){
			doInit();
			$("#btnSave").click(function(){
				//获取所有选中行数组
				var rowsArr = $("#DT1").getSelectedRows();
				$(window).find("#myModal").modal("show");
				$(window).manhuaDialog.setData(rowsArr);
				$(window).manhuaDialog.sendData();
				$(window).manhuaDialog.close();
			});
			$("#btnCancel").click(function(){
				$(window).manhuaDialog.close();
		//		parent.find("#myModal").modal("show");
			});
		});
		function doInit(){
			var data = combineQuery.getQueryCombineData(queryForm,frmPost);
			defaultJson.doQueryJsonList(controllername+"?queryPsnList", data, DT1);
		}
	</script>
  </head>
  
  <body>
		<div class="row-fluid">
			<div class="span12">
				<div class="row-fluid">
					<div class="B-small-from-table-box" style="display:none;">
						<h4>
							信息查询
						</h4>
						<form method="post"
							action=""
							id="queryForm">
							<table class="table-hover table-activeTd B-table">
								<!--可以再此处加入hidden域作为过滤条件 -->
								<TR style="display: none;">
									<TD class="right-border bottom-border"></TD>
									<TD class="right-border bottom-border">
										<INPUT type="text" class="span12" kind="text"
											fieldname="rownum" value="1000" operation="<=">
									</TD>
								</TR>
							</table>
						</form>
					</div>
					<div class="B-small-from-table-auto">
						<h4>
							人员列表
							<span class="pull-right">
								<button id="btnSave" class="btn btn-inverse" type="button">
									确定
								</button>
								<button id="btnCancel" class="btn btn-inverse" type="button">
									关闭
								</button>
							</span>
						</h4>
						<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="multi">
							<thead>
								<tr>
									<th name="XH" id="_XH">序号</th>
									<th fieldname="DEPARTMENT"  maxlength="10">
										所在部门
									</th>
									<th fieldname="ACCOUNT" maxlength="10">
										用户账号
									</th>
									<th fieldname="NAME"  maxlength="10">
										用户姓名
									</th>
									<th fieldname="SEX" tdalign="center">
										用户性别
									</th>
									<th fieldname="SJHM" tdalign="center">
										手机号码
									</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">

			</FORM>
		</div>
	</body>
</html>
