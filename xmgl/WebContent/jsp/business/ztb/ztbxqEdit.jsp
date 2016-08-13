<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<%
	String xx=request.getParameter("xx");
	String delFlag = request.getParameter("delFlag")==null?"":request.getParameter("delFlag");
%>
 <script type="text/javascript">
	var controllername= "${pageContext.request.contextPath }/ZhaoBiaoXuQiuController.do";
	g_bAlertWhenNoResult = false;//禁用查询无结果提示
	var delFlag = '<%=delFlag%>';
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
	 	$("#btnDel").click(function(){
			var data = Form2Json.formToJSON(demoForm2);
			xConfirm("提示信息","是否确认删除！");
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').one("click",function(){
				$(window).manhuaDialog.getParentObj().doDelZtb(data,$(window));
			});
		});
	});
	function doInit(){
		//获取父页面的值
		var a =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
		//将父页面的值转成json对象
		var odd=convertJson.string2json1(a);
		//将数据放入表单
		$("#demoForm2").setFormValues(odd);
	 	ctrlTze($("#TBJFS").val());
		var XQID=$(odd).attr("GC_ZTB_XQ_ID");
		deleteFileData(odd.GC_ZTB_XQ_ID,"","","");
		setFileData(odd.GC_ZTB_XQ_ID,"",odd.SJBH,odd.YWLX,"0");
		queryFileData(odd.GC_ZTB_XQ_ID,"","","");
		$("#XQID").val(XQID);
		//查询项目
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT2);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryZhaoBiaoXiangMu",data,DT2,null,true);
		if(delFlag=="false"){
			$("#btnDel").hide();
		}else{
			$("#btnDel").show();
		}
	}
	function ctrlTze(n){
		if("1,2".indexOf(n)==-1){
 			//$("#yse").removeAttr("placeholder");
 			//$("#yse").removeAttr("check-type");
 			$("#yse").val("");
 			$("#tze1").hide();
 			$("#tze2").hide();
 			$("#tze3").show();
 		}else{
 			//$("#yse").attr("placeholder","必填");
 			//$("#yse").attr("check-type","required" );
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
			var jhsjidStr = $("#jhsjids").val();
			var tempArr = jhsjidStr.split(",");
		  	defaultJson.doInsertJson(controllername + "?updateZhaobiaoxuqiu&jhsjids="+$("#jhsjids").val(), data1,null,"updateHuiDiao");
		}
 	}
 function updateHuiDiao()
 {
	  var data2 = $("#frmPost").find("#resultXML").val();
	  var fuyemian=$(window).manhuaDialog.getParentObj();
	  fuyemian.xiugaihang(data2);
	  $(window).manhuaDialog.close();
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
							<button id="example1" class="btn" type="button">
								选择项目
							</button> </span>
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

						<table width="100%" class="table-hover table-activeTd B-table"
							id="DT2" type="single" noPage="true" pageNum="1000">
							<thead>
								<tr>
									<th name="XH" id="_XH">
										&nbsp;#&nbsp;
									</th>
									<th fieldname="XMBH" linkFunction="rowView">
										&nbsp;项目编号&nbsp;
									</th>
									<th fieldname="XMMC" maxlength="20">
										&nbsp;项目名称&nbsp;
									</th>
									<th fieldname="BDBH" maxlength="15" Customfunction="doBdbh">
										&nbsp;标段编号&nbsp;
									</th>
									<th fieldname="BDMC" maxlength="15" Customfunction="doBdmc">
										&nbsp;标段名称&nbsp;
									</th>
									<th fieldname="XMLX" maxlength="15" tdalign="center">
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
					<form method="post" id="demoForm2">
						<h4 class="title">
							招标信息
							<span class="pull-right">
								<button id="bc_btn" class="btn" type="button">
									保存
								</button>
								<button id="btnDel" class="btn" type="button">
									删除
								</button> </span>
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
									    <!--  add by cbl start -->
									    	<input type="hidden" id="SJBH" fieldname="SJBH" name = SJBH/>
									      	<input type="hidden" id="YWLX" fieldname="YWLX" name = YWLX/>
									     <!--  add by cbl end -->
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right" width="8%">
									工作名称
								</th>
								<td class="right-border bottom-border text-right" colspan="3"
									width="17%">
									<input class="span12" placeholder="必填" check-type="required maxlength"
										type="text" maxlength="4000" id="GZMC" name="GZMC"
										fieldname="GZMC">
								</td>
								<th class="right-border bottom-border text-right" width="8%">
									年度
								</th>
								<td class="right-border bottom-border text-left" colspan="3"
									width="17%">
									<select class="span12 person" id="ND" name="ND"
										check-type="required" fieldname="ND" operation="=" kind="dic"
										src="XQZBNF">
									</select>
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right" width="8%">
									招标类型
								</th>
								<td class="right-border bottom-border " width="17%">
									<select class="span12 4characters" id="ZBLX"
										check-type="required" name="ZBLX" fieldname="ZBLX" kind="dic"
										src="ZBLX" operation="=">
									</select>
								</td>
								<th class="right-border bottom-border text-right" width="8%">
									投标报价方式
								</th>
								<td class=" bottom-border " width="17%">
									<select class="span12 6characters" id="TBJFS" name="TBJFS"
										maxlength="100" fieldname="TBJFS" defaultMemo="请选择" kind="dic"
										src="TBBJFS">
									</select>
								</td>
								<th width="8%" class="right-border bottom-border text-right"
									id="tze1">
									投资额
								</th>
								<td width="17%" class="bottom-border" id="tze2">
									<input class="span10" type="number" name="YSE" id="yse"
										style="text-align: right;" placeholder=""
										check-type="" fieldname="YSE"><b>(元)</b>
								</td>
								<td width="25%" class="bottom-border" id="tze3" colspan=2
									style="display: none;">
								</td>
								<td class="right-border bottom-border " width="25%" colspan=2>
								</td>
							</tr>

							<tr>
								<th class="right-border bottom-border text-right">
									工作内容
								</th>
								<td class="right-border bottom-border text-right" colspan="3">
									<textarea fieldname="GZNR" id="GZNR" name="GZNR" class="span12"
										rows="2" maxlength="4000" check-type="maxlength"></textarea>
								</td>
								<th class="right-border bottom-border text-right" width="8%">
									投标人资质、业绩要求
								</th>
								<td class="right-border bottom-border text-right" colspan="3">
									<textarea fieldname="ZZYJYQ" id="ZZYJYQ" name="ZZYJYQ"
										class="span12" rows="2" maxlength="4000" check-type="maxlength"></textarea>
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									时限要求
								</th>
								<td class="right-border bottom-border text-right" colspan="3">
									<textarea fieldname="SXYQ" id="SXYQ" name="SXYQ" class="span12"
										rows="2" maxlength="4000" check-type="maxlength"></textarea>
								</td>
								<th class="right-border bottom-border text-right">
									技术要求
								</th>
								<td class=" bottom-border text-center" colspan="3">
									<textarea fieldname="JSYQ" id="JSYQ" name="JSYQ" class="span12"
										rows="2" maxlength="4000" check-type="maxlength"></textarea>
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									成果或目标要求
								</th>
								<td class="right-border bottom-border text-right" colspan="3">
									<textarea fieldname="CGMBYQ" id="CGMBYQ" name="CGMBYQ"
										maxlength="4000" class="span12" rows="2" check-type="maxlength"></textarea>
								<th class="right-border bottom-border text-right">
									配备人员要求
								</th>
								<td class="right-border bottom-border text-right" colspan="3">
									<textarea fieldname="PBRYYQ" id="PBRYYQ" name="PBRYYQ"
										class="span12" rows="2" maxlength="4000" check-type="maxlength"></textarea>
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									配备的设备要求
								</th>
								<td class="right-border bottom-border text-right" colspan="3">
									<textarea fieldname="PBSBYQ" id="PBSBYQ" name="PBSBYQ"
										class="span12" rows="2" maxlength="4000" check-type="maxlength"></textarea>
								</td>
								<th class="right-border bottom-border text-right">
									取费标准
								</th>
								<td class="right-border bottom-border" colspan="3">
									<textarea fieldname="QFBZ" id="QFBZ" name="QFBZ" class="span12"
										rows="2" maxlength="4000" check-type="maxlength"></textarea>
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									其它要求
								</th>
								<td class="right-border bottom-border" colspan=3>
									<textarea fieldname="QTYQ" id="QTYQ" name="QTYQ" class="span12"
										rows="2" maxlength="4000" check-type="maxlength"></textarea>
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
										<span class="btn btn-fileUpload" onclick="doSelectFile(this);"
											fjlb="0047"> <i class="icon-plus"></i> <span>添加文件...</span>
										</span>
										<table role="presentation" class="table table-striped">
											<tbody fjlb="0047" class="files showFileTab"
												data-toggle="modal-gallery" data-target="#modal-gallery">
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
				<input type="hidden" name="txtFilter" order="desc" fieldname="c.xmbh,c.xmbs,C.PXH"
					id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">

			</FORM>
		</div>
	</body>
</html>