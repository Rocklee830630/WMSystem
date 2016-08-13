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
<title>工程部-项目甩项管理</title>
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
var controllername= "${pageContext.request.contextPath }/gcsx/gcsxController.do";
var rowIndex;


//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#gcsxList").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	
	setPageHeight();
	
	//自动完成项目名称模糊查询
	//showAutoComplete("QXMMC",controllername+"?xmmcAutoCompleteByYw","getXmmcQueryCondition"); 
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 if(exportRequireQuery($("#gcsxList"))){//该方法需传入表格的jquery对象
		      printTabList("gcsxList");
		  }
	});
	//按钮绑定事件(新增)
	$("#btnInsert").click(function() {
		$(window).manhuaDialog({"title":"工程甩项管理>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/gcsx/gcsx_xmwh.jsp?type=insert&fqlx=2","modal":"1"});
	});
	//按钮绑定事件(修改)
	$("#btnUpdate").click(function() {
		if($("#gcsxList").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#gcsxList").getSelectedRow());
		$(window).manhuaDialog({"title":"工程甩项管理>修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/gcsx/gcsx_xmwh.jsp?type=update&fqlx=2","modal":"1"});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
    });
  	//按钮绑定事件（呈请审批）
    $("#sp_btn").click(function() {
    	if($("#gcsxList").getSelectedRowIndex()==-1){
			requireSelectedOneRow();
			return;
		}
    	var obj = convertJson.string2json1($("#gcsxList").getSelectedRow());
    	var qsid = obj.GC_GCGL_GCQS_ID;
    	var sjbh = obj.SJBH;
    	var ywlx = obj.YWLX;
    	var sfpq = obj.SFSJPQ;
    	
    	if(obj.EVENTSJBH==0) {//流程发起时，用发起的页面
    		// 当前登录人不是此甩项信息录入人，禁止发起流程 add by xiahongbo on 2015-01-08 start 
 	    	var lrr = obj.LRR;
 			if(lrr !== '<%=userid %>') {
 				requireFormMsg("当前登录人不是此甩项信息录入人，禁止发起流程!");
 	    		return;
 	    	}
 			// 当前登录人不是此甩项信息录入人，禁止发起流程 add by xiahongbo on 2015-01-08 end 
 			
 			// templateid 一个为20，一个为22；operationoid一个为43480，一个为43481    add  by  xhb
 	    	if(sfpq == 1){
 	    		var wsActionURL = "/xmgl/PubWS.do?getXMLPrintAction|templateid=20|isrefresh=1|isEdit=0|ywlx="+ywlx+"|sjbh="+sjbh+"|rowid="+Math.random()+".mht";
 	            wsActionURL =  "/xmgl/jsp/framework/print/pubPrint.jsp?param="+wsActionURL+
 	            		"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid=20&isEdit=0&operationoid=43476&processtype=1";
 	            $(window).manhuaDialog({"title":"工程部甩项申请会签单（有拆迁）","type":"text","content":wsActionURL,"modal":"1"});

 	    	}else{
 	    		var wsActionURL = "/xmgl/PubWS.do?getXMLPrintAction|templateid=22|isrefresh=1|isEdit=0|ywlx="+ywlx+"|sjbh="+sjbh+"|rowid="+Math.random()+".mht";
 	            wsActionURL =  "/xmgl/jsp/framework/print/pubPrint.jsp?param="+wsActionURL+
 	            		"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid=22&isEdit=0&operationoid=43479&processtype=1";
 	            $(window).manhuaDialog({"title":"工程部甩项申请会签单（无拆迁）","type":"text","content":wsActionURL,"modal":"1"});

 	    	}
    	} else if(obj.EVENTSJBH==7) {//流程退回时，用退回的页面
    		// 当前登录人不是此甩项信息录入人，禁止发起流程 add by xiahongbo on 2015-01-08 start 
 	    	var lrr = obj.LRR;
 			if(lrr !== '<%=userid %>') {
 				requireFormMsg("当前登录人不是此甩项信息录入人，禁止发起流程!");
 	    		return;
 	    	}
 			// 当前登录人不是此甩项信息录入人，禁止发起流程 add by xiahongbo on 2015-01-08 end 
 			
    		var value=queryLcxx(sjbh);
 			var tempJson = convertJson.string2json1(value);
 			var rowObject = tempJson.response.data[0];
 		   	var url =  '${pageContext.request.contextPath}/'+rowObject.LINKURL+'?taskid='+rowObject.ID+'&taskseq='+rowObject.SEQ+'&sjbh='+rowObject.SJBH+'&ywlx='+rowObject.YWLX+'&spbh='+rowObject.SPBH+'&rwlx='+rowObject.RWLX+"&isRead="+rowObject.XB;
 		   	$(window).manhuaDialog({"title":"审批信息","type":"text","content":url,"modal":"1"});
    	} else {
    		var isview = "1";
 		    var isOver = getProIsover(sjbh,ywlx);
 		    if(isOver =="1"){
 		    	isview = "0";
 		    }
 			var s = "${pageContext.request.contextPath}/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview="+isview;   
 		  	$(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});  
    	}
    	
    });
  	//按钮绑定事件（删除）
    $("#btnDelete").click(function(){
    	if($("#gcsxList").getSelectedRowIndex()==-1){
			requireSelectedOneRow();
			return;
		}
    	//var obj = convertJson.string2json1($("#gcsxList").getSelectedRow());
    	var data = $("#gcsxList").getSelectedRow();
		var data1 = defaultJson.packSaveJson(data);
		xConfirm("提示信息","是否确认删除！");
		$('#ConfirmYesButton').unbind();
		$('#ConfirmYesButton').one("click",function(){  
			defaultJson.doDeleteJson(controllername+"?delete",data1,gcsxList,null); 
		});
    });
});

