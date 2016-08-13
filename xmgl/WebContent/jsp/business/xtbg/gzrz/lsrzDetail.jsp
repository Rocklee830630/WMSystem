
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

<script type="text/javascript">
function checkupFiles(m,n,o){
	var obj = $(document).find("#previewFileid");
	$("#previewMethod").val("history");
	$("#previewName").val(m);
	$("#previewType").val(n);
	$("#previewDir").val(o);
	window.open(encodeURI("${pageContext.request.contextPath }/jsp/file_upload/showPreview.jsp"));
}
</script>
</head>
<% 
 
 User user = (User)session.getAttribute(Globals.USER_KEY);
 String loginName = user.getName();
 
 String id = request.getParameter("id");
 String sql = "select T.DAYID, T.DAYDATE, T.DAYIEMPNAME, T.DAYCONTENT, T.DAYIDATE, T.DAYMDATE, T.DAYTYPE from TOA_DAILY T where T.DAYID='"+id+"'";
 QuerySet qs = DBUtil.executeQuery(sql, null);
 
 
 String nr = null;
 java.sql.Blob dbBlob = (java.sql.Blob)qs.getObject(1, 4);
 if(dbBlob != null) {
	 int length = (int)dbBlob.length();
	 byte[] buffer = dbBlob.getBytes(1, length);
	 nr = new String(buffer, "GBK");
 }
 String fbsj = qs.getString(1, 2);
 String fbr = qs.getString(1, 3);
 String starttime = qs.getString(1, 5);
 String updatetime = qs.getString(1, 6);

String fileSql = "SELECT accnname,acconame,accdir FROM TAC_ACCESSORY WHERE ACCBELONGID='" + id + "'";
QuerySet fileQs = DBUtil.executeQuery(fileSql, null);

%>

<app:dialogs/>
<body>
<div class="container-fluid">
    <p></p>
    <div class="row-fluid">
    	<div class="page-header"><h3 class="text-left"><small>发布时间：<%=fbsj %> 记录时间：<%=starttime %> 发布时间：<%=updatetime %> 发布人：<%=fbr %></small></h3></div>
        <div>
        	<p><%=nr%></p>
        </div>
    </div>
    
    <hr>
    
    <div>
    	<%
    		for(int i = 0; i < fileQs.getRowCount(); i++) {
    	%>
    	<p style=" cursor: pointer;"><img src="${pageContext.request.contextPath }/images/icon-annex.png"  title="点击查看附件"> <a target="_blank" onclick="checkupFiles('<%=fileQs.getString(i+1, 1) %>','<%=fileQs.getString(i+1, 2) %>','<%=fileQs.getString(i+1, 3) %>')"><%=fileQs.getString(i+1, 2) %></a>&nbsp;<a href="javascript:void(0);" download=""><i class="icon-download-alt"></i>下载</a></p>
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