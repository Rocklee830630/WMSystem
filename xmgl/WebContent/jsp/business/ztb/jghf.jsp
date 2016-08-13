<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title></title>
<%
	String ZTBSJID = request.getParameter("ZTBSJID")==null?"":request.getParameter("ZTBSJID");
	
%>
<script type="text/javascript" charset="utf-8">
var controllername= "${pageContext.request.contextPath }/ZhaotoubiaoController.do";
	g_bAlertWhenNoResult = false;
  
//查询
	$(function() {
		var ZTBSJID = '<%=ZTBSJID%>';
			        //生成json串
					var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
					//调用ajax插入
					defaultJson.doQueryJsonList(controllername+"?queryZhongbiaojia&ZTBSJID="+ZTBSJID,data,DT1,null,true);
	});
	
	function tr_click(obj,tabListid){
		//alert(tabListid);
		$("#demoForm").setFormValues(obj);
	}
//修改
	$(function() {
		var btn = $("#insert");
		btn.click(function() {
			 if($("#DT1").getSelectedRowIndex()==-1)
			 {
				requireSelectedOneRow();
				return;
			 }
 		 	if($("#demoForm").validationButton())
			{
 		 		//生成json串
 		 		var data = Form2Json.formToJSON(demoForm);
 				//组成保存json串格式
 				//alert(data);
 				var data1 = defaultJson.packSaveJson(data);
 				//调用ajax插入
 				defaultJson.doUpdateJson(controllername + "?updateZhongbiaojia", data1,DT1,null);
			}
			
		});
	});	
</script>
</head>
<body>
<app:dialogs />
	<div class="container-fluid">
	<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<INPUT id="num" type="text" class="span12" kind="text"
										fieldname="rownum" value="1000" operation="<=" keep="true" />
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
							<tr style="display: none">
							</tr>
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<table width="100%" class="table-hover table-activeTd B-table"
						id="DT1" type="single" editable="0" pageNum="5" nopromptmsg="true">
						<thead>
							<tr>
								<th name="XH" id="_XH" tdalign="">
									&nbsp;#&nbsp;
								</th>
								<th fieldname="GZMC" tdalign="center">
									&nbsp;工作名称&nbsp;
								</th>
								<th fieldname="XMBH" tdalign="center">
									&nbsp;项目编号&nbsp;
								</th>
								<th fieldname="XMMC" maxlength="15">
									&nbsp;项目名称&nbsp;
								</th>
								<th fieldname="BDBH" maxlength="15">
									&nbsp;标段编号&nbsp;
								</th>
								<th fieldname="BDMC" maxlength="15">
									&nbsp;标段名称&nbsp;
								</th>
								<th fieldname="ZZBJ" tdalign="right" maxlength="25" width="">
									&nbsp;金额(元)&nbsp;
								</th>
								<th fieldname="BZ" tdalign="" maxlength="25" width="">
									&nbsp;备注&nbsp;
								</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</div>
	<div style="height:5px;"></div>
	<div class="row-fluid">
	<div class="B-small-from-table-autoConcise">
	<h4 class="title">金额信息
		<span class="pull-right">
			<button id="insert" class="btn" type="button">保 存</button>
		</span>
	</h4>
	<form method="post" id="demoForm">
		<table class="B-table" width="100%">
			<TR  style="display:none;">
				<TD>
					<input class="span12"  id="GC_ZTB_JHSJ_ID"  type="text"  name = "GC_ZTB_JHSJ_ID" fieldname="GC_ZTB_JHSJ_ID">
				</TD>
			</TR>
			<tr>
				<th width="8%" class="right-border bottom-border text-right">项目名称</th>
				<td width="92%" colspan="3" class="right-border bottom-border">
					<input class="span12" type="text" id="XMMC" name="XMMC" fieldname="XMMC" disabled/>
				</td>
			</tr>
			<tr>
				<th width="8%" class="right-border bottom-border text-right">标段名称</th>
				<td width="42%" class="right-border bottom-border">
					<input class="span12" type="text" id="BDMC" name="BDMC" fieldname="BDMC" disabled/>
				</td>
				<th width="8%" class="right-border bottom-border">中标金额</th>
				<td width="42%" class="right-border bottom-border">
					<input class="span10" type="number" id="ZZBJ" name="ZZBJ" fieldname="ZZBJ"  style="text-align: right;"/>&nbsp;&nbsp;<b>(元)</b>
				</td>
			</tr>
			<tr>
				<th width="8%" class="right-border bottom-border text-right">备注</th>
				<td width="92%" colspan="3" class="bottom-border">
					<textarea class="span12" rows="3" id="BZ" name="BZ" fieldname="BZ" maxlength="4000"></textarea>
				</td>
			</tr>
		</table>
		</form>
	</div>
	</div>
	</div>
	  <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true" />
	<div align="center">
		<FORM name="frmPost" method="post" style="display: none" target="_blank" id="frmPost" >
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML"> 
			<input type="hidden" name="txtXML" id="txtXML"> 
			<input type="hidden" name="resultXML" id="resultXML">
			<input type="hidden" name="queryResult" id = "queryResult" />
			<input type="hidden" name="txtFilter" order="desc" fieldname="d.gzmc,c.xmbh,c.xmbs,C.PXH" id="txtFilter">
			<input type="hidden" name="ywid" id = "ywid" value="">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
		</FORM>
	</div>
</body>
</html>