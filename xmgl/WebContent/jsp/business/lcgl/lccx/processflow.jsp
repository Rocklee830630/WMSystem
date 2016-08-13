<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<script type="text/javascript" charset="utf-8">
 /*  var controllername= '${pageContext.request.contextPath }/ProcessAction.do'; */

	
     $(document).ready(function() {

   }); 
	 
</script>
</head>
<body>
	<div class="container-fluid">
		<p></p>
	<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
    
<div class="B-small-from-table-auto">
							<h4>查看信息</h4>

							<table class="B-table" width="100%">
								<tr>
									<td width="15%" align="center" colspan=3>查&nbsp;&nbsp;&nbsp;&nbsp;看&nbsp;&nbsp;&nbsp;&nbsp;信&nbsp;&nbsp;&nbsp;&nbsp;息
									</td>
								</tr>
								<tr height="50">
							
									<td align="center" id="spWs" ></td>
									<td align="center" id="splc" ></td>
									<td align="center" id="spYj"></td>
								</tr>
							</table>
</div>


<div align="center">
		<FORM name="frmPost" method="post" style="display: none"
			target="_blank">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML"> 
			<input type="hidden" name="txtXML" id="txtXML"> 
			<input type="hidden" name="resultXML" id="resultXML">
			<input type="hidden" name="txtFilter"  order="desc" fieldname = "CREATETIME"	id = "txtFilter">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
		</FORM>
	</div>
	</div>
	</div>
	</div>
</body>
</html>