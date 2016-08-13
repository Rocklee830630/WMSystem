<!DOCTYPE html>
<html>
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


<%
	User user = (User) session.getAttribute(Globals.USER_KEY);
   String userid = user.getAccount();
   String isEdit="0";
   String isview=Pub.val(request, "isview");
   String taskid = Pub.val(request, "taskid");
   String taskseq = Pub.val(request, "taskseq");
   String sjbh=(String)request.getParameter("sjbh");
   String ywlx=(String)request.getParameter("ywlx");
	
   String operationoid = (String)request.getParameter("operationoid");
   if(operationoid==null){
	   operationoid = "";
   }
   String processtype = (String)request.getParameter("processtype");
   if(processtype == null){
	   processtype = "1";   
   }
   String templateid="";
   templateid=(String)request.getParameter("temlateid");
   String spzt = request.getParameter("spzt");
   if(spzt == null || "".equals(spzt)) spzt = "9";
   
   String ywid = (String)request.getParameter("ywid");
   String html ="";
   if("1".equals(processtype)){
	   TaskMgrBean t = new TaskMgrBean();
	 html = t.showSelectPersion(operationoid,user);
   }

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
	String result = "";
	String cjrid = "";
	
	String pxZxmSql = "select pq_zxm.gc_pq_zxm_id from GC_HTGL_HT ht,gc_pq_zxm pq_zxm "
			+ "where ht.id=pq_zxm.htid "
			+ "and ht.sjbh='" + sjbh+ "' and ht.ywlx='" + ywlx + "'";
	  String[][] pxZxmResult = DBUtil.query(pxZxmSql);
	  String pxZxm = pxZxmResult == null ? "" : pxZxmResult[0][0];
	try {
		if(Pub.empty(spbh)){
	        String sql_info = "select PROCESSOID,PROCESSTYPEOID,to_char(CREATETIME,'yyyy-mm-dd hh24:mi:ss'),to_char(CLOSETIME,'yyyy-mm-dd hh24:mi:ss'),EVENTID,STATE,OPERATIONOID,MEMO,TASKOID,PROCESSEVENT,RESULT,RESULTDSCR,YXBS,CJRID from ap_processinfo"
	                + " where EVENTID='"+sjbh+"'";
	        String[][] list_info = DBUtil.query(sql_info);
			if(list_info!=null&&list_info.length>0){
				spbh = list_info[0][0];
				result =list_info[0][10];
				cjrid = list_info[0][13];
				operationoid =  list_info[0][1];
			}
		}
		
		//判断是否有文书
		String sql_pub_blob = "select * from pub_blob a,ws_template b where (a.ZFBS = '0' or a.ZFBS IS NULL) and a.ws_template_id = b.ws_template_id and a.SJBH='"
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
		}
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
<head>

<title>打印处理</title>
<app:base/>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script language="javascript" >
g_nameMaxlength = 20;
var ajbh=1;
var sjbh='<%=sjbh%>';
var ywlx='<%=ywlx%>';
var templateid = '<%=ws_template_id%>';
var isedit='<%=isEdit%>';
var condition = '<%=condition%>';
var processtype = '<%=processtype%>';
var operationoid = '<%=operationoid%>';

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

