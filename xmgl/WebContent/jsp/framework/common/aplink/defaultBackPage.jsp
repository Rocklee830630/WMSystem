<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.coreapp.aplink.*"%>
<%@ page import="com.ccthanking.framework.coreapp.aplink.Process"%>
<%@ page import="com.ccthanking.framework.common.*"%>

<%@ page import="java.sql.Connection"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>

<%@ taglib uri= "/tld/base.tld" prefix="app" %>

<html>
<head>
<%
	User user = (User) session.getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
   String isEdit="1";
   String isview=Pub.val(request, "isview");
   String taskid = Pub.val(request, "taskid");
   String taskseq = Pub.val(request, "taskseq");
   String sjbh=(String)request.getParameter("sjbh");
   String ywlx=(String)request.getParameter("ywlx");
	
   String operationoid = "";
   
   String processtype = (String)request.getParameter("processtype");
   String templateid="";
   templateid=(String)request.getParameter("temlateid");
   String spzt = request.getParameter("spzt");
   if(spzt == null || "".equals(spzt)) spzt = "9";
   
   String ywid = (String)request.getParameter("ywid");
   

  String condition = (String)request.getParameter("condition");
  if(Pub.empty(condition)){
	  condition = "";
  }
  
	Connection conn = DBUtil.getConnection();
	String ws_template_id = "";
	String fjbh = "";
	String sp_fjbh = "";
	String ws_template_name = "";
	String dah = "";
	String spbh = "";
	String html =""; 

	  String pxZxmSql = "select pq_zxm.gc_pq_zxm_id from GC_HTGL_HT ht,gc_pq_zxm pq_zxm "
			+ "where ht.id=pq_zxm.htid "
			+ "and ht.sjbh='" + sjbh+ "' and ht.ywlx='" + ywlx + "'";
	  String[][] pxZxmResult = DBUtil.query(pxZxmSql);
	  String pxZxm = pxZxmResult == null ? "" : pxZxmResult[0][0];
	try {
		//获取operationoidstart
    /*    	String  selectsql = "select t.ywlx,t.ws_template_id,d.name,d.operationoid,d.processtype from AP_WS_TYPZ t,AP_PROCESSTYPE d where t.operationoid = d.processtypeoid";
    	selectsql += " and t.ywlx = '"+ywlx+"'";
    	selectsql += " and deptid in('"+dept.getDeptID()+"'";
    	if(!Pub.empty(condition)){
    		selectsql += " and t.condition = '"+condition+"'";
    	}
    	
		while (!("100000000000".equals(dept.getDeptID()))) {
			dept = dept.getParent();
			if(dept==null)
				break;
			selectsql+= ",'"+dept.getDeptID()+"'";

		}
		selectsql += ") order by deptid "; */
		
		String selectsql = "select t.processtypeoid,b.ws_template_id from AP_PROCESSINFO t,pub_blob b " 
				+ "where t.eventid= b.sjbh(+) and t.eventid ='" + sjbh + "'";
    	
		String selrs[][]  = DBUtil.query(conn, selectsql);
		//templateid,ywlx,operationoid,processtype,sjbh
		if(selrs != null){
			operationoid =selrs[0][0];
			ws_template_id = selrs[0][1];
			
			// 当工程甩项申请会签单系列的会签单时，把可编辑状态设置为否。
			// 因为此页面试退回给发起人页面，发起人时可以更改会签单的。
			// 但甩项是伴随业务的，不能有更改。  add by xiahongbo on 2015-01-08 start
			if("27".equals(ws_template_id) || "26".equals(ws_template_id) || "20".equals(ws_template_id) || "22".equals(ws_template_id)) {
				isEdit = "0";
			}
			// add by xiahongbo on 2015-01-08 end
		}
		//add start
	
		TaskMgrBean t = new TaskMgrBean();
		html = t.showSelectPersion(operationoid,user);
		//add end
	//get oeratonoidend
		if(Pub.empty(spbh)){
	        String sql_info = "select PROCESSOID,PROCESSTYPEOID,to_char(CREATETIME,'yyyy-mm-dd hh24:mi:ss'),to_char(CLOSETIME,'yyyy-mm-dd hh24:mi:ss'),EVENTID,STATE,OPERATIONOID,MEMO,TASKOID,PROCESSEVENT,RESULT,RESULTDSCR,YXBS from ap_processinfo"
	                + " where EVENTID='"+sjbh+"'";
	        String[][] list_info = DBUtil.query(sql_info);
			if(list_info!=null&&list_info.length>0){
				spbh = list_info[0][0];
			}
		}
		
		
		//判断是否有文书
		/* String sql_pub_blob = "select * from pub_blob a,ws_template b where (a.ZFBS = '0' or a.ZFBS IS NULL) and a.ws_template_id = b.ws_template_id and a.SJBH='"
				+ sjbh
				+ "' and a.YWLX = '"
				+ ywlx
				+ "' order by is_sp_flag desc";
		QuerySet qs = DBUtil.executeQuery(sql_pub_blob, null);

		for (int j = 0; j < qs.getRowCount(); j++) {
			if ("1".equals(qs.getString(1 + j, "IS_SP_FLAG"))) {
				sp_fjbh = qs.getString(1 + j, "FJBH");
			}
			ws_template_id += qs.getString(1 + j, "WS_TEMPLATE_ID")
					+ ",";
			ws_template_name += qs.getString(1 + j, "FILENAME") + ",";
			fjbh += qs.getString(1 + j, "fjbh") + ",";
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
		} */
		//通过sjbh获取AP_PROCESSCONFIG表的AP_PROCESS_ID主键作为附件查询条件
		String getAP_PROCESSCONFIG = "select AP_PROCESS_ID from AP_PROCESSCONFIG  where SJBH='"+sjbh+"' and YWLX = '"+ywlx+"'";
      	QuerySet rspk = DBUtil.executeQuery(getAP_PROCESSCONFIG,null);
		if(rspk!= null && rspk.getRowCount() > 0){
			ywid = rspk.getString(1, 1);
		}
		//add by cbl end
		
	} catch (Exception ex) {
		ex.printStackTrace();
	} finally {
		if (conn != null) {
			conn.close();
		}
	}
  
