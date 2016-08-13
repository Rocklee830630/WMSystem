<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat"%>
<app:base />
<title>信息</title>
<%-- <%

	Date d=new Date();//获取时间
	SimpleDateFormat sam_date=new SimpleDateFormat("yyyy-MM-dd");		//转换格式
	String today=sam_date.format(d);
%> --%>
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
		var date=$("#QWTRQ").val();
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
	//异步操作
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
		var s_date='{"QWTRQ":\"'+today+'\","QWTRQ_SV":\"'+today+'\"}';
		var date=convertJson.string2json1(s_date);
		$("#XxbForm").setFormValues(date);
	}
</script>
</head>
<body>
	<div class="container-fluid">
	<p></p>
		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
			<!-- <h4>信息添加</h4> -->
			<h4 class="title">委托协议信息<span class="pull-right">
          			<button id="update" name="update" class="btn"  type="button" data-toggle="modal">保存</button>
          </span></h4>
				<form method="post" id="XxbForm">
					<table class="B-table" width="100%">
						<tr style="display:none">
						<!-- <tr> -->
							<td><input type="text" name="gc_zsb_xxb_id" fieldname="GC_ZSB_XXB_ID" id="gc_zsb_xxb_id"/></td>
							<td><input type="text" name="SJBH" fieldname="SJBH" id="SJBH"/></td>
							<td><input type="text" name="JHSJID" fieldname="JHSJID" id="JHSJID"/></td>
							<td><input type="text" name="XMID" fieldname="XMID" id="XMID"/></td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
							<td width="92%" colspan="7" class="right-border bottom-border"><input class="span12" type="text" placeholder="" check-type="required" id="XMMC" name="XMMC" fieldname="XMMC" disabled /></td>
						</tr>
						<tr>
							<th class="right-border bottom-border text-right disabledTh">区域</th>
							<td width="42%" colspan="3" class="right-border bottom-border"><input class="span12" style="width:60%" type="text" placeholder="" id="QY" name="QY" fieldname="QY" check-type="" disabled></td>
							<th class="right-border bottom-border text-right disabledTh">负责单位</th>
							<td width="42%" colspan="3" class="right-border bottom-border"><input class="span12" style="width:60%" type="text" placeholder="" id="ZRDW" name="ZRDW" fieldname="ZRDW" check-type="" disabled></td>
						</tr>
						<tr>	
							<th class="right-border bottom-border text-right">协议金额</th>
							<td  colspan="2" class="right-border bottom-border"><input class="span12" style="width:75%;text-align:right" type="number" id="XYJE" name="XYJE" fieldname="XYJE" min="0">&nbsp;&nbsp;<b>（元）</b></td>
							<td width="17%" class="right-border bottom-border">
							<th class="right-border bottom-border text-right">协议签订日期</th>
							<td class="right-border bottom-border"><input class="span12 date" type="date" id="QWTRQ" name="QWTRQ" fieldname="QWTRQ" check-type="required" /></td>
							<td width="25%" class="right-border bottom-border"  colspan="2">
						</tr>
						<tr>
							<th class="right-border bottom-border text-right">协议签订备注</th>
							<td colspan="7" class="bottom-brder"><textarea class="span12" rows="3" id="QWTBZ" name="QWTBZ" fieldname="QWTBZ" check-type="maxlength" maxlength="4000"></textarea></td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">委托协议附件</th>
								<td colspan=7 class="bottom-border">
									<div>
										<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="1110">
											<i class="icon-plus"></i>
											<span>添加文件...</span>
										</span>
										<table role="presentation" class="table table-striped">
											<tbody fjlb="1110" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery">
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