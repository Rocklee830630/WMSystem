<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<app:base/>
<title>整改回复</title>
<%
	String zgbid=request.getParameter("zgbid");
	String jcbid=request.getParameter("jcbid");
	String zt=request.getParameter("zt");
	Date d=new Date();//获取时间
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//转换格式
	String today=sdf.format(d);
%>
<script type="text/javascript" charset="utf-8">
var controllername_jc= "${pageContext.request.contextPath }/zlaq/jcxxController.do";
var controllername_zg= "${pageContext.request.contextPath }/zlaq/zgxxController.do";
var success,zgbid="<%=zgbid%>";
var jcbid="<%=jcbid%>";
var zt="<%=zt%>";

//初始化加载
$(document).ready(function(){
	$("#ZJBS").attr("title",'');
	$("#AJBS").attr("title",'');
	$("#jchf_only").hide();
	var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
	obj=convertJson.string2json1(rowValue);
	if(obj.ZJBS==1)
	{
		$("#ZJBS").prop("checked",true);
	}
	if(obj.AJBS==1)
	{
		$("#AJBS").prop("checked",true);	
	}	
	deleteFileData(jcbid,"","","");
	setFileData(jcbid,obj.GC_JH_SJ_ID,obj.SJBH,obj.YWLX,"0");	
	if(jcbid!=null&&jcbid!="null")
	{
		var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
		var obj=convertJson.string2json1(rowValue);
		$("#demoForm").setFormValues(obj);
		$("#HFBH").val($("#JCBH").val()+'HF');
		queryFileData(jcbid,"","","");
	}
	if($("#HFRQ").val()=='')
	{
		var json_fz='{"HFRQ":\"'+'<%=today%>'+'\","HFRQ_SV":\"'+'<%=today%>'+'\"}';		
		var obj_fz=convertJson.string2json1(json_fz);
		$("#demoForm").setFormValues(obj_fz);		
	}	
	
	/* //校验限改日期是否大于发送日期
    $("#HFRQ").change(function() {
    	if($("#HFRQ").val()<$("#TZRQ").val())
    	{
    		xInfoMsg('回复日期不能小于发送日期！');
    	}
    	if($("#HFRQ").val()>$("#XGRQ").val())
    	{
    		xInfoMsg('回复日期不能大于限改日期！');
    	}
	});  */ 	
});

 
 $(function() {	
	//保存
	var btn=$("#example_save");
	btn.click(function() {
	 	 if($("#demoForm").validationButton())
		{
			//可以添加提示信息	
		}else{
			return ;
		}   
		if($("#ZJBS").prop("checked")==true)
		{
			$("#ZJBS").val('1');
		}
		if($("#AJBS").prop("checked")==true)
		{
			$("#AJBS").val('1');
		}
       	/* if($("#HFRQ").val()<$("#TZRQ").val())
       	{
       		xInfoMsg('回复日期不能小于发送日期！');
       		$("#HFRQ").focus();
       		return;
       	} */
		//生成json串
		var data=Form2Json.formToJSON(demoForm);
		//组成保存json串格式
		var data1=defaultJson.packSaveJson(data);
		//通过判断id是否为空来判断是插入还是修改
		if(zt==2)
		{
			//插入
			success=defaultJson.doInsertJson(controllername_zg + "?insert_hf&ywid="+$("#ywid").val()+"&flag=0", data1,null,"insert_save");
		}	
		else
		{
			//修改
			success=defaultJson.doInsertJson(controllername_zg + "?update_hf&ywid="+$("#ywid").val()+"&flag=0", data1,null,"update_save");
		}
 	});	
	
	
	//提交
	var btn=$("#example_go");
	btn.click(function() {
	 	 if($("#demoForm").validationButton())
		{
			//可以添加提示信息	
		}else{
			return ;
		}   
		if($("#ZJBS").prop("checked")==true)
		{
			$("#ZJBS").val('1');
		}
		if($("#AJBS").prop("checked")==true)
		{
			$("#AJBS").val('1');
		}
       /* 	if($("#HFRQ").val()<$("#TZRQ").val())
       	{
       		xInfoMsg('回复日期不能小于发送日期！');
       		$("#HFRQ").focus();
       		return;
       	} */
    	xConfirm("信息提示","提交后回复信息不可修改！确定要提交吗？");
    	$('#ConfirmYesButton').unbind();
		$('#ConfirmYesButton').one("click",function(){  
			//生成json串
			var data=Form2Json.formToJSON(demoForm);
			//组成保存json串格式
			var data1=defaultJson.packSaveJson(data);
			//通过判断id是否为空来判断是插入还是修改
			if(zt==2)
			{
				//插入
				success=defaultJson.doInsertJson(controllername_zg + "?insert_hf&ywid="+$("#ywid").val()+"&flag=1", data1,null,"insert_go");
			}	
			else
			{
				//修改
				success=defaultJson.doInsertJson(controllername_zg + "?update_hf&ywid="+$("#ywid").val()+"&flag=1", data1,null,"update_go");
			}
		});
 	});	
});


