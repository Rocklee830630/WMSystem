<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="java.sql.Connection"%>

<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<link rel="stylesheet"href="${pageContext.request.contextPath }/css/bmgk/jcBmgkBase.css?version=13">
<link rel="stylesheet"href="${pageContext.request.contextPath }/css/bmgk/jcBmgkStyle.css?version=15">
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/bmgk/bmgk.css?version=2014041bmjkTjgkBlock">
<app:base/>
<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/zengChaiBuZhangController.do"; 
	$(function()
			{
				setDefaultNd();
				init();
				$("#printButton").click(function(){
					$(this).hide();
					window.print();
					$(this).show();
				});
			});
	//页面初始化
	function init()
	{
		var nd = $("#ND").val();
		g_bAlertWhenNoResult = true;
		tjgk(nd);
		cqjzxq(nd);
		qycqjz(nd);
		zdmj_jzqk(nd);
		qcjzxq(nd);
	}
//根据年度查询
	function queryND()
	{
		init();
	}
//统计概况
	function tjgk(nd)
	{
		var action1 = controllername + "?tongJiGaiKuang&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result)
			{
				 insertTable(result,"TJGK");
			}
		});
	}

//拆迁进展详情
	function cqjzxq(nd)
	{
		var action1 = controllername + "?juMingChaiQianJinZhan&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result)
			{
				 var resultPie=getFusionChartsJsonStringByResult(result,null);
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "myChartId", "100%", "100%");  
			     myChart.setJSONData(resultPie);  
			     myChart.render("jmcqjzqk"); 
			}
		});
	}
//企业拆迁进展详情
	function qycqjz(nd)
	{
		var action1 = controllername + "?qiYeChaiQianJinZhan&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result)
			{
				   var resultPie=getFusionChartsJsonStringByResult(result,null);
				   var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "myChartId1", "100%", "100%");  
				   myChart.setJSONData(resultPie);  
				   myChart.render("qycqjzqk"); 
			}
		});
	}
//征地面积进展详情
	function zdmj_jzqk(nd)
	{
		var action1 = controllername + "?zhengDiMianJiJinZhan&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result)
			{
				 var resultPie=getFusionChartsJsonStringByResult(result,null,'td');
				 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "myChartId2", "100%", "100%");  
				 myChart.setJSONData(resultPie);  
				 myChart.render("zdcqjzqk"); 
			}
		});
	}
