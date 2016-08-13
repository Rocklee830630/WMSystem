<!DOCTYPE html>
<html>
<head>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.common.QuerySet"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%
	String date = com.ccthanking.framework.util.Pub.getDate("yyyy-MM-dd");
	String nd = com.ccthanking.framework.util.Pub.getDate("yyyy");
	String ywlx = com.ccthanking.common.YwlxManager.OA_GWGL_SWGL;
	
/* 	String sql = "SELECT MAX(SERIAL_NUM) MAXNUM FROM XTBG_GWGL_SWGL T WHERE TO_CHAR(SWRQ,'YYYY')='" + nd + "' AND T.SWLB='1'";
	QuerySet qs = DBUtil.executeQuery(sql, null);
	String maxId = qs.getString(1, "MAXNUM");
	maxId = maxId == null ? "0" : maxId;
	int maxIdInt = Integer.parseInt(maxId) + 1;
	maxId = Pub.formatSerialNumber(maxIdInt, 3); */
	
	String id=request.getParameter("id");
	String xx=request.getParameter("xx");
	String sjbh=request.getParameter("sjbh");
	
	String type=request.getParameter("type") == null ? "" : request.getParameter("type");
	String eventSjbh=request.getParameter("eventSjbh") == null ? "" : request.getParameter("eventSjbh");
%>
<app:base />
<script type="text/javascript" charset="UTF-8">

var controllername= "${pageContext.request.contextPath }/swglController.do";

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
 		var json_fz='{"SWRQ":\"'+date+'\"}';
		var obj_fz=eval("("+json_fz+")");
		$("#demoForm").setFormValues(obj_fz);
		
		// 【缓急】字典默认平件
		$("#HJ").val("1");
		
		selectSwlb();
	} else {
		//update by liujs at 2013年11月13日 18:14:59 start
		var swid=$("#SWID").val();
		//删除状态为0的附件
		deleteFileData(swid,"","","");
		// 加载附件
		setFileData(swid,"",sjbh,"200502","0");
		// 查询附件
		queryFileData(swid,"","","");
		//update end
	}
});

function setSwbh() {
	var swlb = $("input:radio[name=SWLB]:checked").val();
	var temp = swlb == "1" ? "收文" : "临";
	
	var serialNumber = $("#SERIAL_NUM").val();
	// 截取最后三位
	var swbh = temp + "[<%=nd%>]"+serialNumber+"号";
	
	$("#SWBH").val(swbh);
}

function selectSwlb() {
	var swlb = $("input:radio[name=SWLB]:checked").val();
	$.ajax({
		url : controllername+"?querySwbh&swlb="+swlb,
		cache : false,
		async : false,
		dataType : 'json',
		success : function(response) {
			var result = convertJson.string2json1(response);
			$("#SERIAL_NUM").val(result.swbh);
			setSwbh();
		}
	});
}

//点击保存按钮
$(function() {
	var saveUserInfoBtn = $("#saveInfo");
	saveUserInfoBtn.click(function() {
		if($("#demoForm").validationButton()) {
			var swrq = $("#SWRQ").val();
			var fwrq = $("#FWRQ").val();
			if(swrq!=""&&fwrq!=""){
				
				if(swrq<fwrq){
					xInfoMsg('收文日期不得小于发文日期！',"");
					return
				}
				
			}
			
			//生成json串
			var data = Form2Json.formToJSON(demoForm);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			//通过判断id是否为空来判断是插入还是修改
			
			var success = false;
			if(isUnique()) {
				if(id == null || id == "null" || id == undefined) {
					var ywid = $("#ywid").val();
					success = defaultJson.doInsertJson(controllername + "?executeSw&operatorSign=1&ywid="+ywid, data1, null);
				} else {
					success = defaultJson.doUpdateJson(controllername + "?executeSw&operatorSign=2", data1, null);
				}
 			} else {
				xFailMsg("收文号重复，请重新填写","");
				return;
			}
			if(success){
				$(window).manhuaDialog.setData("");
				$(window).manhuaDialog.sendData();
				$(window).manhuaDialog.close();
			} 
		} else {
			requireFormMsg();
		  	return;
		}
	});
	
});

var popPage;
//选中项目名称弹出页
function selectXm(){
	popPage = "selectXm";
//	$(window).manhuaDialog({"title":"收文管理->选择项目","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/kfglgl/xmcx.jsp","modal":"1"});
	queryProjectWithBD();
}

//弹出区域回调
getWinData = function(data){
	if(popPage == "selectXm") {
		$("#SSXM").val(JSON.parse(data).XMMC);
	}
};

function chakan(){

	if("<%=eventSjbh%>" == "0") {
		requireFormMsg("此收文还未发起流程，无法查看审批意见");
		return;
	}
	$(window).manhuaDialog({"title":"审批意见","type":"text","content":"<%=request.getContextPath()%>/jsp/framework/common/aplink/spYjView.jsp?sjbh=<%=sjbh%>","modal":"2"});	
}

function clickCancelBtn()
{
	$(window).manhuaDialog.close();
}

