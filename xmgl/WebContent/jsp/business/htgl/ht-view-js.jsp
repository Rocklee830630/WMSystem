<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%@ page import="com.ccthanking.framework.common.User"%>
		<%@ page import="com.ccthanking.framework.common.OrgDept"%>
		<%@ page import="com.ccthanking.framework.Globals"%>
		<%@ page import="com.ccthanking.framework.util.Pub"%>
		<app:base />
		<title>合同-维护</title>
		<%
	String type=request.getParameter("type");//操作类型
	

	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String userid = user.getAccount();
	String username = user.getName();
	String roelsString = user.getRoleListString();
	boolean cwcFlag = false;
	if("100000000002".equals(deptId) || roelsString.indexOf("财务部")!=-1){
	    cwcFlag = true;
	}
	
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
		<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtController.do";
var controllernameHtsj= "${pageContext.request.contextPath }/htgl/gcHtglHtsjController.do";
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,gcHtglHtList);
	});
	
	//按钮绑定事件(修改)
	$("#btnXMBD").click(function() {
		$(window).manhuaDialog({"title":"合同项目/标段>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/htsj-indexOne.jsp?type=update","modal":"2"});
	});
	$("#btnZF").click(function() {
		$(window).manhuaDialog({"title":"合同支付>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtzf-indexOne-view.jsp?type=update","modal":"2"});
	});
	$("#btnWCTZ").click(function() {
		$(window).manhuaDialog({"title":"合同完成投资>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtwctz-indexOne-view.jsp?type=update","modal":"2"});
	});
	$("#btnBG").click(function() {
		$(window).manhuaDialog({"title":"合同变更>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtbg-indexOne-view.jsp?type=update","modal":"2"});
	});
	$("#btnWJ").click(function() {
		$(window).manhuaDialog({"title":"合同文件>详细信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/hthtwj-indexOne.jsp?type=update","modal":"2"});
	});
	

	//按钮绑定事件（新增）
    $("#btnClear_Bins").click(function() {
        $("#gcHtglHtForm").clearFormResult();
        $("#gcHtglHtForm").cancleSelected();
        
        
        $("#ZFRQ").val(new Date().toLocaleDateString());
        $("#ZFJE").val(0);
        $("#ID").val("");
    });
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
	
    if($("#ID").val() == "" || $("#ID").val() == null){
    	$('input:radio[name=SFSDKLXHT]:eq(1)').attr("checked",true);
    	$("input[name=SFXNHT]:eq(1)").attr("checked",'checked'); 
    }

    //置所有input 为disabled
	$(":input").each(function(i){
	   $(this).attr("disabled", "true");
	});

	$("#ZHTJS").attr("disabled", false);
	$("#ZJSRQ").attr("disabled", false);
    
	$("#btnZF").attr("disabled", false);
	$("#btnWCTZ").attr("disabled", false);
	$("#btnBG").attr("disabled", false);
	$("#btnJS").attr("disabled", false);
	$("#ConfirmCancleButton").attr("disabled", false);
	$("#ConfirmYesButton").attr("disabled", false);
	
	if($("#YFDW2").val()!=""){
		$("#otherYFDW2").show();
	}
	if($("#YFDW3").val()!=""){
		$("#otherYFDW3").show();
	}
	

	$("#id_choseZjxx").removeAttr("disabled");
	$("#id_choseZTB").attr("disabled", false);
	$("#id_choseZTB1").attr("disabled", false);
});

