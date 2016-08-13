<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
	Date d=new Date();//获取时间
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//转换格式
	String today=sdf.format(d);
%>
<app:base/>
<app:dialogs></app:dialogs>
<title>质量安全检查</title>

<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script type="text/javascript" charset="utf-8">
 
var controllername= "${pageContext.request.contextPath }/zlaq/jcxxController.do";
var json,id,zt,zgbid;


//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
}

//初始化查询
$(document).ready(function(){
	
	setPageHeight();
	setDefaultNd("ND");
	//setDefaultOption($("#ND"),new Date().getFullYear());
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?query_jc&flag=1",data,DT1,null,true);
 });


//查询
$(function() {
	var btn=$("#example_query");
	btn.click(function() {
		//生成json串
		json=null;
		var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query_jc&flag=1",data,DT1,null,false);
	});
	//清空查询表单
    var btn_clearQuery=$("#query_clear");
    btn_clearQuery.click(function() {
        $("#queryForm").clearFormResult();
        setDefaultNd("ND");
        //setDefaultOption($("#ND"),new Date().getFullYear());
        //其他处理放在下面
    });

    
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		  if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
		      printTabList("DT1");
		  }
	});

	
	//自动完成项目名称模糊查询
	showAutoComplete("XMMC",controllername+"?xmmcAutoQuery","getXmmcQueryCondition"); 
});


