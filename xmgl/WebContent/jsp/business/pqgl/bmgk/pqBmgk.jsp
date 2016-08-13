<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>

<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<link rel="stylesheet"href="${pageContext.request.contextPath }/css/bmgk/jcBmgkBase.css?version=13">
<link rel="stylesheet"href="${pageContext.request.contextPath }/css/bmgk/jcBmgkStyle.css?version=15">
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/bmgk/bmgk.css?version=2014041bmjkTjgkBlock">
<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/pqBmjkController.do"; 
	$(function(){
		setDefaultNd();
		doInit();
		$("#ND").change(function() {
			var nd = $("#ND").val()==""?"":"&nd="+$("#ND").val();
			queryTjgk(nd);
			queryPqRwxx(nd);
			queryPqChart(nd);
			queryPqNyxx(nd);
		});
		$("#printButton").click(function(){
			$(this).hide();
			window.print();
			$(this).show();
		});
	});
	//页面初始化
	function doInit(){
		var nd = $("#ND").val()==""?"":"&nd="+$("#ND").val();
		queryTjgk(nd);
		queryPqRwxx(nd);
		queryPqChart(nd);
		queryPqNyxx(nd);
	}
	//根据年度查询
	function queryND(){
		init();
	}
	//------------------------------------
	//查询统计概况
	//------------------------------------
	function queryTjgk(nd){
		var action1 = controllername + "?queryPqTjgk"+nd;
		$.ajax({
			url : action1,
			success: function(result){
				 insertTable(result,"TJGK");
			}
		});
	}
	//------------------------------------
	//查询排迁任务信息
	//------------------------------------
	function queryPqRwxx(nd){
		var action1 = controllername + "?queryPqRwxx"+nd;
		$.ajax({
			url : action1,
			success: function(result){
				 insertTable(result,"PQRWXX");
			}
		});
	}
	//------------------------------------
	//查询排迁任务信息
	//------------------------------------
	function queryPqNyxx(nd){
		var action1 = controllername + "?queryPqNyxx"+nd;
		$.ajax({
			url : action1,
			success: function(result){
				 insertTable(result,"PQNYXX");
			}
		});
	}
	//------------------------------------
	//查询排迁图表
	//------------------------------------
	function queryPqChart(nd){
		var action1 = controllername + "?queryPqChart"+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSStackedColumn2D.swf", "myChartID1", "100%", "200");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("myPqChart"); 
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
					if($(subresultmsgobj).attr(str)!==''&&$(subresultmsgobj).attr(str)!=undefined)
					if($(subresultmsgobj).attr(str)!=0){
						switch(flag){
							case 'bd':
								$(this).html('<a href="javascript:void(0);" onclick="_blankXmxxBdxx(\'030001\',\'BMJK_PQ_'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'xm':
								$(this).html('<a href="javascript:void(0);" onclick="_blankXmxx(\'030001\',\'BMJK_PQ_'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'pqrw':
								$(this).html('<a href="javascript:void(0);" onclick="openPqrw(\'排迁任务\',\'BMJK_PQ_'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'pqny':
								$(this).html('<a href="javascript:void(0);" onclick="openPqny(\'排迁内业\',\'BMJK_PQ_'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;	
							case 'pqzh':
								$(this).html('<a href="javascript:void(0);" onclick="openPqzh(\'排迁综合\',\'BMJK_PQ_'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
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

	// 项目
	function _blankXmxx(ywlx, bmjkLx) {
		var nd = $("#ND").val();
		xmxxView(ywlx, nd, bmjkLx);
	}
	//标段
	function _blankXmxxBdxx(ywlx, bmjkLx) {
		var nd = $("#ND").val();
		xmxx_bdxxView(ywlx, nd, bmjkLx);
	}
	
	//自定义钻取--排迁任务
	function openPqrw(title,name){
		var nd = $("#ND").val();
		var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/pqgl/bmgk/pqBmgkDetailRwxx.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
	//自定义钻取--排迁内业
	function openPqny(title,name){
		var nd = $("#ND").val();
		var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/pqgl/bmgk/pqBmgkDetailNyxx.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
	//自定义钻取--排迁综合
	function openPqzh(title,name){
		var nd = $("#ND").val();
		var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/pqgl/bmgk/pqBmgkDetailZhxx.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
</script>
</head>
	<body>
		<div class="container-fluid">
			    <span class="pull-right">
    	<button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
    </span>
			<div class="CBK" style="border: 0px;">
				<h4 class="bmjkTitleLine">
					统计概况&nbsp;&nbsp;
					<select class="span2 year" style="width: 8%" id="ND" name="ND" fieldname="ND" operation="="
						kind="dic" noNullSelect ="true" 
						src="T#GC_JH_SJ: distinct ND as NDCODE:ND:SFYX='1' ORDER BY NDCODE asc">
					</select>
				</h4>
				<div class="container-fluid">
					<div class="row-fluid">
						<div class="span12">
						<div class="bmjkTjgkBlock">
			             	 	<p>
									涉及排迁项目数<span flag="xm" bzfieldname="SJPQXMS"></span>，
									具备排迁图项目<span flag="pqzh" bzfieldname="JBPQTXM"></span>[<span bzfieldname="JBPQTXMBFB"></span>%]，
									已完成排迁项目<span flag="pqzh" bzfieldname="YWCPQXM"></span>[<span bzfieldname="YWCPQXMBFB"></span>%]，
									场地移交项目<span flag="pqzh" bzfieldname="CDYJXM"></span>[<span bzfieldname="CDYJXMBFB"></span>%]。
									截至今日应完成排迁项目<span flag="xm" bzfieldname="JZJRYWCPQXM"></span>[有<span flag="xm" bzfieldname="JBLLDXM"></span>个具备联络单]。
									其中，按期完成项目<span flag="bd" bzfieldname="AQWCXM"></span>[<span bzfieldname="AQWCXMBFB"></span>%]，
									延期完成项目<span flag="bd" bzfieldname="YQWCXM"></span>[<span bzfieldname="YQWCXMBFB"></span>%]，
									超期未完成项目<span flag="bd" bzfieldname="CQWCXM"></span>[<span bzfieldname="CQWCXMBFB"></span>%]。
								</p>
				            </div>
						</div>
					</div>
				</div>
			</div>
			<div  class="CBK" style="border: 0px;">
				<h4 class="bmjkTitleLine">
					排迁任务信息
				</h4>
				<div class="container-fluid">
					<div class="row-fluid">
						<div class="span12">
						 	<div class="bmjkTjgkBlock">
				             	 	<p>
										已分配排迁任务<span flag="pqrw" bzfieldname="YFPPQRW" ></span>，
										涉及项目数/标段数<span flag="pqrw" bzfieldname="SJXM" decimal="false"></span>/<span flag="pqrw" bzfieldname="SJBD" decimal="false"></span>。
										截至今日，应开工任务<span flag="pqrw" bzfieldname="YKGRW"></span>[<span bzfieldname="JZJRYKGRWBFB"></span>%]，
										实际已开工任务<span flag="pqrw" bzfieldname="SJYKGRW" decimal="false"></span>[<span bzfieldname="SJYKGRWBFB"></span>%]。 
										其中，按期开工<span flag="pqrw" bzfieldname="AQKG"></span>[<span bzfieldname="AQKGBFB"></span>%]，
										延期开工<span flag="pqrw" bzfieldname="YQKG" decimal="false"></span>[<span bzfieldname="YQKGBFB" ></span>%]，
										计划应完工任务<span flag="pqrw" bzfieldname="JHYWGRW"></span>[<span bzfieldname="JHYWGRWBFB" ></span>%]，
										实际已完工任务<span flag="pqrw" bzfieldname="SJYWGRW" decimal="false"></span>[<span bzfieldname="SJYWGRWBFB" ></span>%]。
										其中，按期完工<span flag="pqrw" bzfieldname="AQWG"></span>[<span bzfieldname="AQWGBFB" ></span>%]，
										延期完工<span flag="pqrw" bzfieldname="YQWG" decimal="false"></span>[<span bzfieldname="YQWGBFB"></span>%]。
						 	 		</p>
				            </div>
						</div>
						<div class="span7">
							<div id="myPqChart"></div>
						</div>
					</div>
				</div>
			</div>
			<div  class="CBK" style="border: 0px;">
				<h4 class="bmjkTitleLine">
					排迁内业信息
				</h4>
				<div class="container-fluid">
					<div class="row-fluid">
						<div class="span12">
						  <div class="bmjkTjgkBlock">
				              <p>
										已上报预算<span flag="pqny" bzfieldname="YSBYS" decimal="false"></span>，
										共计 <span bzfieldname="YSBYSGJ"></span>元。
										完成评审<span flag="pqny" bzfieldname="WCPS"></span>[<span bzfieldname="WCPSBFB"></span>%]，
										审定值<span bzfieldname="SDZ"></span>元，
										核减<span bzfieldname="HJ"></span>元，
										核减率<span bzfieldname="HJBFB"></span>%，
										已签订合同<span flag="pqny" bzfieldname="YQDHT"></span>，
										共计 <span bzfieldname="YQDHTGJ"></span>元。
										其中，预算值合同<span flag="pqny" bzfieldname="YSZHT"></span>，
										共计 <span bzfieldname="YSZHTGJ"></span>元。
										审定值合同<span flag="pqny" bzfieldname="SDZHT"></span>，
										共计 <span bzfieldname="SDZHTGJ"></span>元。
										已完成支付<span bzfieldname="YWCZF"></span>元，
										支付率<span bzfieldname="ZFL"></span>%。
										  </p>
				            </div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>