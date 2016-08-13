<!DOCTYPE html>
<html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<head>
<title>长春市政府投资建设项目管理中心——综合管控中心</title>
<meta charset="utf-8">
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/echarts/asset/js/esl/esl.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/bmgk/bmgk.css?version=20140418">
<style type="text/css">
body {font-size:14px;}
h2 {display:inline; line-height:2em;}
.stageTable .blankTD{
	width:3%;
	padding:0px;
}
.stageTable .contentTD{
	width:14%;
	padding:0px;
	height:100%;
}
.stageTable .block-total{
	color:#FFF;
	font-weight:800;
	text-align:center;
	height:70px;
	min-height:0px;
	margin-left:4px;
}

.tab_title_ncjh{
	text-align:center;
	background:#34485e;
	color:#FFF;
	height:190px;
	font-size:20px;
}
.pieChart_content{
	position:absolute;
	z-index:-1;
	left:50%;
	top:50%;
	background:#FFF;
}
.pieChart_content>hr{
	margin-top:1px;
	margin-bottom:1px;
}
</style>
<script type="text/javascript">

var controllername = "${pageContext.request.contextPath }/gcgk/gcgkController.do";
var p_ec = null; 
require.config({
	paths:{ 
		'echarts' : '${pageContext.request.contextPath }/echarts/js/echarts',
		'echarts/chart/pie' : '${pageContext.request.contextPath }/echarts/js/echarts'
	}
});
function queryByxmlx(nd){
	var xmlx = "";
	$("input[name='Q_XMLX']:checkbox:checked").each(function() {
		xmlx += $(this).val() +"-";
	});
	if(''==xmlx){
		$("#queryJdgl1ChartDiv").html('');
		$("#queryJdgl2ChartDiv").html('');
	}else{
		queryJdgl1(nd,xmlx);
		queryJdgl2(nd,xmlx);	
	}
	
}
function doInit() {
	var nd=$("#ND").val();
	$(".ndText").text(nd);
	gcGcqsTcgk(nd);
	gcGcqsTxfx(nd);
	gcGcsxTcgk(nd);
	gcLybzjTcgk(nd);
	gcGcglTcgk(nd);
	gcGcjlzfZtqk(nd);
	gcGcjlzfTcgk(nd);
	gcJdglTcgk(nd);
	
	queryByxmlx(nd);
	gcLybzjLb();
}

function generateNd(ndObj){
	ndObj.attr("src","T#GC_JH_SJ:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(new Date().getFullYear());
}
// 工程部-工程洽商-统筹概况
function gcGcqsTcgk(nd){
	var action1 = controllername + "?gcGcqsTcgk&nd="+nd;
	$.ajax({
		url : action1,
		success: function(result) {
			insertTable(result,"GC_GCQS_TCGK");
		}
	});
}
// 工程部-工程甩项-统筹概况
function gcGcsxTcgk(nd) {
	$.ajax({
		url:	controllername+"?gcGcsxTcgk&nd="+nd,
		success:function(result) {
			insertTable(result, "GC_GCSX_TCGK");
		}
	});
}
// 工程部-履约保证金-统筹概况
function gcLybzjTcgk(nd) {
	$.ajax({
		url:	controllername+"?gcLybzjTcgk&nd="+nd,
		success:function(result) {
			insertTable(result, "GC_LYBZJ_TCGK");
		}
	});
}
// 工程部-工程管理-统筹概况
function gcGcglTcgk(nd) {
	$.ajax({
		url:	controllername+"?gcGcglTcgk&nd="+nd,
		success:function(result) {
			insertTable(result, "GC_GCGL_TCGK");
		}
	});
}
//工程部-工程洽商-图形分析
function gcGcqsTxfx(nd) {
	$.ajax({
		url:controllername+"?gcGcqsTxfx&nd="+nd,
		success: function(result) {
			insertTable(result, "GC_GCQS_TXFX");
		}
	});
}

// 工程部-工程计量及支付-总体情况
function gcGcjlzfZtqk(nd) {
	$.ajax({
		url : 	controllername + "?gcGcjlzfZtqk&nd="+nd,
		dataType : "json",
		async : false,
		success : function(result) {
		 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Column2D.swf", "GC_GCJLZF_ZTQK1", "100%", "250");  
		     myChart.setJSONData(result.msg);
		     myChart.render("GC_GCJLZF_ZTQK");
		}
	});
}
// 工程部-工程计量及支付-统筹概况
function gcGcjlzfTcgk(nd) {
	$.ajax({
		url:controllername+"?gcGcjlzfTcgk&nd="+nd,
		success: function(result) {
			insertTable(result, "GC_GCJLZF_TCGK");
		}
	});
}


//工程部-进度管理-统筹概况
function gcJdglTcgk(nd) {
	$.ajax({
		url:controllername+"?gcJdglTcgk&nd="+nd,
		success: function(result) {
			insertTable(result, "GC_JDGL_TCGK");
		}
	});
}
// 工程部-进度管理-列表
function gcJdglLb(nd) {
	var xmlx = $("#psgc").val();
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,GC_JDGL_LB);
	defaultJson.doQueryJsonList(controllername+"?gcJdglLb&nd="+nd+"&xmlx="+xmlx, data, GC_JDGL_LB, null, true);
}

