
<%@page import="com.ccthanking.framework.common.DBUtil"%>
<%@page import="java.sql.Connection"%><!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>合同首页</title>
<%
	String type=request.getParameter("type");
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String userid = user.getAccount();
	String username = user.getName();
	String sysdate = Pub.getDate("yyyy-MM-dd");
	
	Connection conn = DBUtil.getConnection();//定义连接
	String sql = "";
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script src="${pageContext.request.contextPath }/js/common/FixTable.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtsjController.do";


var g_pageNum;
//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
//	$("#DT1").attr("pageNum",pageNum);
	g_pageNum = pageNum;
}

//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		normalXm = '';
		exceXm = '';
		errorXm = '';
		allXms = '';
		//if($("#QZBBM").val()!=""){
		////	var v = $("#QZBBM").find("option:selected").text();  
		//	$("#ZBBM").val(v);
		//}
		
		url = controllername+"?queryTzkzdtfx&nd="+$("#QND").val()+"&xmmc="+$('#QXMMC').val();

		if($('input:checkbox[name=JBGSXM]:checked').val()=='1'){
			url+="&gs=1";
		}else{
			url+="&gs=0";
		}
		if($('input:checkbox[name=YBZLBJXM]:checked').val()=='1'){
			url+="&lbj=1";
		}else{
			url+="&lbj=0";
		}
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(url,data,DT1, "doColor", true);
		
		
	});
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		$(window).manhuaDialog({"title":"导出","type":"text","content":"/xmgl/jsp/framework/print/TabListEXP.jsp?tabId=DT1","modal":"3"});
	});
	//按钮绑定事件（清空）
	$("#btnClear").click(function() {
  		//清空查询表单
	    $("#QXMMC").val("");
	    var checkObj = $(":checkbox");
	    for(var i = 0; i < checkObj.length; i++) {
	    	checkObj[i].checked = false;
	    }
	});
	

		//自动完成项目名称模糊查询
		showAutoComplete("QXMMC","${pageContext.request.contextPath }/xmzhxxController.do?xmmcAutoQuery","getXmmcQueryCondition"); 
});




