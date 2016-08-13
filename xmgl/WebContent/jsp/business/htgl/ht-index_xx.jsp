
<%@page import="java.sql.Connection"%>
<%@page import="com.ccthanking.framework.common.DBUtil"%>
<%@page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			OrgDept dept = user.getOrgDept();
			String deptId = dept.getDeptID();
			String showTj = "";
			if(user.getRoleListIdString()!=null&&user.getRoleListIdString().indexOf("60e21aca-9f97-4a43-879d-d8604bcae167")!=-1){
				showTj = "1";
			}
			
			String htid = request.getParameter("htid");
		%>
		<app:base />
		<title>合同首页</title>
<style type="text/css">
.round_normal{width:16px;height:16px;display: inline-block;font-size:20px;line-heigth:16px;text-align:center;color:green;text-decoration:none}
.round_error{width:16px;height:16px;display: inline-block;font-size:20px;line-heigth:16px;text-align:center;color:red;text-decoration:none}
.round_warning{width:16px;height:16px;display: inline-block;font-size:20px;line-heigth:16px;text-align:center;color:yellow;text-decoration:none}
</style>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtController.do";
var zongjian = "<%=showTj%>";
//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(4)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	setPageHeight();
	init();
	
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		
		if($("#QZBBM").val()!=""){
			var v = $("#QZBBM").find("option:selected").text();  
			$("#ZBBM").val(v);
		}
		if($("#rq1").val()!=""||$("#rq2").val()!=""){
			$("#wcrid").val('<%=user.getAccount() %>');
		}
		var url = controllername+"?queryIndex&wcrid="+$("#wcrid").val()+"&kssj="+$("#rq1").val()+"&jssj="+$("#rq2").val();
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(url,data,DT1,null, true);
	});
	//按钮绑定事件(新增)
<%--	$("#btnInsert").click(function() {--%>
<%--		$(window).manhuaDialog({"title":"合同>新增","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/ht-add.jsp?type=insert","modal":"1"});--%>
<%--	});--%>
	$("#btnInsert").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"合同>修改","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/ht-add-bm.jsp?type=update","modal":"1"});
	});
	//按钮绑定事件(修改)
	$("#btnUpdate").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#DT1").getSelectedRow());
		var htlx = JSON.parse($("#DT1").getSelectedRow()).HTLX;
		$(window).manhuaDialog({"title":"合同>支付信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtzf-indexOne.jsp?type=update&htlx="+htlx,"modal":"1"});
	});
	//完成投资
	$("#btnWctz").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#DT1").getSelectedRow());
		var htid = JSON.parse($("#DT1").getSelectedRow()).ID;
		$(window).manhuaDialog({"title":"合同>合同完成投资","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtwctz-indexOne.jsp?hasHt=one&fromPage=htindex&htid="+htid,"modal":"1"});
	});
	
	$("#btnQdsp").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"合同>签订审批","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/ht-update-qdlx.jsp?type=update","modal":"1"});
	});
	
	
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		// var t = $("#DT1").getTableRows();
		// if(t<=0)
		// {
		//	 xAlert("提示信息","请至少查询出一条记录！");
		//	 return;
		// }
	  	 $(window).manhuaDialog({"title":"导出","type":"text","content":"${pageContext.request.contextPath }/jsp/framework/print/TabListEXP.jsp?tabId=DT1","modal":"3"});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
        cwHTZT();
		$("#rq1")[0].value="";
		$("#rq2")[0].value="";
		$("#wcrid").val("");
        $("input:radio[name=spqk][value='1']")[0].checked=true;
    });
	
    
    $("#btnView").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		rowView($("#DT1").getSelectedRowIndex());
	});
	
});
function cwHTZT(){

	//$("#QCWHTZT").val("0");
	$("#QCWHTZT").val("-1");//给财务已审批以上的查看权限 modified by hongpeng.dong at 2014.10.23
}
//页面默认参数
function init(){
	getNd();
	if(zongjian!="1"){
		$("#cwzj_tj").hide();
	}else{
		$("#cwzj_tj").show();
	}
	$("input:radio[name=spqk][value='1']").attr("checked",true);
	var url = controllername+"?queryIndex&htid=<%=htid%>";
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(url,data,DT1);
}
//默认年度
function getNd(){
	//年度信息，里修改
	setDefaultNd("QQDNF");//modify by zhangbr@ccthanking.com
	/**
	var y = new Date().getFullYear();
	$("#QQDNF option").each(function(){
		if(this.value == y){
		 	$(this).attr("selected", true);
		 	return true;
		}
	});
	*/
}

