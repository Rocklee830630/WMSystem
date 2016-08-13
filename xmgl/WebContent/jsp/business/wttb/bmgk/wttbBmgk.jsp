<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<%@ taglib uri= "/tld/base.tld" prefix="app"%>
<app:base/>
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/css/bmgk/bmgk.css?version=4">
<script type="text/javascript">
	var controllername= "${pageContext.request.contextPath }/wttb/WttbBmgkController.do"; 
	var controllername2= "${pageContext.request.contextPath }/wttb/wttbController.do";
	$(function(){
		setDefaultNd();
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
		var nd = $("#ND").val()==""?"":"&nd="+$("#ND").val();
		queryTjgk(nd);
		queryWtxzfbChart(nd);
		queryWtrqfbChart(nd);
		queryWtlbfbChart(nd);
		queryWtlbfbxxChart(nd);
		queryZbbmChart(nd);
		queryNdwttbqkChart(nd);
		queryWtzdxm(nd);
		queryYcsjzcwt(nd);
		queryJjwtzdbm(nd);
		queryInfoTable();
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
	//查询问题日期分布图表
	//------------------------------------
	function queryWtrqfbChart(nd){
		var action1 = controllername + "?queryWtrqfbChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "wtrqfbChart", "100%", "250");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("wtrqfbChartDiv"); 
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
	//查询问题类别分布详细图表
	//------------------------------------
	function queryWtlbfbxxChart(nd){
		var action1 = controllername + "?queryWtlbfbxxChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSStackedColumn2D.swf", "wtlbfbxxChart", "100%", "250");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("wtlbfbxxChartDiv"); 
			}
		});
	}
	//------------------------------------
	//查询主办部门图表
	//------------------------------------
	function queryZbbmChart(nd){
		var action1 = controllername + "?queryZbbmChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSStackedColumn2D.swf", "zbbmChart", "100%", "250");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("zbbmChartDiv"); 
			}
		});
	}
	//------------------------------------
	//查询主办部门图表
	//------------------------------------
	function queryZbbmChart(nd){
		var action1 = controllername + "?queryZbbmChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSStackedColumn2D.swf", "zbbmChart", "100%", "250");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("zbbmChartDiv"); 
			}
		});
	}
	//------------------------------------
	//查询年度问题提报情况图表
	//------------------------------------
	function queryNdwttbqkChart(nd){
		var action1 = controllername + "?queryNdwttbqkChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSLine.swf", "ndwttbqkChart", "100%", "250");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("ndwttbqkChartDiv"); 
			}
		});
	}
	//------------------------------------
	//查询问题最多项目
	//------------------------------------
	function queryWtzdxm(nd){
		var action1 = controllername + "?queryWtzdxm"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
				 intsertUlDiv(result.msg,"wtzdxmDiv");
			}
		});
	}
	//------------------------------------
	//查询延迟时间最长问题
	//------------------------------------
	function queryYcsjzcwt(nd){
		var action1 = controllername + "?queryYcsjzcwt"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
				 intsertUlDiv(result.msg,"ycsjzcwtDiv");
			}
		});
	}
	//------------------------------------
	//查询延迟时间最长问题
	//------------------------------------
	function queryJjwtzdbm(nd){
		var action1 = controllername + "?queryJjwtzdbm"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
				 intsertUlDiv(result.msg,"jjwtzdbmDiv");
			}
		});
	}
	//------------------------------------
	//查询问题表格
	//------------------------------------
	function queryInfoTable(){
		var data = combineQuery.getQueryCombineData(queryForm,frmPost);
		defaultJson.doQueryJsonList(controllername+"?queryInfoTable",data,tabList);
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
		var nd = $("#ND").val()==""?"":"&nd="+$("#ND").val();
		$(window).manhuaDialog({"title":"问题详细列表","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/bmgk/wttbBmgkDetail.jsp?proKey="+proKey+nd,"modal":"1"});
	}
