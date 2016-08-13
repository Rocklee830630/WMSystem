<!DOCTYPE html>
<html>
<head>
<%@ page import="com.ccthanking.framework.common.User" %>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.common.QuerySet"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%
	String sjbh=request.getParameter("sjbh");
	
	String sql = "SELECT SWBH, WJBT,(select DICT_NAME from  FS_COMMON_DICT  where DICT_ID=t.LWDW and DICT_CATEGORY='xtbg_gwgl_lwdw') LWDW, DQZT,(select DICT_NAME from  FS_COMMON_DICT  where DICT_ID=t.WZ and DICT_CATEGORY='xtbg_gwgl_wz') WZ, HJ, WH, SWRQ, " 
			+ "SWLB, SSXM, FWRQ, MJ, YWLX, SERIAL_NUM " 
			+ "FROM XTBG_GWGL_SWGL T WHERE SJBH='" + sjbh + "'";
//	= "select wjbh, wjbt, lsh, dqzt, (select dept_name from VIEW_YW_ORG_DEPT d where d.row_id=t.ngdw) ngdw, " 
//			+ "ngrq, (select name from fs_org_person p where p.account=t.ngr) ngr, fwsj, wz, hj, ssxm, zs, cs, mj, " 
//			+ "dyfs, ywlx, sjbh, bz from xtbg_gwgl_fwgl t where sjbh='" + sjbh + "'";
	QuerySet qs = DBUtil.executeQuery(sql, null);
	
	String eventSjbh=request.getParameter("eventSjbh") == null ? "" : request.getParameter("eventSjbh");
%>
<app:base />
<title>协同办公-信息中心-新增公告</title>
<script type="text/javascript" charset="UTF-8">

var controllername= "${pageContext.request.contextPath }/fwglController.do";

//初始化加载
$(document).ready(function(){
	// 加载附件
	setFileData("","","<%=sjbh %>","200502");
	// 查询附件
	queryFileData("","","<%=sjbh %>","200502");
});

function chakan(){

	if("<%=eventSjbh%>" == "0") {
		requireFormMsg("此发文还未发起流程，无法查看审批意见");
		return;
	}
	$(window).manhuaDialog({"title":"审批意见","type":"text","content":"<%=request.getContextPath()%>/jsp/framework/common/aplink/spYjView.jsp?sjbh=<%=sjbh%>","modal":"2"});	
}

function clickCancelBtn()
{
	$(window).manhuaDialog.close();
}
</script>    
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
    	<div class="B-small-from-table-autoConcise">
     		<form method="post" id="demoForm"  >
      			<table class="B-table" width="100%" id="DT1">
      				<TR  style="display:none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<input type="text" kind="text" fieldname="FWID" name="FWID" id="FWID">
						</TD>
					</TR>
					<tr>
						<th width="5%" class="right-border bottom-border text-right">文件标题</th>
						<td width="29%" colspan="3" class="right-border bottom-border">
							<%=qs.getString(1, "WJBT") == null ? "" : qs.getString(1, "WJBT") %>
						</td>
						
						<th width="5%" class="right-border bottom-border text-right">收文日期</th>
						<td width="12%" class="right-border bottom-border">
							<%=qs.getString(1, "SWRQ") == null ? "" : qs.getString(1, "SWRQ") %>
						</td>
							
						<th width="5%" class="right-border bottom-border text-right">发文日期</th>
						<td width="12%" class="right-border bottom-border">
							<%=qs.getString(1, "FWRQ") == null ? "" : qs.getString(1, "FWRQ") %>
						</td>
					</tr>
					
					<tr>
							
						<th class="right-border bottom-border text-right">所属项目</th>
						<td class="right-border bottom-border" colspan="3">
							<%=qs.getString(1, "SSXM") == null ? "" : qs.getString(1, "SSXM") %>
						</td>
						
						<th class="right-border bottom-border text-right">收文编号</th>
						<td class="right-border bottom-border">
							<%=qs.getString(1, "SWBH") == null ? "" : qs.getString(1, "SWBH") %>
						</td>
						
						<th class="right-border bottom-border text-right">文号</th>
						<td class="right-border bottom-border" colspan="3">
							<%=qs.getString(1, "WH") == null ? "" : qs.getString(1, "WH") %>
						</td>
					</tr>
					
					<tr>
					
						<th class="right-border bottom-border text-right">来文单位</th>
						<td class="right-border bottom-border" colspan="3">
							<%=qs.getString(1, "LWDW") == null ? "" : qs.getString(1, "LWDW") %>
						</td>
						
						<th class="right-border bottom-border text-right">文种</th>
						<td class="right-border bottom-border">
							<%=qs.getString(1, "WZ") == null ? "" : qs.getString(1, "WZ") %>
						</td>
						
						<th class="right-border bottom-border text-right">缓急</th>
						<td class="right-border bottom-border">
							<%=Pub.getDictValueByCode("HJ1000000000302", (qs.getString(1, "HJ") == null ? "" : qs.getString(1, "HJ"))) %>
						</td>
						
					</tr>
					<tr>
			        	<th class="right-border bottom-border text-right">附件信息</th>
			        	<td colspan="7" class="bottom-border right-border">
							<div>
								<table role="presentation" class="table table-striped">
									<tbody  class="files showFileTab"
										data-toggle="modal-gallery" data-target="#modal-gallery" 
										showLrr="true" showSize="false" onlyView="true">
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
         <input type="hidden" name="queryXML" id="queryXML">
         <input type="hidden" name="txtXML" id="txtXML">
         <input type="hidden" name="txtFilter" order="desc" fieldname="ND" id="txtFilter">
         <input type="hidden" name="resultXML" id="resultXML">
         <input type="hidden" id="queryResult" name="queryResult"/>
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">		
 	</FORM>
 </div>
</body>
</html>