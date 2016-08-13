<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="GBK"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>Preview</title>
		<style>
			body{
				margin:0px;
				widht:100%;
				height:100%;
			}
			.iframe{
				widht:100%;
				height:100%;
			}
		</style>
		<script type="text/javascript" charset="utf-8">
			$(function(){
				$("#showPreviewFrame").height(document.body.scrollHeight-5);
				doInit();
			});
			function doInit(){
				var obj = $(window.opener.document).find("#previewFileid");
				var fileid = obj.val();
				var type = $(window.opener.document).find("#previewMethod");
				var actionUrl = "";
				if(type.val()=="history"){
					var fileName = encodeURI($(window.opener.document).find("#previewName").val());
					var fileType = encodeURI($(window.opener.document).find("#previewType").val());
					var fileDir = encodeURI($(window.opener.document).find("#previewDir").val());
					actionUrl = "${pageContext.request.contextPath }/PubWS.do?getOldFjAction&filename="+fileName+"&filepath="+fileDir+"&filenametype="+fileType;
				}else{
					actionUrl = "${pageContext.request.contextPath }/fileUploadController.do?doPreview&fileid="+fileid;
				}
				$.ajax({
					url:encodeURI(actionUrl),
					data:"",
					dataType:"json",
					async:true,
					success:function(result){
						var res = encodeURI(result.msg);
						if(res!=""){
							$("#showPreviewFrame").attr("src",res);
						}
					}
				});
			}
		</script>
	</head>
	<body >
		<iframe id="showPreviewFrame" frameborder="0" style="width:100%;height:98%;overflow:auto;">
		
		</iframe>
	</body>
</html>
