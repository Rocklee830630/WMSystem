<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />

		<title>实例页面-角色信息查询</title>
		<style type="text/css">
			.treeFrame{
				width:100%;
				height:99%;
			}
		</style>
		<script type="text/javascript" charset="UTF-8">
	var id,json,rowindex,rowValue;
	var controllername= "${pageContext.request.contextPath }/roleController.do";
	$(function() {
		var btn = $("#queryBtn");
		btn.click(function() {
	        //生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryRole", data, DT1);
		});
	});

  	//计算本页表格分页数
  	function setPageHeight() {
  		var height = g_iHeight-pageTopHeight-pageTitle-getTableTh(1)-pageNumHeight;
  		var pageNum = parseInt(height/pageTableOne,10);
  		$("#DT1").attr("pageNum",pageNum);
  		$("#treeFrameDiv").attr("style","height:"+(g_iHeight-15)+"px");
  	}
	// 清除表单
	$(function() {
		$("#query_clear").click(function() {
	       $("#queryForm").clearFormResult();
		});
	});
	
	function tr_click(obj,tabListid) {
		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引
		rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
		id = obj.ROLE_ID;
		name = obj.NAME;
		$("#executeFrm").setFormValues(obj);
		var roletype = obj.ROLETYPE;
		if(roletype == 2) {
			$("#awardMenu").attr("disabled","disabled");
		} else {
			$("#awardMenu").removeAttr("disabled");
		}
	}

	//父页面调用查询页面的方法
	$(function() {
		setPageHeight();
		g_bAlertWhenNoResult = false;
		//授予人员权限方法
		$("#awardMenu").click(function() {
			if($("#DT1").getSelectedRowIndex()==-1) {
				 //xAlert("提示信息","请选择一条角色信息");
				 //requireSelectedOneRow();
		 		requireSelectedOneRow();
				return;
			}
			$(window).manhuaDialog({"title" : "角色管理>赋予权限","type" : "text","content" : "${pageContext.request.contextPath}/jsp/framework/system/role/awardMenu.jsp?roleId="+id,"modal":"4"});
		});
		//“加入”按钮方法
		$("#btnAdd").click(function() {
			var id = $("#treeid").val();
			if(id==""){
				xAlert("警告","请至少选中树形列表中的一个类别!","3");
				return;
			}else{
				$(window).manhuaDialog({"title":"角色类别管理>选择要加入的角色","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/system/role/roleCategory/roleList.jsp?treeid="+id,"modal":"1"});
			}
		});
	});
	//父页面调用查询页面的方法
	$(function() {
		$("#awardPerson").click(function() {
			if($("#DT1").getSelectedRowIndex()==-1) {
				 //xAlert("提示信息","请选择一条角色信息");
				 //requireSelectedOneRow();
		 		requireSelectedOneRow();
				return;
			}
			$(window).manhuaDialog({"title" : "角色管理>将角色授予人员>请选择菜单","type" : "text","content" : "${pageContext.request.contextPath}/jsp/framework/system/role/awardPerson.jsp?id="+id+"&name="+name,"modal":"2"});
		});
	});
	//---------------------------------------
	//-获取iframe中树形列表的选中节点
	//---------------------------------------
	function getIFrameSelectTreeNode(){
		$("#treeFrame").zTree.getSelectedNodes();
	}
	//---------------------------------------
	//-显示角色状态
	//---------------------------------------
	function doShowState(obj){
		var showHtml = "";
		var id = obj.FS_ORG_ROLE_TREE_ID;
		showHtml = "<a href='javascript:void(0);' title='点击移除' onclick='doRemove(\""+id+"\")'><i class='icon-minus' style='color:'></i></a>";
		return showHtml;
	}
	function doRemove(id){
	    var index = $(event.target).closest("tr").index();
		$("#DT1").cancleSelected();
    	$("#DT1").setSelect(index);
		var data1 = defaultJson.packSaveJson($("#DT1").getSelectedRow());
		defaultJson.doDeleteJson(controllername+"?doMoveOutTreeNode",data1,DT1,null); 
	}
	function doSearch(obj){
		$("#treeid").val(obj.id);
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		defaultJson.doQueryJsonList(controllername+"?queryRoleByTreeNode", data, DT1);
	}
	function showSuccess(){
		xAlertMsg("操作成功!");
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		defaultJson.doQueryJsonList(controllername+"?queryRoleByTreeNode", data, DT1);
	}
</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<p></p>
			<div class="row-fluid">
				<div class="span4" id="treeFrameDiv" >
					<iframe id="treeFrame" class="treeFrame" style="border-style:none;background:#FFF;box-shadow:none;" src="${pageContext.request.contextPath}/jsp/framework/system/role/roleCategory/showRoleTree.jsp"></iframe>
				</div>
				<div class="span8">
					<div class="B-small-from-table-autoConcise">
						<h4 class="title">
							角色信息列表
							<span class="pull-right">
								<button id="btnAdd" class="btn" type="button">
									加入
								</button>
								<button id="awardPerson" class="btn" type="button">
									授予人员权限
								</button>
							</span>
						</h4>
						<form method="post" id="queryForm">
							<table class="B-table" width="100%">
								<!--可以再此处加入hidden域作为过滤条件 -->
								<TR style="display: none;">
									<TH width="8%" class="right-border bottom-border">
										角色类别表主键
									</TH>
									<TD>
										<input type="text" class="span12" kind="text" id="treeid"
											fieldname="PARENTID" value="" operation="=">
									</TD>
								</TR>
								<!--可以再此处加入hidden域作为过滤条件 -->
							</table>
						</form>
	
						<div style="height: 5px;">
						</div>
						<div class="overFlowX">
							<table width="100%" class="table-hover table-activeTd B-table"
								id="DT1" type="single">
								<thead>
									<tr>
										<th name="XH" id="_XH">
											&nbsp;#&nbsp;
										</th>
										<th fieldname="NAME">
											&nbsp;角色名称&nbsp;
										</th>
										<th fieldname="ROLETYPE">
											&nbsp;角色类别&nbsp;
										</th>
										<th fieldname="S_MEMO">
											&nbsp;汉字描述&nbsp;
										</th>
										<th fieldname="ROLE_ID" CustomFunction="doShowState" tdalign="center">
											&nbsp;移除&nbsp;
										</th>
									</tr>
								</thead>
								<tbody></tbody>
							</table>
	
						</div>
					</div>
				</div>
			</div>
		</div>

		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank" id="frmPost">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id="queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>