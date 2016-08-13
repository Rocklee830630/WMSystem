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
					sql = "select a.PERSON_ACCOUNT,b.NAME from fs_org_role_psn_map a,FS_ORG_PERSON b where ROLE_ID='"
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
						+ process.getTaskOID()
						+ "' and STEPSEQUENCE = '1' order by cjsj asc";
				fqr = DBUtil.querySql(conn, cjrsql);
				if (fqr != null) {
					fqrid = fqr[0][0];
					fqrdwdm = fqr[0][1];
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
   	
function doSubmit(jsrStr,jsdwStr)
{
	var m = temp;
	var strAction = "<%=request.getContextPath()%>/TaskAction.do?doApprove";
	try
	{
		var fqrdwdm = '<%=fqrdwdm%>';
		var farid = '<%=fqrid%>';
		var vfjbh = '<%=sp_fjbh%>';
		
        var spUser = '';
		var data1 = '&id=<%=taskid%>&seq=<%=taskseq%>&sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&spbh=<%=spbh%>&spjg=1&sfth=0&spUser='+spUser;
			//选择返回发起人,获取发起人id和部门传递,否则直接传递参数
		 if( m ==4){//点击返回发起人
				data1+= '&tsdept='+fqrdwdm+'&tsperson='+farid;
			}else{
				data1+= '&tsdept='+jsdwStr+'&tsperson='+jsrStr;
			}
		 data1+= '&result=1&resultDscr='+document.getElementById('mind').value+'&fjbh='+vfjbh+'&spFieldname='+spFieldname;;
			
			
			//m=3为特送下一级操作,单独处理.
	
			if(	m==3 || m ==4){
					data1 +='&isSpecialNextStep=1';
				}
		$.ajax({
			type : 'post',
			url : strAction,
			data : data1,
			dataType : 'json',
			async :	false,
			success : function(result) {
				//xAlertMsg(result.msg);
			      //增加自定义业务
				 var fuyemian=$(window).manhuaDialog.getParentObj();
					fuyemian.gengxinchaxun(result.message);
				success = true;
				
				$(window).manhuaDialog.close();
				/* if(document.referrer){
				    location.href = document.referrer;
				 }else{
				    history.back();
				 } */
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

function wsSearch(){

    var strAction_fg = strActionWS+"?method=QueryWslb&dah=<%=dah%>";
      var xmlHttp3 = new ActiveXObject("Msxml2.XMLHTTP");
      xmlHttp3.open("POST",strAction_fg,false);  
      xmlHttp3.send(); 
      var xmlDoms = createDOMDocument(); 
      xmlDoms.loadXML(xmlHttp3.responseText);
      var str =xmlDoms.selectSingleNode("//ROW/LBMC").text;
      var arr = str.split("|");
      var strid =xmlDoms.selectSingleNode("//ROW/LBDM").text;
      var arrid = strid.split("|");
  
      xmlDoms = null; 
    
      var len=arr.length;
   	
	 if(len==1){
	      return; 
	    }
	var toolObj = document.getElementById('tool');
	toolObj.innerHTML = "";
	for(var i=0;i<len-1;i++){
	    var toolObj=document.getElementById('tool');
		var obj=document.createElement('div');
		obj.id = arrid[i];
		toolObj.appendChild(obj);
		var sid=obj.id+"_d"
		obj.className="tree_1";
	
		var ntxt = arr[i];
	  
		obj.innerHTML='<table width="90%"><tr><td width="1%" onclick=showtree("'+arrid[i]+'")><span id='+sid+' style="color:gray" >+</span></td><td width="99%"  style="font-size:13px;color:#006699;border-bottom:solid 2px #0084C4" onclick=showtree("'+arrid[i]+'")><B>'+ntxt+'</B></td></tr></table>';
       var cobj=document.createElement('div');
		cobj.id=obj.id+"_c";
	
		toolObj.appendChild(cobj);	
		cobj.style.display="none";
	  }
	
	}


function openAttachment(wsid,ywlx,sjbh,spzt){

	var wsActionURL = "<%=request.getContextPath()%>/WsPrint/PubWS.do?method=getPrintAction|templateid="+wsid+"|ywlx="+ywlx+"|sjbh="+sjbh+"|spzt="+spzt+"|rowid="+Math.random()+".mht";
	    wsActionURL = "<%=request.getContextPath()%>/jsp/framework/print/pubPrint.jsp?param="+wsActionURL+"&IsQm=0&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid="+wsid+"&isEdit=0"+"&spzt="+spzt;
	 window.open(wsActionURL,"","width=800px,height=600px,toolar=0,menubar=0,scrollbars=1,status=0,resizable=1,screenX=0,screenY=0");
}

function saveYJ(){

    var form1 = iFrame.wsTemplate;
    for(var i=0;i<form1.elements.length;i++)
    {
    	var obj = form1.elements[i];
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
	doSubmit("","");
	
}
function doSave(m)
{
	
	var n=1; // 1：同意 特送只有同意处理
	saveYJ();
	temp = m;
	try
	{
	/* 	if(m ==3){        add by xhb
			openUserTree("single","");
		} */
		
	//	var cList = document.getElementsByName("selectCheckInfo");
		var strMind;
		var strText;
        var hascheck = false;
	/* 	add by xhb
		for(var i = 0;i<document.getElementsByName("radCheckState").length;i++)
		{
			if (document.getElementsByName("radCheckState").item(i).checked)
			{
				strMind = document.getElementsByName("radCheckState").item(i).value;
                hascheck=true;
				break;
			}
		} */
        	if(!n)
        	{
         		if(hascheck == false)
         		{
					
					xAlertMsg("未给出审批结果，请选择'同意'或'退回'");
					return;
         		}
        	}
         	else
        	{
	    		if (document.getElementById("mind").value.length==0)
	    		{
	
	    			requireFormMsg("请给出审批意见!");
				//-------------------------zl modi -----------------------
					if(parseInt(n) == 3){
						document.getElementById("mind").focus();
					}
				//-----------------------------------------
				return;
	    		}
        	} 
        	

	    	
			strText = document.getElementById("mind").value;
			
		if (strText.length == 0)
		{
			strText = '';
        }
		
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


function QuerySplc(sjbh,spbh){
/*    var x=(screen.width-800)/2;
  var y=(screen.height-600)/2;
  var win = window.open ('spFlowView.jsp?sjbh='+sjbh+'&spbh='+spbh, 'newwindow1', 'height=600, width=800, top='+y+',left='+x+', toolbar=no, menubar=no, scrollbars=yes')
   */
  var strUrl = '${pageContext.request.contextPath}/jsp/framework/common/aplink/spFlowView.jsp?sjbh='+sjbh+'&spbh='+spbh;
  
  $(window).manhuaDialog({"title":"查看审批流程","type":"text","content":strUrl,"modal":"1"});
 };
//added by xukx 查看审批意见
function QuerySpyj(sjbh,spbh){

/*   var x=(screen.width-800)/2;
  var y=(screen.height-600)/2;
  var win = window.open (g_sAppName+'/jsp/framework/aplink/spYjView.jsp?sjbh='+sjbh+'&spbh='+spbh, 'newwindow2', 'height=600, width=800, top='+y+',left='+x+', toolbar=no, menubar=no, scrollbars=yes')
  */
  var strUrl = '${pageContext.request.contextPath}/jsp/framework/common/aplink/spYjView.jsp?sjbh='+sjbh+'&spbh='+spbh;
  
  $(window).manhuaDialog({"title":"查看审批意见","type":"text","content":strUrl,"modal":"1"});
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
function doPersonFilter(){//过滤所选部门下的人员
  if(document.all("USERTODEPT").code.length>0){
    var iNum=0;
    var sqlWhere = "";
    for(var i=0;i<document.all("USERTODEPT").code.length;i++){
      if(document.all("USERTODEPT").code.substring(i,i+1)==','){
        var tmp = document.all("USERTODEPT").code.substring(iNum,i);
        if(iNum==0){
          sqlWhere = sqlWhere+" DEPARTMENT='"+tmp+"'";
        }else{
          sqlWhere = sqlWhere+ " OR DEPARTMENT='"+tmp+"'";
        }
        iNum = iNum+1+i;
      }
    };
    if(iNum==0){
      sqlWhere = " DEPARTMENT='"+document.all("USERTODEPT").code+"'";
    }else{
      sqlWhere = sqlWhere+ " OR DEPARTMENT='"+document.all("USERTODEPT").code.substring(document.all("USERTODEPT").code.length-12,document.all("USERTODEPT").code.length)+"'";
   }
    var srcValue = "T#ORG_PERSON:ACCOUNT:NAME:"+sqlWhere;
    document.all("USERTOPERSON").src = srcValue;
  }else{
    document.all("USERTOPERSON").value = "";
    document.all("USERTOPERSON").code = "";
    document.all("USERTOPERSON").src = "";
  }
}
function OpenDetail(sjbh,ywlx,url)
{
	if(!url) url = "jsp/framework/aplink/defaultDetailPage.jsp";
	if(url.indexOf("?") > 0)
	{
		if(url.trim().charAt(url.trim().length-1) == "?")
		{
			url = url.trim()+"sjbh="+sjbh+"&ywlx="+ywlx;
		}
		else
		{
			url = url.trim()+"&sjbh="+sjbh+"&ywlx="+ywlx;
		}
	}
	else
  	{
  		url += "?sjbh="+sjbh+"&ywlx="+ywlx;
  	}
	var strAction="<%=request.getContextPath()%>/"+url;
	var iWidth = 800;
	var iHeight = 600;
	var iLeft = screen.width - iWidth - 100;
	var iTop = screen.height - iHeight - 80;
	var sFeatrue = "top=" + iTop + ", left=" + iLeft + ",scrollbars=no"

//	window.open(strAction,"summary",sFeatrue);

	window.open(strAction,"name","title=print,toolbar=no, status=yes,menubar=yes,scrollbars=yes,resizable=yes");
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
function clickSpjg(obj)
{
	//alert(obj);
  if(obj){
	 if(clickNum == 0)
	 {
		   clickNum = 1 ;
           document.getElementById("mind").value="";
       	   document.getElementById("mind").disabled=false;
           document.getElementById("mind").readOnly = false;

	 }else{
		if(document.getElementById("mind").value.length == 0)
		{
             document.getElementById("mind").value="";
             document.getElementById("mind").disabled=false;

		}
		else		
		{
          //  xConfirm("提示信息","是否确定修改审批意见？");
    		
    		//$('#ConfirmYesButton').attr('click','');//.click( eval(function(){test(dd);}));
    		//$('#ConfirmYesButton').one("click",function(){
    			
    		//	spyjUpdate();
    			
    	//	});
			
/*             if(confirm("是否确定修改审批意见？"))
            {

                 document.getElementById("mind").value="";
                 document.getElementById("mind").disabled=false;
           	} */
		}
	 }
  }
  //---------------------------------------zl modi 2007 09 25
  else
  {
  	 document.getElementById("mind").disabled=false;
	 document.getElementById("mind").readOnly = false;
  	 //document.getElementById("mind").focus();
  	 //document.all("btnAzt1").disabled = true;
  }
  resultValue=obj.value;
  //------------------------------------------modi end---------------------
}

function doUnLoad()
{

	if(!spflag)
	{
		setTaskInit("<%=taskid%>","<%=taskseq%>");
	}
}
$(function() {
	/* doSearch (1); */
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
        	   
        	   xAlert("运行结果","无审批文书");
				
           };
		
	});
	var splc=$("#splc");
	splc.click(function() {
		
		QuerySplc('<%=sjbh%>','<%=spbh%>');
	});


	 //监听变化事件
    $("#USERTODEPT").change(function() {
    	generatePc($("#USERTOPERSON"));
    }); 
	
    var btnnext = $("#btnnext");
    btnnext.click(function() {
    	doSave(3);
    });
    var btnfqr = $("#btnfqr");
    btnfqr.click(function() {
    	//doSave(4);
    	doSaveFh(4);
    });
    
	// add by xhb start
	$("#blrBtn").click(function(){
		var actorCode = $("#ACTOR").attr("code");
		selectUserTree('single',actorCode,'setBlr') ;
	});
	// add by xhb end
});

// add by xhb start
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

function doArchiveTask()
{

  xConfirm("提示信息","是否确定结束审批?");
	$('#ConfirmYesButton').unbind();
	$('#ConfirmYesButton').attr('click','');
	$('#ConfirmYesButton').one("click",function(){
		
		doJssp();
		
	});  
        
}
function doJssp()
{
	 customOperation();
		 
}
function printDiv()
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
    var strActionUrl = "<%=request.getContextPath()%>/TaskBackAction.do?customOperationAction";
    
	var data1 = '&eventid=<%=sjbh%>&ywlx=<%=ywlx%>&spbh=<%=spbh%>';
		$.ajax({
			type : 'post',
			url : strActionUrl,
			data : data1,
			dataType : 'json',
			async :	false,
			success : function(result) {
				
				// xAlertMsg(result.Msg);
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

    return true;
}
</script>
</head>
<body TOPMARGIN="0" LEFTMARGIN="0" style="overflow:visible">
<app:dialogs/>
<div class="container-fluid">
<p></p>
 <div class="row-fluid">
        <div class="B-small-from-table-autoConcise">
     
            <div class="span7" style="height:100%;overflow:hidden;">
             	<iframe name="iFrame" width="100%" height="90%" src="/xmgl/PubWS.do?getXMLPrintAction&templateid=<%=ws_template_id%>&sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&isEdit=1" FRAMEBORDER=0 SCROLLING= ></iframe>
			</div>
			
    		<div class="span5" >
		    		<h4 class="title">
						<span class="pull-right">
		
						<input type="button" value="报下一级审批" class="btn" id = "btnnext" name="btnSave" > 
						<%
 							if (useraccount.equals(fqrid)) {
 						%>
						<input type="button" value="审批结束" class="btn" name="btnJssp" onClick="doArchiveTask();"> 
						<input type="button" value="打印" class="btn" name="btnDybd" onClick="printDiv();"> 
						<%
 							}else{
 						%>
 						<input type="button" value="返回发起人" class="btn" id = "btnfqr" name="btnFhfqr" > 
 						<%
 							}
 						%>
 						</span>
 					</h4>
 					
           	      	<div class="B-small-from-table-auto">
           	      		<form method="post" id="demoForm">
						<table class="B-table" width="100%">
							<tr<%--  style="display:<%if(!("4".equals(""))) out.println("none");%>" --%>>
								<th class="right-border bottom-border" style="font-size:14px;">办理人</th>
								<td class="bottom-border right-border"  colspan="7">
			         			<input class="span12"  type="text" check-type="required" fieldname="ACTOR" id="ACTOR" name ="ACTOR" style="width:75%" disabled />
			         			<button class="btn btn-link"  type="button" id="blrBtn"><abbr title="点击选择办理人"><i class="icon-edit"></i></abbr></button>
			       	 		</td>
							<%
								if (!useraccount.equals(fqrid)) {
							%>
							<tr style="display:none">
								<td width="42%" colspan="4" class="bottom-border"
									style="border-right-style: none;font-size:14px;"><label
									class="checkbox inline"><input type="checkbox"
										id="FHFQR" name="FHFQR" value="1" onClick="dealValue()">返回发起人</label>
								</td>
							</tr>
							<%
								}
							%>
	                       <tr>
							<th width="12%" class="right-border bottom-border text-center" style="font-size:14px;">附件信息</th>
							<td width="88%" colspan="7" class="bottom-border right-border">
								<div>
									<span class="btn btn-fileUpload" onclick="doSelectFile(this);"
										fjlb="3999"> <i class="icon-plus"></i> <span>添加文件...</span>
									</span>
									<table role="presentation" class="table table-striped">
										<tbody class="files showFileTab"
											data-toggle="modal-gallery" data-target="#modal-gallery">
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
							<table width="100%" class="table" style="display:none">
								<tr id="spyjTextArea" style="display: none;">
									<th width="50" align="center" valign="middle" style="font-size:14px;">审批意见</th>
									<td width="30%">
										<p>
										<form class="form-inline">
											<label class="inline"> <input name="radCheckState"
												type="radio" value="1" fjbh="" wstemplate=""
												onclick="clickSpjg(this)"> 同意
											</label> &nbsp;&nbsp; <label class="inline"> <input
												type="radio" name="radCheckState" value="3" fjbh=""
												wstemplate="" onclick="clickSpjg(this)">退回
											</label>
										</form>
										</p>
										
			

										</p>
									</td>
									</tr>
									<tr>
									<th width="100" align="center" valign="middle">领导批示</th>
									<td colspan="2">
									<div>
											<textarea class="span12" rows="4" name="txtCheckMind"
												id="mind" maxLength="2000"></textarea>
										</div>
									</td>

								</tr>
								</table>
									

						</div>
						

			<br>
			<div style="height:5px;"> </div>

			<div class="B-small-from-table-auto" style="display: none;">
				<h4>审批信息</h4>
			<div class= "overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" style="display:none">
                <thead>
                    <tr>	
                    		<th name="XH" id="_XH" style="font-size:14px;">&nbsp;节点序号&nbsp;</th>
                    		<th fieldname="SPBM"  style="font-size:14px;">&nbsp;审批部门&nbsp;</th>
							<th fieldname="SPR" style="font-size:14px;">&nbsp;审批人&nbsp;</th>
							<th fieldname="SJSPSJ" style="font-size:14px;">&nbsp;审批时间&nbsp;</th>
							<th fieldname="SPYJ" style="font-size:14px;">&nbsp;审批意见&nbsp;</th>
						</tr>
					</thead>
						<tbody>
									<%
										String sqldbxx = "select t.dbryid,t.dbdwdm,t.wcsj,t.spyj,t.wcrxm from AP_TASK_SCHEDULE t where sjbh = '"
												+ sjbh + "' order by cjsj";
										//System.out.println("aaaa:" + sqldbxx);
										String[][] taskListq = DBUtil.query(sqldbxx);
										String slsj = "";
										String slr = "";
										String pcs = "";
										String spyj = "";
										if (taskListq != null) {

											for (int i = 0; i < taskListq.length; i++) {
												slsj = taskListq[i][2];
												spyj = taskListq[i][3];
												slr = taskListq[i][4];
												pcs = Pub.getDeptNameByID(taskListq[i][1]);
												//System.out.println("aaaa:" + slr);
												//System.out.println("sjbh:" + sjbh);
									%>
									
						<tr>
							<td align="center" style="font-size:14px;">第<%=i + 1%>节点</td>
							<td align="center" style="font-size:14px;"><%=pcs%></td>
							<td align="center" style="font-size:14px;"><%=slr%></td>
							<td align="center" style="font-size:14px;"><%=slsj%></td>
							<td align="center" style="font-size:14px;"><%=spyj%></td>
						</tr>
						<%
							slsj = "";
									slr = "";
									pcs = "";
									spyj = "";
								}
							}
						%>
                	</tbody>
				</table>
				</div>
			</div>
		
    	</div>		
	</div>
</div> 
	</body>
	</html>   
<script language="javascript">
var aaa = "<%=gdspsj1%>";

</script>