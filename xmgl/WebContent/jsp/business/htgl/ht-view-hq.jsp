<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.coreapp.aplink.*"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>合同-维护</title>
<%
	String type=request.getParameter("type");//操作类型
	String sjbh=request.getParameter("sjbh");
	String ywlx=request.getParameter("ywlx");
	String spbh=request.getParameter("spbh");

	
	Connection conn = DBUtil.getConnection();
	com.ccthanking.framework.coreapp.aplink.Process process = null;
	Step nowStep = null;
	int stepindex = 0;
	String oid = null;

	try {
		process = TaskBO.getProcess(conn, spbh);
		oid = process.getOperationOID();
		nowStep = process.open();
		stepindex = nowStep.getStepSequence();
	} catch (Exception ex) {
		ex.printStackTrace();
	} finally {
		if (conn != null) {
			conn.close();
		}
	}
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtController.do";
var controllernameHtsj= "${pageContext.request.contextPath }/htgl/gcHtglHtsjController.do";
var controllername_zjgl= "${pageContext.request.contextPath }/zjgl/gcZjglZszjXmzszjqkController.do";
//页面初始化
$(function() {
	$("#SJBH").val('<%=sjbh%>');
	init();
	$(":input").each(function(i){
		   $(this).attr("disabled", "true");
		 });
	<% if("43485".equals(oid)) { %>// 43485表示的是新的合同会签单，wsid为28，会签单会签顺序改变了
		<% if(stepindex==11) { %>
		 $("#btnQueryGroup").removeAttr("disabled","disabled");
		 $("#HTBM").removeAttr("disabled","disabled");
		<% } else if(stepindex==6 || stepindex==5) { %>
		 	$("#btnQueryGroup").removeAttr("disabled","disabled");
			$("#JFID").removeAttr("disabled","disabled");
		<% } %>
	<% } else if("43463".equals(oid)) { %>// 43463表示的是老的合同会签单，wsid为5
		<% if(stepindex==3) { %>
		 $("#btnQueryGroup").removeAttr("disabled","disabled");
		 $("#HTBM").removeAttr("disabled","disabled");
		<% } else if(stepindex==2) { %>
		 	$("#btnQueryGroup").removeAttr("disabled","disabled");
			$("#JFID").removeAttr("disabled","disabled");
		<% } %>
	<% } %>
	var btn = $("#btnQueryGroup");
	btn.click(function() {
		
		var htbh = $("#HTBM").val();

		var jfid = $("#JFID").val();
		

		<% if("43485".equals(oid)) { %>// 43485表示的是新的合同会签单，wsid为28，会签单会签顺序改变了
			<% if(stepindex==11) { %>
				if(!htbh||htbh=="") {
					 xAlert("提示信息","请输入合同编号！",'3');
					return;
				}
			<% } else if(stepindex==6 || stepindex==5) { %>
				if(!jfid||jfid=="") {
					 xAlert("提示信息","请输入甲方单位！",'3');
					return;
				}
			<% } %>
		<% } else if("43463".equals(oid)) { %>// 43463表示的是老的合同会签单，wsid为5
			<% if(stepindex==3) { %>
				if(!htbh||htbh=="") {
					 xAlert("提示信息","请输入合同编号！",'3');
					return;
				}
			<% } else if(stepindex==2) { %>
				if(!jfid||jfid=="") {
					 xAlert("提示信息","请输入甲方单位！",'3');
					return;
				}
			<% } %>
		<% } %>
		
		var data = Form2Json.formToJSON(gcHtglHtForm);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			//调用ajax插入
			defaultJson.doInsertJson(controllername + "?updateHtbh&stepindex=<%=stepindex %>&oid=<%=oid %>", data1,null);
			//alert($("#ID").val())

	});


});
function querySjrws_ZTB(ztbId){
	var url =  controllername+"?queryZtbSjrws&ztbId="+ztbId;
	daoQuery(url);
}
function querySjrws_XM(jhsjids){
	var jhsjid = "";
	for(i=0;i<jhsjids.split(',').length;i++){
		if(jhsjids.split(',')[i]!=''){
			jhsjid += "'"+jhsjids.split(',')[i]+"',";
		}
	}
	var url =  controllername+"?queryZtbSjrws&jhsjids="+jhsjid;
	daoQuery(url);
}
//履约保证金信息
function queryLvbzj(ztbId){
	$.ajax({	
		url : controllername+"?queryZtbLybzj",	
		data : {ztbId:ztbId},	
		cache : false,	
		async :	false,	
		dataType : "json",
		type : 'post',		
		success : function(response) {	
			//$("#resultXML").val(response.msg);	
			var resultobj = defaultJson.dealResultJson(response);	
			if(resultobj!=null&&resultobj.JE!=null&&resultobj.JE!=""){
				$("#LYBZJ_JNJE").val(resultobj.JE);
				$("#LYBZJ_JNBS").val(resultobj.BS);
			}else{
				$("#LYBZJ_JNJE").val(0);
				$("#LYBZJ_JNBS").val(0);
			}
		}
	});
}
//查询设计任务书
function daoQuery(url){
	$("#SHOW_SJRWS_title").show();
	$("#SHOW_SJRWS").show();
	$("#SHOW_SJRWS").html('');
	$.ajax({	
		url : url,	
		cache : false,	
		async :	false,	
		dataType : "json",
		type : 'post',		
		success : function(response) {	
			if(response!=null&&response!=''&&response!='0'){
				var resultmsgobj = convertJson.string2json1(response);
				$.each(resultmsgobj.response.data,function(i,item){
					var htmlStr ='<tr><th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>'
						+'<td width="17%" class="right-border bottom-border">'
						+'<input id="SJ_BDMC" value="'+item.BDMC+'" class="span12" style="width:85%;text-align:left;" name="SJ_BDMC" fieldname="SJ_BDMC" min="0" type="text" disabled/></td>'
						+'<th width="8%" class="right-border bottom-border text-right disabledTh">设计任务书</th>'
						+'<td width="17%" class="right-border bottom-border">'
						+'<input id="SJ_FS" class="span9" style="width:70%;" value="'+item.ZS+'" name="SJ_FS" fieldname="SJ_FS" type="number" disabled="disabled"><b>(份)</b>'
						+'</td></tr>';
					$("#SHOW_SJRWS").append(htmlStr);
				})
			}else{
				$("#SHOW_SJRWS_title").hide();
				$("#SHOW_SJRWS").hide();
				$("#SHOW_SJRWS").html('');
			}
		}
	});
}
var htlx;
//页面默认参数
function init(){
	queryFileData("","","<%=sjbh %>","<%=ywlx%>");
	//生成json串
	//表单赋值
	var data = combineQuery.getQueryCombineData(queryForm,frmPost);	
	var data1 = {
		msg : data
	};	
	
	var flagSFDXMHT;//是否单项目合同控制
	var flagZTBID;//是否有招投标关联
	
	//计算使用和拨付笔数
	$.ajax({	
		url : controllername+"?queryBySJBH",	
		data : data1,	
		cache : false,	
		async :	false,	
		dataType : "json",
		type : 'post',		
		success : function(response) {
			$("#resultXML").val(response.msg);	
			var resultobj = defaultJson.dealResultJson(response.msg);
			if(resultobj.HTLX=="SG"){
				$("#bzjTitle").show();
				$("#bzjBody").show();
			}else{
				$("#bzjTitle").hide();
				$("#bzjBody").hide();
			}
			$("#gcHtglHtForm").setFormValues(resultobj);
			
			
			$("#gcHtglHtForm #ZTBID").val(resultobj.ZTBMC);
			$("#gcHtglHtForm #ZTBID").attr('code',resultobj.ZTBID);
			
			if(resultobj.ZTBID==""){
				//非招投标关联
				$("#show_ztb").hide();
				flagZTBID = false;
			}else{
				$("#show_ztb").show();
				flagZTBID = true;
			}
			
			if(resultobj.SFDXMHT=="0"){
				//多项目合同
				//$("#dxmTR").hide();
			 	$("#aaa").show();
			 	flagSFDXMHT = false;
			}else{
				//单项目合同
				//$("#dxmTR").show();
			 	$("#aaa").hide();
			 	flagSFDXMHT = true;
			}
			if(resultobj.HTLX=='SG'){
				$("#bxjTitle").show();
				$("#bxjTb").show();
				if(flagZTBID && $("#JNDW").val()==""){//如果现有保证金中单位为空，则通过指投标处获取
				//	queryLybzjxx(resultobj.ID);
				}
			}
			if(resultobj.HTLX=="PQ"){
				$("#SHOW_PQZRW_title").show();
				$("#SHOW_PQZRW").show();
				$("#pqzxmid").val(resultobj.GC_PQ_ZXM_ID);
				$("#PQZRW_GXLB").val(resultobj.GXLB_SV);
				$("#PXZRW_RWMC").val(resultobj.ZXMMC);
			}else{
				$("#SHOW_PQZRW_title").hide();
				$("#SHOW_PQZRW").hide();
			}
			htlx = resultobj.HTLX;
		}
	});
	//显示招投标相关信息
	if($("#ZTBID").val()==""){
		$("#show_ztb").hide();
	}else{
		//本合同是和招投标关联的  判断显示
	 	//$("#aaa").show();
	}
	//显示更多乙方 单位
	if($("#YF2ID").val()!=""||$("#YF3ID").val()!=""){
		$("#otherYFDW2").show();
		$("#addYfdw").remove();
	}
	
	
	//查询记录数
	var dataHtsj3 = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtsjList);
	//调用ajax插入
	var flagrs = defaultJson.doQueryJsonList(controllernameHtsj+"?queryOne",dataHtsj3,gcHtglHtsjList, null, true);
	if(flagrs && flagSFDXMHT){
		var obj1 =$("#gcHtglHtsjList tbody tr").eq(0).attr("rowJson");
	 	var obj = convertJson.string2json1(obj1);
	 	//var obj = $("#DT2").getRowJsonObjByIndex(1);
		//$("#XMMC").val(obj.XMMC);
		//$("#BDMC").val(obj.BDMC);
		//$("#sel_xmxx").attr("disabled", true);
		//$("#sel_xmxx").hide();
	}
	if(htlx=="SJ"){
		var jhsjids = "";
		$("#gcHtglHtsjList tbody tr").each(function(i){
			//alert(i);
			var rowValue = $(this).attr("rowJson");
			var value = convertJson.string2json1(rowValue).JHSJID;
			jhsjids+=value+",";
		})
		querySjrws_XM(jhsjids);
	}
	if(htlx=='PQ'){
		queryPxxmxx($("#ID").val());
	}
}

