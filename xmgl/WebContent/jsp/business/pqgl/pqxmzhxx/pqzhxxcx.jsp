<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>排迁任务综合信息</title>
		<%
			String callback = request.getParameter("callback");
		%>
		<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
		<script type="text/javascript" charset="utf-8">
			//请求路径，对应后台RequestMapping
			var controllername= "${pageContext.request.contextPath }/pqgl/pqglController.do";
			g_bAlertWhenNoResult = false;
			var callback = '<%=callback%>';
			var objRow;
			//计算本页表格分页数
			function setPageHeight(){
				var getHeight=getDivStyleHeight();
				var height = getHeight-pageTopHeight-pageTitle-getTableTh(3)-pageNumHeight;
				var pageNum = parseInt(Math.abs(height)/pageTableOne,10);
				$("#DT1").attr("pageNum", pageNum);
			}
			//页面初始化
			$(function() {
				setPageHeight();
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
			  	//按钮绑定事件（确定）
			    $("#btnQd").click(function() {
			    	if($("#DT1").getSelectedRowIndex()==-1)
					 {
			    		requireSelectedOneRow();
					    return;
					 }
			        var rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
			    	if(callback!=""){
						$(window).manhuaDialog.getParentObj().eval(callback+"("+JSON.stringify(rowValue)+")");
					}else{
						$(window).manhuaDialog.setData(rowValue);
						$(window).manhuaDialog.sendData();
					}
					$(window).manhuaDialog.close();
			    });
			});
			//页面默认参数
			function init(){
				generateJhNdMc($("#QueryND"),$("#QXMMC"));
				//如果父页面已经选择了项目，那么排迁任务必须是该项目下的
				var parent_jhsjid = $($(window).manhuaDialog.getParentObj().document).find("#jhsjids").val();
				if(parent_jhsjid!=""){
					$("#q_jhsjid").val(parent_jhsjid);
					var parent_xmmc = $($(window).manhuaDialog.getParentObj().document).find("#XMMC").val();
					$("#QXMMC").val(parent_xmmc);
					$("#QXMMC").attr("keep","true");
					$("#QXMMC").attr("disabled",true);
					$("#QueryND").get(0).options[0].selected=true;
					$("#QueryND").attr("disabled",true);
				}
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
				ndObj.attr("src","T#GC_JH_SJ a,GC_PQ_ZXM b: distinct a.ND:a.ND:a.SFYX='1' and a.gc_jh_sj_id=b.jhsjid order by a.ND");
				ndObj.attr("kind","dic");
				ndObj.html('');
				reloadSelectTableDic(ndObj);
				//ndObj.val(new Date().getFullYear());
				//setDefaultOption(ndObj,new Date().getFullYear());
				//setDefaultNd("QueryND");
				//用户提出，这个查询页面，年度默认使用“全部”就可以了
				$("#QueryND").get(0).options[0].selected=true;
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
							<button id="btnQd" class="btn" type="button">
								确定
							</button>
						</span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" style="width:100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<input class="span12" type="text" placeholder="" name="JHSJID" keep="true"
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
									<input style="display: none;" class="span12" type="text"
										id="QXFLX" name="QXFLX" fieldname="S.XFLX" operation="=" />
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									项目名称
								</th>
								<td class="right-border bottom-border" width="20%">
									<input class="span12 xmmc" type="text" placeholder="" name="QXMMC"
										fieldname="S.XMMC" operation="like" id="QXMMC"
										autocomplete="off" tablePrefix="S">
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									任务名称
								</th>
								<td class="right-border bottom-border" width="20%">
									<input class="span12" type="text" placeholder="" name="RWMC"
										fieldname="Z.ZXMMC" operation="like" id="rwmc"
										autocomplete="off" tablePrefix="Z">
								</td>
								<td class="right-border bottom-border text-right" rowspan=2>
									<button id="btnQuery" class="btn btn-link" type="button">
										<i class="icon-search"></i>查询
									</button>
									<button id="btnClear" class="btn btn-link" type="button">
										<i class="icon-trash"></i>清空
									</button>
								</td>
							</tr>
							<tr>
								<th width="5%" class="right-border bottom-border text-right">
									管线类别
								</th>
								<td class="right-border bottom-border" width="8%">
									<select class="span12" name="GXLB" fieldname="Z.GXLB" defaultMemo="全部"
										 kind="dic" src="GXLB" operation="=">
									</select>
								</td>
								<th class="right-border bottom-border text-right">
									工程造价
								</th>
								<td class="right-border bottom-border">
									<input class="span8" type="number" placeholder="" name="SSZ" style="text-align:right;"
										fieldname="Z.SSZ" operation="=" tablePrefix="Z">&nbsp;&nbsp;<b>(元)</b>
								</td>
								<td colspan=2>
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
									<th name="XH" id="_XH"  colindex=1>
										&nbsp;#&nbsp;
									</th>
									<th fieldname="XMBH" maxlength="15" colindex=2 rowMerge="true" tdalign="left" id="t_xmbh">
										&nbsp;项目编号&nbsp;
									</th>
									<th fieldname="XMMC" colindex=3 rowMerge="true" tdalign="left"
										style="width: 20%" maxlength="15">
										&nbsp;项目名称&nbsp;
									</th>
									<th fieldname="BDMC"  colindex=4 style="width: 20%"
										maxlength="10" Customfunction="doBdmc">
										&nbsp;标段名称&nbsp;
									</th>
									<th fieldname="BDDD"  colindex=5
										style="width: 300px" maxlength="10">
										&nbsp;标段地点&nbsp;
									</th>
									<th fieldname="GXLB"  colindex=6 tdalign="center">
										&nbsp;管线类别&nbsp;
									</th>
									<th  fieldname="ZXMMC"  colindex=7 maxlength="15">
										&nbsp;排迁任务名称&nbsp;
									</th>
									<th  fieldname="XMFZR"  colindex=8>
										&nbsp;项目负责人&nbsp;
									</th>
									<th fieldname="SSZ" tdalign ="right">
										&nbsp;工程造价（元）&nbsp;
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