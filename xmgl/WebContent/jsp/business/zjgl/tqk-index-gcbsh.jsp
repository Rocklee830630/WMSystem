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
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglTqkGcbController.do";
var opttype = '';
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
		defaultJson.doQueryJsonList(controllername+"?query",data,DT1, null, true);
	});
	//按钮绑定事件(新增)
	$("#btnSuccess").click(function() {
		opttype = 'tongguo';
		if($('input:checkbox[name="gcbMxId"]:checked').size()==0){
			xInfoMsg('请选择要操作的数据！',null);
		    return
		}
		xConfirm("信息确认","您确认通过吗？ ");
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){
			updateTqkGcb(opttype);
		})
		
	});
	//按钮绑定事件(修改)
	$("#btnFail").click(function() {
		opttype = 'tuihui';
		if($('input:checkbox[name="gcbMxId"]:checked').size()==0){
			xInfoMsg('请选择要操作的数据！',null);
		    return
		}
		xConfirm("信息确认","您确认退回吗？ ");
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){
			updateTqkGcb(opttype);
		})
	});
	
	


	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
    });

  //按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 //var t = $("#DT1").getTableRows();
		// if(t<=0)
		 //{
			// xAlert("信息提示","请至少查询出一条记录！");
			 //return;
		 //}
	  	 $(window).manhuaDialog({"title":"导出","type":"text","content":"${pageContext.request.contextPath }/jsp/framework/print/TabListEXP.jsp?tabId=DT1","modal":"3"});
	});
});



