<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<title>合同数据首页</title>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtsjController.do";

//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,DT1);
	});
	//按钮绑定事件(新增)
	$("#btnInsert").click(function() {
		parent.$("body").manhuaDialog({"title":"合同数据>新增","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/htsj-add.jsp?type=insert","modal":"1"});
	});
	//按钮绑定事件(修改)
	$("#btnUpdate").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#DT1").getSelectedRow());
		parent.$("body").manhuaDialog({"title":"合同数据>修改","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/htsj-add.jsp?type=update","modal":"1"});
	});
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 var t = $("#DT1").getTableRows();
		 if(t<=0)
		 {
			 xAlert("提示信息","请至少查询出一条记录！");
			 return;
		 }
	  	 $("body").manhuaDialog({"title":"导出","type":"text","content":"${pageContext.request.contextPath }/jsp/framework/print/TabListEXP.jsp?tabId=DT1","modal":"3"});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
    });
	
});

//页面默认参数
function init(){
	getNd();
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,DT1);
}
//默认年度
function getNd(){
	//年度信息，里修改
	$("#ZFRQ").val(new Date().getFullYear());
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
	parent.$("body").manhuaDialog({"title":"合同数据>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/htsj-add.jsp?type=detail","modal":"1"});
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
				合同数据
				<span class="pull-right">  
					<button id="btnInsert" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">新增</button>
      				<button id="btnUpdate" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">修改</button>
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
						<td width="7%" class="right-border bottom-border">
							<select class="span12" id="QND" name="ND" fieldname="ND" operation="=" kind="dic" src="XMNF"  defaultMemo="-年度-">
						</td>
						<th width="5%" class="right-border bottom-border text-right">项目名称</th>
						<td class="right-border bottom-border" width="20%">
							<input class="span12" type="text" id="QXMMC" name="XMMC" fieldname="XMMC" operation="like" >
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
	                		<th fieldname="ZFRQ" colindex=2 tdalign="center" maxlength="10" hasLink="true" linkFunction="rowView">&nbsp;支付日期&nbsp;</th>
							<th fieldname="ID" colindex=0 tdalign="center" maxlength="36">&nbsp;编号&nbsp;</th>
							<th fieldname="XMBH" colindex=1 tdalign="center" maxlength="36">&nbsp;项目编号&nbsp;</th>
							<th fieldname="DBID" colindex=2 tdalign="center" maxlength="36">&nbsp;标段ID&nbsp;</th>
							<th fieldname="ZTBID" colindex=3 tdalign="center" maxlength="36">&nbsp;招投标编号&nbsp;</th>
							<th fieldname="HTID" colindex=4 tdalign="center" maxlength="36">&nbsp;合同编号&nbsp;</th>
							<th fieldname="GCMC" colindex=5 tdalign="center" maxlength="1000">&nbsp;工程名称&nbsp;</th>
							<th fieldname="HTQDJ" colindex=6 tdalign="center" maxlength="17">&nbsp;合同签订价(元)&nbsp;</th>
							<th fieldname="GCJGBFB" colindex=7 tdalign="center" maxlength="5">&nbsp;价格百分比&nbsp;</th>
							<th fieldname="HTJSJ" colindex=8 tdalign="center" maxlength="17">&nbsp;合同结算价或中止价&nbsp;</th>
							<th fieldname="HTJSKSRQ" colindex=9 tdalign="center" maxlength="7">&nbsp;合同计划开始日期&nbsp;</th>
							<th fieldname="HTJHJSRQ" colindex=10 tdalign="center" maxlength="7">&nbsp;合同计划结束日期&nbsp;</th>
							<th fieldname="HTSJKSRQ" colindex=11 tdalign="center" maxlength="7">&nbsp;合同实际开始日期&nbsp;</th>
							<th fieldname="HTJSRQ" colindex=12 tdalign="center" maxlength="7">&nbsp;合同结算日期或实际结束日期&nbsp;</th>
							<th fieldname="MBLX" colindex=13 tdalign="center" maxlength="40">&nbsp;目标类型:变更、支付、完成投资、文件&nbsp;</th>
							<th fieldname="MBID" colindex=14 tdalign="center" maxlength="36">&nbsp;目标ID&nbsp;</th>
							<th fieldname="NF" colindex=15 tdalign="center" maxlength="4">&nbsp;年份&nbsp;</th>
							<th fieldname="YF" colindex=16 tdalign="center" maxlength="2">&nbsp;月份&nbsp;</th>
							<th fieldname="JE" colindex=17 tdalign="center" maxlength="17">&nbsp;金额&nbsp;</th>
							<th fieldname="MBSJLX" colindex=18 tdalign="center" maxlength="40">&nbsp;目标数据类型&nbsp;</th>
							<th fieldname="RQ" colindex=19 tdalign="center" maxlength="7">&nbsp;日期&nbsp;</th>
							<th fieldname="MC" colindex=20 tdalign="center" maxlength="1000">&nbsp;名称&nbsp;</th>
							<th fieldname="BGTS" colindex=21 tdalign="center" maxlength="22">&nbsp;变更天数&nbsp;</th>
							<th fieldname="ZJZH" colindex=22 tdalign="center" maxlength="36">&nbsp;资金账号&nbsp;</th>
							<th fieldname="ZHHTJ" colindex=23 tdalign="center" maxlength="17">&nbsp;最新合同价&nbsp;</th>
							<th fieldname="YWLX" colindex=24 tdalign="center" maxlength="6">&nbsp;业务类型&nbsp;</th>
							<th fieldname="SJBH" colindex=25 tdalign="center" maxlength="36">&nbsp;事件编号&nbsp;</th>
							<th fieldname="SJMJ" colindex=26 tdalign="center" maxlength="36">&nbsp;数据密级&nbsp;</th>
							<th fieldname="SFYX" colindex=27 tdalign="center" maxlength="1">&nbsp;是否有效&nbsp;</th>
							<th fieldname="BZ" colindex=28 tdalign="center" maxlength="4000">&nbsp;备注&nbsp;</th>
							<th fieldname="LRR" colindex=29 tdalign="center" maxlength="30">&nbsp;录入人&nbsp;</th>
							<th fieldname="LRSJ" colindex=30 tdalign="center" maxlength="7">&nbsp;录入时间&nbsp;</th>
							<th fieldname="LRBM" colindex=31 tdalign="center" maxlength="30">&nbsp;录入部门&nbsp;</th>
							<th fieldname="LRBMMC" colindex=32 tdalign="center" maxlength="100">&nbsp;录入部门名称&nbsp;</th>
							<th fieldname="GXR" colindex=33 tdalign="center" maxlength="30">&nbsp;更新人&nbsp;</th>
							<th fieldname="GXSJ" colindex=34 tdalign="center" maxlength="7">&nbsp;更新时间&nbsp;</th>
							<th fieldname="GXBM" colindex=35 tdalign="center" maxlength="30">&nbsp;更新部门&nbsp;</th>
							<th fieldname="GXBMMC" colindex=36 tdalign="center" maxlength="100">&nbsp;更新部门名称&nbsp;</th>
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