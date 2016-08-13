<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>Insert title here</title>
<%
	String sysdate = Pub.getDate("yyyy-MM-dd");
	String conditionJson = request.getParameter("conditionJson") == null ? "1" : request.getParameter("conditionJson");
%>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script src="${pageContext.request.contextPath }/js/common/FixTable.js"></script>

<script type="text/javascript" charset="UTF-8">
var sysdate = '<%=sysdate %>';
var conditionJsonObj = <%=conditionJson %>;
	var controllername = "${pageContext.request.contextPath }/xmzhxxController.do";
	
	//初始化查询
  	$(document).ready(function() {
  		generateNd($("#ND"));
		setDefaultNd();
  		autoquery();
  		if(conditionJsonObj != "1") {
  	  		$("#queryForm").setFormValues(conditionJsonObj);
  		}
  	});
	
	function finishOk() {
		
		var queryJson = combineQuery.getQueryCombineData(queryForm, frmPost, DT1);
		var formJson = Form2Json.formToJSON(queryForm);
	//	formJson=encodeURI(formJson);
		var lastJson = queryJson + "|" + formJson;
	//	alert(lastJson);
		$(window).manhuaDialog.setData(lastJson);
		$(window).manhuaDialog.sendData();
		$(window).manhuaDialog.close();
	}
	
	function clearForm() {
		$("#queryForm").clearFormResult();
    	setDefaultOption($("#ND"),new Date().getFullYear());
	}
	
	//年份查询
	function generateNd(ndObj){
		ndObj.attr("src","T#GC_JH_SJ:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
		ndObj.attr("kind","dic");
		ndObj.html('');
		reloadSelectTableDic(ndObj);
		ndObj.val(new Date().getFullYear());
	}
	
	//年份查询
	function autoquery(){
  		//自动完成项目名称模糊查询
  		showAutoComplete("XMMC",controllername+"?xmmcAutoQuery","getXmmcQueryCondition"); 
  		//自动完成项目名称模糊查询
  		showAutoComplete("BDMC",controllername+"?bdmcAutoQuery","getBdmcQueryCondition");
	}
	
	//生成项目名称查询条件
	  function getXmmcQueryCondition(){
	  	var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":""}]}';
	  	var jsonData = convertJson.string2json1(initData); 
	   	//项目名称
	  	if("" != $("#XMMC").val()){
	  		var defineCondition = {"value": "%"+$("#XMMC").val()+"%","fieldname":"XMMC","operation":"like","logic":"and"};
	  		jsonData.querycondition.conditions.push(defineCondition);
	  	}
	      //年度
	  	if("" != $("#ND").val()){
	  		var defineCondition = {"value": $("#ND").val(),"fieldname":"ND","operation":"=","logic":"and"};
	  		jsonData.querycondition.conditions.push(defineCondition);
	  	}
	   	return JSON.stringify(jsonData);
	  }
	  
	  //生成标段名称查询条件
	  function getBdmcQueryCondition(){
	  	var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":""}]}';
	  	var jsonData = convertJson.string2json1(initData); 
	   	//项目名称
	  	if("" != $("#BDMC").val()){
	  		var defineCondition = {"value": "%"+$("#BDMC").val()+"%","fieldname":"BDMC","operation":"like","logic":"and"};
	  		jsonData.querycondition.conditions.push(defineCondition);
	  	}
	      //年度
	  	if("" != $("#ND").val()){
	  		var defineCondition = {"value": $("#ND").val(),"fieldname":"ND","operation":"=","logic":"and"};
	  		jsonData.querycondition.conditions.push(defineCondition);
	  	}
	   	return JSON.stringify(jsonData);
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
					<button class="btn" type="button" onclick="finishOk()">确定</button>
	                <button class="btn" type="button" onclick="clearForm()">清空</button>
				</span>
      		</h4>
			<form method="post" id="queryForm" name="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>	
						<th width="2%" class="right-border bottom-border text-right">年度</th>
						<td width="7%" class="right-border bottom-border">
							 <select id="ND" class="span12 year" name="ND" defaultMemo="全部" fieldname="ND" operation="=" ></select> 
						</td>
						<th width="3%" class="right-border bottom-border text-right">项目名称</th>
						<td width="20%" class="right-border bottom-border">
							<input class="span12" type="text" placeholder="" name="XMMC" fieldname="XMMC" check-type="maxlength" maxlength="500" operation="like" id="XMMC" autocomplete="off">
						</td>	
						
						<th width="3%" class="right-border bottom-border text-right">标段名称</th>
						<td width="20%" class="right-border bottom-border">
							<input class="span12" type="text" placeholder="" name="BDMC" fieldname="BDMC" check-type="maxlength" maxlength="500" operation="like" id="BDMC" autocomplete="off">
						</td> 
					</tr>
					<tr><!-- 	
						<th width="3%" class="right-border bottom-border text-right">建设位置</th>
						<td width="20%" class="right-border bottom-border">
							<input class="span12" type="text" placeholder="" name="JSDZ" fieldname="JSDZ" check-type="maxlength" maxlength="500" operation="like" id="JSDZ">
						</td>	 -->	 
						<th width="3%" class="right-border bottom-border text-right">项管公司</th>
						<td width="20%" class="right-border bottom-border">
							<!-- <input class="span12" type="text" placeholder="" name="XMGLGS" fieldname="XMGLGS" check-type="maxlength" maxlength="500" operation="like" id="XMGLGS"> -->
							<select class="span12 4characters" id="XMGLGS" kind="dic" src="T#VIEW_YW_XMGLGS:ROW_ID:BMJC" check-type="required" fieldname="XMGLGS" name="XMGLGS" operation="=" logic="and"></select>
						</td> 	 
						<th width="3%" class="right-border bottom-border text-right">项目性质 </th>
						<td width="20%" class="right-border bottom-border">
							<!-- <input class="span12" type="text" placeholder="" name="XMXZ" fieldname="XMXZ" check-type="maxlength" maxlength="500" operation="like" id="XMXZ"> -->
							<select class="span12 4characters" id="XMXZ" kind="dic" src="XMXZ" check-type="required" fieldname="XMXZ" name="XMXZ" operation="=" logic="and" ></select>
						</td> 	 
						
						<th width="3%" class="right-border bottom-border text-right">项目编号</th>
						<td width="20%" class="right-border bottom-border">
							<input class="span12" type="text" placeholder="" name="XMBH" fieldname="XMBH" check-type="maxlength" maxlength="500" operation="like" id="XMBH">
						</td> 
					</tr>
					
					<tr>
						<th colspan="8"  class="right-border bottom-border text-right" >
							<span class="pull-left" style="font-size:16px;">征拆条件</span>
						</th>
					</tr>
					<tr>
						<th width="2%" class="right-border bottom-border">是否征拆</th>
						<td width="7%" class="right-border bottom-border" align="center">
							<select fieldname="ISZC" id="ISZC" name="ISZC" operation="=" logic="and">
							  <option value="" selected="selected">全部</option>
							  <option value="0">不涉及征拆</option>
							  <option value="1">涉及征拆</option>
							</select>
						</td>
						<th width="3%" class="right-border bottom-border text-right">是否完成征拆</th>
						<td width="20%" class="right-border bottom-border" align="center">
							<select fieldname="IS_WC_ZQ" id="IS_WC_ZQ" name="IS_WC_ZQ" operation="=" logic="and">
							  <option value="" selected="selected">全部</option>
							  <option value="0">未完成征拆</option>
							  <option value="1">完成征拆</option>
							</select>
						</td>
						<th width="3%" class="right-border bottom-border text-right">是否超期征拆</th>
						<td width="20%" class="right-border bottom-border" align="center">
							<select fieldname="IS_CQ_ZQ" id="IS_CQ_ZQ" name="IS_CQ_ZQ" operation="=" logic="and">
							  <option value="" selected="selected">全部</option>
							  <option value="0">未超期征拆</option>
							  <option value="1">超期征拆</option>
							</select>
						</td>
					</tr>
					
					<tr>
						<th colspan="8"  class="right-border bottom-border text-right" >
							<span class="pull-left" style="font-size:16px;">排迁条件</span>
						</th>
					</tr>
					<tr>
						<th width="2%" class="right-border bottom-border">是否排迁</th>
						<td width="7%" class="right-border bottom-border" align="center">
							<select fieldname="ISPQ" id="ISPQ" name="ISPQ" operation="=" logic="and">
							  <option value="" selected="selected">全部</option>
							  <option value="0">不涉及排迁</option>
							  <option value="1">涉及排迁</option>
							</select>
						
						</td>
						<th width="3%" class="right-border bottom-border text-right">是否完成排迁</th>
						<td width="20%" class="right-border bottom-border" align="center">
							<select fieldname="IS_WC_PQ" id="IS_WC_PQ" name="IS_WC_PQ" operation="=" logic="and">
							  <option value="" selected="selected">全部</option>
							  <option value="0">未完成排迁</option>
							  <option value="1">完成排迁</option>
							</select>
						</td>		 
						<th width="3%" class="right-border bottom-border text-right">是否超期排迁</th>
						<td width="20%" class="right-border bottom-border" align="center">
							<select fieldname="IS_CQ_PQ" id="IS_CQ_PQ" name="IS_CQ_PQ" operation="=" logic="and">
							  <option value="" selected="selected">全部</option>
							  <option value="0">未超期排迁</option>
							  <option value="1">超期排迁</option>
							</select>
						</td>
					</tr>
					
					<tr>
						<th colspan="8"  class="right-border bottom-border text-right" >
							<span class="pull-left" style="font-size:16px;">开工条件</span>
						</th>
					</tr>
					<tr>
						<th width="2%" class="right-border bottom-border">是否开工</th>
						<td width="7%" class="right-border bottom-border" align="center">
							<select fieldname="ISKG" id="ISKG" name="ISKG" operation="=" logic="and">
							  <option value="" selected="selected">全部</option>
							  <option value="0">未开工</option>
							  <option value="1">已开工</option>
							</select>
						
						</td>
						<th width="3%" class="right-border bottom-border text-right">是否按期开工</th>
						<td width="20%" class="right-border bottom-border" align="center">
							<select fieldname="IS_AQ_KG" id="IS_AQ_KG" name="IS_AQ_KG" operation="=" logic="and">
							  <option value="" selected="selected">全部</option>
							  <option value="0">按期开工</option>
							  <option value="1">延期开工</option>
							</select>
						</td>
					</tr>
					
					<tr>
						<th colspan="8"  class="right-border bottom-border text-right" >
							<span class="pull-left" style="font-size:16px;">完工条件</span>
						</th>
					</tr>
					<tr>
						<th width="2%" class="right-border bottom-border">是否完工</th>
						<td width="7%" class="right-border bottom-border" align="center">
							<select fieldname="ISWG" id="ISWG" name="ISWG" operation="=" logic="and">
							  <option value="" selected="selected">全部</option>
							  <option value="0">未完工</option>
							  <option value="1">已完工</option>
							</select>
						
						</td>
						<th width="3%" class="right-border bottom-border text-right">是否按期完工</th>
						<td width="20%" class="right-border bottom-border" align="center">
							<select fieldname="IS_AQ_WG" id="IS_AQ_WG" name="IS_AQ_WG" operation="=" logic="and">
							  <option value="" selected="selected">全部</option>
							  <option value="0">按期完工</option>
							  <option value="1">延期完工</option>
							</select>
						</td>
					</tr>
				</table>
			</form>
			<div style="height:5px;"> 
			<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" noPage="true" pageNum="1000"></table>
			</div>
			
		</div>
	</div>		
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="ASC" fieldname ="XMBH,XMBS,PXH"/>
		<input type="hidden" name="resultXML"/>
		<input type="hidden" name="queryResult" id="queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
	</FORM>
</div>
</body>
</html>