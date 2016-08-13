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
	String tiaojian = request.getParameter("tiaojian");
	String lie = request.getParameter("lie");
%>
<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/bmgk/GcHtBmgkController.do";
	var controllername1= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
	g_bAlertWhenNoResult = false;
	var g_nd = '<%=nd%>';
	var g_proKey = '<%=proKey%>';
	var g_tiaojian = '<%=tiaojian%>';
	var g_lie = '<%=lie%>';
	//计算本页表格分页数
	function setPageHeight(){
		var height = getDivStyleHeight()-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
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
		var condTiaojian = "";
		if(g_tiaojian!=null&&g_tiaojian!=""){
			condTiaojian = "&tiaojian="+g_tiaojian;
		}
		var condLie = "";
		if(g_lie!=null&&g_lie!=""){
			condLie = "&lie="+g_lie;
		}
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		defaultJson.doQueryJsonList(controllername+"?queryHtsjList"+condTiaojian+condNd+condProKey+condLie,data,DT1,null,false);
	}
	//列表添加信息卡图标
	function doRandering(obj){
		var showHtml = "";
		if(obj.GC_ZTB_SJ_ID!=""){
			showHtml = "<a href='javascript:void(0)' onclick='openInfoCard()' title='招投标信息卡'><i class='icon-file showXmxxkInfo'></i></a>";
		}
		return showHtml;
	}
	//-------------------------------
	//-打开信息卡页面
	//-------------------------------
	function openInfoCard(){
		var index = $(event.target).closest("tr").index();
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(index);
		var json = convertJson.string2json1($("#DT1").getSelectedRow());
		var id = json.GC_ZTB_SJ_ID;
		var zblx = json.ZBLX;
		$(window).manhuaDialog({"title":"招投标管理>查看招标信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/ztbgl_xx.jsp?xx="+id+"&zblx="+zblx+"&cxlx=2","modal":"1"});
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
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
							<tr>
								<th width="5%" class="right-border bottom-border text-right">
									招投标名称
								</th>
								<td width="15%" class=" right-border bottom-border">
									<input class="span12" type="text" placeholder="" name="ZTBMC"
										fieldname="ZTBMC" operation="like" logic="and">
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									招标类型
								</th>
								<td width="8%" class=" right-border bottom-border">
									<select class="span12 4characters" name="ZBLX" kind="dic"
										fieldname="ZBLX" src="ZBLX" operation="=" logic="and"
										 defaultMemo="全部"></select>
								</td>
								<th width="5%" class="bottom-border text-right">
									合同编号
								</th>
								<td width="15%" class=" bottom-border" colspan="2">
									<input class="span12" type="text" placeholder="" name="HTBM"
										fieldname="HTBM" operation="like" id="HTBM">
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									合同名称
								</th>
								<td width="15%" class=" right-border bottom-border">
									<input class="span12" type="text" placeholder="" name="HTMC"
										fieldname="HTMC" operation="like" logic="and">
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
						id="DT1" type="single"  printFileName="合同信息">
						<thead>
							<tr>
								<th name="XH" id="_XH" rowspan=2 colindex=1>
									&nbsp;#&nbsp;
								</th>
								<th fieldname="ZTBMC" CustomFunction="doRandering" noprint="true">
									&nbsp;&nbsp;
								</th>
								<th fieldname="ZTBMC" maxlength="15">
									&nbsp;招投标名称&nbsp;
								</th>
								<th fieldname="ZBLX">
									&nbsp;招标类型&nbsp;
								</th>
								<th fieldname="ZBFS" tdalign="center">
									&nbsp;招标方式&nbsp;
								</th>
								<th fieldname="KBRQ" maxlength="15" tdalign="center">
									&nbsp;开标日期&nbsp;
								</th>
								<th fieldname="ZBTZSBH">
									&nbsp;中标通知书编号&nbsp;
								</th>
								<th fieldname="ZZBJ" tdalign="right">
									&nbsp;中标价&nbsp;
								</th>
								<th fieldname="DSFJGID" maxlength="15" tdalign="left">
									&nbsp;中标单位&nbsp;
								</th>
								<th fieldname="HTZT" maxlength="15">
									&nbsp;合同状态&nbsp;
								</th>
								<th fieldname="HTBM" maxlength="15">
									&nbsp;合同编码&nbsp;
								</th>
								<th fieldname="HTMC" maxlength="15">
									&nbsp;合同名称&nbsp;
								</th>
								<th fieldname="HTLX" maxlength="15">
									&nbsp;合同类型&nbsp;
								</th>
								<th fieldname="ZHTQDJ" tdalign="right">
									&nbsp;合同价&nbsp;
								</th>
								<th fieldname="HTJQDRQ" maxlength="15">
									&nbsp;签订日期&nbsp;
								</th>
								<th fieldname="YFID" maxlength="15">
									&nbsp;乙方单位&nbsp;
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