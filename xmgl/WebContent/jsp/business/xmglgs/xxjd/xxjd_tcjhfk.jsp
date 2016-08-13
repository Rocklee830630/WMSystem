<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>形象进度管理-统筹计划反馈</title>
<%
	//获取当前用户信息
	String sysdate = Pub.getDate("yyyy-MM-dd");
	//获取计划数据ID
	String jhsjid = request.getParameter("jhsjid");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xxjd/xxjdController.do";
//页面初始化
$(function() {
	init();
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		 
		if($("#xxjdForm").validationButton())
		{
			 //生成json串
			var data = Form2Json.formToJSON(xxjdForm);
    		var data1 = addjson(convertJson.string2json1(data),"ZT","0");
		    $(window).manhuaDialog.setData(data1);
   			$(window).manhuaDialog.sendData();
   			$(window).manhuaDialog.close();
		   
		}else{
			requireFormMsg();
		  	return;
		}
	});
	//按钮绑定事件(提交)
	$("#btnTj").click(function() {
		if($("#xxjdForm").validationButton())
		{
			
		    var data = Form2Json.formToJSON(xxjdForm);
    		var data1 = addjson(convertJson.string2json1(data),"ZT","1");
    		$(window).manhuaDialog.setData(data1);
   			$(window).manhuaDialog.sendData();
   			$(window).manhuaDialog.close();
		   
		}else{
			requireFormMsg();
		  	return;
		}
	});
	
});
//页面默认参数
function init(){
	var jhsjid = '<%=jhsjid%>';
	if(jhsjid != ""){
		var data = null;
		submit(controllername+"?queryByJhid&jhsjid="+jhsjid,data,xxjdList);
	}
	
}
function submit(actionName, data,tablistID){
	$.ajax({
		type : 'post',
		url : actionName,
		data : data,
		cache : false,
		dataType : "json",  
		async :	false,
		success : function(result) {
			if(result.msg == "0"){
				setFromDate();
			}else{
				var formObj = convertJson.string2json1(result.msg).response.data[0];
				$("#xxjdForm").setFormValues(formObj);
				$("#BDMC").val(formObj.BDID_SV);
				$("#resultXML").val(JSON.stringify(formObj));
			}
		}
	});
}
//回调值
function setFromDate(){
	var pwindow =$(window).manhuaDialog.getParentObj();
	var rowValue = pwindow.document.getElementById("resultXML").value;
	var tempJson = convertJson.string2json1(rowValue);
	$("#xxjdForm").setFormValues(tempJson);
	$("#JHSJID").val(tempJson.GC_JH_SJ_ID);
	$("#XMID").val(tempJson.XMID);
	$("#BDID").val(tempJson.BDID);
	$("#ND").val(tempJson.ND);
	$("#XMBH").val(tempJson.XMBH);
	$("#FKRQ").val('<%=sysdate%>');

}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">项目统筹计划信息
      	<span class="pull-right">
	  		<button id="btnSave" class="btn" type="button">保存</button>
	  		<!-- <button id="btnTj" class="btn" type="button">提交</button> -->
      	</span>
      </h4>
     <form method="post" id="xxjdForm"  >
      <table class="B-table" width="100%" id="xxjdList">
	  	<input type="hidden" id="GC_XMGLGS_XXJD_ID" fieldname="GC_XMGLGS_XXJD_ID" name = "GC_XMGLGS_XXJD_ID"/></TD>
	  	<input type="hidden" id="JHSJID" fieldname="JHSJID" name = "JHSJID"/></TD>
	  	<input type="hidden" id="XMID" fieldname="XMID" name = "XMID"/></TD>
	  	<input type="hidden" id="BDID" fieldname="BDID" name = "BDID"/></TD>
	  	<input type="hidden" id="ND" fieldname="ND" name = "ND"/></TD>
	  	<input type="hidden" id="XMBH" fieldname="XMBH" name = "XMBH"/></TD>
        <tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
       	 	<td class="bottom-border right-border" colspan="3">
         		<input class="span12" id="XMMC" type="text" fieldname="XMMC" name = "XMMC" keep="true"  readonly />
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
       		<td class="bottom-border right-border">
         		<input class="span12" id="BDMC" type="text" fieldname="BDMC" name = "BDMC" keep="true" readonly />
         	</td>
        </tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right disabledTh">业主代表</th>
       		<td class="bottom-border right-border" >
         		<input class="span12" id="YZDB"  type="text"  fieldname="YZDB" name = "YZDB" readonly/>
         	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">监理单位</th>
       		<td class="bottom-border right-border">
         		<input class="span12" id="JLDW" type="text" fieldname="JLDW" name = "JLDW" keep="true" readonly />
         	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">施工单位</th>
       		<td class="bottom-border right-border">
         		<input class="span12" id="SGDW" type="text" fieldname="SGDW" name = "SGDW" keep="true" readonly />
         	</td>
         </tr>
         <tr>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">年度目标</th>
       		<td class="bottom-border right-border">
         		<input class="span12"  id="JSMB" type="text" fieldname="JSMB" name = "JSMB" keep="true" readonly />
         	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">计划开工时间</th>
       		<td class="bottom-border right-border">
         		<input class="span12"  id="KGSJ" type="text" fieldname="KGSJ" name = "KGSJ" keep="true" readonly />
         	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">计划完工时间</th>
       		<td class="bottom-border right-border">
         		<input class="span12"  id="WGSJ" type="text" fieldname="WGSJ" name = "WGSJ" keep="true" readonly />
         	</td>
        </tr>
      </table>
      <h4 id="xxjd" class="title">项目统筹计划反馈</h4>
		<table id="T_xxjd" class="B-table" width="100%">
			<tr>
	          <th width="5%" class="right-border bottom-border">反馈日期 </th>
	          <td class="right-border bottom-border">
	          	<input class="span12 date" id="FKRQ" type="date" check-type="required" name="FKRQ" fieldname="FKRQ"/>
	          </td>
	          <th width="5%" class="right-border bottom-border">实际开工时间</th>
	          <td class="bottom-border">
	          	<input class="span12 date" id="SJKGSJ" type="date" name="SJKGSJ" fieldname="SJKGSJ"/>
	          </td>
	          <th width="5%" class="right-border bottom-border">实际完工时间</th>
	          <td  class="bottom-border">
	          	<input class="span12 date" id="SJWGSJ" type="date" name="SJWGSJ" fieldname="SJWGSJ"/>
	          </td>
        	</tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">进展反馈 </th>
	          <td class="right-border bottom-border" colspan="5">
	          	<textarea class="span12" id="JZFK" rows="3" name ="JZFK" fieldname="JZFK" maxlength="2000"></textarea>
	          </td>
        	</tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">风险描述 </th>
	          <td class="right-border bottom-border" colspan="5">
	          	<textarea class="span12" id="FXMS" rows="3" name ="FXMS" fieldname="FXMS" maxlength="2000"></textarea>
	          </td>
        	</tr>
        	<tr>
	          <th width="5%" class="right-border bottom-border">备注 </th>
	          <td class="right-border bottom-border" colspan="5">
	          	<textarea class="span12" id="BZ" rows="3" name ="BZ" fieldname="BZ" maxlength="4000"></textarea>
	          </td>
        	</tr>
		</table>
      </form>
    </div>
   </div>
  </div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "XMBH,LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>

</html>