<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>项目批次</title>
<script type="text/javascript" charset="utf-8">

var controllername= "${pageContext.request.contextPath }/xmcbk/xmcbkwhController.do";
var json,id;


//初始化查询
$(document).ready(function(){
	
	generateNd($("#ND"));
	setDefaultNd("ND");
	//setDefaultOption($("#ND"),new Date().getFullYear());
	generatePc($("#PCH"));
	newpch();
	setPageHeight();
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?query_xmpc",data,DT1,null,true);
});

//计算本页表格分页数
function setPageHeight(){
	var getHeight=getDivStyleHeight();
	var height = getHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/40,10);
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
	pcObj.attr("src","T#GC_CBK_PCB:PCH:PCH pch_px: SFYX='1' and isxd='1' and  to_char(xdrq,'YYYY')='"+$("#ND").val()+"' group by pch order by pch_px asc");
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
        setDefaultNd("ND");
        //setDefaultOption($("#ND"),new Date().getFullYear());
		$("#syfyj").attr("checked",false);
		$("#syyj").attr("checked",false);
         document.getElementById('num').value='1000';
         generatePc($("#PCH"));
        //其他处理放在下面
    });
    
    
  	//checkbox绑定事件(所有批次)
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
	});  
});

function query_fz()
{
	//为上传文件是需要的字段赋值
 	var obj=$("#queryResult").val();
	var tempJson=convertJson.string2json1(obj);
	var resultobj=tempJson.response.data[0];	
}

//保存
$(function() {
	var btn=$("#save");
	btn.click(function() {
		var indexarry=new Array();
		indexarry=$("#DT1").getChangeRows();
		if(indexarry == "")
		 {
			requireSelectedOneRow();
			return
		 }
		//获取表格表头的数组,按照表格显示的顺序
		var tharrays=new Array();
		var comprisesData;
		tharrays=$("#DT1").getTableThArrays();
		if(tharrays != null){
			//组成json串,此串为修改行所属
			 comprisesData=$("#DT1").comprisesData(indexarry,tharrays);
			//调用ajax插入
			defaultJson.doUpdateBatchJson(controllername + "?update_pxh", comprisesData,DT1,indexarry,"update");
		}	
	});
});


//更新回调函数
function update()
{
	var data = getQueryCondition();
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query_xmpc",data,DT1,"query_fz",false);
	var parentmain=$(window).manhuaDialog.getParentObj();
	parentmain.query();
}

//点击获取行对象
function tr_click(obj,tabListid){
	id=obj.GC_TCJH_XMCBK_ID;
	json=$("#DT1").getSelectedRow();
	json=encodeURI(json); 
	queryFileData(obj.GC_CBK_PCB_ID,"","","");
}



//清空批次
function makePcAll(){
	$("#PCH").val('');
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


//查询出最新pch
function newpch(){
	var success=true;
	//var data1=combineQuery.getQueryCombineData(queryForm,frmPost,null);
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
			$("#PCH").val('');
		}
		else
		{
			$("#PCH").val(pch);
		}	
		success=true;
		}
	});
  return success;
}

	//详细信息
	function rowView(obj){
		var showStr = "";
		if((obj.XMBM).substring(0,5)=="XXXXX"){
			showStr = "<abbr title='"+obj.XMBM+"'>"+obj.XMBH+"</abbr>";
		}else if(obj.XMBM==""){
			showStr = obj.XMBH;
		}else{
			showStr = "<abbr title='"+obj.XMBM+"'>"+(obj.XMBM).substring(0,14)+"</abbr>";
		}
		return showStr;
	}
</script> 
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">
				排序管理
				<span class="pull-right">
					<button class="btn" id="save">保存</button>
				</span>	
			</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">	
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT id="ISXD" type="text" class="span12" kind="text" fieldname="pcb.ISXD" value="1" operation="="/>
							<INPUT id="num" type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<="/>
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>
						<th width="5%" class="right-border bottom-border text-right">年度</th>
						<td width="7%" class="right-border bottom-border">
							<select	id="ND" class="span12 year" name="ND" defaultMemo="全部" fieldname="to_char(xdrq, 'YYYY')" operation="="></select>
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
						<th width="5%"  class="right-border bottom-border">批次号</th>
						<td width="5%" class="right-border bottom-border">
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
		        <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" pageNum="10" editable="1">
		        	<thead>
	                    <tr>
	                     	<th id="_XH" name="XH" rowspan="2" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
	                     	<th fieldname="PXH" rowspan="2" colindex=2 type="number" tdalign="right" width="5%">&nbsp;排序号&nbsp;</th>	
	                     	<th fieldname="XMBH" rowspan="2" colindex=3 CustomFunction="rowView">&nbsp;项目编号&nbsp;</th>
							<th fieldname="PCH" rowspan="2" tdalign="center" colindex=4>&nbsp;批次号&nbsp;</th>
							<th fieldname="XMMC" rowspan="2" colindex=5 maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="XMLX" rowspan="2" colindex=6 tdalign="center">&nbsp;项目类型&nbsp;</th>
							<th fieldname="XMSX" rowspan="2" colindex=7 tdalign="center">&nbsp;项目属性&nbsp;</th>
							<th fieldname="ISBT" rowspan="2" colindex=8 tdalign="center">&nbsp;BT&nbsp;</th>
							<th fieldname="XMDZ" rowspan="2" colindex=9 maxlength="15" tdalign="left">&nbsp;项目地址&nbsp;</th>
							<th colspan="4" tdalign="right">&nbsp;年度计划总投资额（万元）&nbsp;</th>
							<th fieldname="QY" rowspan="2" colindex=14 maxlength="15">&nbsp;项目来源&nbsp;</th>
	                    </tr>
	                    <tr>
							<th fieldname="GC" colindex=10 tdalign="right">&nbsp;工程&nbsp;</th>
							<th fieldname="ZC" colindex=11 tdalign="right">&nbsp;征拆&nbsp;</th>
							<th fieldname="QT" colindex=12 tdalign="right">&nbsp;其他&nbsp;</th>
							<th fieldname="JHZTZE" colindex=13 tdalign="right">&nbsp;合计&nbsp;</th>
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
		<input type="hidden" name="txtFilter" order="asc" fieldname ="cbk.PXH"/>
		<input type="hidden" name="resultXML"/>
		<input type="hidden" id="queryResult"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
	</FORM>
</div>
</body>
</html>