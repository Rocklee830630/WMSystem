<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>

</head>
<body>
<app:dialogs/>
<div class="container-fluid" >
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise" align="center" >
  <h4 class="title" align="center">长春市政府投资建设项目管理中心
  </h4>
  <h4  class="title" align="center">工作联系单</h4>
 <form method="post" action="${pageContext.request.contextPath }/insertdemo.xhtml" id="demoForm" >
      		<table  class="B-table" style="width:450pt;height:580pt;" >
	      			<TR  style="display:none;">
				        <TD class="right-border bottom-border">
				        </TD>
	                </TR>
	            <tr>
          			<th  width="8%" class=" bottom-border text-right ">主送</th>
          			<td  width="42%" class="right-border bottom-border text-right" ><span></span></td>
         			<th  width="8%" class="right-border bottom-border text-right">缓急程度</th>
          			<td  width="42%" class="right-border bottom-border "><span></span></td>
        		</tr>
      			<tr>
      				<th   class="right-border bottom-border text-right " >抄送</th>
          			<td    colspan="3"  class="right-border bottom-border "  ><span></span></td>
          		</tr>
          		<tr>
      				<th   class="right-border bottom-border text-right " >标题</th>
          			<td    colspan="3"  class="right-border bottom-border "  ><span></span></td>
          		</tr>
        		<tr>
          			<th  class="right-border bottom-border text-right" rowspan="2">主要内容与要求</th>
          			<th   class="right-border bottom-border text-right " >要求反馈时间</th>
          			<td    colspan="2"  class="right-border bottom-border "  ><input type="date"></td>
        		</tr>
        	    <tr><td  class="right-border bottom-border" colspan="3">主要内容与要求</td></tr>
        		</table>
      	  </form>
     	</div>
 	</div>
</div>
 <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		 <!--系统保留定义区域-->
		   <input type="hidden" name="ywid" id = "ywid" value="">
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "XMNF"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>