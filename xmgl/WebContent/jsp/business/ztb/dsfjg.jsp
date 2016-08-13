<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>工程计量管理-第三方机构</title>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/dsfjg/dsfjgController.do";

//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件(维护)
	$("#btnXmwh").click(function() {
		parent.$("body").manhuaDialog({"title":"第三方机构>维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/dsfjg/xmwh.jsp"});
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
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,dsfjgList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,dsfjgList);
}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">第三方机构
      	<span class="pull-right">
      		<button id="btnXmwh" class="btn" type="button" >确定</button>
      		<button id="btnXmwh" class="btn" type="button" >维护</button>
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
          <th width="5%" class="right-border bottom-border text-right">机构类别</th>
          <td class="right-border bottom-border" width="10%">
            <select class="span12" name = "QJGLB" fieldname = "JGLB" operation="=" kind="dic" src="JGLB">
            </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">机构名称</th>
          <td class="right-border bottom-border" width="20%">
           <input class="span12" type="text" name = "QJGMC" fieldname = "JGMC" operation="like" >
          </td>
          <td width="30%" class="text-left bottom-border text-right">
           <button id="btnQuery" class="btn btn-link"  type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-search"></i>查询</button>
           <button id="btnClear" class="btn btn-link" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-trash"></i>清空</button>
          </td>
         </tr>
      </table>
      </form>
   <div style="height:5px;"> </div>
	<table class="table-hover table-activeTd B-table" id="dsfjgList" width="100%" type="single" pageNum="10">
		<thead>
			<tr>
				<th  name="XH" id="_XH" style="width:10px">&nbsp;#&nbsp;</th>
				<th fieldname="JGLB" tdalign="center">&nbsp;机构类别&nbsp;</th>
				<th fieldname="JGMC">&nbsp;机构名称&nbsp;</th>
				<th fieldname="ZBCS" tdalign="right">&nbsp;中标次数&nbsp;</th>
				<th fieldname="LRSJ" tdalign="center">&nbsp;录入时间&nbsp;</th>
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