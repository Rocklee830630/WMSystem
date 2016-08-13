<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%
	String byPerson = request.getParameter("byPerson");
	String queryMethod = "queryFlowApply";
	if(null != byPerson && "1".equals(byPerson)){
		queryMethod = "queryFlowApplyByPerson";
	}
%>
<app:base/>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>

<title>历史流程查询</title>

<script type="text/javascript" charset="UTF-8">
 var id,json,rowindex,rowValue;
  var controllername= "${pageContext.request.contextPath }/lcglController.do";
  
	//计算本页表格分页数
	function setPageHeight(){
		var getHeight=getDivStyleHeight();
		var height = getHeight-pageTopHeight-pageTitle-(pageQuery*2)-getTableTh(1)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	};
  
	$(function() {
		setPageHeight();
		g_bAlertWhenNoResult = false;
		doQuery();
		g_bAlertWhenNoResult = true;
		var btn = $("#queryBtn");
		btn.click(function() {
			 doQuery();
		});
	    var btn_clearQuery = $("#query_clear");
	    btn_clearQuery.click(function() 
	      {
	        $("#queryForm").clearFormResult();
	      });
	});
	function tr_click(obj,tabListid) {

	}
	function linkTest(index)
	{

		var obj = $("#DT1").getRowJsonObjByIndex(index);

	    var wsActionURL = "/xmgl/PubWS.do?getPreviewOldXMLAction|isEdit=0|flaid="+obj.FLAID+"|flaflwid="+obj.FLAFLWID+"|flwno="+obj.FLWNO+"|rowid="+Math.random()+"html";
	    wsActionURL = "/xmgl/jsp/framework/print/pubOldPrint.jsp?param="+wsActionURL+"&flaid="+obj.FLAID+"&flaflwid="+obj.FLAFLWID+"&flwno="+obj.FLWNO;
	    window.open(wsActionURL);
		//alert(index);
	}
	
	function doQuery(){
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		defaultJson.doQueryJsonList(controllername+"?<%=queryMethod%>", data, DT1);
	}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
		<h4 class="title">流程查询
			<span class="pull-right">

			</span>
		</h4>
		<form method="post" id="queryForm" >
		<table class="B-table" width="100%">
			<tr>
				<th width="4%" class="right-border bottom-border">流程</th>
				<td width="15%" class="right-border bottom-border">
					<select class="span12 12characters" placeholder="" name="flaflwid" fieldname="FLAFLWID" defaultMemo="全部" kind="dic" src="T#TAC_FLOW:FLWID:FLWNAME:" operation="="  logic="and">
					</select>
				</td>
				<!-- 
				<th width="3%" class="right-border bottom-border">申请人</th>
				<td width="7%" class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" name="flaaempname" fieldname="FLAAEMPNAME" operation="like" logic="and" >
				</td>
				<th width="8%" class="right-border bottom-border">申请单号</th>
				<td width="17%" class="right-border bottom-border">
					<input class="span12" name="FLANO" fieldname="FLANO"  operation="=" logic="and" ></td>
				 -->
				<th width="4%" class="right-border bottom-border">标题</th>
				<td class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" name="flatitle" fieldname="FLATITLE" operation="like" logic="and" ></td>
						
				<th width="4%" class="right-border bottom-border">日期</th>
				<td width="8%" class="right-border bottom-border">
					<input class="span12 date" type="date"  name="GFLAADATE" fieldname="FLAADATE"  operation=">="  logic="and" fieldtype="date" fieldformat="YYYY-MM-DD">
					
				</td>
				<th width="2%" class="right-border bottom-border">至</th>
				<td width="8%"  class="right-border bottom-border">
					<input class="span12" type="date" name="LFLAADATE" fieldname="FLAADATE"  operation="<=" value="" logic="and" fieldtype="date" fieldformat="YYYY-MM-DD">
				</td>
				<td class="text-left bottom-border text-right" rowspan="2">
					<button	id="queryBtn" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
					<button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
				</td>
			</tr>
			<tr>
				<th class="right-border bottom-border">状态</th>
				<td  class="right-border bottom-border">
					<select class="span12 3characters" placeholder=""  name="FLASTATUS" fieldname="FLASTATUS" operation="="  logic="and">
					<option value>全部</option>
					<option value="办理中">办理中</option>
					<option value="完成">完成</option>
					<option value="中断">中断</option>
					</select>
				</td>
				<th  class="right-border bottom-border">申请人</th>
				<td  class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" name="flaaempname" fieldname="FLAAEMPNAME" operation="like" logic="and" >
				</td>
				<td colspan="4"></td>
			</tr>
		</table>
		</form>
 	<div style="height:5px;"> </div>		
      <div class= "overFlowX">
		<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
			<thead>
				<tr>
					<th name="XH" id="_XH" style="width:10px;">&nbsp;#&nbsp;</th>
					<th fieldname="FLANO"  haslink="true" linkfunction="linkTest">&nbsp;申请单号&nbsp;</th>
					<th fieldname="FLASTATUS">&nbsp;状态&nbsp;</th>   
					<th maxlength="12" fieldname="FLATITLE" >&nbsp;标题&nbsp;</th>
					<th maxlength="10" fieldname="FLWNAME" >&nbsp;流程名称&nbsp;</th>
					<th fieldname="FLAAEMPNAME">&nbsp;申请人&nbsp;</th>
					<th maxlength="10" fieldname="DPTNAME" >&nbsp;申请部门&nbsp;</th>
					<th fieldname="FLAADATE" tdalign="center">&nbsp;申请时间 &nbsp;</th>
					<th fieldname="FLAFDATE" tdalign="center">&nbsp;完成时间 &nbsp;</th>
					<th maxlength="5" fieldname="FLACEMPNAME" >&nbsp;当前办理人 &nbsp;</th>
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
		<input type="hidden" name="txtFilter"  order="desc" fieldname = "FLAADATE"	id = "txtFilter">
		<input type="hidden" name="resultXML" id = "resultXML">
		<input type="hidden" name="queryResult" id = "queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">
	</FORM>
</div>

</body>
</html>