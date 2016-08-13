<!DOCTYPE html>
<html lang="en">
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>业内情况</title>
		<script type="text/javascript" charset="utf-8">
			$(function(){
				doInit();
				$("#btnSave").click(function(){
					if($("#insertForm").validationButton()){
		 				//生成json串
		 				getSjz();
		 				if($("#gczj").val()==""&&$("#sdz").val()!=""){
							xAlert("警告","已录入审定值，必须录入工程造价！","3");
							return;
		 				}
		 				var count = getFileCounts("0005");//大于0表示上传了委托函附件
		 				var flag = $(":radio[name='SFWTH'][value='0']").is(':checked');//表单中设置没有委托函
		 				if(count>0 && flag){
							xConfirm("提示信息","表单中设置没有委托函，但是上传了委托函附件，确认保存么？");
							$('#ConfirmYesButton').unbind();
							$('#ConfirmYesButton').one("click",function(){
				 		 		var data = Form2Json.formToJSON(insertForm);
								$(window).manhuaDialog.setData(data);
								$(window).manhuaDialog.sendData();
								$(window).manhuaDialog.close();
			 				});
		 				}else{
		 					var data = Form2Json.formToJSON(insertForm);
							$(window).manhuaDialog.setData(data);
							$(window).manhuaDialog.sendData();
							$(window).manhuaDialog.close();
		 				}
					}
				});
				$("#btnCancel").click(function(){
					$(window).manhuaDialog.close();
				});
				$("#sdz").blur(function(){
					getSjz();
				});
				$("#gczj").blur(function(){
					getSjz();
				});
			});
			function doInit(){
				//获取父页面iframe中隐藏域的值
				//var rowValue = $(parent.frames["menuiframe"].document).find("#resultXML").val();
				var rowValue = $($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
				//字符串转JSON对象
				var tempJson = eval("("+rowValue+")");
				$("#insertForm").setFormValues(tempJson);
				$("#xmmc").attr("disabled",true);
				$("#bdmc").attr("disabled",true);
				$("#jhwcsj").attr("disabled",true);
				$(":radio[name='SFWTH']").attr("disabled", true);
				if($("#sdrq").val()==""){
					//如果没录审定日期，那么自动填写当前日期
					$("#sdrq").val(getCurrentDate("yyyy-MM-dd"));
				}
				ctrlWth();
				deleteFileData(tempJson.GC_PQ_ZXM_ID,"","","");
				setFileData(tempJson.GC_PQ_ZXM_ID,"",tempJson.SJBH,tempJson.YWLX,"0");
				queryFileData(tempJson.GC_PQ_ZXM_ID,"","","");
			}
			//获取审减值
			function getSjz(){
				var a = $("#sdz").val();
				var b = $("#gczj").val();
				var c = Number(a)-Number(b);
				$("#sjz").val(c);
			}
			//---------------------------------------
			//控制委托函相关样式
			//---------------------------------------
			function ctrlWth(){
				$("#insertForm :radio[name='SFWTH']").each(function(){
					if($(this).is(':checked')){
						if($(this).attr("value")=="1"){
							$("#wthbh").removeAttr("disabled");
							$(".wthbhTH").removeClass("disabledTh");
							$("#wthBtn").show();
				 			//$("#wthbh").attr("placeholder","必填");
				 			//$("#wthbh").attr("check-type","required" );
						}else{
							$("#wthbh").val("");
							$(":radio[name='SFWTH'][value='0']").attr("checked", true);
							$("#wthbh").attr("disabled","true");
							$(".wthbhTH").addClass("disabledTh");
							$("#wthBtn").hide();
				 			//$("#wthbh").removeAttr("placeholder");
				 			//$("#wthbh").removeAttr("check-type");
						}
					}
				});
			}
		</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						排迁任务信息
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
									主键
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="GC_PQ_ZXM_ID" fieldname="GC_PQ_ZXM_ID" id="id">
								</td>
								<th width="8%" class="right-border bottom-border">
									项目ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="XMID" fieldname="XMID" id="xmid">
								</td>
								<th width="8%" class="right-border bottom-border">
									标段ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="BDID" fieldname="BDID" id="bdid">
								</td>
								<th width="8%" class="right-border bottom-border">
									计划ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="JHID" fieldname="JHID" id="jhid">
								</td>
								<th width="8%" class="right-border bottom-border">
									计划数据ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="JHSJID" fieldname="JHSJID" id="jhsjid">
								</td>
								<th width="8%" class="right-border bottom-border">
									事件编号
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="SJBH" fieldname="SJBH">
								</td>
								<th width="8%" class="right-border bottom-border">
									业务类型
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="YWLX" fieldname="YWLX">
								</td>
								<th width="8%" class="right-border bottom-border">
									合同ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="HTID" fieldname="HTID">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									项目名称
								</th>
								<td width="42%" colspan=3 class="right-border bottom-border">
									<input class="span12" type="text" name="XMMC" id="xmmc"
										fieldname="XMMC" placeholder="必填" check-type="required">
								</td>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									标段名称
								</th>
								<td width="42%" colspan=3 class="bottom-border">
									<input class="span12" type="text" name="BDMC" id="bdmc"
										fieldname="BDMC">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									排迁任务名称
								</th>
								<td width="42%" colspan=3 class="right-border bottom-border">
									<input class="span12" type="text" name="ZXMMC" fieldname="ZXMMC" id="jhwcsj">
								</td>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									归档号
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12" type="text" name="PXH" disabled fieldname="PXH">
								</td>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									录入时间
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12" type="text" name="LRSJ" disabled fieldname="LRSJ">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									是否需要委托函
								</th>
								<td width="17%" class="right-border bottom-border">
									<input type="radio" kind="dic" src="SF" fieldname="SFWTH" name="SFWTH" disabled>
								</td>
								<th class="right-border bottom-border text-right disabledTh">
									委托函编号
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12" type="text" name="WTHBH" disabled
										fieldname="WTHBH" id="wthbh">
								</td>
								<th class="right-border bottom-border text-right disabledTh">
									工程造价
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span9" type="number" name="SSZ" style="text-align:right;" 
										fieldname="SSZ" id="gczj" disabled>&nbsp;&nbsp;<b>(元)</b>
								</td>
								<th class="right-border bottom-border text-right">
									送审日期
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12 date" type="date" name="SSRQ" id="ssrq"
										fieldname="SSRQ"  placeholder="必填" check-type="required maxlength">
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									评审报告编号
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12" type="text" name="PSBGBH"
										fieldname="PSBGBH">
								</td>
								<th class="right-border bottom-border text-right">
									审定日期
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12 date" type="date" name="SDRQ" 
										fieldname="SDRQ" id="sdrq">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									审定值
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span9" type="number" name="SDZ"  style="text-align:right;" 
										fieldname="SDZ" id="sdz">&nbsp;&nbsp;<b>(元)</b>
								</td>
								<td colspan='2'>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									审减值
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span9" type="number" name="SJZ" style="text-align:right;" 
										fieldname="SJZ" disabled id="sjz">&nbsp;&nbsp;<b>(元)</b>
								</td>
								<td width="75%" colspan=6 class="right-border bottom-border">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									排迁联络单
								</th>
								<td width="92%" colspan=7 class="bottom-border">
									<div>
										<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0002">
											<i class="icon-plus"></i>
											<span>添加文件...</span>
										</span>
										<table role="presentation" class="table table-striped">
											<tbody fjlb="0002" class="files showFileTab"
												data-toggle="modal-gallery" data-target="#modal-gallery">
											</tbody>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									委托函
								</th>
								<td width="92%" colspan=7 class="bottom-border">
									<div>
										<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0005" id="wthBtn">
											<i class="icon-plus"></i>
											<span>添加文件...</span>
										</span>
										<table role="presentation" class="table table-striped">
											<tbody fjlb="0005" class="files showFileTab"
												data-toggle="modal-gallery" data-target="#modal-gallery">
											</tbody>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									预算送审单
								</th>
								<td width="92%" colspan=7 class="bottom-border">
									<div>
										<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0003">
											<i class="icon-plus"></i>
											<span>添加文件...</span>
										</span>
										<table role="presentation" class="table table-striped">
											<tbody fjlb="0003" class="files showFileTab"
												data-toggle="modal-gallery" data-target="#modal-gallery">
											</tbody>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									预算审定表
								</th>
								<td width="92%" colspan=7 class="bottom-border">
									<div>
										<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0004">
											<i class="icon-plus"></i>
											<span>添加文件...</span>
										</span>
										<table role="presentation" class="table table-striped">
											<tbody fjlb="0004" class="files showFileTab"
												data-toggle="modal-gallery" data-target="#modal-gallery">
											</tbody>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									备注
								</th>
								<td width="92%" colspan=7 class="bottom-border">
									<textarea class="span12" rows="3" id="pfnr" name="PFNR"
										check-type="maxlength" maxlength="4000" fieldname="BZ" id="bz"></textarea>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
			<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
		</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="ywid" id="ywid">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>
