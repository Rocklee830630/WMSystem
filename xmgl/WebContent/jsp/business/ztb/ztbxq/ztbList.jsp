<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<title></title>
<% 
	String num=request.getParameter("num");
	String xx=request.getParameter("xx");
    String zblx=request.getParameter("zblx");
	String cxlx = request.getParameter("cxlx")==null?"1":request.getParameter("cxlx");//定义一个查询类型，用于区分查询需求表(1)还是数据表(2)
%>
<script type="text/javascript" charset="utf-8">
  var json2,json;
  var rowindex,rowValue,text,aa;
  var jhsjid,lbjid,bdmc;
  var ztbsjid = '';
  var p_zbxqid='<%=xx%>';
  var controllername= "${pageContext.request.contextPath }/ZhaotoubiaoController.do";
	$(function() {
		var xz=$("#ztbxz");
		xz.click(function() {
			//$("#DT1").setSelect(0);
			$(window).manhuaDialog({"title":"招投标管理>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/ztbxz.jsp","modal":"1"});
		});
		//招投标管理维护页面
		var wh=$("#wh");
		wh.click(function() {
			if($("#DT1").getSelectedRowIndex()==-1) {
				requireFormMsg("请选择一条要操作的数据！");
			} else {
				//$("#DT1").setSelect(0);
				$("#resultXML").val($("#DT1").getSelectedRow());
				$(window).manhuaDialog({"title":"招投标管理>维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/ztbgl.jsp?ztbsjid="+ztbsjid,"modal":"1"});
			}
		});
	});
	$(function() {
		init();
	});
	//获取行数据json串
	function tr_click(obj,tabListid)
	{
	    json = $("#DT1").getSelectedRowText();
	    ztbsjid = obj.GC_ZTB_SJ_ID;
	    $("#GC_ZTB_SJ_ID").val(obj.GC_ZTB_SJ_ID);
		//json串加密
		json=encodeURI(json);
		//
		if(obj.EVENTSJBH != 0) {
			$("#sp_btn").attr("disabled","disabled");
			$("#wh").attr("disabled","true");
		} else {
			$("#sp_btn").removeAttr("disabled");
			$("#wh").removeAttr("disabled");
		}
		var rowValue = $("#"+tabListid).getSelectedRow();//获得选中行的json 字符串
		var tempJson = convertJson.string2json1(rowValue);//字符串转JSON对象
		if(tempJson.XQZT=="1"){
			$("#tjzb_btn").removeAttr("disabled");
			$("#wh").removeAttr("disabled");
		}else{
			$("#tjzb_btn").attr("disabled","true");
			$("#wh").attr("disabled","true");
		}
		
		//暂时放开 modified by hongpeng.dong at 2014.10.23
		$("#wh").removeAttr("disabled");
		
	}
	//页面默认参数
	function init(){
		$("#zbxqid").val(p_zbxqid);
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryZhaotoubiao",data,DT1,null,true);
	}
	//子页面调用添加行
	function tianjiahang(data)
	{
		var subresultmsgobj = defaultJson.dealResultJson(data);
		$("#DT1").insertResult(JSON.stringify(subresultmsgobj),DT1,1);
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(0);
		json = $("#DT1").getSelectedRowText();
		ztbsjid = $("#DT1").getSelectedRowJsonObj().GC_ZTB_SJ_ID;
		//json串加密
	    json=encodeURI(json);
	    xSuccessMsg("操作成功","");
	}
	//子页面调用修改行
	function xiugaihang(data)
	{
		var index =	$("#DT1").getSelectedRowIndex();
		var subresultmsgobj = defaultJson.dealResultJson(data);
		var comprisesJson = $("#DT1").comprisesJson(subresultmsgobj,index);
		$("#DT1").updateResult(JSON.stringify(comprisesJson),DT1,index);
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(index);
		json = $("#DT1").getSelectedRowText();
			//json串加密
		json=encodeURI(json);
		xSuccessMsg("操作成功","");
	}
	//--------------------------------------
	//-删除方法
	//--------------------------------------
	function doDelZtb(data,winObj){
		var data1 = defaultJson.packSaveJson(data);
		defaultJson.doDeleteJson(controllername+"?deleteZtbsj",data1,DT1,null);
		winObj.manhuaDialog.close();
	}
	//列表添加信息卡图标
	function doRandering(obj){
		var showHtml = "";
		showHtml = "<a href='javascript:void(0)' onclick='openInfoCard()' title='招投标信息卡'><i class='icon-file showXmxxkInfo'></i></a>";
		return showHtml;
	}
	//-------------------------------
	//-打开信息卡页面
	//-------------------------------
	function openInfoCard(){
		var index = $(event.target).closest("tr").index();
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(index);
		var json = convertJson.string2json1($("#DT1").getSelectedRow());
		var id = json.GC_ZTB_SJ_ID;
		var zblx = json.ZBLX;
		$(window).manhuaDialog({"title":"招投标管理>查看招标信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/ztbgl_xx.jsp?xx="+id+"&zblx="+zblx+"&cxlx=2","modal":"1"});
	}