function saveWS_delete(){

    var form1 = iFrame.wsTemplate;
    var data1 = null;
    if(form1)
    {
    	var json = Form2Json.formToJSON(form1);
    	if(json.length <= 2){
    		data1= defaultJson.packSaveJson('{"SJBH":"<%=sjbh%>","YWLX":"<%=ywlx%>","WSTEMPLATEID":"<%=templateid%>"}');
    	} else {
    	  	json = json.replace('}',',"SJBH":"<%=sjbh%>","YWLX":"<%=ywlx%>","WSTEMPLATEID":"<%=templateid%>"}');

    	   	data1 = defaultJson.packSaveJson(json);
    	}
    }else
    {
    	data1= defaultJson.packSaveJson('{"SJBH":"<%=sjbh%>","YWLX":"<%=ywlx%>","WSTEMPLATEID":"<%=templateid%>"}');
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
				//isTS = isTs('<%=operationoid%>');
				
				if("4"=="<%=processtype%>"){
					var dbr = $("#ACTOR").attr("code");
					if(!dbr)
					{
						requireFormMsg("请选择办理人!");
						//$("#ACTOR").focus();
						return;
					}
					CQSPTS(ajbh,sjbh,ywlx,"",'',"",condition,'<%=operationoid%>',dbr);
				}else{
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
					if(!dbr&&dbr=="")
					{
						requireFormMsg("请选择办理人!");
						//$("#ACTOR").focus();
						return;
					}
					var mind = $("#mind").val();
					CQSP("",sjbh,ywlx,"",'',"",'<%=condition%>','<%=operationoid%>',dbr,dbrName,mind);
					//window.parent.getProcess();
				}
				
				
			}

		},
	    error : function(result) {
	     	//alert(234);
	    }
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
		success : function(result) {
		}
	});


}

