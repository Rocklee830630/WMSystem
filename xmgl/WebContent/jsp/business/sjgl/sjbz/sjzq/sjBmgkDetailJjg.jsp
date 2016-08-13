<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<app:dialogs></app:dialogs>
		<title>交竣工管理</title>
		<%	
			String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
		%>
		<script type="text/javascript" charset="utf-8">
 
var controllername= "${pageContext.request.contextPath }/sjSjzqController.do";
var oTable1,json,id;
var iswg;
var wg=false;
var weiwg=false;
var g_nd = '<%=nd%>';
var g_proKey = '<%=proKey%>';

//计算本页表格分页数
function setPageHeight(){
	var getHeight=getDivStyleHeight();
	var height = getHeight-pageTopHeight-pageTitle-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
}
//查询
$(function() {
	setPageHeight();
	doInit();
});
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
	defaultJson.doQueryJsonList(controllername+"?queryDrillingDataJjg"+condNd+condProKey,data,DT1,null,false);
}

//点击获取行对象
function tr_click(obj,tabListid){
	id=$(obj).attr("ID");
	json=$("#DT1").getSelectedRow();
	json=encodeURI(json); 
	
}


//弹出维护窗口
function OpenMiddleWindow_wh(){
	$(window).manhuaDialog({"title":"交竣工>维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jjg/jjgwh.jsp","modal":"1"});
}


//子页调用，查询
function query()
{
	initCommonQueyPage();
	g_bAlertWhenNoResult=false;
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?query_jjg",data,DT1);
	g_bAlertWhenNoResult=true;
}
//子页面调用修改行
function xiugaihang(data)
{
	var index =	$("#DT1").getSelectedRowIndex();
	var subresultmsgobj = defaultJson.dealResultJson(data);
	//var comprisesJson = $("#DT1").comprisesJson(subresultmsgobj,index);
	$("#DT1").updateResult(JSON.stringify(subresultmsgobj),DT1,index);
	$("#DT1").cancleSelected();
	$("#DT1").setSelect(index);
	json = $("#DT1").getSelectedRow();
	//json串加密
	json=encodeURI(json);
	xSuccessMsg("操作成功","");
}
//子页面调用添加行
function tianjiahang(data)
{
	var subresultmsgobj = defaultJson.dealResultJson(data);
	$("#DT1").insertResult(JSON.stringify(subresultmsgobj),DT1,1);
	$("#DT1").cancleSelected();
	$("#DT1").setSelect(0);
	json = $("#DT1").getSelectedRow();
	//json串加密
    json=encodeURI(json);
    xSuccessMsg("操作成功","");
		
}

