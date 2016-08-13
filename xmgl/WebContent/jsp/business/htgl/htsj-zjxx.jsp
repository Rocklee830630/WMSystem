<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>合同项目/标段-维护</title>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtController.do";
//页面初始化
$(function() {
	init();

	
    $("#id_choseXM").attr("disabled", true);
    
});
//页面默认参数
function init(){
	var parentIObj = $(this).manhuaDialog.getParentObj();
	var tempJson = parentIObj.$("#ID").val();
	$("#QHTID").val(tempJson);
	//查询合同造价信息
	var data = combineQuery.getQueryCombineData(queryForm,frmPost);	
	var data1 = {
		msg : data
	};
	$.ajax({	
		url : controllername+"?queryHtzjxx",	
			data : data1,	
		cache : false,	
		async :	false,	
		dataType : "json",
		type : 'post',		
		success : function(response) {
			if(response!=null&&response.msg!=null&&response.msg!="0"&&response.msg!=""){
				$("#resultXML").val(response.msg);	
				var resultobj = defaultJson.dealResultJson(response.msg);	
				$("#gcHtglZjxxForm").setFormValues(resultobj);
			}
		}
	});
	
}


</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
     <form method="post" id="queryForm"  style="display:none;">
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<input class="span12" type="text" id="QHTID" name="HTID"  fieldname="ghh.ID" value="" operation="=" >
			</TD>
        </TR>
         <tr>
			<td class="text-left bottom-border text-right">
	        	<button id="btnQuery" class="btn btn-link"  type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-search"></i>查询</button>
<%--	        	<button id="btnClear" class="btn btn-link" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-trash"></i>清空</button>--%>
	        </td>
		</tr>
      </table>
      </form>
    <div style="height:5px;"></div>
    
	</div>
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
     <form method="post" id="gcHtglZjxxForm"  >
      <table class="B-table" width="100%" >
        <tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">拦标价</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input id="CSSDZ" value=0 class="span9" style="width:70%;text-align:right;"  name="CSSDZ" fieldname="CSSDZ" type="number" min="0" disabled/><b>(元)</b>
       	 	</td>
        </tr>
        <tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">中标价</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZZBJ" value=0 class="span9" style="width:70%;text-align:right;"  name="ZZBJ" fieldname="ZZBJ" type="number" min="0" disabled/><b>(元)</b>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">最新合同价</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZZXHTJ" value=0 class="span9" style="width:70%;text-align:right;"  name="ZZXHTJ" fieldname="ZZXHTJ" type="number" min="0" disabled/><b>(元)</b>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">结算价</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZHTJSJ" value=0 class="span9" style="width:70%;text-align:right;"  name="ZHTJSJ" fieldname="ZHTJS" type="number" min="0" disabled/><b>(元)</b>
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
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
<script>
</script>
</html>