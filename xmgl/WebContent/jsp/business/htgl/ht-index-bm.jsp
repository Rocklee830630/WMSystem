
<%@page import="java.sql.Connection"%>
<%@page import="com.ccthanking.framework.common.DBUtil"%><!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>合同首页</title>
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
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtController.do";
var controllername_bl= "${pageContext.request.contextPath }/TaskAction.do";

var cz = '';

function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(3)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	setPageHeight();
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		
		//if($("#QZBBM").val()!=""){
		////	var v = $("#QZBBM").find("option:selected").text();  
		//	$("#ZBBM").val(v);
		//}
		
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,DT1, null, true);
	});
	
	//按钮绑定事件(修改合同变更信息)
	$("#btnHtChange").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		cz = 'u';
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"部门合同>合同变更信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtbg-indexOne.jsp?type=update","modal":"1"});
	});
	//按钮绑定事件(新增)
	$("#btnInsert").click(function() {
		$(window).manhuaDialog({"title":"部门合同>新增","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/ht-add-bm.jsp?type=insert","modal":"1"});
	});
	//按钮绑定事件(修改)
	$("#btnUpdate").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"部门合同>修改","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/ht-add-bm.jsp?type=update","modal":"1"});
	});
	$("#btnHTQD").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		 cz = 'u';
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"部门合同>合同签订","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/ht-update-bm.jsp?type=update","modal":"1"});
	});
	$("#btnExt").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"部门合同>合同扩展","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/ht-update-bm-ext.jsp?type=update","modal":"1"});
	});
	
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 //var t = $("#DT1").getTableRows();
		// if(t<=0)
		 //{
			// xAlert("信息提示","请至少查询出一条记录！");
			 //return;
		 //}
	  	 $(window).manhuaDialog({"title":"导出","type":"text","content":"${pageContext.request.contextPath }/jsp/framework/print/TabListEXP.jsp?tabId=DT1","modal":"3"});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
        $("#QZBBMID").val('<%=deptId%>');
    });
	
    
    
    $("#btnView").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		rowView($("#DT1").getSelectedRowIndex());
	});

    $("#btnWctz").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		 cz = 'u';
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"部门合同>合同完成投资","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtwctz-indexOne.jsp?hasHt=one&fromPage=htindex","modal":"1"});
	});
	
    $("#btnDelete").click(function() {
    	var rowinx = $("#DT1").getSelectedRowIndex();
    	if(rowinx==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		xConfirm("信息确认","您确定删除所选合同吗？ ");
		
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){
			$("#btnDelete").attr("disabled", true);
			 var rowJson = $("#DT1").getSelectedRow();
			 var data1 = defaultJson.packSaveJson(rowJson);
			 var flag = defaultJson.doDeleteJson(controllername+"?delete", data1, null);
			 if(flag){
				 $("#DT1").removeResult(rowinx);
				 xAlert("信息提示","删除成功！");
			 }
		});
	});
});

//页面默认参数
function init(){
	getNd();
	g_bAlertWhenNoResult = false;
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,DT1,null,true);
	g_bAlertWhenNoResult = true;
}
//默认年度
function getNd(){
	//年度信息，里修改
	var y = new Date().getFullYear();
	$("#QQDNF option").each(function(){
		if(this.value == y){
		 	$(this).attr("selected", true);
		 	return true;
		}
	});
}

