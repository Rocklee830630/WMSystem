<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.service.impl.MenuServiceImpl"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@page import="com.ccthanking.framework.params.SysPara.SysParaConfigureVO"%>
<%@page import="com.ccthanking.framework.params.ParaManager"%>
<%@page import="com.ccthanking.common.YwlxManager"%>
<%@page import="com.ccthanking.framework.coreapp.orgmanage.UserManager"%>
<%@page import="org.apache.commons.codec.binary.*"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", -10);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>长春市政府投资建设项目管理中心—综合管控中心</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<LINK type="text/css" rel="stylesheet" href="/xmgl/css/style.css"> </LINK>
<LINK type="text/css" rel="stylesheet" href="/xmgl/css/style-responsive.css"> </LINK>
<LINK type="text/css" rel="stylesheet" href="/xmgl/css/bootstrap.css"> </LINK>
<LINK type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/css/base.css"> </LINK>
<LINK type="text/css" rel="stylesheet" href="/xmgl/css/default.css"> </LINK>
<link type="text/css" rel="stylesheet" href="/xmgl/css/perfect-scrollbar.min.css" rel="stylesheet" id="style_color"/>
<link rel="icon" href="${pageContext.request.contextPath }/favicon.ico" type="image/x-icon" /> 
<link rel="shortcut icon" href="${pageContext.request.contextPath }/favicon.ico" type="image/x-icon" /> 


</head>
<body class="MainBodyBg">
<app:dialogs />
<%
  User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
  OrgDept dept = user.getOrgDept();
  String deptId = dept.getDeptID();
  String deptName = dept.getDept_Name();
  String bmjc = dept.getBmjc();
  String userid = user.getAccount();
  String username = user.getName();
  String sysdate = Pub.getDate("yyyy-MM-dd HH:mm:ss");
  SysParaConfigureVO syspara = (SysParaConfigureVO) ParaManager
			.getInstance().getSysParameter("DesktopRemindTime");
  String miao = "10";
  if (syspara != null&& !"".equals(syspara.getSysParaConfigureParavalue1())) {
		miao = syspara.getSysParaConfigureParavalue1();
	}
  String lastLoginTime = UserManager.getInstance().getUserLastLoginTime(userid);
  
  //临时使用，用于即时通讯工具调用
  String url =  (String)request.getParameter("l");//转发页面
  String msid = (String)request.getParameter("msid");//转发页面
  if(!Pub.empty(url)){
	  byte[] bUrl = Base64.decodeBase64(StringUtils.getBytesUtf8(url));
	  url = StringUtils.newStringUtf8(bUrl);
  }else{
	  url = "";
  }
%>

