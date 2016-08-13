<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<app:dialogs></app:dialogs>
<title>质量安全整改信息</title>
<%
	Date d=new Date();//获取时间
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//转换格式
	String today=sdf.format(d);
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
%>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script type="text/javascript" charset="utf-8">
 
var controllername_zg= "${pageContext.request.contextPath }/zlaq/zgxxController.do";
var controllername_jc= "${pageContext.request.contextPath }/zlaq/jcxxController.do";
var json,zgbid,zt,id;


//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
}


//初始化查询
$(document).ready(function(){
	setPageHeight();
	query_zd();
	$("#XMGLGS").val('<%=deptId%>');
	var json_fz='{"DQRQ":\"'+'<%=today%>'+'\"}';		
	var obj_fz=convertJson.string2json1(json_fz);
	$("#demoForm").setFormValues(obj_fz);
	setDefaultNd("ND");
	//setDefaultOption($("#ND"),new Date().getFullYear());
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername_jc+"?query_jc&flag=0",data,DT1,null,true);
 });


function query_zd()
{
	document.getElementById('ZT').options[1].remove();
	document.getElementById('ZT').options[1].remove();
	document.getElementById('ZT').options[1].remove();
	document.getElementById('ZT').options[1].innerHTML='未回复';//='待整改';//未回复
	document.getElementById('ZT').options[2].innerHTML='已回复';//未回复
	document.getElementById('ZT').options[3].innerHTML='已复查';//已回复
}

