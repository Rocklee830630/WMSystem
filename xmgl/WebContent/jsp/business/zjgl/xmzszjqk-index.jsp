<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<title>项目征收资金情况首页</title>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglZszjXmzszjqkController.do";
var flag,rowIndex;

//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryGroup",data,DT1);
	});
	//按钮绑定事件(新增)
	$("#btnInsert").click(function() {
		$(window).manhuaDialog({"title":"项目征收资金情况>新增","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/xmzszjqk-addOne.jsp?type=insert","modal":"2"});
	});
	//按钮绑定事件(修改)
	$("#btnEdit").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		flag=true;
		$(window).manhuaDialog({"title":"项目征收资金情况>维护","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/xmzszjqk-add.jsp?type=update","modal":"2"});
	});
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 var t = $("#DT1").getTableRows();
		 if(t<=0)
		 {
			 xAlert("提示信息","请至少查询出一条记录！");
			 return;
		 }
	  	 $(window).manhuaDialog({"title":"导出","type":"text","content":"${pageContext.request.contextPath }/jsp/framework/print/TabListEXP.jsp?tabId=DT1","modal":"3"});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
});

//页面默认参数
function init(){
	getNd();
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryGroup",data,DT1);
}
//默认年度
function getNd(){
	//年度信息，里修改
	var y = new Date().getFullYear();
	$("#QND option").each(function(){
		if(this.value == y){
		 	$(this).attr("selected", true);
		 	return true;
		}
	});
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
		if(defaultJson.doInsertJson(controllername + "?insert", data1)){
			//刷新当前页
			$("#btnQuery").click();
		}
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
	$(window).manhuaDialog({"title":"项目征收资金情况>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/xmzszjqk-add.jsp?type=detail","modal":"2"});
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
				项目征收资金情况
				<span class="pull-right">  
					<button id="btnInsert" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">新增</button>
      				<button id="btnEdit" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">修改</button>
      				<button id="btnExpExcel" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">导出</button>
				</span>
			</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>
						<td width="9%" class="right-border bottom-border">
							<select class="span12" id="QND" fieldname="gjs.ND" operation="=" kind="dic" src="XMNF"  defaultMemo="-年度-">
<%--							<select class="span12" type="date" fieldtype="year" fieldformat="yyyy" id="QZFRQ" name="ZFRQ" fieldname="ZFRQ" operation="=" kind="dic" src="T#GC_ZJGL_ZSZJ_ZFZSB: distinct TO_CHAR(zfrq, 'yyyy') as zfrq:TO_CHAR(zfrq, 'yyyy') AS x:SFYX='1' order by zfrq"></select>--%>
						</td>
						<th width="5%" class="right-border bottom-border text-right">项目编号</th>
						<td class="right-border bottom-border" width="20%">
							<input class="span12" type="text" name = "QXMBH" fieldname = "gjs.XMBH" operation="like" >
						</td>
						<th width="5%" class="right-border bottom-border text-right">项目名称</th>
						<td class="right-border bottom-border" width="20%">
							<input class="span12" type="text" name = "QXMMC" fieldname = "gjs.XMMC" operation="like" >
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
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single"  pageNum="18">
	                <thead>
	                	<tr>
	                		<th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
	                		<th fieldname="XMBH" colindex=2 tdalign="center" maxlength="36">&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" colindex=3 tdalign="center" maxlength="36">&nbsp;项目名称&nbsp;</th>
							<th fieldname="ZFJE" colindex=5 tdalign="center" maxlength="17">&nbsp;支付总金额&nbsp;</th>
							<th fieldname="ZFCS" colindex=5 tdalign="center" maxlength="17">&nbsp;支付次数&nbsp;</th>
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
<%--		<input type="hidden" name="txtFilter" order="desc" fieldname="t.LRSJ" id="txtFilter" />--%>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
</div>
</body>
</html>