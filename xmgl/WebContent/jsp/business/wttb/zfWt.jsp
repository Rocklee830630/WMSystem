<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%@ page import="com.ccthanking.framework.common.User"%>
	    <%@ page import="com.ccthanking.framework.Globals"%>
		<%	String id = request.getParameter("infoID");
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String account = user.getAccount();
		%>
		<app:base />
		<title>转发问题</title>
		
		<script type="text/javascript" charset="utf-8">
			var btnNum = 0;
			var id = '<%=id%>';
			var userid = '<%=account%>';
			$(function(){
				doInit();
				$("#btnSave").click(function(){
					if($("#insertForm").validationButton()){
		 				//生成json串
		 				var zbrStr = $("#zbrName").val()==undefined?"":$("#zbrName").val();
		 				var sbrStr = $("#sbrName").attr("code")==undefined?"":$("#sbrName").attr("code");
		 				var jsrStr = $("#jsrName").val()==undefined?"":$("#jsrName").val();
		 				var jsrString = zbrStr+":"+sbrStr+":"+jsrStr;
		 				if(userid==zbrStr){
							xAlert("警告","不能把问题转发给自己，请修改主送部门!","3");
							return;
		 				}
		 				var sbrArr = sbrStr.split(",");
		 				var uniqueFlag = 1;
		 				for(var i=0;i<sbrArr.length;i++){
		 					if(sbrArr[i]==zbrStr){
		 						uniqueFlag = 2;
		 						break;
		 					}
		 				}
		 				if(uniqueFlag==2){
							xAlert("警告","送办人员包含了主办人，请删除重复数据!","3");
							return;
		 				}
		 				$("#jsr").val(jsrString);
		 		 		var data = Form2Json.formToJSON(insertForm);
		 		 		$(window).manhuaDialog.getParentObj().setYwid($("#ywid").val());
						$(window).manhuaDialog.getParentObj().doTransfer(data);
						$(window).manhuaDialog.close();
					}
				});
				$("#zbrBtn").click(function(){
					btnNum = 1;
					openUserTree("single",$("#zbrName").attr("code"),"");
				});
				$("#sbrBtn").click(function(){
					btnNum = 2;
					selectUserTree("multi",$("#sbrName").attr("code"),"");
				});
				//提报类型radio点击事件
				$("label input[name=TBLX]").click(function(){
					changeDic($(this).val());
				});
			});
			function doInit(){
				$("#wtid").val(id);
				setDefaultTblx();
			}
			function getWinData(rowsArr){
				if(btnNum==1){
					var jsrStr = "";
					var jsdwStr = "";
					var jsrName = "";
					for(var i=0;i<rowsArr.length;i++){
						var tempJson = rowsArr[i];
						jsrStr +=tempJson.ACCOUNT+",";
						jsdwStr +=tempJson.DEPTID+",";
						jsrName +=tempJson.USERNAME+",";
					}
					$("#zbrName").attr("code",jsrStr.substring(0,jsrStr.length-1));//为字段的code赋值
					$("#zbrName").val(jsrName.substring(0,jsrName.length-1));
				}else if(btnNum==2){
					var jsrStr = "";
					var jsdwStr = "";
					var jsrName = "";
					for(var i=0;i<rowsArr.length;i++){
						var tempJson = rowsArr[i];
						jsrStr +=tempJson.ACCOUNT+",";
						jsdwStr +=tempJson.DEPTID+",";
						jsrName +=tempJson.USERNAME+",";
					}
					$("#sbrName").attr("code",jsrStr.substring(0,jsrStr.length-1));//为字段的code赋值
					$("#sbrName").val(jsrName.substring(0,jsrName.length-1));
				}
			}
			//-------------------------------------
			//-提报类型加默认值
			//-------------------------------------
			function setDefaultTblx(){
				$("label input[name=TBLX]:eq(0)").attr("checked",'checked'); 
			}
			//-------------------------------------
			//-改变主送部门字典
			//-------------------------------------
			function changeDic(val){
				if(val=="1"){
					$("#zbrTh").html("主送部门");
					$("#zbrName").attr("src","T#VIEW_YW_ORG_DEPT D:FZR:DEPT_NAME:D.FZR is not null ");
				}else{
					$("#zbrTh").html("主送领导");
					$("#zbrName").attr("src","T#VIEW_ZR D:ACCOUNT:NAME:1=1 order by to_number(SORT) ")
				}
				reloadSelectTableDic($("#zbrName"));
			}
		</script>
	</head>
	<body>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						<span class="pull-right">
							<button id="btnSave" class="btn" type="button">
								保存
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
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="WTTB_INFO_ID" fieldname="WTTB_INFO_ID"
										id="wtid">
								</td>
								<th width="8%" class="right-border bottom-border">
									接收人
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="JSR" fieldname="JSR" id="jsr">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									提报类型
								</th>
								<td width="42%" class="bottom-border">
									<input class="span12" type="radio" kind="dic" src="TBLX" name="TBLX" fieldname="TBLX">
								</td>
								<th width="8%" class="right-border bottom-border text-right" id="zbrTh">
									主送部门
								</th>
								<td width="42%" class="bottom-border">
									<select class="span12" id="zbrName" roattr="9" placeholder="必填" check-type="required" defaultMemo="请选择"
										kind="dic" src="T#VIEW_YW_ORG_DEPT D:FZR:DEPT_NAME:D.FZR is not null "></select>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									抄送人
								</th>
								<td width="92%" colspan=3 class="bottom-border">
									<input style="width:85%;" type="text" id="sbrName" roattr="9" readonly>
									<button class="btn btn-link"  type="button" id="sbrBtn"><i class="icon-edit"></i></button>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									处理建议
								</th>
								<td width="92%" colspan=3 class="bottom-border">
									<textarea class="span12" rows=5 name="JYJJFA" fieldname="JYJJFA"
										roattr="1"></textarea>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									希望解决时间
								</th>
								<td width="42%" colspan=3 class="right-border bottom-border">
									<input style="width:30%;" type="date" name="XWWCSJ" fieldname="XWWCSJ"
										roattr="1" check-type="required">
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
