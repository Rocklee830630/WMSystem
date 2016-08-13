<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>

<script type="text/javascript" charset="UTF-8">

  	var controllername= "${pageContext.request.contextPath }/swglController.do";
  	
  	//计算本页表格分页数
  	function setPageHeight(){
  		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
  		var pageNum = parseInt(height/pageTableOne,10);
  		$("#DT1").attr("pageNum",pageNum);
  	}
  	
  	function init() {
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		defaultJson.doQueryJsonList(controllername+"?queryLs", data, DT1);
	}
  	
	//页面默认参数
	$(document).ready(function() { 
		setPageHeight();
        //生成json串
        g_bAlertWhenNoResult = false;
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryLs", data, DT1);
        g_bAlertWhenNoResult = true;
	}); 
	
	$(function() {
		var btn = $("#queryBtn");
		btn.click(function() {
			var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
			defaultJson.doQueryJsonList(controllername+"?queryLs", data, DT1);
		});
	});
	
	// 清除表单
	$(function() {
		$("#query_clear").click(function() {
	       $("#queryForm").clearFormResult();
		});
	});
	

	function rowView_(index){
		
		var obj = $("#DT1").getRowJsonObjByIndex(index);
	    var wsActionURL = "/xmgl/PubWS.do?getPreviewOldXMLAction|isEdit=0|flaid="+obj.FINFLAID+"|flaflwid="+obj.FLOWID+"|flwno=0704|rowid="+Math.random()+"html";
	    wsActionURL = "/xmgl/jsp/framework/print/pubOldPrint.jsp?param="+wsActionURL+"&flaid="+obj.FINFLAID+"&flaflwid="+obj.FLOWID+"&flwno=0704";
	   // window.open(wsActionURL);
		  $(window).manhuaDialog({"title":"历史收文详细信息","type":"text","content":wsActionURL,"modal":"1"});  
	}

	
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">历史收文查询
			</h4>
			
		<form method="post" id="queryForm"  >
		<table class="B-table">
			
			<tr>
				<th width="5%" class="right-border bottom-border">收文编号</th>
				<td width="10%" class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" id="FINNO" name="FINNO" fieldname="FINNO" operation="like" logic="and" ></td>
				<th width="5%" class="right-border bottom-border">文件标题</th>
				<td width="10%" class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" name="FINTITLE" id="FINTITLE" fieldname="FINTITLE" operation="like" logic="and"></td>
				<th width="2%" class="right-border bottom-border">文种</th>
				<td width="7%" class="bottom-border">
					<input class="span12" type="text" placeholder="" id="FINFILETYPE" name="FINFILETYPE" fieldname="FINFILETYPE" operation="like"  logic="and" /></td>
			
				<th width="2%" class="right-border bottom-border">缓急</th>
				<td width="6%" class="right-border bottom-border">
					<input class="span12" type="text" name="FINPRESS" fieldname="FINPRESS" id="FINPRESS" operation="like" logic="and"></input></td>
				<td width="13%"  class="text-left bottom-border text-right" >
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
					<th fieldname="FINSTATUS">&nbsp;当前状态&nbsp;</th>
					<th fieldname="FINFILETYPE" >&nbsp;文种&nbsp;</th>
					<th fieldname="FINNO">&nbsp;收文编号&nbsp;</th>
					<th fieldname="FINTITLE" hasLink="true" linkFunction="rowView_" maxlength="17">&nbsp;文件标题&nbsp;</th>
					<th fieldname="FINFUTNAME">&nbsp;来文单位&nbsp;</th>
					<th fieldname="FINPRESS" tdalign="center">&nbsp;缓急&nbsp;</th>
					<th fieldname="FINFILENO" >&nbsp;文号&nbsp;</th>
					<th fieldname="FINRECDATE" tdalign="center">&nbsp;收文日期&nbsp;</th>
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
		<input type="hidden" name="txtFilter" order="desc" fieldname="FINRECDATE" id="txtFilter">
		<input type="hidden" name="resultXML" id="resultXML">
		<input type="hidden" name="queryResult" id="queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">
	</FORM>
</div>

</body>
</html>