<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title>项目选择</title>
<%
	String xmglgs = request.getParameter("xmglgs");
%>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script type="text/javascript" charset="utf-8">
var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
var controllername= "${pageContext.request.contextPath }/kfgl/kfglController.do";
var controllername_xm = "${pageContext.request.contextPath }/xmxxController.do";
var xmglgs = '<%=xmglgs%>';

//计算本页表格分页数
function setPageHeight(){
	var height = getDivStyleHeight()-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#xdxmkList").attr("pageNum",pageNum);
}

//自定义的获取页面查询条件
function getQueryCondition(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
    return data;
}

function queryList(){
	//生成json串
	var data = getQueryCondition();
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryXdxmk&xmglgs="+xmglgs,data,xdxmkList);
}

//页面初始化
$(function() {
	setPageHeight();
	init();
	initPage();
	showAutoComplete("QXMMC",autocompleteXmmcController+"?xmmcAutoQueryByXmglgs","getQueryCondition"); 
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        //其他处理放在下面
        initPage();
    });
  	//按钮绑定事件（确定）
    $("#btnQd").click(function() {
    	if($("#xdxmkList").getSelectedRowIndex()==-1)
		 {
			xInfoMsg('请选择一条记录！',"");
		    return
		 }
        var rowValue = $("#xdxmkList").getSelectedRow();//获得选中行的json对象
        $(window).manhuaDialog.setData(rowValue);
		$(window).manhuaDialog.sendData();
		$(window).manhuaDialog.close();
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
    		id = convertJson.string2json1(rowValue).XMID;
    		$(window).manhuaDialog({"title":"项目信息管理>项目信息维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmglgs/xmxxgl/xmxx_wh_temp.jsp?id="+id,"modal":"2"});
    	}else{
    		id = convertJson.string2json1(rowValue).BDID;
    		$(window).manhuaDialog({"title":"项目信息管理>标段信息维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmglgs/xmxxgl/bdxx_wh_temp.jsp?id="+id,"modal":"2"});
    	}
    	
    });
});

function init(){
	$("#xmglgs").val(<%=xmglgs%>);
}

function initPage(){
	 $("#QueryND").attr("src","T#GC_JH_SJ : distinct ND:ND AS NND:SFYX='1' order by NND asc ");
	 $("#QueryND").attr("kind","dic");
	 $("#QueryND").html('');
	reloadSelectTableDic($("#QueryND"));
	setDefaultNd("QueryND");
	//setDefaultOption($("#QueryND"),new Date().getFullYear());
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
	if(obj.BDBH == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDBH;
	}
}
//回调函数
getWinData = function(data){
	var data1 = defaultJson.packSaveJson(data);
	if(convertJson.string2json1(data).FLAG == "XMXX"){
		defaultJson.doUpdateJson(controllername_xm + "?insert&ywid="+convertJson.string2json1(data).YWID, data1,xdxmkList);
		
	}else{
		defaultJson.doUpdateJson(controllername_xm + "?insertBdxx", data1,xdxmkList);
	}
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
	data = defaultJson.getQueryConditionWithNowPageNum(data,"xdxmkList");
	defaultJson.doQueryJsonList(controllername+"?queryXdxmk&xmglgs="+xmglgs,data,xdxmkList);
	
};
</script>
</head>
<body>
	<app:dialogs />
	<div class="container-fluid">

		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
				<h4 class="title">
					项目信息 <span class="pull-right">
						<button id="btnWhxmTemp" class="btn" type="button">信息维护</button>
						<button id="btnQd" class="btn" type="button">确定</button>
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

							<!--公共的查询过滤条件 -->
							<th width="5%" class="right-border bottom-border text-right">年度</th>
							<td class="right-border bottom-border" width="5%"><select
								class="span12 year" id="QueryND" name="QueryND"
								fieldname="t.ND" operation="=" defaultMemo="全部">
							</select>
							</td>
							<th width="7%" class="right-border bottom-border text-right">项目编号</th>
							<td class="right-border bottom-border" width="10%">
							<input class="span12" type="text"  name="XMBH" fieldname="t.XMBH"  id="XMBH" operation="like">
							</td>
							<th width="7%" class="right-border bottom-border text-right">项目名称</th>
							<td class="right-border bottom-border" width="15%"><input
								class="span12" type="text" placeholder="" name="QXMMC"
								fieldname="t.XMMC" operation="like" id="QXMMC"
								autocomplete="off" tablePrefix="t"></td>
							<th width="7%" class="right-border bottom-border text-right">标段名称</th>
							<td class="right-border bottom-border" width="15%">
							<input class="span12" type="text"  name="BDMC" fieldname="t.BDMC"  id="BDMC" operation="like">
							</td>
							<td class="text-left bottom-border text-right">
								<button id="btnQuery" class="btn btn-link" type="button">
									<i class="icon-search"></i>查询
								</button>
								<button id="btnClear" class="btn btn-link" type="button">
									<i class="icon-trash"></i>清空
								</button>
							</td>
						</tr>
					</table>
				</form>
				<div style="height: 5px;"></div>
				<div class="overFlowX">
				<table width="100%" class="table-hover table-activeTd B-table"
					id="xdxmkList" type="single" pagenum="6">
					<thead>
						<tr>
							<th name="XH" id="_XH" style="width: 10px">&nbsp;#&nbsp;</th>
							<th fieldname="XMBH" tdalign="center" rowMerge="true">&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" maxlength="20" rowMerge="true">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDBH" CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
							<th fieldname="BDMC" maxlength="20" CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
							<th fieldname="XMBDDZ" maxlength="20" >&nbsp;项目地址&nbsp;</th>
							<th fieldname="XMGLGS" tdalign="center">&nbsp;项目管理公司&nbsp;</th>
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
			<input type="hidden" name="queryXML" id="queryXML"> <input
				type="hidden" name="txtXML" id="txtXML"> <input
				type="hidden" name="txtFilter" order="asc" fieldname="t.xmbh,t.xmbs,t.pxh"
				id="txtFilter"> <input type="hidden" name="resultXML"
				id="resultXML">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">

		</FORM>
	</div>
</body>
</html>