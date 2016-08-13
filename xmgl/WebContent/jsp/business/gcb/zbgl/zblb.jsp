<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%
	String xmid= request.getParameter("xmid");
	String bdid= request.getParameter("bdid");
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String userid = user.getAccount();
	String username = user.getName();
	String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<app:base/>
<app:dialogs></app:dialogs>
<title>工程周报管理</title>
<script type="text/javascript" charset="utf-8">
 
var controllername= "${pageContext.request.contextPath }/gczb/gczbController.do";
var xmid='<%=xmid%>',xmmc,json,xmbh,GC_JH_SJ_ID,bdid;

bdid='<%=bdid%>';
$("#XMGLGS").val('<%=deptId%>');
//初始化查询
$(document).ready(function(){
	//生成json串
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	if(bdid==null||bdid=="null"||bdid=='undefined'||bdid=='')
	{
		defaultJson.doQueryJsonList(controllername+"?query_tj&xmid="+xmid,data,DT1,null,true);	
	}
	else
	{
		defaultJson.doQueryJsonList(controllername+"?query_tj&bdid="+'<%=bdid%>',data,DT1,null,true);
	}	

    
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		  if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
		      printTabList("DT1");
		  }
	});	
});


//判断标段是否有值
function pdbd(obj){
	var bdid = obj.BDID;
	if(bdid=='undefinde'||bdid=='')
	{
		return '<div style="text-align:center">—</div>'
	}	
}


//现场图片图标
function viewimg(obj){
	var isfj=obj.XCZP_SV;
	if(isfj!='' && isfj!=undefined)
	{	
		return '<a href="javascript:void(0);"><img src=\"/xmgl/images/icon-annex.png\" title="现场照片" onclick="showPic(\''+obj.XCZP+'\')"></a>';
	}	
}


//现场照片
function showPic(ywid)
{
	$(window).manhuaDialog({"title":"现场照片","type":"text","content":g_sAppName +"/jsp/business/gcb/zbgl/viewimg.jsp?ywid="+ywid+"&lb=0311","modal":"2"});
}


//判断标段编号是否有值
function pdbdbh(obj){
	var bdid = obj.BDID;
	if(bdid=='undefinde'||bdid=='')
	{
		return '<div style="text-align:center">—</div>'
	}	
}

