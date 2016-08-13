<!DOCTYPE html>
<html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<head>
<title>长春市政府投资建设项目管理中心——综合管控中心</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${pageContext.request.contextPath }/css/base.css" rel="stylesheet" media="screen">
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/FixTable.js"></script>
<style type="text/css">
body {font-size:14px;}
h2 {display:inline; line-height:2em;}
.stageTable{
	width:30%;
	min-width:600px;
	font-family: Microsoft YaHei;
}
.stageTable .divTitle{
	vertical-align:middle;
	text-align:center;
	background:#004097;
	line-height:20px;
	color:#FFF;
	padding:6px 0px;
	font-size:16px;
	font-weight:800;
}
.stageTable .divContent{
	vertical-align:middle;
	text-align:center;
	background:#C7C7C7;
	padding:6px 0px;
}
.stageTable .block-blue{
	vertical-align:middle;
	text-align:center;
	background:#000099;
	line-height:20px;
	color:#FFF;
	padding:6px 0px;
	margin-left:4px;
}
.stageTable .block-gray{
	vertical-align:middle;
	text-align:center;
	background:#BEBEBE;
	line-height:20px;
	color:#000;
	padding:6px 0px;
	margin-left:4px;
}
.stageTable .fieldValue-A{
	font-size:16px;
	color:#FFFF00;
	font-weight:800;
}
.stageTable .block-blue .fieldValue-A{
	font-size:16px;
	color:#FFFF00;
	font-weight:800;
}
.stageTable .block-red{
	vertical-align:middle;
	text-align:center;
	background:#C00000;
	line-height:20px;
	color:#FFF;
	padding:6px 0px;
	margin-left:4px;
}
.stageTable .block-red .fieldValue-A{
	font-size:16px;
	color:#FFFF00;
	font-weight:800;
}
.stageTable .block-purple{
	vertical-align:middle;
	text-align:center;
	background:#9900FF;
	line-height:20px;
	color:#FFF;
	padding:6px 0px;
	margin-left:4px;
}
.stageTable .block-purple .fieldValue-A{
	font-size:16px;
	color:#FFFF00;
	font-weight:800;
}
.stageTable .block-green{
	vertical-align:middle;
	text-align:center;
	background:#71C671;
	line-height:20px;
	color:#FFF;
	padding:6px 0px;
	margin-left:4px;
}
.stageTable .block-green .fieldValue-A{
	font-size:16px;
	color:#C00000;
	font-weight:800;
}
.stageTable .blockTD{
	height:40px;
	line-height:40px;
	position:relative;
}
.stageTable .upToRightDiv{
	height:30px;
	width:30px;
	position:absolute;
	right:0px;
	top:10px;
	border-left:10px solid RGB(9,163,179);
	border-top:10px solid RGB(9,163,179);
}
.stageTable .containerDiv-upToRight{
	align:center;
	width:100%;
	height:8px;
	position:relative;
}
/* 像右箭头匹配的横线*/
.stageTable .containerDiv-upToRight>.lineDiv{
	margin-top:9px;
	width:inherit;/* 宽度从父元素继承 */
	background-color:#FFF;
	height:8px;
	position:absolute;
	z-index:10;
}
.stageTable .containerDiv-upToRight>.lineDiv>.line{
	z-index:20;
	height:36px;
	width:45%;
	position:absolute;
	right:0px;
	top:-11px;
	border-left:10px solid RGB(9,163,179);
	border-top:10px solid RGB(9,163,179);
}
/*右向下*/
.stageTable .containerDiv-rightToDown{
	width:100%;
	height:100%;
}
.stageTable .containerDiv-rightToDown>.lineDiv{
	margin-top:0px;
	width:inherit;/* 宽度从父元素继承 */
	background-color:#FFF;
	height:100%;
	position:relative;
	z-index:10;
}
.stageTable .containerDiv-rightToDown>.lineDiv>.line{
	z-index:20;
	height:45%;
	width:45%;
	position:absolute;
	left:0px;
	bottom:0px;
	padding-left:5px;
	border-right:10px solid RGB(9,163,179);
	border-top:10px solid RGB(9,163,179);
}
.stageTable .containerDiv-rightToDown>.lineDiv>.line-right{
	z-index:20;
	height:45%;
	width:25%;
	position:absolute;
	right:45%;
	bottom:0px;
	padding-left:5px;
	border-right:10px solid RGB(9,163,179);
	border-top:10px solid RGB(9,163,179);
}

