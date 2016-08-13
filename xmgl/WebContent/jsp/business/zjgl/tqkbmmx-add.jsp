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
<title>提请款明细-维护</title>
<%
	String type=request.getParameter("type");
	String QKLX = request.getParameter("QKLX");
	if(QKLX==null){
	    QKLX = "";
	}
//获取当前用户信息
User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
OrgDept dept = user.getOrgDept();
String deptId = dept.getDeptID();
String deptName = dept.getDept_Name();
String userid = user.getAccount();
String username = user.getName();

%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglTqkbmmxController.do";
var controllernameYfdw= "${pageContext.request.contextPath }/htgl/gcHtglHtController.do";
var controllernameHtsj= "${pageContext.request.contextPath }/htgl/gcHtglHtsjController.do";

var type ="<%=type%>", qklx="<%=QKLX%>";
var msgControl="";
var flag_dosearch = true;
var htid = '';
//页面初始化
$(function() {
	init();
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#gcZjglTqkmxForm").validationButton())
		{
		    //生成json串
		    var data = Form2Json.formToJSON(gcZjglTqkmxForm);
		  //组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  //调用ajax插入
		    if($("#ID").val() == "" || $("#ID").val() == null){
    			var success=defaultJson.doInsertJson(controllername + "?insert", data1);
		    	if(success==true)
				{
					var obj=$("#resultXML").val();
					var subresultmsgobj1=defaultJson.dealResultJson(obj);
					$("#gcZjglTqkmxForm").setFormValues(subresultmsgobj1);
					
					//var parentmain=$(window).manhuaDialog.getParentObj();	
					//parentmain.$("#btnQuery").click();
					msgControl = "showMsg";
					$(window).manhuaDialog.close();
				}
    			
    		}else{
    			var success=defaultJson.doUpdateJson(controllername + "?update", data1);
    			if(success==true)
				{
    				msgControl = "showMsg";
					$(window).manhuaDialog.close();
					
    				//var parentmain=$(window).manhuaDialog.getParentObj();	
					//parentmain.$("#btnQuery").click();
				}
    		}
		}else{
			requireFormMsg();
		  	return;
		}
	});
	//完成投资
	$("#btnWctz").click(function() {
		$(window).manhuaDialog({"title":"部门合同>合同完成投资","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtwctz-indexOne.jsp?fromPage=tqlbmmxadd&hasHt=one&htid="+htid,"modal":"1"});
	});
	//按钮绑定事件（新增）
    $("#btnClear_Bins").click(function() {
        $("#gcZjglTqkmxForm").clearFormResult();
        
        var parentIObj = $(this).manhuaDialog.getParentObj();
    	var tempJson = parentIObj.$("#ID").val();
    	if(tempJson!=""){
    		$("#QTQKID").val(tempJson);
    	}
        
        $("#ID").val("");
        $("#YBF").val(0)
        $("#ZXHTJ").val(0);
        $("#LJBF").val(0);
        $("#AHTBFB").val(0);
        
    });
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
    
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
	
});
//页面默认参数
function init(){
	$("#btnWctz").hide();
	var parentIObj = $(this).manhuaDialog.getParentObj();
	if(type == "insert"){
		var tempJson = parentIObj.$("#ID").val();
		if(tempJson!=""){
			$("#QTQKID").val(tempJson);
		}
		
		//请款年月
		var nd = parentIObj.$("#QKNF").val();
		if(nd!=""){
			$("#QND").val(nd);
		}else{
			flag_dosearch = false;
		}
		var yf = parentIObj.$("#GCPC").val();
		if(yf!=""){
			$("#QJLYF").val(yf);
		}else{
			flag_dosearch = false;
		}
		
		$("#BMBLR").val('<%=userid%>');
		
	}else if(type == "update" || type == "detail"){
		var tempJson = parentIObj.$("#QMXID").val();
		if(tempJson!=""){
			$("#QID").val(tempJson);
		}
		
		var bmtqkmxzt ="xxx";
		
		//查询记录数
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
				$("#gcZjglTqkmxForm").setFormValues(resultobj);
				htid = resultobj.HTXXID;
				bmtqkmxzt = resultobj.BMTQKMXZT;
				if(resultobj.HTLX!="SG"){
					$("#id_view_fsg_wctz").show();
					var zf = $("#HTZF").val();
					var tz = $("#WCZF").val();
					if(parseFloat(zf)>parseFloat(tz)){
						$("#btnWctz").show();
					}
				}
			}
		});
		
		//如果是未提交状态，则查询计量值等
		if(type == "update" && qklx=="16" && (bmtqkmxzt=="1" || bmtqkmxzt=="")){
			querySJZ();
		}
	}
	
	if(qklx=="16"){
		//施工合同显示监理值等
		$("tr[id^='id_view_sgl']").each(function(i){
		   $(this).show();
		});
		$("#id_view_pql_001").show();
	}else if(qklx=="14"){
		$("#id_view_pql_001").show();
	}
}
function gengxintz(){
	var htsjid = $("#QHTID").val();
	$.ajax({
		url : controllernameHtsj+"?query&id="+htsjid,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(response) {
			var resultmsgobj = defaultJson.dealResultJson(response.msg);
			if(resultmsgobj!=0){
				var tz = resultmsgobj.WCZF==''?'0':resultmsgobj.WCZF;
				var zf = resultmsgobj.HTZF==''?'0':resultmsgobj.HTZF;
				$("#WCZF").val(resultmsgobj.WCZF);
				$("#HTZF").val(resultmsgobj.HTZF);
				if(parseFloat(zf)>parseFloat(tz)){
					$("#btnWctz").show();
				}else{
					$("#btnWctz").hide();
				}
				
			}
		}
	});
}

