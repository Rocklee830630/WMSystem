<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<app:base/>
	<%
	request.setCharacterEncoding("utf-8");
	User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
	String username = user.getName();
	String department = user.getDepartment();
	%>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/banGongHuiWenTiController.do";
	$(function() {
		init();
		$("#printButton").click(function(){
			$(this).hide();
			window.print();
			$(this).show();
		});
		//保存按钮
	   });
function  init(){
	var pwindow =$(window).manhuaDialog.getParentObj();
	var rowValue = pwindow.$("#DT1").getSelectedRow();
	var odd = convertJson.string2json1(rowValue);
	if(odd.ZT=='0'&&odd.ZT=='1'){
		$(".issh").hide();
	}
  //将数据放入表单
  $("#demoForm").setFormValues(odd);
  //为上传文件是需要的字段赋值
    var ywid=$(odd).attr("GC_BGH_WT_ID");
	var SJBH=$(odd).attr("SJBH");
	var YWLX=$(odd).attr("YWLX");
	deleteFileData(ywid,"","","");
	setFileData(ywid,"",SJBH,YWLX,"0");
	//查询附件信息
	queryFileData(ywid,"","","");
}
</script>   
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
  <h4 class="title">上会信息
  	<span class="pull-right">
					<button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
				</span> 
  </h4>
 <form method="post" action="${pageContext.request.contextPath }/insertdemo.xhtml" id="demoForm"  >
     	<input class="span12" id="GC_BGH_WT_ID" type="hidden"   fieldname="GC_BGH_WT_ID" name = "GC_BGH_WT_ID">
      		<table class="B-table" width="100%"  >
      		                
	      			<TR style="display: none">
				        <TD class="right-border bottom-border">
				            <input class="span12"  id="XMID"  type="text"  name = "XMID" fieldname="XMID">
							<input class="span12"  id="BDID"  type="text"  name = "BDID" fieldname="BDID">
							<input class="span12"  id="JHSJID"  type="text"  name = "JHSJID" fieldname="JHSJID">
							<input class="span12"  id="ZT"  type="text"  name = "ZT" fieldname="ZT" value="3">
				        </TD>
	                </TR>
      			<tr>
      				<th  width="8%" class="right-border bottom-border text-right disabledTh ">问题标题</th>
          			<td class="right-border bottom-border"  colspan="3">
          				<input class="span12" type="text" disabled check-type="required" placeholder="必填"   id="WTBT"  name = "WTBT" fieldname="WTBT">
          			</td>
          		</tr>
       			<tr>
          			<th class="right-border bottom-border text-right disabledTh">发起部门</th>
            		<td class="right-border bottom-border ">
            			 <select class="span12 person" kind="dic" disabled id="LRBM" name = "LRBM" fieldname= "LRBM" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME" ></select>
           			</td>
        		    <th class="right-border bottom-border text-right disabledTh">希望解决时间</th>
            		<td  class="right-border bottom-border " >
            			<input class="span12 date"  type="date" disabled check-type="" id="XWJJSJ" name = "XWJJSJ" fieldname="XWJJSJ">
           			</td>
        		</tr>
        		<tr>
          			<th class="right-border bottom-border text-right disabledTh">涉及项目</th>
            		<td  class="right-border bottom-border ">
            			<input class="span12" disabled  type="text" placeholder="" check-type="" id="XMMC" name = "XMMC" fieldname="XMMC"></td>
        		    <th class="right-border bottom-border text-right disabledTh">涉及标段</th>
        		<td   width="42%" class="right-border bottom-border "  >
        			<input class="span12"  disabled id="BDMC" type="text" fieldname="BDMC" name = "BDMC">
        		  </td>
        		</tr>
        			<tr>
          			<th class="right-border bottom-border text-right disabledTh">问题描述</th>
          			<td class="right-border bottom-border"  colspan="3">
          			<textarea rows="3" class="span12"  disabled check-type="required" placeholder="必填"    id="WTMS"  name = "WTMS" fieldname="WTMS"></textarea>
          			</td>
        		</tr>
       			<tr>
					<th width="8%" class="right-border bottom-border text-right">附件</th>
					<td colspan="8" class="bottom-border right-border">
						<div>
									<table role="presentation" class="table table-striped">
										<tbody  onlyView="true" sfjlb="0071" id="shangchuanID1" class="files showFileTab"
											data-toggle="modal-gallery" data-target="#modal-gallery">
										</tbody>
									 </table>
						</div>
					  </td>
				</tr>
				<tr class="issh">
          			<th class="right-border bottom-border text-right disabledTh">审核人</th>
            		<td class="right-border bottom-border ">
            			 <select class="span12 person" kind="dic"  disabledid="SHR" name = "SHR" fieldname= "SHR" src="T#fs_org_person:ACCOUNT:NAME:DEPARTMENT= '<%=department%>'  AND PERSON_KIND = '3' AND FLAG='1'  order by sort" ></select>
           			</td>
        		    <th class="right-border bottom-border text-right disabledTh">审核时间</th>
            		<td  class="right-border bottom-border " >
            			<input class="span12 date"  type="date"  check-type="" id="SHSJ" name = "SHSJ" fieldname="SHSJ"></td>
        		</tr>
       			<tr class="issh">
          			<th class="right-border bottom-border text-right disabledTh">审核意见</th>
          			<td class="right-border bottom-border"  colspan="3">
          			<textarea rows="3" class="span12"  check-type="required" placeholder="必填"    id="SHYJ"  name = "SHYJ" fieldname="SHYJ"></textarea>
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