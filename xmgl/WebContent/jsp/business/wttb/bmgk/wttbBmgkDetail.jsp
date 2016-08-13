<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app"%>
<%	String nd = request.getParameter("nd");
	String proKey = request.getParameter("proKey");
	String flag = request.getParameter("flag")==null?"":request.getParameter("flag");
	String wtlx = request.getParameter("wtlx")==null?"":request.getParameter("wtlx");
	String wtxz = request.getParameter("wtxz")==null?"":request.getParameter("wtxz");
	String sjzt = request.getParameter("sjzt")==null?"":request.getParameter("sjzt");
 %>
<app:base/>
<style type="text/css">
	.myUl li p{
		font-size: 15px;
	}
	blockquote{
		margin-bottom:0px;
	}
</style>
<script src="${pageContext.request.contextPath }/js/common/FixTable.js"></script> 
<script type="text/javascript" charset="UTF-8">
	var controllername= "${pageContext.request.contextPath }/wttb/WttbBmgkController.do"; 
	var controllername2= "${pageContext.request.contextPath }/wttb/wttbController.do";
	var controllername3= "${pageContext.request.contextPath }/wttb/WttbXmscController.do"; 
	var g_nd = '<%=nd%>';
	var g_proKey = '<%=proKey%>';
	var g_flag = '<%=flag%>';
	var g_wtlx = '<%=wtlx%>';
	var g_wtxz = '<%=wtxz%>';
	var g_sjzt = '<%=sjzt%>';
	var historyListHeight = 0;//这个变量用于计算历史列表最大允许的高度
	var getHeight=getDivStyleHeight();	//获取弹出页高度
	$(function(){
		setPageHeight();
		doInit();
		//按钮绑定事件（导出）
		$("#btnExp").click(function() {
			if(exportRequireQuery($("#tabList"))){//该方法需传入表格的jquery对象
				printTabList("tabList");
			}
		});
		$("#btnQuery").click(function() {
			if(g_flag=="1" || g_flag=="2"){
				queryJhTabel();
			}else if(g_flag=="3"){
				queryXmscTabel();
			}else{
				queryInfoTable();
			}
		});
		//按钮绑定事件（清空查询条件）
		$("#btnClear").click(function() {
			$("#queryForm").clearFormResult();
		});
	});
	//页面初始化
	function doInit(){
		g_bAlertWhenNoResult = false;
		if(g_flag=="1" || g_flag=="2"){
			$(".MyStateTr").hide();
			queryJhTabel();
		}else if(g_flag=="3"){
			$(".MyStateTr").hide();
			queryXmscTabel();
		}else{
			$(".MyStateTr").show();
			queryInfoTable();
		}
	}
	//------------------------------------
	//查询问题表格
	//------------------------------------
	function queryXmscTabel(){
		var condProKey = "";
		if(g_proKey!=null&&g_proKey!=""){
			condProKey = "&proKey="+g_proKey;
		}
		var condNd = "";
		if(g_nd!=null&&g_nd!=""){
			condNd = "&xmid="+g_nd;
		}
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,tabList);
		defaultJson.doQueryJsonList(controllername3+"?queryDrillingData"+condProKey+condNd,data,tabList);
	}
	//---------------------------------
	//-行点击事件
	//---------------------------------
	function tr_click(obj,tabId){
		if(tabId=="tabList"){
			var elemName = $(event.target).parent().parent()[0].tagName;//("thead").html();
			if(elemName.toUpperCase()=="THEAD"){
				//点击表头就不触发行点击事件了
				return;
			}
			var rowindex = $("#"+tabId).getSelectedRowIndex();//获得选中行的索引
			var rowValue = $("#"+tabId).getSelectedRow();//获得选中行的json 字符串
			var tempJson = eval("("+rowValue+")");//字符串转JSON对象
			getEventList(tempJson.WTTB_INFO_ID,DT23);
		}
	}
	//---------------------------------
	//-获取事件表数据
	//-@param n 问题编号
	//---------------------------------
	function getEventList(n,obj){
		$.ajax({
			url:controllername2 + "?queryPfqk&id="+n,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				showSuggestList(result.msg);
				//add by zhangbr@ccthanking.com 现场说不要固定表头列头了，不好看，改回分页的形式. 问题编号：0000668
				//historyListHeight = getHeight-pageTopHeight-pageTitle-getTableTh(1)-$("#suggest").height();
				setPageHeightWtzz($(obj).attr("id"));
				var data = combineQuery.getQueryCombineData(blankForm,frmPost,obj);
				//defaultJson.doQueryJsonList(controllername2 + "?queryPfqk&id="+n,data,obj,"doFixedTableHeader2",true);
				defaultJson.doQueryJsonList(controllername2 + "?queryPfqk&id="+n,data,obj,null,true);
			}
		});
	}
	//------------------------------------------------
	//-固定表头列头
	//------------------------------------------------
	function doFixedTableHeader2(){
		var rows = $("tbody tr" ,$("#DT23"));
		var tr_obj = rows.eq(0);
		var t = $("#DT23").getTableRows();
		var tr_height = $(tr_obj).height();
		var d = (t*tr_height)+(getTableTh(3)/2);
		if(d>historyListHeight){
			d = historyListHeight;
		}
		window_width = window.screen.availWidth;
		$("#DT23").fixTable({
			fixColumn: 0,//固定列数
			width:window_width*0.3-5,//显示宽度
			height:d//显示高度
		});			
	}
	//---------------------------------
	//-显示批复意见列表
	//-@param str 查询返回的json串
	//---------------------------------
	function showSuggestList(str){
		var files = convertJson.string2json1(str);
		$("#DT22").empty();
		for(var i=0;i<1;i++){
			var file = files.response.data[i];
			var showHtml = "";
			showHtml = "<ul class='unstyled myUl'>";
			//showHtml += "<li><p>"+file.PFSJ_SV+" &nbsp;"+file.PFR_SV+"：</p></li>";
			if(file.PFNR.length>200){
				showHtml += "<li><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<abbr title='"+file.PFNR+"'>"+file.PFNR.substring(0,200)+"</abbr></p></li>";
			}else{
				showHtml += "<li><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+file.PFNR+"</p></li>";
			}
			showHtml +="</ul>";
			$("#DT22").append(showHtml);
		}
	}
	//------------------------------------
	//查询问题表格
	//------------------------------------
	function queryJhTabel(){
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,tabList);
		defaultJson.doQueryJsonList(controllername+"?queryDrillingDataJh&flag="+g_flag+"&nd="+g_nd+"&wtlx="+g_wtlx+"&wtxz="+g_wtxz+"&sjzt="+g_sjzt,data,tabList);
	}
	//------------------------------------
	//查询问题表格
	//------------------------------------
	function queryInfoTable(){
		var condProKey = "";
		if(g_proKey!=null&&g_proKey!=""){
			condProKey = "&proKey="+g_proKey;
		}
		var condNd = "";
		if(g_nd!=null&&g_nd!=""){
			condNd = "&nd="+g_nd;
		}
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,tabList);
		defaultJson.doQueryJsonList(controllername+"?queryDrillingData"+condProKey+condNd,data,tabList);
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
	//计算本页表格分页数
	function setPageHeight() {
		var height = getHeight-pageTopHeight-pageTitle-getTableTh(3)-pageNumHeight;
		var pageNum = parseInt(Math.abs(height)/pageTableOne,10);
		$("#tabList").attr("pageNum", pageNum);
	}
	//计算本页表格分页数--问题追踪
	function setPageHeightWtzz(id) {
		var height = getHeight-pageTopHeight-pageTitle-getTableTh(1)-$("#suggest").height()-pageNumHeight;
		var pageNum = parseInt(Math.abs(height)/pageTableOne,10);
		$("#"+id).attr("pageNum", pageNum);
	}