//审批信息， 暂整为已通过
$(function() {
	$("#btnJS").click(function() {
		
		if($("#gcHtglHtForm").validationButton())
		{
			xConfirm("信息确认","您确定对此合同做结算吗？ ");
			
			$('#ConfirmYesButton').attr('click','');
			$('#ConfirmYesButton').one("click",function(){
				$("#btnJS").attr("disabled", true);
				var data = Form2Json.formToJSON(gcHtglHtForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				defaultJson.doUpdateJson(controllername + "?updateHtjs", data1);
			});
		}
	});
});


//页面默认参数
function init(){
	//生成json串
	<%
		if("detail".equals(type)){
	%>
		var parentmain=$(window).manhuaDialog.getParentObj();	
		var rowValue = parentmain.$("#resultXML").val();
		var tempJson = convertJson.string2json1(rowValue);
		
		$("#QID").val(tempJson.ID);
		$("#QHTSJID").val(tempJson.HTSJID);
		
		setFileData(tempJson.ID,"","","");
		queryFileData(tempJson.ID,"","","");
		
		var flagSFDXMHT;//是否单项目合同控制
		var flagZTBID;//是否有招投标关联
		
		//查询表单赋值
		var data = combineQuery.getQueryCombineData(queryForm,frmPost);
		var data1 = {
			msg : data
		};
		$.ajax({
			url : controllername+"?query",
			data : data1,
			cache : false,
			async :	false,
			dataType : "json",  
			type : 'post',
			success : function(response) {
				$("#resultXML").val(response.msg);
				var resultobj = defaultJson.dealResultJson(response.msg);
				$("#gcHtglHtForm").setFormValues(resultobj);
				$("#gcHtglHtForm #ZTBID").val(resultobj.ZTBMC);
				$("#gcHtglHtForm #ZTBID").attr('code',resultobj.ZTBID);
				
				if(resultobj.ZTBID==""){
					//非招投标关联
					$("#show_ztb").hide();
					flagZTBID = false;
				}else{
					$("#show_ztb").show();
					flagZTBID = true;
				}
				
				if(resultobj.SFDXMHT=="0"){
					//多项目合同
					$("#dxmTR").hide();
				 	$("#aaa").show();
				 	flagSFDXMHT = false;
				}else{
					//单项目合同
					$("#dxmTR").show();
				 	$("#aaa").hide();
				 	flagSFDXMHT = true;
				}
			}
		});
		

		
		
		
		//表单赋值
		var obj = $("#gcHtglHtList").getRowJsonObjByIndex(0);
		//alert(JSON.stringify(obj));
		if(obj!=-1){
			$("#gcHtglHtForm").setFormValues(obj);

			if(obj.ZBGJE==""||obj.ZBGJE=="0"){
				$("#bgtitle").remove();
				$("#bgtable").remove();
			}else{
				var n = parseInt(obj.ZHTQDJ)/parseInt(obj.ZBGJE);
				$("#BGL").val(100*n+"%");
			}
			if(obj.ZJSRQ==""){
				$("#jstitle").remove();
				$("#jstable").remove();
			}
			
			$("#resultXML").val(JSON.stringify(obj));
		}
		$("#btnXMBD").attr("disabled", false);
		$("#btnZF").attr("disabled", false);
		$("#btnWCTZ").attr("disabled", false);
		$("#btnBG").attr("disabled", false);
		
		var dataHtsj = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtsjList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllernameHtsj+"?queryOne",dataHtsj,gcHtglHtsjList);
		
		
		showSubInfo();
		
		//显示更多乙方 单位
		if($("#YFDW2").val()!=""){
			$("#otherYFDW2").show();
		}
		if($("#YFDW3").val()!=""){
			$("#otherYFDW3").show();
		}
		
	 <%
		}
	%>
}


