<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<title>项目储备库  综合页</title>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script type="text/javascript" charset="utf-8">
 
var controllername= "${pageContext.request.contextPath }/xmcbk/xmcbkwhController.do";
var json,id,zt,pch;


//初始化查询
$(document).ready(function(){
	setPageHeight();
	generateNd($("#ND"));
	//setDefaultOption($("#ND"),new Date().getFullYear());
	setDefaultNd("ND");
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?query_cbk",data,DT1,null,true);
});

//计算本页表格分页数
function setPageHeight(){
	//计算本页表格分页数
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	
	/* var getHeight=getDivStyleHeight();
	var height = getHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum); */
}


//年份查询
function generateNd(ndObj){
	ndObj.attr("src","T#GC_TCJH_XMCBK:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(new Date().getFullYear());
}


//查询
$(function() {
	var btn=$("#example_query");
	btn.click(function() {
		//生成json串
        id=null;
		var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query_cbk",data,DT1,null,false);
	});
	
	
	//清空查询表单
    var btn_clearQuery=$("#query_clear");
    btn_clearQuery.click(function() {
        $("#queryForm").clearFormResult();
    	setDefaultNd("ND");
        //setDefaultOption($("#ND"),new Date().getFullYear());
    	//setDefaultOption($("#ND"),new Date().getFullYear());
        //其他处理放在下面
    });

    
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		  if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
		      printTabList("DT1");
		  }
	});

	
	//自动完成项目名称模糊查询
	showAutoComplete("XMMC",controllername+"?xmmcAutoQuery","getXmmcQueryCondition"); 
});


