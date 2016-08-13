<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>区域选择</title>
<%
	String o_qy = request.getParameter("o_qy");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
var o_qy = "<%=o_qy%>";
//页面初始化
$(function() {
	
	init();
	
	 $("#deleteQy").click(function() {
		$("input[type='checkbox']").each(function(){
			$(this).attr("checked",false);
		}); 
	 });

	 
	$("#btnQd").click(function() {
		var qy = [];
		var qyCode = [];
		$("input[type='checkbox']").each(function(){
			if($(this).is(':checked')){
				qy[qy.length] = $(this).attr('value');
			}
		}); 
		var r=document.getElementsByName("QY");  
	    for(var i=0;i<r.length;i++){
	    	if(r[i].checked){
	    		qyCode[qyCode.length] = r[i].nextSibling.nodeValue;
	    	}
	    }      
	    var data = {qyValue:qy,qyText:qyCode};
		$(window).manhuaDialog.setData(data);
		$(window).manhuaDialog.sendData();
		$(window).manhuaDialog.close();
	});
});
//页面默认参数
function init(){
	 if(o_qy == "" || o_qy == null){
		return;
	}else{
		var qyArray = new Array();
		qyArray = o_qy.split(",");
		$("input[type='checkbox']").each(function(){
			for(var i=0;i<qyArray.length;i++){
				if($(this).val() == qyArray[i]){
					$(this).attr("checked",true);
					break;
				}
			}
		}); 
	}	
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	 <div class="row-fluid">
    	<form method="post" id="xdxmkForm"  >
      		<table class="B-table" width="100%">
      		<tr>
      			<td>
      				&nbsp;&nbsp;<input class="span12" type="checkbox" id="qy" lineType="upline" name = "QY"  kind="dic" src="QY" fieldname = "QY" ></br> 
      			</td>
      		</tr>
      		<tr>
      			<td class="text-left bottom-border text-right">
           			<span class="pull-right">
	  					<button id="btnQd" class="btn" type="button" style="margin-top:-660px;">确定</button>
           				<button id="deleteQy" class="btn" type="button" style="margin-top:-660px;">清空</button>
      				</span>
          		</td>
      		</tr>
      		</table>
      		
      	</form>
    </div>
	
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
<script>

</script>
</body>
</html>