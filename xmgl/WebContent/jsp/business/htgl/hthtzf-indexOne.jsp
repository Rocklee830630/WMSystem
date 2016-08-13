<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<title>合同数据首页</title>
<%
	//合同类型
	String htlx=request.getParameter("htlx");
	if(htlx==null || "".equals(htlx)){
	    htlx = "";
	}
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtHtzfController.do";
var controllernameYfdw= "${pageContext.request.contextPath }/htgl/gcHtglHtController.do";

//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryListForm,frmListPost,DT2);
		//调用ajax插入
		var flag1 = defaultJson.doQueryJsonList(controllername+"?query",data,DT2);
			if(flag1){
				if($("#DT2 tbody tr").length==0){
		        	return
				}
				$("#DT2").setSelect(0);
				var objq = $("#DT2").getSelectedRowJsonObj();
				tr_click(objq, "DT2");
			}
	});
	$("#btnQueryGroup").click(function() {
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryOne",data,DT1);
	});
	
	//按钮绑定事件(新增)
	$("#btnInsert").click(function() {
		$(window).manhuaDialog({"title":"合同支付>新增","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtzf-add.jsp?type=insert&htlx=<%=htlx%>","modal":"2"});
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#hthtzfDetailForm").validationButton())
		{
		    //生成json串
		    var data = Form2Json.formToJSON(hthtzfDetailForm);
		  //组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  //调用ajax插入
    		var flag = defaultJson.doUpdateJson(controllername + "?update", data1,DT2);
		  if(flag){
			  btnQueryGroup.click();
		  }
		}else{
			requireFormMsg();
		  	return;
		}
	});
	//按钮绑定事件(修改)
	$("#btnUpdate").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"合同数据>修改","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtzf-add.jsp?type=update","modal":"1"});
	});
	//按钮绑定事件(删除)
    $("#btnDelete").click(function() {
		if($("#ID").val()!=""){
			xConfirm("信息确认","您确定删除所选信息吗？ ");
			
			$('#ConfirmYesButton').attr('click','');
			$('#ConfirmYesButton').one("click",function(){
				
				//生成json串
			    var data = Form2Json.formToJSON(hthtzfDetailForm);
			  	//组成保存json串格式
			    var data1 = defaultJson.packSaveJson(data);
			    $("#btnDelete").attr("disabled", true);
	    		var flag = defaultJson.doDeleteJson(controllername + "?delete", data1,DT2);
	    		if(flag){
	    			//alert("删除成功");
	    			btnQueryGroup.click();
	    		}
				
			});
		}else{
			requireFormMsg();
		  	return;
		}
	});
	
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 var t = $("#DT1").getTableRows();
		 if(t<=0)
		 {
			 xAlert("提示信息","请至少查询出一条记录！");
			 return;
		 }
	  	 $(window).manhuaDialog({"title":"导出","type":"text","content":"${pageContext.request.contextPath }/jsp/framework/print/TabListEXP.jsp?tabId=DT1","modal":"3"});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
    });
	
});

//页面默认参数
function init(){
	var parentIObj = $(this).manhuaDialog.getParentObj();
	var tempJson = parentIObj.$("#resultXML").val();
	
	if(tempJson!=""){
		$("#currHtid").val(convertJson.string2json1(tempJson).ID);
		$("#HTID").val(convertJson.string2json1(tempJson).ID);
		//getNd();
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		var flag = defaultJson.doQueryJsonList(controllername+"?queryOne",data,DT1, null, true);
		if(flag){
			if($("#DT1 tbody tr").length==0){
	        	return
			}else{
				$("#DT1").setSelect(0);
				var obj = $("#DT1").getSelectedRowJsonObj();
				$("#QZFNF").val(obj.ZFNF);
				$("#QZFYF").val(obj.ZFYF);
				//tr_click(obj, "DT1");
				var data1 = combineQuery.getQueryCombineData(queryListForm,frmListPost,DT2);
				//调用ajax插入
				var flag1 = defaultJson.doQueryJsonList(controllername+"?query",data1,DT2);
				if(flag1){
					if($("#DT2 tbody tr").length==0){
			        	return
					}
					$("#DT2").setSelect(0);
					var objq = $("#DT2").getSelectedRowJsonObj();
					tr_click(objq, "DT2");
				}
			}
		}
	}
}
//默认年度
function getNd(){
	//年度信息，里修改
	$("#ZFRQ").val(new Date().getFullYear());
}

