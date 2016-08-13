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
	doSearch();
});

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
	defaultJson.doQueryJsonList(controllername+"?pqnyXx&index="+index+"&name="+name+"&nd="+nd,data,DT1);
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

function doSearch(){
	g_bAlertWhenNoResult = true;
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?pqnyXx&index="+index+"&name="+name+"&nd="+nd,data,DT1,null,false);
}

function doRandering(obj){
	var showHtml = "";
	if(obj.ISJHWC=="1"){
		showHtml = obj.ISJHWC_SV;
	}else if(obj.ISJHWC=="0"){
		showHtml = '<span class="myCellSpan" frame="box">'+obj.ISJHWC_SV+'</span>';
	}else{
		showHtml = '<span class="label"></span>';
	}
	return showHtml;
}
function setYwid(n){
	$("#ywid").val(n);
}
//判断是否是项目
function doBdmc(obj){
	  var bd_name=obj.BDMC;
	  if(bd_name==null||bd_name==""){
		  return '<div style="text-align:center">—</div>';
	  }else{
		  return bd_name;			  
	  }
}
//判断是否是项目
function doBdbh(obj){
	  var bd_name=obj.BDBH;
	  if(bd_name==null||bd_name==""){
		  return '<div style="text-align:center">—</div>';
	  }else{
		  return bd_name;			  
	  }
}

//---------------------------------------
//控制未付合同款的颜色，复数为红色，其他为黑色
//---------------------------------------
function showWfhtk(obj){
	var Reg = /^-\d*$/;// 匹配负数
	if(Reg.test(obj.WFHTK)){
		return "<span style='color:red;'>"+obj.WFHTK_SV+"</span>";
	}else{
		return obj.WFHTK_SV;
	}
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
	<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" pageNum="10">
		<thead>
			<tr>
				<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;
				</th>
				<th fieldname="XMBH" maxlength="15" rowspan="2" colindex=2 rowMerge="true" tdalign="left" hasLink="true" linkFunction="rowView">&nbsp;项目编号&nbsp;
				</th>
				<th fieldname="XMMC" rowspan="2" colindex=3  tdalign="left" rowMerge="true" style="width: 20%" maxlength="15">
					&nbsp;项目名称&nbsp;
				</th>
				<th fieldname="BDBH" rowspan="2" colindex=4 style="width: 20%" maxlength="10" Customfunction="doBdbh">
					&nbsp;标段编号&nbsp;
				</th>
				<th fieldname="BDMC" rowspan="2" colindex=5 style="width: 20%" maxlength="10" Customfunction="doBdmc">
					&nbsp;标段名称&nbsp;
				</th>
				<th fieldname="BDDD" rowspan="2" colindex=6 style="width: 300px" maxlength="10">
					&nbsp;标段地点&nbsp;
				</th>
				<th fieldname="GXLB" rowspan="2" colindex=7 tdalign ="center">
					&nbsp;管线类别&nbsp;
				</th>
				<th  fieldname="ZXMMC" rowspan="2" colindex=8 maxlength="15">
					&nbsp;排迁任务名称&nbsp;
				</th>
				<th  fieldname="PQDD" rowspan="2" colindex=9 maxlength="15">
					&nbsp;排迁地点&nbsp;
				</th>
				<th fieldname="PQFA" rowspan="2" colindex=10 maxlength="15">
					&nbsp;排迁方案&nbsp;
				</th>
				<th fieldname="ISJHWC" rowspan="2" colindex=11 tdalign="center" CustomFunction="doRandering">
					&nbsp;是否能按&nbsp;<br/>&nbsp;计划完成&nbsp;
				</th>
				<th colspan=2>&nbsp;开工时间&nbsp;</th>
				<th colspan=2>&nbsp;完工时间&nbsp;</th>
				<th fieldname="JZQK" rowspan="2" colindex=16 maxlength="15">
					&nbsp;进展情况&nbsp;
				</th>
				<th fieldname="SYGZL" rowspan="2" colindex=17 maxlength="15">
					&nbsp;剩余工作量&nbsp;
				</th>
				<th fieldname="CZWT" rowspan="2" colindex=18 maxlength="15">
					&nbsp;存在问题&nbsp;
				</th>
				<th fieldname="JJFA" rowspan="2" colindex=19 maxlength="15">
					&nbsp;解决方案&nbsp;
				</th>
				<th fieldname="ZYGXPQT" rowspan="2" colindex=20>
					&nbsp;专业管线排迁图&nbsp;
				</th>
				<th fieldname="PQLLD" rowspan="2" colindex=21>
					&nbsp;排迁联络单&nbsp;
				</th>
				<th colspan="3">
					&nbsp;内业情况&nbsp;
				</th>
				<th colspan=7 tdalign ="center">
					&nbsp;合同情况&nbsp;
				</th>
			</tr>
			<tr>
				<th fieldname="KGSJ_JH" colindex=12 type="date" tdalign="center">
					&nbsp;计划&nbsp;
				</th>
				<th fieldname="KGSJ" colindex=13 type="date" tdalign="center">
					&nbsp;实际&nbsp;
				</th>
				<th fieldname="WCSJ_JH" colindex=14 type="date" tdalign="center">
					&nbsp;计划&nbsp;
				</th>
				<th fieldname="WCSJ" colindex=15 type="date" tdalign="center">
					&nbsp;实际&nbsp;
				</th>
				<th fieldname="YSSSD" colindex=22>
					&nbsp;预算送审单&nbsp;
				</th>
				<th fieldname="GCYSSDB" colindex=23>
					&nbsp;工程预算审定表&nbsp;
				</th>
				<th fieldname="WTH" colindex=24>
					&nbsp;委托函&nbsp;
				</th>
				<th fieldname="HTBM" colindex=25>
					&nbsp;合同编号&nbsp;
				</th>
				<th fieldname="HTSX" colindex=26 tdalign ="center">
					&nbsp;合同属性&nbsp;
				</th>
				<th fieldname="HTQDJ" colindex=27 tdalign ="right">
					&nbsp;合同签订价（元）&nbsp;
				</th>
				<th fieldname="SDZ" colindex=28 tdalign ="right">
					&nbsp;合同结算价（元）&nbsp;
				</th>
				<th fieldname="HTZF" colindex=29 tdalign ="right">
					&nbsp;已拨付合同款（元）&nbsp;
				</th>
				<th fieldname="WFHTK" colindex=30 tdalign ="right" CustomFunction="showWfhtk">
					&nbsp;未付合同款（元）&nbsp;
				</th>
				<th fieldname="HTJQDRQ" colindex=31 tdalign ="center">
					&nbsp;签订日期&nbsp;
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