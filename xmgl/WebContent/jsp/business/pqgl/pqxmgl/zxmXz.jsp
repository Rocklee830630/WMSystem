<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@page import="com.ccthanking.framework.Globals"%>
		<%@page import="com.ccthanking.framework.common.User"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%
			User user = (User) request.getSession().getAttribute(
					Globals.USER_KEY);
			String userName = user.getName();
			String userAccount = user.getAccount();
		%>
		<app:base />
		<title>业内情况</title>
		<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
		<script type="text/javascript" charset="utf-8">
			var controllername= "${pageContext.request.contextPath }/pqgl/pqglController.do";
			g_bAlertWhenNoResult = false;
			g_nameMaxlength = 20;
			var userName = '<%=userName%>';
			var userAccount = '<%=userAccount%>';
			$(function(){
				doInit();
				$("#btnSave").click(function(){
					if($("#insertForm").validationButton()){
		 				//生成json串
		 		 		var data = Form2Json.formToJSON(insertForm);
						$(window).manhuaDialog.getParentObj().setYwid($("#ywid").val());
						$(window).manhuaDialog.setData(data);
						$(window).manhuaDialog.sendData();
						$(window).manhuaDialog.close();
					}
				});
				$("#btnAdd").click(function(){
					$("#insertForm").clearFormResult();
					clearFileTab();
					//$("#xmBtn").show();
					$("#ywid").val("");
				});
				$("#xmBtn").click(function(){
					queryProjectWithBD("isPq=1");
				});
				//按钮绑定事件（清空）
			    $("#btnClear").click(function() {
			        $("#queryForm").clearFormResult();
					initCommonQueyPage();
			    });
				$("#btnCancel").click(function(){
					$(window).manhuaDialog.close();
				});
				$(":radio[name='SFWTH']").click(function(){
					ctrlWth();
				});
			});
			function doInit(){
				$("#xmmc").attr("disabled",true);
				$("#bdmc").attr("disabled",true);
				$("#jhwcsj").attr("disabled",true);
				$("#xmfzr").attr("disabled",true);
				$("#xmfzr").attr("disabled",true);
				$("#xmfzr").val(userName);
				$("#xmfzr").attr("code",userAccount);
				//查询
				$("#q_xmfzr").val(userAccount);
				$(":radio[name='SFWTH'][value='0']").attr("checked", true);
				ctrlWth();
				//控制 项目选择按钮
				//$("#xmBtn").hide();
			}
			function getWinData(data){
				var tempJson = eval("("+data+")");
				$("#xmmc").val(tempJson.XMMC);
				$("#bdmc").val(tempJson.BDMC);
				$("#xmid").val(tempJson.XMID);
				$("#bdid").val(tempJson.BDID);
				$("#xmbh").val(tempJson.XMBH);
				$("#bdbh").val(tempJson.BDBH);
				$("#jhid").val(tempJson.JHID);
				$("#jhsjid").val(tempJson.GC_JH_SJ_ID);
				$("#nd").val(tempJson.ND);
				$("#jhwcsj").val(tempJson.PQ);
				setPqzxmCounts();
			}
			
			function tr_click(obj,tabId){
				var rowValue = $("#"+tabId).getSelectedRow();//获得选中行的json 字符串
				var tempJson = eval("("+rowValue+")");//字符串转JSON对象
				$("#insertForm").setFormValues(tempJson);
				//$("#xmBtn").hide();
				setFileData(tempJson.GC_PQ_ZXM_ID,tempJson.JHID,tempJson.SJBH,tempJson.YWLX);
				queryFileData(tempJson.GC_PQ_ZXM_ID,"","","");
			}
			function setPqzxmCounts(){
				var counts = getPqzxmCounts();
				var lsh = (Number(counts)+1)+"";
				var xmbh = $("#xmbh").val()+"-";
				var bdbh = $("#bdbh").val()==""?"":$("#bdbh").val()+"-";
				$("#pxh").val(xmbh+bdbh+lsh);
			}
			function getPqzxmCounts(){
				var n = $("#jhsjid").val();
				var str = "";
				$.ajax({
					url:controllername + "?getPqzxmCounts&jhsjid="+n,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						str = result.msg;
					}
				});
				return str;
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
							//客户说委托函编号不是必填项
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
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<div style="height: 5px;">
					</div>
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
									<input class="span12" type="text" name="GC_PQ_ZXM_ID" fieldname="GC_PQ_ZXM_ID" id="zxmid">
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
									项目编号
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="XMBH" fieldname="XMBH" id="xmbh">
								</td>
								<th width="8%" class="right-border bottom-border">
									标段编号
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="BDBH" fieldname="BDBH" id="bdbh">
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
									年度
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="ND" fieldname="ND" id="nd">
								</td>
								<th width="8%" class="right-border bottom-border">
									能否按计划完成
								</th>
								<td  width="17%" class="right-border bottom-border">
									<input class="span12" type="text" name="ISJHWC" value="1" fieldname="ISJHWC">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									项目名称
								</th>
								<td width="67%" class="right-border bottom-border" colspan=3>
									<input class="span12" type="text" name="XMMC" id="xmmc" 
										fieldname="XMMC" placeholder="必填" check-type="required maxlength">
									</td>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									标段名称
								</th>
								<td width="17%" class="bottom-border" colspan=3>
									<input class="span12" type="text" name="BDMC" id="bdmc" style="width:90%"
										fieldname="BDMC">
									<button class="btn btn-link"  type="button" id="xmBtn" title="点击选择项目"><i class="icon-edit"></i></button>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									排迁任务名称
								</th>
								<td colspan=3 class="bottom-border">
									<textarea class="span12" rows="1" name="ZXMMC"
										placeholder="必填" check-type="required maxlength" maxlength="300" fieldname="ZXMMC" ></textarea>
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									管线类别
								</th>
								<td width="17%" class="right-border bottom-border">
									<select class="span12 3characters" name="GXLB" fieldname="GXLB" 
										 kind="dic" src="GXLB">
									</select>
								</td>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									归档号
								</th>
								<td width="17%" class="bottom-border">
									<input class="span12" type="text" name="PXH" id="pxh" fieldname="PXH"
										placeholder="必填" check-type="required" disabled>
								</td>
							</tr>
							<tr>
								
								<th width="8%" class="right-border bottom-border text-right">
									排迁地点
								</th>
								<td colspan=5 class="bottom-border">
									<textarea class="span12" rows="1" name="PQDD"
										check-type="maxlength" maxlength="300" fieldname="PQDD" ></textarea>
								</td>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									项目负责人
								</th>
								<td width="17%" class="bottom-border">
									<input class="span12" type="text" name="XMFZR" id="xmfzr" keep="true"
										fieldname="XMFZR">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									统筹计划时间
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12 date" type="date" name="WGSJ" id="jhwcsj"
										fieldname="WGSJ">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									计划开工时间
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12 date" type="date" name="KGSJ_JH" 
										fieldname="KGSJ_JH">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									计划完工时间
								</th>
								<td width="17%" class="bottom-border">
									<input class="span12 date" type="date" name="WCSJ_JH" 
										fieldname="WCSJ_JH">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									排迁联络单编号
								</th>
								<td width="17%" class="bottom-border">
									<input class="span12" type="text" name="LDBH" 
										fieldname="LLDBH">
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									工程造价
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span9" type="number" name="SSZ" style="text-align:right;" 
										fieldname="SSZ" id="gczj">&nbsp;&nbsp;<b>(元)</b>
								</td>
								<th class="right-border bottom-border text-right">
									送审单编号
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12" type="text" name="SSDBH" 
										fieldname="SSDBH">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									是否需要委托函
								</th>
								<td width="17%" class="right-border bottom-border">
									<input type="radio" kind="dic" src="SF" fieldname="SFWTH" name="SFWTH">
								</td>
								<th class="right-border bottom-border text-right wthbhTH">
									委托函编号
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12" type="text" name="WTHBH" 
										fieldname="WTHBH" id="wthbh">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									排迁方案
								</th>
								<td width="42%" colspan=7 class="bottom-border">
									<textarea class="span12" rows="3" name="PQFA"
										check-type="maxlength" maxlength="1000" fieldname="PQFA" ></textarea>
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
									专业管线排迁图
								</th>
								<td width="92%" colspan=7 class="bottom-border">
									<div>
										<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0001">
											<i class="icon-plus"></i>
											<span>添加文件...</span>
										</span>
										<table role="presentation" class="table table-striped">
											<tbody fjlb="0001" class="files showFileTab" 
												data-toggle="modal-gallery" data-target="#modal-gallery">
											</tbody>
										</table>
									</div>
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
									备注
								</th>
								<td width="42%" colspan=7 class="bottom-border">
									<textarea class="span12" rows="3" name="BZ"
										check-type="maxlength" maxlength="4000" fieldname="BZ"></textarea>
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
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="Z.XMID,Z.PXH" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="ywid" id="ywid">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>
