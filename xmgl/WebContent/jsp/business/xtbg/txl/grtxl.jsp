<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.business.xtbg.txl.service.TxlService" %>
<%@ page import="com.ccthanking.business.xtbg.txl.service.impl.TxlServiceImpl" %>
<%@ page import="com.ccthanking.framework.common.User" %>
<%@ page import="com.ccthanking.framework.Globals" %>
<%
	User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
	TxlService txl = new TxlServiceImpl();
	String[][] privateGroup = txl.showPrivateGroup(user) == null ? new String[0][0] : txl.showPrivateGroup(user);

	String[][] publicGroup = txl.showPublicGroup(user) == null ? new String[0][0] : txl.showPublicGroup(user);
%>
<app:base/>
<title>个人通讯录管理</title>

<script type="text/javascript" charset="UTF-8">

	var id,json,rowindex,rowValue;
	var publicId,publicRowIndex;
  	var controllername= "${pageContext.request.contextPath }/txlController.do";
	$(function() {
		var btn = $("#queryPrivateBtn");
		btn.click(function() {
	        //生成json串
			var data = combineQuery.getQueryCombineData(queryForm, frmPrivatePost, DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryPrivateTxl", data, DT1);
		});
	});
	
	$(function() {
		$("#query_clear_Private").click(function() {
			$("#queryForm").clearFormResult();
		});
		
		$("#query_clear_Public").click(function() {
			$("#queryPublicForm").clearFormResult();
		});
	});
	
	//页面默认参数
	$(document).ready(function() { 
        g_bAlertWhenNoResult = false;
        //生成json串
		var data = combineQuery.getQueryCombineData(queryForm, frmPrivatePost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryPrivateTxl", data, DT1);

        g_bAlertWhenNoResult = true;

        g_bAlertWhenNoResult = false;
        //生成json串
		var data = combineQuery.getQueryCombineData(queryPublicForm, frmPublicPost, DT2);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryPublicTxl", data, DT2);
        g_bAlertWhenNoResult = true;
	}); 
	
	// 查询公共联系人
	$(function() {
		var btn = $("#queryPublicBtn");
		btn.click(function() {
	        //生成json串
			var data = combineQuery.getQueryCombineData(queryPublicForm, frmPublicPost, DT2);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryPublicTxl", data, DT2);
		});
	});
	
	function tr_click(obj,tabListid){
		//$("#queryForm").setFormValues(obj);
		
		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引
		rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
		id = obj.TXID;
		$("#executeTxlFrm").setFormValues(obj);
		
		publicId = obj.TXID;
		publicRowIndex = $("#DT2").getSelectedRowIndex();//获得选中行的索引
		$("#executePublicTxlFrm").setFormValues(obj);
	}

	// 点击添加按钮 清空表单（个人）
	$(function() {
		var btn = $("#insertBtn");
		btn.click(function() {
			$("#executeTxlFrm").clearFormResult(); 
			id = "null";
		});
	});
	
	// 点击添加按钮 清空表单（公共）
	$(function() {
		var btn = $("#insertPublicBtn");
		btn.click(function() {
			$("#executePublicTxlFrm").clearFormResult(); 
			publicId = "null";
		});
	});
	
	// 点击修改按钮（个人）
	$(function() {
		var btn = $("#updateBtn");
		btn.click(function() {
			if($("#DT1").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条要操作的数据！','3');
				// 没有选中结果集行的时候，不显示弹出层，将【修改用户信息】按钮的【data-toggle】属性修改成空即可
				btn.attr("data-toggle","");
			} else {
				// 选中时，需要恢复【修改信息】按钮的【data-toggle】属性值
				btn.attr("data-toggle","modal");
				
			}
		});
	});
	
	// 点击修改按钮（公共）
	$(function() {
		var btn = $("#updatePublicBtn");
		btn.click(function() {
			if($("#DT2").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条要操作的数据！','3');
				// 没有选中结果集行的时候，不显示弹出层，将【修改用户信息】按钮的【data-toggle】属性修改成空即可
				btn.attr("data-toggle","");
			} else {
				// 选中时，需要恢复【修改信息】按钮的【data-toggle】属性值
				btn.attr("data-toggle","modal");
				
			}
		});
	});
	

	// 点击删除按钮
	$(function() {
		var btn = $("#deleteBtn");
		btn.click(function() {
		 	if($("#DT1").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条要操作的数据！','3');
			} else {
				xConfirm("提示信息","是否确认删除！");
				//生成json串
				var data = Form2Json.formToJSON(executeTxlFrm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//通过判断id是否为空来判断是插入还是修改
				$('#ConfirmYesButton').one("click",function(obj) {
					var success = defaultJson.doUpdateJson(controllername + "?executePrivateTxl&id="+id+"&exeSign=3", data1, DT1);
					
					if(success == true) {
						$("#DT1").setSelect(rowindex);
				//		var dd = {id:1,name:2};//此处为入参
						var index = $("#DT1").getSelectedRowIndex();//获得选中行的索引
						var value = $("#DT1").getSelectedRow();//获得选中行的json对象

				        $("#DT1").removeResult(index);
					}
				});  
				
			} 
		});
	});
	
	// 点击保存按钮（个人）
	$(function() {
		var saveUserInfoBtn = $("#saveUserInfo");
		saveUserInfoBtn.click(function() {
			if($("#executeTxlFrm").validationButton()) {
				//生成json串
				var data = Form2Json.formToJSON(executeTxlFrm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//通过判断id是否为空来判断是插入还是修改
				
				var success = false;
				if(id == null || id == "null" || id == "indefined") {
					success = defaultJson.doInsertJson(controllername + "?executePrivateTxl&id="+publicId+"&exeSign=1", data1, DT1);
				} else {
					success = defaultJson.doUpdateJson(controllername + "?executePrivateTxl&id="+publicId+"&exeSign=2", data1, DT1);
				}
				
				if(success == true) {
					$("#myModal").modal("hide");
				} else {
					//	失败x
				}
			}
		});
	});
	
	// 点击保存按钮（公共）
	$(function() {
		var saveUserInfoBtn = $("#savePublicUserInfo");
		saveUserInfoBtn.click(function() {
			if($("#executePublicTxlFrm").validationButton()) {
				//生成json串
				var data = Form2Json.formToJSON(executePublicTxlFrm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//通过判断id是否为空来判断是插入还是修改
				
				var success = false;
				if(id == null || id == "null" || id == "indefined") {
					success = defaultJson.doInsertJson(controllername + "?executePublicTxl&id="+id+"&exeSign=1", data1, DT2);
				} else {
					success = defaultJson.doUpdateJson(controllername + "?executePublicTxl&id="+id+"&exeSign=2", data1, DT2);
				}
				
				if(success == true) {
					$("#publicModal").modal("hide");
				} else {
					//	失败
				}
			}
		});
	});

	//个人組绑定事件(维护)
	$(function() {
		$("#groupIdPage").click(function() {
			$(window).manhuaDialog({"title":"个人通讯录（组）>维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/txl/group.jsp?isPublic=1"});
		});
	});
	
	//公共組绑定事件(维护)
	$(function() {
		$("#groupIdPageCommon").click(function() {
			$(window).manhuaDialog({"title":"公共通讯录（组）>维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/txl/group.jsp?isPublic=2"});
		});
	});
</script>      
</head>
<body>
<app:dialogs/>
<p></p>
<ul class="nav nav-tabs" id="myTab">
  <li class="active"><a href="#home" data-toggle="tab">个人通讯录</a></li>
  <li><a href="#profile" data-toggle="tab">公共通讯录</a></li>
  <li><a href="#messages" data-toggle="tab">内部通讯录</a></li>
</ul>

<div class="tab-content">
	<div class="tab-pane active" id="home">
	
		<div class="container-fluid">
		  <div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
			<h4 class="title">个人通讯录查询条件  
				<span class="pull-right">  
						<a href="#myModal" role="button" class="btn" data-toggle="modal" id="insertBtn">添加</a>
						<a href="#myModal" role="button" class="btn" data-toggle="modal" id="updateBtn">修改</a>
						<a href="#myModal" role="button" class="btn" id="deleteBtn">删除</a>
		      	</span>
		    </h4>
		     <form method="post" id="queryForm"  >
		      <table class="B-table">
		      <!--可以再此处加入hidden域作为过滤条件 -->
		      	<TR  style="display:none;">
			        <TD class="right-border bottom-border"></TD>
					<TD class="right-border bottom-border">
						<input type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" >
					</TD>
		        </TR>
		        <!--可以再此处加入hidden域作为过滤条件 -->
		        <tr>
		          <th width="4%" class="right-border bottom-border">姓名</th>
		          <td width="8%" class="right-border bottom-border">
		          	<input class="span12" type="text" placeholder="" name="XM" fieldname="XM" operation="like" logic="and" ></td>
		          <th width="4%" class="right-border bottom-border">性别</th>
		          <td width="8%" class="bottom-border">
		          	<select class="span12" type="text" placeholder="" name="XB" fieldname="XB" operation="=" logic="and" kind="dic" src="XB" defaultMemo="全部"></select></td>
		          <th width="4%" class="right-border bottom-border">职位</th>
		          <td width="8%" class="right-border bottom-border">
		          	<input class="span12" type="text" placeholder="" name="ZW" fieldname="ZW" operation="=" logic="and"></td>
		          	
		          
			        <td width="10%"  class="text-left bottom-border text-right" rowspan="2">
						<button	id="queryPrivateBtn" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
	                    <button id="query_clear_Private" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
		            </td>	
		        </tr>
		        <tr>
		          <th width="4%" class="right-border bottom-border">工作电话</th>
		          <td width="8%" class="right-border bottom-border">
		          	<input class="span12" type="text" placeholder="" name="GZDH" fieldname="GZDH" operation="like"  logic="and"></td>
		          	
		          <th width="4%" class="right-border bottom-border">手机号码</th>
		          <td width="8%" class="right-border bottom-border">
		          	<input class="span12" type="text" placeholder="" name="SJHM" fieldname="SJHM" operation="like" logic="and"></td>
		         
		          <th width="4%" class="right-border bottom-border">组</th>
		          <td width="13%" class="right-border bottom-border">
		          	<!-- <input class="span12" type="text" style="width:90%" placeholder="" id="GROUPID" name="GROUPID" fieldname="GROUPID" operation="=" logic="and"> -->
		          	<select class="span12" type="text" style="width:70%" placeholder="" id="GROUPID" name="GROUPID" fieldname="GROUPID" operation="=" logic="and">
		          		<option value="">全部</option>
		          		<%
          						for(int j = 0; j < privateGroup.length; j++) {
          							%>
          	          				<option value="<%=privateGroup[j][0] %>"><%=privateGroup[j][1] %></option>
          	          				<%
          						}
		          		%>
		          	</select>
		          	<span class="pull-right"><button id="groupIdPage" class="btn btn-link" type="button">维护</button></span>
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
		                    <th fieldname="XM">&nbsp;姓名&nbsp;</th>
							<th fieldname="NC" >&nbsp;昵称&nbsp;</th>
							<th fieldname="XB" kind="dic" >&nbsp;性别&nbsp;</th>
							<th fieldname="CSRQ"  fieldformat="YYYY-MM-DD" colindex=4>&nbsp;出生日期&nbsp;</th>
							<th fieldname="DW" >&nbsp;单位&nbsp;</th>
							<th fieldname="ZW" >&nbsp;职位&nbsp;</th>
							<th fieldname="GZDH" >&nbsp;工作电话&nbsp;</th>
							<th fieldname="YX" >&nbsp;邮箱&nbsp;</th>
							<th fieldname="SJHM" >&nbsp;手机号码&nbsp;</th>
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
			<FORM name="frmPrivatePost" method="post" style="display:none" target="_blank" id ="frmPrivatePost">
			 <!--系统保留定义区域-->
		        <input type="hidden" name="queryXML" id = "queryXML">
		        <input type="hidden" name="txtXML" id = "txtXML">
		        <input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ" id="txtFilter">
		        <input type="hidden" name="resultXML" id = "resultXML">
		        <input type="hidden" name="queryResult" id = "queryResult">
		      		 <!--传递行数据用的隐藏域-->
		        <input type="hidden" name="rowData">
			
			</FORM>
		</div>
	</div>
	 
	<div class="tab-pane" id="profile">
	
		<div class="container-fluid">
		  <div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
			<h4 class="title">公共通讯录查询条件  
				<span class="pull-right">  
						<a href="#publicModal" role="button" class="btn" data-toggle="modal" id="insertPublicBtn">添加</a>
						<a href="#publicModal" role="button" class="btn" data-toggle="modal" id="updatePublicBtn">修改</a>
						<a href="#publicModal" role="button" class="btn" id="deletePublicBtn">删除</a>
		      	</span>
		    </h4>
		     <form method="post" id="queryPublicForm"  >
		      <table class="B-table">
		      <!--可以再此处加入hidden域作为过滤条件 -->
		      	<TR  style="display:none;">
			        <TD class="right-border bottom-border"></TD>
					<TD class="right-border bottom-border">
						<input type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" >
					</TD>
		        </TR>
		        <!--可以再此处加入hidden域作为过滤条件 -->
		        <tr>
		          <th width="4%" class="right-border bottom-border">姓名</th>
		          <td width="8%" class="right-border bottom-border">
		          	<input class="span12" type="text" placeholder="" name="XM" fieldname="XM" operation="like" logic="and" ></td>
		          <th width="4%" class="right-border bottom-border">性别</th>
		          <td width="8%" class="bottom-border">
		          	<select class="span12" type="text" placeholder="" name="XB" fieldname="XB" operation="=" logic="and" kind="dic" src="XB" defaultMemo="全部"></select></td>
		          
		          <th width="4%" class="right-border bottom-border">职位</th>
		          <td width="8%" class="right-border bottom-border">
		          	<input class="span12" type="text" placeholder="" name="ZW" fieldname="ZW" operation="=" logic="and"></td>
		          	
		          	
			        <td width="10%"  class="text-left bottom-border text-right" rowspan="2">
						<button	id="queryPublicBtn" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
	                    <button id="query_clear_Public" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
		            </td>	
		        </tr>
		        <tr>
		          <th width="4%" class="right-border bottom-border">工作电话</th>
		          <td width="8%" class="bottom-border">
		          	<input class="span12" type="text" placeholder="" name="GZDH" fieldname="GZDH" operation="like"  logic="and"></td>
		          <th width="4%" class="right-border bottom-border">手机号码</th>
		          <td width="8%" class="right-border bottom-border">
		          	<input class="span12" type="text" placeholder="" name="SJHM" fieldname="SJHM" operation="like" logic="and"></td>
		         
		          <th width="4%" class="right-border bottom-border">组</th>
		          <td width="13%" class="right-border bottom-border">
		          	<!-- <input class="span12" type="text" style="width:90%" placeholder="" id="GROUPID" name="GROUPID" fieldname="GROUPID" operation="=" logic="and"> -->
		          	<select class="span12" style="width:70%" placeholder="" id="GROUPID" name="GROUPID" fieldname="GROUPID" operation="=" logic="and">
		          		<option value="">全部</option>
		          		<%
          						for(int j = 0; j < publicGroup.length; j++) {
          							%>
          	          				<option value="<%=publicGroup[j][0] %>"><%=publicGroup[j][1] %></option>
          	          				<%
          						}
		          		%>
		          	</select>
		          	<span class="pull-right"><button id="groupIdPageCommon" class="btn btn-link" type="button">维护</button></span>
		          </td>
		        </tr>
		      </table>
		      </form>
			<div style="height:5px;"> </div>		
				<div class="overFlowX"> 
		           <table width="100%" class="table-hover table-activeTd B-table" id="DT2" type="single">
		                <thead>
		                    <tr>
		                    <th name="XH" id="_XH">序号</th>
		                    <th fieldname="XM">姓名</th>
							<th fieldname="NC" >昵称</th>
							<th fieldname="XB" kind="dic" >性别</th>
							<th fieldname="CSRQ"  fieldformat="YYYY-MM-DD" colindex=4>出生日期</th>
							<th fieldname="DW" >单位</th>
							<th fieldname="ZW" >职位</th>
							<th fieldname="GZDH" >工作电话</th>
							<th fieldname="YX" >邮箱</th>
							<th fieldname="SJHM" >手机号码</th>
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
			<FORM name="frmPublicPost" method="post" style="display:none" target="_blank" id ="frmPublicPost">
			 <!--系统保留定义区域-->
		        <input type="hidden" name="queryXML" id = "queryXML">
		        <input type="hidden" name="txtXML" id = "txtXML">
		        <input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ" id="txtFilter">
		        <input type="hidden" name="resultXML" id = "resultXML">
		        <input type="hidden" name="queryResult" id = "queryResult">
		      		 <!--传递行数据用的隐藏域-->
		        <input type="hidden" name="rowData">
			
			</FORM>
		</div>
	</div>
	
	<div class="tab-pane" id="messages">...messages</div>
</div>
<!-- Modal -->
<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-header" style="background:#0866c6;">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-remove icon-white"></i></button>
    <h3 id="myModalLabel" style="color:white;">个人通讯录</h3>
  </div>
  <div class="modal-body">
    <form method="post" id="executeTxlFrm"  >
      <table class="B-table" >
         <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			 	<input type="text" kind="text" fieldname="TXID" name="TXID" id="TXID">
			</TD>
        </TR>
        <tr>
          <th width="30%" class="right-border bottom-border">姓名</th>
          <td class="right-border bottom-border">
          	<input type="text" placeholder="必填"  check-type="required maxlength" maxlength="30" fieldname="XM" name="XM" id="XM"></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">昵称</th>
          <td class="right-border bottom-border">
          	<input type="text" maxlength="50" fieldname="NC" name="NC" id="NC"></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">性别</th>
          <td class="right-border bottom-border">
          	<select type="text" placeholder="必填" fieldname="XB" name="XB" id="XB" kind="dic" src="XB"></select></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">出生日期</th>
          <td class="right-border bottom-border">
          	<input type="date" fieldname="CSRQ" name="CSRQ" id="CSRQ" fieldtype="date" fieldformat="YYYY-MM-DD" /></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">单位</th>
          <td class="right-border bottom-border">
          	<!-- <select type="text" placeholder="必填"  check-type="required" fieldname="DW" name="DW" id="DW" ></select> -->
          	<input type="text" fieldname="DW" name="DW" id="DW" />
          </td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">个人组</th>
          <td class="right-border bottom-border">
       	     <select class="span12" style="width:90%" placeholder="" id="GROUPID" name="GROUPID" fieldname="GROUPID" operation="=" logic="and">
          		<option value="">请选择</option>
          		<%
        						for(int j = 0; j < privateGroup.length; j++) {
        							%>
        	          				<option value="<%=privateGroup[j][0] %>"><%=privateGroup[j][1] %></option>
        	          				<%
        						}
          		%>
          	</select>
       	  </td>
        </tr>
      </table>
    </form>
  </div>
  <div class="modal-footer">
    <button class="btn" id="saveUserInfo">保存</button>
    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
  </div>
</div>



<div id="publicModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-header" style="background:#0866c6;">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-remove icon-white"></i></button>
    <h3 id="myModalLabel" style="color:white;">公共通讯录</h3>
  </div>
  <div class="modal-body">
    <form method="post" id="executePublicTxlFrm"  >
      <table class="B-table" >
         <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			 	<input type="text" kind="text" fieldname="TXID" name="TXID" id="TXID">
			</TD>
        </TR>
        <tr>
          <th width="30%" class="right-border bottom-border">姓名</th>
          <td class="right-border bottom-border">
          	<input type="text" placeholder="必填"  check-type="required maxlength" maxlength="30" fieldname="XM" name="XM" id="XM"></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">昵称</th>
          <td class="right-border bottom-border">
          	<input type="text" fieldname="NC" maxlength="50" name="NC" id="NC"></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">性别</th>
          <td class="right-border bottom-border">
          	<select type="text" placeholder="必填" fieldname="XB" name="XB" id="XB" kind="dic" src="XB"></select></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">出生日期</th>
          <td class="right-border bottom-border">
          	<input type="date" fieldname="CSRQ" name="CSRQ" id="CSRQ" fieldtype="date" fieldformat="YYYY-MM-DD" /></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">单位</th>
          <td class="right-border bottom-border">
          	<!-- <select type="text" placeholder="必填"  check-type="required" fieldname="DW" name="DW" id="DW" ></select> -->
          	<input type="text" fieldname="DW" name="DW" id="DW" />
          </td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">公共组</th>
          <td class="right-border bottom-border">
       	     <select class="span12" style="width:90%" placeholder="" id="GROUPID" name="GROUPID" fieldname="GROUPID" operation="=" logic="and">
          		<option value="">请选择</option>
          		<%
        						for(int j = 0; j < publicGroup.length; j++) {
        							%>
        	          				<option value="<%=publicGroup[j][0] %>"><%=publicGroup[j][1] %></option>
        	          				<%
        						}
          		%>
          	</select>
       	  </td>
        </tr>
      </table>
    </form>
  </div>
  <div class="modal-footer">
    <button class="btn" id="savePublicUserInfo">保存</button>
    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
  </div>
</div>

</body>
</html>