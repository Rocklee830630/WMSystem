<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%
	String id=request.getParameter("id");
	String xx=request.getParameter("xx");
	
	
	String nr = "";
	if(id != null) {
		String sql = "SELECT NR FROM XTBG_XXZX_ZXXW T WHERE NEWSID='" + id + "'";
		com.ccthanking.framework.common.QuerySet qs = com.ccthanking.framework.common.DBUtil.executeQuery(sql, null);
		java.sql.Blob dbBlob = (java.sql.Blob)qs.getObject(1, "NR");
		if(dbBlob != null) {
			int length = (int)dbBlob.length();
			byte[] buffer = dbBlob.getBytes(1, length);
			nr = new String(buffer, "GBK");
		}
	}
%>
<app:base />
<meta charset="utf-8" />
<title>协同办公-信息中心-新增新闻</title>
<script charset="UTF-8" src="${pageContext.request.contextPath }/js/kindeditor/kindeditor.js"></script>
<script charset="UTF-8" src="${pageContext.request.contextPath }/js/kindeditor/lang/zh_CN.js"></script>
<script charset="UTF-8" src="${pageContext.request.contextPath }/js/kindeditor/customKE.js"></script>
<script type="text/javascript" charset="UTF-8">

var controllername= "${pageContext.request.contextPath }/zxxwController.do";

var id="<%=id %>";
var editor;

//初始化加载
$(document).ready(function(){
	var obj=<%=xx %>;//json对象
	$("#demoForm").setFormValues(obj);

	$("#NEWSID").val(id);

	$("#kindEditor").val($("#NR").val());
	formEditor.init("kindEditor");
	
	
	if(id == null || id == "null" || id == "indefined") {
		
	}  else {
		// 加载附件
		setFileData(id,"","","");
		// 查询附件
		queryFileData(id,"","","");
	}
});

//点击保存按钮
$(function() {
	var saveUserInfoBtn = $("#saveInfo");
	saveUserInfoBtn.click(function() {
		$("#NR").val(formEditor.htmlVal());
		var nr = $("#NR").val();
		if(nr==null||nr==""){
			xAlert("提示信息","请输入中心新闻内容",'3');
			return;
		}
		
		
		if($("#demoForm").validationButton()) {
			//生成json串
			var data = Form2Json.formToJSON(demoForm);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			//通过判断id是否为空来判断是插入还是修改
			
			var success = false;
			if(id == null || id == "null" || id == "indefined") {
				var ywid = $("#ywid").val();
				success = defaultJson.doInsertJson(controllername + "?executeXw&operatorSign=1&ywid="+ywid, data1, null);
			} else {
				success = defaultJson.doUpdateJson(controllername + "?executeXw&operatorSign=2", data1, null);
			}
			if(success){
				$(window).manhuaDialog.setData("");
				$(window).manhuaDialog.sendData();
				$(window).manhuaDialog.close();
			} 
		}
	});
});
</script>    
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">
      		<h4 class="title">
				<span class="pull-right">
					<button id="saveInfo" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
				</span>    			
      		</h4>
     		<form method="post" id="demoForm"  >
      			<table class="B-table" width="100%" id="DT1">
					<tr>
						<th width="8%" class="right-border bottom-border text-right">标题</th>
						<td width="58%" class="right-border bottom-border">
							<input id="XWBT" class="span12" type="text"  placeholder="必填" check-type="required"  name="XWBT" maxlength="270" fieldname="XWBT"/>
						</td>
							
					</tr>
					
					<tr>
						<th width="8%" class="right-border bottom-border text-right">内容</th>
						<td width="92%" colspan="3" class="bottom-border">
							<textarea class="span12" name="kindEditor" id="kindEditor" style="width:100%;height:300px;visibility:hidden;"><%=nr %></textarea>
						</td>
					</tr>
					
					<tr>
			        	<th width="8%" class="right-border bottom-border text-right">标题缩略图</th>
			        	<td colspan="3" class="bottom-border right-border">
							<div>
								<input type="hidden" name="ywid" id="ywid" value="">  
								<input type="hidden" name="NEWSID" id="NEWSID" fieldname="NEWSID">  
								<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="3002">
									<i class="icon-plus"></i>
									<span>添加文件...</span>
								</span>
								<textarea maxlength="4000" fieldname="NR" id="NR" name="NR" style="visibility: hidden;"><%=nr %></textarea>
								<table role="presentation" class="table table-striped">
									<tbody fjlb="3002" class="files showFileTab"
										data-toggle="modal-gallery" data-target="#modal-gallery">
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
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id="frmPost">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id="queryXML">
         <input type="hidden" name="txtXML" id="txtXML">
         <input type="hidden" name="txtFilter" order="desc" fieldname="ND" id="txtFilter">
         <input type="hidden" name="resultXML" id="resultXML">
         <input type="hidden" id="queryResult" name="queryResult"/>
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">		
 	</FORM>
 </div>
</body>
</html>