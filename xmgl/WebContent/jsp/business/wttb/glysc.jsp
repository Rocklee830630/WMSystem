<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app"%>
<app:base/>
<style type="text/css">
	.myUl li p{
		font-size: 15px;
	}
	blockquote{
		margin-bottom:0px;
	}
</style>
<script src="${pageContext.request.contextPath }/js/common/FixTable.js"></script> 
<script type="text/javascript" charset="UTF-8">
	var controllername= "${pageContext.request.contextPath }/wttb/WttbBmgkController.do"; 
	var controllername2= "${pageContext.request.contextPath }/wttb/wttbController.do";
	var controllername3= "${pageContext.request.contextPath }/wttb/WttbXmscController.do"; 
	var historyListHeight = 0;	//这个变量用于计算历史列表最大允许的高度
	var getHeight=g_iHeight;	//获取弹出页高度
	$(function(){
		setPageHeight();
		doInit();
		//按钮绑定事件（导出）
		$("#btnDel").click(function(){
			if($("#tabList").getSelectedRowIndex()==-1){
				requireSelectedOneRow();
				return;
			}else{
				var obj = $("#tabList").getSelectedRow();
				var data1 = defaultJson.packSaveJson(obj);
				xConfirm("提示信息","是否确认删除！");
				$('#ConfirmYesButton').unbind();
				$('#ConfirmYesButton').one("click",function(){
					defaultJson.doDeleteJson(controllername2+"?deleteWttb",data1,tabList,null); 
				});
			}
		});
		$("#btnQuery").click(function() {
			queryInfoTable();
		});
		//按钮绑定事件（清空查询条件）
		$("#btnClear").click(function() {
			$("#queryForm").clearFormResult();
		});
	});
	//页面初始化
	function doInit(){
		g_bAlertWhenNoResult = false;
		queryInfoTable();
	}
	//------------------------------------
	//查询问题表格
	//------------------------------------
	function queryInfoTable(){
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,tabList);
		defaultJson.doQueryJsonList(controllername+"?queryInfoTable",data,tabList);
	}
	//------------------------------------
	//问题信息卡
	//------------------------------------
	function showInfoCard(obj){
		var showHtml = "";
		var id = obj.WTTB_INFO_ID;
		showHtml = "<a href='javascript:void(0);' title='问题信息卡' onclick=\"openInfoCard('"+id+"')\"><i class='icon-file showInfoCard' wtid='"+obj.WTTB_INFO_ID+"'></i></a>";
		return showHtml;
	}
	//------------------------------------
	//按问题状态生成颜色
	//------------------------------------
	function doRandering(obj){
		var data = JSON.stringify(obj);
		var data1 = defaultJson.packSaveJson(data);
		var showHtml = "";
		$.ajax({
			url:controllername2 + "?getColor",
			data:data1,
			dataType:"json",
			async:false,
			success:function(result){
				var res = result.msg;
				if(obj.SJZT=="3" || obj.SJZT=="6"){
					showHtml = '<span class="label" style="width:50px;">完&nbsp;结</span>';
				}else{
					if(res=="none"){
						showHtml = '<span class="label" style="width:50px;">正&nbsp;常</span>';
					}else{
						tempJson = convertJson.string2json1(res);
						showHtml = '<span class="label '+tempJson.response.data[0].CLASS+'"  style="width:50px;">'+tempJson.response.data[0].TITLE+'</span>';
					}
				}
			}
		});
		return showHtml;
	}
	//------------------------------------
	//打开问题提报信息卡
	//------------------------------------
	function openInfoCard(n){
		var index = $(event.target).closest("tr").index();
		var tabId = $(event.target).closest("table").attr("id");
    	$("#"+tabId).cancleSelected();
    	$("#"+tabId).setSelect(index);
		if($("#"+tabId).getSelectedRowIndex()==-1){
			requireSelectedOneRow();
			return;
		}else{
			$(window).manhuaDialog({"title":"问题提报信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/wtCard.jsp?infoID="+n,"modal":"1"});
    	}
	}
	//计算本页表格分页数
	function setPageHeight() {
		var height = getHeight-pageTopHeight-pageTitle-getTableTh(5)-pageNumHeight;
		var pageNum = parseInt(Math.abs(height)/pageTableOne,10);
		$("#tabList").attr("pageNum", pageNum);
	}
	//计算本页表格分页数--问题追踪
	function setPageHeightWtzz(id) {
		var height = getHeight-pageTopHeight-pageTitle-getTableTh(1)-$("#suggest").height()-pageNumHeight;
		var pageNum = parseInt(Math.abs(height)/pageTableOne,10);
		$("#"+id).attr("pageNum", pageNum);
	}
