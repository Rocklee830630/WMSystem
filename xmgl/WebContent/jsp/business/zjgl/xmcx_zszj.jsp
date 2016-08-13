<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title>项目选择</title>
<%
	String isPq = (String)request.getParameter("isPq");
	isPq = org.apache.commons.lang.StringUtils.isBlank(isPq)?null:isPq;
%>

<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script type="text/javascript" charset="utf-8">
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglZszjXmzszjqkController.do";
var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";


//自定义的获取页面查询条件
function getQueryCondition(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
    return data;
}

function queryList(){
	//生成json串
	var data = getQueryCondition();
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryXdxmkZszj",data,xdxmkList);
}

//页面初始化
$(function() {
	init();
	initPage();
	showAutoComplete("QXMMC",autocompleteXmmcController+"?xmmcAutoQuery","getQueryCondition"); 
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
});

function init(){
	
}

function initPage(){
	 $("#QueryND").attr("src","T#GC_JH_SJ: distinct ND:ND:SFYX='1'");
	 $("#QueryND").attr("kind","dic");
	 $("#QueryND").html('');
	reloadSelectTableDic($("#QueryND"));
	setDefaultOption($("#QueryND"),new Date().getFullYear());
}

//标段图标样式
function doBdmc(obj){
	if(obj.XMBS == "0"){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}
</script>
</head>
<body>
	<app:dialogs />
	<div class="container-fluid">

		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
				<h4 class="title">
					项目信息 <span class="pull-right">
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
							<th width="3%" class="right-border bottom-border text-right">年度</th>
							<td class="right-border bottom-border" width="5%"><select
								class="span12 year" id="QueryND" name="QueryND"
								fieldname="t.ND" operation="=" defaultMemo="全部">
							</select>
									<input style="display: none;" class="span12" type="text"
									id="QXMSX" name="QXMSX" fieldname="t.XMSX"
									operation="=" />
									<input style="display: none;" class="span12" type="text"
									id="QISXF" name="QISXF" fieldname="t.ISXF"
									operation="=" value="" keep="true" />
									<% if(null != isPq) {%>
									<input style="display:none;" class="span12" type="text"
									id="ISPQ" name="ISPQ" fieldname="t.ISPQ"
									operation="=" value="<%=isPq%>" keep="true" />
									<% }%>
							</td>
							<th width="7%" class="right-border bottom-border text-right">项目编号</th>
							<td class="right-border bottom-border" width="10%">
							<input class="span12" type="text"  name="XMBH" fieldname="t.XMBH" id="XMBH"  operation="like">
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
							<th width="5%" class="right-border bottom-border text-right">是否已拨付</th>
							<td class="right-border bottom-border" width="8%">
							<select	class="span12" 	fieldname="vzzzt.bmtqkmxzt" operation="=" defaultMemo="全部">
								<option value="7">是</option>
								<option value="">否</option>
							</select>
							

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
				<table width="100%" class="table-hover table-activeTd B-table"
					id="xdxmkList" type="single" pageNum="6">
					<thead>
						<tr>
							<th name="XH" id="_XH" style="width: 10px">&nbsp;#&nbsp;</th>
							<th fieldname="XMBH" tdalign="center">&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" maxlength="20">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDMC" maxlength="20" CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
							<!-- <th fieldname="XMSX" tdalign="center">&nbsp;项目属性&nbsp;</th>-->
							<th fieldname="XMGLGS" tdalign="center">&nbsp;项目管理公司&nbsp;</th>
							<!--
			<th fieldname="YZDB" maxlength="15">&nbsp;业主代表&nbsp;</th>
			<th fieldname="SJDW" maxlength="15">&nbsp;设计单位&nbsp;</th>
			<th fieldname="JLDW" maxlength="15">&nbsp;监理单位&nbsp;</th>
			<th fieldname="SGDW" maxlength="15">&nbsp;施工单位&nbsp;</th>
			<th fieldname="WGRQ" tdalign="center">&nbsp;完工日期&nbsp;</th>
			 -->
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
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