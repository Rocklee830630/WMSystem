<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.codec.binary.*"%>
<%@ page import="com.ccthanking.framework.*"%>
<%@ page import="com.ccthanking.framework.service.impl.*"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="com.ccthanking.framework.common.UserTemplate"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="java.util.*"%>
<%

//模拟登录页，用于验证用户有效性后进行页面转发.eg:simulateLogin.jsp?u=&p=&l=

String username = (String)request.getParameter("u");
String password = (String)request.getParameter("p");
String url =  (String)request.getParameter("l");//转发页面
String msid = (String)request.getParameter("msid");//消息表ID
boolean result = false;
//不允许为空
if(!Pub.empty(username) && null != password && !Pub.empty(url)){
  //先对密码和url进行解密
  password = StringUtils.newStringUtf8(Base64.decodeBase64(StringUtils.getBytesUtf8(password)));
  msid = StringUtils.newStringUtf8(Base64.decodeBase64(StringUtils.getBytesUtf8(msid)));
  //byte[] bUrl = Base64.decodeBase64(StringUtils.getBytesUtf8(url));
  //url = StringUtils.newStringUtf8(bUrl);

  //对用户有效性进行判断
  UserServiceImpl usi = new UserServiceImpl();
  User user = usi.getUserByUsernameAndPassword(username,password);
  if (user != null) {//如果用户有效，写入session，转到处理页面
      request.getSession().setAttribute(Globals.USER_KEY, user);
      request.getSession().setAttribute("userId", user.getAccount());
      request.getSession().setAttribute("DEPTID", user.getOrgDept().getDeptID());
      request.getSession().setAttribute("DEPTNAME", user.getOrgDept().getDept_Name());
      //转到处理页面
      //response.sendRedirect(request.getContextPath()+url);
      response.sendRedirect(request.getContextPath()+"/jsp/framework/portal/frame.jsp?l="+url+"&msid="+msid);
  }
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>长春市政府投资建设项目管理中心—综合管控平台</title>

</head>


<body>

<%
  if(!result){
	  out.print("未经允许的访问！");
  }else{

  }
%>

</body>
</html>