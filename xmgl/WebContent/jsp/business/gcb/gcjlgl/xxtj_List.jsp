<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>工程部-工程计量汇总</title>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/gcjl/gcjlController.do";
var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
var xmglgs = "";
//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery*2-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#gcjlList").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	setPageHeight();
	//自动完成项目名称模糊查询
	showAutoComplete("QXMMC",autocompleteXmmcController+"?xmmcAutoQueryByXmglgs&xmglgs="+xmglgs,"getXmmcQueryCondition"); 
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
	  	if(exportRequireQuery($("#gcjlList"))){//该方法需传入表格的jquery对象
		      printTabList("gcjlList");
		  }
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
    	$("#queryForm").clearFormResult();
        getNd();
        $("#tr_nd").show();
		$("#tr_yd").hide();
        hideNdYd();
        document.getElementById("radio1").checked = true;
        $("#xmglgs").val("");
    });
	
});
//页面默认参数
function init(){
	getNd();
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}
//默认年度
function getNd(){
	setDefaultNd("xmnd");
	setDefaultNd("qnd");
	//setDefaultOption($("#xmnd"),new Date().getFullYear());
	//setDefaultOption($("#qnd"),new Date().getFullYear());
}
//查询列表
function queryList(){
	var jlyf = "";
	if($("#jlyf").val()){
		jlyf = "&yf1="+$("#jlyf").val();
	}
	var nd = $("#qnd").val();
	var xmnd = $("#xmnd").val();;
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcjlList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryTjxx&xmglgs="+$("#xmglgs").val()+"&nd="+nd+"&xmnd="+xmnd+jlyf,data,gcjlList);
}
//切换单选按钮
function clickRadioShowDate(value){
	if(value == 1){
		$("#td_x_nd").show();
		$("#td_x_xmnd").show();
		$("#td_s_nd").hide();
		$("#td_s_yf").hide();
		setDefaultNd("qnd");
		//setDefaultOption($("#qnd"),new Date().getFullYear());
	}else{
		$("#qnd").val("");
		setDefaultNd("qnd");
		//setDefaultOption($("#qnd"),new Date().getFullYear());
		$("#td_x_nd").hide();
		$("#td_x_xmnd").hide();
		$("#td_s_nd").show();
		$("#td_s_yf").show();
	}
}
//隐藏查询条件
function hideNdYd(){
	$("#td_x_nd").show();
	$("#td_x_xmnd").show();
	$("#td_s_nd").hide();
	$("#td_s_yf").hide();
	setDefaultNd("qnd");
	//setDefaultOption($("#qnd"),new Date().getFullYear());
}
//去掉选中状态
function clearRadio(){
 	$("input[type='radio']").each(function(){ 
        $(this).attr("checked", false);
     }); 
}
//<操作>对应事件
function doView(obj){
	if(obj.GC_JH_SJ_ID != null){
		var id = obj.GC_JH_SJ_ID;
		return '<div style="text-align:center"><a href="javascript:void(0);"><i title="查看月份计量" class="icon-align-justify" onclick="showGcjl(\''+id+'\')"></i></a></div>';
	}
}
//根据ID查询计量信息
function showGcjl(id){
	$(window).manhuaDialog({"title":"查看计量信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmglgs/gcjlgl/gcjl_list_view.jsp?id="+id,"modal":"2"});
}

