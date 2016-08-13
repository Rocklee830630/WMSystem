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
<title>拨付征收办-新增—修改—详细信息</title>
<%
	String type=request.getParameter("type");
	String qy = request.getParameter("qy");
	if(qy==null)
	    qy = "";
	//获取当前用户信息
	//String cDate = DateTimeUtil.getDate();
	
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	String userid = user.getAccount();
	String username = user.getName();
	String cDate = Pub.getDate("yyyy-MM-dd");
	String cYear = Pub.getDate("yyyy");
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">

var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglZszjZfzsbController.do";
var controllernameqCommon= "${pageContext.request.contextPath }/zjgl/gcZjglZszjXmzszjqkController.do";
var flag, jbrID;
var type ="<%=type%>", qy="<%=qy%>";
var retMsg = "";
var popPage;

//页面初始化
$(function() {
	init();
	
	<%
		if(!"detail".equals(type)){
	%>
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#ID").val('');
        //$("#BLR").val('<%=username%>');
        $("#ZFJE").val("0");
        $("#ZFRQ").val('<%=cDate%>');
        
        $("#JBR").val('<%=username%>');
		$("#JBR").attr("code",'<%=userid%>');
		$("#BFDW").val('市征收办');
    });
	
    $("#btnClear_q").click(function() {
        $("#QZFRQ").val('');
        $("#QQY").val('');
        $("#ZFRQB").val('');
        $("#ZFRQE").val('');
        
        $("#JBR").val('<%=username%>');
		$("#JBR").attr("code",'<%=userid%>');
		$("#BFDW").val('市征收办');
    });
    
  	//按钮绑定事件（保存）
	$("#btnSave").click(function() {
    	if($("#demoForm").validationButton())
		{
    		//生成json串
    		var data = Form2Json.formToJSON(demoForm);
		  	//组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  	//调用ajax插入
		    if($("#ID").val() == "" || $("#ID").val() == null){
		    	var flag = defaultJson.doInsertJson(controllername + "?insert", data1);
		    	if(flag){
		    		
		    		retMsg = "showMsg";
		    		$(window).manhuaDialog.close();
		    		return;
		    		
		    		var json=$("#resultXML").val();
		    		var tempJson=eval("("+json+")");
		    		var resultobj=tempJson.response.data[0];
		    		//$("#demoForm").setFormValues(resultobj);
		    	}
    		}else{
    			defaultJson.doUpdateJson(controllername + "?update", data1, DT1);
    		}
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
	
	$("#btnQuery").click(function() {
		var kssj = $("#ZFRQB").val();
		var jssj = $("#ZFRQE").val();
		if(kssj!="" && jssj!="" && kssj > jssj){
			alert("<结束时间>不能早于<开始时间>");
			return false;
		}
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		var flag = defaultJson.doQueryJsonList(controllername+"?query",data,DT1);
	});
	
});
//页面默认参数
function init(){
	
	if(qy!="" && type=="insert"){
		//根据jhsjid (qy)  查询项目信息
		//$("#QJHSJID").val(qy);
		$.ajax({
			url : controllernameqCommon+"?queryCommon&opttype=xmbd&qc_jhsjid="+qy,
			cache : false,
			async :	false,
			dataType : "json",  
			type : 'post',
			success : function(response) {
				var resultmsgobj  = convertJson.string2json2(response.msg);
				if(resultmsgobj.GC_JH_SJ_ID==""){
					$("#JHSJID").val("");
				}else{
					$("#JHSJID").val(resultmsgobj.GC_JH_SJ_ID);
				}
				if(resultmsgobj.XMMC==""){
					$("#XMMC").val("");
				}else{
					$("#XMMC").val(resultmsgobj.XMMC);
				}
				if(resultmsgobj.BDMC==""){
					$("#BDMC").val("");
				}else{
					$("#BDMC").val(resultmsgobj.BDMC);
				}
			}
		});
		
	}
	
	if(type == "insert"){
		$("#ZFRQ").val('<%=cDate%>');
		$("#QZFRQ").val("<%=cYear%>");
		
		$("#JBR").val('<%=username%>');
		$("#JBR").attr("code",'<%=userid%>');
		
		$("#BFDW").val('市征收办');
	}else if(type == "update" || type == "detail"){
		
	}
}

function tr_click(obj,tabListid){
	//alert(JSON.stringify(obj));
	$("#demoForm").setFormValues(obj);
	$("#btnClear").attr("disabled",false);
}

//弹出区域回调
getWinData = function(data){
	//alert(popPage);
	if(popPage == "selectYh") {
		$("#DFYH").val(JSON.parse(data).DICT_NAME);
		$("#DFYH").attr("code",JSON.parse(data).DICT_ID);
	}else if(popPage == "2") {
		//$("#DFYH").val(JSON.parse(data).DWMC);
		$("#JNDW").val(JSON.parse(data).DWMC);
	    $("#JNDW").attr("code",JSON.parse(data).GC_CJDW_ID);
	}else if(popPage == "bd"){
		var jhsjid_val = JSON.parse(data).GC_JH_SJ_ID;
		$("#XMMC").val(JSON.parse(data).XMMC);
		$("#BDMC").val(JSON.parse(data).BDMC);
		$("#JHSJID").val(jhsjid_val);
	}else if(popPage == "tqk"){
		$("#XMMC").val(JSON.parse(data).XMMC);
		$("#BDMC").val(JSON.parse(data).BDMC);
		$("#QKMC").val(JSON.parse(data).QKMC);
		$("#JHSJID").val(JSON.parse(data).JHSJID);
		$("#BKXGDID").val(JSON.parse(data).TQKMXID);
		$("#ZFJE").val(JSON.parse(data).JCHDZ);
		
	}
	
};

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
	$("#"+jbrID).attr("code",userId);
}


//选择项目标段
function selectBb(){
	popPage = "bd";
	$(window).manhuaDialog({"title":"项目列表","type":"text","content": "${pageContext.request.contextPath}/jsp/business/gcb/kfglgl/xmcx.jsp","modal":"2"});
}

//选择已拔付提请款
function selectTqk(){
	popPage = "tqk";
	$(window).manhuaDialog({"title":"提请款列表","type":"text","content": "${pageContext.request.contextPath}/jsp/business/zjgl/tqk-list.jsp?jhsjid="+qy,"modal":"2"});
<%--$(window).manhuaDialog({"title":"提请款列表","type":"text","content": "${pageContext.request.contextPath}/jsp/business/zjgl/tqk-list.jsp","modal":"2"});--%>
}

//按钮绑定事件（导出EXCEL）
$("#btnExpExcel").click(function() {
	 //var t = $("#DT1").getTableRows();
	// if(t<=0)
	 //{
	//	 xAlert("提示信息","请至少查询出一条记录！");
	//	 return;
	 //}
  	 $(window).manhuaDialog({"title":"导出","type":"text","content":"${pageContext.request.contextPath }/jsp/framework/print/TabListEXP.jsp?tabId=DT1","modal":"3"});
});

function closeNowCloseFunction(){
	return retMsg;
}
</script>    
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	</p>	
	<div class="row-fluid">
	 	
	 	<div style="height:5px;"> </div>	
	 	
    	<div class="B-small-from-table-autoConcise">
      		<h4 class="title">拨付信息
		    	<span class="pull-right">
		      		<%
		      		if(!"detail".equals(type)){
		      			if(type.equals("insert")){ %>
<%--		      			<button id="btnClear" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>新拔付</button>--%>
		      			<%} %>
		      		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
		      		<%} %>
		  		</span>
		    </h4>
     		<form method="post" id="demoForm">
      			<table class="B-table" width="100%" id="DTForm">
      				<input type="hidden" id="ID" fieldname="ID" name="ID"/>
      				<input type="hidden" id="JHSJID" name="JHSJID"  fieldname="JHSJID" >
      				<input type="hidden" id="BKXGDID" name="BKXGDID"  fieldname="BKXGDID" >
      				
      				<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">请款名称</th>
						<td width="17%" class="right-border bottom-border">
							<input id="QKMC"  class="span12" style="width:85%"  check-type="maxlength" maxlength="100"  name="QKMC" fieldname="QKMC" type="text" disabled/>
							<button class="btn btn-link"  type="button" onclick="selectTqk()"><i class="icon-edit"></i></button>
						</td>
						<td class="right-border bottom-border" colspan="2"></td>
					</tr>
      				 <tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
						<td width="25%" class="right-border bottom-border">
							<input id="XMMC" class="span12" placeholder="必填" check-type="required" style="width:85%" name="XMMC" fieldname="XMMC" type="text" disabled/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
						<td width="25%" class="right-border bottom-border">
							<input id="BDMC" class="span12" name="BDMC" style="width:85%" fieldname="BDMC" type="text" disabled/>
<%--							<button class="btn btn-link"  type="button" onclick="queryProjectWithBD()"><i class="icon-edit"></i></button>--%>
								<button class="btn btn-link"  type="button" onclick="selectBb()"><i class="icon-edit"></i></button>
						</td>
					</tr>
      				<tr>
      					<th width="8%" class="right-border bottom-border text-right disabledTh">拔付单位</th>
						<td width="17%" class="right-border bottom-border">
							<input id="BFDW"  class="span12" check-type="maxlength" maxlength="100"  name="BFDW" fieldname="BFDW" type="text"  readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">拨付日期</th>
						<td width="17%" class="right-border bottom-border">
							<input id="ZFRQ" class="span7" type="date" check-type="required" check-type="maxlength" maxlength="10" name="ZFRQ" fieldname="ZFRQ"/>
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">拨付金额</th>
						<td width="17%" class="right-border bottom-border">
							<input id="ZFJE" class="span9" keep="true" placeholder="必填" value=0 style="width:70%;text-align:right;" name="ZFJE" fieldname="ZFJE" type="number" min="0" />&nbsp;<b>(元)</b>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">经办人</th>
						<td width="25%" class="right-border bottom-border">
							<input id="JBR"   class="span12" style="width:80%;" check-type="maxlength" maxlength="36"  name="JBR" fieldname="JBR" type="text" readOnly/>
							<button class="btn btn-link"  type="button" id="blrBtn_JBR" value="JBR" title="点击选择经办人"><i class="icon-edit"></i></button>
						</td>
					</tr>
<%--					<tr>--%>
<%--						<th width="8%" class="right-border bottom-border text-right">联系人</th>--%>
<%--						<td width="17%" class="right-border bottom-border">--%>
<%--							<input id="LXR" class="span12" style="width:80%;" check-type="maxlength" maxlength="36" name="LXR" fieldname="LXR" type="text" />--%>
<%--							<button class="btn btn-link"  type="button" id="blrBtn_LXR" value="LXR"><abbr title="点击选择联系人"><i class="icon-edit"></i></abbr></button>--%>
<%--						</td>--%>
<%--						<th width="8%" class="right-border bottom-border text-right">联系方式</th>--%>
<%--						<td width="17%" class="right-border bottom-border">--%>
<%--							<input id="LXFS" class="span12" check-type="maxlength" maxlength="40" name="LXFS" fieldname="LXFS" type="text" />--%>
<%--						</td>--%>
<%--					</tr>--%>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">凭证编号</th>
						<td width="17%" class="right-border bottom-border">
							<input id="PZBH" class="span12" check-type="maxlength" maxlength="100" name="PZBH" fieldname="PZBH" type="text" />
						</td>
						<td class="right-border bottom-border" colspan="2"></td>
<%--						<th width="8%" class="right-border bottom-border text-right">区域</th>--%>
<%--						<td width="17%" class="right-border bottom-border">--%>
<%--							<select  id="QY"  class="span12"  style="width:60%;" name="QY" fieldname="QY"  operation="=" kind="dic" src="QY"  defaultMemo="-全部-">--%>
<%--						</td>--%>
<%--						<th width="8%" class="right-border bottom-border text-right">办理人</th>--%>
<%--						<td width="17%" class="right-border bottom-border">--%>
<%--							<input id="BLR" class="span12" style="width:80%;" check-type="maxlength" maxlength="36" name="BLR" fieldname="BLR" type="text" />--%>
<%--							<button class="btn btn-link"  type="button" id="blrBtn_BLR" value="BLR"><abbr title="点击选择办理人"><i class="icon-edit"></i></abbr></button>--%>
<%--						</td>--%>
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
		<input type="hidden" name="txtFilter" order="desc" fieldname="t.LRSJ" id="txtFilter"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<input type="hidden" id="queryResult"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>		
	</FORM>
</div>
</body>
</html>