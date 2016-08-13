<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="com.ccthanking.framework.common.UserTemplate"%>
<%@ page import="com.ccthanking.framework.coreapp.aplink.*"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<app:base/>
<title>短信息查询</title>
<%

User user = (User) session.getAttribute(Globals.USER_KEY);
String userid = user.getAccount();

%>


<script type="text/javascript" charset="UTF-8">
  	var controllername= "${pageContext.request.contextPath }/messageController.do";
  	
  	
  	//计算本页表格分页数
  	function setPageHeight(){
  		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
  		var pageNum = parseInt(height/pageTableOne,10);
  		$("#DT1").attr("pageNum",pageNum);
  	}
  	
	//页面默认参数
	$(document).ready(function() { 
		setPageHeight();
        //生成json串
        g_bAlertWhenNoResult = false;
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?getUserMessage", data, DT1);
        g_bAlertWhenNoResult = true;
	}); 
	
	$(function() {
		var btn = $("#queryBtn");
		btn.click(function() {
	        //生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?getUserMessage", data, DT1);
		});
	});
	
	// 清除表单
	$(function() {
		$("#query_clear").click(function() {
	       $("#queryForm").clearFormResult();
		});
	});
	
	function tr_click(obj, tabListid) {
		var id = obj.OPID;
		if(obj.STATE!="0"){
			//如果已经是“已读”状态，那么不需要再更新状态了
			changezt(id);
		}
	}
	function getXxzt(obj){
		var xxzt = obj.STATE;
		if(xxzt=="1"){
			return "<div style=\"text-align:center\"><a href=\"javascript:void(0)\" onclick=\"changezt('"+obj.OPID+"')\"><i class=\"icon-green\" title=\"未读\"></i></a></div>";
		}else{
			return '<div style="text-align:center"><i class="icon-gray" title=\"已读\"></i></div>';
		}
		
	}
	
	
	function changezt(id){
		var rowindex = null;//获得选中行的索引
    	var $this = $("#DT1");
        var rows = $("tbody tr" ,$this);
        for(var i=0;i<rows.size();i++){
        	var obj = JSON.parse(rows.eq(i).attr("rowJson"));
        	if(obj.OPID==id){
        		rowindex=i;
        	}
        }

		$.ajax({
			url:controllername + "?changeZt&opid="+id,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				var res = result.msg;
				var subresultmsgobj = defaultJson.dealResultJson(res);
				var comprisesJson = $("#DT1").comprisesJson(subresultmsgobj,rowindex);
				var strarr = $("#DT1").updateResult(JSON.stringify(comprisesJson),DT1,rowindex);
				$("#DT1").setSelect(rowindex);
				// xAlert("信息提示","已更新消息状态为已读！","1");
				 window.parent.getDxx();
			}
		});
		return showHtml;
	}
	
	//详细信息
	function rowView(url){
		var openUrl = {"title":"消息查看","type":"text","content":"${pageContext.request.contextPath}"+url,"modal":"1"};
		$(window).manhuaDialog(openUrl); 
		e.preventDefault();
	    //e.stopPropagation();
	}
	
	function showLink(obj){
		var url = obj.LINKURL;
		if(url == "" || url == "url"){
			return obj.TITLE;
		}else{
			return '<a name="messageLink" href="javascript:void(0);" onclick="rowView(\'' + url+ '\')">'+obj.TITLE+'</a>';
		}
	}
	function showSuccessMsg(title,content,type){
		title=title==undefined?"提示信息":title;
		content=content==undefined?"操作成功!":content;
		type=type==undefined?"1":type;
		xAlert(title,content,type);
	}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">消息查询
				<span class="pull-right">
				
				</span>
			</h4>
		<form method="post" id="queryForm"  >
		<table class="B-table">
		<!--可以再此处加入hidden域作为过滤条件 -->
			<TR  style="display:none;">
				<TD class="right-border bottom-border"></TD>
				<TD class="right-border bottom-border">
					<input type="text" class="span12" kind="text"  fieldname="rownum" keep="true" value="1000" operation="<=" >
					<input type="text" class="span12" kind="text"  fieldname="USERTO" keep="true" value="<%=userid%>" operation="=" >
					
				</TD>
			</TR>
		<!--可以再此处加入hidden域作为过滤条件 -->
			<tr>
				<th width="5%" class="right-border bottom-border">发信人姓名</th>
				<td width="10%" class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" id="USERFROMNAME" name="USERFROMNAME" fieldname="USERFROMNAME" operation="like" logic="and" ></td>
				<th width="5%" class="right-border bottom-border">标题</th>
				<td width="10%" class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" name="TITLE" id="TITLE" fieldname="TITLE" operation="like" logic="and"></td>
					
				<th width="8%" class="right-border bottom-border">发送日期（起止）</th>
				<td width="35%" class="right-border bottom-border">
					<input id="ZFRQB" class="span5" type="date" fieldtype="date" autocomplete="off" placeholder="" name="OPTIME" check-type="maxlength" maxlength="10" fieldname="OPTIME" fieldformat="yyyy-MM-dd" operation=">="/>
							--
				    <input id="ZFRQE" class="span5" type="date" fieldtype="date" autocomplete="off" placeholder="" name="OPTIME" check-type="maxlength" maxlength="10" fieldname="OPTIME" fieldformat="yyyy-MM-dd" operation="<="/>
				</td>				
				<td width="13%"  class="text-left bottom-border text-right" rowspan="2">
					<button	id="queryBtn" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
                    <button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
	            </td>	
			</tr>
		</table>
		</form>
	
	<div style="height:5px;"> </div>		
	<div class="overFlowX"> 
		<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
			<thead>
				<tr>
					<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
					<th fieldname="STATE" tdalign="center" CustomFunction="getXxzt">&nbsp;&nbsp;</th>
					<th fieldname="USERFROMNAME" tdalign="center">&nbsp;发信人姓名&nbsp;</th>
					<th fieldname="TITLE" CustomFunction="showLink" noprint="true">&nbsp;标题&nbsp;</th>
					<th fieldname="OPTIME" tdalign="center">&nbsp;发送时间&nbsp;</th>
					<th fieldname="CONTENT" maxlength="70">&nbsp;内容&nbsp;</th>

				</tr>
			</thead>
		    <tbody></tbody>
		</table>
		            
		</div>
		
		</div>
	</div>
</div>

<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id="queryXML">
		<input type="hidden" name="txtXML" id="txtXML">
		<input type="hidden" name="txtFilter" order="desc" fieldname="OPTIME" id="txtFilter">
		<input type="hidden" name="resultXML" id="resultXML">
		<input type="hidden" name="queryResult" id="queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">
	</FORM>
</div>

</body>
</html>