<script type="text/javascript">

	var p_userAccount = '<%=userid%>';
    //菜单按钮点击响应函数
    function menutree_click(menuName,menuLocation,menuDesc,target)
    {  
    	if("#"!=menuLocation){
    		if(target=="pagearea"){
    			document.getElementById("menuiframe").src = "${pageContext.request.contextPath}/"+menuLocation;
    		}else if(target=="fullscreen"){
    			//OpenFullWindow(menuLocation,menuDesc);
    			$(window).manhuaDialog({"title":menuDesc,"type":"url","content":"${pageContext.request.contextPath}/"+menuLocation,"modal":"1"});
    		}
    		
    	}
    	
    }
    //首页按钮点击响应函数
    function doHomePage()
    {
    	document.getElementById("menuiframe").src = "${pageContext.request.contextPath}/jsp/framework/portal/main.jsp?userid=<%=userid%>";
    }
    //注销按钮响应函数
    function dologout()
    {
    	$("#logoutForm").submit();
    }
    function doXxzx()
    {
    	//document.getElementById("menuiframe").src = "${pageContext.request.contextPath}/jsp/framework/portal/moreNews.jsp";
    	$(window).manhuaDialog({"title":"中心新闻  |  通知公告","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/portal/moreNews.jsp","modal":"1"});
    }
    function doLczx()
    {
    	//document.getElementById("menuiframe").src = "${pageContext.request.contextPath}/jsp/framework/portal/moreNews.jsp";
    	$(window).manhuaDialog({"title":"流程中心","type":"text","content":"${pageContext.request.contextPath}/jsp/business/lcgl/lccx/Mylcgl.jsp","modal":"1"});
    }
    function showGG(id,ydcs){
    	readGG(id,ydcs);
   	 $(window).manhuaDialog({"title":"通知公告","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/portal/newsDetail.jsp?type=1&id="+id+"&ydcs="+ydcs,"modal":"1"});
     getMessages();
   }
    
   /**打开消息列表**/
   function openMessage(){
	   $(window).manhuaDialog({"title":"短消息","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/message/allMessage.jsp","modal":"1"});
   }
   
   function modifyPassword(){
	   $(window).manhuaDialog({"title":"修改用户信息","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/system/user/personInfoModify.jsp","modal":"4"});
   }
   function readGG(ggid, ydcs) {
		$.ajax({
			url : "${pageContext.request.contextPath }/ggtzController.do?readMainGg&ggid="+ggid+"&ydcs="+ydcs ,
//			data : data,
			cache : false,
			async : false,
			dataType : 'json',
			success : function(response) {
				var result = eval("(" + response + ")");
			}
		});
	}
	//打开问题中心
	function openIssueCore(){
		var leaderFlag = getLeaderAccount();
		if(leaderFlag=="true"){
			$(window).manhuaDialog({"title":"问题中心","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/wttbBzMain.jsp","modal":"1"});
		}else{
			$(window).manhuaDialog({"title":"问题中心","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/wttbMain.jsp","modal":"1"});
		}
	}
	//打开问题中心
	function openProcessingCenter(){
		$(window).manhuaDialog({"title":"处理中心","type":"text","content":"${pageContext.request.contextPath}/jsp/business/clzx/clzxMain.jsp","modal":"2"});
	}
	//获取部门负责人账号
	function getLeaderAccount(){
		var account = "";
		$.ajax({
			url:"${pageContext.request.contextPath }/wttb/wttbController.do?queryLeaderFlag",
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				var res = result.msg;
				account = res;
			}
		});
		return account;
	}
	function showSuccessMsg(title,content,type){
		title=title==undefined?"提示信息":title;
		content=content==undefined?"操作成功!":content;
		type=type==undefined?"1":type;
		xAlert(title,content,type);
	}
	//------------------------------------------
	//-打开用户帮助界面
	//------------------------------------------
	function openHelpPage(){
		$(window).manhuaDialog({"title":"帮助","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/portal/help/helpMain.jsp","modal":"4"});
	}
</script>
<!-- 消息显示层start -->
<div id="MessageModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h3 id="MessageModalTitle"></h3>
  </div>
  <div class="modal-body" id="MessageModalContent">
    
  </div>
  <div class="modal-footer">
    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
    <button class="btn btn-inverse" data-dismiss="modal" aria-hidden="true">知道了</button>
  </div>
</div>
<!-- 消息显示层end -->