//点击行事件
function tr_click(obj) {
		//alert(JSON.stringify(obj));
		//$("#gcHtglHtForm").setFormValues(obj);
	}

	//选中项目名称弹出页
	function selectXm() {
		$(window)
				.manhuaDialog(
						{
							"title" : "",
							"type" : "text",
							"content" : "${pageContext.request.contextPath }/jsp/business/zjgl/xmcx.jsp",
							"modal" : "2"
						});
	}
	//弹出区域回调
	getWinData = function(data) {
		$("#XMMC").val(JSON.parse(data).XMMC);
		$("#XMBH").val(JSON.parse(data).XMBH);
		$("#GC_TCJH_XMXDK_ID").val(JSON.parse(data).GC_TCJH_XMXDK_ID);
	};

	//详细信息
	function rowView(index) {
		var obj = $("#gcHtglHtList").getSelectedRowJsonByIndex(index);
		var xmbh = eval("(" + obj + ")").XMBH;
		$(window).manhuaDialog(xmscUrl(xmbh));
	}

	function closeNowCloseFunction() {
		return true;
	}

	function showOtherYfdw() {
		$("#otherYFDW2").toggle();
		$("#otherYFDW3").toggle();
	}

	//判断合同显示
	function showSubInfo() {
		var v = $("#HTLX").val();
		if (v != "") {

			$("#SHOW_SGLHT").hide();
			$("#SHOW_SGLHT_title").hide();
			$("#SHOW_PQZRW_title").hide();
			$("#SHOW_PQZRW").hide();

			if (v.indexOf("SG") != -1) {//施工合同
				$("#SHOW_SGLHT_title").show();
				$("#SHOW_SGLHT").show();
			} else if (v == "PQ") {//排序合同
				$("#SHOW_PQZRW_title").show();
				$("#SHOW_PQZRW").show();
			}

		}

	}

	function xzZtb() {
		$(window)
				.manhuaDialog(
						{
							"title" : "部门合同>合同履行信息>招投标信息",
							"type" : "text",
							"content" : "${pageContext.request.contextPath }/jsp/business/htgl/htsj-indexOne.jsp?type=update",
							"modal" : "2"
						});
	}

	function view_zjxx() {
		$(window)
				.manhuaDialog(
						{
							"title" : "部门合同>合同履行信息>造价信息",
							"type" : "text",
							"content" : "${pageContext.request.contextPath }/jsp/business/htgl/htsj-zjxx.jsp?type=update",
							"modal" : "4"
						});
	}
	function doBD(obj){
		var showHtml = "";
		if(obj.BDMC!=""){
			showHtml = obj.BDMC;
		}else{
			showHtml = '<div style="text-align:center">—</div>';
		}
		return showHtml;
	}
