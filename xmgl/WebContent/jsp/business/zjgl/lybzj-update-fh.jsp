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
    		$("#FHQK").val("6");
    		//生成json串
    		var data = Form2Json.formToJSON(demoForm);
		  	//组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  	//调用ajax插入
    		var flag = defaultJson.doUpdateJson(controllername + "?update&opttype=fh", data1);
    		if(flag){
    			$(window).manhuaDialog.close();
    		}
		}else{
			requireFormMsg();
		  	return;
		}
    });
  	
    if($("#ID").val() == "" || $("#ID").val() == null){
    	$("input[name=JNFS]:eq(1)").attr("checked",'checked'); 
    }
    
    $("button[id^='blrBtn_']").bind("click", function(){
    	var valu = $(this).attr("value");
    	jbrID = valu;
    	var actorCode = $("#"+valu).attr("code");
		openUserTree('single',actorCode,'setBlr') ;
	});
    
    <%
		}else{
	%>
	//置所有input 为disabled
	$(":input").each(function(i){
	   $(this).attr("disabled", "true");
	 });
	
	<%
		}
	%>
	
	//设置保函是否显示
	if(JNFSFalg=="BH"){
		//对保函的处理，显示对方银行 保函起止日期等信息.
		$("#show_div_bh").show();
	}else if(JNFSFalg=="XJ"){
		$("#show_div_bh").hide();
	}
	
	$("#btnPrint").click(function(){
		window.print();
	});
	
	$("input[name=JNFS]:eq(1)").attr("disabled",true); 
	$("input[name=JNFS]:eq(2)").attr("disabled",true); 
});
//页面默认参数
function init(){
	if(type == "insert"){
		$("#JNRQ").val('<%=sysdate%>');
<%--		$("#BLR").val('<%=username%>');--%>
	}else if(type == "update" || type == "detail"){
		
		var parentmain=$(this).manhuaDialog.getParentObj();	
		var rowValue = parentmain.$("#resultXML").val();
		var tempJson = convertJson.string2json1(rowValue);
		
		JNFSFalg = tempJson.JNFS;
		
		//表单赋值
		$("#demoForm").setFormValues(tempJson);
		
		setFileData(tempJson.ID,"","","");
		queryFileData(tempJson.ID,"","","");

		$("#FHRQ").val('<%=sysdate%>');
		$("#FHR").val('<%=username%>');
		
	}
}


//选中项目名称弹出页
function selectHt(){
	$(window).manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath}/jsp/business/htgl/htcx.jsp","modal":"2"});
}

//弹出区域回调
getWinData = function(data){
	$("#JHSJID").val(JSON.parse(data).GC_JH_SJ_ID);
	$("#XMID").val(JSON.parse(data).XMID);
	$("#BDID").val(JSON.parse(data).BDID);
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#BDMC").val(JSON.parse(data).BDMC);
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
}

function closeNowCloseFunction(){
	return true;
}

