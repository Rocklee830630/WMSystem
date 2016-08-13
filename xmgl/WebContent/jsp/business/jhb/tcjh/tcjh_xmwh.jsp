<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>统筹计划管理-维护</title>
<%
java.util.Calendar cal = java.util.Calendar.getInstance();
int year = cal.get(java.util.Calendar.YEAR);
%>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script src="${pageContext.request.contextPath }/js/common/FixTable.js"></script>

<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
var objRow;
var window_width;

//页面初始化
$(function() {
	//自动完成项目名称模糊查询
	showAutoComplete("QXMMC",controllername+"?xmmcAutoQuery","getXmmcQueryCondition"); 
	init();
	
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
 	 	if(!$("#queryForm").validationButton()){
			xInfoMsg('请选择<批次>！',"");
			return
		}
		queryList();
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		//var indexarry = new Array();
		var indexarry = new Array();
		indexarry = $("#DT1").getChangeRows();
		if(indexarry == "")
		{
			xInfoMsg('请至少修改一条记录！',"");
			return
 		}
		var jhid = $("#JHID").val();
		//alert(jhid);
		$("#_fixTableHeader").hide();
	    $("#_fixTableColumn").hide();
		xConfirm("信息提示","是否保存为新版本？");
		$('#ConfirmYesButton').unbind();
		$('#ConfirmYesButton').one("click",function(){  
			$(window).manhuaDialog({"title":"统筹计划管理>保存版本记录","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/tcjh/bbjl_add.jsp?jhid="+jhid,"modal":"4"});
			return;
		});  
		
		$('#ConfirmCancleButton').unbind();
		$('#ConfirmCancleButton').one("click",function(){  
			var url = controllername + "?updatebatchdataNobg&JHID="+$("#JHID").val();
			//获取表格表头的数组,按照表格显示的顺序
			var tharrays = new Array();
			var comprisesData;
	 		var indexarry = new Array();
			indexarry = $("#DT1").getAllRowsJOSNString();
			tharrays = $("#DT1").getTableThArrays();
			if(tharrays != null){
				comprisesData = $("#DT1").comprisesData(indexarry,tharrays);

				defaultJson.doUpdateBatchJson(url, comprisesData,DT1,indexarry,"callbackUpdateDate");

			}
		});
		

	});
	

	//按钮绑定事件(暂存)
	$("#btnLsSave").click(function() {
		var indexarry = $("#DT1").getChangeRows();
		if(indexarry == "")
		{
			xInfoMsg('请至少修改一条记录！',"");
			return
 		}
		var url = controllername + "?updatebatchdataNobg&JHID="+$("#JHID").val();
		//获取表格表头的数组,按照表格显示的顺序
		var tharrays = new Array();
		var comprisesData;
 		var indexarry = new Array();
		indexarry = $("#DT1").getAllRowsJOSNString();
		tharrays = $("#DT1").getTableThArrays();
		if(tharrays != null){
			comprisesData = $("#DT1").comprisesData(indexarry,tharrays);

			defaultJson.doUpdateBatchJson(url, comprisesData,DT1,indexarry,"callbackUpdateDate");

		}
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
       
    });
});
//页面默认参数
function init(){
	getNd();
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}
function tr_click(obj){
	objRow = obj;
}
function xdtcjhByPc(actionName, data1){
	var success = true;
	$.ajax({
		type : 'post',
		url : actionName,
		data : data1,
		dataType : 'json',
		async :	false,
		success : function(result) {
			success = true;
		},
	    error : function(result) {
		     	alert(result.msg);
			    defaultJson.clearTxtXML();
			    success = false;
		}
	});
	 return success;
}
//弹出窗口关闭--弹出页面回调空函数
function closeNowCloseFunction(){
	$(window).manhuaDialog.getParentObj().queryList();
}
//回调函数
getWinData = function(data){
	
	var url = controllername + "?updatebatchdata&JHID="+$("#JHID").val()+"&YWID="+data.YWID+"&BGSM="+encodeURIComponent(data.BGSM);
	var indexarry = new Array();//获取所有行的数组
	var indexArryChange = new Array();//获得经变更行的数组
	indexArryChange = $("#DT1").getChangeRows();
	var tids = "";
	for(var i =0;i<indexArryChange.length;i++){
		tids +=$("#DT1").getRowJsonObjByIndex(indexArryChange[i]).GC_JH_SJ_ID+",";
	}
	indexarry = $("#DT1").getAllRowsJOSNString();
	//获取表格表头的数组,按照表格显示的顺序
	var tharrays = new Array();
	var comprisesData;
	tharrays = $("#DT1").getTableThArrays();
	if(tharrays != null){
		
		comprisesData = $("#DT1").comprisesData(indexarry,tharrays);
		//alert(DT1);
		defaultJson.doUpdateBatchJson(url+"&TIDS="+tids, comprisesData,DT1,indexarry,"callbackUpdateDate");
	}
	
};
function callbackUpdateDate(){
//	queryList();
//	$(window).manhuaDialog.getParentObj().queryList();
}
//列表项<项目地址>加图标
function doDz(obj){
	var xmdz = obj.XMDZ;
	if(xmdz != ""){
		if(xmdz.length>15){
			xmdz = '<abbr title="'+obj.XMDZ+'">'+xmdz.substring(0,15)+'...&nbsp;<a href="javascript:void(0);" onclick="selectDz()"><i title="查看" class="pull-right icon-map-marker"></i></a></abbr>';
		}else{
			xmdz = xmdz+'&nbsp;<a href="javascript:void(0);" onclick="selectDz()"><i title="查看" class="pull-right icon-map-marker"></a></i>';
		}
		return xmdz;
	}
	
}
//点击项目地址图标
function selectDz(){
	window.open("${pageContext.request.contextPath }/jsp/business/jhb/xdxmk/img/earth.png");
}
//默认年度
function getNd(){
	//$("#qnd").val(new Date().getFullYear());
	generateFyjjhNdMc($("#qnd"),$("#JHID"));
	setOptionSelectedIndex($("#JHID"));
}
function queryAfter(){
	var xHeight = parent.document.documentElement.clientHeight;
	var height = xHeight-pageTopHeight-pageQuery-getTableTh(2);
	var pageNum = parseInt(height/pageTableOne,10);
	var rows = $("tbody tr" ,$("#DT1"));
	var tr_obj = rows.eq(0);
     var t = $("#DT1").getTableRows();
     var tr_height = $(tr_obj).height();
     var d = pageNum*tr_height;
  	// 当高度大于500时，显示主页面列表的高度
     if(d>500) d = getDivStyleHeight()-pageQuery-getTableTh(3);
      // 当没有数据的时候，只显示表头
  	if(tr_height==null) d = getTableTh(3)+20;
     window_width = document.documentElement.clientWidth;//$("#allDiv").width()
   //  alert(window_width)
	    $("#DT1").fixTable({
			fixColumn: 4,//固定列数
			width:window_width-13,//显示宽度
			height:d//显示高度
		});
}
//查询列表   
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,DT1,"queryAfter",false);

}
//项目名称自动模糊查询参数
function getXmmcQueryCondition(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	return data;
}
/**
 * 根据计划查询非应急批次
 * ndObj 年度的jquery对象
 * mcObj 计划名称jquery对象
 */
