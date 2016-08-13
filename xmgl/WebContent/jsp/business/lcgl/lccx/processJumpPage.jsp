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
	String blrxm = request.getParameter("blrxm");
	
	String sql = "select * from (select distinct processoid,isms,ap.deptid, stepsequence,  ap.name processname,ap.resultdscr, " +
			"(case   " +
       		"when ap.isms='2' then DECODE(actor,'','',actor) " +  
      		// 自由报送
       		"when ap.isms='3' then decode(actor,'','' ,actor) " +
      		// 返回发起人
       		"when ap.isms='4' then DECODE(actor,'',to_char((select op.account from ap_processinfo info,fs_org_person op where info.processoid=ap.processoid and info.cjrid=op.account)),actor)   " +
      		//  本部门负责人
       		"when ap.isms='5' then DECODE(actor,'',to_char((select op.account from fs_org_dept od,fs_org_person op where op.account=od.fzr and od.row_id=ap.deptid)),actor)  " +
      		// 部门分管副主任
       		"when ap.isms='6' then DECODE(actor,'',decode((select op.account from fs_org_dept od,fs_org_person op where op.account=od.fgzr and od.row_id=ap.deptid),'','',(select op.account from fs_org_dept od,fs_org_person op where op.account=od.fgzr and od.row_id=ap.deptid)),actor)  " +
      		// 部门负责人
       		"when ap.isms='7' then DECODE(actor,'',decode((select op.account from fs_org_dept od,fs_org_person op where op.account=od.fzr and od.row_id=ap.deptid),'','',(select op.account from fs_org_dept od,fs_org_person op where op.account=od.fzr and od.row_id=ap.deptid)),actor)  " +
      		// 发起部门负责人
       		"when ap.isms='8' then DECODE(actor,'',to_char((select op.account from fs_org_dept od,fs_org_person op,ap_processinfo info where info.cjdwdm=od.row_id and op.account=od.fzr and info.processoid=ap.processoid)),actor) " +  
      		// 发起部门分管主任
       		"when ap.isms='9' then DECODE(actor,'',to_char((select op.account from fs_org_dept od,fs_org_person op,ap_processinfo info where info.cjdwdm=od.row_id and op.account=od.fgzr and info.processoid=ap.processoid)),actor)  " +
			"else '' end) pname1, rolename, actor, state, createtime, closetime, processtime " +
			"from ap_processdetail ap ,fs_org_role_psn_map rp " +
			"where processoid = '"+processId+"' and ap.rolename=rp.role_id(+) order by ap.stepsequence)";

	System.out.println(sql);
	QuerySet qs = DBUtil.executeQuery(sql, null);
	
	String fqrSql = "select resultdscr,cjrid,cjdwdm from AP_PROCESSINFO t where processoid='"+processId+"'";
	QuerySet fqrQs = DBUtil.executeQuery(fqrSql, null);
%>
<head>

<app:base/>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script type="text/javascript">
var controllername= '${pageContext.request.contextPath }/ProcessJumpAction.do';
function exeTask() {
    	if(!isChoose) {
    		xAlert("提示信息",'请选中符合条件的节点','3');
			return;
    	}
    	
    	if(isBreak == "1") {
			xConfirm("提示信息","是否确认退回到："+currentStepName);
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').one("click",function() {
	    		backTask();
			});
			return;
	    } else if(isBreak == "2") {
    		xAlert("提示信息",'蓝色区域为当前节点，请选择灰色区域','3');
			return;
	    } else if(isBreak == "3") {
    		xAlert("提示信息",'本节点人员不明确，请重新选择','3');
			return;
	    } else if(isBreak == "4") {
			xConfirm("提示信息","是否确认越级到："+currentStepName);
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').one("click",function() {
				jumpTask();
			});
	    } else if(isBreak == "5") {
    	//	xAlert("提示信息",'暂时无法退回','3');
			xConfirm("提示信息","是否确认退回到发起人："+currentStepName);
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').one("click",function() {
				backFqr();
			});
	    }
}

