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
	<style type="text/css">
		input[disabled],
		textarea[disabled],
		select[disabled]{
			border-style:none;
			background:#FFF;
			box-shadow:none;
		}
		.form-inline{
			width:80%;
		}
		.titleSpan{
			font-weight: bold;
			font-size:14px;
		}
		.contentSpan{
			font-size:14px;
		}
		.cancleResize{
			resize:none;
		}
	</style>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/banGongHuiWenTiController.do";
	$(function() {
		init();
		$("#printButton").click(function(){
			$(this).hide();
			$(".fileTr").hide();
			$("textarea").addClass("cancleResize");//打印过程中，隐藏textarea右下脚拖拽图标
			window.print();
			$("textarea").removeClass("cancleResize");//打印完毕，恢复textarea右下脚拖拽图标
			$(".fileTr").show();
			$(this).show();
		});
	   });
function  init(){
		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.$("#DT1").getSelectedRow();
		var odd = convertJson.string2json1(rowValue);
		if(odd.ZT=='0'||odd.ZT=='1'){
			$(".shcl").hide();
			$(".ytsh").hide();
		}if(odd.ZT=='2'){
			$(".shcl").hide();
		}if(odd.ZT=='3'){
			$(".ytsh").hide();
		}
	  	//将数据放入表单
	  	$("#demoForm").setFormValues(odd);
		$("input").each(function(i){
			//分页的输入框允许使用
			if($(this).attr("id")!="go_DT0"){
				$(this).attr("disabled","true");
			}
		});
		$("textarea").each(function(){
			$(this).attr("disabled","true");
			//$(this).removerAttr("disabled");
			//$(this).attr("style","autoTextarea");
			//$(this).attr("onpropertychange","this.style.height=this.scrollHeight+'px';");
			//$(this).attr("oninput","this.style.height=this.scrollHeight+'px';")
		});
		$("th").addClass("disabledTh");
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
 <div class="row-fluid" style="text-align:center">
 <div class="B-small-from-table-autoConcise" style="min-width:980px;">
	<span class="pull-right">
		<button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
	</span>
	<h3 class="title">议题情况
	</h3>
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
      				<th  width="8%" class="right-border bottom-border text-right disabledTh ">议题标题</th>
          			<td class="right-border bottom-border"  colspan="7">
          				<input class="span12" type="text" disabled check-type="required" placeholder="必填"   id="WTBT"  name = "WTBT" fieldname="WTBT">
          			</td>
          		</tr>
       			<tr>
          			<th class="right-border bottom-border text-right disabledTh" width="7%">发起部门</th>
            		<td class="right-border bottom-border " width="18%">
            			 <input type="text" class="span12 department" kind="dic" disabled id="FQBM" name = "FQBM" fieldname= "FQBM" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME" >
           			</td>
           			<th class="right-border bottom-border text-right disabledTh" width="7%">发起人</th>
            		<td class="right-border bottom-border " width="18%">
            			 <input type="text" class="span12 person" kind="dic" disabled id="FQR" name = "FQR" fieldname= "FQR" src="T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG='1'  AND PERSON_KIND = '3'  order by sort" >
           			</td>
        		    <th class="right-border bottom-border text-right disabledTh" width="7%">希望解决时间</th>
            		<td  class="right-border bottom-border "  td colspan="3">
            			<input class="span12"  type="text" kind="date" disabled check-type="" id="XWJJSJ" name = "XWJJSJ" fieldname="XWJJSJ">
        		</tr>
        		<tr>
          			<th class="right-border bottom-border text-right disabledTh">涉及项目</th>
            		<td  class="right-border bottom-border " colspan="3">
            			<input class="span12" disabled  type="text" placeholder="" check-type="" id="XMMC" name = "XMMC" fieldname="XMMC"></td>
        		    <th class="right-border bottom-border text-right disabledTh">涉及标段</th>
        		<td   class="right-border bottom-border "  colspan="3">
        			<input class="span12"  disabled id="BDMC" type="text" fieldname="BDMC" name = "BDMC">
        		  </td>
        		</tr>
        			<tr>
          			<th class="right-border bottom-border text-right disabledTh">议题描述</th>
          			<td class="right-border bottom-border"  colspan="7">
          			<textarea class="span12" check-type="required" placeholder="必填" rows=5 id="WTMS"  name = "WTMS" fieldname="WTMS"></textarea>
          			</td>
        		</tr>
        		<tr class="fileTr">
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
				<tr class="shcl">
          			<th class="right-border bottom-border text-right disabledTh">审核人</th>
            		<td class="right-border bottom-border ">
            			 <input type="text" class="span12 person" kind="dic"  disabledid="SHR" name = "SHR" fieldname= "SHR" src="T#fs_org_person:ACCOUNT:NAME:DEPARTMENT= '<%=department%>'  AND PERSON_KIND = '3' AND FLAG='1'  order by sort" >
           			</td>
        		    <th class="right-border bottom-border text-right disabledTh">审核时间</th>
            		<td  class="right-border bottom-border " >
            			<input class="span12"  type="text" kind="date" check-type="" id="SHSJ" name = "SHSJ" fieldname="SHSJ"></td>
        		<td colspan="4"></td>
        		</tr>
       			<tr class="shcl">
          			<th class="right-border bottom-border text-right disabledTh">审核意见</th>
          			<td class="right-border bottom-border"  colspan="7">
          			<textarea rows="3" class="span12"  check-type="required" placeholder="必填"    id="SHYJ"  name = "SHYJ" fieldname="SHYJ"></textarea>
          			</td>
        		</tr>
       			<tr class="ytsh">
          			<th class="right-border bottom-border text-right disabledTh">会次</th>
            		<td class="right-border bottom-border ">
            			 <input class="span12 person" type="text" disabled id="HC" name = "HC" fieldname= "HC" >
           			</td>
        		    <th class="right-border bottom-border text-right disabledTh">会议时间</th>
            		<td  class="right-border bottom-border " >
            			<input class="span12" type="text" kind="date" disabled check-type="" id="HYSJ" name = "HYSJ" fieldname="HYSJ">
           			</td><td colspan="4"></td>
        		</tr>
        		<tr class="ytsh">
          			<th class="right-border bottom-border text-right disabledTh">会议结论</th>
          			<td class="right-border bottom-border"  colspan="7">
          			<textarea rows="3" class="span12"  disabled check-type="required" placeholder=""    id="BGHJL"  name = "BGHJL" fieldname="BGHJL"></textarea>
          			</td>
        		</tr>
        		<tr class="ytsh">
          			<th class="right-border bottom-border text-right disabledTh">会议回复</th>
          			<td class="right-border bottom-border"  colspan="7">
          			<textarea rows="3" class="span12"  disabled check-type="required" placeholder=""    id="SHYJ"  name = "BGHHF" fieldname="BGHHF"></textarea>
          			</td>
        		</tr>
        		<tr class="ytsh">
          			<th class="right-border bottom-border text-right disabledTh" width="7%"> 主责部门</th>
            		<td class="right-border bottom-border "  width="18%">
           			 <input type="text" class="span12 department"  disabled check-type="required"  name="ZZBM" fieldname="ZZBM" id="ZZBM" kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME"  onchange="xuzheren(this)">
           			</td>
        		    <th class="right-border bottom-border text-right disabledTh" width="7%">主责人</th>
            		<td  class="right-border bottom-border " width="18%">
            			<input type="text" class="span12 person" id="ZZR" disabled name = "ZZR" check-type="required" fieldname = "ZZR" operation="=" kind="dic" src="T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG='1'  AND PERSON_KIND = '3'  order by sort">
               		</td>
               		<th class="right-border bottom-border text-right disabledTh" width="7%">主责中心领导</th>
            		<td class="right-border bottom-border " width="18%">
            			 <input  type="text" class="span12" kind="dic"  disabled id="ZZZXLD" name = "ZZZXLD" fieldname= "ZZZXLD" src="T#fs_org_person:ACCOUNT:NAME:PERSON_KIND = '3' AND FLAG='1'  order by sort" >
           			</td>
        		    <th class="right-border bottom-border text-right disabledTh" width="7%">督办时限</th>
            		<td  class="right-border bottom-border " width="18%">
            			<input class="span12"  type="text" kind="date" disabled  check-type="" id="YQJJSJ" name = "YQJJSJ" fieldname="YQJJSJ">
            		</td>
        		</tr>
       			<tr class="ytsh">
          			<th class="right-border bottom-border text-right disabledTh">督办次数</th>
            		<td class="right-border bottom-border ">
            			<input type="text" class="span12 person"  id="DBCS" disabled name = "DBCS" fieldname= "DBCS" >
           			</td>
        		    <th class="right-border bottom-border text-right disabledTh">是否解决</th>
            		<td  class="right-border bottom-border " >
            			<input type="text" class="span12 person" kind="dic" disabled id="ISJJ" name = "ISJJ" fieldname= "ISJJ" src="SF" >
           			 </td>
           			 <th class="right-border bottom-border text-right disabledTh">解决时间</th>
          			<td class="right-border bottom-border"  >
          				<input class="span12"  type="text" kind="date" disabled check-type="" id="JJSJ" name = "JJSJ" fieldname="JJSJ">
          			</td>
          			<td colspan="2"></td>
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