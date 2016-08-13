<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>处理中心管理</title>
<style>
th {
	font-weight: bold;
	font-family:Microsoft YaHei;
}
th {
	display: table-cell;
	vertical-align: inherit;
}
table {
	border-collapse: collapse;
	border-spacing: 0;
}
table {
	border-spacing: 2px;
	border-color: gray;
}
</style>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/ztree_css/demo.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/ztree_css/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath }/js/base/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.core-3.5.min.js"></script>
</head>
<body>
<app:dialogs/>
<p></p>
<div class="container-fluid">
	<div class="row-fluid">
    	<div class="span3" style="height: 100%;margin-top:5px;" id="aaaaaa">
      		<div class="B-small-from-table-autoConcise" >
      			<form method="post" id="menuForm">
					<table class="B-table" id="queryTable">
						<tr>
							<th width="5%" class="right-border bottom-border" style="font-weight:bold;vertical-align:inherit;font-family:Microsoft YaHei;font-size:14px;">业务名称</th>
						</tr>
					</table>
				</form>
	  		</div>
      		<p></p>
		    <div class="span5">
			    <ul id="docTree" class="ztree" style="width:340px;background-color:#FFFFFF;border:0px;margin-top:5px;overflow-x:hidden;">
			    	<img alt="请稍后，正在加载数据……" src="${pageContext.request.contextPath }/img/loading.gif" >
			    </ul>
		    </div>
    	</div>
     	<%-- <div class="span8" id="wdlb" style="height: 100%;">
    		<iframe id="frame1" name="projectModel" width="100%" height="25%" src="${pageContext.request.contextPath }/jsp/business/clzx/clzxgl/header.jsp" border=1 framespacing=0 marginheight=0 marginwidth=0> </iframe>
	    	<iframe id="frame2" name="projectModel" width="100%" height="70%" src="${pageContext.request.contextPath }/jsp/business/clzx/clzxgl/seluser.jsp" border=1 framespacing=0 marginheight=0 marginwidth=0> </iframe>
    	</div>  --%>
	    
	    <div class="span4" id="wdlb" style="height: 100%;margin-top:5px;margin-left:5px;">
	    <iframe id="frame1" name="projectModel" width="100%" height="90%" src="${pageContext.request.contextPath }/jsp/business/clzx/clzxgl/header.jsp" border=1 framespacing=0 marginheight=0 marginwidth=0> </iframe>
	    </div>
	    <div class="span5" id="div2" style="margin-top:5px;overflow:hidden;margin-left:5px;">
	    <iframe id="frame2" name="projectModel" width="100%" src="${pageContext.request.contextPath }/jsp/business/clzx/clzxgl/seluser.jsp" border=1 framespacing=0 marginheight=0 marginwidth=0> </iframe>
	    </div>
	</div>
</div>

<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/clzx/clzxManageControllor.do";

//计算本页自适应高度
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle;
	$("#wdlb").css("height",g_iHeight-16);
	$("#docTree").css("height",g_iHeight-pageQuery-16);
	$("#docTree").css("width",$("#queryTable")[0].clientWidth-20);
	$("#frame2").css("height",g_iHeight-16);
};

var setting = {
	async: {
		enable: true,
		url: controllername + "?queryClzxgl",
		autoParam: ["id"],
		dataFilter: function (treeId, parentNode, responseData) {
   //         alert(responseData[0].parentId);
            return responseData;
        }
	},
	view: {
		dblClickExpand: false,
		showLine :false,
		showIcon: false,
		selectedMulti: false,
		addDiyDom: addDiyDom
	},
	data: {
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "parentId"
		}
	},
	callback: {
		onClick  : function(event, treeId, treeNode) {
			if(treeNode.parentId>10000)
			{
				$("#frame1").attr("src" ,"${pageContext.request.contextPath }/jsp/business/clzx/clzxgl/header.jsp?name="+treeNode.name+"&id="+treeNode.id);
			}	
		}/* ,
		beforeClick: beforeClick */
	}
};

