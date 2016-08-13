<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<app:dialogs></app:dialogs>
<title>交竣工管理</title>
<script type="text/javascript" charset="utf-8">
 
var controllername= "${pageContext.request.contextPath }/jjg/jjgwhController.do";
var oTable1,json,id;
var iswg;
var wg=false;
var weiwg=false;
var tbjjg=0;

//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
}

//初始化查询
$(document).ready(function(){
	setPageHeight();
	$("[name='JGYSRQ']").attr('checked','true'); 
	issfwg();
	//generateJhNdMc($("#ND"),null);
	initCommonQueyPage();
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?query_jjg&iswg="+iswg,data,DT1,"queryHuiDiao",true);
});


//查询
$(function() {
	var btn=$("#example_query");
	btn.click(function() {
		//生成json串
		issfwg();
		gettb();
		var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query_jjg&iswg="+iswg+"&tbjjg="+tbjjg,data,DT1,"queryHuiDiao",false);
	});
	//清空查询表单
    var btn_clearQuery=$("#query_clear");
    btn_clearQuery.click(function() {
        $("#queryForm").clearFormResult();
        document.getElementById('num').value='1000';
        initCommonQueyPage();
        //其他处理放在下面
    });
	//新增
	var addlbj = $("#jiaoggl");
	addlbj.click(function() 
 		{
			var index2= $("#DT1").getSelectedRowIndex();
			if(index2<0){
				requireSelectedOneRow();;
			}else{
				   $("#resultXML").val($("#DT1").getSelectedRow());
	 		       $(window).manhuaDialog({"title":"交竣工管理>交工管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jjg/jiaogwh.jsp","modal":"2"});
			}
		}); 
	//新增委托咨询公司
	var WTZXGS = $("#junggl");
	WTZXGS.click(function() 
 		{
			var index2= $("#DT1").getSelectedRowIndex();
			if(index2<0){
				requireSelectedOneRow();;
			}else{
				$("#resultXML").val($("#DT1").getSelectedRow());
	 		      $(window).manhuaDialog({"title":"交竣工管理>竣工管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jjg/jungwh.jsp","modal":"2"});
				}    
		});
	$("#do_excel").click(function(){
		if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
		    /*   printTabList("DT1"); */
		      printTabList("DT1","jjg.xls","XMBH,XMMC,XMDZ,XMGLGS,BDMC,WGRQ,JGYSRQ,JGYSSJ,SGDW,JLDW,JIAOGJSDW,JIAOGJSR,JUNGJSDW,JUNGJSR","3,0"); 

		  }
	});

});


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
function queryJGSJ(obj)
	{
	    if(obj.JGYSSJ!="")
	    	{
				if(obj.JGYSRQ == "")
				{
					return '—';
				}
	    	}
	}
