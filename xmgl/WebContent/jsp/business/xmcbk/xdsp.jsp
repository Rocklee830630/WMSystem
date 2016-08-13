<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<title>下达审批</title>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script type="text/javascript" charset="utf-8">
 
var controllername= "${pageContext.request.contextPath }/xmcbk/xmcbkxdController.do";
var controllername_bl= "${pageContext.request.contextPath }/TaskAction.do";
var spxxid,sjzt,sjbh,sp_data,isxd,odd,memo;


//初始化查询
$(document).ready(function(){
	setNdOption();
	setDefaultOption($("#ND"),new Date().getFullYear());
	pcgl();
	query_zt();
});

//获取年度下拉值
function setNdOption(){
	var year=new Date().getFullYear();
	var num=document.getElementsByTagName("option");
	var year_num=year-2004;
	document.getElementById('ND').options.length=year_num+1;
	for(var i=0;i<year_num+1;i++)
	{
		num[i].id=i;
		num[i].innerHTML=parseInt(2005)+i;
	}
	document.getElementById('ND').value=num[i-1].innerHTML;
}
function query_zt()
{
	document.getElementById('SJZT').options[4].remove();
	document.getElementById('SJZT').options[4].remove();
	document.getElementById('SJZT').options[4].remove();
	document.getElementById('SJZT').options[4].remove();
}

//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
}