//保存插入回调函数
function insert_save()
{
	if(success==true)
	{		
		var obj=$("#resultXML").val();
		var resultmsgobj=convertJson.string2json1(obj);
		var subresultmsgobj=resultmsgobj.response.data[0];
		zgbid=$(subresultmsgobj).attr("GC_ZLAQ_ZGB_ID");
		zt=$(subresultmsgobj).attr("ZT");
		document.getElementById('GC_ZLAQ_ZGB_ID').value=zgbid;
		document.getElementById('GC_ZLAQ_JCB_ID').value=$(subresultmsgobj).attr("GC_ZLAQ_JCB_ID");
		document.getElementById('YWLX').value=$(subresultmsgobj).attr("YWLX");
		zt=3
		$("#ywid").val("");
		var parentmain=$(window).manhuaDialog.getParentObj();			
		parentmain.update(obj);	
	}	
}


//保存更新回调函数
function update_save()
{
	if(success==true)
	{
		var obj=$("#resultXML").val();
		var parentmain=$(window).manhuaDialog.getParentObj();			
		parentmain.update(obj);
	}	
}


//提交插入回调函数
function insert_go()
{
	if(success==true)
	{
		var obj=$("#resultXML").val();
		var resultmsgobj=convertJson.string2json1(obj);
		var subresultmsgobj=resultmsgobj.response.data[0];
		zgbid=$(subresultmsgobj).attr("GC_ZLAQ_ZGB_ID");
		document.getElementById('GC_ZLAQ_ZGB_ID').value=zgbid;
		document.getElementById('GC_ZLAQ_JCB_ID').value=$(subresultmsgobj).attr("GC_ZLAQ_JCB_ID");
		document.getElementById('YWLX').value=$(subresultmsgobj).attr("YWLX");
		$("#ywid").val("");
		zt=4
		var parentmain=$(window).manhuaDialog.getParentObj();			
		parentmain.update(obj);	
		disable(true);
		//$("#example_go").hide();
		//$("#example_save").hide();
		$("#example_go").attr("disabled",true);
		$("#example_save").attr("disabled",true);
		$("#hftj").hide();
		$("#jchf").hide();
		$("#jchf_only").show();
		disabledTh("disabledTh");
	}	
}


//提交更新回调函数
function update_go()
{
	if(success==true)
	{
		var obj=$("#resultXML").val();
		zt='4'
		var parentmain=$(window).manhuaDialog.getParentObj();			
		parentmain.update(obj);
		disable(true);
		$("#example_go").attr("disabled",true);
		$("#example_save").attr("disabled",true);
		//$("#example_go").hide();
		//$("#example_save").hide();
		$("#hftj").hide();
		$("#jchf").hide();
		$("#jchf_only").show();
		disabledTh("disabledTh");
	}	
}


//修改只读样式
function disabledTh(bool_ys)
{
	$("#HFRQ_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	$("#HFNR_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	$("#HFFJ_TH").attr("class","right-border bottom-border text-right "+bool_ys);
	if(bool_ys=='')
	{
		$("#div_read").show();
		$("#BDMC_readonly").hide();	
		//$("#example_save").show();
		$("#example_save").attr("disabled",false);
	}
	else
	{
		$("#div_read").hide();
		//$("#example_save").hide();
		$("#BDMC_readonly").show();
		$("#example_save").attr("disabled",true);
	}	
}

 
//控制编辑权限
 function disable(bool)
 {
 	$("#HFRQ").attr("disabled",bool);
 	$("#HFNR").attr("disabled",bool);
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
				<button id="example_go" class="btn" type="button">提交</button>
			</span>    			      		
     		<form method="post" id="demoForm">
      			<table class="B-table c-table" width="100%" id="DT1">
		      		<h4 id="zgxx" class="title">回复信息</h4>
					<table id="hfbd" class="B-table" width="100%">
				         <jsp:include page="/jsp/business/xmglgs/zlaqjc/hf.jsp" flush="true">
				         	<jsp:param name="prefix" value="hf"/> 
				         </jsp:include>
			   		</table>    		           			      				
		      		<h4 id="zgxx" class="title">整改信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/tz_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="tz_readonly"/> 
				         </jsp:include> 
			         </table>  			
		      		<h4 id="zgxx" class="title">检查信息</h4>
					<table id="jcbd" class="B-table c-table" width="100%">
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
				</table>
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
</body>
</html>