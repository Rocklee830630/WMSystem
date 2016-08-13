<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<title>合同数据首页</title>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtHtzfController.do";

//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryListForm,frmListPost,DT2);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,DT2);
	});
	
    $(":input").each(function(i){
 	   $(this).attr("disabled", "true");
 	 });
});

//页面默认参数
function init(){
	var parentIObj = $(this).manhuaDialog.getParentObj();
	var tempJson = parentIObj.$("#ID").val();
	if(tempJson!=""){
		g_bAlertWhenNoResult = false;
		$("#currHtid").val(tempJson);
		$("#HTID").val(tempJson);
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryOne",data,DT1);
		g_bAlertWhenNoResult = true;
	}
}

//点击获取行对象
function tr_click(obj,tabListid){
	//alert(JSON.stringify(obj));
	if("DT1"==tabListid){
		$("#ZFNF").val(obj.ZFNF);
		$("#ZFYF").val(obj.ZFYF);
		
		$("#btnQuery").click();
	}else{
		$("#hthtzfDetailForm").setFormValues(obj);
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

function closeParentCloseFunction(){
	init();
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
function doZFYT(obj){
	if(obj.ZFYT==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.ZFYT;
	}
}
function doKKZH(obj){
	if(obj.KKZH==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.KKZH_SV;
	}
}
function doBZ(obj){
	if(obj.BZ==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BZ;
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
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single"  pageNum="5" nopromptmsg="true">
	                <thead>
	                	<tr>
	                		<th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
							<th fieldname="NY" colindex=2 tdalign="center" maxlength="10">&nbsp;支付年月&nbsp;</th>
							<th fieldname="SUMZFJE" colindex=3 tdalign="right" maxlength="17">&nbsp;支付金额总和(元)&nbsp;</th>
							<th fieldname="SUMYFK" colindex=4 tdalign="right" maxlength="17">&nbsp;进度款总和(元)&nbsp;</th>
							<th fieldname="SUMKKZH" colindex=5 tdalign="right" maxlength="17">&nbsp;扣款总和(元)&nbsp;</th>
	                	</tr>
	                </thead>
	              	<tbody></tbody>
	           </table>
	       </div>
	 	</div>
	 	<div class="B-small-from-table-autoConcise">
			<h4 class="title">合同支付数据-明细
			</h4>
			<form method="post" id="queryListForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT type="text" class="span12" kind="text" id="numList" fieldname="rownum" value="1000" operation="<="/>
							<INPUT type="text" class="span12" kind="text" id="ZFNF" fieldname="ZFNF" value="" operation="="/>
							<INPUT type="text" class="span12" kind="text" id="ZFYF" fieldname="ZFYF" value="" operation="="/>
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
	                		<th fieldname="HTBM" colindex=3 tdalign="center" maxlength="100">&nbsp;合同编码&nbsp;</th>
<%--							<th fieldname="HTMC" colindex=4 tdalign="center" maxlength="1000">&nbsp;合同名称&nbsp;</th>--%>
							<th fieldname="XMMC"  maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDMC"  maxlength="15" CustomFunction="doBDMC">&nbsp;标段名称&nbsp;</th>
							<th fieldname="ZFJE" colindex=6 tdalign="right" maxlength="17">&nbsp;支付金额(元)&nbsp;</th>
							<th fieldname="YFK" colindex=7 tdalign="right" maxlength="17">&nbsp;进度款(元)&nbsp;</th>
							<th fieldname="KKZH" colindex=13 tdalign="right" maxlength="17" CustomFunction="doKKZH">&nbsp;扣款总和(元)&nbsp;</th>
							<th fieldname="ZFRQ" colindex=5 tdalign="center" CustomFunction="doZFRQ">&nbsp;支付日期&nbsp;</th>
							<th fieldname="ZFYT" colindex=19 tdalign="center" maxlength="40" CustomFunction="doZFYT">&nbsp;支付用途&nbsp;</th>
							<th fieldname="BZ" colindex=26 tdalign="center" maxlength="20" CustomFunction="doBZ">&nbsp;备注&nbsp;</th>
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
      	</span>
      </h4>
     <form method="post" id="hthtzfDetailForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
	  	<input type="hidden" id="GC_TCJH_XMXDK_ID" fieldname="XMID" name = "XMID"/></TD>
	  	<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">合同名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="HTMC" type="text"  fieldname="HTMC" name = "HTMC"  disabled />
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
         		<input class="span12" style="width:85%" id="XMMC" type="text"  fieldname="XMMC" name = "XMMC"  disabled />
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
       		<td class="bottom-border right-border"width="15%">
         		<input class="span12" style="width:100%" id="BDMC" type="text" fieldname="BDMC" name = "BDMC"  disabled/>
         	</td>
        </tr>
        
        <tr>
<tr>
	<th width="8%" class="right-border bottom-border text-right disabledTh">支付年份</th>
	<td width="17%" class="right-border bottom-border">
		<select  id="ZFNF"    class="span6"  name="ZFNF" fieldname="ZFNF"  operation="=" kind="dic" src="XMNF"  defaultMemo="-支付年份-">
	</td>
	<th width="8%" class="right-border bottom-border text-right disabledTh">支付月份</th>
	<td width="17%" class="right-border bottom-border">
		<select  id="ZFYF"    class="span6"  check-type="maxlength" maxlength="2" name="ZFYF" fieldname="ZFYF"  operation="=" kind="dic" src="YF"  defaultMemo="-支付月份-">
	</td>
</tr>
<tr>
	<th width="8%" class="right-border bottom-border text-right disabledTh">进度款</th>
	<td width="17%" class="right-border bottom-border">
		<input id="YFK" value=0 class="span9" style="width:70%;text-align:right;"  name="YFK" fieldname="YFK" type="number" /><b>(元)</b>
	</td>
	<th width="8%" class="right-border bottom-border text-right disabledTh">本次完成工作量</th>
	<td width="17%" class="right-border bottom-border">
		<input id="BCWCGZL" value=0 class="span9" style="width:70%;text-align:right;"  name="BCWCGZL" fieldname="BCWCGZL" type="number" />
	</td>
</tr>
<tr>
	<th width="8%" class="right-border bottom-border text-right disabledTh">备料预付款</th>
	<td width="17%" class="right-border bottom-border">
		<input id="BLYFK" value=0 class="span9" style="width:70%;text-align:right;"  name="BLYFK" fieldname="BLYFK" type="number" /><b>(元)</b>
	</td>
	<th width="8%" class="right-border bottom-border text-right disabledTh">工程预付款</th>
	<td width="17%" class="right-border bottom-border">
		<input id="GCYFK" value=0 class="span9" style="width:70%;text-align:right;"  name="GCYFK" fieldname="GCYFK" type="number" /><b>(元)</b>
	</td>
</tr>
<tr>
	<th width="8%" class="right-border bottom-border text-right disabledTh">付其他款</th>
	<td width="17%" class="right-border bottom-border">
		<input id="FQTK" value=0 class="span9" style="width:70%;text-align:right;"  name="FQTK" fieldname="FQTK" type="number" /><b>(元)</b>
	</td>
	<th width="8%" class="right-border bottom-border text-right disabledTh">返回保修金</th>
	<td width="17%" class="right-border bottom-border">
		<input id="FHBXJ" value=0 class="span9" style="width:70%;text-align:right;"  name="FHBXJ" fieldname="FHBXJ" type="number" /><b>(元)</b>
	</td>
</tr>
<tr>
	<th width="8%" class="right-border bottom-border text-right disabledTh">扣款总和</th>
	<td width="17%" class="right-border bottom-border">
		<input id="KKZH" value=0 class="span9" style="width:70%;text-align:right;"  name="KKZH" fieldname="KKZH" type="number" /><b>(元)</b>
	</td>
	<th width="8%" class="right-border bottom-border text-right disabledTh">扣备料款</th>
	<td width="17%" class="right-border bottom-border">
		<input id="KBLK" value=0 class="span9" style="width:70%;text-align:right;"  name="KBLK" fieldname="KBLK" type="number" /><b>(元)</b>
	</td>
</tr>
<tr>
	<th width="8%" class="right-border bottom-border text-right disabledTh">扣工程预付款</th>
	<td width="17%" class="right-border bottom-border">
		<input id="KGZYFK" value=0 class="span9" style="width:70%;text-align:right;"  name="KGZYFK" fieldname="KGZYFK" type="number" /><b>(元)</b>
	</td>
	<th width="8%" class="right-border bottom-border text-right disabledTh">扣其他款</th>
	<td width="17%" class="right-border bottom-border">
		<input id="KQTK" value=0 class="span9" style="width:70%;text-align:right;"  name="KQTK" fieldname="KQTK" type="number" /><b>(元)</b>
	</td>
</tr>
<tr>
	<th width="8%" class="right-border bottom-border text-right disabledTh">扣保修金</th>
	<td width="17%" class="right-border bottom-border">
		<input id="KBXJ" value=0 class="span9" style="width:70%;text-align:right;"  name="KBXJ" fieldname="KBXJ" type="number" /><b>(元)</b>
	</td>
<th width="8%" class="right-border bottom-border text-right disabledTh">支付金额</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZFJE" class="span12" keep="true"  check-type="maxlength" maxlength="17" value=0 style="width:70%;text-align:right;" name="ZFJE" fieldname="ZFJE" type="number" /><b>(元)</b>
			</td>
</tr>
        
        <tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">支付日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZFRQ" class="span9" fieldtype="date" fieldformat="YYYY-MM-DD"   check-type="maxlength" maxlength="10" name="ZFRQ" fieldname="ZFRQ" type="date" />
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
</div>
</body>
</html>