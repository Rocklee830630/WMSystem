<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>征收资金管理首页</title>
<%
	String cYear = Pub.getDate("yyyy");
	String preYear = String.valueOf(Integer.parseInt(cYear)-1);
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglZszjZfzsbController.do";

var flag, qyID="", cYear;

//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
}


//页面初始化
$(function() {
	setPageHeight();
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		
<%--		if($("#QZBBM").val()!=""){--%>
<%--			var v = $("#QZBBM").find("option:selected").text();  --%>
<%--			$("#ZBBM").val(v);--%>
<%--		}--%>
		
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryQyList&nd="+$("#QND").val(),data,DT1);
	});
	//按钮绑定事件(拨付)
	$("#btnInsertBF0").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			//requireSelectedOneRow();
		    //return
		 }
		var rowValue = $("#DT1").getSelectedRow();
		var tempJson = convertJson.string2json1(rowValue);
		var jhsjid = tempJson.JHSJID;
		
		$(window).manhuaDialog({"title":"征收资金管理>新增拨付","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/zfzsb-addOneOnly.jsp?type=insert&qy="+qyID,"modal":"2"});
	});
	//按钮绑定事件(拨付)
	$("#btnInsertBF").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		var rowValue = $("#DT1").getSelectedRow();
		var tempJson = convertJson.string2json1(rowValue);
		var jhsjid = tempJson.JHSJID;
		
		$(window).manhuaDialog({"title":"征收资金管理>拨付维护","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/zfzsb-addOne.jsp?type=insert&qy="+qyID,"modal":"2"});
	});
	//按钮绑定事件(使用)
	$("#btnInsertSY0").click(function() {
		$(window).manhuaDialog({"title":"征收资金管理>新增使用","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/xmzszjqk-addOne1Only.jsp?type=insert&qy="+qyID,"modal":"2"});
	});
	//按钮绑定事件(使用)
	$("#btnInsertSY").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			//$("#resultXML").val($("#DT1").getSelectedRow());
			requireSelectedOneRow();
		    return
		 }
		$(window).manhuaDialog({"title":"征收资金管理>使用维护","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/xmzszjqk-addOne1.jsp?type=insert&qy="+qyID,"modal":"2"});
	});
	
	$("#btnQdsp").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"合同>签订审批","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/ht-update-qdlx.jsp?type=update","modal":"1"});
	});
	
	
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
	  	 $(window).manhuaDialog({"title":"导出","type":"text","content":"${pageContext.request.contextPath }/jsp/framework/print/TabListEXP.jsp?tabId=DT1","modal":"3"});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    	$("#QND").val('<%= cYear%>');
    });
	
	
});

//页面默认参数
function init(){
	//cYear = $("#QND").val();
	cYear = '<%= cYear%>';
	$("#QND").val(cYear);
	$("#currYear").
	
	g_bAlertWhenNoResult = false;
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryQyList&nd="+$("#QND").val(),data,DT1, null, true);
	g_bAlertWhenNoResult = true;
}

//点击获取行对象
function tr_click(obj,tabListid){
	//alert(JSON.stringify(obj));
	qyID = obj.JHSJID;
}

//回调函数--用于修改新增
getWinData = function(data){
	//var subresultmsgobj = defaultJson.dealResultJson(data);
	var data1 = defaultJson.packSaveJson(data);
	if(JSON.parse(data).ID == "" || JSON.parse(data).ID == null){
		defaultJson.doInsertJson(controllername + "?insert", data1,DT1);
	}else{
		defaultJson.doUpdateJson(controllername + "?update", data1,DT1);
	}
	
};

//详细信息
function rowView1(t){
	if($("#DT1").getSelectedRowIndex()==-1)
	 {
		requireSelectedOneRow();
	    return
	 }
	var rowValue = $("#DT1").getSelectedRow();
	var tempJson = convertJson.string2json1(rowValue);
	var shidvar = tempJson.JHSJID;
	$(window).manhuaDialog({"title":"征收资金>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/zszj-view.jsp?type=detail&jhsjid="+shidvar,"modal":"2"});
}


function closeParentCloseFunction(msgc){
	if (typeof(msgc) == "undefined") {
	}else if(msgc=="showMsg"){
		xSuccessMsg('操作成功！',null);
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		var tempJson = convertJson.string2json1(data);
		var a=$("#DT1").getCurrentpagenum();	//获取当前页码
		tempJson.pages.currentpagenum=a;		//替换json的页码
		data = JSON.stringify(tempJson);
		defaultJson.doQueryJsonList(controllername+"?queryQyList",data,DT1,null,false);
	}else if(msgc=="updateWhitoutMsg"){
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		var tempJson = convertJson.string2json1(data);
		var a=$("#DT1").getCurrentpagenum();	//获取当前页码
		tempJson.pages.currentpagenum=a;		//替换json的页码
		data = JSON.stringify(tempJson);
		defaultJson.doQueryJsonList(controllername+"?queryQyList",data,DT1,null,false);
	}

	
}

