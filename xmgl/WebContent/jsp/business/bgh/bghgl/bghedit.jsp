<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<app:base/>
	<%
	request.setCharacterEncoding("utf-8");
	User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
	String username = user.getName();
	String department = user.getDepartment();
	%>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/banGongHuiController.do";
	$(function() {
		init();
		//保存按钮
		var btn = $("#but_save");
		btn.click(function()
		  {
	 		 	 if($("#demoForm").validationButton())
				{
	 		 		//生成json串
	 				var data = Form2Json.formToJSON(demoForm);
	 				//组成保存json串格式
	 				var data1 = defaultJson.packSaveJson(data);
	 				//调用ajax插入
 			 		defaultJson.doInsertJson(controllername + "?updateBanGongHui", data1,null,'editHuiDiao');
				}
 		   });
		var btn = $("#but_end");
		btn.click(function()
		  {
 		 	 if($("#demoForm").validationButton())
			{
 		 		xConfirm("提示信息","是否确认完成?");
				$('#ConfirmYesButton').unbind();
				$('#ConfirmYesButton').one("click",function(){ 
					//生成json串
					 $("#HYZT").attr("code",1);
	 				var data = Form2Json.formToJSON(demoForm);
	 				//组成保存json串格式
	 				var data1 = defaultJson.packSaveJson(data);
	 				//调用ajax插入
					defaultJson.doInsertJson(controllername + "?updateBanGongHui", data1,null,'editHuiDiao');
				});
			}
 		   });
		});
function  init(){
	  var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#selectrow").val();
	  var odd=convertJson.string2json1(rowValue);
	  var hysj=odd.hysj;
	  hysj=hysj.replace(" ","T");
	  //将数据放入表单
	  $("#GC_BGH_ID").val(odd.id);
	  $("#HYSJ").val(hysj);
	  $("#HC").val(odd.name);
	  $("#HYDD").val(odd.hydd);
	  $("#HYZC").val(odd.hyzc);
	  $("#CHBM").val(odd.chbm);
	  $("#CHRY").val(odd.chry);
	  $("#HYZT").val(odd.hyzt);
}
//回调函数
function editHuiDiao()
{
	var fuyemian=$(window).manhuaDialog.getParentObj();
    fuyemian.edithuidiao(); 
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
		<button id="but_save" class="btn"  type="button">保存</button>
		<button id="but_end" class="btn"  type="button">结束会议</button>
		</span> 
  </h4>
 <form method="post" action="${pageContext.request.contextPath }/insertdemo.xhtml" id="demoForm"  >
     	<input class="span12" id="GC_BGH_ID" type="hidden"   fieldname="GC_BGH_ID" name = "GC_BGH_ID">
     	<input class="span12" id="HYZT" type="hidden"   fieldname="HYZT" name = "HYZT">
      		<table class="B-table" width="100%"  >
      			<tr>
      				<th  width="8%" class="right-border bottom-border text-right ">会议时间</th>
          			<td class="right-border bottom-border">
          				<input class="span5"    id="HYSJ" type="datetime-local"  name = "HYSJ" fieldname="HYSJ">
          			</td>
          			<th  width="8%" class="right-border bottom-border text-right ">会次</th>
          			<td class="right-border bottom-border">
          				<input class="span12"  check-type="required" placeholder="必填" type="text" maxlength="30" id="HC"  name = "HC" fieldname="HC">
          			</td>
          		</tr>
        			<tr>
      				<th  width="8%" class="right-border bottom-border text-right ">会议地点</th>
          			<td class="right-border bottom-border">
          				<input class="span12"  check-type="required" placeholder="必填" type="text" maxlength="100" id="HYDD"  name = "HYDD" fieldname="HYDD">
          			</td>
          			<th  width="8%" class="right-border bottom-border text-right ">会次主持</th>
          			<td class="right-border bottom-border">
          				<input class="span12"  check-type="required" placeholder="必填" type="text" maxlength="100" id="HYZC"  name = "HYZC" fieldname="HYZC">
          			</td>
          		</tr>
          			<tr>
          			<th class="right-border bottom-border text-right ">参会</th>
          			<td class="right-border bottom-border"  colspan="3">
          			<textarea rows="3" class="span12"  check-type="required" placeholder="必填"  maxlength="100" id="CHBM"  name = "CHBM" fieldname="CHBM"></textarea>
          			</td>
        		</tr>
        		</table>
      	  </form>
     	</div>
 	</div>
</div>
   <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
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