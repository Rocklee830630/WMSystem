<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.common.QuerySet"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%
	String wsid = request.getParameter("wsid") == null ? "" : request.getParameter("wsid");
	if(wsid==null)
	{
		wsid="";
	}
%>
<app:base/>
<title>Insert title here</title>
<script type="text/javascript">
//初始化加载
//alert($("#img1")[0].src);
$(document).ready(function() {
	var name = "<%=wsid%>";
	if(name=="")
	{
		$("#img1").attr("style","display:none");
	}		
	else
	{
		$("#img1").attr("style","display:");
		$("#img1").attr("src" , "${pageContext.request.contextPath }/img/wstemplate/"+name+".jpg");
	}

});
</script>
</head>
<body>
<app:dialogs/>


<div class="container-fluid">
  <div class="row-fluid">
    
    <div class="span7" style="width: 100%">
    	<div align="center" class="B-small-from-table-autoConcise">
	    	<img id="img1"  alt="" src="" />
		</div>
    </div>
  </div>
</div>
</body>
</html>