function doRandering_BSBF(obj){
	var showHtml = "";
	if(obj.BSBF!=""){
		showHtml = obj.BSBF_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_ZJEBF(obj){
	var showHtml = "";
	if(obj.ZJEBF!=""){
		showHtml = obj.ZJEBF_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_ZXSJBF(obj){
	var showHtml = "";
	if(obj.ZXSJBF!=""){
		showHtml = obj.ZXSJBF_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_BSSY(obj){
	var showHtml = "";
	if(obj.BSSY!=""){
		showHtml = obj.BSSY;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_ZJESY(obj){
	var showHtml = "";
	if(obj.ZJESY!=""){
		showHtml = obj.ZJESY_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}

function doRandering_ZXSJSY(obj){
	var showHtml = "";
	if(obj.ZXSJSY!=""){
		showHtml = obj.ZXSJSY_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}

<%--function closeParentCloseFunction(){--%>
<%--	//刷新--%>
<%--	g_bAlertWhenNoResult = false;--%>
<%--	//生成json串--%>
<%--	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);--%>
<%--	//调用ajax插入--%>
<%--	defaultJson.doQueryJsonList(controllername+"?queryQyList",data,DT1);--%>
<%--	g_bAlertWhenNoResult = true;--%>
<%--}--%>

function doRandering(obj){
	var showHtml = "";
	showHtml = "<a href='javascript:rowView1(this);' title='查看详细信息'><i class='icon-file showXmxxkInfo' jhsjid='"+obj.ID+"'></i></a>";
	return showHtml;
}
function doBD(obj){
	if(obj.BDMC==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">
				征收资金管理
				<span class="pull-right">
				<app:oPerm url="jsp/business/zjgl/zfzsb-addOneOnly.jsp?type=insert">
				<button id="btnInsertBF0" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">新增拨付</button>
				</app:oPerm>
				<app:oPerm url="jsp/business/zjgl/xmzszjqk-addOne1Only.jsp?type=insert">
				<button id="btnInsertSY0" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">新增使用</button>
				</app:oPerm>
				<app:oPerm url="jsp/business/zjgl/zfzsb-addOne.jsp?type=insert">
					<button id="btnInsertBF" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">拨付维护</button>
					</app:oPerm>
				<app:oPerm url="jsp/business/zjgl/xmzszjqk-addOne1.jsp?type=insert">
      				<button id="btnInsertSY" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">使用维护</button>
      				</app:oPerm>
				<app:oPerm url="jsp/framework/print/TabListEXP.jsp?zszj">
      				<button id="btnExpExcel"  noprint="true" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">导出</button>
      				</app:oPerm>
				</span>
			</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
<%--							<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>--%>
<%--							<INPUT type="text" class="span12" kind="text" id="ZBBM" fieldname="ZBBM" value="" operation="="/>--%>
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>
					 <th width="5%" class="right-border bottom-border text-right">年度</th>
						<td width="10%" class="right-border bottom-border">
							<select class="span12" type="date" fieldtype="year" fieldformat="yyyy" id="QND" kind="dic" src="XMNF"  defaultMemo="-全部-">
						</td>
						<th width="5%" class="right-border bottom-border text-right">项目名称</th>
						<td class="right-border bottom-border" width="18%">
							<input class="span12" type="text" id="QXMMC" name="XMMC" fieldname="XMMC" operation="like" >
						</td>
						<th width="5%" class="right-border bottom-border text-right">标段名称</th>
						<td class="right-border bottom-border" width="18%">
							<input class="span12" type="text" id="QBDMC" name="BDMC" fieldname="BDMC" operation="like" >
						</td>
						<td class="text-left bottom-border text-right">
	                        <button id="btnQuery" class="btn btn-link"  type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-search"></i>查询</button>
           					<button id="btnClear" class="btn btn-link" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-trash"></i>清空</button>
			            </td>						
					</tr>
				</table>
			</form>
			<div style="height:5px;"> </div>	
			<div class="overFlowX">
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single"  pageNum="18" printFileName="征收资金管理">
	                <thead>
	                	<tr>
	                		<th rowspan="2"  name="XH" id="_XH" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
	                		<th rowspan="2" fieldname="JHSJID" colindex=2 tdalign="center" CustomFunction="doRandering" noprint="true"> &nbsp;&nbsp;</th>
							<th rowspan="2" fieldname="XMMC" colindex=3 tdalign="left" maxlength="40">&nbsp;项目名称&nbsp;</th>
							<th rowspan="2" fieldname="BDMC" colindex=4 tdalign="left" maxlength="40" CustomFunction="doBD">&nbsp;标段名称&nbsp;</th>
							<th rowspan="2" fieldname="XMBDDZ" colindex=5 maxlength="17">&nbsp;项目地址&nbsp;</th>
							<th colspan="3">&nbsp;资金拨付&nbsp;</th>
							<th colspan="3">&nbsp;资金使用&nbsp;</th>
							<th rowspan="2" fieldname="YE" colindex=12 tdalign="right" maxlength="50">&nbsp;余额&nbsp;</th>
	                	</tr>
	                	<tr>
	                		<th fieldname="BSBF" colindex=6 tdalign="center" CustomFunction="doRandering_BSBF">&nbsp;笔数&nbsp;</th>
							<th fieldname="ZJEBF" colindex=7 tdalign="right" CustomFunction="doRandering_ZJEBF">&nbsp;<span id="currYear"></span>本年度拨入(元)&nbsp;</th>
							<th fieldname="ZXSJBF" colindex=8 tdalign="right" CustomFunction="doRandering_ZXSJBF">&nbsp;累计拨入(元)&nbsp;</th>
							<th fieldname="BSSY" colindex=9 tdalign="center" CustomFunction="doRandering_BSSY">&nbsp;笔数&nbsp;</th>
							<th fieldname="ZJESY" colindex=10 tdalign="right" CustomFunction="doRandering_ZJESY">&nbsp;本年度使用(元)&nbsp;</th>
							<th fieldname="ZXSJSY" colindex=11 tdalign="right" CustomFunction="doRandering_ZXSJSY">&nbsp;累计使用(元)&nbsp;</th>
	                	</tr>
	                </thead>
	              	<tbody></tbody>
	           </table>
	       </div>
	 	</div>
	</div>     
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
<%--		<input type="hidden" name="txtFilter" order="desc" fieldname="fdt.LRSJ" id="txtFilter"/>--%>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
</div>
</body>
</html>