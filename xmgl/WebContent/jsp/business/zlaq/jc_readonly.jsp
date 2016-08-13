<%@ page language="java" pageEncoding="UTF-8"%>
	<input type="hidden" id="ywid"/>
	<input type="hidden" id="GC_ZLAQ_JCB_ID" fieldname="GC_ZLAQ_JCB_ID" name="GC_ZLAQ_JCB_ID"/>
	<input type="hidden" id="GC_ZLAQ_ZGB_ID" fieldname="GC_ZLAQ_ZGB_ID" name="GC_ZLAQ_ZGB_ID"/>
	<input type="hidden" id="GC_JH_SJ_ID" fieldname="GC_JH_SJ_ID" name="GC_JH_SJ_ID"/>
	<input type="hidden" id="SJBH" fieldname="SJBH" name="SJBH"/>
	<input type="hidden" id="XDKID" fieldname="XDKID" name="XDKID"/>
	<input type="hidden" id="GC_XMBD_ID" fieldname="GC_XMBD_ID" name="GC_XMBD_ID"/>
	<input type="hidden" id="XMBH" fieldname="XMBH" name="XMBH"/>
	<input type="hidden" id="BDID" fieldname="BDID" name="BDID"/>
	<input type="hidden" id="ND" fieldname="ND" name="ND"/>
	<input type="hidden" id="YWLX" fieldname="YWLX" name="YWLX"/>
	<tr>
		<th class="right-border bottom-border text-right">检查类型</th>
		<td class="right-border bottom-border">
			<input type="text" class="span12 4characters"  style="width:30%;"  id="JCLX" fieldname="JCLX" name="JCLX" disabled/>
			&nbsp;&nbsp;&nbsp;&nbsp;质量
			<input class="span12"  style="width:10%;" id="ZJBS" type="checkbox"  name="ZJBS" fieldname="ZJBS" disabled>
			安全
			<input class="span12"  style="width:10%;" id="AJBS" type="checkbox"  name="AJBS" fieldname="AJBS" disabled>
		<th class="right-border bottom-border text-right">检查日期</th>
		<td class="bottom-border">
			<input id="JCRQ" class="span12 date" type="date" name="JCRQ" fieldname="JCRQ" disabled/>
		</td>
		<th class="right-border bottom-border text-right">检查方式</th>
		<td class="right-border bottom-border">
			<input type="text" class="span12 4characters" id="JCGM" fieldname="JCGM" name="JCGM" disabled/>
		</td>
	</tr>
	<tr>    		
		<th class="right-border bottom-border text-right">检查编号</th>
		<td class="right-border bottom-border">
			<input id="JCBH" class="span12" type="text" name="JCBH" fieldname="JCBH" disabled/>
		</td>	 																									
		<th class="right-border bottom-border text-right">组织单位</th>
		<td class="bottom-border">
			<select id="JCBM" class="span12" kind="dic" src="T#GC_CJDW:GC_CJDW_ID:DWMC:DWLX='8'" name="JCBM"  keep="true" fieldname="JCBM" disabled></select>
		</td>
		<th class="right-border bottom-border text-right">参与人</th>
		<td class="bottom-border">
			<input id="JCR" class="span12" type="text" name="JCR"  fieldname="JCR" disabled/>
		</td>								
	<tr>
		<th width="8%" class="right-border bottom-border text-right">检测单位</th>
		<td width="92%" colspan="7" class="bottom-border">
			<textarea class="span12" rows="2" id="JCDW"  fieldname="JCDW" name="JCDW" disabled></textarea>
		</td>
	</tr>	
	<tr>					
		<th width="8%" class="right-border bottom-border text-right">检查内容</th>
		<td width="92%" colspan="7" class="bottom-border">
			<textarea class="span12" rows="4" id="JCNR"  fieldname="JCNR" name="JCNR" disabled></textarea>
		</td>
	</tr>
	<tr>
		<th width="8%" class="right-border bottom-border text-right">存在的问题</th>
		<td width="92%" colspan="7" class="bottom-border">
			<textarea class="span12" rows="4" id="CZWT"  fieldname="CZWT" name="CZWT" disabled></textarea>
		</td>
	</tr>
    <tr>
       	<th width="8%" class="right-border bottom-border text-right">附件信息</th>
       	<td colspan="7" class="bottom-border right-border">
			<div>
				<table role="presentation" class="table table-striped">
					<tbody fjlb="0032" class="files showFileTab" onlyView="true" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
				</table>
			</div>
		</td>
	</tr>				
	<tr>
		<th class="right-border bottom-border text-right">是否需要整改</th>
		<td class="right-border bottom-border">
			<input type="text" class="span12 3characters" style="width:20%" id="ISCZWT" fieldname="ISCZWT" name="ISCZWT" disabled/>
		</td>
		<td colspan="4"></td>
	</tr>
	