//点击获取行对象
function tr_click(obj,tabListid){
	$("#htid").val(obj.ID);
	//alert(JSON.stringify(obj));
	$("#btnHtjs").attr("disabled",false);
	$("#btnHtzz").attr("disabled",false);
	$("#btnViewSp").attr("disabled",false);
	$("#btnUpdate").attr("disabled",true);
	$("#btnWctz").attr("disabled",true);
	if(obj.HTZT=="0"){
		$("#btnQdsp").removeAttr("disabled");
		//$("#btnUpdate").attr("disabled",true);
	}else if(obj.HTZT=="1"){
		//$("#btnUpdate").removeAttr("disabled");
		$("#btnQdsp").attr("disabled",true);
		$("#btnUpdate").attr("disabled",false);
		$("#btnWctz").attr("disabled",false);
	}else if(obj.HTZT=="2"||obj.HTZT=="3"||obj.HTZT=="4"){
		$("#btnQdsp").attr("disabled",true);
		$("#btnHtjs").attr("disabled",true);
		$("#btnHtzz").attr("disabled",true);
	}else{
		$("#btnQdsp").attr("disabled",true);
		//$("#btnUpdate").attr("disabled",true);
	}
	if(obj.HTZT=="-3"){
		$("#btnViewSp").attr("disabled",true);
	}
	if(obj.HTZT=="2"){
		$("#btnUpdate").attr("disabled",false);
		$("#btnWctz").attr("disabled",false);
	}
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
	$(window).manhuaDialog({"title":"合同>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/ht-view.jsp?type=detail","modal":"1"});
}

function closeParentCloseFunction(){
	var index =	$("#DT1").getSelectedRowIndex();
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	var tempJson = convertJson.string2json1(data);
	var a=$("#DT1").getCurrentpagenum();
	tempJson.pages.currentpagenum=a;
	data = JSON.stringify(tempJson);
	//调用ajax插入
	var url = controllername+"?queryIndex&htid=<%=htid%>";
	defaultJson.doQueryJsonList(url,data,DT1);
	$("#DT1").cancleSelected();
	$("#DT1").setSelect(index);
}


function gengxinchaxun(type){
	return true;
}

