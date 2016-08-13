<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%
	//String nd= request.getParameter("nd");
	//int year=Integer.parseInt(nd)+1;
	
%>
<app:base/>
<title>结转>年度设置</title>
<script type="text/javascript" charset="utf-8">

var controllerinsert= "${pageContext.request.contextPath }/xmcbk/xmcbkxdController.do";	

//初始化加载
$(document).ready(function(){
	var year=new Date().getFullYear()
	var num=document.getElementsByTagName("option");
	num[0].innerHTML=year;
	num[1].innerHTML=year+1;
});
//确定
$(function() {	
		var btn = $("#example_ok");
		btn.click(function() {
 			$(window).manhuaDialog.setData($("#ND").val());
			$(window).manhuaDialog.sendData();
			$(window).manhuaDialog.close();
		});
	});
function docancel()
{
	$(window).manhuaDialog.setData('0');
	$(window).manhuaDialog.sendData();
	$(window).manhuaDialog.close();
}
</script>      
</head>
<body>
<app:dialogs/>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
	      		<h4 class="title">
	      			结转年份
					<span class="pull-right">
						<button	id="example_ok" class="btn"  type="button">确定</button>
						<button	onclick="docancel()" class="btn" type="button">取消</button>
					</span>    			
	      		</h4>			
				<form method="post" id="demoForm">
					<input type="hidden" class="span12" type="text" fieldname="ID" id="ID"/>
					<table class="B-table" id="DT1" >
						<tr>
							<th width="5%" class="right-border bottom-border">年度</th>
							<td width="7%" class="right-border bottom-border">
								<select id="ND" class="span12 year" name="ND" fieldname="ND" operation="=">
									<option id="now"></option>
									<option id="next"></option>
								</select>
							</td>
							<td width="75%"></td>		
						</tr>
					</table>
				</form>
			</div>
		</div>									
		</P>
		<font style="color:red;font-weight:bold;">您确定要对所选项目进行结转吗！</font>
				
	</div>
	<div align="center">
		<FORM name="frmPost" method="post" style="display: none" target="_blank">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML"/> 
			<input type="hidden" name="txtXML"/>
			<input type="hidden" name="txtFilter" order="desc" value="LRSJ,DPZBH"/>
			<input type="hidden" name="resultXML"/>
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData"/>
		</FORM>
	</div>
</body>
</html>