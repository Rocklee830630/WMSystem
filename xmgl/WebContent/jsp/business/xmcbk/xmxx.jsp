<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<%
	String id=request.getParameter("id");
	String pch=request.getParameter("pch");
	String pcid=request.getParameter("pcid");
	String zt=request.getParameter("zt");
	String xdlx=request.getParameter("xdlx");
%>
<script type="text/javascript" charset="utf-8">

var controllername= "${pageContext.request.contextPath }/xmcbk/xmcbkwhController.do";
var xmbh,id,json_xg,xdrq,resultobj;


//初始化加载
$(document).ready(function(){
	id='<%=id%>';//修改传参
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	doquery_xmxx(controllername+"?query_cbkxx&id="+id,data,DT1);
	var json=$("#queryResult").val();
	var tempJson=convertJson.string2json1(json);
	resultobj=tempJson.response.data[0];
	json_xg=JSON.stringify(resultobj)
	$("#demoForm").setFormValues(resultobj);
	init()
	if('<%=zt%>'==1)
	{
		$("#fontid")[0].innerHTML=xdrq;
	}	
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


//查询出下达日期
function init(){
	var success=true;
	var data1=combineQuery.getQueryCombineData(queryForm2,frmPost,null);
	var actionName=controllername+"?query_xdrq&pcid="+'<%=pcid%>';
	var data={
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
		xdrq=result.msg;	
		success=true;
		}
	});
    return success;
}
</script>    
</head>
<body>
<app:dialogs/>
<div class="container-fluid">	
	<div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">
      		<h4 class="title">项目信息</h4>
     		<form method="post" id="demoForm">
      			<table class="B-table" width="100%" id="DT1">
      				<input type="hidden" id="YWLX" fieldname="YWLX" name="YWLX"/>
      				<input type="hidden" id="XMBH" fieldname="XMBH" name="XMBH"/>
      				<input type="hidden" id="ISXD" fieldname="ISXD" name="ISXD" value="0"/>
      				<input type="hidden" id="GC_TCJH_XMCBK_ID" fieldname="GC_TCJH_XMCBK_ID" name="GC_TCJH_XMCBK_ID"/>
					<tr>
						<%if("0".equals(zt)) {%>
						本项目尚未下达
						<%} %>
						<%if("1".equals(zt))
						{ 
							if("1".equals(xdlx))
							{	
							%>
							本项目于第<font style=" margin-left:5px; margin-right:5px;;font-size:24px;color:red;"><%=Integer.parseInt(pch)%></font>批次下达，下达日期是:<font  id="fontid" style=" margin-left:5px; margin-right:5px;;font-size:20px;color:red;"></font>
							<%}else{%>
							本项目于第<font style=" margin-left:5px; margin-right:5px;;font-size:20px;color:red;"><%=pch%></font>批次下达，下达日期是:<font id="fontid" style=" margin-left:5px; margin-right:5px;;font-size:20px;color:red;"></font>							
						<%} }%>
						<th width="8%" class="right-border bottom-border text-right disabledTh">项目编号</th>
						<td width="17%" class="right-border bottom-border">
							<input id="XMBH" class="span12" type="text"  check-type="required maxlength" name="XMBH"  maxlength="36" fieldname="XMBH" readonly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
						<td width="42%" colspan="3" class="right-border bottom-border">
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
							<input type="text" class="span12" id="XMLX" disabled fieldname="XMLX" name="XMLX"/>
						</td>
				        <th width="8%" class="right-border bottom-border text-right disabledTh">所属城区</th>
	        			<td width="42%" colspan="3" class="right-border bottom-border">
				        	<input class="span12" id="QY" type="text" disabled fieldname="QY" name="QY" maxlength="100" />
	       				 </td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">是否BT</th>
						<td width="17%" class="right-border bottom-border">
							<input type="text" class="span12" id="ISBT" disabled fieldname="ISBT" name="ISBT">
						</td>
					</tr>
					<tr>							
						<th width="8%" class="right-border bottom-border text-right disabledTh">项目属性</th>
						<td width="17%" class="right-border bottom-border">
							<input type="text" class="span12" name="XMSX" id="XMSX" disabled fieldname= "XMSX"></select>
						</td>
						<th class="right-border bottom-border text-right disabledTh">项目地址</th>
						<td colspan="5" class="right-border bottom-border">
							<input class="span12" type="text" placeholder="" id="XMDZ" check-type="maxlength" maxlength="500" disabled fieldname="XMDZ" name="XMDZ"/>
						</td>						
					</tr>
					<tr>							
						<th class="right-border bottom-border text-right disabledTh">责任部门</th>
						<td  class="right-border bottom-border">
						<input type="text" class="span12" id="ZRBM" disabled fieldname="ZRBM" name="ZRBM">
						</td>
						<th class="right-border bottom-border text-right disabledTh">项目法人</th>
						<td  class="right-border bottom-border">
						<input type="text" class="span12" id="XMFR" disabled fieldname="XMFR" name="XMFR">
						</td>
						<th class="right-border bottom-border text-right disabledTh">是否纳入统计</th>
						<td  class="right-border bottom-border">
						<input type="text" class="span12" id="ISNRTJ" disabled fieldname="ISNRTJ" name="ISNRTJ">
						</td>
						<th class="right-border bottom-border text-right">项目属性</th>
						<td class="right-border bottom-border">
							<input type="text" class="span12" id="XMSX" fieldname="XMSX" name="XMSX" kind="dic" src="XMSX" disabled>
						</td>
					</tr>
						<tr>
						<th width="8%"  class="right-border bottom-border text-right disabledTh">项目编码</th>
						<td width="92%" colspan=7 class="right-border bottom-border">
							<input type="text" class="span9" id="XMBM" placeholder="必填" disabled check-type="required maxlength" maxlength="100" fieldname="XMBM" name="XMBM">
							<span style="font-size:10px;">(如果立项编码为空，请以“XXXXX”代替)</span>
						</td>
					</tr>
					<tr>
						<th class="right-border bottom-border text-left disabledTh"  colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;项目总体投资</th>
					</tr>
					<tr>
						<th class="right-border bottom-border text-right disabledTh" width="8%">工程</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj()" keep="true" placeholder="" style="width:75%;text-align:right;" id="ZTGC" disabled fieldname="ZTGC" name="ZTGC"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>							
						<th class="right-border bottom-border text-right disabledTh" width="8%">征拆</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj()" keep="true" placeholder="" style="width:75%;text-align:right;" id="ZTZC" disabled fieldname="ZTZC" name="ZTZC"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>
						<th class="right-border bottom-border text-right disabledTh" width="8%">其他</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj()" keep="true" placeholder="" style="width:75%;text-align:right;" id="ZTQT" disabled fieldname="ZTQT" name="ZTQT"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>
						<th class="right-border bottom-border text-right disabledTh" width="8%">总投资额</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" placeholder="" style="width:75%;text-align:right;" keep="true" id="ZTZTZE" disabled fieldname="ZTZTZE" name="ZTZTZE" />&nbsp;&nbsp;<b>(万元)</b>
						</td>							
					</tr>
					<tr>
						<th class="right-border bottom-border text-left disabledTh"  colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;项目年度投资</th>
					</tr>
 					<tr>
						<th class="right-border bottom-border text-right disabledTh" width="8%">工程</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj()" keep="true" placeholder="" style="width:75%;text-align:right;" id="GC" disabled fieldname="GC" name="GC"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>							
						<th class="right-border bottom-border text-right disabledTh" width="8%">征拆</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj()" keep="true" placeholder="" style="width:75%;text-align:right;" id="ZC" disabled fieldname="ZC" name="ZC"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>
						<th class="right-border bottom-border text-right disabledTh" width="8%">其他</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" onblur="countHj()" keep="true" placeholder="" style="width:75%;text-align:right;" id="QT" disabled fieldname="QT" name="QT"/>&nbsp;&nbsp;<b>(万元)</b>
						</td>
						<th class="right-border bottom-border text-right disabledTh" width="8%">总投资额</th>
						<td class="right-border bottom-border">
							<input class="span12" type="number" placeholder="" style="width:75%;text-align:right;" keep="true" id="JHZTZE" disabled fieldname="JHZTZE" name="JHZTZE" />&nbsp;&nbsp;<b>(万元)</b>
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
		</TD>
	</TR>	
</form>
<form method="post" id="queryForm2">
	<!--可以再此处加入hidden域作为过滤条件 -->
	<TR style="display: none;">
		<TD class="right-border bottom-border"></TD>
		<TD class="right-border bottom-border">
			<INPUT type="hidden" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
			<%-- <INPUT type="hidden" class="span12" kind="text" id="PCID" fieldname="PCID" value="<%=pcid%>" operation="<="/> --%>
		</TD>
	</TR>	
</form>
</body>
</html>