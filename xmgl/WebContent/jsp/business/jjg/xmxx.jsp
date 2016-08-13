<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>insertDemo</title>
<script type="text/javascript" charset="utf-8">

var controllername= "${pageContext.request.contextPath }/jjg/jjgwhController.do";
var data1="";
var json,id;


//查询
$(function() {
	var btn=$("#chaxun");
	btn.click(function() 
	{
		//生成json串
		var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query_xmxx",data,DT1);
	});
	//清空查询表单
    var btn_clearQuery=$("#query_clear");
    btn_clearQuery.click(function() {
        $("#queryForm").clearFormResult();
        generateJhNdMc($("#ND"),null);
        document.getElementById('num').value='1000';
        //其他处理放在下面
    });
});      


//确定
$(function() {
	var queding=$("#queding");
	queding.click(function(obj) {
		if(json==null||json=='undefined')
		{
			requireSelectedOneRow();
		}
		else
		{
			parent.$("body").manhuaDialog.setData(JSON.stringify(json));
			parent.$("body").manhuaDialog.sendData();
			parent.$("body").manhuaDialog.close();
		}
	});   
});


//获取行数据为 对象
function tr_click(obj){
	json=obj;	
}


//初始化加载
$(document).ready(function() {
   	generateJhNdMc($("#ND"),null);
   	g_bAlertWhenNoResult=false;
   	 var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query_xmxx",data,DT1);
	g_bAlertWhenNoResult=true;
   }
);
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
  	<div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">
	    	<h4 class="title">项目信息
		      	<span class="pull-right">  
		      		<button id="queding" class="btn" type="button">确定</button> 
		      	</span>
		 	</h4>
			<form method="post" id="queryForm">
				<table class="B-table">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR  style="display:none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<="/>
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
				    <tr>
				      	<th width="5%" class="right-border bottom-border">项目名称</th>
				     	<td width="20%" class="bottom-border">
				      		<input class="span12" type="text" placeholder="" name="XMMC" check-type="maxlength" maxlength="100" fieldname="xdk.XMMC" operation="like" logic="and"/>
				      	</td>
				     	<th width="5%" class="right-border right-border bottom-border text-right">年份</th>
				     	<td width="10%" colspan="1" class="left-border bottom-border text-right">
				        	<select class="span12" name="ND" id="ND" fieldname="xdk.ND" defaultMemo="全部" operation="="></select>
				      	</td>
				      	<td  class="text-left bottom-border text-right">
							<button	id="chaxun" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
				        	<button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
				      	</td>																				
				      	<td></td>
					</tr>
				</table>
			</form>
			<div class="overFlowX">
				<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
					<thead>
						<tr>
							<th name="XH" id="_XH" tdalign="center">&nbsp;#&nbsp;</th>
							<th fieldname="XMBH" rowspan="2" colindex=2 tdalign="center">&nbsp;项目编号&nbsp;</th> 
							<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="XMLX" tdalign="center">&nbsp;项目类型&nbsp;</th>
							<th fieldname="BDMC" maxlength="15">&nbsp;标段名称&nbsp;</th>
					  	</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id="queryXML"/>
		<input type="hidden" name="txtXML" id="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" fieldname="bd.BDBH,bd.LRSJ" id="txtFilter"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">		
	</FORM>
</div>
</body>
</html>