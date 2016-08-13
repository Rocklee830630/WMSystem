<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.common.QuerySet"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<app:base />
<title></title>
<% 
	String yw_code=request.getParameter("yw_code");
%>
<script type="text/javascript" charset="utf-8">
var controllername = "${pageContext.request.contextPath}/clzx/clzxManageControllor.do";
var ywCode = "<%=yw_code %>";
var getHeight=getDivStyleHeight()-30;
//计算本页表格分页数
function setPageHeight() {
	var height = getHeight-pageTopHeight-pageTitle-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(Math.abs(height)/pageTableOne,10);
	$("#tcjhList2").attr("pageNum", pageNum);
}
$(function(){
	setPageHeight();
	queryList();
});

function queryList() {
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,tcjhList1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryList&yw_code="+ywCode,data,tcjhList1);
}
function queryListLS() {
	var data = combineQuery.getQueryCombineData(queryForm2,frmPost,tcjhList2);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryListLS&yw_code="+ywCode,data,tcjhList2);
}
$(function() {
	$("#btnFk").click(function(){
		var len = $("[name=subBox]:checkbox:checked").length;
		if(len >=2) {
			xInfoMsg("请选择一条操作的数据",""); 
			return;
		}
		if($("#tcjhList1").getSelectedRowIndex()==-1){
	    	requireSelectedOneRow();
	        return;
		} else {
	    	$("#resultXML").val($("#tcjhList1").getSelectedRow());
	    	var tempJson = $("#tcjhList1").getSelectedRowJsonObj();
	    	openJhfkPage(tempJson.GC_JH_SJ_ID,ywCode,"queryForm","tcjhList1",encodeURI(controllername+"?queryList||yw_code="+ywCode),"2");
		}
	});
	$("#tabPage2").click(function(){
		queryListLS();
		$("#tabPage2").attr("href","#tab2");
	});
	$("#btnPlfk").click(function() {
		var isSelected = true;
		var params = "";
		$("[name=subBox]:checkbox:checked").each(function() {
			var line = $(this).closest("tr").find("th").text()-1;
			isSelected = false;
			params += $(this).val()+",";
		});
		if(isSelected) {
			requireSelectedOneRow();
			return;
		} else {
			xConfirm("提示信息","选中记录将按照部门时间点反馈计划，是否继续？");
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').one("click",function(){
				$.ajax({
					url:controllername + "?plfkJh&params="+params+"&fklx="+ywCode,
					success:function(rs) {
						queryList();
					}
				});
			});
		}
	});
	//查询按钮
	var btn = $("#query");
	btn.click(function() {
		queryListLS();
	});
	//清空查询条件
	var clear=$("#clear");
	clear.click(function(){
		$("#queryForm2").clearFormResult();
		initCommonQueyPage();
	});
	//按钮绑定事件（导出）
	$("#btnExp").click(function() {
		if(exportRequireQuery($("#tcjhList1"))){//该方法需传入表格的jquery对象
			printTabList("tcjhList1");
		}
	});
	//按钮绑定事件（导出）
	$("#btnExpHistory").click(function() {
		if(exportRequireQuery($("#tcjhList2"))){//该方法需传入表格的jquery对象
			printTabList("tcjhList2");
		}
	});
});


//详细信息
function rowView(index){
	var obj = $("#tcjhList1").getSelectedRowJsonByIndex(index);
	var xmid = convertJson.string2json1(obj).XMID;
	$(window).manhuaDialog(xmscUrl(xmid));
}

//复选框
function cks(obj){
	  var hangshu=obj.IDNUM-1;
		 return "<input type='checkbox' colindex='"+hangshu+"' name='subBox' id='fuxuan' value='"+(obj.GC_JH_SJ_ID+"|"+obj.SJSJJD+"|"+ywCode)+"'>"; 
	}
//复选框全选取消
$(function() {
    $("#checkAll").click(function() {
        $('input[name="subBox"]').prop("checked",this.checked); 
    });
    var sub = $("input[name='subBox']");
    sub.click(function(){
        $("#checkAll").prop("checked",sub.length == $("input[name='subBox']:checked").length ? true : false);
    });
 });

