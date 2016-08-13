<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String xm_zt=request.getParameter("zt");
	String flag=request.getParameter("flag");
	int xm_isshow=0;
	if(!"undefined".equals(xm_zt)&&!"null".equals(xm_zt)&&xm_zt!=null)
	{
		xm_isshow=Integer.parseInt(xm_zt);
	}	
%>
	<tr>	
		<th class="right-border bottom-border text-right disabledTh">项目名称</th>
		<td width="30%" class="right-border bottom-border">
			<input class="span12" type="text" data-toggle="modal" placeholder="必填" check-type="required"  id="XMMC" name="XMMC" fieldname="XMMC" disabled/>
		</td>	
		<th id="BDMC_TH" width="7%" class="right-border bottom-border text-right">标段名称</th>
		<td width="30%" class="bottom-border">
		<%if(xm_isshow>1) {
		%>
			<input class="span12" type="text" id="BDMC" name="BDMC" fieldname="BDMC" disabled/>
		<%} else{
				if("1".equals(flag)){
		%>
					<input class="span12" type="text" id="BDMC" name="BDMC" fieldname="BDMC" disabled/>
				<%} else{%>
					<div id="div_read"><input class="span12" type="text" style="width:93%" id="BDMC" name="BDMC" fieldname="BDMC" disabled/>
					&nbsp;<a href="javascript:void(0);"><i  class="icon-edit" title="请选择项目" onclick="xzxm();"></i></a></div>
					<input class="span12" type="text" id="BDMC_readonly" name="BDMC" style="display:none;" fieldname="BDMC_readonly" disabled />
		<%}} %>	
		</td>
		<th width="7%" class="right-border bottom-border text-right disabledTh">工程部负责人</th><!-- ************* -->
		<td width="18%" class="right-border bottom-border">
			<input type="text" class="span12" id="YZDB"  fieldname="YZDB" name="YZDB" disabled/>
		</td>						
	</tr>						
	<tr>														
		<th class="right-border bottom-border text-right disabledTh">项目管理公司</th>
		<td class="right-border bottom-border">
			<input id="XMGLGS" class="span12" type="text" name="XMGLGS" fieldname="XMGLGS" disabled/>
		</td>
		<th class="right-border bottom-border text-right disabledTh">管理公司负责人</th>
		<td class="right-border bottom-border">
			<input id="FZR_GLGS" class="span12" type="text" name="FZR_GLGS" fieldname="FZR_GLGS" disabled/>
		</td>
		<th class="right-border bottom-border text-right disabledTh">管理公司联系方式</th>
		<td class="right-border bottom-border">
			<input id="LXFS_GLGS" class="span12" type="text" name="LXFS_GLGS" fieldname="LXFS_GLGS" disabled/>
		</td>												
	</tr>
	<tr>	
		<th class="right-border bottom-border text-right disabledTh">设计单位</th>
		<td class="bottom-border">
			<input class="span12" rows="2" type="text" id="SJDW" fieldname="SJDW" name="SJDW" disabled/>
		</td>
		<th class="right-border bottom-border text-right disabledTh">设计负责人</th>
		<td class="bottom-border">
			<input class="span12" rows="2" type="text" id="FZR_SJDW" fieldname="FZR_SJDW" name="FZR_SJDW" disabled/>
		</td>						
		<th class="right-border bottom-border text-right disabledTh">负责人联系方式</th>
		<td class="bottom-border">
			<input class="span12" rows="2" type="text" id="LXFS_SJDW" fieldname="LXFS_SJDW" name="LXFS_SJDW" disabled/>
		</td>											
	</tr>
	<tr>	
		<th class="right-border bottom-border text-right disabledTh">监理单位</th>
		<td class="right-border bottom-border">
			<input type="text" class="span12"  id="JLDW"  fieldname="JLDW" name="JLDW" disabled/>
		</td>
		<th class="right-border bottom-border text-right disabledTh">总监</th>
		<td class="right-border bottom-border">
			<input type="text" class="span12"  id="JLDWZJ" fieldname="JLDWZJ" name="JLDWZJ" disabled/>
		</td>
		<th class="right-border bottom-border text-right disabledTh">总监联系方式</th>
		<td class="right-border bottom-border">
			<input type="text" class="span12" id="JLDWZJLXDH" fieldname="JLDWZJLXDH" name="JLDWZJLXDH" disabled/>
		</td>												
	</tr>
	<tr>	
		<th class="right-border bottom-border text-right disabledTh">施工单位</th>
		<td width=18%" class="right-border bottom-border">
			<input type="text" class="span12" id="SGDW" fieldname="SGDW" name="SGDW" disabled/>
		</td>					
		<th class="right-border bottom-border text-right disabledTh">项目经理</th>
		<td class="right-border bottom-border">
			<input type="text" class="span12" id="SGDWXMJL" fieldname="SGDWXMJL" name="SGDWXMJL" disabled/>
		</td>						
		<th class="right-border bottom-border text-right disabledTh">经理联系方式</th>
		<td class="right-border bottom-border">
			<input type="text" class="span12" id="SGDWXMJLLXDH" fieldname="SGDWXMJLLXDH" name="SGDWXMJLLXDH" disabled/>
		</td>						
	</tr>
