<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.common.QuerySet"%>

<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>

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
<title>下达统筹计划</title>
<%
	String id = request.getParameter("id");
	String file_sql = "SELECT accnname,acconame,accdir FROM TAC_ACCESSORY WHERE ACCBELONGID='" + id + "'";
	QuerySet fileQs = DBUtil.executeQuery(file_sql, null);
    
%>  
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	 <div class="row-fluid">
    	<div class="B-small-from-table-auto">
    	
    	<table width="100%" class="table table-striped">
        <thead>
        <tr>
        <th width="60%">附件名称</th>
        <th>操作</th>
        </tr>
        </thead>
	   	  <%
	   	    	 for(int i = 0;i<fileQs.getRowCount();i++){
	   	    		 String wsName = fileQs.getString(i+1, 1);
	   	    		 String name = fileQs.getString(i+1, 2);
	   	    		 String accdir = fileQs.getString(i+1, 3);
	   	    		 %>
	   	    		<tr>
	   	    		  <td><%=name%> </td>
	   	    		  <td>
	   	    		  <a href="javascript:void(0);" onclick="checkupFiles('<%=wsName %>','<%=name %>','<%=accdir %>')"><i class="icon-zoom-in"></i> 预览</a>&nbsp;&nbsp;&nbsp;&nbsp;
	   	    		<%--   <a href="${pageContext.request.contextPath }/fileUploadController.do?getFile&fileid=<%=id%>" download=""><i class="icon-download-alt"></i>下载</a> --%>
	   	    		  </td>
	   	    		</tr> 
	   	 <%   		 
	   	    		 
	   	    	 }
	   	  %>
	   	
	   
	   </table>
	 </div>
    </div>
</div>
<div align="center">
	
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<input type="hidden" name="previewFileid" id="previewMethod">
				<input type="hidden" name="previewFileid" id="previewName">
				<input type="hidden" name="previewFileid" id="previewType">
				<input type="hidden" name="previewFileid" id="previewDir">
			</FORM>
		</div>
</div>
</body>
</html>