//点击获取行对象
function tr_click(obj,tabListid){
	//alert(JSON.stringify(obj));
	if("DT1"==tabListid){
		$("#QZFNF").val(obj.ZFNF);
		$("#QZFYF").val(obj.ZFYF);
		$("#btnQuery").click();
	}else{
		$("#HTSJID").val(obj.HTSJID);
		$("#hthtzfDetailForm").setFormValues(obj);
		$("#btnSave").attr("disabled", false);
		$("#btnDelete").attr("disabled", false);
		
		$("#QYBFHTID").val(obj.JHSJID);
		$("#id_LJYZFB")[0].innerHTML=obj.LJYZFB;
		$("#id_LJWCZFB")[0].innerHTML=obj.LJWCZFB;
		<%
	  	if(htlx.equals("SG")){
	  %>
		cal_bywctz();
		 <%}	%>
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

function closeParentCloseFunction(msgc){
	if (typeof(msgc) == "undefined") {
	}else if(msgc=="showMsg"){
		xSuccessMsg('操作成功！',null);
	}
	
	init();
}


//计算百分比
function cal_ZFJE(){
	
	var YFK = parseInt($("#YFK").val());
	var GCYFK = parseInt($("#GCYFK").val());
	var KGZYFK = parseInt($("#KGZYFK").val());
	var ZFJE = parseInt(YFK+GCYFK-KGZYFK);
	//alert("YFK="+YFK+"   GCYFK="+GCYFK+"  KGZYFK="+KGZYFK+"  ZFJE="+ZFJE);
	$("#ZFJE").val(ZFJE);
	
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
		var data = combineQuery.getQueryCombineData(queryFormYbf,frmPostYbf);
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
					//$("#JNDW").val("");
					//$("#JNDW").attr("code","");
					$("#BYWCTZ").val("0");
					$("#span_id_jgc")[0].innerHTML=0;
					$("#span_id_sjwcgc")[0].innerHTML=0;
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
function doZFRQ(obj){
	if(obj.ZFRQ==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.ZFRQ;
	}
}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">合同支付数据
				<span class="pull-right">  
					<button id="btnInsert" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">新增</button>
<%--      				<button id="btnUpdate" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">修改</button>--%>
<%--      				<button id="btnExpExcel" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">导出</button>--%>
				</span>
			</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
							<INPUT type="text" class="span12" kind="text" id="currHtid" name="ghh.htid" fieldname="ghh.htid" value="" operation="="/>
							 <button id="btnQuery" class="btn btn-link"  type="button" style="display:none;"><i class="icon-search"></i>查询</button>
						</TD>
					</TR>
				</table>
			</form>
			<div style="height:5px;"> </div>	
			<div class="overFlowX">
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single"  pageNum="5">
	                <thead>
	                	<tr>
	                		<th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
							<th fieldname="NY" colindex=2 tdalign="center" maxlength="10">&nbsp;支付年月&nbsp;</th>
							<th fieldname="COUNTYF" colindex=2 tdalign="center" maxlength="10">&nbsp;明细笔数&nbsp;</th>
							<th fieldname="SUMZFJE" colindex=2 tdalign="right" >&nbsp;支付金额(元)&nbsp;</th>
							<th fieldname="LJDDYZF" colindex=2 tdalign="right" >&nbsp;累计到当月支付金额(元)&nbsp;</th>
<%--							<th fieldname="SUMZFJE" colindex=3 tdalign="right" maxlength="17">&nbsp;支付金额总和(元)&nbsp;</th>--%>
<%--							<th fieldname="SUMYFK" colindex=4 tdalign="right" maxlength="17">&nbsp;进度款总和(元)&nbsp;</th>--%>
<%--							<th fieldname="SUMKKZH" colindex=5 tdalign="right" maxlength="17">&nbsp;扣款总和(元)&nbsp;</th>--%>
	                	</tr>
	                </thead>
	              	<tbody></tbody>
	           </table>
	       </div>
	 	</div>
	 	<div class="B-small-from-table-autoConcise">
			<h4 class="title">合同支付数据-明细
<%--				<span class="pull-right">  --%>
<%--					<button id="btnInsert" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">新增</button>--%>
<%--      				<button id="btnUpdate" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">修改</button>--%>
<%--      				<button id="btnExpExcel" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">导出</button>--%>
<%--				</span>--%>
			</h4>
			<form method="post" id="queryListForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT type="text" class="span12" kind="text" id="numList" fieldname="rownum" value="1000" operation="<="/>
							<INPUT type="text" class="span12" kind="text" id="QZFNF" fieldname="ZFNF" value="" operation="="/>
							<INPUT type="text" class="span12" kind="text" id="QZFYF" fieldname="ZFYF" value="" operation="="/>
							<INPUT type="text" class="span12" kind="text" id="HTID" fieldname="ghh2.id" value="" operation="="/>
						</TD>
					</TR>
				</table>
			</form>
			<div style="height:5px;"> </div>	
			<div class="overFlowX">
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT2" type="single"  pageNum="10">
	                <thead>
	                	<tr>
	                		<th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
<%--	                		<th fieldname="HTBM" colindex=3 tdalign="center" maxlength="100">&nbsp;合同编码&nbsp;</th>--%>
<%--							<th fieldname="HTMC" colindex=4 tdalign="center" maxlength="1000">&nbsp;合同名称&nbsp;</th>--%>
							<th fieldname="XMMC" tdalign="left"   maxlength="40">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDMC" tdalign="left"  maxlength="30" CustomFunction="doBDMC">&nbsp;标段名称&nbsp;</th>
							<th fieldname="ZFJE" colindex=6 tdalign="right" maxlength="17">&nbsp;支付金额(元)&nbsp;</th>
<%--							<th fieldname="YFK" colindex=7 tdalign="right" maxlength="17">&nbsp;进度款(元)&nbsp;</th>--%>
<%--							<th fieldname="KKZH" colindex=13 tdalign="right" maxlength="17">&nbsp;扣款总和(元)&nbsp;</th>--%>
							<th fieldname="ZFRQ" colindex=5 tdalign="center" CustomFunction="doZFRQ">&nbsp;支付日期&nbsp;</th>
<%--							<th fieldname="ZFYT" colindex=19 tdalign="center" maxlength="40">&nbsp;支付用途&nbsp;</th>--%>
<%--							<th fieldname="BZ" colindex=26 tdalign="center" maxlength="20">&nbsp;备注&nbsp;</th>--%>
	                	</tr>
	                </thead>
	              	<tbody></tbody>
	           </table>
	       </div>
	 	</div>
	 	<div style="height:5px;"></div>
	 	<div class="B-small-from-table-autoConcise">
      <h4 class="title">
      	<span class="pull-right">
<%--      		<button id="btnClear_Bins" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>--%>
			<button id="btnDelete" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>删除</button>
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>保存</button>
      	</span>
      </h4>
      
       <%
      	if(htlx.equals("SG")){
      %>
      
        <form method="post" id="hthtzfDetailForm"  >
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
		<input id="YFK" value=0 class="span9" style="width:70%;text-align:right;"  onchange="cal_ZFJE();"  name="YFK" fieldname="YFK" type="number" min="0"/><b>(元)</b>
	</td>
<th width="8%" class="right-border bottom-border text-right">工程预付款</th>
	<td width="17%" class="right-border bottom-border">
		<input id="GCYFK" value=0 class="span9" style="width:70%;text-align:right;"   onchange="cal_ZFJE();"   name="GCYFK" fieldname="GCYFK" type="number" min="0"/><b>(元)</b>
	</td>
</tr>
<tr>
	<th width="8%" class="right-border bottom-border text-right">扣甲供材</th>
	<td width="17%" class="right-border bottom-border">
		<input id="KBLK" value=0 class="span9" style="width:70%;text-align:right;"  name="KBLK" fieldname="KBLK" type="number" min="0"/><b>(元)</b>
	</td>
	<th width="8%" class="right-border bottom-border text-right">扣工程预付款</th>
	<td width="17%" class="right-border bottom-border">
		<input id="KGZYFK" value=0 class="span9" style="width:70%;text-align:right;"  onchange="cal_ZFJE();"  name="KGZYFK" fieldname="KGZYFK" type="number" min="0"/><b>(元)</b>
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
           <form method="post" id="hthtzfDetailForm"  >
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
				<input id="ZFJE" class="span12" keep="true" placeholder="必填" check-type="required" check-type="maxlength" maxlength="17" value=0 style="width:70%;text-align:right;" name="ZFJE" fieldname="ZFJE" type="number" min="0"/><b>(元)</b>
			</td>
			<th width="8%" class="right-border bottom-border text-right">支付日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZFRQ" class="span9" fieldtype="date" fieldformat="YYYY-MM-DD"  placeholder="必填" check-type="required" check-type="maxlength" maxlength="10" name="ZFRQ" fieldname="ZFRQ" type="date" />
			</td>
		</tr>
       <%} %>
       
<%--<tr>--%>
<%--	<th width="8%" class="right-border bottom-border text-right">本次支付后支付比</th>--%>
<%--	<td width="17%" class="right-border bottom-border">--%>
<%--		<input id="BCHTZFBL" value=0 class="span9" style="width:70%;text-align:right;" type="number" min="0" readOnly/><b>%</b>(占合同金额)--%>
<%--	</td>--%>
<%--	<th width="8%" class="right-border bottom-border text-right">本次支付后支付比</th>--%>
<%--	<td width="17%" class="right-border bottom-border">--%>
<%--		<input id="BCWCZFBL" value=0 class="span9" style="width:70%;text-align:right;" type="number" min="0" readOnly/><b>%</b>(占完成投资)--%>
<%--	</td>--%>
<%--</tr>--%>
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
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" fieldname="ghhh.zfnf" id="txtFilter"/>
		<input type="hidden" name="txtFilter" order="desc" fieldname="ghhh.zfyf" id="txtFilter"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
	<FORM name="frmListPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" fieldname="t.LRSJ" id="txtFilter"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="queryResult" id="queryResult"/>
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
<FORM name="frmPostYbf" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
	</FORM>

</div>
</body>
</html>