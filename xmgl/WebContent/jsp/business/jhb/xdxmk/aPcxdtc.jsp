<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>下达统筹</title>
<%
	//获取当前时间
	String sysdate = Pub.getDate("yyyy-MM-dd");
	String nd = request.getParameter("nd");
%>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
var index=-1,trlen,flag=true,xjbd;
var nd = '<%=nd%>';
var xdnf,pch,nd;
//页面初始化
$(function(){  
	//自动完成项目名称模糊查询
	showAutoComplete("QXMMC",controllername+"?xmmcAutoCompleteToXmxdk","getXmmcQueryCondition"); 
	init();
	//按钮绑定事件(查询) -按批次
	$("#btnQueryApc").click(function() {
		if(!$("#queryFormApc").validationButton()){
			xInfoMsg("请选择<批次号>","");
			return;
		}
		$("#btn_apc_qd").attr("disabled",false);
		$("#btn_apc_qd").attr("title","点击选择项目");		
		queryListApc();
	});
	//按钮绑定时间（查询）-按项目
	$("#btnQueryAxm").click(function() {
		if(flag){
			g_bAlertWhenNoResult = false;
			var data_Djz = combineQuery.getQueryCombineData(queryFormAxm_hidden,frmPost,xdtcjhxmList);
			defaultJson.doQueryJsonList(controllername+"?queryDjz",data_Djz,xdtcjhxmList);
			flag = false;
		}
		//获取id
		var ids = "";
		$("#xdtcjhxmList tbody tr").each(function(){
			var value = convertJson.string2json1($(this).attr("rowJson")).GC_TCJH_XMXDK_ID;
			ids+="'"+value+"',";
		});
		g_bAlertWhenNoResult = true;
		//生成json串
		var data = combineQuery.getQueryCombineData(queryFormAxm,frmPost,xdxmkListAxm);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryApc&ids="+ids,data,xdxmkListAxm);
		
	});
	//按钮绑定事件(下达统筹计划)批次
	$("#btnXdtcjhApc").click(function() {
		if($("#xdxmkListApc").getTableRows()==0){
			xInfoMsg('没有可下达的项目！',"");
			return;
		}
		if(!$("#jhFormApc").validationButton()){
			requireFormMsg();
			return;
		}
		xConfirm("信息提示","确认将该批次下的项目进行下达吗？");
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){  
			var pch = $("#pch").val();
			var ywid = $("#ywid").val();
			var formData = Form2Json.formToJSON(jhFormApc);
			var formData1 = defaultJson.packSaveJson(formData);
 			var success = doInsertJh(controllername + "?insertJhApc&pch="+pch+"&ywid="+ywid, formData1);
 			if(success){
 				successInfo("","");
				$("#xdxmkListApc").clearResult();
				$("#jhFormApc").clearFormResult();
				clearFileTab();
				$("#ywid").val("");
				init();
			}else{
				xFailMsg('操作失败！',"");
			}
		});  
	});
	//按钮绑定事件(下达统筹计划)项目
	$("#btnXdtcjhAxm").click(function() {
		var ids = "";
		if($("#xdtcjhxmList").getTableRows()==0){
			xInfoMsg('没有可下达的项目！',"");
			return;
		}
		var ids = "";
		$("#xdtcjhxmList tbody tr").each(function(){
			//存行数据
			var value = convertJson.string2json1($(this).attr("rowJson")).GC_TCJH_XMXDK_ID;
			ids+=value+",";
		});
		$(window).manhuaDialog({"title":"下达统筹计划","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/jhxd_axm.jsp?ids="+ids,"modal":"4"});
	});
	//按钮绑定事件（清空）-按批次
    $("#btnClearApc").click(function() {
        $("#queryFormApc").clearFormResult();
        setNdApc();
    });
    //按钮绑定事件（清空）-按项目
    $("#btnClearAxm").click(function() {
        $("#queryFormAxm").clearFormResult();
        setNdAxm();
    });
  //按钮绑定事件(上移)
	$("#btnUp").click(function() {
		var rowindex = $("#xdtcjhxmList").getSelectedRowIndex();//获得选中行的索引
		if(rowindex==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		var rowValue = $("#xdtcjhxmList").getSelectedRow();//获得选中行的json对象
		$("#xdxmkListAxm").insertResult(rowValue,xdxmkListAxm,1);
		$("#xdtcjhxmList").removeResult(rowindex);
		trlen = $("#xdtcjhxmList tr").length-1;
		var num = document.getElementsByTagName("font");
		num[0].innerHTML = trlen;
	});
	//按钮绑定事件(下移)
	$("#btnDown") .click(function() {
		var rowindex = $("#xdxmkListAxm").getSelectedRowIndex();//获得选中行的索引
		if(rowindex==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		var rowValue = $("#xdxmkListAxm").getSelectedRow();//获得选中行的json对象
		//jiangc start 判断项目是否维护项目性质
		var objJson=convertJson.string2json1(rowValue);
		if(objJson.XJXJ=='')
		{
			xInfoMsg('所选项目没有确定项目性质，不能选择！',"");
			return;
		}
		if(objJson.XJXJ=='1'&&objJson.XMWYBH=='')
		{
			xInfoMsg('所选续建项目尚未关联上年项目，不能选择！',"");
			return;	
		}	
		pdxjbd(objJson.GC_TCJH_XMXDK_ID);
		if(xjbd==0)
		{
			xInfoMsg('所选项目没有续建的标段，不能选择！',"");
			return;		
		}	
		// jiangc end
		$("#xdtcjhxmList").insertResult(rowValue,xdtcjhxmList,1);
		$("#xdxmkListAxm").removeResult(rowindex);
		trlen = $("#xdtcjhxmList tr").length-1;
		var num = document.getElementsByTagName("font");
		num[0].innerHTML = trlen;
	});
	//按钮绑定事件(全部下移)
	$("#btnAll").click(function() {
		var bool;
		if($("#xdxmkListAxm").getTableRows()==0){
			xInfoMsg('没有可移动的项目！',"");
			return;
		}
		$("#xdxmkListAxm tbody tr").each(function(){

			var rowValue = convertJson.string2json1($(this).attr("rowJson"));
			//jiangc start 判断项目是否维护项目性质
			if(rowValue.XJXJ=='')
			{
				xInfoMsg('所选项目中存在没有确定项目性质的项目，不能选择！',"");
				bool=true;
				return;
			}
			if(rowValue.XJXJ=='1'&&rowValue.XMWYBH=='')
			{
				xInfoMsg('所选续建项目尚未关联上年项目，不能选择！',"");
				return;	
			}
			//判断是否已续建标段
			pdxjbd(rowValue.GC_TCJH_XMXDK_ID);
			if(xjbd==0)
			{
				xInfoMsg('所选项目中存在没有续建的标段，不能选择！',"");
				return;		
			}	
			
			var rowindex = $(this).index();
			$("#xdxmkListAxm").removeResult(rowindex);
			
			//jiangc end
			$("#xdtcjhxmList").insertResult(JSON.stringify(rowValue),xdtcjhxmList,1);
		});
		//jiangc start
		if(!bool)
		{
			$("#xdxmkListAxm").clearResult();
		}
		//jiangc end
		trlen = $("#xdtcjhxmList tr").length-1;
		var num = document.getElementsByTagName("font");
		num[0].innerHTML = trlen;
	});
	//按钮绑定事件(全部上移)
	$("#btnCannel").click(function() {
		if($("#xdtcjhxmList").getTableRows()==0){
			xInfoMsg('没有可移动的项目！',"");
			return;
		}
		$("#xdtcjhxmList tbody tr").each(function(){
			var rowValue = convertJson.string2json1($(this).attr("rowJson"));
			$("#xdxmkListAxm").insertResult(JSON.stringify(rowValue),xdxmkListAxm,1);
		});
		$("#xdtcjhxmList").clearResult();
		trlen = $("#xdtcjhxmList tr").length-1;
		var num = document.getElementsByTagName("font");
		num[0].innerHTML = trlen;
	});
	//切换按项目TAB页
	$("#axmTab").click('show', function (e) {
		setNdAxm();
		if(flag){
			//设置查询无结果不提示信息
			g_bAlertWhenNoResult = false;
			var data_Djz = combineQuery.getQueryCombineData(queryFormAxm_hidden,frmPost,xdtcjhxmList);
			defaultJson.doQueryJsonList(controllername+"?queryDjz",data_Djz,xdtcjhxmList);
			flag = false;
		}
		//获取id
		var ids = "";
		$("#xdtcjhxmList tbody tr").each(function(){
			var value = convertJson.string2json1($(this).attr("rowJson")).GC_TCJH_XMXDK_ID;
			ids+="'"+value+"',";
		});
		
		//生成json串
		var data = combineQuery.getQueryCombineData(queryFormAxm,frmPost,xdxmkListAxm);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryApc&ids="+ids,data,xdxmkListAxm);
		
		g_bAlertWhenNoResult = true;
		trlen = $("#xdtcjhxmList tr").length-1;
		var num = document.getElementsByTagName("font");
		num[0].innerHTML = trlen;
	});
	//切换按批次TAB页
	$("#apcTab").click('show', function (e) {
		init();
	});
	//按钮绑定事件（暂存）
	$("#btnZc").click(function() {
		//获取id
		var ids = "";
		$("#xdtcjhxmList tbody tr").each(function(){
			var value = convertJson.string2json1($(this).attr("rowJson")).GC_TCJH_XMXDK_ID;
			ids+=value+",";
		});
		
		var success = doInsertDjz(controllername + "?insertDjz&ids="+ids, null);
		if(success){
			successInfo("","");
		}else{
			xFailMsg('操作失败！',"");
		}
	});
	//======================================================================================================================
	//按钮绑定事件(确定)批次
	$("#btn_apc_qd").click(function() {
		if($("#xdxmkListApc").getTableRows()==0){
			xInfoMsg('没有可下达的项目！',"");
			return;
		}

	//	var pch = $("#pch").val();
	//	var nd = $("#qndApc").val();
		$(window).manhuaDialog.getParentObj().getWinData_apcXm(pch,nd);
		$(window).manhuaDialog.close(); 		
	});
	//按钮绑定事件(确定)项目
	$("#btn_axm_qd").click(function() {
		if($("#xdtcjhxmList").getTableRows()==0){
			xInfoMsg('没有可下达的项目！',"");
			return;
		}
		var ids = "";
		var success = false;
		$("#xdtcjhxmList tbody tr").each(function(i){
			//存行数据
			if(i == 0){
				xdnf=convertJson.string2json1($(this).attr("rowJson")).ND;
			}
			var value = convertJson.string2json1($(this).attr("rowJson")).GC_TCJH_XMXDK_ID;
			var nd = convertJson.string2json1($(this).attr("rowJson")).ND;
			if(xdnf != nd){
				success = true;
				return false;
			}else{
				ids+=value+",";
			}
			//xdnf=convertJson.string2json1($(this).attr("rowJson")).ND;
			
		});
		if(success){
			xWarning("<项目年度>不统一，请核对信息！");
			return;
		}else{
			$(window).manhuaDialog.getParentObj().getWinData_axmXm(ids);
			$(window).manhuaDialog.close(); 
		}
		/* var ids = "";
		$("#xdtcjhxmList tbody tr").each(function(){
			//存行数据
			var value = convertJson.string2json1($(this).attr("rowJson")).GC_TCJH_XMXDK_ID;
			ids+=value+",";
		}); */
		
	});
});
//页面默认参数
function init(){
	setNdApc();
	setTcjhDefaultInfo();
	setOptionSelectedIndex($("#pch"));
	g_bAlertWhenNoResult = false;
	queryListApc();
	g_bAlertWhenNoResult = true;
	
	
}
//设置统筹计划相关默认值
function setTcjhDefaultInfo(){
	//默认当前时间
	$("#xdrqApc").val("<%=sysdate %>");
	//默认下发类型
	$("#xflx").val("1");//正常
	//默认批次号
	$("#jhpch").val(setDefaultPch($("#xflx").val()));
	
}
//控制确定按钮是否可用
function xjxj(obj){
	//alert(obj.XJXJ=='')
	if(obj.XJXJ=='')
	{
		$("#btn_apc_qd").attr("disabled",true);
		$("#btn_apc_qd").attr("title","所选批次中存在没有确定项目性质的项目，不能选择！");
	}	
	else
	{
		if(obj.XJXJ==1)
		{
			pdxjbd(obj.GC_TCJH_XMXDK_ID);
			if(xjbd==0)
			{
				$("#btn_apc_qd").attr("disabled",true);
				$("#btn_apc_qd").attr("title","所选批次中存在没有选择续建标段的项目，不能选择！");
			}			
		}	
	}
	if(obj.XJXJ=='1'&&obj.XMWYBH=='')
	{
		$("#btn_apc_qd").attr("disabled",true);
		$("#btn_apc_qd").attr("title","所选批次中存在没有关联上年项目的续建项目，不能选择！");
	}
}
//默认批次号
function setDefaultPch(xflx){
	var pch = "";
	var actionName=controllername+"?queryMaxPch&xflx="+xflx;
	$.ajax({
		url : actionName,
		data : null,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(result) {
			pch = result.msg;
		},
	    error : function(result) {
		}
	});
    return pch;
}
//弹出区域回调
getWinData = function(data){
	if(data.success){
		successInfo("","");
		$("#xdtcjhxmList").clearResult();
		var num = document.getElementsByTagName("font");
		num[0].innerHTML = 0;
	}else{
		xFailMsg('操作失败！',"");
	}
	
};
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
//根据ID新增待结转
doInsertDjz = function(actionName, data1) {
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

//列表项<项目地址>加图标
function doDz(obj){
	var xmdz = obj.XMDZ;
	/* if(xmdz != ""){
		if(xmdz.length>15){
			xmdz = '<abbr title="'+obj.XMDZ+'">'+xmdz.substring(0,15)+'...&nbsp;<a href="javascript:void(0);" onclick="selectDz()"><i title="查看" class="pull-right icon-map-marker"></i></a></abbr>';
		}else{
			xmdz = xmdz+'&nbsp;<a href="javascript:void(0);" onclick="selectDz()"><i title="查看" class="pull-right icon-map-marker"></a></i>';
		}
		return xmdz;
	} */
	if(xmdz != ""){
		return '<a href="javascript:void(0);" onclick="selectDz()"><i title="查看" class="pull-right icon-map-marker"></a></i>';
	}
	
}
//点击项目地址图标
function selectDz(){
	window.open("${pageContext.request.contextPath }/jsp/business/jhb/xdxmk/img/earth.png");
}
//默认年度 -按批次
function setNdApc(){
	
	//$("#qndApc").val(new Date().getFullYear());
	generateXdxmkNdMc($("#qndApc"),$("#pch"));
	//setDefaultNd("qndApc");
	$("#qndApc").val(nd);
}
//默认年度 -按项目
function setNdAxm(){
	$("#qndAxm").val(nd);
	//setDefaultNd("qndAxm");
	//setDefaultOption($("#qndAxm"),new Date().getFullYear());
}
//行加下移按钮
function doDwon(obj){
	return "<button name=\"btnDownTable\" onclick=\"rowDown(this)\" class=\"btn btn-link\" type=\"button\"><i title=\"下移\" class=\"icon-chevron-down\"></i></button>";
}
//行加上移按钮
function doUp(obj){
	return "<button name=\"btnUpTable\" onclick=\"rowUp(this)\" class=\"btn btn-link\" type=\"button\"><i  title=\"上移\" class=\"icon-chevron-up\"></i></button>";
}


//下移按钮事件
function rowDown(obj){	
	var rowIndex = $(obj).parent().parent().index();
	var rowValue = $(obj).parent().parent().attr("rowJson");
	//jiangc start 
	//判断项目是否维护项目性质
	var objJson=convertJson.string2json1(rowValue);
	if(objJson.XJXJ=='')
	{
		xInfoMsg('所选项目没有确定项目性质，不能选择！',"");
		return;
	} 
	if(objJson.XJXJ=='1'&&objJson.XMWYBH=='')
	{
		xInfoMsg('所选续建项目尚未关联上年项目，不能选择！',"");
		return;	
	}	
	//判断是否已续建标段
	pdxjbd(objJson.GC_TCJH_XMXDK_ID);
	if(xjbd==0)
	{
		xInfoMsg('所选项目没有续建的标段，不能选择！',"");
		return;		
	}	
	//jiangc end
	$("#xdtcjhxmList").insertResult(rowValue,xdtcjhxmList,1);
	$("#xdxmkListAxm").removeResult(rowIndex);
	trlen = $("#xdtcjhxmList tr").length-1;
	var num = document.getElementsByTagName("font");
	num[0].innerHTML = trlen;
}
//上移按钮事件
function rowUp(obj){
	var rowIndex = $(obj).parent().parent().index();
	var rowValue = $(obj).parent().parent().attr("rowJson");
	$("#xdxmkListAxm").insertResult(rowValue,xdxmkListAxm,1);
	$("#xdtcjhxmList").removeResult(rowIndex);
	trlen = $("#xdtcjhxmList tr").length-1;
	var num = document.getElementsByTagName("font");
	num[0].innerHTML = trlen;
}

//判断是否已续建标段
function pdxjbd(xmid)
{
	xjbd='';
	var success=true;
	var data1=combineQuery.getQueryCombineData(queryForm,frmPost,null);
	var actionName=controllername+"?query_countbd&xmid="+xmid;
	var data={
		msg : data1
	};
	$.ajax({
		url : actionName,
		data : data,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(result) {
		xjbd=result.msg;
		success=true;
		}
	});
  return success;
}
function tr_click(obj){
	
}
//项目名称自动模糊查询参数
function getXmmcQueryCondition(){
	var data = combineQuery.getQueryCombineData(queryFormAxm,frmPost);
	return data;
}
/**
 * 根据计划查询非应急批次
 * ndObj 年度的jquery对象
 * mcObj 计划名称jquery对象
 */
function generateXdxmkNdMc(ndObj,mcObj){
	ndObj.attr("src","T#GC_TCJH_XMXDK:distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(nd);
	//setDefaultNd(ndObj.attr("id"));
	//setDefaultOption(ndObj,new Date().getFullYear());
	if(mcObj){
		mcObj.attr("kind","dic");
		mcObj.attr("src","T#GC_TCJH_XMXDK:distinct  PCH:PCH:SFYX='1' AND isnatc='0' AND ND='" + ndObj.val()+"'");
		mcObj.html('');
		reloadSelectTableDic(mcObj);
		ndObj.change(function() {
			mcObj.html('');
			if(!ndObj.val().length){
			}
			mcObj.attr("src","T#GC_TCJH_XMXDK:distinct  PCH:PCH:SFYX='1'  AND isnatc='0' AND ND='" + ndObj.val()+"'");
			reloadSelectTableDic(mcObj);
		});
	}
}
//按批次默认查询
function queryListApc(){
	pch = $("#pch").val();
	nd = $("#qndApc").val();
	//生成json串
	var data = combineQuery.getQueryCombineData(queryFormApc,frmPost,xdxmkListApc);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryApc",data,xdxmkListApc);
}

//控制下达文号和批次是否显示
function setPch(e){
	if(e.value==2){
		$("#jhpch").attr("src","LXPC");
		reloadSelectTableDic($("#jhpch"));
	}
	else{
		$("#jhpch").attr("src","PCH");
		reloadSelectTableDic($("#jhpch"));
	}	
}

function closeNowCloseFunction(){
	return true;
}


</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<ul class="nav nav-tabs"> 
	<li class="active"><a href="#apc" data-toggle="tab" id="apcTab">计财批次</a></li> 
	<li class=""><a href="#axm" data-toggle="tab" id="axmTab">自选项目</a></li> 
</ul>
<div class="tab-content"> 
<!-- 静态信息tab页 -->
<div class="tab-pane active" id="apc"> 
  <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
	    <h4 class="title">项目信息
			<span class="pull-right">
				<button id="btn_apc_qd" class="btn" type="button">确定</button>
			</span>
		</h4>
     <form method="post" id="queryFormApc">
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<input type="text" fieldname="ISNATC" name="QISNATC" value="0" keep="true" operation="="> 
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
          <th width="5%" class="right-border bottom-border text-right">年度</th>
          <td class="right-border bottom-border" width="6%">
          	<select class="span12 year" id="qndApc" name = "QND" fieldname = "ND" defaultMemo="全部" operation="=" >
            </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">计财批次号</th>
          <td class="right-border bottom-border" width="6%">
            <select class="span12 year" id="pch" name = "QPCH" check-type="required" fieldname = "PCH" defaultMemo="全部" operation="=">
            </select>
          </td>
		  <td class="text-left bottom-border text-right">
           	<button id="btnQueryApc" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
            <button id="btnClearApc" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
          </td>
        </tr>
      </table>
      </form>
    <div style="height:5px;"></div>
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="xdxmkListApc" width="100%" type="single" pageNum="10000">
		<thead>
			<tr>
				<th  name="XH" id="_XH" style="width:10px" rowspan="2" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
				<th fieldname="XMBH" rowspan="2" colindex=2 >&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" rowspan="2" colindex=3 maxlength="15">&nbsp;项目名称&nbsp;</th>
				<th fieldname="XJXJ" rowspan="2" colindex=4  tdalign="center"  CustomFunction="xjxj">&nbsp;项目性质&nbsp;</th>
				<th fieldname="XMLX" rowspan="2" colindex=5 tdalign="center">&nbsp;项目类型&nbsp;</th>
				<th fieldname="XMGLGS" rowspan="2" colindex=6>&nbsp;项目管理公司&nbsp;</th>
				<th fieldname="XMSX" rowspan="2" colindex=7 tdalign="center">&nbsp;项目属性&nbsp;</th>
				<th fieldname="ISBT" rowspan="2" colindex=8 tdalign="center">&nbsp;BT&nbsp;</th>
				<th fieldname="XMDZ" rowspan="2" colindex=9 maxlength="10">&nbsp;项目地址&nbsp;</th>
				<th fieldname="XMDZ" rowspan="2" colindex=10 CustomFunction="doDz">&nbsp;&nbsp;</th>
				<th colspan="4">年度总投资额（元）</th>
			</tr>
			<tr>
				<th fieldname="GC" colindex=11 tdalign="right">&nbsp;工程&nbsp;</th>
				<th fieldname="ZC" colindex=12 tdalign="right">&nbsp;征拆&nbsp;</th>
				<th fieldname="QT" colindex=13 tdalign="right">&nbsp;其他&nbsp;</th>
				<th fieldname="JHZTZE" colindex=14 tdalign="right">&nbsp;合计&nbsp;</th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	</div>
	</div>
	
  </div>
  </div>
  
  <!-- 按项目下达tab页 -->
    <div class="tab-pane" id="axm">
    <div class="row-fluid">
     <form method="post" id="queryFormAxm" class="B-small-from-table-autoConcise">
     	 <h4 class="title">项目信息
			<span class="pull-right">
				<button id="btn_axm_qd" class="btn" type="button">确定</button>
			</span>
		</h4>
     	<table class="B-table" width="100%">
      		<!--可以再此处加入hidden域作为过滤条件 -->
     		<TR  style="display:none;">
        	<TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<input type="text" fieldname="ISNATC" name="QISNATC" value="0" keep="true" operation="="> 
			</TD>
       		</TR>
        	<!--可以再此处加入hidden域作为过滤条件 -->
         	<tr>
				<th width="5%" class="right-border bottom-border text-right">年度</th>
				<td class="right-border bottom-border" width="6%">
					<select class="span12 year" id="qndAxm" name = "QND" fieldname = "ND" defaultMemo="全部" operation="=" kind="dic" src="T#GC_TCJH_XMXDK:distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE">
				    </select>
				</td>
				<th width="5%" class="right-border bottom-border text-right">项目名称</th>
				<td class="right-border bottom-border" width="20%">
          			<input class="span12" type="text" placeholder="" name="QXMMC"
							fieldname="XMMC" operation="like" id="QXMMC" autocomplete="off"
							tablePrefix=""/>
		  		</td>
				<th width="5%" class="right-border bottom-border text-right">项目性质</th>
				<td class="bottom-border" width="8%">
					<select class="span12 4characters" name = "QXJXJ" fieldname = "XJXJ" defaultMemo="全部" operation="=" kind="dic" src="XMXZ">
				  	</select>
				</td>
				<th width="5%" class="right-border bottom-border text-right">项目属性</th>
				<td class="bottom-border" width="8%">
					<select class="span12 4characters" name = "QXMSX" fieldname = "XMSX" defaultMemo="全部" operation="=" kind="dic" src="XMSX">
				  	</select>
				</td>
				<td class="text-left bottom-border text-right">
					<button id="btnQueryAxm" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
					<button id="btnClearAxm" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
				</td>
			</tr>
      </table>
     </form>
	<div class="B-small-from-table-autoConcise">
	  	<div style="height:200px;overflow:auto;">
		<table class="table-hover table-activeTd B-table" id="xdxmkListAxm" width="100%" type="single" noPage="true" pageNum="1000">
			<thead>
				<tr>
					<th  name="XH" id="_XH" tdalign="center">&nbsp;#&nbsp;</th>
					<th fieldname="XMBH" tdalign="center" CustomFunction="doDwon">&nbsp;操作&nbsp;</th>
					<th fieldname="XMBH">&nbsp;项目编号&nbsp;</th>
					<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
					<th fieldname="XJXJ" tdalign="center" >&nbsp;项目性质&nbsp;</th>
					<th fieldname="XMLX" tdalign="center">&nbsp;项目类型&nbsp;</th>
					<th fieldname="XMGLGS">&nbsp;项目管理公司&nbsp;</th>
					<th fieldname="XMSX" tdalign="center">&nbsp;项目属性&nbsp;</th>
					<th fieldname="ISBT" tdalign="center">&nbsp;BT&nbsp;</th>
					<th fieldname="XMDZ" maxlength="15">&nbsp;项目地址&nbsp;</th>
					<th fieldname="XMDZ" CustomFunction="doDz">&nbsp;&nbsp;</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
		</div>
	</div>
	<br>
	<div align="center">
		<button id="btnAll" class="btn"  type="button"><i class="icon-plus"></i>&nbsp;全部下移</button>&nbsp;
		<button id="btnUp" class="btn" type="button"><i class="icon-chevron-up"></i>&nbsp;上 移</button>&nbsp;
		<button id="btnDown" class="btn" type="button"><i class="icon-chevron-down"></i>&nbsp;下 移</button>&nbsp;
		<button id="btnCannel" class="btn"  type="button"><i class="icon-minus"></i>&nbsp;&nbsp;全部上移</button>
	</div>
	<div style="display:none;">
		<form method="post" id="queryFormAxm_hidden" class="B-small-from-table-autoConcise">
	     	<table class="B-table" width="100%">
	      		<!--可以再此处加入hidden域作为过滤条件 -->
	     		<TR  style="display:none;">
	        	<TD class="right-border bottom-border"></TD>
				<TD class="right-border bottom-border">
					<!-- <input type="text" fieldname="DCZLX" name="QDCZLX" value="3" keep="true" operation="=">  -->
				</TD>
	       		</TR>
	        	<!--可以再此处加入hidden域作为过滤条件 -->
	      	</table>
     	</form>
     </div>
	<div class="B-small-from-table-autoConcise">
	  <h4 class="title">
	  	待下达统筹计划列表&nbsp;&nbsp; 共<font  style=" margin-left:5px; margin-right:5px;;font-size:28px;color:red;">0</font>个
	  	<span class="pull-right">
	  		<button id="btnZc" class="btn" type="button">暂存</button>
		</span>
	  </h4>
		<div style="height:200px;overflow:auto;">
		<table class="table-hover table-activeTd B-table" id="xdtcjhxmList" width="100%" type="single" noPage="true" pageNum="1000">
			<thead>
				<tr>
					<th  name="XH" id="_XH" tdalign="center">&nbsp;#&nbsp;</th>
					<th fieldname="XMBH" tdalign="center" CustomFunction="doUp">&nbsp;操作&nbsp;</th>
					<th fieldname="XMBH">&nbsp;项目编号&nbsp;</th>
					<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
					<th fieldname="XJXJ" tdalign="center" >&nbsp;项目性质&nbsp;</th>
					<th fieldname="XMLX" tdalign="center">&nbsp;项目类型&nbsp;</th>
					<th fieldname="XMGLGS">&nbsp;项目管理公司&nbsp;</th>
					<th fieldname="XMSX" tdalign="center">&nbsp;项目属性&nbsp;</th>
					<th fieldname="ISBT" tdalign="center">&nbsp;BT&nbsp;</th>
					<th fieldname="XMDZ" maxlength="15">&nbsp;项目地址&nbsp;</th>
					<th fieldname="XMDZ" CustomFunction="doDz">&nbsp;&nbsp;</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
		</div>
	</div>
 	</div>
 	</div>
    </div>
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "PXH,XMBH"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
         <input type="hidden" name="ywid" id = "ywid" value="">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">	
 </FORM>
 <form method="post" id="queryForm">
 </form>
 </div>
</body>
</html>