<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%@ page import="com.ccthanking.framework.common.User"%>
		<%@ page import="com.ccthanking.framework.Globals"%>
		<%	String leader = request.getParameter("flag");
			String id = request.getParameter("infoID");
			leader = leader==null || leader==""?"0":leader;
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String deptid = user.getDepartment();
		%>
		<app:base />
		<title>新增问题</title>
		<style type="text/css">
		input[disabled],
		textarea[disabled],
		select[disabled]{
			border-style:none;
			background:#FFF;
			box-shadow:none;
		}
		.form-inline{
			width:80%;
		}
		.titleSpan{
			font-weight: bold;
			font-size:14px;
		}
		.contentSpan{
			font-size:14px;
		}
		.cancleResize{
			resize:none;
		}
		</style>
		<script type="text/javascript" charset="utf-8">
			var controllername= "${pageContext.request.contextPath }/wttb/wttbController.do";
			var btnNum = 0;
			var childBtnNum = 0;
			var flag = '<%=leader%>';
			var id = '<%=id%>';
			$(function(){
				doInit();
				$("#btnSave").click(function(){
					if($("#zbrName").val()==undefined){
						xAlert("请至少选择一位主办人!"); 
						return;
					}else{
						if($("#insertForm").validationButton()){
			 				//生成json串
			 				var zbrStr = $("#zbrName").val()==undefined?"":$("#zbrName").val();
			 				var sbrStr = $("#sbrName").attr("code")==undefined?"":$("#sbrName").attr("code");
			 				var jsrStr = $("#jsrName").val()==undefined?"":$("#jsrName").val();
			 				var jsrString = zbrStr+":"+sbrStr+":"+jsrStr;
			 				if(sbrStr.indexOf(zbrStr)!=-1){
								xAlert("警告","送办人员包含了主办人，请删除重复数据!","3");
								return;
			 				}
			 				if(sbrStr.indexOf(jsrStr)!=-1){
								xAlert("警告","送办人员包含了部门审核人，请删除重复数据!","3");
								return;
			 				}
			 				if(zbrStr.indexOf(jsrStr)!=-1){
								xAlert("警告","主办人和部门审核人重复，请修改主办人!","3");
								return;
			 				}
			 				$("#jsr").val(jsrString);
			 				var zbrName = getUserNameByAccount(zbrStr);
		 		 			xConfirm("提示信息","是否确认提报？主送人："+zbrName);
							$('#ConfirmYesButton').unbind();
							$('#ConfirmYesButton').one("click",function(){
				 		 		var data = Form2Json.formToJSON(insertForm);
								$(window).manhuaDialog.getParentObj().doConfirmSend(data);
								$(window).manhuaDialog.close();
							});
						}
					}
				});
				//送达按钮事件
				$("#btnSend").click(function(){
					if($("#insertForm").validationButton()){
			 			var zbrStr = $("#zbrName").val()==undefined?"":$("#zbrName").val();
		 				var sbrStr = $("#sbrName").attr("code")==undefined?"":$("#sbrName").attr("code");
		 				var jsrStr = $("#jsrName").val()==undefined?"":$("#jsrName").val();
		 				var jsrString = zbrStr+":"+sbrStr+":"+jsrStr;
		 				$("#jsr").val(jsrString);
			 			var zbrName = getUserNameByAccount(zbrStr);
	 		 			xConfirm("提示信息","是否确认提报？主送人："+zbrName);
						$('#ConfirmYesButton').unbind();
						$('#ConfirmYesButton').one("click",function(){
			 		 		var data = Form2Json.formToJSON(insertForm);
							$(window).manhuaDialog.getParentObj().setYwid($("#ywid").val());
							$(window).manhuaDialog.getParentObj().sendReport(data);
							//$(window).manhuaDialog.getParentObj().doShowMsg("操作成功！");
							$(window).manhuaDialog.close();
						});
					}
				});
				//退回按钮事件
				$("#btnSendBack").click(function(){
					btnNum = 5;
					$(window).manhuaDialog({"title":"问题提报>退回问题","type":"text","content":"${pageContext.request.contextPath}/jsp/business/wttb/pfWt.jsp?infoID="+id,"modal":"4"});
				});
				$("#zbrBtn").click(function(){
					btnNum = 1;
					openUserTree("single",$("#zbrName").attr("code"),"");
				});
				$("#sbrBtn").click(function(){
					btnNum = 2;
					selectUserTree("multi",$("#sbrName").attr("code"),"");
				});
				$("#jsrBtn").click(function(){
					btnNum = 4;
					openUserTree("single","","");
				});
				$("#xmBtn").click(function(){
					btnNum = 3;
					queryProjectWithBD();
				});
				$("#btnCancel").click(function(){
					$(window).manhuaDialog.close();
				});
				$("#btnPrint").click(function(){
					deltr();
					$(this).hide();
					$("textarea").addClass("cancleResize");//打印过程中，隐藏textarea右下脚拖拽图标
					//由于A4纸和页面宽度不同，所以要重新指定文本域高度，防止纸张出现滚动条
					$("#insertForm textarea").each(function(i){
						$(this).attr("style","height:"+this.scrollHeight+"px");
					});
					window.print();
					$("textarea").removeClass("cancleResize");//打印完毕，恢复textarea右下脚拖拽图标
					$(this).show();
					//由于A4纸和页面宽度不同，所以要重新指定文本域高度，防止页面高度过高
					$("#insertForm textarea").each(function(i){
						$(this).attr("style","height:"+this.scrollHeight+"px");
					});
					deltr();
				});
			});
			function doInit(){
				getWtInfo(id);
				getPsnList(id);
				getPfqk(id);
				doSearch();
				$("input").each(function(i){
					//分页的输入框允许使用
					if($(this).attr("id")!="go_DT0"){
						$(this).attr("disabled","true");
					}
				});
				$("textarea").attr("disabled","true");
				$("th").addClass("disabledTh");
			}
			//根据账号获取登陆人姓名
			function getUserNameByAccount(n){
				var str = "";
				$.ajax({
					url:controllername + "?getUserNameByAccount&account="+n,
					data:"",
					dataType:"",
					async:false,
					success:function(result){
						var tempJson = convertJson.string2json1(result);
						str = tempJson.msg;
					}
				});
				return str;
			}
			//----------------------------------
			//-获取问题信息
			//----------------------------------
			function getWtInfo(n){
				$.ajax({
					url:controllername + "?queryInfoByWtid&wtid="+n,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						var res = result.msg;
						var tempJson = convertJson.string2json1(res).response.data[0];
						$("#resultXML").val(JSON.stringify(tempJson));
						$("#insertForm").setFormValues(tempJson);
						$("#insertForm textarea").each(function(i){
							$(this).attr("style","height:"+(this.scrollHeight)+"px");
						});
						insertTable(tempJson,"titleDiv");
					}
				});
			}
			//----------------------------------
			//-插入span信息
			//----------------------------------
			function insertTable(tempJson,tableId){
				var subresultmsgobj = tempJson;
				$("#"+tableId+" span").each(function(i){
					var str = $(this).attr("bzfieldname");
					var valueStr =  "";
					if(str!=''&&str!=undefined){
						if($(subresultmsgobj).attr(str+"_SV")!=undefined){
							valueStr = $(subresultmsgobj).attr(str+"_SV");
							if($(this).attr("decimal")=="false"){
								//不需要小数的项，删掉小数点后的内容
								valueStr = valueStr.substring(0,valueStr.lastIndexOf("."));
							}else if($(this).attr("absl")=="true"){
								//是否要处理掉符号（取绝对值）
								if(valueStr.indexOf("-")==0){
									valueStr = valueStr.replace("-","");
								}
							}
						}else{
							valueStr = $(subresultmsgobj).attr(str);
						}
						if($(this).attr("hasLink")!=undefined&&valueStr!="0"){
							valueStr = '<a href="javascript:void(0)" onclick="showDataDetail(\''+$(this).attr("hasLink")+'\')">'+valueStr+'</a>';
						}
						if($(this).attr("CustomFunction")!=undefined){
							valueStr = eval($(this).attr("CustomFunction")+"("+JSON.stringify(tempJson)+")");
						}
						$(this).html(valueStr);
					}
				});
			}
			function getPfqk(n){
				$.ajax({
					url:controllername + "?queryPfqk&id="+n,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						var res = result.msg;
						var tempJson = convertJson.string2json1(res);
						for(var i=0;i<tempJson.response.data.length;i++){
							var row = tempJson.response.data[i];
							if(row.NRLX=="0"){
								$("#pfnr").html(row.PFNR);
								$("#jyjjfa").html(row.JYJJFA);
								$("#xwwcsj").val(row.XWWCSJ);
							}
						}
					}
				});
			}
			function getPsnList(n){
				$.ajax({
					url:controllername + "?queryLzls&id="+n,
					data:"",
					dataType:"json",
					async:false,
					success:function(result){
						var res = result.msg;
						var tempJson = convertJson.string2json1(res);
						var zbrStr = "";
						var zbrCodeStr = "";
						var sbrStr = "";
						var sbrCodeStr = "";
						var blrStr = "";
						var blrCodeStr = "";
						for(var i=0;i<tempJson.response.data.length;i++){
							var row = tempJson.response.data[i];
							if(row.BLRJS=="1"){
								zbrStr +=row.JSR_SV+",";
								zbrCodeStr +=row.JSR+",";
							}else if(row.BLRJS=="4"){
								sbrStr +=row.JSR_SV+",";
								sbrCodeStr +=row.JSR+",";
							}else if(row.BLRJS=="3"){
								blrStr +=row.JSR_SV+",";
								blrCodeStr +=row.JSR+",";
								//$("#xwwcsj").val(row.XWWCSJ);
							}
						}
						zbrStr = zbrStr.length==0?"":zbrStr.substring(0,zbrStr.length-1);
						zbrCodeStr = zbrCodeStr.length==0?"":zbrCodeStr.substring(0,zbrCodeStr.length-1);
						sbrStr = sbrStr.length==0?"":sbrStr.substring(0,sbrStr.length-1);
						sbrCodeStr = sbrCodeStr.length==0?"":sbrCodeStr.substring(0,sbrCodeStr.length-1);
						blrStr = blrStr.length==0?"":blrStr.substring(0,blrStr.length-1);
						blrCodeStr = blrCodeStr.length==0?"":blrCodeStr.substring(0,blrCodeStr.length-1);
						//$("#zbrName").val(zbrStr);//由于改用了下拉框，所以赋值使用code，不能用直接用value
						$("#zbrName").val(zbrCodeStr);
						$("#zbrName").attr("code",zbrCodeStr);
						$("#sbrName").val(sbrStr);
						$("#sbrName").attr("code",sbrCodeStr);
						//$("#jsrName").val(blrStr);
						$("#jsrName").val(blrCodeStr);
						$("#jsrName").attr("code",blrCodeStr);
					}
				});
			}
			function getWinData(rowsArr){
				if(btnNum==1){
					var jsrStr = "";
					var jsdwStr = "";
					var jsrName = "";
					for(var i=0;i<rowsArr.length;i++){
						var tempJson = rowsArr[i];
						jsrStr +=tempJson.ACCOUNT+",";
						jsdwStr +=tempJson.DEPTID+",";
						jsrName +=tempJson.USERNAME+",";
					}
					$("#zbrName").attr("code",jsrStr.substring(0,jsrStr.length-1));//为字段的code赋值
					$("#zbrName").val(jsrName.substring(0,jsrName.length-1));
				}else if(btnNum==2){
					var jsrStr = "";
					var jsdwStr = "";
					var jsrName = "";
					for(var i=0;i<rowsArr.length;i++){
						var tempJson = rowsArr[i];
						jsrStr +=tempJson.ACCOUNT+",";
						jsdwStr +=tempJson.DEPTID+",";
						jsrName +=tempJson.USERNAME+",";
					}
					$("#sbrName").attr("code",jsrStr.substring(0,jsrStr.length-1));//为字段的code赋值
					$("#sbrName").val(jsrName.substring(0,jsrName.length-1));
				}else if(btnNum==3){
					var data = eval("("+rowsArr+")");
					$("#xmmc").val(data.XMMC);
					$("#bdmc").val(data.BDMC);
					$("#jhsjid").val(data.GC_JH_SJ_ID);
					$("#xmid").val(data.XMID);
					$("#bdid").val(data.BDID);
				}else if(btnNum==4){
					var jsrStr = "";
					var jsdwStr = "";
					var jsrName = "";
					for(var i=0;i<rowsArr.length;i++){
						var tempJson = rowsArr[i];
						jsrStr +=tempJson.ACCOUNT+",";
						jsdwStr +=tempJson.DEPTID+",";
						jsrName +=tempJson.USERNAME+",";
					}
					$("#jsrName").attr("code",jsrStr.substring(0,jsrStr.length-1));//为字段的code赋值
					$("#jsrName").val(jsrName.substring(0,jsrName.length-1));
				}
			}
			//回复按钮--回调函数
			function doReply(data){
				var data1 = defaultJson.packSaveJson(data);
				$.ajax({
					url:controllername + "?doSendBack&ywid="+$("#ywid").val(),
					data:data1,
					dataType:"json",
					async:false,
					success:function(result){
					}
				});
			}
			//设置业务ID
			function setYwid(n){
				$("#ywid").val(n);
			}
			//子页面关闭前，对全局变量进行赋值，用于控制父页面的事件
			function setChildBtnNum(n){
				childBtnNum = btnNum*10+n;
			}
			//查询批复情况列表
			function doSearch(){
				var data = combineQuery.getQueryCombineData(queryForm0,frmPost,DT0);
				defaultJson.doQueryJsonList(controllername + "?queryPfqk&id="+id+"&flag=2",data,DT0,null,true);
			}
			//----------------------------------------
			//-隐藏批复内容
			//----------------------------------------
			function hideJjfa(obj){
				if(obj.BLRJS=="3" || ((obj.BLRJS=="2"||obj.BLRJS=="4") && obj.JYJJFA=="")){
					return '<div style="text-align:center;">—</div>';
				}
			}
			function doRandering(obj){
				var data = JSON.stringify(obj);
				var data1 = defaultJson.packSaveJson(data);
				var showHtml = "";
				$.ajax({
					url:controllername + "?getColor",
					data:data1,
					dataType:"json",
					async:false,
					success:function(result){
						var res = result.msg;
						if(obj.SJZT=="3" || obj.SJZT=="6"){
							showHtml = '<span style="width:50px;">完&nbsp;结</span>';
						}else{
							if(res=="none"){
								showHtml = '<span  style="width:50px;">正&nbsp;常</span>';
							}else{
								tempJson = convertJson.string2json1(res);
								var titleMsg = tempJson.response.data[0].TITLE;
								var labelMsg = tempJson.response.data[0].CLASS;
								if(labelMsg=="label-info"){
									showHtml = '<span style="width:50px;color:blue">'+titleMsg+'</span>';
								}else if(labelMsg=="label-warning"){
									showHtml = '<span style="width:50px;color:#f89406">'+titleMsg+'</span>';
								}else if(labelMsg=="label-danger"){
									showHtml = '<span style="width:50px;color:red">'+titleMsg+'</span>';
								}
							}
						}
					}
				});
				return showHtml;
			}
			//打印隐藏
			function deltr(){
		        var delID = document.getElementById("delID");
		        var target_=delID.cellIndex-1;
		        var table = document.getElementById("DT0");
		        $("#delID").toggle();
		        var len = table.rows.length;
		        for(var i = 0;i < len;i++){
		          /*  $("td:eq("+target_+")",$("tr")).hide();  */ 
		           $('#DT0').find('tr:eq(' + i + ')').find('td:eq(' +target_ + ')').toggle();
		        } 
		    }
		</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid" style="text-align:center">
				
				<div class="span1"></div>
				<div class="span10">
				<div class="B-small-from-table-autoConcise" style="min-width:960px;">
					<span class="pull-right" id="btnSpan">
						<button id="btnPrint" class="btn btn-link" type="button">
							<i class="icon-print"></i>打印
						</button>
					</span>
					<div style="text-align:left;" id="titleDiv">
						<span class="titleSpan">问题性质：</span><span class="contentSpan" bzfieldname="WTXZ"></span>&nbsp;&nbsp;&nbsp;&nbsp;
						<span class="titleSpan">问题类型：</span><span class="contentSpan" bzfieldname="WTLX"></span>&nbsp;&nbsp;&nbsp;&nbsp;
						<span class="titleSpan">问题状态：</span><span class="contentSpan" bzfieldname="SJZT"></span>&nbsp;&nbsp;&nbsp;&nbsp;
						<span class="titleSpan">办理情况：</span><span class="contentSpan" bzfieldname="SJZT" CustomFunction="doRandering"></span>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					<h3 class="title">
						问题处理单
					</h3>
					<form method="post" action="" id="insertForm">
						<table class="B-table" width="98%">
							<!-- 这里需要一个隐藏域，存放比如：问题编号,接受人账号，接受单位等信息 -->
							<tr style="display: none;">
								<th width="8%" class="right-border bottom-border">
									问题编号
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="WTTB_INFO_ID" fieldname="WTTB_INFO_ID"
										id="wtid">
								</td>
								<th width="8%" class="right-border bottom-border">
									问题状态
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="SJZT" fieldname="SJZT"
										id="sjzt">
								</td>
								<th width="8%" class="right-border bottom-border">
									业务类型
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="YWLX" fieldname="YWLX"
										id="ywlx">
								</td>
								<th width="8%" class="right-border bottom-border">
									批复ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="WTTB_PFQK_ID" fieldname="WTTB_PFQK_ID"
										id="pfid">
								</td>
								<th width="8%" class="right-border bottom-border">
									接收人
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="JSR" fieldname="JSR" id="jsr">
								</td>
								<th width="8%" class="right-border bottom-border">
									计划数据ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="JHSJID" roattr="1"
										fieldname="JHSJID" id="jhsjid">
								</td>
								<th width="8%" class="right-border bottom-border">
									项目ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="XMID" roattr="1"
										fieldname="XMID" id="xmid">
								</td>
								<th width="8%" class="right-border bottom-border">
									标段ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="BDID" roattr="1"
										fieldname="BDID" id="bdid">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									抄送人
								</th>
								<td width="42%" colspan=3 class="bottom-border">
									<input class="span12"  type="text" id="sbrName" roattr="9" disabled style="width:90%">
									<button class="btn btn-link"  type="button" id="sbrBtn" title="选择抄送人"><i class="icon-edit"></i></button>
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									问题类别
								</th>
								<td width="17%" class="bottom-border">
									<input type="text" class="span12 5characters" name="WTLX" fieldname="WTLX" kind="dic"
										src="WTLX" roattr="1">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									问题性质
								</th>
								<td width="17%" class="bottom-border">
									<select class="span12 6characters" name="WTXZ" fieldname="WTXZ" kind="dic"
										src="WTXZ" roattr="1">
									</select>
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									处理建议
								</th>
								<td width="42%" colspan=3 class="bottom-border">
									<textarea class="span12" rows=6 name="JYJJFA" fieldname="JYJJFA"
										roattr="1" id="jyjjfa"></textarea>
								</td> 
							</tr>
							<tr>
								<th width="15%" class="right-border bottom-border text-right">
									发起人
								</th>
								<td width="35%" class="right-border bottom-border">
									<input class="span12" type="text" name="LRR" fieldname="LRR" kind="dic" src="T#FS_ORG_PERSON P:ACCPUNT:NAME:">
								</td>
								<th width="15%" class="right-border bottom-border text-right" id="zbrTh">
									发起部门
								</th>
								<td width="35%" class="bottom-border">
									<input class="span12" roattr="9" fieldname="LRBM" type="text"
										kind="dic" src="T#VIEW_YW_ORG_DEPT D:ROW_ID:DEPT_NAME: ">
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									发起时间
								</th>
								<td class="right-border bottom-border">
									<input class="span12" type="text" kind="date" name="LRSJ"
										fieldname="LRSJ" check-type="required" >
								</td>
								<th class="right-border bottom-border text-right">
									希望解决时间
								</th>
								<td class="right-border bottom-border">
									<input class="span12" type="text" kind="date" name="YJSJ"
										fieldname="YJSJ">
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									问题标题
								</th>
								<td colspan=3 class="right-border bottom-border">
									<input class="span12" type="text" name="WTBT" roattr="1"
										fieldname="WTBT" placeholder="必填" check-type="required">
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									涉及项目
								</th>
								<td colspan=3 class="right-border bottom-border">
									<input class="span12" type="text" name="XMMC" roattr="1"
										fieldname="XMMC">
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									涉及标段
								</th>
								<td colspan=3 class="right-border bottom-border">
									<input class="span12" type="text" name="BCMC" roattr="1"
										fieldname="BCMC">
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									问题描述
								</th>
								<td colspan=3 class="bottom-border">
									<textarea class="span12" rows=4 id="pfnr" name="PFNR" fieldname="PFNR"
										roattr="1"></textarea>
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									主办人意见
								</th>
								<td colspan=3 class="bottom-border">
									<div class="span12">
										<textarea rows=4 type="text" class="span12" fieldname="ZBRYJ"></textarea>
									</div>
									<div class="span12" style="text-align:right;margin:0px;">
										主办人:<input style="width:50px;padding:0px 0px 3px 1px" type="text" fieldname="ZBR">
									</div>
								</td>
							</tr>
						</table>
					</form>
					<h4 style="text-align:left;font-size:15px;">
						处理信息
					</h4>
					<form method="post" action="" id="queryForm0">
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
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<table width="100%" class="table-hover table-activeTd B-table"
						type="single" id="DT0" pageNum="1000" noPage="true">
						<thead>
							<tr>
								<th name="XH" id="_XH">
									&nbsp;#&nbsp;
								</th>
								<th fieldname="BLRJS" tdalign="center">
									&nbsp;人员角色&nbsp;
								</th>
								<th fieldname="JSR">
									&nbsp;姓名&nbsp;
								</th>
								<th fieldname="PFNR" width="60%" tdalign="left">
									&nbsp;意见&nbsp;
								</th>
								<th fieldname="XWWCSJ" >
									&nbsp;希望解决时间&nbsp;
								</th>
								<th fieldname="PFSJ">
									&nbsp;实际解决日期&nbsp;
								</th>
								<th fieldname="FJ" id="delID" >
									&nbsp;附件&nbsp;
								</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				</div>
			</div>
			<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
		</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="Z.XMID,Z.PXH" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="ywid" id="ywid">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>
