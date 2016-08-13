<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<style type="text/css">
	.TDtitle{
			font-weight: bold; 
			font-size: 15px;
	}
    .divkd{
    margin-left: 0px;
    }
</style>
<title>形象进度管理-形象进度计划编制</title>
<%
	//获取计划编制ID
	String jhbzid = request.getParameter("jhbzid");
	//获取计划反馈ID
	String jhfkid = request.getParameter("jhfkid");
	if(Pub.empty(jhfkid)){
		jhfkid = "";
	}
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String userid = user.getAccount();
	String username = user.getName();
	String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xxjd/xxjdController.do";
//页面初始化
$(function() {
	init();
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {		
		if($("#jhbzForm").validationButton())
		{
			var data = Form2Json.formToJSON(jhbzForm);
    		var xmlxselect='';
			$("input:checkbox[id!=XMLX][ name=XMLX]").each(function(){ 			 
				var xmlx=$(this).val();
				if($(this).prop("checked")==true)
				{
					xmlxselect+=xmlx+','
				}	
			}); 
			xmlxselect=xmlxselect.substring(0,xmlxselect.length-1);
    		$(window).manhuaDialog.getParentObj().getWinData_jhbz(data,xmlxselect);
   			$(window).manhuaDialog.close(); 
		   
		}else{
			requireFormMsg();
		  	return;
		}
	});
	
	//切换类型	
	$("input:checkbox").click(function() {
		 $("input:checkbox[id!=XMLX][ name=XMLX]").each(function(){ 			 
			var xmlx =$(this).val();
			switch_lx($(this).prop("checked"),xmlx);
		}); 		
	});
	
	
	//按钮绑定事件（删除）
    $("#btnDel").click(function(){
	 	var data = Form2Json.formToJSON(jhbzForm);
		var data1 = defaultJson.packSaveJson(data);
		xConfirm("提示信息","是否确认删除！");
		$('#ConfirmYesButton').unbind();
		$('#ConfirmYesButton').one("click",function(){  
			var data = Form2Json.formToJSON(jhbzForm);
			$(window).manhuaDialog.getParentObj().getWinData_del(data);
   			$(window).manhuaDialog.close(); 
		});
    });
	
});
function switch_lx(bool,xmlx)
{
	if(bool)
	{
		switch(xmlx)
		{
			case'1':
				$("#DLGC").show();
			break;
			case'2':
				$("#PSGC").show();
			break;
			case'3':
				$("#QLGC").show();
			break;
			case'4':
				$("#KGGC").show();
			break;
		}
	}
	else
	{
		switch(xmlx)
		{
			case'1':
				$("#DLGC").hide();
			break;
			case'2':
				$("#PSGC").hide();
			break;
			case'3':
				$("#QLGC").hide();
			break;
			case'4':
				$("#KGGC").hide();
			break;
		}
	}	
	
}
//页面默认参数
function init(){
	var jhbzid = '<%=jhbzid%>';
	if(jhbzid != ""){
		var data = null;
		submit(controllername+"?queryJhbzByJhid&jhbzid="+jhbzid,data,jhbzList);
		
	}else{
		setFromDate();
		$("#btnDel").attr("disabled","disabled");
	}
	var pwindow =$(window).manhuaDialog.getParentObj();
	var rowValue = pwindow.document.getElementById("resultXML").value;
	var tempJson = convertJson.string2json1(rowValue);
	if(tempJson.ZT_FK == "1"){
		$("#btnDel").attr("disabled","disabled");
	}
	var xmlxall=$("#XXJDXMLX").val();
	var xmlxs=xmlxall.split(",");
	var rowValue = pwindow.document.getElementById("resultXML").value;
	var tempJson = convertJson.string2json1(rowValue);
	$("input:checkbox[id!=XMLX][ name=XMLX]").each(function(){  
		var xmlx=$(this).val();
		if(xmlxs[0]=="")//如果形象进度中项目类型为空
		{		
			if(xmlx==tempJson.XMLX)
			{
				$(this).prop("checked",true);
				switch_lx($(this).prop("checked"),xmlx);
			}	
		}
		else
		{
			for(var i=0;i<xmlxs.length;i++)
			{
				if(xmlx==xmlxs[i])
				{
					$(this).prop("checked",true);
					switch_lx($(this).prop("checked"),xmlx);
				}	
			}	
		}	
	}); 
}
function submit(actionName, data,tablistID){
	$.ajax({
		type : 'post',
		url : actionName,
		data : data,
		cache : false,
		dataType : "json",  
		async :	false,
		success : function(result) {
			if(result.msg != 0){
				var formObj = convertJson.string2json1(result.msg).response.data[0];
				$("#jhbzForm").setFormValues(formObj);
				$("#BDMC").val(formObj.BDID_SV);
				$("#resultXML").val(JSON.stringify(formObj));
				$("#XXJDXMLX").val(formObj.XMLX);
			}
			
		}
	});
}


