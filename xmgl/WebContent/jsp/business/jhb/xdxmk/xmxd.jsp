<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>下达项目库-项目下达</title>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
var controllername_bl= "${pageContext.request.contextPath }/TaskAction.do";
 //页面初始化
$(function() {
	init();
	//setBtnDisabled();
	//按钮绑定事件(查询)
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件(新增)
	$("#btnInsert").click(function() {
		var nd = $("#qnd").val();
		$(window).manhuaDialog({"title":"新增计划审批","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/jhsp_insert.jsp?type=insert&nd="+nd,"modal":"4"});
	});
	//按钮绑定事件(修改)
	$("#btnUpdate").click(function() {
		if($("#jhpcList").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#jhpcList").getSelectedRow());
		$(window).manhuaDialog({"title":"修改计划审批","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/jhsp_insert.jsp?type=update","modal":"4"});
	});
	//按钮绑定事件(选择项目)
	$("#btnSelect").click(function() {
		var obj = convertJson.string2json1($("#jhpcList").getSelectedRow());
		var nd = obj.ND;
		$(window).manhuaDialog({"title":"选择项目","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/aPcxdtc.jsp?nd="+nd,"modal":"1"});
	});
	//按钮绑定事件（呈请审批）
    $("#btnSp").click(function() {
    	if($("#jhpcList").getSelectedRowIndex()==-1)
		{
			requireSelectedOneRow();
		    return;
		}
    	if($("#xmList").getTableRows()==0){
			xInfoMsg('请选择项目！',"");
			return;
		}
    	var ids = "";
		$("#xmList tbody tr").each(function(){
			//存行数据
			var value=convertJson.string2json1($(this).attr("rowJson")).GC_TCJH_XMXDK_SP_ID;
			ids+=value+",";
		});
		var result = verificationXmnd(controllername+"?verificationXmnd&ids="+ids);
		if(result){
			xWarning("<项目年度>不统一，请核对信息！");
			return;
		}else{
			var obj = $("#jhpcList").getSelectedRow();
	 		var tempValue = convertJson.string2json1(obj);
	 		var sjbh = tempValue.SJBH;
	 		var ywlx = "020001";
	 		var condition = "";
	 		var sjzt = tempValue.SJZT;
	 		if(sjzt!=0&&sjzt!=7){
	 			var isview = "1";
	 		    var isOver = getProIsover(sjbh,ywlx);
	 		    if(isOver =="1"){
	 		    	isview = "0";
	 		    }
	 			var s = "${pageContext.request.contextPath}/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview="+isview;   
	 		  	$(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});  
	 		}
	 		else
	 		{
				var obj = convertJson.string2json1($("#jhpcList").getSelectedRow());
				var spfs = obj.SPFS;
				if(spfs == "1"){//手动通过
					xInfoMsg("手动通过,无审批信息！");
				}
				else
				{
					if(sjzt == 7)
					{
						backSp();
			 		}
					else
					{
						createSPconf(sjbh,ywlx,condition); 	
			 		}					
				}		 			
	 		}	
	 		/* 	if(sjzt == 7){
	 			backSp();
	 		}else{
	 			createSPconf(sjbh,ywlx,condition);

	 		} */
		}
    });
	
	//按钮绑定事件(下达统筹计划)
	$("#btnXdtcjh").click(function() {
		var obj = $("#jhpcList").getSelectedRow();
 		var tempValue = convertJson.string2json1(obj);
 		var jhmc = tempValue.SPMC;
 		var spid = tempValue.GC_TCJH_SP_ID;
		var nd ="";
		$("#xmList tbody tr").each(function(i){
			if(i==0){
				nd = convertJson.string2json1($(this).attr("rowJson")).ND;
			}
		});
		$(window).manhuaDialog({"title":"项目下达","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/jhsp_xd.jsp?jhmc="+jhmc+"&spid="+spid+"&nd="+nd,"modal":"4"});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
    });
    //通过
	$("#btnTg").click(function() {
		var index1 = $("#jhpcList").getSelectedRowIndex();
		if(index1<0){
			requireSelectedOneRow();
			return;
		}
		if($("#xmList").getTableRows()==0){
			xInfoMsg('请选择项目！',"");
			return;
		}
		var ids = "";
		$("#xmList tbody tr").each(function(){
			//存行数据
			var value=convertJson.string2json1($(this).attr("rowJson")).GC_TCJH_XMXDK_SP_ID;
			ids+=value+",";
		});
		var result = verificationXmnd(controllername+"?verificationXmnd&ids="+ids);
		if(result){
			xWarning("<项目年度>不统一，请核对信息！");
			return;
		}else{
			var obj = convertJson.string2json1($("#jhpcList").getSelectedRow());
			var id = obj.GC_TCJH_SP_ID;
			xConfirm("信息确认","确认直接通过审批么？");
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').one("click",function(){
				var success = xmSptg(controllername+"?xmSptg&id="+id,null);
				if(success){
					gs_feedback();
				}else{
					xFailMsg('操作失败！',"");
				}
			});  
		}
	}); 
	//按钮绑定事件(删除)
	$("#btnDelete").click(function() {
		if($("#jhpcList").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		xConfirm("提示信息","是否确认删除！");
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').one("click",function(){ 
				var rowValue = $("#jhpcList").getSelectedRow();
				var data1 = defaultJson.packSaveJson(rowValue);
				defaultJson.doDeleteJson(controllername+"?deleteSp",data1,jhpcList,"setBtnDisabledDefault"); 
			});
	});
});
//再次呈请审批方法
function backSp(){
	var value=queryLcxx();
	var tempJson = convertJson.string2json1(value);
	var rowObject = tempJson.response.data[0];
 	var url =  '${pageContext.request.contextPath}/'+rowObject.LINKURL+'?taskid='+rowObject.ID+'&taskseq='+rowObject.SEQ+'&sjbh='+rowObject.SJBH+'&ywlx='+rowObject.YWLX+'&spbh='+rowObject.SPBH+'&rwlx='+rowObject.RWLX+"&isRead="+rowObject.XB;
 	$(window).manhuaDialog({"title":"审批信息","type":"text","content":url,"modal":"1"});
}


