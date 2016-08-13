<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>下达计划</title>
<%
	String flag=request.getParameter("flag");
	String nd = request.getParameter("nd");
	if(Pub.empty(nd)){
		nd = "";
	}
	Date d=new Date();//获取时间
	SimpleDateFormat n=new SimpleDateFormat("yyyy");//转换格式
	String year=n.format(d);
	int year_num=Integer.parseInt(year);
%>
<script type="text/javascript" charset="utf-8">

//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xmcbk/xmcbkxdController.do";
var nd = '<%=nd%>';

//页面初始化
$(function() {
	setNd()
	init();
	//保存
	 $("#btnSave").click(function() {	
		if(!$("#jhForm").validationButton()){
			return;
		}
		var formData = Form2Json.formToJSON(jhForm);
		var formData1 = defaultJson.packSaveJson(formData);
		if('<%=flag%>'==1){
			success=defaultJson.doInsertJson(controllername+"?insert_sp_info",formData1,null,"insert_close");		
		}
		else{
			success=defaultJson.doUpdateJson(controllername+"?update_sp_info",formData1,null,"update_close");
		}	
	}); 	
});


//插入回调函数
function insert_close()
{
	if(success==true)
	{	

		var obj=$("#resultXML").val();	
		var parentmain=$(window).manhuaDialog.getParentObj();			
		parentmain.query_add(obj);
	}	
	$(window).manhuaDialog.close();
}


//更新回调函数
function update_close()
{
	if(success==true)
	{	
		var obj=$("#resultXML").val();	
		var parentmain=$(window).manhuaDialog.getParentObj();			
		parentmain.query_update(obj);
	}	
	$(window).manhuaDialog.close();
}


//页面默认参数
function init(){
	if(<%=flag%>==2){
		var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
		var odd=convertJson.string2json1(rowValue);
		$("#jhForm").setFormValues(odd);
	}	
}

//初始化年度下拉列表，并指定默认值
function setNd(){
	var year=new Date().getFullYear();
	var num=document.getElementsByTagName("option");
	var year_num=year-2004;
	document.getElementById('ND').options.length=year_num+1;
	for(var i=0;i<year_num+1;i++)
	{
		num[i].id=i;
		num[i].innerHTML=parseInt(2005)+i;
	}
	document.getElementById('ND').value=nd;
}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	 <div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">
			<h4 class="title">
				<span class="pull-right">
					<button id="btnSave" class="btn" type="button">保存</button>
				</span>
			</h4>
		    <form method="post" id="jhForm">
		    	<input type="hidden" id="GC_TCJH_SP_ID" fieldname="GC_TCJH_SP_ID">
		    	<input type="hidden" id="SPLX" fieldname="SPLX" value="1">
				<table class="B-table">
					<th width="10%" class="right-border bottom-border text-right">审批名称</th>
					<td width="70%" class="right-border bottom-border">
						<input id="SPMC" class="span12" type="text" placeholder="必填" check-type="required maxlength" name="SPMC"  maxlength="100" fieldname="SPMC"/>
					</td>					
					<th width="8%" class="right-border bottom-border text-right">审批年度</th>
					<td width="12%" class="right-border bottom-border">
						<select class="span12 year" type="text" id="ND" placeholder="必填" check-type="required" keep="true"  fieldname="ND" name="ND"></select>
					</td>	
	      			<tr>
	         			<th width="10%" class="right-border bottom-border text-right">备注</th>
	         			<td class=" bottom-border text-left" colspan="3">
	         				<textarea class="span12" id="BZ" rows="5" name ="BZ"  fieldname="BZ" maxlength="4000"></textarea>
	        		    </td>
						
	        		</tr>	     
   				</table>
   			</form>  			
	 	</div>
    </div>
</div>
<%-- <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/> --%>
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "PCH,XMBH"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>