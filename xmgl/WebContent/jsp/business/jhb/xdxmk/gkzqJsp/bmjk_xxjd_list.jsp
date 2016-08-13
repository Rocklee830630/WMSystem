<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>项目管理公司-形象进度</title>
<%
	String nd = request.getParameter("nd");
	String index = request.getParameter("index");
	if(Pub.empty(index)){
		index = "";
	}
	String tcjhztid = request.getParameter("tcjhztid");
	if(Pub.empty(tcjhztid)){
		tcjhztid = "";
	}
	String xmglgs = request.getParameter("xmglgs");
	if(Pub.empty(xmglgs)){
		xmglgs = "";
	}
	String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/sjzqController.do";
var nd = '<%=nd%>';
var index = '<%=index%>';
var sysdate = '<%=sysdate %>';
var xmglgs = '<%=xmglgs%>';
//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#xxjdList").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	
	setPageHeight();
	
	init();
	
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 if(exportRequireQuery($("#xxjdList"))){//该方法需传入表格的jquery对象
		      printTabList("xxjdList");
		  }
	});
	
});
//页面默认参数
function init(){
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}



//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xxjdList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query_xxjd&nd="+nd+"&index="+index+"&xmglgs="+xmglgs,data,xxjdList,"aa",false);
}

function aa(){
	
}



