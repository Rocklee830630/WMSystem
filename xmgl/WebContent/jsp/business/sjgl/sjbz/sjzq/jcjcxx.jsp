<!DOCTYPE HTML>
<html lang="en">
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat"%>
<app:base />
<title></title>
<%
	String jhsjid=request.getParameter("jhsjid");
	String lxlb=request.getParameter("lb");
%>		
</head>
  <script>
	var controllername= "${pageContext.request.contextPath }/sjgl/jcjc/jcjcController.do";
	var jhsjid='<%=jhsjid%>';
	var lxlb='<%=lxlb%>';
	function ready() {
  		left_query();
   };
   function left_query(){
	   $("#Q_JHSJID").val(jhsjid);
	   $("#BGLB").val(lxlb);
		var data = combineQuery.getQueryCombineData(queryForm1,frmPost1,DT11);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername + "?queryJs",data,DT11,null,true);
   }
    $(function() 
     {
    	ready();
     });

    function setPageHeight(){
    	var getHeight=getDivStyleHeight();
    	var height = getHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
    	var pageNum = parseInt(height/pageTableOne,10);
    	$("#DT11").attr("pageNum",pageNum);
    }
    //行选
	function tr_click(obj,DT11)
	{
		$("#fj_b").attr("fjlb",$("#BGLB").val());
		var jhsjid=$(obj).attr("JHSJID");
    	var a=$(obj).attr("GC_SJ_BGSF_JS_ID");
    	deleteFileData(jhsjid,"","","");
    	
    	var sjbh=$(obj).attr("SJBH");
    	var ywlx=$(obj).attr("YWLX");
		setFileData(jhsjid,a,sjbh,ywlx,"0");
    	
    	queryFileData("",a,"","");
	}
</script>
<body>
 <app:dialogs/>
<div class="container-fluid">
 <!-- 第一页查询条件开始 -->
  <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
     	<h5 style="color:black;"><font class="title" style="color:red;">&nbsp;</font></h5>
    <form method="post" id="queryForm1"  >
      <table class="B-table" width="100%">
                               <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display: none;">
      	<!-- <TR> -->
			<TD><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" keep="true"></TD>
			<TD><INPUT type="text" class="span12" kind="text" id="BGLB" fieldname="BGLB"   operation="=" ></TD>
			<TD><INPUT type="text" class="span12" kind="text" name="JHSJID" id="Q_JHSJID" fieldname="JHSJID" operation="=" keep="true"></TD>
		</TR>	
                               <!--可以再此处加入hidden域作为过滤条件 -->
      </table>
      </form>
    <div style="height:5px;"></div>
	<div class="overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT11" type="single" pageNum="5">
                <thead>
                   <tr>
	                    <th name="XH" id="_XH" width="5%">&nbsp;#&nbsp;</th>
						<th fieldname="BGLB" tdalign="center" width="15%">&nbsp;检测类别&nbsp;</th>
						<th fieldname="JSRQ" tdalign="center" width="10%">&nbsp;接收日期&nbsp;</th>
						<th fieldname="FS" tdalign="right" width="15%">&nbsp;接收份数&nbsp;</th>
						<th fieldname="JSR" width="20%">&nbsp;接收人&nbsp;</th>
						<th fieldname="BZ" maxlength="25">&nbsp;信息说明&nbsp;</th>
                    </tr> 
                </thead>
             <tbody>
          </tbody>
      </table>
</div>

 <!-- 第一页信息维护开始 -->
</div>
		<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">附件信息</h4>
			<td width="92%" colspan="4" class="bottom-border">
				<div>
					<table role="presentation" class="table table-striped">
						<tbody onlyView="true"  id="fj_b" fjlb="" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
					</table>
				</div>
			</td>
			</div>
	</div>
</div>
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
<div align="center">
 	<FORM name="frmPost1" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "lrsj"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML1">
         <input type="hidden" name="queryResult" id ="queryResult">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
 	</FORM>
 </div>	
</body>
</html>