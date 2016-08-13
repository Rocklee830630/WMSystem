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
			var controllername= "${pageContext.request.contextPath }/sjzq/pqSjzqController.do";
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
					condProKey = "&proKey="+g_proKey;
				}
				var condNd = "";
				if(g_nd!=null&&g_nd!=""){
					condNd = "&nd="+g_nd;
				}
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				defaultJson.doQueryJsonList(controllername+"?queryDrillingDataZxm"+condNd+condProKey,data,DT1,null,false);
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
						<table class="table-hover table-activeTd B-table" id="DT1"
							width="100%" type="single">
							<thead>
								<tr>
									<th name="XH" id="_XH" rowspan="2" colindex=1>
										&nbsp;#&nbsp;
									</th>
									<th fieldname="XMBH" maxlength="15" rowspan="2" colindex=2 rowMerge="true" tdalign="left" hasLink="true" linkFunction="rowView" id="t_xmbh">
										&nbsp;项目编号&nbsp;
									</th>
									<th fieldname="XMMC" rowspan="2" colindex=3 rowMerge="true" tdalign="left"
										style="width: 20%" maxlength="15">
										&nbsp;项目名称&nbsp;
									</th>
									<th fieldname="BDBH" rowspan="2" colindex=4 
										maxlength="10" Customfunction="doBdbh">
										&nbsp;标段编号&nbsp;
									</th>
									<th fieldname="BDMC" rowspan="2" colindex=5 style="width: 20%"
										maxlength="10" Customfunction="doBdmc">
										&nbsp;标段名称&nbsp;
									</th>
									<th fieldname="BDDD" rowspan="2" colindex=6
										style="width: 300px" maxlength="10">
										&nbsp;标段地点&nbsp;
									</th>
									<th fieldname="GXLB" rowspan="2" colindex=7 tdalign="center">
										&nbsp;管线类别&nbsp;
									</th>
									<th  fieldname="ZXMMC" rowspan="2" colindex=8 maxlength="15">
										&nbsp;排迁任务名称&nbsp;
									</th>
									<th  fieldname="XMFZR" rowspan="2" colindex=9>
										&nbsp;项目负责人&nbsp;
									</th>
									<th  fieldname="PQDD" rowspan="2" colindex=10 maxlength="15">
										&nbsp;排迁地点&nbsp;
									</th>
									<th fieldname="PQFA" rowspan="2" colindex=11 maxlength="15">
										&nbsp;排迁方案&nbsp;
									</th>
									<th colspan=2>
										&nbsp;开工时间&nbsp;
									</th>
									<th colspan=2>
										&nbsp;完工时间&nbsp;
									</th>
									<th fieldname="JZQK" rowspan="2" colindex=16 maxlength="15">
										&nbsp;进展情况&nbsp;
									</th>
									<th fieldname="SYGZL" rowspan="2" colindex=17 maxlength="15">
										&nbsp;剩余工作量&nbsp;
									</th>
									<th fieldname="CZWT" rowspan="2" colindex=18 maxlength="15">
										&nbsp;存在问题&nbsp;
									</th>
									<th fieldname="JJFA" rowspan="2" colindex=19 maxlength="15">
										&nbsp;解决方案&nbsp;
									</th>
									<th fieldname="ISJHWC" rowspan="2" colindex=20 tdalign="center">
										&nbsp;是否能按&nbsp;<br/>&nbsp;计划完成&nbsp;
									</th>
									<th fieldname="ZYGXPQT" rowspan="2" colindex=21>
										&nbsp;专业管线排迁图&nbsp;
									</th>
									<th fieldname="PQLLD" rowspan="2" colindex=22>
										&nbsp;排迁联络单&nbsp;
									</th>
									<th colspan="3">
										&nbsp;内业情况&nbsp;
									</th>
									<th colspan=7 tdalign ="center">
										&nbsp;合同情况&nbsp;
									</th>
								</tr>
								<tr>
									<th fieldname="KGSJ_JH" colindex=12 type="date" tdalign="center">
										&nbsp;计划&nbsp;
									</th>
									<th fieldname="KGSJ" colindex=13 type="date" tdalign="center">
										&nbsp;实际&nbsp;
									</th>
									<th fieldname="WCSJ_JH" colindex=14 type="date" tdalign="center">
										&nbsp;计划&nbsp;
									</th>
									<th fieldname="WCSJ" colindex=15 type="date" tdalign="center">
										&nbsp;实际&nbsp;
									</th>
									<th fieldname="YSSSD" colindex=23>
										&nbsp;预算送审单&nbsp;
									</th>
									<th fieldname="GCYSSDB" colindex=24>
										&nbsp;工程预算审定表&nbsp;
									</th>
									<th fieldname="WTH" colindex=25>
										&nbsp;委托函&nbsp;
									</th>
									<th fieldname="HTBM" colindex=26>
										&nbsp;合同编号&nbsp;
									</th>
									<th fieldname="HTSX" colindex=27 tdalign ="center">
										&nbsp;合同属性&nbsp;
									</th>
									<th fieldname="HTQDJ" colindex=28 tdalign ="right">
										&nbsp;合同签订价（元）&nbsp;
									</th>
									<th fieldname="SDZ" colindex=29 tdalign ="right">
										&nbsp;合同结算价（元）&nbsp;
									</th>
									<th fieldname="HTZF" colindex=30 tdalign ="right">
										&nbsp;已拨付合同款（元）&nbsp;
									</th>
									<th fieldname="WFHTK" colindex=31 tdalign ="right" CustomFunction="showWfhtk">
										&nbsp;未付合同款（元）&nbsp;
									</th>
									<th fieldname="HTJQDRQ" colindex=32 tdalign ="center">
										&nbsp;签订日期&nbsp;
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