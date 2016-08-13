<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%
	String id=request.getParameter("id");
	String xx=request.getParameter("xx") == null ? "" : request.getParameter("xx");
	String flag = request.getParameter("flag")==null?"":request.getParameter("flag");
%>
<app:base />
<title>协同办公-新增内部短信</title>
<script type="text/javascript" charset="UTF-8">

var controllername= "${pageContext.request.contextPath }/nbdxController.do";

var id="<%=id %>";
var p_flag = '<%=flag%>';
//初始化加载
$(document).ready(function(){
	var obj=<%="".equals(xx) ? null : xx %>;//json对象
	$("#demoForm").setFormValues(obj);
	$("#OPID").val(id);
	if(id != "null") {
		$("#userTr").css("display", "none");
		$("#example_clear").css("display", "none");
	}
	if(p_flag!=""){
		setDefaultValue();
	}
});
function setDefaultValue(){
	var titleMsg = "";
	var contentMsg = "";
	if(p_flag=="SJWJGL"){
		var parentObj = $($(window).manhuaDialog.getParentObj().document);
		var tzlbStr = parentObj.find("#TZLB option:selected").text();
		var xmmcStr = parentObj.find("#XMMC").val();
		titleMsg = xmmcStr+"["+tzlbStr+"]已接收，请尽快领取！";
		$("#TITLE").val(titleMsg);
	}if(p_flag=="SJBGGL"){
		var parentObj = $($(window).manhuaDialog.getParentObj().document);
		var tzlbStr = parentObj.find("#BGLB option:selected").text();
		var xmmcStr = parentObj.find("#XMMC").val();
		var bgnrStr = parentObj.find("#BGNR")[0].value;
		titleMsg = xmmcStr+"["+tzlbStr+"]设计发生变更，请及时领取图纸！";
		$("#TITLE").val(titleMsg);
		$("#CONTENT").val(bgnrStr);
	}
}
//点击保存按钮
$(function() {
	var saveUserInfoBtn = $("#saveInfo");
	saveUserInfoBtn.click(function() {
		if($("#demoForm").validationButton()) {
			//生成json串
			var data = Form2Json.formToJSON(demoForm);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			//通过判断id是否为空来判断是插入还是修改
			
			var success = false;
			if(id == null || id == "null" || id == "undefined") {
				success = defaultJson.doInsertJson(controllername + "?executeNbdx&operatorSign=1", data1, null);
			} else {
				success = defaultJson.doUpdateJson(controllername + "?executeNbdx&operatorSign=2", data1, null);
			}
			if(success){
				var data2 = $("#frmPost").find("#resultXML").val();
				$(window).manhuaDialog.setData(data2);
				$(window).manhuaDialog.sendData();
				$(window).manhuaDialog.close();
			} 
		} else {
			requireFormMsg();
		  	return;
		}
	});
});


//父页面调用查询页面的方法
$(function() {
	$("#receiverId").click(function() {
	//	$(window).manhuaDialog({"title" : "信息中心>内部短信>显示人员列表","type" : "text","content" : "${pageContext.request.contextPath}/jsp/business/wttb/showPsnList.jsp","modal":"2"});

		var actorCode = $("#USERTONAME").attr("code");
		openUserTree('check', actorCode, 'getWinData');
	});
});

//父页面接受值方法，接收短信人员的信息
function getWinData(data) {
	 
	var userId = "";
	var userName = "";
	for(var i = 0 ; i < data.length ; i++) {
 	 var tempObj =data[i];
 	 if(i < data.length - 1) {
	  userId +=tempObj.ACCOUNT+",";
	  userName +=tempObj.USERNAME+",";
 	 } else {
 	  userId +=tempObj.ACCOUNT;
 	  userName +=tempObj.USERNAME; 
 	 }
	}
	$("#USERTONAME").val(userName);
	$("#USERTONAME").attr("code",userId);
	$("#USERTO").val(userId);//为字段的JSR赋值
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
					<button id="saveInfo" class="btn" type="button">发送</button>
					<!-- 
					<button id="example_clear" class="btn" type="button">清空</button>
					 -->
				</span>    			
      		</h4>
     		<form method="post" id="demoForm"  >
      			<table class="B-table" width="100%" id="DT1">
					<tr>
						<th width="8%" class="right-border bottom-border text-right">标题</th>
						<td class="right-border bottom-border">
							<input id="TITLE" class="span12" type="text" style="width:90%" placeholder="必填" check-type="required maxlength" name="TITLE" maxlength="100" fieldname="TITLE"/>
							<input id="OPID" class="span12" type="hidden" name="OPID" maxlength="100" fieldname="OPID"/>
						</td>
							
					</tr>
					
					<tr id="userTr">							
				        <th width="8%" class="right-border bottom-border text-right">接收人</th>
	        			<td class="right-border bottom-border" >
	        				<input class="span12" readonly="readonly" style="width:90%" id="USERTONAME" type="text" placeholder="必填" check-type="required" fieldname="USERTONAME" name="USERTONAME" />
	        				<input type="hidden" name="USERTO" fieldname="USERTO" id="USERTO" />
	        				<button class="btn btn-link" type="button" id="receiverId" title="点击选择接收人"><i class="icon-edit"></i></button>
	       				 </td>
						
					</tr>
					
					<tr>
						<th width="8%" class="right-border bottom-border text-right">内容</th>
						<td class="bottom-border">
							<textarea class="span12" rows="7" cols="" id="CONTENT" maxlength="2000" placeholder="必填" check-type="required maxlength"  fieldname="CONTENT"  name="CONTENT"></textarea>
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
 	<form method="post"  id="queryForm">
	<!--可以再此处加入hidden域作为过滤条件 -->
	<TR style="display: none;">
		<TD class="right-border bottom-border"></TD>
		<TD class="right-border bottom-border">
			<INPUT type="hidden" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
		</TD>
	</TR>
 		
 	</form>
	<table  id="DT2"  style="display;none;" ></table>
</body>
</html>