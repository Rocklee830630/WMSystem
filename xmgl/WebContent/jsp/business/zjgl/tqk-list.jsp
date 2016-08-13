<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<title>提请款部门明细首页</title>
<%
	String type=request.getParameter("type");
	String jhsjid = request.getParameter("jhsjid");
	if(jhsjid==null)
	    jhsjid = "";
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglTqkbmmxController.do";

//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryYbfBMMX",data,DT1);
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
	
  //按钮绑定事件（确定）
    $("#btnQd").click(function() {
    	if($("#DT1").getSelectedRowIndex()==-1)
		 {
			xInfoMsg('请选择一条记录！',"");
		    return
		 }
        var rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
        $(window).manhuaDialog.setData(rowValue);
		$(window).manhuaDialog.sendData();
		$(window).manhuaDialog.close();
    });
	
});

//页面默认参数
function init(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryYbfBMMX",data,DT1);
}

//点击获取行对象
function tr_click(obj,tabListid){
	//alert(JSON.stringify(obj));
}

//回调函数--用于修改新增
getWinData = function(data){
	//var subresultmsgobj = defaultJson.dealResultJson(data);
	var data1 = defaultJson.packSaveJson(data);
	if(JSON.parse(data).ID == "" || JSON.parse(data).ID == null){
		defaultJson.doInsertJson(controllername + "?insert", data1,DT1);
	}else{
		defaultJson.doUpdateJson(controllername + "?update", data1,DT1);
	}
	
};

//详细信息
function rowView(index){
	$("#DT1").setSelect(index);
	if($("#DT1").getSelectedRowIndex()==-1)
	 {
		requireSelectedOneRow();
	    return
	 }
	$("#resultXML").val($("#DT1").getSelectedRow());
	$(window).manhuaDialog({"title":"提请款部门明细>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqkbmmx-add.jsp?type=detail","modal":"1"});
}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">
				提请款明细
				<span class="pull-right">  
					<button id="btnQd" class="btn" type="button">确定</button>
				</span>
			</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
							<input class="span12" type="text" id="QBMTQKMXZT" name="BMTQKMXZT"  fieldname="BMTQKMXZT" value="7" operation="=" >
							<inp+ut class="span12" type="text" id="QJHSJID" name="JHSJID"  fieldname="JHSJID" value="<%=jhsjid%>" operation="=" >
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>
						<th width="5%" class="right-border bottom-border text-right">时间</th>
						<td width="10%" class="right-border bottom-border">
							<select class="span12" id="QND" type="date" fieldtype="year" fieldformat="yyyy" name="BZRQ" fieldname="BZRQ" operation="=" kind="dic" src="XMNF"  defaultMemo="-年度-">
						</td>
						<th width="5%" class="right-border bottom-border text-right">项目名称</th>
						<td class="right-border bottom-border" width="20%">
							<input class="span12" type="text" id="QXMMC" name="XMMC" fieldname="XMMC" operation="like" >
						</td>
						<th width="5%" class="right-border bottom-border text-right">标段名称</th>
						<td class="right-border bottom-border" width="20%">
							<input class="span12" type="text" id="QBDMC" name="BDMC" fieldname="BDMC" operation="like" >
						</td>
						
						
			            <td class="text-left bottom-border text-right">
	                        <button id="btnQuery" class="btn btn-link"  type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-search"></i>查询</button>
           					<button id="btnClear" class="btn btn-link" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-trash"></i>清空</button>
			            </td>							
					</tr>
				</table>
			</form>
			<div style="height:5px;"> </div>	
			<div class="overFlowX">
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single"  pageNum="10">
	                <thead>
	                	<tr>
	                		<th  name="XH" id="_XH" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
	                		<th fieldname="QKMC" colindex=3 tdalign="center" maxlength="30">&nbsp;请款名称&nbsp;</th>
	                		<th fieldname="XMMC" colindex=2 tdalign="center" maxlength="30">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDMC" colindex=3 tdalign="center" maxlength="20">&nbsp;标段名称&nbsp;</th>
<%--							<th fieldname="HTMC" colindex=1 tdalign="center" maxlength="20" >&nbsp;合同名称&nbsp;</th>--%>
							<th fieldname="HTBM" colindex=6 tdalign="center" maxlength="20" >&nbsp;合同编码&nbsp;</th>
							<th fieldname="CWSHZ" colindex=20 tdalign="center" >&nbsp;财务审核值&nbsp;</th>
							<th fieldname="JCHDZ" colindex=21 tdalign="center" >&nbsp;计财核定值&nbsp;</th>
	                	</tr>
	                </thead>
	              	<tbody></tbody>
	           </table>
	       </div>
	 	</div>
	</div>     
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ" id="txtFilter"/>
<%--		<input type="hidden" name="txtFilter" order="desc" fieldname="ID" id="txtFilter"/>--%>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
</div>
</body>
</html>