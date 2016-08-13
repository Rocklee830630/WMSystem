<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%
	String hasHt = request.getParameter("hasHt");
	String fromPage = request.getParameter("fromPage");
	String htid = "";
	if(request.getParameter("htid")!=null){
		htid = request.getParameter("htid");
	}
%>
<app:base/>
<title>合同完成投资-维护</title>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtWctzController.do";
var htid = '<%=htid%>';
var hasHt = '<%=hasHt%>';
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryListForm,frmPost,DT2);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,DT2);
	});
	$("#btnQueryGroup").click(function() {
		var data = combineQuery.getQueryCombineData(queryForm,frmPost_group,gcHtglHtWctzList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryOne",data,gcHtglHtWctzList);
	});
	
	$("#btnInsert").click(function() {
		$(window).manhuaDialog({"title":"合同完成投资>新增","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtwctz-addOne.jsp?type=insert&hasHt="+hasHt,"modal":"2"});
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#gcHtglHtWctzForm").validationButton())
		{
		  //生成json串
		    var data = Form2Json.formToJSON(gcHtglHtWctzForm);
		  //组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  //调用ajax插入
    		defaultJson.doUpdateJson(controllername + "?update", data1,DT2);
		}else{
			requireFormMsg();
		  	return;
		}
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
	
});
<%if("htindex".equals(fromPage)){
%>
function closeNowCloseFunction(){
	var fuyemian1=$(window).manhuaDialog.getParentObj();
	fuyemian1.gengxinchaxun('update');
}
<%
}%>
<%if("tqlbmmxadd".equals(fromPage)){//父页面为提请款部门明细添加
	%>
	function closeNowCloseFunction(){
		var fuyemian1=$(window).manhuaDialog.getParentObj();
		fuyemian1.gengxintz();
	}
<%
}%>

//页面默认参数
function init(){
	var parentIObj = $(this).manhuaDialog.getParentObj();
	var tempJson = '';
	if(htid!=''){
		tempJson = htid;
	}else{
		tempJson = parentIObj.$("#ID").val();
	}
	//var tempJson = parent.$("body").find("#ID").val();
	if(tempJson!=""){
		$("#currHtid").val(tempJson);
		$("#HTID").val(tempJson);
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost_group,gcHtglHtWctzList);
		//调用ajax插入
		var flag = defaultJson.doQueryJsonList(controllername+"?queryOne",data,gcHtglHtWctzList, null, true);
		if(flag){
			if($("#gcHtglHtWctzList tbody tr").length==0){
	        	return
			}else{
				$("#gcHtglHtWctzList").setSelect(0);
				var obj = $("#gcHtglHtWctzList").getSelectedRowJsonObj();
				$("#ZFNF").val(obj.NF);
				$("#ZFYF").val(obj.YF);
				
				var data1 = combineQuery.getQueryCombineData(queryListForm,frmPost,DT2);
				//调用ajax插入
				var flag1 = defaultJson.doQueryJsonList(controllername+"?query",data1,DT2, null, true);
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


//点击行事件
function tr_click(obj,tabListid){
	//alert(JSON.stringify(obj));
	if("gcHtglHtWctzList"==tabListid){
		$("#ZFNF").val(obj.NF);
		$("#ZFYF").val(obj.YF);
		
		$("#btnQuery").click();
	}else{
		$("#gcHtglHtWctzForm").setFormValues(obj);
		$("#btnSave").attr("disabled", false);
	}
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
	var obj = $("#gcHtglHtWctzList").getSelectedRowJsonByIndex(index);
	var xmbh = eval("("+obj+")").XMBH;
	$(window).manhuaDialog(xmscUrl(xmbh));
}

function closeParentCloseFunction(msgc){
	if (typeof(msgc) == "undefined") {
	}else if(msgc=="showMsg"){
		xSuccessMsg('操作成功！',null);
		init();
	}
}
function doBDMC(obj){
	if(obj.BDMC==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}
function doNF(obj){
	if(obj.NF==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.NF;
	}
}
function doYF(obj){
	if(obj.YF==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.YF;
	}
}
function doBZ(obj){
	if(obj.BZ==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BZ;
	}
}
function doHTBM(obj){
	if(obj.HTBM==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.HTBM;
	}
}
function doNY(obj){
	if(obj.NY==''||obj.NY=='-'){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.NY;
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
					<button id="btnInsert" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">新增</button>
				</span>
			</h4>
		     <form method="post" id="queryForm"  >
		      <table class="B-table" width="100%">
		      <!--可以再此处加入hidden域作为过滤条件 -->
		      	<TR  style="display:none;">
					<TD class="right-border bottom-border">
						<INPUT type="text" class="span12" kind="text" id="currHtid" name="ghh.htid" fieldname="ghh.htid" value="" operation="="/>
					</TD>
		        </TR>
		         <tr style="display:none;">
					<td class="text-left bottom-border text-right">
			        	<button id="btnQuery" class="btn btn-link"  type="button" ><i class="icon-search"></i>查询</button>
			        </td>
				</tr>
		      </table>
		      </form>
    		<div style="height:5px;"></div>
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="gcHtglHtWctzList" width="100%" type="single" pageNum="5" nopromptmsg="true">
		<thead>
			<tr>
				<th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
				<th fieldname="NY" colindex=2 tdalign="center" maxlength="10" CustomFunction="doNY">&nbsp;支付年月&nbsp;</th>
				<th fieldname="NF" colindex=2 tdalign="center" CustomFunction="doNF">&nbsp;年份&nbsp;</th>
				<th fieldname="YF" colindex=3 tdalign="center" CustomFunction="doYF">&nbsp;月份&nbsp;</th>
				<th fieldname="SUMWCZJE" colindex=4 tdalign="center" >&nbsp;完成投资金额&nbsp;</th>
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
	                		<th fieldname="HTBM" colindex=3 tdalign="center" maxlength="100" CustomFunction="doHTBM">&nbsp;合同编码&nbsp;</th>
							<th fieldname="HTMC" colindex=4 tdalign="center" maxlength="1000">&nbsp;合同名称&nbsp;</th>
							<th fieldname="NF" colindex=2 tdalign="center" CustomFunction="doNF">&nbsp;年份&nbsp;</th>
							<th fieldname="YF" colindex=3 tdalign="center" CustomFunction="doYF">&nbsp;月份&nbsp;</th>
							<th fieldname="XMMC"  maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDMC"  maxlength="15" CustomFunction="doBDMC">&nbsp;标段名称&nbsp;</th>
							<th fieldname="WCTZJE" colindex=6 tdalign="center" maxlength="17">&nbsp;完成金额&nbsp;</th>
							<th fieldname="BZ" colindex=26 tdalign="center" maxlength="20" CustomFunction="doBZ">&nbsp;备注&nbsp;</th>
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
<%--      		<button id="btnClear_Bins" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>--%>
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">保存</button>
      	</span>
      </h4>
     <form method="post" id="gcHtglHtWctzForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
	  	<input type="hidden" id="HTSJID" name="HTSJID"  fieldname="HTSJID" >
        <tr>
			<th width="8%" class="right-border bottom-border text-right">合同名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="HTMC" type="text" placeholder="必填" check-type="required" fieldname="HTMC" name = "HTMC"  disabled />
<%--          		<button class="btn btn-link"  type="button" onclick="selectHt()"><i class="icon-edit"></i></button>--%>
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right">合同编码</th>
       		<td class="bottom-border right-border"width="15%">
         		<input class="span12" style="width:100%" id="HTBM" type="text" fieldname="HTBM" name = "HTBM"  disabled/>
         	</td>
        </tr>
        <tr>
			<th width="8%" class="right-border bottom-border text-right">项目名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="XMMC" type="text" fieldname="XMMC" name = "XMMC"  disabled />
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right">标段名称</th>
			<td width="25%" class="right-border bottom-border">
				<input id="BDMC" class="span12" name="BDMC" fieldname="BDMC" type="text" disabled/>
			</td>
        </tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">支付年份</th>
			<td width="17%" class="right-border bottom-border">
				<select  id="NF"   placeholder="必填" check-type="required" class="span6"  name="NF" fieldname="NF"  operation="=" kind="dic" src="XMNF"  defaultMemo="-支付年份-">
			</td>
			<th width="8%" class="right-border bottom-border text-right">支付月份</th>
			<td width="17%" class="right-border bottom-border">
				<select  id="YF"   placeholder="必填" check-type="required" class="span6"  check-type="maxlength" maxlength="2" name="YF" fieldname="YF"  operation="=" kind="dic" src="YF"  defaultMemo="-支付月份-">
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">完成投资金额</th>
			<td width="17%" class="right-border bottom-border">
				<input id="WCTZJE" value=0 class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="WCTZJE" fieldname="WCTZJE" type="number" />
			</td>
			<th width="8%" class="right-border bottom-border text-right">完成投资类型</th>
			<td width="17%" class="right-border bottom-border">
				<select  id="WCTZLX" class="span6"  name="WCTZLX" fieldname="WCTZLX"  operation="=" kind="dic" src="ZFYT"  defaultMemo="-支付用途-">
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
 	<FORM name="frmPost_group" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "ny" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
 	</FORM>
 	
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
<%--         <input type="hidden" name="txtFilter"  order="desc" fieldname = "ny" id = "txtFilter">--%>
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
 	</FORM>
 </div>
</body>
<script>
</script>
</html>