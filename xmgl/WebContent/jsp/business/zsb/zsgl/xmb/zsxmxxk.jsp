<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>

<app:base />
<title>征收任务反馈</title>
<script type="text/javascript" charset="utf-8">
	g_bAlertWhenNoResult = false;
	var controllername= "${pageContext.request.contextPath }/zsb/xmb/xmbController.do";
	var controllername2= "${pageContext.request.contextPath }/zsb/xxb/xxbController.do";
	//获取父页面行选值方法
	function do_value(){
		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.$("#DT1").getSelectedRow();
		var obj = convertJson.string2json1(rowValue);
		return obj;
	}
	$(function(){
		doInit();
		//按钮绑定事件（导出）
		$("#btnExp").click(function() {
			 if(exportRequireQuery($("#DT2"))){//该方法需传入表格的jquery对象
			      printTabList("DT2");
			  }
		});
		$("#btnPrint").click(function(){
			window.print();
		});
	});
	function doInit(){
		var temp=do_value();
		setValueByArr(temp,["JHSJID","JHWCSJ","XMMC"]);
		$("#XXB_JHSJID").val($(temp).attr("JHSJID"));
		//避免主键为空，查询的数据有误
		/* if($("#GC_ZSB_XMB_ID").val()==null||$("#GC_ZSB_XMB_ID").val()==""){
			$("#GC_ZSB_XMB_ID").val('XXXXXXXXXXXXXXXXXXXXXXX');
		} */
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		defaultJson.doQueryJsonList(controllername+"?queryXmb",data,DT1,null,true);
		
		var data1 = combineQuery.getQueryCombineData(showForm,frmPost,DT2);
		defaultJson.doQueryJsonList(controllername2+"?queryXxb",data1,DT2,null,true);
	}

