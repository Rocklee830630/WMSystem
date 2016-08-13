<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/ZhaotoubiaoController.do";
	g_bAlertWhenNoResult = false;
	var i=0;					//页面表单隐藏或显示按钮
	var ZTBSJIDJE = "";
	$(function() {
		doInit();
		//价格划分维护页面
		var jghf=$("#jghf");
		jghf.click(function() {
			//$("#DT1").setSelect(0);
			$(window).manhuaDialog({"title":"金额划分","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/jghf.jsp?&ZTBSJID="+ZTBSJIDJE,"modal":"2"});
		});
		//项目信息
		var xmxx=$("#xmxx");
		xmxx.click(function(){
			$("#aaa").slideToggle("fast");
		});
	 	//保存
	 	$("#save").click(function()
			{
	 		  saveZhaoBiao(); 	
			}
	 	);
	 	$("#btnDel").click(function(){
	 		var data = Form2Json.formToJSON(demoForm);
			xConfirm("提示信息","是否确认删除！");
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').one("click",function(){
				$(window).manhuaDialog.getParentObj().doDelZtb(data,$(window));
			});
	 	});
	});
	var g_btnNum = 0;
	function xzxm(n) {
		g_btnNum = n;
		$(window).manhuaDialog({"title" : "参建单位","type" : "text","content" : "${pageContext.request.contextPath}/jsp/business/gcb/cjdw/cjdw_Query_Add.jsp","modal":"4"});
	}
	function doInit(){
		//获取父页面的值
		var a =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
		//将父页面的值转成json对象
		var odd=convertJson.string2json1(a);
		var p_zblx = $(odd).attr("ZBLX");
		switch(p_zblx){
			case '11':
				 $("#lydwSelect").attr("dwlx", "1"); 
				break;
			case '12':
				 $("#lydwSelect").attr("dwlx", "2"); 
				break;
			case '13':
				 $("#lydwSelect").attr("dwlx", "3"); 
				break;
			case '14':
				 $("#lydwSelect").attr("dwlx", "4"); 
				break;
			case '15':
				 $("#lydwSelect").attr("dwlx", "5"); 
				break;
			case '16':
				 $("#lydwSelect").attr("dwlx", "6"); 
				break;
			case '17':
				 $("#lydwSelect").attr("dwlx", "6"); 
				break;
			case '18':
				 $("#lydwSelect").attr("dwlx", "7"); 
				break;
			default:
				break;
		}
		//将数据放入表单
		$("#demoForm").setFormValues(odd);
		var ZTBSJID=$(odd).attr("GC_ZTB_SJ_ID");
		//$("#ZBFS").attr("disabled","true");//临时 modified by hongpeng.dong at 2014.10.27
		if(odd.ZBFS=="1"){
		  	$(".ggTab").show();
		}else{
		  	$(".ggTab").hide();
		}
		//判断招标类型
		if(p_zblx=="11"){
			//设计招标
			$(".sjzbTh").show();
			$(".sgzbTh").hide();
			$(".jlzbTh").hide();
		}else if(p_zblx=="12"){
			//监理招标
			$(".sjzbTh").hide();
			$(".sgzbTh").hide();
			$(".jlzbTh").show();
		}else if(p_zblx=="13"){
			//施工招标
			$(".sjzbTh").hide();
			$(".sgzbTh").show();
			$(".jlzbTh").hide();
		}else{
			//其他全部是隐藏
			$(".sjzbTh").hide();
			$(".sgzbTh").hide();
			$(".jlzbTh").hide();
		}
		//判断是否是施工招标是查询拦标价
		/* 	if(p_zblx=="13"){
				var action = controllername + "?queryLanBiaoJia&ZTBSJID="+ZTBSJID
				$.ajax({
					url : action,
					dataType:"json",
					async:false,
					success: function(result){
						var obj=convertJson.string2json1(result.msg);
						if(obj){增加判断，解决没有拦标价时会报错的问题 add by hongpeng.dong at 2014.10.24
							var isxyscs=obj.response.data[0].ISXYSCS;
							if('0'==isxyscs){
								$("#CSLBJ").val(obj.response.data[0].SBCSZ);
							}else{
								$("#CSLBJ").val(obj.response.data[0].CSLBJ);
							}
						}
					}
				});
	 	} */

		//用户提出报价系数不需要判断了，一直显示就行了
		$(".bjxsTh").show();
		$(".bjxsThBlank").hide();
		//查看附件
		deleteFileData(ZTBSJID,"","","");
		setFileData(ZTBSJID,"","","","0");
		queryFileData(ZTBSJID,"","","");
		ZTBSJIDJE = ZTBSJID;
		//查询项目
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryZhaoBiaoXiangMu&ZTBSJID="+ZTBSJID,data,DT1,null,true);
		$("#aaa").hide();
	}
	//保存操作
	function saveZhaoBiao(){
		var s_date=$("#GGFBQSRQ").val();
		var e_date=$("#GGFBJSRQ").val();
		if(s_date!='' && e_date!='' && s_date>e_date){
			xFailMsg("公告发布结束日期不能大于起始日期！","");
			return;
		}
		var k_date=$("#KBRQ").val();
		if(k_date!='' && e_date!='' && k_date<e_date){
			xFailMsg("开标日期不能小于公共发布结束日期！","");
			return;
		}
		if($("#demoForm").validationButton()){
			//生成json串
			var data = Form2Json.formToJSON(demoForm);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			//调用ajax插入
			defaultJson.doUpdateJson(controllername + "?updateZhaotoubiao", data1,null,"updateHuiDiao");
		
		}
	}
	function updateHuiDiao()
	{
		var data2 = $("#frmPost").find("#resultXML").val();
		var fuyemian=$(window).manhuaDialog.getParentObj();
		fuyemian.xiugaihang(data2);
	}
	//弹出窗口回调函数
	getWinData = function(data){
		if(g_btnNum==1){
			var odd=convertJson.string2json1(data);
			$("#ZBDL").val(JSON.parse(data).DWMC);
			$("#ZBDL").attr("code",JSON.parse(data).GC_CJDW_ID);
		}else{
	         var odd=convertJson.string2json1(data);
		       $("#DSFJGID").val(JSON.parse(data).DWMC);
		       $("#DSFJGID").attr("code",JSON.parse(data).GC_CJDW_ID);
		      // document.getElementById('DSFJGID').code = JSON.parse(data).GC_CJDW_ID; 
		}	
	};
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
	function doLbj(obj){
		var isxyscs=obj.ISXYSCS;
		if('0'==isxyscs){
			return  obj.SBCSZ;
		}else{
			return  obj.CSLBJ;
		}
	}
