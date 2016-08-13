<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>选择参建单位</title>

<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/cjdw/cjdwController.do";

//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});

	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        //其他处理放在下面
    });
	
  	//按钮绑定事件（确定）
    $("#btnQd").click(function() {
    	if($("#cjdwList").getSelectedRowIndex()==-1)
		 {
			xInfoMsg("请选择一条记录","");
		    return
		 }
        var rowValue = $("#cjdwList").getSelectedRow();//获得选中行的json对象
        $(window).manhuaDialog.setData(rowValue);
        $(window).manhuaDialog.sendData();
        $(window).manhuaDialog.close();
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
	defaultJson.doQueryJsonList(controllername+"?query",data,cjdwList);
}
//<中标次数>样式
function doZbcs(obj){
	if(obj.ZBCS == 0){
		return '0';
	}else{
		var id = obj.GC_CJDW_ID;
		return '<a href="javascript:void(0);" onclick="queryZtbList(\''+id+'\')">'+obj.ZBCS+'</a>';
	}
}
//<中标次数>连接方法
function queryZtbList(id){
	$(window).manhuaDialog({"title":"招投标合同部>中标信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/cjdw/ztb_List.jsp?id="+id,"modal":"2"});
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
      		<button id="btnQd" class="btn" type="button">确定</button>
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
        	<th width="5%" class="right-border bottom-border text-right">单位类型</th>
          <td class="right-border bottom-border" width="17%">
           <select class="span12" defaultMemo="全部" name = "QDWLX" fieldname = "DWLX" operation="=" kind="dic" src="JGLB">
           </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">单位名称</th>
          <td class="right-border bottom-border" width="35%">
            <input type="text" class="span12" name = "QDWMC" fieldname = "DWMC" operation="like" />
          </td>
          
          <td class="text-left bottom-border text-right">
           <button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
           <button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
          </td>
         </tr>
      </table>
      </form>
   <div style="height:5px;"> </div>
	<table class="table-hover table-activeTd B-table" id="cjdwList" width="100%" type="single" noPage="true" pageNum="1000">
		<thead>
			<tr>
				<th  name="XH" id="_XH">&nbsp;#&nbsp;</th>
				<th fieldname="DWBH" tdalign="center">&nbsp;单位编号&nbsp;</th>
				<th fieldname="DWMC">&nbsp;单位名称&nbsp;</th>
				<th fieldname="DWLX" tdalign="center">&nbsp;单位类型&nbsp;</th>
				<th fieldname="QYXZ" tdalign="center">&nbsp;企业性质&nbsp;</th>
				<th fieldname="ZBCS" tdalign="center" CustomFunction="doZbcs" style="width:5%">&nbsp;中标次数&nbsp;</th>
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