//点击行事件
function tr_click(obj){
	//alert(JSON.stringify(obj));
	$("#gcZjglTqkmxForm").setFormValues(obj);
}

//选中项目名称弹出页
var g_btnNum = 0;
function selectHt(){
	g_btnNum = 1;
	$(window).manhuaDialog({"title":"合同选择","type":"text","content":"${pageContext.request.contextPath}/jsp/business/htgl/htcx.jsp?ZBBMID="+<%=deptId%>+"&optype=tqkbmmx","modal":"2"});
<%--$(window).manhuaDialog({"title":"合同选择","type":"text","content":"${pageContext.request.contextPath}/jsp/business/htgl/htcx.jsp?ZBBMID="+<%=deptId%>,"modal":"2"});--%>
}

function xzxm(n) {
	g_btnNum = n;
	$(window).manhuaDialog({"title" : "选择单位","type" : "text","content" : "${pageContext.request.contextPath}/jsp/business/gcb/cjdw/cjdw_Query_Add.jsp","modal":"4"});
}


//弹出区域回调
getWinData = function(data){
	if(g_btnNum==2){
		var odd=convertJson.string2json1(data);
		$("#DWMC").val(JSON.parse(data).DWMC);
		//$("#ZBDL").attr("code",JSON.parse(data).GC_CJDW_ID);
	}else{
		var parseObj = JSON.parse(data);
		$("#HTMC").val(parseObj.HTMC);
		$("#XMMC").val(parseObj.XMMC);
		$("#BDMC").val(parseObj.BDMC);
		$("#QYBFHTID_JGC").val(parseObj.JHSJID);
		if(parseObj.BDMC!=""){
			$("#XMMCNR").val(parseObj.XMMC+"-"+parseObj.BDMC);
		}else{
			$("#XMMCNR").val(parseObj.XMMC);
		}
		$("#HTBM").val(parseObj.HTBM);
		$("#QHTID").val(parseObj.HTSJID);
		$("#DWMC").val(parseObj.YFDW);
		$("#ZXHTJ").val(parseObj.ZHTQDJ);
		
		//影响完成支付查询
		if(parseObj.HTLX=="SG"){
			htid = parseObj.HTID;
		}
		$("#QYBFHTID").val(parseObj.HTSJID);
		
		
		$("#HTZF").val(parseObj.HTZF==''?'0':parseObj.HTZF);
		$("#WCZF").val(parseObj.WCZF==''?'0':parseObj.WCZF);
		if(parseObj.HTLX!='SG'){
			$("#id_view_fsg_wctz").show();
			var zf = $("#HTZF").val();
			var tz = $("#WCZF").val();
			if(parseFloat(zf)>parseFloat(tz)){
				$("#btnWctz").show();
			}
			
		}else{
			$("#id_view_fsg_wctz").hide();
			$("#btnWctz").hide();
			querySJZ();
			if(qklx=="16"){
				// 请款类型 工程类
				cal_bywctz();
			}
		}

		if(parseObj.HTLX=='PQ'){
			queryPQSJZ();
		}
		
		queryYfdw();
	}
	//$("#YBF").val(parseObj.ZHTZF);
};

