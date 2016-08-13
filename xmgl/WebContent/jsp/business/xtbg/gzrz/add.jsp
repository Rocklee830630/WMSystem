<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@page import="java.sql.Blob"%>
<%
	String date = com.ccthanking.framework.util.Pub.getDate("yyyy-MM-dd");
	String id=request.getParameter("id");
	String xx=request.getParameter("xx");
	
	String nr = "";
	if(id != null) {
		String queryGgBlob = "select nr from XTBG_GZBG_GZRZ where DIARYID='"+id+"'";
		com.ccthanking.framework.common.QuerySet qs = com.ccthanking.framework.common.DBUtil.executeQuery(queryGgBlob, null);
		Blob dbBlob = (Blob)qs.getObject(1, "nr");
	   	if (dbBlob != null)
	   	{
	       int length = (int) dbBlob.length();
	       byte[] buffer = dbBlob.getBytes(1, length);
	       nr = new String(buffer,"GBK");
	   	}
	}
%>
<app:base />
<title>协同办公-工作报告-新增工作日志</title>
<script charset="UTF-8" src="${pageContext.request.contextPath }/js/kindeditor/kindeditor.js"></script>
<script charset="UTF-8" src="${pageContext.request.contextPath }/js/kindeditor/lang/zh_CN.js"></script>
<script charset="UTF-8" src="${pageContext.request.contextPath }/js/kindeditor/customKE.js"></script>
<script type="text/javascript" charset="UTF-8">

var controllername= "${pageContext.request.contextPath }/gzrzController.do";

var id = "<%=id %>";

//初始化加载
$(document).ready(function(){
	var obj=<%=xx %>;//json对象
	$("#demoForm").setFormValues(obj);

	$("#DIARYID").val(id);

	if(id == null || id == "null" || id == "indefined") {

		var date = "<%=date %>";
 		var json_fz='{"FBSJ":\"'+date+'\"}';
		var obj_fz=eval("("+json_fz+")");
		$("#demoForm").setFormValues(obj_fz);
		
		$("#RZLB").val("2");
	}  else {
		// 加载附件
		setFileData(id,"","","");
		// 查询附件
		queryFileData(id,"","","");
	}
});

// 清除按钮
	$(function() {
	$("#example_clear").click(function() {
		$("#demoForm").clearFormResult();
	});
});

//点击保存按钮
$(function() {
	var saveUserInfoBtn = $("#saveInfo");
	saveUserInfoBtn.click(function() {
		var nr = $("#NR").val();
		if(nr==null||nr==""){
			xAlert("提示信息","请输入工作日志内容",'3');
			return;
		}
		
		if($("#demoForm").validationButton()) {
			//生成json串
			var data = Form2Json.formToJSON(demoForm);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			//通过判断id是否为空来判断是插入还是修改
			
			var success = false;
			if(isUnique()) {
				if(id == null || id == "null" || id == "indefined") {
					var ywid = $("#ywid").val();
					success = defaultJson.doInsertJson(controllername + "?executeGzrz&operatorSign=1&ywid="+ywid, data1, null);
				} else {
					success = defaultJson.doUpdateJson(controllername + "?executeGzrz&operatorSign=2", data1, null);
				}
			} else {
				xFailMsg("工作日志发布日志重复，请重新填写","");
				return;
			}
			if(success){
				$(window).manhuaDialog.sendData();
				$(window).manhuaDialog.close();
			}
		}
	});
});

function isUnique() {
	var flag = false;
	$.ajax({
		url : controllername+"?queryUnique&date="+$("#FBSJ").val(),
		cache : false,
		async : false,
		dataType : 'json',
		success : function(response) {
			var result = eval("(" + response + ")");
			flag = result.sign == 0 ? true : flag;
		}
	});
	return flag;
}
</script>    
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">
      		<h4 class="title">
				<span class="pull-right">
					<button id="example_clear" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>
					<button id="saveInfo" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
				</span>    			
      		</h4>
     		<form method="post" id="demoForm"  >
      			<table class="B-table" width="100%" id="DT1">
					<tr>
						<th width="1%" class="right-border bottom-border text-right">日志类别</th>
						<td width="2%" class="right-border bottom-border">
							<select id="RZLB" class="span12" type="text" placeholder="必填" check-type="required" name="RZLB" fieldname="RZLB"  kind="dic" src="RZLB"></select>
						</td>
							
						<th width="1%" class="right-border bottom-border text-right">发布时间</th>
						<td width="8%" class="right-border bottom-border">
							<input id="FBSJ" class="span12" style="width: 20%" type="date" name="FBSJ" fieldname="FBSJ" fieldtype="date" fieldformat="YYYY-MM-DD"/>
						</td>
					</tr>
					
					<tr>
						<th class="right-border bottom-border text-right">内容</th>
						<td class="bottom-border" colspan="3" >
							<textarea class="span12" rows="12" placeholder="必填" cols="" id="NR" fieldname="NR"  name="NR"><%=nr %></textarea>
						</td>
					</tr>
					
					<tr>
			        	<th class="right-border bottom-border text-right">附件信息</th>
			        	<td colspan="3" class="bottom-border right-border">
							<div>
								<input type="hidden" name="ywid" id="ywid" value="">  
								<input type="hidden" name="DIARYID" id="DIARYID" fieldname="DIARYID">  
								<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="3003">
									<i class="icon-plus"></i>
									<span>添加文件...</span>
								</span>
								<table role="presentation" class="table table-striped">
									<tbody fjlb="3003" class="files showFileTab"
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
         <input type="hidden" id="queryResult"/>
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">		
 	</FORM>
 </div>
</body>
</html>