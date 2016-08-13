<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>审批待办</title>
<%
java.util.Calendar cal = java.util.Calendar.getInstance();
int year = cal.get(java.util.Calendar.YEAR);
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/TaskAction.do";

var objRow;
//页面初始化
$(function() {
	init();
	
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		if(!$("#queryForm").validationButton()){
			xAlert("提示信息","请选择<计划名称>");
			return;
		}
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?getUserTask",data,DT1);
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		//var indexarry = new Array();
		var indexarry = new Array();
		indexarry = $("#DT1").getChangeRows();
		if(indexarry == "")
		{
	 		xAlert("提示信息",'请至少修改一条记录');
			return
 		}
		var jhid = $("#JHID").val();
		//alert(jhid);
		$(window).manhuaDialog
({"title":"统筹计划管理>保存版本记录","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/tcjh/bbjl_add.jsp?jhid="+jhid,"modal":"2"});

	});

	//按钮绑定事件（导出）
	$("#btnExp").click(function() {
		 var t = $("#DT1").getTableRows();
		 if(t<=0)
		 {
			 xAlert("提示信息","请至少查询出一条记录！");
			 return;
		 }
		 $(window).manhuaDialog({"title":"导出","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/print/TabListEXP.jsp?tabId=DT1","modal":"3"});
	});
	//按钮绑定事件（刷新标签）
	$("#btnSxbq").click(function() {
		xAlert("提示信息","刷新标签");
	});
	//按钮绑定事件（计划下发）
	$("#btnJhxf").click(function() {

		
		xConfirm("提示信息","确认将该计划下的项目进行下达吗？");
		$('#ConfirmYesButton').unbind();
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){
			var data = null;
			var success = xdtcjhByPc(controllername+"?xdtcjhByPc&jhid="+jhid,data,jhid);
			if(success){
				xAlert("提示信息",'操作成功！');
			}else{
				xAlert("提示信息",'操作失败！');
			}
		});  
			
	});
	//按钮绑定事件（版本信息）
	$("#btnBbxx").click(function() {
		if($("#DT1").getTableRows()==0)
		 {
			xAlert("提示信息",'没有数据！');
		    return
		 }
		var jhid = $("#JHID").val();
		$(window).manhuaDialog({"title":"统筹计划管理>版本信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/tcjh/bbjl_view.jsp?jhid="+jhid,"modal":"1"});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
    });
	

	   //监听变化事件
	 	$("#QCJDWDM").on("change", function(){
		   generatePc($("#QCJRID"));
		}); 
});
//页面默认参数
function init(){
	//getNd();
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?getUserTask",data,DT1);
}
//详细信息
function rowView(){
	
}
function tr_click(obj){
	objRow = obj;
}
function xdtcjhByPc(actionName, data1){
	var success = true;
	$.ajax({
		type : 'post',
		url : actionName,
		data : data1,
		dataType : 'json',
		async :	false,
		success : function(result) {
			success = true;
		},
	    error : function(result) {
		     	//alert(result.msg);
			    defaultJson.clearTxtXML();
			    success = false;
		}
	});
	 return success;
}

//回调函数
getWinData = function(data){
	//alert(data.YWID);
	//alert(data.BGSM);
	//alert();
	$(window).manhuaDialog.close();
	var url = controllername + "?updatebatchdata&JHID="+$("#JHID").val()+"&YWID="+data.YWID+"&BGSM="+encodeURIComponent(data.BGSM);
	var indexarry = new Array();
	indexarry = $("#DT1").getChangeRows();
	if(indexarry == "")
	{
	 	xAlert("提示信息",'请至少修改一条记录');
		return
 	}
	//获取表格表头的数组,按照表格显示的顺序
	var tharrays = new Array();
	var comprisesData;
	tharrays = $("#DT1").getTableThArrays();
	var rowValue = $("#tcjhList").getSelectedRow();//获得选中行的json对象
	if(tharrays != null){
		comprisesData = $("#DT1").comprisesData(indexarry,tharrays);
		//alert(DT1);
		defaultJson.doUpdateBatchJson(url, comprisesData,DT1,indexarry);

	}

};
//默认年度
function getNd(){
	//项目年份默认当前年
	var d = new Date();
	var year = d.getFullYear();
	var qnd = $("#qnd");
	var t = 0;
	qnd.find("option").each(function() {
		if(this.value == year){
			return false;
		}
		t++;
	});
	if(t){
		qnd.get(0).selectedIndex=t;  
	}
}

