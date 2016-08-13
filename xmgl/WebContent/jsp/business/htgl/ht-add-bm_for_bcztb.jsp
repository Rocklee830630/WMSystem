<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>合同-维护</title>
<%
	String type=request.getParameter("type");//操作类型
	
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDeptFullName();
	String userid = user.getAccount();
	String username = user.getName();
	String roelsString = user.getRoleListString();
	boolean cwcFlag = false;
	if("100000000002".equals(deptId) || roelsString.indexOf("财务部")!=-1){
	    cwcFlag = true;
	}
	
	String cYear = Pub.getDate("yyyy");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtController.do";
var controllernameHtsj= "${pageContext.request.contextPath }/htgl/gcHtglHtsjController.do";
var controllernameZtbXmxx= "${pageContext.request.contextPath }/ZhaotoubiaoController.do";
var controllername_zjgl= "${pageContext.request.contextPath }/zjgl/gcZjglZszjXmzszjqkController.do";
var depID;
var jbrID;
var xmrowValues;//
var ztbId;
var bdChange=false;
var p_oldBjxs = '';
var p_oldHtlx = '';
var p_type = '<%=type%>';
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,gcHtglHtList);
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		var dxm = $("input:radio[name=SFDXMHT]:checked").val();
		var lx = $("#HTLX").val();
		if(dxm=='2'){
			//无项目合同
			$("#DT2").clearResult();
			$("#jhsjids").val('');
			$("#xmids").val('');
			$("#bdids").val('');
			$("#htsjje").val('');
			$("#XMMC").val('');
			$("#BDMC").val('');
		}
		if(lx=="PQ") {
			if($("#pqzxmid").val()=="") {
				xInfoMsg("未关联排迁子任务",null);
				return;
			}
		}
		if(dxm!="2"&&lx!="PQ"&&$("#jhsjids").val()==''&&$("#ZTBID").attr("code")==''){
			var msg = '未关联任何项目或标段！';
			if($("#DT2 tbody tr").length==0){
				xInfoMsg(msg,null);
	        	return
			}
		}
		
		if(lx=="SG" || lx=="JL") {
			if($("#ZTBID").attr("code")=='') {
				xInfoMsg("施工合同、监理合同必须关联招投标需求",null);
	        	return
			}
		}
		if($("#ID").val() != "" && $("#ID").val() != null){
			if($("#XMMC").val==''){
				var msg = '未关联任何项目或标段！';
				if($("#DT2 tbody tr").length==0){
					xInfoMsg(msg,null);
		        	return
				}
			}
		}
		if($("#gcHtglHtForm").validationButton())
		{
			$("#btnSave").attr('disabled',true);
			var ztbname = $("#ZTBID").val();
			//var sj = $("#HTJQDRQ").val();
			//var ye = sj.substring(0,4);
			<%
				if("insert".equals(type)){
					%>
					$("#QDNF").val('<%=cYear%>');
					<%					
				}
			%>
			
			if(lx == "SG") {
				if(iscf()) {
					$("#btnSave").attr('disabled',false);
					return;
				}
			}
			
		    //生成json串
		    var data = Form2Json.formToJSON(gcHtglHtForm);
		  //组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  	//调用ajax插入
		    if($("#ID").val() == "" || $("#ID").val() == null){
		    	var valywid = $("#ywid").val();
    			p_oldHtlx = $("#HTLX").val();
    			p_oldBjxs = $("#BJXS").val();
    			var flag = defaultJson.doInsertJson(controllername + "?insert&ywid="+valywid+"&xmxxfrom="+xmxxfrom+"&ids="+$("#jhsjids").val()+"&xmids="+$("#xmids").val()+"&bdids="+$("#bdids").val()+"&pqzxmid="+$("#pqzxmid").val()+"&bzjId="+$("#lybzjId").val(), data1 , null, 'insertHt');
    			if(flag){
		    		var json=$("#resultXML").val();
		    		var tempJson=eval("("+json+")");
		    		var resultobj=tempJson.response.data[0];
		    		
		    		//$("#ID").val(obj.ID);
		    		$("#gcHtglHtForm").setFormValues(resultobj);
		    		$("#ZTBID").val(ztbname);
		    		$("#ZTBID").attr('code',resultobj.ZTBID);
		    		$("#btnXMBD").attr("disabled", false);
		    		$("#id_choseZTB").attr("disabled","disabled");
		    		
		    		setFileData(resultobj.ID,"","","");
    				queryFileData(resultobj.ID,"","","");
    				
    				$("#btnClear_Bins").remove();
    				$("#example1").remove();
		    	}
    			
    			//$("#gcHtglHtForm").clearFormResult();
    		}else{
    			p_oldHtlx = $("#HTLX").val();
    			p_oldBjxs = $("#BJXS").val();
    			defaultJson.doUpdateJson(controllername + "?update&bdChange="+bdChange+"&xmxxfrom="+xmxxfrom+"&ids="+$("#jhsjids").val()+"&xmids="+$("#xmids").val()+"&bdids="+$("#bdids").val()+"&pqzxmid="+$("#pqzxmid").val()+"&bzjId="+$("#lybzjId").val(), data1, null, 'updateHt');
    		}
		}else{
			requireFormMsg();
		  	return;
		}
	});
	
	//按钮绑定事件(修改)
	$("#btnXMBD").click(function() {
		g_btnNum = 5;
		$(window).manhuaDialog({"title":"合同项目/标段>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/htsj-indexOne.jsp?type=update","modal":"2"});
	});
	$("#btnZF").click(function() {
		$(window).manhuaDialog({"title":"合同支付>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtzf-indexOne.jsp?type=update","modal":"2"});
	});
	$("#btnWCTZ").click(function() {
		$(window).manhuaDialog({"title":"合同完成投资>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtwctz-indexOne.jsp?type=update","modal":"2"});
	});
	$("#btnBG").click(function() {
		$(window).manhuaDialog({"title":"合同变更>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtbg-indexOne.jsp?type=update","modal":"2"});
	});
	$("#btnWJ").click(function() {
		$(window).manhuaDialog({"title":"合同文件>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtwj-indexOne.jsp?type=update","modal":"2"});
	});


	//按钮绑定事件（新增）
    $("#btnClear_Bins").click(function() {
        $("#gcHtglHtForm").clearFormResult();
        $("#gcHtglHtForm").cancleSelected();
        
        
        $("#ZFRQ").val(new Date().toLocaleDateString());
        $("#ZFJE").val(0);
        $("#ID").val("");
        
        $("#DT2").clearResult();
        xmrowValues = "";
        xmxxfrom="";
        $("#jhsjids").val("");
        $("#example1").attr("disabled",false);
        $("#ZBBM").val('<%=deptName%>');
	    $("#JFJBR").val('<%=username%>');
	    $("#BXQDWLX").val('1');
	    $("#HTZT").val('-3');
	    
	    $("#ZBBMID").val('<%=deptId%>');
	    $("#JFJBRID").val('<%=userid%>');
        
    });
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
	
    
    $("button[id^='deptBtn_']").bind("click", function(){
    	var valu = $(this).attr("value");
    	depID = valu;
    	var deptCode = $("#"+valu).attr("code");
    	openDeptTree('single',deptCode,'setDept') ;
	});
    
    $("button[id^='blrBtn_']").bind("click", function(){
    	var valu = $(this).attr("value");
    	jbrID = valu;
    	var actorCode = $("#"+valu).attr("code");
		openUserTree('single',actorCode,'setBlr') ;
	});
    

    $("#xmxx").click(function() {
    	$("#aaa").slideToggle("fast");
    });
    
    //选择项目标段
    $("#example1").click(function() {
    	$(window).manhuaDialog({"title":"合同信息>选择项目标段","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/xmmore.jsp","modal":"1"});
    });
    $("#btnRemove").click(function(){
    	
    	//获取id
		var ids = "";//计划数据id串
		var rowValues=new Array();//rowjson array
		var rowid = $("#DT2").getSelectedRowIndex();
		if(rowid==-1)
		 {
			requireSelectedOneRow();
		    return false;
		 }
		$("#DT2").removeResult(rowid);
		
		$("#DT2 tbody tr").each(function(i){
			var rowValue = $(this).attr("rowJson");
			rowValues[i]=rowValue;
			var value = convertJson.string2json1($(this).attr("rowJson")).GC_JH_SJ_ID;
			ids+=value+",";
		});	
		
	    fanhuixiangm(rowValues,ids);
    })
});

