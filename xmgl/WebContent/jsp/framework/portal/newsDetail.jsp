
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.common.QuerySet"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.Globals" %>
<%@ page import="com.ccthanking.framework.common.User" %>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link href="${pageContext.request.contextPath }/css/bootstrap.css" rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath }/css/base.css" rel="stylesheet" media="screen">
<title>详细信息页面</title>
<script type="text/javascript" charset="UTF-8">
	
	$(function() {
		$("#readedList").on("click",function(){
			$("#readed").slideToggle("fast");
		});
		
		$("#unReadedList").on("click",function(){
			$("#unRead").slideToggle("fast");
		});
		
		$("#checkPsn").click(function() {
			var noReadPsnCheck = $(":checkbox");
			for(var i = 0; i < noReadPsnCheck.length; i++) {
				if(noReadPsnCheck[i].checked) {
					noReadPsnCheck[i].checked = false;
				} else {
					noReadPsnCheck[i].checked = true;
				}
			}
		});
	});
	
</script>  
</head>
<% 
 
 User user = (User)session.getAttribute(Globals.USER_KEY);
 String loginName = user.getName();
 
 String type = request.getParameter("type");
 String id = request.getParameter("id");
 String sql = Pub.getNewsSql(type, id);
 QuerySet qs = DBUtil.executeQuery(sql, null);
 
 String bt = qs.getString(1, 1);
 String fbsj = qs.getString(1, 4);
 String fbr = qs.getString(1, 3);
 String fbdw = qs.getString(1, 5);
 
 fbsj = fbsj.length() >= 11 ? fbsj.substring(0, 11) : fbsj;
 
 String nr = null;
 java.sql.Blob dbBlob = (java.sql.Blob)qs.getObject(1, 2);
 if(dbBlob != null) {
	 int length = (int)dbBlob.length();
	 byte[] buffer = dbBlob.getBytes(1, length);
	 nr = new String(buffer, "GBK");
 }

// 已阅读人员
String readSql = "select g.ggid, g.jsr_account, g.jsr," 
		+ "(select e.dept_name from VIEW_YW_ORG_DEPT e where e.row_id = g.jsbm) jsbmmc, to_char(g.ydsj,'yyyy-MM-dd HH24:mi:ss') ydsj "
		+ " from xtbg_xxzx_ggtz_fb g "
		+ " where g.sfyd='1' and g.ggid='" + id + "' order by g.jsbm";
QuerySet readQs = DBUtil.executeQuery(readSql, null);

/* String notReadSql = " select p.* "
		+ " from fs_org_person p"
		+ " where p.account not in (select userid from xtbg_xxzx_ggtz_ydr where ggid='"+id+"') order by p.department"; */

// 未阅读人员
String notReadSql = "select f.jsr, (select e.dept_name from VIEW_YW_ORG_DEPT e where e.row_id = f.jsbm) jsbmmc from xtbg_xxzx_ggtz_fb f where f.sfyd='0' and f.ggid='"+id+"' order by f.jsbm";
QuerySet notReadQs = DBUtil.executeQuery(notReadSql, null);
for(int i = 0 ; i < notReadQs.getRowCount() ; i++) {
}

/* String departmentSql = "select a.department, count(*) from fs_org_person a where a.account not in (select userid from xtbg_xxzx_ggtz_ydr where ggid='"+id+"') group by a.department order by a.department"; */
	

// 未阅读人员所在的部门
String departmentSql = "select (select e.dept_name from VIEW_YW_ORG_DEPT e where e.row_id = f.jsbm) jsbm,count(*) from xtbg_xxzx_ggtz_fb f where sfyd='0' and f.ggid='"+id+"' group by f.jsbm order by f.jsbm";
QuerySet departQs = DBUtil.executeQuery(departmentSql, null);
for(int i = 0 ; i < departQs.getRowCount() ; i++) {
}

String fileSql = "SELECT * FROM FS_FILEUPLOAD WHERE YWID='" + id + "'";
QuerySet fileQs = DBUtil.executeQuery(fileSql, null);

