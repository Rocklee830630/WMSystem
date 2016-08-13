<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<title>新增——修改——详细信息</title>
<%
	String id=request.getParameter("id");
	String xx=request.getParameter("xx");
	String flag=request.getParameter("flag");
 %>
<script type="text/javascript" charset="utf-8">

var controllername= "${pageContext.request.contextPath }/xmcbk/xmcbkwhController.do";
var id="<%=id%>";
var xmbh,success,xmbh_flag,flag='<%=flag%>';




//初始化加载
$(document).ready(function(){
	if(id==null||id=="null")
	{
		$(".icon-green").hide();
		json='{"XMSX":\"'+1+'\"}';
		var obj=convertJson.string2json1(json);
		$("#demoForm").setFormValues(obj);
		var year=new Date().getFullYear();
		var num=document.getElementsByTagName("option");
		var year_num=year-2004;
		document.getElementById('ND').options.length=year_num+1;
		for(var i=0;i<year_num+1;i++)
		{
			num[i].id=i;
			num[i].innerHTML=parseInt(2005)+i;
		}
		document.getElementById('ND').value=num[i-2].innerHTML;
		do_xmbh(year);
		$("#XMBH").val(xmbh);
		$('#ISBT').val('0');
		$('#ISNRTJ').val('1');
		
		 $("#ND").change(function() {
			 var year=$("#ND").val();
			 do_xmbh(year);
			 $("#XMBH").val(xmbh);
		 });
	}
	else
	{ 
		if(flag!=2)
		{
			$(".icon-green").hide();
		}	
		var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
		var odd=convertJson.string2json1(rowValue);
		var year=new Date().getFullYear();
		var num=document.getElementsByTagName("option");
		var year_num=year-2004;
		document.getElementById('ND').options.length=year_num+1;
		for(var i=0;i<year_num+1;i++)
		{
			num[i].id=i;
			num[i].innerHTML=parseInt(2005)+i;
		}
		document.getElementById('ND').value=odd.ND;
		//将数据放入表单
		$("#demoForm").setFormValues(odd);

	}	
	$("#ND").change(function() {
		 var year=$("#ND").val();
		 do_xmbh(year);
		 $("#XMBH").val(xmbh);
	 });
	  
	//删除未下达的项目信息
	var btn=$("#delete");
	btn.click(function()
	{
		//生成json串
	 	var data = Form2Json.formToJSON(demoForm);
		var data1 = defaultJson.packSaveJson(data);
		xConfirm("信息提示","删除后数据无法恢复！确定要删除吗？");
		$('#ConfirmYesButton').unbind();
		$('#ConfirmYesButton').one("click",function(){ 
			defaultJson.doDeleteJson(controllername+"?delete",data1,null,'delete_close');
		});		
	});
});


function delete_close()
{
	var parentmain=$(window).manhuaDialog.getParentObj();	
	parentmain.query();
	$(window).manhuaDialog.close();
}

//生成项目编号
function do_xmbh(year){
	var success=true;
	var data1=combineQuery.getQueryCombineData(queryForm,frmPost,null);
	var actionName=controllername+"?query_xmbh&year="+year;
	var data={
		msg : data1
	};
	$.ajax({
		url : actionName,
		data : data,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(result) {
		xmbh=result.msg;	
		success=true;
		}
	});
  return success;
}


//校验项目编号
function do_xmbh_jy(year,id){
	var success=true;
	var data1=combineQuery.getQueryCombineData(queryForm,frmPost,null);
	var actionName=controllername+"?query_xmbh_jy&year="+year+"&xmbh="+$("#XMBH").val()+"&id="+id;
	var data={
		msg : data1
	};
	$.ajax({
		url : actionName,
		data : data,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(result) {
		xmbh_flag=result.msg;	
		success=true;
		}
	});
return success;
}


