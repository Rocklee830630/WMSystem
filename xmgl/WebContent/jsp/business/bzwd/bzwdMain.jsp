<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%
			String str = request.getParameter("checkNodeString");
			String callback = request.getParameter("callback");
			String type = "single";//request.getParameter("type");
			String dept = request.getParameter("dept");
			dept = dept==null ? "":dept;
			str = str==null || str==""?"":"&str="+str;
			callback = callback==null ? "":callback;
		%>
		<title>标准文档主页面</title>

		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/css/ztree_css/demo.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/css/ztree_css/zTreeStyle.css"
			type="text/css">
		<app:base />
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
					<div class="span5">
						<h4 class="title">
							<span class="pull-right">
								<button class="btn" id="btnSave">
									新增根节点
								</button>
								<button class="btn" id="btnAdd">
									新增文档节点
								</button>
								<button class="btn" id="btnEdit">
									修改节点
								</button>
								<!-- <button class="btn" id="btnDel">
									删除
								</button> <button class="btn" id="btnCancel">关闭</button> </span> -->
						</h4>
						<div align="center">
							<div class="zTreeDemoBackground left"
								style="height: 100%; width: 98%;">
								<ul id="myTree" class="ztree"
									style="height: 100%; width: 100%; border: 0; background-color: #FFFFFF; overflow: auto;">
									<li>
										<img alt="请稍后，正在加载数据……"
											src="${pageContext.request.contextPath }/img/loading.gif" />
									</li>
								</ul>
							</div>
						</div>
					</div>
					<div class="span7">
					<div style="height: 5px;">
					</div>
							<form method="post" action="" id="insertForm">
								<table class="B-table" width="100%">
									<!-- 这里需要一个隐藏域，存放比如：问题编号,接收人账号，接收单位等信息 -->
									<tr style="display: none;">
										<th width="8%" class="right-border bottom-border">
											主键
										</th>
										<th width="8%" class="right-border bottom-border">
											事件编号
										</th>
										<td width="42%" class="right-border bottom-border">
											<input class="span12" type="text" name="SJBH"
												fieldname="SJBH">
										</td>
										<th width="8%" class="right-border bottom-border">
											业务类型
										</th>
										<td width="42%" class="right-border bottom-border">
											<input class="span12" type="text" name="YWLX"
												fieldname="YWLX">
										</td>
									</tr>
									<tr>
										<th width="8%" class="right-border bottom-border text-right">
											附件文档
										</th>
										<td width="92%" colspan=7 class="bottom-border">
											<div>
												<span class="btn btn-fileUpload"
													onclick="doSelectFile(this);" fjlb="1060" id="wthBtn">
													<i class="icon-plus"></i> <span>添加文件...</span> </span>
												<table role="presentation" class="table table-striped">
													<tbody fjlb="1060" class="files showFileTab"
														data-toggle="modal-gallery" data-target="#modal-gallery">
													</tbody>
												</table>
											</div>
										</td>
									</tr>
								</table>
							</form>
					</div></element>
					<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true" />
				</div>
				<div style="display:none;">
					<input type="hidden" id="resultXML">
					<input class="span12" type="text" id="ywid" name="ywid" fieldname="ywid">
				</div>
			</div>
		</div>
		<script type="text/javascript">
			var controllername= "${pageContext.request.contextPath }/BzwdController.do";
			var checkNodeString = "<%=str %>";
			var type = "<%=type%>";
			var callback = "<%=callback%>";
			var wdid,json;
			var wddj='';
			var bzwdid='';
			//默认展开id;
			var parentid='';
			var settingSingle = {
				async: {
					enable: true,
					url: controllername + "?queryWdTree&parentid="+parentid,
					autoParam: ["id"]
				},
				view: {
					dblClickExpand: false,
					selectedMulti: false
				},
				callback: {
					onClick  : query,
				},
				data: {
					simpleData: {
						enable: true,
						idKey: "id",
						pIdKey: "parentId"
					}
				}
			};
	//初始化页面
	$(function() {
		//新增根节点
		$("#btnSave").click(function() {
			 $(window).manhuaDialog({"title":"文档节点>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bzwd/bzwdAdd.jsp?wdid=0&wddj=1","modal":"4"});
		});
		//新增文档节点
		$("#btnAdd").click(function() {
			if(wddj!=1)
				{
				xInfoMsg('请选择正确的父节点！');
				return;
				}
			 $(window).manhuaDialog({"title":"文档节点>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bzwd/bzwdAdd.jsp?wddj=2&wdid="+wdid,"modal":"4"});
		});
		//修改文档节点
		$("#btnEdit").click(function() {
			if(''==wddj)
				{
				requireSelectedOneRow();
				return;
				}
			$("#resultXML").val(json);
			$(window).manhuaDialog({"title":"文档节点>修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bzwd/bzwdAdd.jsp","modal":"4"});
		});
		/*//删除文档节点
		$("#btnDel").click(function() {
			
			 if(index==-1){
				requireSelectedOneRow();
				return;
			}else{
				xConfirm("提示信息","是否确认删除！");
				$('#ConfirmYesButton').unbind();
				$('#ConfirmYesButton').one("click",function(){ 
					defaultJson.doDeleteJson(controllername+"?deleteDyqk",data1,DT1,"deleteHuiDiao"); 
				});
			} 
		}); */
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
		$("#wthBtn").hide();
	});
	function doInit(){
		if(type=="single"){
			
			$.fn.zTree.init($("#myTree"), settingSingle);
		}else{
			$.fn.zTree.init($("#myTree"), settingMulti);
		}
	}
	//子页面调用
	function ziyemian(parentid,wdid)
	{
		var aa=wdid;
		bzwdid=aa;
		var ziyemianSettingSingle = {
				async: {
					enable: true,
					url: controllername + "?queryWdTree&parentid="+parentid,
					autoParam: ["id"]
				},
				view: {
					dblClickExpand: false,
					selectedMulti: false
				},
				callback: {
					onClick  : query,
					//用于捕获异步加载正常结束的事件回调函数
					onAsyncSuccess: querySuccess
				},
				data: {
					simpleData: {
						enable: true,
						idKey: "id",
						pIdKey: "parentId"
					}
				}
			};
		$.fn.zTree.init($("#myTree"), ziyemianSettingSingle);
		wddj='';
		wdid='';
		
	}
	//加载完默认选中
	function querySuccess(event, treeId, treeNode, msg)
	{
		var treeObj = $.fn.zTree.getZTreeObj("myTree");
		//通过id查对用的节点
		var nodes = treeObj.getNodesByParam("id", bzwdid, null);;
		//默认选中节点
		treeObj.selectNode(nodes[0]);
		query(event, treeId, nodes[0]);
		//当前选中id清空
		bzwdid='';
	}
//点击查询
	function query(event, treeId, treeNode){
		//点击节点的查询  添加子节点
		wddj = treeNode.wddj;
		wdid=treeNode.id;
		var ywid1=treeNode.bzwdid;
		var sjbh=treeNode.sjbh;
		var ywlx=treeNode.ywlx;
		$("#ywid").val(ywid1);
		
		 //附件上传
		deleteFileData(ywid1,"","","");
		setFileData(ywid1,"",sjbh,ywlx);
		//查询附件信息
		queryFileData(ywid1,"","","");
		 
		json=JSON.stringify(treeNode);
		 if(wddj!='2'){
			$("#wthBtn").hide();
		}else{
			$("#wthBtn").show();
		}
	}
		</script>
	</body>
</html>