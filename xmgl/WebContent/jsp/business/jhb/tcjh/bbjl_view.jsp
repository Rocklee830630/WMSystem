<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>统筹计划-版本信息</title>
<%
	String jhid = request.getParameter("jhid");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
var jhid = "<%=jhid%>";
//页面初始化
$(function() {
	//设置查询无结果不提示信息
	g_bAlertWhenNoResult = false;
	init();
	
});

	
//页面默认参数
function init(){
	$("#JHID").val(jhid);
	//生成json串
	var data = combineQuery.getQueryCombineData(queryFormBgbb,frmPost,bgList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryBgxx",data,bgList);
}
function tr_click(obj,list){
	if(list=="bgList"){
		$("#BGID").val(obj.GC_JH_BGBB_ID);
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,bgxxList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryBgxm",data,bgxxList);
	}
	
}
//列表项<项目地址>加图标
function doDz(obj){
	var xmdz = obj.XMDZ;
	/* if(xmdz != ""){
		if(xmdz.length>15){
			xmdz = '<abbr title="'+obj.XMDZ+'">'+xmdz.substring(0,15)+'...&nbsp;<a href="javascript:void(0);" onclick="selectDz()"><i title="查看" class="pull-right icon-map-marker"></i></a></abbr>';
		}else{
			xmdz = xmdz+'&nbsp;<a href="javascript:void(0);" onclick="selectDz()"><i title="查看" class="pull-right icon-map-marker"></a></i>';
		}
		return xmdz;
	} */
	if(xmdz != ""){
		return '<a href="javascript:void(0);" onclick="selectDz()"><i title="查看" class="pull-right icon-map-marker"></a></i>';
	}
	
}
//列表项<调整标识>加图标
function doXGBS(obj){
	var sfxg = obj.SFXG;
	if(sfxg == "1"){
		//return '<span class="myCellSpan" frame="box">是</span>';
		return '<span class="label label-important">已调整</span>';
	}else{
		return '<span class=""></span>';
	}
	
}
//点击项目地址图标
function selectDz(){
	window.open("${pageContext.request.contextPath }/jsp/business/jhb/xdxmk/img/earth.png");
}
//标段图标样式
function doBdmc(obj){
	if(obj.BDID == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
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
<style>
.myCellSpan{
	background-color:#FF8888 ;
	width:100%;
	height:100%;
	display:inline-block;
	margin:0;
	padding:0;
}
</style>
</head>
<body>
<app:dialogs/>
<div class="row-fluid">
	<form method="post" id="queryFormBgbb">
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<input type="text" id="JHID" fieldname="JHID" name="JHID" value="" keep="true" operation="="> 
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
      </table>
      </form>
	<div class="B-small-from-table-autoConcise">
    <h4 class="title">版本信息</h4>
	 <div class="B-small-from-table-auto">
      <table class="table-hover table-activeTd B-table" id="bgList" width="100%" type="single" pageNum="5">
		<thead>
			<tr>
				<th fieldname="BGBBH" tdalign="center">&nbsp;版本号&nbsp;</th>
				<th fieldname="BGRQ"  tdalign="center">&nbsp;调整日期&nbsp;</th>
				<th fieldname="BGSM" >&nbsp;调整说明&nbsp;</th>
				<th fieldname="FJ"  tdalign="left">&nbsp;相关附件&nbsp;</th>
			</tr>
		</thead>
		<tbody></tbody>
		</table>
	 </div>
	</div>
<div style="display:none">
<form method="post" id="queryForm">
      <table class="B-table">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<INPUT type="text" class="span12" kind="text" id="BGID" fieldname="t1.BGID"  value="" operation="=" >
				<!-- <input type="text" fieldname="XMSX" name="qXMSX" value="1" keep="true" operation="="> --> 
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
      </table>
      </form>
</div>
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">调整涉及项目</h4>
      <div class="overFlowX">
		<table class="table-hover table-activeTd B-table" id="bgxxList"  width="100%" editable="0" type="single" noPage="true" pageNum="1000">
		<thead>
			<tr>
				<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
				<th fieldname="SFXG" rowspan="2" colindex=2 tdalign="center" CustomFunction="doXGBS">&nbsp;是否调整&nbsp;</th>
				<th fieldname="XMBH" rowspan="2" colindex=3 rowMerge="true">&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" rowspan="2" colindex=4 maxlength="15" rowMerge="true">&nbsp;项目名称&nbsp;</th>
				<th fieldname="XMDZ" rowspan="2" colindex=5 maxlength="10">&nbsp;建设位置&nbsp;</th>
				<th fieldname="XMDZ" rowspan="2" colindex=6 CustomFunction="doDz">&nbsp;&nbsp;</th>
				<th fieldname="JSNR" rowspan="2" colindex=7 maxlength="10" >&nbsp;建设内容及规模&nbsp;</th>
				<th fieldname="XMGLGS" rowspan="2" colindex=8 maxlength="10" tdalign="center">&nbsp;项目管理公司&nbsp;</th>
				<th fieldname="BDBH" rowspan="2" colindex=9 CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" rowspan="2" colindex=10 maxlength="15" CustomFunction="doBdmc" >&nbsp;标段名称&nbsp;</th>
				<th fieldname="XMXZ" rowspan="2" colindex=11 tdalign="center">&nbsp;项目性质&nbsp;</th>
				<th fieldname="JSMB" rowspan="2" colindex=12 maxlength="15">&nbsp;年度目标&nbsp;</th>
				<th colspan="2">&nbsp;工期安排（工程部及项目管理公司）&nbsp;</th>
				<th colspan="4">&nbsp;手续办理情况&nbsp;</th>
				<th colspan="4">&nbsp;设计情况&nbsp;</th>
				<th colspan="2">&nbsp;造价&nbsp;</th>
				<th colspan="2">&nbsp;招标&nbsp;</th>
				<th fieldname="ZC" rowspan="2" colindex=27 tdalign="center">&nbsp;征拆&nbsp;</th>
				<th fieldname="PQ" rowspan="2" colindex=28 tdalign="center">&nbsp;排迁&nbsp;</th>
				<th fieldname="JG" rowspan="2" colindex=29 tdalign="center">&nbsp;交工&nbsp;</th>
				<th fieldname="BZ" rowspan="2" colindex=30 maxlength="10">&nbsp;备注&nbsp;</th> 
			</tr>
			<tr>
				<th fieldname="KGSJ" colindex=13 tdalign="center">&nbsp;开工时间&nbsp;</th>
				<th fieldname="WGSJ" colindex=14 tdalign="center">&nbsp;完工时间&nbsp;</th>

				<th fieldname="KYPF" colindex=15 tdalign="center">&nbsp;可研批复&nbsp;</th>
				<th fieldname="HPJDS" colindex=16 tdalign="center">&nbsp;划拔决定书&nbsp;</th>
				<th fieldname="GCXKZ" colindex=17 tdalign="center">&nbsp;工程规划许可证&nbsp;</th>
				<th fieldname="SGXK" colindex=18 tdalign="center">&nbsp;施工许可&nbsp;</th>
				<th fieldname="CBSJPF" colindex=19 tdalign="center">&nbsp;初步设计批复&nbsp;</th>
				<th fieldname="CQT" colindex=20 tdalign="center">&nbsp;拆迁图&nbsp;</th>
				<th fieldname="PQT" colindex=21 tdalign="center">&nbsp;排迁图&nbsp;</th>
				<th fieldname="SGT" colindex=22 tdalign="center">&nbsp;施工图&nbsp;</th>
				<th fieldname="TBJ" colindex=23 tdalign="center">&nbsp;提报价&nbsp;</th>
				<th fieldname="CS" colindex=24 tdalign="center">&nbsp;财审&nbsp;</th>
				<th fieldname="JLDW" colindex=25 tdalign="center">&nbsp;监理单位&nbsp;</th>
				<th fieldname="SGDW" colindex=26 tdalign="center">&nbsp;施工单位&nbsp;</th>
			</tr>
		</thead>
		<tbody></tbody>
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
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "bgbbh"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>
