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
<title>提请款部门-维护</title>
<%
String type=request.getParameter("type");
//获取当前用户信息
User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
OrgDept dept = user.getOrgDept();
String deptId = dept.getDeptID();
String deptName = dept.getDept_Name();
String userid = user.getAccount();
String username = user.getName();
String sysdate = Pub.getDate("yyyy-MM-dd");
String cMonth = Pub.getDate("MM");
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglTqkbmController.do";
var controllernameTqkMx= "${pageContext.request.contextPath }/zjgl/gcZjglTqkbmmxController.do";
var type ="<%=type%>";
var flag,jbrID, lxtype;

var isSend = false;
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		lxtype = $("#QKLX").val();
		if("16"==lxtype){//工程类
			var dataMx = combineQuery.getQueryCombineData(queryFormMx,frmPost,gcZjglTqkbmList_pcl);
			defaultJson.doQueryJsonList(controllernameTqkMx+"?query",dataMx,gcZjglTqkbmList_pcl);
		}else if("14"==lxtype){//排迁类
			var dataMx = combineQuery.getQueryCombineData(queryFormMx,frmPost,gcZjglTqkbmList_pql);
			defaultJson.doQueryJsonList(controllernameTqkMx+"?query",dataMx,gcZjglTqkbmList_pql);
		}else{//其它
			var dataMx = combineQuery.getQueryCombineData(queryFormMx,frmPost,gcZjglTqkbmList);
			defaultJson.doQueryJsonList(controllernameTqkMx+"?query",dataMx,gcZjglTqkbmList);
		}
	});
	//按钮绑定事件(删除)btnTQKDelete
	$("#btnTQKDelete").click(function()
		{
			 //生成json串
		 	var data = Form2Json.formToJSON(gcZjglTqkbmForm);
			var data1 = defaultJson.packSaveJson(data);
			xConfirm("提示信息","是否确认删除！");
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').one("click",function(){ 
			defaultJson.doDeleteJson(controllername+"?deleteTqk",data1,null,"deleteHuiDiao"); 
			}); 
		});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#gcZjglTqkbmForm").validationButton())
		{
		    //生成json串
		    var data = Form2Json.formToJSON(gcZjglTqkbmForm);
		  	//组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  	//调用ajax插入
		    if($("#ID").val() == "" || $("#ID").val() == null){
		    	var success=defaultJson.doInsertJson(controllername + "?insert", data1);
		    	if(success==true)
				{
					var obj=$("#resultXML").val();
					var subresultmsgobj1=defaultJson.dealResultJson(obj);
					$("#gcZjglTqkbmForm").setFormValues(subresultmsgobj1);
					$("#QTQKID").val($("#ID").val());
					$("#btnInsert").attr("disabled", false);
					$("#btnImport").attr("disabled", false);
					
					$("#btnClear_Bins").remove();
					
					$("#btnSendToBmMaster").attr("disabled", false);
					
					var parentmain=$(window).manhuaDialog.getParentObj();	
					parentmain.$("#btnQuery").click();
				}
    			//$("#gcZjglTqkForm").clearFormResult();
    		}else{
    			defaultJson.doUpdateJson(controllername + "?update", data1);
    		}
		}else{
			requireFormMsg();
		  	return;
		}
	});
	
	
	$("#btnSendToBmMaster").click(function() {
		var listID = "";
		if("16"==lxtype){
			listID = "gcZjglTqkbmList_pcl";
		}else if("14"==lxtype){
			listID = "gcZjglTqkbmList_pql";
		}else{
			listID = "gcZjglTqkbmList";
		}
		
		var rows = $("#"+listID+" tbody tr");
		
		if(rows.size()==0){
			xAlert("", "明细不能为空！", 2);
			return false;
		}
		
		xConfirm("信息确认","提交后记录为不可修改！是否确认提交所选的记录？ ");
		
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){
			isSend = true;
			var rowValue = $("#DT1").getSelectedRow();
			var tempJson = convertJson.string2json1(rowValue);
			//更新记录为提交状态
			$("#TQKZT").val("2");
		    //生成json串
		    var data = Form2Json.formToJSON(gcZjglTqkbmForm);
		  	//组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		    var success= defaultJson.doUpdateJson(controllername + "?update&opttype=bm", data1);
		    if(success==true)
			{
		    	$("#btnSave").attr("disabled", true);
				$("#btnSendToBmMaster").attr("disabled", true);
				
				$("#btnInsert").attr("disabled", true);
				$("#btnUpdateMx").attr("disabled", true);
				$("#btnDelete").attr("disabled", true);
				$("#btnImport").attr("disabled", true);
				$("#btnTQKDelete").attr("disabled",true);
			}
		});
	});
	
	//按钮绑定事件(新增)
	$("#btnInsert").click(function() {
		var QKLXVal = $("#QKLX").val();
		$(window).manhuaDialog({"title":"提请款明细>新增","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqkbmmx-add.jsp?type=insert&QKLX="+QKLXVal,"modal":"2"});
	});
	//按钮绑定事件(导入)
	$("#btnImport").click(function() {
		var QKLXVal = $("#QKLX").val();
		//+":"+$("#ID").val();
		$(window).manhuaDialog({"title":"提请款明细>导入","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqkbm-importList.jsp?type=insert&QKLX="+QKLXVal,"modal":"2"});
	});
	
	
  	//修改明细
	$("#btnUpdateMx").click(function() {
		var QKLXVal = $("#QKLX").val();
		var ind = -1;
		if("16"==lxtype){
			ind = $("#gcZjglTqkbmList_pcl").getSelectedRowIndex();
		}else if("14"==lxtype){
			ind = $("#gcZjglTqkbmList_pql").getSelectedRowIndex();
		}else{
			ind = $("#gcZjglTqkbmList").getSelectedRowIndex();
		}
		if(ind==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$(window).manhuaDialog({"title":"提请款明细>修改","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqkbmmx-add.jsp?type=update&QKLX="+QKLXVal,"modal":"2"});
	});
	
	//按钮绑定事件（新增）
    $("#btnClear_Bins").click(function() {
        $("#gcZjglTqkbmForm").clearFormResult();
        $("#gcZjglTqkbmForm").cancleSelected();
        
        
        $("#ZFRQ").val(new Date().toLocaleDateString());
        $("#ZFJE").val(0);
        $("#ID").val("");
    });
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });

    $("#btnWctz").click(function() {
		if($("#HTID").val()=='')
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"部门合同>合同完成投资","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtwctz-indexOne.jsp?hasHt=one&htid="+$("#HTID").val(),"modal":"1"});
	});
	
    $("#btnDelete").click(function() {
    	var rowindex = -1;
		if("16"==lxtype){
			rowindex = $("#gcZjglTqkbmList_pcl").getSelectedRowIndex();
		}else if("14"==lxtype){
			rowindex = $("#gcZjglTqkbmList_pql").getSelectedRowIndex();
		}else{
			rowindex = $("#gcZjglTqkbmList").getSelectedRowIndex();
		}
		if(rowindex==-1)
		 {
			requireSelectedOneRow();
		    return
		}
		
		xConfirm("信息确认","是否确认删除所选记录？");
		
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){
			
			var rowValue;//获得选中行的json对象
			if("16"==lxtype){
				rowValue = $("#gcZjglTqkbmList_pcl").getSelectedRow();
			}else if("14"==lxtype){
				rowValue = $("#gcZjglTqkbmList_pql").getSelectedRow();
			}else{
				rowValue = $("#gcZjglTqkbmList").getSelectedRow();
			}
			
			var tempJson = convertJson.string2json1(rowValue);
			$("#BmMxID").val(tempJson.ID);
			$("#BmMxTQKID").val(tempJson.TQKID);
			
			 //生成json串
		    var data = Form2Json.formToJSON(queryFormTqkBmMx);
		  	//组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
			$.ajax({
				url : controllernameTqkMx+"?delete",
				data : data1,
				cache : false,
				async :	false,
				dataType : "json",  
				type : 'post',
				success : function(response) {
					if("16"==lxtype){
						rowindex = $("#gcZjglTqkbmList_pcl").getSelectedRowIndex();
						$("#gcZjglTqkbmList_pcl").removeResult(rowindex);
					}else if("14"==lxtype){
						rowindex = $("#gcZjglTqkbmList_pql").getSelectedRowIndex();
						$("#gcZjglTqkbmList_pql").removeResult(rowindex);
					}else{
						$("#gcZjglTqkbmList").removeResult(rowindex);
					}
				}
			});
		});
		
	});
    
    $("#btnHtView").click(function(){
		var values = "";
    	if("16"==lxtype){
    		if($("#gcZjglTqkbmList_pcl").getSelectedRowIndex()==-1)
    		{
    			requireSelectedOneRow();
    			return
    		}
    		values = $("#gcZjglTqkbmList_pcl").getSelectedRow();
		}else if("14"==lxtype){
			if($("#gcZjglTqkbmList_pql").getSelectedRowIndex()==-1)
    		{
    			requireSelectedOneRow();
    			return
    		}
			values = $("#gcZjglTqkbmList_pql").getSelectedRow();
		}else{
			if($("#gcZjglTqkbmList").getSelectedRowIndex()==-1)
    		{
    			requireSelectedOneRow();
    			return
    		}
			values = $("#gcZjglTqkbmList").getSelectedRow();
		}
		
		$("#resultXML").val(values);
		//var rowValue = $("#DT1").getSelectedRow();
		//var tempJson = convertJson.string2json1(values);
 		//var idvar = tempJson.ID;
		$(window).manhuaDialog({"title":"部门提请款>合同详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/ht-view-tqkmx.jsp?type=detail","modal":"1"});
    	
    })
    <%
    if(type.equals("detail")){
	%>
	//置所有input 为disabled
	$(":input").each(function(i){
	   $(this).attr("disabled", "true");
	 });
	<%
		}
	%>
	
	$("button[id^='blrBtn_']").bind("click", function(){
    	var valu = $(this).attr("value");
    	jbrID = valu;
    	var actorCode = $("#"+valu).attr("code");
		openUserTree('single',actorCode,'setBlr') ;
	});
	
	
	
});



