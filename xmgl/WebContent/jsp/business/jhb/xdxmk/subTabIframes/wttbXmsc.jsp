<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<%@ taglib uri= "/tld/base.tld" prefix="app"%>
<%	String xmid = request.getParameter("id")==""?"":""+request.getParameter("id"); %>
<app:base/>
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/css/bmgk/bmgk.css?version=4">
<script type="text/javascript">
	var controllername= "${pageContext.request.contextPath }/wttb/WttbXmscController.do"; 
	var controllername2= "${pageContext.request.contextPath }/wttb/wttbController.do";
	var g_xmid = '<%=xmid%>';
	$(function(){
		doInit();
		$("#ND").change(function() {
			doInit();
		});
		$("#btnQuery").click(function() {
			queryInfoTable();
		});
		//按钮绑定事件（清空查询条件）
		$("#btnClear").click(function() {
			$("#queryForm").clearFormResult();
		});
	});
	//页面初始化
	function doInit(){
		g_bAlertWhenNoResult = false;
		var condXmid = "";
		if(g_xmid!=""){
			condXmid="&xmid="+g_xmid;
		}
		queryTjgk(condXmid);
		queryJjqkfbChart(condXmid);
		queryWtxzfbChart(condXmid);
		queryWtlbfbChart(condXmid);
		queryInfoTableWjj();
		queryInfoTableYjj();
		var wjjlb=$("#tabList1 tbody tr").length;
		var yjjlb=$("#tabList2 tbody tr").length;
		if(wjjlb!='0'){
			$("#wjjlb").html("("+wjjlb+")");
		}
		if(yjjlb!='0'){
			$("#yjjlb").html("("+yjjlb+")");
		}	
		
		
		
	}
	//------------------------------------
	//查询统计概况
	//------------------------------------
	function queryTjgk(nd){
		var action1 = controllername + "?queryWttbTjgk"+nd;
		$.ajax({
			url : action1,
			success: function(result){
				 insertTable(result,"TJGK");
			}
		});
	}
	//------------------------------------
	//查询问题性质分布图表
	//------------------------------------
	function queryWtxzfbChart(nd){
		var action1 = controllername + "?queryWtxzfbChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "wtxzfbChart", "100%", "250");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("wtxzfbChartDiv"); 
			}
		});
	}
	//------------------------------------
	//查询解决情况分布图表
	//------------------------------------
	function queryJjqkfbChart(nd){
		var action1 = controllername + "?queryJjqkfbChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "wtrqfbChart", "100%", "250");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("jjqkfbChartDiv"); 
			}
		});
	}
	//------------------------------------
	//查询问题类别分布图表
	//------------------------------------
	function queryWtlbfbChart(nd){
		var action1 = controllername + "?queryWtlbfbChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "wtlbfbChart", "100%", "250");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("wtlbfbChartDiv"); 
			}
		});
	}
	//------------------------------------
	//查询问题表格
	//------------------------------------
	function queryInfoTableWjj(){
		$("#xmidWjj").val(g_xmid);
		var data = combineQuery.getQueryCombineData(queryForm1,frmPost);
		defaultJson.doQueryJsonList(controllername+"?queryInfoTable&orderFlag=1",data,tabList1);
	}
	//------------------------------------
	//查询问题表格
	//------------------------------------
	function queryInfoTableYjj(){
		$("#xmidYjj").val(g_xmid);
		var data = combineQuery.getQueryCombineData(queryForm2,frmPost);
		defaultJson.doQueryJsonList(controllername+"?queryInfoTable&orderFlag=2",data,tabList2);
	}
	//------------------------------------
	//问题信息卡
	//------------------------------------
	function showInfoCard(obj){
		var showHtml = "";
		var id = obj.WTTB_INFO_ID;
		showHtml = "<a href='javascript:void(0);' title='问题信息卡' onclick=\"openInfoCard('"+id+"')\"><i class='icon-file showInfoCard' wtid='"+obj.WTTB_INFO_ID+"'></i></a>";
		return showHtml;
	}
	//------------------------------------
	//按问题状态生成颜色
	//------------------------------------
	function doRandering(obj){
		var data = JSON.stringify(obj);
		var data1 = defaultJson.packSaveJson(data);
		var showHtml = "";
		$.ajax({
			url:controllername2 + "?getColor",
			data:data1,
			dataType:"json",
			async:false,
			success:function(result){
				var res = result.msg;
				if(obj.SJZT=="3" || obj.SJZT=="6"){
					showHtml = '<span class="label" style="width:50px;">完&nbsp;结</span>';
				}else{
					if(res=="none"){
						showHtml = '<span class="label" style="width:50px;">正&nbsp;常</span>';
					}else{
						tempJson = convertJson.string2json1(res);
						showHtml = '<span class="label '+tempJson.response.data[0].CLASS+'"  style="width:50px;">'+tempJson.response.data[0].TITLE+'</span>';
					}
				}
			}
		});
		return showHtml;
	}
	//------------------------------------
	//打开问题提报信息卡
	//------------------------------------
	function openInfoCard(n){
		var index = $(event.target).closest("tr").index();
		var tabId = $(event.target).closest("table").attr("id");
    	$("#"+tabId).cancleSelected();
    	$("#"+tabId).setSelect(index);
		if($("#"+tabId).getSelectedRowIndex()==-1){
			requireSelectedOneRow();
			return;
		}else{
			$(window).manhuaDialog({"title":"问题提报信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/wtCard.jsp?infoID="+n,"modal":"1"});
    	}
	}
	//------------------------------------
	//将查询结果放入DIV
	//------------------------------------
	function intsertUlDiv(result,tableId){
		$("#"+tableId).empty();
		if(result!="0"){
			var resultmsgobj = convertJson.string2json1(result);
			var datas = resultmsgobj.response.data;
			var showHtml = "";
			showHtml += "<ul class='unstyled'>";
			for(var i=0;i<datas.length;i++){
				var rowJson = datas[i];
				showHtml += "<li>";
				showHtml += (i+1)+"、"+rowJson.NAME+"["+rowJson.NUM+"]";
				showHtml += "</li>";
			}
			showHtml += "</ul>";
			$("#"+tableId).append(showHtml);
		}
	}
	//根据结果放入表格
	function insertTable(result,tableId){
		var resultmsgobj = convertJson.string2json1(result);
		var resultobj = convertJson.string2json1(resultmsgobj.msg);
		var subresultmsgobj = resultobj.response.data[0];
		$("#"+tableId+" span").each(function(i){
			var str = $(this).attr("bzfieldname");
			var valueStr =  "";
			if(str!=''&&str!=undefined){
				if($(subresultmsgobj).attr(str+"_SV")!=undefined){
					valueStr = $(subresultmsgobj).attr(str+"_SV");
					if($(this).attr("decimal")=="false"){
						//不需要小数的项，删掉小数点后的内容
						valueStr = valueStr.substring(0,valueStr.lastIndexOf("."));
					}else if($(this).attr("absl")=="true"){
						//是否要处理掉符号（取绝对值）
						if(valueStr.indexOf("-")==0){
							valueStr = valueStr.replace("-","");
						}
					}
				}else{
					valueStr = $(subresultmsgobj).attr(str);
				}
				if($(this).attr("hasLink")!=undefined&&valueStr!="0"){
					valueStr = '<a href="javascript:void(0)" onclick="showDataDetail(\''+$(this).attr("hasLink")+'\')">'+valueStr+'</a>';
				}
				$(this).html(valueStr);
			}
		});
	}
	function showDataDetail(proKey){
		var condXmid = "";
		if(g_xmid!=""){
			condXmid="&nd="+g_xmid;
		}
		$(window).manhuaDialog({"title":"问题详细列表","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/bmgk/wttbBmgkDetail.jsp?flag=3&proKey="+proKey+condXmid,"modal":"1"});
	}
