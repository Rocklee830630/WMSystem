<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<%
	String xx=request.getParameter("xx");
%>
<title>规划审批进度维护</title>
<script type="text/javascript" charset="utf-8">
  
  var controllername= "${pageContext.request.contextPath }/liXiangShouXuController.do";
  var biaozhi=null;
	$(function()
		{
		
			//保存
			    var savebtn=   $("#save");
	            savebtn.click(function() 
					{
 					   //修改
			 		 	if($("#demoForm").validationButton())
						{
			 		 		//生成json串
			 		 		var data = Form2Json.formToJSON(demoForm);
			 				//组成保存json串格式
			 				var data1 = defaultJson.packSaveJson(data);
			 				defaultJson.doUpdateJson(controllername + "?update", data1,null);
			 				var obj = $("#resultXML").val();		
			 				var fuyemian=parent.$("body").manhuaDialog.getParentObj();
	 				        fuyemian.xiugaiahang(obj);
	 				       parent.$("body").manhuaDialog.close();
						}
					});
	            //新增清空
	            var btn = $("#example_clear");
				btn.click(function() 
				{
					$("#demoForm").clearFormResult(); 
				});
		});
	
	//初始化加载
	$(document).ready(function(){
		 //获取父页面的值
		  var a=<%=xx%>;
		  //将父页面的值转成json对象
		  var odd=convertJson.string2json1(a);
		  $("#demoForm").setFormValues(odd);
		 
		//为上传文件是需要的字段赋值
		    var ywid=$(odd).attr("GC_QQSX_LXKY_ID");
			var SJBH=$(odd).attr("SJBH");
			var YWLX=$(odd).attr("YWLX");
			setFileData(ywid,"",SJBH,YWLX);
			//查询附件信息
			queryFileData(ywid,"","","");
		  //将数据放入表单
		  
			
	});
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
    <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">信息维护表单
         <span class="pull-right">
	          <button id="save" name="save" class="btn"  type="button">保存</button>
	          <button id="example_clear" class="btn"  type="button">清空</button> 
          </span>
       </h4>
     <form method="post" id="demoForm"  >
      <table class="B-table" width="100%">
                
         <TR  style="display:none;">
	        <TD class="right-border bottom-border">
		          <input type="text" class="span12" kind="text" keep="true"  fieldname="xdkid"  >
	      		 <input type="text" class="span12" kind="text" keep="true" fieldname="qqsxid" >
				 <input type="text" class="span12" kind="text" keep="true" fieldname="GC_QQSX_LXKY_ID" >
	        </TD>
			<TD class="right-border bottom-border"></TD>
        </TR>
        <tr>
          <th width="10%" class="right-border bottom-border text-right">项目名称</th>

          <td width="40%"  class="right-border bottom-border"><input class="span12" keep="true" readonly="readonly" placeholder="请选择项目" check-type="required" type="text"  fieldname="XMMC" name = "XMMC"></td>
          <th width="10" class="right-border bottom-border text-right"> 办结时间</th>
           <td width="42%" class="bottom-border"><input class="span12" type="date"  name = "BJSJ" fieldname= "BJSJ"></td>
        </tr>
        <tr>
        <th width="10%" class="right-border bottom-border text-right">存在问题</th>
          <td width="90%" colspan="3" class="bottom-border">
          <textarea rows="3" class="span12" id="CZWT" name = "CZWT" fieldname= "CZWT" maxlength="4000"  check-type="maxlength"></textarea>
          </td>
        </tr>
         <tr>
          <th width="12%" class="right-border bottom-border text-right">经办人</th>
          <td width="40%" colspan="2" class="right-border bottom-border"> <input class="span12" type="text" id="JBR" maxlength="20"  name = "JBR" fieldname= "JBR">
          </td>
           <td  class=" bottom-border "></td>
        </tr>
        <tr>
          <th width="12%" class="right-border bottom-border text-right">不办理手续</th>
          <td width="40%" colspan="3" class="right-border bottom-border"> 
          <input class="span12" id="BBLSX" type="checkbox"   kind="dic" src="LXKYFJLX" name = "BBLSX" fieldname="BBLSX"  >
          </td>
          
        </tr>
       
        <tr>
          <th width="8%" class="right-border bottom-border text-right">不办理原因</th>
          <td width="42%" colspan="5" class="bottom-border">
          <textarea rows="3" class="span12" id="BBLYY" name = "BBLYY" fieldname= "BBLYY" maxlength="4000"  check-type="maxlength"></textarea>
          </td>
        </tr>
        <tr>
			<th width="8%" class="right-border bottom-border text-right">附件上传</th>
			<td colspan="5" class="bottom-border right-border">
				<div>
				<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="3050">
					<i class="icon-plus"></i>
						<span>添加文件...</span></span>
							<table role="presentation" class="table table-striped">
								<tbody fjlb="3050" class="files showFileTab"
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
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="resultXML" id = "resultXML">
          <input type="hidden" name="txtFilter" order="desc" fieldname="xmxdk.XMBH,lxky.LRSJ"/>
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
          <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 </div>
</body>
</html>