//查询流程信息，主要是再次呈请审批使用
function queryLcxx(){
	var sjbh = $("#jhpcList").getSelectedRowJsonObj().SJBH;
	var str = "";
	$.ajax({
		url:controllername_bl + "?getFwLc&sjbh="+sjbh,
		data:"",
		dataType:"json",
		async:false,
		success:function(result){
			str = result.msg;
		}
	});
	return str;
}


//页面默认参数
function init(){
	getNd();
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}
//初始化控制按钮
function setBtnDisabledDefault(){
	$("#btnXdtcjh").attr("disabled",true);
	$("#btnSelect").attr("disabled",true);
	 $("#xmList tbody").children().empty();
}
//默认年度
function getNd(){
	setDefaultNd("qnd");
	//setDefaultOption($("#qnd"),new Date().getFullYear());
}
//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,jhpcList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryJhpc",data,jhpcList,"setBtnDisabledDefault");
}

//新增/修改计划审批回调函数
function getWinData_InsertJh(data){
	var data1 = defaultJson.packSaveJson(data);
	var valDate = convertJson.string2json1(data);
	if(valDate.GC_TCJH_SP_ID == "" || valDate.GC_TCJH_SP_ID == null){
		defaultJson.doInsertJson(controllername + "?insertJhsp",data1,jhpcList,null);
		$("#xmList").clearResult();
		$("#jhpcList").cancleSelected();
    	$("#jhpcList").setSelect(0);
		$("#btnUpdate").attr("disabled",false);
		$("#btnDelete").attr("disabled",false);
		$("#btnSelect").attr("disabled",false);
		$("#btnSp")[0].innerText = "呈请审批";
		$("#btnXdtcjh").attr("disabled",true);
		$("#btnTg").attr("disabled",false);
	}else{
		defaultJson.doUpdateJson(controllername + "?insertJhsp", data1,jhpcList);
	}
	
}