%>

<title>打印处理</title>
<app:base/>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script language="javascript" >

g_nameMaxlength = 20;
var ajbh=1;
var sjbh='<%=sjbh%>';
var ywlx='<%=ywlx%>';

var isedit='<%=isEdit%>';
var condition = '<%=condition%>';
var ws_template_id = '<%=ws_template_id%>';
function closeT(){

  window.close();
}

$(function() {
	$("#wsTemplateDiv").height(getDivStyleHeight());
	setFileData("","","<%=sjbh%>","<%=ywlx%>");

	if(pxZxm != "") {
		<%
			String fjlbSql = "select distinct fjlb from fs_fileupload where sjbh='" + sjbh+ "' and ywlx = '" + ywlx + "'";
		  	String[][] fjlbResult = DBUtil.query(fjlbSql);
		  	String fjlb = "3999,";// 默认
		  	if(fjlbResult != null) {
			  	for(int i = 0; i < fjlbResult.length; i++) {
			  		fjlb += fjlbResult[i][0]+ ",";
			  	}
		  	}
		  	fjlb = fjlb.endsWith(",") ? fjlb.substring(0, fjlb.lastIndexOf(",")) : fjlb;
		%>
		$("#fileTbody").attr("fjlb","<%=fjlb %>");

		var pxZxm = '<%=pxZxm %>';
		queryFileData(pxZxm, "", "", "");
	}
	
	queryFileData("","","<%=sjbh%>","<%=ywlx%>");
	$("#blrBtn").click(function(){
		var actorCode = $("#ACTOR").attr("code");
		openUserTree('multi',actorCode,'setBlr') ;
	});
	$("#blbmBtn").click(function(){
		var deptidCode = $("#DEPTID").attr("code");
		openDeptTree('single',deptidCode,'setBlbm') ;
	});
	
});

function showLct(){
	$(window).manhuaDialog({"title":"查看流程图","type":"text","content":"${pageContext.request.contextPath }/jsp/framework/common/aplink/spFlowView_gd.jsp?sjbh=<%=sjbh%>&spbh=<%=spbh%>","modal":"1"});
}

