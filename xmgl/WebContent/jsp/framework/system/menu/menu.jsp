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
<style type="text/css">
	div#rMenu {position:absolute; visibility:hidden; top:0; background-color: #555;text-align: left;padding: 2px;}
	div#rMenu ul li{
		margin: 1px 0;
		padding: 0 5px;
		cursor: pointer;
		list-style: none outside none;
		background-color: #DFDFDF;
	}
	th {
	font-weight: bold;
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
</head>
<body>
<app:dialogs/>
	  
<div class="container-fluid">
  <div class="row-fluid">
    <div class="span3">
    <p></p>
	  <div class="zTreeDemoBackground left">
	    <ul id="menuTree" class="ztree">
	    	<img alt="请稍后，正在加载数据……" src="${pageContext.request.contextPath }/img/loading.gif" />
	    </ul>
	  </div>
	  
	  <!-- 右键菜单 -->
	  <div id="rMenu">
		<ul>
			<li id="m_add" onclick="addMenu();">增加菜单</li>
			<li id="m_fresh" onclick="freshMenu()">刷新</li>
			<li id="m_reset" onclick="updateMenu();">修改菜单</li>
			<li id="m_del" onclick="deleteMenu();">删除节点</li>
		</ul>
	  </div>
    </div>
    <div class="span9">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title" id="h4">菜单管理
		    <span class="pull-right">
		    <!-- 	<button class="btn" id="refreshMenuCache">清除菜单缓存</button> -->
		    	<button class="btn" id="saveUserInfo">保存</button>
		    </span>
			</h4>
			
		<form method="post" id="queryForm"  >
		<table style="width: 100%" class="B-table">
		<!--可以再此处加入hidden域作为过滤条件 -->
			<TR  style="display:none;">
				<TD class="right-border bottom-border"></TD>
				<TD class="right-border bottom-border">
					<input type="text" fieldname="LEVELNO" id="LEVELNO" name="LEVELNO" />
				</TD>
			</TR>
		<!--可以再此处加入hidden域作为过滤条件 -->
			<tr>
				<th width="5%">菜单别名（英文、数字）</th>
				<td width="10%">
					<input type="text" placeholder="必填" check-type="required" id="NAME" name="NAME" fieldname="NAME" ></td>
			</tr>
			<tr>
				<th width="5%">菜单标题（汉字）</th>
				<td width="10%">
					<input type="text" placeholder="必填" check-type="required" id="TITLE" name="TITLE" fieldname="TITLE" ></td>
			</tr>
			<tr>
				<th width="5%">父级菜单编码</th>
				<td width="10%">
					<input type="text" id="PARENT" name="PARENT" fieldname="PARENT" ></td>
			</tr>
			<tr>
				<th width="5%">显示顺序号</th>
				<td width="10%">
					<input type="number" placeholder="" check-type="" id="ORDERNO" name="ORDERNO" fieldname="ORDERNO" ></td>
			</tr>
			<tr>
				<th width="5%">页面显示区域</th>
				<td width="10%">
					<select type="text" placeholder="必填" check-type="required" id="TARGET" name="TARGET" fieldname="TARGET" kind="dic" src="YMXSQY"></select></td>
			</tr>
			<tr>
				<th width="5%">页面web路径</th>
				<td width="10%">
					<input type="text" placeholder="必填" check-type="required" id="LOCATION" name="LOCATION" fieldname="LOCATION" ></td>
			</tr>
		</table>
		</form>
	
		</div>
		
	
		  选中要操作的节点右键，即可操作菜单
	</div>
  </div>
</div>

<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/menuController.do";
var setting = {
	async: {
		enable: true,
		url: controllername + "?getAllMenu",
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
		onRightClick  : showMenuValue
	}
};

/**
 * 点击右键时，获取的菜单信息
 */
function showMenuValue(event, treeId, treeNode) {
	menuTreeJson = treeNode;
	if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
		// 取消选中节点
		zTree.cancelSelectedNode();
		showRMenu("root", event.clientX, event.clientY);
	} else if (treeNode && !treeNode.noR) {
		// 设置选中节点
		zTree.selectNode(treeNode);
		showRMenu("node", event.clientX, event.clientY);
	}
}

