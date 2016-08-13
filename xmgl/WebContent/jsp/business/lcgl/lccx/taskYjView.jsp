<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="java.sql.Connection"%>

<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%
	User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
	String sjbh = request.getParameter("sjbh");
	String ywlx = request.getParameter("ywlx");
	String currentUser = user.getAccount();
%>
<app:base/>
<script type="text/javascript">
var controllername= '${pageContext.request.contextPath }/ProcessAction.do';

$(function() {
	setFileData("","","<%=sjbh%>","<%=ywlx%>");
	queryFileData("","","<%=sjbh%>","<%=ywlx%>");
	$.ajax({
		url : controllername+"?yjView&sjbh=<%=sjbh %>",
		success: function(result) {
			var rsList = convertJson.string2json1(result).rsList;
			var rsListLength = rsList.length;
			
			if(rsListLength == 0) {
				$("#yjTitle").text("会签单上暂无您填写意见，无法补充。可以补充附件");
				return;
			}
			for(var i = 0; i < rsListLength; i++) {
				var obj = rsList[i];
				var str=document.getElementById("tablelist").outerHTML;
				
				str=str.replace( 'DOMAIN_COMMENT' , $(obj).attr("DOMAIN_COMMENT") );
				str=str.replace( 'LRSJ' , $(obj).attr("LRSJ") );
				str=str.replace( 'DOMAIN_VALUE' , $(obj).attr("DOMAIN_VALUE") );
				str=str.replace( 'style="display: none"' , '');
				$("#rsTable").append(str);
			}
			
			var _obj = rsList[0];
			$("#wsid").val(_obj.WS_TEMPLATE_ID);
		}
	});
});

function doSave() {
	var url = controllername+"?bcyj";
	var data1 = "sjbh=<%=sjbh %>&ywlx=<%=ywlx %>&wsid="+$("#wsid").val();
	data1 += "&fieldname="+document.getElementById("fieldname").value+"&mind="+encodeURIComponent(document.getElementById('mind').value);
	if($("#queryForm").validationButton()) {
	 	$.ajax({
			type : 'post',
			url : url,
			data : data1,
			dataType : 'json',
			success: function(result) {
			    xAlert("信息提示",convertJson.string2json1(result).msg);
				setInterval(afterClose, 2000);
			}
		});
	} else {
		requireFormMsg();
	  	return;
	}
}


function afterClose() {
	var s = "${pageContext.request.contextPath}/jsp/business/lcgl/lccx/taskYjView.jsp?sjbh=<%=sjbh%>&ywlx=<%=ywlx%>";
	$(window).manhuaDialog.changeUrl("已审批意见",s);
}
</script>
</head>
<body>
<div class="container-fluid">
	<div class="row-fluid">
	
		
		<h4 class="title" id="yjTitle">意见</h4>
		<div class="B-small-from-table-autoConcise">
	        <div class="row-fluid  ismoban" id="rsTable">
	        	<table width="100%" class="B-table" id="tablelist" style="display: none">
	        		<tr>
	        			<th width="5%" class="right-border bottom-border" style="text-align: right">会签单节点</th>
	        			<td width="10%" class="right-border bottom-border">DOMAIN_COMMENT</td>
	        			<th width="5%" class="right-border bottom-border" style="text-align: right">录入时间</th>
	        			<td width="10%" class="right-border bottom-border">LRSJ</td>
	        		</tr>
	        		<tr>
	        			<th class="right-border bottom-border" style="text-align: right">意见</th>
	        			<td class="right-border bottom-border" colspan="3">DOMAIN_VALUE</td>
	        		</tr>
	        	</table>
	        </div>
		</div>
		<div style="height:5px;"></div>
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">
				<span class="pull-right">
					<button id="btnSave" class="btn" onClick="doSave()" type="button">提 交</button>
				</span>
			</h4>
			<form method="post" id="queryForm"  >
	        	<table width="100%" class="B-table">
		        	<TR  style="display:none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<input type="text" kind="text" fieldname="wsid" name="wsid" id="wsid">
						</TD>
					</TR>
	        		<tr>
		        		<th width="10%" class="right-border bottom-border" style="text-align: right">会签单节点</th>
	        			<td class="right-border bottom-border">
	        				<select class="span12 6characters" id="fieldname" kind="dic"  placeholder="必填" check-type="required"
			 	 			src="T#ap_process_ws t:distinct t.fieldname:(select s.domain_comment from ws_template_sql s where t.ws_template_id = s.ws_template_id and t.fieldname = s.fieldname) DOMAIN_COMMENT:sjbh='<%=sjbh %>' and lrrid='<%=currentUser %>' and wswh_flag=5 and lrsj is not null " 
						 	fieldname="fieldname" name="fieldname"></select>
	        			</td>
	        		</tr>
	        		<tr>
	        			<th width="10%" class="right-border bottom-border" style="text-align: right">补充意见</th>
	        			<td class="right-border bottom-border" colspan="2">
	        				<textarea class="span12" id="mind" placeholder="必填" check-type="required"></textarea>
	        			</td>
	        		</tr>
	        		<tr>
						<th align="center" valign="middle" style="text-align:center;font-size:14px;width:8%">附件信息</th>
						<td colspan="2">
							
							<span class="btn btn-fileUpload" style="font-size:14px;" onclick="doSelectFile(this);"	fjlb="3999"> <i class="icon-plus"></i> <span>添加文件...</span></span>
						    <table role="presentation" class="table table-striped">
								<tbody  class="files showFileTab" notClear="true" id="fileTbody"
									data-toggle="modal-gallery" data-target="#modal-gallery" showLrr="true" deleteUser="<%=user.getAccount()%>" showSize="false" onlyView="true">
								</tbody>
							</table>
						</td>
					</tr>
	        	</table>
	        </form>
		</div>
		
		
	</div>
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
</body>
</html>