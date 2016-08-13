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
	if(roelsString.indexOf("财务处业务")!=-1){
	    cwcFlag = true;
	}
	
	//String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtController.do";
var depID;
var jbrID;
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
    				
    				if($("#addFileSpan").attr("disabled")){
    					$("#addFileSpan").attr("disabled", false);
    					$("#addFileSpan").bind("click", function() {
    						doSelectFile($(this));
    					});
    				}
    				
    				setFileData(obj.ID,"","","");
    				queryFileData(obj.ID,"","","");
    			}
    			//$("#gcHtglHtForm").clearFormResult();
    		}else{
    			defaultJson.doUpdateJson(controllername + "?update", data1,gcHtglHtList);
    		}
		}else{
			requireFormMsg();
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
    
    
    
});
//页面默认参数
function init(){
	//生成json串
	<%
		if("update".equals(type)){
	%>
		var tempJson;
		if(navigator.userAgent.indexOf("Firefox")>0) {
			var rowValue = $(parent.frames["menuiframe"]).contents().find("#resultXML").val();
			tempJson = eval("("+rowValue+")");
		}else{
			var rowValue = $(parent.frames["menuiframe"].document).find("#resultXML").val();
			tempJson = eval("("+rowValue+")");
		}
		$("#QID").val(tempJson.ID);
		$("#QHTSJID").val(tempJson.HTSJID);
		
		//alert(JSON.stringify(tempJson));
		//查询记录数
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,gcHtglHtList);
		
		//表单赋值
		var obj = $("#gcHtglHtList").getRowJsonObjByIndex(0);
		//alert(JSON.stringify(obj));
		if(obj!=-1){
			$("#gcHtglHtForm").setFormValues(obj);
			$("#resultXML").val(JSON.stringify(obj));
		}
		$("#btnXMBD").attr("disabled", false);
		$("#btnZF").attr("disabled", false);
		$("#btnWCTZ").attr("disabled", false);
		$("#btnBG").attr("disabled", false);
		
		if($("#addFileSpan").attr("disabled")){
			$("#addFileSpan").attr("disabled", false);
			$("#addFileSpan").bind("click", function() {
				doSelectFile($(this));
			});
		}
		
		setFileData(tempJson.ID,"","","");
		queryFileData(tempJson.ID,"","","");
	 <%
		}
	%>
}


//点击行事件
function tr_click(obj){
	//alert(JSON.stringify(obj));
	$("#gcHtglHtForm").setFormValues(obj);
}


