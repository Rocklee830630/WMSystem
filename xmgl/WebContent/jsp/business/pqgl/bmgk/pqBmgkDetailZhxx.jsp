<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>排迁计划</title>
		<%	String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
		%>
		<style type="text/css"> 
		i.showXmxxkInfo:hover,i.showXmxxkInfo:focus {
			cursor: pointer;
		}
		</style>
		<script type="text/javascript" charset="utf-8">
			//请求路径，对应后台RequestMapping
			var controllername= "${pageContext.request.contextPath }/sjzq/pqSjzqController.do";
			g_bAlertWhenNoResult = false;
			var g_nd = '<%=nd%>';
			var g_proKey = '<%=proKey%>';
			var btnNum = 0;
			//计算本页表格分页数
			function setPageHeight(){
				var getHeight=getDivStyleHeight();
				var height = getHeight-pageTopHeight-pageTitle-getTableTh(1)-pageNumHeight;
				var pageNum = parseInt(height/pageTableOne,10);
				$("#tabList").attr("pageNum",pageNum);
			}
			//页面初始化
			$(function() {
				setPageHeight();
				doInit();
			});
			function openXmxxkInfo(){
			    var index = $(event.target).closest("tr").index();
				$("#tabList").cancleSelected();
		    	$("#tabList").setSelect(index);
				if($("#tabList").getSelectedRowIndex()==-1){
					requireSelectedOneRow();
					return;
				}else{
					var n = $(this).attr("jhsjid");
					$("#rowJsonXML").val($("#tabList").getSelectedRow());
					$(window).manhuaDialog({"title":"排迁任务信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/pqgl/pqxmzhxx/pqxmxxk.jsp?jhsjid="+n,"modal":"1"});
		    	}
			}
			//页面默认参数
			function doInit(){
				doSearch();
			}
			function doSearch(){
				var condProKey = "";
				if(g_proKey!=null&&g_proKey!=""){
					condProKey = "&proKey="+g_proKey;
				}
				var condNd = "";
				if(g_nd!=null&&g_nd!=""){
					condNd = "&nd="+g_nd;
				}
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,tabList);
				defaultJson.doQueryJsonList(controllername+"?queryDrillingDataZh"+condNd+condProKey,data,tabList,null,false);
			}
			function getWinData(data){
				var index =	$("#tabList").getSelectedRowIndex();
				var rowValue = $("#tabList").getSelectedRow();
				
				//组成保存json串格式
 				var data1 = defaultJson.packSaveJson(data);
 				//调用ajax插入
		 		//defaultJson.doUpdateJson(controllername + "?doJhfk", data1,tabList);
		 		if(btnNum==0){
					var index =	$("#tabList").getSelectedRowIndex();
					defaultJson.doUpdateJson(controllername + "?doJhfk",data1,tabList,null);
					$("#tabList").cancleSelected();
					$("#tabList").setSelect(index);
				}else{
					$.ajax({
						url:controllername + "?doZxmgl",
						data:data1,
						dataType:"json",
						async:false,
						success:function(result){
							xAlertMsg("维护子项目成功");
						}
					});
				}
				$("#tabList").cancleSelected();
				$("#tabList").setSelect(index);
			}
			//单击行事件
			function tr_click(obj,tabId){
				if(tabId=="tabList"){
					var rowValue = $("#"+tabId).getSelectedRow();//获得选中行的json 字符串
					var tempJson = eval("("+rowValue+")");//字符串转JSON对象
					//$("#resultXML").val(tempJson);
				}
			}
			//详细信息
			function rowView(index){
				var obj = $("#tabList").getSelectedRowJsonByIndex(index);
				var id = convertJson.string2json1(obj).XMID;
				$(window).manhuaDialog(xmscUrl(id));
			}
			function doJhfk(data){
				var index =	$("#tabList").getSelectedRowIndex();
				var rowValue = $("#tabList").getSelectedRow();
				//组成保存json串格式
 				var data1 = defaultJson.packSaveJson(data);
				defaultJson.doUpdateJson(controllername + "?doJhsjfk",data1,tabList,null);
				$("#tabList").cancleSelected();
				$("#tabList").setSelect(index);
			}
			function doRandering(obj){
				var showHtml = "";
				showHtml = "<a href='javascript:void(0);' title='排迁任务信息卡' onclick='openXmxxkInfo();'><i class='icon-file showXmxxkInfo' jhsjid='"+obj.GC_JH_SJ_ID+"'></i></a>";
				return showHtml;
			}
			function ctrlTd1(obj){
				var showHtml = "";
				if(obj.ISPQ=="0"){
					showHtml = "—";
				}else{
					showHtml = obj.PQ;
				}
				return showHtml;
			}
			function ctrlTd2(obj){
				var showHtml = "";
				if(obj.ISPQ=="0"){
					showHtml = "—";
				}else{
					showHtml = obj.SJSJ_PQ;
				}
				return showHtml;
			}
			function ctrlTd3(obj){
				var showHtml = "";
				if(obj.ISPQ=="0"){
					showHtml = "—";
				}else{
					showHtml = obj.CDYJ_PQ;
				}
				return showHtml;
			}
			//判断是否是项目
			function doBdmc(obj){
				  var bd_name=obj.BDMC;
				  if(bd_name==null||bd_name==""){
					  /* return '<div style="text-align:center"><abbr title=本条项目信息没有标段内容>—</abbr></div>'; */
					  return '<div style="text-align:center">—</div>';
				  }else{
					  return bd_name;			  
				  }
			}
			//判断是否是项目
			function doBdbh(obj){
				var bd_name=obj.BDBH;
				if(bd_name==null||bd_name==""){
					return '<div style="text-align:center">—</div>';
				}else{
					return bd_name;			  
				}
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
						<span class="pull-right">
							<button id="btnExp" class="btn" type="button">
								导出
							</button>
						</span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
						</table>
					</form>
					<div style="height: 5px;"></div>
					<div class="row-fluid">
						<div class="overFlowX">
							<table class="table-hover table-activeTd B-table" id="tabList"
								width="100%" type="single" pageNum="10">
								<thead>
									<tr>
										<th name="XH" id="_XH">
											&nbsp;#&nbsp;
										</th>
										<th fieldname="XMBH" tdalign="center" CustomFunction="doRandering">
											&nbsp;&nbsp;
										</th>
										<th fieldname="XMBH" maxlength="15" rowMerge="true" tdalign="left" hasLink="true" linkFunction="rowView">
											&nbsp;项目编号&nbsp;
										</th>
										<th fieldname="XMMC" maxlength="15" rowMerge="true" tdalign="left">
											&nbsp;项目名称&nbsp;
										</th>
										<th fieldname="BDBH" rowspan="2" colindex=4 
											maxlength="10" Customfunction="doBdbh">
											&nbsp;标段编号&nbsp;
										</th>
										<th fieldname="BDMC" maxlength="15" Customfunction="doBdmc">
											&nbsp;标段名称&nbsp;
										</th>
										<th fieldname="PQT" type="date" tdalign="center">
											&nbsp;排迁图&nbsp;
										</th>
										<th fieldname="PQ" type="date" tdalign="center" CustomFunction="ctrlTd1">
											&nbsp;计划完成时间&nbsp;
										</th>
										<th fieldname="SJSJ_PQ" type="date" tdalign="center"CustomFunction="ctrlTd2">
											&nbsp;实际完成时间&nbsp;
										</th>
										<th fieldname="CDYJ_PQ" type="date" tdalign="center"CustomFunction="ctrlTd3">
											&nbsp;场地移交时间&nbsp;
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

		<div align="center">
				<FORM name="frmPost" method="post" style="display: none"
					target="_blank">
					<!--系统保留定义区域-->
					<input type="hidden" name="queryXML" id="queryXML">
					<input type="hidden" name="txtXML" id="txtXML">
					<input type="hidden" name="resultXML" id="resultXML">
					<input type="hidden" name="txtFilter" order="asc"  fieldname ="X.xmbh, X.Xmbs, X.pxh,X.BDBH">
					<input type="hidden" name="queryResult" id = "queryResult">
					<input type="hidden" name="rowJsonXML" id="rowJsonXML">
					<!--传递行数据用的隐藏域-->
					<input type="hidden" name="rowData">
				</FORM>
			</div>
	</body>
</html>