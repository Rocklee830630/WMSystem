<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%
			String wdid = request.getParameter("wdid");
			String dept = request.getParameter("dept");
			dept = dept==null ? "":dept;
		%>
		<title>标准文档主页面</title>

		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<app:base />
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid" align="center">
				<div class="B-small-from-table-autoConcise">
					<div class="span12">
					<div style="height: 5px;">
					</div>
							<form method="post" action="" id="insertForm">
								<table class="B-table" width="100%">
									<tr>
										<th width="8%" class="right-border bottom-border text-right">
											附件文档
										</th>
										<td width="92%" colspan=7 class="bottom-border">
											<div>
												<table role="presentation" class="table table-striped">
													<tbody fjlb="1060" class="files showFileTab" onlyView="true"
														data-toggle="modal-gallery" data-target="#modal-gallery">
													</tbody>
												</table>
											</div>
										</td>
									</tr>
								</table>
							</form>
					</div></element>
					<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true" />
				</div>
				<div style="display:none;">
					<input type="hidden" id="resultXML">
					<input class="span12" type="text" id="ywid" name="ywid" fieldname="ywid">
				</div>
			</div>
		</div>
<script type="text/javascript">
	var controllername= "${pageContext.request.contextPath }/BzwdController.do";
	var wdid = "<%=wdid %>";
	$(document).ready(function() {
		doInit();
	});
	function doInit(){
		var action1 = controllername + "?queryOneById&wdid="+wdid;
		$.ajax({
			url : action1,
			success: function(result)
			{
				var resultmsgobj = convertJson.string2json1(result);
				if(resultmsgobj.msg==0)
				{
					return;
				}	
				var resultobj = convertJson.string2json1(resultmsgobj.msg);
				var len = resultobj.response.data.length; 
				for(var i=0;i<len;i++){
					var subresultmsgobj = resultobj.response.data[i];
					var ywid1 = $(subresultmsgobj).attr("GC_WD_BZWD_ID");
					var sjbh = $(subresultmsgobj).attr("SJBH");
					var ywlx = $(subresultmsgobj).attr("YWLX");
					 //附件上传
					deleteFileData(ywid1,"","","");
					setFileData(ywid1,"",sjbh,ywlx);
					//查询附件信息
					queryFileData(ywid1,"","","");
				}
			}
		});
		
		
		
	
	}

</script>
	</body>
</html>