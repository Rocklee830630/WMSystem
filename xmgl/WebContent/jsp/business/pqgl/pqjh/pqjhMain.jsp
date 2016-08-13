<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>排迁计划</title>
		<style type="text/css"> 
		i.showXmxxkInfo:hover,i.showXmxxkInfo:focus {
			cursor: pointer;
		}
		</style>
		<script type="text/javascript" charset="utf-8">
			//请求路径，对应后台RequestMapping
			var controllername= "${pageContext.request.contextPath }/pqgl/pqglController.do";
			g_bAlertWhenNoResult = false;
			var btnNum = 0;
			
			
			//计算本页表格分页数
			function setPageHeight(){
				var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
				var pageNum = parseInt(height/pageTableOne,10);
				$("#tabList").attr("pageNum",pageNum);
			}
			
			//页面初始化
			$(function() {
				setPageHeight();
				initCommonQueyPage();
				init();
				//按钮绑定事件（查询）
				$("#btnQuery").click(function() {
					//生成json串
					//var data = combineQuery.getQueryCombineData(queryForm,frmPost,tabList);
					var data = combineQuery.getQueryCombineData(queryForm,frmPost,tabList);
					defaultJson.doQueryJsonList(controllername+"?queryProjectInfo",data,tabList,null,false);
				});
				//按钮绑定事件(计划反馈)
				$("#btnJhfk").click(function() {
					if($("#tabList").getSelectedRowIndex()==-1){
						requireSelectedOneRow();
						return;
					}else{
						btnNum=0;
						$("#resultXML").val($("#tabList").getSelectedRow());
						$(window).manhuaDialog({"title":"排迁综合管理>场地移交设置","type":"text","content":"${pageContext.request.contextPath}/jsp/business/pqgl/pqjh/jhfk.jsp","modal":"3"});
					}
				});
				//按钮绑定事件（子项目管理）
				$("#btnZxmgl").click(function() {
					if($("#tabList").getSelectedRowIndex()==-1){
						requireSelectedOneRow();
						return;
					}else{
						btnNum=1;
						$("#resultXML").val($("#tabList").getSelectedRow());
						$(window).manhuaDialog({"title":"排迁综合管理>新增子项目","type":"text","content":"${pageContext.request.contextPath}/jsp/business/pqgl/pqjh/zxmgl.jsp","modal":"1"});
					}
				});
				//按钮绑定事件(子项目查看)
				$("#btnZxmck").click(function() {
					var defQuery = "0";//这个用来控制默认的查询方式，0表示无条件，1按条件查询
					if($("#tabList").getSelectedRowIndex()==-1){
						defQuery = "0";
					}else{
						defQuery = "1";
						$("#resultXML").val($("#tabList").getSelectedRow());
					}
					$(window).manhuaDialog({"title":"排迁综合管理>排迁任务信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/pqgl/pqxmzhxx/pqxmzhxxMain.jsp?defQuery="+defQuery,"modal":"1"});
				});
				//按钮绑定事件（清空查询条件）
				$("#btnClear").click(function() {
					$("#queryForm").clearFormResult();
        			initCommonQueyPage();
				});
				//按钮绑定事件（导出）
				$("#btnExp").click(function() {
					if(exportRequireQuery($("#tabList"))){//该方法需传入表格的jquery对象
						printTabList("tabList");
					}
				});
				//按钮绑定事件（计划反馈）
				$("#btnFeedback").click(function(){
					if($("#tabList").getSelectedRowIndex()==-1){
						requireSelectedOneRow();
						return;
					}else{
						$("#resultXML").val($("#tabList").getSelectedRow());
						var tempJson = $("#tabList").getSelectedRowJsonObj();
						openJhfkPage(tempJson.GC_JH_SJ_ID,"1001","queryForm","tabList",encodeURI(controllername+"?queryProjectInfo"));
					}
				});
			});
			function openXmxxkInfo(obj){
			    var index = $(event.target).closest("tr").index();
				$("#tabList").cancleSelected();
		    	$("#tabList").setSelect(index);
				if($("#tabList").getSelectedRowIndex()==-1){
					requireSelectedOneRow();
					return;
				}else{
					var n = $(obj).attr("jhsjid");
					$("#resultXML").val($("#tabList").getSelectedRow());
					$("#rowJsonXML").val($("#tabList").getSelectedRow());
					$(window).manhuaDialog({"title":"排迁任务信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/pqgl/pqxmzhxx/pqxmxxk.jsp?jhsjid="+n,"modal":"1"});
		    	}
			}
			//页面默认参数
			function init(){
				$("#btnZxmgl").hide();
				//var data = combineQuery.getQueryCombineData(queryForm,frmPost,tabList);
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,tabList);
				defaultJson.doQueryJsonList(controllername+"?queryProjectInfo",data,tabList,null,true);
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
				showHtml = "<a href='javascript:void(0);' title='排迁任务信息卡' onclick='openXmxxkInfo(this);' jhsjid='"+obj.GC_JH_SJ_ID+"'><i class='icon-file showXmxxkInfo' jhsjid='"+obj.GC_JH_SJ_ID+"'></i></a>";
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
						排迁综合管理
						<span class="pull-right">
						<app:oPerm url="/jsp/business/pqgl/pqjh/jhfk.jsp">
							<button id="btnFeedback" class="btn" type="button">
								统筹计划反馈
							</button>
						</app:oPerm>
						<app:oPerm url="/jsp/business/pqgl/pqjh/cdyjsz">
							<button id="btnJhfk" class="btn" type="button">
								场地移交设置
							</button>
						</app:oPerm>
						<app:oPerm url="/jsp/business/pqgl/pqjh/zxmgl.jsp">	
							<button id="btnZxmgl" class="btn" type="button">
								新增子项目
							</button>
							</app:oPerm>
						<app:oPerm url="/jsp/business/pqgl/pqxmzhxx/pqxmzhxxMain.jsp">
							<button id="btnZxmck" class="btn" type="button">
								排迁综合信息
							</button>
						</app:oPerm>
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
							<tr>
								<jsp:include page="/jsp/business/common/commonQuery.jsp"
									flush="true">
									<jsp:param name="prefix" value="X" />
								</jsp:include>
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
					<div style="height: 5px;"></div>
					<div class="row-fluid">
						<div class="overFlowX">
							<table class="table-hover table-activeTd B-table" id="tabList"
								width="100%" type="single" pageNum="10" printFileName="排迁综合管理">
								<thead>
									<tr>
										<th name="XH" id="_XH">
											&nbsp;#&nbsp;
										</th>
										<th fieldname="XMBH" tdalign="center" CustomFunction="doRandering">
											&nbsp;&nbsp;
										</th>
										<th fieldname="XMBH" rowMerge="true" tdalign="left" hasLink="true" linkFunction="rowView">
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
										<th fieldname="XMBDDZ" maxlength="15">
											&nbsp;项目地址&nbsp;
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
					<input type="hidden" name="resultXML" id="rowJsonXML">
					<input type="hidden" name="txtFilter" order="asc"  fieldname ="X.xmbh, X.Xmbs, X.pxh,X.BDBH">
					<input type="hidden" name="queryResult" id = "queryResult">
					<!--传递行数据用的隐藏域-->
					<input type="hidden" name="rowData">
				</FORM>
			</div>
	</body>
</html>