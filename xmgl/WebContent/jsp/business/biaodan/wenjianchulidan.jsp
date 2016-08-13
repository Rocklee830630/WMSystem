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
  <h4  class="title" align="center">文件处理单</h4>
 <form method="post" action="${pageContext.request.contextPath }/insertdemo.xhtml" id="demoForm" >
      		<table  class="B-table" style="width:450pt;height:580pt;" >
	      			<TR  style="display:none;">
				        <TD class="right-border bottom-border">
				        </TD>
	                </TR>
      			<tr>
      				<th   class="right-border bottom-border text-right " >文件标题</th>
          			<td    colspan="3"  class="right-border bottom-border "  ><span>关于排除因高架桥施工造成人防工事灌水带来隐患的函</span></td>
          		</tr>
        		<tr>
          			<th  width="8%" class=" bottom-border text-right ">收文号</th>
          			<td  width="42%" class="right-border bottom-border text-right" ><span></span></td>
         			<th  width="8%" class="right-border bottom-border text-right">收文日期</th>
          			<td  width="42%" class="right-border bottom-border "><input type="date"></td>
        		</tr>
        		<tr>
          			<th  class="right-border bottom-border text-right">来文单位</th>
          			<td   class="right-border bottom-border text-right" ><span></span></td>
         			<th   class="right-border bottom-border text-right">发文文号</th>
          			<td   class="right-border bottom-border text-right"><span></span></td>
        		</tr>
        		<tr>
          			<th  class="right-border bottom-border text-right">紧急程度</th>
          			<td   class="right-border bottom-border text-right" ><span></span></td>
         			<th   class="right-border bottom-border text-right">文件类型</th>
          			<td   class="right-border bottom-border text-right"><span></span></td>
        		</tr>
        		<tr>
          			<th  class="right-border bottom-border text-right">保密期限</th>
          			<td   class="right-border bottom-border text-right" ><span></span></td>
         			<th   class="right-border bottom-border text-right">文件密级</th>
          			<td   class="right-border bottom-border text-right"><span></span></td>
        		</tr>
        		<tr>
        		   <th  class="right-border bottom-border text-right">文件拟办意见</th>
          		   <td  colspan="3"  class="right-border bottom-border "  ><textarea rows="4" style="width: 97%"></textarea></td>
          		</tr>
          		<tr>
        		   <th  class="right-border bottom-border text-right">领导批示</th>
          		   <td  colspan="3"  class="right-border bottom-border "  ><textarea rows="4" style="width: 97%"></textarea></td>
          		</tr>
          		<tr>
        		   <th  class="right-border bottom-border text-right">承办部事意见</th>
          		   <td  colspan="3"  class="right-border bottom-border "  ><textarea rows="4" style="width: 97%"></textarea></td>
          		</tr>
          		<tr>
        		   <th  class="right-border bottom-border text-right">办理情况</th>
          		   <td  colspan="3"  class="right-border bottom-border "  ><textarea rows="4" style="width: 97%"></textarea></td>
          		</tr>
          		<tr>
        		   <th  class="right-border bottom-border text-right">阅件人意见</th>
          		   <td  colspan="3"  class="right-border bottom-border "  ><textarea rows="4" style="width: 97%"></textarea></td>
          		</tr> 
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