//保存
$(function() {
	if(flag=="2"){
	//是已经下达后，只允许修改项目编码
		$("input").attr("disabled","true");
		$("textarea").attr("disabled","true");
		$("select").attr("disabled","true");
		$("#XMBM").removeAttr("disabled");
		$("#ISNRTJ").removeAttr("disabled");
		$("#XMFR").removeAttr("disabled");
		$("#ZRBM").removeAttr("disabled");
		$("#NDMB").removeAttr("disabled");
		//$("#ISAMBWC").removeAttr("disabled");
		//完成投资
	//	$("#WCGC").removeAttr("disabled");
	//	$("#WCZC").removeAttr("disabled");
	//	$("#WCQT").removeAttr("disabled");	
		//总体投资
		$("#ZTGC").removeAttr("disabled");
		$("#ZTZC").removeAttr("disabled");
		$("#ZTQT").removeAttr("disabled");	
		//计划投资
		$("#GC").removeAttr("disabled");
		$("#ZC").removeAttr("disabled");
		$("#QT").removeAttr("disabled");	
		$("input[type='checkbox']").each(function(){
			$(this).removeAttr("disabled");
		}); 
	}
	var btn=$("#example_save");
	btn.click(function() {
	 	 if($("#demoForm").validationButton())
		{
			//可以添加提示信息	
		}else{
			return ;
		}
		//通过判断id是否为空来判断是插入还是修改
		if(id==null||id=="null") 
		{
			do_xmbh_jy($("#ND").val(),0);
			if(xmbh_flag!=2)
			{
				do_xmbh($("#ND").val());
				$("#XMBH").val(xmbh);
				//生成json串
				var data=Form2Json.formToJSON(demoForm);
				//组成保存json串格式
				var data1=defaultJson.packSaveJson(data);
				//插入
				success=doInsertJson_xmbh(controllername + "?insert_cbk", data1,null,'insert');		
			}
			else
			{
				var data=Form2Json.formToJSON(demoForm);
				//组成保存json串格式
				var data1=defaultJson.packSaveJson(data);
				//插入
				success=defaultJson.doInsertJson(controllername + "?insert_cbk", data1,null,'insert');								
			}	
		}
		else
		{	
			do_xmbh_jy($("#ND").val(),$("#GC_TCJH_XMCBK_ID").val())
			if(xmbh_flag!=2)
			{
				do_xmbh($("#ND").val())
				var new_xmbh=Number(xmbh);
				xInfoMsg('项目编号重复，请重新设置编号，建议编号设成：'+new_xmbh);
				return;
			}	
			//生成json串
			var data=Form2Json.formToJSON(demoForm);
			//组成保存json串格式
			var data1=defaultJson.packSaveJson(data);
			//修改
			 success=defaultJson.doUpdateJson(controllername + "?update_ckb&id="+id, data1,null,"update");
		}	
	});
});


//插入
doInsertJson_xmbh = function(actionName, data1,tablistID,callbackFunction) {
    var success  = true;

	var isAsync = false;
	if (callbackFunction != undefined) {
		isAsync = true;
	}
	$.ajax({
		url : actionName,
		data : data1,
		dataType : 'json',
		async :	isAsync,
		type : 'post',
		//contentType:'application/json;charset=UTF-8',	    
		success : function(result) {
			//将返回值增加到TabList中,将返回的串转换为数组形式
			//alert(result.msg);
			//add by zhangbr@ccthanking.com BEGIN 
			//上传附件时，需要使用resultXML中的值，所以此处查询成功后，要将返回结果赋值给resultXML
			//如果页面不存在resultXML，附件上传时也有判断，不会报错
			//add by zhangbr@ccthanking.com END
			$("#resultXML").val(result.msg);
			
			if(tablistID != null)
			{
			clearLockTable(tablistID);
			var res = dealSpecialCharactor(result.msg);
			var subresultmsgobj = defaultJson.dealResultJson(res);
			var strarr = $("#"+tablistID.id).insertResult(JSON.stringify(subresultmsgobj),tablistID,1);
			modifyLockTable(tablistID);
			}
			var prompt = result.prompt;
 			if(!prompt){
 				prompt =g_prompt[0];
 			}
 			xInfoMsg('操作成功！项目编号重复，重置编号为：'+xmbh);
			defaultJson.clearTxtXML();

			success = true;
			if(isAsync==true){
			  eval(callbackFunction+"()");
			}
			},
	    error : function(result) {
			    defaultJson.clearTxtXML();
			    success = false;

		}
	});
	 return success;
};


