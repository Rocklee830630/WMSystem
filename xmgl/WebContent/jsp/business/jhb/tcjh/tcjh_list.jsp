<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>统筹计划管理-综合页</title>
<%
	java.util.Calendar cal = java.util.Calendar.getInstance();
	int year = cal.get(java.util.Calendar.YEAR);
%>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>	
<script type="text/javascript" charset="utf-8">

//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
var objRow;

//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
	
}

//页面初始化
$(function() {
	setPageHeight();
	//自动完成项目名称模糊查询
	showAutoComplete("QXMMC",controllername+"?xmmcAutoQuery","getXmmcQueryCondition"); 
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件(维护)
	$("#btnWh").click(function() {
		$(window).manhuaDialog({"title":"统筹计划管理>计划管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/tcjh/tcjh_xmwh.jsp?","modal":"1"});
	});
	//按钮绑定事件（导出）
	$("#btnExp").click(function() {
		 if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
		      printTabList("DT1");
		  }
	});
	//按钮绑定事件（刷新标签）
	$("#btnSxbq").click(function() {
		xInfoMsg("刷新标签","");
	});
	//按钮绑定事件（计划下发）
	$("#btnJhxf").click(function() {
		if($("#JHID").val()==""){
			xInfoMsg('请选择<批次>！',"");
			return;
		}

		var jhid = $("#JHID").val();
		var success = false;
		var data = null;
		var obj = queryJh(controllername+"?queryJh&jhid="+jhid,data,DT1);
		if(obj.XFBB == null || obj.XFBB == ""){
			xConfirm("信息确认","确认将该计划进行下发吗？");
		}else{
			xConfirm("信息确认",
					"该计划已经下发，确定再次下发么？<br>"+
					" 当前计划版本:<font style='margin-left:5px; margin-right:5px;;font-size:16px;color:red;'>"+obj.MQBB
					/**
					+"</font><br> 初次下发版本:<font style='margin-left:5px; margin-right:5px;;font-size:16px;color:red;'>"+obj.SCXFBB
					+"</font> 日期:<font style='margin-left:5px; margin-right:5px;;font-size:16px;color:red;'>"+obj.SCXFRQ
					**/
					+"</font> <br>上一次下发日期:<font style='margin-left:5px; margin-right:5px;;font-size:16px;color:red;'>"+obj.XFRQ+"</font>"
					);
		}
		$('#ConfirmYesButton').unbind();
		$('#ConfirmYesButton').one("click",function(){
			success = xdtcjhByPc(controllername+"?xdtcjhByPc&jhid="+jhid,data,jhid);
			if(success){
				successInfo("","");
			}else{
				xFailMsg('操作失败！',"");
			}
		});  
		
	});
	//按钮绑定事件（版本信息）
	$("#btnBbxx").click(function() {
		if($("#JHID").val()==""){
			xInfoMsg('请选择<批次>！',"");
			return;
		}
		var jhid = $("#JHID").val();
		$(window).manhuaDialog({"title":"统筹计划管理>版本信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/tcjh/bbjl_view.jsp?jhid="+jhid,"modal":"1"});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
    });
	
    $("#btnXmxf").click(function() {
        $(window).manhuaDialog({"title":"项目管理>选择项目","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/tcjh/xmmore.jsp","modal":"1"});
    });
});
//页面默认参数
function init(){
	getNd();
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
	
}
//详细信息
function rowView(index){
	var obj = $("#DT1").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).XMID;
	$(window).manhuaDialog(xmscUrl(id));
}
function tr_click(obj){
	objRow = obj;
}
function xdtcjhByPc(actionName, data1){
	var success = true;
	$.ajax({
		type : 'post',
		url : actionName,
		data : data1,
		dataType : 'json',
		async :	false,
		success : function(result) {
			success = true;
		},
	    error : function(result) {
		     	alert(result.msg);
			    defaultJson.clearTxtXML();
			    success = false;
		}
	});
	 return success;
}
function queryJh(actionName, data,tablistID){
	var rowObj;
	$.ajax({
		type : 'post',
		url : actionName,
		data : data,
		cache : false,
		dataType : "json",  
		async :	false,
		success : function(result) {
			rowObj = convertJson.string2json1(result.msg).response.data[0];
		}
	});
	return rowObj;
}
//回调函数
getWinData = function(data){
	$(window).manhuaDialog.close();
	var url = controllername + "?updatebatchdata&JHID="+$("#JHID").val()+"&YWID="+data.YWID+"&BGSM="+encodeURIComponent(data.BGSM);
	var indexarry = new Array();
	indexarry = $("#DT1").getChangeRows();
	if(indexarry == "")
	{
		xInfoMsg('请至少修改一条记录',"");
		return
 	}
	//获取表格表头的数组,按照表格显示的顺序
	var tharrays = new Array();
	var comprisesData;
	tharrays = $("#DT1").getTableThArrays();
	var rowValue = $("#tcjhList").getSelectedRow();//获得选中行的json对象
	if(tharrays != null){
		comprisesData = $("#DT1").comprisesData(indexarry,tharrays);
		//alert(DT1);
		defaultJson.doUpdateBatchJson(url, comprisesData,DT1,indexarry);

	}

};

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
//点击项目地址图标
function selectDz(){
	window.open("${pageContext.request.contextPath }/jsp/business/jhb/xdxmk/img/earth.png");
}
//默认年度
function getNd(){
	//$("#qnd").val(new Date().getFullYear());
	generateFyjjhNdMc($("#qnd"),$("#JHID")); 
	setOptionSelectedIndex($("#JHID"));
}
//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,DT1);
}
//项目名称自动模糊查询参数
function getXmmcQueryCondition(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	return data;
}