//页面默认参数
function init(){
	//如果是修改显示修改按钮
	if(type=="update"){
		$("#btnTQKDelete").show();
	}else{
		$("#btnTQKDelete").hide();
	}
	if(type == "insert"){
		getNd();
		$("#SQDW").val('<%=deptId%>');
		$("#BZRQ").val('<%=sysdate%>');
		$("#GCPC").val('<%=cMonth%>');
		
		$("#BMBLRID").val('<%=username%>');
		$("#BMBLRID").attr("code", '<%=userid%>');
	}else if(type == "update" || type == "detail"){
		var parentmain=$(window).manhuaDialog.getParentObj();	
		var rowValue = parentmain.$("#resultXML").val();
		var tempJson = convertJson.string2json1(rowValue);
		
		//表单赋值
		$("#demoForm").setFormValues(tempJson);
		
		$("#QTQKID").val(tempJson.ID);
		$("#QID").val(tempJson.ID);
		
		//alert(JSON.stringify(tempJson));
		//查询记录数
		
		//表单赋值
		var data = combineQuery.getQueryCombineData(queryForm,frmPost);
		var data1 = {
			msg : data
		};
		$.ajax({
			url : controllername+"?query",
			data : data1,
			cache : false,
			async :	false,
			dataType : "json",  
			type : 'post',
			success : function(response) {
				$("#resultXML").val(response.msg);
				var resultobj = defaultJson.dealResultJson(response.msg);
				$("#gcZjglTqkbmForm").setFormValues(resultobj);
				lxtype = resultobj.QKLX;
			}
		});
		
		//查询明细
		g_bAlertWhenNoResult = false;
		if("16"==lxtype){
			var dataMx = combineQuery.getQueryCombineData(queryFormMx,frmPost,gcZjglTqkbmList_pcl);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllernameTqkMx+"?query",dataMx,gcZjglTqkbmList_pcl);
			
			$("#QKLX").change();
		}else if("14"==lxtype){
			var dataMx = combineQuery.getQueryCombineData(queryFormMx,frmPost,gcZjglTqkbmList_pql);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllernameTqkMx+"?query",dataMx,gcZjglTqkbmList_pql);
			
			$("#QKLX").change();
		}else{
			var dataMx = combineQuery.getQueryCombineData(queryFormMx,frmPost,gcZjglTqkbmList);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllernameTqkMx+"?query",dataMx,gcZjglTqkbmList);
		}
		g_bAlertWhenNoResult = true;
		
		$("#btnInsert").attr("disabled", false);
		$("#btnSendToBmMaster").attr("disabled", false);
		$("#btnImport").attr("disabled", false);
	}
	$("#btnWctz").hide();
}

