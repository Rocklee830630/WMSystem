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
		<th id="JCLX_TH" class="right-border bottom-border text-right">检查类型</th>
		<td class="right-border bottom-border">
			<select class="span12 4characters" style="width:30%;" kind="dic" src="JCLX" id="JCLX" fieldname="JCLX" placeholder="必填" check-type="required" keep="true" name="JCLX"></select>
			&nbsp;&nbsp;&nbsp;&nbsp;质量
			<input class="span12"  style="width:10%;" id="ZJBS" type="checkbox"  name="ZJBS" fieldname="ZJBS">
			安全
			<input class="span12"  style="width:10%;" id="AJBS" type="checkbox"  name="AJBS" fieldname="AJBS">
		</td>								
		<th id="JCRQ_TH" class="right-border bottom-border text-right">检查日期</th>
		<td class="bottom-border">
			<input id="JCRQ" class="span12 date" type="date" name="JCRQ"  placeholder="必填" check-type="required" keep="true" fieldname="JCRQ"/>
		</td>
		<th id="JCGM_TH" class="right-border bottom-border text-right">检查方式</th>
		<td class="right-border bottom-border">
			<select class="span12 4characters" kind="dic" src="JCGM" id="JCGM" fieldname="JCGM" placeholder="必填" check-type="required" keep="true" name="JCGM"></select>
		</td>
	</tr>
	<tr>    		
		<th class="right-border bottom-border text-right disabledTh">检查编号</th>
		<td class="right-border bottom-border">
			<input id="JCBH" class="span12" type="text" name="JCBH" placeholder="由检查类型自动生成" fieldname="JCBH" check-type="maxlength"  maxlength="100" disabled/>
		</td>	 																									
		<th id="JCBM_TH" class="right-border bottom-border text-right">组织单位</th>
		<td class="bottom-border">
			<div id="div_read_jcbm">
				<select id="JCBM" class="span12" kind="dic" src="T#GC_CJDW:GC_CJDW_ID:DWMC:DWLX='8'" name="JCBM" style="width:93%"  check-type="maxlength" maxlength="36" keep="true" fieldname="JCBM"></select>
				<a href="javascript:void(0)" title="点击选择单位">
					<i id="lydwSelect" selObj="JCBM" class="icon-edit" onclick="selectCjdw('lydwSelect');" dwlx="8" isLxSelect="1" ></i>
				</a>
			</div>
			<select id="JCBM_readonly" class="span12"  style="display:none;"  kind="dic" src="T#GC_CJDW:GC_CJDW_ID:DWMC:DWLX='8'" name="JCBM" check-type="maxlength" maxlength="36" keep="true" fieldname="JCBM_readonly" disabled></select>
		</td>
		<th id="JCR_TH" class="right-border bottom-border text-right">参与人</th>
		<td class="bottom-border">
			<input id="JCR" class="span12" type="text" name="JCR" check-type="maxlength" maxlength="500" keep="true" fieldname="JCR"/>
		</td>
	<tr>
		<th id="JCDW_TH" width="8%" class="right-border bottom-border text-right">检测单位</th>
		<td width="92%" colspan="5" class="bottom-border">
			<textarea class="span12" rows="2" id="JCDW" check-type="maxlength" maxlength="200" fieldname="JCDW" keep="true" name="JCDW"></textarea>
		</td>
	</tr>	
	<tr>					
		<th id="JCNR_TH" width="8%" class="right-border bottom-border text-right">检查内容</th>
		<td width="92%" colspan="5" class="bottom-border">
			<textarea class="span12" rows="4" id="JCNR" placeholder="必填" check-type="required maxlength" maxlength="4000" fieldname="JCNR" keep="true" name="JCNR"></textarea>
		</td>
	</tr>
	<tr>
		<th id="CZWT_TH" width="8%" class="right-border bottom-border text-right">存在的问题</th>
		<td width="92%" colspan="5" class="bottom-border">
			<textarea class="span12" rows="4" id="CZWT" check-type="maxlength" maxlength="4000" fieldname="CZWT" name="CZWT"></textarea>
		</td>
	</tr>
    <tr>
       	<th id="JCFJ_TH" class="right-border bottom-border text-right">附件信息</th>
       	<td colspan="5" class="bottom-border right-border">
			<div>
				<span id="jctj" class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0032">
					<i class="icon-plus"></i>
					<span>添加文件...</span>
				</span>
				<table role="presentation" class="table table-striped">
					<tbody id="jcfj" fjlb="0032" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
					<tbody id="jcfj_only" fjlb="0032" class="files showFileTab" onlyView="true" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
				</table>
			</div>
		</td>
	</tr>				
	<tr>
		<th id="ISCZWT_TH" class="right-border bottom-border text-right">是否需要整改</th>
		<td class="right-border bottom-border">
			<select class="span12 3characters"  style="width:30%" kind="dic" src="SF" id="ISCZWT" fieldname="ISCZWT" placeholder="必填" check-type="required" name="ISCZWT"></select>
		</td>
		<td colspan="4"></td>
	</tr>
	