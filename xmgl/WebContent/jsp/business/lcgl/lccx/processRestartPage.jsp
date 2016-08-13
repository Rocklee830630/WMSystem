<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.coreapp.aplink.*"%>
<%@ page import="com.ccthanking.framework.coreapp.aplink.Process"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.framework.common.QuerySet"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>

<%@ page import="java.sql.Connection"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>

<%@ taglib uri= "/tld/base.tld" prefix="app" %>


<%
	String processId = request.getParameter("processId");
	String ywlx = request.getParameter("ywlx");
	String sjbh = request.getParameter("sjbh");
	
	String sql = "select * from (select distinct processoid,isms,ap.deptid, stepsequence,  ap.name processname,ap.resultdscr, " +
			"rolename, actor pname1, state, createtime, closetime, processtime,actor,taskseq " +
			"from ap_processdetail ap ,fs_org_role_psn_map rp " +
			"where processoid = '"+processId+"' and ap.rolename=rp.role_id(+) order by ap.stepsequence)";

	QuerySet qs = DBUtil.executeQuery(sql, null);
	
	String fqrSql = "select resultdscr,cjrid,cjdwdm from AP_PROCESSINFO t where processoid='"+processId+"'";
	QuerySet fqrQs = DBUtil.executeQuery(fqrSql, null);
%>
<head>

<app:base/>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script type="text/javascript">
var controllername= '${pageContext.request.contextPath }/ProcessJumpAction.do';

var blrxm = "";
$(function() {
    $("#jumpStepTest").click(function(){

    	if(!isChoose) {
    		xAlert("提示信息",'请选中符合条件的节点','3');
			return;
    	}
    	
			xConfirm("提示信息","是否确认重启流程。从"+restartStep+"开始");
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').one("click",function() {
		    	var data1 = "stepsequence="+stepsequence+"&restartStepTaskseq="+restartStepTaskseq+"&restartStepAccount="+restartStepAccount+"&sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&processId=<%=processId%>";
		    	var url = controllername + "?getRestartProcessDetail";
		    
		     	$.ajax({
		 			type : 'post',
		 			url : url,
		 			data : data1,
		 			dataType : 'json',
		 			async :	false,
		 			success : function(result) {
		 				var obj = eval("("+result+")");
		 				blrxm = obj.blrxm;
						$("#jumpStepTest").attr("disabled","disabled");
			 		    xAlert("信息提示",obj.msg);
		 		//		setInterval(afterClose, 2000);
		 			},
		 		    error : function(result) {
		 			//	alert();
		 			}
		 		});
			});
    });
});


function afterClose(){//&issuccess=1
	$(window).manhuaDialog.changeUrl("修改办理人","${pageContext.request.contextPath }/jsp/business/lcgl/lccx/processJumpPage.jsp?sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&processId=<%=processId%>");
}

