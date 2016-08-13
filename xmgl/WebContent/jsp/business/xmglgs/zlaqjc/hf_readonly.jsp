<%@ page language="java" pageEncoding="UTF-8"%>
	<tr>
		<th width="7%" class="right-border bottom-border text-right">回复编号</th>
		<td width="26%" class="right-border bottom-border">
			<input id="HFBH" class="span12" type="text" name="HFBH" fieldname="HFBH" disabled/>
		</td>	 																									
		<th width="7%" class="right-border bottom-border text-right">回复日期</th>
		<td width="26%" class="right-border bottom-border">
			<input class="span12 date" name ="HFRQ" id="HFRQ" fieldname="HFRQ" type="date" disabled/>
		</td>
		<td colspan="2"></td>
	</tr>
	<tr>
		<th class="right-border bottom-border text-right">整改情况回复</th>
		<td colspan="5" class="bottom-border">
			<textarea class="span12"  id="HFNR" name="HFNR" rows="4" fieldname="HFNR" disabled></textarea>
		</td>
	</tr>
    <tr>
       	<th class="right-border bottom-border text-right">回复附件</th>
       	<td colspan="5" class="bottom-border right-border">
			<div>
				<table role="presentation" class="table table-striped">
					<tbody fjlb="0031" class="files showFileTab" onlyView="true" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
				</table>
			</div>
		</td>
    </tr>													
