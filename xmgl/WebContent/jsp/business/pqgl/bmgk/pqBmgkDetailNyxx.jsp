<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>排迁内页信息</title>
		<%	String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
		%>
		<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
		<script type="text/javascript" charset="utf-8">
			//请求路径，对应后台RequestMapping
			var controllername= "${pageContext.request.contextPath }/sjzq/pqSjzqController.do";
			g_bAlertWhenNoResult = false;
			var g_nd = '<%=nd%>';
			var g_proKey = '<%=proKey%>';
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
			});
			//页面默认参数
			function doInit(){
				doSearch()
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
			//详细信息
			function rowView(index){
				var obj = $("#DT1").getSelectedRowJsonByIndex(index);
				var id = convertJson.string2json1(obj).XMID;
				$(window).manhuaDialog(xmscUrl(id));
			}
			function tr_click(obj){
				
			}
			//回调函数
			getWinData = function(data){
				var index =	$("#DT1").getSelectedRowIndex();
				var rowValue = $("#DT1").getSelectedRow();
 				var data1 = defaultJson.packSaveJson(data);
	 			var tempJson = eval("("+rowValue+")");//字符串转JSON对象
				defaultJson.doUpdateJson(controllername + "?doYnqk",data1,DT1,null);
			};
			function getXmmcQueryCondition(){
				var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":"S.pxh"}]}';
				var jsonData = convertJson.string2json1(initData); 
				//年度
				if("" != $("#QueryND").val()){
					var defineCondition = {"value": $("#QueryND").val(),"fieldname":"S.ND","operation":"=","logic":"and"};
					jsonData.querycondition.conditions.push(defineCondition);
				}
				//项目名称
				if("" != $("#QXMMC").val()){
					var defineCondition = {"value": "%"+$("#QXMMC").val()+"%","fieldname":"S.XMMC","operation":"like","logic":"and"};
					jsonData.querycondition.conditions.push(defineCondition);
				}
				return JSON.stringify(jsonData);
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
					<h4 class="title" id="zhxxTitle">
						<span class="pull-right">
							<button id="btnExp" class="btn" type="button">
								导出
							</button>
						</span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" style="width:100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<input class="span12" type="text" placeholder="" name="JHID" keep="true"
										fieldname="Z.JHSJID" id="q_jhsjid" operation="=">
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
						</table>
					</form>
					<div style="height:5px;"> </div>
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
									<th fieldname="XMMC" rowspan="2" colindex=3 tdalign="left" rowMerge="true"
										style="width: 20%" maxlength="15">
										&nbsp;项目名称&nbsp;
									</th>
									<th fieldname="BDBH" rowspan="2" colindex=4 style="width: 20%" 
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
									<th fieldname="GXLB" rowspan="2" colindex=7 tdalign ="center">
										&nbsp;管线类别&nbsp;
									</th>
									<th  fieldname="ZXMMC" rowspan="2" colindex=8 maxlength="15">
										&nbsp;排迁任务名称&nbsp;
									</th>
									<th  fieldname="XMFZR" rowspan="2" colindex=9>
										&nbsp;项目负责人&nbsp;
									</th>
									<th  fieldname="PXH" rowspan="2" colindex=10>
										&nbsp;归档号&nbsp;
									</th>
									<th  fieldname="PQDD" rowspan="2" colindex=11 maxlength="15">
										&nbsp;排迁地点&nbsp;
									</th>
									<th colspan=4>
										&nbsp;内业情况&nbsp;
									</th>
									<th colspan=7 tdalign ="center">
										&nbsp;合同情况&nbsp;
									</th>
								</tr>
								<tr>
									<th fieldname="WTH" colindex=12>
										&nbsp;委托函&nbsp;
									</th>
									<th fieldname="YSSSD" colindex=13>
										&nbsp;预算送审单&nbsp;
									</th>
									<th fieldname="GCYSSDB" colindex=14>
										&nbsp;预算审定表&nbsp;
									</th>
									<th fieldname="SSZ" colindex=15 tdalign ="right">
										&nbsp;工程造价（元）&nbsp;
									</th>
									<th fieldname="HTBM" colindex=16>
										&nbsp;合同编号&nbsp;
									</th>
									<th fieldname="HTSX" colindex=17 tdalign ="center">
										&nbsp;合同属性&nbsp;
									</th>
									<th fieldname="HTQDJ" colindex=18 tdalign ="right">
										&nbsp;合同签订价（元）&nbsp;
									</th>
									<th fieldname="SDZ" colindex=19 tdalign ="right">
										&nbsp;合同结算价（元）&nbsp;
									</th>
									<th fieldname="HTZF" colindex=20 tdalign ="right">
										&nbsp;已拨付合同款（元）&nbsp;
									</th>
									<th fieldname="WFHTK" colindex=21 tdalign ="right" CustomFunction="showWfhtk">
										&nbsp;未付合同款（元）&nbsp;
									</th>
									<th fieldname="HTJQDRQ" colindex=22 tdalign ="center">
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