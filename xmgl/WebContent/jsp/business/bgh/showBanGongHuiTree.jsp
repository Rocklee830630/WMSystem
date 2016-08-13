<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>insertDemo</title>
		<%
	String id = request.getParameter("id");
%>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/css/ztree_css/demo.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/css/ztree_css/zTreeStyle.css"
			type="text/css">
		<script type="text/javascript"
			src="${pageContext.request.contextPath }/js/base/jquery.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.core-3.5.min.js"></script>
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
		<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/banGongHuiController.do";
var nodeLeve=false;
//计算本页自适应高度
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle;
	$("#wdlb").css("clientHeight",$("#aaaaaa")[0].clientHeight+"");
	//height+$("#queryTable")[0].clientHeight
	//alert(document.documentElement.clientHeight+"|"+g_iHeight+"|"+$("#wdlb")[0].clientHeight+"|"+$("#frame1")[0].clientHeight+"|"+height);
	$("#docTree").css("height",g_iHeight-26);
	$("#docTree").css("width",$("#biaoti")[0].clientWidth-20);
};

var setting = {
	async: {
		enable: true,
		url: controllername + "?querybanGongHui",
		autoParam: ["id"],
		dataFilter: function (treeId, parentNode, responseData) {
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
			var parentid = treeNode.PARENT_ID;
			gc_bgh_id = treeNode.id;
			if(parentid!="0"){
				window.parent.query(treeNode);
				nodeLeve=true;
			}
		},
		beforeClick: beforeClick
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
	//iframeAutoFit();
	
	var treeObj = $("#docTree");
	treeObj.hover(function () {
		if (!treeObj.hasClass("showIcon")) {
			treeObj.addClass("showIcon");
		}
	}, function() {
		treeObj.removeClass("showIcon");
	});
	
});
function queryTree() {
	$.fn.zTree.init($("#docTree"), setting);
	zTree = $.fn.zTree.getZTreeObj("docTree");
}

function setStyle(){}
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

var gc_bgh_id = "";
$(function() {
	setPageHeight();
	$("#updateBtn").click(function() {
		window.parent.editbgh(nodeLeve);
	});
	$("#endBtn").click(function() {
		if(nodeLeve){
			window.parent.endBGH(nodeLeve);
		}else{
			window.parent.jinggao();
		}
	}); 
	
	$("#deleteBtn").click(function() {
		if(nodeLeve){
			window.parent.deleteAlert(gc_bgh_id);
		}else{
			window.parent.jinggao();
		}
	});
});
function getNodeData(){
	var nodeObj = new Object();
	nodeObj["nodelevel"] = nodeLeve;
	nodeObj["id"] = gc_bgh_id;
	return nodeObj;
}
/* function xiugai(){
	window.parent.editbgh(nodeLeve);
}
function end(){
	if(nodeLeve){
		window.parent.endBGH(nodeLeve);
	}else{
		window.parent.jinggao();
	}
} */
</script>
	</head>
	<body>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12" style="height: 100%" id="aaaaaa">
					<div style="height:5px;"></div>
					<div class="B-small-from-table-autoConcise">
						<!--<h4 class="title" style="text-align: center;" id="biaoti">
								 <button class="btn" id="addBtn">增加</button> -->
								<!-- <a href="javascript:void(0)" id="updateBtn" style="color: blue;padding-left: 10px;" onclick="xiugai()">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;
								<a href="javascript:void(0)" id="endBtn" style="color: blue;" onclick="end()">完成</a> 
								<button  type="button"  class="btn btn-link" id="deleteBtn">删除</button>
								<button  type="button"  class="btn btn-link" id="updateBtn">修改</button>
								<button  type="button" class="btn btn-link" id="endBtn">结束会议</button>
						</h4>-->
					</div>
					<p></p>
					<div class="zTreeDemoBackground left">
						<ul id="docTree" class="ztree"
							style="width: 300px; background-color: #FFFFFF; border: 0px; margin-top: 5px; overflow-x: hidden;">
							<li>
								<img alt="请稍后，正在加载数据……"
									src="${pageContext.request.contextPath }/img/loading.gif">
							</li>
						</ul>
				</div>
				</div>
			</div>
		</div>
	</body>
</html>