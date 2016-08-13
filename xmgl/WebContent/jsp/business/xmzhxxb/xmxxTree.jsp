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
<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-header" style="background:#0866c6;">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-remove icon-white"></i></button>
				<h3 id="myModalLabel" style="color:white;">
					请输入项目分类名称
				</h3>
			</div>
			<div class="modal-body">
				<table class="B-table">
					<tr>
						<th width="30%" class="right-border bottom-border">
							项目分类名称
						</th>
						<td class="right-border bottom-border">
							<input type="text" placeholder="" id="processname">
						</td>
					</tr>
				</table>
			</div>
			<div class="modal-footer">
				<button class="btn" id="saveName">
					确定
				</button>
				<button class="btn" data-dismiss="modal" aria-hidden="true">
					关闭
				</button>
			</div>
		</div>
	  </div>
		      

  <br>
  <div class="row-fluid">
    
					
					
	  <div class="span5">
      <p></p>
	  <div class="zTreeDemoBackground left" >
	  
	   <ul id="menuTree" class="ztree" style="width:420px;height:500px" >
	    	<img alt="请稍后，正在加载数据……" src="${pageContext.request.contextPath }/img/loading.gif" />
	    </ul>
	  </div>
	   <div id="rMenu">
		<ul>
			<li id="m_add" onclick="addXm();">增加项目类别</li>
			<li id="m_reset" onclick="updateXm();">修改项目类别</li>
			<li id="m_imp" onclick="impXm()">导入子项目</li>
			<li id="m_del" onclick="deleteXm();">删除项目类别</li>
		</ul>
	  </div>
	  </div>
	  <div class="span7">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title" id="h4">项目分类管理
		    <span class="pull-right">
		   
		    </span>
			</h4>
			
		<form method="post" id="queryForm"  >
		<table style="width: 80%" class="B-table">
		
			<tr>
				<th width="5%">年度</th>
				<td width="10%">
					<select	id="ND" class="span12 year" name="ND"  ></select>	</td>
			</tr>
			
		</table>
		</form>
	
		</div>
		
		  选中要操作的节点右键，即可管理项目
	</div>
	  
  </div>
  </div>

 
 
 
 
</div>

<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/projectController.do";
var nd = "";
var setting = {
	async: {
		enable: true,
		url: getAsyncUrl,
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
function getAsyncUrl(){
	return controllername + "?getProjectfl&nd="+nd;
}



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
		$("#m_imp").hide();
		$("#m_reset").hide();
		$("#m_del").hide();
	} else {
		var ly = menuTreeJson.ly;
		// 根节点只能添加
		if(ly == 1) {
			$("#m_add").show();
			$("#m_imp").show();
			$("#m_reset").show();
			$("#m_del").show();
		} else if(ly == 0) {
			$("#m_add").hide();
			$("#m_imp").hide();
			$("#m_reset").hide();
			$("#m_del").show();
		
		}else{
			$("#m_add").show();
			$("#m_imp").show();
			$("#m_reset").hide();
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
function addXm() {
//	$("#h4").html("添加菜单");
	hideRMenu();
	$("#myModal").modal("show");
	operatorSign = 1;

}
function deleteXm(){
	hideRMenu();
	operatorSign = 3;
	$.ajax({
		url : controllername+"?addFl&parent="+menuTreeJson.id+"&flmc="+processname+"&operatorSign="+operatorSign,
		cache : false,
		async : false,
		dataType : 'json',
		success : function(response) {
			var result = response.flag;
			var msg = response.msg;
			if(result=="1"){
				xAlert("提示信息",msg);
				$.fn.zTree.init($("#menuTree"), setting);
			}else{
				xAlert("提示信息",msg,'2');
			}
		}
	});
}

/**
 * 修改按钮方法
 */
function updateXm() {
	hideRMenu();
	$("#processname")[0].value=menuTreeJson.name;
	$("#myModal").modal("show");

	// 设置操作标识符，2表示修改
	operatorSign = 2;
}
function impXm(){
	hideRMenu();
	var id = menuTreeJson.id;
	$(window).manhuaDialog({"title":"子项目选择","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmzhxxb/xmxxSelect.jsp?id="+id+"&nd="+nd,"modal":"1"});
}

function freshMenu() {
	$.fn.zTree.init($("#menuTree"), setting);
	hideRMenu();
}
function closeParentCloseFunction(){
	$.fn.zTree.init($("#menuTree"), setting);
	hideRMenu();
}

//年份查询
function generateNd(ndObj){
	ndObj.attr("src","T#GC_JH_SJ:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(new Date().getFullYear());
}

//点击保存按钮
$(function() {
	generateNd($("#ND"));
	nd = $("#ND").val();
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
	$('#ND').change(function(){
		nd = $("#ND").val();
		freshMenu();
	});
	
	$("#saveName").click(function() {
		var processname = $("#processname").val();
		if(processname==""){
			xAlert("提示信息",'请输入项目分类名称!','3');
			return
		}
		var nd_select =  $("#ND").val();
		
		$.ajax({
			url : controllername+"?addFl&parent="+menuTreeJson.id+"&flmc="+processname+"&operatorSign="+operatorSign+"&nd="+nd_select,
			cache : false,
			async : false,
			dataType : 'json',
			success : function(response) {
				var result = response.flag;
				var msg = response.msg;
				if(result=="1"){
					xAlert("提示信息",msg);
					$.fn.zTree.init($("#menuTree"), setting);
				}else{
					xAlert("提示信息",msg,'2');
				}
			}
		});
		
		$("#myModal").modal("hide");
		$("#processname")[0].value="";
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