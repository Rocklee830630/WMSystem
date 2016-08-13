
<%@page import="com.ccthanking.framework.common.DBUtil"%>
<%@page import="java.sql.Connection"%><!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>履约保证金首页</title>
		<script
			src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
		<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglLybzjController.do";


//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	setPageHeight();
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
		$(window).manhuaDialog({"title":"履约保证金>新增","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/lybzj-add.jsp?type=insert","modal":"2"});
	});
	$("#btnInsertReturn").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"履约保证金>返还","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/lybzj-update-fh.jsp?type=update","modal":"2"});
	});
	//按钮绑定事件(修改)
	$("#btnUpdate").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"履约保证金>修改","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/lybzj-add.jsp?type=update","modal":"2"});
	});
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 $(window).manhuaDialog({"title":"导出","type":"text","content":"${pageContext.request.contextPath }/jsp/framework/print/TabListEXP.jsp?tabId=DT1","modal":"3"});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
    });

	//删除
    $("#btnDelete").click(function() {
    	var rowinx = $("#DT1").getSelectedRowIndex();
    	if(rowinx==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		xConfirm("信息确认","您确定删除所选履约保证金信息吗？ ");
		
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){
			$("#btnDelete").attr("disabled", true);
			 var rowJson = $("#DT1").getSelectedRow();
			 var data1 = defaultJson.packSaveJson(rowJson);
			 var flag = defaultJson.doDeleteJson(controllername+"?delete", data1, null);
			 if(flag){
				 $("#DT1").removeResult(rowinx);
				 xAlert("信息提示","删除成功！");
			 }
		});
	});
    
    $("a[id^='view_detail_']").bind("click", function(){
    	rowView1();
	});
    
    var sp_btn = $("#sp_btn");
	sp_btn.click(function() 
 			{
				var index1 =	$("#DT1").getSelectedRowIndex();
		 		if(index1<0) 
				{
		 			requireSelectedOneRow();
		 			return;
				}
		 		var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
		 		var ywlx = $("#DT1").getSelectedRowJsonObj().YWLX;
		 		var condition = "";
		 		var obj = $("#DT1").getSelectedRowJsonObj();
		 			 var s = "${pageContext.request.contextPath}/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview=1";   
		 		  	 $(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});  
			 });
	
});

//页面默认参数
function init(){
	getNd();
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,DT1, null, true);
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
	<%--	SHZT	审核状态	SHZT--%>
	<%--	0	已交纳	yjn--%>
	<%--	1	审批中	spz--%>
	<%--	2	已审批	ysp--%>
	<%--	6	已返还	yfh--%>
	//alert(JSON.stringify(obj));
	if(obj.FHQK=="6"){
		$("#btnInsertReturn").attr("disabled",true);
		$("#btnUpdate").attr("disabled",true);
		$("#sp_btn").attr("disabled",false);
		$("#btnDelete").attr("disabled",true);
	}else if(obj.FHQK=="0"){
		$("#sp_btn").attr("disabled",true);
		$("#btnInsertReturn").attr("disabled",true);
		$("#btnUpdate").removeAttr("disabled");
		$("#btnDelete").removeAttr("disabled");
	}else if(obj.FHQK=="2"){
		$("#btnInsertReturn").removeAttr("disabled");
		$("#btnUpdate").attr("disabled",true);
		$("#sp_btn").attr("disabled",false);
		$("#btnDelete").attr("disabled",true);
	}else if(obj.FHQK=="1"){
		$("#btnInsertReturn").attr("disabled",true);
		$("#sp_btn").attr("disabled",false);
		if(obj.EVENTSJBH=="5" || obj.EVENTSJBH=="6" || obj.EVENTSJBH=="7"){
			$("#btnUpdate").removeAttr("disabled");
			$("#btnDelete").removeAttr("disabled");
		}else{
			$("#btnUpdate").attr("disabled",true);
			$("#btnDelete").attr("disabled",true);
		}
	}
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
function rowView1(t){
	if($("#DT1").getSelectedRowIndex()==-1)
	 {
		requireSelectedOneRow();
	    return
	 }
	var rowValue = $("#DT1").getSelectedRow();
	var tempJson = convertJson.string2json1(rowValue);
	var shidvar = tempJson.SJBH;
	$(window).manhuaDialog({"title":"履约保证金>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/lybzj-view.jsp?type=detail&sjbh="+shidvar,"modal":"2"});
}
function closeParentCloseFunction(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	var tempJson = convertJson.string2json1(data);
	var a=$("#DT1").getCurrentpagenum();	//获取当前页码
	tempJson.pages.currentpagenum=a;		//替换json的页码
	data = JSON.stringify(tempJson);
	defaultJson.doQueryJsonList(controllername+"?query",data,DT1,null,false);
}
function doRandering(obj){
	var showHtml = "";
	showHtml = "<a href='javascript:rowView1(this);' title='查看履约保证金信息'><i class='icon-file showXmxxkInfo' jhsjid='"+obj.ID+"'></i></a>";
	return showHtml;
}
//状态颜色判断
function docolor(obj)
{
	var xqzt=obj.FHQK;
	if(xqzt=="0"){
		if(obj.JNFS=="BH"){
			return '<span class="label label-danger">已办理</span>';
		}else{
			return '<span class="label label-danger">'+obj.FHQK_SV+'</span>';
		}
	}else if(xqzt=="1"){
		if(obj.EVENTSJBH=="5" || obj.EVENTSJBH=="6" || obj.EVENTSJBH=="7"){
			return '<span class="label label-success">'+obj.EVENTSJBH_SV+'</span>';
		}else{
			return '<span class="label label-success">'+obj.FHQK_SV+'</span>';
		}
	}else if(xqzt=="2"){
	  	return '<span class="label label-danger">'+obj.FHQK_SV+'</span>';
	}else if(xqzt=="6"){
	 	return '<span class="label label-info">'+obj.FHQK_SV+'</span>';
	}
	
}

