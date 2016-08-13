<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%
	String xmid= request.getParameter("xmid");
	String bdid= request.getParameter("bdid");
	String GC_JH_SJ_ID= request.getParameter("GC_JH_SJ_ID");
%>
<app:base/>
<app:dialogs></app:dialogs>
<title>工程周报管理</title>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script type="text/javascript" charset="utf-8">
var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
var controllername= "${pageContext.request.contextPath }/gczb/gczbController.do";
bdid='<%=bdid%>';

//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery*2-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
}

//初始化查询
$(document).ready(function(){
	
	setPageHeight();
	setDefaultNd("ND");
	//setDefaultOption($("#ND"),new Date().getFullYear());
	//生成json串
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?query&xmglgs="+$("#XMGLGS").val(),data,DT1,null,true);	
	

    
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		  if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
		      printTabList("DT1");
		  }
	});
	//自动完成项目名称模糊查询
	showAutoComplete("XMMC",autocompleteXmmcController+"?xmmcAutoQueryByXmglgs","getXmmcQueryCondition"); 
});


//查询
$(function() {
	var btn=$("#example_query");
	btn.click(function() {
		//生成json串
		var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query&xmglgs="+$("#XMGLGS").val(),data,DT1,null,false);	
});
	
	
	//清空查询表单
  var btn_clearQuery=$("#query_clear");
  btn_clearQuery.click(function() {
    $("#queryForm").clearFormResult();
    setDefaultNd("ND");
    $("#XMGLGS").val("");
    //setDefaultOption($("#ND"),new Date().getFullYear());
  	//setDefaultOption($("#ND"),new Date().getFullYear());
      //其他处理放在下面
  });
  
	//查看现场照片
  var btn_viewPic=$("#viewPic");
  btn_viewPic.click(function() {
	  $(window).manhuaDialog({"title":"周报>现场照片","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/zbgl/viewPic.jsp","modal":"1"});
  });

 });



