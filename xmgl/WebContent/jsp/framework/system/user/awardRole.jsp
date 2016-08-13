<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.service.UserService" %>
<%@ page import="com.ccthanking.framework.service.impl.UserServiceImpl" %>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%
	String account = request.getParameter("account");
	String name = request.getParameter("name");
	UserService userService = new UserServiceImpl();
	String[][] roleInfoArray = userService.queryRole(account);
	int spLen = 0;
	int menuLen = 0;
	int quickLen = 0;
	for(int len=0;len<roleInfoArray.length;len++)
	{
		if("2".equals(roleInfoArray[len][3])) {
			spLen++;
		} else if("1".equals(roleInfoArray[len][3])) {
			menuLen++;
		} else if("3".equals(roleInfoArray[len][3])) {
			quickLen++;
		} 
	}
	
%>
<app:base/>

<title>实例页面-查询</title>

<script type="text/javascript" charset="UTF-8">
 	var account = "<%=account %>";
  	var controllername= "${pageContext.request.contextPath }/userController.do";
	
	//页面默认参数
	$(document).ready(function() { 
        
	}); 
	
	// 点击保存按钮
	$(function() {
		var saveUserInfoBtn = $("#saveUserInfo");
		saveUserInfoBtn.click(function() {
			var checkObj = $(":checkbox");
			var checkValue = "";
			for(var i = 0; i < checkObj.length; i++) {
				if(checkObj[i].checked == true) {
					checkValue += checkObj[i].value + ",";
				}
			}
//			alert(checkValue + " | " + account);
			var success = defaultJson.doInsertJson(controllername + "?awardRoleToUser&account="+account+"&checkValue="+checkValue, null, null);
			if(success == true) {
				//parent.$("body").manhuaDialog.close();
			}
		});
		var saveUserInfoBtn = $("#saveUserInfo2");
		saveUserInfoBtn.click(function() {
			var checkObj = $(":checkbox");
			var checkValue = "";
			for(var i = 0; i < checkObj.length; i++) {
				if(checkObj[i].checked == true) {
					checkValue += checkObj[i].value + ",";
				}
			}
//			alert(checkValue + " | " + account);
			var success = defaultJson.doInsertJson(controllername + "?awardRoleToUser&account="+account+"&checkValue="+checkValue, null, null);
			if(success == true) {
				//parent.$("body").manhuaDialog.close();
			}
		});
		var saveUserInfoBtn = $("#saveUserInfo3");
		saveUserInfoBtn.click(function() {
			var checkObj = $(":checkbox");
			var checkValue = "";
			for(var i = 0; i < checkObj.length; i++) {
				if(checkObj[i].checked == true) {
					checkValue += checkObj[i].value + ",";
				}
			}
//			alert(checkValue + " | " + account);
			var success = defaultJson.doInsertJson(controllername + "?awardRoleToUser&account="+account+"&checkValue="+checkValue, null, null);
			if(success == true) {
				//parent.$("body").manhuaDialog.close();
			}
		});

		$("#btnCancel").click(function(){
			$(window).manhuaDialog.close();
		});
		$("#btnCancel2").click(function(){
			$(window).manhuaDialog.close();
		});
		$("#btnCancel3").click(function(){
			$(window).manhuaDialog.close();
		});
	});
	
</script>      
</head>
<body>
<app:dialogs/>
<ul class="nav nav-tabs" id="myTab">
  <li class="active"><a href="#menuRole" data-toggle="tab">菜单角色（<%=menuLen%>）</a></li>
  <li><a href="#spRole" data-toggle="tab">审批角色（<%=spLen%>）</a></li>
  <li><a href="#quickRole" data-toggle="tab">快捷入口角色（<%=quickLen%>）</a></li>
</ul>
<div class="tab-content">
	<div class="tab-pane active" id="menuRole">
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
				<b style="font-size:16px;">当前选择的用户：</b><%=name+"（"+account+"）"%>
				<div>
				<table class="B-table"  style="width: 99%">
				<tr>
					<th colspan="8">
					    <span class="pull-right">
					    	<button class="btn" id="saveUserInfo">保存</button>
					    	<button class="btn" id="btnCancel">关闭</button>
					    </span>
					</th>
				</tr>
				<%
					// 每行显示的个数
					int num = 5;
					// 角色总数
					int line = 0;
					int cols = 1;
					for(int i = 0; i < roleInfoArray.length; i++) {
						
						if("1".equals(roleInfoArray[i][3]))
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
	</div>
			
	
	<div class="tab-pane" id="spRole">
		<div class="container-fluid">
		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
			<b style="font-size:16px;">当前选择的用户：</b><%=name+"（"+account+"）"%>
			<div>
			<table class="B-table"  style="width: 99%">
			<tr>
					<th colspan="8">
					    <span class="pull-right">
					    	<button class="btn" id="saveUserInfo2">保存</button>
					    	<button class="btn" id="btnCancel2">关闭</button>
					    </span>
					</th>
				</tr>
				<%
		
					line = 0;
					cols = 1;
					for(int i = 0; i < roleInfoArray.length; i++) {
						
						if("2".equals(roleInfoArray[i][3]))
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
										<input type="checkbox" id="name" name="name" value="<%=roleInfoArray[i][0] %>=<%=roleInfoArray[i][2] %>" checked="checked"/>&nbsp;&nbsp;<font color="red"><%=roleInfoArray[i][2]%></font>
								<%
									} else {
								%>
										<input type="checkbox" id="name" name="name" value="<%=roleInfoArray[i][0] %>=<%=roleInfoArray[i][2] %>"/>&nbsp;&nbsp;<font color="#000000"><%=roleInfoArray[i][2]%></font>
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
	</div>
	
	<div class="tab-pane" id="quickRole">
		<div class="container-fluid">
		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
			<b style="font-size:16px;">当前选择的用户：</b><%=name+"（"+account+"）"%>
			<div>
			<table class="B-table"  style="width: 99%">
			<tr>
					<th colspan="8">
					    <span class="pull-right">
					    	<button class="btn" id="saveUserInfo3">保存</button>
					    	<button class="btn" id="btnCancel3">关闭</button>
					    </span>
					</th>
				</tr>
				<%
		
					line = 0;
					cols = 1;
					for(int i = 0; i < roleInfoArray.length; i++) {
						
						if("3".equals(roleInfoArray[i][3]))
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
										<input type="checkbox" id="name" name="name" value="<%=roleInfoArray[i][0] %>=<%=roleInfoArray[i][2] %>" checked="checked"/>&nbsp;&nbsp;<font color="red"><%=roleInfoArray[i][2]%></font>
								<%
									} else {
								%>
										<input type="checkbox" id="name" name="name" value="<%=roleInfoArray[i][0] %>=<%=roleInfoArray[i][2] %>"/>&nbsp;&nbsp;<font color="#000000"><%=roleInfoArray[i][2]%></font>
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
								<!-- td class="left-border bottom-border"></td -->
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
	</div>
</div>

</body>
</html>