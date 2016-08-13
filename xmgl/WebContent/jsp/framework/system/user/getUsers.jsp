<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<%
			String str = request.getParameter("checkNodeString");
			String callback = request.getParameter("callback");
			String type = request.getParameter("type");
			String dept = request.getParameter("dept");
			dept = dept==null ? "":dept;
			str = str==null || str==""?"":"&str="+str;
			callback = callback==null ? "":callback;
		%>
		<title>用户选择界面</title>

		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/css/ztree_css/demo.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/css/ztree_css/zTreeStyle.css"
			type="text/css">
		<script type="text/javascript"
			src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.all-3.5.min.js"></script>
		<style type="text/css">
			div#rMenu {
				position: absolute;
				visibility: hidden;
				top: 0;
				background-color: #555;
				text-align: left;
				padding: 2px;
			}
			
			div#rMenu ul li {
				margin: 1px 0;
				padding: 0 5px;
				cursor: pointer;
				list-style: none outside none;
				background-color: #DFDFDF;
			}
		</style>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid" align="center">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						<span class="pull-right">
							<button class="btn" id="btnSave">
								确定
							</button>
							<button class="btn" id="btnClear">
								清空
							</button> <!-- <button class="btn" id="btnCancel">关闭</button> --> </span>
					</h4>
					<div align="center">
						<div class="zTreeDemoBackground left"
							style="height: 100%; width: 98%;">
							<ul id="myTree" class="ztree"
								style="height: 100%; width: 100%; border: 0; background-color: #FFFFFF; overflow: auto;">
								<img alt="请稍后，正在加载数据……"
									src="${pageContext.request.contextPath }/img/loading.gif" />
							</ul>
						</div>
					</div>
					<!-- 右键菜单 -->
					<div id="rMenu">
						<ul>
							<li id="m_add" onclick="addMenu();">
								增加菜单
							</li>
							<li id="m_reset" onclick="updateMenu();">
								修改菜单
							</li>
							<li id="m_del" onclick="deleteMenu();">
								删除节点
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>

		<script type="text/javascript">
			var controllername= "${pageContext.request.contextPath }/userController.do";
			var checkNodeString = "<%=str %>";
			var type = "<%=type%>";
			var callback = "<%=callback%>";
			var dept = "<%=dept%>";
			var settingMulti = {
				check: {
					enable: true
				},
				async: {
					enable: true,
					url: controllername + "?loadAllUser"+checkNodeString+"&dept="+dept,
					autoParam: ["id"]
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
				}
			};
			var settingSingle = {
				async: {
					enable: true,
					url: controllername + "?loadAllUser&dept="+dept,
					autoParam: ["id"]
				},
				view: {
					dblClickExpand: false,
					selectedMulti: false
				},
				data: {
					simpleData: {
						enable: true,
						idKey: "id",
						pIdKey: "parentId"
					}
				}
			};
			//点击保存按钮
			$(function() {
				$("#btnSave").click(function() {
					var zTree = $.fn.zTree.getZTreeObj("myTree");
					// 获取输入框被勾选的节点集合
					var nodes;
					if(type=="single"){
						nodes = zTree.getSelectedNodes();
					}else{
						nodes = zTree.getCheckedNodes(true);
					}
					var data = new Array();
					for(var i = 0; i < nodes.length; i++) {
						if(nodes[i].treenode=="1"){
							var obj = new Object();
							obj.ACCOUNT = nodes[i].id;
							obj.USERNAME = nodes[i].name;
							obj.DEPTID = nodes[i].deptid;
							obj.TEL = nodes[i].tel;
							data.push(obj);
						}
					}
					if(callback!=""){
						$(window).manhuaDialog.getParentObj().eval(callback+"("+JSON.stringify(data)+")");
					}else{
						$(window).manhuaDialog.setData(data);
						$(window).manhuaDialog.sendData();
					}
					$(window).manhuaDialog.close();
				});
				//清空按钮事件
				$("#btnClear").click(function(){
					var zTree = $.fn.zTree.getZTreeObj("myTree");
					var nodes = zTree.checkAllNodes();
					var selectNodes = zTree.getSelectedNodes();
					if (selectNodes.length>0) { 
				        zTree.cancelSelectedNode(selectNodes[0]);
					}
				});
				$("#btnCancel").click(function(){
					$(window).manhuaDialog.close();
				});
			});
			$(document).ready(function() {
				doInit();
			});
			function doInit(){
				if(type=="single"){
					$.fn.zTree.init($("#myTree"), settingSingle);
					$("#btnClear").hide();
				}else{
					$.fn.zTree.init($("#myTree"), settingMulti);
				}
			}
		</script>
	</body>
</html>