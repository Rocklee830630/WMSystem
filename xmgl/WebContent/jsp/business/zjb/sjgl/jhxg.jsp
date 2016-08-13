<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script type="text/javascript" charset="utf-8">
	var controllername = "${pageContext.request.contextPath }/shenJiGuanliController.do";
	var tbl = null;
	$(function() {
		var btn = $("#example1");
		btn.click(function() {
			
			if ($("#demoForm").validationButton()) {
				//生成json串
				var data = Form2Json.formToJSON(demoForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//调用ajax插入
			   defaultJson.doUpdateJson(controllername + "?update_jss", data1,null,'eidtHuiDiao');
			}
		});
		var guanbi = $("#guanbi");
		guanbi.click(function() {
			if (confirm("您确定要关闭本页吗？")) 
			{
				$(window).manhuaDialog.close();
			}
		});
	}
	);
	//清空表单
	$(function()
	  {
			  var sjnd =$($(window).manhuaDialog.getParentObj().document).find("#SJND").val();
			  $("#sjndid").html(sjnd);
				$.ajax(
						{
							     url : controllername+"?querysjxx&sjnd="+sjnd,//此处定义后台controller类和方法
						         dataType : 'json',//此处定义返回值的类型为string，详见样例代码
						         async :	false,
						         success : function(result) {
				        	  	 var resultmsg = result.msg; //返回成功事操作\
						         var odd=convertJson.string2json1(resultmsg);
						         $("#demoForm").setFormValues(odd.response.data[0]);
						         },
						         error : function(result) {//返回失败操作
						           defaultJson.clearTxtXML();
						          }			
						});
				 $("#SJND").val(sjnd);
			  
	  });
function eidtHuiDiao()
{
	//返回数据
	var fuyemian=$(window).manhuaDialog.getParentObj();
	fuyemian.query();
	$(window).manhuaDialog.close();
}
</script>   
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
  <h4 class="title">审计年度为：<font  id ="sjndid" style="color:red;font-weight:bold;"> </font>
  	  <span class="pull-right">
		  <button id="example1" class="btn"  type="button">保存</button>
      </span>  
  </h4>
     	<form method="post" action="${pageContext.request.contextPath }/updateJieSuan.do" id="demoForm"  >
      			<table class="B-table" width="100%"  >
	      			<TR  style="display:none;">
				        <TD class="right-border bottom-border">
	                        <input class="span12" id="SJND" type="text"     fieldname="SJND" name = "SJND">
				        </TD>
	                </TR>
       			<tr>
       			     <th width="8%" class="right-border bottom-border text-right">审计报告编号</th>
          			<td width="17%" class="right-border bottom-border " ><input class="span12"  type="text"     maxlength="36" id="SJBGBH" name = "SJBGBH" fieldname="SJBGBH"></td>
          			<th width="8%"  class="right-border bottom-border text-right">日期</th>
          			<td width="17%" class="right-border bottom-border "><input class="span12 date"  type="date"  id="SJSDRQ"   name = "SJSDRQ" fieldname="SJSDRQ"></td>
          			</tr>
        		</table>
      	</form>
     	</div>
 	</div>
</div>
 <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "XMNF"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
         <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 </div>
  <script>

</script>
</body>
</html>