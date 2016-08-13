<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/ReportController.do";
  
  function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#cjdwList").attr("pageNum",pageNum);
	}
  
  
	$(function() {
		setPageHeight();
		ready();
		//document.getElementById("query").onclick=true;
		//$("#query").click();
		//查询
		var btn = $("#query");
		btn.click(function() {
			var start=$("#START").val();
			var end=$("#END").val();
			//生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryReport",data,DT1,null,false);
				});
		//清空
		var clean=$("#clean");
		clean.click(function(){
			$("#START").val("");
			$("#END").val("");
			$("#queryForm").clearFormResult();
		});
		//按钮绑定事件（导出）
		$("#excel").click(function(){
			if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
				printTabList("DT1","报表信息.xls","BMMC,YWSL,YWMX,YWLJ,YWLJMX,FWSL,FWLJ,TZSL,TZLJ","3,1");
			  }
		});
		//详细页面链接
		$("#detailed").click(function(){
			$(window).manhuaDialog({"title":"监控详情","type":"text","content":"${pageContext.request.contextPath}/jsp/xmgl/xmcbk/monitor.jsp","modal":"1"});
		});
		//详细页面链接
		$("#bm_detailed").click(function(){
			$(window).manhuaDialog({"title":"监控详情","type":"text","content":"${pageContext.request.contextPath}/jsp/xmgl/xmcbk/bm_monitor.jsp","modal":"1"});
		});
	});
	
    function ready() {
   		var today=getCurrentDate();
   		$("#START").attr("max",today);
   		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryReport",data,DT1,null,false);
   };
   //开始时间
   function start(){
	   var start=$("#START").val();
	   $("#END").attr("min",start);
   }
   //结束时间
   function end(){
	   var end=$("#END").val();
	   var today=getCurrentDate();
	   if(end<=today)
	   $("#START").attr("max",end);
   }
</script>
	</head>
	<body>
<app:dialogs />
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">
				征收综合信息
				<span class="pull-right">
			  		<button id="excel" class="btn" type="button">导出</button>
			  		<button id="detailed" class="btn" type="button">录入明细</button>
			  		<button id="bm_detailed" class="btn" type="button">部门录入明细</button>
		  		</span>
			</h4>
			<form method="post" id="queryForm" width="100%">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
				<TR style="display: none;">
					<TD class="right-border bottom-border">
						<INPUT type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<=" keep="true">
					</TD>
				</TR>
				<!--可以再此处加入hidden域作为过滤条件 -->
				<tr style="display:none">

					<!--公共的查询过滤条件 -->
					<%-- <jsp:include page="/jsp/business/common/commonQuery.jsp"
						flush="true">
						<jsp:param name="prefix" value="jhsj" />
					</jsp:include> --%>
					<!-- <th  class="right-border bottom-border">起止时间</th>
					<td  class="bottom-border"><input class="span12 date"  type="date" id="START" name ="STRAT" operation="=" oninput="start();"></td> -->
					<th  class="right-border bottom-border">截至时间</th>
					<td  class="bottom-border"><input class="span12 date"  type="date" id="END" name ="END" operation="=" oninput="end();"></td>
					<td  class="text-left bottom-border text-right" style="width:60%">
						<button id="query" class="btn btn-link" type="button"> <i class="icon-search"></i>查询
						</button>
						<button id="clean" class="btn btn-link" type="button"> <i class="icon-trash"></i>清空
						</button>
					</td>
				</tr>
			</table>
		</form>
		<div style="height: 5px;"></div>
			<div class="overFlowX">
				<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
					<thead>
						<tr>
							<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
							<th fieldname="BMMC" maxlength="15" rowspan="2" colindex=2 >&nbsp;部门名称&nbsp;</th>
							<th colspan="4">&nbsp;业务信息&nbsp;</th>
							<th colspan="2">&nbsp;发文&nbsp;</th>
							<th colspan="2">&nbsp;通知&nbsp;</th>
						</tr>	
						<tr>
							<th fieldname="YWSL" tdalign="right" colindex=3>&nbsp;今日录入&nbsp;</th>
							<th fieldname="YWMX" tdalign="right" colindex=4>&nbsp;个人今日&nbsp;</th>
							<th fieldname="YWLJ" tdalign="right" colindex=5>&nbsp;累计录入&nbsp;</th>
							<th fieldname="YWLJMX" tdalign="right" colindex=6>&nbsp;个人累计&nbsp;</th>
							<th fieldname="FWSL" tdalign="right" colindex=7>&nbsp;今日录入&nbsp;</th>
							<th fieldname="FWLJ" tdalign="right" colindex=8>&nbsp;累计录入&nbsp;</th>
							<th fieldname="TZSL" tdalign="right" colindex=9>&nbsp;今日录入&nbsp;</th>
							<th fieldname="TZLJ" tdalign="right" colindex=10>&nbsp;累计录入&nbsp;</th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>
		</div>
	</div>
</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="txtFilter" order="" fieldname="" id="txtFilter">
				<input type="hidden" name="queryResult" id ="queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>