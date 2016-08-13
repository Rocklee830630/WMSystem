<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>

<title>流程节点配置</title>

<%
 String type = request.getParameter("type");//1为新增，2为修改
 if(type==null){
	 type = "1";
 }
 String processoid = request.getParameter("processoid");
 if(processoid==null){
	 processoid = "";
 }
 String processname = request.getParameter("processname");
 if(processname==null){
	 processname = "";
 }
%>
<script type="text/javascript" charset="UTF-8">
  var type="<%=type%>";
  var processoid="<%=processoid%>";

  var controllername= "${pageContext.request.contextPath }/lcglController.do";
	$(function() {
		
		if(type=="2"){
		  //初始化查询流程
		  var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		  defaultJson.doQueryJsonList(controllername+"?queryProcessStep", data, DT1);
		  getFlowView();
		  windowHeight();
		}else{
		  getFlowView();
		  windowHeight();
		}
		
		//清空按钮响应函数
		$("#btnClear").click(function() {
		     $("#queryForm").clearFormResult();
		});
		//新增按钮响应函数
		$("#insertBtn").click(function() {
			$(window).manhuaDialog({"title":"新增节点","type":"text","content":"${pageContext.request.contextPath}/jsp/business/lcgl/lcpz/stepDetail.jsp?type=add","modal":"2"});
		});
		//修改按钮响应函数
		$("#updateBtn").click(function() {
			if($("#DT1").getSelectedRowIndex()==-1)
			{		
				 xAlert("提示信息",'请至少选择一条记录','3');
				 return
			}
			$(window).manhuaDialog({"title":"修改节点","type":"text","content":"${pageContext.request.contextPath}/jsp/business/lcgl/lcpz/stepDetail.jsp?type=update","modal":"2"});
		});
		//删除按钮响应函数
		$("#deleteBtn").click(function() {
			  var rowindex = $("#DT1").getSelectedRowIndex();
			  $("#DT1").removeResult(rowindex);
			  $('#spFlowView')[0].contentWindow.init(); 
		});
		//上移节点
		$("#moveupBtn").click(function() {
			  var rowindex = $("#DT1").getSelectedRowIndex();
			  if(rowindex==-1)
			  {		
					 xAlert("提示信息",'请至少选择一个节点','3');
					 return
			  }
			  $("#DT1").moveUp(rowindex);
			  getFlowView();
			  
		});
		//下移节点
		$("#movedownBtn").click(function() {
			  var rowindex = $("#DT1").getSelectedRowIndex();
			  if(rowindex==-1)
			  {		
					 xAlert("提示信息",'请至少选择一个节点','3');
					 return
			  }
			  $("#DT1").moveDown(rowindex);
			  getFlowView();
			  
		});
		
		//保存配置按钮响应函数
		$("#saveBtn").click(function(){
			 var t = $("#DT1").getTableRows();
			 if(t<=0)
			 {
				 xAlert("提示信息","请至少添加一个节点！",'3');
				 return;
			 }
		     var rows = $("tbody tr" ,$("#DT1"));
		     var data = "";
		     for(var i=0;i<rows.size();i++){
		    	 var rowJson = rows.eq(i).attr("rowJson");
		    	 if(i<rows.size()-1){
		    		 data +=rowJson+",";
		    	 }else{
		    		 data +=rowJson;
		    	 }
		     }
		     var data1 = defaultJson.packSaveJson(data);
		     var actionName = controllername + "?saveProcess&processoid="+processoid+"&processname=<%=processname%>";
				$.ajax({
					url : actionName,
					data : data1,
					dataType : 'json',
					async :	false,
					type : 'post',
					success : function(result) {
						var subresultmsgobj = defaultJson.dealResultJson(result.msg);
						$(window).manhuaDialog.getParentObj().saveAfter(JSON.stringify(subresultmsgobj));
						$(window).manhuaDialog.close();
					}
				});

		});
		
		
	});
	function tr_click(obj,tabListid) {

	}
	function getFlowView(){
		 document.getElementById("spFlowView").src = "${pageContext.request.contextPath}/jsp/business/lcgl/lcpz/spFlowView.jsp?";
		 
	}
	function insertStep(data,type){
	//	var rowindex = $("#DT1").getSelectedRowIndex();
		if(type=="add")
		{		
			 var rowcount = $("#DT1").getTableRows();
			 if(rowcount==0){
				 rowcount = 1;
			 }else{
				 rowcount = rowcount+1
			 }
			 $("#DT1").insertResult(data,DT1,rowcount);
			 $('#spFlowView')[0].contentWindow.init(); 
		}else{
			var rowindex = $("#DT1").getSelectedRowIndex();
			//var comprisesJson =  $("#DT1").comprisesJson(convertJson.string2json1(data),rowindex);
			$("#DT1").updateResult(data,DT1,rowindex);
			$('#spFlowView')[0].contentWindow.init(); 
		}
	}
	
	

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
		<h4 class="title"><%=processname %>
			<span class="pull-right">
             <button id="insertBtn" class="btn"  type="button">新增节点</button>
             <button id="updateBtn" class="btn"  type="button">修改节点</button>
             <button id="deleteBtn" class="btn"  type="button">删除节点</button>
             <button id="moveupBtn" class="btn"  type="button">上移节点</button>
             <button id="movedownBtn" class="btn"  type="button">下移节点</button>
             <button id="saveBtn" class="btn"  type="button">保存配置信息</button>
			</span>
		</h4>
		<form method="post" id="queryForm" >
		<table class="B-table">
		<!--可以再此处加入hidden域作为过滤条件 -->
			<TR  style="display:none;">
				<TD class="right-border bottom-border"></TD>
				<TD class="right-border bottom-border">
					<input type="text" class="span12" kind="text"  fieldname="processtypeoid"  value="<%=processoid%>" operation="=" >
				</TD>
			</TR>
		</table>
		</form>
 	<div style="height:5px;"> </div>		
      <div class= "overFlowX">
		<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" noPage="true" pageNum="100">
			<thead>
				<tr>
					<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
					<th fieldname="NAME" tdalign="left" >&nbsp;节点名称&nbsp;</th>
					<th fieldname="ISMS" tdalign="left" >&nbsp;报送方式&nbsp;</th>
					<th fieldname="SHEDULE_DAYS" tdalign="center">&nbsp;规定天数&nbsp;</th>
					<th fieldname="DEPTID" tdalign="left">&nbsp;节点单位&nbsp;</th>   
					<th fieldname="ROLENAME" tdalign="left">&nbsp;节点角色&nbsp;</th>   
					<th fieldname="ACTOR" tdalign="left">&nbsp;办理人&nbsp;</th>   
					<th fieldname="CCACTOR" tdalign="left">&nbsp;抄送人&nbsp;</th>
				</tr>
			</thead>
		    <tbody></tbody>
		</table>
		 <br>           
		</div>
		
		<div class="windowOpenAuto" width="500px">
     				  <iframe id="spFlowView" style="width:100%;height:100%;frameborder=1;scrolling=auto">
     				  </iframe>
        </div>
		</div>
		</div>
	</div>
</div>

<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id = "queryXML">
		<input type="hidden" name="txtXML" id = "txtXML">
		<input type="hidden" name="txtFilter"  order="asc" fieldname = "STEPSEQUENCE"	id = "txtFilter">
		<input type="hidden" name="resultXML" id = "resultXML">
		<input type="hidden" name="queryResult" id = "queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">
	</FORM>
</div>

</body>
</html>