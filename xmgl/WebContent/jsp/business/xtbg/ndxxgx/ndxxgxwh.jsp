<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Blob"%>
<%
Date d=new Date();//获取时间
SimpleDateFormat sam_date=new SimpleDateFormat("yyyy-MM-dd");		//转换格式
String today=sam_date.format(d);
String id=request.getParameter("id");
String nr = "";
if(id != null) {
	String queryGgBlob = "select nr from XTBG_XXZX_NDXXGX where XTBG_XXZX_NDXXGX_ID='"+id+"'";
	
	com.ccthanking.framework.common.QuerySet qs = com.ccthanking.framework.common.DBUtil.executeQuery(queryGgBlob, null);
	Blob dbBlob = (Blob)qs.getObject(1, "nr");
   	if (dbBlob != null)
   	{
       int length = (int) dbBlob.length();
       byte[] buffer = dbBlob.getBytes(1, length);
       nr = new String(buffer,"GBK");
   	}
}
%>
<app:base/>
<script charset="UTF-8" src="${pageContext.request.contextPath }/js/kindeditor/kindeditor.js"></script>
<script charset="UTF-8" src="${pageContext.request.contextPath }/js/kindeditor/lang/zh_CN.js"></script>
<script charset="UTF-8" src="${pageContext.request.contextPath }/js/kindeditor/customKE.js"></script>
<script type="text/javascript" charset="utf-8">
	var controllername = "${pageContext.request.contextPath }/ndxxgxController.do";
	var id="<%=id %>";
	$(function(){
		formEditor.init("kindEditor");
		if(id == null || id == "null" || id == "undefined") {
			var date= "<%=today %>";
	  		$("#FBSJ").val(date);
		}else{
		  	//获取父页面的值
		  	//将父页面的值转成json对象
		  	var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
		  	var odd=convertJson.string2json1(rowValue);
		  	//将数据放入表单
		  	$("#demoForm").setFormValues(odd);
		  	$("#kindEditor").val($("#NR").val());
			//为上传文件是需要的字段赋值
		    var ywid=$(odd).attr("XTBG_XXZX_NDXXGX_ID");
			var SJBH=$(odd).attr("SJBH");
			var YWLX=$(odd).attr("YWLX");
			deleteFileData(ywid,"","","");
			setFileData(ywid,"",SJBH,YWLX,"0");
			//查询附件信息
			queryFileData(ywid,"","","");
		}
	  });
	$(function() {
		var btn = $("#editBut");
		btn.click(function() {
			$("#NR").val(formEditor.htmlVal());
			var nr = $("#NR").val();
			if(nr==null||nr==""){
				xAlert("提示信息","请输入内容",'3');
				return;
			}
			if ($("#demoForm").validationButton()) {
				//生成json串
				var data = Form2Json.formToJSON(demoForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				var ywid = $("#ywid").val();
				//调用ajax插入
				if($("#XTBG_XXZX_NDXXGX_ID").val()==''){
					 defaultJson.doUpdateJson(controllername + "?insertNdxxgx&ywid="+ywid, data1,null,'addHuiDiao');
				}else{
					 defaultJson.doUpdateJson(controllername + "?updateNdxxgx", data1,null,'eidtHuiDiao');
				}
			}
		});
	});

	function eidtHuiDiao(){
		//从resultXML获取修改的数据
		var data2 = $("#frmPost").find("#resultXML").val();
		//返回数据
		var fuyemian=$(window).manhuaDialog.getParentObj();
		fuyemian.xiugaihang(data2);
		$(window).manhuaDialog.close();
	}
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
		  <button id="editBut" class="btn"  type="button">保存</button>
      </span>  
  </h4>
     	<form method="post" action="${pageContext.request.contextPath }/updateJieSuan.do" id="demoForm"  >
      			<table class="B-table" width="100%"  >
	      			<TR  style="display:none;">
				        <TD class="right-border bottom-border">
	                        <input class="span12" id="XTBG_XXZX_NDXXGX_ID" type="text" placeholder=""  check-type=""  fieldname="XTBG_XXZX_NDXXGX_ID" name = "XTBG_XXZX_NDXXGX_ID">
				        <input type="hidden" name="ywid" id="ywid" value="">  
				        </TD>
	                </TR>
        		</table>
        		 <table class="B-table" width="100%">
       			<tr>
          			<th width="8%" class="right-border bottom-border text-right" >标题</th>
          			<td width="17%" class="right-border bottom-border " colspan="3"><input class="span12"  check-type="required"  type="text" maxlength="500"  id="NDXXBT" name = "NDXXBT" fieldname="NDXXBT"></td>
          		</tr>
          		<tr>
          			<th width="8%" class="right-border bottom-border text-right">发布时间</th>
          			<td width="21%" class="right-border bottom-border text-left" ><input class="span12 date"  type="date"    id="FBSJ" name = "FBSJ" fieldname="FBSJ"></td>
          		 	<th  width="8%" class="right-border bottom-border text-right">是否发布</th>
         			<td class=" bottom-border text-left" >
         				<select class="span2" id="ISFB" name = "ISFB" check-type="required"    fieldname="ISFB" kind="dic"  src="SF" >
		                </select>
	                </td>
          		</tr>
       			<tr>
         			<th width="8%" class="right-border bottom-border text-right">内容</th>
						<td width="92%" colspan="7" class="bottom-border">
							<textarea class="span12" name="kindEditor"  id="kindEditor" maxlength="4000" style="width:100%;height:300px;visibility:hidden;"><%=nr %></textarea>
						</td>
        		</tr>
        		<tr>
					<th width="8%" class="right-border bottom-border text-right">附件上传</th>
					<td colspan="3" class="bottom-border right-border">
						<div>
						<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0073">
							<i class="icon-plus"></i>
								<span>添加文件...</span></span>
								<textarea maxlength="4000" fieldname="NR" id="NR" name="NR" style="visibility: hidden;"><%=nr %></textarea>
								<table role="presentation" class="table table-striped">
									<tbody fjlb="0073" class="files showFileTab"
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