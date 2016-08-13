<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%@ page import="com.ccthanking.common.YwlxManager" %>
		<%  String ywlx = YwlxManager.GC_PQ; %>
		<app:base />
		<title>排迁计划</title>
		
		<script type="text/javascript" charset="utf-8">
			//请求路径，对应后台RequestMapping
			g_bAlertWhenNoResult = false;
			var ywlx = '<%=ywlx%>';
			var controllername= "${pageContext.request.contextPath }/pqgl/pqglController.do";
			$(function(){
				doInit();
				$("#btnSave").click(function(){
					if($("#insertForm").validationButton()){
		 				//生成json串
		 				if($("#pqid").val()==""){
		 					var guid = getGUID();
		 					$("#pqid").val(guid);
		 				}
		 		 		var data = Form2Json.formToJSON(insertForm);
						$(window).manhuaDialog.setData(data);
						$(window).manhuaDialog.sendData();
						xAlertMsg("保存成功");
						//$(window).manhuaDialog.close();
					}
				});
				$("#btnFk").click(function(){
					if($("#insertForm").validationButton()){
						var counts = getJhfkCounts();
						if(Number(counts)<1){
							var data = Form2Json.formToJSON(insertForm);
							var parentmain=$(window).manhuaDialog.getParentObj();
							parentmain.doJhfk(data);
							$(window).manhuaDialog.close();
						}else{
							xConfirm("提示信息","您已经对该计划进行过反馈了，再次反馈将覆盖之前的数据，是否继续？");
							$('#ConfirmYesButton').one("click",function(){  
				 		 		var data = Form2Json.formToJSON(insertForm);
								var parentmain=$(window).manhuaDialog.getParentObj();
								parentmain.doJhfk(data);
								$(window).manhuaDialog.close();
							});
						}
					}
				});
				$("#btnCancel").click(function(){
					$(window).manhuaDialog.close();
				});
			});
			function doInit(){
				//获取父页面iframe中隐藏域的值
				var rowValue = $(parent.frames["menuiframe"].document).find("#resultXML").val();
				$("#rowJsonXML").val(rowValue);
				//字符串转JSON对象
				var tempJson = convertJson.string2json1(rowValue);
				$("#insertForm").setFormValues(tempJson);
				$("#xmmc").attr("disabled",true);
				$("#bdmc").attr("disabled",true);
				$("#jhwcsj").attr("disabled",true);
				$("#jhsjid").val(tempJson.GC_JH_SJ_ID);
				$("#q_jhsjid").val(tempJson.GC_JH_SJ_ID);
			}
			function tr_click(){}
			/**
			*	获取计划反馈次数
			*/
			function getJhfkCounts(){
				var n = $("#jhsjid").val();
				var str = "";
				$.ajax({
					url:controllername + "?getJhfkCounts&jhsjid="+n+"&ywlx="+ywlx,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						str = result.msg;
					}
				});
				return str;
			}
			//--------------------------------
			//-获取GUID
			//--------------------------------
			function getGUID(){
				var str = "";
				$.ajax({
					url:controllername + "?getGUID",
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						str = result.msg;
					}
				});
				return str;
			}
			//进展情况
			function jzqk(){
				var id=$("#JHSJID").val();
			 	$(window).manhuaDialog({"title":"排迁>进展情况","type":"text","content":"${pageContext.request.contextPath}/jsp/business/pqgl/pqxmzhxx/pqxmxxk.jsp?jhsjid="+id,"modal":"2"});
			}
		</script>
	</head>
	<body>
	<app:dialogs/>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						排迁计划信息
						<span class="pull-right">
							<button id="btnSave" class="btn" type="button">
								保存
							</button>
						</span>
					</h4>
					<form method="post" action="" id="insertForm">
						<table class="B-table" width="100%">
							<!-- 这里需要一个隐藏域，存放比如：问题编号,接收人账号，接收单位等信息 -->
							<tr style="display: none;">
								<th width="8%" class="right-border bottom-border">
									排迁ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="GC_PQ_ID" fieldname="GC_PQ_ID" id="pqid">
								</td>
								<th width="8%" class="right-border bottom-border">
									计划ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="JHID" fieldname="JHID">
								</td>
								<th width="8%" class="right-border bottom-border">
									计划数据ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="JHSJID" fieldname="JHSJID" id="jhsjid">
								</td>
								<th width="8%" class="right-border bottom-border">
									年度
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="ND" fieldname="ND">
								</td>
								<th width="8%" class="right-border bottom-border">
									项目编号
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="XMID" fieldname="XMID">
								</td>
								<th width="8%" class="right-border bottom-border">
									标段编号
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="BDID" fieldname="BDID">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									场地移交日期
								</th>
								<td width="17%" class="bottom-border">
									<input type="date" class="span12 date" name="CDYJ_PQ" fieldname="CDYJ_PQ" id="cdyjrq">
								</td>
								<td  class="right-border bottom-border" colspan="2">
									<a href='javascript:void(0)' onclick="jzqk()"><i class="icon-list"></i>查看进展情况</a>
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
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="Z.XMID,Z.PXH" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
					<input type="hidden" name="rowJsonXML" id="rowJsonXML">
				<input type="hidden" name="queryResult" id = "queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>
