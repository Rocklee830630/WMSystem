<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%
	User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
	String username = user.getName();
	String department = user.getDepartment();
%>
	<tr>
		<th width="7%" class="right-border bottom-border text-right">复查编号</th>
		<td width="18%" class="right-border bottom-border">
			<input id="FCBH" class="span12" type="text" name="FCBH" fieldname="FCBH" disabled/>
		</td>	 																									
		<th width="7%" class="right-border bottom-border text-right">复查人</th>
		<td width="18%" class="right-border bottom-border">
			<input type="text" class="span12 3characters" id="FCR" name="FCR" fieldname= "FCR" disabled/>
		</td>
		<th width="7%" class="right-border bottom-border text-right">复查日期</th>
		<td width="18%" class="right-border bottom-border">
			<input class="span12 date" name="FCRQ" id="FCRQ" fieldname="FCRQ" type="date" disabled/>
		</td>
		<td colspan="2"></td>
	</tr>
	<tr>
		<th class="right-border bottom-border text-right">复查意见</th>
		<td colspan="7" class="bottom-border">
			<textarea class="span12" id="FCYJ" rows="4" name="FCYJ" fieldname="FCYJ" disabled></textarea>
		</td>
	</tr>
	<tr>
       	<th class="right-border bottom-border text-right">复查附件</th>
       	<td colspan="7" class="bottom-border right-border">
			<div>
				<table role="presentation" class="table table-striped">
					<tbody fjlb="0034" class="files showFileTab" onlyView="true" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
				</table>
			</div>
		</td>
	</tr>													
	<tr>
		<th width="7%" class="right-border bottom-border text-right">复查结论</th>
		<td width="18%" class="right-border bottom-border">
			<input type="text" class="span12 3characters" id="FCJL" fieldname="FCJL" disabled  name="FCJL"/>
		</td>
		<td colspan="6"></td>
	</tr>
					