//弹出区域回调
getWinData = function(data){
	$("#ZTBNAME").val(JSON.parse(data).XMMC);
	$("#ZTBID").val(JSON.parse(data).GC_JH_SJ_ID);
	
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
	//$("#DEPTID").attr("code",Id);
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

function returnJFDW(obj){
	$("#JFDWID").val($(obj).find("option:selected").text());
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
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
    <div style="height:5px;"></div>
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="gcHtglHtList" width="100%" type="single" pageNum="5">
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
				<th fieldname="ID" colindex=0 tdalign="center" maxlength="30" >&nbsp;编号&nbsp;</th>
				<th fieldname="HTLX" colindex=1 tdalign="center" maxlength="30" >&nbsp;合同类型&nbsp;</th>
				<th fieldname="ZTBID" colindex=2 tdalign="center" maxlength="30" >&nbsp;招投标编号&nbsp;</th>
				<th fieldname="HTBM" colindex=3 tdalign="center" maxlength="30" >&nbsp;合同编码&nbsp;</th>
				<th fieldname="HTMC" colindex=4 tdalign="center" maxlength="30" >&nbsp;合同名称&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	</div>

	<div class="B-small-from-table-autoConcise">
      <h4 class="title">合同基本信息
      	<span class="pull-right">
      	
      		<%
      		if(!"detail".equals(type)){
      			if(type.equals("insert")){ %>
      			<button id="btnClear_Bins" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>
      			<%} %>
	      		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
	      		&nbsp;&nbsp;
		  		<button id="btnXMBD" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">项目/标段</button>
		  		<button id="btnZF" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">合同支付</button>
		  		<button id="btnWCTZ" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">完成投资</button>
		  		<button id="btnBG" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled="disabled">合同变更</button>
      		<%} %>
	  		
      	</span>
      </h4>
     <form method="post" id="gcHtglHtForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
      	<input type="hidden" id="AlerdyHTSJFlag" value = "false"/></TD>
      	<input type="hidden" id="ZTBID" fieldname="ZTBID" name = "ZTBID"/></TD>
      	<input type="hidden" id="QDNF" fieldname="QDNF" name = "QDNF"/></TD>
      	<input type="hidden" id="JFDWID" name="JFDW"  fieldname="JFDW" >
      	
		<tr>
			<th width="8%" class="right-border bottom-border text-right">招投标编号</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZTBNAME"  style="width:88%" placeholder="必填" class="span12" check-type="required" name="ZTBNAME" fieldname="ZTBNAME" type="text"  disabled />
				<button class="btn btn-link"  type="button" onclick="queryProjectWithBD()"><abbr title="点击选择招投标"><i class="icon-edit"></i></abbr></button>
			</td>
			<th width="8%" class="right-border bottom-border text-right">合同编码</th>
			<td width="17%" class="right-border bottom-border">
				<input id="HTBM"   placeholder="必填" check-type="required" class="span12" check-type="maxlength" maxlength="100"  name="HTBM" fieldname="HTBM" type="text" />
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
			<th width="8%" class="right-border bottom-border text-right">合同类型</th>
			<td width="17%" class="right-border bottom-border">
				<select  id="HTLX"   placeholder="必填" check-type="required" class="span6"  name="HTLX" fieldname="HTLX"  operation="=" kind="dic" src="HTLX"  defaultMemo="-请选择-">
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">是否虚拟合同</th>
			<td width="17%" class="right-border bottom-border">&nbsp;&nbsp;
			    <input class="span12" id="SFXNHT" type="radio" placeholder="" kind="dic" src="SF" name = "SFXNHT" fieldname="SFXNHT">
			</td>
			<th width="8%" class="right-border bottom-border text-right">是否贷款利息合同</th>
			<td width="17%" class="right-border bottom-border">
<%--				<select  id="SFSDKLXHT"  class="span6"  name="SFSDKLXHT" fieldname="SFSDKLXHT"  operation="=" kind="dic" src="SF"  defaultMemo="-请选择-">--%>
				<input class="span12" id="SFSDKLXHT" type="radio" placeholder="" kind="dic" src="SF" name = "SFSDKLXHT" fieldname="SFSDKLXHT">
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">合同签订日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="HTJQDRQ"  class="span5" style="width:70%;" fieldtype="date" fieldformat="YYYY-MM-DD"  placeholder="必填" check-type="required"  name="HTJQDRQ" fieldname="HTJQDRQ" type="date" />
			</td>
			<th width="8%" class="right-border bottom-border text-right"><span title="合同结算日期">合同结算日期</span></th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZJSRQ"  class="span9" style="width:70%;" fieldtype="date" fieldformat="YYYY-MM-DD"  name="ZJSRQ" fieldname="ZJSRQ" type="date" />
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">合同实际开始日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="HTSJKSRQ"  class="span9" style="width:70%;" fieldtype="date" fieldformat="YYYY-MM-DD"  name="HTSJKSRQ" fieldname="HTSJKSRQ" type="date" />
			</td>
			<th width="8%" class="right-border bottom-border text-right"><span title="合同实际结束日期">合同实际结束日期</span></th>
			<td width="17%" class="right-border bottom-border">
				<input id="HTJSRQ"  class="span9" style="width:70%;" fieldtype="date" fieldformat="YYYY-MM-DD" name="HTJSRQ" fieldname="HTJSRQ" type="date" />
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">合同签订价(元)</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZHTQDJ" value=0 class="span12" placeholder="必填" check-type="required"  name="ZHTQDJ" fieldname="ZHTQDJ" type="number" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right">报价系数</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BJXS" value=0 class="span12" placeholder="必填" check-type="required"  name="BJXS" fieldname="BJXS" type="number" />
			</td>
		</tr>
		<%
      		if("update".equals(type)){
      	%>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">最新合同价(元)</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZZXHTJ" value=0 class="span12" name="ZZXHTJ" fieldname="ZZXHTJ" type="number" disabled/>
<%--				需计算得出  : 总合同签订价+合同变更--%>
			</td>
			<th width="8%" class="right-border bottom-border text-right">合同支付</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZHTZF" value=0 class="span12" name="ZHTZF" fieldname="ZHTZF" type="number" disabled/>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">完成投资</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZWCZF" value=0 class="span12" name="ZWCZF" fieldname="ZWCZF" type="number" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right">合同结算价</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZHTJS" value=0 class="span12" name="ZHTJS" fieldname="ZHTJS" type="number" disabled/>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">合同变更金额</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZBGJE" value=0 class="span12" name="ZBGJE" fieldname="ZBGJE" type="number" disabled/>
			</td>
		</tr>
		<%}%>
<%--		<tr>--%>
<%--			<th width="8%" class="right-border bottom-border text-right">合同状态</th>--%>
<%--			<td width="17%" class="right-border bottom-border">--%>
<%--				<select  id="HTZT"   placeholder="必填" check-type="required" class="span6"  name="HTZT" fieldname="HTZT"  operation="=" kind="dic" src="HTRXZT"  defaultMemo="-请选择-">--%>
<%--			</td>--%>
<%--			<th width="8%" class="right-border bottom-border text-right">发包方式</th>--%>
<%--			<td width="17%" class="right-border bottom-border">--%>
<%--				<select  id="FBFS"   placeholder="必填" check-type="required" class="span6"  name="FBFS" fieldname="FBFS"  operation="=" kind="dic" src="FBFS"  defaultMemo="-请选择-">--%>
<%--			</td>--%>
<%--		</tr>--%>
		</table>
		<%if(cwcFlag){ %>
		<%} %>
		<h4 class="title">保修金信息</h4>
		<table class="B-table" width="100%">
		<tr>
			<th width="8%" class="right-border bottom-border text-right">保修金额</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BXJE" value=0 class="span12"   placeholder="必填" check-type="required"  name="BXJE" fieldname="BXJE" type="number" />
			</td>
			<th width="8%" class="right-border bottom-border text-right">保修金率</th>
			<td width="17%" class="right-border bottom-border">
				<input id="BXJL" value=0 class="span12"  placeholder="必填" check-type="required"  name="BXJL" fieldname="BXJL" type="number" />
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">保修期</th>
			<td width="17%" class="right-border bottom-border" colspan="3">
				<input id="BXQ" value=0 class="span9" style="width:30%;text-align:right;"  placeholder="必填" check-type="required"  name="BXQ" fieldname="BXQ" type="number" />
				<select  id="BXQDWLX" style="width:10%;"  class="span6"  name="BXQDWLX" fieldname="BXQDWLX"  operation="=" kind="dic" src="BXQDW"  defaultMemo="-请选择-">
<%--					<input class="span12" id="BXQDWLX" type="radio" placeholder="" kind="dic" src="BXQDW" name = "BXQDWLX" fieldname="BXQDWLX">--%>
			</td>
		</tr>
		</table>
		<h4 class="title">合同签订信息</h4>
		<table class="B-table" width="100%">
		<tr>
			<th width="8%" class="right-border bottom-border text-right">甲方单位</th>
			<td width="17%" class="right-border bottom-border">
				<select id="JFID" check-type="required" class="span6" onchange="returnJFDW(this)" name="JFID" fieldname="JFID"  operation="=" kind="dic" src="JFDW" style="width:88%"></select>
			</td>
			<th width="8%" class="right-border bottom-border text-right">甲方签订人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="JFQDR"   class="span12" check-type="maxlength" maxlength="100"  name="JFQDR" fieldname="JFQDR" type="text" style="width:88%" disabled/>
				<button class="btn btn-link"  type="button" id="blrBtn_JFQDR" value="JFQDR"><abbr title="点击选择甲方签订人"><i class="icon-edit"></i></abbr></button>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">主办部门</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZBBM"   placeholder="必填" check-type="required" class="span12" check-type="maxlength" maxlength="36"  name="ZBBM" fieldname="ZBBM" type="text" style="width:88%" disabled/>
				<button class="btn btn-link"  type="button" id="deptBtn_ZBBM" value="ZBBM"><abbr title="点击选择主办部门"><i class="icon-edit"></i></abbr></button>
			</td>
			<th width="8%" class="right-border bottom-border text-right">甲方经办人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="JFJBR"   class="span12" check-type="maxlength" maxlength="36"  name="JFJBR" fieldname="JFJBR" type="text" style="width:88%" disabled/>
				<button class="btn btn-link"  type="button" id="blrBtn_JFJBR" value="JFJBR"><abbr title="点击选择甲方经办人"><i class="icon-edit"></i></abbr></button>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">乙方单位</th>
			<td width="17%" class="right-border bottom-border">
				<input id="YFDW"   placeholder="必填" check-type="required" class="span12" check-type="maxlength" maxlength="200"  name="YFDW" fieldname="YFDW" type="text" />
			</td>
			<th width="8%" class="right-border bottom-border text-right">乙方单位签订人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="YFDWQDR" style="width:88%" class="span12" check-type="maxlength" maxlength="100"  name="YFDWQDR" fieldname="YFDWQDR" type="text" />
<%--				<a href="javascript:void(0);" onclick="javascript:showOtherYfdw();"><b>+</b></a>--%>
				<button class="btn btn-link"  type="button" onclick="javascript:showOtherYfdw();"><abbr title="增加乙方单位输入框"><i class="icon-edit"></i></abbr></button>
			</td>
		</tr>
			<tr id="otherYFDW2" style="display:none">
				<th width="8%" class="right-border bottom-border text-right">乙方单位2</th>
				<td width="17%" class="right-border bottom-border">
					<input id="YFDW2"  class="span12" check-type="maxlength" maxlength="36"  name="YFDW2" fieldname="YFDW2" type="text" />
				</td>
				<th width="8%" class="right-border bottom-border text-right">乙方单位签订人2</th>
				<td width="17%" class="right-border bottom-border">
					<input id="YFDWQDR2"   class="span12" check-type="maxlength" maxlength="100"  name="YFDWQDR2" fieldname="YFDWQDR2" type="text" />
				</td>
			</tr>
			<tr id="otherYFDW3" style="display:none">
				<th width="8%" class="right-border bottom-border text-right">乙方单位3</th>
				<td width="17%" class="right-border bottom-border">
					<input id="YFDW3" class="span12" check-type="maxlength" maxlength="36"  name="YFDW3" fieldname="YFDW3" type="text" />
				</td>
				<th width="8%" class="right-border bottom-border text-right">乙方单位签订人3</th>
				<td width="17%" class="right-border bottom-border">
					<input id="YFDWQDR3" class="span12" check-type="maxlength" maxlength="100"  name="YFDWQDR3" fieldname="YFDWQDR3" type="text" />
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
<%--		<tr>--%>
<%--			<th width="8%" class="right-border bottom-border text-right">合同份数</th>--%>
<%--			<td width="17%" class="right-border bottom-border">--%>
<%--				<input id="HTFS" value=0 class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="HTFS" fieldname="HTFS" type="number" />--%>
<%--			</td>--%>
<%--			<th width="8%" class="right-border bottom-border text-right">签订年份</th>--%>
<%--			<td width="17%" class="right-border bottom-border">--%>
<%--				<select class="span12" id="QDNF" name="QDNF" fieldname="QDNF" operation="=" kind="dic" src="XMNF"  defaultMemo="-请选择-">--%>
<%--			</td>--%>
<%--		</tr>--%>
<%--		<tr>--%>
<%--			<th width="8%" class="right-border bottom-border text-right">变更金额</th>--%>
<%--			<td width="17%" class="right-border bottom-border">--%>
<%--				<input id="ZBGJE" value=0 class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="ZBGJE" fieldname="ZBGJE" type="number" />--%>
<%--			</td>--%>
<%--		</tr>--%>
<%--		<tr>--%>
<%--			<th width="8%" class="right-border bottom-border text-right">合同支付</th>--%>
<%--			<td width="17%" class="right-border bottom-border">--%>
<%--				<input id="ZHTZF" value=0 class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="ZHTZF" fieldname="ZHTZF" type="number" />--%>
<%--			</td>--%>
<%--			<th width="8%" class="right-border bottom-border text-right">完成投资</th>--%>
<%--			<td width="17%" class="right-border bottom-border">--%>
<%--				<input id="ZWCZF" value=0 class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="ZWCZF" fieldname="ZWCZF" type="number" />--%>
<%--			</td>--%>
<%--		</tr>--%>
		
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
			<th width="8%" class="right-border bottom-border text-right">中止理由</th>
			<td colspan="3" class="bottom-border right-border" >
				<textarea class="span12" rows="2" id="HTZZLY" check-type="maxlength" maxlength="4000" fieldname="HTZZLY" name="HTZZLY"></textarea>
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
					<span class="btn btn-fileUpload" id="addFileSpan" disabled="disabled" fjlb="0701">
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