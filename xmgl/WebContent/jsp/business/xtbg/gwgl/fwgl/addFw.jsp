<!DOCTYPE html>
<html>
<head>
<%@ page import="com.ccthanking.framework.common.User" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%
	String date = com.ccthanking.framework.util.Pub.getDate("yyyy-MM-dd");
	String id=request.getParameter("id");
	String xx=request.getParameter("xx");
	String sjbh=request.getParameter("sjbh");
	
	User user = (User)session.getAttribute(com.ccthanking.framework.Globals.USER_KEY);
	String dept = user.getDepartment();
	String account = user.getAccount();
	
	String type=request.getParameter("type") == null ? "" : request.getParameter("type");
	String eventSjbh=request.getParameter("eventSjbh") == null ? "" : request.getParameter("eventSjbh");
%>
<app:base />
<script type="text/javascript" charset="UTF-8">

var controllername= "${pageContext.request.contextPath }/fwglController.do";

var id="<%=id %>";
var sjbh="<%=sjbh %>";
var type = "<%=type%>";

$(function() {
	if(type == "1") {
		$("#saveInfo").hide();
	} else {
		$("#chakan").hide();
	}
});
//初始化加载
$(document).ready(function(){
	
	var obj=<%=xx %>;//json对象
	//var nr = ''
	$("#demoForm").setFormValues(obj);
	if(id == null || id == "null" || id == undefined) {
		var date = "<%=date %>";
 		var json_fz='{"NGRQ":\"'+date+'\"}';
		var obj_fz=eval("("+json_fz+")");
		$("#demoForm").setFormValues(obj_fz);
		
		// 【缓急】字典默认平件
		$("#HJ").val("1");
		//
		$("#NGDW").val("<%=dept%>");
		//
		$("#NGR").val("<%=account%>");
		// 带文号默认是
	    $("input:radio[name=WZ][value='1']").attr("checked",true);
	} else {
		//update by liujs at 2013年11月13日 18:14:59 start
		var fwid=$("#FWID").val();
		//删除状态为0的附件
		deleteFileData(fwid,"","","");
		// 加载附件
		setFileData(fwid,"",sjbh,"200503","0");
		// 查询附件
		queryFileData(fwid,"","","");
		//update end

	}
});

//点击保存按钮
$(function() {
	var saveUserInfoBtn = $("#saveInfo");
	saveUserInfoBtn.click(function() {
		if($("#demoForm").validationButton()) {
			//生成json串
			var data = Form2Json.formToJSON(demoForm);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			//通过判断id是否为空来判断是插入还是修改
			
			var success = false;
			if(id == null || id == "null" || id == undefined) {
				var ywid = $("#ywid").val();
				success = defaultJson.doInsertJson(controllername + "?executeFw&operatorSign=1&ywid="+ywid, data1, null, 'updateCallBack');
			} else {
				success = defaultJson.doUpdateJson(controllername + "?executeFw&operatorSign=2", data1, null, 'updateCallBack');
			}
		} else {
			requireFormMsg();
		  	return;
		}
	});
	
	//监听变化事件
   	$("#NGDW").change(function() {
   		generatePc($("#NGR"));
   	}); 
});

function updateCallBack() {
	$(window).manhuaDialog.setData("");
	$(window).manhuaDialog.sendData();
	$(window).manhuaDialog.close();
}



var popPage;
//选中项目名称弹出页
function selectXm(){
	popPage = "selectXm";
//	$(window).manhuaDialog({"title":"发文管理->选择项目","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/kfglgl/xmcx.jsp","modal":"1"});
	queryProjectWithBD();;
}


 //人员查询
