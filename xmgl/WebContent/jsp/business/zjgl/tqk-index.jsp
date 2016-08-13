<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<title>提请款首页</title>
<%--<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>--%>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglTqkController.do";

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
		$(window).manhuaDialog({"title":"提请款>新增","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqk-add.jsp?type=insert","modal":"1"});
	});
	//按钮绑定事件(修改)
	$("#btnUpdate").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"提请款>修改","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqk-add.jsp?type=update","modal":"1"});
	});
	$("#btnUpdatebyCw").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		var rowjson =	$("#DT1").getSelectedRow();
		$("#resultXML").val(rowjson);
		//alert(JSON.parse(rowjson).QKLX);
		$(window).manhuaDialog({"title":"提请款>审核","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqk-add-cwsh.jsp?type=update&qlx="+JSON.parse(rowjson).QKLX,"modal":"1"});
	});
	$("#btnUpdateHdsz").click(function() {
		if($("#DT1").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"提请款>设置核定数据","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqk-add-hdsj.jsp?type=update","modal":"1"});
	});
	
	//设置审批中 暂
	$("#btnUpdateHdsz01").click(function() {
		var rowindex=$("#DT1").getSelectedRowIndex();//获得选中行的索引
		if(rowindex==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		xConfirm("信息确认","设置为 审批中 状态？（等同于 启动 呈请审批，流程发起信息会缺失 不建议使用！）  "); //6
		
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){

			var rowValue = $("#DT1").getSelectedRow();
			var tempJson = convertJson.string2json1(rowValue);
			//更新记录为提交状态
			$("#QTQKZT").val("5");
			$("#QID").val(tempJson.ID);
			if(tempJson.CWJLS=="0") {alert('没有明细 不能操作');return false;}
		    //生成json串
		    var data = Form2Json.formToJSON(queryFormTqkChange);
		  	//组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		    var success= defaultJson.doUpdateJson(controllername + "?update&opttype=spz", data1, DT1);
		    if(success==true)
			{
				$("#btnQuery").click();
				
			}
		});
	});
	//设置审批通过 暂
	$("#btnUpdateHdsz11").click(function() {
		var rowindex=$("#DT1").getSelectedRowIndex();//获得选中行的索引
		if(rowindex==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		xConfirm("信息确认","设置为 已审批 状态？ （此功能为非正常通道 已跨过会签流程！设置状态） "); //6
		
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){
			var rowValue = $("#DT1").getSelectedRow();
			var tempJson = convertJson.string2json1(rowValue);
			//更新记录为提交状态
			$("#QTQKZT").val("6");
			$("#QID").val(tempJson.ID);
			if(tempJson.CWJLS=="0") {alert('没有明细 不能操作');return false;}
		    //生成json串
		    var data = Form2Json.formToJSON(queryFormTqkChange);
		  	//组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		    var success= defaultJson.doUpdateJson(controllername + "?update&opttype=sptg", data1, DT1);
		    if(success==true)
			{
				$("#btnQuery").click();
				
			}
		});
	});
	
	
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
	 $(window).manhuaDialog({"title":"导出","type":"text","content":"${pageContext.request.contextPath }/jsp/framework/print/TabListEXP.jsp?tabId=DT1","modal":"3"});
		//if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
		//      printTabList("DT1");
		//}
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
    });
	
	//审批
	var sp_btn = $("#sp_btn");
	sp_btn.click(function() 
 			{
			//判断是否选中
			var index1 =	$("#DT1").getSelectedRowIndex();
	 		if(index1<0) 
			{
	 			requireSelectedOneRow();
	 			return;
			}
	 		var rowjson =	$("#DT1").getSelectedRow();
	 		var rowid = JSON.parse(rowjson).ID;

	 		$("#sp_btn").attr("disabled",true);
			$.ajax({
			   type: "POST",
			   url: controllername+"?syncdata",
			   data: "tqkid="+rowid,
			   success: function(msg){
			     var oo = convertJson.string2json1(msg);
			     if(oo){
			     	var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
			 		var ywlx = $("#DT1").getSelectedRowJsonObj().YWLX;
			 		var condition = "";
		 			createSPconf(sjbh,ywlx,condition);
		 			$("#DT1").cancleSelected();
			     }
			   }
			});
		
		
				
		 		
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

	 		var isview = "1";
 		    var isOver = getProIsover(sjbh,ywlx);
 		    if(isOver =="1"){
 		    	isview = "0";
 		    }
	 		
 			 var s = "${pageContext.request.contextPath}/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview="+isview;   
 		  	 $(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});  
     });
	
	 //TQKZT   移除
	 $("#TQKZT option[value='2']").remove();
	 $("#TQKZT option[value='3']").remove();
});

