<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.common.QuerySet"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<app:base />
<title></title>
<% 
	String num=request.getParameter("num");
%>
<style type="text/css">
/***
Dashboard stats
***/
.dashboard-stat {
  margin-bottom: 25px;
  position:relative;
  width:100%;
}

.dashboard-stat:before,
.dashboard-stat:after {
  display: table;
  line-height: 0;
  content: "";
}
.dashboard-stat:after {
  clear: both;
}

.dashboard-stat .visual {
  width: 120px;
  height:80px;
  display: block;
  float: left;
  padding-top: 10px;
  padding-left: 15px;
  padding-bottom:20px;
}

.dashboard-stat .visual i {
  font-size: 65px;
  color: #fff;
}

.dashboard-stat .details {
  float: right;
  padding-right: 10px;
}

.dashboard-stat .details .number {    
  padding-top: 25px;
  text-align: right;
  font-size: 34px;
  letter-spacing: -1px;
  font-weight: 300;
  color: #fff;
  margin-bottom: 10px;
}

.dashboard-stat .details .desc {
  text-align: right;
  font-size: 16px;
  letter-spacing: 0px;
  font-weight: 300;
  color: #fff;
}

.dashboard-stat .more {
  clear: both;
  display: block;
  padding: 5px 0px 5px 0px;
  text-transform: uppercase;
  font-weight: 300;
  font-size: 11px;
  color: #fff;  
  opacity: 0.7;  
  filter: alpha(opacity=70);
	position:absolute;
	z-index:3;
	width:inherit;
	bottom:0px;
}  

.dashboard-stat .more i {
  margin-top: 4px;
  margin-right:10px;
  float: right;
}

.dashboard-stat .more:hover {
  text-decoration: none;
  opacity: 1;  
  filter: alpha(opacity=100);
}

.dashboard-stat.blue {
  background-color: #27a9e3;
}

.dashboard-stat.blue .more {
  background-color: #208dbe;
} 

.dashboard-stat.green {
  background-color: #28b779;
}

.dashboard-stat.green .more { 
  background-color: #10a062;
} 

.dashboard-stat.red {
  background-color: #e7191b;
}

.dashboard-stat.red .more { 
  background-color:#bc0d0e;
} 

.dashboard-stat.yellow {
  background-color: #ffb848;
}

.dashboard-stat.yellow .more { 
  background-color: #cb871b;
} 

.dashboard-stat.purple {
  background-color: #852b99;
}

.dashboard-stat.purple .more { 
  background-color: #6e1881;
} 
.dashboard-stat .title { 
  font-size:25px;
  color:#FFF;
}
.row-fluid [class*="span"]{
	margin-left:0px;
	margin-right:1.127659574468085%;
} 
</style>
<script type="text/javascript" charset="utf-8">
var controllername = "${pageContext.request.contextPath}/clzx/clzxManageControllor.do";
$(function(){
	queryGcjsTxfx();
});

function queryGcjsTxfx(){
	var action1 = controllername + "?queryYwlx";
	$.ajax({
		url : action1,
		success: function(result){
			$("#dbList").html("");
			var resultmsgobj = convertJson.string2json1(result);
			var resultobj = convertJson.string2json1(resultmsgobj.msg);
			if(resultobj == "0") {
				xAlert("暂无处理信息");
			} else {
				var subresultmsgobj = resultobj.response.data;
				var len = subresultmsgobj.length;
				var showHtml="";
				for(var i=0;i<len;i++) {
					var title = subresultmsgobj[i].YWMC;
					var yw_code = subresultmsgobj[i].YWBID;
					var cnt = subresultmsgobj[i].CNT;
					
					showHtml += '<div class="span3 responsive" data-tablet="span6" data-desktop="span3">';
					showHtml += '<div class="dashboard-stat blue">';
					showHtml += '<div class="visual">';
					showHtml += '<span class="title">'+title+'</span>';
					showHtml += '</div>';
					showHtml += '<div class="details">';
					showHtml += '<div class="number">';
					showHtml += cnt;
					showHtml += '</div>';
					showHtml += '<div class="desc">';									
					showHtml += '时间点反馈';
					showHtml += '</div>';
					showHtml += '</div>';
					showHtml += '<a class="more" href="#" onclick="openDetail(\''+title+'\',\''+yw_code+'\')">';
					showHtml += '&nbsp;&nbsp;&nbsp;&nbsp;查看详细 <i class="icon-share-alt icon-white"></i>';
					showHtml += '</a>';	
					showHtml += '<div style="height:40px;"></div>';		
					showHtml += '</div>';
					showHtml += '</div>';
				}
				$("#dbList").append(showHtml);
			}
			$(".dashboard-stat").mouseover(function(){
				//$(this).find(".more").attr("style","height:100px;");
				$(this).find(".more").stop().animate({'height':"100px"},100);
			});
			$(".dashboard-stat").mouseout(function(){
				//$(this).find(".more").removeAttr("style");
				$(this).find(".more").stop().animate({'height':"20px"},100);
			});
		}
	});
}

function openDetail(title,yw_code) {
//	alert(title+"||"+yw_code);
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/clzx/clzxList.jsp?yw_code="+yw_code,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}

function callBackQuery() {
	queryGcjsTxfx();
}


//关闭事件
function closeNowCloseFunction()
{
 try {
    	var fuyemian=$(window).manhuaDialog.getParentObj();
		var s = fuyemian.getClzxList;
	    if(s){
		   fuyemian.getClzxList();
	    } else {
    		var frameW = $(window).manhuaDialog.getFrameObj();
    		var w = frameW.getClzxList;
	      	if(w){
	      		frameW.getClzxList();
	      	}
     	}
 } catch(e) {
    	window.parent.getClzxList();
 }
}
</script>
</head>
	<body>
	<div class="row-fluid" id="dbList">
	</div>
	</body>
</html>