function queryLcxx(sjbh){
	var str = "";
	$.ajax({
		url: "${pageContext.request.contextPath }/TaskAction.do?getFwLc&sjbh="+sjbh,
		data:"",
		dataType:"json",
		async:false,
		success:function(result){
			str = result.msg;
		}
	});
	return str;
}
//页面默认参数
function init(){
	getNd();
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
		
		var row_index=$("#gcsxList").getSelectedRowIndex();
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcsxList);
		var tempJson = convertJson.string2json1(data);
		var a=$("#gcsxList").getCurrentpagenum();
		tempJson.pages.currentpagenum=a;
		data = JSON.stringify(tempJson);
		defaultJson.doQueryJsonList(controllername+"?query",data,gcsxList);
		$("#gcsxList").setSelect(row_index);
		var selObj = $("#gcsxList").getSelectedRowJsonObj();//获得选中行的索引
		tr_click(selObj, gcsxList);
}
//回调函数
getWinData = function(data){

	var data1 = defaultJson.packSaveJson(data);
	if(convertJson.string2json1(data).GC_GCGL_GCSX_ID == "" || convertJson.string2json1(data).GC_GCGL_GCSX_ID == null){
		defaultJson.doInsertJson(controllername + "?insert&ywid="+convertJson.string2json1(data).YWID, data1,gcsxList);
		$("#gcsxList").setSelect(0);
		var obj = convertJson.string2json1($("#gcsxList").getSelectedRow());
		//btnSel(obj);
		
	}else{
		defaultJson.doUpdateJson(controllername + "?insert", data1,gcsxList);
		
	}
	
};
//呈请审批回调函数
function prcCallback(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcsxList);
	data = defaultJson.getQueryConditionWithNowPageNum(data,"gcsxList");
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,gcsxList);
	var obj = convertJson.string2json1($("#gcsxList").getSelectedRow());
	btnSel(obj);
}

function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcsxList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,gcsxList);
	
}
//项目名称自动模糊查询参数
function getXmmcQueryCondition(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcsxList);
	return data;
}
//按钮权限控制
function btnSel(obj){
	if(obj.EVENTSJBH == "3"){
		$("#sp_btn")[0].innerText = "审批信息";
		$("#sp_btn").attr("disabled",false);
		$("#btnUpdate").attr("disabled",true);
		$("#btnDelete").attr("disabled",true);
	}
	if(obj.EVENTSJBH == "0"){//登记完成
		$("#sp_btn").attr("disabled",false);
		$("#btnUpdate").attr("disabled",false);
		$("#btnDelete").attr("disabled",false);
		$("#sp_btn")[0].innerText = "呈请审批";
	}else if(obj.EVENTSJBH == "1"){//审批中
		$("#sp_btn")[0].innerText = "审批信息";
		$("#sp_btn").attr("disabled",false);
		$("#btnUpdate").attr("disabled",true);
		$("#btnDelete").attr("disabled",true);
	}else if(obj.EVENTSJBH == "2"){//审批通过
		$("#sp_btn")[0].innerText = "审批信息";
		$("#sp_btn").attr("disabled",false);
		$("#btnUpdate").attr("disabled",true);
		$("#btnDelete").attr("disabled",true);
	}else if(obj.EVENTSJBH == "7"){//审批退回
		$("#sp_btn")[0].innerText = "再次呈请";
		$("#sp_btn").attr("disabled",false);
		$("#btnUpdate").attr("disabled",false);
		$("#btnDelete").attr("disabled",false);
	}
}
//点击行事件
function tr_click(obj){
	btnSel(obj);
}
//详细信息
function rowView(index){
	var obj = $("#gcsxList").getSelectedRowJsonByIndex(index);
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
	defaultJson.doDeleteJson(controllername+"?delete",data1,gcsxList,null); 
}

