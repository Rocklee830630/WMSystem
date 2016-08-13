<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<app:base/>
<title>提请款-维护</title>
<%
	String type=request.getParameter("type");
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String userid = user.getAccount();
	String username = user.getName();
	String sysdate = Pub.getDate("yyyy-MM-dd");
	String YF = Pub.getDate("MM");
	String cYear = Pub.getDate("yyyy");
	
	String sql = "SELECT max(gzt.gcpc)+1 FROM gc_zjgl_tqk gzt WHERE gzt.qknf='"+cYear+"'";
	Connection conn = DBUtil.getConnection();//定义连接
	String pc = "";
	String[][] yzcqArray = DBUtil.query(conn, sql);
	if(yzcqArray.length>0 && null !=yzcqArray && !Pub.empty(yzcqArray[0][0])){
	    pc = yzcqArray[0][0].toString();
	}
	DBUtil.closeConnetion(conn);
	
	if("".equals(pc)){
	    pc = "1";
	}
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglTqkController.do";
var controllernameTqkMx= "${pageContext.request.contextPath }/zjgl/gcZjglTqkmxController.do";
var controllernameTqkBmMx= "${pageContext.request.contextPath }/zjgl/gcZjglTqkbmmxController.do";
var type ="<%=type%>", jbrID;
var xmrowValues;
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryFormMx,frmPost,gcZjglTqkmxList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllernameTqkMx+"?query&opttype=tocwedit",data,gcZjglTqkmxList);
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#gcZjglTqkForm").validationButton())
		{
		    //生成json串
		    var data = Form2Json.formToJSON(gcZjglTqkForm);
		  	//组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  	//调用ajax插入
		    if($("#ID").val() == "" || $("#ID").val() == null){
		    	var success=defaultJson.doInsertJson(controllername + "?insert", data1);
		    	if(success==true)
				{
					var obj=$("#resultXML").val();
					var subresultmsgobj1=defaultJson.dealResultJson(obj);
					$("#gcZjglTqkForm").setFormValues(subresultmsgobj1);
					$("#RZZT").attr("code",subresultmsgobj1.RZZT);
					$("#RZZT").val(subresultmsgobj1.DICT_NAME);
					$("#QTQKID").val($("#ID").val());
					$("#QID").val($("#ID").val());
					$("#btnInsert_Sel").attr("disabled", false);
					
					//$("#btnClear_Bins").remove();
				}
    			//$("#gcZjglTqkForm").clearFormResult();
    		}else{
    			defaultJson.doUpdateJson(controllername + "?update", data1);
    		}
		}else{
			requireFormMsg();
		  	return;
		}
	});
	
	//移除
	$("#btnDelete").click(function() {
		var rowindex=$("#gcZjglTqkmxList").getSelectedRowIndex();//获得选中行的索引
		if(rowindex==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		
		xConfirm("信息确认","移除当前明细？  "); //6
		
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){
			var rowValue=$("#gcZjglTqkmxList").getSelectedRow();//获得选中行的json对象
			var tempJson = convertJson.string2json1(rowValue);
			
			//表单赋值
			var data = combineQuery.getQueryCombineData(queryForm,frmPost);
			var data1 = {
				msg : data
			};
			$.ajax({
				url : controllernameTqkMx+"?delete&opttype=delonly&ids="+tempJson.ID,
				data : data1,
				cache : false,
				async :	false,
				dataType : "json",  
				type : 'post',
				success : function(response) {
					//$("#btnQuery").click();
					
					$("#gcZjglTqkmxList").removeResult(rowindex);
				}
			});
		});
	});
	//移除并退回
	$("#btnDeleteReturn").click(function() {
		var rowindex=$("#gcZjglTqkmxList").getSelectedRowIndex();//获得选中行的索引
		if(rowindex==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		
		xConfirm("信息确认","将当前明细退回至部门？  "); //6
		
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){
			var rowValue=$("#gcZjglTqkmxList").getSelectedRow();//获得选中行的json对象
			var tempJson = convertJson.string2json1(rowValue);
			
			//表单赋值
			var data = combineQuery.getQueryCombineData(queryForm,frmPost);
			var data1 = {
				msg : data
			};
			$.ajax({
				url : controllernameTqkMx+"?delete&opttype=delandreturn&ids="+tempJson.ID,
				data : data1,
				cache : false,
				async :	false,
				dataType : "json",  
				type : 'post',
				success : function(response) {
					//$("#btnQuery").click();
					
					$("#gcZjglTqkmxList").removeResult(rowindex);
					
				}
			});
		});
	});
	
	$("#btnSendToBmMaster").click(function() {
		
		var rows = $("#gcZjglTqkmxList tbody tr");
		if(rows.size()==0){
			xAlert("", "明细不能为空！", 2);
			return false;
		}
		
		xConfirm("信息确认","您确认提交吗？ ");
		
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){
			$("#btnSave").attr("disabled", true);
			$("#btnSendToBmMaster").attr("disabled", true);
			//更新记录为提交状态
			$("#TQKZT").val("4");
		    //生成json串
		    var data = Form2Json.formToJSON(gcZjglTqkForm);
		  	//组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		    var success= defaultJson.doUpdateJson(controllername + "?update&opttype=cwtj", data1);
		    if(success==true)
			{
		    	$("#btnSendToBmMaster").attr("disabled", true);
		    	$("#btnDelete").attr("disabled", true);
		    	$("#btnDeleteReturn").attr("disabled", true);
		    	$("#btnInsert_Sel").attr("disabled", true);

		    	  
			}
		});
	});
	
	//按钮绑定事件（新增）
    $("#btnClear_Bins").click(function() {
        $("#gcZjglTqkForm").clearFormResult();
        $("#gcZjglTqkForm").cancleSelected();
        
        
        getNd();
    	$("#SQDW").val('<%=deptId%>');
    	$("#BZRQ").val('<%=sysdate%>');
        $("#ID").val("");
    });
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
    
  	//按钮绑定事件(新增)
	$("#btnInsert").click(function() {
		$(window).manhuaDialog({"title":"提请款明细>新增","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqkmx-add.jsp?type=insert","modal":"2"});
	});
  	//修改明细
	$("#btnUpdateMx").click(function() {
		if($("#gcZjglTqkmxList").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$(window).manhuaDialog({"title":"提请款明细>修改","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqkmx-add.jsp?type=update","modal":"2"});
	});
	
  
    <%
    if(type.equals("detail")){
	%>
	//置所有input 为disabled
	$(":input").each(function(i){
	   $(this).attr("disabled", "true");
	 });
	<%
		}
	%>
	
	$("#btnInsert_Sel").click(function() {
		$(window).manhuaDialog({"title":"提请款明细>选择部门明细","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqkbmmx-more.jsp?qklx="+$("#QKLX").val(),"modal":"2"});
	});

	$("#btnHtView").click(function(){
		  var values = "";
		    
		      if($("#gcZjglTqkmxList").getSelectedRowIndex()==-1)
		      {
		       requireSelectedOneRow();
		       return
		      }
		      values = $("#gcZjglTqkmxList").getSelectedRow();
		 
		 
		  $("#resultXML").val(values);
		  //var rowValue = $("#DT1").getSelectedRow();
		  //var tempJson = convertJson.string2json1(values);
		   //var idvar = tempJson.ID;
		  $(window).manhuaDialog({"title":"部门提请款>合同详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/ht-view-tqkmx.jsp?type=detail","modal":"1"});
		     
	});
	
	$("button[id^='blrBtn_']").bind("click", function(){
    	var valu = $(this).attr("value");
    	jbrID = valu;
    	var actorCode = $("#"+valu).attr("code");
		openUserTree('single',actorCode,'setBlr') ;
	});
	
});
//页面默认参数
function init(){
	if(type == "insert"){
		getNd();
		$("#SQDW").val('100000000000');
		$("#BZRQ").val('<%=sysdate%>');
		
		
		$("#QKNF").val('<%=cYear%>');
		$("#YF").val('<%=YF%>');
		
		$("#CWBLRID").val('<%=username%>');
		$("#CWBLRID").attr("code", '<%=userid%>');
		
		$("#GCPC").val('<%=pc%>');
		setTqkName();
		
	}else if(type == "update" || type == "detail"){
		var parentmain=$(this).manhuaDialog.getParentObj();	
		var rowValue = parentmain.$("#resultXML").val();
		var tempJson = convertJson.string2json1(rowValue);
		
		$("#QTQKID").val(tempJson.ID);
		$("#QID").val(tempJson.ID);
		
		//alert(JSON.stringify(tempJson));
		//查询记录数
		
		//表单赋值
		var data = combineQuery.getQueryCombineData(queryForm,frmPost);
		var data1 = {
			msg : data
		};
		$.ajax({
			url : controllername+"?query",
			data : data1,
			cache : false,
			async :	false,
			dataType : "json",  
			type : 'post',
			success : function(response) {
				$("#resultXML").val(response.msg);
				var resultobj = defaultJson.dealResultJson(response.msg);
				$("#gcZjglTqkForm").setFormValues(resultobj);
				$("#RZZT").attr("code",resultobj.RZZT);
				$("#RZZT").val(resultobj.DICT_NAME);
			}
		});
		
		
		//查询明细
		var dataMx = combineQuery.getQueryCombineData(queryFormMx,frmPost,gcZjglTqkmxList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllernameTqkMx+"?query&opttype=tocwedit",dataMx,gcZjglTqkmxList, null, true);
		
		$("#btnInsert_Sel").attr("disabled", false);
		$("#btnSendToBmMaster").attr("disabled", false);
		//$("#btnClear_Bins").remove();
	}
}
var popPage;
function selectRZZT(){
	popPage = "selectRZZT";
	$(window).manhuaDialog({"title":"提请款管理>选择融资主体","type":"text","content":"/xmgl/jsp/framework/system/dict/commonDict.jsp?category=zjgl_tqk_rzzt&title=融资主体&field=RZZT","modal":"4"});
}
function setBlr(data){
	var userId = "";
	var userName = "";
	for(var i=0;i<data.length;i++){
 	 var tempObj =data[i];
 	 if(i<data.length-1){
	  userId +=tempObj.ACCOUNT+",";
	  userName +=tempObj.USERNAME+",";
 	 }else{
 	  userId +=tempObj.ACCOUNT;
 	  userName +=tempObj.USERNAME; 
 	 }
	}
	$("#"+jbrID).val(userName);
	$("#"+jbrID).attr("code", userId);
}

