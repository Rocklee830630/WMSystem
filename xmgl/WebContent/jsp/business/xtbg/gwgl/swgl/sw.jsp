<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<app:base/>
<title>公告通知-公告信息查询</title>

<script type="text/javascript" charset="UTF-8">
 	var id,json,rowindex,rowValue,flag,exeJson,sjbh;
  	var controllername= "${pageContext.request.contextPath }/swglController.do";
  	
  	
  	//计算本页表格分页数
  	function setPageHeight(){
  		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
  		var pageNum = parseInt(height/pageTableOne,10);
  		$("#DT1").attr("pageNum",pageNum);
  	}
  	
  	function init() {
        //生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?querySw", data, DT1);
	}
  	
	//页面默认参数
	$(document).ready(function() { 
		setPageHeight();
        //生成json串
        g_bAlertWhenNoResult = false;
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?querySw", data, DT1);
        g_bAlertWhenNoResult = true;
	}); 
	
	$(function() {
		var btn = $("#queryBtn");
		btn.click(function() {
	        //生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?querySw", data, DT1);
		});
	});
	
	// 清除表单
	$(function() {
		$("#query_clear").click(function() {
	       $("#queryForm").clearFormResult();
		});
	});
	
	function tr_click(obj, tabListid) {
		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引
		rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
		id = obj.SWID;
		sjbh = obj.SJBH;
		exeJson = JSON.stringify(obj);
	//	alert(rowindex);
		json = JSON.stringify(obj);
		json=encodeURI(json); 

		$("#deleteBtn").attr("disabled","disabled");
		if(obj.EVENTSJBH != 0) {
			$("#updateBtn").attr("disabled",true);
			$("#cqspBtn").attr("disabled","disabled");
		} else {
			$("#deleteBtn").removeAttr("disabled");
			$("#updateBtn").removeAttr("disabled");
			$("#cqspBtn").removeAttr("disabled");
		}
		
		// 流程重启
		if(obj.EVENTSJBH != 2) {
			$("#reStart").attr("disabled",true);
		} else {
			$("#reStart").removeAttr("disabled");
		}
	}
	
	// 点击删除按钮
	$(function() {
		var btn = $("#deleteBtn");
		btn.click(function() {
			var rowindex = $("#DT1").getSelectedRowIndex();
		 	if(rowindex==-1) {
				requireFormMsg("请选择一条要操作的数据！");
			} else {
 		 		xConfirm("提示信息","是否确认删除收文信息");
				$('#ConfirmYesButton').unbind();
			 	$('#ConfirmYesButton').one("click",function(obj) {
					$.ajax({
						url : controllername + "?deleteSw&swid="+id ,
						cache : false,
						async : false,
						dataType : 'json',
						success : function(response) {
							if(response == "1") {
								$("#DT1").removeResult(rowindex);
							}
						}
					});
				});
			} 
		});
	});
	
	// 点击发布按钮
	$(function() {
		var btn = $("#publishGg");
		btn.click(function() {
		 	if($("#DT1").getSelectedRowIndex()==-1) {
				requireFormMsg("请选择一条要操作的数据！");
			} else {
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
	});
	
	//按钮绑定事件(新增公告)
	$(function() {
		$("#insertBtn").click(function() {
			flag = true;
			$(window).manhuaDialog({"title":"收文管理>登记收文","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/gwgl/swgl/addSw.jsp","modal":"1"});
		});
	});

	//按钮绑定事件(新增公告)
	$(function() {
		$("#insertBtn1").click(function() {
			flag = true;
			$(window).manhuaDialog({"title":"收文管理>登记","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/gwgl/swgl/addSw1.jsp","modal":"1"});
		});
	});

	//按钮绑定事件(修改收文)
	$(function() {
		$("#updateBtn").click(function() {
			if($("#DT1").getSelectedRowIndex()==-1) {
				requireFormMsg("请选择一条要操作的数据！");
			} else {
				flag = false;
				var obj = $("#DT1").getSelectedRowJsonObj();//获得选中行的索引
				id = obj.SWID;
				sjbh=obj.SJBH;
				exeJson = JSON.stringify(obj);
				json = JSON.stringify(obj);
				json=encodeURI(json); 
				$(window).manhuaDialog({"title":"收文管理>修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/gwgl/swgl/addSw.jsp?xx="+json+"&&id="+id+"&sjbh="+sjbh,"modal":"1"});
			}
		});
	});
	
	//回调函数
	getWinData = function(data){
		index =	$("#DT1").getSelectedRowIndex();
		if(flag==false)
		{
			//修改	
			$("#DT1").setSelect(index);
			successInfo();
		}
		else
		{
			//新增
			init();
			$("#DT1").setSelect(0);
			successInfo();
		    index = 0;
		    
		}
		$("#reStart").attr("disabled",true);
	};
	//呈请审批
	$(function() {
		$("#cqspBtn").click(function() {
			//获取ywlx,sjbh
			if($("#DT1").getSelectedRowIndex()==-1) {
				requireFormMsg("请选择一条要操作的数据！");
			} else {
				var rowjson =	$("#DT1").getSelectedRow();
				var sjbh = JSON.parse(rowjson).SJBH;
				var ywlx = JSON.parse(rowjson).YWLX;
				var hasroles
			//	alert(rowjson + " | " + sjbh + " | " + ywlx);
				hasroles =   hasSprole("381",ywlx);
				
				if(hasroles){
			   		creatSP("381",ywlx,sjbh,"");
				}else{
					requireFormMsg ("无审批权限!");
				}
			}
		});
		
		//历史收文查询
		$("#lsQuery").click(function() {
			$(window).manhuaDialog({"title":"收文历史查询","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/gwgl/swgl/lsQuery.jsp","modal":"1"});
		});
	});
	
	function reStart() {
		//获取ywlx,sjbh
		if($("#DT1").getSelectedRowIndex()==-1) {
			requireFormMsg("请选择一条要操作的数据！");
		} else {
			var rowjson =	$("#DT1").getSelectedRow();
			var sjbh = JSON.parse(rowjson).SJBH;
			var ywlx = JSON.parse(rowjson).YWLX;
			var hasroles = hasSprole("381",ywlx);
			
			if(hasroles) {
		   		creatSP("381",ywlx,sjbh,"isReStart");
			} else {
				requireFormMsg ("无审批权限!");
			}
		}
	}
	
	function creatSP(templateid,ywlx,sjbh,isReStart) {
		if(sjbh==null ||sjbh == undefined || sjbh =="")
			return;
	    var wsActionURL = "/xmgl/PubWS.do?getXMLPrintAction|templateid="+templateid+"|isSp=1|ywlx="+ywlx+"|isrefresh=1|sjbh="+sjbh+"|rowid="+Math.random()+".mht";
	    wsActionURL = "/xmgl/jsp/framework/common/gwglLink/swPrint.jsp?param="+wsActionURL+"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid="+templateid+"&isSp=1&operationoid=2486&processtype=4&isReStart="+isReStart;
	    $(window).manhuaDialog({"title":"流程申请>收文稿纸","type":"text","content":wsActionURL,"modal":"1"});
	}
	
	function getMessage(msg)
	{
		xAlert("运行结果",msg);
		g_bAlertWhenNoResult=false;
		 //生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?querySw", data, DT1);
		g_bAlertWhenNoResult=true;
	}
	
	

	function rowView_(index){

	    var obj=$("#DT1").getRowJsonObjByIndex(index);//获取行对象
	    var sjbh = obj.SJBH;
	    var eventSjbh = obj.EVENTSJBH;
	    var ywlx = obj.YWLX;
	    if(eventSjbh == "0") {
	    	$(window).manhuaDialog({"title":"收文管理>查看","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/gwgl/swgl/detailSw.jsp?sjbh="+sjbh+"&type=1&eventSjbh="+eventSjbh,"modal":"1"});
		} else {
			var wsActionURL = "/xmgl/PubWS.do?getXMLPrintAction|templateid=381|isEdit=0|ywlx="+ywlx+"|sjbh="+sjbh+"|rowid="+Math.random()+".mht";
			wsActionURL = "/xmgl/jsp/business/lcgl/lccx/processDetail.jsp?param="+wsActionURL+"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid=381&isEdit=0"+"&isview=1";
			var s = "/xmgl/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&isview=1";     
			$(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});
		}
	}

	
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">收文管理
				<span class="pull-right">
					<button id="insertBtn" class="btn"  type="button" >登记</button>
				<!-- 	<button id="insertBtn1" class="btn"  type="button" >登记1</button> -->
					<button id="updateBtn" class="btn"  type="button" >修改</button>
					<button id="deleteBtn" class="btn"  type="button" >删除</button>
					<button id="cqspBtn" class="btn"  type="button" >呈请审批</button>
					<button id="reStart" class="btn" type="button" onclick="reStart()">重启收文</button>
					<button id="lsQuery" class="btn" type="button">历史查询</button>
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
				<th width="5%" class="right-border bottom-border">收文编号</th>
				<td width="10%" class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" id="SWBH" name="SWBH" fieldname="SWBH" operation="like" logic="and" ></td>
				<th width="5%" class="right-border bottom-border">文件标题</th>
				<td width="10%" class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" name="WJBT" id="WJBT" fieldname="WJBT" kind="dic" src="GGLB"  operation="like" logic="and"></td>
					
			<!-- 	<th width="5%" class="right-border bottom-border">来文单位</th>
				<td width="10%" class="right-border bottom-border">
					<input class="span12" type="text" name="LWDW" fieldname="LWDW" id="LWDW" operation="like" logic="and" /></td> -->
				
				<th width="2%" class="right-border bottom-border">收文状态</th>
				<td width="7%" class="bottom-border">
					<!-- <input class="span12" type="text" placeholder="" id="WZ" name="WZ" fieldname="WZ" operation="like"  logic="and" /></td> -->
			<select class="span12" type="text" name="t.sjzt" fieldname="t.sjzt" id="sjzt" defaultMemo="全部" kind="dic" src="T#fs_dic_tree:DIC_CODE:DIC_VALUE:ID='9000000000282' or ID='9000000000283' or ID='9000000000284' " operation="=" logic="and"></select></td>
			
				<th width="2%" class="right-border bottom-border">缓急</th>
				<td width="6%" class="right-border bottom-border">
					<select class="span12" type="text" name="HJ" fieldname="HJ" id="HJ" defaultMemo="全部" kind="dic" src="HJ1000000000302" operation="=" logic="and"></select></td>
			
				<td width="13%"  class="text-left bottom-border text-right" >
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
					<th fieldname="EVENTSJBH" >&nbsp;当前状态&nbsp;</th>
					<th fieldname="WZ" >&nbsp;文种&nbsp;</th>
					<th fieldname="SWBH" tdalign="center">&nbsp;收文编号&nbsp;</th>
					<th fieldname="WJBT" hasLink="true" linkFunction="rowView_" maxlength="13">&nbsp;文件标题&nbsp;</th>
					<th fieldname="LWDW">&nbsp;来文单位&nbsp;</th>
					<th fieldname="HJ" >&nbsp;缓急&nbsp;</th>
					<th fieldname="WH" >&nbsp;文号&nbsp;</th>
					<th fieldname="SWRQ" tdalign="center">&nbsp;收文日期&nbsp;</th>
					<!-- <th fieldname="YDCS" >&nbsp;阅读次数&nbsp;</th> -->
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