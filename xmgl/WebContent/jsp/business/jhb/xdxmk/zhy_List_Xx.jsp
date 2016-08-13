<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%
	String spid = request.getParameter("spid");
%>
<app:base/>
<title>下达项目库-综合页</title>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";

//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#xdxmkList").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	setPageHeight();
	//自动完成项目名称模糊查询
	showAutoComplete("QXMMC",controllername+"?xmmcAutoCompleteToXmxdk","getXmmcQueryCondition"); 
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件(维护)
	$("#btnXmwh").click(function() {
		$(window).manhuaDialog({"title":"项目下达库>维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/xmwh.jsp"});
	});

	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 if(exportRequireQuery($("#xdxmkList"))){//该方法需传入表格的jquery对象
		      printTabList("xdxmkList");
		  }
	});
	//按钮绑定事件（下达统筹）
	$("#btnXdtc").click(function() {
		$(window).manhuaDialog({"title":"项目下达库>下达项目","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/xmxd.jsp"});
	});
	//按钮绑定事件（按项目下达统筹）
	$("#btnDownByXm").click(function() {
		$(window).manhuaDialog({"title":"项目下达库>按项目下达统筹","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/aXmxdtc.jsp"});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        //getNd();
    });
	
});
//页面默认参数
function init(){
	//getNd();
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}
//详细信息
function rowView(index){
	var obj = $("#xdxmkList").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).GC_TCJH_XMXDK_ID;
	$(window).manhuaDialog(xmscUrl(id));
}
//默认年度
function getNd(){
	setDefaultNd("qnd");
	///setDefaultOption($("#qnd"),new Date().getFullYear());
	//$("#qnd").val(new Date().getFullYear());
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
//列表项<稳定度>加图标
function doWDD(obj){
	var wdd = obj.WDD;
	if(wdd != ""){
		if(wdd == "1"){
			return '<i title="'+obj.WDD_SV+'" class="icon-green"></i>';
		}else if(wdd == "2"){
			return '<i title="'+obj.WDD_SV+'" class="icon-yellow"></i>';
		}else if(wdd =="3"){
			return '<i title="'+obj.WDD_SV+'" class="icon-red"></i>';
		}
	}
}
//点击项目地址图标
function selectDz(){
	window.open("${pageContext.request.contextPath }/jsp/business/jhb/xdxmk/img/earth.png");
}
//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryXx&spid=<%=spid %>",data,xdxmkList);
}
//项目名称自动模糊查询参数
function getXmmcQueryCondition(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
	return data;
}
//弹出区域回调
getWinData = function(data){
	queryList();
};

function closeParentCloseFunction(val){
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">项目下达库
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
        <!-- <tr style="display: none;">
          <th class="right-border bottom-border text-right">年度</th>
          <td class="right-border bottom-border">
          	<select class="span12 year" id="qnd" name = "QND"  fieldname = "ND" defaultMemo="全部" operation="=" kind="dic" src="T#GC_TCJH_XMXDK:distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE">
            </select>
          </td>
          <th class="right-border bottom-border text-right">项目名称</th>
          <td class="right-border bottom-border">
          	<input class="span12" type="text" placeholder="" name="QXMMC"
				fieldname="XMMC" operation="like" id="QXMMC" autocomplete="off"
				tablePrefix=""/>
		  </td>
          <th class="right-border bottom-border text-right">新/续</th>
          <td class="right-border bottom-border">
            <select class="span12 2characters" name = "QXJXJ" fieldname = "XJXJ" defaultMemo="全部" operation="=" kind="dic" src="XMXZ">
            </select>
          </td>
          <th class="right-border bottom-border text-right">项目类型</th>
          <td class="right-border bottom-border">
            <select class="span12 4characters" name = "QXMLX" fieldname = "XMLX" defaultMemo="全部" operation="=" kind="dic" src="XMLX">
            </select>
          </td>
          <th class="right-border bottom-border text-right">项目属性</th>
          <td class="bottom-border">
          	<select class="span12 2characters" name = "QXMSX" fieldname = "XMSX" defaultMemo="全部" operation="=" kind="dic" src="XMSX">
            </select>
          </td>
          <th class="right-border bottom-border text-right">下达状态</th>
          <td class="bottom-border">
          	<select class="span12 3characters" name = "ISNATC" fieldname = "ISNATC" defaultMemo="全部" operation="=" kind="dic" src="XDZT">
            </select>
          </td>
          <td class="text-left bottom-border text-right">
           <button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
           <button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
          </td>
         </tr> -->
      </table>
      </form>
	<div style="height:5px;"> </div>
	<div class="overFlowX">
		<table class="table-hover table-activeTd B-table" id="xdxmkList" width="100%" type="single" pageNum="10" editable="0" printFileName="项目下达库">
		<thead>
			<tr>
				<th  name="XH" id="_XH" rowspan="2" colindex=1 tdalign="center">#</th>
				<th fieldname="XMBH" rowspan="2" colindex=2 hasLink="true" linkFunction="rowView">项目编号</th>
				<th fieldname="XMMC" rowspan="2" colindex=3 maxlength="15">项目名称</th>
				<th fieldname="PCH" rowspan="2" colindex=4 tdalign="center">计财批</th>
				<th fieldname="ISNATC" rowspan="2" colindex=5 tdalign="center" >下达</th>
				<th fieldname="XJXJ" rowspan="2" colindex=6 tdalign="center">新/续</th>
				<th fieldname="XMLX" rowspan="2" colindex=7 tdalign="center">项目类型</th>
				<th fieldname="XMGLGS" rowspan="2" colindex=8 maxlength="10">项管公司</th>
				<th fieldname="XMSX" rowspan="2" colindex=9 tdalign="center">项目属性</th>
				<th fieldname="WDD" rowspan="2" colindex=10 tdalign="center" CustomFunction="doWDD">稳定度</th>
				<th fieldname="ISBT" rowspan="2" colindex=11 tdalign="center">BT</th>
				<th fieldname="XMDZ" rowspan="2" colindex=12 maxlength="15">项目地址</th>
				<th fieldname="XMDZ" rowspan="2" colindex=13 CustomFunction="doDz" noprint="true"></th>
				<th colspan="4">年度总投资额（元）</th>
			</tr>
			<tr>
				<th fieldname="GC" colindex=14 tdalign="right">工程</th>
				<th fieldname="ZC" colindex=15 tdalign="right">征拆</th>
				<th fieldname="QT" colindex=16 tdalign="right">其他</th>
				<th fieldname="JHZTZE" colindex=17 tdalign="right">合计</th>
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
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "PXH,XMBH"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>