function updateTqkGcb(type){
	//xConfirm("信息确认","您确认通过吗？ ");
	var id = '';
	$('input:checkbox[name="gcbMxId"]:checked').each(function(){  
		id += ("'"+$(this).val()+"',");			        
	});
	$.ajax({
		url : controllername+"?updateGcbMxzt&ids="+id+"&type="+type,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(response) {
			$("#btnQuery").click();
			xSuccessMsg("操作成功","");
		}
	});
}
//页面默认参数
function init(){
	getNd();
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,DT1,null,true);
}
var checkId = '';
//点击获取行对象
function tr_click(obj,tabListid){
	//alert(JSON.stringify(obj));
	if(obj.GCBZT!='0'){return}
	var index  = $("#"+tabListid).getSelectedRowIndex();
	var row_dom = $("#"+tabListid+" tbody tr").eq(index);
	//alert($(row_dom).find('td').eq(0).html());
	var box_dom = $(row_dom).find("input[type='checkbox'][name='gcbMxId']");
	var check_flag = $(row_dom).find("input[type='checkbox'][name='gcbMxId']:checked").length;
	if(check_flag==0){
		//alert('false:'+check_flag);
		//$(box_dom).removeAttr('checked');
		$(box_dom).prop('checked',true);
		$('#btnSuccess').attr('disabled',false);
		$('#btnFail').attr('disabled',false);
		checkId += ($(box_dom).val() + ',');
	}else{
		//alert('true:'+check_flag);
		$(box_dom).removeAttr('checked');
		checkId = checkId.replace($(box_dom).val() + ',','');
		if(checkId==''){
			$('#btnSuccess').attr('disabled',true);
			$('#btnFail').attr('disabled',true);	
		}
	}
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


function closeParentCloseFunction(msgc){
	if (typeof(msgc) == "undefined") {
	}else if(msgc=="showMsg"){
		xSuccessMsg('操作成功！',null);
	}
	$("#btnQuery").click();
}


function doCheckBox(obj){
		return "<input type='checkbox' name='gcbMxId' id='gcbMxId' value='"+obj.GCBTQKID+"' disabled/>"
}
function doRandering(obj){
	var showHtml = "";
	showHtml = "<a href='javascript:rowView1(this);' title='查看详细信息'><i class='icon-file showXmxxkInfo'></i></a>";
	return showHtml;
}

function rowView1(){

	if($("#DT1").getSelectedRowIndex()==-1)
	 {
		requireSelectedOneRow();
	    return
	 }
	$("#resultXML").val($("#DT1").getSelectedRow());
	$(window).manhuaDialog({"title":"部门提请款审核>部门提请款明细","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqkbm-view-gcb.jsp?type=detail","modal":"1"});
}

function doRandering_bmze(obj){
	var showHtml = "";
	if(obj.BMZE!=""){
		showHtml = obj.BMZE_SV;
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
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
	var xqzt1=obj.GCBZT;
	if(xqzt1=="0"){
		return '<span class="label label-warning">'+obj.GCBZT_SV+'</span>';
	}else if(xqzt1=="1"){
		var xqzt=obj.TQKZT;
		if(xqzt=="1"){
			return '<span class="label label-danger">'+obj.TQKZT_SV+'</span>';
		}else if(xqzt=="2"){
		  	return '<span class="label label-warning">'+obj.TQKZT_SV+'</span>';
		}else if(xqzt=="3"){
		  	return '<span class="label label-danger">'+obj.TQKZT_SV+'</span>';
		}else if(xqzt=="4"){
		  	return '<span class="label label-success">'+obj.TQKZT_SV+'</span>';
		}else if(xqzt=="5"){
		  	return '<span class="label label-success">'+obj.TQKZT_SV+'</span>';
		}else if(xqzt=="6"){
		 	return '<span class="label label-success">'+obj.TQKZT_SV+'</span>';
		}else if(xqzt=="7"){
			return '<span class="label label-success">'+obj.TQKZT_SV+'</span>';
		}else{
			return obj.TQKZT_SV;
		}
	  	//return '<span class="label label-success">'+obj.GCBZT_SV+'</span>';
	}else if(xqzt1=="2"){
	  	return '<span class="label label-danger">'+obj.GCBZT_SV+'</span>';
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
			<h4 class="title">
				部门提请款审核
				<span class="pull-right">  
				
<%--					<button id="btnPc" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">同批次明细信息</button>--%>
						<button id="btnSuccess" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>审批通过</button>
      					<button id="btnFail" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>退回</button>
      					<button id="btnExpExcel" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">导出</button>
<%--      				title="条件：只有未提交的提请款才可以进行修改"--%>
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
							<select class="span12" id="QND" name="QKNF" fieldname="QKNF" operation="=" kind="dic" src="XMNF"  defaultMemo="年度"></select>
						</td>
						<th width="5%" class="right-border bottom-border text-right">月份</th>
						<td width="10%" class="right-border bottom-border">
							<select  id="GCPC"   class="span12"  name="GCPC" fieldname="GCPC" operation="="  kind="dic" src="YF"  defaultMemo="请选择"></select>
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
	           <table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" pageNum="10">
				<thead>
					<tr>
	                		<th rowspan="2"  name="XH" id="_XH"  colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
	                		<th rowspan="2" fieldname="GCBZT" colindex=2 tdalign="center" CustomFunction="doCheckBox">&nbsp;&nbsp;</th>
	                		<th rowspan="2" fieldname="ID" tdalign="center" colindex=3 CustomFunction="doRandering" noprint="true">&nbsp;&nbsp;</th>
	                		<th rowspan="2" fieldname="GCBZT" colindex=4 tdalign="center" CustomFunction="docolor">&nbsp;状态&nbsp;</th>
	                		<th rowspan="2" fieldname="QKNF" colindex=5 tdalign="center" maxlength="30" >&nbsp;年份&nbsp;</th>
	                		<th rowspan="2" fieldname="GCPC" colindex=6 tdalign="center" maxlength="30" >&nbsp;月份&nbsp;</th>
							<th rowspan="2" fieldname="QKLX" colindex=7 tdalign="center" maxlength="30" >&nbsp;请款类型&nbsp;</th>
							<th colspan="2">&nbsp;部门请款情况&nbsp;</th>
							<th colspan="2">&nbsp;财务审核情况&nbsp;</th>
							<th colspan="2">&nbsp;计财审定情况&nbsp;</th>
							<th rowspan="2" fieldname="BZRQ" colindex=14 tdalign="center">&nbsp;编制日期&nbsp;</th>
	                	</tr>
	                	<tr>
	                		<th fieldname="BMJLS" colindex=8 tdalign="center">&nbsp;明细数&nbsp;</th>
							<th fieldname="BMZE" colindex=9 tdalign="right" CustomFunction="doRandering_bmze">&nbsp;总额(元)&nbsp;</th>
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
</body>
</html>