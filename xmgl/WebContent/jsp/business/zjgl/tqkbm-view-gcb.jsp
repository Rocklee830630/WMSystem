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
<title>提请款部门-维护</title>
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
String cMonth = Pub.getDate("MM");
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglTqkGcbController.do";
var controllernameTqkBm= "${pageContext.request.contextPath }/zjgl/gcZjglTqkbmController.do";
var type ="<%=type%>";
var flag,jbrID, lxtype;
var msgControl= '';
var opttype = '';
var gcbtqkid = '';
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		var dataMx = combineQuery.getQueryCombineData(queryFormMx,frmPost,DT1);
		defaultJson.doQueryJsonList(controllername+"?queryMx",dataMx,DT1);
	});
	//按钮绑定事件(通过)
	$("#btnSuccess").click(function() {
		opttype = 'tongguo';
		
		xConfirm("信息确认","您确认通过吗？ ");
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){
			updateTqkGcb(opttype);
		})
		
	});
	//按钮绑定事件(退回)
	$("#btnFail").click(function() {
		opttype = 'tuihui';
		
		xConfirm("信息确认","您确认退回吗？ ");
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){
			updateTqkGcb(opttype);
		})
	});
	$("#btnHtView").click(function(){
		  
		if($("#DT1").getSelectedRowIndex()==-1)
		{
			requireSelectedOneRow();
			return
		}
		values = $("#DT1").getSelectedRow();
		  
		$("#resultXML").val(values);
		  //var rowValue = $("#DT1").getSelectedRow();
		  //var tempJson = convertJson.string2json1(values);
		   //var idvar = tempJson.ID;
		$(window).manhuaDialog({"title":"部门提请款审核>合同详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/ht-view-tqkmx.jsp?type=detail","modal":"1"});
		     
	});

});

function updateTqkGcb(type){
	//alert(type);
	//xConfirm("信息确认","您确认通过吗？ ");
	$.ajax({
		url : controllername+"?updateGcbMxzt&ids='"+gcbtqkid+"',&type="+type,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(response) {
			xSuccessMsg("操作成功","");
			$("#btnSuccess").attr("disabled",true);
			$("#btnFail").attr("disabled",true);
		}
	});
}

function tr_click(obj,tabListid){
	$("#btnHtView").attr("disabled", false);
}

