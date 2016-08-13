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
%>
<script type="text/javascript" charset="utf-8">
  var json2,json;
  var rowindex,rowValue,text,aa;
  var jhsjid,lbjid,bdmc;
  var ztbsjid = '';
  var controllername= "${pageContext.request.contextPath }/ZhaotoubiaoController.do";
  
  
	//计算本页表格分页数
	function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
  
	$(function() {
		setPageHeight();
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
		//清空查询条件
		$("#query_clear").click(function(){
			$("#queryForm").clearFormResult();
			//getNd();
			//init();
		});
		$("#sp_btn").click(function(){
			var index1 =	$("#DT1").getSelectedRowIndex();
			if(index1<0){
				requireSelectedOneRow();
				return;
			}
			var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
			var ywlx = $("#DT1").getSelectedRowJsonObj().YWLX;
			var condition = "";
			createSPconf(sjbh,ywlx,condition);
			/* var url =  '${pageContext.request.contextPath}/jsp/business/lcgl/lcsq/lcsq.jsp?sjbh='+sjbh;
			$(window).manhuaDialog({"title":"流程申请","type":"text","content":url,"modal":"2"});   */
		});
		//按钮绑定事件（导出）
		$("#btnExp").click(function() {
			if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
				printTabList("DT1");
			}
		});
	});
	$(function() {
		init();
		//按钮绑定事件（查询）
		$("#chaxun").click(function() {
			//生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryZhaotoubiao",data,DT1,null,false);
		});
		//提交到合同
		var shanchu=$("#tjzb_btn");
		shanchu.click(function()
		{
			var index =	$("#DT1").getSelectedRowIndex();
				//生成json串
		 	var data = Form2Json.formToJSON(ztbForm);
			var data1 = defaultJson.packSaveJson(data);
			if(index==-1){
				requireSelectedOneRow();
				return;
			}else{
				var rowObj = $("#DT1").getSelectedRowJsonObj();
				var zongZbj=rowObj.ZZBJ;
				var num = getWhfxmCount(rowObj.GC_ZTB_SJ_ID);
				if(zongZbj==""){
					xAlert("警告","请录入总中标价!","3");
					return;
				}
				/* if(Number(num)!=0){
					xAlert("警告","存在未划分金额项目，请先划分金额!","3");
					return;
				}
				var numZZBJ = getxmZJECount(rowObj.GC_ZTB_SJ_ID);
				if(numZZBJ!=''){
					if(zongZbj!=numZZBJ){
						xAlert("警告","子中标价不等于总中标价!","3");
						return;
					}	
				} */
				if(rowObj.KBRQ=="" ||rowObj.KBRQ==undefined){
					//由于页面取消了“开标日期”必录项校验，所以在“提交到合同”的时候进行校验
					xAlert("警告","请录入开标日期!","3");
					return;
				}
				if(rowObj.DSFJGID=="" ||rowObj.DSFJGID==undefined){
					//由于页面取消了“中部单位”必录项校验，所以在“提交到合同”的时候进行校验
					xAlert("警告","请录入中标单位!","3");
					return;
				}
				xConfirm("提示信息","是否确认提交！");
				$('#ConfirmYesButton').unbind();
				$('#ConfirmYesButton').one("click",function(){ 
					defaultJson.doUpdateJson(controllername+"?updateZhaotoubiaoZT",data1,DT1,null);
					$("#tjzb_btn").attr("disabled","true");
					$("#wh").attr("disabled","true");
				});
			}
		}
		);
	});
	//查询项目金额是否等于总中标价
	function getxmZJECount(sjid){
		var str = "";
		$.ajax({
			url:controllername + "?getxmZJECount&id="+sjid,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				str = result.msg;
			}
		});
		return str;
	}
	//----------------------------
	//-获取未划分金额项目的数量
	//----------------------------
	function getWhfxmCount(sjid){
		var str = "";
		$.ajax({
			url:controllername + "?getWhfxmCount&id="+sjid,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				str = result.msg;
			}
		});
		return str;
	}
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
		setDefaultNd();
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
	//-------------------------------
	//-打开合同信息页面
	//-------------------------------
	function rowViewHT(index){
		var obj = $("#DT1").getSelectedRowJsonByIndex(index);
		var id = convertJson.string2json1(obj).HTID;
		$(window).manhuaDialog({"title":"招投标管理>合同履行信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/ht-view.jsp?type=detail&htid="+id,"modal":"1"});
	}
	//-------------------------------
	//-用于显示合同状态
	//-------------------------------
	function showHtzt(obj){
		var xqzt=obj.HTZT;
		if(xqzt=="-3"){
			return '<span class="label label-danger">'+obj.HTZT_SV+'</span>';
		}else if(xqzt=="2"){
		  	return '<span class="label label-danger">'+obj.HTZT_SV+'</span>';
		}else if(xqzt=="3"){
		  	return '<span class="label label-danger">'+obj.HTZT_SV+'</span>';
		}else if(xqzt=="4"){
		  	return '<span class="label label-danger">'+obj.HTZT_SV+'</span>';
		}else if(xqzt=="3"){
		  	return '<span class="label label-warning">'+obj.HTZT_SV+'</span>';
		}else if(xqzt=="1"){
		  	return '<span class="label label-success">'+obj.HTZT_SV+'</span>';
		}else if(xqzt=="0"){
		 	return '<span class="label label-success">'+obj.HTZT_SV+'</span>';
		}else if(xqzt=="-2"){
			if(obj.EVENTSJBH=="5" || obj.EVENTSJBH=="6" || obj.EVENTSJBH=="7"){
				return '<span class="label label-success">'+obj.EVENTSJBH_SV+'</span>';
			}else{
				return '<span class="label label-warning">'+obj.HTZT_SV+'</span>';
			}
		}else if(xqzt=="-1"){
		 	return '<span class="label label-danger">'+obj.HTZT_SV+'</span>';
		}else{
			return obj.HTZT_SV;
		}
		
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
						招投标管理
						<span class="pull-right">
							<button id="ztbxz" class="btn" type="button" style="display:none;">
								招投标新增
							</button>
							<app:oPerm url="/jsp/business/ztb/ztbgl.jsp">
								<button id="wh" class="btn" type="button">
									招投标维护
								</button>
							</app:oPerm>
							<app:oPerm url="/jsp/framework/common/aplink/defaultArchivePage.jsp?ztbxqzh">
								<button id="sp_btn" class="btn" type="button" style="display:none;">
									呈请审批
								</button>
							</app:oPerm>
							<app:oPerm url="/jsp/business/ztb/ztbgl.jsp?updateZhaotoubiaoZT">
								<button id="tjzb_btn" class="btn" type="button">
									提交到合同
								</button>
							</app:oPerm>
							<button id="btnExp" class="btn" type="button">
								导出
							</button> </span>
					</h4>
					<form method="post" id="queryForm">
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
								<th width="3%" class="right-border bottom-border text-right">招投标年度</th>
								<td class="right-border bottom-border" width="5%">
									<select class="span12 year" id="ND" name="QueryND" kind="dic" src="T#GC_ZTB_SJ:distinct to_char(KBRQ,'YYYY') CODE:to_char(KBRQ,'YYYY') VALUE:SFYX='1' and KBRQ is not null order by to_char(KBRQ,'YYYY') asc"
										fieldname="to_char(a.KBRQ,'yyyy')" operation="=" defaultMemo="全部">
									</select>
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									招投标名称
								</th>
								<td width="20%" class="right-border bottom-border">
									<input class="span12" type="text" id="ZTBMC" name="ZTBMC"
										fieldname="a.ZTBMC" operation="like" maxlength="100">
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									招标类型
								</th>
								<td width="10%" class=" right-border bottom-border">
									<select class="span12 4characters" id="ZBLX" name="ZBLX"
										fieldname="x.ZBLX" defaultMemo="全部" kind="dic" src="ZBLX"
										operation="=">
									</select>
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									招标方式
								</th>
								<td width="10%" class=" right-border bottom-border">
									<select class="span12 4characters" id="ZBFS" name="ZBFS"
										fieldname="a.ZBFS" defaultMemo="全部" kind="dic" src="ZBFS"
										operation="=">
									</select>
								</td>
									<th width="5%" class="right-border bottom-border text-right">
									状态
								</th>
								<td width="10%" class=" right-border bottom-border">
									<select class="span12 4characters" id="XQZT" name="XQZT"
										fieldname="a.XQZT" defaultMemo="全部" kind="dic" src="ZBZT"
										operation="=">
									</select>
								</td>
							
								<td width="30%" class=" bottom-border text-right" rowspan="2">
									<button id="chaxun" class="btn btn-link" type="button">
										<i class="icon-search"></i>查询
									</button>
									<button id="query_clear" class="btn btn-link" type="button">
										<i class="icon-trash"></i>清空
									</button>
								</td>
							</tr>
							<tr>
								<th width="5%" class="right-border bottom-border text-right">
									项目名称
								</th>
								<td width="20%" class=" right-border bottom-border" colspan="3">
									<input class="span12" type="text" id="XMMC" name="XMMC"
										fieldname="C.XMMC" operation="like" maxlength="100">
								</td> 
								<td colspan="6"></td>
							</tr>
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
					<table width="100%" class="table-hover table-activeTd B-table"
						id="DT1" type="single" editable="0" printFileName="招投标管理">
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
								<th fieldname="ZBLX" tdalign="">
									&nbsp;招标类型&nbsp;
								</th>
								<th fieldname="ZBFS" tdalign="center" maxlength="15">
									&nbsp;招标方式&nbsp;
								</th>
								<th fieldname="KBRQ" tdalign="center" maxlength="15">
									&nbsp;开标日期&nbsp;
								</th>
								<th fieldname="ZZBJ" tdalign="right" maxlength="15">
									&nbsp;总中标价(元)
								</th>
								<th fieldname="BJXS" tdalign="right" maxlength="15">
									&nbsp;报价系数
								</th>
								<th fieldname="DZFS" tdalign="" maxlength="15">
									&nbsp;付款方式&nbsp;
								</th>
								<th fieldname="ZBXMFZR" tdalign="center" maxlength="15">
									&nbsp;项目负责人
								</th>
								<th fieldname="DSFJGID" tdalign="left" maxlength="15">
									&nbsp;中标单位&nbsp;
								</th>
								<th fieldname="HTZT" tdalign="center"  hasLink="true" CustomFunction="showHtzt" noprint="true">
									&nbsp;合同状态&nbsp;
								</th>
								<th fieldname="HTBM" tdalign="left"  hasLink="true" linkFunction="rowViewHT">
									&nbsp;合同编码&nbsp;
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
								<TD>
									<INPUT class="span12" type="text" name="GC_ZTB_SJ_ID"
										fieldname="GC_ZTB_SJ_ID" id="GC_ZTB_SJ_ID" />
									<INPUT class="span12" type="text" id="XQZT" name="XQZT"
										fieldname="XQZT" value="2" keep="true" />
								</TD>
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