var blrxm = "<%=blrxm %>";
function backTask() {
 	var data1 = "stepSeq="+currentStepSeq+"&sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&processId=<%=processId%>";
	var url = controllername + "?backTaskProcessDetail";
	
 	$.ajax({
		type : 'post',
		url : url,
		data : data1,
		dataType : 'json',
		async :	false,
		success : function(result) {
			var obj = eval("("+result+")");
			blrxm = obj.blrxm;
		    xAlert("信息提示",obj.msg);
			setInterval(afterClose, 2000);
		},
	    error : function(result) {
		//	alert();
		}
	});
}

function jumpTask() {
	var data1 = "currentStepCode="+currentStepCode+"&currentStepName="+currentStepName+"&stepSeq="+currentStepSeq;
	data1 += "&sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&processId=<%=processId%>";
	var url = controllername + "?getProcessDetail";
	
 	$.ajax({
		type : 'post',
		url : url,
		data : data1,
		dataType : 'json',
		async :	false,
		success : function(result) {
			var obj = eval("("+result+")");
			blrxm = obj.blrxm;
		    xAlert("信息提示",obj.msg);
			setInterval(afterClose, 2000);
		},
	    error : function(result) {
		//	alert();
		}
	});
}

function backFqr() {
	var data1 = "fqr="+fqr+"&sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&processId=<%=processId%>";
	var url = controllername + "?backToFqrProcessDetail";
	
 	$.ajax({
		type : 'post',
		url : url,
		data : data1,
		dataType : 'json',
		async :	false,
		success : function(result) {
			var obj = eval("("+result+")");
			blrxm = obj.blrxm;
		    xAlert("信息提示",obj.msg);
			setInterval(afterClose, 2000);
		},
	    error : function(result) {
		//	alert();
		}
	});
}

function afterClose(){
	$(window).manhuaDialog.changeUrl("修改办理人","${pageContext.request.contextPath }/jsp/business/lcgl/lccx/processJumpPage.jsp?sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&processId=<%=processId%>&blrxm="+blrxm);
}