//状态颜色判断
function docolor(obj)
{
	var xqzt=obj.HTZT;
	if(xqzt=="-3"){
		return '<span class="label label-warning">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="-2"){
	 	return '<span class="label label-warning">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="-1"){
	 	return '<span class="label label-warning">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="0"){
	 	return '<span class="label label-danger">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="1"){
	  	return '<span class="label label-success">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="2"){
	  	return '<span class="label label-info">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="3"){
	  	return '<span class="label label-info">'+obj.HTZT_SV+'</span>';
	}else if(xqzt=="4"){
	  	return '<span class="label label-info">'+obj.HTZT_SV+'</span>';
	}else{
		return obj.HTZT_SV;
	}
	
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
function doZT(obj){
	var showHtml = '<div class="round_normal" title="正常">●</div>';
	var title = "";
	var flagType = 0;
	if(parseFloat(obj.ZHTZF) > parseFloat(obj.ZZXHTJDO)){
		//合同支付和最新合同价比较(红色警告)
		title += '支付超过最新合同价;';
		flagType = 1;
	}
	if(parseFloat(obj.ZBGJE) > parseFloat(obj.ZHTQDJ)*0.15){
		//合同变更和合同签订价的15%比较(红色警告)
		title += '变更超过合同签订价的15%;';
		flagType = 1;
	}
	if(obj.HTJSRQ!=null&&obj.HTJSRQ!=''){
		//合同结束日期与当前日期比较
		var myDate = new Date();
		var jsDate = new Date(obj.HTJSRQ); 
		if(myDate>jsDate&&obj.HTZT=='1'){
			title += '已超过合同结束日期仍在履行中;';
			flagType = flagType==1?1:2;
		}
	}
		
	//完成投资和最新合同价比较
	if(parseFloat(obj.ZWCZF) > parseFloat(obj.ZZXHTJDO)){
		title += '完成投资超过最新合同价;';
		flagType = flagType==1?1:2;
	}

	if(flagType==1){
		showHtml = '<div class="round_error" title="'+title+'">●</div>';
	}else if(flagType==2){
		showHtml = '<div class="round_warning" title="'+title+'">●</div>';
	} 
	
	return showHtml;
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
function doZBBM(obj){
	if(obj.ZBBM==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.ZBBM;
	}
	
}
function doHTSJKSRQ(obj){
	if(obj.HTSJKSRQ==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.HTSJKSRQ;
	}
	
}
function doHTJSRQ(obj){
	if(obj.HTJSRQ==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.HTJSRQ;
	}
	
}
function doHTJQDRQ(obj){
	if(obj.HTJQDRQ==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.HTJQDRQ;
	}
	
}


//合同结束
$(function() {
	$("#btnHtjs").click(function() {
		//获取ywlx,sjbh
		if($("#DT1").getSelectedRowIndex()==-1) {
			requireFormMsg("请选择一条要操作的数据！");
		} else {
			var rowjson = $("#DT1").getSelectedRow();
			xConfirm("信息确认","是否确认结束所选的合同？ ");
			$('#ConfirmYesButton').attr('click','');
			$('#ConfirmYesButton').one("click",function(){
				
				var rowid = JSON.parse(rowjson).ID;
				$.ajax({
					url : controllername+"?update",
					data : "opttype=htjs&id="+rowid,
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
				
			});
		}
	});
});
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

		var s = "${pageContext.request.contextPath}/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview=1";   
		$(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});
	});
});
//合同中止
$(function() {
	$("#btnHtzz").click(function() {
		//获取ywlx,sjbh
		if($("#DT1").getSelectedRowIndex()==-1) {
			requireFormMsg("请选择一条要操作的数据！");
		} else {
			$("#resultXML").val($("#DT1").getSelectedRow());
			$(window).manhuaDialog({"title":"部门合同>合同履行信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/ht-view-htzz.jsp?type=detail","modal":"1"});
		}
	});
});
function bgShqk(){
	var v = $("input:radio[name='spqk']:checked").val();
	if(v=="0"){
		$("#wcrid").val('<%=user.getAccount() %>');
	}else{
		$("#wcrid").val("");
	}
}

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
</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<p></p>
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						合同综合管理
						<span class="pull-right">
							<app:oPerm
								url="jsp/business/htgl/ht-update-qdlx.jsp?type=update">
								<button id="btnQdsp" class="btn" type="button"
									style="font-family: SimYou, Microsoft YaHei; font-weight: bold;"
									disabled >
