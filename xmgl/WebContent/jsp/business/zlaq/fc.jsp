<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%
	User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
	String username = user.getName();
	String department = user.getDepartment();
	String userid= user.getAccount();
%>
<script type="text/javascript">
//设置复查人
$(document).ready(function(){
	var userid='<%=userid%>';
	var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
	var obj=convertJson.string2json1(rowValue);
	if(obj.FCR!='')
	{
		$("#FCR").val(obj.FCR)
	}	
	else
	{
		setDefaultOption($("#FCR"),userid);
	}	
});

</script>
 	<tr>
		<th width="7%" class="right-border bottom-border text-right disabledTh">复查编号</th>
		<td width="18%" class="right-border bottom-border">
			<input id="FCBH" class="span12" type="text" name="FCBH" fieldname="FCBH" check-type="maxlength"  maxlength="100" disabled/>
		</td>	 																									
		<th width="7%" class="right-border bottom-border text-right">复查人</th>
		<td width="18%" class="right-border bottom-border">
			<select class="span12 3characters" kind="dic" id="FCR" name="FCR" check-type="maxlength" maxlength="36" fieldname= "FCR" src="T#fs_org_person:ACCOUNT:NAME:DEPARTMENT= '<%=department%>'  AND PERSON_KIND = '3' AND FLAG='1' order by sort" ></select>
		</td>
		<th width="7%" class="right-border bottom-border text-right">复查日期</th>
		<td width="18%" class="right-border bottom-border">
			<input class="span12 date" name="FCRQ" id="FCRQ" fieldname="FCRQ" type="date" placeholder="必填" check-type="required" />
		</td>
		<td colspan="2"></td>
	</tr>
	<tr>
		<th class="right-border bottom-border text-right">复查意见</th>
		<td colspan="7" class="bottom-border">
			<textarea class="span12" id="FCYJ" rows="4" name="FCYJ" placeholder="必填" check-type="required maxlength" maxlength="4000" fieldname="FCYJ"></textarea>
		</td>
	</tr>
   	<tr>
      	<th class="right-border bottom-border text-right">复查附件</th>
      	<td colspan="7" class="bottom-border right-border">
			<div>
				<span id="fctj" class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0034">
					<i class="icon-plus"></i>
					<span>添加文件...</span>
				</span>
				<table role="presentation" class="table table-striped">
					<tbody id="jcfc" fjlb="0034" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
				</table>
			</div>
		</td>
   </tr>													
 	<tr>
		<th width="7%" class="right-border bottom-border text-right">复查结论</th>
		<td width="18%" class="right-border bottom-border">
			<select class="span12 3characters" kind="dic" src="FCJL" id="FCJL" fieldname="FCJL"  placeholder="必填" check-type="required" name="FCJL"></select>
		</td>
		<td colspan="6"></td>
	</tr>
					