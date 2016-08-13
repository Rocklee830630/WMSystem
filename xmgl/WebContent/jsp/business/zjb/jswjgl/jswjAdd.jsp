<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/zaoJiaRenWuController.do";
	$(function() {
		//保存按钮
		var btn = $("#example1");
		btn.click(function()
		  {
	 		 	 if($("#demoForm").validationButton())
				{
	 		 		//生成json串
	 				var data = Form2Json.formToJSON(demoForm);
	 				//组成保存json串格式
	 				var data1 = defaultJson.packSaveJson(data);
	 				//调用ajax插入
 			 		defaultJson.doInsertJson(controllername + "?insertZaoJiaRenWu&ywid="+$("#ywid").val(), data1,null,'addHuiDiao');
				}
 		   });
	});
//回调函数
function addHuiDiao()
{
	
	var data2 = $("#frmPost").find("#resultXML").val();
	var fuyemian=$(window).manhuaDialog.getParentObj();
    fuyemian.tianjiahang(data2);
    $(window).manhuaDialog.close();
}
</script>   
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
  <h4 class="title">
  	<span class="pull-right">
		 <button id="example1" class="btn"  type="button">保存</button>
	</span> 
  </h4>
 <form method="post" action="${pageContext.request.contextPath }/insertdemo.xhtml" id="demoForm"  >
     	<input class="span12" id="GC_ZJB_JSWJ_ID" type="hidden" placeholder=""  check-type=""  fieldname="GC_ZJB_JSWJ_ID" name = "GC_ZJB_JSWJ_ID">
      		<table class="B-table" width="100%"  >
      			<tr>
          			<th  width="5%"  class="right-border bottom-border text-right ">年度</th>
          			<td width="45%" class="right-border bottom-border ">
          				 <select class="span12 year" id="ND" name = "ND" fieldname="ND" check-type="required"  kind="dic" src="T#GC_JH_SJ: distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE">
	              		</select>
					</td>
         			<th width="5%" class="right-border bottom-border text-right">接收时间</th>
          			<td width="45%" class="right-border bottom-border " >
          				<input class="span12" id="JSDATE" type="date"  check-type="required" fieldname="JSDATE" name = "JSDATE">
       				</td>
			</tr>
			<tr>
			<th width="8%" class="right-border bottom-border text-right">附件上传</th>
			<td colspan="4" class="bottom-border right-border">
				<div>
				<span class="btn btn-fileUpload" onclick="doSelectFile(this);" id="shangchuanID" fjlb="0072">
					<i class="icon-plus"></i>
						<span>添加文件...</span></span>
							<table role="presentation" class="table table-striped">
								<tbody fjlb="0072" id="shangchuanID1" class="files showFileTab"
									data-toggle="modal-gallery" data-target="#modal-gallery">
								</tbody>
							 </table>
				</div>
			  </td>
			</tr>
      		</table>
      	  </form>
     	</div>
 	</div>
</div>
   <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
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