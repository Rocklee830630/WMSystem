<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<app:base/>
<app:dialogs></app:dialogs>
<title>质量安全检查信息</title>
<%
	Date d=new Date();//获取时间
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//转换格式
	String today=sdf.format(d);
%>
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
	query();
    
    
    //监听当前状态
   $("#ZT").change(function() {
	   //alert($("#ZT").val())
    if($("#ZT").val()=='2')
   	{
   		$("#ZT").attr("operation",">=");
   	}
    else
    {
    	$("#ZT").attr("operation","=");
    }	
   }); 
});


//查询
function query()
{
	setPageHeight();
	setDefaultNd("ND");
	//setDefaultOption($("#ND"),new Date().getFullYear());
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?query_jc&flag=1",data,DT1,"query_zd",true);
	json=null;
	id=null;
}


//查询回调函数
function query_zd()
{
	document.getElementById('ZT').options[1].innerHTML='无需整改';
	document.getElementById('ZT').options[2].innerHTML='未发送';
	document.getElementById('ZT').options[3].innerHTML='已发送';
	document.getElementById('ZT').options[4].innerHTML='待回复';
	document.getElementById('ZT').options[5].innerHTML='待复查';
	document.getElementById('ZT').options[6].innerHTML='已复查';
}

//查询
$(function() {
	var btn=$("#example_query");
	btn.click(function() {
		//生成json串
		json=null;
		id=null;
		zt=null;
		var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query_jc",data,DT1,null,false);
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
	//showAutoComplete("XMMC",controllername+"?xmmcAutoQuery","getXmmcQueryCondition"); 
	var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
	showAutoComplete("XMMC",autocompleteXmmcController+"?xmmcAutoQuery","getXmmcQueryCondition");
});


//生成项目名称查询条件
function getXmmcQueryCondition(){
	var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":"pxh"}]}';
	var jsonData = eval('(' + initData + ')'); 
	//项目名称
	if("" != $("#XMMC").val()){
		var defineCondition = {"value": "%"+$("#XMMC").val()+"%","fieldname":"sj.XMMC","operation":"like","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	
  //年度
	if("" != $("#ND").val()){
		var defineCondition = {"value": $("#ND").val(),"fieldname":"sj.ND","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	//项目类型
	if("" != $("#BDMC").val()){
		var defineCondition = {"value": $("#BDMC").val(),"fieldname":"sj.BDMC","operation":"like","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}	
	//项目管理公司
	if("" != $("#XMGLGS").val()){
		var defineCondition = {"value": $("#XMGLGS").val(),"fieldname":"sj.XMGLGS","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	//项目状态
	if("" != $("#ZT").val()){
		if($("#ZT").val()=='2')
		{
			var defineCondition = {"value": $("#ZT").val(),"fieldname":"sj.ZT","operation":">=","logic":"and"};
		}
		else
		{
			var defineCondition = {"value": $("#ZT").val(),"fieldname":"sj.ZT","operation":"=","logic":"and"};
		}	
		jsonData.querycondition.conditions.push(defineCondition);
	} 
	return JSON.stringify(jsonData);
}


//点击获取行对象
function tr_click(obj,tabListid){
	id=$(obj).attr("GC_ZLAQ_JCB_ID");
	zt=$(obj).attr("ZT");
	zgbid=$(obj).attr("GC_ZLAQ_ZGB_ID");
	json=JSON.stringify(obj);
	json=encodeURI(json);
}
 

//弹出检查窗口
function OpenMiddleWindow_jcadd(){
	$(window).manhuaDialog({"title":"质量安全>检查","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zlaq/jcwh.jsp","modal":"1"});
}


//弹出整改窗口
function OpenMiddleWindow_zg(){
	if(json!=null&&id!=null)
	{
		if(parseInt(zt)<2)
		{
			$("#resultXML").val($("#DT1").getSelectedRow());
			$(window).manhuaDialog({"title":"质量安全>整改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zlaq/zgtz.jsp?zgbid="+zgbid+"&jcbid="+id+"&zt="+zt+"&flag=1","modal":"1"});		
		}
		else
		{
			switch(zt)
			{
				case '2':
					xInfoMsg('该项目已经发送,不能进行整改操作');
					break;
				case '3':
					xInfoMsg('该项目正在回复中,不能进行整改操作');
					break;
				case '4':
					xInfoMsg('该项目已回复,不能进行整改操作');
					break;
				case '5':
					xInfoMsg('该项目已复查,不能进行整改操作');
					break;
			}
		}	
	}
	else
	{
		requireSelectedOneRow();
	}	
}


//弹出复查窗口
function OpenMiddleWindow_fc(){
	if(json!=null&&id!=null)
	{
		if(parseInt(zt)==4||parseInt(zt)==5)
		{
			$("#resultXML").val($("#DT1").getSelectedRow());
			$(window).manhuaDialog({"title":"质量安全>复查","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zlaq/zgfc.jsp?zgbid="+zgbid+"&jcbid="+id+"&zt="+zt,"modal":"1"});
		}
		else
		{
			switch(zt)
			{
				case '0':
					xInfoMsg('该项目不需整改,不用进行复查');
					break;
				case '1':
					xInfoMsg('该项目尚未发送,不能进行复查操作');
					break;
				case '2':
					xInfoMsg('该项目尚未回复,不能进行复查操作');
					break;
				case '3':
					xInfoMsg('该项目正在回复中,不能进行复查操作');
					break;
			}
		}
	}	
	else
	{
		requireSelectedOneRow();
	}	
}

//弹出综合窗口
function OpenMiddleWindow_zhgl(){
	if(json!=null&&id!=null)
	{
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"质量安全>检查信息维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zlaq/zhgl.jsp?id="+id+"&zt="+zt,"modal":"1"});
	}	
	else
	{
		requireSelectedOneRow();
	}	
}


//插入时列表行更新
function add(data)
{
	var subresultmsgobj1=defaultJson.dealResultJson(data);
	$("#DT1").insertResult(JSON.stringify(subresultmsgobj1),DT1,1);
	$("#DT1").cancleSelected();
	$("#DT1").setSelect(0);
	var index1 =$("#DT1").getSelectedRowIndex();
	json=$("#DT1").getSelectedRowJsonByIndex(index1);
	var tempJson=convertJson.string2json1(json);
	id=$(tempJson).attr("GC_ZLAQ_JCB_ID");
	zt=$(tempJson).attr("ZT");
	zgbid=$(tempJson).attr("GC_ZLAQ_ZGB_ID");
	json=encodeURI(json); 
}


//修改时列表行更新
function update(data)
{
	 var index= $("#DT1").getSelectedRowIndex();
	 var subresultmsgobj1=defaultJson.dealResultJson(data);
	 $("#DT1").updateResult(JSON.stringify(subresultmsgobj1),DT1,index);
	 $("#DT1").cancleSelected();
	 $("#DT1").setSelect(index);
	 json=$("#DT1").getSelectedRowJsonByIndex(index);
	 var tempJson=convertJson.string2json1(json);
	 id=$(tempJson).attr("GC_ZLAQ_JCB_ID");
	 zt=$(tempJson).attr("ZT");
	 $("#"+id).val(encodeURI(JSON.stringify(subresultmsgobj1)))
	 json=encodeURI(json);  
	 xSuccessMsg("操作成功");
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
function doZt(obj){
	var zt = obj.ZT;
	switch(zt)
	{
		case '0':
			zt = '<i title=无需整改>—</i>';	
			break;
		case '1':
			zt = '<span class="label background-color: gray;" title=未发送整改通知>通知未发送</span>';	
			break;
		case '2':
			if('<%=today%>'>obj.XGRQ)
			{
				zt = '<span class="label label-important-orange" title=发送日期："'+obj.TZRQ+'"&nbsp;&nbsp;当前状态：已发送>限改超期'+DateDiff('<%=today%>',obj.XGRQ)+'天</span>';	
			}
			else
			{
				zt = '<span class="label background-color: gray;" title=发送日期："'+obj.TZRQ+'"&nbsp;&nbsp;当前状态：已发送>限改剩余'+DateDiff(obj.XGRQ,'<%=today%>')+'天</span>';
	
			}
			break;
		case '3':
			if('<%=today%>'>obj.XGRQ)
			{
				zt = '<span class="label label-important-orange" title=发送日期："'+obj.TZRQ+'"&nbsp;&nbsp;当前状态：回复中>限改超期'+DateDiff('<%=today%>',obj.XGRQ)+'天</span>';	
			}
			else
			{
				zt = '<span class="label background-color: gray;" title=发送日期："'+obj.TZRQ+'"&nbsp;&nbsp;当前状态：回复中>限改剩余'+DateDiff(obj.XGRQ,'<%=today%>')+'天</span>';
			}
			break;
		case '4':
			zt = '<span class="label background-color: gray;" title=回复日期："'+obj.HFRQ+'">回复待复查</span>';				
			break;
		case '5':
			if(obj.FCJL==1)
			{
				zt = '<span class="label background-color: gray;" title=复查日期："'+obj.FCRQ+'">复查已通过</span>';
			}	
			else
			{
				zt = '<span class="label label-important" title=复查日期："'+obj.FCRQ+'">复查未通过</span>';
			}	
			break;
	}
	return zt;
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


//操作图标
function cztb(obj)
{
	var id,zt,json;
	id=obj.GC_ZLAQ_JCB_ID;
	zt=obj.ZT;
	json=JSON.stringify(obj);
	//json=encodeURI(json); 
	$("#resultXML").val(json);
	$("#resultXML_form").append('<input class=\"span12\" type=\"hidden\" name=\"resultXML\"  form =\"resultXML_form" value=\"'+json+'\" id=\"'+id+'\"/>');
	return '<a href="javascript:void(0);"><i title="综合信息" class="icon-file showXmxxkInfo" onclick="xxk(\''+id+'\',\''+zt+'\')"></i></a>';
}


//信息卡
function xxk(id,zt)
{
	$(window).manhuaDialog({"title":"质量安全>综合信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zlaq/zhxx.jsp?jcbid="+id+"&zt="+zt+"&flag=1","modal":"1"});
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
		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
				<h4 class="title">
					质量安全检查信息
					<span class="pull-right">
						<app:oPerm url="jsp/business/zlaq/jcwh.jsp">
							<button class="btn" onclick="OpenMiddleWindow_jcadd()">新增检查</button>
						</app:oPerm>
						<app:oPerm url="jsp/business/zlaq/zgtz.jsp">
							<button class="btn" onclick="OpenMiddleWindow_zg()">新增整改</button>
						</app:oPerm>
						<app:oPerm url="jsp/business/zlaq/zgfc.jsp">
							<button class="btn" onclick="OpenMiddleWindow_fc()">新增复查</button>
						</app:oPerm>
							<!-- <button class="btn" onclick="OpenMiddleWindow_zhgl()">综合管理</button> -->
					
						<app:oPerm url="jsp/business/zlaq/jcwh_update.jsp">
							<button class="btn" onclick="OpenMiddleWindow_zhgl()">检查信息维护</button>
						</app:oPerm>
						<app:oPerm url="jsp/business/zlaq/xxtx_zlaq.jsp">
							<button class="btn" onclick="sendMsg('质量安全部')">发送提醒</button>
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
							<td width="13%" class="right-border bottom-border">
								<input class="span12" id="XMMC" type="text" placeholder="" name="XMMC" check-type="maxlength" maxlength="500" fieldname="XMMC" operation="like" logic="and" autocomplete="off" tablePrefix="sj"/>
							</td>
							<th width="5%" class="right-border bottom-border">标段名称</th>
							<td width="13%" class="right-border bottom-border">
								<input class="span12" id="BDMC" type="text" placeholder="" name="BDMC" check-type="maxlength" maxlength="100" fieldname="BDMC" operation="like" logic="and"/>
							</td>
							<th width="5%" class="right-border bottom-border">项目管理公司</th>
							<td width="10%" class="right-border bottom-border">
								<select class="span12 3characters" id="XMGLGS" kind="dic" src="T#VIEW_YW_XMGLGS:ROW_ID:BMJC"  placeholder="" defaultMemo="全部" name="XMGLGS"  fieldname="XMGLGS" operation="=" logic="and"></select>
							</td>
							<th width="5%" class="right-border bottom-border">当前状态</th>
							<td width="9%" class="right-border bottom-border">
								<select id="ZT" kind="dic" src="ZGZT" class="span12 4characters" name="ZT" fieldname="ZT" operation="=" defaultMemo="全部"></select>
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
		            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" printFileName="质量安全检查信息">
		                <thead>
		                    <tr>
		                     	<th id="_XH" name="XH" tdalign="center">&nbsp;#&nbsp;</th>
		                     	<th fieldname="GC_ZLAQ_JCB_ID" tdalign="center" noprint="true" CustomFunction="cztb"></th>
								<th fieldname="ISCZWT" tdalign="center" CustomFunction="doZg" noprint="true">&nbsp;需要整改&nbsp;</th>
								<th fieldname="ZT" tdalign="center" CustomFunction="doZt" noprint="true">&nbsp;当前状态&nbsp;</th>
								<th fieldname="XMBH" hasLink="true" tdalign="center" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>								
								<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
								<th fieldname="BDBH" maxlength="15" CustomFunction="pdbdbh">&nbsp;标段编号&nbsp;</th>
								<th fieldname="BDMC" maxlength="15" CustomFunction="pdbd">&nbsp;标段名称&nbsp;</th>
								<th fieldname="XMGLGS" tdalign="center">&nbsp;项管公司&nbsp;</th>
								<th fieldname="JCRQ">&nbsp;检查日期&nbsp;</th>
								<th fieldname="JCLX" tdalign="center">&nbsp;检查类型&nbsp;</th>
								<th fieldname="JCGM" tdalign="center">&nbsp;检查方式&nbsp;</th>
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
			<input type="hidden" name="txtFilter" order="desc" fieldname="JCRQ"/>
			<input type="hidden" name="resultXML" id="resultXML"/>
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData"/>
			<input type="hidden" name="queryResult" id="queryResult"/>
		</FORM>
		<form method="post" id="demoForm">
			<input type="hidden" id="GC_ZLAQ_JCB_ID" name="GC_ZLAQ_JCB_ID" fieldname="GC_ZLAQ_JCB_ID"/>
			<input type="hidden" id="GC_ZLAQ_ZGB_ID" name="GC_ZLAQ_ZGB_ID" fieldname="GC_ZLAQ_ZGB_ID"/>
			<input type="hidden" id="ZT" name="ZT" fieldname="ZT"/>
		</form>	
	</div>
	<FORM id="resultXML_form"></FORM>
</body>
</html>