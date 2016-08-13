<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%
	String type=request.getParameter("type");	
	String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<app:base/>
<title>项目征收资金情况-维护</title>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglZszjXmzszjqkController.do";
var jbrID;
var type ="<%=type%>";
//页面初始化
$(function() {
	//init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,xmzszjqkList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,xmzszjqkList);
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
<%--		if($("#xmzszjqkForm").validationButton())--%>
<%--		{--%>
<%--		    //生成json串--%>
<%--		    var data = Form2Json.formToJSON(xmzszjqkForm);--%>
<%--		  //组成保存json串格式--%>
<%--		    var data1 = defaultJson.packSaveJson(data);--%>
<%--		  //调用ajax插入--%>
<%--		    if($("#ID").val() == "" || $("#ID").val() == null){--%>
<%--    			defaultJson.doInsertJson(controllername + "?insert", data1,xmzszjqkList);--%>
<%--    			$("#xmzszjqkForm").clearFormResult();--%>
<%--    		}else{--%>
<%--    			defaultJson.doUpdateJson(controllername + "?insert", data1,xmzszjqkList);--%>
<%--    		}--%>
<%--		}else{--%>
<%--			requireFormMsg();--%>
<%--		  	return;--%>
<%--		}--%>
		
		
		if($("#xmzszjqkForm").validationButton())
		{
    		//生成json串
    		var data = Form2Json.formToJSON(xmzszjqkForm);
		   	$(window).manhuaDialog.setData(data);
			$(window).manhuaDialog.sendData();//回调函数getWinData(data)
			$(window).manhuaDialog.close();
		}else{
			requireFormMsg();
		  	return;
		}
	});
	
	//按钮绑定事件（新增）
    $("#btnClear_Bins").click(function() {
        $("#xmzszjqkForm").clearFormResult();
        $("#xmzszjqkForm").cancleSelected();
        
        
        $("#ZFRQ").val(new Date().toLocaleDateString());
        $("#ZFJE").val(0);
        $("#ID").val("");
    });
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
	
    $("button[id^='blrBtn_']").bind("click", function(){
    	var valu = $(this).attr("value");
    	jbrID = valu;
    	var actorCode = $("#"+valu).attr("code");
		openUserTree('single',actorCode,'setBlr') ;
	});
    
	if(type == "insert"){
		$("#ZFRQ").val('<%=sysdate%>');
	}
    
});
//页面默认参数
function init(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xmzszjqkList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,xmzszjqkList);
}


//点击行事件
function tr_click(obj){
	//alert(JSON.stringify(obj));
	$("#xmzszjqkForm").setFormValues(obj);
}

//选中项目名称弹出页
function selectXm(){
<%--	$(window).manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zjgl/xmcx.jsp","modal":"2"});--%>
	$(window).manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/kfglgl/xmcx.jsp","modal":"2"}); 
}
//弹出区域回调
getWinData = function(data){
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#XMBH").val(JSON.parse(data).XMBH);
	$("#GC_JH_SJ_ID").val(JSON.parse(data).GC_JH_SJ_ID);
};

//详细信息
function rowView(index){
	var obj = $("#xmzszjqkList").getSelectedRowJsonByIndex(index);
	var xmbh = eval("("+obj+")").XMBH;
	$(window).manhuaDialog(xmscUrl(xmbh));
}

function setBlr(data){
	var userId = "";
	var userName = "";
	for(var i=0;i<data.length;i++){
 	 var tempObj =data[i];
 	 if(i<data.length-1){
	  userId +=tempObj.ACCOUNT+",";
	  userName +=tempObj.USERNAME+",";
 	 }else{
 	  userId +=tempObj.ACCOUNT;
 	  userName +=tempObj.USERNAME; 
 	 }
	}
	$("#"+jbrID).val(userName);
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">项目征收资金情况
      	<span class="pull-right">
      		<button id="btnClear_Bins" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
      	</span>
      </h4>
     <form method="post" id="xmzszjqkForm"  >
      <table class="B-table" width="100%" >
      <input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
	  	<input type="hidden" id="GC_JH_SJ_ID" fieldname="XMID" name = "XMID"/></TD>
        <tr>
			<th width="8%" class="right-border bottom-border text-right">项目名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="XMMC" type="text" placeholder="必填" check-type="required" fieldname="XMMC" name = "XMMC"  disabled />
          		<button class="btn btn-link"  type="button" onclick="queryProjectWithBD()"><i class="icon-edit"></i></button>
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right">项目编号</th>
       		<td class="bottom-border right-border"width="15%">
         		<input class="span12" style="width:100%" id="XMBH" type="text" fieldname="XMBH" name = "XMBH"  disabled/>
         	</td>
        </tr>
        <tr>
			<th width="8%" class="right-border bottom-border text-right">支付金额</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZFJE" class="span12" keep="true" placeholder="必填" check-type="required" check-type="maxlength" maxlength="17" value=0 style="width:70%;text-align:right;" name="ZFJE" fieldname="ZFJE" type="number" />&nbsp;<b>(元)</b>
			</td>
			<th width="8%" class="right-border bottom-border text-right">支付日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZFRQ" class="span7" type="date" check-type="maxlength" maxlength="10" name="ZFRQ" fieldname="ZFRQ"/>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">联系人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="LXR" class="span12" style="width:80%;" pcheck-type="maxlength" maxlength="36" name="LXR" fieldname="LXR" type="text" />
				<button class="btn btn-link"  type="button" id="blrBtn_LXR" value="LXR"><abbr title="点击选择联系人"><i class="icon-edit"></i></abbr></button>
			</td>
			<th width="8%" class="right-border bottom-border text-right">联系方式</th>
			<td width="17%" class="right-border bottom-border">
				<input id="LXFS" class="span12" check-type="maxlength" maxlength="40" name="LXFS" fieldname="LXFS" type="text" />
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
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "t.LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>