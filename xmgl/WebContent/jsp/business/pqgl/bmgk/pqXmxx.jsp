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
<title>排迁查询方法</title>
<script type="text/javascript" charset="UTF-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/pqSjzqController.do";
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
	var height = xHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(Math.abs(height)/pageTableOne,10);
	$("#DT1").attr("pageNum", pageNum);
}

//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?pqrwFp&index="+index+"&name="+name+"&nd="+nd,data,DT1);
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
//页面处理方法
function doShow(){
	mark=='double'?($("#double").show()):($("#double").hide());
	var action = controllername +"?pqrwTable&index="+index+"&name="+name+"&nd="+nd;
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
			if(mark=='double'){
				$("#single th").each(function(j)
				{
					var str = $(this).attr("fieldname");
					array.indexOf(str)!=-1?($(this).show()):($(this).hide());
					if(str=="KGSJ_JH"||str=="KGSJ"||str=="WCSJ"||str=="WCSJ_JH"){
						$(this).removeAttr("fieldname");
						$(this).hide();
					}
					typeof(str)=='undefined'&&array.indexOf($(this).attr("id"))!=-1?($(this).show()):("");
					typeof(str)=='undefined'&&$(this).attr("id")=="_XH"?($(this).show()):("");
				});
				$("#double").show();
				$("#double th").each(function(j){
					var str = $(this).attr("fieldname");
					array.indexOf(str)!=-1?($(this).show()):($(this).hide());
				});
				var colindex=1;
				$("#DT1 tr th").each(function(j){
					var str=$(this).attr("fieldname");
					typeof(str)=='undefined'&&$(this).attr("name")=="_XH"?($(this).attr("colindex",colindex),colindex+=1):("");
					array.indexOf(str)!=-1?($(this).attr("colindex",colindex),colindex+=1):("");
				});
			}else{
				$("#single th").each(function(j)
				{
					var str = $(this).attr("fieldname");
					array.indexOf(str)!=-1?($(this).show()):($(this).hide());
					if(typeof(str)=='undefined'){
						$(this).attr("id")=="_XH"?($(this).show(),$(this).remove("colindex"),$(this).remove("rowspan")):($(this).hide());	
					}
				});
				$("#double").hide();
				$("#double th").each(function(j){
					$(this).remove("conlinde");
					$(this).remove("fieldname");
					$(this).hide();
				});
			}
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
			<tr id="single">
				<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;
				</th>
				<th fieldname="XMBH"  rowspan="2" colindex=2 rowMerge="true" tdalign="left" hasLink="true" linkFunction="rowView">
					&nbsp;项目编号&nbsp;
				</th>
				<th fieldname="XMMC"  rowspan="2" colindex=3 tdalign="left" rowMerge="true" maxlength="15">
					&nbsp;项目名称&nbsp;
				</th>
				<th fieldname="BDBH"  rowspan="2" colindex=4 >&nbsp;标段编号&nbsp;
				</th>
				<th fieldname="BDMC"  rowspan="2" colindex=5 maxlength="10" >&nbsp;标段名称&nbsp;
				</th>
				<th fieldname="BDDD"  rowspan="2" colindex=6 maxlength="10" >&nbsp;标段地点&nbsp;
				</th>
				<th fieldname="GXLB"  rowspan="2" colindex=7 tdalign ="center" >&nbsp;管线类别&nbsp;
				</th>
				<th  fieldname="ZXMMC"  rowspan="2" colindex=8 maxlength="15" >&nbsp;排迁任务名称&nbsp;
				</th>
				<th  fieldname="PQDD"  rowspan="2" colindex=9 maxlength="15" >&nbsp;排迁地点&nbsp;
				</th>
				<th id="KGSJ" colspan=2 >&nbsp;开工时间&nbsp;
				</th>
				<th id="WCSJ" colspan=2 >&nbsp;完工时间&nbsp;
				</th>
				<!-- <th fieldname="KGSJ_JH" colindex=10 tdalign="center" style="display:none"> &nbsp;开工计划时间&nbsp;
				</th>
				<th fieldname="KGSJ" colindex=11 tdalign="center" style="display:none">&nbsp;开工实际时间&nbsp;
				</th>
				<th fieldname="WCSJ_JH" colindex=12 tdalign="center" style="display:none">&nbsp;完工计划时间&nbsp;
				</th>
				<th fieldname="WCSJ" colindex=13 tdalign="center" style="display:none">&nbsp;完工实际时间&nbsp;
				</th> -->
			</tr>	
			<tr id="double" >	
				<th fieldname="KGSJ_JH" colindex=10 tdalign="center" > &nbsp;开工计划时间&nbsp;
				</th>
				<th fieldname="KGSJ" colindex=11 tdalign="center" >&nbsp;开工实际时间&nbsp;
				</th>
				<th fieldname="WCSJ_JH" colindex=12 tdalign="center" >&nbsp;完工计划时间&nbsp;
				</th>
				<th fieldname="WCSJ" colindex=13 tdalign="center" >&nbsp;完工实际时间&nbsp;
				</th>
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