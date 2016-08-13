<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<%
//当前节点ID是添加子节点的父ID
String parentid=request.getParameter("wdid");
String wddj=request.getParameter("wddj");
%>

<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/BzwdController.do";
  var g_parentid='<%=parentid%>';
  var g_wddj='<%=wddj%>';
	$(function() {
		doInit();
		//保存按钮
		var btn = $("#btn_add");
		btn.click(function(){
	 		 	 if($("#demoForm").validationButton())
				{
	 		 		//生成json串
	 				var data = Form2Json.formToJSON(demoForm);
	 				//组成保存json串格式
	 				var data1 = defaultJson.packSaveJson(data);
	 				//调用ajax插入
	 				var id=$("#GC_WD_BZWD_ID").val();
	 			 	if(""==id)
	 			 		{
		 			 		defaultJson.doInsertJson(controllername + "?insertBzwd", data1,null,'addHuiDiao');
	 			 		}
	 			 	else{
		 			 		defaultJson.doInsertJson(controllername + "?updateBzwd", data1,null,'editHuiDiao');
	 			 	}
				}else
				{
					return ;
				} 
		  });
	});
	function doInit(){
		$("#PARENT_ID").val(g_parentid);
		$("#WDDJ").val(g_wddj);
		//修改数据
		if('null'==g_wddj){
			var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
			var odd=convertJson.string2json1(rowValue);
			var name=odd.name;
			var id=odd.bzwdid;
			var sort=odd.sort;
			var wddj=odd.wddj;
			$("#WDMC").val(name);
			$("#GC_WD_BZWD_ID").val(id);
			$("#SORT").val(sort);
			$("#WDDJ").val(wddj);
			//如果是跟节点默认为0
			if(wddj=='1'){
				$("#PARENT_ID").val('0');
			}else{
				$("#PARENT_ID").val(odd.parentId);
			}
		}
		
	}
//回调函数
function addHuiDiao()
{
	var data2 = $("#frmPost").find("#resultXML").val();
	var resultmsgobj = convertJson.string2json1(data2);
	var subresultmsgobj = resultmsgobj.response.data[0];
	var parentid=$(subresultmsgobj).attr("PARENT_ID");
	var wdid=$(subresultmsgobj).attr("WDID");
	var wdmc=$(subresultmsgobj).attr("WDMC");
	var fuyemian=$(window).manhuaDialog.getParentObj();
    fuyemian.ziyemian(parentid,wdid,wdmc);
    $(window).manhuaDialog.close();
}
function editHuiDiao()
{
	var data2 = $("#frmPost").find("#resultXML").val();
	var resultmsgobj = convertJson.string2json1(data2);
	var subresultmsgobj = resultmsgobj.response.data[0];
	var parentid=$(subresultmsgobj).attr("PARENT_ID");
	var wdid=$(subresultmsgobj).attr("WDID");
	var fuyemian=$(window).manhuaDialog.getParentObj();
    fuyemian.ziyemian(parentid,wdid);
    $(window).manhuaDialog.close();
}


</script>   
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
  <h4 class="title">节点信息
  	<span class="pull-right">
				 <button id="btn_add" class="btn"  type="button">保存</button>
	</span> 
  </h4>
 <form method="post" action="${pageContext.request.contextPath }/insertdemo.xhtml" id="demoForm"  >
      		<table class="B-table" width="100%"  >
      			<TR  style="display: none;">
			        <TD class="right-border bottom-border">
			            <input class="span12"  id="GC_WD_BZWD_ID"  type="text"  name = "GC_WD_BZWD_ID" fieldname="GC_WD_BZWD_ID">
			            <input class="span12"  id="PARENT_ID"  type="text"  name = "PARENT_ID" fieldname="PARENT_ID">
			            <input class="span12"  id="WDDJ"  type="text"  name = "WDDJ" fieldname="WDDJ">
			        </TD>
                </TR>
      			<tr>
      				<th  width="12%" class="right-border bottom-border text-right">节点名称</th>
          			<td   width="38%" class="right-border bottom-border "  >
          				<input class="span12" style="width:90%"   id="WDMC" type="text" placeholder="必填" fieldname="WDMC" check-type="required" name = "WDMC">
          			<th  width="12%"  class="right-border bottom-border text-right">排序号</th>
          			<td width="38%" class="right-border bottom-border ">
          			<input  class="span12"  id="SORT"  type="text"  check-type="required number" placeholder="必填"   name = "SORT" fieldname="SORT"></td>
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