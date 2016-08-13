<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base />
<title>项目管理公司-项目信息管理</title>
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
var controllername = "${pageContext.request.contextPath }/xmxxController.do";
var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
var xmglgs = <%=deptId%>;
//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#xdxmkList").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	setPageHeight();
	//自动完成项目名称模糊查询
	showAutoComplete("QXMMC",autocompleteXmmcController+"?xmmcAutoQueryByXmglgs","getQueryCondition"); 
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
  	//按钮绑定事件（项目信息）
    $("#btnWhxm").click(function() {
    	if($("#xdxmkList").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
    	var rowValue = $("#xdxmkList").getSelectedRow();//获得选中行的json对象
    	var id = "";
    	if(convertJson.string2json1(rowValue).BDID == ""){
    		id = convertJson.string2json1(rowValue).GC_TCJH_XMXDK_ID;
    		$(window).manhuaDialog({"title":"项目信息管理>项目信息维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmglgs/xmxxgl/xmxx_wh.jsp?id="+id});
    	}else{
    		id = convertJson.string2json1(rowValue).BDID;
    		$(window).manhuaDialog({"title":"项目信息管理>标段信息维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmglgs/xmxxgl/bdxx_wh.jsp?id="+id});
    	}
    	
    });
  	//按钮绑定事件（临时信息维护,这个可以把施工单位等信息回写到项目表中）
	$("#btnWhxmTemp").click(function() {
		if($("#xdxmkList").getSelectedRowIndex()==-1){
			requireSelectedOneRow();
		    return
		}
    	var rowValue = $("#xdxmkList").getSelectedRow();//获得选中行的json对象
    	var id = "";
    	if(convertJson.string2json1(rowValue).BDID == ""){
    		id = convertJson.string2json1(rowValue).GC_TCJH_XMXDK_ID;
    		$(window).manhuaDialog({"title":"项目信息管理>项目信息维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmglgs/xmxxgl/xmxx_wh_temp.jsp?id="+id});
    	}else{
    		id = convertJson.string2json1(rowValue).BDID;
    		$(window).manhuaDialog({"title":"项目信息管理>标段信息维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmglgs/xmxxgl/bdxx_wh_temp.jsp?id="+id});
    	}
    	
    });
  	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 if(exportRequireQuery($("#xdxmkList"))){//该方法需传入表格的jquery对象
		      printTabList("xdxmkList");
		  }
	});
});
//初始化页面
function init(){
	getNd();
	
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}
//默认年度
function getNd(){
	setDefaultNd("qnd");
	//setDefaultOption($("#qnd"),new Date().getFullYear());
}
//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query&xmglgs="+xmglgs,data,xdxmkList);
}
//自定义的获取页面查询条件
function getQueryCondition(){
/* 	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
    return data; */
    
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
	var obj = $("#xdxmkList").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).GC_TCJH_XMXDK_ID;
	$(window).manhuaDialog(xmscUrl(id));
}
//回调函数
getWinData = function(data){
	var data1 = defaultJson.packSaveJson(data);
	if(convertJson.string2json1(data).FLAG == "XMXX"){
		defaultJson.doUpdateJson(controllername + "?insert&ywid="+convertJson.string2json1(data).YWID, data1,xdxmkList);
		
	}else{
		defaultJson.doUpdateJson(controllername + "?insertBdxx", data1,xdxmkList);
	}
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
	data = defaultJson.getQueryConditionWithNowPageNum(data,"xdxmkList");
	defaultJson.doQueryJsonList(controllername+"?query&xmglgs="+xmglgs,data,xdxmkList);
	
};
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
	if(obj.BDBH == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDBH;
	}
}
</script>
</head>
<body>
<app:dialogs />
<div class="container-fluid">
<p></p>
<div class="row-fluid">
	<div class="B-small-from-table-autoConcise">
	<h4 class="title">项目信息管理
		<span class="pull-right">
			<app:oPerm url="jsp/business/xmglgs/xmxxgl/xmxx_wh.jsp">
			<button id="btnWhxm" class="btn" type="button">项目信息</button>
			</app:oPerm>
			<app:oPerm url="jsp/business/xmglgs/xmxxgl/xmxx_wh_temp.jsp">
			<button id="btnWhxmTemp" class="btn" type="button">临时信息维护</button>
			</app:oPerm>
			<button id="btnExpExcel" class="btn"  type="button">导出</button>
		</span>
	</h4>
	<form method="post" id="queryForm">
		<table class="B-table" width="100%">
			<!--可以再此处加入hidden域作为过滤条件 -->
			<TR style="display: none;">
				<TD class="right-border bottom-border"></TD>
				<TD class="right-border bottom-border">
				</TD>
			</TR>
			<!--可以再此处加入hidden域作为过滤条件 -->
			<tr>
				<th width="5%" class="right-border bottom-border text-right">年度</th>
				<td class="right-border bottom-border" width="6%">
					<select class="span12 year" id="qnd" name = "QND" defaultMemo="全部" fieldname = "t.ND" operation="=" kind="dic" src="T#GC_JH_SJ: distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE">
            		</select>
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
	<div style="height: 5px;"></div>
	<div class="overFlowX">
		<table class="table-hover table-activeTd B-table" id="xdxmkList"  width="100%" editable="0" type="single" pageNum="10" printFileName="项目信息管理">
			<thead>
				<tr>
					<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
					<th fieldname="XMBH" rowspan="2" colindex=2 hasLink="true" linkFunction="rowView" rowMerge="true">&nbsp;项目编号&nbsp;</th>
					<th fieldname="XMMC" rowspan="2" colindex=3 maxlength="15" rowMerge="true">&nbsp;项目名称&nbsp;</th>
					<th fieldname="BDBH" rowspan="2" colindex=4 CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
					<th fieldname="BDMC" rowspan="2" colindex=5 maxlength="15" CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
					<th fieldname="XMBDDZ" rowspan="2" colindex=6 maxlength="15" >&nbsp;项目地址&nbsp;</th>
					<th fieldname="XMLX" rowspan="2" colindex=7 tdalign="center">&nbsp;项目类型&nbsp;</th>
					<th fieldname="YZDB" rowspan="2" colindex=8 tdalign="center">&nbsp;业主代表&nbsp;</th>
					<th fieldname="LXFS_YZDB" rowspan="2" colindex=9>&nbsp;业主代表&nbsp;<br>&nbsp;联系方式&nbsp;</th>
					<th colspan="3">&nbsp;项目管理公司&nbsp;</th>
					<th colspan="3">&nbsp;设计单位&nbsp;</th>
					<th colspan="3">&nbsp;施工单位&nbsp;</th>
					<th colspan="3">&nbsp;监理单位&nbsp;</th>
				</tr>
				<tr>
					<th fieldname="XMGLGS" colindex=10 >&nbsp;单位名称&nbsp;</th>
					<th fieldname="FZR_GLGS" colindex=11 tdalign="center">&nbsp;负责人&nbsp;</th>
					<th fieldname="LXFS_GLGS" colindex=12>&nbsp;联系电话&nbsp;</th>
					<th fieldname="SJDW" colindex=13 >&nbsp;单位名称&nbsp;</th>
					<th fieldname="SJDWFZR" colindex=14 tdalign="center">&nbsp;设计负责人&nbsp;</th>
					<th fieldname="SJDWFZRLXFS" colindex=15 >&nbsp;联系电话&nbsp;</th>
					<th fieldname="SGDW" colindex=16 >&nbsp;单位名称&nbsp;</th>
					<th fieldname="SGDWXMJL" colindex=17 tdalign="center">&nbsp;项目经理&nbsp;</th>
					<th fieldname="SGDWXMJLLXDH" colindex=18>&nbsp;联系电话&nbsp;</th>
					<th fieldname="JLDW" colindex=19 >&nbsp;单位名称&nbsp;</th>
					<th fieldname="JLDWZJ" colindex=20 tdalign="center">&nbsp;总监&nbsp;</th>
					<th fieldname="JLDWZJLXDH" colindex=21 >&nbsp;联系电话&nbsp;</th>
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
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="asc" fieldname = "T.XMBH,T.XMBS,T.PXH""	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>

</body>
</html>