<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>工程洽商-工程洽商管理</title>
<%
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String userid = user.getAccount();
	String username = user.getName();
	String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/gcqs/gcqsController.do";
var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
var rowIndex;
var xmglgs = '<%=deptId%>';

//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#gcqsList").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	
	setPageHeight();
	
	//自动完成项目名称模糊查询
	showAutoComplete("QXMMC",autocompleteXmmcController+"?xmmcAutoQueryByXmglgs&xmglgs="+xmglgs,"getXmmcQueryCondition"); 
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 if(exportRequireQuery($("#gcqsList"))){//该方法需传入表格的jquery对象
		      printTabList("gcqsList");
		  }
	});
	//按钮绑定事件(新增)
	$("#btnInsert").click(function() {
		$(window).manhuaDialog({"title":"工程洽商管理>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/gcqs/gcqs_xmwh.jsp?type=insert","modal":"1"});
	});
	//按钮绑定事件(修改)
	$("#btnUpdate").click(function() {
		if($("#gcqsList").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#gcqsList").getSelectedRow());
		$(window).manhuaDialog({"title":"工程洽商管理>修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/gcqs/gcqs_xmwh.jsp?type=update","modal":"1"});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
    });
  	//按钮绑定事件（呈请审批）
    $("#sp_btn").click(function() {
    	if($("#gcqsList").getSelectedRowIndex()==-1){
			requireSelectedOneRow();
			return;
		}
    	var obj = convertJson.string2json1($("#gcqsList").getSelectedRow());
    	var qsid = obj.GC_GCGL_GCQS_ID;
    	var sjbh = obj.SJBH;
    	var ywlx = obj.YWLX;
    	var sfpq = obj.SFSJPQ;
    	if(obj.EVENTSJBH!=0&&obj.EVENTSJBH!=7){
 			var isview = "1";
 		    var isOver = getProIsover(sjbh,ywlx);
 		    if(isOver =="1"){
 		    	isview = "0";
 		    }
 			var s = "${pageContext.request.contextPath}/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview="+isview;   
 		  	$(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});  
 		}else{
 			// templateid 一个为24，一个为25；operationoid一个为43480，一个为43481    add  by  xhb
 	    	if(sfpq == 1){
 	 			var wsActionURL = "/xmgl/PubWS.do?getXMLPrintAction|templateid=24|isrefresh=1|isEdit=0|ywlx="+ywlx+"|sjbh="+sjbh+"|qsid="+qsid+"|rowid="+Math.random()+".mht";
 	            wsActionURL =  "/xmgl/jsp/framework/print/pubPrint.jsp?param="+wsActionURL+
 	            		"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid=24&isEdit=0&operationoid=43480&processtype=1";
 	            $(window).manhuaDialog({"title":"流程申请>工程洽商审核意见单（ 有拆迁）","type":"text","content":wsActionURL,"modal":"1"});
 	    	}else{
 	 			var wsActionURL = "/xmgl/PubWS.do?getXMLPrintAction|templateid=25|isrefresh=1|isEdit=0|ywlx="+ywlx+"|sjbh="+sjbh+"|qsid="+qsid+"|rowid="+Math.random()+".mht";
 	            wsActionURL =  "/xmgl/jsp/framework/print/pubPrint.jsp?param="+wsActionURL+
 	            		"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid=25&isEdit=0&operationoid=43481&processtype=1";
 	            $(window).manhuaDialog({"title":"流程申请>工程洽商审核意见单（ 无拆迁）","type":"text","content":wsActionURL,"modal":"1"});
 	    	}
 		}
    	
    });
	
});
//页面默认参数
function init(){
	getNd();
//	$("#XMGLGS").val('<%=deptId%>');
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}
//默认年度
function getNd(){
	setDefaultNd("qnd");
	//setDefaultOption($("#qnd"),new Date().getFullYear());
}
//审批结束回调
function gengxinchaxun(obj)
{
		xAlertMsg(obj);
}
//回调函数
getWinData = function(data){

	var data1 = defaultJson.packSaveJson(data);
	if(convertJson.string2json1(data).GC_GCGL_GCQS_ID == "" || convertJson.string2json1(data).GC_GCGL_GCQS_ID == null){
		defaultJson.doInsertJson(controllername + "?insert&ywid="+convertJson.string2json1(data).YWID, data1,gcqsList);
		$("#gcqsList").setSelect(0);
		var obj = convertJson.string2json1($("#gcqsList").getSelectedRow());
		btnSel(obj);
		
	}else{
		defaultJson.doUpdateJson(controllername + "?insert", data1,gcqsList);
		
	}
	
};
//呈请审批回调函数
function prcCallback(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcqsList);
	data = defaultJson.getQueryConditionWithNowPageNum(data,"gcqsList");
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,gcqsList);
	var obj = convertJson.string2json1($("#gcqsList").getSelectedRow());
	btnSel(obj);
}