//页面默认参数
function init(){
	setDefaultNd("QQDNF");
	//生成json串
	<%	
		if("insert".equals(type)){
	%>
			$("#btnXMBD").remove();
		    $("#ZBBM").val('<%=deptName%>');
		    $("#JFJBR").val('<%=username%>');
		    $("#ZBBMID").val('<%=deptId%>');
		    $("#JFJBRID").val('<%=userid%>');
		//    $("#JFDWID").val('长春市政府投资建设项目管理中心');
		    
		    $("#BXQDWLX").val('1');//保修期单位类型  1年
		    $("#HTZT").val('-3');
		    
		    $("input:radio[name=SFXNHT][value='0']").attr("checked",true);
		    $("input:radio[name=SFDXMHT][value='1']").attr("checked",true);
		    $("input:radio[name=SFZFJTZ][value='1']")[0].checked=true;
		    $("input:radio[name=HTSX][value='1']").attr("checked",true);
		    $("#htsx_text").text("预算值");
	<%
		}else if("update".equals(type)){
	%>
	
		$(":input").attr("disabled",true);
		$("#xzztbxx").attr("disabled", false);
		$("#xzztbsc").attr("disabled", false);
		$("#ConfirmYesButton").attr("disabled", false);
		$("#ConfirmCancleButton").attr("disabled", false);
		
		//$("#btnXMBD").hide();
		$("#btnXMBD").show();
		//$("#example1").remove();
		$("#btnRemove").remove();
	
		var parentmain=$(window).manhuaDialog.getParentObj();	
		var rowValue = parentmain.$("#resultXML").val();
		var tempJson = convertJson.string2json1(rowValue);
		
		$("#QID").val(tempJson.ID);
		$("#QHTSJID").val(tempJson.HTSJID);
		
		var flagSFDXMHT;//是否单项目合同控制
		var flagZTBID;//是否有招投标关联
		var SFDXMHT_str;//SFDXMHT数值
		//查询表单赋值
		var data = combineQuery.getQueryCombineData(queryForm,frmPost);
		var data1 = {
			msg : data
		};
		$.ajax({
			url : controllername+"?query",
			data : data1,
			cache : false,
			async :	false,
			dataType : "json",  
			type : 'post',
			success : function(response) {
				$("#resultXML").val(response.msg);
				var resultobj = defaultJson.dealResultJson(response.msg);
		    	p_oldBjxs = resultobj.BJXS;
		    	p_oldHtlx = resultobj.HTLX;
				$("#gcHtglHtForm").setFormValues(resultobj);
				$("#gcHtglHtForm #ZTBID").val(resultobj.ZTBMC);
				$("#gcHtglHtForm #ZTBID").attr('code',resultobj.ZTBID);
				SFDXMHT_str = resultobj.SFDXMHT;
				
				if(resultobj.HTLX=="SG"){//施工合同
					$("#SHOW_SGLHT_title").show();
					$("#SHOW_SGLHT").show();
				}

				if(resultobj.HTLX=="SJ"){//设计合同
					flagSJHT = true;
				}

				if(resultobj.HTLX=="PQ"){
					$("#SHOW_PQZRW_title").show();
					$("#SHOW_PQZRW").show();
					$("#pqzxmid").val(resultobj.GC_PQ_ZXM_ID);
					$("#PQZRW_GXLB").val(resultobj.GXLB_SV);
					$("#PXZRW_RWMC").val(resultobj.ZXMMC);
					queryFileData(resultobj.GC_PQ_ZXM_ID,"","","");
				}
				if(resultobj.ZTBID==""){
					//非招投标关联
					$("#show_ztb").hide();
					flagZTBID = false;
				}else{
					ztbId = resultobj.ZTBID;
					$("#show_ztb").show();
					flagZTBID = true;
					if(resultobj.HTLX=='SG'){
						$("#SHOW_LYBZJ_title").show();
						$("#SHOW_LYBZJ").show();
						//$("#id_choseBZJ").hide();
					}
					xmxxfrom = "choseztb";
				}

				
				if(resultobj.SFDXMHT=="0"){
					//多项目合同
					$("#dxmTR").hide();
				 	$("#aaa").show();
				 	flagSFDXMHT = false;
				 	$("#xmxx").show();
				 	//$("#btnXMBD").show();
				}else if(resultobj.SFDXMHT=='1'){
					//单项目合同
					$("#dxmTR").show();
				 	$("#aaa").hide();
				 	flagSFDXMHT = true;
				 	//$("#btnXMBD").remove();
				 	$("#xmxx").hide();
				 	//$("#btnXMBD").show();
				}else{
					flagSFDXMHT = false;
					//无项目合同
					$("#dxmTR").hide();
					$("#aaa").hide();
					$("#xmxx").hide();
					//$("#btnXMBD").remove();
				}
				
			}
		});
		
		
		 //查询项目标段信息
<%--		 var ZTBSJID = $("#ZTBID").val();--%>
<%--		 if(ZTBSJID!=""){--%>
<%--			 var dataxmxx = combineQuery.getQueryCombineData(queryFormXmxx,frmPost,DT2);--%>
<%--			 //调用ajax插入--%>
<%--			 defaultJson.doQueryJsonList(controllernameZtbXmxx+"?queryZhaoBiaoXiangMu&ZTBSJID="+ZTBSJID,dataxmxx,DT2);--%>
<%--			 --%>
<%--			 $("#aaa").show();--%>
<%--		 }--%>
//合同修改时， 获取项目信息

		if(flagZTBID){
			$("input:radio[name=SFDXMHT]").attr('disabled','disabled');
			$("#sel_xmxx").attr('disabled','disabled');
			$("#sel_xmxx").hide();
			$("#ZHTQDJ").attr('disabled','disabled');
			$("#ZHTQDJ_th").addClass("disabledTh");
		}
		 if (SFDXMHT_str!="2" && typeof(tempJson) != "undefined"){
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT2);
			var flagrs =  defaultJson.doQueryJsonList(controllernameHtsj+"?queryOne",data,DT2,null, true);
			var rowValues=new Array();
			var jhsjids="";
			$("#DT2 tbody tr").each(function(i){
				var rowValue = $(this).attr("rowJson");
				rowValues[i]=rowValue;
				var value = convertJson.string2json1(rowValue).JHSJID;
				jhsjids+=value+",";
			})
			xmrowValues=rowValues;
			if(flagrs && flagSFDXMHT){
				var obj1 =$("#DT2 tbody tr").eq(0).attr("rowJson");
				if(obj1!=null){
					var obj = convertJson.string2json1(obj1);
				 	//var obj = $("#DT2").getRowJsonObjByIndex(1);
					$("#XMMC").val(obj.XMMC);
					$("#BDMC").val(obj.BDMC);
					//$("#sel_xmxx").attr("disabled", true);
					//$("#sel_xmxx").hide();
					//if(flagSJHT)querySjrws_XM(obj.JHSJID);
					$('#jhsjids').val(obj.JHSJID);
				}
				$('#example1').hide();
			}else{
				if(jhsjids!='')$("#xmxx").show();
				$('#jhsjids').val(jhsjids);
				$('#example1').show();
			}
		 }
		
		
		$("#btnXMBD").attr("disabled", false);
		
		deleteFileData(tempJson.ID,"","","");
		setFileData(tempJson.ID,"",tempJson.SJBH,tempJson.YWLX,"1");
		queryFileData(tempJson.ID,"","","");
		
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
		showSubInfo();
		if($("#HTLX").val()=='PQ'){
			queryPxxmxx($("#ID").val());
		}
		//置SFDXMHT disabled
		//$("input:radio[name=SFDXMHT]").each(function(i){
		//	$(this).attr("disabled", true);
		// });
	 <%
		}
	%>
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
				$("#SSZ").val(resultmsgobj.SSZ);
				$("#SDZ").val(resultmsgobj.SDZ);
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
function refreshPage(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT2);
	var flagrs =  defaultJson.doQueryJsonList(controllernameHtsj+"?queryOne",data,DT2,null, true);
	var rowValues=new Array();
	$("#DT2 tbody tr").each(function(i){
		var rowValue = $(this).attr("rowJson");
		rowValues[i]=rowValue;
	})
	xmrowValues=rowValues;
}
function addXM(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT2);
	var flagrs =  defaultJson.doQueryJsonList(controllernameHtsj+"?queryOne",data,DT2,null, true);
}

