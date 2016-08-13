<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>

<app:base />
<title>信息</title>
<%-- <script type="text/javascript" src="${pageContext.request.contextPath }/js/common/xWindow.js"> </script> --%>
<script type="text/javascript" charset="utf-8">
	var controllername = "${pageContext.request.contextPath }/zsb/xxb/xxbController.do";
	//获取父页面的值
	function do_value(){
		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.$("#DT1").getSelectedRow();
		var obj = convertJson.string2json1(rowValue);
		return obj;
	}
	//填写表单
	function setvalue(){
		var value=do_value();
		$("#XxbForm").setFormValues(value);
	}
	
	$(function() {
		setvalue();
		var btn = $("#update");
		btn.click(function() {
					if ($("#XxbForm").validationButton()) {
					//生成json串
					var data = Form2Json.formToJSON(XxbForm);
					//组成保存json串格式
					var data1 = defaultJson.packSaveJson(data);
					//调用ajax插入
					defaultJson.doUpdateJson(controllername + "?updateXxb",data1, null,"callback");
					}else{
				  		defaultJson.clearTxtXML();
					}
					
				});
		var delbtn=$("#delete");
		delbtn.click(function() {
	 		var data = Form2Json.formToJSON(XxbForm);
			var data1 = defaultJson.packSaveJson(data);
			xConfirm("提示信息","本操作会删除本征收任务，是否确认删除？");
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').one("click",function(){ 
				defaultJson.doDeleteJson(controllername+"?deleteZsxx",data1,null); 
				var fuyemian=$(window).manhuaDialog.getParentObj();
				fuyemian.ready(1);
				$(window).manhuaDialog.close();
			});
		});
	});
	
	//异步操作
	function callback(){
		//从resultXML获取修改的数据
		var data2 = $("#resultXML").val();
		//返回数据
		var fuyemian=$(window).manhuaDialog.getParentObj();
		fuyemian.xgh(data2);
		$(window).manhuaDialog.close();
	}
</script>
</head>
<body>
<app:dialogs />
	<div class="container-fluid">
	<p></p>
		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
			<!-- <h4>信息添加</h4> -->
			<h4 class="title">任务维护信息<span class="pull-right">
          			<button id="update" name="update" class="btn"  type="button" data-toggle="modal">保存</button>
          			<button id="delete" name="delete" class="btn"  type="button" data-toggle="modal">删除</button>
          </span></h4>
				<form method="post" id="XxbForm">
					<table class="B-table" width="100%">
						<tr style="display:none">
							<td><input type="text" name="gc_zsb_xxb_id" fieldname="GC_ZSB_XXB_ID" id="gc_zsb_xxb_id"/></td>
							<td><input type="text" name="SJBH" fieldname="SJBH" id="SJBH"/></td>
						</tr>	
						<tr>
							<th width="10%" class="right-border bottom-border text-right disabledTh">项目名称</th>
							<td width="90%" colspan=9 class="right-border bottom-border"><input class="span12" type="text" placeholder="" check-type="required" id="XMMC" name="XMMC" fieldname="XMMC" disabled /></td>
						</tr>
						<tr>
							<th width="10%" class="right-border bottom-border text-right disabledTh">区域</th>
							<td width="20%" colspan=2 class="right-border bottom-border">
								<input class="span12" type="text" id="QY" name="QY" fieldname="QY" check-type="required" disabled />
							</td>
							<td width="20%" colspan=2 class="right-border bottom-border">
							<th width="10%" class="right-border bottom-border text-right disabledTh">责任单位</th>
							<td width="20%" colspan=2 class="right-border bottom-border"><input class="span12" type="text" id="ZRDW" name="ZRDW" fieldname="ZRDW" maxlength="200" check-type="required" disabled/></td>
							<td width="20%" colspan=2 class="right-border bottom-border">
						</tr>
						<tr>
							<th width="10%" class="right-border bottom-border text-right">资金到位日期</th>
							<td width="20%" colspan=2 class="right-border bottom-border"><input class="span12 date" type="date" id="ZJDWRQ" name="ZJDWRQ" fieldname="ZJDWRQ" check-type="" /></td>
							<td width="20%" colspan=2 class="right-border bottom-border">
							<th width="10%" class="right-border bottom-border text-right">资金到位金额</th>
							<td width="20%" colspan=2 class="right-border bottom-border">
								<input class="span12" type="number" style="width:75%;text-align:right;" id="ZJDWJE" name="ZJDWJE" fieldname="ZJDWJE" check-type="number" min="0"/>&nbsp;&nbsp;<b>（元）</b>
							</td>
							<td width="20%" colspan=2 class="right-border bottom-border">
						</tr>
						<tr>
							<th width="10%" class="right-border bottom-border text-right">资金到位备注</th>
							<td width="90%" colspan=9 class="right-border bottom-brder"><textarea class="span12" rows="3" id="ZJDWBZ" name="ZJDWBZ" fieldname="ZJDWBZ" check-type="maxlength" maxlength="4000"></textarea></td>
						</tr>
						<tr>
							<th width="10%" class="right-border bottom-border text-right">拆迁实施日期</th>
							<td width="20%" colspan=2 class="right-border bottom-border"><input class="span12 date" type="date" id="SJRQ" name="SJRQ" fieldname="SJRQ" check-type="" /></td>
							<td colspan="7" class="right-border bottom-border">
						</tr>
						<tr>
							<th width="10%" class="right-border bottom-border text-right">拆迁实施备注</th>
							<td width="90%" colspan="9" class="right-border bottom-brder"><textarea class="span12" rows="3" id="CQSSBZ" name="CQSSBZ" fieldname="CQSSBZ" check-type="maxlength" maxlength="4000"></textarea></td>
						</tr>
						<tr>
							<th width="10%" class="right-border bottom-border text-right">场地移交日期</th>
							<td width="20%" colspan=2 class="right-border bottom-border"><input class="span12 date" type="date" id="CDYJRQ" name="CDYJRQ" fieldname="CDYJRQ" check-type="" /></td>
							<td colspan=7 class="right-border bottom-border">
						</tr>
						<tr>
							<th width="10%" class="right-border bottom-border text-right">场地移交备注</th>
							<td width="90%" colspan=9 class="right-border bottom-brder"><textarea class="span12" rows="3" id="CDYJBZ" name="CDYJBZ" fieldname="CDYJBZ" check-type="maxlength=" maxlength="4000"></textarea></td>
						</tr>
						<tr>
							<th width="10%" class="right-border bottom-border text-right">土地房屋移交日期</th>
							<td width="20%" colspan=2 class="right-border bottom-border"><input class="span12 date" type="date" id="TDFWYJRQ" name="TDFWYJRQ" fieldname="TDFWYJRQ" check-type="" /></td>
							<td colspan="7" class="right-border bottom-border">
						</tr>
						<tr>
							<th width="10%" class="right-border bottom-border text-right">土地房屋移交备注</th>
							<td width="90%" colspan=9 class="right-border bottom-brder"><textarea class="span12" rows="3" id="TDFWBZ" name="TDFWBZ" fieldname="TDFWBZ" check-type="maxlength" maxlength="4000"></textarea></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	<div align="center">
		<FORM name="frmPost" method="post" id="frmPost" style="display: none"
			target="_blank">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML"> 
			<input type="hidden" name="txtXML" id="txtXML">
			<input type="hidden" name="txtFilter"  order="desc" fieldname = "lrsj"	id = "txtFilter">
			<input type="hidden" name="resultXML" id="resultXML">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
		</FORM>
	</div>
</body>
</html>