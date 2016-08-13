<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@page import="com.ccthanking.framework.Globals"%>
		<%@page import="com.ccthanking.framework.common.User"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%
			User user = (User) request.getSession().getAttribute(
					Globals.USER_KEY);
			String userName = user.getName();
			String userAccount = user.getAccount();
		%>
		<app:base />
		<title>排迁项目维护</title>
		<style>
		.myCellSpan{
			background-color:#FF8888 ;
			width:100%;
			height:100%;
			display:inline-block;
			margin:0;
			padding:0;
		}
		</style>
		<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
		<script type="text/javascript" charset="utf-8">
			var controllername= "${pageContext.request.contextPath }/pqgl/pqglController.do";
			g_bAlertWhenNoResult = false;
			g_nameMaxlength = 20;
			var userName = '<%=userName%>';
			var userAccount = '<%=userAccount%>';
			$(function(){
				doInit();
				var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
				showAutoComplete("QXMMC",autocompleteXmmcController+"?xmmcAutoQuery","getXmmcQueryCondition");
				//按钮绑定事件（查询）
				$("#btnQuery").click(function() {
					if($("#queryForm").validationButton()){
						g_bAlertWhenNoResult = true;
						var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
						defaultJson.doQueryJsonList(controllername+"?queryPqInfo",data,DT1,null,false);
					}
				});
				$("#btnSave").click(function(){
					if($("#insertForm").validationButton()){
		 				//生成json串
		 		 		var data = Form2Json.formToJSON(insertForm);
						if($("#zxmid").val()!=undefined && $("#zxmid").val()!=""){
							var data1 = defaultJson.packSaveJson(data);
							defaultJson.doUpdateJson(controllername + "?updatePqzxm",data1,DT1,null);
						}else{
							var data1 = defaultJson.packSaveJson(data);
							defaultJson.doInsertJson(controllername + "?doZxmgl&ywid="+$("#ywid").val(),data1,DT1,null);
							$("#insertForm").clearFormResult();
							clearFileTab();
							$("#ywid").val("");
							//$(window).manhuaDialog.getParentObj().insertTable($("#resultXML").val());
						}
						$(window).manhuaDialog.setData($("#DT1"));
						$(window).manhuaDialog.sendData();
						//$(window).manhuaDialog.close();
					}
				});
				$("#btnAdd").click(function(){
					$("#insertForm").clearFormResult();
					clearFileTab();
					$("#ywid").val("");
				});
				$("#xmBtn").click(function(){
					queryProjectWithBD();
				});
				//按钮绑定事件（清空）
			    $("#btnClear").click(function() {
			        $("#queryForm").clearFormResult();
					generateJhNdMc($("#QueryND"),$("#QXMMC"));
			    });
				$("#btnCancel").click(function(){
					$(window).manhuaDialog.close();
				});
				$("#btnDel").click(function(){
					if($("#DT1").getSelectedRowIndex()==-1){
						requireSelectedOneRow();
						return;
					}else{
						var obj = $("#DT1").getSelectedRowJsonObj();
						var sfwth = obj.SSRQ;
						if(sfwth!=""){
							xAlert("警告","内业人员已经对该数据进行了操作，不允许删除！","3");
						}else{
			 		 		var data = Form2Json.formToJSON(insertForm);
							var data1 = defaultJson.packSaveJson(data);
							xConfirm("提示信息","是否确认删除！");
							$('#ConfirmYesButton').unbind();
							$('#ConfirmYesButton').one("click",function(){  
								defaultJson.doDeleteJson(controllername+"?deletePqzxm",data1,DT1,null); 
								$("#insertForm").clearFormResult();
								clearFileTab();
								$("#ywid").val("");
							});
						}
					}
				});
				$("#btnFk").click(function() {
					if($("#DT1").getSelectedRowIndex()==-1){
						requireSelectedOneRow();
						return;
					}else{
						btnNum = 0;
						$("#resultXML").val($("#DT1").getSelectedRow());
				        $(window).manhuaDialog({"title":"排迁任务管理>进展管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/pqgl/pqxmgl/pqJzfk.jsp","modal":"1"});
			    	}
			    });
				$(":radio[name='SFWTH']").click(function(){
					ctrlWth();
				});
			});
			function doInit(){
				//获取父页面iframe中隐藏域的值
				var rowValue = $(parent.frames["menuiframe"].document).find("#resultXML").val();
				//字符串转JSON对象
				$("#xmmc").attr("disabled",true);
				$("#bdmc").attr("disabled",true);
				$("#jhwcsj").attr("disabled",true);
				$("#xmfzr").attr("disabled",true);
				$("#xmfzr").val(userName);
				$("#xmfzr").attr("code",userAccount);
				//查询
				$("#q_xmfzr").val(userAccount);
				generateJhNdMc($("#QueryND"),$("#QXMMC"));
				//控制 项目选择按钮
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				defaultJson.doQueryJsonList(controllername+"?queryPqInfo",data,DT1,null,true);
			}
			function doJzgl(data){
				var index =	$("#DT1").getSelectedRowIndex();
				var rowValue = $("#DT1").getSelectedRow();
				var tempJson = convertJson.string2json1(rowValue);//字符串转JSON对象
				var resJson = convertJson.string2json1(data);
				resJson.GC_PQ_ZXM_ID=resJson.ZXMID;
				var data1 = defaultJson.packSaveJson(JSON.stringify(resJson));
				defaultJson.doUpdateJson(controllername + "?queryPqzxmByPk", data1,DT1,null);
			}
			function getWinData(data){
				var tempJson = eval("("+data+")");
				$("#xmmc").val(tempJson.XMMC);
				$("#bdmc").val(tempJson.BDMC);
				$("#xmid").val(tempJson.XMID);
				$("#bdid").val(tempJson.BDID);
				$("#jhid").val(tempJson.JHID);
				$("#jhsjid").val(tempJson.GC_JH_SJ_ID);
				$("#nd").val(tempJson.ND);
			}
			
			function tr_click(obj,tabId){
				var rowValue = $("#"+tabId).getSelectedRow();//获得选中行的json 字符串
				var tempJson = convertJson.string2json1(rowValue);//字符串转JSON对象
				//控制是否委托函默认值
				if(tempJson.SFWTH==""){
					tempJson.SFWTH="0"
				}
				$("#insertForm").setFormValues(tempJson);
				deleteFileData(tempJson.GC_PQ_ZXM_ID,"","","");
				setFileData(tempJson.GC_PQ_ZXM_ID,tempJson.JHID,tempJson.SJBH,tempJson.YWLX,"0");
				queryFileData(tempJson.GC_PQ_ZXM_ID,"","","");
				//控制委托函编号样式
				ctrlWth();
				/**
				var sfwth = obj.SFWTH;
				if(sfwth!=""){
					$("#gczjTH").addClass("disabledTh");
					$("#gczj").attr("disabled","true");
				}else{
					$("#gczjTH").removeClass("disabledTh");
					$("#gczj").removeAttr("disabled");
				}
				*/
			}
			/**
			 * 业务通用年度和计划名称的级联查询
			 * ndObj 年度的jquery对象
			 * mcObj 计划名称jquery对象
			 */
			function generateJhNdMc(ndObj,mcObj){
				ndObj.attr("src","T#GC_JH_SJ a,GC_PQ_ZXM b: distinct a.ND:a.ND:a.SFYX='1' and b.SFYX='1' and a.gc_jh_sj_id=b.jhsjid order by a.ND");
				ndObj.attr("kind","dic");
				ndObj.html('');
				reloadSelectTableDic(ndObj);
				//ndObj.val(new Date().getFullYear());
				//setDefaultOption(ndObj,new Date().getFullYear());
				setDefaultNd("QueryND");
				if(mcObj){
					mcObj.attr("kind","dic");
					mcObj.attr("src","T#GC_JH_ZT:GC_JH_ZT_ID:jhpch:SFYX='1' AND ND='" + ndObj.val()+"' and isxf = '1'");
					mcObj.html('');
					reloadSelectTableDic(mcObj);
					ndObj.change(function() {
						mcObj.html('');
						if(!ndObj.val().length){
						}
						mcObj.attr("src","T#GC_JH_ZT:GC_JH_ZT_ID:jhpch:SFYX='1' AND ND='" + ndObj.val()+"' and isxf = '1'");
						reloadSelectTableDic(mcObj);
					});
				}
			}
			function getXmmcQueryCondition(){
				var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":"S.pxh"}]}';
				var jsonData = eval('(' + initData + ')'); 
				//项目名称
				if("" != $("#QXMMC").val()){
					var defineCondition = {"value": "%"+$("#QXMMC").val()+"%","fieldname":"S.XMMC","operation":"like","logic":"and"};
					jsonData.querycondition.conditions.push(defineCondition);
				}
				return JSON.stringify(jsonData);
			}
			function doRandering(obj){
				var showHtml = "";
				if(obj.ISJHWC=="1"){
					showHtml = obj.ISJHWC_SV;
				}else if(obj.ISJHWC=="0"){
					showHtml = '<span class="myCellSpan" frame="box">'+obj.ISJHWC_SV+'</span>';
				}else{
					showHtml = '<span class="label"></span>';
				}
				return showHtml;
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
			//---------------------------------------
			//控制未付合同款的颜色，复数为红色，其他为黑色
			//---------------------------------------
			function showWfhtk(obj){
				var Reg = /^-\d*$/;// 匹配负数
				if(Reg.test(obj.WFHTK)){
					return "<span style='color:red;'>"+obj.WFHTK_SV+"</span>";
				}else{
					return obj.WFHTK_SV;
				}
			}
			//---------------------------------------
			//控制委托函相关样式
			//---------------------------------------
			function ctrlWth(){
				$("#insertForm :radio[name='SFWTH']").each(function(){
					if($(this).is(':checked')){
						if($(this).attr("value")=="1"){
							$("#wthbh").removeAttr("disabled");
							$(".wthbhTH").removeClass("disabledTh");
							$("#wthBtn").show();
				 			//$("#wthbh").attr("placeholder","必填");
				 			//$("#wthbh").attr("check-type","required" );
						}else{
							$("#wthbh").val("");
							$(":radio[name='SFWTH'][value='0']").attr("checked", true);
							$("#wthbh").attr("disabled","true");
							$(".wthbhTH").addClass("disabledTh");
							$("#wthBtn").hide();
				 			//$("#wthbh").removeAttr("placeholder");
				 			//$("#wthbh").removeAttr("check-type");
						}
					}
				});
			}
		</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
								</TD>
								<th width="5%" class="right-border bottom-border text-right">
									项目负责人
								</th>
								<td class="right-border bottom-border" width="15%">
									<input class="span12" type="text" name="XMFZR" keep="true"
										fieldname="Z.XMFZR" operation="=" id="q_xmfzr">
								</td>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
							<tr>
								<th width="3%" class="right-border bottom-border text-right">年度</th>
								<td class="right-border bottom-border" width="7%">
									<select class="span12" id="QueryND" name="QueryND"
										fieldname="S.ND" operation="=" defaultMemo="全部">
									</select>
								</td>
								<th width="6%" class="right-border bottom-border text-right">
									项目名称
								</th>
								<td class="right-border bottom-border" width="25%">
									<input class="span12" type="text" placeholder="" name="QXMMC"
										fieldname="S.XMMC" operation="like" id="QXMMC"
										autocomplete="off" tablePrefix="S">
								</td>
								<th width="6%" class="right-border bottom-border text-right">
									任务名称
								</th>
								<td class="right-border bottom-border" width="25%">
									<input class="span12" type="text" placeholder="" name="RWMC"
										fieldname="Z.ZXMMC" operation="like" id="rwmc"
										autocomplete="off" tablePrefix="Z">
								</td>
								<th width="6%" class="right-border bottom-border text-right">
									管线类别
								</th>
								<td class="right-border bottom-border" width="5%">
									<select class="span12 3characters" name="GXLB" fieldname="GXLB" 
										 kind="dic" src="GXLB" operation="=" placeholder="" defaultMemo="全部">
									</select>
								</td>
								<td class="right-border bottom-border text-right">
									<button id="btnQuery" class="btn btn-link" type="button">
										<i class="icon-search"></i>查询
									</button>
									<button id="btnClear" class="btn btn-link" type="button">
										<i class="icon-trash"></i>清空
									</button>
								</td>
							</tr>
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="DT1"
							width="100%" type="single" pageNum="5">
							<thead>
								<tr>
									<th name="XH" id="_XH" rowspan="2" colindex=1>
										&nbsp;#&nbsp;
									</th>
									<th fieldname="XMBH" maxlength="15" rowspan="2" colindex=2 rowMerge="true" tdalign="left">
										&nbsp;项目编号&nbsp;
									</th>
									<th fieldname="XMMC" rowspan="2" colindex=3  tdalign="center" rowMerge="true"
										style="width: 20%" maxlength="15">
										&nbsp;项目名称&nbsp;
									</th>
									<th fieldname="BDBH" rowspan="2" colindex=4 style="width: 20%" 
										maxlength="10" Customfunction="doBdbh">
										&nbsp;标段编号&nbsp;
									</th>
									<th fieldname="BDMC" rowspan="2" colindex=5 style="width: 20%" 
										maxlength="10" Customfunction="doBdmc">
										&nbsp;标段名称&nbsp;
									</th>
									<th fieldname="BDDD" rowspan="2" colindex=6
										style="width: 300px" maxlength="10">
										&nbsp;标段地点&nbsp;
									</th>
									<th fieldname="GXLB" rowspan="2" colindex=7 tdalign ="center">
										&nbsp;管线类别&nbsp;
									</th>
									<th  fieldname="ZXMMC" rowspan="2" colindex=8>
										&nbsp;排迁任务名称&nbsp;
									</th>
									<th  fieldname="PQDD" rowspan="2" colindex=9 maxlength="15">
										&nbsp;排迁地点&nbsp;
									</th>
									<th fieldname="PQFA" rowspan="2" colindex=10 maxlength="15">
										&nbsp;排迁方案&nbsp;
									</th>
									<th fieldname="ISJHWC" rowspan="2" colindex=11 tdalign="center" CustomFunction="doRandering">
										&nbsp;是否能按&nbsp;<br/>&nbsp;计划完成&nbsp;
									</th>
									<th colspan=2>
										&nbsp;开工时间&nbsp;
									</th>
									<th colspan=2>
										&nbsp;完工时间&nbsp;
									</th>
									<th fieldname="JZQK" rowspan="2" colindex=16 maxlength="15">
										&nbsp;进展情况&nbsp;
									</th>
									<th fieldname="SYGZL" rowspan="2" colindex=17 maxlength="15">
										&nbsp;剩余工作量&nbsp;
									</th>
									<th fieldname="CZWT" rowspan="2" colindex=18 maxlength="15">
										&nbsp;存在问题&nbsp;
									</th>
									<th fieldname="JJFA" rowspan="2" colindex=19 maxlength="15">
										&nbsp;解决方案&nbsp;
									</th>
									<th fieldname="ZYGXPQT" rowspan="2" colindex=20>
										&nbsp;专业管线排迁图&nbsp;
									</th>
									<th fieldname="PQLLD" rowspan="2" colindex=21>
										&nbsp;排迁联络单&nbsp;
									</th>
									<th colspan=5>
										&nbsp;内业情况&nbsp;
									</th>
									<th colspan=7 tdalign ="center">
										&nbsp;合同情况&nbsp;
									</th>
								</tr>
								<tr>
									<th fieldname="KGSJ_JH" colindex=12 type="date" tdalign="center">
										&nbsp;计划&nbsp;
									</th>
									<th fieldname="KGSJ" colindex=13 type="date" tdalign="center">
										&nbsp;实际&nbsp;
									</th>
									<th fieldname="WCSJ_JH" colindex=14 type="date" tdalign="center">
										&nbsp;计划&nbsp;
									</th>
									<th fieldname="WCSJ" colindex=15 type="date" tdalign="center">
										&nbsp;实际&nbsp;
									</th>
									<th fieldname="YSSSD" colindex=22>
										&nbsp;预算送审单&nbsp;
									</th>
									<th fieldname="GCYSSDB" colindex=23>
										&nbsp;工程预算审定表&nbsp;
									</th>
									<th fieldname="WTH" colindex=24>
										&nbsp;委托函&nbsp;
									</th>
									<th fieldname="SSZ" colindex=25 tdalign ="right">
										&nbsp;工程造价（元）&nbsp;
									</th>
									<th fieldname="SDZ" colindex=26 tdalign ="right">
										&nbsp;审定值（元）&nbsp;
									</th>
									<th fieldname="HTBM" colindex=27>
										&nbsp;合同编号&nbsp;
									</th>
									<th fieldname="HTSX" colindex=28 tdalign ="center">
										&nbsp;合同属性&nbsp;
									</th>
									<th fieldname="HTQDJ" colindex=29 tdalign ="right">
										&nbsp;合同签订价（元）&nbsp;
									</th>
									<th fieldname="SDZ" colindex=30 tdalign ="right">
										&nbsp;合同结算价（元）&nbsp;
									</th>
									<th fieldname="HTZF" colindex=31 tdalign ="right">
										&nbsp;已拨付合同款（元）&nbsp;
									</th>
									<th fieldname="WFHTK" colindex=32 tdalign ="right" CustomFunction="showWfhtk">
										&nbsp;未付合同款（元）&nbsp;
									</th>
									<th fieldname="HTJQDRQ" colindex=33 tdalign ="center">
										&nbsp;签订日期&nbsp;
									</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<h4 class="title">
						排迁任务信息
						<span class="pull-right">
							<button id="btnSave" class="btn" type="button">
								保存
							</button>
							<button id="btnFk" class="btn" type="button">
								进展管理
							</button>
							<button id="btnDel" class="btn" type="button">
								删除
							</button>
						</span>
					</h4>
					<form method="post" action="" id="insertForm">
						<table class="B-table" width="100%">
							<!-- 这里需要一个隐藏域，存放比如：问题编号,接收人账号，接收单位等信息 -->
							<tr style="display: none;">
								<th width="8%" class="right-border bottom-border">
									主键
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="GC_PQ_ZXM_ID" fieldname="GC_PQ_ZXM_ID" id="zxmid">
								</td>
								<th width="8%" class="right-border bottom-border">
									项目ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="XMID" fieldname="XMID" id="xmid">
								</td>
								<th width="8%" class="right-border bottom-border">
									标段ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="BDID" fieldname="BDID" id="bdid">
								</td>
								<th width="8%" class="right-border bottom-border">
									计划ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="JHID" fieldname="JHID" id="jhid">
								</td>
								<th width="8%" class="right-border bottom-border">
									计划数据ID
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="JHSJID" fieldname="JHSJID" id="jhsjid">
								</td>
								<th width="8%" class="right-border bottom-border">
									年度
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="ND" fieldname="ND" id="nd">
								</td>
								<th width="8%" class="right-border bottom-border">
									事件编号
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="SJBH" fieldname="SJBH">
								</td>
								<th width="8%" class="right-border bottom-border">
									业务类型
								</th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="YWLX" fieldname="YWLX">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									项目名称
								</th>
								<td width="42%" class="right-border bottom-border" colspan=3>
									<input class="span12" type="text" name="XMMC" id="xmmc" 
										fieldname="XMMC" placeholder="必填" check-type="required maxlength">
									</td>
								<th width="8%" class="right-border bottom-border text-right">
									标段名称
								</th>
								<td width="42%" class="bottom-border" colspan=3>
									<input class="span12" type="text" name="BDMC" id="bdmc"
										fieldname="BDMC">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									排迁任务名称
								</th>
								<td colspan=3 class="bottom-border">
									<textarea class="span12" rows="1" name="ZXMMC"
										placeholder="必填" check-type="required maxlength" maxlength="300" fieldname="ZXMMC" ></textarea>
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									管线类别
								</th>
								<td width="17%" class="right-border bottom-border">
									<select class="span12 3characters" name="GXLB" fieldname="GXLB"
										 kind="dic" src="GXLB">
									</select>
								</td>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									归档号
								</th>
								<td width="17%" class="bottom-border">
									<input class="span12" type="text" name="PXH" fieldname="PXH"
										placeholder="必填" check-type="required" disabled>
								</td>
							</tr>
							<tr>
								
								<th width="8%" class="right-border bottom-border text-right">
									排迁地点
								</th>
								<td colspan=5 class="bottom-border">
									<textarea class="span12" rows="1" name="PQDD"
										check-type="maxlength" maxlength="300" fieldname="PQDD" ></textarea>
								</td>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									项目负责人
								</th>
								<td width="17%" class="bottom-border">
									<input class="span12" type="text" name="XMFZR" id="xmfzr" keep="true"
										fieldname="XMFZR">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right disabledTh">
									统筹计划时间
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12 date" type="date" name="WGSJ" id="jhwcsj"
										fieldname="WGSJ">
								</td>
								<td width="75%" colspan=6 class="bottom-border">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									计划开工时间
								</th>
								<td class="right-border bottom-border">
									<input class="span12 date" type="date" name="KGSJ_JH" 
										fieldname="KGSJ_JH">
								</td>
								<th class="right-border bottom-border text-right">
									计划完工时间
								</th>
								<td class="bottom-border">
									<input class="span12 date" type="date" name="WCSJ_JH" 
										fieldname="WCSJ_JH">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									能否按计划完成
								</th>
								<td class="right-border bottom-border">
									<select class="span12 3characters" name="ISJHWC" fieldname="ISJHWC"
										 kind="dic" src="SF">
									</select>
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									排迁联络单编号
								</th>
								<td width="17%" class="bottom-border">
									<input class="span12" type="text" name="LDBH" 
										fieldname="LLDBH">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									实际开工时间
								</th>
								<td class="right-border bottom-border">
									<input class="span12 date" type="date" name="KGSJ" 
										fieldname="KGSJ">
								</td>
								<th class="right-border bottom-border text-right">
									实际完工时间
								</th>
								<td class="bottom-border">
									<input class="span12 date" type="date" name="WCSJ" 
										fieldname="WCSJ">
								</td>
								<th class="right-border bottom-border text-right" id="gczjTH">
									工程造价
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span9" type="number" name="SSZ" style="text-align:right;" 
										fieldname="SSZ" id="gczj">&nbsp;&nbsp;<b>(元)</b>
								</td>
								<th class="right-border bottom-border text-right">
									送审单编号
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12" type="text" name="SSDBH" 
										fieldname="SSDBH">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									是否需要委托函
								</th>
								<td width="17%" class="right-border bottom-border">
									<input type="radio" kind="dic" src="SF" fieldname="SFWTH" name="SFWTH">
								</td>
								<th class="right-border bottom-border text-right wthbhTH">
									委托函编号
								</th>
								<td width="17%" class="right-border bottom-border">
									<input class="span12" type="text" name="WTHBH" 
										fieldname="WTHBH" id="wthbh">
								</td>
								<td width="50%" colspan=4 class="right-border bottom-border">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									排迁方案
								</th>
								<td width="42%" colspan=7 class="bottom-border">
									<textarea class="span12" rows="3" name="PQFA"
										check-type="maxlength" maxlength="1000" fieldname="PQFA" ></textarea>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									委托函
								</th>
								<td width="92%" colspan=7 class="bottom-border">
									<div>
										<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0005" id="wthBtn">
											<i class="icon-plus"></i>
											<span>添加文件...</span>
										</span>
										<table role="presentation" class="table table-striped">
											<tbody fjlb="0005" class="files showFileTab"
												data-toggle="modal-gallery" data-target="#modal-gallery">
											</tbody>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									专业管线排迁图
								</th>
								<td width="92%" colspan=7 class="bottom-border">
									<div>
										<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0001">
											<i class="icon-plus"></i>
											<span>添加文件...</span>
										</span>
										<table role="presentation" class="table table-striped">
											<tbody fjlb="0001" class="files showFileTab" 
												data-toggle="modal-gallery" data-target="#modal-gallery">
											</tbody>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									排迁联络单
								</th>
								<td width="92%" colspan=7 class="bottom-border">
									<div>
										<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0002">
											<i class="icon-plus"></i>
											<span>添加文件...</span>
										</span>
										<table role="presentation" class="table table-striped">
											<tbody fjlb="0002" class="files showFileTab"
												data-toggle="modal-gallery" data-target="#modal-gallery">
											</tbody>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									备注
								</th>
								<td width="42%" colspan=7 class="bottom-border">
									<textarea class="span12" rows="3" name="BZ"
										check-type="maxlength" maxlength="4000" fieldname="BZ"></textarea>
								</td>
						</table>
					</form>
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