//生成项目名称查询条件
function getXmmcQueryCondition(){
	var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":"pxh"}]}';
	var jsonData = eval('(' + initData + ')'); 
	//项目名称
	if("" != $("#XMMC").val()){
		var defineCondition = {"value": "%"+$("#XMMC").val()+"%","fieldname":"XMMC","operation":"like","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
  //年度
	if("" != $("#ND").val()){
		var defineCondition = {"value": $("#ND").val(),"fieldname":"ND","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	//项目类型
	if("" != $("#BDMC").val()){
		var defineCondition = {"value": $("#BDMC").val(),"fieldname":"BDMC","operation":"like","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}	
	//项目类型
	if("" != $("#XMGLGS").val()){
		var defineCondition = {"value": $("#XMGLGS").val(),"fieldname":"XMGLGS","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
 	return JSON.stringify(jsonData);
}


//点击获取行对象
function tr_click(obj,tabListid){
	id=$(obj).attr("GC_ZLAQ_JCB_ID");
	zgbid=$(obj).attr("GC_ZLAQ_ZGB_ID");
	zt=obj.ZT;
	json=$("#DT1").getSelectedRow();
	json=JSON.stringify(obj);
	//alert(json)
	json=encodeURI(json); 
}


//弹出综合窗口
function OpenMiddleWindow_zhgl(){
	if(json!=null&&id!=null)
	{
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"质量安全>综合信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zlaq/zhxx.jsp?jcbid="+id+"&zt="+zt+"&flag=1&flag_ty=1","modal":"1"});
	}	
	else
	{
		requireSelectedOneRow();
	}	
}



//详细信息
function rowView(index){
    var obj=$("#DT1").getSelectedRowJsonByIndex(index);//获取行对象
	var xdkid=convertJson.string2json1(obj).XDKID;//取行对象<项目编号>
	$(window).manhuaDialog(xmscUrl(xdkid));//调用公共方法,根据项目编号查询
}


//清空表单，取消选择行
function canclerow()
{
	$("#DT1").cancleSelected();
	id=null;
}


//状态————通知
function doTz(obj){
	var zt = obj.ZT;
	switch(zt)
	{
		case '0':
			zt = '<i title=无需整改>—</i>';	
			break;
		case '1':
			zt = '<i class="icon-gray" title=未发送整改通知></i>';	
			break;
		default:
				zt = '<i class="icon-green" title=发送日期："'+obj.TZRQ+'"></i>';
			break;
	}
	return zt;
}


//状态————回复
function doHf(obj){
	var zt = obj.ZT;
	if(obj.ISCZWT==0)
	{
		zt = '<i title=无需回复>—</i>';
	}
	else
	{
		switch(zt)
		{				
			case '1':
				zt = '<i class="icon-gray" title=未回复></i>';		
				break;
			case '2':
				if('<%=today%>'>obj.XGRQ)
				{
					zt = '<i class="icon-yellow" title=发送日期："'+obj.TZRQ+'"&nbsp;超期："'+DateDiff('<%=today%>',obj.XGRQ)+'"天></i>';	
				}
				else
				{
					zt = '<i class="icon-gray" title=发送日期："'+obj.TZRQ+'"&nbsp;剩余："'+DateDiff(obj.XGRQ,'<%=today%>')+'"天></i>';
				}
				break;
			case '3':
				if('<%=today%>'>obj.XGRQ)
				{
					zt = '<i class="icon-yellow" title=发送日期："'+obj.TZRQ+'"&nbsp;超期："'+DateDiff('<%=today%>',obj.XGRQ)+'"天></i>';	
				}
				else
				{
					zt = '<i class="icon-gray" title=发送日期："'+obj.TZRQ+'"&nbsp;剩余："'+DateDiff(obj.XGRQ,'<%=today%>')+'"天></i>';
				}
				break;
			default:
				zt = '<i class="icon-green" title=回复日期："'+obj.TZRQ+'"></i>';
				break;
		}	
	}
	return zt;
}


//状态————复查
function doFc(obj){
	var zt = obj.ZT;
	if(obj.ISCZWT==0)
	{
		zt = '<i title=无需回复>—</i>';
	}
	else
	{
		switch(zt)
		{
			case '4':
				zt = '<i class="icon-gray" title=回复日期："'+obj.HFRQ+'"></i>';				
				break;
			case '5':
				if(obj.FCJL==1)
				{
					zt = '<i class="icon-green" title=复查日期："'+obj.FCRQ+'"></i>';
				}	
				else
				{
					zt = '<i class="icon-red" title=复查日期："'+obj.FCRQ+'",复查未通过></i>';
				}	
				break;
			default:
				zt = '<i class="icon-gray" title=未复查>';	
				break;
		}	
	}	
	return zt;
}


//两个日期的差值(d1 - d2).
function DateDiff(d1,d2)
{
	var day = 24 * 60 * 60 *1000;
	try{   
	     var dateArr = d1.split("-");
	     var checkDate = new Date();
	     checkDate.setFullYear(parseInt(dateArr[0]),(parseInt(dateArr[1])-1),parseInt(dateArr[2]));
	     var checkTime = checkDate.getTime();
	     var dateArr2 = d2.split("-");
	     var checkDate2 = new Date();
	     checkDate2.setFullYear(parseInt(dateArr2[0]),(parseInt(dateArr2[1])-1),parseInt(dateArr2[2]));
	     var checkTime2 = checkDate2.getTime();
	     var cha = (checkTime - checkTime2)/day; 
	     cha=Math.round(cha); 
	     return cha;
	}
	catch(e)
	{
   	return false;
	}
}	

//状态提示日期
function doZg(obj){
	var zg = obj.ISCZWT;
	if(zg==1)
	{
		return  '<i class="icon-ok" title=需要整改></i>';	
	}	
	else
	{
		return  '<i title=不需要整改>—</i>';	
	}	
}


//判断标段是否有值
function pdbd(obj){
	var bdid = obj.BDID;
	if(bdid=='undefinde'||bdid=='')
	{
		return '<div style="text-align:center">—</div>'
	}	
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
		</p>	
		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
				<h4 class="title">
					检查整改综合信息 
					<span class="pull-right">  
						<app:oPerm url="jsp/business/zlaq/jcxx_ty.jsp">
							<button class="btn" onclick="OpenMiddleWindow_zhgl()">综合信息</button>			
						</app:oPerm>
						<button class="btn" id="btnExpExcel">导出</button>
					</span>
				</h4>
				<form method="post" id="queryForm">
					<table class="B-table">
						<!--可以再此处加入hidden域作为过滤条件 -->
						<TR style="display: none;">
							<TD class="right-border bottom-border"></TD>
							<TD class="right-border bottom-border">
								<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
							</TD>
						</TR>
						<!--可以再此处加入hidden域作为过滤条件 -->
						<tr>
							<th width="5%" class="right-border bottom-border">年度</th>
							<td width="7%" class="right-border bottom-border">
								<select	id="ND" class="span12 year" name="ND" fieldname="ND" defaultMemo="全部" operation="=" kind="dic" src="T#GC_ZLAQ_JCB:distinct ND:ND as nnd:SFYX='1' order by nnd asc "></select>
							</td>
							<th width="5%" class="right-border bottom-border">项目名称</th>
							<td width="21%" class="right-border bottom-border">
								<input class="span12" id="XMMC" type="text" placeholder="" name="XMMC" check-type="maxlength" maxlength="500" fieldname="XMMC" operation="like" logic="and" autocomplete="off"/>
							</td>
							<th width="5%" class="right-border bottom-border">标段名称</th>
							<td width="21%" class="right-border bottom-border">
								<input class="span12" id="BDMC" type="text" placeholder="" name="BDMC" check-type="maxlength" maxlength="100" fieldname="BDMC" operation="like" logic="and"/>
							</td>
							<th width="5%" class="right-border bottom-border">项目管理公司</th>
							<td width="8%" class="right-border bottom-border">
								<select class="span12 3characters" id="XMGLGS" kind="dic" src="T#VIEW_YW_XMGLGS:ROW_ID:BMJC" placeholder="" defaultMemo="全部" name="XMGLGS" check-type="maxlength" maxlength="100" fieldname="XMGLGS" operation="=" logic="and"></select>
							</td>
				            <td class="text-left bottom-border text-right">
								<button	id="example_query" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
 	                        	<button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
				            </td>																				
						</tr>
					</table>
				</form>
				<div style="height:5px;"> </div>								
				<div class="overFlowX"> 
		            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" printFileName="检查整改综合信息">
		                <thead>
		                    <tr>
		                     	<th id="_XH" name="XH" tdalign="center">&nbsp;#&nbsp;</th>	
								<th fieldname="XMBH" colindex=2 hasLink="true" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>								
								<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
								<th fieldname="BDBH" maxlength="15" CustomFunction="pdbdbh">&nbsp;标段编号&nbsp;</th>
								<th fieldname="BDMC" maxlength="15" CustomFunction="pdbd">&nbsp;标段名称&nbsp;</th>
								<th fieldname="XMGLGS" tdalign="center">&nbsp;项管公司&nbsp;</th>
								<th fieldname="JCRQ" tdalign="center">&nbsp;检查日期&nbsp;</th>
								<th fieldname="ISCZWT" tdalign="center" CustomFunction="doZg" noprint="true">&nbsp;需要整改&nbsp;</th>
								<th fieldname="ZT" tdalign="center" CustomFunction="doTz" noprint="true">&nbsp;整改通知&nbsp;</th>
								<th fieldname="ZT" tdalign="center" CustomFunction="doHf" noprint="true">&nbsp;整改回复&nbsp;</th>
								<th fieldname="ZT" tdalign="center" CustomFunction="doFc" noprint="true">&nbsp;整改复查&nbsp;</th>
								<th fieldname="JCNR" maxlength="15">&nbsp;检查内容&nbsp;</th>
		                    </tr>
		                </thead> 
		              	<tbody></tbody>
		           </table>
		       </div>     
			</div>
		</div>
	</div>
	<div align="center">
		<FORM name="frmPost" method="post" style="display: none" target="_blank">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML"/> 
			<input type="hidden" name="txtXML"/>
			<input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ"/>
			<input type="hidden" name="resultXML" id="resultXML"/>
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData"/>
			<input type="hidden" name="queryResult" id="queryResult"/>
		</FORM>
	</div>
</body>
</html>