</script>
</head>
<body>
<app:dialogs />
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<form method="post" id="queryForm">
				<table class="B-table">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
					<!-- <TR> -->
						<td><input class="span12" type="text" name="JHSJID" fieldname="xmb.JHSJID" id="JHSJID" operation="="></TD>
						<TD><INPUT type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<=" ></TD>
					</TR>
				</table>
					</form>
					
					<div class="B-small-from-table-autoConcise" style="width:100%">
						<h4 class="title">
							统筹计划信息
							<span class="pull-right">
								<button id="btnExp" class="btn" type="button">
									导出
								</button>
								<!-- <button id="btnPrint" class="btn" type="button">
									打印
								</button> -->
							</span>
						</h4>
						<form method="post" action="" id="showForm">
							<table class="B-table" width="100%">
								<!-- 这里需要一个隐藏域，存放比如：问题编号,接收人账号，接收单位等信息 -->
								<tr style="display: none;">
								<!-- <tr> -->
									<td class="right-border bottom-border">
										<input class="span12" type="text" name="JHSJID" fieldname="xxb.JHSJID" id="XXB_JHSJID" keep="true" operation="=">
									</td>
								</tr>
								<tr>
									<th width="10%" class="right-border bottom-border disabledTh">项目名称
									</th>
									<td width="30%" class="right-border bottom-border" colspan=3>
										<input class="span12" type="text" name="XMMC" id="XMMC" disabled>
									</td>
									<th width="10%" class="right-border bottom-border disabledTh">计划完成时间
									</th>
									<td width="10%" class="right-border bottom-border" >
										<input class="span12" type="text" name="JHWCSJ" id="JHWCSJ" disabled>
									</td>
									<td width="40%" class="right-border bottom-border"></td>
								</tr>
							</table>
						</form>
					</div>
					<div>
						<h4 class="title">
							征收综合信息
						</h4>
					</div>
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="DT1"
							width="100%" type="single" pageNum="1000" noPage="true">
							<thead>
								<tr>
									<th name="XH" id="_XH" rowspan="2" colindex=1
										style="width: 10px">
										&nbsp;#&nbsp;
									</th>
									<th width="50px" fieldname="SFCQ_JH" tdalign="center" rowspan="2"
										colindex=2>
										&nbsp;是否拆迁&nbsp;
									</th>
									<th width="100px" fieldname="CQTQDSJ" tdalign="center"
										rowspan="2" colindex=3>
										拆迁图
										<p></p>
										取得时间
									</th>
									<th width="100px" fieldname="GHXKZ" tdalign="center" rowspan="2"
										colindex=4>
										用地规划
										<p></p>
										许可证&nbsp;
									</th>
									<th width="100px" fieldname="TDSYZ" tdalign="center" rowspan="2"
										colindex=5>
										&nbsp;土地使用证&nbsp;
									</th>
									<th colspan="2">
										&nbsp;完成时间&nbsp;
									</th>
									<th width="100px" fieldname="CDSJSJ" tdalign="center"
										rowspan="2" colindex=8>
										&nbsp;场地移交
										<p></p>
										时间&nbsp;
									</th>
								</tr>
								<tr>
									<th width="100px" fieldname="JHWCSJ" tdalign="center"
										colindex=6>
										&nbsp;计划&nbsp;
									</th>
									<th width="100px" fieldname="SJWCRQ" tdalign="center"
										colindex=7>
										&nbsp;实际&nbsp;
									</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<div>
						<h4 class="title">
							征收任务信息
						</h4>
					</div>
					<div class="overFlowX">
					<!-- 
					<table class="table-hover table-activeTd B-table" id="DT2"
						width="100%" type="single" noPage="true" pageNum="1000">
						<thead>
							<tr>
								<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
								<th fieldname="XMMC" rowspan="2" colindex=2>&nbsp;项目名称&nbsp;</th>
								<th fieldname="QY" rowspan="2" colindex=3>&nbsp;区域&nbsp;</th>
								<th fieldname="ZRDW" rowspan="2" colindex=4>&nbsp;负责单位&nbsp;</th>
								<th fieldname="GHSJ" tdalign="center" rowspan="2" colindex=5>用地规划<p></p>许可证&nbsp;</th>
								<th fieldname="TDSJ" tdalign="center" rowspan="2" colindex=6>&nbsp;土地使用证&nbsp;</th>
								<th fieldname="GHXKZ" tdalign="center" rowspan="2" colindex=5>用地规划<p></p>许可证&nbsp;</th>
								<th fieldname="TDSYZ" tdalign="center" rowspan="2" colindex=6>&nbsp;土地使用证&nbsp;</th>
								<th fieldname="CQTHDSJ" tdalign="center" rowspan="2" colindex=7>拆迁图<p></p>取得时间&nbsp;</th>
								<th fieldname="MDWCRQ" tdalign="center" rowspan="2" colindex=8>摸底完成<p></p>时间&nbsp;</th>
								<th colspan="2">拆迁居民数</th>
								<th colspan="2">拆迁企业数</th>
								<th colspan="2">征地面积</th>
								<th fieldname="QWTRQ" tdalign="center" rowspan="2" colindex=15>区委托协议签订<p></p>日期&nbsp;</th>
								<th fieldname="SJRQ" tdalign="center" rowspan="2" colindex=16>&nbsp;实施日期&nbsp;</th>
								<th fieldname="CDYJRQ" tdalign="center" rowspan="2" colindex=17>场地移交<p></p>日期&nbsp;</th>
								<th fieldname="TDFWYJRQ" tdalign="center" rowspan="2"colindex=18>土地房屋移交<p></p>日期&nbsp;</th>
								<th fieldname="WTJFX" rowspan="2" colindex=19>&nbsp;问题及风险&nbsp;</th>
							</tr>
							<tr>
								<th fieldname="LJJMZL" tdalign="right" colindex=9>&nbsp;已完成&nbsp;</th>
								<th fieldname="JMHS" tdalign="right" colindex=10>&nbsp;总量&nbsp;</th>
								<th fieldname="LJQYZL" tdalign="right" colindex=11>&nbsp;已完成&nbsp;</th>
								<th fieldname="QYJS" tdalign="right" colindex=12>&nbsp;总量&nbsp;</th>
								<th fieldname="LJZDMJ" tdalign="right" colindex=13>&nbsp;已完成&nbsp;</th>
								<th fieldname="ZMJ" tdalign="right" colindex=14>&nbsp;总量&nbsp;</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					 -->
						<table class="table-hover table-activeTd B-table" id="DT2"
							width="100%" type="single" noPage="true" pageNum="1000">
							<thead>
								<tr>
									<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
									<th fieldname="QY" rowspan="2" colindex=2>&nbsp;区域&nbsp;</th>
									<th fieldname="ZRDW" rowspan="2" colindex=3>&nbsp;负责单位&nbsp;</th>
									<th fieldname="MDWCRQ" tdalign="center" rowspan="2" colindex=4>摸底完成<p></p>时间&nbsp;</th>
									<th colspan="2">拆迁居民数</th>
									<th colspan="2">拆迁企业数</th>
									<th colspan="2">征地面积</th>
									<th fieldname="QWTRQ" tdalign="center" rowspan="2" colindex=11>区委托协议签订<p></p>日期&nbsp;</th>
									<th fieldname="SJRQ" tdalign="center" rowspan="2" colindex=12>&nbsp;实施日期&nbsp;</th>
									<th fieldname="CDYJRQ" tdalign="center" rowspan="2" colindex=13>场地移交<p></p>日期&nbsp;</th>
									<th fieldname="TDFWYJRQ" tdalign="center" rowspan="2"colindex=14>土地房屋移交<p></p>日期&nbsp;</th>
									<th fieldname="WTJFX" rowspan="2" colindex=15 maxlength="15" style="width:15%">&nbsp;问题及风险&nbsp;</th>
								</tr>
								<tr>
									<th fieldname="LJJMZL" tdalign="right" colindex=5>&nbsp;已完成&nbsp;</th>
									<th fieldname="JMHS" tdalign="right" colindex=6>&nbsp;总量&nbsp;</th>
									<th fieldname="LJQYZL" tdalign="right" colindex=7>&nbsp;已完成&nbsp;</th>
									<th fieldname="QYJS" tdalign="right" colindex=8>&nbsp;总量&nbsp;</th>
									<th fieldname="LJZDMJ" tdalign="right" colindex=9>&nbsp;已完成&nbsp;</th>
									<th fieldname="ZMJ" tdalign="right" colindex=10>&nbsp;总量&nbsp;</th>
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
					<input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ" id="txtFilter">
					<input type="hidden" name="resultXML" id="resultXML">
					<input type="hidden" name="queryResult" id="queryResult">
					<!--传递行数据用的隐藏域-->
					<input type="hidden" name="rowData">
				</FORM>
			</div>
	</body>
</html>
