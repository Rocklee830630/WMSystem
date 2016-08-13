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
	String ids = request.getParameter("ids");
	String sjxmsxs = request.getParameter("sjxmsxs");
	String spxxid = request.getParameter("spxxid");
	String nd = request.getParameter("nd");
	//String xdnf = request.getParameter("xdnf");
	Date d=new Date();//获取时间
	SimpleDateFormat n=new SimpleDateFormat("yyyy");//转换格式
	String year=n.format(d);
	int year_num=Integer.parseInt(year);
	SimpleDateFormat nyr=new SimpleDateFormat("yyyy-MM-dd");//转换格式
	String xdrq=nyr.format(d);
%>
<script type="text/javascript" charset="utf-8">

//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xmcbk/xmcbkxdController.do";
var ids = "<%=ids%>";
var nd = '<%=nd%>';
var pch,xdlx=1,xdnf,year,success_flag;
var sjxmsxs="<%=sjxmsxs%>";

//页面初始化
$(function() {
	init();
	//按钮绑定事件(下达统筹计划)项目
	$("#btnSave").click(function() {
		var isxd=false;
		//选择的批次中城建表中的项目属性为新增的，不可以下达为年初批次。
		$("input[name=PCLX]").each(function(){
			if(this.checked==true){
				var xdlx_val=this.value;
				if (xdlx_val == '1') {
					var array =sjxmsxs.split(",");
					for(var i=0;i<array.length;i++){
						if(array[i]=='2'){
							isxd=true;
						}
					}
				} 
			}
		});
		if(isxd){
			xInfoMsg("项目属性为新增，不可以下达为年初批次!");
			return;
		}
		if(xdlx==2)
		{
	    	var count = getFileCounts("0030");
	    	if(count<=0){
				xInfoMsg("请添加附件!");
		    	return;
	    	}
		}	
		if(!$("#jhForm").validationButton()){
			return;
		}
		var ywid = $("#ywid").val();
		var formData = Form2Json.formToJSON(jhForm);
		var jsonobj = convertJson.string2json1(formData);
		jsonobj["ids"] = ids;
		var jsonstring = JSON.stringify(jsonobj);
		var formData1 = defaultJson.packSaveJson(jsonstring);
		doInsertJson_xd(controllername + "?insert_xd&ywid="+ywid+"&nd="+nd+"&spxxid=<%=spxxid%>", formData1,null,"insert");
 		 	
	});
	
	//监听下达日期的变化
	$("#XDRQ").change(function() {	
		year=parseInt($("#XDRQ").val().substring(0,4));
	 	maxpch($("#XDRQ").val().substring(0,4));
		var json_fz='{"PCH":\"'+pch+'\","XDRQ":\"'+$("#XDRQ").val()+'\","XDRQ_SV":\"'+$("#XDRQ").val()+'\"}';		
		var obj_fz=convertJson.string2json1(json_fz);
		$("#jhForm").setFormValues(obj_fz);
 	});
});


