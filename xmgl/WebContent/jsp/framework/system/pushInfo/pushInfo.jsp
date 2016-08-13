<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>insertDemo</title>

<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/ztree_css/demo.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/ztree_css/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.core-3.5.min.js"></script>
</head>
<body>
<app:dialogs/>
	  
<div class="container-fluid">
  <div class="row-fluid">
    <div class="span3" >
	  <div class="zTreeDemoBackground left" style="height: 500px;">
	    <ul id="menuTree" class="ztree" style="overflow:hidden;background-color:#FFFFFF;margin:10px 27px 0px 27px;>
	    	<img alt="请稍后，正在加载数据……" src="${pageContext.request.contextPath }/img/loading.gif" />
	    </ul>
	  </div>
    </div>
    <div class="span9" id="personDiv" style="height: 500px;">
		<iframe id="iFrame" name="pushInfoAwardPerson" width="100%" height="100%" src="${pageContext.request.contextPath }/jsp/framework/system/pushInfo/awardPerson.jsp" frameborder="0" framespacing=0 marginheight=0 marginwidth=0> </iframe>
	</div>
  </div>
</div>

<script type="text/javascript">

//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-23;
	$("#menuTree").css("height",height);	
	$("#personDiv").css("height",height);	
}

var controllername= "${pageContext.request.contextPath }/pushInfoController.do";
var setting = {
	async: {
		enable: true,
		url: controllername + "?getPushInfoMenu",
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
		onClick  : showAwardedPerson
	}
};

function showAwardedPerson(event, treeId, treeNode) {
	var id = treeNode.id;
	var parentId = treeNode.parentId;
	var name = treeNode.name;
	if(!(treeNode!=null&&treeNode.children!=null&&treeNode.children.length!=null&&treeNode.children.length>0)) {
		$("#iFrame").attr("src", "${pageContext.request.contextPath }/jsp/framework/system/pushInfo/awardPerson.jsp?id="+id+"&name="+name);
	} else {
		$("#iFrame").attr("src", "${pageContext.request.contextPath }/jsp/framework/system/pushInfo/awardPerson.jsp");
	}
}

// 检测菜单信息是否重复
var sign;
$(function() {
	setPageHeight();
	var menuName = $("#NAME");
	menuName.blur(function() {
		$.ajax({
			url : controllername+"?queryUnique&menuName="+menuName.val(),
			cache : false,
			async : false,
			dataType : 'json',
			success : function(response) {
				var result = eval("(" + response + ")");
				// 设置添加时得校验标识，1表示数据重复
				sign = result.sign;
				//xAlert("提示信息", result.isUnique);
				xInfoMsg(result.isUnique,"");
			}
		});
	});
});

var zTree,rMenu,menuTreeJson,operatorSign;
$(document).ready(function() {
	$.fn.zTree.init($("#menuTree"), setting);
	zTree = $.fn.zTree.getZTreeObj("menuTree");
	rMenu = $("#rMenu");
});
</script>

</body>
</html>