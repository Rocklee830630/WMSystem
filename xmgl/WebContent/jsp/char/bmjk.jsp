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
	var controllername = "${pageContext.request.contextPath }/tcjh/jhjk/JhjkController.do";
	//var controllername = "${pageContext.request.contextPath }/bmgk/GcHtBmgkController.do";
	var p_chartNum = 1;
	$(function(){
		generateNd($("#ND"));
		setDefaultNd("ND");
		$(".ndText").text($("#ND").val());
		doInit();
		$("input[name='CBKXMFL']").click(function(){
			var valStr = $(this).val();
			var liHeight=$("#cbkxmxx ul li").height();
			$("#cbkxmxx ul").prepend($("#cbkxmxx ul li[name='TAB_"+valStr+"']").css("height","0px").animate({
				height:liHeight+"px"
			},"fast"));
		});
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
	});
	function doInit(){
		var ndVal = $("#ND").val();
		queryCBK(ndVal);
		queryJhgzColumn2d(ndVal);
		queryJhxms(ndVal);
		queryJhgk(ndVal);
		queryJhbzTjgk(ndVal);
		queryJhgzlb();
		tableClass("jhgzlb");
	}
	//储备库
	function queryCBK(ndVal){
		$("input[name='CBKXMFL']:first").click();
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,TAB_XMXZ);
		defaultJson.doQueryJsonList(controllername+"?queryXMFR&nd="+ndVal,data,TAB_XMFR,null,true);//按项目法人
		defaultJson.doQueryJsonList(controllername+"?queryZRBM&nd="+ndVal,data,TAB_ZRBM,null,true);//按部门
		defaultJson.doQueryJsonList(controllername+"?queryXMLX&nd="+ndVal,data,TAB_XMLX,null,true);//按项目类型
		defaultJson.doQueryJsonList(controllername+"?queryXMJD&nd="+ndVal,data,TAB_XMJD,null,true);//按项目进度
		defaultJson.doQueryJsonList(controllername+"?queryXMXZ&nd="+ndVal,data,TAB_XMXZ,null,true);//按项目性质
		queryXMFLChart(ndVal);//项目分类
		queryJHBZChart(ndVal);//计划编制
		tableClass("TAB_XMFR");
		tableClass("TAB_ZRBM");
		tableClass("TAB_XMLX");
		tableClass("TAB_XMJD");
		tableClass("TAB_XMXZ");
	}