//点击获取行对象
function tr_click(obj,tabListid){
<%--	HTRXZT	合同履行类型--%>
<%--	-3	未审批	新建--%>
<%--	-2	审批中	呈请审批--%>
<%--	-1	已审批	审批信息   审批通过   查看审批信息--%>
<%--	0	审核中	部门：部门提交审批    合同管理部：	签订审核--%>
<%--	1	履行中	合同管理部：签订履行--%>
<%--	2	已结算--%>
<%--	3	已结束--%>
<%--	4	已中止--%>
	//alert(JSON.stringify(obj));
	$('#ID').val(obj.ID);
	//$("#btnViewSptg").attr("disabled",true);
	
	$("#btnDelete").attr("disabled",true);
	$("#btnUpdate").attr("disabled",true);
	$("#btnHtChange").attr("disabled",true);
	$("#btnJs").attr("disabled",true);
	$("#sp_btn").attr("disabled",true);
	$("#btnViewSp").attr("disabled",false);
	$("#btnHTQD").attr("disabled",true);
	if(obj.HTZT=="-3"){
		//未审批的数据是允许删除和修改的
		$("#btnDelete").removeAttr("disabled");
		$("#btnUpdate").removeAttr("disabled");
		$("#sp_btn").removeAttr("disabled");
		$("#btnViewSp").attr("disabled",true);
		$("#btnView").attr("disabled",true);
		$("#btnWctz").attr("disabled","disabled");
	}else if(obj.HTZT=="-2"){
		// 退回的业务可以更改,也允许删除
		if(obj.EVENTSJBH=="5" || obj.EVENTSJBH=="7"){
			$("#btnUpdate").removeAttr("disabled");
			$("#btnDelete").removeAttr("disabled");
		}
		$("#btnView").attr("disabled",true);
		//$("#btnViewSptg").removeAttr("disabled");
		$("#btnWctz").attr("disabled","disabled");
	}else if(obj.HTZT=="-1"){
		$("#btnHTQD").removeAttr("disabled");
		$("#btnView").removeAttr("disabled");
		$("#btnWctz").attr("disabled","disabled");
		$("#btnUpdate").attr("disabled","disabled");
	}else if(obj.HTZT=="0"){
		$("#btnView").attr("disabled",true);
		$("#btnWctz").attr("disabled","disabled");
		$("#btnUpdate").attr("disabled","disabled");
	}else if(obj.HTZT=="1"){
		if(obj.HTLX!="SG"){
			$("#btnJs").removeAttr("disabled");
		}
		$("#btnWctz").removeAttr("disabled");
		$("#btnView").removeAttr("disabled");
		$("#btnHtChange").removeAttr("disabled");
		$("#btnUpdate").attr("disabled","disabled");
	}else if(obj.HTZT=="2"){
		$("#btnView").removeAttr("disabled");
		$("#btnWctz").attr("disabled","disabled");
		$("#btnUpdate").attr("disabled","disabled");
	}else if(obj.HTZT=="3"){
		$("#btnView").removeAttr("disabled");
		$("#btnWctz").attr("disabled","disabled");
		$("#btnUpdate").attr("disabled","disabled");
	}else if(obj.HTZT=="4"){
		$("#btnView").removeAttr("disabled");
		$("#btnWctz").attr("disabled","disabled");
		$("#btnUpdate").attr("disabled","disabled");
	}
	//	alert( obj.HTZT_SV+"|"+obj.EVENTSJBH_SV+"|"+obj.EVENTSJBH);
}

//回调函数--用于修改新增
getWinData = function(data){
	//var subresultmsgobj = defaultJson.dealResultJson(data);
	var data1 = defaultJson.packSaveJson(data);
	if(JSON.parse(data).ID == "" || JSON.parse(data).ID == null){
		defaultJson.doInsertJson(controllername + "?insert", data1,DT1);
	}else{
		defaultJson.doUpdateJson(controllername + "?update", data1,DT1);
	}
	
};

