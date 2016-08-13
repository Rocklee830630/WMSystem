<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>长春市政府投资建设项目管理中心—综合管控平台</title>
</head>
<% 
  String error  = (String)request.getAttribute("error");
   if(error == null) error = "";
%>
<body>

<% 
  out.println(error);
%>
<A href='#' onclick="logout();"><U><font color=\"red\">点击我进行登录!</U></A>

<SCRIPT language="JavaScript">
 function logout(){
    parent.window.location="<%=request.getContextPath()%>/index.jsp";
 }
</script>
</body>
</html>