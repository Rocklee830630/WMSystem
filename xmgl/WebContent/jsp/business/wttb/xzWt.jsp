<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%@ page import="com.ccthanking.framework.common.User"%>
		<%@ page import="com.ccthanking.framework.Globals"%>
		<app:base />
		<%	String leader = request.getParameter("flag");
			leader = leader==null || leader==""?"0":leader;
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String deptid = user.getDepartment();
			String newWin = request.getParameter("newWin");
			newWin = newWin==null?"":newWin;
		%>
		<title>新增问题</title>
		
		<script type="text/javascript" charset="utf-8">
			var controllername= "${pageContext.request.contextPath }/wttb/wttbController.do";
			var btnNum = 0;
			var flag = '<%=leader%>';
			var newWin = '<%=newWin%>';
			$(function(){
				doInit();
				$("#btnSave").click(function(){
					if($("#zbrName").val()==undefined){
						xAlertMsg("请至少选择一位主办人!"); 
						return;
					}else{
						if($("#insertForm").validationButton()){
			 				//生成json串
			 				var zbrStr = $("#zbrName").val()==undefined?"":$("#zbrName").val();
			 				var sbrStr = $("#sbrName").attr("code")==undefined?"":$("#sbrName").attr("code");
			 				var jsrStr = $("#jsrName").val()==undefined?"":$("#jsrName").val();
			 				var jsrString = zbrStr+":"+sbrStr+":"+jsrStr;
			 				var sbrArr = sbrStr.split(",");
			 				var uniqueFlag = 1;
			 				for(var i=0;i<sbrArr.length;i++){
			 					if(sbrArr[i]==zbrStr){
			 						uniqueFlag = 2;
			 						break;
			 					}
			 					if(sbrArr[i]!="" && sbrArr[i]==jsrStr){
			 						uniqueFlag = 3;
			 						break;
			 					}
			 				}
			 				if(uniqueFlag==2){
								xAlert("警告","送办人员包含了主办人，请删除重复数据!","3");
								return;
			 				}
			 				if(uniqueFlag==3){
								xAlert("警告","送办人员包含了部门发起负责人，请删除重复数据!","3");
								return;
			 				}
			 				/**
			 				if(zbrStr.indexOf(jsrStr)!=-1){
								xAlert("警告","主办人和部门发起负责人重复，请修改主办人!","3");
								return;
			 				}
			 				*/
			 				$("#jsr").val(jsrString);
			 		 		var data = Form2Json.formToJSON(insertForm);
			 		 		var zbrName = getUserNameByAccount(zbrStr);
			 		 		var sjztFlag = "";
			 		 		//部门人员发送给部长，部长发送给其他部门负责人
			 		 		if(flag=="0"){
			 		 			zbrName = $("#jsrName  option:selected").text();
			 		 			if(zbrStr==jsrStr){
			 		 				//如果主送人就是本部门部长，那么就显示主送人
			 		 				sjztFlag = "&sjzt=2";
			 		 				xConfirm("提示信息","是否确认提报？主送人："+zbrName);
				 				}else{
				 					//不然显示审核人
		 		 					xConfirm("提示信息","是否确认提报？发起负责人："+zbrName);
		 		 				}
			 		 		}else{
		 		 				xConfirm("提示信息","是否确认提报？主送人："+zbrName);
			 		 		}
							$('#ConfirmYesButton').unbind();
							$('#ConfirmYesButton').one("click",function(){
				 		 		if(newWin=="1"){
				 		 			//从右侧快捷入口打开新窗口
				 		 			if(flag=="1"){
				 		 				//部长直接提交
				 		 				var data1 = defaultJson.packSaveJson(data);
				 		 				var myUrl = controllername + "?doConfirmSend&ywid="+$("#ywid").val();
				 		 				submitData(myUrl,data1);
				 		 			}else{
				 		 				//办事人发送给部长
				 		 				var data1 = defaultJson.packSaveJson(data);
				 		 				var myUrl = controllername + "?sendReport&ywid="+$("#ywid").val()+sjztFlag;
				 		 				submitData(myUrl,data1);
				 		 			}
				 		 		}else{
									$(window).manhuaDialog.getParentObj().setYwid($("#ywid").val()+sjztFlag);
									$(window).manhuaDialog.setData(data);
									$(window).manhuaDialog.sendData();
								}
								$(window).manhuaDialog.close();
							});
						}
					}
				});
				$("#btnSave2").click(function(){
					$("#btnSave").click();
				});
				$("#zbrBtn").click(function(){
					btnNum = 1;
					openUserTree("single","","");
				});
				$("#sbrBtn").click(function(){
					btnNum = 2;
					selectUserTree("multi","","");
				});
				$("#xmBtn").click(function(){
					btnNum = 3;
					queryProjectWithBD();
				});
				$("#jsrBtn").click(function(){
					btnNum = 4;
					openUserTree("single","","");
				});
				//提报类型radio点击事件
				$("label input[name=TBLX]").click(function(){
					changeDic($(this).val());
				});
			});
			function doInit(){
				setTqjsr();
				if(flag=="1"){
					$(".bsrElem").hide();
					$(".bzElem").show();
					$("#jsrName").removeAttr("check-type");
				}else{
					$(".bsrElem").show();
					$(".bzElem").hide();
				}
				setDefaultTblx();
			}
			//根据账号获取登陆人姓名
			function getUserNameByAccount(n){
				var str = "";
				$.ajax({
					url:controllername + "?getUserNameByAccount&account="+n,
					data:"",
					dataType:"",
					async:false,
					success:function(result){
						var tempJson = convertJson.string2json1(result);
						str = tempJson.msg;
					}
				});
				return str;
			}
			function setTqjsr(){
				$.ajax({
					url:controllername + "?queryLeader",
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						var res = result.msg;
						if(res!=""){
							var tempJson = convertJson.string2json1(res);
							var blrStr = "";
							var blrCodeStr = "";
							//for(var i=0;i<tempJson.response.data.length;i++){
							for(var i=0;i<1;i++){
								var row = tempJson.response.data[i];
								if(row.ACCOUNT==""){
									xAlert("警告","本部门无负责人，请联系系统管理员!","3");
									break;
								}else{
									if(row.FLAG!=undefined&&row.FLAG=="ZR"){
										$("#jsrName").attr("src","T#VIEW_ZR D:ACCOUNT:NAME:1=1 order by to_number(SORT)");
										reloadSelectTableDic($("#jsrName"));
									}
									blrStr +=row.NAME+",";
									blrCodeStr +=row.ACCOUNT+",";
								}
							}
							blrStr = blrStr.length==0?"":blrStr.substring(0,blrStr.length-1);
							blrCodeStr = blrCodeStr.length==0?"":blrCodeStr.substring(0,blrCodeStr.length-1);
							$("#jsrName").val(blrCodeStr);
							//$("#jsrName").attr("code",blrCodeStr);
						}
					}
				});
			}
			function submitData(myUrl,myData){
				$.ajax({
					url:myUrl,
					data:myData,
					dataType:"json",
					async:false,
					success:function(result){
						xAlertMsg("操作成功!");
						window.parent.showSuccessMsg();
					}
				});
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
				}else if(btnNum==3){
					var data = convertJson.string2json1(rowsArr);
					$("#xmmc").val(data.XMMC);
					$("#bdmc").val(data.BDMC);
					$("#jhsjid").val(data.GC_JH_SJ_ID);
					$("#xmid").val(data.XMID);
					$("#bdid").val(data.BDID);
				}else if(btnNum==4){
					var jsrStr = "";
					var jsdwStr = "";
					var jsrName = "";
					for(var i=0;i<rowsArr.length;i++){
						var tempJson = rowsArr[i];
						jsrStr +=tempJson.ACCOUNT+",";
						jsdwStr +=tempJson.DEPTID+",";
						jsrName +=tempJson.USERNAME+",";
					}
					$("#jsrName").attr("code",jsrStr.substring(0,jsrStr.length-1));//为字段的code赋值
					$("#jsrName").val(jsrName.substring(0,jsrName.length-1));
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
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title bsrElem">
						<span class="pull-right">
							<button id="btnSave" class="btn" type="button">
								提交
							</button>
						</span>
					</h4>
					<form method="post" action="" id="insertForm">
						<table class="B-table" width="100%">
							<!-- 这里需要一个隐藏域，存放比如：问题编号,接受人账号，接受单位等信息 -->
							<tr style="display: none;">
								<th width="8%" class="right-border bottom-border">
									问题状态
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="SJZT" fieldname="SJZT"
										id="sjzt">
								</td>
								<th width="8%" class="right-border bottom-border">
									业务类型
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="YWLX" fieldname="YWLX"
										id="ywlx">
								</td>
								<th width="8%" class="right-border bottom-border">
									批复ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="PFID" fieldname="PFID"
										id="pfid">
								</td>
								<th width="8%" class="right-border bottom-border">
									录入人
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="LRR" fieldname="LRR"
										id="lrr">
								</td>
								<th width="8%" class="right-border bottom-border">
									接收人
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="JSR" fieldname="JSR" id="jsr">
								</td>
								<th width="8%" class="right-border bottom-border">
									计划数据ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="JHSJID" roattr="1"
										fieldname="JHSJID" id="jhsjid">
								</td>
								<th width="8%" class="right-border bottom-border">
									项目ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="XMID" roattr="1"
										fieldname="XMID" id="xmid">
								</td>
								<th width="8%" class="right-border bottom-border">
									标段ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="BDID" roattr="1"
										fieldname="BDID" id="bdid">
								</td>
							</tr>
							<tr class="bsrElem">
								<th width="8%" class="right-border bottom-border text-right">
									发起负责人
								</th>
								<td width="17%" class="bottom-border">
									<select class="span12 4characters" id="jsrName" roattr="9" placeholder="必填" check-type="required" kind="dic" src="T#VIEW_YW_ORG_DEPT D,FS_ORG_PERSON P:ACCOUNT:NAME:D.FZR=P.ACCOUNT and D.ROW_ID='<%=deptid %>' "></select>
									<!-- <button class="btn btn-link"  type="button" id="jsrBtn" title="选择部门负责人"><i class="icon-edit"></i></button> -->
								</td>
								<td width="75%" colspan=6 class="bottom-border">
								</td>
							</tr>
						</table>
						<h4 class="title">
							问题起草
							<span class="pull-right bzElem">
								<button id="btnSave2" class="btn" type="button">
									提交
								</button>
							</span>
						</h4>
						<table class="B-table" width="100%">
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									提报类型
								</th>
								<td width="17%" class="bottom-border">
									<input class="span12" type="radio" kind="dic" src="TBLX" name="TBLX" fieldname="TBLX">
								</td>
								<th width="8%" class="right-border bottom-border text-right" id="zbrTh">
									主送部门
								</th>
								<td width="17%" class="bottom-border">
									<select class="span12" id="zbrName" roattr="9" placeholder="必填" check-type="required" defaultMemo="请选择"
										kind="dic" src="T#VIEW_YW_ORG_DEPT D:FZR:DEPT_NAME:D.FZR is not null "></select>
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									抄送人
								</th>
								<td width="42%" colspan=3 class="bottom-border">
									<input class="span12" type="text" id="sbrName" roattr="9" disabled style="width:90%">
									<button class="btn btn-link"  type="button" id="sbrBtn" title="选择抄送人"><i class="icon-edit"></i></button>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									问题标题
								</th>
								<td width="42%" colspan=3 class="right-border bottom-border">
									<input class="span12" type="text" name="WTBT" roattr="1"
										fieldname="WTBT" placeholder="必填" check-type="required">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									问题类别
								</th>
								<td width="17%" class="bottom-border">
									<select class="span12 4characters" name="WTLX" fieldname="WTLX" kind="dic"
										src="WTLX" roattr="1" placeholder="必填" check-type="required">
									</select>
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									问题性质
								</th>
								<td width="17%" class="bottom-border">
									<select class="span12 6characters" name="WTXZ" fieldname="WTXZ" kind="dic"
										src="WTXZ" roattr="1" placeholder="必填" check-type="required">
									</select>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									项目名称
								</th>
								<td width="67%" colspan=3 class="right-border bottom-border">
									<input class="span12" type="text" name="XMMC" roattr="1" 
										 id="xmmc" fieldname="XMMC" disabled>
								</td>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									标段名称
								</th>
								<td width="17%" colspan=3 class="bottom-border">
									<input type="text" class="span12" style="width:90%;" name="BDMC" kind="text" id="bdmc" fieldname="BDMC" disabled>
									<button class="btn btn-link"  type="button" id="xmBtn" title="请选择项目"><i class="icon-edit"></i></button>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									问题描述
								</th>
								<td width="42%" colspan=3 class="bottom-border">
									<textarea class="span12" rows=6 name="PFNR" fieldname="PFNR"
										roattr="1" placeholder="必填" check-type="required"></textarea>
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									处理建议
								</th>
								<td width="42%" colspan=3 class="bottom-border">
									<textarea class="span12" rows=6 name="JYJJFA" fieldname="JYJJFA"
										roattr="1" placeholder="必填" check-type="required"></textarea>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									希望反馈日期
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12 date" type="date" name="XWWCSJ" fieldname="XWWCSJ"
										roattr="1" check-type="required">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									希望解决时间
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12 date" type="date" name="YJSJ" fieldname="YJSJ"
										id="yjsj" check-type="required">
								</td>
								<td width="50%" colspan=4 class="bottom-border">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									附件信息
								</th>
								<td width="92%" colspan=7 class="bottom-border">
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
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="Z.XMID,Z.PXH" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="ywid" id="ywid">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>
