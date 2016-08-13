<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>设计综合信息部长监控钻取页面</title>
		<%	String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
		%>
		<style type="text/css">
			i.showXmxxkInfo:hover,i.showXmxxkInfo:focus {
				cursor: pointer;
			}
		</style>
		<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/sjSjzqController.do";
	var g_nd = '<%=nd%>';
	var g_proKey = '<%=proKey%>';
	var index;
	var jhsj_id;
	
	//计算本页表格分页数
	function setPageHeight(){
		var getHeight=getDivStyleHeight();
		var height = getHeight-pageTopHeight-pageTitle-getTableTh(2)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
	
	$(function() {
		setPageHeight();
		doInit();
		//按钮绑定事件（查询）
		$("#btnQuery").click(function() {
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			defaultJson.doQueryJsonList(controllername+"?querySj",data,DT1,'bindEvent',false);
			//bindEvent();
		});
		//按钮绑定事件(设计文件管理)
		$("#btn_Sjwj").click(function() {
			if($("#DT1").getSelectedRowIndex()==-1){
				requireSelectedOneRow();
				return;
			}else{
				$(window).manhuaDialog({"title":"设计综合管理>设计文件管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/sjgl/zlsf/zlsfMain.jsp","modal":"1"});
			}
		});
		//按钮绑定事件(概算管理)
		$("#btn_Gs").click(function() {
			if($("#DT1").getSelectedRowIndex()==-1){
				requireSelectedOneRow();
				return;
			}else{
				$(window).manhuaDialog({"title":"设计综合管理>概预算管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/sjgl/gs/gs.jsp","modal":"2"});
			}
		});
		//按钮绑定事件(变更管理)
		$("#btn_Bg").click(function() {
			if($("#DT1").getSelectedRowIndex()==-1){
				requireSelectedOneRow();
				return;
			}else{
				$(window).manhuaDialog({"title":"设计综合管理>变更管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/sjgl/sjbg/sjbgMain.jsp","modal":"1"});
			}
		});
		//按钮绑定事件(计划反馈)
		$("#feedback").click(function() {
			if($("#DT1").getSelectedRowIndex()==-1){
		    	requireSelectedOneRow();
		    	       return;
		    	}else{
		    	$("#resultXML").val($("#DT1").getSelectedRow());
		    	var tempJson = $("#DT1").getSelectedRowJsonObj();
		    	openJhfkPage(tempJson.JHSJID,"1007","queryForm","DT1",encodeURI(controllername+"?querySj"),"2");
		    	}
		});
		//按钮绑定事件(计划反馈作废)
		$("#feedback1").click(function() {
			if($("#DT1").getSelectedRowIndex()==-1){
				requireSelectedOneRow();
				return;
			}else{
				$(window).manhuaDialog({"title":"设计综合管理>统筹计划反馈","type":"text","content":"${pageContext.request.contextPath}/jsp/business/sjgl/sj/feedback.jsp","modal":"2"});
			}
		});
		//按钮绑定事件(设计任务书管理)
		$("#btn_RWS").click(function() {
			if($("#DT1").getSelectedRowIndex()==-1){
				requireSelectedOneRow();
				return;
			}else{
				$(window).manhuaDialog({"title":"设计综合管理>设计任务书管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/sjgl/rwsgl/rwsgl.jsp","modal":"2"});
			}
		});
		
		//按钮绑定事件（导出）
		$("#btnExp").click(function(){
			if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
			      //printTabList("DT1");
				printTabList("DT1","sj_main.xls","XMBH,XMMC,BDBH,BDMC,WCSJ_KCBG,WCSJ_YS,PFNR,GS,CBSJPF_SJ,WCSJ_CQT,CQT_SJ,WCSJ_PQT,PQT_SJ,WCSJ_SGT_SSB,WCSJ_SGT_ZSB,SGT_SJ,BG,JJGSJ","3,1");
			  }
		});	
		bindEvent();
	});
	function bindEvent(){
		$(".showXmxxkInfo").unbind();
		$(".showXmxxkInfo").click(function() {
			var index = $(event.target).closest("tr").index();
	    	$("#DT1").cancleSelected();
	    	$("#DT1").setSelect(index);
			if($("#DT1").getSelectedRowIndex()==-1){
				requireSelectedOneRow();
				return;
			}else{
				var n = $(this).attr("jhsjid");
				$(window).manhuaDialog({"title":"设计综合信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/sjgl/sj/sjxxk.jsp?jhsjid="+n,"modal":"1"});
	    	}
		});
	}
	//页面默认参数
	function doInit(){
		g_bAlertWhenNoResult = false;
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
		defaultJson.doQueryJsonList(controllername+"?queryDrillingDataZh"+condNd+condProKey,data,DT1,null,false);
	}
	//单击行事件
	function tr_click(obj,tabId){
		//if(tabId=="DT1"){
			var rowValue = $("#DT1").getSelectedRow();//获得选中行的json 字符串
			var tempJson = convertJson.string2json1(rowValue);//字符串转JSON对象
			$("#resultXML").val(tempJson);
			jhsj_id=$(obj).attr("JHSJID");
		//}
	}
	//详细信息
	function rowView(index){
		var obj = $("#DT1").getSelectedRowJsonByIndex(index);
		var id = convertJson.string2json1(obj).XMID;
		$(window).manhuaDialog(xmscUrl(id));
	}
	function doRandering(obj){
		var showHtml = "";
		showHtml = "<a href='javascript:void(0)' title='设计综合信息卡'><i class='icon-file showXmxxkInfo' jhsjid='"+obj.JHSJID+"'></i></a>";
		return showHtml;
	}
	
	//具备规划显示√ 否则显示×
	function doJbgh(obj){
		var gh=obj.JBGH;
		var jbgh=gh?('<i title="具备规划条件" class=\"icon-ok\"></i>'):('<i title="不具备规划条件" class=\"icon-remove\"></i>');
		return jbgh;
	}
	//拆迁图反馈
	function doFk_cqt(obj){
		var wcsj=obj.WCSJ_CQT;
		var sj=obj.CQT_SJ;
		var fk=doFk(wcsj,sj);
		return fk;
	}
	//排迁图反馈
	function doFk_pqt(obj){
		var wcsj=obj.WCSJ_PQT;
		var sj=obj.PQT_SJ;
		var fk=doFk(wcsj,sj);
		return fk;
	}
	//施工图反馈
	function doFk_sgt(obj){
		var wcsj=obj.WCSJ_SGT_ZSB;
		var sj=obj.SGT_SJ;
		var fk=doFk(wcsj,sj);
		return fk;
	}
	//概算反馈
	function doFk_gs(obj){
		/* var wcsj=obj.WCSJ_YS; */
		var wcsj=obj.WCSJ_YS;
		var sj=obj.CBSJPF_SJ;
		var fk=doFk(wcsj,sj);
		return fk;
	}
	//反馈方法
  	function doFk(wcsj,sj){
 		var fk="";
		if(sj==""||sj==null){
			fk=wcsj?'<i title="未反馈" class=\"icon-yellow\"></i>':'&nbsp;';
		}else{
			fk='<i title="已反馈" class=\"icon-green\"></i>';
		} 
		return fk;
	} 
  //标段名称
  function doBdmc(obj){
	  var bd_name=obj.BDMC;
	  if(bd_name==null||bd_name==""){
		  /* return '<div style="text-align:center"><abbr title=本条项目信息没有标段内容>—</abbr></div>'; */
		  return '<div style="text-align:center">—</div>';
	  }else{
		  return bd_name;			  
	  }
  }
	function doBg(obj){
		var bg=obj.BG;
		bg=bg?bg:('—');
		return bg;
	}
	//勘测任务书
	function doKCRWS(obj){
		var kcrws=obj.CCRWS;
		var kcrw=kcrws>0?('<i title="具备勘测任务书" class=\"icon-ok\"></i>'):('<i title="不具备勘测任务书" class=\"icon-remove\"></i>');
		return kcrw;
	}
	//设计任务书
	function doSJRWS(obj){
		var sjrws=obj.SHRWS;
		var sjrw=sjrws>0?('<i title="具备设计任务书" class=\"icon-ok\"></i>'):('<i title="不具备设计任务书" class=\"icon-remove\"></i>');
		return sjrw; 
	}
	//概算页面成功提示
	//标段名称
	function doBdbh(obj){
		var bd_bh=obj.BDBH;
		if(bd_bh==null||bd_bh==""){
			return '<div style="text-align:center">—</div>';
		}else{
			return bd_bh;			  
		}
	}
	function showGstj(obj){
		var fk=null;
		var gstj=obj.SFJTGS;
		if(gstj=='false'){
			fk='<i title="手续条件不齐全" class=\"icon-yellow\"></i>';
		}else{
			fk='<i title="手续条件齐全" class=\"icon-green\"></i>';
		} 
		return fk;
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
							</button> </span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<tr>
							</tr>
						</table>
					</form>
					<div style="height: 5px;"></div>
					<div class="row-fluid">
						<div class="overFlowX">
							<table class="table-hover table-activeTd B-table" id="DT1"
								width="100%" type="single" pageNum="10"
								pagingFunction="bindEvent">
								<thead>
									<tr>
										<th name="XH" id="_XH" rowspan="2" colindex=1>
											&nbsp;#&nbsp;
										</th>
										<th fieldname="XMBH" id="Q_XMBH" maxlength="15"
											tdalign="center" CustomFunction="doRandering" rowspan="2"
											colindex=2 noprint="true">
											&nbsp;&nbsp;
										</th>
										<th fieldname="XMBH" id="E_XMHB" maxlength="15"
											rowMerge="true" hasLink="true" linkFunction="rowView"
											rowspan="2" colindex=3>
											&nbsp;项目编号&nbsp;
										</th>
										<th fieldname="XMMC" id="XMMC" maxlength="15" rowMerge="true"
											rowspan="2" colindex=4>
											&nbsp;项目名称&nbsp;
										</th>
										<th fieldname="BDBH" maxlength="15" rowspan="2" colindex=5
											Customfunction="doBdbh">
											&nbsp;标段编号&nbsp;
										</th>
										<th fieldname="BDMC" maxlength="15" rowspan="2" colindex=6
											Customfunction="doBdmc">
											&nbsp;标段名称&nbsp;
										</th>
										<th fieldname="JBGH" id="JBGH" tdalign="center" rowspan="2"
											colindex=7 CustomFunction="doJbgh" noprint="true">
											&nbsp;具备规划&nbsp;
										</th>
										<th fieldname="WCSJ_KCBG" tdalign="center"
											CustomFunction="doKCRWS" rowspan="2" colindex=8>
											&nbsp;勘测任务书&nbsp;
										</th>
										<th fieldname="WCSJ_KCBG" tdalign="center"
											CustomFunction="doSJRWS" rowspan="2" colindex=9>
											&nbsp;设计任务书&nbsp;
										</th>
										<th fieldname="WCSJ_KCBG" tdalign="center" rowspan="2"
											colindex=10>
											&nbsp;勘察报告&nbsp;
										</th>
										<th colspan="4">
											&nbsp;概预算&nbsp;
										</th>
										<th colspan="2">
											&nbsp;拆迁图&nbsp;
										</th>
										<th colspan="2">
											&nbsp;排迁图&nbsp;
										</th>
										<th colspan="3">
											&nbsp;施工图&nbsp;
										</th>
										<th fieldname="BG" tdalign="center" rowspan="2" colindex=22
											CustomFunction="doBg">
											&nbsp;变更&nbsp;
										</th>
										<th fieldname="JJGSJ" tdalign="center" rowspan="2" colindex=23>
											&nbsp;交竣工时间&nbsp;
										</th>
									</tr>
									<tr>
										<th fieldname="SFJTGS" colindex=11 tdalign="center"
											CustomFunction="showGstj">
											&nbsp;概预算条件&nbsp;
										</th>
										<th fieldname="WCSJ_YS" colindex=12 tdalign="center">
											&nbsp;完成时间&nbsp;
										</th>
										<th fieldname="GYS" colindex=13 tdalign="right">
											&nbsp;概预算&nbsp;
										</th>
										<th fieldname="SJGS" colindex=14 tdalign="center"
											CustomFunction="doFk_gs" noprint="true">
											&nbsp;是否反馈&nbsp;
										</th>
										<th fieldname="WCSJ_CQT" colindex=15 tdalign="center">
											&nbsp;时间&nbsp;
										</th>
										<th fieldname="CQT_SJ" colindex=16 tdalign="center"
											CustomFunction="doFk_cqt" noprint="true">
											&nbsp;是否反馈&nbsp;
										</th>
										<th fieldname="WCSJ_PQT" colindex=17 tdalign="center">
											&nbsp;时间&nbsp;
										</th>
										<th fieldname="PQT_SJ" colindex=18 tdalign="center"
											CustomFunction="doFk_pqt" noprint="true">
											&nbsp;是否反馈&nbsp;
										</th>
										<th fieldname="WCSJ_SGT_SSB" tdalign="center" colindex=19>
											&nbsp;送审版&nbsp;
										</th>
										<th fieldname="WCSJ_SGT_ZSB" colindex=20 tdalign="center">
											&nbsp;正式版&nbsp;
										</th>
										<th fieldname="SGT_SJ" colindex=21 tdalign="center"
											CustomFunction="doFk_sgt" noprint="true">
											&nbsp;是否反馈&nbsp;
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
		</div>

		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="jhsj.xmbh,jhsj.xmbs,jhsj.pxh">
				<input type="hidden" name="queryResult" id="queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>