<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<title>新增——修改——详细信息</title>
<%
	String id=request.getParameter("id");
%>
<script type="text/javascript" charset="utf-8">

var controllername= "${pageContext.request.contextPath }/xmcbk/xmcbkwhController.do";
var xmbh,id,json_xg,pch,zt;


//初始化加载
$(document).ready(function(){
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
 	doquery_xmxx(controllername+"?query_cbkxx_ty",data,DT1);
	var json=$("#queryResult").val();
	var tempJson=convertJson.string2json1(json);
	var resultobj=tempJson.response.data[0];
	json_xg=JSON.stringify(resultobj)
	$("#demoForm").setFormValues(resultobj);
	$("#QY").val(resultobj.QY_SV);
	$("#QY").attr("code",resultobj.QY);
 });


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
      		<h4 class="title c-table">项目信息</h4>
     		<form method="post" id="demoForm">
      			<table class="B-table" width="100%" id="DT1">
      				<input type="hidden" id="YWLX" fieldname="YWLX" name="YWLX"/>
      				<input type="hidden" id="XMBH" fieldname="XMBH" name="XMBH"/>
      				<input type="hidden" id="ISXD" fieldname="ISXD" name="ISXD" value="0"/>
      				<input type="hidden" id="GC_TCJH_XMCBK_ID" fieldname="GC_TCJH_XMCBK_ID" name="GC_TCJH_XMCBK_ID"/>
					<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">项目编号</th>
						<td width="17%" class="right-border bottom-border">
							<input id="XMBH" class="span12" type="text"  check-type="required maxlength" name="XMBH"  maxlength="36" fieldname="XMBH" readonly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
						<td width="43%" colspan="3" class="right-border bottom-border">
							<input id="XMMC" class="span12" type="text" placeholder="必填" check-type="required" name="XMMC" check-type="maxlength" maxlength="500" disabled fieldname="XMMC"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">年度</th>
						<td width="17%" class="right-border bottom-border">
							<input class="span12" type="text" id="ND" placeholder="必填" check-type="required" keep="true"  disabled fieldname="ND" name="ND"/>
						</td>	
					</tr>
					<tr>							
						<th width="8%" class="right-border bottom-border text-right disabledTh">项目类型</th>
						<td width="17%" class="right-border bottom-border">
							<input type="text" class="span12" id="XMLX" disabled fieldname="XMLX" name="XMLX"></select>
						</td>
				        <th width="8%" class="right-border bottom-border text-right disabledTh">项目来源</th>
	        			<td width="43%" colspan="3" class="right-border bottom-border">
				        	<input class="span12" id="QY" type="text" disabled fieldname="QY" name="QY" maxlength="100" />
	       				 </td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">是否BT</th>
						<td width="17%" class="right-border bottom-border">
							<input type="text" class="span12" id="ISBT" disabled fieldname="ISBT" name="ISBT"></select>
						</td>
					</tr>
					<tr>							
						<th width="8%" class="right-border bottom-border text-right disabledTh">项目属性</th>
						<td width="17%" class="right-border bottom-border">
							<input type="text" class="span12" name="XMSX" id="XMSX" disabled fieldname="XMSX"></select>
						</td>
						<th class="right-border bottom-border text-right disabledTh">项目地址</th>
						<td colspan="3" class="right-border bottom-border">
							<input class="span12" type="text" placeholder="" id="XMDZ" check-type="maxlength" maxlength="500" disabled fieldname="XMDZ" name="XMDZ"/>
						</td>
						<th class="right-border bottom-border text-right">项目属性</th>
						<td class="right-border bottom-border">
							<input type="text" class="span12" id="XMSX" fieldname="XMSX" name="XMSX" kind="dic" src="XMSX" disabled>
						</td>
					</tr>
 					<tr>
			<th class="right-border bottom-border text-left disabledTh"  colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;项目总体投资</th>
		</tr>
		<tr>
		  <th width="8%" class="right-border bottom-border text-right disabledTh">工程</th>
		  <td class="right-border bottom-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="ZTGC" type="number" onblur="countHj_ZT()" fieldname="ZTGC"  name = "ZTGC" min="0" disabled><b>（元）</b>
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">征拆</th>
          <td class="right-border bottom-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="ZTZC" type="number" onblur="countHj_ZT()" fieldname="ZTZC"  name = "ZTZC" min="0" disabled><b>（元）</b>
         </td>
         <th width="8%" class="right-border bottom-border text-right disabledTh">其他</th>
          <td class="bottom-border right-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="ZTQT" type="number" onblur="countHj_ZT()" fieldname="ZTQT"  name = "ZTQT" min="0" disabled><b>（元）</b>
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">总投资额</th>
          <td class="right-border bottom-border" width="17%">
          <input class="span12" style="width:70%;text-align:right" id="ZTZTZE" type="number" fieldname="ZTZTZE" name = "ZTZTZE" readonly><b>（元）</b>
         </td>
		</tr>
		<tr>
			<th class="right-border bottom-border text-left disabledTh"  colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;项目年度投资</th>
		</tr>
        <tr>
         <th width="8%" class="right-border bottom-border text-right disabledTh">工程</th>
          <td class="right-border bottom-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="GC" type="number" onblur="countHj()" fieldname="GC"  name = "GC" min="0" disabled><b>（元）</b>
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">征拆</th>
          <td class="right-border bottom-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="ZC" type="number" onblur="countHj()" fieldname="ZC"  name = "ZC" min="0" disabled><b>（元）</b>
         </td>
         <th width="8%" class="right-border bottom-border text-right disabledTh">其他</th>
          <td class="bottom-border right-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="QT" type="number" onblur="countHj()" fieldname="QT"  name = "QT" min="0" disabled><b>（元）</b>
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">总投资额</th>
	        <td class="right-border bottom-border" width="17%">
	          <input class="span12" style="width:70%;text-align:right" id="JHZTZE" type="number" fieldname="JHZTZE" name = "JHZTZE" readonly><b>（元）</b>
	        </td>
		</tr>			
					<tr>
						<th class="right-border bottom-border text-right disabledTh">建设任务</th>
						<td colspan="7" class="bottom-border">
							<textarea class="span12" rows="2" id="JSRW" check-type="maxlength" maxlength="500" disabled fieldname="JSRW" name="JSRW"></textarea>
						</td>
					</tr>
					<tr>
						<th class="right-border bottom-border text-right disabledTh">建设内容</th>
						<td colspan="7" class="bottom-border">
							<textarea class="span12" rows="2" id="JSNR" check-type="maxlength" maxlength="500" disabled fieldname="JSNR" name="JSNR"></textarea>
						</td>
					</tr>
					<tr>
						<th class="right-border bottom-border text-right disabledTh">建设意义</th>
						<td colspan="7" class="bottom-border">
							<textarea class="span12" rows="2" id="JSYY" check-type="maxlength" maxlength="500" disabled fieldname="JSYY" name="JSYY"></textarea>
						</td>
					</tr>
					<tr>
						<th class="right-border bottom-border text-right disabledTh">备注</th>
						<td colspan="7" class="bottom-border">
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
			<INPUT type="hidden" class="span12" kind="text" id="GC_TCJH_XMCBK_ID" fieldname="GC_TCJH_XMCBK_ID" value="<%=id%>" operation="="/> 
		</TD>
	</TR>	
</form>
</body>
</html>