<!-- start 顶部导航栏 -->
	<div class="header">
		<div class="logo text-center"><img src="${pageContext.request.contextPath}/images/mainLogo.png" alt="长春市政府投资建设项目管理中心—综合管控平台"></div>
        <div class="Nav text-center DefaultLink"><a href="javascript:void(0)" onclick="doHomePage();"><img class="DefaultLinkImg" src="${pageContext.request.contextPath}/images/headerNavHome.png" alt="首页"><br>首页</a></div>
        
        <div class="Nav text-center DefaultLink">
           <div class="dropdown nums text-right" id="MessageList">
        
            </div>
           <a href="javascript:void(0)" onclick="doXxzx()"><img class="DefaultLinkImg" src="${pageContext.request.contextPath}/images/headerNavXxzx.png" alt="信息中心"><br>信息中心</a>
        </div>
        <div class="Nav text-center DefaultLink">
        <div class="dropdown nums text-right" id="ProcessList">
           </div>
           <a href="javascript:void(0)" onclick="doLczx()"><img class="DefaultLinkImg" src="${pageContext.request.contextPath}/images/headerNavLczx.png" alt="流程中心"><br>流程中心</a>
                   </div>


			<div class="Nav text-center DefaultLink">
				<div class="dropdown nums text-right" id="sywtsl">
				</div>
				<a href="javascript:void(0)" onclick="openIssueCore();"><img class="DefaultLinkImg"
						src="${pageContext.request.contextPath}/images/headerNavDbsx.png"
						alt="问题中心">
					<br>问题中心</a>
			</div>
			<div class="Nav text-center DefaultLink" id="clzxHead">
				<div class="nums text-right" id="clzxList">
					<a href="javascript:void(0)">0</a>
				</div>
				<a href="javascript:void(0)" onclick="openProcessingCenter();"><img class="DefaultLinkImg"
						src="${pageContext.request.contextPath}/images/headerNavYjzx.png"
						alt="处理中心">
					<br>处理中心</a>
			</div>
			<div class="user">
        	<div class="imgBox" style="overflow:hidden"><img src="${pageContext.request.contextPath }/fileUploadController.do?getFile&account=<%=userid %>&ywlx=<%=YwlxManager.SYSTEM_USER_TX %>" alt="登录人员" ></div>
        	<div class="contents">您好，<%=username %>&nbsp;&nbsp;&nbsp;&nbsp;<i class="icon-envelope icon-white"></i><a class="dropdown-toggle" href="javascript:void(0)" onclick="openMessage()"><span class="badge" style="color:#ffffff" id="messageid"></span></a>
        		<ul>
        			<div>部门：<%=bmjc %></div>
        			<div>上次登录时间：<%=lastLoginTime %></div>
        			<div>
        				<a href="javascript:void(0)" style="color:#ffffff" id="modifyPass" onclick="modifyPassword()"><i class="icon-lock icon-white" ></i> 用户信息</a>
        				&nbsp;
        				<a href="javascript:void(0)" style="color:#ffffff" onclick="openHelpPage();"><i class="icon-question-sign icon-white" ></i> 帮助</a>
        				&nbsp;
        				<a href="javascript:void(0)" style="color:#ffffff" onclick="dologout();"><i class="icon-off icon-white" ></i> 注销登录</a>
        			</div>
        		</ul>
        	</div>
        </div>
    </div>
    
<!-- end 顶部导航栏 -->
<!-- start 菜单 -->
  <div class="page-sidebar nav-collapse collapse leftNav"> 
        <ul class="page-sidebar-menu" id="menuUL" style="overflow-y:auto;overflow-x:hidden;">
            <li> 
                <!-- start 左侧点击缩放 -->
                <div class="sidebar-toggler hidden-phone"></div>
                <!-- end 左侧点击缩放 -->
            </li>
             <app:menu/>
        </ul>
	</div>
<!-- end 菜单 -->
<!-- start 主体Box -->

	<div class="MainBox">
		<iframe  src="${pageContext.request.contextPath}/jsp/framework/portal/main.jsp?userid=<%=userid%>" id="menuiframe" width="100%"frameborder="0"></iframe>
	</div>
	<form style=" height:0; margin:0;" method="post" action="${pageContext.request.contextPath }/userController.do?logout" id="logoutForm" >
	</form>
	
<script src="${pageContext.request.contextPath }/js/base/jquery.js"></script>
<script src="${pageContext.request.contextPath }/js/base/jquery-ui.js"></script>

<script src="${pageContext.request.contextPath }/js/base/bootstrap.js"></script>
<script src="${pageContext.request.contextPath }/js/base/jsBruce.js"></script>

