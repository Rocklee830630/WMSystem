<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%
	String bdid= request.getParameter("id");
%>
<app:base/>
<app:dialogs></app:dialogs>
<title>标段划分管理</title>
<script type="text/javascript" charset="utf-8">
 
var controllername= "${pageContext.request.contextPath }/bdhf/bdwhController.do";
var bdid = '<%=bdid%>';
//保存
$(function() {	
	init();
});
//页面初始化
function init(){
	setFormData(bdid);
}


//根据ID查询标段信息
function setFormData(id){
	var obj = "";
	var actionName=controllername+"?queryBdxxByBdid&id="+id;
	$.ajax({
		url : actionName,
		data : null,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(result) {
			//obj = result.msg;
			var rowObj = convertJson.string2json1(result.msg).response.data[0];
			$("#bdForm").setFormValues(rowObj);
		},
	    error : function(result) {
		}
	});
    return obj;
}



</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">	
			<h4 class="title">项目信息
			</h4>
    			<form method="post" id="bdForm">
     				<table class="B-table" width="100%">
     				<TR  style="display:none;">
     				    <td>
	     					<input type="text" id="GC_XMBD_ID" name="GC_XMBD_ID" fieldname="GC_XMBD_ID"/>
	     					<input type="text" id="JHSJID" name="JHSJID" fieldname="JHSJID"/>
					    </td>
					</TR>
					<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh" >项目编号</th>
						<td class="right-border bottom-border">
							<input class="span12" type="text" fieldname="XMBH" name = "XMBH" disabled>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh" disabledTh>项目名称</th>
						<td class="bottom-border right-border" colspan="3">
						  <input class="span12" type="text" fieldname="XMMC" name = "XMMC" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">项目年度</th>
						<td class="bottom-border right-border">
						  <input class="span12" type="text" fieldname="ND" name = "ND" readOnly/>
						</td>
			        </tr>
			        <tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">项目属性</th>
						<td class="right-border bottom-border">
						  	<input class="span12" type="text" fieldname="XMSX" name = "XMXS" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">项目类型</th>
					       <td class="bottom-border right-border">
					       	<input class="span12" type="text" fieldname="XMLX" name = "XMLX" readOnly/>
						</td>
			         	<th width="8%" class="right-border bottom-border text-right disabledTh">是否BT</th>
			         	<td class="right-border bottom-border">
				          	<input class="span12" type="text" fieldname="ISBT" name = "ISBT" readOnly/>
				        </td>
				        <th width="8%" class="right-border bottom-border text-right disabledTh">项目性质</th>
					       <td class="bottom-border right-border">
					       	<input class="span12" id="XJXJ" type="text" fieldname="XJXJ" name = "XJXJ" readOnly/>
						</td>
			        </tr>
			       </table>
			       <h4 class="title">标段信息</h4>
			       <table class="B-table" width="100%">
					<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">标段编号</th>
						<td class="right-border bottom-border">
							<input class="span12" type="text" id="BDBH" name="BDBH" fieldname="BDBH" readOnly/>	    
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
						<td colspan="3" class="right-border bottom-border">
							<input class="span12" type="text" id="BDMC" name="BDMC"  fieldname="BDMC" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">项目管理公司</th>
						<td class="bottom-border right-border">
							<input class="span12" type="text" id="XMGLGS" fieldname="XMGLGS" name="XMGLGS" readOnly />
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">起点（桩号）</th>
						<td width="18%" class="right-border bottom-border">
							<input id="QDZH" class="span12" type="text" name="QDZH" fieldname="QDZH" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">终点（桩号）</th>
						<td width="18%" class="right-border bottom-border">
							<input class="span12" name ="ZDZH" id="ZDZH" fieldname="ZDZH" type="text" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">起始点（桩号）</th>
						<td width="18%" colspan="3" class="right-border bottom-border">
							<input class="span12" type="text" name="BDDD" id="BDDD"  fieldname="BDDD" readOnly/>
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">建设规模</th>
						<td width="43%" colspan="3" class="right-border bottom-border">
							<input class="span12" type="text" id="JSGM" name="JSGM" fieldname="JSGM" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">长度</th>
						<td width="18%" class="right-border bottom-border">
							<input  class="span12" type="text" name="CD" id="CD"  fieldname="CD" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">宽度</th>
						<td width="18%" class="right-border bottom-border">
							<input class="span12" type="text" name="KD" id="KD"  fieldname="KD" readOnly/>
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">管径</th>
						<td width="18%" class="right-border bottom-border">
							<input class="span12" type="text" fieldname="GJ"name="GJ"  id="GJ" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">面积</th>
						<td width="18%" class="right-border bottom-border">
							<input class="span12" type="text" fieldname="MJ" name="MJ" id="MJ" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">工程主体费用</th>
						<td width="18%" class="right-border bottom-border">
							<input class="span12" type="text" fieldname="GCZTFY"  style="width:70%" name="GCZTFY" id="GCZTFY" readOnly/>&nbsp;&nbsp;<b>(万元)</b>
						</td>
						<td colspan="2"></td>
					</tr>
					<tr>
			      		<th width="8%" class="right-border bottom-border text-right disabledTh">设计单位</th>
						<td class="bottom-border right-border" colspan="3" width="42%">
							<input class="span12" type="text" id="SJDW" name="SJDW" fieldname="SJDW" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">设计负责人</th>
						<td class="bottom-border right-border" width="17%">
							<input class="span12" type="text" id="SJDWFZR" name="SJDWFZR" fieldname="SJDWFZR" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">联系方式</th>
						<td class="bottom-border right-border" width="17%">
							<input class="span12" type="text" id="SJDWFZRLXFS" name="SJDWFZRLXFS" fieldname="SJDWFZRLXFS" readOnly/>
						</td>
			      	</tr>
			      	<tr>
			      		<th width="8%" class="right-border bottom-border text-right disabledTh" rowspan="2">施工单位</th>
						<td class="bottom-border right-border" colspan="3" rowspan="2" width="45%">
							<input class="span12" type="text" id="SGDW" name="SGDW" fieldname="SGDW" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">项目经理</th>
						<td class="bottom-border right-border" width="17%">
							<input class="span12" type="text" id="SGDWXMJL" name="SGDWXMJL" fieldname="SGDWXMJL" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">联系方式</th>
						<td class="bottom-border right-border" width="17%">
							<input class="span12" type="text" id="SGDWXMJLLXDH" name="SGDWXMJLLXDH" fieldname="SGDWXMJLLXDH" readOnly/>
						</td>
			      	</tr>
			      	<tr>
			      		<th width="8%" class="right-border bottom-border text-right disabledTh">技术负责人</th>
						<td class="bottom-border right-border" width="17%">
							<input class="span12" type="text" id="SGDWJSFZR" name="SGDWJSFZR" fieldname="SGDWJSFZR" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">联系方式</th>
						<td class="bottom-border right-border" width="17%">
							<input class="span12" type="text" id="SGDWJSFZRLXDH" name="SGDWJSFZRLXDH" fieldname="SGDWJSFZRLXDH" readOnly/>
						</td>
			      	</tr>
			      	<tr>
			      		<th width="8%" class="right-border bottom-border text-right disabledTh">监理单位</th>
						<td class="bottom-border right-border" width="17%" colspan="3">
							<input class="span12" type="text" id="JLDW" name="JLDW" fieldname="JLDW" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">总监</th>
						<td class="bottom-border right-border" width="17%">
							<input class="span12" type="text" id="JLDWZJ" name="JLDWZJ" fieldname="JLDWZJ" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">联系方式</th>
						<td class="bottom-border right-border" width="17%">
							<input class="span12" type="text" id="JLDWZJLXDH" name="JLDWZJLXDH" fieldname="JLDWZJLXDH" readOnly/>
						</td>
			      	</tr>
			      	<tr>
			      		<th width="8%" class="right-border bottom-border text-right disabledTh">总监代表</th>
						<td class="bottom-border right-border" width="17%">
							<input class="span12" type="text" id="JLDWZJDB" name="JLDWZJDB" fieldname="JLDWZJDB" readOnly/>
						</td>
			      		<th width="8%" class="right-border bottom-border text-right disabledTh">联系方式</th>
						<td class="bottom-border right-border" width="17%">
							<input class="span12" type="text" id="JLDWZJDBLXDH" name="JLDWZJDBLXDH" fieldname="JLDWZJDBLXDH" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">安全监理</th>
						<td class="bottom-border right-border" width="17%">
							<input class="span12" type="text" id="JLDWAQJL" name="JLDWAQJL" fieldname="JLDWAQJL" readOnly/>
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">联系方式</th>
						<td class="bottom-border right-border" width="17%">
							<input class="span12" type="text" id="JLDWAQJLLXDH" name="JLDWAQJLLXDH" fieldname="JLDWAQJLLXDH" readOnly/>
						</td>
			      	</tr>
			      	<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">备注</th>
						<td width="92%" colspan="7" class="bottom-border">
							<textarea class="span12" rows="3" id="BZ" fieldname="BZ" name="BZ" disabled></textarea>
						</td>
					</tr>
   	 			</table>
     		</form>
	       
		</div>
	</div>		
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" id="px" fieldname="BDBH,LRSJ"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<input type="hidden" id="queryResult"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
	</FORM>
	
</div>
</body>
</html>