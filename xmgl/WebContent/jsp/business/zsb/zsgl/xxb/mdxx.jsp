<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>

<app:base />
<title>信息</title>
<%-- <script type="text/javascript" src="${pageContext.request.contextPath }/js/common/xWindow.js"> </script> --%>
<script type="text/javascript" charset="utf-8">
	var controllername = "${pageContext.request.contextPath }/zsb/xxb/xxbController.do";
	//获取父页面的值
	function do_value(){
		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.$("#DT1").getSelectedRow();
		var obj = convertJson.string2json1(rowValue);
		return obj;
	}
	//填写表单
	function setvalue(){
		var value=do_value();
		$("#XxbForm").setFormValues(value);
		var date=$("#MDWCRQ").val();
		date?"":(def_date());
		
		var sjbh=$(value).attr("SJBH");
    	var ywlx=$(value).attr("YWLX");
    	var xxbid=$(value).attr("GC_ZSB_XXB_ID");
		deleteFileData(xxbid,"","","");
		setFileData(xxbid,"",sjbh,ywlx,"0");
		queryFileData(xxbid,"","","");
	}
	
	$(function() {
		setvalue();
		var btn = $("#update");
		btn.click(function() {
					if ($("#XxbForm").validationButton()) {
					//页面为空的值显示0
					getNumber();
					//计算总面积
					plus();
					//生成json串
					var data = Form2Json.formToJSON(XxbForm);
					//组成保存json串格式
					var data1 = defaultJson.packSaveJson(data);
					//调用ajax插入
					defaultJson.doUpdateJson(controllername + "?updateXxb",data1, null,"callback");
					}else{
				  		defaultJson.clearTxtXML();
					}
					
				});
	});
	function callback(){
		//从resultXML获取修改的数据
		var data2 = $("#resultXML").val();
		//返回数据
		var fuyemian=$(window).manhuaDialog.getParentObj();
		fuyemian.xgh(data2);
		$(window).manhuaDialog.close();
	}
	//默认时间
	function def_date(){
		var today=getCurrentDate();
		var date='{"MDWCRQ":\"'+today+'\","MDWCRQ_SV":\"'+today+'\"}';	
		date=convertJson.string2json1(date);
		$("#XxbForm").setFormValues(date);
	}
	//
	function plus(){
		var jttdmj=$("#JTTDMJ").val();
		var gytdmj=$("#GYTDMJ").val();
		var num=Number(jttdmj)+Number(gytdmj);
		$("#ZMJ").val(num);
	}
	//页面值为空，则存储为0
	function getNumber(){
		zero("JMHS");
		zero("QYJS");
		zero("JTTDMJ");
		zero("GYTDMJ");
	}
	function zero(id){
		$("#"+id).val()?($("#"+id).val()):($("#"+id).val("0"));
	}
