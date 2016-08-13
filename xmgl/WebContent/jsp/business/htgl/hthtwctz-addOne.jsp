<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>合同完成投资-维护</title>
<%
	String type=request.getParameter("type");
	String hasHt=request.getParameter("hasHt");
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtWctzController.do";
var controllernameHtsj= "${pageContext.request.contextPath }/htgl/gcHtglHtsjController.do";
var type ="<%=type%>";
var retMsg="";
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtWctzList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,gcHtglHtWctzList);
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#gcHtglHtWctzForm").validationButton())
		{
			 //生成json串
		    var data = Form2Json.formToJSON(gcHtglHtWctzForm);
		    var data1 = defaultJson.packSaveJson(data);
		 	//调用ajax插入
		 	var flag = defaultJson.doInsertJson(controllername + "?insert", data1);
		 	if(flag){
		 		//var parentmain=$(window).manhuaDialog.getParentObj();
				//parentmain.$("#btnQueryGroup").click();
				
				retMsg = "showMsg";
	    		$(window).manhuaDialog.close();
		 	}
			
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
	if(type == "insert"){
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
		}
	}else if(type == "update" || type == "detail"){
		var tempJson;
		if(navigator.userAgent.indexOf("Firefox")>0) {
			var rowValue = $(parent.frames["menuiframe"]).contents().find("#resultXML").val();
			tempJson = eval("("+rowValue+")");
		}else{
			var rowValue = $(parent.frames["menuiframe"].document).find("#resultXML").val();
			tempJson = eval("("+rowValue+")");
		}
		//表单赋值
		$("#gcHtglHtWctzForm").setFormValues(tempJson);
	}
}


//点击行事件
function tr_click(obj){
	//alert(JSON.stringify(obj));
	$("#gcHtglHtWctzForm").setFormValues(obj);
}

//选中项目名称弹出页
function selectHt(){
	$(window).manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath}/jsp/business/htgl/htcx.jsp","modal":"2"});
}
//弹出区域回调
getWinData = function(data){
	$("#HTSJID").val(JSON.parse(data).HTSJID);
	$("#HTMC").val(JSON.parse(data).HTMC);
	$("#HTBM").val(JSON.parse(data).HTBM);
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#BDMC").val(JSON.parse(data).BDMC);
	$("#BGMC").val(JSON.parse(data).BDMC);
};

//详细信息
function rowView(index){
	var obj = $("#gcHtglHtWctzList").getSelectedRowJsonByIndex(index);
	var xmbh = eval("("+obj+")").XMBH;
	$(window).manhuaDialog(xmscUrl(xmbh));
}

function closeNowCloseFunction(){
	return retMsg;
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
 			    <th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
           		<th fieldname="HTBM" colindex=3 tdalign="center" maxlength="40">&nbsp;合同编码&nbsp;</th>
				<th fieldname="HTMC" colindex=4 tdalign="center" maxlength="50">&nbsp;合同名称&nbsp;</th>
				<th fieldname="HTLX" colindex=1 tdalign="center" maxlength="30" >&nbsp;合同类型&nbsp;</th>
				<th fieldname="HTZT" colindex=6 tdalign="center" maxlength="30" >&nbsp;合同状态&nbsp;</th>
				<th fieldname="FBFS" colindex=8 tdalign="center" maxlength="30" >&nbsp;发包方式&nbsp;</th>
				<th fieldname="GCJGBFB" colindex=6 tdalign="center" >&nbsp;价格百分比&nbsp;</th>
				<th fieldname="XMMC"  maxlength="70">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDMC"  maxlength="70">&nbsp;标段名称&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	</div>
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">合同完成投资
      	<span class="pull-right">
      		<button id="btnClear_Bins" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
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
          		<%if(!"one".equals(hasHt)){ %><button class="btn btn-link"  type="button" onclick="selectHt()"><i class="icon-edit"></i></button><%} %>
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
			<th width="8%" class="right-border bottom-border text-right">完成投资类型)</th>
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
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "ghh2.LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
<script>
</script>
</html>