</script>
</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<p></p>
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						招投标信息
					</h4>
					<form method="post" id="queryForm" style="display:none;">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD>
									<INPUT id="num" type="text" class="span12" kind="text"
										fieldname="rownum" value="1000" operation="<=" keep="true" />
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
							<tr>
								<th width="5%" class="right-border bottom-border text-right">
									招投标名称
								</th>
								<td width="20%" class="right-border bottom-border">
									<input class="span12" type="text" id="zbxqid" name="XQID"
										fieldname="X.GC_ZTB_XQ_ID" operation="=" >
								</td>
								<td width="30%" class=" bottom-border text-right">
									<button id="chaxun" class="btn btn-link" type="button">
										<i class="icon-search"></i>查询
									</button>
									<button id="query_clear" class="btn btn-link" type="button">
										<i class="icon-trash"></i>清空
									</button>
								</td>
							</tr>
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
					<table width="100%" class="table-hover table-activeTd B-table"
						id="DT1" type="single" editable="0" pageNum="7" printFileName="招投标管理">
						<thead>
							<tr>
								<th name="XH" id="_XH" tdalign="">
									&nbsp;#&nbsp;
								</th>
								<th fieldname="SFYX" maxlength="15" tdalign="center" CustomFunction="doRandering" noprint="true">
									&nbsp;&nbsp;
								</th>
								<th fieldname="XQZT" tdalign="center">
									&nbsp;状态&nbsp;
								</th>
								<th fieldname="ZTBMC" tdalign="" maxlength="15">
									&nbsp;招投标名称&nbsp;
								</th>
								<!--
								<th fieldname="ZBBH" tdalign="" maxlength="15">
									&nbsp;招标编号&nbsp;
								</th>
								-->
								<th fieldname="ZBFS" tdalign="center" maxlength="15">
									&nbsp;招标方式&nbsp;
								</th>
								<th fieldname="KBRQ" tdalign="center" maxlength="15">
									&nbsp;开标日期&nbsp;
								</th>
								<th fieldname="ZZBJ" tdalign="right" maxlength="15">
									&nbsp;总中标价(元)
								</th>
								<th fieldname="DSFJGID" tdalign="left" maxlength="15">
									&nbsp;中标单位&nbsp;
								</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					</div>
					<form method="post" id="ztbForm" style="display: none">
						<table class="B-table" width="100%">
							<TR style="display: none;">
								<!-- <TR> -->
								<TD>di<INPUT class="span12" type="text" name="GC_ZTB_SJ_ID"
										fieldname="GC_ZTB_SJ_ID" id="GC_ZTB_SJ_ID" />
									<INPUT class="span12" type="text" id="XQZT" name="XQZT"
										fieldname="XQZT" value="2" keep="true" />
								<br><br></TD>
							</TR>
						</table>
					</form>
				</div>
			</div>
		</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id="queryResult" />
				<input type="hidden" name="txtFilter" order="desc"
					fieldname="a.LRSJ" id="txtFilter">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
			
		</div>
	</body>
</html>