function generatePc(ndObj){
	var condition = $("#NGDW").val() == "" ? "" : "and DEPARTMENT = "+$("#NGDW").val() + " order by sort";
	ndObj.attr("src", "T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG=1  AND PERSON_KIND = '3' "+condition);
	ndObj.attr("kind", "dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
}

//弹出区域回调
getWinData = function(data){
	if(popPage == "selectXm") {
		$("#SSXM").val(JSON.parse(data).XMMC);
	}
};


function chakan(){

	if("<%=eventSjbh%>" == "0") {
		requireFormMsg("此发文还未发起流程，无法查看审批意见");
		return;
	}
	$(window).manhuaDialog({"title":"审批意见","type":"text","content":"<%=request.getContextPath()%>/jsp/framework/common/aplink/spYjView.jsp?sjbh=<%=sjbh%>","modal":"2"});	
}

function clickCancelBtn()
{
	$(window).manhuaDialog.close();
}
</script>    
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">
      		<h4 class="title">
				<span class="pull-right">
					<button id="chakan" type="button" class="btn" name="chakan" onClick="chakan()">查看办理意见</button>
					<button id="saveInfo" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
				</span>
      		</h4>
     		<form method="post" id="demoForm"  >
      			<table class="B-table" width="100%" id="DT1">
      				<TR  style="display:none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<input type="text" kind="text" fieldname="FWID" name="FWID" id="FWID">
						</TD>
					</TR>
					<tr>
						<th width="5%" class="right-border bottom-border text-right">文件标题</th>
						<td width="29%" colspan="3" class="right-border bottom-border">
							<input id="WJBT" class="span12" type="text"  placeholder="必填" check-type="required" name="WJBT" maxlength="500" fieldname="WJBT"/>
						</td>
						
						<!-- <th width="5%" class="right-border bottom-border text-right">文件编号</th>
						<td width="12%" class="right-border bottom-border">
							<input class="span12" type="text" name="WJBH" maxlength="50" fieldname="WJBH" id="WJBH"></td> -->
						<th width="5%" class="right-border bottom-border text-right">拟稿日期</th>
						<td width="12%" class="right-border bottom-border">
							<input id="NGRQ" class="span12" type="date" name="NGRQ" fieldname="NGRQ" fieldtype="date" fieldformat="YYYY-MM-DD"/></td>
							
						<th width="5%" class="right-border bottom-border text-right">打印份数</th>
						<td width="12%" class="right-border bottom-border">
							<input class="span12" style="text-align:right" placeholder="" check-type="number maxlength" type="number" name="DYFS" maxlength="3" fieldname="DYFS" id="DYFS" min="0"/></td>
						
					</tr>
					
					<tr>
							
						<th class="right-border bottom-border text-right">所属项目</th>
						<td class="right-border bottom-border" colspan="3">
							<input class="span12" style="width: 80%" type="text" name="SSXM" maxlength="100" fieldname="SSXM" id="SSXM"/>
							<button class="btn btn-link"  type="button" onclick="selectXm()" title="点击选择项目"><i class="icon-edit"></i></button></td>
							
						<th class="right-border bottom-border text-right">拟稿单位</th>
						<td class="right-border bottom-border">
							<select class="span12" check-type="required" type="text" disabled name="NGDW" maxlength="100" fieldname="NGDW" id="NGDW" kind="dic"
										src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME" ></select></td>
						
						<th class="right-border bottom-border text-right">拟稿人</th>
						<td class="right-border bottom-border">
							<select class="span12" check-type="required" type="text" disabled name="NGR" maxlength="30" fieldname="NGR" id="NGR" kind="dic" src="T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG = 1  AND PERSON_KIND = '3'"></select>
						</td>
						
					</tr>
					
					<tr>
					
						<th class="right-border bottom-border text-right">主送</th>
						<td class="right-border bottom-border" colspan="3">
							<select class="span12" id="ZS" fieldname="ZS" name="ZS" style="width: 80%" kind="dic" category="xtbg_gwgl_zs" src="T#FS_COMMON_DICT:DICT_ID:DICT_NAME:DICT_CATEGORY='xtbg_gwgl_zs' order by pxh"></select>
							<button class="btn btn-link"  type="button" onclick="selectLwdw('ZS')" title="点击选择主送"><i class="icon-edit"></i></button></td>
						
						<th class="right-border bottom-border text-right">缓急</th>
						<td class="right-border bottom-border">
							<select id="HJ" class="span12" type="text" name="HJ" maxlength="100" fieldname="HJ" kind="dic" src="HJ1000000000302"></select>
						</td>
						
						<th class="right-border bottom-border text-right">带文号</th>
						<td class="right-border bottom-border" align="center">
						<!-- 	<input id="WZ" class="span12" style="width: 70%" type="text" name="WZ" maxlength="100" fieldname="WZ"/>
							<button class="btn btn-link"  type="button" onclick="selectWz()" title="点击选择文种"><i class="icon-edit"></i></button> -->
					<!-- 		是<input class="span12" type="radio" id="WZ" name="WZ" fieldname="WZ" value="1" checked="checked">&nbsp;&nbsp;&nbsp;
							否<input class="span12" type="radio" id="WZ" name="WZ" fieldname="WZ" value="2"> -->
			    			  <input class="span12" id="WZ" type="radio" placeholder="" kind="dic" src="WZ" name = "WZ" fieldname="WZ">
						</td>
						
					</tr>
					
					<tr>
						<th class="right-border bottom-border text-right">抄送</th>
						<td class="right-border bottom-border" colspan="3">
							<select class="span12" id="CS" fieldname="CS" name="CS" style="width: 80%" kind="dic" category="xtbg_gwgl_cs" src="T#FS_COMMON_DICT:DICT_ID:DICT_NAME:DICT_CATEGORY='xtbg_gwgl_cs' order by pxh"></select>
							<button class="btn btn-link"  type="button" onclick="selectLwdw('CS')" title="点击选择抄送"><i class="icon-edit"></i></button></td>
						
						<th class="right-border bottom-border text-right">联合发文</th>
						<td class="right-border bottom-border" colspan="3">
							<input class="span12" type="text" name="LHFW" maxlength="100" fieldname="LHFW" id="LHFW"/></td>
							
					</tr>
					<tr>
			        	<th class="right-border bottom-border text-right">附件信息</th>
			        	<td colspan="7" class="bottom-border right-border">
							<div>
								<input type="hidden" name="ywid" id="ywid" value="">  
								<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="3006">
									<i class="icon-plus"></i>
									<span>添加文件...</span>
								</span>
								<table role="presentation" class="table table-striped">
									<tbody fjlb="3006" class="files showFileTab"
										data-toggle="modal-gallery" data-target="#modal-gallery">
									</tbody>
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
         <input type="hidden" name="queryXML" id="queryXML">
         <input type="hidden" name="txtXML" id="txtXML">
         <input type="hidden" name="txtFilter" order="desc" fieldname="ND" id="txtFilter">
         <input type="hidden" name="resultXML" id="resultXML">
         <input type="hidden" id="queryResult" name="queryResult"/>
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">		
 	</FORM>
 </div>
</body>
</html>