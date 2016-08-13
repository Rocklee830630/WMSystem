<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.*"%>

<%@ page import="com.ccthanking.framework.common.*"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%
	User user = (User) session.getAttribute(Globals.USER_KEY);
    String username = user.getAccount();
    OrgDept dept = user.getOrgDept();
    String fzr = dept.getFzr();
    String deptid = "";
    if(!Pub.empty(fzr)){
    	if(fzr.equals(username)){
    		deptid = user.getDepartment();
    	}
    }

%>

<app:base/>
<title>公告通知-公告信息查询</title>

<script type="text/javascript" charset="UTF-8">
 	var id,json,rowindex,rowValue,flag,exeJson;
  	var controllername= "${pageContext.request.contextPath }/ggtzController.do";
  	
  	
  	//计算本页表格分页数
  	function setPageHeight(){
  		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
  		var pageNum = parseInt(height/pageTableOne,10);
  		$("#DT1").attr("pageNum",pageNum);
  	}
  	
  	function init() {
        //生成json串
        g_bAlertWhenNoResult = false;
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryGgtz", data, DT1);
		g_bAlertWhenNoResult = true;
	}
  	
	//页面默认参数
	$(document).ready(function() { 
		setPageHeight();
        //生成json串
        g_bAlertWhenNoResult = false;
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryGgtz", data, DT1);
		g_bAlertWhenNoResult = true;

	}); 
	
	$(function() {
		var btn = $("#queryBtn");
		btn.click(function() {
	        //生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryGgtz", data, DT1);
		});
	});
	
	// 清除表单
	$(function() {
		$("#query_clear").click(function() {
	       $("#queryForm").clearFormResult();
		});
	});
	 
	var fbzt,type;
	function tr_click(obj, tabListid) {
		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引
		rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
		id = obj.GGID;
		exeJson = JSON.stringify(obj);
		
	//	alert(rowindex);
		json = JSON.stringify(obj);
		json=encodeURI(json); 
		
		// 发不过的公告进制再次发布
		fbzt = obj.SHZT;
		if(fbzt=="0"){//登记
			$("#updateBtn").removeAttr("disabled");
			$("#publishGg").removeAttr("disabled");
			$("#deleteBtn").attr("disabled","disabled");
			$("#removeGg").removeAttr("disabled");
			$("#shtgBtn").attr("disabled","disabled");
			$("#shbtgBtn").attr("disabled","disabled");
		}else if(fbzt=="1"){//已发布
			$("#updateBtn").attr("disabled","disabled");
			$("#publishGg").attr("disabled","disabled");
			$("#deleteBtn").removeAttr("disabled");
			$("#removeGg").attr("disabled","disabled");
			$("#shtgBtn").attr("disabled","disabled");
			$("#shbtgBtn").attr("disabled","disabled");
		}else if(fbzt=="2"){//已作废
			$("#updateBtn").attr("disabled","disabled");
			$("#publishGg").attr("disabled","disabled");
			$("#deleteBtn").attr("disabled","disabled");
			$("#removeGg").attr("disabled","disabled");
			$("#shtgBtn").attr("disabled","disabled");
			$("#shbtgBtn").attr("disabled","disabled");
		}else if(fbzt=="3"){//审核中
			$("#updateBtn").attr("disabled","disabled");
			$("#publishGg").attr("disabled","disabled");
			$("#deleteBtn").attr("disabled","disabled");
			$("#removeGg").attr("disabled","disabled");
			$("#shtgBtn").removeAttr("disabled");
			$("#shbtgBtn").removeAttr("disabled");
		}else {
			$("#updateBtn").removeAttr("disabled");
			$("#publishGg").removeAttr("disabled");
			$("#deleteBtn").removeAttr("disabled");
			$("#removeGg").removeAttr("disabled");
			$("#shtgBtn").removeAttr("disabled");
			$("#shbtgBtn").removeAttr("disabled");
		}
		
	}
	
	function dosh(zt){
		if($("#DT1").getSelectedRowIndex()==-1) {
			xAlert("提示信息",'请选择一条通知公告！','3');
		} else {
			var msg = "";
			if(zt==0){
				msg = "是否确认审核通过此公告！";
			}else{
				msg = "是否确认不审核通过此公告！";
			}
			xConfirm("提示信息",msg);
			$('#ConfirmYesButton').unbind();
		 	$('#ConfirmYesButton').one("click",function(obj) {
					var o = jQuery.parseJSON(exeJson);
					exeJson = JSON.stringify(o);
					var data1 = defaultJson.packSaveJson(exeJson);
					var GGID = $("#DT1").getSelectedRowJsonObj().GGID;
					var success = defaultJson.doUpdateJson(controllername + "?doggsh&zt="+zt+"&ywid="+GGID, data1, null);
					if(success) {
						init();
						//successInfo();
					
					}
			});
		}
		
	}
	
	
	// 点击作废按钮
	function deleteGg() {
		type = "deleteBtn";
		if(fbzt == 0) {
			xInfoMsg("此公告未发布，无法作废");
			return;
		} else {
		 	if($("#DT1").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条要操作的数据！','3');
			} else {
				xConfirm("提示信息","是否确认作废此公告！");
				$('#ConfirmYesButton').unbind();
			 	$('#ConfirmYesButton').one("click",function(obj) {
					if(type=="deleteBtn") {
						var o = jQuery.parseJSON(exeJson);
						delete o.LRSJ;
						exeJson = JSON.stringify(o);
						var data1 = defaultJson.packSaveJson(exeJson);
						var success = defaultJson.doUpdateJson(controllername + "?executeGg&operatorSign=3", data1, null);
						if(success) {
							init();
							successInfo();
						
						}
					}
				});
			}
		}
	}
	
	// 点击删除按钮
	function removeGg() {
		type = "removeBtn";
		if(fbzt == 1 || fbzt == 2) {
			xInfoMsg("此公告已经公布，无法删除");
			return;
		} else {
		 	if($("#DT1").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条要操作的数据！','3');
			} else {
				xConfirm("提示信息","是否确认删除此公告！");
				$('#ConfirmYesButton').unbind();
			 	$('#ConfirmYesButton').one("click",function(obj) {
					if(type=="removeBtn") {
						var o = jQuery.parseJSON(exeJson);
						delete o.LRSJ;
						exeJson = JSON.stringify(o);
						var data1 = defaultJson.packSaveJson(exeJson);
						var success = defaultJson.doUpdateJson(controllername + "?executeGg&operatorSign=4", data1, null);
						if(success) {
							init();
							//successInfo();
						}
					}
				});
			}
		}
	}
	
	function cqsh(){
		if($("#DT1").getSelectedRowIndex()==-1) {
			xAlert("提示信息",'请选择一条要操作的数据！','3');
		} else {
			xConfirm("提示信息","是否确认将此公告呈请部门领导审核！");
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').one("click",function(obj) {
				var o = jQuery.parseJSON(exeJson);
				id = o.GGID;
					$.ajax({
						url : controllername + "?cqsh&ggid="+id+"&sjbh="+o.SJBH+"&ywlx="+o.YWLX ,
						cache : false,
						async : false,
						dataType : 'json',
						success : function(response) {
							var result = eval("(" + response + ")");
							init();
							if(result.sign == 0) {
								successInfo();
							}
						}
					});
			});
		}
		
	}
	
	function publishGg() {
		type = "publishGg";
		if(fbzt == 1 || fbzt == 2) {
			xInfoMsg("此公告已发布或作废，禁止再次发布");
			return;
		} else {
		 	if($("#DT1").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条要操作的数据！','3');
			} else {
				xConfirm("提示信息","是否确认发布此公告！");
				$('#ConfirmYesButton').unbind();
				$('#ConfirmYesButton').one("click",function(obj) {
					var o = jQuery.parseJSON(exeJson);
					id = o.GGID;
					if(type=="publishGg") {
						$.ajax({
							url : controllername + "?publishGg&ggid="+id ,
							cache : false,
							async : false,
							dataType : 'json',
							success : function(response) {
								var result = eval("(" + response + ")");
								init();
								if(result.sign == 0) {
									successInfo();
								}
							}
						});
					}
				});
			}
		}
	}
	
	//按钮绑定事件(新增公告)
	$(function() {
		$("#insertBtn").click(function() {
			flag = true;
			$(window).manhuaDialog({"title":"通知公告>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/xxzx/gg/addGg.jsp","modal":"1"});
		});
	});
	
	function detail(index) {
		var obj = $("#DT1").getRowJsonObjByIndex(index);
		var ggid = obj.GGID;
		readGG(ggid);
		$(window).manhuaDialog({"title":"通知公告","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/portal/newsDetail.jsp?type=1&id="+ggid, "modal":"1"});
	}

	//按钮绑定事件(修改公告)
	$(function() {
		$("#updateBtn").click(function() {
			if(fbzt == 1||fbzt == 2) {
				xInfoMsg("此公告已发布或作废，禁止修改");
				return;
			} else {
				if($("#DT1").getSelectedRowIndex()==-1) {
					xAlert("提示信息",'请选择一条要操作的数据！','3');
				} else {
					flag = false;
					var obj = $("#DT1").getSelectedRowJsonObj();//获得选中行的索引
					id = obj.GGID;
					exeJson = JSON.stringify(obj);
					json = JSON.stringify(obj);
					json=encodeURI(json); 
					$(window).manhuaDialog({"title":"通知公告>修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/xxzx/gg/addGg.jsp?xx="+json+"&&id="+id,"modal":"1"});
				}
			}
		});
	});
	
	//回调函数
	getWinData = function(data) {
	//	data = JSON.stringify(defaultJson.dealResultJson(data));
		index =	$("#DT1").getSelectedRowIndex();
		$("#queryForm").clearFormResult();
		init();
		if(!flag) {
			//修改	
			$("#DT1").setSelect(index);
			exeJson = $("#DT1").getSelectedRowJsonByIndex(index);
			successInfo();
		} else {
			//新增
			//$("#DT1").setSelect(0);
			//exeJson = $("#DT1").getSelectedRowJsonByIndex(0);
			successInfo();
		    index = 0;
		}
	};
	

	//记录阅读 
	function readGG(ggid) {
		$.ajax({
			url : controllername + "?readMainGg&ggid="+ggid ,
//			data : data,
			cache : false,
			async : false,
			dataType : 'json',
			success : function(response) {
				var result = eval("(" + response + ")");
			}
		});
	}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">通知公告
				<span class="pull-right">
				<!-- 	<button id="detail" class="btn"  type="button" >查看</button> -->
					<app:oPerm url="jsp/business/xtbg/xxzx/gg/ggtz.jsp?type=insert">
					<button id="insertBtn" class="btn"  type="button" >新增</button>
					</app:oPerm>
					<app:oPerm url="jsp/business/xtbg/xxzx/gg/ggtz.jsp?type=update">
					<button id="updateBtn" class="btn"  type="button" >修改</button>
					</app:oPerm>
					<app:oPerm url="jsp/business/xtbg/xxzx/gg/ggtz.jsp?type=cqsh">
					<button id="publishGg" class="btn"  type="button" onclick="cqsh()">呈请审核</button>
					</app:oPerm>
					<app:oPerm url="jsp/business/xtbg/xxzx/gg/ggtz.jsp?type=zf">
					<button id="deleteBtn" class="btn"  type="button" onclick="deleteGg()">作废</button>
					</app:oPerm>
					<app:oPerm url="jsp/business/xtbg/xxzx/gg/ggtz.jsp?type=delete">
					<button id="removeGg" class="btn"  type="button" onclick="removeGg()">删除</button>
					</app:oPerm>
					<app:oPerm url="jsp/business/xtbg/xxzx/gg/ggtz.jsp?type=shtg">
					<button id="shtgBtn" class="btn"  type="button" onclick="dosh(0)">审核通过</button>
					</app:oPerm>
					<app:oPerm url="jsp/business/xtbg/xxzx/gg/ggtz.jsp?type=shbtg">
					<button id="shbtgBtn" class="btn"  type="button" onclick="dosh(1)">审核不通过</button>
					</app:oPerm>
				</span>
			</h4>
		<form method="post" id="queryForm"  >
		<table class="B-table">
		<!--可以再此处加入hidden域作为过滤条件 -->
			<TR  style="display:none;">
				<TD class="right-border bottom-border"></TD>
				<TD class="right-border bottom-border">
					<input type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" >
					<% if(!Pub.empty(deptid)){ %>
					 <input type="text" class="span12" kind="text" keep="true" fieldname="LRBM"  value="<%=deptid%>" operation="=" >
					<% }else{%>
					
				    <input type="text" class="span12" kind="text" keep="true" fieldname="LRR"  value="<%=username%>" operation="=" >
				    <% }%>
					
				</TD>
			</TR>
		<!--可以再此处加入hidden域作为过滤条件 -->
			<tr>
				<th width="4%" class="right-border bottom-border">标题</th>
				<td class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" id="GGBT" name="GGBT" fieldname="GGBT" operation="like" logic="and" ></td>
				<th width="4%" class="right-border bottom-border">类别</th>
				<td width="8%" class="right-border bottom-border">
					<select class="span12 2characters" type="text" placeholder="" name="GGLB" id="GGLB" fieldname="GGLB" kind="dic" src="GGLB" operation="=" logic="and" defaultMemo="全部"></select></td>
					
				<th width="6%" class="right-border bottom-border">发布状态</th>
				<td width="8%" class="right-border bottom-border">
					<select class="span12 3characters" placeholder="" name="SHZT" id="SHZT" fieldname="SHZT" kind="dic" src="GGZT" operation="=" logic="and" defaultMemo="全部"></select></td>
				<!-- 
				<th width="1%" class="right-border bottom-border">发布人</th>
				<td width="10%" class="bottom-border">
					<input class="span12" placeholder="" id="FBR" name="FBR" fieldname="FBR" operation="like"  logic="and" /></td> -->
				<th width="6%" class="right-border bottom-border">发布时间</th>
				<td width="8%" class="right-border bottom-border">
					<input class="span12 date" type="date" name="FBSJ" fieldname="FBSJ" id="FBSJ" operation=">=" logic="and" fieldtype="date" fieldformat="YYYY-MM-DD"/></td>
				<th width="2%" class="right-border bottom-border">至</th>
				<td width="8%" class="right-border bottom-border">
					<input class="span12 date" type="date" name="FBSJ" fieldname="FBSJ" id="FBSJ" operation="<=" logic="and" fieldtype="date" fieldformat="YYYY-MM-DD"/></td>
			
				<td  class="text-left bottom-border text-right">
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
					<th fieldname="GGLB" tdalign="center">&nbsp;类别&nbsp;</th>
					<th fieldname="GGBT" haslink="true" linkfunction="detail" maxlength="35">&nbsp;标题&nbsp;</th>
					<th fieldname="FBFWMC" maxlength="13">&nbsp;发布范围&nbsp;</th>
					<th fieldname="LRBM" >&nbsp;发布部门&nbsp;</th>
					<th fieldname="FBR" >&nbsp;发布人&nbsp;</th>
					<th fieldname="FBSJ" tdalign="center">&nbsp;发布时间&nbsp;</th>
					<!-- <th fieldname="YDCS" >&nbsp;阅读次数&nbsp;</th> -->
					<th fieldname="SHZT">发布状态</th>
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
		<input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ" id="txtFilter">
		<input type="hidden" name="resultXML" id="resultXML">
		<input type="hidden" name="queryResult" id="queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">
	</FORM>
</div>

</body>
</html>