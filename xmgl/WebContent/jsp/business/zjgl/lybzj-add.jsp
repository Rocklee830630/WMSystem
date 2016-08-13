<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<style type="text/css" media=print> 
.noprint{display : none } 
</style>
<title>履约保证金-新增——修改——详细信息</title>
<%
	String type=request.getParameter("type");
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	String userid = user.getAccount();
	String username = user.getName();
	
	String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">

var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglLybzjController.do";
var controllernameYfdw= "${pageContext.request.contextPath }/htgl/gcHtglHtController.do";
var flag,jbrID, JNFSFalg;
var type ="<%=type%>";


//页面初始化
$(function() {
	init();
	
	<%
		if(!"detail".equals(type)){
	%>
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#demoForm").clearFormResult();
        $("#JNRQ").val(new Date().toLocaleDateString());
        $("#FHRQ").val(new Date().toLocaleDateString());
    });
  	//按钮绑定事件（保存）
    $("#btnSave").click(function() {
    	
    	if($("#demoForm").validationButton())
		{
    		//生成json串
    		var data = Form2Json.formToJSON(demoForm);
		  	//组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  	//调用ajax插入
		    if($("#ID").val() == "" || $("#ID").val() == null){
		    	var valywid = $("#ywid").val();
		    	var flag = defaultJson.doInsertJson(controllername + "?insert&ywid="+valywid, data1);
		    	if(flag){
		    		var json=$("#resultXML").val();
		    		var tempJson=eval("("+json+")");
		    		var resultobj=tempJson.response.data[0];
		    		$("#demoForm").setFormValues(resultobj);
		    		
		    		setFileData(tempJson.ID,"","","","1");
    				queryFileData(resultobj.ID,"","","");
    				$(window).manhuaDialog.close();
		    	}
    		}else{
    			var flag  =defaultJson.doUpdateJson(controllername + "?update", data1);
    			if(flag){
    				$(window).manhuaDialog.close();
    			}
    		}
		}else{
			requireFormMsg();
		  	return;
		}
    });
  	
    if($("#ID").val() == "" || $("#ID").val() == null){
    	$("input[name=JNFS]:eq(1)").attr("checked",'checked'); 
    	$("input[name=BHLX]:eq(1)").attr("checked",'checked'); 
    }
    
    $("button[id^='blrBtn_']").bind("click", function(){
    	var valu = $(this).attr("value");
    	jbrID = valu;
    	var actorCode = $("#"+valu).attr("code");
		openUserTree('single',actorCode,'setBlr') ;
		//selectUserTree('single',actorCode,'setBlr'); //新方式
	});
    
    <%
		}else{
	%>
	//置所有input 为disabled
	$(":input").each(function(i){
	   $(this).attr("disabled", "true");
	 });
	
	$("#id_chose_jndw").removeAttr("onclick");
	<%
		}
	%>
	
	//设置保函是否显示
	if(JNFSFalg=="BH"){
		//对保函的处理，显示对方银行 保函起止日期等信息.
		$("#show_div_bh").show();
		$("#show_th_bhlx").show();
		$("#show_td_bhlx").show();
	}else if(JNFSFalg=="XJ"){
		$("#show_div_bh").hide();
		$("#show_th_bhlx").hide();
		$("#show_td_bhlx").hide();
	}
	
	$("#btnPrint").click(function(){
		window.print();
	});
});
//页面默认参数
function init(){
	if(type == "insert"){
		$("#JNRQ").val('<%=sysdate%>');
		$("#BLR").val('<%=username%>');
		$("#BLR").attr("code", '<%=userid%>');
	}else if(type == "update" || type == "detail"){
		
		var parentmain=$(this).manhuaDialog.getParentObj();	
		var rowValue = parentmain.$("#resultXML").val();
		var tempJson = convertJson.string2json1(rowValue);
		
		JNFSFalg = tempJson.JNFS;
		
		//表单赋值
		$("#demoForm").setFormValues(tempJson);
		
		if(type == "detail"){
			$(".showFileTab").attr("onlyView","true");
		}
		
		deleteFileData(tempJson.ID,"","","");
		setFileData(tempJson.ID,"","","","1");
		queryFileData(tempJson.ID,"","","");
	}
}