//点击行事件
function tr_click(obj,tabListId){
	if(tabListId == "jhpcList"){
		if(obj.GC_TCJH_SP_ID !="" ){
			$("#apctcjhztid").val(obj.GC_TCJH_SP_ID);
			//生成json串
			var data = combineQuery.getQueryCombineData(queryFormApc,frmPost,xmList);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryXmxxByPch",data,xmList,"setBtnDisabled",true);
		}
		
	}else{
		$("#btnSp").attr("disabled",true);
		$("#btnUpdate").attr("disabled",true);
		$("#btnDelete").attr("disabled",true);
		$("#btnSelect").attr("disabled",true);
		$("#btnXdtcjh").attr("disabled",true);
		$("#btnTg").attr("disabled",true);
	}
	
}
//行选查询回调函数
function setBtnDisabled(){
	var obj = convertJson.string2json1($("#jhpcList").getSelectedRow());
	var spfs = obj.SPFS;
	var isxd = obj.ISXD;
	if(spfs == "1"){//手动通过
		$("#btnUpdate").attr("disabled",true);
		$("#btnDelete").attr("disabled",true);
		$("#btnSelect").attr("disabled",true);
		//$("#btnSp").attr("disabled",true);
		$("#btnSp").attr("disabled",false);
		$("#btnSp")[0].innerText = "审批信息";
		if(isxd == "1"){
			$("#btnXdtcjh").attr("disabled",true);
		}else{
			$("#btnXdtcjh").attr("disabled",false);
		}
		$("#btnTg").attr("disabled",true);
		return;
	}
	//alert(JSON.stringify(obj));
	if(obj.SJZT == "3"){
		$("#btnSp")[0].innerText = "审批信息";
		$("#btnSp").attr("disabled",false);
		$("#btnUpdate").attr("disabled",true);
		$("#btnDelete").attr("disabled",true);
		$("#btnSelect").attr("disabled",true);
		$("#btnXdtcjh").attr("disabled",true);
		$("#btnTg").attr("disabled",true);
		return;
	}
	if(obj.SJZT == "0"){//登记完成
		$("#btnSelect").attr("disabled",false);
		$("#btnSp").attr("disabled",false);
		$("#btnUpdate").attr("disabled",false);
		$("#btnDelete").attr("disabled",false);
		$("#btnXdtcjh").attr("disabled",true);
		$("#btnTg").attr("disabled",false);
		$("#btnSp")[0].innerText = "呈请审批";
	}else if(obj.SJZT == "1"){//审批中
		$("#btnSp")[0].innerText = "审批信息";
		$("#btnSp").attr("disabled",false);
		$("#btnUpdate").attr("disabled",true);
		$("#btnDelete").attr("disabled",true);
		$("#btnSelect").attr("disabled",true);
		$("#btnXdtcjh").attr("disabled",true);
		$("#btnTg").attr("disabled",true);
	}else if(obj.SJZT == "2"){//审批通过
		$("#btnSp")[0].innerText = "审批信息";
		$("#btnSp").attr("disabled",false);
		$("#btnUpdate").attr("disabled",true);
		$("#btnDelete").attr("disabled",true);
		$("#btnSelect").attr("disabled",true);
		$("#btnTg").attr("disabled",true);
		if(obj.ISXD == "0"){
			$("#btnXdtcjh").attr("disabled",false);
		}else{
			$("#btnXdtcjh").attr("disabled",true);
		}
		
	}else if(obj.SJZT == "7"){//审批退回
		$("#btnSp")[0].innerText = "再次呈请";
		$("#btnSp").attr("disabled",false);
		$("#btnUpdate").attr("disabled",false);
		$("#btnDelete").attr("disabled",false);
		$("#btnSelect").attr("disabled",false);
		$("#btnXdtcjh").attr("disabled",true);
		$("#btnTg").attr("disabled",false);
	}
}
//列表项<项目地址>加图标
function doDz(obj){
	var xmdz = obj.XMDZ;
	if(xmdz != ""){
		return '<a href="javascript:void(0);" onclick="selectDz()"><i title="查看" class="pull-right icon-map-marker"></a></i>';
	}
}
//点击项目地址图标
function selectDz(){
	window.open("${pageContext.request.contextPath }/jsp/business/jhb/xdxmk/img/earth.png");
}
//删除按钮
function doDwon(obj){
	return "<button name=\"btnDownTable\" onclick=\"delData(this)\" class=\"btn btn-link\" type=\"button\"><i title=\"移除\" class=\"icon-remove\"></i></button>";
}
//删除对应方法
function delData(obj){
	
	var rowValue = $("#jhpcList").getSelectedRow();
	var index1 =	$("#jhpcList").getSelectedRowIndex();
	var sjzt = convertJson.string2json1(rowValue).SJZT;
	var spfs = convertJson.string2json1(rowValue).SPFS;;
	if(spfs=="1"){
		xAlert('警告','该批次项目已通过，不能删除！','3');
		return;
	}
	if(sjzt == 1){
		xAlert('警告','不可删除<审批中>的项目！','3');
		$("#jhpcList").cancleSelected();
    	$("#jhpcList").setSelect(index1); 
		return;
	}else if(sjzt == 2){
		xAlert('警告','不可删除<审批通过>的项目！','3');
		$("#jhpcList").cancleSelected();
    	$("#jhpcList").setSelect(index1); 
		return;
	}else{
		var index = $(event.target).closest("tr").index();
    	$("#xmList").cancleSelected();
    	$("#xmList").setSelect(index);
		var rowValue = $("#xmList").getSelectedRow();
		var data1 = defaultJson.packSaveJson(rowValue);
		defaultJson.doDeleteJson(controllername+"?deleteXdkxmSp",data1,xmList,"deleteCallback"); 
	}
}
//删除回调函数
function deleteCallback(){
	var jhpcIndex = $("#jhpcList").getSelectedRowIndex();
	$("#xmList").cancleSelected();
	$("#jhpcList").setSelect(jhpcIndex);
	tr_click($("#jhpcList").getSelectedRowJsonObj(),"jhpcList");
}
//按批次选项目回调函数
//根据年度、批次号插入下达库审批表
function getWinData_apcXm(pch,nd){
	var rowValue = $("#jhpcList").getSelectedRow();
	var tcjhztid = convertJson.string2json1(rowValue).GC_TCJH_SP_ID;
	var actionName=controllername+"?insertXmxdkSp&nd="+nd+"&pch="+pch+"&tcjhztid="+tcjhztid;
	$.ajax({
		url : actionName,
		data : null,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(result) {
			if(result.msg != 0){
				
				$("#apctcjhztid").val(tcjhztid);
				//生成json串
				var data = combineQuery.getQueryCombineData(queryFormApc,frmPost,xmList);
				//调用ajax插入
				defaultJson.doQueryJsonList(controllername+"?queryXmxxByPch",data,xmList,null,true);
			}
			
		},
	    error : function(result) {
		}
	});
    return "";
}
//按项目选择后回调函数
function getWinData_axmXm(data){
	var rowValue = $("#jhpcList").getSelectedRow();
	var tcjhztid = convertJson.string2json1(rowValue).GC_TCJH_SP_ID;
	//alert(tcjhztid);
	var actionName = controllername+"?insertXmxdSpAxm&ids="+data+"&tcjhztid="+tcjhztid;
	$.ajax({
		url : actionName,
		data : null,
		cache : false,
		async :	true,
		dataType : "json",  
		type : 'post',
		success : function(result) {
			if(result.msg != 0){
				//alert(tcjhztid);
				
				$("#apctcjhztid").val(tcjhztid);
				//生成json串
				var data = combineQuery.getQueryCombineData(queryFormApc,frmPost,xmList);
				//调用ajax插入
				defaultJson.doQueryJsonList(controllername+"?queryXmxxByPch",data,xmList,"cellbackQd",true);
			}
			
		},
	    error : function(result) {
		}
	});
    return "";
	
}
//<确定>回调函数
function cellbackQd(){
	gs_feedback();
}
//提请审批后调用函数（重载）
function prcCallback()
{
	gs_feedback();
	$("#btnSp")[0].innerText = "审批信息";
	$("#btnUpdate").attr("disabled",true);
	$("#btnDelete").attr("disabled",true);
	$("#btnSelect").attr("disabled",true);
	$("#btnTg").attr("disabled",true);
}


