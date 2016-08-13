<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>合同支付-维护</title>
<%
	//合同类型
	String htlx=request.getParameter("htlx");
	if(htlx==null || "".equals(htlx)){
	    htlx = "";
	}
	
	String cYear = Pub.getDate("yyyy");
	String cMoth = Pub.getDate("MM");
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtHtzfController.do";
var controllernameHtsj= "${pageContext.request.contextPath }/htgl/gcHtglHtsjController.do";
var controllernameYfdw= "${pageContext.request.contextPath }/htgl/gcHtglHtController.do";
var retMsg="";
//页面初始化
$(function() {
	$("#ZFNF").val('<%=cYear%>');
	$("#ZFYF").val('<%=cMoth%>');
	
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtHtzfList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,gcHtglHtHtzfList);
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#gcHtglHtHtzfForm").validationButton())
		{
		    //生成json串
		    var data = Form2Json.formToJSON(gcHtglHtHtzfForm);
		  //组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  //调用ajax插入
		    if($("#ID").val() == "" || $("#ID").val() == null){
    			var flag = defaultJson.doInsertJson(controllername + "?insert", data1);
    			if(flag){
    		 		var parentmain=$(window).manhuaDialog.getParentObj();
    				parentmain.$("#btnQueryGroup").click();
    				
    				retMsg = "showMsg";
    				$(window).manhuaDialog.close();
    		 	}
    			$("#gcHtglHtHtzfForm").clearFormResult();
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
        $("#gcHtglHtHtzfForm").clearFormResult();
        $("#gcHtglHtHtzfForm").cancleSelected();
        
        
        $("#ZFRQ").val(new Date().toLocaleDateString());
        
        $("#YFK").val(0);
        $("#ID").val("");
    });
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
});
//页面默认参数
function init(){
	<% 
		if("PQ".equals(htlx)){
			%>
				$("#pq_je").show();
				
			<%
		}else{
			%>
				$("#htsx").remove();
			<%
		}
	%>
	var parentIObj = $(this).manhuaDialog.getParentObj();
	var tempJson = parentIObj.$("#currHtid").val();
	if(typeof(tempJson) != "undefined"){
	//if (typeof(parent.$("body").find("#currHtid")) != "undefined") {
		//从hthtzf-indexOne.jsp新增
	   	//var tempJson = parent.$("body").find("#currHtid").val();
		if(tempJson!=""){
			$("#subCcurrHtid").val(tempJson);
			//生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtHtzfList);
			//调用ajax插入
			var flag = defaultJson.doQueryJsonList(controllernameHtsj+"?queryOne",data,gcHtglHtHtzfList);
			if(flag){
				if($("#gcHtglHtHtzfList tbody tr").length==0){
		        	return
				}
				$("#gcHtglHtHtzfList").setSelect(0);
				
				var obj = $("#gcHtglHtHtzfList").getSelectedRowJsonObj();
				tr_click(obj, "gcHtglHtHtzfList");
			}
		}
	}else{
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtHtzfList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllernameHtsj+"?queryOne",data,gcHtglHtHtzfList);
	}
}


//点击行事件
function tr_click(obj, tabid){
	//alert(JSON.stringify(obj));
	$("#gcHtglHtHtzfForm").setFormValues(obj);
	$("#HTMC").attr('title',obj.HTMC);
	$("#SSZ").val(obj.SSZ);
	$("#SDZ").val(obj.SDZ);
	$("#QYBFHTID").val(obj.JHSJID);
	$("#ID").val("");
	<%
  	if(htlx.equals("SG")){
  %>
	
  $("#GCYFK").val("0");
  $("#KGZYFK").val("0");
  cal_bywctz();
  <%}else{	%>
	
	<%}%>
	$("#id_LJYZFB")[0].innerHTML=obj.LJYZFB;
	$("#id_LJWCZFB")[0].innerHTML=obj.LJWCZFB;
	$("#btnSave").attr("disabled", false);
}

