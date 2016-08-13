<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<app:base/>
<title>项目信息管理-项目信息维护</title>
<%
	String id=request.getParameter("id");
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
%>
<script type="text/javascript" charset="utf-8">
var controllername = "${pageContext.request.contextPath }/xmxxController.do";
var id = '<%=id %>',rowObj;
//页面初始化
$(function() {
	init();
  	//按钮绑定事件（保存）
    $("#btnSave").click(function() {
    	
    	if($("#xmxxForm").validationButton())
		{
    		var data = Form2Json.formToJSON(xmxxForm);
    		var ywid=$("#ywid").val();
    		var data1 = addjson(convertJson.string2json1(data),"FLAG","XMXX");
    		var data2 = addjson(convertJson.string2json1(data1),"SJDWFZR",convertJson.string2json1(data).FZR_SJDW);
    		var data3 = addjson(convertJson.string2json1(data2),"SJDWFZRLXFS",convertJson.string2json1(data).LXFS_SJDW);
		    var data4=addjson(convertJson.string2json1(data3),"YWID",ywid);
    		$(window).manhuaDialog.setData(data4);
		   	$(window).manhuaDialog.sendData();
		   	$(window).manhuaDialog.close();
		}else{
			requireFormMsg();
		  	return;
		}
    });
	
});
//页面默认参数
function init(){
	//setFormData();
	getPensonByDep();
	submit(controllername+"?queryXdkById&id="+id,null,xdxmkList);
	deleteFileData(rowObj.GC_TCJH_XMXDK_ID,"",rowObj.SJBH,rowObj.YWLX);
	setFileData(rowObj.GC_TCJH_XMXDK_ID,"",rowObj.SJBH,rowObj.YWLX,"0");
	queryFileData(rowObj.GC_TCJH_XMXDK_ID,"",rowObj.SJBH,rowObj.YWLX);//业务ID，关联ID，事件编号，业务类型 

	
}
/* //绑定form值
function setFormData(){
	var pwindow =$(window).manhuaDialog.getParentObj();
	var rowValue = pwindow.document.getElementById("resultXML").value;
	var obj = convertJson.string2json1(rowValue);
	$("#xmxxForm").setFormValues(obj);
} */
function submit(actionName, data,tablistID){
	$.ajax({
		type : 'post',
		url : actionName,
		data : data,
		cache : false,
		dataType : "json",  
		async :	false,
		success : function(result) {
			 rowObj = convertJson.string2json1(result.msg).response.data[0];
			//alert(JSON.stringify(substr)); 
			$("#xmxxForm").setFormValues(rowObj);
			//$("#resultXML").val(JSON.stringify(rowObj));
		}
	});
}
//过滤负责人
function getPensonByDep(){
	var depid = '<%=deptId %>';
	var src = "T#VIEW_YW_ORG_PERSON:ACCOUNT:NAME:PERSON_KIND = '3' and DEPARTMENT =  '"+depid+"' order by sort ";
	$("#FZR_GLGS").attr('src',src);
	reloadSelectTableDic($("#FZR_GLGS"));
}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
  <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">项目信息
      	<span class="pull-right">
      		<button id="btnSave" class="btn" type="button">保存</button>
  		</span>
      </h4>
     <form method="post" id="xmxxForm">
      <table class="B-table" id="xdxmkList">
        <input type="hidden" class="span12" kind="text"  fieldname="GC_TCJH_XMXDK_ID"  >
        <tr>
        	<th width="5%" class="right-border bottom-border text-right disabledTh">项目编号</th>
			<td class="right-border bottom-border" width="10%"> 
				<input class="span12"  type="text"  fieldname="XMBH" id="XMBH" name = "XMBH" readonly />
			</td>
			<th width="5%" class="right-border bottom-border text-right disabledTh">项目名称</th>
			<td class="right-border bottom-border" colspan="3"> 
				<input class="span12"  type="text"  fieldname="XMMC" id="XMMC" name = "XMMC" readonly />
			</td>
			<th width="5%" class="right-border bottom-border text-right disabledTh">项目类型</th>
			<td class="right-border bottom-border" width="10%"> 
				<input class="span12"  type="text" fieldname="XMLX" id="XMLX" name = "XMLX" readonly />
			</td>
        </tr>
        <tr>
        	<th width="5%" class="right-border bottom-border text-right">业主代表</th>
			<td class="right-border bottom-border"> 
				<select class="span12 person" id="YZDB" kind="dic" src="T#VIEW_YW_ORG_PERSON:ACCOUNT:NAME:PERSON_KIND = '3' and DEPARTMENT in (select row_id from VIEW_YW_ORG_DEPT where EXTEND1='2')" check-type="required" fieldname="YZDB" name="YZDB">
	            </select>
			</td>
			<td colspan="6"></td>
        </tr>
      </table>
      <h5 id="sjdw" class="black">项目管理公司</h5>
		<table id="T_sjdw" class="B-table" width="100%">
			<tr>
	          <th width="10%" class="right-border bottom-border text-right disabledTh">单位名称 </th>
	          <td class="right-border bottom-border" colspan="3">
	          	<input class="span12"  type="text" fieldname="XMGLGS" name = "XMGLGS" readonly />
	          </td>
        	</tr>
        	<tr>
        	  <th width="10%" class="right-border bottom-border text-right">负责人</th>
	          <td class="bottom-border" width="15%">
	          	<select class="span12 person" id="FZR_GLGS" kind="dic" src="" check-type="required" fieldname="FZR_GLGS" name="FZR_GLGS">
	            </select>
	          </td>
	          <th width="10%" class="right-border bottom-border text-right">联系电话</th>
	          <td  class="bottom-border">
	          	<input class="span12" type="text" name = "LXFS_GLGS" maxlength="100" fieldname= "LXFS_GLGS" style="width:40%"/>
	          </td>
        	</tr>
		</table>
      <h5 id="sjdw" class="black">设计单位</h5>
		<table id="T_sjdw" class="B-table" width="100%">
			<tr>
	          <th width="10%" class="right-border bottom-border text-right disabledTh">单位名称 </th>
	          <td class="right-border bottom-border" colspan="3">
	          	<input class="span12"  type="text" fieldname="SJDW" name = "SJDW" readonly />
	          </td>
        	</tr>
        	<tr>
        	  <th width="10%" class="right-border bottom-border text-right">设计负责人</th>
	          <td class="bottom-border" width="15%">
	          	<input class="span12" type="text" name = "FZR_SJDW" maxlength="100" fieldname= "FZR_SJDW"/>
	          </td>
	          <th width="10%" class="right-border bottom-border text-right">联系电话</th>
	          <td  class="bottom-border">
	          	<input class="span12" type="text" name = "LXFS_SJDW" maxlength="100" fieldname= "LXFS_SJDW"  style="width:40%"/>
	          </td>
        	</tr>
		</table>
		<h5 id="sgdw" class="black">施工单位</h5>
		<table id="T_sgdw" class="B-table" width="100%">
			<tr>
	          <th width="10%" class="right-border bottom-border text-right disabledTh">单位名称 </th>
	          <td class="right-border bottom-border" colspan="7">
	          	<input class="span12"  type="text" fieldname="SGDW" name = "SGDW" readonly />
	          </td>
        	</tr>
        	<tr>
        	  <th width="10%" class="right-border bottom-border text-right">项目经理</th>
	          <td class="bottom-border" width="15%">
	          	<input class="span12" type="text" name = "SGDWXMJL" maxlength="100" fieldname= "SGDWXMJL"/>
	          </td>
	          <th width="10%" class="right-border bottom-border text-right">联系电话</th>
	          <td class="bottom-border" >
	          	<input class="span12" type="text" name = "SGDWXMJLLXDH" maxlength="100" fieldname= "SGDWXMJLLXDH"/>
	          </td>
	          <th width="10%" class="right-border bottom-border text-right">技术负责人</th>
	          <td class="bottom-border" width="15%">
	          	<input class="span12" type="text" name = "SGDWJSFZR" maxlength="100" fieldname= "SGDWJSFZR"/>
	          </td>
	          <th width="10%" class="right-border bottom-border text-right">联系电话</th>
	          <td class="bottom-border">
	          	<input class="span12" type="text" name = "SGDWJSFZRLXDH" maxlength="100" fieldname= "SGDWJSFZRLXDH"/>
	          </td>
        	</tr>
		</table>
		<h5 id="jldw" class="black">监理单位</h5>
		<table id="T_jldw" class="B-table" width="100%">
			<tr>
	          <th width="10%" class="right-border bottom-border text-right disabledTh">单位名称 </th>
	          <td class="right-border bottom-border" colspan="11">
	          	<input class="span12"  type="text" fieldname="JLDW" name = "JLDW" readonly />
	          </td>
        	</tr>
        	<tr>
        	 <th width="10%" class="right-border bottom-border text-right">总监</th>
	          <td  class="bottom-border">
	          	<input class="span12" type="text" name = "JLDWZJ" maxlength="100" fieldname= "JLDWZJ" />
	          	</td>
	          <th width="5%" class="right-border bottom-border text-right">联系电话</th>
	          <td class="bottom-border">
	          	<input class="span12" type="text" name = "JLDWZJLXDH" maxlength="100" fieldname= "JLDWZJLXDH" />
	          </td>
	          <th width="5%" class="right-border bottom-border text-right">总监代表</th>
	          <td  class="bottom-border">
	          	<input class="span12" type="text" name = "JLDWZJDB" maxlength="100" fieldname= "JLDWZJDB" />
	          	</td>
	          <th width="5%" class="right-border bottom-border text-right">联系电话</th>
	          <td class="bottom-border">
	          	<input class="span12" type="text" name = "JLDWZJDBLXDH" maxlength="100" fieldname= "JLDWZJDBLXDH" />
	          </td>
	          <th width="5%" class="right-border bottom-border text-right">安全监理</th>
	          <td  class="bottom-border">
	          	<input class="span12" type="text" name = "JLDWAQJL" maxlength="100" fieldname= "JLDWAQJL" />
	          	</td>
	          <th width="5%" class="right-border bottom-border text-right">联系电话</th>
	          <td class="bottom-border">
	          	<input class="span12" type="text" name = "JLDWAQJLLXDH" maxlength="100" fieldname= "JLDWAQJLLXDH" />
	          </td>
        	</tr>
	        <tr>
	        	<th width="10%" class="right-border bottom-border text-right">附件信息</th>
	        	<td colspan="11" class="bottom-border right-border">
					<div>
						<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0035">
							<i class="icon-plus"></i>
							<span>添加文件...</span>
						</span>
						<table role="presentation" class="table table-striped">
							<tbody fjlb="0035" class="files showFileTab" uploadOptions="type:image"
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
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id="frmPost">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter" order="desc" fieldname="A.XMBH,A.LRSJ"/>
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
          <input type="hidden" name="ywid" id = "ywid" value="">
         <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 </div>
</body>
</html>