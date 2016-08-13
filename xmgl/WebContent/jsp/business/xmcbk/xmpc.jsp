<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>项目批次</title>
<%
	String num=request.getParameter("num");
%>
<script type="text/javascript" charset="utf-8">

var controllername= "${pageContext.request.contextPath }/xmcbk/xmcbkwhController.do";
var json,id;


//初始化查询
$(document).ready(function(){
	setPageHeight();
	generateNd($("#ND"));
	setDefaultNd("ND");
	//setDefaultOption($("#ND"),new Date().getFullYear());
	generatePc($("#PCH"));
	newpch();
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?query_xmpc",data,DT1,"query_fz",true);
});


//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
}

//年份查询
function generateNd(ndObj){
 	ndObj.attr("src","T#GC_CBK_PCB:distinct to_char(xdrq,'YYYY'):to_char(xdrq,'YYYY') as nnd:SFYX='1' and  isxd='1' order by nnd asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(new Date().getFullYear());
}


//批次查询
function generatePc(pcObj){
	pcObj.attr("src","T#GC_CBK_PCB:PCH:PCH pch_px: SFYX='1' and isxd='1' and to_char(xdrq,'YYYY')='"+$("#ND").val()+"'  group by pch order by pch_px asc");
	pcObj.attr("kind","dic");
	pcObj.html('');
	reloadSelectTableDic(pcObj);
} 


//查询
$(function() {
	var btn=$("#example_query");
	btn.click(function() {
		var data = getQueryCondition();
		//调用ajax插入
 		defaultJson.doQueryJsonList(controllername+"?query_xmpc",data,DT1,"query_fz",false);
 	});
	
	
	//清空查询表单
    var btn_clearQuery=$("#query_clear");
    btn_clearQuery.click(function() {
        $("#queryForm").clearFormResult();
        generateNd($("#ND"));
        setDefaultNd("ND");
        //setDefaultOption($("#ND"),new Date().getFullYear());
		$("#syfyj").attr("checked",false);
		$("#syyj").attr("checked",false);
		generatePc($("#PCH"));
        //其他处理放在下面
    });
    
    
  //按钮绑定事件（导出EXCEL）
    $("#btnExpExcel").click(function() {
    	if( exportRequireQuery($("#DT1")))
     	 printTabList("DT1");
    });
  
  
    //按钮绑定事件（排序管理）
    $("#pxgl").click(function() {
    	$(window).manhuaDialog({"title":"储备库下达批次>排序管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmcbk/pxgl.jsp","modal":"1"});
    });
 
  
	//checkbox绑定事件(所有非应急)
    $("#syfyj").click(function() {
    	if(this.checked){
    		makePcAll();
    	}
    });
	
	
  	//checkbox绑定事件(所有应急)
    $("#syyj").click(function() {
    	if(this.checked){
    		makePcAll();
    	}
    });
  	
  	
    //批次选定事件
    $("#PCH").change(function() {
    	if('' != $(this).val()){
    		$("#syfyj").attr("checked",false);
    		$("#syyj").attr("checked",false);
    	}
    });  
    
    
    //监听年度变化事件
    $("#ND").change(function() {
    	generatePc($("#PCH"));
    	//$("#xdnd").val($("#ND").val());
    });  
 });


function query_fz()
{
	//为上传文件是需要的字段赋值
 	var obj=$("#queryResult").val();
    clearFileTab();
    if($("#PCH").val()!='')
    {
		var tempJson=convertJson.string2json1(obj);
		var resultobj=tempJson.response.data[0];
		queryFileData($(resultobj).attr("GC_CBK_PCB_ID"),"","","");	
    	var count = getFileCounts("0030");
    	if(count>0){
			$("#fj").hide();
			$("#_file").hide();
    	}
    	else
    	{
    		$("#fj").hide();
    	}	
    }
    else
    {
    	$("#fj").hide();
    }	
}

function query_file() {
 	var obj=$("#queryResult").val();
    if($("#PCH").val()!='')
	{
		var tempJson=convertJson.string2json1(obj);
		var resultobj=tempJson.response.data[0];
		var ywid = $(resultobj).attr("GC_CBK_PCB_ID");
    	var count = getFileCounts("0030");
    	if(count>0){

    		showAllFiles(ywid,"0030");
    	}
    	else
    	{
			  xAlert("提示信息","查无附件!",'3');
    	}	
    }
}


//查询出最新pch
function newpch(){
	var success=true;
	var actionName=controllername+"?query_newpch";
	$.ajax({
		url : actionName,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(result) {
		pch=result.msg;
		if(pch=='flag')
		{
			setDefaultOption($("#PCH"),'');
		}
		else
		{
			setDefaultOption($("#PCH"),pch);
		}	
		success=true;
		}
	});
    return success;
}


//点击获取行对象
function tr_click(obj,tabListid){
	id=obj.GC_TCJH_XMCBK_ID;
	json=$("#DT1").getSelectedRow();
	json=encodeURI(json); 
}

/**
//详细信息
function rowView(index){
    var obj_json=$("#DT1").getSelectedRowJsonByIndex(index);//获取行json串
	var obj=convertJson.string2json1(obj_json);//获取行对象
	var id_xxxx=obj.GC_TCJH_XMCBK_ID;//取行对象<项目编号>
	zt=obj.ISXD;
	xdlx=obj.XDLX;
	var pcid=obj.PCID;
	var pch=obj.PCH;//取行对象<项目批次号>
	$(window).manhuaDialog({"title":"项目储备库  >详细信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmcbk/xmxx.jsp?id="+id_xxxx+"&pch="+pch+"&zt="+zt+"&xdlx="+xdlx+"&pcid="+pcid,"modal":"1"});
}
*/
//详细信息
function rowView(obj){
    //var obj_json=$("#DT1").getSelectedRowJsonByIndex(index);//获取行json串
	//var obj=convertJson.string2json1(obj_json);//获取行对象
	var id_xxxx=obj.GC_TCJH_XMCBK_ID;//取行对象<项目编号>
	var zt=obj.ISXD;
	var xdlx=obj.XDLX;
	var pcid=obj.PCID;
	var pch=obj.PCH;//取行对象<项目批次号>
	var showStr = "";
	if((obj.XMBM).substring(0,5)=="XXXXX"){
		showStr = "<abbr title='"+obj.XMBM+"'>"+obj.XMBH+"</abbr>";
	}else if(obj.XMBM==""){
		showStr = obj.XMBH;
	}else{
		showStr = "<abbr title='"+obj.XMBM+"'>"+(obj.XMBM).substring(0,14)+"</abbr>";
	}
	return "<a href='javascript:void(0);' onclick='showInfoCard(this);' id_xxxx='"+id_xxxx+"' pch='"+pch+"' zt='"+zt+"' xdlx='"+xdlx+"' pcid='"+pcid+"'>"+showStr+"</a>";
}
//弹出项目信息卡
function showInfoCard(em){
	var obj = $(em);
	var id_xxxx=obj.attr("id_xxxx");//取行对象<项目编号>
	var zt=obj.attr("zt");
	var xdlx=obj.attr("xdlx");
	var pcid=obj.attr("pcid");
	var pch=obj.attr("pch");//取行对象<项目批次号>
	$(window).manhuaDialog({"title":"项目储备库>详细信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmcbk/xmxx.jsp?id="+id_xxxx+"&pch="+pch+"&zt="+zt+"&xdlx="+xdlx+"&pcid="+pcid,"modal":"1"});
}

//清空批次
function makePcAll(){
	$("#PCH").val('');
}


//由子页面操作后调用，更新父页面信息
function query()
{
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?query_xmpc",data,DT1,null,true);
}


//自定义查询条件
function getQueryCondition(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	return combineYjFyj(data);
}


/**
 * 根据全部应急还是全部非应急的选择，在原有的页面查询条件基础上增加查询条件
 * @param data 页面原有的查询条件
 * @param prefix 计划数据表的查询自定义前缀 
 */
function combineYjFyj(data){
	var jsonData = convertJson.string2json1(data); 
	if($("#syyj").is(':checked') && $("#syfyj").is(':checked')){
		return data;
	}
	if(!$("#syyj").is(':checked') && !$("#syfyj").is(':checked')){
		return data;
	}
	var xflx = "1";
	if($("#syyj").is(':checked')){
		xflx = "2";
	}
	var defineCondition = {"value": xflx,"fieldname":"cbk.XDLX","operation":"=","logic":"and"};
	jsonData.querycondition.conditions.push(defineCondition);
	return JSON.stringify(jsonData);
}
</script> 
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">
				储备库下达批次
				<span class="pull-right">
					<button class="btn" id="queryFile" onclick="query_file()">查看附件</button>
					<app:oPerm url="jsp/business/xmcbk/pxgl.jsp">
						<button class="btn" id="pxgl">排序管理</button>
					</app:oPerm>
					<button class="btn" id="btnExpExcel">导出</button>
				</span>	
			</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">	
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
						<INPUT id="ISXD" type="text" class="span12" kind="text" fieldname="pcb.ISXD" value="1" operation="="/>
						<%if(num!=null)
							{%>
								<INPUT id="num" type="text" class="span12" kind="text" fieldname="rownum" value=<%=num%> operation="<="/>
						<%
							}else
							{ %>	
								<INPUT id="num" type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<="/>
						<%}%>
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>
						<th width="5%" class="right-border bottom-border">年度</th>
						<td width="7%" class="right-border bottom-border">
							<select	id="ND" class="span12 year" name="ND" fieldname="to_char(xdrq, 'YYYY')" defaultMemo="全部" operation="="></select>
						</td>
						<th width="3%" class="right-border bottom-border text-right">全部项目</th>
						<td class="right-border bottom-border text-center" width="7%">
							<label class="checkbox inline" style="font-weight: bold;"> 
								<input type="checkbox" id="syfyj" name="XDLX" operation="=" class="text-right"/>非应急
							</label>
						</td>
						<td class="right-border bottom-border text-center" width="7%">
							<label class="checkbox inline" style="font-weight: bold;"> 
								<input type="checkbox" id="syyj" name="XDLX" operation="=" class="text-right"/>应急
							</label>
						</td>
						<th width="5%" class="right-border bottom-border">批次号</th>
						<td width="8%" class="right-border bottom-border">
							<select id="PCH"  class="span12 3characters" name="PCH" fieldname="pcb.PCH" operation="=" defaultMemo="全部"></select>
						</td>		     
						<td class="text-left bottom-border text-right">
							<button	id="example_query" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
	                        <button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
			            </td>
					</tr>
				</table>
			</form>
			<div style="height:5px;"> </div>
			<div class="overFlowX"> 
		        <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" pageNum="10" printFileName="储备库下达批次">
		        	<thead>
	                    <tr>
	                     	<th id="_XH" name="XH" rowspan="2" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>	
	                     	<th fieldname="XMBH" rowspan="2" colindex=2 CustomFunction="rowView">&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" rowspan="2" colindex=3 maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="PCH" rowspan="2" tdalign="center" colindex=4>&nbsp;批次号&nbsp;</th>
							<th fieldname="XMLX" rowspan="2" colindex=5 tdalign="center">&nbsp;项目类型&nbsp;</th>
							<th fieldname="XMSX" rowspan="2" colindex=6 tdalign="center">&nbsp;项目属性&nbsp;</th>
							<th fieldname="ISBT" rowspan="2" colindex=7 tdalign="center">&nbsp;BT&nbsp;</th>
							<th fieldname="XMDZ" rowspan="2" colindex=8 maxlength="15" tdalign="left">&nbsp;项目地址&nbsp;</th>
							<th colspan="4" tdalign="right">&nbsp;年度总投资额（万元）&nbsp;</th>
							<th fieldname="QY" rowspan="2" colindex=13 maxlength="15">&nbsp;项目来源&nbsp;</th>
	                    </tr>
	                    <tr>
							<th fieldname="GC" colindex=9 tdalign="right">&nbsp;工程&nbsp;</th>
							<th fieldname="ZC" colindex=10 tdalign="right">&nbsp;征拆&nbsp;</th>
							<th fieldname="QT" colindex=11 tdalign="right">&nbsp;其他&nbsp;</th>
							<th fieldname="JHZTZE" colindex=12 tdalign="right">&nbsp;合计&nbsp;</th>
	                	</tr>
			        </thead> 
		            <tbody></tbody>
		    	</table>
	    	</div>
		</div>
		<div class="B-small-from-table-autoConcise">
			<h4 id="fj" class="title" style="display:none;">批次附件信息</h4>
			<div id="_file">
				<table role="presentation" class="table table-striped">
					<tbody fjlb="0030" class="files showFileTab" onlyView="true" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
				</table>
			</div>
		</div>
	</div>		
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="asc" fieldname ="cbk.PXH"/>
		<input type="hidden" name="resultXML"/>
		<input type="hidden" name="queryResult" id="queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
	</FORM>
</div>
</body>
</html>