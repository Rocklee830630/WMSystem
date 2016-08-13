<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title></title>
<script type="text/javascript" charset="utf-8">
	var controllername = "${pageContext.request.contextPath }/sjgl/sj/sjController.do";
	var info;			//反馈使用
	function do_value(){
		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.$("#DT1").getSelectedRow();
		var obj = convertJson.string2json1(rowValue);
		return obj;
	}
	//初始化加载
	$(document).ready(function() {
		setValue();
		$("#cqt_Feedback").click(function(){
			if($("#cqtForm").validationButton()){
				//生成json串
				var data = Form2Json.formToJSON(cqtForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				defaultJson.doUpdateJson(controllername+"?Feedback&lb=1"+info,data1,null,"par_feedback");
				/* par_feedback(); */
				}else{
			  		defaultJson.clearTxtXML();
			}
		});
		$("#sgt_Feedback").click(function(){
			if($("#sgtForm").validationButton()){
				//生成json串
				var data = Form2Json.formToJSON(sgtForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				defaultJson.doUpdateJson(controllername+"?Feedback&lb=2"+info,data1,null,"par_feedback");
				/* par_feedback(); */
				}else{
			  		defaultJson.clearTxtXML();
			}
		});
		$("#pqt_Feedback").click(function(){
			if($("#pqtForm").validationButton()){
				//生成json串
				var data = Form2Json.formToJSON(pqtForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				defaultJson.doUpdateJson(controllername+"?Feedback&lb=3"+info,data1,null,"par_feedback");
				/* par_feedback(); */
				}else{
			  		defaultJson.clearTxtXML();
			}
		});
		$("#gs_Feedback").click(function(){
			if($("#gsForm").validationButton()){
				//生成json串
				var data = Form2Json.formToJSON(gsForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				defaultJson.doUpdateJson(controllername+"?Feedback&lb=4"+info,data1,null,"gsPar_feedback");
				/* var fa_page=$(window).manhuaDialog.getParentObj();
				fa_page.gsPar_feedback(); */
				}else{
			  		defaultJson.clearTxtXML();
			}
		});
		$("#jjg_Feedback").click(function(){
			if($("#jjgForm").validationButton()){
				//生成json串
				var data = Form2Json.formToJSON(jjgForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				defaultJson.doUpdateJson(controllername+"?Feedback&lb=5"+info,data1,null,"par_feedback");
				}else{
			  		defaultJson.clearTxtXML();
			}
		});
	});
	//父页面回显方法
	function par_feedback(){
		var fa_page=$(window).manhuaDialog.getParentObj();
		fa_page.xiugaihang();
	}
	//概算父页面回显
	function gsPar_feedback(){
		var fa_page=$(window).manhuaDialog.getParentObj();
		fa_page.gsPar_feedback();
	}
	//页面初始化赋值
	function setValue(){
		var json=do_value();
		//设计表主键（反馈使用）
		sjid=$(json).attr("GC_SJ_ID");
		//计划主体表主键
		jhid=$(json).attr("JHID");
		//显示设计的sjbh
		sjbh=$(json).attr("SJ_SJBH");
		info="&sjid="+sjid+"&jhid="+jhid+"&sjbh="+sjbh+"&xmid="+json.XMID+"&bdid="+json.BDID+"&nd="+json.ND;
		//显示项目名称
		$("#XMMC").val($(json).attr("XMMC"));
		//显示计划时间
		$("#JHCQT").val($(json).attr("JHCQT"));
		$("#JHPQT").val($(json).attr("JHPQT"));
		$("#JHSGT").val($(json).attr("JHSGT"));
		$("#JHGS").val($(json).attr("JHGS"));
		//显示备注信息
		$("#CBSJPF_BZ").val(json.CBSJPF_BZ);
		$("#CQT_BZ").val(json.CQT_BZ);
		$("#PQT_BZ").val(json.PQT_BZ);
		$("#SGT_BZ").val(json.SGT_BZ);
		
		var bdmc=$(json).attr("BDMC");
		if(bdmc==null||bdmc==""){
			$("#BDMC").val('所选数据不包含标段信息');
	        $("#BDMC").attr("style","color:red;");
		}else{
			$("#BDMC").val(bdmc);
		}
		//显示计划数据表主键	
		var jhsjid=$(json).attr("JHSJID");
		//var gsjhid=$(json).attr("GSJHID");
		$("#cqt_JHSJID").val(jhsjid);
		$("#pqt_JHSJID").val(jhsjid);
		$("#sgt_JHSJID").val(jhsjid);
		$("#gs_JHSJID").val(jhsjid);
		$("#jg_JHSJID").val(jhsjid);
		/* $("#gs_JHSJID").val(gsjhid); */
		cqt_date();
		pqt_date();
		sgt_date();
		gs_date();
		jg_date();
		//do_hide();
	}
	//隐藏反馈按钮，并且转化为disabled
	function do_hide(){
		var json=do_value();
		var bdid=$(json).attr("BDID");
		if(bdid==null||bdid==""){
			
		}else{
			$("#gs_Feedback").hide();
			$("#GS").attr("disabled",true);
		}
	}
	//显示概算时间
 	function gs_date(){
		var json=do_value();
		var gs_wc=$(json).attr("WCSJ_YS");
		//一下注释的是概算按照项目进行反馈的时候，应该显示的时间
		//var gs_sj=$(json).attr("SJGS");
		var gs_sj=$(json).attr("CBSJPF_SJ");
		var gs=setDate(gs_wc,gs_sj);
		$("#GS").val(gs);
	}
	//显示拆迁图时间
 	function cqt_date(){
		var json=do_value();
		var cqt_wc=$(json).attr("WCSJ_CQT");
		var cqt_sj=$(json).attr("CQT_SJ");
		var cqt=setDate(cqt_wc,cqt_sj);
		$("#CQT").val(cqt);
	}
	//显示排迁图时间
	function pqt_date(){
		var json=do_value();
		var pqt_wc=$(json).attr("WCSJ_PQT");
		var pqt_sj=$(json).attr("PQT_SJ");
		var pqt=setDate(pqt_wc,pqt_sj);
		$("#PQT").val(pqt);
	}
	//显示施工图时间
	function sgt_date(){	
		var json=do_value();
		var sgt_wc=$(json).attr("WCSJ_SGT_ZSB");
		var sgt_sj=$(json).attr("SGT_SJ");
		var sgt=setDate(sgt_wc,sgt_sj);
		$("#SGT").val(sgt);
	}
	//显示交工时间
	function jg_date(){	
		var json=do_value();
		var jiaog_wc=$(json).attr("JIAOGSJ");
		var jung_wc=$(json).attr("JUNGSJ");
		var jiaog_sj=$(json).attr("JG_SJ");
		var jung_sj=$(json).attr("JS_SJ");
		var jiaog=setDate(jiaog_wc,jiaog_sj);
		var jung=setDate(jung_wc,jung_sj);
		$("#JIAOG").val(jiaog);
		$("#JUNG").val(jung);
	}
	//页面时间赋值方法
	function setDate(wc,sj){
		var date=sj?sj:wc;
		return date;
	}
	//页面链接方法
	function doLook(tzlb){
		var json=do_value();
		var jhsjid=$(json).attr("JHSJID");
		$(window).manhuaDialog({"title":"详细信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/sjgl/sj/feed_look.jsp?jhsjid="+jhsjid+"&tzlb="+tzlb,"modal":"4"});		
	}
	//概算链接
	function gs_look(){
		var json=do_value();
		json=JSON.stringify(json);
		json=encodeURI(json);
		$(window).manhuaDialog({"title":"详细信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/sjgl/sj/gs_feed_look.jsp","modal":"4"});		
	}
	//概算链接
	function jjg_look(){
		var json=do_value();
		var jhsjid=json.JHSJID;
		var jiaog_wc=$(json).attr("JIAOGSJ");
		//交工时间为主
		var urlValue=jiaog_wc?("jiaog_feed_look"):("jung_feed_look");
		var title=jiaog_wc?("交工"):("竣工");
		$(window).manhuaDialog({"title":title,"type":"text","content":"${pageContext.request.contextPath}/jsp/business/sjgl/sj/"+urlValue+".jsp?jhsjid="+jhsjid,"modal":"2"});		
	}
</script>
</head>
<body>
	<app:dialogs />
	<div class="container-fluid">
		<p></p>
		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
				<h4 class="title">
					项目信息 <span class="pull-right">
					</span>
				</h4>
<form method="post" id="demoForm"  >
	<table class="B-table" width="100%">
		<tr id="tr1">
			<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
			<td width="40%" class="right-border bottom-border">
			   <input class="span12" keep="true" id="XMMC" disabled type="text" name="XMMC">
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
			<td width="40%" class="right-border bottom-border">
			   <input class="span12" keep="true" id="BDMC" disabled type="text" name="BDMC">
			</td>
		</tr>
	</table>
	</form>
	<form method="post" id="gsForm"  >	
	<h4 class="title">概算 <span class="pull-right">
			<button id="gs_Feedback" name="gssave" class="btn" type="button">提交</button>
		</span>
	</h4>
	<table class="B-table" width="80%" >
			<TR style="display: none;">
			<!-- <TR> -->
			<TD><input type="text" class="span12" kind="text" keep="true" name="JHSJID" id="gs_JHSJID" fieldname="GC_JH_SJ_ID">
			</TD>
		</TR>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">计划完成时间</th>
			<td width="10%" class="right-border bottom-border">
			    <input class="span12" type="text" id="JHGS" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right">实际完成时间</th>
			<td width="20%" class="right-border bottom-border">
			    <input class="span12 date" type="date" id="GS" fieldname="CBSJPF_SJ" name="CBSJPF_SJ" check-type="required" />
			</td>
			<td class="right-border bottom-border" colspan="1">
				<!-- <button class="btn btn-link"  title="查看进展情况" type="button" id="gs" onclick="gs_look()"><i class="icon-edit"></i></button> -->
				<a href='javascript:void(0)' onclick="gs_look()"><i class="icon-list"></i>查看反馈情况</a>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">概算备注</th>
			<td colspan="4" width="54%" class="right-border bottom-border">
			    <textarea class="span12" fieldname="CBSJPF_BZ" id="CBSJPF_BZ" maxlength="2000" check-type="maxlength"></textarea>
			</td>
		</tr>
	</table>
	</form>
<form method="post" id="cqtForm"  >	
	<h4 class="title">拆迁图 <span class="pull-right">
			<button id="cqt_Feedback" name="cqtsave" class="btn" type="button">提交</button>
		</span>
	</h4>
	<table class="B-table" width="80%" >
			<TR style="display: none;">
			<!-- <TR> -->
			<TD><input type="text" class="span12" kind="text" keep="true" name="JHSJID" id="cqt_JHSJID" fieldname="GC_JH_SJ_ID">
			</TD>
		</TR>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">计划完成时间</th>
			<td width="10%" class="right-border bottom-border">
			    <input class="span12" type="text" id="JHCQT" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right">实际完成时间</th>
			<td width="20%" class="right-border bottom-border">
			    <input class="span12 date" type="date" id="CQT" fieldname="CQT_SJ" name="CQT_SJ" check-type="required" />
			</td>
			<td class="right-border bottom-border" colspan="1">
				<!-- <button class="btn btn-link"  title="查看进展情况" type="button" id="cqt" onclick="doLook(0)"><i class="icon-edit"></i></button> -->
				<a href='javascript:void(0)' onclick="doLook(3)"><i class="icon-list"></i>查看反馈情况</a>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">拆迁图备注</th>
			<td colspan="4" width="54%" class="right-border bottom-border">
			    <textarea class="span12" fieldname="CQT_BZ" id="CQT_BZ" maxlength="2000" check-type="maxlength"></textarea>
			</td>
		</tr>
	</table>
	</form>
<form method="post" id="pqtForm"  >
	<h4 class="title">排迁图<span class="pull-right">
			<button id="pqt_Feedback" name="pqtsave" class="btn" type="button">提交</button>
		</span>
	</h4>
	<table class="B-table" width="80%" >
		<TR style="display: none;">
		<!-- <TR> -->
			<TD><input type="text" class="span12" kind="text" id="pqt_JHSJID" keep="true" fieldname="GC_JH_SJ_ID">
			</TD>
		</TR>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">计划完成时间</th>
			<td width="10%" class="right-border bottom-border">
			    <input class="span12" type="text" id="JHPQT" disabled style="disable:true;" />
			</td>
			<th width="8%" class="right-border bottom-border text-right">实际完成时间</th>
			<td width="20%" class="right-border bottom-border">
			    <input class="span12 date" type="date" id="PQT" fieldname="PQT_SJ" name="PQT_SJ" check-type="required" />
			</td>
			<td class="right-border bottom-border">
				<!-- <button class="btn btn-link"  title="查看进展情况" type="button" id="pqt" onclick="doLook(1)"><i class="icon-edit"></i></button> -->
				<a href='javascript:void(0)' onclick="doLook(4)"><i class="icon-list"></i>查看反馈情况</a>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">排迁图图备注</th>
			<td colspan="4" width="54%" class="right-border bottom-border">
			    <textarea class="span12" fieldname="PQT_BZ" id="PQT_BZ" maxlength="2000" check-type="maxlength"></textarea>
			</td>
		</tr>
	</table>
</form>
<form method="post" id="sgtForm"  >
	<h4 class="title">施工图<span class="pull-right">
			<button id="sgt_Feedback" name="sgtsave" class="btn" type="button">提交</button>
		</span>
	</h4>
	<table class="B-table" width="80%" >
	    <TR style="display: none;">
	    <!-- <TR> -->
			<TD><input type="text" class="span12" kind="text" keep="true" id="sgt_JHSJID" fieldname="GC_JH_SJ_ID">
			</TD>
		</TR>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">计划完成时间</th>
			<td width="10%" class="right-border bottom-border">
			    <input class="span12" type="text" id="JHSGT" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right">实际完成时间</th>
			<td width="20%" class="right-border bottom-border">
			    <input class="span12 date" type="date" id="SGT" fieldname="SGT_SJ" name="SGT_SJ"  check-type="required" />
			</td>
			<td class="right-border bottom-border">
				<!-- <button class="btn btn-link"  title="查看进展情况" type="button" id="sgt" onclick="doLook(3)"><i class="icon-edit"></i></button> -->
				<a href='javascript:void(0)' onclick="doLook(7)"><i class="icon-list"></i>查看反馈情况</a>
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">施工图备注</th>
			<td colspan="4" width="54%" class="right-border bottom-border">
			    <textarea class="span12" fieldname="SGT_BZ" id="SGT_BZ" maxlength="2000" check-type="maxlength"></textarea>
			</td>
		</tr>
	</table>
  </form>
  <form method="post" id="jjgForm"  >
	<h4 class="title">交竣工<span class="pull-right">
			<button id="jjg_Feedback" name="jjgsave" class="btn" type="button">提交</button>
		</span>
	</h4>
	<table class="B-table" width="80%" >
	    <TR style="display: none;">
	    <!-- <TR> -->
			<TD><input type="text" class="span12" kind="text" keep="true" id="jg_JHSJID" fieldname="GC_JH_SJ_ID">
			</TD>
		</TR>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">计划完成时间</th>
			<td width="10%" class="right-border bottom-border">
			    <input class="span12" type="text" id="JHJG" disabled/>
			</td>
			<th width="8%" class="right-border bottom-border text-right">交工时间</th>
			<td width="20%" class="right-border bottom-border">
			    <input class="span12 date" type="date" id="JIAOG" fieldname="JG_SJ" name="JG_SJ"  check-type="" />
			</td>
			<th width="8%" class="right-border bottom-border text-right">竣工时间</th>
			<td width="20%" class="right-border bottom-border">
			    <input class="span12 date" type="date" id="JUNG" fieldname="JS_SJ" name="JS_SJ"  check-type="" />
			</td>
			<!-- <td class="right-border bottom-border">
				<button class="btn btn-link"  title="查看进展情况" type="button" id="sgt" onclick="doLook(3)"><i class="icon-edit"></i></button>
				<a href='javascript:void(0)' onclick="jjg_look(7)"><i class="icon-list"></i>查看反馈情况</a>
			</td> -->
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">备注</th>
			<td colspan="5" width="54%" class="right-border bottom-border">
			    <textarea class="span12" fieldname="JG_BZ" id="JG_BZ" maxlength="2000" check-type="maxlength"></textarea>
			</td>
		</tr>
	</table>
  </form>
</div>
</div>
</div>
	<div align="center">
		<FORM name="frmPost" method="post" style="display: none" target="_blank" id="frmPost">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML"> 
			<input type="hidden" name="txtXML" id="txtXML"> 
			<input type="hidden" name="resultXML" id="resultXML"> 
			<input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ" />
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
			<input type="hidden" name="queryResult" id="queryResult">

		</FORM>
	</div>
</body>
</html>