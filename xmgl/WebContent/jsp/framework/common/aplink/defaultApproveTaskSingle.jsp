<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="com.ccthanking.framework.common.UserTemplate"%>
<%@ page import="com.ccthanking.framework.coreapp.aplink.*"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>

<%

User user = (User)session.getAttribute(Globals.USER_KEY);
	String userName = user.getName();
	String userId = user.getAccount();
	String time = DateTimeUtil.getDateTime("yyyy-MM-dd");
	String timeStr = DateTimeUtil.getDate();
	java.util.Date nowDate = DateTimeUtil.getNow();
	OrgDept dept = user.getOrgDept();
	String deptid = user.getDepartment();
	String deptName = dept.getDeptName();
	String level = String.valueOf(dept.getDeptLevel());
	String pageareaURL = "";
	//pageareaURL = pageareaURL.substring(0,pageareaURL.lastIndexOf("/"))+"/task.jsp";
	String gdspsx=null;
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
td{
	font-size:14px;
	
}
.windowOpenAuto {position:static;}
</style>

<script language="javascript">

g_nameMaxlength=12;
var resultValue;//zl add 全局变量 记录审批结果
var spflag=false;
var IsQM=false; //zl add 判断签名
//--------------------------------zl add gz-------------------------------
var template_ID="";
//--------------------------------------------
var strAction = "${pageContext.request.contextPath}/TaskAction.do?";
var strActionWS = "${pageContext.request.contextPath}/ViewWsAction.do?";
var hasWs = false;