function addDiyDom(treeId, treeNode) {
	var spaceWidth = 5;
	var switchObj = $("#" + treeNode.tId + "_switch"),
	icoObj = $("#" + treeNode.tId + "_ico");
	switchObj.remove();
	icoObj.before(switchObj);
	if (treeNode.level > 1) {
		var spaceStr = "<span style='display: inline-block;width:" + (spaceWidth * treeNode.level)+ "px'></span>";
		switchObj.before(spaceStr);
	}
}

function beforeClick(treeId, treeNode) {
	alert(0)
	if (treeNode.level == 0 ) {
		var zTree = $.fn.zTree.getZTreeObj("docTree");
		zTree.expandNode(treeNode);
		return false;
	}
	return true;
}

var zTree,rMenu,menuTreeJson,operatorSign;
$(document).ready(function() {
	$.fn.zTree.init($("#docTree"), setting);
	zTree = $.fn.zTree.getZTreeObj("docTree");
	iframeAutoFit();
	
	var treeObj = $("#docTree");
	treeObj.hover(function () {
		if (!treeObj.hasClass("showIcon")) {
			treeObj.addClass("showIcon");
		}
	}, function() {
		treeObj.removeClass("showIcon");
	});
	
});

$(function() {
	setPageHeight();
	$("#queryBtn").click(function() {
		var projectName = $("#prjname").val();
		$.fn.zTree.init($("#docTree"), setting);
	});
	
});

function iframeAutoFit()
{
    try
    {
        if(window!=parent)
        {
            var a = parent.document.getElementById("xmht");
            for(var i=0; i<a.length; i++) //author:meizz
            {
                if(a[i].contentWindow==window)
                {
                    var h1=0, h2=0, d=document, dd=d.documentElement;
                    a[i].parentNode.style.height = a[i].offsetHeight +"px";
                    a[i].style.height = "10px";
                    if(dd && dd.scrollHeight) h1=dd.scrollHeight;
                    if(d.body) h2=d.body.scrollHeight;
                    var h=Math.max(h1, h2);

                    if(document.all) {h += 4;}
                    if(window.opera) {h += 1;}
                    a[i].style.height = a[i].parentNode.style.height = h +"px";
                }
            }
        }
    } catch (ex){}

}

</script>
<style type="text/css">
	.ztree * {font-size: 10pt;font-family:"Microsoft Yahei",Verdana,Simsun,"Segoe UI Web Light","Segoe UI Light","Segoe UI Web Regular","Segoe UI","Segoe UI Symbol","Helvetica Neue",Arial}
	.ztree li ul{ margin:0; padding:0}
	.ztree li {line-height:30px;}
	.ztree li a {width:200px;height:30px;padding-top: 0px;}
	.ztree li a:hover {text-decoration:none; background-color: #E7E7E7;}
	.ztree li a span.button.switch {visibility:hidden}
	.ztree.showIcon li a span.button.switch {visibility:visible}
	.ztree li a.curSelectedNode {background-color:#D4D4D4;border:0;height:30px;}
	.ztree li span {line-height:30px;}
	.ztree li span.button {margin-top: -7px;}
	.ztree li span.button.switch {width: 16px;height: 16px;}
	
	.ztree li a.level0 span {font-size: 130%;font-weight: bold;}
	.ztree li span.button {background-image:url("${pageContext.request.contextPath }/css/ztree_css/left_menuForOutLook.png"); *background-image:url("${pageContext.request.contextPath }/css/ztree_css/left_menuForOutLook.gif")}
	.ztree li span.button.switch.level0 {width: 20px; height:20px}
	.ztree li span.button.switch.level1 {width: 20px; height:20px}
	.ztree li span.button.noline_open {background-position: 0 0;}
	.ztree li span.button.noline_close {background-position: -18px 0;}
	.ztree li span.button.noline_open.level0 {background-position: 0 -18px;}
	.ztree li span.button.noline_close.level0 {background-position: -18px -18px;}
</style>
</body>
</html>