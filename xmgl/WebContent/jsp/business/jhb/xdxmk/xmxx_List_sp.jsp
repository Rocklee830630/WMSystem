<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>下达项目库-项目信息</title>
<%
	String sjbh = request.getParameter("sjbh");
%>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
var sjbh = '<%=sjbh%>';
 //页面初始化
$(function() {
	init();
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
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xmList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryXmxxSp&sjbh="+sjbh,data,xmList);
}


//列表项<项目地址>加图标
function doDz(obj){
	var xmdz = obj.XMDZ;
	if(xmdz != ""){
		return '<a href="javascript:void(0);" onclick="selectDz()"><i title="查看" class="pull-right icon-map-marker"></a></i>';
	}
}
//点击项目地址图标
function selectDz(){
	window.open("${pageContext.request.contextPath }/jsp/business/jhb/xdxmk/img/earth.png");
}

//详细信息
function rowView(index){
	var obj = $("#xmList").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).GC_TCJH_XMXDK_SP_ID;
	$(window).manhuaDialog({"title":"项目下达库>项目信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/xmxxView.jsp?id="+id});
}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
	<div class="B-small-from-table-autoConcise">
	  <h4 class="title">项目信息
	  </h4>
	  <form method="post" id="queryForm">
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			</TD>
        </TR>
      </table>
      </form>
		<table class="table-hover table-activeTd B-table" id="xmList" width="100%" type="single" noPage="true" pageNum="1000">
			<thead>
				<tr>
					<th  name="XH" id="_XH" tdalign="center">&nbsp;#&nbsp;</th>
					<th fieldname="XMBH" hasLink="true" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>
					<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
					<th fieldname="XJXJ" tdalign="center" >&nbsp;项目性质&nbsp;</th>
					<th fieldname="XMLX" tdalign="center">&nbsp;项目类型&nbsp;</th>
					<th fieldname="XMGLGS">&nbsp;项目管理公司&nbsp;</th>
					<th fieldname="XMSX" tdalign="center">&nbsp;项目属性&nbsp;</th>
					<th fieldname="ISBT" tdalign="center">&nbsp;BT&nbsp;</th>
					<th fieldname="XMDZ" maxlength="15">&nbsp;项目地址&nbsp;</th>
					<th fieldname="XMDZ" CustomFunction="doDz">&nbsp;&nbsp;</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
    </div>
   </div>
  </div>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>

</html>