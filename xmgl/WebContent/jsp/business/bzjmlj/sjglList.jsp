<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title></title>
		<%	String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
			String flag = request.getParameter("flag");
			String sxbh = request.getParameter("sxbh");
			String sxmc = request.getParameter("sxmc");
			String bllx = request.getParameter("bllx");	
			String sgflag = request.getParameter("sgflag");
			String tiaojian = request.getParameter("tiaojian");
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
		<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
		<script type="text/javascript" charset="utf-8">
			//请求路径，对应后台RequestMapping
			var controllername= "${pageContext.request.contextPath }/bzjkjm/SjbzjkCommonController.do";
			var controllername1= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
			g_bAlertWhenNoResult = false;
			var g_nd = '<%=nd%>';
			var g_proKey = '<%=proKey%>';
			var flag='<%=flag%>';
			var sxbh='<%=sxbh%>';
			var sxmc='<%=sxmc%>';
			var bllx='<%=bllx%>';
			var sgflag='<%=sgflag%>';
			var g_tiaojian = '<%=tiaojian%>';
			var btnNum = 0;
			var objRow;
			
			//计算本页表格分页数
			function setPageHeight(){
				var getHeight=getDivStyleHeight();
				var height = getHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
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
				//按钮绑定事件（查询）
				$("#btnQuery").click(function() {
					 doInit();
				});
				//按钮绑定事件（清空）
			    $("#btnClear").click(function() {
			        $("#queryForm").clearFormResult();
			        $("#SGJLDW").val('');
			    });
				//自动完成项目名称模糊查询
				showAutoComplete("QXMMC",controllername1+"?xmmcAutoCompleteToXmxdk","getXmmcQueryCondition"); 
			});
			//项目名称自动模糊查询参数
			function getXmmcQueryCondition(){
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				return data;
			}
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
				var condTiaojian = "";
				if(g_tiaojian!=null&&g_tiaojian!=""){
					condTiaojian = "&tiaojian="+g_tiaojian;
				}
				var sgjl=$("#SGJLDW").val();
				var condSGJL = "";
				if(sgjl!=null&&sgjl!=""){
					condSGJL = "&SGJLDW="+sgjl;
				}
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				defaultJson.doQueryJsonList(controllername+"?querySjglList"+condTiaojian+condNd+condProKey+condSGJL,data,DT1,null,false);
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
							<tr>
							  <th width="5%" class="right-border bottom-border text-right">项目名称</th>
					          <td class="right-border bottom-border" width="15%">
					          	<input class="span12" type="text" placeholder="" name="QXMMC"
									fieldname="JHSJ.XMMC" operation="like" id="QXMMC" autocomplete="off"
									tablePrefix="A"/>
							  </td>
							    <th width="5%" class="right-border bottom-border text-right">标段名称</th>
					          <td class="right-border bottom-border" width="15%">
					          	<input class="span12" type="text" placeholder="" name="QXMMC"
									fieldname="JHSJ.BDMC" operation="like" id="BDMC"   />
							  </td>
							      <th width="5%" class="right-border bottom-border text-right">项目性质</th>
					          <td class="right-border bottom-border" width="10%">
					          	  <select class="span12" id="XJXJ" name = "XJXJ" fieldname="JHSJ.XMXZ"  defaultMemo="全部" operation="="  kind="dic" src="XMXZ">
					              </select>
							  </td>
							  
							     <td class="text-left bottom-border text-right">
					           		<button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
					           		<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
					          	</td>
							</tr>
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" printFileName="项目详细列表">
							<thead>
				          	<tr>
								<th name="XH" id="_XH" colindex=1>&nbsp;#&nbsp;</th>
								<th fieldname="XMMC" colindex=2  rowMerge="true" maxlength="15">&nbsp;项目名称&nbsp;</th>
								<th fieldname="BDMC" colindex=3  >&nbsp;标段名称&nbsp;</th>	
								<th fieldname="XMDZ" colindex=4 maxlength="10">&nbsp;项目地址&nbsp;</th>	
								<th fieldname="XMXZ" colindex=5 tdalign="center">&nbsp;项目性质&nbsp;</th>
								<th fieldname="NDMB" colindex=6 maxlength="15">&nbsp;年度目标&nbsp;</th>
								
								<th fieldname="KYBG" colindex=7 maxlength="15">&nbsp;可研报告&nbsp;</th>
								<th fieldname="SJDWID" colindex=8 maxlength="10"  >&nbsp;设计单位&nbsp;</th>
								<th fieldname="CSPF" colindex=9 tdalign="center">&nbsp;初设批复&nbsp;</th>
								<th fieldname="GHTJ" colindex=10 colindex=9 tdalign="center" maxlength="10" >&nbsp;规划条件 &nbsp;</th>
								<th fieldname="WCSJ_SGT_SSB" colindex=10 maxlength="10" colindex=9 tdalign="center" >&nbsp;施工图（送审版）&nbsp;</th>
								<th fieldname="SGTSCBG" colindex=10  colindex=9 tdalign="center" maxlength="10" >&nbsp;施工图审查报告&nbsp;</th>
								<th fieldname="WCSJ_SGT_ZSB" colindex=11 tdalign="center" colindex=9 tdalign="center">&nbsp;施工图（正式）&nbsp;</th>
								<th fieldname="CQT_SJ" colindex=12 tdalign="center"  colindex=9 tdalign="center">&nbsp;拆迁图&nbsp;</th>
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