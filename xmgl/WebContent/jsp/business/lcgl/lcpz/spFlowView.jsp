<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.coreapp.aplink.*"%>
<%@ page import="com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager"%>
<%@ page import="com.ccthanking.framework.common.*"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	String processoid = request.getParameter("processoid");
	if(processoid==null)processoid = "";
			
%>

<head>
<TITLE>审批流程图示</TITLE>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<BODY>
<script type="text/javascript" charset="UTF-8">
var processoid = "<%=processoid%>";
var controllername= "${pageContext.request.contextPath }/lcglController.do";

	$(function() {
		init ();
	});
	function init (){
		if(processoid!=""){
		  var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		  defaultJson.doQueryJsonList(controllername+"?queryProcessStep", data, DT1);
		}
		show();
		iframeAutoFit();
	}
    //自适应iframe
	function iframeAutoFit()
    {
        try
        {
            if(window!=parent)
            {
                var a = parent.document.getElementsByTagName("IFRAME");
                for(var i=0; i<a.length; i++) //author:meizz
                {
                    if(a[i].contentWindow==window)
                    {
                        var h1=0, h2=0, d=document, dd=d.documentElement;
                        a[i].parentNode.style.height = a[i].offsetHeight +"px";
                        a[i].style.height = "10px";

                        if(dd && dd.scrollHeight) h1=dd.scrollHeight;
                        if(d.body) h2=d.body.scrollHeight;
                        var h=Math.max(h1, h2);

                        if(document.all) {h += 4;}
                        if(window.opera) {h += 1;}
                        a[i].style.height = a[i].parentNode.style.height = h +"px";
                    }
                }
            }
        }
        catch (ex){}
    }
    function show(){
    	var tabObj = null;
    	if(processoid!=""){
    		tabObj = document.getElementById("DT1");
    	}else{
    		tabObj = parent.document.getElementById("DT1");
    	}
    	var $tabObj = $(tabObj);
    	 var rows = $("tbody tr" ,$tabObj);
    	 var lcHtml = "";
    	 if(rows.length>0){
			  lcHtml+="<div class=\"ProcessBox\">";
    		  lcHtml +="<div class=\"ProcessSuccess Process\">";
			  lcHtml +="<h5>流程发起</h5>";
//			  lcHtml +="办理人："+ms[m]+"<br>";
			  lcHtml +="</div>";
			  lcHtml +="</div>";
			  lcHtml +="<p class=\"text-center theNext\"> <i class=\"icon-arrow-down\"></i></p>";
    		 for(var i=0;i<rows.size();i++){
    			 var rowJson = rows.eq(i).attr("rowJson");
    			  if(rowJson!=""){
    				  lcHtml+="<div class=\"ProcessBox\">";
    				  rowJson = JSON.parse(rowJson);
    				  var name = rowJson.NAME;
    				  var actor = rowJson.ACTOR_SV;
    				  var days = rowJson.SHEDULE_DAYS;
    				  var ccactor = rowJson.CCACTOR_SV;
    				  var deptid = rowJson.DEPTID_SV;
    				  var rolename = rowJson.ROLENAME_SV;
    				  if(actor&&actor!=""){
    					  var ms = actor.split(",");
    					  for(var m=0;m<ms.length;m++)
    					  {
    						  lcHtml +="<div class=\"ProcessSuccess Process\">";
    						  lcHtml +="<h5>"+name+"</h5>";
    						  lcHtml +="办理人："+ms[m]+"<br>";
    						  lcHtml +="规定天数："+days;
    						  lcHtml +="</div>";
    					  }
    				  }else{
    					  lcHtml +="<div class=\"ProcessSuccess Process\">";
						  lcHtml +="<h5>"+name+"</h5>";
						  if(rolename)
						  lcHtml +="节点角色："+rolename+"<br>";
						  if(deptid)
						  lcHtml +="节点单位："+deptid+"<br>";
						  lcHtml +="规定天数："+days;
						  lcHtml +="</div>";
    				  }
    				  if(ccactor&&ccactor!=""){
    					  var ms = ccactor.split(",");
    					  for(var m=0;m<ms.length;m++)
    					  {
    						  lcHtml +="<div class=\"ProcessInfo Process\">";
    						  lcHtml +="<h5>"+name+"</h5>";
    						  lcHtml +="抄送人："+ms[m]+"<br>";
    						  lcHtml +="规定天数："+days;
    						  lcHtml +="</div>";
    					  }
    				  }
    				 // lcHtml +="<div class=\"clearDiv\"></div>";
    				  lcHtml +="</div>";
    				  
    				  if(i<rows.size()-1){
    					  lcHtml +="<p class=\"text-center theNext\"> <i class=\"icon-arrow-down\"></i></p>";
    				  }
    			  }
    		 }	
			  lcHtml +="<p class=\"text-center theNext\"> <i class=\"icon-arrow-down\"></i></p>";
    		  lcHtml+="<div class=\"ProcessBox\">";
   		      lcHtml +="<div class=\"ProcessSuccess Process\">";
			  lcHtml +="<h5>流程归档</h5>";
//			  lcHtml +="办理人："+ms[m]+"<br>";
			  lcHtml +="</div>";
			  lcHtml +="</div>";
    		 $("#lct").empty();
    		 $("#lct").html(lcHtml);
    	 }
    	 setStyle();
    	
    }
    function setStyle(){
    	 $('.ProcessBox').css('width',function () {
 			var ProcesWidth = $('.Process').innerWidth();
 			var ProcesNum = ($(this).find('.Process:last').index())+1;
 			var ProcesBoxsWidth = ($(this).find('.Process').innerWidth()+20)*(ProcesNum) + 2*(ProcesNum);
 			var ProcesNextsWidth = ($(this).find('.next').innerWidth())*(ProcesNum-1);
 			var ProcessBoxSum = ProcesBoxsWidth + ProcesNextsWidth;
 			/*if (ProcesNum == 0) {
 				return (ProcesWidth)	
 			}*/
 			return (ProcessBoxSum);
 		})
    }
    
