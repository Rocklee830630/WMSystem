<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<title>查询用户操作日志</title>
<%
	String id=request.getParameter("id");
%>
<script type="text/javascript" charset="UTF-8">

var controllername= "${pageContext.request.contextPath }/logController.do";

	//计算本页表格分页数
	function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}

$(function() {
	var logId = "<%=id %>";
	$("#loginId")[0].value = logId;
	if(logId != "null") {
	    //生成json串
		var data = combineQuery.getQueryCombineData(queryForm1, frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryLogOperation",data,DT1);
	}
	
	var btn = $("#opBtn");
	btn.click(function() {
        //生成json串
		var loginTime = $("#loginTime").val();
		var loginTime_ = $("#loginTime_").val();
        if(loginTime!=""&&loginTime_!=""){
        	if(loginTime>loginTime_){
        		xAlert("信息提示","登录起始时间不能大于终止时间！",'3');
        		return;
        	}
        }
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryLogOperation",data,DT1);
	});
});


//页面默认参数
$(document).ready(function() { 
	setPageHeight();
    //生成json串
        g_bAlertWhenNoResult = false;
	var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryLogOperation", data, DT1);
    g_bAlertWhenNoResult = true;
}); 

// 清除表单
$(function() {
	$("#query_clear").click(function() {
       $("#queryForm").clearFormResult();
	});
});

</script>    
</head>
<body>
<app:dialogs/>

<div class="container-fluid">
  <p></p>
  <div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
  <%
  	if(id == null) {
  %>
			<h4 class="title">操作日志查询</h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<input type="text" class="span12" kind="text"  fieldname="rownum"  value="10000" operation="<=" >
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
          <th width="5%" class="right-border bottom-border text-right">用户登录名</th>
          <td width="15%" class="right-border bottom-border">
          	<input class="span12" type="text" placeholder="" name = "userId" fieldname = "USERID" operation="like" logic = "and" ></td>
          <th width="5%" class="right-border bottom-border text-right">用户姓名</th>
          <td width="15%" class="bottom-border">
          	<input class="span12" type="text" placeholder="" name = "userName" fieldname = "USERNAME" operation="like" logic = "and" ></td>
          <th width="5%" class="right-border bottom-border text-right">登录时间</th>
          <td width="10%" class="right-border bottom-border">
          	<input class="span12" type="date" placeholder="" id="loginTime" name = "operateTime" fieldname = "OPERATETIME" operation=">=" logic = "and"  fieldtype="date" fieldformat="YYYY-MM-DD"></td>
          <th width="3%" class="right-border bottom-border">至</th>
          <td width="10%" colspan="2" class="bottom-border">
          	<input class="span12" type="date" placeholder="" id="loginTime_" name = "operateTime" fieldname = "OPERATETIME" operation="<="  logic="and"  fieldtype="date" fieldformat="YYYY-MM-DD"></td>
        
			<td width="32%"  class="text-left bottom-border text-right">
				<button	id=opBtn class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
                <button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
            </td>
        </tr>
      </table>
      </form>
  <% } %>
  
  <form method="post" id="queryForm1"  >
    <input class="span12" type="hidden" placeholder="" id="loginId" name="loginId" fieldname="LOGINID" operation="=" logic="and" ></td>
  </form>
	<div style="height:5px;"> </div>		
	<div class="overFlowX"> 
           <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
                <thead>
                    <tr>
                    <th name="XH" id="_XH" width="3%">#</th>
                    <th fieldname="USERID" id="USERID" colindex=1>&nbsp;用户登录名&nbsp;</th>
					<th fieldname="USERNAME" colindex=2>&nbsp;用户姓名&nbsp;</th>
					<th fieldname="USERDEPTID" colindex=3>&nbsp;用户单位&nbsp;</th>
					<th fieldname="OPERATEIP" colindex=4>&nbsp;用户登录IP&nbsp;</th>
					<th fieldname="OPERATETIME" fieldformat="YYYY-MM-DD" colindex=5 tdalign="center" width="10%">&nbsp;操作时间&nbsp;</th>
					<th fieldname="YWLX" colindex=6 tdalign="center">&nbsp;业务类型&nbsp;</th>
					<th fieldname="RESULT" kind="dic" colindex=7 tdalign="center">&nbsp;操作结果&nbsp;</th>
					<th fieldname="MEMO" kind="dic" colindex=8>&nbsp;操作描述&nbsp;</th>
					<th fieldname="OPERATETYPE" kind="dic" colindex=8 tdalign="center">&nbsp;操作类型&nbsp;</th>
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
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id="frmPost">
	 <!--系统保留定义区域-->
        <input type="hidden" name="queryXML" id = "queryXML">
        <input type="hidden" name="txtXML" id = "txtXML">
        <input type="hidden" name="txtFilter"  order="desc" fieldname = "OPERATETIME"	id = "txtFilter">
        <input type="hidden" name="resultXML" id = "resultXML">
		<input type="hidden" name="queryResult" id="queryResult">
      		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">		
	</FORM>
</div>
</body>
</html>