function setTqkName(){

	var subName ="工程用款申请表";
	var fullName = $("#QKNF").val()+"年";
	if($("#RZZT").val()!=""){
		fullName+=$("#RZZT").val();
	}
	fullName+="第";
	if($("#GCPC").val()!="")
		fullName += $("#GCPC").val()+"批"+subName;
	if($("#QKLX").val()!=""){
		//fullName += $("#QKLX").text();
		fullName += $("#QKLX").find("option:selected").text();
	}
	$("#QKMC").val(fullName);
}

//点击行事件
function tr_click(obj,tabListid){
	//alert(JSON.stringify(obj));
	//$("#gcZjglTqkForm").setFormValues(obj);
	if(tabListid='gcZjglTqkmxList'){
		$("#QMXID").val(obj.ID);
		$("#btnUpdateMx").attr("disabled", false);
		$("#btnDelete").attr("disabled", false);
	}
}

//默认年度
function getNd(){
	//$("#QKNF").val(new Date().getFullYear());
}
//选中项目名称弹出页
function selectXm(){
	$(window).manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/xmcx.jsp","modal":"2"});
}
//弹出区域回调
getWinData = function(data){
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#XMBH").val(JSON.parse(data).XMBH);
	$("#GC_TCJH_XMXDK_ID").val(JSON.parse(data).GC_TCJH_XMXDK_ID);
};

