<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="com.ccthanking.framework.common.UserTemplate"%>
<%@ page import="com.ccthanking.framework.coreapp.aplink.*"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>

<%
	User user = (User) session.getAttribute(Globals.USER_KEY);
	String userName = user.getName();
	String time = DateTimeUtil.getDateTime("yyyyMMdd");
	String timeStr = DateTimeUtil.getDate();
	OrgDept dept = user.getOrgDept();
	String deptid = user.getDepartment();
	String deptName = dept.getDeptName();
	String level = String.valueOf(dept.getDeptLevel());
	String useraccount = user.getAccount();
	String pageareaURL = "";
	//pageareaURL = pageareaURL.substring(0,pageareaURL.lastIndexOf("/"))+"/task.jsp";
	String gdspsj1 = null;
%>
<html>
<head>
<title>审批处理</title>

<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<style type="text/css"> 
p.slider {
	margin: 0px;
	padding: 5px;
	font-size: 16px;
	text-align: middle;
	background: #FFFFFF;
	border: solid 0px #c3c3c3;
	overflow: hidden;
	color: #0088cc;
}

p.slider:hover,p.slider:focus {
	cursor: pointer;
	color: #005580;
	text-decoration: underline;
}

</style>

<script language="javascript">


var resultValue;//zl add 全局变量 记录审批结果
var spflag=false;
var IsQM=false; //zl add 判断签名
var temp = "";
//--------------------------------zl add gz-------------------------------
var template_ID="";
var spFieldname = "";
//--------------------------------------------
var strAction = "${pageContext.request.contextPath}/TaskAction.do?";
var strActionWS = "${pageContext.request.contextPath}/ViewWsAction.do?";
var hasWs = false;
var xuanzhong = '';
	<%String taskid = Pub.val(request, "taskid");
			String taskseq = Pub.val(request, "taskseq");
			String sjbh = Pub.val(request, "sjbh");
			String spbh = Pub.val(request, "spbh");
			String ywid = "";

			String ywlx = Pub.val(request, "ywlx");
			String rwlx = Pub.val(request, "rwlx");
			String strHh = Pub.val(request, "hh");
			String ws_template_id = "";
			String fjbh = "";
			String sp_fjbh = "";
			String ws_template_name = "";
			String dah = "";
			String ywurl = "";
			String[][] fqr = null;
			if ("".equals(dah)) {
				try {
					//dah = ApproveTaskBO.getDAH(sjbh,ywlx);
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}

			int TotalNodesCount = 0;
			int CurrentNodeIndex = 0;

			String zab = "";

			Connection conn = DBUtil.getConnection();

			com.ccthanking.framework.coreapp.aplink.Process process = null;
			Step nowStep = null;
			Step nextStep = null;
			Step lastStep = null;
			String processType = null;
			String spRole = "";
			String sql = "";
			String[][] firstSpRolw = null;
			int countsteps = 0;
			String fqrid = "";
			String fqrxm = "";
			String fqrdwdm = "";
			try {
				process = TaskBO.getProcess(conn, spbh);
				nowStep = process.open();
				nextStep = process.getNextStep();
				lastStep = process.getLastStep();
				CurrentNodeIndex = nowStep.getStepSequence();
				if (TotalNodesCount <= CurrentNodeIndex) {
					//parentDeptId="";
				}
				String sql_processtype = "Select a.processtype from ap_processinfo b,ap_processtype a Where a.PROCESSTYPEOID = b.PROCESSTYPEOID and b.PROCESSOID='"
						+ spbh + "'";
				QuerySet qs_processtype = DBUtil.executeQuery(sql_processtype,
						null, conn);
				if (qs_processtype.getRowCount() > 0) {
					processType = qs_processtype.getString(1, 1);
				}

				if (nextStep != null) {
					spRole = nextStep.getRole();
					//sql = "select a.PERSON_ACCOUNT,b.NAME from org_role_psn_map a,ORG_PERSON b where ROLE_NAME='"+spRole+"' and a.PERSON_ACCOUNT = b.ACCOUNT  and b.flag='1'";
					//firstSpRolw= DBUtil.querySql(sql);
					/**修改查询外分局同角色的人的问题*/
					sql = "select a.PERSON_ACCOUNT,b.NAME from fs_org_role_psn_map a,FS_ORG_PERSON b where ROLE_NAME='"
							+ spRole
							+ "' and a.PERSON_ACCOUNT = b.ACCOUNT  and b.flag='1' ";
					OrgDept parentDept = dept.getParent();

					firstSpRolw = DBUtil.querySql(conn, sql);

					while (null == firstSpRolw) {
						parentDept = dept.getParent();
						if (parentDept == null)
							break;
						firstSpRolw = DBUtil.querySql(sql
								+ parentDept.getDeptID() + "'");
						if (null == parentDept
								|| parentDept.getDeptID()
										.equals("100000000000")) {
							break;
						}
					}
				}
				//add by cbl start
				//判断是否有文书
				String sql_pub_blob = "select * from pub_blob a,ws_template b where (a.ZFBS = '0' or a.ZFBS IS NULL) and a.ws_template_id = b.ws_template_id and a.SJBH='"
						+ sjbh
						+ "' and a.YWLX = '"
						+ ywlx
						+ "' order by is_sp_flag desc";
				QuerySet qs = DBUtil.executeQuery(sql_pub_blob, null);
				if (qs != null) {
					for (int j = 0; j < qs.getRowCount(); j++) {
						if ("1".equals(qs.getString(1 + j, "IS_SP_FLAG"))) {
							sp_fjbh = qs.getString(1 + j, "FJBH");
						}
						ws_template_id += qs.getString(1 + j, "WS_TEMPLATE_ID")
								+ ",";
						ws_template_name += qs.getString(1 + j, "FILENAME")
								+ ",";
						fjbh += qs.getString(1 + j, "fjbh") + ",";
					}
				}

				if (ws_template_id.length() > 0) {
					ws_template_id = ws_template_id.substring(0,
							ws_template_id.length() - 1);
				}
				if (ws_template_name.length() > 0) {
					ws_template_name = ws_template_name.substring(0,
							ws_template_name.length() - 1);
				}
				if (fjbh.length() > 0) {
					fjbh = fjbh.substring(0, fjbh.length() - 1);
				}

				//通过sjbh获取AP_PROCESSCONFIG表的AP_PROCESS_ID主键作为附件查询条件
				String getAP_PROCESSCONFIG = "select AP_PROCESS_ID from AP_PROCESSCONFIG  where SJBH='"
						+ sjbh + "' and YWLX = '" + ywlx + "'";
				QuerySet rspk = DBUtil.executeQuery(getAP_PROCESSCONFIG, null);
				if (rspk != null && rspk.getRowCount() > 0) {
					ywid = rspk.getString(1, 1);
				}

				//获取发起人

				String cjrsql = "select CJRID,PCSDM from ap_task_schedule where id='"
						+ process.getTaskOID() + "' order by cjsj asc";
				fqr = DBUtil.querySql(conn, cjrsql);
				if (fqr != null) {
					fqrid = fqr[0][0];
					fqrdwdm = fqr[0][1];
					fqrxm = Pub.getUserNameByLoginId(fqrid);
				}

				
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (conn != null) {
					conn.close();
				}
			}

			boolean islastStep = false;
			if (nowStep.getStepSequence() == lastStep.getStepSequence()) {
				islastStep = true;
			}%>
	var processtype = "<%=processType%>";
	
	/* if(processtype=="1"){//流程类型(1、流程内2、流程内特送3、特送)
    	document.getElementById("processtype2").style.display="none";
    	document.getElementById("processtype3").style.display="none";

  	}else if(processtype=="2"){

  		document.getElementById("processtype2").style.display="";

    	var srcValue = "T#AP_PROCESSDETAIL:STEPSEQUENCE:NAME:STATE='1' and PROCESSOID='"+spbh+"'";
    	USERSTEP.src=srcValue;
    	document.getElementById("processtype3").style.display="none";
   	}else if(processtype=="3"){
   		document.getElementById("processtype2").style.display="none";
   		document.getElementById("processtype3").style.display="";
   	} */
   	var p_ywid='<%=ywid%>';

	var fqrid = "<%=fqrid %>";
	var useraccount = "<%=useraccount %>";
	
function doSubmit(jsrStr,jsdwStr)
{
	var strText = document.getElementById("mind").value;
	// 当代办人是发起人时，可以不填写意见
	if(fqrid != useraccount) {
   		if (strText == "") {
   			requireFormMsg("请给出审批意见!");
			document.getElementById("mind").focus();
			return;
   		}
	}
	
	var m = temp;
	var strAction = "<%=request.getContextPath()%>/TaskAction.do?doApprove";
	
	try
	{
		var fqrdwdm = '<%=fqrdwdm%>';
		var farid = '<%=fqrid%>';
		var vfjbh = '<%=sp_fjbh%>';
		
        var spUser = '';
		//var data1 = '&id=<%=taskid%>&seq=<%=taskseq%>&sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&spbh=<%=spbh%>&spjg=1&sfth=0&spUser='+spUser;
		var jsonObj = new Object();
		jsonObj["id"] = '<%=taskid%>';
		jsonObj["seq"] = '<%=taskseq%>';
		jsonObj["sjbh"] = '<%=sjbh%>';
		jsonObj["ywlx"] = '<%=ywlx%>';
		jsonObj["spbh"] = '<%=spbh%>';
		jsonObj["spjg"] = '1';
		jsonObj["sfth"] = '0';
		jsonObj["spUser"] = spUser;
		//选择返回发起人,获取发起人id和部门传递,否则直接传递参数
		if( m ==4){//点击返回发起人
			jsonObj["tsdept"] = fqrdwdm;
			jsonObj["tsperson"] = farid;
			//data1+= '&tsdept='+fqrdwdm+'&tsperson='+farid;
		}else{
			jsonObj["tsdept"] = jsdwStr;
			jsonObj["tsperson"] = jsrStr;
			//data1+= '&tsdept='+jsdwStr+'&tsperson='+jsrStr;
		}
		//add by zhangbr@ccthanking.com	添加escape处理审批意见中带特殊符号的情况
		//data1+= '&result=1&resultDscr='+escape(document.getElementById('mind').value)+'&fjbh='+vfjbh+'&spFieldname='+spFieldname+'&isRead=1';
		jsonObj["result"] = 1;
		jsonObj["resultDscr"] = document.getElementById('mind').value;
		jsonObj["fjbh"] = vfjbh;
		jsonObj["spFieldname"] = spFieldname;
		jsonObj["isRead"] = 1;
			
		//m=3为特送下一级操作,单独处理.
		if(	m==3 || m ==4){
			jsonObj["isSpecialNextStep"] = 1;
			//data1 +='&isSpecialNextStep=1';
		}
		jsonData = {
			msg:JSON.stringify(jsonObj)
		}
		$.ajax({
			type : 'post',
			url : strAction+"&domFlag=true",
			data : jsonData,
			dataType : 'json',
			async :	false,
			success : function(result) {
				var fuyemian=$(window).manhuaDialog.getParentObj();
			     var s = fuyemian.gengxinchaxun;
		         if(s){
			       fuyemian.gengxinchaxun(result.message);
		         }else{
		        	 var frameW = $(window).manhuaDialog.getFrameObj();
		        	 var w = frameW.gengxinchaxun;
		        	 if(w){
		        		 frameW.gengxinchaxun(result.message);
		        	 }
		         }
				 success = true;
			
				 $(window).manhuaDialog.close();
			},
		    error : function(result) {
		    	xAlertMsg(result.message);
				    //defaultJson.clearTxtXML();
				    success = false;
			}
		});
		document.frmPost.txtSubmitSQL.value = "";
		document.frmPost.txtFilter.value = "";
		document.frmPost.result.value = "";

	}
	catch(e)
	{
		
	}
	spflag=true;
}




function fqrSaveWs_(){
	var dbrName = $("#ACTOR").val();
	if(!dbrName&&dbrName=="")
	{
	  requireFormMsg("请选择办理人!!!");
	  return;
	}
    var form1 = iFrame.wsTemplate;
    var data1 = null;
    if(form1) {
    	var json = Form2Json.formToJSON(form1);
    	if(json.length <= 2) {
    		data1= defaultJson.packSaveJson('{"SJBH":"<%=sjbh%>","YWLX":"<%=ywlx%>","WSTEMPLATEID":"<%=ws_template_id%>"}');
    	} else {
    	  	json = json.replace('}',',"SJBH":"<%=sjbh%>","YWLX":"<%=ywlx%>","WSTEMPLATEID":"<%=ws_template_id%>"}');
    	  	
    	   	data1 = defaultJson.packSaveJson(json);
    	   	
    	}
    } else {
    	data1= defaultJson.packSaveJson('{"SJBH":"<%=sjbh%>","YWLX":"<%=ywlx%>","WSTEMPLATEID":"<%=ws_template_id%>"}');
    }
    var wsActionURL = "/xmgl/PubWS.do?saveWSJson&issp=1";

	xConfirm("提示信息","是否提交审批信息?办理人："+dbrName);
	$('#ConfirmYesButton').unbind();
	$('#ConfirmYesButton').one("click",function(){
		$.ajax({
			url : wsActionURL,
			data : data1,
			dataType : 'json',
			async :	false,
			type : 'post',
			//contentType:'application/json;charset=UTF-8',	    
			success : function(result) {
	
				if(result)
			       flag = result.msg;
				if("0"==flag) {
					alert("文书保存出错！");
					return ;	
				} else {
					
					var sjbh="<%=sjbh%>";
					var ywlx="<%=ywlx%>";
					var dbr = "";
					if($("#ACTOR")[0].tagName=="INPUT") {
						dbr = $("#ACTOR").attr("code");
					}
					if($("#ACTOR")[0].tagName=="SELECT") {
						dbr = $("#ACTOR").val();
					}
					
					$.ajax({
						url : "/xmgl/SPAction.do?TsFqr&dbr="+dbr+"&ywlx="+ywlx+"&sjbh="+sjbh,
						data : null,
						dataType : 'json',
						async :	false,
						type : 'post',
						//contentType:'application/json;charset=UTF-8',	    
						success : function(result) {
							 var fuyemian=$(window).manhuaDialog.getParentObj();
						     var s = fuyemian.gengxinchaxun;
					         if(s){
						       fuyemian.gengxinchaxun(result.message);
					         } else {
					        	 var frameW = $(window).manhuaDialog.getFrameObj();
					        	 var w = frameW.gengxinchaxun;
					        	 if(w){
					        		 frameW.gengxinchaxun(result.message);
					        	 }
					         }
							 success = true;
						
							 $(window).manhuaDialog.close();
						},
					    error : function(result) {
					    	return false;
					    }
					});
				}
	
			},
		    error : function(result) {
		     	//alert(234);
		    }
		});
	});
}

function saveYJ(){


    var form1 = iFrame.wsTemplate;
//    alert(form1.elements.length);
    if(form1.elements.length > 0)
	    for(var i=0;i<form1.elements.length;i++)
	    {
	    	var obj = form1.elements[i];
	//		alert(obj.id);
	    	if(obj.value)
	    		{
	    			resultValue = 1;
	    			spFieldname = obj.id;
	    			document.getElementById("mind").value = obj.value;
	    		}
	    }

}
//弹出区域回调
getWinData = function(data){

	var jsrStr = "",jsdwStr = "";
	for(var i=0;i<data.length;i++){
		var tempJson = data[i];
		jsrStr +=tempJson.ACCOUNT+",";
		jsdwStr +=tempJson.DEPTID+",";
		
	}
	jsrStr = jsrStr.substring(0,jsrStr.length-1);
	jsdwStr = jsdwStr.substring(0,jsdwStr.length-1);
	$("#USERTODEPT").val() == jsrStr;
	$("#USERTOPERSON").val() == jsrStr;
	
	doSubmit(jsrStr,jsdwStr);
	
};
function doSaveFh(m)
{
	var n=1; // 1：同意 特送只有同意处理
	saveYJ();
	temp = m;
	//alert(temps);
	doSubmit("","");
	
}
function doSave(m)
{
	saveYJ();
	temp = m;
	try {
		var strText = document.getElementById("mind").value;
		// 当代办人是发起人时，可以不填写意见
		if(fqrid != useraccount) {
	   		if (strText == "") {
	   			requireFormMsg("请给出审批意见!");
				document.getElementById("mind").focus();
				return;
	   		}
		}
        	

	    	
		/* 	strText = document.getElementById("mind").value;
			
		if (strText.length == 0)
		{
			strText = '';
        } */
		
		// add by xhb 
		var dbr = $("#ACTOR").attr("code");
		if(!dbr) {
			requireFormMsg("请选择提交人员");
			return;
		}
		doSubmit(dbr,"");   		 
	}
	catch(e)
	{
		alert(e.description);
		document.frmPost.checkOver.disabled=true;
	}
	spflag=true;
};



function print(fjbh,ws_template_id,sjbh,ywlx){
   <%--  var strAction = "<%=request.getContextPath()%>/PubWS.do?getPrintAction&templateid="+ws_template_id+"&sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&ywid=<%=ywid%>"+"&isEdit=1&isSp=1&rowid="+Math.random()+".mht";

    parent.$("body").manhuaDialog({"title":"呈请审批","type":"text","content":strAction,"modal":"1"});
 --%>   
 // window.open(strAction,"", 'title=print,toolbar=no, menubar=yes,scrollbars=yes,resizable=yes');
   
    var wsActionURL = '/xmgl/PubWS.do?getXMLPrintAction|templateid='+ws_template_id+'|isEdit=0|ywlx='+ywlx+'|sjbh='+sjbh+'|rowid='+Math.random()+'.mht';

    var rswsActionURL = '/xmgl/jsp/framework/print/pubPrint.jsp?param='+wsActionURL+'&isview=1&sjbh='+sjbh+'&ywlx='+ywlx+'&temlateid='+ws_template_id+'&isEdit=0&ywid='+p_ywid;

   // window.open(wsActionURL,"","width=800px,height=600px,toolar=0,menubar=0,scrollbars=1,status=0,resizable=1,screenX=0,screenY=0");
     $(window).manhuaDialog({"title":"查看文书","type":"text","content":rswsActionURL,"modal":"1"});  

}



function submitForm()
	{

		var cList = document.getElementsByName("selectCheckInfo");
        var maySubmit = false;
		for(var i=0;i<cList.length;i++){
			var obj = cList[i];
			if(obj.checked){
             maySubmit = true;
             break;
			}
		}
        if(maySubmit==false){
           alert("请选择一条审批!");
           return;
        }
		if (document.getElementById("mind").value.length>1000)
		{
			alert('审批意见不能超过1000个汉字，请核对!');
			document.getElementById("mind").focus();
			return false;
		}
	}

function spyjUpdate()
{
	
    document.getElementById("mind").value="";
    document.getElementById("mind").disabled=false;
}

var clickNum = 0 ;

$(function() {
	/* doSearch (1); */
	$("#wsTemplateDiv").height(getDivStyleHeight());
	setFileData("","","<%=sjbh%>","<%=ywlx%>");
	queryFileData("","","<%=sjbh%>","<%=ywlx%>");

	 //监听变化事件
    $("#USERTODEPT").change(function() {
    	generatePc($("#USERTOPERSON"));
    }); 

    var btnnext = $("#btnnext");
    btnnext.click(function() {
    	var dbrName = $("#ACTOR").val();
    	var dbrid = $("#ACTOR").attr("code");
    	if(!dbrName&&dbrName=="")
		{
		  requireFormMsg("请选择办理人!");
		  return;
		}
    	if(dbrid == useraccount) {
		  requireFormMsg("下一节点办理人禁止选择当前登录人。请重新选择办理人，或按【返回发起人】按钮！！！！！！");
		  return;
    	}
    	
        xConfirm("提示信息","是否提交审批信息?办理人："+dbrName);
		$('#ConfirmYesButton').unbind();
   		$('#ConfirmYesButton').attr('click','');//.click( eval(function(){test(dd);}));
   		$('#ConfirmYesButton').one("click",function(){
   	    	doSave(3);
   		});
    });
    var btnfqr = $("#btnfqr");
    btnfqr.click(function() {
    	var dbrName = "<%=fqrxm%>";
    	//doSave(4); 
    	xConfirm("提示信息","是否提交审批信息?办理人："+dbrName);
		$('#ConfirmYesButton').unbind();
   		$('#ConfirmYesButton').attr('click','');//.click( eval(function(){test(dd);}));
   		$('#ConfirmYesButton').one("click",function(){
   	    	doSaveFh(4);
   		});
    });
    
	// add by xhb start
	$("#blrBtn").click(function(){
		var actorCode = $("#ACTOR").attr("code");
		selectUserTree('checkbox',actorCode,'setBlr') ;
	});
	// add by xhb end

	var splc=$("#splc");
	splc.click(function() {
		
		var strUrl = '${pageContext.request.contextPath}/jsp/framework/common/aplink/spFlowView_gd.jsp?sjbh=<%=sjbh %>&spbh=<%=spbh %>';
		
		$(window).manhuaDialog({"title":"查看审批流程","type":"text","content":strUrl,"modal":"1"});
	});
	
	var ywxx=$("#ywxx");
	ywxx.click(function() {
		
		 $(window).manhuaDialog({"title":"查看业务详细信息","type":"text","content":"<%=ywurl%>","modal":"1"});  
	});
});

// add by xhb start
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
	$("#ACTOR").val(userName);
	$("#ACTOR").attr("code",userId);
}
// add by xhb end 

 //人员查询