</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise" style="display: none;">
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border">
									<INPUT type="text" class="span12" kind="text" id="QID"
										name="ID" fieldname="ghh.ID" value="" operation="=" />
									<INPUT type="text" class="span12" kind="text" id="QHTSJID"
										name="HTSJID" fieldname="ghh2.ID" value="" operation="=" />
								</TD>
							</TR>
							<tr>
								<td class="text-left bottom-border text-right">
									<button id="btnQuery" class="btn btn-link" type="button"
										style="font-family: SimYou, Microsoft YaHei; font-weight: bold;">
										<i class="icon-search"></i>查询
									</button>
									<button id="btnClear" class="btn btn-link" type="button"
										style="font-family: SimYou, Microsoft YaHei; font-weight: bold;">
										<i class="icon-trash"></i>清空
									</button>
								</td>
							</tr>
						</table>
					</form>
				</div>

				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						合同基本信息
						<span class="pull-right">
							<button id="btnJS" class="btn" type="button"
								style="font-family: SimYou, Microsoft YaHei; font-weight: bold;">
								合同结算
							</button>
							<button id="btnZF" class="btn" type="button"
								style="font-family: SimYou, Microsoft YaHei; font-weight: bold;"
								disabled="disabled">
								合同支付
							</button>
							<button id="btnWCTZ" class="btn" type="button"
								style="font-family: SimYou, Microsoft YaHei; font-weight: bold;"
								disabled="disabled">
								完成投资
							</button>
							<button id="btnBG" class="btn" type="button"
								style="font-family: SimYou, Microsoft YaHei; font-weight: bold;"
								disabled="disabled">
								合同变更
							</button> </span>
					</h4>
					<form method="post" id="gcHtglHtForm">
						<table class="B-table" width="100%">
							<input type="hidden" id="ID" fieldname="ID" name="ID" />
							</TD>
							<input type="hidden" id="AlerdyHTSJFlag" value="false" />
							</TD>

							<tr id="show_ztb" style="display: black;">
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									招投标名称
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="ZTBID" style="width: 88%" class="span12"
										name="ZTBID" fieldname="ZTBID" type="text" val="" code=""
										disabled />
									<button class="btn btn-link" type="button" id="id_choseZTB"
										onclick="xzZtb();" title="点击查看 招投标信息">
										<i id="id_choseZTB1" class="icon-edit"></i>
									</button>
								</td>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									招标方式
								</th>
								<td width="17%" class="right-border bottom-border">
									<select id="FBFS" class="span6" name="FBFS" fieldname="FBFS"
										operation="=" kind="dic" src="ZBFS" defaultMemo="-请选择-"
										disabled>
								</td>
							</tr>
							<tr>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									合同名称
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="HTMC" style="width: 88%" class="span12"
										check-type="maxlength" maxlength="1000" name="HTMC"
										fieldname="HTMC" type="text" />
								</td>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									合同编码
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="HTBM" style="width: 70%" class="span12"
										check-type="maxlength" maxlength="100" name="HTBM"
										fieldname="HTBM" type="text" />
									<select id="HTLX" class="span6" name="HTLX" fieldname="HTLX"
										kind="dic" src="HTLX" style="display: none;">
								</td>
							</tr>
							<tr>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									是否虚拟合同
								</th>
								<td width="17%" class="right-border bottom-border">
									&nbsp;&nbsp;
									<input class="span12" id="SFXNHT" type="radio" placeholder=""
										kind="dic" src="SF" name="SFXNHT" fieldname="SFXNHT">
								</td>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									合同签订日期
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="HTJQDRQ" class="span5" style="width: 70%;"
										fieldtype="date" fieldformat="YYYY-MM-DD" name="HTJQDRQ"
										fieldname="HTJQDRQ" type="date" />
								</td>
							</tr>
							<tr>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									合同实际开始日期
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="HTSJKSRQ" class="span9" style="width: 70%;"
										fieldtype="date" fieldformat="YYYY-MM-DD" name="HTSJKSRQ"
										fieldname="HTSJKSRQ" type="date" />
								</td>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									<span title="合同实际结束日期">合同实际结束日期</span>
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="HTJSRQ" class="span9" style="width: 70%;"
										fieldtype="date" fieldformat="YYYY-MM-DD" name="HTJSRQ"
										fieldname="HTJSRQ" type="date" />
								</td>
							</tr>
							<tr>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									合同签订价
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="ZHTQDJ" style="width: 70%; text-align: right;"
										value=0 class="span12" name="ZHTQDJ" fieldname="ZHTQDJ"
										type="number" disabled />
									<b>(元)</b>
								</td>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									变更价
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="ZBGJE" style="width: 70%; text-align: right;"
										class="span12" check-type="maxlength" maxlength="200"
										name="ZBGJE" fieldname="ZBGJE" type="number" disabled />
									<b>(元)</b>
								</td>
							</tr>
							<tr>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									最新合同价
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="ZXHTJ" value=0 class="span9"
										style="width: 70%; text-align: right;" name="ZXHTJ"
										fieldname="ZXHTJ" type="number" min="0" />
									<b>(元)</b>
									<%--				需计算得出  : 总合同签订价+合同变更--%>
								</td>

								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									完成投资
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="ZWCZF" value=0 class="span9"
										style="width: 70%; text-align: right;" name="ZWCZF"
										fieldname="ZWCZF" type="number" min="0" />
									<b>(元)</b>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									&nbsp;
								</td>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									合同支付
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="ZHTZF" value=0 class="span9"
										style="width: 70%; text-align: right;" name="ZHTZF"
										fieldname="ZHTZF" type="number" min="0" />
									<b>(元)</b>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									合同结算价
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="ZHTJS" value=0 class="span9"
										style="width: 70%; text-align: right;" name="ZHTJS"
										fieldname="ZHTJS" type="number" min="0" />
									<b>(元)</b>
									<!-- <button class="btn btn-link"  type="button" id="id_choseZjxx" onclick="view_zjxx()" title="点击查看选价信息"><i class="icon-edit"></i></button> -->
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									结算日期
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="ZJSRQ" class="span9" style="width: 70%;"
										fieldtype="date" fieldformat="YYYY-MM-DD" name="ZJSRQ"
										fieldname="ZJSRQ" type="date" check-type="required" />
								</td>
							</tr>
						</table>
						<h4 class="title">
							项目及标段信息
						</h4>
						<table class="B-table" width="100%">
							<tr>
								<td colspan="4" align="center">
									<div class="overFlowX">
										<table class="table-hover table-activeTd B-table"
											id="gcHtglHtsjList" width="90%" type="single" noPage="true">
											<thead>
												<tr>
													<th name="XH" id="_XH" style="width: 10px" colindex=1
														tdalign="center">
														&nbsp;#&nbsp;
													</th>
													<th fieldname="XMMC" colindex=2 tdalign="center"
														maxlength="30">
														&nbsp;项目名称&nbsp;
													</th>
													<th fieldname="BDMC" colindex=3 tdalign="center"
														maxlength="30" CustomFunction="doBD">
														&nbsp;标段名称&nbsp;
													</th>
													<th fieldname="HTQDJ" colindex=5 tdalign="center">
														&nbsp;合同签订价(元)&nbsp;
													</th>
													<th fieldname="BGJE" colindex=18 tdalign="center" >
														&nbsp;变更金额(元)&nbsp;
													</th>
													<th fieldname="ZXHTJ" colindex=20 tdalign="center">
														&nbsp;最新合同价(元)&nbsp;
													</th>
													<th fieldname="WCZF" colindex=17 tdalign="center">
														&nbsp;完成投资(元)&nbsp;
													</th>
													<th fieldname="HTZF" colindex=16 tdalign="center">
														&nbsp;合同支付(元)&nbsp;
													</th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
									</div>
								</td>
							</tr>
						</table>
						<h4 class="title">
							合同签订信息
						</h4>
						<table class="B-table" width="100%">
							<tr>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									甲方单位
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="JFDW" class="span12" name="JFDW" fieldname="JFDW" type="text"
										style="width: 88%" disabled />
								</td>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									甲方签订人
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="JFQDR" class="span12" check-type="maxlength"
										maxlength="100" name="JFQDR" fieldname="JFQDR" type="text"
										style="width: 88%" disabled />
								</td>
							</tr>
							<tr>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									主办部门
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="ZBBM" class="span12" check-type="maxlength"
										maxlength="106" name="ZBBM" fieldname="ZBBM" type="text"
										style="width: 88%" disabled />
								</td>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									甲方经办人
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="JFJBR" class="span12" check-type="maxlength"
										maxlength="36" name="JFJBR" fieldname="JFJBR" type="text"
										style="width: 88%" disabled />
								</td>
							</tr>
							<tr>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									乙方单位
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="YFDW" class="span12" style="width: 88%" name="YFDW" fieldname="YFDW"
										type="text" />
								</td>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									乙方单位签订人
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="YFDWQDR" style="width: 88%" class="span12"
										check-type="maxlength" maxlength="100" name="YFDWQDR"
										fieldname="YFDWQDR" type="text" />
								</td>
							</tr>
							<tr id="otherYFDW2" style="display: none">
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									丙方单位
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="YFDW2" class="span12" style="width: 88%" name="YFDW2"
										fieldname="YFDW2" type="text" />
								</td>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									丙方单位签订人
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="YFDWQDR2" class="span12" check-type="maxlength"
										maxlength="100" style="width: 88%" name="YFDWQDR2"
										fieldname="YFDWQDR2" type="text" />
								</td>
							</tr>
							<tr id="otherYFDW3" style="display: none">
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									丁方单位
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="YFDW3" class="span12" style="width: 88%" name="YFDW3"
										fieldname="YFDW3" type="text" />
								</td>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									丁方单位签订人
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="YFDWQDR3" class="span12" check-type="maxlength"
										maxlength="100" style="width: 88%" name="YFDWQDR3"
										fieldname="YFDWQDR3" type="text" />
								</td>
							</tr>
						</table>
						<% 
		    if(cwcFlag){
		 %>
		<% 
		    }
		 %>
						<h4 id="SHOW_SGLHT_title" class="title" style="display: none;">
							保修金信息
						</h4>
						<table id="SHOW_SGLHT" class="B-table" width="100%"
							style="display: none;">
							<tr>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									保修金额
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="BXJE" value=0 class="span12"
										style="width: 70%; text-align: right;" onchange="calPercen();"
										name="BXJE" fieldname="BXJE" type="number" min="0" />
									<b>(元)</b>
								</td>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									保修金率
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="BXJL" value=0 class="span12"
										style="width: 70%; text-align: right;" name="BXJL"
										fieldname="BXJL" type="number" min="0" readOnly />
								</td>
							</tr>
							<tr>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									保修期
								</th>
								<td width="17%" class="right-border bottom-border">
									<input id="BXQ" value=0 class="span12"
										style="width: 70%; text-align: right;" placeholder="必填"
										check-type="required" name="BXQ" fieldname="BXQ" type="number"
										min="0" />
									<select id="BXQDWLX" style="width: 25%;" class="span12"
										name="BXQDWLX" fieldname="BXQDWLX" operation="=" kind="dic"
										src="BXQDW" defaultMemo="-请选择-">
								</td>
								<td colspan="2">
									&nbsp;
								</td>
							</tr>
						</table>
						<h4 class="title">
							合同内容及其它信息
						</h4>
						<table class="B-table" width="100%">
							<tr>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									合同主要内容
								</th>
								<td colspan="3" class="bottom-border right-border">
									<textarea class="span12" rows="2" id="HTZYNR"
										check-type="maxlength" maxlength="4000" fieldname="HTZYNR"
										name="HTZYNR"></textarea>
								</td>
							</tr>
							<tr>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									付款方式
								</th>
								<td colspan="3" class="bottom-border right-border">
									<textarea class="span12" rows="2" id="HTFKFS"
										check-type="maxlength" maxlength="4000" fieldname="HTFKFS"
										name="HTFKFS"></textarea>
								</td>
							</tr>
							<tr>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									违约条款
								</th>
								<td colspan="3" class="bottom-border right-border">
									<textarea class="span12" rows="2" id="HTWYTK"
										check-type="maxlength" maxlength="4000" fieldname="HTWYTK"
										name="HTWYTK"></textarea>
								</td>
							</tr>
							<tr>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									结束依据
								</th>
								<td colspan="3" class="bottom-border right-border">
									<textarea class="span12" rows="2" id="HTZSYJ"
										check-type="maxlength" maxlength="4000" fieldname="HTZSYJ"
										name="HTZSYJ"></textarea>
								</td>
							</tr>
							<tr>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									备注
								</th>
								<td colspan="3" class="bottom-border right-border">
									<textarea class="span12" rows="2" id="BZ"
										check-type="maxlength" maxlength="4000" fieldname="BZ"
										name="BZ"></textarea>
								</td>
							</tr>
							<tr>
								<th width="8%"
									class="right-border bottom-border text-right disabledTh">
									相关附件
								</th>
								<td colspan="3" class="bottom-border right-border">
									<div>
										<!--  <span class="btn btn-fileUpload" id="addFileSpan" disabled="disabled" fjlb="0701">
						 <span>添加文件...</span>
					</span>
					-->
										<table role="presentation" class="table table-striped">
											<tbody fjlb="0701" class="files showFileTab"
												data-toggle="modal-gallery" data-target="#modal-gallery"
												onlyView="true"></tbody>
										</table>
									</div>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
		</div>
		<jsp:include page="/jsp/file_upload/fileupload_config.jsp"
			flush="true" />
		<div align="center">
			<FORM id="frmPost1" name="frmPost" method="post"
				style="display: none" target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="desc"
					fieldname="ghh.LRSJ" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">

			</FORM>
		</div>
	</body>
	<script>
	
</script>
</html>