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
<title>提请款-维护</title>
<%
	String type=request.getParameter("type");
	String jhsjid=request.getParameter("jhsjid");
	//获取当前用户信息
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglZszjZfzsbController.do";
var controllernameZF= "${pageContext.request.contextPath }/zjgl/gcZjglZszjZfzsbController.do";
var controllernameSY= "${pageContext.request.contextPath }/zjgl/gcZjglZszjXmzszjqkController.do"
var type ="<%=type%>";

var jhsjid='<%=jhsjid%>';
var xmrowValues;
//页面初始化
$(function() {
	init();
});
//页面默认参数
function init(){
	if(type == "detail"){
	
		$("#QJHSJID").val(jhsjid);
		$("#QID").val(jhsjid);
		
		//查询明细
		var dataMx = combineQuery.getQueryCombineData(queryFormMx,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllernameZF+"?query",dataMx,DT1);
		
		//查询明细
		var dataMx2 = combineQuery.getQueryCombineData(queryFormMx,frmPost,DT2);	
		//调用ajax插入
		defaultJson.doQueryJsonList(controllernameSY+"?query",dataMx2,DT2);	
		
		//alert(JSON.stringify(tempJson));
		//查询记录数
		
		//表单赋值
			var data = combineQuery.getQueryCombineData(queryForm,frmPost);	
			var data1 = {
				msg : data
			};	
			//计算使用和拨付笔数
			$.ajax({	
				url : controllername+"?queryZjInfo",	
					data : data1,	
				cache : false,	
				async :	false,	
				dataType : "json",
				type : 'post',		
				success : function(response) {	
					$("#resultXML").val(response.msg);	
					var resultobj = defaultJson.dealResultJson(response.msg);	
					$("#gcZjglTqkForm").setFormValues(resultobj);	
					$("#bsbf").text("共"+resultobj.BSBF+"笔");
					$("#bssy").text("共"+resultobj.BSSY+"笔");
				}
		});
	}
}


//点击行事件
function tr_click(obj,tabListid){
	//alert(JSON.stringify(obj));
	//$("#gcZjglTqkForm").setFormValues(obj);
	if(tabListid='gcZjglTqkmxList'){
		$("#QMXID").val(obj.ID);
		$("#btnUpdateMx").attr("disabled", false);
		$("#btnDelete").attr("disabled", false);
	}
}
//弹出区域回调
getWinData = function(data){
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#XMBH").val(JSON.parse(data).XMBH);
	$("#GC_TCJH_XMXDK_ID").val(JSON.parse(data).GC_TCJH_XMXDK_ID);
};
function doBDMC(obj){
	if(obj.BDMC==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
	<div class="B-small-from-table-autoConcise" style="display:black;">
	<input type="hidden" id="jhsjids" name="jhsjids"/>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
			<TD class="right-border bottom-border">
				<input class="span12" type="text" id="QID" name="ID"  fieldname="fdt.jhsjid" value="" operation="=" >
			</TD>
        </TR>
      </table>
      </form>
	</div>
	
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">项目征收资金信息
      </h4>

     <form method="post" id="gcZjglTqkForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
      	<input type="hidden" id="TQKZT" fieldname="TQKZT" name = "TQKZT" value="0"/></TD>
		
		
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
			<td width="25%" class="right-border bottom-border">
				<input id="XMMC" class="span12" style="width:95%" name="XMMC" fieldname="XMMC" type="text" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
			<td width="25%" class="right-border bottom-border">
				<input id="BDMC" class="span12" name="BDMC" style="width:95%"  fieldname="BDMC" type="text" disabled/>
			</td>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">拨付金额（元）</th>
				<td width="25%" class="right-border bottom-border">
					<input id="ZXSJBF" class="span12" style="width:80%" name="ZXSJBF" fieldname="ZXSJBF" type="text" disabled/>
					<span id="bsbf"></span>
				</td>
				<th width="8%" class="right-border bottom-border text-right disabledTh">使用金额（元）</th>
				<td width="25%" class="right-border bottom-border">
					<input id="ZXSJSY" class="span12" name="ZXSJSY" style="width:80%"  fieldname="ZXSJSY" type="text" disabled/>
					<span id="bssy"></span>
				</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">余额</th>
				<td width="25%" class="right-border bottom-border">
					<input id="YE" class="span12" style="width:95%" name="YE" fieldname="YE" type="text" disabled/>
				</td>
				<!-- <th width="8%" class="right-border bottom-border text-right">使用金额</th>
				<td width="25%" class="right-border bottom-border">
					<input id="ZXSJSY" class="span12" name="BDMC" style="width:95%"  fieldname="BDMC" type="text" disabled/>
				</td> -->
		</tr>
      </table>
      </form>
      </div>
      
      
      <div style="height:5px;"></div>
      
      <h4 class="title">
      	<span class="pull-right">
      	</span>
      	 <form method="post" id="queryFormMx">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border">
							<input type="hidden" id="QJHSJID" name="JHSJID"  fieldname="t.JHSJID" operation="="/>
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
				</table>
	</form>
      </h4>
     
    <div class="B-small-from-table-autoConcise">
	 <h4 class="title">拨入明细
      	<span class="pull-right">
      	</span>
      	</h4>
      	<div style="height:5px;"></div>
	<div class="overFlowX">
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single"  pageNum="1000" noPage=true>
	                <thead>
	                	<tr>
	                		<th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
	                		<th fieldname="XMMC" colindex=2 tdalign="center" maxlength="40">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDMC" colindex=3 tdalign="center" maxlength="40" CustomFunction="doBDMC">&nbsp;标段名称&nbsp;</th>
	                		<th fieldname="ZFRQ" colindex=5 tdalign="center" maxlength="10" >&nbsp;拨付日期&nbsp;</th>
	                		<th fieldname="ZFJE" colindex=6 tdalign="right" maxlength="17">&nbsp;拨付金额&nbsp;</th>
	                	</tr>
	                </thead>
	              	<tbody></tbody>
	           </table>
	       </div>
	       </div>
	       <div style="height:5px;"></div>
	       <div style="height:5px;"></div>
	       
	       <div class="B-small-from-table-autoConcise">
	  <h4 class="title">使用明细
      	<span class="pull-right">
      	</span></h4>
      	<div style="height:5px;"></div>
	<div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="DT2" width="100%" type="single" pageNum="1000" noPage=true>
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
				<th fieldname="XMMC" colindex=2 tdalign="center" maxlength="36">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDMC" colindex=3 tdalign="center" maxlength="40" CustomFunction="doBDMC">&nbsp;标段名称&nbsp;</th>
				<th fieldname="QY" colindex=4 tdalign="center" maxlength="30" >&nbsp;区域&nbsp;</th>
				<th fieldname="ZFRQ" colindex=5 tdalign="center" >&nbsp;使用日期&nbsp;</th>
				<th fieldname="ZFJE" colindex=6 tdalign="right">&nbsp;使用金额&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	</div>
   </div>
  </div>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter" >
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
<script>
</script>
</html>