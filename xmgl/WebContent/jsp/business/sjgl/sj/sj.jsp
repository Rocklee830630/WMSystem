<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<style type="text/css"> 
	i.showXmxxkInfo:hover,i.showXmxxkInfo:focus {
		cursor: pointer;
	}
</style>
<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/sjgl/sj/sjController.do";
	var index;
	var jhsj_id;
/* 	function getCurrentPage(obj){
		alert($("#queryResult").val());
		var queryData = convertJson.string2json1($("#queryResult").val());
		var currP = 1;
		//alert(queryData.pages.currentpagenum);
		if(queryData.pages.currentpagenum){
			currP = queryData.pages.currentpagenum;
		}
		//alert(currP);
		return currP;
	} */
	//页面初始化
	
	//计算本页表格分页数
  function setPageHeight(){
  	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
  	var pageNum = parseInt(height/pageTableOne,10);
  	$("#DT1").attr("pageNum",pageNum);
  }
	
	$(function() {
		setPageHeight();
		initCommonQueyPage();
		init();
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
		//按钮绑定事件（清空查询条件）
		$("#btnClear").click(function() {
			$("#queryForm").clearFormResult();
      			initCommonQueyPage();
		});
		bindEvent();
	});
	function bindEvent(){
		/**
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
		*/
	}
	//页面默认参数
	function init(){
		//g_bAlertWhenNoResult = false;
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		defaultJson.doQueryJsonList(controllername+"?querySj",data,DT1,'bindEvent',true);
		//g_bAlertWhenNoResult = true;
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
		var id = obj.JHSJID;
		showHtml = "<a href='javascript:void(0)' title='设计综合信息卡' onclick='openInfoCard()'><i class='icon-file showXmxxkInfo' jhsjid='"+obj.JHSJID+"'></i></a>";
		return showHtml;
	}
	function openInfoCard(n){
		var index = $(event.target).closest("tr").index();
    	$("#DT1").cancleSelected();
    	$("#DT1").setSelect(index);
		if($("#DT1").getSelectedRowIndex()==-1){
			requireSelectedOneRow();
			return;
		}else{
			$(window).manhuaDialog({"title":"设计综合信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/sjgl/sj/sjxxk.jsp","modal":"1"});
    	}
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
		var isneed=obj.ISCQT;
		var fk=doFk(wcsj,sj,isneed);
		return fk;
	}
	//排迁图反馈
	function doFk_pqt(obj){
		var wcsj=obj.WCSJ_PQT;
		var sj=obj.PQT_SJ;
		var isneed=obj.ISPQT;
		var fk=doFk(wcsj,sj,isneed);
		return fk;
	}
	//施工图反馈
	function doFk_sgt(obj){
		var wcsj=obj.WCSJ_SGT_ZSB;
		var sj=obj.SGT_SJ;
		var isneed=obj.ISSGT;
		var fk=doFk(wcsj,sj,isneed);
		return fk;
	}
	//概算反馈
	function doFk_gs(obj){
		/* var wcsj=obj.WCSJ_YS; */
		var wcsj=obj.WCSJ_YS;
		var sj=obj.CBSJPF_SJ;
		var isneed=obj.ISCBSJPF;
		var fk=doFk(wcsj,sj,isneed);
		return fk;
	}
	//反馈方法
  	function doFk(wcsj,sj,isNeed){
 		var fk=" &nbsp;";
 		if('1'==isNeed){
			if(sj==""||sj==null){
				fk=wcsj?'<i title="未反馈" class=\"icon-yellow\"></i>':'&nbsp;';
			}else{
				fk='<i title="已反馈" class=\"icon-green\"></i>';
			} 
 		}
		return fk;
	} 
	
  //弹出页回显修改
	function xiugaihang()
	{
		$.ajax(
		{
			   url : controllername+"?querySj",//此处定义后台controller类和方法
		         data : {jhsjid:jhsj_id},    //此处为传入后台的数据，可以为json，可以为string，如果为json，那起结构必须和后台接收的bean一致或和bean的get方法名一致，例如｛id：1，name：2｝后台接收的bean方法至少包含String id,String name方法  如果为string，那么可以写为{portal: JSON.stringify(data)}, 后台接收的时候参数可以为String，名字必须和前台保持一致及定义为String portal
		         dataType : 'json',//此处定义返回值的类型为string，详见样例代码
		         async : false,   //同步执行，即执行完ajax方法后才可以执行下面的函数，如果不设置则为异步执行，及ajax和其他函数是异步的，可能后面的代码执行完了，ajax还没执行
		         success : function(result) {
		         var resultmsg = result.msg; //返回成功事操作
		      	 var index= $("#DT1").getSelectedRowIndex();
				 var subresultmsgobj1 = defaultJson.dealResultJson(resultmsg);
				 $("#DT1").updateResult(JSON.stringify(subresultmsgobj1),DT1,index);
				 $("#DT1").setSelect(index);
		         },
		         error : function(result) {//返回失败操作
		           defaultJson.clearTxtXML();
		          }			
		}		
		);
	}
  //概算反馈
  function gs_feedback(){
	  var row_index=$("#DT1").getSelectedRowIndex();
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		var tempJson = convertJson.string2json1(data);
		var a=$("#DT1").getCurrentpagenum();
		tempJson.pages.currentpagenum=a;
		data = JSON.stringify(tempJson);
		defaultJson.doQueryJsonList(controllername+"?querySj",data,DT1);
		bindEvent();
		$("#DT1").setSelect(row_index);
		var json=$("#DT1").getSelectedRow();
		json=encodeURI(json);
		successInfo("","");
  }
  //概算反馈
  function gsPar_feedback(){
	  var row_index=$("#DT1").getSelectedRowIndex();
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		var tempJson = convertJson.string2json1(data);
		var a=$("#DT1").getCurrentpagenum();
		tempJson.pages.currentpagenum=a;
		data = JSON.stringify(tempJson);
		defaultJson.doQueryJsonList(controllername+"?querySj",data,DT1);
		bindEvent();
		$("#DT1").setSelect(row_index);
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
  function doKCRWS(obj)
  {
		var kcrws=obj.CCRWS;
		var kcrw=kcrws>0?('<i title="具备勘测任务书" class=\"icon-ok\"></i>'):('<i title="不具备勘测任务书" class=\"icon-remove\"></i>');
		return kcrw;
  }
  //设计任务书
  function doSJRWS(obj)
  {
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
		<h4 class="title">设计综合管理
			<span class="pull-right">
			
			<!-- <button id="feedback1" class="btn" type="button">统筹计划反馈</button> -->
			<app:oPerm url="jsp/business/sjgl/sj/feedback.jsp">
				<button id="feedback" class="btn" type="button">统筹计划反馈</button>
	  		</app:oPerm>
			<app:oPerm url="jsp/business/sjgl/zlsf/zlsfMain.jsp">
				<button id="btn_Sjwj" class="btn" type="button">设计文件管理</button>
	  		</app:oPerm>
			<app:oPerm url="jsp/business/sjgl/gs/gs.jsp">
				<button id="btn_Gs" class="btn" type="button">概预算管理</button>
	  		</app:oPerm>
			<app:oPerm url="jsp/business/sjgl/sjbg/sjbgMain.jsp">
				<button id="btn_Bg" class="btn" type="button">变更管理</button>
  			</app:oPerm>
  			<app:oPerm url="/jsp/business/sjgl/rwsgl/rwsgl.jsp">
				<button id="btn_RWS" class="btn" type="button">设计任务书管理</button>
  			</app:oPerm>
				<button id="btnExp" class="btn" type="button">导出</button>
				
				
			</span>
		</h4>
	<form method="post" id="queryForm">
		<table class="B-table" width="100%">
			<tr>
				<jsp:include page="/jsp/business/common/commonQuery.jsp" flush="true"> 
					<jsp:param name="prefix" value="jhsj" />
				</jsp:include>
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
	<div style="height: 5px;"></div>
	<div class="row-fluid">
		<div class="overFlowX">
		<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" pageNum="10" pagingFunction="bindEvent" printFileName="设计综合管理">
			<thead>
				<tr>
					<th name="XH" id="_XH" rowspan="2" colindex=1>
						&nbsp;#&nbsp;
					</th>
					<th fieldname="XMBH" id="Q_XMBH" tdalign="center" CustomFunction="doRandering" rowspan="2" colindex=2 noprint="true">
						&nbsp;&nbsp;
					</th>
					<th fieldname="XMBH" id="E_XMHB" maxlength="15" rowMerge="true" hasLink="true" linkFunction="rowView" rowspan="2" colindex=3>
						&nbsp;项目编号&nbsp;
					</th>
					<th fieldname="XMMC" id="XMMC" maxlength="15" rowMerge="true" rowspan="2" colindex=4>
						&nbsp;项目名称&nbsp;
					</th>
					<th fieldname="BDBH" maxlength="15" rowspan="2" colindex=5 Customfunction="doBdbh">
						&nbsp;标段编号&nbsp;
					</th>
					<th fieldname="BDMC" maxlength="15" rowspan="2" colindex=6 Customfunction="doBdmc">
						&nbsp;标段名称&nbsp;
					</th>
					<th fieldname="XMBDDZ" maxlength="15" rowspan="2" colindex=7 >
						&nbsp;项目地址&nbsp;
					</th>
					<th fieldname="JBGH" id="JBGH" tdalign="center" rowspan="2" colindex=8 CustomFunction="doJbgh" noprint="true">
						&nbsp;具备规划&nbsp;
					</th>
					<th fieldname="WCSJ_KCBG" tdalign="center" CustomFunction="doKCRWS" rowspan="2" colindex=9>
						&nbsp;勘测任务书&nbsp;
					</th>
					<th fieldname="WCSJ_KCBG" tdalign="center" CustomFunction="doSJRWS" rowspan="2" colindex=10>
						&nbsp;设计任务书&nbsp;
					</th>
					<th fieldname="WCSJ_KCBG" tdalign="center" rowspan="2" colindex=11>
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
					<th fieldname="BG" tdalign="center" rowspan="2" colindex=23 CustomFunction="doBg">
						&nbsp;变更&nbsp;
					</th>
					<th fieldname="ZBGFY" tdalign="center" rowspan="2" colindex=24 >
						&nbsp;变更金额&nbsp;
					</th>
					<th fieldname="JJGSJ" tdalign="center" rowspan="2" colindex=25>
						&nbsp;交竣工时间&nbsp;
					</th>
				</tr>
				<tr>
					<th fieldname="SFJTGS" colindex=12 tdalign="center" CustomFunction="showGstj">&nbsp;概预算条件&nbsp;</th>
					<th fieldname="WCSJ_YS" colindex=13 tdalign="center">&nbsp;完成时间&nbsp;</th>
					<th fieldname="GYS" colindex=14 tdalign="right">&nbsp;概预算&nbsp;</th>
					<th fieldname="SJGS" colindex=15 tdalign="center" CustomFunction="doFk_gs" noprint="true">&nbsp;是否反馈&nbsp;</th>
					<th fieldname="WCSJ_CQT" colindex=16 tdalign="center">&nbsp;时间&nbsp;</th>
					<th fieldname="CQT_SJ" colindex=17 tdalign="center" CustomFunction="doFk_cqt" noprint="true">&nbsp;是否反馈&nbsp;</th>
					<th fieldname="WCSJ_PQT" colindex=18 tdalign="center">&nbsp;时间&nbsp;</th>
					<th fieldname="PQT_SJ" colindex=19 tdalign="center" CustomFunction="doFk_pqt" noprint="true">&nbsp;是否反馈&nbsp;</th>
					<th fieldname="WCSJ_SGT_SSB" tdalign="center" colindex=20>&nbsp;送审版&nbsp;</th>
					<th fieldname="WCSJ_SGT_ZSB" colindex=21 tdalign="center">&nbsp;正式版&nbsp;</th>
					<th fieldname="SGT_SJ" colindex=22 tdalign="center" CustomFunction="doFk_sgt" noprint="true">&nbsp;是否反馈&nbsp;</th>
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
		<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML">
			<input type="hidden" name="txtXML" id="txtXML">
			<input type="hidden" name="resultXML" id="resultXML">
			<input type="hidden" name="txtFilter" order="asc"  fieldname ="jhsj.xmbh,jhsj.xmbs,jhsj.pxh">
			<input type="hidden" name="queryResult" id ="queryResult">
		<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
		</FORM>
	</div>
</body>
</html>