<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<app:base/>
<title>综合管理</title>
<%
	String id=request.getParameter("id");
	String zt=request.getParameter("zt");
	Date d=new Date();//获取时间
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//转换格式
	String today=sdf.format(d);
%>
<script type="text/javascript" charset="utf-8">

var controllername_zg= "${pageContext.request.contextPath }/zlaq/zgxxController.do";
var controllername_jc= "${pageContext.request.contextPath }/zlaq/jcxxController.do";
var id="<%=id%>";
var zt="<%=zt%>";
var obj,success;


//初始化加载
$(document).ready(function(){
	$("#ZJBS").attr("title",'');
	$("#AJBS").attr("title",'');	
	$("#jcfj_only").hide();
	var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
	obj=convertJson.string2json1(rowValue);	
	switch_checkbox(obj.JCLX,obj.ZJBS,obj.AJBS);
	deleteFileData(id,"","","");
	setFileData(id,obj.GC_JH_SJ_ID,obj.SJBH,obj.YWLX,"0");
	switch(zt)
	{
	case '0':
		$("#zgxx").hide();
		$("#zgbd").hide();
		$("#jctz_only").hide();
		$("#jcfj_only").hide();
		$("#XGRQ").attr("check-type","");
		$("#TZRQ").attr("check-type","");
		$("#CLJY").attr("check-type","");
		$("#example_go").attr("disabled",true);
		break;
	case '1':
		$("#zgxx").show();
		$("#zgbd").show();
		$("#jctz_only").hide();
		$("#jcfj_only").hide();
		$("#ZGBH").val($("#JCBH").val()+'ZG');
		$("#TZRQ").attr("check-type","required");
		$("#XGRQ").attr("check-type","required");
		$("#CLJY").attr("check-type","required");
		if($("#JCRQ").val()=='<%=today%>')
		{
			var json_fz='{"TZRQ":\"'+'<%=today%>'+'\","TZRQ_SV":\"'+'<%=today%>'+'\"}';		
			var obj_fz=convertJson.string2json1(json_fz);
			$("#demoForm").setFormValues(obj_fz);
		}
		break;
	case '5':	
		$("#example_go").attr("disabled",true);
		$("#delete").attr("disabled",true);
		break;
	default:
		$("#example_go").attr("disabled",true);
		$("#delete").attr("disabled",true);
		$("#example_save").attr("disabled",true);;
		break;
	}
	
	if(id!=null&&id!="null")
	{
		$("#demoForm").setFormValues(obj);
		queryFileData(id,"","","");
	}
	
	
    //校验发送日期是否大于当前日期
    $("#TZRQ").change(function() {
	});  	
 
    
    //校验限改日期是否大于发送日期
    $("#XGRQ").change(function() {
	});
    
    
    //监听检查日期
    $("#JCRQ").change(function() {
    });
    
    
    //监听检查类型
   $("#JCLX").change(function() {
   	bhlx();
   	if($("#ISCZWT").val()=='1')
   	{
   		$("#ZGBH").val($("#JCBH").val()+'ZG');
   	}	
   }); 
   
   
   //监听检查日期
  $("#JCRQ").change(function() {
		if($("#JCLX").val()=='')
		{
			xInfoMsg('请先填写检查类型');
			$("#JCLX").focus();
			$("#JCRQ").val('');
  		return;
	}	
  	bhlx();
   	if($("#ISCZWT").val()=='1')
  	{
  		$("#ZGBH").val($("#JCBH").val()+'ZG');
  	}	
  });  	
	
  //监听是否需要整改变化事件
  $("#ISCZWT").change(function() {
  	//1需要整改    0不需要整改
  	if($("#ISCZWT").val()=='1')
  	{
			if($("#JCLX").val()=='')
			{
				xInfoMsg('请先填写检查类型');
				$("#JCLX").focus();
				$("#ISCZWT").val('');
				return;	
			}
			else
			{
	    		$("#zgxx").show();
	    		$("#zgbd").show();
	    		$("#example_go").attr("disabled",false);
	    		$("#ZGBH").val($("#JCBH").val()+'ZG');
	    		$("#TZRQ").attr("check-type","required");
	    		$("#XGRQ").attr("check-type","required");
	    		$("#CLJY").attr("check-type","required");
	    		if($("#JCRQ").val()=='<%=today%>')
	    		{
	    			var json_fz='{"TZRQ":\"'+'<%=today%>'+'\","TZRQ_SV":\"'+'<%=today%>'+'\"}';		
	    			var obj_fz=convertJson.string2json1(json_fz);
	    			$("#demoForm").setFormValues(obj_fz);
	    		}	
			}	
  	}
  	else
  	{
  		$("#zgxx").hide();
  		$("#zgbd").hide();
  		$("#example_go").attr("disabled",true);
  		$("#TZRQ").val("");
  		$("#XGRQ").val("");
  		$("#CLJY").val("");
  		$("#ZGJY").val("");
  		$("#XGRQ").attr("check-type","");
  		$("#TZRQ").attr("check-type","");
  		$("#CLJY").attr("check-type","");
  	}	
  });
	
  
	//删除未发送的检查信息
	var btn=$("#delete");
	btn.click(function()
	{
		//生成json串
	 	var data = Form2Json.formToJSON(demoForm);
		var data1 = defaultJson.packSaveJson(data);
		xConfirm("信息提示","删除后数据无法恢复！确定要删除吗？");
		$('#ConfirmYesButton').unbind();
		$('#ConfirmYesButton').one("click",function(){ 
			defaultJson.doDeleteJson(controllername_jc+"?delete_jc",data1,null,'delete_close');
		});		
	}
	);
});


