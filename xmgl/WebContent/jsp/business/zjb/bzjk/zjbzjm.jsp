<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>

<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/css/bmgk/jcBmgkBase.css?version=13">
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/css/bmgk/jcBmgkStyle.css?version=13">
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/bmgk/bmgk.css?version=20140416">
<style type="text/css">
body {font-size:14px;}
h2 {display:inline; line-height:2em;}

.stageTable .blankTD{
	width:3%;
	padding:0px;
}
.stageTable .contentTD{
	width:11%;
	padding:0px;
	height:100%;
}

.stageTable .blankTD3 {
	width:3%;
	padding:0px;
}

.js_toDown {
	align:center;
	width:100%;
	height:100%;
	position: relative;
}
.js_toDown>.lineDiv {
	margin-top: 0px;
	width:10px;
	background-color: #FFF;
	height:100%;
	z-index:10;
	left:45%;
	position: relative;
}
.js_toDown>.lineDiv>.line {
	z-index:20;
	height:inherit;
	width: 10px;
	position: absolute;
	margin: 0px 5px;
	border-left: 10px solid RGB(9, 163, 179);
}
.js_triangle-down {
	position:absolute;
	left:45%;
	bottom:-16px;
	z-index:30;
	border:10px solid #000;
	border-color: RGB(9,163,179) transparent transparent transparent;
	width:0px;
	height:0px;
	display:inline-block;
}

