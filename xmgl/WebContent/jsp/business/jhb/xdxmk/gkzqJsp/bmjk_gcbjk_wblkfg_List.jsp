<!DOCTYPE HTML>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>统筹计划-项目查询</title>
<script type="text/javascript" charset="UTF-8">
<%
	String nd = request.getParameter("nd");
%>
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/sjzqController.do";
var nd = "<%=nd%>";

$(function() {
	setPageHeight();
	init();
});
//页面默认参数
function init() {
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}

//计算本页表格分页数
function setPageHeight() {
	var xHeight = parent.document.documentElement.clientHeight;
	var height = xHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(Math.abs(height)/pageTableOne,10);
//	alert("xHeight:"+xHeight+"|pageTopHeight:"+pageTopHeight+"|pageTitle:"+pageTitle+"|pageQuery:"+pageQuery+"|pageNumHeight:"+pageNumHeight+"|getTableTh(1):"+getTableTh(1));
//	alert(Math.abs(height) +"|" + pageTableOne + " | " + pageNum);
	$("#DT1").attr("pageNum", pageNum);
}

//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query_kfgl&nd="+nd,data,DT1);
}

//详细信息
function rowView(index) {
	var obj = $("#DT1").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).XMID;
	$(window).manhuaDialog(xmscUrl(id));
}

//按钮绑定事件（导出EXCEL）
$(function() {
	$("#btnExp").click(function() {
		 if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
		      printTabList("DT1");
		  }
	});
});
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">
      <span class="pull-right">
      	<button id="btnExp" class="btn"  type="button">导出</button>
      </span>
      </h4>
     <form method="post" id="queryForm">
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
		 		<input type="text" id="ND" fieldname="ND" name="ND" operation="="> 
			</TD>
        </TR>
      </table>
      </form>
 	<div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="DT1"  width="100%" editable="0" type="single">
		<thead>
			<tr>
				<th name="XH" id="_XH" colindex=1>&nbsp;#&nbsp;</th>
				<th fieldname="XMBH" colindex=2 tdalign="center" hasLink="true" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" colindex=3 maxlength="15" rowMerge="true">&nbsp;项目名称&nbsp;</th>
		<!-- 		<th fieldname="BDMC" colindex=4 maxlength="15" >&nbsp;标段名称&nbsp;</th> -->
			 	<th fieldname="XMGLGS" colindex=4 maxlength="10">&nbsp;项目管理公司&nbsp;</th>
				<th fieldname="XMXZ" colindex=5 tdalign="center">&nbsp;项目性质&nbsp;</th>
			 	<th fieldname="XMDZ" colindex=6>&nbsp;建设位置&nbsp;</th>
				<th fieldname="JSMB" colindex=7 maxlength="15">&nbsp;年度目标&nbsp;</th>
		<!-- 		<th colspan="2">&nbsp;工期安排（工程部及项目管理公司）&nbsp;</th>
				<th colspan="4">&nbsp;手续办理情况&nbsp;</th>
				<th colspan="4">&nbsp;设计情况&nbsp;</th>
				<th colspan="2">&nbsp;造价&nbsp;</th>
				<th colspan="2">&nbsp;招标&nbsp;</th>
				<th fieldname="ZC" rowspan="2" colindex=24 tdalign="center">&nbsp;征拆&nbsp;</th>
				<th fieldname="PQ" rowspan="2" colindex=25 tdalign="center">&nbsp;排迁&nbsp;</th>
				<th fieldname="JG" rowspan="2" colindex=26 tdalign="center">&nbsp;交工&nbsp;</th>
				<th fieldname="BZ" rowspan="2" colindex=27 maxlength="10">&nbsp;备注&nbsp;</th>  -->
			</tr>
		<!-- 	<tr>
				<th fieldname="KGSJ" colindex=10 tdalign="center">&nbsp;开工时间&nbsp;</th>
				<th fieldname="WGSJ" colindex=11 tdalign="center">&nbsp;完工时间&nbsp;</th>

				<th fieldname="KYPF" colindex=12 tdalign="center">&nbsp;可研批复&nbsp;</th>
				<th fieldname="HPJDS" colindex=13 tdalign="center">&nbsp;划拔决定书&nbsp;</th>
				<th fieldname="GCXKZ" colindex=14 tdalign="center">&nbsp;工程规划许可证&nbsp;</th>
				<th fieldname="SGXK" colindex=15 tdalign="center">&nbsp;施工许可&nbsp;</th>
				<th fieldname="CBSJPF" colindex=16 tdalign="center">&nbsp;初步设计批复&nbsp;</th>
				<th fieldname="CQT" colindex=17 tdalign="center">&nbsp;拆迁图&nbsp;</th>
				<th fieldname="PQT" colindex=18 tdalign="center">&nbsp;排迁图&nbsp;</th>
				<th fieldname="SGT" colindex=19 tdalign="center">&nbsp;施工图&nbsp;</th>
				<th fieldname="TBJ" colindex=20 tdalign="center">&nbsp;提报价&nbsp;</th>
				<th fieldname="CS" colindex=21 tdalign="center">&nbsp;财审&nbsp;</th>
				<th fieldname="JLDW" colindex=22 tdalign="center">&nbsp;监理单位&nbsp;</th>
				<th fieldname="SGDW" colindex=23 tdalign="center">&nbsp;施工单位&nbsp;</th>
			</tr> -->
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	</div>
        </div>
  </div>
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id="frmPost">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="" fieldname = ""	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>