function delete_close()
{
	var parentmain=$(window).manhuaDialog.getParentObj();	
	parentmain.query();
	$(window).manhuaDialog.close();
}


//切换检查类型
function switch_checkbox(jclx,zjbs,ajbs)
{
	switch(jclx)
	{
		case '1':
			$("#ZJBS").prop("checked",true);
			$("#ZJBS").attr("disabled",true);
			$("#AJBS").prop("checked",false);
			$("#AJBS").attr("disabled",true);
			break;
		case '2':
			$("#ZJBS").prop("checked",false);
			$("#ZJBS").attr("disabled",true);
			$("#AJBS").prop("checked",true);
			$("#AJBS").attr("disabled",true);
			break;
		case '3':
			$("#ZJBS").prop("checked",true);
			$("#ZJBS").attr("disabled",true);
			$("#AJBS").prop("checked",false);
			$("#AJBS").attr("disabled",true);
			break;
		case '4':
			if(zjbs==1)
			{
				$("#ZJBS").prop("checked",true);
			}
			if(ajbs==1)
			{
				$("#AJBS").prop("checked",true);	
			} 
		case '5':
			if(zjbs==1)
			{
				$("#ZJBS").prop("checked",true);
			}
			if(ajbs==1)
			{
				$("#AJBS").prop("checked",true);	
			}	
			break;
	}
}
    

