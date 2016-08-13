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
String id=request.getParameter("id");
//获取当前用户信息
User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
OrgDept dept = user.getOrgDept();
String deptId = dept.getDeptID();
String deptName = dept.getDept_Name();
String userid = user.getAccount();
String username = user.getName();
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglTqkbmController.do";
var controllernameTqkMx= "${pageContext.request.contextPath }/zjgl/gcZjglTqkbmmxController.do";
var type ="<%=type%>", idvar="<%=id%>";
var flag,jbrID, lxtype;

//页面初始化
$(function() {
	init();
	
	//置所有input 为disabled
	$(":input").each(function(i){
	   $(this).attr("disabled", "true");
	 });
	
	$("#example_query").attr("disabled", false);

	$("#btnHtView").click(function(){
		  var values = "";
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
		  
		  $("#resultXML").val(values);
		  //var rowValue = $("#DT1").getSelectedRow();
		  //var tempJson = convertJson.string2json1(values);
		   //var idvar = tempJson.ID;
		  $(window).manhuaDialog({"title":"部门提请款>合同详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/ht-view-tqkmx.jsp?type=detail","modal":"1"});
		     
		    });
});


//页面默认参数
function init(){
		
		//表单赋值
		$("#QTQKID").val(idvar);
		$("#QID").val(idvar);
		
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
				$("#gcZjglTqkbmForm").setFormValues(resultobj);
				lxtype = resultobj.QKLX;
			}
		});
		
		
		//查询明细
		//查询明细
		g_bAlertWhenNoResult = false;
		if("16"==lxtype){
			var dataMx = combineQuery.getQueryCombineData(queryFormMx,frmPost,gcZjglTqkbmList_pcl);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllernameTqkMx+"?query",dataMx,gcZjglTqkbmList_pcl);
			
			$("#id_show_pcl").show();
			$("#id_show_fgcl").hide();
			$("#id_show_pql").hide();
		}else if("14"==lxtype){
			var dataMx = combineQuery.getQueryCombineData(queryFormMx,frmPost,gcZjglTqkbmList_pql);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllernameTqkMx+"?query",dataMx,gcZjglTqkbmList_pql);
			$("#id_show_pcl").hide();
			$("#id_show_fgcl").hide();
			$("#id_show_pql").show();
		}else{
			var dataMx = combineQuery.getQueryCombineData(queryFormMx,frmPost,gcZjglTqkbmList);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllernameTqkMx+"?query",dataMx,gcZjglTqkbmList);
			$("#id_show_pcl").hide();
			$("#id_show_pql").hide();
			$("#id_show_fgcl").show();
		}
		g_bAlertWhenNoResult = true;
		
}

