<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<%
	String sjbh=request.getParameter("sjbh");
%>
 <script type="text/javascript">
	var controllername= "${pageContext.request.contextPath }/ZhaoBiaoXuQiuController.do";
	g_bAlertWhenNoResult = false;//禁用查询无结果提示
 	var xmrowValues;
	$(function(){
		doInit();
		//选择项目
		$("#example1").click(function(){
			huoqutr();
			$(window).manhuaDialog({"title":"项目管理>选择项目","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/xmmore.jsp","modal":"1"});
		}); 
		//保存
		$("#bc_btn").click(function(){
			saveZhaoBiao(); 	
		});
		//保存提交
		$("#tj_btn").click(function(){
			$("#xqzt").val('2');
			saveZhaoBiao();
		});
	 	$("#TBJFS").change(function(){
	 		ctrlTze($(this).val());
	 	});
	});
	function doInit(){
		//获取父页面的值
		var a='<%=sjbh%>';
		//赋值查询需求start
		$("#xqsjbh").val(a) ;
		g_bAlertWhenNoResult = false;
		//生成json串
		var data = combineQuery.getQueryCombineData(queryFormXQ,frmPostxq,DT3);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryZhaobiaoxuqiu&readonly=1",data,DT3);
		g_bAlertWhenNoResult = true;
		//查询需求end
		//获取DT3第一行json对象
		$("#DT3").setSelect(0);
		var rowobj = $("#DT3").getSelectedRowJsonObj();
		
		//将父页面的值转成json对象
		//将数据放入表单
		$("#demoForm2").setFormValues(rowobj);
	 	ctrlTze($("#TBJFS").val());
		var XQID=$(rowobj).attr("GC_ZTB_XQ_ID");
		setFileData(rowobj.GC_ZTB_XQ_ID,"",rowobj.SJBH,rowobj.YWLX);
		queryFileData(rowobj.GC_ZTB_XQ_ID,"","","");
		$("#XQID").val(XQID);
		//查询项目
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT2);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryZhaoBiaoXiangMu",data,DT2);
	}
	function ctrlTze(n){
		if("1,2".indexOf(n)==-1){
 			$("#yse").removeAttr("placeholder");
 			$("#yse").removeAttr("check-type");
 			$("#yse").val("");
 			$("#tze1").hide();
 			$("#tze2").hide();
 			$("#tze3").show();
 		}else{
 			$("#yse").attr("placeholder","必填");
 			$("#yse").attr("check-type","required" );
 			$("#tze1").show();
 			$("#tze2").show();
 			$("#tze3").hide();
 		}
	}
 //保存操作
	function saveZhaoBiao(){
		if($("#demoForm2").validationButton()){
			huoqutr();
			/**
			*暂时不要求必须选项目
			if(""==($("#jhsjids").val())){
				xInfoMsg('请选择招标的项目！');
				return ;
			}
			*/
		  //生成json串
		  var data = Form2Json.formToJSON(demoForm2);
		  //组成保存json串格式
		  var data1 = defaultJson.packSaveJson(data);
		  //调用ajax插入
		  defaultJson.doInsertJson(controllername + "?updateZhaobiaoxuqiu&jhsjids="+$("#jhsjids").val(), data1,null);
		  var data2 = $("#frmPost").find("#resultXML").val();
		  var fuyemian=$(window).manhuaDialog.getParentObj();
		  fuyemian.xiugaihang(data2);
		  $(window).manhuaDialog.close();
		}
 }
 
 //子页面调用
 function fanhuixiangm(rowValues,ids)
 {
	 //清空列表
	 $("#DT2").find("tbody").children().remove();
	 xmrowValues=rowValues;
	 for(var i=0;i<rowValues.length;i++)
	 {
	   $("#DT2").insertResult(rowValues[i],DT2,1);
	 }
	 $("#jhsjids").val(ids);
 }
 //子页面调用
 function getArr()
 {
	 return xmrowValues;
 }
	//获取table TR的所有值
	function huoqutr(){
		var ids = "";
		var rowValues=new Array();
		$("#DT2 tbody tr").each(function(i){
			var rowValue = $(this).attr("rowJson");
			rowValues[i]=rowValue;
			var value = convertJson.string2json1($(this).attr("rowJson")).GC_JH_SJ_ID;
			ids+=value+",";
		});	 
		xmrowValues=rowValues;
		$("#jhsjids").val(ids);
	}
 </script>
