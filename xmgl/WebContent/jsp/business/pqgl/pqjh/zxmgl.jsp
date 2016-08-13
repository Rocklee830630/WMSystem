<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>子项目管理</title>
		
		<script type="text/javascript" charset="utf-8">
			$(function(){
				doInit();
				$("#btnSave").click(function(){
					if($("#insertForm").validationButton()){
		 				//生成json串
		 		 		var data = Form2Json.formToJSON(insertForm);
						$(window).manhuaDialog.setData(data);
						$(window).manhuaDialog.sendData();
						$(window).manhuaDialog.close();
					}
				});
				$("#btnCancel").click(function(){
					$(window).manhuaDialog.close();
				});
			});
			function doInit(){
				//获取父页面iframe中隐藏域的值
				var rowValue = $(parent.frames["menuiframe"].document).find("#resultXML").val();
				//字符串转JSON对象
				var tempJson = eval("("+rowValue+")");
				$("#insertForm").setFormValues(tempJson);
				$("#xmmc").attr("disabled",true);
				$("#bdmc").attr("disabled",true);
				$("#jhwcsj").attr("disabled",true);
				$("#jhid").val(tempJson.JHID);
				$("#jhsjid").val(tempJson.GC_JH_SJ_ID);
				$("#bz").val("");
			}
			
		</script>
	</head>
	<body>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						子项目信息
						<span class="pull-right">
							<button id="btnSave" class="btn" type="button">
								保存
							</button>
						</span>
					</h4>
					<form method="post" action="" id="insertForm">
						<table class="B-table">
							<!-- 这里需要一个隐藏域，存放比如：问题编号,接收人账号，接收单位等信息 -->
							<tr style="display: none;">
								<th width="8%" class="right-border bottom-border">
									计划ID
								</th>
								<th width="8%" class="right-border bottom-border">
									计划数据ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="JHSJID" fieldname="JHSJID" id="jhsjid">
								</td>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="JHID" fieldname="JHID" id="jhid">
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
									<input class="span12" type="text" name="XMBH" fieldname="XMBH">
								</td>
								<th width="8%" class="right-border bottom-border">
									标段编号
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="BDBH" fieldname="BDBH">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border">
									项目名称
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="XMMC" id="xmmc"
										fieldname="XMMC" placeholder="必填" check-type="required">
								</td>
								<th width="8%" class="right-border bottom-border">
									标段名称
								</th>
								<td width="42%" class="bottom-border" colspan=3>
									<input class="span12" type="text" name="BDMC" id="bdmc" 
										fieldname="BDMC" >
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border">
									计划完成时间
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="date" name="JHSJ" fieldname="JHSJ" id="jhwcsj">
								</td>
								<th width="8%" class="right-border bottom-border">
								</th>
								<td width="42%" class="bottom-border" colspan=3>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border">
									排迁子项目
								</th>
								<td width="42%" class="bottom-border">
									<input type="text" class="span12" name="ZXMMC" check-type="maxlength" maxlength="100" fieldname="ZXMMC">
								</td>
								<th width="8%" class="right-border bottom-border">
									排序号
								</th>
								<td width="17%" class="bottom-border">
									<input type="number" class="span12" name="PXH" fieldname="PXH">
								</td>
								<th width="8%" class="right-border bottom-border">
									管线类别
								</th>
								<td width="17%" class="bottom-border">
									<select class="span12" name = "GXLB" fieldname = "GXLB" kind="dic" src="GXLB" >
									</select>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border">
									排迁地点
								</th>
								<td width="92%" colspan="6" class="bottom-border">
									<input type="text" class="span12" name="PQDD" check-type="maxlength" maxlength="100" fieldname="PQDD">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border">
									排迁方案
								</th>
								<td width="92%" colspan="6" class="bottom-border">
									<input type="text" class="span12" name="PQFA" check-type="maxlength" maxlength="300" fieldname="PQFA">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border">
									是否能按计划完成
								</th>
								<td width="42%" class="bottom-border">
									<select class="span12" name = "ISJHWC" fieldname = "ISJHWC" kind="dic" src="SF">
						            </select>
								</td>
								<th width="8%" class="right-border bottom-border">
									开工时间
								</th>
								<td width="42%" class="bottom-border"  colspan=3>
									<input type="date" class="span12" name="KGSJ" fieldname="KGSJ">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border">
									备注
								</th>
								<td width="92%" colspan="6" class="bottom-border">
									<textarea class="span12" rows="3" name="BZ"
										check-type="maxlength" maxlength="4000" fieldname="BZ" id="bz"></textarea>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
		</div>
	</body>
</html>
