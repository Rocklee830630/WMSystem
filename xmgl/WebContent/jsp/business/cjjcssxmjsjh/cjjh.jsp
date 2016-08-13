<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>Insert title here</title>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script src="${pageContext.request.contextPath }/js/common/FixTable.js"></script>

<script type="text/javascript" charset="UTF-8">
	var controllername = "${pageContext.request.contextPath }/gcCjjhController.do";
	$(function(){
		generateNd($("#QueryND"));
		doSearch();
		showAutoComplete("QXMMC",controllername+"?xmmcAutoQuery","getXmmcQueryCondition");
		$("#btnQuery").click(function(){
			doSearch();
		});
		//按钮绑定事件（清空）
	    $("#btnClear").click(function() {
	        $("#queryForm").clearFormResult();
			generateNd($("#QueryND"));
	    });
	});
	function doSearch() {
		var data1 = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		var data = {
			msg : data1
		};
		$.ajax({
			url : controllername+"?query" ,
			cache : false,
			async : true,
			dataType : 'json',
			data:data,
			type:"post",
			success : function(response) {
				if(response.msg=="0"){
					var dataLength = $("#page_"+DT1.id+" form").find("img.noresult").length;
					if(dataLength==1){
						$("#page_"+DT1.id).find("form").remove();//删除旧图片
					} 
					$("#DT1").find("tbody").children().remove();
				}else{
					var resObj = convertJson.string2json1(response.msg);
					listObj = resObj.response.data;
					var htmlVal = null;
					$.each(listObj, function(i) {
						var objDate = $(listObj).get(i);
						var xh = objDate.XH.length > 14 ? objDate.XH.substring(0, 14)+"..." : objDate.XH;
						var xmmc = objDate.XMMC.length >= 18 ? objDate.XMMC.substring(0, 18)+"..." : objDate.XMMC;
						var xmjsnrgm = objDate.XMJSNRGM.length >= 20 ? objDate.XMJSNRGM.substring(0, 20)+"..." : objDate.XMJSNRGM;
					 	if(i==2) {
						//	alert(objDate.XMMC.length)
						}
						htmlVal += "<tr>";
						//htmlVal += "<td>"+(i+1)+"</td>";
						//htmlVal += "<td>"+objDate.JWXM+"</td>";
						//htmlVal += "<td>"+objDate.WBXM+"</td>";
						if(objDate.NODELEVEL=="1"){
							htmlVal += "<td colspan='4' style='font-weight:900;font-size:16px;' title='"+objDate.XMMC+"'>"+xmmc+"</td>";
						}else if(objDate.NODELEVEL=="2"){
							htmlVal += "<td colspan='4' style='font-weight:900;' title='"+objDate.XMMC+"'>"+xmmc+"</td>";
						}else{
							htmlVal += "<td title='"+objDate.XH+"'>"+xh+"</td>";
							htmlVal += "<td>"+objDate.MXS+"</td>";
							htmlVal += "<td>"+objDate.ZXSM+"</td>";
							htmlVal += "<td title='"+objDate.XMMC+"'>"+xmmc+"</td>";
						}
						var xmdz = (objDate["XMJSWZ_SV"]!=undefined?objDate["XMJSWZ_SV"]:objDate["XMJSWZ"]);
						if(xmdz.length>15){
							htmlVal += "<td><abbr title="+xmdz+">"+xmdz.substring(0,14)+"</abbr></td>";
						}else{
							htmlVal += "<td>"+xmdz+"</td>";
						}
						htmlVal += "<td title='"+objDate.XMJSNRGM+"'>"+xmjsnrgm+"</td>";
						htmlVal += "<td style='text-align:right;'>"+(objDate["JH_XJ_SV"]!=undefined?objDate["JH_XJ_SV"]:objDate["JH_XJ"])+"</td>";
						htmlVal += "<td style='text-align:right;'>"+(objDate["JH_GC_SV"]!=undefined?objDate["JH_GC_SV"]:objDate["JH_GC"])+"</td>";
						htmlVal += "<td style='text-align:right;'>"+(objDate["JH_ZQ_SV"]!=undefined?objDate["JH_ZQ_SV"]:objDate["JH_ZQ"])+"</td>";
						htmlVal += "<td style='text-align:right;'>"+(objDate["JH_QT_SV"]!=undefined?objDate["JH_QT_SV"]:objDate["JH_QT"])+"</td>";
						htmlVal += "<td>"+(objDate["XMXZ_SV"]!=undefined?objDate["XMXZ_SV"]:objDate["XMXZ"])+"</td>";
						htmlVal += "<td style='text-align:right;'>"+(objDate["ND_XJ_SV"]!=undefined?objDate["ND_XJ_SV"]:objDate["ND_XJ"])+"</td>";
						htmlVal += "<td style='text-align:right;'>"+(objDate["ND_GC_SV"]!=undefined?objDate["ND_GC_SV"]:objDate["ND_GC"])+"</td>";
						htmlVal += "<td style='text-align:right;'>"+(objDate["ND_ZQ_SV"]!=undefined?objDate["ND_ZQ_SV"]:objDate["ND_ZQ"])+"</td>";
						htmlVal += "<td style='text-align:right;'>"+(objDate["ND_QT_SV"]!=undefined?objDate["ND_QT_SV"]:objDate["ND_QT"])+"</td>";
						htmlVal += "<td>"+(objDate["NDMB_SV"]!=undefined?objDate["NDMB_SV"]:objDate["NDMB"])+"</td>";
						htmlVal += "<td>"+(objDate["ZRBM_SV"]!=undefined?objDate["ZRBM_SV"]:objDate["ZRBM"])+"</td>";
						htmlVal += "<td>"+(objDate["XMFR_SV"]!=undefined?objDate["XMFR_SV"]:objDate["XMFR"])+"</td>";
						htmlVal += "</tr>";
					});
				 	$("#DT1 tbody").html(htmlVal);
				 	
				}
				 	var rows = $("tbody tr" ,$("#DT1"));
			        
			    	var tr_obj = rows.eq(0);
			        var t = $("#DT1").getTableRows();
			        var tr_height = $(tr_obj).height();
			        var d = t*tr_height+getTableTh(2)+20;
	
			     	// 当高度大于500时，显示主页面列表的高度
			        if(d>500) d = g_iHeight-pageQuery-getTableTh(2);
					// 当没有数据的时候，只显示表头
					//	if(tr_height==null) d = getTableTh(2)+20;
			        window_width = document.documentElement.clientWidth;//$("#allDiv").width()
			   	   $("#DT1").fixTable({
			   			fixColumn: 4,//固定列数
			   			width:window_width-10,//显示宽度
			   			height:d//显示高度
			   		}); 
		   		
			}
		});
	}
	
	function formatTable() {
		var rows = $("tbody tr" ,$("#DT1"));
        
    	var tr_obj = rows.eq(0);
        var t = $("#DT1").getTableRows();
        var tr_height = $(tr_obj).height();
        var d = t*tr_height+getTableTh(2)+20;

     	// 当高度大于500时，显示主页面列表的高度
        if(d>500) d = g_iHeight-pageQuery-getTableTh(1);
         // 当没有数据的时候，只显示表头
     	if(tr_height==null) d = getTableTh(2)+20;
        window_width = document.documentElement.clientWidth;//$("#allDiv").width()
   	    $("#DT1").fixTable({
   			fixColumn: 4,//固定列数
   			width:window_width-10,//显示宽度
   			height:d//显示高度
   		});
	}
	
	function addXm() {
		$(window).manhuaDialog({"title":"新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/cjjcssxmjsjh/cjjhTree.jsp","modal":"1"});
	}
	/**
	 * 获取年度
	 */
	function generateNd(ndObj){
		ndObj.attr("src","T#GC_CJJH:distinct ND:ND as nnd:SFYX='1' order by nnd asc");
		ndObj.attr("kind","dic");
		ndObj.html('');
		setDefaultNd("QueryND");
		
	}
	function getXmmcQueryCondition(){
		var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":"S.pxh"}]}';
		var jsonData = eval('(' + initData + ')'); 
		//项目名称
		if("" != $("#QXMMC").val()){
			var defineCondition = {"value": "%"+$("#QXMMC").val()+"%","fieldname":"S.XMMC","operation":"like","logic":"and"};
			jsonData.querycondition.conditions.push(defineCondition);
		}
		return JSON.stringify(jsonData);
	}
	//弹出下达窗口
	function OpenMiddleWindow_xd(){
		 $(window).manhuaDialog({"title":"项目储备库  >下达审批","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmcbk/xdsp.jsp","modal":"1"});
	}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">年城建基础设施项目建设计划(明细表)
			  <span class="pull-right">
			  	<button class="btn" onclick="OpenMiddleWindow_xd()">项目下达</button>
			    <button id="insertBtn" class="btn"  type="button" onclick="addXm()">维护</button>
			  </span>
			</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<tr style="display:none;">
						<th width="3%" class="right-border bottom-border text-right">项目名称</th>
						<td width="20%" class="right-border bottom-border">
							<input class="span12" type="text" 
								fieldname="C.NODELEVEL" operation="!=" value="0">
						</td>
					</tr>
					<tr>	<!-- 	 
						<th width="3%" class="right-border bottom-border text-right">项目名称</th>
						<td width="20%" class="right-border bottom-border">
							<input class="span12" type="text" placeholder="" name="XMMC" fieldname="XMMC" check-type="maxlength" maxlength="500" operation="like" id="XMMC" autocomplete="off">
						</td>  -->
						<th width="2%" class="right-border bottom-border">年度</th>
						<td width="7%" class="right-border bottom-border">
							<select class="span12" id="QueryND" name="QueryND" noNullSelect ="true" 
								fieldname="C.ND" operation="=" defaultMemo="全部">
							</select>
						</td>
						<th width="3%" class="right-border bottom-border text-right">项目名称</th>
						<td width="20%" class="right-border bottom-border">
							<input class="span12" type="text" placeholder="" name="QXMMC"
								fieldname="C.XMMC" operation="like" id="QXMMC"
								autocomplete="off" tablePrefix="S">
						</td>
						<th width="5%" class="right-border bottom-border">投资类型</th>
						<td class="right-border bottom-border">
							<select	kind="dic" src="TZLX" id="TZLX" class="span12 4characters" name="TZLX" fieldname="TZLX" operation="=" defaultMemo="全部"></select>
						</td>
						<td class="text-left bottom-border text-right">
							<button	id="btnQuery" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
	                        <button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
			            </td>
					</tr>
				</table>
			</form>
			<div style="height:5px;"> </div>
			<div class="overFlowX">
			<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" noPage="true" pageNum="1000">
			  <thead>
              <tr>
				<th rowspan="2">&nbsp;序号&nbsp;</th>
				<th rowspan="2">&nbsp;项目数&nbsp;</th>
				<th rowspan="2">&nbsp;子项数目&nbsp;</th>
				<th rowspan="2">&nbsp;项目名称&nbsp;</th>
				<th rowspan="2">&nbsp;项目地址&nbsp;</th>
				<th rowspan="2">&nbsp;项目建设内容及规模&nbsp;</th>
				<th colspan="4">&nbsp;计划总投资&nbsp;</th>
				<th rowspan="2">&nbsp;项目建设阶段&nbsp;</th>
				<th colspan="5">&nbsp;年度计划投资&nbsp;</th>
				<th rowspan="2">&nbsp;责任部门&nbsp;</th>
				<th rowspan="2">&nbsp;项目法人&nbsp;</th>
              </tr>
              <tr>
				<th >&nbsp;小计&nbsp;</th>
				<th >&nbsp;工程费用&nbsp;</th>
				<th >&nbsp;征拆、排迁费用&nbsp;</th>
				<th >&nbsp;其他费用&nbsp;</th>
				
				<th >&nbsp;小计&nbsp;</th>
				<th >&nbsp;工程费用&nbsp;</th>
				<th >&nbsp;征拆、排迁费用&nbsp;</th>
				<th >&nbsp;其他费用&nbsp;</th>
				<th >&nbsp;年度建设目标&nbsp;</th>
              </tr>
              </thead>
              <tbody></tbody>
            </table>
            <table id="aaaa"></table>
			</div>
		</div>
	</div>		
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="ASC" fieldname ="PXH_SJ"/>
		<input type="hidden" name="resultXML"/>
		<input type="hidden" name="queryResult" id="queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
	</FORM>
</div>
</body>
</html>