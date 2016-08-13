<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>工程部-工程计量统计</title>
<%
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String userid = user.getAccount();
	String username = user.getName();
	String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script type="text/javascript" charset="utf-8">

//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/gcjl/gcjlController.do";
var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
var xmglgs = '<%=deptId%>';

//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-(pageQuery*2)-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#gcjlList").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	setPageHeight();
	//自动完成项目名称模糊查询
	showAutoComplete("QXMMC",autocompleteXmmcController+"?xmmcAutoQueryByXmglgs","getXmmcQueryCondition"); 
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
        setDefaultOption($("#xmnd"),new Date().getFullYear());
        hideNdYd();
        //clearRadio();
        //alert($("#radio1").attr("Checked"));
        //$(":radio[name='tjfs'][id='radio1']").attr("checked",true);
        document.getElementById("radio1").checked = true;


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
	defaultJson.doQueryJsonList(controllername+"?queryTjxx&xmglgs="+xmglgs+"&nd="+nd+"&xmnd="+xmnd+jlyf,data,gcjlList);
}
//切换单选按钮
function clickRadioShowDate(value){
	if(value == 1){
		$("#td_x_nd").show();
		$("#td_x_xmnd").show();
		$("#td_s_nd").hide();
		$("#td_s_yf").hide();
		$("#jlyf").val("");
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
	if("" != $("#XMGLGS").val()){
		var defineCondition = {"value": +xmglgs,"fieldname":"t.XMGLGS","operation":"=","logic":"and"};
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
		return obj.BDBH;
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
      <h4 class="title">工程计量统计
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
        	<th width="5%" class="right-border bottom-border text-right">年度</th>
			<td class="right-border bottom-border">
				<select class="span12 year" id="xmnd" name = "QXMND" defaultMemo="全部" operation="="  kind="dic" src="T#GC_JH_SJ:distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE">
            	</select>
			</td>
			<th width="5%" class="right-border bottom-border text-right">项目名称</th>
			<td class="right-border bottom-border"><input
				class="span12" type="text" placeholder="" name="QXMMC"
				fieldname="t.XMMC" operation="like" id="QXMMC"
				autocomplete="off" tablePrefix="t">
		  	</td>
			<th width="5%" class="right-border bottom-border text-right">标段名称</th>
			<td class="right-border bottom-border">
				<input class="span12" type="text" name = "QBDMC" fieldname = "t.BDMC" operation="like" >
			</td>
			<td class="text-left bottom-border text-right" >
	           <button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
	           <button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
	        </td>
         </tr>
         <tr>
         	<th width="5%" class="right-border bottom-border text-right">统计方式</th>
			<td class="right-border bottom-border">&nbsp;
				<span>
					<input class="" id="radio1" type="radio" name = "tjfs" onclick="clickRadioShowDate(1)" checked="checked"/>&nbsp;&nbsp;按年度统计
					<input class="" id="radio2" type="radio" name = "tjfs" onclick="clickRadioShowDate(2)"/>&nbsp;&nbsp;按月度统计
				</span>
			</td>
         	<th id="td_x_nd" width="5%" class="right-border bottom-border text-right">计量年度</th>
			<td id="td_x_xmnd" class="right-border bottom-border">
				<select class="span12 year" id="qnd" name = "QND" defaultMemo="全部" operation="=" kind="dic" src="T#GC_XMGLGS_JLB:distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE">
            	</select>
			</td>
			<th id="td_s_nd" width="5%" class="right-border bottom-border text-right" style="display:none;">计量月份</th>
			<td id="td_s_yf" class="right-border bottom-border" style="display:none;">
				<input class="span12 date" id="jlyf" type="month" name="JLYF">
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
	<table class="table-hover table-activeTd B-table" id="gcjlList" width="100%" type="single" pageNum="10" printFileName="工程计量统计">
		<thead>
			<tr>
				<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
				<th fieldname="XMID" CustomFunction="doView" noprint="true">&nbsp;&nbsp;</th>
				<th fieldname="XMBH" rowMerge="true" hasLink="true" linkFunction="rowView" >&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMID" rowMerge="true">&nbsp;项目名称&nbsp;</th>
				<th fieldname="XMBDDZ" >&nbsp;项目地址&nbsp;</th>
				<th fieldname="BDID" CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
				<th fieldname="HTBM">&nbsp;合同编号&nbsp;</th>
				<th fieldname="LJJLSDZ" tdalign="right">&nbsp;累计监理审定值&nbsp;</th>
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
       <input type="hidden" name="txtFilter"  order="ASC" fieldname = "T.XMBH,T.XMBS,T.PXH"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>