/**
 * 显示右键按钮
 * type	 
 * x	右键时所在的x坐标
 * y	右键时所在的y坐标
 */
function showRMenu(type, x, y) {
//	alert("showRMenu : " + type);
	$("#rMenu ul").show();
	if (type=="root") {
		$("#m_add").hide();
		$("#m_fresh").hide();
		$("#m_reset").hide();
		$("#m_del").hide();
	} else {
		var levelno = menuTreeJson.levelno;
		// 根节点只能添加
		if(levelno == 0) {
			$("#m_add").show();
			$("#m_fresh").show();
			$("#m_reset").hide();
			$("#m_del").hide();
		// 第三层节点不允许再添加
		} else if(levelno == 3) {
			$("#m_add").hide();
			$("#m_fresh").hide();
			$("#m_reset").show();
			$("#m_del").show();
		// 其他正常
		} else {
			$("#m_add").show();
			$("#m_fresh").hide();
			$("#m_reset").show();
			$("#m_del").hide();
		}
	}
	rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});

	$("body").bind("mousedown", onBodyMouseDown);
}

/**
 * 隐藏右键按钮
 */
function hideRMenu() {
	if (rMenu) rMenu.css({"visibility": "hidden"});
	$("body").unbind("mousedown", onBodyMouseDown);
}

function onBodyMouseDown(event) {
	if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
		rMenu.css({"visibility" : "hidden"});
	}
}

/**
 * 添加按钮方法
 */
function addMenu() {
//	$("#h4").html("添加菜单");
	hideRMenu();
	$("#PARENT").val(menuTreeJson.id);
	var thisLevel = menuTreeJson.levelno;
	var nextLevel = parseInt(thisLevel)+1;
	$("#LEVELNO").val(nextLevel);

	$("#NAME").removeAttr("readonly");
	$("#PARENT").attr("readonly", "readonly");

	$("#NAME").val("");
	$("#TITLE").val("");
	$("#TARGET").val("");
	$("#LOCATION").val("");
	
	// 设置操作标识符，1表示添加 
	operatorSign = 1;
}

/**
 * 修改按钮方法
 */
function updateMenu() {
//	$("#h4").html("修改菜单");
	hideRMenu();
	$("#NAME").val(menuTreeJson.id);
	$("#TITLE").val(menuTreeJson.name);
	$("#PARENT").val(menuTreeJson.parentId);
	$("#TARGET").val(menuTreeJson.target);
	$("#LOCATION").val(menuTreeJson.location);
	$("#ORDERNO").val(menuTreeJson.orderno);
	$("#LEVELNO").val(menuTreeJson.levelno);

	$("#NAME").attr("readonly", "readonly");
	$("#PARENT").attr("readonly", "readonly");

	
	// 设置操作标识符，2表示修改
	operatorSign = 2;
}

function freshMenu() {
	$.fn.zTree.init($("#menuTree"), setting);
	hideRMenu();
}

//点击保存按钮
$(function() {
	var saveUserInfoBtn = $("#saveUserInfo");
	saveUserInfoBtn.click(function() {
		if(!operatorSign) {
			//xAlert("提示信息","请选择一个父节点进行添加");
			xInfoMsg("请选择一个父节点进行添加","");
			return;
		}
 		if(sign == 1) {
			 //xAlert("提示信息","登录用户名重复，请重新填写");
			 xInfoMsg("登录用户名重复，请重新填写","");
			 return;
		}
		if($("#queryForm").validationButton()) {
			//生成json串
			var data = Form2Json.formToJSON(queryForm);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			//通过判断id是否为空来判断是插入还是修改
			
			var success = false;
			if(operatorSign == 1) {
				success = defaultJson.doInsertJson(controllername + "?executeMenu&operatorSign=" + operatorSign, data1, null);
			} else if(operatorSign == 2) {
				success = defaultJson.doUpdateJson(controllername + "?executeMenu&operatorSign=" + operatorSign, data1, null);
			}
			$.fn.zTree.init($("#menuTree"), setting);
		}
	});
});

// 检测菜单信息是否重复
var sign;
$(function() {
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