//选择编号类型
function bhlx()
{
	switch($("#JCLX").val())
	{
	case '1': 
		doQuery_jcbh(controllername_jc+"?query_jcbh&jclx=1&jcrq="+$("#JCRQ").val()+"&jc=ZL");
		$("#ZJBS").prop("checked",true);
		$("#ZJBS").attr("disabled",true);
		$("#AJBS").prop("checked",false);
		$("#AJBS").attr("disabled",true);
		break;
	case '2':	
		doQuery_jcbh(controllername_jc+"?query_jcbh&jclx=2&jcrq="+$("#JCRQ").val()+"&jc=AJ");
		$("#ZJBS").prop("checked",false);
		$("#ZJBS").attr("disabled",true);
		$("#AJBS").prop("checked",true);
		$("#AJBS").attr("disabled",true);
		break;
	case '3':	
		doQuery_jcbh(controllername_jc+"?query_jcbh&jclx=3&jcrq="+$("#JCRQ").val()+"&jc=JC");
		$("#ZJBS").prop("checked",true);
		$("#ZJBS").attr("disabled",true);
		$("#AJBS").prop("checked",false);
		$("#AJBS").attr("disabled",true);
		break;
	case '4':	
		doQuery_jcbh(controllername_jc+"?query_jcbh&jclx=4&jcrq="+$("#JCRQ").val()+"&jc=YS");
		$("#ZJBS").attr("disabled",false);
		$("#AJBS").attr("disabled",false);
		$("#AJBS").prop("checked",false);
		$("#ZJBS").prop("checked",false);
		break;
	case '5':	
		doQuery_jcbh(controllername_jc+"?query_jcbh&jclx=5&jcrq="+$("#JCRQ").val()+"&jc=ZH");
		$("#ZJBS").prop("checked",true);
		$("#AJBS").prop("checked",true);
		$("#ZJBS").attr("disabled",false);
		$("#AJBS").attr("disabled",false);
		break;
	}
}


//生成检查编号方法
doQuery_jcbh = function(actionName){
    var success  = true;
	$.ajax({
		url : actionName,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(result) {
			var returnMsg = result.msg;
			$("#JCBH").val(returnMsg);
			success = true;
		}
	});
	return success;	
};


//保存
$(function() {	
	var btn=$("#example_save");
	btn.click(function() {
	 	 if($("#demoForm").validationButton())
		{
			//可以添加提示信息	
		}else{
	 		xInfoMsg('请填写必填项！');
			return ;
		} 
	 	if(zt<2)
	 	{	
	    	if($("#ISCZWT").val()=='1')
	    	{
	    	}
	    	else
	    	{
	    		$("#jcfj").attr("notClear","true");
	    		clearFileTab();
	    	}
	 	}
		if($("#ZJBS").prop("checked")==true)
		{
			$("#ZJBS").val('1');
		}
		if($("#AJBS").prop("checked")==true)
		{
			$("#AJBS").val('1');
		}
 	 //生成json串
		var data=Form2Json.formToJSON(demoForm);
		//组成保存json串格式
		var data1=defaultJson.packSaveJson(data);
		//通过判断id是否为空来判断是插入还是修改
		//修改
 		switch(zt)
		{
			case '0':
				if($("#ISCZWT").val()==0)
				{
					zt='0';
				}
				else
				{
					zt='1';
				}	
				success=defaultJson.doInsertJson(controllername_jc + "?update_jc&flag=0&ywid="+$("#ywid").val(), data1,null,"update_save");
			break;
			case '1':
				if($("#ISCZWT").val()==0)
				{
					zt='0';
				}
				else
				{
					zt='1';
				}	
				success=defaultJson.doInsertJson(controllername_jc + "?update_jc&flag=0&ywid="+$("#ywid").val(), data1,null,"update_save");
			break;
			case '2':
				success=defaultJson.doInsertJson(controllername_zg + "?update_zg&ywid="+$("#ywid").val(), data1,null,"update_save");
 			break;
			case '5':
				success=defaultJson.doInsertJson(controllername_zg + "?update_fc&ywid="+$("#ywid").val(), data1,null,"update_save");
 			break;
			default:
		}
	});


	//发送
	var btn=$("#example_go");
	btn.click(function() {
	 	 if($("#demoForm").validationButton())
		{
		}else{
	 		xInfoMsg('请填写必填项！');
			return ;
		}   
		if($("#ISCZWT").val()=='1')
		{
		}
		if($("#ZJBS").prop("checked")==true)
		{
			$("#ZJBS").val('1');
		}
		if($("#AJBS").prop("checked")==true)
		{
			$("#AJBS").val('1');
		}
		xConfirm("信息提示","发送后检查和整改信息不可修改！确定要发送整改通知吗？");
    	$('#ConfirmYesButton').unbind();
		$('#ConfirmYesButton').one("click",function(){  
	
			//生成json串
			var data=Form2Json.formToJSON(demoForm);
			//组成保存json串格式
			var data1=defaultJson.packSaveJson(data);
			//通过判断id是否为空来判断是插入还是修改
			if(id==null||id=="null"||id=='undefined') 
			{
				//插入
				succss=defaultJson.doInsertJson(controllername_jc + "?insert_jc&ywid="+$("#ywid").val()+"&flag=1", data1,null,"insert_go");
			}
			else
			{
				//修改
			
				success=defaultJson.doInsertJson(controllername_jc + "?update_jc&flag=1&ywid="+$("#ywid").val(), data1,null,"update_go");
			}
		});
	});	
});


