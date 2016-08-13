<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="java.sql.Connection"%>
<%@ page import="com.ccthanking.framework.common.*"%>


<app:base/>
<title>项目批次</title>
<%
	Connection conn = DBUtil.getConnection();//定义连接
	String SJBH=request.getParameter("sjbh");
	String idsql="SELECT gc_tcjh_sp_id from gc_tcjh_sp WHERE sjbh='"+SJBH+"' AND SFYX = '1'";
	String[][] id = DBUtil.query(conn, idsql);
	String spxxid=id[0][0];

%>
<script type="text/javascript" charset="utf-8">

var controllername= "${pageContext.request.contextPath }/xmcbk/xmcbkxdController.do";


//初始化查询
$(document).ready(function(){
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?query_sp&spxxid=<%=spxxid%>",data,DT1,null,true);
});


//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
}


//点击获取行对象
function tr_click(obj,tabListid){
	id=obj.GC_TCJH_XMCBK_ID;
	json=$("#DT1").getSelectedRow();
	json=encodeURI(json); 
}


//详细信息
function rowView(index){
    var obj_json=$("#DT1").getSelectedRowJsonByIndex(index);//获取行json串
	var obj=convertJson.string2json1(obj_json);//获取行对象
	var id_xxxx=obj.GC_TCJH_XMCBK_ID;//取行对象<项目编号>
	zt=obj.ISXD;
	xdlx=obj.XDLX;
	var pcid=obj.PCID;
	var pch=obj.PCH;//取行对象<项目批次号>
	$(window).manhuaDialog({"title":"项目储备库  >详细信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmcbk/xmxx_ty.jsp?id="+id_xxxx,"modal":"1"});
}
</script> 
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	</p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">项目信息</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">	
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">	
							<INPUT id="num" type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<="/>
							<%-- <INPUT id="SJBH" type="text" class="span12" kind="text" fieldname="pcb.SJBH" value="<%=SJBH %>" operation="="/> --%>
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
				</table>
			</form>
			<div style="height:5px;"> </div>
			<div class="overFlowX"> 
		        <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" pageNum="10">
		        	<thead>
	                    <tr>
	                     	<th id="_XH" name="XH" tdalign="center">&nbsp;#&nbsp;</th>	
	                     	<th fieldname="XMBH" hasLink="true" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="XMLX" tdalign="center">&nbsp;项目类型&nbsp;</th>
							<th fieldname="ISBT" tdalign="center">&nbsp;BT&nbsp;</th>
							<th fieldname="XMDZ" maxlength="15" tdalign="left">&nbsp;项目地址&nbsp;</th>
							<th fieldname="JHZTZE" tdalign="right">&nbsp;年度总投资额（万元）&nbsp;</th>
							<th fieldname="QY" maxlength="15">&nbsp;项目来源&nbsp;</th>
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
		<input type="hidden" name="txtFilter" order="asc" fieldname ="xmbh"/>
		<input type="hidden" name="resultXML"/>
		<input type="hidden" name="queryResult" id="queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
	</FORM>
</div>
</body>
</html>