function generateFyjjhNdMc(ndObj,mcObj){
	ndObj.attr("src","T#GC_JH_SJ: distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	//ndObj.val(new Date().getFullYear());
	//setDefaultOption(ndObj,new Date().getFullYear());
	setDefaultNd(ndObj.attr("id"));
	if(mcObj){
		mcObj.attr("kind","dic");
		/* mcObj.attr("src","T#GC_JH_ZT:GC_JH_ZT_ID:jhpch:SFYX='1' AND ND='" + ndObj.val()+"' and GC_JH_ZT_ID in(select JHID from GC_JH_SJ where sfyx='1' and nd='"+ ndObj.val()+"' and XMSX='1') ORDER BY JHPCH ASC"); */
		//调整为应急项目也可以编制计划 modified by hongpeng.dong at 2013.12.1
		mcObj.attr("src","T#GC_JH_ZT:GC_JH_ZT_ID:jhpch:SFYX='1' AND ND='" + ndObj.val()+"' and GC_JH_ZT_ID in(select JHID from GC_JH_SJ where sfyx='1' and nd='"+ ndObj.val()+"') ORDER BY JHPCH ASC");
		mcObj.html('');
		reloadSelectTableDic(mcObj);
		ndObj.change(function() {
			mcObj.html('');
			if(!ndObj.val().length){
			}
			/* mcObj.attr("src","T#GC_JH_ZT:GC_JH_ZT_ID:jhpch:SFYX='1' AND ND='" + ndObj.val()+"' and GC_JH_ZT_ID in(select JHID from GC_JH_SJ where sfyx='1' and nd='"+ ndObj.val()+"' and XMSX='1') ORDER BY JHPCH ASC"); */
			mcObj.attr("src","T#GC_JH_ZT:GC_JH_ZT_ID:jhpch:SFYX='1' AND ND='" + ndObj.val()+"' and GC_JH_ZT_ID in(select JHID from GC_JH_SJ where sfyx='1' and nd='"+ ndObj.val()+"') ORDER BY JHPCH ASC");
			reloadSelectTableDic(mcObj);
		});
	}
}
//标段图标样式
function doBdmc(obj){
	if(obj.XMBS == "0"){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}

//标段编号图标样式
function doBdbh1(obj){
	if(obj.BDBH == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDBH;
	}
}
//标段编号图标样式
function doBdbh2(obj){
	if(obj.BDBH == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDBH;
	}
}
//开工时间样式
function doKGSJ(obj){
	if(obj.ISKGSJ == "0"){
		$(obj).attr("disabled",true);
	}
}
function KGJD(obj,fieldname){
	var fName = fieldname.substring(2,fieldname.length);
	//var index = $(event.target).closest("tr").index();
	
	var rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
	var temp = convertJson.string2json1(rowValue);
	var xmid = temp.XMID;//项目ID
	var aObj;
	var bObj;
	var xObj;
	var bsObj;
	if(temp.XMBS == "0" && temp.ISNOBDXM == '0'){
		$("#DT1 tbody tr").each(function(i){
			xObj = convertJson.string2json1($("#DT1").getSelectedRowJsonByIndex(i)).XMID;
			bObj = $(this).find("input[fieldname='"+fName+"']");
			aObj = $(this).find("select[fieldname='"+fieldname+"']");
			if(xObj == xmid){
				if($(obj).val() == "0"){
					aObj.val("0");
					bObj.val("");
					bObj.attr("disabled",true);
				}else{
					bObj.attr("disabled",false);
				}
			}
		});
	}else if(temp.XMBS == "1"){
		if($(obj).val() == "1"){
			$("#DT1 tbody tr").each(function(i){
				xObj = convertJson.string2json1($("#DT1").getSelectedRowJsonByIndex(i)).XMID;
				bsObj = convertJson.string2json1($("#DT1").getSelectedRowJsonByIndex(i)).XMBS;
				bObj = $(this).find("input[fieldname='"+fName+"']");
				aObj = $(this).find("select[fieldname='"+fieldname+"']");
				if(xObj == xmid && bsObj =="0"){
					aObj.val("1");
					bObj.attr("disabled",false);
					var kgsjObj = $(event.target).closest("tr").find("input[fieldname='"+fName+"']");
					kgsjObj.attr("disabled",false);
				}
			});
		}else{
			var kgsjObj = $(event.target).closest("tr").find("input[fieldname='"+fName+"']");
			if($(obj).val() == "0"){
				kgsjObj.val("");
				kgsjObj.attr("disabled",true);
			}else{
				kgsjObj.attr("disabled",false);
			}
		}
	}
	/**
	
	//alert(rowValue);
	
	if($(obj).val() == "0"){
		tmep.attr("disabled",true);
	}
	*/
	//alert(tmep.KGSJ);
	//alert($(obj).val());
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">统筹计划管理
      	<span class="pull-right">
      	<button id="btnSave" class="btn"  type="button">保存</button>
      	<button id="btnLsSave" class="btn"  type="button">暂存</button>
      	</span>
      </h4>
     <form method="post" id="queryForm">
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<!-- <input type="text" fieldname="t.XMSX" name="qXMSX" value="1" keep="true" operation="="> --> 
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
          <th width="5%" class="right-border bottom-border text-right">年度</th>
          <td class="right-border bottom-border" width="6%">
            <select class="span12 year" id="qnd" name = "QND"  fieldname = "t.ND" defaultMemo="全部"  operation="=">
            </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">批次</th>
          <td class="right-border bottom-border" width="8%">
          	<select class="span12 4characters" id="JHID" name = "QJHID"  fieldname = "t.JHID" defaultMemo="全部"  check-type="required" operation="=">
            </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">项目名称</th>
          <td class="right-border bottom-border" width="20%">
          	<input class="span12" type="text" placeholder="" name="QXMMC"
				fieldname="t.XMMC" operation="like" id="QXMMC" autocomplete="off"
				tablePrefix="t"/>
		  </td>
		  <th width="5%" class="right-border bottom-border text-right">项目管理公司</th>
			<td class="right-border bottom-border" width="7%">
				<select class="span12 2characters" id="xmglgs" name = "QXMGLGS" defaultMemo="全部"  fieldname = "t1.XMGLGS" operation="=" kind="dic" src="T#VIEW_YW_XMGLGS:ROW_ID:BMJC">
            	</select>
			</td>
          <td class="text-left bottom-border text-right">
            <button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
      		<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
          </td>

         </tr>
      </table>
      </form>
    </div>
  </div>
 <div class="row-fluid">
 <div class="B-small-from-table-auto">
  <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="DT1"  width="100%" editable="1" type="multi" noPage="true" pageNum="1000" isUpdate="noRefresh">
		<thead>
			<tr>
				<th name="XH" id="_XH" rowspan="3" colindex=1>&nbsp;#&nbsp;</th>
				<th fieldname="XMMC" rowspan="3" colspan=2 colindex=2 maxlength="15" tdalign="left">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDBH" rowspan="3" colindex=3 CustomFunction="doBdbh1">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" rowspan="3" colindex=4 maxlength="15" CustomFunction="doBdmc" >&nbsp;标段名称&nbsp;</th>
				<th fieldname="PXH" rowspan="3" colindex=5 tdalign="center" type="text" >&nbsp;排序号&nbsp;</th>
				<th colspan="4">&nbsp;工期安排（工程部及项目管理公司）&nbsp;</th>
				<th colspan="8">&nbsp;手续办理情况&nbsp;</th>
				<th colspan="8">&nbsp;设计情况&nbsp;</th>
				<th colspan="4">&nbsp;造价&nbsp;</th>
				<th colspan="4">&nbsp;招标&nbsp;</th>
				<th colspan="2" rowspan="2">&nbsp;征拆&nbsp;</th>
				<th colspan="2" rowspan="2">&nbsp;排迁&nbsp;</th>
				<th colspan="2" rowspan="2">&nbsp;交工&nbsp;</th>
				<th fieldname="BZ" rowspan="3" colindex=40 type="text" style="padding-left: 100px;padding-right: 100px;"> &nbsp;备注&nbsp; </th>
			</tr>
			<tr>
				<th colspan="2">&nbsp;开工&nbsp;</th>
				<th colspan="2">&nbsp;完工&nbsp;</th>
				<th colspan="2">&nbsp;可研批复&nbsp;</th>
				<th colspan="2">&nbsp;划拔决定书&nbsp;</th>
				<th colspan="2">&nbsp;工程规划许可证&nbsp;</th>
				<th colspan="2">&nbsp;施工许可&nbsp;</th>
				<th colspan="2">&nbsp;初步设计批复&nbsp;</th>
				<th colspan="2">&nbsp;拆迁图&nbsp;</th>
				<th colspan="2">&nbsp;排迁图&nbsp;</th>
				<th colspan="2">&nbsp;施工图&nbsp;</th>
				<th colspan="2">&nbsp;提报价&nbsp;</th>
				<th colspan="2">&nbsp;财审&nbsp;</th>
				<th colspan="2">&nbsp;监理单位&nbsp;</th>
				<th colspan="2">&nbsp;施工单位&nbsp;</th>
			</tr>
			<tr>
				<th fieldname="ISKGSJ" colindex=6 type="dic" src="QQSXZT" changeFunction="KGJD" noNullSelect ="true">&nbsp;是否办理&nbsp;</th>
				<th fieldname="KGSJ" colindex=7 type="date" CustomFunction="doKGSJ">&nbsp;计划时间&nbsp;</th>
				<th fieldname="ISWGSJ" colindex=8 type="dic" changeFunction="KGJD" src="QQSXZT" noNullSelect ="true">&nbsp;是否办理&nbsp;</th>
				<th fieldname="WGSJ" colindex=9 type="date">&nbsp;计划时间&nbsp;</th>
				<th fieldname="ISKYPF" colindex=10 type="dic" changeFunction="KGJD" src="QQSXZT" noNullSelect ="true">&nbsp;是否办理&nbsp;</th>
				<th fieldname="KYPF" colindex=11 type="date">&nbsp;计划时间&nbsp;</th>
				<th fieldname="ISHPJDS" colindex=12 type="dic" changeFunction="KGJD" src="QQSXZT" noNullSelect ="true">&nbsp;是否办理&nbsp;</th>
				<th fieldname="HPJDS" colindex=13 type="date">&nbsp;计划时间&nbsp;</th>
				<th fieldname="ISGCXKZ" colindex=14 type="dic" changeFunction="KGJD" src="QQSXZT" noNullSelect ="true">&nbsp;是否办理&nbsp;</th>
				<th fieldname="GCXKZ" colindex=15 type="date">&nbsp;计划时间&nbsp;</th>
				<th fieldname="ISSGXK" colindex=16 type="dic" changeFunction="KGJD" src="QQSXZT" noNullSelect ="true">&nbsp;是否办理&nbsp;</th>
				<th fieldname="SGXK" colindex=17 type="date">&nbsp;计划时间&nbsp;</th>
				<th fieldname="ISCBSJPF" colindex=18 type="dic" changeFunction="KGJD" src="QQSXZT" noNullSelect ="true">&nbsp;是否办理&nbsp;</th>
				<th fieldname="CBSJPF" colindex=19 type="date">&nbsp;计划时间&nbsp;</th>
				<th fieldname="ISCQT" colindex=20 type="dic" changeFunction="KGJD" src="QQSXZT" noNullSelect ="true">&nbsp;是否办理&nbsp;</th>
				<th fieldname="CQT" colindex=21 type="date">&nbsp;计划时间&nbsp;</th>
				<th fieldname="ISPQT" colindex=22 type="dic" changeFunction="KGJD" src="QQSXZT" noNullSelect ="true">&nbsp;是否办理&nbsp;</th>
				<th fieldname="PQT" colindex=23 type="date">&nbsp;计划时间&nbsp;</th>
				<th fieldname="ISSGT" colindex=24 type="dic" changeFunction="KGJD" src="QQSXZT" noNullSelect ="true">&nbsp;是否办理&nbsp;</th>
				<th fieldname="SGT" colindex=25 type="date">&nbsp;计划时间&nbsp;</th>
				<th fieldname="ISTBJ" colindex=26 type="dic" changeFunction="KGJD" src="QQSXZT" noNullSelect ="true">&nbsp;是否办理&nbsp;</th>
				<th fieldname="TBJ" colindex=27 type="date">&nbsp;计划时间&nbsp;</th>
				<th fieldname="ISCS" colindex=28 type="dic" changeFunction="KGJD" src="QQSXZT" noNullSelect ="true">&nbsp;是否办理&nbsp;</th>
				<th fieldname="CS" colindex=29 type="date">&nbsp;计划时间&nbsp;</th>
				<th fieldname="ISJLDW" colindex=30 type="dic" changeFunction="KGJD" src="QQSXZT" noNullSelect ="true">&nbsp;是否办理&nbsp;</th>
				<th fieldname="JLDW" colindex=31 type="date">&nbsp;计划时间&nbsp;</th>
				<th fieldname="ISSGDW" colindex=32 type="dic" changeFunction="KGJD" src="QQSXZT" noNullSelect ="true">&nbsp;是否办理&nbsp;</th>
				<th fieldname="SGDW" colindex=33 type="date">&nbsp;计划时间&nbsp;</th>
				<th fieldname="ISZC" colindex=34 type="dic" changeFunction="KGJD" src="QQSXZT" noNullSelect ="true">&nbsp;是否办理&nbsp;</th>
				<th fieldname="ZC" colindex=35 type="date">&nbsp;计划时间&nbsp;</th>
				<th fieldname="ISPQ" colindex=36 type="dic" changeFunction="KGJD" src="QQSXZT" noNullSelect ="true">&nbsp;是否办理&nbsp;</th>
				<th fieldname="PQ" colindex=37 type="date">&nbsp;计划时间&nbsp;</th>
				<th fieldname="ISJG" colindex=38 type="dic" changeFunction="KGJD" src="QQSXZT" noNullSelect ="true">&nbsp;是否办理&nbsp;</th>
				<th fieldname="JG" colindex=39 type="date">&nbsp;计划时间&nbsp;</th>
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
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="asc" fieldname = "PXH,XMBH,XMBS,bdbh"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>