<script src="${pageContext.request.contextPath }/js/base/app.js"></script>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script src="${pageContext.request.contextPath }/js/base/modal.js"></script>
<script
	src="${pageContext.request.contextPath }/js/common/default.js"></script>
<script
	src="${pageContext.request.contextPath }/js/common/loadFields.js"></script>
<script
	src="${pageContext.request.contextPath }/js/common/bootstrap-validation.js"></script>

	<script type="text/javascript">
      //弹出窗口关闭父页面回调空函数
      function closeParentCloseFunction(val){
	  }
      //弹出窗口关闭弹出页面回调空函数
      function closeNowCloseFunction(){
	  }
      function showMessage(title,content)
      {
		if($("#MessageModal")&&$("#MessageModal").length>0){
			$("#MessageModalTitle").text(title);
			$("#MessageModalContent").html("<p>"+content+"</p>");
			$("#MessageModal").modal("show");
		}
      }
      function showMoreMessage()
      {
    	//  document.getElementById("menuiframe").src = "${pageContext.request.contextPath}/jsp/framework/portal/moreNews.jsp";
    	  $(window).manhuaDialog({"title":"中心新闻  |  通知公告","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/portal/moreNews.jsp","modal":"1"});
      }
      function showMoreProcess()
      {
    	//  document.getElementById("menuiframe").src = "${pageContext.request.contextPath}/jsp/framework/portal/moreNews.jsp";
    	  $(window).manhuaDialog({"title":"我的办理流程","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/common/spFlow/spdb.jsp","modal":"1"});
      }
      function processbl(LINKURL,ID,SEQ,SJBH,YWLX,SPBH,RWLX){
    		var url =  '${pageContext.request.contextPath}/'+LINKURL+'?taskid='+ID+'&taskseq='+SEQ+'&sjbh='+SJBH+'&ywlx='+YWLX+'&spbh='+SPBH+'&rwlx='+RWLX;
    		 $(window).manhuaDialog({"title":"审批信息","type":"text","content":url,"modal":"1"});  
      }
      function getMessages(){
    	  //获得消息
      	 $.ajax({
 				url : '${pageContext.request.contextPath }/ggtzController.do?frameGgtz',
 				data :"",
 				cache : false,
 				dataType : "json",
 				contentType:"application/json",
 				success : function(response) {
 					var obj =eval("("+response.msg+")");
 					
 					if(obj.count==0)
 					{
 						var messageHTML = "<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"javascript:void(0)\">0</a>";
 						$("#MessageList").html(messageHTML);

 					}else{
 						var messagearr = obj.messages;
 						var messageHTML = "<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"javascript:void(0)\">"+obj.count+"</a>";
 						messageHTML +=" <ul class=\"dropdown-menu extended notification\">";
 						messageHTML +="<div class=\"upArrow\"></div>";
 						messageHTML +="<li><p><i class=\"icon-comment\"></i> 你有"+obj.count+"条未读通知公告</p></li>";
 						for(var k in messagearr){  
 							messageHTML +="<li><a href=\"javascript:void(0)\" onclick=\"showGG('"+messagearr[k].ggid+"','')\"><span class=\"label label-success\"><i class=\"icon-plus\"></i></span><abbr title=\""+messagearr[k].content+"\">"+(messagearr[k].title).substr(0,15)+"</abbr></a></li>";
 					      } 
 						messageHTML +="<li class=\"external\"><a href=\"javascript:void(0)\" onclick=\"showMoreMessage()\">查看更多通知公告<i class=\"icon-circle-arrow-right\"></i></a></li>";
 						messageHTML +=" </ul>";
 						$("#MessageList").html(messageHTML);
 					}
 					
 				}
 			});
    	
    	  
      }
      function gengxinchaxun(message){
    	  xAlertMsg(message);
    	  getProcess();
      }
      function Mylcgengxinchaxun(message){
    	  getProcess();
      }
      function getProcess(){
    	  //流程中心
        	$.ajax({
  				url : '${pageContext.request.contextPath }/ProcessAction.do?frameGgtz',
  				data :"",
  				cache : false,
  				dataType : "json",
  				contentType:"application/json",
  				success : function(response) {
  					var obj =eval("("+response.msg+")");
  					if(obj.count==0)
  					{
  						var messageHTML = "<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"javascript:void(0)\">0</a>";
  						$("#ProcessList").html(messageHTML);

  					}else{
  						var messagearr = obj.messages;
  						var messageHTML = "<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"javascript:void(0)\">"+obj.count+"</a>";
  						messageHTML +=" <ul class=\"dropdown-menu extended notification\">";
  						messageHTML +="<div class=\"upArrow\"></div>";
  						messageHTML +="<li><p><i class=\"icon-comment\"></i> 你有"+obj.count+"条待办理流程</p></li>";
  						for(var k in messagearr){  
  							messageHTML +="<li><a href=\"javascript:void(0)\" onclick=\"processbl('"+messagearr[k].linkurl+"','"+messagearr[k].id+"','"+messagearr[k].seq+"','"+messagearr[k].sjbh+"','"+messagearr[k].ywlx+"','"+messagearr[k].spbh+"','"+messagearr[k].rwlx+"')\"><span class=\"label label-success\"><i class=\"icon-plus\"></i></span><abbr title=\""+messagearr[k].content+"\">"+(messagearr[k].title).substr(0,15)+"</abbr></a></li>";
  					      } 
  						messageHTML +="<li class=\"external\"><a href=\"javascript:void(0)\" onclick=\"showMoreProcess()\">查看更多待办理流程<i class=\"icon-circle-arrow-right\"></i></a></li>";
  						messageHTML +=" </ul>";
  						$("#ProcessList").html(messageHTML);

  					}
  					
  				}
  			});
      }
      function getDxx(){
    	  $.ajax({
				url : '${pageContext.request.contextPath }/messageController.do?getMessage',
				data :"",
				cache : false,
				dataType : "json",
				contentType:"application/json",
				success : function(response) {
					var obj =eval("("+response.msg+")");
					$("#messageid").html(obj.count);
			}
			});
      }
	//获取问题提报业务待处理问题的数量
	function getWtCounts(){
		var wtcounts = "0";
		$.ajax({
			url:"${pageContext.request.contextPath }/wttb/wttbController.do?getWtCounts",
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				var res = result.msg;
				var tempJson = eval("("+res+")");
				var row = tempJson.response.data[0];
				wtcounts = row.COUNTS;
			}
		});
		return wtcounts;
	}
	//--------------------------------------------
	//-获取待处理问题列表
	//--------------------------------------------
	function getWtList(){
		var wtcounts = getWtCounts();
		$.ajax({
			url : "${pageContext.request.contextPath }/wttb/wttbController.do?getWtList",
			data :"",
			cache : false,
			dataType : "json",
			contentType:"application/json",
			success : function(result) {
				var res = result.msg;
				if(wtcounts=="0" || res=="0"){
					var messageHTML = '<a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0)">0</a>';
					$("#sywtsl").html(messageHTML);
				}else{
					var tempJson = eval("("+res+")");
					var len = tempJson.response.data.length;
					var messageHTML = '<a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0)">'+wtcounts+'</a>';
					messageHTML +='<ul class="dropdown-menu extended notification">';
					messageHTML +='<div class="upArrow"></div>';
					messageHTML +='<li><p><i class="icon-comment"></i> 你有'+wtcounts+'条待处理问题</p></li>';
					for(var k=0;k<len;k++){  
						var row = tempJson.response.data[k];
						messageHTML +='<li><a href="javascript:void(0)" onclick="openIssueCore()"><span class="label label-success"><i class="icon-plus"></i></span><abbr title="'+row.WTBT+'">'+(row.WTBT).substr(0,15)+'</abbr></a></li>';
					} 
					messageHTML +='<li class="external"><a href="javascript:void(0)" onclick="openIssueCore()">查看更多待处理问题<i class="icon-circle-arrow-right"></i></a></li>';
					messageHTML +=' </ul>';
					$("#sywtsl").html(messageHTML);
				}	
			}
		});
	}
	
	function getClzxList(){
		$.ajax({
			url : "${pageContext.request.contextPath }/clzx/clzxManageControllor.do?queryCount",
			data :"",
			cache : false,
			dataType : "json",
			contentType:"application/json",
			success : function(result) {
				var res = result.msg;
				var tempJson = eval("("+res+")");
				var clCnt = tempJson.response.data[0].COUNT;
				if(clCnt==undefined){
					var messageHTML = '<a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0)">0</a>';
					$("#clzxList").html(messageHTML);
				}else{
					var len = tempJson.response.data.length;
					var messageHTML = '<a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0)">'+clCnt+'</a>';
					$("#clzxList").html(messageHTML);
				}	
			}
		});
	}
	
	function isShow() {
		$.ajax({
			url : "${pageContext.request.contextPath }/clzx/clzxManageControllor.do?queryIsShowClzx",
			data :"",
			cache : false,
			dataType : "json",
			contentType:"application/json",
			success : function(result) {
				var res = result.msg;
				var tempJson = eval("("+res+")");
				var isshow = tempJson.response.data[0].CNT;
				if(isshow == "0") {
					$("#clzxHead").hide();
				} else {
					$("#clzxHead").show();
				}
			}
		});
	}
	var openWindowHeight = 5;
	jQuery(document).ready(function() {    
		$("#clzxHead").hide();
	   App.init(); // initlayout and core plugins
	   getMessages();
	   getProcess();
	   getDxx();
	   getWtList();
	   getClzxList();
	   isShow();
	   openWindowHeight = $(window).innerHeight();
	   var pageHeight = document.documentElement.clientHeight;//页面高度
	   $("#menuUL").css("height",pageHeight-90);
	   
	   //临时，即时通讯工具使用
	   if("<%=url%>"){
	   	//add by zhangbr@ccthanking.com	添加对?和&连接符号的处理
	   	var symbol = "?";
	   	if("<%=url%>".indexOf("?")!=-1){
	   		symbol = "&";
	   	}
	   	var url =  "${pageContext.request.contextPath }"+"<%=url%>"+symbol+"msid=<%=msid%>";
	   	$(window).manhuaDialog({"title":"消息提醒","type":"text","content":url,"modal":"1"});  
	   }
	   
	});
