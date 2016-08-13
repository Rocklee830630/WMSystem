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

</head>
<body>
<app:dialogs/>
<script type="text/javascript">
	$(function(){
		$("#menuiFrame").height(getDivStyleHeight()-getTableTh(2)-10);
		$("#quickEnteriFrame").height(getDivStyleHeight()-getTableTh(2)-10);
	})
</script>
<ul class="nav nav-tabs" id="myTab">
  <li class="active"><a href="#menu" data-toggle="tab">普通菜单</a></li>
  <li><a href="#quickEnter" data-toggle="tab">快捷入口</a></li>
</ul>
<div class="tab-content">
	<div class="tab-pane active" id="menu" style="height: 100%">
		<iframe id="menuiFrame" name="menuFrame" width="100%" src="${pageContext.request.contextPath }/jsp/framework/system/role/showMenu.jsp?roleId=<%=roleId %>" style="border:0px;" framespacing=0 marginheight=0 marginwidth=0> </iframe>
	</div>
	
	<div class="tab-pane" id="quickEnter" style="height: 100%">
		<iframe id="quickEnteriFrame" name="menuFrame" width="100%" height="80%" src="${pageContext.request.contextPath }/jsp/framework/system/role/showQuickEntry.jsp?roleId=<%=roleId %>" style="border:0px;" framespacing=0 marginheight=0 marginwidth=0> </iframe>
	</div>
</div>


<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/menuController.do";

</script>

</body>
</html>