//插入
doInsertJson_xd = function(actionName, data1,tablistID,callbackFunction) {
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
		success : function(result) {
			$("#resultXML").val(result.msg);
			success_flag=result.msg;
			var prompt = result.prompt;
			if(!prompt){
				prompt =g_prompt[0];
			}
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
	if(success_flag!=1)
	{
		var parentmain=$(window).manhuaDialog.getParentObj();	
		parentmain.insert_xd();
		$(window).manhuaDialog.close();
	}	
	else
	{
		var parentmain=$(window).manhuaDialog.getParentObj();			
		parentmain.nosuccess();
		$(window).manhuaDialog.close();
	}	
}

//页面默认参数
function init(){
	$("input[name=XDLX]").each(function()
		{
			if(this.value=='1')
			{
				this.checked=true;
			}
		});
		//maxpch(xdrq);
		$("#XDRQ").val('<%=xdrq%>');
		maxpch(nd);
		//var newXdrq = xdrq.replace(xdrq.substring(0, 4),nd);
		//var json_fz='{"PCH":\"'+pch+'\","XDRQ":\"'+xdrq+'\","XDRQ_SV":\"'+xdrq+'\"}';
		var json_fz='{"PCH":\"'+pch+'\","XDRQ":\"'+$("#XDRQ").val()+'\","XDRQ_SV":\"'+$("#XDRQ").val()+'\"}';
		var obj_fz=convertJson.string2json1(json_fz);
		$("#jhForm").setFormValues(obj_fz);
}



//控制批次号变化
function test(e)
{
	if(e.value==2)
	{
		xdlx=2;
		$("#XDWHTH").hide();
		$("#XDWHTD").hide();	
		$("#switch").show();
		$("#XDWH").attr("check-type","");
		$("#PCH").attr("src","YJPCH");
		reloadSelectTableDic($("#PCH"));
		maxpch($("#XDRQ").val().substring(0,4));
		var json_fz='{"PCH":\"'+pch+'\","XDRQ":\"'+$("#XDRQ").val()+'\","XDRQ_SV":\"'+$("#XDRQ").val()+'\"}';
		var obj_fz=convertJson.string2json1(json_fz);
		$("#jhForm").setFormValues(obj_fz);
	}
	else
	{
		xdlx=1;
		$("#XDWHTH").show();
		$("#XDWHTD").show();
		$("#switch").hide();
		$("#XDWH").attr("check-type","required maxlength");
		$("#PCH").attr("src","PCH");
		reloadSelectTableDic($("#PCH"));
		maxpch($("#XDRQ").val().substring(0,4));
		var json_fz='{"PCH":\"'+pch+'\","XDRQ":\"'+$("#XDRQ").val()+'\","XDRQ_SV":\"'+$("#XDRQ").val()+'\"}';
		var obj_fz=convertJson.string2json1(json_fz);
		$("#jhForm").setFormValues(obj_fz);
	}	
}


//查询出最大pch
function maxpch(xdnf){
	var success=true;
	var data1=combineQuery.getQueryCombineData(queryForm,frmPost,null);
	var actionName=controllername+"?query_maxpch&xdlx="+xdlx+"&xdnf="+xdnf;
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
//计算金额
function countHj(){
	var count = parseFloat($("#GC").val()==""?0:$("#GC").val())+parseFloat($("#ZC").val()==""?0:$("#ZC").val())+parseFloat($("#QT").val()==""?0:$("#QT").val());
	//var subCount = count.toFixed(4);
	$("#JHZTZE").val(count);
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
					<button id="btnSave" class="btn" type="button">确定</button>
				</span>
			</h4>
		    <form method="post" id="jhForm">
		    	 <input type="hidden" id="GC_CBK_PCB_ID" fieldname="GC_CBK_PCB_ID">
		    	 <input type="hidden" id="SJBH" fieldname="SJBH">
				<table class="B-table">
	      			<tr>
	         			<!-- <th width="10%" class="right-border bottom-border text-right">下达类型</th>
	         			<td width="40%" class=" bottom-border text-left"><input class="span12" id="XDLX" type="radio" onclick="test(this)" placeholder="" kind="dic" src="E#1=正常下达:2=应急下达" name= "XDLX" fieldname="XDLX">
	        		    </td>
	         			<th width="10%" class="right-border bottom-border text-right">下达类型</th>
	         			<td width="40%" class=" bottom-border text-left"><input class="span12" id="XDLX" type="text" name= "XDLX" fieldname="XDLX" disabled>
	        		    </td> -->
	        		    <th width="10%" class="right-border bottom-border text-right">批次类型</th>
	         			<td width="40%" class=" bottom-border text-left"><input class="span12" id="XDLX" type="radio" onclick="test(this)" placeholder="" kind="dic" src="PCLX" name= "PCLX" fieldname="PCLX">
	        		    </td>
						<th width="10%" id="PCHTH" class="right-border bottom-border text-right">下达批次</th>
						<td width="40" id="PCHTD" class="right-border bottom-border">
							<select class="span12 3characters" id="PCH" name="PCH" fieldname="PCH"  check-type="required" kind="dic" src="PCH"></select>
						</td>
	        		</tr>
	        		<tr>	    
						<th width="10%" class="right-border bottom-border text-right">下达日期</th>
						<td width="40%" class="right-border bottom-border">
							<input class="span12 date" id="XDRQ" name="XDRQ" placeholder="YYYY-MM-DD" check-type="required" fieldname="XDRQ" type="date"/>
						</td> 
						<td id="switch" colspan="2" style="display:none;"></td>        
						<th width="10%" id="XDWHTH" class="right-border bottom-border text-right">下达文号</th>
						<td width="40" id="XDWHTD"class="right-border bottom-border">
							<input class="span12" id="XDWH" name="XDWH" fieldname="XDWH"  placeholder="必填"  maxlength="100"  check-type="required maxlength"  type="text"/>
						</td>	
					</tr>
					<tr>
						<th class="right-border bottom-border text-right" width="8%">工程</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj()" min="0" placeholder="" style="width:65%;text-align:right;" id="GC" fieldname="PC_GC" name="GC"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>							
						<th class="right-border bottom-border text-right" width="8%">征拆</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj()" min="0" placeholder="" style="width:65%;text-align:right;" id="ZC" fieldname="PC_ZC" name="ZC"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>
					</tr>
					<tr>
						<th class="right-border bottom-border text-right" width="8%">其他</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj()" min="0" placeholder="" style="width:65%;text-align:right;" id="QT" fieldname="PC_QT" name="QT"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>
						<th class="right-border bottom-border text-right disabledTh" width="8%">总投资额</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" min="0" placeholder="" min="0" style="width:65%;text-align:right;" id="JHZTZE" fieldname="PC_JHZTZE" name="JHZTZE" readonly/>&nbsp;&nbsp;<b>(万元)</b>
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