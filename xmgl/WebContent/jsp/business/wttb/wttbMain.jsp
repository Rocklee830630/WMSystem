<!DOCTYPE HTML>
<html lang="en">
  <head>
	<%@ page language="java" pageEncoding="UTF-8"%>
	<%@ taglib uri= "/tld/base.tld" prefix="app" %>
	<%@ page import="com.ccthanking.framework.common.User"%>
    <%@ page import="com.ccthanking.framework.Globals"%>
	<%@ page import="com.ccthanking.framework.common.Role"%>
	<%	User user = (User)  request.getSession().getAttribute(Globals.USER_KEY); 
		String role = user.getRoleListString();
		String leader = "0";
		if(!"".equals(role)&&(role.indexOf("部长")!=-1 || role.indexOf("管理员")!=-1 || "superman".equals(user.getAccount()))){
			leader="1";//等于1表示是领导
		}
	%>
	<app:base />
	<app:dialogs/>
    <title>问题提报主页面</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="问题">
	<meta http-equiv="description" content="问题提报">
	<style type="text/css">
		.myUl li p{
			font-size: 15px;
			word-wrap:break-word;
		}
		blockquote{
			margin-bottom:0px;
		}
	</style>
	<script src="${pageContext.request.contextPath }/js/common/FixTable.js"></script> 
	<script type="text/javascript" charset="utf-8">
		var controllername= "${pageContext.request.contextPath }/wttb/wttbController.do";
		var nowTabPageNUm = 2;//用于记录当前显示第几个tab页，默认是第一页
		var btnNum = 0;
		g_bAlertWhenNoResult = false;
		var p_leader = '<%=leader%>';
		var historyListHeight = 0;//这个变量用于计算历史列表最大允许的高度
		var getHeight=getDivStyleHeight()-30;
		
		//计算本页表格分页数
		function setPageHeight() {
			var height = getHeight-pageTopHeight-pageTitle-getTableTh(2)-pageNumHeight;
			var pageNum = parseInt(Math.abs(height)/pageTableOne,10);
			$("#DT21").attr("pageNum", pageNum);
			$("#DT0").attr("pageNum", pageNum);
		}
		//计算本页表格分页数--我处理
		function setPageHeightYcl() {
			var height = getHeight-pageTopHeight-pageTitle-getTableTh(1)-pageNumHeight;
			var pageNum = parseInt(Math.abs(height)/pageTableOne,10);
			$("#DT1").attr("pageNum", pageNum);
		}
		//计算本页表格分页数--待处理
		function setPageHeightWtzzDcl(id) {
			var height = getHeight-pageTopHeight-pageTitle-pageTitle-getTableTh(1)-$("#suggestDcl").height()-pageNumHeight;
			var pageNum = parseInt(Math.abs(height)/pageTableOne,10);
			$("#"+id).attr("pageNum", pageNum);
		}
		//计算本页表格分页数--我提交
		function setPageHeightWtzzWtj(id) {
			var height = getHeight-pageTopHeight-pageTitle-pageTitle-getTableTh(1)-$("#suggestWtj").height()-pageNumHeight;
			var pageNum = parseInt(Math.abs(height)/pageTableOne,10);
			$("#"+id).attr("pageNum", pageNum);
		}
		//父页面调用查询页面的方法
		$(function() {
			setPageHeight();
			setPageHeightYcl();
			doInit();
			//查询按钮事件
			$("#btnQuery").click(function() {
				//生成json串
				doSearch();
			});
			$("#btnQuery2").click(function() {
				//生成json串
				doSearch2();
			});
			//新增按钮事件
			$("#btnAdd").click(function() {
				btnNum = 1;
				$(window).manhuaDialog({"title":"问题提报>新增问题","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/xzWt.jsp","modal":"1"});
			});
			$("#xmBtn").click(function(){
				btnNum = 3;
				$(window).manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/kfglgl/xmcx.jsp","modal":"2"}); 
			});
			//存草稿按钮事件
			$("#btnSave").click(function(){
				if($("#insertForm").validationButton()){
	 				//生成json串
	 		 		var data = Form2Json.formToJSON(insertForm);
	 				//组成保存json串格式
	 				var data1 = defaultJson.packSaveJson(data);
	 				//调用ajax插入
	 				var flag;
	 				if($("#wtid").val()!="" && $("#wtid").val()!=undefined){
	 					flag = defaultJson.doUpdateJson(controllername + "?insertInfo", data1,DT1);
	 				}else{
	 					flag = defaultJson.doInsertJson(controllername + "?insertInfo", data1,DT1);
	 				}
	 				if(flag){
						$("#tabPage0").tab("show");
		 				clearAll(); 
	 				}
				}
			});
			//送达按钮事件
			$("#btnSend").click(function(){
				if($("#jsrName").attr("code")==undefined){
					xAlertMsg("请至少选择一位接收人!"); 
					return;
				}else{
					if($("#insertForm").validationButton()){
		 				//生成json串
		 		 		var data = Form2Json.formToJSON(insertForm);
		 				//组成保存json串格式
		 				var data1 = defaultJson.packSaveJson(data);
		 				//调用ajax插入
		 				var flag;
		 				if($("#wtid").val()!="" && $("#wtid").val()!=undefined){
		 					flag = defaultJson.doUpdateJson(controllername + "?sendReport", data1,DT1);
		 				}else{
		 					flag = defaultJson.doInsertJson(controllername + "?sendReport", data1,DT1);
		 				}
		 				if(flag){
							$("#tabPage0").tab("show");
			 				clearAll(); 
		 				}
					}
				}
			});
			//已解决按钮事件
			$("#btnSolve").click(function(){
				var tabId = "DT21";
				if(nowTabPageNUm==2){
					tabId = "DT21";
				}else if(nowTabPageNUm==0){
					tabId = "DT0";
				}else if(nowTabPageNUm==1){
					tabId = "DT1";
				}
				if($("#"+tabId).getSelectedRowIndex()==-1){
					requireSelectedOneRow();
					return;
				}else{
					btnNum = 4;
					var tempJson = convertJson.string2json1($("#"+tabId).getSelectedRow());
					var id = tempJson.WTTB_INFO_ID;
					$(window).manhuaDialog({"title":"问题提报>解决时间","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/jjWt.jsp?infoID="+id,"modal":"3"});
				}
			});
			//未解决按钮事件
			$("#btnUnSolve").click(function(){
				var tabId = "DT21";
				if(nowTabPageNUm==2){
					tabId = "DT21";
				}else if(nowTabPageNUm==0){
					tabId = "DT0";
				}else if(nowTabPageNUm==1){
					tabId = "DT1";
				}
				if($("#"+tabId).getSelectedRowIndex()==-1){
					requireSelectedOneRow();
					return;
				}else{
					btnNum = 5;
					var tempJson = convertJson.string2json1($("#"+tabId).getSelectedRow());
					var id = tempJson.WTTB_INFO_ID;
					$(window).manhuaDialog({"title":"问题提报>未解决","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/jjWt.jsp?type=wjj&infoID="+id,"modal":"3"});
				}
			});
			//催办按钮事件
			$("#btnUrge").click(function(){
				var tabId = "DT21";
				if(nowTabPageNUm==2){
					tabId = "DT21";
				}else if(nowTabPageNUm==0){
					tabId = "DT0";
				}else if(nowTabPageNUm==1){
					tabId = "DT1";
				}
				if($("#"+tabId).getSelectedRowIndex()==-1){
					requireSelectedOneRow();
					return;
				}else{
					if(nowTabPageNUm==2){
						//待处理
						var index =	$("#DT21").getSelectedRowIndex();
						var rowValue = $("#DT21").getSelectedRow();
						var tempJson = convertJson.string2json1(rowValue);
						tempJson["CBCS"]=Number(tempJson["CBCS"])+1;
						xConfirm("提示信息","催办将使问题的催办次数加1，是否继续？");
						$('#ConfirmYesButton').unbind();
						$('#ConfirmYesButton').one("click",function(){
							var data = JSON.stringify(tempJson);
				 			var data1 = defaultJson.packSaveJson(data);
			 				defaultJson.doUpdateJson(controllername + "?doUrge&blrjs=fqr", data1,DT21);
							doSearch();
							clearAll2();
		 				});
	 				}else if(nowTabPageNUm==0){
	 					//我提交的
	 					var index =	$("#DT0").getSelectedRowIndex();
						var rowValue = $("#DT0").getSelectedRow();
						var tempJson = convertJson.string2json1(rowValue);
						tempJson["CBCS"]=Number(tempJson["CBCS"])+1;
						xConfirm("提示信息","催办将使问题的催办次数加1，是否继续？");
						$('#ConfirmYesButton').unbind();
						$('#ConfirmYesButton').one("click",function(){
							var data = JSON.stringify(tempJson);
				 			var data1 = defaultJson.packSaveJson(data);
			 				defaultJson.doUpdateJson(controllername + "?doUrge&blrjs=fqr", data1,DT0);
							g_bAlertWhenNoResult = false;
							doSearch2();
							g_bAlertWhenNoResult = true;
							//clearAll();
		 				});
	 				}
					ctrlBtn(tempJson);
				}
			});
			//退回按钮事件
			$("#btnSendBack").click(function(){
				if($("#DT1").getSelectedRowIndex()==-1){
					requireSelectedOneRow();
					return;
				}else if($("#DT1 textarea").val()==""){
					xAlertMsg("退回业务必须说明意见"); 
					return;
				}else{
					if($("#insertForm").validationButton()){
		 		 		var data = Form2Json.formToJSON(insertForm);
		 				var data1 = defaultJson.packSaveJson(data);
		 				var flag = defaultJson.doUpdateJson(controllername + "?doSendBack", data1,DT1);
		 				if(flag){
							$("#tabPage0").tab("show");
			 				clearAll();
		 				}
					}
				}
			});
			//撤销按钮事件
			$("#btnRevoke").click(function(){
				if($("#DT1").getSelectedRowIndex()==-1){
					xAlertMsg("请选择一条记录"); 
					return;
				}else{
					if($("#insertForm").validationButton()){
		 				//生成json串
		 		 		var data = Form2Json.formToJSON(insertForm);
		 				//组成保存json串格式
		 				var data1 = defaultJson.packSaveJson(data);
		 				//调用ajax插入
		 				var flag = defaultJson.doUpdateJson(controllername + "?doRevoke", data1,DT1);
						$("#insertForm").clearFormResult(); 
					}
				}
			});
			//补充意见按钮事件
			$("#btnSugg").click(function(){
				if($("#DT0").getSelectedRowIndex()==-1){
					requireSelectedOneRow();
					return;
				}else{
					//$("#showMyModal").modal("show");
					btnNum = 2;
					$("#resultXML").val($("#DT0").getSelectedRow());
					$(window).manhuaDialog({"title":"问题提报>批复问题","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/pfWt.jsp","modal":"3"});
				}
			});
			//补充意见按钮
			$("#btnAddOpinion").click(function(){
				var tabId = "DT21";
				if(nowTabPageNUm==2){
					tabId = "DT21";
				}else if(nowTabPageNUm==0){
					tabId = "DT0";
				}else if(nowTabPageNUm==1){
					tabId = "DT1";
				}
				if($("#"+tabId).getSelectedRowIndex()==-1){
					requireSelectedOneRow();
					return;
				}else{
					var id = convertJson.string2json1($("#"+tabId).getSelectedRow()).WTTB_INFO_ID;
					$("#resultXML").val($("#"+tabId).getSelectedRow());
					$(window).manhuaDialog({"title":"问题提报>补充意见","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/pfWt.jsp?type=main&infoID="+id,"modal":"4"});
				}
			});
			$('#tabPage0').on('show', function (e) {
				nowTabPageNUm = 0;
				$("#DT21").cancleSelected();
				$("#DT1").cancleSelected();
			});
			$('#tabPage1').on('show', function (e) {
				nowTabPageNUm = 1;
				$("#DT0").cancleSelected();
				$("#DT21").cancleSelected();
			});
			$('#tabPage2').on('show', function (e) {
				nowTabPageNUm = 2;
				$("#DT0").cancleSelected();
				$("#DT1").cancleSelected();
			});
			//按钮绑定事件（清空查询条件）
			$("#btnClear2").click(function() {
				$("#queryForm2").clearFormResult();
       			clearCkeckBox("queryForm2");
			});
			//按钮绑定事件（清空查询条件）
			$("#btnClear0").click(function() {
				$("#queryForm0").clearFormResult();
       			clearCkeckBox("queryForm0");
			});
			$("#btnQuery1").click(function() {
				doSearchYcl();
			});
			//按钮绑定事件（清空查询条件）
			$("#btnClear1").click(function() {
				$("#queryForm1").clearFormResult();
       			clearCkeckBox("queryForm1");
			});
			//按钮绑定事件（导出）
			$("#btnExp1").click(function() {
				if(exportRequireQuery($("#DT23"))){//该方法需传入表格的jquery对象
					printTabList("DT23","wttb_wtzz.xls","BLRJS,JSR,PFSJ,PFNR","2,1");
				}
			});
			//按钮绑定事件（导出）
			$("#btnExp2").click(function() {
				if(exportRequireQuery($("#DT3"))){//该方法需传入表格的jquery对象
					printTabList("DT3","wttb_wtzz.xls","BLRJS,JSR,PFSJ,PFNR","2,1");
				}
			});
			//按钮绑定事件（再次提请）
			$("#btnResend").click(function(){
				var tabId = "DT21";
				if(nowTabPageNUm==2){
					tabId = "DT21";
				}else if(nowTabPageNUm==0){
					tabId = "DT0";
				}else if(nowTabPageNUm==1){
					tabId = "DT1";
				}
				if($("#"+tabId).getSelectedRowIndex()==-1){
					requireSelectedOneRow();
					return;
				}else{
					btnNum = 6;
					var id = convertJson.string2json1($("#"+tabId).getSelectedRow()).WTTB_INFO_ID;
					var sjzt = convertJson.string2json1($("#"+tabId).getSelectedRow()).SJZT;
					btnNum = 5;
					var n = id;
					if(sjzt=="5"){
						$(window).manhuaDialog({"title":"问题提报>处理问题","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/infoCard.jsp?infoID="+n+"","modal":"1"});
					}else{
						$(window).manhuaDialog({"title":"问题提报>处理问题","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/infoCard.jsp?infoID="+n+"&resendFlag=1","modal":"1"});
					}
				}
			});
			//删除按钮事件
			$("#btnDelete").click(function(){
				var tabId = "DT21";
				if(nowTabPageNUm==2){
					tabId = "DT21";
				}else if(nowTabPageNUm==0){
					tabId = "DT0";
				}else if(nowTabPageNUm==1){
					tabId = "DT1";
				}
				if($("#"+tabId).getSelectedRowIndex()==-1){
					requireSelectedOneRow();
					return;
				}else{
					xConfirm("提示信息","请确认是否删除？");
					$('#ConfirmYesButton').unbind();
					$('#ConfirmYesButton').one("click",function(){
						var data = JSON.stringify($("#"+tabId).getSelectedRowJsonObj());
			 			var data1 = defaultJson.packSaveJson(data);
		 				defaultJson.doDeleteJson(controllername + "?deleteWttb", data1,document.getElementById(tabId));
						doInit();
					});
				}
			});
			
		});
		//回复按钮--回调函数
		function doReply(data){
			var tabId = "DT21";
			if(nowTabPageNUm==2){
				tabId = "DT21";
			}else if(nowTabPageNUm==0){
				tabId = "DT0";
			}else if(nowTabPageNUm==1){
				tabId = "DT1";
			}
			var data1 = defaultJson.packSaveJson(data);
			$.ajax({
				url:controllername + "?doSugg&nrlx=2&ywid="+$("#ywid").val(),
				data:data1,
				dataType:"json",
				async:false,
				success:function(result){
					xAlertMsg("操作成功！");
					var id = convertJson.string2json1($("#"+tabId).getSelectedRow()).WTTB_INFO_ID;
					if(nowTabPageNUm==2){
						getEventList2(id,DT23);
					}else if(nowTabPageNUm==0){
						getEventList(id,DT3);
					}else if(nowTabPageNUm==1){
					}
				}
			});
		}
		//清空查询表格中的复选框
		function clearCkeckBox(formID){
			$("#"+formID+" input[type='checkbox']").each(function () { 
				if($(this).attr("keep")!="true"){ 
					$(this).attr("checked", false);
				}
			}); 
		}
		//---------------------------------
		//-初始化事件
		//---------------------------------
		function doInit(){
			doSearch();
			doSearch2();
			doSearchYcl();
		}
		//已处理问题
		function doSearchYcl(){
			var data = combineQuery.getQueryCombineData(queryForm1,frmPost,DT1);
			defaultJson.doQueryJsonList(controllername+"?queryInfoYcl"+getCheckboxCond("queryForm1"),data,DT1);
			getWtCounts();
		}
		function doSearch(){
			var cond = "";
			$("#queryForm0 input[type='checkbox']").each(function(){
				if($(this).is(':checked')){
					cond += $(this).attr('value')+",";
				}
			});
			cond = cond==""?"":"&hfqk="+cond.substring(0,cond.length-1);
			var data = combineQuery.getQueryCombineData(queryForm0,frmPost,DT0);
			defaultJson.doQueryJsonList(controllername+"?queryInfoJsr"+cond,data,DT0);
			clearAll();
			getWtCounts();
		}
		function doSearch2(){
			var cond = "";
			$("#queryForm2 input[type='checkbox']").each(function(){
				if($(this).is(':checked')){
					cond += $(this).attr('value')+",";
				}
			});
			cond = cond==""?"":"&sjzt="+cond.substring(0,cond.length-1);
			var data = combineQuery.getQueryCombineData(queryForm2,frmPost,DT21);
			defaultJson.doQueryJsonList(controllername+"?queryInfoBlr"+cond,data,DT21);
			clearAll2();
			getWtCounts();
		}
		//控制按钮
		function ctrlBtn(tempJson){
			if(tempJson.LRRFLAG=="2"){
				$("#btnSolve").attr("disabled","true");
				$("#btnUnSolve").attr("disabled","true");
				$("#btnUrge").attr("disabled","true");
				$("#btnResend").attr("disabled","true");
				$("#btnDelete").attr("disabled","true");
				$("#btnAddOpinion").attr("disabled","true");
			}else{
				if("0,1,2,4".indexOf(tempJson.SJZT)!=-1){
					$("#btnSolve").removeAttr("disabled");
					$("#btnUnSolve").removeAttr("disabled");
					$("#btnAddOpinion").removeAttr("disabled");
				}else{
					$("#btnSolve").attr("disabled","true");
					$("#btnUnSolve").attr("disabled","true");
					$("#btnAddOpinion").attr("disabled","true");
				}
				//催办按钮
				if(tempJson.SJZT=="1" ||tempJson.SJZT=="2"){
					$("#btnUrge").removeAttr("disabled");
				}else{
					$("#btnUrge").attr("disabled","true");
				}
				//处理按钮（部门人员可能暂时没有处理按钮）
				if(tempJson.SJZT=="3"||tempJson.SJZT=="6"){
					$("#btnDeal").attr("disabled","true");
				}else{
					$("#btnDeal").removeAttr("disabled");
				}
				//再次发送按钮
				if(tempJson.SJZT=="3" ||tempJson.SJZT=="4" ||tempJson.SJZT=="5" ){
					$("#btnResend").removeAttr("disabled");
					$("#btnDelete").removeAttr("disabled");
				}else{
					$("#btnResend").attr("disabled","true");
					$("#btnDelete").attr("disabled","true");
				}
			}
		}
		function doRandering(obj){
			var data = JSON.stringify(obj);
			var data1 = defaultJson.packSaveJson(data);
			var showHtml = "";
			$.ajax({
				url:controllername + "?getColor",
				data:data1,
				dataType:"json",
				async:false,
				success:function(result){
					var res = result.msg;
					if(obj.SJZT=="3" || obj.SJZT=="6"){
						showHtml = '<span class="label" style="width:50px;">完&nbsp;结</span>';
					}else{
						if(res=="none"){
							showHtml = '<span class="label" style="width:50px;">正&nbsp;常</span>';
						}else{
							tempJson = convertJson.string2json1(res);
							showHtml = '<span class="label '+tempJson.response.data[0].CLASS+'"  style="width:50px;">'+tempJson.response.data[0].TITLE+'</span>';
						}
					}
				}
			});
			return showHtml;
		}
		function btnHandel(n){
			if(n=="1"){
				$("#btnAdd").hide();
				$("#btnSolve").hide();
				$("#btnUnSolve").hide();
				$("#btnSugg").show();
			}else{
				$("#btnAdd").show();
				$("#btnSolve").show();
				$("#btnUnSolve").show();
				$("#btnSugg").hide();
			}
		}
		//---------------------------------
		//-行点击事件
		//---------------------------------
		function tr_click(obj,tabId){
			var elemName = $(event.target).parent().parent()[0].tagName;//("thead").html();
			if(elemName.toUpperCase()=="THEAD"){
				//点击表头就不触发行点击事件了
				return;
			}
			if(tabId=="DT21"){
				//清空表单数据以及两个关联的事件表数据
				$("#insertForm").clearFormResult(); 
				//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
				var rowindex = $("#"+tabId).getSelectedRowIndex();//获得选中行的索引
				var rowValue = $("#"+tabId).getSelectedRow();//获得选中行的json 字符串
				var tempJson = eval("("+rowValue+")");//字符串转JSON对象
				getEventList2(tempJson.WTTB_INFO_ID,DT23);
				ctrlBtn(tempJson);
				/**
				if(tempJson.LRRFLAG=="2"){
					$("#btnSolve").attr("disabled","true");
					$("#btnUnSolve").attr("disabled","true");
				}else{
					if(tempJson.SJZT=="3"||tempJson.SJZT=="6"){
						$("#btnSolve").attr("disabled","true");
						$("#btnUnSolve").attr("disabled","true");
					}else{
						$("#btnSolve").removeAttr("disabled");
						$("#btnUnSolve").removeAttr("disabled");
					}
					//催办按钮
					if(tempJson.SJZT=="1" ||tempJson.SJZT=="2" ){
						$("#btnUrge").removeAttr("disabled");
					}else{
						$("#btnUrge").attr("disabled","true");
					}
				}
				*/
			}else if(tabId=="DT0"){
				//清空表单数据以及两个关联的事件表数据
				$("#insertForm").clearFormResult(); 
				//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
				var rowindex = $("#"+tabId).getSelectedRowIndex();//获得选中行的索引
				var rowValue = $("#"+tabId).getSelectedRow();//获得选中行的json 字符串
				var tempJson = eval("("+rowValue+")");//字符串转JSON对象
				getEventList(tempJson.WTTB_INFO_ID,DT3);
				ctrlBtn(tempJson);
				/**
				if(tempJson.LRRFLAG=="2"){
					$("#btnSolve").attr("disabled","true");
					$("#btnUnSolve").attr("disabled","true");
				}else{
					if(tempJson.SJZT=="3"||tempJson.SJZT=="6"){
						$("#btnSolve").attr("disabled","true");
						$("#btnUnSolve").attr("disabled","true");
					}else{
						$("#btnSolve").removeAttr("disabled");
						$("#btnUnSolve").removeAttr("disabled");
					}
					//催办按钮
					if(tempJson.SJZT=="1" ||tempJson.SJZT=="2"){
						$("#btnUrge").removeAttr("disabled");
					}else{
						$("#btnUrge").attr("disabled","true");
					}
				}
				*/
			}else{
				//我处理的问题列表中，应该不允许任何按钮操作了
				$("#btnSolve").attr("disabled","true");
				$("#btnUnSolve").attr("disabled","true");
				$("#btnUrge").attr("disabled","true");
				$("#btnResend").attr("disabled","true");
				$("#btnDelete").attr("disabled","true");
				$("#btnAddOpinion").attr("disabled","true");
			}
		}
		//---------------------------------
		//-清除页面的
		//---------------------------------
		function clearAll(){
			//清空表单数据以及两个关联的事件表数据
			//$("#insertForm").clearFormResult(); 
			$("#DT2").empty();
			$("#DT3").clearResult();
		}
		//---------------------------------
		//-清除页面的
		//---------------------------------
		function clearAll2(){
			//清空表单数据以及两个关联的事件表数据
			//$("#insertForm").clearFormResult(); 
			$("#DT22").empty();
			$("#DT23").clearResult();
		}
		//---------------------------------
		//-有些数据是拼接出来的，单次查询无法实现，所以要单独进行一次局部查询
		//-@param n 问题编号
		//---------------------------------
		function getSomeValue(n){
			$.ajax({
				url:controllername + "?querySomeData&id="+n,
				data:"",
				dataType:"json",
				async:false,
				success:function(result){
					var res = eval('(' + result.msg + ')');
					var len = res.response.data.length;
					var jsrStrValue = "";
					var jsrStrCode = "";
					for(var i=0;i<len;i++){
						jsrStrValue = res.response.data[0].JSR_SV+",";
						jsrStrCode = res.response.data[0].JSR+","
					}
					if(jsrStrValue.length>1){
						jsrStrValue = jsrStrValue.substring(0,jsrStrValue.length-1);
						$("#jsrName").val(jsrStrValue);
					}
					if(jsrStrCode.length>1){
						jsrStrCode = jsrStrCode.substring(0,jsrStrCode.length-1);
						$("#jsrName").attr("code",jsrStrCode);
					}
					$("#pfid").val(res.response.data[0].PFID);
					$("#sfwc").val(res.response.data[0].SFWC);
				}
			});
		}
		//---------------------------------
		//-获取事件表数据
		//-@param n 问题编号
		//---------------------------------
		function getEventList2(n,obj){
			$.ajax({
				url:controllername + "?queryPfqk&id="+n,
				data:"",
				dataType:"json",
				async:false,
				success:function(result){
					showSuggestList2(result.msg);
					//add by zhangbr@ccthanking.com 现场说不要固定表头列头了，不好看，改回分页的形式. 问题编号：0000668
					setPageHeightWtzzDcl($(obj).attr("id"));
					var data = combineQuery.getQueryCombineData(blankForm,frmPost,obj);
					//historyListHeight = getHeight-pageTopHeight-pageTitle-getTableTh(1)-pageNumHeight-$("#suggestDcl").height();
					//defaultJson.doQueryJsonList(controllername + "?queryPfqk&id="+n,data,obj,"doFixedTableHeader2",true);
					defaultJson.doQueryJsonList(controllername + "?queryPfqk&id="+n,data,obj,null,true);
				}
			});
		}
		//---------------------------------
		//-显示批复意见列表
		//-@param str 查询返回的json串
		//---------------------------------
		function showSuggestList2(str){
			var files = convertJson.string2json1(str);
			$("#DT22").empty();
			for(var i=0;i<1;i++){
				var file = files.response.data[i];
				var showHtml = "";
				showHtml = "<ul class='unstyled myUl'>";
				//showHtml += "<li><p>"+file.PFSJ_SV+" &nbsp;"+file.PFR_SV+"：</p></li>";
				if(file.PFNR.length>200){
					showHtml += "<li><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<abbr title='"+file.PFNR+"'>"+file.PFNR.substring(0,200)+"</abbr></p></li>";
				}else{
					showHtml += "<li><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+file.PFNR+"</p></li>";
				}
				showHtml +="</ul>";
				$("#DT22").append(showHtml);
			}
			//$("#DT23").clearResult();
			//var strarr = $("#DT23").InsertTableRows(str,DT23);
			//$("#DT23").fnClickAddRowList(DT23,strarr);
		}
		//---------------------------------
		//-获取事件表数据
		//-@param n 问题编号
		//---------------------------------
		function getEventList(n,obj){
			$.ajax({
				url:controllername + "?queryPfqk&id="+n,
				data:"",
				dataType:"json",
				async:false,
				success:function(result){
					showSuggestList(result.msg);
					if($(obj).attr("id")=="DT3"){
						//add by zhangbr@ccthanking.com 现场说不要固定表头列头了，不好看，改回分页的形式. 问题编号：0000668
						//historyListHeight = getHeight-pageTopHeight-pageTitle-getTableTh(1)-pageNumHeight-$("#suggestWtj").height();
						//defaultJson.doQueryJsonList(controllername + "?queryPfqk&id="+n,data,obj,"doFixedTableHeader1",true);
						setPageHeightWtzzWtj($(obj).attr("id"));
						var data = combineQuery.getQueryCombineData(blankForm,frmPost,obj);
						defaultJson.doQueryJsonList(controllername + "?queryPfqk&id="+n,data,obj,null,true);
					}else{
						//add by zhangbr@ccthanking.com 现场说不要固定表头列头了，不好看，改回分页的形式. 问题编号：0000668
						//historyListHeight = getHeight-pageTopHeight-pageTitle-getTableTh(1)-pageNumHeight-$("#suggestDcl").height();
						//defaultJson.doQueryJsonList(controllername + "?queryPfqk&id="+n,data,obj,"doFixedTableHeader2",true);
						setPageHeightWtzzDcl($(obj).attr("id"));
						var data = combineQuery.getQueryCombineData(blankForm,frmPost,obj);
						defaultJson.doQueryJsonList(controllername + "?queryPfqk&id="+n,data,obj,null,true);
					}
				}
			});
		}
		//---------------------------------
		//-显示批复意见列表
		//-@param str 查询返回的json串
		//---------------------------------
		function showSuggestList(str){
			var files = convertJson.string2json1(str);
			$("#DT2").empty();
			for(var i=0;i<1;i++){
				var file = files.response.data[i];
				var showHtml = "";
				showHtml = "<ul class='unstyled myUl'>";
				//showHtml += "<li><p>"+file.PFSJ_SV+" &nbsp;"+file.PFR_SV+"：</p></li>";
				if(file.PFNR.length>200){
					showHtml += "<li><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<abbr title='"+file.PFNR+"'>"+file.PFNR.substring(0,200)+"</abbr></p></li>";
				}else{
					showHtml += "<li><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+file.PFNR+"</p></li>";
				}
				showHtml +="</ul>";
				$("#DT2").append(showHtml);
			}
			//$("#DT3").clearResult();
			//var strarr = $("#DT3").InsertTableRows(str,DT3);
			//$("#DT3").fnClickAddRowList(DT3,strarr);
		}
		//---------------------------------
		//-显示流转历史列表
		//-@param str 查询返回的json串
		//---------------------------------
		function showHistoryList(str){
			$("#DT23").clearResult();
			var strarr = $("#DT23").InsertTableRows(str,DT3);
			$("#DT23").fnClickAddRowList(DT3,strarr);
		}
		//---------------------------------
		//-打开项目信息查询页面
		//---------------------------------
		function showXzxm(){
			$(window).manhuaDialog({"title" : "项目信息查询","type" : "text","content" : "${pageContext.request.contextPath}/jsp/business/zjb/lbj/xmcx.jsp","modal":"2"});
		}
		function getWinData(data){
			switch(btnNum){
				case 1:
					//发送问题
					var data1 = defaultJson.packSaveJson(data);
					var priv_ywid = $("#ywid").val()==""||$("#ywid").val()==undefined?"":"&ywid="+$("#ywid").val();
					defaultJson.doInsertJson(controllername + "?sendReport"+priv_ywid,data1,DT0);
					var queryData = combineQuery.getQueryCombineData(queryForm0,frmPost,DT0);
					defaultJson.doQueryJsonList(controllername+"?queryInfoJsr",queryData,DT0);
					break;
				case 2:
					var index = $("#DT0").getSelectedRowIndex();//获得选中行的索引
					var data1 = defaultJson.packSaveJson(data);
					defaultJson.doUpdateJson(controllername + "?doSugg",data1,DT0);
					$("#DT0").setSelect(index);
					var rowValue = $("#DT0").getSelectedRow();//获得选中行的json 字符串
					var tempJson = eval("("+rowValue+")");//字符串转JSON对象
					//getEventList(tempJson.WTTB_INFO_ID,DT0);
					break;
				case 3:
					var data1 = eval("("+data+")");
					$("#q_xmmc").val(data1.XMMC);
					$("#q_xmid").val(data1.XMBH);
					$("#q_bdmc").val(data1.BDMC);
					$("#q_bdid").val(data1.BDBH);
					break;
				case 4:
					var tabId = "DT21";
					if(nowTabPageNUm==2){
						tabId = "DT21";
					}else if(nowTabPageNUm==0){
						tabId = "DT0";
					}else if(nowTabPageNUm==1){
						tabId = "DT1";
					}
					var index =	$("#"+tabId).getSelectedRowIndex();
					var rowValue = $("#"+tabId).getSelectedRow();
					var tempJson = eval("("+rowValue+")");
					var resJson = eval("("+data+")");
					tempJson["SJZT"] = "6";
					tempJson["SJSJ"] = resJson.SJSJ;
					tempJson["MYCD"] = resJson.MYCD;
					var data = JSON.stringify(tempJson);
		 			var data1 = defaultJson.packSaveJson(data);
					if(nowTabPageNUm==2){
		 				defaultJson.doDeleteJson(controllername + "?doConfirm&blrjs=fqr", data1,DT21);
						doSearch();
						clearAll();
					}else if(nowTabPageNUm==0){
		 				defaultJson.doUpdateJson(controllername + "?doConfirm&blrjs=fqr", data1,DT0);
						doSearch2();
					}
					ctrlBtn(tempJson);
	 				break;
	 			case 5:
	 				var tabId = "DT21";
					if(nowTabPageNUm==2){
						tabId = "DT21";
					}else if(nowTabPageNUm==0){
						tabId = "DT0";
					}else if(nowTabPageNUm==1){
						tabId = "DT1";
					}
					var index =	$("#"+tabId).getSelectedRowIndex();
					var rowValue = $("#"+tabId).getSelectedRow();
					var tempJson = eval("("+rowValue+")");
					var resJson = eval("("+data+")");
					tempJson["SJZT"] = "3";
					tempJson["MYCD"] = resJson.MYCD;
					var data = JSON.stringify(tempJson);
		 			var data1 = defaultJson.packSaveJson(data);
					if(nowTabPageNUm==2){
		 				defaultJson.doDeleteJson(controllername + "?doConfirm&blrjs=fqr", data1,DT21);
						doSearch();
						clearAll();
					}else if(nowTabPageNUm==0){
		 				defaultJson.doUpdateJson(controllername + "?doConfirm&blrjs=fqr", data1,DT0);
						doSearch2();
					}
					ctrlBtn(tempJson);
			}
			clearAll2();
		}
		function closeParentCloseFunction(val){
		}
		function closeNowCloseFunction(){
			window.parent.getWtList();
		}
		function bindEvent(){
		}
		function openInfoCard(n){
			var index = $(event.target).closest("tr").index();
			var tabId = $(event.target).closest("table").attr("id");
	    	$("#"+tabId).cancleSelected();
	    	$("#"+tabId).setSelect(index);
			if($("#"+tabId).getSelectedRowIndex()==-1){
				requireSelectedOneRow();
				return;
			}else{
				$(window).manhuaDialog({"title":"问题提报信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/wtCard.jsp?infoID="+n,"modal":"1"});
	    	}
		}
		function showInfoCard(obj){
			var showHtml = "";
			var id = obj.WTTB_INFO_ID;
			showHtml = "<a href='javascript:void(0);' title='问题信息卡' onclick=\"openInfoCard('"+id+"')\"><i class='icon-file showInfoCard' wtid='"+obj.WTTB_INFO_ID+"'></i></a>";
			return showHtml;
		}
		function sendReport(data){
			var data1 = defaultJson.packSaveJson(data);
			defaultJson.doUpdateJson(controllername + "?sendReport&ywid="+$("#ywid").val(),data1,DT0);
			doSearch();
			doSearch2();
		}
		//-----------------------------------
		//-再次提请按钮回调函数
		//-----------------------------------
		function doResend(data){
			if(nowTabPageNUm==2){
				tabId = "DT21";
			}else if(nowTabPageNUm==0){
				tabId = "DT0";
			}else if(nowTabPageNUm==1){
				tabId = "DT1";
			}
			var obj = document.getElementById(tabId);
			var data1 = defaultJson.packSaveJson(data);
			defaultJson.doUpdateJson(controllername + "?doResend&ywid="+$("#ywid").val(),data1,obj);
			doSearch();
			doSearch2();
		}
		function setYwid(n){
			$("#ywid").val(n);
		}
		function getWtCounts(){
			$.ajax({
				url:controllername + "?getWtCounts",
				data:"",
				dataType:"json",
				async:false,
				success:function(result){
					var res = result.msg;
					var tempJson = convertJson.string2json1(res);
					var row = tempJson.response.data[0];
					if(row.COUNTS=="0"){
						$("#sywtsl").html("");
					}else{
						$("#sywtsl").html(row.COUNTS);
					}
				}
			});
		}
		//显示信息
		function doShowMsg(str){
			xAlertMsg(str);
		}
		//----------------------------------------
		//-隐藏批复内容
		//----------------------------------------
		function hidePfnr(obj){
			if(obj.BLRJS=="3"){
				return '<div style="text-align:center;">—</div>';
			}
		}
		//------------------------------------------------
		//-固定表头列头
		//------------------------------------------------
		function doFixedTableHeader1(){
			var rows = $("tbody tr" ,$("#DT3"));
			var tr_obj = rows.eq(0);
			var t = $("#DT3").getTableRows();
			var tr_height = $(tr_obj).height();
			var d = (t*tr_height)+(getTableTh(3)/2);
			if(d>historyListHeight){
				d = historyListHeight;
			}
			window_width = window.screen.availWidth
			$("#DT3").fixTable({
				fixColumn: 0,//固定列数
				width:window_width*0.3-6,//显示宽度
				height:d//显示高度
			});			
		}
		//------------------------------------------------
		//-固定表头列头
		//------------------------------------------------
		function doFixedTableHeader2(){
			var rows = $("tbody tr" ,$("#DT23"));
			var tr_obj = rows.eq(0);
			var t = $("#DT23").getTableRows();
			var tr_height = $(tr_obj).height();
			var d = t*tr_height;
			var d = (t*tr_height)+(getTableTh(3)/2);
			if(d>historyListHeight){
				d = historyListHeight;
			}
			window_width = window.screen.availWidth
			$("#DT23").fixTable({
				fixColumn: 0,//固定列数
				width:window_width*0.3-6,//显示宽度
				height:d//显示高度
			});			
		}
		function getCheckboxCond(formName){
			var cond1 = "";
			var cond2 = "";
			$("#"+formName+" input[type='checkbox']").each(function(){
				if($(this).is(':checked')){
					if($(this).attr("name")=="BLRJS"){
						cond1 += $(this).attr('value')+",";
					}else if($(this).attr("name")=="HFQK"){
						cond2 += $(this).attr('value')+",";
					}
				}
			});
			cond1 = cond1==""?"":"&blrjs="+cond1.substring(0,cond1.length-1);
			cond2 = cond2==""?"":"&hfqk="+cond2.substring(0,cond2.length-1);
			return cond1+cond2;
		}
		//“未回复”状态高亮
		function showHfqk(obj){
			var showHtml = "";
			if(obj.HFQK=="0"){
				showHtml = '<span class="label label-warning">未回复</span>';
			}
			return showHtml;
		}
  	</script>
  </head>
	<body id="a">
		<app:dialogs />
		<div class="row-fluid">
			<p class="text-right tabsRightButtonP">
				<button id="btnAdd" class="btn" type="button" title="新增一个问题">
					新增
				</button>
				<button id="btnAddOpinion" class="btn" type="button" title="对自己发起的问题进行补充意见操作">
					补充意见
				</button>
				<button id="btnSolve" class="btn" type="button" title="只有问题的发起人才能对自己的问题使用“解决”功能">
					解决
				</button>
				<button id="btnUnSolve" class="btn" type="button" title="只有问题的发起人才能对自己的问题使用“未解决”功能">
					未解决
				</button>
				<button id="btnUrge" class="btn" type="button" 
					title="只有审核中和处理中的问题，才允许问题的发起人使用催办功能；催办后，催办次数加1，并且系统会自动向当前办理人或审核人发送提醒">
					催办
				</button>
				<button id="btnResend" class="btn" type="button" 
					title="当问题处于“被退回”、“待填报人确认”、“无法解决”状态时，允许问题的发起人进行再次发送操作">
					再次提请
				</button>
				<button id="btnDelete" class="btn" type="button" 
					title="当问题处于“被退回”、“待填报人确认”、“无法解决”状态时，允许问题的发起人进行删除操作">
					删除
				</button>
			</p>
			<ul class="nav nav-tabs">
				<li class="active">
					<a href="#tab2" data-toggle="tab" id="tabPage2">待处理的问题&nbsp;<span class="badge" id="sywtsl" title="剩余问题数量"></span></a>
				</li>
				<li class="">
					<a href="#tab0" data-toggle="tab" id="tabPage0">我提交的问题</a>
				</li>
				<li class="">
					<a href="#tab1" data-toggle="tab" id="tabPage1">我处理的问题</a>
				</li>
			</ul>
			<div class="tab-content active">
				<div class="tab-pane active" id="tab2">
					<div class="span7">
							<div class="row-fluid">
								<div class="B-small-from-table-autoConcise">
									<form method="post"
										action="${pageContext.request.contextPath }/insertdemo.do"
										id="queryForm2">
										<table class="B-table" width="100%">
											<!--可以再此处加入hidden域作为过滤条件 -->
											<TR style="display: none;">
												<TD class="right-border bottom-border"></TD>
												<TD class="right-border bottom-border">
													<INPUT type="text" class="span12" kind="text"
														fieldname="rownum" value="1000" operation="<=">
												</TD>
												<td>
													人员角色隐藏条件-用于控制按钮，不传入后台
												</td>
												<TD class="right-border bottom-border">
													<INPUT type="text" class="span12" kind="text" id="ryjs_cond">
												</TD>
											</TR>
											<!--可以再此处加入hidden域作为过滤条件 -->
											<tr>
												<th width="8%" class="right-border bottom-border">
													问题标题
												</th>
												<td width="17%" class="right-border bottom-border">
													<input class="span12" type="text" placeholder="" name="WTBT"
														fieldname="I.WTBT" operation="like" logic="and">
												</td>
												<th width="8%" class="right-border bottom-border">
													问题性质
												</th>
												<td width="27%" class="bottom-border">
													<select class="span12 6characters" name="WTXZ" fieldname="I.WTXZ"
														kind="dic" src="WTXZ" operation="=" logic="and" defaultMemo="全部">
													</select>
												</td>
												<td class="right-border bottom-border text-right">
													<button id="btnQuery2" class="btn btn-link" type="button">
														<i class="icon-search"></i>查询
													</button>
													<button id="btnClear2" class="btn btn-link" type="button">
														<i class="icon-trash"></i>清空
													</button>
												</td>
											</tr>
										</table>
									</form>
									<div style="height: 5px;">
									</div>
									<div class="overFlowX">
									<table class="table-hover table-activeTd B-table" id="DT21"
										width="100%" type="single" pageNum="10" pagingFunction="bindEvent">
										<thead>
											<tr>
												<th name="XH" id="_XH" rowspan="2" colindex=1>
													&nbsp;#&nbsp;
												</th>
												<th fieldname="WTLX" maxlength="15"
													tdalign="center" rowspan=2 colindex=2
													CustomFunction="showInfoCard">
													&nbsp;&nbsp;
												</th>
												<th fieldname="WTLX" tdalign="center" rowspan=2 colindex=3>
													&nbsp;问题类别&nbsp;
												</th>
												<th fieldname="WTBT" rowspan="2" colindex=4 style="width: 20%"
													maxlength="9">
													&nbsp;问题标题&nbsp;
												</th>
												<th fieldname="SJZT" rowspan="2" colindex=5 maxlength="10" tdalign="center">
													&nbsp;问题状态&nbsp;
												</th>
												<th colspan=2>
													&nbsp;问题最新情况&nbsp;
												</th>
												<th fieldname="LRSJ" tdalign="center" rowspan=2 colindex=8>
													&nbsp;提出时间&nbsp;
												</th>
												<th fieldname="CQBZ"
													tdalign="center" rowspan=2 colindex=9>
													&nbsp;超期情况&nbsp;
												</th>
												<th fieldname="CBCS"
													tdalign="right" rowspan=2 colindex=10>
													&nbsp;催办次数&nbsp;
												</th>
												<th fieldname="HFQK"
													tdalign="center" rowspan=2 colindex=11 customFunction="showHfqk">
													&nbsp;回复情况&nbsp;
												</th>
											</tr>
											<tr>
												<th fieldname="JSR" colindex=6 tdalign="center">
													&nbsp;接收人&nbsp;
												</th>
												<th fieldname="XWWCSJ" colindex=7 tdalign="center">
													&nbsp;希望反馈日期&nbsp;
												</th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
									</div>
								</div>
							</div>
					</div>
					<div class="span5" style="height:100%;">
						<blockquote>
							<h4 class="title">
								问题追踪
							</h4>
							<div class="B-small-from-table-autoConcise">
								<div id="suggestDcl" style="border:1px;">
									<h5 class="title" style="color:#000000;">
										问题描述
									</h5>
									<div id="DT22">
									</div>
								</div>
								<h5 class="title" style="color:#000000;">
									处理信息
									<span class="pull-right">
										<button id="btnExp1" class="btn" type="button">
											导出
										</button> </span>
								</h5>
								<form method="post" action="" id="blankForm">
									<table class="B-table" width="100%">
										<!--可以再此处加入hidden域作为过滤条件 -->
										<TR style="display: none;">
											<TD class="right-border bottom-border">
												<INPUT type="text" class="span12" kind="text"
													fieldname="rownum" value="1000" operation="<=">
											</TD>
										</TR>
										<!--可以再此处加入hidden域作为过滤条件 -->
									</table>
								</form>
								<table class="table-hover table-activeTd B-table" id="DT23"
									width="100%" pageNum="10"  printFileName="问题追踪">
									<thead>
										<tr>
											<th fieldname="BLRJS" tdalign="center">
												人员角色
											</th>
											<th fieldname="JSR">
												姓名
											</th>
											<th fieldname="PFSJ" tdalign="center">
												处理时间
											</th>
											<th fieldname="PFNR" maxlength="10">
												内容
											</th>
											<th fieldname="FJ">
												附件
											</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</blockquote>
					</div>
				</div>
				<div class="tab-pane" id="tab0">
					<div class="span7">
							<div class="row-fluid">
								<div class="B-small-from-table-autoConcise">
									<form method="post"
										action="${pageContext.request.contextPath }/insertdemo.do"
										id="queryForm0">
										<table class="B-table" width="100%">
											<!--可以再此处加入hidden域作为过滤条件 -->
											<TR style="display: none;">
												<TD class="right-border bottom-border"></TD>
												<TD class="right-border bottom-border">
													<INPUT type="text" class="span12" kind="text"
														fieldname="rownum" value="1000" operation="<=">
												</TD>
												<td>
													人员角色隐藏条件-用于控制按钮，不传入后台
												</td>
												<TD class="right-border bottom-border">
													<INPUT type="text" class="span12" kind="text" id="ryjs_cond">
												</TD>
											</TR>
											<!--可以再此处加入hidden域作为过滤条件 -->
											<tr>
												<th width="8%" class="right-border bottom-border">
													问题标题
												</th>
												<td width="17%" class="right-border bottom-border">
													<input class="span12" type="text" placeholder="" name="WTBT"
														fieldname="I.WTBT" operation="like" logic="and">
												</td>
												<th width="8%" class="right-border bottom-border">
													问题性质
												</th>
												<td width="27%" class="bottom-border">
													<select class="span12 6characters" name="WTXZ" fieldname="I.WTXZ"
														kind="dic" src="WTXZ" operation="=" logic="and" defaultMemo="全部">
													</select>
												</td>
												<td class="right-border bottom-border text-right">
													<button id="btnQuery" class="btn btn-link" type="button">
														<i class="icon-search"></i>查询
													</button>
													<button id="btnClear0" class="btn btn-link" type="button">
														<i class="icon-trash"></i>清空
													</button>
												</td>
											</tr>
										</table>
									</form>
									<div style="height: 5px;">
									</div>
									<div class="overFlowX">
									<table class="table-hover table-activeTd B-table" id="DT0"
										width="100%" type="single" pageNum="10" pagingFunction="bindEvent">
										<thead>
											<tr>
												<th name="XH" id="_XH" rowspan="2" colindex=1>
													&nbsp;#&nbsp;
												</th>
												<th fieldname="WTLX" maxlength="15"
													tdalign="center" rowspan=2 colindex=2
													CustomFunction="showInfoCard">
													&nbsp;&nbsp;
												</th>
												<th fieldname="WTLX" tdalign="center" rowspan=2 colindex=3>
													&nbsp;问题类别&nbsp;
												</th>
												<th fieldname="WTBT" rowspan="2" colindex=4 style="width: 20%"
													maxlength="9">
													&nbsp;问题标题&nbsp;
												</th>
												<th fieldname="SJZT" rowspan="2" colindex=5 maxlength="10" tdalign="center">
													&nbsp;问题状态&nbsp;
												</th>
												<th colspan=2>
													&nbsp;问题最新情况&nbsp;
												</th>
												<th fieldname="LRSJ" tdalign="center" rowspan=2 colindex=8>
													&nbsp;提出时间&nbsp;
												</th>
												<th fieldname="CQBZ"
													tdalign="center" rowspan=2 colindex=9>
													&nbsp;超期情况&nbsp;
												</th>
												<th fieldname="CBCS"
													tdalign="right" rowspan=2 colindex=10>
													&nbsp;催办次数&nbsp;
												</th>
												<th fieldname="HFQK" customFunction="showHfqk"
													tdalign="center" rowspan=2 colindex=11>
													&nbsp;回复情况&nbsp;
												</th>
											</tr>
											<tr>
												<th fieldname="JSR" colindex=6 tdalign="center">
													&nbsp;接收人&nbsp;
												</th>
												<th fieldname="XWWCSJ" colindex=7 tdalign="center">
													&nbsp;希望反馈日期&nbsp;
												</th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
									</div>
								</div>
							</div>
					</div>
					<div class="span5" style="height:100%;">
						<blockquote>
							<h4 class="title">
								问题追踪
							</h4>
							<div class="B-small-from-table-autoConcise">
								<div id="suggestWtj" style="border:1px;">
									<h5 class="title" style="color:#000000;">
										问题描述
									</h5>
									<div id="DT2">
									</div>
								</div>
								<h5 class="title" style="color:#000000;">
									处理信息
									<span class="pull-right">
										<button id="btnExp2" class="btn" type="button">
											导出
										</button> </span>
								</h5>
								<table class="table-hover table-activeTd B-table" id="DT3"
									width="100%" pageNum="10" printFileName="问题追踪">
									<thead>
										<tr>
											<th fieldname="BLRJS" tdalign="center">
												人员角色
											</th>
											<th fieldname="JSR">
												姓名
											</th>
											<th fieldname="PFSJ" tdalign="center">
												处理时间
											</th>
											<th fieldname="PFNR" maxlength="10">
												内容
											</th>
											<th fieldname="FJ">
												附件
											</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</blockquote>
					</div>
				</div>
				<div class="tab-pane" id="tab1">
					<div class="row-fluid">
							<div class="B-small-from-table-autoConcise">
								<form method="post" action="" id="queryForm1">
									<table class="B-table" width="100%">
										<TR style="display: none;">
											<TD class="right-border bottom-border"></TD>
											<TD class="right-border bottom-border">
												<INPUT type="text" class="span12" kind="text"
													fieldname="rownum" value="1000" operation="<=">
											</TD>
											<td>
												人员角色隐藏条件-用于控制按钮，不传入后台
											</td>
											<TD class="right-border bottom-border">
												<INPUT type="text" class="span12" kind="text" id="ryjs_cond">
											</TD>
											<th width="8%" class="right-border bottom-border">
												回复情况
											</th>
											<td style="width:12%;min-width:150px;" class="bottom-border">
												<label class="checkbox inline"><input type="checkbox" name="HFQK" value="'2'" title="待回复" keep="true">待回复</label>
												<label class="checkbox inline"><input type="checkbox" name="HFQK" value="'3','4','6'" title="已回复" keep="true" checked>已回复</label>
											</td>
										</TR>
										<!--可以再此处加入hidden域作为过滤条件 -->
										<tr>
											<th width="8%" class="right-border bottom-border">
												问题标题
											</th>
											<td width="17%" class="right-border bottom-border">
												<input class="span12" type="text" placeholder="" name="WTBT"
													fieldname="I.WTBT" operation="like" logic="and">
											</td>
											<th width="8%" class="right-border bottom-border">
												问题性质
											</th>
											<td width="12%" class="bottom-border" colspan=3>
												<select class="span12 6characters" name="WTXZ" fieldname="I.WTXZ"
													kind="dic" src="WTXZ" operation="=" logic="and" defaultMemo="全部">
												</select>
											</td>
											<th width="8%" class="right-border bottom-border">
												人员角色
											</th>
											<td class="bottom-border">
												<label class="checkbox inline"><input type="checkbox" name="BLRJS" value="'1'" title="主办人">主办人</label>
												<label class="checkbox inline"><input type="checkbox" name="BLRJS" value="'2'" title="送办人">送办人</label>
												<label class="checkbox inline">
													<input type="checkbox" name="BLRJS" value="'4'"
														title="抄送人">
													抄送人
												</label>
											</td>
											<td class="right-border bottom-border text-right">
												<button id="btnQuery1" class="btn btn-link" type="button">
													<i class="icon-search"></i>查询
												</button>
												<button id="btnClear1" class="btn btn-link" type="button">
													<i class="icon-trash"></i>清空
												</button>
											</td>

										</tr>
									</table>
								</form>

								<div style="height: 5px;">
								</div>
								<div class="overFlowX">
								<table width="100%" class="table-hover table-activeTd B-table"
									type="single" id="DT1" pageNum="5">
									<thead>
										<tr>
											<th name="XH" id="_XH">
												#
											</th>
											<th fieldname="WTLX" maxlength="15" tdalign="center" 
												CustomFunction="showInfoCard">
												&nbsp;&nbsp;
											</th>
											<th fieldname="WTXZ" tdalign="center">
												&nbsp;问题性质&nbsp;
											</th>
											<th fieldname="WTBT">
												&nbsp;问题标题&nbsp;
											</th>
											<th fieldname="LRR">
												&nbsp;提出人&nbsp;
											</th>
											<th fieldname="LRSJ" tdalign="center">
												&nbsp;提出时间&nbsp;
											</th>
											<th fieldname="ZBR">
												&nbsp;主办人&nbsp;
											</th>
											<th fieldname="SJSJ" tdalign="center">
												&nbsp;解决时间&nbsp;
											</th>
											<th fieldname="XMMC" maxlength="15">
												&nbsp;涉及项目&nbsp;
											</th>
											<th fieldname="SJZT" tdalign="center">
												&nbsp;问题状态&nbsp;
											</th>
											<th fieldname="CQBZ"
												tdalign="center" colindex=9>
												&nbsp;超期情况&nbsp;
											</th>
											<th fieldname="CBCS"
												tdalign="right" colindex=10>
												&nbsp;催办次数&nbsp;
											</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
								</div>
							</div>
						</div>
				</div>
			</div>
		</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="ywid" id="ywid">
				<input type="hidden" name="txtFilter" order="desc" fieldname="SJZT,LRSJ"
					id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id = "queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">

			</FORM>
		</div>
		<div id="showMyModal" class="modal hide fade" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" >
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">
					×
				</button>
				<h3 id="myModalLabel">
					批复意见
				</h3>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="span12">
						<div class="B-small-from-table-autoConcise" style="">
							<h4>
								信息查询
							</h4>
							<form method="post"
								action=""
								id="myModalForm">
								<table class="B-table" width="100%">
									<tr>
										<td width="100%" class="bottom-border">
											<textarea class="span12" rows="3" name="PFNR"
												fieldname="PFNR"></textarea>
										</td>
									</tr>
								</table>
							</form>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button class="btn" data-dismiss="modal" aria-hidden="true">
						关闭
					</button>
					<button class="btn btn-primary" id="btnGetPsn">
						确定
					</button>
				</div>
			</div>
		</div>
	</body>
</html>
