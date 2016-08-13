<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<%@ taglib uri= "/tld/base.tld" prefix="app"%>
<app:base/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/css/bmgk/bmgk.css?version=4">
<script type="text/javascript">
	var controllername= "${pageContext.request.contextPath }/wttb/WttbBmgkController.do"; 
	var controllername2= "${pageContext.request.contextPath }/wttb/wttbController.do";
	$(function(){
		setDefaultNd();
		doInit();
		var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
		showAutoComplete("QXMMC",autocompleteXmmcController+"?xmmcAutoQuery","getXmmcQueryCondition");
		$("#ND").change(function() {
			doInit();
			var sysdate = new Date();
			if($("#ND").val()==sysdate.getFullYear()){
				$("span[name='head']").html("截止"+getCurrentDate("yyyy年MM月DD日")+"，");
			}else{
				$("span[name='head']").html("");
			}
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
		$("span[name='head']").html("截止"+getCurrentDate("yyyy年MM月DD日")+"，");
		setDefaultNd("q_nd");
		queryTjgk(nd);
		queryWtxzfbChart(nd);
		queryWtrqfbChart(nd);
		queryWtlbfbChart(nd);
		queryZbbmChart(nd);
		queryZbldChart(nd);
		queryNdwttbqkChart(nd);
		queryWtzdxm(nd);
		queryYcsjzcwt(nd);
		queryJjwtzdbm(nd);
		queryJjxlzgbm(nd);
		queryInfoTable();
		initTHSort("tabList");
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
	//查询主办领导图表
	//------------------------------------
	function queryZbldChart(nd){
		var action1 = controllername + "?queryZbldChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSStackedColumn2D.swf", "zbbmChart", "100%", "250");  
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
	//查询问题表格
	//------------------------------------
	function queryInfoTable(){
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,tabList);
		defaultJson.doQueryJsonList(controllername+"?queryInfoTable",data,tabList);
	}
	function sortQuery(){
		queryInfoTable();
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
	//-----------------------
	//--项目名称自动匹配
	//-----------------------
	function getXmmcQueryCondition(){
		var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":"S.pxh"}]}';
		var jsonData = eval('(' + initData + ')'); 
		//项目名称
		if("" != $("#QXMMC").val()){
			var defineCondition = {"value": "%"+$("#QXMMC").val()+"%","fieldname":"S.XMMC","operation":"like","logic":"and"};
			jsonData.querycondition.conditions.push(defineCondition);
		}
		return JSON.stringify(jsonData);
	}
</script>
</head>
	<body>
		<div class="container-fluid">
			<div class="B-small-from-table-auto" style="border: 0px;">
				<h4
					style="background:#F0F0F0;color:#333;height:30px;padding:5px 5px 5px 5px;font-size:13pt;">
					问题概况及跟踪&nbsp;&nbsp;
					<select class="span2 year" style="width: 8%;display:none;" id="ND" name="ND" fieldname="ND" operation="="
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
													<span name="head"></span>本年度共发起问题：<span bzfieldname="BNDWTS" hasLink="ZQ_BNDWTS"></span><span>个。</span>
													<span>其中一般件：</span><span bzfieldname="NDYBWT" hasLink="ZQ_NDYBWT"></span><span>个，</span>
													<span>急件：</span><span bzfieldname="NDYZWT" hasLink="ZQ_NDYZWT"></span><span>个，</span>
													<span>特急件：</span><span bzfieldname="NDJQYZWT" hasLink="ZQ_NDJQYZWT"></span><span>个。</span>
												</th>
											</tr>
											<tr>
												<th>
													<span>全部问题中，已解决：</span><span bzfieldname="LJYJJWT" hasLink="ZQ_LJYJJWT"></span><span>个，</span>
													<span>未解决：</span><span bzfieldname="LJWJJWT" hasLink="ZQ_LJWJJWT"></span><span>个，</span>
													<span>正在处理解决：</span><span bzfieldname="LJZZJJWT" hasLink="ZQ_LJZZJJWT"></span><span>个。</span>
													<span>问题最集中的项目是：</span><span bzfieldname="WTZDXM" hasLink="ZQ_WTZDXM"></span><span>，</span>
													<span>问题最集中的类型是：</span><span bzfieldname="WTZDLX" hasLink="ZQ_WTZDLX"></span><span>。</span>
												</th>
											</tr>
											<tr>
												<th>
													<span>提交给项目管理公司：</span><span bzfieldname="TJGXMGLGS" hasLink="ZQ_TJGXMGLGS"></span><span>个，</span>
													<span>其中已解决：</span><span bzfieldname="TJGXMGLGSYJJ" hasLink="ZQ_TJGXMGLGSYJJ"></span><span>个；</span>
													<span>提交给中心各部门问题：</span><span bzfieldname="TJGZXGBM" hasLink="ZQ_TJGZXGBM"></span><span>个，</span>
													<span>其中已解决：</span><span bzfieldname="TJGZXGBMYJJ" hasLink="ZQ_TJGZXGBMYJJ"></span><span>个，</span>
													<span>提交给中心领导问题：</span><span bzfieldname="TJGZXLD" hasLink="ZQ_TJGZXLD"></span><span>个，</span>
													<span>其中已解决：</span><span bzfieldname="TJGZXLDYJJ" hasLink="ZQ_TJGZXLDYJJ"></span><span>个。</span>
												</th>
											</tr>
											<tr>
												<th>
													<span>超期未解决问题：</span><span bzfieldname="CQWJJWT" hasLink="ZQ_CQWJJWT"></span><span>个，</span>
													<span>超期未反馈问题：</span><span bzfieldname="CQWFKWT" hasLink="ZQ_CQWFKWT"></span><span>个。</span>
												</th>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</div>
						<div class="span12" style="margin-left:0px;margin-top:10px;">
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
											<th width="5%" class="right-border bottom-border text-right">
												问题状态
											</th>
											<td class="right-border bottom-border" width="15%">
												<input class="span12" type="text" name="SJZT"  FIELDTYPE="text"
													fieldname="SJZT" operation="in" value="2,3,4,6" logic="and" >
											</td>
										</TR>
										<!--可以再此处加入hidden域作为过滤条件 -->
										<tr>
											<th class="right-border bottom-border" style="display:none;">
												年度
											</th>
											<td class="right-border bottom-border" style="display:none;">
												<select class="span12 year" name="LRSJ" fieldname="to_char(I.LRSJ,'YYYY')" operation="="
													kind="dic" logic="and" id="q_nd" keep="true"
													src="T#(select distinct ND from GC_JH_SJ S where SFYX='1' union all select distinct to_char(I.LRSJ,'yyyy') as ND from GC_JH_SJ S,WTTB_INFO I where S.ND!=to_char(I.LRSJ,'yyyy')): distinct ND as NDCODE: ND:1=1 ORDER BY NDCODE asc">
												</select>
											</td>
											<th class="right-border bottom-border">
												问题状态
											</th>
											<td class="right-border bottom-border">
												<select class="span12 4characters" name="LRSJ" fieldname="SJZT" operation="="
													kind="dic" logic="and" src="T#FS_DIC_TREE :DIC_CODE:DIC_VALUE:DIC_NAME_CODE='WTZT' and DIC_CODE in ('2','3','4','6') " defaultMemo="全部">
												</select>
											</td>
											<th class="right-border bottom-border">
												类别
											</th>
											<td class="right-border bottom-border">
												<select class="span12 5characters" name=""WTLX"" fieldname="WTLX" operation="="
													kind="dic" logic="and" src="WTLX" defaultMemo="全部">
												</select>
											</td>
											<th class="right-border bottom-border">
												问题标题
											</th>
											<td width="17%" class="right-border bottom-border">
												<input class="span12" type="text" placeholder="" name="WTBT"
													fieldname="I.WTBT" operation="like" logic="and">
											</td>
											<th class="right-border bottom-border">
												问题性质
											</th>
											<td class="bottom-border">
												<select class="span12 3characters" name="WTXZ" fieldname="I.WTXZ"
													kind="dic" src="WTXZ" operation="=" logic="and" defaultMemo="全部">
												</select>
											</td>
											<th class="right-border bottom-border">
												超期情况
											</th>
											<td>
												<select class="span12 3characters" name="CQBZ" fieldname="I.CQBZ"
													kind="dic" src="CQBZ" operation="=" logic="and" defaultMemo="全部">
												</select>
											</td>
											<td class="right-border bottom-border text-right" rowspan=3>
												<button id="btnQuery" class="btn btn-link" type="button">
													<i class="icon-search"></i>查询
												</button>
												<button id="btnClear" class="btn btn-link" type="button">
													<i class="icon-trash"></i>清空
												</button>
											</td>
										</tr>
										<tr>
											<th class="right-border bottom-border">
												项目名称
											</th>
											<td class="bottom-border" colspan=3>
												<input class="span12" type="text" placeholder="" name="QXMMC"
													fieldname="XMMC" operation="like" id="QXMMC"
													autocomplete="off" tablePrefix="S">
											</td>
											<th class="right-border bottom-border">
												标段名称
											</th>
											<td class="bottom-border">
												<input class="span12" type="text" placeholder="" name="QBDMC"
													fieldname="BDMC" operation="like">
											</td>
											<th class="right-border bottom-border">
												发起部门
											</th>
											<td class="bottom-border">
												<select class="span12 6characters" name="LRBM" fieldname="I.LRBM"
													kind="dic" src="T#VIEW_YW_ORG_DEPT D:ROW_ID:DEPT_NAME:D.ACTIVE_FLAG='1' " operation="=" logic="and" defaultMemo="全部">
												</select>
											</td>
											
											<th class="right-border bottom-border">
												发起人
											</th>
											<td class="bottom-border">
												<input class="span12 4characters" name="P.NAME" fieldname="P.NAME" type="text"
													operation="like" logic="and">
											</td>
										</tr>
										<tr>
											<th class="right-border bottom-border">
												主办部门
											</th>
											<td class="bottom-border">
												<select class="span12 6characters" name="JSBM" fieldname="L.JSBM"
													kind="dic" src="T#VIEW_YW_ORG_DEPT D:ROW_ID:DEPT_NAME:D.ACTIVE_FLAG='1' " operation="=" logic="and" defaultMemo="全部">
												</select>
											</td>
											
											<th class="right-border bottom-border">
												主责中心领导
											</th>
											<td class="bottom-border">
												<select class="span12 3characters" name="JSR" fieldname="L.JSR"
													kind="dic" src="T#VIEW_ZR D:ACCOUNT:NAME:1=1 order by SORT " operation="=" logic="and" defaultMemo="全部">
												</select>
											</td>
											<td class="bottom-border" colspan=6>
												
											</td>
										</tr>
									</table>
								</form>
								<div style="height: 5px;">
								</div>
								<div class="overFlowX">
									<table class="table-hover table-activeTd B-table" id="tabList"
										width="100%" type="single" pageNum="10">
										<thead>
											<tr>
												<th name="XH" id="_XH" rowspan="2" colindex=1>
													&nbsp;#&nbsp;
												</th>
												<th fieldname="WTLX" maxlength="15"
													tdalign="center" 
													CustomFunction="showInfoCard">
													&nbsp;&nbsp;
												</th>
												<th fieldname="WTLX" tdalign="center" sort="true">
													&nbsp;类别&nbsp;
												</th>
												<th fieldname="WTBT"
													maxlength="10" sort="true">
													&nbsp;问题标题&nbsp;
												</th>
												<th fieldname="SJZT"  maxlength="10" tdalign="center" sort="true">
													&nbsp;问题状态&nbsp;
												</th>
												<th fieldname="SJSJ" tdalign="center"  sort="true">
													&nbsp;解决时间&nbsp;
												</th>
												<th fieldname="LRBM" sort="true">
													&nbsp;发起部门&nbsp;
												</th>
												<th fieldname="JSBM" sort="true">
													&nbsp;主办部门&nbsp;
												</th>
												<th fieldname="JSR" sort="true">
													&nbsp;主办人&nbsp;
												</th>
												<th fieldname="BLQK" tdalign="center" CustomFunction="doRandering">
													&nbsp;办理情况&nbsp;
												</th>
												<th fieldname="XMMC" maxlength="15" sort="true">
													&nbsp;项目名称&nbsp;
												</th>
												<th fieldname="BDMC"  maxlength="15" sort="true">
													&nbsp;标段名称&nbsp;
												</th>
												<th fieldname="LRR" sort="true">
													&nbsp;发起人&nbsp;
												</th>
												<th fieldname="LRSJ" sort="true">
													&nbsp;提出时间&nbsp;
												</th>
												<th fieldname="CBCS" sort="true" tdalign="right">
													&nbsp;催办次数&nbsp;
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
			<div class="B-small-from-table-auto" style="border: 0px;">
				<h4 style="background:#F0F0F0;color:#333;height:30px;padding:5px 5px 5px 5px;font-size:13pt;">
					问题分类统计
				</h4>
				<div class="container-fluid">
					<div style="height:5px;"></div>
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
					<div style="height:10px;">
					
					</div>
					<div class="row-fluid divBottom">
						<div class="span3">
							<div class="msgTitle">问题最多的项目[TOP5]</div>
							<div id="wtzdxmDiv"></div>
						</div>
						<div class="span3">
							<div class="msgTitle">延迟时间最长的问题[TOP5]</div>
							<div id="ycsjzcwtDiv"></div>
						</div>
						<div class="span3">
							<div class="msgTitle">解决问题最多的部门/分管领导[TOP5]</div>
							<div id="jjwtzdbmDiv"></div>
						</div>
						<div class="span3">
							<div class="msgTitle">问题解决效率最高的部门/领导[TOP5]</div>
							<div id="jjxlzgbmDiv"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="B-small-from-table-auto" style="border: 0px;">
				<h4 style="background:#F0F0F0;color:#333;height:30px;padding:5px 5px 5px 5px;font-size:13pt;">
					各主办部门问题解决情况
				</h4>
				<div class="container-fluid">
					<div class="row-fluid divBottom">
						<div class="span12">
							<div id="zbbmChartDiv"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="B-small-from-table-auto" style="border: 0px;">
				<h4 style="background:#F0F0F0;color:#333;height:30px;padding:5px 5px 5px 5px;font-size:13pt;">
					各领导问题解决情况
				</h4>
				<div class="container-fluid">
					<div class="row-fluid divBottom">
						<div class="span12">
							<div id="zbldChartDiv"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="B-small-from-table-auto" style="border: 0px;">
				<h4 style="background:#F0F0F0;color:#333;height:30px;padding:5px 5px 5px 5px;font-size:13pt;">
					年度问题整体解决情况
				</h4>
				<div class="container-fluid">
					<div class="row-fluid">
						<div class="span12 divBottom">
							<div id="ndwttbqkChartDiv"></div>
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