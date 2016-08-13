<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<title></title>
<% 
	String lujing = request.getParameter("lujing");
	String mingchen = request.getParameter("mingchen");
	String tiaojian = request.getParameter("tiaojian");
	String nd = request.getParameter("nd");
%>
<script type="text/javascript" charset="utf-8">
  var json2,json;
  var rowindex,rowValue,text,aa;
  var jhsjid,lbjid,bdmc;
  var ztbsjid = '';
  var controllername= "${pageContext.request.contextPath }/zjbmjkController.do";
  var lujing = "<%=lujing %>";
  var mingchen = "<%=mingchen%>";
  var nd = "<%=nd%>";
  var tiaojian = "<%=tiaojian%>";
	//计算本页表格分页数
	function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
  
	$(function() {
		setPageHeight();
		ready();
		//按钮绑定事件（导出）
		$("#btnExp").click(function() {
			if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
				printTabList("DT1");
			}
		});
	});
    function ready() {
   	 	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryLbj&lujing="+lujing+"&mingchen="+mingchen+"&nd="+nd+"&tiaojian="+tiaojian,data,DT1,null,true);
   };
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
	//列表添加信息卡图标
	function doRandering(obj){
		var showHtml = "";
		showHtml = "<a href='javascript:void(0)' onclick='openInfoCard()' title='招投标信息卡'><i class='icon-file showXmxxkInfo'></i></a>";
		return showHtml;
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
						招投标管理
						<span class="pull-right">
							<button id="btnExp" class="btn" type="button">
								导出
							</button> </span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD>
									<INPUT id="num" type="text" class="span12" kind="text"
										fieldname="rownum" value="1000" operation="<=" keep="true" />
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
					<table width="100%" class="table-hover table-activeTd B-table"
						id="DT1" type="single" editable="0">
						<thead>
							<tr>
								<th name="XH" id="_XH" tdalign="">
									&nbsp;#&nbsp;
								</th>
								<th fieldname="SFYX" maxlength="15" tdalign="center" CustomFunction="doRandering" noprint="true">
									&nbsp;&nbsp;
								</th>
								<th fieldname="XQZT" tdalign="center">
									&nbsp;状态&nbsp;
								</th>
								<th fieldname="ZTBMC" tdalign="" maxlength="15">
									&nbsp;招投标名称&nbsp;
								</th>
								<th fieldname="ZBBH" tdalign="" maxlength="15">
									&nbsp;招标编号&nbsp;
								</th>
								<th fieldname="ZBFS" tdalign="center" maxlength="15">
									&nbsp;招标方式&nbsp;
								</th>
								<th fieldname="KBRQ" tdalign="center" maxlength="15">
									&nbsp;开标日期&nbsp;
								</th>
								<th fieldname="ZZBJ" tdalign="right" maxlength="15">
									&nbsp;总中标价(元)
								</th>
								<th fieldname="BJXS" tdalign="right" maxlength="15">
									&nbsp;报价系数
								</th>
								<th fieldname="DZFS" tdalign="" maxlength="15">
									&nbsp;付款方式&nbsp;
								</th>
								<th fieldname="ZBXMFZR" tdalign="center" maxlength="15">
									&nbsp;项目负责人
								</th>
								<th fieldname="DSFJGID" tdalign="left" maxlength="15">
									&nbsp;中标单位&nbsp;
								</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					</div>
					<form method="post" id="ztbForm" style="display: none">
						<table class="B-table" width="100%">
							<TR style="display: none;">
								<!-- <TR> -->
								<TD>
									<INPUT class="span12" type="text" name="GC_ZTB_SJ_ID"
										fieldname="GC_ZTB_SJ_ID" id="GC_ZTB_SJ_ID" />
									<INPUT class="span12" type="text" id="XQZT" name="XQZT"
										fieldname="XQZT" value="2" keep="true" />
								</TD>
							</TR>
						</table>
					</form>
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
				<input type="hidden" name="queryResult" id="queryResult" />
				<input type="hidden" name="txtFilter" order="desc"
					fieldname="a.LRSJ" id="txtFilter">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
			
		</div>
	</body>
</html>