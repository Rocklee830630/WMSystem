<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<app:base/>
<title>下达计划</title>
<%
	String flag=request.getParameter("flag");
	Date d=new Date();//获取时间
	SimpleDateFormat n=new SimpleDateFormat("yyyy");//转换格式
	String year=n.format(d);
	int year_num=Integer.parseInt(year);
%>
<script type="text/javascript" charset="utf-8">

//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xmcbk/xmcbkxdController.do";
var year="<%=year%>";
var pch,xdlx=1,success;


//页面初始化
$(function() {
	init();
 	//按钮绑定事件(下达统筹计划)项目
	$("#btnSave").click(function() {	
		if(!$("#jhForm").validationButton()){
			return;
		}
		var ywid = $("#ywid").val();
		var formData = Form2Json.formToJSON(jhForm);
		var formData1 = defaultJson.packSaveJson(formData);
		if('<%=flag%>'==1)
		{
			success=defaultJson.doInsertJson(controllername+"?insert_pc&ywid="+$("#ywid").val(),formData1,null,"insert_close");		
		}
		else
		{
			success=defaultJson.doUpdateJson(controllername+"?update_pc&ywid="+$("#ywid").val(),formData1,null,"update_close");
		}	
	});
});


//插入回调函数
function insert_close()
{
	if(success==true)
	{	

		var obj=$("#resultXML").val();	
		var parentmain=$(window).manhuaDialog.getParentObj();			
		parentmain.query_add(obj);
	}	
	$(window).manhuaDialog.close();
}


//更新回调函数
function update_close()
{
	if(success==true)
	{	
		var obj=$("#resultXML").val();	
		var parentmain=$(window).manhuaDialog.getParentObj();			
		parentmain.query_update(obj);
	}	
	$(window).manhuaDialog.close();
}


//页面默认参数
function init(){
	if('<%=flag%>'==1)
	{
		$("input[name=XDLX]").each(function()
	     {
		  	if(this.value=='1')
	    	{
			  this.checked=true;
	    	}
		});
		maxpch(year);
	 	var json_fz='{"PCH":\"'+pch+'\"}';
		var obj_fz=convertJson.string2json1(json_fz);
		$("#jhForm").setFormValues(obj_fz);
	}
	else
	{
		var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
		var odd=convertJson.string2json1(rowValue);
		if(odd.XDLX==2)
		{
			$("#PCH").attr("src","YJPCH");
			reloadSelectTableDic($("#PCH"));
		}	
		$("#jhForm").setFormValues(odd);
		deleteFileData(odd.GC_CBK_PCB_ID,"","","");
		setFileData(odd.GC_CBK_PCB_ID,"",odd.SJBH,"010002","0");
		queryFileData(odd.GC_CBK_PCB_ID,"","","");

	}	
		
}


//控制批次号变化
function test(e)
{
	if(e.value==2)
	{
		xdlx=2;
		$("#PCH").attr("src","YJPCH");
		reloadSelectTableDic($("#PCH"));
		maxpch(year.substring(0,4));
		var json_fz='{"PCH":\"'+pch+'\"}';
		var obj_fz=convertJson.string2json1(json_fz);
		$("#jhForm").setFormValues(obj_fz);
	}
	else
	{
		xdlx=1;
		$("#PCH").attr("src","PCH");
		reloadSelectTableDic($("#PCH"));
		maxpch(year.substring(0,4));
	 	var json_fz='{"PCH":\"'+pch+'\"}';
		var obj_fz=convertJson.string2json1(json_fz);
		$("#jhForm").setFormValues(obj_fz);
	}	
}
//查询出最大pch
function maxpch(xdnf){
	var success=true;
	var data1=combineQuery.getQueryCombineData(queryForm,frmPost,null);
	var actionName=controllername+"?query_maxpch&xdlx="+xdlx+"&xdnf="+year;
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
		pch=result.msg;	
		success=true;
		}
	});
    return success;
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	 <div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">
			<h4 class="title">批次信息
				<span class="pull-right">
					<button id="btnSave" class="btn" type="button">保存</button>
				</span>
			</h4>
		    <form method="post" id="jhForm">
		    	 <input type="hidden" id="GC_CBK_PCB_ID" fieldname="GC_CBK_PCB_ID">
		    	 <input type="hidden" id="SJBH" fieldname="SJBH">
		    	 <input type="hidden" id="SPLX" fieldname="SPLX" value="1">
				<table class="B-table">
	      			<tr>
	         			<th width="10%" class="right-border bottom-border text-right">下达类型</th>
	         			<td width="40%" class=" bottom-border text-left"><input class="span12" id="XDLX" type="radio" onclick="test(this)" placeholder="" kind="dic" src="E#1=正常下达:2=应急下达" name= "XDLX" fieldname="XDLX">
	        		    </td>
						<th width="10%" id="PCHTH" class="right-border bottom-border text-right">下达批次</th>
						<td width="40" id="PCHTD" class="right-border bottom-border">
							<select class="span12 3characters" id="PCH" name="PCH" fieldname="PCH"  check-type="required" kind="dic" src="PCH"></select>
						</td>
	        		</tr>	     
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right">附件信息</th>
			        	<td colspan="3" class="bottom-border right-border">
							<div>
								<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0030">
									<i class="icon-plus"></i>
									<span>添加文件...</span>
								</span>
								<table role="presentation" class="table table-striped">
									<tbody fjlb="0030" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
								</table>
							</div>
						</td>
			        </tr>	     
   				</table>
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
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "PCH,XMBH"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="ywid" id = "ywid" value="">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
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