function doView(obj){
	var id = obj.XCZP;
	var tempJson = convertJson.string2json1(JSON.stringify(obj));
	return '<a href="javascript:void(0);"><i title="周报信息卡" class="icon-file" onclick="showZbxx()"></i></a>';
}
function showZbxx(){
	var index = $(event.target).closest("tr").index();
	$("#DT1").cancleSelected();
	$("#DT1").setSelect(index);
	$("#resultXML").val($("#DT1").getSelectedRow());
	$(window).manhuaDialog({"title":"工程周报>详细信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/zbgl/zbxx_view.jsp?","modal":"2"});
}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">
				周报列表
				<span class="pull-right">
					<button class="btn" id="btnExpExcel">导出</button>
				</span>	
			</h4>						
			<div class="overFlowX"> 
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" pageNum="9" type="single">
	                <thead>
						<tr>
							<th  name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
							<th fieldname="XMBH" rowspan="2" colindex=2 CustomFunction="doView">&nbsp;&nbsp;</th>
							<th fieldname="XMBH" rowspan="2" colindex=3>&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" rowspan="2" colindex=4 maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDBH" rowspan="2" colindex=5 CustomFunction="pdbdbh">&nbsp;标段编号&nbsp;</th>
							<th fieldname="BDMC" rowspan="2" colindex=6 maxlength="15" CustomFunction="pdbd">&nbsp;标段名称&nbsp;</th>
							<th fieldname="XCZP" rowspan="2" colindex=7 tdalign="center" noprint="true" CustomFunction="viewimg">&nbsp;现场照片&nbsp;</th>
							<th fieldname="QTWJ" rowspan="2" colindex=8 tdalign="center" noprint="true">&nbsp;其他文件&nbsp;</th>							
 							<th fieldname="KSSJ" rowspan="2" colindex=9 tdalign="center">&nbsp;开始时间&nbsp;</th>
							<th fieldname="JSSJ" rowspan="2" colindex=10 tdalign="center">&nbsp;结束时间&nbsp;</th>
 							<th colspan="4">工程形象进度</th>
							<th fieldname="XZJH" rowspan="2" maxlength="15" colindex=15>&nbsp;下周计划&nbsp;</th>
							<th colspan="3">排迁形象进度</th>
							<th colspan="3">拆迁形象进度</th>
							<th colspan="3">周计量（万元）</th>
							<th colspan="5">存在问题及建议完成期限</th>
							<th fieldname="NOTE" rowspan="2" colindex=30 maxlength="15">&nbsp;备注&nbsp;</th>
						</tr>
						<tr>
							<th fieldname="BZJH" maxlength="15" colindex=11>&nbsp;本周计划&nbsp;</th>
							<th fieldname="BZWC" maxlength="15" colindex=12>&nbsp;本周完成&nbsp;</th>
							<th fieldname="BNWC" maxlength="15" colindex=13>&nbsp;本年完成&nbsp;</th>
							<th fieldname="LJWC" maxlength="15" colindex=14>&nbsp;累计完成&nbsp;</th>
							<th fieldname="GXMC" colindex=16 maxlength="15">&nbsp;管线名称&nbsp;</th>
							<th fieldname="PQWCSJ" colindex=17>&nbsp;完成时限&nbsp;</th>
							<th fieldname="PABZJZ" colindex=18 maxlength="15">&nbsp;本周进展&nbsp;</th>
							<th fieldname="CQWMC" colindex=19 maxlength="15">&nbsp;拆迁物名称&nbsp;</th>
							<th fieldname="CQWCSJ" colindex=20>&nbsp;完成时限&nbsp;</th>
							<th fieldname="CQBZJZ" colindex=21 maxlength="15">&nbsp;本周进展&nbsp;</th>
							<th fieldname="ZJLBZ" colindex=22 tdalign="right">&nbsp;本周完成&nbsp;</th>
							<th fieldname="ZJLND" colindex=23 tdalign="right">&nbsp;本年完成&nbsp;</th>
							<th fieldname="ZJLLJWC" colindex=24 tdalign="right">&nbsp;累计完成（含总工程量）&nbsp;</th>
							<th fieldname="QQWT" colindex=25 maxlength="15">&nbsp;前期问题&nbsp;</th>
							<th fieldname="HTZJWT" colindex=26 maxlength="15">&nbsp;合同、造价问题&nbsp;</th>
							<th fieldname="SJWT" colindex=27 maxlength="15">&nbsp;设计问题&nbsp;</th>
							<th fieldname="ZCWT" colindex=28 maxlength="15">&nbsp;征拆问题&nbsp;</th>
							<th fieldname="PQWT" colindex=29 maxlength="15">&nbsp;排迁问题&nbsp;</th>
						</tr>
	                </thead>
	                <tbody></tbody>
	           </table>
	        </div>     
		</div>
	</div>		
</div>  
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" id="px" fieldname="xmbh,xmbs,bdbh,kssj desc,pxh"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<input type="hidden" id="queryResult"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
	<form method="post" id="queryForm">
		<table class="B-table">
			<!--可以再此处加入hidden域作为过滤条件 -->
			<TR style="display: none;">
				<TD class="right-border bottom-border"></TD>
				<TD class="right-border bottom-border">
					<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
					<input type="hidden" id="XMGLGS" fieldname="xdk.XMGLGS" name="XMGLGS"  operation="="/>
				</TD>
			</TR>
		</table>
	</form>
</div>
</body>
</html>