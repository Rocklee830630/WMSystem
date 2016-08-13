<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/gcCjjhController.do";
	$(function() {
		var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultEdit").val();
		var odd=convertJson.string2json1(rowValue);
		$("#demoForm").setFormValues(odd);
		//保存按钮
		$("#example1").click(function(){
	 	 if($("#demoForm").validationButton()){
		 		//生成json串
				var data = Form2Json.formToJSON(demoForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//调用ajax插入
		 		doInsertJson_save(controllername + "?saveRoot", data1,null,'addHuiDiao');
			}
	 	 });
   });
//插入
doInsertJson_save = function(actionName, data1,tablistID,callbackFunction) {
  var success  = true;

	var isAsync = false;
	if (callbackFunction != undefined) {
		isAsync = true;
	}
	$.ajax({
		url : actionName,
		data : data1,
		dataType : 'json',
		async :	isAsync,
		type : 'post',   
		success : function(result) {
			$("#resultXML").val(result.msg);
			var prompt = result.prompt;
			if(!prompt){
				prompt =g_prompt[0];
			}
			defaultJson.clearTxtXML();
			success = true;
			if(isAsync==true){
			  eval(callbackFunction+"()");
			}
			},
	    error : function(result) {
			    defaultJson.clearTxtXML();
			    success = false;

		}
	});
	 return success;
};
//回调函数
function addHuiDiao()
{
	var data2 = $("#frmPost").find("#resultXML").val();	
	var fuyemian=$(window).manhuaDialog.getParentObj();
	fuyemian.rootadd(data2,1);
    $(window).manhuaDialog.close();
}
</script>   
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
  <h4 class="title">
  	<span class="pull-right">
		<button id="example1" class="btn"  type="button">保存</button>
    </span> 
  </h4>
 <form method="post"  id="demoForm"  >
      		<table style="width: 100%" class="B-table">
				<!--可以再此处加入hidden域作为过滤条件 -->
				<TR style="display: none;">
					<th >主键</th>
					<td >
						<input type="text" disabled id="GC_CJJH_ID" name="GC_CJJH_ID" fieldname="GC_CJJH_ID">
					</td>
				</TR>
				<tr>
					<th>年度</th>
					<td>
						<input  class="span10" type="text" disabled name="ND" fieldname="ND" id="ND" placeholder="必填" check-type="required">
					</td>
					<th >排序号<span style="font-size:10px;font-weight:normal">（用于数据排序）</span>
					</th>
					<td>
						<input class="span10" type="number" id="PXH" name="PXH" fieldname="PXH" placeholder="必填" check-type="required" style="text-align:right;">
					</td>
				</tr>
				<tr>
					<th>根节点名称</th>
					<td colspan=3>
						<input class="span4" type="text" id="XMMC" name="XMMC" style="width:100%" fieldname="XMMC"  placeholder="必填" check-type="required" fillValue="XMMC">						
					</td>
				</tr>
			</table>
      	  </form>
     	</div>
 	</div>
</div>
<div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		 <!--系统保留定义区域-->
		   <input type="hidden" name="ywid" id = "ywid" value="">
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "XMNF"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>