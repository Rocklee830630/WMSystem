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
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtsjController.do";
var htqdj = 0;
var htqdj_new = 0;
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtsjList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,gcHtglHtsjList);
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#gcHtglHtsjForm").validationButton())
		{
		    //生成json串
		    var data = Form2Json.formToJSON(gcHtglHtsjForm);
		  //组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  //调用ajax插入
		    if($("#ID").val() == "" || $("#ID").val() == null){
		    	var flag = defaultJson.doInsertJson(controllername + "?insert", data1,gcHtglHtsjList);
		    	if(flag){
	    			//$("#gcHtglHtsjForm").clearFormResult();
	    			//var tempJson = parent.$("body").find("#AlerdyHTSJFlag").val("true");
	    			
	    			var parentIObj = $(this).manhuaDialog.getParentObj();
					parentIObj.$("#btnZF").attr("disabled", false);
					parentIObj.$("#btnWCTZ").attr("disabled", false);
					parentIObj.$("#btnBG").attr("disabled", false);
		    	}
    		}else{
    			defaultJson.doUpdateJson(controllername + "?update", data1,gcHtglHtsjList);
    		}
		    $(window).manhuaDialog.setData("save");
			$(window).manhuaDialog.sendData();
		}else{
			requireFormMsg();
		  	return;
		}
	});
	//按钮绑定事件(删除)
	$("#btnDelete").click(function() {
		if($("#gcHtglHtsjForm").validationButton())
		{
			xConfirm("信息确认","您确认删除所选项目吗？ ");
			
			$('#ConfirmYesButton').attr('click','');
			$('#ConfirmYesButton').one("click",function(){
			    //生成json串
			    var data = Form2Json.formToJSON(gcHtglHtsjForm);
			  //组成保存json串格式
			    var data1 = defaultJson.packSaveJson(data);
			    $("#btnDelete").attr("disabled", true);
	    		var flag = defaultJson.doDeleteJson(controllername + "?delete", data1,gcHtglHtsjList);
	    		if(flag){
	    			//alert("删除成功");
	    			//$("#btnDelete").attr("disabled", false);
	    		}
	    		$(window).manhuaDialog.setData("delete");
				$(window).manhuaDialog.sendData();
			});
		}else{
			requireFormMsg();
		  	return;
		}
	});
	
	
	$("#HTQDJ").blur(function(){
		if($("#HTQDJ").val()!=''&&!isNaN($("#HTQDJ").val())){
			var qdj = $("#HTQDJ").val();
			//var res = Math.round(qdj / htqdj * 10000) / 100.00;
			var res = 0;
			if(htqdj==0){
				res = 100.00;
			}else{
				var zhtqdj = htqdj-htqdj_new+qdj;
				res = (Number(qdj)/zhtqdj*100).toFixed(2);
			}
			$("#GCJGBFB").val(res);
		}else{
			$("#GCJGBFB").val(0);
		}
	});

	$("#GCJGBFB").blur(function(){
		if($("#GCJGBFB").val()!=''&&!isNaN($("#GCJGBFB").val())){
			var qdj = $("#GCJGBFB").val();
			var res = parseFloat(qdj) / parseFloat(100) ;
			$("#HTQDJ").val((htqdj*res).toFixed(2));
		}
	});
	
	//按钮绑定事件（新增）
    $("#btnClear_Bins").click(function() {
        //$("#gcHtglHtsjForm").clearFormResult();
        //$("#gcHtglHtsjForm").cancleSelected();
        
        
        $("#ID").val("");
        $("#XMMC").val("");
    	$("#BDMC").val("");
    	$("#JHSJID").val("");
    	$("#XMID").val("");
    	$("#BDID").val("");
    });
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
    //$("#id_choseXM").attr("disabled", true);
    
});
//页面默认参数
function init(){
	var parentIObj = $(this).manhuaDialog.getParentObj();
	var tempJson = parentIObj.$("#ID").val();

	
	//var tempJson = parent.$("body").find("#resultXML").val();
	//var tempJson = parent.$("body").find("#ID").val();
	if(tempJson!=""){
		//tempJson = eval("("+tempJson+")");
		//$("#QHTID").val(tempJson.ID);
		//$("#HTID").val(tempJson.ID);
		$("#QHTID").val(tempJson);
		$("#HTID").val(tempJson);
		htqdj = parseInt(parentIObj.$("#ZHTQDJ").val());
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtsjList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryOne",data,gcHtglHtsjList);
	}
}


//点击行事件
function tr_click(obj){
	//alert(JSON.stringify(obj));
	$("#gcHtglHtsjForm").setFormValues(obj);
	if($("#HTQDJ").val()==""){
		$("#HTQDJ").val("0");
	}
	if($("#HTJSJ").val()==""){
		$("#HTJSJ").val("0");
	}
	
	htqdj_new = obj.HTQDJ;
	
	$("#btnSave").attr("disabled", false);
	$("#btnDelete").attr("disabled", false);
}

//选中项目名称弹出页
function selectXm(){
	$(window).manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/kfglgl/xmcx.jsp","modal":"2"}); 
}
//弹出区域回调
getWinData = function(data){
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#BDMC").val(JSON.parse(data).BDMC);
	$("#JHSJID").val(JSON.parse(data).GC_JH_SJ_ID);
	$("#XMID").val(JSON.parse(data).XMID);
	$("#BDID").val(JSON.parse(data).BDID);
	$("#btnSave").attr("disabled", false);
	
	
};

