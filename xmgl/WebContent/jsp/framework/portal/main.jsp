<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.ccthanking.framework.service.impl.MenuServiceImpl"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>长春市政府投资建设项目管理中心——综合管控中心</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link href="${pageContext.request.contextPath }/css/bootstrap.css" rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath }/css/base.css?version=20150605" rel="stylesheet" media="screen">
<script src="${pageContext.request.contextPath }/js/base/jquery.js"></script>
<script src="${pageContext.request.contextPath }/js/base/bootstrap.js"></script>
<script src="${pageContext.request.contextPath }/js/base/jquery-ui.js"></script>
<script src="${pageContext.request.contextPath }/js/base/jsBruce.js?version=20150607"></script>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script src="${pageContext.request.contextPath }/js/common/default.js"></script>
</head>
<%
	String userid = request.getParameter("userid");
%>
<script type="text/javascript">
//-------------------------------
//-问题提报弹出窗口
//-添加人：zhangbr@ccthanking.com
//-------------------------------
function OpenFullWindowWttb(){
	$(window).manhuaDialog({"title":"问题提报","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/wttbMain.jsp"});
	//parent.$("body").manhuaDialog({"title":"问题提报","type":"text","content":"${pageContext.request.contextPath}/jsp//business/wttb/wttbMain.jsp","modal":"2"});
}
function changeurl(id)
{
	var url;
	if(id=='gk')
	{

		url=document.getElementById('gk').value;
		parent.menutree_click('',url,'','pagearea');
	}
	else
	{
		var urltitle=document.getElementById(id).value;
		if(urltitle==null||urltitle=="undefined")
		{
			return;
		}
		else
		{
			var index=urltitle.indexOf('|', 0);
			url=urltitle.substring(0,index);
			title=urltitle.substring(index+1,urltitle.length);
			$(window).manhuaDialog({"title":title,"type":"text","content":"${pageContext.request.contextPath}/"+url,"modal":"1"});
		}	
	}
}
function popPage(target, url, title) {
	if(target == 'pagearea') {
		parent.menutree_click('',url,'','pagearea');
	} else if(target == 'fullscreen') {
		$(window).manhuaDialog({"title":title,"type":"text","content":"${pageContext.request.contextPath}/"+url});
	}else if(target == 'modal1'){
		$(window).manhuaDialog({"title":title,"type":"text","content":"${pageContext.request.contextPath}/"+url,"modal":"1"});
	}else if(target == 'modal2'){
		$(window).manhuaDialog({"title":title,"type":"text","content":"${pageContext.request.contextPath}/"+url,"modal":"2"});
	}else if(target == 'modal3'){
		$(window).manhuaDialog({"title":title,"type":"text","content":"${pageContext.request.contextPath}/"+url,"modal":"3"});
	}else if(target == 'modal4'){
		$(window).manhuaDialog({"title":title,"type":"text","content":"${pageContext.request.contextPath}/"+url,"modal":"4"});
	}else if(target == 'modal5'){
		$(window).manhuaDialog({"title":title,"type":"text","content":"${pageContext.request.contextPath}/"+url,"modal":"5"});
	}
}
function showNewDetail(type,id,ydcs,obj){
	var title="通知公告";
	if(type=="2"){
		title = "中心新闻 ";
	}
	if(type=="1"){
	    var $tr = $(obj.parentElement);
	    var imgObj = $tr.find("img");
	    if(imgObj.length>0){
	    	var src_ = $(imgObj[0]).attr("src");
	    	if(src_.indexOf("listIcon")>-1)
	    	imgObj[0].remove();
	    }
		readGG(id, ydcs);
		parent.getMessages();
	}
	$(window).manhuaDialog({"title":title,"type":"text","content":"${pageContext.request.contextPath}/jsp/framework/portal/newsDetail.jsp?type="+type+"&id="+id,"modal":"1"});
	//window.location.href = "${pageContext.request.contextPath}/jsp/framework/portal/newsDetail.jsp?type="+type+"&id="+id;
}
//点击标题，打开主任办公会
function showZrbghDetail(type,id,title,hysj){
	$(window).manhuaDialog({"title":title,"type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghgl/zrbgh.jsp?type="+type+"&id="+id+"&hysj="+hysj,"modal":"1"});
}
//点击主任办公会的更多，打开更多页面
function showMoreZrbgh(title){
	$(window).manhuaDialog({"title":title,"type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghgl/zrbgh.jsp","modal":"1"});
}
//点击共享信息标题，打开共享信息详细页面
function showGxxxDetail(id,title,obj){
	var $tr = $(obj.parentElement);
	var imgObj = $tr.find("img");
	if(imgObj.length>0){
		var src_ = $(imgObj[0]).attr("src");
		if(src_.indexOf("new.gif")>-1){
			src_ = src_.replace("new.gif","content_new_blank.png");
			$(imgObj[0]).attr("src",src_);
		}
	}
	readGG(id, '1');
	parent.getMessages();
	$(window).manhuaDialog({"title":title,"type":"text","content":"${pageContext.request.contextPath}/jsp/framework/portal/ndxxgxDetail.jsp?&id="+id,"modal":"1"});
}
function showMoreNews(type){
	$(window).manhuaDialog({"title":"中心新闻  |  通知公告","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/portal/moreNews.jsp?type="+type,"modal":"1"});
	//window.location.href = "${pageContext.request.contextPath}/jsp/framework/portal/moreNews.jsp";
}
function showMoreNdxxgx(type){
	$(window).manhuaDialog({"title":"年度信息共享","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/portal/moreNdxxgx.jsp?type="+type,"modal":"1"});
}
//记录阅读 
function readGG(ggid, ydcs) {
	$.ajax({
		url : "${pageContext.request.contextPath }/ggtzController.do?readMainGg&ggid="+ggid+"&ydcs="+ydcs ,
//		data : data,
		cache : false,
		async : false,
		dataType : 'json',
		success : function(response) {
			var result = eval("(" + response + ")");
		}
	});
}
</script>
<body>
<!-- start 操作区 -->
		<div class="indexMain">
			<div class="container-fluid">
				<app:news/>
			</div>
		</div>
		<!-- end 操作区 -->
		<!-- start 快速入口 -->
		<div class="QuickLinks">
			<h3>快速入口</h3>
			<div style="text-align:center;">
				<app:quickEntry/>
			</div>
			<!-- end 快速入口 -->
		</div>
