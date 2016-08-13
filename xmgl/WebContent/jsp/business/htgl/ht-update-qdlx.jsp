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
	String deptName = dept.getDept_Name();
	String userid = user.getAccount();
	String username = user.getName();
	String roelsString = user.getRoleListString();
	boolean cwcFlag = false;
	if("100000000002".equals(deptId) || roelsString.indexOf("财务部")!=-1){
	    cwcFlag = true;
	}
	
	//String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtController.do";
var controllernameHtsj= "${pageContext.request.contextPath }/htgl/gcHtglHtsjController.do";
var depID;
var jbrID;
var saveflag = false, same_htbm_flag = false;
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
		if(same_htbm_flag){
			xFailMsg("已存在相同合同编码！","提示信息");
			return true;
		}
		saveflag = false;
		if($("#gcHtglHtForm").validationButton())
		{
			var sj = $("#HTJQDRQ").val();
			var ye = sj.substring(0,4);
			$("#QDNF").val(ye);
			
		    //生成json串
		    var data = Form2Json.formToJSON(gcHtglHtForm);
		  //组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  	//调用ajax插入
		    if($("#ID").val() == "" || $("#ID").val() == null){
    			var flag = defaultJson.doInsertJson(controllername + "?insert", data1,gcHtglHtList);
    			if(flag){
    				var obj = $("#gcHtglHtList").getRowJsonObjByIndex(0);
    				//alert(JSON.stringify(obj));
    				if(obj!=-1){
    					$("#ID").val(obj.ID);
    				}
    				$("#btnXMBD").attr("disabled", false);
    			}
    			//$("#gcHtglHtForm").clearFormResult();
    		}else{
    			saveflag = defaultJson.doUpdateJson(controllername + "?update", data1,DT2);
    		}
		}else{
			requireFormMsg();
			saveflag = false;
		  	return;
		}
	});
	
	//按钮绑定事件(修改)
	$("#btnXMBD").click(function() {
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
    });
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
	
    if($("#ID").val() == "" || $("#ID").val() == null){
    	$('input:radio[name=SFSDKLXHT]:eq(1)').attr("checked",true);
    	$("input[name=SFXNHT]:eq(1)").attr("checked",'checked'); 
    }
    
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
    
    
  	//自动完成 合同编码模糊查询
	//showAutoComplete("HTBM",controllername+"?htbmAutoComplete","getHtbmQueryCondition");
  	
	$("#HTBM").blur(function () {
		var queryCondition = getHtbmQueryCondition();
		$.post(controllername+"?htbmAutoComplete",{"matchInfo":queryCondition,"matchCount":3,"matchInputValue":"","tablePrefix":"","tableName":""},function(respData){
			var parsedO = JSON.parse(respData);
			if(parsedO.length>0){
				//存在相同的合同编号
				same_htbm_flag = true;
			}else{
				same_htbm_flag = false;
			}
		});
	});
});

