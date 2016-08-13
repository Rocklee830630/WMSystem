<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>排迁任务反馈</title>
		<script type="text/javascript" charset="utf-8">
			g_bAlertWhenNoResult = false;
			var controllername= "${pageContext.request.contextPath }/pqgl/pqglController.do";
			$(function(){
				doInit();
				$("#btnAdd").click(function(){
					if($("#insertForm").validationButton()){
		 		 		var data = Form2Json.formToJSON(insertForm);
 						var data1 = defaultJson.packSaveJson(data);
						defaultJson.doInsertJson(controllername + "?insertJzfk&ywid="+$("#ywid").val(), data1,tabList,null);
						$("#insertForm").clearFormResult();
						var sysdate = getCurrentDate();
						$("#fkrq").val(sysdate);
						clearFileTab();
						$("#ywid").val("");
						var tempJson = convertJson.string2json1(data);
						$("#insertForm1").setFormValues(tempJson);
 						//$(window).manhuaDialog.setData(data);
						//$(window).manhuaDialog.sendData();
						$(window).manhuaDialog.getParentObj().doJzgl(data);
					}
				});
				$("#btnSave").click(function(){
					if($("#insertForm1").validationButton()){
						var index =	$("#tabList").getSelectedRowIndex();
		 				//生成json串
		 		 		var data = Form2Json.formToJSON(insertForm1);
 						var data1 = defaultJson.packSaveJson(data);
 						if(index==-1){
							requireSelectedOneRow();
							return;
 						}else{
 							defaultJson.doUpdateJson(controllername + "?updateJzfk", data1,tabList,null);
 						}
 						//$(window).manhuaDialog.setData(data);
						//$(window).manhuaDialog.sendData();
						$(window).manhuaDialog.getParentObj().doJzgl(data);
					}
				});
				$("#btnDel").click(function(){
					var index =	$("#tabList").getSelectedRowIndex();
	 				//生成json串
	 		 		var data = Form2Json.formToJSON(insertForm1);
					var data1 = defaultJson.packSaveJson(data);
					if(index==-1){
						requireSelectedOneRow();
						return;
					}else{
						xConfirm("提示信息","是否确认删除！");
						$('#ConfirmYesButton').unbind();
						$('#ConfirmYesButton').one("click",function(){  
							defaultJson.doDeleteJson(controllername+"?deleteJzfk",data1,tabList,null); 
							$("#insertForm1").clearFormResult();
							clearFileTab();
							$("#ywid").val("");
							//$(window).manhuaDialog.setData(data);
							//$(window).manhuaDialog.sendData();
							$(window).manhuaDialog.getParentObj().doJzgl(data);
						});
					}
					//doSearch();
					//$("#tabList").deleteSelectedRow();
				});
				$("#xzfj").click(function(){
					var index =	$("#tabList").getSelectedRowIndex();
					if(index!=-1){
						$("#tabList").cancleSelected();
						$("#insertForm1").clearFormResult();
						clearFileTab();
						$("#ywid").val("");
					}
					doSelectFile(this);
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
				var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
				//字符串转JSON对象
				var tempJson = convertJson.string2json1(rowValue);
				$("#q_zxmid").val(tempJson.ZXMID);
				$("#insertForm").setFormValues(tempJson);
				$("#insertForm1").setFormValues(tempJson);
				doSearch();
				queryFileData(tempJson.GC_PQ_ZXM_ID,"","","");
				$("#insertForm").clearFormResult();
				$("#insertForm1").clearFormResult();
				setReadonly($("#insertForm"),true);
				setReadonly($("#insertForm1"),true);
				//$("#sliderZone").hide();
				var sysdate = getCurrentDate();
				$("#fkrq").val(sysdate);
				$("#fkrq1").val(sysdate);
				clearFileTab();
				$("#ywid").val("");
			}
			function doSearch(){
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,tabList);
				defaultJson.doQueryJsonList(controllername+"?queryJzfk",data,tabList,null,true);
			}
			//---------------------------------
			//-表格可用性控制
			//---------------------------------
			function setReadonly(obj,flag){
				$('input,select,textarea',obj).each(function () {
					var el = $(this);
					if(el.attr("ro")=="true"){
						el.attr("disabled",flag);
					}
				});
			}
			//单击行事件
			function tr_click(obj,tabId){
				if(tabId=="tabList"){
					var rowValue = $("#"+tabId).getSelectedRow();//获得选中行的json 字符串
					var tempJson = eval("("+rowValue+")");//字符串转JSON对象
					$("#insertForm1").setFormValues(tempJson);
					var index =	$("#tabList").getSelectedRowIndex();
					if(index==0){
						setLock($("#insertForm1"),false);
					}else{
						setLock($("#insertForm1"),true);
					}
					deleteFileData(tempJson.GC_PQ_JZFK_ID,tempJson.ZXMID,tempJson.SJBH,tempJson.YWLX);
					setFileData(tempJson.GC_PQ_JZFK_ID,tempJson.ZXMID,tempJson.SJBH,tempJson.YWLX,"0");
					queryFileData(tempJson.GC_PQ_JZFK_ID,"","","");
				}
			}
			function setLock(obj,flag){
				$('input,select,textarea',obj).each(function () {
					var el = $(this);
					if(el.attr("lock")=="true"){
						el.attr("disabled",flag);
					}
				});
			}
			
		</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<ul class="nav nav-tabs">
				<li class="active">
					<a href="#tab0" data-toggle="tab" id="tabPage0">新增</a>
				</li>
				<li class="">
					<a href="#tab1" data-toggle="tab" id="tabPage1">查看及修改</a>
				</li>
			</ul>

			<div class="tab-content">
				<div class="tab-pane active" id="tab0">
					<div class="row-fluid">
							<div class="B-small-from-table-autoConcise" style="width: 100%">
								<h4 class="title">

									<span class="pull-right">
										<button id="btnAdd" class="btn" type="button">
											新增
										</button>
									</span>
								</h4>
								<form method="post" action="" id="insertForm">
									<table class="B-table" width="100%">
										<!-- 这里需要一个隐藏域，存放比如：问题编号,接收人账号，接收单位等信息 -->
										<tr style="display:none;">
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
												<input class="span12" type="text" name="GC_PQ_JZFK_ID"
													fieldname="GC_PQ_JZFK_ID" keep="true">
											</td>
											<th width="8%" class="right-border bottom-border">
												计划数据ID
											</th>
											<td width="42%" class="right-border bottom-border">
												<input class="span12" type="text" name="JHSJID"
													fieldname="JHSJID" keep="true">
											</td>
											<th width="8%" class="right-border bottom-border">
												计划ID
											</th>
											<td width="42%" class="right-border bottom-border">
												<input class="span12" type="text" name="JHID"
													fieldname="JHID" keep="true">
											</td>
											<th width="8%" class="right-border bottom-border">
												项目ID
											</th>
											<td width="42%" class="right-border bottom-border">
												<input class="span12" type="text" name="XMID"
													fieldname="XMID" keep="true">
											</td>
											<th width="8%" class="right-border bottom-border">
												标段ID
											</th>
											<td width="42%" class="right-border bottom-border">
												<input class="span12" type="text" name="BDID"
													fieldname="BDID" keep="true" id="bdid">
											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right disabledTh">
												项目名称
											</th>
											<td width="42%" class="right-border bottom-border" colspan=3>
												<input class="span12" type="text" name="XMMC" id="xmmc"
													fieldname="XMMC" placeholder="必填" ro="true" keep="true"
													check-type="required maxlength">
											</td>
											<th width="8%" class="right-border bottom-border text-right disabledTh">
												标段名称
											</th>
											<td width="42%" class="right-border bottom-border" colspan=3>
												<input class="span12" type="text" name="BDMC" id="bdmc"
													ro="true" fieldname="BDMC" keep="true">
											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right disabledTh">
												排迁任务名称
											</th>
											<td colspan=3 class="bottom-border">
												<textarea class="span12" rows="1" name="ZXMMC" ro="true"
													keep="true" placeholder="必填"
													check-type="required maxlength" maxlength="300"
													fieldname="ZXMMC"></textarea>
											</td>
											<th width="8%" class="right-border bottom-border text-right disabledTh">
												管线类别
											</th>
											<td width="17%" class="right-border bottom-border">
												<select class="span12 3characters" name="GXLB" fieldname="GXLB"
													ro="true" kind="dic" src="GXLB" keep="true">
												</select>
											</td>
											<th width="8%" class="right-border bottom-border text-right disabledTh">
												归档号
											</th>
											<td width="17%" class="bottom-border">
												<input class="span12" type="text" name="PXH" keep="true"
													ro="true" placeholder="必填" check-type="required"
													fieldname="PXH">
											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right disabledTh">
												统筹计划时间
											</th>
											<td width="17%" class="right-border bottom-border">
												<input class="span12 date" type="date" name="WGSJ" id="jhwcsj"
													ro="true" fieldname="WGSJ" keep="true">
											</td>
											<th width="8%" class="right-border bottom-border text-right">
												能否按计划完成
											</th>
											<td width="17%" class="right-border bottom-border">
												<select class="span12 3characters" name="ISJHWC" fieldname="ISJHWC"
													kind="dic" src="SF" keep="true">
												</select>
											</td>
											<th width="8%" class="right-border bottom-border text-right">
												实际开工时间
											</th>
											<td width="17%" class="right-border bottom-border">
												<input class="span12 date" type="date" name="KGSJ"
													fieldname="KGSJ" keep="true">
											</td>
											<th width="8%" class="right-border bottom-border text-right">
												实际完工时间
											</th>
											<td width="17%" class="bottom-border">
												<input class="span12 date" type="date" name="WCSJ"
													fieldname="WCSJ" keep="true">
											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right">
												进展情况
											</th>
											<td width="42%" class="bottom-border" colspan=3>
												<textarea class="span12" rows="3" name="JZQK"
													check-type="maxlength" maxlength="1000" fieldname="JZQK"></textarea>
											</td>
											<th width="8%" class="right-border bottom-border text-right">
												剩余工作量
											</th>
											<td width="42%" class="bottom-border" colspan=3>
												<textarea class="span12" rows="3" name="SYGZL"
													check-type="maxlength" maxlength="1000" fieldname="SYGZL"></textarea>
											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right">
												存在问题
											</th>
											<td width="42%" class="bottom-border" colspan=3>
												<textarea class="span12" rows="3" name="CZWT"
													check-type="maxlength" maxlength="1000" fieldname="CZWT"></textarea>
											</td>
											<th width="8%" class="right-border bottom-border text-right">
												解决方案
											</th>
											<td width="42%" class="bottom-border" colspan=3>
												<textarea class="span12" rows="3" name="JJFA"
													check-type="maxlength" maxlength="1000" fieldname="JJFA"></textarea>
											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right">
												反馈日期
											</th>
											<td width="17%" class="bottom-border">
												<input class="span12 date" type="date" name="FKRQ" id="fkrq" 
													fieldname="FKRQ" placeholder="必填" check-type="required">
											</td>
											<td width="75%" colspan=6 class="right-border bottom-border">

											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right">
												附件
											</th>
											<td width="92%" colspan=7 class="bottom-border">
												<div>
													<span class="btn btn-fileUpload" id="xzfj" fjlb="0049">
														<i class="icon-plus"></i>
														<span>添加文件...</span>
													</span>
													<table role="presentation" class="table table-striped">
														<tbody fjlb="0049" class="files showFileTab" data-toggle="modal-gallery"
															data-target="#modal-gallery">
														</tbody>
													</table>
												</div>
											</td>
										</tr>
									</table>
								</form>
							</div>
					</div>
				</div>
				<div class="tab-pane" id="tab1">
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
											<th fieldname="FKRQ" type="date" tdalign="center">
												反馈日期
											</th>
											<th fieldname="JZQK" maxlength="15">
												进展情况
											</th>
											<th fieldname="SYGZL" maxlength="15">
												剩余工作量
											</th>
											<th fieldname="CZWT" maxlength="15">
												存在问题
											</th>
											<th fieldname="JJFA" maxlength="15">
												解决方案
											</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
							<div class="B-small-from-table-autoConcise" style="width: 100%">
								<h4 class="title">
									<span class="pull-right">
										<button id="btnSave" class="btn" type="button">
											保存
										</button>
										<button id="btnDel" class="btn" type="button">
											删除
										</button>
									</span>
								</h4>
								<form method="post" action="" id="insertForm1">
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
												<input class="span12" type="text" name="GC_PQ_JZFK_ID"
													fieldname="GC_PQ_JZFK_ID" keep="true">
											</td>
											<th width="8%" class="right-border bottom-border">
												计划数据ID
											</th>
											<td width="42%" class="right-border bottom-border">
												<input class="span12" type="text" name="JHSJID"
													fieldname="JHSJID" keep="true">
											</td>
											<th width="8%" class="right-border bottom-border">
												项目ID
											</th>
											<td width="42%" class="right-border bottom-border">
												<input class="span12" type="text" name="XMID"
													fieldname="XMID" keep="true">
											</td>
											<th width="8%" class="right-border bottom-border">
												标段ID
											</th>
											<td width="42%" class="right-border bottom-border">
												<input class="span12" type="text" name="BDID"
													fieldname="BDID" keep="true">
											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right disabledTh">
												项目名称
											</th>
											<td width="42%" class="right-border bottom-border" colspan=3>
												<input class="span12" type="text" name="XMMC" id="xmmc"
													fieldname="XMMC" placeholder="必填" ro="true" keep="true"
													check-type="required maxlength">
											</td>
											<th width="8%" class="right-border bottom-border text-right disabledTh">
												标段名称
											</th>
											<td width="42%" class="right-border bottom-border" colspan=3>
												<input class="span12" type="text" name="BDMC" id="bdmc"
													ro="true" fieldname="BDMC" keep="true">
											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right disabledTh">
												排迁任务名称
											</th>
											<td colspan=3 class="bottom-border">
												<textarea class="span12" rows="1" name="ZXMMC" ro="true"
													keep="true" placeholder="必填"
													check-type="required maxlength" maxlength="300"
													fieldname="ZXMMC"></textarea>
											</td>
											<th width="8%" class="right-border bottom-border text-right disabledTh">
												管线类别
											</th>
											<td width="17%" class="right-border bottom-border">
												<select class="span12 3characters" name="GXLB" fieldname="GXLB"
													ro="true" kind="dic" src="GXLB" keep="true">
												</select>
											</td>
											<th width="8%" class="right-border bottom-border text-right disabledTh">
												归档号
											</th>
											<td width="17%" class="bottom-border">
												<input class="span12" type="text" name="PXH" keep="true"
													ro="true" placeholder="必填" check-type="required"
													fieldname="PXH">
											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right disabledTh">
												统筹计划时间
											</th>
											<td width="17%" class="right-border bottom-border">
												<input class="span12 date" type="date" name="WGSJ" id="jhwcsj"
													ro="true" fieldname="WGSJ" keep="true">
											</td>
											<th width="8%" class="right-border bottom-border text-right">
												能否按计划完成
											</th>
											<td width="17%" class="right-border bottom-border">
												<select class="span12 3characters" name="ISJHWC" fieldname="ISJHWC"
													kind="dic" src="SF" keep="true" lock="true">
												</select>
											</td>
											<th width="8%" class="right-border bottom-border text-right">
												实际开工时间
											</th>
											<td width="17%" class="right-border bottom-border">
												<input class="span12 date" type="date" name="KGSJ"
													fieldname="KGSJ" keep="true" lock="true">
											</td>
											<th width="8%" class="right-border bottom-border text-right">
												实际完工时间
											</th>
											<td width="17%" class="bottom-border">
												<input class="span12 date" type="date" name="WCSJ"
													fieldname="WCSJ" keep="true" lock="true">
											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right">
												进展情况
											</th>
											<td width="42%" class="bottom-border" colspan=3>
												<textarea class="span12" rows="3" name="JZQK"
													check-type="maxlength" maxlength="1000" fieldname="JZQK"></textarea>
											</td>
											<th width="8%" class="right-border bottom-border text-right">
												剩余工作量
											</th>
											<td width="42%" class="bottom-border" colspan=3>
												<textarea class="span12" rows="3" name="SYGZL"
													check-type="maxlength" maxlength="1000" fieldname="SYGZL"></textarea>
											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right">
												存在问题
											</th>
											<td width="42%" class="bottom-border" colspan=3>
												<textarea class="span12" rows="3" name="CZWT"
													check-type="maxlength" maxlength="1000" fieldname="CZWT"></textarea>
											</td>
											<th width="8%" class="right-border bottom-border text-right">
												解决方案
											</th>
											<td width="42%" class="bottom-border" colspan=3>
												<textarea class="span12" rows="3" name="JJFA"
													check-type="maxlength" maxlength="1000" fieldname="JJFA"></textarea>
											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right">
												反馈日期
											</th>
											<td width="17%" class="bottom-border">
												<input class="span12 date" type="date" name="FKRQ" id="fkrq1"
													fieldname="FKRQ" placeholder="必填" check-type="required">
											</td>
											<td width="8%" colspan=6 class="right-border bottom-border">

											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right">
												附件
											</th>
											<td width="92%" colspan=7 class="bottom-border">
												<div>
													<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0050">
														<i class="icon-plus"></i>
														<span>添加文件...</span>
													</span>
													<table role="presentation" class="table table-striped">
														<tbody fjlb="0050" class="files showFileTab" data-toggle="modal-gallery"
															data-target="#modal-gallery">
														</tbody>
													</table>
												</div>
											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right disabledTh">
												排迁地点
											</th>
											<td colspan=5 class="bottom-border">
												<textarea class="span12" rows="1" name="PQDD" keep="true"
													ro="true" check-type="maxlength" maxlength="300"
													fieldname="PQDD"></textarea>
											</td>
											<th width="8%" class="right-border bottom-border text-right disabledTh">
												项目负责人
											</th>
											<td width="17%" class="bottom-border">
												<input class="span12" type="text" name="XMFZR" id="xmfzr"
													keep="true" ro="true" fieldname="XMFZR" keep="true">
											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right disabledTh">
												排迁方案
											</th>
											<td width="42%" colspan=7 class="bottom-border">
												<textarea class="span12" rows="3" name="PQFA" keep="true"
													ro="true" check-type="maxlength" maxlength="1000"
													fieldname="PQFA"></textarea>
											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right disabledTh">
												备注
											</th>
											<td width="42%" colspan=7 class="bottom-border">
												<textarea class="span12" rows="3" name="BZ" keep="true"
													ro="true" check-type="maxlength" maxlength="4000"
													fieldname="BZ"></textarea>
											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right">
												专业管线排迁图
											</th>
											<td width="92%" colspan=7 class="bottom-border">
												<div>
													<table role="presentation" class="table table-striped">
														<tbody fjlb="0001" class="files showFileTab" notClear="true"
															onlyView="true" data-toggle="modal-gallery"
															data-target="#modal-gallery">
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
													<table role="presentation" class="table table-striped">
														<tbody fjlb="0002" class="files showFileTab" notClear="true"
															onlyView="true" data-toggle="modal-gallery"
															data-target="#modal-gallery">
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
													<table role="presentation" class="table table-striped">
														<tbody fjlb="0003" class="files showFileTab" notClear="true"
															onlyView="true" data-toggle="modal-gallery"
															data-target="#modal-gallery">
														</tbody>
													</table>
												</div>
											</td>
										</tr>
										<tr>
											<th width="8%" class="right-border bottom-border text-right">
												工程预算审定表
											</th>
											<td width="92%" colspan=7 class="bottom-border">
												<div>
													<table role="presentation" class="table table-striped">
														<tbody fjlb="0004" class="files showFileTab" notClear="true"
															onlyView="true" data-toggle="modal-gallery"
															data-target="#modal-gallery">
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
													<table role="presentation" class="table table-striped">
														<tbody fjlb="0005" class="files showFileTab" notClear="true"
															onlyView="true" data-toggle="modal-gallery"
															data-target="#modal-gallery">
														</tbody>
													</table>
												</div>
											</td>
										</tr>
									</table>
								</form>
							</div>
						</div>
						
					</div>
				</div>
			</div>
		<jsp:include page="/jsp/file_upload/fileupload_config.jsp"
							flush="true" />
		</div>
			<div align="center">
				<FORM name="frmPost" method="post" style="display: none"
					target="_blank">
					<!--系统保留定义区域-->
					<input type="hidden" name="queryXML" id="queryXML">
					<input type="hidden" name="txtXML" id="txtXML">
					<input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ"
						id="txtFilter">
					<input type="hidden" name="ywid" id="ywid">
					<input type="hidden" name="resultXML" id="resultXML">
					<!--传递行数据用的隐藏域-->
					<input type="hidden" name="rowData">
				</FORM>
			</div>
	</body>
</html>
