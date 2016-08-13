<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.service.UserService" %>
<%@ page import="com.ccthanking.framework.service.impl.UserServiceImpl" %>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%
	String id = request.getParameter("id");
	String name = request.getParameter("name");
	UserService userService = new UserServiceImpl();
	String[][] roleInfoArray = userService.queryPersonRole(id);
	int spLen = 0;
	int menuLen = 0;
	String dept_name = "";
	int kk = 1;
%>
<app:base/>

<title>实例页面-查询</title>

<script type="text/javascript" charset="UTF-8">
 	var id = "<%=id%>";
  	var controllername= "${pageContext.request.contextPath }/userController.do";
	
	//页面默认参数
	$(document).ready(function() { 
        
	}); 
	
	function clickSaveBtn(n)
	{
		var checkObj = $(":checkbox");
		var checkValue = "";
		for(var i = 0; i < checkObj.length; i++) {
			if(checkObj[i].checked == true) {
				checkValue += checkObj[i].value + ",";
			}
		}
//	alert(checkValue + " | " + account);
		var success = defaultJson.doInsertJson(controllername + "?awardRoleToPerson&roleid=<%=id%>&rolename=<%=name%>&checkValue="+checkValue, null, null);
		if(success == true) {
		
			//parent.$("body").manhuaDialog.close();
		}
	}
	function clickCancelBtn()
	{
		$(window).manhuaDialog.close();
	}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<b style="font-size:16px;">当前选择的角色：</b><%="（"+name+"）"%>
			<!-- <h4 class="title">
				当前项目：<font style=" margin-left:5px; margin-right:5px;color:gray;"></font>
				<span class="pull-right">  
					<button class="btn" id="example_save">保存</button> 
				</span>
			</h4> -->
			
		    <span class="pull-right">
		    	<button class="btn" id="saveUserInfo<%=kk%>" onclick="clickSaveBtn(<%=kk%>)">保存</button>
		    	<button class="btn" id="btnCancel<%=kk%>" onclick="clickCancelBtn(<%=kk%>)">关闭</button>
		    </span>
			<br></br>
			<div style="height:450px;overflow:auto;">
				<table class="B-table"  style="width: 100%">
		
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
						<th colspan="<%=num*2%>">
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
							 <th><%=(line+1)%></th>
						
						<%  }
		
								%>
									<td class="left-border bottom-border" style="text-align: left">&nbsp;&nbsp;
								<%
									if(roleInfoArray[i][0].equals(roleInfoArray[i][1])) {
								%>
										<input type="checkbox" id="name" name="name" value="<%=roleInfoArray[i][0] %>=<%=roleInfoArray[i][2] %>" checked="checked"/>&nbsp;&nbsp;<font color="red"><%=roleInfoArray[i][2] %></font>
								<%
									} else {
								%>
										<input type="checkbox" id="name" name="name" value="<%=roleInfoArray[i][0] %>=<%=roleInfoArray[i][2] %>"/>&nbsp;&nbsp;<font color="#000000"><%=roleInfoArray[i][2] %></font>
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