//生成项目名称查询条件
function getXmmcQueryCondition(){
	var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":""}]}';
	var jsonData = convertJson.string2json1(initData); 
 	//项目名称
	if("" != $("#XMMC").val()){
		var defineCondition = {"value": "%"+$("#QXMMC").val()+"%","fieldname":"XMMC","operation":"like","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
    //年度
	if("" != $("#ND").val()){
		var defineCondition = {"value": $("#QND").val(),"fieldname":"ND","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
 	return JSON.stringify(jsonData);
}

function setTbHeight(){
	var height = getDivStyleHeight()-pageTitle-pageQuery-getTableTh(1);
	var pageNum = parseInt(height/pageTableOne,10);
	
	var $thistab = $("#DT1");
	var rows = $("tbody tr" ,$thistab);
	var rows = $("tbody tr" ,$("#DT1"));
     
 	var tr_obj = rows.eq(0);
    var t = $("#DT1").attr("pageNum");
	var tr_height = $(tr_obj).height();
	var d = pageNum*tr_height;
	if(d<150) d = 200;
    // 当高度大于400时，显示主页面列表的高度
    if(d>400) d = height;
    // 当没有数据的时候，只显示表头
	if(tr_height==null) d = getTableTh(1)+20;

	window_width = document.documentElement.clientWidth;//$("#allDiv").width()
	$("#DT1").fixTable({
 		fixColumn: 5,//固定列数
 		width:window_width-10,//显示宽度
 		height:d//显示高度
 	});
	
}
function showException(){
	$("#DT1 tbody tr").each(function(){
		if($(this).attr('id')=='normal'){
			$(this).remove();
		}
	})
}

function setDefaultNd_11(n){
	if(n==""||n==undefined){
		n="ND";
	}
	var obj = $("#"+n);
	//获取当前年
	var date = new Date();
	var sysYear = date.getFullYear();
	var len = obj.find('option').length;
	//遍历select，如果选项中没有当前年，那么年份减1，直到找出存在的年份选项
	var existYear = "";
	for(var x=sysYear;x>0;x--){
		var hasValueFlag = false;
		obj.find("option").each(function(){
			if($(this).text()==x){
				hasValueFlag=true;
				return false;
			}
		});
		if(hasValueFlag==true){
			existYear=x;
			break;
		}
	}
	obj.val(existYear);
}

//页面默认参数
function init(){
	setDefaultNd_11("QND");
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryTzkzdtfx&nd="+$("#QND").val(),data,DT1,"doColor",true);

	//doColor();
	//setTbHeight();
	//getYcxx();

}

function doBD(obj){
	var showHtml = '';
	if(obj.BDMC==null||obj.BDMC==''){
		showHtml='<div style="text-align:center">—</div>';
	}else{
		showHtml='<div style="width:100px;">'+obj.BDMC+'</div>';
	}
	return showHtml;
}
function rowView1(obj){
	//var rowValue = $("#DT1").getSelectedRow();
	$("#resultXML").val(obj);
	$(window).manhuaDialog({"title":"投资控制动态分析>详细信息","type":"text","content":"/xmgl/jsp/business/htgl/sj-tzjkmx.jsp","height":"100%","width":"100%","modal":"1"});

}
function doGS(obj){
	var showHtml = "";
	if(obj.GS==''||obj.GS=='null'){
		showHtml = '<div style="width:150px;text-align:center">—</div>';
	}else{
		showHtml = '<div style="width:150px;">'+obj.GS_SV+'</div>';
	}
	return showHtml;
}

function doCSSDZ(obj){
	var showHtml = "";
	if(obj.CSSDZ==''||obj.CSSDZ=='null'){
		showHtml = '<div style="width:130px;text-align:center">—</div>';
	}else{
<%--			showHtml = '<div style="width:130px;">'+obj.GSSDZ_SV+'</div>';--%>
		showHtml = '<div style="width:130px;">'+obj.CSSDZ_SV+'</div>';
	}
	return showHtml;
}


function doOrderTCJH(obj){
	var showHtml ='';
	if(obj.TCJHNDTZ!=''&&obj.TCJHNDTZ!='null'){
			showHtml = '<div style="width:130px;">'+obj.TCJHNDTZ_SV+'</div>';
	}else{
		showHtml = '<div style="text-align:center">—</div>';
	}
	return showHtml;
}







function doColor(){
	var xmids = '';
	var errorxm = '';
	var exceptionxm = '';
	var errorType = '';
	$("#DT1 tbody tr").each(function(){
		//获取行数据
		var obj = convertJson.string2json1($(this).attr("rowJson"));
		var tdLength = $(this).find('td').length;
		var index = 0;
		var flag = true;	//轻微异常(黄色)
		var flagError = true;	//严重异常(红色)
		//统筹计划投资异常判定
		if(parseFloat(obj.TCJHNDTZ)>parseFloat(obj.JHZTZ)){
			if(tdLength==12){
				index = 5;
				$(this).find('td').eq(index).css('background','#FBC77D');
				$(this).find('td').eq(index).attr('title','统筹计划投资大于计财投资');
			}
				flag = false;
		}
		//概算异常判定
		if(obj.ZGS!=''&&parseFloat(obj.ZGS)>parseFloat(obj.TCJHNDTZ)){
			if(tdLength==12){
				index = 6;
			}else if(tdLength==7){
				index = 1;
			}
			$(this).find('td').eq(index).css('background','#FBC77D');
			$(this).find('td').eq(index).attr('title','概算大于统筹计划投资');
			flag = false;
		}
		//拦标价异常判定
		if(obj.GS==''&&obj.ZXMLBJ!=''&&parseFloat(obj.ZXMLBJ)>parseFloat(obj.TCJHNDTZ)){
			//概算为空
			if(tdLength==12){
				index = 7;
			}else if(tdLength==7){
				index = 2;
			}
			$(this).find('td').eq(index).css('background','#FBC77D');
			$(this).find('td').eq(index).attr('title','拦标价大于统筹计划投资');
			flag = false;
		}else if(obj.GS!=''&&obj.CSSDZ!=''&&parseFloat(obj.CSSDZ)>parseFloat(obj.GS)){
			//概算不为空
			if(tdLength==12){
				index = 7;
				
			}else if(tdLength==7){
				index = 2;
			}
			$(this).find('td').eq(index).css('background','#FF7F7F');
			$(this).find('td').eq(index).attr('title','拦标价大于概算');
			flagError = false;
			flag = false;
		}
		//最新合同价异常判定
		if(parseFloat(obj.ZZXHTJ)>parseFloat(obj.TCJHNDTZ)){
			if(tdLength==12){
				index = 8;
			}else if(tdLength==7){
				index = 3;
			}
			$(this).find('td').eq(index).css('background','#FBC77D');
			$(this).find('td').eq(index).attr('title','最新合同价大于统筹计划投资');
			flag = false;
		}
		if(obj.GS!=''&&parseFloat(obj.ZXHTJ)>parseFloat(obj.GS)){
			if(tdLength==12){
				index = 8;
			}else if(tdLength==7){
				index = 3;
			}
			$(this).find('td').eq(index).css('background','#FF7F7F');
			$(this).find('td').eq(index).attr('title','最新合同价大于概算');
			flag = false;
			flagError = false;
		}
		//完成投资异常判定
		if(parseFloat(obj.WCTZ)>parseFloat(obj.ZXHTJ)){
			if(tdLength==12){
				index = 9;
				
			}else if(tdLength==7){
				index = 4;
			}
			$(this).find('td').eq(index).css('background','#FBC77D');
			$(this).find('td').eq(index).attr('title','完成投资投资大于最新合同价');
			flag = false;
		}
		//支付异常判定
		if(parseFloat(obj.ZF)>parseFloat(obj.ZXHTJ)*0.8&&parseFloat(obj.ZF)<parseFloat(obj.ZXHTJ)){
			if(tdLength==12){
				index = 10 ;
				
			}else if(tdLength==7){
				index = 5;
			}
			$(this).find('td').eq(index).css('background','#FBC77D');
			$(this).find('td').eq(index).attr('title','支付大于最新合同价的80%');
			if(parseFloat(obj.ZF)>parseFloat(obj.WCTZ)){
				$(this).find('td').eq(index).css('background','#FF7F7F');
				$(this).find('td').eq(index).attr('title','支付大于最新合同价的80%并且支付大于完成投资');
				flagError = false;
			}
			flag = false;
		}else if(parseFloat(obj.ZF)>parseFloat(obj.ZXHTJ)){
			if(tdLength==12){
				index = 10;
			}else if(tdLength==7){
				index = 5;
			}
			$(this).find('td').eq(index).css('background','#FF7F7F');
			$(this).find('td').eq(index).attr('title','支付大于最新合同价');
			flag = false;
			if(parseFloat(obj.ZF)>parseFloat(obj.WCTZ)){
				$(this).find('td').eq(index).css('background','#FF7F7F');
				$(this).find('td').eq(index).attr('title','支付大于最新合同价并且支付大于完成投资');
			}
			flagError = false;
		}else if(parseFloat(obj.ZF)>parseFloat(obj.WCTZ)){
			if(tdLength==12){
				index = 10;
			}else if(tdLength==7){
				index = 5;
			}
			$(this).find('td').eq(index).css('background','#FF7F7F');
			$(this).find('td').eq(index).attr('title','支付大于完成投资');
			flag = false;
			flagError = false;
		}
		//结算异常判定
		var js_title='';
		if(parseFloat(obj.JS)>parseFloat(obj.ZXHTJ)){
			if(tdLength==12){
				index = 11;
			}else if(tdLength==7){
				index = 6;
			}
			$(this).find('td').eq(index).css('background','#FBC77D');
			$(this).find('td').eq(index).attr('title','结算价大于最新合同价');
			js_title = '1';
			flag = false;
		}
		if(obj.GS!=''&&parseFloat(obj.JS)>parseFloat(obj.GS)){
			var title = '';
			if(tdLength==12){
				index = 11;
			}else if(tdLength==7){
				index = 6;
			}
			if(js_title==''){
				title = '结算价大于概算';
			}else if(js_title=='1'){
				title = '结算价大于最新合同价并且结算价大于概算';
			}
			$(this).find('td').eq(index).css('background','#FF7F7F');
			$(this).find('td').eq(index).attr('title',title);
			flag = false;
			flagError = false;
		}
		
		//状态
		if(!flag){
			if(!flagError){
				if(errorXm.indexOf(obj.XMID)==-1){
					errorXm+=(obj.XMID+',');
				}
				
			}else{
				if(exceptionxm.indexOf(obj.XMID)==-1){
					exceptionxm+=(obj.XMID+',');
				}
			}
		}else if(flag){
			if(xmids.indexOf(obj.XMID)==-1){
				xmids+=(obj.XMID+',');
			}
		}
	})
	
	for(i=0;i<xmids.split(',').length;i++){
		var xm = xmids.split(',')[i];
		if(exceptionxm.indexOf(xm)==-1&&errorxm.indexOf(xm)==-1&&xm!=''){
			normalXm += (xm+',');
		}
	}
	for(i=0;i<exceptionxm.split(',').length;i++){
		var xm = exceptionxm.split(',')[i];
		if(errorXm.indexOf(xm)==-1&&xm!=''){
			exceXm += (xm+',');
		}
	}
	
	showErrorLv();
	if($('input:checkbox[name=YCXM]:checked').val()=='1'){
		showException();
	}
	setTbHeight();
}
var normalXm = '';
var exceXm = '';
var errorXm = '';
var allXms = '';
function showErrorLv(){
		$("#DT1 tbody tr").each(function(){
			var obj = convertJson.string2json1($(this).attr("rowJson"));
			if(allXms.indexOf(obj.XMID)==-1){
				var txt = '';
				if(exceXm.indexOf(obj.XMID)!=-1){
					txt = '<span class="label label-warning">异常</span>';
				}else if(errorXm.indexOf(obj.XMID)!=-1){
					txt = '<span class="label label-danger">异常</span>';
				}else if(normalXm.indexOf(obj.XMID)!=-1){
					txt = '<span class="label label-success">正常</span>';
				}
				$(this).find('td').eq(0).html(txt);
				$(this).find('td').eq(1).html("<a href=javascript:rowView1('"+$(this).attr("rowJson")+"'); title='查看详细信息' xmid='"+obj.XMID+"'><i class='icon-file showXmxxkInfo' xmid='"+obj.XMID+"' jhsjid='"+obj.ID+"'></i></a>");
				allXms += obj.XMID+',';
			}
			if(exceXm.indexOf(obj.XMID)!=-1){
				$(this).attr('id','exec');
			}else if(errorXm.indexOf(obj.XMID)!=-1){
				$(this).attr('id','error');
			}else if(normalXm.indexOf(obj.XMID)!=-1){
				$(this).attr('id','normal');
			}
		})
}
function doZTZ(obj){
	return '<div style="width:150px;">'+obj.JHZTZ_SV+'</div>';
}
function doZXHTJ(obj){
	return '<div style="width:130px;">'+obj.ZXHTJ_SV+'</div>';
}
function doWCTZ(obj){
	return '<div style="width:130px;">'+obj.WCTZ_SV+'</div>';
}
function doZF(obj){
	return '<div style="width:130px;">'+obj.ZF_SV+'</div>';
}
function doJS(obj){
	return '<div style="width:130px;">'+obj.JS_SV+'</div>';
}
function doMC(obj){
	return '<div style="width:150px;">'+obj.XMMC+'</div>';
}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">
				投资控制动态分析
				<span class="pull-right">
					<button id="btnExpExcel" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">导出</button>
				</span>
			</h4>
			<form method="post" id="queryForm1">
			</form>
			<form method="post" id="queryForm">
 				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>
						<th width="5%" class="right-border bottom-border text-right">年度</th>
						<td width="10%" class="right-border bottom-border">
							<select class="span12" id="QND" >
								<option name="">全部</option>
							<%
								StringBuffer sbSql = new StringBuffer();
				        		sbSql.append("select distinct(nd) from gc_tcjh_xmxdk order by  nd asc ");
				        		
				        		sql = sbSql.toString();
				         		String[][] nf_Array = DBUtil.query(conn, sql);
				         		if(null !=nf_Array && nf_Array.length>0){
				 	        		for(int i=0;i<nf_Array.length;i++){
				        	
				        	%>
										<option  name="<%= nf_Array[i][0]%>"><%= nf_Array[i][0]%></option>
							<% 
				        			}
				         		}
				        	%>
						</select>
						</td>
						<th width="5%" class="right-border bottom-border text-right">项目名称</th>
				        <td width="20%" class="right-border bottom-border">
				            <input  class="span12" type="text" id="QXMMC" name="XMMC" style="width:90%"/>
				        </td>
				        <td width="15%" class="right-border bottom-border" >
				       	 	<label class="checkbox">
				       	 		具备概算项目
				       	 		<input type="checkbox" name="JBGSXM" id='JBGSXM' value="1"/>
				       	 	</label>
				        </td>
				         <td width="15%" class="right-border bottom-border" >
				         	<label class="checkbox">
				         		异常项目
				         		<input type="checkbox" name="YCXM" id='ycxm' value='1' />
				         	</label>
				        </td>
				        <td width="15%" class="right-border bottom-border" >
				            <label class="checkbox">
				            	已编制拦标价项目
				            	<input type="checkbox" name="YBZLBJXM" id="YBZLBJXM" value="1"/>
            				</label>
				        </td>
				        
						<td class="right-border bottom-border text-right" width="73%" style="background-image: none; background-position: initial initial; background-repeat: initial initial;">
	                        <button id="btnQuery" class="btn btn-link"  type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-search"></i>查询</button>
           					<button id="btnClear" class="btn btn-link" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-trash"></i>清空</button>
			            </td>
					</tr>
				</table>
			</form>
			<div style="height:5px;"> </div>	
			<div class="overFlowX">
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" noPage="true" pageNum="1000">
	                <thead>
	                	<tr>
	                		<th name="XH" id="_XH" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
	                		<th fieldname="XMBH" colindex=2 tdalign="center" noprint="true" rowmerge="true">&nbsp;状态&nbsp;</th>
	                		<th fieldname="XMBH" colindex=3 tdalign="center" rowMerge="true" hasLink="true">&nbsp;&nbsp;</th>
	                		<th fieldname="XMMC" colindex=4 tdalign="left" maxlength="15" rowmerge="true" customfunction="doMC">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDMC" style="width:80px" colindex=5 tdalign="left" maxlength="10" customfunction="doBD">&nbsp;标段名称&nbsp;</th>
							<th fieldname="JHZTZ" style="width:160px" colindex=6 tdalign="right"  maxlength="18" rowmerge="true" customfunction="doZTZ">&nbsp;计划总投资(元)&nbsp;</th>
							<th fieldname="TCJHNDTZ" colindex=7 tdalign="right" maxlength="18" rowmerge="true" customfunction="doOrderTCJH">&nbsp;统筹计划投资(元)&nbsp;</th>
							<th fieldname="GS" colindex=8 tdalign="right" maxlength="18" customfunction="doGS">&nbsp;概算(元)&nbsp;</th>
							<th fieldname="CSSDZ" colindex=9 tdalign="right" maxlength="18" customfunction="doCSSDZ">&nbsp;拦标价(元)&nbsp;</th>
							<th fieldname="ZXHTJ" colindex=10 tdalign="right" maxlength="18" customfunction="doZXHTJ">&nbsp;最新合同价(元)&nbsp;</th>
							<th fieldname="WCTZ" colindex=11 tdalign="right" maxlength="18" customfunction="doWCTZ">&nbsp;完成投资(元)&nbsp;</th>
							<th fieldname="ZF" colindex=12 tdalign="right" maxlength="18" customfunction="doZF">&nbsp;支付(元)&nbsp;</th>
							<th fieldname="JS" colindex=13 tdalign="right" maxlength="18" customfunction="doJS">&nbsp;结算(元)&nbsp;</th>
	                	</tr>
	                </thead>
	                <tbody></tbody>
	           </table>
	       </div>
	 	</div>
	</div>     
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display: none;" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" fieldname="gjs.xmid" id="txtFilter"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
</div>
</body>
</html>