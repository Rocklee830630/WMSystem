
<%@page import="com.ccthanking.framework.common.*"%>
<%@page import="com.ccthanking.framework.Globals"%><!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>选择参建单位</title>
<%
	String bmht = request.getParameter("bmht");
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName= dept.getDept_Name();
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtController.do";
var bmht = '<%=bmht %>';
var bmid = '<%=deptId %>';
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
    	if($("#DT1").getSelectedRowIndex()==-1)
		 {
			xInfoMsg("请选择一条记录","");
		    return
		 }
        var rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
/*         $(window).manhuaDialog.setData(rowValue);
        $(window).manhuaDialog.sendData(); */
		  var fuyemian=$(window).manhuaDialog.getParentObj();
		  fuyemian.selectLybzj(rowValue);
        $(window).manhuaDialog.close();
    });
});
var ztbId; 
//页面默认参数
function init(){
	var fuyemian=$(window).manhuaDialog.getParentObj();
	ztbId = fuyemian.ztbId;
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?queryLybzjList&ztbId="+ztbId,data,DT1,null,true);
}
//查询列表
function queryList(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?queryLybzjList&ztbId="+ztbId,data,DT1,null,false);
}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">履约保证金信息
      	<span class="pull-right">
      		<button id="btnQd" class="btn" type="button">确定</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
        <tr>
			<th width="5%" class="right-border bottom-border text-right">年度</th>
			 <td width="10%" class="right-border bottom-border">
	              <select class="span12" id="ND" name = "ND" fieldname="to_char(v.jnrq,'yyyy')"  defaultMemo="-全部-" operation="="  kind="dic" src="T#GC_ZJGL_LYBZJ:to_char(jnrq,'yyyy') :to_char(jnrq,'yyyy'):SFYX='1' group by to_char(jnrq,'yyyy') order by to_char(jnrq,'yyyy') ">
	              </select>
	          </td>
			<!-- <th width="5%" class="right-border bottom-border text-right">招标类型</th>
			<td width="10%" class=" right-border bottom-border">
	          <select class="span12" id="ZBLX" name = "ZBLX" fieldname="c.ZBLX" kind="dic" src="ZBLX" operation="=">
			 </select>
		      </td> 
			<th width="5%" class="right-border bottom-border text-right">工作名称</th>
			<td width="20%" class="right-border bottom-border">
				<input class="span12" type="text" id="GZMC" name="GZMC" fieldname="c.GZMC" operation="like" maxlength="100">
			</td> -->
			<th width="5%" class="right-border bottom-border text-right">交纳单位</th>
			<td width="20%" class="right-border bottom-border">
				<input class="span12" type="text" id="JNDW" name="JNDW" fieldname="v2.dwmc" operation="like" maxlength="100">
			</td>
               
       		<td width="30%" class=" bottom-border text-right">
       			<button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
       			<button id="btnClear" class="btn btn-link"  type="button"><i class="icon-trash"></i>清空</button>
       		</td>
        </tr>
      </table>
      </form>
	<div style="height:5px;"> </div>
        <div class= "overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" editable="0">
                <thead>
                    <tr>	
                    	<th name="XH" id="_XH"  tdalign="">&nbsp;#&nbsp;</th>
						<th fieldname="XMMC"  tdalign="left" maxlength="15">&nbsp;项目名称&nbsp;</th>
						<th fieldname="BDMC"  tdalign="left" maxlength="15">&nbsp;标段名称&nbsp;</th>
						<th fieldname="JNFS"  tdalign="center" maxlength="15">&nbsp;交纳方式&nbsp;</th>
						<th fieldname="JNRQ"  tdalign="right" maxlength="15">&nbsp;交纳日期&nbsp;</th>
						<th fieldname="JE"  tdalign="right" maxlength="15">&nbsp;金额&nbsp;</th>
						<th fieldname="DWMC"  tdalign="left" maxlength="15">&nbsp;交纳单位&nbsp;</th>
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
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "v.LRSJ"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>