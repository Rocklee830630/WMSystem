<!DOCTYPE HTML>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%
	String nd = request.getParameter("nd");
	String index = request.getParameter("index");
	String name = request.getParameter("name");
	String mark =request.getParameter("mark");
%>
<app:base/>
<title>设计数据钻取查询方法</title>
<script type="text/javascript" charset="UTF-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/sjSjzqController.do";
var index = "<%=index%>";
var nd = "<%=nd%>";
var name = "<%=name%>";
var mark="<%=mark%>";
$(function() {
	setPageHeight();
	//doShow();
	init();
});
//页面默认参数
function init() {
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}

//计算本页表格分页数
function setPageHeight() {
	var xHeight = parent.document.documentElement.clientHeight;
	var height = xHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(Math.abs(height)/pageTableOne,10);
	$("#DT1").attr("pageNum", pageNum);
}

//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?sjbgValue&index="+index+"&name="+name+"&nd="+nd,data,DT1);
}

//详细信息
function rowView(index) {
	var obj = $("#DT1").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).XMID;
	$(window).manhuaDialog(xmscUrl(id));
}

//按钮绑定事件（导出EXCEL）
$(function() {
	$("#btnExp").click(function() {
		 if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
		      printTabList("DT1");
		  }
	});
});

//项目部显示标段
function showBdmc(obj){
	var bd=obj.BDMC;
	if(bd==null||bd==""){
		return '<div style="text-align:center">—</div>';
	}else{
		return bd;
	}
}
//项目部显示标段
function showBdbh(obj){
	var bd=obj.BDBH;
	if(bd==null||bd==""){
		return '<div style="text-align:center">—</div>';
	}else{
		return bd;
	}
}

//页面处理方法
function doShow(){
	mark=='double'?($("#double").show()):($("#double").hide());
	var action = controllername +"?getTable&index="+index+"&name="+name+"&nd="+nd;
	$.ajax({
		url : action,
		success: function(result)
		{
			var resultmsgobj = convertJson.string2json1(result);
			var resultobj = convertJson.string2json1(resultmsgobj.msg);
			var subresultmsgobj = resultobj.response.data[0];
			var array=new Array();
			for(var key in subresultmsgobj)
			{
			    array.push(key);
			}
			for(var a=0;a<array.length;a++){
				var current=array[a]+"_SV";
				var next=array[a+1];
				current==next?(array.splice(a+1,1)):("");
				typeof(next)=='undefined'?(array.splice(a,1)):("");
			}
			$("#DT1 tr th").each(function(j)
			{
				var str = $(this).attr("fieldname");
				alert(str);
				array.indexOf(str)!=-1?($(this).show()):($(this).hide());
				typeof(str)=='undefined'&&$(this).attr("name")=="XH"?($(this).show()):("");
			});
		}
	});
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">
      <span class="pull-right">
      	<button id="btnExp" class="btn"  type="button">导出</button>
      </span>
      </h4>
     <form method="post" id="queryForm">
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
		 		<input type="text" id="ND" fieldname="ND" name="ND" operation="=">
		 		<input id="num" type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<=" /> 
			</TD>
        </TR>
      </table>
      </form>
 	<div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="DT1"  width="100%" editable="0" type="single">
		<thead>
			<tr>
				<th name="XH" id="_XH" >&nbsp;#&nbsp;</th>
				<th fieldname="XMBH" rowMerge="true" tdalign="left" hasLink="true" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" tdalign="left" rowMerge="true" maxlength="15">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDBH" CustomFunction="showBdbh">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" maxlength="10" CustomFunction="showBdmc">&nbsp;标段名称&nbsp;</th>
				<th fieldname="BGLB" tdalign="center" >&nbsp;变更类别&nbsp;</th>
				<th fieldname="BGBH" tdalign="center" >&nbsp;变更编号&nbsp;</th>
				<th fieldname="JBGRQ" tdalign="center" >&nbsp;变更日期&nbsp;</th>
				<th fieldname="BGFY" tdalign="right" >&nbsp;变更费用&nbsp;</th>
				<th fieldname="SJY" tdalign="center" >&nbsp;来源单位&nbsp;</th>
				<th fieldname="JSRQ" tdalign="center" >&nbsp;接收日期&nbsp;</th>
				<th fieldname="JSR" >&nbsp;接收人&nbsp;</th>
				<th fieldname="FS" tdalign="right" >&nbsp;接收份数&nbsp;</th>
				<th fieldname="LQFS" tdalign="right" >&nbsp;领取份数&nbsp;</th>
				<th fieldname="BGLLDJSRQ"  tdalign="center" >&nbsp;联络单接收日期&nbsp;</th>
				<th fieldname="LLDFFRQ" tdalign="center" >&nbsp;联络单发放日期&nbsp;</th>
				<!-- <th fieldname="SJDW" >&nbsp;设计单位&nbsp;</th>
				<th fieldname="SGDW" >&nbsp;施工单位&nbsp;</th>
				<th fieldname="JLDW" >&nbsp;监理单位&nbsp;</th> -->
				<th fieldname="BGNR"  maxlength="10" >&nbsp;变更内容&nbsp;</th>
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
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id="frmPost">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="" fieldname = ""	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>