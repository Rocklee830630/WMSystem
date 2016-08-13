<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%
	String xmid= request.getParameter("xmid");
	String xmbs= request.getParameter("xmbs");
	String jhsjid = request.getParameter("jhsjid");
%>
<app:base/>
<app:dialogs></app:dialogs>
<title>标段划分管理</title>
<script type="text/javascript" charset="utf-8">
 
var controllername= "${pageContext.request.contextPath }/bdhf/bdwhController.do";
var xmbs = '<%=xmbs%>';
var id = '<%=xmid%>';
var jhsjid_xm = '<%=jhsjid%>';
//保存
$(function() {	
	init();
	//按钮绑定事件（保存）
	$("#btnSave").click(function(){
		if(!$("#demoForm").validationButton()){
			requireFormMsg();
			return ;
		}
		xConfirm("信息提示","<保存>操作会将数据同步到【统筹计划】，<font color='red'>是否继续？</font>");
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){  
			var data=Form2Json.formToJSON(demoForm);
			var data1=defaultJson.packSaveJson(data);
			var obj = convertJson.string2json1(data);
			if(obj.GC_XMBD_ID == "" || obj.GC_XMBD_ID == null){
				defaultJson.doInsertJson(controllername + "?update_bdbd&jhsjid="+jhsjid_xm, data1,DT1,"update_hd");
			}else{
				defaultJson.doUpdateJson(controllername + "?update_bdbd", data1,DT1,"update_hd");
			}
			
		});  
 	});
	//按钮绑定事件（新增）
	$("#btnInsert").click(function(){
		$("#demoForm").clearFormResult();
		if(!$("#DT1").getSelectedRowIndex()!=-1){	
			$("#DT1").cancleSelected();
		}
 	});
});
//页面初始化
function init(){
	if(xmbs == "0"){
		$("#DT1").show();
		queryList();
	}else{
		$("#DT1").hide();
		queryBdxxByBdid(id);
	}
}
//查询列表(标段)
function queryList(){
	$("#xmid").val(id);	
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?querydemo_bd&xmid="+id,data,DT1,"callBackFun",true);
}
//查询回调函数
function callBackFun(){
	$("#DT1>tbody").find("tr").eq(0).click();
	$("#DT1").cancleSelected();
	$("#demoForm").clearFormResult();
}
$(function() {	
	var btn=$("#example_ok");
	btn.click(function() {
		$("#demoForm").clearFormResult(); 
		bdid=null;
	});
    btn=$("#baidubd");
	btn.click(function() {
		if(xmbs == "0"){
			if($("#DT1").getSelectedRowIndex()==-1)
			 {
				xAlert("提示信息",'请选择一条记录','3');
				return
			 }
		}
		var xmbh = $("#DT1").getSelectedRowJsonObj().XMID;
		var bdbh = $("#DT1").getSelectedRowJsonObj().BDID;
		
		$(window).manhuaDialog({"title":"标段地址表单","type":"text","content":"${pageContext.request.contextPath}/jsp/xmgl/xmcbk/baidu_bd.jsp?xmbh="+xmbh+"&bdbh="+bdbh,"modal":"1"});
	});
});
//查询标段信息
function queryBdxxByBdid(id){
	var obj = convertJson.string2json1(setFormData(id)).response.data[0];
	$("#demoForm").setFormValues(obj);
}

//根据ID查询标段信息
function setFormData(id){
	var obj = "";
	var actionName=controllername+"?queryBdxxByBdid&id="+id;
	$.ajax({
		url : actionName,
		data : null,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(result) {
			obj = result.msg;
		},
	    error : function(result) {
		}
	});
    return obj;
}

//点击获取行对象
function tr_click(obj,tabListid){
	$("#demoForm").setFormValues(obj);
}

//更新回调函数
function update_hd(){
	$(window).manhuaDialog.getParentObj().gsPar_feedback();	
}