</script>
</head>
	<body>
		<div class="container-fluid">
			<p></p>
			<div class="B-small-from-table-auto" style="border: 0px;">
				<h4
					style="background: none; color: #333; border-bottom: #ccc solid 1px;">
					问题概况&nbsp;&nbsp;
					<select class="span2 year" style="width: 8%" id="ND" name="ND" fieldname="ND" operation="="
						kind="dic" noNullSelect ="true" 
						src="T#(select distinct ND from GC_JH_SJ S where SFYX='1' union all select distinct to_char(I.LRSJ,'yyyy') as ND from GC_JH_SJ S,WTTB_INFO I where S.ND!=to_char(I.LRSJ,'yyyy')): distinct ND as NDCODE: ND:1=1 ORDER BY NDCODE asc">
					</select>
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
													累计问题数：<span bzfieldname="LJWTS" hasLink="ZQ_LJWTS"></span>
												</th>
												<th>
													其中，极其严重问题：<span bzfieldname="LJJQYZWT" hasLink="ZQ_LJJQYZWT"></span>[<span bzfieldname="LJJQYZWTBFB"></span>%]
												</th>
												<th>
													严重问题：<span bzfieldname="LJYZWT" hasLink="ZQ_LJYZWT"></span>[<span bzfieldname="LJYZWTBFB"></span>%]
												</th>
												<th>
													一般问题：<span bzfieldname="LJYBWT" hasLink="ZQ_LJYBWT"></span>[<span bzfieldname="LJYBWTBFB"></span>%]
												</th>
											</tr>
											<tr>
												<th>
													本年度问题数：<span bzfieldname="BNDWTS" hasLink="ZQ_BNDWTS"></span>
												</th>
												<th>
													其中，极其严重问题：<span bzfieldname="NDJQYZWT" hasLink="ZQ_NDJQYZWT"></span>[<span bzfieldname="NDJQYZWTBFB"></span>%]
												</th>
												<th>
													严重问题：<span bzfieldname="NDYZWT" hasLink="ZQ_NDYZWT"></span>[<span bzfieldname="NDYZWTBFB"></span>%]
												</th>
												<th>
													一般问题：<span bzfieldname="NDYBWT" hasLink="ZQ_NDYBWT"></span>[<span bzfieldname="NDYBWTBFB"></span>%]
												</th>
											</tr>
											<tr>
												<th>
												</th>
												<th>
													已解决：<span bzfieldname="YJJ" hasLink="ZQ_YJJ"></span>[<span bzfieldname="YJJBFB"></span>%]
												</th>
												<th>
													尚未解决：<span bzfieldname="SWJJ" hasLink="ZQ_SWJJ"></span>[<span bzfieldname="SWJJBFB"></span>%]
												</th>
												<th>
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
			<div class="B-small-from-table-auto" style="border: 0px;">
				<h4
					style="background: none; color: #333; border-bottom: #ccc solid 1px;">
					问题统计与分析
				</h4>
				<div class="container-fluid">
					<div class="row-fluid divBottom">
						<div class="span4">
							<div id="wtxzfbChartDiv"></div>
							<div class="chartTitle">问题性质分布</div>
						</div>
						<div class="span4">
							<div id="wtrqfbChartDiv"></div>
							<div class="chartTitle">问题日期分布</div>
						</div>
						<div class="span4">
							<div id="wtlbfbChartDiv"></div>
							<div class="chartTitle">问题类别分布</div>
						</div>
					</div>
					<div class="row-fluid divBottom">
						<div class="span4">
							<div class="msgTitle">问题最多的项目[TOP5]</div>
							<div id="wtzdxmDiv"></div>
						</div>
						<div class="span4">
							<div class="msgTitle">延迟时间最长的问题[TOP5]</div>
							<div id="ycsjzcwtDiv"></div>
						</div>
						<div class="span4">
							<div class="msgTitle">解决问题最多的部门/分管领导[TOP5]</div>
							<div id="jjwtzdbmDiv"></div>
						</div>
					</div>
					<div class="row-fluid divBottom">
						<div class="span12">
							<div id="wtlbfbxxChartDiv"></div>
							<div class="chartTitle">问题类别</div>
						</div>
					</div>
					<div class="row-fluid divBottom">
						<div class="span12">
							<div id="zbbmChartDiv"></div>
							<div class="chartTitle">主办部门</div>
						</div>
					</div>
					<div class="row-fluid">
						<div class="span12 divBottom">
							<div id="ndwttbqkChartDiv"></div>
							<div class="chartTitle">年度问题提报情况</div>
						</div>
					</div>
					<div class="row-fluid divBottom">
						<div class="span12">
							<div class="B-small-from-table-autoConcise">
								<form method="post"
									action="${pageContext.request.contextPath }/insertdemo.do"
									id="queryForm">
									<table class="B-table" width="100%">
										<!--可以再此处加入hidden域作为过滤条件 -->
										<TR style="display: none;">
											<TD class="right-border bottom-border"></TD>
											<TD class="right-border bottom-border">
												<INPUT type="text" class="span12" kind="text"
													fieldname="rownum" value="1000" operation="<=">
											</TD>
											<td>
												人员角色隐藏条件-用于控制按钮，不传入后台
											</td>
											<TD class="right-border bottom-border">
												<INPUT type="text" class="span12" kind="text" id="ryjs_cond">
											</TD>
										</TR>
										<!--可以再此处加入hidden域作为过滤条件 -->
										<tr>
											<th width="8%" class="right-border bottom-border">
												问题标题
											</th>
											<td width="17%" class="right-border bottom-border">
												<input class="span12" type="text" placeholder="" name="WTBT"
													fieldname="I.WTBT" operation="like" logic="and">
											</td>
											<th width="8%" class="right-border bottom-border">
												问题性质
											</th>
											<td width="27%" class="bottom-border">
												<select class="span12 6characters" name="WTXZ" fieldname="I.WTXZ"
													kind="dic" src="WTXZ" operation="=" logic="and" defaultMemo="全部">
												</select>
											</td>
											<td class="right-border bottom-border text-right">
												<button id="btnQuery" class="btn btn-link" type="button">
													<i class="icon-search"></i>查询
												</button>
												<button id="btnClear" class="btn btn-link" type="button">
													<i class="icon-trash"></i>清空
												</button>
											</td>
										</tr>
									</table>
								</form>
								<div style="height: 5px;">
								</div>
								<table class="table-hover table-activeTd B-table" id="tabList"
									width="100%" type="single" pageNum="10">
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
											<th fieldname="BLQK" CustomFunction="doRandering"
												tdalign="center" rowspan=2 colindex=10>
												&nbsp;办理情况&nbsp;
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