.js_triangle-up {
	position:absolute;
	left:45%;
	top: -15px;
	z-index:30;
	border:10px solid #000;
	border-color: transparent transparent RGB(9,163,179) transparent;
	width:0px;
	height:0px;
	display:inline-block;
	
}
</style>
<script type="text/javascript">
	var controllername = "${pageContext.request.contextPath}/bmgk/ZjBmgkController.do";
	var p_chartNum = 1;
	$(function(){
		generateNd($("#ND"));
		setDefaultNd("ND");
		$(".ndText").text($("#ND").val());
		//监听年度变化
	    $("#ND").change(function() {
	    	p_chartNum++;
	    	$(".ndText").text($(this).val());
	    	doInit();
	    });
		//打印按钮
		$("#printButton").click(function(){
			$(this).hide();
			window.print();
			$(this).show();
		});
		doInit();
	});
	function doInit(){
		var ndVal = $("#ND").val();
		queryZjbzTjgk(ndVal);
		queryZjfxgl(ndVal);
		queryGcjsTjgk(ndVal);
		queryGcjsTxfx(ndVal);
		queryGcjsList(ndVal);
	}
	function queryZjbzTjgk(nd){
		var action1 = controllername + "?queryZjbzTjgk&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result){
				insertTable(result,"ZJBZ");
			}
		});
	}

	function queryGcjsTjgk(nd){
		var action1 = controllername + "?queryGcjsTjgk&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result){
				insertTable(result,"GCJS");
			}
		});
	}
	function queryGcjsTxfx(nd){
		var action1 = controllername + "?queryGcjsTxfx&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result){
				insertTable(result,"GCJS_TXFX");
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
								$(this).html('<a href="javascript:void(0);" onclick="openDetailJH(\'项目详细列表\',\'TCJh_DETAIL**ZJB**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\'CBK_DETAIL**ZJB**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink_zjbz' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetailZJBZ(\'项目详细列表\',\''+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink_gcjs' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetailGCJS(\'项目详细列表\',\'GCJS_DETAIL**ZJB**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							default:
								$(this).html('<a href="javascript:void(0);" onclick="openDetailJH(\'项目详细列表\',\'TCJh_DETAIL**ZJB**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
								/* var html=$(subresultmsgobj).attr(str);
								$(this).html(html);
								break; */
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
	//年份查询
	function generateNd(ndObj){
		ndObj.attr("src","T#GC_JH_SJ:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
		ndObj.attr("kind","dic");
		ndObj.html('');
		reloadSelectTableDic(ndObj);
		ndObj.val(new Date().getFullYear());
	}
	//查询造价分析管理
	function queryZjfxgl(ndVal){
		queryFxglTjgk(ndVal);
		queryFxglCgfbChart(ndVal);
		queryFxglCgfbTable(ndVal);
	}
	//查询造价分析管理统计概况
	function queryFxglTjgk(nd){
		var action1 = controllername + "?queryFxglTjgk&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result){
				insertTable(result,"FXGL");
			}
		});
	}
	//查询超概分布饼图
	function queryFxglCgfbChart(nd){
		var action = controllername + "?queryFxglCgfbChart&nd="+nd;
		$.ajax({
			url : action,
			dataType:"json",
			async:false,
			success: function(result){
				var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "fxglCgfbChart"+p_chartNum, "100%", "320");  
				myChart.setJSONData(result.msg);  
				myChart.render("fxglCgfbDiv"); 
			}
		});
	}
	//造价分析管理列表
	function queryFxglCgfbTable(nd){
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,tabList);
		defaultJson.doQueryJsonList(controllername+"?queryFxglCgfbTable&nd="+nd,data,tabList,"yangshi");
	}
	function yangshi(){
		$("#tabList").removeClass();
		$($("#tabList tbody>tr:even")).addClass("tableTrEvenColor");
		$("#tabList").addClass("table3 tableList");
		$($("#tabList thead tr:eq(0)")).addClass("tableTheadTr");
	}
	//工程结算列表
	function queryGcjsList(nd){
		var action1 = controllername + "?queryGcjsList&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result)
			{
				addTable(result,"tabList1");
			}
		});
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
						showHtml+='<td class="mc" style="text-align:center;"><a href="javascript:void(0);" onclick="openDetailJSLBLIST(\'项目详细列表\',\''+str+'\',\''+$(subresultmsgobj).attr("ND")+'\')">'+$(subresultmsgobj).attr(str)+'</a></td>';
					}
				//showHtml+="<td class='mc' style='text-align:center; '>"+$(subresultmsgobj).attr(str)+" </td>";
				} 
			});
			showHtml+="</tr>";
		}
		$("#"+tableID).append(showHtml);
		$($("#"+tableID+" tbody>tr:even")).addClass("tableTrEvenColor");
		$("#"+tableID).addClass("tableList");
		$($("#"+tableID+" thead tr:eq(0)")).addClass("tableTheadTr");
	      
	}
	//工程结算列表
	function openDetailJSLBLIST(title,name,nd){
		var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/gcjs_lb_List.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
	//判断是否是项目
	function doBdmc(obj){
		  var bd_name=obj.BDMC;
		  if(bd_name==null||bd_name==""){
			  return '<div style="text-align:center">—</div>';
		  }else{
			  return bd_name;			  
		  }
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
	function BDZS(obj){
		var flag=obj.BDZS;
		var nd=obj.ND;
		  if(flag==0){
			  return '<div style="text-align:right">'+flag+'</div>';
		  }else{
			  return '<a href="javascript:void(0);" onclick="openDetailJHList("项目详细列表","TCJh_DETAIL**ZJB**BDZS",'+nd+')">'+flag+'</a>';			  
		  }
	}
	function KGSJ_SJ(obj){
		var flag=obj.KGSJ_SJ;
		var nd=obj.ND;
		  if(flag==0){
			  return '<div style="text-align:right">'+flag+'</div>';
		  }else{
			  return '<a href="javascript:void(0);" onclick="openDetailJHList("项目详细列表","TCJh_DETAIL**ZJB**KGSJ_SJ",'+nd+')">'+flag+'</a>';			  
		  }
	}
	function JG_SJ(obj){
		var flag=obj.JG_SJ;
		var nd=obj.ND;
		  if(flag==0){
			  return '<div style="text-align:right">'+flag+'</div>';
		  }else{
			  return '<a href="javascript:void(0);" onclick="openDetailJHList("项目详细列表","TCJh_DETAIL**ZJB**JG_SJ",'+nd+')">'+flag+'</a>';			  
		  }
	}
	function TBJS(obj){
		var flag=obj.TBJS;
		var nd=obj.ND;
		  if(flag==0){
			  return '<div style="text-align:right">'+flag+'</div>';
		  }else{
			  return '<a href="javascript:void(0);" onclick="openDetailJHList("项目详细列表","TCJh_DETAIL**ZJB**TBJS",'+nd+')">'+flag+'</a>';			  
		  }
	}
	function YZSDRQ(obj){
		var flag=obj.YZSDRQ;
		var nd=obj.ND;
		  if(flag==0){
			  return '<div style="text-align:right">'+flag+'</div>';
		  }else{
			  return '<a href="javascript:void(0);" onclick="openDetailJHList("项目详细列表","TCJh_DETAIL**ZJB**YZSDRQ",'+nd+')">'+flag+'</a>';			  
		  }
	}
	function CSSDRQ(obj){
		var flag=obj.CSSDRQ;
		var nd=obj.ND;
		  if(flag==0){
			  return '<div style="text-align:right">'+flag+'</div>';
		  }else{
			  return '<a href="javascript:void(0);" onclick="openDetailJHList("项目详细列表","TCJh_DETAIL**ZJB**CSSDRQ",'+nd+')">'+flag+'</a>';			  
		  }
	}
	function SJSDRQ(obj){
		var flag=obj.SJSDRQ;
		var nd=obj.ND;
		  if(flag==0){
			  return '<div style="text-align:right">'+flag+'</div>';
		  }else{
			  return '<a href="javascript:void(0);" onclick="openDetailJHList("项目详细列表","TCJh_DETAIL**ZJB**SJSDRQ",'+nd+')">'+flag+'</a>';			  
		  }
	}
	function objlist(obj,col){
		var flag=obj[col];
		var nd=obj.ND;
		  if(flag==0){
			  return '<div style="text-align:right">'+flag+'</div>';
		  }else{
			  return '<a href="javascript:void(0);" onclick="openDetailJHList(\'项目详细列表\',\'TCJh_DETAIL**ZJB**'+col+'\','+nd+')">'+flag+'</a>';			  
		  }
	}
	function openDetailJHList(title,name,nd){
		var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/tcjhList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
	//造价编制
	function openDetailZJBZ(title,name){
		var nd = $("#ND").val();
		var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/zjbzList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
	//造价编制
	function openDetailGCJS(title,name){
		var nd = $("#ND").val();
		var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/gcjsList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
</script>
</head>
	<body>
		<div class="container-fluid">
			<span class="pull-right">
    			<button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
			</span>
			<div class="ZJBZ" style="border: 0px;">
				<h4 class="bmjkTitleLine">
					造价编制&nbsp;&nbsp;				
					<select class="span2 year" style="width: 8%" id="ND" name="ND"  fieldname="ND" operation="=" noNullSelect ="true"></select> 
				</h4>
				<div class="container-fluid" style="padding:0px 0px;">
					<div class="row-fluid">
						<div class="span12">
							<div class="bmjkTjgkBlock">
								<p>截至目前，<span class="ndText">2014</span>年建管中心负责<span flag="hasLink_jgzxfz_dx" bzfieldname="ZJBZ_TJGK_JGZX_DX">XX</span>项（<span flag="hasLink_jgzxfz_zx" bzfieldname="ZJBZ_TJGK_JGZX_ZX">XX</span>个子项）重点工程。
								其中，新建<span flag="hasLink__jgzxfz_xjdx" bzfieldname="ZJBZ_TJGK_XINJ_DX"></span>项（<span  flag="hasLink__jgzxfz_xjzx" bzfieldname="ZJBZ_TJGK_XINJ_ZX"></span>个子项），续建<span flag="hasLink__jgzxfz_xujdx" bzfieldname="ZJBZ_TJGK_XUJ_DX"></span>项（<span flag="hasLink__jgzxfz_xujzx" bzfieldname="ZJBZ_TJGK_XUJ_ZX"></span>个子项）。
								</p>
							</div>
						</div>
						<div class="span12" style="margin-left:0px;min-width:960px;">
							<table border="0" class="stageTable">
								<tr>
									<td class="contentTD" rowspan="5">
										<div class="divTitle">
										前置情况
										</div>
										<div class="divContent">
											已完成施工图设计<br/>
											<span class="fieldValue-A" flag="hasLink_zjbz" bzfieldname="ZJBZ_YWCSGTSJ_ZX">XX</span>子项
											<span class="fieldValue-A" flag="hasLink_zjbz" bzfieldname="ZJBZ_YWCSGTSJ_BD">XX</span>标段
										</div>
									</td>
									<td class="blankTD">
									</td>
									<td class="contentTD">
									</td>
									<td class="blockTD blankTD">
										<div class="containerDiv-upToRight">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
										</div>
									</td>
									<td class="contentTD">
										<div class="block-red">
											暂缓造价编制 <br/>
											<span class="fieldValue-A" flag="hasLink_zjbz" bzfieldname="ZJBZ_ZHZJBZ_ZX">XX</span>子项
											<span class="fieldValue-A" flag="hasLink_zjbz" bzfieldname="ZJBZ_ZHZJBZ_BD">XX</span>标段
										</div>
									</td>
									<td class="blankTD">
										<div class="containerDiv-upToRight">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
										</div>
									</td>
									<td class="contentTD">
										<div class="block-red">
											未确定造价咨询机构 <br/>
											<span class="fieldValue-A" flag="hasLink_zjbz" bzfieldname="ZJBZ_WQDZXJG_ZX">XX</span>子项
											<span class="fieldValue-A" flag="hasLink_zjbz" bzfieldname="ZJBZ_WQDZXJG_BD">XX</span>标段
										</div>
									</td>
									<td class="blankTD">
										<div class="containerDiv-upToRight">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
										</div>
									</td>
									<td class="contentTD">
										<div class="block-red">
											未完成造价编制 <br/>
											<span class="fieldValue-A"   flag="hasLink_zjbz" bzfieldname="ZJBZ_WWCZKBZ_ZX">XX</span>子项
											<span class="fieldValue-A"  flag="hasLink_zjbz"  bzfieldname="ZJBZ_WWCZKBZ_BD">XX</span>标段
										</div>
									</td>
									<td class="blankTD">
										<div class="containerDiv-upToRight">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
										</div>
									</td>
									<td class="contentTD">
										<div class="block-blue">
											不需要送财审<br/>
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_BXYSCS_ZX">XX</span>子项
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_BXYSCS_BD">XX</span>标段
										</div>
									</td>
									<td class="line-H blankTD">
										<div class="div-H" style="width:100%;"></div>
									</td>
									<td class="contentTD" style="height:40px;">
										<div class="containerDiv-rightToDown">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td class="blankTD">
									</td>
									<td class="contentTD">
									</td>
									<td class="line-S blankTD">
										<div class="div-S" style="height:30px;"></div>
									</td>
									<td class="contentTD"></td>
									<td class="line-S blankTD">
										<div class="div-S" style="height:40px;"></div>
									</td>
									<td class="contentTD">
										<div class="containerDiv-upToDown" style="height:30px;">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-down"></div>
										</div>
									</td>
									<td class="line-S blankTD">
										<div class="div-S" style="height:30px;"></div>
									</td>
									<td class="contentTD">
										<div class="containerDiv-upToDown" style="height:30px;">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-down"></div>
										</div>
									</td>
									<td class="line-S blankTD">
										<div class="div-S" style="height:30px;"></div>
									</td>
									<td class="contentTD"></td>
									<td class="blankTD"></td>
									<td class="contentTD">
										<div class="containerDiv-upToDown" style="height:30px;">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-down"></div>
										</div>
									</td>
								</tr>
								<tr>
									<td class="line-S blankTD">
										<div class="containerDiv-leftToRight">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
										</div>
										<div class="div-S" style="height:55%;top:45%;"></div>
									</td>
									<td class="contentTD">
										<div class="block-blue">
											建管中心造价部负责 <br/>
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZGZXZJBFZ_ZX">XX</span>子项
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZGZXZJBFZ_BD">XX</span>标段
										</div>
									</td>
									<td class="line-S blankTD">
										<div class="div-S" style="height:80px;"></div>
										<div class="containerDiv-leftToRight">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
										</div>
									</td>
									<td class="contentTD">
										<div class="block-blue">
											具备造价编制条件 <br/>
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_JBZJBZTJ_ZX">XX</span>子项
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_JBZJBZTJ_BD">XX</span>标段
										</div>
									</td>
									<td class="line-S blankTD">
										<div class="div-S" style="height:100%;"></div>
										<div class="containerDiv-leftToRight">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
										</div>
									</td>
									<td class="contentTD">
										<div class="block-blue">
											已确定造价咨询机构 <br/>
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_YQDZJZXJG_ZX">XX</span>子项
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_YQDZJZXJG_BD">XX</span>标段
										</div>
									</td>
									<td class="line-S blankTD" style="height:100%;">
										<div class="div-S" style="height:100%;"></div>
										<div class="containerDiv-leftToRight">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
										</div>
									</td>
									<td class="contentTD">
										<div class="block-blue">
											已完成造价编制  <br/>
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_YWCZJBZ_ZX">XX</span>子项
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_YWCZJBZ_BD">XX</span>标段
										</div>
									</td>
									<td class="line-S blankTD">
										<div class="div-S" style="height:100%;"></div>
										<div class="containerDiv-leftToRight">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
										</div>
									</td>
									<td class="contentTD">
										<div class="block-blue">
											已送财审 <br/>
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_YSCS_ZX">XX</span>子项
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_YSCS_BD">XX</span>标段
										</div>
									</td>
									<td class="line-S blankTD">
										<div class="containerDiv-leftToRight">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
										</div>
										<div class="div-S" style="height:55%;top:45%;"></div>
									</td>
									<td class="contentTD">
										<div class="block-blue">
											已完成财审<br/>
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_YWCCSZ_ZX">XX</span>子项
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_YWCCSZ_BD">XX</span>标段
										</div>
									</td>
								</tr>
								<tr>
									<td class="line-S blankTD" style="height:100%;">
										<div class="div-S" style="height:100%;"></div>
									</td>
									<td class="contentTD">
									</td>
									<td class="line-S blankTD" style="height:100%;">
										<div class="div-S" style="height:100%;"></div>
									</td>
									<td class="contentTD"></td>
									<td class="line-S blankTD">
										<div class="div-S" style="height:25px;"></div>
									</td>
									<td class="line-S contentTD">
										<div class="containerDiv-upToDown" style="height:20px;">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-up"></div>
										</div>
									</td>
									<td class="line-S blankTD" style="height:100%;">
										<div class="div-S" style="height:25px;"></div>
									</td>
									<td class="line-S contentTD">
										<div class="containerDiv-upToDown" style="height:20px;">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-up"></div>
										</div>
									</td>
									<td class="line-S blankTD">
										<div class="div-S" style="height:25px;"></div>
									</td>
									<td class="contentTD"></td>
										<td class="line-S blankTD">
										<div class="div-S" style="height:25px;"></div>
									</td>
									<td class="contentTD"></td>
								</tr>
								<tr>
									<td class="line-S blankTD" style="height:100%">
										<div class="div-S" style="height:100%"></div>
									</td>
									<td class="contentTD">
									</td>
									<td class="line-S blankTD" style="height:100%">
										<div class="div-S" style="height:100%"></div>
									</td>
									<td class="contentTD"></td>
									<td class="blankTD">
										<div class="containerDiv-downToRight">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
										</div>
									</td>
									<td class="contentTD">
										<div class="block-blue" >
											部分确定造价咨询机构 <br/>
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_BFQDZJZXJG_ZX">XX</span>子项
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_BFQDZJZXJG_YWBD">XX</span>确定标段<br/>
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_BFQDZJZXJG_WWBD">XX</span>未确定标段
										</div>
									</td>
									<td class="blankTD">
										<div class="containerDiv-downToRight">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
										</div>
									</td>
									<td class="contentTD" style="padding:0px;">
										<div class="block-blue" style="height:100%;">
											部分完成造价编制 <br/>
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_BFWCZJBZ_ZX">XX</span>子项
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_BFWCZJBZ_YWBD">XX</span>已完标段<br/>
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_BFWCZJBZ_WWBD">XX</span>未完标段
										</div>
									</td>
									<td class="blankTD">
										<div class="containerDiv-downToRight">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
										</div>
									</td>
									<td class="contentTD">
										<div class="block-red">
											未送财审 <br/>
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_WSCS_ZX">XX</span>子项
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_WSCS_BD">XX</span>标段
										</div>
									</td>
									<td class="blankTD">
										<div class="containerDiv-downToRight">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
										</div>
									</td>
									<td class="contentTD">
										<div class="block-red">
											未完成财审<br/>
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_WWCCS_ZX">XX</span>子项
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_WWCCS_BD">XX</span>标段
										</div>
									</td>
								</tr>
								<tr>
									<td class="contentTD"></td>
									<td class="blockTD blankTD">
										<div class="containerDiv-downToRight">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
										</div>
									</td>
									<td class="contentTD">
										<div class="block-blue">
											其他法人单位负责 <br>
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="QTBMFZ_ZX">XX</span>子项
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="QTBMFZ_BD">XX</span>标段
										</div>
									</td>
									<td class="blockTD blankTD">
										<div class="containerDiv-downToRight">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
										</div>
									</td>
									<td class="contentTD">
										<div class="block-blue">
											不需要编制造价 <br/>
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_BXYBZZJ_ZX">XX</span>子项
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_BXYBZZJ_BD">XX</span>标段
										</div>
									</td>
									<td  class="contentTD" colspan=7>
										<div class="containerDiv-leftToRight">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
										</div>
									</td>
									<td class="contentTD">
										<div class="block-blue">
											预算加签证 <br/>（无需送财审）  <br/>
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_BXYBZZJ_ZX">XX</span>子项
											<span class="fieldValue-A"  flag="hasLink_zjbz" bzfieldname="ZJBZ_BXYBZZJ_BD">XX</span>标段
										</div>
									</td>
								</tr>
								
							</table>
						</div>
						<div class="span2">
							
						</div>
					</div>
				</div>
			</div>
			<!--  -->
			<div class="" style="border: 0px;">
			<h4 class="bmjkTitleLine">
				工程结算
			</h4>
			<div class="row-fluid">
				<div class="span12 GCJS">
					<div class="bmjkTjgkBlock">
						<p>截至目前，建管中心共结算<span flag="hasLinkTCJH" bzfieldname="GCJS_TJGK_JGZXGJS">XX</span>子项。<span class="ndText">2014</span>年建管中心已完工<span flag="hasLinkTCJH" bzfieldname="GCJS_TJGK_WGZX">XX</span>子项（<span flag="hasLinkTCJH" bzfieldname="GCJS_TJGK_WGBD">XX</span>个标段）（含甩项）。 <br>
						—按施工合同类型划分：施工合同<span flag="hasLinkTCJH" bzfieldname="GCJS_TJGK_SGHTZX">XX</span>子项（<span flag="hasLinkTCJH" bzfieldname="GCJS_TJGK_SGHTBD">XX</span>个标段），
						BT合同<span flag="hasLinkTCJH" bzfieldname="GCJS_TJGK_BTSGHTZX">XX</span>子项（<span flag="hasLinkTCJH" bzfieldname="GCJS_TJGK_BTSGHTBD">XX</span>个标段）。 <br>
						—按施工情况划分：甩项结算<span flag="hasLink_gcjs" bzfieldname="GCJS_TJGK_SXJSZX">XX</span>子项（<span flag="hasLink_gcjs" bzfieldname="GCJS_TJGK_SXJSBD">XX</span>个标段），
						竣工（完工）结算<span flag="hasLink_gcjs" bzfieldname=GCJS_TJGK_JGJSZX>XX</span>子项（<span flag="hasLink_gcjs" bzfieldname="GCJS_TJGK_JGJSBD">XX</span>个标段）。<br>
						</p>
					</div>
				</div>
				<div class="span12" style="margin-left:0px;min-width:960px;">
					<table border="0" class="stageTable GCJS_TXFX">
						<tr>
							<td class="contentTD" colspan=2>
								<div class="divTitle">
								<span class="ndText">2014</span>
								</div>
								<div class="divContent">
									完工工程<br/>
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WGGCZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WGGCBD">XX</span>标段
								</div>
							</td>
							<td></td>
							<td class="js_tdContent"></td>
							<td></td>
							<td class="js_tdContent"></td>
							<td></td>
							<td class="js_tdContent"></td>
							<td></td>
							<td class="js_tdContent"></td>
							<td></td>
							<td class="contentTD">
								<div class="divTitle" style="margin-top: 15px">
								结算报告
								</div>
								<div class="divContent">
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_JSBGZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_JSBGBD">XX</span>标段
								</div></td>
							<td class="line-S blankTD" style="height:40px;">
								<div class="containerDiv-leftToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								<div class="div-S" style="height:50px;"></div>
								</div>
							</td>
							<td class="contentTD" style="vertical-align:bottom;">
								<div class="block-red">
									未列入审计计划<br/>
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WLRSJJHZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WLRSJJHBD">XX</span>标段
								</div>
							</td>
						</tr>
						<tr>
							<td  class="blankTD line-S" rowspan=4 style="height:120px;">
								<div class="containerDiv-downToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
								<div class="div-S" style="height:100%"></div>
							</td>
							<td class="js_tdContent">
							</td>
							<td></td>
							<td class="js_tdContent"></td>
							<td></td>
							<td class="js_tdContent"></td>
							<td></td>
							<td class="js_tdContent"></td>
							<td></td>
							<td class="js_tdContent"></td>
							<td></td>
							<td class="contentTD" rowspan="4">
								<div class="containerDiv-upToDown" style="height:124px;">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-up"></div>
								</div></td>
							<td class="line-S" rowspan="2">
								<div class="containerDiv-downToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
							</td>
							<td class="contentTD">
								<div class="containerDiv-upToDown" style="height:21px;">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-down"></div>
								</div>
							</td>
						</tr>
						<tr>
							<td class="contentTD" rowspan="3">
								<div class="block-blue" style="margin-bottom: 20px">
									未提报<br/>
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WTBZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WTBBD">XX</span>标段
								</div>
							</td>
							<td class="blankTD3" rowspan="2">
								<div class="containerDiv-upToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
							</td>
							<td class="contentTD" rowspan="2">
								<div class="block-blue">
									未超期<br/>
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WTBWCQZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WTBWCQBD">XX</span>标段
								</div>
							</td>
							<td></td>
							<td class="js_tdContent"></td>
							<td></td>
							<td class="js_tdContent"></td>
							<td></td>
							<td class="js_tdContent"></td>
							<td class="blankTD">
								
							</td>
							<td class="contentTD">
								<div class="block-blue">
									已列入审计计划<br/>
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_YLRSJJHZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_YLRSJJHBD">XX</span>标段
								</div>
							</td>
						</tr>
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td class="js_tdContent"></td>
							<td ></td>
							<td class="js_tdContent"></td>
							<td ></td>
							<td class="line-H line-S" rowspan=2 style="width:0%;">
								<div class="div-top-H" style="width: 60%;left:50%;top: 45%;margin-left: -2px;margin-top:1px;"></div>
								<div class="div-S" style="height:50%;top:50%;"></div>
							</td>
							<td  class="contentTD line-H" rowspan=2>
								<div class="containerDiv-upToDown" style="height:51px;">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-down"></div>
								</div>
								<div class="div-H" style="width:50%;"></div>
							</td>
						</tr>
						<tr>
							<td class="line-H line-S blockTD blankTD3">
								<div class="containerDiv-downToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
								<div class="div-top-H" style="width:54%;"></div>
								<div class="div-S" style="height:50%;"></div>
							</td>
							<td class="contentTD">
								<div class="block-red">
									已超期<br/>
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WTBYCQZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WTBYCQBD">XX</span>标段
								</div>
							</td>
							<td></td>
							<td class="js_tdContent"></td>
							<td></td>
							<td class="js_tdContent"></td>
							<td></td>
							<td class="js_tdContent"></td>
						</tr>
						<tr>
							<td  class="blankTD line-S" rowspan="5" style="height:150px;">
								<div class="containerDiv-downToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
								<div class="div-S" style="height:40%"></div>
							</td>
							<td class="contentTD" rowspan="5">
								<div class="block-blue">
									已提报 <br/>
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_YTBZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_YTBBD">XX</span>标段
								</div>
							</td>
							<td></td>
							<td class="contentTD" rowspan="3">
								<div class="block-blue">
									需确定咨询机构 <br/>
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_XQDZXJGZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_XQDZXJGBD">XX</span>标段
								</div>
							</td>
							<td class="blankTD3">
								<div class="containerDiv-upToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
							</td>
							<td class="contentTD">
								<div class="block-red">
									未确定造价咨询机构<br/>
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WQDZJZZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WQDZJZBD">XX</span>标段
								</div></td>
							<td class="blankTD3">
								<div class="containerDiv-upToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
							</td>
							<td class="contentTD">
								<div class="block-red">
									未审完<br/>
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WSWZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WSWBD">XX</span>标段
								</div></td>
							<td></td>
							<td class="js_tdContent"></td>
							<td class="blankTD3">
								<div class="containerDiv-upToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
							</td>
							<td class="contentTD">
								<div class="block-blue">
									财审完<br/>
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_CSWZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_CSWBD">XX</span>标段
								</div></td>
							<td class="blankTD line-S">
								<div class="div-S" style="height:100%"></div>
							</td>
							<td class="contentTD">
								<div class="block-red">
									审计未完<br/>
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_SJWWZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_SJWWBD">XX</span>标段
								</div>
							</td>
						</tr>
						<tr>
							<td class="blankTD3">
								<div class="containerDiv-upToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
							</td>
							<td class="line-H line-S blankTD3">
								<div class="div-H" style="width: 45%"></div>
								<div class="div-S"></div>
							</td>
							<td class="contentTD">
								<div class="containerDiv-upToDown" style="height:30px;">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-down"></div>
								</div>
							</td>
							<td class="line-S blankTD3">
								<div class="div-S"></div></td>
							<td class="contentTD">
								<div class="containerDiv-upToDown" style="height:30px;">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-down"></div>
								</div>
							</td>
							<td></td>
							<td class="js_tdContent"></td>
							<td class="line-S blankTD3">
								<div class="div-S"></div></td>
							<td class="contentTD">
								<div class="containerDiv-upToDown" style="height:30px;">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-up"></div>
								</div></td>
							<td class="line-S">
								<div class="div-S" style="height:100%"></div>
							</td>
							<td class="contentTD">
								<div class="containerDiv-upToDown" style="height:30px;">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-down"></div>
								</div></td>
						</tr>
						<tr>
							<td class="line-S line-H blankTD3">
								<div class="div-top-H" style="width:45%; margin-top:40px"></div>
								<div class="div-S" style="height:55px;"></div>
							</td>
							<td class="blockTD blankTD3">
								<div class="containerDiv-downToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
							</td>
							<td class="contentTD">
								<div class="block-blue">
									已确定造价咨询机构<br/> 
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_YQDZJZZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_YQDZJZBD">XX</span>标段
								</div></td>
							<td class="line-S blankTD3">
								<div class="div-S" style="height:49%;"></div>
								<div class="containerDiv-leftToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								</div></td>
							<td class="contentTD">
								<div class="block-blue">
									已审完<br/>  
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_YSWZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_YSWBD">XX</span>标段
								</div></td>
							
							<td class="blankTD3">
								<div class="containerDiv-leftToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								</div></td>
							<td class="contentTD">
								<div class="block-blue">
									已送财审 <br/>
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_YSCSZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_YSCSBD">XX</span>标段
								</div></td>
							<td class="line-S blankTD3">
								<div class="div-S" style="height:49%;"></div>
								<div class="containerDiv-leftToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								</div></td>
							<td class="contentTD">
								<div class="block-red">
									未财审完 <br/> 
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WCSWZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WCSWBD">XX</span>标段
								</div>
							</td>
							<td class="blankTD">
								<div class="containerDiv-downToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
							</td>
							<td class="contentTD">
								<div class="block-blue">
									审计已完<br/>
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_SJYWZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_SJYWBD">XX</span>标段
								</div>
							</td>
						</tr>
						<tr>
							<td class="line-S blockTD blankTD3">
								<div class="div-S" style="height:46px;"></div>
								<div class="containerDiv-downToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
							</td>
							<td class="contentTD">
								<div class="block-blue">
									不需要咨询机构<br/> 
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_BXQDZXJGZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_BXQDZXJGBD">XX</span>标段
								</div>
							</td>
							<td class="line-H contentTD" colspan="5">
								<div class="div-bottom-H" style="width:100%;"></div>
							</td>
							<td class="line-H contentTD" style="height:40px;">
								<div class="containerDiv-rightToUp">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-up"></div>
								</div>
							</td>
							<td></td>
							<td class="js_tdContent">
							</td>
							<td></td>
							<td class="js_tdContent">
							</td>
						</tr>
						
						<tr>
							<td class="blockTD blankTD3">
								<div class="containerDiv-downToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
							</td>
							<td class="contentTD">
								<div class="block-red" style="margin-top:5px">
									未确定<br/>
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WQDZX">XX</span>子项
									<span class="fieldValue-A"  flag="hasLink_gcjs" bzfieldname="GCJS_TXFX_WQDBD">XX</span>标段
								</div></td>
							<td></td>
							<td class="js_tdContent"></td>
							<td></td>
							<td class="js_tdContent"></td>
							<td></td>
							<td class="js_tdContent"></td>
							<td></td>
							<td class=""></td>
							<td class=""></td>
							<td class=""></td>
						</tr>
					</table>
				</div>
				<div class="span12" style="margin-left: 0px;">
							<div class="B-small-from-table-autoConcise">
							<div class="overFlowX" style="height:100%; ">
							<div style="height: 15px;">
								</div>
								<table width="100%"    align="center" cellpadding="0"
									cellspacing="0" class="">
									<tr >
										<td width="100%">
											<table width="100%" border="0" cellpadding="0" id="tabList1"
												cellspacing="0" class="table3">
												<thead>
											<tr >
											<th bzfieldname="ND"  >
													<div style="text-align: center;" >年份</div>
											</th>
											<th bzfieldname="BDZS"  >
													<div style="text-align: center;" >计划项目数</div>
											</th>
											<th bzfieldname="KGSJ_SJ"  >
													<div style="text-align: center;" >实施项目数</div>
											</th>
											<th bzfieldname="JG_SJ"  >
													<div style="text-align: center;" >已竣工（完工）</div>
											</th>
											<th  bzfieldname="TBJS" >
													<div style="text-align: center;" >已提报</div>
											</th>
											<th bzfieldname="YZSDRQ"  >
													<div style="text-align: center;" >建管中心已审定</div>
											</th>
											<th bzfieldname="CSSDRQ"   >
													<div style="text-align: center;" >财政已审定（已结算）</div>
											</th>
											<th bzfieldname="SJSDRQ"  >
													<div style="text-align: center;" >审计已审定</div>
											</th>
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
			<!--  -->
				<h4 class="bmjkTitleLine">
					造价分析管理
				</h4>
				<div class="container-fluid" style="padding:0px 0px;">
					<div class="row-fluid">
						<div class="span12 FXGL" id="FXGL">
							<div class="bmjkTjgkBlock">
								<p>经统计，<span class="ndText">2014</span>年建管中心已结算<span flag="hasLink_gcjs" bzfieldname="FXGL_TJGK_YJS_ZX"></span>子项（<span flag="hasLink_gcjs" bzfieldname="FXGL_TJGK_YJS_BD"></span>个标段）。
								</p>
							</div>
						</div>
						<div class="span3" style="margin-left:0px;">
							<div id="fxglCgfbDiv"></div>
						</div>
						<div class="span9">
							<div class="B-small-from-table-autoConcise">
								<form method="post"
									action="${pageContext.request.contextPath }/insertdemo.do"
									id="queryForm" style="display:none;">
									<table class="B-table" width="100%">
										<!--可以再此处加入hidden域作为过滤条件 -->
										<TR style="display: none;">
											<TD class="right-border bottom-border"></TD>
											<TD class="right-border bottom-border">
												<INPUT type="text" class="span12" kind="text"
													fieldname="rownum" value="1000" operation="<=">
											</TD>
											<td>
												人员角色隐藏条件-用于控制按钮，不传入后台
											</td>
											<TD class="right-border bottom-border">
												<INPUT type="text" class="span12" kind="text" id="ryjs_cond">
											</TD>
										</TR>
										<!--可以再此处加入hidden域作为过滤条件 -->
									</table>
								</form>
								<div style="height: 5px;">
								</div>
								<table class="table-hover table-activeTd B-table" id="tabList"
									width="100%" type="single" pageNum="7">
									<thead>
										<tr>
											<th name="XH" id="_XH">
												&nbsp;#&nbsp;
											</th>
											<th fieldname="XMMC" tdalign="left"
												maxlength="10">
												&nbsp;计划名称&nbsp;
											</th>
											<th fieldname="BDMC"
												maxlength="10" Customfunction="doBdmc">
												&nbsp;标段名称&nbsp;
											</th>
											<th fieldname="GSTZ" tdalign="right">
												&nbsp;概算投资<br>(元)&nbsp;
											</th>
											<th fieldname="YSTZ" tdalign="right">
												&nbsp;预算投资<br>(元)&nbsp;
											</th>
											<th  fieldname="LBJ" tdalign="right">
												&nbsp;拦标价<br>(元)&nbsp;
											</th>
											<th fieldname="ZBJ" tdalign="right">
												&nbsp;中标价<br>(元)&nbsp;
											</th>
											<th fieldname="JSZ"
												tdalign="right">
												&nbsp;结算值<br>(元)&nbsp;
											</th>
											<th fieldname="CGBL"
												tdalign="center">
												&nbsp;超概比例&nbsp;
											</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
					
					</div>
				</div>
			</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="ywid" id="ywid">
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="jhsj.XMBH" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id = "queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>