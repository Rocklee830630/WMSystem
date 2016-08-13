<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>合同变更-维护</title>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtHtbgController.do";
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtHtbgList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,gcHtglHtHtbgList);
	});
	$("#btnQueryGroup").click(function() {
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtHtbgList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryOne",data,gcHtglHtHtbgList, null, true);
	});
	$("#btnInsert").click(function() {
		$(window).manhuaDialog({"title":"合同变更>新增","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtbg-addOne.jsp?type=insert","modal":"2"});
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#gcHtglHtHtbgForm").validationButton())
		{
		    //生成json串
		    var data = Form2Json.formToJSON(gcHtglHtHtbgForm);
		  	//组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  	//调用ajax插入
    		defaultJson.doUpdateJson(controllername + "?update", data1,gcHtglHtHtbgList);
		}else{
			requireFormMsg();
		  	return;
		}
	});
	
	//按钮绑定事件（新增）
    $("#btnClear_Bins").click(function() {
        $("#gcHtglHtHtbgForm").clearFormResult();
        $("#gcHtglHtHtbgForm").cancleSelected();
        
        
        $("#ZFRQ").val(new Date().toLocaleDateString());
        $("#ZFJE").val(0);
        $("#ID").val("");
    });
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
	
});
//页面默认参数
function init(){
	var parentIObj = $(this).manhuaDialog.getParentObj();
	var tempJson = parentIObj.$("#resultXML").val();
	//var tempJson = parent.$("body").find("#ID").val();
	if(tempJson!=""){
		$("#currHtid").val(convertJson.string2json1(tempJson).ID);
		$("#HTID").val(convertJson.string2json1(tempJson).ID);
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtHtbgList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryOne",data,gcHtglHtHtbgList, null, true);
	}
}


//点击行事件
function tr_click(obj){
	//alert(JSON.stringify(obj));
	$("#gcHtglHtHtbgForm").setFormValues(obj);
	$("#btnSave").attr("disabled", false);
}

//选中项目名称弹出页
function selectXm(){
	$(window).manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/xmcx.jsp","modal":"2"});
}
//弹出区域回调
getWinData = function(data){
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#XMBH").val(JSON.parse(data).XMBH);
	$("#GC_TCJH_XMXDK_ID").val(JSON.parse(data).GC_TCJH_XMXDK_ID);
};

//详细信息
function rowView(index){
	var obj = $("#gcHtglHtHtbgList").getSelectedRowJsonByIndex(index);
	var xmbh = eval("("+obj+")").XMBH;
	$(window).manhuaDialog(xmscUrl(xmbh));
}

function doHTBM(obj){
	if(obj.HTBM==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.HTBM;
	}
}
function doBDMC(obj){
	if(obj.BDMC==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}
function doBGLY(obj){
	if(obj.BGLY==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BGLY;
	}
}
function queryBgUpdate(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtHtbgList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryOne",data,gcHtglHtHtbgList, null, true);
}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
    <h4 class="title">合同变更-明细
		<span class="pull-right">  
			<button id="btnInsert" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">新增</button>
		</span>
	</h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
			<TD class="right-border bottom-border">
				<INPUT type="text" class="span12" kind="text" id="currHtid" name="ghh.id" fieldname="ghh.id" value="" operation="="/>
			</TD>
        </TR>
      </table>
      </form>
    <div style="height:5px;"></div>
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="gcHtglHtHtbgList" width="100%" type="single" pageNum="5" nopromptmsg="true">
		<thead>
		    <tr>
            	<th  name="XH" id="_XH" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
				<th fieldname="HTBM" colindex=3 tdalign="center" maxlength="30" rowMerge="true" CustomFunction="doHTBM">&nbsp;合同编码&nbsp;</th>
<%--				<th fieldname="HTMC" colindex=4 tdalign="center" maxlength="30" >&nbsp;合同名称&nbsp;</th>--%>
<%--				<th fieldname="HTLX" colindex=1 tdalign="center" maxlength="30" >&nbsp;合同类型&nbsp;</th>--%>
<%--				<th fieldname="HTZT" colindex=6 tdalign="center" maxlength="30" >&nbsp;合同状态&nbsp;</th>--%>
				<th fieldname="BGLX" colindex=2 tdalign="center" rowMerge="true">&nbsp;变更类型&nbsp;</th>
				<th fieldname="BGMC" colindex=3 tdalign="center" maxlength="100" >&nbsp;变更名称&nbsp;</th>
				<th fieldname="BGJE" colindex=4 tdalign="right" maxlength="17">&nbsp;变更金额<b>(元)</b>&nbsp;</th>
				<th fieldname="BGTS" colindex=5 tdalign="center" maxlength="22">&nbsp;变更天数&nbsp;</th>
				<th fieldname="BGRQ" colindex=6 tdalign="center">&nbsp;变更日期&nbsp;</th>
				<th fieldname="BGLY" colindex=7 tdalign="center" maxlength="40" CustomFunction="doBGLY">&nbsp;变更理由&nbsp;</th>
				<th fieldname="XMMC" colindex=6 maxlength="40" tdalign="center">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDMC" colindex=7 maxlength="40" tdalign="center" CustomFunction="doBDMC">&nbsp;标段名称&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	</div>
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">合同变更
      	<span class="pull-right">
<%--      		<button id="btnClear_Bins" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>--%>
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">保存</button>
      	</span>
      </h4>
     <form method="post" id="gcHtglHtHtbgForm"  >
      <table class="B-table" width="100%" >
      <input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
	  	<input type="hidden" id="HTSJID" name="HTSJID"  fieldname="HTSJID" >
        <tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">合同名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="HTMC" type="text" fieldname="HTMC" name = "HTMC"  disabled />
<%--          		<button class="btn btn-link"  type="button" onclick="selectHt()"><i class="icon-edit"></i></button>--%>
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">合同编码</th>
       		<td class="bottom-border right-border"width="15%">
         		<input class="span12" style="width:100%" id="HTBM" type="text" fieldname="HTBM" name = "HTBM"  disabled/>
         	</td>
        </tr>
        <tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="XMMC" type="text" fieldname="XMMC" name = "XMMC"  disabled />
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
			<td width="25%" class="right-border bottom-border">
				<input id="BDMC" class="span12" name="BDMC" fieldname="BDMC" type="text" disabled/>
			</td>
        </tr>
		
		<tr>
			<th width="8%" class="right-border bottom-border text-right">变更类型</th>
			<td width="17%" class="right-border bottom-border">
				<select  id="BGLX"   placeholder="必填" check-type="required" class="span6"  name="BGLX" fieldname="BGLX"  operation="=" kind="dic" src="BGLX"  defaultMemo="-变更类型-">
			</td>
			<th width="8%" class="right-border bottom-border text-right">变更名称</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BGMC"   class="span12" check-type="maxlength" maxlength="100"  name="BGMC" fieldname="BGMC" type="text" />
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">变更金额</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BGJE" value=0 class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="BGJE" fieldname="BGJE" type="number" /><b>(元)</b>
			</td>
			<th width="8%" class="right-border bottom-border text-right">变更天数</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BGTS" value=0 class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="BGTS" fieldname="BGTS" type="number" />
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">变更日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BGRQ"  class="span9" fieldtype="date" fieldformat="YYYY-MM-DD"  placeholder="必填" check-type="required"  name="BGRQ" fieldname="BGRQ" type="date" />
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">变更理由</th>
			<td colspan="3" class="bottom-border right-border" >
				<textarea class="span12" rows="2" id="BGLY" check-type="maxlength" maxlength="4000" fieldname="BGLY" name="BGLY"></textarea>
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