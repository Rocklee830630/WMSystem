<!DOCTYPE html>
<html>
<head>
<%@ page import="com.ccthanking.framework.common.User" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<script type="text/javascript" charset="UTF-8">

var controllername= "${pageContext.request.contextPath }/jhtzController.do";
//初始化加载
$(document).ready(function(){
	var year=new Date().getFullYear();
	var num=document.getElementsByTagName("option");
	var year_num=year-2006;
	document.getElementById('JHTZ_ND').options.length=year_num+1;
	for(var i=0;i<year_num+1;i++)
	{
		num[i].id=i;
		num[i].innerHTML=parseInt(2007)+i;
	}
	document.getElementById('JHTZ_ND').value=num[i-2].innerHTML;
	init();
});

function init() {
	var nd = $("#JHTZ_ND").val();
    $.ajax({
    	url:		controllername+"?queryOne&nd="+nd,
    	data:		"",
    	dataType:	"json",
    	async:		false,
    	success:function(rs) {
    	//	alert(JSON.stringify(rs));
    		$("#demoForm").setFormValues(rs);
    	}
    });
}

//点击保存按钮
$(function() {
	var saveUserInfoBtn = $("#saveInfo");
	saveUserInfoBtn.click(function() {
		if($("#demoForm").validationButton()) {
			//生成json串
			var data = Form2Json.formToJSON(demoForm);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			//通过判断id是否为空来判断是插入还是修改
			
			var success = defaultJson.doInsertJson(controllername + "?saveOrUpdate", data1, null, null);
		} else {
			requireFormMsg();
		  	return;
		}
		
		if(success) {
			init();
		} else {
			alert();
		}
	});
	
	$("#JHTZ_ND").change(function() {
		var nd = $("#JHTZ_ND").val();
		$.ajax({
	    	url:		controllername+"?queryOne&nd="+nd,
	    	data:		"",
	    	dataType:	"json",
	    	async:		false,
	    	success:function(rs) {
	    	//	alert(JSON.stringify(rs));
	    		$("#demoForm").setFormValues(rs);
	    	}
	    });
   	}); 
});



</script>    
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">
      		<h4 class="title">计划调整
				<span class="pull-right">
					<button id="saveInfo" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
				</span>
      		</h4>
     		<form method="post" id="demoForm"  >
      			<table class="B-table" width="100%" id="DT1">
					<tr>
						<th width="5%" class="right-border bottom-border text-right">年度</th>
						<td width="8%" class="right-border bottom-border">
							<select id="JHTZ_ND" class="span12 year" type="text" placeholder="必填" check-type="required" name="JHTZ_ND" maxlength="4" fieldname="JHTZ_ND"></select>
						</td>
						
						<td class="right-border bottom-border" colspan="7"></td>
					</tr>
					
					<tr>
						<th class="right-border bottom-border text-right">内容1</th>
						<td class="right-border bottom-border" colspan="7">
							<textarea class="span12" rows="4" id="JHTZ_NR1" check-type="required maxlength" maxlength="3000" fieldname="JHTZ_NR1" name="JHTZ_NR1"></textarea></td>
					</tr>
					
					<tr>
						<th class="right-border bottom-border text-right">内容2</th>
						<td class="right-border bottom-border" colspan="7">
							<textarea class="span12" rows="7" id="JHTZ_NR2" check-type="required maxlength" maxlength="3000" fieldname="JHTZ_NR2" name="JHTZ_NR2"></textarea></td>
					</tr>
     		</table>
      	</form>
    </div>
  </div>
</div>

  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id="frmPost">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id="queryXML">
         <input type="hidden" name="txtXML" id="txtXML">
         <input type="hidden" name="txtFilter" order="" fieldname="" id="txtFilter">
         <input type="hidden" name="resultXML" id="resultXML">
         <input type="hidden" id="queryResult" name="queryResult"/>
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">		
 	</FORM>
 </div>
</body>
</html>