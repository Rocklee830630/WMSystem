<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<%
	String roleId = request.getParameter("roleId");
%>
<title>insertDemo</title>

<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/ztree_css/demo.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/ztree_css/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.all-3.5.min.js"></script>

</head>
<body>
<app:dialogs/>
<div class="container-fluid">
  <div class="row-fluid" align="center">
   	  <div class="B-small-from-table-autoConcise">
	  <h4 class="title" style="width: 260px">
	    <span class="pull-right" >
	    	<button class="btn" id="saveUserInfo">保存</button>
		    <button class="btn" id="btnCancel">关闭</button>
	    </span>
	  </h4>
	  <div class="zTreeDemoBackground left" style="height: 220px;">
	    <ul id="menuTree" class="ztree" style="height: 220px;width: 260px">
	    	<img alt="请稍后，正在加载数据……" src="${pageContext.request.contextPath }/img/loading.gif" />
	    </ul>
	  </div>
    </div>
  </div>
</div>


<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/quickEntryController.do";
var roleId = "<%=roleId %>";

var setting = {
	check: {
		enable: true
	},
	async: {
		enable: true,
		url: controllername + "?getQuickEntryTree&roleId="+roleId,
		autoParam: ["id"]
	},
	view: {
		dblClickExpand: false
	},
	data: {
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "parentId"
		}
	}
};
	
//点击保存按钮
$(function() {
	var saveUserInfoBtn = $("#saveUserInfo");
	saveUserInfoBtn.click(function() {
		var zTree = $.fn.zTree.getZTreeObj("menuTree");
		// 获取输入框被勾选的节点集合
		var nodes = zTree.getCheckedNodes(true);
		var val = "";
		for(var i = 0; i < nodes.length; i++) {
			val += nodes[i].id + ",";
		}
	//	alert(val);
		var success = defaultJson.doInsertJson(controllername + "?awardQuickEntryToRole&roleId="+roleId+"&val="+val, null, null);

	});

	$("#btnCancel").click(function(){
		parent.$(window).manhuaDialog.close();
	});
});

$(document).ready(function() {
	$.fn.zTree.init($("#menuTree"), setting);
});
</script>

</body>
</html>