function insertBzj(){
	var fuyemian1=$(window).manhuaDialog.getParentObj();
	fuyemian1.gengxinchaxun('insert');
}
function updateBzj(){
	var fuyemian1=$(window).manhuaDialog.getParentObj();
	fuyemian1.gengxinchaxun('update');
}


//点击行事件
function tr_click(obj,tabListid){
	if(obj.HTLX!='SG'){
		$("#HTID").val(obj.HTXXID);
		$("#btnWctz").show();
	}else{
		$("#HTID").val("");
		$("#btnWctz").hide();
	}
	if(tabListid='gcZjglTqkbmList'){
		$("#QMXID").val(obj.ID);
		if(!isSend){
			$("#btnUpdateMx").attr("disabled", false);
			$("#btnDelete").attr("disabled", false);
		}
		$("#btnHtView").attr("disabled", false);
	}
	if(tabListid='gcZjglTqkbmList_pcl'){
		$("#QMXID").val(obj.ID);
		if(!isSend){
			$("#btnUpdateMx").attr("disabled", false);
			$("#btnDelete").attr("disabled", false);
		}
		$("#btnHtView").attr("disabled", false);
	}
	if(tabListid='gcZjglTqkbmList_pql'){
		$("#QMXID").val(obj.ID);
		if(!isSend){
			$("#btnUpdateMx").attr("disabled", false);
			$("#btnDelete").attr("disabled", false);
		}
		$("#btnHtView").attr("disabled", false);
	}
}

