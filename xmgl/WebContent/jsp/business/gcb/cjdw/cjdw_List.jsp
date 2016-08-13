<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>工程部-参建单位</title>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/cjdw/cjdwController.do";

function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#cjdwList").attr("pageNum",pageNum);
}


//页面初始化
$(function() {
	
	setPageHeight();
	
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件(新增)
	$("#btnInsert").click(function() {
		$(window).manhuaDialog({"title":"参建单位>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/cjdw/cjdw_insert.jsp","modal":"4"});
	});
	//按钮绑定事件(修改)
	$("#btnUpdate").click(function() {
		if($("#cjdwList").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		$("#resultXML").val($("#cjdwList").getSelectedRow());
		$(window).manhuaDialog({"title":"参建单位>修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/cjdw/cjdw_update.jsp","modal":"4"});
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        //其他处理放在下面
    });
	
});
//页面默认参数
function init(){
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}
//查询列表
function queryList(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,cjdwList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,cjdwList);
}
//回调函数
getWinData = function(data){
	var data1 = defaultJson.packSaveJson(data);
	if(convertJson.string2json1(data).GC_CJDW_ID == "" || convertJson.string2json1(data).GC_CJDW_ID == null){
		defaultJson.doInsertJson(controllername + "?insert", data1,cjdwList);
		$("#cjdwList").setSelect(0);
	}else{
		defaultJson.doUpdateJson(controllername + "?insert", data1,cjdwList);
	}
};

//<中标次数>样式
function doZbcs(obj){
	if(obj.ZBCS == 0){
		return '0';
	}else{
		var id = obj.GC_CJDW_ID;
		return '<a href="javascript:void(0);" onclick="queryZtbList(\''+id+'\')">'+obj.ZBCS+'</a>';
	}
}
//涉及项目
function doSjxm(obj){
	if(obj.ZS == 0){
		return '0';
	}else{
		var id = obj.GC_CJDW_ID;
		return '<a href="javascript:void(0);" onclick="queryXMList(\''+id+'\')">'+obj.ZS+'</a>';
	}
}
//<涉及项目>连接方法
function queryXMList(id){
	$(window).manhuaDialog({"title":"参建单位> 涉及项目","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/cjdw/cjdw_tcjh_list.jsp?id="+id,"modal":"2"});
}
//<中标次数>连接方法
function queryZtbList(id){
	$(window).manhuaDialog({"title":"招投标合同部>中标信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/cjdw/ztb_List.jsp?id="+id,"modal":"2"});
}
//删除回调
function delhang()
{
	  var rowindex = $("#cjdwList").getSelectedRowIndex();
	  $("#cjdwList").removeResult(rowindex);
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">参建单位
      	<span class="pull-right">
      		<app:oPerm url="jsp/business/gcb/cjdw/cjdw_insert.jsp">
      		<button id="btnInsert" class="btn" type="button">新增</button>
      		</app:oPerm>
      		<app:oPerm url="jsp/business/gcb/cjdw/cjdw_update.jsp">
      		<button id="btnUpdate" class="btn" type="button">修改</button>
      		</app:oPerm>
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
          <th width="5%" class="right-border bottom-border text-right">单位名称</th>
          <td class="right-border bottom-border" width="20%">
            <input type="text" class="span12" name = "QDWMC" fieldname = "DWMC" operation="like" />
          </td>
          <th width="5%" class="right-border bottom-border text-right">单位类型</th>
          <td class="right-border bottom-border" width="10%">
           <select class="span12" defaultMemo="全部" name = "QDWLX" fieldname = "DWLX" operation="=" kind="dic" src="JGLB">
           </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">企业性质</th>
          <td class="right-border bottom-border" width="10%">
           <select class="span12" defaultMemo="全部" name = "QQYXZ" fieldname = "QYXZ" operation="=" kind="dic" src="QYXZ">
           </select>
          </td>
          <td class="text-left bottom-border text-right">
           <button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
           <button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
          </td>
         </tr>
      </table>
      </form>
   <div style="height:5px;"> </div>
	<table class="table-hover table-activeTd B-table" id="cjdwList" width="100%" type="single" pageNum="10" printFileName="参建单位">
		<thead>
			<tr>
				<th  name="XH" id="_XH">&nbsp;#&nbsp;</th>
				<th fieldname="DWBH" tdalign="center">&nbsp;单位编号&nbsp;</th>
				<th fieldname="DWMC">&nbsp;单位名称&nbsp;</th>
				<th fieldname="DWLX" tdalign="center">&nbsp;单位类型&nbsp;</th>
				<th fieldname="ZBCS" tdalign="center" CustomFunction="doZbcs" style="width:5%">&nbsp;中标次数&nbsp;</th>
				<th fieldname="ZS" tdalign="center" CustomFunction="doSjxm" style="width:5%">&nbsp;涉及项目&nbsp;</th>
				<th fieldname="QYXZ" tdalign="center">&nbsp;企业性质&nbsp;</th>
				<th fieldname="FZR" tdalign="center">&nbsp;负责人&nbsp;</th>
				<th fieldname="DH">&nbsp;电话&nbsp;</th>
				<th fieldname="CLSJ" tdalign="center">&nbsp;成立时间（年）&nbsp;</th>
			</tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
</div>
</div>
    
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>