//计算扣除甲供材
function kcjgc_val(){
	var BCSQ = $("#BCSQ").val();
	var JGC = $("#JGC").val();
	if(BCSQ!="" && JGC!=""){
		$("#KQJGC").val( parseInt(BCSQ)- parseInt(JGC));
	}
}

//查询本月完成投资
function cal_bywctz(){
	var ZFNF = $("#QND").val();
	var ZFYF = $("#QJLYF").val();
	var QYBFHTID = $("#QYBFHTID_JGC").val();
	if(ZFNF!="" && ZFYF!="" && QYBFHTID!=""){
		
		//alert("执行查询");
		//根据数据ID查询所关联的合同信息 乙方单位
		var data = combineQuery.getQueryCombineData(queryFormJgc,frmPost);
		var data1 = {
			msg : data
		};
		$.ajax({
			url : controllernameYfdw+"?queryDwxxByJhid&opttype=bywctz",
			data : data1,
			cache : false,
			async :	false,
			dataType : "json",  
			type : 'post',
			success : function(response) {
				var resultmsgobj = convertJson.string2json1(response.msg);
				if(resultmsgobj!=0){
					var resultobj =  resultmsgobj.response.data[0];
					$("#JGC").val(resultobj.GCK_LJ);
					kcjgc_val();
				}else{
					$("#JGC").val("0");
				}
			}
		});
		
	}
}

	function queryYfdw(){
		//根据数据ID查询所关联的合同信息 乙方单位
		var data = combineQuery.getQueryCombineData(queryFormYbf,frmPost);
		var data1 = {
			msg : data
		};
		$.ajax({
			url : controllernameYfdw+"?queryDwxxByJhid&opttype=ybf",
			data : data1,
			cache : false,
			async :	false,
			dataType : "json",  
			type : 'post',
			success : function(response) {
				var resultmsgobj = convertJson.string2json1(response.msg);
				if(resultmsgobj!=0){
					var resultobj =  resultmsgobj.response.data[0];
					$("#YBF").val(resultobj.YBF);
					//$("#JNDW").val(resultobj.YFDW);
					//$("#JNDW").attr("code",resultobj.YFID);
				}else{
					//$("#JNDW").val("");
					//$("#JNDW").attr("code","");
				}
			}
		});
	}
function querySJZ(){
	
	var parentIObj = $(this).manhuaDialog.getParentObj();
	var nf = parentIObj.$("#QKNF").val();
	var yf = parentIObj.$("#GCPC").val();
	$.ajax({
		url : controllername+"?queryJlsj&id="+nf+":"+yf+":"+$("#QHTID").val(),
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(response) {
			var resultmsgobj  = convertJson.string2json2(response.msg);
			if(resultmsgobj.CSZ==""){
				$("#CSZ").val("0");
			}else{
				$("#CSZ").val(resultmsgobj.CSZ);
			}
			if(resultmsgobj.SJZ==""){
				$("#SJZ").val("0");
			}else{
				$("#SJZ").val(resultmsgobj.SJZ);
			}
			if(resultmsgobj.JZQR==""){
				$("#JZQR").val("0");
			}else{
				$("#JZQR").val(resultmsgobj.JZQR);
			}

			if($("#JZQR").val()!="0"){
				var jzqr = $("#JZQR").val();
				var ljbf = $("#LJBF").val();
				if(jzqr!='0'){
					var str_percent = Math.round(ljbf/jzqr *10000)/100.00;
					$("#AJLFKB").val(str_percent);
				}
			}
		}
	});
	
}
function queryPQSJZ(){
	$("#CSZ").val("0");
	var parentIObj = $(this).manhuaDialog.getParentObj();
	$.ajax({
		url : controllername+"?queryPqsdz&id="+$("#QHTID").val(),
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(response) {
			var resultmsgobj  = convertJson.string2json2(response.msg);
			if(resultmsgobj!=null&&resultmsgobj!=''){
				if(resultmsgobj.HTSX=="1"){
					$("#CSZ").val("0");
				}else if(resultmsgobj.HTSX=="2"){
					$("#CSZ").val(resultmsgobj.SDZ);
				}
			}
		}
	});
	
}
function calPercen(t){
	var ZXHTJ = $("#ZXHTJ").val();
	//	var str_percent = Math.round( t.value / ZXHTJ *100);
	var BCSQ=$("#BCSQ").val();
	 
		 if(BCSQ!=""&&parseInt(BCSQ)>parseInt(ZXHTJ))
		 {
			 xAlert("本次申请拨款","申请拨款金额不能大于合同价！");
			 return;
		 }
	
	
	
	
	var ljbf = parseInt(t.value)+parseInt($("#YBF").val());
	$("#LJBF").val(ljbf);
	
	
	var str_percent = Math.round(ljbf/ZXHTJ *10000)/100.00;
	//var str_percent = Math.round(t.value/ZXHTJ *10000)/100.00;
	$("#AHTBFB").val(str_percent);

	if($("#JZQR").val()!='0'){
		var ljbf = $("#LJBF").val();
		var str_percent = Math.round(ljbf/$("#JZQR").val() *10000)/100.00;
		$("#AJLFKB").val(str_percent);
	}
	
	kcjgc_val();
}

