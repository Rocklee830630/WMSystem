<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<style type="text/css">
	.treeFrame{
		width:100%;
		height:99%;
	}
</style>
</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span3" id="treeFrameDiv">
					<div class="span12" style="text-align:center;">
						<app:oPerm url="/jsp/business/bgh/bghgl/sqshytgl.jsp?delete">
							<button  type="button"  class="btn btn-link" id="deleteBtn">删除</button>
						</app:oPerm>
						<app:oPerm url="/jsp/business/bgh/bghgl/sqshytgl.jsp?update">
							<button  type="button"  class="btn btn-link" id="updateBtn">修改</button>
						</app:oPerm>
						<app:oPerm url="/jsp/business/bgh/bghgl/sqshytgl.jsp?finish">
							<button  type="button" class="btn btn-link" id="endBtn">结束会议</button>
						</app:oPerm>
					</div>
					<iframe id="treeFrame" class="treeFrame" scrolling="no"
						style="border-style: none; background: #FFF; box-shadow: none;"
						src="${pageContext.request.contextPath}/jsp/business/bgh/showBanGongHuiTree.jsp"></iframe>
				</div>
				<div class="span9" style="margin-left: 5px;">
					<div class="B-small-from-table-autoConcise">
						<h4 class="title">
							<span class="pull-right"> <app:oPerm
									url="/jsp/business/bgh/bghgl/sqshytgl.jsp">
									<button class="btn" id="ytxzBtn">
										新增议题
									</button>
								</app:oPerm> <app:oPerm url="/jsp/business/bgh/bghgl/sqshytgl.jsp">
									<button class="btn" id="sqshwtBtn">
										申请上会议题
									</button>
								</app:oPerm> <app:oPerm url="/jsp/business/bgh/bghgl/yhjy.jsp">
									<button class="btn" id="ytdyBtn">
										议题打印
									</button>
								</app:oPerm> <app:oPerm url="/jsp/business/bgh/bghgl/bghwtedit.jsp">
									<button class="btn" id="ytwhqBtn">
										议题维护
									</button>
								</app:oPerm> <app:oPerm url="/jsp/business/bgh/bghgl/yhjy.jsp1">
									<button class="btn" id="hyjyqBtn">
										会议纪要
									</button>
								</app:oPerm> <app:oPerm url="/jsp/business/bgh/bghgl/delete1">
									<button class="btn" id="hyjyqBtn" onclick="deleteBghyt()">
										删除
									</button>
								</app:oPerm> </span>
						</h4>

						<form method="post" id="queryForm">
							<INPUT style="display: none;" type="text" class="span12"
								kind="text" id="PARENT_ID" fieldname="GC_BGH_ID" value=""
								operation="=" />
						</form>

						<div style="height: 83px;">
							<font><b>会议状态：</b><span id="hyzt"></span>
							</font>
							<br>
							<font><b>会议日期：</b><span id="hyrq"></span>
							</font>
							<br>
							<font><b>参会：</b><span id="chbm"></span>
							</font>
							<br>
						</div>
						<table width="100%" class="table-hover table-activeTd B-table"
							id="DT1" type="single" pageNum="5">
							<thead>
								<tr>
									<th name="XH" id="_XH">
										&nbsp;#&nbsp;
									</th>
									<th fieldname="WTBT" tdalign="center"
										CustomFunction="doRandering">
										&nbsp;&nbsp;
									</th>
									<th fieldname="WTBT" maxlength="5">
										&nbsp;标题&nbsp;
									</th>
									<th fieldname="WTLX">
										&nbsp;类型&nbsp;
									</th>
									<th fieldname="ISJJ" tdalign="center">
										&nbsp;解决&nbsp;
									</th>
									<th fieldname="JJSJ">
										&nbsp;解决时间&nbsp;
									</th>
									<th fieldname="FQR">
										&nbsp;发起人&nbsp;
									</th>
									<th fieldname="LRSJ">
										&nbsp;发起时间&nbsp;
									</th>
									<th fieldname="BGHJL" maxlength="5">
										&nbsp;会议结论&nbsp;
									</th>
									<th fieldname="BGHHF" maxlength="5">
										&nbsp;会议答复&nbsp;
									</th>
									<th fieldname="DBCS" tdalign="right">
										&nbsp;督办&nbsp;
									</th>
									<th fieldname="FJ">
										&nbsp;附件&nbsp;
									</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" />
				<input type="hidden" name="txtXML" />
				<input type="hidden" name="resultXML" id="resultXML" />
				<input type="hidden" name="txtFilter" order="desc"
					fieldname="fqsj,lrsj" />
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData" />
				<input type="hidden" name="selectrow" id="selectrow" />
				<input type="hidden" name="queryResult" id="queryResult" />
			</FORM>
		</div>
		<div>
			<form method="post"
				action="${pageContext.request.contextPath }/insertdemo.xhtml"
				id="demoForm">
				<input class="span12" id="GC_BGH_ID" type="hidden"
					fieldname="GC_BGH_ID" name="GC_BGH_ID">
				<table class="B-table" width="100%">
					<TR style="display: none">
						<TD class="right-border bottom-border">
							<input class="span12" id="HYZT" type="text" name="HYZT"
								fieldname="HYZT">
						</TD>
					</TR>
				</table>
			</form>
		</div>
		<script type="text/javascript">
	var controllername= "${pageContext.request.contextPath }/banGongHuiController.do";
	var controllername_del= "${pageContext.request.contextPath }/banGongHuiWenTiController.do";

	//计算本页自适应高度
	function setPageHeight(){
  		var height = g_iHeight-pageTopHeight-pageTitle-getTableTh(1)-pageNumHeight-85;
  		var pageNum = parseInt(height/pageTableOne,10);
  		$("#DT1").attr("pageNum",pageNum);
  		$("#treeFrameDiv").attr("style","height:"+(g_iHeight-30)+"px");
	};
	$(function() {
		setPageHeight();
	});
	//点击树调查询
	function query(treeNode){
		var id = treeNode.id;
		$("#PARENT_ID").val(id);
		$("#hyrq").html(treeNode.hysj);
		$("#chbm").html(treeNode.chbm);
		if("0"==treeNode.hyzt){
			$("#hyzt").html("未结束");
		}else{
			$("#hyzt").html("已结束");
		}
		
		$("#selectrow").val(JSON.stringify(treeNode));
		var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		defaultJson.doQueryJsonList(controllername+"?querybanGongHuiList&sfgk=all",data,DT1,null,true);
		if('1'!=treeNode.hyzt){
			$("#hyjyqBtn").attr("disabled",true);
			$("#ytdyBtn").attr("disabled",false);
			$("#ytxzBtn").attr("disabled",false);
			/* $("#treeFrame").contents().find("#endBtn").show(); */
		}else{
			$("#hyjyqBtn").attr("disabled",false);
			$("#ytdyBtn").attr("disabled",true);
			$("#ytxzBtn").attr("disabled",true);
			/* $("#treeFrame").contents().find("#endBtn").hide(); */
		}
	}
	//修改办公会
	function editbgh(nodeLeve){
		$(window).manhuaDialog({"title":"维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghgl/bghedit.jsp","modal":"2"});	
	}
	function jinggao()
	{
		xAlert("警告","请选择节点","3");
	}
	
	function deleteAlert(gc_bgh_id) {
		xConfirm("提示信息","是否确认删除会议");
		$('#ConfirmYesButton').unbind();
		$('#ConfirmYesButton').one("click",function() {
			$.ajax({
				url : controllername+"?delete&gc_bgh_id="+gc_bgh_id,
				cache : false,
				async : false,
				dataType : 'json',
				success : function(response) {
					if(response == "1") {
						cleartreeFrame();
						xAlert("提示","操作成功","1");
					}
				}
			});
		});
	}
	//完成办公会
	function endBGH(nodeLeve){
		xConfirm("提示信息","是否确认完成?");
		$('#ConfirmYesButton').unbind();
		$('#ConfirmYesButton').one("click",function(){ 
		$("#HYZT").attr("code",1);
		$("#GC_BGH_ID").val($("#PARENT_ID").val());
		//生成json串
		var data = Form2Json.formToJSON(demoForm);
		//组成保存json串格式
		var data1 = defaultJson.packSaveJson(data);
		//调用ajax插入
		defaultJson.doInsertJson(controllername + "?updateBanGongHui", data1,null,'cleartreeFrame');
		});
	}
	var tempFlag = 1;
	$(function() {
		setPageHeight();
		//申请上会议题
		$("#sqshwtBtn").click(function() {
			tempFlag = 2;
			 $(window).manhuaDialog({"title":"申请上会议题","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghgl/sqshytgl.jsp","modal":"2"});
		});
		//议题维护
		$("#ytwhqBtn").click(function() {
			var index1 =$("#DT1").getSelectedRowIndex();
	 		if(index1<0){
	 			requireSelectedOneRow();
			}else{
			 $(window).manhuaDialog({"title":"议题维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghgl/bghwtedit.jsp","modal":"2"});
	 		}
	 		});
		//会议纪要
		$("#hyjyqBtn").click(function() {
			var treeNode=$("#PARENT_ID").val();
	 		if(''==treeNode){
	 			requireSelectedOneRow();
			}else{
			 $(window).manhuaDialog({"title":"会议纪要","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghgl/yhjy.jsp","modal":"1"});
	 		}
	 		});
		//议题打印
		$("#ytdyBtn").click(function() {
			var treeNode=$("#PARENT_ID").val();
	 		if(''==treeNode){
	 			requireSelectedOneRow();
			}else{
			 $(window).manhuaDialog({"title":"议题打印","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghgl/yhjy.jsp","modal":"1"});
	 		}
	 		});
		//新增议题
		$("#ytxzBtn").click(function() {
			var treeNode=$("#PARENT_ID").val();
	 		if(''==treeNode){
	 			requireSelectedOneRow();
			}else{
			 $(window).manhuaDialog({"title":"新增议题","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghwtgladd.jsp?bz=2","modal":"2"});
	 		}
 		});
 		//删除会议按钮
		$("#deleteBtn").click(function(){
			var nodeObj = $(window.document).contents().find("#treeFrame")[0].contentWindow.getNodeData(); 
			var nodeLevel = nodeObj["nodelevel"];
			var nodeID = nodeObj["id"];
			if(nodeLevel){
				deleteAlert(nodeID);
			}else{
				jinggao();
			}
		});
		//结束会议按钮
		$("#endBtn").click(function() {
			var nodeObj = $(window.document).contents().find("#treeFrame")[0].contentWindow.getNodeData(); 
			var nodeLevel = nodeObj["nodelevel"];
			var nodeID = nodeObj["id"];
			if(nodeLevel){
				endBGH(nodeLevel);
			}else{
				jinggao();
			}
		});
		//修改会议按钮
		$("#updateBtn").click(function() {
			var nodeObj = $(window.document).contents().find("#treeFrame")[0].contentWindow.getNodeData(); 
			var nodeLevel = nodeObj["nodelevel"];
			var nodeID = nodeObj["id"];
			if(nodeLevel){
				editbgh(nodeLevel);
			}else{
				jinggao();
			}
		});
	});
	//获取行数据json串
	function tr_click(obj,tabListid)
	{
	$("#resultXML").val(JSON.stringify(obj));
	}
	//子页面调用修改行
	function xiugaihang(data)
	{
		var index =	$("#DT1").getSelectedRowIndex();
		var subresultmsgobj = defaultJson.dealResultJson(data);
		var comprisesJson = $("#DT1").comprisesJson(subresultmsgobj,index);
		$("#DT1").updateResult(JSON.stringify(comprisesJson),DT1,index);
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(index);
		var resultmsgobj1 = convertJson.string2json1(data);
		var subresultmsgobj1 = resultmsgobj1.response.data[0];
		$("#resultXML").val(JSON.stringify(subresultmsgobj1));
		xSuccessMsg("操作成功","");
	}
	//子页面调用添加行
	function tianjiahang(data){
		var subresultmsgobj = defaultJson.dealResultJson(data);
		$("#DT1").insertResult(JSON.stringify(subresultmsgobj),DT1,1);
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(0);
		var resultmsgobj1 = convertJson.string2json1(data);
		var subresultmsgobj1 = resultmsgobj1.response.data[0];
		$("#resultXML").val(JSON.stringify(subresultmsgobj1));
	    xSuccessMsg("操作成功","");
	}
	function doRandering(obj){
		var showHtml = "";
		showHtml = "<a href='javascript:void(0)' title='信息卡' onclick='openBGH();'><i class='icon-file showXmxxkInfo' ></i></a>";
		return showHtml;
	}
	function openBGH(){
	    var index = $(event.target).closest("tr").index();
		$("#DT1").cancleSelected();
    	$("#DT1").setSelect(index);
		if($("#DT1").getSelectedRowIndex()==-1){
			requireSelectedOneRow();
			return;
		}else{
			$(window).manhuaDialog({"title":"会议中心","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghgl/shytd.jsp","modal":"1"});
		}
	}
	//弹出窗口关闭--弹出页面回调空函数
    function closeParent(){
		if(tempFlag==2){
			tempFlag = 1;
			cleartreeFrame();
		}else{
			
		}
	}
	//完成修改回调
	function edithuidiao(){
		cleartreeFrame();
		xAlert("提示信息","操作成功","1");
	}
	function cleartreeFrame(){
		$("#PARENT_ID").val("");
		$("#hyrq").html("");
		$("#hyzt").html("");
		$("#chbm").html("");
		$("#DT1 tbody").children().remove();
		$("#treeFrame").attr("src",$("#treeFrame").attr("src"));
		$("#hyjyqBtn").attr("disabled",false);
		$("#ytdyBtn").attr("disabled",false);
	}
	

	function deleteBghyt() {
		if($("#DT1").getSelectedRowIndex()==-1) {
			xAlert("提示信息",'请选择一条记录','3');
			return
	    }
		
		var line = $("#DT1").getSelectedRowJsonObj();
		$("#deleteBghFrom").setFormValues(line);
		var data3 = Form2Json.formToJSON(deleteBghFrom);
		var data4 = defaultJson.packSaveJson(data3);
 		xConfirm("提示信息","是否确认删除！");
		$('#ConfirmYesButton').unbind();
		$('#ConfirmYesButton').one("click",function(){
			defaultJson.doDeleteJson(controllername_del+"?deleteGongHuiWenTi",data4,null,"delhang");
		});
	}
	
	//删除回调
	function delhang()
	{
		  var rowindex = $("#DT1").getSelectedRowIndex();
		  $("#DT1").removeResult(rowindex);
	}
</script>

		<div align="center">
			<FORM name="deleteBghFrom" id="deleteBghFrom" method="post"
				style="display: none" target="_blank">
				<input type="text" kind="text" fieldname="GC_BGH_WT_ID"
					name="GC_BGH_WT_ID" id="GC_BGH_WT_ID">
			</FORM>
		</div>
	</body>
</html>