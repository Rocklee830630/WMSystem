
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.common.QuerySet"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link href="${pageContext.request.contextPath }/css/bootstrap.css" rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath }/css/base.css" rel="stylesheet" media="screen">
<title>详细信息页面</title>
<% 
 String type = "1";
 String sjbh = request.getParameter("sjbh");
 String taskid = request.getParameter("taskid");

 String id = "";
 QuerySet qs1 = DBUtil.executeQuery("select ggid from XTBG_XXZX_GGTZ where sjbh = '"+sjbh+"'", null);

 id = qs1.getString(1, 1);
 
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
		+ "g.jsbmmc, to_char(g.ydsj,'yyyy-MM-dd HH24:mi:ss') "
		+ " from xtbg_xxzx_ggtz_fb g "
		+ " where g.sfyd='1' and g.ggid='" + id + "' order by g.jsbm";
QuerySet readQs = DBUtil.executeQuery(readSql, null);

/* String notReadSql = " select p.* "
		+ " from fs_org_person p"
		+ " where p.account not in (select userid from xtbg_xxzx_ggtz_ydr where ggid='"+id+"') order by p.department"; */

// 未阅读人员
String notReadSql = "select f.jsr,f.jsbm,f.jsbmmc from xtbg_xxzx_ggtz_fb f where f.sfyd='0' and f.ggid='"+id+"' order by f.jsbm";
QuerySet notReadQs = DBUtil.executeQuery(notReadSql, null);
//for(int i = 0 ; i < notReadQs.getRowCount() ; i++) {
//}

/* String departmentSql = "select a.department, count(*) from fs_org_person a where a.account not in (select userid from xtbg_xxzx_ggtz_ydr where ggid='"+id+"') group by a.department order by a.department"; */
	

// 未阅读人员所在的部门
String departmentSql = "select (select e.dept_name from VIEW_YW_ORG_DEPT e where e.row_id = f.jsbm) jsbm,count(*) from xtbg_xxzx_ggtz_fb f where sfyd='0' and f.ggid='"+id+"' group by f.jsbm order by f.jsbm";
QuerySet departQs = DBUtil.executeQuery(departmentSql, null);
//for(int i = 0 ; i < departQs.getRowCount() ; i++) {
//}

String fileSql = "SELECT * FROM FS_FILEUPLOAD WHERE YWID='" + id + "'";
QuerySet fileQs = DBUtil.executeQuery(fileSql, null);

%>
<script type="text/javascript" charset="UTF-8">
	var controllername= "${pageContext.request.contextPath }/ggtzController.do";

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
		$("#shtg").on("click",function(){
			var shyj = $("#shyj").val();
			if(shyj==""){
				shyj = "同意";
			}
			xConfirm("提示信息","是否确认提交审核信息！");
			$('#ConfirmYesButton').unbind();
		 	$('#ConfirmYesButton').one("click",function(obj) {
			var data1 = "id=<%=id%>&shyj="+shyj+"&shjg=0&taskid=<%=taskid%>&sjbh=<%=sjbh%>";
			$.ajax({
				type : 'post',
				url : controllername+"?doggsh",
				data : data1,
				dataType : 'json',
				async :	false,
				success : function(result) {
					var fuyemian=$(window).manhuaDialog.getParentObj();
					 fuyemian.gengxinchaxun("操作成功！");
				
					 $(window).manhuaDialog.close();

				},
			    error : function(result) {
			    	xAlertMsg(result.message);
				    success = false;
				}
			});	
		   });	
		});
		$("#shbtg").on("click",function(){
			var shyj = $("#shyj").val();
			if(!shyj&&shyj=="")
			{
				requireFormMsg("请输入审核意见!");
				return;
			}
			xConfirm("提示信息","是否确认提交审核信息！");
			$('#ConfirmYesButton').unbind();
		 	$('#ConfirmYesButton').one("click",function(obj) {
			var data1 = "id=<%=id%>&shyj="+shyj+"&shjg=1&taskid=<%=taskid%>&sjbh=<%=sjbh%>";
			$.ajax({
				type : 'post',
				url : controllername+"?doggsh",
				data : data1,
				dataType : 'json',
				async :	false,
				success : function(result) {
					var fuyemian=$(window).manhuaDialog.getParentObj();
					 fuyemian.gengxinchaxun("操作成功！");
				
					 $(window).manhuaDialog.close();

				},
			    error : function(result) {
			    	xAlertMsg(result.message);
				    success = false;
				}
			 });	
		 	});	
			
		});
	});
	
</script>  
</head>

<app:dialogs/>
<body>
<div class="container-fluid">
    <p></p>
    <div class="row-fluid">
           <div class="B-small-from-table-auto">
        <table class="B-table" width="80%">
        <tr>
          <th width="8%" class="right-border bottom-border">审核意见</th>
          <td width="72%" class="right-border bottom-border"><input class="span12" type="text" placeholder="" id="shyj" name = "shyj" fieldname = "XMMC"  >
          
          </td>
           <td>
           <button id="shtg" class="btn"  type="button">审核通过</button>
           <button id="shbtg" class="btn"  type="button">审核不通过</button>
           </td>
        </tr>
      
      </table>
      </div>
        
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