function do_view(obj){
	if(obj.JNFS=="BH"){
		return '<span title="'+obj.JNRQ_SV+'">'+obj.BHYXQS_SV+"至"+obj.BHYXQZ_SV+'</span>';
	}else{
		return obj.JNRQ_SV;
	}
}
function do_bhlx(obj){
	if(obj.JNFS=="BH"){
		if(obj.BHLX!=''){
			return obj.BHLX_SV+""+obj.JNFS_SV;
		}else{
			return obj.JNFS_SV;
		}
	}else{
		return obj.JNFS_SV;
	}
}
function doBD(obj){
	if(obj.BDMC==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}
function doFHRQ(obj){
	if(obj.FHRQ==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.FHRQ;
	}
}
//中标单位
function doZbdw(obj){
	if(obj.DSFJGID==''){
		return obj.DSFJGID;
	}else{
		return obj.DSFJGID_SV;
	}
}
//招投标详细页
function doZTBXX(obj){
	var showHtml = "";
	showHtml = "<a href='javascript:void(0)' onclick='openInfoCard()' title='招投标信息卡'>"+obj.ZTBMC+"</a>";
	return showHtml;
}
//-------------------------------
//-打开信息卡页面
//-------------------------------
function openInfoCard(){
	var index = $(event.target).closest("tr").index();
	$("#DT1").cancleSelected();
	$("#DT1").setSelect(index);
	var json = convertJson.string2json1($("#DT1").getSelectedRow());
	var id = json.GC_ZTB_SJ_ID;
	var zblx = json.ZBLX;
	$(window).manhuaDialog({"title":"招投标管理>查看招标信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/ztbgl_xx.jsp?xx="+id+"&zblx="+zblx+"&cxlx=2","modal":"1"});
}
</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<p></p>
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						履约保证金
						<span class="pull-right"> <app:oPerm
								url="jsp/business/zjgl/lybzj-index.jsp?type=insert">
								<button id="btnInsert" class="btn" type="button"
									style="font-family: SimYou, Microsoft YaHei; font-weight: bold;">
									新增登记
								</button>
								<button id="btnDelete" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled title="">删除</button>
							</app:oPerm> <app:oPerm
								url="jsp/business/zjgl/lybzj-update-fh.jsp?type=update">
								<button id="btnInsertReturn" class="btn" type="button"
									style="font-family: SimYou, Microsoft YaHei; font-weight: bold;" >
<%--									title="条件：只有已审批的履约保证金才可以进行新增返还"--%>
									新增返还
								</button>
								<button id="sp_btn" class="btn" type="button" >
<%--								title="条件：只有已返还的履约保证金才可以查看审批信息"--%>
									审批信息
								</button>
							</app:oPerm> <app:oPerm url="jsp/business/zjgl/lybzj-add.jsp?type=update">
								<button id="btnUpdate" class="btn" type="button"
									style="font-family: SimYou, Microsoft YaHei; font-weight: bold;">
<%--									 title="条件：只有已交纳的履约保证金才可以维护"--%>
									维护
								</button>
							</app:oPerm> <app:oPerm url="jsp/framework/print/TabListEXP.jsp?tabId=DT1">
								<button id="btnExpExcel" class="btn" type="button"
									style="font-family: SimYou, Microsoft YaHei; font-weight: bold;">
									导出
								</button>
							</app:oPerm> </span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<%--							<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>--%>
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
							<tr>
								<th width="5%" class="right-border bottom-border text-right">
									交纳年份
								</th>
								<td width="10%" class="right-border bottom-border">
									<select class="span12" type="date" fieldtype="year"
										fieldformat="yyyy" id="QND" name="JNRQ" fieldname="JNRQ"
										operation="="  kind="dic" src="T#GC_ZJGL_LYBZJ: distinct TO_CHAR(jnrq,'yyyy') as JNRQ:TO_CHAR(jnrq,'yyyy') as x:SFYX='1' ORDER BY JNRQ ASC">
										</select>
										
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									招投标名称 
								</th>
									<td width="20%" class="right-border bottom-border">
									<input class="span12" type="text" id="ZTBMC" name="ZTBMC"
										fieldname="ZTB.ZTBMC" operation="like" maxlength="100">
								</td>
									<th width="5%" class="right-border bottom-border text-right">
									招标类型
								</th>
								<td width="10%" class=" right-border bottom-border">
									<select class="span12 4characters" id="ZBLX" name="ZBLX"
										fieldname="x.ZBLX" defaultMemo="全部" kind="dic" src="ZBLX"
										operation="=">
									</select>
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									状态
								</th>
								<td class="right-border bottom-border" width="10%">
									<select class="span12" fieldtype="text" id="QFHQK" name="FHQK"
										fieldname="FHQK" operation="=" kind="dic" src="SHZT"
										defaultMemo="-全部-"></select>
								</td>
								<td class="text-left bottom-border text-right">
									<button id="btnQuery" class="btn btn-link" type="button"
										style="font-family: SimYou, Microsoft YaHei; font-weight: bold;">
										<i class="icon-search"></i>查询
									</button>
									<button id="btnClear" class="btn btn-link" type="button"
										style="font-family: SimYou, Microsoft YaHei; font-weight: bold;">
										<i class="icon-trash"></i>清空
									</button>
								</td>
							</tr>
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
						<table width="100%" class="table-hover table-activeTd B-table"
							id="DT1" type="single" pageNum="18" printFileName="履约保证金">
							<thead>
								<tr>
									<th name="XH" id="_XH" colindex=1 tdalign="center">
										&nbsp;#&nbsp;
									</th>
									<th fieldname="ID" tdalign="center" colindex=2
										CustomFunction="doRandering" noprint="true">
										&nbsp;&nbsp;
									</th>
									<th fieldname="FHQK" colindex=3 tdalign="center"
										CustomFunction="docolor">
										&nbsp;状态&nbsp;
									</th>
									
									<th fieldname="ZTBMC" maxlength="20" CustomFunction="doZTBXX"  >
										&nbsp;招投标名称&nbsp;
									</th>
									<th fieldname="ZBLX" maxlength="20"  >
										&nbsp;招标类型&nbsp;
									</th>
									<th fieldname="ZBFS" maxlength="20"  >
										&nbsp;招标方式&nbsp;
									</th>
									<th fieldname="ZZBJ" maxlength="20"  >
										&nbsp;中标价&nbsp;
									</th>
									<th fieldname="DSFJGID" maxlength="20" CustomFunction="doZbdw">
										&nbsp;中标单位&nbsp;
									</th>
									<th fieldname="JNFS" colindex=6 tdalign="center" CustomFunction="do_bhlx">
										&nbsp;交纳方式&nbsp;
									</th>
									<th fieldname="JE" colindex=8 tdalign="right">
										&nbsp;金额(元)&nbsp;
									</th>
									<th fieldname="JNRQ" colindex=9 tdalign="center" CustomFunction="do_view">
										&nbsp;交纳日期&nbsp;
									</th>
									<th fieldname="FHRQ" colindex=10 tdalign="center" CustomFunction="doFHRQ">
										&nbsp;返还日期&nbsp;
									</th>
									<th fieldname="HTMC" colindex=11 maxlength="20" tdalign="left">
										&nbsp;合同名称&nbsp;
									</th>
									<th fieldname="HTBM" colindex=12 tdalign="center">
										&nbsp;合同编码&nbsp;
									</th>
									<th fieldname="JNDW" colindex=13 tdalign="left"
										maxlength="30">
										&nbsp;交纳单位&nbsp;
									</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" />
				<input type="hidden" name="txtXML" />
				<input type="hidden" name="txtFilter" order="desc"
					fieldname="t.gxsj" id="txtFilter" />
				<%--				<input type="hidden" name="txtFilter" order="desc" fieldname="ID" id="txtFilter"/>--%>
				<input type="hidden" name="resultXML" id="resultXML" />
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData" />
				<input type="hidden" name="queryResult" id="queryResult" />
			</FORM>
		</div>
	</body>
</html>