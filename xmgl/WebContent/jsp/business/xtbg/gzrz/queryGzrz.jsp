<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>公告通知-查询</title>

<script type="text/javascript" charset="UTF-8">
 	var id,json,rowindex,rowValue,flag,exeJson;
  	var controllername= "${pageContext.request.contextPath }/gzrzController.do";
  	
	//页面默认参数
	$(document).ready(function() { 
        //生成json串
        g_bAlertWhenNoResult = false;
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryWorkGzrz", data, DT1);
        g_bAlertWhenNoResult = true;
	}); 
	
	$(function() {
		var btn = $("#queryBtn");
		btn.click(function() {
	        //生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryWorkGzrz", data, DT1);
		});
	});
	
	// 清除按钮
  	$(function() {
		$("#query_clear").click(function() {
			$("#queryForm").clearFormResult();
		});
	});
	
	function tr_click(obj, tabListid) {
		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引
		rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
		id = obj.DIARYID;
		exeJson = JSON.stringify(obj);
	//	alert(rowindex);
		json = JSON.stringify(obj);
		json=encodeURI(json); 
	}
	

	// 点击查看工作日誌
	$(function() {
		var btn = $("#readGg");
		btn.click(function() {
		 	if($("#DT1").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条要操作的数据！','3');
			} else {
				//生成json串
				var data = Form2Json.formToJSON(queryForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(rowValue);
				//通过判断id是否为空来判断是插入还是修改
				defaultJson.doUpdateJson(controllername + "?readGgtz&id="+id, data1, DT1);
			}
		});
	});
	

	function detail(index) {
		var obj = $("#DT1").getRowJsonObjByIndex(index);
		var id = obj.DIARYID;
		$(window).manhuaDialog({"title":"工作日志详情","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/gzrz/rzDetail.jsp?id="+id, "modal":"1"});
	
	}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
		<h4 class="title">工作日志查询</h4>
		<form method="post" id="queryForm"  >
		<table class="B-table" width="100%">
		<!--可以再此处加入hidden域作为过滤条件 -->
			<TR  style="display:none;">
				<TD class="right-border bottom-border"></TD>
				<TD class="right-border bottom-border">
					<input type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" >
				</TD>
			</TR>
		<!--可以再此处加入hidden域作为过滤条件 -->
			<tr>
				<th width="8%" class="right-border bottom-border">发布日期</th>
				<td width="15%" class="right-border bottom-border">
					<input class="span12" type="date" placeholder="" id="FBSJ" name="FBSJ" fieldname="FBSJ" operation=">=" logic="and" fieldtype="date" fieldformat="YYYY-MM-DD"></td>
				<th width="3%" class="right-border bottom-border">至</th>
				<td width="15%" class="right-border bottom-border">
					<input class="span12" type="date" placeholder="" name="FBSJ" id="FBSJ" fieldname="FBSJ" operation="<=" logic="and" fieldtype="date" fieldformat="YYYY-MM-DD"></td>
					
				<th width="5%" class="right-border bottom-border">记录人</th>
				<td width="15%" class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" id="JLR" name="JLR" fieldname="JLR" operation="like" logic="and" ></td>
		        
		        <td  class="text-left bottom-border text-right">
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
					<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
					<th style="width:10%" tdalign="center" fieldname="FBSJ" >&nbsp;发布日期&nbsp;</th>
					<th style="width:10%" tdalign="center" fieldname="RZLB" haslink="true" linkfunction="detail">&nbsp;日志类别&nbsp;</th>
					<th style="width:10%" fieldname="JLR" >&nbsp;记录人&nbsp;</th>
					<th fieldname="NR" maxlength="50">&nbsp;内容&nbsp;</th>
					<th style="width:7%" tdalign="center" fieldname="DIARYID" >&nbsp;附件&nbsp;</th>
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
		<input type="hidden" name="txtFilter" order="desc" fieldname="FBSJ" id="txtFilter">
		<input type="hidden" name="resultXML" id="resultXML">
		<input type="hidden" name="queryResult" id="queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">
	</FORM>
</div>

</body>
</html>