//页面默认参数
function init(){
	getNd();
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,DT1,null,true);
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
	
	$("#btnUpdatebyCw").attr("disabled",true);
	if(obj.TQKZT=="1"){
		$("#btnUpdateHdsz11").attr("disabled",true);
		$("#btnUpdate").attr("disabled",false);
		//$("#sp_btn").attr("disabled",false);
		$("#btnUpdateHdsz").attr("disabled",true);
		$("#sp_btn_view").attr("disabled",true);
		$("#sp_btn").removeAttr("disabled",true);
		if(obj.EVENTSJBH=="5" || obj.EVENTSJBH=="6" || obj.EVENTSJBH=="7"){
			$("#sp_btn").removeAttr("disabled");
		}else{
			$("#sp_btn").attr("disabled",true);
		}
	}else if(obj.TQKZT=="4"){
		$("#btnUpdateHdsz11").attr("disabled",true);
		$("#btnUpdate").attr("disabled",true);
		$("#btnUpdateHdsz").attr("disabled",true);
		$("#btnUpdatebyCw").attr("disabled",false);
		$("#sp_btn_view").attr("disabled",true);
		$("#sp_btn").removeAttr("disabled",true);
		if(obj.CWZE!=""){
			$("#sp_btn").attr("disabled",false);
		}
	}else if(obj.TQKZT=="5"){
		$("#btnUpdateHdsz11").attr("disabled",false);
		$("#btnUpdate").attr("disabled",true);
		$("#sp_btn").attr("disabled",true);
		$("#btnUpdateHdsz").attr("disabled",true);
		$("#sp_btn_view").attr("disabled",false);
		
		if(obj.EVENTSJBH=="5" || obj.EVENTSJBH=="6" || obj.EVENTSJBH=="7"){
			$("#btnUpdate").attr("disabled",false);
			$("#sp_btn").removeAttr("disabled");
		}else{
			$("#sp_btn").attr("disabled",true);
		}
		
	}else if(obj.TQKZT=="6"){
		$("#btnUpdateHdsz11").attr("disabled",true);
		$("#btnUpdate").attr("disabled",true);
		$("#btnUpdateHdsz").attr("disabled",false);
		$("#sp_btn_view").attr("disabled",false);
		$("#sp_btn").attr("disabled",true);
	}else if(obj.TQKZT=="7"){
		$("#btnUpdateHdsz11").attr("disabled",true);
		$("#btnUpdate").attr("disabled",true);
		$("#btnUpdateHdsz").attr("disabled",true);
		$("#sp_btn_view").attr("disabled",false);
		$("#sp_btn").attr("disabled",true);
	}
	
	
	if(obj.CWZE=="") {
		//$("#sp_btn").attr("disabled",false);
	//} else {
		$("#sp_btn").attr("disabled",true);
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

function closeParentCloseFunction(){
	$("#btnQuery").click();
}

//详细信息
function rowView(index){
	$("#DT1").setSelect(index);
	if($("#DT1").getSelectedRowIndex()==-1)
	 {
		requireSelectedOneRow();
	    return
	 }
	$("#resultXML").val($("#DT1").getSelectedRow());
	$(window).manhuaDialog({"title":"提请款>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqk-view.jsp?type=detail","modal":"1"});
}
function rowView1(t){
	if($("#DT1").getSelectedRowIndex()==-1)
	 {
		requireSelectedOneRow();
	    return
	 }
	$("#resultXML").val($("#DT1").getSelectedRow());
	var rowValue = $("#DT1").getSelectedRow();
	var tempJson = convertJson.string2json1(rowValue);
	var idvar = tempJson.ID;
	$(window).manhuaDialog({"title":"提请款>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqk-view.jsp?type=detail","modal":"1"});
}

function doRandering(obj){
	var showHtml = "";
	showHtml = "<a href='javascript:rowView1(this);' title='查看详细信息'><i class='icon-file showXmxxkInfo'></i></a>";
	return showHtml;
}

function doRandering_cwze(obj){
	var showHtml = "";
	if(obj.CWZE!=""){
		showHtml = obj.CWZE_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_jcze(obj){
	var showHtml = "";
	if(obj.JCZE!=""){
		showHtml = obj.JCZE_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_CWJLS(obj){
	var showHtml = "";
	if(obj.CWJLS!="0"){
		showHtml = obj.CWJLS;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}
function doRandering_JCJLS(obj){
	var showHtml = "";
	if(obj.JCJLS!="0"){
		showHtml = obj.JCJLS;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}

//状态颜色判断
function docolor(obj)
{
<%--	TQKZT	提请款状态--%>
<%--	1	未提交		新建--%>
<%--	2	审核中		提交领导审核--%>
<%--	3	已退回		部门领导退回--%>
<%--	4	待处理		部门领导通过--%>
<%--	5	审批中		呈请审批--%>
<%--	6	已审批		审批通过--%>
<%--	7	已拔付--%>
	var xqzt=obj.TQKZT;
	if(xqzt=="1"){
		return '<span class="label label-danger">'+obj.TQKZT_SV+'</span>';
	}else if(xqzt=="2"){
	  	return '<span class="label label-warning">'+obj.TQKZT_SV+'</span>';
	}else if(xqzt=="3"){
	  	return '<span class="label label-warning">'+obj.TQKZT_SV+'</span>';
	}else if(xqzt=="4"){
	  	return '<span class="label label-danger">'+obj.TQKZT_SV+'</span>';
	}else if(xqzt=="5"){
		var zt = obj.EVENTSJBH;
		if(zt=='0'){
			zt = obj.TQKZT_SV;
		}
	 	return '<span class="label label-success">'+obj.EVENTSJBH_SV+'</span>';
	}else if(xqzt=="6"){
	 	return '<span class="label label-warning">'+obj.TQKZT_SV+'</span>';
	}else if(xqzt=="7"){
	 	return '<span class="label label-success">'+obj.TQKZT_SV+'</span>';
	}else{
		return obj.TQKZT_SV;
	}
}

function gengxinchaxun()
{
		var row_index=$("#DT1").getSelectedRowIndex();
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		var tempJson = convertJson.string2json1(data);
		var a=$("#DT1").getCurrentpagenum();
		tempJson.pages.currentpagenum=a;
		data = JSON.stringify(tempJson);
		defaultJson.doQueryJsonList(controllername+"?query",data,DT1);
		$("#DT1").setSelect(row_index);
		var selObj = $("#DT1").getSelectedRowJsonObj();//获得选中行的索引
		tr_click(selObj, gcsxList);
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
				提请款管理
				<span class="pull-right">  
				<button id="btnUpdateHdsz01" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">设置审批中</button>
				<app:oPerm url="zjgl/gcZjglTqkController.do?update&opttype=sptg">
				<button id="btnUpdateHdsz11" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">通过</button>
				</app:oPerm>
				&nbsp;&nbsp;
				
				<app:oPerm url="jsp/business/zjgl/tqk-add.jsp?type=insert">
					<button id="btnInsert" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">新增</button>
					</app:oPerm>
				<app:oPerm url="jsp/business/zjgl/tqk-add.jsp?type=update">
      				<button id="btnUpdate" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" >修改</button>
<%--      				title="条件：只有未提交的提请款才可以进行修改"--%>
      				</app:oPerm>
      			<app:oPerm url="jsp/business/zjgl/tqk-add-cwsh.jsp?type=update">
      				<button id="btnUpdatebyCw" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" >审核</button>
<%--      				title="条件：只有待处理的提请款才可以进行审核"--%>
      				</app:oPerm>
				<app:oPerm url="jsp/business/zjgl/tqk-add.jsp?type=sp">
      				<button id="sp_btn" class="btn"  type="button" >呈请审批</button>
<%--      				title="条件：只有待处理或审批退回的提请款才可以呈请审批"--%>
      			</app:oPerm>
      			<app:oPerm url="/jsp/framework/common/aplink/defaultArchivePage.jsp?ywlx=700204">
      				<button id="sp_btn_view" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" >审批信息</button>
<%--      				title="条件：只有审批中或已审批后的提请款才可以查看审批信息"--%>
      			</app:oPerm>
				<app:oPerm url="jsp/business/zjgl/tqk-add-hdsj.jsp?type=update">
				<button id="btnUpdateHdsz" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" >审定设置</button>
<%--				title="条件：只有已审批的提请款才可以进行审定设置"--%>
				</app:oPerm>
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
						<th width="5%" class="right-border bottom-border text-right">年度</th>
						<td width="10%" class="right-border bottom-border">
							<select  id="QND"   placeholder="必填" check-type="required" class="span12"  name="QKNF" fieldname="t.QKNF"  operation="=" kind="dic" src="XMNF" defaultMemo="-请选择-">
						</td>
						<th width="5%" class="right-border bottom-border text-right">月份</th>
						<td width="10%" class="right-border bottom-border">
							<select  id="YF"   class="span12"  name="YF" fieldname="YF" operation="="  kind="dic" src="YF"  defaultMemo="-请选择-">
						</td>
						<th width="5%" class="right-border bottom-border text-right">请款类型</th>
						<td width="10%" class="right-border bottom-border">
							<select id="QKLX" class="span12"  name="QKLX" fieldname="QKLX"  operation="=" kind="dic" src="QKLX"  defaultMemo="-请选择-">
						</td>
						<th width="5%" class="right-border bottom-border text-right">状态</th>
						<td width="10%" class="right-border bottom-border">
							<select  style="width:100%;" id="TQKZT"   class="span12"  name="TQKZT" fieldname="TQKZT" operation="="  kind="dic" src="TQKZT"  defaultMemo="-请选择-">
						</td>
						<th width="5%" class="right-border bottom-border text-right">融资主体</th>
						<td width="15%" class="right-border bottom-border">
							<select  style="width:100%;" id="RZZT"   class="span12"  name="RZZT" fieldname="RZZT" operation="="  kind="dic" src="T#FS_COMMON_DICT: DICT_ID as c:DICT_NAME as t:DICT_CATEGORY='zjgl_tqk_rzzt'"  defaultMemo="-请选择-">
						</td>
						
						
<%--						<th width="8%" class="right-border bottom-border text-right">请款类型</th>--%>
<%--						<td width="17%" class="right-border bottom-border">--%>
<%--							<select id="QKLX"   class="span12"  name="QKLX" fieldname="QKLX"  operation="=" kind="dic" src="QKLX"  defaultMemo="-请选择-">--%>
<%--						</td>--%>
						
						
			            <td class="text-left bottom-border text-right">
	                        <button id="btnQuery" class="btn btn-link"  type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-search"></i>查询</button>
           					<button id="btnClear" class="btn btn-link" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-trash"></i>清空</button>
			            </td>							
					</tr>
				</table>
			</form>
			<div style="height:5px;"> </div>	
			<div class="overFlowX">
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single"  pageNum="18" printFileName="提请款管理">
	                <thead>
	                	<tr>
	                		<th rowspan="2"  name="XH" id="_XH" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
	                		<th rowspan="2" fieldname="ID" tdalign="center" colindex=2 CustomFunction="doRandering" noprint="true">&nbsp;&nbsp;</th>
	                		<th rowspan="2" fieldname="TQKZT" colindex=3 tdalign="center" CustomFunction="docolor">&nbsp;状态&nbsp;</th>
	                		<th rowspan="2" fieldname="QKNF" colindex=4 tdalign="center" maxlength="30" >&nbsp;年份&nbsp;</th>
	                		<th rowspan="2" fieldname="YF" colindex=5 tdalign="center" maxlength="30" >&nbsp;月份&nbsp;</th>
	                		<th rowspan="2" fieldname="GCPC" colindex=6 tdalign="center" maxlength="30" >&nbsp;批次&nbsp;</th>
							<th rowspan="2" fieldname="QKLX" colindex=7 tdalign="center" maxlength="30" >&nbsp;请款类型&nbsp;</th>
							<th rowspan="2" fieldname="DICT_NAME" colindex=8 tdalign="center" maxlength="30" >&nbsp;融资主体&nbsp;</th>
							<th rowspan="2" fieldname="QKMC" colindex=9 tdalign="center" maxlength="30" >&nbsp;请款名称&nbsp;</th>
							<th colspan="2">&nbsp;财务审核情况&nbsp;</th>
							<th colspan="2">&nbsp;计财审定情况&nbsp;</th>
							<th rowspan="2" fieldname="BZRQ" colindex=14 tdalign="center">&nbsp;编制日期&nbsp;</th>
							<th rowspan="2" fieldname="SQDW" colindex=15 tdalign="center">&nbsp;申请部门&nbsp;</th>
							<th rowspan="2" fieldname="CWBLRID" colindex=16 tdalign="center">&nbsp;申请人&nbsp;</th>
	                	</tr>
	                	<tr>
							<th fieldname="CWJLS" colindex=10 tdalign="center" CustomFunction="doRandering_CWJLS">&nbsp;明细数&nbsp;</th>
							<th fieldname="CWZE" colindex=11 tdalign="right" CustomFunction="doRandering_cwze">&nbsp;总额(元)&nbsp;</th>
							<th fieldname="JCJLS" colindex=12 tdalign="center" CustomFunction="doRandering_JCJLS">&nbsp;明细数&nbsp;</th>
							<th fieldname="JCZE" colindex=13 tdalign="right" CustomFunction="doRandering_jcze">&nbsp;总额(元)&nbsp;</th>
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
 <form method="post" id="queryFormTqkChange">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border">
							<input type="hidden" id="QID" name="ID"  fieldname="ID" >
							<input type="hidden" id="QTQKZT" name="TQKZT"  fieldname="TQKZT" >
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
				</table>
	</form>
</body>
</html>