<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>项目征收资金情况-维护</title>
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
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglZszjXmzszjqkController.do";
var jbrID;
var type ="<%=type%>", qy="<%=qy%>";
var retMsg="";
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,DT1);
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#xmzszjqkForm").validationButton())
		{
    		//生成json串
    		var data = Form2Json.formToJSON(xmzszjqkForm);
		  	//组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  	//调用ajax插入
		    if($("#ID").val() == "" || $("#ID").val() == null){
		    	var flag = defaultJson.doInsertJson(controllername + "?insert", data1);
		    	if(flag){
		    		retMsg = "showMsg";
		    		$(window).manhuaDialog.close();
<%--		    		var json=$("#resultXML").val();--%>
<%--		    		var tempJson=eval("("+json+")");--%>
<%--		    		var resultobj=tempJson.response.data[0];--%>
<%--		    		$("#xmzszjqkForm").setFormValues(resultobj);--%>
		    	}
    		}else{
    			defaultJson.doUpdateJson(controllername + "?update", data1);
    		}
		}else{
			requireFormMsg();
		  	return;
		}
	});

	//按钮绑定事件（新增）
    $("#btnClear_Bins").click(function() {
        //$("#xmzszjqkForm").clearFormResult();
        //$("#xmzszjqkForm").cancleSelected();
        
        $("#ZFRQ").val('<%=cDate%>');
        $("#ZFJE").val(0);
        $("#ID").val("");
        
        $("#ID").val('');
        //$("#BLR").val('<%=username%>');
        $("#ZFJE").val("0");
        $("#ZFRQ").val('<%=cDate%>');
    });
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
	
    $("button[id^='blrBtn_']").bind("click", function(){
    	var valu = $(this).attr("value");
    	jbrID = valu;
    	var actorCode = $("#"+valu).attr("code");
		openUserTree('single',actorCode,'setBlr') ;
	});
	

	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 var t = $("#DT1").getTableRows();
		 if(t<=0)
		 {
			 xAlert("信息提示","请至少查询出一条记录！");
			 return;
		 }
	  	 $(window).manhuaDialog({"title":"导出","type":"text","content":"${pageContext.request.contextPath }/jsp/framework/print/TabListEXP.jsp?tabId=DT1","modal":"3"});
	});
});
//页面默认参数
function init(){
	if(qy!="" && type=="insert"){
		//根据jhsjid (qy)  查询项目信息
		//$("#QJHSJID").val(qy);
		$.ajax({
			url : controllername+"?queryCommon&opttype=xmbd&qc_jhsjid="+qy,
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
	
	//$("#QZFRQ").val("<%=cYear%>");
	$("#ZFRQ").val('<%=cDate%>');
	//获取父窗口行设置的条件
	
}


//点击行事件
function tr_click(obj){
	//alert(JSON.stringify(obj));
	$("#xmzszjqkForm").setFormValues(obj);
	$("#btnSave").attr("disabled", false);
}

//选中项目名称弹出页
function selectXm(){
	$(window).manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zjgl/xmcx.jsp","modal":"2"});
}
//弹出区域回调
getWinData = function(data, tID){
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#BDMC").val(JSON.parse(data).BDMC);
	$("#JHSJID").val(JSON.parse(data).GC_JH_SJ_ID);
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
}

//选择项目标段
function selectBb(){
	//popPage = "bd";
	$(window).manhuaDialog({"title":"项目列表","type":"text","content": "${pageContext.request.contextPath}/jsp/business/zjgl/xmcx_zszj.jsp","modal":"2"});
}

function selectBlBb(){
	$(window).manhuaDialog({"title":"项目列表","type":"text","content": "${pageContext.request.contextPath}/jsp/business/gcb/kfglgl/xmcx.jsp","modal":"2"});
}

function closeNowCloseFunction(){
	return retMsg;
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">项目征收资金使用情况
      	<span class="pull-right">
      	<button id="btnBl" class="btn"  onclick="selectBlBb()" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">补录</button>
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
      	</span>
      </h4>
     <form method="post" id="xmzszjqkForm"  >
      <table class="B-table" width="100%" >
      <input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
	  	<input type="hidden" id="GC_TCJH_XMXDK_ID" fieldname="XMID" name = "XMID"/></TD>
	  	<input type="hidden" id="JHSJID" name="JHSJID"  fieldname="JHSJID" >
         <tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
			<td width="25%" class="right-border bottom-border">
				<input id="XMMC" class="span12" placeholder="必填" check-type="required" style="width:85%" name="XMMC" fieldname="XMMC" type="text" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
			<td width="25%" class="right-border bottom-border">
				<input id="BDMC" class="span12" name="BDMC" style="width:85%" fieldname="BDMC" type="text" disabled/>
					<button class="btn btn-link"  type="button" onclick="selectBb()"><i class="icon-edit"></i></button>
			</td>
		</tr>
        
        <tr>
			<th width="8%" class="right-border bottom-border text-right">区域</th>
			<td width="17%" class="right-border bottom-border" colspan="3">
				<select  id="QY"  class="span12"  style="width:40%;" name="QY" fieldname="QY"  operation="=" kind="dic" src="QY"  defaultMemo="-全部-">
			</td>
		</tr>
        <tr>
			<th width="8%" class="right-border bottom-border text-right">使用金额</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZFJE" class="span12" keep="true" placeholder="必填" check-type="required" check-type="maxlength" maxlength="17" value=0 style="width:70%;text-align:right;" name="ZFJE" fieldname="ZFJE" type="number" min="0" />&nbsp;<b>(元)</b>
			</td>
			<th width="8%" class="right-border bottom-border text-right">使用日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZFRQ" class="span7" type="date" check-type="maxlength" maxlength="10" name="ZFRQ" fieldname="ZFRQ"/>
			</td>
		</tr>
		<!-- <tr>
			<th width="8%" class="right-border bottom-border text-right">联系人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="LXR" class="span12" style="width:80%;" placeholder="必填" maxlength="36" name="LXR" fieldname="LXR" type="text" readOnly/>
				<button class="btn btn-link"  type="button" id="blrBtn_LXR" value="LXR"><abbr title="点击选择联系人"><i class="icon-edit"></i></abbr></button>
			</td>
			<th width="8%" class="right-border bottom-border text-right">联系方式</th>
			<td width="17%" class="right-border bottom-border">
				<input id="LXFS" class="span12"  maxlength="40" name="LXFS" fieldname="LXFS" type="text" />
			</td>
		</tr> -->
        <tr>
	        <th width="8%" class="right-border bottom-border text-right">备注</th>
	        <td colspan="3" class="bottom-border right-border" >
	        	<textarea class="span12" rows="2" id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ"></textarea>
	        </td>
        </tr>
      </table>
      </form>
    </div>
   </div>
  </div>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "t.LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
       		 <input type="hidden" id="queryResult"/>
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>