//详细信息
function rowView(index){
	var obj = $("#gcHtglHtsjList").getSelectedRowJsonByIndex(index);
	var xmbh = eval("("+obj+")").XMBH;
	$(window).manhuaDialog(xmscUrl(xmbh));
}

function doRandering_HTBM(obj){
	var showHtml = "";
	if(obj.HTBM!=""){
		showHtml = obj.HTBM;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}

function doRandering_BDMC(obj){
	var showHtml = "";
	if(obj.BDMC!=""){
		showHtml = obj.BDMC;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_BDBH(obj){
	var showHtml = "";
	if(obj.BDBH!=""){
		showHtml = obj.BDBH;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doHTQDJ(obj){
	var showHtml = "";
	if(obj.HTQDJ!=""){
		showHtml = obj.HTQDJ_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doGCJGBFB(obj){
	var showHtml = "";
	if(obj.GCJGBFB!=""){
		showHtml = obj.GCJGBFB;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doHTJSJ(obj){
	var showHtml = "";
	if(obj.HTJSJ!=""){
		showHtml = obj.HTJSJ_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
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
				<input class="span12" type="text" id="QJHSJID" name="JHSJID"  fieldname="JHSJID" value="" operation="=" >
				<input class="span12" type="text" id="QXMID" name="XMID"  fieldname="XMID" value="" operation="=" >
				<input class="span12" type="text" id="QBDID" name="BDID"  fieldname="BDID" value="" operation="=" >
				<input class="span12" type="text" id="QHTID" name="HTID"  fieldname="HTID" value="" operation="=" >
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
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="gcHtglHtsjList" width="100%" type="single" pageNum="9">
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
 			    <th fieldname="HTBM" colindex=3 tdalign="center" maxlength="30" CustomFunction="doRandering_HTBM">&nbsp;合同编码&nbsp;</th>
<%--				<th fieldname="HTMC" colindex=4 tdalign="center" maxlength="30" >&nbsp;合同名称&nbsp;</th>--%>
				<th fieldname="XMMC" colindex=2 tdalign="center" maxlength="30" >&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDMC" colindex=3 tdalign="center" maxlength="30" CustomFunction="doRandering_BDMC">&nbsp;标段名称&nbsp;</th>
				<th fieldname="BDBH" colindex=2 tdalign="center" maxlength="30" CustomFunction="doRandering_BDBH">&nbsp;标段编号&nbsp;</th>
				<th fieldname="HTQDJ" colindex=5 tdalign="center" CustomFunction="doHTQDJ">&nbsp;合同签订价<b>(元)</b>&nbsp;</th>
				<th fieldname="GCJGBFB" colindex=6 tdalign="center" CustomFunction="doGCJGBFB">&nbsp;价格百分比(%)&nbsp;</th>
				<th fieldname="HTJSJ" colindex=7 tdalign="center" CustomFunction="doHTJSJ">&nbsp;合同结算价或中止价<b>(元)</b>&nbsp;</th>
				
<%--				<th fieldname="BZ" colindex=20 tdalign="center" maxlength="30" >&nbsp;备注&nbsp;</th>--%>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	</div>
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">合同项目/标段
      	<span class="pull-right">
<%--      		<button id="btnClear_Bins" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>--%>
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>保存</button>
	  		<button id="btnDelete" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>删除</button>
      	</span>
      </h4>
     <form method="post" id="gcHtglHtsjForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
	  	<input type="hidden" id="JHSJID" name="JHSJID"  fieldname="JHSJID" >
		<input type="hidden" id="XMID" name="XMID"  fieldname="XMID" >
		<input type="hidden" id="BDID" name="BDID"  fieldname="BDID" >
		<input type="hidden" id="HTID" name="HTID"  fieldname="HTID" >
	  	
        <tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
       	 	<td class="bottom-border right-border" width="25%">
         		<input class="span12" style="width:85%" id="XMMC" type="text" fieldname="XMMC" name = "XMMC"  disabled />
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
			<td width="23%" class="right-border bottom-border">
				<input id="BDMC" class="span12" style="width:85%" name="BDMC" fieldname="BDMC" type="text" disabled/>
				<%--          		<button class="btn btn-link"  type="button" id="id_choseXM" onclick="queryProjectWithBD()"><i class="icon-edit"></i></button>--%>
				<button class="btn btn-link"  type="button" id="id_choseXM" onclick="queryProjectWithBD()"><i class="icon-edit"></i></button>
			</td>
        </tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">合同签订价</th>
			<td width="17%" class="right-border bottom-border">
				<input id="HTQDJ" value=0 class="span9" style="width:70%;text-align:right;" check-type="required"  name="HTQDJ" fieldname="HTQDJ" type="number" min="0"/><b>(元)</b>
			</td>
			<th width="8%" class="right-border bottom-border text-right">价格百分比</th>
			<td width="17%" class="right-border bottom-border">
				<input id="GCJGBFB" value=0 class="span9" style="width:70%;text-align:right;"  name="GCJGBFB" fieldname="GCJGBFB" type="number" min="0"/><b>(%)</b>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">合同结算价或中止价</th>
			<td width="17%" class="right-border bottom-border">
				<input id="HTJSJ" value=0 class="span9" style="width:70%;text-align:right;"  name="HTJSJ" fieldname="HTJSJ" type="number" min="0"/><b>(元)</b>
			</td>
		</tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right">备注</th>
	        <td colspan="3" class="bottom-border right-border" >
	        	<textarea class="span12" rows="2" id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ"></textarea>
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
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "ghh2.LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
<script>
</script>
</html>