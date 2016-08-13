<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%
	String ywid = request.getParameter("ywid");
    String lb   = request.getParameter("lb");
    if(ywid==null) ywid = "";
	String file_sql = "select  FILEID,FILENAME,BZ,URL from fs_fileupload where ywid = '"+ywid+"' ";
	if(lb!=null&&!"".equals(lb)){
		if(lb.indexOf(",")!=-1){
			lb = lb.replaceAll("\\,","\\','");
			file_sql +=" and fjlb in('"+lb+"')";
		}else{
			file_sql +=" and fjlb='"+lb+"'";
		}
	}
	String[][] a =DBUtil.query(file_sql);
   	String[] fileid = null;
   	String[] filenames = null;
   	String id=null;
%>  

<app:base/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap-carousel.js"></script>
<style>
	.carousel-indicators > li  {background-color:gray;}
	.carousel-indicators .active{
		background-color:orange;
	}
</style>
</head>
<body>

<div id="myCarousel" class="carousel slide">
  <ol class="carousel-indicators">
  <%
	if(a!=null && a.length>0){
		for (int i=0;i<a.length;i++){
			if(0 == i){
  %>
    <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
  <%
			continue;
			}
  %>
    <li data-target="#myCarousel"  data-slide-to="<%=i%>"></li>
  <%}}%>
  </ol>
  <!-- Carousel items -->
  <div class="carousel-inner" id="picViewDiv">
		<%
	    	if(a!=null&&a.length>0)
	    	{
	    		filenames = new String[a.length];
	    		fileid = new String[a.length];
	    		for (int i=0;i<a.length;i++){
	    			if(0 == i){
	      %>
			   <div class="active item" align="center">
			    	 <img src="${pageContext.request.contextPath }/fileUploadController.do?getFile&fileid=<%=a[i][0]%>" alt="">
			   </div>
	    
	      <%
			continue;
			}
  		%>
				    <div class="item" align="center">
						<img src="${pageContext.request.contextPath }/fileUploadController.do?getFile&fileid=<%=a[i][0]%>">
				    </div>
	    <% 			
	     		}		
	    	}
		%> 		   
  </div>
  <!-- Carousel nav -->
  <a class="carousel-control left" href="#myCarousel" data-slide="prev">&lsaquo;</a>
  <a class="carousel-control right" href="#myCarousel" data-slide="next">&rsaquo;</a>
</div>

<script type="text/javascript" charset="utf-8">


$(function(){
	var height = $(window).manhuaDialog.getParentObj().parent.openWindowHeight;
	$("img").css("height",(height - 170+"px"));
	$('.carousel').carousel({
		  interval: 4000
		})
});

</script>

</body>
</html>