//标段编号图标样式
function doBdbh(obj){
	if(obj.BDID == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDBH;
	}
}
//信息卡
function doRandering(obj){
	var showHtml = "";
	showHtml = "<a href='javascript:void(0)' title='工程甩项信息卡' onclick='openInfoCard()'><i class='icon-file showXmxxkInfo'  ></i></a>";
	return showHtml;
}
//信息卡事件
function openInfoCard(){
	var index = $(event.target).closest("tr").index();
	$("#gcsxList").cancleSelected();
	$("#gcsxList").setSelect(index);
	if($("#gcsxList").getSelectedRowIndex()==-1){
		requireSelectedOneRow();
		return;
	}else{
		$("#resultXML").val($("#gcsxList").getSelectedRow());//获得选中行的json 字符串
		$(window).manhuaDialog({"title":"工程甩项信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/gcsx/gcsx_xmxx.jsp","modal":"1"});
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
      <h4 class="title">工程甩项
      	<span class="pull-right">
      		<app:oPerm url="jsp/business/gcb/gcsx/gcsx_xmwh.jsp?type=insert&fqlx=2">
      			<button id="btnInsert" class="btn" type="button">新增</button>
      		</app:oPerm>
      		<app:oPerm url="jsp/business/gcb/gcsx/gcsx_xmwh.jsp?type=update&fqlx=2">
      			<button id="btnUpdate" class="btn" type="button">修改</button>
      		</app:oPerm>
      		<app:oPerm url="jsp/framework/common/aplink/defaultArchivePage.jsp?type=gcsx_gc">
      			<button id="sp_btn" class="btn"  type="button">呈请审批</button>
      		</app:oPerm>
      		<app:oPerm url="jsp/business/gcb/gcsx/gcsx_xmwh.jsp?type=delete&fqlx=2">
      			<button id="btnDelete" class="btn" type="button">删除</button>
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
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
          <th width="5%" class="right-border bottom-border text-right">年度</th>
          <td class="right-border bottom-border" width="6%">
            <select class="span12 year" id="qnd" name = "QND" defaultMemo="全部" fieldname = "to_char(t.SXSQRQ,'yyyy')" operation="=" kind="dic" src="T#GC_GCGL_GCSX: distinct TO_CHAR(SXSQRQ,'yyyy') as JNRQ:TO_CHAR(SXSQRQ,'yyyy') as x:SFYX='1' ORDER BY JNRQ ASC">
            </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">项目名称</th>
          <td class="right-border bottom-border" width="20%"><input
			class="span12" type="text" placeholder="" name="QXMMC"
			fieldname="t1.XMMC" operation="like" id="QXMMC"
			autocomplete="off" tablePrefix="t1" tableName="GC_GCGL_GCQS"></td> 
		  <th width="5%" class="right-border bottom-border text-right">业主代表</th>
		  <td class="bottom-border right-border">
			<select class="span12" id="GCBYZDB" operation="=" kind="dic" src="T#VIEW_YW_ORG_PERSON:ACCOUNT:NAME:PERSON_KIND = '3' and DEPARTMENT in (select row_id from VIEW_YW_ORG_DEPT where EXTEND1='2')" fieldname="t.GCBYZDB" name="GCBYZDB">
			</select>
 	 	  </td>
 	 	  <th width="5%" class="right-border bottom-border text-right">审批状态</th>
          <td class="right-border bottom-border" width="12%">
            <select class="span12" name = "SJZT" fieldname = "t2.SJZT" defaultMemo="全部" operation="=" kind="dic" src="SJZT">
            </select>
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
	<table class="table-hover table-activeTd B-table" id="gcsxList" width="100%" type="single" pageNum="10" printFileName="工程甩项">
		<thead>
			<tr>
				<th  name="XH" id="_XH" >&nbsp;#&nbsp;</th>
				<th fieldname="XMBH"  tdalign="center" CustomFunction="doRandering"  noprint="true"></th>
				<th fieldname="SJZT" tdalign="center">&nbsp;当前状态&nbsp;</th>
				<th fieldname="XMBH" hasLink="true" linkFunction="rowView" >&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDMC" maxlength="15" CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
				<th fieldname="XMBDDZ" maxlength="15">&nbsp;项目地址&nbsp;</th>
				<th fieldname="SXSQRQ" tdalign="center">&nbsp;甩项申请日期&nbsp;</th>
				<th fieldname="SGDW" maxlength="15">&nbsp;施工单位&nbsp;</th>
				<th fieldname="SGDWXMJL" tdalign="left">&nbsp;项目经理&nbsp;</th>
				<th fieldname="JLDW" maxlength="15">&nbsp;监理单位&nbsp;</th>
				<th fieldname="JLDWZJ" tdalign="left">&nbsp;项目总监&nbsp;</th>
				<th fieldname="GCBYZDB" tdalign="left">&nbsp;工程部业主代表&nbsp;</th>
				<th fieldname="LRR" tdalign="left">&nbsp;录入人&nbsp;</th>
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
       <input type="hidden" name="txtFilter"  order="DESC" fieldname = "t.LRSJ"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>