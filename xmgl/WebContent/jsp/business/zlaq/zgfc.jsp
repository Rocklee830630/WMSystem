<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<app:base/>
<title>整改复查</title>
<%

	String jcbid=request.getParameter("jcbid");
	String zt=request.getParameter("zt");
	Date d=new Date();//获取时间
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//转换格式
	String today=sdf.format(d);
%>
<script type="text/javascript" charset="utf-8">

var controllername_zg= "${pageContext.request.contextPath }/zlaq/zgxxController.do";
var controllername_jc= "${pageContext.request.contextPath }/zlaq/jcxxController.do";
var success,obj;
var jcbid="<%=jcbid%>";
var zt="<%=zt%>";
//初始化加载
$(document).ready(function(){
	$("#ZJBS").attr("title",'');
	$("#AJBS").attr("title",'');
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
		$("#demoForm").setFormValues(obj);
		$("#FCBH").val($("#JCBH").val()+'FC');
		queryFileData(jcbid,"","","");
	}
	if($("#FCRQ").val()=='')
	{
		var json_fz='{"FCRQ":\"'+'<%=today%>'+'\","FCRQ_SV":\"'+'<%=today%>'+'\"}';		
		var obj_fz=convertJson.string2json1(json_fz);
		$("#demoForm").setFormValues(obj_fz);		
	}		
	
	/* //校验限改日期是否大于发送日期
    $("#FCRQ").change(function() {
    	if($("#FCRQ").val()<$("#HFRQ").val())
    	{
    		xInfoMsg('复查日期不能小于回复日期！');
    	}
	}); */  	
});


//控制编辑权限
function disable(bool)
{
	$("#FCR").attr("disabled",bool);
	$("#FCRQ").attr("disabled",bool);
	$("#FCYJ").attr("disabled",bool);
	$("#FCJL").attr("disabled",bool);
}



$(function() {	
	//复查
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
      	/* if($("#FCRQ").val()<$("#HFRQ").val())
      	{
      		xInfoMsg('回复日期不能小于回复日期！');
      		$("#FCRQ").focus();
      		return;
      	} */
		//生成json串
		var data=Form2Json.formToJSON(demoForm);
		//组成保存json串格式
		var data1=defaultJson.packSaveJson(data);
		//通过判断id是否为空来判断是插入还是修改
		if(zt==4)
		{
			//插入
			success=defaultJson.doInsertJson(controllername_zg + "?insert_fc&ywid="+$("#ywid").val(), data1,null,"insert_save");
		}	
		else
		{
			//修改
			success=defaultJson.doInsertJson(controllername_zg + "?update_fc&ywid="+$("#ywid").val(), data1,null,"update_save");
		}
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
		document.getElementById('GC_ZLAQ_ZGB_ID').value=$(subresultmsgobj).attr("GC_ZLAQ_ZGB_ID");;
		document.getElementById('GC_ZLAQ_JCB_ID').value=$(subresultmsgobj).attr("GC_ZLAQ_JCB_ID");
		document.getElementById('YWLX').value=$(subresultmsgobj).attr("YWLX");
		$("#ywid").val("");
		zt='5';
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
</script>    
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	</p>
	<div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">
			<span class="pull-right">
				<button id="example_go" class="btn" type="button">保存</button>
			</span>    			      		
      		<form method="post" id="demoForm">	      	
				<h4 id="zgxx" class="title">复查信息</h4>   			      				
				<table id="hfbd" class="B-table" width="100%">
		        <jsp:include page="/jsp/business/zlaq/fc.jsp" flush="true">
		         	<jsp:param name="prefix" value="fc"/> 
		        </jsp:include> 
		    	</table>       			      				
	      		<h4 id="zgxx" class="title">回复信息</h4>
				<table id="hfbd" class="B-table c-table" width="100%">
			         <jsp:include page="/jsp/business/xmglgs/zlaqjc/hf_readonly.jsp" flush="true">
			         	<jsp:param name="prefix" value="hf_readonly"/> 
			         </jsp:include>
			     </table>    
	      		<h4 id="zgxx" class="title">整改信息</h4>
				<table id="hfbd" class="B-table c-table" width="100%">
			         <jsp:include page="/jsp/business/zlaq/tz_readonly.jsp" flush="true">
			         	<jsp:param name="prefix" value="tz_readonly"/> 
			         </jsp:include> 
			     </table>      			
	      		<h4 id="zgxx" class="title">检查信息</h4>
				<table id="hfbd" class="B-table c-table" width="100%">
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