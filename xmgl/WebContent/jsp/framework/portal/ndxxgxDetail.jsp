
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.common.QuerySet"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.common.User"%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<link href="${pageContext.request.contextPath }/css/bootstrap.css"
			rel="stylesheet" media="screen">
		<link href="${pageContext.request.contextPath }/css/base.css"
			rel="stylesheet" media="screen">
		<title>详细信息页面</title>
		<% 
	User user = (User)session.getAttribute(Globals.USER_KEY);
	String loginName = user.getName();
 
	String type = request.getParameter("type");
	String id = request.getParameter("id");
	String sql = Pub.getNewsSql("5", id);
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
			+ "d.BMJC jsbmmc, to_char(g.ydsj,'yyyy-MM-dd HH24:mi:ss') ydsj "
			+ " from xtbg_xxzx_ggtz_fb g,VIEW_YW_ORG_DEPT d "
			+ " where g.jsbm=d.row_id and g.sfyd='1' and g.ggid='" + id + "' order by d.sort asc,d.row_id asc";
	QuerySet readQs = DBUtil.executeQuery(readSql, null);
	// 未阅读人员
	String notReadSql = "select f.jsr, d.BMJC jsbmmc "
			+ " from xtbg_xxzx_ggtz_fb f,VIEW_YW_ORG_DEPT d "
			+ " where f.jsbm=d.row_id and f.sfyd='0' and f.ggid='"+id+"' order by d.sort asc,d.row_id asc";
	QuerySet notReadQs = DBUtil.executeQuery(notReadSql, null);
	
	// 未阅读人员所在的部门
//	"select d.dept_name jsbm,count(*) "
//			+ " from xtbg_xxzx_ggtz_fb f,VIEW_YW_ORG_DEPT D "
//			+ " where f.jsbm=d.row_id and sfyd='0' and f.ggid='"+id+"' group by d.dept_name order by d.sort asc,d.row_id asc";
	String departmentSql = 
			"select D.BMJC jsbm,A.SL from "
			+ "(select count(*) SL,JSBM from xtbg_xxzx_ggtz_fb where sfyd = '0' and ggid = '"+id+"' group by JSBM)  A,"
			+ "VIEW_YW_ORG_DEPT D "
			+ "where A.JSBM=D.ROW_ID "
			+ "order by d.sort asc,d.row_id asc ";
	QuerySet departQs = DBUtil.executeQuery(departmentSql, null);
	
	String fileSql = "SELECT * FROM FS_FILEUPLOAD WHERE YWID='" + id + "'";
	QuerySet fileQs = DBUtil.executeQuery(fileSql, null);

	%>
		<script type="text/javascript" charset="UTF-8">
	var controllername= "${pageContext.request.contextPath }/ndxxgxController.do"; 
	var p_id = "<%=id%>";
	$(function() {
		doInit();
		$("#readedList").on("click",function(){
			$("#readed").slideToggle("fast");
		});
		
		$("#unReadedList").on("click",function(){
			$("#unRead").slideToggle("fast");
		});
	});
	function doInit(){
		queryFormValue();
	}
	function queryFormValue(){
		var dataObj = new Object();
		var querycondition = new Object();
		var conditions = new Array();
		var pages = new Object();
		var idcond = new Object();
		idcond["value"] = p_id;
		idcond["fieldname"] = "XTBG_XXZX_NDXXGX_ID";
		idcond["operation"] = "=";
		idcond["logic"] = "and";
		conditions.push(idcond);
		pages["recordsperpage"] = 10;
		pages["currentpagenum"] = 1;
		pages["totalpage"] = 0;
		pages["countrows"] = 0;
		querycondition["conditions"] = conditions;
		dataObj["querycondition"] = querycondition;
		dataObj["pages"] = pages;
		var dataMsg = {msg:JSON.stringify(dataObj)};
		var action1 = controllername + "?queryNdxxgx";
		$.ajax({
			url : action1,
			data: dataMsg,
			success: function(result)
			{
				var resultmsgobj = convertJson.string2json1(result);
				var msgStr = resultmsgobj.msg;	//保留换行
				msgStr = msgStr.replace(/\\b/g,"&nbsp;");				//保留空格
				msgStr = msgStr.replace(/\&lt;/g,"<");					//保留左尖叫号
				msgStr = msgStr.replace(/\&gt;/g,">");					//保留右尖角号
				msgStr = msgStr.replace(/\&nbsp;/g," ");				//保留空格
				var resultobj = convertJson.string2json1(msgStr);
				if('0'==resultobj){
					return ;
				}
				var subresultmsgobj = resultobj.response.data[0];
				$("span[fieldname]").each(function(){
					$(this).html($(subresultmsgobj).attr($(this).attr("fieldname")));
				});
				queryFileData();
			}
		});
		//-----------------
		//-	附件查询
		//-----------------
		function queryFileData(){
			var obj = new Object();
			obj.YWID = p_id;
			var data = JSON.stringify(obj);
			var data1 = defaultJson.packSaveJson(data);
			$.ajax({
				url : "${pageContext.request.contextPath }/fileUploadController.do?queryFileList",
				data : data1,
				cache : false,
				async :	false,
				dataType : "json",
				success : function(result) {
					if(result.msg=="[]"){
						//表示没有附件
					}else{
						var res = convertJson.string2json1(result.msg);
						var showHtml = "";
						for(var i=0;i<res.length;i++){
							var fileID = res[i]["fileid"];
							var fileName = res[i]["name"];
							showHtml += '<p style="cursor: pointer;">';
							showHtml += '<img src="${pageContext.request.contextPath }/images/icon-annex.png"  title="点击查看附件">';
							showHtml += '<a target="_blank" onclick="checkupFiles(\''+fileID+'\')">'+fileName+'</a>';
							showHtml += '&nbsp;';
							showHtml += '<a href="${pageContext.request.contextPath }/fileUploadController.do?getFile&fileid='+fileID+'" download="">';
							showHtml += '<i class="icon-download-alt"></i>下载</a>';
							showHtml += '</p>';
						}
						$("#fileContent").html(showHtml);//放入附件
					}
				}
			});
		}
	}