//选中项目名称弹出页
function selectXm(){
	$("body").manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/xmcx.jsp","modal":"2"});
}
//弹出区域回调
getWinData = function(data){
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#XMBH").val(JSON.parse(data).XMBH);
	$("#GC_TCJH_XMXDK_ID").val(JSON.parse(data).GC_TCJH_XMXDK_ID);
};

//详细信息
function rowView(index){
	var obj = $("#gcHtglHtHtzfList").getSelectedRowJsonByIndex(index);
	var xmbh = eval("("+obj+")").XMBH;
	$("body").manhuaDialog(xmscUrl(xmbh));
}

function closeNowCloseFunction(){
	return retMsg;
}

//计算百分比
function cal_ZFJE(){
	
	var YFK = parseInt($("#YFK").val());
	var GCYFK = parseInt($("#GCYFK").val());
	var KGZYFK = parseInt($("#KGZYFK").val());
	var ZFJE = parseInt(YFK+GCYFK-KGZYFK);
	//alert("YFK="+YFK+"   GCYFK="+GCYFK+"  KGZYFK="+KGZYFK+"  ZFJE="+ZFJE);
	$("#ZFJE").val(ZFJE);
	//$("#WKYFK").val(GCYFK-KGZYFK);
	
	var zz = parseInt($("#ZFJE").val());
	var ljzf = zz+parseInt($("#HTZF").val());
	var ljzf1 = zz+parseInt($("#WCZF").val());
	var zj = parseInt($("#ZHTQDJ").val());
	
	//alert("zz="+zz+"   ljzf="+ljzf+"  ljzf1="+ljzf1+"  zj="+zj);
	if(zj==0){
		$("#BCHTZFBL").val("0");
		$("#BCWCZFBL").val("0");
	}else{
		var str_percent = Math.round(ljzf/zj *10000)/100.00;
		var str_percent1 = Math.round(ljzf1/zj *10000)/100.00;
		$("#BCHTZFBL").val(str_percent);
		$("#BCWCZFBL").val(str_percent1);
	}
}
//查询本月完成投资
function cal_bywctz(){
	var ZFNF = $("#ZFNF").val();
	var ZFYF = $("#ZFYF").val();
	if(ZFNF!="" && ZFYF!=""){
		
		var ZFNF = $("#ZFNF").val();
		var ZFYF = $("#ZFYF").val();
		$("#QND").val(ZFNF);
		$("#QJLYF").val(ZFYF);
		
		//alert("执行查询");
		//根据数据ID查询所关联的合同信息 乙方单位
		var data = combineQuery.getQueryCombineData(queryFormYbf,frmPost);
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
					$("#BYWCTZ").val(resultobj.DYYFK);
					$("#span_id_jgc")[0].innerHTML=resultobj.GCK_DY;
					$("#span_id_sjwcgc")[0].innerHTML=resultobj.GCK1_DY;
					//$("#JNDW").val(resultobj.YFDW);
					//$("#JNDW").attr("code",resultobj.YFID);
				}else{
					$("#BYWCTZ").val("0");
					$("#span_id_jgc")[0].innerHTML=0;
					$("#span_id_sjwcgc")[0].innerHTML=0;
					//$("#JNDW").val("");
					//$("#JNDW").attr("code","");
				}
			}
		});
		
	}
}


