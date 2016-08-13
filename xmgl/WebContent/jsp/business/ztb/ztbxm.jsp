<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>项目选择</title>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
 <script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xmcbk/xmcbkxdController.do";
var trlen; 


//初始化查询
$(document).ready(function(){
	var year=new Date().getFullYear()
	var num=document.getElementsByTagName("option");
	num[0].innerHTML=year;
	num[1].innerHTML=year+1;	
	g_bAlertWhenNoResult=false;
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query_jzxd&dczlx=1",data,DT1);
	var data=combineQuery.getQueryCombineData(queryForm2,frmPost,DT2);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query_zc",data,DT2);
	g_bAlertWhenNoResult=true;
	trlen=$("#DT2 tr").length-1;
	var num=document.getElementsByTagName("font");
	num[0].innerHTML=trlen;
});


//页面初始化
$(function() {
	//按钮绑定事件(查询)
	$("#btnQuery").click(function() {
		//生成json串
		var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query_jzxd&dczlx=1",data,DT1);
	});

	
	//按钮绑定事件(下达统筹计划)项目
/* 	$("#btnXdt").click(function() {
		var ids = "";
		if($("#DT2").getTableRows()==0){
			xInfoMsg('没有可下达的项目！');
			return;
		}
		var ids = "";
		$("#DT2 tbody tr").each(function(){
			//存行数据
			var value = convertJson.string2json1($(this).attr("rowJson")).GC_TCJH_XMCBK_ID;
			ids+=value+",";
		});
		$("body").manhuaDialog({"title":"储备库项目下达","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmcbk/xdxx.jsp?ids="+ids,"modal":"4"});
	}); */

	
	//按钮绑定事件（暂存）
	$("#btnZc").click(function() {
		   parent.$("body").manhuaDialog.close();
		//获取id
/* 		var ids = "";
		$("#DT2 tbody tr").each(function(){
			var value = convertJson.string2json1($(this).attr("rowJson")).GC_TCJH_XMCBK_ID;
			ids+=value+",";
		});	
		var success = doInsertZc(controllername + "?insert_zc&ids="+ids+"&dczlx=1", null);
		if(success){
			xSuccessMsg('操作成功！');
		}else{
			xFailMsg('操作失败！');
		} */
	});	

	
	//根据ID新增待下达
	doInsertZc = function(actionName, data1) {
	    var success  = true;
		$.ajax({
			type : 'post',
			url : actionName,
			data : data1,
			dataType : 'json',
			async :	false,
			success : function(result) {
				$("#resultXML").val(result.msg);
				success = true;
			},
		    error : function(result) {
		     	alert(result.msg);
			    defaultJson.clearTxtXML();
			    success = false;
			}
		});
		 return success;
	};
	
	
	//按钮绑定事件(上移)
	$("#btnUp").click(function() {
		var rowindex=$("#DT2").getSelectedRowIndex();//获得选中行的索引
		if(rowindex==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		var rowValue=$("#DT2").getSelectedRow();//获得选中行的json对象
		$("#DT1").insertResult(rowValue,DT1,1);
		$("#DT2").removeResult(rowindex);
		trlen=$("#DT2 tr").length-1;
		var num=document.getElementsByTagName("font");
		num[0].innerHTML=trlen;
	});
	
	
	//按钮绑定事件(下移)
	$("#btnDown").click(function() {
		var rowindex=$("#DT1").getSelectedRowIndex();//获得选中行的索引
 		if(rowindex==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		var rowValue=$("#DT1").getSelectedRow();//获得选中行的json对象
		$("#DT2").insertResult(rowValue,DT2,1);
		$("#DT1").removeResult(rowindex);
		trlen=$("#DT2 tr").length-1;
		var num=document.getElementsByTagName("font");
		num[0].innerHTML=trlen;
	});
	

	//按钮绑定事件(全选)
	$("#all").click(function() {
		if($("#DT1 tr").length==1)
		{
			xInfoMsg('没有可移动的项目！');
			return;
		}	
		$("#DT1 tbody tr").each(function(){
			var rowValue=convertJson.string2json1($(this).attr("rowJson"));
			$("#DT2").insertResult(JSON.stringify(rowValue),DT2,1);
		});
		$("#DT1").clearResult();
		trlen=$("#DT2 tr").length-1;
		var num=document.getElementsByTagName("font");
		num[0].innerHTML=trlen;
	});
	
	
	//按钮绑定事件(取消)
	$("#cannel").click(function() {
		if($("#DT2 tr").length==1)
		{
			xInfoMsg('没有可移动的项目！');
			return;
		}	
		$("#DT2 tbody tr").each(function(){
			var rowValue=convertJson.string2json1($(this).attr("rowJson"));
			$("#DT1").insertResult(JSON.stringify(rowValue),DT1,1);
		});
		$("#DT2").clearResult();
		trlen=$("#DT2 tr").length-1;
		var num=document.getElementsByTagName("font");
		num[0].innerHTML=trlen;
	});

	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        $("#num").val("1000");
        //其他处理放在下面
    });

	
	//自动完成项目名称模糊查询
	var controllername_xd= "${pageContext.request.contextPath }/xmcbk/xmcbkwhController.do";
	showAutoComplete("XMMC",controllername_xd+"?xmmcAutoQuery","getXmmcQueryCondition"); 
});