//项目名称自动模糊查询参数
function getXmmcQueryCondition(){
	var initData = '{"querycondition":{"conditions":[]}}';
	var jsonData = convertJson.string2json1(initData);
	//年度
	if("" != $("#xmnd").val()){
		var defineCondition = {"value": $("#xmnd").val(),"fieldname":"t.ND","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	//项目名称
	if("" != $("#QXMMC").val()){
		var defineCondition = {"value": "%"+$("#QXMMC").val()+"%","fieldname":"t.XMMC","operation":"like","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	if("" != $("#xmglgs").val()){
		var defineCondition = {"value": +$("#xmglgs").val(),"fieldname":"t.XMGLGS","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	return JSON.stringify(jsonData);
}
//详细信息
function rowView(index){
	var obj = $("#gcjlList").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).XMID;
	$(window).manhuaDialog(xmscUrl(id));
}
//标段图标样式
function doBdmc(obj){
	if(obj.XMBS == "0"){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}
//标段编号图标样式
function doBdbh(obj){
	if(obj.BDID == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDID.SV;
	}
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">工程计量汇总
      	<span class="pull-right">
      		<button id="btnExpExcel" class="btn" type="button">导出</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
        	<th width="5%" class="right-border bottom-border text-right">项目管理公司</th>
			<td class="right-border bottom-border" width="15%">
				<select class="span12 2characters" id="xmglgs" name = "QXMGLGS" defaultMemo="全部"  operation="=" kind="dic" src="T#VIEW_YW_XMGLGS:ROW_ID:BMJC">
            	</select>
			</td>
			<th width="5%" class="right-border bottom-border text-right">年度</th>
			<td class="right-border bottom-border" width="15%">
				<select class="span12 year" id="xmnd" name = "QXMND" defaultMemo="全部" operation="=" fieldname = "t.ND" kind="dic" src="T#GC_JH_SJ:distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE">
            	</select>
			</td>
			<th width="5%" class="right-border bottom-border text-right">项目名称</th>
			<td class="right-border bottom-border" width="15%"><input
				class="span12" type="text" placeholder="" name="QXMMC"
				fieldname="t.XMMC" operation="like" id="QXMMC"
				autocomplete="off" tablePrefix="t">
		  	</td>
			<th width="5%" class="right-border bottom-border text-right">标段名称</th>
			<td class="right-border bottom-border" width="15%">
				<input class="span12" type="text" name = "QBDMC" fieldname = "t.BDMC" operation="like" >
			</td>
			<td class="text-left bottom-border text-right" width="20%">
	           <button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
	           <button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
	        </td>
         </tr>
         <tr>
         	<th width="5%" class="right-border bottom-border text-right">统计方式</th>
			<td class="right-border bottom-border">&nbsp;
				<span>
					<input class="" id="radio1" type="radio" name = "tjfs" onclick="clickRadioShowDate(1)" checked="checked"/>&nbsp;按年度
					<input class="" id="radio2" type="radio" name = "tjfs" onclick="clickRadioShowDate(2)"/>&nbsp;按月度
				</span>
			</td>
         	<th id="td_x_nd" width="5%" class="right-border bottom-border text-right">计量年度</th>
			<td id="td_x_xmnd" class="right-border bottom-border">
				<select class="span12 year" id="qnd" name = "QND" defaultMemo="全部" operation="=" kind="dic" src="T#GC_XMGLGS_JLB:distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE">
            	</select>
			</td>
			<th id="td_s_nd" width="5%" class="right-border bottom-border text-right" style="display:none;">计量月份</th>
			<td id="td_s_yf" class="right-border bottom-border" style="display:none;">
				<input class="span12" style="width:90%" id="jlyf" type="month" name="JLYF">
			</td>
			<th id="td_e_nd" width="5%" class="right-border bottom-border text-right" style="display:none;">结束</th>
			<td id="td_e_yf" class="right-border bottom-border" style="display:none;">
           		<span>
           		年份
           		<select class="span12 year" id="nd2" name = "ND" defaultMemo="全部" operation="=" kind="dic" src="T#GC_XMGLGS_JLB:distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE">
            	</select>
           		月份
           		<select class="span12 year" id="jlyf2" name = "QJLYF" defaultMemo="全部" operation="=" kind="dic" src="JLYF">
            	</select>
           		</span>
			</td>
         </tr>
      </table>
      </form>
    <div style="height:5px;"> </div>
  <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="gcjlList" width="100%" type="multi" pageNum="10" printFileName="工程计量汇总">
		<thead>
			<tr>
				<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
				<th fieldname="XMID" rowspan="2" colindex=2 CustomFunction="doView" noprint="true">&nbsp;&nbsp;</th>
				<th fieldname="XMBH" rowspan="2" colindex=3 rowMerge="true" hasLink="true" linkFunction="rowView" >&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMID" rowspan="2" colindex=4 rowMerge="true">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDID" rowspan="2" colindex=5 CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" rowspan="2" colindex=6 CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
				<th fieldname="XMBDDZ" rowspan="2" colindex=7 >&nbsp;项目地址&nbsp;</th>
				<th fieldname="HTBM" rowspan="2" colindex=8>&nbsp;合同编号&nbsp;</th>
				<th colspan="5">&nbsp;累计计量&nbsp;</th>
				<th fieldname="JZJLZ" rowspan="2" colindex=14 tdalign="right">&nbsp;以前年度结转监理审定值&nbsp;</th>
				<th fieldname="LJJLZ" rowspan="2" colindex=15 tdalign="right">&nbsp;当前年度累计监理审定值&nbsp;</th>
			</tr>
			<tr>
				<th fieldname="GCK_LJ" colindex=9 tdalign="right">&nbsp;甲供材款&nbsp;</th>
				<th fieldname="GCK1_LJ" colindex=10 tdalign="right">&nbsp;工程款&nbsp;</th>
				<th fieldname="LJJLSDZ" colindex=11 tdalign="right">&nbsp;监理审定值&nbsp;</th>
				<th fieldname="LJDYYFK" colindex=12 tdalign="right">&nbsp;应付款&nbsp;</th>
				<th fieldname="LJZBJ" colindex=13 tdalign="right">&nbsp;质保金&nbsp;</th>
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
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="asc" fieldname = "T.XMBH,T.XMBS,T.PXH"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>