//年份查询
function generateNd(ndObj){
	ndObj.attr("src","T#GC_TCJH_SP:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(new Date().getFullYear());
}


//查询
$(function() {
	var btn=$("#example_query");
	btn.click(function() {
        pcgl();
	});
	
	
	//清空查询表单
    var btn_clearQuery=$("#query_clear");
    btn_clearQuery.click(function() {
        $("#queryForm").clearFormResult();
        setDefaultOption($("#ND"),new Date().getFullYear());
        //其他处理放在下面
    });

    	
	//新增批次
	$("#add_sp").click(function() {
		selectH();
		var nd = $("#ND").val();
		$(window).manhuaDialog({"title":"储备库项目>新增审批","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmcbk/addsp.jsp?flag=1&nd="+nd,"modal":"4"});
	});

	
	//修改审批
	$("#update_sp").click(function() {
		if(-1==$("#DT1").getSelectedRowIndex())
		{
			 requireSelectedOneRow();
			 return;
		}
		selectH();
		var nd = $("#ND").val();
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"储备库项目>修改审批","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmcbk/addsp.jsp?flag=2&nd="+nd,"modal":"4"});

	});
	

	//呈请审批
	$("#cqsp").click(function() {
		
		var ids = "";
		$("#DT2 tbody tr").each(function(){
			//存行数据
			var value=convertJson.string2json1($(this).attr("rowJson")).GC_TCJH_XMCBK_ID;
			ids+=value+",";
		});
		var result = verificationXmnd(controllername+"?verificationXmnd&ids="+ids);
		if(result){
			xWarning("<项目年度>不统一，请核对信息！");
			return;
		}else{
			var index1 = $("#DT1").getSelectedRowIndex();
			$("#resultXML").val($("#DT1").getSelectedRow());
		    odd=convertJson.string2json1($("#resultXML").val());
			sjbh=odd.SJBH;
			sjzt=odd.SJZT;
			ywlx="010002";
			if(index1<0) 
			{
				requireSelectedOneRow();
				return;
			}
			if(sjzt==1||sjzt==2)
			{
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
				var obj = convertJson.string2json1($("#DT1").getSelectedRow());
				var spfs = obj.SPFS;
				if(spfs == "1"){//手动通过
					xInfoMsg("手动通过,无审批信息！");
				}
				else
				{
					if(sjzt == 7)
					{
						backSp();
			 		} else if(sjzt == 8) {
			 			 var s = "${pageContext.request.contextPath}/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview=1";
			 		  	 $(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});
					} else {
						createSPconf(sjbh,ywlx,""); 	
			 		}					
				}	
			} 	
			selectH();
		}
		
	});
	
	//通过
	$("#btnTg").click(function() {
		var index1 = $("#DT1").getSelectedRowIndex();
		if(index1<0)
		{
			requireSelectedOneRow();
			return;
		}
		var ids = "";
		$("#DT2 tbody tr").each(function(){
			//存行数据
			var value=convertJson.string2json1($(this).attr("rowJson")).GC_TCJH_XMCBK_ID;
			ids+=value+",";
		});
		var result = verificationXmnd(controllername+"?verificationXmnd&ids="+ids);
		if(result){
			xWarning("<项目年度>不统一，请核对信息！");
			return;
		}else{
			var obj = convertJson.string2json1($("#DT1").getSelectedRow());
			var id = obj.GC_TCJH_SP_ID;
			xConfirm("信息确认","确认直接通过审批么？");
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').one("click",function(){
				var success = xmSptg(controllername+"?xmSptg&id="+id);
				//var success = true;
				if(success){
					$("#cqsp").attr("disabled",false);
					$("#update_sp").attr("disabled",true);
					$("#btnDelete").attr("disabled",true);
					$("#xd").attr("disabled",false);
					$("#selectxm").attr("disabled",true);
					$("#btnTg").attr("disabled",true);
					//xSuccessMsg("操作成功");
					var index= $("#DT1").getSelectedRowIndex();
					$("#DT1").cancleSelected();
					$("#DT1").setSelect(index);
				}else{
					xFailMsg('操作失败！',"");
				}
			});  
		}
	}); 
	
	//下达项目
	$("#xd").click(function() {
		var index1 = $("#DT1").getSelectedRowIndex();
		if(index1<0) 
		{
			requireSelectedOneRow();
			return;
		}
		selectH();
		var ids = "";
		var nd ="";
		var sjxmsxs="";
		$("#DT2 tbody tr").each(function(i){
			if(i==0){
				nd = convertJson.string2json1($(this).attr("rowJson")).ND;
			}
			//存行数据
			var value=convertJson.string2json1($(this).attr("rowJson")).GC_TCJH_XMCBK_ID;
			var sjxmsx=convertJson.string2json1($(this).attr("rowJson")).SJXMSX;
			ids+=value+",";
			sjxmsxs=sjxmsx+",";
		});
		$("#resultXML").val($("#DT1").getSelectedRow());
		//var obj = convertJson.string2json1($("#DT1").getSelectedRow());
		
		$(window).manhuaDialog({"title":"储备库项目下达","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmcbk/xdxx.jsp?ids="+ids+"&spxxid="+spxxid+"&nd="+nd+"&sjxmsxs="+sjxmsxs,"modal":"4"});
	});


	//选择项目
	$("#selectxm").click(function() {
		if(-1==$("#DT1").getSelectedRowIndex())
		{
			 requireSelectedOneRow();
			 return;
		}
		switch(sjzt)
		{
			case 1:
				xInfoMsg("该批次正在审批，不可再次选择项目！")
				return;
				break;
			case 2:
				xInfoMsg("该批次已审批通过，不可再次选择项目！")
				return;
				break;
			default:
				selectH();
				var obj = convertJson.string2json1($("#DT1").getSelectedRow());
				var nd = obj.ND;
				$(window).manhuaDialog({"title":"项目储备库  >项目下达","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmcbk/xd.jsp?nd="+nd,"modal":"1"});
				break;
		}	
	});
	//按钮绑定事件(删除)
	$("#btnDelete").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		xConfirm("提示信息","是否确认删除！");
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').one("click",function(){ 
				var rowValue = $("#DT1").getSelectedRow();
				var data1 = defaultJson.packSaveJson(rowValue);
				defaultJson.doDeleteJson(controllername+"?deleteSp",data1,DT1,"deleteHuiDiao"); 
			});
	});
});
//删除回调
function  deleteHuiDiao(){
	 $("#DT2 tbody").children().empty();
}

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
	var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
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


