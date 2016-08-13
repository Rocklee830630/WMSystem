<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<app:base/>
<title>公告通知-公告信息查询</title>
<%
	User user = (User) session.getAttribute(Globals.USER_KEY);
	String account=user.getAccount();
	String dept=user.getDepartment();
%>

<script type="text/javascript" charset="UTF-8">
 	var id,json,rowindex,rowValue,flag,exeJson,sjbh;
  	var controllername= "${pageContext.request.contextPath }/fwglController.do";
  	var controllername_bl= "${pageContext.request.contextPath }/TaskAction.do";
  	
  	
  	//计算本页表格分页数
  	function setPageHeight(){
  		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
  		var pageNum = parseInt(height/pageTableOne,10);
  		$("#DT1").attr("pageNum",pageNum);
  	}
  	
  	function init() {
		$("#NGDW").val("<%=user.getDepartment() %>");
        //生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryFw", data, DT1,"queryCallBack");
	}
  	
	//页面默认参数
	$(document).ready(function() { 
		setPageHeight();
		person();
		$("#NGDW").val("<%=user.getDepartment() %>");
        //生成json串
        g_bAlertWhenNoResult = false;
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryFw", data, DT1);
        g_bAlertWhenNoResult = true;
	}); 
	
	$(function() {
		var btn = $("#queryBtn");
		btn.click(function() {
			$("#cqspBtn").html("呈请审批");
	        //生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryFw", data, DT1, null);
		});
	});
	
	// 清除表单
	$(function() {
		$("#query_clear").click(function() {
	       $("#queryForm").clearFormResult();
	       person();
		});
	});
	
	function tr_click(obj, tabListid) {
		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引
		rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
		id = obj.FWID;
		sjbh = obj.SJBH;
		exeJson = JSON.stringify(obj);
		json = JSON.stringify(obj);
		json=encodeURI(json); 
		var mark=obj.EVENTSJBH;
		switch(parseInt(mark)){
		case 0:
			$("#updateBtn").removeAttr("disabled");
			$("#deleteBtn").removeAttr("disabled");
			$("#cqspBtn").html("呈请审批");
			break;
		case 7:
			$("#updateBtn").removeAttr("disabled");
			$("#deleteBtn").removeAttr("disabled");
			$("#cqspBtn").html("再次呈请审批");
			break;
		default:
			$("#updateBtn").attr("disabled",true);
			$("#deleteBtn").attr("disabled",true);
			$("#cqspBtn").html("查看流程信息");		
			break;
		}
	}
	
	// 点击发布按钮
	$(function() {
		var btn = $("#publishGg");
		btn.click(function() {
		 	if($("#DT1").getSelectedRowIndex()==-1) {
				requireFormMsg("请选择一条要操作的数据！");
			} else {
				$.ajax({
					url : controllername + "?publishGg&ggid="+id ,
					cache : false,
					async : false,
					dataType : 'json',
					success : function(response) {
						var result = eval("(" + response + ")");
						init();
						if(result.sign == 0) {
							successInfo();
						}
					}
				});
			}
		});
	});
	
	//按钮绑定事件(新增公告)
	$(function() {
		$("#insertBtn").click(function() {
			flag = true;
			$(window).manhuaDialog({"title":"发文管理>拟稿发文","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/gwgl/fwgl/addFw.jsp","modal":"1"});
		});
	});
	//按钮绑定事件(新增公告)
	$(function() {
		$("#insertBtn1").click(function() {
			flag = true;
			$(window).manhuaDialog({"title":"发文管理>拟稿","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/gwgl/fwgl/addFw1.jsp","modal":"1"});
		});
	});

	//按钮绑定事件(修改收文)
	$(function() {
		$("#updateBtn").click(function() {
			if($("#DT1").getSelectedRowIndex()==-1) {
				requireFormMsg("请选择一条要操作的数据！");
			} else {
				flag = false;
				var obj = $("#DT1").getSelectedRowJsonObj();//获得选中行的索引
				id = obj.SWID;
				exeJson = JSON.stringify(obj);
				json = JSON.stringify(obj);
				json=encodeURI(json); 
				$(window).manhuaDialog({"title":"发文管理>修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/gwgl/fwgl/addFw.jsp?xx="+json+"&&id="+id+"&sjbh="+sjbh,"modal":"1"});
			}
		});
	});
	
	//回调函数
	getWinData = function(data){
	//	data = JSON.stringify(defaultJson.dealResultJson(data));
	  	index =	$("#DT1").getSelectedRowIndex();
		//$("#queryForm").clearFormResult();
		init();
	};
	function queryCallBack(){
		if(flag==false)
		{
			//修改	
			$("#DT1").setSelect(index);
			successInfo();
			var selObj = $("#DT1").getSelectedRowJsonObj();//获得选中行的索引
			tr_click(selObj, DT1);
		}
		else
		{
			//新增
			$("#DT1").setSelect(0);
			successInfo();
			var selObj = $("#DT1").getSelectedRowJsonObj();//获得选中行的索引
			tr_click(selObj, DT1);
		    index = 0;
		} 
	}
	$(function() {
		
		
		//历史发文查询
		$("#lsQuery").click(function() {
			$(window).manhuaDialog({"title":"发文历史查询","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/gwgl/fwgl/lsQuery.jsp?byPerson=1","modal":"1"});
		});
		
	});
	
	function getMessage(msg)
	{
		xAlert("运行结果",msg);
		g_bAlertWhenNoResult=false;
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryFw", data, DT1);
		g_bAlertWhenNoResult=true;
	}
	
	function rowView_(index){
	    var obj=$("#DT1").getRowJsonObjByIndex(index);//获取行对象
	    var sjbh = obj.SJBH;
	    var eventSjbh = obj.EVENTSJBH;
	    var wz = obj.WZ;
	    var ywlx = obj.YWLX;
	    if(eventSjbh == "0") {
	    	$(window).manhuaDialog({"title":"发文管理>查看","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/gwgl/fwgl/detailFw.jsp?sjbh="+sjbh+"&type=1&eventSjbh="+eventSjbh,"modal":"1"});
		} else {
		/* 	var wsActionURL = "/xmgl/PubWS.do?getXMLPrintAction|templateid="+wz+"|isEdit=0|ywlx="+ywlx+"|sjbh="+sjbh+"|rowid="+Math.random()+".mht";
			wsActionURL = "/xmgl/jsp/business/lcgl/lccx/processDetail.jsp?param="+wsActionURL+"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid="+wz+"&isEdit=0"+"&isview=1";
			var s = "/xmgl/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&isview=1";     
			$(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"}); */

			var s = "${pageContext.request.contextPath}/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview=1";   
			$(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});
		}
	}
	//author by liujs
	//呈请审批按钮说明
	$(function() {
		$("#cqspBtn").click(function() {
			//获取ywlx,sjbh
			if($("#DT1").getSelectedRowIndex()==-1) {
				requireFormMsg("请选择一条要操作的数据！");
			} else {
				var rowjson =	$("#DT1").getSelectedRow();
				var sjbh = JSON.parse(rowjson).SJBH;
				var ywlx = JSON.parse(rowjson).YWLX;
				var fwid = JSON.parse(rowjson).FWID;
				
				var eventSjbh=JSON.parse(rowjson).EVENTSJBH;
				switch (parseInt(eventSjbh)){
				case 0:
					createSPconf(sjbh,ywlx,"1");
					break;
				case 7:
					backSp();
					break;
				default:
					var rowIndex=$("#DT1").getSelectedRowIndex();
					rowView_(rowIndex);
				  	break;
				}
			}
		});
		var btn = $("#deleteBtn");
		btn.click(function() {
			var rowindex = $("#DT1").getSelectedRowIndex();
		 	if(rowindex==-1) {
				requireFormMsg("请选择一条要操作的数据！");
			} else {
 		 		xConfirm("提示信息","是否确认删除发文信息");
				$('#ConfirmYesButton').unbind();
			 	$('#ConfirmYesButton').one("click",function(obj) {
			 		var obj = $("#DT1").getSelectedRowJsonObj();//获得选中行的索引
					$.ajax({
						url : controllername + "?deleteFw&fwid="+obj.FWID,
						cache : false,
						async : false,
						dataType : 'json',
						success : function(response) {
							if(response == "1") {
								$("#DT1").removeResult(rowindex);
							}
						}
					});
				});
			} 
		});
	});
	//再次呈请审批方法
	function backSp(){
		var value=queryLcxx();
		var tempJson = convertJson.string2json1(value);
		var rowObject = tempJson.response.data[0];
	   	var url =  '${pageContext.request.contextPath}/'+rowObject.LINKURL+'?taskid='+rowObject.ID+'&taskseq='+rowObject.SEQ+'&sjbh='+rowObject.SJBH+'&ywlx='+rowObject.YWLX+'&spbh='+rowObject.SPBH+'&rwlx='+rowObject.RWLX+"&isRead="+rowObject.XB;
	   	$(window).manhuaDialog({"title":"审批信息","type":"text","content":url,"modal":"1"});
	}
	//查询流程信息，主要是再次呈请审批使用
	function queryLcxx(){
		var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
		var str = "";
		$.ajax({
			url:controllername_bl + "?getFwLc&sjbh="+sjbh,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				str = result.msg;
			}
		});
		return str;
	}
	//父页面更新方法，再次呈请审批后，父页面更新
	function gengxinchaxun()
	{
		var row_index=$("#DT1").getSelectedRowIndex();
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		var tempJson = convertJson.string2json1(data);
		var a=$("#DT1").getCurrentpagenum();
		tempJson.pages.currentpagenum=a;
		data = JSON.stringify(tempJson);
		defaultJson.doQueryJsonList(controllername+"?queryFw",data,DT1);
		$("#DT1").setSelect(row_index);
		successInfo("","");
		var selObj = $("#DT1").getSelectedRowJsonObj();//获得选中行的索引
		tr_click(selObj, DT1);
	}
	function person(){
		$("#radio1").prop("checked","checked");
		$("#radio2").removeAttr("fieldname");
		$("#radio1").attr("fieldname","LRR");
	}
	function deptement(){
		$("#radio1").removeAttr("fieldname");
		$("#radio2").attr("fieldname","LRBM");
	}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">发文管理
				<span class="pull-right">
					<button id="insertBtn" class="btn"  type="button" >拟稿</button>
				<!-- 	<button id="insertBtn1" class="btn"  type="button" >拟稿1</button> -->
					<button id="updateBtn" class="btn"  type="button" >修改</button>
					<button id="deleteBtn" class="btn"  type="button" >删除</button>
					<button id="cqspBtn" class="btn"  type="button" >呈请审批</button>
					<button id="lsQuery" class="btn" type="button">历史查询</button>
				<!-- 	<button id="cqspBtnBdwh" class="btn"  type="button" >呈请审批(不带文号)</button> -->
				<!-- 	<button id="deleteBtn" class="btn"  type="button" >删除公告</button> -->
				</span>
			</h4>
		<form method="post" id="queryForm"  >
		<table class="B-table">
		<!--可以再此处加入hidden域作为过滤条件 -->
			<TR  style="display:none;">
				<TD class="right-border bottom-border"></TD>
				<TD class="right-border bottom-border">
					<input type="text" class="span12" kind="text"  fieldname="rownum" keep="true"  value="1000" operation="<=" >
				</TD>
			</TR>
		<!--可以再此处加入hidden域作为过滤条件 -->
			<tr>
				<th width="5%" class="right-border bottom-border">文件编号</th>
				<td width="10%" class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" id="WJBH" name="WJBH" fieldname="WJBH" operation="like" logic="and" ></td>
				<th width="5%" class="right-border bottom-border">文件标题</th>
				<td width="10%" class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" name="WJBT" id="WJBT" fieldname="WJBT" operation="like" logic="and"></td>
				<th width="5%" class="right-border bottom-border text-right">发起</th>
				<td width="15%" class="right-border bottom-border">&nbsp;
					<span >
						<input class="" id="radio1" type="radio" style="margin: 0px;" fieldname="LRR" name = "tjfs" value="<%=account %>" operation="=" logic="and" onclick="person()" />&nbsp;&nbsp;本人
						<input class="" id="radio2" type="radio" style="margin: 0px;" fieldname="LRBM" name = "tjfs" value="<%= dept%>"  operation="=" logic="and" onclick="deptement()"/>&nbsp;&nbsp;本部门
					</span>
				</td>
				<!-- <th width="5%" class="right-border bottom-border">拟稿单位</th>
				<td width="10%" class="right-border bottom-border">
					<select class="span12" check-type="required" type="text" name="NGDW" maxlength="100" fieldname="NGDW" id="NGDW" kind="dic"
										src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME" operation="=" ></select></td>	 -->
			<!-- 	<th width="5%" class="right-border bottom-border">拟稿单位</th>
				<td width="10%" class="right-border bottom-border">
					<select class="span12" name="NGDW" type="text" fieldname="NGDW" id="NGDW" operation="=" logic="and"  kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME"></select></td>
					 -->
				<th width="2%" class="right-border bottom-border">带文号</th>
				<td width="9%" class="bottom-border">
					<select class="span12" type="text" placeholder="" id="WZ" name="WZ" fieldname="WZ" operation="="  logic="and"kind="dic" src="WZ"></select>
				</td>
	<!-- 			<th width="3%" class="right-border bottom-border">流水号</th>
				<td width="9%" class="right-border bottom-border">
					<input class="span12" type="text" name="LSH" fieldname="LSH" id="LSH" operation="like"  logic="and"/></td> -->
				<th width="2%" class="right-border bottom-border">缓急</th>
				<td width="6%" class="right-border bottom-border">
					<select class="span12" type="text" name="HJ" fieldname="HJ" id="HJ" defaultMemo="全部" kind="dic" src="HJ1000000000302" operation="="  logic="and"></select></td>
			
				<td width="13%"  class="text-left bottom-border text-right" rowspan="2">
					<button	id="queryBtn" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
                    <button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
	            </td>	
			</tr>
		</table>
		</form>
	
	<div style="height:5px;"> </div>		
	<div class="overFlowX"> 
		<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
			<thead>
				<tr>
					<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
					<th fieldname="EVENTSJBH" >&nbsp;当前状态&nbsp;</th>
					<th fieldname="WJBH" >&nbsp;文件编号&nbsp;</th>
					<th fieldname="WZ" tdalign="center" maxlength="13">&nbsp;带文号&nbsp;</th>
					<th fieldname="WJBT" hasLink="true" maxlength="16" linkFunction="rowView_">&nbsp;文件标题&nbsp;</th>
					<th fieldname="NGDW" >&nbsp;拟稿单位&nbsp;</th>
					<th fieldname="NGRQ"  tdalign="center">&nbsp;拟稿日期&nbsp;</th>
					<th fieldname="HJ" tdalign="center">&nbsp;缓急&nbsp;</th>
				<!-- 	<th fieldname="FWSJ"  tdalign="center">&nbsp;发文时间&nbsp;</th> -->
				</tr>
			</thead>
		    <tbody></tbody>
		</table>
		            
		</div>
		
		</div>
	</div>
</div>

<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id="queryXML">
		<input type="hidden" name="txtXML" id="txtXML">
		<input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ" id="txtFilter">
		<input type="hidden" name="resultXML" id="resultXML">
		<input type="hidden" name="queryResult" id="queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">
	</FORM>
</div>

</body>
</html>