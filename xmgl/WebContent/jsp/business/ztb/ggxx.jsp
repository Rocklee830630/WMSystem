<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script type="text/javascript" charset="utf-8">
	var controllername = "${pageContext.request.contextPath }/jieSuanGuanliController.do";

	$(function() {
		
	});
</script>   
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
  <h4 class="title">公告信息
  	  <span class="pull-right">
		  <button id="save" class="btn"  type="button">保存</button>
      </span>  
  </h4>
     	<form method="post" id="demoForm"  >
      		<table class="B-table" width="100%"  >
	      		<TR  style="display:none;">
				    <TD>
						<input class="span12"  id="JHSJID"  type="text"  name = "JHSJID" fieldname="JHSJID">
						<input class="span12"  id="JHID"  type="text"  name = "JHID" fieldname="JHID">
				    </TD>
	            </TR>
          		<tr>
          			<th width="8%" class="right-border bottom-border text-right">公告发布媒体</th>
          			<td width="92%" colspan="3" class="right-border bottom-border text-right" >
          				<input class="span12" type="text" id="GGFBMT" name="GGFBMT" fieldname="GGFBMT">
          			</td>
          		</tr>
        	 	<tr>
          			<th width="8%" class="right-border bottom-border text-right">公告发布起始日期</th>
          			<td width="42%" class="right-border bottom-border text-right">
          				<input class="span12" type="date" id="GGFBQSRQ" name="GGFBQSRQ" fieldname="GGFBQSRQ">
          			</td>
          			<th width="8%" class="right-border bottom-border text-right">公告发布结束日期</th>
          			<td width="42%" class="right-border bottom-border text-right" >
          				<input class="span12" type="date" id="GGFBJSRQ" name="GGFBJSRQ" fieldname="GGFBJSRQ">
          			</td>
          		</tr>
          		<tr>
          			<!-- <td width="8%" class="right-border bottom-border" ></td> -->
          			<td width="50%" class="right-border bottom-border" colspan="2">
            			<label class="checkbox">发布公告时间满足5个工作日 
            				<input type="checkbox" >
            			</label>
          			</td>
          			<!-- <td width="8%" class="right-border bottom-border" ></td> -->
          			<td width="50%" class="right-border bottom-border" colspan="2">
            			<label class="checkbox">发售招标文件至开标满足20天 
            				<input type="checkbox" >
            			</label>
          			</td>
          		</tr>
          		<tr>
          			<!-- <td width="8%" class="right-border bottom-border" ></td> -->
          			<td width="50%" class="right-border bottom-border" colspan="2">
            			<label class="checkbox">报名单位大于3家 
            				<input type="checkbox" >
            			</label>
          			</td>
          			<td width="50%" class=" bottom-border" colspan="2">
          		</tr>
        	</table>
      	</form>
     	</div>
 	</div>
</div>
 <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "XMNF"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
         <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 </div>
  <script>

</script>
</body>
</html>