//行删除
function delete_sp(obj){
	if(obj.SJZT==0 || obj.SJZT==7)
	{
		if(obj.SPFS != "1"){
			return "<button name=\"btnDownTable\" onclick=\"row_delete(this)\" class=\"btn btn-link\" type=\"button\"><i title=\"删除\" class=\"icon-remove\"></i></button>";
		}else{
			return "<button name=\"btnDownTable\" onclick=\"delete_info(\'"+obj.SJZT+"\',\'"+obj.ISXD+"\',\'"+obj.SPFS+"\')\" class=\"btn btn-link\" type=\"button\"><i title=\"删除\" class=\"icon-remove\"></i></button>";	
		}
	}
	else
	{
		return "<button name=\"btnDownTable\" onclick=\"delete_info(\'"+obj.SJZT+"\',\'"+obj.ISXD+"\',\'"+obj.SPFS+"\')\" class=\"btn btn-link\" type=\"button\"><i title=\"删除\" class=\"icon-remove\"></i></button>";						
	}	
}


//删除提示信息
function delete_info(sjzt,isxd,spfs)
{
	if(spfs=="1"){
		xInfoMsg('该批次项目已通过，不能删除！');
		return;
	}
	if(sjzt==1)
	{
		xInfoMsg('该批次项目正在审批，不能删除！');
	}
	else
	{
		if(isxd==0)
		{
			xInfoMsg('该批次项目已审批，不能删除！');
		}
		else
		{
			xInfoMsg('该批次项目已下达，不能删除！');
		}	
	}	
}


//删除数据方法
function row_delete(obj){
	var rowValue = $(obj).parent().parent().attr("rowJson");
	var obj=convertJson.string2json1(rowValue);
	defaultJson.doDeleteJson1(controllername+"?delete_sp&cbkid="+obj.GC_TCJH_XMCBK_ID,'query_delete');
}


//自定义删除方法
defaultJson.doDeleteJson1 = function(actionName,callbackFunction) {
    var success  = true;
    var isAsync = false;
    if(callbackFunction!=undefined){
 	   isAsync = true;
    }
	$.ajax({
		url : actionName,
		dataType : 'json',
		async :	isAsync,
		type : 'post',
		success : function(result) {
			//将返回值增加到TabList中,将返回的串转换为数组形式
			//add by zhangbr@ccthanking.com BEGIN 
			//上传附件时，需要使用resultXML中的值，所以此处查询成功后，要将返回结果赋值给resultXML
			//如果页面不存在resultXML，附件上传时也有判断，不会报错
			//add by zhangbr@ccthanking.com END
			$("#resultXML").val(result.msg);
			var prompt = result.prompt;
 			if(!prompt){
 				prompt =g_prompt[0];
 			}
			defaultJson.clearTxtXML();
			success = true;
			if(isAsync==true){
				  eval(callbackFunction+"()");
			}
		},
	    error : function(result) {
		     	alert(result.msg);
			    defaultJson.clearTxtXML();
			    success = false;

		}
	});
	 return success;
};


//插入审批项目
function insert_sp(ids)
{
	$("#resultXML").val($("#DT1").getSelectedRow());
    odd=convertJson.string2json1($("#resultXML").val());
    spxxid=odd.GC_TCJH_SP_ID;
	doInsertJson_sp(controllername + "?insert_sp&ids="+ids+"&spxxid="+spxxid, null,null,"insert");
}


//插入批次后行更新
function query_add(data)
{
	var subresultmsgobj1=defaultJson.dealResultJson(data);
	$("#DT1").insertResult(JSON.stringify(subresultmsgobj1),DT1,1);
	$("#DT1").cancleSelected();
	$("#DT1").setSelect(0);
	$("#DT2").clearResult();
	$("#resultXML").val($("#DT1").getSelectedRow());
    odd=convertJson.string2json1($("#resultXML").val());
	pcbid=odd.GC_CBK_PCB_ID;
	sjzt=odd.SJZT;
	btnSwitch(sjzt);
	xSuccessMsg("操作成功");
}