//插入回调函数
function insert()
{
	if(success==true)
	{	
		var obj=$("#resultXML").val();	
		var resultmsgobj=convertJson.string2json1(obj);
		subresultmsgobj=resultmsgobj.response.data[0];
		$("#demoForm").setFormValues(subresultmsgobj);
		id=$(subresultmsgobj).attr("GC_TCJH_XMCBK_ID");
		document.getElementById('GC_TCJH_XMCBK_ID').value=id;
		document.getElementById('YWLX').value=$(subresultmsgobj).attr("YWLX");
		var parentmain=$(window).manhuaDialog.getParentObj();			
		parentmain.query_add();
	}	
}

/* 
//插入回调函数
function insert_xmbh()
{
	if(success==true)
	{	
		var obj=$("#resultXML").val();	
		var resultmsgobj=convertJson.string2json1(obj);
		subresultmsgobj=resultmsgobj.response.data[0];
		$("#demoForm").setFormValues(subresultmsgobj);
		id=$(subresultmsgobj).attr("GC_TCJH_XMCBK_ID");
		document.getElementById('GC_TCJH_XMCBK_ID').value=id;
		document.getElementById('YWLX').value=$(subresultmsgobj).attr("YWLX");
		var parentmain=$(window).manhuaDialog.getParentObj();			
		parentmain.add(obj);
	}	
}
 */


//更新回调函数
function update()
{
	if(success==true)
	{
		var obj=$("#resultXML").val();		
		var resultmsgobj=convertJson.string2json1(obj);
		var subresultmsgobj=resultmsgobj.response.data[0];
		var parentmain=$(window).manhuaDialog.getParentObj();	
		parentmain.update(obj);
		if(flag==0)
		{
		$(window).manhuaDialog.close();
		}	 
	}	
}

//继续新增
$(function() {	
	var btn=$("#example_copyadd");
	btn.click(function() {
		id=null;
		var json=$("#resultXML").val();	       	
		$("#demoForm").clearFormResult();
		$('#ISBT').val('0');
		$('#ISNRTJ').val('1');
        if(json){
			var subobj=defaultJson.dealResultJson(json);
			$("#QY").val(subobj.QY_SV);
			$("#QY").attr("code",subobj.QY);
			var json_fz='{"XMLX":\"'+subobj.XMLX+'\","ND":\"'+subobj.ND+'\","ISBT":\"'+subobj.ISBT+'\"}';
			var obj_fz=convertJson.string2json1(json_fz);
			$("#demoForm").setFormValues(obj_fz);
        }
		var year=$("#ND").val();
		do_xmbh(year);
		$("#XMBH").val(xmbh);
	});
});


//选择 区域
function addQy(){
	var o_qy="";
	if($("#QY").val()==""){
		o_qy="";
	}else{
		o_qy=$("#QY").attr("code");
	}
	$(window).manhuaDialog({"title":"区域选择","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/SelectQy.jsp?o_qy="+o_qy,"modal":"4"});
}


//弹出区域回调
getWinData=function(data){
	$("#QY").val(data.qyText);
	$("#QY").attr("code",data.qyValue);
};


//计算金额
function countHj(){
	var count = parseFloat($("#GC").val()==""?0:$("#GC").val())+parseFloat($("#ZC").val()==""?0:$("#ZC").val())+parseFloat($("#QT").val()==""?0:$("#QT").val());
	//var subCount = count.toFixed(4);
	$("#JHZTZE").val(count);
}