//详细信息
function rowView(index){
    var obj=$("#DT1").getSelectedRowJsonByIndex(index);//获取行对象
	//var xmbh=eval("("+obj+")").XXMBH;//取行对象<项目编号>
	var XMID = convertJson.string2json1(obj).XMID;
	$(window).manhuaDialog(xmscUrl(XMID));//调用公共方法,根据项目编号查询
}
//交工时间判断
function queryJGSJ(obj){
    if(obj.JGYSSJ!=""){
		if(obj.JGYSRQ == ""){
			return '—';
		}
   	}
}
function issfwg()
{
	$("input[type='checkbox']").each(function(i)
			{
		       if($(this).is(':checked')){
			 //  cond += $(this).attr('value')+",";
			   if($(this).attr('value')=='shi'){
				   iswg=0;
				   wg=true;
			   }
			   if($(this).attr('value')=='fou'){
				   iswg=1;
				   weiwg=true;
			   }
		    }});
	if(wg==true&&weiwg==true)
		{
		 iswg=3;
		}
	 wg=false;
	 weiwg=false;
}
//判断是否是项目
function doBdmc(obj){
	  var bd_name=obj.BDMC;
	  if(bd_name==null||bd_name==""){
		  /* return '<div style="text-align:center"><abbr title=本条项目信息没有标段内容>—</abbr></div>'; */
		  return '<div style="text-align:center">—</div>';
	  }else{
		  return bd_name;			  
	  }
}
	function queryHuiDiao()
	{
		iswg='';
	}
	  //标段编号
    function doBdBH(obj){
	  var bd_name=obj.BDBH;
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
					<h4 class="row-fluid">
						<span class="pull-right">
							<button class="btn" id="do_excel" type="button">
								导出
							</button> </span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" style="width: 100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<INPUT type="text" class="span12" kind="text" id="num"
										fieldname="rownum" value="1000" operation="<=" />
								</TD>
							</TR>
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
						<table width="100%" class="table-hover table-activeTd B-table"
							id="DT1" type="single">
							<thead>
								<tr>
									<th id="_XH" name="XH" rowspan="2" tdalign="center"
										rowMerge="true" colindex=1>
										&nbsp;#&nbsp;
									</th>
									<th fieldname="XMBH" rowspan="2" colindex=2 hasLink="true"
										rowMerge="true" linkFunction="rowView">
										&nbsp;项目编号&nbsp;
									</th>
									<th fieldname="XMMC" rowspan="2" colindex=3 rowMerge="true"
										maxlength="15">
										&nbsp;项目名称&nbsp;
									</th>
									<th fieldname="XMDZ" rowspan="2" colindex=4 maxlength="15">
										&nbsp;项目地址&nbsp;
									</th>
									<th fieldname="XMGLGS" rowspan="2" colindex=5 maxlength="15">
										&nbsp;项目管理公司&nbsp;
									</th>
									<th fieldname="BDBH" rowspan="2" maxlength="15" colindex=6
										Customfunction="doBdBH">
										&nbsp;标段编号&nbsp;
									</th>
									<th fieldname="BDMC" rowspan="2" colindex=7
										Customfunction="doBdmc" maxlength="15">
										&nbsp;标段名称&nbsp;
									</th>
									<th fieldname="WGRQ" rowspan="2" colindex=8 tdalign="center">
										&nbsp;完工时间&nbsp;
									</th>
									<th fieldname="JGYSRQ" rowspan="2" colindex=9
										CustomFunction="queryJGSJ" tdalign="center">
										&nbsp;交工时间&nbsp;
									</th>
									<th fieldname="JGYSSJ" rowspan="2" colindex=10 tdalign="center">
										&nbsp;竣工时间&nbsp;
									</th>
									<th fieldname="SGDW" rowspan="2" colindex=11 maxlength="15">
										&nbsp;施工单位&nbsp;
									</th>
									<th fieldname="JLDW" rowspan="2" colindex=12 maxlength="15">
										&nbsp;监理单位&nbsp;
									</th>
									<th colspan="3">
										&nbsp;交工&nbsp;
									</th>
									<th colspan="3">
										&nbsp;竣工&nbsp;
									</th>
								</tr>
								<tr>
									<th fieldname="JIAOGJSDW" colindex=13 maxlength="10">
										&nbsp;接收单位&nbsp;
									</th>
									<th fieldname="JIAOGJSR" colindex=14 maxlength="10">
										&nbsp;接收人&nbsp;
									</th>
									<th fieldname="JIAOGONGFJ" colindex=15 noprint="true">
										&nbsp;附件&nbsp;
									</th>
									<th fieldname="JUNGJSDW" colindex=16 maxlength="10">
										&nbsp;参加单位&nbsp;
									</th>
									<th fieldname="JUNGJSR" colindex=17 maxlength="10">
										&nbsp;参加人&nbsp;
									</th>
									<th fieldname="JUNGONGFJ" colindex=18 noprint="true">
										&nbsp;附件&nbsp;
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
				<input type="hidden" name="queryXML" />
				<input type="hidden" name="txtXML" />
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="sj.xmbh, sj.xmbs, sj.pxh" />
				<input type="hidden" name="resultXML" id="resultXML" />
				<input type="hidden" name="queryResult" id="queryResult" />
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData" />
			</FORM>
		</div>
	</body>
</html>