<%
	String taskid = Pub.val(request, "taskid");
	String taskseq = Pub.val(request, "taskseq");
	String sjbh = Pub.val(request, "sjbh");
	String spbh = Pub.val(request, "spbh");
	String ywid = "";
	String spBackRY[][]=null;
	String ywlx = Pub.val(request, "ywlx");
	String rwlx = Pub.val(request, "rwlx");
	String strHh = Pub.val(request, "hh");
	String spjg = null;
	String ws_template_id = "";
	String fjbh = "";
	String sp_fjbh = "";
	String ws_template_name = "";
	String dah = "";
	String ywurl = "";
	if ("".equals(dah)) {
		try {
			//dah = ApproveTaskBO.getDAH(sjbh,ywlx);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	boolean ccFlag = false;
	
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
	String html = "";
	String isth = null;
	boolean isfqr = false;

	  String pxZxmSql = "select pq_zxm.gc_pq_zxm_id from GC_HTGL_HT ht,gc_pq_zxm pq_zxm "
			+ "where ht.id=pq_zxm.htid "
			+ "and ht.sjbh='" + sjbh+ "' and ht.ywlx='" + ywlx + "'";
	  String pxZxm ="";
	try {

		  String[][] pxZxmResult = DBUtil.query(pxZxmSql);
		  pxZxm = pxZxmResult == null ? "" : pxZxmResult[0][0];
		process = TaskBO.getProcess(conn, spbh);
		nowStep = process.open();
		nextStep = process.getNextStep();
		lastStep = process.getLastStep();
		ProcessDetail p = (ProcessDetail)nextStep;
		ProcessDetail nowProcessDetail = (ProcessDetail)nowStep;
		TaskMgrBean tss = new TaskMgrBean();
		String operation =    process.getOperationOID();
		isth = nowProcessDetail.getIsTh();
		int seq = 0;
		if(p!=null){
			seq = p.getStepSequence();
		}
		html = tss.SelectPersonHtml_SPBL(operation,seq,spbh,user);
		CurrentNodeIndex = nowStep.getStepSequence();
		if (TotalNodesCount <= CurrentNodeIndex) {
			//parentDeptId="";
		}
		String rwztSql = "select spjg,yxbs from AP_TASK_SCHEDULE where id='"+taskid+"' and seq='"+taskseq+"' order by cjsj desc";
		String res[][] = DBUtil.query(rwztSql);
		if(res!=null)
		{
			spjg = res[0][0];
			if(res[0][1].length()>0)
			{
				if("0".equals(res[0][1]))
					ccFlag = true;
			}
		}
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
				ws_template_id += qs.getString(1 + j, "WS_TEMPLATE_ID")
						+ ",";
				ws_template_name += qs.getString(1 + j, "FILENAME") + ",";
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
		if (rspk != null&&rspk.getRowCount()>0) {
			ywid = rspk.getString(1, 1);
		}
		String spbackslq = "select t.cjrid,t.cjrxm,t.dbryid,(select name from fs_org_person where account=t.dbryid) as name,t.cjsj,t.stepsequence from AP_TASK_SCHEDULE t "+
						   " where t.sjbh = '"+sjbh+"' and t.ywlx='"+ywlx+"' and t.spbh='"+spbh+"' and t.yxbs = '1' order by t.cjsj";
		spBackRY = DBUtil.query(spbackslq);
	
		String link_sql = "select url from ap_task_link t where ywlx = '"+ywlx+"' and rwlx = '7'";
		String[][] links = DBUtil.querySql(link_sql);
		if(links!=null){
		   ywurl = request.getContextPath()+"/"+links[0][0]+"?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+spbh;
		}
		String fqr_sql = "select cjrid from ap_processinfo where PROCESSOID='"+spbh+"'";
		String[][] fqr_ = DBUtil.querySql(fqr_sql);

		if(fqr_!=null){
		   String fqr = fqr_[0][0];
		   if(userId.equals(fqr)){
			   isfqr = true;
		   }
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
	}
%>
var processtype = "<%=processType%>";
var p_ywid='<%=ywid%>';
function doSubmit()
{
	var strAction = "<%=request.getContextPath()%>/TaskAction.do?doApprove";
	try
	{
		document.frmPost.checkOver.disabled=true;
        var spUser = "";
        var stepsequence = "";
        var spr = document.all("spr");
		if(spr){
            for(var w=0;w<spr.length;w++){
               if(spr[w].selected) {
            	   var dbrArray = spr[w].value.split("_");
                   spUser = dbrArray[0];
                   stepsequence = dbrArray[1];
                   break;
               }
            }
        }
		if(spUser==""){
		  if($("#ACTOR")[0].tagName=="INPUT"){
			  spUser = $("#ACTOR").attr("code");
		  }
		  if($("#ACTOR")[0].tagName=="SELECT"){
			  spUser = $("#ACTOR").val();
		  }					
		  if(!spUser)
		  {
			requireFormMsg("请选择办理人!");
			return;
		  }
		}
		
		var data1 = "id=<%=taskid%>&seq=<%=taskseq%>&sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&spbh=<%=spbh%>&spjg="+resultValue+"&sfth=0&spUser="+spUser+"&stepsequence="+stepsequence;
			//data1+= "&tsdept="+document.all('USERTODEPT').value+"&tsperson="+document.all('USERTOPERSON').value+"&tsstep="+document.all("USERSTEP").value;
			data1+= "&result="+resultValue+"&resultDscr="+encodeURIComponent(document.getElementById('mind').value)+"&fjbh=<%=sp_fjbh%>&ccFlag=<%if(ccFlag) out.print("1");%>";
		$.ajax({
			type : 'post',
			url : strAction,
			data : data1,
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
			    success = false;
			}
		});
		document.frmPost.txtSubmitSQL.value = "";
		document.frmPost.txtFilter.value = "";
		document.frmPost.result.value = "";

	}
	catch(e)
	{
		document.frmPost.checkOver.disabled=false;
	}
	spflag=true;
}
function openAttachment(wsid,ywlx,sjbh,spzt){

	var wsActionURL = "<%=request.getContextPath()%>/WsPrint/PubWS.do?method=getPrintAction|templateid="+wsid+"|ywlx="+ywlx+"|sjbh="+sjbh+"|spzt="+spzt+"|rowid="+Math.random()+".mht";
	    wsActionURL = "<%=request.getContextPath()%>/jsp/framework/print/pubPrint.jsp?param="+wsActionURL+"&IsQm=0&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid="+wsid+"&isEdit=0"+"&spzt="+spzt;
	 window.open(wsActionURL,"","width=800px,height=600px,toolar=0,menubar=0,scrollbars=1,status=0,resizable=1,screenX=0,screenY=0");
}

function doSave()
{
	var n=resultValue; // 1：同意   2：不同意  3：退回
	try
	{
		var cList = document.getElementsByName("selectCheckInfo");
		var strMind;
		var strText;
        var hascheck = false;
		for(var i = 0;i<document.getElementsByName("radCheckState").length;i++)
		{
			if (document.getElementsByName("radCheckState").item(i).checked)
			{
				strMind = document.getElementsByName("radCheckState").item(i).value;
                hascheck=true;
				break;
			}
		}
        	if(!n)
        	{
         		if(hascheck == false)
         		{
         			xAlert("提示信息","未给出审批结果，请选择'同意'或'退回'");
					return;
         		}
        	}
        	else
        	{
	    		if (document.getElementById("mind").value.length==0)
	    		{
	    			if(n==3){
	    				xAlert("提示信息","请给出意见!!!!!!",'3');
						//if(parseInt(n) == 3){
							document.getElementById("mind").focus();
						//}
						return;
	    			}else{
	    				document.getElementById("mind").value="同意";
	    			}
	    		}
        	}
	    	if (document.getElementById("mind").value.length>1000)
	    	{
	    		xAlert("提示信息",'审批意见不能超过1000个汉字，请核对!','3');
		   		document.getElementById("mind").focus();
		   		return false;
	    	}
	    	
	    	var spUser = "";
	        var stepsequence = "";
	        var spr = document.all("spr");
	    	var spUserName;
	 		if(spr){
	             for(var w=0;w<spr.length;w++){
	                if(spr[w].selected) {
	             	   var dbrArray = spr[w].value.split("_");
	                    spUser = dbrArray[0];
	                    stepsequence = dbrArray[1];
	                    break;
	                }
	             }
	        }
	       if(spUser==""){
	    	if($("#ACTOR")[0].tagName=="INPUT"){
				  spUser = $("#ACTOR").attr("code");
				  spUserName = $("#ACTOR").val();
			  }
			  if($("#ACTOR")[0].tagName=="SELECT"){
				  spUser = $("#ACTOR").val();
				  spUserName = $("#ACTOR").find("option:selected").text(); 
			  }		
			  if(!spUser)
			  {
				  xAlert("提示信息","请选择办理人!",'3');
				return;
			  }

	         }
			strText = document.getElementById("mind").value;
		
		if (strText.length == 0)
		{
			strText = '';
        }
          if(n == 3){ // 回退操作
        	  
        	  	if($("#spr").val()=="")
        	  	{
        	  		xAlert("提示信息",'请指定审批退回人！','3');
        	  		return false;
        	  	}

        		xConfirm("提示信息","是否退回审批信息?");

        		$('#ConfirmYesButton').unbind();
        		$('#ConfirmYesButton').attr('click','');//.click( eval(function(){test(dd);}));
        		$('#ConfirmYesButton').one("click",function(){
        			
        			doSubmit();
        			
        		});  
        		
          }else{
                xConfirm("提示信息","是否提交办理信息?办理人："+spUserName);
            	$('#ConfirmYesButton').unbind();
           		$('#ConfirmYesButton').attr('click','');//.click( eval(function(){test(dd);}));
           		$('#ConfirmYesButton').one("click",function(){
           			doSubmit();
           		});
          }
	}
	catch(e)
	{
		alert(e.description);
		document.frmPost.checkOver.disabled=true;
	}
	spflag=true;
};


function QuerySplc(sjbh,spbh){

  var strUrl = '${pageContext.request.contextPath}/jsp/framework/common/aplink/spFlowView_gd.jsp?sjbh='+sjbh+'&spbh='+spbh;
  
  $(window).manhuaDialog({"title":"查看审批流程","type":"text","content":strUrl,"modal":"1"});
 };

function QuerySpyj(sjbh,spbh){

  var strUrl = '${pageContext.request.contextPath}/jsp/framework/common/aplink/spYjView.jsp?sjbh='+sjbh+'&spbh='+spbh;
  
  $(window).manhuaDialog({"title":"查看审批意见","type":"text","content":strUrl,"modal":"1"});
};

function print(fjbh,ws_template_id,sjbh,ywlx){
   
    var wsActionURL = '/xmgl/PubWS.do?getXMLPrintAction|templateid='+ws_template_id+'|isEdit=0|ywlx='+ywlx+'|sjbh='+sjbh+'|rowid='+Math.random()+'.mht';

    var rswsActionURL = '/xmgl/jsp/framework/print/pubPrint.jsp?param='+wsActionURL+'&isview=1&sjbh='+sjbh+'&ywlx='+ywlx+'&temlateid='+ws_template_id+'&isEdit=0&ywid='+p_ywid;
    $(window).manhuaDialog({"title":"查看文书及附件信息","type":"text","content":rswsActionURL,"modal":"1"});  

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


var clickNum = 0 ;
function clickSpjg(obj)
{
	//alert(obj.value=="3");
	var s = $("#mind").val();	
	if(obj.value=="3")
	{
		$("#spr").show();
		$("#select_person").hide();
		if(s!=""&&s=="同意")
		$("#mind").val("不同意");
	
	}else if(obj.value=="1")
	{
		if(s!=""&&s=="不同意")
		$("#mind").val("同意");
		$("#select_person").show();
		$("#spr").hide();
		$("#spr").val("");
	}
	 if(obj){
		 if(clickNum == 0)
		 {
			clickNum = 1 ;
		
		 }else{
			if(document.getElementById("mind").value.length == 0)
			{

			}
			else		
			{
		     //  	xConfirm("提示信息","是否确定修改审批意见？");
			//  $('#ConfirmYesButton').unbind();
		   	//	$('#ConfirmYesButton').attr('click','');//.click( eval(function(){test(dd);}));
		   	//	$('#ConfirmYesButton').one("click",function(){
		   	//		spyjUpdate();
			//	});
			}
		}
	}
  resultValue=obj.value;
}

function doUnLoad()
{

	if(!spflag)
	{
		setTaskInit("<%=taskid%>","<%=taskseq%>");
	}
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
$(function() {
	/* doSearch (1); */
	
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
	$("#task_dbrid").attr("colspan","2");
	
	if("<%=isth %>" == "0") {
		$("#spyjTextArea").css("display","none");
	}
	var spws=$("#spws");
	spws.click(function() {
		
		var ywid = '<%=ywid%>';
		var fjbh = "<%=fjbh%>";
		
		var ws_template_id = '<%=ws_template_id%>';
		var ws_template_name = '<%=ws_template_name%>';
		var fjbhs = fjbh.split(",");
		 var ws_template_ids = ws_template_id.split(",");
         var ws_template_names = ws_template_name.split(",");
         var sjbh = '<%=sjbh%>';
         var ywlx = '<%=ywlx%>';
        
         var wsText="";

		 if(fjbh.length>0&&fjbhs.length>0){
             for(var i =0;i<fjbhs.length;i++){
                if(i==0){
					var radCheckState = document.getElementsByName("radCheckState");
                   radCheckState[0].fjbh=fjbhs[i];
                   radCheckState[0].wstemplate=ws_template_ids[i];
                   radCheckState[1].fjbh=fjbhs[i];
                   radCheckState[1].wstemplate=ws_template_ids[i];
                }
				
                print(fjbhs[i],ws_template_ids[i],sjbh,ywlx);		
             }
          }else{
       	   
       	   print("",ws_template_id,sjbh,ywlx);
			
          };
		
	});
	$("#blrBtn").click(function(){
		var actorCode = $("#ACTOR").attr("code");
		var dept = $("#ACTOR").attr("dept");
		selectUserTree('multi',actorCode,'setBlr',dept) ;
	});

	$("#blbmBtn").click(function(){
		var deptidCode = $("#DEPTID").attr("code");
		var selectType = $(this).attr("selectType");
		var type = "";
		if(selectType&&selectType=="multi"){
			type = "multi";
		}else{
			type  = "single";
		}
		
		
		openDeptTree(type,deptidCode,'setBlbm') ;
	});
	var splc=$("#splc");
	splc.click(function() {
		
		QuerySplc('<%=sjbh%>','<%=spbh%>');
	});

	var ywxx=$("#ywxx");
	ywxx.click(function() {
		
		 $(window).manhuaDialog({"title":"查看业务详细信息","type":"text","content":"<%=ywurl%>","modal":"1"});  
	});
});
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
 		deptFzrName +=tempObj.FZRNAME+",";
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

function viewSpyj(){

	$(window).manhuaDialog({"title":"审批意见","type":"text","content":"<%=request.getContextPath()%>/jsp/framework/common/aplink/spYjView.jsp?sjbh=<%=sjbh%>&spbh=<%=spbh%>","modal":"2"});	
}
resultValue=1;


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
function printDiv()
{
	window.frames["iFrame"].printIframe();
	
}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
    	<%if("14".equals(ws_template_id)){ %>
				<div  id="iframeright" style="width=100%;height:60%;overflow:hidden;" >

		    		<h4 class="title">审批办理<font style="font-size:14px;text-vertical:middle;font-weight:normal">——最后办理期限为：</font><span style="font-size:14px;text-vertical:middle;" id="gdspsj"></span>
			      	<span class="pull-right">
			      						
			      		<button id="btnSave" class="btn" onClick="doSave()" type="button">审批完成</button>
			      		<% if(isfqr==true){ %>
			            <button id="cmdZdlc" class="btn" style="display:none" onclick="breakSP()" type="button">中断流程</button>
			            <%} %>
		              	<button id="example_query" class="btn btn-link" type="button" onclick="printDiv()"><i class="icon-print"></i>打印</button>
			      	</span>
			      	</h4>
           	       
                       <form method="post" action="" id="insertForm">
                       <div class="windowOpenAuto" style="height:90%;overflow:auto;">
                       <table class="B-table" style="margin-bottom:2px;" width="95%">
                            <tr id="spyjTextArea">
							<th width="20%" align="center" valign="middle" style="font-size:14px;vertical-align:middle;">审批结果</th>
							<td width="80%" style="font-size:14px;vertical-align:middle;">
							<input name="radCheckState" type="radio" style="margin:0px;" value="1" fjbh="" checked wstemplate="" onclick="clickSpjg(this)">同意	</label> &nbsp;&nbsp;
							<input type="radio" name="radCheckState" style="margin:0px;" value="3" fjbh="" wstemplate="" 
							<% if(("1".equals(nowStep.getIsMS()))&&!("3".equals(spjg))||ccFlag) out.println("disabled");%> onclick="clickSpjg(this)">退回</label>
								<%	if(spBackRY!=null)
									{
								%>
									<select id="spr" style="display:none">
				                		<option value="">请选择退回人员</option>
				                		<option value="<%=spBackRY[0][0]+"temp"%>">发起人：<%=spBackRY[0][1]%></option>
			         			<%		
			         			    ArrayList al = new ArrayList();
									for(int i=0;i<spBackRY.length;i++)
									{
										if(spBackRY[i][3]!=null&&spBackRY[i][3].length()>0)
										{
											if(spBackRY[i][5]!=null && spBackRY[i][5].length()>0 && nowStep.getStepSequence()<=Integer.parseInt(spBackRY[i][5]))
												break;
											if( user.getAccount().equals(spBackRY[i][2]))
												continue;
											if(Pub.empty(spBackRY[i][5]))
												continue;
											boolean isredo = false;
											if(al.size()>0)
											for(int k =0;k<al.size();k++)
											{
												int ss = (Integer)al.get(k);
												if(!Pub.empty(spBackRY[i][5]))
												if(ss==Integer.parseInt(spBackRY[i][5])){
													isredo = true;
													break;
												}
											}
											if(isredo==true){
												continue;
											}else{
												if(!Pub.empty(spBackRY[i][5]))
												al.add(Integer.parseInt(spBackRY[i][5]));
											}
											
								%>
										<option value="<%=spBackRY[i][2]+"_"+spBackRY[i][5]%>">第<%=spBackRY[i][5]%>节点：<%=spBackRY[i][3]%></option>	
								<% 		
										}
									}
								%>	
									</select>
								<%		
									}
															
								%>
							</td>
						</tr>
						<tr id="select_person">
							<%=html%>
						</tr>
					   <tr>
							<th align="center" valign="middle" style="text-align:center;font-size:14px;%">相关信息</th>
							<td style="font-size:14px;">
			           			<%
							       if(!Pub.empty(ywurl)) {
							    %>
							    <button id="ywxx" class="btn btn-link"  type="button"><i class=" icon-comment"></i>业务信息</button>
							    <%
							       }
							    %>
			           			<button id="splc" class="btn btn-link" type="button"><i class="icon-book"></i>审批流程</button>
			                   	<button id="cmdSpyj" class="btn btn-link" onclick="viewSpyj()" type="button"><i class="icon-th-list"></i>办理意见</button>
							</td>
						</tr>
						<tr>
					   <th align="center" valign="middle" style="text-align:center;font-size:14px;">办理意见</th>
				       <td colspan="7" class="bottom-border right-border">
					      <div><textarea class="span12" rows="3" name="txtCheckMind" id="mind" maxLength="2000">同意</textarea></div>	
				       </td>
					   </tr>		    
                       <tr>
						<th style="text-align:center;font-size:14px;">附件信息</th>
						<td colspan="7" class="bottom-border right-border">
							<div>
								<input type="hidden" name=ywid id="ywid" value="111">  
								<table role="presentation" class="table table-striped">
									<tbody  class="files showFileTab"
										data-toggle="modal-gallery" data-target="#modal-gallery" showLrr="true" deleteUser="<%=user.getAccount()%>" showSize="false" onlyView="true">
									</tbody>
								</table>
									<span  class="btn btn-fileUpload" onclick="doSelectFile(this);"	fjlb="3999"> <i class="icon-plus"></i> <span>添加文件...</span>
									</span>
							</div>
						</td>
					   </tr>
                       </table>
                        </div>
                       </form>
                  
                   
				   <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
              </div> 
              			
		
            <div id="iframediv" style="width: 100%;height: 70%;overflow:hidden;">
             	<iframe id="iframe" style="width: 100%;height: 90%;" name="iFrame" src="${pageContext.request.contextPath}/jsp/business/zjgl/ykWs.jsp?sjbh=<%=sjbh %>&ywlx=<%=ywlx %>&isEdit=1" FRAMEBORDER=0 SCROLLING=></iframe>
			</div>
			<% } else if("".equals(ws_template_id)) { %>
			
			<h4 class="title">审批办理<font style="font-size:14px;text-vertical:middle;font-weight:normal">——最后办理期限为1：</font><span style="font-size:14px;text-vertical:middle;" id="gdspsj"></span>
		      	<span class="pull-right">
		      						
		      		<button id="btnSave" class="btn" onClick="doSave()" type="button">办理完成</button>
		
		      	</span>
		      </h4>

  			<div class="windowOpenAuto">
			<table width="100%" class="table" style="margin-bottom:2px;" id="DT1">
				<tr id="spyjTextArea">			
				<th width="20%" id = "spbljg"  align="center" valign="middle" style="font-size:14px;vertical-align:middle" >办理结果</th>
				<td width="80%" style="font-size:14px;vertical-align:middle;">
				<form class="form-inline">
				<label><input name="radCheckState" type="radio" style="margin:0px;" value="1" fjbh="" checked wstemplate="" onclick="clickSpjg(this)">同意	</label> &nbsp;&nbsp;
				<label><input type="radio" name="radCheckState" style="margin:0px;" value="3" fjbh=""	wstemplate="" 
				<% if(("1".equals(nowStep.getIsMS()))&&!("3".equals(spjg))||ccFlag) out.println("disabled");%> onclick="clickSpjg(this)">退回</label>
					<%	if(spBackRY!=null)
						{
					%>
						<select id="spr" style="display:none">
	                		<option value="">请选择退回人员</option>
	                		<option value="<%=spBackRY[0][0]+"_temp"%>">发起人：<%=spBackRY[0][1]%></option>
         			<%		
 			             ArrayList al = new ArrayList();
						for(int i=0;i<spBackRY.length;i++)
						{
							if(spBackRY[i][3]!=null&&spBackRY[i][3].length()>0)
							{
								if(spBackRY[i][5]!=null && spBackRY[i][5].length()>0 && nowStep.getStepSequence()<=Integer.parseInt(spBackRY[i][5]))
									break;
								if( user.getAccount().equals(spBackRY[i][2]))
									continue;
								if(Pub.empty(spBackRY[i][5]))
									continue;
								boolean isredo = false;
								if(al.size()>0)
								for(int k =0;k<al.size();k++)
								{
									int ss = (Integer)al.get(k);
									if(!Pub.empty(spBackRY[i][5]))
									if(ss==Integer.parseInt(spBackRY[i][5])){
										isredo = true;
										break;
									}
								}
								if(isredo==true){
									continue;
								}else{
									if(!Pub.empty(spBackRY[i][5]))
									al.add(Integer.parseInt(spBackRY[i][5]));
								}
								
					%>
							<option value="<%=spBackRY[i][2]+"_"+spBackRY[i][5]%>">第<%=spBackRY[i][5]%>节点：<%=spBackRY[i][3]%></option>	
					<% 		
							}
						}
					%>	
						</select>
					<%		
						}
												
					%>
				</form>
				</td>
			</tr>
			<tr id="select_person">
				<%=html%>
			</tr>
			<tr>
				<th align="center" valign="middle" style="text-align:center;font-size:14px;">相关信息</th>
				<td style="font-size:14px;">
           			<%
				       if(!Pub.empty(ywurl)) {
				    %>
				    <button id="ywxx" class="btn btn-link"  type="button"><i class=" icon-comment"></i>业务信息</button>
				    <%
				       }
				    %>
           			<button id="splc" class="btn btn-link" type="button"><i class="icon-book"></i>审批流程</button>
                   	<button id="cmdSpyj" class="btn btn-link" onclick="viewSpyj()" type="button"><i class="icon-th-list"></i>办理意见</button>
				</td>
			</tr>
			<tr id="spyj">
				<th id ="spblyj" align="center" valign="middle" style="text-align:center;font-size:14px;">办理意见</th>
				<td colspan = "2">
					<div><textarea class="span12" rows="3" name="txtCheckMind" id="mind" maxlength="1000">同意</textarea></div>
				</td>
				</tr>
				<tr >
				<th align="center" valign="middle" style="text-align:center;font-size:14px;">附件信息</th>
				<td colspan = "2">

						<span  class="btn btn-fileUpload" style="font-size:14px;"  onclick="doSelectFile(this);"	fjlb="3999"> <i class="icon-plus"></i> <span>添加文件...</span>
						</span>	
						<table role="presentation" class="table table-striped">
							<tbody  class="files showFileTab"
								data-toggle="modal-gallery" data-target="#modal-gallery" showLrr="true" deleteUser="<%=user.getAccount()%>" showSize="false" onlyView="true">
							</tbody>
						</table>
						
					
				</td>
				</tr>
			</table>
			
			<% } else { %>
      
			<div class="span7" style="height:95%;overflow:hidden;">
				<iframe name="iFrame" width="100%" height="90%" src="${pageContext.request.contextPath }/PubWS.do?getXMLPrintAction&templateid=<%=ws_template_id %>&isview=1&ywlx=<%=ywlx %>&sjbh=<%=sjbh %>&rowid=mht" FRAMEBORDER=0 SCROLLING= ></iframe>
			</div>
		
    		<div class="span5" >
    			<h4 class="title">审批办理<font style="font-size:14px;text-vertical:middle;font-weight:normal">——最后办理期限为：</font><span style="font-size:14px;text-vertical:middle;" id="gdspsj"></span>
		      	<span class="pull-right">
		      						
		      		<button id="btnSave" class="btn" onClick="doSave()" type="button">审批完成</button>
		            <% if(isfqr==true){ %>
			        <button id="cmdZdlc" class="btn" style="display:none" onclick="breakSP()" type="button">中断流程</button>
			        <%} %>
		            <button id="example_query" class="btn btn-link" type="button" onclick="printDiv()"><i class="icon-print"></i>打印</button>
		      	</span>
		      	</h4>
		      	<div class="windowOpenAuto">
					<table width="100%" class="table" style="margin-bottom:2px;" id="DT1">
						<tr id="spyjTextArea">
							<th width="20%" align="center" valign="middle" style="font-size:14px;vertical-align:middle;">审批结果</th>
							<td width="80%" style="font-size:14px;vertical-align:middle;">
							<p>
							<form class="form-inline">
							<label class="inline">
							<input name="radCheckState" type="radio" style="margin:0px;" value="1" fjbh="" checked wstemplate="" onclick="clickSpjg(this)">同意	</label> &nbsp;&nbsp;
							<label class="inline">
							<input type="radio" name="radCheckState" style="margin:0px;" value="3" fjbh=""	wstemplate="" 
							<% if(("1".equals(nowStep.getIsMS()))&&!("3".equals(spjg))||ccFlag) out.println("disabled");%> onclick="clickSpjg(this)">退回</label>
								<%	if(spBackRY!=null)
									{
								%>
									<select id="spr" style="display:none">
				                		<option value="">请选择退回人员</option>
				                		<option value="<%=spBackRY[0][0]+"_temp"%>">发起人：<%=spBackRY[0][1]%></option>
			         			<%		
			         			    ArrayList al = new ArrayList();
									for(int i=0;i<spBackRY.length;i++)
									{
										if(spBackRY[i][3]!=null&&spBackRY[i][3].length()>0)
										{
										 	if(spBackRY[i][5]!=null && spBackRY[i][5].length()>0 && nowStep.getStepSequence()<=Integer.parseInt(spBackRY[i][5]))
												break;
											if( user.getAccount().equals(spBackRY[i][2]))
												continue;
											if(Pub.empty(spBackRY[i][5]))
												continue;
											boolean isredo = false;
											if(al.size()>0)
											for(int k =0;k<al.size();k++)
											{
												int ss = (Integer)al.get(k);
												if(!Pub.empty(spBackRY[i][5]))
												if(ss==Integer.parseInt(spBackRY[i][5])){
													isredo = true;
													break;
												}
											}
											if(isredo==true){
												continue;
											}else{
												if(!Pub.empty(spBackRY[i][5]))
												al.add(Integer.parseInt(spBackRY[i][5]));
											}
											 
								%>
										<option value="<%=spBackRY[i][2]+"_"+spBackRY[i][5] %>">第<%=spBackRY[i][5]%>节点：<%=spBackRY[i][3]%></option>	
								<% 		
										}
									}
								%>	
									</select>
								<%		
									}
															
								%>
							</form>
							</p>
							</td>
						</tr>
						<tr>
							<th width="20%" align="center" valign="middle" style="text-align:center;font-size:14px;">相关信息</th>
							<td width="80%" style="font-size:14px;">
			           			<%
							       if(!Pub.empty(ywurl)) {
							    %>
							    <button id="ywxx" class="btn btn-link"  type="button"><i class=" icon-comment"></i>业务信息</button>
							    <%
							       }
							    %>
			           			<button id="splc" class="btn btn-link" type="button"><i class="icon-book"></i>审批流程</button>
		                    	<button id="cmdSpyj" class="btn btn-link" onclick="viewSpyj()" type="button"><i class="icon-th-list"></i>办理意见</button>
							</td>
						</tr>
						<tr id="select_person">
							<%=html%>
						</tr>
						<tr>
							<th align="center" valign="middle" style="text-align:center;font-size:14px;">审批意见</th>
							<td colspan="4">
								<div><textarea class="span12" rows="3" name="txtCheckMind" id="mind" maxlength="1000">同意</textarea></div>
								
							</td>
						</tr>
						<tr>
							<th align="center" valign="middle" style="text-align:center;font-size:14px;">附件信息</th>
							<td colspan="4">
								
								<span class="btn btn-fileUpload" style="font-size:14px;" onclick="doSelectFile(this);"	fjlb="3999"> <i class="icon-plus"></i> <span>添加文件...</span></span>
							    <table role="presentation" class="table table-striped">
									<tbody  class="files showFileTab" notClear="true" id="fileTbody"
										data-toggle="modal-gallery" data-target="#modal-gallery" showLrr="true" deleteUser="<%=user.getAccount()%>" showSize="false" onlyView="true">
									</tbody>
								</table>
							</td>
						</tr>
					   
					   <!-- 选择排迁子项目时，把该子项目的委托函、联络单、审定表附件带到合同中。 -->
					   <% if(!"".equals(pxZxm)) { %>
						<tr>
							<th align="center" valign="middle" style="text-align:center;font-size:14px;width:8%">
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
							<th align="center" valign="middle" style="text-align:center;font-size:14px;width:8%">
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
							<th align="center" valign="middle" style="text-align:center;font-size:14px;width:8%">
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
				
      		<% } %>
				<div class="text-center">
					<form name="frmPost" method="post" action="<%=request.getContextPath()%>/TaskAction.do?method=doApprove">
						<input type="button" value="保存意见" style="display:none" class="btn" disabled name="checkOver" onClick="doSubmit();"> 
						
						<input type="hidden" name="txtXML"> 
						<input type="hidden" name="resultXML">
						<input type="hidden" name="queryXML">
						<INPUT type="hidden" name="txtQueryMode" value="0">
						<input type="hidden" name="txtParentFilter">
						<input type="hidden" name="txtFilter">
						<INPUT type="hidden" name="templateid" id="templateid">
						<INPUT type="hidden" name="txtSubmitSQL">
						<INPUT type="hidden" name="result">
						<input type="hidden" name="hh" value="<%=strHh%>" />
						<input type="hidden" name="rowData">
					</form>
				</div>
    		</div>
			<%
				String sqlquery = "select t.cjrxm,to_char(t.cjsj, 'YYYY-MM-DD HH24:MI'),t.dbryid,to_char(t.wcsj, 'YYYY-MM-DD HH24:MI'),"+
					       		"t.result,t.resultdscr,to_char(t.shedule_time, 'YYYY-MM-DD'),t.yxbs from ap_task_schedule t, AP_PROCESSDETAIL d "+
								" where t.sjbh = '"+sjbh+"' and t.spbh = d.processoid(+) and t.stepsequence = d.stepsequence(+) order by cjsj,wcsj";
				String[][] taskListq = DBUtil.query(sqlquery);
				for(int k=0;k<taskListq.length;k++)
				{
					if(user.getAccount().equals(taskListq[k][2]))
					{
						gdspsx = taskListq[k][6];
					}
				}
			%>
  		
	
</div>
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
</div> 
</body>
</html>
<script language="javascript">
var aaa = "<%=gdspsx%>";
document.getElementById("gdspsj").innerText=aaa;
if('<%=timeStr%>'>aaa)
{
	document.getElementById("gdspsj").style.color='red';
}
</script>