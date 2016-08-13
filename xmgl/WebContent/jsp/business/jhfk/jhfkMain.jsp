<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%	String fklx = request.getParameter("fklx");
			String jhsjid = request.getParameter("jhsjid");
			String queryFormID = request.getParameter("queryFormID")==null?"":request.getParameter("queryFormID");
			String tabListID = request.getParameter("tabListID")==null?"":request.getParameter("tabListID");
			String queryURL = request.getParameter("queryURL")==null?"":request.getParameter("queryURL");
			queryURL = queryURL.replace("||","&");
		 %>
		<app:base />
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/css/bootstrap-switch.css?version=20140110">
		<style type="text/css">
			.historyA{
				font-size:14px;
				line-height:14px;
				overflow:hidden; 
				padding:0px;
			}
			.historyA-icon{
				line-height:12px;
				margin-top:5px;
			}
		</style>
		<script
			src="${pageContext.request.contextPath }/js/common/bootstrap-switch.js"></script>
		<title>计划反馈主页面</title>
		<script type="text/javascript" charset="utf-8">
			//请求路径，对应后台RequestMapping
			var controllername= "${pageContext.request.contextPath }/jhfk/JhfkController.do";
			g_bAlertWhenNoResult = false;
			var btnNum = 0;
			var g_fklx = "<%=fklx%>";	//全局变量	反馈类型
			var g_jhsjid = "<%=jhsjid%>";//全局变量	计划数据ID
			var g_queryFormID = '<%=queryFormID%>';
			var g_tabListID = '<%=tabListID%>';
			var g_queryURL = '<%=queryURL%>';
			//var g_fklx = '';
			//页面初始化
			$(function() {
				doInit();
				$('.label-toggle-switch').on('switch-change', function (e, data) {
					doSwitch($(this),data);
				});
				$(".btnSave").each(function(i){
					$(this).click(function(){
						var formID = $(this).attr("bindFormID");
						var fklxDetailed = g_fklx = $(this).attr("fklx");
						var fklb = $(this).attr("fklb");
						if($("#"+formID).validationButton()){
							var counts = getJhfkCounts(fklxDetailed);
							if(Number(counts)<1 || fklb!="1"){
								var data = Form2Json.formToJSON(document.getElementById(formID));
				 				var data1 = defaultJson.packSaveJson(data);
								defaultJson.doInsertJson(controllername + "?doJhsjfk&fklx="+fklxDetailed,data1,tabList,"doDeferredExecution");
							}else{
								xConfirm("提示信息","您已经对该计划进行过时间点反馈了，再次反馈将覆盖之前的数据，是否继续？");
								$('#ConfirmYesButton').unbind();
								$('#ConfirmYesButton').one("click",function(){  
					 		 		var data = Form2Json.formToJSON(document.getElementById(formID));
					 				var data1 = defaultJson.packSaveJson(data);
									defaultJson.doInsertJson(controllername + "?doJhsjfk&fklx="+fklxDetailed,data1,tabList,"doDeferredExecution");
								});
							}
						}
					});
				});
			});
			var timer = "";
			function doDeferredExecution(){
				timer = setInterval(doAutoFeedback, 500);
			}
			function doAutoFeedback(){
				clearInterval(timer);
				var str = queryXMFkxx();
				if(str=="0"){
					//如果不需要反馈，那么方法不需要执行了
					return;
				}else{
					var tempJson = convertJson.string2json1(str);
					var rowJson = tempJson.response.data[0];
					if((rowJson.FKBZ=="0" && rowJson.FKLX!="1008001")||(rowJson.FKBZ=="1" && rowJson.FKLX=="1008001")){
						//反馈标识为0，表示可以自动反馈
						var showText = "项目下所有标段时间节点已反馈，是否自动更新项目的时间节点？";
						if(rowJson.FKLX=="1008001"){
							showText = "项目下有一个标段已开工，是否自动更新项目的时间节点？";
						}
						xConfirm("提示信息",showText);
						$('#ConfirmYesButton').unbind();
						$('#ConfirmYesButton').one("click",function(){  
			 		 		var data = new Object();
			 		 		data.JHSJID = rowJson.JHSJID;
			 		 		data.FKRQ = rowJson.FKRQ;
			 		 		data.FLAG = "1";
			 				var data1 = defaultJson.packSaveJson(JSON.stringify(data));
							$.ajax({
								url:controllername + "?doJhsjfk&fklx="+g_fklx,
								data:data1,
								dataType:"json",
								async:false,
								success:function(result){
									xAlertMsg("操作成功！");
								}
							});
						});
					// FKLX!='1008001'反馈类型为开工，开工时不判断此情况
					}else if(rowJson.FKBZ!="0" && rowJson.XMRQ!="" && rowJson.FKLX!='1008001'){
						//存在时间点为空的标段，但是项目日期的值不为空，此时需要清空项目的时间点
						var data = new Object();
		 		 		data.JHSJID = rowJson.JHSJID;
		 		 		data.FKRQ = "";//清空项目的时间点
		 		 		data.FLAG = "1";
		 				var data1 = defaultJson.packSaveJson(JSON.stringify(data));
						$.ajax({
							url:controllername + "?doJhsjfk&fklx="+g_fklx+"&zdfk=1",
							data:data1,
							dataType:"json",
							async:false,
							success:function(result){
								//xAlertMsg("操作成功！");
							}
						});
					}else{
						//反馈标识不为0，表示还不需要自动反馈
					}
				}
			}
			//------------------------------------------------
			//-查询项目的反馈信息
			//------------------------------------------------
			function queryXMFkxx(){
				var str = "";
				$.ajax({
					url:controllername + "?queryXMFkxx&jhsjid="+g_jhsjid+"&fklx="+g_fklx,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						str = result.msg;
					}
				});
				return str;
			}
			//------------------------------------------------
			//-查询表格数据
			//------------------------------------------------
			function doSwitch(obj,data){
				var flag = data.value;
				var formID = obj.attr("bindFormID");
				if(flag==true){
					$("#"+formID+" .NO").hide();
					$("#"+formID+" .SI").show();
					$("button").each(function(i){
						if($(this).attr("bindFormID")==formID){
							$(this).attr("fklx",obj.attr("data-on-value"));
							$(this).attr("fklb","1");
						}
					});
				}else{
					$("#"+formID+" .NO").show();
					$("#"+formID+" .SI").hide();
					$("button").each(function(i){
						if($(this).attr("bindFormID")==formID){
							$(this).attr("fklx",obj.attr("data-off-value"));
							$(this).attr("fklb","2");
						}
					});
				}
			}
			//页面默认参数
			function doInit(){
				var str = queryFormInfo(g_fklx);
				drawForm(str);
				queryTabList();
				queryXmxx();
				getParentRowJson();
				g_bAlertWhenNoResult = true;
			}
			//-----------------------------------------
			//-查询项目信息
			//-----------------------------------------
			function queryXmxx(){
				$.ajax({
					url:controllername + "?queryXmxx&jhsjid="+g_jhsjid,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						$("#resultXML").val(result.msg);
					}
				});
			}
			//------------------------------------------------
			//-查询表格数据
			//------------------------------------------------
			function queryTabList(){
				$("#jhsjid").val(g_jhsjid);
				var fklxCond = "&fklx="+g_fklx;
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,tabList);
				defaultJson.doQueryJsonList(controllername+"?queryTabList"+fklxCond,data,tabList);
			}
			//------------------------------------------------
			//-获取计划反馈次数
			//------------------------------------------------
			function getJhfkCounts(m){
				var str = "";
				$.ajax({
					url:controllername + "?getJhfkCounts&jhsjid="+g_jhsjid+"&fklx="+m,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						str = result.msg;
					}
				});
				return str;
			}
			//------------------------------------------------
			//-查询表单数据
			//------------------------------------------------
			function queryFormInfo(fklx){
				var dom = "";
				$.ajax({
					url:controllername + "?queryFormInfo&fklx="+fklx,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						dom = result.msg;
					}
				});
				return dom;
			}
			//------------------------------------------------
			//-获取父页面可能存在的行数据
			//------------------------------------------------
			function getParentRowJson(){
				var parentRowJson = $($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
				$("#rowJsonXML").val(parentRowJson);
				return parentRowJson;
			}
			
			function getValue(){
		 		var pwindow =$(window).manhuaDialog.getParentObj();
				var rowValue = pwindow.$("#"+g_tabListID).getSelectedRow();
				return rowValue;
		 	}
			function openHistoryPage(m,n,o){
				var condJhsjid = "";
				if(n.indexOf("?")!=-1){
					condJhsjid = "&jhsjid="+o;
				}else{
					condJhsjid = "?jhsjid="+o;
				}
				$(window).manhuaDialog({"title":m,"type":"text","content":"${pageContext.request.contextPath}/"+n+condJhsjid,"modal":"2"});
			}
			//------------------------------------------------
			//-画表单
			//------------------------------------------------
			function drawForm(doc){
				if(doc=="0"){
					//没数据，下面的事都不用做了
					return;
				}
				var docJson = convertJson.string2json1(doc);
				var dataJson = docJson.response.data;
				for(var i=0;i<dataJson.length;i++){
					var rowJson = dataJson[i];
					var fkFlag = getFkFlag(rowJson.FKLX,g_jhsjid);
					var showHtml = "";
					//生成分割线
					//showHtml += '<HR style="margin:1px;FILTER: alpha(opacity=100,finishopacity=0,style=3)" width="100%" color="#987cb9" SIZE=3>';
					//生成标题
					showHtml += '<h4 class="title">';
					//showHtml += '<i class=" icon-list-alt"></i>';
					//showHtml += '<span class="badge badge-info">'+titleNum+'</span>';
					showHtml += rowJson.TITLE;
					showHtml += "&nbsp;";
					if(fkFlag=="0"){
						showHtml += "&nbsp;";
						showHtml += "</h4>";
						showHtml += "<span style='color:red;font-size:14px;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本节点不需要反馈</span>";
						//如果两个title是一致的，那么下一个也不反馈了
						for(var x=i+1;x<dataJson.length;x++){
							var nextRowJson = dataJson[x];
							if(nextRowJson.TITLE==rowJson.TITLE){
								i++;
							}
						}
						$(".formDiv").append(showHtml);
					}else if(rowJson.FKLB=="1"){
						showHtml = getSjWtHtml(rowJson,dataJson,i,showHtml);
						showHtml += '<br>';
						$(".formDiv").append(showHtml);
						$('.make-switch').each(function(){
							if($(this).attr("bindformid")==("insertForm"+i)){
								$(this).bootstrapSwitch();
							}
						});
						$("#insertForm"+i+" .NO").hide();
						i++;
					}else if(rowJson.FKLB=="2"){
						showHtml = getWtHtml(rowJson,dataJson,i,showHtml);
						$(".formDiv").append(showHtml);
					}
				}
			}
			//------------------------------------------------
			//-生成问题代码
			//------------------------------------------------
			function getWtHtml(rowJson,dataJson,i,showHtml){
				showHtml += "<span class='pull-right'>";
				showHtml += "<button class='btn btnSave' bindFormID='insertForm"+i+"' type='button' fklx='"+rowJson.FKLX+"' fklb='2'>";
				showHtml += "提交";
				showHtml += "</button>";
				showHtml += "</h4>";
				//生成表单
				showHtml += "<form method='post' action='' id='insertForm"+i+"'>";
				showHtml += "<table class='B-table' width='100%'>";
				showHtml += "<tr style='display: none;'>";
				showHtml += "<td>";
				showHtml += "<input class='span12' type='text' name='JHSJID' fieldname='JHSJID' value='"+g_jhsjid+"'>";
				showHtml += "</td>";
				showHtml += "</tr>";
				showHtml += makeWtHtml(rowJson);
				return showHtml;
			}
			//------------------------------------------------
			//-生成问题和时间代码
			//------------------------------------------------
			function getSjWtHtml(rowJson,dataJson,i,showHtml){
				var fkFkrq = getFkFkrq(rowJson.FKLX,g_jhsjid);//获取业务表可能存在的反馈日期
				var fkJhrq = getFkJhrq(rowJson.FKLX,g_jhsjid);//获取计划日期
				//生成选择控件
				showHtml += "<div class='label-toggle-switch make-switch switch-mini' bindFormID='insertForm"+i+"' data-on-label='时间' data-on-value='"+rowJson.FKLX+"' data-on='success' data-off-label='问题' data-off-value='%DATAOFFVALUE%' data-off='warning'>";
				showHtml += '<input type="checkbox" checked />';
				showHtml += '</div>';
				//生成历史链接
				if(rowJson.HISURL!=""){
					showHtml += '&nbsp;&nbsp;';
					showHtml += '<i class="icon-list historyA-icon"></i><a href="javascript:void(0);" class="historyA" onclick="openHistoryPage(\''+rowJson.HISDESC+'\',\''+rowJson.HISURL+'\',\''+g_jhsjid+'\')">'+rowJson.HISDESC+'</a>';
				}
				//生成提交按钮
				showHtml += "<span class='pull-right'>";
				showHtml += "<button class='btn btnSave' bindFormID='insertForm"+i+"' type='button' fklx='"+rowJson.FKLX+"' fklb='1'>";
				showHtml += "提交";
				showHtml += "</button>";
				showHtml += "</h4>";
				//生成表单
				showHtml += "<form method='post' action='' id='insertForm"+i+"'>";
				showHtml += "<table class='B-table' width='100%'>";
				showHtml += "<tr style='display: none;'>";
				showHtml += "<td>";
				showHtml += "<input class='span12' type='text' name='JHSJID' fieldname='JHSJID' value='"+g_jhsjid+"'>";
				showHtml += "</td>";
				showHtml += "</tr>";
				showHtml += "<tr class='SI'>";
				showHtml += "<th width='10%' class='right-border bottom-border text-right disabledTh'>";
				showHtml += rowJson.JHSJMS;
				showHtml += "</th>";
				showHtml += "<td width='40%' class='right-border bottom-border'>";
				if(fkJhrq==""){
					showHtml += "<input class='span12 date valueSI' type='date' name='JHRQ' disabled>";
				}else{
					showHtml += "<input class='span12 date valueSI' type='date' name='JHRQ' value='"+fkJhrq+"' disabled>";
				}
				showHtml += "</td>";
				showHtml += "<th width='10%' class='right-border bottom-border text-right'>";
				showHtml += rowJson.FKMS;
				showHtml += "</th>";
				showHtml += "<td width='40%' class='right-border bottom-border'>";
				if(fkFkrq==""){
					showHtml += "<input class='span12 date valueSI' type='date' name='FKRQ' fieldname='FKRQ' >";
				}else{
					showHtml += "<input class='span12 date valueSI' type='date' name='FKRQ' fieldname='FKRQ' value='"+fkFkrq+"'>";
				}
				showHtml += "</td>";
				showHtml += "</tr>";
				//如果两个title是一致的，那么在一个表单中显示
				for(var x=i+1;x<dataJson.length;x++){
					var nextRowJson = dataJson[x];
					if(nextRowJson.TITLE==rowJson.TITLE){
						showHtml += makeWtHtml(nextRowJson)
						showHtml = showHtml.replace("%DATAOFFVALUE%",nextRowJson.FKLX);
						i++;
					}
				}
				showHtml += "</table>";
				showHtml += "</form>";
				return showHtml;
			}
			function makeWtHtml(obj){
				var showHtml = "";
				showHtml += "<tr class='NO'>";
				showHtml += "<th width='20%' class='right-border bottom-border text-right'>";
				showHtml += obj.FKMS;
				showHtml += "</th>";
				showHtml += "<td width='80%' colspan=3 class='right-border bottom-border'>";
				showHtml += "<textarea class='span12 valueNO' rows='3' name='PFNR' check-type='maxlength' maxlength='4000' fieldname='BZ'>";
				showHtml += "</textarea>";
				showHtml += "</td>";
				showHtml += "</tr>";
				return showHtml;
			}
			//------------------------------------------------
			//-处理反馈内容的显示
			//------------------------------------------------
			function doRandering(obj){
				var showHtml = "";
				if(obj.FKLB=="1"){
					showHtml = '<div style="text-align:center">'+obj.FKRQ_SV+'</div>';
				}else{
					showHtml = obj.FKWT;
				}
				return showHtml;
			}
			//------------------------------------------------
			//-处理反馈内容的显示
			//-@param	m	反馈类型
			//-@param	n	计划数据ID	
			//------------------------------------------------
			function getFkFlag(m,n){
				var fkFlag = "";
				$.ajax({
					url:controllername + "?getFkFlag&fklx="+m+"&jhsjid="+n,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						fkFlag = result.msg;
					}
				});
				return fkFlag;
			}
			//------------------------------------------------
			//-获取反馈日期
			//-@param	m	反馈类型
			//-@param	n	计划数据ID	
			//------------------------------------------------
			function getFkFkrq(m,n){
				var fkFlag = "";
				$.ajax({
					url:controllername + "?getFkFkrq&fklx="+m+"&jhsjid="+n,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						fkFlag = result.msg;
					}
				});
				return fkFlag;
			}
			//------------------------------------------------
			//-获取反馈日期
			//-@param	m	反馈类型
			//-@param	n	计划数据ID	
			//------------------------------------------------
			function getFkJhrq(m,n){
				var fkFlag = "";
				$.ajax({
					url:controllername + "?getFkJhrq&fklx="+m+"&jhsjid="+n,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						fkFlag = result.msg;
					}
				});
				return fkFlag;
			}
			//------------------------------------------------
			//-页面关闭回调函数
			//-@param	m	反馈类型
			//-@param	n	计划数据ID	
			//------------------------------------------------
			function closeNowCloseFunction(){
				if(g_queryFormID==""&&g_tabListID==""){
				}else{
					$(window).manhuaDialog.getParentObj().doRefreshPageWithCondition(g_queryFormID,g_tabListID,g_queryURL);
				}
			}
		</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<p></p>
			<div class="row-fluid">
				<ul class="nav nav-tabs">
					<li class="active">
						<a href="#tab1" data-toggle="tab" id="tabPage1">统筹计划反馈</a>
					</li>
					<li class="">
						<a href="#tab2" data-toggle="tab" id="tabPage2">反馈历史查询</a>
					</li>
				</ul>
				<div class="tab-content active">
					<div class="tab-pane active" id="tab1">
						<div class="B-small-from-table-autoConcise">
							<div class="formDiv">
							</div>
						</div>
					</div>
					<div class="tab-pane" id="tab2">
						<div class="B-small-from-table-autoConcise">
							<form method="post" id="queryForm" style="display:none;">
								<table class="B-table" width="100%">
									<!--可以再此处加入hidden域作为过滤条件 -->
									<TR style="display: none;">
										<TD class="right-border bottom-border"></TD>
										<TD class="right-border bottom-border">
											<input class="span12" type="text" name="JHSJID" keep="true"
												fieldname="F.JHSJID" operation="=" id="jhsjid">
										</TD>
									</TR>
									<!--可以再此处加入hidden域作为过滤条件 -->
								</table>
							</form>
							<div class="row-fluid">
								<div class="overFlowX">
									<table class="table-hover table-activeTd B-table" id="tabList"
										width="100%" type="single" pageNum="7">
										<thead>
											<tr>
												<th name="XH" id="_XH">
													&nbsp;#&nbsp;
												</th>
												<th fieldname="TITLE" maxlength="15" tdalign="center">
													&nbsp;类型&nbsp;
												</th>
												<th fieldname="FKLB" maxlength="15" tdalign="center">
													&nbsp;反馈类别&nbsp;
												</th>
												<th fieldname="LRSJ" maxlength="15" tdalign="center">
													&nbsp;反馈时间&nbsp;
												</th>
												<th fieldname="FKNR" tdalign="left" CustomFunction="doRandering">
													&nbsp;反馈内容&nbsp;
												</th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
			</div>
		</div>
</div>
		<div align="center">
				<FORM name="frmPost" method="post" style="display: none"
					target="_blank">
					<!--系统保留定义区域-->
					<input type="hidden" name="queryXML" id="queryXML">
					<input type="hidden" name="txtXML" id="txtXML">
					<input type="hidden" name="resultXML" id="resultXML">
					<input type="hidden" name="txtFilter" order="desc"  fieldname ="F.LRSJ">
					<input type="hidden" name="queryResult" id = "queryResult">
					<input type="hidden" name="rowJsonXML" id="rowJsonXML">
					<!--传递行数据用的隐藏域-->
					<input type="hidden" name="rowData">
				</FORM>
			</div>
	</body>
</html>