//详细信息
function rowView(index){
	var obj = $("#xxjdList").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).XMID;
	$(window).manhuaDialog(xmscUrl(id));
}
//标段图标样式
function doBdmc(obj){
	if(obj.XMBS == "0"){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}
//<状态>样式
function doZt(obj){
	if(obj.ZT == "0"){
		return '<span class="label label-important">'+obj.ZT_SV+'</span>';
	}else if(obj.ZT == "1"){
		return '<span class="label label-success">'+obj.ZT_SV+'</span>';
	}
	
}
//<编制>样式
function doBZ(obj){
	if(obj.ZT_BZ == "0"){
		return '<span class="label label-important">'+obj.ZT_BZ_SV+'</span>';
	}else if(obj.ZT_BZ == "1"){
		return '<span class="label label-success">'+obj.ZT_BZ_SV+'</span>';
	}
	
}
//<反馈>样式
function doFK(obj){
	if(obj.ZT_FK == "0"){
		return '<span class="label label-important">'+obj.ZT_FK_SV+'</span>';
	}else if(obj.ZT_FK == "1"){
		return '<span class="label label-success">'+obj.ZT_FK_SV+'</span>';
	}
	
}
//列表项<健康状况>状态
function doJkzk(obj){
	var days = 0;
	var showInfo = "";
	if(obj.SJKGSJ !="" && obj.SJWGSJ != ""){
		return '<div style="text-align:center"><i title="正常执行" class="icon-green"></i></div>';
	}else if(obj.KGSJ == "" && obj.WGSJ == ""){
		return '<div style="text-align:center"><i title="正常执行" class="icon-green"></i></div>';
	}else if(obj.KGSJ == "" && obj.WGSJ != ""){
		days = getDays(sysdate,obj.WGSJ);
		if(days >= 5 && days < 20 ){
			showInfo = '<div style="text-align:center"><i title="超期完成" class="icon-yellow"></i></div>';
		}else if(days >= 20){
			showInfo = '<div style="text-align:center"><i title="严重超期完成" class="icon-red"></i></div>';
		}else{
			showInfo = '<div style="text-align:center"><i title="正常执行" class="icon-green"></i></div>';
		}
	}else if(obj.KGSJ != "" && obj.WGSJ == ""){
		days = getDays(sysdate,obj.KGSJ);
		if(days >= 5 && days < 20 ){
			showInfo = '<div style="text-align:center"><i title="超期完成" class="icon-yellow"></i></div>';
		}else if(days >= 20){
			showInfo = '<div style="text-align:center"><i title="严重超期完成" class="icon-red"></i></div>';
		}else{
			showInfo = '<div style="text-align:center"><i title="正常执行" class="icon-green"></i></div>';
		}
	}else if(obj.KGSJ != "" && obj.WGSJ != ""){
		days = getDays(sysdate,obj.KGSJ);
		var days1 = getDays(sysdate,obj.WGSJ);
		if(days >= days1){
			if(days >= 5 && days < 20 ){
				showInfo = '<div style="text-align:center"><i title="超期完成" class="icon-yellow"></i></div>';
			}else if(days >= 20){
				showInfo = '<div style="text-align:center"><i title="严重超期完成" class="icon-red"></i></div>';
			}else{
				showInfo = '<div style="text-align:center"><i title="正常执行" class="icon-green"></i></div>';
			}
		}else{
			if(days1 >= 5 && days1 < 20 ){
				showInfo = '<div style="text-align:center"><i title="超期完成" class="icon-yellow"></i></div>';
			}else if(days1 >= 20){
				showInfo = '<div style="text-align:center"><i title="严重超期完成" class="icon-red"></i></div>';
			}else{
				showInfo = '<div style="text-align:center"><i title="正常执行" class="icon-green"></i></div>';
			}
		}
	}
	return showInfo;
}
//计算日期天数
function getDays(strDateStart,strDateEnd){
	if(strDateStart < strDateEnd){
		return 0;
	}
   var strSeparator = "-"; //日期分隔符
   var oDate1;
   var oDate2;
   var iDays;
   oDate1= strDateStart.split(strSeparator);
   oDate2= strDateEnd.split(strSeparator);
   var strDateS = new Date(oDate1[0] + "-" + oDate1[1] + "-" + oDate1[2]);
   var strDateE = new Date(oDate2[0] + "-" + oDate2[1] + "-" + oDate2[2]);
   iDays = parseInt(Math.abs(strDateS - strDateE ) / 1000 / 60 / 60 /24)//把相差的毫秒数转换为天数 

   return iDays ;
}


//标段编号图标样式
function doBdbh(obj){
	if(obj.BDBH == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDBH;
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
      <h4 class="title">形象进度
      	<span class="pull-right">
   			<button id="btnExpExcel" class="btn"  type="button">导出</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
              </table>
      </form>
<div style="height:5px;"> </div>
<div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="xxjdList" width="100%" type="single" pageNum="10">
		<thead>
			<tr>
				<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
				<th fieldname="ZT" rowspan="2" colindex=2 tdalign="center" CustomFunction="doZt" noprint="true">&nbsp;统筹计划状态&nbsp;</th>
				<th colspan="2">&nbsp;形象进度计划状态&nbsp;</th>
				<th fieldname="XMID" rowspan="2" colindex=5 CustomFunction="doJkzk" noprint="true">&nbsp;健康&nbsp;<BR>&nbsp;状况&nbsp;</th>
				<th fieldname="XMBH" rowspan="2" colindex=6 rowMerge="true" hasLink="true" linkFunction="rowView" >&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" rowspan="2" colindex=7 maxlength="15" rowMerge="true">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDBH" rowspan="2" colindex=8 CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" rowspan="2" colindex=9 CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
				<th fieldname="YZDB" rowspan="2" colindex=10 tdalign="center">&nbsp;业主&nbsp;<BR>&nbsp;代表&nbsp;</th>
				<th fieldname="SGDW" rowspan="2" colindex=11 >&nbsp;施工单位&nbsp;</th>
				<th fieldname="JLDW" rowspan="2" colindex=12 >&nbsp;监理单位&nbsp;</th>
				<th fieldname="JSMB" rowspan="2" colindex=13 maxlength="15">&nbsp;年度目标&nbsp;</th>
				<th colspan="2">&nbsp;计划时间&nbsp;</th>
				<th colspan="2">&nbsp;实际时间&nbsp;</th>
				<th fieldname="FXMS" rowspan="2" colindex=18 >&nbsp;风险描述&nbsp;</th>
			</tr>
			<tr>
				<th fieldname="ZT_BZ" colindex=3 tdalign="center" CustomFunction="doBZ" noprint="true">&nbsp;编制&nbsp;</th>
				<th fieldname="ZT_FK" colindex=4 tdalign="center" CustomFunction="doFK" noprint="true">&nbsp;反馈&nbsp;</th>
				<th fieldname="KGSJ" colindex=14 tdalign="center">&nbsp;开工&nbsp;</th>
				<th fieldname="WGSJ" colindex=15 tdalign="center">&nbsp;完工&nbsp;</th>
				<th fieldname="SJKGSJ" colindex=16 tdalign="center">&nbsp;开工&nbsp;</th>
				<th fieldname="SJWGSJ" colindex=17 tdalign="center">&nbsp;完工&nbsp;</th>
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
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="ASC" fieldname = "T.XMBH,T.XMBS,T.PXH"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>