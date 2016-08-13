<%@ page language="java" pageEncoding="UTF-8"%>
	<tr>
		<th width="7%" class="right-border bottom-border text-right">整改编号</th>
		<td width="26%" class="right-border bottom-border">
			<input id="ZGBH" class="span12" type="text" name="ZGBH" fieldname="ZGBH"  disabled/>
		</td>	 																									
		<th width="7%" class="right-border bottom-border text-right">发送日期</th>
		<td width="26%" class="right-border bottom-border">
			<input class="span12 date" name ="TZRQ" id="TZRQ" fieldname="TZRQ" type="date" disabled/>
		</td>
		<th width="7%" class="right-border bottom-border text-right">限改日期</th>
		<td width="26%" class="bottom-border">
			<input class="span12 date" name ="XGRQ" id="XGRQ" fieldname= "XGRQ" min="0" type="date" disabled/>
		</td>
	</tr>  	
	<tr>
		<th class="right-border bottom-border text-right">整改意见</th>
		<td colspan="5" class="bottom-border">
			<textarea class="span12" id="CLJY" name="CLJY" rows="4" fieldname="CLJY" disabled></textarea>
		</td>
	</tr>
    <tr>
       	<th class="right-border bottom-border text-right">整改附件</th>
       	<td colspan="5" class="bottom-border right-border">
			<div>
				<table role="presentation" class="table table-striped">
					<tbody fjlb="0033" class="files showFileTab" data-toggle="modal-gallery" onlyView="true" data-target="#modal-gallery"></tbody>
				</table>
			</div>
		</td>
     </tr>															