//计算百分比
function cal_BCHTZFBL_Percen(t){
	var ljzf = parseInt(t.value)+parseInt($("#HTZF").val());
	var ljzf1 = parseInt(t.value)+parseInt($("#WCZF").val());
	var zj = parseInt($("#ZHTQDJ").val());
	if(zj==0){
		$("#BCHTZFBL").val("0");
		$("#BCWCZFBL").val("0");
	}else{
		var str_percent = Math.round(ljzf/zj *10000)/100.00;
		var str_percent1 = Math.round(ljzf1/zj *10000)/100.00;
		$("#BCHTZFBL").val(str_percent);
		$("#BCWCZFBL").val(str_percent1);
	}
}
function doBDMC(obj){
	if(obj.BDMC==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}

function doHTBM(obj){
	if(obj.HTBM==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.HTBM;
	}
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
     <form method="post" id="queryForm"  >
     		<INPUT type="hidden" class="span12" kind="text" id="subCcurrHtid" name="ghh.id" fieldname="ghh.id" value="" operation="="/>
      </form>
    <div style="height:5px;"></div>
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="gcHtglHtHtzfList" width="100%" type="single" pageNum="5">
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
           		<th fieldname="HTBM" colindex=2 tdalign="center" maxlength="40"  rowMerge="true" CustomFunction="doHTBM">&nbsp;合同编码&nbsp;</th>
<%--				<th fieldname="HTMC" colindex=4 tdalign="center" maxlength="50">&nbsp;合同名称&nbsp;</th>--%>
				<th fieldname="HTLX" colindex=3 tdalign="center" maxlength="30"  rowMerge="true">&nbsp;合同类型&nbsp;</th>
				<th fieldname="HTSX" colindex=4 id='htsx' maxlength="30" tdalign="center" >&nbsp;合同属性&nbsp;</th>
<%--				<th fieldname="HTZT" colindex=6 tdalign="center" maxlength="30"  rowMerge="true">&nbsp;合同状态&nbsp;</th>--%>
<%--				<th fieldname="FBFS" colindex=8 tdalign="center" maxlength="30" >&nbsp;发包方式&nbsp;</th>--%>
				<th fieldname="HTQDJ" colindex=5 tdalign="right" >&nbsp;合同签订价&nbsp;</th>
				<th fieldname="ZXHTJ" colindex=6 tdalign="right" >&nbsp;最新合同价&nbsp;</th>
				<th fieldname="GCJGBFB" colindex=7 tdalign="center" >&nbsp;(%)&nbsp;</th>
				<th fieldname="XMMC"  maxlength="40" colindex=8 tdalign="left" >&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDMC"  maxlength="30" colindex=9 tdalign="left" CustomFunction="doBDMC">&nbsp;标段名称&nbsp;</th>
				<th fieldname="YFDW"  maxlength="30" colindex=10 tdalign="left" >&nbsp;乙方单位&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	</div>
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">合同支付
      	<span class="pull-right">
<%--      		<button id="btnClear_Bins" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>--%>
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">保存</button>
      	</span>
      </h4>
       <%
      	if(htlx.equals("SG")){
      %>
      <form method="post" id="gcHtglHtHtzfForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
	  	<input type="hidden" id="HTSJID" fieldname="HTSJID" name = "HTSJID"/></TD>
	  	<input type="hidden" id="ZHTQDJ" fieldname="ZHTQDJ" name = "ZHTQDJ"/>
	  	
        <tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">合同名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="HTMC" type="text" fieldname="HTMC" name = "HTMC"  disabled />
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">合同编码</th>
       		<td class="bottom-border right-border"width="15%">
         		<input class="span12" style="width:100%" id="HTBM" type="text" fieldname="HTBM" name = "HTBM"  disabled/>
         	</td>
        </tr>
        <tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="XMMC" type="text"  fieldname="XMMC" name = "XMMC"  disabled />
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
       		<td class="bottom-border right-border"width="15%">
         		<input class="span12" style="width:100%" id="BDMC" type="text" fieldname="BDMC" name = "BDMC"  disabled/>
         	</td>
        </tr>
        <tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">累计已支付</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input id="HTZF" value=0 class="span9" style="width:70%;text-align:right;" name="HTZF" fieldname="HTZF" type="number" min="0" readOnly/><b>(元)</b>(<span id="id_LJYZFB"></span>%)
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">完成投资</th>
       		<td class="bottom-border right-border"width="15%">
         		<input id="WCZF" value=0 class="span9" style="width:70%;text-align:right;" name="WCZF" fieldname="WCZF" type="number" min="0" readOnly/><b>(元)</b>(<span id="id_LJWCZFB"></span>%)
         	</td>
        </tr>
<tr>
	<th width="8%" class="right-border bottom-border text-right">支付年份</th>
	<td width="17%" class="right-border bottom-border">
		<select  id="ZFNF"   placeholder="必填" check-type="required" onchange="cal_bywctz();" class="span6"  name="ZFNF" fieldname="ZFNF"  operation="=" kind="dic" src="XMNF"  defaultMemo="-支付年份-">
	</td>
	<th width="8%" class="right-border bottom-border text-right">支付月份</th>
	<td width="17%" class="right-border bottom-border">
		<select  id="ZFYF"   placeholder="必填" check-type="required"  onchange="cal_bywctz();" class="span6"  check-type="maxlength" maxlength="2" name="ZFYF" fieldname="ZFYF"  operation="=" kind="dic" src="YF"  defaultMemo="-支付月份-">
	</td>
</tr>

<tr>
	<th width="8%" class="right-border bottom-border text-right">本月完成投资</th>
	<td width="17%" class="right-border bottom-border">
		<input id="BYWCTZ" value=0 class="span9" style="width:70%;text-align:right;" name="BYWCTZ" fieldname="BYWCTZ" type="number" min="0" readOnly/><b>(元)</b>
		<br/>（其中：甲供材<span id="span_id_jgc"></span> 元，实际完成工程<span id="span_id_sjwcgc"></span>元）
	</td>
<th width="8%" class="right-border bottom-border text-right">未扣预付款</th>
	<td width="17%" class="right-border bottom-border">
		<input id="WKYFK" value=0 class="span9" style="width:70%;text-align:right;"    name="WKYFK" fieldname="WKYFK" type="number" min="0" readOnly/><b>(元)</b>
	</td>
</tr>
<tr>
	<th width="8%" class="right-border bottom-border text-right">进度款</th>
	<td width="17%" class="right-border bottom-border">
		<input id="YFK" value=0 class="span9" style="width:70%;text-align:right;" onchange="cal_ZFJE();" name="YFK" fieldname="YFK" type="number" min="0"/><b>(元)</b>
	</td>
<th width="8%" class="right-border bottom-border text-right">工程预付款</th>
	<td width="17%" class="right-border bottom-border">
		<input id="GCYFK" value=0 class="span9" style="width:70%;text-align:right;"  onchange="cal_ZFJE();"  name="GCYFK" fieldname="GCYFK" type="number" min="0"/><b>(元)</b>
	</td>
</tr>
<tr>
	<th width="8%" class="right-border bottom-border text-right">扣甲供材</th>
	<td width="17%" class="right-border bottom-border">
		<input id="KBLK" value=0 class="span9" style="width:70%;text-align:right;"  name="KBLK" fieldname="KBLK" type="number" min="0"/><b>(元)</b>
	</td>
	<th width="8%" class="right-border bottom-border text-right">扣工程预付款</th>
	<td width="17%" class="right-border bottom-border">
		<input id="KGZYFK" value=0 class="span9" style="width:70%;text-align:right;"   onchange="cal_ZFJE();"   name="KGZYFK" fieldname="KGZYFK" type="number" min="0"/><b>(元)</b>
	</td>
</tr>
	<th width="8%" class="right-border bottom-border text-right">支付金额</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZFJE" class="span12" keep="true" placeholder="必填" check-type="required" check-type="maxlength" maxlength="17" value=0 style="width:70%;text-align:right;" readOnly name="ZFJE" fieldname="ZFJE" type="number" min="0"/><b>(元)</b>
			</td>
			<th width="8%" class="right-border bottom-border text-right">支付日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZFRQ" class="span9" fieldtype="date" fieldformat="YYYY-MM-DD"  placeholder="必填" check-type="required" check-type="maxlength" maxlength="10" name="ZFRQ" fieldname="ZFRQ" type="date" />
			</td>
		</tr>

      <%
      	}else{
      %>
      
     <form method="post" id="gcHtglHtHtzfForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/>
	  	<input type="hidden" id="HTSJID" fieldname="HTSJID" name = "HTSJID"/>
	  	<input type="hidden" id="ZHTQDJ" fieldname="ZHTQDJ" name = "ZHTQDJ"/>
<%--	  	<input type="hidden" id="HTQDJ" fieldname="HTQDJ" name = "HTQDJ"/>--%>
	  	
	  	<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">合同名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="HTMC" type="text" fieldname="HTMC" name = "HTMC"  disabled />
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">合同编码</th>
       		<td class="bottom-border right-border"width="15%">
         		<input class="span12" style="width:100%" id="HTBM" type="text" fieldname="HTBM" name = "HTBM"  disabled/>
         	</td>
        </tr>
        <tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="XMMC" type="text"  fieldname="XMMC" name = "XMMC"  disabled />
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
       		<td class="bottom-border right-border"width="15%">
         		<input class="span12" style="width:100%" id="BDMC" type="text" fieldname="BDMC" name = "BDMC"  disabled/>
         	</td>
        </tr>
        <tr id="pq_je" style="display:none;">
			<th width="8%" class="right-border bottom-border text-right disabledTh">送审值</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input id="SSZ" value=0 class="span9" style="width:75%;text-align:right;" name="SSZ" type="number" min="0" readOnly/><b>(元)</b>
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">审定值</th>
       		<td class="bottom-border right-border"width="15%">
         		<input id="SDZ" value=0 class="span9" style="width:85%;text-align:right;" name="SDZ" type="number" min="0" readOnly/><b>(元)</b>
         	</td>
        </tr>
        <tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">累计已支付</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input id="HTZF" value=0 class="span9" style="width:70%;text-align:right;" name="HTZF" fieldname="HTZF" type="number" min="0" readOnly/><b>(元)</b>(<span id="id_LJYZFB"></span>%)
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">完成投资</th>
       		<td class="bottom-border right-border"width="15%">
         		<input id="WCZF" value=0 class="span9" style="width:70%;text-align:right;" name="WCZF" fieldname="WCZF" type="number" min="0" readOnly/><b>(元)</b>(<span id="id_LJWCZFB"></span>%)
         	</td>
        </tr>
        
<tr>
	<th width="8%" class="right-border bottom-border text-right">支付年份</th>
	<td width="17%" class="right-border bottom-border">
		<select  id="ZFNF"   placeholder="必填" check-type="required" class="span6"  name="ZFNF" fieldname="ZFNF"  operation="=" kind="dic" src="XMNF"  defaultMemo="-支付年份-">
	</td>
	<th width="8%" class="right-border bottom-border text-right">支付月份</th>
	<td width="17%" class="right-border bottom-border">
		<select  id="ZFYF"   placeholder="必填" check-type="required" class="span6"  check-type="maxlength" maxlength="2" name="ZFYF" fieldname="ZFYF"  operation="=" kind="dic" src="YF"  defaultMemo="-支付月份-">
	</td>
</tr>
<tr>
	<th width="8%" class="right-border bottom-border text-right">支付金额</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZFJE" onchange="cal_BCHTZFBL_Percen(this);" class="span12" keep="true" placeholder="必填" check-type="required" check-type="maxlength" maxlength="17" value=0 style="width:70%;text-align:right;" name="ZFJE" fieldname="ZFJE" type="number" min="0"/><b>(元)</b>
			</td>
			<th width="8%" class="right-border bottom-border text-right">支付日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZFRQ" class="span9" fieldtype="date" fieldformat="YYYY-MM-DD"  placeholder="必填" check-type="required" check-type="maxlength" maxlength="10" name="ZFRQ" fieldname="ZFRQ" type="date" />
			</td>
		</tr>
      <%} %>
<tr>
	<th width="8%" class="right-border bottom-border text-right">本次支付后支付比</th>
	<td width="17%" class="right-border bottom-border">
		<input id="BCHTZFBL" value=0 class="span9" style="width:60%;text-align:right;" type="number" min="0" readOnly/><b>%</b>(占合同金额)
	</td>
	<th width="8%" class="right-border bottom-border text-right">本次支付后支付比</th>
	<td width="17%" class="right-border bottom-border">
		<input id="BCWCZFBL" value=0 class="span9" style="width:60%;text-align:right;" type="number" min="0" readOnly/><b>%</b>(占完成投资)
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
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "ghh2.LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 	
 	<form method="post" id="queryFormYbf">
	<!--可以再此处加入hidden域作为过滤条件 -->
	<TR style="display: none;">
		<TD class="right-border bottom-border">
			<input type="hidden" id="QYBFHTID" fieldname="TCJH_SJ_ID" operation="="/>
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