function issfwg()
{
	$("input[name='JGYSRQ']").each(function(i)
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
function gettb()
{
	$("input[name='tbjjg']").each(function(i)
	{
		
       	if($(this).is(':checked')){
       		tbjjg=1;
	   	}
		else
		{
			tbjjg=0;
		}
    });

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
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="row-fluid">
				交竣工管理  
				<span class="pull-right">  
				<!-- 	<button class="btn" onclick="OpenMiddleWindow_wh()">维护</button>  -->
					<app:oPerm url="/jsp/business/jjg/jiaogwh.jsp">
					<button class="btn" id="jiaoggl"  type="button">交工管理</button>
					</app:oPerm>
					<app:oPerm url="/jsp/business/jjg/jungwh.jsp">
					<button class="btn" id="junggl" type="button" >竣工管理</button>
					</app:oPerm>
					<button class="btn" id="do_excel" type="button" >导出</button>
				</span>
			</h4>
			<form method="post" id="queryForm">
				<table class="B-table"  style="width: 100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>
					<!-- 	<th width="5%" class="right-border bottom-border">年度</th>
						<td width="10%" class="right-border bottom-border">
							<select	id="ND" class="span12" name="ND" fieldname="xdk.ND" defaultMemo="全部" operation="="></select>
						</td>
						<th width="5%" class="right-border bottom-border">项目名称</th>
						<td width="20%" class="right-border bottom-border">
							<input class="span12" type="text" placeholder="" name="XMMC" check-type="maxlength" maxlength="100" fieldname="xdk.XMMC" operation="like" logic="and"/>
						</td> -->
						 <!--公共的查询过滤条件 -->
				         <jsp:include page="/jsp/business/common/commonQuery.jsp" flush="true">
				         	<jsp:param name="prefix" value="sj"/> 
				         </jsp:include>
				         <td class="right-border bottom-border text-center">
				         	<label class="checkbox inline" style="font-weight: bold;"> 
				         		<input type="checkbox" id="jgysrq" name="JGYSRQ" class="text-right" value="shi" />完工
							</label>
				         	<label class="checkbox inline" style="font-weight: bold;"> 
				         		<input type="checkbox" id="tbjjg" name="tbjjg" class="text-right"  />提报交竣工
							</label>							
						 </td>
						<!-- <th width="5%" class="right-border bottom-border">完工</th>
						<td width="8%" class="bottom-border">
							<label class="checkbox inline">
							<input type="checkbox" id="jgysrq" name="JGYSRQ" value="shi">是</label>
							<label class="checkbox inline">
							<input type="checkbox" id="jgysrq" name="JGYSRQ" value="fou">否</label>
						</td> -->
			            <td class="text-left bottom-border text-right">
							<button	id="example_query" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
	                        	<button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
			            </td>																				
					</tr>
				</table>
			</form>
			<div style="height:5px;"> </div>								
			<div class="overFlowX"> 
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" printFileName="交竣工管理">
	                <thead>
	                    <tr>
	                     	<th id="_XH" name="XH" rowspan="2" tdalign="center" rowMerge="true" colindex=1>&nbsp;#&nbsp;</th>	
							<th fieldname="XMBH" rowspan="2" colindex=2  hasLink="true" rowMerge="true" linkFunction="rowView">&nbsp;项目编号&nbsp;</th> 
							<th fieldname="XMMC" rowspan="2" colindex=3 rowMerge="true" maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDBH"  rowspan="2"  maxlength="15" colindex=4 Customfunction="doBdBH" >&nbsp;标段编号&nbsp;</th>
							<th fieldname="BDMC" rowspan="2" colindex=5   Customfunction="doBdmc" maxlength="15">&nbsp;标段名称&nbsp;</th>
							<th fieldname="XMGLGS" rowspan="2" colindex=6  maxlength="15">&nbsp;项目管理公司&nbsp;</th>
							<th fieldname="XMDZ" rowspan="2" colindex=7  maxlength="15">&nbsp;项目地址&nbsp;</th>
							<th fieldname="WGRQ" rowspan="2" colindex=8 tdalign="center">&nbsp;完工时间&nbsp;</th>
							<th fieldname="JGYSRQ" rowspan="2" colindex=9 CustomFunction="queryJGSJ" tdalign="center">&nbsp;交工时间&nbsp;</th>
							<th fieldname="JGYSSJ"rowspan="2" colindex=10 tdalign="center">&nbsp;竣工时间&nbsp;</th>
				 			<th fieldname="SGDW" rowspan="2" colindex=11 maxlength="15">&nbsp;施工单位&nbsp;</th>
							<th fieldname="JLDW" rowspan="2" colindex=12 maxlength="15">&nbsp;监理单位&nbsp;</th>
							<th  colspan="3"  >&nbsp;交工&nbsp;</th>
			                <th  colspan="3"  >&nbsp;竣工&nbsp;</th>
			                <th fieldname="TBJJG" rowspan="2" colindex=19 maxlength="15">&nbsp;是否提交<br/>交竣工&nbsp;</th>
			                <th fieldname="XMLX" rowspan="2" colindex=20 maxlength="15">&nbsp;项目类型&nbsp;</th>
						</tr>
			            <tr>
							<th fieldname="JIAOGJSDW" colindex=13 maxlength="10">&nbsp;接收单位&nbsp;</th>
							<th fieldname="JIAOGJSR"  colindex=14 maxlength="10">&nbsp;接收人&nbsp;</th>
							<th fieldname="JIAOGONGFJ"  colindex=15 noprint="true" >&nbsp;附件&nbsp;</th>
							<th fieldname="JUNGJSDW" colindex=16 maxlength="10">&nbsp;参加单位&nbsp;</th>
							<th fieldname="JUNGJSR"  colindex=17 maxlength="10">&nbsp;参加人&nbsp;</th>
							<th fieldname="JUNGONGFJ"  colindex=18  noprint="true">&nbsp;附件&nbsp;</th>
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
		<input type="hidden" name="txtFilter" order="asc" fieldname="sj.xmbh, sj.xmbs, sj.pxh"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<input type="hidden" name="queryResult" id = "queryResult" />
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
	</FORM>
</div>
</body>
</html>