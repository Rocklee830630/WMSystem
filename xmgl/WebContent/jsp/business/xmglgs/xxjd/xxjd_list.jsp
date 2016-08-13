<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>项目管理公司-形象进度</title>
<%
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String userid = user.getAccount();
	String username = user.getName();
	String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xxjd/xxjdController.do";
var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
var controllername_tb ="${pageContext.request.contextPath }/common_yw/kgtjgl/kgtjglController.do";
var xmglgs = '<%=deptId%>';
var sysdate = '<%=sysdate %>';
//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#xxjdList").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	
	setPageHeight();
	//自动完成项目名称模糊查询
	showAutoComplete("QXMMC",autocompleteXmmcController+"?xmmcAutoQueryByXmglgs","getXmmcQueryCondition"); 
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	
	//按钮绑定事件（统筹计划反馈）
	$("#btnTcjhfkt").click(function() {
		if($("#xxjdList").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#xxjdList").getSelectedRow());
		var obj = $("#xxjdList").getSelectedRowJsonByIndex($("#xxjdList").getSelectedRowIndex());
		var jhsjid = convertJson.string2json1(obj).GC_JH_SJ_ID;
		$(window).manhuaDialog({"title":"形象进度管理>统筹计划反馈","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmglgs/xxjd/xxjd_tcjhfk.jsp?jhsjid="+jhsjid});
	});
	//按钮绑定事件（形象进度计划编制）
	$("#btnXxjdjhbz").click(function() {
		if($("#xxjdList").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#xxjdList").getSelectedRow());
		var obj = $("#xxjdList").getSelectedRowJsonByIndex($("#xxjdList").getSelectedRowIndex());
		var jhbzid = convertJson.string2json1(obj).GC_XMGLGS_XXJD_JHBZ_ID;
		var jhfsid = convertJson.string2json1(obj).GC_XMGLGS_XXJD_JHFK_ID;
		$(window).manhuaDialog({"title":"形象进度管理>形象进度计划编制","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmglgs/xxjd/xxjd_xxjdjhbz.jsp?jhbzid="+jhbzid+"&jsfkid="+jhfsid});
	});
	//按钮绑定事件（形象进度计划反馈）
	$("#btnXxjdjhfk").click(function() {
		if($("#xxjdList").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#xxjdList").getSelectedRow());
		var obj = $("#xxjdList").getSelectedRowJsonByIndex($("#xxjdList").getSelectedRowIndex());
		var jhfkId = convertJson.string2json1(obj).GC_XMGLGS_XXJD_JHFK_ID;
		$(window).manhuaDialog({"title":"形象进度管理>形象进度计划反馈","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmglgs/xxjd/xxjd_xxjdjhfk.jsp?jhfkId="+jhfkId});
	});
	//按钮绑定事件（生成施工任务单）
	$("#btnScsgrwd").click(function() {
		
	});
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 if(exportRequireQuery($("#xxjdList"))){//该方法需传入表格的jquery对象
		      printTabList("xxjdList");
		  }
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
    });
	//统筹计划反馈
    $("#tcjhfk").click(function() {
        if($("#xxjdList").getSelectedRowIndex()==-1){
        	requireSelectedOneRow();
            return;
    	}else{
        	$("#resultXML").val($("#xxjdList").getSelectedRow());
        	var tempJson = $("#xxjdList").getSelectedRowJsonObj();
        	openJhfkPage(tempJson.GC_JH_SJ_ID,"1008","queryForm","xxjdList",controllername+"%3Fquery%26xmglgs="+xmglgs);
    	}
    });

	//提报交竣工
    $("#tbjjg").click(function() {
        if($("#xxjdList").getSelectedRowIndex()==-1){
        	requireSelectedOneRow();
            return;
    	}else{
    		xConfirm("提示信息","是否提报交竣工！");
    		$('#ConfirmYesButton').unbind();
    		$('#ConfirmYesButton').one("click",function(){ 
    			var actionFlag = true;
   				var data=$("#xxjdList").getSelectedRow();
   				var obj=convertJson.string2json1(data);
   				data=JSON.stringify(obj);
   				//组成保存json串格式
   				var data1 = defaultJson.packSaveJson(data);
   				//调用ajax插入
   				actionFlag =defaultJson.doUpdateJson(controllername_tb + "?insert_tb&nd="+obj.ND,data1, xxjdList);
   				if(actionFlag==false){
   					return false;
   				}
    			if(actionFlag==false){
    				//xAlert("信息提示","更新失败！");
    			}else{
    				//$("#checkAll").removeAttr("checked");//取消#右侧复选框选中状态
    				//xAlert("信息提示","更新成功！");
    			}
    		 }); 		
    	}
    }); 
});
//页面默认参数
function init(){
	getNd();
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}
//默认年度
function getNd(){
	setDefaultNd("qnd");
	//setDefaultOption($("#qnd"),new Date().getFullYear());
}
//行选事件
function tr_click(obj){
	if(obj.GC_XMGLGS_XXJD_JHFK_ID == ""){
		$("#btnXxjdjhfk").attr("disabled","disabled");
	}else if(obj.ZT_BZ == "0" && obj.GC_XMGLGS_XXJD_JHFK_ID !=""){
		$("#btnXxjdjhfk").removeAttr("disabled");
	}else{
		$("#btnXxjdjhfk").removeAttr("disabled");
	}
	obj.ZT==1&&obj.SJWGSJ!=""?$("#tbjjg").attr("disabled",false):$("#tbjjg").attr("disabled",true);	
}

