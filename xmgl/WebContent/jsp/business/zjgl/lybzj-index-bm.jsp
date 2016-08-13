
<%@page import="com.ccthanking.framework.common.DBUtil"%>
<%@page import="java.sql.Connection"%><!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<title>履约保证金管理首页</title>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
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
		defaultJson.doQueryJsonList(controllername+"?query&opttype=gcb",data,DT1);
	});
	
	//按钮绑定事件(通过)
	$("#sp_btn1").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		
		xConfirm("信息确认","确认设置审批状态为 \"已审批\"？ "); //2
		
		$('#ConfirmYesButton').one("click",function(){
			var rowValue = $("#DT1").getSelectedRow();
			var tempJson = convertJson.string2json1(rowValue);

			$("#FHQK").val("2");
	   		$("#ID").val(tempJson.ID);
	   		//生成json串
	   		var data = Form2Json.formToJSON(queryFormFHQukck);
		  	//组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  	//调用ajax插入
	   		var success = defaultJson.doUpdateJson(controllername + "?update&opttype=fh", data1,DT1);
		    if(success)
			{
				//$("#btnQuery").click();
			}
		});
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
        getNd();
    });

	$("a[id^='view_detail_']").bind("click", function(){
    	rowView1();
	});
	
	//审批
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
	 			createSPconf(sjbh,ywlx,condition);
	 			 $("#DT1").cancleSelected();
			 });
	//历史项目审批-----------------sjl---1.24---
	var sp_btn = $("#sp_ls_btn");
	sp_btn.click(function() 
 			{
				var index1 =$("#DT1").getSelectedRowIndex();
		 		if(index1<0) 
				{
		 			requireSelectedOneRow();
		 			return;
				}
		 		var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
		 		var ywlx = $("#DT1").getSelectedRowJsonObj().YWLX;
		 		var condition = "";
	 			createSPconf(sjbh,ywlx,condition);
	 			 $("#DT1").cancleSelected();
			 });
	
	 //查看审批信息
	 $("#sp_btn_view").click(function() {
		 var index1 =	$("#DT1").getSelectedRowIndex();
	 		if(index1<0) 
			{
	 			requireSelectedOneRow();
	 			return;
			}
	 		var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
	 		var ywlx = $("#DT1").getSelectedRowJsonObj().YWLX;
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
	defaultJson.doQueryJsonList(controllername+"?query&opttype=gcb",data,DT1, null, true);
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
	if(obj.FHQK=="0"){
		$("#sp_btn1").attr("disabled",true);
		if(obj.JGYSSJ!="" && obj.TBRQ!=""){
			
			$("#sp_btn").removeAttr("disabled");
		}else{
			$("#sp_btn").attr("disabled",true);
		}
		$("#sp_btn_view").attr("disabled",true);
	}else if(obj.FHQK=="1"){
		$("#sp_btn1").removeAttr("disabled");
		$("#sp_btn_view").removeAttr("disabled");
		if(obj.EVENTSJBH=="5" || obj.EVENTSJBH=="6" || obj.EVENTSJBH=="7"){
			$("#sp_btn").removeAttr("disabled");
			$("#sp_ls_btn").removeAttr("disabled");
		}else{
			$("#sp_btn").attr("disabled",true);
			$("#sp_ls_btn").attr("disabled",true);
		}
	}else if(obj.FHQK=="2"){
		$("#sp_btn1").attr("disabled",true);
		$("#sp_btn").attr("disabled",true);
		$("#sp_ls_btn").attr("disabled",true);
		$("#sp_btn_view").removeAttr("disabled");
	}else if(obj.FHQK=="6"){
		$("#sp_btn1").attr("disabled",true);
		$("#sp_btn").attr("disabled",true);
		$("#sp_ls_btn").attr("disabled",true);
		$("#sp_btn_view").removeAttr("disabled");
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
	$("#btnQuery").click();
}

//显示控制
function doRandering(obj){
	var showHtml = "";
	showHtml = "<a href='javascript:rowView1(this);' title='查看履约保证金信息'><i class='icon-file showXmxxkInfo' jhsjid='"+obj.ID+"'></i></a>";
	return showHtml;
}
function doRandering_dateJGYSSJ(obj){
	var showHtml = "";
	if(obj.JGYSSJ!=""){
		showHtml = obj.JGYSSJ;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_dateTBRQ(obj){
	var showHtml = "";
	if(obj.TBRQ!=""){
		showHtml = obj.TBRQ;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
//状态颜色判断
function docolor(obj)
{
<%--	SHZT	审核状态	SHZT--%>
<%--	0	已交纳	yjn--%>
<%--	1	审批中	spz--%>
<%--	2	已审批	ysp--%>
<%--	6	已返还	yfh--%>
	var xqzt=obj.FHQK;
	if(xqzt=="0"){
		if(obj.JNFS=="BH"){
			return '<span class="label label-danger">已办理</span>';
		}else{
			return '<span class="label label-danger">'+obj.FHQK_SV+'</span>';
		}
	}else if(xqzt=="1"){
		if(obj.EVENTSJBH=="5" || obj.EVENTSJBH=="6" || obj.EVENTSJBH=="7"){
			return '<span class="label label-danger">'+obj.EVENTSJBH_SV+'</span>';
		}else{
			return '<span class="label label-warning">'+obj.FHQK_SV+'</span>';
		}
	}else if(xqzt=="2"){
	  	return '<span class="label label-success">'+obj.FHQK_SV+'</span>';
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
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">
				履约保证金管理
				<span class="pull-right">
				<app:oPerm url="jsp/business/zjgl/lybzj-index-bm.jsp">
					<button id="sp_btn1" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">通过</button>
					&nbsp;&nbsp;
					</app:oPerm>
				<app:oPerm url="jsp/framework/common/aplink/defaultArchivePage.jsp">
      				<button id="sp_btn" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">呈请审批</button>
<%--      				 title="条件：已交纳且竣工验收和 结算提报日期不为空"--%>
      				</app:oPerm>
				<app:oPerm url="/jsp/framework/common/aplink/defaultArchivePage.jsp?ywlx=700201">
      				<button id="sp_btn_view" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">审批信息</button>
      				</app:oPerm>
				<app:oPerm url="jsp/framework/print/TabListEXP.jsp">
      				<button id="btnExpExcel" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">导出</button>
      				</app:oPerm>
      			<button id="sp_ls_btn" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">历史项目呈请</button>
				</span>
      			
			</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border">
						</TD>
					</TR>
					<tr>
						<th width="5%" class="right-border bottom-border text-right">交纳年份</th>
						<td width="7%" class="right-border bottom-border">
							<select class="span12" type="date" fieldtype="year" fieldformat="yyyy" id="QND" name="JNRQ" fieldname="JNRQ" operation="=" kind="dic" src="T#GC_ZJGL_LYBZJ: distinct TO_CHAR(jnrq,'yyyy') as JNRQ:TO_CHAR(jnrq,'yyyy') as x:SFYX='1' ORDER BY JNRQ ASC">
							
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
						<th width="5%" class="right-border bottom-border text-right">状态</th>
						<td class="right-border bottom-border" width="8%">
							<select class="span12" fieldtype="text" id="QFHQK" name="FHQK" fieldname="FHQK" operation="=" kind="dic" src="SHZT"  defaultMemo="-全部-">
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
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single"  pageNum="18" printFileName="履约保证金管理">
	                <thead>
	                	<tr>
	                		<th  name="XH" id="_XH" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
	                		<th fieldname="ID" tdalign="center" CustomFunction="doRandering" noprint="true">&nbsp;&nbsp;</th>
							<th fieldname="FHQK" colindex=8 tdalign="center" CustomFunction="docolor">&nbsp;状态&nbsp;</th>
	                		<th fieldname="ZTBMC" maxlength="20"  CustomFunction="doZTBXX"> &nbsp;招投标名称&nbsp; </th>
	                		<th fieldname="ZBLX" maxlength="20"  >
								&nbsp;招标类型&nbsp;
							</th>
							<th fieldname="ZBFS" maxlength="20"  >
								&nbsp;招标方式&nbsp;
							</th>
							<th fieldname="ZZBJ" maxlength="20"  >
								&nbsp;中标价&nbsp;
							</th>
							<th fieldname="DSFJGID" maxlength="20" CustomFunction="doZbdw"> &nbsp;中标单位&nbsp; </th>
	                		<th fieldname="HTMC" colindex=2 tdalign="left" maxlength="20">&nbsp;合同名称&nbsp;</th>
							<th fieldname="HTBM" colindex=3 tdalign="center" maxlength="20">&nbsp;合同编码&nbsp;</th>
							<th fieldname="JNDW" colindex=17 tdalign="left" maxlength="30" >&nbsp;交纳单位&nbsp;</th>
							<th fieldname="JNFS" colindex=5 tdalign="center" CustomFunction="do_bhlx">&nbsp;交纳方式&nbsp;</th>
							<th fieldname="JE" colindex=6 tdalign="right">&nbsp;金额(元)&nbsp;</th>
							<th fieldname="JNRQ" colindex=7 tdalign="center" CustomFunction="do_view">&nbsp;交纳日期&nbsp;</th>
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
		<!-- <input type="hidden" name="txtFilter" order="desc" fieldname="JGYSSJ" id="txtFilter"/> -->
		<input type="hidden" name="txtFilter" order="desc" fieldname="t.LRSJ" id="txtFilter"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
</div>

<form method="post" id="queryFormFHQukck">
	<!--可以再此处加入hidden域作为过滤条件 -->
	<TR style="display: none;">
		<TD class="right-border bottom-border"></TD>
		<TD class="right-border bottom-border">
			<input type="hidden" id="ID" fieldname="ID" name="ID"/>
			<input type="hidden" id="FHQK" name="FHQK"  fieldname="FHQK" >
		</TD>
	</TR>	
</form>
</body>
</html>