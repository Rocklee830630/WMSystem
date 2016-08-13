<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>

<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.framework.util.*"%>

<%
	User user = (User)session.getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String userid = user.getAccount();
	String deptid = user.getDepartment();
	String YWLX = request.getParameter("ywlx");
%>
<app:base/>
<title>审批查询</title>
<script type="text/javascript" charset="UTF-8">
 var id,json,rowindex,rowValue;
 alert(111111);
  var controllername= "${pageContext.request.contextPath }/TaskQuery.do";
	$(function() {
		var btn = $("#queryBtn");
		btn.click(function() {
	        //生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryUser&ywlx=<%=YWLX%>", data, DT1);
		});
	});
	
	function tr_click(obj,tabListid) {
		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引
		rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
		id = obj.ACCOUNT;

		$("#executeFrm").setFormValues(obj);
	//	alert(rowindex);
	}

function doOK()
{
  if(tabList2.getSelectedRowIndex()==-1)
    {
           alert('请选择一个审批流程!');
           return ;
    }
	
	var selectedRow = tabList2.getSelectedRow();
    var reValue = selectedRow.CONDITION;
    
	window.returnValue = reValue;
	
	window.close();
}

function doClose()
{
	window.returnValue="";
	window.close();
}
//-->
</SCRIPT>
</head>
<body leftmargin="10" topmargin="0">
<div class="container-fluid">
	<p>
	    <INPUT type="button" name="cmdOK" id="cmdOK" class="Button" value="#S确  认" onclick="doOK();">
        <INPUT type="button" name="cmdClose" id="cmdClose" class="Button" value="#C关  闭" onclick="doClose();">
	
	</p>
	<div class="row-fluid">
		<div class="B-small-from-table-auto">
		<h4>查询条件  <span class="pull-right"><button id="queryBtn" class="btn btn-inverse"  type="button">查询</button></span></h4>
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
				<th width="8%" class="right-border bottom-border">用户登录名</th>
				<td width="42%" class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" name="account" fieldname="ACCOUNT" operation="like" logic="and" ></td>
				<th width="8%" class="right-border bottom-border">用户姓名</th>
				<td width="42%" class="right-border bottom-border">
					<input class="span12" placeholder="" name="name" fieldname="NAME" operation="like" logic="and" ></td>
					
		        <td width="17%" class="text-left bottom-border"></td>
			</tr>
			<tr>
				<th width="8%" class="right-border bottom-border">性别</th>
				<td width="42%" class="right-border bottom-border">
					<select class="span12" name="SEX" fieldname="SEX" id="SEX" kind="dic" src="XB" operation="=" logic="and" ></select></td>
				<th width="8%" class="right-border bottom-border">所在单位</th>
				<td width="42%" colspan="2" class="bottom-border">
					<select class="span12" placeholder="" name="department" fieldname="DEPARTMENT" kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME:DEPT_PARANT_ROWID='0'" operation="="  logic="and"></select></td>
			</tr>
		</table>
		</form>
		</div>
	</div>
	
	<div class="row-fluid">
		<div class="B-small-from-table-auto">
		<h4>结果列表
			<span class="pull-right">
				<a href="#myModal" role="button" class="btn btn-inverse" id="viewBtn">预览文书</a>
				<a href="#myModal" role="button" class="btn btn-inverse" id="makeBtn">制作文书</a>
				<a href="#myModal" role="button" class="btn btn-inverse" id="spBtn">呈请审批</a>
				<a href="#myModal" role="button" class="btn btn-inverse" data-toggle="modal" id="insertBtn">添加用户信息</a>
				<a href="#myModal" role="button" class="btn btn-inverse" data-toggle="modal" id="updateBtn">修改用户信息</a>
				<a href="#myModal" role="button" class="btn btn-inverse" id="deleteBtn">删除用户信息</a>
			</span>
		</h4>
		<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
			<thead>
				<tr>
					<th name="XH" id="_XH">序号</th>
					<th fieldname="ACCOUNT" >用户登录名</th>
					<th fieldname="NAME" >用户姓名</th>
					<th fieldname="SEX" >性别</th>
					<th fieldname="DEPARTMENT" >所在单位</th>
					<th fieldname="PERSON_KIND" >用户类别</th>
				</tr>
			</thead>
		    <tbody></tbody>
		</table>
		            
		</div>
	</div>
</div>

<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id = "queryXML">
		<input type="hidden" name="txtXML" id = "txtXML">
		<input type="hidden" name="txtFilter"  order="desc" fieldname = "XMNF"	id = "txtFilter">
		<input type="hidden" name="resultXML" id = "resultXML">
		<input type="hidden" name="queryResult" id = "queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">
	</FORM>
</div>
</FORM>
</div>
</body>
</html>
