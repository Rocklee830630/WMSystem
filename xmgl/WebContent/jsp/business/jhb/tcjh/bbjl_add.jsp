<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>统筹计划-版本记录</title>
<%
	String jhid = request.getParameter("jhid");
	String xmbh = request.getParameter("xmbh");
%>
<script type="text/javascript" charset="utf-8">

//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
var jhid = "<%=jhid%>";
var xmbh = "<%=xmbh%>";

//页面初始化
$(function() {
	//设置查询无结果不提示信息
	g_bAlertWhenNoResult = false;
	
	init();
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#bgbbForm").validationButton())
		{
			var data = {BGSM:$("#BGSM").val(),YWID:$("#ywid").val()};
			$(window).manhuaDialog.setData(data);
			$(window).manhuaDialog.sendData();
			$(window).manhuaDialog.close();
		}else{
			requireFormMsg();
		  	return;
		}
	});
	//按钮绑定事件(关闭)
	$("#btnClose").click(function() {
		$(window).manhuaDialog.close();
	});
		
});

	
//页面默认参数
function init(){
	$("#JHID").val(jhid);
	//生成json串
	var data = combineQuery.getQueryCombineData(queryFormBgbb,frmPost,bgList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryBgxx",data,bgList);
}

</script>      
    
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">版本信息说明
      	<span class="pull-right">
      		<button id="btnSave" class="btn"  type="button" >保存</button>
      	</span>
      </h4>
     <form method="post" id="bgbbForm"  >
      <table class="B-table" width="100%" id="bgbbList" >
        <tr>
			<th width="5%" class="right-border bottom-border text-right">编制说明</th>
			<td class="right-border bottom-border" colspan="5">
				<textarea class="span12" id="BGSM" rows="5" placeholder="必填"  check-type="required" name ="BGSM" fieldname="BGSM" maxlength="4000"></textarea>
			</td>
        </tr>
        <tr>
			<th width="8%" class="right-border bottom-border text-right">附件信息</th>
        	<td colspan="5" class="bottom-border right-border">
        		<div>
					<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0060">
						<i class="icon-plus"></i>
						<span>添加文件...</span>
					</span>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0060" class="files showFileTab"
							data-toggle="modal-gallery" data-target="#modal-gallery">
						</tbody>
					</table>
				</div>
        	</td>
        </tr>

      </table>
      </form>
    </div>
    <form method="post" id="queryFormBgbb">
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<input type="text" id="JHID" fieldname="JHID" name="JHID" value="" keep="true" operation="="> 
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
      </table>
      </form>
    <div class="B-small-from-table-autoConcise">
    <h4 class="title">历史版本信息</h4>
	 <div class="B-small-from-table-auto">
      <table class="table-hover table-activeTd B-table" id="bgList" width="100%" type="single" noPage="true" pageNum="1000">
		<thead>
			<tr>
				<th fieldname="BGBBH" tdalign="center" style="width:10%">&nbsp;版本号&nbsp;</th>
				<th fieldname="BGRQ"  tdalign="center" style="width:20%">&nbsp;编制日期&nbsp;</th>
				<th fieldname="BGSM" style="width:70%">&nbsp;编制说明&nbsp;</th>
			</tr>
		</thead>
		<tbody></tbody>
		</table>
	 </div>
	</div>
</div>
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
		 <input type="hidden" name="ywid" id="ywid" value="">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>
