<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>insertDemo</title>

		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/css/ztree_css/demo.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/css/ztree_css/zTreeStyle.css"
			type="text/css">
		<script type="text/javascript"
			src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.core-3.5.min.js"></script>
		<style type="text/css">
div#rMenu {
	position: absolute;
	visibility: hidden;
	top: 0px;
	/**background-color: #555;**/
	background-color: #DFDFDF;
	text-align: left;
	padding: 2px;
	width:150px;
}

div#rMenu ul li {
	margin: 5px 2px 5px 2px;
	padding: 0 5px;
	cursor: pointer;
	list-style: none outside none;
	background-color: #DFDFDF;
	color:blue;
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
	font-family:Microsoft YaHei,"Helvetica Neue", Helvetica, Arial, sans-serif;
}

</style>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span4">
					<div class=" left">
						<span style="font-size:10px;"></span>
							 
							<select class="span3 year" style="width: 25%;display: none;" id="ND" name="ND"  fieldname="ND" operation="=" noNullSelect ="true"></select>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button class="btn" id="btnQd">
								确定
							</button> 
						<ul id="menuTree" class="ztree" style="width: 420px; height: 410px">
							<img alt="请稍后，正在加载数据……"
								src="${pageContext.request.contextPath }/img/loading.gif" />
						</ul>
					</div>
				</div>
			</div>
		</div>
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter" order="asc" fieldname=""/>
         <input type="hidden" name="resultXML" id = "resultXML">
          <input type="hidden" name="resultEdit" id = "resultEdit">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
         <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/gcCjjhController.do";
var nd = "";
var menuTreeJson="";
var g_defaultOpenNodeID = "";
var zTree,rMenu,menuTreeJson,operatorSign;
var setting="";
var xmid="";
$(document).ready(function() {
	generateNd($("#ND"));
	setDefaultNd("ND");	
	set();
	$.fn.zTree.init($("#menuTree"), setting);
	zTree = $.fn.zTree.getZTreeObj("menuTree");
	rMenu = $("#rMenu");
	

});
	$(function() {
		//按钮绑定事件（确定）
		$("#btnQd").click(function() {
			if(""==xmid)
			 {
				requireSelectedOneRow();
			    return;
			 }
				var fuyemian=$(window).manhuaDialog.getParentObj();
				fuyemian.changeParentId(menuTreeJson);
				$(window).manhuaDialog.close();
		});
	
	});
function generateNd(ndObj){
	ndObj.attr("src","T#GC_CJJH:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(new Date().getFullYear());
}
function set()	{
	setting = {
		async: {
			enable: true,
			url: getAsyncUrl(),
			autoParam: ["id"],
			dataFilter: function (treeId, parentNode, responseData) {
	            return responseData;
	        }
		},
		view: {
			dblClickExpand: true,
			nameIsHTML: true
		},
		data: {
			simpleData: {
				enable: true,
				idKey: "id",
				pIdKey: "parentId"
			}
		},
		callback: {
			onClick  :queryByID 
			 
		}
	};
}
//点击查询列表
function queryByID(event, treeId, treeNode){
	menuTreeJson=treeNode;
	xmid=treeNode.id;
}
function getAsyncUrl(){
	nd=$("#ND").val();
	return controllername + "?getProjectfl"+g_defaultOpenNodeID+"&ISXM=0&nd="+nd;
}


 </script>

	</body>
</html>