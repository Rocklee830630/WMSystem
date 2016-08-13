<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<title>新增——修改——详细信息</title>
<%
	String xmbh=request.getParameter("xmbh");
	String id=request.getParameter("id");
	String zt=request.getParameter("zt");
	String pch=request.getParameter("pch");
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">

var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglTqkbmmxController.do";
var xmbh,id,json_xg,pch,zt;


//初始化加载
$(document).ready(function(){
	xmbh='<%=xmbh%>';
	id='<%=id%>';
	pch='<%=pch%>';
	zt='<%=zt%>';
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	doquery_xmxx(controllername+"?query&xmbh="+xmbh,data,DT1);
	var json=$("#queryResult").val();
	var tempJson=eval("("+json+")");
	var resultobj=tempJson.response.data[0];
	json_xg=JSON.stringify(resultobj)
	$("#demoForm").setFormValues(resultobj);
});


//弹出修改窗口
function OpenMiddleWindow_alter(){
	json_xg=encodeURI(json_xg); 
	$(window).manhuaDialog({"title":"项目储备库>修改","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/tqkbmmx-add.jsp?xx="+json_xg+"&&id="+id+"&zt="+zt+"&pch="+pch,"modal":"1"});
}


//项目信息查询
doquery_xmxx= function(actionName, data1,tablistID){
    var success  = true;
	var data = {
		msg : data1
	};
	$.ajax({
		url : actionName,
		data : data,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(result) {
			//查询清空结果
			var returnMsg = result.msg;
			$("#queryResult").val(result.msg);
			success = true;
		}
	});
    return success;	
};
</script>    
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	</p>	
	<div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">
      		<h4 class="title">
      			项目信息
				<span class="pull-right">
					<button id="example_save" onclick="OpenMiddleWindow_alter()" class="btn" type="button">修改</button>
				</span>    			
      		</h4>
     		<form method="post" id="demoForm">
      			<table class="B-table" width="100%" id="DT1">
      				<input type="hidden" id="YWLX" fieldname="YWLX" name="YWLX"/>
      				<input type="hidden" id="XMBH" fieldname="XMBH" name="XMBH"/>
      				<input type="hidden" id="ISXD" fieldname="ISXD" name="ISXD" value="0"/>
      				<input type="hidden" id="GC_TCJH_XMCBK_ID" fieldname="GC_TCJH_XMCBK_ID" name="GC_TCJH_XMCBK_ID"/>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">项目名称</th>
						<td width="58%" colspan="5" class="right-border bottom-border">
							<input id="XMMC" class="span12" type="text" placeholder="必填" check-type="required" name="XMMC" check-type="maxlength" maxlength="100" disabled fieldname="XMMC"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">年度</th>
						<td width="17%" class="right-border bottom-border">
							<input class="span12" type="text" id="ND" placeholder="必填" check-type="required" keep="true"  disabled fieldname="ND" name="ND"/>
						</td>	
					</tr>
					<tr>							
				        <th width="8%" class="right-border bottom-border text-right">项目来源</th>
	        			<td width="17%" class="right-border bottom-border">
				        	<input class="span12" style="width:70%" id="QY" type="text" disabled fieldname="QY" name="QY" maxlength="100" />
				        	&nbsp;
				        	<a href="#" onclick="addQy();"data-toggle="modal">选择</a>
				        	&nbsp;
				        	<a href="#" onclick="deleteQy();"data-toggle="modal">清空</a>
	       				 </td>
						<th width="8%" class="right-border bottom-border text-right">项目属性</th>
						<td width="17%" class="right-border bottom-border">
							<select class="span12" name="XMSX" id="XMSX" disabled fieldname= "XMSX" kind="dic" src="XMSX"></select>
						</td>
						<th width="8%" class="right-border bottom-border text-right">项目类型</th>
						<td width="17%" class="right-border bottom-border">
							<select class="span12" kind="dic" src="XMLX" id="XMLX" disabled fieldname="XMLX" name="XMLX"></select>
						</td>
						<th width="8%" class="right-border bottom-border text-right">是否BT</th>
						<td width="17%" class="right-border bottom-border">
							<select class="span12" id="ISBT" kind="dic" src="SF" disabled fieldname="ISBT" name="ISBT"></select>
						</td>
					</tr>
					<tr>							
						<th width="8%" class="right-border bottom-border text-right">项目地址</th>
						<td width="92%" colspan="7" class="right-border bottom-border">
							<input class="span12" type="text" placeholder="" id="XMDZ" check-type="maxlength" maxlength="500" disabled fieldname="XMDZ" name="XMDZ"/>
						</td>						
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">计划总投资额</th>
						<td width="25%" class="right-border bottom-border">
							<input class="span12" type="number" placeholder="" style="width:70%;text-align:right;" keep="true" value=0 id="JHZTZE" disabled fieldname="JHZTZE" name="JHZTZE" />&nbsp;<b>(万元)</b>
						</td>
					<td colspan="7" class="right-border bottom-border"></td>
					</tr>
 					<tr>
						<th width="8%" class="right-border bottom-border text-right">工程计划投资</th>
						<td width="17%" class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj()" keep="true" placeholder="" value=0 style="width:70%;text-align:right;" id="GC" disabled fieldname="GC" name="GC"/><b>(万元)</b>
						</td>							
						<th width="8%" class="right-border bottom-border text-right">征拆计划投资</th>
						<td width="17%" class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj()" keep="true" placeholder="" value=0 style="width:70%;text-align:right;" id="ZC" disabled fieldname="ZC" name="ZC"/><b>(万元)</b>
						</td>
						<th width="8%" class="right-border bottom-border text-right">其他计划投资</th>
						<td width="17%" class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj()" keep="true" placeholder="" value=0 style="width:70%;text-align:right;" id="QT" disabled fieldname="QT" name="QT"/><b>(万元)</b>
						</td>
						<td colspan="2" class="right-border bottom-border"></td>							
					</tr>			
					<tr>
						<th width="8%" class="right-border bottom-border text-right">建设任务</th>
						<td width="92%" colspan="7" class="bottom-border">
							<textarea class="span12" rows="2" id="JSRW" check-type="maxlength" maxlength="500" disabled fieldname="JSRW" name="JSRW"></textarea>
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">建设内容</th>
						<td width="92%" colspan="7" class="bottom-border">
							<textarea class="span12" rows="2" id="JSNR" check-type="maxlength" maxlength="500" disabled fieldname="JSNR" name="JSNR"></textarea>
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">建设意义</th>
						<td width="92%" colspan="7" class="bottom-border">
							<textarea class="span12" rows="2" id="JSYY" check-type="maxlength" maxlength="500" disabled fieldname="JSYY" name="JSYY"></textarea>
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">备注</th>
						<td width="92%" colspan="7" class="bottom-border">
							<textarea class="span12" rows="2" id="BZ" check-type="maxlength" maxlength="4000" disabled fieldname="BZ" name="BZ"></textarea>
						</td>
					</tr>
     		</table>
      	</form>
    </div>
  </div>
</div>
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