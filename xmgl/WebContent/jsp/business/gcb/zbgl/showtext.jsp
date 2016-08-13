
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.common.QuerySet"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.Globals" %>
<%@ page import="com.ccthanking.framework.common.User" %>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link href="${pageContext.request.contextPath }/css/bootstrap.css" rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath }/css/base.css" rel="stylesheet" media="screen">
<title>复制信息页面</title>
</head>
<% 
 String zdname = request.getParameter("zdname");
 String id = request.getParameter("zbid");
 String title = request.getParameter("title");
 String sql = "select "+zdname+" ,nvl(xmbh,' ') xmbh,nvl(xmmc,' ') xmmc,nvl(bdbh,' ') bdbh ,nvl(bdmc,' ') bdmc,kssj,jssj from gc_xmglgs_zbb where GC_XMGLGS_ZBB_ID='"+id+"'";
 QuerySet qs = DBUtil.executeQuery(sql, null);
 String nr = null;
 String xmmc=qs.getString(1, "XMMC");
 String bdbh=qs.getString(1, "BDBH");
 String bdmc=qs.getString(1, "BDMC");
 java.sql.Blob dbBlob = (java.sql.Blob)qs.getObject(1, zdname);
 if(dbBlob != null) {
	 int length = (int)dbBlob.length();
	 byte[] buffer = dbBlob.getBytes(1, length);
	 nr = new String(buffer, "GBK");
	 nr=nr.replace("\n", "<br>");
 }
%>
<app:dialogs/>
<body>
<div class="container-fluid">
    <p></p>
    <div class="row-fluid">
        <div class="page-header">
        <h4  class="text-center"><%=title%></h4>
        <h5>项目名称：<%=xmmc%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;标段名称：<%=bdmc%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;标段编号：<%=bdbh%></h5>
        </div>
        <div>
        	<p><%=nr%></p>
        </div>
    </div>
    <hr>
</div>
<form action="" name="frmPost" style="display: none"></form>
</body>
</html>