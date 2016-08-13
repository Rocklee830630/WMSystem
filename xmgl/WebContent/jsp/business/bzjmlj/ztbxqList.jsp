<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
		<%	String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
			String tiaojian = request.getParameter("tiaojian");
			String lie = request.getParameter("lie");
		%>
<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/bzjk/bzjkCommonController.do";
	g_bAlertWhenNoResult = false;
	var g_nd = '<%=nd%>';
	var g_proKey = '<%=proKey%>';
	var g_tiaojian = '<%=tiaojian%>';
	var g_lie = '<%=lie%>';
	//计算本页表格分页数
	function setPageHeight(){
		var height = getDivStyleHeight()-pageTopHeight-pageTitle-getTableTh(1)-pageQuery-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
	//页面初始化
	$(function() {
		setPageHeight();
		doInit();
		//按钮绑定事件（导出EXCEL）
		$("#btnExp").click(function() {
			  if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
			      printTabList("DT1");
			  }
		});
	   var btn_clearQuery = $("#query_clear");
		btn_clearQuery.click(function() 
		{
	        $("#queryForm").clearFormResult();
	      });
		//按钮绑定事件（查询）
		$("#chaxun").click(function() {
			doSearch();
		});
	});
	//页面默认参数
	function doInit(){
		if(g_tiaojian=="ZBHT"){
			controllername= "${pageContext.request.contextPath }/bmgk/GcHtBmgkController.do";
		}
		doSearch();
	}
	function doSearch(){
		var condProKey = "";
		if(g_proKey!=null&&g_proKey!=""){
			condProKey = "&proKey="+g_proKey;
		}
		var condNd = "";
		if(g_nd!=null&&g_nd!=""){
			condNd = "&nd="+g_nd;
		}
		var condTiaojian = "";
		if(g_tiaojian!=null&&g_tiaojian!=""){
			condTiaojian = "&tiaojian="+g_tiaojian;
		}
		var condLie = "";
		if(g_lie!=null&&g_lie!=""){
			condLie = "&lie="+g_lie;
		}
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		defaultJson.doQueryJsonList(controllername+"?queryZBXQList"+condTiaojian+condNd+condProKey+condLie,data,DT1,null,false);
	}
	function doRanderingYuan(obj){
		var showHtml = "";
		if(obj.TBJFS=="1" || obj.TBJFS=="2"){
			showHtml = obj.YSE_SV;
		}else{
			showHtml = '<div style="text-align:center">—</div>';
		}
		return showHtml;
	}
	//状态颜色判断
	function docolor(obj)
	{
		var xqzt=obj.XQZT;
		if(xqzt=="1") 
		{
			return '<span class="label label-danger">'+obj.XQZT_SV+'</span>';
		}
		if(xqzt=="3")
		{
		  	return '<span class="label label-warning">'+obj.XQZT_SV+'</span>';
		}
		if(xqzt=="6")
		{
		 	return '<span class="label label-success">'+obj.XQZT_SV+'</span>';
		}
		else
			{
			return obj.XQZT_SV;
			}
		
	}
	//信息卡
	function doRandering(obj)
	  {
			var showHtml = "";
			showHtml = "<a href='javascript:void(0)' onclick='xinxika()' title='招标需求信息卡'><i class='icon-file showXmxxkInfo'></i></a>";
			return showHtml;
	  }
	function xinxika(){
		var index = $(event.target).closest("tr").index();
	    $("#DT1").cancleSelected();
	    $("#DT1").setSelect(index);
		json = $("#DT1").getSelectedRowText();
		//json串加密
		json=encodeURI(json);
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"招标需求信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/ztbxq/ztbxqxxk.jsp","modal":"1"});
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
						部门招标需求
						<span class="pull-right">
										
							<button id="btnExp" class="btn" type="button">
								导出
							</button> </span>
					</h4>
					<form method="post" id="demoForm2">
						<table class="B-table" id="DT" width="100%">
							<tr style="display: none;">
								<td>
									<input type="text" id="GC_ZTB_XQ_ID" name="GC_ZTB_XQ_ID"
										fieldname="GC_ZTB_XQ_ID" />
								</td>
								<td>
									<input type="text" id="XQZT" name="XQZT" fieldname="XQZT" />
								</td>
							</tr>
						</table>
					</form>
					<form method="post"
						action="${pageContext.request.contextPath }/insertdemo.do"
						id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<INPUT id="num" type="text" class="span12" keep="true"
										kind="text" fieldname="rownum" value="1000" operation="<=" />
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
							<tr>
								<th width="5%" class="bottom-border text-right">
									项目名称
								</th>
								<td width="15%" class=" bottom-border" colspan="2">
									<input class="span12" type="text" placeholder="" name="XMMC"
										fieldname="T.XMMC" operation="like" logic="and">
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									工作名称
								</th>
								<td width="15%" class=" right-border bottom-border">
									<input class="span12" type="text" placeholder="" name="GZMC"
										fieldname="GZMC" operation="like" logic="and">
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									招标类型
								</th>
								<td width="10%" class=" right-border bottom-border">
									<select class="span12 4characters" id="ZBLX" name="ZBLX"
										fieldname="ZBLX" defaultMemo="全部" kind="dic" src="ZBLX"
										operation="=">
									</select>
								</td>
								<td class="text-left bottom-border text-right" rowspan="2">
									<button id="chaxun" class="btn btn-link" type="button">
										<i class="icon-search"></i>查询
									</button>
									<button id="query_clear" class="btn btn-link" type="button">
										<i class="icon-trash"></i>清空
									</button>
								</td>
							</tr>
							
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
					<table width="100%" class="table-hover table-activeTd B-table"
						id="DT1" type="single" printFileName="部门招标需求">
						<thead>
							<tr>
								<th name="XH" id="_XH">
									&nbsp;#&nbsp;
								</th>
								<th fieldname="SFYX" maxlength="15" tdalign="center" CustomFunction="doRandering" noprint="true">
									&nbsp;&nbsp;
								</th>
								<th fieldname="XQZT" tdalign="center"  CustomFunction="docolor">
									&nbsp;需求状态&nbsp;
								</th>
								<th fieldname="EVENTSJBH" maxlength="15">
									&nbsp;审批状态&nbsp;
								</th>
								<th fieldname="GZMC" maxlength="15">
									&nbsp;工作名称&nbsp;
								</th>
								<th fieldname="LRBM" maxlength="15">
									&nbsp;提出部门&nbsp;
								</th>
								<th fieldname="ZBLX" maxlength="15" tdalign="center">
									&nbsp;招标类型&nbsp;
								</th>
								<th fieldname="TBJFS" maxlength="15" tdalign="center">
									&nbsp;投标报价方式&nbsp;
								</th>
								<th fieldname="YSE" maxlength="15" tdalign="right"
									CustomFunction="doRanderingYuan">
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