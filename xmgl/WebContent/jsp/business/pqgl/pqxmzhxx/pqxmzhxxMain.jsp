<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>排迁任务综合信息</title>
		<%
			String defQuery = request.getParameter("defQuery");
		%>
		<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
		<script type="text/javascript" charset="utf-8">
			//请求路径，对应后台RequestMapping
			var controllername= "${pageContext.request.contextPath }/pqgl/pqglController.do";
			g_bAlertWhenNoResult = false;
			var defQuery = '<%=defQuery%>';
			var objRow;
			//页面初始化
			$(function() {
				init();
				var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
				showAutoComplete("QXMMC",autocompleteXmmcController+"?xmmcAutoQuery","getXmmcQueryCondition");
				//按钮绑定事件（查询）
				$("#btnQuery").click(function() {
					if($("#queryForm").validationButton()){
						var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
						defaultJson.doQueryJsonList(controllername+"?queryPqInfo",data,DT1,null,false);
					}
				});
				//按钮绑定事件(内业情况)
				$("#btnSave").click(function() {
					if($("#DT1").getSelectedRowIndex()==-1){
						requireSelectedOneRow();
						return;
					}else{
						$("#resultXML").val($("#DT1").getSelectedRow());
						$(window).manhuaDialog({"title":"排迁任务综合信息>内业情况","type":"text","content":"${pageContext.request.contextPath}/jsp/business/pqgl/pqxmzhxx/ynqkgl.jsp","modal":"1"});
					}
				});
				//按钮绑定事件（清空）
			    $("#btnClear").click(function() {
					$("#queryForm").clearFormResult();
					generateJhNdMc($("#QueryND"),$("#QXMMC"));
			    });
			    //按钮绑定事件（导出）
				$("#btnExp").click(function() {
					if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
						//printTabList("DT1");
						printTabList("DT1","pqrwgl.xls","XMBH,XMMC,BDMC,BDDD,GXLB,ZXMMC,PQDD,PQFA,ISJHWC,KGSJ_JH,KGSJ,WCSJ_JH,WCSJ,JZQK,SYGZL,CZWT,JJFA,HTZT,HTZT,HTZT,HTZT,HTZT,HTZT,HTZT,HTZT","2,1"); 
					}
				});
			});
			//计算本页表格分页数
			function setPageHeight(){
				var getHeight=getDivStyleHeight();
				var height = getHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
				var pageNum = parseInt(height/pageTableOne,10);
				$("#DT1").attr("pageNum",pageNum);
			}
			//页面默认参数
			function init(){
				generateJhNdMc($("#QueryND"),$("#QXMMC"));
				if(defQuery=="0"){
					//$("#zhxxTitle").hide();
					$("#t_xmbh").removeAttr("linkFunction");
				}else if(defQuery=="1"){
					//暂时不区分前一也页面是否选中
					//var rowValue = $(parent.frames["menuiframe"].document).find("#resultXML").val();
					//var tempJson = eval("("+rowValue+")");
					//$("#q_jhsjid").val(tempJson.GC_JH_SJ_ID);
					//$("#zhxxTitle").hide();
					$("#t_xmbh").removeAttr("linkFunction");
				}
				setPageHeight();
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				defaultJson.doQueryJsonList(controllername+"?queryPqInfo",data,DT1,null,true);
			}
			//详细信息
			function rowView(index){
			    var obj = $("#DT1").getSelectedRowJsonByIndex(index);//获取行对象
				var xmbh = eval("("+obj+")").XMBH;//取行对象<项目编号>
				$(window).manhuaDialog(xmscUrl(xmbh));//调用公共方法,根据项目编号查询
			}
			function tr_click(obj){
				
			}
			//回调函数
			getWinData = function(data){
				var index =	$("#DT1").getSelectedRowIndex();
				var rowValue = $("#DT1").getSelectedRow();
 				var data1 = defaultJson.packSaveJson(data);
	 			var tempJson = eval("("+rowValue+")");//字符串转JSON对象
				defaultJson.doUpdateJson(controllername + "?doYnqk",data1,DT1,null);
			};
			/**
			 * 业务通用年度和计划名称的级联查询
			 * ndObj 年度的jquery对象
			 * mcObj 计划名称jquery对象
			 */
			function generateJhNdMc(ndObj,mcObj){
				ndObj.attr("src","T#GC_JH_SJ a,GC_PQ_ZXM b: distinct a.ND:a.ND:a.SFYX='1' and a.gc_jh_sj_id=b.jhsjid");
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
				var jsonData = convertJson.string2json1(initData);
				//年度
				if("" != $("#QueryND").val()){
					var defineCondition = {"value": $("#QueryND").val(),"fieldname":"S.ND","operation":"=","logic":"and"};
					jsonData.querycondition.conditions.push(defineCondition);
				}
				//项目名称
				if("" != $("#QXMMC").val()){
					var defineCondition = {"value": "%"+$("#QXMMC").val()+"%","fieldname":"S.XMMC","operation":"like","logic":"and"};
					jsonData.querycondition.conditions.push(defineCondition);
				}
				return JSON.stringify(jsonData);
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
			//控制完工情况颜色，橙色为“未完工”，其他无颜色
			//---------------------------------------
			function showWgqk(obj){
				var p_wcsj=obj.WCSJ;
				if(p_wcsj==null||p_wcsj==""){
					return '<span class="label label-warning">未完工</span>';
				}else{
					return '已完工';			  
				}
			}
		</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title" id="zhxxTitle">
						排迁任务信息
						<span class="pull-right">
							<button id="btnExp" class="btn" type="button">
								导出
							</button>
						</span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" style="width:100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<input class="span12" type="text" placeholder="" name="JHID" keep="true"
										fieldname="Z.JHSJID" id="q_jhsjid" operation="=">
								</TD>
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
								<td class="right-border bottom-border" width="18%">
									<input class="span12 xmmc" type="text" placeholder="" name="QXMMC"
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
								<th width="5%" class="right-border bottom-border text-right">
									管线类别
								</th>
								<td class="right-border bottom-border" width="8%">
									<select class="span12" name="GXLB" fieldname="Z.GXLB" defaultMemo="全部"
										 kind="dic" src="GXLB" operation="=">
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
					<div style="height:5px;"> </div>
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="DT1"
							width="100%" type="single" pageNum="10">
							<thead>
								<tr>
									<th name="XH" id="_XH" rowspan="2" colindex=1>
										&nbsp;#&nbsp;
									</th>
									<th fieldname="XMBH" maxlength="15" rowspan="2" colindex=2 rowMerge="true" tdalign="left" hasLink="true" linkFunction="rowView" id="t_xmbh">
										&nbsp;项目编号&nbsp;
									</th>
									<th fieldname="XMMC" rowspan="2" colindex=3 rowMerge="true" tdalign="left"
										style="width: 20%" maxlength="15">
										&nbsp;项目名称&nbsp;
									</th>
									<th fieldname="BDBH" rowspan="2" colindex=4 
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
									<th fieldname="GXLB" rowspan="2" colindex=7 tdalign="center">
										&nbsp;管线类别&nbsp;
									</th>
									<th  fieldname="ZXMMC" rowspan="2" colindex=8 maxlength="15">
										&nbsp;排迁任务名称&nbsp;
									</th>
									<th  fieldname="XMFZR" rowspan="2" colindex=9>
										&nbsp;项目负责人&nbsp;
									</th>
									<th  fieldname="PQDD" rowspan="2" colindex=10 maxlength="15">
										&nbsp;排迁地点&nbsp;
									</th>
									<th fieldname="PQFA" rowspan="2" colindex=11 maxlength="15">
										&nbsp;排迁方案&nbsp;
									</th>
									<th fieldname="WCSJ" rowspan="2" colindex=12 tdalign="center" Customfunction="showWgqk">
										&nbsp;完工情况&nbsp;
									</th>
									<th colspan=2>
										&nbsp;开工时间&nbsp;
									</th>
									<th colspan=2>
										&nbsp;完工时间&nbsp;
									</th>
									<th fieldname="JZQK" rowspan="2" colindex=17 maxlength="15">
										&nbsp;进展情况&nbsp;
									</th>
									<th fieldname="SYGZL" rowspan="2" colindex=18 maxlength="15">
										&nbsp;剩余工作量&nbsp;
									</th>
									<th fieldname="CZWT" rowspan="2" colindex=19 maxlength="15">
										&nbsp;存在问题&nbsp;
									</th>
									<th fieldname="JJFA" rowspan="2" colindex=20 maxlength="15">
										&nbsp;解决方案&nbsp;
									</th>
									<th fieldname="ISJHWC" rowspan="2" colindex=21 tdalign="center">
										&nbsp;是否能按&nbsp;<br/>&nbsp;计划完成&nbsp;
									</th>
									<th fieldname="ZYGXPQT" rowspan="2" colindex=22>
										&nbsp;专业管线排迁图&nbsp;
									</th>
									<th fieldname="PQLLD" rowspan="2" colindex=23>
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
									<th fieldname="KGSJ_JH" colindex=13 type="date" tdalign="center">
										&nbsp;计划&nbsp;
									</th>
									<th fieldname="KGSJ" colindex=14 type="date" tdalign="center">
										&nbsp;实际&nbsp;
									</th>
									<th fieldname="WCSJ_JH" colindex=15 type="date" tdalign="center">
										&nbsp;计划&nbsp;
									</th>
									<th fieldname="WCSJ" colindex=16 type="date" tdalign="center">
										&nbsp;实际&nbsp;
									</th>
									<th fieldname="YSSSD" colindex=24>
										&nbsp;预算送审单&nbsp;
									</th>
									<th fieldname="GCYSSDB" colindex=25>
										&nbsp;工程预算审定表&nbsp;
									</th>
									<th fieldname="WTH" colindex=26>
										&nbsp;委托函&nbsp;
									</th>
									<th fieldname="SSZ" colindex=27 tdalign ="right">
										&nbsp;工程造价（元）&nbsp;
									</th>
									<th fieldname="SDZ" colindex=28 tdalign ="right">
										&nbsp;审定值（元）&nbsp;
									</th>
									<th fieldname="HTBM" colindex=29>
										&nbsp;合同编号&nbsp;
									</th>
									<th fieldname="HTSX" colindex=30 tdalign ="center">
										&nbsp;合同属性&nbsp;
									</th>
									<th fieldname="HTQDJ" colindex=31 tdalign ="right">
										&nbsp;合同签订价（元）&nbsp;
									</th>
									<th fieldname="SDZ" colindex=32 tdalign ="right">
										&nbsp;合同结算价（元）&nbsp;
									</th>
									<th fieldname="HTZF" colindex=33 tdalign ="right">
										&nbsp;已拨付合同款（元）&nbsp;
									</th>
									<th fieldname="WFHTK" colindex=34 tdalign="right" CustomFunction="showWfhtk">
										&nbsp;未付合同款（元）&nbsp;
									</th>
									<th fieldname="HTJQDRQ" colindex=35 tdalign ="center">
										&nbsp;签订日期&nbsp;
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

		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="Z.XMID,Z.PXH" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id = "queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>