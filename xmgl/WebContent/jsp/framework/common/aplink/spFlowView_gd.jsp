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
	String spbh = request.getParameter("sjbh");
	if(spbh==null){
		spbh = "";
	}
%>

<head>
<TITLE>审批流程图示</TITLE>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<BODY>
<script type="text/javascript" charset="UTF-8">

	$(function() {
		init ();
	});
	function init (){
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
            	
                var a = parent.document.getElementById("spFlowView");
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
    function singleView(rowJson){
    	var lcHtml = "";
    	  var yxbs = rowJson.YXBS;
		  var wcsj = rowJson.WCSJ;
		  var spyj = rowJson.RESULTDSCR;
		  var spyj_s = "";
		  if(spyj.length>8){
			  spyj_s = spyj.substring(0,8)+"...";
		  }
		  var dbr  = rowJson.DBRYID_SV;
		  var cjsj = rowJson.CJSJ;
		  var result = rowJson.RESULT;
		  var cqbs = rowJson.CQBS;
		  var rwlx = rowJson.RWLX;
		  var classname = "ProcessSuccess Process";
		  if(cqbs!=""&&cqbs<0){
			  classname = "ProcessError Process";//超时显示红色
		  }
		  if(result==""&&rwlx!="1"){
			  classname = "ProcessWarning Process";//未办理显示黄色
		  }
		  if(rwlx==1){
			  if(wcsj==""){
				  classname = "ProcessWarning Process";//未办理显示黄色
			  }else{
				  classname = "ProcessSuccess Process";
			  }
		  }
			  if(yxbs=="1"||yxbs==""){
				  if(result=="1"){
				   lcHtml +="<div class=\""+classname+"\">";//审批人
				   lcHtml +="<h5>审批信息</h5>";
				   lcHtml +="审批人："+dbr+"<br>";
				   lcHtml +="审批结果：同意<br>";
				   if(spyj_s!=""){
					lcHtml +="审批意见：<abbr title=\""+spyj+"\">"+spyj_s+"</abbr><br>";  
				   }else{
				    lcHtml +="审批意见："+spyj+"<br>";
				   }
				   lcHtml +="完成时间："+wcsj;

				  }else if(result=="3"){
					   lcHtml +="<div class=\""+classname+"\">";//审批人
					   lcHtml +="<h5>审批信息</h5>";
					   lcHtml +="审批人："+dbr+"<br>";
					   lcHtml +="审批结果：退回<br>";
					   if(spyj_s!=""){
							lcHtml +="退回意见：<abbr title=\""+spyj+"\">"+spyj_s+"</abbr><br>";  
						   }else{
						    lcHtml +="退回意见："+spyj+"<br>";
					   }
					   lcHtml +="完成时间："+wcsj;

				  }else if(result=="4"){
					   lcHtml +="<div class=\""+classname+"\">";//审批人
					   lcHtml +="<h5>办理信息</h5>";
					   lcHtml +="办理人："+dbr+"<br>";
					   lcHtml +="办理结果：重新发起<br>";
					   lcHtml +="完成时间："+wcsj;

				  }else{
					  if(rwlx==1){
						   lcHtml +="<div class=\""+classname+"\">";//审批人
						   lcHtml +="<h5>归档信息</h5>";
						   lcHtml +="归档人："+dbr+"<br>";
						   lcHtml +="完成时间："+wcsj;
					  }else{
						   lcHtml +="<div class=\""+classname+"\">";//审批人
						   lcHtml +="<h5>审批信息</h5>";
						   lcHtml +="审批人："+dbr+"<br>";
						   lcHtml +="审批结果：<br>";
						   if(spyj_s!=""){
								lcHtml +="审批意见：<abbr title=\""+spyj+"\">"+spyj_s+"</abbr><br>";  
							   }else{
							    lcHtml +="审批意见："+spyj+"<br>";
						   }
						   lcHtml +="完成时间："+wcsj;
					  }
					  
				  }
				  lcHtml +="</div>";
			  }else{
				  lcHtml +="<div class=\"ProcessInfo Process\">"; //抄送人 
				  lcHtml +="<h5>抄送信息</h5>";
				  lcHtml +="抄送人："+dbr+"<br>";
				  if(spyj_s!=""){
						lcHtml +="意见：<abbr title=\""+spyj+"\">"+spyj_s+"</abbr><br>";  
					   }else{
					    lcHtml +="意见："+spyj+"<br>";
				   }
				  lcHtml +="完成时间："+wcsj;
				  lcHtml +="</div>";
			  }
	     return lcHtml;
    }
    
    function  getView(rows,steprows,processinfo){
    	 var lcHtml = "";
    	 if(rows.length>0){
    		 for(var i=0;i<rows.length;i++){
    			 var rowJson = rows[i];
    			  if(rowJson!=null){
    				  if(i==0){
    					  lcHtml+="<div class=\"ProcessBox\">";
    					  lcHtml +="<div class=\"ProcessSuccess Process\">";
						  lcHtml +="<h5>流程发起</h5>";
						  lcHtml +="发起人："+processinfo.CJRID_SV+"<br>";
						  lcHtml +="创建时间："+processinfo.CREATETIME+"<br>";
						  lcHtml +="办理意见："+processinfo.VALUE4;
						  lcHtml +="</div>";
        				  lcHtml +="</div>";
        				  lcHtml +="<p class=\"text-center theNext\"> <i class=\"icon-arrow-down\"></i></p>";
    				  }
    				  lcHtml+="<div class=\"ProcessBox\">"; //审批人
    				  lcHtml +=singleView(rowJson);
    				  var cjsj = rowJson.CJSJ;
	    				  for(var j=i+1;j<rows.length;j++){
	    					  if(cjsj==rows[j].CJSJ){
	    						  lcHtml +=singleView(rows[j]);
	    		    				  i++;
	    					  }else{
	    						  break;
	    					  }
	    				  }

    				  lcHtml +="</div>";
    				  
    				  if(i<rows.length-1){
    					  lcHtml +="<p class=\"text-center theNext\"> <i class=\"icon-arrow-down\"></i></p>";
    				  }
    			  }
    		 }	
    		 if(steprows.length>0){
			    		lcHtml +="<p class=\"text-center theNext\"> <i class=\"icon-arrow-down\"></i></p>";
        		 for(var i=0;i<steprows.length;i++){
        			 var rowJson = steprows[i];
       			       if(rowJson!=null){
       			    	  
        				  lcHtml+="<div class=\"ProcessBox\">";
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
        						  lcHtml +="<div class=\"ProcessGray Process\">";
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
        						  lcHtml +="<div class=\"ProcessGray Process\">";
        						  lcHtml +="<h5>"+name+"</h5>";
        						  lcHtml +="抄送人："+ms[m]+"<br>";
        						  lcHtml +="规定天数："+days;
        						  lcHtml +="</div>";
        					  }
        				  }
        				  lcHtml +="</div>";
        				  
        				  if(i<steprows.length-1){
        					  lcHtml +="<p class=\"text-center theNext\"> <i class=\"icon-arrow-down\"></i></p>";
        				  }
        			  }
        		 }
    		 }
    		 
    		 $("#lct").empty();
    		 $("#lct").html(lcHtml);
    	 }
    	 setStyle();
    }
    
    function show(){
    	var controllername= "${pageContext.request.contextPath }/lcglController.do?queryProcessTask&processoid=<%=spbh%>";
    	
    	$.ajax({
    		url : controllername,
    		data : "",
    		dataType : 'json',
    		async :	false,
    		type : 'post',
    		success : function(result) {
    			var resultmsgobj = convertJson.string2json1(result.msg);
    			if("0"==JSON.stringify(resultmsgobj)){
    				$("#lct").html("此合同是历史数据！没有会签流程");
    				parent.document.getElementById("cmdSpyj").disabled="disabled";
    				return;
    			}
    			var subresultmsgobj = resultmsgobj.response.data;
    			var processprompt = result.prompt;
    			var processinfo = result.obj;
    			if(processprompt==0){
    				getView(subresultmsgobj,"",(convertJson.string2json1(processinfo).response.data)[0]);
    			}else{
    		   	    getView(subresultmsgobj,convertJson.string2json1(processprompt).response.data,(convertJson.string2json1(processinfo).response.data)[0]);
    			}
    		
    		}
    	});
    	
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

<div class="container-fluid">
    <div class="row-fluid">
		<div class="span12">
        	<div class="windowOpenAuto" id="lct">

            </div>
        </div>
    </div>
</div>

</BODY>
</HTML>