function closeNowCloseFunction(){
	return true;
}

function closeParentCloseFunction(){
	if(popPage == "selectRZZT"){return;}
	//刷新
	$("#btnQuery").click();
}

//详细信息
function rowView(index){
	var obj = $("#gcZjglTqkList").getSelectedRowJsonByIndex(index);
	var xmbh = eval("("+obj+")").XMBH;
	$(window).manhuaDialog(xmscUrl(xmbh));
}

function saveMX(){
	//生成json串
	  var data = Form2Json.formToJSON(gcZjglTqkForm);
	  //组成保存json串格式
	  var data1 = defaultJson.packSaveJson(data);
	  //调用ajax插入
	  defaultJson.doInsertJson(controllernameTqkMx + "?insert&opttype=iinto&id="+$("#ID").val()+"&ids="+$("#jhsjids").val(), data1,null);
	  var data3 = $("#frmPost").find("#resultXML").val();
	  
	  xmrowValues = "";
	  $("#jhsjids").val("");
	 
}

//子页面调用
function fanhuixiangm(rowValues,ids)
{
	 //清空列表
	// $("#gcZjglTqkmxList").find("tbody").children().remove();
	// alert(rowValues.length);
	 xmrowValues=rowValues;
	 //for(var i=0;i<rowValues.length;i++)
	 //{
	 //  $("#gcZjglTqkmxList").insertResult(rowValues[i],gcZjglTqkmxList,1);
	 //}
	 $("#jhsjids").val(ids);
	 saveMX();
}
function getArr()
{
	 return xmrowValues;
}

