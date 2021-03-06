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
	String pageareaURL = "";

	String gdspsj1=null;
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
var spFieldname;
//--------------------------------zl add gz-------------------------------
var template_ID="";
//--------------------------------------------
var strAction = "${pageContext.request.contextPath}/TaskAction.do?";
var strActionWS = "${pageContext.request.contextPath}/ViewWsAction.do?";
var hasWs = false;

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
function doSubmit()
{
	
	var strAction = "<%=request.getContextPath()%>/TaskAction.do?doApprove";
	try
	{
		document.frmPost.checkOver.disabled=true;
		//alert(111);
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
		var data1 = "&id=<%=taskid%>&seq=<%=taskseq%>&sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&spbh=<%=spbh%>&spjg="+resultValue+"&sfth="+(resultValue==3?1:0)+"&spUser="+spUser;
			data1+= "&tsdept="+document.all('USERTODEPT').value+"&tsperson="+document.all('USERTOPERSON').value+"&tsstep="+document.all("USERSTEP").value;
			data1+= "&result="+resultValue+"&resultDscr="+document.getElementById('mind').value+"&fjbh=<%=sp_fjbh%>&spFieldname="+spFieldname;
			//alert(strAction);
		$.ajax({
			type : 'post',
			url : strAction,
			data : data1,
			dataType : 'json',
			async :	false,
			success : function(result) {
				//xAlertMsg(result.msg);
			      //增加自定义业务
				 var fuyemian=parent.$("body").manhuaDialog.getParentObj();
					fuyemian.gengxinchaxun(result.message);
				success = true;
				
				parent.$("body").manhuaDialog.close();
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
		document.frmPost.checkOver.disabled=false;
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

function doSave()
{
	saveYJ();
	//return;
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
					
					xAlertMsg("未给出审批结果，请选择'同意'或'退回'");
					return;
         		}
        	}
        	else
        	{
	    		if (document.getElementById("mind").value.length==0)
	    		{
	
					xAlertMsg("请给出审批意见!");
					return;
	    		}
        	}
        	
	    	if (document.getElementById("mind").value.length>1000)
	    	{
	    		xAlertMsg('审批意见不能超过1000个汉字，请核对!');
		   		//document.getElementById("mind").focus();
		   		return false;
	    	}
	    	
			strText = document.getElementById("mind").value;
			
		if (strText.length == 0)
		{
			strText = '';
        }
		
		//alert(123);

          if(n == 3){ // 回退操作
        		/* if(window.confirm("是否退回审批信息?"))
				{
					doSubmit();
				} */

        		xConfirm("提示信息","是否退回审批信息?");
        		
        		$('#ConfirmYesButton').attr('click','');//.click( eval(function(){test(dd);}));
        		$('#ConfirmYesButton').one("click",function(){
        			
        			doSubmit();
        			
        		});  
        		
          }else{
        	  
              	if (document.getElementById("mind").value.length==0)
	    		{
	
					xAlertMsg("请给出审批意见!");
					return;
	    		}
                    
                    xConfirm("提示信息","是否提交审批信息?");
            		
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
/*    var x=(screen.width-800)/2;
  var y=(screen.height-600)/2;
  var win = window.open ('spFlowView.jsp?sjbh='+sjbh+'&spbh='+spbh, 'newwindow1', 'height=600, width=800, top='+y+',left='+x+', toolbar=no, menubar=no, scrollbars=yes')
   */
  var strUrl = '${pageContext.request.contextPath}/jsp/framework/common/aplink/spFlowView.jsp?sjbh='+sjbh+'&spbh='+spbh;
  
   $("body").manhuaDialog({"title":"查看审批流程","type":"text","content":strUrl,"modal":"1"});
 };
//added by xukx 查看审批意见
function QuerySpyj(sjbh,spbh){

/*   var x=(screen.width-800)/2;
  var y=(screen.height-600)/2;
  var win = window.open (g_sAppName+'/jsp/framework/aplink/spYjView.jsp?sjbh='+sjbh+'&spbh='+spbh, 'newwindow2', 'height=600, width=800, top='+y+',left='+x+', toolbar=no, menubar=no, scrollbars=yes')
  */
  var strUrl = '${pageContext.request.contextPath}/jsp/framework/common/aplink/spYjView.jsp?sjbh='+sjbh+'&spbh='+spbh;
  
  $("body").manhuaDialog({"title":"查看审批意见","type":"text","content":strUrl,"modal":"1"});
};


	<%-- function doSearch (n)
	{
		//
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
        	 hasWs = true;
         }
		 if(fjbh.length>0&&fjbhs.length>0){
             for(var i =0;i<fjbhs.length;i++){
                if(i==0){
					var radCheckState = document.getElementsByName("radCheckState");
                   radCheckState[0].fjbh=fjbhs[i];
                   radCheckState[0].wstemplate=ws_template_ids[i];
                   radCheckState[1].fjbh=fjbhs[i];
                   radCheckState[1].wstemplate=ws_template_ids[i];
                }
				 wsText+="<a href='javascript:void(0)' onclick='print(\""+fjbhs[i]+"\",\""+ws_template_ids[i]+"\",\""+sjbh+"\",\""+ywlx+"\")'>查看"+ws_template_names[i]+"</a><br>";
                 if(i!=fjbhs.length-1){
					wsText+="<br>";
                 }
             }
             
				//document.all("spWs").innerHTML = wsText;
				//alert(wsText)
           }else{
				//document.all("spWs").innerText = "无审批文书";
           }
		
		//
        document.all("splc").innerHTML = "<p class='slider' style='font-size:14px;' onclick='QuerySplc(\"<%=sjbh%>\",\"<%=spbh%>\");'>查看审批流程</p>";

        //added by xukx 增加查看审批意见列
   
        //end
	} --%>

function print(fjbh,ws_template_id,sjbh,ywlx){
   <%--  var strAction = "<%=request.getContextPath()%>/PubWS.do?getPrintAction&templateid="+ws_template_id+"&sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&ywid=<%=ywid%>"+"&isEdit=1&isSp=1&rowid="+Math.random()+".mht";

    parent.$("body").manhuaDialog({"title":"呈请审批","type":"text","content":strAction,"modal":"1"});
 --%>   
 // window.open(strAction,"", 'title=print,toolbar=no, menubar=yes,scrollbars=yes,resizable=yes');
   
    var wsActionURL = '/xmgl/PubWS.do?getXMLPrintAction|templateid='+ws_template_id+'|isEdit=0|ywlx='+ywlx+'|sjbh='+sjbh+'|rowid='+Math.random()+'.mht';

    var rswsActionURL = '/xmgl/jsp/framework/print/pubPrint.jsp?param='+wsActionURL+'&isview=1&sjbh='+sjbh+'&ywlx='+ywlx+'&temlateid='+ws_template_id+'&isEdit=0&ywid='+p_ywid;

   // window.open(wsActionURL,"","width=800px,height=600px,toolar=0,menubar=0,scrollbars=1,status=0,resizable=1,screenX=0,screenY=0");
     $("body").manhuaDialog({"title":"查看文书","type":"text","content":rswsActionURL,"modal":"1"});  

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
            xConfirm("提示信息","是否确定修改审批意见？");
    		
    		$('#ConfirmYesButton').attr('click','');//.click( eval(function(){test(dd);}));
    		$('#ConfirmYesButton').one("click",function(){
    			
    			spyjUpdate();
    			
    		});
			
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
<%-- 	var Ygdspsj ='<%=gdspsj%>';
	alert(Ygdspsj); --%>
});

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
$(function() {
	
	setFileData("","","<%=sjbh%>","<%=ywlx%>");
	queryFileData("","","<%=sjbh%>","<%=ywlx%>");
});
</script>
</head>
<body TOPMARGIN="0" LEFTMARGIN="0" style="overflow:visible" >
<app:dialogs/>
<div class="container-fluid">
    <p><!--占位用--></p>
    <div class="row-fluid" >

        	<div class="span7" style="height:100%;overflow:hidden;">
             	<iframe name="iFrame" width="100%" height="90%" src="/xmgl/PubWS.do?getXMLPrintAction&templateid=<%=ws_template_id%>&sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&isEdit=1" FRAMEBORDER=0 SCROLLING= ></iframe>
             	<div class="text-center">
					<input type="button" style="font" value="审批结束" class="btn" name="btnSave" onClick="doSave();">
            	</div>
			</div>
			
    		<div class="span5" >
           	      <div class="B-small-from-table-auto">
                       <form method="post" action="" id="insertForm">
                       <table class="B-table" width="100%">
                       <tr>
						<th width="12%" class="right-border bottom-border text-right">附件信息</th>
						<td width="88%" colspan="7" class="bottom-border right-border">
							<div>
								<span class="btn btn-fileUpload" onclick="doSelectFile(this);"
									fjlb="3004"> <i class="icon-plus"></i> <span>添加文件...</span>
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
		<!-- iframe name="iFrame" width="100%" style="height:500px;" src="/xmgl/PubWS.do?getXMLPrintAction&templateid=<%=ws_template_id%>&sjbh=<%=sjbh%>&ywlx=<%=ywlx%>&isEdit=1" FRAMEBORDER=0 SCROLLING= ></iframe>-->
							<table width="100%" class="table" style="display:none">
								<tr id="spyjTextArea">
									<th width="50" align="center" valign="middle" style="font-size:14px;">审批意见</th>
									<td width="30%">
										<p>
										<form class="form-inline">
											<label class="inline"> <input name="radCheckState"
												type="radio" value="1" fjbh="" wstemplate=""
												onclick="clickSpjg(this)" checked> 同意
											</label> &nbsp;&nbsp; <label class="inline"> <input
												type="radio" name="radCheckState" value="3" fjbh=""
												wstemplate="" onclick="clickSpjg(this)">退回
											</label>
										</form>
										</p>

									</td>
									
									<td style="text-align:right;font-size:14px;"><b>规定审批时限</b>:&nbsp;&nbsp;<span style="font-size:14px" id="gdspsj"></span>&nbsp;&nbsp;</td>
									</tr>
									<tr>
									<th width="100" align="center" valign="middle">领导批示</th>
									<td colspan="2">
									<div>
											<textarea class="span12"  rows="4" name="txtCheckMind"
												id="mind" maxLength="2000"></textarea>
										</div>
									</td>
									<TD id="processtype2" style="display: none" align="center">特送节点:
										<span><INPUT id="USERSTEP" name="USERSTEP" type="text"
											must="false" class="Edit" kind="dic" src="" multi="false"
											onblur="doPersonFilter()"></span> <span><input
											style="" type="text" name="ddd" class="Edit" kind="text"></span>
									</TD>
									<TD id="processtype3" style="display: none;" align="left">
										特送部门: <span><INPUT id="USERTODEPT" name="USERTODEPT"
											type="text" must="false" class="Edit" kind="dic" src="ZZJG"
											style="width: 40%" multi="false" onblur="doPersonFilter()"></span>
										特送人: <span><INPUT id="USERTOPERSON" name="USERTOPERSON"
											type="text" must="false" class="Edit" kind="dic" hint=""
											multi="false" style="width: 20%"></span> <span><input
											style="" type="text" name="ddd" class="Edit" kind="text"></span>
									</TD>


								</tr>

								
							</table>
						</div>
				<div class="text-center">
				<form name="frmPost" method="post"
					action="<%=request.getContextPath()%>/TaskAction.do?method=doApprove">
					<input type="button" value="保存意见" style="display: none" class="btn"
						disabled name="checkOver" onClick="doSubmit();";> 
					
						 <!-- <input type="button" value="返   回" class="btn" name="btnBack" onClick="history.back();">  -->
						<input
						type="hidden" name="txtXML"> <input type="hidden"
						name="resultXML"> <input type="hidden" name="queryXML">
					<INPUT type="hidden" name="txtQueryMode" value="0"> 
					<input type="hidden" name="txtParentFilter"> 
					<input type="hidden" name="txtFilter"> 
					<INPUT type="hidden" name="templateid"	id="templateid"> 
					<INPUT type="hidden" name="txtSubmitSQL">
					<INPUT type="hidden" name="result"> 
					<input type="hidden" name="hh" value="<%=strHh%>" /> 
					<input type="hidden" name="rowData">
				</form>
			</div>

		</div>
	</div> 
	</body>
	</html>   
