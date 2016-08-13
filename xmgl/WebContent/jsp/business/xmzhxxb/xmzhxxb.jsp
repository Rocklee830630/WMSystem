<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>Insert title here</title>
<%
	String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script src="${pageContext.request.contextPath }/js/common/FixTable.js"></script>

<script type="text/javascript" charset="UTF-8">
var sysdate = '<%=sysdate %>';
	var controllername = "${pageContext.request.contextPath }/xmzhxxController.do";
	var isMore = false;
	var moreQueryData,conditionJson;
	var g_pageNum;

	//年份查询
	function generateNd(ndObj){
		ndObj.attr("src","T#GC_JH_SJ:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
		ndObj.attr("kind","dic");
		ndObj.html('');
		reloadSelectTableDic(ndObj);
		ndObj.val(new Date().getFullYear());
	}
	
  	//计算本页表格分页数
  	function setPageHeight() {
  		var xHeight = parent.document.documentElement.clientHeight;
  		var height = xHeight-pageTopHeight-pageQuery-getTableTh(2);
  		var pageNum = parseInt(height/pageTableOne,10);
  //		alert("g_iHeight:"+g_iHeight+"|pageTopHeight:"+pageTopHeight+"|pageTitle:"+pageTitle+"|pageQuery:"+pageQuery+"|pageNumHeight:"+pageNumHeight+"|getTableTh(2):"+getTableTh(1));
  //		alert(height +"|" + pageTableOne + " | " + pageNum);
  //		$("#DT1").attr("pageNum", pageNum);
  		g_pageNum = pageNum;
  	}
  	
  	//初始化查询
  	$(document).ready(function() {
  		generateNd($("#ND"));
		setDefaultNd();
  		setPageHeight();
  		var data=combineQuery.getQueryCombineData(queryForm, frmPost, DT1);
  		defaultJson.doQueryJsonList(controllername+"?queryXmzhxx", data, DT1, "setTdBackColor", true);
  	});
  	
  	//查询
  	$(function() {
  		var btn=$("#example_query");
  		btn.click(function() {
  	//		setPageHeight();
	        //生成json串
	        var data;
	        if(isMore) {
	        	data = moreQueryData;
	        	var dataObj = convertJson.string2json1(moreQueryData);
	      //  	alert(dataObj);
	        	return;
	        } else {
	        	data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
	        }
  			//调用ajax插入
  	 		defaultJson.doQueryJsonList(controllername+"?queryXmzhxx",data, DT1, "setTdBackColor", false);
  	 	});  
  		
  		//清空查询表单
  	    var btn_clearQuery=$("#query_clear");
  	    btn_clearQuery.click(function() {
  	        $("#queryForm").clearFormResult();
  	      	isMore = false;
  	    	setDefaultOption($("#ND"),new Date().getFullYear());
  	    	setDefaultOption($("#ND"),new Date().getFullYear());
  	        //其他处理放在下面
  	    });

		//自定义导出
  		$("#do_excel").click(function() {
			var t = $("#DT1").getTableRows();
			if(t<=0) {
			xAlert("提示信息","请至少查询出一条记录！",'3');
				return;
			}
			 
			var tabObj = document.getElementById("DT1");
				
			var actionName = tabObj.getAttribute("queryPath");
			var queryData = tabObj.getAttribute("queryData");
			var queryResult = document.getElementById("queryResult").value;
				
			var querycondition = setTotalPage(queryData, queryResult);
				
			var data = {
					msg : querycondition
				};
				$.ajax({
					url : actionName,
					data : data,
					cache : false,
					async :	false,
					dataType : "json",  
					type : 'post',
					success : function(result) {
						document.getElementById("exportQueryResultCondition").value = result.msg;//当前页的查询结果集
					}
				});
				
			$("#loginForm").submit();
			 
	//		printCustomTabList("DT1",controllername+"?exportXxzhxx");
		});
  		

  		//自动完成项目名称模糊查询
  		showAutoComplete("XMMC",controllername+"?xmmcAutoQuery","getXmmcQueryCondition"); 
  		//自动完成项目名称模糊查询
  		showAutoComplete("BDMC",controllername+"?bdmcAutoQuery","getBdmcQueryCondition");

  	 });
  	
  	function  setTotalPage(querycondition,queryResult)
	{
    	var qr= convertJson.string2json1(queryResult);
    	var countrows = qr.pages["countrows"];
    	var qc= convertJson.string2json1(querycondition);
    	 qc.pages["recordsperpage"] = countrows;
        return JSON.stringify(qc);
		
	}	

  //生成项目名称查询条件
  function getXmmcQueryCondition(){
  	var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":""}]}';
  	var jsonData = convertJson.string2json1(initData); 
   	//项目名称
  	if("" != $("#XMMC").val()){
  		var defineCondition = {"value": "%"+$("#XMMC").val()+"%","fieldname":"XMMC","operation":"like","logic":"and"};
  		jsonData.querycondition.conditions.push(defineCondition);
  	}
      //年度
  	if("" != $("#ND").val()){
  		var defineCondition = {"value": $("#ND").val(),"fieldname":"ND","operation":"=","logic":"and"};
  		jsonData.querycondition.conditions.push(defineCondition);
  	}
   	return JSON.stringify(jsonData);
  }
  

  //生成标段名称查询条件
  function getBdmcQueryCondition(){
  	var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":""}]}';
  	var jsonData = convertJson.string2json1(initData); 
   	//项目名称
  	if("" != $("#BDMC").val()){
  		var defineCondition = {"value": "%"+$("#BDMC").val()+"%","fieldname":"BDMC","operation":"like","logic":"and"};
  		jsonData.querycondition.conditions.push(defineCondition);
  	}
      //年度
  	if("" != $("#ND").val()){
  		var defineCondition = {"value": $("#ND").val(),"fieldname":"ND","operation":"=","logic":"and"};
  		jsonData.querycondition.conditions.push(defineCondition);
  	}
   	return JSON.stringify(jsonData);
  }
  	
  	function setTdBackColor(){
		var $thistab = $("#DT1");
        var rows = $("tbody tr" ,$thistab);
        for(var t =0;t<rows.size();t++) {
        	var $tr_obj = $(rows[t]);
        	
        	// 获取此列的行数，如果合并为65行，如果未合并为67行
        	var tdcnt = $tr_obj.find("td").size();
        	// 每行是否有合并的项，如果此行有合并结果为2，如果此行没有合并结果为0
        	var result = 67-tdcnt;
        	// 工程进展
           	var td_obj10 = $tr_obj.find("td").eq(10-result);
           	var td_obj11 = $tr_obj.find("td").eq(11-result);
           	var td_obj12 = $tr_obj.find("td").eq(12-result);
           	var td_obj13 = $tr_obj.find("td").eq(13-result);
           	var td_obj14 = $tr_obj.find("td").eq(14-result);
           	td_obj10.css({"background":"#f6f6f6"});
           	td_obj11.css({"background":"#f6f6f6"});
           	td_obj12.css({"background":"#f6f6f6"});
           	td_obj13.css({"background":"#f6f6f6"});
           	td_obj14.css({"background":"#f6f6f6"});
           	// 设计进展
           	var td_obj19 = $tr_obj.find("td").eq(19-result);
           	var td_obj20 = $tr_obj.find("td").eq(20-result);
           	var td_obj21 = $tr_obj.find("td").eq(21-result);
           	var td_obj22 = $tr_obj.find("td").eq(22-result);
           	var td_obj23 = $tr_obj.find("td").eq(23-result);
           	var td_obj24 = $tr_obj.find("td").eq(24-result);
           	td_obj19.css({"background":"#f6f6f6"});
           	td_obj20.css({"background":"#f6f6f6"});
           	td_obj21.css({"background":"#f6f6f6"});
           	td_obj22.css({"background":"#f6f6f6"});
           	td_obj23.css({"background":"#f6f6f6"});
           	td_obj24.css({"background":"#f6f6f6"});
           	// 招标进展
           	var td_obj26 = $tr_obj.find("td").eq(26-result);
           	var td_obj27 = $tr_obj.find("td").eq(27-result);
           	var td_obj28 = $tr_obj.find("td").eq(28-result);
           	td_obj26.css({"background":"#f6f6f6"});
           	td_obj27.css({"background":"#f6f6f6"});
           	td_obj28.css({"background":"#f6f6f6"});
           	// 征收信息
           	var td_obj33 = $tr_obj.find("td").eq(33-result);
           	var td_obj34 = $tr_obj.find("td").eq(34-result);
           	var td_obj35 = $tr_obj.find("td").eq(35-result);
           	var td_obj36 = $tr_obj.find("td").eq(36-result);
           	var td_obj37 = $tr_obj.find("td").eq(37-result);
           	var td_obj38 = $tr_obj.find("td").eq(38-result);
           	td_obj33.css({"background":"#f6f6f6"});
           	td_obj34.css({"background":"#f6f6f6"});
           	td_obj35.css({"background":"#f6f6f6"});
           	td_obj36.css({"background":"#f6f6f6"});
           	td_obj37.css({"background":"#f6f6f6"});
           	td_obj38.css({"background":"#f6f6f6"});
           	// 投资估算（万元）
           	var td_obj51 = $tr_obj.find("td").eq(51-result);
            var td_obj52 = $tr_obj.find("td").eq(52-result);
           	var td_obj53 = $tr_obj.find("td").eq(53-result);
           	var td_obj54 = $tr_obj.find("td").eq(54-result);
           	td_obj51.css({"background":"#f6f6f6"});
           	td_obj52.css({"background":"#f6f6f6"});
           	td_obj53.css({"background":"#f6f6f6"});
           	td_obj54.css({"background":"#f6f6f6"});
           	// 资金支付信息（元）
           	var td_obj59 = $tr_obj.find("td").eq(59-result);
            var td_obj60 = $tr_obj.find("td").eq(60-result);
           	var td_obj61 = $tr_obj.find("td").eq(61-result);
           	var td_obj62 = $tr_obj.find("td").eq(62-result);
           	td_obj59.css({"background":"#f6f6f6"});
           	td_obj60.css({"background":"#f6f6f6"});
           	td_obj61.css({"background":"#f6f6f6"});
           	td_obj62.css({"background":"#f6f6f6"});
        }
        var rows = $("tbody tr" ,$("#DT1"));
        
    	var tr_obj = rows.eq(0);
        var t = $("#DT1").getTableRows();
        var tr_height = $(tr_obj).height();
        var d = t*tr_height+getTableTh(2)+20;

     	// 当高度大于500时，显示主页面列表的高度
        if(d>500) d = getDivStyleHeight()-pageQuery-getTableTh(1);
         // 当没有数据的时候，只显示表头
     	if(tr_height==null) d = getTableTh(2)+20;
        window_width = document.documentElement.clientWidth;//$("#allDiv").width()
   	    $("#DT1").fixTable({
   			fixColumn: 6,//固定列数
   			width:window_width-10,//显示宽度
   			height:d//显示高度
   		});
        
    	    rows = $("td");
    	    for(var t =0;t<rows.size();t++) {
        	    	if($("#exception",$(rows[t])).size()>0){
        	    		//结算背景橙色（自定义）
            	    	$(rows[t]).css('background','#FBC77D');
            	    }
        	    	if($("#error" ,$(rows[t])).size()>0){
        	    		// 结算背景粉色（自定义）
            	    	$(rows[t]).css('background','#FF7F7F');
            	    }
    	    }
        
	}
//=============================SJL=======================================
//设置甘特图字段样式
function doGtt(obj){
	var id = obj.GTT;
	return "<div style=\"text-align:center\"><a href=\"javascript:void(0);\"><i title=\"查看甘特图\" class=\"icon-tasks\" onclick=\"showGtt('"+id+"')\"></i></a></div>";
}
//显示甘特图
function showGtt(id){
	$(window).manhuaDialog({"title":"项目甘特图","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/tcjh/tcjh_gtt.jsp?id="+id,"modal":"1"});
}
//列表项<项目地址>加图标
function doDz(obj){
	//if(obj.JKZK != ""){
	//	return '<a href="javascript:void(0);" onclick="selectDz()"><i title="查看" class="pull-right icon-map-marker"></a></i>';
	//}
	var xmdz = obj.XMDZ;
	var xmid = obj.XMID;
	var bdid = obj.BDID;
	if(xmdz != ""){
		return "<a href=\"javascript:void(0);\" onclick=\"selectDz('"+xmid+"','"+bdid+"')\"><i title=\"查看\" class=\"pull-right icon-map-marker\"></a></i>";
	}
}
//点击项目地址图标
function selectDz(xmid,bdid){
	var url = "${pageContext.request.contextPath }/jsp/xmgl/xmcbk/baidu_view.jsp?xmbh="+xmid+"&bdbh="+bdid;
	$(window).manhuaDialog({"title":"项目地图","type":"text","content":url,"modal":"1"});
}
//标段名称图标样式
function doBdmc(obj){
	if(obj.BDMC == ""){
		return '<div style="text-align:center"><i class="icon-minus" title="无标段"></i></div>';
	}else{
		return obj.BDMC;
	}
}
//列表项<健康状况>状态
function doJkzk(obj){
	
	var daysArray = new Array();
	var textArray = new Array();
	if(obj.KGSJ != "" && obj.KGSJ_SJ == ""){
		daysArray.push(obj.KGSJ);
		textArray.push("开工时间");
	}
	if(obj.WGSJ != "" && obj.WGSJ_SJ == ""){
		daysArray.push(obj.WGSJ);
		textArray.push("完工时间");
	}
	if(obj.KYPF !="" && obj.KYPF_SJ == ""){
		daysArray.push(obj.KYPF);
		textArray.push("可研批复");
	} 
	if(obj.HPJDS != "" && obj.HPJDS_SJ == ""){
		daysArray.push(obj.HPJDS);
		textArray.push("划拔决定书");
	}
	if(obj.GCXKZ != "" && obj.GCXKZ_SJ == ""){
		daysArray.push(obj.GCXKZ);
		textArray.push("工程规划许可证");
	}
	if(obj.SGXK !="" && obj.SGXK_SJ ==""){
		daysArray.push(obj.SGXK);
		textArray.push("施工许可");
	}
	if(obj.CBSJPF != "" && obj.CBSJPF_SJ == ""){
		daysArray.push(obj.CBSJPF);
		textArray.push("初步设计批复");
	}
	if(obj.CQT != "" && obj.CQT_SJ == ""){
		daysArray.push(obj.CQT);
		textArray.push("拆迁图");
	}
	if(obj.PQT != "" && obj.PQT_SJ == ""){
		daysArray.push(obj.PQT);
		textArray.push("排迁图");
	}
	if(obj.SGT != "" && obj.SGT_SJ == ""){
		daysArray.push(obj.SGT);
		textArray.push("施工图");
	}
	if(obj.TBJ != "" && obj.TBJ_SJ == ""){
		daysArray.push(obj.TBJ);
		textArray.push("提报价");
	}
	if(obj.CS != "" && obj.CS_SJ == ""){
		daysArray.push(obj.CS);
		textArray.push("造价财审");
	}
	if(obj.JLDW != "" && obj.JLDW_SJ == ""){
		daysArray.push(obj.JLDW);
		textArray.push("招标监理单位");
	}
	if(obj.SGDW != "" && obj.SGDW_SJ == ""){
		daysArray.push(obj.SGDW);
		textArray.push("招标施工单位");
	}
	if(obj.ZC != "" && obj.ZC_SJ == ""){
		daysArray.push(obj.ZC);
		textArray.push("征拆");
	}
	if(obj.PQ != "" && obj.PQ_SJ == ""){
		daysArray.push(obj.PQ);
		textArray.push("排迁");
	}
	if(obj.JG != "" && obj.JG_SJ == ""){
		daysArray.push(obj.JG);
		textArray.push("交工");
	}
	if(daysArray.length == 0){
		return '<div style="text-align:center"><i title="正常执行" class="icon-green"></i></div>';
	}
	for(var i = 0; i<daysArray.length; i++){
		for(var j = 0; j<daysArray.length - i;j++){
			if(new Date(daysArray[j]) < new Date(daysArray[j+1]) ){
				var temp = daysArray[j];
				daysArray[j] = daysArray[j + 1];
				daysArray[j + 1] = temp;
				var textTemp = textArray[j];
				textArray[j] = textArray[j + 1];
				textArray[j + 1] = textTemp;
				
			}
		}
	}
	var dateVal = getDays(sysdate,daysArray[daysArray.length-1]);
	var textVal = textArray[textArray.length-1];
	if( dateVal<= 5){
		return '<div style="text-align:center"><i title="正常执行" class="icon-green"></i></div>';
	}else if(dateVal >5 && dateVal <=10){
		return '<div style="text-align:center"><i title="<'+textVal+'>截止到今天超期'+dateVal+'天未反馈" class="icon-yellow"></i></div>';
	}else if(dateVal >10){
		return '<div style="text-align:center"><i title="<'+textVal+'>截止到今天超期'+dateVal+'天未反馈" class="icon-red"></i></div>';
	}
}
//计算日期天数
function getDays(strDateStart,strDateEnd){
	if(strDateStart < strDateEnd){
		return 0;
	}
   var strSeparator = "-"; //日期分隔符
   var oDate1;
   var oDate2;
   var iDays;
   oDate1= strDateStart.split(strSeparator);
   oDate2= strDateEnd.split(strSeparator);
   var strDateS = new Date(oDate1[0] + "-" + oDate1[1] + "-" + oDate1[2]);
   var strDateE = new Date(oDate2[0] + "-" + oDate2[1] + "-" + oDate2[2]);
   iDays = parseInt(Math.abs(strDateS - strDateE ) / 1000 / 60 / 60 /24)//把相差的毫秒数转换为天数 

   return iDays ;
}
//列表项<征拆>状态,不需要办理：灰色√
function do22ZC(obj){
	if(obj.ISZC == "0"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	}else{
		return getCellStyle(obj.ZC_SJ,obj.ZC);
	}
}

function doZC(obj){
	if(obj.GC_YCQ == "1"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	} else if(obj.GC_YCQ == "2") {
		return '<i class="icon-yes-green" title="按期完成"></i>';
	} else if(obj.GC_YCQ == "3") {
		return '<i class="icon-minus" title="未完成未到期"></i>';
	} else if(obj.GC_YCQ == "4") {
		return '<i class="icon-yes-yellow" title="延期完成"></i>';
	} else if(obj.GC_YCQ == "5") {
		return '<i class="icon-no-red" title="到期未完成"></i>';
	}
}
//列表项<排迁>状态

function do22PQ(obj){
	if(obj.ISPQ == "0"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	}else{
		return getCellStyle(obj.PQ_SJ,obj.PQ);
	}
} 


/*
1：不需要办理
2：按期完成、
3：未完成未到期
4：延期完成
5：到期未完成
大部分用这个通用的。
*/
function doPQ(obj){
	if(obj.GC_YPQ == "1"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	} else if(obj.GC_YPQ == "2") {
		return '<i class="icon-yes-green" title="按期完成"></i>';
	} else if(obj.GC_YPQ == "3") {
		return '<i class="icon-minus" title="未完成未到期"></i>';
	} else if(obj.GC_YPQ == "4") {
		return '<i class="icon-yes-yellow" title="延期完成"></i>';
	} else if(obj.GC_YPQ == "5") {
		return '<i class="icon-no-red" title="到期未完成"></i>';
	}
}
//拆迁图 
function do22CQT(obj){
	if(obj.ISCQT == "0"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	}else{
		return getCellStyle(obj.CQT_SJ,obj.CQT);
	}
}

function doCQT(obj){
	if(obj.SJ_CQT == "1"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	} else if(obj.SJ_CQT == "2") {
		return '<i class="icon-yes-green" title="按期完成"></i>';
	} else if(obj.SJ_CQT == "3") {
		return '<i class="icon-minus" title="未完成未到期"></i>';
	} else if(obj.SJ_CQT == "4") {
		return '<i class="icon-yes-yellow" title="延期完成"></i>';
	} else if(obj.SJ_CQT == "5") {
		return '<i class="icon-no-red" title="到期未完成"></i>';
	}
}
//排迁图
function do22PQT(obj){
	if(obj.ISPQT == "0"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	}else{
		return getCellStyle(obj.PQT_SJ,obj.PQT);
	}
}

function doPQT(obj){
	if(obj.SJ_PQT == "1"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	} else if(obj.SJ_PQT == "2") {
		return '<i class="icon-yes-green" title="按期完成"></i>';
	} else if(obj.SJ_PQT == "3") {
		return '<i class="icon-minus" title="未完成未到期"></i>';
	} else if(obj.SJ_PQT == "4") {
		return '<i class="icon-yes-yellow" title="延期完成"></i>';
	} else if(obj.SJ_PQT == "5") {
		return '<i class="icon-no-red" title="到期未完成"></i>';
	}
}
//施工图
function do22SGT(obj){
	if(obj.ISSGT == "0"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	}else{
		return getCellStyle(obj.SGT_SJ,obj.SGT);
	}
}

function doSGT(obj){
	if(obj.SJ_SGT == "1"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	} else if(obj.SJ_SGT == "2") {
		return '<i class="icon-yes-green" title="按期完成"></i>';
	} else if(obj.SJ_SGT == "3") {
		return '<i class="icon-minus" title="未完成未到期"></i>';
	} else if(obj.SJ_SGT == "4") {
		return '<i class="icon-yes-yellow" title="延期完成"></i>';
	} else if(obj.SJ_SGT == "5") {
		return '<i class="icon-no-red" title="到期未完成"></i>';
	}
}
//开工时间
function do22KG(obj){
	return getCellStyle(obj.KGSJ_SJ,obj.KGSJ);
}

function doKG(obj){
	if(obj.GC_YKG == "1"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	} else if(obj.GC_YKG == "2") {
		return '<i class="icon-yes-green" title="按期完成"></i>';
	} else if(obj.GC_YKG == "3") {
		return '<i class="icon-minus" title="未完成未到期"></i>';
	} else if(obj.GC_YKG == "4") {
		return '<i class="icon-yes-yellow" title="延期完成"></i>';
	} else if(obj.GC_YKG == "5") {
		return '<i class="icon-no-red" title="到期未完成"></i>';
	}
}
//完工时间
function do22WG(obj){
	return getCellStyle(obj.WGSJ_SJ,obj.WGSJ);
}

function doWG(obj){
	if(obj.GC_YJJG == "1"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	} else if(obj.GC_YWG == "2") {
		return '<i class="icon-yes-green" title="按期完成"></i>';
	} else if(obj.GC_YWG == "3") {
		return '<i class="icon-minus" title="未完成未到期"></i>';
	} else if(obj.GC_YWG == "4") {
		return '<i class="icon-yes-yellow" title="延期完成"></i>';
	} else if(obj.GC_YWG == "5") {
		return '<i class="icon-no-red" title="到期未完成"></i>';
	}
}
//可研批复
function do22KYPF(obj){
	if(obj.ISKYPF == "0"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	}else{
		return getCellStyle(obj.KYPF_SJ,obj.KYPF);
	}
}

function doKYPF(obj){
	if(obj.SXBL_KYPF == "1"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	} else if(obj.SXBL_KYPF == "2") {
		return '<i class="icon-yes-green" title="按期完成"></i>';
	} else if(obj.SXBL_KYPF == "3") {
		return '<i class="icon-minus" title="未完成未到期"></i>';
	} else if(obj.SXBL_KYPF == "4") {
		return '<i class="icon-yes-yellow" title="延期完成"></i>';
	} else if(obj.SXBL_KYPF == "5") {
		return '<i class="icon-no-red" title="到期未完成"></i>';
	}
}

//划拔决定书
function do22HBJDS(obj){
	if(obj.ISHPJDS == "0"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	}else{
		return getCellStyle(obj.HPJDS_SJ,obj.HPJDS);
	}
	
}

function doHBJDS(obj){
	if(obj.SXBL_TDHB == "1"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	} else if(obj.SXBL_TDHB == "2") {
		return '<i class="icon-yes-green" title="按期完成"></i>';
	} else if(obj.SXBL_TDHB == "3") {
		return '<i class="icon-minus" title="未完成未到期"></i>';
	} else if(obj.SXBL_TDHB == "4") {
		return '<i class="icon-yes-yellow" title="延期完成"></i>';
	} else if(obj.SXBL_TDHB == "5") {
		return '<i class="icon-no-red" title="到期未完成"></i>';
	}
}
//工程规划许可证
function do22GCGHXKZ(obj){
	if(obj.ISGCXKZ == "0"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	}else{
		return getCellStyle(obj.GCXKZ_SJ,obj.GCXKZ);
	}
}

function doGCGHXKZ(obj){
	if(obj.SXBL_GCGHXK == "1"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	} else if(obj.SXBL_GCGHXK == "2") {
		return '<i class="icon-yes-green" title="按期完成"></i>';
	} else if(obj.SXBL_GCGHXK == "3") {
		return '<i class="icon-minus" title="未完成未到期"></i>';
	} else if(obj.SXBL_GCGHXK == "4") {
		return '<i class="icon-yes-yellow" title="延期完成"></i>';
	} else if(obj.SXBL_GCGHXK == "5") {
		return '<i class="icon-no-red" title="到期未完成"></i>';
	}
}
//施工许可
function do22SGXK(obj){
	if(obj.ISSGXK == "0"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	}else{
		return getCellStyle(obj.SGXK_SJ,obj.SGXK);
	}
}

function doSGXK(obj){
	if(obj.SXBL_SGXK == "1"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	} else if(obj.SXBL_SGXK == "2") {
		return '<i class="icon-yes-green" title="按期完成"></i>';
	} else if(obj.SXBL_SGXK == "3") {
		return '<i class="icon-minus" title="未完成未到期"></i>';
	} else if(obj.SXBL_SGXK == "4") {
		return '<i class="icon-yes-yellow" title="延期完成"></i>';
	} else if(obj.SXBL_SGXK == "5") {
		return '<i class="icon-no-red" title="到期未完成"></i>';
	}
}
//初步设计批复
function do22CBSJPF(obj){
	if(obj.ISCBSJPF == "0"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	}else{
		return getCellStyle(obj.CBSJPF_SJ,obj.CBSJPF);
	}
}

function doCBSJPF(obj){
	if(obj.SJ_CSPF == "1"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	} else if(obj.SJ_CSPF == "2") {
		return '<i class="icon-yes-green" title="按期完成"></i>';
	} else if(obj.SJ_CSPF == "3") {
		return '<i class="icon-minus" title="未完成未到期"></i>';
	} else if(obj.SJ_CSPF == "4") {
		return '<i class="icon-yes-yellow" title="延期完成"></i>';
	} else if(obj.SJ_CSPF == "5") {
		return '<i class="icon-no-red" title="到期未完成"></i>';
	}
}
//交工
function do22JG(obj){
	if(obj.ISJG == "0" && obj.ISJS == "0"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	}else{
		if(obj.JG != ""){
			if(obj.JS_SJ != ""){
				if(new Date(obj.JS_SJ) > new Date(obj.JG)){
					return '<i class="icon-yes-yellow" title="延期完成"></i>';
				}else{
					return '<i class="icon-yes-green" title="按期完成"></i>';
				}
			}else{
				if(obj.JG_SJ == ""){
					if(new Date(sysdate) > new Date(obj.JG)){
						return '<i class="icon-no-red" title="到期未完成"></i>';
					}else{
						return '<i class="icon-minus" title="未完成未到期"></i>';
					}
				}else{
					if(new Date(obj.JG_SJ) > new Date(obj.JG)){
						return '<i class="icon-yes-yellow" title="延期完成"></i>';
					}else{
						return '<i class="icon-yes-green" title="按期完成"></i>';
					}
				}
			}
		}else{
			if(obj.JS_SJ == ""){
				return '<i class="icon-minus" title="未完成未到期"></i>';
			}else if(obj.JG_SJ == ""){
				return '<i class="icon-minus" title="未完成未到期"></i>';
			}else{
				return '<i class="icon-yes-green" title="按期完成"></i>';
			}
		}
	}
	
}

function doJG(obj){
	if(obj.GC_YJJG == "1"){
		return '<i class="icon-ok" title="不需要办理"></i>';
	} else if(obj.GC_YJJG == "2") {
		return '<i class="icon-yes-green" title="按期完成"></i>';
	} else if(obj.GC_YJJG == "3") {
		return '<i class="icon-minus" title="未完成未到期"></i>';
	} else if(obj.GC_YJJG == "4") {
		return '<i class="icon-yes-yellow" title="延期完成"></i>';
	} else if(obj.GC_YJJG == "5") {
		return '<i class="icon-no-red" title="到期未完成"></i>';
	}
}
//各节点样式,绿√；延期完成：黄√；未完成未到期：—；到期未完成：红× 
function getCellStyle(str1,str2){
	if(str2 != ""){
		if(str1 == ""){
			if(new Date(sysdate) > new Date(str2)){
				return '<i class="icon-no-red" title="到期未完成"></i>';
			}else{
				return '<i class="icon-minus" title="未完成未到期"></i>';
			}
		}else{
			if(new Date(str1) > new Date(str2)){
				return '<i class="icon-yes-yellow" title="延期完成"></i>';
			}else{
				return '<i class="icon-yes-green" title="按期完成"></i>';
			}
			
		}
	}else{
		if(str1 == ""){
			return '<i class="icon-minus" title="未完成未到期"></i>';
		}else{
			return '<i class="icon-yes-green" title="按期完成"></i>';
		}
	}
}
/******************************** 
 * 设计-start
 ********************************/
//设计任务书样式                                                                                                                                                                                                                                                                                                                                                                                                                                                       ，cf
function doSjrws(obj){
	var sjrws=Number(obj.SJ_SJRWS);
	var value=sjrws !=0 ?('<i class="icon-yes-green" title="有设计任务书"></i>'):('<i class="icon-no-red" title="无设计任务书"></i>');
	return value;
}
/******************************** 
 * 设计-end
 ********************************/
 
/******************************** 
 * 征收方法-start
 ********************************/
//征收，居民显示
function showJm(obj){
	var show=(obj.BDMC)?('<div style="text-align:center"><i class="icon-minus"></i></div>'):(obj.ZS_ZJMS_JWCQ);
	return show;
}
//征收，企业显示
function showQy(obj){
	var show=(obj.BDMC)?('<div style="text-align:center"><i class="icon-minus"></i></div>'):(obj.ZS_ZQYS_YWCQ);
	return show;
}
//征收，土地显示
function showZd(obj){
	var show=(obj.BDMC)?('<div style="text-align:center"><i class="icon-minus"></i></div>'):(obj.ZS_ZDMJ_YWZD);
	return show;
}
//征收，拨入金额显示
function showBrzj(obj){
	var show=(obj.BDMC)?('<div style="text-align:center"><i class="icon-minus"></i></div>'):(obj.ZS_BRZJ_SV);
	return show;
}
//征收，使用金额显示
function showSyzj(obj){
	var show=(obj.BDMC)?('<div style="text-align:center"><i class="icon-minus"></i></div>'):(obj.ZS_YSYZJ_SV);
	return show;
}
//征收，余额显示
function showYe(obj){
	var show;
	if(obj.BDMC==null||obj.BDMC==""){
		show=(eval(obj.ZS_YE)>=eval(0))?(obj.ZS_YE_SV):('<span class="label label-important" title="'+obj.ZS_YE_SV+'">'+obj.ZS_YE_SV+'</span>');
	}else{
		show='<div style="text-align:center"><i class="icon-minus"></i></div>';
	}
	return show;
}
/******************************** 
 * 征收方法-end
 ********************************/
 
 /******************************** 
  *概算-start
  ********************************/
 //概算总投资
 function showZtz(obj){
 	var value=(eval(obj.GS_ZTZ)>eval(obj.TZ_ZTZ))?('<span class="label label-important-orange" title="'+obj.GS_ZTZ_SV+'">'+obj.GS_ZTZ_SV+'</span>'):(obj.GS_ZTZ_SV);
 	return value;
 }
 /******************************** 
  * 概算-end
  ********************************/
  
  
  /*****************拦标价 START*********************/
	  // 拦标价
	 function doLBJ(obj) {
		 if(!obj.LBJ) {
			 if(obj.CS == "") {
				 return '<div style="text-align:center"><i class="icon-minus" title="未到期"></i></div>';
			 } else {
				 if(new Date(sysdate) > new Date(obj.CS)) {
					 return '<div style="text-align:center"><i class="icon-no-red" title="已到期"></i></div>';
				 } else {
					 return '<div style="text-align:center"><i class="icon-minus" title="未到期"></i></div>';
				 }
			 }
		 } else {
			 return obj.LBJ_SV;
		 }
	 }
  /*****************拦标价 END*********************/
  
 /*****************招标进展 START*********************/
 // 设计单位
 function doZB_SJDW(obj) {
	 if(!obj.ZB_SJDW) {
		 return '<div style="text-align:center"><i class="icon-minus"></i></div>';
	 } else {
		 return obj.ZB_SJDW_SV;
	 }
 }
 // 监理单位
 function doZB_JLDW(obj) {
	 if(!obj.ZB_JLDW) {
		 if(new Date(sysdate) > new Date(obj.JLDW)) {
			 return '<div style="text-align:center"><i class="icon-no-red" title="已到期"></i></div>';
		 } else {
			 return '<div style="text-align:center"><i class="icon-minus" title="未到期"></i></div>';
		 }
	 } else {
		 return obj.ZB_JLDW_SV;
	 }
 }
 // 施工单位
 function doZB_SGDW(obj) {
	 if(!obj.ZB_SGDW) {
		 if(new Date(sysdate) > new Date(obj.SGDW)) {
			 return '<div style="text-align:center"><i class="icon-no-red" title="已到期"></i></div>';
		 } else {
			 return '<div style="text-align:center"><i class="icon-minus" title="未到期"></i></div>';
		 }
	 } else {
		 return obj.ZB_SGDW_SV;
	 }
 }
 /*****************招标进展 END*********************/
 
 
 /*****************投资估算 START*********************/
 function doTZ_GC(obj) {
	 if(obj.XMBS == "1") {
		 return '<div style="text-align:center"><i class="icon-minus"></i></div>';
	 } else {
		 return obj.TZ_GC_SV;
	 }
 }
 
 function doTZ_ZC(obj) {
	 if(obj.XMBS == "1") {
		 return '<div style="text-align:center"><i class="icon-minus"></i></div>';
	 } else {
		 return obj.TZ_ZC_SV;
	 }
 }
 
 function doTZ_QT(obj) {
	 if(obj.XMBS == "1") {
		 return '<div style="text-align:center"><i class="icon-minus"></i></div>';
	 } else {
		 return obj.TZ_QT_SV;
	 }
 }
 
 function doTZ_ZTZ(obj) {
	 if(obj.XMBS == "1") {
		 return '<div style="text-align:center"><i class="icon-minus"></i></div>';
	 } else {
		 return obj.TZ_ZTZ_SV;
	 }
 }
 /*****************投资估算 END*********************/
 
 /*****************总体投资估算 BEGIN*********************/
 function doZTTZ_GC(obj) {
	 if(obj.XMBS == "1") {
		 return '<div style="text-align:center"><i class="icon-minus"></i></div>';
	 } else {
		 return obj.ZTTZ_GC_SV;
	 }
 }
 
 function doZTTZ_ZC(obj) {
	 if(obj.XMBS == "1") {
		 return '<div style="text-align:center"><i class="icon-minus"></i></div>';
	 } else {
		 return obj.ZTTZ_ZC_SV;
	 }
 }
 
 function doZTTZ_QT(obj) {
	 if(obj.XMBS == "1") {
		 return '<div style="text-align:center"><i class="icon-minus"></i></div>';
	 } else {
		 return obj.ZTTZ_QT_SV;
	 }
 }
 
 function doZTTZ_ZTZ(obj) {
	 if(obj.XMBS == "1") {
		 return '<div style="text-align:center"><i class="icon-minus"></i></div>';
	 } else {
		 return obj.ZTTZ_ZTZ_SV;
	 }
 }
 /*****************投资估算 END*********************/
 
 /***************** 概算信息 START*********************/
 function doGS_GC(obj) {
	 if(obj.XMBS == "1") {
		 return '<div style="text-align:center"><i class="icon-minus"></i></div>';
	 } else {
		 return obj.GS_GC_SV;
	 }
 }
 
 function doGS_ZC(obj) {
	 if(obj.XMBS == "1") {
		 return '<div style="text-align:center"><i class="icon-minus"></i></div>';
	 } else {
		 return obj.GS_ZC_SV;
	 }
 }
 
 function doGS_QT(obj) {
	 if(obj.XMBS == "1") {
		 return '<div style="text-align:center"><i class="icon-minus"></i></div>';
	 } else {
		 return obj.GS_QT_SV;
	 }
 }
 
 function doGS_ZTZ(obj) {
	 if(obj.XMBS == "1") {
		 return '<div style="text-align:center"><i class="icon-minus"></i></div>';
	 } else {
		 return (eval(obj.GS_ZTZ)>eval(obj.TZ_ZTZ))?('<span class="label label-important-orange" title="概算总金额超过估算总金额">'+obj.GS_ZTZ_SV+'</span>'):(obj.GS_ZTZ_SV);
	 }
 }
 /***************** 概算信息 END*********************/
function doGCJE(obj){
	var showHtml = obj.JS_GCJE_SV;
	if(parseFloat(obj.SJB)>0.15&&parseFloat(obj.SJB)<0.2){
		showHtml = "<div id='exception' title='审减率超过15%，少于20%'>"+obj.JS_GCJE_SV+"</div>";
	}else if (parseFloat(obj.SJB)>0.2){
		showHtml = "<div id='error' title='审减率超过20%'>"+obj.JS_GCJE_SV+"</div>";
	}
	return showHtml;
}
 
function showHT_WCTZ(obj) {
	var showHtml = obj.HT_WCTZ;
	if(parseFloat(obj.HT_WCTZ) > parseFloat(obj.HT_ZHTJE)) {
		showHtml = "<span style='background:#FBC77D' title='完成投资金额超过合同总金额'>" + obj.HT_WCTZ_SV + "</span>";
	}
	return showHtml;
}

function showHT_YZF(obj) {
	var showHtml = obj.HT_YZF;
 	if(parseFloat(obj.HT_YZF) > parseFloat(obj.HT_ZHTJE*0.8)) {
		showHtml = "<span style='background:#FBC77D' title='已支付金额超过合同总金额80%'>" + obj.HT_YZF_SV + "</span>";
	}
 	if(parseFloat(obj.HT_YZF) > parseFloat(obj.HT_ZHTJE)) {
		showHtml = "<span style='background:red' title='已支付金额超过合同总金额'>" + obj.HT_YZF_SV + "</span>";
	}
	return showHtml;
}


function moreQuery() {
	var condition = conditionJson == undefined ? "" : "?conditionJson="+encodeURI(conditionJson);
	$(window).manhuaDialog({"title":"项目综合信息-更多条件","type":"text","content":g_sAppName + "/jsp/business/xmzhxxb/xmzhxxb_moreQuery.jsp"+condition,"modal":"2"});
}

//弹出区域回调
getWinData = function(data){
	isMore = true;
	moreQueryData = data.split("|")[0];
	conditionJson = data.split("|")[1];
	var conditionJsonObj = convertJson.string2json1(conditionJson);
	$("#queryForm").setFormValues(conditionJsonObj);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryXmzhxx",moreQueryData, DT1, "setTdBackColor", false);
	isMore = false;
};


//详细信息
function rowView(index) {
	var obj = $("#DT1").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).XMID;
	$(window).manhuaDialog(xmscUrl(id));
}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>	<!-- 	 
						<th width="3%" class="right-border bottom-border text-right">项目名称</th>
						<td width="20%" class="right-border bottom-border">
							<input class="span12" type="text" placeholder="" name="XMMC" fieldname="XMMC" check-type="maxlength" maxlength="500" operation="like" id="XMMC" autocomplete="off">
						</td>  -->
						<th width="2%" class="right-border bottom-border">年度</th>
						<td width="7%" class="right-border bottom-border">
							 <select id="ND" class="span12 year" name="ND" defaultMemo="全部" fieldname="ND" operation="=" ></select> 
						</td>
						<th width="3%" class="right-border bottom-border text-right">项目名称</th>
						<td width="20%" class="right-border bottom-border">
							<input class="span12" type="text" placeholder="" name="XMMC" fieldname="XMMC" check-type="maxlength" maxlength="500" operation="like" id="XMMC" autocomplete="off">
						</td>		 
						<th width="3%" class="right-border bottom-border text-right">标段名称</th>
						<td width="20%" class="right-border bottom-border">
							<input class="span12" type="text" placeholder="" name="BDMC" fieldname="BDMC" check-type="maxlength" maxlength="500" operation="like" id="BDMC" autocomplete="off">
						</td> 
						 
						<td class="text-left bottom-border text-right">
							<span class="pull-left">
								<button	onclick="moreQuery()" class="btn btn-link" type="button"><i class="icon-search"></i>查询更多</button>
							</span>
							
							
							<button	id="example_query" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
	                        <button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
	                        <button class="btn" type="button" id="do_excel">导出</button>
			            </td>
					</tr>
				</table>
			</form>
			<div style="height:5px;"> </div>
			<div class="overFlowX">
			<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" noPage="true" pageNum="1000">
                <thead>		                		
                    <tr>
                     	<th colindex=1 name="XH" id="_XH" rowspan="2" tdalign="center">&nbsp;#&nbsp;</th>	
						<th colindex=2  fieldname="JKZK" rowspan="2" CustomFunction="doJkzk" noprint="true">&nbsp;健康&nbsp;<br>&nbsp;状况&nbsp;</th>
                     	<th colindex=3  fieldname="GTT" rowspan="2" CustomFunction="doGtt" noprint="true">&nbsp;甘特图&nbsp;</th>
						<th colindex=4  fieldname="XMBH" rowspan="2" tdalign="center" rowMerge="true" hasLink="true" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>
						<th colindex=5  fieldname="XMMC" rowspan="2" maxlength="7" rowMerge="true">&nbsp;项目名称&nbsp;</th>
						<th colindex=6  fieldname="BDMC" rowspan="2" maxlength="7" CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
						<th colindex=7  fieldname="JSDZ" rowspan="2" maxlength="9">&nbsp;建设位置&nbsp;</th>
						<th colindex=8  fieldname="XXJT" rowspan="2" tdalign="center" CustomFunction="doDz"></th>
						<th colindex=9  fieldname="XMGLGS" rowspan="2" maxlength="9">&nbsp;项管公司&nbsp;</th>
						<th colindex=10 fieldname="XMXZ" rowspan="2" maxlength="9" tdalign="center">&nbsp;项目性质&nbsp;</th>
						<th colindex=11 fieldname="NDMB" rowspan="2" maxlength="9">&nbsp;年度目标&nbsp;</th>
						<th colspan="5">&nbsp;工程进展&nbsp;</th>
						<th colspan="4">&nbsp;手续办理&nbsp;</th>
						<th colspan="6">&nbsp;设计进展&nbsp;</th>
						<th colindex=27 fieldname="LBJ" rowspan="2" maxlength="15" tdalign="right" CustomFunction="doLBJ">&nbsp;拦标价（元）&nbsp;</th>
						<th colspan="3">&nbsp;招标进展&nbsp;</th>
						<th colspan="4">&nbsp;排迁信息（元）&nbsp;</th>
						<th colspan="6">&nbsp;征收信息（元）&nbsp;</th>
						<th colspan="12">&nbsp;合同信息（元）&nbsp;</th>
						<th colspan="4">&nbsp;年度投资估算（万元）&nbsp;</th>
						<th colspan="4">&nbsp;总体投资估算（万元）&nbsp;</th>
						<th colspan="4">&nbsp;概算信息（元）&nbsp;</th>
						<th colspan="4">&nbsp;资金支付信息（元）&nbsp;</th>
						<th colspan="4">&nbsp;结算信息（元）&nbsp;</th>
                    </tr>
                    <tr>
                    	<!-- 工程进展 -->
						<th colindex=12 tdalign="center" fieldname="GC_YCQ" CustomFunction="doZC">&nbsp;已拆迁&nbsp;</th>
						<th colindex=13 tdalign="center" fieldname="GC_YPQ" CustomFunction="doPQ">&nbsp;已排迁&nbsp;</th>
						<th colindex=14 tdalign="center" fieldname="GC_YKG" CustomFunction="doKG">&nbsp;已开工&nbsp;</th>
						<th colindex=15 tdalign="center" fieldname="GC_YWG" CustomFunction="doWG">&nbsp;已完工&nbsp;</th>
						<th colindex=16 tdalign="center" fieldname="GC_YJJG" CustomFunction="doJG">&nbsp;已交/竣工&nbsp;</th>
						
                    	<!-- 手续办理 -->
						<th colindex=17 tdalign="center" fieldname="SXBL_KYPF" CustomFunction="doKYPF">&nbsp;可研批复&nbsp;</th>
						<th colindex=18 tdalign="center" fieldname="SXBL_TDHB" CustomFunction="doHBJDS">&nbsp;土地划拨&nbsp;</th>
						<th colindex=19 tdalign="center" fieldname="SXBL_GCGHXK" CustomFunction="doGCGHXKZ">&nbsp;工程规划许可&nbsp;</th>
						<th colindex=20 tdalign="center" fieldname="SXBL_SGXK" CustomFunction="doSGXK">&nbsp;施工许可&nbsp;</th>
							
                    	<!-- 设计进展 -->
						<th colindex=21 tdalign="center" fieldname="SJ_SJRWS" CustomFunction="doSjrws">&nbsp;设计任务书&nbsp;</th>
						<th colindex=22 tdalign="center" fieldname="SJ_CSPF" CustomFunction="doCBSJPF">&nbsp;初设批复&nbsp;</th>
						<th colindex=23 tdalign="center" fieldname="SJ_CQT" CustomFunction="doCQT">&nbsp;拆迁图&nbsp;</th>
						<th colindex=24 tdalign="center" fieldname="SJ_PQT" CustomFunction="doPQT">&nbsp;排迁图&nbsp;</th>
						<th colindex=25 tdalign="center" fieldname="SJ_SGT" CustomFunction="doSGT">&nbsp;施工图&nbsp;</th>
						<th colindex=26 tdalign="right"  fieldname="SJ_BGS">&nbsp;变更数&nbsp;</th>
							
                    	<!-- 招标进展 -->
						<th colindex=28 fieldname="ZB_SJDW" maxlength="9" CustomFunction="doZB_SJDW">&nbsp;设计单位&nbsp;</th>
						<th colindex=29 fieldname="ZB_JLDW" maxlength="9" CustomFunction="doZB_JLDW">&nbsp;监理单位&nbsp;</th>
						<th colindex=30 fieldname="ZB_SGDW" maxlength="9" CustomFunction="doZB_SGDW">&nbsp;施工单位&nbsp;</th>
							
                    	<!-- 排迁信息 -->
						<th colindex=31 tdalign="right" fieldname="PQ_RWS_WCS">&nbsp;排迁任务数/完成数&nbsp;</th>
						<th colindex=32 tdalign="right" fieldname="PQ_ZHTJE">&nbsp;总合同金额&nbsp;</th>
						<th colindex=33 tdalign="right" fieldname="PQ_YZF">&nbsp;已支付&nbsp;</th>
						<th colindex=34 tdalign="right" fieldname="PQ_BFB">&nbsp;%&nbsp;</th>
							
                    	<!-- 征收信息 -->
						<th colindex=35 tdalign="right" fieldname="ZS_ZJMS_JWCQ" CustomFunction="showJm">&nbsp;总居民数/已完拆迁&nbsp;</th>
						<th colindex=36 tdalign="right" fieldname="ZS_ZQYS_YWCQ" CustomFunction="showQy">&nbsp;总企业数/已完拆迁&nbsp;</th>
						<th colindex=37 tdalign="right" fieldname="ZS_ZDMJ_YWZD" CustomFunction="showZd">&nbsp;征地面积（m2）/已完征地&nbsp;</th>
						<th colindex=38 tdalign="right" fieldname="ZS_BRZJ" CustomFunction="showBrzj">&nbsp;拨入资金&nbsp;</th>
						<th colindex=39 tdalign="right" fieldname="ZS_YSYZJ" CustomFunction="showSyzj">&nbsp;已使用资金&nbsp;</th>
						<th colindex=40 tdalign="right" fieldname="ZS_YE" CustomFunction="showYe">&nbsp;余额&nbsp;</th>
							
                    	<!-- 合同信息 -->
						<th colindex=41 tdalign="right" fieldname="HT_JQHTS">&nbsp;已签合同数&nbsp;</th>
						<th colindex=42 tdalign="right" fieldname="HT_ZSHTJE">&nbsp;征收合同（虚拟）金额&nbsp;</th>
						<th colindex=43 tdalign="right" fieldname="HT_PQHTJE">&nbsp;排迁合同金额&nbsp;</th>
						<th colindex=44 tdalign="right" fieldname="HT_SJHTJE">&nbsp;设计合同金额&nbsp;</th>
						<th colindex=45 tdalign="right" fieldname="HT_JLHTJE">&nbsp;监理合同金额&nbsp;</th>
						<th colindex=46 tdalign="right" fieldname="HT_SGHTJE">&nbsp;施工合同金额&nbsp;</th>
						<th colindex=47 tdalign="right" fieldname="HT_QTHTJE">&nbsp;其他合同金额&nbsp;</th>
						<th colindex=48 tdalign="right" fieldname="HT_ZHTJE">&nbsp;合同总金额&nbsp;</th>
						<th colindex=49 tdalign="right" fieldname="HT_WCTZ" CustomFunction="showHT_WCTZ">&nbsp;完成投资&nbsp;</th>
						<th colindex=50 tdalign="right" fieldname="HT_BFB1">&nbsp;%&nbsp;</th>
						<th colindex=51 tdalign="right" fieldname="HT_YZF" CustomFunction="showHT_YZF">&nbsp;已支付&nbsp;</th>
						<th colindex=52 tdalign="right" fieldname="HT_BFB2">&nbsp;%&nbsp;</th>
							
                    	<!-- 投资估算（万元） -->
						<th colindex=53 tdalign="right" fieldname="TZ_GC" CustomFunction="doTZ_GC">&nbsp;工程&nbsp;</th>
						<th colindex=54 tdalign="right" fieldname="TZ_ZC" CustomFunction="doTZ_ZC">&nbsp;征拆&nbsp;</th>
						<th colindex=55 tdalign="right" fieldname="TZ_QT" CustomFunction="doTZ_QT">&nbsp;其他&nbsp;</th>
						<th colindex=56 tdalign="right" fieldname="TZ_ZTZ" CustomFunction="doTZ_ZTZ">&nbsp;总投资&nbsp;</th>
							
                    	<!-- 总体投资估算（万元） -->
						<th colindex=57 tdalign="right" fieldname="TZ_GC" CustomFunction="doZTTZ_GC">&nbsp;工程&nbsp;</th>
						<th colindex=58 tdalign="right" fieldname="TZ_ZC" CustomFunction="doZTTZ_ZC">&nbsp;征拆&nbsp;</th>
						<th colindex=59 tdalign="right" fieldname="TZ_QT" CustomFunction="doZTTZ_QT">&nbsp;其他&nbsp;</th>
						<th colindex=60 tdalign="right" fieldname="TZ_ZTZ" CustomFunction="doZTTZ_ZTZ">&nbsp;总投资&nbsp;</th>
							
                    	<!-- 概算信息（元） -->
						<th colindex=61 tdalign="right" fieldname="GS_GC" CustomFunction="doGS_GC">&nbsp;工程&nbsp;</th>
						<th colindex=62 tdalign="right" fieldname="GS_ZC" CustomFunction="doGS_ZC">&nbsp;征拆&nbsp;</th>
						<th colindex=63 tdalign="right" fieldname="GS_QT" CustomFunction="doGS_QT">&nbsp;其他&nbsp;</th>
						<th colindex=64 tdalign="right" fieldname="GS_ZTZ" CustomFunction="doGS_ZTZ">&nbsp;总投资&nbsp;</th>
							
                    	<!-- 资金支付信息（元） -->
						<th colindex=65 tdalign="right" fieldname="ZJZF_GCFY">&nbsp;工程费用&nbsp;</th>
						<th colindex=66 tdalign="right" fieldname="ZJZF_ZCFY">&nbsp;征拆费用&nbsp;</th>
						<th colindex=67 tdalign="right" fieldname="ZJZF_QTFY">&nbsp;其他费用&nbsp;</th>
						<th colindex=68 tdalign="right" fieldname="ZJZF_ZZFFY">&nbsp;总支付费用&nbsp;</th>
							
                    	<!--  结算信息（元） -->
						<th colindex=69 tdalign="right" fieldname="JS_GCJE" CustomFunction='doGCJE'>&nbsp;工程金额&nbsp;</th>
						<th colindex=70 tdalign="right" fieldname="JS_ZCJE">&nbsp;征拆金额&nbsp;</th>
						<th colindex=71 tdalign="right" fieldname="JS_QTJE">&nbsp;其他金额&nbsp;</th>
						<th colindex=72 tdalign="right" fieldname="JS_ZJSJE">&nbsp;总结算金额&nbsp;</th>
                	</tr>
                </thead> 
              	<tbody></tbody>
           </table>
			</div>
		</div>
	</div>		
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="ASC" fieldname ="xmzhxx.XMBH,xmzhxx.XMBS,xmzhxx.PXH"/>
		<input type="hidden" name="resultXML"/>
		<input type="hidden" name="queryResult" id="queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
	</FORM>
	
	<form method="post" action="${pageContext.request.contextPath }/xmzhxxController.do?exportXxzhxx" id="loginForm" >
       <input type="hidden" id="exportQueryResultCondition" name="exportQueryResultCondition">
   </form>
</div>
</body>
</html>