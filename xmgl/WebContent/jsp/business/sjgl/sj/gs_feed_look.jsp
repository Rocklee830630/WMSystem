<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat"%>
<app:base />
<title></title>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/sjgl/gs/gsController.do";
  //var fd,sd;
 	//页面初始化
  $(function() {
	  setValue();
	});
	//页面初始化赋值
	function setValue(){
		var json=do_value();
		$("#gsForm").setFormValues(json);
		$("#BZ").val($(json).attr("BZ_GS"));
	}
	
	function do_value(){
		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.getValue();
		var obj = convertJson.string2json1(rowValue);
		return obj;
	}

</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid" style="width:98%">
	<p></p>
	<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      
				<form method="post" id="gsForm">
					<table class="B-table" width="100%">
	        			<tr>
							<th width="8%" class="right-border bottom-border text-right">送审时间</th>
							<td width="42%" class="bottom-border">
								<input class="span12" type="text" id="SSJS" name="SSJS" fieldname="SSJS" disabled/>
							</td>
							<th width="8%" class="right-border bottom-border text-right">送审金额</th>
							<td width="42%" class="bottom-border">
								<input class="span12" type="number" id="SSE" check-type="" name="SSE" fieldname="SSE" style="width:75%;text-align:right;" min="0" disabled/>&nbsp;&nbsp;<b>（元）</b>
							</td>
						</tr>
	        			<tr>
							<th width="8%" class="right-border bottom-border text-right">批复时间</th>
							<td width="42%" class="bottom-border">
								<input class="span12" type="text" id="CBSJPF" check-type="" name="CBSJPF" fieldname="CBSJPF" disabled/>
							</td>
							<th width="8%" class="right-border bottom-border text-right">金额</th>
							<td width="42%" class="bottom-border">
								<input class="span12" type="number" id="GS" check-type="" name="GS" fieldname="GS" style="width:75%;text-align:right;" min="0" disabled/>&nbsp;&nbsp;<b>（元）</b>
							</td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">批复内容</th>
							<td width="92%" colspan="3" class="bottom-border">
								<textarea class="span12" rows="4" id="" check-type="maxlength" name="PFNR" fieldname="PFNR" maxlength="4000" disabled></textarea>
							</td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">备注</th>
							<td width="92%" colspan="3" class="bottom-border">
								<textarea class="span12" rows="4" id="BZ" check-type="maxlength" name="BZ" fieldname="BZ" maxlength="4000" disabled></textarea>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
			</div>
	<div align="center">
		<FORM name="frmPost" method="post" style="display: none"
			target="_blank">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML"> 
			<input type="hidden" name="txtXML" id="txtXML"> 
			<input type="hidden" name="queryResult" id = "queryResult" />
			<input type="hidden" name="resultXML" id="resultXML">
			<input type="hidden" name="txtFilter"  order="desc" fieldname = "pxh" id = "txtFilter">
			<input type="hidden" name="queryResult" id ="queryResult">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
		</FORM>
	</div>
</body>
</html>