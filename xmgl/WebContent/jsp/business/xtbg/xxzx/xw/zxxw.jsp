<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.QuerySet" %>
<%@ page import="com.ccthanking.framework.common.DBUtil" %>
<%
	String sql = "SELECT FBR FROM XTBG_XXZX_ZXXW T GROUP BY FBR";
	QuerySet qs = DBUtil.executeQuery(sql, null);
%>
<app:base/>

<title>中心新闻-中心新闻查询</title>

<script type="text/javascript" charset="UTF-8">
  	var id,json,rowindex,rowValue,flag,exeJson;
  	var controllername= "${pageContext.request.contextPath }/zxxwController.do";

  	//计算本页表格分页数
  	function setPageHeight(){
  		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
  		var pageNum = parseInt(height/pageTableOne,10);
  		$("#DT1").attr("pageNum",pageNum);
  	}
  	
  	function init() {
        //生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryXw", data, DT1);
	}
  	
	//页面默认参数
	$(document).ready(function() { 
		setPageHeight();
        //生成json串
        g_bAlertWhenNoResult = false;
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryXw", data, DT1);
		g_bAlertWhenNoResult = true;
		
	}); 
	
	$(function() {
		var btn = $("#queryBtn");
		btn.click(function() {
	        //生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryXw", data, DT1);
		});
	});
	
	// 清除表单
	$(function() {
		$("#query_clear").click(function() {
	       $("#queryForm").clearFormResult();
		});
	});
	
	function tr_click(obj, tabListid) {
		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引
		rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
		id = obj.NEWSID;
		exeJson = JSON.stringify(obj);
	//	alert(rowindex);
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
			//	var data = Form2Json.formToJSON(frmPost);
				//组成保存json串格式
				//通过判断id是否为空来判断是插入还是修改
				$('#ConfirmYesButton').unbind();
				$('#ConfirmYesButton').one("click",function(obj) {
					var data1 = defaultJson.packSaveJson(exeJson);
					var success = defaultJson.doDeleteJson(controllername + "?executeXw&operatorSign=3", data1, DT1);
				});
			} 
		});
	});

	// 点击查看公告
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
				
	//			xAlert("提示信息",data);
	//			xAlert("提示信息",id);
	//			alert(data + "|\n" + rowValue);
//				xAlert("提示信息",id);
				defaultJson.doUpdateJson(controllername + "?readGgtz&id="+id, data1, DT1);
				
			}
		});
	});
	
	//按钮绑定事件(新增新闻)
	$(function() {
		$("#insertBtn").click(function() {
			flag = true;
			$(window).manhuaDialog({"title":"中心新闻>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/xxzx/xw/add.jsp","modal":"1"});
		});
	});

	//按钮绑定事件(修改公告)
	$(function() {
		$("#updateBtn").click(function() {
			if($("#DT1").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条要操作的数据！','3');
			} else {
				flag = false;
					var obj = $("#DT1").getSelectedRowJsonObj();//获得选中行的索引
					id = obj.NEWSID;
					exeJson = JSON.stringify(obj);
					json = JSON.stringify(obj);
					json=encodeURI(json); 
				
				$(window).manhuaDialog({"title":"中心新闻>修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/xxzx/xw/add.jsp?xx="+json+"&&id="+id,"modal":"1"});
			}
		});
	});
	
	//回调函数
	getWinData = function(data){
	//	data = JSON.stringify(defaultJson.dealResultJson(data));
		index =	$("#DT1").getSelectedRowIndex();
		init();
		if(flag==false)
		{
			//修改	
			$("#DT1").setSelect(index);
			exeJson = $("#DT1").getSelectedRowJsonByIndex(index);
			successInfo();
		}
		else
		{
			//新增
			$("#DT1").setSelect(0);
			exeJson = $("#DT1").getSelectedRowJsonByIndex(0);
			successInfo();
		    index = 0;
		}
	};
	
	
	function detail(index) {
		var obj = $("#DT1").getRowJsonObjByIndex(index);
		var newId = obj.NEWSID;
		$(window).manhuaDialog({"title":"中心新闻","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/portal/newsDetail.jsp?type=2&id="+newId, "modal":"1"});
	
	}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">中心新闻
				<span class="pull-right">
	  				<button id="insertBtn" class="btn"  type="button" >新增</button>
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
				<th width="5%" class="right-border bottom-border">标题</th>
				<td class="right-border bottom-border">
					<input class="span12" type="text" id="XWBT" name="XWBT" fieldname="XWBT" operation="like" logic="and" ></td>
				
				<th width="5%" class="right-border bottom-border">发布人</th>
				<td width="12%" colspan="2" class="bottom-border">
					<select class="span12 person" style="width:100%" id="FBR" name="FBR" fieldname="FBR" operation="=" logic="and">
							<option value="">全部</option>
					<%
						for(int i = 0; i < qs.getRowCount(); i++) {
							%>
  	          				<option value="<%=qs.getString(i+1, "FBR") %>"><%=qs.getString(i+1, "FBR") %></option>
  	          				<%		
						}
					%>
					</select>
				</td>
					
				<th width="8%" class="right-border bottom-border">发布时间</th>
				<td width="8%" class="right-border bottom-border">
					<input class="span12 date" type="date" name="FBSJ" fieldname="FBSJ" id="FBSJ" operation=">=" logic="and" fieldtype="date" fieldformat="YYYY-MM-DD"/></td>
				<th width="2%" class="right-border bottom-border">至</th>
				<td width="8%" class="right-border bottom-border">
					<input class="span12 date" type="date" name="FBSJ" fieldname="FBSJ" id="FBSJ" operation="<=" logic="and" fieldtype="date" fieldformat="YYYY-MM-DD"/></td>
			
				<td class="text-left bottom-border text-right" >
					<button	id="queryBtn" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
                    <button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
	            </td>	
			</tr>
			
			<tr>
				</tr>
			<tr>
			</tr>
		</table>
		</form>
	
	<div style="height:5px;"> </div>		
	<div class="overFlowX"> 
		<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
			<thead>
				<tr>
					<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
					<th fieldname="XWBT" haslink="true" linkfunction="detail" maxlength="50">&nbsp;标题&nbsp;</th>
					<th fieldname="FBSJ"  tdalign="center">&nbsp;发布时间&nbsp;</th>
					<th fieldname="FBR" >&nbsp;发布人&nbsp;</th>
				<!-- 	<th fieldname="HFCS" >&nbsp;回复次数&nbsp;</th> -->
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