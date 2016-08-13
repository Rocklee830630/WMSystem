<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link href="${pageContext.request.contextPath }/css/bootstrap.css" rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath }/css/base.css" rel="stylesheet" media="screen">
<title>Insert title here</title>
</head>
<%
	String userid = request.getParameter("userid");
%>
<script type="text/javascript">
function OpenFullWindowWttb(){
	$(window).manhuaDialog({"title":"问题提报","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/wttbMain.jsp"});
	//parent.$("body").manhuaDialog({"title":"问题提报","type":"text","content":"${pageContext.request.contextPath}/jsp//business/wttb/wttbMain.jsp","modal":"2"});
}
function changeurl(id)
{
	var url
	if(id=='gk')
	{
		url=document.getElementById('gk').value;
		parent.menutree_click('',url,'','pagearea');
	}
	else
	{
		urltitle=document.getElementById(id).value;
		if(urltitle==null||urltitle=="undefined")
		{
			return;
		}
		else
		{
			var index=urltitle.indexOf("|", 0);
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
	}
}
</script>
<body>
<!-- start 操作区 -->
<div class="indexMain">
  <div class="container-fluid">
    <div class="row-fluid">
      <p></p>
      <div class="B-small-from-table-auto">
        <h4>工作进展<a href=""><div class="pull-right print"><i class="icon-print icon-white"></i></div></a></h4>
        <div class="B-div">
        	<p class="gaikuangTitle"><span>造价</span>管理工作概况</p>
            <div class="gaikuangContent">
            	<h4 class="primary">拦标价结算工作情况</h4>
            	<p class="primary">
            		截止2013年6月17日，已下达计划 <a href="${pageContext.request.contextPath }/jsp/business/zjb/lbj/lbjwh.jsp?num=21"> <font style="font-size:18px;font-weight:bold;">21</font></a>项，
            		已出拦标价  <a href="${pageContext.request.contextPath }/jsp/business/zjb/lbj/lbjwh.jsp?num=10"> <font style="font-size:18px;font-weight:bold;">10</font></a>项。
            	</p>
            	<h4 class="primary">结算项目情况</h4>
                <p class="primary">
                	截止2013年6月17日，已下达城建重点项目第一批计划<a href="${pageContext.request.contextPath }/jsp/business/zjb/jsgl/jsgl.jsp?num=32"> <font style="font-size:18px;font-weight:bold;">32</font></a> 项，
                	其中已竣工 <a href="${pageContext.request.contextPath }/jsp/business/zjb/jsgl/jsgl.jsp?num=10"> <font style="font-size:18px;font-weight:bold;">10</font></a>项，
                	建管中心审查中 <a href="${pageContext.request.contextPath }/jsp/business/zjb/jsgl/jsgl.jsp?num=9"> <font style="font-size:18px;font-weight:bold;">9</font></a>项，建管中心审完<a href="${pageContext.request.contextPath }/jsp/business/zjb/jsgl/jsgl.jsp?num=3"> <font style="font-size:18px;font-weight:bold;">3</font></a>项，
                	财审中心审查中<a href="${pageContext.request.contextPath }/jsp/business/zjb/jsgl/jsgl.jsp?num=5"> <font style="font-size:18px;font-weight:bold;">5</font></a>项，
                	财审中心审完未报审计<a href="${pageContext.request.contextPath }/jsp/business/zjb/jsgl/jsgl.jsp?num=3"> <font style="font-size:18px;font-weight:bold;">3</font></a>项，
                	财审中心审完已报审计 <a href="${pageContext.request.contextPath }/jsp/business/zjb/jsgl/jsgl.jsp?num=0"> <font style="font-size:18px;font-weight:bold;">0</font></a> 项，
                	审计局审查中<a href="${pageContext.request.contextPath }/jsp/business/zjb/jsgl/jsgl.jsp?num=0"> <font style="font-size:18px;font-weight:bold;">0</font></a>项，
                	审计局已审完<a href="${pageContext.request.contextPath }/jsp/business/zjb/jsgl/jsgl.jsp?num=1"> <font style="font-size:18px;font-weight:bold;">1</font></a> 项。
                </p>
            	<h4 class="danger">存在问题</h4>
                <p class="danger">
		重大问题的有： <a> <font style="font-size:18px;font-weight:bold; color:red;">1</font></a> 项。<br>
      	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 一般问题的有： <a> <font style="font-size:18px;font-weight:bold; color:red;">5</font></a> 项。
                </p>
            </div>
        </div>
      </div>
    </div>
  </div>
</div>
<!-- end 操作区 --> 
<!-- start 快速入口 -->
<div class="QuickLinks">
  	<h3>快速入口</h3>
  	<div> 
		<app:quickEntry/>
	</div>
</div>
<!-- end 快速入口 --> 
<!-- start 引用bootstrap --> 
<script src="${pageContext.request.contextPath }/js/jquery.js"></script> 
<script src="${pageContext.request.contextPath }/js/jquery-ui.js"></script> 
<script src="${pageContext.request.contextPath }/js/bootstrap.js"></script> 
<script src="${pageContext.request.contextPath }/js/jsBruce.js"></script> 
<!-- end 引用bootstrap -->
<script type="text/javascript">
var userid="<%=userid%>";
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
		document.getElementById('tjsj').value="jsp/char/xmkgChart.jsp?first=1|项目开工情况统计报表";
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
}

</script>

</body>
</html>