<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title></title>
		<%	String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
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
		<script type="text/javascript" charset="utf-8">
			//请求路径，对应后台RequestMapping
			var controllername= "${pageContext.request.contextPath }/bzjk/bzjkCommonController.do";
			g_bAlertWhenNoResult = false;
			var g_nd = '<%=nd%>';
			var g_proKey = '<%=proKey%>';
			var g_tiaojian = '<%=tiaojian%>';
			var btnNum = 0;
			var objRow;
			//计算本页表格分页数
			function setPageHeight(){
				var getHeight=getDivStyleHeight();
				var height = getHeight-pageTopHeight-pageTitle-getTableTh(2)-pageNumHeight-pageQuery;
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
					doSearch();
				});
				//按钮绑定事件（清空）
				$("#btnClear").click(function() {
					$("#queryForm").clearFormResult();
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
				var condTiaojian = "";
				if(g_tiaojian!=null&&g_tiaojian!=""){
					condTiaojian = "&tiaojian="+g_tiaojian;
				}
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				defaultJson.doQueryJsonList(controllername+"?queryJHBZList"+condTiaojian+condNd+condProKey,data,DT1,null,false);
			}
			//标段名称图标样式
			function doBdmc(obj){
				if(obj.XMBS == "0"){
					return '<div style="text-align:center">—</div>';
				}else{
					return obj.BDMC;
				}
			}

			//标段编号图标样式
			function doBdbh(obj){
				if(obj.BDBH == ""){
					return '<div style="text-align:center">—</div>';
				}else{
					return obj.BDBH;
				}
			}
			//列表项<项目地址>加图标
			function doDz(obj){
				var xmdz = obj.XMDZ;
				if(xmdz != ""){
					return '<a href="javascript:void(0);" onclick="selectDz()"><i title="查看" class="pull-right icon-map-marker"></a></i>';
				}
				
			}
			//点击项目地址图标
			function selectDz(){
				window.open("${pageContext.request.contextPath }/jsp/business/jhb/xdxmk/img/earth.png");
			}
			//<开工时间>图标样式
			function doKGSJ(obj){
				if(obj.ISKGSJ == "0"){
					return '<div style="text-align:center">—</div>';
				}else if(obj.ISKGSJ == "2"){
					return '<div style="text-align:center">往年</div>';
				}else{
					return obj.KGSJ;
				}
			}
			//<完工时间>图标样式
			function doWGSJ(obj){
				if(obj.ISWGSJ == "0"){
					return '<div style="text-align:center">—</div>';
				}else if(obj.ISWGSJ == "2"){
					return '<div style="text-align:center">往年</div>';
				}else{
					return obj.WGSJ;
				}
			}
			//<可研批复>图标样式
			function doKYPF(obj){
				if(obj.ISKYPF == "0"){
					return '<div style="text-align:center">—</div>';
				}else if(obj.ISKYPF == "2"){
					return '<div style="text-align:center">往年</div>';
				}else{
					return obj.KYPF;
				}
			}
			//<划拨决定书>图标样式
			function doHPJDS(obj){
				if(obj.ISHPJDS == "0"){
					return '<div style="text-align:center">—</div>';
				}else if(obj.ISHPJDS == "2"){
					return '<div style="text-align:center">往年</div>';
				}else{
					return obj.HPJDS;
				}
			}
			//<工程规划许可证>图标样式
			function doGCXKZ(obj){
				if(obj.ISGCXKZ == "0"){
					return '<div style="text-align:center">—</div>';
				}else if(obj.ISGCXKZ == "2"){
					return '<div style="text-align:center">往年</div>';
				}else{
					return obj.GCXKZ;
				}
			}
			//<施工许可>图标样式
			function doSGXK(obj){
				if(obj.ISSGXK == "0"){
					return '<div style="text-align:center">—</div>';
				}else if(obj.ISSGXK == "2"){
					return '<div style="text-align:center">往年</div>';
				}else{
					return obj.SGXK;
				}
			}
			//<计划初步设计批复>图标样式
			function doCBSJPF(obj){
				if(obj.ISCBSJPF == "0"){
					return '<div style="text-align:center">—</div>';
				}else if(obj.ISCBSJPF == "2"){
					return '<div style="text-align:center">往年</div>';
				}else{
					return obj.CBSJPF;
				}
			}
			//<拆迁图>图标样式
			function doCQT(obj){
				if(obj.ISCQT == "0"){
					return '<div style="text-align:center">—</div>';
				}else if(obj.ISCQT == "2"){
					return '<div style="text-align:center">往年</div>';
				}else{
					return obj.CQT;
				}
			}
			//<排迁图>图标样式
			function doPQT(obj){
				if(obj.ISPQT == "0"){
					return '<div style="text-align:center">—</div>';
				}else if(obj.ISPQT == "2"){
					return '<div style="text-align:center">往年</div>';
				}else{
					return obj.PQT;
				}
			}
			//<施工图>图标样式
			function doSGT(obj){
				if(obj.ISSGT == "0"){
					return '<div style="text-align:center">—</div>';
				}else if(obj.ISSGT == "2"){
					return '<div style="text-align:center">往年</div>';
				}else{
					return obj.SGT;
				}
			}
			//<提报价>图标样式
			function doTBJ(obj){
				if(obj.ISTBJ == "0"){
					return '<div style="text-align:center">—</div>';
				}else if(obj.ISTBJ == "2"){
					return '<div style="text-align:center">往年</div>';
				}else{
					return obj.TBJ;
				}
			}
			//<造价财审>图标样式
			function doCS(obj){
				if(obj.ISCS == "0"){
					return '<div style="text-align:center">—</div>';
				}else if(obj.ISCS == "2"){
					return '<div style="text-align:center">往年</div>';
				}else{
					return obj.CS;
				}
			}
			//<招标监理>图标样式
			function doJLDW(obj){
				if(obj.ISJLDW == "0"){
					return '<div style="text-align:center">—</div>';
				}else if(obj.ISJLDW == "2"){
					return '<div style="text-align:center">往年</div>';
				}else{
					return obj.JLDW;
				}
			}
			//<招标施工>图标样式
			function doSGDW(obj){
				if(obj.ISSGDW == "0"){
					return '<div style="text-align:center">—</div>';
				}else if(obj.ISSGDW == "2"){
					return '<div style="text-align:center">往年</div>';
				}else{
					return obj.SGDW;
				}
			}
			//<征拆>图标样式
			function doZC(obj){
				if(obj.ISZC == "0"){
					return '<div style="text-align:center">—</div>';
				}else if(obj.ISZC == "2"){
					return '<div style="text-align:center">往年</div>';
				}else{
					return obj.ZC;
				}
			}
			//<排迁>图标样式
			function doPQ(obj){
				if(obj.ISPQ == "0"){
					return '<div style="text-align:center">—</div>';
				}else if(obj.ISPQ == "2"){
					return '<div style="text-align:center">往年</div>';
				}else{
					return obj.PQ;
				}
			}
			//<交工>图标样式
			function doJG(obj){
				if(obj.ISJG == "0"){
					return '<div style="text-align:center">—</div>';
				}else if(obj.ISJG == "2"){
					return '<div style="text-align:center">往年</div>';
				}else{
					return obj.JG;
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
								<TD>
									<INPUT id="num" type="text" class="span12" kind="text"
										fieldname="rownum" value="1000" operation="<=" keep="true" />
								</TD>
							</TR>
							<tr>
								<th width="6%" class="right-border bottom-border text-right">
									项目名称
								</th>
								<td class="right-border bottom-border" width="12%">
									<input class="span12 xmmc" type="text" placeholder="" name="QXMMC"
										fieldname="T.XMMC" operation="like" id="QXMMC">
								</td>
								<th width="6%" class="right-border bottom-border text-right">
									新/续建
								</th>
								<td class="right-border bottom-border" width="10%">
									<select class="span12 2characters" name = "QXJXJ" fieldname = "T1.XJXJ" defaultMemo="全部" operation="=" kind="dic" src="XMXZ">
									</select>
								</td>
								<th width="6%" class="right-border bottom-border text-right">
									项目管理公司
								</th>
								<td class="right-border bottom-border" width="5%">
									<select class="span12 4characters"  defaultMemo="全部" 
									id="XMGLGS" kind="dic" src="T#VIEW_YW_XMGLGS:ROW_ID:BMJC" operation="=" fieldname="T.XMGLGS" name="XMGLGS">
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
							width="100%" editable="0" type="single" pageNum="10">
							<thead>
								<tr>
									<th name="XH" id="_XH" rowspan="2" colindex=1>
										&nbsp;#&nbsp;
									</th>
									<th fieldname="XMMC" rowspan="2" colindex=2 hasLink="true"
										rowMerge="true">
										&nbsp;项目名称&nbsp;
									</th>
									<th fieldname="BDMC" rowspan="2" colindex=3 maxlength="15"
										CustomFunction="doBdmc"
										>
										&nbsp;标段名称&nbsp;
									</th>
									<th fieldname="XMDZ" rowspan="2" colindex=4 maxlength="10">
										&nbsp;项目地址&nbsp;
									</th>
									<th fieldname="XMDZ" rowspan="2" colindex=5
										CustomFunction="doDz" noprint="true">
										&nbsp;&nbsp;
									</th>
									<th fieldname="JSNR" rowspan="2" colindex=6 maxlength="10">
										&nbsp;建设内容及规模&nbsp;
									</th>
									<th fieldname="XMGLGS" rowspan="2" colindex=7 maxlength="10"
										tdalign="center">
										&nbsp;项目管理公司&nbsp;
									</th>
									<th fieldname="XMXZ" rowspan="2" colindex=8 tdalign="center">
										&nbsp;项目性质&nbsp;
									</th>
									<th colspan="2">
										&nbsp;工期安排（工程部及项目管理公司）&nbsp;
									</th>
									<th colspan="4">
										&nbsp;手续办理情况&nbsp;
									</th>
									<th colspan="4">
										&nbsp;设计情况&nbsp;
									</th>
									<th colspan="2">
										&nbsp;造价&nbsp;
									</th>
									<th colspan="2">
										&nbsp;招标&nbsp;
									</th>
									<th fieldname="ZC" rowspan="2" colindex=23 tdalign="center"
										CustomFunction="doZC">
										&nbsp;征拆&nbsp;
									</th>
									<th fieldname="PQ" rowspan="2" colindex=24 tdalign="center"
										CustomFunction="doPQ">
										&nbsp;排迁&nbsp;
									</th>
									<th fieldname="JG" rowspan="2" colindex=25 tdalign="center"
										CustomFunction="doJG">
										&nbsp;交工&nbsp;
									</th>
									<th fieldname="BZ" rowspan="2" colindex=26 maxlength="10">
										&nbsp;备注&nbsp;
									</th>
								</tr>
								<tr>
									<th fieldname="KGSJ" colindex=9 tdalign="center"
										CustomFunction="doKGSJ">
										&nbsp;开工时间&nbsp;
									</th>
									<th fieldname="WGSJ" colindex=10 tdalign="center"
										CustomFunction="doWGSJ">
										&nbsp;完工时间&nbsp;
									</th>
									<th fieldname="KYPF" colindex=11 tdalign="center"
										CustomFunction="doKYPF">
										&nbsp;可研批复&nbsp;
									</th>
									<th fieldname="HPJDS" colindex=12 tdalign="center"
										CustomFunction="doHPJDS">
										&nbsp;划拔决定书&nbsp;
									</th>
									<th fieldname="GCXKZ" colindex=13 tdalign="center"
										CustomFunction="doGCXKZ">
										&nbsp;工程规划许可证&nbsp;
									</th>
									<th fieldname="SGXK" colindex=14 tdalign="center"
										CustomFunction="doSGXK">
										&nbsp;施工许可&nbsp;
									</th>
									<th fieldname="CBSJPF" colindex=15 tdalign="center"
										CustomFunction="doCBSJPF">
										&nbsp;初步设计批复&nbsp;
									</th>
									<th fieldname="CQT" colindex=16 tdalign="center"
										CustomFunction="doCQT">
										&nbsp;拆迁图&nbsp;
									</th>
									<th fieldname="PQT" colindex=17 tdalign="center"
										CustomFunction="doPQT">
										&nbsp;排迁图&nbsp;
									</th>
									<th fieldname="SGT" colindex=18 tdalign="center"
										CustomFunction="doSGT">
										&nbsp;施工图&nbsp;
									</th>
									<th fieldname="TBJ" colindex=19 tdalign="center"
										CustomFunction="doTBJ">
										&nbsp;提报价&nbsp;
									</th>
									<th fieldname="CS" colindex=20 tdalign="center"
										CustomFunction="doCS">
										&nbsp;财审&nbsp;
									</th>
									<th fieldname="JLDW" colindex=21 tdalign="center"
										CustomFunction="doJLDW">
										&nbsp;监理单位&nbsp;
									</th>
									<th fieldname="SGDW" colindex=22 tdalign="center"
										CustomFunction="doSGDW">
										&nbsp;施工单位&nbsp;
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
				<input type="hidden" name="ywid" id="ywid">
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="T.PXH,T.XMBH,T.XMBS,T.BDBH" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id = "queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>