//页面默认参数
function init(){
	
	if(type == "detail"){

		
		
		var parentmain=$(window).manhuaDialog.getParentObj();	
		var rowValue = parentmain.$("#resultXML").val();
		var tempJson = convertJson.string2json1(rowValue);
		
		//表单赋值
		$("#demoForm").setFormValues(tempJson);
		
		$("#QGCBTQKID").val(tempJson.GCBTQKID);
		gcbtqkid = tempJson.GCBTQKID;
		$("#QID").val(tempJson.ID);
		var zt = tempJson.GCBZT;

		if(zt!='0'){
			$("#btnSuccess").hide();
			$("#btnFail").hide();
		}
		//alert(JSON.stringify(tempJson));
		//查询记录数
		
		//表单赋值
		var data = combineQuery.getQueryCombineData(queryForm,frmPost);
		var data1 = {
			msg : data
		};
		$.ajax({
			url : controllernameTqkBm+"?query",
			data : data1,
			cache : false,
			async :	false,
			dataType : "json",  
			type : 'post',
			success : function(response) {
				$("#resultXML").val(response.msg);
				var resultobj = defaultJson.dealResultJson(response.msg);
				$("#gcZjglTqkbmForm").setFormValues(resultobj);
				lxtype = resultobj.QKLX;
				if($("#QKLX").val()!='16'){
					$("#sgty").remove();
				}
			}
		});
		
		//查询明细
		g_bAlertWhenNoResult = false;
		var dataMx = combineQuery.getQueryCombineData(queryFormMx,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryMx",dataMx,DT1);
			
		
		g_bAlertWhenNoResult = true;
		
		$("#btnInsert").attr("disabled", false);
		$("#btnSendToBmMaster").attr("disabled", false);
		$("#btnImport").attr("disabled", false);
	}
	//$("#btnWctz").hide();
}

function doCheckBox(obj){
	if(obj.GCBSHMXZT==0){
		return "<input type='checkbox' name='gcbMxId' id='gcbMxId' value='"+obj.ID+"'/>"
	}else{
		return "<input type='checkbox' name='gcbMxId' id='gcbMxId' value='"+obj.ID+"' disabled/>"
	}
	
	
}
//状态颜色判断
function docolor(obj)
{
<%--	TQKZT	提请款状态--%>
<%--	1	待审核		新建--%>
<%--	2	已通过		提交领导审核--%>
<%--	3	已退回		部门领导退回--%>
	var xqzt=obj.GCBSHMXZT;
	if(xqzt=="0"){
		return '<span class="label label-warning">'+obj.GCBSHMXZT_SV+'</span>';
	}else if(xqzt=="1"){
	  	return '<span class="label label-success">'+obj.GCBSHMXZT_SV+'</span>';
	}else if(xqzt=="2"){
	  	return '<span class="label label-danger">'+obj.GCBSHMXZT_SV+'</span>';
	}else if(xqzt=="3"){
	  	return '<span class="label label-danger">'+obj.GCBSHMXZT_SV+'</span>';
	}
}
function closeNowCloseFunction(){
	return msgControl;
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise" style="display:black;">
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
      <div class="B-small-from-table-autoConcise" style="display:black;">
      <form method="post" id="queryFormTqkBmMx"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
			<TD class="right-border bottom-border">
				<input class="span12" type="text" id="BmMxID" name="ID"  fieldname="ID" />
				<input type="hidden" id="BmMxTQKID" name="TQKID"  fieldname="TQKID" />
			</TD>
        </TR>
      </table>
      </form>
      </div>
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">部门提请款
       <span class="pull-right">
	      	<%
		    	if(type.equals("detail")){
			%>
      			<button id="btnSuccess" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">审批通过</button>
      			<button id="btnFail" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" >退回</button>
	  		<%
				}
			%>
      	</span>
      </h4>
     
     <form method="post" id="gcZjglTqkbmForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
      	<input type="hidden" id="HTID"  name = "HTID"/></TD>
      	<input type="hidden" id="TQKZT" fieldname="TQKZT" name = "TQKZT" value="1"/></TD>
      	
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">部门</th>
			<td width="17%" class="right-border bottom-border">
				<select type="text" fieldname="SQDW" name="SQDW" id="SQDW" kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME" disabled="disabled"></select></td>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">请款类型</th>
			<td width="17%" class="right-border bottom-border">
				<select  style="width:25%;" id="QKLX" onchange="change_show(this);"  placeholder="必填" check-type="required" class="span6"  name="QKLX" fieldname="QKLX"  operation="=" kind="dic" src="QKLX"  defaultMemo="请选择" disabled="disabled">
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">请款年份</th>
			<td width="17%" class="right-border bottom-border">
				<select style="width:25%;" id="QKNF"   placeholder="必填" check-type="required" class="span6"  name="QKNF" fieldname="QKNF"  operation="=" kind="dic" src="XMNF" defaultMemo="请选择" disabled="disabled">
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">请款月份</th>
			<td width="17%" class="right-border bottom-border">
				<select  style="width:25%;" id="GCPC"   class="span6"  name="GCPC" fieldname="GCPC" kind="dic" src="YF"  defaultMemo="请选择" disabled="disabled">
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">编制日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BZRQ" style="width:40%" class="span9" fieldtype="date" fieldformat="YYYY-MM-DD"  placeholder="必填" check-type="required"  name="BZRQ" fieldname="BZRQ" type="date" disabled="disabled"/>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">部门办理人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BMBLRID"  style="width:70%;"  class="span12" check-type="maxlength" maxlength="36"  name="BMBLRID" fieldname="BMBLRID" type="text" disabled />
			</td>
		</tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right disabledTh">备注</th>
	        <td colspan="3" class="bottom-border right-border" >
	        	<textarea class="span12" rows="2" id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ" disabled="disabled"></textarea>
	        </td>
        </tr>
      </table>
      </form>
      
       <div style="height:5px;"></div>
      
      <h4 class="title">部门提请款明细
      	<span class="pull-right">
      		<button id="btnHtView" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">合同信息</button>
    	</span>
      	 <form method="post" id="queryFormMx">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<input class="span12" type="text" id="QGCBTQKID" name="GCBTQKID"  fieldname="GCBTQKID" value="" operation="=" >
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
				</table>
	</form>
      </h4>
    <div class="overFlowX">
	           <table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" pageNum="10">
				<thead>
					<tr>
		 			    <th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
		<%--			   <th fieldname="GCBSHMXZT" colindex=2 tdalign="center" CustomFunction="docolor">&nbsp;&nbsp;</th>--%>
						<th fieldname="DWMC" colindex=2 tdalign="center" maxlength="30" >&nbsp;收款单位&nbsp;</th>
						<th fieldname="XMMCNR" colindex=3 tdalign="left" maxlength="30" >&nbsp;项目内容&nbsp;</th>
						<th fieldname="HTBM" colindex=4 tdalign="center" maxlength="30" >&nbsp;合同编码&nbsp;</th>
						<th fieldname="HTLX" colindex=5 tdalign="center" maxlength="30" >&nbsp;合同类型&nbsp;</th>
						<th fieldname="ZXHTJ" colindex=6 tdalign="right" >&nbsp;最新合同价(元)&nbsp;</th>
						<th fieldname="ZWCZF" colindex=7 tdalign="right" >&nbsp;完成投资(元)&nbsp;</th>
						<th id="sgty" fieldname="JZQR" colindex=9 tdalign="right" >&nbsp;监理确认计量款(元)&nbsp;</th>
						<th fieldname="YBF" colindex=10 tdalign="right" >&nbsp;已拨付(元)&nbsp;</th>
						<th fieldname="BCSQ" colindex=11 tdalign="right" >&nbsp;部门申请值(元)&nbsp;</th>
		<%--				<th fieldname="LJBF" colindex=12 tdalign="center" >&nbsp;累计拔付&nbsp;</th>--%>
						<th fieldname="AHTBFB" colindex=12 tdalign="right" >&nbsp;累计付款比例(%)&nbsp;</th>
						<th fieldname="BZ" colindex=13 tdalign="center" maxlength="30" >&nbsp;备注&nbsp;</th>
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
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
<script>
</script>
</html>