//点击行事件
function tr_click(obj, tid){
	//alert(JSON.stringify(obj));
	if(tid=="DT2"){
		$("#btnRemove").attr("disabled",false);
	}
}


function insertHt(){
	var fuyemian1=$(window).manhuaDialog.getParentObj();
	fuyemian1.gengxinchaxun('insert');
	$("#btnSave").attr('disabled',false);
}
function updateHt(){
	refreshPage();
	var fuyemian1=$(window).manhuaDialog.getParentObj();
	fuyemian1.gengxinchaxun('update');
	$("#btnSave").attr('disabled',false);
}


//详细信息
function rowView(index){
	var obj = $("#gcHtglHtList").getSelectedRowJsonByIndex(index);
	var xmbh = eval("("+obj+")").XMBH;
	$(window).manhuaDialog(xmscUrl(xmbh));
}

function closeNowCloseFunction(){
	var fuyemian=$(window).manhuaDialog.getParentObj();
	fuyemian.queryList();
	return true;
}

function showOtherYfdw(){
	$("#otherYFDW2").toggle();
}

//设置部门
function setDept(data){
	var Id = "";
	var Name = "";
	for(var i=0;i<data.length;i++){
 	 var tempObj =data[i];
 	 if(i<data.length-1){
 		Id +=tempObj.DEPTID+",";
 		Name +=tempObj.DEPTNAME+",";
 	 }else{
 		Id +=tempObj.DEPTID;
 		Name +=tempObj.DEPTNAME; 
 	 }
	}
	$("#"+depID).val(Name);
	$("#"+depID+"ID").val(Id);
}

function setBlr(data){
	var userId = "";
	var userName = "";
	for(var i=0;i<data.length;i++){
 	 var tempObj =data[i];
 	 if(i<data.length-1){
	  userId +=tempObj.ACCOUNT+",";
	  userName +=tempObj.USERNAME+",";
 	 }else{
 	  userId +=tempObj.ACCOUNT;
 	  userName +=tempObj.USERNAME; 
 	 }
	}
	$("#"+jbrID).val(userName);
	$("#"+jbrID+"ID").val(userId);
}

//判断合同显示
function showSubInfo(){
	$("#pqzxmid").val("");
	var v = $("#HTLX").val();
	if(v!=null && v!=""){
		
		$("#SHOW_SGLHT").hide();
		$("#SHOW_SGLHT_title").hide();
		$("#SHOW_PQZRW_title").hide();
		$("#SHOW_PQZRW").hide();
		$("#SHOW_SGLHT_title").hide();
		$("#SHOW_SGLHT").hide();
		$("#SHOW_SJRWS_title").hide();
		$("#SHOW_SJRWS").hide();
		$("#SHOW_SJRWS").html('');
		if(v==p_oldHtlx){
			$("#BJXS").val(p_oldBjxs);
		}else if(v=="PQ"){
			$("#BJXS").val("1");
		}else{
			$("#BJXS").val("0");
		}
		
		if(v.indexOf("SG")!=-1){//施工合同
			$("#SHOW_SGLHT_title").show();
			$("#SHOW_SGLHT").show();
			var ztbId = $("#ZTBID").attr("code");
			if(ztbId!=''){
				$("#SHOW_LYBZJ_title").show();
				$("#SHOW_LYBZJ").show();
				//queryLvbzj(ztbId);
			}else{
				$("#LYBZJ_JNJE").val('');
				$("#LYBZJ_JNRQ").val('');
				$("#lybzjId").val('');
			}
			//支付即投资默置为
			//置SFZFJTZ disabled
			$("input:radio[name=SFZFJTZ]").each(function(i){
				$(this).attr("disabled", true);
			});
			$("input:radio[name=SFZFJTZ][value='0']")[0].checked=true;
		}else{
			$("input:radio[name=SFZFJTZ]").each(function(i){
				$(this).attr("disabled", false);
			});
		}

		if(v.indexOf("SJ")!=-1){
			//设计合同,显示设计任务书
			var ztbId = $("#ZTBID").attr("code");

			var jhsjids = $("#jhsjids").val();
			if(ztbId!=''){
				querySjrws_ZTB(ztbId);
			}else if(jhsjids!=''){
				querySjrws_XM(jhsjids);
			}
		}
		
		if(v.indexOf("PQ")!=-1){
			$("#SHOW_PQZRW_title").show();
			$("#SHOW_PQZRW").show();
		}
	}
	
}

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
		url : controllername+"?queryLybzjList",	
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
//招投标
function xzZtb() {

	var parentmain=$(window).manhuaDialog.getParentObj();	
	var rowValue = parentmain.$("#resultXML").val();
	var tempJson = convertJson.string2json1(rowValue);
	
	var htid = tempJson.ID;
	$(window).manhuaDialog({"title" : "选择招投标信息","type" : "text","content" : "${pageContext.request.contextPath}/jsp/business/htgl/ztbQuery.jsp?bmht=false&htid="+htid,"modal":"4"});
}

function scxm(){
	var action = controllername + "?deleteHTxm";
    var data1 = "htid="+$("#ID").val();
    var sghtIscf = false;

	xConfirm("信息提示","是否确认删除合同中的项目？");
	$('#ConfirmYesButton').unbind();
 	$('#ConfirmYesButton').attr('click','');
	$('#ConfirmYesButton').one("click",function(){
		$.ajax({
			type : 'post',
			url : action,
			data : data1,
			dataType : 'json',
			async :	false,
			success : function(result) {
				xInfoMsg("操作成功!");
			},
		    error : function(result) {
		    	xAlertMsg(1);
			}
		});
	});
	return sghtIscf;
}

//履约保证金
function xzLybzj() {
	$(window).manhuaDialog({"title" : "选择履约保证金信息","type" : "text","content" : "${pageContext.request.contextPath}/jsp/business/htgl/lybzjQuery.jsp","modal":"4"});
}


function xzZtbSingle() {
	$(window).manhuaDialog({"title":"项目管理>选择项目","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/xmmoreSingle.jsp","modal":"1"});
}
//回调履约保证金信息
function selectLybzj(rowValue){
	var tempJson = convertJson.string2json1(rowValue);
	if(tempJson!=null&&tempJson!=''){
		$("#LYBZJ_JNJE").val(tempJson.JE);
		$("#LYBZJ_JNRQ").val(tempJson.JNRQ);
		$("#lybzjId").val(tempJson.ID);
	}
}