function tableClass(tableID){
	$("#"+tableID).removeClass();
	$($("#"+tableID+" tbody>tr:even")).addClass("tableTrEvenColor");
	$("#"+tableID).addClass("table3 tableList");
	$($("#"+tableID+" thead tr:eq(0)")).addClass("tableTheadTr");
	$($($("#"+tableID+" thead tr:eq(0)")).find("th")).addClass("tableTdAlign");
}


		//年份查询
	function generateNd(ndObj){
		ndObj.attr("src","T#GC_TCJH_XMCBK:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
		ndObj.attr("kind","dic");
		ndObj.html('');
		reloadSelectTableDic(ndObj);
		ndObj.val(new Date().getFullYear());
	}
	//年度项目分类情况
	function queryXMFLChart(nd){
		var action = controllername + "?queryXMFLChart&nd="+nd;
		$.ajax({
			url : action,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "XMFLChart"+p_chartNum, "100%", "400");
			     myChart.setJSONData(result.msg);  
			     myChart.render("XMFLChartDiv"); 
			}
		});
	}
 	//年度项目分类情况
	function queryJhxms(nd){
		var action = controllername + "?queryJhxms&nd="+nd;
		$.ajax({
			url : action,
			dataType:"json",
			async:false,
			success: function(result){
				var obj=convertJson.string2json1(result.msg);
				$("#JHXMS").html('<a href="javascript:void(0);" onclick="openDetailXDK(\'项目详细列表\',\'XDK_DETAIL**JH**JHXMS\')">'+obj.response.data[0].JHXMS+'</a>');
				
			}
		});
	} 
	//计划编制
	function queryJHBZChart(nd){
		var action = controllername + "?queryJHBZChart&nd="+nd;
		$.ajax({
			url : action,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "JHBZChart"+p_chartNum, "100%", "400");
			     myChart.setJSONData(result.msg);  
			     myChart.render("JHBZChartDiv"); 
			}
		});
	}
	function queryJhgzColumn2d(nd){
		//var controllername = "${pageContext.request.contextPath }/bmgk/GcHtBmgkController.do";
		var action1 = controllername + "?queryJhgzColumn2d&nd="+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSStackedColumn2D.swf", "myChartID1", "100%", "300");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("JHGZ_Column2D"); 
			}
		});
	}
	//
	function xmfl(tiaojian){
		var tiaojian1=tiaojian.replace(/%/g,"%25");
		var nd1=$("#ND").val();
		var proKey="CBK_DETAIL**JH**XMFLChartDiv";
		var condition = "nd="+nd1+"&tiaojian="+tiaojian1+"&proKey="+proKey;
		var  xmsc = {"title":"项目信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/cbkList.jsp?"+condition,"modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
	function jhbz(tiaojian){
		var tiaojian1=tiaojian.replace(/%/g,"%25");
		var nd1=$("#ND").val();
		var proKey,xmsc,condition;
		//之前是链接到下达库，现改为链接到计划
		//20150325 zhaiyl 之前全部链接计划，先改为无法编制链接下达库
		if('1'==tiaojian1){
			 proKey="XDK_DETAIL**JH**JHBZChartDiv";
			 condition = "nd="+nd1+"&tiaojian="+tiaojian1+"&proKey="+proKey;
			 xmsc = {"title":"项目信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/xdkList.jsp?"+condition,"modal":"1"};
		}else{
		 	 proKey="JH_DETAIL**JH**JHBZChartDiv";
		 	 condition = "nd="+nd1+"&tiaojian="+tiaojian1+"&proKey="+proKey;
		 	 xmsc = {"title":"项目信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/tcjhbzList.jsp?"+condition,"modal":"1"};
		 	}
		$(window).manhuaDialog(xmsc);
	}
	//计划跟踪链接
	function jhgz(tiaojian,lie){
		switch(parseInt(lie)){
		case 15 :
			lie='KGSJ';
			break;
		case 16:
			lie='WGSJ';
			break;
		case 1:
			lie='KYPF';
			break;
		case 2:
			lie='HPJDS';
			break;
		case 3:
			lie='GCXKZ';
			break;
		case 14:
			lie='SGXK';
			break;
		case 4:
			lie='CBSJPF';
			break;
		case 6:
			lie='CQT';
			break;
		case 7:
			lie='PQT';
			break;
		case 5:
			lie='SGT';
			break;
		case 10:
			lie='TBJ';
			break;
		case 11:
			lie='CS';
			break;
		case 13:
			lie='JLDW';
			break;
		case 12:
			lie='SGDW';
			break;
		case 8:
			lie='ZC';
			break;
		case 9:
			lie='PQ';
			break;
		case 17:
			lie='JG';
			break;
		
		default:
			lie='';
		}
		var tiaojian1=tiaojian.replace(/%/g,"%25");
		var lie1=lie.replace(/%/g,"%25");
		var nd1=$("#ND").val();
		var proKey="JHGZ_DETAIL**JH**JHGZChartDiv";
		var condition = "nd="+nd1+"&tiaojian="+tiaojian1+"&proKey="+proKey+"&lie="+lie1;
		var  xmsc = {"title":"项目信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/tcjhgzlist.jsp?"+condition,"modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
	//统筹概况
	function queryJhgk(nd) {
		$.ajax({
			url:controllername+"?queryJhgk&nd="+nd,
			success: function(result) {
				insertTable(result, "GC_JDGL_TCGK");
			}
		});
	}
	//计划编制统计概况
	function queryJhbzTjgk(nd) {
		$.ajax({
			url:controllername+"?queryJhbzTjgk&nd="+nd,
			success: function(result) {
				insertTable(result, "GC_JHBZ_TJGK");
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
								$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\'CBK_DETAIL**COMMON**jgzxfz_zx\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink__jgzxfz_xjdx' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\'CBK_DETAIL**COMMON**jgzxfz_xjdx\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink__jgzxfz_xjzx' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\'CBK_DETAIL**COMMON**jgzxfz_xjzx\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink__jgzxfz_xujdx' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\'CBK_DETAIL**COMMON**jgzxfz_xujdx\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink__jgzxfz_xujzx' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\'CBK_DETAIL**COMMON**jgzxfz_xujzx\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLinkTCJH' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetailJH(\'项目详细列表\',\'TCJh_DETAIL**GCB**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLinkXDK' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetailXDK(\'项目详细列表\',\'XDK_DETAIL**JH**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'nohasLink' :
								var html=$(subresultmsgobj).attr(str);
								$(this).html(html);
								break;
							default:
								$(this).html('<a href="javascript:void(0);" style="color: #FFFF00" onclick="openDetailJH(\'项目详细列表\',\'TCJh_DETAIL**GCB**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
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
	function openDetailJH(title,name){
		var nd = $("#ND").val();
		var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/tcjhList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
	// 项目
	function _blankXmxx(ywlx, bmjkLx) {
		var nd = $("#ND").val();
		xmxxView(ywlx, nd, bmjkLx);
	}
	function queryJhgzlb() {
		var nd = $("#ND").val();
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,jhgzlb);
		defaultJson.doQueryJsonList(controllername+"?queryJhgzlb&nd="+nd, data, jhgzlb, null, true);
	}
	//完工超链接
	function doWG(obj){
		return doLink(obj,'WG','WGSJ');
	}
	//开工超链接
	function doKG(obj){
		return doLink(obj,'KG','KGSJ');
	}
	// 可研批复 超链接
	function doKYPF(obj){
		return doLink(obj,'KYPF','KYPF');
	}
	// 划拨决定书 超链接
	function doHPJDS(obj){
		return doLink(obj,'HPJDS','HPJDS');
	}
	// 工程规划许可证 超链接
	function doGCXKZ(obj){
		return doLink(obj,'GCXKZ','GCXKZ');
	}
	// 施工许可 超链接
	function doSGXK(obj){
		return doLink(obj,'SGXK','SGXK');
	}
	// 初步设计批复 超链接
	function doCBSJPF(obj){
		return doLink(obj,'CBSJPF','CBSJPF');
	}
	// 拆迁图 超链接
	function doCQT(obj){
		return doLink(obj,'CQT','CQT');
	}	
	// 排迁图 超链接
	function doPQT(obj){
		return doLink(obj,'PQT','PQT');
	}
	// 施工图 超链接
	function doSGT(obj){
		return doLink(obj,'SGT','SGT');
	}
	// 提报价 超链接
	function doTBJ(obj){
		return doLink(obj,'TBJ','TBJ');
	}	
	// 财审值 超链接
	function doCS(obj){
		return doLink(obj,'CS','CS');
	}
	// 监理招标 超链接
	function doJLDW(obj){
		return doLink(obj,'JLDW','JLDW');
	}
	// 施工招标 超链接
	function doSGDW(obj){
		return doLink(obj,'SGDW','SGDW');
	}	
	// 征拆 超链接
	function doZC(obj){
		return doLink(obj,'ZC','ZC');
	}
	// 排迁 超链接
	function doPQ(obj){
		return doLink(obj,'PQ','PQ');
	}
	// 交工 超链接
	function doJG(obj){
		return doLink(obj,'JG','JG');
	}
	//链接方法
	function doLink(obj,col,lie){
		var flag=obj[col];
		  if(flag==0){
			  return '<div style="text-align:center">'+flag+'</div>';
		  }else{
				if(obj.LB=='已编制'){
					return '<a href="javascript:void(0);" onclick="openDetailJHBZ(\'项目详细列表\',\'JH_DETAIL**JH**JHYBZ\',\''+lie+'\')">'+flag+'</a>';			  
				}else if(obj.LB=='未编制'){
					return '<a href="javascript:void(0);" onclick="openDetailJHBZ(\'项目详细列表\',\'JH_DETAIL**JH**JHWBZ\',\''+lie+'\')">'+flag+'</a>';			  
				}else{
					return '<a href="javascript:void(0);" onclick="openDetailJHBZ(\'项目详细列表\',\'JH_DETAIL**JH**JHWXBZ\',\''+lie+'\')">'+flag+'</a>';			  
				}
		    }
	  }
	function openDetailJHBZ(title,name,tiaojian){
		var nd = $("#ND").val();
		var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/tcjhbzList.jsp?nd="+nd+"&proKey="+name+"&tiaojian="+tiaojian,"modal":"1"};
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
					下达库&nbsp;&nbsp;				
					<select class="span2 year" style="width: 8%" id="ND" name="ND"  fieldname="ND" operation="=" noNullSelect ="true"></select> 
				</h4>
				<div class="container-fluid" style="padding:0px 0px;">
					<div class="row-fluid">
					    <div class="span12 GC_JDGL_TCGK">
				            <div class="bmjkTjgkBlock">
				              <p>
				              	截至目前，建委向中心下达计划<span flag="hasLinkXDK"  bzfieldname="ZBGL_TJGK_JGZX_ZX"></span>项重点工程。
				              	其中，年初下达<span flag="hasLinkXDK"  bzfieldname="ZBGL_TJGK_JGZX_NCXD"></span>项，
				              	追加下达<span flag="hasLinkXDK"  bzfieldname="ZBGL_TJGK_JGZX_ZJXD"></span>项，
				              	新建项目<span flag="hasLinkXDK"  bzfieldname="ZBGL_TJGK_JGZX_XINJ"></span>项，
				              	续建项目<span flag="hasLinkXDK"  bzfieldname="ZBGL_TJGK_JGZX_XUJ"></span>项。
				              </p>
				            </div>
				          </div>
						<div class="span12"  style="margin-left: 0px;">
							<div class="span3">
								<div class="bmjkTjgkBlock">
									<p>计划项目<span   id="JHXMS"></span>项</p>
								</div>
								<ul class="bmgk-ul-list" style="margin-left:5%;">
									<li><label><input type="radio" name="CBKXMFL" value="XMFR">按项目法人</label></li>
									<li><label><input type="radio" name="CBKXMFL" value="ZRBM">按责任部门</label></li>
									<li><label><input type="radio" name="CBKXMFL" value="XMJD">按项目进度</label></li>
									<li><label><input type="radio" name="CBKXMFL" value="XMLX">按项目类型</label></li>
									<li><label><input type="radio" name="CBKXMFL" value="XMXZ">按项目性质</label></li>
								</ul>
							</div>
							<div class="span9">
								<div id="club">
								    <div class="modle" id="cbkxmxx">
										<div class="modle_con">
											<ul>
												<li class="fn-clear" name="TAB_XMFR">
													<div class="modle_text fn-right">
														<div class="B-small-from-table-autoConcise">
															<div class="overFlowX" style="height:200px; overflow:auto">
																<table class="table-hover table-activeTd B-table" id="TAB_XMFR"
																	width="100%" type="single" pageNum="100" nopage="true">
																	<thead>
																		<tr>
																			<th name="XH" id="_XH" colindex=1>
																				&nbsp;#&nbsp;
																			</th>
																			<th fieldname="XMFR" maxlength="15" tdalign="center">
																				&nbsp;法人单位&nbsp;
																			</th>
																			<th fieldname="XS" tdalign="center"
																				style="width: 20%" maxlength="15">
																				&nbsp;项数&nbsp;
																			</th>
																			<th fieldname="JHZTZE" tdalign="right"
																				style="width: 20%" maxlength="15">
																				&nbsp;总投资额（万元）&nbsp;
																			</th>
																			<th fieldname="NCJH" tdalign="center"
																				style="width: 20%" maxlength="15">
																				&nbsp;年初计划&nbsp;
																			</th>
																			<th fieldname="ZJJH" tdalign="center">
																				&nbsp;追加计划&nbsp;
																			</th>
																		</tr>
																	</thead>
																	<tbody>
																	</tbody>
																</table>
															</div>
														</div>
													</div>
												</li>
												<li class="fn-clear" name="TAB_ZRBM">
													<div class="modle_text fn-right">
														<div class="B-small-from-table-autoConcise">
															<div class="overFlowX" style="height:200px; overflow:auto">
																<table class="table-hover table-activeTd B-table" id="TAB_ZRBM"
																	width="100%" type="single" pageNum="100" nopage="true">
																	<thead>
																		<tr>
																			<th name="XH" id="_XH" colindex=1>
																				&nbsp;#&nbsp;
																			</th>
																			<th fieldname="ZRBM" maxlength="15" tdalign="center">
																				&nbsp;责任部门&nbsp;
																			</th>
																			<th fieldname="XS" tdalign="center"
																				style="width: 20%" maxlength="15">
																				&nbsp;项数&nbsp;
																			</th>
																			<th fieldname="JHZTZE"  tdalign="right"
																				style="width: 20%;" maxlength="15">
																				&nbsp;总投资额（万元）&nbsp;
																			</th>
																			<th fieldname="NCJH" tdalign="center">
																				&nbsp;年初计划&nbsp;
																			</th>
																			<th fieldname="ZJJH" tdalign="center">
																				&nbsp;追加计划&nbsp;
																			</th>
																		</tr>
																	</thead>
																	<tbody>
																	</tbody>
																</table>
															</div>
														</div>
													</div>
												</li>
												<li class="fn-clear" name="TAB_XMJD">
													<div class="modle_text fn-right">
														<div class="B-small-from-table-autoConcise">
															<div class="overFlowX" style="height:200px; overflow:auto">
																<table class="table-hover table-activeTd B-table" id="TAB_XMJD"
																	width="100%" type="single" pageNum="10" nopage="true">
																	<thead>
																		<tr>
																			<th name="XH" id="_XH" colindex=1>
																				&nbsp;#&nbsp;
																			</th>
																			<th fieldname="NDMB" maxlength="15" tdalign="center">
																				&nbsp;年度目标&nbsp;
																			</th>
																			<th fieldname="XS" tdalign="right"
																				style="width: 20%" maxlength="15">
																				&nbsp;项数&nbsp;
																			</th>
																			<th fieldname="JHZTZE" tdalign="center"
																				style="width: 20%" maxlength="15">
																				&nbsp;总投资额（万元）&nbsp;
																			</th>
																			<th fieldname="NCJH" tdalign="center">
																				&nbsp;年初计划&nbsp;
																			</th>
																			<th fieldname="ZJJH" tdalign="center">
																				&nbsp;追加计划&nbsp;
																			</th>
																		</tr>
																	</thead>
																	<tbody>
																	</tbody>
																</table>
															</div>
														</div>
													</div>
												</li>
												<li class="fn-clear" name="TAB_XMLX">
													<div class="modle_text fn-right">
														<div class="B-small-from-table-autoConcise">
															<div class="overFlowX">
																<table class="table-hover table-activeTd B-table" id="TAB_XMLX"
																	width="100%" type="single" pageNum="10" nopage="true">
																	<thead>
																		<tr>
																			<th name="XH" id="_XH" colindex=1>
																				&nbsp;#&nbsp;
																			</th>
																			<th fieldname="XMLX" maxlength="15" tdalign="center">
																				&nbsp;项目类型&nbsp;
																			</th>
																			<th fieldname="XS" tdalign="center"
																				style="width: 20%" maxlength="15">
																				&nbsp;项数&nbsp;
																			</th>
																			<th fieldname="JHZTZE" tdalign="right"
																				style="width: 20%" maxlength="15">
																				&nbsp;总投资额（万元）&nbsp;
																			</th>
																			<th fieldname="NCJH" tdalign="center">
																				&nbsp;年初计划&nbsp;
																			</th>
																			<th fieldname="ZJJH" tdalign="center">
																				&nbsp;追加计划&nbsp;
																			</th>
																		</tr>
																	</thead>
																	<tbody>
																	</tbody>
																</table>
															</div>
														</div>
													</div>
												</li>
												<li class="fn-clear" name="TAB_XMXZ">
													<div class="modle_text fn-right">
														<div class="B-small-from-table-autoConcise">
															<div class="overFlowX">
																<table class="table-hover table-activeTd B-table" id="TAB_XMXZ"
																	width="100%" type="single" pageNum="10" nopage="true">
																	<thead>
																		<tr>
																			<th name="XH" id="_XH" colindex=1>
																				&nbsp;#&nbsp;
																			</th>
																			<th fieldname="XMXZ" maxlength="15" tdalign="center">
																				&nbsp;项目性质&nbsp;
																			</th>
																			<th fieldname="XS" tdalign="center"
																				style="width: 20%" maxlength="15">
																				&nbsp;项数&nbsp;
																			</th>
																			<th fieldname="JHZTZE" tdalign="right"
																				style="width: 20%" maxlength="15">
																				&nbsp;总投资额（万元）&nbsp;
																			</th>
																			<th fieldname="NCJH" tdalign="center">
																				&nbsp;年初计划&nbsp;
																			</th>
																			<th fieldname="ZJJH" tdalign="center">
																				&nbsp;追加计划&nbsp;
																			</th>
																		</tr>
																	</thead>
																	<tbody>
																	</tbody>
																</table>
															</div>
														</div>
													</div>
												</li>
											</ul>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="CBK" style="border: 0px;">
				<h4 class="bmjkTitleLine">
					项目分类&计划编制
				</h4>
				<div class="container-fluid" style="padding:0px 0px;">
					<div class="row-fluid">
						<div class="span12 GC_JHBZ_TJGK">
							<div class="bmjkTjgkBlock">
								<p>
									截至目前，所有项目中已完成统筹计划编制<span flag="hasLinkXDK"  bzfieldname="JHBZ_TJGK_JGZX_WCBZ"></span>项，
									正在编制<span flag="hasLinkXDK"  bzfieldname="JHBZ_TJGK_JGZX_ZZBZ"></span>项，
									无法编制<span flag="hasLinkXDK"  bzfieldname="JHBZ_TJGK_JGZX_WFBZ"></span>项。
									稳定<span flag="hasLinkXDK"  bzfieldname="JHBZ_TJGK_JGZX_WD"></span>项，
									不稳定<span flag="hasLinkXDK"  bzfieldname="JHBZ_TJGK_JGZX_BWD"></span>项。
								</p>
							</div>
						</div>
						<div class="span6">			
							<div id="XMFLChartDiv"></div>
							<div class="chartTitle" style="text-align: center;">年度项目分类情况</div>						
						</div>
						<div class="span5">
							<div id="JHBZChartDiv"></div>
							<div class="chartTitle" style="text-align: center;">计划编制</div>					
						</div>
					</div>
					<div class="row-fluid">
					<div class="B-small-from-table-autoConcise">
        			<div class="overFlowX"> 
        			<div style="height: 10px;"></div>		
						<table width="100%" class="table-hover table-activeTd B-table" id="jhgzlb" type="single" nopage="true">
							<thead>
								<tr>
									<th fieldname="LB" tdalign="center" >&nbsp;是否编制&nbsp;</th>
									<th fieldname="KYPF" tdalign="center" Customfunction="doKYPF" >&nbsp;可研批复&nbsp;</th>
									<th fieldname="HPJDS" tdalign="center" Customfunction="doHPJDS" >&nbsp;划拨决定书&nbsp;</th>
									<th fieldname="GCXKZ" tdalign="center" Customfunction="doGCXKZ" >&nbsp;工程规划许可证&nbsp;</th>
									<th fieldname="CBSJPF"  tdalign="center" Customfunction="doCBSJPF" >&nbsp;初步设计批复&nbsp;</th>
									<th fieldname="SGT"  tdalign="center" Customfunction="doSGT" >&nbsp;施工图&nbsp;</th>
									<th fieldname="CQT"  tdalign="center" Customfunction="doCQT" >&nbsp;拆迁图&nbsp;</th>
									<th fieldname="PQT"  tdalign="center" Customfunction="doPQT" >&nbsp;排迁图&nbsp;</th>
									<th fieldname="ZC"  tdalign="center" Customfunction="doZC" >&nbsp;征拆&nbsp;</th>
									<th fieldname="PQ"  tdalign="center" Customfunction="doPQ" >&nbsp;排迁&nbsp;</th>
									<th fieldname="TBJ"  tdalign="center" Customfunction="doTBJ" >&nbsp;提报价&nbsp;</th>
									<th fieldname="CS"  tdalign="center" Customfunction="doCS" >&nbsp;财审值&nbsp;</th>
									<th fieldname="SGDW"  tdalign="center" Customfunction="doSGDW" >&nbsp;施工招标&nbsp;</th>
									<th fieldname="JLDW"  tdalign="center" Customfunction="doJLDW" >&nbsp;监理招标&nbsp;</th>
									<th fieldname="SGXK"  tdalign="center" Customfunction="doSGXK" >&nbsp;施工许可&nbsp;</th>
									<th fieldname="KG" tdalign="center" Customfunction="doKG" >&nbsp;开工&nbsp;</th>
									<th fieldname="WG" tdalign="center" Customfunction="doWG" >&nbsp;完工&nbsp;</th>
									<th fieldname="JG"  tdalign="center" Customfunction="doJG" >&nbsp;交工&nbsp;</th>
								</tr>
							</thead>
						    <tbody></tbody>
						</table>
					</div>
					</div>
				</div>
				</div>
			</div>
			<div class="B-small-from-table-autoConcise CBK" style="border: 0px;">
				<h4 class="bmjkTitleLine">
					计划跟踪
				</h4>
			</div>			
			<div class="container-fluid" style="padding:0px 0px;">
				<div class="row-fluid">
					<div class="span12">			
						<div id="JHGZ_Column2D"></div>					
					</div>
				</div>
				
			</div>
		</div>
		<div align="center">
			<form method="post" id="queryForm"></form>
			<FORM name="frmPost" method="post" style="display: none"target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="ywid" id="ywid">
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="Z.XMID,Z.PXH" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id = "queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>