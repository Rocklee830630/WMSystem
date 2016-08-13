<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>工程计量管理-查看</title>
<%
	String id = request.getParameter("id");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/gcjl/gcjlController.do";
var id = "<%= id%>";
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
    });
  	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 if(exportRequireQuery($("#gcjlList"))){//该方法需传入表格的jquery对象
		      printTabList("gcjlList","gcjlgl_view.xls","JLYF,XMMC,BDMC,HTID,HTJ,DYJLSDZ,DYZBJ,DYYFK,LJJLSDZ,LJZBJ,LJDYYFK,JZJLZ,LJJLZ,WGBFB,BZ","3,1");
		  }
	});
	
});
//页面默认参数
function init(){
	$("#id").val(id);
	getNd();
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}


//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcjlList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryByTcjhId",data,gcjlList);
}
//默认年度
function getNd(){
	setDefaultOption($("#qnd"),new Date().getFullYear());
}

//标段编号图标样式
function doBdbh(obj){
	if(obj.BDID == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDBH;
	}
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
    <h4 class="title">计量信息
    	<span class="pull-right">
    		<button id="btnExpExcel" class="btn"  type="button">导出</button>
    	</span>
    </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<input type="text" id="id" name="TCJH_SJ_ID" fieldname="TCJH_SJ_ID" keep="true" operation="="/>
			</TD>
        </TR>
         
      </table>
      </form>
    <div style="height:5px;"></div>
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="gcjlList" width="100%" noPage="true" type="single" pageNum="1000">
		<thead>
			<tr>
				<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
				<th fieldname="JLYF" rowspan="2" colindex=2 tdalign="center">&nbsp;计量月份&nbsp;</th>
				<th fieldname="XMMC" rowspan="2" colindex=3 maxlength="15">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDID" rowspan="2" colindex=4 CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" rowspan="2" colindex=5 maxlength="15">&nbsp;标段名称&nbsp;</th>
				<th fieldname="HTBM" rowspan="2" colindex=6 tdalign="center">&nbsp;合同编号&nbsp;</th>
				<th fieldname="HTQDJ" rowspan="2" colindex=7 tdalign="right">&nbsp;合同价&nbsp;</th>
				<th colspan="5">&nbsp;当月计量(单位：元)&nbsp;</th>
				<th colspan="5">&nbsp;累计计量(单位：元)&nbsp;</th>
				<th fieldname="JZJLZ" rowspan="2" colindex=18 tdalign="right">&nbsp;以前年度结转监理审定值&nbsp;</th>
				<th fieldname="LJJLZ" rowspan="2" colindex=19 tdalign="right">&nbsp;当前年度结转监理审定值&nbsp;</th>
				<th fieldname="WGBFB" rowspan="2" colindex=20 tdalign="right">&nbsp;完工百分比&nbsp;</th>
				<th fieldname="BZ" rowspan="2" colindex=21 maxlength="15">&nbsp;备注&nbsp;</th>
			</tr>
			<tr>
				<th fieldname="GCK_DY" colindex=8 tdalign="right">&nbsp;甲供材款&nbsp;</th>
				<th fieldname="GCK1_DY" colindex=9 tdalign="right">&nbsp;工程款&nbsp;</th>
				<th fieldname="DYJLSDZ" colindex=10 tdalign="right">&nbsp;监理审定值&nbsp;</th>
				<th fieldname="DYZBJ" colindex=11 tdalign="right">&nbsp;质保金&nbsp;</th>
				<th fieldname="DYYFK" colindex=12 tdalign="right">&nbsp;当月应付款&nbsp;</th>
				
				<th fieldname="GCK_LJ" colindex=13 tdalign="right">&nbsp;甲供材款&nbsp;</th>
				<th fieldname="GCK1_LJ" colindex=14 tdalign="right">&nbsp;工程款&nbsp;</th>
				<th fieldname="LJJLSDZ" colindex=15 tdalign="right">&nbsp;监理审定值&nbsp;</th>
				<th fieldname="LJZBJ" colindex=16 tdalign="right">&nbsp;质保金&nbsp;</th>
				<th fieldname="LJDYYFK" colindex=17 tdalign="right">&nbsp;累计应付款&nbsp;</th>
				
			</tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	</div>
   </div>
  </div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "JLYF" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
         <input type="hidden" name="queryResult" id = "queryResult">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>

</html>