%>
<app:dialogs/>
<body>
<div class="container-fluid">
    <p></p>
    <div class="row-fluid">
        <div class="page-header"><h3 class="text-center"><%=bt%><br><small>发布时间：<%=fbsj%> 发布人：<%=fbr %></small></h3></div>
        <div>
        	<p><%=nr%></p>
        </div>
        <div>
        	<%
        	if("1".equals(type)) {
        		for(int i = 0; i < fileQs.getRowCount(); i++) {
        	%>
        	<p style=" cursor: pointer;"><img src="${pageContext.request.contextPath }/images/icon-annex.png"  title="点击查看附件"> <a target="_blank" onclick="checkupFiles('<%=fileQs.getString(i+1, "FILEID") %>')"><%=fileQs.getString(i+1, "FILENAME") %></a>&nbsp;<a href="${pageContext.request.contextPath }/fileUploadController.do?getFile&fileid=<%=fileQs.getString(i+1, "FILEID")%>" download=""><i class="icon-download-alt"></i>下载</a></p>
        	<%		
        		}
        	}
        	%>
        </div>
    </div>
    <hr>
    <div class="row-fluid">
	<%
	 if("1".equals(type)){
	%>
	
	<p id="readedList"  style=" cursor: pointer;" title="点击查看已阅读人员"><b  style="font-size:16px;">已阅读人员（<%=readQs.getRowCount()%>人）</b></p>
		
    	<div class="B-small-from-table-autoConcise" id="readed" style="display: none">
		<table class="B-table" width="520px">
			<tr align="center" style="font-size:15px;">
				<th width="8%">部门</th>
				<th width="10%">已阅人员</th>
				<th width="20%">阅读时间</th>
			</tr>
			<%
				for(int i = 0; i < readQs.getRowCount(); i++) {
					if(readQs.getString(i+1, 2) != null) {
			%>
			<tr style="font-size:13px;">
				<td width="25%" class="right-border bottom-border"><%=readQs.getString(i+1, "jsbmmc") %></td>
				<td width="8%" class="right-border bottom-border"><%=readQs.getString(i+1, "jsr") %></td>
				<td width="20%" class="right-border bottom-border" align="center"><%=readQs.getString(i+1, "ydsj") == null ? "" : readQs.getString(i+1, 5) %></td>
			</tr>
			<%	
					}
				}
			%>
		</table>
		</div>
		
	<p id="unReadedList"  style=" cursor: pointer;" title="点击查看未阅读人员"><b  style="font-size:16px;">未阅读人员（<%=notReadQs.getRowCount() %>人）</b></p>
	
	<div id="unRead" style="display: none">
    	<div class="B-small-from-table-autoConcise">
		<table class="B-table" width="800px">
			<%	
				int colspanCnt = 8;
				int cccnt = 1;
			%>
			<%
				if(fbr != null && fbr.equals(loginName)) {
			%>
			<tr>
				<th colspan="<%=colspanCnt %>" style="font-size:15px;height: 40px;">
				<!-- 	<button id="checkPsn" class="btn"  type="button" >反选</button>
					<button id="sendSms" class="btn"  type="button" >发送短信</button>
					<button id="sentMsg" class="btn"  type="button" >发送消息</button> -->
				</th>
			</tr>
			<% } %>
			<%
				for(int i = 0; i < departQs.getRowCount(); i++) {
					// 显示部门的标识
					boolean showDeptFlag = false;
					
					int count = Integer.parseInt(departQs.getString(i+1, 2));
					// 4个一行，line表示的是行数
					int line = count / colspanCnt;
					// remainder表示的是余数
					int remainder = count % colspanCnt;
					// 当余数为0时，count总数恰好被整除；当余数不为零时，应该再加一行。
					line = remainder == 0 ? line : line + 1;
					
					for(int j = 0; j < notReadQs.getRowCount(); j++) {
						String notReadDept = notReadQs.getString(j+1, "jsbmmc") == null ? "未知部门" : notReadQs.getString(j+1, "jsbmmc");
						String depart = departQs.getString(i+1, "jsbm") == null ? "未知部门" : departQs.getString(i+1, "jsbm");
						if(notReadDept.equals(depart)) {
							if(!showDeptFlag) {
								%>
								<tr>
									<th colspan="<%=colspanCnt %>" align="left" style="font-size:15px;"><%=notReadDept %></th>
								</tr>
								<%
							}
							showDeptFlag = true;
						}
					}
					
					for(int j = 0; j < line; j++) {
				//		if(readQs.getString(j+1, 4).equals(departQs.getString(i+1, 1))) {
								int start = colspanCnt * j;
								int end = colspanCnt * j + colspanCnt;
						%>
						<tr style="font-size:13px;">
						<%
								for(int k = start; k < end; k++) {
									if(k >= count) {
										%>
										<td class="right-border bottom-border"></td>
										<%
										continue;
									}
									%>
									<td style="width: 100px" class="right-border bottom-border">
										<%
											if(fbr != null && fbr.equals(loginName)) {
										%>
										<label class="checkbox"><input id="noReadPsnCheck" type="checkbox"/>
										<%	} %>
										<%=notReadQs.getString(cccnt, "jsr") %></label>
									</td>
									<%
									cccnt++;
								}
						%>
							
						</tr>
						<%	
					//		}
					}
					
				}
			%>
		</table>
		</div>
		<%
	     }
	    %>
	</div>
    <!-- div class="span6 pull-left text-left">上一条：<a href="#">XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX</a></div -->
    <!-- div class="span6 pull-right text-right">下一条：<a href="#">XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX</a></div></div -->
	</div>
</div>
<!-- start 引用bootstrap --> 

<!-- end 引用bootstrap -->
<form action="" name="frmPost" style="display: none"></form>
</body>
</html>