var popPage;
function selectYh(){
	popPage = "selectYh";
	$(window).manhuaDialog({"title":"履约保证金->选择对方银行","type":"text","content":"/xmgl/jsp/framework/system/dict/commonDict.jsp?category=zjgl_lybzj_dfyh&title=对方银行&field=DFYH","modal":"4"});
}

function xzxm(n) {
	popPage = n;
	$(window).manhuaDialog({"title" : "单位选择","type" : "text","content" : "${pageContext.request.contextPath}/jsp/business/gcb/cjdw/cjdw_Query_Add.jsp","modal":"4"});
}
//选中项目名称弹出页
function selectHt(){
	$(window).manhuaDialog({"title":"选择合同","type":"text","content":"${pageContext.request.contextPath}/jsp/business/htgl/htcx.jsp","modal":"2"});
}

function selectBb(){
	popPage = "bd";
	$(window).manhuaDialog({"title":"项目列表","type":"text","content": "${pageContext.request.contextPath}/jsp/business/gcb/kfglgl/xmcx.jsp","modal":"2"});
}



//弹出区域回调
getWinData = function(data){
	//alert(popPage);
	if(popPage == "selectYh") {
		$("#DFYH").val(JSON.parse(data).DICT_NAME);
		$("#DFYH").attr("code",JSON.parse(data).DICT_ID);
	}else if(popPage == "2") {
		//$("#DFYH").val(JSON.parse(data).DWMC);
		$("#JNDW").val(JSON.parse(data).DWMC);
	    $("#JNDW").attr("code",JSON.parse(data).GC_CJDW_ID);
	}else if(popPage == "bd"){
		var jhsjid_val = JSON.parse(data).GC_JH_SJ_ID;
		$("#JHSJID").val(jhsjid_val);
		$("#XMID").val(JSON.parse(data).XMID);
		$("#BDID").val(JSON.parse(data).BDID);
		$("#XMMC").val(JSON.parse(data).XMMC);
		$("#BDMC").val(JSON.parse(data).BDMC);
		
		$("#QJHSJID").val(jhsjid_val);
		
		//查询乙方单位
		 queryYfdw();
	}
};

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
	$("#"+jbrID).attr("code", userId);
}

function queryYfdw(){
	//根据数据ID查询所关联的合同信息 乙方单位
	var data = combineQuery.getQueryCombineData(queryFormYFDW,frmPost);
	var data1 = {
		msg : data
	};
	$.ajax({
		url : controllernameYfdw+"?queryDwxxByJhid&opttype=yfdw",
		data : data1,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(response) {
			var resultmsgobj = convertJson.string2json1(response.msg);
			if(resultmsgobj!=0){
				var resultobj =  resultmsgobj.response.data[0];
				$("#JNDW").val(resultobj.YFDW);
				$("#JNDW").attr("code",resultobj.YFID);
			}else{
				$("#JNDW").val("");
				$("#JNDW").attr("code","");
			}
		}
	});
}

function closeParentCloseFunction(){
	//queryYfdw();
	return true;
}

function closeNowCloseFunction(){
	return true;
}