function closeNowCloseFunction(){
	var fuyemian1=$(window).manhuaDialog.getParentObj();
	fuyemian1.jumpUpdateList(blrxm);
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
		  <button id="jumpStepTest" class="btn" onclick="exeTask()" type="button">确认跳越</button>
		  <button id="backStepTest" class="btn" onclick="exeTask()" type="button">确认退回</button>
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
			<th fieldname="SPJG" style="font-size:14px;">&nbsp;节点人员&nbsp;</th>
			<th fieldname="SPYJ" style="font-size:14px;width:30%;">&nbsp;所属部门&nbsp;</th>
			<th fieldname="CJRXM" style="font-size:14px;">&nbsp;节点角色&nbsp;</th>
			<th fieldname="CJSJ" style="font-size:14px;">&nbsp;完成人员&nbsp;</th>
			<th fieldname="CJSJ" style="font-size:14px;">&nbsp;审批意见&nbsp;</th>
			</tr>
		</thead>
		<tbody>
		
			<tr onclick="backToFqrProcessDetail(this)" name="<%=Pub.getUserNameByLoginId(fqrQs.getString(1, "CJRID")) %>" 
											 account="<%=fqrQs.getString(1, "CJRID")%>"
											 taskseq="0" style="color: green">
				<td align="left" style="font-size:14px;">0</td>
				<td align="left" style="font-size:14px;">发起人</td>
				<td align="left" style="font-size:14px;">发起人</td>
				<td align="left" style="font-size:14px;"><%=fqrQs.getString(1, "CJRID") == null ? "" : Pub.getUserNameByLoginId(fqrQs.getString(1, "CJRID")) %></td>
				<td align="left" style="font-size:14px;"><%=fqrQs.getString(1, "CJDWDM") == null ? "" : Pub.getDeptNameByID(fqrQs.getString(1, "CJDWDM")) %></td>
				<td align="left" style="font-size:14px;"></td>
				<td align="left" style="font-size:14px;"><%=fqrQs.getString(1, "CJRID") == null ? "" : Pub.getUserNameByLoginId(fqrQs.getString(1, "CJRID")) %></td>
				<td align="left" style="font-size:14px;"><%=fqrQs.getString(1, "RESULTDSCR") == null ? "" : fqrQs.getString(1, "RESULTDSCR") %></td>
			</tr>
			<%
				int seq = 1;
				for(int i = 0; i < qs.getRowCount(); i++) {
					if("1".equals(qs.getString(i+1, "STATE"))) {
						seq = Integer.parseInt(qs.getString(i+1, "STEPSEQUENCE"));
						break;
					}
				}

				String unKnownStep = "";
				for(int i = 0; i < qs.getRowCount(); i++) {
					
					String _sql = "";
					String actor = qs.getString(i+1, "ACTOR");
					String _isms = qs.getString(i+1, "ISMS");
					String rolename = qs.getString(i+1, "ROLENAME");
					String deptid = qs.getString(i+1, "DEPTID");
					int isms = Integer.parseInt(_isms);
					
					String stepsequence = qs.getString(i+1, "STEPSEQUENCE");
					String pname1 = qs.getString(i+1, "PNAME1");
					if(pname1 == null || "".equals(pname1)) {
						unKnownStep = stepsequence;
					}
					int currentStepSeq = Integer.parseInt(stepsequence);
					
					String color =  "";
					if(seq > currentStepSeq) {
						color = "green";
					} else if(seq == currentStepSeq) {
						color = "blue";
					} else {
						color = "grey";
					}
					String id = stepsequence+isms;
			%>
			<tr onclick="validProcess(this)" name="<%=Pub.getUserNameByLoginId(pname1) %>" code="<%=pname1 %>" id="<%=id %>" _current="<%=stepsequence %>" style="color: <%=color %>">
				<td align="left" style="font-size:14px;"><%=qs.getString(i+1, "STEPSEQUENCE") %></td>
				<td align="left" style="font-size:14px;"><%=qs.getString(i+1, "PROCESSNAME") %></td>
				<td align="left" style="font-size:14px;"><%=Pub.getDictValueByCode("SPJDBSFS", qs.getString(i+1, "ISMS")) %></td>
			<%
					String[][] actorArray = null;
					if(pname1 == null || "".equals(pname1)) {
						switch(isms) {
							case 2:// 角色控制
								_sql = "select fun_rolename('"+rolename+"') from dual ";
								actorArray = DBUtil.query(_sql);
								System.out.println(_sql);
								System.out.println("角色控制:"+actorArray[0][0]);
								%>
								<td align="left" style="font-size:14px;">
								<%
								  if(actorArray[0][0].indexOf(",")==-1) {
									%>
				         			<%=Pub.getUserNameByLoginId(actorArray[0][0]) %><span style="display: none">-<%=actorArray[0][0] %></span>
									<% 
								  } else {
									  %>
				         			  <select class="span12 person">
									  <% 
									  String[] _roleCode = actorArray[0][0].split(",");
									  for(int k=0; k < _roleCode.length; k++) {
										  String _roleName = Pub.getUserNameByLoginId(_roleCode[k]);
										%>
					         			<option value="<%=_roleName %>" code="<%=_roleCode[k] %>"><%=_roleName %></option>
										<%
									  }
									  %>
				         			  </select>
									  <% 
								  }
								  
								%>
								</td>
								<%
								break;
							case 3:// 自由报送
								System.out.println("自由报送:");
								%>
								<td align="left" style="font-size:14px;">
			         			<input class="span12"  type="text" id="ACTOR<%=id %>" style="width:75%" disabled deptid="<%=deptid %>" />
			         			<button class="btn btn-link"  type="button" id="blrBtn<%=id %>" onclick="selectBlr(<%=id %>)"><i class="icon-edit" title="点击选择办理人"></i></button>
								</td>
								<%
								break;
							case 4:// 返回发起人
								_sql = "select op.name from ap_processinfo info,fs_org_person op where info.processoid='"+processId+"' and info.cjrid=op.account ";
								actorArray = DBUtil.query(_sql);
								System.out.println(_sql);
								System.out.println("返回发起人:"+actorArray[0][0]);
								%>
								<td align="left" style="font-size:14px;">
								<%=actorArray[0][0] %>
								</td>
								<%
								break;
							case 5://  本部门负责人
								System.out.println("本部门负责人:");
								%>
								<td align="left" style="font-size:14px;">
			         			<input class="span12" type="text" id="ACTOR<%=id %>" style="width:75%" disabled />
			         			<button class="btn btn-link"  type="button" id="blrBtn<%=id %>" onclick="selectBlr(<%=id %>)"><i class="icon-edit" title="点击选择办理人"></i></button>
								</td>
								<%
								break;
							case 6:// 部门分管副主任
								if(deptid != null && "".equals(deptid)) {
									_sql = "select op.name from fs_org_dept od,fs_org_person op where op.account=od.fgzr and od.row_id='"+deptid+"'";
									actorArray = DBUtil.query(_sql);
									System.out.println(_sql);
									System.out.println("部门分管副主任:"+actorArray[0][0]);
									%>
									<td align="left" style="font-size:14px;">
									<%=actorArray[0][0] %>
									</td>
									<%
								} else {
									System.out.println("部门分管副主任:办理人员未明确，需上一节点指定");
									%>
									<td align="left" style="font-size:14px;">
				         			<input class="span12" type="text" id="ACTOR<%=id %>" style="width:75%" disabled />
				         			<button class="btn btn-link"  type="button" id="blrBtn<%=id %>" onclick="selectBlr(<%=id %>)"><i class="icon-edit" title="点击选择办理人"></i></button>
									</td>
									<%
								}
								break;
							case 7:// 部门负责人

								if(deptid != null && "".equals(deptid)) {
									_sql = "select op.name from fs_org_dept od,fs_org_person op where op.account=od.fzr and od.row_id='"+deptid+"'";
									actorArray = DBUtil.query(_sql);
									System.out.println(_sql);
									System.out.println("部门负责人:"+actorArray[0][0]+"|"+deptid);
									%>
									<td align="left" style="font-size:14px;">
									<%=actorArray[0][0] %>
									</td>
									<%
								} else {
									System.out.println("部门负责人:办理人员未明确，需上一节点指定");
									%>
									<td align="left" style="font-size:14px;">
				         			<input class="span12"  type="text" id="ACTOR<%=id %>" style="width:75%" disabled />
				         			<button class="btn btn-link" type="button" id="blrBtn<%=id %>" onclick="selectBlr(<%=id %>)"><i class="icon-edit" title="点击选择办理人"></i></button>
									</td>
									<%
								}
								break;
							case 8:// 发起部门负责人
								_sql = "select op.name from fs_org_dept od,fs_org_person op,ap_processinfo info where info.cjdwdm=od.row_id and op.account=od.fzr and info.processoid='"+processId+"'";
								actorArray = DBUtil.query(_sql);
								System.out.println(_sql);
								System.out.println("发起部门负责人:"+actorArray[0][0]);
								%>
								<td align="left" style="font-size:14px;">
								<%=actorArray[0][0] %>
								</td>
								<%
								break;
							case 9:// 发起部门分管主任
								_sql = "select op.name from fs_org_dept od,fs_org_person op,ap_processinfo info where info.cjdwdm=od.row_id and op.account=od.fgzr and info.processoid='"+processId+"'";
								actorArray = DBUtil.query(_sql);
								System.out.println(_sql);
								System.out.println("发起部门分管主任:"+actorArray[0][0]);
								%>
								<td align="left" style="font-size:14px;">
								<%=actorArray[0][0] %>
								</td>
								<%
								break;
						}
					} else {
						%>
						<td align="left" style="font-size:14px;"><%=Pub.getUserNameByLoginId(pname1) == null ? pname1 : Pub.getUserNameByLoginId(pname1) %></td>
						<%
					}
			%>
				<td align="left" style="font-size:14px;"><%=qs.getString(i+1, "DEPTID") == null ? "" : Pub.getDeptNameByID(qs.getString(i+1, "DEPTID")) %></td>
				<td align="left" style="font-size:14px;"><%=qs.getString(i+1, "ROLENAME") == null ? "" : Pub.getRoleNameByID(qs.getString(i+1, "ROLENAME")) %></td>
				<td align="left" style="font-size:14px;"><%=qs.getString(i+1, "ACTOR") == null ? "" : Pub.getUserNameByLoginId(qs.getString(i+1, "ACTOR")) %></td>
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
var isBreak = "";
var currentStepSeq= "";

var currentStepName = "";
var currentStepCode = "";
function validProcess(obj) {
	isChoose = true;
	isBreak = "";
	var _stepSeq = "<%=seq %>";
	var unKnownStep = "<%=unKnownStep %>";
	currentStepSeq = $(obj).attr("_current");
	
	var selectTd = $("#"+obj.id+" td:eq(3)").html().trim();
	currentStepName = $(obj).attr("name");
	currentStepCode = $(obj).attr("code");
	
	if(selectTd.indexOf("input")!=-1) {
		currentStepName = $("#ACTOR"+obj.id).val();
		currentStepCode = $("#ACTOR"+obj.id).attr("code");
	}
	
	// 当角色查询出多个人员时，会用到此过程
	if(selectTd.indexOf("option")!=-1) {
		currentStepName = $("select option:selected").val();
		currentStepCode = $("select option:selected").attr("code");
	}
	
	if(currentStepName == "null") {
		var _t = $("#"+obj.id+" td:eq(3)").text().trim();
		currentStepName = _t.split("-")[0];
		currentStepCode = _t.split("-")[1];
	}
	
 	if(currentStepName == "") {
		// 未办理
		isBreak = "3";
		return;
	}
	
	if(parseInt(_stepSeq) > parseInt(currentStepSeq)) {
		// 已办理
		$("#jumpStepTest").attr("disabled",true);
		$("#backStepTest").removeAttr("disabled");
		isBreak = "1";
		return;
	}
	
	if(parseInt(_stepSeq) == parseInt(currentStepSeq)) {
		// 当前节点
		isBreak = "2";
		return;
	}
	
	if(parseInt(_stepSeq) < parseInt(currentStepSeq)) {
		// 未办理
		$("#backStepTest").attr("disabled",true);
		$("#jumpStepTest").removeAttr("disabled");

	 	if(currentStepName == "") {
			if(parseInt(unKnownStep) == parseInt(currentStepSeq)) {
				isBreak = "3";
				return;
			}
	 	}
		isBreak = "4";
		return;
	}
}

var fqr="";
function backToFqrProcessDetail(obj) {
	isChoose = true;
	isBreak = "5";
	fqr = $(obj).attr("account");
	currentStepName = $(obj).attr("name");
	$("#jumpStepTest").attr("disabled",true);
	$("#backStepTest").removeAttr("disabled");
}


var isms = null;
function selectBlr(n) {
	isms = n;
	var actorCode = $("#ACTOR"+isms).attr("code");
	var deptid = $("#ACTOR"+isms).attr("deptid");
	selectUserTree('single',actorCode,'setBlr',deptid);
}

function setBlr(data) {
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
	$("#ACTOR"+isms).val(userName);
	$("#ACTOR"+isms).attr("code",userId);

	currentStepName = userName;
	currentStepCode = userId;
}
</script>
</body>

</html>