function tr_click(obj,tabListid){
	 $("#btnHtView").attr("disabled", false);
}
function doRandering_cwze(obj){
	var showHtml = "";
	if(obj.CWSHZ!=""){
		showHtml = obj.CWSHZ_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_jcze(obj){
	var showHtml = "";
	if(obj.JCHDZ!=""){
		showHtml = obj.JCHDZ_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}

function printDiv()
{
	window.print();
}

//状态颜色判断
function docolor(obj)
{
	var xqzt=obj.BMTQKMXZT;
	if(xqzt=="5"){
	 	return '<span class="label label-success">'+obj.BMTQKMXZT_SV+'</span>';
	}else if(xqzt=="6"){
	 	return '<span class="label label-success">'+obj.BMTQKMXZT_SV+'</span>';
	}else if(xqzt=="7"){
	 	return '<span class="label label-success">'+obj.BMTQKMXZT_SV+'</span>';
	}else if(xqzt=="3"){
	 	return '<span class="label label-danger">'+obj.BMTQKMXZT_SV+'</span>';
	}else if(xqzt=="4"){
	 	return '<span class="label label-danger">'+obj.BMTQKMXZT_SV+'</span>';
	}else{
		return '<span class="label label-danger">未处理</span>';
	}
	
}
function doDWMC(obj){
	if(obj.DWMC==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.DWMC;
	}
}
function doXMMCNR(obj){
	if(obj.XMMCNR==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.XMMCNR;
	}
}
function doHTBM(obj){
	if(obj.HTBM==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.HTBM;
	}
}
function doCWSHZ(obj){
	if(obj.CWSHZ==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.CWSHZ_SV;
	}
}
function doJCHDZ(obj){
	if(obj.JCHDZ==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.JCHDZ_SV;
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
      		<button id="example_query" class="btn btn-link" type="button" onclick="printDiv()"><i class="icon-print"></i>打印</button>
      	</span>
      </h4>
     <form method="post" id="gcZjglTqkbmForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
      	<input type="hidden" id="TQKZT" fieldname="TQKZT" name = "TQKZT" value="1"/></TD>
      	
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">部门</th>
			<td width="17%" class="right-border bottom-border">
				<select type="text" fieldname="SQDW" name="SQDW" id="SQDW" kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME" disabled="disabled"></select></td>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">请款类型</th>
			<td width="17%" class="right-border bottom-border">
				<select id="QKLX"   placeholder="必填" check-type="required" class="span6"  name="QKLX" fieldname="QKLX"  operation="=" kind="dic" disabled src="QKLX"  defaultMemo="-请选择-">
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">请款年份</th>
			<td width="17%" class="right-border bottom-border">
				<select style="width:50%;" id="QKNF"   placeholder="必填" check-type="required" class="span6"  name="QKNF" fieldname="QKNF"  disabled operation="=" kind="dic" src="XMNF" defaultMemo="-请选择-">
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">请款月份</th>
			<td width="17%" class="right-border bottom-border">
				<select  style="width:30%;" id="GCPC"   class="span6"  name="GCPC" fieldname="GCPC" kind="dic" src="YF" disabled defaultMemo="-请选择-">
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">编制日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BZRQ"  class="span9" fieldtype="date" fieldformat="YYYY-MM-DD"  placeholder="必填" check-type="required"  name="BZRQ" fieldname="BZRQ" type="date" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">部门办理人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BMBLRID"  style="width:85%;"  class="span12" check-type="maxlength" maxlength="36"  name="BMBLRID" fieldname="BMBLRID" type="text" disabled />
			</td>
		</tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right disabledTh">备注</th>
	        <td colspan="3" class="bottom-border right-border" >
	        	<textarea class="span12" rows="2" id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ"></textarea>
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
							<input class="span12" type="text" id="QTQKID" name="TQKID"  fieldname="TQKID" value="" operation="=" >
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
				</table>
	</form>
      </h4>
     
    <div class="overFlowX" id="id_show_fgcl">
    <input type="hidden" id="QMXID">
	<table class="table-hover table-activeTd B-table" id="gcZjglTqkbmList" width="100%" type="single" pageNum="10">
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
 			    <th fieldname="BMTQKMXZT" colindex=17 tdalign="center" CustomFunction="docolor">&nbsp;状态&nbsp;</th>
				<th fieldname="DWMC" colindex=4 tdalign="center" maxlength="30" CustomFunction="doDWMC">&nbsp;收款单位&nbsp;</th>
				<th fieldname="XMMCNR" colindex=5 tdalign="left" maxlength="30" CustomFunction="doXMMCNR">&nbsp;项目内容&nbsp;</th>
				<th fieldname="HTBM" colindex=6 tdalign="center" maxlength="30" CustomFunction="doHTBM">&nbsp;合同编码&nbsp;</th>
				<th fieldname="ZXHTJ" colindex=7 tdalign="right" >&nbsp;最新合同价(元)&nbsp;</th>
				<th fieldname="YBF" colindex=8 tdalign="right" >&nbsp;已付款(元)&nbsp;</th>
				<th fieldname="BCSQ" colindex=9 tdalign="right" >&nbsp;部门申请值(元)&nbsp;</th>
				<th fieldname="CWSHZ" colindex=10 tdalign="right" CustomFunction="doCWSHZ">&nbsp;财务确认申请值(元)&nbsp;</th>
				<th fieldname="JCHDZ" colindex=11 tdalign="right" CustomFunction="doJCHDZ" type="text">&nbsp;计财审定值(元)&nbsp;</th>
<%--				<th fieldname="LJBF" colindex=10 tdalign="center" >&nbsp;累计拔付&nbsp;</th>--%>
				<th fieldname="AHTBFB" colindex=11 tdalign="right" >&nbsp;累计付款比例(%)&nbsp;</th>
				<th fieldname="BZ" colindex=19 tdalign="center" maxlength="30" CustomFunction="doBZ">&nbsp;备注&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
    
    <div class="overFlowX" id="id_show_pcl" style="display:none;">
	<table class="table-hover table-activeTd B-table" id="gcZjglTqkbmList_pcl" width="100%" type="single" pageNum="10">
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
 			    <th fieldname="BMTQKMXZT" colindex=17 tdalign="center" CustomFunction="docolor">&nbsp;状态&nbsp;</th>
				<th fieldname="DWMC" colindex=4 tdalign="center" maxlength="30" CustomFunction="doDWMC">&nbsp;收款单位&nbsp;</th>
				<th fieldname="XMMCNR" colindex=5 tdalign="left" maxlength="30" CustomFunction="doXMMCNR">&nbsp;项目内容&nbsp;</th>
				<th fieldname="HTBM" colindex=6 tdalign="center" maxlength="30" CustomFunction="doHTBM">&nbsp;合同编码&nbsp;</th>
				<th fieldname="ZXHTJ" colindex=7 tdalign="right" >&nbsp;最新合同价(元)&nbsp;</th>
				<th fieldname="CSZ" colindex=12 tdalign="right" >&nbsp;财审值(元)&nbsp;</th>
				<th fieldname="CSZ" colindex=12 tdalign="right" >&nbsp;审计值(元)&nbsp;</th>
				<th fieldname="JZQR" colindex=13 tdalign="right" >&nbsp;监理确认计量款(元)&nbsp;</th>
				<th fieldname="YBF" colindex=8 tdalign="right" >&nbsp;已付款(元)&nbsp;</th>
				<th fieldname="BCSQ" colindex=9 tdalign="right" >&nbsp;部门申请值(元)&nbsp;</th>
				<th fieldname="CWSHZ" colindex=10 tdalign="right" CustomFunction="doCWSHZ">&nbsp;财务确认申请值(元)&nbsp;</th>
				<th fieldname="JCHDZ" colindex=11 tdalign="right" type="text" CustomFunction="doJCHDZ">&nbsp;计财审定值(元)&nbsp;</th>
				<th fieldname="AJLFKB" colindex=14 tdalign="right" >&nbsp;按计量付款比例(%)&nbsp;</th>
				<th fieldname="AHTBFB" colindex=11 tdalign="right" >&nbsp;累计付款比例(%)&nbsp;</th>
				<th fieldname="BZ" colindex=19 tdalign="center" maxlength="30" CustomFunction="doBZ">&nbsp;备注&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	
	<div class="overFlowX" id="id_show_pql" style="display:none;">
	<table class="table-hover table-activeTd B-table" id="gcZjglTqkbmList_pql" width="100%" type="single" pageNum="10">
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
 			    <th fieldname="BMTQKMXZT" colindex=17 tdalign="center" CustomFunction="docolor">&nbsp;状态&nbsp;</th>
				<th fieldname="DWMC" colindex=4 tdalign="center" maxlength="30" CustomFunction="doDWMC">&nbsp;收款单位&nbsp;</th>
				<th fieldname="XMMCNR" colindex=5 tdalign="left" maxlength="30" CustomFunction="doXMMCNR">&nbsp;项目内容&nbsp;</th>
				<th fieldname="HTBM" colindex=6 tdalign="center" maxlength="30" CustomFunction="doHTBM">&nbsp;合同编码&nbsp;</th>
				<th fieldname="ZXHTJ" colindex=7 tdalign="right" >&nbsp;最新合同价(元)&nbsp;</th>
				<th fieldname="CSZ" colindex=12 tdalign="right" >&nbsp;财审值(元)&nbsp;</th>
				<th fieldname="YBF" colindex=8 tdalign="right" >&nbsp;已付款(元)&nbsp;</th>
				<th fieldname="BCSQ" colindex=9 tdalign="right" >&nbsp;部门申请值(元)&nbsp;</th>
				<th fieldname="CWSHZ" colindex=10 tdalign="right" CustomFunction="doCWSHZ">&nbsp;财务确认申请值(元)&nbsp;</th>
				<th fieldname="JCHDZ" colindex=11 tdalign="right" type="text" CustomFunction="doJCHDZ">&nbsp;计财审定值(元)&nbsp;</th>
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