/**
 * 根据计划查询非应急批次
 * ndObj 年度的jquery对象
 * mcObj 计划名称jquery对象
 */
function generateFyjjhNdMc(ndObj,mcObj){
	ndObj.attr("src","T#GC_JH_SJ: distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	//ndObj.val(new Date().getFullYear());
	//setDefaultOption(ndObj,new Date().getFullYear());
	setDefaultNd(ndObj.attr("id"));
	if(mcObj){
		mcObj.attr("kind","dic");
		/* mcObj.attr("src","T#GC_JH_ZT:GC_JH_ZT_ID:jhpch:SFYX='1' AND ND='" + ndObj.val()+"' and GC_JH_ZT_ID in(select JHID from GC_JH_SJ where sfyx='1' and nd='"+ ndObj.val()+"' and XMSX='1') ORDER BY JHPCH ASC"); */
		//调整为应急项目也可以编制计划 modified by hongpeng.dong at 2013.12.1
		mcObj.attr("src","T#GC_JH_ZT:GC_JH_ZT_ID:jhpch:SFYX='1' AND ND='" + ndObj.val()+"' and GC_JH_ZT_ID in(select JHID from GC_JH_SJ where sfyx='1' and nd='"+ ndObj.val()+"') ORDER BY JHPCH ASC");
		mcObj.html('');
		reloadSelectTableDic(mcObj);
		ndObj.change(function() {
			mcObj.html('');
			if(!ndObj.val().length){
			}
			/* mcObj.attr("src","T#GC_JH_ZT:GC_JH_ZT_ID:jhpch:SFYX='1' AND ND='" + ndObj.val()+"' and GC_JH_ZT_ID in(select JHID from GC_JH_SJ where sfyx='1' and nd='"+ ndObj.val()+"' and XMSX='1') ORDER BY JHPCH ASC"); */
			mcObj.attr("src","T#GC_JH_ZT:GC_JH_ZT_ID:jhpch:SFYX='1' AND ND='" + ndObj.val()+"' and GC_JH_ZT_ID in(select JHID from GC_JH_SJ where sfyx='1' and nd='"+ ndObj.val()+"') ORDER BY JHPCH ASC");
			reloadSelectTableDic(mcObj);
		});
	}
}
//标段名称图标样式
function doBdmc(obj){
	if(obj.XMBS == "0"){
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

//<开工时间>图标样式
function doKGSJ(obj){
	if(obj.ISKGSJ == "0"){
		return '<div style="text-align:center">—</div>';
	}else if(obj.ISKGSJ == "2"){
		return '<div style="text-align:center">往年</div>';
	}else{
		return obj.KGSJ;
	}
}
//<完工时间>图标样式
function doWGSJ(obj){
	if(obj.ISWGSJ == "0"){
		return '<div style="text-align:center">—</div>';
	}else if(obj.ISWGSJ == "2"){
		return '<div style="text-align:center">往年</div>';
	}else{
		return obj.WGSJ;
	}
}
//<可研批复>图标样式
function doKYPF(obj){
	if(obj.ISKYPF == "0"){
		return '<div style="text-align:center">—</div>';
	}else if(obj.ISKYPF == "2"){
		return '<div style="text-align:center">往年</div>';
	}else{
		return obj.KYPF;
	}
}
//<划拨决定书>图标样式
function doHPJDS(obj){
	if(obj.ISHPJDS == "0"){
		return '<div style="text-align:center">—</div>';
	}else if(obj.ISHPJDS == "2"){
		return '<div style="text-align:center">往年</div>';
	}else{
		return obj.HPJDS;
	}
}
//<工程规划许可证>图标样式
function doGCXKZ(obj){
	if(obj.ISGCXKZ == "0"){
		return '<div style="text-align:center">—</div>';
	}else if(obj.ISGCXKZ == "2"){
		return '<div style="text-align:center">往年</div>';
	}else{
		return obj.GCXKZ;
	}
}
//<施工许可>图标样式
function doSGXK(obj){
	if(obj.ISSGXK == "0"){
		return '<div style="text-align:center">—</div>';
	}else if(obj.ISSGXK == "2"){
		return '<div style="text-align:center">往年</div>';
	}else{
		return obj.SGXK;
	}
}
//<计划初步设计批复>图标样式
function doCBSJPF(obj){
	if(obj.ISCBSJPF == "0"){
		return '<div style="text-align:center">—</div>';
	}else if(obj.ISCBSJPF == "2"){
		return '<div style="text-align:center">往年</div>';
	}else{
		return obj.CBSJPF;
	}
}
//<拆迁图>图标样式
function doCQT(obj){
	if(obj.ISCQT == "0"){
		return '<div style="text-align:center">—</div>';
	}else if(obj.ISCQT == "2"){
		return '<div style="text-align:center">往年</div>';
	}else{
		return obj.CQT;
	}
}
//<排迁图>图标样式
function doPQT(obj){
	if(obj.ISPQT == "0"){
		return '<div style="text-align:center">—</div>';
	}else if(obj.ISPQT == "2"){
		return '<div style="text-align:center">往年</div>';
	}else{
		return obj.PQT;
	}
}
//<施工图>图标样式
function doSGT(obj){
	if(obj.ISSGT == "0"){
		return '<div style="text-align:center">—</div>';
	}else if(obj.ISSGT == "2"){
		return '<div style="text-align:center">往年</div>';
	}else{
		return obj.SGT;
	}
}
//<提报价>图标样式
function doTBJ(obj){
	if(obj.ISTBJ == "0"){
		return '<div style="text-align:center">—</div>';
	}else if(obj.ISTBJ == "2"){
		return '<div style="text-align:center">往年</div>';
	}else{
		return obj.TBJ;
	}
}
//<造价财审>图标样式
function doCS(obj){
	if(obj.ISCS == "0"){
		return '<div style="text-align:center">—</div>';
	}else if(obj.ISCS == "2"){
		return '<div style="text-align:center">往年</div>';
	}else{
		return obj.CS;
	}
}
//<招标监理>图标样式
function doJLDW(obj){
	if(obj.ISJLDW == "0"){
		return '<div style="text-align:center">—</div>';
	}else if(obj.ISJLDW == "2"){
		return '<div style="text-align:center">往年</div>';
	}else{
		return obj.JLDW;
	}
}
//<招标施工>图标样式
function doSGDW(obj){
	if(obj.ISSGDW == "0"){
		return '<div style="text-align:center">—</div>';
	}else if(obj.ISSGDW == "2"){
		return '<div style="text-align:center">往年</div>';
	}else{
		return obj.SGDW;
	}
}
//<征拆>图标样式
function doZC(obj){
	if(obj.ISZC == "0"){
		return '<div style="text-align:center">—</div>';
	}else if(obj.ISZC == "2"){
		return '<div style="text-align:center">往年</div>';
	}else{
		return obj.ZC;
	}
}
//<排迁>图标样式
function doPQ(obj){
	if(obj.ISPQ == "0"){
		return '<div style="text-align:center">—</div>';
	}else if(obj.ISPQ == "2"){
		return '<div style="text-align:center">往年</div>';
	}else{
		return obj.PQ;
	}
}
//<交工>图标样式
function doJG(obj){
	if(obj.ISJG == "0"){
		return '<div style="text-align:center">—</div>';
	}else if(obj.ISJG == "2"){
		return '<div style="text-align:center">往年</div>';
	}else{
		return obj.JG;
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
      <h4 class="title">统筹计划管理
      	<span class="pull-right">
      	<app:oPerm url="jsp/business/jhb/tcjh/tcjh_xmwh.jsp">
      	<button id="btnWh" class="btn"  type="button">计划编制与调整</button>
      	</app:oPerm>
        <!--
  		<button id="btnSxbq" class="btn btn-inverse"  type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">刷新标段</button>
        -->
        <app:oPerm url="/tcjh/tcjhController?xdtcjhByPc">
  		<button id="btnJhxf" class="btn"  type="button">计划下发</button>
  		<button id="btnXmxf" class="btn"  type="button">项目下发</button>
  		</app:oPerm>
  		<app:oPerm url="jsp/business/jhb/tcjh/bbjl_view.jsp">
  		<button id="btnBbxx" class="btn"  type="button">版本信息</button>
  		</app:oPerm>
  		<button id="btnExp" class="btn"  type="button">导出</button>
      	</span>
      </h4>
     <form method="post" id="queryForm">
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<!-- <input type="text" fieldname="t.XMSX" name="qXMSX" value="1" keep="true" operation="=">  -->
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
          <th width="5%" class="right-border bottom-border text-right">年度</th>
          <td class="right-border bottom-border" width="6%">
            <select class="span12 year" id="qnd" name = "QND"  fieldname ="t.ND" defaultMemo="全部" operation="=">
            </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">批次</th>
          <td class="right-border bottom-border" width="9%">
            <select class="span12 4characters" id="JHID" name = "QJHID"  defaultMemo="全部" fieldname = "t.JHID" operation="=">
            </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">项目名称</th>
          <td class="right-border bottom-border" width="20%">
          	<input class="span12" type="text" placeholder="" name="QXMMC"
				fieldname="t.XMMC" operation="like" id="QXMMC" autocomplete="off"
				tablePrefix="t"/>
		  </td>
		  <th width="5%" class="right-border bottom-border text-right">项目管理公司</th>
			<td class="right-border bottom-border" width="7%">
				<select class="span12 2characters" id="xmglgs" name = "QXMGLGS" defaultMemo="全部"  fieldname = "t1.XMGLGS" operation="=" kind="dic" src="T#VIEW_YW_XMGLGS:ROW_ID:BMJC">
            	</select>
			</td>
          <td class="text-left bottom-border text-right">
            <button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
      		<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
          </td>

         </tr>
      </table>
      </form>
      <div style="height:5px;"> </div>
 	<div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="DT1"  width="100%" editable="0" type="single" pageNum="10" printFileName="统筹计划管理">
		<thead>
			<tr>
				<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
				<th fieldname="XMBH" rowspan="2" colindex=2 hasLink="true" linkFunction="rowView" rowMerge="true">&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" rowspan="2" colindex=3 maxlength="15" rowMerge="true">&nbsp;项目名称&nbsp;</th>
				<th fieldname="XMDZ" rowspan="2" colindex=4 maxlength="10">&nbsp;建设位置&nbsp;</th>
				<th fieldname="XMDZ" rowspan="2" colindex=5 CustomFunction="doDz" noprint="true">&nbsp;&nbsp;</th>
				<th fieldname="JSNR" rowspan="2" colindex=6 maxlength="10" >&nbsp;建设内容及规模&nbsp;</th>
				<th fieldname="XMGLGS" rowspan="2" colindex=7 maxlength="10" tdalign="center">&nbsp;项目管理公司&nbsp;</th>
				<th fieldname="BDBH" rowspan="2" colindex=8 CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" rowspan="2" colindex=9 maxlength="15" CustomFunction="doBdmc" >&nbsp;标段名称&nbsp;</th>
				<th fieldname="XMXZ" rowspan="2" colindex=10 tdalign="center">&nbsp;项目性质&nbsp;</th>
				<th fieldname="JSMB" rowspan="2" colindex=11 maxlength="15">&nbsp;年度目标&nbsp;</th>
				<th colspan="2">&nbsp;工期安排（工程部及项目管理公司）&nbsp;</th>
				<th colspan="4">&nbsp;手续办理情况&nbsp;</th>
				<th colspan="4">&nbsp;设计情况&nbsp;</th>
				<th colspan="2">&nbsp;造价&nbsp;</th>
				<th colspan="2">&nbsp;招标&nbsp;</th>
				<th fieldname="ZC" rowspan="2" colindex=26 tdalign="center" CustomFunction="doZC">&nbsp;征拆&nbsp;</th>
				<th fieldname="PQ" rowspan="2" colindex=27 tdalign="center" CustomFunction="doPQ">&nbsp;排迁&nbsp;</th>
				<th fieldname="JG" rowspan="2" colindex=28 tdalign="center" CustomFunction="doJG">&nbsp;交工&nbsp;</th>
				<th fieldname="BZ" rowspan="2" colindex=29 maxlength="10">&nbsp;备注&nbsp;</th> 
			</tr>
			<tr>
				<th fieldname="KGSJ" colindex=12 tdalign="center" CustomFunction="doKGSJ">&nbsp;开工时间&nbsp;</th>
				<th fieldname="WGSJ" colindex=13 tdalign="center" CustomFunction="doWGSJ">&nbsp;完工时间&nbsp;</th>
				<th fieldname="KYPF" colindex=14 tdalign="center" CustomFunction="doKYPF">&nbsp;可研批复&nbsp;</th>
				<th fieldname="HPJDS" colindex=15 tdalign="center" CustomFunction="doHPJDS">&nbsp;划拔决定书&nbsp;</th>
				<th fieldname="GCXKZ" colindex=16 tdalign="center" CustomFunction="doGCXKZ">&nbsp;工程规划许可证&nbsp;</th>
				<th fieldname="SGXK" colindex=17 tdalign="center" CustomFunction="doSGXK">&nbsp;施工许可&nbsp;</th>
				<th fieldname="CBSJPF" colindex=18 tdalign="center" CustomFunction="doCBSJPF">&nbsp;初步设计批复&nbsp;</th>
				<th fieldname="CQT" colindex=19 tdalign="center" CustomFunction="doCQT">&nbsp;拆迁图&nbsp;</th>
				<th fieldname="PQT" colindex=20 tdalign="center" CustomFunction="doPQT">&nbsp;排迁图&nbsp;</th>
				<th fieldname="SGT" colindex=21 tdalign="center" CustomFunction="doSGT">&nbsp;施工图&nbsp;</th>
				<th fieldname="TBJ" colindex=22 tdalign="center" CustomFunction="doTBJ">&nbsp;提报价&nbsp;</th>
				<th fieldname="CS" colindex=23 tdalign="center" CustomFunction="doCS">&nbsp;财审&nbsp;</th>
				<th fieldname="JLDW" colindex=24 tdalign="center" CustomFunction="doJLDW">&nbsp;监理单位&nbsp;</th>
				<th fieldname="SGDW" colindex=25 tdalign="center" CustomFunction="doSGDW">&nbsp;施工单位&nbsp;</th>
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
       <input type="hidden" name="txtFilter"  order="asc" fieldname = "t.PXH,t.XMBH,t.XMBS,T.BDBH"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>