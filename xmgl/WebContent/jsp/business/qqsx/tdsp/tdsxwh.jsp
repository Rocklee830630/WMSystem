<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title>规划审批进度维护</title>
<%
	String id=request.getParameter("id");
%>
<%-- <script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script> --%>
<script type="text/javascript" charset="utf-8">
  var a,fj;
  var controllername= "${pageContext.request.contextPath }/qqsx/tdsp/sxfjController.do";
	var id="<%=id%>";
	$(function() {
		var querybtn = $("#query");
		var newbtn= $("#new");
		var savebtn=   $("#save");
		var clean=$("#clean");
		var clear=$("#clear");
		querybtn.click(function() {
			//生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,sxfjList);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?querySxfj",data,sxfjList);
				});
		savebtn.click(function() {
			var data = Form2Json.formToJSON(SxfjForm);
			/* var a=$(obj).attr("ID"); */
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			if(a==null||a=="null")
				{	
					defaultJson.doInsertJson(controllername + "?insertSxfj&ywid="+$("#ywid").val(), data1,sxfjList);
					var fuyemian=parent.$("body").manhuaDialog.getParentObj();
					fuyemian.gengxinchaxun();
				}else{
					defaultJson.doUpdateJson(controllername + "?updateSxfj", data1,sxfjList);
					var fuyemian=parent.$("body").manhuaDialog.getParentObj();
					fuyemian.gengxinchaxun();
				}
			
				});
		clean.click(function(){
			$("#queryForm").clearFormResult();
				});
		clear.click(function(){
			$("#SxfjForm").clearFormResult();
				});
	});
    $(document).ready(function() {
    	g_bAlertWhenNoResult=false;	
      	 var data = combineQuery.getQueryCombineData(queryForm,frmPost,sxfjList);
   		//调用ajax插入
   		defaultJson.doQueryJsonList(controllername+"?querySxfj",data,sxfjList);
   		g_bAlertWhenNoResult=true;	
      });
    function tr_click(obj){
    	a=$(obj).attr("GC_QQSX_SXFJ_ID");
		$("#SxfjForm").setFormValues(obj);
		setFileData(id,a,obj.SJBH,obj.YWLX);
    	queryFileData("",a,"","");
		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		$("#resultXML").val(JSON.stringify(obj));
	}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
 <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
      	<!-- <TR> -->
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" keep="true">
				<INPUT type="text" class="span12" kind="text"  fieldname="YWBID"  value="<%=id%>" operation="=" keep="true">
			</TD>			
        </TR>
        <tr>
          <th width="5%" class="right-border bottom-border text-right">选择手续</th>
          <td class="left-border bottom-border" width="10%">
          	<select class="span12" id="FJLX" name = "FJLX" fieldname = "FJLX" operation="=" kind="dic" src="TDSPSX">
            </select>
          </td>
          <td width="30%" class="text-left bottom-border text-right">
           <button id="query" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
           <button id="clean" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
          </td>
        </tr>
      </table>
      </form>
    <div style="height:5px;"></div>
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="sxfjList" width="100%" type="single" pageNum="5">
		<thead>
			<tr>
			<th name="XH" id="_XH" >&nbsp;&nbsp;#&nbsp;&nbsp;</th>
            <th fieldname="WHMC" maxlength="15">&nbsp;&nbsp;文号名称&nbsp;&nbsp;</th>
            <th fieldname="FJLX">&nbsp;&nbsp;手续类型&nbsp;&nbsp;</th>
            <th fieldname="BLR">&nbsp;&nbsp;办理人&nbsp;&nbsp;</th>
            <th fieldname="BLSJ" tdalign="center">&nbsp;&nbsp;办理时间&nbsp;&nbsp;</th>
			</tr>
		</thead>
	<tbody></tbody>
	</table>
	</div>
</div>
    <div style="height:5px;"></div>
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">土地手续信息
      <span class="pull-right">
          <button id="save" name="save" class="btn"  type="button">保存</button>
          <button id="clear" name="clear" class="btn"  type="button">清空</button>
       </span></h4>
     <form method="post" id="SxfjForm"  >
      <table class="B-table" width="100%">
         <TR  style="display:none;">
         <!-- <TR> -->
	        <TD><input type="text" class="span12" kind="text"  fieldname="GC_QQSX_SXFJ_ID" keep="true" ></TD>
			<TD><input type="text" class="span12" kind="text"  fieldname="YWBID" value="<%=id %>" keep="true" ></TD>
			<TD><input type="text" class="span12" kind="text"  id="ywid"></TD>
        </TR>
        <tr>
          <th width="10%" class="right-border bottom-border text-right">选择手续</th>
          <td width="40%" colspan="4" class="right-border bottom-border">
          <select class="span12" id="FJLX1" name = "FJLX" fieldname = "FJLX" kind="dic" src="TDSPSX" >
            </select>
          </td>
          <th width="10%" class="right-border bottom-border text-right">文号名称</th>
          <td width="40%" colspan="4" class="right-border bottom-border"><input class="span12"  type="text" placeholder="必填"  check-type="required" fieldname="WHMC" name = "WHMC" check-type="maxlength" maxlength="200"></td>
        </tr>
        <tr>
          <th width="10%" class="right-border bottom-border text-right">办理人</th>
          <td width="40%" colspan="4" class="right-border bottom-border">
          	<input class="span12"  type="text" placeholder="必填"  check-type="maxlength" fieldname="BLR" id="BLR" name = "BLR" maxlength="20"></td>
          <th width="10%" class="right-border bottom-border text-right">办理时间</th>
          <td width="40%" colspan="4" class="right-border bottom-border">
            <input class="span12"  type="date" placeholder="必填"  check-type="" fieldname="BLSJ" name = "BLSJ" ></td>
        </tr>
        <tr>
			<th width="10%" class="right-border bottom-border text-right">附件信息</th>
			   <td width="90%" colspan="9" class="bottom-border right-border">
					<div>
						<span class="btn btn-fileUpload" id="scfj" onclick="doSelectFile(this);" fjlb="0104">
								<i class="icon-plus"></i>
								<span>添加文件...</span>
						</span>
							<table role="presentation" class="table table-striped">
								<tbody id="scfj1" fjlb="0104" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery">
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
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>