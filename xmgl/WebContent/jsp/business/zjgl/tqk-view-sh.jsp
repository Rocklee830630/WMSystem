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
<title>提请款-维护</title>
<%
	String type=request.getParameter("type");
	if(type==null)type="detail";
	String sjbh =request.getParameter("sjbh");
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String userid = user.getAccount();
	String username = user.getName();
	String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglTqkController.do";
var controllernameTqkMx= "${pageContext.request.contextPath }/zjgl/gcZjglTqkmxController.do";
var controllernameTqkBmMx= "${pageContext.request.contextPath }/zjgl/gcZjglTqkbmmxController.do";
var type ="<%=type%>";
var xmrowValues;
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
	
});
//页面默认参数
function init(){
	if(type == "update" || type == "detail"){
		
		
		$("#QSJBH").val('<%=sjbh%>');
		
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
				if(resultobj.QKLX!='16'){
					$("#sygt").remove();
				}
			}
		});
		
		$("#QTQKID").val($("#ID").val());
		
		//查询明细
		var dataMx = combineQuery.getQueryCombineData(queryFormMx,frmPost,gcZjglTqkmxList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllernameTqkMx+"?query",dataMx,gcZjglTqkmxList);
		
		$("#btnInsert_Sel").attr("disabled", false);
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
				<input class="span12" type="text" id="QSJBH" name="SJBH"  fieldname="SJBH" value="" operation="=" >
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
      		<button id="btnClear_Bins" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
	  		<%
				}
			%>
      	</span>
      </h4>

     <form method="post" id="gcZjglTqkForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
      	<input type="hidden" id="TQKZT" fieldname="TQKZT" name = "TQKZT" value="0"/></TD>
		
		<tr>
			<th width="8%" class="right-border bottom-border text-right">申请部门</th>
			<td width="17%" class="right-border bottom-border">
				<select type="text" class="span12"  fieldname="SQDW" name="SQDW" id="SQDW" kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME" disabled></select>
			</td>
			<th width="8%" class="right-border bottom-border text-right">请款类型</th>
			<td width="17%" class="right-border bottom-border">
				<select onchange="setTqkName();"  id="QKLX"    placeholder="必填" check-type="required" class="span12"  name="QKLX" fieldname="QKLX"  operation="=" kind="dic" src="QKLX"  defaultMemo="-请选择-" disabled>
			</td>
			<th width="8%" class="right-border bottom-border text-right">批次</th>
			<td width="17%" class="right-border bottom-border">
					<input id="GCPC" onchange="setTqkName();" type="number" style="width:60%;text-align:right;" title="工程批次"   placeholder="必填" check-type="required" class="span12" check-type="maxlength" maxlength="40"  name="GCPC" fieldname="GCPC" disabled/>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">请款年份</th>
			<td width="17%" class="right-border bottom-border">
				<select  id="QKNF"   placeholder="必填" check-type="required" class="span12"  name="QKNF" fieldname="QKNF"  operation="=" kind="dic" src="XMNF" defaultMemo="-请选择-" disabled>
			</td>
			<th width="8%" class="right-border bottom-border text-right">请款月份</th>
			<td width="17%" class="right-border bottom-border">
				<select id="YF"   class="span12"  name="YF" fieldname="YF" kind="dic" src="YF"  defaultMemo="-请选择-" disabled>
			</td>
			<th width="8%" class="right-border bottom-border text-right">请款名称</th>
			<td width="17%" class="right-border bottom-border">
				<input id="QKMC"   placeholder="必填" check-type="required" class="span12" check-type="maxlength" maxlength="100"  name="QKMC" fieldname="QKMC" type="text" disabled/>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">编制日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BZRQ"  class="span9" fieldtype="date" fieldformat="YYYY-MM-DD"  placeholder="必填" check-type="required"  name="BZRQ" fieldname="BZRQ" type="date" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right">编制人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="CWBLRID"  class="span12" check-type="maxlength" maxlength="36"  name="CWBLRID" fieldname="CWBLRID" type="text" disabled/>
			</td>
			 <th width="8%" class="right-border bottom-border text-right">备注</th>
	        <td  width="17%" class="bottom-border right-border" >
	        	<textarea class="span12"  id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ"></textarea>
	        </td>
		</tr>
      </table>
      </form>
      
      
      <div style="height:5px;"></div>
      
      <h4 class="title">提请款明细
      	<span class="pull-right">
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
				<th fieldname="ZXHTJ" colindex=5 tdalign="center" >&nbsp;最新合同价(元)&nbsp;</th>
				<th id="sygt" fieldname="JZQR" colindex=7 tdalign="right">&nbsp;监理确认计量款(元)&nbsp;</th>
				<th fieldname="YBF" colindex=6 tdalign="center" >&nbsp;已拔付(元)&nbsp;</th>
				<th fieldname="BCSQ" colindex=7 tdalign="center" >&nbsp;本次申请拔款(元)&nbsp;</th>
				<th fieldname="LJBF" colindex=8 tdalign="center" >&nbsp;累计拔付(元)&nbsp;</th>
				<th fieldname="AHTBFB" colindex=9 tdalign="center" >&nbsp;按合同付款比例(%)&nbsp;</th>
				<th fieldname="CWSHZ" colindex=10 tdalign="center" type="text">&nbsp;财务审核值(元)&nbsp;</th>
				<th fieldname="JCHDZ" colindex=11 tdalign="center" type="text">&nbsp;计财审定值(元)&nbsp;</th>
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