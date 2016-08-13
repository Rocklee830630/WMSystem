<!DOCTYPE HTML>
<html lang="en">
  <head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base />
<app:dialogs/>
<%
	String jhsjid=request.getParameter("jhsjid");
	String tzlb=request.getParameter("tzlb");
%>		
</head>
  <script>
	var controllername= "${pageContext.request.contextPath }/ziLiaoShouFaController.do";
    $(function() 
     {
    	setValue();
    });
    //页面赋值
    function setValue(){
    	var tzlb='<%=tzlb%>';
    	var jhsjid='<%=jhsjid%>';
    	$("#TZLB").val(tzlb);
    	/* $("#JHSJID").val(jhsjid); */
    	defQuery();
    }
    //默认查询 
	function defQuery()
	{	
		var jhsjid='<%=jhsjid%>';
		setPageHeight();
		var data = combineQuery.getQueryCombineData(queryForm,frmPost1,DT11);
		defaultJson.doQueryJsonList(controllername + "?queryAllLList&JHSJID="+jhsjid,data,DT11,null,true);
	} 
	  //计算本页表格分页数
    function setPageHeight(){
    	var getHeight=getDivStyleHeight();
    	var height = getHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
    	var pageNum = parseInt(height/pageTableOne,10);
    	$("#DT1").attr("pageNum",pageNum);
    }
</script>
<body>
<div class="container-fluid">
<div class="row-fluid">
 <!-- 第一页查询条件开始 -->
 <div class="B-small-from-table-autoConcise">
  <p></p>
    <form method="post"  id="queryForm"  >
      <table class="B-table" width="100%">
      	<TR  style="display:none;">
      	<!-- <TR> -->
	        <TD><INPUT type="text" class="span12" kind="text" keep="true" fieldname="rownum"  value="1000" operation="<=" ></TD>
	        <TD><INPUT type="text" class="span12" kind="text" keep="true" id="TZLB" fieldname="a.TZLB" operation="=" ></TD>
	        <!-- <TD><INPUT type="text" class="span12" kind="text" keep="true" id="JHSJID" fieldname="JHSJID" operation="=" ></TD> -->
        </TR>
      </table>
      </form>
    <div style="height:5px;"></div>
    <div class="overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT11" type="single">
                <thead>
                   <tr>
	                    <th  name="XH" id="_XH">&nbsp;#&nbsp;</th>
						<!-- <th fieldname="TZLB" >&nbsp;资料类别&nbsp;</th> -->
						<th fieldname="SJY" maxlength="15">&nbsp;来源单位&nbsp;</th>
						<th fieldname="JSRQ" tdalign="center">&nbsp;接收日期&nbsp;</th>
						<th fieldname="FS" tdalign="right"  maxlength="3">&nbsp;接收份数&nbsp;</th>
						<th fieldname="JSR"  >&nbsp;接收人&nbsp;</th>
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
 	<FORM name="frmPost1" method="post" style="display:none" target="_blank" id="frmPost1">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
        <input type="hidden" name="txtFilter" order="desc" fieldname="XMBH,LRSJ"/>
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
 	</FORM>
 </div>			
</body>
</html>
