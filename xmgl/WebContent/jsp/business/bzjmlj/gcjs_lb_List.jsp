<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<%	
	String nd = request.getParameter("nd");
	String proKey = request.getParameter("proKey");
%>
<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/bmgk/ZjBmgkController.do";
	var controllername1= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
	g_bAlertWhenNoResult = false;
	var g_nd = '<%=nd%>';
	var g_proKey = '<%=proKey%>';
	//计算本页表格分页数
	function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
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
		$("#query_clear").click(function(){
			$("#queryForm").clearFormResult();
		});
		//按钮绑定事件（查询）
		$("#chaxun").click(function() {
			doSearch();
		});
		//自动完成项目名称模糊查询
		showAutoComplete("QXMMC",controllername1+"?xmmcAutoCompleteToXmxdk","getXmmcQueryCondition"); 
	});
	//项目名称自动模糊查询参数
	function getXmmcQueryCondition(){
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		return data;
	}
	//页面默认参数
	function doInit(){
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
		
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		defaultJson.doQueryJsonList(controllername+"?queryJslbljList"+condNd+condProKey,data,DT1,null,false);
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
							</button> </span>
					</h4>
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
									<input class="span12" type="text" placeholder="" name="QXMMC"
										fieldname="T.XMMC" operation="like" id="QXMMC" autocomplete="off"
										tablePrefix="T"/>
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									标段名称
								</th>
								<td width="15%" class=" right-border bottom-border">
									<input class="span12" type="text" placeholder="" name="BDMC"
										fieldname="BDMC" operation="like" logic="and">
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									项目性质
								</th>
								<td width="10%" class=" right-border bottom-border">
									<select class="span12 4characters" id="XMXZ" name="XMXZ"
										fieldname="XMXZ" defaultMemo="全部" kind="dic" src="XMXZ"
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
					<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" printFileName="项目详细列表">
						<thead>
							<tr>
								<th name="XH" id="_XH" rowspan=2 colindex=1>
									&nbsp;#&nbsp;
								</th>
								<th fieldname="XMMC" maxlength="15" rowspan=2 colindex=2 rowMerge="true">
									&nbsp;项目名称&nbsp;
								</th>
								<th fieldname="BDMC" maxlength="15" rowspan=2 colindex=3>
									&nbsp;标段名称&nbsp;
								</th>
								<th fieldname="XMDZ" maxlength="15" rowspan=2 colindex=4>
									&nbsp;项目地址&nbsp;
								</th>
								<th fieldname="XMXZ" tdalign="center" rowspan=2 colindex=5>
									&nbsp;项目性质&nbsp;
								</th>
								<th fieldname="ISBT" maxlength="15" tdalign="center" rowspan=2 colindex=6>
									&nbsp;是否BT&nbsp;
								</th>
								<th fieldname="XMGLGS" maxlength="15" rowspan=2 colindex=7>
									&nbsp;项管公司&nbsp;
								</th>
								<th fieldname="SGDWID" maxlength="15" rowspan=2 colindex=8>
									&nbsp;施工单位&nbsp;
								</th>
								<th fieldname="HTQDJ" tdalign="right" rowspan=2 colindex=9>
									&nbsp;合同签订价&nbsp;
								</th>
								<th fieldname="WTZXGS" maxlength="15" rowspan=2 colindex=10>
									&nbsp;咨询公司&nbsp;
								</th>
								<th colspan=2>
									&nbsp;上报值&nbsp;
								</th>
								<th colspan=2>
									&nbsp;业主审定值&nbsp;
								</th>
								<th colspan=2>
									&nbsp;财审审定值&nbsp;
								</th>
								<th colspan=2>
									&nbsp;审计审定值&nbsp;
								</th>
							</tr>
							<tr>
								<th fieldname="TBJE" tdalign="right" colindex=11>金额</th>
								<th fieldname="TBRQ" tdalign="right" colindex=12>日期</th>
								<th fieldname="YZSDJE" tdalign="right" colindex=13>金额</th>
								<th fieldname="YZSDRQ" tdalign="right" colindex=14>日期</th>
								<th fieldname="CSSDJE" tdalign="right" colindex=15>金额</th>
								<th fieldname="CSSDRQ" tdalign="right" colindex=16>日期</th>
								<th fieldname="SJSDJE" tdalign="right" colindex=17>金额</th>
								<th fieldname="SJSDRQ" tdalign="right" colindex=18>日期</th>
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