</script>
</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<p></p>
			<div class="row-fluid">
				<div class="span12">
					<div class="row-fluid">
						<div class="B-small-from-table-autoConcise" style="border: 0px;">
							<h4 class="title">
								问题概况&nbsp;&nbsp;
								<span class="pull-right">
									<button id="btnDel" class="btn" type="button">
										删除
									</button> </span>
							</h4>
							<form method="post"
									action="${pageContext.request.contextPath }/insertdemo.do"
									id="queryForm">
									<table class="B-table" width="100%">
										<!--可以再此处加入hidden域作为过滤条件 -->
										<TR style="display: none;">
											<TD class="right-border bottom-border"></TD>
											<TD class="right-border bottom-border">
												<INPUT type="text" class="span12" kind="text"
													fieldname="rownum" value="1000" operation="<=">
											</TD>
											<td>
												人员角色隐藏条件-用于控制按钮，不传入后台
											</td>
											<TD class="right-border bottom-border">
												<INPUT type="text" class="span12" kind="text" id="ryjs_cond">
											</TD>
										</TR>
										<!--可以再此处加入hidden域作为过滤条件 -->
										<tr>
											<th class="right-border bottom-border" style="display:none;">
												年度
											</th>
											<td class="right-border bottom-border" style="display:none;">
												<select class="span12 year" name="LRSJ" fieldname="to_char(I.LRSJ,'YYYY')" operation="="
													kind="dic" logic="and" id="q_nd" keep="true"
													src="T#(select distinct ND from GC_JH_SJ S where SFYX='1' union all select distinct to_char(I.LRSJ,'yyyy') as ND from GC_JH_SJ S,WTTB_INFO I where S.ND!=to_char(I.LRSJ,'yyyy')): distinct ND as NDCODE: ND:1=1 ORDER BY NDCODE asc">
												</select>
											</td>
											<th class="right-border bottom-border">
												问题状态
											</th>
											<td class="right-border bottom-border">
												<select class="span12 4characters" name="LRSJ" fieldname="SJZT" operation="="
													kind="dic" logic="and" src="WTZT" defaultMemo="全部">
												</select>
											</td>
											<th class="right-border bottom-border">
												类别
											</th>
											<td class="right-border bottom-border">
												<select class="span12 5characters" name=""WTLX"" fieldname="WTLX" operation="="
													kind="dic" logic="and" src="WTLX" defaultMemo="全部">
												</select>
											</td>
											<th class="right-border bottom-border">
												问题标题
											</th>
											<td width="17%" class="right-border bottom-border">
												<input class="span12" type="text" placeholder="" name="WTBT"
													fieldname="I.WTBT" operation="like" logic="and">
											</td>
											<th class="right-border bottom-border">
												问题性质
											</th>
											<td class="bottom-border">
												<select class="span12 3characters" name="WTXZ" fieldname="I.WTXZ"
													kind="dic" src="WTXZ" operation="=" logic="and" defaultMemo="全部">
												</select>
											</td>
											<th class="right-border bottom-border">
												超期情况
											</th>
											<td>
												<select class="span12 3characters" name="CQBZ" fieldname="I.CQBZ"
													kind="dic" src="CQBZ" operation="=" logic="and" defaultMemo="全部">
												</select>
											</td>
											<td class="right-border bottom-border text-right" rowspan=3>
												<button id="btnQuery" class="btn btn-link" type="button">
													<i class="icon-search"></i>查询
												</button>
												<button id="btnClear" class="btn btn-link" type="button">
													<i class="icon-trash"></i>清空
												</button>
											</td>
										</tr>
										<tr>
											<th class="right-border bottom-border">
												项目名称
											</th>
											<td class="bottom-border" colspan=3>
												<input class="span12" type="text" placeholder="" name="QXMMC"
													fieldname="XMMC" operation="like" id="QXMMC"
													autocomplete="off" tablePrefix="S">
											</td>
											<th class="right-border bottom-border">
												标段名称
											</th>
											<td class="bottom-border">
												<input class="span12" type="text" placeholder="" name="QBDMC"
													fieldname="BDMC" operation="like">
											</td>
											<th class="right-border bottom-border">
												发起部门
											</th>
											<td class="bottom-border">
												<select class="span12 6characters" name="LRBM" fieldname="I.LRBM"
													kind="dic" src="T#VIEW_YW_ORG_DEPT D:ROW_ID:DEPT_NAME:D.ACTIVE_FLAG='1' " operation="=" logic="and" defaultMemo="全部">
												</select>
											</td>
											
											<th class="right-border bottom-border">
												发起人
											</th>
											<td class="bottom-border">
												<input class="span12 4characters" name="P.NAME" fieldname="P.NAME" type="text"
													operation="like" logic="and">
											</td>
										</tr>
										<tr>
											<th class="right-border bottom-border">
												主办部门
											</th>
											<td class="bottom-border">
												<select class="span12 6characters" name="JSBM" fieldname="L.JSBM"
													kind="dic" src="T#VIEW_YW_ORG_DEPT D:ROW_ID:DEPT_NAME:D.ACTIVE_FLAG='1' " operation="=" logic="and" defaultMemo="全部">
												</select>
											</td>
											
											<th class="right-border bottom-border">
												主责中心领导
											</th>
											<td class="bottom-border">
												<select class="span12 3characters" name="JSR" fieldname="L.JSR"
													kind="dic" src="T#VIEW_ZR D:ACCOUNT:NAME:1=1 order by to_number(SORT) " operation="=" logic="and" defaultMemo="全部">
												</select>
											</td>
											<td class="bottom-border" colspan=6>
												
											</td>
										</tr>
									</table>
								</form>
							<div style="height: 5px;">
							</div>
							<div class="overFlowX">
								<table width="100%" class="table-hover table-activeTd B-table"
									id="tabList" type="single" editable="0">
									<thead>
											<tr>
												<th name="XH" id="_XH" rowspan="2" colindex=1>
													&nbsp;#&nbsp;
												</th>
												<th fieldname="WTLX" maxlength="15"
													tdalign="center" 
													CustomFunction="showInfoCard">
													&nbsp;&nbsp;
												</th>
												<th fieldname="WTLX" tdalign="center" sort="true">
													&nbsp;类别&nbsp;
												</th>
												<th fieldname="WTBT"
													maxlength="10" sort="true">
													&nbsp;问题标题&nbsp;
												</th>
												<th fieldname="SJZT"  maxlength="10" tdalign="center" sort="true">
													&nbsp;问题状态&nbsp;
												</th>
												<th fieldname="SJSJ" tdalign="center"  sort="true">
													&nbsp;解决时间&nbsp;
												</th>
												<th fieldname="LRBM" sort="true">
													&nbsp;发起部门&nbsp;
												</th>
												<th fieldname="JSBM" sort="true">
													&nbsp;主办部门&nbsp;
												</th>
												<th fieldname="JSR" sort="true">
													&nbsp;主办人&nbsp;
												</th>
												<th fieldname="BLQK" tdalign="center" CustomFunction="doRandering">
													&nbsp;办理情况&nbsp;
												</th>
												<th fieldname="XMMC" maxlength="15" sort="true">
													&nbsp;项目名称&nbsp;
												</th>
												<th fieldname="BDMC"  maxlength="15" sort="true">
													&nbsp;标段名称&nbsp;
												</th>
												<th fieldname="LRR" sort="true">
													&nbsp;发起人&nbsp;
												</th>
												<th fieldname="LRSJ" sort="true">
													&nbsp;提出时间&nbsp;
												</th>
												<th fieldname="CBCS" sort="true" tdalign="right">
													&nbsp;催办次数&nbsp;
												</th>
											</tr>
										</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="ywid" id="ywid">
				<input type="hidden" name="txtFilter" order="desc" fieldname="SJZT,LRSJ"
					id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">

			</FORM>
		</div>
	</body>
</html>