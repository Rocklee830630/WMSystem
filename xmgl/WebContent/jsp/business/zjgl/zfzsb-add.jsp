<!DOCTYPE html>
<%@page import="com.ccthanking.framework.util.DateTimeUtil"%>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>支付征收办-新增—修改—详细信息</title>
<%
	String type=request.getParameter("type");
	//获取当前用户信息
	String cDate = DateTimeUtil.getDate();
	
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	String userid = user.getAccount();
	String username = user.getName();
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">

var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglZszjZfzsbController.do";
var flag, jbrID;
var type ="<%=type%>";


//页面初始化
$(function() {
	init();
	
	<%
		if(!"detail".equals(type)){
	%>
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#demoForm").clearFormResult();
        $("#ZFRQ").val(new Date().toLocaleDateString());
    });
  	//按钮绑定事件（保存）
    $("#btnSave").click(function() {
    	
    	if($("#demoForm").validationButton())
		{
    		//生成json串
    		var data = Form2Json.formToJSON(demoForm);
		   	parent.$("body").manhuaDialog.setData(data);
			parent.$("body").manhuaDialog.sendData();//回调函数getWinData(data)
			parent.$("body").manhuaDialog.close();
		}else{
			requireFormMsg();
		  	return;
		}
    });
  	
    $("button[id^='blrBtn_']").bind("click", function(){
    	var valu = $(this).attr("value");
    	jbrID = valu;
    	var actorCode = $("#"+valu).attr("code");
		openUserTree('single',actorCode,'setBlr') ;
	});
    <%
		}else{
	%>
	//置所有input 为disabled
	$(":input").each(function(i){
	   $(this).attr("disabled", "true");
	 });
	<%
		}
	%>
	
	
	
});
//页面默认参数
function init(){
	if(type == "insert"){
		$("#ZFRQ").val('<%=cDate%>');
		$("#BLR").val('<%=username%>');
	}else if(type == "update" || type == "detail"){
		var tempJson;
		if(navigator.userAgent.indexOf("Firefox")>0) {
			var rowValue = $(parent.frames["menuiframe"]).contents().find("#resultXML").val();
			tempJson = eval("("+rowValue+")");
		}else{
			var rowValue = $(parent.frames["menuiframe"].document).find("#resultXML").val();
			tempJson = eval("("+rowValue+")");
		}
		//表单赋值
		$("#demoForm").setFormValues(tempJson);
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
	$("#"+jbrID).val(userName);
}
</script>    
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	</p>	
	<div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">
      		<h4 class="title">项目信息
		    	<span class="pull-right">
		      		<%
		      		if(!"detail".equals(type)){
		      			if(type.equals("insert")){ %>
		      			<button id="btnClear" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>
		      			<%} %>
		      		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
		      		<%} %>
		  		</span>
		    </h4>
     		<form method="post" id="demoForm">
      			<table class="B-table" width="100%" id="DT1">
      				<input type="hidden" id="YWLX" fieldname="YWLX" name="YWLX"/>
      				<input type="hidden" id="ID" fieldname="ID" name="ID"/>
      				
      				<tr>
						<th width="8%" class="right-border bottom-border text-right">支付日期</th>
						<td width="17%" class="right-border bottom-border">
							<input id="ZFRQ" class="span7" type="date" check-type="required" check-type="maxlength" maxlength="10" name="ZFRQ" fieldname="ZFRQ"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">支付金额</th>
						<td width="17%" class="right-border bottom-border">
							<input id="ZFJE" class="span9" keep="true" placeholder="必填" value=0 style="width:70%;text-align:right;" name="ZFJE" fieldname="ZFJE" type="number" />&nbsp;<b>(元)</b>
						</td>	
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">联系人</th>
						<td width="17%" class="right-border bottom-border">
							<input id="LXR" class="span12" style="width:80%;" check-type="maxlength" maxlength="36" name="LXR" fieldname="LXR" type="text" />
							<button class="btn btn-link"  type="button" id="blrBtn_LXR" value="LXR"><abbr title="点击选择联系人"><i class="icon-edit"></i></abbr></button>
						</td>
						<th width="8%" class="right-border bottom-border text-right">联系方式</th>
						<td width="17%" class="right-border bottom-border">
							<input id="LXFS" class="span12" check-type="maxlength" maxlength="40" name="LXFS" fieldname="LXFS" type="text" />
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">凭证编号</th>
						<td width="17%" class="right-border bottom-border">
							<input id="PZBH" class="span12" check-type="maxlength" maxlength="100" name="PZBH" fieldname="PZBH" type="text" />
						</td>
						<th width="8%" class="right-border bottom-border text-right">办理人</th>
						<td width="17%" class="right-border bottom-border">
							<input id="BLR" class="span12" style="width:80%;" check-type="maxlength" maxlength="36" name="BLR" fieldname="BLR" type="text" />
							<button class="btn btn-link"  type="button" id="blrBtn_BLR" value="BLR"><abbr title="点击选择办理人"><i class="icon-edit"></i></abbr></button>
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">备注</th>
						<td width="92%" colspan="7" class="bottom-border">
							<textarea class="span12" rows="2" id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ"></textarea>
						</td>
					</tr>
     		</table>
      	</form>
    </div>
  </div>
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id="frmPost">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id="queryXML"/>
		<input type="hidden" name="txtXML" id="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" fieldname="ND" id="txtFilter"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<input type="hidden" id="queryResult"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>		
	</FORM>
</div>
<form method="post" id="queryForm">
	<!--可以再此处加入hidden域作为过滤条件 -->
	<TR style="display: none;">
		<TD class="right-border bottom-border"></TD>
		<TD class="right-border bottom-border">
			<INPUT type="hidden" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
		</TD>
	</TR>	
</form>
</body>
</html>