//项目名称自动模糊查询参数
function getXmmcQueryCondition(){
	var initData = '{"querycondition":{"conditions":[]}}';
	var jsonData = convertJson.string2json1(initData);
	//年度
	if("" != $("#ND").val()){
		var defineCondition = {"value": $("#ND").val(),"fieldname":"t.ND","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	//项目名称
	if("" != $("#XMMC").val()){
		var defineCondition = {"value": "%"+$("#XMMC").val()+"%","fieldname":"t.XMMC","operation":"like","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	if("" != $("#XMGLGS").val()){
		var defineCondition = {"value": +$("#XMGLGS").val(),"fieldname":"t.XMGLGS","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	return JSON.stringify(jsonData);
}


//详细信息
function rowView(index){
    var obj=$("#DT1").getSelectedRowJsonByIndex(index);//获取行对象
	var xdkid=convertJson.string2json1(obj).GC_TCJH_XMXDK_ID;//取行对象<项目编号>
	$(window).manhuaDialog(xmscUrl(xdkid));//调用公共方法,根据项目编号查询
}


//判断标段是否有值
function pdbd(obj){
	var bdid = obj.BDID;
	if(bdid=='undefinde'||bdid=='')
	{
		return '<div style="text-align:center">—</div>'
	}	
}


//周报图标
function zblb(obj)
{
	var xmid=obj.GC_TCJH_XMXDK_ID;
	var bdid=obj.BDID;
	return '<a href="javascript:void(0);"><i title="周报列表" class="icon-align-justify" onclick="showLb(\''+xmid+'\',\''+bdid+'\')"></i></a>';
}


//周报列表展示界面
function showLb(xmid,bdid)
{
	$(window).manhuaDialog({"title":"工程周报>周报列表","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/zbgl/zblb.jsp?xmid="+xmid+"&bdid="+bdid,"modal":"2"});
}


//现场图片图标
function viewimg(obj){
	var isfj=obj.XCZP_SV;
	if(isfj!='' && isfj!=undefined)
	{
		return '<a href="javascript:void(0);"><img src=\"/xmgl/images/icon-annex.png\" title="现场照片" onclick="showPic(\''+obj.XCZP+'\')"></a>';
	}	
}


//现场照片
function showPic(ywid)
{
	$(window).manhuaDialog({"title":"现场照片","type":"text","content":g_sAppName +"/jsp/business/gcb/zbgl/viewimg.jsp?ywid="+ywid+"&lb=0311","modal":"2"});
}


//判断标段编号是否有值
function pdbdbh(obj){
	var bdid = obj.BDID;
	if(bdid=='undefinde'||bdid=='')
	{
		return '<div style="text-align:center">—</div>'
	}	
}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">	
			<h4 class="title">
				工程周报汇总
				<span class="pull-right">
					<!-- <button class="btn" id="viewPic">现场照片</button> -->					
					<button class="btn" id="btnExpExcel">导出</button>					
				</span>
			</h4>
			<form method="post" id="queryForm"  >
      			<table class="B-table" width="100%">
      				<!--可以再此处加入hidden域作为过滤条件 -->
			      	<TR  style="display:none;">
				        <TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
						</TD>
			        </TR>
        			<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>
						<th width="7%" class="right-border bottom-border text-right">年度</th>
						<td width="10%" class="right-border bottom-border">
							<select	id="ND" class="span12 year" name="ND" fieldname="ND" defaultMemo="全部" operation="=" kind="dic" src="T#GC_JH_SJ:distinct ND:ND as nnd:SFYX='1' order by nnd asc "></select>
						</td>
						<th width="7%" class="right-border bottom-border text-right">项目名称</th>
						<td width="35%" class="right-border bottom-border">
							<input
								class="span12" type="text" placeholder="" name="QXMMC"
								fieldname="tcjh.XMMC" operation="like" id="XMMC"
								autocomplete="off" tablePrefix="t">
						</td>
						<th width="7%" class="right-border bottom-border text-right">项目管理公司</th>
						<td width="10%" class="right-border bottom-border">
							<select class="span12 3characters" id="XMGLGS" src="T#VIEW_YW_XMGLGS:ROW_ID:BMJC" kind="dic" placeholder="" defaultMemo="全部" name="XMGLGS"  operation="=" logic="and"></select>
						</td>
						<td width="22%" class="text-left bottom-border text-right">
							<button id="example_query" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
							<button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
						</td>
					</tr>
					<tr>						
						<th class="right-border bottom-border text-right">开始时间</th>
						<td class="right-border bottom-border">
							<input class="span12 date" id="kssj" type="date" fieldtype="date" name="QKSSJ" fieldname="KSSJ" fieldformat="yyyyMMdd" operation=">="/>
						</td>
						<th class="right-border bottom-border text-right">结束时间</th>
						<td colspan="4" class="right-border bottom-border">
							<input class="span12 date" id="jssj" type="date" fieldtype="date" name="QJSSJ" fieldname="JSSJ" fieldformat="yyyyMMdd" operation="<="/>
						</td>
						<!-- <td width="7%"></td>
						<td width="10%"></td> -->
 						<!-- <td  width="49" colspan="3">
						</td> -->
         			</tr>
      			</table>
      		</form>
			<div style="height:5px;"> </div>
			<div class="overFlowX"> 
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" pageNum="5" type="single" printFileName="工程周报汇总">
	                <thead>
						<tr>
							<th  name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
							<th fieldname="XMBH" rowspan="2" colindex=2 tdalign="center" noprint="true" CustomFunction="zblb"></th>
							<th fieldname="XMBH" rowspan="2" colindex=3 hasLink="true" linkFunction="rowView" rowMerge="true">&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" rowspan="2" colindex=4 maxlength="15" rowMerge="true">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDBH" rowspan="2" colindex=5 CustomFunction="pdbdbh">&nbsp;标段编号&nbsp;</th>
							<th fieldname="BDMC" rowspan="2" colindex=6 maxlength="15" CustomFunction="pdbd">&nbsp;标段名称&nbsp;</th>
							<th fieldname="XMBDDZ" rowspan="2" colindex=7 maxlength="15" >&nbsp;项目地址&nbsp;</th>
							<th fieldname="SGDW" rowspan="2" colindex=8 maxlength="15">&nbsp;施工单位&nbsp;</th>
							<th fieldname="JLDW" rowspan="2" colindex=9 maxlength="15">&nbsp;监理单位&nbsp;</th>
							<th fieldname="YZDB" rowspan="2" colindex=10 maxlength="15">&nbsp;业主代表&nbsp;</th>
							<th fieldname="JSRW" rowspan="2" colindex=11 maxlength="15">&nbsp;建设目标&nbsp;</th>
							<th fieldname="XCZP" rowspan="2" colindex=12 tdalign="center" noprint="true" CustomFunction="viewimg">&nbsp;现场照片&nbsp;</th>
							<th fieldname="QTWJ" rowspan="2" colindex=13 tdalign="center" noprint="true">&nbsp;其他文件&nbsp;</th>							
 							<th fieldname="KSSJ" rowspan="2" colindex=14 tdalign="center">&nbsp;开始时间&nbsp;</th>
							<th fieldname="JSSJ" rowspan="2" colindex=15 tdalign="center">&nbsp;结束时间&nbsp;</th>
 							<th colspan="4">工程形象进度</th>
							<th fieldname="XZJH" maxlength="15" rowspan="2" colindex=20>&nbsp;下周计划&nbsp;</th>
							<th colspan="3">排迁形象进度</th>
							<th colspan="3">拆迁形象进度</th>
							<th colspan="3">周计量（万元）</th>
							<th colspan="5">存在问题及建议完成期限</th>
							<th fieldname="NOTE" rowspan="2" colindex=34 maxlength="15">&nbsp;备注&nbsp;</th>
						</tr>
						<tr>
							<th fieldname="BZJH" maxlength="15" colindex=16>&nbsp;本周计划&nbsp;</th>
							<th fieldname="BZWC" maxlength="15" colindex=17>&nbsp;本周完成&nbsp;</th>
							<th fieldname="BNWC" maxlength="15" colindex=18>&nbsp;本年完成&nbsp;</th>
							<th fieldname="LJWC" maxlength="15" colindex=19>&nbsp;累计完成&nbsp;</th>
							
							<th fieldname="GXMC" colindex=21>&nbsp;管线名称&nbsp;</th>
							<th fieldname="PQWCSJ" colindex=22>&nbsp;完成时限&nbsp;</th>
							<th fieldname="PABZJZ" colindex=23 maxlength="15" >&nbsp;本周进展&nbsp;</th>
							
							<th fieldname="CQWMC" colindex=24 maxlength="15" >&nbsp;拆迁物名称&nbsp;</th>
							<th fieldname="CQWCSJ" colindex=25>&nbsp;完成时限&nbsp;</th>
							<th fieldname="CQBZJZ" colindex=26 maxlength="15" >&nbsp;本周进展&nbsp;</th>
							
							<th fieldname="ZJLBZ" colindex=27 tdalign="right">&nbsp;本周完成&nbsp;</th>
							<th fieldname="ZJLND" colindex=28 tdalign="right">&nbsp;本年完成&nbsp;</th>
							<th fieldname="ZJLLJWC" colindex=29 tdalign="right">&nbsp;累计完成（含总工程量）&nbsp;</th>
							
							<th fieldname="QQWT" colindex=30 maxlength="15" >&nbsp;前期问题&nbsp;</th>
							<th fieldname="HTZJWT" colindex=31 maxlength="15" >&nbsp;合同、造价问题&nbsp;</th>
							<th fieldname="SJWT" colindex=32 maxlength="15" >&nbsp;设计问题&nbsp;</th>
							<th fieldname="ZCWT" colindex=33 maxlength="15" >&nbsp;征拆问题&nbsp;</th>
							<th fieldname="PQWT" colindex=34 maxlength="15" >&nbsp;排迁问题&nbsp;</th>
						</tr>
	                </thead>
	                <tbody></tbody>
	           </table>
	        </div>     
		</div>
	</div>		
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>  
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="asc" id="px" fieldname="xmbh,xmbs,pxh "/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<input type="hidden" id="queryResult"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
	</FORM>
</div>
</body>
</html>