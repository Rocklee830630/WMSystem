<!DOCTYPE html>
<%@page import="java.sql.Blob"%>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%
	String date = com.ccthanking.framework.util.Pub.getDate("yyyy-MM-dd");
	String id=request.getParameter("id");
	String xx=request.getParameter("xx");
	String nr = "";
	if(id != null) {
		String queryGgBlob = "select nr from XTBG_XXZX_GGTZ where ggid='"+id+"'";
		
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
<title>协同办公-信息中心-新增公告</title>
<meta charset="utf-8" />
<script charset="UTF-8" src="${pageContext.request.contextPath }/js/kindeditor/kindeditor.js"></script>
<script charset="UTF-8" src="${pageContext.request.contextPath }/js/kindeditor/lang/zh_CN.js"></script>
<script charset="UTF-8" src="${pageContext.request.contextPath }/js/kindeditor/customKE.js"></script>
<script type="text/javascript" charset="UTF-8">

var controllername= "${pageContext.request.contextPath }/ggtzController.do";

var id="<%=id %>";

//初始化加载
$(document).ready(function(){
	var obj=<%=xx %>;//json对象
	//var nr = ''
	// 默认是对部门发送
	$("#IS_TO_PERSON").val("0");

	formEditor.init("kindEditor");
	if(id == null || id == "null" || id == "undefined") {
		var date = "<%=date %>";
 		var json_fz='{"FBSJ":\"'+date+'\"}';
		var obj_fz=eval("("+json_fz+")");
	//	alert(json_fz);
		$("#demoForm").setFormValues(obj_fz);
		
		// 
		$("#GGLB").val("GG");
	} else {
		// 加载附件
		setFileData(id,"","","");
		// 查询附件
		queryFileData(id,"","","");
		
		// 修改通知公告需要加载已有信息
		$("#demoForm").setFormValues(obj);
		$("input:radio[name=scope][value="+obj.IS_TO_PERSON+"]").attr("checked",true);
		$("#GGID").val(id);
		$("#kindEditor").val($("#NR").val());
		
		if(obj.IS_TO_PERSON == 0) {
			$("#person").hide();
			// 选择对人发送时，设置fieldname属性，并且值为0
			$("#IS_TO_PERSON").val("0");
			$("#dept").show();
		} else {
			$("#dept").hide();
			$("#person").show();
			// 选择对人发送时，设置fieldname属性，并且值为1
			$("#IS_TO_PERSON").val("1");
			

			$("#FBFWMC2").attr("code",obj.FBFW);
			$("#FBFWMC2").val(obj.FBFWMC);
		}
	}
});

//点击保存按钮
$(function() {
	var saveUserInfoBtn = $("#saveInfo");
	saveUserInfoBtn.click(function() {
		$("#NR").val(formEditor.htmlVal());
		var nr = $("#NR").val();
		if(nr==null||nr==""){
			xAlert("提示信息","请输入通知公告内容",'3');
			return;
		}
		var toPerson = $("#FBFWMC2").val();
		if($("#person").is(":visible") && (toPerson==null || toPerson=="")) {

			xAlert("提示信息","请选择发送人员",'3');
			return;
		}
		
		
		if($("#demoForm").validationButton()) {
			//生成json串
			var data = Form2Json.formToJSON(demoForm);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			//通过判断id是否为空来判断是插入还是修改
			
			var success = false;
			var ywid = $("#ywid").val();
			var jsry = $("#FBFWMC2").attr("code");
			var jsrmc = $("#FBFWMC2").val();
			if(id == null || id == "null" || id == "indefined") {
				success = defaultJson.doInsertJson(controllername + "?executeGg&operatorSign=1&ywid="+ywid+"&jsry="+jsry+"&jsrmc="+jsrmc, data1, null);
			} else {
				success = defaultJson.doUpdateJson(controllername + "?executeGg&operatorSign=2&ywid="+ywid+"&jsry="+jsry+"&jsrmc="+jsrmc, data1, null);
			}
			if(success){
				$(window).manhuaDialog.setData("");
				$(window).manhuaDialog.sendData();
				$(window).manhuaDialog.close();
			} 
		}
	});
	var saveAndPublishInfo = $("#saveAndPublishInfo");
	saveAndPublishInfo.click(function() {
		$("#NR").val(formEditor.htmlVal());
		var nr = $("#NR").val();
		if(nr==null||nr==""){
			xAlert("提示信息","请输入通知公告内容",'3');
			return;
		}
		var toPerson = $("#FBFWMC2").val();
		if($("#person").is(":visible") && (toPerson==null || toPerson=="")) {

			xAlert("提示信息","请选择发送人员",'3');
			return;
		}
		
		xConfirm("提示信息","是否确认将此公告呈请部门领导审核！");
		$('#ConfirmYesButton').unbind();
	 	$('#ConfirmYesButton').one("click",function(obj) {
			if($("#demoForm").validationButton()) {
				//生成json串
				var data = Form2Json.formToJSON(demoForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//通过判断id是否为空来判断是插入还是修改
				
				var success = false;
				var ywid = $("#ywid").val();
				var jsry = $("#FBFWMC2").attr("code");
				var jsrmc = $("#FBFWMC2").val();
				if(id == null || id == "null" || id == "indefined") {
					success = defaultJson.doInsertJson(controllername + "?executeGg&operatorSign=1&ywid="+ywid+"&sffb=1&jsry="+jsry+"&jsrmc="+jsrmc, data1, null);
				} else {
					success = defaultJson.doUpdateJson(controllername + "?executeGg&operatorSign=2&sffb=1&ywid="+ywid+"&jsry="+jsry+"&jsrmc="+jsrmc, data1, null);
				}
				if(success){
					$(window).manhuaDialog.setData("");
					$(window).manhuaDialog.sendData();
					$(window).manhuaDialog.close();
				} 
			}
		});
		

	});
	
});

//选择部门
function selectDept(){
	var o_qy=$("#FBFW").val();
/* 	if($("#hideFBFW").val()==""){
		o_qy="";
	}else{
		o_qy=$("#hideFBFW").val();
	} */
	$(window).manhuaDialog({"title":"机构选择","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/xxzx/gg/selectDept.jsp?o_qy="+o_qy,"modal":"4"});
}

//弹出区域回调
getWinData=function(data){
	$("#FBFWMC").val(data.qyText);
	$("#FBFW").val(data.qyValue);
//	alert();
};


function selectScope() {
	var value = $("input:radio[name=scope]:checked").val();
//	var value = $("#scope")
	if(value == 0) {
		$("#person").hide();
		// 默认是对部门发送，把IS_TO_PERSON这个fieldname属性去掉
		$("#IS_TO_PERSON").val("0");
		$("#dept").show();
		$("#FBFWMC2").attr("code","");
		$("#FBFWMC2").val("");
		$("#FBFWMC").val("");
		
	} else {
		$("#dept").hide();
		$("#person").show();
		// 选择对人发送时，设置fieldname属性，并且值为1
		$("#IS_TO_PERSON").val("1");
	}
}


function setBlr(data){
	var userId = "";
	var userName = "";
	for(var i=0;i<data.length;i++){
 	 var tempObj =data[i];
 	 if(i<data.length-1){
	  userId +=tempObj.ACCOUNT+",";
	  userName +=tempObj.USERNAME+",";
 	 }else{
 	  userId +=tempObj.ACCOUNT;
 	  userName +=tempObj.USERNAME; 
 	 }
	}
	$("#FBFWMC2").val(userName);
	$("#FBFWMC2").attr("code",userId);
}

function selectPerson() {
	var actorCode = $("#FBFWMC2").attr("code");
	selectUserTree('multi',actorCode,'setBlr') ;
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
					<button id="saveInfo" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
					<button id="saveAndPublishInfo" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存并呈请审核</button>
				</span>    			
      		</h4>
     		<form method="post" id="demoForm"  >
      			<table class="B-table" width="100%" id="DT1">
      			<TR  style="display:none;">
	               <TD class="right-border bottom-border"></TD>
			       <TD class="right-border bottom-border"><input type="text" class="span12" kind="text" id="SJBH"  name="SJBH" fieldname="SJBH"  value=""  ></TD>
			       	               <TD class="right-border bottom-border"></TD>
			       <TD class="right-border bottom-border"><input type="text" class="span12" kind="text" id="YWLX"  name="YWLX" fieldname="YWLX"  value=""  ></TD>
                </TR>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">标题</th>
						<td class="right-border bottom-border" colspan="7">
							<input id="GGBT" style="width:70%" class="span12" type="text"  placeholder="必填" check-type="required"  name="GGBT" maxlength="100" fieldname="GGBT"/>
						</td>
						</tr>
					
					<tr>
						<th width="5%" class="right-border bottom-border text-right">选择发布范围</th>
						<td width="20%" class="right-border bottom-border">
						 <input class="span12" type="radio" placeholder=""  name = "scope" id="scope"  kind="dic" src="E#0=部门:1=人员" onclick="selectScope()">

							<!-- 部门<input type="radio" id="scope" name="scope" value="1" onclick="selectScope()" checked="checked">&nbsp;&nbsp;&nbsp;&nbsp;
							人员<input type="radio" id="scope" name="scope" value="2" onclick="selectScope()"> -->
						</td>
						
						<th width="5%" class="right-border bottom-border text-right">类别</th>
						<td width="20%" class="right-border bottom-border">
							<select  class="span12" style="width: 40%" placeholder="必填" check-type="required" kind="dic"  src="GGLB" id="GGLB" fieldname="GGLB" name="GGLB"></select>
						</td>
						
						<th width="5%" class="right-border bottom-border text-right">发布时间</th>
						<td width="20%" class="right-border bottom-border">
							<input class="span12" style="width: 60%" placeholder="必填" check-type="required" type="date" name="FBSJ"  fieldname="FBSJ" id="FBSJ" fieldtype="date" fieldformat="YYYY-MM-DD"/></td>
					
						<td width="25%"></td>
					</tr>
					
					<tr id="dept">
				        <th width="8%" class="right-border bottom-border text-right">发布范围</th>
	        			<td class="right-border bottom-border" colspan="7">
	        				<input class="span12" style="width:70%" placeholder="所有部门" id="FBFWMC" type="text" fieldname="FBFWMC" name="FBFWMC" maxlength="512" readonly="readonly"/>
	        				<input id="FBFW" type="hidden" fieldname="FBFW" name="FBFW" maxlength="512" readonly="readonly"/>
				        	<button class="btn btn-link"  type="button" onclick="selectDept();" title="请选择部门"><i class="icon-edit"></i></button>
	       				 </td>
					</tr>
					
					<tr id="person" style="display: none;">
				        <th width="8%" class="right-border bottom-border text-right">发布范围</th>
	        			<td class="right-border bottom-border" colspan="7">
	        				<input class="span12" style="width:70%" placeholder="选择人员" id="FBFWMC2" type="text" name="FBFWMC" maxlength="512" readonly="readonly"/>
	        				<input id="IS_TO_PERSON" type="hidden" fieldname="IS_TO_PERSON" name="IS_TO_PERSON" maxlength="512" value="0"/>
				        	<button class="btn btn-link"  type="button" onclick="selectPerson();" title="选择人员"><i class="icon-edit"></i></button>
	       				 </td>
					</tr>
					
					<tr>
						<th width="8%" class="right-border bottom-border text-right">内容</th>
						<td width="92%" colspan="7" class="bottom-border">
							<textarea class="span12" name="kindEditor"  id="kindEditor" maxlength="4000" style="width:100%;height:300px;visibility:hidden;"><%=nr %></textarea>
						</td>
					</tr>
					
					<tr>
			        	<th width="8%" class="right-border bottom-border text-right">附件信息</th>
			        	<td colspan="7" class="bottom-border right-border">
							<div>
								<input type="hidden" name="ywid" id="ywid" value="">  
								<input type="hidden" name="GGID" id="GGID" fieldname="GGID">  
								<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="3001">
									<i class="icon-plus"></i>
									<span>添加文件...</span>
								</span>
								<textarea maxlength="4000" fieldname="NR" id="NR" name="NR" style="visibility: hidden;"><%=nr %></textarea>
								<table role="presentation" class="table table-striped">
									<tbody fjlb="3001" class="files showFileTab"
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