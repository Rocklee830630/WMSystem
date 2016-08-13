<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>计划下达</title>
<%
	//获取当前时间
	String sysdate = Pub.getDate("yyyy-MM-dd");
	String jhmc = request.getParameter("jhmc");
	String spid = request.getParameter("spid");
	String nd = request.getParameter("nd");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
var spid = '<%=spid%>';
var nd = '<%=nd%>';
//页面初始化
$(function() {
	init();
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if(!$("#jhFormAxm").validationButton()){
			requireFormMsg();
			return;
		} 
		//jiangc start 添加提醒
		
		xConfirm("信息提示","下达后项目性质不可修改，确认要下达吗？");
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){  
			//var xdrq = $("#xdrqApc").val();
			var ywid = $("#ywid").val();
			var formData = Form2Json.formToJSON(jhFormAxm);
			var data = defaultJson.packSaveJson(formData);
			doInsertJh(controllername + "?insertJhApc&ywid="+ywid+"&spid="+spid+"&nd="+nd,data);			
		});  
		//jiangc end
	});
});
//页面默认参数
function init(){
	setTcjhDefaultInfo();
	checkXflx();
}


//设置统筹计划相关默认值
function setTcjhDefaultInfo(){
	//默认当前时间
	$("#xdrqAxm").val("<%=sysdate %>");
	//默认下发类型
	$("#XFLX").val("1");//正常
	//默认批次号
	$("#jhpch").val(setDefaultPch($("#XFLX").val(),nd));
}

//默认批次号
function setDefaultPch(xflx,nd){
	var pch = "";
	var actionName=controllername+"?queryMaxPch&xflx="+xflx+"&nd="+nd;
	$.ajax({
		url : actionName,
		data : null,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(result) {
			pch = result.msg;
		},
	    error : function(result) {
		}
	});
    return pch;
}
//下发类型默认选中正常
function checkXflx(){
	$("input[name=XFLX]").each(function(){
	  	if(this.value=='1'){
		  this.checked=true;
    	}
	});	
}
//控制下达文号和批次是否显示
function setPch(e){
	if(e.value==2){
		$("#jhpch").attr("src","LXPC");
		reloadSelectTableDic($("#jhpch"));
		
	}
	else{
		$("#jhpch").attr("src","PCH");
		reloadSelectTableDic($("#jhpch"));
	}	
	//默认批次号
	$("#jhpch").val(setDefaultPch(e.value));

}

//按项目下发(业务操作包括，更新下达库数据，新增计划主体数据，新增统筹计划数据)
doInsertJh = function(actionName, data1) {
    var success  = true;
	$.ajax({
		type : 'post',
		url : actionName,
		data : data1,
		dataType : 'json',
		async :	false,
		success : function(result) {
			$(window).manhuaDialog.getParentObj().getWinData_jhxd();
			$(window).manhuaDialog.close(); 
		},
	    error : function(result) {
		     	alert(result.msg);
			    defaultJson.clearTxtXML();
			    success = false;
		}
	});
	 return success;
};
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	 <div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">
		<h4 class="title"><%=jhmc %>
			<span class="pull-right">
				<button id="btnSave" class="btn" type="button">保存</button>
			</span>
		</h4>
	  	<form method="post" id="jhFormAxm"  >
	   	<table class="B-table" id="jhpcList">
	   	<tr>
	    	<th width="8%" class="right-border bottom-border text-right">下发类型</th>
	        <td class="bottom-border right-border" colspan="3">
	        	<input class="span12" id="XFLX" type="radio" onclick="setPch(this)" kind="dic" src="E#1=正常下达:2=零星下达" name= "XFLX" fieldname="XFLX"/>
	        </td>
	    </tr>
	    <tr>
	    	<th width="8%" class="right-border bottom-border text-right">统筹名称</th>
	        <td class="bottom-border right-border" colspan="3">
	        	<input class="span12" type="text"  fieldname="JHMC" name = "JHMC" maxlength="100">
	        </td>
	    </tr>
	    <tr>
	        <th width="8%" class="right-border bottom-border text-right">统筹批次</th>
	        <td class="right-border bottom-border" width="20%">
	        	<select class="span12" id="jhpch" name = "JHPCH" check-type="required" fieldname = "JHPCH" operation="=" kind="dic" src="PCH">
	          </select>
	        </td>
	        <th width="8%" class="right-border bottom-border text-right">下达日期</th>
	        <td class="right-border bottom-border">
	        	<input class="span12" type="date" id="xdrqAxm" placeholder="" check-type="required" name = "XDRQ" fieldname="XDRQ">
	        </td>
	    </tr>
	    <tr>
	     	<th width="8%" class="right-border bottom-border text-right">附件信息</th>
	     	<td colspan="3" class="bottom-border right-border">
				<div>
					<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0060">
						<i class="icon-plus"></i>
						<span>添加文件...</span>
					</span>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0060" class="files showFileTab"
							data-toggle="modal-gallery" data-target="#modal-gallery">
						</tbody>
					</table>
				</div>
				
			</td>
	     </tr>
	   </table>
	   </form>
	 </div>
    </div>
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "PCH,XMBH"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="ywid" id = "ywid" value="">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>