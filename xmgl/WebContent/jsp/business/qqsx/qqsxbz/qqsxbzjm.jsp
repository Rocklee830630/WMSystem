<!DOCTYPE html>
<html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<head>
<script type="text/javascript" src="${pageContext.request.contextPath }/echarts/asset/js/esl/esl.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/bmgk/bmgk.css?version=20140419">
<style type="text/css">
body {font-size:14px;}
h2 {display:inline; line-height:2em;}
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
.table1 {
}
.table1 tr td,.table1 tr th {
	line-height: 1.5em;
	padding: 4px;
	border-right: #000 solid 0;
	border-bottom: #000 solid 0;
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
.stageTable .blankTD{
	width:3%;
	padding:0px;
}
.stageTable .contentTD{
	width:11%;
	padding:0px;
	height:100%;
}
</style>
<script src="../js/echars/esl.js"></script>
<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/qqsx/bzgkController.do";

	var p_ec = null; 
	require.config({
		paths:{ 
			'echarts' : '${pageContext.request.contextPath }/echarts/js/echarts',
			'echarts/chart/bar' : '${pageContext.request.contextPath }/echarts/js/echarts'
		}
	});
	function doInit(){
		var nd = $("#ND").val();
		$(".ndText").text(nd);
		g_bAlertWhenNoResult = true;
		querySx(nd);
		querySxZxtColumn2d(nd);
		queryLx(nd);
		queryLxZxtColumn2d(nd);
		queryTd(nd);
		queryTdZxtColumn2d(nd);
		queryGh(nd);
		queryGhZxtColumn2d(nd);
		querySg(nd);
		querySgZxtColumn2d(nd);
		
		queryZtqk(nd);
		queryDst1(nd);
		queryDst2(nd);
	}
	function doSearch(m,n){
		var actionUrl = controllername + "?"+m;
		$.ajax({
			url : actionUrl,
			success: function(result){
				var resultmsgobj = convertJson.string2json1(result);
				var resultobj = convertJson.string2json1(resultmsgobj.msg);
				var len = resultobj.response.data.length;
				var showHtml='';
			}
		});
	}
	//根据年度查询
	function queryND()
	{
		doInit();
	}
	//手续汇总
	function querySx(nd)
	{
		var action1 = controllername + "?querySx&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result)
			{
				addTable(result,"sxtable");
			}
		});
	}

	function querySxZxtColumn2d(nd){
		var action = controllername + "?querySxZxtColumn2d&nd="+nd;
		$.ajax({
			url : action,
			dataType:"json",
			async:false,
			success: function(result){
				if(result.msg==0){
					return;
				}else{
					var resObj = convertJson.string2json1(result.msg);
					var myChart = p_ec.init(document.getElementById("sxDiv"));
					myChart.setOption(resObj);					
				}
			}
		});
		
	}
	//立项可研批复手续进展
	function queryLx(nd)
	{
		var action1 = controllername + "?queryLx&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result)
			{
				addTable(result,"lxtable");
			}
		});
	}

	function queryLxZxtColumn2d(nd){
		var action = controllername + "?queryLxZxtColumn2d&nd="+nd;
		$.ajax({
			url : action,
			dataType:"json",
			async:false,
			success: function(result){
				if(result.msg==0){
					return;
				}else{
					var resObj = convertJson.string2json1(result.msg);
					var myChart = p_ec.init(document.getElementById("lxDiv"));
					myChart.setOption(resObj);
				}
			}
		});
	}

	//土地手续进展
	function queryTd(nd)
	{
		var action1 = controllername + "?queryTd&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result)
			{
				addTable(result,"tdtable");
			}
		});
	}

	function queryTdZxtColumn2d(nd){
		var action = controllername + "?queryTdZxtColumn2d&nd="+nd;
		$.ajax({
			url : action,
			dataType:"json",
			async:false,
			success: function(result){
				if(result.msg==0){
					return;
				}else{
					var resObj = convertJson.string2json1(result.msg);
					var myChart = p_ec.init(document.getElementById("tdDiv"));
					myChart.setOption(resObj);
					
				}
			}
		});
	}
	//规划手续进展
	function queryGh(nd)
	{
		var action1 = controllername + "?queryGh&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result)
			{
				addTable(result,"ghtable");
			}
		});
	}

	function queryGhZxtColumn2d(nd){
		var action = controllername + "?queryGhZxtColumn2d&nd="+nd;
		$.ajax({
			url : action,
			dataType:"json",
			async:false,
			success: function(result){
				if(result.msg==0){
					return;
				}else{
					var resObj = convertJson.string2json1(result.msg);
					var myChart = p_ec.init(document.getElementById("ghDiv"));
					myChart.setOption(resObj);
				}
			}
		});
	}
	//施工手续进展
	function querySg(nd)
	{
		var action1 = controllername + "?querySg&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result)
			{
				addTable(result,"sgtable");
			}
		});
	}
	function querySgZxtColumn2d(nd){
		var action = controllername + "?querySgZxtColumn2d&nd="+nd;
		$.ajax({
			url : action,
			dataType:"json",
			async:false,
			success: function(result){
				if(result.msg==0){
					return;
				}else{
					var resObj = convertJson.string2json1(result.msg);
					var myChart = p_ec.init(document.getElementById("sgDiv"));
					myChart.setOption(resObj);
				}
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
		var openWin;
		for(var i=0;i<len;i++){
			showHtml +="<tr>";
			var nr,bllx,sgflag="";
			$("#"+tableID+" tr th").each(function(j)
			{
				var subresultmsgobj = resultobj.response.data[i];
				var str = $(this).attr("bzfieldname");
				
					if(str=='MC') {
						showHtml+="<td class='mc'>"+$(subresultmsgobj).attr(str)+" </td>";
						nr= $(subresultmsgobj).attr(str);
					} else {
						var rs = $(subresultmsgobj).attr(str);
						if(str == 'YINGBANLI') {
							bllx = '2';
						}

						if(tableID == "tdtable") {
							sgflag = "";
						} else if(tableID == "sgtable") {
							sgflag = "1";
						} else if(tableID == "ghtable") {
							sgflag = "";
						} else if(tableID == "lxtable") {
							sgflag = "";
						}
						if(rs == "-") {
							showHtml+="<td style=\"text-align:center;\">"+rs+" </td>";
						} else if(rs == "0") {
							showHtml+="<td style=\"text-align:center;\">"+rs+" </td>";
						} else {
							showHtml+="<td style=\"text-align:center;\" col=\""+str+"\" sgflag=\""+sgflag+"\"><a href=\"javascript:void(0);\" onclick=\"openT(this)\">"+rs+"</as></td>";
						}
					}
			});
			showHtml+="</tr>";
		}
		$("#"+tableID).append(showHtml);
		$($("#"+tableID+" tbody>tr:even")).addClass("tableTrEvenColor");
		$("#"+tableID).addClass("tableList");
		$($("#"+tableID+" thead tr:eq(0)")).addClass("tableTheadTr");
	}
	

	function queryZtqk(nd) {
		var action1 = controllername + "?queryZtqk&nd="+nd;
		$.ajax({
			url:		action1,
			success:	function(result) {
				insertTable(result,"QQ_ZTQK");
			}
		});
	}
	//导视图1
	function queryDst1(nd){
		var action1 = controllername + "?queryQqsx_DST1&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result){
				insertTable(result,"QQSX_DST1");
			}
		});
	}
	//导视图2
	function queryDst2(nd){
		var action1 = controllername + "?queryQqsx_DST2&nd="+nd;
		$.ajax({
			url : action1,
			success: function(result){
				insertTable(result,"QQSX_DST2");
			}
		});
	}
	//根据结果放入表格
	function insertTable(result,tableId) {
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
								$(this).html('<a href="javascript:void(0);" onclick="openDetailJH(\'项目详细列表\',\'TCJh_DETAIL**GCB**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\'CBK_DETAIL**GCB**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'nohasLink' :
								var html=$(subresultmsgobj).attr(str);
								$(this).html(html);
								break;
							//导视图链接
							case 'XMJYS_YBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'YIBANLI\',\'\',\'立项批复\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'XMJYS_WBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'WEIBANLI\',\'\',\'立项批复\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'XMJYS_BXYBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'BUXUBANLI\',\'\',\'立项批复\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'KYPF_YBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'YIBANLI\',\'\',\'可研批复\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'KYPF_WBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'WEIBANLI\',\'\',\'可研批复\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'KYPF_BXYBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'BUXUBANLI\',\'\',\'可研批复\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'YDGHXKZ_YBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'YIBANLI\',\'\',\'用地规划许可证\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'YDGHXKZ_WBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'WEIBANLI\',\'\',\'用地规划许可证\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'YDGHXKZ_BXYBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'BUXUBANLI\',\'\',\'用地规划许可证\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'YDPZS_YBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'YIBANLI\',\'\',\'用地批准书\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'YDPZS_WBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'WEIBANLI\',\'\',\'用地批准书\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'YDPZS_BXYBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'BUXUBANLI\',\'\',\'用地批准书\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'JSGCGHXKZ_YBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'YIBANLI\',\'\',\'工程规划许可证\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'JSGCGHXKZ_WBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'WEIBANLI\',\'\',\'工程规划许可证\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'JSGCGHXKZ_BXYBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'BUXUBANLI\',\'\',\'工程规划许可证\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'HP_YBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'YIBANLI\',\'\',\'环评\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'TDYJH_YBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'YIBANLI\',\'\',\'土地意见函\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'XZYJS_YBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'YIBANLI\',\'\',\'选址意见书\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'JNSC_YBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'YIBANLI\',\'\',\'能评\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'SG_Custom_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'SGCustom\',\''+$(this).attr("bzfieldname")+'\',\'SGCustom\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'BJ_YBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'YIBANLI\',\'1\',\'报建\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'BJ_WBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'WEIBANLI\',\'1\',\'报建\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'ZB_YBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'YIBANLI\',\'\',\'招投标\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'ZB_WBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'WEIBANLI\',\'\',\'招投标\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'SGXK_YBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'YIBANLI\',\'\',\'施工许可证\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'SGXK_WBL_HasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'WEIBANLI\',\'\',\'施工许可证\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink_SGXK_YKG' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'YIBANLI\',\'\',\'施工许可证\',\'YKG\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink_SGXK_WKG' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'YIBANLI\',\'\',\'施工许可证\',\'WKG\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink_SGXKWG_YKG' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'WEIBANLI\',\'\',\'施工许可证\',\'YKG\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink_SGXKWG_WKG' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'WEIBANLI\',\'\',\'施工许可证\',\'WKG\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink_YKG' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'WEIBANLI\',\'4\',\'开工\',\'YKG\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink_WKG' :
								$(this).html('<a href="javascript:void(0);" onclick="isSX(\'WEIBANLI\',\'4\',\'开工\',\'WKG\')">'+$(subresultmsgobj).attr(str)+'</a>');
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
	function openT(obj) {
		var col = $(obj).closest("td").attr("col");
		var sgflag = $(obj).closest("td").attr("sgflag");
		var mc  = $(obj).closest("td").prevAll(".mc").text().trim();
		isSX(col,sgflag,mc);
	}
	function isSX(col,sgflag,mc,iskg){
		var sxbh,sxmc,bllx,viewInfo;
		// 应办理
		if(col == "YINGBANLI") {
			bllx = "2";
			// 已办理
		} else if(col == "YIBANLI") {
			bllx = "0";
			// 部分办理
		} else if(col == "BFBL") {
			bllx = "1";
			// 未办理
		} else if(col == "WEIBANLI") {
			bllx = "3";
			// 不需要办理
		} else if(col == "BUXUBANLI") {
			bllx = "4";
		}
		// 规划手续进展
		if(mc=="工程规划许可证") {
			sxbh='0009';
			sxmc='qq.GHSPBBLSX';
			viewInfo='ghsxList.jsp';
		} else if(mc=="用地规划许可证") {
			sxbh='0008';
			sxmc='qq.GHSPBBLSX';
			viewInfo='ghsxList.jsp';
		} else if(mc=="选址意见书") {
			sxbh='0007';
			sxmc='qq.GHSPBBLSX';
			viewInfo='ghsxList.jsp';
		} 
		// 施工许可手续进展
		else if(mc=="报建") {
			sxbh='0015';
			sxmc='qq.SGXKBBLSX';
			viewInfo = "sgxkbblsx.jsp";
		} else if(mc=="质量监督") {
			sxbh='0017';
			sxmc='qq.SGXKBBLSX';
			viewInfo = "sgxkbblsx.jsp";
		} else if(mc=="安全管理") {
			sxbh='0018';
			sxmc='qq.SGXKBBLSX';
			viewInfo = "sgxkbblsx.jsp";
		} else if(mc=="执法监督") {
			sxbh='0021';
			sxmc='qq.SGXKBBLSX';
			viewInfo = "sgxkbblsx.jsp";
		} else if(mc=="双拖欠") {
			sxbh='0020';
			sxmc='qq.SGXKBBLSX';
			viewInfo = "sgxkbblsx.jsp";
		} else if(mc=="其他") {
			sxbh='0016';
			sxmc='qq.SGXKBBLSX';
			viewInfo = "sgxkbblsx.jsp";
		} else if(mc=="施工许可证") {
			sxbh='0022';
			sxmc='qq.SGXKBBLSX';
			sgflag = "1";
			viewInfo = "sgxkbblsx.jsp";
		} else if(mc=="招投标") {
			sgflag = "3";
			viewInfo = "sgxkbblsx.jsp";
		} else if(mc=="施工图审查备案") {
			sgflag = "2";
			viewInfo = "sgxkbblsx.jsp";
		}else if(mc=="SGCustom"){
			sxbh = "customFlag";
			viewInfo = "sgxkbblsx.jsp";
		} else if(mc=="开工") {
			sgflag = "4";
			viewInfo = "sgxkbblsx.jsp";
		} 
		// 土地手续进展
		else if(mc=="用地批准书") {
			sxbh='0023';
			sxmc='qq.TDSPBBLSX';
			viewInfo='tdsxList.jsp';
		} else if(mc=="划拨决定书") {
			sxbh='0025';
			sxmc='qq.TDSPBBLSX';
			viewInfo='tdsxList.jsp';
		} else if(mc=="登记发证") {
			sxbh='0014';
			sxmc='qq.TDSPBBLSX';
			viewInfo='tdsxList.jsp';
		} 
		// 立项可研进展整体状况
		else if(mc=="立项批复") {
			sxbh='2020';
			sxmc='qq.LXKYBBLSX';
			viewInfo='lxkyList.jsp';
		} else if(mc=="能评") {
			sxbh='2023';
			sxmc='qq.LXKYBBLSX';
			viewInfo='lxkyList.jsp';
		} else if(mc=="环评") {
			sxbh='2021';
			sxmc='qq.LXKYBBLSX';
			viewInfo='lxkyList.jsp';
		} else if(mc=="选址意见书") {
			sxbh='0007';
			sxmc='qq.GHSPBBLSX';
			viewInfo='lxkyList.jsp';
		} else if(mc=="土地意见函") {
			sxbh='2022';
			sxmc='qq.LXKYBBLSX';
			viewInfo='lxkyList.jsp';
		} else if(mc=="可研批复") {
			sxbh='2024';
			sxmc='qq.LXKYBBLSX';
			viewInfo='lxkyList.jsp';
		} else if(mc=="") {
			
		}
//		alert(sxmc+"|"+sxbh+"|"+mc +"|"+bllx);
		var nd = $("#ND").val();
		sgflag = sgflag == "" ? "" : "&sgflag="+sgflag;
		//判断是否有开工条件
		if(iskg==undefined||iskg==""){
			iskg="";
		}else{
			iskg = "&iskg="+iskg;
		}
		var  xmsc = {"title":'项目详细列表',"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/"+viewInfo+"?flag=1"+iskg+""+sgflag+"&nd="+nd+"&sxbh="+sxbh+"&sxmc="+sxmc+"&bllx="+bllx,"modal":"1"};
		$(window).manhuaDialog(xmsc);
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
	
</script>
</head>
<body>
<div class="container-fluid" style="min-width:1024px;">
           <span class="pull-right">
    	<button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
    </span>
     <div class="" style="border:0px;">
				<h4 class="bmjkTitleLine">整体情况&nbsp;&nbsp;
						<select
							class="span2 year" style="width: 8%" id="ND" onchange="queryND()"
							name="ND" fieldname="ND" operation="=" noNullSelect ="true"  kind="dic" 
							src="T#GC_JH_SJ: distinct ND as NDCODE:ND:SFYX='1' ORDER BY NDCODE asc">
						</select>
				</h4>
				<div class="container-fluid" style="padding-left:0px;padding-right:0px;">
         <div class="row-fluid">
                <div class="span12 QQ_ZTQK">
              		<div class="bmjkTjgkBlock">
						<p><span class="ndText"></span>年建管中心共实施<span flag="hasLink_jgzxfz_dx" bzfieldname="DX"></span>项
						（<span flag="hasLink_jgzxfz_zx" bzfieldname="ZX"></span>个子项）重点工程，
						总投资<span flag="nohasLink" bzfieldname="ZTZ"></span>亿元，
						年度计划完成投资<span flag="nohasLink" bzfieldname="JHWCTZ"></span>亿元。
						</p>
					</div>
                </div>
              </div>
        </div>
    </div>
    <div class="" style="border:0px;">
        <div class="container-fluid">
            <div class="row-fluid">
              <div class="span7">
		       <table width="100%" border="0" align="center"   cellpadding="0" cellspacing="0" class="">
				    <tr>
				        <td width="100%"><table width="100%" border="0" cellpadding="0" id="sxtable" cellspacing="0" class="table3">
				            <thead><tr  >
				                <th  bzfieldname="MC"><div style="text-align: center;">办理内容</div></th>
				                <th  bzfieldname="YINGBANLI"><div style="text-align: center;">应办理</div></th>
				                <th  bzfieldname="YIBANLI"><div style="text-align: center;">已办理</div></th>
				                <th  bzfieldname="BFBL"><div style="text-align: center;">部分办理</div></th>
				                <th  bzfieldname="WEIBANLI"><div style="text-align: center;">未办理</div></th>
				                <th  bzfieldname="BUXUBANLI"><div style="text-align: center;">不需办理</div></th>
				            </tr></thead>
				        </table></td>
				    </tr>
				</table>
		      </div>
				<div class="span5" style="margin-left:0px;">
					<div id="sxDiv" style="height:300px;min-width:518px;"></div>
				</div>
				<div class="span12 QQSX_DST1" style="margin-left:0px;min-width:960px;">
					<table border="0" class="stageTable" style="width:100%;">
						<tr>
							<td rowspan=12 class="contentTD">
								<div class="block-blue">
									年度统筹计划<br/>
									<span class="fieldValue-A" flag="hasLink_jgzxfz_zx" bzfieldname="NDTCJH"></span>项
								</div>
							</td>
							<td rowspan=4 class="blankTD">
								<div class="containerDiv-upToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:66px"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td rowspan=4 class="contentTD">
								<div class="block-blue">
									已办项目建议书<br/>
									<span class="fieldValue-A" flag="XMJYS_YBL_HasLink" bzfieldname="XMJYSYIBANLI"></span>项
								</div>
							</td>
							<td rowspan=3 class="line-H blankTD">
								<div class="containerDiv-upToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:66px"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							  	<div class="div-H" style="width:50%;height:33%;"></div>
							</td>
							<td rowspan=3 class="contentTD">
								<div class="block-blue">
									环评报告<br/>
									<span class="fieldValue-A" flag="HP_YBL_HasLink" bzfieldname="HPYIBANLI"></span>项
								</div>
							</td>
							<td rowspan=3 class="blankTD line-S" style="height: 69px;">
								<div class="containerDiv-rightToDown">
							    	<div class="lineDiv">
							      		<div class="line" style="padding-left:45%;height:99px;top:50%;"></div>
							    	</div>
							  	</div>
							</td>
							<td rowspan=4 class="blankTD">
								<div class="containerDiv-leftToRight">
							    	<div class="lineDiv">
							      		<div class="line"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td rowspan=4 class="contentTD">
								<div class="block-blue">
									已完可研批复<br/>
									<span class="fieldValue-A" flag="KYPF_YBL_HasLink" bzfieldname="KYPFYIBANLI"></span>项
								</div>
							</td>
							<td rowspan=4 class="blankTD line-S">
								<div class="containerDiv-leftToRight">
							    	<div class="lineDiv">
							      		<div class="line"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							  	<div class="div-S" style="height:50%;top:49%;"></div>
							</td>
							<td rowspan=4 class="contentTD">
								<div class="block-blue">
									已办建设用地规划许可证<br/>
									<span class="fieldValue-A" flag="YDGHXKZ_YBL_HasLink" bzfieldname="JSYDYIBANLI"></span>项
								</div>
							</td>
							<td rowspan=4 class="blankTD line-S">
								<div class="containerDiv-leftToRight">
							    	<div class="lineDiv">
							      		<div class="line"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							  	<div class="div-S" style="height:50%;top:49%;"></div>
							</td>
							<td rowspan=4 class="contentTD">
								<div class="block-blue">
									已办用地批准书<br/>
									<span class="fieldValue-A"  flag="YDPZS_YBL_HasLink" bzfieldname="YDPZSYIBANLI"></span>项
								</div>
							</td>
							<td rowspan=4 class="blankTD line-S">
								<div class="containerDiv-leftToRight">
							    	<div class="lineDiv">
							      		<div class="line"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							  	<div class="div-S" style="height:50%;top:49%;"></div>
							</td>
							<td rowspan=4 class="contentTD">
								<div class="block-blue">
									已办建设工程规划许可证<br/>
									<span class="fieldValue-A" flag="JSGCGHXKZ_YBL_HasLink" bzfieldname="JSGCYIBANLI"></span>项
								</div>
							</td>
							<td style="width:1px;">
								
							</td>
						</tr>
						<tr>
							<td>
								
							</td>
						</tr>
						<tr>
							<td>
								
							</td>
						</tr>
						<tr>
							<td rowspan=3 class="blankTD">
								<div class="containerDiv-downToRight">
								<div class="lineDiv">
									<div class="line" style="height:66px;top:-66px;"></div>
								</div>
								<div class="triangle-right"></div>
							</td>
							<td rowspan=3 class="contentTD">
								<div class="block-blue">
									土地意见函<br/>
									<span class="fieldValue-A" flag="TDYJH_YBL_HasLink" bzfieldname="TDYIBANLI"></span>项
								</div>
							</td>
							<td rowspan=3 class="blankTD" style="height: 69px;">
								<div class="containerDiv-rightToDown">
							    	<div class="lineDiv">
							      		<div class="line" style="padding-left:45%;height:138px;top:50%;"></div>
							    	</div>
							  	</div>
							</td>
							<td>
								
							</td>
						</tr>
						<tr>
							<td rowspan=4 class="blankTD line-S">
							<div class="containerDiv-leftToRight">
								<div class="lineDiv">
									<div class="line"></div>
								</div>
								<div class="triangle-right"></div>
								</div>
								<div class="div-S" style="height:100%;"></div>
							</td>
							<td rowspan=4>
								<div class="block-red">
									未办项目建议书<br/>
									<span class="fieldValue-A" flag="XMJYS_WBL_HasLink" bzfieldname="XMJYSWEIBANLI"></span>项
								</div>
							</td>
							<td rowspan=4 class="blankTD">
								<div class="containerDiv-leftToRight">
							    	<div class="lineDiv">
							      		<div class="line"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td rowspan=4 class="contentTD">
								<div class="block-red">
									未完可研批复<br/>
									<span class="fieldValue-A" flag="KYPF_WBL_HasLink" bzfieldname="KYPFYWEIBANLI"></span>项
								</div>
							</td>
							<td rowspan=4 class="blankTD">
								<div class="containerDiv-downToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:60px;top:-60px;"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td rowspan=4 class="contentTD">
								<div class="block-red">
									未办建设用地规划许可证<br/>
									<span class="fieldValue-A" flag="YDGHXKZ_WBL_HasLink" bzfieldname="JSYDWEIBANLI"></span>项
								</div>
							</td>
							<td rowspan=4 class="blankTD">
								<div class="containerDiv-downToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:60px;top:-60px;"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td rowspan=4 class="contentTD">
								<div class="block-red">
									未办用地批准书<br/>
									<span class="fieldValue-A" flag="YDPZS_WBL_HasLink" bzfieldname="YDPZSWEIBANLI"></span>项
								</div>
							</td>
							<td rowspan=4 class="blankTD">
								<div class="containerDiv-downToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:60px;top:-60px;"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td rowspan=4 class="contentTD">
								<div class="block-red">
									未办建设工程规划许可证<br/>
									<span class="fieldValue-A" flag="JSGCGHXKZ_WBL_HasLink" bzfieldname="JSGCWEIBANLI"></span>项
								</div>
							</td>
							<td>
								
							</td>	
						</tr>
						<tr>
							<td>
								
							</td>
						</tr>
						<tr>
							<td rowspan=3 class="blankTD">
								<div class="containerDiv-downToRight">
								<div class="lineDiv">
									<div class="line" style="height:88px;top:-88px;"></div>
								</div>
								<div class="triangle-right"></div>
							</td>
							<td rowspan=3 class="contentTD">
								<div class="block-blue">
									选址意见书<br/>
									<span class="fieldValue-A" flag="XZYJS_YBL_HasLink" bzfieldname="XZYJSYIBANLI"></span>项
								</div>
							</td>
							<td rowspan=3 class="blankTD line-S" style="height: 69px;">
								<div class="containerDiv-rightToDown">
									<div class="lineDiv">
										<div class="line"
											style="padding-left:45%; height:29px; bottom:0px;"></div>
									</div>
								</div>
								<div class="div-S"
									style="height:80px;border-right: 4px solid RGB(135,142,141);width: 45%;top:-10px;left: 45%;border-left: 0px;margin-left: 1px;"></div>
							</td>
							<td>
								
							</td>
						</tr>
						<tr>
							<td>
								
							</td>	
						</tr>
						<tr>
							<td rowspan=4 class="blankTD">
								<div class="containerDiv-downToRight">
									<div class="lineDiv">
										<div class="line" style="height:66px;top:-66px;"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
							</td>
							<td rowspan=4 class="contentTD">
								<div class="block-gray">
									不需办项目建议书<br/>
									<span class="fieldValue-A" flag="XMJYS_BXYBL_HasLink" bzfieldname="XMJYSBUXUBANLI"></span>项
								</div>
							</td>
							<td rowspan=4 class="blankTD">
								<div class="containerDiv-leftToRight">
							    	<div class="lineDiv">
							      		<div class="line"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td rowspan=4 class="contentTD">
								<div class="block-gray">
									不需办可研批复<br/>
									<span class="fieldValue-A" flag="KYPF_BXYBL_HasLink" bzfieldname="KYPFYBUXUBANLI"></span>项
								</div>
							</td>
							<td rowspan=4 class="blankTD">
								<div class="containerDiv-downToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:110px;top:-110px;"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td rowspan=4 class="contentTD">
								<div class="block-gray">
									不需办建设用地规划许可证<br/>
									<span class="fieldValue-A" flag="YDGHXKZ_BXYBL_HasLink" bzfieldname="JSYDBUXUBANLI"></span>项
								</div>
							</td>
							<td rowspan=4 class="blankTD">
								<div class="containerDiv-downToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:110px;top:-110px;"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td rowspan=4 class="contentTD">
								<div class="block-gray">
									不需办用地批准书<br/>
									<span class="fieldValue-A" flag="YDPZS_BXYBL_HasLink" bzfieldname="YDPZSBUXUBANLI"></span>项
								</div>
							</td>
							<td rowspan=4 class="blankTD">
								<div class="containerDiv-downToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:110px;top:-110px;"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td rowspan=4 class="contentTD">
								<div class="block-gray">
									不需办建设工程规划许可证<br/>
									<span class="fieldValue-A"  flag="JSGCGHXKZ_BXYBL_HasLink" bzfieldname="JSGCBUXUBANLI"></span>项
								</div>
							</td>
							<td>
							</td>	
						</tr>
						<tr>
							<td rowspan=3 class="blankTD">
								<div class="containerDiv-downToRight">
								<div class="lineDiv">
									<div class="line" style="height:66px;top:-66px;"></div>
								</div>
								<div class="triangle-right"></div>
							</td>
							<td rowspan=3 class="contentTD">
								<div class="block-blue">
									节能审查<br/>
									<span class="fieldValue-A" flag="JNSC_YBL_HasLink" bzfieldname="NPYIBANLI"></span>项
								</div>
							</td>
							<td rowspan=3 class="blankTD" style="height: 69px;">
								<div class="containerDiv-rightToUp">
							    	<div class="lineDiv">
							      		<div class="line" style="padding-left:45%;left:1px;"></div>
							    	</div>
							  	</div>
							</td>
							<td>
							</td>
						</tr>
						<tr>
							<td>
							</td>
						</tr>
						<tr>
							<td>
							</td>
						</tr>
					</table>
				</div>
				<div class="span12 QQSX_DST2" style="margin-left:0px;min-width:960px;">
					<table border="0" class="stageTable" style="width:100%;">
						<tr>
							<td rowspan=9 class="contentTD">
								<div class="block-blue">
									年度统筹计划<br/>
									<span class="fieldValue-A" flag="hasLink_jgzxfz_zx" bzfieldname="NDTCJH"></span>项
								</div>
							</td>
							<td colspan=10>
							</td>
							<td class="blankTD">
								<div class="containerDiv-upToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:40px"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td class="contentTD">
								<div class="block-blue">
									已开工<br/>
									<span class="fieldValue-A" flag="hasLink_SGXK_YKG" bzfieldname="SGXK_YKG"></span>项
								</div>
							</td>
							<td>
							</td>
						</tr>
						<tr>
							<td class="blankTD">
								<div class="containerDiv-upToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:66px"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td class="contentTD">
								<div class="block-blue">
									已报建<br/>
									<span class="fieldValue-A" flag="BJ_YBL_HasLink"   bzfieldname="BJYIBANLI"></span>项
								</div>
							</td>
							<td class="blankTD line-S">
								<div class="containerDiv-leftToRight">
							    	<div class="lineDiv">
							      		<div class="line"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							  	<div class="div-S" style="height:50%;top:49%;"></div>
							</td>
							<td class="contentTD">
								<div class="block-blue">
									已招标<br/>
									<span class="fieldValue-A" flag="ZB_YBL_HasLink" bzfieldname="ZBYIBANLI"></span>项
								</div>
							</td>
							<td class="blankTD line-S">
								<div class="containerDiv-leftToRight">
							    	<div class="lineDiv">
							      		<div class="line"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							  	<div class="div-S" style="height:50%;top:49%;"></div>
							</td>
							<td class="contentTD">
								<div class="block-blue">
									已办七项审批<br/>
									<span flag="SG_Custom_HasLink" class="fieldValue-A" bzfieldname="YBSPQX"></span>项
								</div>
							</td>
							<td class="blankTD line-S">
								<div class="containerDiv-leftToRight">
							    	<div class="lineDiv">
							      		<div class="line"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							  	<div class="div-S" style="height:50%;top:49%;"></div>
							</td>
							<td class="contentTD">
								<div class="block-blue">
									已办建设工程规划许可证<br/>
									<span class="fieldValue-A" flag="JSGCGHXKZ_YBL_HasLink" bzfieldname="JSGCYIBANLI"></span>项
								</div>
							</td>
							<td class="blankTD line-S">
								<div class="containerDiv-leftToRight">
							    	<div class="lineDiv">
							      		<div class="line"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							  	<div class="div-S" style="height:50%;top:49%;"></div>
							</td>
							<td class="contentTD">
								<div class="block-blue">
									已办施工许可<br/>
									<span class="fieldValue-A" flag="SGXK_YBL_HasLink"  bzfieldname="SGXKYIBANLI"></span>项
								</div>
							</td>
							<td class="blankTD line-S">
								<div class="containerDiv-leftToRight">
							    	<div class="lineDiv">
							      		<div class="line"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							  	<div class="div-S" style="height:50%;top:0px;"></div>
							</td>
							<td class="contentTD">
								<div class="block-red">
									未开工<br/>
									<span class="fieldValue-A" flag="hasLink_SGXK_WKG" bzfieldname="SGXK_WKG"></span>项
								</div>
							</td>
							<td>
							</td>
						</tr>
						<tr>
							<td class="blankTD">
								<div class="containerDiv-upToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:66px"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td class="contentTD">
								<div class="block-red">
									未报建<br/>
									<span class="fieldValue-A" flag="BJ_WBL_HasLink" bzfieldname="BJWEIBANLI"></span>项
								</div>
							</td>
							<td class="blankTD">
								<div class="containerDiv-downToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:50px;top:-50px;"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td class="contentTD">
								<div class="block-red">
									未招标<br/>
									<span class="fieldValue-A" flag="ZB_WBL_HasLink" bzfieldname="ZBWEIBANLI"></span>项
								</div>
							</td>
							<td class="blankTD">
								<div class="containerDiv-downToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:50px;top:-50px;"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td class="contentTD">
								<div class="block-red">
									未办七项审批<br/>
									<span flag="SG_Custom_HasLink" class="fieldValue-A" bzfieldname="WBQXSP"></span>项
								</div>
							</td>
							<td class="blankTD">
								<div class="containerDiv-downToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:50px;top:-50px;"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td class="contentTD">
								<div class="block-red">
									未办建设工程规划许可证<br/>
									<span class="fieldValue-A"  flag="JSGCGHXKZ_WBL_HasLink" bzfieldname="JSGCWEIBANLI"></span>项
								</div>
							</td>
							<td class="blankTD">
								<div class="containerDiv-downToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:50px;top:-50px;"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td class="contentTD">
								<div class="block-red">
									未办施工许可证<br/>
									<span class="fieldValue-A" flag="SGXK_WBL_HasLink" bzfieldname="SGXKWEIBANLI"></span>项
								</div>
							</td>
							<td class="blankTD line-S">
								<div class="containerDiv-leftToRight">
							    	<div class="lineDiv">
							      		<div class="line"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							  	<div class="div-S" style="height:50%;top:49%;"></div>
							</td>
							<td class="contentTD">
								<div class="block-blue">
									已开工<br/>
									<span class="fieldValue-A" flag="hasLink_SGXKWG_YKG" bzfieldname="SGXKWG_YKG"></span>项
								</div>
							</td>
							<td>
							</td>
						</tr>
						<tr>
							<td class="blankTD line-S" rowspan=2>
								<div class="containerDiv-leftToRight">
									<div class="lineDiv">
										<div class="line"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
								<div class="div-S" style="height:100%;"></div>
							</td>
							<td class="contentTD" rowspan=2>
								<div class="block-blue">
									已开工<br/>
									<span class="fieldValue-A" flag="hasLink_YKG" bzfieldname="YKG"></span>项
								</div>
							</td>
							<td class="blankTD line-H">
								<div class="containerDiv-upToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:40px"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							  	<div class="div-H" style="width:50%;bottom:-1px;height:0px;"></div>
							</td>
							<td class="contentTD">
								<div class="block-blue">
									已办施工许可证<br/>
									<span class="fieldValue-A" flag="hasLink_SGXK_YKG" bzfieldname="SGXK_YKG"></span>项
								</div>
							</td>
							<td colspan=6>
							</td>
							<td class="blankTD">
								<div class="containerDiv-downToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:50px;top:-50px;"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							</td>
							<td class="contentTD">
								<div class="block-red">
									未开工<br/>
									<span class="fieldValue-A" flag="hasLink_SGXKWG_WKG" bzfieldname="SGXKWG_WKG"></span>项
								</div>
							</td>
							<td>
							</td>
						</tr>
						<tr>
							<td class="blankTD">
								<div class="containerDiv-downToRight">
									<div class="lineDiv">
										<div class="line" style="height:40px;top:-40px;"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
							</td>
							<td class="contentTD">
								<div class="block-red">
									未办施工许可证<br/>
									<span class="fieldValue-A"  flag="hasLink_SGXKWG_YKG" bzfieldname="SGXKWG_YKG"></span>项
								</div>
							</td>
							<td colspan=8>
							</td>
							<td>
							</td>
						</tr>
						<tr>
							<td class="blankTD" rowspan=2>
								<div class="containerDiv-downToRight">
									<div class="lineDiv">
										<div class="line" style="height:60px;top:-60px;"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
							</td>
							<td class="contentTD" rowspan=2>
								<div class="block-red">
									未开工<br/>
									<span class="fieldValue-A" flag="hasLink_WKG"  bzfieldname="WKG"></span>项
								</div>
							</td>
							<td class="blankTD line-H">
								<div class="containerDiv-upToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:40px"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							  	<div class="div-H" style="width:50%;bottom:-1px;height:0px;"></div>
							</td>
							<td class="contentTD">
								<div class="block-blue">
									已办施工许可证<br/>
									<span class="fieldValue-A" flag="hasLink_SGXK_WKG" bzfieldname="SGXK_WKG"></span>项
								</div>
							</td>
							<td colspan=8>
							</td>
							<td>
							</td>
						</tr>
						<tr>
							<td class="blankTD">
								<div class="containerDiv-downToRight">
									<div class="lineDiv">
										<div class="line" style="height:40px;top:-40px;"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
							</td>
							<td class="contentTD">
								<div class="block-red">
									未办施工许可证<br/>
									<span class="fieldValue-A" flag="hasLink_SGXKWG_WKG" bzfieldname="SGXKWG_WKG"></span>项
								</div>
							</td>
							<td colspan=8>
							</td>
							<td>
							</td>
						</tr>
						<tr>
							<td class="blankTD" rowspan=2>
								<div class="containerDiv-downToRight">
									<div class="lineDiv">
										<div class="line" style="height:110px;top:-110px;"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
							</td>
							<td class="contentTD" rowspan=2>
								<div class="block-gray">
									暂缓开工<br/>
									<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_JGZXFZ_ZX">XX</span>项
								</div>
							</td>
							<td class="blankTD line-H">
								<div class="containerDiv-upToRight">
							    	<div class="lineDiv">
							      		<div class="line" style="height:40px"></div>
							    	</div>
							    	<div class="triangle-right"></div>
							  	</div>
							  	<div class="div-H" style="width:50%;bottom:-1px;height:0px;"></div>
							</td>
							<td class="contentTD">
								<div class="block-blue">
									已办施工许可证<br/>
									<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_JGZXFZ_ZX">XX</span>项
								</div>
							</td>
							<td colspan=8>
							</td>
							<td>
							</td>
						</tr>
						<tr>
							<td class="blankTD">
								<div class="containerDiv-downToRight">
									<div class="lineDiv">
										<div class="line" style="height:40px;top:-40px;"></div>
									</div>
									<div class="triangle-right"></div>
								</div>
							</td>
							<td class="contentTD">
								<div class="block-red">
									未办施工许可证<br/>
									<span class="fieldValue-A" bzfieldname="GCGL_KFGGL_JGZXFZ_ZX">XX</span>项
								</div>
							</td>
							<td colspan=8>
							</td>
							<td>
							</td>
						</tr>
					</table>
				</div>
            </div>
        </div>
		<h4 class="bmjkTitleLine">各手续办理情况</h4>
        <div class="container-fluid">
            <div class="row-fluid">
              <div class="span7">
              <h4  align="left" style="background:none; color:#333;">立项可研手续进展</h4>
		       <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="">
				    <tr>
				        <td width="100%"><table width="100%" border="0" cellpadding="0" id="lxtable" cellspacing="0" class="table3">
				            <thead>
				            <tr  >
				                <th  bzfieldname="MC"><div style="text-align: center;">办理内容</div></th>
				                <th  bzfieldname="YINGBANLI"><div style="text-align: center;">应办理</div></th>
				                <th  bzfieldname="YIBANLI"><div style="text-align: center;">已办理</div></th>
				                <th  bzfieldname="BFBL"><div style="text-align: center;">部分办理</div></th>
				                <th  bzfieldname="WEIBANLI"><div style="text-align: center;">未办理</div></th>
				                <th  bzfieldname="BUXUBANLI"><div style="text-align: center;">不需办理</div></th>
				            </tr></thead>
				        </table></td>
				    </tr>
				</table>
		      </div>
		      <div class="span5" style="margin-left:0px;">
					<div id="lxDiv" style="height:300px;min-width:518px;"></div>
				</div>
            </div>
        </div>
          <div class="container-fluid">
            <div class="row-fluid">
              <div class="span7">
              <h4  align="left" style="background:none; color:#333;">土地手续进展</h4>
		       <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="">
				    <tr>
				        <td width="100%"><table width="100%" border="0" cellpadding="0" id="tdtable" cellspacing="0" class="table3">
				            <thead><tr  >
				                <th  bzfieldname="MC"><div style="text-align: center;">办理内容</div></th>
				                <th  bzfieldname="YINGBANLI"><div style="text-align: center;">应办理</div></th>
				                <th  bzfieldname="YIBANLI"><div style="text-align: center;">已办理</div></th>
				                <th  bzfieldname="BFBL"><div style="text-align: center;">部分办理</div></th>
				                <th  bzfieldname="WEIBANLI"><div style="text-align: center;">未办理</div></th>
				                <th  bzfieldname="BUXUBANLI"><div style="text-align: center;">不需办理</div></th>
				            </tr></thead>
				        </table></td>
				    </tr>
				</table>
		      </div>
		      <div class="span5" style="margin-left:0px;">
					<div id="tdDiv" style="height:200px;min-width:518px;"></div>
				</div>
            </div>
        </div>
          <div class="container-fluid">
            <div class="row-fluid">
              <div class="span7">
              <h4  align="left" style="background:none; color:#333;">规划手续进展</h4>
		       <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="">
				    <tr>
				        <td width="100%"><table width="100%" border="0" cellpadding="0" id="ghtable" cellspacing="0" class="table3">
				            <thead><tr  >
				                <th  bzfieldname="MC"><div style="text-align: center;">办理内容</div></th>
				                <th  bzfieldname="YINGBANLI"><div style="text-align: center;">应办理</div></th>
				                <th  bzfieldname="YIBANLI"><div style="text-align: center;">已办理</div></th>
				                <th  bzfieldname="BFBL"><div style="text-align: center;">部分办理</div></th>
				                <th  bzfieldname="WEIBANLI"><div style="text-align: center;">未办理</div></th>
				                <th  bzfieldname="BUXUBANLI"><div style="text-align: center;">不需办理</div></th>
				            </tr></thead>
				        </table></td>
				    </tr>
				</table>
		      </div>
		      <div class="span5" style="margin-left:0px;">
					<div id="ghDiv" style="height:200px;min-width:518px;"></div>
				</div>
            </div>
        </div>
          <div class="container-fluid">
            <div class="row-fluid">
              <div class="span7">
              <h4  align="left" style="background:none; color:#333;">施工许可手续进展</h4>
		       <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="">
				    <tr>
				        <td width="100%"><table width="100%" border="0" cellpadding="0" id="sgtable" cellspacing="0" class="table3">
				            <thead><tr  >
				                <th  bzfieldname="MC"><div style="text-align: center;">办理内容</div></th>
				                <th  bzfieldname="YINGBANLI"><div style="text-align: center;">应办理</div></th>
				                <th  bzfieldname="YIBANLI"><div style="text-align: center;">已办理</div></th>
				                <th  bzfieldname="BFBL"><div style="text-align: center;">部分办理</div></th>
				                <th  bzfieldname="WEIBANLI"><div style="text-align: center;">未办理</div></th>
				                <th  bzfieldname="BUXUBANLI"><div style="text-align: center;">不需办理</div></th>
				            </tr></thead>
				        </table></td>
				    </tr>
				</table>
		      </div>
			<div class="span5" style="margin-top:30px;margin-left:0px;">
				<div id="sgDiv" style="height:350px;min-width:518px;"></div>
			</div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
		// 使用
		require(
		    [
		        'echarts',
		        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
		    ],
		    function (ec) {
		    	p_ec = ec;
		    	setDefaultNd();
		    	doInit();
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