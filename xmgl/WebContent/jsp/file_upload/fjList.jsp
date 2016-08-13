<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>

<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>下达统筹计划</title>
<%
	String ywid = request.getParameter("ywid");
    String lb   = request.getParameter("lb");
    if(ywid==null) ywid = "";
	String file_sql = "select  FILEID,FILENAME,BZ from fs_fileupload where ywid = '"+ywid+"' and fjzt='1' ";
	if(lb!=null&&!"".equals(lb)){
		if(lb.indexOf(",")!=-1){
			lb = lb.replaceAll("\\,","\\','");
			file_sql +=" and fjlb in('"+lb+"')";
		}else{
			file_sql +=" and fjlb='"+lb+"'";
		}
	}
	String[][] a =DBUtil.query(file_sql);
    //	String fileid = "";
    	String[] fileid = null;
    	String[] filenames = null;
    	if(a!=null&&a.length>0)
    	{
    		filenames = new String[a.length];
    		fileid = new String[a.length];
    		
    		for (int i =0;i<a.length;i++)
    		{
    			if(!Pub.empty(a[i][2]))
    			{
    				filenames[i] = a[i][2];
    			}else{
    				fileid[i] = a[i][0];
    				filenames[i] = a[i][1];
    			}
    		}
    		
    	}
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
	   	     if(fileid!=null){
	   	    	 for(int i = 0;i<fileid.length;i++){
	   	    		 String id = fileid[i];
	   	    		 String name = filenames[i];
	   	    		 %>
	   	    		<tr>
	   	    		  <td><%=name%> </td>
	   	    		  <td>
	   	    		  <a href="javascript:void(0);" onclick="checkupFiles('<%=id%>')"><i class="icon-zoom-in"></i> 预览</a>&nbsp;&nbsp;&nbsp;&nbsp;
	   	    		  <a href="${pageContext.request.contextPath }/fileUploadController.do?getFile&fileid=<%=id%>" download=""><i class="icon-download-alt"></i>下载</a>
	   	    		  </td>
	   	    		</tr> 
	   	 <%   		 
	   	    		 
	   	    	 }
	   	     }
	   	  %>
	   	
	   
	   </table>
	 </div>
    </div>
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "PCH,XMBH"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="ywid" id = "ywid" value="">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>