</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						项目列表

						<span class="pull-right">
							 </span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<td width="10%" class=" right-border bottom-border">
									<input class="span12" type="hidden" id="XQID" name="XQID"
										fieldname="A.XQID" operation="=" logic="and">
								</td>
							</TR>
						</table>
					</form>
					<form method="post"
						id="queryFormXQ" style="display: none;">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<INPUT id="num" type="text" class="span12" keep="true"
										kind="text" fieldname="rownum" value="1000" operation="<=" />
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
							<tr>
								<td width="15%" class=" right-border bottom-border">
									<input class="span12" type="text" placeholder="" name="xqsjbh" id = "xqsjbh"
										fieldname="sjbh" operation="=" logic="and">
								</td>
								
							</tr>
						</table>
					</form>

					<form method="post" id="demoForm1">
						<div class="overFlowX">
							<table width="100%" class="table-hover table-activeTd B-table"
								id="DT2" type="single" noPage="true">
								<thead>
									<tr>
										<th name="XH" id="_XH">
											&nbsp;#&nbsp;
										</th>
										<th fieldname="XMBH" linkFunction="rowView">
											&nbsp;项目编号&nbsp;
										</th>
										<th fieldname="XMMC" maxlength="15">
											&nbsp;项目名称&nbsp;
										</th>
										<th fieldname="BDMC" maxlength="15">
											&nbsp;标段名称&nbsp;
										</th>
										<th fieldname="XMLX" maxlength="15">
											&nbsp;项目类型&nbsp;
										</th>
										<th fieldname="XMDZ" maxlength="15">
											&nbsp;项目地址&nbsp;
										</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
							<table width="100%" class="table-hover table-activeTd B-table"
								id="DT3" type="single" noPage="true" style="display: none">
								<thead>
									<tr>
									<th name="XH" id="_XH">
										&nbsp;#&nbsp;
									</th>
									<th fieldname="GZMC" maxlength="15">
										&nbsp;工作名称&nbsp;
									</th>
								</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</form>
					<form method="post" id="demoForm2">
						<h4 class="title">
							招标信息
							<span class="pull-right">
								 </span>
						</h4>
						<table class="B-table" id="DT" width="100%">
							<tr style="display: none;">
								<td>
									<input type="text" id="jhsjids" name="jhsjids" />
									<input type="text" id="GC_ZTB_XQ_ID" name="GC_ZTB_XQ_ID"
										fieldname="GC_ZTB_XQ_ID" />
								</td>
								<td>
									<input type="text" id="xqzt" name="xqzt" fieldname="XQZT" />
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right" width="8%">
									工作名称
								</th>
								<td class="right-border bottom-border text-right" colspan="3"
									width="17%" >
									<input class="span12"  
										type="text" maxlength="100" id="GZMC" name="GZMC"
										fieldname="GZMC" readonly="readonly">
								</td>
								<td class="right-border bottom-border text-right" colspan="4">
							</tr>
							<tr>
								<th class="right-border bottom-border text-right" width="8%">
									招标类型
								</th>
								<td class="right-border bottom-border " width="17%">
								
									<input class="span12"  
										type="text" maxlength="100" id="ZBLX" name="ZBLX"
										fieldname="ZBLX" readonly="readonly">
								</td>
								<th class="right-border bottom-border text-right" width="8%">
									投标报价方式
								</th>
								<td class=" bottom-border " width="17%">
									<input class="span12"  
										type="text" maxlength="100" id="TBJFS" name="TBJFS"
										fieldname="TBJFS" readonly="readonly">
								</td>
								<th class="right-border bottom-border text-right " width="8%">
									招标方式
								</th>
								<td class="right-border bottom-border " width="17%">
									<input class="span12"  
										type="text" maxlength="100" id="ZBFS" name="ZBFS"
										fieldname="ZBFS" readonly="readonly">
								</td>
								<th width="8%" class="right-border bottom-border text-right"
									id="tze1">
									投资额
								</th>
								<td width="17%" class="bottom-border" id="tze2">
									<input class="span10" type="number" name="YSE" id="yse"
										style="text-align: right;" 
										 fieldname="YSE" readonly="readonly">
									&nbsp;元
								</td>
								<td width="25%" class="bottom-border" id="tze3"
									style="display: none;">
								</td>
							</tr>

							<tr>
								<th class="right-border bottom-border text-right">
									工作内容
								</th>
								<td class="right-border bottom-border text-right" colspan="3">
									<textarea fieldname="GZNR" id="GZNR" name="GZNR" class="span12"
										rows="2" maxlength="4000" readonly="readonly"></textarea>
								</td>
								<th class="right-border bottom-border text-right" width="8%">
									投标人资质、业绩要求
								</th>
								<td class="right-border bottom-border text-right" colspan="3">
									<textarea fieldname="ZZYJYQ" id="ZZYJYQ" name="ZZYJYQ"
										class="span12" rows="2" maxlength="4000" readonly="readonly"></textarea>
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									时限要求
								</th>
								<td class="right-border bottom-border text-right" colspan="3">
									<textarea fieldname="SXYQ" id="SXYQ" name="SXYQ" class="span12"
										rows="2" maxlength="4000" readonly="readonly"></textarea>
								</td>
								<th class="right-border bottom-border text-right">
									技术要求
								</th>
								<td class=" bottom-border text-center" colspan="3">
									<textarea fieldname="JSYQ" id="JSYQ" name="JSYQ" class="span12"
										rows="2" maxlength="4000" readonly="readonly"></textarea>
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									成果或目标要求
								</th>
								<td class="right-border bottom-border text-right" colspan="3">
									<textarea fieldname="CGMBYQ" id="CGMBYQ" name="CGMBYQ"
										maxlength="4000" class="span12" rows="2" readonly="readonly"></textarea>
								<th class="right-border bottom-border text-right">
									配备人员要求
								</th>
								<td class="right-border bottom-border text-right" colspan="3">
									<textarea fieldname="PBRYYQ" id="PBRYYQ" name="PBRYYQ"
										class="span12" rows="2" maxlength="4000" readonly="readonly"></textarea>
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									配备的设备要求
								</th>
								<td class="right-border bottom-border text-right" colspan="3">
									<textarea fieldname="PBSBYQ" id="PBSBYQ" name="PBSBYQ"
										class="span12" rows="2" maxlength="4000" readonly="readonly"></textarea>
								</td>
								<th class="right-border bottom-border text-right">
									取费标准
								</th>
								<td class="right-border bottom-border" colspan="3">
									<textarea fieldname="QFBZ" id="QFBZ" name="QFBZ" class="span12"
										rows="2" maxlength="4000" readonly="readonly"></textarea>
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									其它要求
								</th>
								<td class="right-border bottom-border" colspan=3>
									<textarea fieldname="QTYQ" id="QTYQ" name="QTYQ" class="span12"
										rows="2" maxlength="4000" readonly="readonly"></textarea>
								</td>
								<th class="right-border bottom-border text-right">
								</th>
								<td colspan=3 class="bottom-border">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									附件信息
								</th>
								<td width="92%" colspan=7 class="bottom-border">
									<div>
										<table role="presentation" class="table table-striped" >
											<tbody fjlb="0047" class="files showFileTab"
												data-toggle="modal-gallery" data-target="#modal-gallery" onlyView="true">
											</tbody>
										</table>
									</div>
								</td>
							</tr>
							<tr style="display: none;">
								<th class="right-border bottom-border text-right" width="8%">
									需求单位经办人
								</th>
								<td class="right-border bottom-border " width="17%">
									<input class="span12" id="XQDWJBR" name="XQDWJBR"
										fieldname="XQDWJBR" type="text">
								</td>
								<th class="right-border bottom-border text-right" width="8%">
									需求单位经办人办理时间
								</th>
								<td class=" bottom-border " width="17%">
									<input class="span8" id="XQDWJBRSJ" name="XQDWJBRSJ"
										maxlength="20" fieldname="XQDWJBRSJ" type="date">

								</td>
								<th class="right-border bottom-border text-right " width="8%">
									需求单位负责人
								</th>
								<td class="right-border bottom-border " width="17%">
									<input class="span12" id="XQDWFZR" name="XQDWFZR"
										fieldname="XQDWFZR" type="text">

								</td>
								<th width="8%" class="right-border bottom-border text-right">
									需求单位负责人办理时间
								</th>
								<td width="17%" class="bottom-border">
									<input class="span10" type="date" id="XQDWFZRSJ"
										name="XQDWFZRSJ" fieldname="XQDWFZRSJ">
								</td>
							</tr>
							<tr style="display: none;">
								<th class="right-border bottom-border text-right" width="8%">
									招标部经办人
								</th>
								<td class="right-border bottom-border " width="17%">
									<input class="span12" id="ZBBJBR" name="ZBBJBR"
										fieldname="ZBBJBR" type="text">
								</td>
								<th class="right-border bottom-border text-right" width="8%">
									招标部负责人
								</th>
								<td class=" bottom-border " width="17%">
									<input class="span8" id="ZBBFZR" name="ZBBFZR" maxlength="20"
										fieldname="ZBBFZR" type="text">

								</td>
								<td class="right-border bottom-border text-right" colspan="4">
							</tr>
						</table>
					</form>
				</div>
			</div>
			<jsp:include page="/jsp/file_upload/fileupload_config.jsp"
				flush="true" />
		</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank" id="frmPost">
				<!--系统保留定义区域-->
				<input type="hidden" name="ywid" id="ywid" value="">
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="desc" fieldname="C.PXH"
					id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">

			</FORM>
			<FORM name="frmPostxq" method="post" style="display: none"
				target="_blank" id="frmPostxq">
				<!--系统保留定义区域-->
				<input type="hidden" name="ywid" id="ywid" value="">
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">

			</FORM>
		</div>
		<script type="text/javascript">
 </script>
	</body>
</html>