function generatePc(ndObj){
		
	ndObj.attr("src", "T#FS_ORG_PERSON:ACCOUNT:NAME:DEPARTMENT = "+$("#USERTODEPT").val()+ " AND FLAG=1 ");
	ndObj.attr("kind", "dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
}

function dealValue(){
	
	$("input[type='checkbox']").each(function()
				{
					if($(this).is(':checked')){
						xuanzhong =xuanzhong+$(this).attr('value')+',';	
					}
				});
	xuanzhong = xuanzhong.substring(0, xuanzhong.length-1);
	if(xuanzhong == 1){
		document.all('USERTODEPT').value = '';
		document.all('USERTOPERSON').value = '';
	}
}

function doArchiveTask() {
  	xConfirm("提示信息","是否确定结束审批?");
	$('#ConfirmYesButton').unbind();
	$('#ConfirmYesButton').attr('click','');
	$('#ConfirmYesButton').one("click",function() {
		 customOperation();
	});  
}
function printDiv()
{
	window.frames["iFrame"].printIframe();
	
}
function printDiv_temp()
{
	var ywlx = '<%=ywlx%>';
	var sjbh = '<%=sjbh%>';
	var ws_template_id = '<%=ws_template_id%>';
	var wsActionURL = '<%=request.getContextPath()%>/PubWS.do?getPrintAction|templateid='+ws_template_id+'|ywlx='+ywlx+'|sjbh='+sjbh+'|rowid='+Math.random()+'.mht';
    wsActionURL = g_sAppName+'/jsp/framework/print/pubPrint.jsp?param='+wsActionURL+'&isview=1&sjbh='+sjbh+'&ywlx='+ywlx+'&temlateid='+ws_template_id+'&isEdit=0&ywid='+p_ywid;

    window.open(wsActionURL,"","width=800px,height=600px,toolar=0,menubar=0,scrollbars=1,status=0,resizable=1,screenX=0,screenY=0");	 
}
function customOperation()
{
//	saveYJ();
	
    var strActionUrl = "<%=request.getContextPath()%>/TaskBackAction.do?customOperationAction";
    
	var data1 = '&eventid=<%=sjbh%>&ywlx=<%=ywlx%>&spbh=<%=spbh%>&taskId=<%=taskid %>&processtype=4';
		$.ajax({
			type : 'post',
			url : strActionUrl,
			data : data1,
			dataType : 'json',
			async :	false,
			success : function(result) {
				
				var fuyemian=$(window).manhuaDialog.getParentObj();
				var s = fuyemian.gengxinchaxun;
			     if(s){
				    fuyemian.gengxinchaxun(result.Msg);
			     }else{
		        	 var frameW = $(window).manhuaDialog.getFrameObj();
		        	 var w = frameW.gengxinchaxun;
		        	 if(w){
		        		 frameW.gengxinchaxun(result.Msg);
		        	 }
		         }
			
				$(window).manhuaDialog.close();
			},
		    error : function(result) {
		    	 //alert(result);
			     //alert(result.msg);
			     xAlertMsg(result.Msg);
			     
				 return false;
			}
		});

    return true;
}

function comfirmFinishThis() {
	xConfirm("提示信息","是否确定结束此待办?");
	$('#ConfirmYesButton').unbind();
	$('#ConfirmYesButton').attr('click','');
	$('#ConfirmYesButton').one("click",function() {
		finishThis();
	});
}

function finishThis() {
	var data = "taskid=<%=taskid%>&taskseq=<%=taskseq%>&sjbh=<%=sjbh%>&ywlx=<%=ywlx%>";
	
	$.ajax({
		type : 'post',
		url : "<%=request.getContextPath() %>/TaskBackAction.do?finishThis",
		data : data,
		dataType : 'json',
		async :	false,
		success : function(result) {
			
			var fuyemian=$(window).manhuaDialog.getParentObj();
			fuyemian.gengxinchaxun(result.Msg);
			
			$(window).manhuaDialog.close();
			return true;
		},
	    error : function(result) {
	    	 //alert(result);
		     //alert(result.msg);
		     xAlertMsg(result.Msg);
		     
			 return false;
		}
	});
}

function viewSpyj(){

	$(window).manhuaDialog({"title":"审批意见","type":"text","content":"<%=request.getContextPath()%>/jsp/framework/common/aplink/spYjView.jsp?sjbh=<%=sjbh%>&spbh=<%=spbh%>","modal":"2"});	
}
</script>
</head>
<body TOPMARGIN="0" LEFTMARGIN="0" style="overflow:visible">
<app:dialogs/>
<div class="container-fluid">
<p></p>
 <div class="row-fluid">
        <div class="B-small-from-table-autoConcise">
     		<%
     			String isEdit = useraccount.equals(fqrid) ? "1" : "0";
     		%>
            <div class="span7" style="height:100%;overflow:hidden;"  id="wsTemplateDiv">
             	<iframe name="iFrame" width="100%" height="90%" src="/xmgl/PubWS.do?getXMLPrintAction&templateid=<%=ws_template_id%>&sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&isSp=1&isEdit=<%=isEdit %>" FRAMEBORDER=0 SCROLLING= ></iframe>
			</div>
			
    		<div class="span5" >
		    		<h4 class="title">
						<span class="pull-right">
		
						
					<!-- 	<button type="button" class="btn" name="btnJssp" onClick="saveYJ();">test</button>  -->
						<%
 							if (useraccount.equals(fqrid)) {
						%>
						<button type="button" class="btn" name="btnJssp" onClick="doArchiveTask();">审批结束</button> 
 						<button type="button" class="btn" onclick="fqrSaveWs_()" >提交</button>
		              	<button id="example_query" class="btn btn-link" type="button" onclick="printDiv()"><i class="icon-print"></i>打印</button>
						<%
 							} else {
 						%>
 						<button type="button" class="btn" id = "btnnext" name="btnSave" >提交</button>
 						<button type="button" class="btn" id="btnfqr" name="btnFhfqr">返回发起人</button> 
		              	<button id="example_query" class="btn btn-link" type="button" onclick="printDiv()"><i class="icon-print"></i>打印</button>
 						<%
 							}
 						%>
 						</span>
 					</h4>
 					
           	      	<div class="B-small-from-table-auto">
           	      		<form method="post" id="demoForm">
						<table class="B-table" width="100%">
							<tr>
								<th align="center" valign="middle" style="text-align:center;font-size:14px;width:8%">相关信息</th>
								<td style="font-size:14px;">
				           			<button id="splc" class="btn btn-link" type="button"><i class="icon-book"></i>审批流程</button>
			                    	<button id="cmdSpyj" class="btn btn-link" onclick="viewSpyj()" type="button"><i class="icon-th-list"></i>办理意见</button>
								</td>
							</tr>
						   <tr>
								<th class="right-border bottom-border" style="font-size:14px;">办理人</th>
								<td class="bottom-border right-border">
			         			<input class="span12"  type="text" check-type="required" fieldname="ACTOR" id="ACTOR" name ="ACTOR" style="width:75%" disabled />
			         			<button class="btn btn-link"  type="button" id="blrBtn"><i class="icon-edit" title="点击选择办理人"></i></button>
			       	 			</td>
						   </tr><!-- 
						   	<tr>
								<th align="center" valign="middle" style="text-align:center;font-size:14px;width:8%">审批意见</th>
								<td colspan="4">
									<div><textarea class="span12" rows="3" name="txtCheckMind" id="mind" maxlength="1000">同意</textarea></div>
									
								</td>
							</tr> -->
						   <tr>
							<th width="15%" class="right-border bottom-border text-center" style="font-size:14px;">附件信息</th>
							<td width="85%" class="bottom-border right-border">
								<div>
									<span class="btn btn-fileUpload" onclick="doSelectFile(this);"
										fjlb="3999"> <i class="icon-plus"></i> <span>添加文件...</span>
									</span>
									<table role="presentation" class="table table-striped">
										<tbody class="files showFileTab" showLrr="true" deleteUser="<%=user.getAccount()%>"
											 showSize="false" onlyView="true" data-toggle="modal-gallery" data-target="#modal-gallery">
										</tbody>
									</table>
								</div>
							</td>
						   </tr>
                       </table>
                       </form>
  					
                       
                   </div>
				   <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
              </div>
              <div class="windowOpenAuto" style="display:none">
     			<form method="post" id="mindForm"  >
				<table width="100%" class="table" style="display:none">
					<tr>
						<th width="100" align="center" valign="middle">领导批示</th>
						<td colspan="2">
								<textarea class="span12" rows="4" name="txtCheckMind" fieldname="mind"
									id="mind" maxLength="2000"></textarea>
						</td>
					</tr>
				</table>
				</form>
			  </div>
    	</div>		
	</div>
</div> 
	</body>
	</html>   
<script language="javascript">
var aaa = "<%=gdspsj1%>";

</script>