//详细信息
function rowView(index){
	$("#DT1").setSelect(index);
	if($("#DT1").getSelectedRowIndex()==-1)
	 {
		requireSelectedOneRow();
	    return
	 }
	$("#resultXML").val($("#DT1").getSelectedRow());
	$(window).manhuaDialog({"title":"部门合同>合同履行信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/ht-view.jsp?type=detail","modal":"1"});
}
function gengxinchaxun(type){
	
	if(type=='insert'){
		$("#btnQuery").click();
	}else if(type=='update'){
		var index =	$("#DT1").getSelectedRowIndex();
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		var tempJson = convertJson.string2json1(data);
		var a=$("#DT1").getCurrentpagenum();
		tempJson.pages.currentpagenum=a;
		
		data = JSON.stringify(tempJson);
		defaultJson.doQueryJsonList(controllername+"?query",data,DT1,null,false);

		$("#DT1").cancleSelected();
		$("#DT1").setSelect(index);
	}else if(type=='操作成功！') { // 在业务页面操作流程时，type传过来的是操作的返回值。ArchiveTaskAction.java类中doBreakSPAction方法 add by xhb
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		data = defaultJson.getQueryConditionWithNowPageNum(data,"DT1");
		defaultJson.doQueryJsonList(controllername+"?query",data,DT1, 'setButtonCallBack', true);
	} else { // 当type为空的时候，是Mylcgl里面点击关闭的时候调用的。
		var row_index=$("#DT1").getSelectedRowIndex();
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		var tempJson = convertJson.string2json1(data);
		var a=$("#DT1").getCurrentpagenum();
		tempJson.pages.currentpagenum=a;
		data = JSON.stringify(tempJson);
		defaultJson.doQueryJsonList(controllername+"?query",data,DT1);
		$("#DT1").setSelect(row_index);
		successInfo("","");
		var selObj = $("#DT1").getSelectedRowJsonObj();//获得选中行的索引
		tr_click(selObj, DT1);
	}
}

function setButtonCallBack() {
	var obj = $("#DT1").getSelectedRowJsonObj();
	tr_click(obj,"DT1");
}
function closeParentCloseFunction(){
	if(cz=='u'){
		var index =	$("#DT1").getSelectedRowIndex();
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		var tempJson = convertJson.string2json1(data);
		var a=$("#DT1").getCurrentpagenum();
		tempJson.pages.currentpagenum=a;
		data = JSON.stringify(tempJson);
		defaultJson.doQueryJsonList(controllername+"?query",data,DT1,null,false);
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(index);
	}
}
//呈请审批
$(function() {
	$("#sp_btn11111").click(function() {
		//获取ywlx,sjbh
		
		if($("#DT1").getSelectedRowIndex()==-1) {
			requireFormMsg("请选择一条要操作的数据！");
		} else {
			var rowjson =	$("#DT1").getSelectedRow();
			
			var sjbh = JSON.parse(rowjson).SJBH;
			var ywlx = JSON.parse(rowjson).YWLX;
			var rowid = JSON.parse(rowjson).ID;
			
			
			cqspLocal(rowjson);
			
		}
	});
	
	var sp_btn = $("#sp_btn");
	sp_btn.click(function() 
 			{
				var index1 =	$("#DT1").getSelectedRowIndex();
		 		if(index1<0) 
				{
		 			requireSelectedOneRow();
		 			return;
				}
		 		var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
		 		var ywlx = $("#DT1").getSelectedRowJsonObj().YWLX;
		 		var condition = "1";
		 		var obj = $("#DT1").getSelectedRowJsonObj();
		 		var xqzt = obj.HTZT;
		 		if(xqzt!=-3) {
		 			
		 				var s = "${pageContext.request.contextPath}/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview=1";   
			 		  	$(window).manhuaDialog({"title":"审批信息","type":"text","content":s,"modal":"1"});  
		 		}else{

			 		cz = 'u';
		 			//alert("审批");
		 			createSPconf(sjbh,ywlx,condition);
		 			 $("#DT1").cancleSelected();
		 		}
			 });
});
function queryLcxx(){
	var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
	var str = "";
	$.ajax({
		url: "${pageContext.request.contextPath }/TaskAction.do?getFwLc&sjbh="+sjbh,
		data:"",
		dataType:"json",
		async:false,
		success:function(result){
			str = result.msg;
		}
	});
	return str;
}