//生成合同编码查询条件
function getHtbmQueryCondition(){
	var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":"htbm"}]}';
	var jsonData = convertJson.string2json1(initData);
	//不包括乍身ID
	if("" != $("#ID").val()){
		var defineCondition = {"value": $("#ID").val(),"fieldname":"ID","operation":"!=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	//合同编码
	if("" != $("#HTBM").val()){
		var defineCondition = {"value": "%"+$("#HTBM").val()+"%","fieldname":"HTBM","operation":"like","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	return JSON.stringify(jsonData);
}

//页面默认参数
function init(){
	//生成json串
	<%
		if("update".equals(type)){
	%>
		var parentmain=$(window).manhuaDialog.getParentObj();	
		var rowValue = parentmain.$("#resultXML").val();
		var tempJson = convertJson.string2json1(rowValue);
		
		$("#QID").val(tempJson.ID);
		$("#QHTSJID").val(tempJson.HTSJID);
		
		var flagSFDXMHT;//是否单项目合同控制
		var flagZTBID;//是否有招投标关联
		
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
					$("#dxmTR").hide();
				 	$("#aaa").show();
				 	flagSFDXMHT = false;
				}else{
					//单项目合同
					//$("#dxmTR").show();
				 	$("#aaa").hide();
				 	flagSFDXMHT = true;
				}
				
				//显示更多乙方 单位
				if($("#YF2ID").val()!=""||$("#YF3ID").val()!=""){
					$("#otherYFDW2").show();
					$("#otherYFDW3").show();
					$("#btn_add_yfdw").remove();
				}
			}
		});
		
		$("#btnXMBD").attr("disabled", false);
		$("#btnZF").attr("disabled", false);
		$("#btnWCTZ").attr("disabled", false);
		$("#btnBG").attr("disabled", false);
		$("#btnWJ").attr("disabled", false);
		
		
		if($(".addFileSpan").attr("disabled")){
			$(".addFileSpan").attr("disabled", false);
		}
		
		setFileData(tempJson.ID,"","","");
		queryFileData(tempJson.ID,"","","");
		
		 if (typeof(tempJson) != "undefined"){
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT2);
			var rrflag = 	defaultJson.doQueryJsonList(controllernameHtsj+"?queryOne",data,DT2);
			if(rrflag){
				if($("#DT2 tbody tr").length==1){//单项目合同
				 	 
				 	 $("#dxmTR").show();
				 	 
				 	var obj1 =$("#DT2 tbody tr").eq(0).attr("rowJson");
				 	var obj = convertJson.string2json1(obj1);
				 	//var obj = $("#DT2").getRowJsonObjByIndex(1);
					$("#XMMC").val(obj.XMMC);
					$("#BDMC").val(obj.BDMC);
				 	 
				 }
			}
		}
	 <%
		}
	%>
}


//点击行事件
function tr_click(obj){
	//alert(JSON.stringify(obj));
	//$("#gcHtglHtForm").setFormValues(obj);
}

var g_btnNum = 0;
function xzxm(n) {
	g_btnNum = n;
	$(window).manhuaDialog({"title" : "单位选择","type" : "text","content" : "${pageContext.request.contextPath}/jsp/business/gcb/cjdw/cjdw_Query_Add.jsp","modal":"4"});
}



//弹出区域回调
getWinData = function(data){
	if(g_btnNum==2){
		var odd=convertJson.string2json1(data);
		$("#YFDW").val(JSON.parse(data).DWMC);
		$("#YFID").val(JSON.parse(data).GC_CJDW_ID);
	}else if(g_btnNum==3){
		var odd=convertJson.string2json1(data);
		$("#YFDW2").val(JSON.parse(data).DWMC);
		$("#YFDW2ID").val(JSON.parse(data).GC_CJDW_ID);
	}else if(g_btnNum==4){
		var odd=convertJson.string2json1(data);
		$("#YFDW3").val(JSON.parse(data).DWMC);
		$("#YFDW3ID").val(JSON.parse(data).GC_CJDW_ID);
	}else{
		$("#ZTBNAME").val(JSON.parse(data).XMMC);
		$("#ZTBID").val(JSON.parse(data).GC_JH_SJ_ID);
	}	
	
	
};

//详细信息
function rowView(index){
	var obj = $("#gcHtglHtList").getSelectedRowJsonByIndex(index);
	var xmbh = eval("("+obj+")").XMBH;
	$(window).manhuaDialog(xmscUrl(xmbh));
}

function closeNowCloseFunction(){
	return true;
}

function showOtherYfdw(){
	$("#otherYFDW2").toggle();
	$("#otherYFDW3").toggle();
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
}

//提交合同部审批
$(function() {
	$("#btnQdlx").click(function() {
		
		xConfirm("信息确认","签订后合同不可修改！是否确认操作？ ");
		
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){
			
			$("#btnSave").click();
			if(!saveflag){
				return false;
			}else{
				var rowid = $("#QID").val();
				$.ajax({
					url : controllername+"?update",
					data : "opttype=htbqdlx&id="+rowid,
					cache : false,
					async :	false,
					dataType : "json",  
					type : 'post',
					success : function(result) {
						$("#resultXML").val(result.msg);
						//xAlert("提示信息","签订成功！");
						$("#btnQdlx").attr("disabled",true);
						$("#btnSave").attr("disabled",true);
						$("#btnXMBD").attr("disabled",true);
					}
				});
			}
		});
		
		
	});
});