//保存更新回调函数
function update_save()
{
	if(success==true)
	{
		
		var obj=$("#resultXML").val();
		var resultmsgobj=convertJson.string2json1(obj);
		var subresultmsgobj=resultmsgobj.response.data[0];
		id=$(subresultmsgobj).attr("GC_ZLAQ_JCB_ID");
		document.getElementById('GC_ZLAQ_JCB_ID').value=id;
		document.getElementById('GC_ZLAQ_ZGB_ID').value=$(subresultmsgobj).attr("GC_ZLAQ_ZGB_ID");
		document.getElementById('YWLX').value=$(subresultmsgobj).attr("YWLX");
		$("#ywid").val("");
		var parentmain=$(window).manhuaDialog.getParentObj();			
		parentmain.update(obj);
	}	
}


//发送插入回调函数
function insert_go()
{
	if(success==true)
	{
		var obj=$("#resultXML").val();
		var resultmsgobj=convertJson.string2json1(obj);
		var subresultmsgobj=resultmsgobj.response.data[0];
		id=$(subresultmsgobj).attr("GC_ZLAQ_JCB_ID");
		document.getElementById('GC_ZLAQ_JCB_ID').value=id;
		document.getElementById('GC_ZLAQ_ZGB_ID').value=$(subresultmsgobj).attr("GC_ZLAQ_ZGB_ID");
		document.getElementById('YWLX').value=$(subresultmsgobj).attr("YWLX");
		$("#ywid").val("");
		var parentmain=$(window).manhuaDialog.getParentObj();			
		parentmain.add(obj);
 		disable(true);
		$("#jctj").hide();
		$("#jcfj").hide();
		$("#jcfj_only").show();
		$("#tztj").hide();
		$("#jctz").hide();
		$("#jctz_only").show();
		$("#example_go").attr("disabled",true);
		$("#delete").attr("disabled",true);
		$("#example_save").attr("disabled",true);
		disabledTh("disabledTh");
	}	
}


//发送更新回调函数
function update_go()
{
	if(success==true)
	{
		var obj=$("#resultXML").val();
		var parentmain=$(window).manhuaDialog.getParentObj();			
		parentmain.update(obj);
 		disable(true);
		$("#jctj").hide();
		$("#jcfj").hide();
		$("#jcfj_only").show();
		$("#tztj").hide();
		$("#jctz").hide();
		$("#jctz_only").show();
		$("#example_go").attr("disabled",true);
		$("#example_save").attr("disabled",true);
		$("#delete").attr("disabled",true);
		disabledTh("disabledTh");
	}	
}


