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
<title>项目管理公司-形象进度</title>
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
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/tiBaoJieSuanController.do";
//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#xxjdList").attr("pageNum",pageNum);
}
//页面初始化
$(function() {
	setPageHeight();
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        initCommonQueyPage();
    });
	//导出Excel
	var tbjs=$("#tbjs");
	tbjs.click(function(){
		if($("#xxjdList").getSelectedRowIndex()==-1){
        	requireSelectedOneRow();
            return;
    	}else{
		 	xConfirm("提示信息","是否对选中项目提报结算！");
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').one("click",function(){ 
				//生成json串
				var data=$("#xxjdList").getSelectedRow();
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//调用ajax插入
			 	defaultJson.doUpdateJson(controllername + "?insertTBJSZT",data1, xxjdList,"addHuiDiao");
		 	});
			} 
	 });
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 if(exportRequireQuery($("#xxjdList"))){//该方法需传入表格的jquery对象
		      printTabList("xxjdList");
		  }
	});
});
//添加回调
function addHuiDiao(){
	$("#tbjs").attr("disabled",true);
}
//页面默认参数
function init(){
	initCommonQueyPage();
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}
//行选事件
function tr_click(obj){
	if(obj.TBJSZT == ""){
		$("#tbjs").attr("disabled",false);
	}else{
		$("#tbjs").attr("disabled",true);
	}
}
//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xxjdList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryTiBaoJieSuan",data,xxjdList);
}
//详细信息
function rowView(index){
	var obj = $("#xxjdList").getSelectedRowJsonByIndex(index);
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
//标段编号图标样式
function doBdbh(obj){
	if(obj.BDBH == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDBH;
	}
}
//提报结算状态
function doZt(obj){
	if(obj.TBJSZT == ""){
		return "<div style='text-align:center'>未提交</div>";
	}else{
		return "<div style='text-align:center'>已提交</div>";
	}
}
//导出Excel
var excel=$("#excel");
excel.click(function(){
	 if(exportRequireQuery($("#xxjdList"))){//该方法需传入表格的jquery对象
	      printTabList("xxjdList");
	  }
});

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">提报结算管理
      	<span class="pull-right">
      	<app:oPerm url="jsp/business/xmglgs/xxjd/xxjd_tbjs.jsp"> 
      		<button id="tbjs" class="btn" type="button">提报结算</button>
  		 </app:oPerm> 
  			<button id="btnExpExcel" class="btn"  type="button">导出</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
        <tr>
        <jsp:include page="/jsp/business/common/commonQuery.jsp" flush="true">
         	<jsp:param name="prefix" value="jhsj"/> 
         </jsp:include>
			<td class="text-left bottom-border text-right">
	        	<button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
	        	<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
	        </td>
		</tr>
      </table>
      </form>
<div style="height:5px;"> </div>
<div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="xxjdList" width="100%" type="single" pageNum="10" printFileName="形象进度">
		<thead>
			<tr>
				<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
				<th fieldname="XMBH" rowspan="2" colindex=2 rowMerge="true" hasLink="true" linkFunction="rowView" >&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" rowspan="2" colindex=3 maxlength="15" rowMerge="true">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDBH" rowspan="2" colindex=4 CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" rowspan="2" colindex=5 CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
				<th fieldname="XMBDDZ" rowspan="2" colindex=6 >&nbsp;项目地址&nbsp;</th>
				<th fieldname="TBJSZT" rowspan="2" colindex=7  CustomFunction="doZt">&nbsp;状态&nbsp;</th>
				<th colspan="2">&nbsp;计划时间&nbsp;</th>
				<th colspan="2">&nbsp;实际时间&nbsp;</th>
			</tr>
			<tr>
				<th fieldname="KGSJ" colindex=8 tdalign="center">&nbsp;开工&nbsp;</th>
				<th fieldname="WGSJ" colindex=9 tdalign="center">&nbsp;完工&nbsp;</th>
				<th fieldname="KGSJSJ" colindex=10 tdalign="center">&nbsp;开工&nbsp;</th>
				<th fieldname="WGSJSJ" colindex=11 tdalign="center">&nbsp;完工&nbsp;</th>
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
       <input type="hidden" name="txtFilter"  order="ASC" fieldname = "JHSJ.XMBH,JHSJ.XMBS,JHSJ.PXH"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>