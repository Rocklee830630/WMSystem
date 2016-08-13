<%@ page language="java" pageEncoding="UTF-8"%>
	<tr>
		<th width="7%" class="right-border bottom-border text-right disabledTh">整改编号</th>
		<td width="26%" class="right-border bottom-border">
			<input id="ZGBH" class="span12" type="text" name="ZGBH" fieldname="ZGBH" check-type="maxlength"  maxlength="100" disabled/>
		</td>	 																									
		<th id="TZRQ_TH" width="7%" class="right-border bottom-border text-right">发送日期</th>
		<td width="26%" class="right-border bottom-border">
			<input class="span12 date" name ="TZRQ" id="TZRQ" fieldname="TZRQ" placeholder="必填" check-type="" type="date"/>
		</td>
		<th id="XGRQ_TH" width="7%" class="right-border bottom-border text-right">限改日期</th>
		<td width="26%" class="bottom-border">
			<input class="span12 date" name ="XGRQ" id="XGRQ" fieldname= "XGRQ" placeholder="必填" check-type="" type="date"/>
		</td>
	</tr>  	
	<tr>
		<th id="CLJY_TH" class="right-border bottom-border text-right">整改意见</th>
		<td colspan="5" class="bottom-border">
			<textarea class="span12" id="CLJY" rows="4" name="CLJY" placeholder="必填" check-type="required maxlength" maxlength="4000" fieldname="CLJY"></textarea>
		</td>
	</tr>
    <tr>
       	<th id="ZGFJ_TH" class="right-border bottom-border text-right">整改附件</th>
       	<td colspan="5" class="bottom-border right-border">
			<div>
				<span id="tztj" class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0033">
					<i class="icon-plus"></i>
					<span>添加文件...</span>
				</span>
				<table role="presentation" class="table table-striped">
					<tbody id="jctz" fjlb="0033" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
					<tbody id="jctz_only" fjlb="0033" class="files showFileTab" onlyView="true" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
				</table>
			</div>
		</td>
     </tr>															
