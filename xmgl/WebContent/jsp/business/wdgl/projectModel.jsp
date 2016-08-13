<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.common.QuerySet"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%
	String id = request.getParameter("id");
	QuerySet qs = null;
	if(id != null) {
		String sql = "select d1.dcslayer,d1.dcslayername,d4.prjno,d2.acconame,d2.accnname "+
		  		" from tpj_docclass d1, TAC_ACCESSORY d2,TPJ_DOCUMENT d3,TPJ_PROJECT d4 "+
		 		" where d1.dcsid = d3.docdcsid "+
		 		" and d1.dcsprjid = d4.prjid  "+
		 		" and d3.docid = d2.accbelongid "+
		 		" and d2.accdir = 'Project' "+
		 		" and d2.acctype = '工程文档' "+
		 		" and d4.prjid = "+id+
		 		" order by d1.dcsparentid, d1.dcsid";
		qs = DBUtil.executeQuery(sql, null);
	}
%>
<app:base/>
<title>Insert title here</title>
<script type="text/javascript">
//初始化加载
$(document).ready(function(){
	var cnt = <%=qs == null ? "" : qs.getRowCount() %>;
	if(cnt == 0) {
		xAlert("查询结果","查询无记录",'3');
	}
});
	//查看附件方法
	function checkupFiles(m,n,o){
		var obj = $(document).find("#previewFileid");
		$("#previewMethod").val("history");
		$("#previewName").val(m);
		$("#previewType").val(n);
		$("#previewDir").val(o);
		window.open(encodeURI("${pageContext.request.contextPath }/jsp/file_upload/showPreview.jsp"));
	}
</script>
</head>
<body>
<app:dialogs/>


<div class="container-fluid">
  <div class="row-fluid">
    
    <div class="span7" style="width: 100%">
    	<div class="B-small-from-table-autoConcise">
			<h4 class="title">工程管理 <small>-文档查看</small></h4>
			<table style="width: 100%" class="B-table" border="1">
				<tr>
					<th width="5%">&nbsp;#&nbsp;</th>
					<th width="15%">&nbsp;阶段&nbsp;</th>
					<th width="32%">&nbsp;资料内容&nbsp;</th>
					<th width="15%">&nbsp;文档编号&nbsp;</th>
					<th width="32%">&nbsp;文档名称&nbsp;</th>

				</tr>
				<% 
					if(qs != null && qs.getRowCount()>0)
				   	{
						for(int i=0;i<qs.getRowCount();i++)
						{
							String jd = qs.getString(i+1, 2);
							String tempstr[] = "".equals(jd) ? new String[0] : jd.split("\\\\");
							
				%>
						<tr>
							<td><%=i+1%></td>
				<%
							if(tempstr.length != 4) {
				%>	
							<td>文档</td>
							<td>文档</td>
				<%			
							} else {
				%>	
							<td><%=tempstr[2]%></td>
							<td><%=tempstr[3]%></td>
				<%
							}
				%>
				
							<td><%=qs.getString(i+1, 3)%></td>
							<td><a id="AccHrefj<%=i+1%>" style="CURSOR: hand; TEXT-DECORATION: none" onclick="checkupFiles('<%=qs.getString(i+1, 5)%>','<%=qs.getString(i+1, 4)%>','Project')" href="javascript:void(0);" target="_black"><%=qs.getString(i+1, 4)%></a></td>
						</tr>		
				<%				
						}
				   	}
				%>

				
			</table>
		</div>
    </div>
  </div>
	<div align="center">
		<FORM name="frmPost" method="post" style="display: none"
			target="_blank">
			<input type="hidden" name="previewFileid" id="previewMethod">
			<input type="hidden" name="previewFileid" id="previewName">
			<input type="hidden" name="previewFileid" id="previewType">
			<input type="hidden" name="previewFileid" id="previewDir">
		</FORM>
	</div>
</div>
</body>
</html>