function saveWS(){

    var form1 = iFrame.wsTemplate;
    var data1 = null;
    if(form1)
    {
    	var json = Form2Json.formToJSON(form1);
    	if(json.length <= 2){
    		data1= defaultJson.packSaveJson('{"SJBH":"<%=sjbh%>","YWLX":"<%=ywlx%>","WSTEMPLATEID":"<%=ws_template_id%>"}');
    	} else {
    	  	json = json.replace('}',',"SJBH":"<%=sjbh%>","YWLX":"<%=ywlx%>","WSTEMPLATEID":"<%=ws_template_id%>"}');
    	  	
    	   	data1 = defaultJson.packSaveJson(json);
    	   	
    	}
    }else
    {
    	data1= defaultJson.packSaveJson('{"SJBH":"<%=sjbh%>","YWLX":"<%=ywlx%>","WSTEMPLATEID":"<%=ws_template_id%>"}');
    }
    var wsActionURL = "/xmgl/PubWS.do?saveWSJson";
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
			if("0"==flag)
			{
				alert("文书保存出错！");
				return ;	
			}else
			{
				
				var sjbh="<%=sjbh%>";
				var ywlx="<%=ywlx%>";
				var dbr = "";
				var dbrName="";
				if($("#ACTOR")[0].tagName=="INPUT"){
					dbr = $("#ACTOR").attr("code");
					dbrName = $("#ACTOR").val();
				}
				if($("#ACTOR")[0].tagName=="SELECT"){
					dbr = $("#ACTOR").val();
					dbrName = $("#ACTOR").find("option:selected").text(); 
				}		
			//	alert(dbr)
				if(!dbr||dbr=="")
				{
					requireFormMsg("请选择办理人!");
					//$("#ACTOR").focus();
					return;
				}
			
					 doSPAgain(dbrName); 
				
			}

		},
	    error : function(result) {
	     	//alert(234);
	    }
	});


}

