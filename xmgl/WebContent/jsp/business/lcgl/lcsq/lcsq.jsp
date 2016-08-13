<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="java.sql.*"%>
<app:base/>
<%
	User user = (User) session.getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String roleArray = user.getRoleListIdString();
	String sjbh = request.getParameter("sjbh") == null ? "" :request.getParameter("sjbh");
	String [][]rsconf = null;
	Connection conn = DBUtil.getConnection();
	QuerySet qs = null;
	boolean auto = false;
	try{
		String sql = "";

		if(!"".equals(sjbh)){
			//通过sjbh 获取ywlx\operationid
			String getconf = "select ywlx,operationoid from AP_PROCESSCONFIG t where sjbh = '"+sjbh+"'";
			rsconf = DBUtil.querySql(conn, getconf);
			if(rsconf != null){
				sql = "select t.ywlx,t.ws_template_id,d.name,d.operationoid,d.processtype from AP_WS_TYPZ t,AP_PROCESSTYPE d where t.operationoid = d.processtypeoid and t.ywlx = '"+rsconf[0][0]+"' and t.operationoid = '"+rsconf[0][1]+"'";
			}else{
				sql = "select t.ywlx,t.ws_template_id,d.name,d.operationoid,d.processtype from AP_WS_TYPZ t,AP_PROCESSTYPE d where t.operationoid = d.processtypeoid and ISYWLY = '0'";
			}
			 qs = DBUtil.executeQuery(sql, null);
			  auto = true;
		}else{
			sql = "select t.ywlx,t.ws_template_id,d.name,d.operationoid,d.processtype from AP_WS_TYPZ t,AP_PROCESSTYPE d where t.operationoid = d.processtypeoid and ISYWLY = '0'";
			roleArray = ("".equals(roleArray)||null==roleArray)?"''":roleArray;
			/* sql += " and rolename in ("+roleArray+")"; */
				sql += " and deptid in('100000000000','" + dept.getDeptID() + "')";	
			//sql += " and deptid in('"+dept.getDeptID()+"'";
			//while (!("100000000000".equals(dept.getDeptID()))) {
			//	dept = dept.getParent();
			//	if(dept==null)
			//		break;
			//	sql+= ",'"+dept.getDeptID()+"'";

			//}
			sql += " order by to_number(t.ws_template_id) desc ";
			System.out.println(sql);
			 qs = DBUtil.executeQuery(sql, null);
		}
		
	}catch(Exception e ){
		e.printStackTrace();
	}finally{
		if (conn != null) {
			conn.close();
		}
	}
	

%>
<title>实例页面-查询</title>

<script type="text/javascript" charset="UTF-8">

function getSJBH(templateid,ywlx,operationoid)
{
	var action1 = "${pageContext.request.contextPath }/PubWS.do?createSP&ywlx="+ywlx+"&templateid="+templateid+"&operationoid="+operationoid;
	var sjbh = null;
	$.ajax({
		url : action1,
		data : null,
		dataType : 'json',
		async :	false,
		type : 'post',
		//contentType:'application/json;charset=UTF-8',	    
		success : function(result) {
			//alert(123);
		       //xmlDoms.loadXML(result);
		      
			if(result.MSG=="1")
			{
				sjbh = result.SJBH;
			}else
			{
				sjbh = null;
			}
			
		},
	    error : function(result) {
	    	alert("创建申请失败");
	    	sjbh =  null;
	     	//alert(234);
	    }
	});
	return sjbh;
}


function getMessage(msg)
{
	xAlert("运行结果",msg);
	$(window).manhuaDialog.getParentObj().prcCallback();
}
function viewWs(wsid)
{
	//var path = "chihuo.jpg";
	$("#frame1").attr("src", "${pageContext.request.contextPath }/jsp/business/lcgl/lcsq/showPic.jsp?wsid=" + wsid);
}