//修改只读样式
function disabledTh(bool_ys)
{
	$("#BDMC_readonly").val($("#BDMC").val());
	$("#JCBM_readonly").val($("#JCBM").val());
	$("#BDMC_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	$("#JCLX_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	$("#JCRQ_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	$("#JCGM_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	$("#JCBM_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	$("#JCR_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	$("#JCDW_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	$("#JCNR_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	$("#CZWT_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	$("#JCFJ_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	$("#ISCZWT_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	$("#TZRQ_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	$("#XGRQ_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	$("#CLJY_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	$("#ZGFJ_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	if(bool_ys=='')
	{
		$("#div_read").show();
		$("#div_read_jcbm").show();
		$("#BDMC_readonly").hide();			
		$("#JCBM_readonly").hide();	
		$("#example_save").attr("disabled",false);
	}
	else
	{
		$("#div_read").hide();
		$("#div_read_jcbm").hide();
		$("#example_save").attr("disabled",true);
		$("#BDMC_readonly").show();
		$("#JCBM_readonly").show();	
	}	
}


//弹出项目列表
function xzxm() {
	queryProjectWithBD();
}


//控制编辑权限
function disable(bool)
{
	$("#JCBM").attr("disabled",bool);
	$("#JCR").attr("disabled",bool);
	$("#JCLX").attr("disabled",bool);
	$("#JCGM").attr("disabled",bool);
	$("#JCRQ").attr("disabled",bool);
	$("#JCDW").attr("disabled",bool);
	$("#JCNR").attr("disabled",bool);
	$("#CZWT").attr("disabled",bool);
	$("#ISCZWT").attr("disabled",bool);
	$("#TZRQ").attr("disabled",bool);
	$("#XGRQ").attr("disabled",bool);
	$("#CLJY").attr("disabled",bool);
}
</script>    
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	</p>
	<div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">	
				<span class="pull-right">
					<button id="example_save" class="btn" type="button">保存</button>
					<button id="example_go" class="btn" type="button">发送</button>
					<%-- <app:oPerm url="jsp/business/zlaq/jcwh.jsp?delete"> --%>
					<button class="btn" id="delete">删除</button>				
					<%-- </app:oPerm>		 --%>
				</span>    			      		
     		<form method="post" id="demoForm">
      			<%int isshow=Integer.parseInt(zt);
      				switch(isshow)
      				{
      					case 0:
      			%>	
	     			<h4 id="xmxx" class="title">项目信息</h4>
	      			<table class="B-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/xmxx_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="xmxx_readonly"/> 
				         </jsp:include>   			
					</table>
		      		<h4 class="title">检查信息</h4>
					<table class="B-table" width="100%">
				    	<jsp:include page="/jsp/business/zlaq/jc.jsp" flush="true">
				        	<jsp:param name="prefix" value="jc"/> 
				        </jsp:include>
				    </table> 
		      		<h4 id="zgxx" class="title">整改信息</h4>
					<table id="zgbd" class="B-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/tz.jsp" flush="true">
				         	<jsp:param name="prefix" value="tz"/> 
				         </jsp:include>   			
					</table>
      			<%		break;
      					case 1:
      			%>
	     			<h4 id="xmxx" class="title">项目信息</h4>
	      			<table class="B-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/xmxx_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="xmxx_readonly"/> 
				         </jsp:include>   			
					</table>
 		      		<h4 class="title">检查信息</h4>
					<table class="B-table" width="100%">
				    	<jsp:include page="/jsp/business/zlaq/jc.jsp" flush="true">
				        	<jsp:param name="prefix" value="jc"/> 
				        </jsp:include>
				    </table> 
		      		<h4 id="zgxx" class="title">整改信息</h4>
					<table id="zgbd" class="B-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/tz.jsp" flush="true">
				         	<jsp:param name="prefix" value="tz"/> 
			         	</jsp:include>	
			        </table>		
      			<%		break;
      					case 2:
      			%>
		      		<h4 id="zgxx" class="title">整改信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/tz_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="tz_readonly"/> 
			         	</jsp:include>	
			        </table>		
 		      		<h4 class="title">检查信息</h4>
					<table class="B-table c-table" width="100%">
				    	<jsp:include page="/jsp/business/zlaq/jc_readonly.jsp" flush="true">
				        	<jsp:param name="prefix" value="jc_readonly"/> 
				        </jsp:include>
				    </table> 
	     			<h4 id="xmxx" class="title">项目信息</h4>
	      			<table class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/xmxx_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="xmxx_readonly"/> 
				         </jsp:include>   			
					</table>
      			<%		break;
      					case 3:
      			%>
		      		<h4 id="zgxx" class="title">整改信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/tz_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="tz_readonly"/> 
			         	</jsp:include>	
			        </table>		
 		      		<h4 class="title">检查信息</h4>
					<table class="B-table c-table" width="100%">
				    	<jsp:include page="/jsp/business/zlaq/jc_readonly.jsp" flush="true">
				        	<jsp:param name="prefix" value="jc_readonly"/> 
				        </jsp:include>
				    </table> 
	     			<h4 id="xmxx" class="title">项目信息</h4>
	      			<table class="B-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/xmxx_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="xmxx_readonly"/> 
				         </jsp:include>   			
					</table>
      			<%		break;
      					case 4:
      			%>
		      		<h4 id="zgxx" class="title">回复信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/xmglgs/zlaqjc/hf_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="hf_readonly"/> 
				         </jsp:include>
			         </table>   			      				
		      		<h4 id="zgxx" class="title">整改信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/tz_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="tz"/> 
			         	</jsp:include>	
			        </table>		
 		      		<h4 class="title">检查信息</h4>
					<table class="B-table c-table" width="100%">
				    	<jsp:include page="/jsp/business/zlaq/jc_readonly.jsp" flush="true">
				        	<jsp:param name="prefix" value="jc_readonly"/> 
				        </jsp:include>
				    </table> 
	     			<h4 id="xmxx" class="title">项目信息</h4>
	      			<table class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/xmxx_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="xmxx_readonly"/> 
				         </jsp:include>   			
					</table>
      			<%		break;
      					case 5:
      			%>
 		      		<h4 id="zgxx" class="title">复查信息</h4>
					<table id="zgbd" class="B-table" width="100%">
	 					<jsp:include page="/jsp/business/zlaq/fc.jsp" flush="true">
				         	<jsp:param name="prefix" value="fc"/> 
				         </jsp:include>
			         </table>   			      				
		      		<h4 id="zgxx" class="title">回复信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/xmglgs/zlaqjc/hf_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="hf_readonly"/> 
				         </jsp:include>
			         </table>   			      				
		      		<h4 id="zgxx" class="title">整改信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/tz_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="tz"/> 
			         	</jsp:include>	
			        </table>		
 		      		<h4 class="title">检查信息</h4>
					<table class="B-table c-table" width="100%">
				    	<jsp:include page="/jsp/business/zlaq/jc_readonly.jsp" flush="true">
				        	<jsp:param name="prefix" value="jc_readonly"/> 
				        </jsp:include>
				    </table> 
	     			<h4 id="xmxx" class="title">项目信息</h4>
	      			<table class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/xmxx_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="xmxx_readonly"/> 
				         </jsp:include>   			
					</table>
     			<%} %>	
    		</form>	
		</div>
	</div>
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>     	
<div align="center">
	<FORM name="frmPost" method="post" id="frmPost" style="display: none"
		target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id="queryXML"/>
		 <input type="hidden" name="txtXML" id="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" fieldname="XMNF" id="txtFilter"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
	</FORM>
</div>
<script type="text/javascript">
//将列表中的数据赋到表单中
getWinData=function(data){
	var tempJson=convertJson.string2json1(data);
	document.getElementById('XDKID').value=tempJson.GC_TCJH_XMXDK_ID;
	document.getElementById('BDID').value=tempJson.BDBH;
	document.getElementById('ND').value=tempJson.JHND;
	$("#demoForm").setFormValues(tempJson);
};
</script>
</body>
</html>