</script>   
</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						招标信息
						<span class="pull-right">
							<button id="xmxx" class="btn" type="button">
								项目信息
							</button>
							<button id="jghf" class="btn" type="button">
								金额划分
							</button> 
							<button id="save" class="btn" type="button">
								保存
							</button> 
							<button id="btnDel" class="btn" type="button">
								删除
							</button></span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<input type="text" class="span12" kind="text"
										fieldname="rownum" value="1000" operation="<=">
								</TD>
							</TR>
						</table>
					</form>
					<div class="overFlowX">
					<div class="B-small-from-table-autoConcise" id="aaa">
						<!-- 台账展示信息_start -->
							<table width="100%" class="table-hover table-activeTd B-table"
								id="DT1" type="single" editable="0" noPage="true" pageNum="1000" nopromptmsg="true">
								<thead>
									<tr>
										<th name="XH" id="_XH" tdalign="">
											&nbsp;#&nbsp;
										</th>
										<th fieldname="GZMC" maxlength="20">
											&nbsp;工作名称&nbsp;
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
										<th fieldname="SBCSZ" tdalign="right" maxlength="20" Customfunction="doLbj">
											&nbsp;财审拦标价&nbsp;
										</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
					</div>
					</div>
					<!-- 台账展示信息_end -->
					<form method="post" id="demoForm">
						<div style="height: 5px;"></div>
						<table class="B-table" width="100%">
							<TR style="display: none;">
								<TD>
									<input class="span12" id="GC_ZTB_SJ_ID" type="text"
										name="GC_ZTB_SJ_ID" fieldname="GC_ZTB_SJ_ID">
								</TD>
								<TD>
									<input class="span12" id="XQZT" type="text"
										name="XQZT" fieldname="XQZT">
								</TD>
								<TD>
									<input class="span12" type="text"
										name="SJBH" fieldname="SJBH">
								</TD>
								<TD>
									<input class="span12" type="text"
										name="YWLX" fieldname="YWLX">
								</TD>
							</TR>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									招投标名称
								</th>
								<td width="42%" class="right-border bottom-border ">
									<input class="span12" id="ZTBMC" type="text" fieldname="ZTBMC"
										name="ZTBMC" maxlength="1000" check-type="required maxlength" placeholder="必填">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									招标编号
								</th>
								<td width="17%" class="right-border bottom-border text-right">
									<input class="span12" type="text" id="ZBBH"
										check-type="maxlength" fieldname="ZBBH"
										name="ZBBH" maxlength="100">
								</td>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									招标方式
								</th>
								<td width="17%" class="right-border bottom-border">
									<select class="span12 4characters" id="ZBFS" type="text"
										fieldname="ZBFS" name="ZBFS" kind="dic" src="ZBFS">
									</select>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									招标代理
								</th>
								<td width="42%" class="right-border bottom-border">
									<select class="span12 department" id="ZBDL" name="ZBDL"
										style="width: 90%" fieldname="ZBDL" kind="dic"
										src="T#GC_CJDW:GC_CJDW_ID:DWMC:1=1" defaultMemo=" "
										disabled="disabled">
									</select>
									<a href="javascript:void(0)" title="点击选择单位"><i
										id="zbdlSelect" selObj="ZBDL" class="icon-edit"
										onclick="selectCjdw('zbdlSelect');" dwlx="5"></i>
									</a>
								</td>
								<td width="50%" colspan=4 class="right-border bottom-border ">
							</tr>
						</table>
						<h4 class="title ggTab">
							公告信息
						</h4>
						<table class="B-table ggTab" width="100%">
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									公告发布媒体
								</th>
								<td width="42%" colspan="7"
									class="right-border bottom-border text-right" colspan="7">
									<input class="span12" type="text" id="GGFBMT" name="GGFBMT"
										fieldname="GGFBMT" maxlength="1000" check-type="maxlength">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									公告发布起始日期
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12 date" type="date" id="GGFBQSRQ"
										name="GGFBQSRQ" fieldname="GGFBQSRQ">
								</td>
								<td width="25%" class="right-border bottom-border" colspan=2></td>
								<th width="8%" class="right-border bottom-border text-right">
									公告发布结束日期
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12 date" type="date" id="GGFBJSRQ"
										name="GGFBJSRQ" fieldname="GGFBJSRQ">
								</td>
								<td width="25%" class="right-border bottom-border" colspan=2></td>
							</tr>
							<tr>
								<td width="100%" colspan="8" class="bottom-border">
									<input class="span12" type="checkbox" placeholder=""
										name="GGQK" kind="dic" src="GGQK" fieldname="GGQK">
								</td>
							</tr>
						</table>
						<h4 class="title">
							开标信息
						</h4>
						<table class="B-table" width="100%">
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									开标时间
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12 date" type="date" id="KBRQ" name="KBRQ"
										fieldname="KBRQ">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									付款方式
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12" type="text" id="DZFS" name="DZFS" fieldname="DZFS" check-type="maxlength" maxlength="300"/>
								</td>
								<td class="right-border bottom-border" colspan="4"></td>
							</tr>
						</table>
						<h4 class="title">
							中标信息
						</h4>
						<table class="B-table" width="100%">
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									中标通知书编号
								</th>
								<td width="42%" class="right-border bottom-border text-right" colspan=3>
									<input class="span12" type="text" id="ZBTZSBH" name="ZBTZSBH"
										fieldname="ZBTZSBH" maxlength="100"  check-type="maxlength">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									总中标价
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span9" type="number" min="0" id="ZZBJ"
										name="ZZBJ" fieldname="ZZBJ" style="text-align: right;">&nbsp;&nbsp;<b>(元)</b>
								</td>
								<th width="8%" class="right-border bottom-border text-right bjxsTh">
									报价系数
								</th>
								<td width="17%" class="right-border bottom-border text-right bjxsTh">
									<input class="span12" type="number" min="0" id="BJXS"
										name="BJXS" fieldname="BJXS" style="text-align: right;">
								</td>
								<td width="25%" class="right-border bottom-border text-right bjxsThBlank">
								</td>
							</tr>

							<!-- 监理招标信息_end -->
							<tr>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									中标单位
								</th>
								<td width="42%" class="right-border bottom-border" colspan=3>
									<select class="span12 department" id="DSFJGID" name="DSFJGID"
										style="width: 90%" fieldname="DSFJGID" kind="dic"
										src="T#GC_CJDW:GC_CJDW_ID:DWMC:1=1"
										disabled="disabled">
									</select>
									<a href="javascript:void(0)" title="点击选择单位"><i
										id="lydwSelect" selObj="DSFJGID" class="icon-edit"
										onclick="selectCjdw('lydwSelect');" ></i>
									</a>
									<!-- <a href="javascript:void(0)" title="请选择单位"><i
										class="icon-edit" onclick="xzxm(2);"></i>
									</a> -->
								</td>
								<td class="right-border bottom-border" colspan=4></td>
							</tr>

							<!-- 施工招标特有信息_start -->
							<tr class="sgzbTh">
								<th class="right-border bottom-border text-right">
									项目经理
								</th>
								<td class="right-border bottom-border text-right">
									<input class="span12" type="text" id="XMJL" name="XMJL"
										fieldname="XMJL" maxlength="100" check-type="maxlength">
								</td>
								<th class="right-border bottom-border text-right">
									技术负责人
								</th>
								<td class="right-border bottom-border text-right">
									<input class="span12" type="text" id="JSFZR" name="JSFZR"
										fieldname="JSFZR" maxlength="100" check-type="maxlength">
								</td>
								<td class="right-border bottom-border" colspan="4"></td>
							</tr>
							<tr class="sgzbTh">
								<th class="right-border bottom-border text-right">
									项目其他人员
								</th>
								<td class="right-border bottom-border text-right" colspan="7">
									<textarea rows="3" class="span12" type="text" id="XMQTRY"
										name="XMQTRY" fieldname="XMQTRY" maxlength="100" check-type="maxlength"></textarea>
								</td>
							</tr>
							<!-- 施工招标特有信息_end -->

							<!-- 监理招标特有信息_start -->
							<tr class="jlzbTh">
								<th class="right-border bottom-border text-right">
									项目总监
								</th>
								<td class="right-border bottom-border">
									<input class="span12" type="text" id="XMZJ" name="XMZJ"
										fieldname="XMZJ" maxlength="100" check-type="maxlength">
								</td>
									<th class="right-border bottom-border text-right">
									总监代表
								</th>
								<td class="right-border bottom-border text-right">
									<input class="span12" type="text" id="ZJDB" name="ZJDB"
										fieldname="ZJDB" maxlength="100" check-type="maxlength">
								</td>
								<th class="right-border bottom-border text-right">
									安全监理
								</th>
								<td class="right-border bottom-border text-right">
									<input class="span12" type="text" id="AQJL" name="AQJL"
										fieldname="AQJL" maxlength="100" check-type="maxlength">
								</td>
								<td class="right-border bottom-border" colspan=2></td>
							</tr>
							<tr class="jlzbTh">
								<th class="right-border bottom-border text-right">
									项目其他
									<br>
									监理人员
								</th>
								<td class="right-border bottom-border text-right" colspan="7">
									<textarea rows="3" class="span12" type="text" id="XMQTJLRY"
										name="XMQTJLRY" fieldname="XMQTJLRY" maxlength="100" check-type="maxlength"></textarea>
								</td>
							</tr>
							<!-- 监理招标特有信息_end -->


							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									附件上传
								</th>
								<td colspan="8" class="bottom-border right-border">
									<div>
										<span class="btn btn-fileUpload" onclick="doSelectFile(this);"
											fjlb="2029"> <i class="icon-plus"></i> <span>添加文件...</span>
										</span>
										<table role="presentation" class="table table-striped">
											<tbody fjlb="2029" class="files showFileTab"
												data-toggle="modal-gallery" data-target="#modal-gallery">
											</tbody>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									备注
								</th>
								<td class="right-border bottom-border text-right" colspan="7">
									<textarea rows="3" class="span12" type="text" id="BZ" name="BZ"
										fieldname="BZ" maxlength="4000" check-type="maxlength"></textarea>
								</td>
							</tr>
						</table>

						<h4 class="title sgzbTh">
							施工招标信息
						</h4>
						<table class="B-table sgzbTh" id ="sgzbTable" width="100%">
						<!-- 	<tr>
								<th width="8%" class="right-border bottom-border text-right">
									财审拦标价
								</th>
								<td width="17%" class="right-border bottom-border">
									<input style="text-align: right;"  disabled class="span9"
										type="number" min="0" id="CSLBJ" name="CSLBJ"
										fieldname="CSLBJ">
									&nbsp;&nbsp;<b>(元)</b>
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									中标价比照财审拦标价降幅
								</th>
								<td width="17%" class="right-border bottom-border">
									<input style="text-align: right; width: 80%" class="span8"
										type="number" min="0" max="100" id="ZBJBZCS" name="ZBJBZCS"
										fieldname="ZBJBZCS">
									%
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									现场摇球降幅
								</th>
								<td width="17%" class="right-border bottom-border">
									<input style="text-align: right; width: 80%" class="span8"
										type="number" min="0" max="100" id="XCYQJF" name="XCYQJF"
										fieldname="XCYQJF">
									&nbsp;%
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									预留金
								</th>
								<td width="17%" class="right-border bottom-border">
									<input style="text-align: right;" class="span9"
										type="number" min="0" id="YLJ" name="YLJ" fieldname="YLJ">
									&nbsp;&nbsp;<b>(元)</b>
								</td>
							</tr> -->
							<tr>
								<th class="right-border bottom-border text-right">
									应缴纳履约保证金
								</th>
								<td class="right-border bottom-border">
									<input style="text-align: right;" class="span9"
										type="number" min="0" id="YJNLYBZJ" name="YJNLYBZJ"
										fieldname="YJNLYBZJ">
									&nbsp;&nbsp;<b>(元)</b>
								</td>
								<td class="right-border bottom-border" width="70%" colspan=6></td>
							</tr>
						</table>
						<h4 class="title sjzbTh" >
							设计招标区域
						</h4>
						<table class="B-table sjzbTh" width="100%">
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									折扣系数
								</th>
								<td width="17%" class="right-border bottom-border text-right">
									<input class="span12" type="number" min="0" id="ZKXS"
										name="ZKXS" fieldname="ZKXS" style="text-align: right;">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									计划中主体造价
								</th>
								<td width="17%" class="right-border bottom-border text-right">
									<input class="span9" type="number" min="0" id="JHZZTZJ"
										name="JHZZTZJ" fieldname="JHZZTZJ" style="text-align: right;">&nbsp;&nbsp;<b>(元)</b>
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									八折造价
								</th>
								<td width="17%" class="right-border bottom-border text-right">
									<input class="span9" type="number" min="0" id="BZZJ"
										name="BZZJ" fieldname="BZZJ" style="text-align: right;">&nbsp;&nbsp;<b>(元)</b>
								</td>
							   <th width="8%" class="right-border bottom-border text-right">
									负责人
								</th>
								<td width="17%" class="right-border bottom-border text-right">
									<input class="span12" type="text"  id="SJFZR"  check-type="maxlength" maxlength="100"
										name="SJFZR"  fieldname="SJFZR">
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
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank" id="frmPost">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="desc" fieldname="d.gzmc,c.xmbh,c.xmbs,C.PXH" id="txtFilter">
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