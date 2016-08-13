<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title>规划审批进度维护</title>
<%-- <script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script> --%>
<%
	String json=request.getParameter("json");
%>
<script type="text/javascript" charset="utf-8">
var controllername= "${pageContext.request.contextPath }/qqsx/ghsp/ghspController.do";
	$(document).ready(function(){
		var a=<%=json%>;
		var odd=convertJson.string2json1(a);
		$("#GhspForm").setFormValues(odd);
		var aa=$(odd).attr("GC_QQSX_GHSP_ID");
		var SJBH=$(odd).attr("SJBH");
		var YWLX=$(odd).attr("YWLX");
		setFileData(aa,"",SJBH,YWLX);
		//查询附件信息
		queryFileData(aa,"","","");
	});
	$(function() {
		var savebtn= $("#save");
		var clear=$("#clear");
		clear.click(function(){
			$("#GhspForm").clearFormResult();
			$("input[type='checkbox']").prop("checked", false);
		});
		savebtn.click(function() {
					var data = Form2Json.formToJSON(GhspForm);
					//组成保存json串格式
					var data1 = defaultJson.packSaveJson(data);
					//调用ajax插入
					defaultJson.doUpdateJson(controllername + "?updateGhsp", data1,null);
					//从resultXML获取修改的数据
						var obj = $("#resultXML").val();		
		 				var parentmain=parent.$("body").manhuaDialog.getParentObj();			
		 				parentmain.update(obj);
						parent.$("body").manhuaDialog.close();
				});
	});
	function deleteDate(){
		$("#BJSJ").val('');
	};
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise"  hidden="true">
  <div class= "overFlowX">
	<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single">
		<thead>
 			<tr>
			</tr>
		</thead>
	<tbody></tbody>
	</table>
	</div>
	</div>
	<div style="height:5px;"></div>
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">规划信息
      <span class="pull-right">
          <button id="save" name="save" class="btn"  type="button">保存</button>
          <button id="clear" name="clear" class="btn"  type="button">清空</button>
      </span></h4>
     <form method="post" id="GhspForm"  >
      <table class="B-table">
        <TR  style="display:none;">
        <!-- <TR> -->
        	<TD><input type="text" class="span12" kind="text" id="XDKID" fieldname="XDKID" name="XDKID" keep="true"></TD>
	        <TD><input type="text" class="span12" kind="text" id="QQSXID" fieldname="QQSXID" name="QQSXID" keep="true"></TD>
			<TD><input type="text" class="span12" kind="text" id="GC_QQSX_GHSP_ID" fieldname="GC_QQSX_GHSP_ID" name="GC_QQSX_GHSP_ID" keep="true"></TD>
			<TD><input type="text" class="span12" kind="text" id="XMBH" fieldname="XMBH" name="XMBH" keep="true"></TD>
        </TR>
        <tr>
          <th width="12%" class="right-border bottom-border text-right">项目名称</th>

          <td width="42%" colspan="3" class="right-border bottom-border"><input class="span12"  type="text" readonly="ture" fieldname="XMMC" id="XMMC" name = "XMMC" check-type="maxlength" maxlength="200" keep="true"></td>
          <th width="12%" class="right-border bottom-border text-right">办结时间</th>
		  <td width="20%" class="right-border bottom-border" >
          	<input class="span12"  type="date" id="BJSJ" fieldname="BJSJ" name = "BJSJ" />
          </td>
        </tr>
        
        <tr>
        <th width="12%" class="right-border bottom-border text-right">存在问题</th>
          <td width="92%" colspan="5" class="bottom-border">
          <textarea rows="3" class="span12" id="CZWT" name = "CZWT" fieldname= "CZWT" check-type="maxlength" maxlength="4000"></textarea>
          </td>
        </tr>
        <tr>
        <th width="12%" class="right-border bottom-border text-right">不办理手续</th>
        <td width="88%" colspan="5" class="right-border bottom-border">
        <input class="span12" type="checkbox" placeholder="" id="BBLSX" name = "BBLSX"  kind="dic" src="GHSX" fieldname = "BBLSX" ></td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right">不办理原因</th>
          <td width="42%" colspan="5" class="bottom-border">
          <textarea rows="3" class="span12" id="BBLYY" name = "BBLYY" fieldname= "BBLYY" check-type="maxlength" maxlength="4000"></textarea>
          </td>
        </tr>
        <tr>
			<th width="8%" class="right-border bottom-border text-right">不办理原因附件</th>
			<td colspan="5" class="bottom-border right-border">
				<div>
				<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0103">
					<i class="icon-plus"></i>
						<span>添加文件...</span></span>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0103" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery">
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
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="resultXML" id = "resultXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>