function calAJLFKBPercen(t){
	var ljbf = $("#LJBF").val();
	var str_percent = Math.round(ljbf/t.value *10000)/100.00;
	$("#AJLFKB").val(str_percent);
}


function closeNowCloseFunction(){
	return msgControl;
}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">

	<div class="B-small-from-table-autoConcise" style="display:none;">
			<h4 class="title">
				部门提请款明细
			</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border">
							<input class="span12" type="text" id="QID" name="ID"  fieldname="t.ID" value="" operation="=" >
						</TD>
					</TR>
				</table>
			</form>
	 	</div>

	<div class="B-small-from-table-autoConcise">
      <h4 class="title">提请款明细
      	<span class="pull-right">
      		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
      		<button id="btnWctz" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" style='display:none'>完成投资</button>
      		<!--  <button id="btnClear_Bins" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">继续新增</button>-->
      	</span>
      </h4>
     <form method="post" id="gcZjglTqkmxForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/>
      	<input type="hidden" id="QTQKID" name="TQKID"  fieldname="TQKID" >
	  	<input type="hidden" id="QHTID" name="HTID"  fieldname="HTID" >
	  	<input type="hidden" id="QDWID" name="DWID"  fieldname="DWID" >
	  	<input type="hidden" id="BMBLR" name="BMBLR"  fieldname="BMBLR" >
	  	<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">合同名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="HTMC" type="text" placeholder="必填" check-type="required" fieldname="HTMC" name = "HTMC"  disabled />
          		<button class="btn btn-link"  type="button" onclick="selectHt()" title="合同选择"><i class="icon-edit"></i></button>
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">合同编码</th>
       		<td class="bottom-border right-border"width="10%">
         		<input class="span12" style="width:100%" id="HTBM" type="text" fieldname="HTBM" name = "HTBM"  disabled/>
         	</td>
        </tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
			<td class="bottom-border right-border" >
				<input class="span12" style="width:100%" id="XMMC" type="text" fieldname="XMMC" name = "XMMC"  disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
			<td width="17%" class="right-border bottom-border">
				<input class="span12" style="width:100%" id="BDMC" type="text" fieldname="BDMC" name = "BDMC"  disabled/>
<%--				<i width="10%" class="icon-edit" title="请选择单位" onclick="xzxm(2);"></i>--%>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称内容</th>
			<td class="bottom-border right-border" >
				<textarea class="span12" rows="2" id="XMMCNR" check-type="maxlength" maxlength="500" fieldname="XMMCNR" name="XMMCNR" readonly></textarea>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">乙方单位全称</th>
			<td width="17%" class="right-border bottom-border">
				<input id="DWMC"  class="span12" check-type="maxlength" maxlength="200"  name="DWMC" fieldname="DWMC" style="width:100%" type="text" disabled />
