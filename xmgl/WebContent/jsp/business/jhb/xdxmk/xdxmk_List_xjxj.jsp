<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>下达项目库-项目选择</title>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
//计算本页表格分页数
function setPageHeight(){
	var height = getDivStyleHeight()-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#xdxmkList").attr("pageNum",pageNum);
}
//页面初始化
$(function() {
	//自动完成项目名称模糊查询
	showAutoComplete("QXMMC",controllername+"?xmmcAutoCompleteToXmxdk","getXmmcQueryCondition"); 
	setPageHeight();
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
    });
  	//按钮绑定事件（确定）
    $("#btnQd").click(function() {
    	if($("#xdxmkList").getSelectedRowIndex()==-1)
		 {
    		xInfoMsg('请选择一条记录！',"");
		    return
		 }
    	var rowjson=$("#xdxmkList").getSelectedRowJsonObj();
    	if(rowjson.XJXM!=""&&rowjson.XJXM!="undefined")
    	{
    		xInfoMsg('所选项目已被续建，不能再次续建！',"");
    		return;
    	}	
		xConfirm("信息确认","选择续建项目后，本项目的已录信息将被覆盖，是否继续！");
		$('#ConfirmYesButton').unbind();
		$('#ConfirmYesButton').one("click",function(){
	       	var rowValue = $("#xdxmkList").getSelectedRow();//获得选中行的json对象
	       	$(window).manhuaDialog.setData(rowValue);
			$(window).manhuaDialog.sendData();
			$(window).manhuaDialog.close();
		});  
	});
    
});
//页面默认参数
function init(){
	getNd();
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}

function tr_click(obj){
	if(obj.XJXM!="")
	{
		xInfoMsg('所选项目已被续建！',"");
	}	
}

//默认年度
function getNd(){
	setDefaultNd("qnd");
	//jiangc start 处理年度查询条件
	var length=document.getElementById('qnd').length;
	document.getElementById('qnd').options[length-1].remove();
	//jiangc end
	//setDefaultOption($("#qnd"),new Date().getFullYear());
}
//列表项<项目地址>加图标
function doDz(obj){
	var xmdz = obj.XMDZ;
	/* if(xmdz != ""){
		if(xmdz.length>15){
			//xmdz = xmdz.substring(0,10);
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

//处理是否被续建数据
function isxj(obj)
{
	var xjxm = obj.XJXM;
	if(xjxm!="")
	{
		return '<span >是</span>';
	}	
}
//点击项目地址图标
function selectDz(){
	window.open("${pageContext.request.contextPath }/fileUploadController.do?doPreview&fileid=a6d53878-82e0-4234-89e0-8a6bc5225da4");
	//window.open("${pageContext.request.contextPath }/WebContent/jsp/business/jhb/xdxmk/img/20120813100429-44746319.jpg");
}
//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
	//调用ajax插入
	//jiangc start 处理后台查询条件，不查询当年项目
	defaultJson.doQueryJsonList(controllername+"?query&flag_xj=1",data,xdxmkList);
	//jiangc end
}
//项目名称自动模糊查询参数
function getXmmcQueryCondition(){
	$("#QXMMC").attr("fieldname","XMMC");
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
	$("#QXMMC").attr("fieldname","xdk.XMMC");
	return data;
}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">项目下达库
      	<span class="pull-right">
      		<button id="btnQd" class="btn" type="button">确定</button>
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
        <tr>
          <th width="5%" class="right-border bottom-border text-right">年度</th>
          <td class="right-border bottom-border" width="7%">
          	<select class="span12 year" id="qnd" name = "QND"  fieldname = "XDK.ND" defaultMemo="全部" operation="=" kind="dic" src="T#GC_TCJH_XMXDK:distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE">
            </select>
          </td>
           <th width="5%" class="right-border bottom-border text-right">项目编号</th>
          <td class="right-border bottom-border" width="15%">
          	<input class="span12" type="text"  name="XMBH" fieldname="XDK.XMBH" operation="like" id="XMBH" />
		  </td>
          <th width="5%" class="right-border bottom-border text-right">项目名称</th>
          <td class="right-border bottom-border" width="15%">
          	<input class="span12" type="text" placeholder="" name="QXMMC"
				fieldname="XDK.XMMC" operation="like" id="QXMMC" autocomplete="off"
				tablePrefix=""/>
		  </td>
          <th width="5%" class="right-border bottom-border text-right">项目类型</th>
          <td class="right-border bottom-border" width="9%">
            <select class="span12 4characters" name = "QXMLX" fieldname = "XDK.XMLX" defaultMemo="全部" operation="=" kind="dic" src="XMLX">
            </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">项目属性</th>
          <td class="bottom-border" width="8%">
          	<select class="span12 2characters" name = "QXMSX" fieldname = "XDK.XMSX" defaultMemo="全部" operation="=" kind="dic" src="XMSX">
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
		<table class="table-hover table-activeTd B-table" id="xdxmkList" width="100%" type="single" pageNum="5" editable="0">
		<thead>
			<tr>
				<th  name="XH" id="_XH" rowspan="2" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
				<th fieldname="PXH" rowspan="2" colindex=2 tdalign="center">&nbsp;排序号&nbsp;</th>
				<th fieldname="XMBH" rowspan="2" colindex=3>&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" rowspan="2" colindex=4 maxlength="15">&nbsp;项目名称&nbsp;</th>
				<th fieldname="ISNATC" rowspan="2" colindex=5 tdalign="center" >&nbsp;下达状态&nbsp;</th>
				<th fieldname="XMLX" rowspan="2" colindex=6 tdalign="center">&nbsp;项目类型&nbsp;</th>
				<th fieldname="XMGLGS" rowspan="2" colindex=7 maxlength="10">&nbsp;项目管理公司&nbsp;</th>
				<th fieldname="XMSX" rowspan="2" colindex=8 tdalign="center">&nbsp;项目属性&nbsp;</th>
				<th fieldname="ISBT" rowspan="2" colindex=9 tdalign="center">&nbsp;BT&nbsp;</th>
				<th fieldname="XMDZ" rowspan="2" colindex=10 maxlength="10">&nbsp;项目地址&nbsp;</th>
				<th fieldname="XMDZ" rowspan="2" colindex=11 CustomFunction="doDz">&nbsp;&nbsp;</th>
				<th colspan="4">年度总投资额（元）</th>
				<th fieldname="XJXM" rowspan="2" colindex=16 CustomFunction="isxj" tdalign="center">&nbsp;是否被续建&nbsp;</th>
				<th fieldname="XJXM" rowspan="2" colindex=17 maxlength="10">&nbsp;续建项目&nbsp;</th>
			</tr>
			<tr>
				<th fieldname="GC" colindex=12 tdalign="right">&nbsp;工程&nbsp;</th>
				<th fieldname="ZC" colindex=13 tdalign="right">&nbsp;征拆&nbsp;</th>
				<th fieldname="QT" colindex=14 tdalign="right">&nbsp;其他&nbsp;</th>
				<th fieldname="JHZTZE" colindex=15 tdalign="right">&nbsp;合计&nbsp;</th>
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
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>