<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<%
	String jhsjid=request.getParameter("jhsjid");
%>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/zsb/xmb/xmbController.do";
  var countcontroller= "${pageContext.request.contextPath }/pqgl/pqglController.do";
  var jhsjid="<%=jhsjid%>";
  //获取父页面行选值方法
  function do_value(){
		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.$("#DT1").getSelectedRow();
		var obj = convertJson.string2json1(rowValue);
		return obj;
	}
  var id,xmkid,xmmc;
	$(function() {
		ready();		//页面初始查询
			});
	//初始查询方法
    function ready() {
		$("#JHSJ").val(jhsjid);
		setPageHeight();
      	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
   		//调用ajax插入
   		defaultJson.doQueryJsonList(controllername+"?queryJdxx",data,DT1,null,true);
    };
  //计算本页表格分页数
    function setPageHeight(){
    	var getHeight=getDivStyleHeight();
    	var height = getHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
    	var pageNum = parseInt(height/pageTableOne,10);
    	$("#DT1").attr("pageNum",pageNum);
    }
</script>
</head>
	<body>
	<app:dialogs />
		<div class="container-fluid">
		<p></p>
		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
		<h4 class="title">进度信息</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<!-- <TR> -->
						<TD><INPUT type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<=" keep="true"></TD>
						<TD><input type="text" class="span12" kind="text" id="JHSJ" fieldname="xxb.JHSJID" name="JHSJID" operation="=" keep="true"></TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
				</table>
			</form>
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" pageNum="7">
							<thead>
								<tr>
									<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
									<th fieldname="QY" rowspan="2" colindex=2>&nbsp;区域&nbsp;</th>
									<th fieldname="ZRDW" rowspan="2" colindex=3>&nbsp;责任单位&nbsp;</th>
									<th colspan="3">&nbsp;摸底信息&nbsp;</th>
									<th colspan="3">&nbsp;拆迁进度（累计）&nbsp;</th>
									<th fieldname="CDYJRQ" rowspan="2" colindex=10 tdalign="center">场地移交<p></p>日期&nbsp;</th>
									<th fieldname="TDFWYJRQ" rowspan="2" colindex=11 tdalign="center">土地房屋移交<p></p>日期&nbsp;</th>
									<th fieldname="WTJFX" maxlength="15" rowspan="2" colindex=12 style="width:15%">&nbsp;问题及风险&nbsp;</th>
								</tr>
								<tr>
									<th fieldname="JMHS" tdalign="right" colindex=4>&nbsp;居民户数&nbsp;</th>
									<th fieldname="QYJS" tdalign="right" colindex=5>&nbsp;企业家数&nbsp;</th>
									<th fieldname="ZMJ" tdalign="right" colindex=6>&nbsp;总面积&nbsp;</th>
									<th fieldname="LJJMZL" tdalign="right" colindex=7>&nbsp;居民户数&nbsp;</th>
									<th fieldname="LJQYZL" tdalign="right" colindex=8>&nbsp;企业家数&nbsp;</th>
									<th fieldname="LJZDMJ" tdalign="right" colindex=9>&nbsp;总面积&nbsp;</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<jsp:include page="/jsp/file_upload/fileupload_config.jsp"
			flush="true" />
		<div align="center">
			<FORM name="frmPost" id="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="txtFilter" order="desc" fieldname="xxb.LRSJ" id="txtFilter">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">

			</FORM>
		</div>
	</body>
</html>