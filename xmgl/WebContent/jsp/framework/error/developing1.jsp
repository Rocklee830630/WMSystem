<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${pageContext.request.contextPath }/css/bootstrap.css" rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath }/css/base.css" rel="stylesheet" media="screen">
<title>正在开发中</title>
</head>
<body class="developmentBg">
<div class="developmentIng"><img src="${pageContext.request.contextPath }/images/developmentIng.jpg" alt=""/></div>
<div class="QuickLinks">
  	<h3>快速入口</h3>
  	<div> 
		<app:quickEntry/>
	</div>
</div>
</body>
</html>