<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%
	String isPublic = request.getParameter("isPublic");
%>
<app:base/>
<title>下达项目库-维护</title>
<script type="text/javascript" charset="UTF-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/txlController.do";
var isPublic = "<%=isPublic %>";
var id,rowindex;
//页面初始化
$(function() {
	init();
	//按钮绑定事件(查询)
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryTxlGroup&isPublic="+isPublic, data, xdxmkList);
        $("#xdxmkForm").clearFormResult();
        id = null;
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#xdxmkForm").validationButton())
		{
		    //生成json串
		    var data = Form2Json.formToJSON(xdxmkForm);
		  //组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  //调用ajax插入
		  	if(id == null || id == "null" || id == "indefined") {
				defaultJson.doInsertJson(controllername + "?executeTxlGroup&isPublic="+isPublic+"&id="+id, data1,xdxmkList);
		        $("#xdxmkForm").clearFormResult();
			} else {
				defaultJson.doUpdateJson(controllername + "?executeTxlGroup&isPublic="+isPublic+"&id="+id, data1,xdxmkList);
			}
		}else{
			requireFormMsg();
		  	return;
		}
	});
	
	// 点击删除按钮
	$(function() {
		var btn = $("#deleteBtn");
		btn.click(function() {
		 	if($("#xdxmkList").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条要操作的数据！','3');
			} else {
				xConfirm("提示信息","是否确认删除！");
			    //生成json串
			    var data = Form2Json.formToJSON(xdxmkForm);
			  //组成保存json串格式
			    var data1 = defaultJson.packSaveJson(data);
				//通过判断id是否为空来判断是插入还是修改
				$('#ConfirmYesButton').one("click",function(obj) {
					var success = defaultJson.doUpdateJson(controllername + "?deleteTxlGroup", data1,xdxmkList);
					
					if(success == true) {
						$("#xdxmkList").setSelect(rowindex);
				//		var dd = {id:1,name:2};//此处为入参
						var index = $("#xdxmkList").getSelectedRowIndex();//获得选中行的索引
						var value = $("#xdxmkList").getSelectedRow();//获得选中行的json对象

				        $("#xdxmkList").removeResult(index);
				        $("#xdxmkForm").clearFormResult();
					}
				});  
				
			} 
		});
	});

	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
	
});
//页面默认参数
function init(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryTxlGroup&isPublic="+isPublic,data,xdxmkList);
}

//点击行事件
function tr_click(obj){
	//alert(JSON.stringify(obj));
	$("#xdxmkForm").setFormValues(obj);
	rowindex = $("#xdxmkList").getSelectedRowIndex();//获得选中行的索引
	var rowValue = $("#xdxmkList").getSelectedRow();//获得选中行的json对象
	id = obj.GROUPID;
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			</TD>
        </TR>
         <tr>
          <th width="5%" class="right-border bottom-border text-right">组名称</th>
          <td class="right-border bottom-border" width="30%">
           <input class="span12" type="text" placeholder="" name="ZMC" fieldname="ZMC" operation="like" logic="and">
          </td>
          <td width="30%" class="text-left bottom-border text-right">
           <button id="btnQuery" class="btn btn-link"  type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-search"></i>查询</button>
           <button id="btnClear" class="btn btn-link" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-trash"></i>清空</button>
           <button id="deleteBtn" class="btn btn-link" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-remove"></i>删除</button>
          </td>
        </tr>
      </table>
      </form>
    <div style="height:5px;"></div>
    <div class="overFlowX">
		<table class="table-hover table-activeTd B-table" id="xdxmkList" width="100%" type="single" pageNum="5" editable="0">
		<thead>
			<tr>
                <th name="XH" id="_XH">序号</th>
                <th fieldname="ZMC">组名称</th>
			</tr>
		</thead>
		<tbody></tbody>
		</table>
		</div>
	</div>
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">组信息
      	<span class="pull-right">
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
      	</span>
      </h4>
     <form method="post" id="xdxmkForm"  >
      <table class="B-table" width="100%">
        <tr>
         <th width="8%" class="right-border bottom-border text-right">组名称</th>
       	 <td class="bottom-border right-border"  colspan="5">
         	<input class="span12" id="ZMC"  type="text" placeholder="必填" check-type="required maxlength" maxlength="30" fieldname="ZMC" name="ZMC" maxlength="100">
	  		<input type="hidden" id="GROUPID" fieldname="GROUPID" name="GROUPID"/>
       	 </td>
        </tr>
      </table>
      </form>
    </div>
   </div>
  </div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id="queryXML">
         <input type="hidden" name="txtXML" id="txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname="XMBH,LRSJ" id="txtFilter">
         <input type="hidden" name="resultXML" id="resultXML">
         <input type="hidden" name="queryResult" id = "queryResult">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
<script>
</script>
</html>