<%--				<i width="10%" class="icon-edit" title="请选择单位" onclick="xzxm(2);"></i>--%>
			</td>
		</tr>
		<tr id="id_view_pql_001" style="display:none;">
			<th width="8%" class="right-border bottom-border text-right disabledTh">财审值</th>
			<td width="17%" class="right-border bottom-border">
				<input id="CSZ" value=0 class="span9" style="width:70%;text-align:right;" name="CSZ" fieldname="CSZ" type="number" disabled/>
			</td>
			<td width="17%" class="right-border bottom-border" colspan="2">
			&nbsp;
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">最新合同价</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZXHTJ" value=0 class="span9" style="width:70%;text-align:right;" name="ZXHTJ" fieldname="ZXHTJ" type="number" disabled/>(元)</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">已拔付</th>
			<td width="17%" class="right-border bottom-border">
				<input id="YBF" value=0 class="span9" style="width:70%;text-align:right;" name="YBF" fieldname="YBF" type="number" disabled/>(元)</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right ">本次申请拔款</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BCSQ" value=0  onchange="calPercen(this);"  class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required" min="0" name="BCSQ" fieldname="BCSQ" type="number" />(元)</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">累计拔付</th>
			<td width="17%" class="right-border bottom-border">
				<input id="LJBF" value=0 class="span9" style="width:70%;text-align:right;"  name="LJBF" fieldname="LJBF" type="number" disabled/>
				<input id="AHTBFB" value=0 class="span9" style="width:20%;text-align:right;" title="累计支付百分比" name="AHTBFB" fieldname="AHTBFB" type="number" disabled/>%
			</td>
		</tr>
		<tr id="id_view_sgl_001" style="display:none;">
			<th width="8%" class="right-border bottom-border text-right disabledTh">审计值</th>
			<td width="17%" class="right-border bottom-border">
				<input id="SJZ" value=0 class="span9" style="width:70%;text-align:right;" name="SJZ" fieldname="SJZ" type="number" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">监理确认计量款</th>
			<td width="17%" class="right-border bottom-border">
				<input id="JZQR" onchange="calAJLFKBPercen(this);" value=0 class="span9" style="width:70%;text-align:right;" name="JZQR" fieldname="JZQR" type="number" disabled/>
				<input id="AJLFKB" value=0 class="span9" style="width:20%;text-align:right;" title="按计量付款比例" name="AJLFKB" fieldname="AJLFKB" type="number" disabled/>%
			</td>
		</tr>
		<tr id="id_view_sgl_002" style="display:none;">
			<th width="8%" class="right-border bottom-border text-right">甲供材</th>
			<td width="17%" class="right-border bottom-border">
				<input id="JGC" onchange="kcjgc_val();" value=0 class="span9" style="width:70%;text-align:right;" name="JGC" fieldname="JGC" type="number" />
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">扣除甲供材</th>
			<td width="17%" class="right-border bottom-border">
				<input id="KQJGC" value=0 class="span9" style="width:70%;text-align:right;" name="KQJGC" fieldname="KQJGC" type="number" readOnly/>
			</td>
		</tr>
		<tr id="id_view_fsg_wctz" style="display:none;">
			<th width="8%" class="right-border bottom-border text-right">合同支付</th>
			<td width="17%" class="right-border bottom-border">
				<input id="HTZF" value=0 class="span9" style="width:70%;text-align:right;" name="HTZF" fieldname="HTZF" type="number" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right">完成投资</th>
			<td width="17%" class="right-border bottom-border">
				<input id="WCZF" value=0 class="span9" style="width:70%;text-align:right;" name="WCZF" fieldname="WCZF" type="number" disabled/>
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
    </div>
   </div>
  </div>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
<%--         <input type="hidden" name="txtFilter"  order="desc" fieldname = "t.LRSJ" id = "txtFilter">--%>
		 <input type="hidden" name="txtFilter"  order="desc">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 	
 	<form method="post" id="queryFormYbf">
	<!--可以再此处加入hidden域作为过滤条件 -->
	<TR style="display: none;">
		<TD class="right-border bottom-border">
			<input type="hidden" id="QYBFHTID" fieldname="HTID" operation="="/>
		</TD>
	</TR>	
	</form>
	<form method="post" id="queryFormJgc">
	<!--可以再此处加入hidden域作为过滤条件 -->
	<TR style="display: none;">
		<TD class="right-border bottom-border">
			<input type="hidden" id="QYBFHTID_JGC" fieldname="TCJH_SJ_ID" operation="="/>
			<input type="hidden" id="QND" fieldname="ND" operation="="/>
			<input type="hidden" id="QJLYF" fieldname="JLYF" operation="="/>
		</TD>
	</TR>	
	</form>
 </div>
</body>
<script>
</script>
</html>