//计算金额
function countHj_ZT(){
	var count = parseFloat($("#ZTGC").val()==""?0:$("#ZTGC").val())+parseFloat($("#ZTZC").val()==""?0:$("#ZTZC").val())+parseFloat($("#ZTQT").val()==""?0:$("#ZTQT").val());
	//var subCount = count.toFixed(4);
	$("#ZTZTZE").val(count);
}
function countHj_WC(){
	var count = parseFloat($("#WCGC").val()==""?0:$("#WCGC").val())+parseFloat($("#WCZC").val()==""?0:$("#WCZC").val())+parseFloat($("#WCQT").val()==""?0:$("#WCQT").val());
	//var subCount = count.toFixed(4);
	$("#WCZTZE").val(count);
}
</script>    
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">
      		<h4 class="title">
      			项目信息
				<span class="pull-right">
					<button id="example_save" class="btn" type="button">保存</button>
					<%if(id==null){ %>
					<button id="example_copyadd" class="btn" type="button">新增</button>			  
					<%} else if(id!=null && (flag=="0" || "0".equals(flag))){%>
					<button id="delete" class="btn" type="button">删除</button>	
					<%} %>
				</span>    			
      		</h4>
     		<form method="post" id="demoForm">
      			<table class="B-table" width="100%" id="DT1">
      				<input type="hidden" id="YWLX" fieldname="YWLX" name="YWLX"/>
      				<input type="hidden" id="ISXD" fieldname="ISXD" name="ISXD" keep="true" value="0"/>
      				<input type="hidden" id="GC_TCJH_XMCBK_ID" fieldname="GC_TCJH_XMCBK_ID" name="GC_TCJH_XMCBK_ID"/>
					<tr>
						<%if(id!=null && (flag=="0" || "0".equals(flag))){%>
						本项目尚未下达
						<%}else if(id!=null && (flag=="2" || "2".equals(flag))){%>
						本项目已经下达，只允许修改被<i class="icon-green "></i>标记项
						<%} %>
						<th width="8%" class="right-border bottom-border text-right disabledTh">项目编号</th>
						<td width="17%" class="right-border bottom-border">
							<input id="XMBH" class="span12" type="text"  check-type="maxlength" name="XMBH"  maxlength="36" fieldname="XMBH" readonly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">项目名称</th>
						<td width="42%" colspan="3" class="right-border bottom-border">
							<input id="XMMC" class="span12" type="text" placeholder="必填" check-type="required maxlength" name="XMMC"  maxlength="500" fieldname="XMMC"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">年度</th>
						<td width="17%" class="right-border bottom-border">
							<select class="span12 year" type="text" id="ND" placeholder="必填" check-type="required" keep="true"  fieldname="ND" name="ND"></select>
						</td>	
					</tr>
					<tr>							
						<th width="8%"  class="right-border bottom-border text-right">项目类型</th>
						<td width="17%" class="right-border bottom-border">
							<select class="span12 4characters" kind="dic" src="XMLX" id="XMLX" fieldname="XMLX" placeholder="必填" check-type="required" name="XMLX"></select>
						</td>
				        <th width="8%"  class="right-border bottom-border text-right">所属城区</th>
	        			<td width="42%" colspan="3"  class="right-border bottom-border">
				        	<input class="span12" style="width:94%" id="QY" type="text" placeholder="必填" check-type="required" name="QY" fieldname="QY" readonly/>
				        	&nbsp;
				        	<a href="javascript:void(0)"><i class="icon-edit" title="点击选择区域" onclick="addQy();"></i></a>
	       				</td>
						<th width="8%"  class="right-border bottom-border text-right">是否BT</th>
						<td width="17%" class="right-border bottom-border">
							<select class="span12 3characters" id="ISBT" kind="dic" src="SF" placeholder="必填" check-type="required" fieldname="ISBT" name="ISBT"></select>
						</td>
					</tr>
					<tr>							
						<th class="right-border bottom-border text-right">项目地址</th>
						<td colspan="7" class="right-border bottom-border">
							<input class="span12" type="text"  placeholder="必填" check-type="required maxlength" id="XMDZ"  maxlength="500" fieldname="XMDZ" name="XMDZ"/>
						</td>
					</tr>
					<tr>							
						<th class="right-border bottom-border text-right"><i class="icon-green "></i>责任部门</th>
						<td  class="right-border bottom-border" colspan="7">
							<!-- <select class="span12" name="ZRBM" fieldname="ZRBM"  kind="dic" src="ZRBM"  defaultMemo="请选择">
							</select> -->
							<input class="span12" id="ZRBM" type="checkbox" kind="dic" src="ZRBM"  name="ZRBM" fieldname="ZRBM">
						</td>
					</tr>
					<tr>
						<th class="right-border bottom-border text-right"><i class="icon-green "></i>项目法人</th>
						<td  class="right-border bottom-border">
							<select class="span12" id="XMFR" name="XMFR" fieldname="XMFR"  kind="dic" src="XMFR"  defaultMemo="请选择"></select>
						</td>
						<th class="right-border bottom-border text-right"><i class="icon-green "></i>是否纳入统计</th>
						<td  class="right-border bottom-border">
							<select class="span12 3characters" id="ISNRTJ" kind="dic" src="SF" placeholder="必填" check-type="required" fieldname="ISNRTJ" name="ISNRTJ"></select>
						</td>	
						<th class="right-border bottom-border text-right"><i class="icon-green "></i>年度目标</th>
						<td  class="right-border bottom-border">
							<select class="span12 3characters" id="NDMB" name="NDMB" fieldname="NDMB"  kind="dic" src="NDMB"  defaultMemo="请选择"></select>
						</td>
						<th class="right-border bottom-border text-right">是否按目标完成</th>
						<td  class="right-border bottom-border">
							<select class="span12 3characters" id="ISAMBWC" name="ISAMBWC" fieldname="ISAMBWC"  kind="dic" src="SF"  defaultMemo="请选择"></select>
						</td>			
					</tr>
					<tr>
						<th width="8%"  class="right-border bottom-border text-right"><i class="icon-green "></i>项目编码</th>
						<td width="92%" colspan=7 class="right-border bottom-border">
							<input type="text" class="span9" id="XMBM" placeholder="必填" check-type="required maxlength" maxlength="100" fieldname="XMBM" name="XMBM">
							<span style="font-size:10px;">(如果立项编码为空，请以“XXXXX”代替)</span>
						</td>
					</tr>
					<tr>
						<th class="right-border bottom-border text-left disabledTh"  colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;<i class="icon-green "></i>项目总体投资</th>
					</tr>
 					<tr>
						<th class="right-border bottom-border text-right" width="8%">工程</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj_ZT()" min="0" placeholder="" style="width:65%;text-align:right;" id="ZTGC" fieldname="ZTGC" name="ZTGC"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>							
						<th class="right-border bottom-border text-right" width="8%">征拆</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj_ZT()" min="0" placeholder="" style="width:65%;text-align:right;" id="ZTZC" fieldname="ZTZC" name="ZTZC"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>
						<th class="right-border bottom-border text-right" width="8%">其他</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj_ZT()" min="0" placeholder="" style="width:65%;text-align:right;" id="ZTQT" fieldname="ZTQT" name="ZTQT"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>
						<th class="right-border bottom-border text-right disabledTh" width="8%">总投资额</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" min="0" placeholder="" min="0" style="width:65%;text-align:right;" id="ZTZTZE" fieldname="ZTZTZE" name="ZTZTZE" readonly/>&nbsp;&nbsp;<b>(万元)</b>
						</td>							
					</tr>
					<tr>
						<th class="right-border bottom-border text-left disabledTh"  colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;<i class="icon-green "></i>年度计划投资</th>
					</tr>
 					<tr>
						<th class="right-border bottom-border text-right" width="8%">工程</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj()" min="0" placeholder="" style="width:65%;text-align:right;" id="GC" fieldname="GC" name="GC"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>							
						<th class="right-border bottom-border text-right" width="8%">征拆</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj()" min="0" placeholder="" style="width:65%;text-align:right;" id="ZC" fieldname="ZC" name="ZC"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>
						<th class="right-border bottom-border text-right" width="8%">其他</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj()" min="0" placeholder="" style="width:65%;text-align:right;" id="QT" fieldname="QT" name="QT"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>
						<th class="right-border bottom-border text-right disabledTh" width="8%">总投资额</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" min="0" placeholder="" min="0" style="width:65%;text-align:right;" id="JHZTZE" fieldname="JHZTZE" name="JHZTZE" readonly/>&nbsp;&nbsp;<b>(万元)</b>
						</td>							
					</tr>
					<tr>
						<th class="right-border bottom-border text-left disabledTh"  colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;年度完成投资</th>
					</tr>
 					<tr>
						<th class="right-border bottom-border text-right" width="8%">工程</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj_WC()" min="0" placeholder="" style="width:65%;text-align:right;" id="WCGC" fieldname="WCGC" name="WCGC"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>							
						<th class="right-border bottom-border text-right" width="8%">征拆</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj_WC()" min="0" placeholder="" style="width:65%;text-align:right;" id="WCZC" fieldname="WCZC" name="WCZC"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>
						<th class="right-border bottom-border text-right" width="8%">其他</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj_WC()" min="0" placeholder="" style="width:65%;text-align:right;" id="WCQT" fieldname="WCQT" name="WCQT"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>
						<th class="right-border bottom-border text-right disabledTh" width="8%">总投资额</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" min="0" placeholder="" min="0" style="width:65%;text-align:right;" id="WCZTZE" fieldname="WCZTZE" name="WCZTZE" readonly/>&nbsp;&nbsp;<b>(万元)</b>
						</td>							
					</tr>		
					<tr>
						<th class="right-border bottom-border text-right">建设任务</th>
						<td colspan="7" class="bottom-border">
							<textarea class="span12" rows="2" id="JSRW" check-type="maxlength" maxlength="500" fieldname="JSRW" name="JSRW"></textarea>
						</td>
					</tr>
					<tr>
						<th class="right-border bottom-border text-right">建设内容</th>
						<td colspan="7" class="bottom-border">
							<textarea class="span12" rows="2" id="JSNR" check-type="maxlength" maxlength="500" fieldname="JSNR" name="JSNR"></textarea>
						</td>
					</tr>
					<tr>
						<th class="right-border bottom-border text-right">建设意义</th>
						<td colspan="7" class="bottom-border">
							<textarea class="span12" rows="2" id="JSYY" maxlength="500" check-type="maxlength" fieldname="JSYY" name="JSYY"></textarea>
						</td>
					</tr>
					<tr>
						<th class="right-border bottom-border text-right">备注</th>
						<td colspan="7" class="bottom-border">
							<textarea class="span12" rows="2" id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ"></textarea>
						</td>
					</tr>
     			</table>
      		</form>
    	</div>
	</div>
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id="frmPost">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id="queryXML"/>
		<input type="hidden" name="txtXML" id="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" fieldname="ND" id="txtFilter"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<input type="hidden" id="queryResult"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>		
	</FORM>
</div>
<form method="post" id="queryForm">
	<!--可以再此处加入hidden域作为过滤条件 -->
	<TR style="display: none;">
		<TD class="right-border bottom-border"></TD>
		<TD class="right-border bottom-border">
			<INPUT type="hidden" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
		</TD>
	</TR>	
</form>
</body>
</html>