//再次呈请审批提交后回调
function gengxinchaxun()
{
	prcCallback();
}

//按钮权限控制
function btnKz(){
	var rowValue = $("#jhpcList").getSelectedRow();
	var spfs = convertJson.string2json1(rowValue).SPFS;
	if(spfs == 1){
		$("#btnSp").attr("disabled",false);
		$("#btnUpdate").attr("disabled",true);
		$("#btnDelete").attr("disabled",true);
		$("#btnSelect").attr("disabled",true);
		$("#btnXdtcjh").attr("disabled",false);
		$("#btnTg").attr("disabled",true);
	}
}

//提交审批后刷新页面数据
function gs_feedback(){
	var row_index=$("#jhpcList").getSelectedRowIndex();
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,jhpcList);
	var tempJson = convertJson.string2json1(data);
	var a=$("#jhpcList").getCurrentpagenum();
	tempJson.pages.currentpagenum=a;
	data = JSON.stringify(tempJson);
	defaultJson.doQueryJsonList(controllername+"?queryJhpc",data,jhpcList);
	$("#jhpcList").setSelect(row_index);
	btnKz();
	var json=$("#jhpcList").getSelectedRow();
	json=encodeURI(json);
}

//按项目下发(业务操作包括，更新下达库数据，新增计划主体数据，新增统筹计划数据)
doInsertJh = function(actionName, data1) {
    var success  = true;
	$.ajax({
		type : 'post',
		url : actionName,
		data : data1,
		dataType : 'json',
		async :	false,
		success : function(result) {
			$("#resultXML").val(result.msg);
			success = true;
		},
	    error : function(result) {
		     	alert(result.msg);
			    defaultJson.clearTxtXML();
			    success = false;
		}
	});
	 return success;
};

