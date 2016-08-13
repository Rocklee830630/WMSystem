<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/FixTable.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/bmgk/bmgk.css?version=20140418">
<style type="text/css">
body {font-size:14px;}
h2 {display:inline; line-height:2em;}
.stageTable .blankTD{
	width:2%;
	padding:0px;
}
.stageTable .contentTD{
	width:10%;
	padding:0px;
	height:100%;
}
.stageTable .contentTD7{
	width:7%;
	padding:0px;
	height:100%;
}

.stageTable .blankTD3 {
	width:3%;
	padding:0px;
}
</style>

<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }//sjgk/sjgkController.do"; 
//初始化页面
$(function() {
	generateNd($("#ND"));
	setDefaultNd();
	init();
	$(".ndText").text($("#ND").val());
	 //监听年度变化
    $("#ND").change(function() {
    	$(".ndText").text($(this).val());
    	init();
    });
	//打印按钮
	$("#printButton").click(function(){
		$(this).hide();
		window.print();
		$(this).show();
	});
});
//年份查询
function generateNd(ndObj){
	ndObj.attr("src","T#GC_JH_SJ:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(new Date().getFullYear());
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
							$(this).html('<a href="javascript:void(0);" onclick="openDetailJH(\'项目详细列表\',\'TCJh_DETAIL**SJ**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLink' :
							$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\'CBK_DETAIL**SJ**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLinkSJBG':
							$(this).html('<a href="javascript:void(0);" onclick="openDetailSJBG(\'项目详细列表\',\'SJBG_DETAIL**SJ**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLinkSJGL':
							$(this).html('<a href="javascript:void(0);" onclick="openDetailSJGL(\'项目详细列表\',\''+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						case 'hasLinkJJG':
							$(this).html('<a href="javascript:void(0);" onclick="openDetailJJG(\'项目详细列表\',\''+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
						default:
							var html=$(subresultmsgobj).attr(str);
							$(this).html(html);
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
//初始化调用方法
function init(){
	var nd=$("#ND").val();
	queryTxfx(nd);
	//queryQll(nd);
	queryDll(nd);
	querySjglTcgk(nd);
	queryJgjvgTcgk(nd); 
}
//图形分析
function queryTxfx(nd){
	var action1 = controllername + "?queryTxfx&nd="+nd;
	$.ajax({
		url : action1,
		success: function(result){
			insertTable(result,"TXFX");
		}
	});
}
//桥梁类
function queryQll(nd){
	var action1 = controllername + "?queryQll&nd="+nd;
	$.ajax({
		url : action1,
		success: function(result){
			insertTable(result,"QLL");
		}
	});
}
//道路类
function queryDll(nd){
	var action1 = controllername + "?queryDll&nd="+nd;
	$.ajax({
		url : action1,
		success: function(result){
			insertTable(result,"DLL");
		}
	});
}

//设计部门-设计管理-统筹概况
function querySjglTcgk(nd){
	var action1 = controllername + "?querySjglTcgk&nd="+nd;
	$.ajax({
		url : action1,
		success: function(result){
			insertTable(result,"SJ_SJGL_TCGK");
		}
	});
}

//设计部门-设计管理-统筹概况
function queryJgjvgTcgk(nd){
	var action1 = controllername + "?queryJgjvgTcgk&nd="+nd;
	$.ajax({
		url : action1,
		success: function(result){
			insertTable(result,"SJ_JGJVG_TCGK");
		}
	});
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
function openDetailSJBG(title,name){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/sjbgList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}

// 设计管理图表详细页面
function openDetailSJGL(title,name){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/sjglList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}

//交（竣）工验收管理图表详细页面
function openDetailJJG(title,name){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/sjjjgglList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
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
      	设计管理&nbsp;&nbsp;
      	 <select class="span2 year" style="width: 8%" id="ND" name="ND"  fieldname="ND" operation="=" noNullSelect ="true"></select>
      </h4>
      <div class="container-fluid" style="padding:0px 0px;">
        <div class="row-fluid">
          <div class="span12 SJ_SJGL_TCGK">
            <div class="bmjkTjgkBlock">
              <p>
              	<span class="ndText">2014</span>建管中心共负责<span flag="hasLink_jgzxfz_dx" bzfieldname="SJ_SJGL_TCGK_JGZX_DX">XX</span>项
              	（<span flag="hasLink_jgzxfz_zx" bzfieldname="SJ_SJGL_TCGK_JGZX_ZX">XX</span>个子项）重点工程。
              	其中，新建<span flag="hasLink__jgzxfz_xjdx" bzfieldname="SJ_SJGL_TCGK_XINJ_DX">XX</span>项（<span flag="hasLink__jgzxfz_xjzx" bzfieldname="SJ_SJGL_TCGK_XINJ_ZX">XX</span>个子项），
              	续建<span flag="hasLink__jgzxfz_xujdx" bzfieldname="SJ_SJGL_TCGK_XUJ_DX">XX</span>项（<span flag="hasLink__jgzxfz_xujzx" bzfieldname="SJ_SJGL_TCGK_XUJ_ZX">XX</span>个子项）。<br/>
              	—按设计管理责任部门划分：建管中心总工办负责<span flag="hasLink_jgzxfz_dx" bzfieldname="SJ_SJGL_TCGK_JGZX_DX">XX</span>项
              	（<span flag="hasLink_jgzxfz_zx" bzfieldname="SJ_SJGL_TCGK_JGZX_ZX">XX</span>子项），
              	其他法人单位负责<span bzfieldname="">0</span>项（<span bzfieldname="">0</span>子项）。
              </p>
            </div>
          </div>
          <div class="span12" style="margin-left:0px;min-width:960px;">
            <table border="0" class="stageTable TXFX">
              <tr>
                <td class="contentTD" style="vertical-align:bottom;">
                  <div class="divTitle" style="margin-top:7px;">年度计划</div>
				  <div class="divContent">
					<span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="NDJH_ZX"></span>子项
				  </div>
				</td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td class="line-S blankTD">
				  <div class="containerDiv-upToRight">
				    <div class="lineDiv">
				      <div class="line" style="height:50px;"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD">
                  <div class="block-red">
                  	未完成初设<br/>
                  	<span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="WWCCBSJPH"></span>子项
                  </div>
                </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-upToRight">
				    <div class="lineDiv">
				      <div class="line" style="height:50px;"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD">
                  <div class="block-red">
                  	未完成送审版<br/>
                  	<span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="WWCSSB">XX</span>子项
                  </div>
                </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-upToRight">
				    <div class="lineDiv">
				      <div class="line" style="height:50px;"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD">
                  <div class="block-red">
                  	部分完成正式版<br/>
                  	<span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="BFWCZSB">XX</span>子项，
                  	<span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="BFCZSB_YWBD">XX</span>标段完成<br/>
                  	<span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="BFCZSB_WWBD">XX</span>标段未完成
                  </div>
                </td>
              </tr>
              <tr>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
                </td>
                <td></td>
                <td class="line-S blankTD"></td>
                <td class="contentTD"></td>
                <td></td>
                <td class="contentTD" rowspan="4" style="width:14%;">
                	<div class="block-total">
	               		<div class="span12 title-M">设计招标情况</div>
	               		<div class="span5 title-N" style="font-size: 12px;">已招<span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="ZBZX"></span>子项</div>
	               		<div class="span7 title-Q">
	               			<div class="span12 title-P">未招标<span class="fieldValue-B" flag="hasLinkSJGL" bzfieldname="WZBBD"></span>标段</div>
	               			<div class="span12 title-O">已招标<span class="fieldValue-B" flag="hasLinkSJGL" bzfieldname="YZBBD"></span>标段</div>
	               		</div>
	               		<div class="span5 title-P" style="width:45%;">
	               			未招标<span class="fieldValue-B" flag="hasLinkSJGL" bzfieldname="WZBZX"></span>子项
	               		</div>
	               		<div class="span7 title-Q" style="font-size: 12px;">
	               			<div class="span12 title-P"><span class="fieldValue-B" flag="hasLinkSJGL" bzfieldname="WZBBDQB"></span>标段</div>
	               		</div>
               		</div>
                </td>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
                </td>
                <td></td>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
                </td>
                <td class="contentTD"></td>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
                </td>
                <td></td>
              </tr>
              <tr>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
                </td>
                <td class="contentTD" rowspan="3" style="width:14%;">
                	<div class="block-total">
	               		<div class="span12 title-M">可研报告编制情况</div>
	               		<div class="span5 title-N"><span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="KYBGBZ_ZX"></span>子项</div>
	               		<div class="span7 title-Q">
	               			<div class="span12 title-O">已编制<span class="fieldValue-B" flag="hasLinkSJGL" bzfieldname="YBZKYBG"></span>子项</div>
	               			<div class="span12 title-P">未编制<span class="fieldValue-B" flag="hasLinkSJGL" bzfieldname="WBZKYBG"></span>子项</div>
	               		</div>
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
                  <div class="block-blue">
                  	建管中心设计组负责<br/>
                  	<span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="KYBGBZ_ZX"></span>子项
                  </div>
                </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-leftToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-leftToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
				  <div class="div-S" style="height:50%;"></div>
                </td>
                <td class="contentTD">
                  <div class="block-blue">
                  	已完成初设<br/>
                  	<span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="YWCCBSJPF">XX</span>子项
                  </div>
                </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-leftToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
				  <div class="div-S" style="height:50%;"></div>
                </td>
                <td class="contentTD">
                  <div class="block-blue">
                  	已完成送审版<br/>
                  	<span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="YWCSSB">XX</span>子项
                  </div>
                </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-leftToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
				  <div class="div-S" style="height:100%;"></div>
                </td>
                <td class="contentTD">
                  <div class="block-red">
                  	未完成正式版<br/>
                  	<span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="WWCZSB">XX</span>子项，
                  	<span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="WWCZSB_BD">XX</span>标段
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
				  <div class="div-S" style="height:50%;"></div>
                </td>
                <td class="line-H blankTD">
				  <div class="div-H" style="width:50%;"></div>
                </td>
                <td class="blankTD"></td>
                <td class="line-H line-S blankTD"></td>
                <td class="line-H line-S blankTD">
                </td>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
                </td>
                <td class="line-S blankTD"></td>
                <td></td>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
                </td>
                <td></td>
              </tr>
              <tr>
                <td></td>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
                </td>
                <td class="contentTD"></td>
                <td></td>
                <td class="blankTD"></td>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
                </td>
                <td class="line-S blankTD"></td>
                  <td class="contentTD">
                  <div class="block-total">
	               		<div class="span12 title-M">施工图审查备案情况</div>
	               		<div class="span12" style="margin: 0px;">
	               		<div class="span7 title-N"  style="width: 50%;">已完成<span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="YWCSGTSCBA"></span>子项</div>
	               		<div class="span7 title-N" style="width: 50%;background-color: #B22222;">未完成<span class="fieldValue-B" flag="hasLinkSJGL" bzfieldname="WWCSGTSCBA"></span>子项</div>
               		</div>
               		</div>
                </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD">
                  <div class="block-blue">
                  	已完成正式版<br/>
                  	<span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="YWCZSB">XX</span>子项，
                  	<span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="YWCZSB_BD">XX</span>标段
                  </div>
                </td>
              </tr>
              <tr>
                <td class="blankTD">
                </td>
                <td class="blankTD">
                </td>
                <td class="line-S  blankTD">
				  <div class="div-S" style="height:100%;"></div>
				</td>
                <td class="blankTD"></td>
                <td class="blankTD">
                </td>
                <td class="blankTD">
                </td>
                <td class="blankTD"></td>
                <td class="line-S contentTD">
				  <div class="div-S" style="height:100%;"></div>
                </td>
                <td class="line-S blankTD">
                </td>
                <td></td>
              </tr>
              <tr>
                <td ></td>
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
                  <div class="block-blue">
                  	其他单位负责<br/>
                  	<span class="fieldValue-A" bzfieldname="">0</span>子项
                  </div>
                </td>
                <td></td>
                <td class="contentTD">
               		 <div class="block-total">
	               		<div class="span12 title-M">规划条件情况</div>
	               		<div class="span12" style="margin: 0px;">
	               		<div class="span7 title-N"  style="width: 50%;">已完成<span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="YWCGHTJ"></span>子项</div>
	               		<div class="span7 title-N" style="width: 50%;background-color: #B22222;">未完成<span class="fieldValue-B" flag="hasLinkSJGL" bzfieldname="WWCGHTJ"></span>子项</div>
               		</div>
               		</div>
                </td>
                <td></td>
                <td class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
				      <div class="line" style="top:-36px;"></div>
				    </div>
				  </div>
                </td>
                <td class="line-H blankTD">
				  <div class="div-H" style="width:100%;bottom:4px;"></div>
                </td>
                <td class="line-H blankTD">
				  <div class="div-H" style="width:100%;bottom:4px;"></div>
                </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-leftToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD">
                  <div class="block-blue">
                  	已完成拆迁图<br/>
                  	<span class="fieldValue-A" flag="hasLinkSJGL" bzfieldname="YWCQT_BD">XX</span>标段
                  </div>
                </td>
              </tr>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
  	<div class="" style="border: 0px;">
	  <h4 class="bmjkTitleLine">
		设计变更
	  </h4>
      <div class="container-fluid" style="padding:0px 0px;">
        <div class="row-fluid">
          <div class="span12 SJ_JGJVG_TCGK">
            <div class="bmjkTjgkBlock">
              <p> 
             	截至目前，共发生设计变更<span flag="hasLinkSJBG" bzfieldname="SJBG_CS">XX</span>次，
              	涉及<span flag="hasLinkSJBG" bzfieldname="SJBG_ZX">XX</span>子项
              	<span flag="hasLinkSJBG" bzfieldname="SJBG_BD">XX</span>个标段，
              	变更金额共计<span  bzfieldname="SJBG_JE">XX</span>万元。
              </p>
            </div>
          </div>
          </div>
          </div>
      </div>
	<div class="" style="border: 0px;">
	  <h4 class="bmjkTitleLine">
		交（竣）工验收管理
	  </h4>
      <div class="container-fluid" style="padding:0px 0px;">
        <div class="row-fluid">
          <div class="span12 SJ_JGJVG_TCGK">
            <div class="bmjkTjgkBlock">
              <p> 	
              	截至目前，<span class="ndText">2014</span>年建管中心共完工<span flag="hasLinkTCJH" bzfieldname="YWG_BD">XX</span>标段（含甩项），
              	已完成交工<span flag="hasLinkTCJH" bzfieldname="YJG_BD">XX</span>个标段，
              	已完成竣工<span flag="hasLinkTCJH" bzfieldname="YJVG_BD">XX</span>标段。
              </p>
            </div>
          </div>
	      <div class="span12 DLL" style="margin-left:0px;min-width:960px;">
            <div style="height:20px;"></div>
	        <table border="0" class="stageTable">
              <tr>
                <td class="contentTD7" colspan=2>
                  <div class="divTitle">前置条件</div>
				  <div class="divContent">
					已完工<br/>
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_YWG"></span>标段
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
                <td></td>
                <td></td>
                <td></td>
                <td ></td>
                <td class="line-S blankTD">
				  <div class="containerDiv-upToRight">
				    <div class="lineDiv">
				      <div class="line" style="height:40px;"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD7">
                  <div class="block-blue">
                  	资料存档<br/>
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_ZLCD"></span>标段
                  </div>
                </td>
              </tr>
              <tr>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
				</td>
                <td > </td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td class="line-S blankTD">
				  <div class="containerDiv-upToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD7">
                  <div class="block-red">
                  	未报质监站<br/>
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_WBZJZ"></span>标段
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
                <td class="contentTD7">
                  <div class="block-red">
                  	暂不具备验收条件<br/>
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_BJBYSTJ"></span>标段
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
                <td class="contentTD7">
                  <div class="block-red">
                  	未组织竣工验收<br/>
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_WZZJGYS"></span>标段
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
                <td class="contentTD7">
                  <div class="block-blue">
                  	验收合格<br/>
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_YSHG"></span>标段
                  </div>
                </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-leftToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
				  <div class="div-S" style="height:50%;"></div>
                </td>
                <td class="contentTD7">
                  <div class="block-blue">
                  	质监站备案<br/>
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_ZJZBA"></span>标段
                  </div>
                </td>
              </tr>
              <tr>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
				</td>
                <td > </td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
				</td>
                <td></td>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
				</td>
                <td></td>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
				</td>
                <td></td>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
				</td>
                <td > </td>
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
                <td class="contentTD7">
                  <div class="block-red">
                  	未提交<br/>
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_WTJ"></span>标段
                  </div>
                </td>
                <td></td>
                <td > </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-upToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD7">
                  <div class="divTitle" style="font-size:14px;">竣工验收</div>
				  <div class="divContent">
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_JUNGYS"></span>标段
				  </div>
				</td>
                <td class="line-S blankTD">
				  <div class="containerDiv-leftToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
				  <div class="div-S" style="height:50%;"></div>
                </td>
                <td class="contentTD7">
                  <div class="block-blue">
                  	已报质监站<br/>
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_BZJZ"></span>标段
                  </div>
                </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-leftToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
				  <div class="div-S" style="height:50%;"></div>
                </td>
                <td class="contentTD7">
                  <div class="block-blue">
                  	具备验收条件<br/>
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_JBYSTJ"></span>标段
                  </div>
                </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-leftToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
				  <div class="div-S" style="height:50%;"></div>
                </td>
                <td class="contentTD7">
                  <div class="block-blue">
                  	组织竣工验收<br/>
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_ZZJGYS"></span>标段
                  </div>
                </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-leftToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
				  <div class="div-S" style="height:100%;"></div>
                </td>
                <td class="contentTD7">
                  <div class="block-red">
                  	交保证金<br/>
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_JBZJ"></span>标段
                  </div>
                </td>
                <td></td>
                <td></td>
              </tr>
              <tr>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
				</td>
                <td > </td>
                <td></td>
                <td></td>
                <td class="line-S blankTD">
                	<div class="div-S" style="height:100%;"></div>
                </td>
                <td>
				</td>
                <td>
				</td>
                <td></td>
                <td class="blankTD">
				</td>
                <td></td>
                <td class="blankTD">
				</td>
                <td></td>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
				</td>
                <td > </td>
                <td></td>
                <td></td>
              </tr>
              <tr>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
				</td>
                <td > </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-upToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD" style="width:9%">
				  <div class="block-blue">
                  	建管中心总工办负责<br/>
                  	<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_ZGB"></span>标段
                  </div>
				</td>
                <td class="line-S line-H blankTD">
				  <div class="div-H" style="width:50%;"></div>
				  <div class="div-S" style="height:100%;"></div>
				</td>
                <td></td>
                <td class="line-S blankTD">
				  <div class="containerDiv-upToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD7">
                  <div class="block-blue">
                  	已交工<br/>
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_YJG"></span>标段
                  </div>
                </td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD7">
                  <div class="block-red">
                  	整改<br/>
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_ZZZG"></span>标段
                  </div>
                </td>
                <td></td>
                <td></td>
              </tr>
              <tr>
                <td class="line-S blankTD">
				  <div class="div-S" style="height:100%;"></div>
				</td>
                <td > </td>
                <td></td>
                <td></td>
                <td class="line-S blankTD">
                	<div class="div-S" style="height:100%;"></div>
                </td>
                <td>
				</td>
                <td>
				</td>
                <td></td>
                <td class="blankTD">
				</td>
                <td></td>
                <td class="blankTD">
				</td>
                <td></td>
                <td class="blankTD">
				</td>
                <td > </td>
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
                </td>
                <td class="contentTD7">
                  <div class="block-blue">
                  	已提交<br/>
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_YTJ"></span>标段
                  </div>
                </td>
                <td class="line-S line-H blankTD">
				  <div class="div-H" style="width:50%;"></div>
				  <div class="div-S" style="height:100%;"></div>
				</td>
                <td ></td>
                <td class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD7">
                  <div class="divTitle" style="font-size:14px;">交工验收</div>
				  <div class="divContent">
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_JGYS"></span>标段
				  </div>
				</td>
                <td class="line-S blankTD">
				  <div class="containerDiv-leftToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
				  <div class="div-S" style="height:50%;"></div>
                </td>
                <td class="contentTD7">
                  <div class="block-red">
                  	未交工<br/>
					<span class="fieldValue-A" flag="hasLinkJJG" bzfieldname="DLL_WJG"></span>标段
                  </div>
                </td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td > </td>
                <td></td>
                <td></td>
              </tr>
              <tr>
                <td></td>
                <td > </td>
                <td class="line-S blankTD">
				  <div class="containerDiv-downToRight">
				    <div class="lineDiv">
				      <div class="line"></div>
				    </div>
				    <div class="triangle-right"></div>
				  </div>
                </td>
                <td class="contentTD7">
				  <div class="block-blue">
                  	其他法人单位负责<br/>
                  	<span class="fieldValue-A" bzfieldname="">0</span>标段
                  </div>
				</td>
                <td></td>
                <td > </td>
                <td></td>
                <td > </td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td > </td>
                <td></td>
                <td></td>
              </tr>
	        </table>
	        <p></p>
	      </div>
        </div>
      </div>
	</div>
</body>
</html>