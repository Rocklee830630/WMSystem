<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>进展剩余情况</title>
		<script type="text/javascript" charset="utf-8">
			g_bAlertWhenNoResult = false;
			var controllername= "${pageContext.request.contextPath }/pqgl/pqglController.do";
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
				$(".slider").on("click",function(){
					//$("#sliderZone").slideToggle("slow");
				});
			});
			function doInit(){
				//获取父页面iframe中隐藏域的值
				var rowValue = $(parent.frames["menuiframe"].document).find("#resultXML").val();
				//字符串转JSON对象
				var tempJson = eval("("+rowValue+")");
				$("#q_zxmid").val(tempJson.ZXMID);
				$("#insertForm").setFormValues(tempJson);
				$("#insertForm").clearFormResult();
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,tabList);
				defaultJson.doQueryJsonList(controllername+"?queryJzsy",data,tabList,null,true);
				$("#showForm").setFormValues(tempJson);
				setReadonly("showForm",true);
				//$("#sliderZone").hide();
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
					$("#insertForm").setFormValues(tempJson);
				}
			}
		</script>
	</head>
	<body>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<form method="post" id="queryForm" style="display: none;">
						<table class="B-table">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
							<tr>
								<th class="right-border bottom-border text-right">
									子项目ID
								</th>
								<td class="right-border bottom-border">
									<input class="span12" type="text" placeholder="" name="ZXMID"
										fieldname="ZXMID" operation="=" id="q_zxmid">
								</td>
							</tr>
						</table>
					</form>
					<div>
						<table class="table-hover table-activeTd B-table" id="tabList"
							width="100%" type="single" pageNum="5">
							<thead>
								<tr>
									<th name="XH" id="_XH" style="width: 10px">
										#
									</th>
									<th fieldname="JZRQ" type="date" tdalign="center">
										截止日期
									</th>
									<th fieldname="JZQK" maxlength="15">
										进展情况
									</th>
									<th fieldname="SYGZL" maxlength="15">
										剩余工作量
									</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<div class="B-small-from-table-autoConcise" style="width:100%">
						<h4 class="title">
							
							<span class="pull-right">
								<button id="btnSave" class="btn" type="button">
									保存
								</button>
								<button id="btnClear" class="btn">
									清空
								</button>
							</span>
						</h4>
						<p class="slider"><i class="icon-file"></i>排迁任务信息</p>
						<div class="B-small-from-table-autoConcise" id="sliderZone">
							<jsp:include page="/jsp/business/pqgl/pqxmgl/pqxmViewForm.html" flush="true"/>
						</div>

						<form method="post" action="" id="insertForm">
							<table class="B-table" width="100%">
								<!-- 这里需要一个隐藏域，存放比如：问题编号,接收人账号，接收单位等信息 -->
								<tr style="display: none;">
									<th width="8%" class="right-border bottom-border">
										子项目ID
									</th>
									<td width="42%" class="right-border bottom-border">
										<input class="span12" type="text" name="ZXMID"
											fieldname="ZXMID" id="zxmid" keep="true">
									</td>
									<th width="8%" class="right-border bottom-border">
										主键
									</th>
									<td width="42%" class="right-border bottom-border">
										<input class="span12" type="text" name="GC_PQ_JZ_ID"
											fieldname="GC_PQ_JZ_ID">
									</td>
								</tr>
								<tr>
									<th width="8%" class="right-border bottom-border">
										截止日期
									</th>
									<td width="42%" class="bottom-border">
										<input class="span5" type="date" name="JZRQ" id="xmmc" 
											fieldname="JZRQ" placeholder="必填" check-type="required">
									</td>
									<th width="8%" class="right-border bottom-border">

									</th>
									<td width="42%" class="bottom-border">

									</td>
								</tr>
								<tr>
									<th width="8%" class="right-border bottom-border">
										进展情况
									</th>
									<td width="42%"  class="bottom-border" >
										<textarea class="span12" rows="3" name="JZQK"
											check-type="maxlength" maxlength="1000" fieldname="JZQK"></textarea>
									</td>
									<th width="8%" class="right-border bottom-border">
										剩余工作量
									</th>
									<td width="42%"  class="bottom-border">
										<textarea class="span12" rows="3" name="SYGZL"
											check-type="maxlength" maxlength="1000" fieldname="SYGZL"></textarea>
									</td>
								</tr>
							</table>
						</form>
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
					<!--传递行数据用的隐藏域-->
					<input type="hidden" name="rowData">
				</FORM>
			</div>
	</body>
</html>
