<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>提请款明细-维护</title>
<%
	String type=request.getParameter("type");
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglTqkbmmxController.do";
var type ="<%=type%>";
//页面初始化
$(function() {
	init();
    
    <%
    if(type.equals("detail")){
	%>
	$("#btn_seleht").remove();
	
	//置所有input 为disabled
	$(":input").each(function(i){
	   $(this).attr("disabled", "true");
	 });
	<%
		}
	%>
	
});
//页面默认参数
function init(){
	
	var parentmain=$(window).manhuaDialog.getParentObj();	
	var rowValue = parentmain.$("#resultXML").val();
	var tempJson = convertJson.string2json1(rowValue);

	$("#QID").val(tempJson.ID);
		
		//查询记录数
		var data = combineQuery.getQueryCombineData(queryForm,frmPost);
		var data1 = {
			msg : data
		};
		$.ajax({
			url : controllername+"?query",
			data : data1,
			cache : false,
			async :	false,
			dataType : "json",  
			type : 'post',
			success : function(response) {
				$("#resultXML").val(response.msg);
				var resultobj = defaultJson.dealResultJson(response.msg);
				$("#gcZjglTqkmxForm").setFormValues(resultobj);
			}
		});
}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">

	<div class="B-small-from-table-autoConcise" style="display:none;">
			<h4 class="title">
				部门提请款明细
			</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border">
							<input class="span12" type="text" id="QID" name="ID"  fieldname="t.ID" value="" operation="=" >
						</TD>
					</TR>
				</table>
			</form>
	 	</div>

	<div class="B-small-from-table-autoConcise">
      <h4 class="title">部门提请款明细
      	<span class="pull-right">
      	</span>
      </h4>
     <form method="post" id="gcZjglTqkmxForm"  >
      <table class="B-table" width="100%" >
      	<input type="hidden" id="ID" fieldname="ID" name = "ID"/>
      	<input type="hidden" id="QTQKID" name="TQKID"  fieldname="TQKID" >
	  	<input type="hidden" id="QHTID" name="HTID"  fieldname="HTID" >
	  	<input type="hidden" id="QDWID" name="DWID"  fieldname="DWID" >
	  	<tr>
			<th width="8%" class="right-border bottom-border text-right">合同名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="HTMC" type="text" placeholder="必填" check-type="required" fieldname="HTMC" name = "HTMC"  disabled />
          		<button class="btn btn-link" id="btn_seleht"  type="button" onclick="selectHt()"><i class="icon-edit"></i></button>
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right">合同编码</th>
       		<td class="bottom-border right-border"width="10%">
         		<input class="span12" style="width:100%" id="HTBM" type="text" fieldname="HTBM" name = "HTBM"  disabled/>
         	</td>
        </tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">项目名称内容</th>
			<td class="bottom-border right-border" >
				<textarea class="span12" rows="2" id="XMMCNR" check-type="maxlength" maxlength="500" fieldname="XMMCNR" name="XMMCNR"></textarea>
			</td>
			<th width="8%" class="right-border bottom-border text-right">单位全称</th>
			<td width="17%" class="right-border bottom-border">
				<input id="DWMC"   placeholder="必填" check-type="required" class="span12" check-type="maxlength" maxlength="200"  name="DWMC" fieldname="DWMC" type="text" />
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">合同价</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZXHTJ" value=0 class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="ZXHTJ" fieldname="ZXHTJ" type="number" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right">已拔付</th>
			<td width="17%" class="right-border bottom-border">
				<input id="YBF" value=0 class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="YBF" fieldname="YBF" type="number" disabled/>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">累计拔付</th>
			<td width="17%" class="right-border bottom-border">
				<input id="LJBF" value=0 class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="LJBF" fieldname="LJBF" type="number" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right">按合同付款比例</th>
			<td width="17%" class="right-border bottom-border">
				<input id="AHTBFB" value=0 class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="AHTBFB" fieldname="AHTBFB" type="number" disabled/>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">本次申请拔款</th>
			<td colspan="3" width="17%" class="right-border bottom-border">
				<input id="BCSQ" value=0  onchange="calPercen(this);"  class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="BCSQ" fieldname="BCSQ" type="number" />
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">财审值</th>
			<td width="17%" class="right-border bottom-border">
				<input id="CSZ" value=0 class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="CSZ" fieldname="CSZ" type="number" />
			</td>
			<th width="8%" class="right-border bottom-border text-right">监理确认计量款</th>
			<td width="17%" class="right-border bottom-border">
				<input id="JZQR" value=0 class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="JZQR" fieldname="JZQR" type="number" />
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">按计量付款比例</th>
			<td colspan="3" width="17%" class="right-border bottom-border">
				<input id="AJLFKB" value=0 class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="AJLFKB" fieldname="AJLFKB" type="number" />
			</td>
		</tr>
		
        <tr>
	        <th width="8%" class="right-border bottom-border text-right">备注</th>
	        <td colspan="3" class="bottom-border right-border" >
	        	<textarea class="span12" rows="2" id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ"></textarea>
	        </td>
        </tr>
      </table>
      </form>
    </div>
   </div>
  </div>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "t.LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
<script>
</script>
</html>