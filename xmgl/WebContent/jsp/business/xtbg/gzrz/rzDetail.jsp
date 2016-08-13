
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
<title>详细信息页面</title>

</head>
<% 
 
 User user = (User)session.getAttribute(Globals.USER_KEY);
 String loginName = user.getName();
 
 String id = request.getParameter("id");
 String sql = "SELECT DIARYID, USERID, FBSJ, NR, RZLB, JLR, JLSJ, ZHXGSJ, YWLX, SJBH, BZ, LRR, LRSJ, LRBM, LRBMMC, GXR, GXSJ, GXBM, GXBMMC, SJMJ, SFYX FROM XTBG_GZBG_GZRZ where DIARYID='"+id+"'";
 QuerySet qs = DBUtil.executeQuery(sql, null);
 
 
 String nr = null;
 java.sql.Blob dbBlob = (java.sql.Blob)qs.getObject(1, "NR");
 if(dbBlob != null) {
	 int length = (int)dbBlob.length();
	 byte[] buffer = dbBlob.getBytes(1, length);
	 nr = new String(buffer, "GBK");
 }
 String fbsj = qs.getString(1, "FBSJ");
 String fbr = qs.getString(1, "JLR");
 String starttime = qs.getString(1, "LRSJ");
 String updatetime = qs.getString(1, "GXSJ");

String fileSql = "SELECT * FROM FS_FILEUPLOAD WHERE YWID='" + id + "'";
QuerySet fileQs = DBUtil.executeQuery(fileSql, null);

%>

<app:dialogs/>
<body>
<div class="container-fluid">
    <p></p>
    <div class="row-fluid">
    	<div class="page-header"><h3 class="text-left"><small>发布时间：<%=fbsj %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发布人：<%=fbr %></small></h3></div>
        <div>
        	<p><%=nr%></p>
        </div>
    </div>
    
    <hr>
    
    <div>
    	<%
    		for(int i = 0; i < fileQs.getRowCount(); i++) {
    	%>
    	<p style=" cursor: pointer;"><img src="${pageContext.request.contextPath }/images/icon-annex.png"  title="点击查看附件"> <a target="_blank" onclick="checkupFiles('<%=fileQs.getString(i+1, "FILEID") %>')"><%=fileQs.getString(i+1, "FILENAME") %></a>&nbsp;<a href="${pageContext.request.contextPath }/fileUploadController.do?getFile&fileid=<%=fileQs.getString(i+1, "FILEID")%>" download=""><i class="icon-download-alt"></i>下载</a></p>
    	<%		
    		}
    	%>
    </div>
</div>
<!-- start 引用bootstrap --> 

<!-- end 引用bootstrap -->
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<input type="hidden" name="previewFileid" id="previewMethod">
				<input type="hidden" name="previewFileid" id="previewName">
				<input type="hidden" name="previewFileid" id="previewType">
				<input type="hidden" name="previewFileid" id="previewDir">
			</FORM>
		</div>
</body>
</html>