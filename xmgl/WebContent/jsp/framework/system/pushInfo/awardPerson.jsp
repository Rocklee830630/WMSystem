<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%
	String id = request.getParameter("id");
	String name = request.getParameter("name");
/* 	UserService userService = new UserServiceImpl(); */
/* 	String[][] roleInfoArray = userService.queryPersonRole(id); */

	String sql = "SELECT OP.ACCOUNT,PP.USERID,OP.NAME, " 
			+ "E.DEPT_NAME DEPT,OP.DEPARTMENT " 
			+ "FROM FS_ORG_PERSON OP, FS_PUSHINFO_PSN PP,VIEW_YW_ORG_DEPT e "
			+ "WHERE OP.ACCOUNT=PP.USERID(+) and OP.Flag='1' and E.ROW_ID = OP.DEPARTMENT " 
			+ "AND PP.OPERATOR_NO(+)='" + id + "' ORDER BY E.SORT,OP.DEPARTMENT,OP.ACCOUNT ";

	String[][] roleInfoArray = DBUtil.query(sql);
	int spLen = 0;
	int menuLen = 0;
	String dept_name = "";
	int kk = 1;
%>
<app:base/>

<title>实例页面-查询</title>

<script type="text/javascript" charset="UTF-8">
 	var id = "<%=id%>";
 	var name = "<%=name %>";
  	var controllername= "${pageContext.request.contextPath }/pushInfoController.do";
	
	//页面默认参数
	$(document).ready(function() {
        
	}); 
	
	function clickSaveBtn(n)
	{
		if(name == "null") {
			requireFormMsg("业务有子节点，禁止对业务进行配置");
			return ;
		}
		var checkObj = $(":checkbox");
		var checkValue = "";
		for(var i = 0; i < checkObj.length; i++) {
			if(checkObj[i].checked == true) {
				checkValue += checkObj[i].value + ",";
			}
		}
//	alert(checkValue + " | ");

		var success = defaultJson.doInsertJson(controllername + "?savePushInfoMap&id=<%=id%>&name=<%=name%>&checkValue="+checkValue, null, null);
		
	}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
		<%-- <b style="font-size:16px;">当前选择的角色：</b><%="（"+name+"）"%> --%>
		<span class="pull-right">
		    	<button class="btn" id="saveUserInfo<%=kk%>" onclick="clickSaveBtn(<%=kk%>)">保存</button>
		</span>
		<br><br>
		
		<div style="height:450px;overflow:auto;">
		<table class="B-table" width="100%">

		<%
			// 每行显示的个数
			int num = 5;
			// 角色总数
			int line = 0;
			int cols = 1;
			for(int i = 0; i < roleInfoArray.length; i++) {
				if(!(dept_name.equals(roleInfoArray[i][3])))
				{
			%>
			<tr>
			<th colspan="<%=num*2%>" class="right-border bottom-border text-right" style="border-left:#ddd solid 1px;border-top:#ddd solid 1px">
			<span class="pull-left" style="font-size:16px;"><%=((roleInfoArray[i][3]==null||"".equals(roleInfoArray[i][3]))?"未知部门":roleInfoArray[i][3])%></span>
		    
			</th>
			</tr>
				<%
					dept_name = roleInfoArray[i][3];
					line = 0;
					cols=1;
					kk++;
				}

				{
					if(cols==1)
					{
				%>
					<tr>
					 <th class="right-border bottom-border text-right" style="border-left:#ddd solid 1px"><%=(line+1)%></th>
				
				<%  }

						%>
							<td class="left-border bottom-border" style="text-align: left;">&nbsp;&nbsp;
						<%
							if(roleInfoArray[i][0].equals(roleInfoArray[i][1])) {
						%>
								<input type="checkbox" id="name" name="name" value="<%=roleInfoArray[i][0] %>=<%=roleInfoArray[i][4] %>" checked="checked"/>&nbsp;&nbsp;<font color="red"><%=roleInfoArray[i][2] %>（<%=roleInfoArray[i][0] %>）</font>
						<%
							} else {
						%>
								<input type="checkbox" id="name" name="name" value="<%=roleInfoArray[i][0] %>=<%=roleInfoArray[i][4] %>"/>&nbsp;&nbsp;<font color="#000000"><%=roleInfoArray[i][2] %>（<%=roleInfoArray[i][0] %>）</font>
						<%		
							}
						%>
							
							</td>
						<%			
					if(cols==num)
					{ 
						
					%>
					</tr>
					<%
					}
					cols++;
					if(cols>num)
					{
						cols = 1;
						line++;
					}
				}
			}
			
			if(cols>1&&cols<num+1)
			{
				for(int k=num;k>=cols;k--)
				{%>
					<td class="left-border bottom-border"></td>
			  <%}%>
			  </tr>
			  <%
			}
		%>
		</table>
		</div>
		</div>	
	</div>
</div>

</body>
</html>