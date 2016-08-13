<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<app:base/>
<%	String nd = request.getParameter("nd");
	String proKey = request.getParameter("proKey");
%>
<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/ztbBmjkController.do";
	var json,XQID,XQZT;
	var g_nd = '<%=nd%>';
	var g_proKey = '<%=proKey%>';
   
	//计算本页表格分页数
	function setPageHeight(){
		var getHeight=getDivStyleHeight();
		var height = getHeight-pageTopHeight-pageTitle-getTableTh(1)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
   
	$(function() {
		setPageHeight();
		init();
	});
	//获取行数据json串
	function tr_click(obj,tabId){
		var rowValue = $("#"+tabId).getSelectedRow();//获得选中行的json 字符串
		var tempJson = convertJson.string2json1(rowValue);//字符串转JSON对象
		if(tempJson.XQZT!="3"){
			$("#xiugai").attr("disabled","true");
		}else{
			$("#xiugai").removeAttr("disabled");
		}
	}
	//页面默认参数
	function init(){
		//生成json串
		doSearch();
	}
	function doSearch(){
		var str = "&xqzt='3','5','6'";
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		var condProKey = "";
		if(g_proKey!=null&&g_proKey!=""){
			condProKey = "&proKey="+g_proKey;
		}
		var condNd = "";
		if(g_nd!=null&&g_nd!=""){
			condNd = "&nd="+g_nd;
		}
		defaultJson.doQueryJsonList(controllername+"?queryDrillingDataZbxqzh"+condProKey+condNd,data,DT1,null,true);
	}
	//子页面调用修改行
	function xiugaihang(data,n){
		var str = "&xqzt='3','5','6'";
		var data1 = defaultJson.packSaveJson(data);
		defaultJson.doUpdateJson(controllername+"?doQdzb&ztbxqid="+n+str, data1,DT1);
		$("#xiugai").attr("disabled","true");
	}
	//信息卡
	function doRandering(obj){
			var showHtml = "";
			showHtml = "<a href='javascript:void(0)' onclick='xinxika()' title='招标需求信息卡'><i class='icon-file showXmxxkInfo' jhsjid='"+obj.GC_JH_SJ_ID+"'></i></a>";
			return showHtml;
	}
	function xinxika(){
		var index = $(event.target).closest("tr").index();
	    $("#DT1").cancleSelected();
	    $("#DT1").setSelect(index);
		json = $("#DT1").getSelectedRowText();
		//json串加密
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"招标需求信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/ztbxq/ztbxqxxk.jsp","modal":"1"});
	}
	function docolor(obj){
		var xqzt=obj.XQZT;
		if(xqzt=="1"){
			return '<span class="label label-danger">'+obj.XQZT_SV+'</span>';
		}else if(xqzt=="3"){
		  	return '<span class="label label-warning">'+obj.XQZT_SV+'</span>';
		}else if(xqzt=="6"){
		 	return '<span class="label label-success">'+obj.XQZT_SV+'</span>';
		}else{
			return obj.XQZT_SV;
		}
	}
	//控制投资额显示
	function showTze(obj){
		var showHtml = "";
		if(obj.TBJFS=="1" || obj.TBJFS=="2"){
			showHtml = obj.YSE_SV;
		}else{
			showHtml = '<div style="text-align:center">—</div>';
		}
		return showHtml;
	}
	//子页面调用添加行
	function tianjiahang(data){
		var subresultmsgobj = defaultJson.dealResultJson(data);
		$("#DT1").insertResult(JSON.stringify(subresultmsgobj),DT1,1);
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(0);
		json = $("#DT1").getSelectedRowText();
		//json串加密
	    json=encodeURI(json);
	    xSuccessMsg("操作成功","");
	}
</script>     
</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<p></p>
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						<span class="pull-right">
							<button id="btnExp" class="btn" type="button">
								导出
							</button></span>
					</h4>
					<form method="post"
						action="${pageContext.request.contextPath }/insertdemo.do"
						id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
					<table width="100%" class="table-hover table-activeTd B-table"
						id="DT1" type="single">
						<thead>
							<tr>
								<th name="XH" id="_XH">
									&nbsp;#&nbsp;
								</th>
								<th fieldname="SFYX" maxlength="15" tdalign="center" CustomFunction="doRandering" noprint="true">
									&nbsp;&nbsp;
								</th>
								<th fieldname="XQZT" maxlength="15" tdalign="center" CustomFunction="docolor">
									&nbsp;状态&nbsp;
								</th>
								<th fieldname="GZMC" maxlength="15">
									&nbsp;工作名称&nbsp;
								</th>
								<th fieldname="ZBLX" maxlength="15" tdalign="center">
									&nbsp;招标类型&nbsp;
								</th>
								<th fieldname="ZBFS" maxlength="15" tdalign="center">
									&nbsp;招标方式&nbsp;
								</th>
								<th fieldname="TBJFS" maxlength="15" tdalign="center">
									&nbsp;投标报价方式&nbsp;
								</th>
								<th fieldname="YSE" maxlength="15" tdalign="right" CustomFunction="showTze">
									&nbsp;投资额(元)&nbsp;
								</th>
								<th fieldname="GZNR" maxlength="15">
									&nbsp;工作内容&nbsp;
								</th>
								<th fieldname="SXYQ" maxlength="15">
									&nbsp;时限要求&nbsp;
								</th>
								<th fieldname="JSYQ" maxlength="15">
									&nbsp;技术要求&nbsp;
								</th>
								<!-- 	<th fieldname="XQZT"  maxlength="15" tdalign="center">&nbsp;是否有效&nbsp;</th> -->
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
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
				<input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ" />
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
				<input type="hidden" name="queryResult" id="queryResult">

			</FORM>
		</div>
	</body>
</html>