</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<form method="post" id="queryForm">
			<table class="B-table">
				<!--可以再此处加入hidden域作为过滤条件 -->
				<TR style="display: none;">
					<TD class="right-border bottom-border"></TD>
					<TD class="right-border bottom-border">
						<INPUT type="text" class="span12" kind="text" id="xmid" fieldname="xmid" keep="true" operation="="/>
					</TD>
				</TR>
			</table>
		</form>
		<div class="B-small-from-table-autoConcise">	
			<div class="overFlowX"> 
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" pageNum="5" type="single">
	                <thead>
	                    <tr>
	                     	<th id="_XH" name="XH" tdalign="center">&nbsp;#&nbsp;</th>	
	                     	<th fieldname="BDBM">&nbsp;编码&nbsp;</th>	
							<th fieldname="BDBH" tdalign="center">&nbsp;标段编号&nbsp;</th>
							<th fieldname="BDMC" maxlength="15">&nbsp;标段名称&nbsp;</th>
							<th fieldname="XMGLGS" tdalign="center">&nbsp;项目管理公司&nbsp;</th>
							<th fieldname="QDZH" maxlength="15">&nbsp;起点(桩号)&nbsp;</th>
							<th fieldname="ZDZH" maxlength="15">&nbsp;终点(桩号)&nbsp;</th>
						    <th fieldname="BDDD" maxlength="15">&nbsp;起始点&nbsp;</th>
						    <th fieldname="JSGM">&nbsp;建设内容及规模&nbsp;</th>		
							<th fieldname="CD" tdalign="right">&nbsp;长度&nbsp;</th>
							<th fieldname="KD" tdalign="right">&nbsp;宽度&nbsp;</th>
							<th fieldname="GJ" tdalign="right">&nbsp;管径&nbsp;</th>
							<th fieldname="MJ" tdalign="right">&nbsp;面积&nbsp;</th>
							<th fieldname="GCZTFY" tdalign="right">&nbsp;工程主体费用（万元）&nbsp;</th>
							<th fieldname="BZ" maxlength="15">&nbsp;备注&nbsp;</th>
						</tr>
	                </thead>
	                <tbody></tbody>
	           </table>
	        </div>     
			<div style="height:5px;"> </div>
			<h4 class="title">
	      		标段信息
	      		<span class="pull-right">
	          		<button id="btnInsert" name="new" class="btn" type="button">新增</button>
	          		<button id="btnSave" name="save" class="btn" type="button">保存</button>
	          		<button id="baidubd" name="save" class="btn" type="button">标段地址标点</button>
	          	</span>
			</h4>
    			<form method="post" id="demoForm">
     				<table class="B-table" width="100%">
     				<TR  style="display:none;">
     				    <td>
	     					<input type="text" id="GC_XMBD_ID" name="GC_XMBD_ID" fieldname="GC_XMBD_ID"/>
	     					<input type="text" keep="true" id="JHSJID" name="JHSJID" fieldname="JHSJID"/>
	     					<input type="text" keep="true" id="ND" name="ND" fieldname="ND"/>
	     					<input type="text" keep="true" id="XMID" name="XMID" fieldname="XMID"/>
	     					<input type="text" keep="true" id="JHID" name="JHID" fieldname="JHID"/>
					    </td>
					</TR>
					<tr>
			        	<th width="8%" class="right-border bottom-border text-right">项目编号</th>
			 	 		<td class="bottom-border right-border" colspan=7>
			 	 			<input class="span3" type="text" id="PRE_BDBM" fieldname="PRE_BDBM" keep="true" disabled>
			   				<input class="span9" id="BD_XMBM"  type="text" placeholder="必填" check-type="required" fieldname="BD_XMBM" name = "BD_XMBM" maxlength="100">
			 	 		</td>
			 	 	</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">标段编号</th>
						<td class="right-border bottom-border">
							<input class="span12" type="text" placeholder="必填" data-toggle="modal" check-type="required maxlength" maxlength="36" id="BDBH" name="BDBH" fieldname="BDBH"/>	    
						</td>
						<th width="8%" class="right-border bottom-border text-right">标段名称</th>
						<td colspan="3" class="right-border bottom-border">
							<input class="span12" type="text" id="BDMC" placeholder="必填" name="BDMC"  check-type="required maxlength" maxlength="200" fieldname="BDMC"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">项目管理公司</th>
						<td class="bottom-border right-border">
							<select class="span12 4characters" id="XMGLGS" kind="dic" src="T#VIEW_YW_XMGLGS:ROW_ID:BMJC" fieldname="XMGLGS" name="XMGLGS">
				     		</select>
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">起点（桩号）</th>
						<td width="18%" class="right-border bottom-border">
							<input id="QDZH" class="span12" type="text" name="QDZH" check-type="maxlength" maxlength="100"fieldname="QDZH"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">终点（桩号）</th>
						<td width="18%" class="right-border bottom-border">
							<input class="span12" name ="ZDZH" id="ZDZH" check-type="maxlength" maxlength="100" fieldname="ZDZH" type="text"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">起始点（桩号）</th>
						<td width="18%" colspan="3" class="right-border bottom-border">
							<input class="span12" type="text" name="BDDD" onfocus="qsd()" id="BDDD" check-type="maxlength" maxlength="400" fieldname="BDDD"/>
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">建设规模</th>
						<td width="43%" colspan="3" class="right-border bottom-border">
							<input class="span12" type="text" id="JSGM" name="JSGM" check-type="maxlength" maxlength="500" fieldname="JSGM"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">长度</th>
						<td width="18%" class="right-border bottom-border">
							<input  class="span12" type="number" name="CD" id="CD" min="0" style="text-align:right;" fieldname="CD"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">宽度</th>
						<td width="18%" class="right-border bottom-border">
							<input class="span12" type="number" name="KD" id="KD" min="0" style="text-align:right;" fieldname="KD"/>
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">管径</th>
						<td width="18%" class="right-border bottom-border">
							<input class="span12" type="number" fieldname="GJ" min="0" name="GJ" style="text-align:right;" id="GJ"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">面积</th>
						<td width="18%" class="right-border bottom-border">
							<input class="span12" type="number" fieldname="MJ" name="MJ" min="0" style="text-align:right;" id="MJ"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">工程主体费用</th>
						<td width="18%" class="right-border bottom-border">
							<input class="span12" type="number" fieldname="GCZTFY" min="0" style="text-align:right; width:70%" name="GCZTFY" id="GCZTFY"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>
						<td colspan="2"></td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">备注</th>
						<td width="92%" colspan="7" class="bottom-border">
							<textarea class="span12" rows="5" id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ"></textarea>
						</td>
					</tr>
   	 			</table>
     		</form>
	       
		</div>
	</div>		
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" id="px" fieldname="BDBH,LRSJ"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<input type="hidden" id="queryResult"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
	</FORM>
	
</div>
</body>
</html>