function queryPxxmxx(htid){
	$.ajax({
		url : controllername_zjgl+"?queryCommon&opttype=pq&qc_htid="+htid,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(response) {
			var resultmsgobj  = convertJson.string2json2(response.msg);
			if(null!=resultmsgobj){
			$("#pqzxmid").val(resultmsgobj.GC_PQ_ZXM_ID);
			$("input:radio[name=HTSX][value='"+resultmsgobj.HTSX+"']")[0].checked = true;
			//$("#HTSX").val(resultmsgobj.HTSX);
			$("#PQZRW_GXLB").val(resultmsgobj.GXLB_SV);
			$("#PXZRW_RWMC").val(resultmsgobj.ZXMMC);
			if(resultmsgobj.HTSX=='1'){
				$("#ZRWJE").val(resultmsgobj.SSZ==''?'0':resultmsgobj.SSZ);
				$("#htsx_text").text('预算值');
			}else if(resultmsgobj.HTSX=='2'){
				$("#ZRWJE").val(resultmsgobj.SDZ==''?'0':resultmsgobj.SDZ);
				$("#htsx_text").text('审定值');
			}
			}
		}
	});
}
function queryLybzjxx(htid){
	$.ajax({
		url : controllername_zjgl+"?queryCommon&opttype=lybzj&qc_htid="+htid,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(response) {
			var resultmsgobj  = convertJson.string2json2(response.msg);
			if(null!=resultmsgobj){
				$("#JNDW").val(resultmsgobj.DWMC);
				//$("#JNRQ").val(resultmsgobj.JNRQ);
				$("#JE").val(resultmsgobj.JE);
				$("input:radio[name=JNFS][value='"+resultmsgobj.JNFS+"']")[0].checked = true;
			}
		}
	});
}

