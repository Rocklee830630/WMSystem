<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>履约保证金-履约保证金管理</title>

<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/lybzj/lybzjController.do";

//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#lybzjList").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
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
        //其他处理放在下面
    });
	
});
//页面默认参数
function init(){
	getNd();
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}
//默认年度
function getNd(){
	setDefaultOption($("#qnd"),new Date().getFullYear());
}
//查询列表
function queryList(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,lybzjList);
	defaultJson.doQueryJsonList(controllername+"?query",data,lybzjList);
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">履约保证金
      	<span class="pull-right">
      		
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
			<td class="right-border bottom-border" width="10%">
				<select class="span12 year" id="qnd" name = "QND"  fieldname = "t1.ND" defaultMemo="全部" operation="=" kind="dic" src="T#GC_JH_SJ:distinct ND:ND:SFYX='1'">
            	</select>
			</td>
			<th width="5%" class="right-border bottom-border text-right">项目名称</th>
			<td class="right-border bottom-border" width="20%">
				<input class="span12" type="text" placeholder="" name = "QXMMC" fieldname = "t.XMMC" operation="like" >
			</td>
			<th width="5%" class="right-border bottom-border text-right">项目管理公司</th>
			<td class="right-border bottom-border" width="10%">
				<select class="span12 2characters" id="XMGLGS" kind="dic" src="XMGLGS" defaultMemo="全部" fieldname="t1.XMGLGS" name="QXMGLGS" operation="=">
	            </select>
			</td>
			<td width="30%" class="text-left bottom-border text-right">
	           <button id="btnQuery" class="btn btn-link"  type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-search"></i>查询</button>
	           <button id="btnClear" class="btn btn-link" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-trash"></i>清空</button>
	          </td>
         </tr>
      </table>
      </form>
    <div style="height:5px;"> </div>
	<table class="table-hover table-activeTd B-table" id="lybzjList" width="100%" type="single" pageNum="10">
		<thead>
			<tr>
				<th  name="XH" id="_XH" style="width:10px">&nbsp;#&nbsp;</th>
				<th fieldname="XMMC">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDMC">&nbsp;标段名称&nbsp;</th>
				<th fieldname="ZBDW">&nbsp;交款单位&nbsp;</th>
				<th fieldname="HTMC">&nbsp;合同号&nbsp;</th>
				<th fieldname="HTJE">&nbsp;金额(元)&nbsp;</th>
				<th fieldname="JGYSSJ">&nbsp;竣工验收时间&nbsp;</th>
				<th fieldname="JSWJ">&nbsp;是否上报结算文件&nbsp;</th>
				<th fieldname="FHQK">&nbsp;是否返还&nbsp;</th>
				<th fieldname="FHRI">&nbsp;返还时间&nbsp;</th>
				<th fieldname="">&nbsp;操作&nbsp;</th>
				<th fieldname="">&nbsp;会签单附件&nbsp;</th>
			</tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
</div>
</div>
    
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "t1.XMBH,t.LRSJ"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>