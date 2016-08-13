<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>参建单位-新增</title>
<%
	String dwlx = request.getParameter("dwlx");//单位类型
	String isLxSelect = request.getParameter("isLxSelect");//单位类型
	if(null == dwlx || "".equals(dwlx)){
		dwlx = "";
	}
	if(null == isLxSelect || "".equals(isLxSelect)){
		isLxSelect = "";
	}
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/cjdw/cjdwController.do";
var dwlx = "<%=dwlx%>";
var isLxSelect = "<%=isLxSelect%>";

//页面初始化
$(function() {
	init();
  	//按钮绑定事件（保存）
    $("#btnSave").click(function() {
    	if($("#cjdwForm").validationButton())
		{
	    	var dwmc=$("#DWMC").val();
	    	var dwlx=$("#DWLX").val();
	    	var action1 = controllername + "?queryByMingChen&dwmc="+dwmc+"&dwlx="+dwlx;
			$.ajax({
				url : action1,
				async :	false,
				success: function(result)
				{
					var resultmsgobj = convertJson.string2json1(result);
					var resultobj = convertJson.string2json1(resultmsgobj.msg);
					var subresultmsgobj = resultobj.response.data[0];
					if(subresultmsgobj.ZS>0)
						{
						   xAlert("警告","单位名称已存在，请重新输入","3");
						   return;
						}
					else{
				    		//生成json串
				    		var data = Form2Json.formToJSON(cjdwForm);
				    		//var data1 = defaultJson.packSaveJson(data);
				    		//defaultJson.doInsertJson(controllername + "?insert", data1,null);
				   			$(window).manhuaDialog.setData(data);
				   			$(window).manhuaDialog.sendData();
				   			$(window).manhuaDialog.close();
						}
					}
				});
			}
    	else{
			requireFormMsg();
		  	return;
		}
    });
  	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#cjdwForm").clearFormResult();
        clearCkeckBox();
        //其他处理放在下面
    });
	
});
//页面默认参数
function init(){
	if(dwlx){
		$("#DWLX").val(dwlx);
	}
	if(isLxSelect){
		$("#DWLX").attr("disabled","true");
	}
}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">参建单位信息
      	<span class="pull-right">
      		<button id="btnSave" class="btn" type="button">保存</button>
  		</span>
      </h4>
     <form method="post" id="cjdwForm">
      <table class="B-table" width="100%">
      <input type="hidden" id="GC_CJDW_ID" fieldname="GC_CJDW_ID" name = "GC_CJDW_ID"/>
        <tr>
          <th width="8%" class="right-border bottom-border text-right">单位名称</th>
          <td class="right-border bottom-border">
          	<input class="span12" id="DWMC" type="text" placeholder="必填" check-type="required" fieldname="DWMC" name = "DWMC" maxlength="300"/>
          </td>
          <th width="8%" class="right-border bottom-border text-right">中标次数</th>
          <td class="right-border bottom-border">
          	<input class="span12" id="ZBCS" type="number" style="text-align:right" fieldname="ZBCS" name = "ZBCS" min="0"/>
          </td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right">单位编号</th>
          <td class="right-border bottom-border">
          	<input class="span12" id="DWBH" type="text" fieldname="DWBH" name = "DWBH" maxlength="36"/>
          </td>
          <th width="8%" class="right-border bottom-border text-right">单位类型</th>
          <td class="right-border bottom-border">
          	<select class="span12" id="DWLX" kind="dic" src="JGLB" check-type="required" fieldname="DWLX" name="DWLX">
            </select>
          </td>
        </tr>
		<tr>
		 <th width="8%" class="right-border bottom-border text-right">地址</th>
          <td class="right-border bottom-border" colspan="3">
          	<input class="span12" id="DZ" type="text" fieldname="DZ" name = "DZ" maxlength="500"/>
          </td>
        </tr>
        <tr>
		 <th width="8%" class="right-border bottom-border text-right">电话</th>
          <td class="right-border bottom-border">
          	<input class="span12" id="DH" type="text" fieldname="DH" name = "DH" maxlength="50"/>
          </td>
          <th width="8%" class="right-border bottom-border text-right">成立时间(年)</th>
          <td class="right-border bottom-border">
          	<input class="span12" id="CLSJ" type="text" fieldname="CLSJ" name = "CLSJ" maxlength="4"/>
          </td>
        </tr>
        <tr>
        	<th width="8%" class="right-border bottom-border text-right">企业资质</th>
          <td class="right-border bottom-border">
          	<input class="span12" id="QYZZ" type="text" fieldname="QYZZ" name = "QYZZ" maxlength="100"/>
          </td>
          <th width="8%" class="right-border bottom-border text-right">企业性质</th>
          <td class="right-border bottom-border">
          	<select class="span12" id="QYXZ" kind="dic" src="QYXZ" fieldname="QYXZ" name="QYXZ">
            </select>
          </td>
        </tr>
        <tr>
        	<th width="8%" class="right-border bottom-border text-right">负责人</th>
          <td class="right-border bottom-border">
          	<input class="span12" id="FZR" type="text" fieldname="FZR" name = "FZR" maxlength="100"/>
          </td>
          <th width="8%" class="right-border bottom-border text-right">负责人电话</th>
          <td class="right-border bottom-border">
          	<input class="span12" id="FZRDH" type="text"  fieldname="FZRDH" name = "FZRDH" maxlength="50"/>
          </td>
        </tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right">业务范围</th>
	        <td colspan="3" class="bottom-border right-border">
	        	<textarea class="span12" id="YWFW" rows="3" name ="YWFW"  fieldname="YWFW" maxlength="500"></textarea>
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
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>