//默认年度
function getNd(){
	//年度信息，里修改
	var y = new Date().getFullYear();
	$("#QKNF option").each(function(){
		if(this.value == y){
		 	$(this).attr("selected", true);
		 	return true;
		}
	});
}

//选中项目名称弹出页
function selectXm(){
	$(window).manhuaDialog({"title":"项目选 择","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/xmcx.jsp","modal":"2"});
}
//弹出区域回调
getWinData = function(data){
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#XMBH").val(JSON.parse(data).XMBH);
	$("#GC_TCJH_XMXDK_ID").val(JSON.parse(data).GC_TCJH_XMXDK_ID);
};

//详细信息
function rowView(index){
	var obj = $("#gcZjglTqkbmList").getSelectedRowJsonByIndex(index);
	var xmbh = eval("("+obj+")").XMBH;
	$(window).manhuaDialog(xmscUrl(xmbh));
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
	$("#"+jbrID).attr("code", userId);
}

function change_show(t){
	if(t!="" && t.value=="16"){
		$("#id_show_pcl").show();
		$("#id_show_fgcl").hide();
		$("#id_show_pql").hide();
	}else if(t!="" && t.value=="14"){
		$("#id_show_pcl").hide();
		$("#id_show_fgcl").hide();
		$("#id_show_pql").show();
	}else{
		$("#id_show_pcl").hide();
		$("#id_show_pql").hide();
		$("#id_show_fgcl").show();
	}
}