function returnJFDW(obj){
	$("#JFDWID").val($(obj).find("option:selected").text());
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
<div class="B-small-from-table-autoConcise" id="aaa">
	 <h4 class="title">项目列表
	  	<span class="pull-right">
		</span> 
	  </h4>
	   <form method="post"  id="demoForm1"  >
     	<div class= "overFlowX" >
				<table class="table-hover table-activeTd B-table" id="DT2" width="100%" type="single" editable="0"  pageNum="1000" noPage="true">
					<thead>
						<tr>
			 			    <th  name="XH" id="_XH" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
							<th fieldname="XMMC" colindex=4 tdalign="center" maxlength="30" >&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDMC" colindex=5 tdalign="center" maxlength="30" >&nbsp;标段名称&nbsp;</th>
							<th fieldname="HTQDJ" colindex=6 tdalign="right" >&nbsp;合同签订价<b>(元)</b>&nbsp;</th>
							<th fieldname="GCJGBFB" colindex=7 tdalign="center" >&nbsp;价格百分比(%)&nbsp;</th>
			             </tr>
					</thead>
					<tbody>
			           </tbody>
				</table>
			</div>
			</form>
		</div>

	<div class="B-small-from-table-autoConcise" style="display:none;">
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
			<TD class="right-border bottom-border">
				<INPUT type="text" class="span12" kind="text" id="QID" name="ID"  fieldname="ghh.ID" value="" operation="="/>
				<INPUT type="text" class="span12" kind="text" id="QHTSJID" name="HTSJID"  fieldname="ghh2.ID" value="" operation="="/>
			</TD>
        </TR>
         <tr>
			<td class="text-left bottom-border text-right">
	        	<button id="btnQuery" class="btn btn-link"  type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-search"></i>查询</button>
	        	<button id="btnClear" class="btn btn-link" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-trash"></i>清空</button>
	        </td>
		</tr>
      </table>
      </form>
	</div>

	<div class="B-small-from-table-autoConcise">
      <h4 class="title">合同基本信息
      	<span class="pull-right">
      	
      		<%
      		if(!"detail".equals(type)){
      			 %>
				<button id="btnQdlx" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">签订履行</button>
      			      			
	      		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
	      		&nbsp;&nbsp;
		  		<button id="btnXMBD" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">项目/标段(金额)</button>
		  		<button id="xmxx" class="btn"  type="button" style="display:none;">项目信息</button>
      		<%} %>
	  		
      	</span>
      </h4>
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
      	
      	<tr id="show_ztb" style="display:black;">
			<th width="8%" class="right-border bottom-border text-right disabledTh">招投标名称</th>
			<td width="17%" class="right-border bottom-border">
			<input id="ZTBID"  style="width:88%" class="span12" name="ZTBID" fieldname="ZTBID" type="text" val="" code="" disabled />
				<button class="btn btn-link"  type="button" id="id_choseZTB" onclick="xzZtb()" title="点击选择招投标"><i class="icon-edit"></i></button>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">招标方式</th>
			<td width="17%" class="right-border bottom-border">
				<select  id="FBFS"   class="span6"  name="FBFS" fieldname="FBFS"  operation="=" kind="dic" src="ZBFS" defaultMemo="-请选择-" disabled>
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
			<th width="8%" class="right-border bottom-border text-right">合同名称</th>
			<td width="17%" class="right-border bottom-border">
				<input id="HTMC" style="width:88%" placeholder="必填" check-type="required" class="span12" check-type="maxlength" maxlength="1000"  name="HTMC" fieldname="HTMC" type="text" />
			</td>
<%--			<th width="8%" class="right-border bottom-border text-right">合同类型</th>--%>
<%--			<td width="17%" class="right-border bottom-border">--%>
<%--				<select  id="HTLX"   placeholder="必填" check-type="required" class="span6"  name="HTLX" fieldname="HTLX"  operation="=" kind="dic" src="HTLX"  defaultMemo="-请选择-">--%>
<%--			</td>--%>
			<th width="8%" class="right-border bottom-border text-right">合同编码</th>
			<td width="17%" class="right-border bottom-border">
				<input id="HTBM" style="width:70%"  class="span12" check-type="maxlength" maxlength="100"  name="HTBM" fieldname="HTBM" type="text" />
				<select  id="HTLX"   class="span6"  name="HTLX" fieldname="HTLX"  kind="dic" src="HTLX" style="display:none;">
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">是否虚拟合同</th>
			<td width="17%" class="right-border bottom-border">&nbsp;&nbsp;
			    <input class="span12" id="SFXNHT" type="radio" placeholder="" kind="dic" src="SF" name = "SFXNHT" fieldname="SFXNHT">
			</td>
			<th width="8%" class="right-border bottom-border text-right">合同签订日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="HTJQDRQ"  class="span5" style="width:70%;" fieldtype="date" fieldformat="YYYY-MM-DD"  placeholder="必填" check-type="required"  name="HTJQDRQ" fieldname="HTJQDRQ" type="date" />
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">合同实际开始日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="HTSJKSRQ"  class="span9" style="width:70%;" fieldtype="date" fieldformat="YYYY-MM-DD" check-type="required"  name="HTSJKSRQ" fieldname="HTSJKSRQ" type="date" />
			</td>
			<th width="8%" class="right-border bottom-border text-right"><span title="合同实际结束日期">合同实际结束日期</span></th>
			<td width="17%" class="right-border bottom-border">
				<input id="HTJSRQ"  class="span9" style="width:70%;" fieldtype="date" fieldformat="YYYY-MM-DD" name="HTJSRQ" fieldname="HTJSRQ" type="date" />
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">合同签订价</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZHTQDJ" value=0 class="span12" placeholder="必填" check-type="required"  name="ZHTQDJ" fieldname="ZHTQDJ" type="number"  min="0" disabled style="width:88%;text-align:right;"/><b>(元)</b>
			</td>
			<th width="8%" class="right-border bottom-border text-right">报价系数</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BJXS" value=0 class="span12" placeholder="必填" check-type="required"  name="BJXS" fieldname="BJXS" type="number"  min="0" style="width:88%;text-align:right;"/>
			</td>
		</tr>
		<tr id="dxmTR" style="display:black;">
			<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
			<td width="17%" class="right-border bottom-border">
				<input id="XMMC" class="span12"  style="width:85%" name="XMMC" fieldname="XMMC" type="text" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BDMC" class="span12" name="BDMC" style="width:85%" fieldname="BDMC" type="text" disabled/>
			</td>
			</tr>
		</table>
		
		<% 
		    if(cwcFlag){
		 %>
		<%} %>
		<h4 class="title">保修金信息</h4>
		<table class="B-table" width="100%">
		<tr>
			<th width="8%" class="right-border bottom-border text-right">保修金额</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BXJE" value=0 class="span12"   name="BXJE" fieldname="BXJE" type="number"  min="0" style="width:88%;text-align:right;"/><b>(元)</b>
			</td>
			<th width="8%" class="right-border bottom-border text-right">保修金率</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BXJL" value=0 class="span12"  name="BXJL" fieldname="BXJL" type="number"  min="0" style="width:88%;text-align:right;"/>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">保修期</th>
			<td width="17%" class="right-border bottom-border" colspan="3">
				<input id="BXQ" value=0 class="span9" style="width:30%;text-align:right;" name="BXQ" fieldname="BXQ" type="number"  min="0" style="width:88%;text-align:right;"/>
				<select  id="BXQDWLX" style="width:10%;"  class="span6"  name="BXQDWLX" fieldname="BXQDWLX"  operation="=" kind="dic" src="BXQDW"  defaultMemo="-请选择-">
<%--					<input class="span12" id="BXQDWLX" type="radio" placeholder="" kind="dic" src="BXQDW" name = "BXQDWLX" fieldname="BXQDWLX">--%>
			</td>
		</tr>
		</table>
		<h4 class="title">合同签订信息</h4>
		<table class="B-table" width="100%">
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">甲方单位</th>
			<td width="17%" class="right-border bottom-border">
				<select id="JFID" check-type="required" class="span6" onchange="returnJFDW(this)" name="JFID" fieldname="JFID"  operation="=" kind="dic" src="JFDW" style="width:88%"></select>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">甲方签订人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="JFQDR"   class="span12" check-type="maxlength" maxlength="100"  name="JFQDR" fieldname="JFQDR" type="text" style="width:88%" disabled/>
				<button class="btn btn-link"  type="button" id="blrBtn_JFQDR" value="JFQDR"><abbr title="点击选择甲方签订人"><i class="icon-edit"></i></abbr></button>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">主办部门</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZBBM"   placeholder="必填" check-type="required" class="span12" check-type="maxlength" maxlength="36"  name="ZBBM" fieldname="ZBBM" type="text" style="width:88%" disabled/>
				<button class="btn btn-link"  type="button" id="deptBtn_ZBBM" value="ZBBM"><abbr title="点击选择主办部门"><i class="icon-edit"></i></abbr></button>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">甲方经办人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="JFJBR"   class="span12" check-type="maxlength" maxlength="36"  name="JFJBR" fieldname="JFJBR" type="text" style="width:88%" disabled/>
				<button class="btn btn-link"  type="button" id="blrBtn_JFJBR" value="JFJBR"><abbr title="点击选择甲方经办人"><i class="icon-edit"></i></abbr></button>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">乙方单位</th>
			<td width="17%" class="right-border bottom-border">
				<input id="YFDW"  style="width: 88%" class="span12" name="YFDW" fieldname="YFDW" type="text" readOnly/>
				&nbsp;&nbsp;&nbsp;<i width="10%" class="icon-edit" title="请选择单位" onclick="xzxm(2);"></i>
			</td>
			<th width="8%" class="right-border bottom-border text-right">乙方单位签订人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="YFDWQDR" style="width:88%" class="span12" check-type="maxlength" maxlength="100"  name="YFDWQDR" fieldname="YFDWQDR" type="text"/>
<%--				<a href="javascript:void(0);" onclick="javascript:showOtherYfdw();"><b>+</b></a>--%>
				<button class="btn btn-link" id="btn_add_yfdw" type="button" onclick="javascript:showOtherYfdw();"><abbr title="增加乙方单位输入框"><i class="icon-plus"></i></abbr></button>
			</td>
		</tr>
			<tr id="otherYFDW2" style="display:none">
				<th width="8%" class="right-border bottom-border text-right disabledTh">丙方单位</th>
				<td width="17%" class="right-border bottom-border">
					<input id="YFDW2" style="width: 88%"  class="span12" name="YFDW2" fieldname="YFDW2" type="text" readOnly/>
					&nbsp;&nbsp;&nbsp;<i width="10%" class="icon-edit" title="请选择单位" onclick="xzxm(3);"></i>
				</td>
				<th width="8%" class="right-border bottom-border text-right">丙方单位签订人</th>
				<td width="17%" class="right-border bottom-border">
					<input id="YFDWQDR2"   style="width:88%"  class="span12" check-type="maxlength" maxlength="100"  name="YFDWQDR2" fieldname="YFDWQDR2" type="text"/>
				</td>
			</tr>
			<tr id="otherYFDW3" style="display:none">
				<th width="8%" class="right-border bottom-border text-right disabledTh">丁方单位</th>
				<td width="17%" class="right-border bottom-border">
					<input id="YFDW3" style="width: 88%"  class="span12" name="YFDW3" fieldname="YFDW3" type="text" readOnly/>
					&nbsp;&nbsp;&nbsp;<i width="10%" class="icon-edit" title="请选择单位" onclick="xzxm(4);"></i>
				</td>
				<th width="8%" class="right-border bottom-border text-right">丁方单位签订人</th>
				<td width="17%" class="right-border bottom-border">
					<input id="YFDWQDR3" class="span12"  style="width:88%"  check-type="maxlength" maxlength="100"  name="YFDWQDR3" fieldname="YFDWQDR3" type="text"/>
				</td>
			</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">担保单位</th>
			<td width="17%" class="right-border bottom-border" colspan="3">
				<input id="DBDW"   style="width:40%" class="span12" check-type="maxlength" maxlength="1000"  name="DBDW" fieldname="DBDW" type="text" />
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
					<span class="btn btn-fileUpload addFileSpan" onclick="doSelectFile(this);" disabled="disabled" fjlb="0701">
						<i class="icon-plus"></i>
						<span>添加文件...</span>
					</span>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0701" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
					</table>
				</div>
			</td>
        </tr>
        <tr>
        	<th width="8%" class="right-border bottom-border text-right">正式合同</th>
        	<td colspan="3" class="bottom-border right-border">
				<div>
					<span class="btn btn-fileUpload addFileSpan" onclick="doSelectFile(this);" disabled="disabled" fjlb="0702">
						<i class="icon-plus"></i>
						<span>添加文件...</span>
					</span>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0702" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
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