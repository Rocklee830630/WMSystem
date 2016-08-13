<!DOCTYPE HTML>
<html lang="en">
  <head>
	<%@ page language="java" pageEncoding="UTF-8"%>
	<%@ taglib uri= "/tld/base.tld" prefix="app" %>
	<%@ page import="com.ccthanking.framework.common.User"%>
    <%@ page import="com.ccthanking.framework.Globals"%>
	<app:base />
	<app:dialogs/>
<%
request.setCharacterEncoding("utf-8");
User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
String department = user.getDepartment();
String account=user.getAccount();
String username = user.getName();
%>		
</head>
<style type="text/css">
table {
	border-left: #000 solid 1px;
	border-top: #000 solid 1px;
}
td, th {
	line-height: 1.8em;
	padding: 5px;
	border-right: #000 solid 1px;
	border-bottom: #000 solid 1px;
}
input[type='text'] {
	vertical-align: middle;
	height: 20px;
	line-height: 16px;
	padding: 2px;
}
</style>
<script>


	$(function(){
		
		init();
		
		$("#printButton").click(function(){
			$(this).hide();
			window.print();
			$(this).show();
		})
	})
	
	function init(){
		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.$("#DT2").getSelectedRow();
		var obj = convertJson.string2json1(rowValue);
		$("#lqbm").html(obj.LQBM_SV);
		$("#lqrq").html(obj.LQRQ);
		$("#lqr").html(obj.LQR_SV);
		$("#fsbm").html(obj.LRBMMC);
		$("#fsrq").html(obj.LQRQ);
		if(obj.BLR_SV==null||obj.BLR_SV==""){
			$("#jbr").html(pwindow.$("#BLR").val());
		}else{
			$("#jbr").html(obj.BLR_SV);
		}
		$("#xmmc").html(pwindow.$("#XMMC").val());
		$("#zllb").html(pwindow.$("#TZLB option:selected").text());
		$("#lqfs").html(obj.FS);
		
	}
	
	
</script>
<body>

	<div>
	<span class="pull-right">
		<button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
	</span>
	</div>
	
	
<h2 align="center">资 料 领 取 单</h2>
<table width="800" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="8" align="center">长春市政府投资建设项目管理中心</td>
    </tr>
    <tr>
        <td width="215">领取部门</td>
        <td colspan="3" id="lqbm"></td>
        <td width="134">领取日期</td>
        <td colspan="3"id="lqrq"></td>
    </tr>
    <tr>
        <td>领取人</td>
        <td colspan="3" id="lqr">　</td>
        <td>领取人签字</td>
        <td colspan="3">　</td>
    </tr>
    <tr>
        <td colspan="8" align="center">领取图纸内容</td>
    </tr>
    <tr>
        <td>序号</td>
        <td colspan="3">工程名称</td>
        <td colspan="2">图纸内容（类别）</td>
        <td width="81">版本</td>
        <td width="102">份数</td>
    </tr>
    <tr>
        <td>1</td>
        <td colspan="3" id="xmmc">　</td>
        <td colspan="2" id="zllb">　</td>
        <td>　</td>
        <td id="lqfs">　</td>
    </tr>
    <tr>
        <td>2</td>
        <td colspan="3">　</td>
        <td colspan="2">　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td>3</td>
        <td colspan="3">　</td>
        <td colspan="2">　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td>4</td>
        <td colspan="3">　</td>
        <td colspan="2">　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td>5</td>
        <td colspan="3">　</td>
        <td colspan="2">　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td>6</td>
        <td colspan="3">　</td>
        <td colspan="2">　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td>发送部门</td>
        <td colspan="3" id="fsbm"></td>
        <td colspan="2">发送日期　</td>
        <td colspan="2" id="fsrq"></td>
    </tr>
    <tr>
    	<td>领导签字</td>
        <td colspan="3"></td>
        <td>经办人</td>
        <td colspan="3" id="jbr"></td>
    </tr>
</table>

	
</body>
</html>