function isUnique() {
	var flag = false;
	var swlb = $("input:radio[name=SWLB]:checked").val();
	$.ajax({
		url : controllername+"?queryUnique&serial_num="+$("#SERIAL_NUM").val()+"&swlb="+swlb+"&swid="+$("#SWID").val(),
		cache : false,
		async : false,
		dataType : 'json',
		success : function(response) {
			var result = eval("(" + response + ")");
			flag = result.sign == 0 ? true : flag;
		}
	});
	return flag;
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
							<input type="text" kind="text" fieldname="SWID" name="SWID" id="SWID">
							<input type="text" kind="text" fieldname="SJBH" name="SJBH" id="SJBH">
						</TD>
					</TR>
					<tr>
						<th width="5%" class="right-border bottom-border text-right">文件标题</th>
						<td width="29%" colspan="3" class="right-border bottom-border">
							<input id="WJBT" class="span12" type="text"  placeholder="必填" check-type="required" name="WJBT" maxlength="100" fieldname="WJBT"/>
						</td>
						
						
						<th width="5%" class="right-border bottom-border text-right">收文日期</th>
						<td width="12%" class="right-border bottom-border">
							<input id="SWRQ" class="span12" type="date"  placeholder="必填" check-type="required" name="SWRQ" fieldname="SWRQ" fieldtype="date" fieldformat="YYYY-MM-DD"/>
						</td>
						
						<th width="5%" class="right-border bottom-border text-right">发文日期</th>
						<td width="12%" class="right-border bottom-border">
							<input id="FWRQ" class="span12" type="date" name="FWRQ" fieldname="FWRQ" fieldtype="date" fieldformat="YYYY-MM-DD"/>
						</td>
						
					</tr>
					
					<tr>
							
						<th class="right-border bottom-border text-right">所属项目</th>
						<td class="right-border bottom-border" colspan="3">
							<input class="span12" style="width: 80%" type="text" name="SSXM" maxlength="100" fieldname="SSXM" id="SSXM"/>
							<button class="btn btn-link"  type="button" onclick="selectXm()"><i class="icon-edit" title="点击选择项目"></i></button></td>
						
						<th class="right-border bottom-border text-right">收文号</th>
						<td class="right-border bottom-border">
							<input class="span12" placeholder="必填" check-type="required" type="text" onchange="setSwbh()" onkeyup="setSwbh()" id="SERIAL_NUM" name="SERIAL_NUM" fieldname="SERIAL_NUM"></td>
								
						<th class="right-border bottom-border text-right">收文编号</th>
						<td class="right-border bottom-border">
							<input class="span12" placeholder="必填" check-type="required" disabled type="text" name="SWBH" maxlength="20" fieldname="SWBH" id="SWBH"/>
						</td>
						
					</tr>
					
					<tr>
						<th class="right-border bottom-border text-right">来文单位</th>
						<td class="right-border bottom-border" colspan="3">
							<select class="span12" id="LWDW" placeholder="必填" check-type="required" style="width: 80%" kind="dic" category="xtbg_gwgl_lwdw" src="T#FS_COMMON_DICT:DICT_ID:DICT_NAME:DICT_CATEGORY='xtbg_gwgl_lwdw' order by pxh" fieldname="LWDW" name="LWDW"></select>
							<button class="btn btn-link"  type="button" onclick="selectLwdw('LWDW')"><i class="icon-edit" title="点击选择来文单位"></i></button></td>
						
						<th class="right-border bottom-border text-right">文种</th>
						<td class="right-border bottom-border">
							<!-- 
							<input id="WZ" class="span12" style="width: 70%" type="text" category="xtbg_gwgl_wz" name="WZ" maxlength="100" fieldname="WZ"/>
							 -->
							<select class="span12" id="WZ" style="width: 80%" kind="dic" category="xtbg_gwgl_wz" src="T#FS_COMMON_DICT:DICT_ID:DICT_NAME:DICT_CATEGORY='xtbg_gwgl_wz' order by pxh" placeholder="必填" check-type="required" fieldname="WZ" name="WZ"></select>
							<button class="btn btn-link"  type="button"  onclick="selectLwdw('WZ')" title="点击选择文种"><i class="icon-edit"></i></button></td>
						
						<th class="right-border bottom-border text-right">缓急</th>
						<td class="right-border bottom-border">
							<select id="HJ" class="span12" type="text" name="HJ" maxlength="10" fieldname="HJ" kind="dic" src="HJ1000000000302"></select>
						</td>
					</tr>
					<tr>
						<th class="right-border bottom-border text-right">文号</th>
						<td class="right-border bottom-border" colspan="3">
							<input class="span12" type="text" name="WH" maxlength="100" fieldname="WH" id="WH"/></td>
						
						<th class="right-border bottom-border text-right">文号类别</th>
						<td class="right-border bottom-border" align="center">
			    			  <input class="span12" id="SWLB" onclick="selectSwlb()" type="radio" placeholder="" kind="dic" src="E#1=收文编号:2=临时编号" defaultValue="1" name="SWLB" fieldname="SWLB"></td>
						
						<th></th>
						<td></td>
					</tr>
					<tr>
			        	<th class="right-border bottom-border text-right">附件信息</th>
			        	<td colspan="7" class="bottom-border right-border">
							<div>
								<input type="hidden" name="ywid" id="ywid" value="">  
								<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="3005">
									<i class="icon-plus"></i>
									<span>添加文件...</span>
								</span>
								<table role="presentation" class="table table-striped">
									<tbody fjlb="3005" class="files showFileTab"
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