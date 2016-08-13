<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.business.clzx.service.ClzxManagerService" %>
<%@ page import="com.ccthanking.business.clzx.service.impl.ClzxManagerServiceImpl" %>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%
	String id = request.getParameter("id");
	String name = request.getParameter("name");
	ClzxManagerService service = new ClzxManagerServiceImpl();
	String[][] ywry = service.queryYwry(id);
%>
<app:base/>
<title>业务人员</title>  
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise" style="height: 100%;">
			<div style="height: 100%;overflow:auto;" id="nameall">
			
			<%
				if(id!=null)
				{
			%>		
				<table id="TD1" class="B-table"  style="width: 100%;height: 100%;">
					<th>
					<span class="pull-left" style="font-size:16px;"><%=name %></span>
					<button style="float:right;"class="btn" id="btnSave">保存</button> 
					</th>
				</table>
			<%}	
				if(ywry!=null&&ywry.length>0&&ywry[0][0]!=null)
				{
					for(int i=0;i<ywry.length;i++)
					{
			%>
						<div style="border:1px solid #DDDDDD;margin-right:5px;margin-top:10px;width: 30%;height:18%;float:left;text-align:center;" account="<%=ywry[i][1]%>"><font style="font-size:14px;color:#000000;"><%=ywry[i][0] %></font><i style="float:right;" class="icon-remove"></i></div>
			<%		
					}
				}
			%>
			</div>
			<div style="height:100%;border:1px solid #DDDDDD;margin-top:5px;"></div>
			<input type="hidden" value="<%=id%>" id="hidid"></input>
		</div>	
	</div>
</div>
<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/clzx/clzxManageControllor.do";
//点击保存按钮
$(function() {
	$("#btnSave").click(function() {
		var div_obj=$("#nameall").find("div");
		if(div_obj.length==0)
		{
			return;
		}
		var rybids='';
		for(var i=0;i<div_obj.length;i++)
		{
			rybids+=$(div_obj[i]).attr("account")+","
		}
		success=defaultJson.doInsertJson(controllername + "?insert_clzx&ids="+rybids+"&ywid=<%=id%>", null,null);

	});
	$("#nameall").find("i").click(function(e) {
        $(this.parentElement).remove();
    });
});

</script>
</body>
</html>