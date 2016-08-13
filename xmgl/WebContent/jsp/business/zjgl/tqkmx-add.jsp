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
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglTqkmxController.do";
var type ="<%=type%>";
//页面初始化
$(function() {
	init();
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#gcZjglTqkmxForm").validationButton())
		{
		    //生成json串
		    var data = Form2Json.formToJSON(gcZjglTqkmxForm);
		  //组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  //调用ajax插入
		    if($("#ID").val() == "" || $("#ID").val() == null){
    			var success=defaultJson.doInsertJson(controllername + "?insert", data1);
		    	if(success==true)
				{
					var obj=$("#resultXML").val();
					var subresultmsgobj1=defaultJson.dealResultJson(obj);
					$("#gcZjglTqkmxForm").setFormValues(subresultmsgobj1);
					
					var parentmain=$(this).manhuaDialog.getParentObj();	
					parentmain.$("#btnQuery").click();
				}
    			
    		}else{
    			defaultJson.doUpdateJson(controllername + "?update", data1);
    		}
		}else{
			requireFormMsg();
		  	return;
		}
	});
	
	//按钮绑定事件（新增）
    $("#btnClear_Bins").click(function() {
        $("#gcZjglTqkmxForm").clearFormResult();
        
        var parentIObj = $(this).manhuaDialog.getParentObj();
    	var tempJson = parentIObj.$("#ID").val();
    	if(tempJson!=""){
    		$("#QTQKID").val(tempJson);
    	}
        
        $("#ID").val("");
    });
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
    
    <%
    if(type.equals("detail")){
	%>
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
	var parentIObj = $(this).manhuaDialog.getParentObj();
	if(type == "insert"){
		var tempJson = parentIObj.$("#ID").val();
		if(tempJson!=""){
			$("#QTQKID").val(tempJson);
		}
	}else if(type == "update" || type == "detail"){
		var tempJson = parentIObj.$("#QMXID").val();
		if(tempJson!=""){
			$("#QID").val(tempJson);
		}
		
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
}


//点击行事件
function tr_click(obj){
	//alert(JSON.stringify(obj));
	$("#gcZjglTqkmxForm").setFormValues(obj);
}

//选中项目名称弹出页
function selectHt(){
	$(window).manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath}/jsp/business/htgl/htcx.jsp","modal":"2"});
}
//弹出区域回调
getWinData = function(data){
	var parseObj = JSON.parse(data);
	$("#HTMC").val(parseObj.HTMC);
	$("#XMMCNR").val(parseObj.XMMC);
	$("#HTBM").val(parseObj.HTBM);
	$("#QHTID").val(parseObj.HTSJID);
	$("#DWMC").val(parseObj.YFDW);
	$("#ZXHTJ").val(parseObj.ZHTQDJ);
	
	
	//$("#YBF").val(parseObj.ZHTZF);
	
	
};

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">

	<div class="B-small-from-table-autoConcise" style="display:none;">
			<h4 class="title">
				提请款明细
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
      <h4 class="title">提请款明细
      	<span class="pull-right">
      		<button id="btnClear_Bins" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
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
          		<button class="btn btn-link"  type="button" onclick="selectHt()" disabled><i class="icon-edit"></i></button>
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
			<th width="8%" class="right-border bottom-border text-right">部门本次申请拔款</th>
			<td colspan="3" width="17%" class="right-border bottom-border">
				<input id="BCSQ" value=0 class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="BCSQ" fieldname="BCSQ" type="number" disabled/>
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
			<th width="8%" class="right-border bottom-border text-right">财务审核值</th>
			<td width="17%" class="right-border bottom-border">
				<input id="CWSHZ" value=0 class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="CWSHZ" fieldname="CWSHZ" type="number" />
			</td>
			<th width="8%" class="right-border bottom-border text-right">计财核定值</th>
			<td width="17%" class="right-border bottom-border">
				<input id="JCHDZ" value=0 class="span9" style="width:70%;text-align:right;"  placeholder="必填" check-type="required"  name="JCHDZ" fieldname="JCHDZ" type="number" />
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