//关闭事件
function closeNowCloseFunction()
{
   try {
      	var fuyemian=$(window).manhuaDialog.getParentObj();
		var s = fuyemian.callBackQuery;
	    if(s){
		   fuyemian.callBackQuery();
	    } else {
      		var frameW = $(window).manhuaDialog.getFrameObj();
      		var w = frameW.callBackQuery;
	      	if(w){
	      		frameW.callBackQuery();
	      	}
       	}
   } catch(e) {
      	window.parent.getProcess();
   }
}
</script>
</head>
<app:dialogs/>
	<body>
	<div class="row-fluid">
			<div class="span12">
			<ul class="nav nav-tabs">
					<li class="active">
						<a href="#tab1" data-toggle="tab" id="tabPage1">待处理</a>
					</li>
					<li class="">
						<a href="#tab2" data-toggle="tab" id="tabPage2">处理历史</a>
					</li>
				</ul>
		<div class="tab-content active">
			<div class="tab-pane active" id="tab1">
				<div class="span12">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						<span class="pull-right">
							<button id="btnFk" class="btn" type="button">反馈统筹计划</button>
							<button id="btnPlfk" class="btn" type="button">批量反馈计划</button>
							<button id="btnExp" class="btn" type="button">导出</button>
						</span>
					</h4>
					<form method="post" id="queryForm"></form>
					<div style="height: 5px;"></div>
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="tcjhList1" width="100%" type="single" pageNum="1000">
							<thead>
								<tr>
									<th name="XH" id="_XH" colindex=1 tdalign="center"> &nbsp;#&nbsp; </th>
									<th  fieldname="XMBH"   tdalign="center" CustomFunction="cks">
			                     		<input type="checkbox" name="fuxuans"  id="checkAll" > 
			                     	</th>
									<th fieldname="XMBH" colindex=3 hasLink="true" linkFunction="rowView" rowMerge="true"> &nbsp;项目编号&nbsp;</th>
									<th fieldname="XMMC" colindex=4 maxlength="15" rowMerge="true"> &nbsp;项目名称&nbsp; </th>
									<th fieldname="BDBH" colindex=5 > &nbsp;标段编号&nbsp; </th>
									<th fieldname="BDMC" colindex=6 maxlength="15" > &nbsp;标段名称&nbsp; </th>
									<th fieldname="XMGLGS" colindex=7 maxlength="15" tdalign="center"> &nbsp;项目管理公司&nbsp; </th>
									<th fieldname="XMXZ" colindex=8 tdalign="center"> &nbsp;项目性质&nbsp; </th>
									<th fieldname="JHSJJD" colindex=9 tdalign="center"> &nbsp;计划完成时间&nbsp; </th>
									<th fieldname="SJSJJD" colindex=10 tdalign="center"> &nbsp;部门反馈时间&nbsp; </th>
									<th fieldname="FKQK" colindex=11 tdalign="right">&nbsp;历史反馈次数&nbsp;</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		</div>
		<div class="tab-pane" id="tab2">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						<span class="pull-right">
							<button id="btnExpHistory" class="btn" type="button">导出</button>
							<!-- <button id="btnFk" class="btn" type="button">反馈统筹计划</button>
							<button id="btnPlfk" class="btn" type="button">批量反馈计划</button> -->
						</span>
					</h4>
					<form method="post" id="queryForm2">
					 <table class="B-table" width="100%">
					<tr>
			        <!--公共的查询过滤条件 -->
			         <jsp:include page="/jsp/business/common/commonQuery.jsp" flush="true">
			         	<jsp:param name="prefix" value="t"/> 
			         </jsp:include>
			        
			         <td class=" bottom-border text-right">
			           <button id="query" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
			           <button id="clear" class="btn btn-link"  type="button"><i class="icon-trash"></i>清空</button>
			          </td>
			        </tr>
			        </table>
					</form>
					<div style="height: 5px;"></div>
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="tcjhList2" width="100%" type="single" pageNum="2">
							<thead>
								<tr>
									<th name="XH" id="_XH" colindex=1 tdalign="center"> &nbsp;#&nbsp; </th>
									<th fieldname="XMBH" colindex=3 hasLink="true" linkFunction="rowView" rowMerge="true"> &nbsp;项目编号&nbsp;</th>
									<th fieldname="XMMC" colindex=4 maxlength="15" rowMerge="true"> &nbsp;项目名称&nbsp; </th>
									<th fieldname="BDBH" colindex=5 > &nbsp;标段编号&nbsp; </th>
									<th fieldname="BDMC" colindex=6 maxlength="15" > &nbsp;标段名称&nbsp; </th>
									<th fieldname="XMGLGS" colindex=7 maxlength="15" tdalign="center"> &nbsp;项目管理公司&nbsp; </th>
									<th fieldname="XMXZ" colindex=8 tdalign="center"> &nbsp;项目性质&nbsp; </th>
									<th fieldname="JHSJJD" colindex=9 tdalign="center"> &nbsp;计划完成时间&nbsp; </th>
									<th fieldname="SJSJJD" colindex=10 tdalign="center"> &nbsp;部门反馈时间&nbsp; </th>
									<th fieldname="JHZDMC" colindex=10 tdalign="center"> &nbsp;实际完成时间&nbsp; </th>
									<th fieldname="CLSJ" colindex=11 tdalign="center">&nbsp;处理时间&nbsp;</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		</div></div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none" target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="asc" fieldname="" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id="queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>