</script>
</head>
	<body>
		<div class="container-fluid">
			<div class="B-small-from-table-autoConcise" style="border: 0px;">
				<h4
					style="background: none; color: #333; border-bottom: #ccc solid 1px;font-size:16px">
					问题概况&nbsp;&nbsp;
				</h4>
				<div class="container-fluid">
					<div class="row-fluid">
						<div class="span12">
							<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="">
								<tr>
									<td width="25%">
										<table width="100%" id="TJGK" border="0" cellspacing="0"
											class="textTable" cellpadding="0">
											<tr>
												<th>
													共提报问题：<span bzfieldname="GTBWT" hasLink="ZQ_BNDWTS"></span>
												</th>
												<th>
													其中，极其严重问题：<span bzfieldname="JQYZWT" hasLink="ZQ_NDJQYZWT"></span>
												</th>
												<th>
													严重问题：<span bzfieldname="YZWT" hasLink="ZQ_NDYZWT"></span>
												</th>
												<th>
													一般问题：<span bzfieldname="YBWT" hasLink="ZQ_NDYBWT"></span>
												</th>
											</tr>
											<tr>
												<th>
												</th>
												<th>
													已解决问题：<span bzfieldname="YJJWT" hasLink="ZQ_YJJ"></span>
												</th>
												<th>
													未解决问题：<span bzfieldname="WJJWT" hasLink="ZQ_SWJJ"></span>
												</th>
												<th>
													其中,超期未解决：<span bzfieldname="CQWJJ" hasLink="ZQ_CQWJJ"></span>
												</th>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
			<div class="B-small-from-table-autoConcise" style="border: 0px;">
				<h4
					style="background: none; color: #333; border-bottom: #ccc solid 1px;font-size:16px">
					问题整体分布
				</h4>
				<div class="container-fluid">
					<div class="row-fluid divBottom">
						<div class="span4">
							<div id="jjqkfbChartDiv"></div>
							<div class="chartTitle">解决情况分布</div>
						</div>
						<div class="span4">
							<div id="wtxzfbChartDiv"></div>
							<div class="chartTitle">问题性质分布</div>
						</div>
						<div class="span4">
							<div id="wtlbfbChartDiv"></div>
							<div class="chartTitle">问题类别分布</div>
						</div>
					</div>
				</div>
			</div>
			<div class="B-small-from-table-autoConcise" style="border: 0px;">
				<h4
					style="background: none; color: #333; border-bottom: #ccc solid 1px;font-size:16px">
					未解决问题列表 <span id="wjjlb" > </span>
				</h4>
				<div class="container-fluid">
					<div class="row-fluid divBottom">
						<div class="span12">
							<div class="B-small-from-table-autoConcise">
								<form method="post"
									action="${pageContext.request.contextPath }/insertdemo.do"
									id="queryForm1">
									<table class="B-table" width="100%">
										<TR style="display: none;">
											<TD class="right-border bottom-border"></TD>
											<TD class="right-border bottom-border">
												<INPUT type="text" class="span12" kind="text"
													fieldname="rownum" value="1000" operation="<=">
											</TD>
											<th width="8%" class="right-border bottom-border">
												问题状态
											</th>
											<td width="17%" class="right-border bottom-border">
												<input class="span12" type="text" placeholder="" name="SJZT" value="6"
													fieldname="I.SJZT" operation="!=" logic="and">
											</td>
											<th width="8%" class="right-border bottom-border">
												项目ID
											</th>
											<td width="17%" class="right-border bottom-border">
												<input class="span12" type="text" placeholder="" name="XMID" id="xmidWjj"
													fieldname="I.XMID" operation="=" logic="and">
											</td>
											<td>
												人员角色隐藏条件-用于控制按钮，不传入后台
											</td>
											<TD class="right-border bottom-border">
												<INPUT type="text" class="span12" kind="text" id="ryjs_cond">
											</TD>
										</TR>
										<!--可以再此处加入hidden域作为过滤条件 -->
									</table>
								</form>
								<div style="height: 5px;">
								</div>
								<table class="table-hover table-activeTd B-table" id="tabList1"
									width="100%" type="single" pageNum="10" noPage="true">
									<thead>
										<tr>
											<th name="XH" id="_XH" rowspan="2" colindex=1>
												&nbsp;#&nbsp;
											</th>
											<th fieldname="WTLX" maxlength="15"
												tdalign="center" rowspan=2 colindex=2
												CustomFunction="showInfoCard">
												&nbsp;&nbsp;
											</th>
											<th fieldname="WTLX" tdalign="center" rowspan=2 colindex=3>
												&nbsp;问题类别&nbsp;
											</th>
											<th fieldname="WTBT" rowspan="2" colindex=4 style="width: 20%"
												maxlength="10">
												&nbsp;问题标题&nbsp;
											</th>
											<th fieldname="SJZT" rowspan="2" colindex=5 maxlength="10" tdalign="center">
												&nbsp;问题状态&nbsp;
											</th>
											<th fieldname="LRR" tdalign="center" rowspan=2 colindex=6>
												&nbsp;发起人&nbsp;
											</th>
											<th colspan=2>
												&nbsp;问题最新情况&nbsp;
											</th>
											<th fieldname="LRSJ" tdalign="center" rowspan=2 colindex=9>
												&nbsp;提出时间&nbsp;
											</th>
											<th fieldname="CQBZ"
												tdalign="center" rowspan=2 colindex=10>
												&nbsp;超期情况&nbsp;
											</th>
											<th fieldname="HFQK"
												tdalign="center" rowspan=2 colindex=11>
												&nbsp;回复情况&nbsp;
											</th>
										</tr>
										<tr>
											<th fieldname="JSR" colindex=7 tdalign="center">
												&nbsp;接收人&nbsp;
											</th>
											<th fieldname="XWWCSJ" colindex=8 tdalign="center">
												&nbsp;希望反馈日期&nbsp;
											</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="B-small-from-table-autoConcise" style="border: 0px;">
				<h4
					style="background: none; color: #333; border-bottom: #ccc solid 1px;font-size:16px">
					已解决问题列表 <span id="yjjlb" > </span>
				</h4>
				<div class="container-fluid">
					<div class="row-fluid divBottom">
						<div class="span12">
							<div class="B-small-from-table-autoConcise">
								<form method="post"
									action="${pageContext.request.contextPath }/insertdemo.do"
									id="queryForm2">
									<table class="B-table" width="100%">
										<TR style="display: none;">
											<TD class="right-border bottom-border"></TD>
											<TD class="right-border bottom-border">
												<INPUT type="text" class="span12" kind="text"
													fieldname="rownum" value="1000" operation="<=">
											</TD>
											<th width="8%" class="right-border bottom-border">
												问题状态
											</th>
											<td width="17%" class="right-border bottom-border">
												<input class="span12" type="text" placeholder="" name="SJZT" value="6"
													fieldname="I.SJZT" operation="=" logic="and">
											</td>
											<th width="8%" class="right-border bottom-border">
												项目ID
											</th>
											<td width="17%" class="right-border bottom-border">
												<input class="span12" type="text" placeholder="" name="XMID" id="xmidYjj"
													fieldname="I.XMID" operation="=" logic="and">
											</td>
											<td>
												人员角色隐藏条件-用于控制按钮，不传入后台
											</td>
											<TD class="right-border bottom-border">
												<INPUT type="text" class="span12" kind="text" id="ryjs_cond">
											</TD>
										</TR>
										<!--可以再此处加入hidden域作为过滤条件 -->
									</table>
								</form>
								<div style="height: 5px;">
								</div>
								<table class="table-hover table-activeTd B-table" id="tabList2"
									width="100%" type="single" pageNum="10" noPage="true">
									<thead>
										<tr>
											<th name="XH" id="_XH" rowspan="2" colindex=1>
												&nbsp;#&nbsp;
											</th>
											<th fieldname="WTLX" maxlength="15"
												tdalign="center" rowspan=2 colindex=2
												CustomFunction="showInfoCard">
												&nbsp;&nbsp;
											</th>
											<th fieldname="WTLX" tdalign="center" rowspan=2 colindex=3>
												&nbsp;问题类别&nbsp;
											</th>
											<th fieldname="WTBT" rowspan="2" colindex=4 style="width: 20%"
												maxlength="10">
												&nbsp;问题标题&nbsp;
											</th>
											<th fieldname="SJZT" rowspan="2" colindex=5 maxlength="10" tdalign="center">
												&nbsp;问题状态&nbsp;
											</th>
											<th fieldname="LRR" tdalign="center" rowspan=2 colindex=6>
												&nbsp;发起人&nbsp;
											</th>
											<th colspan=2>
												&nbsp;问题最新情况&nbsp;
											</th>
											<th fieldname="LRSJ" tdalign="center" rowspan=2 colindex=9>
												&nbsp;提出时间&nbsp;
											</th>
											<th fieldname="CQBZ"
												tdalign="center" rowspan=2 colindex=10>
												&nbsp;超期情况&nbsp;
											</th>
										</tr>
										<tr>
											<th fieldname="JSR" colindex=7 tdalign="center">
												&nbsp;接收人&nbsp;
											</th>
											<th fieldname="XWWCSJ" colindex=8 tdalign="center">
												&nbsp;希望反馈日期&nbsp;
											</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
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
				<input type="hidden" name="ywid" id="ywid">
				<input type="hidden" name="txtFilter" order="desc" fieldname="SJZT,LRSJ"
					id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">

			</FORM>
		</div>
	</body>
</html>