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
<title>工程部-开复工令管理</title>
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
var controllername= "${pageContext.request.contextPath }/kfgl/kfglController.do";
var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
var xmglgs = <%=deptId%>;

//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#kfglList").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	
	setPageHeight();
	
	//自动完成项目名称模糊查询
	showAutoComplete("QXMMC",autocompleteXmmcController+"?xmmcAutoQueryByXmglgs","getQueryCondition"); 
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 if(exportRequireQuery($("#kfglList"))){//该方法需传入表格的jquery对象
		      //printTabList("kfglList");
		      printTabList("kfglList","kfgl.xls","XMBH,XMMC,BDMC,XMLX,M_KGSJ,M_FGSJ,M_TGSJ","3,1");
		  }
	});
	//按钮绑定事件(指令管理)
	$("#btnXmwh").click(function() {
		if($("#kfglList").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		var rowValue = $("#kfglList").getSelectedRow();//获得选中行的json对象
		var id = convertJson.string2json1(rowValue).GC_JH_SJ_ID;
		$("#resultXML").val($("#kfglList").getSelectedRow());
		$(window).manhuaDialog({"title":"开复工令管理>指令管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/kfglgl/xmwh.jsp?id="+id});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
        //其他处理放在下面
    });
   
});
//页面默认参数
function init(){
	getNd();
	
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}
//详细信息
function rowView(index){
	var obj = $("#kfglList").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).GC_TCJH_XMXDK_ID;
	$(window).manhuaDialog(xmscUrl(id));
}
//默认年度
function getNd(){
	setDefaultNd("qnd");
	//setDefaultOption($("#qnd"),new Date().getFullYear());
}
//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,kfglList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query&xmglgs="+xmglgs,data,kfglList);	
}
//自定义的获取页面查询条件
function getQueryCondition(){

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
//点击行事件
function tr_click(obj){
	
}
//标段图标样式
function doBdmc(obj){
	if(obj.XMBS == "0"){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}
//页刷新
function gs_feedback(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,kfglList);
	//调用ajax插入
	data = defaultJson.getQueryConditionWithNowPageNum(data,"kfglList");
	defaultJson.doQueryJsonList(controllername+"?query&xmglgs="+xmglgs,data,kfglList);	
	 	/* var row_index=$("#kfglList").getSelectedRowIndex();
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,kfglList);
		var tempJson = convertJson.string2json1(data);
		var a=$("#kfglList").getCurrentpagenum();
		tempJson.pages.currentpagenum=a;
		data = JSON.stringify(tempJson);
		defaultJson.doQueryJsonList(controllername+"?query",data,kfglList);
		$("#kfglList").setSelect(row_index);
		var json=$("#kfglList").getSelectedRow();
		json=encodeURI(json); */
		/* $("#sp_btn")[0].innerText = "呈请审批";
		$("#sp_btn").removeAttr("disabled");
		$("#btnSave").removeAttr("disabled");	 */
}
//标段编号图标样式
function doBdbh(obj){
	if(obj.BDBH == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDBH;
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
      <h4 class="title">开复工令管理
      	<span class="pull-right">
      	<app:oPerm url="jsp/business/gcb/kfglgl/xmwh.jsp">
      		<button id="btnXmwh" class="btn" type="button">指令管理</button>
      	</app:oPerm>
      		<button id="btnExpExcel" class="btn"  type="button">导出</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table">
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
            <select class="span12 year" id="qnd" name = "QND" defaultMemo="全部" fieldname = "t.ND" operation="=" kind="dic" src="T#GC_JH_SJ: distinct ND:ND NND:SFYX='1'  order by NND  asc ">
            </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">项目名称</th>
          <td class="right-border bottom-border" width="20%"><input
			class="span12" type="text" placeholder="" name="QXMMC"
			fieldname="t.XMMC" operation="like" id="QXMMC"
			autocomplete="off" tablePrefix="t">
		  </td>
          <td class="text-left bottom-border text-right">
           <button id="btnQuery" class="btn btn-link"  type="button" ><i class="icon-search"></i>查询</button>
           <button id="btnClear" class="btn btn-link" type="button" ><i class="icon-trash"></i>清空</button>
          </td>
          
         </tr>
      </table>
      </form>
    <div style="height:5px;"> </div>
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="kfglList" width="100%" type="single" pageNum="10" editable="0" printFileName="开复工令管理">
		<thead>
			<tr>
				<th  name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
				<th fieldname="XMBH" rowspan="2" colindex=2 hasLink="true" linkFunction="rowView" rowMerge="true">&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" maxlength="20" rowspan="2" colindex=3 rowMerge="true">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDBH" rowspan="2" colindex=4 CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" maxlength="20" rowspan="2" colindex=5 CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
				<th fieldname="XMBDDZ" maxlength="20" rowspan="2" colindex=6 >&nbsp;项目地址&nbsp;</th>
				<th fieldname="XMLX" rowspan="2" colindex=7 tdalign="center">&nbsp;项目类型&nbsp;</th>
				<th colspan="2">开工</th>
				<th colspan="2">复工</th>
				<th colspan="2">停工</th>
			</tr>
			<tr>
				<th fieldname="M_KGSJ" colindex=8 tdalign="center">&nbsp;办理时间&nbsp;</th>
				<th fieldname="M_KGFJ" colindex=9 tdalign="center">&nbsp;附件&nbsp;</th>
				<th fieldname="M_FGSJ" colindex=10 tdalign="center">&nbsp;办理时间&nbsp;</th>
				<th fieldname="M_FGFJ" colindex=11 tdalign="center">&nbsp;附件&nbsp;</th>
				<th fieldname="M_TGSJ" colindex=12 tdalign="center">&nbsp;办理时间&nbsp;</th>
				<th fieldname="M_TGFJ" colindex=13 tdalign="center">&nbsp;附件&nbsp;</th>
			</tr>
		</thead>
		<tbody></tbody>
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
       <input type="hidden" name="txtFilter"  order="asc" fieldname = "t.xmbh,t1.xmbs,t1.pxh"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
       <input type="hidden" name="ywid" id = "ywid" value="">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>