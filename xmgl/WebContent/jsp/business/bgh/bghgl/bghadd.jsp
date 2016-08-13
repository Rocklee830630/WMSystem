<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<app:base/>
	<%
	request.setCharacterEncoding("utf-8");
	User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
	String username = user.getName();
	String department = user.getDepartment();
	%>
<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/banGongHuiController.do";
	$(function() {
		setPageHeight();
		//保存按钮
		var btn = $("#but_save");
		btn.click(function(){
			if($("#demoForm").validationButton()){
				var rows = $("#DT2").getTabRowsToJsonArray();
				var bghwtidStr = "";
				if(rows.length==0){
					xAlert("警告","请至少选中一个问题！",3);
					return;
				}else{
					for(var i=0;i<rows.length;i++){
						bghwtidStr += rows[i].GC_BGH_WT_ID+",";
					}
					bghwtidStr = bghwtidStr.substring(0,bghwtidStr.length-1);
					$("#GC_BGH_WT_ID").val(bghwtidStr);
					//生成json串
					var data = Form2Json.formToJSON(demoForm);
					//组成保存json串格式
					var data1 = defaultJson.packSaveJson(data);
					//调用ajax插入
					defaultJson.doInsertJson(controllername + "?insertBanGongHui", data1,null,'addHuiDiao');
				}
			}
		});
		doInit();
	});
	function doInit(){
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		defaultJson.doQueryJsonList(controllername+"?queryDshyt",data,DT1,null,false);
	}
	function setPageHeight(){
		var getHeight=getDivStyleHeight();
		var height = getHeight-243;
		$("#DT1Container").height(height);
		$("#DT2Container").height(height);
		$("#DemoContainer").css("top",height/2);
	}
	function tr_dbclick(obj,tabID){
		if(tabID=="DT1"){
			if(checkContains(obj.GC_BGH_WT_ID)){
				var rowValue=$("#DT1").getSelectedRow();//获得选中行的json对象
				$("#DT2").insertResult(rowValue,DT2,1);
				trlen=$("#DT2 tr").length-1;
				var num=document.getElementsByTagName("font");
				num[0].innerHTML=trlen;
			}else{
				xAlert("警告","该问题已经选择！",3);
			}
		}else if(tabID=="DT2"){
			var rowindex=$("#DT2").getSelectedRowIndex();
			$("#DT2").removeResult(rowindex);
		}
	}
	function checkContains(idVal){
		var rows = $("#DT2").getTabRowsToJsonArray();
		var flag = true;
		for(var i=0;i<rows.length;i++){
			if(idVal==rows[i].GC_BGH_WT_ID){
				flag = false;
			}
		}
		return flag;
	}
	//回调函数
	function addHuiDiao(){
		var fuyemian=$(window).manhuaDialog.getParentObj();
		fuyemian.initfuyemian(); 
		$(window).manhuaDialog.close();
	}
</script>   
</head>
<body>
<app:dialogs/>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						<span class="pull-right">
							<button id="but_save" class="btn" type="button">
								保存
							</button> </span>
					</h4>
					<form method="post"
						action="${pageContext.request.contextPath }/insertdemo.xhtml"
						id="demoForm">
						<input class="span12" id="GC_BGH_ID" type="hidden"
							fieldname="GC_BGH_ID" name="GC_BGH_ID">
						<table class="B-table" width="100%">

							<TR style="display: none">
								<TD class="right-border bottom-border">
									<input class="span12" id="ZT" type="text" name="ZT"
										fieldname="ZT">
								</TD>
							</TR>
							<tr>
								<th width="8%" class="right-border bottom-border text-right ">
									会议时间
								</th>
								<td class="right-border bottom-border">
									<input class="span12" id="HYSJ" type="datetime-local"
										name="HYSJ" fieldname="HYSJ">
								</td>
								<th width="8%" class="right-border bottom-border text-right ">
									会次
								</th>
								<td class="right-border bottom-border">
									<input class="span12" check-type="required" placeholder="必填"
										type="text" maxlength="30" id="HC" name="HC" fieldname="HC">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right ">
									会议地点
								</th>
								<td class="right-border bottom-border">
									<input class="span12" check-type="required" placeholder="必填"
										type="text" id="HYDD" maxlength="100" name="HYDD"
										fieldname="HYDD">
								</td>
								<th width="8%" class="right-border bottom-border text-right ">
									会次主持
								</th>
								<td class="right-border bottom-border">
									<input class="span12" check-type="required" placeholder="必填"
										type="text" id="HYZC" maxlength="30" name="HYZC"
										fieldname="HYZC">
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right ">
									参会
								</th>
								<td class="right-border bottom-border" colspan="3">
									<textarea rows="3" class="span12" check-type="required"
										placeholder="必填" id="CHBM" maxlength="100" name="CHBM"
										fieldname="CHBM"></textarea>
								</td>
							</tr>
							<tr style="display:none;">
								<th>问题ID</th>
								<td>
									<input class="span12" 
										type="text" id="GC_BGH_WT_ID" name="GC_BGH_WT_ID"
										fieldname="GC_BGH_WT_ID">
								</td>
							</tr>
							<!-- 	<tr>
          			<th class="right-border bottom-border text-right ">参会人</th>
          			<td class="right-border bottom-border"  colspan="3">
          			<textarea rows="3" class="span12"  check-type="required" placeholder="必填"   id="CHRY"  name = "CHRY" fieldname="CHRY"></textarea>
          			</td>
        		</tr> -->
						</table>
					</form>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6" id="DT1Container" style="overflow-y:auto;">
					<div style="display:none;">
						<form action="" id="queryForm"></form>
					</div>
					<div class="B-small-from-table-autoConcise">
						<h4 class="title">
							待上会议题
						</h4>
						<div class="overFlowX">
							<table width="100%" class="table-hover table-activeTd B-table"
								id="DT1" type="single" nopage="true" pageNum="1000">
								<thead>
									<tr>
										<th name="XH" id="_XH">
											&nbsp;#&nbsp;
										</th>
										<th fieldname="WTBT" maxlength="12">
											&nbsp;标题&nbsp;
										</th>
										<th fieldname="WTLX" tdalign="center" >
											&nbsp;类型&nbsp;
										</th>
										<th fieldname="ZT" tdalign="center" >
											&nbsp;状态&nbsp;
										</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="span1" style="position:relative;">
					<div id="DemoContainer" style="position:absolute;">
						双击<i class="icon-chevron-right"></i>
						<br>
						选择<i class="icon-chevron-right"></i>
					</div>
				</div>
				<div class="span5" id="DT2Container" style="overflow-y:auto;">
					<div class="B-small-from-table-autoConcise">
						<h4 class="title">
							本次上会议题
						</h4>
						<div class="overFlowX">
							<table width="100%" class="table-hover table-activeTd B-table"
								id="DT2" type="single">
								<thead>
									<tr>
										<th name="XH" id="_XH">
											&nbsp;#&nbsp;
										</th>
										<th fieldname="WTBT" maxlength="12">
											&nbsp;标题&nbsp;
										</th>
										<th fieldname="WTLX" tdalign="center" >
											&nbsp;类型&nbsp;
										</th>
										<th fieldname="ZT" tdalign="center" >
											&nbsp;状态&nbsp;
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
		<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
 <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		 <!--系统保留定义区域-->
		   <input type="hidden" name="ywid" id = "ywid" value="">
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>