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
	href="${pageContext.request.contextPath }/css/bmgk/jcBmgkStyle.css?version=15">
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/bmgk/bmgk.css?version=20140415">
<style type="text/css">
body {font-size:14px;}
h2 {display:inline; line-height:2em;}
a,a:focus, a:hover, a:active{
	font-weight:900;
	color:#FFFF00;
}
.table2 {
	border-left: #000 solid 1px;
	border-top: #000 solid 1px;
	margin:10px auto;
}
.marginBottom15px {margin-bottom:15px;}
.table2 tr td,.table2 tr th {
	line-height: 1.5em;
	padding: 4px;
	border-right: #000 solid 1px;
	border-bottom: #000 solid 1px;
}
input[type='text'] {
	vertical-align: middle;
	height: 20px;
	line-height: 16px;
	padding: 2px;
}
.textTable {
}
.textTable tr td,.textTable tr th {
	line-height: 1.5em;
	padding: 4px;
	border-right: #000 solid 0;
	border-bottom: #000 solid 0;
	font-weight:normal;
}
.textTable th{
	text-align:left;
}
.textTable td{
	text-align:right;
}
.table3 {
}
.table3 tr td,.table3 tr th {
	line-height: 1.5em;
	padding: 4px;
	border-right: #000 solid 0;
	border-bottom: #000 solid 0;
}
.table3 tr th {/* border-top: #000 solid 1px; border-bottom: #000 solid 1px; */}
.B-small-from-table-auto h4{padding:0px;}
.bmgk-ul-list{
	list-style-type:none;
	margin-left:0px;
}
.bmgk-ul-list li{
	margin:0px 4px 0px 4px;
}
.bmgk-ul-list input[type="radio"]{
	margin:0px 4px 0px 4px;
}
.bmgk_tab_list{
	width:100%;
	border:0px;
}
.bmgk_tab_list tr{
	border-bottom:solid 5px #FFF;
}
.bmgk_tab_list .tab_title{
	text-align:center;
	font-size:12px;
	width:25%;
	height:40px;
	background:#4F81BD;
	color:#FFF;
	border-right:solid 1px #ddd;
}
.bmgk_tab_list .tab_content{
	font-size:12px;
	width:75%;
	height:40px;
	color:#FFF;
	background:#36648B;
}
.bmgk_tab_list_pcjh{
	width:94%;
}
.bmgk_tab_list_pcjh .tab_title_ncjh{
	text-align:center;
	background:#34485e;
	color:#FFF;
	font-size:18px;
	font-weight:600;
	border-right:solid 4px #FFF;
}
.bmgk_tab_list_pcjh .tab_title_pcmc{
	text-align:center;
	background:#4F81BD;
	color:#FFF;
	font-size:16px;
	font-weight:600;
	border-right:solid 3px #FFF;
	border-bottom:solid 2px #FFF;
}
.bmgk_tab_list_pcjh .tab_content_zxm{
	text-align:center;
	background:#7A99AD;
	color:#FFF;
	height:30px;
	border-right:solid 1px #FFF;
	border-bottom:solid 1px #FFF;
}
.bmgk_tab_list_pcjh .tab_content_xdrq{
	text-align:center;
	background:#A6A6A6;
	color:#FFF;
}
.bmgk_tab_list_pcjh .tab_content_qqxm{
	text-align:center;
	background:#36648B;
	border-right:solid 1px #FFF;
	border-bottom:solid 2px #FFF;
	color:#FFF;
}
.bmgk_tab_list_pcjh .tab_content_kgxm{
	text-align:center;
	background:#36648B;
	border-right:solid 1px #FFF;
	border-bottom:solid 2px #FFF;
	color:#FFF;
}
.bmgk_tab_list_pcjh .tab_content_wgxm{
	text-align:center;
	background:#36648B;
	border-bottom:solid 2px #FFF;
	color:#FFF;
}
.cellTable{
	border:solid 1px #FFF;
}
.cellTable .bmgk-content-td{
	border:solid 2px #FFF;
	height:30px;
	width:110px;
	background:#36648B;
	text-align:center;
	color:#FFF;
}
.cellTable .bmgk-title-td{
	border:solid 2px #FFF;
	height:30px;
	background:#4F81BD;
	text-align:center;
	font-weight:700;
	color:#FFF;
	font-size:16px;
}
.cellTable .bmgk-caption-td{
	background:#34485e;
	color:#FFF;
	font-size:18px;
	font-weight:600;
	line-height:25px;
}
</style>
<script type="text/javascript">
	var controllername = "${pageContext.request.contextPath }/bmgk/XmcbkBmgkController.do";
	var p_chartNum = 1;
	$(function(){
		generateNd($("#ND"));
		setDefaultNd("ND");
		$(".ndText").text($("#ND").val());
		doInit();
		$("input[name='CBKXMFL']").click(function(){
			var valStr = $(this).val();
			var liHeight=$("#cbkxmxx ul li").height();
			$("#cbkxmxx ul").prepend($("#cbkxmxx ul li[name='TAB_CBK_"+valStr+"']").css("height","0px").animate({
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
		queryJHXD(ndVal);
		queryJHZX(ndVal);
		queryJHTZ(ndVal);
	}
	//储备库
	function queryCBK(ndVal){
		$("input[name='CBKXMFL']:first").click();
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,TAB_CBK_AZTZ);
		defaultJson.doQueryJsonList(controllername+"?queryTableAssdq&nd="+ndVal,data,TAB_CBK_ASSDSQ,null,true);//按所属地区（建委项目）
		defaultJson.doQueryJsonList(controllername+"?queryTableAssdqAll&nd="+ndVal,data,TAB_CBK_ASSDSQ_ALL,null,true);//按所属地区（所有项目）
		defaultJson.doQueryJsonList(controllername+"?queryTableAzrbm&nd="+ndVal,data,TAB_CBK_AZRBM,null,true);//按部门
		defaultJson.doQueryJsonList(controllername+"?queryTableAxmlx&nd="+ndVal,data,TAB_CBK_AXMLX,null,true);//按项目类型
		defaultJson.doQueryJsonList(controllername+"?queryTableAztz&nd="+ndVal,data,TAB_CBK_AZTZ,null,true);//按总投资额
		queryLwyjChart(ndVal);//两委一局饼图3
	//	queryZf_NdChart(ndVal);
	//	querySch_NdChart(ndVal);
		queryCbkTjgk(ndVal);
		tableClass("TAB_CBK_ASSDSQ");
		tableClass("TAB_CBK_ASSDSQ_ALL");
		tableClass("TAB_CBK_AZRBM");
		tableClass("TAB_CBK_AXMLX");
		tableClass("TAB_CBK_AZTZ");
	}
	function tableClass(tableID){
		$("#"+tableID).removeClass();
		$($("#"+tableID+" tbody>tr:even")).addClass("tableTrEvenColor");
		$("#"+tableID).addClass("table3 tableList");
		$($("#"+tableID+" thead tr:eq(0)")).addClass("tableTheadTr");
		$($($("#"+tableID+" thead tr:eq(0)")).find("th")).addClass("tableTdAlign");
	}
	//计划下达
	function queryJHXD(ndVal){
		queryJhxdTjgk(ndVal);
		queryJhxdNcjh(ndVal);
		queryJhxdZjjh(ndVal);
		queryLwyjZf(ndVal);
		queryLwyjSc(ndVal);
	}
	//计划执行
	function queryJHZX(ndVal){
		queryJhzxTjgk(ndVal);
		queryJhzxZjjh(ndVal);
		queryZf_Sch(ndVal);//政府市场化
		queryJhzxNcjhZftz(ndVal);
		queryJhzxNcjhSctz(ndVal);
		queryKwgjs(ndVal);
	}
	//计划调整
	function queryJHTZ(ndVal){
		queryJhtzTjgk(ndVal);
	}
	//计划调整——统计概况
	function queryJhtzTjgk(nd){
		var action1 = controllername + "?queryJhtzTjgk&nd="+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			success: function(result){
				if(result.msg=="0"){
					$("#JHTZ_NR1").html("");
					$("#JHTZ_NR2").html("");
				}else{
					var msgStr = result.msg.replace(/\\n/g,"<br/>");	//保留换行
					msgStr = msgStr.replace(/\\b/g,"&nbsp;");			//保留空格
					var obj = convertJson.string2json1(msgStr);
					$("#JHTZ_NR1").html(obj.response.data[0].JHTZ_NR1);
					$("#JHTZ_NR2").html(obj.response.data[0].JHTZ_NR2);
				}
			}
		});
	}
	//------------------------------------
	//查询统计概况
	//------------------------------------
	function queryCbkTjgk(nd){
		var action1 = controllername + "?queryCbkTjgk&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result){
				insertTable(result,"CBK");
			}
		});
	}
	//------------------------------------
	//查询统计概况
	//------------------------------------
	function queryJhxdTjgk(nd){
		var action1 = controllername + "?queryJhxdTjgk&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result){
				insertTable(result,"JHXD");
			}
		});
	}
	//------------------------------------
	//查询开完工结算统计概况
	//------------------------------------
	function queryKwgjs(nd){
		var action1 = controllername + "?queryKwgjs&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result){
				insertTable(result,"KWGJS");
			}
		});
	}
	//------------------------------------
	//查询计划执行统计概况
	//------------------------------------
	function queryJhzxTjgk(nd){
		var action1 = controllername + "?queryJhzxTjgk&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result){
				insertTable(result,"JHZX");
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
								case 'hasLink' :
									$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\''+tableId+'_DETAIL**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
									break;
								case 'hasLinkKfg' :
									$(this).html('<a href="javascript:void(0);" onclick="openDetailKfg(\'项目详细列表\',\''+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
									break;
								case 'hasLinkTCJH' :
									$(this).html('<a href="javascript:void(0);" onclick="openDetailJH(\'项目详细列表\',\'TCJh_DETAIL**ZJB**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
									break;
								case 'hasLink_gcjs' :
									$(this).html('<a href="javascript:void(0);" onclick="openDetailGCJS(\'项目详细列表\',\'GCJS_DETAIL**ZJB**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
									break;
								default:
									var html=$(subresultmsgobj).attr(str);
									if(str=='JHXD_CNT_NDMB'||str=='JHXD_CNT_XMFR')
									{
										html=html.replace(eval("/&lt;/gi"),'<');
										html=html.replace(eval("/&gt;/gi"),'>');
										html=html.replace(eval("/&nbsp;/gi"),' ');
									}	
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
	//年份查询
	function generateNd(ndObj){
		ndObj.attr("src","T#GC_TCJH_XMCBK:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
		ndObj.attr("kind","dic");
		ndObj.html('');
		reloadSelectTableDic(ndObj);
		ndObj.val(new Date().getFullYear());
	}
	//政府——年度投资
	function queryZf_NdChart(nd){
		var action = controllername + "?queryZf_NdChart&nd="+nd;
		$.ajax({
			url : action,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "Zf_NdChart"+p_chartNum, "100%", "250");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("Zf_NdChartDiv"); 
			}
		});
	}
	//市场化——年度投资
	function querySch_NdChart(nd){
		var action = controllername + "?querySch_NdChart&nd="+nd;
		$.ajax({
			url : action,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "Sch_NdChart"+p_chartNum, "100%", "250");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("Sch_NdChartDiv"); 
			}
		});
	}

	//两委一局饼图3
	function queryLwyjChart(nd){
		var action = controllername + "?queryLwyjChart&nd="+nd;
		$.ajax({
			url : action,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/Pie2D.swf", "LwyjChart"+p_chartNum, "100%", "400");
			     myChart.setJSONData(result.msg);  
			     myChart.render("LwyjChartDiv"); 
			}
		});
	}
	//政府市场化
	
	function queryZf_Sch(nd){
		var action = controllername +"?queryZf_Sch&nd="+nd;
		$.ajax({
			url : action,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSStackedColumn2D.swf", "myChartID1"+p_chartNum, "100%", "200");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("zf_sch"); 
			}
		});
	}
	//计划执行--年初计划--政府投资
	function queryJhzxNcjhZftz(nd){
		var action = controllername +"?queryJhzxNcjhZftzChart&nd="+nd;
		$.ajax({
			url : action,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSStackedColumn2D.swf", "jhzxNcjhZftzChart"+p_chartNum, "100%", "200");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("jhzxNcjhZftzDiv"); 
			}
		});
	}
	//计划执行--年初计划--市场投资
	function queryJhzxNcjhSctz(nd){
		var action = controllername +"?queryJhzxNcjhSctzChart&nd="+nd;
		$.ajax({
			url : action,
			dataType:"json",
			async:false,
			success: function(result){
			 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSStackedColumn2D.swf", "jhzxNcjhSctzChart"+p_chartNum, "100%", "200");  
			     myChart.setJSONData(result.msg);  
			     myChart.render("jhzxNcjhSctzDiv"); 
			}
		});
	}
	//------------------------------------
	//查询计划下达-年初计划列表
	//------------------------------------
	function queryJhxdNcjh(nd){
		var action1 = controllername + "?queryJhxdNcjh&nd="+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
				if(result.msg=="0"){
					$("#jhxd_ncjh_table").empty();
				}else{
					var obj = convertJson.string2json1(result.msg);
					var len = obj.response.data.length;
					var showHtml = "";
					showHtml += '<tr>';
					showHtml += '<td rowspan='+(len*2)+' class="tab_title_ncjh">';
					showHtml += '<div>年<br/>初<br/>计<br/>划</div>';
					showHtml += '</td>';
					for(var i=0;i<len;i++){
						var rowData = obj.response.data[i];
						if(i==0){
						
						}else{
							showHtml += '<tr>';
						}
						showHtml += '<td rowspan=2 class="tab_title_pcmc">第'+rowData.PCH+'批</td>';
						showHtml += '<td colspan=2 class="tab_content_zxm">'+rowData.XMS+'项，年度投资'+rowData.JHZTZE+'亿元</td>';
						showHtml += '<td class="tab_content_xdrq">'+rowData.XDRQ+'</td>';
						showHtml += '</tr>';
						showHtml += '<tr>';
						showHtml += '<td class="tab_content_qqxm">前期项目'+rowData.QQ+'项</td>';
						showHtml += '<td class="tab_content_kgxm">开工项目'+rowData.KG+'项</td>';
						showHtml += '<td class="tab_content_wgxm">完工项目'+rowData.WG+'项</td>';
						showHtml += '</tr>';
					}
					$("#jhxd_ncjh_table").empty();
					$("#jhxd_ncjh_table").html(showHtml);
				}
			}
		});
	}
	//------------------------------------
	//查询计划下达-年初计划列表
	//------------------------------------
	function queryJhxdZjjh(nd){
		var action1 = controllername + "?queryJhxdZjjh&nd="+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
				if(result.msg=="0"){
					$("#jhxd_zjjh_table").empty();
				}else{
					var obj = convertJson.string2json1(result.msg);
					var len = obj.response.data.length;
					var showHtml = "";
					showHtml += '<tr>';
					showHtml += '<td rowspan='+(len*2)+' class="tab_title_ncjh">';
					showHtml += '<div>追<br/>加<br/>计<br/>划</div>';
					showHtml += '</td>';
					for(var i=0;i<len;i++){
						var rowData = obj.response.data[i];
						if(i==0){
						
						}else{
							showHtml += '<tr>';
						}
						showHtml += '<td rowspan=2 class="tab_title_pcmc">第'+rowData.PCH+'批</td>';
						showHtml += '<td colspan=2 class="tab_content_zxm">'+rowData.XMS+'项，年度投资'+rowData.JHZTZE+'亿元</td>';
						showHtml += '<td class="tab_content_xdrq">'+rowData.XDRQ+'</td>';
						showHtml += '</tr>';
						showHtml += '<tr>';
						showHtml += '<td class="tab_content_qqxm">前期项目'+rowData.QQ+'项</td>';
						showHtml += '<td class="tab_content_kgxm">开工项目'+rowData.KG+'项</td>';
						showHtml += '<td class="tab_content_wgxm">完工项目'+rowData.WG+'项</td>';
						showHtml += '</tr>';
					}
					$("#jhxd_zjjh_table").empty();
					$("#jhxd_zjjh_table").html(showHtml);
				}
			}
		});
	}
	//------------------------------------
	//查询计划下达——追加计划多级饼图
	//------------------------------------
	function queryJhzxZjjh(nd){
		var action1 = controllername + "?queryJhzxZjjhChart&nd="+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			async:false,
			success: function(result){
				var count_cells = 3;
				var obj = convertJson.string2json1(result.msg);
				if(obj.category.length=="0"){
					//表示无数据
					$("#JHZX_ZJJH_TABLE").empty();
					return;
				}
				var count_zftz = obj.category[0].category[0].category.length;
				var count_sctz = obj.category[0].category[1]==null?0:obj.category[0].category[1].category.length;
				var count_zftz_rows = Math.ceil(count_zftz/count_cells);
				var count_sctz_rows = Math.ceil(count_sctz/count_cells);
				var count_total_rows = count_zftz_rows+count_sctz_rows+(obj.category[0].category.length);
				var showHtml = "";
				showHtml += '<tr>';
                showHtml += '<td rowspan='+count_total_rows+' class="bmgk-caption-td">追加<br/>计划</td>';
                for(var m=0;m<obj.category[0].category.length;m++){
                	if(m==0){
                	
					}else{
						showHtml += '<tr>';
					}
					showHtml += '<td colspan=3 class="bmgk-title-td">'+(obj.category[0].category[m].label).replace("投资,","投资完成")+"亿元"+'</td>';
					showHtml += '</tr>';
					var len = obj.category[0].category[m].category.length;
					for(var i=0;i<len;i++){
						if(i%count_cells==0){
							showHtml += '<tr>';
						}
						showHtml += '<td class="bmgk-content-td">'+(obj.category[0].category[m].category[i].LABEL).replace(",","<br>完成")+"亿元"+'</td>';
						if((i+1)==len){
							//到最后一个了，不足一行的，要补充空白TD，并以TR结束
							var blankNum = count_cells-((i+1)%count_cells);
							if(blankNum!=count_cells){
								for(var j=0;j<blankNum;j++){
									showHtml += '<td class="bmgk-content-td"></td>';
								}
							}
							showHtml += '</tr>';
						}else if((i+1)%count_cells==0){
							//没到最后一个，但是满足一行了，以TR结束
							showHtml += '</tr>';
						}
					}
				}
				$("#JHZX_ZJJH_TABLE").empty();
				$("#JHZX_ZJJH_TABLE").html(showHtml);
			}
		});
	}
	//两委一局政府投资表格
	function queryLwyjZf(nd){
		var action1 = controllername + "?queryTabLwyjZf&nd="+nd;
		$.ajax({
			url : action1,
			dataType:"json",
			success: function(result){
				if(result.msg=="0"){
					$("#TAB_LWYJ_ZF").empty();
				}else{
					var resultObj = convertJson.string2json1(result.msg);
					var len = resultObj.response.data.length;
					var TAB_LWYJ_ZF = $("#TAB_LWYJ_ZF");
					TAB_LWYJ_ZF.empty();
					for(var i=0;i<len;i++) {
						TAB_LWYJ_ZF.append("<tr><td class='tab_title'></td><td class='tab_content'></td></tr>");
						$("#TAB_LWYJ_ZF tr:eq("+i+") td:eq(0)").text(resultObj.response.data[i].LABEL);
						$("#TAB_LWYJ_ZF tr:eq("+i+") td:eq(1)").text(resultObj.response.data[i].VALUE);
					}
				}
			}
		});
	}
	//两委一局市场化投资表格
	function queryLwyjSc(ndVal) {
		$.ajax({
			url:		controllername+"?queryTabLwyjSc&nd="+ndVal,
			data:		"",
			dataType:	"json",
			async:		false,
			success:	function(result) {
				if(result.msg=="0"){
					$("#TAB_LWYJ_SC").empty();
				}else{
					var resultObj = convertJson.string2json1(result.msg);
					var len = resultObj.response.data.length;
					var TAB_LWYJ_SC = $("#TAB_LWYJ_SC");
					TAB_LWYJ_SC.empty();
					for(var i=0;i<len;i++) {
						TAB_LWYJ_SC.append("<tr><td class='tab_title'></td><td class='tab_content'></td></tr>");
						$("#TAB_LWYJ_SC tr:eq("+i+") td:eq(0)").text(resultObj.response.data[i].LABEL);
						$("#TAB_LWYJ_SC tr:eq("+i+") td:eq(1)").text(resultObj.response.data[i].VALUE);
					}
				}
			}
		});
	}
	

	function openDetail(title,name){
		var nd = $("#ND").val();
		var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/xmcbk/bmgk/cbkList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
	//开复工链接
	function openDetailKfg(title,name) {
		var nd = $("#ND").val();
		var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/kfgList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
	//打开计划链接页面
	function openDetailJH(title,name){
		var nd = $("#ND").val();
		var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/tcjhList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
	//造价编制链接
	function openDetailGCJS(title,name){
		var nd = $("#ND").val();
		var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/gcjsList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
</script>
</head>
	<body>
		<div class="container-fluid" style="padding:0px 10px;">
			<span class="pull-right">
    			<button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
			</span>
			<div class="CBK" style="border: 0px;">
				<h4 class="bmjkTitleLine">
					储备库&nbsp;&nbsp;				
					<select class="span2 year" style="width: 8%" id="ND" name="ND"  fieldname="ND" operation="=" noNullSelect ="true"></select> 
				</h4>
				<div class="container-fluid" style="padding:0px 0px;">
					<div class="row-fluid">
						<div class="span12">
							<div class="span3">
								<div style="width:100%;height:40px;vertical-align:middle;text-align:center;background:#34485e;margin-bottom:10px;">
									<span style="margin:auto;line-height:40px;color:#FFF;font-size:16px;">储备项目<span bzfieldname="CBK_XMZS"></span>项</span>
								</div>
								<ul class="bmgk-ul-list" style="margin-left:5%;">
									<li><label><input type="radio" name="CBKXMFL" value="ASSDQ">按所属地区（建委）</label></li>
									<li><label><input type="radio" name="CBKXMFL" value="ASSDQ_ALL">按所属地区（其他委办局）</label></li>
									<li><label><input type="radio" name="CBKXMFL" value="AZRBM">按责任部门</label></li>
									<li><label><input type="radio" name="CBKXMFL" value="AXMLX">按项目类型</label></li>
									<li><label><input type="radio" name="CBKXMFL" value="AZTZ">按总投资</label></li>
								</ul>
							</div>
							<div class="span9">
								<div id="club">
								    <div class="modle" id="cbkxmxx">
										<div class="modle_con">
											<ul>
												<li class="fn-clear" name="TAB_CBK_ASSDQ">
													<div class="modle_text fn-right">
														<div class="B-small-from-table-autoConcise">
															<div class="overFlowX" style="height:200px; overflow:auto">
																<table class="table-hover table-activeTd B-table" id="TAB_CBK_ASSDSQ"
																	width="100%" type="single" pageNum="100" nopage="true">
																	<thead>
																		<tr>
																			<th name="XH" id="_XH" colindex=1>
																				&nbsp;#&nbsp;
																			</th>
																			<th fieldname="QY" maxlength="15" tdalign="center">
																				&nbsp;城区&nbsp;
																			</th>
																			<th fieldname="XJ" tdalign="center"
																				style="width: 20%" maxlength="15">
																				&nbsp;项数&nbsp;
																			</th>
																			<th fieldname="ZTZE" tdalign="right"
																				style="width: 20%">
																				&nbsp;总投资额（万元）&nbsp;
																			</th>
																			<th fieldname="YLRJH" tdalign="center">
																				&nbsp;已列入计划&nbsp;
																			</th>
																			<th fieldname="WLRJH" tdalign="center">
																				&nbsp;未列入计划&nbsp;
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
												<li class="fn-clear" name="TAB_CBK_ASSDQ_ALL">
													<div class="modle_text fn-right">
														<div class="B-small-from-table-autoConcise">
															<div class="overFlowX" style="height:200px; overflow:auto">
																<table class="table-hover table-activeTd B-table" id="TAB_CBK_ASSDSQ_ALL"
																	width="100%" type="single" pageNum="100" nopage="true">
																	<thead>
																		<tr>
																			<th name="XH" id="_XH" colindex=1>
																				&nbsp;#&nbsp;
																			</th>
																			<th fieldname="QY" maxlength="15" tdalign="center">
																				&nbsp;城区&nbsp;
																			</th>
																			<th fieldname="XJ" tdalign="center"
																				style="width: 20%" maxlength="15">
																				&nbsp;项数&nbsp;
																			</th>
																			<th fieldname="ZTZE" tdalign="right"
																				style="width: 20%">
																				&nbsp;总投资额（万元）&nbsp;
																			</th>
																			<th fieldname="YLRJH" tdalign="center">
																				&nbsp;已列入计划&nbsp;
																			</th>
																			<th fieldname="WLRJH" tdalign="center">
																				&nbsp;未列入计划&nbsp;
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
												<li class="fn-clear" name="TAB_CBK_AZRBM">
													<div class="modle_text fn-right">
														<div class="B-small-from-table-autoConcise">
															<div class="overFlowX" style="height:200px; overflow:auto">
																<table class="table-hover table-activeTd B-table" id="TAB_CBK_AZRBM"
																	width="100%" type="single" pageNum="10" nopage="true">
																	<thead>
																		<tr>
																			<th name="XH" id="_XH" colindex=1>
																				&nbsp;#&nbsp;
																			</th>
																			<th fieldname="ZRBM" maxlength="15" tdalign="center">
																				&nbsp;责任部门&nbsp;
																			</th>
																			<th fieldname="XJ" tdalign="center"
																				style="width: 20%" maxlength="15">
																				&nbsp;项数&nbsp;
																			</th>
																			<th fieldname="ZTZE" tdalign="right"
																				style="width: 20%">
																				&nbsp;总投资额（万元）&nbsp;
																			</th>
																			<th fieldname="YLRJH" tdalign="center">
																				&nbsp;已列入计划&nbsp;
																			</th>
																			<th fieldname="WLRJH" tdalign="center">
																				&nbsp;未列入计划&nbsp;
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
												<li class="fn-clear" name="TAB_CBK_AXMLX">
													<div class="modle_text fn-right">
														<div class="B-small-from-table-autoConcise">
															<div class="overFlowX">
																<table class="table-hover table-activeTd B-table" id="TAB_CBK_AXMLX"
																	width="100%" type="single" pageNum="10" nopage="true">
																	<thead>
																		<tr>
																			<th name="XH" id="_XH" colindex=1>
																				&nbsp;#&nbsp;
																			</th>
																			<th fieldname="XMLX" maxlength="15" tdalign="center">
																				&nbsp;项目类型&nbsp;
																			</th>
																			<th fieldname="XJ" tdalign="center"
																				style="width: 20%" maxlength="15">
																				&nbsp;项数&nbsp;
																			</th>
																			<th fieldname="ZTZE" tdalign="right"
																				style="width: 20%">
																				&nbsp;总投资额（万元）&nbsp;
																			</th>
																			<th fieldname="YLRJH" tdalign="center">
																				&nbsp;已列入计划&nbsp;
																			</th>
																			<th fieldname="WLRJH" tdalign="center">
																				&nbsp;未列入计划&nbsp;
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
												<li class="fn-clear" name="TAB_CBK_AZTZ">
													<div class="modle_text fn-right">
														<div class="B-small-from-table-autoConcise">
															<div class="overFlowX">
																<table class="table-hover table-activeTd B-table" id="TAB_CBK_AZTZ"
																	width="100%" type="single" pageNum="10" nopage="true">
																	<thead>
																		<tr>
																			<th name="XH" id="_XH" colindex=1>
																				&nbsp;#&nbsp;
																			</th>
																			<th fieldname="ZTZ" maxlength="15" tdalign="center">
																				&nbsp;总投资&nbsp;
																			</th>
																			<th fieldname="XJ" tdalign="center"
																				style="width: 20%" maxlength="15">
																				&nbsp;项数&nbsp;
																			</th>
																			<th fieldname="ZTZE" tdalign="right"
																				style="width: 20%">
																				&nbsp;总投资额（万元）&nbsp;
																			</th>
																			<th fieldname="YLRJH" tdalign="center">
																				&nbsp;已列入计划&nbsp;
																			</th>
																			<th fieldname="WLRJH" tdalign="center">
																				&nbsp;未列入计划&nbsp;
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
					“两委一局”计划（年初计划）
				</h4>
				<div class="container-fluid" style="padding:0px 0px;">
					<div class="row-fluid">
						<div class="span12">
							<div class="bmjkTjgkBlock">
								<p>
								<span class="ndText">2014</span>年初步安排<span flag="hasLink" bzfieldname="LWYJ_DX"></span>项（<span flag="hasLink" bzfieldname="LWYJ_ZX"></span>个子项）重点工程，
								年度计划完成投资<span bzfieldname="LWYJ_JHZTZE"></span>亿元。
								<br/>
								其中，新建<span flag="hasLink" bzfieldname="LWYJ_XJDX"></span>项（<span flag="hasLink" bzfieldname="LWYJ_XJZX"></span>个子项），年度投资<span bzfieldname="LWYJ_XJJHZTZE"></span>亿元，
								续建<span flag="hasLink" bzfieldname="LWYJ_XUJDX"></span>项（<span flag="hasLink" bzfieldname="LWYJ_XUJZX"></span>个子项），年度投资<span bzfieldname="LWYJ_XUJJHZTZE"></span>亿元。
								</p>
							</div>
						</div>
						<div class="span2" style="margin-left:0px;">
							<div style="width:100%;height:60px;vertical-align:middle;text-align:center;background:#34485e;margin-bottom:10px;">
								<p style="margin:auto;line-height:20px;color:#FFF;font-size:16px;padding:6px 0px;">
								年度实施<span style="font-size:22px;color:#FFFF0D;" flag="hasLink" bzfieldname="LWYJ_NDSSDX"></span>项<br/>
								年度估算<span style="font-size:22px;color:#FFFF0D;" bzfieldname="LWYJ_NDGS"></span>亿
								</p>
							</div>
						</div>
						<div class="span2">
							<div style="width:100%;height:60px;vertical-align:middle;text-align:center;background:#34485e;margin-bottom:10px;">
								<p style="margin:auto;line-height:20px;color:#FFF;font-size:16px;padding:6px 0px;">
								政府投资<span style="font-size:22px;color:#FFFF0D;" flag="hasLink" bzfieldname="LWYJ_ZFTZ">XX</span>项<br/>
								年度投资<span style="font-size:22px;color:#FFFF0D;" bzfieldname="LWYJ_ZFTZJHZTZE">XX</span>亿元
								</p>
							</div>
							<div id="Zf_NdChartDiv">
							  <table class="bmgk_tab_list" id="TAB_LWYJ_ZF"></table>
							</div>				
						</div>
						<div class="span2">
							<div style="width:100%;height:60px;vertical-align:middle;text-align:center;background:#34485e;margin-bottom:10px;">
								<p style="margin:auto;line-height:20px;color:#FFF;font-size:16px;padding:6px 0px;">
								市场化投资<span style="font-size:22px;color:#FFFF0D;" flag="hasLink" bzfieldname="LWYJ_SCHTZ">XX</span>项<br/>
								年度投资<span style="font-size:22px;color:#FFFF0D;" bzfieldname="LWYJ_SCHJHZTZE">XX</span>亿元
								</p>
							</div>
							<div id="Sch_NdChartDiv">
							  <table class="bmgk_tab_list" id="TAB_LWYJ_SC"></table>
							</div>					
						</div>
						<div class="span6">
							<div id="LwyjChartDiv"></div>
							<div class="chartTitle" style="text-align: center;">年度项目分类情况</div>						
						</div>
					</div>
				</div>
			</div>
			<!-- 第二章结束 -->
			<div class="JHXD" style="border: 0px;">
				<h4 class="bmjkTitleLine">
					计划下达
				</h4>
				<div class="container-fluid" style="padding:0px 0px;">
					<div class="row-fluid">
						<div class="span12">
							<div class="bmjkTjgkBlock">
								<p>
								<span class="ndText">2014</span>年，下达城建重点实施计划<span bzfieldname="JHXD_CNT_PC"></span>批，共<span flag="hasLink" bzfieldname="JHXD_CNT_ZX"></span>个重点工程，年度计划投资<span bzfieldname="JHXD_CNT_ZX_NDTZ"></span>亿元。
								其中，年初计划<span flag="hasLink"  bzfieldname="JHXD_CNT_NCJH"></span>项，年度投资<span bzfieldname="JHXD_CNT_NCJH_NDTZ"></span>亿元，追加计划<span flag="hasLink"  bzfieldname="JHXD_CNT_ZJJH"></span>项，年度投资<span bzfieldname="JHXD_CNT_ZJJH_NDTZ"></span>亿元。<br/>
								—按年度目标划分：<span style="color:#FFF;font-size:16px;font-weight:100;" bzfieldname="JHXD_CNT_NDMB"></span>。<br/>
								—按实施性质划分：正常实施项目<span flag="hasLink"  bzfieldname="JHXD_CNT_ZCXM"></span>项，应急实施项目<span flag="hasLink"  bzfieldname="JHXD_CNT_YJXM"></span>项。<br/>
								—按项目法人划分：<span style="color:#FFF;font-size:16px;font-weight:100;" bzfieldname="JHXD_CNT_XMFR"></span>。
								</p>
							</div>
						</div>
						<div class="span6" style="margin-left:0px;margin-right:0%;">
							<table class="bmgk_tab_list_pcjh" id="jhxd_ncjh_table">
							</table>
						</div>
						<div class="span6" style="margin-left:2%;margin-right:0%;text-align:right;">
							<table class="bmgk_tab_list_pcjh" id="jhxd_zjjh_table">
							</table>
						</div>
					</div>
				</div>
			</div>
			<div class="JHZX" style="border: 0px;">
				<h4 class="bmjkTitleLine">
					计划执行
				</h4>
				<div class="container-fluid" style="padding:0px 0px;">
					<div class="row-fluid">
						<div class="span12">
							<div class="bmjkTjgkBlock">
								<p>
								截至目前，<span class="ndText">2014</span>年全市累计完成投资<span bzfieldname="JHZX_CNT_LJ_WCTZ"></span>亿元，按目标完成<span flag="hasLink" bzfieldname="JHZX_CNT_LJ_AMBZS"></span>项（<span flag="hasLink" bzfieldname="JHZX_CNT_LJ_AMBZX"></span>子项）。<br/>
								—政府投资完成<span bzfieldname="JHZX_CNT_ZFLJ_WCTZ"></span>亿元，按目标计划完成<span flag="hasLink" bzfieldname="JHZX_CNT_ZFLJ_AMBZS"></span>项（<span flag="hasLink" bzfieldname="JHZX_CNT_ZFLJ_AMBZX"></span>子项），含计划外完成投资<span bzfieldname="JHZX_CNT_ZFZJLJ_WCTZ"></span>亿元，<span flag="hasLink" bzfieldname="JHZX_CNT_ZFZJLJ_AMBZS"></span>项（<span flag="hasLink" bzfieldname="JHZX_CNT_ZFZJLJ_AMBZX"></span>个子项）。<br/>
								—市场化投资完成<span bzfieldname="JHZX_CNT_SCLJ_WCTZ"></span>亿元，按目标计划完成<span flag="hasLink" bzfieldname="JHZX_CNT_SCLJ_AMBZS"></span>项（<span flag="hasLink" bzfieldname="JHZX_CNT_SCLJ_AMBZX"></span>个子项），含计划外完成投资<span bzfieldname="JHZX_CNT_SCZJLJ_WCTZ"></span>亿元，<span flag="hasLink" bzfieldname="JHZX_CNT_SCZJLJ_AMBZS"></span>项（<span flag="hasLink" bzfieldname="JHZX_CNT_SCZJLJ_AMBZX"></span>个子项）。
								</p>
							</div>
						</div>
						<div class="span9" style="margin-left:0px;margin-right:0px;">
							<div class="span1"style="width:25px;height:400px;display:inline-block;background:#34485e;color:#FFF;padding-top:140px;padding-left:3px;font-size:18px;font-weight:600;line-height:25px;">年初计划</div>
							<div class="span4" id="zf_sch" style="margin-left:0px;"></div>
							<div class="span7" id="jhzxNcjhSctzDiv"></div>
							<div class="span11" id="jhzxNcjhZftzDiv" style="margin-left:0px;"></div>
						</div>
						<div class="span3" style="margin-left:2%;margin-right:0%;text-align:right;">
							<div class="span12">
								<table class="cellTable" id="JHZX_ZJJH_TABLE">
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="B-small-from-table-autoConcise JHTZ" style="border: 0px;">
				<h4 class="bmjkTitleLine">
					计划调整
				</h4>
				<div class="container-fluid" style="padding:0px 0px;">
					<div class="row-fluid">
						<div class="span12">
							<div class="bmjkTjgkBlock">
								<p id="JHTZ_NR1">
								截至目前，<span class="ndText">2014</span>年全市累计完成投资<span bzfieldname="JHZX_CNT_LJ_WCTZ"></span>亿元，按目标完成<span bzfieldname="JHZX_CNT_LJ_AMBZS"></span>项（<span bzfieldname="JHZX_CNT_LJ_AMBZX"></span>子项）<br/>
								—政府投资完成<span bzfieldname="JHZX_CNT_ZFLJ_WCTZ"></span>亿元，按目标计划完成<span bzfieldname="JHZX_CNT_ZFLJ_AMBZS"></span>项（<span bzfieldname="JHZX_CNT_ZFLJ_AMBZX"></span>子项），含计划外完成投资<span bzfieldname="JHZX_CNT_ZFZJLJ_WCTZ"></span>亿元，<span bzfieldname="JHZX_CNT_ZFZJLJ_AMBZS"></span>项（<span bzfieldname="JHZX_CNT_ZFZJLJ_AMBZX"></span>个子项）<br/>
								—市场化投资完成<span bzfieldname="JHZX_CNT_SCLJ_WCTZ"></span>亿元，按目标计划完成<span bzfieldname="JHZX_CNT_SCLJ_AMBZS"></span>项（<span bzfieldname="JHZX_CNT_SCLJ_AMBZX"></span>个子项），含计划外完成投资<span bzfieldname="JHZX_CNT_SCZJLJ_WCTZ"></span>亿元，<span bzfieldname="JHZX_CNT_SCZJLJ_AMBZS"></span>项（<span bzfieldname="JHZX_CNT_SCZJLJ_AMBZX"></span>个子项）。
								</p>
							</div>
						</div>
						<div class="span12" style="margin-left:10px;margin-right:0%;" >
							<p style="padding:8px 0px 8px 0px;color:#000;font-size:16px;line-height:24px;" id="JHTZ_NR2">
								
							</p>
						</div>
					</div>
				</div>
			</div>
			<div class="B-small-from-table-autoConcise KWGJS" style="border: 0px;">
				<h4 class="bmjkTitleLine">
					开工、完工及结算
				</h4>
				<div class="container-fluid" style="padding:0px 0px;">
					<div class="row-fluid">
						<div class="span12">
							<div class="bmjkTjgkBlock GC_JDGL_TCGK">
								<p>
								截至目前，<span class="ndText">2014</span>年建管中心应开工未开工
								<span  flag="hasLinkKfg"  bzfieldname="YKGWKG_ZX">XX</span>子项
								<span  flag="hasLinkKfg"  bzfieldname="YKGWKG_BD">XX</span>标段，
								已开（复）工<span  flag="hasLinkKfg" bzfieldname="YKG_ZX">XX</span>子项
								<span  flag="hasLinkKfg"  bzfieldname="YKG_BD">XX</span>标段。
								其中，延期开（复）工<span  flag="hasLinkKfg"  bzfieldname="YQKG_ZX">XX</span>子项<span  flag="hasLinkKfg"  bzfieldname="YQKG_BD">XX</span>标段。
								<br/>
								已完工<span  flag="hasLinkKfg"  bzfieldname="YWG_ZX">XX</span>子项<span  flag="hasLinkKfg"  bzfieldname="YWKG_BD">XX</span>标段。
								其中，延期完工<span   flag="hasLinkKfg"  bzfieldname="YQWG_ZX">XX</span>子项<span  flag="hasLinkKfg"  bzfieldname="YQWG_BD">XX</span>标段。
								<br/>
								—按施工合同类型划分：施工合同<span flag="hasLinkTCJH" bzfieldname="GCJS_TJGK_SGHTZX">XX</span>子项（<span flag="hasLinkTCJH" bzfieldname="GCJS_TJGK_SGHTBD">XX</span>个标段），
								BT合同<span flag="hasLinkTCJH" bzfieldname="GCJS_TJGK_BTSGHTZX">XX</span>子项（<span flag="hasLinkTCJH" bzfieldname="GCJS_TJGK_BTSGHTBD">XX</span>个标段）。 <br>
								—按施工情况划分：甩项结算<span flag="hasLink_gcjs" bzfieldname="GCJS_TJGK_SXJSZX">XX</span>子项（<span flag="hasLink_gcjs" bzfieldname="GCJS_TJGK_SXJSBD">XX</span>个标段），
								竣工（完工）结算<span flag="hasLink_gcjs" bzfieldname=GCJS_TJGK_JGJSZX>XX</span>子项（<span flag="hasLink_gcjs" bzfieldname="GCJS_TJGK_JGJSBD">XX</span>个标段）。<br>
								</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div align="center">
			<form method="post" id="queryForm">
			</form>
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
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
			<FORM name="frmPost_lwyj" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="ywid" id="ywid">
				<input type="hidden" name="txtFilter" order="" fieldname="" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id = "queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>