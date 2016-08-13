<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<title>流程配置</title>
<%
  String type = request.getParameter("type");
  
%>
<script type="text/javascript" charset="utf-8">

var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglLybzjController.do";
var flag;
var type = "<%=type%>";

//页面初始化
$(function() {
	
	<% if("update".equals(type)){ %>
	var tableObj = $(window).manhuaDialog.getParentObj().document.getElementById("DT1");
	var $tableObj;
	if(tableObj){
		$tableObj = $(tableObj);
	}
	if($tableObj.getSelectedRowIndex()>-1){
		var obj = $tableObj.getSelectedRowJsonObj();
		$("#demoForm").setFormValues(obj);
		var s= $('input:radio[name="ISMS"]:checked');
		showDetail(s[0])
	}
	<%}%>
	
	$("#blrBtn").click(function(){
		var actorCode = $("#ACTOR").attr("code");
		openUserTree('multi',actorCode,'setBlr') ;
	});
	$("#csrBtn").click(function(){
		var actorCode = $("#CCACTOR").attr("code");
		openUserTree('multi',actorCode,'setCsr') ;
	});
	$("#bldwBtn").click(function(){
		var deptidCode = $("#DEPTID").attr("code");
		openDeptTree('single',deptidCode,'setBldw') ;
	});
	$("#btnSave").click(function(){
		var list= $('input:radio[name="ISMS"]:checked').val();
		if(list==null){
			xAlert("信息提示","请选择报送方式",3);
			return false;
		}
		if($("#demoForm").validationButton()){
				//生成json串
		 	var data = Form2Json.formToJSON(demoForm);
		 	$(window).manhuaDialog.getParentObj().insertStep(data,type);
		 	$(window).manhuaDialog.close();
		}
	});

});
//页面默认参数
function init(){

}
function setBldw(data){
	var deptid = "";
	var deptname = "";
	for(var i=0;i<data.length;i++){
 	 var tempObj =data[i];
 	 if(i<data.length-1){
 		deptid +=tempObj.DEPTID+",";
 		deptname +=tempObj.DEPTNAME+",";
 	 }else{
 		deptid +=tempObj.DEPTID;
 		deptname +=tempObj.DEPTNAME; 
 	 }
	}
	document.getElementById("DEPTID").value=deptname;
	$("#DEPTID").attr("code",deptid);

}

