<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%	String openType = request.getParameter("openType")==null?"":request.getParameter("openType");
	String zbxqID = request.getParameter("xqid")==null?"":request.getParameter("xqid");
	String msid = request.getParameter("msid")==null?"":request.getParameter("msid");
 %>
<app:base/>
	<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/ZhaotoubiaoController.do";
	var controllername_zbxq= "${pageContext.request.contextPath }/ZhaoBiaoXuQiuController.do";
	var i=0;					//页面表单隐藏或显示按钮
	var xmrowValues;
	var p_zbFlag = 1;
	var p_openType = "<%=openType%>";
	var p_zbxqID = "<%=zbxqID%>";
	var p_msid = "<%=msid%>";
	$(function() {
		doInit();
		//选择需求
	 	$("#example1").click(function(){
	 		$(window).manhuaDialog({"title":"招投标管理>选择需求","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/xqmore.jsp","modal":"1"});
		});
	 	$("#bc_btn").click(function(){
	 	 	saveZhaoTouBiao();
	 	});
	 	$("#btnFlip").click(function(){
	 		if(p_zbFlag==1){
				$("#DT2").fadeOut();
				$("#zxmList").fadeIn();
				$(this).text("显示招标需求");
				p_zbFlag = 2;
			}else{
				$("#zxmList").fadeOut();
				$("#DT2").fadeIn();
				$(this).text("显示待招标项目");
				p_zbFlag = 1;
			}
	 	});
	});
	function changeState(){
		$.ajax({
			url:"${pageContext.request.contextPath }/messageController.do?changeZt&opid="+p_msid,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
			}
		});
	}
	function doInit(){
		//获取父页面iframe中隐藏域的值
		var rowValue;
		if(p_openType=="message"){
			$("#example1").hide();
			changeState();//修改消息状态
			rowValue = getFormValueByID(p_zbxqID);
			if(rowValue=="" || rowValue==null){
				xAlert("提示",'该需求已经启动招标','3');
				$("#zxmList").hide();
				return false;
			}
		}else{
			rowValue = $($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
		}
		if(rowValue!=undefined && rowValue!=""){
			//字符串转JSON对象
			var tempJson = convertJson.string2json1(rowValue);
			$("#DT2").insertResult(rowValue,DT2,1);
			$("#ztbmc").val(tempJson.GZMC);
			$("#zbfs").val(tempJson.ZBFS);
			//$("#zbfs").attr("disabled","true");
			//$("#zbfsTh").addClass("disabledTh");
			huoqutr();
			//ctrlZbdl(tempJson.ZBLX);
		}else{
			//$("#zbdlTh").hide();
		}
	 	$("#zxmList").hide();
		doSearchZxm();
	}
	function getFormValueByID(n){
		var resJson = "";
		var action = controllername +"?getFormValueByID&xqzt='3'&id="+n;
		$.ajax({
			url : action,
			dataType:"json",
			async:false,
			success: function(result){
				var resultmsg = result.msg;
				var resultmsgobj = convertJson.string2json1(resultmsg);
				if(resultmsgobj==0){
					return false;
				}
				resJson = JSON.stringify(resultmsgobj.response.data[0]);
			}
		});
		return resJson;
	}
	function saveZhaoTouBiao(){
		if($("#DT2").getTableRows()==0){
			xInfoMsg('请选择招标需求！');
			return ;
		}
		if($("#demoForm2").validationButton()){
			var xqidStr = "";
			for(var i=0;i<$("#DT2").getTableRows();i++){
				var tempJson = convertJson.string2json1($("#DT2").getSelectedRowJsonByIndex(i));
				xqidStr +=tempJson.GC_ZTB_XQ_ID+",";
			}
			xqidStr = xqidStr.substring(0,xqidStr.length-1);
			var checkFlag = doCheckFlag(xqidStr);
			if(checkFlag=="0"){
				//0表示正常
			}else{
				xAlert("警告",'存在已经启动招标的需求，不能再次启动招标！','3');
				return;
			}
			var zbfsVal = $("#zbfs").val();
			xqidStr += "&zbfs="+zbfsVal;
			xqidStr += getCheckBoxValueStr();//加入可能存在的招投标计划数据ID
			var data = Form2Json.formToJSON(demoForm2);
			if(p_openType=="message"){
				//如果是从消息页面进入，那么直接提交，不找父页面方法
				var data1 = defaultJson.packSaveJson(data);
				$.ajax({
					url:controllername_zbxq + "?doQdzb&ztbxqid="+xqidStr,
					data:data1,
					dataType:"json",
					async:false,
					success:function(result){
						doSearchZxm();
						isXM();
					}
				});
			}else{
				var fuyemian=$(window).manhuaDialog.getParentObj();
				fuyemian.xiugaihang(data,xqidStr);
				doSearchZxm();
				isXM();
			}
		}
	}
	//校验是否存在已启动招标的需求
	function doCheckFlag(n){
		var res = "";
		$.ajax({
			url:controllername + "?doCheck&ztbxqid="+n,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				res = result.msg;
			}
		});
		return res;
	}
	//判断是否还有项目
	function isXM(){
		if(0>=$("#zxmList").getTableRows()){
			if(p_openType=="message"){
				//如果是消息页面打开的，回掉一下操作成功
				var fuyemian=$(window).manhuaDialog.getParentObj();
				try{
					fuyemian.showSuccessMsg();
				}catch(e){
					window.parent.showSuccessMsg();
				}
			}
			$(window).manhuaDialog.close();
		}else{
			xAlert("提示信息",'操作成功','1');
			return;
		}
	}
	function getCheckBoxValueStr(){
		var str = "";
		$("input[name='ck']:checked").each(function () {
			str += $(this).attr("zxmid")+",";
		});
		if(str!=""){
			str = "&zxmid="+str.substring(0,str.length-1);
		}
		return str;
	}
	function ctrlZbdl(n){
		if(n=="15"){
			$("#zbdlTh").show();
		}else{
			$("#zbdlTh").hide();
		}
	}
	//子页面“确定”按钮回调函数
	function fanhuixiangm(rowValues,ids){
		//清空列表
		var ids = "";
		$("#DT2").find("tbody").children().remove();
		xmrowValues=rowValues;
		for(var i=0;i<rowValues.length;i++){
			$("#DT2").insertResult(rowValues[i],DT2,1);
			var value = convertJson.string2json1(rowValues[i]).GC_ZTB_XQ_ID;
			ids+=value+",";
		}
		ids = ids==""?"":ids.substring(0,ids.length-1);
		var tempJson = convertJson.string2json1(rowValues[0]);
		$("#ztbmc").val(tempJson.GZMC);
		$("#zbfs").val(tempJson.ZBFS);
		//$("#zbfs").attr("disabled","true");//允许用户修改招标方式
		//$("#zbfsTh").addClass("disabledTh");
		//ctrlZbdl(tempJson.ZBLX);
		$("#ZTBXQID").val(ids);
		doSearchZxm();
	}
	//----------------------------------
	//-查询列表中需求记录所对应的招投标ID
	//----------------------------------
	function doSearchZxm(){
		var xqidStr = "";
		for(var i=0;i<$("#DT2").getTableRows();i++){
			var tempJson = convertJson.string2json1($("#DT2").getSelectedRowJsonByIndex(i));
			xqidStr +=tempJson.GC_ZTB_XQ_ID+",";
		}
		xqidStr = xqidStr.substring(0,xqidStr.length-1);
		var data = new Object();
		data["XQID"] = xqidStr;
		dataStr = JSON.stringify(data);
		defaultJson.doQueryJsonList(controllername+"?queryZxmListWithXqid",dataStr,zxmList);
	}
	function getArr(){
		return xmrowValues;
	}
	//获取table TR的所有值
	function huoqutr(){
		var ids = "";
		var rowValues=new Array();
		$("#DT2 tbody tr").each(function(i){
			var rowValue = $(this).attr("rowJson");
			rowValues[i]=rowValue;
			var value = convertJson.string2json1($(this).attr("rowJson")).GC_ZTB_XQ_ID;
			ids+=value+",";
		});
		ids = ids==""?"":ids.substring(0,ids.length-1);
		xmrowValues=rowValues;
		$("#ZTBXQID").val(ids);
	}
	function xzxm() {
		$(window).manhuaDialog({"title" : "参建单位","type" : "text","content" : "${pageContext.request.contextPath}/jsp/business/gcb/cjdw/cjdw_Query_Add.jsp","modal":"4"});
	}
	//弹出窗口回调函数
	getWinData = function(data){
	
			var odd=convertJson.string2json1(data);
			$("#ZBDL").val(JSON.parse(data).DWMC);
			$("#ZBDL").attr("code",JSON.parse(data).GC_CJDW_ID);
	
	}; 
	//弹出窗口关闭--弹出页面回调空函数
    function closeNowCloseFunction(){
		if(p_openType=="message"){
			//如果是从消息页面进入，不需要刷新父页面
		}else{
			var fuyemian=$(window).manhuaDialog.getParentObj();
			fuyemian.doSearch();
		}
	}
	//判断是否是项目
	function doBdmc(obj){
		var bd_name=obj.BDMC;
		if(bd_name==null||bd_name==""){
			return '<div style="text-align:center">—</div>';
		}else{
			return bd_name;			  
		}
	}
	//判断是否是项目
	function doBdbh(obj){
		var bd_name=obj.BDBH;
		if(bd_name==null||bd_name==""){
			return '<div style="text-align:center">—</div>';
		}else{
		 	return bd_name;			  
		}
	}
	//把表格中每行的第一个td替换成checkbox
	function showCheckbox(obj){
		var showHtml = '<div style="width:20px;text-align:center;"><input type="checkbox" zxmid="'+obj.GC_ZTB_JHSJ_ID+'" style="margin:0px;" name="ck" checked></div>';
		return showHtml;
	}