//履约保证金列表
function gcLybzjLb() {
	var action1 = controllername + "?gcLybzjLb";
	$.ajax({
		url : action1,
		success: function(result)
		{
			addTable(result,"tabList1");
		}
	});
/* 	var data = combineQuery.getQueryCombineData(queryForm,frmPost,LybzjLb);
	defaultJson.doQueryJsonList(controllername+"?gcLybzjLb", data, LybzjLb, null, true); */
}
function addTable(result,tableID)
{
	$("#"+tableID+" tr").each(function(i){
		if(i>0){
			$(this).empty()
		}
	});
	var resultmsgobj = convertJson.string2json1(result);
	var resultobj = convertJson.string2json1(resultmsgobj.msg);
	var len = resultobj.response.data.length;
	var showHtml='';
	for(var i=0;i<len;i++){
		showHtml +="<tr>";
		$("#"+tableID+" tr th").each(function(j)
		{
			var subresultmsgobj = resultobj.response.data[i];
			var str = $(this).attr("bzfieldname");
			if(str=='ND') {
				showHtml+="<td class='mc' >"+$(subresultmsgobj).attr(str)+" </td>";
					
			}else{
				if("0"==$(subresultmsgobj).attr(str)){
					showHtml+='<td class="mc" style="text-align:center;"> '+$(subresultmsgobj).attr(str)+' </td>';
				}else{
					showHtml+='<td class="mc" style="text-align:center;"><a href="javascript:void(0);" onclick="openDetailLYBZJ(\'项目详细列表\',\''+str+'\',\''+$(subresultmsgobj).attr("ND")+'\')">'+$(subresultmsgobj).attr(str)+'</a></td>';
				}
			} 
		});
		showHtml+="</tr>";
	}
	$("#"+tableID).append(showHtml);
	$($("#"+tableID+" tbody>tr:even")).addClass("tableTrEvenColor");
	$("#"+tableID).addClass("tableList");
	$($("#"+tableID+" thead tr:eq(0)")).addClass("tableTheadTr");
      
}
//履约保证金LIST
function openDetailLYBZJ(title,name,nd){
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/gcLybzj_lb_List.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}
//根据结果放入表格
function insertTable(result,tableId){
	var resultmsgobj = convertJson.string2json1(result);
	var resultobj = convertJson.string2json1(resultmsgobj.msg);
	var subresultmsgobj = resultobj.response.data[0];
	$("."+tableId+" span").each(function(i){
		var str = $(this).attr("bzfieldname");
		
		if(str!=''&&str!=undefined){
			if($(subresultmsgobj).attr(str+"_SV")!=undefined){
				var valueStr = $(subresultmsgobj).attr(str+"_SV");
				if($(this).attr("decimal")=="false"){
					//不需要小数的项，删掉小数点后的内容
					valueStr = valueStr.substring(0,valueStr.lastIndexOf("."));
				}else if($(this).attr("absl")=="true"){
					//是否要处理掉符号（取绝对值）
					if(valueStr.indexOf("-")==0){
						valueStr = valueStr.replace("-","");
					}
				}
		     	$(this).html(valueStr);
			}else{
				//$(this).html($(subresultmsgobj).attr(str));
				var flag = $(this).attr("flag");
				if($(subresultmsgobj).attr(str)!==''&&$(subresultmsgobj).attr(str)!=undefined){
					if($(subresultmsgobj).attr(str)!=0){
						switch(flag) {
						case 'hasLink_jgzxfz_dx' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\'CBK_DETAIL**COMMON**jgzxfz_dx\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLink_jgzxfz_zx' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetailJH(\'项目详细列表\',\'TCJh_DETAIL**COMMON**jgzxfz_zx\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLink__jgzxfz_xjdx' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\'CBK_DETAIL**COMMON**jgzxfz_xjdx\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLink__jgzxfz_xjzx' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetailJH(\'项目详细列表\',\'TCJh_DETAIL**COMMON**jgzxfz_xjzx\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLink__jgzxfz_xujdx' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\'CBK_DETAIL**COMMON**jgzxfz_xujdx\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLink__jgzxfz_xujzx' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetailJH(\'项目详细列表\',\'TCJh_DETAIL**COMMON**jgzxfz_xujzx\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLinkTCJH' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetailJH(\'项目详细列表\',\'TCJh_DETAIL**GCB**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLink' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\'CBK_DETAIL**GCB**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLinkLybzj' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetailLybzj(\'项目详细列表\',\''+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLinkGcqs' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetailGcqs(\'项目详细列表\',\''+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLinkKfg' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetailKfg(\'项目详细列表\',\''+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLinkGcsx' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetailGcsx(\'项目详细列表\',\''+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'nohasLink' :
							var html=$(subresultmsgobj).attr(str);
							$(this).html(html);
							break;
						default:
							$(this).html('<a href="javascript:void(0);" style="color: #FFFF00" onclick="openDetailKfg(\'项目详细列表\',\''+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						}
					}else{
						$(this).html($(subresultmsgobj).attr(str));
					}
				}else{
					$(this).html("");
				}
			}
		}
	});
}


//年度项目分类情况
function queryJdgl1(nd,xmlx){
	var action = controllername + "?queryJdgl1&nd="+nd+"&xmlx="+xmlx;
	$.ajax({
		url : action,
		dataType:"json",
		async:false,
		success: function(result){
			if(result.msg==0){
				return;
			}else{
				var resObj = convertJson.string2json1(result.msg);
				var myChart = p_ec.init(document.getElementById("queryJdgl1ChartDiv"));
				myChart.setOption(resObj);
				createContentDiv(resObj,"queryJdgl1ContentDiv");
				var ecConfig = require('echarts/config');
				function eConsole(param) 
				{
					var nd = $("#ND").val();
					var  xmsc = {"title":'项目详细列表',"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/tcjhgttList.jsp?flag=1&nd="+nd+"&xmlx="+xmlx+"&tiaojian="+param.dataIndex,"modal":"1"};
					$(window).manhuaDialog(xmsc);
				}
				myChart.on(ecConfig.EVENT.CLICK, eConsole);
			}
		}
	});
}
//计划编制
function queryJdgl2(nd,xmlx){
	var action = controllername + "?queryJdgl2&nd="+nd+"&xmlx="+xmlx;
	$.ajax({
		url : action,
		dataType:"json",
		async:false,
		success: function(result){
			if(result.msg==0){
				return;
			}else{
				var resObj = convertJson.string2json1(result.msg);
				var myChart = p_ec.init(document.getElementById("queryJdgl2ChartDiv"));
				myChart.setOption(resObj);
				createContentDiv(resObj,"queryJdgl2ContentDiv");
				var ecConfig = require('echarts/config');
				function eConsole(param) 
				{
					var nd = $("#ND").val();
					var  xmsc = {"title":'项目详细列表',"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/tcjhgttList.jsp?flag=2&nd="+nd+"&xmlx="+xmlx+"&tiaojian="+param.dataIndex,"modal":"1"};
					$(window).manhuaDialog(xmsc);
				}
				myChart.on(ecConfig.EVENT.CLICK, eConsole);
			}
		}
	});
}
function createContentDiv(resObj,contentID){
	var showHtml = "";
	var dataObj = resObj.series[0].data;
	for(var i=0;i<dataObj.length;i++){
		if(i!=0){
			showHtml += "<br/><hr/>";
		}
		showHtml += '<span style="color:'+resObj.color[i]+';font-size:12px;">'+dataObj[i]['name']+'：'+dataObj[i]['value']+'</span>';
	}
	$("#"+contentID).html(showHtml);
	var styleStr = "margin-left:-"+($("#"+contentID).width()/2)+"px;margin-top:-"+($("#"+contentID).height()/2)+"px;";
	$("#"+contentID).attr("style",styleStr);
}
function openDetail(title,name){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/cbkList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}
function openDetailJH(title,name){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/tcjhList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}
/**
 * 工程部-履约保证金查详细
 */
function openDetailLybzj(title,name){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/gcLybzjList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}

/**
 * 工程部-工程洽商
 */
function openDetailGcqs(title,name) {
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/gcqsList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}

/**
 * 工程部-工程甩项
 */
function openDetailGcsx(title,name) {
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/gcsxList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}
//开复工链接
function openDetailKfg(title,name) {
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/kfgList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}
</script>
</head>
<body>
  <div class="container-fluid">
    <span class="pull-right">
      <button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
    </span>
    <div class="" style="border: 0px;">
      <h4 class="bmjkTitleLine">
      	开复工管理&nbsp;&nbsp;
      	<select class="span2 year" style="width: 8%" id="ND" name="ND"  fieldname="ND" operation="=" noNullSelect ="true"></select>
      </h4>
      <div class="container-fluid" style="padding:0px 0px;">
        <div class="row-fluid">
          <div class="span12 GC_GCGL_TCGK">
            <div class="bmjkTjgkBlock">
              <p> <span class="ndText"></span>年建管中心共负责<span flag="hasLink_jgzxfz_dx"  bzfieldname="ZBGL_TJGK_JGZX_DX"></span>项（<span flag="hasLink_jgzxfz_zx" bzfieldname="ZBGL_TJGK_JGZX_ZX"></span>个子项）重点工程。
              	其中，新建<span flag="hasLink__jgzxfz_xjdx" bzfieldname="ZBGL_TJGK_XINJ_DX"></span>项（<span flag="hasLink__jgzxfz_xjzx" bzfieldname="ZBGL_TJGK_XINJ_ZX"></span>个子项），
              	续建<span flag="hasLink__jgzxfz_xujdx" bzfieldname="ZBGL_TJGK_XUJ_DX"></span>项（<span flag="hasLink__jgzxfz_xujzx" bzfieldname="ZBGL_TJGK_XUJ_ZX"></span>个子项）。<br/>
              	截至目前，已开（复）工<span flag="hasLinkKfg" bzfieldname="GCGL_KFGGL_YKFG_ZX"></span>子项<span  flag="hasLinkKfg"  bzfieldname="GCGL_KFGGL_YKFG_BD"></span>标段，已完工<span  flag="hasLinkKfg"  bzfieldname="GCGL_KFGGL_YWG_ZX"></span>子项<span  flag="hasLinkKfg"  bzfieldname="GCGL_KFGGL_YWG_BD"></span>标段。
              </p>
            </div>
          </div>
          <div class="span12 GC_GCGL_TCGK" style="margin-left:0px;min-width:960px;">
            <table border="0" class="stageTable">
              <tr>
                <td class="blankTD">
					<div class="containerDiv-upToRight">
				    	<div class="lineDiv">
				      		<div class="line" style="height:66px"></div>
				    	</div>
				  	</div>
                </td>
                <td class="contentTD" title="已确定施工单位计划表施工单位实际时间有值的。考虑续建项目">
                  <div class="divTitle">前置条件</div>
				  <div class="divContent">
					已确定施工单位<br/>
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_YQDSGDW_ZX"></span>子项
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_YQDSGDW_BD"></span>标段
				  </div>
				</td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
              </tr>
              <tr>
                <td class="line-S blankTD">
                  <div class="div-S" style="height:100%;"></div>
                </td>
                <td class="contentTD" rowspan="3">
                  <div class="block-blue">
                  	建管中心工程部负责<br/>
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_JGZXFZ_ZX"></span>子项
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_JGZXFZ_BD"></span>标段
                  </div>
                </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-upToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD" title="暂不具备开工条件  新增功能，由工程部，对计划添加“具备”、“不具备”的情况">
                  <div class="block-red">
                  	暂缓实施<br/>
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_ZBJBKGTJ_ZX"></span>子项
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_ZBJBKGTJ_BD"></span>标段
                  </div>
                </td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
              </tr>
              <tr>
                <td class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                  <div class="div-S" style="height:100%;"></div>
                </td>
                <td class="line-H line-S blankTD">
                  <div class="div-H" style="width:50%;"></div>
                  <div class="div-S" style="height:100%;"></div>
                </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-upToDown" style="height:30px;">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-down"></div>
				  </div>
                </td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
              </tr>
              <tr>
                <td class="line-S blankTD">
                  <div class="div-S" style="height:100%;"></div>
                </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD" title="具备开工条件">
                  <div class="block-blue">
                  	具备开工条件<br/>
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_JBKGTJ_ZX"></span>子项
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_JBKGTJ_BD"></span>标段
                  </div>
                </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-upToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD">
                  <div class="block-red">
                  	未开工<br/>
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_WKFG_WKG_ZX"></span>子项
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_WKFG_WKG_BD"></span>标段
                  </div>
                </td>
                <td class="line-H line-S blankTD">
				  <div class="containerDiv-upToRight">
				    <div class="lineDiv">
						<div class="line"></div>
					</div>
					<div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD">
                  <div class="block-blue">
                  	已完工<br/>
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_WKFG_YWG_ZX"></span>子项
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_WKFG_YWG_BD"></span>标段
                  </div>
                </td>
                <td></td>
                <td></td>
              </tr>
              <tr>
                <td class="line-S blankTD">
                  <div class="div-S" style="height:100%;"></div>
                </td>
                <td class="contentTD"></td>
                <td class="blankTD">
					<div class="containerDiv-upToRight">
				    	<div class="lineDiv">
				      		<div class="line" style="height:66px"></div>
				    	</div>
				  	</div>
                </td>
                <td class="line-H line-S blankTD" style="height:20px">
                  <div class="div-H" style="width:53%;"></div>
                  <div class="div-S" style="height:50%;"></div>
                </td>
                <td class="line-S blankTD">
                  <div class="div-S" style="height:100%;"></div>
                </td>
                <td></td>
                <td class="line-S blankTD">
                  <div class="div-S" style="height:100%;"></div>
                </td>
                <td></td>
                <td></td>
                <td></td>
              </tr>
              <tr>
                <td class="line-S blankTD">
                  <div class="div-S" style="height:100%;"></div>
                </td>
                <td class="contentTD"></td>
                <td class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                  <div class="div-S" style="height:100%;"></div>
                </td>
                <td class="contentTD">
                  <div class="block-red">
                  	未报开(复)工令<br/>
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_WKFG_ZX"></span>子项
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_WKFG_BD"></span>标段
                  </div>
                </td>
                <td class="line-H line-S blankTD">
				  <div class="containerDiv-leftToRight">
				    <div class="lineDiv">
						<div class="line"></div>
					</div>
					<div class="triangle-right"></div>
				  </div>
                  <div class="div-S" style="height:100%;"></div>
                </td>
                <td class="contentTD">
                  <div class="block-blue">
                  	已开工<br/>
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_WKFG_YKG_ZX"></span>子项
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_WKFG_YKG_BD"></span>标段
                  </div>
                </td>
                <td class="line-H line-S blankTD">
				  <div class="containerDiv-leftToRight">
				    <div class="lineDiv">
						<div class="line"></div>
					</div>
					<div class="triangle-right"></div>
				  </div>
                  <div class="div-S" style="height:50%;"></div>
                </td>
                <td class="contentTD">
                	<div class="block-total">
	               		<div class="span12 title-M">在建跨年结转</div>
	               		<div class="span5 title-N"><span class="fieldValue-B" bzfieldname="GCGL_KFGGL_WKFG_ZJKNJZ_ZX"></span>子项</div>
	               		<div class="span7 title-Q">
	               			<div class="span12 title-O">已完<span class="fieldValue-B" bzfieldname="GCGL_KFGGL_WKFG_ZJKNJZ_YB_BD"></span>标段</div>
	               			<div class="span12 title-P">在建<span class="fieldValue-B" bzfieldname="GCGL_KFGGL_WKFG_ZJKNJZ_WB_BD"></span>标段</div>
	               		</div>
               		</div>
                </td>
                <td class="line-H line-S blankTD">
				 <div class="containerDiv-leftToRight">
				    <div class="lineDiv">
						<div class="line"></div>
					</div>
					<div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD">
                	<div class="block-total">
	               		<div class="span12 title-M">停工令情况</div>
	               		<div class="span5 title-N" title="统计年度已开工但未完工的子项（应办停工令子项）">
	               		<span class="fieldValue-B" bzfieldname="GCGL_KFGGL_WKFG_TGL_ZX"></span>子项</div>
	               		<div class="span7 title-Q">
	               			<div class="span12 title-O">已报<span class="fieldValue-B" bzfieldname="GCGL_KFGGL_WKFG_TGL_YB_BD"></span>标段</div>
	               			<div class="span12 title-P">未报<span class="fieldValue-B" bzfieldname="GCGL_KFGGL_WKFG_TGL_WB_BD"></span>标段</div>
	               		</div>
               		</div>
                </td>
              </tr>
              <tr>
                <td class="line-S blankTD">
                  <div class="div-S" style="height:100%;"></div>
                </td>
                <td></td>
                <td class="line-S blankTD">
                  <div class="div-S" style="height:100%;"></div>
                </td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
              </tr>
              <tr>
                <td class="line-S blankTD">
                  <div class="div-S" style="height:100%;"></div>
                </td>
                <td></td>
                <td class="line-S blankTD">
                  <div class="div-S" style="height:100%;"></div>
                </td>
                <td></td>
                <td class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD">
                  <div class="block-red">
                  	部分开工<br/>
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_WKFG_BFKG_ZX"></span>子项
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_WKFG_BFKG_BD"></span>标段,未开<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_WKFG_BFKG_WKBD"></span>标段
                  </div>
                </td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
              </tr>
              <tr>
                <td class="line-S blankTD">
                  <div class="div-S" style="height:100%;"></div>
                </td>
                <td></td>
                <td class="line-S blankTD">
                  <div class="div-S" style="height:100%;"></div>
                </td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
              </tr>
              <tr>
                <td rowspan="5" class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                  <div class="div-S" style="height:50%;"></div>
                </td>
                <td rowspan="5">
                  <div class="block-blue">
                  	其他单位负责</br>
                  <span class="fieldValue-A" bzfieldname="">0</span>子项
				  <span class="fieldValue-A" bzfieldname="">0</span>标段
                  </div>
                </td>
                <td rowspan="5" class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                  <div class="div-S" style="height:50%;"></div>
                </td>
                <td rowspan="5">
                  <div class="block-blue">
                  	已报开(复)工令<br/>
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_BYKFG_ZX"></span>子项
					已报<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_BYKFG_BD"></span>标段，未报<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_BYKFG_WKBD"></span>标段
                  </div>
                </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-upToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD" TITLE="已报开复工令未开工 前一条件下，计划表开工实际时间值为空的  所有的未开工是一个项目">
                  <div class="block-red">
                  	未开工<br/>
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_YKFG_WKG_ZX"></span>子项
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_YKFG_WKG_BD"></span>标段
                  </div>
                </td>
                <td class="line-H line-S blankTD">
				 <div class="containerDiv-upToRight">
				    <div class="lineDiv">
						<div class="line"></div>
					</div>
					<div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD">
                  <div class="block-blue">
                  	已完工<br/>
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_YKFG_YWG_ZX"></span>子项
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_YKFG_YWG_BD"></span>标段
                  </div>
                </td>
                <td></td>
                <td></td>
              </tr>
              <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
              </tr>
              <tr>
                <td class="line-H line-S blankTD">
				  <div class="containerDiv-leftToRight">
				    <div class="lineDiv">
						<div class="line"></div>
					</div>
					<div class="triangle-right"></div>
				  </div>
                  <div class="div-S" style="height:100%;"></div>
                </td>
                <td class="contentTD">
                  <div class="block-blue">
                  	已开工<br/>
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_YKFG_YKG_ZX"></span>子项
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_YKFG_YKG_BD"></span>标段
                  </div>
                </td>
                <td class="line-H line-S blankTD">
				 <div class="containerDiv-leftToRight">
				    <div class="lineDiv">
						<div class="line"></div>
					</div>
					<div class="triangle-right"></div>
				  </div>
                  <div class="div-S" style="height:50%;"></div>
                </td>
                <td class="contentTD">
                	<div class="block-total">
	               		<div class="span12 title-M">在建跨年结转</div>
	               		<div class="span5 title-N"><span class="fieldValue-B" bzfieldname="GCGL_KFGGL_YKFG_ZJKNJZ_ZX"></span>子项</div>
	               		<div class="span7 title-Q">
	               			<div class="span12 title-O">已完<span class="fieldValue-B" bzfieldname="GCGL_KFGGL_YKFG_ZJKNJZ_ZX_YWBD"></span>标段</div>
	               			<div class="span12 title-P">在建<span class="fieldValue-B" bzfieldname="GCGL_KFGGL_YKFG_ZJKNJZ_ZX_WWBD"></span>标段</div>
	               		</div>
               		</div>
                </td>
                <td class="line-H line-S blankTD">
				 <div class="containerDiv-leftToRight">
				    <div class="lineDiv">
						<div class="line"></div>
					</div>
					<div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD">
                	<div class="block-total">
	               		<div class="span12 title-M">停工令情况</div>
	               		<div class="span5 title-N" title="统计年度已开工但未完工的子项（应办停工令子项）"><span class="fieldValue-B" bzfieldname="GCGL_KFGGL_YKFG_TGL_ZX"></span>子项</div>
	               		<div class="span7 title-Q">
	               			<div class="span12 title-O">已报<span class="fieldValue-B" bzfieldname="GCGL_KFGGL_YKFG_YB_BD"></span>标段</div>
	               			<div class="span12 title-P">未报<span class="fieldValue-B" bzfieldname="GCGL_KFGGL_YKFG_WB_BD"></span>标段</div>
	               		</div>
               		</div>
                </td>
              </tr>
              <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td class="blankTD"></td>
                <td></td>
              </tr>
              <tr>
                <td class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD">
                  <div class="block-red">
                  	部分开工<br/>
					<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_YKFG_BFKG_ZX"></span>子项
					已开<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_YKFG_BFKG_BD"></span>标段，未开<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_YKFG_BFKG_WKBD"></span>标段
                  </div>
                </td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
              </tr>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
	<div class="" style="border: 0px;">
	  <h4 class="bmjkTitleLine">
		进度管理
	  </h4>
      <div class="container-fluid" style="padding:0px 0px;">
        <div class="row-fluid">
          <div class="span12 GC_JDGL_TCGK">
            <div class="bmjkTjgkBlock">
              <p>截至目前，<span class="ndText"></span>年建管中心应开工未开工<span  flag="hasLinkKfg"  bzfieldname="YKGWKG_ZX"></span>子项<span  flag="hasLinkKfg"  bzfieldname="YKGWKG_BD"></span>标段，
              	已开（复）工<span  flag="hasLinkKfg" bzfieldname="YKG_ZX"></span>子项<span  flag="hasLinkKfg"  bzfieldname="YKG_BD"></span>标段，
              	其中，延期开（复）工<span  flag="hasLinkKfg"  bzfieldname="YQKG_ZX"></span>子项<span  flag="hasLinkKfg"  bzfieldname="YQKG_BD"></span>标段。
              	已完工<span  flag="hasLinkKfg"  bzfieldname="YWG_ZX"></span>子项<span  flag="hasLinkKfg"  bzfieldname="YWKG_BD"></span>标段，
              	其中，延期完工<span   flag="hasLinkKfg"  bzfieldname="YQWG_ZX"></span>子项<span  flag="hasLinkKfg"  bzfieldname="YQWG_BD"></span>标段。
              </p>
            </div>
          </div>
        </div>
   		<div class="row-fluid">
       	  <div class="span12">
			<div class="B-small-from-table-autoConcise">
				<table class="B-table" style="border:0;width: 100%;">
				  <tr>
				  	<td><label><input id="psgc" style="margin:0px 4px 0px 4px;" type="checkbox" checked="checked" name="Q_XMLX" value="2"><b>排水工程</b></label></td>
				  	<td><label><input style="margin:0px 4px 0px 4px;" type="checkbox" checked="checked" name="Q_XMLX" value="1"><b>道路工程</b></label></td>
				  	<td><label><input style="margin:0px 4px 0px 4px;" type="checkbox" checked="checked" name="Q_XMLX" value="3"><b>桥梁工程</b></label></td>
				  	<td><label><input style="margin:0px 4px 0px 4px;" type="checkbox" checked="checked" name="Q_XMLX" value="4"><b>框构（下穿）工程</b></label></td>
				  </tr>
				</table>
			</div>
       	 </div>
 		</div>
 		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span4" style="position:relative;min-width:415px;">
				<div id="queryJdgl1ContentDiv" class="pieChart_content">
				</div>
				<div id="queryJdgl1ChartDiv" style="height:200px;min-width:415px;"></div>
			</div>
			<div class="span2">
			</div>
			<div class="span4" style="margin-left:0px;position:relative;min-width:415px;">
				<div id="queryJdgl2ContentDiv" class="pieChart_content">
				</div>
				<div id="queryJdgl2ChartDiv" style="height:200px;width:100%;min-width:415px;"></div>
			</div>
			<div class="span1">
			</div>
        </div>
      </div>
	</div>
	<div class="" style="border: 0px;">
	  <h4 class="bmjkTitleLine">
		工程计量及支付
	  </h4>
      <div class="container-fluid" style="padding:0px 0px;">
        <div class="row-fluid">
          <div class="span12 GC_GCJLZF_TCGK">
            <div class="bmjkTjgkBlock">
              <p>截至目前，<span class="ndText"></span>年建管中心已开（复）工<span flag="hasLinkKfg" bzfieldname="GCGL_KFGGL_YKFG_ZX"></span>子项<span flag="hasLinkKfg"  bzfieldname="GCGL_KFGGL_YKFG_BD"></span>标段，
              	已完工<span flag="hasLinkKfg" bzfieldname="GCGL_KFGGL_YWG_ZX"></span>子项<span flag="hasLinkKfg" bzfieldname="GCGL_KFGGL_YWG_BD"></span>标段，
              	工程累计完成投资<span  flag="nohasLink" bzfieldname="GC_GCJLZF_TCGK_LJWCZ"></span>亿元，已拨付<span flag="nohasLink" bzfieldname="GC_GCJLZF_TCGK_YBF"></span>亿元。
              </p>
            </div>
          </div>
        </div>
        <div class="row-fluid">
          <div class="span6">
            <table>
              <tr>
                <td class="tab_title_ncjh"><div>总<br/><br/>体<br/><br/>情<br/><br/>况</div></td>
                <td><div id="GC_GCJLZF_ZTQK"></div></td>
              </tr>
            </table>
          </div>
        </div>
      </div>
	</div>
	<div class="" style="border: 0px;">
	  <h4 class="bmjkTitleLine">
		工程洽商
	  </h4>
      <div class="container-fluid" style="padding:0px 0px;">
        <div class="row-fluid">
          <div class="span12 GC_GCQS_TCGK">
            <div class="bmjkTjgkBlock">
              <p>截至目前，已发生工程洽商<span  flag="hasLinkGcqs" bzfieldname="GC_GCQS_TCGK_CNT"></span>次，
              	涉及<span flag="hasLinkTCJH" bzfieldname="GC_GCQS_TCGK_ZX"></span>子项
              	<span flag="hasLinkGcqs" bzfieldname="GC_GCQS_TCGK_BD"></span>标段，
              	签证金额共计<span flag="nohasLink" bzfieldname="GC_GCQS_TCGK_QZJE">0</span>万元。
              	其中，已审批通过<span flag="hasLinkGcqs" bzfieldname="GC_GCQS_TCGK_SPTG_CNT"></span>次，
              	涉及<span flag="hasLinkTCJH" bzfieldname="GC_GCQS_TCGK_SPTG_ZX"></span>子项
              	<span flag="hasLinkGcqs" bzfieldname="GC_GCQS_TCGK_SPTG_BD"></span>标段，
              	签证金额共计<span flag="nohasLink" bzfieldname="GC_GCQS_TCGK_SPTG_QZJE">0</span>万元。
              </p>
            </div>
          </div>
        </div>
        <div class="row-fluid" style="display:none;">
        <div class="span2">
          <table border="0" class="stageTable GC_GCQS_TXFX" style="min-width: 0px;width:140px">
            <tr>
                <td class="line-H line-S blankTD">
                  <div class="div-right-H" style="width:50%;"></div>
                  <div class="div-S" style="height:70%;top:37%;"></div>
                </td>
                <td class="contentTD">
                  <div class="block-gray" style="margin-left:0px;margin-bottom:5px;">
                  	已提报申请<br/>
					<span bzfieldname="GC_GCQS_TXFX_YTBSQ_ZX">XX</span>子项
					<span bzfieldname="GC_GCQS_TXFX_YTBSQ_BD">XX</span>标段
                  </div>
                </td>
            </tr>
            <tr>
                <td class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
						<div class="line"></div>
					</div>
					<div class="triangle-right"></div>
                    <div class="div-S"></div>
				  </div>
                </td>
                <td class="contentTD">
                  <div class="block-blue" style="margin-left:0px;margin-bottom:5px;">
                  	审批完<br/>
					<span bzfieldname="GC_GCQS_TXFX_SPW_ZX">XX</span>子项
					<span bzfieldname="GC_GCQS_TXFX_SPW_BD">XX</span>标段
                  </div>
                </td>
            </tr>
            <tr>
            	<td class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
						<div class="line"></div>
					</div>
					<div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD">
                  <div class="block-red" style="margin-left:0px;">
                  	审批中<br/>
					<span bzfieldname="GC_GCQS_TXFX_SPZ_ZX">XX</span>子项
					<span bzfieldname="GC_GCQS_TXFX_SPZ_BD">XX</span>标段
                  </div>
                </td>
            </tr>
          </table>
        </div>
        </div>
      </div>
	</div>
	<div class="" style="border: 0px;">
	  <h4 class="bmjkTitleLine">
		工程甩项管理
	  </h4>
      <div class="container-fluid" style="padding:0px 0px;">
        <div class="row-fluid">
          <div class="span12 GC_GCSX_TCGK">
            <div class="bmjkTjgkBlock">
              <p>截至目前，已提请甩项<span flag="hasLinkGcsx" bzfieldname="GC_GCSX_TCGK_CNT"></span>个，
              	涉及<span flag="hasLinkTCJH" bzfieldname="GC_GCSX_TCGK_ZX"></span>子项<span flag="hasLinkGcsx" bzfieldname="GC_GCSX_TCGK_BD"></span>标段。
              	已审批完<span flag="hasLinkGcsx" bzfieldname="GC_GCSX_TCGK_YSP_CNT"></span>个，
              	涉及<span flag="hasLinkTCJH" bzfieldname="GC_GCSX_TCGK_YSP_ZX"></span>子项<span flag="hasLinkGcsx" bzfieldname="GC_GCSX_TCGK_YSP_BD"></span>标段。
              </p>
            </div>
          </div>
        </div>
        <div class="row-fluid" style="display:none;">
        <div class="span2">
          <table border="0" class="stageTable" style="min-width: 0px;width:140px">
            <tr>
                <td class="line-H line-S blankTD">
                  <div class="div-right-H" style="width:50%;"></div>
                  <div class="div-S" style="height:70%;top:37%;"></div>
                </td>
                <td class="contentTD">
                  <div class="block-gray" style="margin-left:0px;margin-bottom:5px;">
                  	已提报申请<br/>
					<span bzfieldname="">XX</span>子项
					<span bzfieldname="">XX</span>标段
                  </div>
                </td>
            </tr>
            <tr>
                <td class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
						<div class="line"></div>
					</div>
					<div class="triangle-right"></div>
                    <div class="div-S"></div>
				  </div>
                </td>
                <td class="contentTD">
                  <div class="block-blue" style="margin-left:0px;margin-bottom:5px;">
                  	审批完<br/>
					<span bzfieldname="">XX</span>子项
					<span bzfieldname="">XX</span>标段
                  </div>
                </td>
            </tr>
            <tr>
            	<td class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
						<div class="line"></div>
					</div>
					<div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD">
                  <div class="block-red" style="margin-left:0px;">
                  	审批中<br/>
					<span bzfieldname="">XX</span>子项
					<span bzfieldname="">XX</span>标段
                  </div>
                </td>
            </tr>
          </table>
        </div>
        </div>
      </div>
	</div>
	<div class="" style="border: 0px;">
	  <h4 class="bmjkTitleLine">
		履约保证金管理
	  </h4>
      <div class="container-fluid" style="padding:0px 0px;">
        <div class="row-fluid">
          <div class="span12 GC_LYBZJ_TCGK">
            <div class="bmjkTjgkBlock">
              <p>截至目前，已提请返还履约保证金<span flag="hasLinkLybzj" bzfieldname="GC_LYBZJ_TCGK_CNT">XX</span>个，
              	涉及<span flag="hasLinkTCJH" bzfieldname="GC_LYBZJ_TCGK_ZX">XX</span>子项
              	<span flag="hasLinkLybzj" bzfieldname="GC_LYBZJ_TCGK_BD">XX</span>标段。
              	其中，已审批通过<span flag="hasLinkLybzj" bzfieldname="GC_LYBZJ_TCGK_SPTG">XX</span>个，
              	已返还<span flag="hasLinkLybzj" bzfieldname="GC_LYBZJ_TCGK_YFH">XX</span>个。
              </p>
            </div>
          </div>
        </div>
        <div class="row-fluid">
        	<div class="overFlowX"> 
        	<div style="height: 15px;">
								</div>
								<table width="100%"    align="center" cellpadding="0"
									cellspacing="0" class="">
									<tr >
										<td width="100%">
											<table width="100%" border="0" cellpadding="0" id="tabList1"
												cellspacing="0" class="table3">
												<thead>
													<tr  >
														<th bzfieldname="ND"  ><div style="text-align: center;" >年份</div></th>
														<th bzfieldname="JHXMS"  ><div style="text-align: center;" >计划项目数</div></th>
														<th bzfieldname="JVGS"   ><div style="text-align: center;" >已竣工验收数</div></th>
														<th bzfieldname="SQS"  ><div style="text-align: center;" >申请数</div></th>
														<th bzfieldname="SPZ"  ><div style="text-align: center;" >审批中</div></th>
														<th bzfieldname="SPW"  ><div style="text-align: center;" >审批完</div></th>
													</tr>
												</thead>
											</table>
										</td>
									</tr>
								</table>
			
			</div>
        </div>
        <div class="row-fluid" style="display:none;">
        <div class="span2">
          <table border="0" class="stageTable" style="min-width: 0px;width:140px">
            <tr>
                <td class="line-H line-S blankTD">
                  <div class="div-right-H" style="width:50%;"></div>
                  <div class="div-S" style="height:70%;top:37%;"></div>
                </td>
                <td class="contentTD">
                  <div class="block-gray" style="margin-left:0px;margin-bottom:5px;">
                  	已提报申请<br/>
					<span bzfieldname="">XX</span>子项
					<span bzfieldname="">XX</span>标段
                  </div>
                </td>
            </tr>
            <tr>
                <td class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
						<div class="line"></div>
					</div>
					<div class="triangle-right"></div>
                    <div class="div-S"></div>
				  </div>
                </td>
                <td class="contentTD">
                  <div class="block-blue" style="margin-left:0px;margin-bottom:5px;">
                  	审批完<br/>
					<span bzfieldname="">XX</span>子项
					<span bzfieldname="">XX</span>标段
                  </div>
                </td>
            </tr>
            <tr>
            	<td class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
						<div class="line"></div>
					</div>
					<div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD">
                  <div class="block-red" style="margin-left:0px;">
                  	审批中<br/>
					<span bzfieldname="">XX</span>子项
					<span bzfieldname="">XX</span>标段
                  </div>
                </td>
            </tr>
          </table>
        </div>
        </div>
      </div>
	</div>
	<div>
		<form method="post" id="queryForm"></form>
		<FORM name="frmPost" method="post" style="display: none" target="_blank">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML">
			<input type="hidden" name="txtXML" id="txtXML">
			<input type="hidden" name="ywid" id="ywid">
			<input type="hidden" name="txtFilter" order="asc" fieldname="" id="txtFilter">
			<input type="hidden" name="resultXML" id="resultXML">
			<input type="hidden" name="queryResult" id = "queryResult">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
		</FORM>
	</div>
	<script type="text/javascript">
		// 使用
		require(
		    [
		        'echarts',
		        'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
		    ],
		    function (ec) {
		    	p_ec = ec;
		    	generateNd($("#ND"));
				setDefaultNd("ND");
		    	doInit();
		    	$("input[name='Q_XMLX']").click(function() {
					var nd=$("#ND").val();
					queryByxmlx(nd);
				});
			
			    $("#ND").change(function() {
			    	doInit();
			    });
				//打印按钮
				$("#printButton").click(function(){
					$(this).hide();
					window.print();
					$(this).show();
				});
		    }
		);
	</script>
</body>
</html>
