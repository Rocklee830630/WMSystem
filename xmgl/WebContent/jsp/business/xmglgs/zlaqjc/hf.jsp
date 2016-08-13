<%@ page language="java" pageEncoding="UTF-8"%>
	<tr>
		<th width="7%" class="right-border bottom-border text-right disabledTh">回复编号</th>
		<td width="26%" class="right-border bottom-border">
			<input id="HFBH" class="span12" type="text" name="HFBH" fieldname="HFBH" check-type="maxlength"  maxlength="100" disabled/>
		</td>	 																									
		<th id="HFRQ_TH" width="7%" class="right-border bottom-border text-right">回复日期</th>
		<td width="26%" class="right-border bottom-border">
			<input class="span12 date" name ="HFRQ" id="HFRQ" fieldname="HFRQ" placeholder="必填" check-type="required" type="date"/>
		</td>
		<td colspan="2"></td>
	</tr>
	<tr>
		<th id="HFNR_TH" class="right-border bottom-border text-right">整改情况回复</th>
		<td colspan="5" class="bottom-border">
			<textarea class="span12" rows="2" id="HFNR" name="HFNR" rows="4" placeholder="必填" check-type="required maxlength" maxlength="4000" fieldname="HFNR"></textarea>
		</td>
	</tr>
    <tr>
       	<th id="HFFJ_TH" class="right-border bottom-border text-right">回复附件</th>
       	<td colspan="5" class="bottom-border right-border">
			<div>
				<span id="hftj" class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0031">
					<i class="icon-plus"></i>
					<span>添加文件...</span>
				</span>
				<table role="presentation" class="table table-striped">
					<tbody id="jchf" fjlb="0031" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
					<tbody id="jchf_only" fjlb="0031" onlyView="true" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
				</table>
			</div>
		</td>
    </tr>													