//审批信息查看
$(function() {
	$("#btnViewSp").click(function() {
		var index1 =	$("#DT1").getSelectedRowIndex();
 		if(index1<0) 
		{
 			requireSelectedOneRow();
 			return;
		}
 		var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
 		var ywlx = $("#DT1").getSelectedRowJsonObj().YWLX;
 		var condition = "";
 		var obj = $("#DT1").getSelectedRowJsonObj();
 		if($("#DT1").getSelectedRowJsonObj().EVENTSJBH == 7) {
			var value=queryLcxx();
 			var tempJson = convertJson.string2json1(value);
 			var rowObject = tempJson.response.data[0];
 		   	var url =  '${pageContext.request.contextPath}/'+rowObject.LINKURL+'?taskid='+rowObject.ID+'&taskseq='+rowObject.SEQ+'&sjbh='+rowObject.SJBH+'&ywlx='+rowObject.YWLX+'&spbh='+rowObject.SPBH+'&rwlx='+rowObject.RWLX+"&isRead="+rowObject.XB;
 		   	$(window).manhuaDialog({"title":"审批信息","type":"text","content":url,"modal":"1"});
		} else {
			var s = "${pageContext.request.contextPath}/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview=1";   
			$(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});
		}
	});
});

//审批信息， 暂整为已通过
$(function() {
	$("#btnViewSptg").click(function() {
		//获取ywlx,sjbh
		if($("#DT1").getSelectedRowIndex()==-1) {
			requireFormMsg("请选择一条要操作的数据！");
		} else {
			var rowjson = $("#DT1").getSelectedRow();
			var tempJson = convertJson.string2json1(rowjson);
			var cqspFlag = getCqspFlag(tempJson.SJBH);
			if(cqspFlag=="true"){
				xAlert("警告","该数据已经发起审批流程，不允许使用通过功能!","3");
				return;
			}else{
				cz = 'u';
				xConfirm("信息确认","提交后记录为不可修改！是否确认提交所选的记录？ ");
				$('#ConfirmYesButton').attr('click','');
				$('#ConfirmYesButton').one("click",function(){
					cqspLocal_tg(rowjson);
				});
			}
		}
	});
});

//--------------------------------------
//-获取当前数据是否已经进行了“呈请审批”操作标志
//--------------------------------------
function getCqspFlag(n){
	var str = "";
	$.ajax({
		url:controllername_bl + "?getCqspFlag&sjbh="+n,
		data:"",
		dataType:"json",
		async:false,
		success:function(result){
			str = result.msg;
		}
	});
	return str;
}
function cqspLocal_tg(rowjson){
	var rowid = JSON.parse(rowjson).ID;
	$.ajax({
		url : controllername+"?update",
		data : "opttype=cqsptg&id="+rowid,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(result) {
			$("#resultXML").val(result.msg);

			var rowindex = $("#DT1").getSelectedRowIndex();
			var subresultmsgobj = defaultJson.dealResultJson(result.msg);
			/*
			 * 如果返回结果为单表，更新的table也为单表则可以直接将subresultmsgobj传给updateResult使用，否则需要如下方式组成新的json
			 * 
			 */
			var comprisesJson = $("#DT1").comprisesJson(subresultmsgobj,rowindex);
			var strarr = $("#DT1").updateResult(JSON.stringify(comprisesJson),DT1,rowindex);

			$("#DT1").setSelect(rowindex);
		}
	});
}

//本地记录处理
function cqspLocal(rowjson){
	var rowid = JSON.parse(rowjson).ID;
	$.ajax({
		url : controllername+"?update",
		data : "opttype=cqsp&id="+rowid,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(result) {
			$("#resultXML").val(result.msg);

			var rowindex = $("#DT1").getSelectedRowIndex();
			var subresultmsgobj = defaultJson.dealResultJson(result.msg);
			/*
			 * 如果返回结果为单表，更新的table也为单表则可以直接将subresultmsgobj传给updateResult使用，否则需要如下方式组成新的json
			 * 
			 */
			var comprisesJson = $("#DT1").comprisesJson(subresultmsgobj,rowindex);
			var strarr = $("#DT1").updateResult(JSON.stringify(comprisesJson),DT1,rowindex);

			$("#DT1").setSelect(rowindex);
		}
	});
}