//计划下达填写时间后回调函数
function getWinData_jhxd(){
	//$("#btnXdtcjh").attr("disabled","disabled");
	//var obj = $("#jhpcList").getSelectedRow();
	//var tempValue = convertJson.string2json1(obj);
	//var jhztid = tempValue.GC_TCJH_SP_ID;
	//var success = doInsertJh(controllername + "?insertJhApc&jhztid="+jhztid+"&ywlx="+data, null);
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,jhpcList);
	//调用ajax插入
	data = defaultJson.getQueryConditionWithNowPageNum(data,"jhpcList");
	defaultJson.doQueryJsonList(controllername+"?queryJhpc",data,jhpcList,"setBtnDisabledDefault");
	successInfo("","");

}

//审批状态样式
function doZt(obj){
	if(obj.SJZT == 0){
		return '<span class="label background-color">'+obj.SJZT_SV+'</span>';
	}else if(obj.SJZT == 2){
		return '<span class="label label-success">'+obj.SJZT_SV+'</span>';
	}else if(obj.SJZT == 1){
		return '<span class="label label-important-orange">'+obj.SJZT_SV+'</span>';
	}else if(obj.SJZT == 7){
		return '<span class="label label-important">'+obj.SJZT_SV+'</span>';
	}
	
}
//=============================sjl=========================================
//列表项<批次号>加图标
function doPch(obj){
	if(obj.PCH == ""){
		return '<div style="text-align:center">—</div>'; 
	}else{
		return obj.PCH;
	}
}

//列表项<审批时间>加图标
function doSpsj(obj){
	if(obj.CLOSETIME == ""){
		return '<div style="text-align:center">—</div>'; 
	}else{
		return obj.CLOSETIME;
	}
}

//列表项<审批意见>加图标
function doSpyj(obj){
	if(obj.RESULTDSCR == ""){
		return '<div style="text-align:center">—</div>'; 
	}else{
		return obj.RESULTDSCR;
	}
}

