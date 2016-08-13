<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>insertDemo</title>
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
<script type="text/javascript" src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.core-3.5.min.js"></script>
</head>
<body>
<app:dialogs/>
	  
<div class="container-fluid">
  <div class="row-fluid">
    <div class="span5">
      <div class="B-small-from-table-autoConcise">
			<h4 class="title" style="font-family:Microsoft YaHei;">定位工程 </h4>
      		<form method="post" id="menuForm">
			<table class="B-table" id="queryTable">
				<tr>
					<th width="5%" class="right-border bottom-border" style="font-weight:bold;vertical-align:inherit;font-family:Microsoft YaHei;font-size:14px;">工程名称</th>
					<td width="14%" class="right-border bottom-border">
						<input class="span12" type="text" placeholder="" name="prjName" id="prjname" operation="like" logic="and" ></td>
				
					<td width="4%"  class="text-left bottom-border text-right">
						<button	id="queryBtn" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
	            	</td>	
				</tr>
			
			</table>
			</form>
	  </div>
      <p></p>
	  <div class="span5">
	    <ul id="docTree" class="ztree" style="width:392px;">
	    	<img alt="请稍后，正在加载数据……" src="${pageContext.request.contextPath }/img/loading.gif" >
	    </ul>
	  </div>
    </div>
    <div class="span7" id="wdlb" style="height: 500px">
    	<iframe id="frame1" name="projectModel" width="100%" height="100%" src="${pageContext.request.contextPath }/jsp/business/wdgl/projectModel.jsp" border=1 framespacing=0 marginheight=0 marginwidth=0> </iframe>
    </div>
  </div>
</div>

<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/projectController.do";

//计算本页自适应高度
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-pageNumHeight;
	$("#wdlb").css("height",g_iHeight-15);
	$("#docTree").css("height",height+55);
	$("#docTree").css("width",$("#queryTable")[0].clientWidth-20);
};

var setting = {
	async: {
		enable: true,
		url: controllername + "?getAllProject",
		autoParam: ["id"],
		dataFilter: function (treeId, parentNode, responseData) {
   //         alert(responseData[0].parentId);
            return responseData;
        }
	},
	view: {
		dblClickExpand: false
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
		//	alert(treeNode.name);
			$("#frame1").attr("src" ,"${pageContext.request.contextPath }/jsp/business/wdgl/projectModel.jsp?name="+treeNode.name+"&id="+treeNode.id);
		}
	}
};

var zTree,rMenu,menuTreeJson,operatorSign;
$(document).ready(function() {
	$.fn.zTree.init($("#docTree"), setting);
	zTree = $.fn.zTree.getZTreeObj("docTree");
});

$(function() {
	setPageHeight();
	$("#queryBtn").click(function() {
		var projectName = $("#prjname").val();
		setting.async.url = controllername + "?getAllProject&prjName="+projectName;
	//	alert(setting.async.url);
		$.fn.zTree.init($("#docTree"), setting);
	});
});
</script>
</body>
</html>