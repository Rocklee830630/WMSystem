<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>下达项目库-维护</title>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script src="${pageContext.request.contextPath }/js/common/FixTable.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
//页面初始化
$(function() {
	//自动完成项目名称模糊查询
	showAutoComplete("QXMMC",controllername+"?xmmcAutoCompleteToXmxdk","getXmmcQueryCondition"); 
	init();
	//按钮绑定事件(查询)
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
 	 	if($("#xdxmkForm").validationButton())
		{
			//可以添加提示信息	
		}else{
			return ;
		}
		if("" == $("#GC_TCJH_XMXDK_ID").val()) {
			requireSelectedOneRow();
			return;
		 }
		if($("#bdList").validationButton()){
			var ids='';
			$("input[type='checkbox']").each(function(){
				if($(this).is(':checked')){
					ids += $(this).attr('value')+',';
				}
			});
			ids=ids.substring(0,ids.length-1);
		    //生成json串
		    var data = Form2Json.formToJSON(xdxmkForm);
		  //组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  //调用ajax插入
		    defaultJson.doUpdateJson(controllername + "?insert&ids="+ids, data1,xdxmkList,"updateCallback");		    
		}else{
			requireFormMsg();
		  	return;
		}
	});
	//按钮绑定事件(下达统筹计划)
	$("#btnXdtcjh").click(function() {
		$(window).manhuaDialog({"title":"项目下达库>下达项目","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/xmxd.jsp"});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
    });
	
});
//修改数据回调函数
function updateCallback(){
	$(window).manhuaDialog.getParentObj().queryList();
}
//页面默认参数
function init(){
	getNd();
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
	switchbd();
}

//点击行事件
function tr_click(obj,tabListid){
	if(tabListid=="xdxmkList")
	{	
		$("#xdxmkForm").setFormValues(obj);
		var rowindex = $("#xdxmkList").getSelectedRowIndex();//获得选中行的索引
		var rowValue = $("#xdxmkList").getSelectedRow();//获得选中行的json对象
		deleteFileData(obj.GC_TCJH_XMXDK_ID,"",obj.SJBH,obj.YWLX);
		setFileData(obj.GC_TCJH_XMXDK_ID,"",obj.SJBH,obj.YWLX,"0");
		queryFileData(obj.GC_TCJH_XMXDK_ID,"",obj.SJBH,obj.YWLX);//业务ID，关联ID，事件编号，业务类型
		showXj_xdxmkid();
		if(obj.ISNATC==1)		//已下达 项目，项目性质不允许改变
		{
			$("#XJXJ").attr('disabled',true);
			$("#ISJXM").attr('disabled',true);
			$("#select_xj").attr('disabled',true);
			obj.XJXJ==1?$("#select_xj").hide():"";
			
		}
		else
		{
			$("#XJXJ").attr('disabled',false);	
			$("#ISJXM").attr('disabled',false);
			$("#select_xj").attr('disabled',false);
			obj.XJXJ==1?$("#select_xj").show():"";
		}
	}	
}

//弹出区域回调
getWinData = function(data){
	queryList_bd(JSON.parse(data).GC_TCJH_XMXDK_ID);
	$("#bdtr").attr("style","display: table-row;");		
	queryFileData(JSON.parse(data).GC_TCJH_XMXDK_ID,"",JSON.parse(data).SJBH,JSON.parse(data).YWLX);//业务ID，关联ID，事件编号，业务类型
	$("#WDD").val(JSON.parse(data).WDD);
	$("#XMGLGS").val(JSON.parse(data).XMGLGS);
	$("#YZDB").val(JSON.parse(data).YZDB);
	$("#XJ_XMID").val(JSON.parse(data).XMMC);
	$("#XJ_XMID").attr("code",JSON.parse(data).GC_TCJH_XMXDK_ID);
	var xjxj = $("#XJXJ").val(1);

};
//计算金额
function countHj(){
	var count = parseFloat($("#GC").val()==""?0:$("#GC").val())+parseFloat($("#ZC").val()==""?0:$("#ZC").val())+parseFloat($("#QT").val()==""?0:$("#QT").val());
	var subCount = count.toFixed(4);
	$("#JHZTZE").val(count.toFixed(4));
}
function formatFloat(src1, pos)
{
    return Math.round(src*Math.pow(10, pos))/Math.pow(10, pos).toFixed(1);
}