</script>   
</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						需求信息
						<span class="pull-right">
							<button id="btnFlip" class="btn" type="button">
								显示待招标项目
							</button>
							<button id="example1" class="btn" type="button">
								选择需求
							</button>
						</span>
					</h4>
					<table width="100%" id="zxmList" class="table-hover table-activeTd B-table" 
						type="single" noPage="true" pageNum="1000" noPromptMsg="true">
						<thead>
							<tr>
								<th name="XH" id="_XH" tdalign="">
									&nbsp;#&nbsp;
								</th>
								<th fieldname="XMBH" tdalign="center" Customfunction="showCheckbox">
									选择
								</th>
								<th fieldname="XMBH" >
									&nbsp;项目编号&nbsp;
								</th>
								<th fieldname="XMMC" maxlength="15">
									&nbsp;项目名称&nbsp;
								</th>
								<th fieldname="BDBH" maxlength="15" Customfunction="doBdbh">
									&nbsp;标段编号&nbsp;
								</th>
								<th fieldname="BDMC" maxlength="15" Customfunction="doBdmc">
									&nbsp;标段名称&nbsp;
								</th>
								<th fieldname="XMLX" tdalign="center">
									&nbsp;项目类型&nbsp;
								</th>
								<th fieldname="XMSX" tdalign="center">
									&nbsp;项目属性&nbsp;
								</th>
								<th fieldname="XMDZ" maxlength="20">
									&nbsp;项目地址&nbsp;
								</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					<table width="100%" class="table-hover table-activeTd B-table"
						id="DT2" type="single" noPage="true" pageNum="1000">
						<thead>
							<tr>
								<th name="XH" id="_XH">
									&nbsp;#&nbsp;
								</th>
								<th fieldname="GZMC" maxlength="15">
									&nbsp;工作名称&nbsp;
								</th>
								<th fieldname="GZNR" maxlength="15">
									&nbsp;工作内容&nbsp;
								</th>
								<th fieldname=ZBLX maxlength="15" tdalign="center">
									&nbsp;招标类型&nbsp;
								</th>
								<th fieldname="JSYQ" maxlength="15">
									&nbsp;技术要求&nbsp;
								</th>
								<th fieldname="TBJFS" maxlength="15" tdalign="center">
									&nbsp;投标报价方式&nbsp;
								</th>
								<th fieldname="YSE" maxlength="15" tdalign="right">
									&nbsp;投资额(元)&nbsp;
								</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					<form method="post" id="demoForm2">
						<h4 class="title">
							招标信息
							<span class="pull-right">
								<button id="bc_btn" class="btn" type="button">
									保存
								</button>
							</span>
						</h4>
						<div style="height: 5px;"></div>
						<table class="B-table" width="100%">
							<TR style="display: none">
								<TD>
									<input class="span12" id="GC_ZTB_SJ_ID" type="text"
										name="GC_ZTB_SJ_ID" fieldname="GC_ZTB_SJ_ID">
									<input type="text" id="ZTBXQID" name="ZTBXQID" />
									<input class="span12" id="GC_ZTB_JHSJ_ID" type="text"
										name="GC_ZTB_JHSJ_ID" fieldname="GC_ZTB_JHSJ_ID">
								</TD>
							</TR>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									招投标名称
								</th>
								<td width="42%" colspan=3 class="right-border bottom-border ">
									<input class="span12" id="ztbmc" type="text" fieldname="ZTBMC"
										name="ZTBMC" check-type="required maxlength" placeholder="必填" maxlength="1000">
								</td>
								<th width="8%" id="zbfsTh" class="right-border bottom-border text-right">
									招标方式
								</th>
								<td width="17%" class="right-border bottom-border">
									<select class="span12 4characters" type="text" id="zbfs" check-type="required" placeholder="必填"
										fieldname="ZBFS" name="ZBFS" kind="dic" src="ZBFS">
									</select>
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									招标编号
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12" type="text" id="ZBBH" id="zbbh"
										check-type=" maxlength" fieldname="ZBBH"
										name="ZBBH" maxlength="100">
								</td>
							</tr>
							<tr id="zbdlTh">
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									招标代理
								</th>
								<td width="42%" colspan=3 class="right-border bottom-border">
									<select class="span12 department" id="ZBDL" name="ZBDL"
										style="width: 90%" fieldname="ZBDL" kind="dic"
										src="T#GC_CJDW:GC_CJDW_ID:DWMC:1=1" defaultMemo=" "
										disabled="disabled">
									</select>
									<a href="javascript:void(0)" title="点击选择单位"><i
										id="zbdlSelect" selObj="ZBDL" class="icon-edit"
										onclick="selectCjdw('zbdlSelect');" isLxSelect="1" dwlx="5"></i>
									</a>
								</td>
								<td class="right-border bottom-border" colspan=4></td>
							</tr>
						</table>

					</form>
				</div>
			</div>
		</div>
		<jsp:include page="/jsp/file_upload/fileupload_config.jsp"
			flush="true" />
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank" id="frmPost">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="desc" fieldname="XMNF"
					id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
				<input type="hidden" name="queryResult" id="queryResult">

			</FORM>
		</div>
		<script>

</script>
	</body>
</html>