//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xxjdList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query&xmglgs="+xmglgs,data,xxjdList);
}

//计划编制返回函数
function getWinData_jhbz(data,xmlx){
	var resJson = convertJson.string2json1(data);
	var data1 = defaultJson.packSaveJson(JSON.stringify(resJson));
	defaultJson.doUpdateJson(controllername + "?insertJhbz&xmlx="+xmlx, data1,xxjdList);
	if(convertJson.string2json1(data).ZT_BZ == "1"){
		$("#btnXxjdjhfk").removeAttr("disabled");
	}else if(convertJson.string2json1(data) == "0"){
		
	}
}

//计划反馈返回函数
function getWinData_jhfk(data){
	var data2 = defaultJson.packSaveJson(data);
	defaultJson.doUpdateJson(controllername + "?insertJhfk", data2,xxjdList);
	
}

//回调函数
getWinData = function(data){
	var data1 = defaultJson.packSaveJson(data);
	if(convertJson.string2json1(data).GC_XMGLGS_XXJD_ID == ""){
		defaultJson.doUpdateJson(controllername + "?insert", data1,xxjdList);
	}else{
		defaultJson.doUpdateJson(controllername + "?insert", data1,xxjdList);
	}
};
//项目名称自动模糊查询参数
function getXmmcQueryCondition(){
	var initData = '{"querycondition":{"conditions":[]}}';
	var jsonData = convertJson.string2json1(initData);
	//年度
	if("" != $("#qnd").val()){
		var defineCondition = {"value": $("#qnd").val(),"fieldname":"t.ND","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	//项目名称
	if("" != $("#QXMMC").val()){
		var defineCondition = {"value": "%"+$("#QXMMC").val()+"%","fieldname":"t.XMMC","operation":"like","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	if("" != $("#XMGLGS").val()){
		var defineCondition = {"value": +xmglgs,"fieldname":"t.XMGLGS","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	return JSON.stringify(jsonData);
}
//详细信息
function rowView(index){
	var obj = $("#xxjdList").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).XMID;
	$(window).manhuaDialog(xmscUrl(id));
}
//标段图标样式
function doBdmc(obj){
	if(obj.XMBS == "0"){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}
//<状态>样式
function doZt(obj){
	if(obj.ZT == "0"){
		return '<span class="label label-important">'+obj.ZT_SV+'</span>';
	}else if(obj.ZT == "1"){
		return '<span class="label label-success">'+obj.ZT_SV+'</span>';
	}
	
}
//<编制>样式
function doBZ(obj){
	if(obj.ZT_BZ == "0"){
		return '<span class="label label-important">'+obj.ZT_BZ_SV+'</span>';
	}else if(obj.ZT_BZ == "1"){
		return '<span class="label label-success">'+obj.ZT_BZ_SV+'</span>';
	}
	
}
//<反馈>样式
function doFK(obj){
	if(obj.ZT_FK == "0"){
		return '<span class="label label-important">'+obj.ZT_FK_SV+'</span>';
	}else if(obj.ZT_FK == "1"){
		return '<span class="label label-success">'+obj.ZT_FK_SV+'</span>';
	}
	
}
//列表项<健康状况>状态
function doJkzk(obj){
	var days = 0;
	var showInfo = "";
	if(obj.SJKGSJ !="" && obj.SJWGSJ != ""){
		return '<div style="text-align:center"><i title="正常执行" class="icon-green"></i></div>';
	}else if(obj.KGSJ == "" && obj.WGSJ == ""){
		return '<div style="text-align:center"><i title="正常执行" class="icon-green"></i></div>';
	}else if(obj.KGSJ == "" && obj.WGSJ != ""){
		days = getDays(sysdate,obj.WGSJ);
		if(days >= 5 && days < 20 ){
			showInfo = '<div style="text-align:center"><i title="超期完成" class="icon-yellow"></i></div>';
		}else if(days >= 20){
			showInfo = '<div style="text-align:center"><i title="严重超期完成" class="icon-red"></i></div>';
		}else{
			showInfo = '<div style="text-align:center"><i title="正常执行" class="icon-green"></i></div>';
		}
	}else if(obj.KGSJ != "" && obj.WGSJ == ""){
		days = getDays(sysdate,obj.KGSJ);
		if(days >= 5 && days < 20 ){
			showInfo = '<div style="text-align:center"><i title="超期完成" class="icon-yellow"></i></div>';
		}else if(days >= 20){
			showInfo = '<div style="text-align:center"><i title="严重超期完成" class="icon-red"></i></div>';
		}else{
			showInfo = '<div style="text-align:center"><i title="正常执行" class="icon-green"></i></div>';
		}
	}else if(obj.KGSJ != "" && obj.WGSJ != ""){
		days = getDays(sysdate,obj.KGSJ);
		var days1 = getDays(sysdate,obj.WGSJ);
		if(days >= days1){
			if(days >= 5 && days < 20 ){
				showInfo = '<div style="text-align:center"><i title="超期完成" class="icon-yellow"></i></div>';
			}else if(days >= 20){
				showInfo = '<div style="text-align:center"><i title="严重超期完成" class="icon-red"></i></div>';
			}else{
				showInfo = '<div style="text-align:center"><i title="正常执行" class="icon-green"></i></div>';
			}
		}else{
			if(days1 >= 5 && days1 < 20 ){
				showInfo = '<div style="text-align:center"><i title="超期完成" class="icon-yellow"></i></div>';
			}else if(days1 >= 20){
				showInfo = '<div style="text-align:center"><i title="严重超期完成" class="icon-red"></i></div>';
			}else{
				showInfo = '<div style="text-align:center"><i title="正常执行" class="icon-green"></i></div>';
			}
		}
	}
	return showInfo;
}
//计算日期天数
function getDays(strDateStart,strDateEnd){
	if(strDateStart < strDateEnd){
		return 0;
	}
   var strSeparator = "-"; //日期分隔符
   var oDate1;
   var oDate2;
   var iDays;
   oDate1= strDateStart.split(strSeparator);
   oDate2= strDateEnd.split(strSeparator);
   var strDateS = new Date(oDate1[0] + "-" + oDate1[1] + "-" + oDate1[2]);
   var strDateE = new Date(oDate2[0] + "-" + oDate2[1] + "-" + oDate2[2]);
   iDays = parseInt(Math.abs(strDateS - strDateE ) / 1000 / 60 / 60 /24)//把相差的毫秒数转换为天数 

   return iDays ;
}

//删除返回函数
function getWinData_del(data){
	var data1 = defaultJson.packSaveJson(data);
	defaultJson.doUpdateJson(controllername+"?delete",data1,xxjdList,null); 
}
//标段编号图标样式
function doBdbh(obj){
	if(obj.BDBH == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDBH;
	}
}

//甘特图
function showGtt(id, jhbzid,bz_xmlx){
	if(jhbzid == "") {
		requireFormMsg("此项目还未编制形象进度!");
	} else {
		if(bz_xmlx=="")
		{
			return;
		}
		else
		{
			$(window).manhuaDialog({"title":"项目甘特图","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmglgs/xxjd/xxjd_gtt.jsp?id="+id,"modal":"1"});
		}	
	}
}
//设置甘特图字段样式
function doGtt(obj){
	var id = obj.GC_JH_SJ_ID;
	var jhbzid = obj.GC_XMGLGS_XXJD_JHBZ_ID;
	var bz_xmlx=obj.BZ_XMLX;
	return "<div style=\"text-align:center\"><a href=\"javascript:void(0);\"><i title=\"查看甘特图\" class=\"icon-tasks\" onclick=\"showGtt('"+id+"','"+jhbzid+"','"+bz_xmlx+"')\"></i></a></div>";
	//return "<a href=\"#\" onclick=\"showGtt(\'"+id+"\')\">干特图</a>";
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">形象进度
      	<span class="pull-right">
      	<button id="tbjjg" class="btn" type="button" title="已完工的项目，可以提报设计组办理交竣工">提报交竣工</button>
      	<app:oPerm url="jsp/business/xmglgs/xxjd/xxjd_tcjhfk.jsp">
      		<button id="tcjhfk" class="btn" type="button">统筹计划反馈</button>
  			<!-- <button id="btnTcjhfkt" class="btn" type="button">统筹计划反馈</button> -->
  		</app:oPerm>
  		<app:oPerm url="jsp/business/xmglgs/xxjd/xxjd_xxjdjhbz.jsp">
  			<button id="btnXxjdjhbz" class="btn" type="button">形象进度计划编制</button>
  		</app:oPerm>
  		<app:oPerm url="jsp/business/xmglgs/xxjd/xxjd_xxjdjhfk.jsp">
  			<button id="btnXxjdjhfk" class="btn" type="button">形象进度计划反馈</button>
  		</app:oPerm>
  		<app:oPerm url="jsp/business/xmglgs/xxjd/scsgrwd.jsp">
  			<button id="btnScsgrwd" class="btn" type="button">生成施工任务单</button>
  		</app:oPerm>
  			<button id="btnExpExcel" class="btn"  type="button">导出</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
        	<th width="5%" class="right-border bottom-border text-right">年度</th>
	        <td class="right-border bottom-border" width="6%">
	            <select class="span12 year" id="qnd" name = "QND" fieldname ="t.ND" defaultMemo="全部" operation="=" kind="dic" src="T#GC_JH_SJ:distinct ND:ND  as NND:SFYX='1'  order by NND  asc ">
            	</select>
	        </td>
	        <th width="5%" class="right-border bottom-border text-right">项目名称</th>
			<td class="right-border bottom-border" width="20%"><input
			class="span12" type="text" placeholder="" name="QXMMC"
			fieldname="t.XMMC" operation="like" id="QXMMC"
			autocomplete="off" tablePrefix="t">
		  </td>
			<td class="text-left bottom-border text-right">
	        	<button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
	        	<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
	        </td>
		</tr>
      </table>
      </form>
<div style="height:5px;"> </div>
<div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="xxjdList" width="100%" type="single" pageNum="10" printFileName="形象进度">
		<thead>
			<tr>
				<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
				<th fieldname="ZT" rowspan="2" colindex=2 tdalign="center" CustomFunction="doZt" noprint="true">&nbsp;统筹计划状态&nbsp;</th>
				<th fieldname="XMID" rowspan="2" colindex=3 CustomFunction="doJkzk" noprint="true">&nbsp;健康&nbsp;<BR>&nbsp;状况&nbsp;</th>
				<th fieldname="XMID" rowspan="2" colindex=4 CustomFunction="doGtt" noprint="true">甘特图</th> 
				<th fieldname="XMBH" rowspan="2" colindex=5 rowMerge="true" hasLink="true" linkFunction="rowView" >&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" rowspan="2" colindex=6 maxlength="15" rowMerge="true">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDBH" rowspan="2" colindex=7 CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" rowspan="2" colindex=8 CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
				<th fieldname="XMBDDZ" rowspan="2" colindex=9 >&nbsp;项目地址&nbsp;</th>
				<th fieldname="YZDB" rowspan="2" colindex=10 tdalign="center">&nbsp;业主&nbsp;<BR>&nbsp;代表&nbsp;</th>
				<th fieldname="SGDW" rowspan="2" colindex=11 >&nbsp;施工单位&nbsp;</th>
				<th fieldname="JLDW" rowspan="2" colindex=12 >&nbsp;监理单位&nbsp;</th>
				<th fieldname="JSMB" rowspan="2" colindex=13 maxlength="15">&nbsp;年度目标&nbsp;</th>
				<th colspan="2">&nbsp;计划时间&nbsp;</th>
				<th colspan="2">&nbsp;实际时间&nbsp;</th>
				<th fieldname="FXMS" rowspan="2" colindex=18 >&nbsp;风险描述&nbsp;</th>
				<th fieldname="TBJJG" rowspan="2" colindex=19 >&nbsp;是否提报</br>交竣工&nbsp;</th>
				<th fieldname="XMLX" rowspan="2" colindex=20 >&nbsp;项目类型&nbsp;</th>
			</tr>
			<tr>
				<th fieldname="KGSJ" colindex=14 tdalign="center">&nbsp;开工&nbsp;</th>
				<th fieldname="WGSJ" colindex=15 tdalign="center">&nbsp;完工&nbsp;</th>
				<th fieldname="SJKGSJ" colindex=16 tdalign="center">&nbsp;开工&nbsp;</th>
				<th fieldname="SJWGSJ" colindex=17 tdalign="center">&nbsp;完工&nbsp;</th>
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
       <input type="hidden" name="txtFilter"  order="ASC" fieldname = "T.XMBH,T.XMBS,T.PXH"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>