</script> 
<!-- end 主体Box -->
<script type="text/javascript">

const miao = <%=miao%>;

var pushIcon = "${pageContext.request.contextPath }/images/logo.png";
var pushTitle = "消息提示";
var pushInfo =  null;
var pushUrl = null;

function show(info,url,title){
	/**
	var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
	var isChrome = userAgent.indexOf("Chrome") > -1 ; //判断是否Chrome
	if(isChrome){
	  notify(info,url);
	}else{
	  alert(info);//非chrome浏览器暂时处理
	}
	**/
	
	getDxx();//不知道为什么要放到这里
	
	
	pushInfo = info;
	pushUrl = url;
	pushTitle = title?title:pushTitle;
	notify();
}

function RequestPermission (callback) {
	window.webkitNotifications.requestPermission(callback);
}


function notify() {
	 var popup = window
     .webkitNotifications.createNotification(pushIcon,
     		pushTitle, pushInfo);
 popup.ondisplay = function(event) {
 	/**
     setTimeout(function() {
         event.currentTarget.cancel();
     }, miao * 1000);
 	**/
 }
 popup.onclick = function(){
 	if(pushUrl){
			document.getElementById("menuiframe").src = "${pageContext.request.contextPath}/"+pushUrl;
 	}
    	this.cancel();
 }
 popup.show();
}

$(function() {
	jQuery('.page-sidebar-menu>li>a:last').click();
});

</script>

</body>
</html>