<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<%@ taglib uri= "/tld/base.tld" prefix="app"%>

<app:base/>
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/bmgk/bmgk.css?version=20140402">
<script type="text/javascript">
	var controllername= "${pageContext.request.contextPath }/dtgkController.do";
	
	$(function() {
		doInit();
  		generateNd($("#qND"));
		// 项目总体概况（文字描述）
		xmztgk();
		// 项目总体进展（几字图表）
		xmztjz();
		// 动态表头
		queryList();
		// 新建续建
		queryXjXuj();
	});
	function doInit(){
		$("span[name='head']").html("截止"+getCurrentDate("yyyy年MM月DD日")+"，");
	}
	//年份查询
	function generateNd(ndObj) {
		ndObj.attr("src","T#GC_JH_SJ:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
		ndObj.attr("kind","dic");
		ndObj.html('');
		reloadSelectTableDic(ndObj);
		ndObj.val(new Date().getFullYear());
	}
	
	function tableTHInit(){
		
		 //可用TH表头json对象
		 TH_JSON = {};
		 TH_JSON['_XH'] = '<th  name="XH" id="_XH" rowspan="2" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>';
		 
		 // 将选中的字段中的intoJson属性按照格式组合
		 $("#columnsCheckbox input[type=checkbox]:checked").each(function(i) {
			 if($(this).attr("intoJson") != undefined) {
				 var listTh = convertJson.string2json1($(this).attr("intoJson"));
				 var thName = $(this).attr("thName");
				 var column = $(this).val();
				 var th = "<th ";
				 $.each(listTh, function(key, value) {
					 th = th + key + "='" + value+ "' ";
				 });
				 th += ">"+thName+"</th>";
				 
				 TH_JSON[column] = th;
			 }
		 });
		 
		 //指定TH表头对应的多级表头json对象
		 THJSONS = {};
		 THJSONS['NDZTZ'] = ['<th fieldname="GC" colindex=13 tdalign="right">&nbsp;工程&nbsp;</th>',
		 				'<th fieldname="ZC" colindex=14 tdalign="right">&nbsp;征拆&nbsp;</th>',
		 				'<th fieldname="QT" colindex=15 tdalign="right">&nbsp;其他&nbsp;</th>',
		 				'<th fieldname="JHZTZE" colindex=16 tdalign="right">&nbsp;合计&nbsp;</th>'];
		 
		 
			// 初始化字段选择的順序
			ORDER_ARRAY = new Array();

			/* 序号 */
			ORDER_ARRAY[0] = '_XH';
			/* 项目编号 */
			ORDER_ARRAY[1] = 'XMBH';
			/* 项目名称 */
			ORDER_ARRAY[2] = 'XMMC';
			/* 项目性质 */
			ORDER_ARRAY[3] = 'XMXZ';
			/* 项目类型 */
			ORDER_ARRAY[4] = 'XMLX';
			/* 项管公司 */
			ORDER_ARRAY[5] = 'XGGS';
			/* 建设位置 */
			ORDER_ARRAY[6] = 'JSWZ';
			/* 项目属性 */
			ORDER_ARRAY[7] = 'XMSX';
			/* 是否BT */
			ORDER_ARRAY[8] = 'SFBT';
			/* 年度目标 */
			ORDER_ARRAY[9] = 'NDMB';
			/* 年度总投资 */
			ORDER_ARRAY[10] = 'NDZTZ';
			/* 业主代表 */
			ORDER_ARRAY[11] = 'YZDB';

			// 选中的字段顺序组数
			CHECKED_COLUMNS_ARRAY = new Array();
			CHECKED_COLUMNS_ARRAY[0] = "_XH";
			var index = 1;
			// 将选中的字段重新按照ORDER_ARRAY数组的顺序排序
			for(var j = 0; j < ORDER_ARRAY.length; j++) {
				$("#columnsCheckbox input[type=checkbox]:checked").each(function(i) {
					var checkedColumn = $(this).val();
					if(checkedColumn == ORDER_ARRAY[j]) {
						CHECKED_COLUMNS_ARRAY[index] = checkedColumn;
						index++;
					}
				});
			}
		 
	}
	
	
	/**
	 * 根据用户勾选显示数据项和初始TH结构动态生成表头
	 * 参数1：表格id，参数2：复选框区域td的id
	 * (待重构)
	 */
	function generateTh(tableId,selectId) {
		$("#"+tableId +" thead").empty();
		var thObj,thHTML;//th对象，th的html字符
		var colIndex = 1;//th列序号
		var multiTr = 1;
		$("#"+tableId +" thead").append($('<tr></tr>'));
		$.each(CHECKED_COLUMNS_ARRAY, function(i){
			thHTML = TH_JSON[CHECKED_COLUMNS_ARRAY[i]];
			$("#"+tableId +" tr:eq(0)").append(thHTML);
			//非多表头
			if($("#"+tableId +" tr:eq(0) th:last").attr("colindex")){
				$("#"+tableId +" tr:eq(0) th:last").attr("colindex",colIndex);
				colIndex ++;
			}else{//多表头操作
				if(-1 == $("#"+tableId +" thead tr:eq(1)").index()) {
					$("#"+tableId +" thead").append($('<tr></tr>'));
				}
				thHTML = THJSONS[CHECKED_COLUMNS_ARRAY[i]];
				for(var j=0;j<thHTML.length;j++){
					$("#"+tableId +" thead tr:eq(1)").append(thHTML[j]);
					$("#"+tableId +" tr:eq(1) th:last").attr("colindex",colIndex);
					colIndex ++;
				}
				multiTr = 2;
			}
		});
		
		//设置多表头rowspan
		$("#"+tableId +" tr th").each(function(i) {
			if($("#"+tableId +" tr:eq(0) th:eq("+i+")").attr("colindex")) {
				$(this).attr("rowspan",multiTr);
			}
		});
		
	}
	
	$(function(){
		$("#queryList").click(function(){
			queryList();
		});
		

		$("input:checkbox").click(function() {
			queryList();
		});
	});
	
	
	
	function queryList() {
		tableTHInit();
		generateTh("columnsCheckboxList","columnsCheckbox");
		
		isNdztz = $("#NDZTZ_").is(":checked") ? "1" : "0";

        //生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, columnsCheckboxList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query&isNdztz="+isNdztz, data, columnsCheckboxList, "zjCallBack");
		

		
	}
	
	function zjCallBack() {
		if($("#columnsCheckboxList tbody tr").length != 0) {
			if(isNdztz == "1") {
				var rowJsonObj = convertJson.string2json1($("#columnsCheckboxList tbody tr:first").attr("rowjson"));
				$("#columnsCheckboxList tbody tr:last").clone(true).insertAfter($("#columnsCheckboxList tbody tr:last"));
				$("#columnsCheckboxList tbody tr:last th").text("");
				$("#columnsCheckboxList tbody tr:last").css("font-weight","bold");
				 
				$("#columnsCheckboxList thead th").each(function(i) {
						var str = i;
						if($(this).attr("colindex")) {
							$($("#columnsCheckboxList tbody tr:last td")[parseInt($(this).attr("colindex"),10)-2]).text("");
							if($(this).attr("fieldname")) {
								if($(this).attr("fieldname") == "GC") {
									$($("#columnsCheckboxList tbody tr:last td")[parseInt($(this).attr("colindex"),10)-2]).text(rowJsonObj.GCZJ_SV);
								}
								if($(this).attr("fieldname") == "ZC") {
									$($("#columnsCheckboxList tbody tr:last td")[parseInt($(this).attr("colindex"),10)-2]).text(rowJsonObj.ZCZJ_SV);
								}
								if($(this).attr("fieldname") == "QT") {
									$($("#columnsCheckboxList tbody tr:last td")[parseInt($(this).attr("colindex"),10)-2]).text(rowJsonObj.QTZJ_SV);
								}
								if($(this).attr("fieldname") == "JHZTZE") {
									$($("#columnsCheckboxList tbody tr:last td")[parseInt($(this).attr("colindex"),10)-2]).text(rowJsonObj.ZJ_SV);
								}
							}
						}
					});
			}
		}
	}

	// 清除表单
	$(function() {
		$("#query_clear").click(function() {
	       $("#queryForm").clearFormResult();
		});
	});

	//详细信息
	function rowView(index) {
		var obj = $("#columnsCheckboxList").getSelectedRowJsonByIndex(index);
		var id = convertJson.string2json1(obj).XMID;
		$(window).manhuaDialog(xmscUrl(id));
	}
	
	//根据结果放入表格
	function insertTable(result,tableId)
	{
//		var resultmsgobj = convertJson.string2json1(result);
		var resultobj = convertJson.string2json1(result.msg);
		var subresultmsgobj = resultobj.response.data[0];
	   $("span").each(function(i){
			var str = $(this).attr("bzfieldname");
			if(str!=''&&str!=undefined)
			{
				var str1=str+"_SV";
				if($(subresultmsgobj).attr(str1)!==''&&$(subresultmsgobj).attr(str1)!=undefined)
				{
		     		 $(this).html($(subresultmsgobj).attr(str1));
				}
				else{
					var flag = $(this).attr("flag");
					if($(subresultmsgobj).attr(str)!==''&&$(subresultmsgobj).attr(str)!=undefined){
						if($(subresultmsgobj).attr(str)!=0){
							var hasLink = $(this).attr("hasLink");
							switch(flag){
								case 'bd':
									$(this).html('<a href="javascript:void(0);" onclick="_blankXmxxBdxx(\''+str+'_SQL\')">'+$(subresultmsgobj).attr(str)+'</a>');
									break;
								case 'xm':
									$(this).html('<a href="javascript:void(0);" onclick="_blankXmxx(\'040001\',\'BMJK_SJ_'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
									break;
								case 'ZH':
									$(this).html('<a href="javascript:void(0);" onclick="openZhxx(\'设计综合信息\',\''+hasLink+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
									break;
								case 'LBJ':
									$(this).html('<a href="javascript:void(0);" onclick="openLbjxx(\'拦标价信息\',\''+hasLink+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
									break;	
								case 'JJG':
									$(this).html('<a href="javascript:void(0);" onclick="openJjgxx(\'交竣工信息\',\''+hasLink+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
									break;	
								case 'JCJC':
									$(this).html('<a href="javascript:void(0);" onclick="openJcjcxx(\'监测检测信息\',\''+hasLink+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
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
			}
		});
	}

	function xmztgk() {
		$.ajax({
			url : controllername+"?ztgkRs",
			dataType : "json",  
			type : 'post',
			success : function(result) {
				insertTable(result,"xmztgkDiv");	
			}
		});
	}
	
	function xmztjz() {
		$.ajax({
			url : controllername+"?ztjzRs",
			dataType : "json",  
			type : 'post',
			success : function(result) {
				insertTable(result,"xmztgkDiv");	
			}
		});
	}

	function queryXjXuj() {
        //生成json串
		var data = combineQuery.getQueryCombineData(queryXjxujListForm,frmPost, xjxujList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?xjxujRs", data, xjxujList, "xjxujRsCallBack");
	}
	
	// 总计样式加粗
	function xjxujRsCallBack() {
		$("#xjxujList tbody tr:last th").text("");
		$("#xjxujList tbody tr:last").css("font-weight","bold");
	}
	
	
	// 项目
	function _blankXmxx(ywlx, bmjkLx) {
		var  xmsc = {"title":"统筹计划管理信息","type":"text","content":g_sAppName+"/jsp/business/zrjm/dtgk/dtgl_xmxx.jsp","modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
	

	function _blankXmxxBdxx(sql) {
		var  xmsc = {"title":"统筹计划跟踪信息","type":"text","content":g_sAppName+"/jsp/business/zrjm/dtgk/dtgl_bdxx.jsp?sql="+sql,"modal":"1"};
		$(window).manhuaDialog(xmsc);
	}
</script>
</head>
	<body>
		
		<div class="container-fluid">
		<div class="B-small-from-table-auto" style="border: 0px;">
		<h4 style="background:#F0F0F0;color:#333;height:30px;padding:5px 5px 5px 5px;font-size:13pt;">
					项目总体情况
			</h4>
		</div>
			<div id="xmztgkDiv">
			<p class="xmztgk">
			<span name="head"></span>建管中心年度建设子项<span bzfieldname="XMCNT"></span>项（标段<span bzfieldname="BDCNT"></span>个），
			其中新建<span bzfieldname="XJXMCNT"></span>项，续建<span bzfieldname="XUJXMCNT"></span>项。
			目前，已开工<span bzfieldname="KGCNT"></span>项，完工<span bzfieldname="WGCNT"></span>项，
			未开工<span bzfieldname="WKGCNT"></span>项
			</p>
			
			<p class="xmztgk">
			在<span bzfieldname="XUJXMCNT"></span>项续建项目中：
			未完工<span bzfieldname="XUJ_WWG_CNT"></span>项，已开工但未完工<span bzfieldname="XUJ_YKG_WWG_CNT"></span>项，未开工<span bzfieldname="XUJ_WKG_CNT"></span>项；
			未完成排迁<span flag="xm" bzfieldname="XUJ_WPQ_CNT"></span>项，未完成拆迁<span bzfieldname="XUJ_WZC_CNT"></span>项；
			未完成施工招标<span bzfieldname="XUJ_WSGZB_CNT"></span>项，未完成监理招标<span bzfieldname="XUJ_WJLZB_CNT"></span>项；
			在未完成施工招标中，未完拦标价<span bzfieldname="XUJ_WSG_WZJ_CNT"></span>项；
			未完施工图<span bzfieldname="XUJ_WWSGT_CNT"></span>项，未完拆迁图<span bzfieldname="XUJ_WWCQT_CNT"></span>项，未完排迁图<span bzfieldname="XUJ_WWPQT_CNT"></span>项；
			前期手续尚未全部完成<span bzfieldname="XUJ_WQQ_CNT"></span>项。
			</p>
			
			<p class="xmztgk">
			在<span bzfieldname="XJXMCNT"></span>项新建项目中：
			已落实项目管理公司<span bzfieldname="XJ_YLSXGGS_CNT"></span>项；
			全部完成前期手续<span bzfieldname="XJ_YQQ_CNT"></span>项；
			已完成拆迁图<span bzfieldname="XJ_YCQT_CNT"></span>项，已完成排迁图<span bzfieldname="XJ_YPQT_CNT"></span>项，已完成施工图<span bzfieldname="XJ_YSGT_CNT"></span>项；
			已完成拦标价<span bzfieldname="XJ_YZJ_CNT"></span>项；
			已完成施工招标<span bzfieldname="XJ_YWSGZB_CNT"></span>项，已完成监理招标<span bzfieldname="XJ_YWJLZB_CNT"></span>项；
			已完成排迁<span bzfieldname="XJ_YPQ_CNT"></span>项，已完成拆迁<span bzfieldname="XJ_YZC_CNT"></span>项；
			已开工<span bzfieldname="XJ_YKG_CNT"></span>项，已开工但未完工<span bzfieldname="XJ_YKG_WWG_CNT"></span>项，已完工<span bzfieldname="XJ_YWG_CNT"></span>项。
			</p>
			</div>
			
			
			<div class="B-small-from-table-auto" style="border: 0px;">
			<h4 style="background:#F0F0F0;color:#333;height:30px;padding:5px 5px 5px 5px;font-size:13pt;">
					项目阶段进展情况
			</h4>
				<div class="container-fluid">
					<div class="row-fluid">
						<div class="span12" id="xmztjzDiv">
							<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="">
								<tr>
									<td colspan="6"></td>
									<td class="titleTD" title="征收已完，排迁未开始（不涉及排迁的统计施工未开始的）">
										<span>征收(</span><span bzfieldname="ZS"></span><span>)</span>
									</td>
									<td class="titleTD" colspan=2 title="排迁已完，施工未开始">
										<span>排迁(</span><span bzfieldname="PQ"></span><span>)</span>
									</td>
									<td colspan=11></td>
								</tr>
								<tr>
									<td colspan=5 rowspan=2></td>
									<td class="upToRightTD" rowspan=2>
										<div>
										</div>
									</td>
									<td class="horizontalTD">
										<div class="containerDiv" attr="征收的圆点">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="concentricCircle"></div>
										</div>
									</td>
									<td class="horizontalTD" colspan=2>
										<div class="containerDiv" attr="排迁的圆点">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="concentricCircle"></div>
										</div>
									</td>
									<td class="rightToDownTD" rowspan=2>
										<div></div>
									</td>
									<td colspan=10 rowspan=2></td>
								</tr>
								<tr style="height:20px;">
									<td></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td class="brightTD">
										<div title="可研已完，前期未完">
											<!-- 前期 -->
											<br/><span class="titleSpan">立项可研</span><br/><span class="contentSpan" bzfieldname="QQ_KYPF"></span><span  class="contentSpan">/</span><span class="contentSpan">-</span>
										</div>
									</td>
									<td class="darkTD">
										<div title="土地审批已完，前期未完">
											<!-- 前期 -->
											<br/><span class="titleSpan">土地审批</span><br/><span bzfieldname="QQ_HPJDS" class="contentSpan"></span><span class="contentSpan">/</span><span class="contentSpan">-</span>
										</div>
									</td>
									<td class="blankTD"></td>
									<td class="brightTD">
										<div>
											<!-- 设计 -->
											<br/><span class="titleSpan">设计任务书</span><br/><span bzfieldname="SJ_SJRWS" class="contentSpan"></span><span class="contentSpan">/</span><span class="contentSpan">-</span>
										</div>
									</td>
									<td class="darkTD">
										<div>
											<!-- 设计 -->
											<br/><span class="titleSpan">概算</span><br/><span bzfieldname="SJ_GS" class="contentSpan"></span><span class="contentSpan">/</span><span class="contentSpan">-</span>
										</div>
									</td>
									<td class="verticalTD">
										<div></div>
									</td>
									<td class="brightTD">
										<div>
											<!-- 征收 -->
											<br/><span class="titleSpan">涉及征收</span><br/><span bzfieldname="ZC_SJZS" class="contentSpan"></span><span class="contentSpan">/</span><span class="contentSpan">-</span>
										</div>
									</td>
									<td class="blankTD"></td>
									<td class="brightTD">
										<div>
											<!-- 排迁 -->
											<br/><span class="titleSpan">涉及排迁</span><br/><span bzfieldname="PQ_SJPQ" class="contentSpan"></span><span class="contentSpan">/</span><span class="contentSpan">-</span>
										</div>
									</td>
									<td class="verticalTD">
										<div class=""></div>
									</td>
									<td class="brightTD">
										<div>
											<!-- 造价 -->
											<br/><span class="titleSpan">拦标价</span><br/><span bzfieldname="ZJ_LBJ_XMS" class="contentSpan"></span><span class="contentSpan">/</span><span bzfieldname="ZJ_LBJ_BDS" class="contentSpan"></span>
										</div>
									</td>
									<td class="blankTD"></td>
									<td class="darkTD">
										<div>
											<!-- 招投标 -->
											<br/><span class="titleSpan">设计招标</span><br/><span bzfieldname="SJZB_XM" class="contentSpan"></span><span class="contentSpan">/</span><span bzfieldname="SJZB_BD" class="contentSpan"></span>
										</div>
									</td>
									<td class="blankTD"></td>
									<td class="darkTD">
										<div>
											<!-- 合同 -->
											<br/><span class="titleSpan">设计合同</span><br/><span bzfieldname="SJHT_XM" class="contentSpan"></span><span class="contentSpan">/</span><span bzfieldname="SJHT_BD" class="contentSpan"></span>
										</div>
									</td>
									<td class="blankTD"></td>
									<td class="brightTD">
										<div>
											<!-- 完工 -->
											<br/><span class="titleSpan">已开工</span><br/><span bzfieldname="YKGXM" class="contentSpan"></span><span class="contentSpan">/</span><span bzfieldname="YKGBD" class="contentSpan"></span>
										</div>
									</td>
									<td class="brightTD">
										<div>
											<!-- 完工 -->
											<br/><span class="titleSpan">已复工</span><br/><span bzfieldname="FGLXM" class="contentSpan"></span><span class="contentSpan">/</span><span bzfieldname="FGLBD" class="contentSpan"></span>
										</div>
									</td>
									<td class="blankTD"></td>
									<td class="darkTD">
										<div>
											<!-- 交工 -->
											<br/><span class="titleSpan">已交工</span><br/><span bzfieldname="YJGXM" class="contentSpan"></span><span class="contentSpan">/</span><span bzfieldname="YJGBD" class="contentSpan"></span>
										</div>
									</td>
								</tr>
								<tr>
									<td class="darkTD">
										<div>
											<!-- 前期 -->
											<br/><span class="titleSpan">规划审批</span><br/><span bzfieldname="QQ_GCXKZ" class="contentSpan"></span><span class="contentSpan">/</span><span class="contentSpan">-</span>
										</div>
									</td>
									<td class="brightTD">
										<div>
											<!-- 前期 -->
											<br/><span class="titleSpan">施工许可</span><br/><span bzfieldname="QQ_SGXKXM" class="contentSpan"></span><span class="contentSpan">/</span><span bzfieldname="QQ_SGXKBD" class="contentSpan"></span>
										</div>
									</td>
									<td class="blankTD"></td>
									<td class="darkTD">
										<div>
											<!-- 设计 -->
											<br/><span class="titleSpan">拆迁图</span><br/><span bzfieldname="SJ_CQT" class="contentSpan"></span><span class="contentSpan">/</span><span class="contentSpan">-</span>
										</div>
									</td>
									<td class="darkTD">
										<div>
											<!-- 设计 -->
											<br/><span class="titleSpan">排迁图</span><br/><span bzfieldname="SJ_PQT" class="contentSpan"></span><span class="contentSpan">/</span><span class="contentSpan">-</span>
										</div>
									</td>
									<td class="verticalTD">
										<div></div>
									</td>
									<td class="darkTD">
										<div>
										<!-- 征收 -->
											<br/><span class="titleSpan">已完成</span><br/><span bzfieldname="ZC_YWC" class="contentSpan"></span><span class="contentSpan">/</span><span class="contentSpan">-</span>
										</div>
									</td>
									<td class="blankTD"></td>
									<td class="darkTD">
										<div>
										<!-- 排迁 -->
											<br/><span class="titleSpan">已完成</span><br/><span bzfieldname="PQ_YWC" class="contentSpan"></span><span class="contentSpan">/</span><span class="contentSpan">-</span>
										</div>
									</td>
									<td class="verticalTD">
										<div class=""></div>
									</td>
									<td class="darkTD">
										<div>
											<!-- 造价 -->
											<br/><span class="titleSpan">结算</span><br/><span bzfieldname="ZJ_JSXMS" class="contentSpan"></span><span class="contentSpan">/</span><span bzfieldname="ZJ_JSBDS" class="contentSpan"></span>
										</div>
									</td>
									<td class="blankTD"></td>
									<td class="darkTD">
										<div>
											<!-- 招投标 -->
											<br/><span class="titleSpan">施工招标</span><br/><span bzfieldname="SGZB_XM" class="contentSpan"></span><span class="contentSpan">/</span><span bzfieldname="SGZB_BD" class="contentSpan"></span>
										</div>
									</td>
									<td class="blankTD"></td>
									<td class="darkTD">
										<div>
											<!-- 合同 -->
											<br/><span class="titleSpan">施工合同</span><br/><span bzfieldname="SGHT_XM" class="contentSpan"></span><span class="contentSpan">/</span><span bzfieldname="SGHT_BD" class="contentSpan"></span>
										</div>
									</td>
									<td class="blankTD"></td>
									<td class="darkTD">
										<div>
											<!-- 完工 -->
											<br/><span class="titleSpan">已停工</span><br/><span bzfieldname="TGLXM" class="contentSpan"></span><span class="contentSpan">/</span><span bzfieldname="TGLBD" class="contentSpan"></span>
										</div>
									</td>
									<td class="darkTD">
										<div>
											<!-- 完工 -->
											<br/><span class="titleSpan">已完工</span><br/><span bzfieldname="YWGXM" class="contentSpan"></span><span class="contentSpan">/</span><span bzfieldname="YWGBD" class="contentSpan"></span>
										</div>
									</td>
									<td class="blankTD"></td>
									<td class="brightTD">
										<div>
											<!-- 交竣工 -->
											<br/><span class="titleSpan">已竣工</span><br/><span bzfieldname="YJUNGXM" class="contentSpan"></span><span class="contentSpan">/</span><span bzfieldname="YJUNGBD" class="contentSpan"></span>
										</div>
									</td>
								</tr>
								<tr>
									<td colspan=3></td>
									<td class="brightTD">
										<div>
										<!-- 设计 -->
											<br/><span class="titleSpan">施工图</span><br/><span bzfieldname="SJ_SGTXM" class="contentSpan"></span><span class="contentSpan">/</span><span bzfieldname="SJ_SGTBD" class="contentSpan"></span>
										</div>
									</td>
									<td></td>
									<td class="verticalTD">
										<div></div>
									</td>
									<td class="darkTD">
										<div>
										<!-- 征收 -->
											<br/><span class="titleSpan">未完成</span><br/><span bzfieldname="ZC_WWC" class="contentSpan"></span><span class="contentSpan">/</span><span class="contentSpan">-</span>
										</div>
									</td>
									<td class="blankTD"></td>
									<td class="darkTD">
										<div>
										<!-- 排迁 -->
											<br/><span class="titleSpan">未完成</span><br/><span bzfieldname="PQ_WWC" class="contentSpan"></span><span class="contentSpan">/</span><span class="contentSpan">-</span>
										</div>
									</td>
									<td class="verticalTD">
										<div class=""></div>
									</td>
									<td></td>
									<td class="blankTD"></td>
									<td class="darkTD">
										<div>
											<!-- 招投标 -->
											<br/><span class="titleSpan">监理招标</span><br/><span bzfieldname="JLZB_XM" class="contentSpan"></span><span class="contentSpan">/</span><span flag="bd" bzfieldname="JLZB_BD" class="contentSpan"></span>
										</div>
									</td>
									<td class="blankTD"></td>
									<td class="darkTD">
										<div>
											<!-- 合同 -->
											<br/><span class="titleSpan">监理合同</span><br/><span bzfieldname="JLHT_XM" class="contentSpan"></span><span class="contentSpan">/</span><span bzfieldname="JLHT_BD" class="contentSpan"></span>
										</div>
									</td>
									<td class="blankTD"></td>
									<td class="darkTD">
										<div>
											<!-- 完工 -->
											<br/><span class="titleSpan">未完工</span><br/><span bzfieldname="WWGXM" class="contentSpan"></span><span class="contentSpan">/</span><span bzfieldname="WWGBD" class="contentSpan"></span>
										</div>
									</td>
									<td colspan=3>
									</td>
								</tr>
								<tr>
									<td class="horizontalTD" colspan=2>
										<div class="containerDiv" attr="前期的圆点">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="concentricCircle"></div>
										</div>
									</td>
									<td class="horizontalTD" colspan=3>
										<div class="containerDiv" attr="设计的圆点">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="concentricCircle"></div>
										</div>
									</td>
									<td class="rightToUpTD">
										<div></div>
									</td>
									<td colspan=3></td>
									<td class="downToRightTD">
										<div></div>
									</td>
									<td class="horizontalTD">
										<div class="containerDiv" attr="造价的圆点">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="concentricCircle"></div>
										</div>
									</td>
									<td class="horizontalTD" colspan=2>
										<div class="containerDiv" attr="招投标的圆点">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="concentricCircle"></div>
										</div>
									</td>
									<td class="horizontalTD" colspan=2>
										<div class="containerDiv" attr="合同的圆点">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="concentricCircle"></div>
										</div>
									</td>
									<td class="horizontalTD" colspan=3>
										<div class="containerDiv" attr="完工的圆点">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="concentricCircle"></div>
										</div>
									</td>
									<td class="horizontalTD" colspan=2>
										<div class="containerDiv" attr="交竣工的圆点">
											<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="concentricCircle"></div>
										</div>
									</td>
								</tr>
								<tr>
									<td class="titleTD" colspan=2>
										<span>前期(</span><span bzfieldname="QQ_XMS"></span><span>/</span><span bzfieldname="QQ_BDS"></span><span>)</span>
									</td>
									<td class="titleTD" colspan=3>
										<span>设计(</span><span bzfieldname="SJ_XMS"></span><span>/</span><span bzfieldname="SJ_BDS"></span><span>)</span>
									</td>
									<td colspan=5></td>
									<td class="titleTD">
										<span>造价(</span><span bzfieldname="ZJ_LBJ_XMS"></span><span>/</span><span bzfieldname="ZJ_LBJ_BDS"></span><span>)</span>
									</td>
									<td class="titleTD" colspan=2>
										<span>招投标(</span><span bzfieldname="ZB_XM"></span><span>/</span><span bzfieldname="ZB_BD"></span><span>)</span>
									</td>
									<td class="titleTD" colspan=2>
										<span>合同(</span><span bzfieldname="HT_XM"></span><span>/</span><span bzfieldname="HT_BD"></span><span>)</span>
									</td>
									<td class="titleTD" colspan=3>
										<span>完工(</span><span bzfieldname="WGXM"></span><span>/</span><span bzfieldname="WGBD"></span><span>)</span>
									</td>
									<td class="titleTD" colspan=2>
										<span>交竣工(</span><span bzfieldname="JUNGXM"></span><span>/</span><span bzfieldname="JUNGBD"></span><span>)</span>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
			<p></p>
			
			<div class="B-small-from-table-autoConcise">
			<h4 style="background:#F0F0F0;color:#333;height:30px;padding:5px 5px 0px 5px;font-size:13pt;">
					新续建项目情况
			</h4>
				<form method="post" id="queryXjxujListForm"></form>
      			<table width="100%" class="table-hover table-activeTd B-table" id="xjxujList" noPage="true">
      				<thead>
                    	<tr>
                    		<th colindex=1 rowspan="2" name="XH" id="_XH">&nbsp;#&nbsp;</th>
                    		<th colindex=2 rowspan="2" fieldname="XZ" tdalign="center" rowMerge="true"></th>
                    		<th colindex=3 rowspan="2" fieldname="YKWK" tdalign="center"></th>
							<th colindex=4 rowspan="2" fieldname="ZJ" tdalign="center">总计</th>
							<th colindex=5 rowspan="2" fieldname="YWZB" tdalign="center" width="3%">完成招标</th>
							<th colindex=6 rowspan="2" fieldname="WCZJ" tdalign="center" width="3%">完成造价</th>
							<th colspan="6" tdalign="center">完成设计</th>
							<th colindex=13 rowspan="2" fieldname="WCPQ" tdalign="center" width="3%">完成排迁</th>
							<th colindex=14 rowspan="2" fieldname="WCZS" tdalign="center" width="3%">完成征收</th>
							<th colspan="2" tdalign="center">完成前期</th>
							<th colspan="3" tdalign="center">开工条件</th>
							<th colindex=20 rowspan="2" fieldname="YWG" tdalign="center" width="3%">已完工</th>
						</tr>
                    	<tr>
	                    	<!-- 完成设计 -->
							<th colindex=7 tdalign="center" fieldname="SGT">施工图</th>
							<th colindex=8 tdalign="center" fieldname="SGTSC">审查</th>
							<th colindex=9 tdalign="center" fieldname="PQT">排迁图</th>
							<th colindex=10 tdalign="center" fieldname="CQT">拆迁图</th>
							<th colindex=11 tdalign="center" fieldname="F_AN">方案</th>
							<th colindex=12 tdalign="center" fieldname="RWS">任务书</th>
							
	                    	<!-- 完成前期 -->
							<th colindex=15 tdalign="center" fieldname="SGXK">施工许可</th>
							<th colindex=16 tdalign="center" fieldname="KGL">开工令</th>
							
	                    	<!-- 开工条件 -->
							<th colindex=17 tdalign="center" fieldname="GHSP">规划审批</th>
							<th colindex=18 tdalign="center" fieldname="TDSP">土地审批</th>
							<th colindex=19 tdalign="center" fieldname="LXSP">立项审批</th>
						</tr>
						
					</thead>
					<tbody></tbody>
      			</table>
			</div>
			<div class="B-small-from-table-autoConcise" style="border: 0px;">
				 	<h4 style="background:#F0F0F0;color:#333;height:30px;padding:5px 5px 0px 5px;font-size:13pt;">项目动态查询统计</h4>
					<form method="post" id="queryForm">
      					<table class="B-table">
      						<TR  style="display:none;">
						        <th width="2%" class="right-border bottom-border">年度</th>
								<td width="7%" class="right-border bottom-border">
									 <select id="qND" class="span4 year" name="ND" keep="true" defaultMemo="全部" fieldname="ND" operation="=" logic="and" ></select> 
								</td>
					        </TR>
							<tr>
								
								<th width="2%" class="right-border bottom-border">是否BT</th>
								<td class="bottom-border">
									<select class="span12 year" id="ISBT" name="ISBT" logic="and"
										fieldname="ISBT" kind="dic" src="SF" operation="="
										defaultMemo="全部"></select>
								</td>
								<th width="5%" class="right-border bottom-border">项管公司</th>
								<td class="bottom-border">
									<select class="span12 2characters" id="XMGLGS" name="QXMGLGS" defaultMemo="全部"  fieldname="XMGLGS" operation="=" kind="dic" src="T#VIEW_YW_XMGLGS:ROW_ID:BMJC"></select>
								</td>
								<th width="8%" class="right-border bottom-border">业主代表</th>
					 	 		<td class="bottom-border right-border">
					 	 			<select class="span12 person" id="YZDB" kind="dic"  operation="="
					 	 			src="T#VIEW_YW_ORG_PERSON:ACCOUNT:NAME:PERSON_KIND = '3' and DEPARTMENT in (select row_id from VIEW_YW_ORG_DEPT where EXTEND1='2')" 
					 	 			fieldname="YZDB" name="YZDB"></select>
					 	 		</td>	
								<th width="5%" class="right-border bottom-border">项目名称</th>
								<td class="right-border bottom-border">
									<input class="span3" type="text" id="XMMC" name="XMMC" fieldname="XMMC" operation="like" logic="and">
								</td>
								
								<td width="13%"  class="text-left bottom-border text-right" rowspan="2">
									<button	id="queryList" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
                    				<button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
					            </td>
							</tr>
							
							<tr>
								<th class="right-border bottom-border">项目性质</th>
								<td class="right-border bottom-border">
									<select class="span2 year" id="XJXJ" name="XJXJ"
										fieldname="XJXJ" kind="dic" src="XMXZ" operation="=" logic="and"
										defaultMemo="全部"></select>
								</td>
								<th class="right-border bottom-border">项目属性</th>
								<td class="right-border bottom-border">
									<select class="span3 year" id="XMSX" name="XMSX"
										fieldname="XMSX" kind="dic" src="XMSX" operation="=" logic="and"
										defaultMemo="全部"></select>
								</td>
								<th class="right-border bottom-border">项目类型</th>
								<td class="right-border bottom-border">
									<select class="span12 4characters" id="XMLX" name="XMLX" logic="and"
										fieldname="XMLX" kind="dic" src="XMLX" operation="="
										defaultMemo="全部"></select>
								</td>
								<th class="right-border bottom-border"></th>
								<td class="right-border bottom-border">
								</td>
							</tr>
						</table>
      				</form>
				
				
				<fieldset class="b_ddd">
					<legend>字段选择</legend>
					
					<div id="columnsCheckbox">
					<div>
					<table border="0">
					 <tr>
					  <td style="min-width:120px;"><label class="checkbox inline">项目基本信息：</label></td>
					  <td>
						<label class="checkbox inline">项目编号<input disabled type="checkbox" value="XMBH" intoJson="{fieldname:'XMBH',rowspan:'2',colindex:'2',hasLink:'true',linkFunction:'rowView', tdalign:'center'}" thName="&nbsp;项目编号&nbsp;" checked/></label>
					<!--   </td>
					  <td> -->
						<label class="checkbox inline">项目名称<input disabled type="checkbox" value="XMMC" intoJson="{fieldname:'XMMC',rowspan:'2',colindex:'3', maxlength:'10'}" thName="&nbsp;项目名称&nbsp;" checked/></label>
					<!--   </td>
					  <td> -->
					  	<label class="checkbox inline">项目性质<input type="checkbox" value="XMXZ" intoJson="{fieldname:'XMXZ',rowspan:'2',colindex:'5', tdalign:'center'}" thName="&nbsp;项目性质&nbsp;" checked/></label>
					 <!--   </td>
					  <td> -->
					  	<label class="checkbox inline">项目类型<input type="checkbox" value="XMLX" intoJson="{fieldname:'XMLX',rowspan:'2',colindex:'7', tdalign:'center'}" thName="&nbsp;项目类型&nbsp;" checked/></label>
					  <!--   </td>
					  <td> -->
					  	<label class="checkbox inline">建设位置<input type="checkbox" value="JSWZ" intoJson="{fieldname:'JSWZ',rowspan:'2',colindex:'7', maxlength:'10'}" thName="&nbsp;建设位置&nbsp;" checked/></label>
					<!--   </td>
					  <td> -->
					  	<label class="checkbox inline">项目属性<input type="checkbox" value="XMSX" intoJson="{fieldname:'XMSX',rowspan:'2',colindex:'6', tdalign:'center'}" thName="&nbsp;项目属性&nbsp;" checked/></label>
					<!--   </td>
					  <td> -->
						<label class="checkbox inline">是否BT<input type="checkbox" value="SFBT" intoJson="{fieldname:'SFBT',rowspan:'2',colindex:'8', tdalign:'center'}" thName="&nbsp;是否BT&nbsp;" checked/></label>
					 <!--   </td>
					  <td> -->
						<label class="checkbox inline">年度目标<input type="checkbox" value="NDMB" intoJson="{fieldname:'NDMB',rowspan:'2',colindex:'9', maxlength:'10'}" thName="&nbsp;年度目标&nbsp;" checked/></label>
					<!--   </td>
					  <td> -->
					  	<label class="checkbox inline">年度总投资<input type="checkbox" value="NDZTZ" id="NDZTZ_" name="NDZTZ_" intoJson="{colspan:'4'}" thName="&nbsp;年度总投资&nbsp;" checked/></label>
					 <!--   </td>
					  <td> -->
					  	<label class="checkbox inline">项目年度<input type="checkbox" value="XMND" intoJson="{fieldname:'XMND',rowspan:'2',colindex:'4', tdalign:'center'}" thName="&nbsp;项目年度&nbsp;" checked/></label>
						<!-- 
						<label class="checkbox inline">工程进展（多）</label>
						<label class="checkbox inline"><input type="checkbox" value="GCJZ" intoJson="{colspan:'4'}" thName="&nbsp;工程进展&nbsp;" checked/></label>
					  
						<label class="checkbox inline">test</label>
						<label class="checkbox inline"><input type="checkbox" value="test" intoJson="{fieldname:'test',rowspan:'2',colindex:'4'}" thName="&nbsp;test&nbsp;" checked/></label>
					  
						<label class="checkbox inline">工程进展1（多）</label>
						<label class="checkbox inline"><input type="checkbox" value="GCJZ1" intoJson="{colspan:'4'}" thName="&nbsp;工程进展1&nbsp;" checked/></label>
					  
						<label class="checkbox inline">test1</label>
						<label class="checkbox inline"><input type="checkbox" value="test1" intoJson="{fieldname:'test1',rowspan:'2',colindex:'4'}" thName="&nbsp;test1&nbsp;" checked/></label>
					   -->
					  </td>
					 </tr>
					 
					 <tr>
					  <td><label class="checkbox inline">项目组织信息：</label></td>
					  <td>
					  	
						<label class="checkbox inline">项管公司<input type="checkbox" value="XGGS" intoJson="{fieldname:'XGGS',rowspan:'2',colindex:'11', maxlength:'12', tdalign:'center'}" thName="&nbsp;项管公司&nbsp;" checked/></label>
					 <!--   </td>
					  <td> -->
					  	<label class="checkbox inline">业主代表<input type="checkbox" value="YZDB" intoJson="{fieldname:'YZDB',rowspan:'2',colindex:'10'}" thName="&nbsp;业主代表&nbsp;" checked/></label>
					  </td>
					 </tr>
					</table>
					
					</div>
					
					</div>
				</fieldset>
				
				<div style="height:5px;"></div>
				<div class="B-small-from-table-autoConcise">
					<div class="overFlowX">
					<table width="100%" class="table-hover table-activeTd B-table" id="columnsCheckboxList" type="single"  pageNum="10">
		                <thead>	</thead>
						<tbody>
						
						</tbody>
	                </table>
	                </div>
                </div>	                		
	                    
			</div>
		</div>
		

<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id="queryXML">
		<input type="hidden" name="txtXML" id="txtXML">
		<input type="hidden" name="txtFilter" order="asc" fieldname="XMBH" id="txtFilter">
		<input type="hidden" name="resultXML" id="resultXML">
		<input type="hidden" name="queryResult" id="queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">
	</FORM>
</div>
	</body>
</html>