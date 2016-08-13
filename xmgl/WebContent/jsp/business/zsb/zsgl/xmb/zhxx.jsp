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
  var controllername= "${pageContext.request.contextPath }/zsb/xmb/xmbController.do";
  var json;		//页面传值
  var index;	//行选值
  var x=1;//当前行使用方法的列数
  var z=1;//主要显示当前行
  var xalert="";//提醒信息
  
  function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#cjdwList").attr("pageNum",pageNum);
	}
  
  
	$(function() {
		setPageHeight();
		initCommonQueyPage();
		var btn = $("#query");
		btn.click(function() {
			//生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryXmb",data,DT1,null,false);
			//bindEvent();
				});
		var zxm=$("#zxm");
		zxm.click(function(){
			if($("#DT1").getSelectedRowIndex()==-1)
			 {
				requireSelectedOneRow();
			    return;
			 }
			$(window).manhuaDialog({"title":"征地拆迁管理>场地移交设置","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zsb/zsgl/xmb/jhfk.jsp","modal":"3"});
		});
		var clean=$("#clean");
		clean.click(function(){
			$("#queryForm").clearFormResult();
			initCommonQueyPage();
		});
		ready();
		//按钮绑定事件（导出）
		$("#excel").click(function(){
			if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
			      printTabList("DT1");
			  }
		});
		//统筹计划反馈
	    $("#tcjhfk").click(function() {
	        if($("#DT1").getSelectedRowIndex()==-1){
	        	requireSelectedOneRow();
	            return;
	    	}else{
	        	$("#resultXML").val($("#DT1").getSelectedRow());
	        	var tempJson = $("#DT1").getSelectedRowJsonObj();
	        	openJhfkPage(tempJson.JHSJID,"1009","queryForm","DT1",encodeURI(controllername+"?queryXmb"));
	    	}
	    });
		
	});
    function ready() {
    	//g_bAlertWhenNoResult=false;	
   		 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		defaultJson.doQueryJsonList(controllername+"?queryXmb",data,DT1,null,true);
		//g_bAlertWhenNoResult=true;
		//bindEvent();
   };
	function bindEvent(){
		$(".showXmxxkInfo").unbind();
		$(".showXmxxkInfo").click(function() {
			$("#DT1").cancleSelected();
	    	//var row = $(event.target).parent().parent().index();
	    	var index = $(event.target).closest("tr").index();
	    	$("#DT1").setSelect(index);
			//parent.$("body").manhuaDialog({"title":"征收项目信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zsb/zsgl/xmb/zsxmxxk.jsp?jhsjid="+n,"modal":"1"});
			$(window).manhuaDialog({"title":"征收项目信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zsb/zsgl/xmb/zsxmxxk.jsp","modal":"1"});
		});
	}
  //详细信息
    function rowView(index){
        var obj=$("#DT1").getSelectedRowJsonByIndex(index);//获取行对象
    	var id=convertJson.string2json1(obj).XMID;
    	$(window).manhuaDialog(xmscUrl(id));//调用公共方法,根据项目编号查询
    }
	//行选按钮
	function tr_click(obj){
		json = JSON.stringify(obj);
		json=encodeURI(json);
		index =	$("#DT1").getSelectedRowIndex();
	}
	function doRandering(obj){
		var showHtml = "";
		showHtml = "<a href='javascript:void(0);' title='征收任务信息卡' onclick='bindEvent();'><i class='icon-file showXmxxkInfo' jhsjid='"+obj.JHSJID+"'></i></a>";
		return showHtml;
	}
	//子页面更新父页面方法
		function xgh(data)
	{
		var subresultmsgobj = defaultJson.dealResultJson(data);
		var comprisesJson = $("#DT1").comprisesJson(subresultmsgobj,index);
		$("#DT1").updateResult(JSON.stringify(comprisesJson),DT1,index);
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(index);
		json = $("#DT1").getSelectedRow();
		json=encodeURI(json);
		xSuccessMsg("操作成功","");
		bindEvent();
	}

	//显示拆迁图日期
	function showCqt_date(obj){
		var sf=parseInt(obj.SFCQ_JH);
		var cqt=obj.CQTQDSJ;
		var cqt_date=opera_date(sf,cqt);
		return cqt_date;
	}
	//显示计划完成日期
	function showJhwc_date(obj){
		var sf=parseInt(obj.SFCQ_JH);
		var jhwc = obj.JHWCSJ;
		var jhwc_date=opera_date(sf,jhwc);
		return jhwc_date;
	}
	//显示实际完成日期
	function showSjwc_date(obj){
		var sf=parseInt(obj.SFCQ_JH);
		var sjwc = obj.SJWCRQ;
		var sjwc_date=opera_date(sf,sjwc);
		return sjwc_date;
	}
	//显示场地移交时间
	function showCdyj_date(obj){
		var sf = parseInt(obj.SFCQ_JH);
		var cdyj=obj.CDSJSJ;
		var cdyj_date=opera_date(sf,cdyj);
		return cdyj_date;
	}
	//显示时间处理
	function opera_date(sf,date){
		var value_date;
		if(sf==0){
			value_date='<div style="text-align:center">—</div>';
		}else{
			value_date=date?date:'';
		}
		return value_date;
	}
	//规划显示
	function showGh(obj){
		var mark=obj.MARK;
		var date_gh =obj.GHXKZ;
		var sf_gh=obj.SFGH;
		var gh_show=opera_sx(date_gh,sf_gh,mark);
		return gh_show;
	}
	//土地显示
	function showTd(obj){
		var mark=obj.MARK;
		var date_td =obj.TDSYZ;
		var sf_td=obj.SFTD;
		var td_show=opera_sx(date_td,sf_td,mark);
		return td_show;
	}
	//显示操作
	function opera_sx(date,sf,mark){
		var show;
		if(mark==null||mark==""){
			show='<div style="text-align:center"><i title="未办理" class=\"icon-yellow\"></i></div>';
		}else{
			if(sf==""||sf==null){
				show=date?date:('<div style="text-align:center"><i title="未办理" class=\"icon-yellow\"></i></div>');
			}else{
				show='<div style="text-align:center">—</div>';
			}
		}
		return show;
	} 
	//状态的反馈显示
	function show_ztFb(obj){
		var fkzt=obj.ISFK;
		if(fkzt==null||fkzt==""){
			show='&nbsp;';
		}else{
			if(fkzt=="1"){
				show='<div style="text-align:center"><i title="已反馈" class=\"icon-green\"></i></div>';
			}else{
				show='<div style="text-align:center"><i title="未反馈" class=\"icon-yellow\"></i></div>';
			}
		}
		return show;
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
						征收综合信息
						<span class="pull-right">
							<app:oPerm url="jsp/business/zsb/zsgl/xmb/jhfk.jsp">
								<button id="tcjhfk" class="btn" type="button">统筹计划反馈</button>
					  		</app:oPerm>
							<app:oPerm url="jsp/business/zsb/zsgl/xmb/cdyjsz">
								<button id="zxm" class="btn" type="button">场地移交设置</button>
					  		</app:oPerm>
					  			<button id="excel" class="btn" type="button">导出</button>
				  		</span>
					</h4>
					<form method="post" id="queryForm" width="100%">
						<table class="B-table">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<INPUT type="text" class="span12" kind="text"
										fieldname="rownum" value="1000" operation="<=" keep="true">
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
							<tr>

								<!--公共的查询过滤条件 -->
								<jsp:include page="/jsp/business/common/commonQuery.jsp"
									flush="true">
									<jsp:param name="prefix" value="jhsj" />
								</jsp:include>

								<th width="5%" class="right-border bottom-border text-right">
									是否拆迁
								</th>
								<td width="7%" class="right-border bottom-border">
									<select class="span12 2characters" name="SFCQ" fieldname="jhsj.ISZC"
										operation="=" id="SFCQ" kind="dic" src="SF" defaultMemo="全部">
									</select>
								</td>
								<td class="text-left bottom-border text-right">
									<button id="query" class="btn btn-link" type="button">
										<i class="icon-search"></i>查询
									</button>
									<button id="clean" class="btn btn-link" type="button">
										<i class="icon-trash"></i>清空
									</button>
								</td>
							</tr>
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
						<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" printFileName="征收综合信息">
							<thead>
								<tr>
									<th name="XH" id="_XH" rowspan="2" colindex=1>
										&nbsp;#&nbsp;
									</th>
									<th fieldname="XMBH" rowspan="2" colindex=2
										tdalign="center" CustomFunction="doRandering" noprint="true">
										&nbsp;&nbsp;
									</th>
									<th fieldname="XMBH" hasLink="true"
										linkFunction="rowView" rowspan="2" colindex=3>
										&nbsp;项目编号&nbsp;
									</th>
									<th fieldname="XMMC" maxlength="20" rowspan="2" colindex=4>
										&nbsp;项目名称&nbsp;
									</th>
									<!-- <th width="50px" fieldname="SFCQ_SJ" tdalign="center" rowspan="2" CustomFunction="show_ztFb"
										colindex=5 noprint="true">
										统筹计划<p></p>反馈状态
									</th> -->
									<th width="50px" fieldname="XMBDDZ" rowspan="2" colindex=5>
										&nbsp;项目地址&nbsp;
									</th>
									<th width="50px" fieldname="SFCQ_JH" tdalign="center" rowspan="2"
										colindex=6>
										&nbsp;是否拆迁&nbsp;
									</th>
									<th width="100px" fieldname="CQTQDSJ" tdalign="center"
										rowspan="2" colindex=7  CustomFunction="showCqt_date">
										拆迁图
										<p></p>
										取得时间
									</th>
									<th width="100px" fieldname="GHXKZ" tdalign="center" rowspan="2"
						 				colindex=8 CustomFunction="showGh">
										用地规划
										<p></p>
										许可证&nbsp;
									</th>
									<th width="100px" fieldname="TDSYZ" tdalign="center" rowspan="2"
										colindex=9 CustomFunction="showTd">
										&nbsp;土地使用证&nbsp;
									</th>
									<th colspan="2">
										&nbsp;完成时间&nbsp;
									</th>
									<th width="100px" fieldname="CDSJSJ" tdalign="center"
										rowspan="2" colindex=12  CustomFunction="showCdyj_date">
										&nbsp;场地移交
										<p></p>
										时间&nbsp;
									</th>
								</tr>
								<tr>
									<th width="100px" fieldname="JHWCSJ" tdalign="center"
										colindex=10  CustomFunction="showJhwc_date">
										&nbsp;计划&nbsp;
									</th>
									<th width="100px" fieldname="SJWCRQ" tdalign="center"
										colindex=11  CustomFunction="showSjwc_date">
										&nbsp;实际&nbsp;
									</th>
								</tr>
							</thead>
							<tbody></tbody>
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
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="txtFilter" order="asc" fieldname="jhsj.xmbh,jhsj.PXH" id="txtFilter">
				<input type="hidden" name="queryResult" id ="queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>