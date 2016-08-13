<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>公告通知-内部短信查询</title>

<script type="text/javascript" charset="UTF-8">
	var id,json,rowindex,rowValue,flag,exeJson;
  	var controllername= "${pageContext.request.contextPath }/nbdxController.do";
	
	function init() {
        //生成json串
		var data = combineQuery.getQueryCombineData(queryForm, frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryNbdx", data, DT1);
	}
	
	$(function() {
		init();
		var btn = $("#queryBtn");
		btn.click(function() {
	        //生成json串
			var data = combineQuery.getQueryCombineData(queryForm, frmPost, DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryNbdx", data, DT1);
		});
	});
	
	function tr_click(obj, tabListid) {
		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引
		rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
		id = obj.OPID;
		exeJson = JSON.stringify(obj);
	//	alert(rowValue);
		json = JSON.stringify(obj);
		json=encodeURI(json); 
	}
	
	// 点击删除按钮
	$(function() {
		var btn = $("#deleteBtn");
		btn.click(function() {
		 	if($("#DT1").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条要操作的数据！','3');
			} else {
				xConfirm("提示信息","是否确认删除！");
				//生成json串
			//	var data = Form2Json.formToJSON(executeFrm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(exeJson);
				//通过判断id是否为空来判断是插入还是修改
				$('#ConfirmYesButton').one("click",function(obj) {
					var success = defaultJson.doUpdateJson(controllername + "?executeNbdx&operatorSign=3", data1, DT1);
					
					if(success == true) {
						$("#DT1").setSelect(rowindex);
				//		var dd = {id:1,name:2};//此处为入参
						var index = $("#DT1").getSelectedRowIndex();//获得选中行的索引

				        $("#DT1").removeResult(index);
					}
				});  
				
			} 
		});
	});
	
	
	//按钮绑定事件(新增内部短信)
	$(function() {
		$("#insertBtn").click(function() {
			flag = true;
			$(window).manhuaDialog({"title":"信息中心>新增内部短信","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/msg/addNbdx.jsp","modal":"1"});
		});
	});

	//按钮绑定事件(修改内部短信)
	$(function() {
		$("#updateBtn").click(function() {
			if($("#DT1").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条要操作的数据！','3');
			} else {
				flag = false;
				$(window).manhuaDialog({"title":"信息中心>修改内部短信","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/msg/addNbdx.jsp?xx="+json+"&&id="+id,"modal":"1"});
			}
		});
	});
	
	$(function() {
		$("#query_clear").click(function() {
			$("#queryForm").clearFormResult();
		});
	});
	
	//回调函数
	getWinData = function(data){
		index =	$("#DT1").getSelectedRowIndex();
		init();
		if(flag==false)
		{
			//修改	
			$("#DT1").setSelect(index);
		    xSuccessMsg("修改消息成功","");
		}
		else
		{
			//新增
			$("#DT1").setSelect(0);
		    xSuccessMsg("添加消息成功","");
		    index = 0;
		}
	};
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
		<h4 class="title">内部短信查询
		 <span class="pull-right">
  				<button id="insertBtn" class="btn"  type="button" >添加</button>
  				<button id="updateBtn" class="btn"  type="button" >修改</button>
  				<button id="deleteBtn" class="btn"  type="button" >删除</button>
		 </span>
		</h4>
		<form method="post" id="queryForm"  >
		<table class="B-table">
		<!--可以再此处加入hidden域作为过滤条件 -->
			<TR  style="display:none;">
				<TD class="right-border bottom-border"></TD>
				<TD class="right-border bottom-border">
					<input type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" >
				</TD>
			</TR>
		<!--可以再此处加入hidden域作为过滤条件 -->
			<tr>
				<th width="8%" class="right-border bottom-border">发信人姓名</th>
				<td width="13%" class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" id="USERFROMNAME" name="USERFROMNAME" fieldname="USERFROMNAME" operation="like" logic="and" ></td>
				<th width="8%" class="right-border bottom-border">接信人姓名</th>
				<td width="13%" class="right-border bottom-border">
					<input class="span12" placeholder="" name="USERTONAME" id="USERTONAME" fieldname="USERTONAME" operation="like" logic="and" ></td>
					
		        <th width="5%" class="right-border bottom-border">标题</th>
				<td width="13%" class="right-border bottom-border">
					<input class="span12" name="TITLE" fieldname="TITLE" id="TITLE" operation="like" logic="and" /></td>
					
		        <td width="13%"  class="text-left bottom-border text-right" rowspan="2">
					<button	id="queryBtn" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
                    <button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
	            </td>	
			</tr>
			<tr>
				<th width="8%" class="right-border bottom-border">业务类型</th>
				<td width="13%" class="bottom-border">
					<input class="span12" placeholder="" id="YWLX" name="YWLX" fieldname="YWLX" operation="like"  logic="and" /></td>
					
				<th width="8%" class="right-border bottom-border">发送时间</th>
				<td width="13%" class="right-border bottom-border">
					<input class="span12" type="date" name="OPTIME" fieldname="OPTIME" id="OPTIME" operation=">=" logic="and" fieldtype="date" fieldformat="YYYY-MM-DD"/></td>
				<th width="5%" class="right-border bottom-border">至</th>
				<td width="13%" class="right-border bottom-border">
					<input class="span12" type="date" name="OPTIME" fieldname="OPTIME" id="OPTIME" operation="<=" logic="and" fieldtype="date" fieldformat="YYYY-MM-DD"/></td>
					
			</tr>
		</table>
		</form>
		</div>
	</div>
	
	<div class="row-fluid">
		<div class="B-small-from-table-auto">
		<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
			<thead>
				<tr>
					<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
					<th fieldname="USERFROMNAME" >&nbsp;发信人姓名&nbsp;</th>
					<th fieldname="USERTONAME" >&nbsp;接信人姓名&nbsp;</th>
					<th fieldname="TITLE" >&nbsp;标题&nbsp;</th>
					<th fieldname="OPTIME" >&nbsp;发送时间&nbsp;</th>
					<th fieldname="YWLX" >&nbsp;业务类型&nbsp;</th>
				</tr>
			</thead>
		    <tbody></tbody>
		</table>
		            
		</div>
	</div>
</div>

<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id="queryXML">
		<input type="hidden" name="txtXML" id="txtXML">
		<input type="hidden" name="txtFilter" order="desc" fieldname="OPTIME" id="txtFilter">
		<input type="hidden" name="resultXML" id="resultXML">
		<input type="hidden" name="queryResult" id="queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">
	</FORM>
</div>

</body>
</html>