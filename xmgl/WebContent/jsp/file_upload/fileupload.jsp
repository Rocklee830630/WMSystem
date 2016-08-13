<!DOCTYPE HTML>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri= "/tld/base.tld" prefix="app" %>
		<app:base/>
		<title>附件上传页面</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<!-- fileUpload css -->
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/file_upload/css/style.css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/file_upload/css/bootstrap-responsive.min.css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/file_upload/css/bootstrap-image-gallery.css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/file_upload/css/jquery.fileupload-ui.css">
		<!-- fileUpload css -->
		
	</head>

	<body onload="doInit();">
		<div id="topDiv">
		<div class="row-fluid">
			<!-- The file upload form used as target for the file upload widget -->
			<form id="fileupload"
				action="${pageContext.request.contextPath }/UploadServlet"
				method="POST" enctype="multipart/form-data">
				<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->

				<div class="row fileupload-buttonbar">
					<div class="span7">
						<!-- The fileinput-button span is used to style the file input field as button -->
						<span class="btn btn-success fileinput-button"> <i
							class="icon-plus icon-white"></i> <span>添加文件...</span> <input
								type="file" name="files[]" multiple> </span>
						<button type="submit" class="btn btn-primary start">
							<i class="icon-upload icon-white"></i>
							<span>全部上传</span>
						</button>
						<button type="reset" class="btn btn-warning cancel">
							<i class="icon-ban-circle icon-white"></i>
							<span>取消上传</span>
						</button>
						<button type="button" class="btn btn-danger delete">
							<i class="icon-trash icon-white"></i>
							<span>批量删除</span>
						</button>
						<input type="checkbox" class="toggle">
					</div>
					<!-- The global progress information -->
					<div class="span5 fileupload-progress fade">
						<!-- The global progress bar -->
						<div class="progress progress-success progress-striped active"
							role="progressbar" aria-valuemin="0" aria-valuemax="100">
							<div class="bar" style="width: 0%;"></div>
						</div>
						<!-- The extended global progress information -->
						<div class="progress-extended">
							&nbsp;
						</div>
					</div>
				</div>
				<!-- The loading indicator is shown during file processing -->
				<div class="fileupload-loading"></div>
				<br>
				<!-- The table listing the files available for upload/download -->
				<table role="presentation" class="table table-striped">
					<tbody class="files" data-toggle="modal-gallery"
						data-target="#modal-gallery">

					</tbody>
				</table>
			</form>
		</div>
		<div id="modal-gallery" class="modal modal-gallery hide fade"
			data-filter=":odd">
			<div class="modal-header">
				<a class="close" data-dismiss="modal">&times;</a>
				<h3 class="modal-title"></h3>
			</div>
			<div class="modal-body">
				<div class="modal-image"></div>
			</div>
			<div class="modal-footer">
				<a class="btn modal-download" target="_blank"> <i
					class="icon-download"></i> <span>预览图片/下载</span> </a>

				<a class="btn btn-success modal-play modal-slideshow"
					data-slideshow="5000"> <i class="icon-play icon-white"></i> <span>幻灯片播放</span>
				</a>
				<a class="btn btn-info modal-prev"> <i
					class="icon-arrow-left icon-white"></i> <span>上一个</span> </a>
				<a class="btn btn-primary modal-next"> <span>下一个</span> <i
					class="icon-arrow-right icon-white"></i> </a>
			</div>
		</div>
		<div style="display: none">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" id="xmid">
				<input type="hidden" id="sjbh">
				<input type="hidden" id="ywlx">
			</FORM>
		</div>
		<script id="template-upload" type="text/x-tmpl">
            {% for (var i=0, file; file=o.files[i]; i++) { %}
        <tr class="template-upload fade">
            <td class="preview"><span class="fade"></span></td>
			
			{% if(file.name.length>16){ %}
            	<td class="name"><span>{%=file.name.substring(0,file.name.length/2)%}<br/>{%=file.name.substring((file.name.length/2),file.name.length)%}</span></td>
			{% }else{ %}  
				<td class="name"><span>{%=file.name%}</span></td>
            {% }%}          
			<td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
            {% if (file.error) { %}
            <td class="error" colspan="2"><span class="label label-important">Error</span> {%=file.error%}</td>
            {% } else if (o.files.valid && !i) { %}
            <td>
                <div class="progress progress-success progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="bar" style="width:0%;"></div></div>
            </td>
            <td class="start">{% if (!o.options.autoUpload) { %}
                <button class="btn btn-primary">
                    <i class="icon-upload icon-white"></i>
                    <span>上传</span>
                </button>
                {% } %}</td>
            {% } else { %}
            <td colspan="2"></td>
            {% } %}
            <td class="cancel">{% if (!i) { %}
                <button class="btn btn-warning">
                    <i class="icon-ban-circle icon-white"></i>
                    <span>取消</span>
                </button>
                {% } %}</td>
        </tr>
        {% } %}
    </script>
		<!-- The template to display files available for download -->
		<script id="template-download" type="text/x-tmpl">
        {% for (var i=0, file; file=o.files[i]; i++) { %}
        <tr class="template-download fade">
            {% if (file.error) { %}
            <td></td>
			{% if(file.name.length>16){ %}
            	<td class="name"><span>{%=file.name.substring(0,file.name.length/2)%}<br/>{%=file.name.substring((file.name.length/2),file.name.length)%}</span></td>
            {% }else{ %}
				<td class="name"><span>{%=file.name%}</span></td>
            {% }%}
			<td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
            <td class="error" colspan="2"><span class="label label-important">Error</span> {%=file.error%}</td>
            {% } else { %}
            <td class="preview">{% if (file.thumbnail_url) { %}
                <a href="{%=file.url%}" title="{%=file.name%}" rel="gallery" download="{%=file.name%}"><img src="{%=file.thumbnail_url%}"></a>
                {% } %}</td>
            <td class="name">
			{% if(file.name.length>16){ %}
                <a href="{%=file.url%}" title="{%=file.name%}" rel="{%=file.thumbnail_url&&'gallery'%}" download="{%=file.name%}">{%=file.name.substring(0,file.name.length/2)%}<br/>{%=file.name.substring((file.name.length/2),file.name.length)%}</a>
			{% }else{ %} 
				<a href="{%=file.url%}" title="{%=file.name%}" rel="{%=file.thumbnail_url&&'gallery'%}" download="{%=file.name%}">{%=file.name %}</a>
            {% }%}           
			</td>
            <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
            <td colspan="2"></td>
            {% } %}
            <td class="delete">
                <button class="btn btn-danger" data-type="{%=file.delete_type%}" data-url="{%=file.delete_url%}"{% if (file.delete_with_credentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
                        <i class="icon-trash icon-white"></i>
                    <span>删除</span>
                </button>
                <input type="checkbox" name="delete" value="1">
            </td>
        </tr>
        {% } %}
    </script>
    	<script
			src="${pageContext.request.contextPath }/file_upload/js/vendor/jquery.ui.widget.js"></script>
		<script
			src="${pageContext.request.contextPath }/file_upload/js/tmpl.min.js"></script>
		<script
			src="${pageContext.request.contextPath }/file_upload/js/load-image.min.js"></script>
		<script
			src="${pageContext.request.contextPath }/file_upload/js/canvas-to-blob.min.js"></script>
		<script
			src="${pageContext.request.contextPath }/file_upload/js/bootstrap-image-gallery.min.js"></script>
		<script
			src="${pageContext.request.contextPath }/file_upload/js/jquery.iframe-transport.js"></script>
		<script
			src="${pageContext.request.contextPath }/file_upload/js/jquery.fileupload.js"></script>
		<script
			src="${pageContext.request.contextPath }/file_upload/js/jquery.fileupload-fp.js"></script>
		<script
			src="${pageContext.request.contextPath }/file_upload/js/jquery.fileupload-ui.js"></script>
		<script
			src="${pageContext.request.contextPath }/file_upload/js/locale.js"></script>
		<script
			src="${pageContext.request.contextPath }/file_upload/js/main.js"></script>
		</div>
	</body>

	<script type="text/javascript">
	/**
	$(document).ready(function(){
		doInit();
	});
	*/
	//----------------------------
	//-调整窗口大小时，同时调整iframe大小
	//----------------------------
	$(window).resize(function(){
		resizeparentFrame();
	});
	//----------------------------
	//-滚动条滚动时，同时调整iframe大小
	//----------------------------
	$(window).scroll(function(){
		resizeparentFrame();
	});
	/**
	$(function(){
		var lazyheight = 0;
		//获取数据
		function showload(){ 
			lazyheight = parseFloat($(window).height()) + parseFloat($(window).scrollTop()); 
			if ($(document).height()-100 <= lazyheight) { 
				alert($(document).height());
			} 
		}
		//showload();
		//绑定事件
		$(window).bind("scroll", function(){ 
			//当滚动条滚动时
			showload();
		}); 
	});
	*/
	var controllername= "${pageContext.request.contextPath }/fileUploadController.do";
	$(function() {
	});
	function queryData(n){
		//var data1 = combineQuery.getQueryCombineData(queryForm,frmPost);
		var data = "";
		$.ajax({
			url : controllername+"?queryFileList",
			data : n,
			cache : false,
			async :	false,
			dataType : "json",
			success : function(result) {
				var files = eval('(' + result.msg + ')');
				for(var i=0;i<files.length;i++){
					var file = files[i];
					var showHtml = "";
					showHtml +="<tr class='template-download'><td class='preview'>";
					//如果不是图片，那么不使用gallery功能
					if(file.fileType.indexOf("image")!=-1){
						showHtml +="<a href='"+file.url+"' title='"+file.name+"' rel='gallery' download='"+file.name+"'><img src='"+file.thumbnail_url+"'></a>";
					}else{
						showHtml +="<a href='"+file.url+"' title='"+file.name+"'  download='"+file.name+"'><img src='"+file.thumbnail_url+"'></a>";
					}
					showHtml +="</td>";
					showHtml +="<td class='name'>";
					if(file.name.length>16){
						showHtml +="<a href='"+file.url+"' title='"+file.name+"' rel='gallery' download='"+file.name+"'>"+file.name.substring(0,file.name.length/2)+"<br/>"+file.name.substring((file.name.length/2),file.name.length)+"</a>"
					}else{
						showHtml +="<a href='"+file.url+"' title='"+file.name+"' rel='gallery' download='"+file.name+"'>"+file.name+"</a>"
					}
					showHtml +="</td>";
					showHtml +="<td class='size'><span>"+file.size+"</span></td>";
					showHtml +="<td colspan='2'></td>";
					showHtml +="<td class='delete'>";
					showHtml +="<button class='btn btn-danger' data-type='"+file.delete_type+"' data-url='"+file.delete_url+"'>";
					showHtml +="<i class='icon-trash icon-white'></i>";
					showHtml +="<span>删除</span>";
					showHtml +="</button>&nbsp;";
					showHtml +="<input type='checkbox' name='delete' value='1'>"
					showHtml +="</td>";
					showHtml +="</tr>";
					$(".files").append(showHtml);
				}
				resizeparentFrame();
			}
		});
	}
	function doInit(){
		//add by zhangbr@ccthanking.com 添加对项目信息的获取
		//如果无法获取项目ID，则方法停止执行
		var ___resultXML = $("#resultXML",parent.document);
		var ___resultJson = eval('(' + ___resultXML.val() + ')');
		var fileUploadObj = new Object();
		if(___resultJson.data){
			$("#xmid").val(___resultJson.data[0].ID);
			$("#sjbh").val(___resultJson.data[0].SJBH);
			$("#ywlx").val(___resultJson.data[0].YWLX);
			fileUploadObj.XMID = ___resultJson.data[0].ID;
			fileUploadObj.SJBH = ___resultJson.data[0].SJBH;
			fileUploadObj.YWLX = ___resultJson.data[0].YWLX;
		}else{
			$("#xmid").val(___resultJson.ID);
			$("#sjbh").val(___resultJson.SJBH);
			$("#ywlx").val(___resultJson.YWLX);
			fileUploadObj.XMID = ___resultJson.ID;
			fileUploadObj.SJBH = ___resultJson.SJBH;
			fileUploadObj.YWLX = ___resultJson.YWLX;
		}
		if($("#xmid").val()==""){
			alert("无法获取项目编号");
			return;
		}
		//add END
		var xmid = $("#xmid").val();
		if(___resultJson.QUERYFLAG!=undefined){
			fileUploadObj.SJBH = "";
			fileUploadObj.YWLX = "";
		}else{
			fileUploadObj.FILEID = ___resultJson.FILEID;
		}
		var data1 = JSON.stringify(fileUploadObj);
		var data = defaultJson.packSaveJson(data1);
		if(xmid!=undefined && xmid!=""){
			queryData(data);
		}
	}
	function resizeparentFrame(){
		var topDivObj = document.getElementById("topDiv");
		var hh = document.body.scrollHeight+90;
		hh = topDivObj.scrollHeight+10;
		var ww = document.body.scrollWidth+80;
		window.parent.resizeIframe(ww,hh);
	}
	</script>
</html>
