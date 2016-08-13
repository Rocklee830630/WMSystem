<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>计划审批</title>
<%
	String type = request.getParameter("type");
	String nd = request.getParameter("nd");
	if(Pub.empty(nd)){
		nd = "";
	}
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
var type = '<%=type%>';
var nd = '<%=nd%>';
//页面初始化
$(function() {
	init();
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if(!$("#jhForm").validationButton()){
			requireFormMsg();
			return;
		} 
		var data = Form2Json.formToJSON(jhForm);
		$(window).manhuaDialog.getParentObj().getWinData_InsertJh(data);
		$(window).manhuaDialog.close(); 
 			
	});
});
//页面默认参数
function init(){
	if(type == "insert"){
		setTcjhDefaultInfo();
		//checkXflx();
		//setJhmcDefaultInto($("#jhpch").val(),$("#XFLX").val());
	}else if(type == "update"){
		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.document.getElementById("resultXML").value;
		var obj = convertJson.string2json1(rowValue);
		if(obj != null){
			//deleteFileData(obj.GC_JH_ZT_ID,"",obj.SJBH,obj.YWLX);
			//setFileData(obj.GC_JH_ZT_ID,"",obj.SJBH,obj.YWLX,"0");
			//queryFileData(obj.GC_JH_ZT_ID,"",obj.SJBH,obj.YWLX);
			$("#jhForm").setFormValues(obj);
		}
	}
	
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
			$("#resultXML").val(result.msg);
			success = true;
		},
	    error : function(result) {
		     	alert(result.msg);
			    defaultJson.clearTxtXML();
			    success = false;
		}
	});
	 return success;
};

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
		$("#jhpch").val(setDefaultPch(e.value));
		setJhmcDefaultInto($("#jhpch").val(),2);
	}
	else{
		$("#jhpch").attr("src","PCH");
		reloadSelectTableDic($("#jhpch"));
		$("#jhpch").val(setDefaultPch(e.value));
		setJhmcDefaultInto($("#jhpch").val(),1);
	}	
	//默认批次号
	
	
}
//设置统筹计划相关默认值
function setTcjhDefaultInfo(){

	//默认下发类型
	//$("#XFLX").val("1");//正常
	//默认批次号
	//$("#jhpch").val(setDefaultPch($("#XFLX").val()));
	//setDefaultNd("qnd");
	$("#qnd").val(nd);
}
//默认批次号
function setDefaultPch(xflx){
	var pch = "";
	var actionName=controllername+"?queryMaxPch&xflx="+xflx;
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

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	 <div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">
		<h4 class="title">计划信息
			<span class="pull-right">
				<button id="btnSave" class="btn" type="button">保存</button>
			</span>
		</h4>
	  	<form method="post" id="jhForm">
		    	<input type="hidden" id="GC_TCJH_SP_ID" fieldname="GC_TCJH_SP_ID">
		    	<input type="hidden" id="SJBH" fieldname="SJBH">
		    	<input type="hidden" id="SPLX" fieldname="SPLX" value="2">
				<table class="B-table">
					<th class="right-border bottom-border text-right">审批名称</th>
					<td class="right-border bottom-border">
						<input id="SPMC" class="span12" type="text" placeholder="必填" check-type="required maxlength" name="SPMC"  maxlength="100" fieldname="SPMC"/>
					</td>					
					<th class="right-border bottom-border text-right">年度</th>
					<td width="15%" class="right-border bottom-border">
						<select class="span12" id="qnd" name = "QND" fieldname = "ND" kind="dic" src="T#GC_TCJH_XMCBK:distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE">
            		</select>
					</td>	
	      			<tr>
	         			<th width="10%" class="right-border bottom-border text-right">备注</th>
	         			<td class=" bottom-border text-left" colspan="3">
	         				<textarea class="span12" id="BZ" rows="5" name ="BZ"  fieldname="BZ" maxlength="4000"></textarea>
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