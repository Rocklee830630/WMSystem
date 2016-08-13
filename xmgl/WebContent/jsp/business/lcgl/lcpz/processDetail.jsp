<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<title>流程配置</title>
<%
  String processoid = request.getParameter("processoid");
  
%>
<script type="text/javascript" charset="utf-8">

var controllername= "${pageContext.request.contextPath }/lcglController.do";
var flag;
var processoid = "<%=processoid%>";

//页面初始化
$(function() {
	g_bAlertWhenNoResult = false;
	defaultJson.doQueryJsonList(controllername+"?queryProcessWs&processoid="+processoid, "", DT1);	
	g_bAlertWhenNoResult = true;

	$("#btnSave").click(function(){
		if($("#demoForm").validationButton()){
		 		//生成json串
		 	var data = Form2Json.formToJSON(demoForm);
				//组成保存json串格式
				//alert(data);
			var data1 = defaultJson.packSaveJson(data);
				//调用ajax插入
			var id = document.getElementById("WS_TYPZ_ID").value;
			if(id!=null&&id!=""){
				defaultJson.doUpdateJson(controllername + "?saveApwstypz&type=update", data1,DT1);
			}else{
				defaultJson.doInsertJson(controllername + "?saveApwstypz&type=add", data1,DT1);
			}
	        $("#demoForm").clearFormResult();

			

		}
	});
	$("#deptBtn").click(function(){
		var deptCode = $("#DEPTID").attr("code");
		openDeptTree('single',deptCode,'setDept') ;
	});
	  var btn_clearQuery=$("#clear");
	    btn_clearQuery.click(function() {
	        $("#demoForm").clearFormResult();
	    	$("#DT1").cancleSelected();
	    });

});
//页面默认参数
function init(){
 
}

function setDept(data){
	var Id = "";
	var Name = "";
	for(var i=0;i<data.length;i++){
 	 var tempObj =data[i];
 	 if(i<data.length-1){
 		Id +=tempObj.DEPTID+",";
 		Name +=tempObj.DEPTNAME+",";
 	 }else{
 		Id +=tempObj.DEPTID;
 		Name +=tempObj.DEPTNAME; 
 	 }
	}
	document.getElementById("DEPTID").value=Name;
	$("#DEPTID").attr("code",Id);

	
}
//弹出区域回调
getWinData = function(data){
	
};

function tr_click(obj,tabListid){
	$("#demoForm").setFormValues(obj);

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
      			流程文书关联信息
				<span class="pull-right">
		      		<button id="btnSave" class="btn" type="button">保存</button>
		      		<button id="clear" class="btn" type="button">清空</button>
				</span>    			
      		</h4>
     		<form method="post" id="demoForm">
      			<table class="B-table" width="100%" >
      				<input type="hidden" id="OPERATIONOID" fieldname="OPERATIONOID" name="OPERATIONOID" value="<%=processoid%>" keep="true"/>
      				<input type="hidden" id="WS_TYPZ_ID" fieldname="WS_TYPZ_ID" name="WS_TYPZ_ID"/>
					<tr>
						
			         	<th width="8%" class="right-border bottom-border text-right">业务类型</th>
			       		<td width="42%" class="bottom-border right-border"width="15%">
                        <select class="span12" id="YWLX" name = "YWLX" onchange="setJdmc(this)" fieldname="YWLX" kind="dic" src="YWLX"    check-type="required">

                        </select>
			         	</td>
			         	<th width="8%" class="right-border bottom-border text-right">文书模版</th>
						<td width="42%" class="right-border bottom-border" >
						<select class="span12" id="WS_TEMPLATE_ID" name = "WS_TEMPLATE_ID" fieldname="WS_TEMPLATE_ID" kind="dic" check-type="" src="T#WS_TEMPLATE:WS_TEMPLATE_ID:WS_TEMPLATE_NAME:">

                        </select>
						</td>
			        </tr>
                    <tr>
						<!-- <th width="8%" class="right-border bottom-border text-right">发起人角色</th>
			       	 	<td class="bottom-border right-border" width="23%">
                        <select class="span12" id="ROLENAME" name = "ROLENAME"  fieldname="ROLENAME" kind="dic"  src="T#fs_org_role:ROLE_ID:NAME:ROLETYPE='2'" >

                        </select>
                        </td> -->
			         	
			         	<th width="8%" class="right-border bottom-border text-right">发起单位</th>
			       	 	<td width="42%" class="bottom-border right-border" width="23%" >
			         		<input class="span12"  type="text"  fieldname="DEPTID" id="DEPTID" check-type="required" name = "DEPTID" style="width:90%" disabled />
			         		<button class="btn btn-link"  type="button" id="deptBtn"><abbr title="点击选择单位"><i class="icon-edit"></i></abbr></button>
			       	 	</td>
			       	 	<td  class="right-border bottom-border" colspan=2></td>
			        </tr>
			        <tr>
			           
			        </tr>

     		</table>
      	</form>
      	<div style="height:5px;"> </div>		
      <div class= "overFlowX">
		<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
			<thead>
				<tr>
					<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
					<th fieldname="YWLX" tdalign="center">&nbsp;业务类型&nbsp;</th>   
					<th fieldname="WS_TEMPLATE_ID" tdalign="center" >&nbsp;文书名称&nbsp;</th>
					<!-- <th fieldname="ROLENAME" tdalign="center" >&nbsp;发起人角色&nbsp;</th> -->
					<th fieldname="DEPTID" tdalign="center" >&nbsp;发起人所在单位&nbsp;</th>
					
				</tr>
			</thead>
		    <tbody></tbody>
		</table>
		            
		</div>
	</div>
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