<%--									title="条件：只有审核中的合同才可以进行此操作"--%>
									签订审核
								</button>
							</app:oPerm>
							<app:oPerm url="jsp/business/htgl/ht-index.jsp?type=doaudiview">
			      				<button id="btnViewSp" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>审批信息</button>
			      				</app:oPerm>
							 <app:oPerm url="jsp/business/htgl/ht-view.jsp?type=detail">
								<button id="btnView" class="btn" type="button"
									style="font-family: SimYou, Microsoft YaHei; font-weight: bold;">
									合同综合信息
								</button>
							</app:oPerm>
						</span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<INPUT type="text" class="span12" kind="text" id="num"
										fieldname="rownum" value="1000" operation="<=" />
									<INPUT type="text" class="span12" kind="text" id="ZBBM"
										fieldname="ZBBM" value="" operation="=" />
										<%
											if("100000000002".equals(deptId)){
												//当前人员为财务部，只显示签订之后的合同
												%>
													<input class="span12" type="text" id="QCWHTZT" name="CWHTZT"
										fieldname="to_number(HTZT)" operation=">=" value="-1">
												<%
											}
										%>
									
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
							<tr style="display: none;">
								<th width="3%" class="right-border bottom-border text-right">
									年度
								</th>
								<td class="right-border bottom-border" style="width: 8%">
									<select class="span12 year" id="QQDNF" name="QDNF"
										fieldname="QDNF" operation="=" kind="dic" src="T#GC_HTGL_HT: distinct qdnf as qdnf:qdnf as x:SFYX='1' ORDER BY qdnf ASC">
									</select>
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									项目名称
								</th>
								<td class="right-border bottom-border" style="width: 15%">
									<input class="span12" type="text" id="QXMMC" name="XMMC"
										fieldname="XMMC" operation="like">
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									合同编码
								</th>
								<td class="right-border bottom-border" style="width: 8%">
									<input id="HTBM" class="span12" type="text" autocomplete="off"
										placeholder="" name="HTBM" check-type="maxlength"
										maxlength="100" fieldname="HTBM" operation="like" logic="and" />
								</td>
								<th width="5%" class="right-border bottom-border">
									合同名称
								</th>
								<td class="right-border bottom-border" style="width: 15%">
									<input id="HTMC" class="span12" type="text" autocomplete="off"
										placeholder="" name="HTMC" check-type="maxlength"
										maxlength="1000" fieldname="HTMC" operation="like" logic="and" />
								</td>
								<%--						<td class="right-border bottom-border" rowspan=2 >--%>
								<td class="text-left bottom-border text-right" rowspan=3>
									<button id="btnQuery" class="btn btn-link" type="button"
										style="font-family: SimYou, Microsoft YaHei; font-weight: bold;">
										<i class="icon-search"></i>查询
									</button>
									<button id="btnClear" class="btn btn-link" type="button"
										style="font-family: SimYou, Microsoft YaHei; font-weight: bold;">
										<i class="icon-trash"></i>清空
									</button>
								</td>
							</tr>
							<tr style="display: none;">
								<th width="5%" class="right-border bottom-border text-right">
									合同状态
								</th>
								<td class="right-border bottom-border">
									<select id="HTZT" class="span12 3characters" name="HTZT"
										fieldname="HTZT" operation="=" kind="dic" src="HTRXZT"
										defaultMemo="全部"></select>
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									合同类型
								</th>
								<td width="17%" class="right-border bottom-border">
									<select id="HTLX" class="span12" name="HTLX" fieldname="HTLX"
										operation="=" kind="dic" src="HTLX" defaultMemo="全部"></select>
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									主办部门
								</th>
								<td width="17%" class="right-border bottom-border">
									<select type="text" id="QZBBM" kind="dic"
										src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME"
										defaultMemo="全部"></select>
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									是否虚拟
								</th>
								<td width="17%" class="right-border bottom-border">
									<select class="span12 2characters" id="SFSDKLXHT"
										name="SFXNHT" fieldname="SFXNHT" operation="="
										kind="dic" src="SF" defaultMemo="全部"></select>
								</td>

							</tr>
							<tr id="cwzj_tj">
								<th width="5%" class="right-border bottom-border text-right">
									审批情况
								</th>
								<td class="right-border bottom-border" colspan="3">
									<label class="radio inline"><input id="spqk" name="spqk" type="radio" onchange="bgShqk()" value="0"/>我审批过的</label>
									<label class="radio inline"><input id="spqk" name="spqk" type="radio" value="1" onchange="bgShqk()"/>全部合同</label>
									<input id="wcrid" class="span12" type="hidden" autocomplete="off"  operation="=" logic="and" value=""/>
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									日期
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="rq1" class="date" type="date" name="HTJQDRQ" style="width:93%;" operation=">=" fieldtype="date" fieldformat="YYYY-MM-DD">
								</td>
								<th width="5%" class="right-border bottom-border">
									至
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="rq2" class="date" type="date" name="HTJQDRQ" style="width:92%;" operation="<=" fieldtype="date" fieldformat="YYYY-MM-DD">
								</td>

							</tr>
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
						<table width="100%" class="table-hover table-activeTd B-table"
							id="DT1" type="single" pageNum="18" printFileName="合同综合管理">
							<thead>
								<tr>
									<th rowspan="2" name="XH" id="_XH" style="width: 10px"
										colindex=1 tdalign="center">
										&nbsp;#&nbsp;
									</th>
									<%--							<th fieldname="ZTBID" colindex=2 tdalign="center" maxlength="36">&nbsp;招投标编号&nbsp;</th>--%>
									<th rowspan="2" fieldname="HTZT" colindex=2 tdalign="center"
										CustomFunction="docolor">
										&nbsp;合同状态&nbsp;
									</th>
									<th rowspan="2" fieldname="HTZT" colindex=3 tdalign="center"
										CustomFunction="doZT">
										&nbsp;状态&nbsp;
									</th>
									<th rowspan="2" fieldname="HTBM" colindex=4 tdalign="left"
										maxlength="30" CustomFunction="doHTBM">
										&nbsp;合同编码&nbsp;
									</th>
									<th rowspan="2" fieldname="HTMC" colindex=5 tdalign="left"
										maxlength="30">
										&nbsp;合同名称&nbsp;
									</th>
									<th rowspan="2" fieldname="HTLX" colindex=6 tdalign="center">
										&nbsp;合同类型&nbsp;
									</th>
									<th colspan="3">
										&nbsp;合同价（元）&nbsp;
									</th>
									<th rowspan="2" fieldname="ZWCZF" colindex=10 tdalign="right"
										CustomFunction="doRandering_ZWCZF">
										&nbsp;完成投资&nbsp;
									</th>
									<th rowspan="2" fieldname="ZHTZF" colindex=11 tdalign="right"
										CustomFunction="doRandering_ZHTZF">
										&nbsp;合同支付&nbsp;
									</th>
									<th rowspan="2" fieldname="SFXNHT" colindex=12 tdalign="center">
										&nbsp;是否虚拟&nbsp;
									</th>
									<th rowspan="2" fieldname="SFDXMHT" colindex=13 tdalign="center" CustomFunction="doSFDXM">
										&nbsp;单项目&nbsp;
									</th>
									<th rowspan="2" fieldname="SFZFJTZ" colindex=14 tdalign="center">
										&nbsp;支付即投资&nbsp;
									</th>
									<th rowspan="2" fieldname="YFDW" colindex=15 tdalign="left"
										maxlength="36" CustomFunction="doYFDW">
										&nbsp;乙方单位&nbsp;
									</th>
									<th rowspan="2" fieldname="ZBBM" colindex=16 tdalign="center"
										maxlength="36" CustomFunction="doZBBM">
										&nbsp;主办部门&nbsp;
									</th>
									<th rowspan="2" fieldname="HTSJKSRQ" colindex=17
										tdalign="center" CustomFunction="doHTSJKSRQ">
										&nbsp;合同开始日期&nbsp;
									</th>
									<th rowspan="2" fieldname="HTJSRQ" colindex=18 tdalign="center" CustomFunction="doHTJSRQ">
										&nbsp;合同结束日期&nbsp;
									</th>
									<th rowspan="2" fieldname="HTJQDRQ" colindex=19
										tdalign="center" CustomFunction="doHTJQDRQ">
										&nbsp;合同签订日期&nbsp;
									</th>
								</tr>
								<tr>
									<th fieldname="ZHTQDJ" colindex=7 tdalign="right">
										&nbsp;合同签订价&nbsp;
									</th>
									<th fieldname="ZBGJE" colindex=8 tdalign="right"
										CustomFunction="doRandering_ZBGJE">
										&nbsp;变更金额&nbsp;
									</th>
									<th fieldname="ZZXHTJDO" colindex=9 tdalign="right">
										&nbsp;最新合同价&nbsp;
									</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" />
				<input type="hidden" name="txtXML" />
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="to_number(htzt)" id="txtFilter" />
				<input type="hidden" name="resultXML" id="resultXML" />
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData" />
				<input type="hidden" name="queryResult" id="queryResult" />
			</FORM>
		</div>
		<input type="hidden" id="htid" />
	</body>
</html>