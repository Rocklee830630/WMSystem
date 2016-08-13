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
	String QKLX=request.getParameter("QKLX");
	if(QKLX==null)
	    QKLX = "";
	String QKNF=request.getParameter("QKNF");
	if(QKNF==null)
	    QKNF = "";
	String YF=request.getParameter("YF");
	if(YF==null)
	    YF = "";
	String TQKID=request.getParameter("TQKID");
	if(TQKID==null)
	    TQKID = "";
	
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String userid = user.getAccount();
	String username = user.getName();
	String sysdate = Pub.getDate("yyyy-MM-dd");
	
	String sql = "SELECT max(gzt.gcpc)+1 FROM gc_zjgl_tqk gzt WHERE gzt.qknf='"+QKNF+"' AND gzt.qklx='"+QKLX+"'";
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
var type ="<%=type%>";
var xmrowValues, lxtype="<%=QKLX%>";
var hasSaved = false;
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryFormMx,frmPost,gcZjglTqkmxList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllernameTqkMx+"?query",data,gcZjglTqkmxList);
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
		    	var success=defaultJson.doInsertJson(controllername + "?insert&tqkid="+$("#QBMTQKID").val(), data1);
		    	if(success==true)
				{
					var obj=$("#resultXML").val();
					var subresultmsgobj1=defaultJson.dealResultJson(obj);
					$("#gcZjglTqkForm").setFormValues(subresultmsgobj1);
					$("#RZZT").attr("code",subresultmsgobj1.RZZT);
					$("#RZZT").val(subresultmsgobj1.DICT_NAME);
					$("#QTQKID").val($("#ID").val());
					$("#QID").val($("#ID").val());
<%--					$("#btnInsert").attr("disabled", false);--%>
					$("#btnUpdateMxSave").attr("disabled", false);
					
					$("#btnClear_Bins").remove();
					
					//查询明细
					var dataMx = combineQuery.getQueryCombineData(queryFormMx,frmPost,gcZjglTqkmxList);
					//调用ajax插入
					var ff = defaultJson.doQueryJsonList(controllernameTqkMx+"?query&opttype=tocwinsertedit",dataMx,gcZjglTqkmxList);
					if(ff){
						$("#btnDelete").attr("disabled", false);
						$("#btnDeleteReturn").attr("disabled", false);
					}
					
					$("#id_show_pcl").hide();
					$("#id_show_fgcl").hide();
					$("#id_show_pql").hide();
					$("#id_show_cwb").show();
					hasSaved = true;
					$("#btnSendToBmMaster").attr("disabled", false);
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
			}
		});
	});
	
	//批量保存
	$("#btnUpdateMxSave").click(function() {
		
		var url = controllernameTqkBmMx + "?updatebatchdata&opttype=cwb&tqkid="+$("#QID").val();
		
		var indexarry = new Array();
		var indexArryChange = new Array();
		indexArryChange = $("#gcZjglTqkmxList").getChangeRows();
		indexarry = $("#gcZjglTqkmxList").getAllRowsJOSNString();
		//获取表格表头的数组,按照表格显示的顺序
		var tharrays = new Array();
		var comprisesData;
		tharrays = $("#gcZjglTqkmxList").getTableThArrays();
		if(tharrays != null){
			comprisesData = $("#gcZjglTqkmxList").comprisesData(indexArryChange,tharrays);
			var success  = defaultJson.doUpdateBatchJson(url, comprisesData,null,indexArryChange);
			if(success){
				$("#sp_btn").attr("disabled",false);
			}
		}

		
<%--		var indexarry = new Array();--%>
		//获得改变的行数无法使用
		//indexarry = $("#gcZjglTqkmxList").getChangeRows();
		//if(indexarry == "")
		//{
		///	xInfoMsg('请至少修改一条记录！',"");
		////	return
 		//}
			//获取表格表头的数组,按照表格显示的顺序
<%--			var tharrays = new Array();--%>
<%--			var comprisesData;--%>
<%--			indexarry = $("#gcZjglTqkmxList").getAllRowsJOSNString();--%>
<%--			tharrays = $("#gcZjglTqkmxList").getTableThArrays();--%>
<%--			if(tharrays != null){--%>
<%--				comprisesData = $("#gcZjglTqkmxList").comprisesData(indexarry,tharrays);--%>
<%--				$("#btnSave").attr("disabled",true);--%>
<%--				alert(comprisesData);--%>
<%--				var xx = convertJson.simplejson2string(comprisesData);--%>
<%--				alert(xx);--%>
<%--				$("#txtXML").val(xx);--%>
<%--				var success  = defaultJson.doUpdateBatchJson(url, comprisesData,null,indexarry);--%>

				//$(window).manhuaDialog.getParentObj().queryList();
<%--				if(success){--%>
<%--					$("#sp_btn").attr("disabled",false);--%>
<%--				}--%>
<%--			}--%>
		
	});
	
	//移除
	$("#btnDelete").click(function() {
		var rowindex=$("#gcZjglTqkmxList").getSelectedRowIndex();//获得选中行的索引
		if(rowindex==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
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
	
	//移除并退回
	$("#btnDeleteReturn").click(function() {
		var rowindex=$("#gcZjglTqkmxList").getSelectedRowIndex();//获得选中行的索引
		if(rowindex==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
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
	
	$("#btnHtView").click(function(){
		var values = "";
		if(hasSaved){
			if($("#gcZjglTqkmxList").getSelectedRowIndex()==-1)
			{
		    	requireSelectedOneRow();
		    	return
			}
			values = $("#gcZjglTqkmxList").getSelectedRow();
		}else{
			if("16"==lxtype){
				if($("#gcZjglTqkbmList_pcl").getSelectedRowIndex()==-1)
				{
					requireSelectedOneRow();
		 			return
		  		}
			values = $("#gcZjglTqkbmList_pcl").getSelectedRow();
		}else if("14"==lxtype){
		   if($("#gcZjglTqkbmList_pql").getSelectedRowIndex()==-1)
		      {
		       requireSelectedOneRow();
		       return
		      }
		   	values = $("#gcZjglTqkbmList_pql").getSelectedRow();
		  }else{
		   	if($("#gcZjglTqkbmList").getSelectedRowIndex()==-1)
		      {
		       requireSelectedOneRow();
		       return
		      }
		   	values = $("#gcZjglTqkbmList").getSelectedRow();
		  }
		}
		  $("#resultXML").val(values);
		  //var rowValue = $("#DT1").getSelectedRow();
		  //var tempJson = convertJson.string2json1(values);
		   //var idvar = tempJson.ID;
		  $(window).manhuaDialog({"title":"部门提请款>合同详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/ht-view-tqkmx.jsp?type=detail","modal":"1"});
		     
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
		$(window).manhuaDialog({"title":"提请款明细>选择部门明细","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqkbmmx-more.jsp","modal":"2"});
	});
	
	var sp_btn = $("#sp_btn");
	sp_btn.click(function() 
 			{
		 		var sjbh = $("#QYWLX").val();
		 		var ywlx = $("#QSJBH").val();
		 		
		 		var condition = "";
		 		var xqzt = $("#TQKZT").val();
		 		if(xqzt!="1"){
		 			//alert("查看");
		 			 var s = "${pageContext.request.contextPath}/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview=1";   
		 		  	 $(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});  
		 		  	 
		 		}else{
		 		  //alert("审批");
			 			createSPconf(sjbh,ywlx,condition);
			 			 $("#DT1").cancleSelected();
		 		}
			 });
	
});
//页面默认参数
function init(){
	if(type == "insert"){
		$("#SQDW").val('100000000000');
		$("#BZRQ").val('<%=sysdate%>');
		
		$("#QKLX").val('<%=QKLX%>');
		$("#QKNF").val('<%=QKNF%>');
		$("#YF").val('<%=YF%>');
		$("#QBMTQKID").val('<%=TQKID%>');
		$("#CWBLRID").val('<%=username%>');
		$("#CWBLRID").attr("code", '<%=userid%>');
		
		
		//2013年第09批工程用款申请表监理类
		//设置名称
		$("#GCPC").val('<%=pc%>');
		setTqkName();
		
		
		
		//查询明细
		g_bAlertWhenNoResult = false;
		if("16"==lxtype){
			var dataMx = combineQuery.getQueryCombineData(queryFormBmMx,frmPost,gcZjglTqkbmList_pcl);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllernameTqkBmMx+"?query",dataMx,gcZjglTqkbmList_pcl);
			
			$("#id_show_pcl").show();
			$("#id_show_fgcl").hide();
			$("#id_show_pql").hide();
		}else if("14"==lxtype){
			var dataMx = combineQuery.getQueryCombineData(queryFormBmMx,frmPost,gcZjglTqkbmList_pql);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllernameTqkBmMx+"?query",dataMx,gcZjglTqkbmList_pql);
			
			$("#id_show_pcl").hide();
			$("#id_show_fgcl").hide();
			$("#id_show_pql").show();
		}else{
			var dataMx = combineQuery.getQueryCombineData(queryFormBmMx,frmPost,gcZjglTqkbmList);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllernameTqkBmMx+"?query",dataMx,gcZjglTqkbmList);
			$("#id_show_pcl").hide();
			$("#id_show_pql").hide();
			$("#id_show_fgcl").show();
		}
		g_bAlertWhenNoResult = true;
		
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
			}
		});
		
		
		//查询明细
		var dataMx = combineQuery.getQueryCombineData(queryFormMx,frmPost,gcZjglTqkmxList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllernameTqkMx+"?query",dataMx,gcZjglTqkmxList);
		
		$("#btnInsert_Sel").attr("disabled", false);
	}
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
	if(tabListid='gcZjglTqkmxList'){
		$("#QMXID").val(obj.ID);
		//$("#btnUpdateMx").attr("disabled", false);
		$("#btnDelete").attr("disabled", false);
		$("#btnDeleteReturn").attr("disabled", false);
	}
}
var popPage;
function selectRZZT(){
	popPage = "selectRZZT";
	$(window).manhuaDialog({"title":"部门提请款综合>选择融资主体","type":"text","content":"/xmgl/jsp/framework/system/dict/commonDict.jsp?category=zjgl_tqk_rzzt&title=融资主体&field=RZZT","modal":"4"});
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

//状态颜色判断
function docolor(obj)
{
	var xqzt=obj.BMTQKMXZT;
	if(xqzt=="3"){
	  	return '<span class="label label-success">'+obj.BMTQKMXZT_SV+'</span>';
	}else if(xqzt=="4"){
		if(obj.CWTQKID=="0"){
			return '<span class="label label-success">可用</span>';
		}
	  	return '<span class="label label-warning">'+obj.BMTQKMXZT_SV+'</span>';
	}else if(xqzt=="5"){
		return '<span class="label label-danger">'+obj.BMTQKMXZT_SV+'</span>';
	}else if(xqzt=="6"){
	 	return '<span class="label label-success">'+obj.BMTQKMXZT_SV+'</span>';
	}else if(xqzt=="7"){
	 	return '<span class="label label-success">'+obj.BMTQKMXZT_SV+'</span>';
	}else{
		return obj.BMTQKMXZT_SV;
	}
}

function doParse(obj){
	return '<div style="text-align:center">—</div>';
}
function doDBF(obj){
	if(obj.DBF==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.DBF_SV;
	}
}
function doBZ(obj){

	if(obj.BZ==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BZ;
	}
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
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
<%--	  		<button id="sp_btn" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>呈请审批</button>--%>
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
      	<input type="hidden" id="QYWLX" name="YWLX"  fieldname="YWLX" >
		<input type="hidden" id="QSJBH" name="SJBH"  fieldname="SJBH" >
		
		<tr>
			<th width="8%" class="right-border bottom-border text-right">申请部门</th>
			<td width="17%" class="right-border bottom-border">
				<select type="text" class="span12"  fieldname="SQDW" name="SQDW" id="SQDW" kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME"></select></td>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">请款类型</th>
			<td width="17%" class="right-border bottom-border">
				<select onchange="setTqkName();"  id="QKLX"    placeholder="必填" check-type="required" class="span12"  name="QKLX" fieldname="QKLX"  operation="=" kind="dic" src="QKLX"  defaultMemo="-请选择-" disabled>
			</td>
			<th width="8%" class="right-border bottom-border text-right">批次</th>
			<td width="17%" class="right-border bottom-border">
<%--				<select   onchange="setTqkName();" id="GCPC"   placeholder="必填" check-type="required" class="span6"  name="GCPC" fieldname="GCPC"  operation="=" kind="dic" src="PCH"  defaultMemo="-请选择-">--%>
					<input id="GCPC" onchange="setTqkName();" type="number" style="width:60%;text-align:right;" title="工程批次"   placeholder="必填" check-type="required" class="span12" check-type="maxlength" maxlength="40"  name="GCPC" fieldname="GCPC" />
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
				<input id="RZZT" class="span12" style="width:80%;" check-type="required" name="RZZT" fieldname="RZZT" type="text" disabled/>
				<button class="btn btn-link"  type="button" onclick="selectRZZT()" title="点击选择融资主体"><i class="icon-edit"></i></button>
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
				<input id="CWBLRID"  class="span12" check-type="maxlength" maxlength="36"  name="CWBLRID" fieldname="CWBLRID" type="text" disabled/>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">备注</th>
	        <td  width="17%" class="bottom-border right-border" colspan="5">
<%--	        	<textarea class="span12"  id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ"></textarea>--%>
	        	<input id="BZ"  class="span12" check-type="maxlength" maxlength="4000"  name="BZ" fieldname="BZ" type="text" />
	        </td>
		</tr>
<%--        <tr>--%>
<%--	        <th width="8%" class="right-border bottom-border text-right">备注</th>--%>
<%--	        <td colspan="3" class="bottom-border right-border" >--%>
<%--	        	<textarea class="span12" rows="2" id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ"></textarea>--%>
<%--	        </td>--%>
<%--        </tr>--%>
      </table>
      </form>
      
      
      <div style="height:5px;"></div>
      
      <h4 class="title">提请款明细
      	<span class="pull-right">
	      	<%
		    	if(!type.equals("detail")){
			%>
      		<button id="btnQuery" class="btn btn-link"  type="button" style="display:none;"><i class="icon-search"></i>查询</button>
      		<button id="btnHtView" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">合同信息</button>
<%--	  		<button id="btnUpdateMxSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>保存明细修改</button>--%>
	  		
<%--	  		<button id="btnDelete" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>移除</button>--%>
<%--	  		<button id="btnDeleteReturn" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>退回</button>--%>
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
							<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
							<input class="span12" type="text" id="QTQKID" name="TQKID"  fieldname="t.TQKID" value="" operation="=" />
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
				</table>
	</form>
	 <form method="post" id="queryFormBmMx">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
							<input class="span12" type="text" id="QBMTQKID" name="TQKID"  fieldname="TQKID" value="" operation="=" >
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
				</table>
	</form>
      </h4>
     
    <div class="overFlowX" style="display:none;" id="id_show_cwb">
    <input type="hidden" id="QMXID">
<%--	<table class="table-hover table-activeTd B-table" id="gcZjglTqkmxList" width="100%" pageNum="1000" editable="1" type="multi" noPage="true">--%>
	<table class="table-hover table-activeTd B-table" id="gcZjglTqkmxList" width="100%" type="single" pageNum="1000" noPage="true">
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
				<th fieldname="DWMC" colindex=2 tdalign="center" maxlength="30" >&nbsp;收款单位&nbsp;</th>
				<th fieldname="XMMCNR" colindex=3 tdalign="center" maxlength="30" >&nbsp;项目名称内容&nbsp;</th>
				<th fieldname="HTBM" colindex=4 tdalign="center" maxlength="30" >&nbsp;合同编码&nbsp;</th>
				<th fieldname="ZXHTJ" colindex=5 tdalign="center" >&nbsp;最新合同价(元)&nbsp;</th>
				<th fieldname="YBF" colindex=6 tdalign="center" >&nbsp;已拔付(元)&nbsp;</th>
				<th fieldname="DBF" colindex=6 tdalign="right" CustomFunction="doDBF">&nbsp;待拔付(元)&nbsp;</th>
				<th fieldname="BCSQ" colindex=7 tdalign="center" >&nbsp;本次申请拔款(元)&nbsp;</th>
<%--				<th fieldname="CWSHZ" colindex=10 tdalign="center" type="text">&nbsp;财务审核值(元)&nbsp;</th>--%>
				<th fieldname="CWSHZ" colindex=10 tdalign="center" CustomFunction="doParse">&nbsp;财务审核值(元)&nbsp;</th>
				<th fieldname="LJBF" colindex=8 tdalign="center" >&nbsp;累计拔付(元)&nbsp;</th>
				<th fieldname="AHTBFB" colindex=9 tdalign="center" >&nbsp;按合同付款比例(%)&nbsp;</th>
				<th fieldname="BZ" colindex=12 tdalign="center" maxlength="30" CustomFunction="doBZ">&nbsp;备注&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	
	    <div class="overFlowX" id="id_show_fgcl">
	<table class="table-hover table-activeTd B-table" id="gcZjglTqkbmList" width="100%" type="single" pageNum="1000" noPage="true">
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
 			    <th fieldname="BMTQKMXZT" colindex=17 tdalign="center" CustomFunction="docolor">&nbsp;明细状态&nbsp;</th>
				<th fieldname="DWMC" colindex=4 tdalign="center" maxlength="30" >&nbsp;收款单位&nbsp;</th>
				<th fieldname="XMMCNR" colindex=5 tdalign="left" maxlength="30" >&nbsp;项目内容&nbsp;</th>
				<th fieldname="HTBM" colindex=6 tdalign="center" maxlength="30" >&nbsp;合同编码&nbsp;</th>
				<th fieldname="ZXHTJ" colindex=7 tdalign="right" >&nbsp;最新合同价(元)&nbsp;</th>
				<th fieldname="YBF" colindex=8 tdalign="right" >&nbsp;已拨付(元)&nbsp;</th>
				<th fieldname="BCSQ" colindex=9 tdalign="right" >&nbsp;部门申请值(元)&nbsp;</th>
<%--				<th fieldname="LJBF" colindex=10 tdalign="center" >&nbsp;累计拔付&nbsp;</th>--%>
				<th fieldname="AHTBFB" colindex=11 tdalign="right" >&nbsp;累计付款比例(%)&nbsp;</th>
				<th fieldname="BZ" colindex=19 tdalign="center" maxlength="30" >&nbsp;备注&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
    
    <div class="overFlowX" id="id_show_pcl" style="display:none;">
	<table class="table-hover table-activeTd B-table" id="gcZjglTqkbmList_pcl" width="100%" type="single"  pageNum="1000" noPage="true">
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
 			    <th fieldname="BMTQKMXZT" colindex=17 tdalign="center" CustomFunction="docolor">&nbsp;明细状态&nbsp;</th>
				<th fieldname="DWMC" colindex=4 tdalign="center" maxlength="30" >&nbsp;收款单位&nbsp;</th>
				<th fieldname="XMMCNR" colindex=5 tdalign="left" maxlength="30" >&nbsp;项目内容&nbsp;</th>
				<th fieldname="HTBM" colindex=6 tdalign="center" maxlength="30" >&nbsp;合同编码&nbsp;</th>
				<th fieldname="ZXHTJ" colindex=7 tdalign="right" >&nbsp;最新合同价(元)&nbsp;</th>
				<th fieldname="CSZ" colindex=12 tdalign="right" >&nbsp;财审值(元)&nbsp;</th>
				<th fieldname="CSZ" colindex=12 tdalign="right" >&nbsp;审计值(元)&nbsp;</th>
				<th fieldname="JZQR" colindex=13 tdalign="right" >&nbsp;监理确认计量款(元)&nbsp;</th>
				<th fieldname="JGC" colindex=13 tdalign="right" >&nbsp;甲供材(元)&nbsp;</th>
				<th fieldname="YBF" colindex=8 tdalign="right" >&nbsp;已拨付(元)&nbsp;</th>
				<th fieldname="BCSQ" colindex=9 tdalign="right" >&nbsp;部门申请值(元)&nbsp;</th>
				<th fieldname="AJLFKB" colindex=14 tdalign="right" >&nbsp;按计量付款比例(%)&nbsp;</th>
				<th fieldname="AHTBFB" colindex=11 tdalign="right" >&nbsp;累计付款比例(%)&nbsp;</th>
				<th fieldname="BZ" colindex=19 tdalign="center" maxlength="30" >&nbsp;备注&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	
	<div class="overFlowX" id="id_show_pql" style="display:none;">
	<table class="table-hover table-activeTd B-table" id="gcZjglTqkbmList_pql" width="100%" type="single"  pageNum="1000" noPage="true">
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
 			    <th fieldname="BMTQKMXZT" colindex=17 tdalign="center" CustomFunction="docolor">&nbsp;明细状态&nbsp;</th>
				<th fieldname="DWMC" colindex=4 tdalign="center" maxlength="30" >&nbsp;收款单位&nbsp;</th>
				<th fieldname="XMMCNR" colindex=5 tdalign="left" maxlength="30" >&nbsp;项目内容&nbsp;</th>
				<th fieldname="HTBM" colindex=6 tdalign="center" maxlength="30" >&nbsp;合同编码&nbsp;</th>
				<th fieldname="ZXHTJ" colindex=7 tdalign="right" >&nbsp;最新合同价(元)&nbsp;</th>
				<th fieldname="CSZ" colindex=12 tdalign="right" >&nbsp;财审值(元)&nbsp;</th>
				<th fieldname="YBF" colindex=8 tdalign="right" >&nbsp;已拨付(元)&nbsp;</th>
				<th fieldname="BCSQ" colindex=9 tdalign="right" >&nbsp;部门申请值(元)&nbsp;</th>
				<th fieldname="AHTBFB" colindex=11 tdalign="right" >&nbsp;累计付款比例(%)&nbsp;</th>
				<th fieldname="BZ" colindex=19 tdalign="center" maxlength="30" >&nbsp;备注&nbsp;</th>
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
         <input type="hidden" name="txtFilter"  order="asc" fieldname = "rownum " id = "txtFilter">
         
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
<script>
</script>
</html>