//回调值
function setFromDate(){
	var pwindow =$(window).manhuaDialog.getParentObj();
	var rowValue = pwindow.document.getElementById("resultXML").value;
	var tempJson = convertJson.string2json1(rowValue);
	$("#jhbzForm").setFormValues(tempJson);
	$("#JHSJID").val(tempJson.GC_JH_SJ_ID);
	$("#XMID").val(tempJson.XMID);
	$("#BDID").val(tempJson.BDID);
	$("#XMBH").val(tempJson.XMBH);
	$("#ND").val(tempJson.ND);
	$("#XXJDID").val(tempJson.GC_XMGLGS_XXJD_ID);
}


</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">项目统筹计划信息
      	<span class="pull-right">
	  		<button id="btnSave" class="btn" type="button">保存</button>
	  		<!-- <button id="btnTj" class="btn" type="button">提交</button> -->
	  		<button id="btnDel" class="btn" type="button">删除</button>
      	</span>
      </h4>
     <form method="post" id="jhbzForm"  >
      <table class="B-table" width="100%" id="jhbzList">
        <input type="hidden" id="GC_XMGLGS_XXJD_JHBZ_ID" fieldname="GC_XMGLGS_XXJD_JHBZ_ID" name = "GC_XMGLGS_XXJD_JHBZ_ID"/></TD>
	  	<input type="hidden" id="XXJDID" fieldname="XXJDID" name = "XXJDID"/></TD>
	  	<input type="hidden" id="JHSJID" fieldname="JHSJID" name = "JHSJID"/></TD>
	  	<input type="hidden" id="XMID" fieldname="XMID" name = "XMID"/></TD>
	  	<input type="hidden" id="BDID" fieldname="BDID" name = "BDID"/></TD>
	  	<input type="hidden" id="ND" fieldname="ND" name = "ND"/></TD>
	  	<input type="hidden" id="XMBH" fieldname="XMBH" name = "XMBH"/></TD>
	  	<input type="hidden" id="JHKFID" fieldname="JHKFID" name = "JHKFID"/></TD>
	  	<input type="hidden" id="XXJDXMLX" /></TD>
	  	<input type="hidden" id="ZT_BZ"  fieldname="ZT_BZ" value="0"/></TD>
	  	
        <tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
       	 	<td class="bottom-border right-border" colspan="3">
         		<input class="span12" id="XMMC" type="text" fieldname="XMMC" name = "XMMC" keep="true"  readonly />
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
       		<td class="bottom-border right-border">
         		<input class="span12" id="BDMC" type="text" fieldname="BDMC" name = "BDMC" keep="true" readonly />
         	</td>
        </tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right disabledTh">业主代表</th>
       		<td class="bottom-border right-border" >
         		<input class="span12" id="YZDB"  type="text"  fieldname="YZDB" name = "YZDB" readonly/>
         	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">监理单位</th>
       		<td class="bottom-border right-border">
         		<input class="span12" id="JLDW" type="text" fieldname="JLDW" name = "JLDW" keep="true" readonly />
         	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">施工单位</th>
       		<td class="bottom-border right-border">
         		<input class="span12" id="SGDW" type="text" fieldname="SGDW" name = "SGDW" keep="true" readonly />
         	</td>
         </tr>
         <tr>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">年度目标</th>
       		<td class="bottom-border right-border">
         		<input class="span12"  id="JSMB" type="text" fieldname="JSMB" name = "JSMB" keep="true" readonly />
         	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">计划开工时间</th>
       		<td class="bottom-border right-border">
         		<input class="span12"  id="KGSJ" type="text" fieldname="KGSJ" name = "KGSJ" keep="true" readonly />
         	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">计划完工时间</th>
       		<td class="bottom-border right-border">
         		<input class="span12"  id="WGSJ" type="text" fieldname="WGSJ" name = "WGSJ" keep="true" readonly />
         	</td>
        </tr>
        <tr>
        	<th width="8%" class="right-border bottom-border text-right disabledTh">项目类型</th>
        	<td class="bottom-border right-border">
		    	<input class="span12" type="checkbox" placeholder="" id="XMLX" name="XMLX" kind="dic" keep="true" src="T#FS_DIC_TREE:DIC_CODE:DIC_VALUE:PARENT_ID=1000000000010 and dic_code<5" >
         	</td>
         	<td colspan="8"></td>
        </tr>
      </table>
      <div class="span12 divkd" style="display: none;margin-left: 0px;" id="DLGC">
      <h4 id="xxjdjhbz" class="title" style="background-color:gray;">道路</h4>
        <table class="B-table span6 " style="margin-left: 0px;">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">开工 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划开工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="DLJHKGSJ"  name = "DLJHKGSJ" fieldname="DLJHKGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际开工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="DLSJKGSJ"  name = "DLSJKGSJ" fieldname="DLSJKGSJ">
	          </td>
        	</tr>
        		<th width="5%" class="right-border bottom-border">开工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="DLKGYWYY"  name = "DLKGYWYY" fieldname="DLKGYWYY"></textarea>
	          </td>
		</table>
        <table class="B-table span6 ">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">完工 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="DLJHWGSJ"  name = "DLJHWGSJ" fieldname="DLJHWGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="DLSJWGSJ"  name = "DLSJWGSJ" fieldname="DLSJWGSJ">
	          </td>
        	</tr>
        		<th width="5%" class="right-border bottom-border">完工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="DLWGYWYY"  name = "DLWGYWYY" fieldname="DLWGYWYY"></textarea>
	          </td>
		</table>        
		<table class="B-table span6 " style="margin-left: 0px;">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">土基 </td>
        </tr>
        <tr>
	          <th width="5%" class="right-border bottom-border">计划开工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="TJJHKGSJ"  name = "TJJHKGSJ" fieldname="TJJHKGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际开工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="TJSJKGSJ"  name = "TJSJKGSJ" fieldname="TJSJKGSJ">
	          </td>
        	</tr>
        	<tr>
        		<th width="5%" class="right-border bottom-border">开工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="TJKGYWYY"  name = "TJKGYWYY" fieldname="TJKGYWYY"></textarea>
	          </td>
          </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="TJJSWCSJ"  name = "TJJSWCSJ" fieldname="TJJSWCSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="TJSJWCSJ"  name = "TJSJWCSJ" fieldname="TJSJWCSJ">
	          </td>
        	</tr>
        	<tr>
        		<th width="5%" class="right-border bottom-border">工期延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="TJYWYY"  name = "TJYWYY" fieldname="TJYWYY"></textarea>
	          </td>
	          </tr>
		</table>
      <table class="B-table span6" >
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"   colspan="4">基层 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划开工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="JCJHKGSJ"  name = "JCJHKGSJ" fieldname="JCJHKGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际开工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="JCSJKGSJ"  name = "JCSJKGSJ" fieldname="JCSJKGSJ">
	          </td>
        	</tr><tr>
        		<th width="5%" class="right-border bottom-border">开工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="JCKGYWYY"  name = "JCKGYWYY" fieldname="JCKGYWYY"></textarea>
	          </td>
	          </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="JCJSWCSJ"  name = "JCJSWCSJ" fieldname="JCJSWCSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="JCSJWCSJ"  name = "JCSJWCSJ" fieldname="JCSJWCSJ">
	          </td>
        	</tr><tr>
        		<th width="5%" class="right-border bottom-border">工期延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="JCYWYY"  name = "JCYWYY" fieldname="JCYWYY"></textarea>
	          </td></tr>
		</table>
      <table class="B-table span6" style="margin-left: 0px;">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">面层 </td>
        </tr>
        <tr>
	          <th width="5%" class="right-border bottom-border">计划开工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="MCJHKGSJ"  name = "MCJHKGSJ" fieldname="MCJHKGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际开工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="MCSJKGSJ"  name = "MCSJKGSJ" fieldname="MCSJKGSJ">
	          </td>
        	</tr><tr>
        		<th width="5%" class="right-border bottom-border">开工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="MCKGYWYY"  name = "MCKGYWYY" fieldname="MCKGYWYY"></textarea>
	          </td></tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="MCJSWCSJ"  name = "MCJSWCSJ" fieldname="MCJSWCSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="MCSJWCSJ"  name = "MCSJWCSJ" fieldname="MCSJWCSJ">
	          </td>
        	</tr><tr>
        		<th width="5%" class="right-border bottom-border">工期延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="MCYWYY"  name = "MCYWYY" fieldname="MCYWYY"></textarea>
	          </td></tr>
		</table>
	   	<table class="B-table span6" >
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">方砖 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划开工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="FZJHKGSJ"  name = "FZJHKGSJ" fieldname="FZJHKGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际开工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="FZSJKGSJ"  name = "FZSJKGSJ" fieldname="FZSJKGSJ">
	          </td>
        	</tr><tr>
        		<th width="5%" class="right-border bottom-border">开工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="FZKGYWYY"  name = "FZKGYWYY" fieldname="FZKGYWYY"></textarea>
	          </td></tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="FZJSWCSJ"  name = "FZJSWCSJ" fieldname="FZJSWCSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="FZSJWCSJ"  name = "FZSJWCSJ" fieldname="FZSJWCSJ">
	          </td>
        	</tr><tr>
        		<th width="5%" class="right-border bottom-border">工期延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="FZYWYY"  name = "FZYWYY" fieldname="FZYWYY"></textarea>
	          </td></tr>
		</table>
		</div>
		<div class="span12"  id="PSGC" style="display: none;margin-left: 0px;">
		<h4 id="xxjdjhbz" class="title span12" style="background-color:gray;">排水</h4>
        <table class="B-table span6 " style="margin-left: 0px;">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">开工 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划开工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="PSJHKGSJ"  name = "PSJHKGSJ" fieldname="PSJHKGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际开工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="PSSJKGSJ"  name = "PSSJKGSJ" fieldname="PSSJKGSJ">
	          </td>
        	</tr><tr>
        		<th width="5%" class="right-border bottom-border">开工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="PSKGYWYY"  name = "PSKGYWYY" fieldname="PSKGYWYY"></textarea>
	          </td></tr>
		</table>
        <table class="B-table span6 ">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">完工 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="PSJHWGSJ"  name = "PSJHWGSJ" fieldname="PSJHWGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="PSSJWGSJ"  name = "PSSJWGSJ" fieldname="PSSJWGSJ">
	          </td>
        	</tr>
        		<th width="5%" class="right-border bottom-border">完工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="PSWGYWYY"  name = "PSWGYWYY" fieldname="PSWGYWYY"></textarea>
	          </td>
		</table>  
		</div>
		<div class="span12"  id="QLGC" style="display: none;margin-left: 0px;">
		<h4 id="xxjdjhbz" class="title span12" style="background-color:gray;">桥梁</h4>
        <table class="B-table span6 " style="margin-left: 0px;">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">开工 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划开工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="QLJHKGSJ"  name = "QLJHKGSJ" fieldname="QLJHKGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际开工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="QLSJKGSJ"  name = "QLSJKGSJ" fieldname="QLSJKGSJ">
	          </td>
        	</tr>
        		<th width="5%" class="right-border bottom-border">开工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="QLKGYWYY"  name = "QLKGYWYY" fieldname="QLKGYWYY"></textarea>
	          </td>
		</table>
        <table class="B-table span6 ">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">完工 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="QLJHWGSJ"  name = "QLJHWGSJ" fieldname="QLJHWGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="QLSJWGSJ"  name = "QLSJWGSJ" fieldname="QLSJWGSJ">
	          </td>
        	</tr>
        		<th width="5%" class="right-border bottom-border">完工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="QLWGYWYY"  name = "QLWGYWYY" fieldname="QLWGYWYY"></textarea>
	          </td>
		</table>  
		<table class="B-table span6" style="margin-left: 0px;">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">桩基 </td>
        </tr>
           	<tr>
	          <th width="5%" class="right-border bottom-border">计划开工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="ZJJHKGSJ"  name = "ZJJHKGSJ" fieldname="ZJJHKGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际开工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="ZJSJKGSJ"  name = "ZJSJKGSJ" fieldname="ZJSJKGSJ">
	          </td>
	       	</tr>
	       	<tr>
	       		<th width="5%" class="right-border bottom-border">开工延误原因</th>
	       	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="ZJKGYWYY"  name = "ZJKGYWYY" fieldname="ZJKGYWYY"></textarea>
	          </td>
          </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="ZJCJSWCSJ"  name = "ZJCJSWCSJ" fieldname="ZJCJSWCSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="ZJCSJWCSJ"  name = "ZJCSJWCSJ" fieldname="ZJCSJWCSJ">
	          </td>
        	</tr>
        	<tr>
        		<th width="5%" class="right-border bottom-border">工期延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="ZJCYWYY"  name = "ZJCYWYY" fieldname="ZJCYWYY"></textarea>
	          </td>
	          </tr>
		</table>
      <table class="B-table span6" >
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"   colspan="4">承台及墩柱 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划开工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="CTJJZJHKGSJ"  name = "CTJJZJHKGSJ" fieldname="CTJJZJHKGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际开工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="CTJJZSJKGSJ"  name = "CTJJZSJKGSJ" fieldname="CTJJZSJKGSJ">
	          </td>
        	</tr>
        	<tr>
        		<th width="5%" class="right-border bottom-border">开工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="CTJJZKGYWYY"  name = "CTJJZKGYWYY" fieldname="CTJJZKGYWYY"></textarea>
	          </td>
	          </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="DZJSWCSJ"  name = "DZJSWCSJ" fieldname="DZJSWCSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="DZSJWCSJ"  name = "DZSJWCSJ" fieldname="DZSJWCSJ">
	          </td>
        	</tr>
        	<tr>
        		<th width="5%" class="right-border bottom-border">工期延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="DZYWYY"  name = "DZYWYY" fieldname="DZYWYY"></textarea>
	          </td>
	          </tr>
		</table>
      <table class="B-table span6" style="margin-left: 0px;">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">桥梁上部结构 </td>
        </tr>
         	<tr>
	          <th width="5%" class="right-border bottom-border">计划开工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="QLSBJGJHKGSJ"  name = "QLSBJGJHKGSJ" fieldname="QLSBJGJHKGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际开工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="QLSBJGSJKGSJ"  name = "QLSBJGSJKGSJ" fieldname="QLSBJGSJKGSJ">
	          </td>
        	</tr>
        	<tr>
        		<th width="5%" class="right-border bottom-border">开工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="QLSBJGKGYWYY"  name = "QLSBJGKGYWYY" fieldname="QLSBJGKGYWYY"></textarea>
	          </td>
	          </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="QLSBJSWCSJ"  name = "QLSBJSWCSJ" fieldname="QLSBJSWCSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="QLSBSJWCSJ"  name = "QLSBSJWCSJ" fieldname="QLSBSJWCSJ">
	          </td>
        	</tr>
        	<tr>
        		<th width="5%" class="right-border bottom-border">工期延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="QLSBYWYY"  name = "QLSBYWYY" fieldname="QLSBYWYY"></textarea>
	          </td>
	          </tr>
		</table>
	   	<table class="B-table span6" >
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">桥梁附属 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划开工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="QLFSJHKGSJ"  name = "QLFSJHKGSJ" fieldname="QLFSJHKGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际开工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="QLFSSJKGSJ"  name = "QLFSSJKGSJ" fieldname="QLFSSJKGSJ">
	          </td>
        	</tr>
        	<tr>
        		<th width="5%" class="right-border bottom-border">开工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="QLFSKGYWYY"  name = "QLFSKGYWYY" fieldname="QLFSKGYWYY"></textarea>
	          </td>
	          </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="QLFSJSWCSJ"  name = "QLFSJSWCSJ" fieldname="QLFSJSWCSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="QLFSSJWCSJ"  name = "QLFSSJWCSJ" fieldname="QLFSSJWCSJ">
	          </td>
        	</tr>
        	<tr>
        		<th width="5%" class="right-border bottom-border">工期延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="QLFSYWYY"  name = "QLFSYWYY" fieldname="QLFSYWYY"></textarea>
	          </td>
	          </tr>
		</table>
		</div>
		<div class="span12" id="KGGC" style="display: none;margin-left: 0px;">
		<h4 id="xxjdjhbz" class="title span12" style="background-color:gray;">框构</h4>
         <table class="B-table span6 " style="margin-left: 0px;">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">开工 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划开工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="KGJHKGSJ"  name = "KGJHKGSJ" fieldname="KGJHKGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际开工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="KGSJKGSJ"  name = "KGSJKGSJ" fieldname="KGSJKGSJ">
	          </td>
        	</tr>
        	<tr>
        		<th width="5%" class="right-border bottom-border">开工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="KGKGYWYY"  name = "KGKGYWYY" fieldname="KGKGYWYY"></textarea>
	          </td>
	          </tr>
		</table>
        <table class="B-table span6 ">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">完工 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="KGJHWGSJ"  name = "KGJHWGSJ" fieldname="KGJHWGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="KGSJWGSJ"  name = "KGSJWGSJ" fieldname="KGSJWGSJ">
	          </td>
        	</tr>
        		<th width="5%" class="right-border bottom-border">完工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="KGWGYWYY"  name = "KGWGYWYY" fieldname="KGWGYWYY"></textarea>
	          </td>
		</table>  
        <table class="B-table span6" style="margin-left: 0px;">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">土方 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划开工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="TFJHKGSJ"  name = "TFJHKGSJ" fieldname="TFJHKGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际开工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="TFSJKGSJ"  name = "TFSJKGSJ" fieldname="TFSJKGSJ">
	          </td>
        	</tr>
        	<tr>
        		<th width="5%" class="right-border bottom-border">开工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="TFKGYWYY"  name = "TFKGYWYY" fieldname="TFKGYWYY"></textarea>
	          </td>
	          </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="TFJSWCSJ"  name = "TFJSWCSJ" fieldname="TFJSWCSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="TFSJWCSJ"  name = "TFSJWCSJ" fieldname="TFSJWCSJ">
	          </td>
        	</tr>
        	<tr>
        		<th width="5%" class="right-border bottom-border">工期延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="TFYWYY"  name = "TFYWYY" fieldname="TFYWYY"></textarea>
	          </td>
	          </tr>
		</table>
      <table class="B-table span6" >
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"   colspan="4">结构 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划开工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="JGJHKGSJ"  name = "JGJHKGSJ" fieldname="JGJHKGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际开工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="JGSJKGSJ"  name = "JGSJKGSJ" fieldname="JGSJKGSJ">
	          </td>
        	</tr><tr>
        		<th width="5%" class="right-border bottom-border">开工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="JGKGYWYY"  name = "JGKGYWYY" fieldname="JGKGYWYY"></textarea>
	          </td>
	          </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="JGJSWCSJ"  name = "JGJSWCSJ" fieldname="JGJSWCSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="JGSJWCSJ"  name = "JGSJWCSJ" fieldname="JGSJWCSJ">
	          </td>
        	</tr><tr>
        		<th width="5%" class="right-border bottom-border">工期延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="JGYWYY"  name = "JGYWYY" fieldname="JGYWYY"></textarea>
	          </td>
	          </tr>
		</table>
      <table class="B-table span6 divkd" style="margin-left: 0px;">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">附属 </td>
        </tr>
         	<tr>
	          <th width="5%" class="right-border bottom-border">计划开工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="FSJHKGSJ"  name = "FSJHKGSJ" fieldname="FSJHKGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际开工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="FSSJKGSJ"  name = "FSSJKGSJ" fieldname="FSSJKGSJ">
	          </td>
        	</tr>
        	<tr>
        		<th width="5%" class="right-border bottom-border">开工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="FSKGYWYY"  name = "FSKGYWYY" fieldname="FSKGYWYY"></textarea>
	          </td>
	          </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="FSJSWCSJ"  name = "FSJSWCSJ" fieldname="FSJSWCSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="FSSJWCSJ"  name = "FSSJWCSJ" fieldname="FSSJWCSJ">
	          </td>
        	</tr>
        	<tr>
        		<th width="5%" class="right-border bottom-border">工期延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="FSYWYY"  name = "FSYWYY" fieldname="FSYWYY"></textarea>
	          </td>
	          </tr>
		</table>
		</div>
		<!-- 
		<div class="span12" id="KGGC" style="display: none;margin-left: 0px;">
		<h4 id="xxjdjhbz" class="title span12">框构工程</h4>
         <table class="B-table span6 " style="margin-left: 0px;">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">开工 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划开工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="KGJHKGSJ"  name = "KGJHKGSJ" fieldname="KGJHKGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际开工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="KGSJKGSJ"  name = "KGSJKGSJ" fieldname="KGSJKGSJ">
	          </td>
        	</tr>
        		<th width="5%" class="right-border bottom-border">开工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="KGKGYWYY"  name = "KGKGYWYY" fieldname="KGKGYWYY"></textarea>
	          </td>
		</table>
        <table class="B-table span6 ">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">完工 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="KGJHWGSJ"  name = "KGJHWGSJ" fieldname="KGJHWGSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="KGSJWGSJ"  name = "KGSJWGSJ" fieldname="KGSJWGSJ">
	          </td>
        	</tr>
        		<th width="5%" class="right-border bottom-border">完工延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="KGKGYWYY"  name = "KGWGYWYY" fieldname="KGWGYWYY"></textarea>
	          </td>
		</table>  
        <table class="B-table span6" style="margin-left: 0px;">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">土方 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="TFJSWCSJ"  name = "TFJSWCSJ" fieldname="TFJSWCSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="TFSJWCSJ"  name = "TFSJWCSJ" fieldname="TFSJWCSJ">
	          </td>
        	</tr>
        		<th width="5%" class="right-border bottom-border">工期延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="TFYWYY"  name = "TFYWYY" fieldname="TFYWYY"></textarea>
	          </td>
		</table>
      <table class="B-table span6" >
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"   colspan="4">结构 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="JGJSWCSJ"  name = "JGJSWCSJ" fieldname="JGJSWCSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="JGSJWCSJ"  name = "JGSJWCSJ" fieldname="JGSJWCSJ">
	          </td>
        	</tr>
        		<th width="5%" class="right-border bottom-border">工期延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="JGYWYY"  name = "JGYWYY" fieldname="JGYWYY"></textarea>
	          </td>
		</table>
      <table class="B-table span6 divkd" style="margin-left: 0px;">
        <tr>
        <td width="5%" class="right-border bottom-border text-left TDtitle"  colspan="4">附属 </td>
        </tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">计划完工时间</th>
	          <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="FSJSWCSJ"  name = "FSJSWCSJ" fieldname="FSJSWCSJ">
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	           <td class="right-border bottom-border" >
	           <input class="span12 date"  type="date"  id="FSSJWCSJ"  name = "FSSJWCSJ" fieldname="FSSJWCSJ">
	          </td>
        	</tr>
        		<th width="5%" class="right-border bottom-border">工期延误原因</th>
        	  <td class="right-border bottom-border"  colspan="3">
	           <textarea class="span12 date"    id="FSYWYY"  name = "FSYWYY" fieldname="FSYWYY"></textarea>
	          </td>
		</table>
		</div> -->
      </form>
    </div>
   </div>
  </div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "XMBH,LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>

</html>