function showBH(t){
	if(t.value=="BH"){
		//对保函的处理，显示对方银行 保函起止日期等信息.
		$("#show_div_bh").show();
		$("#show_th_bhlx").show();
		$("#show_td_bhlx").show();
	}else if(t.value=="XJ"){
		$("#show_div_bh").hide();
		$("#show_th_bhlx").hide();
		$("#show_td_bhlx").hide();
	}
}
//招投标
function xzZtb() {
	$(window).manhuaDialog({"title" : "选择招投标信息","type" : "text","content" : "${pageContext.request.contextPath}/jsp/business/ztb/ztbQuery.jsp?bmht=false","modal":"2"});
}
//回调招投标信息
function selectZtb(rowValue){
	bdChange = true;
	var tempJson = convertJson.string2json1(rowValue);
	//var mc = tempJson.ZTBMC;
	ztbId = tempJson.GC_ZTB_SJ_ID;
	$("#ZTBMC").val(convertJson.string2json1(rowValue).ZTBMC);
	$("#ZTBID").val(convertJson.string2json1(rowValue).GC_ZTB_SJ_ID);
	$("#DSFJGID").val(tempJson.DSFJGID_SV);//中标单位
}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	</p>	
	<div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">
      		<h4 class="title">
      			履约保证金信息
				<span class="pull-right noprint">
					<%
		      		if(!"detail".equals(type)){
		      			if(type.equals("insert")){ %>
<%--		      			<button id="btnClear" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>--%>
		      			<%} %>
		      		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
<%--		      		<button id="btnPrint" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">打印</button>--%>
		      		<%} %>
				</span>    			
      		</h4>
     		<form method="post" id="demoForm">
      			<table class="B-table" width="100%" id="DT1">
      				<input type="hidden" id="ID" fieldname="ID" name="ID"/>
      				<input type="hidden" id="HTID" fieldname="HTID" name="HTID"/>
      				<input type="hidden" id="JHSJID" name="JHSJID"  fieldname="JHSJID" >
      				<input type="hidden" id="XMID" name="XMID"  fieldname="XMID" >
					<input type="hidden" id="BDID" name="BDID"  fieldname="BDID" >
					<input type="hidden" id="ZTBID" name="ZTBID"  fieldname="ZTBID" >
			        <tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">招投标名称</th>
						<td width="25%" class="right-border bottom-border">
							<input id="ZTBMC"  style="width:88%" class="span12" name="ZTBMC" fieldname="ZTBMC" type="text" val="" code="" disabled />
							<button class="btn btn-link"  type="button" id="id_choseZTB" onclick="xzZtb()" title="点击选择招投标"><i class="icon-edit"></i></button>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">中标单位</th>
						<td width="25%" class="right-border bottom-border">
							<input id="DSFJGID" class="span12" name="DSFJGID" fieldname="DSFJGID" type="text"    disabled />
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">交纳单位</th>
						<td width="17%" class="right-border bottom-border">
							<input id="JNDW" class="span12" name="JNDW" style="width:85%" fieldname="JNDW" type="text" disabled/>
<%--							<i width="10%" class="icon-edit" id="id_chose_jndw" title="点击选择单位" onclick="xzxm('2');"></i>--%>
							<button class="btn btn-link" id="id_chose_jndw" type="button" onclick="xzxm('2');" title="点击选择单位"><i class="icon-edit"></i></button>
						</td>
						<th width="8%" class="right-border bottom-border text-right">交纳日期</th>
						<td width="17%" class="right-border bottom-border">
							<input style="width:45%;" id="JNRQ" class="span7" type="date" check-type="maxlength" maxlength="10" name="JNRQ" fieldname="JNRQ" />
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">金额</th>
						<td width="17%" class="right-border bottom-border">
							<input id="JE" class="span9" placeholder="必填" value=0 style="width:70%;text-align:right;" check-type="required" check-type="maxlength" maxlength="17" name="JE" fieldname="JE" type="number" min="0"/>&nbsp;<b>(元)
						</td>
						<th width="8%" class="right-border bottom-border text-right">交纳方式</th>
						<td width="17%" class="right-border bottom-border">