</script>
	</head>
	<app:dialogs />
	<body>
		<div class="container-fluid">
			<p></p>
			<div class="row-fluid">
				<div class="page-header">
					<h3 class="text-center">
						<span fieldname="NDXXBT"></span>
						<br>
						<small>发布时间：<span fieldname="FBSJ"></span>&nbsp;&nbsp;&nbsp;&nbsp;发布人：<span
							fieldname="FBR_SV"></span> </small>
					</h3>
				</div>
				<div>
					<p>
						<span fieldname="NR"></span>
					</p>
				</div>
				<div id="fileContent">
				</div>
			</div>
			<hr>
			<p id="readedList" style="cursor: pointer;" title="点击查看已阅读人员">
				<b style="font-size: 16px;">已阅读人员（<%=readQs.getRowCount()%>人）</b>
			</p>
			<div class="B-small-from-table-autoConcise" id="readed"
				style="display: none">
				<table class="B-table" width="520px">
					<tr align="center" style="font-size: 15px;">
						<th width="8%">
							部门
						</th>
						<th width="10%">
							已阅人员
						</th>
						<th width="20%">
							阅读时间
						</th>
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
			<p id="unReadedList" style="cursor: pointer;" title="点击查看未阅读人员">
				<b style="font-size: 16px;">未阅读人员（<%=notReadQs.getRowCount() %>人）</b>
			</p>
			<div id="unRead" style="display: none">
				<div class="B-small-from-table-autoConcise">
					<table class="B-table" width="800px">
					<%	
						int colspanCnt = 8;
						int cccnt = 1;
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
						<%	
									}
						%>
										<%=notReadQs.getString(cccnt, "jsr") %>
										</label>
									</td>
									<%
									cccnt++;
								}
						%>
						</tr>
						<%	
							}
						}
						%>
					</table>
				</div>
			</div>
		</div>
		<form action="" name="frmPost" style="display: none"></form>
	</body>
</html>