function doRandering_DBF(obj){
	var showHtml = "";
	if(obj.DBF!=""){
		showHtml = obj.DBF_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}


function doRandering_CWSHZ(obj){
	var showHtml = "";
	if(obj.CWSHZ!=""){
		showHtml = obj.CWSHZ_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
	<div class="B-small-from-table-autoConcise" style="display:black;">
	<input type="hidden" id="jhsjids" name="jhsjids"/>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
			<TD class="right-border bottom-border">
				<input class="span12" type="text" id="QID" name="ID"  fieldname="ID" value="" operation="=" >
			</TD>
        </TR>
      </table>
      </form>
	</div>
	
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">提请款
      	<span class="pull-right">
      		<%
		    	if(!type.equals("detail")){
			%>
<%--      		<button id="btnClear_Bins" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>--%>
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
	  		<button id="btnSendToBmMaster" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>提交</button>
	  		<%
				}
			%>
      	</span>
      </h4>

     <form method="post" id="gcZjglTqkForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
      	<input type="hidden" id="TQKZT" fieldname="TQKZT" name = "TQKZT" value="1"/></TD>
		
		<tr>
			<th width="8%" class="right-border bottom-border text-right">申请部门</th>
			<td width="17%" class="right-border bottom-border">
				<select type="text" class="span12"  fieldname="SQDW" name="SQDW" id="SQDW" kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME" ></select></td>
			</td>
			<th width="8%" class="right-border bottom-border text-right">请款类型</th>
			<td width="17%" class="right-border bottom-border">
				<select onchange="setTqkName();"  id="QKLX"    placeholder="必填" check-type="required" class="span12"  name="QKLX" fieldname="QKLX"  operation="=" kind="dic" src="QKLX"  defaultMemo="-请选择-">
			</td>
			<th width="8%" class="right-border bottom-border text-right">批次</th>
			<td width="17%" class="right-border bottom-border">
<%--				<select   onchange="setTqkName();" id="GCPC"   placeholder="必填" check-type="required" class="span6"  name="GCPC" fieldname="GCPC"  operation="=" kind="dic" src="PCH"  defaultMemo="-请选择-">--%>
					<input id="GCPC" onchange="setTqkName();" type="number" style="width:60%;text-align:right;" title="工程批次"   placeholder="必填" check-type="required" class="span12" check-type="maxlength" maxlength="40"  name="GCPC" fieldname="GCPC" min="0" />
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">请款年份</th>
			<td width="17%" class="right-border bottom-border">
				<select  id="QKNF"   placeholder="必填" check-type="required" class="span12"  name="QKNF" fieldname="QKNF"  operation="=" kind="dic" src="XMNF" defaultMemo="-请选择-" >
<%--				&nbsp;&nbsp;&nbsp;&nbsp;<input id="GCPC" onchange="setTqkName();" style="width:20%;" title="工程批次"   placeholder="必填" check-type="required" class="span12" check-type="maxlength" maxlength="40"  name="GCPC" fieldname="GCPC" type="text" />--%>
			</td>
			<th width="8%" class="right-border bottom-border text-right">请款月份</th>
			<td width="17%" class="right-border bottom-border">
				<select id="YF"   class="span12"  name="YF" fieldname="YF" kind="dic" src="YF"  defaultMemo="-请选择-" >
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">融资主体</th>
			<td width="17%" class="right-border bottom-border">
				<input id="RZZT" class="span12" style="width:80%;"  check-type="required"  name="RZZT" fieldname="RZZT" type="text" disabled/>
				<button class="btn btn-link"  type="button" onclick="selectRZZT()"  title="点击选择银行"><i class="icon-edit"></i></button>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">请款名称</th>
			<td width="17%" class="right-border bottom-border">
				<input id="QKMC"   placeholder="必填" check-type="required" class="span12" check-type="maxlength" maxlength="100"  name="QKMC" fieldname="QKMC" type="text" />
			</td>
			<th width="8%" class="right-border bottom-border text-right">编制日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BZRQ"  class="span9" fieldtype="date" fieldformat="YYYY-MM-DD"  placeholder="必填" check-type="required"  name="BZRQ" fieldname="BZRQ" type="date" />
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">编制人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="CWBLRID" style="width:80%;" class="span12" check-type="maxlength" maxlength="36"  name="CWBLRID" fieldname="CWBLRID" type="text" disabled/>
				<button class="btn btn-link"  type="button" id="blrBtn_BLR" value="CWBLRID" title="点击选择编制人"><i class="icon-edit"></i></button>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">备注</th>
	        <td  width="17%" class="bottom-border right-border" colspan="5">
	        	<textarea class="span12"  id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ"></textarea>
	        </td>
		</tr>
      </table>
      </form>
      
      
      <div style="height:5px;"></div>
      
      <h4 class="title">提请款明细
      	<span class="pull-right">
	      	<%
		    	if(!type.equals("detail")){
			%>
      		<button id="btnQuery" class="btn btn-link"  type="button" style="display:none;"><i class="icon-search"></i>查询</button>
<%--	  		<button id="btnInsert" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">添加明细</button>--%>
	  		<button id="btnHtView" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" >合同信息</button>
	  		<button id="btnInsert_Sel" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">选择添加明细</button>
<%--	  		<button id="btnUpdateMx" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">修改</button>--%>
	  		<button id="btnDelete" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">移除</button>
	  		<button id="btnDeleteReturn" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">退回</button>
	  		<%
				}
			%>
      	</span>
      	 <form method="post" id="queryFormMx">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<input class="span12" type="text" id="QTQKID" name="TQKID"  fieldname="t.TQKID" value="" operation="=" >
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
				</table>
	</form>
      </h4>
     
    <div class="overFlowX">
    <input type="hidden" id="QMXID">
	<table class="table-hover table-activeTd B-table" id="gcZjglTqkmxList" width="100%" pageNum="10" type="single" noPage="true">
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
				<th fieldname="DWMC" colindex=2 tdalign="center" maxlength="30" >&nbsp;收款单位&nbsp;</th>
				<th fieldname="XMMCNR" colindex=3 tdalign="center" maxlength="30" >&nbsp;项目名称内容&nbsp;</th>
				<th fieldname="HTBM" colindex=4 tdalign="center" maxlength="30" >&nbsp;合同编码&nbsp;</th>
				<th fieldname="ZXHTJ" colindex=5 tdalign="right" >&nbsp;最新合同价(元)&nbsp;</th>
				<th fieldname="YBF" colindex=6 tdalign="right" >&nbsp;已拔付(元)&nbsp;</th>
				<th fieldname="DBF" colindex=6 tdalign="right" CustomFunction="doRandering_DBF">&nbsp;待拔付(元)&nbsp;</th>
				<th fieldname="BCSQ" colindex=7 tdalign="right">&nbsp;本次申请拔款(元)&nbsp;</th>
				<th fieldname="CWSHZ" colindex=10 tdalign="right" CustomFunction="doRandering_CWSHZ">&nbsp;财务审核值(元)&nbsp;</th>
				<th fieldname="LJBF" colindex=8 tdalign="right" >&nbsp;累计拔付(元)&nbsp;</th>
				<th fieldname="AHTBFB" colindex=9 tdalign="right" >&nbsp;按合同付款比例(%)&nbsp;</th>
				<th fieldname="BZ" colindex=12 tdalign="center" maxlength="30" >&nbsp;备注&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
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
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "t.LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
<script>
</script>
</html>