</script>
</head>
<body>
	<div class="container-fluid">
	<p></p>
		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
			<!-- <h4>信息添加</h4> -->
			<h4 class="title">摸底信息<span class="pull-right">
          			<button id="update" name="update" class="btn"  type="button" data-toggle="modal">保存</button>
          </span></h4>
				<form method="post" id="XxbForm">
					<table class="B-table" width="100%">
						<tr style="display:none">
							<td><input type="text" name="gc_zsb_xxb_id" fieldname="GC_ZSB_XXB_ID" id="gc_zsb_xxb_id"/></td>
							<td><input type="text" name="SJBH" fieldname="SJBH" id="SJBH"/></td>
							<td><input type="text" name="JHSJID" fieldname="JHSJID" id="JHSJID"/></td>
							<td><input type="text" name="XMID" fieldname="XMID" id="XMID"/></td>
						</tr>	
						<tr>
							<th width="7%" class="right-border bottom-border text-right disabledTh">项目名称</th>
							<td width="68%" colspan="5" class="right-border bottom-border"><input class="span12" type="text" placeholder="" check-type="required" id="XMMC" name="XMMC" fieldname="XMMC" disabled /></td>
							<th width="7%" class="right-border bottom-border text-right disabledTh">区域</th>
							<td width="18%" class="right-border bottom-border"><input class="span12" type="text" placeholder="" check-type="required" id="QY" name="QY" fieldname="QY" disabled /></td>
						</tr>
						<tr>
							<th width="7%" class="right-border bottom-border text-right">摸底完成时间</th>
							<td width="18%" class="right-border bottom-border"><input class="span12 date" type="date" id="MDWCRQ" name="MDWCRQ" fieldname="MDWCRQ" check-type="required" placeholder="必填"/></td>
							<td width="75%" colspan="6" class="right-border bottom-border"></td>
						</tr>
						<tr>
							<th width="7%" class="right-border bottom-border text-right">居民户数</th>
							<td width="18%" class="right-border bottom-border">
								<input class="span12" type="number" placeholder="" id="JMHS" name="JMHS" fieldname="JMHS" check-type="number" style="text-align:right;" min='0'>
							</td>
							<th width="7%" class="right-border bottom-border text-right">企业家数</th>
							<td width="18%" class="right-border bottom-border">
								<input class="span12" type="number" placeholder="" id="QYJS" name="QYJS" fieldname="QYJS"  check-type="number" style="text-align:right;" min='0'>
							</td>
							<th width="7%" class="right-border bottom-border text-right">集体土地征地面积</th>
							<td width="18%" class="right-border bottom-border">
								<input class="span12" type="number" placeholder="" id="JTTDMJ" name="JTTDMJ" fieldname="JTTDMJ" check-type="number" style="text-align:right;" min='0' oninput="plus();" >
								</td>
							<th width="7%" class="right-border bottom-border text-right">国有土地征地面积</th>
							<td width="18%" class="right-border bottom-border">
								<input class="span12" type="number" placeholder="" id="GYTDMJ" name="GYTDMJ" fieldname="GYTDMJ"  check-type="number" style="text-align:right;" min='0' oninput="plus();" >
							</td>
						</tr>
						<tr>
							<th width="7%" class="right-border bottom-border text-right disabledTh">总面积</th>
							<td width="18%" class="right-border bottom-border">
								<input class="span12" type="number" placeholder="" id="ZMJ" name="ZMJ" fieldname="ZMJ" check-type="number" style="text-align:right;" min='0' disabled>
							</td>
							<td width="25%" colspan="2" class="right-border bottom-border"></td>
							<th width="7%" class="right-border bottom-border text-right">摸底估算</th>
							<td width="43%" colspan="3" class="bottom-border">
								<input class="span12" type="number" style="width:36%;text-align:right;" placeholder="" min="0" id="MDGS" name="MDGS" fieldname="MDGS" check-type="number" min='0'>&nbsp;&nbsp;<b>（元）</b>
							</td>
							<!-- <td width="25%" colspan="2" class="right-border bottom-border"></td> -->
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">摸底工作备注</th>
							<td width="92%" colspan="7" class="right-border bottom-brder"><textarea class="span12" rows="3" id="MDGZBZ" name="MDGZBZ" fieldname="MDGZBZ" check-type="maxlength" maxlength="4000"></textarea></td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">摸底信息附件</th>
								<td colspan=7 class="bottom-border">
									<div>
										<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="1111">
										<i class="icon-plus"></i>
										<span>添加文件...</span>
									</span>
									<table role="presentation" class="table table-striped">
										<tbody fjlb="1111" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery">
										</tbody>
									</table>
								</div>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
	</div>
	<div align="center">
		<FORM name="frmPost" method="post" id="frmPost" style="display: none"
			target="_blank">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML"> 
			<input type="hidden" name="txtXML" id="txtXML">
			<input type="hidden" name="ywid" id="ywid">
			<input type="hidden" name="txtFilter"  order="desc" fieldname = "lrsj"	id = "txtFilter">
			<input type="hidden" name="resultXML" id="resultXML">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
		</FORM>
	</div>
</body>
</html>