//列表项<审批方式>翻译
function doSpfs(obj){
	if(obj.SPFS == "0"){
		return '审批通过'; 
	}else if(obj.SPFS == "1"){
		return '手动通过';
	}else{
		return '';
	}
}
//======================
//验证项目年度是否相同
function verificationXmnd(actionName){
	var success = false;
	$.ajax({
		type : 'post',
		url : actionName,
		dataType : 'json',
		async :	false,
		success : function(result) {
			if(result.msg == "true"){
				success = true;
			}
		},
	    error : function(result) {
		     	alert(result.msg);
			    defaultJson.clearTxtXML();
			    success = false;
		}
	});
	 return success;
}
//项目直接通过审批调用方法
function xmSptg(actionName, data1){
	var success = true;
	$.ajax({
		type : 'post',
		url : actionName,
		data : data1,
		dataType : 'json',
		async :	false,
		success : function(result) {
			success = true;
		},
	    error : function(result) {
		     	alert(result.msg);
			    defaultJson.clearTxtXML();
			    success = false;
		}
	});
	 return success;
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">审批信息
      	<span class="pull-right">
	  		<button id="btnInsert" class="btn" type="button">新建</button>
	  		<button id="btnUpdate" class="btn" type="button">修改</button>
	  		<button id="btnDelete" class="btn" type="button">删除</button>
	  		<app:oPerm url="jsp/business/jhb/xdxmk/xmxd.jsp?xmxdtg">
				<button class="btn" id="btnTg" >通过</button>
			</app:oPerm>
	  		<button id="btnSp" class="btn" type="button">呈请审批</button>
      		<button id="btnXdtcjh" class="btn" type="button">下达</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<INPUT type="text" class="span12" kind="text" id="splx" fieldname="SPLX" value="2" keep="true" operation="="/>
			</TD>
        </TR>
         <tr>
          <th width="5%" class="right-border bottom-border text-right">年度</th>
          <td class="right-border bottom-border" width="6%">
          	<select class="span12 year" id="qnd" name = "QND" fieldname = "ND" defaultMemo="全部" operation="=" kind="dic" src="T#GC_TCJH_SP:distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE">
            </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">审批名称</th>
  		  <td class="right-border bottom-border">
			<input class="span12" type="text" name="QSPMC" fieldname="SPMC" operation="like" id="QSPMC" />
		  </td>
          <th width="5%" class="right-border bottom-border text-right">审批状态</th>
          <td class="right-border bottom-border" width="12%">
            <select class="span12" name = "QSJZT" fieldname = "SJZT" defaultMemo="全部" operation="=" kind="dic" src="SJZT">
            </select>
          </td>
          <td class="text-left bottom-border text-right">
           <button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
           <button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
          </td>
        </tr>
      </table>
      </form>
    <div style="height:5px;"></div>
    <div class="overFlowX">
		<table class="table-hover table-activeTd B-table" id="jhpcList" width="100%" type="single" pageNum="5" editable="0">
		<thead>
			<tr>
				<th  name="XH" id="_XH" tdalign="center">&nbsp;#&nbsp;</th>
				<th fieldname="SJZT" tdalign="center" CustomFunction="doZt">&nbsp;审批状态&nbsp;</th>
				<th fieldname="SPMC" maxlength="30" >&nbsp;审批名称&nbsp;</th>	
				<th fieldname="ND" tdalign="center">&nbsp;审批年度&nbsp;</th>				
				<th fieldname="PCH" tdalign="center" CustomFunction="doPch">&nbsp;下达批次号&nbsp;</th>
				<th fieldname="RESULTDSCR" tdalign="center" CustomFunction="doSpyj">&nbsp;审批意见&nbsp;</th>
				<th fieldname="CLOSETIME" tdalign="center" CustomFunction="doSpsj">&nbsp;审批时间&nbsp;</th>
				<th fieldname="SPFS" tdalign="center" CustomFunction="doSpfs">&nbsp;通过方式&nbsp;</th>
				<th fieldname="BZ" maxlength="25">&nbsp;备注&nbsp;</th>
				<th fieldname="LRSJ" tdalign="center">&nbsp;登记时间&nbsp;</th>
				<th fieldname="ISXD" tdalign="center">&nbsp;是否下达&nbsp;</th>
			</tr>
		</thead>
		<tbody></tbody>
		</table>
		</div>
	</div>
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
	  <h4>审批项目&nbsp;&nbsp; <font style=" margin-left:5px; margin-right:5px;;font-size:16px;">(所选项目必须为同一年度)</font> 
	  	<span class="pull-right">
	  		<button id="btnSelect" class="btn" type="button">选择项目</button>
      	</span>
	  </h4>
	  <div style="height:5px;"> </div>
	  <form method="post" id="queryFormApc">
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<input type="text" id="apctcjhztid" fieldname="TCJHZTID" name="QTCJHZTID" value=""  operation="="> 
				
			</TD>
        </TR>
      </table>
      </form>
      <div style="height:200px;overflow:auto;">
		<table class="table-hover table-activeTd B-table" id="xmList" width="100%" type="single" noPage="true" pageNum="1000" nopromptmsg="true">
			<thead>
				<tr>
					<th  name="XH" id="_XH" tdalign="center">&nbsp;#&nbsp;</th>
					<th fieldname="XMBH">&nbsp;项目编号&nbsp;</th>
					<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
					<th fieldname="XJXJ" tdalign="center" >&nbsp;项目性质&nbsp;</th>
					<th fieldname="XMLX" tdalign="center">&nbsp;项目类型&nbsp;</th>
					<th fieldname="XMGLGS">&nbsp;项目管理公司&nbsp;</th>
					<th fieldname="XMSX" tdalign="center">&nbsp;项目属性&nbsp;</th>
					<th fieldname="ISBT" tdalign="center">&nbsp;BT&nbsp;</th>
					<th fieldname="XMDZ" maxlength="15">&nbsp;项目地址&nbsp;</th>
					<th fieldname="XMDZ" CustomFunction="doDz">&nbsp;&nbsp;</th>
					<th fieldname="XMBH" tdalign="center" CustomFunction="doDwon">&nbsp;操作&nbsp;</th>
				</tr>
			</thead>
			<tbody></tbody>
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

</html>