/*右向上*/
.stageTable .containerDiv-rightToUp {
	width:100%;
	height:100%;
	position:relative;
}
.stageTable .containerDiv-rightToUp>.lineDiv {
	margin-top:0px;
	width:inherit;/* 宽度从父元素继承 */
	background-color:#FFF;
	height:inherit;
	position:absolute;
	z-index:10;
}
.stageTable .containerDiv-rightToUp>.lineDiv>.line{
	z-index:20;
	height:45%;
	width:45%;
	position:absolute;
	left:0px;
	top:0px;
	padding-left:5px;
	border-right:10px solid RGB(9,163,179);
	border-bottom:10px solid RGB(9,163,179);
}

.stageTable .containerDiv-downToRight{
	align:center;
	width:100%;
	height:8px;
	position:relative;
}
/* 像右箭头匹配的横线*/
.stageTable .containerDiv-downToRight>.lineDiv{
	margin-top:0px;
	width:inherit;/* 宽度从父元素继承 */
	background-color:#FFF;
	height:8px;
	position:absolute;
	z-index:10;
}
.stageTable .containerDiv-downToRight>.lineDiv>.line{
	z-index:20;
	height:34px;
	width:45%;
	position:absolute;
	right:0px;
	top:-36px;
	border-left:10px solid RGB(9,163,179);
	border-bottom:10px solid RGB(9,163,179);
}
/* 向右箭头 */
.stageTable .triangle-right{
	position:absolute;
	top:-7px;
	right:-16px;
	z-index:30;
	border:10px solid #000;
	border-color: transparent transparent transparent RGB(9,163,179);
	width:0px;
	height:0px;
	display:inline-block;
}


/*竖线*/
.stageTable .line-S{
	padding-top:0px;
	padding-bottom:0px;
	width:inherit;
	height:100%;
	z-index:50;
	position:relative;
}
.stageTable .line-S .div-S{
	z-index:40;
	height:40px;
	width:45%;
	position:absolute;
	right:0px;
	top:0px;
	border-left:10px solid RGB(9,163,179);
}
/* 像右箭头匹配的横线*/
.stageTable .line-H{
	margin-top:0px;
	width:100%;/* 宽度从父元素继承 */
	background-color:#FFF;
	height:40px;
	z-index:10;
	position:relative;
}
.stageTable .line-H .div-H{
	z-index:20;
	height:45%;
	width:inherit;
	position:absolute;
	left:0px;
	bottom:0px;
	border-top:10px solid RGB(9,163,179);
}
.stageTable .line-H .div-right-H{
	z-index:20;
	height:45%;
	width:inherit;
	position:absolute;
	right:0px;
	bottom:0px;
	border-top:10px solid RGB(9,163,179);
}

.stageTable .blankTD{
	width:3%;
	padding:0px;
}
.stageTable .contentTD{
	width:14%;
	padding:0px;
	height:100%;
}

.stageTable .blankTD3 {
	width:3%;
	padding:0px;
}

.tab_title_ncjh{
	text-align:center;
	background:#004097;
	color:#FFF;
	height:190px;
	font-size:20px;
}
</style>
<script type="text/javascript">

var controllername = "${pageContext.request.contextPath }/cwjk/CwjkController.do";
$(function(){
	generateNd($("#ND"));
	setDefaultNd("ND");
	doInit();
	
    $("#ND").change(function() {
    	doInit();
    });
});
function doInit() {
	 var nd=$("#ND").val();
	$(".setnd").text(nd);
	queryJbqk(nd);
	queryZfqkColumn2d(nd);
}

