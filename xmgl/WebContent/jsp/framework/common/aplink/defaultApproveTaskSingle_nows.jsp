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
	User user = (User) session.getAttribute(Globals.USER_KEY);
	String userName = user.getName();
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
</style>

<script language="javascript">


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
	String ywurl = "";
	String isMs = "";
    String[][] processinfo = null;
	
	try {
		process = TaskBO.getProcess(conn, spbh);
		processinfo = DBUtil.query("select PROCESSOID,to_char(createtime,'yyyy-mm-dd hh24:mi') as createtime,cjrid,value4 from ap_processinfo where  PROCESSOID='"+spbh+"'");
		nowStep = process.open();
		isMs = nowStep.getIsMS();
		if(isMs==null) isMs="";
		nextStep = process.getNextStep();
		lastStep = process.getLastStep();
		ProcessDetail p = (ProcessDetail)nextStep;
		TaskMgrBean tss = new TaskMgrBean();
		String operation =    process.getOperationOID();
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
var islastStep = '<%=islastStep%>';
function doSubmit()
{
	var strAction = "<%=request.getContextPath()%>/TaskAction.do?doApprove";
	try
	{
		document.frmPost.checkOver.disabled=true;
        var spUser = "";

        var spr = document.all("spr");
		if(spr){
            for(var w=0;w<spr.length;w++){
               if(spr[w].selected){
                   spUser=spr[w].value;
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
		  if(!spUser&&spUser=="")
		  {
			requireFormMsg("请选择办理人!");
			return;
		  }
		}
	//	alert(spUser)
		var data1 = "id=<%=taskid%>&seq=<%=taskseq%>&sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&spbh=<%=spbh%>&spjg="+resultValue+"&sfth="+(resultValue==3?1:0)+"&spUser="+spUser;
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
         			xAlert("提示信息","未给出结果，请选择'同意'或'退回'");
					return;
         		}
        	}
        	else
        	{
	    		if (document.getElementById("mind").value.length==0)
	    		{
	    			if(n==3){
	    				xAlert("提示信息","请给出意见!",'3');
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
	    		xAlert("提示信息",'意见不能超过1000个汉字，请核对!','3');
		   		document.getElementById("mind").focus();
		   		return false;
	    	}
	    	var spr = document.all("spr");
	    	var spUser ="";
			if(spr){
	            for(var w=0;w<spr.length;w++){
	               if(spr[w].selected){
	                   spUser=spr[w].value;
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
			  if(!spUser&&spUser=="")
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
        	  		xAlert("提示信息",'请指定退回人！','3');
        	  		return false;
        	  	}

        		xConfirm("提示信息","是否退回信息?");
        		
        		$('#ConfirmYesButton').attr('click','');//.click( eval(function(){test(dd);}));
        		$('#ConfirmYesButton').one("click",function(){
        			
        			doSubmit();
        			
        		});  
        		
          }else{
        	   var spUser = "";
      		   if(spUser==""){
      		    if($("#ACTOR")[0].tagName=="INPUT"){
      			  spUser = $("#ACTOR").val();
      		    }
      		    if($("#ACTOR")[0].tagName=="SELECT"){
      			  spUser = $("#ACTOR").find("option:selected").text(); 
      		    }					
      		    if(!spUser&&spUser=="")
      		    {
      			 requireFormMsg("请选择办理人!");
      			 return;
      		    }
      		   }    
      		   <%if (islastStep==true){%>
      		    var ms = "是否返回发起人?";
      		    if(spUser!=""){
     			   ms +=" 发起人："+spUser;
     		    }
      		   <%}else{%>
      		    var ms = "是否提交办理信息?";
      		    if(spUser!=""){
    			   ms +=" 办理人："+spUser;
    		    }
      		   <%}%>
      		   
      		   
                xConfirm("提示信息",ms);
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
  
  $(window).manhuaDialog({"title":"查看办理流程","type":"text","content":strUrl,"modal":"1"});
 };

function QuerySpyj(sjbh,spbh){

  var strUrl = '${pageContext.request.contextPath}/jsp/framework/common/aplink/spYjView.jsp?sjbh='+sjbh+'&spbh='+spbh;
  
  $(window).manhuaDialog({"title":"查看办理意见","type":"text","content":strUrl,"modal":"1"});
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
		if(s!=""&&s=="同意")
		$("#mind").val("不同意");
		var trObj = $("#DT1").find("tr").eq(1);
		var thObj = trObj.find("th");
		trObj.hide();
	}else if(obj.value=="1")
	{
		if(s!=""&&s=="不同意")
		$("#mind").val("同意");
		$("#spr").hide();
		$("#spr").val("");
		var trObj = $("#DT1").find("tr").eq(1);
		var thObj = trObj.find("th");
		var ss = thObj[0].style.display
		if(ss=="none"){
			
		}else{
			trObj.show();
		}
		//alert(trObj[0].outerHTML);
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
		      // 	xConfirm("提示信息","是否确定修改审批意见？");
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
function isFqrModify(){
	var ty_th = $("#spyjTextArea").find("th")[0];
	ty_th.style.display="none";
	var ty_td = $("#spyjTextArea").find("td")[0];
	ty_td.style.display="none";
	$("#btnSave")[0].innerText = "提交";
	var h4 = $("h4");
	//h4.hide();
	var spyj_th = $("#spyj").find("th");
	spyj_th[0].innerText ="办理意见";
}

function lastStepModify(){
	//修改办理结果办理意见为审批结果审批意见
	$("#spbljg")[0].innerText = "审批结果";
	$("#spblyj")[0].innerText = "审批意见";
	//办理时限改为审批实现
	$("#spblsx")[0].innerText = "审批时限";
	$("#btnSave")[0].innerText = "审批完成";
	/* $("#cyblyj")[0].innerText = "常用审批意见"; */
}

$(function() {
	/* doSearch (1); */
	<%if("4".equals(isMs)){%>
		isFqrModify();
    <%}%>
    <%if(islastStep){%>
    	lastStepModify();
	<%}%>
	setFileData("","","<%=sjbh%>","<%=ywlx%>");
	queryFileData("","","<%=sjbh%>","<%=ywlx%>");
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
		if(dept&&dept!=""){
			selectUserTree('single',actorCode,'setBlr',dept) ;
		}else{
		    selectUserTree('single',actorCode,'setBlr') ;
		}
	});
	$("#blbmBtn").click(function(){
		var deptidCode = $("#DEPTID").attr("code");
		openDeptTree('single',deptidCode,'setBlbm') ;
	});
	var splc=$("#splc");
	splc.click(function() {
		
		QuerySplc('<%=sjbh%>','<%=spbh%>');
	});
	var xxxx=$("#xxxx");
	xxxx.click(function() {
		
		 $(window).manhuaDialog({"title":"查看业务详细信息","type":"text","content":"<%=ywurl%>","modal":"1"});  
	});
	 $("#cyblyjxz").on("change", function(){

		   generateYj();
	   }); 

});

function generateYj(){
 	var blyj = '';
      var cyblyj = document.getElementById("cyblyjxz");
          for(var w=0;w<cyblyj.length;w++){
             if(cyblyj[w].selected){
            	 blyj=cyblyj[w].value;
                 break;
             }
          }
              document.getElementById("mind").value = blyj;
          
}
resultValue=1;
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">审批办理<font style="font-size:14px;text-vertical:middle;font-weight:normal">——要求办理时间至：</font><span style="font-size:14px;text-vertical:middle;" id="gdspsj"></span>
      	<span class="pull-right">
      						
      		<button id="btnSave" class="btn" onClick="doSave()" type="button">办理完成</button>

      	</span>
      </h4>

  		<div class="windowOpenAuto">
			<table width="100%" class="table" style="margin-bottom:2px;" id="DT1">
				<!-- 样式控制，如果为发起人则样式单独显示 -->
				<%if("4".equals(isMs)){%>
				<tr id="spyjTextArea">			
				<th  id = "spbljg"  align="center" valign="middle" style="font-size:14px;vertical-align:middle" >办理结果</th>
				<td  style="font-size:14px;vertical-align:middle;">
				<p>
				<form class="form-inline">
				<label class="inline">
				<input name="radCheckState" type="radio" style="margin:0px;" value="1" fjbh="" checked wstemplate="" onclick="clickSpjg(this)">同意	</label> &nbsp;&nbsp;
				<label class="inline">
				<input type="radio" name="radCheckState" style="margin:0px;" value="3" fjbh=""	wstemplate="" 
				<% if(("1".equals(nowStep.getIsMS()))&&!("3".equals(spjg))||ccFlag) out.println("disabled");%> 
				onclick="clickSpjg(this)">退回</label>
					<%	if(spBackRY!=null)
						{
					%>
						<select id="spr" style="display:none">
	                		<option value="">请选择退回人员</option>
	                		<option value="<%=spBackRY[0][0]%>">发起人：<%=spBackRY[0][1]%></option>
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
							<option value="<%=spBackRY[i][2]%>">第<%=spBackRY[i][5]%>节点：<%=spBackRY[i][3]%></option>	
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
					<%=html%> 
					<td style="text-align:right;font-size:14px;width:50%">
				    <%
				       if(!Pub.empty(ywurl)){
				    %>
				    <button id="xxxx" class="btn btn-link"  type="button"><i class=" icon-comment"></i>查看业务信息</button>
				    <%
				       }
				    %>
				    <% if (ws_template_id.length() > 0) { %>
				    <button id="spws" class="btn btn-link"  type="button"><i class="icon-book"></i>查看文书及附件</button>
				    <%} %>
           			<button id="splc" class="btn btn-link" type="button"><i class="icon-th-list"></i>查看办理流程</button>
					</td>
				</tr>
				<% }else{%>
						<tr id="spyjTextArea">			
				<th  id = "spbljg"  align="center" valign="middle" style="font-size:14px;vertical-align:middle" >办理结果</th>
				<td  style="font-size:14px;vertical-align:middle;">
				<p>
				<form class="form-inline">
				<label class="inline">
				<input name="radCheckState" type="radio" style="margin:0px;" value="1" fjbh="" checked wstemplate="" onclick="clickSpjg(this)">同意	</label> &nbsp;&nbsp;
				<label class="inline">
				<input type="radio" name="radCheckState" style="margin:0px;" value="3" fjbh=""	wstemplate="" 
				<% if(("1".equals(nowStep.getIsMS()))&&!("3".equals(spjg))||ccFlag) out.println("disabled");%> 
				onclick="clickSpjg(this)">退回</label>
					<%	if(spBackRY!=null)
						{
					%>
						<select id="spr" style="display:none">
	                		<option value="">请选择退回人员</option>
	                		<option value="<%=spBackRY[0][0]%>">发起人：<%=spBackRY[0][1]%></option>
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
							<option value="<%=spBackRY[i][2]%>">第<%=spBackRY[i][5]%>节点：<%=spBackRY[i][3]%></option>	
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
				
				<%-- <%=html%> --%>
				
				<td style="text-align:right;font-size:14px;width:50%">
				    <%
				       if(!Pub.empty(ywurl)){
				    %>
				    <button id="xxxx" class="btn btn-link"  type="button"><i class=" icon-comment"></i>查看业务信息</button>
				    <%
				       }
				    %>
				    <% if (ws_template_id.length() > 0) { %>
				    <button id="spws" class="btn btn-link"  type="button"><i class="icon-book"></i>查看文书及附件</button>
				    <%} %>
           			<button id="splc" class="btn btn-link" type="button"><i class="icon-th-list"></i>查看办理流程</button>
				</td>
			</tr>
			<tr>
				<%=html%> 
			</tr>
				<% }%>
				<tr id="spyj">
				<th id ="spblyj" align="center" valign="middle" style="text-align:center;font-size:14px;width:8%">办理意见</th>
				<td colspan = "2">
					<div><textarea class="span12" rows="3" name="txtCheckMind" id="mind" maxlength="1000">同意</textarea></div>
					
				    <!--table role="presentation" class="table table-striped">
									<tbody  class="files showFileTab"
										data-toggle="modal-gallery" data-target="#modal-gallery" showLrr="true" deleteUser="<%=user.getAccount()%>" showSize="false" onlyView="true">
									</tbody>
					</table-->
				</td>
				</tr>
				<tr >
				<th align="center" valign="middle" style="text-align:center;font-size:14px;width:8%">附件信息</th>
				<td colspan = "2">

						<span  class="btn btn-fileUpload" style="font-size:14px;"  onclick="doSelectFile(this);"	fjlb="3006"> <i class="icon-plus"></i> <span>添加文件...</span>
						</span>	
						<table role="presentation" class="table table-striped">
							<tbody  class="files showFileTab"
								data-toggle="modal-gallery" data-target="#modal-gallery" showLrr="true" deleteUser="<%=user.getAccount()%>" showSize="false" onlyView="true">
							</tbody>
						</table>
						
					
				</td>
				</tr>
		</table>
		
	</div>
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

			<%
				String sqlquery = "select t.cjrxm,to_char(t.cjsj, 'YYYY-MM-DD HH24:MI'),t.dbryid,to_char(t.wcsj, 'YYYY-MM-DD HH24:MI'),"+
					       		"t.result,t.resultdscr,to_char(t.shedule_time, 'YYYY-MM-DD'),t.yxbs from ap_task_schedule t, AP_PROCESSDETAIL d "+
								" where t.sjbh = '"+sjbh+"' and t.spbh = d.processoid(+) and t.stepsequence = d.stepsequence(+) order by cjsj,wcsj";
				String[][] taskListq = DBUtil.query(sqlquery);
			%>
				<h4 class="title">办理记录</h4>
			<div class="B-small-from-table-auto">
				
			
            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
                <thead>
                    <tr>	
                    		<th name="XH" id="_XH" style="font-size:14px;">&nbsp;#&nbsp;</th>

							<th id="spblsx" fieldname="CJSJ" style="font-size:14px;">&nbsp;办理时限&nbsp;</th>
							<th fieldname="WCRXM" style="font-size:14px;">&nbsp;办理人&nbsp;</th>
							<th fieldname="WCSJ" style="font-size:14px;">&nbsp;办理时间&nbsp;</th>
							<th fieldname="SPJG" style="font-size:14px;">&nbsp;办理结果&nbsp;</th>
							<th fieldname="SPYJ" style="font-size:14px;width:30%;">&nbsp;办理意见&nbsp;</th>
							<th fieldname="CJRXM" style="font-size:14px;">&nbsp;送办人&nbsp;</th>
							<th fieldname="CJSJ" style="font-size:14px;">&nbsp;送办时间&nbsp;</th>
					</tr>
					</thead>
					<tbody>
					   <tr>
							<th align="center" style="font-size:14px;">1</th>

							<td align="center" style="font-size:14px;"></td>
							<td align="center" style="font-size:14px;"><%=Pub.getUserNameByLoginId(processinfo[0][2])%></td>
							<td align="center" style="font-size:14px;"><%=processinfo[0][1]%></td>
							<td align="center" style="font-size:14px;">已发起</td>
							<td align="left" style="font-size:14px;width:30%;word-break:break-all;"><%=processinfo[0][3]%></td>
							<td align="center" style="font-size:14px;"></td>
							<td align="center" style="font-size:14px;"></td>
						</tr>
					
					
					
						<%
							for(int k=0;k<taskListq.length;k++)
							{
								if(user.getAccount().equals(taskListq[k][2]))
								{
									gdspsx = taskListq[k][6];
								}
						%>
						<tr>
							<th align="center" style="font-size:14px;"><%=k+2%></th>

							<td align="center" style="font-size:14px;"><%=(taskListq[k][6]==null||"".equals(taskListq[k][6]))?"无":(taskListq[k][6])%></td>
							<td align="center" style="font-size:14px;"><%=Pub.getUserNameByLoginId(taskListq[k][2])%><%if("0".equals(taskListq[k][7])) out.print("(抄送)");%></td>
							<td align="center" style="font-size:14px;<%if((taskListq[k][6]!=null&&(taskListq[k][6].length()>0))&&(taskListq[k][3]!=null&&(taskListq[k][3].length()>0))&&DateTimeUtil.parse(taskListq[k][3],"yyyy-MM-dd").after(DateTimeUtil.parse(taskListq[k][6],"yyyy-MM-dd"))) out.print("color:red;"); %>"><%=taskListq[k][3]%></td>
							<td align="center" style="font-size:14px;">
							<%if(taskListq[k][4]!=null&&taskListq[k][4].length()>0)
							  {
								 if("3".equals(taskListq[k][4]))
									out.print("<font color='red'>"+Pub.getDictValueByCode("SPJG",taskListq[k][4])+"</font>");
								 else if("4".equals(taskListq[k][4]))
									out.print("<font color='blue'>"+Pub.getDictValueByCode("SPJG",taskListq[k][4])+"</font>");
								 else if("1".equals(taskListq[k][4]))
									out.print("<font color='green'>"+Pub.getDictValueByCode("SPJG",taskListq[k][4])+"</font>"); 
							  }else
								 out.print("<font color='grey'>未处理</font>");
							
							%></td>
							<td align="left" style="font-size:14px;width:30%;word-break:break-all;"><%=taskListq[k][5].replaceAll("\n","<br>")%></td>
							<td align="center" style="font-size:14px;"><%=taskListq[k][0]%></td>
							<td align="center" style="font-size:14px;"><%=taskListq[k][1]%></td>
						</tr>
						<%
							}
						%>
                	</tbody>
				</table>
				
			</div>
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