//根据结果放入表格
	function insertTable(result,tableId){
		var resultmsgobj = convertJson.string2json1(result);
		var resultobj = convertJson.string2json1(resultmsgobj.msg);
		var subresultmsgobj = resultobj.response.data[0];
		$("span").each(function(i){
			var str = $(this).attr("bzfieldname");
			if(str!=''&&str!=undefined){
				if($(subresultmsgobj).attr(str+"_SV")!=undefined){
					var valueStr = $(subresultmsgobj).attr(str+"_SV");
					if($(this).attr("decimal")=="false"){
						//不需要小数的项，删掉小数点后的内容
						valueStr = valueStr.substring(0,valueStr.lastIndexOf("."));
					}
			     	$(this).html(valueStr);
				}else{
					var flag = $(this).attr("flag");
					if($(subresultmsgobj).attr(str)!==''&&$(subresultmsgobj).attr(str)!=undefined)
					if($(subresultmsgobj).attr(str)!=0){
						switch(flag){
							case 'ndzsrwxx':
								$(this).html('<a href="javascript:void(0);" onclick="zsrwxxView(\'zjbz\',\'ZSYWLB\',\''+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'ndzszhxx':
								$(this).html('<a href="javascript:void(0);" onclick="zszhxxView(\'zjbz\',\'ZSZHXX\',\''+str+'\',\''+$("#ND").val()+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'bd':
								$(this).html('<a href="javascript:void(0);" onclick="_blankXmxxBdxx(\'100001\',\'BMJK_ZS_'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'xm':
								$(this).html('<a href="javascript:void(0);" onclick="_blankXmxx(\'100001\',\'BMJK_ZS_'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							default:
								$(this).html($(subresultmsgobj).attr(str));
								break;
						}
					}else{
						$(this).html($(subresultmsgobj).attr(str));
					}
				}
			}
		});
	}
	//钻取信息
	/**
	 *sql  properties名 ，sql名，需要的条件，年度 
	 *
	 */
	function zsrwxxView(lujing, mingchen, tiaojian){
		var nd1=$("#ND").val();
		var condition = "lujing="+lujing+"&mingchen="+mingchen+"&tiaojian="+tiaojian+"&nd="+nd1;
		var  xmsc = {"title":"项目详细信息","type":"text","content":g_sAppName+"/jsp/business/zsb/zcbz/zsrxxl.jsp?"+condition,"modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
	function zszhxxView(lujing, mingchen, tiaojian){
		var nd1=$("#ND").val();
		var condition = "lujing="+lujing+"&mingchen="+mingchen+"&tiaojian="+tiaojian+"&nd="+nd1;
		var  xmsc = {"title":"项目详细信息","type":"text","content":g_sAppName+"/jsp/business/zsb/zcbz/zhxxbz.jsp?"+condition,"modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
//拆迁进展详情放入表格
	function qcjzxq(nd)
	{
		$("#cqjzxq tr td").remove();
		var action1 = controllername + "?zhengChaiJinZhan&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result)
			{
				var resultmsgobj = convertJson.string2json1(result);
				var resultobj = convertJson.string2json1(resultmsgobj.msg);
				var len = resultobj.response.data.length;
				var showHtml='';
				for(var i=0;i<len;i++){
					showHtml +="<tr>";
					$("#cqjzxq tr th").each(function(j)
					{
						var subresultmsgobj = resultobj.response.data[i];
						var str = $(this).attr("bzfieldname");
						if(str=='DIC_VALUE')
							{
								showHtml+="<td>"+$(subresultmsgobj).attr(str)+" </td>";
							}
						else{
							if(str=="XYJE"||str=="ZJDWJE"){
								  str=str+"_SV";
								  showHtml+="<td style=\"text-align:right;\">"+$(subresultmsgobj).attr(str)+" </td>";
								}else
								{
								showHtml+="<td style=\"text-align:center;\">"+$(subresultmsgobj).attr(str)+" </td>";
								}
							}
					});
					showHtml+="</tr>";
				}
				$("#cqjzxq").append(showHtml);
				$($("#cqjzxq tbody>tr:even")).addClass("tableTrEvenColor");
				$("#cqjzxq").addClass("tableList");
				$($("#cqjzxq thead tr:eq(0)")).addClass("tableTheadTr");
			}
		});
	}

//饼图属性
function getFusionChartsJsonStringByResult(res,attrObj,lb){
	var jsonString = "";
	var resultObj = new Object();
	//chart属性--------------------开始
	var chartObj = new Object();
	if(lb=='td'){
		chartObj.numberSuffix="(平方米)";
	}
	chartObj.pieRadius = "70";
	chartObj.palette = "4";
	chartObj.decimals = "0";
	chartObj.enableSmartLabels = "1";
	chartObj.enableRotation = "1";
	chartObj.bgColor = "#FFFFFF";
	chartObj.bgAngle = "360";
	chartObj.showBorder = "0";
	chartObj.startingAngle = "70";
	chartObj.baseFont = "微软雅黑";
	chartObj.baseFontSize = "12";
	chartObj.xAxisName = "用户";
	chartObj.showFCMenuItem = "0";
	chartObj.formatNumberScale='0'; 
	chartObj.use3DLighting = "0";
	chartObj.showShadow = "0";
	for(var a in attrObj){
		//chartObj.hasOwnProperty("pieradius")
		chartObj[a] = attrObj[a];
	}
	//chart属性--------------------结束
	//data属性---------------------开始
	var dataArray = new Array();
	if(res!="0"){
		var resultmsgobj = convertJson.string2json1(res);
		var resultobj = convertJson.string2json1(resultmsgobj.msg);
		var len = resultobj.response.data.length;
		for(var i=0;i<len;i++){
			var labelObj = new Object();
			labelObj.label = resultobj.response.data[i].LABEL;
			labelObj.value = resultobj.response.data[i].VALUE;
			if(i==1){
				labelObj.color='#19BC9C' ;
			}else{
				labelObj.color='#DDDDDD' ;
			}
			dataArray.push(labelObj);
		}
	}
	//data属性---------------------结束
	//向obj中加入chart属性和data属性
	resultObj.chart = chartObj;
	resultObj.data = dataArray;
	//obj对象转string
	jsonString = JSON.stringify(resultObj);
	return jsonString;
}

// 计划标段数
function _blankXmxxBdxx(ywlx, bmjkLx) {
	var nd = $("#ND").val();
	xmxx_bdxxView(ywlx, nd, bmjkLx);
}

// 项目
function _blankXmxx(ywlx, bmjkLx) {
	var nd = $("#ND").val();
	xmxxView(ywlx, nd, bmjkLx);
}
</script>
</head>
<body>
<div class="container-fluid">
              <span class="pull-right">
    	<button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
    </span>
     <div class="CBK" style="border:0px;">
        <h4 class="bmjkTitleLine">统计概况
        	<select class="span2 year" style="width: 8%" id="ND" onchange="queryND()" name="ND" fieldname="ND" operation="=" noNullSelect ="true" defaultMemo="全部" kind="dic" 
						src="T#GC_JH_SJ: distinct ND as NDCODE:ND:SFYX='1' ORDER BY NDCODE asc">
		   	</select>
        </h4>
        <div class="container-fluid">
         <div class="row-fluid">
                <div class="span12">
                	<div class="bmjkTjgkBlock">
	             	 	<p>
					                 涉及拆迁项目数<span flag="xm" bzfieldname="SJCQXMS" ></span> ，
					                 摸底完成项目数<span flag="ndzsrwxx" bzfieldname="MDWCXMS"  ></span> ，
					                 完成拆迁项目数<span flag="ndzszhxx" bzfieldname="WCCQXMS"></span> ，
					                 场地移交项目数<span flag="ndzszhxx" bzfieldname="CDYJXMS"></span> ，
					                 土地房屋移交项目数<span flag="ndzsrwxx" bzfieldname="TDFWYJXMS"></span> ，
					                 委托协议签订完成项目数<span flag="xm" bzfieldname="WTXYQDWCXMS" ></span> ，
					                 协议金额总计<span bzfieldname="XYJEZJ" ></span>元 ，
					                 资金到位金额总计<span bzfieldname="ZJDWJE" ></span>元  。
		                </p>
		            </div>
                </div>
              </div>
        </div>
    </div>
    <div  class="CBK" style="border: 0px;">
		<h4 class="bmjkTitleLine">摸底详情<font   style="font-weight: normal;font-size: 15px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;最新摸底完成时间:<span bzfieldname="ZXMDWCSJ" id="ZXMDWCSJ" ></span></font></h4> 
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span12">
                <div class="bmjkTjgkBlock">
	             	 	<p>
	      				   居民户数总计 <span bzfieldname="JMHSZJ" decimal="false"></span> ，
					             企业家数总计 <span bzfieldname="QYJZJ" decimal="false"></span> ，
						   集体土地征地面积总计 <span bzfieldname="JTTDZDMJZJ" ></span>平方米 ，
						    国有土地征地面积总计 <span bzfieldname="GYTDZDMJZJ" ></span>平方米 ，
						   土地面积总计 <span bzfieldname="TDZMJZJ" ></span>平方米， 
						    摸底估算合计 <span bzfieldname="MDGSHJ" ></span>元。 
					      </p>
		            </div>
                </div>
            </div>
        </div>
    </div>
   <div  class="CBK" style="border: 0px;">
		<h4 class="bmjkTitleLine">拆迁进展详情<font  style="font-weight: normal;font-size: 15px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;最新填报时间 :<span bzfieldname="ZXTBSJ" ></span></font></h4>
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span12">
      			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="">
				    <tr>
				        <td width="25%">
					        <table width="100%" id="QCJZXQ" border="0" cellpadding="0" cellspacing="0" class="table1">
					            <tr>
					                <td style="text-align:center;" width="33%">
					                <div style = "width:100%;height:300px;display:inline-block;" class="text-center" id = "jmcqjzqk" ></div>
					                <p style="width:100%;text-align:center;">居民拆迁进展情况</p>  </td>
					                <td style="text-align:center;" width="33%"><div style = "width:100%;height:300px;display:inline-block;" class="text-center" id = "qycqjzqk" ></div>
					                 <p style="width:100%;text-align:center;">企业拆迁进展情况</p></td>
					                <td style="text-align:center;" width="33%"><div style = "width:100%;height:300px;display:inline-block;" class="text-center" id = "zdcqjzqk" ></div>
					                 <p style="width:100%;text-align:center;">征地面积进展情况&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p></td>
					            </tr>
					        </table>
				        </td>
				    </tr>
			    </table>
                </div>
            </div>
        </div>
    </div>
    <div  class="CBK" style="border: 0px;">
		<h4 class="bmjkTitleLine">拆迁进展详情<font style="font-weight: normal;font-size: 15px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;最新填报时间: <span bzfieldname="ZXTBSJ" ></span></font></h4>
        <div class="container-fluid">
            <div class="row-fluid">
              <div class="span12">
		       <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="">
				    <tr>
				        <td width="25%"><table width="100%" border="0" cellpadding="0" id="cqjzxq" cellspacing="0" class="table3">
				            <tr style="border-bottom: #ccc solid 1px;border-top: #ccc solid 1px;">
				                <th  bzfieldname="DIC_VALUE">&nbsp;</th>
				                <th bzfieldname="SJCQXMS"><div style="text-align: center;">涉及拆迁<br>项目数</div></th>
				                <th bzfieldname="MDWCXMS"><div style="text-align: center;">摸底完成<br>项目数</div></th>
				                <th bzfieldname="WCCQXMS"><div style="text-align: center;">完成拆迁<br> 项目数</div></th>
				                <th bzfieldname="CDYJXMS"><div style="text-align: center;">场地移交<br>项目数</div></th>
				                <th bzfieldname="TDFWYJXMS"><div style="text-align: center;">土地房屋移交<br>项目数</div></th>
				                <th bzfieldname="WTXYQDWCXMS"><div style="text-align: center;"> 委托协议签订完成<br>项目数</div></th>
				                <th bzfieldname="XYJE"><div style="text-align: center;">协议金额<br>（元）</div></th>
				                <th bzfieldname="ZJDWJE"><div style="text-align: center;">资金到位<br>（元）</div></th>
				            </tr>
				        </table></td>
				    </tr>
				</table>
				 <div style="background:none; color:#333;width: 100%;border-top:#ccc solid 1px;"></div>
		      </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>