<script type="text/javascript">
<%-- var userid="<%=userid%>";
switch(userid)
{
	case 'superman' : 
		document.getElementById('gk').value="jsp/framework/portal/zj.jsp?userid="+"<%=userid%>";
		document.getElementById('tjsj').value="jsp/char/charDemo.jsp|结算汇总统计报表";
		//document.getElementById('xmsc').value="jsp/business/xmcbk/add.jsp";
		//document.getElementById('wdgl').value="jsp/business/xmcbk/xiada.jsp";
		//document.getElementById('bgsc').value="jsp/business/jhb/xdxmk/zhy_List.jsp";
		//document.getElementById('khgl').value="jsp/business/xmcbk/xmjz.jsp";
								
	break;
	
	 case 'zjbz' :
		document.getElementById('gk').value="jsp/framework/portal/zj.jsp?userid="+"<%=userid%>";
		document.getElementById('tjsj').value="jsp/char/charDemo.jsp|结算汇总统计报表";
		//document.getElementById('xmsc').value="jsp/business/zjb/lbj/lbjwh.jsp";
		//document.getElementById('wdgl').value="jsp/business/qqsx/qqsx/qqsxgl.jsp";
		//document.getElementById('bgsc').value="jsp/business/jhb/xdxmk/zhy_List.jsp";
		//document.getElementById('khgl').value="jsp/business/zjb/jsgl/jsgl.jsp";
	break; 
	
	 case 'zjry':
		//	document.getElementById('xmsc').value="jsp/business/zjb/lbj/lbjwh.jsp";
		//	document.getElementById('wdgl').value="jsp/business/qqsx/qqsx/qqsxgl.jsp";
		document.getElementById('tjsj').value="jsp/char/charDemo.jsp|结算汇总统计报表";
			document.getElementById('lbj').value="jsp/business/zjb/lbj/lbjxx.jsp|拦标价>新增";
			document.getElementById('js').value="jsp/business/zjb/jsgl/jiesuanwh.jsp|结算>新增";
	 break;
	
	 case 'jhbz' :
		document.getElementById('gk').value="jsp/framework/portal/jh.jsp?userid="+"<%=userid%>";
		document.getElementById('tjsj').value="jsp/char/bmjk.jsp?first=1|部门监控";
		//document.getElementById('xmsc').value="jsp/business/zjb/lbj/lbjwh.jsp";
		//document.getElementById('wdgl').value="jsp/business/qqsx/qqsx/qqsxgl.jsp";
		//document.getElementById('bgsc').value="jsp/business/jhb/xdxmk/zhy_List.jsp";
		//document.getElementById('khgl').value="jsp/business/zjb/jsgl/jsgl.jsp";
	break; 
	
	 case 'jhry':
			//document.getElementById('xmsc').value="jsp/business/zjb/lbj/lbjwh.jsp";
			//document.getElementById('wdgl').value="jsp/business/qqsx/qqsx/qqsxgl.jsp";
			document.getElementById('tjsj').value="jsp/char/xmkgChart.jsp?first=1|项目开工情况统计报表";
			document.getElementById('xzxm').value="jsp/business/xmcbk/add.jsp|储备库>新增";
			document.getElementById('jhtz').value="jsp/business/jhb/tcjh/tcjh_list.jsp|统筹计划管理";
	 break;
	
} --%>

</script>


	
</body>
</html>