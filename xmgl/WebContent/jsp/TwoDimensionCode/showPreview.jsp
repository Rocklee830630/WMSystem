<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="GBK"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>Preview</title>
		<%
			String id = request.getParameter("id");
		%>
		<style>
			body{
				margin:0px;
				width:100%;
				height:100%;
			}
			.iframe{
				widht:100%;
				height:100%;
			}
		</style>
		<script type="text/javascript" charset="utf-8">
			$(function(){
				//doInit();
			});
			function doInit(){
				var	actionUrl = '${pageContext.request.contextPath }/fileUploadController.do?doPreviewEwm&id=<%=id%>';
				$.ajax({
					url:actionUrl,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						var res = result.msg;
						if(res!=""){
							$("#showPreviewFrame").attr("src",res);
						}
					}
				});
			}
		</script>
	</head>
	<body style="text-align: center;">
		<div align="center">
		<img   src = "${pageContext.request.contextPath }/images/TwoDimensionCode/<%=id%>.png"">
		
		</img>
		</div>
	</body>
</html>
