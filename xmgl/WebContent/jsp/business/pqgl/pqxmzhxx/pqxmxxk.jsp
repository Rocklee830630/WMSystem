<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<% String jhsjid = request.getParameter("jhsjid");%>
		<app:base />
		<title>排迁任务反馈</title>
		<script type="text/javascript" charset="utf-8">
			g_bAlertWhenNoResult = false;
			var controllername= "${pageContext.request.contextPath }/pqgl/pqglController.do";
			var p_jhsjid='<%=jhsjid%>';
			$(function(){
				doInit();
				$("#btnSave").click(function(){
					if($("#insertForm").validationButton()){
						var index =	$("#tabList").getSelectedRowIndex();
		 				//生成json串
		 		 		var data = Form2Json.formToJSON(insertForm);
 						var data1 = defaultJson.packSaveJson(data);
 						if(index!=-1){
 							defaultJson.doUpdateJson(controllername + "?updateJzsy", data1,tabList,null);
 						}else{
 							defaultJson.doInsertJson(controllername + "?insertJzsy", data1,tabList,null);
							$("#insertForm").clearFormResult(); 
 						}
 						$(window).manhuaDialog.setData(data);
						$(window).manhuaDialog.sendData();
					}
				});
				$("#btnClear").click(function(){
					$("#insertForm").clearFormResult(); 
				});
				$("#btnCancel").click(function(){
					$(window).manhuaDialog.close();
				});
				//按钮绑定事件（导出）
				$("#btnExp").click(function() {
					if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
						printTabList("DT1");
					}
				});
				$("#btnPrint").click(function(){
					$(this).hide();
					window.print();
					$(this).show();
				});
			});
			function doInit(){
				var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#rowJsonXML").val();
				//字符串转JSON对象
				var rowValueJson = convertJson.string2json1(rowValue);
				$("#showForm").setFormValues(rowValueJson);
				if(p_jhsjid!=""){
					$("#q_jhsjid").val(p_jhsjid);
				}else{
					$("#q_jhsjid").val("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");//这是防止计划数据ID出现空值时，给出一个假的ID，防止后台获取不到查询条件
				}
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				defaultJson.doQueryJsonList(controllername+"?queryPqInfo",data,DT1);
				defaultJson.doQueryJsonList(controllername+"?queryPqInfo",data,DT2);
				var tempJson = $("#queryResult").val();
				if(tempJson!=""){
					$("#showForm").setFormValues(convertJson.string2json1(tempJson).response.data[0]);
				}else{
					//xAlertMsg("本项目下暂时无排迁任务！");
				}
				setReadonly("showForm",true);
			}
			//---------------------------------
			//-表格可用性控制
			//---------------------------------
			function setReadonly(o,flag){
				var obj = $("#"+o);
				$('input,select,textarea',obj).each(function () {
					var el = $(this);
					el.attr("disabled",flag);
				});
			}
			//单击行事件
			function tr_click(obj,tabId){
				if(tabId=="tabList"){
					var rowValue = $("#"+tabId).getSelectedRow();//获得选中行的json 字符串
					var tempJson = eval("("+rowValue+")");//字符串转JSON对象
					//$("#insertForm").setFormValues(tempJson);
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
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<form method="post" id="queryForm" style="display: none;">
						<table class="B-table">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<th width="5%" class="right-border bottom-border text-right">
									计划数据ID
								</th>
								<td class="right-border bottom-border" width="8%">
									<input class="span12" type="text" name="JHSJID" fieldname="Z.JHSJID" id="q_jhsjid"
										operation="=">
								</TD>
							</TR>
						</table>
					</form>
					
					<div class="B-small-from-table-autoConcise" style="width:100%">
						<h4 class="title">
							统筹计划信息
							<span class="pull-right">
								<button id="btnPrint" class="btn btn-link" type="button" style="display:none;">
									<i class="icon-print"></i>打印
								</button>
							</span>
						</h4>
						<form method="post" action="" id="showForm">
							<table class="B-table" width="100%">
								<!-- 这里需要一个隐藏域，存放比如：问题编号,接收人账号，接收单位等信息 -->
								<tr style="display: none;">
									<th width="8%" class="right-border bottom-border">
										子项目ID
									<br></th>
									<td width="42%" class="right-border bottom-border">
										<input class="span12" type="text" name="ZXMID"
											fieldname="ZXMID" id="zxmid" keep="true">
									<br></td>
									<th width="8%" class="right-border bottom-border">
										主键
									<br></th>
									<td width="42%" class="right-border bottom-border"><input class="span12" type="text" name="GC_PQ_JZ_ID"
											fieldname="GC_PQ_JZ_ID">
									<br></td>
								</tr>
								<tr>
									<th width="8%" class="right-border bottom-border disabledTh ">
										项目名称
									</th>
									<td width="42%" class="right-border bottom-border " colspan=3>
										<input class="span12" type="text" name="XMMC" 
											 fieldname="XMMC" 
											check-type="required maxlength">
									</td>
									<th width="8%" class="right-border bottom-border disabledTh">
										标段名称
									</th>
									<td width="42%" class="right-border bottom-border" colspan=3>
										<input class="span12" type="text" name="BDMC" 
											fieldname="BDMC">
									</td>
								</tr>
								<tr>
									<th width="8%" class="right-border bottom-border disabledTh">
										标段地点
									</th>
									<td class="right-border bottom-border" colspan=3>
										<input class="span12" type="text" name="BDDD" fieldname="BDDD" >
									</td>
									<th width="8%" class="right-border bottom-border disabledTh">
										统筹计划时间
									</th>
									<td class="right-border bottom-border"  colspan=3>
										<input class="span12" type="date" name="PQ" 
											fieldname="PQ">
									</td>
								</tr>
							</table>
						</form>
					</div>
					<div>
						<h4 class="title">
							排迁任务信息
						</h4>
					</div>
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="DT1"
							width="100%" type="single" pageNum="1000" noPage="true">
							<thead>
								<tr>
									<th name="XH" id="_XH" colindex=1
										style="width: 10px">
										&nbsp;#&nbsp;
									</th>
									<th  fieldname="ZXMMC" colindex=2>
										&nbsp;排迁任务名称&nbsp;
									</th>
									<th fieldname="GXLB" colindex=3 tdalign ="center">
										&nbsp;管线类别&nbsp;
									</th>
									<th  fieldname="XMFZR" colindex=4>
										&nbsp;项目负责人&nbsp;
									</th>
									<th  fieldname="PQDD" colindex=5 maxlength="15">
										&nbsp;排迁地点&nbsp;
									</th>
									<th fieldname="PQFA" colindex=6 maxlength="15">
										&nbsp;排迁方案&nbsp;
									</th>
									<th fieldname="KGSJ_JH" colindex=7 type="date" tdalign="center">
										&nbsp;计划开工时间&nbsp;
									</th>
									<th fieldname="WCSJ_JH" colindex=8 type="date" tdalign="center">
										&nbsp;计划完工时间&nbsp;
									</th>
									<th fieldname="KGSJ" colindex=9 type="date" tdalign="center">
										&nbsp;实际开工时间&nbsp;
									</th>
									<th fieldname="WCSJ" colindex=10 type="date" tdalign="center">
										&nbsp;实际完工时间&nbsp;
									</th>
									<th fieldname="JZQK" colindex=11 maxlength="15">
										&nbsp;进展情况&nbsp;
									</th>
									<th fieldname="SYGZL" colindex=12 maxlength="15">
										&nbsp;剩余工作量&nbsp;
									</th>
									<th fieldname="CZWT" colindex=13 maxlength="15">
										&nbsp;存在问题&nbsp;
									</th>
									<th fieldname="JJFA" colindex=14 maxlength="15">
										&nbsp;解决方案&nbsp;
									</th>
									<th fieldname="ZYGXPQT" colindex=15>
										&nbsp;专业管线排迁图&nbsp;
									</th>
									<th fieldname="PQLLD" colindex=16>
										&nbsp;排迁联络单&nbsp;
									</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<div>
						<h4 class="title">
							排迁内业信息
						</h4>
					</div>
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="DT2"
							width="100%" type="single" noPage="true" pageNum="1000">
							<thead>
								<tr>
									<th name="XH" id="_XH" rowspan="2" colindex=1
										style="width: 10px">
										&nbsp;#&nbsp;
									</th>
									<th  fieldname="ZXMMC" rowspan="2" colindex=2>
										&nbsp;排迁任务名称&nbsp;
									</th>
									<th fieldname="GXLB" rowspan="2" colindex=3 tdalign ="center">
										&nbsp;管线类别&nbsp;
									</th>
									<th colspan=5>
										&nbsp;内业情况&nbsp;
									</th>
									<th colspan=7 colindex=9 tdalign ="center">
										&nbsp;合同情况&nbsp;
									</th>
								</tr>
								<tr>
									<th fieldname="YSSSD" colindex=4>
										&nbsp;预算送审单&nbsp;
									</th>
									<th fieldname="GCYSSDB" colindex=5>
										&nbsp;工程预算审定表&nbsp;
									</th>
									<th fieldname="WTH" colindex=6>
										&nbsp;委托函&nbsp;
									</th>
									<th fieldname="SSZ" colindex=7 tdalign ="right">
										&nbsp;工程造价（元）&nbsp;
									</th>
									<th fieldname="SDZ" colindex=8 tdalign ="right">
										&nbsp;审定值（元）&nbsp;
									</th>
									<th fieldname="HTBM" colindex=10>
										&nbsp;合同编号&nbsp;
									</th>
									<th fieldname="HTSX" colindex=11 tdalign ="center">
										&nbsp;合同属性&nbsp;
									</th>
									<th fieldname="HTQDJ" colindex=12 tdalign ="right">
										&nbsp;合同签订价（元）&nbsp;
									</th>
									<th fieldname="SDZ" colindex=13 tdalign ="right">
										&nbsp;合同结算价（元）&nbsp;
									</th>
									<th fieldname="HTZF" colindex=14 tdalign ="right">
										&nbsp;已拨付合同款（元）&nbsp;
									</th>
									<th fieldname="WFHTK" colindex=15 tdalign ="right" CustomFunction="showWfhtk">
										&nbsp;未付合同款（元）&nbsp;
									</th>
									<th fieldname="HTJQDRQ" colindex=16 tdalign ="center">
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
			<div align="center">
				<FORM name="frmPost" method="post" style="display: none"
					target="_blank">
					<!--系统保留定义区域-->
					<input type="hidden" name="queryXML" id="queryXML">
					<input type="hidden" name="txtXML" id="txtXML">
					<input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ"
						id="txtFilter">
					<input type="hidden" name="resultXML" id="resultXML">
					<input type="hidden" name="queryResult" id="queryResult">
					<!--传递行数据用的隐藏域-->
					<input type="hidden" name="rowData">
				</FORM>
			</div>
	</body>
</html>
