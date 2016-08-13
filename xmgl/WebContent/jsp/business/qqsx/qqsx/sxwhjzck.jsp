<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<app:base />
<title></title>
<%
	String dfl=request.getParameter("dfl"); 
%>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/qianQiShouXuController.do";
  var xmmc;
  var jhsjid;
  var dfl="<%=dfl%>";
	$(function() {
		
			var pwindow =$(window).manhuaDialog.getParentObj();
			var rowValue=pwindow.getValue();
	    	var odd = convertJson.string2json1(rowValue);
		    var xmmc=$(odd).attr("XMMC");
		    var bdmc=$(odd).attr("BDMC");
		   $("#SJWYBH").val($(odd).attr("SJWYBH"));
		    var xm=document.getElementsByTagName("font");
			if(bdmc=="null"||bdmc==""){
				xm[0].innerHTML = "项目名称："+xmmc;
			}else{
				xm[0].innerHTML = "项目名称："+xmmc+"，标段名称："+bdmc;	
			}
			ready();
	});
    //页面初始化查询 清空表单 获取行数 行数导致反馈按钮是否显示
    function ready() {
      	 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
   		 //调用ajax插入
   		 defaultJson.doQueryJsonList(controllername+"?querySxfjzj&dfl="+dfl,data,DT1,null,true);
      };

    function tr_click(obj){
    	a=$(obj).attr("GC_QQSX_SXFJ_ID");
    	var fjlx=$(obj).attr("FJLX");
    	$("#sxfj1").attr("fjlb",fjlx);
		setFileData(jhsjid,a,obj.SJBH,obj.YWLX);
    	queryFileData("",a,"","");
	}

</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
 <div class="row-fluid">
      <h4 class="title">手续信息&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      	  <font class="title" style="color:gray;">&nbsp;</font><font class="title" style="color:red;">&nbsp;</font>
      </h4>
    <div class="B-small-from-table-autoConcise">
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display: none">
      	<!-- <TR> -->
	        <TD><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="10000" operation="<=" keep="true"></TD>
			<TD> <INPUT type="text" class="span12" kind="text"  fieldname="SJWYBH" id="SJWYBH"  operation="=" keep="true">
			</TD>
        </TR>
      </table>
      </form>
    <div style="height:5px;"></div>
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" pageNum="5">
		<thead>
			<tr>
			<th name="XH" id="_XH" width="2%">&nbsp;#&nbsp;</th>
            <th fieldname="BLSJ" tdalign="center" width="10%">&nbsp;反馈时间&nbsp;</th>
            <th fieldname="BLR" width="10%">&nbsp;反馈人&nbsp;</th>
            <th fieldname="WHMC" maxlength="15" width="15%">&nbsp;文号名称&nbsp;</th>
            <th fieldname="FJLX" width="15%">&nbsp;手续类型&nbsp;</th>
           <!--  <th  fieldname="SXFJ" maxlength="30">&nbsp;手续附件&nbsp;</th> -->
            <th fieldname="CZWT" maxlength="30">&nbsp;存在问题&nbsp;</th>
			</tr>
		</thead>
	<tbody></tbody>
	</table>
	</div>
</div>
		<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">附件信息</h4>
			<td width="92%" colspan="4" class="bottom-border">
				<div>
					<table role="presentation" class="table table-striped">
						<tbody onlyView="true" fjlb="" id="sxfj1" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
					</table>
				</div>
			</td>
			</div>
	</div>
  </div>
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="resultXML" id = "resultXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
 	</FORM>
 </div>
</body>
</html>