function deleteProcess(){
    xConfirm("提示信息","是否确认删除此审批?");
	$('#ConfirmYesButton').unbind();
 	$('#ConfirmYesButton').attr('click','');
 	$('#ConfirmYesButton').bind("click",function(){
 		var stAction = "<%=request.getContextPath()%>/TaskBackAction.do?deleteProcess";
 	    var data1 = "sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&id=<%=taskid%>&seq=<%=taskseq%>&spbh=<%=spbh%>";
 		$.ajax({
 			type : 'post',
 			url : stAction,
 			data : data1,
 			dataType : 'json',
 			async :	false,
 			success : function(result) {
 				var fuyemian=$(window).manhuaDialog.getParentObj();
 				var s = fuyemian.gengxinchaxun;
			     if(s){
				    fuyemian.gengxinchaxun(result.Msg);
			     }
 				//fuyemian.gengxinchaxun(result.Msg);
 				$(window).manhuaDialog.close();

 			},
 		    error : function(result) {
 		    	 //alert(result);
 			     //alert(result.msg);
 		    	 xAlertMsg(result.Msg);
 			}
 		});
 		
 	});  
	
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
	$("#ACTOR").val(userName);
	$("#ACTOR").attr("code",userId);

}
function setBlbm(data){
	var deptid = "";
	var deptname = "";
	var deptFzr = "";
	var deptFzrName = "";

	for(var i=0;i<data.length;i++){
 	 var tempObj =data[i];
 	 if(i<data.length-1){
 		deptid +=tempObj.DEPTID+",";
 		deptname +=tempObj.DEPTNAME+",";
 		deptFzr +=tempObj.FZR+",";
 		deptFzrName +=tempObj.FZRNAME+"";
 	 }else{
 		deptid +=tempObj.DEPTID;
 		deptname +=tempObj.DEPTNAME; 
 		deptFzr +=tempObj.FZR;
 		deptFzrName +=tempObj.FZRNAME;

 	 }
	}
	document.getElementById("DEPTID").value=deptname;
	$("#DEPTID").attr("code",deptid);
	document.getElementById("ACTOR").value=deptFzrName;
	$("#ACTOR").attr("code",deptFzr);
}
function printDiv()
{
	window.frames["iFrame"].printIframe();
	
}
function viewSpyj(){

	$(window).manhuaDialog({"title":"审批意见","type":"text","content":"<%=request.getContextPath()%>/jsp/framework/common/aplink/spYjView.jsp?sjbh=<%=sjbh%>&spbh=<%=spbh%>","modal":"2"});	
}
function doJssp()
{
	 customOperation();
}
function doArchiveTask()
{

  xConfirm("提示信息","是否确定结束审批?");
	$('#ConfirmYesButton').unbind();
	$('#ConfirmYesButton').attr('click','');
	$('#ConfirmYesButton').one("click",function(){
		
		doJssp();
		
	});  
        
}
function customOperation()
{
    var strActionUrl = "<%=request.getContextPath()%>/TaskBackAction.do?customOperationAction";
//    alert(strActionUrl);
	var data1 = "eventid=<%=sjbh%>&ywlx=<%=ywlx%>&spbh=<%=spbh%>&taskId=<%=taskid%>";
		$.ajax({
			type : 'post',
			url : strActionUrl,
			data : data1,
			dataType : 'json',
			async :	false,
			success : function(result) {
				
				// xAlertMsg(result.Msg);
				 var fuyemian=$(window).manhuaDialog.getParentObj();
					var s = fuyemian.gengxinchaxun;
				     if(s){
					    fuyemian.gengxinchaxun(result.Msg);
				     }
				
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

    return true;
}
function doSPAgain(dbr)
{
     xConfirm("提示信息","是否重新提请审批？办理人："+dbr);
 	$('#ConfirmYesButton').unbind();
 	$('#ConfirmYesButton').attr('click','');
 	$('#ConfirmYesButton').one("click",function(){
 		
 		doJsspAgain();
 		
 	});  

    
}
function doJsspAgain()
{

	var stAction = "<%=request.getContextPath()%>/TaskBackAction.do?doSpAgainAction";
    var data1 = "sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&id=<%=taskid%>&seq=<%=taskseq%>&spbh=<%=spbh%>&mind="+encodeURIComponent($("#mind").val());
	$.ajax({
		type : 'post',
		url : stAction,
		data : data1,
		dataType : 'json',
		async :	false,
		success : function(result) {
			var fuyemian=$(window).manhuaDialog.getParentObj();
		//	fuyemian.gengxinchaxun(result.Msg);
			var s = fuyemian.gengxinchaxun;
		     if(s){
			    fuyemian.gengxinchaxun(result.Msg);
		     }
			$(window).manhuaDialog.close();

		},
	    error : function(result) {
	    	 //alert(result);
		     //alert(result.msg);
	    	 xAlertMsg(result.Msg);
		}
	});
}
function breakSP(){
	  xConfirm("提示信息","是否中断此审批？");
		$('#ConfirmYesButton').unbind();
	 	$('#ConfirmYesButton').attr('click','');
	 	$('#ConfirmYesButton').one("click",function(){
	 		
	 		breakSP_();
	 		
	 	});  
	
}
function breakSP_(){
	var stAction = "<%=request.getContextPath()%>/TaskBackAction.do?doBreakSPAction";
    var data1 = "sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&id=<%=taskid%>&seq=<%=taskseq%>&spbh=<%=spbh%>";
	$.ajax({
		type : 'post',
		url : stAction,
		data : data1,
		dataType : 'json',
		async :	false,
		success : function(result) {
			var fuyemian=$(window).manhuaDialog.getParentObj();
		//	fuyemian.gengxinchaxun(result.Msg);
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
		}
	});
}

function closeNowCloseFunction() {
	try {
		$(window).manhuaDialog.getParentObj().prcCallback();
		$(window).manhuaDialog.getParentObj().getMessage(result.message);
		$(window).manhuaDialog.close();
	} catch(e) {
		$(window).manhuaDialog.close();
	}
}
</script>
</head>
<body TOPMARGIN="0" LEFTMARGIN="0" style="overflow:visible" >
<app:dialogs/>
<div class="container-fluid">

    <div class="row-fluid" >
		<div class="B-small-from-table-autoConcise">
        	<div class="span7" style="height:80%;overflow:hidden;">
        		<%
        			if("200503".equals(ywlx) && "43486".equals(operationoid)) {
    			%>
    			<iframe name="iFrame" id="wsTemplateDiv" width="100%" height="95%" src="/xmgl/jsp/framework/common/gwglLink/fwWs.jsp?sjbh=<%=sjbh %>&isview=1" FRAMEBORDER=0 SCROLLING= ></iframe>
    			<%
        			} else if(ws_template_id!=null&&!"".equals(ws_template_id)){
        		%>
        		<iframe name="iFrame" id="wsTemplateDiv" width="100%" height="95%" src="<%=request.getContextPath()%>/PubWS.do?getPrintAction&templateid=<%=ws_template_id %>&isrefresh=1&ywlx=<%=ywlx%>&sjbh=<%=sjbh%>&isEdit=<%=isEdit %>&rowid=<%=System.currentTimeMillis() %>.mht" FRAMEBORDER=0 SCROLLING= ></iframe>
        		<%
        			}else {
        		%>
        		<iframe name="iFrame" id="wsTemplateDiv" width="100%" height="95%" src="${pageContext.request.contextPath }/jsp/framework/common/aplink/spFlowView_gd.jsp?sjbh=<%=sjbh%>&spbh=<%=spbh%>" FRAMEBORDER=0 SCROLLING= ></iframe>
        		<% } %>
             	
			</div>
			
    		<div class="span5" style="height:80%;overflow:hidden;">

		    		<h4 class="title" style="width:100%">

						<span class="pull-right">
						    <button id="cmdZdlc" class="btn" onclick="breakSP()" type="button">中断流程</button>
							<button id="cmdZctp" class="btn" onclick="saveWS()" type="button">再次提请</button>
							<button id="cmdSclc" class="btn" style="display:none" onclick="deleteProcess()" type="button">删除流程</button>
           	           	   <%--  <%if(isview==null || !("1".equals(isview))){ %>
           		            <button id="cmdGD" class="btn" onclick="doArchiveTask()" type="button">审批结束</button>
           		            <%}%> --%>
           		            <% if(ws_template_id!=null&&!"".equals(ws_template_id)){ %>
           		            <button id="cmdSpws" class="btn btn-link" onclick="showLct()" type="button"><i class="icon-book"></i>查看流程图</button>
           		            <% } %>
		                    <button id="cmdSpyj" class="btn btn-link" onclick="viewSpyj()" type="button"><i class="icon-th-list"></i>查看退回意见</button>
		                    <% if(ws_template_id!=null&&!"".equals(ws_template_id)){ %>
		              		<button id="example_query" class="btn btn-link" type="button" onclick="printDiv()"><i class="icon-print"></i>打印</button>
		              		<% } %>
						</span>
					</h4>
           	       
                       <form method="post" action="" id="insertForm">
                       <div class="windowOpenAuto" style="height:90%;overflow:auto;">
                       <table class="B-table" width="100%">
                            <% if(("4".equals(processtype))){ %>
                       		<tr style="display:<%if(!("4".equals(processtype))) out.println("none");%>">
								<th class="right-border bottom-border" style="font-size:14px;">办理人</th>
								<td class="bottom-border right-border"  colspan="7">
			         			<input class="span12"  type="text" check-type="required" fieldname="ACTOR" id="ACTOR" name ="ACTOR" style="width:75%" disabled />
			         			<button class="btn btn-link"  type="button" id="blrBtn"><abbr title="点击选择办理人"><i class="icon-edit"></i></abbr></button>
			       	 			</td>
							</tr>
							<%}else {
								out.println(html);
							  }
							 %> 
						<tr>
							<th width="12%"  align="center" valign="middle" style="text-align:center;font-size:14px;">办理意见</th>
						    <td width="88%" colspan="7" class="bottom-border right-border">
								<div><textarea class="span12" rows="3" name="txtCheckMind" id="mind" maxLength="1500"> </textarea></div>
						    </td>
						</tr>
						
						<tr>
						<th width="12%" class="right-border bottom-border text-right">附件信息</th>
						<td width="88%" colspan="7" class="bottom-border right-border">
							<div>
								<span  class="btn btn-fileUpload" onclick="doSelectFile(this);"	fjlb="3999"> <i class="icon-plus"></i> <span>添加文件...</span>
									</span>
								<table role="presentation" class="table table-striped">
									<tbody id="fileTbody" class="files showFileTab" notClear="true" deleteUser="<%=user.getAccount()%>" 
										data-toggle="modal-gallery" data-target="#modal-gallery" showLrr="true" showSize="false" onlyView="true">
									</tbody>
								</table>
								
							</div>
						</td>
					   </tr>
					   
					   
					   
					   <!-- 选择排迁子项目时，把该子项目的委托函、联络单、审定表附件带到合同中。 -->
					   <% if(!"".equals(pxZxm)) { %>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">
								委托函
							</th>
							<td width="92%" colspan=7 class="bottom-border">
								<div>
									<table role="presentation" class="table table-striped">
										<tbody fjlb="0005" class="files showFileTab" onlyView="true" notClear="true" showLrr="true" 
											data-toggle="modal-gallery" data-target="#modal-gallery">
										</tbody>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">
								排迁联络单
							</th>
							<td width="92%" colspan=7 class="bottom-border">
								<div>
									<table role="presentation" class="table table-striped">
										<tbody fjlb="0002" class="files showFileTab" onlyView="true" notClear="true" showLrr="true" 
											data-toggle="modal-gallery" data-target="#modal-gallery">
										</tbody>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">
								预算审定表
							</th>
							<td width="92%" colspan=7 class="bottom-border">
								<div>
									<table role="presentation" class="table table-striped">
										<tbody fjlb="0004" class="files showFileTab" onlyView="true" notClear="true" showLrr="true" 
											data-toggle="modal-gallery" data-target="#modal-gallery">
										</tbody>
									</table>
								</div>
							</td>
						</tr>
						<% } %>
                       </table>
                        </div>
                       </form>
                  
                   
				   <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
              </div>
        </div>
    </div>
</div>
</body>

</html>