function closeNowCloseFunction(){
	var fuyemian1=$(window).manhuaDialog.getParentObj();
	fuyemian1.restartUpdateList(blrxm);
}
</script>
</head>
<body TOPMARGIN="0" LEFTMARGIN="0" style="overflow:visible" >
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
   <div class="B-small-from-table-autoConcise">
      <h4 class="title">
		<span class="pull-right">
		  <button id="jumpStepTest" class="btn"  type="button">确认重启</button>
		</span>
		<br>
	  </h4>
   <div style="height:5px;"> </div>
	<table class="table-hover table-activeTd B-table" id="processDetail" width="100%" type="single" noPage="true">
		<thead>
			<tr>	
			<th fieldname="CJSJ" style="font-size:14px;">&nbsp;流程顺序&nbsp;</th>
			<th fieldname="WCRXM" style="font-size:14px;">&nbsp;节点名称&nbsp;</th>
			<th fieldname="WCSJ" style="font-size:14px;">&nbsp;节点属性&nbsp;</th>
			<th fieldname="CJSJ" style="font-size:14px;">&nbsp;完成人员&nbsp;</th>
			<th fieldname="SPYJ" style="font-size:14px;width:30%;">&nbsp;所属部门&nbsp;</th>
			<th fieldname="CJRXM" style="font-size:14px;">&nbsp;节点角色&nbsp;</th>
			<th fieldname="CJSJ" style="font-size:14px;">&nbsp;审批意见&nbsp;</th>
			</tr>
		</thead>
		<tbody>
			<tr onclick="validProcess(this)" name="<%=Pub.getUserNameByLoginId(fqrQs.getString(1, "CJRID")) %>" 
											 account="<%=fqrQs.getString(1, "CJRID")%>"
											 taskseq="0">
				<td align="left" style="font-size:14px;">0</td>
				<td align="left" style="font-size:14px;">发起人</td>
				<td align="left" style="font-size:14px;">发起人</td>
				<td align="left" style="font-size:14px;"><%=fqrQs.getString(1, "CJRID") == null ? "" : Pub.getUserNameByLoginId(fqrQs.getString(1, "CJRID")) %></td>
				<td align="left" style="font-size:14px;"><%=fqrQs.getString(1, "CJDWDM") == null ? "" : Pub.getDeptNameByID(fqrQs.getString(1, "CJDWDM")) %></td>
				<td align="left" style="font-size:14px;"></td>
				<td align="left" style="font-size:14px;"><%=fqrQs.getString(1, "RESULTDSCR") == null ? "" : fqrQs.getString(1, "RESULTDSCR") %></td>
			</tr>
			<%
				String unKnownStep = "";
				for(int i = 0; i < qs.getRowCount(); i++) {
					String stepsequence = qs.getString(i+1, "STEPSEQUENCE");
					String pname1 = qs.getString(i+1, "PNAME1");
					if(pname1 == null) {
						unKnownStep = stepsequence;
					}
			%>
			<tr onclick="validProcess(this)" name="<%=Pub.getUserNameByLoginId(qs.getString(i+1, "ACTOR")) %>" 
											 account="<%=qs.getString(i+1, "ACTOR")%>"
											 taskseq="<%=qs.getString(i+1, "TASKSEQ")%>"
											 stepsequence="<%=qs.getString(i+1, "STEPSEQUENCE")%>">
				<td align="left" style="font-size:14px;"><%=qs.getString(i+1, "STEPSEQUENCE") %></td>
				<td align="left" style="font-size:14px;"><%=qs.getString(i+1, "PROCESSNAME") %></td>
				<td align="left" style="font-size:14px;"><%=Pub.getDictValueByCode("SPJDBSFS", qs.getString(i+1, "ISMS")) %></td>
				<td align="left" style="font-size:14px;"><%=qs.getString(i+1, "ACTOR") == null ? "" : Pub.getUserNameByLoginId(qs.getString(i+1, "ACTOR")) %></td>
				<td align="left" style="font-size:14px;"><%=qs.getString(i+1, "DEPTID") == null ? "" : Pub.getDeptNameByID(qs.getString(i+1, "DEPTID")) %></td>
				<td align="left" style="font-size:14px;"><%=qs.getString(i+1, "ROLENAME") == null ? "" : Pub.getRoleNameByID(qs.getString(i+1, "ROLENAME")) %></td>
				<td align="left" style="font-size:14px;"><%=qs.getString(i+1, "RESULTDSCR") == null ? "" : qs.getString(i+1, "RESULTDSCR") %></td>
			</tr>
			<%
				}
			%>
        </tbody>
	</table>
	</div>
	</div>
	</div>
	
      <div align="center">
		<FORM name="frmPost" method="post" style="display: none" target="_blank">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML"> 
			<input type="hidden" name="txtXML" id="txtXML"> 
			<input type="hidden" name="resultXML" id="resultXML">
			<input type="hidden" name="txtFilter"  order="desc" fieldname = ""	id = "txtFilter">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
		</FORM>
		
		<form method="post" id="queryForm"  >
	      <table width="100%">
	        <tr>
	          <td >
	        <!--    <input class="span12" type="hidden" id="processoid" name="processoid" fieldname="processoid" operation="="/> -->
	          </td>
	         </tr>
	      </table>
	    </form>
	</div>

<script language="javascript" >

var isChoose = false;
var restartStep = "";
var restartStepAccount = "";
var restartStepTaskseq = "";
var stepsequence = "";
function validProcess(obj) {
	isChoose = true;
	restartStep = $(obj).attr("name");
	restartStepAccount = $(obj).attr("account");
	restartStepTaskseq = $(obj).attr("taskseq");
	stepsequence = $(obj).attr("stepsequence");
}
</script>
</body>

</html>