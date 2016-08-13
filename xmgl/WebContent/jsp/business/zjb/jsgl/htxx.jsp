<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<title>合同首页</title>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/jieSuanGuanliController.do";

//页面初始化
$(function() {
	init(); 
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryhtxx",data,DT1);
	});
	//清空查询条件
	var btn_clearQuery = $("#btnClear");
	btn_clearQuery.click(function() 
	  {
	    $("#queryForm").clearFormResult();
	   /*  setDefaultOption($("#QQDNF"),new Date().getFullYear()); */
	  //  init();
	  });
});

//页面默认参数
function init(){
	//getNdMR();
	/* setDefaultOption($("#QQDNF"),new Date().getFullYear()); */
	//生成json串
	g_bAlertWhenNoResult=false;	
	setPageHeight();
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryhtxx",data,DT1);
	g_bAlertWhenNoResult=true;	
}
/* //默认年度
function getNdMR(){
    	var d=new Date();
    	var year=d.getFullYear();
    	$("#QQDNF").val(year);
} */
//计算本页表格分页数
function setPageHeight(){
	var getHeight=getDivStyleHeight();
	var height = getHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
}


</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">
				合同信息
			</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>
					   <th width="5%" class="right-border bottom-border text-right" >年度</th>
						<td width="15%" class="right-border bottom-border">
							<select class="span12" id="QQDNF" name="QDNF" fieldname="QDNF" operation="=" kind="dic" src="T#GC_HTGL_HT:distinct qdnf :qdnf zz:SFYX='1' order by  zz  "  defaultMemo="全部">
							</select>
						</td>
						 <td class="text-left bottom-border text-right" colspan="2">
	                        <button id="btnQuery" class="btn btn-link"  type="button" ><i class="icon-search"></i>查询</button>
           					<button id="btnClear" class="btn btn-link" type="button" ><i class="icon-trash"></i>清空</button>
			            </td>	
					</tr>
				</table>
			</form>
			<div style="height:5px;"> </div>	
			<div class="overFlowX">
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single"  pageNum="18">
	                <thead>
	                	<tr>
	                		<th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
<%--							<th fieldname="ZTBID" colindex=2 tdalign="center" maxlength="36">&nbsp;招投标编号&nbsp;</th>--%>
							<th fieldname="HTBM" colindex=3 tdalign="center" maxlength="15" >&nbsp;合同编码&nbsp;</th>
							<th fieldname="HTMC" colindex=4 tdalign="center" maxlength="15">&nbsp;合同名称&nbsp;</th>
							<th fieldname="HTLX" colindex=1 tdalign="center" maxlength="15">&nbsp;合同类型&nbsp;</th>
							<th fieldname="ZHTQDJ" colindex=39 tdalign="center">&nbsp;合同签订价(元)&nbsp;</th>
							<th fieldname="YFDW" colindex=23 tdalign="center" maxlength="36">&nbsp;乙方单位&nbsp;</th>
							<th fieldname="HTJSRQ" colindex=10 tdalign="center">&nbsp;合同结算/结束日期&nbsp;</th>
	                	</tr>
	                </thead>
	              	<tbody></tbody>
	           </table>
	       </div>
	 	</div>
	</div>     
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" fieldname="GHH.LRSJ" id="txtFilter"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
</div>
</body>
</html>