</script>
</head>
	<body>
		<div class="container-fluid">
			<p></p>
			<div class="row-fluid">
				<div class="span7">
					<div class="row-fluid">
						<div class="B-small-from-table-autoConcise" style="border: 0px;">
							<h4 class="title">
								问题概况&nbsp;&nbsp;
								<span class="pull-right">
									<button id="btnExp" class="btn" type="button">
										导出
									</button> </span>
							</h4>
							<form method="post" id="queryForm">
								<table class="B-table" width="100%">
									<!--可以再此处加入hidden域作为过滤条件 -->
									<TR style="display: none;">
										<TD>
											<INPUT id="num" type="text" class="span12" kind="text"
												fieldname="rownum" value="1000" operation="<=" keep="true" />
										</TD>
									</TR>
									<tr class="MyStateTr">
										<th class="right-border bottom-border" width="8%">
											主责中心领导
										</th>
										<td class="bottom-border">
											<select class="span12 3characters" name="JSR" fieldname="L.JSR"
												kind="dic" src="T#VIEW_ZR D:ACCOUNT:NAME:1=1 order by SORT " operation="=" logic="and" defaultMemo="全部">
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
								</table>
							</form>
							<div style="height: 5px;">
							</div>
							<div class="overFlowX">
								<table width="100%" class="table-hover table-activeTd B-table"
									id="tabList" type="single" editable="0">
									<thead>
												<tr>
													<th name="XH" id="_XH" rowspan="2" colindex=1>
														&nbsp;#&nbsp;
													</th>
													<th fieldname="WTLX" maxlength="15" tdalign="center"
														rowspan=2 colindex=2 CustomFunction="showInfoCard">
														&nbsp;&nbsp;
													</th>
													<th fieldname="WTLX" tdalign="center" rowspan=2 colindex=3>
														&nbsp;问题类别&nbsp;
													</th>
													<th fieldname="WTBT" rowspan="2" colindex=4
														style="width: 20%" maxlength="9">
														&nbsp;问题标题&nbsp;
													</th>
													<th fieldname="CQBZ"
														tdalign="center" rowspan=2 colindex=5>
														&nbsp;超期情况&nbsp;
													</th>
													<th fieldname="SJZT" tdalign="center" rowspan="2"
														colindex=6 maxlength="10">
														&nbsp;问题状态&nbsp;
													</th>
													<th fieldname="LRR" tdalign="center" rowspan=2 colindex=7>
														&nbsp;发起人&nbsp;
													</th>
													<th colspan=2>
														&nbsp;问题最新情况&nbsp;
													</th>
													<th fieldname="LRSJ" tdalign="center" rowspan=2 colindex=10>
														&nbsp;提出时间&nbsp;
													</th>
													<th fieldname="CBCS"
														tdalign="right" rowspan=2 colindex=11>
														&nbsp;催办次数&nbsp;
													</th>
												</tr>
												<tr>
													<th fieldname="JSR" colindex=8 tdalign="center">
														&nbsp;主办人&nbsp;
													</th>
													<th fieldname="XWWCSJ" colindex=9 tdalign="center">
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
				<div class="span5" style="height: 100%;">
					<blockquote>
						<h4 class="title">
							问题追踪
						</h4>
						<div class="B-small-from-table-autoConcise">
							<div id="suggest" style="border: 1px;">
								<h5 class="title" style="color: #000000;">
									问题描述
								</h5>
								<div id="DT22">
								</div>
							</div>
							<h5 class="title" style="color: #000000;">
								处理信息
							</h5>
							<form method="post" action="" id="blankForm">
								<table class="B-table" width="100%">
									<!--可以再此处加入hidden域作为过滤条件 -->
									<TR style="display: none;">
										<TD class="right-border bottom-border">
											<INPUT type="text" class="span12" kind="text"
												fieldname="rownum" value="1000" operation="<=">
										</TD>
									</TR>
									<!--可以再此处加入hidden域作为过滤条件 -->
								</table>
							</form>
							<table class="table-hover table-activeTd B-table" id="DT23"
								width="100%" pageNum="1000">
								<thead>
									<tr>
										<th fieldname="BLRJS" tdalign="center">
											人员角色
										</th>
										<th fieldname="JSR">
											姓名
										</th>
										<th fieldname="PFSJ" tdalign="center">
											处理日期
										</th>
										<th fieldname="PFNR" maxlength="8">
											内容
										</th>
										<th fieldname="FJ">
											附件
										</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</blockquote>
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