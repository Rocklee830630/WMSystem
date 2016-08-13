<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>

<app:base />
<title></title>
<%
	String xx=request.getParameter("xx");
%>
<script type="text/javascript" charset="utf-8">
	var controllername = "${pageContext.request.contextPath }/sjgl/cbsjpf/cbsjController.do";
	$(function() {
		var clear=$("#clear");
		clear.click(function(){
			$("#LbjForm").clearFormResult();
			
		});
		var btn = $("#update");
		btn.click(function() {
			 	if($("#LbjForm").validationButton())
					{
						//生成json串
						var data = Form2Json.formToJSON(LbjForm);
						//组成保存json串格式
						var data1 = defaultJson.packSaveJson(data);
						//调用ajax插入
						var aa=document.getElementById("ID").value;
						if(aa==null||aa=="")
							{
							defaultJson.doInsertJson(controllername + "?insertCbsj",data1, null);
							}else{	
							defaultJson.doUpdateJson(controllername + "?updateCbsj",data1, null);
							}
							/* parent.gengxinchaxun(odd);
							parent.$("body").manhuaDialog.close(); */
							var fuyemian=parent.$("body").manhuaDialog.getParentObj();
							fuyemian.gengxinchaxun();
							parent.$("body").manhuaDialog.close();
					}else{
				  		defaultJson.clearTxtXML();
					}
				});
	});
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid" hidden="ture">
			<div class=B-small-from-table-autoConcise>
				<table cellpadding="0" cellspacing="0" border="0" class="" hidden="ture" id="DT1" width="10%">
					<thead>
						<tr>
							
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
			<h4 class="title">初步设计批复信息
			<span class="pull-right">
         		 <button id="update" class="btn"  type="button">保存</button>
         		 <!-- <button id="clear" class="btn"  type="button">清空</button> -->
          	</span></h4>
				<form method="post" action="${pageContext.request.contextPath }/zjb/lbj/insertLbj.xhtml" id="LbjForm">
					<table class="B-table" width="100%">
						<TR  style="display:none;">
						<!-- <TR> -->
						<TD>
						</TD><TD>	<input type="text" class="span12" kind="text"  fieldname="GC_SJ_CBSJPF_ID" id="ID" keep="true">
						</TD><TD>	<input type="text" class="span12" kind="text"  fieldname="JHID" id="JHID" keep="true">
						</TD><TD>	<input type="text" class="span12" kind="text"  fieldname="JHSJID" id="JHSJID" keep="true">
						</TD><TD>	<input type="text" class="span12" kind="text"  fieldname="ND" id="ND" keep="true">
						</TD><TD>	<input type="text" class="span12" kind="text"  fieldname="XMID" id="XMID" keep="true">
						</TD><TD>	<input type="text" class="span12" kind="text"  fieldname="BDID" id="BDID" keep="true">
						</TD>
        				</TR>
						<tr>
							<th width="10%" colspan="1" class="right-border bottom-border text-right">项目名称</th>
							<td width="90%" colspan="9" class="right-border bottom-border"><input class="span12" type="text" name = "XMMC" id="XMMC" readonly keep="true"/></td>
						</tr>
						<tr>
							<th width="10%" colspan="1" class="right-border bottom-border text-right">标段名称</th>
							<td width="50%" colspan="5" class="right-border bottom-border"><input class="span12" type="text" name = "BDMC" id="BDMC" readonly keep="true"/></td>							
							<th width="10%" colspan="1" class="right-border bottom-border text-right">批复时间</th>
							<td width="30%" colspan="3" class="bottom-border"><input class="span12" type="date" id="CBSJPF" name="CBSJPF" fieldname="CBSJPF" check-type="required" placeholder="必填"></td>
						</tr>
						<tr>
							<th width="10%" colspan="1" class="right-border bottom-border text-right">备注</th>
							<td width="90%" colspan="9" class="bottom-border"><textarea class="span12" rows="3" id="" check-type="maxlength" name="BZ" fieldname="BZ" maxlength="4000"></textarea></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>

	<div align="center">
		<FORM name="frmPost" method="post" id="frmPost" style="display: none"
			target="_blank">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML"> <input
				type="hidden" name="txtXML" id="txtXML">
			<input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
			<input type="hidden" name="resultXML" id="resultXML">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">

		</FORM>
	</div>
	<script>
  var a=<%=xx%>;
  var odd=convertJson.string2json1(a); 
  setValueByArr(odd,["XMMC","BDMC"]);
  $("#LbjForm").setFormValues(odd);
  /* setValueByArr(a,["ID","XMMC","BDMC","CSBGBH","TXQSJ","ZBSJ","ZXGS","TZJJSJ","ZXGSJ","YWRQ","DYRQ","SGFAJS","ZBWJJS","ZXGSRQ","JGBCSRQ","SBCSZ","CZSWRQ","CSSDZ","SJZ","SJBFB","ZDJ","BZ"]);  */
  </script>
</body>
</html>