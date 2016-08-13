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
	if(type==null)
	    type="";
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	String userid = user.getAccount();
	String username = user.getName();
	
	String sysdate = Pub.getDate("yyyy-MM-dd");
	
	String sjbh=request.getParameter("sjbh");
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">

var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglLybzjController.do";
var flag,jbrID, JNFSFalg;
var type ="<%=type%>";


//页面初始化
$(function() {
	init();
	
	//置所有input 为disabled
	$(":input").each(function(i){
	   $(this).attr("disabled", "true");
	 });
	
	$("#id_chose_jndw").removeAttr("onclick");
	
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
});
//页面默认参数
function init(){
	//查询表单赋值
	var data = combineQuery.getQueryCombineData(queryForm,frmPost);
	var data1 = {
		msg : data
	};
	$.ajax({
		url : controllername+"?query&opttype=gcb",
		data : data1,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(response) {
			$("#resultXML").val(response.msg);
			var resultobj = defaultJson.dealResultJson(response.msg);
			$("#demoForm").setFormValues(resultobj);
			if(resultobj.FHRQ!=""){
				$("#id_show_fhrq").show();
			}
		}
	});

	var idval= $("#ID").val();
		
	setFileData(idval,"","","");
	queryFileData(idval,"","","");
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
				</span>    			
      		</h4>
     		<form method="post" id="demoForm">
      			<table class="B-table" width="100%" id="DT1">
      				<input type="hidden" id="ID" fieldname="ID" name="ID"/>
      				<input type="hidden" id="HTID" fieldname="HTID" name="HTID"/>
      				<input type="hidden" id="JHSJID" name="JHSJID"  fieldname="JHSJID" >
      				<input type="hidden" id="XMID" name="XMID"  fieldname="XMID" >
					<input type="hidden" id="BDID" name="BDID"  fieldname="BDID" >
					
			        <tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">招投标名称</th>
						<td width="25%" class="right-border bottom-border">
							<input id="ZTBID"  style="width:88%" class="span12" name="ZTBID" fieldname="ZTBMC" type="text" val="" code="" disabled />
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh" >招标类型</th>
	          			<td width="17%" class="right-border bottom-border text-left" >
	          				<select class="span4" id="ZBLX" type="text" fieldname="ZBLX" name="ZBLX" kind = "dic" src="ZBLX">
	          				</select>
	          			</td>
          			</tr>
          			<tr>
         			<th width="8%" class="right-border bottom-border text-right disabledTh">招标方式</th>
          			<td width="17%" class="right-border bottom-border text-left" >
          				<select class="span4" id="ZBFS" type="text" fieldname="ZBFS" name="ZBFS" kind = "dic" src="ZBFS">
          				</select>
          			</td>
       				<th width="8%" class="right-border bottom-border text-right disabledTh">中标价</th>
          			<td width="17%" class="right-border bottom-border text-left" >
          				<input class="span6" type="number" min="0" id="ZZBJ" name="ZZBJ" fieldname="ZZBJ" style="text-align:right;">&nbsp;&nbsp;<b>(元)</b>
          			</td>
          			</tr><tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">中标单位</th>
						<td width="25%" class="right-border bottom-border">
							<input id="DSFJGID" class="span12" name="DSFJGID" fieldname="DSFJGID" type="text"    disabled />
						</td>
					</tr>
					<tr>
				     	<th width="8%" class="right-border bottom-border text-right disabledTh">合同名称</th>
						<td width="25%" class="right-border bottom-border">
							<input id="HTMC" class="span12" check-type="maxlength" maxlength="50" style="width:95%"  name="HTMC" disabled fieldname="HTMC" type="text" />
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">合同编码</th>
						<td width="25%" class="right-border bottom-border">
							<input id="HTBM"   class="span12" check-type="maxlength" maxlength="100" style="width:95%"  name="HTBM" fieldname="HTBM" disabled type="text" />
						</td>
				     </tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">交纳单位</th>
						<td width="17%" class="right-border bottom-border">
							<input id="JNDW" class="span12" name="JNDW" style="width:95%" fieldname="JNDW" type="text" disabled />
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">交纳日期</th>
						<td width="17%" class="right-border bottom-border">
							<input id="JNRQ" class="span7" type="date" check-type="maxlength" maxlength="10" name="JNRQ" fieldname="JNRQ" disabled/>
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">金额</th>
						<td width="17%" class="right-border bottom-border">
							<input id="JE" class="span9"  value=0 disabled style="width:70%;text-align:right;" check-type="required" check-type="maxlength" maxlength="17" name="JE" fieldname="JE" type="number" />&nbsp;<b>(元)
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">交纳方式</th>
						<td width="17%" class="right-border bottom-border">
<%--							<input id="JNFS" class="span5" placeholder="必填" check-type="required" check-type="maxlength" maxlength="40" name="JNFS" fieldname="JNFS" type="text" />--%>
							<input class="span12" onclick="javascript:showBH(this);" id="JNFS" type="radio" placeholder="" kind="dic" src="JNFS" name = "JNFS" fieldname="JNFS">
						</td>
					</tr>
					<tr id="show_div_bh">
						<th width="8%" class="right-border bottom-border text-right disabledTh">对方银行</th>
						<td width="17%" class="right-border bottom-border">
							<input id="DFYH" class="span12" style="width:95%;" check-type="maxlength" maxlength="200"  name="DFYH" fieldname="DFYH" type="text" disabled/>
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
							<input id="BLR" class="span12" style="width:95%;"  check-type="maxlength" maxlength="36" name="BLR" fieldname="BLR" type="text" readOnly />
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">备注</th>
						<td width="92%" colspan="3" class="bottom-border">
							<textarea class="span12" rows="2" id="BZ" check-type="maxlength" maxlength="4000"  disabled fieldname="BZ" name="BZ"></textarea>
						</td>
					</tr>
					<tr id="id_show_fhrq" style="display:none">
				     	<th width="8%" class="right-border bottom-border text-right disabledTh">返还日期</th>
						<td width="17%" class="right-border bottom-border">
							<input id="FHRQ" style="width:45%;" class="span9" fieldtype="date" fieldformat="YYYY-MM-DD"  placeholder="必填" check-type="required"  name="FHRQ" fieldname="FHRQ" type="date" />
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">返还经办人</th>
						<td width="17%" class="right-border bottom-border">
							<input id="FHR" style="width:85%;" class="span12" check-type="maxlength" maxlength="40"  name="FHR" fieldname="FHR" type="text" readOnly/>
						</td>
				     </tr>
					 <tr>
				     	<th width="8%" class="right-border bottom-border text-right disabledTh">附件信息</th>
				     	<td colspan="3" class="bottom-border right-border">
							<div>
								<table role="presentation" class="table table-striped">
									<tbody fjlb="0801" class="files showFileTab" data-toggle="modal-gallery" disabled data-target="#modal-gallery"  onlyView="true" ></tbody>
								</table>
							</div>
						</td>
				     </tr>
				      <tr>
				     	<th width="8%" class="right-border bottom-border text-right disabledTh">竣工验收时间</th>
						<td width="17%" class="right-border bottom-border">
							<input id="JGYSSJ" style="width:45%;" class="span9" fieldtype="date" fieldformat="YYYY-MM-DD" disabled name="JGYSSJ" fieldname="JGYSSJ" type="date" />
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">结算提报日期</th>
						<td width="17%" class="right-border bottom-border">
							<input id="TBRQ" style="width:45%;" class="span9" fieldtype="date" fieldformat="YYYY-MM-DD" disabled name="TBRQ" fieldname="TBRQ" type="date" />
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
		<input type="hidden" name="txtFilter" id="txtFilter"/>
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
		<TD class="right-border bottom-border">
			<input type="hidden" id="QSJBH" name="SJBH"  fieldname="t.SJBH" operation="=" value="<%=sjbh%>">
		</TD>
	</TR>	
</form>
</body>
</html>