function tishi(rowValue){
	xAlert(rowValue);
	init();
	$("#xzztbxx").attr("disabled", true);
}
//回调招投标信息
function selectZtb(rowValue){
	bdChange = true;
	var tempJson = convertJson.string2json1(rowValue);
	//var mc = tempJson.ZTBMC;
	ztbId = tempJson.GC_ZTB_SJ_ID;
	$("#ZTBID").val(convertJson.string2json1(rowValue).ZTBMC);
	$("#ZTBID").attr("code", tempJson.GC_ZTB_SJ_ID);
	$("#FBFS").val(tempJson.ZBFS);
	$("#YFID").val(tempJson.DSFJGID);
	$("#YFDW").val(tempJson.DSFJGID_SV);//乙方单位
	$("#ZHTQDJ").val(tempJson.ZZBJ);
	$("#BJXS").val(tempJson.BJXS);
	
	$("#ZHTQDJ").attr("readOnly", true);
	$("#ZHTQDJ_th").addClass("disabledTh");
	
	xmxxfrom = "choseztb";
	$("#DT2").clearResult();
	
	//查询项目标段信息
	 var ZTBSJID = tempJson.GC_ZTB_SJ_ID;
	 if(ZTBSJID!=""){
		 var dataxmxx = combineQuery.getQueryCombineData(queryFormXmxx,frmPost,DT2);

		 
		 //调用ajax插入
		 var flag_ztbbdxx = defaultJson.doQueryJsonList(controllernameZtbXmxx+"?queryZhaoBiaoXiangMu&ZTBSJID="+ZTBSJID,dataxmxx,DT2, null, true);
		 if(flag_ztbbdxx){
			 var xmids = "";
			 var bdids = "";
			 var ids = "";
			 
			 $("#DT2 tbody tr").each(function(i){
				var rowValue = $(this).attr("rowJson");
				var xmid = convertJson.string2json1($(this).attr("rowJson")).XMID;
				var bdid = convertJson.string2json1($(this).attr("rowJson")).BDID;
				var value = convertJson.string2json1($(this).attr("rowJson")).GC_JH_SJ_ID;
				ids+=value+",";
				xmids+=xmid+",";
				bdids+=bdid+",";
			 });
			 
			 $("#btnRemove").hide();
			 $("#example1").hide();
	
			 if($("#DT2 tbody tr").length==1){//单项目合同
				 $("input:radio[name=SFDXMHT][value='1']")[0].checked=true;
	
			 	 
			 	 $("#dxmTR").show();
			 	 $("#aaa").hide();
			 	 
			 	 $("#sel_xmxx").attr("disabled", true);
			     $("#sel_xmxx").hide();
			 	 
			 	var obj1 =$("#DT2 tbody tr").eq(0).attr("rowJson");
			 	var obj = convertJson.string2json1(obj1);
			 	//var obj = $("#DT2").getRowJsonObjByIndex(1);
				$("#XMMC").val(obj.XMMC);
				$("#BDMC").val(obj.BDMC);
				 $('#ZHTQDJ').removeAttr('disabled');
			 }else if($("#DT2 tbody tr").length>1){//多项目合同
				$("input:radio[name=SFDXMHT][value='0']")[0].checked=true;
			
			 
				
				$("#dxmTR").hide();
				//多项目标段信息不可操作
				$("#btnRemove").hide();
				$("#example1").hide();
			 	$("#aaa").show();
		 	 }else{
		 		$("#XMMC").val('');
				$("#BDMC").val('');
			 }
			 $("#jhsjids").val(ids);
			 $("#xmids").val(xmids);
			 $("#bdids").val(bdids);
		 }

		 //履约保证金
		 var v = $("#HTLX").val();
			
		if(v.indexOf("SG")!=-1){//施工合同
			$("#SHOW_LYBZJ_title").show();
			$("#SHOW_LYBZJ").show();
		}

		if(v.indexOf("SJ")!=-1){//施工合同
			querySjrws_ZTB(ZTBSJID);
		}
		$("input:radio[name=SFDXMHT]").attr('disabled','disabled');
	 }
}

var g_btnNum = -1;
function xzxm(n) {
	g_btnNum = n;
	$(window).manhuaDialog({"title" : "单位选择","type" : "text","content" : "${pageContext.request.contextPath}/jsp/business/gcb/cjdw/cjdw_Query_Add.jsp","modal":"4"});
}
var xmxxfrom;//控制数据是从哪个地方来的，招投标或者项目选择
//子页面调用 设置选择行array信息及 jhsjid主键串
function fanhuixiangm(rowValues,ids)
{
	$("#show_ztb").hide();
	$("#dxmTR").hide();
	
	var xmids = "";
	var bdids = "";
	var jes = "";
	var sumje = 0;
	 //清空列表
	 $("#DT2").find("tbody").children().remove();
	// alert(rowValues.length);
	 xmrowValues=rowValues;
	 
	 for(var i=0;i<rowValues.length;i++)
	 {
		xmids += convertJson.string2json1(rowValues[i]).XMID+',';
		bdids += convertJson.string2json1(rowValues[i]).BDID+',';
		jes += convertJson.string2json1(rowValues[i]).JE+',';
		sumje += parseFloat(convertJson.string2json1(rowValues[i]).JE);
	    $("#DT2").insertResult(rowValues[i],DT2,1);
	    $("#singleName").val(convertJson.string2json1(rowValues[i]).XMMC);
	 }
	 if(rowValues.length==1){
		 $("input:radio[name=SFDXMHT][value='1']")[0].checked=true;
		 
		 $('#ZHTQDJ').removeAttr('disabled');
	 }else if(rowValues.length>1){
		 $("input:radio[name=SFDXMHT][value='0']")[0].checked=true;
		 $('#ZHTQDJ').attr('disabled','disabled');
	 }
	 $("#jhsjids").val(ids);
	 $("#xmids").val(xmids);
	 $("#bdids").val(bdids);
	 $("#htsjje").val(jes);
	 //$("#ZHTQDJ").val(sumje);
	 //$("#ZHTQDJ").val($("#ZHTQDJ").val()==''?'0':$("#ZHTQDJ").val())
	 $("#id_choseZTB").attr("disabled","disabled");

	 var v = $("#HTLX").val();
	 if(v.indexOf("SJ")!=-1){//施工合同
		querySjrws_XM(ids);
	}
	 if(v == "SG"){//施工合同
		 if(iscf()) {
			 return;
		 }
	}
	xmxxfrom = "chosexm";

	bdChange = true;
	 //xmxxfrom = "choseztb";
}
function getArr()
{
	 return xmrowValues;
}

//打开标段选择按钮
function showDxmht1(t){
 	
 	if(t=="0" || t=="1"){
 	 	if(t=="0"){
 	 		bdChange = false;
 	 		$("#aaa").show();
 	 		$("#example1").show();
 	 	}else{
 	 		$("#aaa").hide();
 	 	}
 	 	if(t=="1"){
 	 	 	bdChange = true;
 	 		$("#dxmTR").show();
 	 		$("#xmxx").hide();
 	 	}else{
 	 		$("#dxmTR").hide();
 	 		$("#xmxx").show();
 	 	}
		//$("#btnXMBD").show();
 	}else{
 		bdChange = true;
 		$("#btnXMBD").hide();
 		$("#dxmTR").hide();
 		$("#aaa").hide();
 		$("#xmxx").hide();
 	}
}