//修改批次后更新
function query_update(data)
{
	var index= $("#DT1").getSelectedRowIndex();
	var subresultmsgobj1=defaultJson.dealResultJson(data);
	$("#DT1").updateResult(JSON.stringify(subresultmsgobj1),DT1,index);
	$("#DT1").cancleSelected();
	$("#DT1").setSelect(index);
	btnSwitch(sjzt);
	xSuccessMsg("操作成功");

}


//删除回调函数
function query_delete()
{
	query_sp(spxxid);
	btnSwitch(sjzt);
	selectH();
}


//插入审批回调函数
function insert()
{
	query_sp(spxxid);
	sjzt=odd.SJZT;
	btnSwitch(sjzt);
}


//呈请审批提交后回调
function prcCallback()
{
	actionName=controllername+"?update_spzt&sjbh="+sjbh;
	update_spzt(actionName);
	var index= $("#DT1").getSelectedRowIndex();
	if(sp_data != 0){
		var subresultmsgobj1=defaultJson.dealResultJson(sp_data);
		$("#DT1").updateResult(JSON.stringify(subresultmsgobj1),DT1,index);
	}
	$("#DT1").cancleSelected();
	$("#DT1").setSelect(index);
	$("#resultXML").val($("#DT1").getSelectedRow());
    odd=convertJson.string2json1($("#resultXML").val());
    sjzt=odd.SJZT;
	btnSwitch(sjzt);
	query_sp(spxxid);
}


//再次呈请审批提交后回调
function gengxinchaxun()
{
	prcCallback();
}

//更新审批状态
function update_spzt(actionName) {
var success  = true;
	$.ajax({
		url : actionName,
		dataType : 'json',
		async :	false,
		type : 'post',
		success : function(result) {
			sp_data=result.msg;
		},  
	});
	 return success;
};


//DT1行选中
function selectH()
{
	var index= $("#DT1").getSelectedRowIndex();
	$("#DT2").cancleSelected();
	$("#DT1").setSelect(index);
}


//插入
doInsertJson_sp = function(actionName, data1,tablistID,callbackFunction) {
var success  = true;

	var isAsync = false;
	if (callbackFunction != undefined) {
		isAsync = true;
	}
	$.ajax({
		url : actionName,
		data : data1,
		dataType : 'json',
		async :	isAsync,
		type : 'post',    
		success : function(result) {
			$("#resultXML").val(result.msg);
			success_flag=result.msg;
			var prompt = result.prompt;
			if(!prompt){
				prompt =g_prompt[0];
			}
			defaultJson.clearTxtXML();
			success = true;
			if(isAsync==true){
			  eval(callbackFunction+"()");
			}
			},
	    error : function(result) {
			    defaultJson.clearTxtXML();
			    success = false;

		}
	});
	 return success;
};


//审批信息查询
function pcgl()
{
	//生成json串
	//$("#px").attr("fieldname","LRSJ");
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?query_sp_info",data,DT1,null,true);
}

//查询审批项目
function query_sp(spxxid)
{
	//$("#px").attr("fieldname","XMBH");
	var data=combineQuery.getQueryCombineData(queryForm2,frmPost,DT2);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query_sp&spxxid="+spxxid,data,DT2,null,true);	
}


//点击获取行对象
function tr_click(obj,tabListid){
	if(tabListid=="DT1")
	{
		spxxid=obj.GC_TCJH_SP_ID;
		sjzt=obj.SJZT;
		sjbh=obj.SJBH;
		isxd=obj.ISXD;
		memo=obj.MEMO;
		query_sp(spxxid);
		var num=$("#DT2 tr").length-1;
		//控制按钮状态
		btnSwitch(sjzt);
	}
}


