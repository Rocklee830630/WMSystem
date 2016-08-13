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
<title>开复工令-指令管理</title>
<%
	String id = request.getParameter("id");
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
var controllername= "${pageContext.request.contextPath }/kfgl/kfglController.do";
var id = "<%= id%>";
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		var kfg = "";
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,kfglList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryKfgById&kfg="+kfg,data,kfglList);
	});
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 if(exportRequireQuery($("#kfglList"))){//该方法需传入表格的jquery对象
		      printTabList("kfglList","kfgl_view.xls","XMBH,XMMC,BDMC,KGFG,GCSL,JGNR,BLSJ","2,1");
		  }
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        clearCkeckBox();
        //其他处理放在下面
    });
	
});
//页面默认参数
function init(){
	$("#id").val(id);
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
	
}

//查询列表
function queryList(){
	var kfg = "";
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,kfglList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryKfgById&kfg="+kfg,data,kfglList);
}

//标段图标样式
function doBdmc(obj){
	if(obj.XMBS == "0"){
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
     <h4 class="title">开复工令
      	<span class="pull-right">
	  		<button id="btnExpExcel" class="btn"  type="button">导出</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<input type="text" id="id" name="TCJHSJID" fieldname="t3.TCJHSJID" keep="true" operation="="/>
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
       
      </table>
      </form>
    <div style="height:5px;"> </div>
	<table class="table-hover table-activeTd B-table" id="kfglList" width="100%" type="single" pageNum="5">
		<thead>
			<tr>
				<th  name="XH" id="_XH">&nbsp;#&nbsp;</th>
				<th fieldname="XMBH">&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" maxlength="25">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDMC" maxlength="25" CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
				<th fieldname="KGFG" tdalign="center">&nbsp;开工/复工/停工&nbsp;</th>
				<th fieldname="GCSL" >&nbsp;工程数量&nbsp;</th>
				<th fieldname="JGNR" maxlength="15">&nbsp;结构内容&nbsp;</th>
				<th fieldname="BLSJ" tdalign="center">&nbsp;办理时间&nbsp;</th>
				<th fieldname="FJ" tdalign="center" noprint="true">&nbsp;附件&nbsp;</th>
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
 	   <input type="hidden" name="ywid" id = "ywid" value="">
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="DESC" fieldname = "t3.BLSJ"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>