//年份查询
function generateNd(ndObj){
	ndObj.attr("src","T#GC_ZLAQ_JCB:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(new Date().getFullYear());
}

//查询
$(function() {
	var btn=$("#example_query");
	btn.click(function() {
		id=null;
		//生成json串
		var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername_jc+"?query_jc&flag=0&zt="+$("#ZT").val(),data,DT1,null,false);
	});
	//清空查询表单
    var btn_clearQuery=$("#query_clear");
    btn_clearQuery.click(function() {
        $("#queryForm").clearFormResult();
        $("#ZT").val('');
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
	showAutoComplete("XMMC",controllername_zg+"?xmmcAutoQuery","getXmmcQueryCondition"); 
});


//生成项目名称查询条件
function getXmmcQueryCondition(){
	var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":"pxh"}]}';
	var jsonData = eval('(' + initData + ')'); 
	jsonData.querycondition.conditions.push({"value": $("#XMGLGS").val(),"fieldname":"jcb.XMGLGS","operation":"=","logic":"and"});
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
	//检查类型
	if("" != $("#JCLX").val()){
		var defineCondition = {"value": $("#JCLX").val(),"fieldname":"JCLX","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}	
	//检查规模
	if("" != $("#JCGM").val()){
		var defineCondition = {"value": $("#JCGM").val(),"fieldname":"JCGM","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	//项目状态
	if("" != $("#ZT").val()){
		var defineCondition = {"value": $("#ZT").val(),"fieldname":"ZT","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	return JSON.stringify(jsonData);
}


//点击获取行对象
function tr_click(obj,tabListid){
	zgbid=$(obj).attr("GC_ZLAQ_ZGB_ID");
	id=$(obj).attr("GC_ZLAQ_JCB_ID");
	zt=$(obj).attr("ZT");
	json=$("#DT1").getSelectedRow();
	json=JSON.stringify(obj);
	//alert(json)
	json=encodeURI(json); 
}

//弹出回复窗口
function OpenMiddleWindow_hf(){
 	if(id==null||id=='undefined')
	{
		requireSelectedOneRow();
	}
	else
	{
		if(zt<4)
		{
			$("#resultXML").val($("#DT1").getSelectedRow());
	 	 	$(window).manhuaDialog({"title":"质量安全>整改回复","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmglgs/zlaqjc/zghf.jsp?zgbid="+zgbid+"&jcbid="+id+"&zt="+zt,"modal":"1"}); 
		}
		else
		{
			xInfoMsg('该项目已经回复,不能再次回复操作');
		}	
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
	zgbid=$(tempJson).attr("GC_ZLAQ_ZGB_ID");
	zt=$(tempJson).attr("ZT");
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
	zgbid=$(tempJson).attr("GC_ZLAQ_ZGB_ID");
	zt=$(tempJson).attr("ZT");
	json=encodeURI(json); 	
	xSuccessMsg("操作成功")
}


//详细信息
function rowView(index){
var obj=$("#DT1").getSelectedRowJsonByIndex(index);//获取行对象
	var xdkid=convertJson.string2json1(obj).XDKID;//取行对象<项目编号>
	$(window).manhuaDialog(xmscUrl(xdkid));//调用公共方法,根据项目编号查询
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
function doHf(obj){
	var zt = obj.ZT;
	switch(zt)
	{
		case '2':
			if('<%=today%>'>obj.XGRQ)
			{
				zt = '<span class="label label-important-orange" title=发送日期："'+obj.TZRQ+'"&nbsp;&nbsp;当前状态：未回复>限改超期'+DateDiff($("#DQRQ").val(),obj.XGRQ)+'天</span>';
			}
			else
			{
				zt = '<span class="label background-color: gray;" title=发送日期："'+obj.TZRQ+'"&nbsp;&nbsp;当前状态：未回复>限改剩余'+DateDiff(obj.XGRQ,$("#DQRQ").val())+'天</span>';
			}	
		break;
		case '3':
			if('<%=today%>'>obj.XGRQ)
			{
				zt = '<span class="label label-important-orange" title=发送日期："'+obj.TZRQ+'"&nbsp;&nbsp;当前状态：未回复>限改超期'+DateDiff($("#DQRQ").val(),obj.XGRQ)+'天</span>';
			}
			else
			{
				zt = '<span class="label background-color: gray;" title=发送日期："'+obj.TZRQ+'"&nbsp;&nbsp;当前状态：未回复>限改剩余'+DateDiff(obj.XGRQ,$("#DQRQ").val())+'天</span>';
			}	
		break;		
		case '4':
				zt = '<span class="label background-color: gray;" title=回复日期："'+obj.HFRQ+'">回复已提交</span>';	
		break;
		case '5':
			zt = '<span class="label background-color: gray;" title=回复日期："'+obj.HFRQ+'">回复已提交</span>';	
		break;
		
	}
	return zt;	
}


function doFc(obj){
	var zt = obj.ZT;
	if(zt<5)
	{
		zt='<i title=尚未复查>—</i>';
	}	
	else
	{
		if(obj.FCJL==1)
		{
			zt = '<span class="label background-color: gray;" title=复查日期："'+obj.FCRQ+'">复查已通过</span>';
		}	
		else
		{
			zt = '<span class="label label-important" title=复查日期："'+obj.FCRQ+'">复查未通过</label>';
		}	
	}
	return zt;	
}


//操作图标
function cztb(obj)
{
	var id,zt,json;
	id=obj.GC_ZLAQ_JCB_ID;
	zt=obj.ZT;
	json=JSON.stringify(obj);
	json=encodeURI(json);  
	$("#resultXML_form").append('<input class=\"span12\" type=\"hidden\" name=\"resultXML\"  form =\"resultXML_form" value=\"'+json+'\" id=\"'+id+'\"/>');
	return '<a href="javascript:void(0);"><i title="综合信息" class="icon-file showXmxxkInfo" onclick="xxk(\''+id+'\',\''+zt+'\',\''+json+'\')"></i></a>';
}


//信息卡
function xxk(id,zt,json)
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
				质量安全整改信息
				<span class="pull-right">
					<app:oPerm url="jsp/business/xmglgs/zlaqjc/jlaqxx.jsp">
						<button class="btn" onclick="OpenMiddleWindow_hf()">回复管理</button>			
					</app:oPerm>
					<app:oPerm url="jsp/business/zlaq/xxtx_glgs.jsp">
						<button class="btn" onclick="sendMsg('项目管理公司')">发送提醒</button>
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
							<INPUT type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<="/>
						<!-- 	<input type="hidden" id="XMGLGS" fieldname="XMGLGS" name="XMGLGS" keep="true" operation="="/> -->
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>
						<th width="5%" class="right-border bottom-border">年度</th>
						<td width="7%" class="right-border bottom-border">
							<select	id="ND" class="span12 year" name="ND" fieldname="ND" defaultMemo="全部" operation="=" kind="dic" src="T#GC_ZLAQ_JCB:distinct ND:ND as nnd:SFYX='1' order by nnd asc"></select>
						</td>
						<th width="5%" class="right-border bottom-border">项目名称</th>
						<td width="18%" class="right-border bottom-border">
							<input id="XMMC" class="span12" type="text" placeholder="" name="XMMC" check-type="maxlength" maxlength="500" fieldname="XMMC" operation="like" logic="and" autocomplete="off"/>
						</td>				
						<th width="5%" class="right-border bottom-border">检查类型</th>
						<td width="9%" class="right-border bottom-border">
							<select id="JCLX"	kind="dic" src="JCLX" class="span12 4characters" name="JCLX" fieldname="JCLX" operation="=" defaultMemo="全部"></select>
						</td>
						<th width="5%" class="right-border bottom-border">检查方式</th>
						<td width="9%" class="right-border bottom-border">
							<select id="JCGM" kind="dic" src="JCGM" class="span12 4characters" name="JCGM" fieldname="JCGM" operation="=" defaultMemo="全部"></select>
						</td>
						<th width="5%" class="right-border bottom-border">当前状态</th>
						<td width="8%" class="right-border bottom-border">
							<select id="ZT" kind="dic" src="ZGZT" class="span12 3characters" name="ZT" defaultMemo="全部"></select>
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
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" pageNum="10" printFileName="质量安全整改信息">
	                <thead>
	                    <tr>
	                     	<th id="_XH" name="XH" tdalign="center">&nbsp;#&nbsp;</th>	
							<th fieldname="GC_ZLAQ_JCB_ID" tdalign="center" noprint="true" CustomFunction="cztb"></th>
							<th fieldname="ZT" tdalign="center" CustomFunction="doHf" noprint="true">&nbsp;回复状态&nbsp;</th>
							<th fieldname="ZT" tdalign="center" CustomFunction="doFc" noprint="true">&nbsp;复查状态&nbsp;</th>
							<th fieldname="XMBH" colindex=2 hasLink="true" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>								
							<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDBH" maxlength="15" CustomFunction="pdbdbh">&nbsp;标段编号&nbsp;</th>
							<th fieldname="BDMC" maxlength="15" CustomFunction="pdbd">&nbsp;标段名称&nbsp;</th>
							<th fieldname="XGRQ" tdalign="center">&nbsp;限改日期&nbsp;</th>
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
		<input type="hidden" name="txtFilter" order="desc"  fieldname="LRSJ"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
		<input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
	<form method="post" id="demoForm">
		<input type="hidden" id="DQRQ" name="DQRQ" fieldname="DQRQ"/>
	</form>	
</div>
<FORM id="resultXML_form"></FORM>
</body>
</html>