function setBlr(data){
	var userId = "";
	var userName = "";
	for(var i=0;i<data.length;i++){
 	 var tempObj =data[i];
 	 if(i<data.length-1){
	  userId +=tempObj.ACCOUNT+",";
	  userName +=tempObj.USERNAME+",";
 	 }else{
 	  userId +=tempObj.ACCOUNT;
 	  userName +=tempObj.USERNAME; 
 	 }
	}
	document.getElementById("ACTOR").value=userName;
	$("#ACTOR").attr("code",userId);

}
function setCsr(data){
	var userId = "";
	var userName = "";
	for(var i=0;i<data.length;i++){
 	 var tempObj =data[i];
 	 if(i<data.length-1){
	  userId +=tempObj.ACCOUNT+",";
	  userName +=tempObj.USERNAME+",";
 	 }else{
 	  userId +=tempObj.ACCOUNT;
 	  userName +=tempObj.USERNAME; 
 	 }
	}
	document.getElementById("CCACTOR").value=userName;
	$("#CCACTOR").attr("code",userId);

	
}
//弹出区域回调
getWinData = function(data){
	
};
function setJdmc(obj){
	var s = $(obj).val();
		document.getElementById("NAME").value=s;
}
function showDetail(obj){
	var radioValue = obj.value;
	var trObj = $("#DT1").find("tr");
	 showAlltr();
	if(radioValue=="1")
	{
		$(trObj[2])[0].style.display="none";
		clearRole();
	}else if(radioValue=="2")
	{
		$(trObj[3])[0].style.display="none";
		clearActor();
	}else if(radioValue=="7"||radioValue=="6")
	{
		$(trObj[3])[0].style.display="none";
		clearActor();
	}else //if(radioValue=="3"||radioValue=="4")
	{
		$(trObj[3])[0].style.display="none";
		$(trObj[2])[0].style.display="none";
		clearRole();
		clearActor();
    }
	
}
function clearRole(){
	document.getElementById("DEPTID").value="";
	//$("#DEPTID").attr("value","");
	$("#DEPTID").attr("code","");
	$("#ROLENAME").get(0).options[0].selected=true;  

	
}function clearActor(){
	document.getElementById("ACTOR").value="";
	//$("#ACTOR").attr("value","");
	$("#ACTOR").attr("code","");
}
function showAlltr(){
	var trObj = $("#DT1").find("tr");
	for(var i =0;i<trObj.length;i++){
		$(trObj[i])[0].style.display="";
	}
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
      			节点信息
				<span class="pull-right">
		      		<button id="btnSave" class="btn" type="button">保存</button>
				</span>    			
      		</h4>
     		<form method="post" id="demoForm">
      			<table class="B-table" width="100%" id="DT1">
      				<input type="hidden" id="PROCESSTYPEOID" fieldname="PROCESSTYPEOID" name="PROCESSTYPEOID"/>
   			        <tr>
			           <th width="8%" class="right-border bottom-border text-right">报送方式</th>
			       	 	<td class="bottom-border right-border" width="23%" colspan="3">
			         		<input class="span12" onclick="showDetail(this)"  type="radio" check-type="required" fieldname="ISMS" id="ISMS"  kind="dic" src="SPJDBSFS" name = "ISMS" style="width:90%"  />
			       	 	</td>
			        </tr>
					<tr>
				
						<th width="8%" class="right-border bottom-border text-right">节点名称</th>
			       	 	<td class="bottom-border right-border" width="33%">
			         		<input class="span12"   type="text" placeholder="必填" check-type="required" fieldname="NAME" id="NAME" name = "NAME"  />
			          		
			       	 	</td>
			         	<th width="8%" class="right-border bottom-border text-right">规定天数</th>
						<td width="17%" class="right-border bottom-border">
							<input id="SHEDULE_DAYS" class="span5"  check-type="maxlength" maxlength="40" name="SHEDULE_DAYS" fieldname="SHEDULE_DAYS" type="number" />
						</td>
			        </tr>
			        <tr>
			            <th width="8%" class="right-border bottom-border text-right">节点单位</th>
                       <td class="bottom-border right-border" width="33%" >
			         		<input class="span12"   type="text" check-type="" fieldname="DEPTID" id="DEPTID" name = "DEPTID" style="width:90%" disabled />
			         		<button class="btn btn-link"  type="button" id="bldwBtn" title="点击选择节点单位"><i class="icon-edit"></i></button>
			       	 	</td>
			         	<th width="8%" class="right-border bottom-border text-right">节点角色</th>
			       		<td class="bottom-border right-border"width="15%">
                        <select class="span12" id="ROLENAME" name = "ROLENAME"  fieldname="ROLENAME" kind="dic" src="T#fs_org_role:ROLE_ID:NAME:ROLETYPE='2'">

                        </select>
			         	</td>					
			         	
			        </tr>
			        
			        <tr>
			           <th width="8%" class="right-border bottom-border text-right">办理人</th>
			       	 	<td class="bottom-border right-border" width="23%" colspan="3">
			         		<input class="span12"   type="text" check-type="" fieldname="ACTOR" id="ACTOR" name = "ACTOR" style="width:90%" disabled />
			         		<button class="btn btn-link"  type="button" id="blrBtn" title="点击选择办理人"><i class="icon-edit"></i></button>
			       	 	</td>
			        </tr>
			        <tr>
			           <th width="8%" class="right-border bottom-border text-right">抄送人</th>
			       	 	<td class="bottom-border right-border" width="23%" colspan="3">
			         		<input class="span12"  type="text"  fieldname="CCACTOR" id="CCACTOR" name = "CCACTOR" style="width:90%" disabled />
			         		<button class="btn btn-link"  type="button" id="csrBtn" title="点击选择抄送人"><i class="icon-edit"></i></button>
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