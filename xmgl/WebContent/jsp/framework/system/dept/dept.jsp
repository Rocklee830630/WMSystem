<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>

<title>实例页面-部门信息查询</title>

<script type="text/javascript" charset="UTF-8">
 var id,json,rowindex,rowValue;
  var controllername= "${pageContext.request.contextPath }/deptController.do";
	$(function() {
		var btn = $("#queryBtn");
		btn.click(function() {
	        //生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryDept", data, DT1);
		});
	});
	
	function init() {
        //生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryDept", data, DT1);
	}

  	//计算本页表格分页数
  	function setPageHeight(){
  		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
  		var pageNum = parseInt(height/pageTableOne,10);
  		$("#DT1").attr("pageNum",pageNum);
  	}
  	
	//页面默认参数
	$(document).ready(function() { 
		setPageHeight();
        //生成json串
        g_bAlertWhenNoResult = false;
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryDept", data, DT1);
        g_bAlertWhenNoResult = true;
	}); 
	
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
		id = obj.ROW_ID;

		$("#executeFrm").setFormValues(obj);
	//	alert(rowindex);
	}
	
	// 点击添加按钮 清空表单
	$(function() {
		var btn = $("#insertBtn");
		btn.click(function() {
			//$("#myModalLabel").html("添加部门信息");
			$("#myModalLabel").html("部门管理>新增");
			// 当添加用户信息时，解除ID为【ACCOUNT】的disabled属性
			$("#executeFrm").clearFormResult(); 
			id = "null";
			$("#EXTEND1").val("0");
			$("#SORT").val("999");
		});
	});
	
	// 点击修改按钮
	$(function() {
		var btn = $("#updateBtn");
		btn.click(function() {
			if($("#DT1").getSelectedRowIndex()==-1) {
				requireSelectedOneRow();
				// 没有选中结果集行的时候，不显示弹出层，将【修改用户信息】按钮的【data-toggle】属性修改成空即可
				btn.attr("data-toggle","");
			} else {
				// 选中时，需要恢复【修改用户信息】按钮的【data-toggle】属性值
				btn.attr("data-toggle","modal");
				//$("#myModalLabel").html("修改部门信息");
				$("#myModalLabel").html("部门管理>修改");
			}
		});
	});
	
	// 点击领导信息按钮
	$(function() {
		var btn = $("#leaderBtn");
		btn.click(function() {
			if($("#DT1").getSelectedRowIndex()==-1) {
				requireSelectedOneRow();
				// 没有选中结果集行的时候，不显示弹出层，将【修改用户信息】按钮的【data-toggle】属性修改成空即可
				btn.attr("data-toggle","");
			} else {
				var obj = $("#DT1").getSelectedRowJsonObj();
				var department = obj.ROW_ID;
				$("#FZR").attr("src","T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG='1' AND PERSON_KIND='3' AND DEPARTMENT='"+department+"'");
				reloadSelectTableDic($("#FZR"));
				$("#leaderFrm").setFormValues(obj);
				// 选中时，需要恢复【修改用户信息】按钮的【data-toggle】属性值
				btn.attr("data-toggle","modal");
				//$("#myModalLabel").html("修改部门信息");
				$("#leaderModalLabel").html("部门管理>领导指定");
			}
		});
	});
	
	// 领导信息保存
	$(function() {
		var saveUserInfoBtn = $("#saveLeader");
		saveUserInfoBtn.click(function() {
			if($("#leaderFrm").validationButton()) {
				//生成json串
				var data = Form2Json.formToJSON(leaderFrm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				var success = false;
				var department = $("#DT1").getSelectedRowJsonObj().ROW_ID;
				success = defaultJson.doUpdateJson(controllername + "?leaderSave&id="+department, data1, DT1);
				if(success == true) {
					$("#leaderModal").modal("hide");
					init();
				} else {
					//	失败
				}
			}
		});
		//分管领导
		$("#fgzrBtn").click(function(){
			openUserTree("single","","huidiao");
		});
	});
	function huidiao(rowsArr){
		var jsrStr = "";
		var jsdwStr = "";
		var jsrName = "";
		for(var i=0;i<rowsArr.length;i++){
			var tempJson = rowsArr[i];
			jsrStr +=tempJson.ACCOUNT+",";
			jsdwStr +=tempJson.DEPTID+",";
			jsrName +=tempJson.USERNAME+",";
		}
		$("#FGZR").attr("code",jsrStr.substring(0,jsrStr.length-1));//为字段的code赋值
		$("#FGZR").val(jsrName.substring(0,jsrName.length-1));
	}
	// 点击删除按钮
	$(function() {
		var btn = $("#deleteBtn");
		btn.click(function() {
		 	if($("#DT1").getSelectedRowIndex()==-1) {
		 		requireSelectedOneRow();
			} else {
				xConfirm("提示信息","是否确认删除！");
				//生成json串
				var data = Form2Json.formToJSON(executeFrm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//通过判断id是否为空来判断是插入还是修改
				$('#ConfirmYesButton').one("click",function(obj) {
					var success = defaultJson.doUpdateJson(controllername + "?executeDept&id="+id+"&operatorSign=3", data1, DT1);
					if(success == true) {
						$("#DT1").setSelect(rowindex);
				//		var dd = {id:1,name:2};//此处为入参
						var index = $("#DT1").getSelectedRowIndex();//获得选中行的索引
						var value = $("#DT1").getSelectedRow();//获得选中行的json对象

				        $("#DT1").removeResult(index);
					}
				});  
				
			} 
		});
	});
	
	// 点击保存按钮
	$(function() {
		var saveUserInfoBtn = $("#saveUserInfo");
		saveUserInfoBtn.click(function() {
			if($("#executeFrm").validationButton()) {
				//生成json串
				var data = Form2Json.formToJSON(executeFrm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//通过判断id是否为空来判断是插入还是修改
				
				var success = false;
				if(id == null || id == "null" || id == "undefined") {
					success = defaultJson.doInsertJson(controllername + "?executeDept&id="+id+"&operatorSign=1", data1, DT1);
				} else {
					success = defaultJson.doUpdateJson(controllername + "?executeDept&id="+id+"&operatorSign=2", data1, DT1);
				}
				
				if(success == true) {
					$("#myModal").modal("hide");
				} else {
					//	失败
				}
			}
		});
	});
	
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">部门管理
			<span class="pull-right">
				<a href="#myModal" role="button" class="btn" data-toggle="modal" id="insertBtn">新增</a>
				<a href="#myModal" role="button" class="btn" data-toggle="modal" id="updateBtn">修改</a>
				<a href="#leaderModal" role="button" class="btn" data-toggle="modal" id="leaderBtn">领导信息</a>
				<a href="#myModal" role="button" class="btn" id="deleteBtn">删除</a>
			</span>
		</h4>
		<form method="post" id="queryForm"  >
		<table class="B-table" width="100%">
		<!--可以再此处加入hidden域作为过滤条件 -->
			<TR  style="display:none;">
				<TD>
					<input type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" >
				</TD>
			</TR>
		<!--可以再此处加入hidden域作为过滤条件 -->
			<tr>
				<th width="5%" class="right-border bottom-border text-right">部门名称</th>
				<td width="15%" class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" name="DEPT_NAME" fieldname="DEPT_NAME" operation="like" logic="and" ></td>
				
				<td width="80%"  class="text-left bottom-border text-right">
					<button	id="queryBtn" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
                    <button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
	            </td>	
			</tr>
			
		</table>
		</form>
	
	<div style="height:5px;"> </div>		
	<div class="overFlowX"> 
		<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
			<thead>
				<tr>
					<th name="XH" id="_XH" width="3%">#</th>
					<th fieldname="ROW_ID" tdalign="center" width="15%">&nbsp;部门编号&nbsp;</th>
					<th fieldname="DEPT_NAME">&nbsp;部门名称&nbsp;</th>
					<th fieldname="BMJC">&nbsp;部门简称&nbsp;</th>
					<th fieldname="EXTEND1">&nbsp;部门性质&nbsp;</th>
					<th fieldname="FZR" >&nbsp;部门负责人&nbsp;</th>
					<th fieldname="FGZR" >&nbsp;分管主任&nbsp;</th>
					<th fieldname="DEPT_PARANT_ROWID" >&nbsp;上级部门&nbsp;</th>
					<th fieldname="SORT" >&nbsp;排序号&nbsp;</th>
				</tr>
			</thead>
		    <tbody></tbody>
		</table>
		            
		</div>
		</div>
	</div>
</div>

<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id="queryXML">
		<input type="hidden" name="txtXML" id="txtXML">
		<input type="hidden" name="txtFilter" order="asc" fieldname="SORT" id="txtFilter">
		<input type="hidden" name="resultXML" id="resultXML">
		<input type="hidden" name="queryResult" id="queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">
	</FORM>
</div>

<!-- Modal -->
<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-header" style="background:#0866c6;">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-remove icon-white"></i></button>
    <h3 id="myModalLabel" style="color:white;">添加部门信息</h3>
  </div>
  <div class="modal-body">
    <form method="post" id="executeFrm"  >
      <table class="B-table" >
         <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			<input type="text" kind="text" fieldname="ROW_ID" name="ROW_ID" id="ROW_ID">
			</TD>
        </TR>
        <tr>
          <th width="30%" class="right-border bottom-border">部门名称</th>
          <td class="right-border bottom-border">
          	<input type="text" placeholder="必填"  check-type="required" maxlength="100" fieldname="DEPT_NAME" name="DEPT_NAME" id="DEPT_NAME"></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">上级部门</th>
          <td class="right-border bottom-border">
          	<select type="text" fieldname="DEPT_PARANT_ROWID" name="DEPT_PARANT_ROWID" id="DEPT_PARANT_ROWID" kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME:ACTIVE_FLAG='1' ORDER BY SORT"></select></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">部门性质</th>
          <td class="right-border bottom-border">
          	<select type="text" fieldname="EXTEND1" name="EXTEND1" id="EXTEND1" kind="dic" src="SFSXMGLGS"></select></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">部门简称</th>
          <td class="right-border bottom-border">
          	<input type="text" fieldname="BMJC" name="BMJC" id="BMJC" maxlength="60"></input></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">排序号</th>
          <td class="right-border bottom-border">
          	<input type="text" fieldname="SORT" name="SORT" id="SORT" maxlength="60"></input></td>
        </tr>
      </table>
    </form>
  </div>
  <div class="modal-footer">
    <button class="btn" id="saveUserInfo">保存</button>
    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
  </div>
</div>



<!-- Modal -->
<div id="leaderModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="leaderModalLabel" aria-hidden="true">
  <div class="modal-header" style="background:#0866c6;">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-remove icon-white"></i></button>
    <h3 id="myModalLabel" style="color:white;">领导信息</h3>
  </div>
  <div class="modal-body">
    <form method="post" id="leaderFrm"  >
      <table class="B-table" >
      	<TR  style="display:none;">
			<input type="text" style="display:none;" kind="text" fieldname="ROW_ID">
        </TR>
        <tr>
          <th width="30%" class="right-border bottom-border">部门负责人</th>
          <td class="right-border bottom-border">
          	<select type="text" placeholder="必填"  check-type="required" maxlength="100" fieldname="FZR" name="FZR" id="FZR" kind="dic" src=""></select></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">分管主任</th>
          <td class="right-border bottom-border">
          	<input class="span12" type="text" id="FGZR" fieldname="FGZR" roattr="9" disabled style="width:70%">
			<button class="btn btn-link"  type="button" id="fgzrBtn" title="选择分管主任人"><i class="icon-edit"></i></button>
          	<!-- <select type="text" fieldname="FGZR" name="FGZR" id="FGZR" kind="dic" src="T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG='1' AND PERSON_KIND='3' AND DEPARTMENT='100000000000' order by to_number(sort)"></select>
          	 -->
          </td>
        </tr>
      </table>
    </form>
  </div>
  <div class="modal-footer">
    <button class="btn" id="saveLeader">保存</button>
    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
  </div>
</div>



</body>
</html>