//列表项<项目地址>加图标
function doDz(obj){
	var xmdz = obj.XMDZ;
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
//默认年度
function getNd(){
	setDefaultNd("qnd");
	//setDefaultOption($("#qnd"),new Date().getFullYear());
}
//查看储备库信息
function queryCbk(){
	if($("#xdxmkList").getSelectedRowIndex()==-1)
	 {
		requireSelectedOneRow();
		return;
	 }
	var rowValue = $("#xdxmkList").getSelectedRow();//获得选中行的json对象
	var id = convertJson.string2json1(rowValue).XMCBK_ID;
	$(window).manhuaDialog({"title":"项目储备库>详细信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmcbk/xmxx_ty.jsp?id="+id,"modal":"2"});
}
//查询列表
function queryList(){
	$("#txtFilter").attr("fieldname","PXH,XMBH");
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,xdxmkList,"queryAfter",false);
	$("#xdxmkForm").clearFormResult();
}
//项目名称自动模糊查询参数
function getXmmcQueryCondition(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
	return data;
}
//选择下达库项目
function selectXdkxm(){
	$(window).manhuaDialog({"title":"选择下达库项目","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/xdxmk_List_xjxj.jsp?flag_xj=1","modal":"2"});
}
//改变新建/续建下拉框时显示/隐藏关联项目
function showXj_xdxmkid(){
	var xjxj = $("#XJXJ").val();
	var isjxm=$("#ISJXM").val();
	if(xjxj == "0"){//新建 -隐藏tr 非必填项
		//$("#XJ_XMID").attr("check-type","");
		$("#XJ_XMID").val("");
		$("#XJ_XMID").attr("code","");
		$("#tr_xjxdkxm").hide();
		$("#isyxm").hide();
		$("#ISJXM").attr("check-type","");
		$("#ISJXM").attr("placeholder","");
		$("#bdtr").attr("style","display: none;");	
		$("#XJ_XMID").attr("check-type","");
		$("#XJ_XMID").attr("placeholder","");
	}else if(xjxj == "1"){//续建 -显示tr 必填项
		//$("#XJ_XMID").attr("check-type","required");
		var xmid=$("#GC_TCJH_XMXDK_ID").val();
		queryList_bdxj($("#GC_TCJH_XMXDK_ID").val());
		$("#bdtr").attr("style","display: table-row;");		
		$("#tr_xjxdkxm").show();
		$("#isyxm").show();
		$("#select_xj").show();
		$("#XJ_XMID").attr("check-type","required");
		$("#XJ_XMID").attr("placeholder","必填");
		$("#ISJXM").attr("check-type","required");
		$("#ISJXM").attr("placeholder","必填");
		if(""==isjxm){
			$("#ISJXM").val('1');
		}
		isyjxm();
	}else{//请选择 -隐藏tr 非必填项
		//$("#XJ_XMID").attr("check-type","");
		$("#XJ_XMID").val("");
		$("#XJ_XMID").attr("code","");
		$("#tr_xjxdkxm").hide();
		$("#isyxm").hide();
		$("#bdtr").attr("style","display: none;");		
	}
}
//获取人员
function doCallback(obj){
	$("#YZDB").val(obj[0].USERNAME);
	$("#YZDB").attr("code",obj[0].ACCOUNT);
	$("#LXFS_YZDB").val(obj[0].TEL);
}
//计算金额
function countHj_ZT(){
	var count = parseFloat($("#ZTGC").val()==""?0:$("#ZTGC").val())+parseFloat($("#ZTZC").val()==""?0:$("#ZTZC").val())+parseFloat($("#ZTQT").val()==""?0:$("#ZTQT").val());
	//var subCount = count.toFixed(4);
	$("#ZTZTZE").val(count);
}
//jiangc 切换标段表显示隐藏
function switchbd()
{
	$("#bdtr").toggle();
}

