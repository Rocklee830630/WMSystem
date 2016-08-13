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
<title>项目管理公司-工程计量信息</title>
<%
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String userid = user.getAccount();
	String username = user.getName();
	String sysdate = Pub.getDate("yyyy-MM");
%>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/gcjl/gcjlController.do";
var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
var xmglgs = '<%=deptId%>';
var p_selectIndex = '0';
//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#gcjlList").attr("pageNum",pageNum);
}


//页面初始化
$(function() {
	
	setPageHeight();
	//自动完成项目名称模糊查询
	showAutoComplete("QXMMC",autocompleteXmmcController+"?xmmcAutoQuery","getXmmcQueryCondition"); 
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 if(exportRequireQuery($("#gcjlList"))){//该方法需传入表格的jquery对象
		      printTabList("gcjlList","gcjlgl.xls","XMBH,XMID,BDID,HTID,JLYF,DYJLSDZ","2,1");
		  }
	});
	//按钮绑定事件(新增 )
	$("#btnInsert").click(function() {
		if($("#gcjlList").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#gcjlList").getSelectedRow());
		$(window).manhuaDialog({"title":"工程计量管理>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmglgs/gcjlgl/gcjl_insert.jsp"});
	});
	//按钮绑定事件(修改 )
	$("#btnUpdate").click(function() {
		if($("#gcjlList").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		var rowValue = $("#gcjlList").getSelectedRow();//获得选中行的json对象
		var id = convertJson.string2json1(rowValue).GC_JH_SJ_ID;
		$(window).manhuaDialog({"title":"工程计量管理>修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmglgs/gcjlgl/gcjl_update.jsp?id="+id});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
        setDefaultMonth();
    });
	
});
//页面默认参数
function init(){
	getNd();
	
	setDefaultMonth();
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}
//默认年度
function getNd(){
	setDefaultNd("qnd");
	//setDefaultOption($("#qnd"),new Date().getFullYear());
}
//设置月份字典值
function setDefaultMonth(){
	var sysdate = getMaxMonth($("#qnd").val(),$("#XMGLGS").val());
	if(sysdate == ""){
		sysdate = '<%=sysdate%>';
	}
	$("#jlyf").val(sysdate);
	//setDefaultOption($("#jlyf"),);
}
//获取最大月份
function getMaxMonth(nd,xmglgs){
	var month = "";
	var actionName=controllername+"?queryMaxMonth&nd="+nd+"&xmglgs="+xmglgs;
	$.ajax({
		url : actionName,
		data : null,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(result) {
			month = result.msg;
		},
	    error : function(result) {
		}
	});
    return month;
}
//查询列表
function queryList(){
	var yf = $("#jlyf").val();
	if(yf == ""){
		xInfoMsg('请选择<计量月份>！',"");
		return;
	}
	var nd = $("#qnd").val();
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcjlList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query&xmglgs="+xmglgs+"&yf="+yf+"&nd="+nd,data,gcjlList);
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

//回调函数
getWinData = function(data){
	var data1 = defaultJson.packSaveJson(data);
	defaultJson.doUpdateJson(controllername + "?insert", data1,gcjlList);
	//生成json串
	var yf = $("#jlyf").val();
	if(yf == ""){
		xInfoMsg('请选择<计量月份>！',"");
		return;
	}
	var nd = $("#qnd").val();
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcjlList);
	data = defaultJson.getQueryConditionWithNowPageNum(data,"gcjlList");
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query&xmglgs="+xmglgs+"&yf="+yf+"&nd="+nd,data,gcjlList);
	
};
//项目名称自动模糊查询参数
function getXmmcQueryCondition(){
	var initData = '{"querycondition":{"conditions":[]}}';
	var jsonData = convertJson.string2json1(initData);
	//年度
	if("" != $("#qnd").val()){
		var defineCondition = {"value": $("#qnd").val(),"fieldname":"t.ND","operation":"=","logic":"and"};
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

function tr_click(obj){
	if(obj.ISNOBDXM == "0" &&  obj.XMBS == "0"){
		$("#btnInsert").attr("disabled","disabled");
	}else{
		$("#btnInsert").removeAttr("disabled");
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
      <h4 class="title">工程计量
      	<span class="pull-right">
      		<app:oPerm url="jsp/business/xmglgs/gcjlgl/gcjl_insert.jsp">
      			<button id="btnInsert" class="btn" type="button">新增</button>
      		</app:oPerm>
      		<app:oPerm url="jsp/business/xmglgs/gcjlgl/gcjl_update.jsp">
      			<button id="btnUpdate" class="btn" type="button">修改</button>
      		</app:oPerm>
      			<button id="btnExpExcel" class="btn"  type="button">导出</button>
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
	        <td class="right-border bottom-border" width="6%">
	            <select class="span12 year" id="qnd" name = "QND" defaultMemo="全部" operation="=" kind="dic" src="T#GC_JH_SJ:distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE">
            	</select>
	        </td>
	        <th width="5%" class="right-border bottom-border text-right">计量月份</th>
			<td class="right-border bottom-border" width="6%">
           		<input class="span12 date" id="jlyf" type="month" name="JLYF" check-type="month" operation="="/>
			</td>
	        <th width="5%" class="right-border bottom-border text-right">项目名称</th>
			<td class="right-border bottom-border" width="20%"><input
			class="span12" type="text" placeholder="" name="QXMMC"
			fieldname="t.XMMC" operation="like" id="QXMMC"
			autocomplete="off" tablePrefix="t">
		  </td>
			<td class="text-left bottom-border text-right">
	        	<button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
	        	<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
	        </td>
		</tr>
      </table>
      </form>
<div style="height:5px;"> </div>
	<div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="gcjlList" width="100%" type="single" pageNum="10" printFileName="工程计量">
		<thead>
			<tr>
				<th name="XH" rowspan=2 colindex=1 id="_XH">&nbsp;#&nbsp;</th>
				<th fieldname="XMID" rowspan=2 colindex=2 CustomFunction="doView" noprint="true">&nbsp;&nbsp;</th>
				<th fieldname="XMBH" rowspan=2 colindex=3 rowMerge="true" hasLink="true" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" rowspan=2 colindex=4 rowMerge="true" maxlength="15">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDID" rowspan="2" colindex=5 CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" rowspan=2 colindex=6 CustomFunction="doBdmc" maxlength="15">&nbsp;标段名称&nbsp;</th>
				<th fieldname="XMBDDZ" rowspan=2 colindex=7 maxlength="15">&nbsp;项目地址&nbsp;</th>
				<th fieldname="HTBM" rowspan=2 colindex=8>&nbsp;合同编号&nbsp;</th>
				<th fieldname="JLYF" rowspan=2 colindex=9 tdalign="center">&nbsp;计量月份&nbsp;</th>
				<th fieldname="DYJLSDZ" rowspan=2 colindex=10 tdalign="right">&nbsp;当月监理审定值&nbsp;</th>
				<th colspan="5">&nbsp;累计计量(单位：元)&nbsp;</th>
				<th fieldname="JZJLZ" rowspan=2 colindex=16 tdalign="right">&nbsp;以前年度结转<br>&nbsp;监理审定值&nbsp;</th>
				<th fieldname="LJJLZ" rowspan=2 colindex=17 tdalign="right">&nbsp;当前年度结转<br>&nbsp;监理审定值&nbsp;</th>
				<th fieldname="WGBFB" rowspan=2 colindex=18 tdalign="right">&nbsp;完工百分比&nbsp;</th>
			</tr>
			<tr>
				<th fieldname="GCK_LJ" colindex=11 tdalign="right">&nbsp;甲供材款&nbsp;</th>
				<th fieldname="GCK1_LJ" colindex=12 tdalign="right">&nbsp;工程款&nbsp;</th>
				<th fieldname="LJJLSDZ" colindex=13 tdalign="right">&nbsp;监理审定值&nbsp;</th>
				<th fieldname="LJZBJ" colindex=14 tdalign="right">&nbsp;质保金&nbsp;</th>
				<th fieldname="LJDYYFK" colindex=15 tdalign="right">&nbsp;累计应付款&nbsp;</th>
				
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