function doArchiveTask()
{
  xConfirm("提示信息","是否确定结束审批?");
	$('#ConfirmYesButton').unbind();
	$('#ConfirmYesButton').attr('click','');
	$('#ConfirmYesButton').one("click",function(){
		
	<%
	if("200503".equals(ywlx)) {
    %>
    saveWS();
    <%
    }%>
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
				     }else{
			        	 var frameW = $(window).manhuaDialog.getFrameObj();
			        	 var w = frameW.gengxinchaxun;
			        	 if(w){
			        		 frameW.gengxinchaxun(result.Msg);
			        	 }
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
//恢复审批
function hfzd(){
	  xConfirm("提示信息","是否恢复中断此审批？");
		$('#ConfirmYesButton').unbind();
	 	$('#ConfirmYesButton').attr('click','');
	 	$('#ConfirmYesButton').one("click",function(){
	 		
	 		regainSP();
	 		
	 	});  
}

function regainSP(){
	var stAction = "<%=request.getContextPath()%>/TaskBackAction.do?doRegainSPAction";
    var data1 = "sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&spbh=<%=spbh%>";
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

//重新发起审批
function cxfq(){
	xConfirm("提示信息","重新发起审批会删除历史记录后再次发起，是否确认重新审批信息！");
	$('#ConfirmYesButton').unbind();
	$('#ConfirmYesButton').attr('click','');
 	$('#ConfirmYesButton').one("click",function(){
 		var stAction = "<%=request.getContextPath()%>/TaskBackAction.do?deleteProcess";
 	    var data1 = "sjbh=<%=sjbh%>&spbh=<%=spbh%>&ywlx=<%=ywlx%>&isgly=1";
 	    var success = 0;
 		$.ajax({
 			type : 'post',
 			url : stAction,
 			data : data1,
 			dataType : 'json',
 			async :	false,
 			success : function(result) {
 				var status = result.status;
 				var msg = result.Msg;
 				if(status=="sucess"){
 					success = 1;
 				}
 		  		//  var rowindex = $("#DT1").getSelectedRowIndex();
 				//  $("#DT1").removeResult(rowindex);
 				//xAlert("信息提示",msg);
 			},
 		    error : function(result) {
 		    	 //alert(result);
 			     //alert(result.msg);
 		    	 xAlertMsg(result.Msg);
 			}
 		});
 		if(success==1){
 		
 			if(sjbh==null ||sjbh == undefined || sjbh =="")
 				return;
 		    var wsActionURL = "${pageContext.request.contextPath }/PubWS.do?getXMLPrintAction|templateid="+templateid+"|isEdit=1|ywlx="+ywlx+"|sjbh="+sjbh+"|rowid="+Math.random()+".mht";
 		    wsActionURL = "${pageContext.request.contextPath }/jsp/framework/print/pubPrint.jsp?param="+wsActionURL+"&ymmc=defaultArchivePage"+
 		    		"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid="+templateid+"&isEdit=1"+"&operationoid="+operationoid+"&processtype="+processtype;
 		    $(window).manhuaDialog({"title":"流程申请","type":"text","content":wsActionURL,"modal":"1"});

 		}
 		
 		
 	});
}
function closeFunction(){
	xConfirm("提示信息","重新发起审批会删除历史记录后再次发起，是否确认重新审批信息！");
	$('#ConfirmYesButton').unbind();
	$('#ConfirmYesButton').attr('click','');
 	$('#ConfirmYesButton').one("click",function(){
 		var stAction = "<%=request.getContextPath()%>/TaskBackAction.do?deleteProcess";
 	    var data1 = "sjbh=<%=sjbh%>&spbh=<%=spbh%>&ywlx=<%=ywlx%>&isgly=1";
 	    var success = 0;
 		$.ajax({
 			type : 'post',
 			url : stAction,
 			data : data1,
 			dataType : 'json',
 			async :	false,
 			success : function(result) {
 				var status = result.status;
 				var msg = result.Msg;
 				if(status=="sucess"){
 					success = 1;
 				}
 		  		//  var rowindex = $("#DT1").getSelectedRowIndex();
 				//  $("#DT1").removeResult(rowindex);
 				//xAlert("信息提示",msg);
 			},
 		    error : function(result) {
 		    	 //alert(result);
 			     //alert(result.msg);
 		    	 xAlertMsg(result.Msg);
 			}
 		});
 		if(success==1){
 		
 			if(sjbh==null ||sjbh == undefined || sjbh =="")
 				return;
 		    var wsActionURL = "${pageContext.request.contextPath }/PubWS.do?getXMLPrintAction|templateid="+templateid+"|isEdit=1|ywlx="+ywlx+"|sjbh="+sjbh+"|rowid="+Math.random()+".mht";
 		    wsActionURL = "${pageContext.request.contextPath }/jsp/framework/print/pubPrint.jsp?param="+wsActionURL+
 		    		"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid="+templateid+"&isEdit=1"+"&operationoid="+operationoid+"&processtype="+processtype;
 		    $(window).manhuaDialog({"title":"流程申请","type":"text","content":wsActionURL,"modal":"1"});

 		}
 		
 		
 	});
	
}
function closeNowCloseFunction(){
//	return "1";
}

function closeWindow(obj){
	obj.manhuaDialog.close();
	setInterval(closeMySelf, 1200);
}
function closeMySelf(){
	try {
		closeLoading3();
		$(window).manhuaDialog.getParentObj().gengxinchaxun();
		$(window).manhuaDialog.close();
	} catch(e) {
		closeLoading3();
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
		<%if("700204".equals(ywlx)) {%>
				<div  id="iframeright" style="width=100%;height:30%;overflow:hidden;" >

		    		<h4 class="title" style="width:100%">

						<span class="pull-right">

		             	 <%if(isview==null || !("1".equals(isview))){ %>
           		            <button id="cmdGD" class="btn" onclick="doArchiveTask()" type="button">审批结束</button>
           		            <%}%>
           		            <%if(result!=null && ("3".equals(result))&&userid.equals(cjrid)){ %>
           		            <button id="cmdHfzd" class="btn" onclick="hfzd()" type="button">恢复中断审批</button>
           		            <button id="cmdCxfq" class="btn" onclick="cxfq()" type="button">重新发起审批</button>
           		             <%}%>
           		            <button id="cmdSpws" class="btn btn-link" onclick="showLct()" type="button"><i class="icon-book"></i>查看流程图</button>
		                    <button id="cmdSpyj" class="btn btn-link" onclick="viewSpyj()" type="button"><i class="icon-th-list"></i>查看办理意见</button>
		              		<button id="example_query" class="btn btn-link" type="button" onclick="printDiv()"><i class="icon-print"></i>打印</button>
						</span>
					</h4>
           	        <form method="post" action="" id="insertForm">
                       <div class="windowOpenAuto" style="height:90%;overflow:auto;">
                       <table class="B-table" width="100%">
                        <tr>
						<th width="12%" class="right-border bottom-border text-right">附件信息</th>
						<td width="88%" colspan="7" class="bottom-border right-border">
							<div>
								<input type="hidden" name="sjbh" id="sjbh" value="<%=sjbh%>">  
								<table role="presentation" class="table table-striped">
									<tbody  class="files showFileTab"
										data-toggle="modal-gallery" data-target="#modal-gallery" showLrr="true" showSize="false" onlyView="true">
									</tbody>
								</table>
								
							</div>
						</td>
					   </tr>
                       </table>
                        </div>
                       </form>                  
                   
				   <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
              </div> 
              			
		
               <div id="iframediv" style="width: 100%;height: 70%;overflow:hidden;">
             	<iframe  style="width: 100%;height: 100%;" name="iFrame" src="${pageContext.request.contextPath}/jsp/business/zjgl/ykWs.jsp?sjbh=<%=sjbh %>&ywlx=<%=ywlx %>&isEdit=1" FRAMEBORDER=0 SCROLLING=></iframe>
				</div>
			<%}else{ %>
        	<div class="span7" id="wsTemplateDiv" style="overflow:hidden;">
        		<%
        			if("200503".equals(ywlx) && "43486".equals(operationoid)) {
    			%>
    			<iframe name="iFrame" width="100%" height="95%" src="/xmgl/jsp/framework/common/gwglLink/fwWs.jsp?sjbh=<%=sjbh %>&isview=<%=isview %>" FRAMEBORDER=0 SCROLLING= ></iframe>
    			<%
        			} else if(ws_template_id!=null&&!"".equals(ws_template_id)){
        		%>
        		<iframe name="iFrame" width="100%" height="95%" src="<%=request.getContextPath()%>/PubWS.do?getPrintAction&templateid=<%=ws_template_id %>&ywlx=<%=ywlx%>&sjbh=<%=sjbh%>&isrefresh=1&rowid=<%=System.currentTimeMillis() %>.mht" FRAMEBORDER=0 SCROLLING= ></iframe>
        		<%
        			}else {
        		%>
        		<iframe name="iFrame" width="100%" height="95%" src="${pageContext.request.contextPath }/jsp/framework/common/aplink/spFlowView_gd.jsp?sjbh=<%=sjbh%>&spbh=<%=spbh%>" FRAMEBORDER=0 SCROLLING= ></iframe>
        		<% } %>
             	
			</div>
			
    		<div class="span5" style="height:80%;overflow:hidden;">

		    		<h4 class="title" style="width:100%">

						<span class="pull-right">

           	           	    <%if(isview==null || !("1".equals(isview))){ %>
           		            <button id="cmdGD" class="btn" onclick="doArchiveTask()" type="button">审批结束</button>
           		            <%}%>
           		            <%if(result!=null && ("3".equals(result))&&userid.equals(cjrid)){ %>
           		            <button id="cmdHfzd" class="btn" onclick="hfzd()" type="button">恢复中断审批</button>
           		            <button id="cmdCxfq" class="btn" onclick="cxfq()" type="button">重新发起审批</button>
           		             <%}%>
           		            <% if(ws_template_id!=null&&!"".equals(ws_template_id)){ %>
           		            <button id="cmdSpws" class="btn btn-link" onclick="showLct()" type="button"><i class="icon-book"></i>查看流程图</button>
           		            <% } %>
		                    <button id="cmdSpyj" class="btn btn-link" onclick="viewSpyj()" type="button"><i class="icon-th-list"></i>查看办理意见</button>
		                    <% if(ws_template_id!=null&&!"".equals(ws_template_id)){ %>
		              		<button id="example_query" class="btn btn-link" type="button" onclick="printDiv()"><i class="icon-print"></i>打印</button>
		              		<% } %>
						</span>
					</h4>
           	       
                       <form method="post" action="" id="insertForm">
                       <div class="windowOpenAuto" style="height:90%;overflow:auto;">
                       <table class="B-table" width="100%">
                       <tr>   
						<th width="12%" class="right-border bottom-border text-right">附件信息</th>
						<td width="88%" colspan="7" class="bottom-border right-border">
							<div>
								<table role="presentation" class="table table-striped">
									<tbody id="fileTbody" class="files showFileTab"
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
              	<%} %>
        </div>
    </div>
</div>

</body>

</html>