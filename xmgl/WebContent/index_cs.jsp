<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", -10);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>长春市政府投资建设项目管理中心—综合管控平台</title>
<link href="${pageContext.request.contextPath }/css/bootstrap.css" rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath }/css/base.css" rel="stylesheet" media="screen">
<script src="${pageContext.request.contextPath }/js/base/jquery.js"></script>
<script src="${pageContext.request.contextPath }/js/base/bootstrap.js"></script>
<link rel="icon" href="${pageContext.request.contextPath }/favicon.ico" type="image/x-icon" /> 
<link rel="shortcut icon" href="${pageContext.request.contextPath }/favicon.ico" type="image/x-icon" />
</head>
<%
 String error = (String)request.getAttribute("error");
  if(error==null)
  {
	  error = "";
  }
%>

<body class="body_login_bg">
<div id="MessageModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h3 id="MessageModalTitle">提示信息</h3>
  </div>
  <div class="modal-body" id="MessageModalContent">
    <%=error %>
  </div>
  <div class="modal-footer">
    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
  </div>
</div>
<div class="loginBox">
	<div class="loginBoxLeft">
        <div class="loginBoxLeft_1"><img src="${pageContext.request.contextPath }/images/login/loginBoxLeft_1.png"  alt="综合管控平台"/></div>
        <div class="loginBoxLeft_2"><a href="#"><img src="${pageContext.request.contextPath }/images/login/loginBoxLeft_2.png"  alt="支持chrome浏览器，1366*768及以上分辨率"/></a></div>
        <div class="loginBoxLeft_3"><a href="#"><img src="${pageContext.request.contextPath }/images/login/loginBoxLeft_3.png"  alt="统计数据 如此轻松"/></a></div>
        <div class="loginBoxLeft_4"><a href="#"><img src="${pageContext.request.contextPath }/images/login/loginBoxLeft_4.png"  alt="帮助"/></a></div>
        <div class="loginBoxLeft_5"><a href="#"><img src="${pageContext.request.contextPath }/images/login/loginBoxLeft_5.png"  alt="找回密码"/></a></div>
    </div>
    <form method="post" action="${pageContext.request.contextPath }/userController.do?login" id="loginForm" >
    
    <div class="loginBoxRight">
    	<h3>用户登录<font style="color:red;font-weight:bold;">（测试环境）</font></h3>
        <div class="loginBoxRightRow">
        	<span class="text-center"><i class="icon-user"></i></span>
        	<input type="text"  name="username" id="username"  value="${username }"  placeholder="请输入用户名">
            </div>
            <div class="loginBoxRightRow">
        	<span class="text-center"><i class="icon-lock"></i></span>
        	<input type="password" name="password" id="password"  placeholder="请输入密码">
        </div>
        <div class="loginBoxRightRow"><button id="login">登 录</button></div>
        <div class="pull-right">
        	<button	class="btn btn-link" type="button" onclick="download()"><font style="color:red;">浏览器等安装文件和设置说明下载</font></button>
        </div>
    </div>
    </form>
</div>
</body>


</html>
<script  type="text/javascript">

function download(){
	window.open("${pageContext.request.contextPath }/setup.zip");
}

function setAlertHTML()
{
	var alertHTML  = "<div id=\"pubAlert\" class=\"alert alert-block block-b-open\">";
	    alertHTML += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\"></button>";
        alertHTML += "<h4>提示信息</h4>";
        alertHTML += "<span class=\"alertContent\"></span >";
        alertHTML += "</div>";
    $("body").prepend(alertHTML);
}
$(document).ready(function(){
	  setAlertHTML();
	  $("#username").focus();
	<% if(error.length()>0){%>
	  $("#pubAlert").find("span").html("<%=error %>");

	  $("#pubAlert").slideDown('fast').delay(2000).slideUp('fast');
	<%}%>
	var path='${pageContext.request.contextPath }';
	
	$("#username").keydown(function(event){ 
		if(event.keyCode==13){ 
			$("#password").focus();
		}
	});
	$("#password").keydown(function(event){ 
		if(event.keyCode==13){ 
			$("#loginForm").submit();
		}
	});

	$("#login").click(function() {
		var username = $("#username").val();
		if(username=="")
		{
			$("#pubAlert").find("span").html("请输入用户名！");
			$("#pubAlert").slideDown('fast').delay(2000).slideUp('fast');		
			$("#username").focus();
			return false;
		}
		var password = $("#password").val();
		$("#loginForm").submit();
		    
	});

});

</script>