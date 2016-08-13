<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%	String id = request.getParameter("infoID");
			String type = request.getParameter("type");
		%>
		<app:base />
		<title>批复问题</title>
		<script type="text/javascript" charset="utf-8">
			var controllername= "${pageContext.request.contextPath }/wttb/wttbController.do";
			var btnNum = 0;
			var id = '<%=id%>';
			var p_type = '<%=type%>';
			$(document).unbind("ajaxStart");
			$(function(){
				doInit();
				$("#btnSave").click(function(){
					if($("#insertForm").validationButton()){
		 				//生成json串
						btnNum = 1;
		 		 		var data = Form2Json.formToJSON(insertForm);
		 		 		$(window).manhuaDialog.getParentObj().setYwid($("#ywid").val());
						if(p_type=="main"){
							$(window).manhuaDialog.getParentObj().doReply(data);
							//如果是主页面调用的批复页面，那么只关闭自己(表示是补充意见按钮进入的)
							$(window).manhuaDialog.close();
						}else{
							var accepter = getAccepter();
							xConfirm("提示信息","回复消息给:"+accepter.jsrname+"，是否确认？");
		 					$('#ConfirmYesButton').unbind();
		 					$('#ConfirmYesButton').one("click",function(){
								$(window).manhuaDialog.getParentObj().doReply(data);
								$(window).manhuaDialog.getParentObj().closeWindow($(window));
							});
						}
						//$(window).manhuaDialog.close();//由父页面关闭
					}
				});
			});
			function doInit(){
				if(p_type=="main"){
					$("#btnSave").text("保存");
				}
				$("#wtid").val(id);
			}
			function getAccepter(){
				var str = "";
				$.ajax({
					url:controllername + "?getAccepter&wtid="+id,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						str = convertJson.string2json1(result.msg);
					}
				});
				return str;
			}
		</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						<span class="pull-right">
							<button id="btnSave" class="btn" type="button">
								回复
							</button>
						</span>
					</h4>
					<form method="post" action="" id="insertForm">
						<table class="B-table" width="100%">
							<!-- 这里需要一个隐藏域，存放比如：问题编号,接受人账号，接受单位等信息 -->
							<tr style="display: none;">
								<th width="8%" class="right-border bottom-border">
									问题编号
								</th>
								<td width="42%" class="right-border bottom-border"><input class="span12" type="text" name="WTTB_INFO_ID" fieldname="WTTB_INFO_ID"
										id="wtid">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border">
									回复意见
								</th>
								<td width="92%" colspan=5 class="bottom-border">
									<textarea class="span12" rows=5 name="PFNR" fieldname="PFNR"
										placeholder="必填" check-type="required" roattr="1"></textarea>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									附件
								</th>
								<td width="92%" colspan=3 class="bottom-border">
									<div>
										<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0045">
											<i class="icon-plus"></i>
											<span>添加文件...</span>
										</span>
										<table role="presentation" class="table table-striped">
											<tbody fjlb="0045" class="files showFileTab" 
												data-toggle="modal-gallery" data-target="#modal-gallery">
											</tbody>
										</table>
									</div>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
			<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
		</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="ywid" id="ywid" value="">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>
