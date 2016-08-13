<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>排迁任务管理信息</title>
		<%	String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
		%>
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
		<script type="text/javascript" charset="utf-8">
			//请求路径，对应后台RequestMapping
			var controllername= "${pageContext.request.contextPath }/bmgk/XmcbkBmgkController.do";
			g_bAlertWhenNoResult = false;
			var g_nd = '<%=nd%>';
			var g_proKey = '<%=proKey%>';
			var btnNum = 0;
			var objRow;
			
			//计算本页表格分页数
			function setPageHeight(){
				var getHeight=getDivStyleHeight();
				var height = getHeight-pageTopHeight-pageTitle-getTableTh(2)-pageNumHeight;
				var pageNum = parseInt(height/pageTableOne,10);
				$("#DT1").attr("pageNum",pageNum);
			}
			
			//页面初始化
			$(function() {
				setPageHeight();
				doInit();
				//按钮绑定事件（导出EXCEL）
				$("#btnExp").click(function() {
					  if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
					      printTabList("DT1");
					  }
				});
			});
			//页面默认参数
			function doInit(){
				doSearch();
			}
			function doSearch(){
				var condProKey = "";
				if(g_proKey!=null&&g_proKey!=""){
					condProKey = "&proKey="+g_proKey;
				}
				var condNd = "";
				if(g_nd!=null&&g_nd!=""){
					condNd = "&nd="+g_nd;
				}
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				defaultJson.doQueryJsonList(controllername+"?queryList"+condNd+condProKey,data,DT1,null,false);
			}
			function tr_click(obj,tabId){
				var rowValue = $("#"+tabId).getSelectedRow();//获得选中行的json 字符串
				var tempJson = convertJson.string2json1(rowValue);//字符串转JSON对象
			}
			//详细信息
			function rowView(index){
				var obj = $("#DT1").getSelectedRowJsonByIndex(index);
				var id = convertJson.string2json1(obj).XMID;
				$(window).manhuaDialog(xmscUrl(id));
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

			//修改计划执行名称
			function rename(obj)
			{
				var isxd = obj.CJXMSX;
				if(isxd=='1')
				{
					return "年初计划";
				}
				else
				{
					return "追加计划";
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
						<span class="pull-right">
							<button id="btnExp" class="btn" type="button">
								导出
							</button>
						</span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" printFileName="项目详细列表">
							<thead>
				              <tr>
								<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
								<th rowspan="2" fieldname="XMBH" colindex=2>&nbsp;项目编号&nbsp;</th>
								<th rowspan="2" fieldname="XMMC" colindex=3 maxlength="15">&nbsp;项目名称&nbsp;</th>
								<th rowspan="2" fieldname="XMLX" colindex=4 tdalign="center">&nbsp;项目类型&nbsp;</th>
								<th rowspan="2" fieldname="SSXZ" colindex=5 tdalign="center">&nbsp;实施性质&nbsp;</th>
								<th rowspan="2" fieldname="ISBT" colindex=6 tdalign="center">&nbsp;BT&nbsp;</th>
								<th rowspan="2" fieldname="XMXZ" colindex=7 tdalign="center">&nbsp;项目性质&nbsp;</th>
								<th rowspan="2" fieldname="CJXMSX" colindex=8 tdalign="center" CustomFunction="rename">&nbsp;计划执行&nbsp;</th>
								<th rowspan="2" fieldname="ISXD" colindex=9 tdalign="center">&nbsp;是否下达&nbsp;</th>
								<th rowspan="2" fieldname="QY" colindex=10 maxlength="15">&nbsp;所属区域&nbsp;</th>
								<th rowspan="2" fieldname="XMDZ" colindex=11 maxlength="15">&nbsp;项目地址&nbsp;</th>
								<th rowspan="2" fieldname="ZRBM" colindex=12 maxlength="15">&nbsp;责任部门&nbsp;</th>
								<th rowspan="2" fieldname="XMFR" colindex=13 maxlength="15">&nbsp;项目法人&nbsp;</th>
								<th rowspan="2" fieldname="NDMB" colindex=14>&nbsp;年度目标&nbsp;</th>
								<th colspan="4" >&nbsp;年度总投资（万元）&nbsp;</th>
								<th colspan="4" >&nbsp;计划总投资（万元）&nbsp;</th>
				              </tr>
				              <tr>
								<th  fieldname="ND_GC" colindex=15 tdalign="right">&nbsp;工程&nbsp;</th>
								<th  fieldname="ND_ZQ" colindex=16 tdalign="right">&nbsp;征拆&nbsp;</th>
								<th  fieldname="ND_QT" colindex=17 tdalign="right">&nbsp;其他&nbsp;</th>
								<th  fieldname="ND_XJ" colindex=18 tdalign="right">&nbsp;合计&nbsp;</th>
								
								<th  fieldname="JH_GC" colindex=19 tdalign="right">&nbsp;工程&nbsp;</th>
								<th  fieldname="JH_ZQ" colindex=20 tdalign="right">&nbsp;征拆&nbsp;</th>
								<th  fieldname="JH_QT" colindex=21 tdalign="right">&nbsp;其他&nbsp;</th>
								<th  fieldname="JH_XJ" colindex=22 tdalign="right">&nbsp;合计&nbsp;</th>
								
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
				<input type="hidden" name="ywid" id="ywid">
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