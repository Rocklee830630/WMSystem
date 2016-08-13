
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
	
	String htid = request.getParameter("htid");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/ZhaotoubiaoController.do";
var bmht = '<%=bmht %>';
var bmid = '<%=deptId %>';
var htid = '<%=htid %>';
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
/*         var rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
		  var fuyemian=$(window).manhuaDialog.getParentObj();
		  fuyemian.selectZtb(rowValue);
        $(window).manhuaDialog.close(); */
        
        var rowValue = $("#DT1").getSelectedRow();
    	var tempJson = convertJson.string2json1(rowValue);
    	var zbsjid = tempJson.GC_ZTB_SJ_ID;
        $.ajax({
    		url: "${pageContext.request.contextPath }/htgl/gcHtglHtController.do?bcZtbxx&htid="+htid+"&zbsjid="+zbsjid,
    		data:"",
    		dataType:"json",
    		async:false,
    		success:function(result) {
    			var jsonObj = convertJson.string2json1(result);
    		//	alert(jsonObj.msg +"|"+jsonObj.error);
    			if(jsonObj.error=="1") {
    				xInfoMsg(jsonObj.msg,"");
    			} else {
    				var fuyemian=$(window).manhuaDialog.getParentObj();
    				fuyemian.tishi(jsonObj.msg);
    				$(window).manhuaDialog.close();
    			}
    		}
    	});
        
    });
});
//页面默认参数
function init(){
    if(bmht=="true"){
        $("#BMID").val(bmid);
    }
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?queryZtbsptg&bmht="+bmht,data,DT1,null,true);
}
//查询列表
function queryList(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?queryZtbsptg&bmht="+bmht,data,DT1,null,false);
}


//列表添加信息卡图标
function doRandering(obj){
	var showHtml = "";
	showHtml = "<a href='javascript:void(0)' onclick='openInfoCard()' title='招投标信息卡'><i class='icon-file showXmxxkInfo'></i></a>";
	return showHtml;
}
//-------------------------------
//-打开信息卡页面
//-------------------------------
function openInfoCard(){
	var index = $(event.target).closest("tr").index();
	$("#DT1").cancleSelected();
	$("#DT1").setSelect(index);
	var json = convertJson.string2json1($("#DT1").getSelectedRow());
	var id = json.GC_ZTB_SJ_ID;
	var zblx = json.ZBLX;
	$(window).manhuaDialog({"title":"招投标管理>查看招标信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/ztbgl_xx.jsp?xx="+id+"&zblx="+zblx+"&cxlx=2","modal":"1"});
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">招投标信息
      	<span class="pull-right">
      		<button id="btnQd" class="btn" type="button">确定</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      <TR  style="display:none;">
	        <TD>
				<INPUT id="num" type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<=" keep="true"/>
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
			<th width="5%" class="right-border bottom-border text-right">年度</th>
			 <td width="10%" class="right-border bottom-border">
	            <select class="span12 year" id="ND" name="QueryND" kind="dic" src="T#GC_ZTB_SJ:distinct to_char(KBRQ,'YYYY') CODE:to_char(KBRQ,'YYYY') VALUE:SFYX='1' and KBRQ is not null order by to_char(KBRQ,'YYYY') asc"
					fieldname="to_char(a.KBRQ,'yyyy')" operation="=" defaultMemo="全部">
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
			<th width="5%" class="right-border bottom-border text-right">招投标名称</th>
			<td width="20%" class="right-border bottom-border">
				<input class="span12" type="text" id="ZTBMC" name="ZTBMC" fieldname="a.ZTBMC" operation="like" maxlength="100">
				<input class="span12" type="hidden" id="BMID" keep="true" name="BMID" fieldname="gzq.LRBM" operation="=" maxlength="100">
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
							<th fieldname="SFYX" tdalign="center" CustomFunction="doRandering" noprint="true"> &nbsp;&nbsp; </th>
							<th fieldname="ZTBMC"  tdalign="" maxlength="15">&nbsp;招投标名称&nbsp;</th>
							<th fieldname="ZBLX"  tdalign="" maxlength="15">&nbsp;招标类型&nbsp;</th>
							<th fieldname="ZBFS"  tdalign="" maxlength="15">&nbsp;招标方式&nbsp;</th>
							<th fieldname="BJXS"  tdalign="" maxlength="15">&nbsp;报价系数&nbsp;</th>
							<th fieldname="ZZBJ"  tdalign="" maxlength="15">&nbsp;总中标价&nbsp;</th>
							<th fieldname="DSFJGID"  tdalign="" maxlength="15">&nbsp;中标单位&nbsp;</th>
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
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>