//jiangc 添加复选框
function xz(obj)
{
	//alert(obj.ISGX);
	if(obj.ISGX==0)
	{
		var a='<input type="checkbox"  class="text-right bdid"  value="'+obj.GC_XMBD_ID+'">';
	}
	else
	{
		if($("#ISNATC").val()==1)
		{
			var a='<input type="checkbox" checked class="text-right bdid"  value="'+obj.GC_XMBD_ID+'" disabled>';
		}
		else
		{
			var a='<input type="checkbox" class="text-right bdid"  value="'+obj.GC_XMBD_ID+'" checked>';		
		}	
	}	
	return a;
}
// jiangc查询项目对应标段
function queryList_bd(id){
	$("#txtFilter").attr("fieldname","BDBH");
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm2,frmPost,bdList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query_bd&xmid="+id,data,bdList);
}
//jiangc查询续建标段
function queryList_bdxj(id){
	$("#txtFilter").attr("fieldname","BDBH");
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm2,frmPost,bdList);
	//调用ajax插入
	$("#ISNATC").val()==1?defaultJson.doQueryJsonList(controllername+"?query_bdxj&isxf=1&xmid="+id,data,bdList):defaultJson.doQueryJsonList(controllername+"?query_bdxj&isxf=0&xmid="+id,data,bdList);
}
function isyjxm(){
	
	var isjxm=$("#ISJXM").val();
	if(isjxm == "0"){//没有原项目 -隐藏tr 非必填项
		$("#XJ_XMID").val("");
		$("#XJ_XMID").attr("code","");
		$("#tr_xjxdkxm").hide();
		$("#bdtr").attr("style","display: none;");	
		$("#XJ_XMID").attr("check-type","");
		$("#XJ_XMID").attr("placeholder","");
	}else if(isjxm == "1"){//有原项目-显示tr 必填项
		queryList_bdxj($("#GC_TCJH_XMXDK_ID").val());
		$("#bdtr").attr("style","display: table-row;");		
		$("#tr_xjxdkxm").show();
		$("#select_xj").show();
		$("#XJ_XMID").attr("check-type","required");
		$("#XJ_XMID").attr("placeholder","必填");
	}else{//请选择 -隐藏tr 非必填项
		$("#XJ_XMID").val("");
		$("#XJ_XMID").attr("code","");
		$("#tr_xjxdkxm").hide();
		$("#bdtr").attr("style","display: none;");		
	}
}
function queryAfter(){
	var getHeight=getDivStyleHeight();
	getHeight=getHeight==0?611:getHeight;
 	var height = getHeight-pageTitle-pageQuery*2-getTableTh(2);
	var pageNum = parseInt(height/pageTableOne,10);
	
	var rows = $("tbody tr" ,$("#xdxmkList"));
	var tr_obj = rows.eq(0);
    var t = $("#xdxmkList").getTableRows();
    var tr_height = $(tr_obj).height();
    var d = t*(tr_height)+getTableTh(2)+20;
     if(d<200) d = 150;
    // 当高度大于400时，显示主页面列表的高度
    if(d>400) d = height;
    // 当没有数据的时候，只显示表头
 	if(tr_height==null) d = getTableTh(1)+20;
   
 	window_width = document.documentElement.clientWidth;//$("#allDiv").width()

 	$("#xdxmkList").fixTable({
		fixColumn: 4,//固定列数
		width:window_width-10,//显示宽度
		height:d//显示高度
	}); 
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			</TD>
        </TR>
         <tr>
          <th class="right-border bottom-border text-right">年度</th>
          <td class="right-border bottom-border" width="6%">
          	<select class="span12 year" id="qnd" name = "QND" fieldname = "XDK.ND" defaultMemo="全部" operation="=" kind="dic" src="T#GC_TCJH_XMXDK:distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE">
            </select>
          </td>
          <th class="right-border bottom-border text-right">项目名称</th>
          <td class="right-border bottom-border" width="20%">
          	<input class="span12" type="text" placeholder="" name="QXMMC"
				fieldname="XDK.XMMC" operation="like" id="QXMMC" autocomplete="off"
				tablePrefix="XDK"/>
		  </td>
		  <th class="right-border bottom-border text-right">新/续</th>
          <td class="right-border bottom-border">
            <select class="span12 2characters" name = "QXJXJ" fieldname = "XDK.XJXJ" defaultMemo="全部" operation="=" kind="dic" src="XMXZ">
            </select>
          </td>
          <th class="right-border bottom-border text-right">项目类型</th>
          <td class="right-border bottom-border" width="8%">
            <select class="span12 4characters" name = "QXMLX" fieldname = "XDK.XMLX" defaultMemo="全部" operation="=" kind="dic" src="XMLX">
            </select>
          </td>
          <th class="right-border bottom-border text-right">项目属性</th>
          <td class="bottom-border" width="8%">
          	<select class="span12 2characters" name = "QXMSX" fieldname = "XDK.XMSX" defaultMemo="全部" operation="=" kind="dic" src="XMSX">
            </select>
          </td>
          <th class="right-border bottom-border text-right">下达状态</th>
          <td class="bottom-border" width="8%">
          	<select class="span12 3characters" name = "ISNATC" fieldname = "XDK.ISNATC" defaultMemo="全部" operation="=" kind="dic" src="XDZT">
            </select>
          </td>
          <td class="text-left bottom-border text-right" rowspan="2">
           <button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
           <button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
          </td>
        </tr>
        <tr>
	           <th class="right-border bottom-border text-right">项目法人</th>
	          <td class="bottom-border" colspan="3">
	          	<select class="span8 " name = "XMFR" fieldname = "CBK.XMFR" defaultMemo="全部" operation="=" kind="dic" src="XMFR">
	            </select>
	          </td>
	           <th class="right-border bottom-border text-right">稳定度</th>
	          <td class="bottom-border">
	          	<select class="span12  " name = "WDD" fieldname = "XDK.WDD" defaultMemo="全部" operation="=" kind="dic" src="WDD">
	            </select>
	          </td>
	          <td colspan="6"></td>
	         
         </tr>
      </table>
      </form>
    <div style="height:5px;"></div>
    <div class="overFlowX">
		<table class="table-hover table-activeTd B-table" id="xdxmkList" width="100%" type="single" pageNum="5" editable="0">
		<thead>
			<tr>
				<th  name="XH" id="_XH" style="width:10px" rowspan="2" colindex=1 tdalign="center">#</th>
				<th fieldname="PXH" rowspan="2" colindex=2 tdalign="center">排序号</th>
				<th fieldname="XMBH" rowspan="2" colindex=3>项目编号</th>
				<th fieldname="XMMC" rowspan="2" colindex=4 maxlength="15">项目名称</th>
				<th fieldname="PCH" rowspan="2" colindex=5 tdalign="center">计财批</th>
				<th fieldname="ISNATC" rowspan="2" colindex=6 style="width:60px" tdalign="center" >下达</th>
				<th fieldname="XJXJ" rowspan="2" colindex=7 tdalign="center">新/续</th>
				<th fieldname="XMLX" rowspan="2" colindex=8 tdalign="center">项目类型</th>
				<th fieldname="XMFR" rowspan="2" colindex=9 maxlength="10">项目法人</th>
				<th fieldname="XMSX" rowspan="2" colindex=10 tdalign="center">项目属性</th>
				<th fieldname="WDD" rowspan="2" colindex=11 tdalign="center" CustomFunction="doWDD">稳定度</th>
				<th fieldname="ISBT" rowspan="2" colindex=12 tdalign="center">BT</th>
				<th fieldname="XMDZ" rowspan="2" colindex=13 maxlength="15">项目地址</th>
				<th fieldname="XMDZ" rowspan="2" colindex=14 CustomFunction="doDz"></th>
				<th colspan="4">年度总投资额（元）</th>
				<th fieldname="BZ" rowspan="2" maxlength="15" colindex="19">备注</th>
			</tr>
			<tr>
				<th fieldname="GC" colindex=15 tdalign="right">工程</th>
				<th fieldname="ZC" colindex=16 tdalign="right">征拆</th>
				<th fieldname="QT" colindex=17 tdalign="right">其他</th>
				<th fieldname="JHZTZE" colindex=18 tdalign="right">合计</th>
			</tr>
		</thead>
		<tbody></tbody>
		</table>
		</div>
	</div>
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">项目信息
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0);" onclick="queryCbk()"><span><i class="icon-zoom-in"></i>&nbsp;<font size="2">查看储备库信息</font></span></a>
      	<span class="pull-right">
	  		<button id="btnSave" class="btn" type="button">保存</button>
      		<button id="btnXdtcjh" class="btn" type="button">下达项目</button>
      	</span>
      </h4>
     <form method="post" id="xdxmkForm"  >
      <table class="B-table" width="100%">
	  	<input type="hidden" id="GC_TCJH_XMXDK_ID" fieldname="GC_TCJH_XMXDK_ID" keep="true" name = "GC_TCJH_XMXDK_ID" value=""/>
	  	<input type="hidden" id="ISNATC" fieldname="ISNATC" name = "ISNATC"/>
	  	<input type="hidden" fieldname="SJBH" name = "SJBH"/>
	  	<input type="hidden" fieldname="YWLX" name = "YWLX"/>
	  	<input type="hidden" fieldname="PCH" name = "PCH"/>
	  	<input type="hidden" fieldname="XMCBK_ID" name = "XMCBK_ID"/>
        <tr>
        	<th width="8%" class="right-border bottom-border text-right">项目编号</th>
 	 		<td class="bottom-border right-border" colspan=7>
 	 			<input class="span3" type="text" id="PRE_XMBM" fieldname="PRE_XMBM" disabled>
   				<input class="span9" id="XDK_XMBM"  type="text" keep="true" fieldname="XDK_XMBM" name = "XDK_XMBM" maxlength="100">
 	 		</td>
 	 	</tr>
 	 	<tr>
			<th width="8%" class="right-border bottom-border text-right">项目名称</th>
 	 		<td class="bottom-border right-border" colspan="3">
   				<input class="span12" id="XMMC"  type="text" placeholder="必填" keep="true"  check-type="required" fieldname="XMMC" name = "XMMC" maxlength="500">
 	 		</td>
 	 		
 			<th width="8%" class="right-border bottom-border text-right disabledTh">年度</th>
				<td class="bottom-border right-border">
  				
    			<input class="span12" id="ND"  type="text" fieldname="ND"  keep="true" name = "ND" readonly />
  			</td>
			<th class="right-border bottom-border text-right">是否按目标完成</th>
			<td  class="right-border bottom-border">
				<select class="span12 3characters" id="ISAMBWC" name="ISAMBWC" fieldname="ISAMBWC"  kind="dic" src="SF"  defaultMemo="请选择"></select>
			</td>			
  			<td colspan=1>
  			
  			</td>
        </tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right">项目性质</th>
        	<td class="bottom-border right-border" width="17%">
          		<select class="span12 4characters" id="XJXJ" kind="dic" src="XMXZ" check-type="required" fieldname="XJXJ" name="XJXJ" onchange="showXj_xdxmkid()">
            	</select>
          	</td>
	        <th width="8%" class="right-border bottom-border text-right">排序号</th>
			<td class="bottom-border right-border">
  				<input class="span12" id="PXH"  type="number" check-type="number" fieldname="PXH" style="text-align:right" name = "PXH" maxlength="17" min="0">
  			</td>
          	<th width="8%" class="right-border bottom-border text-right">项目类型</th>
            <td class="right-border bottom-border" width="17%">
	          	<select class="span12 4characters" id="XMLX" kind="dic" src="XMLX" check-type="required" fieldname="XMLX" name="XMLX">
	            </select>
            </td>
	        <th width="8%" class="right-border bottom-border text-right">是否BT</th>
          	<td class="bottom-border right-border" width="17%">
	          	<select class="span12 3characters" id="ISBT" kind="dic" src="SF" check-type="required" fieldname="ISBT" name="ISBT">
	            </select>
         	</td>
         </tr>
         <tr id="isyxm" style="display:none;">
         	<th width="8%" class="right-border bottom-border text-right">是否有原项目</th>
          	<td class="bottom-border right-border">
	          	<select class="span12 3characters" id="ISJXM" kind="dic" src="SF" check-type="required" fieldname="ISJXM" name="ISJXM" onchange="isyjxm()">
	            </select>
         	</td><td colspan="6"></td>
         </tr>
         <tr id="tr_xjxdkxm" style="display:none;">
         	<th width="8%" class="right-border bottom-border text-right">续建项目</th>
          	<td class="bottom-border right-border" colspan="3">
	          	<input class="span12" style="width:85%" type="text" id="XJ_XMID" fieldname="XJ_XMID" name="XJ_XMID" readonly>
          		<button id="select_xj" class="btn btn-link"  type="button" onclick="selectXdkxm()"><i title="选择项目" class="icon-edit"></i></button>
         	</td>
         	<td class="bottom-border right-border" colspan="2">
         		<a href="javascript:void(0)" onClick="switchbd()">续建标段</a>
         	</td>	
        </tr>
        <tr id="bdtr">
        	<td class="bottom-border right-border" colspan="8">
				<div class="B-small-from-table-autoConcise">
					<div style="overflow:auto;">
						<table class="table-hover table-activeTd B-table" id="bdList" width="100%" type="single" noPage="true" nopromptmsg="true" pageNum="1000">
							<thead>
			                    <tr>
			                     	<th id="_XH" name="XH" tdalign="center">&nbsp;#&nbsp;</th>	
									<th fieldname="BDBH" tdalign="center" CustomFunction="xz">&nbsp;操作&nbsp;</th>
									<th fieldname="BDMC" maxlength="15" type="text">&nbsp;标段名称&nbsp;</th>
									<th fieldname="BDBH" type="text">&nbsp;标段编号&nbsp;</th>
									<th fieldname="QDZH" type="text" tdalign="right">&nbsp;起点(桩号)&nbsp;</th>
									<th fieldname="ZDZH" type="text" tdalign="right">&nbsp;终点(桩号)&nbsp;</th>
								    <th fieldname="BDDD" type="text" tdalign="right">&nbsp;起止点&nbsp;</th>
								    <th fieldname="XMGLGS" type="dic" tdalign="right" >&nbsp;项目管理公司&nbsp;</th>
								    <th fieldname="JSGM" type="text">&nbsp;建设内容及规模&nbsp;</th>
									<th fieldname="CD" type="number">&nbsp;长度&nbsp;</th>
									<th fieldname="KD" type="number" tdalign="right">&nbsp;宽度&nbsp;</th>
									<th fieldname="GJ" type="number" tdalign="right">&nbsp;管径&nbsp;</th>
									<th fieldname="MJ" type="number" tdalign="right">&nbsp;面积&nbsp;</th>
									<th fieldname="GCZTFY" type="number" tdalign="right">&nbsp;工程主体费用（万元）&nbsp;</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>		  
				</div>
        	</td>
        </tr>
         <tr>
        	<th width="8%" class="right-border bottom-border text-right">项目地址</th>
          	<td class="bottom-border right-border" colspan="5">
	          	<input class="span12" style="width:85%" type="text" id="XMDZ"  check-type="required" placeholder="必填" fieldname="XMDZ" name="XMDZ" maxlength="500">
	          	<button class="btn btn-link"  type="button" onclick="selectDz()"><i title="查看" class="icon-map-marker"></i></button>
         	</td>
         	<th width="8%" class="right-border bottom-border text-right">稳定度</th>
        	<td class="bottom-border right-border" width="17%">
          		<select class="span12 4characters" id="WDD" kind="dic" src="WDD" fieldname="WDD" name="WDD">
            	</select>
          	</td>
        </tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">项目管理公司</th>
			<td class="bottom-border right-border">
				<select class="span12 4characters" id="XMGLGS" kind="dic" src="T#VIEW_YW_XMGLGS:ROW_ID:BMJC" fieldname="XMGLGS" name="XMGLGS">
	     		</select>
			</td>
			<th width="8%" class="right-border bottom-border text-right">业主代表</th>
 	 		<td class="bottom-border right-border">
 	 			<select class="span12 person" id="YZDB" kind="dic" src="T#VIEW_YW_ORG_PERSON:ACCOUNT:NAME:PERSON_KIND = '3' and DEPARTMENT in (select row_id from VIEW_YW_ORG_DEPT where EXTEND1='2')" fieldname="YZDB" name="YZDB">
	            </select>
   				<!-- 
   				<input class="span12 person" id="YZDB" type="text"  fieldname="YZDB" name = "YZDB" />
   				<button class="btn btn-link"  type="button" onclick="openUserTree('single','','doCallback')"><i class="icon-edit"></i></button>
   				 -->
 	 		</td>
 	 		<th class="right-border bottom-border text-right">项目属性</th>
			<td class="right-border bottom-border">
				<select class="span12" type="text"  placeholder="必填" check-type="required" id="XMSX" fieldname="XMSX" name="XMSX" kind="dic" src="XMSX"></select>
			</td>
 	 		<th class="right-border bottom-border text-right">是否可以编制</th>
			<td  class="right-border bottom-border">
				<select class="span12 3characters" id="SFKYBZ" kind="dic" src="SF" placeholder="必填" check-type="required" fieldname="SFKYBZ" name="SFKYBZ"></select>
			</td>	 		
		</tr>
		<tr>
			<th class="right-border bottom-border text-left disabledTh"  colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;项目总体投资</th>
		</tr>
		<tr>
		  <th width="8%" class="right-border bottom-border text-right">工程</th>
		  <td class="right-border bottom-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="ZTGC" type="number" onblur="countHj_ZT()" fieldname="ZTGC"  name = "ZTGC" min="0"><b>（元）</b>
          </td>
          <th width="8%" class="right-border bottom-border text-right">征拆</th>
          <td class="right-border bottom-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="ZTZC" type="number" onblur="countHj_ZT()" fieldname="ZTZC"  name = "ZTZC" min="0"><b>（元）</b>
         </td>
         <th width="8%" class="right-border bottom-border text-right">其他</th>
          <td class="bottom-border right-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="ZTQT" type="number" onblur="countHj_ZT()" fieldname="ZTQT"  name = "ZTQT" min="0"><b>（元）</b>
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">总投资额</th>
          <td class="right-border bottom-border" width="17%">
          <input class="span12" style="width:70%;text-align:right" id="ZTZTZE" type="number" fieldname="ZTZTZE" name = "ZTZTZE" readonly><b>（元）</b>
         </td>
		</tr>
		<tr>
			<th class="right-border bottom-border text-left disabledTh"  colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;项目年度投资</th>
		</tr>
        <tr>
         <th width="8%" class="right-border bottom-border text-right">工程</th>
          <td class="right-border bottom-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="GC" type="number" onblur="countHj()" fieldname="GC"  name = "GC" min="0"><b>（元）</b>
          </td>
          <th width="8%" class="right-border bottom-border text-right">征拆</th>
          <td class="right-border bottom-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="ZC" type="number" onblur="countHj()" fieldname="ZC"  name = "ZC" min="0"><b>（元）</b>
         </td>
         <th width="8%" class="right-border bottom-border text-right">其他</th>
          <td class="bottom-border right-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="QT" type="number" onblur="countHj()" fieldname="QT"  name = "QT" min="0"><b>（元）</b>
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">总投资额</th>
	        <td class="right-border bottom-border" width="17%">
	          <input class="span12" style="width:70%;text-align:right" id="JHZTZE" type="number" fieldname="JHZTZE" name = "JHZTZE" readonly><b>（元）</b>
	        </td>
		</tr>
		<tr>
	        <th width="8%" class="right-border bottom-border text-right">年度建设目标</th>
	        <td colspan="7" class="bottom-border right-border" >
	        	<textarea class="span12" id="JSMB" rows="3" name ="JSMB"  fieldname="JSMB" maxlength="4000"></textarea>
	        </td>
        </tr>
		<tr>
          <th width="8%" class="right-border bottom-border text-right">建设任务</th>
          <td colspan="7" class="bottom-border right-border">
          	<textarea class="span12" id="JSRW" rows="3" name ="JSRW"  fieldname="JSRW" maxlength="4000"></textarea>
          </td>
        </tr>
        <tr>
         <th width="8%" class="right-border bottom-border text-right">建设内容及规模</th>
          <td colspan="7" class="bottom-border right-border">
          	<textarea class="span12" id="JSNR" rows="3" name ="JSNR"  fieldname="JSNR" maxlength="4000"></textarea>
          </td>
        </tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right">建设意义</th>
	        <td colspan="7" class="bottom-border right-border" >
	        	<textarea class="span12" id="JSYY" rows="3" name ="JSYY"  fieldname="JSYY" maxlength="4000"></textarea>
	        </td>
        </tr>
        
        <tr>
	        <th width="8%" class="right-border bottom-border text-right">备注</th>
	        <td colspan="7" class="bottom-border right-border" >
	        	<textarea class="span12" id="BZ" rows="3" name ="BZ"  fieldname="BZ" maxlength="4000"></textarea>
	        </td>
        </tr>
        <tr>
        	<th width="8%" class="right-border bottom-border text-right">附件信息</th>
        	<td colspan="7" class="bottom-border right-border">
				<div>
					<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0035">
						<i class="icon-plus"></i>
						<span>添加文件...</span>
					</span>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0035" class="files showFileTab"
							data-toggle="modal-gallery" data-target="#modal-gallery">
						</tbody>
					</table>
				</div>
			</td>
        </tr>
      </table>
      </form>
    </div>
   </div>
  </div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "PXH,XMBH" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
         <input type="hidden" name="ywid" id = "ywid">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
	<form method="post" id="queryForm2" class="B-small-from-table-autoConcise">
	<!--可以再此处加入hidden域作为过滤条件 -->
		<TR style="display:none;">
			<TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border"></TD>
		</TR>
	</form>	
 	
 </div>
</body>

</html>