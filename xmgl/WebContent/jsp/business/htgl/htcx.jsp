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
<title>项目选择</title>
<%
	String type=request.getParameter("type");

	String HTZT=request.getParameter("HTZT");
	
	String optype=request.getParameter("optype");
	
	String url = "queryHt";
	if(HTZT==null){
	    HTZT = "";
	}
	String ZBBMID=request.getParameter("ZBBMID");
	if(ZBBMID==null){
	    ZBBMID = "";
	}
	if("tqkbmmx".equals(optype==null?"":optype)){
		url = "queryHtOnlyTqkbm";
	}
	
	
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
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtController.do";
var url = '<%=url%>';
//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(7)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#htList").attr("pageNum",pageNum);
}


//自定义的获取页面查询条件
function getQueryCondition(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,htList);
    return data;
}

function queryList(){
	//生成json串
	var data = getQueryCondition();
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?"+url,data,htList);
	
	
	//$("tbody tr td", $("#htList")).dblclick( function () { alert("Hello World!"); });
	$("tbody tr td", $("#htList")).bind( "dblclick", function() {$("#btnQd").click();});
}

//页面初始化
$(function() {
	setPageHeight();
	initPage();
	queryList();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        //其他处理放在下面
        initPage();
    });
  	//按钮绑定事件（确定）
    $("#btnQd").click(function() {
    	if($("#htList").getSelectedRowIndex()==-1)
		 {
    		xInfoMsg('请选择一条记录！',"");
		    return
		 }
        var rowValue = $("#htList").getSelectedRow();//获得选中行的json对象
        $(window).manhuaDialog.setData(rowValue);
		$(window).manhuaDialog.sendData();
		$(window).manhuaDialog.close();
    });
});



function initPage(){
	
	//设置查询条件
	if('<%=HTZT%>'!=""){
		$("#QHTZT").val('<%=HTZT%>');
	}
	if('<%=ZBBMID%>'!=""){
		$("#QZBBMID").val('<%=ZBBMID%>');
	}
	
	//initCommonQueyPage();
}

function doBDMC(obj){
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
      <h4 class="title">合同信息
      <span class="pull-right">
      	<button id="btnQd" class="btn" type="button" style="display:black;">确定</button>
      </span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
			<TD class="right-border bottom-border">
				<input type="hidden" id="QZBBMID" name="ZBBMID"  fieldname="ghh.ZBBMID" operation="=" />
				<input type="hidden" id="QHTZT" name="HTZT"  fieldname="ghh.HTZT" operation="=" />
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
         <tr>
         	<th width="5%" class="right-border bottom-border text-right">合同编码</th>
			<td class="right-border bottom-border">
				<input class="span12" type="text" id="QHTBM" name="HTBM" fieldname="HTBM" operation="like" >
			</td>
			<th width="5%" class="right-border bottom-border text-right">合同名称</th>
			<td class="right-border bottom-border">
				<input class="span12" type="text" id="QHTMC" name="HTMC" fieldname="HTMC" operation="like" >
			</td>
			<th width="5%" class="right-border bottom-border text-right">乙方单位</th>
			<td class="right-border bottom-border">
				<input class="span12" type="text" id="Q"YFDW"" name=""YFDW"" fieldname="YFDW" operation="like" >
			</td>
         </tr>
         <tr>
			<th width="5%" class="right-border bottom-border text-right">合同类型</th>
			<td  class="right-border bottom-border">
				<select  id="HTLX"  class="span7"  name="HTLX" fieldname="HTLX"  operation="=" kind="dic" src="HTLX"   defaultMemo="-请选择-">
			</td>
			<th width="5%" class="right-border bottom-border text-right">项目名称</th>
			<td class="right-border bottom-border">
				<input class="span12" type="text" id="QXMMC" name="XMMC" fieldname="XMMC" operation="like" >
			</td>
			<td width="10%" class="right-border bottom-border">
				<select class="span12" id="QND" name="ND" fieldname="ND" operation="=" kind="dic" src="XMNF"  defaultMemo="-年度-">
			</td>
			
	          <td class="text-left bottom-border text-right">
	        	<button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
	        	<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
	          </td>
         </tr>
      </table>
      </form>
	<div style="height:5px;"> </div>
	<table width="100%" class="table-hover table-activeTd B-table" id="htList" type="single" pageNum="10">
	<thead>
		<tr>
			<th name="XH" id="_XH" style="width:10px">&nbsp;#&nbsp;</th>
			<th fieldname="HTBM" colindex=3 tdalign="center" maxlength="30">&nbsp;合同编码&nbsp;</th>
			<th fieldname="HTMC" colindex=4 tdalign="center" maxlength="30">&nbsp;合同名称&nbsp;</th>
			<th fieldname="HTLX" colindex=1 tdalign="center" maxlength="30">&nbsp;合同类型&nbsp;</th>
			<th fieldname="YFDW" colindex=5 tdalign="center" maxlength="36">&nbsp;乙方单位&nbsp;</th>
			<th fieldname="ZHTQDJ" colindex=6 tdalign="center" maxlength="17">&nbsp;合同签订价(元)&nbsp;</th>
<%--			<th fieldname="HTJQDRQ" colindex=7 tdalign="center" maxlength="10">&nbsp;合同签订日期&nbsp;</th>--%>
			<th fieldname="XMMC" colindex=2 tdalign="center" maxlength="36">&nbsp;项目名称&nbsp;</th>
			<th fieldname="BDMC" colindex=3 tdalign="center" maxlength="36" CustomFunction="doBDMC">&nbsp;标段名称&nbsp;</th>
			<th fieldname="HTQDJ" colindex=6 tdalign="center" maxlength="17">&nbsp;标段合同签订价(元)&nbsp;</th>
			<th fieldname="HTZF" colindex=6 tdalign="center" maxlength="17">&nbsp;标段支付(元)&nbsp;</th>
			<th fieldname="WCZF" colindex=6 tdalign="center" maxlength="17">&nbsp;标段完成投资(元)&nbsp;</th>
	  </tr>
	</thead>
	<tbody>
    </tbody>
</table>
</div>
    </div>
</div>

  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "ghh.LRSJ"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 </FORM>
 </div>
</body>
</html>