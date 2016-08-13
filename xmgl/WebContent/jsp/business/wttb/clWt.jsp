<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%@ page import="com.ccthanking.framework.common.User"%>
		<%@ page import="com.ccthanking.framework.common.Role"%>
		<%@ page import="com.ccthanking.framework.Globals"%>
		<app:base />
		<%	String leader = request.getParameter("flag");
			String id = request.getParameter("infoID");
			leader = leader==null || leader==""?"0":leader;
			String openType = request.getParameter("openType");
			//判断当前人是否是常务副主任角色，如果是，要额外提供一个“提交到会议中心按钮”
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			Role[] role = user.getRoles();
			String cwfzr = "";
			for(int i=0;i<role.length;i++){
				String roleID = role[i].getRoleId();
				if("5aa5956e-947b-4d76-a215-827be93fa33e".equals(roleID)){
					cwfzr = "1";
					break;
				}
			}
		%>
		<title>处理问题</title>
		<script src="${pageContext.request.contextPath }/js/common/FixTable.js"></script> 
		<script type="text/javascript" charset="utf-8">
			var controllername= "${pageContext.request.contextPath }/wttb/wttbController.do";
			var btnNum = 0;
			var flag = '<%=leader%>';
			var id = '<%=id%>';
			var p_cwfzr = '<%=cwfzr%>';
			var g_openType = '<%=openType%>';//如果等于1，那么表示是部长的处理页面，否则是给其他页面使用的详细信息页面
			var getHeight=getDivStyleHeight()-20;
			var p_wttbJSON = "";
			var historyListHeight = 0;//这个变量用于计算历史列表最大允许的高度
			
			//计算本页表格分页数
			function setPageHeight() {
				var height = getHeight-pageTopHeight-pageTitle-pageTitle-getTableTh(1)-$("#insertForm").height()-pageNumHeight;
				var pageNum = parseInt(Math.abs(height)/pageTableOne,10);
				$("#DT0").attr("pageNum", pageNum);
			}
			$(function(){
				setPageHeight();
				doInit();
				$("#btnSave").click(function(){
					if($("#zbrName").val()==undefined){
						xAlert("请至少选择一位主办人!"); 
						return;
					}else{
						if($("#insertForm").validationButton()){
			 				//生成json串
			 				var zbrStr = $("#zbrName").attr("code")==undefined?"":$("#zbrName").attr("code");
			 				var sbrStr = $("#sbrName").attr("code")==undefined?"":$("#sbrName").attr("code");
			 				var jsrStr = $("#jsrName").attr("code")==undefined?"":$("#jsrName").attr("code");
			 				var jsrString = zbrStr+":"+sbrStr+":"+jsrStr;
			 				$("#jsr").val(jsrString);
			 		 		var data = Form2Json.formToJSON(insertForm);
							$(window).manhuaDialog.getParentObj().setYwid($("#ywid").val());
							$(window).manhuaDialog.setData(data);
							$(window).manhuaDialog.sendData();
							$(window).manhuaDialog.close();
						}
					}
				});
				$("#btnTransfer").click(function(){
					$("#resultXML").val($("#DT0").getSelectedRow());
					$(window).manhuaDialog({"title":"问题提报>转发问题","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/zfWt.jsp?infoID="+id,"modal":"4"});
				});
				$("#btnReply").click(function(){
					$("#resultXML").val($("#DT0").getSelectedRow());
					$(window).manhuaDialog({"title":"问题提报>回复问题","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/pfWt.jsp?infoID="+id,"modal":"4"});
				});
				$("#btnHyzx").click(function(){
					xConfirm("提示信息","提交后问题将进入会议中心流程，是否确认？");
 					$('#ConfirmYesButton').unbind();
 					$('#ConfirmYesButton').one("click",function(){
 						$.ajax({
							url:controllername + "?doSendToHyzx&wtid="+id,
							data:"",
							dataType:"json",
							async:false,
							success:function(result){
								var res = result.msg;
								if(res=="true"){
									$("#btnHyzx").remove();
	 								$("#btnTransfer").remove();
									xAlertMsg("操作成功！");
								}else{
									xAlert("提交过程发生错误！"); 
								}
							}
						});
 					});
				});
				//按钮绑定事件（导出）
				$("#btnExp").click(function() {
					if(exportRequireQuery($("#DT0"))){
						//printTabList("DT0");
						printTabList("DT0","wttb_wtcl.xls","BLRJS,JSR,PFSJ,PFNR,JYJJFA","2,1");
					}
				});
			});
			//初始化方法
			function doInit(){
				doSearch();
				getWtInfo(id);
				btnHandel();
			}
			//查询批复情况列表
			function doSearch(){
				//add by zhangbr@ccthanking.com 现场说不要固定表头列头了，不好看，改回分页的形式. 问题编号：0000668
				//historyListHeight = getHeight-pageTopHeight-pageTitle-pageTitle-getTableTh(1)-$("#insertForm").height();
				var data = combineQuery.getQueryCombineData(queryForm0,frmPost,DT0);
				//defaultJson.doQueryJsonList(controllername + "?queryPfqk&id="+id,data,DT0,"doFixedTableHeader",true);
				defaultJson.doQueryJsonList(controllername + "?queryPfqk&id="+id,data,DT0,null,true);
			}
			//------------------------------------------------
			//-固定表头列头
			//------------------------------------------------
			function doFixedTableHeader(){
				var rows = $("tbody tr" ,$("#DT0"));
				var tr_obj = rows.eq(0);
				var t = $("#DT0").getTableRows();
				var tr_height = $(tr_obj).height();
				var d = (t*tr_height)+(getTableTh(3)/2);
				if(d>historyListHeight){
					d = historyListHeight;
				}
				window_width = window.screen.availWidth;
				$("#DT0").fixTable({
					fixColumn: 0,//固定列数
					width:window_width-55,//显示宽度
					height:d//显示高度
				});			
			}
			function closeWindow(obj){
				obj.manhuaDialog.close();
				//$("#btnSave").attr("disabled","true");
				//$("#btnSendBack").attr("disabled","true");
				//$(window).manhuaDialog.close();
			}
			function getWtInfo(n){
				$.ajax({
					url:controllername + "?queryInfoByWtid&wtid="+n,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						var res = result.msg;
						var p_wttbJSON = convertJson.string2json1(res).response.data[0];
						$("#resultXML").val(JSON.stringify(p_wttbJSON));
						$("#insertForm").setFormValues(p_wttbJSON);
					}
				});
			}
			//获取办理人角色
			function getBlrjs(){
				var blrjs = "";
				$.ajax({
					url:controllername + "?queryBlrjs&wtid="+id,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						var res = result.msg;
						var tempJson = convertJson.string2json1(res);
						var row = tempJson.response.data[0];
						blrjs = row.BLRJS;
					}
				});
				return blrjs;
			}
			function btnHandel(){
				if(g_openType=="1"){
					if(p_wttbJSON==""){
						p_wttbJSON=$("#resultXML").val();
					}
					var tempJson = convertJson.string2json1(p_wttbJSON);
					var blrjs = "";
					/**
					if(tempJson.BLRJS==undefined){
						blrjs = getBlrjs();
					}else{
						blrjs = tempJson.BLRJS;
					}
					*/
					blrjs = getBlrjs();//这个值还是都从后台取吧，因为角色是动态变化的
					if(blrjs=="1"){
						if(tempJson.SJZT=="7"){
							$("#btnTransfer").hide();
						}else{
							$("#btnTransfer").show();
							//领导处理页有一个特殊功能--如果当前用户有“常务副主任”角色，并且该人员是问题主办人，那么提供一个特殊按钮：“提交到会议中心”
							if(p_cwfzr=="1"){
								var showHtml = '&nbsp;<button id="btnHyzx" class="btn" type="button">';
								showHtml +=	'提交到会议中心';
								showHtml +=	'</button>';
								$("#btnTransfer").after(showHtml);
							}
						}
					}else{
						$("#btnTransfer").hide();
						$("#btnHyzx").hide();
					}
					
				}else{
					$("#btnTransfer").hide();
					$("#btnReply").hide();
				}
			}
			//转发按钮--回调函数
			function doTransfer(data){
				var data1 = defaultJson.packSaveJson(data);
				$.ajax({
					url:controllername + "?doTransfer&ywid="+$("#ywid").val(),
					data:data1,
					dataType:"json",
					async:false,
					success:function(result){
						doSearch();
						btnHandel();
					}
				});
			}
			//回复按钮--回调函数
			function doReply(data){
				var data1 = defaultJson.packSaveJson(data);
				$.ajax({
					url:controllername + "?doSugg&ywid="+$("#ywid").val(),
					data:data1,
					dataType:"json",
					async:false,
					success:function(result){
						xAlertMsg("操作成功！");
						doSearch();
						btnHandel();
					}
				});
			}
			function setYwid(n){
				$("#ywid").val(n);
			}
			//子页面关闭前，对全局变量进行赋值，用于控制父页面的事件
			function setChildBtnNum(n){
			}
			//----------------------------------------
			//-隐藏批复内容
			//----------------------------------------
			function hideJjfa(obj){
				if(obj.BLRJS=="3" || ((obj.BLRJS=="2"||obj.BLRJS=="4") && obj.JYJJFA=="")){
					return '<div style="text-align:center;">—</div>';
				}
			}
		</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						问题处理情况
						<span class="pull-right">
							<button id="btnExp" class="btn" type="button">
								导出
							</button>
						</span>
					</h4>
					<form method="post" action="" id="queryForm0">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<INPUT type="text" class="span12" kind="text"
										fieldname="rownum" value="1000" operation="<=">
								</TD>
								<td>人员角色隐藏条件-用于控制按钮，不传入后台</td>
								<TD class="right-border bottom-border">
									<INPUT type="text" class="span12" kind="text" id="ryjs_cond">
								</TD>
								
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<table width="100%" class="table-hover table-activeTd B-table"
						type="single" id="DT0" pageNum="1000">
						<thead>
							<tr>
								<th name="XH" id="_XH">
									&nbsp;#&nbsp;
								</th>
								<th fieldname="BLRJS" tdalign="center">
									&nbsp;角色&nbsp;
								</th>
								<th fieldname="JSR">
									&nbsp;人员&nbsp;
								</th>
								<th fieldname="PFSJ" tdalign="center">
									&nbsp;处理时间&nbsp;
								</th>
								<th fieldname="PFNR" maxlength="20">
									&nbsp;内容&nbsp;
								</th>
								<th fieldname="JYJJFA" maxlength="20" CustomFunction="hideJjfa">
									&nbsp;建议解决方案&nbsp;
								</th>
								<th fieldname="FJ">
									&nbsp;附件&nbsp;
								</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						问题信息
						<span class="pull-right">
							<button id="btnReply" class="btn" type="button">
								回复
							</button>
							<button id="btnTransfer" class="btn" type="button">
								上报/转发
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
									<input class="span12" type="text" name="ID" fieldname="ID"
										id="wtid">
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
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									问题标题
								</th>
								<td width="42%" colspan=3 class="right-border bottom-border">
									<input class="span12" type="text" name="WTBT" roattr="1" disabled
										fieldname="WTBT" placeholder="必填" check-type="required">
								</td>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									问题类别
								</th>
								<td width="17%" class="bottom-border">
									<select class="span12 4characters" name="WTLX" fieldname="WTLX" kind="dic" disabled
										src="WTLX" roattr="1">
									</select>
								</td>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									问题性质
								</th>
								<td width="17%" class="bottom-border">
									<select class="span12 6characters" name="WTXZ" fieldname="WTXZ" kind="dic" disabled
										src="WTXZ" roattr="1">
									</select>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									项目名称
								</th>
								<td width="42%" colspan=3 class="right-border bottom-border">
									<input class="span12" type="text" name="XMMC" roattr="1" 
										 id="xmmc" fieldname="XMMC" disabled>
								</td>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									标段名称
								</th>
								<td  width="42%" colspan=3 class="bottom-border">
									<input type="text" class="span12" name="BDMC" kind="text" id="bdmc" fieldname="BDMC" disabled>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									问题描述
								</th>
								<td width="42%" colspan=3 class="bottom-border">
									<textarea class="span12" rows=5 name="PFNR" fieldname="PFNR"
										roattr="1" disabled></textarea>
								</td>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									处理建议
								</th>
								<td width="42%" colspan=3 class="bottom-border">
									<textarea class="span12" rows=5 name="JYJJFA" fieldname="JYJJFA"
										roattr="1" disabled></textarea>
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
				<input type="hidden" name="queryResult" id = "queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>