//按钮状态切换
function btnSwitch(sjzt)
{
	var num=$("#DT2 tr").length-1;
	var obj = convertJson.string2json1($("#DT1").getSelectedRow());
	var spfs = obj.SPFS;
	var isxd = obj.ISXD;
	if(spfs == "1"){//手动通过
		$("#update_sp").attr("disabled",true);
		$("#btnDelete").attr("disabled",true);
		$("#selectxm").attr("disabled",true);
		$("#cqsp").attr("disabled",false);
		$("#cqsp")[0].innerText = "审批信息";
		if(isxd == "1"){
			$("#xd").attr("disabled",true);
		}else{
			$("#xd").attr("disabled",false);
		}
		$("#btnTg").attr("disabled",true);
		return;
	}
	switch(sjzt)
	{
	case '0':
		$("#cqsp")[0].innerText = "呈请审批";
		if(num<=0)
		{
			$("#update_sp").attr("disabled",false);
			$("#btnDelete").attr("disabled",false);
			$("#selectxm").attr("disabled",false);
			$("#cqsp").attr("disabled",true);
			$("#btnTg").attr("disabled",true);
		}
		else
		{
			$("#cqsp").attr("disabled",false);
			$("#update_sp").attr("disabled",false);
			$("#btnDelete").attr("disabled",false);
			$("#selectxm").attr("disabled",false);
			$("#btnTg").attr("disabled",false);
		}	
		$("#xd").attr("disabled",true);
		break;
	case '1':
		$("#cqsp")[0].innerText = "审批信息";
		$("#cqsp").attr("disabled",false);
		$("#update_sp").attr("disabled",true);
		$("#btnDelete").attr("disabled",true);
		$("#xd").attr("disabled",true);
		$("#selectxm").attr("disabled",true);
		$("#btnTg").attr("disabled",true);
		break;
	case '2':
		$("#cqsp")[0].innerText = "审批信息";
		if(memo==''||memo=='undefine')
		{
			$("#cqsp").attr("disabled",true);
		}
		else
		{
			$("#cqsp").attr("disabled",false);
		}	
		$("#update_sp").attr("disabled",true);
		$("#btnDelete").attr("disabled",true);
		$("#selectxm").attr("disabled",true);
		$("#btnTg").attr("disabled",true);
		if(isxd==1)
		{
			$("#xd").attr("disabled",true);
		}
		else
		{
			$("#xd").attr("disabled",false);
		}	
		break;
	case '7':
		$("#cqsp")[0].innerText = "再次呈请";
		if(num<=0)
		{
			$("#update_sp").attr("disabled",false);
			$("#btnDelete").attr("disabled",false);
			$("#selectxm").attr("disabled",false);
			$("#cqsp").attr("disabled",true);
			$("#btnTg").attr("disabled",true);
		}
		else
		{
			$("#cqsp").attr("disabled",false);
			$("#update_sp").attr("disabled",false);
			$("#btnDelete").attr("disabled",false);
			$("#selectxm").attr("disabled",false);
			$("#btnTg").attr("disabled",true);
		}	
		$("#xd").attr("disabled",true);	
		break;
	case '8':
		$("#cqsp")[0].innerText = "呈请审批";
		if(num<=0)
		{
			$("#update_sp").attr("disabled",false);
			$("#btnDelete").attr("disabled",false);
			$("#selectxm").attr("disabled",false);
			$("#cqsp").attr("disabled",true);
			$("#btnTg").attr("disabled",true);
		}
		else
		{
			$("#cqsp").attr("disabled",false);
			$("#update_sp").attr("disabled",false);
			$("#btnDelete").attr("disabled",false);
			$("#selectxm").attr("disabled",false);
			$("#btnTg").attr("disabled",false);
		}	
		$("#xd").attr("disabled",true);	
		break;
	}
}


//下达成功后回调
function insert_xd(){
	prcCallback();
	query_sp(spxxid);
	$("#resultXML").val($("#DT1").getSelectedRow());
    odd=convertJson.string2json1($("#resultXML").val());
	sjzt=odd.SJZT;
	isxd=odd.ISXD;
	btnSwitch(sjzt);
 	xSuccessMsg('操作成功！');
 	//var parentmain=$(window).manhuaDialog.getParentObj();
 	//parentmain.query();
 };
 
 
