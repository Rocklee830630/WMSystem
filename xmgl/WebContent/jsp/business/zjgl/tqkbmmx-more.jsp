<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>项目下达</title>
<%-- <% 
	String num=request.getParameter("rowValues");
%>
 --%>
 <%
 	String cMonth = Pub.getDate("MM");
 	String qklx = request.getParameter("qklx");
 	if(qklx==null){qklx="";}
 %>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglTqkbmmxController.do";
var trlen; 


//初始化查询
$(document).ready(function(){
	var year=new Date().getFullYear()
	$("#GCPC").val('<%=cMonth%>');
	$("#QKLX").val('<%=qklx%>');
	
	$("#QKLX").attr("disabled", true);
	
	var num=document.getElementsByTagName("option");
	num[0].innerHTML=year;
	num[1].innerHTML=year+1;	
	g_bAlertWhenNoResult=false;
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	<%
		if("15".equals(qklx)||"16".equals(qklx)){
			%>
				defaultJson.doQueryJsonList(controllername+"?queryBMMXdisGcb&opttype=bmmx",data,DT1);
			<%
		}else{
			
			%>
				defaultJson.doQueryJsonList(controllername+"?queryBMMX&opttype=bmmx",data,DT1);
			<%
		}
	%>
	
	//var data=combineQuery.getQueryCombineData(queryForm2,frmPost,DT2);
	g_bAlertWhenNoResult=true;
	trlen=$("#DT2 tr").length-1;
	var num=document.getElementsByTagName("font");
	num[0].innerHTML=trlen;
});