function viewProcess(operationoid)
{

	$("#frame1").attr("src", "${pageContext.request.contextPath }/jsp/framework/common/aplink/StaticspFlowView.jsp?operationoid="+operationoid); 

}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
    <div class="span7">
    	<h4 class="title"></h4>
		<div class="B-small-from-table-auto">
		
		<table class="B-table">
			<tr>
				<th width="2%" class="right-border bottom-border">&nbsp;#&nbsp;</th>
				<th width="38%" class="right-border bottom-border">流程名称</th>
				<th width="60%" class="right-border bottom-border" colspan="3">操作</th>
			</tr>
			<% 	if(qs!=null && qs.getRowCount()>0)
				{
					for(int i=0;i<qs.getRowCount();i++)
					{
				%>
				<tr>
					<th class="right-border bottom-border"><%=i+1%></th>
					<td class="text-left bottom-border"><%=qs.getString(i+1,3)%></td>
					<td class="text-center bottom-border" colspan="3" align="center">
						<button	id="viewBtn<%=i%>" class="btn" onclick="creatSP('<%=qs.getString(i+1,2)==null?"":qs.getString(i+1,2)%>','<%=qs.getString(i+1,1)%>','<%=qs.getString(i+1,4)%>','<%=qs.getString(i+1,5)%>','<%=qs.getString(i+1,3)%>')" type="button">发起流程</button>
						<button	id="showPic<%=i%>" <%if(null==qs.getString(i+1,2) ||"".equals(qs.getString(i+1,2))) out.print("disabled");%> class="btn btn-link" onclick="viewWs('<%=qs.getString(i+1,2)%>')" type="button"><i class="icon-book"></i>查看文书</button>
						<button	id="contact<%=i%>"  class="btn btn-link" type="button" onclick="viewProcess('<%=qs.getString(i+1,4)%>')"><i class="icon-th-list"></i>查看流程</button></td>	
				</tr>
			<% 
					}
				}else
				{
			%>
				<tr>
					<th class="right-border bottom-border"><%=1%></th>
					<td class="text-center bottom-border" colspan="4"><b>无可用审批流程，请与系统管理员联系！</b></td>
				</tr>				
			<%	}
			%>	
		</table>
		</div>
	</div>
    <div class="span5" style="height:540px">
    	<iframe id="frame1" name="projectModel" width="100%" height="100%" src="${pageContext.request.contextPath }/jsp/business/lcgl/lcsq/showPic.jsp" style="border:0px;" framespacing=0 marginheight=0 marginwidth=0> </iframe>
    </div>
  </div>
</div>

<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id = "queryXML">
		<input type="hidden" name="txtXML" id = "txtXML">
		<input type="hidden" name="txtFilter"  order="desc" fieldname = "XMNF"	id = "txtFilter">
		<input type="hidden" name="resultXML" id = "resultXML">
		<input type="hidden" name="queryResult" id = "queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">
	</FORM>
</div>
<script type="text/javascript" charset="UTF-8">
var auto = '<%= auto%>';
if(auto){
	//alert(auto);
	$("viewBtn0").click();

}
$(function() {
	var btn = $("viewBtn0");
	btn.click(function() {
		var templateid = '<%=qs.getString(1,2)==null?"":qs.getString(1,2)%>';
		var ywlx = '<%=qs.getString(1,1)%>';
		var operationoid = '<%=qs.getString(1,4)%>';
		var processtype = '<%=qs.getString(1,5)%>';
		//alert(ywlx);
		creatSP(templateid,ywlx,operationoid,processtype);
		 
		});
});
function creatSP(templateid,ywlx,operationoid,processtype,processname) {
	var sjbh = "";
	sjbh = getSJBH(templateid,ywlx,operationoid);
	//alert("sjbh=="+sjbh);
	if(sjbh==null ||sjbh == undefined || sjbh =="")
		return;
    var wsActionURL = "${pageContext.request.contextPath }/PubWS.do?getXMLPrintAction|templateid="+templateid+"|isEdit=1|ywlx="+ywlx+"|sjbh="+sjbh+"|rowid="+Math.random()+".mht";
    wsActionURL = "${pageContext.request.contextPath }/jsp/framework/print/pubPrint.jsp?param="+wsActionURL+
    		"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid="+templateid+"&isEdit=1"+"&operationoid="+operationoid+"&processtype="+processtype;
    $(window).manhuaDialog({"title":"流程申请>"+processname+"","type":"text","content":wsActionURL,"modal":"1"});
}
</script>
</body>
</html>