function closeParentCloseFunction(msgc){
	if (typeof(msgc) == "undefined") {
	}else if(msgc=="showMsg"){
		xSuccessMsg('操作成功！',null);
	}
	$("#btnQuery").click();
}
//删除回调
function deleteHuiDiao()
{
	var fuyemian=$(window).manhuaDialog.getParentObj();
	fuyemian.delhang();
	$(window).manhuaDialog.close();
}
function doBZ(obj){

	if(obj.BZ==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BZ;
	}
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise" style="display:black;">
      <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
			<TD class="right-border bottom-border">
				<input class="span12" type="text" id="QID" name="ID"  fieldname="ID" value="" operation="=" >
			</TD>
        </TR>
      </table>
      </form>
      </div>
      <div class="B-small-from-table-autoConcise" style="display:black;">
      <form method="post" id="queryFormTqkBmMx"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
			<TD class="right-border bottom-border">
				<input class="span12" type="text" id="BmMxID" name="ID"  fieldname="ID" />
				<input type="hidden" id="BmMxTQKID" name="TQKID"  fieldname="TQKID" />
			</TD>
        </TR>
      </table>
      </form>
      </div>
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">部门提请款
      	<span class="pull-right">
      		<%
		    	if(!type.equals("detail")){
			%>
<%--      		<button id="btnClear_Bins" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>--%>
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
	  		<button id="btnTQKDelete" class="btn"  type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">删除</button>
	  		<button id="btnSendToBmMaster" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>提交</button>
<%--	  		<button id="btnPrint" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">打印</button>--%>
	  		<%
				}
			%>
      	</span>
      </h4>
     <form method="post" id="gcZjglTqkbmForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
      	<input type="hidden" id="HTID"  name = "HTID"/></TD>
      	<input type="hidden" id="TQKZT" fieldname="TQKZT" name = "TQKZT" value="1"/></TD>
      	
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">部门</th>
			<td width="17%" class="right-border bottom-border">
				<select type="text" fieldname="SQDW" name="SQDW" id="SQDW" kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME" disabled="disabled"></select></td>
			</td>
			<th width="8%" class="right-border bottom-border text-right">请款类型</th>
			<td width="17%" class="right-border bottom-border">
				<select  style="width:25%;" id="QKLX" onchange="change_show(this);"  placeholder="必填" check-type="required" class="span6"  name="QKLX" fieldname="QKLX"  operation="=" kind="dic" src="QKLX"  defaultMemo="请选择">
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">请款年份</th>
			<td width="17%" class="right-border bottom-border">
				<select style="width:25%;" id="QKNF"   placeholder="必填" check-type="required" class="span6"  name="QKNF" fieldname="QKNF"  operation="=" kind="dic" src="XMNF" defaultMemo="请选择">
			</td>
			<th width="8%" class="right-border bottom-border text-right">请款月份</th>
			<td width="17%" class="right-border bottom-border">
				<select  style="width:25%;" id="GCPC"   class="span6"  name="GCPC" fieldname="GCPC" kind="dic" src="YF"  defaultMemo="请选择">
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">编制日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BZRQ" style="width:40%" class="span9" fieldtype="date" fieldformat="YYYY-MM-DD"  placeholder="必填" check-type="required"  name="BZRQ" fieldname="BZRQ" type="date" />
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">部门办理人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BMBLRID"  style="width:70%;"  class="span12" check-type="maxlength" maxlength="36"  name="BMBLRID" fieldname="BMBLRID" type="text" disabled />
				<button class="btn btn-link"  type="button" id="blrBtn_BMBLRID" value="BMBLRID" title="点击选择办理人"><i class="icon-edit"></i></button>
			</td>
		</tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right">备注</th>
	        <td colspan="3" class="bottom-border right-border" >
	        	<textarea class="span12" rows="2" id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ"></textarea>
	        </td>
        </tr>
      </table>
      </form>
      
       <div style="height:5px;"></div>
      
      <h4 class="title">部门提请款明细
      	<span class="pull-right">
	      	<%
		    	if(!type.equals("detail")){
			%>
	  		<button id="btnHtView" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">合同信息</button>
      		<button id="btnQuery" class="btn btn-link"  type="button" style="display:none;"><i class="icon-search"></i>查询</button>
	  		<button id="btnInsert" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">添加明细</button>
	  		<!-- <button id="btnWctz" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" >完成投资</button> -->
	  		<button id="btnUpdateMx" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">修改</button>
	  		<button id="btnDelete" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">移除</button>
	  		<button id="btnImport" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">导入</button>
	  		<%
				}
			%>
      	</span>
      	 <form method="post" id="queryFormMx">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<input class="span12" type="text" id="QTQKID" name="TQKID"  fieldname="TQKID" value="" operation="=" >
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
				</table>
	</form>
      </h4>
     
    <div class="overFlowX" id="id_show_fgcl">
    <input type="hidden" id="QMXID">
	<table class="table-hover table-activeTd B-table" id="gcZjglTqkbmList" width="100%" type="single" pageNum="10">
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
				<th fieldname="DWMC" colindex=4 tdalign="center" maxlength="30" >&nbsp;收款单位&nbsp;</th>
				<th fieldname="XMMCNR" colindex=5 tdalign="left" maxlength="30" >&nbsp;项目内容&nbsp;</th>
				<th fieldname="HTBM" colindex=6 tdalign="center" maxlength="30" >&nbsp;合同编码&nbsp;</th>
				<th fieldname="ZXHTJ" colindex=7 tdalign="right" >&nbsp;最新合同价(元)&nbsp;</th>
				<th fieldname="ZWCZF" colindex=8 tdalign="right" >&nbsp;完成投资(元)&nbsp;</th>
				<th fieldname="YBF" colindex=8 tdalign="right" >&nbsp;已拔付(元)&nbsp;</th>
				<th fieldname="BCSQ" colindex=9 tdalign="right" >&nbsp;部门申请值(元)&nbsp;</th>
<%--				<th fieldname="LJBF" colindex=10 tdalign="center" >&nbsp;累计拔付&nbsp;</th>--%>
				<th fieldname="AHTBFB" colindex=11 tdalign="right" >&nbsp;累计付款比例(%)&nbsp;</th>
				<th fieldname="BZ" colindex=19 tdalign="center" maxlength="30" CustomFunction="doBZ">&nbsp;备注&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
    
    <div class="overFlowX" id="id_show_pcl" style="display:none;">
	<table class="table-hover table-activeTd B-table" id="gcZjglTqkbmList_pcl" width="100%" type="single" pageNum="10">
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
				<th fieldname="DWMC" colindex=4 tdalign="center" maxlength="30" >&nbsp;收款单位&nbsp;</th>
				<th fieldname="XMMCNR" colindex=5 tdalign="left" maxlength="30" >&nbsp;项目内容&nbsp;</th>
				<th fieldname="HTBM" colindex=6 tdalign="center" maxlength="30" >&nbsp;合同编码&nbsp;</th>
				<th fieldname="ZXHTJ" colindex=7 tdalign="right" >&nbsp;最新合同价(元)&nbsp;</th>
				<th fieldname="CSZ" colindex=12 tdalign="right" >&nbsp;财审值(元)&nbsp;</th>
				<th fieldname="SJZ" colindex=12 tdalign="right" >&nbsp;审计值(元)&nbsp;</th>
				<th fieldname="JZQR" colindex=13 tdalign="right" >&nbsp;监理确认计量款(元)&nbsp;</th>
				<th fieldname="JGC" colindex=13 tdalign="right" >&nbsp;甲供材(元)&nbsp;</th>
				<th fieldname="YBF" colindex=8 tdalign="right" >&nbsp;已付款(元)&nbsp;</th>
				<th fieldname="BCSQ" colindex=9 tdalign="right" >&nbsp;部门申请值(元)&nbsp;</th>
				<th fieldname="AJLFKB" colindex=14 tdalign="right" >&nbsp;按计量付款比例(%)&nbsp;</th>
				<th fieldname="AHTBFB" colindex=11 tdalign="right" >&nbsp;累计付款比例(%)&nbsp;</th>
				<th fieldname="BZ" colindex=19 tdalign="center" maxlength="30" CustomFunction="doBZ">&nbsp;备注&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	
	<div class="overFlowX" id="id_show_pql" style="display:none;">
	<table class="table-hover table-activeTd B-table" id="gcZjglTqkbmList_pql" width="100%" type="single" pageNum="10">
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
				<th fieldname="DWMC" colindex=4 tdalign="center" maxlength="30" >&nbsp;收款单位&nbsp;</th>
				<th fieldname="XMMCNR" colindex=5 tdalign="left" maxlength="30" >&nbsp;项目内容&nbsp;</th>
				<th fieldname="HTBM" colindex=6 tdalign="center" maxlength="30" >&nbsp;合同编码&nbsp;</th>
				<th fieldname="ZXHTJ" colindex=7 tdalign="right" >&nbsp;最新合同价(元)&nbsp;</th>
				<th fieldname="ZWCZF" colindex=8 tdalign="right" >&nbsp;完成投资(元)&nbsp;</th>
				<th fieldname="CSZ" colindex=9 tdalign="right" >&nbsp;财审值(元)&nbsp;</th>
				<th fieldname="YBF" colindex=10 tdalign="right" >&nbsp;已拨付(元)&nbsp;</th>
				<th fieldname="BCSQ" colindex=11 tdalign="right" >&nbsp;部门申请值(元)&nbsp;</th>
				<th fieldname="AHTBFB" colindex=12 tdalign="right" >&nbsp;累计付款比例(%)&nbsp;</th>
				<th fieldname="BZ" colindex=13 tdalign="center" maxlength="30" CustomFunction="doBZ">&nbsp;备注&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	
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
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
<script>
</script>
</html>