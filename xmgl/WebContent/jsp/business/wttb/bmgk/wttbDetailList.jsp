<!DOCTYPE html>
<html>
	<head>
		<title>问题监控</title>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ page import="java.util.*" %>
		<%@ page import="com.ccthanking.framework.util.*" %>
		<%@ page import="com.ccthanking.business.wttb.bmgk.*" %>
		<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
		<%@ taglib uri= "/tld/base.tld" prefix="app"%>
		<%
			String fieldname = request.getParameter("fieldname")==null?"":request.getParameter("fieldname");
			List<Map<String, String>> wtList = Tj_Wttb.getWttbData(fieldname);
			Map<String,List> wtxz = TjUtil.getDataGroupBy(wtList,"WTXZ");
		%>
		<app:base/>
		<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/css/bmgk/bmgk.css?version=4">
		<style>
		.sxtj {
			margin-left: 0px;
			margin-top: 5px;
			font-size: 9pt;
		}
		
		.sxtj span.contentSpan {
			cursor: pointer;
		}
		
		.firstspan {
			padding: 0px 7px 0px 15px;
		}
		
		.sxtjLast {
			margin-left: 20px;
			margin-top: 5px;
			font-size: 9pt;
		}
		
		
		span.title {
			font-weight: bold;
			color: black;
		}
		
		span.select {
			background-color: #0077CC;
			color: white;
			border-radius: 3px;
		}
		
		.hTitle {
			background: #F0F0F0;
			color: #333;
			height: 20px;
			padding: 5px 0px 5px 5px;
			margin: 2px 0px 0px 0px;
			font-size: 13pt;
			padding-left: 20px;
		}
		
		.linkTx {
			margin-left: 40px;
			font-size: 10pt;
			font-size: 9pt;
		}
		
		.lineTop {
			border-top: 2px dashed #ccc;
		}
		
		.lineBottom {
			border-bottom: 2px dashed #ccc;
		}
		.contentSpan{
			margin:0px 0px 0px 10px;
		}
		.checkedSpan{
			background-color:#0077CC;
			color:#FFF;
		}
		.linkSpan{
			padding:1px 1px 1px 1px;
			cursor: pointer;
			background:#FFFF00;
			color:#000;
			font-weight: normal;
		}
		.linkSpan a{
			color:#000;
		}
		ul{list-style-type:none;}
		.starDiv{position:relative;width:100%;}
		.starDiv ul,.starDiv span{float:left;display:inline;height:19px;line-height:19px;width:72px;}
		.starDiv ul{margin:0 0px;}
		.starDiv li{float:left;width:24px;cursor:pointer;text-indent:-9999px;background:url(${pageContext.request.contextPath }/images/star.png) no-repeat;}
		.starDiv strong{color:#f60;padding-left:10px;}
		.starDiv li.on{background-position:0 -28px;}
		.starDiv p{position:absolute;top:20px;width:159px;height:60px;display:none;background:url(${pageContext.request.contextPath }/images/icon.gif) no-repeat;padding:7px 10px 0;}
		.starDiv p em{color:#f60;display:block;font-style:normal;}
		</style>
		<script type="text/javascript">
			var controllername= "${pageContext.request.contextPath }/wttb/WttbBmgkController.do"; 
			var controllername2= "${pageContext.request.contextPath }/wttb/wttbController.do";
			var p_fieldname = '<%=fieldname%>';
			var tableTitleContent = '';//表格的标题
			$(function(){
				//setDefaultNd();
				doInit();
				var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
				showAutoComplete("QXMMC",autocompleteXmmcController+"?xmmcAutoQuery","getXmmcQueryCondition");
				$("#ND").change(function() {
					doInit();
					var sysdate = new Date();
					if($("#ND").val()==sysdate.getFullYear()){
						$("span[name='head']").html("截止"+getCurrentDate("yyyy年MM月DD日")+"，");
					}else{
						$("span[name='head']").html("");
					}
				});
				$("#btnQuery").click(function() {
					queryInfoTable();
				});
				//按钮绑定事件（清空查询条件）
				$("#btnClear").click(function() {
					$(".contentSpan").each(function(){
						$(this).removeClass("select");
					});
					$("#queryForm").clearFormResult();
				});
				$(".contentSpan").click(function(){
					$(".contentSpan").each(function(){
						$(this).removeClass("select");
					});
					$(this).addClass("select");
					var fieldname = $(this).attr("fieldname");
					var fieldvalue = $(this).attr("fieldvalue");
					var operation = $(this).attr("operation");
					var fieldtype = $(this).attr("fieldtype")==undefined?"text":$(this).attr("fieldtype");
					$("#common").attr("fieldname",fieldname);
					$("#common").val(fieldvalue);
					$("#common").attr("operation",operation);
					$("#common").attr("FIELDTYPE",fieldtype);
					queryInfoTable();
				});
				//数字加点击事件
			 	$(".linkSpan").click(function(){
			 		$(this).prev().click();
				});
				//按钮绑定事件（导出）
				$("#btnExp").click(function() {
					if(exportRequireQuery($("#tabList"))){//该方法需传入表格的jquery对象
						printTabList("tabList","wttbjkxx.xls","WTLX,WTBT,SJZT,SJSJ,LRBM,JSR,CQBZ,XMMC,BDMC,LRR,LRSJ,CBCS,HFQK,MYCD","1,1");
					}
				});
			});
			function doInit(){
				if(p_fieldname!=""){
					if(p_fieldname.indexOf("@")!=-1){
						var fieldnameArr = p_fieldname.split("@");
						for(var i=0;i<fieldnameArr.length;i++){
							var oneFieldname = fieldnameArr[i];
							setDefaultSelect(oneFieldname);
						}
					}else{
						setDefaultSelect(p_fieldname);
					}
					$("#tableTitle").html(tableTitleContent);
				}else{
					$("#tableTitle").html("问题总数 ");
				}
			 	//隐藏数据为0的项
			 	$(".linkSpan").each(function(){
					if($(this).text()=="(0)"){
						$(this).prev("span").hide();
						$(this).hide();
						$(this).next("span").hide();
					}
				});
				//隐藏所有竖线
				$(".contentLine").hide();
				$(".containerDiv").each(function(){
					if($(this).attr("id")!=undefined){
						daleixianshi($(this).attr("id"));
					}
				});
				queryInfoTable();
			}
			//判断子类是否都为0 为0隐藏大类
			function daleixianshi(id){
				var biaozhi=true;
				$($("#"+id+" .linkSpan")).each(function(){
					if($(this).text()!="(0)"){
						biaozhi=false;
					}
				});
				if(biaozhi){
					id=id.toUpperCase();
					$("."+id+"").hide();
				}   
			}
			function setDefaultSelect(n){
				var fieldtext = n.substring(0,n.indexOf("_"));
				$("#"+n).addClass("select");
				var fieldname = $("#"+n).attr("fieldname");
				var fieldvalue = $("#"+n).attr("fieldvalue");
				var operation = $("#"+n).attr("operation");
				var fieldtype = $("#"+n).attr("fieldtype")==undefined?"text":$("#"+n).attr("fieldtype");
				$("#Q_"+fieldtext).attr("fieldname",fieldname);
				$("#Q_"+fieldtext).val(fieldvalue);
				$("#Q_"+fieldtext).attr("operation",operation);
				$("#Q_"+fieldtext).attr("FIELDTYPE",fieldtype);
				$("#Q_"+fieldtext).text($("#"+n).text());
				//$(".linkSpan").hide();
				$("."+fieldtext).hide();
				$("span").each(function(i){
					if($(this).hasClass("title") && $(this).hasClass(fieldtext)){
						var suffix = "";
						if($(this).attr("suffix")!=undefined){
							suffix = $(this).attr("suffix");
						}
						tableTitleContent +=$("#"+n).text()+suffix+" ";
					}
				});
			}
			//------------------------------------
			//查询问题表格
			//------------------------------------
			function queryInfoTable(){
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,tabList);
				defaultJson.doQueryJsonList(controllername+"?queryInfoTable",data,tabList);
			}
			//------------------------------------
			//问题信息卡
			//------------------------------------
			function showInfoCard(obj){
				var showHtml = "";
				var id = obj.WTTB_INFO_ID;
				showHtml = "<a href='javascript:void(0);' title='问题信息卡' onclick=\"openInfoCard('"+id+"')\"><i class='icon-file showInfoCard' wtid='"+obj.WTTB_INFO_ID+"'></i></a>";
				return showHtml;
			}
			//------------------------------------
			//打开问题提报信息卡
			//------------------------------------
			function openInfoCard(n){
				var index = $(event.target).closest("tr").index();
				var tabId = $(event.target).closest("table").attr("id");
		    	$("#"+tabId).cancleSelected();
		    	$("#"+tabId).setSelect(index);
				if($("#"+tabId).getSelectedRowIndex()==-1){
					requireSelectedOneRow();
					return;
				}else{
					$(window).manhuaDialog({"title":"问题提报信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/wtCard.jsp?infoID="+n,"modal":"1"});
		    	}
			}
			//-----------------------
			//--项目名称自动匹配
			//-----------------------
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
			//判断是否显示
			function zzzxld(){
				$("#zzzxld").toggle();
				$("#zzzxbm").hide();
			}
			//判断是否显示
			function zzzxbm(){
				$("#zzzxld").hide();
				$("#zzzxbm").toggle();
			}
			//显示满意程度星星
			function showMycdStar(obj){
				var showHtml = "";
				if(obj.MYCD!=""){
					showHtml += '<div class="starDiv" title="'+obj.MYCD_SV+'">';
					showHtml += '<ul>';
					for(var i=0;i<Number(obj.MYCD);i++){
						showHtml += '<li class="on">';
						showHtml += '<a href="javascript:void(0);">1</a>';
						showHtml += '</li>';
					}
					showHtml += '</ul>';
					showHtml += '</div>';
				}
				return showHtml;
			}
			//“未回复”状态高亮
			function showHfqk(obj){
				var showHtml = "";
				if(obj.HFQK=="0"){
					showHtml = '<span class="label label-warning">未回复</span>';
				}
				return showHtml;
			}
        </script>
	</head>
	<body>
		<div class="row-fluid">
			<h4 class="hTitle" style="display:none;">
				问题列表
			</h4>
			<div style="margin: 5px 5px 5px 30px;">
				<div class="title span8" style="text-align: center;">
					<span class="select" id="tableTitle"></span>
					<span>共<b>(<%=wtList.size() %>)</b>个
					</span>
				</div>
				<div class="pull-right span4">
					<a href="#" onclick="javascript:$('#sxDiv').toggle()">
						显示/隐藏详细筛选条件 </a>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="lineTop lineBottom" id="sxDiv">
				<div class="sxtj" >
				    <div class="containerDiv" id="qzhj" style="float:left;margin-right:30px;">
					<span class="title QZHJ">轻重缓急:</span>
					<span class="contentSpan QZHJ" fieldname="WTXZ" operation="=" fieldvalue="11" id="QZHJ_11"> 一般件</span>
					<span class="linkSpan QZHJ label">(<%=TjUtil.getDataEqual(wtList,"WTXZ","11").size() %>)</span>
					<span class="contentLine QZHJ">|</span>
					<span class="contentSpan QZHJ" fieldname="WTXZ" operation="=" fieldvalue="12" id="QZHJ_12">急件</span>
					<span class="linkSpan QZHJ label">(<%=TjUtil.getDataEqual(wtList,"WTXZ","12").size() %>)</span>
					<span class="contentLine QZHJ">|</span>
					<span class="contentSpan QZHJ" fieldname="WTXZ" operation="=" fieldvalue="13" id="QZHJ_13">特急件</span>
					<span class="linkSpan QZHJ label">(<%=TjUtil.getDataEqual(wtList,"WTXZ","13").size() %>)</span>
					</div>
					<div class="containerDiv" id="jjqk" style="float:left;margin-right:30px;">
					<span class="title JJQK">解决情况:</span>
					<span class="contentSpan JJQK" fieldname="SJZT" operation="=" fieldvalue="6" id="JJQK_YJJ"> 已解决</span>
					<span class="linkSpan JJQK label">(<%=TjUtil.getDataEqual(wtList,"SJZT","6").size() %>)</span>
					<span class="contentLine JJQK">|</span>
					<span class="contentSpan JJQK" fieldname="SJZT" operation="in" fieldvalue="2,4" id="JJQK_CLZ">处理中</span>
					<span class="linkSpan JJQK label">(<%=TjUtil.getDataEqual(wtList,"SJZT","2").size()+TjUtil.getDataEqual(wtList,"SJZT","4").size() %>)</span>
					<span class="contentLine JJQK">|</span>
					<span class="contentSpan JJQK" fieldname="SJZT" operation="=" fieldvalue="3" id="JJQK_WFJJ">未解决</span>
					<span class="linkSpan JJQK label">(<%=TjUtil.getDataEqual(wtList,"SJZT","3").size() %>)</span>
					<span class="contentSpan JJQK" fieldname="SJZT" operation="in" fieldvalue="2,3,4" id="JJQK_WJJ" style="display:none;">未解决</span>
					<span class="linkSpan JJQK label"  style="display:none;">(0)</span>
					</div>
					<div class="containerDiv" id="blsx" style="float:left;margin-right:30px;">
					<span class="title BLSX"  suffix="问题">办理时限:</span>
					<span class="contentSpan BLSX" fieldname="CQBZ" operation="=" fieldvalue="0" id="BLSX_0">正常办理</span>
					<span class="linkSpan BLSX label">(<%=TjUtil.getDataEqual(wtList,"CQBZ","0").size() %>)</span>
					<span class="contentLine BLSX">|</span>
					<span class="contentSpan BLSX" fieldname="CQBZ" operation="=" fieldvalue="1" id="BLSX_1">超期</span>
					<span class="linkSpan BLSX label">(<%=TjUtil.getDataEqual(wtList,"CQBZ","1").size() %>)</span>
					</div>
					<div class="containerDiv" id="sjxm">
					<span class="title SJXM" suffix="项目问题">涉及项目:</span>
					<span class="contentSpan SJXM" fieldname="nvl(JHSJID,'0')" operation="!=" fieldvalue="0" id="SJXM_SJ">涉及</span>
					<span class="linkSpan SJXM label">(<%=TjUtil.getDataEqual(wtList,"SJXM","1").size() %>)</span>
					<span class="contentLine SJXM">|</span>
					<span class="contentSpan SJXM" fieldname="nvl(JHSJID,'0')" operation="=" fieldvalue="0" id="SJXM_BSJ">不涉及</span>
					<span class="linkSpan SJXM label">(<%=TjUtil.getDataEqual(wtList,"SJXM","0").size() %>)</span>
					</div>
				</div>
				<div class="sxtj">
					<div class="containerDiv" id="HFQK" style="float:left;margin-right:30px;">
						<span class="title HFQK" suffix="问题">回复情况:</span>
						<span class="contentSpan HFQK" fieldname="HFQK" operation="=" fieldvalue="1" id="HFQK_YHF">已回复</span>
						<span class="linkSpan HFQK label">(<%=TjUtil.getDataEqual(wtList,"HFQK","1").size() %>)</span>
						<span class="contentLine HFQK">|</span>
						<span class="contentSpan HFQK" fieldname="HFQK" operation="=" fieldvalue="0" id="HFQK_WHF">未回复</span>
						<span class="linkSpan HFQK label">(<%=TjUtil.getDataEqual(wtList,"HFQK","0").size() %>)</span>
					</div>
					<div class="containerDiv" id="MYCD" style="float:left;margin-right:30px;">
						<span class="title MYCD" suffix="">满意程度:</span>
						<span class="contentSpan MYCD" fieldname="MYCD" operation="=" fieldvalue="1" id="MYCD_1">不满意</span>
						<span class="linkSpan MYCD label">(<%=TjUtil.getDataEqual(wtList,"MYCD","1").size() %>)</span>
						<span class="contentLine MYCD">|</span>
						<span class="contentSpan MYCD" fieldname="MYCD" operation="=" fieldvalue="2" id="MYCD_2">基本满意</span>
						<span class="linkSpan MYCD label">(<%=TjUtil.getDataEqual(wtList,"MYCD","2").size() %>)</span>
						<span class="contentLine MYCD">|</span>
						<span class="contentSpan MYCD" fieldname="MYCD" operation="=" fieldvalue="3" id="MYCD_3">非常满意</span>
						<span class="linkSpan MYCD label">(<%=TjUtil.getDataEqual(wtList,"MYCD","3").size() %>)</span>
					</div>
					<div class="containerDiv" style="display:none;">
					&nbsp;
					</div>
				</div>
				<div class="containerDiv sxtj" id="wtlx">
					<span class="title WTLX" suffix="问题">问题类型:</span>
					<span class="contentSpan WTLX" fieldname="WTLX" operation="=" fieldvalue="11" id="WTLX_11">进度</span>
					<span class="linkSpan WTLX label">(<%=TjUtil.getDataEqual(wtList,"WTLX","11").size() %>)</span><span class="contentLine WTLX">|</span>
					<span class="contentSpan WTLX" fieldname="WTLX" operation="=" fieldvalue="12" id="WTLX_12">造价</span>
					<span class="linkSpan WTLX label">(<%=TjUtil.getDataEqual(wtList,"WTLX","12").size() %>)</span><span class="contentLine WTLX">|</span>
					<span class="contentSpan WTLX" fieldname="WTLX" operation="=" fieldvalue="13" id="WTLX_13">质量</span>
					<span class="linkSpan WTLX label">(<%=TjUtil.getDataEqual(wtList,"WTLX","13").size() %>)</span><span class="contentLine WTLX">|</span>
					<span class="contentSpan WTLX" fieldname="WTLX" operation="=" fieldvalue="14" id="WTLX_14">安全</span>
					<span class="linkSpan WTLX label">(<%=TjUtil.getDataEqual(wtList,"WTLX","14").size() %>)</span><span class="contentLine WTLX">|</span>
					<span class="contentSpan WTLX" fieldname="WTLX" operation="=" fieldvalue="15" id="WTLX_15">合同</span>
					<span class="linkSpan WTLX label">(<%=TjUtil.getDataEqual(wtList,"WTLX","15").size() %>)</span><span class="contentLine WTLX">|</span>
					<span class="contentSpan WTLX" fieldname="WTLX" operation="=" fieldvalue="16" id="WTLX_16">协调</span>
					<span class="linkSpan WTLX label">(<%=TjUtil.getDataEqual(wtList,"WTLX","16").size() %>)</span><span class="contentLine WTLX">|</span>
					<span class="contentSpan WTLX" fieldname="WTLX" operation="=" fieldvalue="17" id="WTLX_17">征收</span>
					<span class="linkSpan WTLX label">(<%=TjUtil.getDataEqual(wtList,"WTLX","17").size() %>)</span><span class="contentLine WTLX">|</span>
					<span class="contentSpan WTLX" fieldname="WTLX" operation="=" fieldvalue="18" id="WTLX_18">手续</span>
					<span class="linkSpan WTLX label">(<%=TjUtil.getDataEqual(wtList,"WTLX","18").size() %>)</span><span class="contentLine WTLX">|</span>
					<span class="contentSpan WTLX" fieldname="WTLX" operation="=" fieldvalue="20" id="WTLX_20">设计</span>
					<span class="linkSpan WTLX label">(<%=TjUtil.getDataEqual(wtList,"WTLX","20").size() %>)</span><span class="contentLine WTLX">|</span>
					<span class="contentSpan WTLX" fieldname="WTLX" operation="=" fieldvalue="21" id="WTLX_21">招投标</span>
					<span class="linkSpan WTLX label">(<%=TjUtil.getDataEqual(wtList,"WTLX","21").size() %>)</span><span class="contentLine WTLX">|</span>
					<span class="contentSpan WTLX" fieldname="WTLX" operation="=" fieldvalue="22" id="WTLX_22">排迁</span>
					<span class="linkSpan WTLX label">(<%=TjUtil.getDataEqual(wtList,"WTLX","22").size() %>)</span><span class="contentLine WTLX">|</span>
					<span class="contentSpan WTLX" fieldname="WTLX" operation="=" fieldvalue="23" id="WTLX_23">施工</span>
					<span class="linkSpan WTLX label">(<%=TjUtil.getDataEqual(wtList,"WTLX","23").size() %>)</span><span class="contentLine WTLX">|</span>
					<span class="contentSpan WTLX" fieldname="WTLX" operation="=" fieldvalue="24" id="WTLX_24">计划</span>
					<span class="linkSpan WTLX label">(<%=TjUtil.getDataEqual(wtList,"WTLX","24").size() %>)</span><span class="contentLine WTLX">|</span>
					<span class="contentSpan WTLX" fieldname="WTLX" operation="=" fieldvalue="25" id="WTLX_25">资金</span>
					<span class="linkSpan WTLX label">(<%=TjUtil.getDataEqual(wtList,"WTLX","25").size() %>)</span><span class="contentLine WTLX">|</span>
					<span class="contentSpan WTLX" fieldname="WTLX" operation="=" fieldvalue="26" id="WTLX_26">现场</span>
					<span class="linkSpan WTLX label">(<%=TjUtil.getDataEqual(wtList,"WTLX","26").size() %>)</span><span class="contentLine WTLX">|</span>
					<span class="contentSpan WTLX" fieldname="WTLX" operation="=" fieldvalue="27" id="WTLX_27">行政</span>
					<span class="linkSpan WTLX label">(<%=TjUtil.getDataEqual(wtList,"WTLX","27").size() %>)</span><span class="contentLine WTLX">|</span>
					<span class="contentSpan WTLX" fieldname="WTLX" operation="=" fieldvalue="19" id="WTLX_19">其他</span>
					<span class="linkSpan WTLX label">(<%=TjUtil.getDataEqual(wtList,"WTLX","19").size() %>)</span>
				</div>
				<div class="containerDiv sxtj" id="tcbm">
					<span class="title TCBM" suffix="提出">提出部门:</span>
					<span class="contentSpan TCBM" fieldname="D1.EXTEND1" operation="=" fieldvalue="1" id="TCBM_XMGLGS">项目管理公司</span>
					<span class="linkSpan TCBM label">(<%=TjUtil.getDataEqual(wtList,"D1EXTEND1","1").size() %>)</span>
					<span class="contentLine TCBM">|</span>
					<span class="contentSpan TCBM" fieldname="I.LRBM" operation="=" fieldvalue="100000000000" id="TCBM_100000000000">长建管</span>
					<span class="linkSpan TCBM label">(<%=TjUtil.getDataEqual(wtList,"LRBM","100000000000").size() %>)</span>
					<span class="contentLine TCBM">|</span>
					<span class="contentSpan TCBM" fieldname="I.LRBM" operation="=" fieldvalue="100000000001" id="TCBM_100000000001">办公室</span>
					<span class="linkSpan TCBM label">(<%=TjUtil.getDataEqual(wtList,"LRBM","100000000001").size() %>)</span>
					<span class="contentLine TCBM">|</span>
					<span class="contentSpan TCBM" fieldname="I.LRBM" operation="=" fieldvalue="100000000015" id="TCBM_100000000015">总工办计划组</span>
					<span class="linkSpan TCBM label">(<%=TjUtil.getDataEqual(wtList,"LRBM","100000000015").size() %>)</span>
					<span class="contentLine TCBM">|</span>
					<span class="contentSpan TCBM" fieldname="I.LRBM" operation="=" fieldvalue="100000000014" id="TCBM_100000000014">总工办手续组</span>
					<span class="linkSpan TCBM label">(<%=TjUtil.getDataEqual(wtList,"LRBM","100000000014").size() %>)</span>
					<span class="contentLine TCBM">|</span>
					<span class="contentSpan TCBM" fieldname="I.LRBM" operation="=" fieldvalue="100000000016" id="TCBM_100000000016">总工办设计组</span>
					<span class="linkSpan TCBM label">(<%=TjUtil.getDataEqual(wtList,"LRBM","100000000016").size() %>)</span>
					<span class="contentLine TCBM">|</span>
					<span class="contentSpan TCBM" fieldname="I.LRBM" operation="=" fieldvalue="100000000017" id="TCBM_100000000017">总工办排迁组</span>
					<span class="linkSpan TCBM label">(<%=TjUtil.getDataEqual(wtList,"LRBM","100000000017").size() %>)</span>
					<span class="contentLine TCBM">|</span>
					<span class="contentSpan TCBM" fieldname="I.LRBM" operation="=" fieldvalue="100000000005" id="TCBM_100000000005">前期部（征收部）</span>
					<span class="linkSpan TCBM label">(<%=TjUtil.getDataEqual(wtList,"LRBM","100000000005").size() %>)</span>
					<span class="contentLine TCBM">|</span>
					<span class="contentSpan TCBM" fieldname="I.LRBM" operation="=" fieldvalue="100000000009" id="TCBM_100000000009">招标合同</span>
					<span class="linkSpan TCBM label">(<%=TjUtil.getDataEqual(wtList,"LRBM","100000000009").size() %>)</span>
					<span class="contentLine TCBM">|</span>
					<span class="contentSpan TCBM" fieldname="I.LRBM" operation="=" fieldvalue="100000000004" id="TCBM_100000000004">造价</span>
					<span class="linkSpan TCBM label">(<%=TjUtil.getDataEqual(wtList,"LRBM","100000000004").size() %>)</span>
					<span class="contentLine TCBM">|</span>
					<span class="contentSpan TCBM" fieldname="I.LRBM" operation="=" fieldvalue="100000000003" id="TCBM_100000000003">工程部</span>
					<span class="linkSpan TCBM label">(<%=TjUtil.getDataEqual(wtList,"LRBM","100000000003").size() %>)</span>
					<span class="contentLine TCBM">|</span>
					<span class="contentSpan TCBM" fieldname="I.LRBM" operation="=" fieldvalue="100000000007" id="TCBM_100000000007">轨道办</span>
					<span class="linkSpan TCBM label">(<%=TjUtil.getDataEqual(wtList,"LRBM","100000000007").size() %>)</span>
					<span class="contentLine TCBM">|</span>
					<span class="contentSpan TCBM" fieldname="I.LRBM" operation="=" fieldvalue="100000000006" id="TCBM_100000000006">质量安全部</span>
					<span class="linkSpan TCBM label">(<%=TjUtil.getDataEqual(wtList,"LRBM","100000000006").size() %>)</span>
					<span class="contentLine TCBM">|</span>
					<span class="contentSpan TCBM" fieldname="I.LRBM" operation="=" fieldvalue="100000000002" id="TCBM_100000000002">财务部</span>
					<span class="linkSpan TCBM label">(<%=TjUtil.getDataEqual(wtList,"LRBM","100000000002").size() %>)</span>
					<span class="contentLine TCBM">|</span>
					<span class="contentSpan TCBM" fieldname="I.LRBM" operation="=" fieldvalue="100000000020" id="TCBM_100000000020">建委计财处</span>
					<span class="linkSpan TCBM label">(<%=TjUtil.getDataEqual(wtList,"LRBM","100000000020").size() %>)</span>
					<span class="contentSpan TCBM" kind="query" fieldtype="query" fieldname="WTTB_INFO_ID" operation="in" 
						fieldvalue="select WTTB_INFO_ID from WTTB_INFO I,(select JSR,JSBM,WTID from WTTB_LZLS where BLRJS='1') L,FS_ORG_DEPT D where I.WTTB_INFO_ID=L.WTID and I.LRBM=D.ROW_ID and D.EXTEND1!='1' and D.ROW_ID!='100000000020'" 
						id="TCBM_ZXGBM" style="display:none;">中心各部门</span>
				</div>
				<div class="sxtj WTZZ containerDiv">
					<span class="title ">问题主责:</span>
					<span class="contentSpan WTZZ" fieldtype="query" fieldname="L.JSR" operation="in" 
						fieldvalue="select ACCOUNT from VIEW_ZXZR" 
						id="WTZZ_ZXLD">中心领导</span><span class="linkSpan label"><a href="#"
						onclick="zzzxld()">(<%=TjUtil.getDataEqual(wtList,"ZXLDJS","1").size() %>)</a>
					</span><span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldtype="query" fieldname="I.WTTB_INFO_ID" operation="in" 
						fieldvalue="select WTTB_INFO_ID from WTTB_INFO I,(select JSR,JSBM,WTID from WTTB_LZLS where BLRJS='1') L,FS_ORG_DEPT D where I.WTTB_INFO_ID=L.WTID and L.JSBM=D.ROW_ID and D.EXTEND1!='1' and L.JSR not in (select ACCOUNT from VIEW_ZXZR)"
						id="WTZZ_ZXGBM">中心各部门</span><span class="linkSpan label"><a href="#"
						onclick="zzzxbm()">(<%=TjUtil.getDataEqual(wtList,"ZXGBMJS","1").size() %>)</a>
					</span><span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" kind="query" fieldtype="query" fieldname="I.WTTB_INFO_ID" operation="in" 
						fieldvalue="select WTTB_INFO_ID from WTTB_INFO I,(select JSR,JSBM,WTID from WTTB_LZLS where BLRJS='1') L,FS_ORG_DEPT D where I.WTTB_INFO_ID=L.WTID and L.JSBM=D.ROW_ID and D.EXTEND1='1' and L.JSR not in (select ACCOUNT from VIEW_ZXZR)"
						id="WTZZ_XMGLGS">项目管理公司</span>
					<span class="linkSpan WTZZ label">(<%=TjUtil.getDataEqual(wtList,"XMGLGSJS","1").size() %>)</span>
				</div>
				<div class="sxtj WTZZ" id="zzzxld"
					style="display: none; margin-left: 30px;">
					<span class="title ZXLD" suffix="接收">主责中心领导:</span>
					<span class="contentSpan WTZZ" fieldname="L.JSR" operation="=" fieldvalue="lij" id="WTZZ_lij">李健</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSR","lij").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSR" operation="=" fieldvalue="longqk" id="WTZZ_longqk">龙庆宽</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSR","longqk").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSR" operation="=" fieldvalue="zhouhl" id="WTZZ_zhouhl">周洪亮</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSR","zhouhl").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSR" operation="=" fieldvalue="lisf" id="WTZZ_lisf">李淑芳</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSR","lisf").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSR" operation="=" fieldvalue="liangfq" id="WTZZ_liangfq">梁富清</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSR","liangfq").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSR" operation="=" fieldvalue="maj" id="WTZZ_maj">马俊</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSR","maj").size() %>)</span>
					<span class="contentSpan WTZZ" kind="query" fieldtype="query" fieldname="L.JSR" operation="in" 
						fieldvalue="select ACCOUNT from VIEW_ZXZR"
						id="WTZZ_ZXLD" style="display:none;">中心领导</span>
				</div>
				<div class="sxtj sxtjLast WTZZ" id="zzzxbm"
					style="display: none; margin-left: 30px;">
					<span class="title ZXBM" suffix="接收">主责中心部门:</span>
					<span class="contentSpan WTZZ" fieldname="I.WTTB_INFO_ID" operation="in" 
						fieldvalue="select WTTB_INFO_ID from WTTB_INFO I,(select JSR, JSBM, WTID from WTTB_LZLS where BLRJS = '1') L,FS_ORG_DEPT D where I.WTTB_INFO_ID = L.WTID and I.LRBM = D.ROW_ID and L.JSR not in (select ACCOUNT from VIEW_ZXZR) and D.ROW_ID = '100000000000'" 
						id="WTZZ_100000000000">长建管</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"CJGJS","1").size() %>)</span><span class="WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000001" id="WTZZ_100000000001">办公室</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000001").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000015" id="WTZZ_100000000015">总工办计划组</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000015").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000014" id="WTZZ_100000000014">总工办手续组</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000014").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000016" id="WTZZ_100000000016">总工办设计组</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000016").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000017" id="WTZZ_100000000017">总工办排迁组</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000017").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000005" id="WTZZ_100000000005">前期部（征收部）</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000005").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000009" id="WTZZ_100000000009">招标合同</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000009").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000004" id="WTZZ_100000000004">造价</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000004").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000003" id="WTZZ_100000000003">工程部</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000003").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000007" id="WTZZ_100000000007">轨道办</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000007").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000006" id="WTZZ_100000000006">质量安全部</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000006").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000002" id="WTZZ_100000000002">财务部</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000002").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000020" id="WTZZ_100000000020">建委计财处</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000020").size() %>)</span>
					<span class="contentSpan WTZZ" kind="query" fieldtype="query" fieldname="I.WTTB_INFO_ID" operation="in" 
						fieldvalue="select WTTB_INFO_ID from WTTB_INFO I,(select JSR,JSBM,WTID from WTTB_LZLS where BLRJS='1') L,FS_ORG_DEPT D where I.WTTB_INFO_ID=L.WTID and L.JSBM=D.ROW_ID and D.EXTEND1!='1' and L.JSR not in (select ACCOUNT from VIEW_ZXZR)"
						id="WTZZ_ZXGBM" style="display:none;">中心各部门</span>
				</div>
				<div class="sxtj sxtjLast WTZZ" id="xmglgs"
					style="display: none; margin-left: 30px;">
					<span class="title WTZZ" suffix="接收">项目管理公司:</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000001" id="WTZZ_100000000012">华星</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000001").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000011" id="WTZZ_100000000011">中豪</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000011").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000010" id="WTZZ_100000000010">鸿安</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000010").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000013" id="WTZZ_100000000013">润得</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000013").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="100000000021" id="WTZZ_100000000021">长城投</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","100000000021").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="200000000002" id="WTZZ_200000000002">西客站指挥部</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","200000000002").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" fieldname="L.JSBM" operation="=" fieldvalue="200000000001" id="WTZZ_200000000001">其他</span>
					<span class="linkSpan label">(<%=TjUtil.getDataEqual(wtList,"JSBM","200000000001").size() %>)</span>
					<span class="contentLine WTZZ">|</span>
					<span class="contentSpan WTZZ" kind="query" fieldtype="query" fieldname="I.WTTB_INFO_ID" operation="in" 
						fieldvalue="select WTTB_INFO_ID from WTTB_INFO I,(select JSR,JSBM,WTID from WTTB_LZLS where BLRJS='1') L,FS_ORG_DEPT D where I.WTTB_INFO_ID=L.WTID and L.JSBM=D.ROW_ID and D.EXTEND1='1' and L.JSR not in (select ACCOUNT from VIEW_ZXZR)""
						id="WTZZ_XMGLGS" style="display:none;">项目管理公司</span>
				</div>
				<div class="sxtj sxtjLast WTZZ" style="display:none;">
					<div class="containerDiv" id="fgzr" style="float:left;margin-right:30px;">
						<span class="WTZZ" suffix="接收">问题主责:</span>
						<span class="contentSpan WTZZ" fieldname="L.JSR" operation="in" fieldvalue="longqk,zhouhl,lisf,liangfq,maj" id="WTZZ_FGZR">分管主任</span>
						<span class="linkSpan WTZZ label"></span>
					</div>
				</div>
			</div>
			<div style="height:5px;"></div>
			<div class="row-fluid">
				<div class="span12">
					<div class="B-small-from-table-autoConcise">
						<form method="post"
							action="${pageContext.request.contextPath }/insertdemo.do"
							id="queryForm">
							<table class="B-table" width="100%">
								<!--可以再此处加入hidden域作为过滤条件 -->
								<TR style="display:none;">
									<TD class="right-border bottom-border"></TD>
									<TD class="right-border bottom-border">
										<INPUT type="text" class="span12" kind="text" keep="true"
											fieldname="rownum" value="1000" operation="<=">
									</TD>
									<td>
										人员角色隐藏条件-用于控制按钮，不传入后台
									</td>
									<TD class="right-border bottom-border">
										<input class="span12" type="text" placeholder="" name="LRSJ" id="ND" FIELDTYPE="text"
											fieldname="to_char(I.LRSJ,'yyyy')" operation="=" logic="and">
									</TD>
									<TD class="right-border bottom-border">
										<input class="span12" kind="text" type="text" id="common" operation="=" logic="and">
									</TD>
									<TD class="right-border bottom-border">
										<input class="span12" kind="text" type="text" id="titlecommon" operation="=" logic="and" keep="true">
									</TD>
									<th width="8%" class="right-border bottom-border">
										轻重缓急预留条件
									</th>
									<TD class="right-border bottom-border">
										<input class="span12" kind="text" type="text" id="Q_QZHJ" operation="=" logic="and" keep="true">
									</TD>
									<th width="8%" class="right-border bottom-border">
										解决情况预留条件
									</th>
									<TD class="right-border bottom-border">
										<input class="span12" kind="text" type="text" id="Q_JJQK" operation="=" logic="and" keep="true">
									</TD>
									<th width="8%" class="right-border bottom-border">
										办理时限
									</th>
									<TD class="right-border bottom-border">
										<input class="span12" kind="text" type="text" id="Q_BLSX" operation="=" logic="and" keep="true">
									</TD>
									<th width="8%" class="right-border bottom-border">
										涉及项目
									</th>
									<TD class="right-border bottom-border">
										<input class="span12" kind="text" type="text" id="Q_SJXM" operation="=" logic="and" keep="true">
									</TD>
									<th width="8%" class="right-border bottom-border">
										问题类型
									</th>
									<TD class="right-border bottom-border">
										<input class="span12" kind="text" type="text" id="Q_WTLX" operation="=" logic="and" keep="true">
									</TD>
									<th width="8%" class="right-border bottom-border">
										提出部门
									</th>
									<TD class="right-border bottom-border">
										<input class="span12" kind="text" type="text" id="Q_TCBM" operation="=" logic="and" keep="true">
									</TD>
									<th width="8%" class="right-border bottom-border">
										主责中心领导
									</th>
									<TD class="right-border bottom-border">
										<input class="span12" kind="text" type="text" id="Q_WTZZ" operation="=" logic="and" keep="true">
									</TD>
									<th width="8%" class="right-border bottom-border">
										回复情况
									</th>
									<TD class="right-border bottom-border">
										<input class="span12" kind="text" type="text" id="Q_HFQK" operation="=" logic="and" keep="true">
									</TD>
									<th width="8%" class="right-border bottom-border">
										满意程度
									</th>
									<TD class="right-border bottom-border">
										<input class="span12" kind="text" type="text" id="Q_MYCD" operation="=" logic="and" keep="true">
									</TD>
								</TR>
								<!--可以再此处加入hidden域作为过滤条件 -->
								<tr>
									<th width="8%" class="right-border bottom-border">
										问题标题
									</th>
									<td width="17%" class="right-border bottom-border">
										<input class="span12" type="text" placeholder="" name="WTBT"
											fieldname="I.WTBT" operation="like" logic="and">
									</td>
									<th class="right-border bottom-border">
										项目名称
									</th>
									<td class="bottom-border" colspan=3>
										<input class="span12" type="text" placeholder="" name="QXMMC"
											fieldname="XMMC" operation="like" id="QXMMC"
											autocomplete="off" tablePrefix="S">
									</td>
									<td class="right-border bottom-border text-right">
										<button id="btnQuery" class="btn btn-link" type="button">
											<i class="icon-search"></i>查询
										</button>
										<button id="btnClear" class="btn btn-link" type="button">
											<i class="icon-trash"></i>清空
										</button>
										<button id="btnExp" class="btn btn-link" type="button">
											<i class="icon-trash"></i>导出
										</button>
									</td>
								</tr>
							</table>
						</form>
						<div style="height: 5px;">
						</div>
						<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="tabList"
							width="100%" type="single" pageNum="10">
							<thead>
								<tr>
									<th name="XH" id="_XH" colindex=1>
										&nbsp;#&nbsp;
									</th>
									<th fieldname="SFYX" maxlength="15" tdalign="center"
										colindex=2 CustomFunction="showInfoCard">
										&nbsp;&nbsp;
									</th>
									<th fieldname="WTLX" tdalign="center"colindex=3>
										&nbsp;问题类别&nbsp;
									</th>
									<th fieldname="WTBT" colindex=4 style="width: 20%"
										maxlength="10">
										&nbsp;问题标题&nbsp;
									</th>
									<th fieldname="SJZT" colindex=5 maxlength="10"
										tdalign="center">
										&nbsp;问题状态&nbsp;
									</th>
									<th fieldname="SJSJ" tdalign="center"  sort="true">
										&nbsp;解决时间&nbsp;
									</th>
									<th fieldname="LRBM" sort="true">
										&nbsp;发起部门&nbsp;
									</th>
									<th fieldname="JSR" sort="true">
										&nbsp;主责人&nbsp;
									</th>
									<th fieldname="CQBZ" tdalign="center">
										&nbsp;是否超期&nbsp;
									</th>
									<th fieldname="XMMC" maxlength="15" sort="true">
										&nbsp;项目名称&nbsp;
									</th>
									<th fieldname="BDMC"  maxlength="15" sort="true">
										&nbsp;标段名称&nbsp;
									</th>
									<th fieldname="LRR" sort="true">
										&nbsp;发起人&nbsp;
									</th>
									<th fieldname="LRSJ" sort="true">
										&nbsp;提出时间&nbsp;
									</th>
									<th fieldname="CBCS" sort="true" tdalign="right">
										&nbsp;催办&nbsp;
									</th>
									<th fieldname="HFQK" sort="true" tdalign="center" customFunction="showHfqk">
										&nbsp;回复情况&nbsp;
									</th>
									<th fieldname="MYCD" sort="true" tdalign="center" customFunction="showMycdStar">
										&nbsp;满意程度&nbsp;
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
			<div align="center">
				<FORM name="frmPost" method="post" style="display: none"
					target="_blank">
					<!--系统保留定义区域-->
					<input type="hidden" name="queryXML" id="queryXML">
					<input type="hidden" name="txtXML" id="txtXML">
					<input type="hidden" name="ywid" id="ywid">
					<input type="hidden" name="txtFilter" order="desc" fieldname="SJZT,LRSJ"
						id="txtFilter">
					<input type="hidden" name="resultXML" id="resultXML">
					<!--传递行数据用的隐藏域-->
					<input type="hidden" name="rowData">
					<input type="hidden" name="queryResult" id="queryResult">
				</FORM>
		</div>
	</body>
</html>