<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<title>insertDemo</title>

<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/ztree_css/demo.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/ztree_css/zTreeStyle.css" type="text/css">
<app:base/>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.core-3.5.min.js"></script>
<style>
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
	  
<div class="container-fluid">
  <div class="row-fluid">
  
  
	  <div class="zTreeDemoBackground left span3">
	    <ul id="dictTree" class="ztree" style="background-color:#FFFFFF;margin:10px 27px 0px 27px;">
	    	<img alt="请稍后，正在加载数据……" src="${pageContext.request.contextPath }/img/loading.gif" />
	    </ul>
	  </div>
    
    
    	<div class="B-small-from-table-autoConcise span9">
    	
    		<h4 class="title">
				字典列表  
				<span class="pull-right">
					<button class="btn" id="insertBtn">新增</button>
					<button class="btn" id="updateBtn">修改</button>
				</span>
			</h4>
			
			<form method="post" id="queryForm">
    			<INPUT style="display:none;" type="text" class="span12" kind="text" id="PARENT_ID" fieldname="PARENT_ID" value="" operation="="/>
    		</form>
			
    			<div style="height:5px;"> </div>
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" pageNum="5">
	                <thead>		                		
	                    <tr>
	                     	<th  name="XH" id="_XH" width="30">&nbsp;#&nbsp;</th>
	                     	<th fieldname="DIC_CODE">&nbsp;字典编号&nbsp;</th>
	                     	<th fieldname="DIC_VALUE">&nbsp;字典值&nbsp;</th>
	                     	<th fieldname="DIC_VALUE2">&nbsp;字典说明&nbsp;</th>
	                     	<th fieldname="PXH">&nbsp;排序号&nbsp;</th>
	                	</tr>
	                </thead> 
	              	<tbody></tbody>
	           </table>
    </div>
    
    
  </div>
</div>
<script type="text/javascript">

var controllername = "${pageContext.request.contextPath }/dictController.do";

var setting = {
	async: {
		enable: true,
		url: controllername + "?getCustomDict",
		autoParam: ["id"],
		dataFilter: function (treeId, parentNode, responseData) {
            return responseData;
        }
	},
	data: {
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "parent_Id",
		}
	},
	callback: {
		onClick  : query
	},
	view:{
		showLine :false
	}
};

$(document).ready(function() {
	setPageHeight();
	$.fn.zTree.init($("#dictTree"), setting);
});

//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
	$("#dictTree").css("height",g_iHeight-25);
}

function query(event, treeId, treeNode){
	var id = treeNode.id;
	//var parentId = treeNode.parentId;
	$("#PARENT_ID").val(id);
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?getDicsByParent",data,DT1);
}

function queryDics(){
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?getDicsByParent",data,DT1);
}

	// 点击添加按钮 清空表单
	$(function() {
		var btn = $("#insertBtn");
		btn.click(function() {
			if(!$("#PARENT_ID").val()) {
				xInfoMsg('请在左侧选择一个父级字典！');
				$('#dicModal').modal('hide');
				return false;
			}
			$("#dicModalLabel").html("字典管理>新增");
			$("#dicFrm").clearFormResult(); 
			var parentId = $("#PARENT_ID").val();
			$("#dicParentId").val(parentId);
			$("#PXH").val('999');
			$("#dicModal").modal("show");
		});
		
		var btnUpdate = $("#updateBtn");
		btnUpdate.click(function() {
			if($("#DT1").getSelectedRowIndex()==-1) {
				requireSelectedOneRow();
				$("#dicModal").modal("hide");
				return false;
			} 
			var obj = $("#DT1").getSelectedRowJsonObj();
			$("#dicFrm").setFormValues(obj);
			//btnUpdate.attr("data-toggle","modal");
			$("#dicModalLabel").html("字典管理>修改");
			$('#dicModal').modal('show');
		});
		
		var saveInfoBtn = $("#saveDic");
		saveInfoBtn.click(function() {
			if($("#dicFrm").validationButton()) {
				//生成json串
				var data = Form2Json.formToJSON(dicFrm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				try{
					defaultJson.doUpdateJson(controllername + "?insertOrUpdate", data1, DT1);
					var data = $("#frmPost").find("#resultXML").val();
					data = defaultJson.dealResultJson(data);
					var index = $("#DT1").getSelectedRowIndex();
					if(index == -1) {
						$("#DT1").insertResult(JSON.stringify(data),DT1,1);
						$("#DT1").cancleSelected();
						$("#DT1").setSelect(0);
					}else{
						var comprisesJson = $("#DT1").comprisesJson(data,index);
						$("#DT1").updateResult(JSON.stringify(comprisesJson),DT1,index);
						$("#DT1").cancleSelected();
						$("#DT1").setSelect(index);
					}
					
				}catch(e){}
				$("#dicModal").modal("hide");
				queryDics();
				//successInfo();
			}
		});
		
	});
	
	function getInput(){//屏蔽非数字和非退格符
	    var k = event.keyCode;   //48-57是大键盘的数字键，96-105是小键盘的数字键，8是退格符←，37是光标左移键，39是光标右移键
	    if ((k <= 57 && k >= 48) || (k <= 105 && k >= 96) || (k == 8)||(k==37)||(k==39)){
	     return true;
	    } else {
	     return false;
	    }
	}
	function Set(obj){
	}
</script>

<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="asc" fieldname="PXH"/>
		<input type="hidden" name="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
</div>

<!-- Modal -->
<div id="dicModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="dicModalLabel" aria-hidden="true">
  <div class="modal-header" style="background:#0866c6;">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-remove icon-white"></i></button>
    <h3 id="myModalLabel" style="color:white;">字典信息</h3>
  </div>
  <div class="modal-body">
    <form method="post" id="dicFrm">
      <table class="B-table" >
      	<TR  style="display:none;">
      		<td>
				<input type="text" style="display:none;" kind="text" fieldname="ID" >
				<input type="text" id="dicParentId" style="display:none;" kind="text" fieldname="PARENT_ID">
      		</td>
        </TR>
        <tr>
          <th width="30%" class="right-border bottom-border">字典值</th>
          <td class="right-border bottom-border">
          	<input type="text" placeholder="必填"  check-type="required" maxlength="100" fieldname="DIC_VALUE" name="DIC_VALUE" id="DIC_VALUE"></input>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">字典说明</th>
          <td class="right-border bottom-border">
          	<input type="text" placeholder="必填"  check-type="required" maxlength="100" fieldname="DIC_VALUE2" name="DIC_VALUE2" id="DIC_VALUE2"></input>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">排序号</th>
          <td class="right-border bottom-border">
          	<input type="number" placeholder="必填"  check-type="required maxlength" maxlength="3" fieldname="PXH" name="PXH" id="PXH" onkeydown="return getInput();" onkeyup="Set(this);" min="0"></input>
        </tr>
      </table>
    </form>
  </div>
  <div class="modal-footer">
    <button class="btn" id="saveDic">保存</button>
    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
  </div>
</div>

</body>
</html>