//状态颜色判断
function docolor(obj)
{
	var xqzt=obj.HTZT;
	if(xqzt=="-3"){
		return '<span class="label label-danger">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="2"){
	  	return '<span class="label label-danger">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="3"){
	  	return '<span class="label label-danger">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="4"){
	  	return '<span class="label label-danger">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="3"){
	  	return '<span class="label label-warning">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="1"){
	  	return '<span class="label label-success">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="0"){
	 	return '<span class="label label-success">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="-2"){
		if(obj.EVENTSJBH=="5" || obj.EVENTSJBH=="6" || obj.EVENTSJBH=="7"){
			return '<span class="label label-success">'+obj.EVENTSJBH_SV+'</span>';
		}else{
			return '<span class="label label-warning">'+obj.HTZT_SV+'</span>';
		}
	}else if(xqzt=="-1"){
	 	return '<span class="label label-danger">'+obj.HTZT_SV+'</span>';
	}else{
		return obj.HTZT_SV;
	}
	
}

function rowView1(t){
	if($("#DT1").getSelectedRowIndex()==-1)
	 {
		requireSelectedOneRow();
	    return
	 }
	$("#resultXML").val($("#DT1").getSelectedRow());
	var rowValue = $("#DT1").getSelectedRow();
	var tempJson = convertJson.string2json1(rowValue);
	var idvar = tempJson.ID;
	$(window).manhuaDialog({"title":"部门合同>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/ht-view.jsp?type=detail","modal":"1"});
}
function doRandering(obj){
	var showHtml = "";
	showHtml = "<a href='javascript:rowView1(this);' title='查看详细信息'><i class='icon-file showXmxxkInfo'></i></a>";
	return showHtml;
}
function doRandering_ZWCZF(obj){
	var showHtml = "";
	if(obj.ZWCZF!=0){
		showHtml = obj.ZWCZF_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_ZHTZF(obj){
	var showHtml = "";
	if(obj.ZHTZF!=0){
		showHtml = obj.ZHTZF_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_ZBGJE(obj){
	var showHtml = "";
	if(obj.ZBGJE!=0){
		showHtml = obj.ZBGJE_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}

function jiesuan(){

	if($("#DT1").getSelectedRowIndex()==-1)
	 {
		requireSelectedOneRow();
	    return
	 }
	 cz = 'u';
	$("#resultXML").val($("#DT1").getSelectedRow());
	$(window).manhuaDialog({"title":"部门合同>合同履行信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/ht-view-js.jsp?type=detail","modal":"1"});
}
function doSFDXM(obj){
	if(obj.SFDXMHT=='2'){
		return '否';	
	}else{
		return obj.SFDXMHT_SV;
	}
}
function doHTBM(obj){
	if(obj.HTBM==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.HTBM;
	}
	
}
function doYFDW(obj){
	if(obj.YFDW==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.YFDW;
	}
	
}
function doHTJQDRQ(obj){
	if(obj.HTJQDRQ==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.HTJQDRQ;
	}
	
}
//-------------------------------
//-打开合同信息页面
//-------------------------------
function showZtbInfo(index){
	var obj = $("#DT1").getSelectedRowJsonByIndex(index);
	var json = convertJson.string2json1(obj);
	var id = json.ZTBID;
	var zblx = json.ZBLX;
	$(window).manhuaDialog({"title":"部门合同>查看招标信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/ztbgl_xx.jsp?xx="+id+"&zblx="+zblx+"&cxlx=2","modal":"1"});

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
				部门合同管理
				<span class="pull-right">
				<button id="btnViewSptg" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" title="提供试用时功能    条件：审批中的可以进行通过操作">通过</button>
				&nbsp;&nbsp;
				<app:oPerm url="jsp/business/htgl/ht-index.jsp?type=insert">
					<button id="btnInsert" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">新增</button>
					<button id="btnDelete" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled >删除</button>
<%--					title="条件：只有未审批的合同才可以删除"--%>
					</app:oPerm>
				<app:oPerm url="jsp/business/htgl/ht-index.jsp?type=update">
      				<button id="btnUpdate" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>修改</button>
<%--      				 title="条件：只有未审批的合同才可以进行修改"--%>
      				</app:oPerm>
      			<button id="btnHtChange" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled title="条件：履行中合同才会产生变更">合同变更信息</button>
      			<button id="btnJs" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" onclick="jiesuan()" title="条件：履行中且为非施工类合同的可结算">结算</button>
				<app:oPerm url="jsp/business/htgl/ht-index.jsp?type=doaudi">
      				<button id="sp_btn" class="btn"  type="button" disabled>呈请审批</button>
      				</app:oPerm>
				<app:oPerm url="jsp/business/htgl/ht-index.jsp?type=doaudiview">
      				<button id="btnViewSp" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>审批信息</button>
      				</app:oPerm>
				<app:oPerm url="jsp/business/htgl/ht-update-bm.jsp?type=update">
      				<button id="btnHTQD" class="btn"  type="button" disabled title="条件：只有已审批的合同才需要进行合同 签订">合同签订</button>
      				</app:oPerm>
<%--      				<button id="btnExt" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>合同扩展</button>--%>
				<app:oPerm url="jsp/business/htgl/ht-update-bm.jsp?type=detail">
      				<button id="btnView" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">合同履行信息</button>
      				</app:oPerm>
      			<app:oPerm url="jsp/business/htgl/ht-update-bm.jsp?type=update">
      				<button id="btnWctz" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>完成投资</button>
      				</app:oPerm>
      				<button id="btnExpExcel" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">导出</button>
      				
				</span>
			</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
<%--							<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>--%>
							<input class="span12" type="text" id="QZBBMID" name="ZBBMID"  fieldname="ZBBMID" value="<%=deptId%>" operation="=" >
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
					<input type='hidden' id='ID'/>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">年度</th>
				        <td width="8%" class="right-border bottom-border" width="10%">
				            <select class="span12 year" id="QQDNF" name="QDNF" fieldname="QDNF" operation="="  kind="dic" src="T#GC_HTGL_HT: distinct qdnf as qdnf:qdnf as x:SFYX='1' ORDER BY qdnf ASC">
			            	</select>
				        </td>
				        
						<th width="8%" class="right-border bottom-border text-right">合同状态</th>
						<td width="8%" class="right-border bottom-border">
							<select  id="HTZT"  class="span12 year"  name="HTZT" fieldname="HTZT"  operation="=" kind="dic" src="HTRXZT"  defaultMemo="全部"></select>
						</td>
						<th width="8%" class="right-border bottom-border text-right">合同类型</th>
						<td width="15%" class="right-border bottom-border">
							<select  id="HTLX"  class="span10"  name="HTLX" fieldname="HTLX"  operation="=" kind="dic" src="HTLX"  defaultMemo="全部"></select>
						</td>
						<th width="8%" class="right-border bottom-border text-right">合同名称</th>
						<td width="20%" class="right-border bottom-border" >
							<input id="HTMC"  class="span12" type="text" autocomplete="off" placeholder="" name="HTMC" check-type="maxlength" maxlength="1000" fieldname="HTMC" operation="like" logic="and"/>
						</td>
						<td class="right-border bottom-border text-right" width="20%" rowspan="2" style="background-image: none; background-position: initial initial; background-repeat: initial initial;">
	                        <button id="btnQuery" class="btn btn-link"  type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-search"></i>查询</button>
           					<button id="btnClear" class="btn btn-link" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-trash"></i>清空</button>
			            </td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">项目名称</th>
						<td width="15%" class="right-border bottom-border" colspan="3">
							<input id="Q_XMMC"  class="span12" type="text" autocomplete="off" placeholder="" name="XMMC" check-type="maxlength" maxlength="1000" fieldname="GJS.XMMC" operation="like" logic="and"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">是否虚拟</th>
						<td width="8%" class="right-border bottom-border">
							<select class="span12 year" id="SFSDKLXHT" name="SFSDKLXHT" fieldname="SFSDKLXHT" operation="=" kind="dic" src="SF"  defaultMemo="全部"></select>
						</td>
						<th width="5%" class="right-border bottom-border text-right" >合同编码</th>
						<td width="20%" class="right-border bottom-border ">

							<input id="HTBM"  class="span12" type="text" autocomplete="off" placeholder="" name="HTBM" check-type="maxlength" maxlength="100" fieldname="HTBM" operation="like" logic="and"/>
						</td>
					</tr>
				</table>
			</form>
			<div style="height:5px;"> </div>	
			<div class="overFlowX">
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single"  pageNum="18" printFileName="部门合同管理">
	                <thead>
	                	<tr>
	                		<th rowspan="2"  name="XH" id="_XH" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
	                		<th rowspan="2" fieldname="ID" tdalign="center" colindex=2 CustomFunction="doRandering" noprint="true">&nbsp;&nbsp;</th>
	                		<th rowspan="2" fieldname="HTZT" colindex=3 tdalign="center" CustomFunction="docolor">&nbsp;合同状态&nbsp;</th>
							<th rowspan="2" fieldname="HTBM" colindex=4 tdalign="left" maxlength="20" CustomFunction="doHTBM">&nbsp;合同编码&nbsp;</th>
							<th rowspan="2" fieldname="HTMC" colindex=5 tdalign="left" maxlength="30">&nbsp;合同名称&nbsp;</th>
							<th rowspan="2" fieldname="HTLX" colindex=6 tdalign="center">&nbsp;合同类型&nbsp;</th>
							<th colspan="3">&nbsp;合同价（元）&nbsp;</th>
							<th rowspan="2" fieldname="ZWCZF" colindex=10 tdalign="right" CustomFunction="doRandering_ZWCZF">&nbsp;完成投资&nbsp;</th>
							<th rowspan="2" fieldname="ZHTZF" colindex=11 tdalign="right" CustomFunction="doRandering_ZHTZF">&nbsp;合同支付&nbsp;</th>
							<th rowspan="2" fieldname="SFXNHT" colindex=12 tdalign="center" >&nbsp;是否虚拟&nbsp;</th>
							<th rowspan="2" fieldname="SFDXMHT" colindex=13 tdalign="center" CustomFunction="doSFDXM">&nbsp;单项目&nbsp;</th>
							<th rowspan="2" fieldname="SFZFJTZ" colindex=14 tdalign="center" >&nbsp;支付即投资&nbsp;</th>
							<th rowspan="2" fieldname="YFDW" colindex=15 tdalign="left" maxlength="36" CustomFunction="doYFDW">&nbsp;乙方单位&nbsp;</th>
							<th rowspan="2" fieldname="HTJQDRQ" colindex=16 tdalign="center" CustomFunction="doHTJQDRQ">&nbsp;合同签订日期&nbsp;</th>
							<th rowspan="2" fieldname="ZTBMC" colindex=17 tdalign="left" hasLink="true" maxlength="30" linkFunction="showZtbInfo">&nbsp;招投标名称&nbsp;</th>
	                	</tr>
	                	<tr>
	                		<th fieldname="ZHTQDJ" colindex=7 tdalign="right">&nbsp;合同签订价&nbsp;</th>
	                		<th fieldname="ZBGJE" colindex=8 tdalign="right" CustomFunction="doRandering_ZBGJE">&nbsp;变更金额&nbsp;</th>
	                		<th fieldname="ZZXHTJDO" colindex=9 tdalign="right">&nbsp;最新合同价&nbsp;</th>
	                	</tr>
	                </thead>
	              	<tbody></tbody>
	           </table>
	       </div>
	 	</div>
	</div>     
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" fieldname="ghh.LRSJ" id="txtFilter"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
</div>
</body>
</html>