function generateNd(ndObj){
	ndObj.attr("src","T#GC_JH_SJ:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(new Date().getFullYear());
}

// 基本情况
function queryJbqk(nd) {
	$.ajax({
		url:	controllername+"?queryJbqk&nd="+nd,
		success:function(result) {
			insertTable(result, "CWJK_JBQK");
		}
	});
}
function queryZfqkColumn2d(nd){
	var action1 = controllername + "?queryZfqkColumn2d&nd="+nd;
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
							case 'hasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\''+tableId+'_DETAIL**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
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
</script>
</head>
<body>
  <div class="container-fluid">
    <span class="pull-right">
      <button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
    </span>
    <div class="B-small-from-table-autoConcise" style="border: 0px;">
      <h4 style="background: none; color: #333; border-bottom: #4677B3 solid 10px;margin-bottom:3px;font-size:20px;">
      	基本情况&nbsp;&nbsp;
      	<select class="span2 year" style="width: 8%" id="ND" name="ND"  fieldname="ND" operation="=" noNullSelect ="true"></select>
      </h4>
      <div class="container-fluid" style="padding:0px 0px;">
        <div class="row-fluid">
          <div class="span12 CWJK_JBQK">
            <div style="width:100%;height:40px;vertical-align:middle;background:#88B9DD;margin-bottom:5px;">
              <p style="padding:8px 0px 8px 0px;color:#000;font-size:16px;">
              	<span class="setnd"></span>年建管中心共实施<span bzfieldname="DX"></span>项（<span bzfieldname="ZX"></span>个子项）重点工程。总投资<span bzfieldname="ZTZ"></span>亿元，年度计划完成投资<span bzfieldname="JHWCTZ"></span>亿元<br/>
              </p>

            </div>
          </div>
          <!-- <div class="span6 GC_GCGL_TCGK" style="margin-left:0px;min-width:960px;">
            <table border="0" class="stageTable">
              <tr>
                <td class="contentTD"  rowspan="3">
                  <div class="block-blue">
                  	建管中心共实施
					<span bzfieldname="GCGL_KFGGL_YQDSGDW_ZX">XX</span>项
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
                  	已到位资金
					<span bzfieldname="GCGL_KFGGL_ZBJBKGTJ_ZX">XX</span>亿元，
					<span bzfieldname="GCGL_KFGGL_ZBJBKGTJ_BD">XX</span>项
                  </div>
                </td>
              </tr>
              <tr>
                <td class="line-H line-S blankTD">
                  <div class="div-H" style="width:50%;"></div>
                  <div class="div-S" style="height:100%;"></div>
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
                <td class="contentTD" title="具备开工条件">
                  <div class="block-blue">
                  	未到位资金
					<span bzfieldname="GCGL_KFGGL_ZBJBKGTJ_ZX">XX</span>亿元，
					<span bzfieldname="GCGL_KFGGL_ZBJBKGTJ_BD">XX</span>项
                  </div>
                </td>
              </tr>
            </table>
          </div> -->
        </div>
      </div>
    </div>
  </div>
  <div class="B-small-from-table-autoConcise" style="border: 0px;">
	  <h4 style="background: none; color: #333; border-bottom: #4677B3 solid 10px;margin-bottom:3px;font-size:20px;">
		投资完成及支付情况
	  </h4>
      <div class="container-fluid" style="padding:0px 0px;">
        <div class="row-fluid">
          <div class="span12 GC_GCJLZF_TCGK">
            <div style="width:100%;height:60px;vertical-align:middle;background:#88B9DD;margin-bottom:5px;">
              <p style="padding:8px 0px 8px 0px;color:#000;font-size:16px;">
              	截至目前，<span class="setnd">XX</span>年度累计完成投资<span bzfieldname="">XX</span>亿元，
              	其中，工程完成<span bzfieldname="">XX</span>亿元；拆迁完成<span bzfieldname="">XX</span>亿元；排迁完成<span bzfieldname="">XX</span>亿元；前期完成<span bzfieldname="">XX</span>亿元。</br>
              	本年度已支付<span bzfieldname="">XX</span>亿元，其中，工程支付<span bzfieldname="">XX</span>亿元；拆迁支付<span bzfieldname="">XX</span>亿元。排迁支付<span bzfieldname="">XX</span>亿元；前期程支付<span bzfieldname="">XX</span>亿元。
              </p>
            </div>
          </div>
        </div>
        <div class="row-fluid">
          <div class="span12">
            <table width="50%">
              <tr>
                <td><div id="ZFQK_Column2D"></div></td>
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
</body>
</html>