//控制是否单项目合同情况
function showDxmht(){
	$("#DT2").clearResult();
	$("#jhsjids").val('');
	$("#xmids").val('');
	$("#bdids").val('');
	$("#htsjje").val('');
	xmrowValues="";
	var val = $("input:radio[name=SFDXMHT]:checked").val();
	if(val=="0"){
		//多项目合同
		$("#XMMC").val('');
		$("#BDMC").val('');
	
		$("#aaa").show();
		$("#xmxx").show();
		$("#example1").attr('disabled',false);
		$("#dxmTR").hide();
		$('#ZHTQDJ').attr('disabled','disabled');
		$("#id_choseZTB").removeAttr('disabled');
	}else if(val=="1"){
		//单项目合同
		$("#aaa").hide();
		
		//$("#DT2").find("tbody").children().remove();
		//$("#example1").attr('disabled','disabled');
		
		$("#dxmTR").show();
		$("#xmxx").hide();
		$('#ZHTQDJ').removeAttr('disabled');
		$("#id_choseZTB").removeAttr('disabled');
<%--		$("#jhsjids").val('');--%>
<%--		$("#XMMC").val('');--%>
<%--		$("#BDMC").val('');--%>
<%--		$('#aaa').hide();--%>
<%--		--%>
<%--		$("#xmids").val('');--%>
<%--		$("#bdids").val('');--%>
<%--		$('#ZTBID').val('');--%>
<%--		$('#FBFS').val('');--%>
<%--		$('#ZHTQDJ').val('');--%>
<%--		  --%>
<%--		xmrowValues="";--%>
	}else if(val=="2"){
		//无项目合同
	
		$("#id_choseZTB").attr('disabled','disabled');
		
		$("#xmxx").hide();
		$('#ZHTQDJ').removeAttr('disabled');
		$("#dxmTR").hide();
		$("#aaa").hide();
	}
	
}
var popPage;
//选择项目标段
function selectBb(){
	xmxxfrom = "chosexm";
	g_btnNum = 0;
	$(window).manhuaDialog({"title":"项目列表","type":"text","content": "${pageContext.request.contextPath}/jsp/business/gcb/kfglgl/xmcx.jsp","modal":"2"});
}
//弹出区域回调
getWinData = function(data){
	//alert(popPage);
	if(g_btnNum == 0){
		bdChange = true;
		var jhsjid_val = JSON.parse(data).GC_JH_SJ_ID;
		$("#XMMC").val(JSON.parse(data).XMMC);
		$("#BDMC").val(JSON.parse(data).BDMC);
		$("#jhsjids").val(jhsjid_val);
		$("#id_choseZTB").attr("disabled", true);	
		$("#show_ztb").hide();
		$("#DT2").insertResult(data,DT2,1);
		var rowValues=new Array();
		$("#DT2 tbody tr").each(function(i){
			var rowValue = $(this).attr("rowJson");
			rowValues[i]=rowValue;
		});
		xmrowValues = rowValues;

		var v = $("#HTLX").val();
		if(v.indexOf("SJ")!=-1){//施工合同
			querySjrws_XM(jhsjid_val);
		}
		
		// 当合同类型是施工合同时，判断选择的项目是否已经签过施工合同
		var lx = $("#HTLX").val();
		if(lx == "SG") {
			if(iscf()) {
				return;
			}
		}
		
	}else if(g_btnNum == 1){
		//调用其它方法
	}else if(g_btnNum==2){
		var odd=convertJson.string2json1(data);
		$("#YFDW").val(JSON.parse(data).DWMC);
		$("#YFID").val(JSON.parse(data).GC_CJDW_ID);
	}else if(g_btnNum==3){
		var odd=convertJson.string2json1(data);
		$("#YFDW2").val(JSON.parse(data).DWMC);
		$("#YF2ID").val(JSON.parse(data).GC_CJDW_ID);
	}else if(g_btnNum==4){
		var odd=convertJson.string2json1(data);
		$("#YFDW3").val(JSON.parse(data).DWMC);
		$("#YF3ID").val(JSON.parse(data).GC_CJDW_ID);
	}else if(g_btnNum==5){
		getHtj();
	}
	
};

function iscf() {
	var action = controllername + "?queryIsCf";
    var data1 = "ids="+$("#jhsjids").val()+"&htid="+$("#ID").val();
    var sghtIscf = false;
	$.ajax({
		type : 'post',
		url : action,
		data : data1,
		dataType : 'json',
		async :	false,
		success : function(result) {
			var obj = convertJson.string2json1(result);
			if(obj.isDouble == "1") {
				xInfoMsg(obj.xmmc+"的施工合同已经签过。","");
				sghtIscf = true;
			}
		},
	    error : function(result) {
	    	xAlertMsg(1);
		}
	});
	return sghtIscf;
}

//获取最新合同价
function getHtj(){

	var data2 = combineQuery.getQueryCombineData(queryForm,frmPost,DT2);
	defaultJson.doQueryJsonList(controllernameHtsj+"?queryOne",data2,DT2,null, true);
	
	var data = combineQuery.getQueryCombineData(queryForm,frmPost);
	var data1 = {
		msg : data
	};
	$.ajax({
		url : controllername+"?query",
		data : data1,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(response) {
			$("#resultXML").val(response.msg);
			var resultobj = defaultJson.dealResultJson(response.msg);
			//$("#gcHtglHtForm").setFormValues(resultobj);
			$("#ZHTQDJ").val(resultobj.ZHTQDJ);
		}
	});
}

//排迁任务
function openPqrwQueryPage(m){
	g_btnNum = 1;
	var url = g_sAppName+"/jsp/business/pqgl/pqxmzhxx/pqzhxxcx.jsp";
	if(m!="" && m!=undefined){
	url += "?callback="+m;
	}
	$(window).manhuaDialog({"title" : "选择排迁任务","type" : "text","content" : url,"modal":"2"});
}
function callPq(jsonObj){//排迁回调
	var json = convertJson.string2json1(jsonObj);
	$("#pqzxmid").val(json.GC_PQ_ZXM_ID);
	$("#PQZRW_GXLB").val(json.GXLB_SV);
	$("#PXZRW_RWMC").val(json.ZXMMC);
	$("#jhsjids").val(json.JHSJID);
	$("#xmids").val(json.XMID);
	$("#bdids").val(json.BDID);
	$("#XMMC").val(json.XMMC);
	$("#BDMC").val(json.BDMC);
	var val = $("input:radio[name=HTSX]:checked").val();
	$("#SSZ").val(json.SSZ==""?0:json.SSZ);
	$("#SDZ").val(json.SDZ==""?0:json.SDZ);
	$("#htsx_value").show();
	if(val=="1"){
		$("#ZHTQDJ").val(json.SSZ==""?0:json.SSZ);
		$("#ZRWJE").val(json.SSZ==""?0:json.SSZ);
		$("#htsjje").val(json.SSZ==""?0:json.SSZ);
	}else if(val=="2"){
		$("#ZHTQDJ").val(json.SDZ==""?0:json.SDZ);
		$("#ZRWJE").val(json.SDZ==""?0:json.SDZ);
		$("#htsjje").val(json.SDZ==""?0:json.SDZ);
	}
	$("#HTMC").val(json.XMMC+json.BDMC+json.ZXMMC);
	
	//
    queryFileData(json.GC_PQ_ZXM_ID,"","","");
	 xmxxfrom = "chosexm";
}

