<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>合同文件-维护</title>
<%
	String type=request.getParameter("type");
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtWjController.do";
var type ="<%=type%>";
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtWjList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,gcHtglHtWjList);
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#gcHtglHtWjForm").validationButton())
		{
		    //生成json串
		    var data = Form2Json.formToJSON(gcHtglHtWjForm);
		 	//调用ajax插入
	    	parent.$("body").manhuaDialog.setData(data);
			parent.$("body").manhuaDialog.sendData();//回调函数getWinData(data)
			parent.$("body").manhuaDialog.close();
		}else{
			requireFormMsg();
		  	return;
		}
	});
	
	//按钮绑定事件（新增）
    $("#btnClear_Bins").click(function() {
        $("#gcHtglHtWjForm").clearFormResult();
        $("#gcHtglHtWjForm").cancleSelected();
        
        
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
		$("#gcHtglHtWjForm").setFormValues(tempJson);
		
		//$("#addFileSpan").bind("click", doSelectFile(this));
		$("#addFileSpan").bind("click", function() {
			doSelectFile($(this));
		});

		
		setFileData(tempJson.ID,"","","");
    	queryFileData(tempJson.ID,"","","");
	}
}


//点击行事件
function tr_click(obj){
	//alert(JSON.stringify(obj));
	$("#gcHtglHtWjForm").setFormValues(obj);
}

//选中项目名称弹出页
function selectHt(){
	$("body").manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath}/jsp/business/htgl/htcx.jsp","modal":"2"});
}
//弹出区域回调
getWinData = function(data){
	$("#HTSJID").val(JSON.parse(data).HTID);
	$("#HTMC").val(JSON.parse(data).HTMC);
	$("#HTBM").val(JSON.parse(data).HTBM);
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#BDMC").val(JSON.parse(data).BDMC);
	$("#BGMC").val(JSON.parse(data).BDMC);
};

//详细信息
function rowView(index){
	var obj = $("#gcHtglHtWjList").getSelectedRowJsonByIndex(index);
	var xmbh = eval("("+obj+")").XMBH;
	$("body").manhuaDialog(xmscUrl(xmbh));
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">合同文件
      	<span class="pull-right">
      		<button id="btnClear_Bins" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
      	</span>
      </h4>
     <form method="post" id="gcHtglHtWjForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
	  	<input type="hidden" id="HTSJID" name="HTSJID"  fieldname="HTSJID" >
	  	
        <tr>
			<th width="8%" class="right-border bottom-border text-right">合同名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="HTMC" type="text" placeholder="必填" check-type="required" fieldname="HTMC" name = "HTMC"  disabled />
          		<button class="btn btn-link"  type="button" onclick="selectHt()"><i class="icon-edit"></i></button>
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
        	<th width="8%" class="right-border bottom-border text-right">相关附件</th>
        	<td colspan="7" class="bottom-border right-border">
				<div>
					<span class="btn btn-fileUpload" id="addFileSpan" fjlb="0029">
						<span>添加文件...</span>
					</span>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0029" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
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
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "t.LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>