//下达失败后回调
function nosuccess()
{
	xFailMsg('操作失败！选择的批次已被其他用户下达，请重新选择批次');
}

 
//处理审批名称
function submc(obj)
{
	var mc=obj.MEMO;
	if(mc==''||mc=='undefinde'||mc==null)
	{
		return mc;
	}
	else
	{
		var end=mc.indexOf('】');
		r=mc.substring(1,end); 
		return r;
	}	
}

//处理审批状态
function getsjzt(obj)
{
	var zt='';
	switch(obj.SJZT)
	{
		case '0':
			zt='<span class="label background-color">'+obj.SJZT_SV+'</span>'
			break;
		case '1':
			zt='<span class="label label-important-orange">'+obj.SJZT_SV+'</span>'
			break;
		case '2':
			zt='<span class="label label-success">'+obj.SJZT_SV+'</span>'
			break;
		case '7':
			zt='<span class="label label-important">'+obj.SJZT_SV+'</span>'
			break;		
	}
	return zt;
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
//==========================================
	//项目直接通过审批调用方法
	function xmSptg(actionName){
		var success = true;
		$.ajax({
			type : 'post',
			url : actionName,
			dataType : 'json',
			async :	false,
			success : function(result) {
				query_update(result.msg);
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
	//详细信息
	function rowView(obj){
	    //var obj_json=$("#DT1").getSelectedRowJsonByIndex(index);//获取行json串
		//var obj=convertJson.string2json1(obj_json);//获取行对象
		var id_xxxx=obj.GC_TCJH_XMCBK_ID;//取行对象<项目编号>
		var zt=obj.ISXD;
		var xdlx=obj.XDLX;
		var pcid=obj.PCID;
		var pch=obj.PCH;//取行对象<项目批次号>
		var showStr = "";
		if((obj.XMBM).substring(0,5)=="XXXXX"){
			showStr = "<abbr title='"+obj.XMBM+"'>"+obj.XMBH+"</abbr>";
		}else if(obj.XMBM==""){
			showStr = obj.XMBH;
		}else{
			showStr = "<abbr title='"+obj.XMBM+"'>"+(obj.XMBM).substring(0,14)+"</abbr>";
		}
		return showStr;
	}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">
				审批批次
				<span class="pull-right">
					<button class="btn" id="add_sp">新增审批</button>
					<button class="btn" id="update_sp">修改审批</button>
					<button id="btnDelete" class="btn" type="button">删除</button>
					<app:oPerm url="jsp/business/xmcbk/xdsp.jsp?xmxdtg">
						<button class="btn" id="btnTg" >通过</button>
					</app:oPerm>
					<button class="btn" id="cqsp" >呈请审批</button>
					<button class="btn" id="xd">下达</button>				
				</span>
			</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT type="text" class="span12" kind="text" id="splx" fieldname="SPLX" value="1" keep="true" operation="="/>
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>
						<th width="5%" class="right-border bottom-border">审批年度</th>
						<td width="7%" class="right-border bottom-border">
							 <select id="ND" class="span12 year" name="ND" fieldname="ND"  operation="=" ></select> 
						</td>
						<th width="5%" class="right-border bottom-border text-right">审批名称</th>
          				<td class="right-border bottom-border">
          					<input class="span12" type="text" name="QSPMC" fieldname="SPMC" operation="like" id="QSPMC" />
		  				</td>
						<th width="5%" class="right-border bottom-border">审批状态</th>
						<td class="right-border bottom-border">
							<select	kind="dic" src="SJZT" id=SJZT class="span12 4characters" name="SJZT" fieldname="SJZT" operation="=" defaultMemo="全部"></select>
						</td>						
			            <td class="text-left bottom-border text-right">
							<button	id="example_query" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
	                        <button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
			            </td>
					</tr>
				</table>
			</form>	
			<div style="height:5px;"> </div>		
			<div class="B-small-from-table-autoConcise" width="100%">
				<div style="height:200px;overflow:auto;">
					<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" noPage="true" pageNum="1000">
		                <thead><tr>		                		
							<th  name="XH" id="_XH" tdalign="center">&nbsp;#&nbsp;</th>
							<th fieldname="SJZT" tdalign="center" CustomFunction="getsjzt">&nbsp;审批状态&nbsp;</th>
							<th fieldname="SPMC" maxlength="30" >&nbsp;审批名称&nbsp;</th>	
							<th fieldname="ND" tdalign="center">&nbsp;审批年度&nbsp;</th>				
							<th fieldname="PCH" tdalign="center" CustomFunction="doPch">&nbsp;下达批次号&nbsp;</th>
							<th fieldname="RESULTDSCR" tdalign="center" CustomFunction="doSpyj">&nbsp;审批意见&nbsp;</th>
							<th fieldname="CLOSETIME" tdalign="center" CustomFunction="doSpsj">&nbsp;审批时间&nbsp;</th>
							<th fieldname="SPFS" tdalign="center" CustomFunction="doSpfs">&nbsp;通过方式&nbsp;</th>
							<th fieldname="BZ" maxlength="25">&nbsp;备注&nbsp;</th>
							<th fieldname="LRSJ" tdalign="center">&nbsp;登记时间&nbsp;</th>
							<th fieldname="ISXD" tdalign="center">&nbsp;是否下达&nbsp;</th>
							<!-- <th fieldname="FJ" tdalign="center" noprint="true">&nbsp;附件&nbsp;</th>	 -->					
							<!-- <th fieldname="XMBH" tdalign="center" CustomFunction="doDwon">&nbsp;操作&nbsp;</th> -->
		                </tr></thead> 
		              	<tbody></tbody>
		           </table>
		       </div>
		 	</div>
	 		<br><br>
	 		<div style="height:5px;"> </div>	
			<div class="B-small-from-table-autoConcise">
				<h4>
					审批项目&nbsp;&nbsp; <font style=" margin-left:5px; margin-right:5px;;font-size:16px;">(所选项目必须为同一年度)</font> 
				 	<span class="pull-right">
						<button class="btn" id="selectxm">选择项目</button>
				 	</span>					
				</h4>
				<div style="height:5px;"> </div>	
				<div style="height:200px;overflow:auto;">
			  		<form method="post" id="queryForm2" class="B-small-from-table-autoConcise">
				      	<!--可以再此处加入hidden域作为过滤条件 -->
				      	<TR style="display:none;">
					        <TD class="right-border bottom-border"></TD>
							<TD class="right-border bottom-border">
								<!-- <input type="hidden" id="DCZLX" name="DCZLX" fieldname="DCZLX" value="1" operation="="/> -->
							</TD>
			       		</TR>
				    </form>	
					<table class="table-hover table-activeTd B-table" id="DT2" width="100%" type="single" noPage="true" pageNum="1000" nopromptmsg="true">
						<thead>
							<tr>
								<th  name="XH" id="_XH" tdalign="center">&nbsp;#&nbsp;</th>
								<th fieldname="XMBH" CustomFunction="rowView">&nbsp;项目编号&nbsp;</th>
								<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>						
								<th fieldname="XMLX" tdalign="center">&nbsp;项目类型&nbsp;</th>
								<th fieldname="JHZTZE" tdalign="right">&nbsp;年度总投资额（万元）&nbsp;</th>
								<th fieldname="ISBT" tdalign="center">&nbsp;BT&nbsp;</th>
								<th fieldname="XMDZ" maxlength="15">&nbsp;项目地址&nbsp;</th>
								<th fieldname="XMBH" tdalign="center" CustomFunction="delete_sp">&nbsp;操作&nbsp;</th>
	 						</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>		  
			</div>
		</div>     
	</div>
</div>	
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ" id="px"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
</div>
</body>
</html>