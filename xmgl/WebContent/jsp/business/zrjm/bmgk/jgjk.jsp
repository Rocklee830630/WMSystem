<!DOCTYPE html>
<html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<head>
<title>建管中心概况</title>
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
	z-index:1;
	left:50%;
	top:50%;
}
.pieChart_div{
	height:200px;
	width:200px;
}
.pieChart_content>hr{
	margin-top:1px;
	margin-bottom:1px;
}
.pieChart_title{
	bottom:0px;
	position:absolute;
	color:#36648B;
	margin-left:-40px;
	left:50%;
	font-weight:900;
}
</style>
<script type="text/javascript">
var controllername = "${pageContext.request.contextPath }/bzjkjm/zrBzjkCommonController.do";
var controllernameCw = "${pageContext.request.contextPath }/cwjk/CwjkController.do";
var p_ec = null; 
require.config({
	paths:{ 
		'echarts' : '${pageContext.request.contextPath }/echarts/js/echarts',
		'echarts/chart/pie' : '${pageContext.request.contextPath }/echarts/js/echarts',
		'echarts/chart/bar' : '${pageContext.request.contextPath }/echarts/js/echarts'
	}
});
function queryByxmlx(nd){
	queryAld(nd);
	queryAbm(nd);
	
}
function doInit() {
	var nd=$("#ND").val();
	$(".ndText").text(nd);
	queryFxwtTjgk(nd);
	queryByxmlx(nd);
	//gcLybzjLb();
	queryJhssxmqk(nd);
	queryYkgxmqk(nd);
	queryList(nd);
	queryJhbzTjgk(nd);
	queryZfqkColumn2d(nd);
}
//----------------------------
//-发现问题-统计概况
//----------------------------
function queryFxwtTjgk(nd){
	var action1 = controllername + "?queryFxwtTjgk&nd="+nd;
	$.ajax({
		url : action1,
		success: function(result) {
			insertTable(result,"GC_FXWT_TCGK");
		}
	});
}
function generateNd(ndObj){
	ndObj.attr("src","T#GC_JH_SJ:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(new Date().getFullYear());
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
			if(str=='BM') {
				showHtml+="<td class='mc' >"+$(subresultmsgobj).attr(str)+" </td>";
					
			}else{
			showHtml+="<td class='mc' style='text-align:center; '>"+$(subresultmsgobj).attr(str)+" </td>";
			} 
		});
		showHtml+="</tr>";
	}
	$("#"+tableID).append(showHtml);
	$($("#"+tableID+" tbody>tr:even")).addClass("tableTrEvenColor");
	$("#"+tableID).addClass("tableList");
	$($("#"+tableID+" thead tr:eq(0)")).addClass("tableTheadTr");
      
}
function queryZfqkColumn2d(nd){
	var action1 = controllernameCw + "?queryZfqkColumn2d&nd="+nd;
	$.ajax({
		url : action1,
		dataType:"json",
		async:false,
		success: function(result){
		 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSStackedColumn2D.swf", "myChartID1", "100%", "200");  
		     myChart.setJSONData(result.msg);  
		     myChart.render("ZFQK_Column2D"); 
		}
	});
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
						case 'hasLinkWttb' :
							var p_linkField = $(this).attr("linkField");
							var p_linkLabel = $(this).attr("linkLabel");
							$(this).html('<a href="javascript:void(0);" onclick="showDataDetail(\''+p_linkLabel+'\',\''+p_linkField+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLinkTCJH' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetailJH(\'项目详细列表\',\'TCJh_DETAIL**GCB**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLink' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\'CBK_DETAIL**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLinkLybzj' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetailLybzj(\'项目详细列表\',\''+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLinkGcqs' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetailGcqs(\'项目详细列表\',\''+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLinkGcsx' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetailGcsx(\'项目详细列表\',\''+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLinkXDK' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetailXDK(\'项目详细列表\',\'XDK_DETAIL**JH**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'nohasLink' :
							var html=$(subresultmsgobj).attr(str);
							$(this).html(html);
							break;
						default:
							$(this).html($(subresultmsgobj).attr(str));
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
function showDataDetail(label,fieldname){
	if(fieldname=="TOTAL"){
		$(window).manhuaDialog({"title":"问题详细列表","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/bmgk/wttbDetailList.jsp","modal":"1"});
	}else{
		$(window).manhuaDialog({"title":"问题详细列表","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/bmgk/wttbDetailList.jsp?fieldname="+fieldname+"&label="+label,"modal":"1"});
	}
}

//按领导
function queryAld(nd){
	var action = controllername + "?queryZbldChart&nd="+nd;
	$.ajax({
		url : action,
		dataType:"json",
		async:false,
		success: function(result){
			if(result.msg==0){
				return;
			}else{
				var resObj = convertJson.string2json1(result.msg);
				var myChart = p_ec.init(document.getElementById("fxwtZbldChartDiv"));
				myChart.setOption(resObj);
				createContentDiv(resObj,"fxwtZbldContentDiv");
				var ecConfig = require('echarts/config');
				function eConsole(param){
					var dIndex = param.dataIndex;
					var sIndex = param.seriesIndex;
					var rowIndex = ((dIndex)*2)+sIndex;
					eval(resObj.link[rowIndex]);
				}
				myChart.on(ecConfig.EVENT.CLICK, eConsole);
			}
		}
	});
}
//按部门
function queryAbm(nd){
	var action = controllername + "?queryZbbmChart&nd="+nd;
	$.ajax({
		url : action,
		dataType:"json",
		async:false,
		success: function(result){
			if(result.msg==0){
				return;
			}else{
				var resObj = convertJson.string2json1(result.msg);
				var myChart = p_ec.init(document.getElementById("fxwtZbbmChartDiv"));
				myChart.setOption(resObj);
				createContentDiv(resObj,"fxwtZbbmContentDiv");
				var ecConfig = require('echarts/config');
				function eConsole(param){
					var dIndex = param.dataIndex;
					var sIndex = param.seriesIndex;
					var rowIndex = ((dIndex)*2)+sIndex;
					eval(resObj.link[rowIndex]);//resObj就是上面的那个数据
				}
				myChart.on(ecConfig.EVENT.CLICK, eConsole);
			}
		}
	});
}
//已开工项目情况
function queryYkgxmqk(nd){
	var action = controllername + "?queryYkgxmqkColumn2d&nd="+nd;
	$.ajax({
		url : action,
		dataType:"json",
		async:false,
		success: function(result){
			if(result.msg==0){
				return;
			}else{
				var resObj = convertJson.string2json1(result.msg);
				var myChart = p_ec.init(document.getElementById("queryYkgxmqkChartDiv"));
				myChart.setOption(resObj);
				createContentDiv(resObj,"queryYkgxmqkContentDiv");
				var ecConfig = require('echarts/config');
				function eConsole(param) 
				{
					var nd = $("#ND").val();
					var  xmsc = {"title":'项目详细列表',"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/tcjhgttList.jsp?flag=4&nd="+nd+"&tiaojian="+param.dataIndex,"modal":"1"};
					$(window).manhuaDialog(xmsc);
				}
				myChart.on(ecConfig.EVENT.CLICK, eConsole);
			}
		}
	});
}
//计划实施项目
function queryJhssxmqk(nd,xmlx){
	var action = controllername + "?queryJhssxmColumn2d&nd="+nd;
	$.ajax({
		url : action,
		dataType:"json",
		async:false,
		success: function(result){
			if(result.msg==0){
				return;
			}else{
				var resObj = convertJson.string2json1(result.msg);
				var myChart = p_ec.init(document.getElementById("queryJhssxmqkChartDiv"));
				myChart.setOption(resObj);
				createContentDiv(resObj,"queryJhssxmqkContentDiv");
				var ecConfig = require('echarts/config');
				function eConsole(param) 
				{
					var nd = $("#ND").val();
					var  xmsc = {"title":'项目详细列表',"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/tcjhgttList.jsp?flag=3&nd="+nd+"&tiaojian="+param.dataIndex,"modal":"1"};
					$(window).manhuaDialog(xmsc);
				}
				myChart.on(ecConfig.EVENT.CLICK, eConsole);
			}
		}
	});
}
//列表
function queryList(nd)
{
	var action1 = controllername + "?queryList&nd="+nd;
	$.ajax({
		url : action1,
		success: function(result)
		{
			addTable2(result,"HtglListTableMore");
		}
	});
}
function addTable2(result,tableID)
{
	$("#"+tableID+" tr").each(function(i){
		if(i>1){
			$(this).empty()
		}
	});
	var resultmsgobj = convertJson.string2json1(result);
	var resultobj = convertJson.string2json1(resultmsgobj.msg);
	var len = resultobj.response.data.length;
	var showHtml='';
	var openWin;
	for(var i=0;i<len;i++){
		showHtml +="<tr>";
		$("#"+tableID+" tr").each(function(j)
		{
			var lie=0;
			if(j=='0'){
				$($("#"+tableID+" tr ")[j]).children().each(function(h){
					var subresultmsgobj = resultobj.response.data[i];
					var str = $(this).attr("bzfieldname");
					var flag=$(this).attr("colspan");
					if(flag>1){
						flag=Number(flag)+Number(lie);
						$($("#"+tableID+" tr ")[j+1]).children().each(function(k){
							if(lie<=k&&k<flag){
								lie++;
								var str = $(this).attr("bzfieldname");
								var shu=" ";
								if(""==$(subresultmsgobj).attr(str)){
									shu=0;
								}else{
									shu=$(subresultmsgobj).attr(str);
								}
								//var lianjie=doLinkHT( $(subresultmsgobj) ,$(this).attr("bzfieldname"));
								if($(subresultmsgobj).attr(str)!=0 && k<2){
									showHtml+='<td class="mc" style="text-align:center;"><a href="javascript:void(0);" onclick="openDetailQqsxSg(\'项目详细列表\',\''+str+'\',\''+$(subresultmsgobj).attr("XH")+'\')">'+shu+'</a></td>';
								}else if($(subresultmsgobj).attr(str)!=0 &&k>1){
									if( k==2 || k==4 || k==5){
										showHtml+='<td class="mc" style="text-align:center;"><a href="javascript:void(0);" onclick="openDetailLX(\'项目详细列表\',\''+str+'\',\''+$(subresultmsgobj).attr("XH")+'\')">'+shu+'</a></td>';	}else if( i==2){
									} else if(k==3 ){
										showHtml+='<td class="mc" style="text-align:center;"><a href="javascript:void(0);" onclick="openDetailGH(\'项目详细列表\',\''+str+'\',\''+$(subresultmsgobj).attr("XH")+'\')">'+shu+'</a></td>';
									}else if( k==6 || k==7 || k==8 ){
										showHtml+='<td class="mc" style="text-align:center;"><a href="javascript:void(0);" onclick="openDetailSJ(\'项目详细列表\',\''+str+'\',\''+$(subresultmsgobj).attr("XH")+'\')">'+shu+'</a></td>';
									}else if( k==9 || k==10){
										showHtml+='<td class="mc" style="text-align:center;"><a href="javascript:void(0);" onclick="openDetailZJ(\'项目详细列表\',\''+str+'\',\''+$(subresultmsgobj).attr("XH")+'\')">'+shu+'</a></td>';
									} else{
										if(i==0){
											showHtml+='<td class="mc" style="text-align:center;"><a href="javascript:void(0);" onclick="openDetailZB(\'项目详细列表\',\''+str+'\',\''+$(subresultmsgobj).attr("XH")+'\')">'+shu+'</a></td>';
												
										}else{
											showHtml+='<td class="mc" style="text-align:center;"><a href="javascript:void(0);" onclick="openDetailZBTCJH(\'项目详细列表\',\''+str+'\',\''+$(subresultmsgobj).attr("XH")+'\')">'+shu+'</a></td>';
										}
										}  
								}else{
									showHtml+="<td class='mc' style='text-align:center;'>"+$(subresultmsgobj).attr(str)+" </td>";
								}
							}
						});
					}else{
						var shu=" ";
						if(""==$(subresultmsgobj).attr(str)){
							shu=0;
						}else{
							shu=$(subresultmsgobj).attr(str);
						}
						if(str=='BM') {
							showHtml+="<td class='mc' >"+shu+" </td>";
						}else{
							if(h>0 && h<4){
								showHtml+='<td class="mc" style="text-align:center;"><a href="javascript:void(0);" onclick="openDetailZbxq(\'项目详细列表6\',\'ZBXQ_DETAIL**ZTB**'+str+'\',\''+$(subresultmsgobj).attr("ORDERNUM")+'\')">'+shu+'</a></td>';
							}else{
								showHtml+="<td class='mc' style='text-align:center;'>"+shu+" </td>";
							}
						}
					}
				}); 	
			}
		
		});
		showHtml+="</tr>";
	}
	$("#"+tableID).append(showHtml);
	$($("#"+tableID+" tbody>tr:even")).addClass("tableTrEvenColor");
	$("#"+tableID).addClass("tableList");
	$($("#"+tableID+" thead tr:eq(0)")).addClass("tableTheadTr");
}
function createContentDiv(resObj,contentID){
	var showHtml = "";
	var dataObj = resObj.series[0].data;
	for(var i=0;i<dataObj.length;i++){
		if(i!=0){
			showHtml += "<hr/>";
		}
		showHtml += '<span style="color:'+resObj.color[i]+';font-size:12px;">'+dataObj[i]['name']+'：'+dataObj[i]['value']+'&nbsp;&nbsp;</span>';
	}
	$("#"+contentID).html(showHtml);
	var styleStr = "margin-left:-"+($("#"+contentID).width()/2)+"px;margin-top:-"+($("#"+contentID).height()/2)+"px;";
	$("#"+contentID).attr("style",styleStr);
}

function openDetailJH(title,name){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/tcjhList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}
function queryJhbzTjgk(nd) {
	$.ajax({
		url:controllername+"?queryNDRW&nd="+nd,
		success: function(result) {
			insertTable(result, "GC_JHBZ_TJGK");
		}
	});
}
//链接
function openDetail(title,name){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/cbkList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}
function openDetailXDK(title,name){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/xdkList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}
//列表链接
function openDetailQqsxSg(title,name,colNum){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/sgxkbblsx_zrjm_list.jsp?nd="+nd+"&proKey="+name+"&flag="+colNum+"","modal":"1"};
	$(window).manhuaDialog(xmsc);
}
//列表链接
function openDetailLX(title,name,colNum){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/lxkyList_zrjm_list.jsp?nd="+nd+"&proKey="+name+"&flag="+colNum+"","modal":"1"};
	$(window).manhuaDialog(xmsc);
}
//列表链接
function openDetailGH(title,name,colNum){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/ghsxList_zr_list.jsp?nd="+nd+"&proKey="+name+"&flag="+colNum+"","modal":"1"};
	$(window).manhuaDialog(xmsc);
}
//列表链接
function openDetailSJ(title,name,colNum){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/sjgl_zr_List.jsp?nd="+nd+"&proKey="+name+"&flag="+colNum+"","modal":"1"};
	$(window).manhuaDialog(xmsc);
}
//列表链接
function openDetailZJ(title,name,colNum){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/zjbz_zr_List.jsp?nd="+nd+"&proKey="+name+"&flag="+colNum+"","modal":"1"};
	$(window).manhuaDialog(xmsc);
}
//列表链接
function openDetailZB(title,name,colNum){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/ztbxq_zr_List.jsp?nd="+nd+"&proKey="+name+"&flag="+colNum+"","modal":"1"};
	$(window).manhuaDialog(xmsc);
}

function openDetailZBTCJH(title,name,colNum){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/tcjh_zr_List.jsp?nd="+nd+"&proKey="+name+"&flag="+colNum+"","modal":"1"};
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
      	年度任务&进展&nbsp;&nbsp;
      	<select class="span2 year" style="width: 8%" id="ND" name="ND"  fieldname="ND" operation="=" noNullSelect ="true"></select>
      </h4>
      <div style="height:1px;position:relative;">
			<a href="javascript:void(0);" id="btnGbmjk">
				<span class="pieChart_title" style="bottom:12px;left:320px;">查看监控汇总&nbsp;<i class="icon-folder-open"></i></span>
			</a>
		</div>
      <div class="container-fluid" style="padding:0px 0px;">
        <div class="row-fluid">
          <div class="span12 GC_JHBZ_TJGK">
            <div class="bmjkTjgkBlock">
              <p>
              	截至目前，本年度建委向中心下达计划<span flag="hasLink_jgzxfz_dx"  bzfieldname="ZBGL_TJGK_JGZX_DX"></span>大项<span flag="hasLink_jgzxfz_zx" bzfieldname="ZBGL_TJGK_JGZX_ZX"></span>子项重点工程，
              	年度计划完成投资<span bzfieldname="LWYJ_JHZTZE"></span>亿元。
              	其中，新建项目<span flag="hasLink__jgzxfz_xjdx" bzfieldname="ZBGL_TJGK_XINJ_DX"></span>项， 续建项目<span flag="hasLink__jgzxfz_xujdx" bzfieldname="ZBGL_TJGK_XUJ_DX"></span>项；
              	计划实施<span flag="hasLinkTCJH" bzfieldname="JHXD_CNT_JHSS"></span>项，
              	启动前期<span flag="hasLinkTCJH" bzfieldname="JHXD_CNT_NDMB"></span>项。
              	<br/>  下达项目中已完成统筹计划编制<span flag="hasLinkXDK"  bzfieldname="JHBZ_TJGK_JGZX_WCBZ"></span>项， 
              	正在编制<span flag="hasLinkXDK"  bzfieldname="JHBZ_TJGK_JGZX_ZZBZ"></span>项，
              	无法编制<span flag="hasLinkXDK"  bzfieldname="JHBZ_TJGK_JGZX_WFBZ"></span>项。 
              </p>
            </div>
          </div>
        </div>
      </div>
      <div class="container-fluid" style="padding:0px 0px;">
			<div class="row-fluid">
				<div class="span4" style="min-width:415px;">
					<div style="height:20px;position:relative;">
						<span class="pieChart_title">计划实施项目情况</span>
					</div>
					<div style="height:200px;position:relative;">
						<div id="queryJhssxmqkContentDiv" class="pieChart_content">
						</div>
						<div id="queryJhssxmqkChartDiv" class="pieChart_div" style="min-width:415px;"></div>
					</div>
				</div>
          		<div class="span2"></div>
				<div class="span4" style="min-width:415px;">
					<div style="height:20px;position:relative;">
						<span class="pieChart_title">已开工项目情况</span>
					</div>
					<div style="height:200px;position:relative;">
						<div id="queryYkgxmqkContentDiv" class="pieChart_content">
						</div>
						<div id="queryYkgxmqkChartDiv" class="pieChart_div" style="min-width:415px;"></div>
					</div>
				</div>
	          	<div class="span1"></div>
				<div class="span12" style="margin-left:0px;">
					<div class="B-small-from-table-autoConcise">
						<div class="overFlowX" style="height: 100%;">
							<table width="100%" style="border-bottom: #ccc solid 1px;"
								align="center" cellpadding="0" cellspacing="0" class="">
								<tr>
									<td width="100%">
										<table width="100%" border="0" cellpadding="0"
											id="HtglListTableMore" cellspacing="0" class="table3">
											<thead>
												<tr
													style="border-bottom: #fff solid 1px; border-top: #ccc solid 1px; color: #fff; background: #36648B">
													<th bzfieldname="ISWC" rowspan=2>
														<div    style="text-align: center;">
															是否完成
														</div>
													</th>
													<th bzfieldname="KONG" colspan="6">
														<div style="text-align: center;">
															手续
														</div>
													</th>
													<th bzfieldname="KONG" colspan="3">
														<div style="text-align: center;">
															设计
														</div>
													</th>
													<th bzfieldname="KONG" colspan="2">
														<div style="text-align: center;">
															造价
														</div>
													</th>
													<th bzfieldname="KONG" colspan="2">
														<div style="text-align: center;">
															招标
														</div>
													</th>
													<!-- <th bzfieldname="ZCVALUE" rowspan="2">
														<div style="text-align: center;">
															排迁
														</div>
													</th> -->
												<!-- 	<th bzfieldname="KONG" rowspan="2">
														<div style="text-align: center;">
															工程
														</div>
													</th> -->
												</tr>
												<tr style="border-bottom: #FFF solid 1px; border-top: #ccc solid 1px; color: #fff; background: #36648B">
													<th bzfieldname="SGXKVALUE">
														<div style="text-align: center;">
															施工许可证
														</div>
													</th>
													<th bzfieldname="BJVALUE">
														<div style="text-align: center;">
															报建
														</div>
													</th>
													<th bzfieldname="TDVALUE">
														<div style="text-align: center;">
															土地
														</div>
													</th>
													<th bzfieldname="GHVALUE">
														<div style="text-align: center;">
															规划
														</div>
													</th>
													<th bzfieldname="KYPFVALUE">
														<div style="text-align: center;">
															可研批复
														</div>
													</th>
													<th bzfieldname="XMJYSVALUE">
														<div style="text-align: center;">
															项目建议书批复
														</div>
													</th>
													<th bzfieldname="CQTVALUE" style="border-left:1px solid #FFF;">
														<div style="text-align: center;">
															拆迁图
														</div>
													</th>
													<th bzfieldname="PQTVALUE">
														<div style="text-align: center;">
															排迁图
														</div>
													</th>
													<th bzfieldname="SGTVALUE">
														<div style="text-align: center;">
															施工图
														</div>
													</th>
													<th bzfieldname="TBJVALUE" style="border-left:1px solid #FFF;">
														<div style="text-align: center;">
															造价编制
														</div>
													</th>
													<th bzfieldname="CSVALUE">
														<div style="text-align: center;">
															财审
														</div>
													</th>
													<th bzfieldname="SGDWVALUE" style="border-left:1px solid #FFF;">
														<div style="text-align: center;">
															施工招标
														</div>
													</th>
													<th bzfieldname="JLDWVALUE">
														<div style="text-align: center;">
															监理招标
														</div>
													</th>
													<!-- <th bzfieldname="ZCVALUE" style="border-left:1px solid #FFF;">
														<div style="text-align: center;">
															实施排迁
														</div>
													</th> -->
													<!-- <th bzfieldname="ZCVALUE">
														<div style="text-align: center;">
															无排迁
														</div>
													</th> -->
												</tr>
											</thead>
										</table>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
    </div>
  </div>
	<div class="" style="border: 0px;">
	  <h4 class="bmjkTitleLine">
		发现的问题
	  </h4>
		<div style="height:1px;position:relative;">
			<a href="javascript:void(0);" id="btnWttb">
				<span class="pieChart_title" style="bottom:6px;left:150px;">查看问题详细监控&nbsp;<i class="icon-folder-open"></i></span>
			</a>
		</div>
      <div class="container-fluid" style="padding:0px 0px;">
        <div class="row-fluid">
          <div class="span12 GC_FXWT_TCGK">
            <div class="bmjkTjgkBlock">
              <p>
              	截至目前，中心共收到问题<span flag="hasLinkWttb" linkField="TOTAL" bzfieldname="FXWT_TJGK_ZXWT"></span>个，
              	其中未解决问题<span flag="hasLinkWttb" linkField="JJQK_WJJ" linkLabel="未解决" bzfieldname="FXWT_TJGK_WJJWT"></span>个，
              	已解决问题<span flag="hasLinkWttb" linkField="JJQK_YJJ" linkLabel="已解决" bzfieldname="FXWT_TJGK_YJJWT"></span>个。
              	<br>
              	其中，我待解决的问题<span flag="hasLinkWttb" linkField="WTZZ_lij@JJQK_WJJ" linkLabel="我待解决的问题" bzfieldname="FXWT_TJGK_WDWT"></span>个，
              	其中特急件<span flag="hasLinkWttb"  linkField="WTZZ_lij@JJQK_WJJ@QZHJ_13" linkLabel="我待解决的问题" bzfieldname="FXWT_TJGK_WDWTTJJ"></span>个，
              	急件<span flag="hasLinkWttb"  linkField="WTZZ_lij@JJQK_WJJ@QZHJ_12" linkLabel="我待解决的问题" bzfieldname="FXWT_TJGK_WDWTJJ"></span>个，
              	一般件<span flag="hasLinkWttb"  linkField="WTZZ_lij@JJQK_WJJ@QZHJ_11" linkLabel="我待解决的问题" bzfieldname="FXWT_TJGK_WDWTYBJ"></span>个。
              	<br>
              	各分管主任待解决的问题<span flag="hasLinkWttb" linkField="WTZZ_FGZR@JJQK_WJJ" linkLabel="分管主任待解决" bzfieldname="FXWT_TJGK_LDWT"></span>个，
              	其中特急件<span flag="hasLinkWttb" linkField="WTZZ_FGZR@JJQK_WJJ@QZHJ_13" linkLabel="分管主任待解决特急件" bzfieldname="FXWT_TJGK_LDWTTJJ"></span>个，
              	急件<span flag="hasLinkWttb" linkField="WTZZ_FGZR@JJQK_WJJ@QZHJ_12" linkLabel="分管主任待解决急件" bzfieldname="FXWT_TJGK_LDWTJJ"></span>个，
              	一般件<span flag="hasLinkWttb" linkField="WTZZ_FGZR@JJQK_WJJ@QZHJ_11" linkLabel="分管主任待解决一般件" bzfieldname="FXWT_TJGK_LDWTYBJ"></span>个。
              	<br>
              	各部长待解决的问题<span flag="hasLinkWttb" linkField="WTZZ_ZXGBM@JJQK_WJJ" linkLabel="各部长待解决" linkField="WTZZ_ZXLD@" bzfieldname="FXWT_TJGK_BZWT"></span>个，
              	其中特急件<span flag="hasLinkWttb"  linkField="WTZZ_ZXGBM@JJQK_WJJ@QZHJ_13" linkLabel="各部长待解决特急件" bzfieldname="FXWT_TJGK_BZWTTJJ"></span>个，
              	急件<span flag="hasLinkWttb"  linkField="WTZZ_ZXGBM@JJQK_WJJ@QZHJ_12" linkLabel="各部长待解决急件" bzfieldname="FXWT_TJGK_BZWTJJ"></span>个，
              	一般件<span flag="hasLinkWttb"  linkField="WTZZ_ZXGBM@JJQK_WJJ@QZHJ_11" linkLabel="各部长待解决一般件" bzfieldname="FXWT_TJGK_BZWTYBJ"></span>个。
              </p>
            </div>
          </div>
        </div>
 		<div class="row-fluid">
			<div class="span6">
				<div style="height:1px;position:relative;">
					<span class="pieChart_title" style="bottom:-30px">领导问题解决情况</span>
				</div>
				<div style="height:370px;position:relative;">
					<div id="fxwtZbldChartDiv" style="height:100%;min-width:600px;"></div>
				</div>
			</div>
			<div class="span6">
				<div style="height:1px;position:relative;">
					<span class="pieChart_title" style="bottom:-30px">各部门问题解决情况</span>
				</div>
				<div style="height:370px;position:relative;">
					<div id="fxwtZbbmChartDiv" style="height:100%;min-width:600px;"></div>
				</div>
			</div>
        </div>
      </div>
	</div>
	<div class="" style="border: 0px;">
	  <h4 class="bmjkTitleLine">
		投资完成及支付
	  </h4>
      <div class="container-fluid" style="padding:0px 0px;">
        <div class="row-fluid">
          <div class="span12 GC_GCQS_TCGK">
            <div class="bmjkTjgkBlock">
              <p>
              	截至目前，<span class="setnd">XX</span>年度累计完成投资<span bzfieldname="">XX</span>亿元，
              	其中，工程完成<span bzfieldname="">XX</span>亿元；拆迁完成<span bzfieldname="">XX</span>亿元；排迁完成<span bzfieldname="">XX</span>亿元；前期完成<span bzfieldname="">XX</span>亿元。</br>
              	本年度已支付<span bzfieldname="">XX</span>亿元，其中，工程支付<span bzfieldname="">XX</span>亿元；拆迁支付<span bzfieldname="">XX</span>亿元。排迁支付<span bzfieldname="">XX</span>亿元；前期程支付<span bzfieldname="">XX</span>亿元。
              </p>
            </div>
          </div>
        </div>
      </div>
			<div class="row-fluid">
				<div class="span12">
					<table width="50%">
						<tr>
							<td>
								<div id="ZFQK_Column2D"></div>
							</td>
						</tr>
					</table>
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
		        'echarts/chart/pie', // 使用柱状图就加载bar模块，按需加载
		        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
		    ],
		    function (ec) {
		    	p_ec = ec;
		    	generateNd($("#ND"));
				setDefaultNd("ND");
		    	doInit();
			    $("#ND").change(function() {
			    	doInit();
			    });
			    $("#btnWttb").click(function(){
					var  xmsc = {"title":"问题监控","type":"text","content":"${pageContext.request.contextPath }/jsp/business/wttb/bmgk/wttbZrjkNew.jsp","modal":"1"};
					$(window).manhuaDialog(xmsc);
			    });
			    $("#btnGbmjk").click(function(){
					var  xmsc = {"title":"监控汇总","type":"text","content":"${pageContext.request.contextPath }/jsp/business/common_yw/bmjk/bmjkhz.jsp","modal":"1"};
					$(window).manhuaDialog(xmsc);
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
