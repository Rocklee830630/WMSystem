<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<title>整改通知</title>
<%
	String jcbid=request.getParameter("jcbid");
	String zt=request.getParameter("zt");
	String flag_ty=request.getParameter("flag");
%>
<script type="text/javascript" charset="utf-8">
var controllername_jc= "${pageContext.request.contextPath }/zlaq/jcxxController.do";
var controllername_zg= "${pageContext.request.contextPath }/zlaq/zgxxController.do";
var jcbid="<%=jcbid%>";

//初始化加载
$(document).ready(function(){
	if(jcbid!=null&&jcbid!="null")
	{
		$("#ZJBS").attr("title",'');
		$("#AJBS").attr("title",'');
		var rowValue;
		if(<%=flag_ty%>==1)
		{
			rowValue = queryById(controllername_jc+"?queryById&id="+jcbid);
			//var rowValue = $(parent.parent.document).find("div[isopenwindow='true']").find("iframe").contents().find("#xmsc3").find("iframe").contents().find("#resultXML").val();
		}
		else
		{
			rowValue = queryById(controllername_jc+"?queryById&id="+jcbid);
			//var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#"+jcbid).val();
		}
		//var obj=convertJson.string2json1(decodeURI(rowValue));
		var obj = rowValue;
		if(obj.ZJBS==1)
		{
			$("#ZJBS").prop("checked",true);
		}
		if(obj.AJBS==1)
		{
			$("#AJBS").prop("checked",true);	
		}	
		deleteFileData(jcbid,"","","");
		setFileData(jcbid,obj.GC_JH_SJ_ID,obj.SJBH,obj.YWLX,"0");
		$("#demoForm").setFormValues(obj);
		$("#FCBH").val($("#JCBH").val()+'FC');
		queryFileData(jcbid,"","","");
	}	
});
//========================根据Id查询信息===========================
function queryById(actionName){
	var rowObj;
	$.ajax({
		url : actionName,
		dataType : 'json',
		async :	false,
		type : 'post',
		success : function(result) {
			rowObj = convertJson.string2json1(result.msg).response.data[0];
			$("#demoForm").setFormValues(rowObj);
		},  
	});
	 return rowObj;
}
</script>    
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">
      		<form method="post" id="demoForm">
      			<%int isshow=Integer.parseInt(zt);
      				switch(isshow)
      				{
      					case 0:
      			%>	
	     			<h4 id="xmxx" class="title">项目信息</h4>
	      			<table class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/xmxx_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="xmxx_readonly"/> 
				         </jsp:include>   			
					</table>
		      		<h4 id="zgxx" class="title">检查信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				    	<jsp:include page="/jsp/business/zlaq/jc_readonly.jsp" flush="true">
				        	<jsp:param name="prefix" value="jc_readonly"/> 
				        </jsp:include>
				    </table> 
      			<%		break;
      					case 1:
      			%>
		      		<h4 id="zgxx" class="title">整改信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/tz_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="tz_readonly"/> 
			         	</jsp:include>	
			        </table>		
 		      		<h4 id="zgxx" class="title">检查信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				    	<jsp:include page="/jsp/business/zlaq/jc_readonly.jsp" flush="true">
				        	<jsp:param name="prefix" value="jc_readonly"/> 
				        </jsp:include>
				    </table> 
	     			<h4 id="xmxx" class="title">项目信息</h4>
	      			<table class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/xmxx_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="xmxx_readonly"/> 
				         </jsp:include>   			
					</table>
      			<%		break;
      					case 2:
      			%>
		      		<h4 id="zgxx" class="title">整改信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/tz_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="tz_readonly"/> 
			         	</jsp:include>	
			        </table>		
 		      		<h4 id="zgxx" class="title">检查信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				    	<jsp:include page="/jsp/business/zlaq/jc_readonly.jsp" flush="true">
				        	<jsp:param name="prefix" value="jc_readonly"/> 
				        </jsp:include>
				    </table> 
	     			<h4 id="xmxx" class="title">项目信息</h4>
	      			<table class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/xmxx_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="xmxx_readonly"/> 
				         </jsp:include>   			
					</table>
      			<%		break;
      					case 3:
      			%>
		      		<h4 id="zgxx" class="title">整改信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/tz_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="tz_readonly"/> 
			         	</jsp:include>	
			        </table>		
 		      		<h4 id="zgxx" class="title">检查信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				    	<jsp:include page="/jsp/business/zlaq/jc_readonly.jsp" flush="true">
				        	<jsp:param name="prefix" value="jc_readonly"/> 
				        </jsp:include>
				    </table> 
	     			<h4 id="xmxx" class="title">项目信息</h4>
	      			<table class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/xmxx_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="xmxx_readonly"/> 
				         </jsp:include>   			
					</table>
      			<%		break;
      					case 4:
      			%>
		      		<h4 id="zgxx" class="title">回复信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/xmglgs/zlaqjc/hf_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="hf_readonly"/> 
				         </jsp:include>
			         </table>   			      				
		      		<h4 id="zgxx" class="title">整改信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/tz_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="tz"/> 
			         	</jsp:include>	
			        </table>		
 		      		<h4 id="zgxx" class="title">检查信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				    	<jsp:include page="/jsp/business/zlaq/jc_readonly.jsp" flush="true">
				        	<jsp:param name="prefix" value="jc_readonly"/> 
				        </jsp:include>
				    </table> 
	     			<h4 id="xmxx" class="title">项目信息</h4>
	      			<table class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/xmxx_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="xmxx_readonly"/> 
				         </jsp:include>   			
					</table>
      			<%		break;
      					case 5:
      			%>
 		      		<h4 id="zgxx" class="title">复查信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
	 					<jsp:include page="/jsp/business/zlaq/fc_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="fc_readonly"/> 
				         </jsp:include>
			         </table>   			      				
		      		<h4 id="zgxx" class="title">回复信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/xmglgs/zlaqjc/hf_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="hf_readonly"/> 
				         </jsp:include>
			         </table>   			      				
		      		<h4 id="zgxx" class="title">整改信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/tz_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="tz"/> 
			         	</jsp:include>	
			        </table>		
 		      		<h4 id="zgxx" class="title">检查信息</h4>
					<table id="zgbd" class="B-table c-table" width="100%">
				    	<jsp:include page="/jsp/business/zlaq/jc_readonly.jsp" flush="true">
				        	<jsp:param name="prefix" value="jc_readonly"/> 
				        </jsp:include>
				    </table> 
	     			<h4 id="xmxx" class="title">项目信息</h4>
	      			<table class="B-table c-table" width="100%">
				         <jsp:include page="/jsp/business/zlaq/xmxx_readonly.jsp" flush="true">
				         	<jsp:param name="prefix" value="xmxx_readonly"/> 
				         </jsp:include>   			
					</table>
     			<%} %>	
			</form>	
		</div>
	</div>
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>     	
<div align="center">
	<FORM name="frmPost" method="post" id="frmPost" style="display: none"
		target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id="queryXML"/>
		 <input type="hidden" name="txtXML" id="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" fieldname="XMNF" id="txtFilter"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
	</FORM>
</div>
</body>
</html>