//计算保修金率
function calPercen(){
	var ZHTQDJ = $("#ZHTQDJ").val();
	var BXJE = $("#BXJE").val();
	if(ZHTQDJ!="" && BXJE!=""){
		var str_percent = Math.round(parseInt(BXJE)/parseInt(ZHTQDJ) *10000)/100.00;
		$("#BXJL").val(str_percent);
	}
}
function showHTSX(){
	var val = $("input:radio[name=HTSX]:checked").val();
	$("#htsx_value").show();
		if(val=="1"){
			$("#htsx_text").text("预算值");
			if($("#pqzxmid").val()!=""){
				$("#ZHTQDJ").val($("#SSZ").val()==''?'0':$("#SSZ").val());
				$("#ZRWJE").val($("#SSZ").val()==''?'0':$("#SSZ").val());
			}
		}else if(val=="2"){
			$("#htsx_text").text("审定值");
			if($("#pqzxmid").val()!=""){
				$("#ZHTQDJ").val($("#SDZ").val()==''?'0':$("#SDZ").val());
				$("#ZRWJE").val($("#SDZ").val()==''?'0':$("#SDZ").val());
			}
		}
	
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
	<div class="B-small-from-table-autoConcise" id="aaa" style="display:none;">
	 <h4 class="title">项目列表
	  	<span class="pull-right">
	  	  <button id="btnRemove" class="btn"  type="button" disabled>移除项目</button>
		  <button id="example1" class="btn"  type="button">选择项目</button>
		</span> 
	  </h4>
	   <form method="post"  id="demoForm1"  >
     	<div class= "overFlowX" >
     	
<%--     	<%	--%>
<%--		if("insert".equals(type)){--%>
<%--	%>--%>
            <table width="100%" class="table-hover table-activeTd B-table" id="DT2" type="single" editable="0"  pageNum="1000" noPage="true">
                <thead>
                    <tr>	
                    		<th name="XH" id="_XH"  tdalign="">&nbsp;#&nbsp;</th>
<%--                    		<th fieldname="GC_JH_SJ_ID" tdalign="center">&nbsp;选择&nbsp;</th>--%>
							<th fieldname="XMBH" tdalign="center">&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>		
							<th fieldname="BDMC" maxlength="15">&nbsp;标段名称&nbsp;</th>						
							<th fieldname="XMLX" tdalign="center">&nbsp;项目类型&nbsp;</th>
<%--							<th fieldname="XMSX" tdalign="center">&nbsp;项目属性&nbsp;</th>--%>
							<th fieldname="ISBT" tdalign="center">&nbsp;BT&nbsp;</th>
							<th fieldname="XMDZ" maxlength="20">&nbsp;项目地址&nbsp;</th>
						</tr>
					</thead>
					<tbody>
                	</tbody>
				</table>
<%--			<%--%>
<%--				}else if("update".equals(type)){--%>
<%--     		%>	--%>
<%--				<table class="table-hover table-activeTd B-table" id="DT2" width="100%" type="single" editable="0"  pageNum="1000" noPage="true">--%>
<%--					<thead>--%>
<%--						<tr>--%>
<%--			 			    <th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>--%>
<%--							<th fieldname="XMMC" colindex=2 tdalign="center" maxlength="30" >&nbsp;项目名称&nbsp;</th>--%>
<%--							<th fieldname="BDMC" colindex=3 tdalign="center" maxlength="30" >&nbsp;标段名称&nbsp;</th>--%>
<%--							<th fieldname="HTQDJ" colindex=5 tdalign="center" >&nbsp;合同签订价(元)&nbsp;</th>--%>
<%--							<th fieldname="GCJGBFB" colindex=6 tdalign="center" >&nbsp;价格百分比(%)&nbsp;</th>--%>
<%--			             </tr>--%>
<%--					</thead>--%>
<%--					<tbody>--%>
<%--			           </tbody>--%>
<%--				</table>--%>
<%--				<%} %>--%>
			</div>
			</form>
		</div>
		
	<div class="B-small-from-table-autoConcise">
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
			<TD class="right-border bottom-border">
				<INPUT type="text" class="span12" kind="text" id="QID" name="ID"  fieldname="ghh.ID" value="" operation="="/>
				<INPUT type="text" class="span12" kind="text" id="QHTSJID" name="HTSJID"  fieldname="ghh2.ID" value="" operation="="/>
			</TD>
        </TR>
      </table>
      </form>
      <form method="post"  id="queryFormXmxx"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
        </TR>
      </table>
      </form>
	</div>

	<div class="B-small-from-table-autoConcise">
      <h4 class="title">合同基本信息
		<span class="pull-right">
		<button id="xzztbsc" class="btn" onclick="scxm()" type="button">删除合同项目</button>
		<button id="xzztbxx" class="btn" onclick="xzZtb()" type="button">选择招投标信息</button>
		</span>
      </h4>
     <form method="post" id="gcHtglHtForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/>
      	<input type="hidden" id="AlerdyHTSJFlag" value = "false"/>
      	<!-- <input type="hidden" id="QDNF" fieldname="QDNF" name = "QDNF"/> -->
      	<input type="hidden" id="HTZT" fieldname="HTZT" name = "HTZT"/>
      	<input type="hidden" id="YFID" name="YFID"  fieldname="YFID" >
      	<input type="hidden" id="YF2ID" name="YF2ID"  fieldname="YF2ID" >
      	<input type="hidden" id="YF3ID" name="YF3ID"  fieldname="YF3ID" >
      	<input type="hidden" id="JFDWID" name="JFDW"  fieldname="JFDW" >
      	<input type="hidden" id="ZBBMID" name="ZBBMID"  fieldname="ZBBMID" >
      	<input type="hidden" id="JFJBRID" name="JFJBRID"  fieldname="JFJBRID" >
      	<input type="hidden" id="lybzjId" name="lybzjId"  fieldname="lybzjId" >
      	<input type="hidden" id="pqzxmid"  name="GC_PQ_ZXM_ID"  fieldname="GC_PQ_ZXM_ID"  /><!-- 排迁子项目 -->
      	
      	<tr style="display:none;">
        	<td>
        		<input type="text" id="jhsjids" name="jhsjids"/>
<%--        		jhsjids 保存计划数据ID号们 , --%>
        		<input type="text" id="xmids" name="xmids"/>
        		<input type="text" id="bdids" name="bdids"/>
        		<input type="text" id="htsjje" name="htsjje"/>
        		<input type="text" id="pqzrw_jhsjid" name="pqzrw_jhsjid"/>
        		<input id="SSZ" class="span12" name="SSZ" type="text" />
				<input id="SDZ" class="span12" name="SDZ" type="text" />
        	</td>
        </tr>
		<tr id="show_ztb" style="display:black;">
			<th width="4%" class="right-border bottom-border text-right disabledTh">招投标名称</th>
			<td width="17%" class="right-border bottom-border">
<%--			<input id="ZTBNAME"  style="width:88%" class="span12" name="ZTBNAME" fieldname="ZTBNAME" type="text"  disabled />--%>
				<input id="ZTBID"  style="width:88%" class="span12" name="ZTBID" fieldname="ZTBID" type="text" val="" code="" disabled />
				<button class="btn btn-link"  type="button" id="id_choseZTB" onclick="xzZtb()" title="点击选择招投标"><i class="icon-edit"></i></button>
			</td>
<%--			<th width="8%" class="right-border bottom-border text-right">招标类型</th>--%>
			<th width="8%" class="right-border bottom-border text-right disabledTh">招标方式</th>
			<td width="17%" class="right-border bottom-border">
				<select  id="FBFS"   class="span6"  name="FBFS" fieldname="FBFS"  operation="=" kind="dic" src="ZBFS" defaultMemo="请选择" disabled></select>
			</td>
		</tr>
<%--		<tr>--%>
<%--			<th width="8%" class="right-border bottom-border text-right">项目名称</th>--%>
<%--       	 	<td class="bottom-border right-border" width="23%">--%>
<%--         		<input class="span12" style="width:85%" id="XMMC" type="text" placeholder="必填" check-type="required" fieldname="XMMC" name = "XMMC"  disabled />--%>
<%--       	 	</td>--%>
<%--         	<th width="8%" class="right-border bottom-border text-right">项目编号</th>--%>
<%--       		<td class="bottom-border right-border"width="15%">--%>
<%--         		<input class="span12" style="width:100%" id="XMBH" type="text" fieldname="XMBH" name = "XMBH"  disabled/>--%>
<%--         	</td>--%>
<%--        </tr>--%>
		<tr>
			<th width="4%" class="right-border bottom-border text-right">合同名称</th>
			<td width="17%" class="right-border bottom-border" >
				<input id="HTMC" style="width:99%" placeholder="必填" check-type="required" class="span12" check-type="maxlength" maxlength="1000"  name="HTMC" fieldname="HTMC" type="text" />
			</td>
			<th width="8%" class="right-border bottom-border text-right">年度</th>
			<td width="20%" class="right-border bottom-border">
	            <select class="span12 person" id="QQDNF" name="QDNF" check-type="required" fieldname="QDNF" operation="="  kind="dic" src="T#GC_TCJH_XMXDK:distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE"></select>
	        </td>
		</tr>
		<tr>
<%--			<th width="8%" class="right-border bottom-border text-right">合同编码</th>--%>
<%--			<td width="17%" class="right-border bottom-border">--%>
<%--				<input id="HTBM"  style="width:88%" class="span12" check-type="maxlength" maxlength="100"  name="HTBM" fieldname="HTBM" type="text" />--%>
<%--			</td>--%>
		</tr>
		<tr>
			<th width="4%" class="right-border bottom-border text-right">是否虚拟合同</th>
			<td width="17%" class="right-border bottom-border">&nbsp;&nbsp;
			    <input class="span12" id="SFXNHT" type="radio" placeholder="" kind="dic" src="SF" name = "SFXNHT" fieldname="SFXNHT">
			</td>
<%--			<th width="8%" class="right-border bottom-border text-right">是否贷款利息合同</th>--%>
<%--			<td width="17%" class="right-border bottom-border">--%>
<%--				<input class="span12" id="SFSDKLXHT" type="radio" placeholder="" kind="dic" src="SF" name = "SFSDKLXHT" fieldname="SFSDKLXHT">--%>
<%--			</td>--%>
			<th width="8%" class="right-border bottom-border text-right">合同类型</th>
			<td width="17%" class="right-border bottom-border">
				<select onchange="showSubInfo();"  id="HTLX"   placeholder="必填" check-type="required" class="span6"  name="HTLX" fieldname="HTLX"  operation="=" kind="dic" src="HTLX"  defaultMemo="请选择"></select>
			</td>
		</tr>
<%--		<tr>--%>
<%--			<th width="8%" class="right-border bottom-border text-right">合同签订日期</th>--%>
<%--			<td width="17%" class="right-border bottom-border">--%>
<%--				<input id="HTJQDRQ"  class="span5" style="width:70%;" fieldtype="date" fieldformat="YYYY-MM-DD"  placeholder="必填" check-type="required"  name="HTJQDRQ" fieldname="HTJQDRQ" type="date" />--%>
<%--			</td>--%>
<%--			<th width="8%" class="right-border bottom-border text-right"><span title="合同结算日期">合同结算日期</span></th>--%>
<%--			<td width="17%" class="right-border bottom-border">--%>
<%--				<input id="ZJSRQ"  class="span9" style="width:70%;" fieldtype="date" fieldformat="YYYY-MM-DD"  name="ZJSRQ" fieldname="ZJSRQ" type="date" />--%>
<%--			</td>--%>
<%--		</tr>--%>
<%--		<tr>--%>
<%--			<th width="8%" class="right-border bottom-border text-right">合同实际开始日期</th>--%>
<%--			<td width="17%" class="right-border bottom-border">--%>
<%--				<input id="HTSJKSRQ"  class="span9" style="width:70%;" fieldtype="date" fieldformat="YYYY-MM-DD"  name="HTSJKSRQ" fieldname="HTSJKSRQ" type="date" />--%>
<%--			</td>--%>
<%--			<th width="8%" class="right-border bottom-border text-right"><span title="合同实际结束日期">合同实际结束日期</span></th>--%>
<%--			<td width="17%" class="right-border bottom-border">--%>
<%--				<input id="HTJSRQ"  class="span9" style="width:70%;" fieldtype="date" fieldformat="YYYY-MM-DD" name="HTJSRQ" fieldname="HTJSRQ" type="date" />--%>
<%--			</td>--%>
<%--		</tr>--%>
		<tr>
			<th width="4%" class="right-border bottom-border text-right" id="ZHTQDJ_th">合同签订价</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZHTQDJ" value=0 class="span12" onchange="calPercen();" style="width:70%;text-align:right;" name="ZHTQDJ" fieldname="ZHTQDJ" min="0" type="number"/><b>(元)</b>
			</td>
			<th width="8%" class="right-border bottom-border text-right">报价系数</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BJXS" value=0 class="span12" style="width:70%;text-align:right;" name="BJXS" fieldname="BJXS" type="number" min="0" />
			</td>
		</tr>
		<tr>
			<th width="4%" class="right-border bottom-border text-right">合同项目数</th>
			<td width="17%" class="right-border bottom-border">&nbsp;&nbsp;
			<%
			if("insert".equals(type)){
			%>
				 <input class="span12" onclick="showDxmht();" id="SFDXMHT" type="radio" placeholder="" kind="dic" src="HTXMGS" name = "SFDXMHT" fieldname="SFDXMHT">
			<%
			}else if("update".equals(type)){
			%>
				 <input class="span12" onclick="showDxmht1(this.value);" id="SFDXMHT" type="radio" placeholder="" kind="dic" src="HTXMGS" name = "SFDXMHT" fieldname="SFDXMHT">
			<%
			}
			%>
			</td>
			<th width="4%" class="right-border bottom-border text-right">是否支付即投资</th>
			<td width="17%" class="right-border bottom-border">&nbsp;&nbsp;
				 <input class="span12" id="SFZFJTZ" type="radio" placeholder="" kind="dic" src="SF" name = "SFZFJTZ" fieldname="SFZFJTZ">
			</td>
		</tr>
		<tr id="dxmTR" style="display:black;">
			<th width="4%" class="right-border bottom-border text-right disabledTh">项目名称</th>
			<td width="17%" class="right-border bottom-border">
				<input id="XMMC" class="span12"  style="width:85%" name="XMMC" fieldname="XMMC" type="text" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BDMC" class="span12" name="BDMC" style="width:85%" fieldname="BDMC" type="text" disabled/>
				<button class="btn btn-link"  type="button" id="sel_xmxx" onclick="selectBb()" title="点击选择标段"><i class="icon-edit"></i></button>
			</td>
			</tr>
	</table>
		
		<% if(false && !type.equals("insert")){ 
		    if(cwcFlag){
		 %>
		<%}} %>
		<h4 id="SHOW_SGLHT_title" class="title" style="display:none;" >保修金信息</h4>
		<table id="SHOW_SGLHT" class="B-table" width="100%" style="display:none;">
		<tr>
			<th width="4%" class="right-border bottom-border text-right">保修金额</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BXJE" value=0 class="span12" style="width:70%;text-align:right;" onchange="calPercen();"  name="BXJE" fieldname="BXJE" type="number" min="0"/><b>(元)</b>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">保修金率</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BXJL" value=0 class="span12" style="width:70%;text-align:right;" name="BXJL" fieldname="BXJL" type="number" min="0" readOnly/>
			</td>
		</tr>
		<tr>
			<th width="4%" class="right-border bottom-border text-right">保修期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BXQ" value=0 class="span12" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="BXQ" fieldname="BXQ" type="number" min="0"/>
				<select  id="BXQDWLX" style="width:25%;"  class="span12"  name="BXQDWLX" fieldname="BXQDWLX"  operation="=" kind="dic" src="BXQDW"  defaultMemo="请选择">
			</td>
			<td colspan="2">
				&nbsp;
			</td>
		</tr>
		</table>
		
		<h4 id="SHOW_PQZRW_title" class="title" style="display:none;" >排迁子任务</h4>
		<table id="SHOW_PQZRW" class="B-table" width="100%" style="display:none;">
			<tr>
				<th width="4%" class="right-border bottom-border text-right disabledTh">管线类别</th>
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
				<th width="4%" class="right-border bottom-border text-right">合同属性</th>
				<td width="17%" class="right-border bottom-border">&nbsp;&nbsp;
					 <input class="span12" onclick="showHTSX();" id="HTSX" type="radio" placeholder="" kind="dic" src="HTSX" name = "HTSX" fieldname="HTSX">
				</td>
				<th width="8%" class="right-border bottom-border text-right disabledTh" ><span id="htsx_text"></span></th>
				<td width="17%" class="right-border bottom-border">
					<input id="ZRWJE" value=0 class="span12" style="width:70%;text-align:right;" name="ZRWJE" min="0" type="number" disabled/><b>(元)</b>
				</td>
			</tr>
			<!-- 选择排迁子项目时，把该子项目的委托函、联络单、审定表附件带到合同中。 -->
			<tr>
				<th width="4%" class="right-border bottom-border text-right">
					委托函
				</th>
				<td width="92%" colspan=7 class="bottom-border">
					<div>
						<table role="presentation" class="table table-striped">
							<tbody fjlb="0005" class="files showFileTab" onlyView="true" notClear="true"
								data-toggle="modal-gallery" data-target="#modal-gallery">
							</tbody>
						</table>
					</div>
				</td>
			</tr>
			<tr>
				<th width="4%" class="right-border bottom-border text-right">
					排迁联络单
				</th>
				<td width="92%" colspan=7 class="bottom-border">
					<div>
						<table role="presentation" class="table table-striped">
							<tbody fjlb="0002" class="files showFileTab" onlyView="true" notClear="true"
								data-toggle="modal-gallery" data-target="#modal-gallery">
							</tbody>
						</table>
					</div>
				</td>
			</tr>
			<tr>
				<th width="4%" class="right-border bottom-border text-right">
					预算审定表
				</th>
				<td width="92%" colspan=7 class="bottom-border">
					<div>
						<table role="presentation" class="table table-striped">
							<tbody fjlb="0004" class="files showFileTab" onlyView="true" notClear="true"
								data-toggle="modal-gallery" data-target="#modal-gallery">
							</tbody>
						</table>
					</div>
				</td>
			</tr>
			
		</table>
		
		<h4 id="SHOW_LYBZJ_title" class="title" style="display:none" >履约保证金信息</h4>
		<table id="SHOW_LYBZJ" class="B-table" width="100%" style="display:none">
			<tr>
				<th width="4%" class="right-border bottom-border text-right disabledTh">交纳金额</th>
				<td width="17%" class="right-border bottom-border">
					<input id="LYBZJ_JNJE" value=0 class="span12" style="width:70%;text-align:right;" name="JNJE" fieldname="BZJ_JNJE" min="0" type="number" disabled/><b>(元)</b>
					<button class="btn btn-link"  type="button" id="id_choseBZJ" onclick="xzLybzj()" title="点击选择履约保证金"><i class="icon-edit"></i></button>
				</td>
				<th width="8%" class="right-border bottom-border text-right disabledTh">交纳日期</th>
				<td width="17%" class="right-border bottom-border">
					<input id="LYBZJ_JNRQ" class="span9" style="width:70%;" fieldtype="date" fieldformat="YYYY-MM-DD" name="JNRQ" fieldname="BZJ_JNRQ" type="date" disabled="disabled">
				</td>
			</tr>
		</table>
		
		<h4 id="SHOW_SJRWS_title" class="title" style="display:none" >设计任务书信息</h4>
		<table id="SHOW_SJRWS" class="B-table" width="100%" style="display:none">
			
		</table>
		
		<h4 class="title">合同签订信息
		<span class="pull-right">
	      		&nbsp;&nbsp;
		  		<button id="addYfdw" class="btn" type="button" onclick="javascript:showOtherYfdw();" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">增加单位</button>
      	</span>
		</h4>
		<table class="B-table" width="100%">
		<tr>
			<th width="4%" class="right-border bottom-border text-right disabledTh">主办部门</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZBBM"   placeholder="必填" check-type="required" class="span12" check-type="maxlength" maxlength="36"  name="ZBBM" fieldname="ZBBM" type="text" style="width:88%" disabled/>
				<button class="btn btn-link"  type="button" id="deptBtn_ZBBM" value="ZBBM" title="点击选择主办部门"><i class="icon-edit"></i></button>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">部门经办人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="JFJBR"   class="span12" check-type="maxlength" maxlength="36"  name="JFJBR" fieldname="JFJBR" type="text" style="width:88%" disabled/>
				<button class="btn btn-link"  type="button" id="blrBtn_JFJBR" value="JFJBR"  title="点击选择部门经办人"><i class="icon-edit"></i></button>
			</td>
		</tr>
		<tr>
			
			<th width="4%" class="right-border bottom-border text-right disabledTh">甲方单位</th>
			<td width="17%" class="right-border bottom-border">
				<select id="JFID" class="span6" name="JFID" fieldname="JFID" disabled operation="=" kind="dic" src="JFDW" style="width:88%"></select>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">乙方单位</th>
			<td width="17%" class="right-border bottom-border">
				<input id="YFDW"  style="width: 88%" class="span12" placeholder="必填" check-type="required" name="YFDW" fieldname="YFDW" type="text" disabled/>
				<button class="btn btn-link"  type="button" title="点击选择乙方单位"><i class="icon-edit" onclick="xzxm(2);"></i></button>
			</td>
		</tr>
			<tr id="otherYFDW2" style="display:none">
				<th width="4%" class="right-border bottom-border text-right disabledTh">丙方单位</th>
				<td width="17%" class="right-border bottom-border">
					<input id="YFDW2"  style="width: 88%"  class="span12" name="YFDW2" fieldname="YFDW2" type="text" readOnly/>
					<button class="btn btn-link"  type="button" title="点击选择丙方单位"><i class="icon-edit" onclick="xzxm(3);"></i></button>
				</td>
				<th width="8%" class="right-border bottom-border text-right disabledTh">丁方单位</th>
				<td width="17%" class="right-border bottom-border">
					<input id="YFDW3"  style="width: 88%"  class="span12" name="YFDW3" fieldname="YFDW3" type="text" readOnly/>
					<button class="btn btn-link"  type="button" title="点击选择丁方单位"><i class="icon-edit" onclick="xzxm(4);"></i></button>
				</td>
			</tr>
		</table>
		<h4 class="title">合同内容及其它信息</h4>
		<table class="B-table" width="100%">
		<tr>
			<th width="8%" class="right-border bottom-border text-right">合同主要内容</th>
			<td colspan="3" class="bottom-border right-border" >
				<textarea class="span12" rows="2" id="HTZYNR" check-type="maxlength" maxlength="4000" fieldname="HTZYNR" name="HTZYNR"></textarea>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">付款方式</th>
			<td colspan="3" class="bottom-border right-border" >
				<textarea class="span12" rows="2" id="HTFKFS" check-type="maxlength" maxlength="4000" fieldname="HTFKFS" name="HTFKFS"></textarea>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">违约条款</th>
			<td colspan="3" class="bottom-border right-border" >
				<textarea class="span12" rows="2" id="HTWYTK" check-type="maxlength" maxlength="4000" fieldname="HTWYTK" name="HTWYTK"></textarea>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">结束依据</th>
			<td colspan="3" class="bottom-border right-border" >
				<textarea class="span12" rows="2" id="HTZSYJ" check-type="maxlength" maxlength="4000" fieldname="HTZSYJ" name="HTZSYJ"></textarea>
			</td>
		</tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right">备注</th>
	        <td colspan="3" class="bottom-border right-border" >
	        	<textarea class="span12" rows="2" id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ"></textarea>
	        </td>
        </tr>
        <tr>
        	<th width="8%" class="right-border bottom-border text-right">相关附件</th>
        	<td colspan="3" class="bottom-border right-border">
				<div>
					<span class="btn btn-fileUpload" id="addFileSpan" onclick="doSelectFile(this);"  fjlb="0701">
						<i class="icon-plus"></i>
						<span>添加文件...</span>
					</span>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0701" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
					</table>
				</div>
			</td>
        </tr>
      </table>
      	
      </form>
    </div>
   </div>
  </div>
  <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
  <div align="center">
 	<FORM id="frmPost" name="frmPost"  method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter" 	id = "txtFilter">
<%--         <input type="hidden" name="txtFilter"  order="desc" fieldname = "ghh.LRSJ" id = "txtFilter">--%>
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		 <input type="hidden" name="ywid" id = "ywid" value="">
 	</FORM>
 </div>
</body>
<script>
</script>
</html>