<%--							<input id="JNFS" class="span5" placeholder="必填" check-type="required" check-type="maxlength" maxlength="40" name="JNFS" fieldname="JNFS" type="text" />--%>
							<input class="span12" onclick="javascript:showBH(this);" id="JNFS" type="radio" placeholder="" kind="dic" src="JNFS" name = "JNFS" fieldname="JNFS">
						</td>
					</tr>
					<tr id="show_div_bh">
						<th width="8%" class="right-border bottom-border text-right disabledTh">对方银行</th>
						<td width="17%" class="right-border bottom-border">
							<input id="DFYH" class="span12" style="width:85%;" check-type="maxlength" maxlength="200"  name="DFYH" fieldname="DFYH" type="text" disabled/>
							<button class="btn btn-link"  type="button" onclick="selectYh()" title="点击选择银行"><i class="icon-edit"></i></button>
						</td>
						<th width="8%" class="right-border bottom-border text-right">保函有效期期限</th>
						<td width="17%" class="right-border bottom-border" colspan="3">
							<input id="BHYXQS"  class="span9" style="width:45%;" fieldtype="date" fieldformat="YYYY-MM-DD"  name="BHYXQS" fieldname="BHYXQS" type="date" />至
							<input id="BHYXQZ"  class="span9" style="width:45%;" fieldtype="date" fieldformat="YYYY-MM-DD"  name="BHYXQZ" fieldname="BHYXQZ" type="date" />
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">交纳经办人</th>
						<td width="17%" class="right-border bottom-border">
							<input id="BLR" class="span12" style="width:85%;"  check-type="maxlength" maxlength="36" name="BLR" fieldname="BLR" type="text" readOnly />
							<button class="btn btn-link"  type="button" id="blrBtn_BLR" value="BLR" title="点击选择办理人"><i class="icon-edit"></i></button>
						</td>
						<th id='show_th_bhlx' width="8%" class="right-border bottom-border text-right">保函性质</th>
						<td id='show_td_bhlx' width="17%" class="right-border bottom-border">
							<input class="span12" id="BHLX" type="radio" placeholder="" kind="dic" src="BHLX" name = "BHLX" fieldname="BHLX">
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">备注</th>
						<td width="92%" colspan="5" class="bottom-border">
							<textarea class="span12" rows="2" id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ"></textarea>
						</td>
					</tr>
					 <tr>
				     	<th width="8%" class="right-border bottom-border text-right">附件信息</th>
				     	<td colspan="7" class="bottom-border right-border">
							<div>
								<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0801">
									<i class="icon-plus"></i>
									<span>添加文件...</span>
								</span>
								<table role="presentation" class="table table-striped">
									<tbody fjlb="0801" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
								</table>
							</div>
						</td>
				     </tr>
				     <%
				     	if("detail".equals(type)){
				      %>
				      <tr>
				     	<th width="8%" class="right-border bottom-border text-right">返还日期</th>
						<td width="17%" class="right-border bottom-border" colspan="3">
							<input id="FHRQ" style="width:45%;" class="span9" fieldtype="date" fieldformat="YYYY-MM-DD"  name="FHRQ" fieldname="FHRQ" type="date" />
						</td>
				     </tr>
				     <%} %>
				     
     		</table>
      	</form>
    </div>
  </div>
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id="frmPost">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id="queryXML"/>
		<input type="hidden" name="txtXML" id="txtXML"/>
<%--		<input type="hidden" name="txtFilter" order="desc" fieldname="ND" id="txtFilter"/>--%>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<input type="hidden" id="queryResult"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="ywid" id = "ywid" value="">
	</FORM>
</div>
<form method="post" id="queryForm">
	<!--可以再此处加入hidden域作为过滤条件 -->
	<TR style="display: none;">
		<TD class="right-border bottom-border"></TD>
		<TD class="right-border bottom-border">
			<INPUT type="hidden" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
		</TD>
	</TR>	
</form>
<form method="post" id="queryFormYFDW">
	<!--可以再此处加入hidden域作为过滤条件 -->
	<TR style="display: none;">
		<TD class="right-border bottom-border">
			<input type="hidden" id="QJHSJID" name="JHSJID"  fieldname="JHSJID" operation="="/>
		</TD>
	</TR>	
</form>
</body>
</html>