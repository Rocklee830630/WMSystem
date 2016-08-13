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


<%
   User user = (User) session.getAttribute(Globals.USER_KEY);

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
	String operation = "";
	Process process = null;
	Step nextStep = null;
	TaskMgrBean tss = new TaskMgrBean();
	String html ="";
	try {
		if(Pub.empty(spbh)){
	        String sql_info = "select PROCESSOID,PROCESSTYPEOID,to_char(CREATETIME,'yyyy-mm-dd hh24:mi:ss'),to_char(CLOSETIME,'yyyy-mm-dd hh24:mi:ss'),EVENTID,STATE,OPERATIONOID,MEMO,TASKOID,PROCESSEVENT,RESULT,RESULTDSCR,YXBS from ap_processinfo"
	                + " where EVENTID='"+sjbh+"'";
	        String[][] list_info = DBUtil.query(sql_info);
			if(list_info!=null&&list_info.length>0){
				spbh = list_info[0][0];
				operation = list_info[0][1];
			}
		}
		String ap_task_schedule_sql = "select id,seq from ap_task_schedule where sjbh='"+sjbh+"' and rwzt='01'";
		QuerySet qs_ = DBUtil.executeQuery(ap_task_schedule_sql, null);
		if(qs_ != null&&qs_.getRowCount()>0){
			taskid = qs_.getString(1, 1);
			taskseq = qs_.getString(1, 2);
		}
		
		process = TaskBO.getProcess(conn, spbh);
		nextStep = process.getNextStep();
		ProcessDetail p = (ProcessDetail)nextStep;
		int seq = 0;
		if(p!=null){
			seq = p.getStepSequence();
		}
		html = tss.SelectPersonHtml_SPBL(operation,seq,spbh,user);

		//判断是否有文书
		String sql_pub_blob = "select * from pub_blob a,ws_template b where (a.ZFBS = '0' or a.ZFBS IS NULL) and a.ws_template_id = b.ws_template_id and a.SJBH='"
				+ sjbh
				+ "' and a.YWLX = '"
				+ ywlx
				+ "' order by is_sp_flag desc";
		QuerySet qs = DBUtil.executeQuery(sql_pub_blob, null);
		if(qs != null){
			for (int j = 0; j < qs.getRowCount(); j++) {
				if ("1".equals(qs.getString(1 + j, "IS_SP_FLAG"))) {
					sp_fjbh = qs.getString(1 + j, "FJBH");
				}
			}
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

<app:base/>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script language="javascript" >
g_nameMaxlength = 20;
var ajbh=1;
var sjbh='<%=sjbh%>';
var ywlx='<%=ywlx%>';

var isedit='<%=isEdit%>';
var condition = '<%=condition%>';
function closeT(){

  window.close();
}

$(function() {
	setFileData("","","<%=sjbh%>","<%=ywlx%>");
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
//关闭事件
function closeNowCloseFunction()
{
   window.parent.getProcess();
}
function jumpStep(){
	 var spUser = "";
	 var spUserName = "";
	 if($("#ACTOR")[0].tagName=="INPUT"){
		  spUser = $("#ACTOR").attr("code");
		  spUserName = $("#ACTOR").val();
	 }
	 if($("#ACTOR")[0].tagName=="SELECT"){
		  spUser = $("#ACTOR").val();
		  spUserName = $("#ACTOR").find("option:selected").text(); 
	 }					
	 if(!spUser||spUser=="")
	 {
		requireFormMsg("无法指定下一办理人，不能自动越级审批!");
		return;
	 }
	 var resultValue = "1";
	 var resultDscr = "自动越级审批";
	 var ms = "是否确定自动越级审批?办理人："+spUserName;
	 var strAction = "${pageContext.request.contextPath }/TaskAction.do?doApprove";
		var data1 = "id=<%=taskid%>&seq=<%=taskseq%>&sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&spbh=<%=spbh%>&spjg="+resultValue+"&sfth="+(resultValue==3?1:0)+"&spUser="+spUser;
		//data1+= "&tsdept="+document.all('USERTODEPT').value+"&tsperson="+document.all('USERTOPERSON').value+"&tsstep="+document.all("USERSTEP").value;
		data1+= "&result="+resultValue+"&resultDscr="+encodeURIComponent(resultDscr)+"&fjbh=<%=sp_fjbh%>&autodoApprove=1";
		    xConfirm("提示信息",ms);
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').attr('click','');
			$('#ConfirmYesButton').one("click",function(){
				$.ajax({
					type : 'post',
					url : strAction,
					data : data1,
					dataType : 'json',
					async :	true,
					success : function(result) {

						 var fuyemian=$(window).manhuaDialog.getParentObj();
						     var s = fuyemian.gengxinchaxun;
						     if(s){
							    fuyemian.gengxinchaxun(result.message);
						     }
							 success = true;
							 $(window).manhuaDialog.close();

					},
				    error : function(result) {
				    	xAlertMsg(result.message);
					    success = false;
					}
				});
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
</script>
</head>
<body TOPMARGIN="0" LEFTMARGIN="0" style="overflow:visible" >
<app:dialogs/>
<div class="container-fluid">

    <div class="row-fluid" >
		<div class="B-small-from-table-autoConcise">
        	<div class="span7" style="height:80%;overflow:hidden;">

        		<iframe name="iFrame" width="100%" height="95%" src="${pageContext.request.contextPath }/jsp/framework/common/aplink/spFlowView_gd.jsp?sjbh=<%=sjbh%>&spbh=<%=spbh%>" FRAMEBORDER=0 SCROLLING= ></iframe>
             	
			</div>
			
    		<div class="span5" style="height:80%;overflow:hidden;">

		    		<h4 class="title" style="width:100%">

						<span class="pull-right">
                            <button id="jumpStep" class="btn" onClick="jumpStep()" type="button">自动越级审批</button>
		                    <button id="cmdSpyj" class="btn btn-link" onclick="viewSpyj()" type="button"><i class="icon-th-list"></i>查看办理意见</button>
		                   
						</span>
					</h4>
           	           <table width="100%" class="table" style="margin-bottom:2px;" id="DT1">
           	             <tr>
           	              <%=html%>
           	             </tr>
           	           </table>
                       <form method="post" action="" id="insertForm">
                       <div class="windowOpenAuto" style="height:90%;overflow:auto;">
                       <table class="B-table" width="100%">
                            
						<th width="12%" class="right-border bottom-border text-right">附件信息</th>
						<td width="88%" colspan="7" class="bottom-border right-border">
							<div>
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
        </div>
    </div>
</div>

</body>

</html>