function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcqsList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,gcqsList);
	
}
//项目名称自动模糊查询参数
function getXmmcQueryCondition(){
	var initData = '{"querycondition":{"conditions":[]}}';
	var jsonData = convertJson.string2json1(initData);
	//年度
	if("" != $("#qnd").val()){
		var defineCondition = {"value": $("#qnd").val(),"fieldname":"t.ND","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	//项目名称
	if("" != $("#QXMMC").val()){
		var defineCondition = {"value": "%"+$("#QXMMC").val()+"%","fieldname":"t.XMMC","operation":"like","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	if("" != $("#XMGLGS").val()){
		var defineCondition = {"value": +$("#XMGLGS").val(),"fieldname":"t.XMGLGS","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	return JSON.stringify(jsonData);
}
//按钮权限控制
function btnSel(obj){
	if(obj.EVENTSJBH == "3"){
		$("#sp_btn").html("审批信息");
		$("#sp_btn").attr("disabled",false);
		$("#btnUpdate").attr("disabled",true);
	}
	if(obj.EVENTSJBH == "0"){//登记完成
		$("#sp_btn").attr("disabled",false);
		$("#btnUpdate").attr("disabled",false);
		$("#sp_btn").html("呈请审批");
	}else if(obj.EVENTSJBH == "1"){//审批中
		$("#sp_btn").html("审批信息");
		$("#sp_btn").attr("disabled",false);
		$("#btnUpdate").attr("disabled",true);
	}else if(obj.EVENTSJBH == "2"){//审批通过
		$("#sp_btn").html("审批信息");
		$("#sp_btn").attr("disabled",false);
		$("#btnUpdate").attr("disabled",true);
	}else if(obj.EVENTSJBH == "7"){//审批退回
		$("#sp_btn").html("再次呈请");
		$("#sp_btn").attr("disabled",false);
		$("#btnUpdate").attr("disabled",false);
	}
}
//点击行事件
function tr_click(obj){
	btnSel(obj);
}
//详细信息
function rowView(index){
	var obj = $("#gcqsList").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).XMID;
	$(window).manhuaDialog(xmscUrl(id));
}
//标段图标样式
function doBdmc(obj){
	if(obj.XMBS == "0"){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}

//删除返回函数
function getWinData_del(data){
	var data1 = defaultJson.packSaveJson(data);
	defaultJson.doDeleteJson(controllername+"?delete",data1,gcqsList,null); 
}

//标段编号图标样式
function doBdbh(obj){
	if(obj.BDID == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDBH;
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
      <h4 class="title">工程洽商
      	<span class="pull-right">
      		<app:oPerm url="jsp/business/gcb/gcqs/gcqs_xmwh.jsp?type=insert">
      			<button id="btnInsert" class="btn" type="button">新增</button>
      		</app:oPerm>
      		<app:oPerm url="jsp/business/gcb/gcqs/gcqs_xmwh.jsp?type=update">
      			<button id="btnUpdate" class="btn" type="button">修改</button>
      		</app:oPerm>
      		<app:oPerm url="jsp/framework/common/aplink/defaultArchivePage.jsp?type=gcqs">
      			<button id="sp_btn" class="btn"  type="button">呈请审批</button>
      		</app:oPerm>
      		<button id="btnExpExcel" class="btn"  type="button">导出</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<input type="text" id="XMGLGS" name="XMGLGS" fieldname="t1.XMGLGS" keep="true" operation="=" />
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
          <th width="5%" class="right-border bottom-border text-right">年度</th>
          <td class="right-border bottom-border" width="6%">
            <select class="span12 year" id="qnd" name = "QND" defaultMemo="全部" fieldname = "to_char(t.QSTCRQ,'yyyy')" operation="=" kind="dic" src="T#GC_GCGL_GCQS: distinct TO_CHAR(QSTCRQ,'yyyy') as JNRQ:TO_CHAR(QSTCRQ,'yyyy') as x:SFYX='1' ORDER BY JNRQ ASC">
            </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">项目名称</th>
          <td class="right-border bottom-border" width="15%"><input
				class="span12" type="text" placeholder="" name="QXMMC"
				fieldname="t1.XMMC" operation="like" id="QXMMC"
				autocomplete="off" tablePrefix="t">
		  	</td>
		<th width="5%" class="right-border bottom-border text-right">洽商标题</th>
          <td class="right-border bottom-border" width="20%">
          	<input class="span12" type="text" name="QQSBT"
			fieldname="t.QSBT" operation="like" id="QSBT" >
          </td>
          <td class="text-left bottom-border text-right">
           <button id="btnQuery" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
           <button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
          </td>
         </tr>
      </table>
      </form>
<div style="height:5px;"> </div>
<div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="gcqsList" width="100%" type="single" pageNum="10" printFileName="工程洽商">
		<thead>
			<tr>
				<th  name="XH" id="_XH" >&nbsp;#&nbsp;</th>
				<!-- <th fieldname="EVENTSJBH" tdalign="center">&nbsp;当前状态&nbsp;</th> -->
				<th fieldname="XMBH" hasLink="true" linkFunction="rowView" >&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
				<th fieldname="QSBT" maxlength="15">&nbsp;洽商标题&nbsp;</th>
				<th fieldname="BDBH" CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" maxlength="15" CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
				<th fieldname="XMBDDZ" maxlength="15" >&nbsp;项目地址&nbsp;</th>
				<th fieldname="SJDW" maxlength="15">&nbsp;设计单位&nbsp;</th>
				<th fieldname="JLDW" maxlength="15">&nbsp;监理单位&nbsp;</th>
				<th fieldname="SGDW" maxlength="15">&nbsp;施工单位&nbsp;</th>
				<th fieldname="GSZJ" tdalign="right">&nbsp;估算造价&nbsp;</th>
				<th fieldname="SFSJPQ" tdalign="center">&nbsp;是否涉及拆迁&nbsp;</th>
				<th fieldname="QSTCRQ" tdalign="center">&nbsp;洽商提出日期&nbsp;</th>
			</tr>
		</thead>
		<tbody>
           </tbody>
	</table>
</div>
		</div>
	</div>
   </div>
    
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="DESC" fieldname = "t.BLRQ"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>