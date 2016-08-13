<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<%@ taglib uri= "/tld/base.tld" prefix="app"%>
<app:base/>
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/css/bmgk/bmgk.css?version=4">
<style type="text/css">
	.divBottom{
		border:0px;
	}
</style>
<script type="text/javascript">
	var controllername= "${pageContext.request.contextPath }/wttb/WttbBmgkController.do"; 
	var controllername2= "${pageContext.request.contextPath }/wttb/wttbController.do";
	$(function(){
		setDefaultNd();
		doInit();
		$("#ND").change(function() {
			doInit();
			var sysdate = new Date();
			if($("#ND").val()==sysdate.getFullYear()){
				$("span[name='head']").html("截止"+getCurrentDate("yyyy年MM月DD日")+"，");
			}else{
				$("span[name='head']").html("");
			}
		});
		$("#showDetailList").click(function(){
			$(window).manhuaDialog({"title":"问题详细列表","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/bmgk/wttbDetailList.jsp","modal":"1"});
		});
	});
	//页面初始化
	function doInit(){
		var nd = $("#ND").val()==""?"":"&nd="+$("#ND").val();
		if($("#ND").val()==getCurrentDate("yyyy")){
			$("span[name='head']").html("截止"+getCurrentDate("yyyy年MM月DD日")+"，");
		}
		queryTjgk(nd);
		queryWtxzfbChart(nd);
		queryWtjjcdChart(nd);
		queryWtlbfbChart(nd);
		queryTcbmChart(nd);
		queryZbbmChart(nd);
		queryZbbmMycdChart(nd);
		queryZbldChart(nd);
		queryWtzdxm(nd);
		queryYcsjzcwt(nd);
		queryJjwtzdbm(nd);
		queryJjxlzgbm(nd);
		queryTcwtfb(nd);
		queryJswtfb(nd);
		queryWtmycdChart(nd);
		queryWtcqqkChart(nd);
		queryNdwttbqkChart(nd);
	}
	//------------------------------------
	//查询统计概况
	//------------------------------------
	function queryTjgk(nd){
		var action1 = controllername + "?queryWttbTjgkNew"+nd;
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
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "wtxzfbChart", "100%", "200");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("wtxzfbChartDiv"); 
			}
		});
	}
	//------------------------------------
	//查询问题日期分布图表
	//------------------------------------
	function queryWtjjcdChart(nd){
		var action1 = controllername + "?queryWtjjcdChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "wtjjcdChart", "100%", "200");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("wtjjcdChartDiv"); 
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
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "wtlbfbChart", "100%", "200");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("wtlbfbChartDiv"); 
			}
		});
	}
	//------------------------------------
	//查询问题类别分布图表
	//------------------------------------
	function queryWtcqqkChart(nd){
		var action1 = controllername + "?queryWtcqqkChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
				var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "cqqkChart", "100%", "150");  
				myChart.setJSONData(result.msg);
				myChart.render("cqqkChartDiv");
			}
		});
	}
	//------------------------------------
	//查询主办领导图表
	//------------------------------------
	function queryZbldChart(nd){
		var action1 = controllername + "?queryZbldChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSStackedColumn2D.swf", "zbldChart", "100%", "150");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("zbldChartDiv"); 
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
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSStackedColumn2D.swf", "zbbmChart", "100%", "150");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("zbbmChartDiv"); 
			}
		});
	}
	//------------------------------------
	//查询主办部门图表
	//------------------------------------
	function queryTcbmChart(nd){
		var action1 = controllername + "?queryWttcChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSStackedColumn2D.swf", "tcbmChart", "100%", "300");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("tcbmChartDiv"); 
			}
		});
	}
	//------------------------------------
	//查询接收部门满意程度图表
	//------------------------------------
	function queryZbbmMycdChart(nd){
		var action1 = controllername + "?queryZbbmMycdChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSStackedColumn2D.swf", "mycdztChart", "100%", "300");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("mycdztChartDiv");
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
	//查询解决问题最多部门
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
	//查询延迟时间最长问题
	//------------------------------------
	function queryJjxlzgbm(nd){
		var action1 = controllername + "?queryJjxlzgbm"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
				 intsertUlDiv(result.msg,"jjxlzgbmDiv");
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
	//查询满意程度分布详细图表
	//------------------------------------
	function queryWtmycdChart(nd){
		var action1 = controllername + "?queryWtmycdChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "mycdfbxxChart", "100%", "250");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("wtmycdChartDiv"); 
			}
		});
	}
	
	//------------------------------------
	//查询提出问题分布多级饼图
	//------------------------------------
	function queryTcwtfb(nd){
		var action1 = controllername + "?queryWttcbtChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
				var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MultiLevelPie.swf", "tcfbChart", "100%", "300");  
				myChart.setJSONData(result.msg);
				myChart.render("tcfbChartDiv"); 
			}
		});
	}
	//------------------------------------
	//查询接收问题分布
	//------------------------------------
	function queryJswtfb(nd){
		var action1 = controllername + "?queryWtjsbtChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
				var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MultiLevelPie.swf", "jsfbChart", "100%", "300");  
				myChart.setJSONData(result.msg);  
				myChart.render("jsfbChartDiv"); 
			}
		});
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
				var nameStr = rowJson.NAME;
				var numStr = "";
				if($(rowJson).attr("NUMBFB")!=undefined){
					numStr = $(rowJson).attr("NUMBFB")+"%";
				}else{
					numStr = $(rowJson).attr("NUM");
				}
				var maxNum = 16;
				if(nameStr.length>maxNum){
					showHtml += (i+1)+"、<abbr title='"+nameStr+"'>"+nameStr.substring(0,maxNum-1)+"</abbr>["+numStr+"]";
				}else{
					showHtml += (i+1)+"、"+nameStr+"["+numStr+"]";
				}
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
		$("."+tableId+" span").each(function(i){
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
				if($(this).attr("prefix")!=undefined && valueStr!=""){
					valueStr = "<span>"+$(this).attr("suffix")+"</span>"+valueStr;
				}
				$(this).html(valueStr);
			}
		});
	}
	function showDataDetail(label,fieldname){
		$(window).manhuaDialog({"title":"问题详细列表","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/bmgk/wttbDetailList.jsp?fieldname="+fieldname+"&label="+label,"modal":"1"});
	}
