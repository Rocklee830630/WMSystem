<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>排迁任务管理信息</title>
		<%	String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
		%>
		<style>
		.myCellSpan{
			background-color:#FF8888 ;
			width:100%;
			height:100%;
			display:inline-block;
			margin:0;
			padding:0;
		}
		</style>
		<script type="text/javascript" charset="utf-8">
			//请求路径，对应后台RequestMapping
			var controllername= "${pageContext.request.contextPath }/bmgk/xmcbkXxBmgkController.do";
			g_bAlertWhenNoResult = false;
			var g_nd = '<%=nd%>';
			var g_proKey = '<%=proKey%>';
			var btnNum = 0;
			var objRow;
			
			//计算本页表格分页数
			function setPageHeight(){
				var getHeight=getDivStyleHeight();
				var height = getHeight-pageTopHeight-pageTitle-getTableTh(2)-pageNumHeight;
				var pageNum = parseInt(height/pageTableOne,10);
				$("#DT1").attr("pageNum",pageNum);
			}
			
			//页面初始化
			$(function() {
				setPageHeight();
				doInit();
			    //按钮绑定事件（导出）
				$("#btnExp").click(function() {
					if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
						printTabList("DT1","pqrwgl.xls","XMBH,XMMC,BDMC,BDDD,GXLB,ZXMMC,PQDD,PQFA,ISJHWC,KGSJ_JH,KGSJ,WCSJ_JH,WCSJ,JZQK,SYGZL,CZWT,JJFA,HTZT,HTZT,HTZT,HTZT,HTZT,HTZT,HTZT,HTZT","2,1"); 
					}
				});
			});
			//页面默认参数
			function doInit(){
				doSearch();
			}
			function doSearch(){
				var condProKey = "";
				if(g_proKey!=null&&g_proKey!=""){
					condProKey = "&condiProSql="+g_proKey;
				}
				var condNd = "";
				if(g_nd!=null&&g_nd!=""){
					condNd = "&nd="+g_nd;
				}
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				defaultJson.doQueryJsonList(controllername+"?queryJcjkXx"+condNd+condProKey,data,DT1,null,false);
			}
			function tr_click(obj,tabId){
				var rowValue = $("#"+tabId).getSelectedRow();//获得选中行的json 字符串
				var tempJson = convertJson.string2json1(rowValue);//字符串转JSON对象
			}
			function getWinData(data){
				var index =	$("#DT1").getSelectedRowIndex();
				var rowValue = $("#DT1").getSelectedRow();
				var tempJson = convertJson.string2json1(rowValue);//字符串转JSON对象
				if(btnNum==0){
					//进展反馈
					var resJson = convertJson.string2json1(data);
					resJson.GC_PQ_ZXM_ID=resJson.ZXMID;
 					var data1 = defaultJson.packSaveJson(JSON.stringify(resJson));
 					defaultJson.doUpdateJson(controllername + "?queryPqzxmByPk", data1,DT1,null);
				}else if(btnNum == 1){
					//问题解决情况
					var resJson = eval("("+data+")");
					var data1 = defaultJson.packSaveJson(data);
					$.ajax({
						url:controllername + "?queryPqzxmByPk",
						data:data1,
						dataType:"json",
						async:false,
						success:function(result){
							var resultmsg = result.msg;
							var resultmsgobj = convertJson.string2json1(resultmsg);
							var resJson = resultmsgobj.response.data[0];
							tempJson["CZWT"]=resJson.CZWT;
							tempJson["JJFA"]=resJson.JJFA;
							$("#DT1").updateResult(JSON.stringify(tempJson),DT1,index);
							$("#DT1").setSelect(index);
						}
					});
				}else if(btnNum == 2){
					//defaultJson.updateBySrcTab(DT1,data);
					$("#btnQuery").click();
				}else if(btnNum == 3){
					var data1 = defaultJson.packSaveJson(data);
					defaultJson.doInsertJson(controllername + "?doZxmgl&ywid="+$("#ywid").val(),data1,DT1,null);
					$("#DT1").cancleSelected();
					$("#DT1").setSelect(0);
				}
			}
			//详细信息
			function rowView(index){
				var obj = $("#DT1").getSelectedRowJsonByIndex(index);
				var id = convertJson.string2json1(obj).XMID;
				$(window).manhuaDialog(xmscUrl(id));
			}
			function insertTable(data){
				var res = dealSpecialCharactor(data);
				var subresultmsgobj = defaultJson.dealResultJson(res);
				$("#DT1").insertResult(JSON.stringify(subresultmsgobj),DT1,1);
			}
			function doRandering(obj){
				var showHtml = "";
				if(obj.ISJHWC=="1"){
					showHtml = obj.ISJHWC_SV;
				}else if(obj.ISJHWC=="0"){
					showHtml = '<span class="myCellSpan" frame="box">'+obj.ISJHWC_SV+'</span>';
				}else{
					showHtml = '<span class="label"></span>';
				}
				return showHtml;
			}
			function setYwid(n){
				$("#ywid").val(n);
			}
			//判断是否是项目
			function doBdmc(obj){
				  var bd_name=obj.BDMC;
				  if(bd_name==null||bd_name==""){
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
			//子页面关闭时调用的函数
			function closeParentCloseFunction(val){
				if(btnNum==2){
					g_bAlertWhenNoResult = false;
					doSearch();
					g_bAlertWhenNoResult = true;
				}
			}
			//---------------------------------------
			//控制未付合同款的颜色，复数为红色，其他为黑色
			//---------------------------------------
			function showWfhtk(obj){
				var Reg = /^-\d*$/;// 匹配负数
				if(Reg.test(obj.WFHTK)){
					return "<span style='color:red;'>"+obj.WFHTK_SV+"</span>";
				}else{
					return obj.WFHTK_SV;
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
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single">
							<thead>
				              <tr>
								<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
								<th rowspan="2" fieldname="XMMC" colindex=2>&nbsp;项目名称&nbsp;</th>
								<th rowspan="2" fieldname="XMJSNRGM" colindex=3 maxlength="15">&nbsp;项目建设内容及规模&nbsp;</th>
								<th colspan="4" >&nbsp;计划总投资&nbsp;</th>
								<th rowspan="2" fieldname="XMXZ" colindex=8>&nbsp;项目建设阶段&nbsp;</th>
								<th colspan="5" >&nbsp;年度计划投资&nbsp;</th>
								<th rowspan="2" fieldname="ZRBM" colindex=14>&nbsp;责任部门&nbsp;</th>
								<th rowspan="2" fieldname="XMFR" colindex=15>&nbsp;项目法人&nbsp;</th>
				              </tr>
				              <tr>
								<th  fieldname="JH_XJ" colindex=4>&nbsp;小计&nbsp;</th>
								<th  fieldname="JH_GC" colindex=5>&nbsp;工程费用&nbsp;</th>
								<th  fieldname="JH_ZQ" colindex=6>&nbsp;征拆、排迁费用&nbsp;</th>
								<th  fieldname="JH_QT" colindex=7>&nbsp;其他费用&nbsp;</th>
								
								<th  fieldname="ND_XJ" colindex=9>&nbsp;小计&nbsp;</th>
								<th  fieldname="ND_GC" colindex=10>&nbsp;工程费用&nbsp;</th>
								<th  fieldname="ND_ZQ" colindex=11>&nbsp;征拆、排迁费用&nbsp;</th>
								<th  fieldname="ND_QT" colindex=12>&nbsp;其他费用&nbsp;</th>
								<th  fieldname="ND_JSMB" colindex=13 maxlength="15">&nbsp;年度建设目标&nbsp;</th>
				              </tr>
							</thead>
							<tbody>
							</tbody>
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
				<input type="hidden" name="ywid" id="ywid">
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="Z.XMID,Z.PXH" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id = "queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>