function showBH(t){
	if(t.value=="BH"){
		//对保函的处理，显示对方银行 保函起止日期等信息.
		$("#show_div_bh").show();
	}else if(t.value=="XJ"){
		$("#show_div_bh").hide();
	}
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
		      			<button id="btnClear" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>
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
					<input type="hidden" id="FHQK" name="FHQK"  fieldname="FHQK" >
<%--					<tr>--%>
<%--						<th width="8%" class="right-border bottom-border text-right">合同名称</th>--%>
<%--			       	 	<td class="bottom-border right-border" width="23%">--%>
<%--			         		<input class="span12" style="width:85%" id="HTMC" type="text" placeholder="必填" check-type="required" fieldname="HTMC" name = "HTMC"  disabled />--%>
<%--			          		<button class="btn btn-link"  type="button" onclick="selectHt()"><i class="icon-edit"></i></button>--%>
<%--			       	 	</td>--%>
<%--			         	<th width="8%" class="right-border bottom-border text-right">合同编码</th>--%>
<%--			       		<td class="bottom-border right-border"width="10%">--%>
<%--			         		<input class="span12" style="width:100%" id="HTBM" type="text" fieldname="HTBM" name = "HTBM"  disabled/>--%>
<%--			         	</td>--%>
<%--			         	<th width="8%" class="right-border bottom-border text-right">乙方单位</th>--%>
<%--			         	<td width="25%" class="right-border bottom-border">--%>
<%--							<input id="YFDW" class="span12" name="YFDW" fieldname="YFDW" type="text" disabled/>--%>
<%--						</td>--%>
<%--			        </tr>--%>
			         <tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">招投标名称</th>
						<td width="25%" class="right-border bottom-border">
							<input id="ZTBID"   class="span12" name="ZTBID" fieldname="ZTBMC" type="text" val="" code="" disabled />
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
<%--							<i width="10%" class="icon-edit" title="请选择单位" onclick="xzxm('2');"></i>--%>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">交纳日期</th>
						<td width="17%" class="right-border bottom-border">
							<input id="JNRQ" class="span7" type="date" check-type="maxlength" maxlength="10" name="JNRQ" fieldname="JNRQ" disabled/>
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh disabledTh">金额</th>
						<td width="17%" class="right-border bottom-border">
							<input id="JE" class="span9" placeholder="必填" value=0 style="width:70%;text-align:right;" check-type="required" check-type="maxlength" maxlength="17" name="JE" fieldname="JE" type="number"  disabled/>&nbsp;<b>(元)
						</td>
						<th width="8%" class="right-border bottom-border text-right">交纳方式</th>
						<td width="17%" class="right-border bottom-border">
<%--							<input id="JNFS" class="span5" placeholder="必填" check-type="required" check-type="maxlength" maxlength="40" name="JNFS" fieldname="JNFS" type="text" />--%>
							<input class="span12" onclick="javascript:showBH(this);" id="JNFS" type="radio" placeholder="" kind="dic" src="JNFS" name = "JNFS" fieldname="JNFS" disabled>
						</td>
					</tr>
					<tr id="show_div_bh">
						<th width="8%" class="right-border bottom-border text-right disabledTh">对方银行</th>
						<td width="17%" class="right-border bottom-border">
							<input id="DFYH" class="span12" check-type="maxlength" maxlength="200"  name="DFYH" fieldname="DFYH" type="text" disabled/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">保函有效期期限</th>
						<td width="17%" class="right-border bottom-border" colspan="3">
							<input id="BHYXQS"  class="span9" style="width:45%;" fieldtype="date" fieldformat="YYYY-MM-DD"  name="BHYXQS" fieldname="BHYXQS" type="date" disabled/>至
							<input id="BHYXQZ"  class="span9" style="width:45%;" fieldtype="date" fieldformat="YYYY-MM-DD"  name="BHYXQZ" fieldname="BHYXQZ" type="date" disabled/>
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">交纳经办人</th>
						<td width="17%" class="right-border bottom-border">
							<input id="BLR" class="span12" style="width:85%;"  check-type="maxlength" maxlength="36" name="BLR" fieldname="BLR" type="text" readOnly />
						</td>
					</tr>
<%-- 					<tr>--%>
<%--						<th width="8%" class="right-border bottom-border text-right">返还情况</th>--%>
<%--						<td width="17%" class="right-border bottom-border">--%>
<%--							<input id="FHQK" class="span3" placeholder="必填" check-type="required" check-type="maxlength" maxlength="40" name="FHQK" fieldname="FHQK" type="text" />--%>
<%--						</td>--%>
<%--						<th width="8%" class="right-border bottom-border text-right">返还日期</th>--%>
<%--						<td width="17%" class="right-border bottom-border">--%>
<%--							<input id="FHRQ" class="span9" type="date" name="FHRQ" fieldname="FHRQ" />--%>
<%--						</td>--%>
<%--						<th width="8%" class="right-border bottom-border text-right">办理人</th>--%>
<%--						<td width="17%" class="right-border bottom-border">--%>
<%--							<input id="BLR" class="span12" style="width:80%;"  check-type="maxlength" maxlength="36" name="BLR" fieldname="BLR" type="text" />--%>
<%--							<button class="btn btn-link"  type="button" id="blrBtn_BLR" value="BLR"><abbr title="点击选择办理人"><i class="icon-edit"></i></abbr></button>--%>
<%--						</td>--%>
<%--						<th width="8%" class="right-border bottom-border text-right">凭证编号</th>--%>
<%--						<td width="17%" class="right-border bottom-border">--%>
<%--							<input id="PZBH" class="span12" check-type="maxlength" maxlength="100" name="PZBH" fieldname="PZBH" type="text" />--%>
<%--						</td>--%>
<%--					</tr>--%>
					<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">备注</th>
						<td width="92%" colspan="5" class="bottom-border">
							<textarea class="span12" rows="2" id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ" disabled></textarea>
						</td>
					</tr>
					<tr>
				     	<th width="8%" class="right-border bottom-border text-right">返还日期</th>
						<td width="17%" class="right-border bottom-border">
							<input id="FHRQ" style="width:45%;" class="span9" fieldtype="date" fieldformat="YYYY-MM-DD"  placeholder="必填" check-type="required"  name="FHRQ" fieldname="FHRQ" type="date" />
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">返还经办人</th>
						<td width="17%" class="right-border bottom-border">
							<input id="FHR" style="width:85%;" class="span12" check-type="maxlength" maxlength="40"  name="FHR" fieldname="FHR" type="text" readOnly/>
							<button class="btn btn-link"  type="button" id="blrBtn_FHR" value="FHR"  title="点击选择办理人"><i class="icon-edit"></i></button>
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
									<tbody fjlb="0801" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery" deleteUser="<%=user.getAccount()%>"></tbody>
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
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id="frmPost">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id="queryXML"/>
		<input type="hidden" name="txtXML" id="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" fieldname="ND" id="txtFilter"/>
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
</body>
</html>