function returnJFDW(obj){
	$("#JFDWID").val($(obj).find("option:selected").text());
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
	<div class="B-small-from-table-autoConcise" >
		<% if("43485".equals(oid)) { %><!-- // 43485表示的是新的合同会签单，wsid为28，会签单会签顺序改变了 -->
			<%if(stepindex == 11) { %>
		      <h4 class="title">
				<span class="pull-right">  
					<button id="btnQueryGroup" class="btn" type="button" >保存合同编号</button>
				</span>
			  </h4>
			 <% } else if(stepindex == 6 || stepindex==5) { %>
			  <h4 class="title">
				<span class="pull-right">  
					<button id="btnQueryGroup" class="btn" type="button" >保存甲方单位</button>
				</span>
			  </h4>
			 <% } %>
		<% } else if("43463".equals(oid)) { %><!-- // 43463表示的是老的合同会签单，wsid为5 -->
			<%if(stepindex == 3) { %>
		      <h4 class="title">
				<span class="pull-right">  
					<button id="btnQueryGroup" class="btn" type="button" >保存合同编号</button>
				</span>
			  </h4>
			 <% } else if(stepindex == 2) { %>
			  <h4 class="title">
				<span class="pull-right">  
					<button id="btnQueryGroup" class="btn" type="button" >保存甲方单位</button>
				</span>
			  </h4>
			 <% } %>
	 	<% } %>
     
      
	</div>
	<div style="display:none;">
	 <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      		<INPUT type="text" class="span12" kind="text" id="SJBH" name="SJBH"  fieldname="ghh.SJBH" value="" operation="="/>
      <!--可以再此处加入hidden域作为过滤条件 -->
      		<INPUT type="text" class="span12" kind="text" id="QID" name="ID"  fieldname="ghh.ID" value="" operation="="/>
		<INPUT type="text" class="span12" kind="text" id="QHTSJID" name="HTSJID"  fieldname="ghh2.ID" value="" operation="="/>
      </table>
      </form>
	</div>

	<div class="B-small-from-table-autoConcise">
     <form method="post" id="gcHtglHtForm"  >
		
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
      	<input type="hidden" id="AlerdyHTSJFlag" value = "false"/></TD>
      	<input type="hidden" id="QDNF" fieldname="QDNF" name = "QDNF"/></TD>
      	
      	<input type="hidden" id="YFID" name="YFID"  fieldname="YFID" >
      	<input type="hidden" id="JFDWID" name="JFDW"  fieldname="JFDW" >
      	<input type="hidden" id="ZBBMID" name="ZBBMID"  fieldname="ZBBMID" >
      	<input type="hidden" id="JFJBRID" name="JFJBRID"  fieldname="JFJBRID" >
      	<input type="hidden" id="JFQDRID" name="JFRDRID"  fieldname="JFRDRID" >
      	
      	<input type="hidden" id="YFDW2ID" name="YF2ID"  fieldname="YF2ID" >
		<input type="hidden" id="YFDW3ID" name="YF3ID"  fieldname="YF3ID" >
	    <input type="hidden" id="SJBH" name="SJBH"  fieldname="SJBH" value="<%=sjbh %>" >
		
      	
      	<tr id="show_ztb" style="display:black;">
			<th width="8%" class="right-border bottom-border text-right disabledTh">招投标名称</th>
			<td width="17%" class="right-border bottom-border">
			<input id="ZTBID"  style="width:88%" class="span12" name="ZTBID" fieldname="ZTBID" type="text" val="" code="" disabled />
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">招标方式</th>
			<td width="17%" class="right-border bottom-border">
				<select  id="FBFS"   class="span6"  name="FBFS" fieldname="FBFS"  operation="=" kind="dic" src="ZBFS" defaultMemo="-请选择-" disabled>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">合同名称</th>
			<td width="17%" class="right-border bottom-border">
				<input id="HTMC" style="width:88%" placeholder="必填" check-type="required" class="span12" check-type="maxlength" maxlength="1000"  name="HTMC" fieldname="HTMC" type="text" />
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">合同编码</th>
			<td width="17%" class="right-border bottom-border">
				<input id="HTBM" style="width:70%"  class="span12" check-type="maxlength" maxlength="100"  name="HTBM" fieldname="HTBM" type="text" />
				<select  id="HTLX"   class="span6"  name="HTLX" fieldname="HTLX"  kind="dic" src="HTLX" style="display:none;">
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">是否虚拟合同</th>
			<td width="17%" class="right-border bottom-border">&nbsp;&nbsp;
			    <input class="span12" id="SFXNHT" type="radio" placeholder="" kind="dic" src="SF" name = "SFXNHT" fieldname="SFXNHT">
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">合同签订日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="HTJQDRQ"  class="span5" style="width:70%;" fieldtype="date" fieldformat="YYYY-MM-DD"  placeholder="必填" check-type="required"  name="HTJQDRQ" fieldname="HTJQDRQ" type="date" />
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">合同实际开始日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="HTSJKSRQ"  class="span9" style="width:70%;" fieldtype="date" fieldformat="YYYY-MM-DD"  name="HTSJKSRQ" fieldname="HTSJKSRQ" type="date" />
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh"><span title="合同实际结束日期">合同实际结束日期</span></th>
			<td width="17%" class="right-border bottom-border">
				<input id="HTJSRQ"  class="span9" style="width:70%;" fieldtype="date" fieldformat="YYYY-MM-DD" name="HTJSRQ" fieldname="HTJSRQ" type="date" />
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">合同签订价</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZHTQDJ" value=0 class="span12" name="ZHTQDJ" fieldname="ZHTQDJ" type="number"  min="0" disabled style="width:88%;text-align:right;"/><b>(元)</b>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">报价系数</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BJXS" value=0 class="span12" name="BJXS" fieldname="BJXS" type="number"  min="0" style="width:88%;text-align:right;"/>
			</td>
		</tr>
		
		</table>
		<h4 class="title">项目及标段信息</h4>
		<table class="B-table" width="100%">
		<tr>
			<td colspan="4" align="center">
			<div class="overFlowX">
				<table class="table-hover table-activeTd B-table"  id="gcHtglHtsjList" width="100%" type="single" noPage="true">
					<thead>
						<tr>
			 			    <th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
							<th fieldname="XMMC" colindex=2 tdalign="center" maxlength="30" >&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDMC" colindex=3 tdalign="center" maxlength="30" >&nbsp;标段名称&nbsp;</th>
							<th fieldname="BDBH" colindex=3 tdalign="center" maxlength="30" >&nbsp;标段编号&nbsp;</th>
							<th fieldname="HTQDJ" colindex=5 tdalign="right" >&nbsp;合同签订价(元)&nbsp;</th>
							<th fieldname="GCJGBFB" colindex=6 tdalign="center" >&nbsp;价格百分比(%)&nbsp;</th>
			             </tr>
					</thead>
					<tbody>
			           </tbody>
			</table>
			</div>
			</td>
		</tr>
		</table>
		
			<h4 class="title" id="bzjTitle">履约保证金情况</h4>
			<table class="B-table" width="100%" id="bzjBody">
				<tr>
					<th width="8%" class="right-border bottom-border text-right disabledTh">交纳单位</th>
					<td width="17%" class="right-border bottom-border">
						<input id="JNDW" class="span12" name="JNDW" style="width:85%" fieldname="BZJ_JNDW" type="text" disabled/>
					</td>
					<th width="8%" class="right-border bottom-border text-right disabledTh">交纳日期</th>
					<td width="17%" class="right-border bottom-border">
						<input style="width:45%;" id="JNRQ" class="span7" type="date" check-type="maxlength" maxlength="10" name="JNRQ" fieldname="BZJ_JNRQ" disabled/>
					</td>
				</tr>
				<tr>
					<th width="8%" class="right-border bottom-border text-right disabledTh">金额</th>
					<td width="17%" class="right-border bottom-border">
						<input id="JE" class="span9"  value=0 style="width:70%;text-align:right;"  check-type="maxlength" maxlength="17" name="JE" fieldname="BZJ_JE" type="number" disabled/><b>(元)</b>
					</td>
					<th width="8%" class="right-border bottom-border text-right disabledTh">交纳方式</th>
					<td width="17%" class="right-border bottom-border">
						<input class="span12" id="JNFS" type="radio" placeholder="" kind="dic" src="JNFS" name = "JNFS" fieldname="BZJ_JNFS_SV">
					</td>
				</tr>
			</table>
		<h4 id="bxjTitle" style="display:none;" class="title">保修金信息</h4>
		<table id="bxjTb" style="display:none;" class="B-table" width="100%">
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">保修金额</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BXJE"  style="width:70%;text-align:right;" value=0 class="span12"   placeholder="必填" check-type="required"  name="BXJE" fieldname="BXJE" type="number" disabled/><b>(元)</b>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">保修金率</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BXJL" style="width:70%;text-align:right;"  value=0 class="span12"  placeholder="必填" check-type="required"  name="BXJL" fieldname="BXJL" type="number" disabled/>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">保修期</th>
			<td width="17%" class="right-border bottom-border" colspan="3">
				<input id="BXQ" value=0 class="span9" style="width:28%;text-align:right;"  placeholder="必填" check-type="required"  name="BXQ" fieldname="BXQ" type="number" disabled/>
				<select  id="BXQDWLX" style="width:10%;"  class="span6"  name="BXQDWLX" fieldname="BXQDWLX"  operation="=" kind="dic" src="BXQDW"  defaultMemo="-请选择-" disabled>
			</td>
		</tr>
		</table>
		<h4 id="SHOW_PQZRW_title" class="title" style="display:none;" >排迁子任务</h4>
		<table id="SHOW_PQZRW" class="B-table" width="100%" style="display:none;">
			<tr>
				<th width="8%" class="right-border bottom-border text-right disabledTh">管线类别</th>
				<td width="17%" class="right-border bottom-border">
					<input id="PQZRW_GXLB" class="span12"  style="width:85%" name="PQZRW_XMMC" type="text" disabled/>
				</td>
				<th width="8%" class="right-border bottom-border text-right disabledTh">排迁任务名称</th>
				<td width="17%" class="right-border bottom-border">
					<input id="PXZRW_RWMC" class="span12" name="PXZRW_RWMC" style="width:85%" type="text" disabled/>
					<button class="btn btn-link"  type="button" onclick="openPqrwQueryPage('callPq');"><i class="icon-edit"></i></button>
				</td>
			</tr>
			<tr>
				<th width="8%" class="right-border bottom-border text-right">合同属性</th>
				<td width="17%" class="right-border bottom-border">&nbsp;&nbsp;
					 <input class="span12" onclick="showHTSX();" id="HTSX" type="radio" placeholder="" kind="dic" src="HTSX" name = "HTSX" fieldname="HTSX">
				</td>
				<th width="8%" class="right-border bottom-border text-right disabledTh" ><span id="htsx_text"></span></th>
				<td width="17%" class="right-border bottom-border">
					<input id="ZRWJE" value=0 class="span12" style="width:70%;text-align:right;" name="ZRWJE" min="0" type="number" disabled/><b>(元)</b>
				</td>
			</tr>
		</table>
		<h4 id="SHOW_SJRWS_title" class="title" style="display:none" >设计任务书信息</h4>
		<table id="SHOW_SJRWS" class="B-table" width="100%" style="display:none">
			
		</table>
<%--		<h4 class="title" id="bgtitle">合同变更信息</h4>--%>
<%--			<table class="B-table" width="100%" id="bgtable">--%>
<%--				<tr>--%>
<%--					<th width="8%" class="right-border bottom-border text-right disabledTh">变更价</th>--%>
<%--					<td width="17%" class="right-border bottom-border">--%>
<%--						<input id="ZBGJE"  style="width: 88%" class="span12" check-type="maxlength" maxlength="200"  name="ZBGJE" fieldname="ZBGJE" type="text" readOnly/><b>(元)</b>--%>
<%--					</td>--%>
<%--					<th width="8%" class="right-border bottom-border text-right disabledTh">变更率</th>--%>
<%--					<td width="17%" class="right-border bottom-border">--%>
<%--						<input id="BGL"  style="width: 88%" class="span12" check-type="maxlength" maxlength="200"  name="BGL" fieldname="BGL" type="text" readOnly/>--%>
<%--					</td>--%>
<%--				</tr>--%>
<%--			</table>--%>
<%--			<h4 class="title" id="jstitle">合同结算信息</h4>--%>
<%--			<table class="B-table" width="100%" id="jstable">--%>
<%--				<tr>--%>
<%--					<th width="8%" class="right-border bottom-border text-right disabledTh">结算日期</th>--%>
<%--					<td width="17%" class="right-border bottom-border">--%>
<%--						<input id="ZJSRQ"  class="span9" fieldtype="date" fieldformat="YYYY-MM-DD"  placeholder="必填" check-type="required"  name="ZJSRQ" fieldname="ZJSRQ" type="date" disabled/>--%>
<%--					</td>--%>
<%--					<th width="8%" class="right-border bottom-border text-right disabledTh">结算价</th>--%>
<%--					<td width="17%" class="right-border bottom-border">--%>
<%--						<input id="ZHTJS"  style="width: 88%" class="span12" check-type="maxlength" maxlength="200"  name="ZHTJS" fieldname="ZHTJS" type="text" readOnly /><b>(元)</b>--%>
<%--					</td>--%>
<%--				</tr>--%>
<%--			</table>--%>
<%--			<h4 class="title">合同签订日期</h4>--%>
<%--			<table class="B-table" width="100%">--%>
<%--				<tr>--%>
<%--					<th width="8%" class="right-border bottom-border text-right disabledTh">签订日期</th>--%>
<%--					<td width="14%" class="right-border bottom-border">--%>
<%--						<input id="HTJQDRQ"  class="span9" fieldtype="date" fieldformat="YYYY-MM-DD" check-type="required"  name="HTJQDRQ" fieldname="HTJQDRQ" type="date" readonly/>--%>
<%--					</td>--%>
<%--					<th width="8%" class="right-border bottom-border text-right disabledTh">合同开始日期</th>--%>
<%--					<td width="14%" class="right-border bottom-border">--%>
<%--						<input id="HTSJKSRQ"  class="span9" fieldtype="date" fieldformat="YYYY-MM-DD" check-type="required"  name="HTSJKSRQ" fieldname="HTSJKSRQ" type="date" readonly/>--%>
<%--					</td>--%>
<%--					<th width="8%" class="right-border bottom-border text-right disabledTh">合同结束日期</th>--%>
<%--					<td width="14%" class="right-border bottom-border">--%>
<%--						<input id="HTJSRQ"  class="span9" fieldtype="date" fieldformat="YYYY-MM-DD"  check-type="required"  name="HTJSRQ" fieldname="HTJSRQ" type="date" disabled/>--%>
<%--					</td>--%>
<%--				</tr>--%>
<%--			</table>--%>
		<h4 class="title">合同签订信息</h4>
		<table class="B-table" width="100%">
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">甲方单位</th>
			<td width="17%" class="right-border bottom-border">
				<select id="JFID" check-type="required" class="span6" onchange="returnJFDW(this)" name="JFID" fieldname="JFID" kind="dic" src="JFDW" style="width:88%" disabled></select>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">甲方签订人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="JFQDR"   class="span12" check-type="maxlength" maxlength="100"  name="JFQDR" fieldname="JFQDR" type="text" style="width:88%" disabled/>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">主办部门</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZBBM"   placeholder="必填" check-type="required" class="span12" check-type="maxlength" maxlength="36"  name="ZBBM" fieldname="ZBBM" type="text" style="width:88%" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">甲方经办人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="JFJBR"   class="span12" check-type="maxlength" maxlength="36"  name="JFJBR" fieldname="JFJBR" type="text" style="width:88%" disabled/>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">乙方单位</th>
			<td width="17%" class="right-border bottom-border">
				<input id="YFDW"  class="span12" style="width:88%" name="YFDW" fieldname="YFDW" type="text" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">乙方单位签订人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="YFDWQDR" style="width:88%" class="span12" check-type="maxlength" maxlength="100"  name="YFDWQDR" fieldname="YFDWQDR" type="text" disabled/>
			</td>
		</tr>
			<tr id="otherYFDW2" style="display:none">
				<th width="8%" class="right-border bottom-border text-right disabledTh">丙方单位</th>
				<td width="17%" class="right-border bottom-border">
					<input id="YFDW2" style="width: 88%"  class="span12" name="YFDW2" fieldname="YFDW2" type="text" />
				</td>
				<th width="8%" class="right-border bottom-border text-right disabledTh">丙方单位签订人</th>
				<td width="17%" class="right-border bottom-border">
					<input id="YFDWQDR2"   class="span12" check-type="maxlength" maxlength="100" style="width:88%"  name="YFDWQDR2" fieldname="YFDWQDR2" type="text" />
				</td>
			</tr>
			<tr id="otherYFDW3" style="display:none">
				<th width="8%" class="right-border bottom-border text-right">丁方单位</th>
				<td width="17%" class="right-border bottom-border">
					<input id="YFDW3" style="width: 88%"  class="span12" name="YFDW3" fieldname="YFDW3" type="text" />
				</td>
				<th width="8%" class="right-border bottom-border text-right">丁方单位签订人</th>
				<td width="17%" class="right-border bottom-border">
					<input id="YFDWQDR3" class="span12" check-type="maxlength" maxlength="100"  name="YFDWQDR3" fieldname="YFDWQDR3" type="text" />
				</td>
			</tr>
		</table>
		<h4 class="title">合同内容及其它信息</h4>
		<table class="B-table" width="100%">
		
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">合同主要内容</th>
			<td colspan="3" class="bottom-border right-border" >
				<textarea class="span12" rows="2" id="HTZYNR" check-type="maxlength" maxlength="4000" fieldname="HTZYNR" name="HTZYNR" disabled></textarea>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">付款方式</th>
			<td colspan="3" class="bottom-border right-border" >
				<textarea class="span12" rows="2" id="HTFKFS" check-type="maxlength" maxlength="4000" fieldname="HTFKFS" name="HTFKFS" disabled></textarea>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">违约条款</th>
			<td colspan="3" class="bottom-border right-border" >
				<textarea class="span12" rows="2" id="HTWYTK" check-type="maxlength" maxlength="4000" fieldname="HTWYTK" name="HTWYTK" disabled></textarea>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">结束依据</th>
			<td colspan="3" class="bottom-border right-border" >
				<textarea class="span12" rows="2" id="HTZSYJ" check-type="maxlength" maxlength="4000" fieldname="HTZSYJ" name="HTZSYJ" disabled></textarea>
			</td>
		</tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right disabledTh">备注</th>
	        <td colspan="3" class="bottom-border right-border" >
	        	<textarea class="span12" rows="2" id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ" disabled></textarea>
	        </td>
        </tr>
         <tr>
        	<th width="8%" class="right-border bottom-border text-right disabledTh">相关附件</th>
        	<td colspan="3" class="bottom-border right-border">
				<div>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0701" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery" onlyView="true"></tbody>
					</table>
				</div>
			</td>
        </tr>
      </table>
      </div>
      </form>
    </div>
   </div>
  </div>
  <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
  <div align="center">
 	<FORM id="frmPost1" name="frmPost"  method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "ghh.LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
<script>
</script>
</html>