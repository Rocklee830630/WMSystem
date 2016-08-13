<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%@ page import="com.ccthanking.framework.common.User"%>
		<%@ page import="com.ccthanking.framework.Globals"%>
		<%	String leader = request.getParameter("flag");
			String id = request.getParameter("infoID");
			leader = leader==null || leader==""?"0":leader;
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String deptid = user.getDepartment();
			String resendFlag = request.getParameter("resendFlag");
		%>
		<app:base />
		<title>新增问题</title>
		<script type="text/javascript" charset="utf-8">
			var controllername= "${pageContext.request.contextPath }/wttb/wttbController.do";
			var btnNum = 0;
			var childBtnNum = 0;
			var flag = '<%=leader%>';
			var resendFlag = '<%=resendFlag%>';
			var id = '<%=id%>';
			$(function(){
				doInit();
				$("#btnSave").click(function(){
					if($("#zbrName").val()==undefined){
						xAlert("请至少选择一位主办人!"); 
						return;
					}else{
						if($("#insertForm").validationButton()){
			 				//生成json串
			 				var zbrStr = $("#zbrName").val()==undefined?"":$("#zbrName").val();
			 				var sbrStr = $("#sbrName").attr("code")==undefined?"":$("#sbrName").attr("code");
			 				var jsrStr = $("#jsrName").val()==undefined?"":$("#jsrName").val();
			 				var jsrString = zbrStr+":"+sbrStr+":"+jsrStr;
			 				if(sbrStr.indexOf(zbrStr)!=-1){
								xAlert("警告","送办人员包含了主办人，请删除重复数据!","3");
								return;
			 				}
			 				if(sbrStr.indexOf(jsrStr)!=-1){
								xAlert("警告","送办人员包含了部门发起负责人，请删除重复数据!","3");
								return;
			 				}
			 				if(zbrStr.indexOf(jsrStr)!=-1){
								xAlert("警告","主办人和部门发起负责人重复，请修改主办人!","3");
								return;
			 				}
			 				$("#jsr").val(jsrString);
			 				var zbrName = getUserNameByAccount(zbrStr);
		 		 			xConfirm("提示信息","是否确认提报？主送人："+zbrName);
							$('#ConfirmYesButton').unbind();
							$('#ConfirmYesButton').one("click",function(){
				 		 		var data = Form2Json.formToJSON(insertForm);
								$(window).manhuaDialog.getParentObj().doConfirmSend(data);
								$(window).manhuaDialog.close();
							});
						}
					}
				});
				//送达按钮事件
				$("#btnSend").click(function(){
					if($("#insertForm").validationButton()){
			 			var zbrStr = $("#zbrName").val()==undefined?"":$("#zbrName").val();
		 				var sbrStr = $("#sbrName").attr("code")==undefined?"":$("#sbrName").attr("code");
		 				var jsrStr = $("#jsrName").val()==undefined?"":$("#jsrName").val();
		 				var jsrString = zbrStr+":"+sbrStr+":"+jsrStr;
		 				$("#jsr").val(jsrString);
			 			var zbrName = getUserNameByAccount(zbrStr);
	 		 			if(flag=="0"){
		 		 			zbrName = $("#jsrName  option:selected").text();
		 		 			if(zbrStr==jsrStr){
		 		 				//如果主送人就是本部门部长，那么就显示主送人
		 		 				sjztFlag = "&sjzt=2";
		 		 				xConfirm("提示信息","是否确认提报？主送人："+zbrName);
			 				}else{
			 					//不然显示发起负责人
	 		 					xConfirm("提示信息","是否确认提报？发起负责人："+zbrName);
	 		 				}
		 		 		}else{
	 		 				xConfirm("提示信息","是否确认提报？主送人："+zbrName);
		 		 		}
						$('#ConfirmYesButton').unbind();
						$('#ConfirmYesButton').one("click",function(){
			 		 		var data = Form2Json.formToJSON(insertForm);
							$(window).manhuaDialog.getParentObj().setYwid($("#ywid").val());
							$(window).manhuaDialog.getParentObj().sendReport(data);
							//$(window).manhuaDialog.getParentObj().doShowMsg("操作成功！");
							$(window).manhuaDialog.close();
						});
					}
				});
				//退回按钮事件
				$("#btnSendBack").click(function(){
					btnNum = 5;
					$(window).manhuaDialog({"title":"问题提报>处理意见","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/pfWt.jsp?infoID="+id,"modal":"4"});
				});
				$("#zbrBtn").click(function(){
					btnNum = 1;
					openUserTree("single",$("#zbrName").attr("code"),"");
				});
				$("#sbrBtn").click(function(){
					btnNum = 2;
					selectUserTree("multi",$("#sbrName").attr("code"),"");
				});
				$("#jsrBtn").click(function(){
					btnNum = 4;
					openUserTree("single","","");
				});
				$("#xmBtn").click(function(){
					btnNum = 3;
					queryProjectWithBD();
				});
				$("#btnCancel").click(function(){
					$(window).manhuaDialog.close();
				});
				//提报类型radio点击事件
				$("label input[name=TBLX]").click(function(){
					changeDic($(this).val());
				});
				$("#btnResend").click(function(){
					if($("#insertForm").validationButton()){
			 			var zbrStr = $("#zbrName").val()==undefined?"":$("#zbrName").val();
		 				var sbrStr = $("#sbrName").attr("code")==undefined?"":$("#sbrName").attr("code");
		 				var jsrStr = $("#jsrName").val()==undefined?"":$("#jsrName").val();
		 				var jsrString = zbrStr+":"+sbrStr+":"+jsrStr;
		 				$("#jsr").val(jsrString);
			 			var zbrName = getUserNameByAccount(zbrStr);
	 		 			xConfirm("提示信息","是否确认提报？主送人："+zbrName);
						$('#ConfirmYesButton').unbind();
						$('#ConfirmYesButton').one("click",function(){
			 		 		var data = Form2Json.formToJSON(insertForm);
							$(window).manhuaDialog.getParentObj().setYwid($("#ywid").val());
							$(window).manhuaDialog.getParentObj().doResend(data);
							//$(window).manhuaDialog.getParentObj().doShowMsg("操作成功！");
							$(window).manhuaDialog.close();
						});
					}
				});
			});
			function doInit(){
				getWtInfo(id);
				getPsnList(id);
				getPfqk(id);
				ctrlButton();
			}
			function ctrlButton(){
				if(resendFlag=="1"){
					$("#btnSendBack").hide();
					if(flag=="1"){
						//部门负责人
						if($("#sjzt").val()=="5"){
							$("#btnResend").hide();
							$("#btnSave").show();
							$("#btnSave").text("提交");
						}else{
							$("#btnSave").hide();
							$("#btnResend").show();
						}
						$("#btnSend").hide();
						$("#jsrTr").hide();
					}else{
						$("#btnSave").hide();
						if($("#sjzt").val()=="5"){
							$("#btnSend").show();
							$("#btnResend").hide();
						}else{
							$("#btnSend").hide();
							$("#btnResend").show();
						}
						$("#jsrTr").show();
					}
					setFileData($("#pfid").val(),"","","");
					queryFileData($("#pfid").val(),"","","");
				}else{
					$("#btnResend").hide();
					if(flag=="1"){
					//部门负责人
						$("#btnSend").hide();
						if($("#sjzt").val()=="1"){
							$("#btnSendBack").show();
							$("#btnSave").show();
							$(".btn-fileUpload").show();
						}else{
							$("#btnSendBack").hide();
							$("#btnSave").hide();
							$(".btn-fileUpload").hide();
							$("input").attr("disabled","true");
							$("select").attr("disabled","true");
							$("textarea").attr("disabled","true");
							$("button").remove();
							$("th").addClass("disabledTh");
							$("#sbrName").removeAttr("style");
							$("#sbrName").attr("class","span12");
							$("#bdmc").removeAttr("style");
							$("#bdmc").attr("class","span12");
						}
						$("#jsrTr").hide();
					}else{
					//办事人
						if($("#sjzt").val()=="5"){
							$("#btnSend").show();
							$("#jsrTr").show();
							$(".btn-fileUpload").show();
						}else{
							$("#btnSend").hide();
							$("#jsrTr").hide();
							$(".btn-fileUpload").hide();
							$("#fileTab").attr("onlyView","true");
							$("input").attr("disabled","true");
							$("select").attr("disabled","true");
							$("textarea").attr("disabled","true");
							$("button").remove();
							$("th").addClass("disabledTh");
							$("#sbrName").removeAttr("style");
							$("#sbrName").attr("class","span12");
							$("#bdmc").removeAttr("style");
							$("#bdmc").attr("class","span12");
						}
						$("#btnSendBack").hide();
						$("#btnSave").hide();
					}
					//控制删除按钮
					if("2,3,4,5,6".indexOf($("#sjzt").val())!=-1){
						$("#fileTab").attr("onlyView","true");
					}
					setFileData($("#pfid").val(),"","","");
					queryFileData($("#pfid").val(),"","","");
				}
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
			function getWtInfo(n){
				$.ajax({
					url:controllername + "?queryInfoByWtid&wtid="+n,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						var res = result.msg;
						var tempJson = convertJson.string2json1(res).response.data[0];
						$("#resultXML").val(JSON.stringify(tempJson));
						$("#insertForm").setFormValues(tempJson);
						changeDic(tempJson.TBLX);
					}
				});
			}
			function getPfqk(n){
				$.ajax({
					url:controllername + "?queryPfqk&id="+n,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						var res = result.msg;
						var tempJson = convertJson.string2json1(res);
						for(var i=0;i<tempJson.response.data.length;i++){
							var row = tempJson.response.data[i];
							if(row.NRLX=="0"){
								$("#pfnr").html(row.PFNR);
								$("#jyjjfa").html(row.JYJJFA);
								$("#xwwcsj").val(row.XWWCSJ);
							}
						}
					}
				});
			}
			function getPsnList(n){
				$.ajax({
					url:controllername + "?queryLzls&id="+n,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						var res = result.msg;
						var tempJson = convertJson.string2json1(res);
						var zbrStr = "";
						var zbrCodeStr = "";
						var sbrStr = "";
						var sbrCodeStr = "";
						var blrStr = "";
						var blrCodeStr = "";
						for(var i=0;i<tempJson.response.data.length;i++){
							var row = tempJson.response.data[i];
							if(row.BLRJS=="1"){
								zbrStr +=row.JSR_SV+",";
								zbrCodeStr +=row.JSR+",";
							}else if(row.BLRJS=="4"){
								sbrStr +=row.JSR_SV+",";
								sbrCodeStr +=row.JSR+",";
							}else if(row.BLRJS=="3"){
								blrStr +=row.JSR_SV+",";
								blrCodeStr +=row.JSR+",";
								//$("#xwwcsj").val(row.XWWCSJ);
							}
						}
						zbrStr = zbrStr.length==0?"":zbrStr.substring(0,zbrStr.length-1);
						zbrCodeStr = zbrCodeStr.length==0?"":zbrCodeStr.substring(0,zbrCodeStr.length-1);
						sbrStr = sbrStr.length==0?"":sbrStr.substring(0,sbrStr.length-1);
						sbrCodeStr = sbrCodeStr.length==0?"":sbrCodeStr.substring(0,sbrCodeStr.length-1);
						blrStr = blrStr.length==0?"":blrStr.substring(0,blrStr.length-1);
						blrCodeStr = blrCodeStr.length==0?"":blrCodeStr.substring(0,blrCodeStr.length-1);
						//$("#zbrName").val(zbrStr);//由于改用了下拉框，所以赋值使用code，不能用直接用value
						$("#zbrName").val(zbrCodeStr);
						$("#zbrName").attr("code",zbrCodeStr);
						$("#sbrName").val(sbrStr);
						$("#sbrName").attr("code",sbrCodeStr);
						//$("#jsrName").val(blrStr);
						$("#jsrName").val(blrCodeStr);
						$("#jsrName").attr("code",blrCodeStr);
					}
				});
			}
			function closeWindow(obj){
				obj.manhuaDialog.close();
				$("#btnSave").attr("disabled","true");
				$("#btnSendBack").attr("disabled","true");
				setInterval(closeMySelf, 500);
				//$(window).manhuaDialog.close();
			}
			function closeMySelf(){
				$(window).manhuaDialog.getParentObj().doShowMsg("操作成功！");
				$(window).manhuaDialog.close();
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
					var data = eval("("+rowsArr+")");
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
			//回复按钮--回调函数
			function doReply(data){
				var data1 = defaultJson.packSaveJson(data);
				$.ajax({
					url:controllername + "?doSendBack&ywid="+$("#ywid").val(),
					data:data1,
					dataType:"json",
					async:false,
					success:function(result){
					}
				});
			}
			//设置业务ID
			function setYwid(n){
				$("#ywid").val(n);
			}
			//子页面关闭前，对全局变量进行赋值，用于控制父页面的事件
			function setChildBtnNum(n){
				childBtnNum = btnNum*10+n;
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
					<h4 class="title">
						<span class="pull-right" id="btnSpan">
							<button id="btnSave" class="btn" type="button">
								确认提报
							</button>
							<button id="btnSendBack" class="btn" type="button">
								处理意见
							</button>
							<button id="btnSend" class="btn" type="button">
								提交
							</button>
							<button id="btnResend" class="btn" type="button">
								提交
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
									问题状态
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="hidden" name="SJZT" fieldname="SJZT"
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
									<input class="span12" type="text" name="WTTB_PFQK_ID" fieldname="WTTB_PFQK_ID"
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
							<tr id="jsrTr">
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
									<select class="span12 5characters" name="WTLX" fieldname="WTLX" kind="dic"
										src="WTLX" roattr="1">
									</select>
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									问题性质
								</th>
								<td width="17%" class="bottom-border">
									<select class="span12 6characters" name="WTXZ" fieldname="WTXZ" kind="dic"
										src="WTXZ" roattr="1">
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
									<textarea class="span12" rows=6 name="PFNR" fieldname="PFNR" id="pfnr"
										roattr="1" placeholder="必填" check-type="required"></textarea>
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									处理建议
								</th>
								<td width="42%" colspan=3 class="bottom-border">
									<textarea class="span12" rows=6 name="JYJJFA" fieldname="JYJJFA"
										roattr="1" id="jyjjfa" placeholder="必填" check-type="required"></textarea>
								</td> 
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									希望反馈日期
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12 date" type="date" name="XWWCSJ" fieldname="XWWCSJ"
										id="xwwcsj" check-type="required">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									希望解决时间
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12 date" type="date" name="YJSJ" fieldname="YJSJ"
										id="yjsj" check-type="required">
								</td>
								<td width="50%" colspan=6 class="bottom-border">
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
											<tbody id="fileTab" fjlb="0045" class="files showFileTab"
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