</script>    
<p></p>
<form method="post" id="queryForm" >
		<table class="B-table">
		<!--可以再此处加入hidden域作为过滤条件 -->
			<TR  style="display:none;">
				<TD class="right-border bottom-border"></TD>
				<TD class="right-border bottom-border">
					<input type="text" class="span12" kind="text"  fieldname="processtypeoid"  value="<%=processoid%>" operation="=" >
				</TD>
			</TR>
		</table>
		</form>
      <div style= "display:none">
		<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" noPage="true" >
			<thead>
				<tr>
					<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
					<th fieldname="NAME" tdalign="center" >&nbsp;节点名称&nbsp;</th>
					<th fieldname="ISMS" tdalign="center" >&nbsp;报送方式&nbsp;</th>
					<th fieldname="SHEDULE_DAYS" >&nbsp;规定天数&nbsp;</th>
					<th fieldname="DEPTID" tdalign="center">&nbsp;节点单位&nbsp;</th>   
					<th fieldname="ROLENAME" tdalign="center">&nbsp;节点角色&nbsp;</th>   
					<th fieldname="ACTOR" tdalign="center">&nbsp;办理人&nbsp;</th>   
					<th fieldname="CCACTOR" >&nbsp;抄送人&nbsp;</th>
				</tr>
			</thead>
		    <tbody></tbody>
		</table>
		 <br>           
		</div>
<div class="container-fluid">
    <div class="row-fluid">
		<div class="span12">
        	<div class="windowOpenAuto" id="lct">

            </div>
        </div>
    </div>
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id = "queryXML">
		<input type="hidden" name="txtXML" id = "txtXML">
		<input type="hidden" name="txtFilter"  order="asc" fieldname = "STEPSEQUENCE"	id = "txtFilter">
		<input type="hidden" name="resultXML" id = "resultXML">
		<input type="hidden" name="queryResult" id = "queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">
	</FORM>
</div>
</BODY>
</HTML>