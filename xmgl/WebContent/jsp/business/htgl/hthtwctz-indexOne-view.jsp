<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>合同完成投资-维护</title>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtWctzController.do";
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryListForm,frmPost,gcHtglHtWctzList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,DT2);
	});
	
	//按钮绑定事件（新增）
    $("#btnClear_Bins").click(function() {
        $("#gcHtglHtWctzForm").clearFormResult();
        $("#gcHtglHtWctzForm").cancleSelected();
        
        
        $("#ZFRQ").val(new Date().toLocaleDateString());
        $("#ZFJE").val(0);
        $("#ID").val("");
    });
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
	
    $(":input").each(function(i){
 	   $(this).attr("disabled", "true");
 	 });
});
//页面默认参数
function init(){
	var parentIObj = $(this).manhuaDialog.getParentObj();
	var tempJson = parentIObj.$("#ID").val();
	//var tempJson = parent.$("body").find("#ID").val();
	if(tempJson!=""){
		g_bAlertWhenNoResult = false;
		$("#currHtid").val(tempJson);
		$("#HTID").val(tempJson);
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtWctzList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryOne",data,gcHtglHtWctzList);
		g_bAlertWhenNoResult = true;
	}
}


//点击行事件
function tr_click(obj,tabListid){
	//alert(JSON.stringify(obj));
	if("gcHtglHtWctzList"==tabListid){
		$("#ZFNF").val(obj.NF);
		$("#ZFYF").val(obj.YF);
		
		$("#btnQuery").click();
	}else{
		$("#gcHtglHtWctzForm").setFormValues(obj);
	}
}

//弹出区域回调
getWinData = function(data){
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#XMBH").val(JSON.parse(data).XMBH);
	$("#GC_TCJH_XMXDK_ID").val(JSON.parse(data).GC_TCJH_XMXDK_ID);
};

function doBDMC(obj){
	if(obj.BDMC==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
    		<h4 class="title">合同完成投资数据
				<span class="pull-right">  
				</span>
			</h4>
		     <form method="post" id="queryForm"  >
		      <table class="B-table" width="100%">
		      <!--可以再此处加入hidden域作为过滤条件 -->
		      	<TR  style="display:none;">
					<TD class="right-border bottom-border">
						<INPUT type="text" class="span12" kind="text" id="currHtid" name="ghh.htid" fieldname="ghh.htid" value="" operation="="/>
						<button id="btnQuery" class="btn btn-link"  type="button" style="display:none;"><i class="icon-search"></i>查询</button>
					</TD>
		        </TR>
		      </table>
		      </form>
    		<div style="height:5px;"></div>
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="gcHtglHtWctzList" width="100%" type="single" pageNum="5" nopromptmsg="true">
		<thead>
			<tr>
				<th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
				<th fieldname="NY" colindex=2 tdalign="center" maxlength="10">&nbsp;支付年月&nbsp;</th>
				<th fieldname="NF" colindex=2 tdalign="center" >&nbsp;年份&nbsp;</th>
				<th fieldname="YF" colindex=3 tdalign="center" >&nbsp;月份&nbsp;</th>
				<th fieldname="SUMWCZJE" colindex=4 tdalign="right" >&nbsp;完成投资金额(元)&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	</div>
	<div class="B-small-from-table-autoConcise">
			<h4 class="title">合同完成投资-明细
			</h4>
			<form method="post" id="queryListForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT type="text" class="span12" kind="text" id="numList" fieldname="rownum" value="1000" operation="<="/>
							<INPUT type="text" class="span12" kind="text" id="ZFNF" fieldname="NF" value="" operation="="/>
							<INPUT type="text" class="span12" kind="text" id="ZFYF" fieldname="YF" value="" operation="="/>
							<INPUT type="text" class="span12" kind="text" id="HTID" fieldname="ghh2.htid" value="" operation="="/>
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
	                		<th fieldname="HTBM" colindex=3 tdalign="center" maxlength="100">&nbsp;合同编码&nbsp;</th>
							<th fieldname="NF" colindex=2 tdalign="center" >&nbsp;年份&nbsp;</th>
							<th fieldname="YF" colindex=3 tdalign="center" >&nbsp;月份&nbsp;</th>
							<th fieldname="XMMC"  maxlength="40" tdalign="center">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDMC"  maxlength="30" tdalign="center" CustomFunction="doBDMC">&nbsp;标段名称&nbsp;</th>
							<th fieldname="WCTZJE" colindex=6 tdalign="right" maxlength="17">&nbsp;完成金额(元)&nbsp;</th>
							<th fieldname="BZ" colindex=26 tdalign="center" maxlength="20">&nbsp;备注&nbsp;</th>
	                	</tr>
	                </thead>
	              	<tbody></tbody>
	           </table>
	       </div>
	 	</div>
	
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">合同完成投资
      	<span class="pull-right">
      	</span>
      </h4>
     <form method="post" id="gcHtglHtWctzForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
	  	<input type="hidden" id="HTSJID" name="HTSJID"  fieldname="HTSJID" >
        <tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">合同名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="HTMC" type="text"  fieldname="HTMC" name = "HTMC"  disabled />
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
			<th width="8%" class="right-border bottom-border text-right disabledTh">支付年份</th>
			<td width="17%" class="right-border bottom-border">
				<select  id="NF"    class="span6"  name="NF" fieldname="NF"  operation="=" kind="dic" src="XMNF"  defaultMemo="-支付年份-">
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">支付月份</th>
			<td width="17%" class="right-border bottom-border">
				<select  id="YF"    class="span6"  check-type="maxlength" maxlength="2" name="YF" fieldname="YF"  operation="=" kind="dic" src="YF"  defaultMemo="-支付月份-">
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">完成投资金额</th>
			<td width="17%" class="right-border bottom-border">
				<input id="WCTZJE" value=0 class="span9" style="width:70%;text-align:right;"  min="0" name="WCTZJE" fieldname="WCTZJE" type="number" /><b>(元)</b>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">完成投资类型</th>
			<td width="17%" class="right-border bottom-border">
				<select  id="WCTZLX" class="span6"  name="WCTZLX" fieldname="WCTZLX"  operation="=" kind="dic" src="ZFYT"  defaultMemo="-支付用途-">
			</td>
		</tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right disabledTh">备注</th>
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
<%--         <input type="hidden" name="txtFilter"  order="desc" fieldname = "ghhw.LRSJ" id = "txtFilter">--%>
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
<script>
</script>
</html>