//生成项目名称查询条件
function getXmmcQueryCondition(){
	var initData = '{"querycondition":{"conditions":[ {"value": "0","fieldname":"ISXD","operation":"=","logic":"and"},]},"orders":[{"order":"desc","fieldname":"pxh"}]}';
	var jsonData = eval('(' + initData + ')'); 
	//项目名称
	if("" != $("#XMMC").val()){
		var defineCondition = {"value": "%"+$("#XMMC").val()+"%","fieldname":"XMMC","operation":"like","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	//年度
	if("" != $("#ND").val()){
		var defineCondition = {"value": $("#ND").val(),"fieldname":"ND","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	return JSON.stringify(jsonData);
}


//结转方法
doInsertJh=function(actionName, data1) {
    var success =true;
	$.ajax({
		type : 'post',
		url : actionName,
		data : data1,
		dataType : 'json',
		async :	false,
		success : function(result) {
			$("#resultXML").val(result.msg);
			$("#jhForm").clearFormResult();
			success=true;
		},
	    error : function(result) {
	     	alert(result.msg);
		    defaultJson.clearTxtXML();
		    success=false;
		}
	});
	return success;
};



//行加下移按钮
function doDwon(obj){
	return "<button name=\"btnDownTable\" onclick=\"rowDown(this)\" class=\"btn btn-link\" type=\"button\"><i class=\"icon-chevron-down\"></i></button>";
}
//行加上移按钮
function doUp(obj){
	return "<button name=\"btnUpTable\" onclick=\"rowUp(this)\" class=\"btn btn-link\" type=\"button\"><i class=\"icon-chevron-up\"></i></button>";
}


//下移按钮事件
function rowDown(obj){
	var rowIndex = $(obj).parent().parent().index();
	var rowValue = $(obj).parent().parent().attr("rowJson");
	$("#DT2").insertResult(rowValue,DT2,1);
	$("#DT1").removeResult(rowIndex);
	trlen = $("#DT2 tr").length-1;
	var num = document.getElementsByTagName("font");
	num[0].innerHTML = trlen;
}


//上移按钮事件
function rowUp(obj){
	var rowIndex = $(obj).parent().parent().index();
	var rowValue = $(obj).parent().parent().attr("rowJson");
	$("#DT1").insertResult(rowValue,DT1,1);
	$("#DT2").removeResult(rowIndex);
	trlen = $("#DT2 tr").length-1;
	var num = document.getElementsByTagName("font");
	num[0].innerHTML = trlen;
}


//单击获取对象
function tr_click(obj){}
//弹出区域回调
getWinData = function(data){
	if(data.success){
		xSuccessMsg('操作成功！');
		$("#DT2").clearResult();
		var parentmain=parent.$("body").manhuaDialog.getParentObj();
		parentmain.query();
		var num = document.getElementsByTagName("font");
		num[0].innerHTML = 0;
	}else{
		xFailMsg('操作失败！');
	}
	
};

</script>  
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
  		<form method="post" id="queryForm" class="B-small-from-table-autoConcise">
   			<table class="B-table" width="100%">
		      	<!--可以再此处加入hidden域作为过滤条件 -->
		      	<TR style="display:none;">
			        <TD class="right-border bottom-border"></TD>
					<TD class="right-border bottom-border">
						<input type="hidden" id="ywid"/>
						<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
					</TD>
		       	</TR>
     			<!--可以再此处加入hidden域作为过滤条件 -->
       			<tr>
					<th width="5%" class="right-border bottom-border text-right">年度</th>
					<td width="10%" class="right-border bottom-border">
						<select class="span12" id="ND" name="ND" defaultMemo="全部" check-type="required" fieldname="ND" operation="=">
							<option id="now"></option>
							<option id="next"></option>						
						</select>
					</td>
					<th width="5%" class="right-border bottom-border text-right">项目名称</th>
					<td width="25%" colspan="3"  class="right-border bottom-border">
						<input class="span12" type="text" placeholder="" name="XMMC" check-type="maxlength" maxlength="100" fieldname="XMMC" operation="like" id="XMMC" autocomplete="off">
					</td>					
					<td width="55%" class="text-left bottom-border text-right">
						<button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
						<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
					</td>
      			</tr>
   			</table>
   		</form>
		<div class="B-small-from-table-autoConcise">
			<div style="height:200px;overflow:auto;">
				<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" noPage="true" pageNum="1000">
					<thead>
						<tr>
							<th  name="XH" id="_XH" tdalign="center">&nbsp;#&nbsp;</th>
							<th fieldname="XMBH" tdalign="center" CustomFunction="doDwon">&nbsp;操作&nbsp;</th>
							<th fieldname="XMBH" tdalign="center">&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>		
							<th fieldname="XMMC" maxlength="15">&nbsp;标段名称&nbsp;</th>						
							<th fieldname="XMLX" tdalign="center">&nbsp;项目类型&nbsp;</th>
							<th fieldname="XMSX" tdalign="center">&nbsp;项目属性&nbsp;</th>
							<th fieldname="XMDZ" maxlength="15">&nbsp;项目地址&nbsp;</th>
						</tr>							
					</thead>
					<tbody id="tbody"></tbody>
				</table>
			</div>	
		</div>
		<br><br>
		<div align="center">
			<button id="all" class="btn" type="button"><i class="icon-plus"></i>&nbsp;全部下移</button>
			<button id="btnUp" class="btn" type="button"><i class="icon-chevron-up"></i>上移</button>
			<button id="btnDown" class="btn" type="button"><i class="icon-chevron-down"></i>下移</button>
			<button id="cannel" class="btn" type="button"><i class="icon-minus"></i>&nbsp;全部上移</button>	
		</div>
		<div class="B-small-from-table-autoConcise">
			<h4>
				已选择项目列表&nbsp;&nbsp; 共<font style=" margin-left:5px; margin-right:5px;;font-size:28px;color:red;">0</font>个
			 	<span class="pull-right">
			 		<button id="btnZc" class="btn" type="button">确定</button>
			 		<!--  <button id="btnXdt" class="btn" type="button"></button> -->
			 	</span>
					
			</h4>
			<div style="height:200px;overflow:auto;">
		  		<form method="post" id="queryForm2" class="B-small-from-table-autoConcise">
			      	<!--可以再此处加入hidden域作为过滤条件 -->
			      	<TR style="display:none;">
				        <TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<input type="hidden" id="DCZLX" name="DCZLX" fieldname="DCZLX" value="1" operation="="/>
							<INPUT type="hidden" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
						</TD>
		       		</TR>
			    </form>	
				<table class="table-hover table-activeTd B-table" id="DT2" width="100%" type="single" noPage="true" pageNum="1000">
					<thead>
						<tr>
							<th  name="XH" id="_XH" tdalign="center">&nbsp;#&nbsp;</th>
							<th fieldname="XMBH" tdalign="center" CustomFunction="doUp">&nbsp;操作&nbsp;</th>
							<th fieldname="XMBH" tdalign="center">&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>	
							<th fieldname="XMMC" maxlength="15">&nbsp;标段名称&nbsp;</th>						
							<th fieldname="XMLX" tdalign="center">&nbsp;项目类型&nbsp;</th>
							<th fieldname="XMSX" tdalign="center">&nbsp;项目属性&nbsp;</th>
							<th fieldname="XMDZ" maxlength="15">&nbsp;项目地址&nbsp;</th>
 						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>		  
		</div>
	</div>
</div>				
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>   
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id="queryXML"/>
		<input type="hidden" name="txtXML" id="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" fieldname ="PXH" id="txtFilter"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>		
	</FORM>
</div>
</body>
</html>