//生成项目名称查询条件
function getXmmcQueryCondition(){
	var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":"pxh"}]}';
	var jsonData = convertJson.string2json1(initData); 
 	//项目名称
	if("" != $("#XMMC").val()){
		var defineCondition = {"value": "%"+$("#XMMC").val()+"%","fieldname":"XMMC","operation":"like","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
    //年度
	if("" != $("#ND").val()){
		var defineCondition = {"value": $("#ND").val(),"fieldname":"ND","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	//项目类型
	if("" != $("#XMLX").val()){
		var defineCondition = {"value": $("#XMLX").val(),"fieldname":"XMLX","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}	
	//项目类型
	if("" != $("#XMSX").val()){
		var defineCondition = {"value": $("#XMSX").val(),"fieldname":"XMSX","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	//
	if("" != $("#ISXD").val()){
		var defineCondition = {"value": $("#ISXD").val(),"fieldname":"ISXD","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
 	return JSON.stringify(jsonData);
}


//点击获取行对象
function tr_click(obj,tabListid){
	id=$(obj).attr("GC_TCJH_XMCBK_ID");
	zt=obj.ISXD;
	pch=obj.PCH;
	json=$("#DT1").getSelectedRow();
	json=JSON.stringify(obj);
	json=encodeURI(json); 
}


//弹出新增窗口
function OpenMiddleWindow_add(){
	$(window).manhuaDialog({"title":"项目储备库  >新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmcbk/add.jsp?flag=1","modal":"1"});
}


//弹出修改窗口
function OpenMiddleWindow_alter(){
	if(json!=null&&id!=null)
	{	
		if(zt==0)
		{
			$("#resultXML").val($("#DT1").getSelectedRow());
		 	$(window).manhuaDialog({"title":"项目储备库  >修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmcbk/add.jsp?id="+id+"&flag=0","modal":"1"});			
		}
		else
		{
			//xInfoMsg("该项目已经下达不能进行修改！")
			$("#resultXML").val($("#DT1").getSelectedRow());
		 	$(window).manhuaDialog({"title":"项目储备库  >修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmcbk/add.jsp?id="+id+"&flag=2","modal":"1"});			
		}	
	}
	else
	{
        requireSelectedOneRow();
	}	
}


//弹出下达窗口
function OpenMiddleWindow_xd(){
	 $(window).manhuaDialog({"title":"项目储备库  >下达审批","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmcbk/xdsp.jsp","modal":"1"});
}


//弹出结转窗口
function OpenMiddleWindow_jz(){
	 $(window).manhuaDialog({"title":"项目储备库  >项目结转","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmcbk/jz.jsp","modal":"1"});
}


//插入时列表行更新
function add(data)
{
	var subresultmsgobj1=defaultJson.dealResultJson(data);
	$("#DT1").insertResult(JSON.stringify(subresultmsgobj1),DT1,1);
	$("#DT1").cancleSelected();
	$("#DT1").setSelect(0);
	var index1 =$("#DT1").getSelectedRowIndex();
	json=$("#DT1").getSelectedRowJsonByIndex(index1);
	var tempJson=convertJson.string2json1(json);
	id=$(tempJson).attr("GC_TCJH_XMCBK_ID");
	zt=tempJson.ISXD;
	json=encodeURI(json); 
	generateNd($("#ND"));
	//setDefaultOption($("#ND"),new Date().getFullYear());
	setDefaultNd("ND");
}


//修改时列表行更新
function update(data)
{
	var index= $("#DT1").getSelectedRowIndex();
	var subresultmsgobj1=defaultJson.dealResultJson(data);
	$("#DT1").updateResult(JSON.stringify(subresultmsgobj1),DT1,index);
	$("#DT1").cancleSelected();
	$("#DT1").setSelect(index);
	generateNd($("#ND"));
	setDefaultNd("ND");
	//setDefaultOption($("#ND"),new Date().getFullYear());
	json=$("#DT1").getSelectedRowJsonByIndex(index);
	var tempJson=convertJson.string2json1(json);
	id=$(tempJson).attr("GC_TCJH_XMCBK_ID");
	zt=tempJson.ISXD;
	json=encodeURI(json); 
	xSuccessMsg("操作成功")
}


//结转后查询父页
function query()
{
	generateNd($("#ND"));
	setDefaultNd("ND");
	//setDefaultOption($("#ND"),new Date().getFullYear());
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?query_cbk",data,DT1,null,true);
	xSuccessMsg("操作成功");
}


/* function query_ND()
{
	generateNd($("#ND"));
	setDefaultOption($("#ND"),new Date().getFullYear());
}
 */
//新增后查询父页
function query_add()
{
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?query_cbk",data,DT1,"query_add_tj",true);
}


function query_add_tj()
{
	generateNd($("#ND"));
	setDefaultNd("ND");
	//setDefaultOption($("#ND"),new Date().getFullYear());
	$("#DT1").cancleSelected();
	$("#DT1").setSelect(0);
	var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#DT1").getSelectedRow();
	var odd=convertJson.string2json1(rowValue);
	id=odd.GC_TCJH_XMCBK_ID;
	zt=odd.ISXD;
	pch=odd.PCH;
	json=$("#DT1").getSelectedRow();
	json=JSON.stringify(odd);
	json=encodeURI(json); 
}

//清空表单，取消选择行
function canclerow()
{
	$("#DT1").cancleSelected();
	id=null;
}


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


//列表项已下达加批次
function Dopc(obj)
{
	var isxd = obj.ISXD;
	var xdlx = obj.XDLX;
	if(isxd=='1')
	{
		if(obj.PCH=='')
		{	
			return "是"+"&nbsp;(无)";
		}
		else
		{
			switch(parseInt(xdlx))
			{
				case 1:
					return "是"+"&nbsp;("+parseInt(obj.PCH)+")";
				break;
				case 2:
					return "是"+"&nbsp;("+obj.PCH_SV+")";	
				break;
				default:
					return "是"+"&nbsp;(无)";		
			}
		}	
	}
}


//列表项已下达加属性
function Dosx(obj)
{
	var isxd = obj.ISXD;
	var xdsx = obj.XMSX;
	if(isxd=='1')
	{
		return obj.XMSX_SV;
	}
	else
	{
		return "—";
	}	
}
function cks(obj){
	 return "<input type='checkbox'  name='subBox' id='fuxuan' value='"+obj.GC_TCJH_XMCBK_ID+"'>"; 
}

$(function() {
    $("#checkAll").click(function() {
         $('input[name="subBox"]').prop("checked",this.checked); 
     });
     var sub = $("input[name='subBox']");
     sub.click(function(){
         $("#checkAll").prop("checked",sub.length == $("input[name='subBox']:checked").length ? true : false);
     });
 });
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">
				项目储备库  
				<span class="pull-right">
					<app:oPerm url="jsp/business/xmcbk/add.jsp?flag=true">
						<button class="btn" onclick="OpenMiddleWindow_add()" >新增</button>
					</app:oPerm>
					<app:oPerm url="jsp/business/xmcbk/add.jsp?">
						<button class="btn" onclick="OpenMiddleWindow_alter()">修改</button>
					</app:oPerm>
					<app:oPerm url="jsp/business/xmcbk/xd.jsp">
					</app:oPerm>
					<app:oPerm url="jsp/business/xmcbk/jz.jsp">
						<button class="btn" onclick="OpenMiddleWindow_jz()">项目结转</button>
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
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>
						<th width="5%" class="right-border bottom-border">年度</th>
						<td class="right-border bottom-border">
							 <select id="ND" class="span12 year" name="ND" defaultMemo="全部" fieldname="ND" operation="=" ></select> 
						</td>
						<th width="5%" class="right-border bottom-border text-right">项目名称</th>
						<td width="20%" class="right-border bottom-border">
							<input class="span12" type="text" placeholder="" name="XMMC" fieldname="XMMC" check-type="maxlength" maxlength="500" operation="like" id="XMMC" autocomplete="off">
						</td>					
						<th width="5%" class="right-border bottom-border">项目类型</th>
						<td class="right-border bottom-border">
							<select	kind="dic" src="XMLX" id="XMLX" class="span12 4characters" name="XMLX" fieldname="XMLX" operation="=" defaultMemo="全部"></select>
						</td>
						<th width="5%" class="right-border bottom-border">项目属性</th>
						<td class="right-border bottom-border">
							<select kind="dic" src="XMSX" class="span12 2characters" id="XMSX" name="XMSX" fieldname="XMSX" operation="=" defaultMemo="全部"></select>
						</td>						
						<th width="5%" class="right-border bottom-border">是否下达</th>
						<td class="right-border bottom-border">
							<select kind="dic" src="XMZT" class="span12 2characters" id="ISXD" name="ISXD" fieldname="ISXD" operation="=" defaultMemo="全部"></select>
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
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" pageNum="5" printFileName="项目储备库">
	                <thead>		                		
	                    <tr>
	                    	<th  fieldname="GC_TCJH_XMCBK_ID" rowspan="2"  colindex=1 tdalign="center" CustomFunction="cks">
	                     	<input type="checkbox" name="fuxuans"  id="checkAll" > 
	                     	</th>
	                     	<th  name="XH" id="_XH" rowspan="2" colindex=2 tdalign="center">&nbsp;#&nbsp;</th>	
	                     	
	                     	<th fieldname="XMBH" rowspan="2" colindex=3 CustomFunction="rowView">&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" rowspan="2" colindex=4 maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="ISXD" rowspan="2" colindex=5 tdalign="center">&nbsp;是否下达&nbsp;</th>
							<!-- 
							<th fieldname="PCH" rowspan="2" tdalign="center" colindex=5>&nbsp;批次号&nbsp;</th>
							 -->
							<th fieldname="XMLX" rowspan="2" colindex=6 tdalign="center">&nbsp;项目类型&nbsp;</th>
							<th fieldname="XMSX" rowspan="2" colindex=7  CustomFunction="Dosx" tdalign="center">&nbsp;项目属性&nbsp;</th>
							<th fieldname="ISBT" rowspan="2" colindex=8 tdalign="center">&nbsp;BT&nbsp;</th>
							<th fieldname="XMDZ" rowspan="2" colindex=9 maxlength="15">&nbsp;项目地址&nbsp;</th>
							<th colspan="4">&nbsp;年度总投资额（万元）&nbsp;</th>
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
		<input type="hidden" name="txtFilter" order="desc" fieldname="XMBH"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
</div>
</body>
</html>