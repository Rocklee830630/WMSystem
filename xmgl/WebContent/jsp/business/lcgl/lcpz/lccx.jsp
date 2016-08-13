<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>

<title>流程配置查询</title>

<script type="text/javascript" charset="UTF-8">
  var controllername= "${pageContext.request.contextPath }/lcglController.do";
  
	//计算本页表格分页数
	function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
  
	$(function() {
		setPageHeight();
		//初始化查询流程
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		defaultJson.doQueryJsonList(controllername+"?queryProcessType", data, DT1);
		//查询流程按钮响应函数
		var btn = $("#queryBtn");
		btn.click(function() {
	        //生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryProcessType", data, DT1);
		});
		
		//清空按钮响应函数
		$("#btnClear").click(function() {
		     $("#queryForm").clearFormResult();
		});
		//新增按钮响应函数
		$("#insertBtn").click(function() {
			$("#myModal").modal("show");
		});
		
		$("#saveName").click(function() {
			var processname = $("#processname").val();
			if(processname==""){
				xAlert("提示信息",'请输入流程名称!','3');
				return
			}
			
			$("#myModal").modal("hide");
			$(window).manhuaDialog({"title":"新增流程","type":"text","content":"${pageContext.request.contextPath}/jsp/business/lcgl/lcpz/lcpz.jsp?type=1&processname="+processname,"modal":"1"});
		});
		
		//修改按钮响应函数
		$("#updateBtn").click(function() {
			if($("#DT1").getSelectedRowIndex()==-1)
			{
				xAlert("提示信息",'请选择一条记录!','3');
				return
			}
			var processoid = $("#DT1").getSelectedRowJsonObj().PROCESSTYPEOID;
			var processname = $("#DT1").getSelectedRowJsonObj().NAME;
			$(window).manhuaDialog({"title":"修改流程","type":"text","content":"${pageContext.request.contextPath}/jsp/business/lcgl/lcpz/lcpz.jsp?type=2&processoid="+processoid+"&processname="+processname,"modal":"1"});
		});
		//删除按钮响应函数
		$("#deleteBtn").click(function() {
			if($("#DT1").getSelectedRowIndex()==-1)
			 {
				xAlert("提示信息",'请选择一条记录','3');
				return
			 }
			xConfirm("提示信息","是否确认删除！");
			
			$('#ConfirmYesButton').attr('click','');//.click( eval(function(){test(dd);}));
			$('#ConfirmYesButton').one("click",function(){  
				doDeleteRow();
			});  
		});
		
		//配置流程属性按钮响应函数
		$("#processBtn").click(function() {
			if($("#DT1").getSelectedRowIndex()==-1)
			{
				xAlert("提示信息",'请选择一条记录!','3');
				return
			}
			var processoid = $("#DT1").getSelectedRowJsonObj().PROCESSTYPEOID;
			$(window).manhuaDialog({"title":"流程文书关联设置","type":"text","content":"${pageContext.request.contextPath}/jsp/business/lcgl/lcpz/processDetail.jsp?processoid="+processoid,"modal":"1"});
		});
		
	});
    function doDeleteRow (){
        var data = $("#DT1").getSelectedRows();
        var data1 = defaultJson.packSaveJson(data);
        defaultJson.doDeleteJson(controllername+"?deleteProcess",data1,DT1); 
    }
	function tr_click(obj,tabListid) {

	}
	function saveAfter(obj){
		var rowindex = $("#DT1").getSelectedRowIndex();
		if(rowindex==-1)
		{
			$("#DT1").insertResult(obj,DT1,1);
		}else{
			$("#DT1").updateResult(obj,DT1,rowindex);
		}
		xAlert("提示信息",'流程配置操作成功！','1');
	}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-header" style="background:#0866c6;">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-remove icon-white"></i></button>
				<h3 id="myModalLabel" style="color:white;">
					请输入流程名称
				</h3>
			</div>
			<div class="modal-body">
				<table class="B-table">
					<tr>
						<th width="30%" class="right-border bottom-border">
							流程名称
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
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
		<h4 class="title">流程查询
			<span class="pull-right">
             <button id="insertBtn" class="btn"  type="button">新增流程</button>
             <button id="updateBtn" class="btn"  type="button">修改流程</button>
             <button id="processBtn" class="btn"  type="button">流程文书关联设置</button>
             <button id="deleteBtn" class="btn" style="display:none" type="button">删除流程</button>
			</span>
		</h4>
		<form method="post" id="queryForm" >
		<table class="B-table">
		<!--可以再此处加入hidden域作为过滤条件 -->
			<TR  style="display:none;">
				<TD class="right-border bottom-border"></TD>
				<TD class="right-border bottom-border">
					<input type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" >
					<input type="text" class="span12" kind="text"  fieldname="PROCESSTYPE"  value="1" operation="=" >
				</TD>
			</TR>
		<!--可以再此处加入hidden域作为过滤条件 -->
			<tr>
				<th width="8%" class="right-border bottom-border">流程名称</th>
				<td width="10%" class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" name="NAME" fieldname="NAME" operation="like" logic="and" ></td>
						
				<th width="8%" class="right-border bottom-border">创建日期</th>
				<td width="17%" class="right-border bottom-border">
					<input class="span12" type="date"  name="CREATETIME" fieldname="CREATETIME"  operation=">="  logic="and" fieldtype="date" fieldformat="YYYY-MM-DD">
					
				</td>
				<th width="8%" class="right-border bottom-border">到</th>
				<td width="17%"  class="right-border bottom-border">
					<input class="span12" type="date" name="CREATETIME_" fieldname="CREATETIME"  operation="<=" value="" logic="and" fieldtype="date" fieldformat="YYYY-MM-DD">
					
				</td>
                 <td width="15%" rowspan="2"  class="text-center bottom-border text-right">
					<button id="queryBtn" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
					<button id="btnClear" class="btn btn-link"  type="button"><i class="icon-trash"></i>清空</button>
				</td>
             </tr>
		</table>
		</form>
 	<div style="height:5px;"> </div>		
      <div class= "overFlowX">
		<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
			<thead>
				<tr>
					<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
					  
					<th fieldname="NAME" tdalign="left" >&nbsp;流程名称&nbsp;</th>
					<th fieldname="ACTOR" tdalign="center">&nbsp;创建人&nbsp;</th>   
					<th fieldname="CREATETIME" >&nbsp;创建时间&nbsp;</th>
					<th fieldname="MEMO" >&nbsp;备注&nbsp;</th>
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
		<input type="hidden" name="queryXML" id = "queryXML">
		<input type="hidden" name="txtXML" id = "txtXML">
		<input type="hidden" name="txtFilter"  order="desc" fieldname = "CREATETIME"	id = "txtFilter">
		<input type="hidden" name="resultXML" id = "resultXML">
		<input type="hidden" name="queryResult" id = "queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">
	</FORM>
</div>

</body>
</html>