</script>
</head>
	<body>
		<div class="container-fluid">
			<div class="B-small-from-table-auto" style="border: 0px;">
				<h4 style="background:#F0F0F0;color:#333;height:27px;line-height:27px;padding:0px 5px 0px 5px;font-size:16px;">
					问题概况及跟踪&nbsp;&nbsp;
					<select class="span2 year" style="width: 8%;height:25px;padding:0px;margin-bottom:5px;" id="ND" name="ND" fieldname="ND" operation="="
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
										<table width="100%" border="0" cellspacing="0"
											class="TJGK textTable" cellpadding="0">
											<tr>
												<th>
													<span name="head"></span>本年度中心共有问题：<span bzfieldname="BNDWTS"  content="只查询问题状态为'处理中'，'无法解决'，'已处理'，'已解决'的问题，本页面所有数字都有这个条件"></span><span>个。</span>
													&nbsp;&nbsp;&nbsp;<a href="javascript:void(0);" id="showDetailList">查看问题详细列表</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;整体分布如下：
												</th>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</div>
						<div id="fbtDiv" class="span12" style="margin-left:0px;">
							<div class="row-fluid divBottom">
								<div class="span4">
									<div id="wtxzfbChartDiv"></div>
									<div class="chartTitle">轻重缓急</div>
								</div>
								<div class="span4">
									<div id="wtjjcdChartDiv"></div>
									<div class="chartTitle">解决程度</div>
								</div>
								<div class="span4">
									<div id="wtlbfbChartDiv"></div>
									<div class="chartTitle">问题类型</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="B-small-from-table-auto" style="border: 0px;">
				<h4 style="background:#F0F0F0;color:#333;height:20px;line-height:20px;padding:0px 5px 0px 5px;font-size:16px;">
					问题提出
				</h4>
				<div class="container-fluid">
					<div class="row-fluid">
						<div class="span12 divBottom">
							<div class="span3" id="tcfbChartDiv"></div>
							<div class="span9" id="tcbmChartDiv"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="B-small-from-table-auto" style="border: 0px;">
				<h4 style="background:#F0F0F0;color:#333;height:20px;line-height:20px;padding:0px 5px 0px 5px;font-size:16px;">
					问题接收
				</h4>
				<div class="container-fluid">
					<div class="row-fluid">
						<div class="span12 divBottom">
							<div class="span3" id="jsfbChartDiv"></div>
							<div class="span9" >
								<div class="span12" style="height:49%;display:inline-block;">
									<div class="span6" id="zbldChartDiv"></div>
									<div class="span6" id="cqqkChartDiv"></div>
								</div>
								<div class="span12" style="height:49%;display:inline-block;margin-left:0px;">
									<div id="zbbmChartDiv"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="B-small-from-table-auto" style="border: 0px;">
				<h4 style="background:#F0F0F0;color:#333;height:20px;line-height:20px;padding:0px 5px 0px 5px;font-size:16px;">
					满意程度
				</h4>
				<div class="container-fluid">
					<div class="row-fluid">
						<div class="span12 divBottom">
							<div class="span3" id="wtmycdChartDiv"></div>
							<div class="span9" id="mycdztChartDiv"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="B-small-from-table-auto" style="border: 0px;">
				<h4 style="background:#F0F0F0;color:#333;height:20px;line-height:20px;padding:0px 5px 0px 5px;font-size:16px;">
					月度分布
				</h4>
				<div class="container-fluid">
					<div class="row-fluid">
						<div class="span12 divBottom">
							<div class="container-fluid">
								<div class="row-fluid">
									<div class="span12 divBottom">
										<div id="ndwttbqkChartDiv"></div>
									</div>
								</div>
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