function linkTest(index){
    //alert(obj.name);	
	alert("OK:"+index);
}
function doDetail(i)
{
	var rowJson = $("#DT1").getSelectedRowJsonByIndex(i);
	var rowObject = JSON.parse(rowJson);
	
	var url =  '${pageContext.request.contextPath}/'+rowObject.LINKURL+'?taskid='+rowObject.ID+'&taskseq='+rowObject.SEQ+'&sjbh='+rowObject.SJBH+'&ywlx='+rowObject.YWLX+'&spbh='+rowObject.SPBH+'&rwlx='+rowObject.RWLX;
  	//alert(url);
	//window.location.href=url;
	 $(window).manhuaDialog({"title":"审批信息","type":"text","content":url,"modal":"1"});  
}
function gengxinchaxun(obj)
{
	xAlertMsg(obj);
	g_bAlertWhenNoResult = false;
	init();
	g_bAlertWhenNoResult = true;
	window.parent.getProcess();
}


function generatePc(ndObj) {
		// 默认查询所有人员
		var srcVal = "T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG = 1  AND PERSON_KIND = '3'";
		if($("#QCJDWDM").val()) {
			srcVal = "T#FS_ORG_PERSON:ACCOUNT:NAME:DEPARTMENT = "+$("#QCJDWDM").val()+ " AND FLAG=1  AND PERSON_KIND = '3' order by sort";
		}
		ndObj.attr("src", srcVal);
		ndObj.attr("kind", "dic");
		ndObj.html('');
		reloadSelectTableDic(ndObj);
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">流程办理 </h4>
     <form method="post" id="queryForm">
      <table class="B-table">
        <tr>
          <th width="4%" class="right-border bottom-border text-right">申请部门</th>
          <td width="8%" class="right-border bottom-border">
         	 
         	 <select class="span12" type="text"  id="QCJDWDM" fieldname="a.CJDWDM" name="CJDWDM" kind="dic"
									operation="=" 	src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME" defaultMemo="全部"></select>
          </td>
          <th width="3%" class="right-border bottom-border text-right">申请人</th>
          <td width="8%" class="right-border bottom-border">
           <select class="span12" type="text"  id="QCJRID" fieldname="a.CJRID" name="CJRID" defaultMemo="全部" operation="=" kind="dic" src ="T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG = 1"></select>
          </td>
          <th width="4%" class="right-border bottom-border">申请时间</th>
          <td width="8%" class="right-border bottom-border">
			<input  class="span12" type="date" name = "QCJSJGT" fieldname = "CJSJ" operation=">="  fieldtype="date" fieldformat="YYYY-MM-DD"/>
          </td>
		
          <th width="2%" class="right-border bottom-border">至</th>
          <td width="7%" class="right-border bottom-border">
			<input  class="span12" type="date" name = "QCJSJLT" fieldname = "CJSJ" operation="<="  fieldtype="date" fieldformat="YYYY-MM-DD"/>
          </td>
          <td width="10%" class="right-border bottom-border text-right">
            <button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
      		<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
          </td>
 
          </tr>
      </table>
      </form>
	<div style="height:5px;"> </div>		
	<div class="overFlowX"> 
	<table class="table-hover table-activeTd B-table" id="DT1"  width="100%" type="single" pageNum="8">
		<thead>
					<tr>
						<th name="XH" id="_XH" style="width:10px" tdalign="center">&nbsp;#&nbsp;</th>
						<th fieldname="MEMO" tdalign="left" maxlength="40" hasLink="true" linkFunction="doDetail">&nbsp;待办标题&nbsp;</th>
						<th fieldname="YWLX">&nbsp;业务类型&nbsp;</th>
						<th fieldname="CJRID" maxlength="15">&nbsp;上级办理人&nbsp;</th>
						<th fieldname="CJSJ" tdalign="center">&nbsp;上级办理时间&nbsp;</th>
					</tr>
		</thead>
		<tbody></tbody>
	</table>
	</div>
	</div>
</div>
</div>
    
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "cjsj" id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>