//页面初始化
$(function() {
	$("#ND1").hide();
	$("#ND").val(new Date().getFullYear());
	chaxunxiangm();
	//按钮绑定事件(查询)
	$("#btnQuery").click(function() {
		//生成json串
		var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		<%
		if("15".equals(qklx)||"16".equals(qklx)){
			%>
				defaultJson.doQueryJsonList(controllername+"?queryBMMXdisGcb&opttype=bmmx",data,DT1);
			<%
		}else{
			
			%>
				defaultJson.doQueryJsonList(controllername+"?queryBMMX&opttype=bmmx",data,DT1);
			<%
		}
	%>
	});
 //调用父页面方法获取已选项目
 function  chaxunxiangm()
  {
	var fuyemian=$(window).manhuaDialog.getParentObj();
    var obj = fuyemian.getArr();
    if(obj==undefined){
    }else{
   	 for(var i=0;i<obj.length;i++)
	 {
	   $("#DT2").insertResult(obj[i],DT2,1);
	   trlen=$("#DT2 tr").length-1;
	   var num=document.getElementsByTagName("font");
	   num[0].innerHTML=trlen;
	 }
    }
  }
	
	//按钮绑定事件（确定）
	$("#btnFH").click(function() {
		//获取id
		var ids = "";
		var rowValues=new Array();
		$("#DT2 tbody tr").each(function(i){
			var rowValue = $(this).attr("rowJson");
			rowValues[i]=rowValue;
			var value = convertJson.string2json1($(this).attr("rowJson")).ID;
			ids+=value+",";
		});	
		var fuyemian=$(window).manhuaDialog.getParentObj();
	    fuyemian.fanhuixiangm(rowValues,ids);
	    $(window).manhuaDialog.close();
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
		     	//alert(result.msg);
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
 		var chongfu=chongfupanduan();
 		if(chongfu==false)
 			{
 			  xInfoMsg('项目已添加！');
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
		//判断是否有相同的项目
		$("#DT1 tbody tr").each(function(){
			var isinsert = true;
			var rowValue=convertJson.string2json1($(this).attr("rowJson"));
			/* $("#DT2").insertResult(JSON.stringify(rowValue),DT2,1); */
			var dtid = rowValue.ID;
			$("#DT2 tbody tr").each(function()
			   {
				  var rowValue1=convertJson.string2json1($(this).attr("rowJson"));
				  var dtid2=rowValue1.ID;
				  //alert(dtid+'---'+dtid);
				  if(dtid==dtid2)
					{
					isinsert = false;
					return
					}
			  });
			if(isinsert)
				{
			      $("#DT2").insertResult(JSON.stringify(rowValue),DT2,1);
				}
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
	    /* 	$("#DT2 tbody tr").each(function(){
			var rowValue=convertJson.string2json1($(this).attr("rowJson"));
			$("#DT1").insertResult(JSON.stringify(rowValue),DT1,1);
		}); */
		//判断是否有相同的项目
		$("#DT2 tbody tr").each(function(){
			var isinsert = true;
			var rowValue=convertJson.string2json1($(this).attr("rowJson"));
			/* $("#DT2").insertResult(JSON.stringify(rowValue),DT2,1); */
			var dtid2 = rowValue.ID;
			$("#DT1 tbody tr").each(function()
			{
				var rowValue1=convertJson.string2json1($(this).attr("rowJson"));
				var dtid1=rowValue1.ID;
				//alert(dtid1+'---'+dtid2);
				if(dtid1==dtid2)
					{
					isinsert = false;
					return
					}
		       });
			if(isinsert)
				{
			      $("#DT1").insertResult(JSON.stringify(rowValue),DT1,1);
				}
		});
		
		$("#DT2").clearResult();
		trlen=$("#DT2 tr").length-1;
		var num=document.getElementsByTagName("font");
		num[0].innerHTML=trlen;
	});

	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        //$("#queryForm").clearFormResult();
        
        $("#ND").val("");
        $("#GCPC").val("");
        $("#SQDW").val("");
        $("#num").val("1000");
        //其他处理放在下面
    });

	
	//自动完成项目名称模糊查询
	//var controllername_xd= "${pageContext.request.contextPath }/xmcbk/xmcbkwhController.do";
	//showAutoComplete("XMMC",controllername_xd+"?xmmcAutoQuery","getXmmcQueryCondition"); 
});


//生成项目名称查询条件
function getXmmcQueryCondition(){
	var initData = '{"querycondition":{"conditions":[ {"value": "0","fieldname":"ISXD","operation":"=","logic":"and"},]},"orders":[{"order":"desc","fieldname":"pxh"}]}';
	var jsonData = convertJson.string2json1(initData); 
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
	     	//alert(result.msg);
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
	$("#DT1").setSelect(rowIndex);
	var chongfu=chongfupanduan();
		if(chongfu==false)
			{
			  xInfoMsg('项目已添加！');
			  return 
			}
	//var rowIndex = $(obj).parent().parent().index();
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
	$("#DT2").setSelect(rowIndex);
	var sycf=xiayichongfupanduan();
	var rowValue = $(obj).parent().parent().attr("rowJson");
	if(sycf){
	$("#DT1").insertResult(rowValue,DT1,1);
	}
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

//上移判断
function chongfupanduan()
{
	 var success =true;
		$("#DT2 tbody tr").each(function(){
			var dt2id = convertJson.string2json1($(this).attr("rowJson")).ID;
			var json=$("#DT1").getSelectedRow();
			var odd = convertJson.string2json1(json);
			var id=$(odd).attr("ID");
			if(id==dt2id)
				{
				success=false;
				}
		});
		return success;
}
//下移判断
function xiayichongfupanduan()
{
	 var success =true;
		$("#DT1 tbody tr").each(function(){
			var dtid1 = convertJson.string2json1($(this).attr("rowJson")).ID;
			var json=$("#DT2").getSelectedRow();
			var odd = convertJson.string2json1(json);
			var id=$(odd).attr("ID");
			if(id==dtid1)
				{
				success=false;
				}
		});
		return success;
}
//父页面项目修改
function addxm()
{
	 for(var i=0;i<rowValues.length;i++)
	 {
		// alert(rowValues[i]);
	   $("#DT1").insertResult(rowValues[i],DT1,1);
	 }
	 $("#jhsjids").val(ids);
	 $("#rowValues").val(rowValues);
}

//详细信息
function rowView(index){
	$("#DT1").setSelect(index);
	if($("#DT1").getSelectedRowIndex()==-1)
	 {
		requireSelectedOneRow();
	    return
	 }
	$("#resultXML").val($("#DT1").getSelectedRow());
	$(window).manhuaDialog({"title":"提请款部门明细>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqkbmmx-add-cwbview.jsp?type=detail","modal":"2"});
}
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
					<TD class="right-border bottom-border">
<%--						<input type="hidden" id="ywid"/>--%>
<%--						<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>--%>
					</TD>
		       	</TR>
     			<!--可以再此处加入hidden域作为过滤条件 -->
       			<tr>
					<th width="5%" class="right-border bottom-border text-right">请款年份</th>
					<td width="12%" class="right-border bottom-border">
						<select style="width:90%;" id="ND1"  class="span6"  name="QKNF" fieldname="gzt.QKNF"  operation="=" kind="dic" src="T#GC_ZJGL_TQKBM: distinct qknf as QKNF:qknf as x:SFYX='1' ORDER BY QKNF ASC" defaultMemo="-请选择-"></select>
						<select style="width:90%;" id="ND"   placeholder="必填" check-type="required" class="span6"  name="QKNF" fieldname="gzt.QKNF"  operation="=" kind="dic" src="T#GC_ZJGL_TQKBM: distinct qknf as QKNF:qknf as x:SFYX='1' ORDER BY QKNF ASC" defaultMemo="-请选择-"></select>
					</td>
					<th width="5%" class="right-border bottom-border text-right">请款月份</th>
					<td width="12%" class="right-border bottom-border">
						<select  style="width:90%;" id="GCPC"   class="span6"  name="GCPC" fieldname="GCPC" kind="dic" src="YF"  operation="="  defaultMemo="-请选择-">
					</td>

					<th width="8%" class="right-border bottom-border text-right">申请单位</th>
					<td width="17%" class="right-border bottom-border">
						<select type="text" fieldname="SQDW" name="SQDW" id="SQDW" kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME" operation="="></select>
					</td>
					<th width="8%" class="right-border bottom-border text-right">请款类型</th>
					<td width="17%" class="right-border bottom-border">
						<select id="QKLX"   placeholder="必填" check-type="required" class="span12"  name="QKLX" fieldname="QKLX"  operation="=" kind="dic" src="QKLX"  defaultMemo="-请选择-">
					</td>
					
					
					<td width="55%" class="text-left bottom-border text-right">
						<button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
						<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
					</td>
      			</tr>
   			</table>
   		</form>
		<div class="B-small-from-table-autoConcise">
			<div style="overflow:auto;">
				<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single"  pageNum="5">
					<thead>
						<tr>
							<th  name="XH" id="_XH" tdalign="center">&nbsp;#&nbsp;</th>
							<th fieldname="ID" tdalign="center" CustomFunction="doDwon">&nbsp;操作&nbsp;</th>
							<th fieldname="XMMCNR" colindex=1 tdalign="center" maxlength="30" >&nbsp;项目名称内容&nbsp;</th>
							<th fieldname="SQDW" colindex=1 tdalign="center" maxlength="30" >&nbsp;申请单位&nbsp;</th>
							<th fieldname="QKLX" colindex=2 tdalign="center" maxlength="30" >&nbsp;请款类型&nbsp;</th>
							<th fieldname="QKNF" colindex=3 tdalign="center" maxlength="30" >&nbsp;请款年份&nbsp;</th>					
							<th fieldname="ZXHTJ" colindex=7 tdalign="center" >&nbsp;最新合同价&nbsp;</th>
							<th fieldname="YBF" colindex=8 tdalign="center" >&nbsp;已拔付&nbsp;</th>
							<th fieldname="BCSQ" colindex=9 tdalign="center" hasLink="true" linkFunction="rowView" >&nbsp;本次申请拔款&nbsp;</th>
							<th fieldname="LJBF" colindex=10 tdalign="center" >&nbsp;累计拔付&nbsp;</th>
							<th fieldname="AHTBFB" colindex=11 tdalign="center" >&nbsp;按合同付款比例&nbsp;</th>
							<th fieldname="CSZ" colindex=12 tdalign="center" >&nbsp;财审值&nbsp;</th>
							<th fieldname="JZQR" colindex=13 tdalign="center" >&nbsp;监理确认计量款&nbsp;</th>
							<th fieldname="AJLFKB" colindex=14 tdalign="center" >&nbsp;按计量付款比例&nbsp;</th>
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
				已选择明细项目列表&nbsp;&nbsp; 共<font style=" margin-left:5px; margin-right:5px;;font-size:28px;color:red;">0</font>个
			 	<span class="pull-right">
			 		<button id="btnFH" class="btn" type="button">确定</button>
			 	</span>
					
			</h4>
			<div style="height:200px;overflow:auto;">
		  		<form method="post" id="queryForm2" class="B-small-from-table-autoConcise">
			      	<!--可以再此处加入hidden域作为过滤条件 -->
			      	<TR style="display:none;">
				        <TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<!-- <input type="hidden" id="DCZLX" name="DCZLX" fieldname="DCZLX" value="1" operation="="/> -->
							<INPUT type="hidden" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
						</TD>
		       		</TR>
			    </form>	
				<table class="table-hover table-activeTd B-table" id="DT2" width="100%" type="single" noPage="true" pageNum="1000">
					<thead>
						<tr>
							<th  name="XH" id="_XH" tdalign="center">&nbsp;#&nbsp;</th>
							<th fieldname="ID" tdalign="center" CustomFunction="doUp">&nbsp;操作&nbsp;</th>
							<th fieldname="XMMCNR" colindex=1 tdalign="center" maxlength="30" >&nbsp;项目名称内容&nbsp;</th>
							<th fieldname="SQDW" colindex=1 tdalign="center" maxlength="30" >&nbsp;申请单位&nbsp;</th>
							<th fieldname="QKLX" colindex=2 tdalign="center" maxlength="30" >&nbsp;请款类型&nbsp;</th>
							<th fieldname="QKNF" colindex=3 tdalign="center" maxlength="30" >&nbsp;请款年份&nbsp;</th>					
							<th fieldname="ZXHTJ" colindex=7 tdalign="center" >&nbsp;最新合同价&nbsp;</th>
							<th fieldname="YBF" colindex=8 tdalign="center" >&nbsp;已拔付&nbsp;</th>
							<th fieldname="BCSQ" colindex=9 tdalign="center" >&nbsp;本次申请拔款&nbsp;</th>
							<th fieldname="LJBF" colindex=10 tdalign="center" >&nbsp;累计拔付&nbsp;</th>
							<th fieldname="AHTBFB" colindex=11 tdalign="center" >&nbsp;按合同付款比例&nbsp;</th>
							<th fieldname="CSZ" colindex=12 tdalign="center" >&nbsp;财审值&nbsp;</th>
							<th fieldname="JZQR" colindex=13 tdalign="center" >&nbsp;监理确认计量款&nbsp;</th>
							<th fieldname="AJLFKB" colindex=14 tdalign="center" >&nbsp;按计量付款比例&nbsp;</th>
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
		<input type="hidden" name="txtFilter" order="desc" fieldname ="gzt2.lrsj" id="txtFilter"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>		
	</FORM>
</div>
</body>
</html>