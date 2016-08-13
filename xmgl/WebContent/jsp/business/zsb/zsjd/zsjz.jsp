<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat"%>
<app:base />
<title>任务反馈</title>
<script type="text/javascript" charset="utf-8">
//g_bAlertWhenNoResult = false;
var controllername= "${pageContext.request.contextPath }/zsb/jdb/jdbController.do";
var tr_sbrq,count=0;
//获取父页面的值
function do_value(){
	var pwindow =$(window).manhuaDialog.getParentObj();
	var rowValue = pwindow.$("#DT1").getSelectedRow();
	var obj = convertJson.string2json1(rowValue);
	return obj;
}
var index;		//行选信息
function setvalue(){
	var value=do_value();
	def_date();
	var zdxxid=$(value).attr("GC_ZSB_XXB_ID");
	$("#S_ZDXXID").val(zdxxid);
	$("#E_ZDXXID").val(zdxxid);
	$("#Q_ZDXXID").val(zdxxid);
	$("#insertForm").setFormValues(value);
	$("#S_WTJFX").val("");
	
	$("#insertForm1").setFormValues(value);
	$("#E_WTJFX").val("");
}


//操作后更新累计数目
function opera(){
	//本次居民
	$("#S_BCWCJMS").val('0');
	//本次企业
	$("#S_BCWCQYS").val('0');
	//本次土地
	$("#S_BCZDMJ").val('0');
	if($("#tabList").getTableRows()==0){
		//累计居民
		$("#E_LJJMZL").val(0);
		$("#S_LJJMZL").val(0);
		//累计企业
		$("#E_LJQYZL").val(0);
		$("#S_LJQYZL").val(0);
		//累计土地面积
		$("#E_LJZDMJ").val(0);
		$("#S_LJZDMJ").val(0);
	}else{
	$("#tabList").setSelect(0);
	var val = $("#tabList").getSelectedRow();
	val=convertJson.string2json1(val);
	$("#tabList").cancleSelected();

	//累计居民数
	var e_ljjm=Number($(val).attr("LJJMZL"));
	$("#E_LJJMZL").val(e_ljjm);
	$("#S_LJJMZL").val(e_ljjm);
	//累计企业
	var e_ljqy=Number($(val).attr("LJQYZL"));
	$("#E_LJQYZL").val(e_ljqy);
	$("#S_LJQYZL").val(e_ljqy);
	//累计土地面积
	var e_ljmj=Number($(val).attr("LJZDMJ"));
	$("#E_LJZDMJ").val(e_ljmj);
	$("#S_LJZDMJ").val(e_ljmj);
	}
	var value=do_value();
	var jmhs=$(value).attr("JMHS");
	var qyjs=$(value).attr("QYJS");
	var zmj=$(value).attr("ZMJ");
	dis_opera(jmhs,"#S_BCWCJMS","#E_BCWCJMS","#s_bcjm","#e_bcjm");
	dis_opera(qyjs,"#S_BCWCQYS","#E_BCWCQYS","#s_bcqy","#e_bcqy");
	dis_opera(zmj,"#S_BCZDMJ","#E_BCZDMJ","#s_bczd","#e_bczd");
}
//不可输入项
function dis_opera(value,s_fieldname,e_fieldname,s_th,e_th){
	if(""==value||null==value){
		count=Number(count)+1;
		$(s_fieldname).attr("disabled",true);
		$(e_fieldname).attr("disabled",true);
		$(s_fieldname).attr("type","text");
		$(e_fieldname).attr("type","text");
		$(s_th).addClass("disabledTh");
		$(e_th).addClass("disabledTh");
	}
	if(count==3){
		$("#btnAdd").attr("disabled",true);
		$("#btnSave").attr("disabled",true);
		$("#btnDel").attr("disabled",true);
	}
}
//查询基础进度信息
function doquery(){
	setvalue();
	//g_bAlertWhenNoResult=false;	
  	 	var data = combineQuery.getQueryCombineData(queryForm,frmPost,tabList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryJdb",data,tabList,null,true);
	//g_bAlertWhenNoResult=true;
};
//保存或修改校验
function do_check(operate){
	var check_value=true;
	var value=do_value();
	var jmxz=Number($(value).attr("JMHS"))-Number($("#E_LJJMZL").val());
	var qyxz=Number($(value).attr("QYJS"))-Number($("#E_LJQYZL").val());
	var mjxz=Number($(value).attr("ZMJ"))-Number($("#E_LJZDMJ").val());
	if(operate=="insert"){
		var bcjm=Number(jmxz)-Number($("#S_BCWCJMS").val());
		var bcqy=Number(qyxz)-Number($("#S_BCWCQYS").val());
		var bczd=Number(mjxz)-Number($("#S_BCZDMJ").val());
		if(bcjm<0||bcqy<0||bczd<0){
			check_value=false;
		}
	}else{
		var bcjm=Number(jmxz)-Number($("#E_BCWCJMS").val());
		var bcqy=Number(qyxz)-Number($("#E_BCWCQYS").val());
		var bczd=Number(mjxz)-Number($("#E_BCZDMJ").val());
		if(bcjm<0||bcqy<0||bczd<0){
			check_value=false;
		}
	}
	return check_value;
}
$(function(){
	doquery();
	opera();
	$("#btnAdd").click(function(){
		if($("#insertForm").validationButton()){
			var jdid=getZsjd($("#S_SBRQ").val());
	 		var data = Form2Json.formToJSON(insertForm);
			var data1 = defaultJson.packSaveJson(data);
			var a=do_check("insert");
			if(a){
				if(jdid==null||jdid==""){
						defaultJson.doInsertJson(controllername + "?insertJdb", data1,tabList,"callback");
					}else{
						xConfirm("提示信息","该时间下，您已经录入征收进展信息，是否要进行修改？");
						$('#ConfirmYesButton').unbind();
						$('#ConfirmCancleButton').unbind();
						$('#ConfirmYesButton').one("click",function(){
							$('#myTab a[href="#tab1"]').tab('show');
							$("#insertForm").clearFormResult();
							def_date();
						});
						$('#ConfirmCancleButton').one("click",function(){
			                $("#S_SBRQ").val("");   
			            });
					}
				}else{
					xFailMsg("录入的本次征收信息溢出，无法继续操作！","");
					return;
				}
		}else{
	  		defaultJson.clearTxtXML();
		}
	});
	$("#btnSave").click(function(){
		if($("#JDBID").val()==""||$("#JDBID").val()==null){
				requireSelectedOneRow();
				return;
			}else{
			if($("#insertForm1").validationButton()){
				var data = Form2Json.formToJSON(insertForm1);
				var data1 = defaultJson.packSaveJson(data);
				var up_sbrq=$("#E_SBRQ").val();
				var a=do_check("update");
				if(a){
					if(up_sbrq==tr_sbrq){
						defaultJson.doUpdateJson(controllername + "?updateJdb", data1,tabList,"callback");
					}else{
						var jdid=getZsjd(up_sbrq);
						if(null==jdid||""==jdid){
							defaultJson.doUpdateJson(controllername + "?updateJdb", data1,tabList,"callback");
						}else{
							xConfirm("提示信息","该时间下，您已经录入征收进展信息，请重新确定时间！");
							$('#ConfirmYesButton').unbind();
						}
					}
					}else{
						xFailMsg("录入的本次征收信息溢出，无法继续操作！","");
						return;
					}
			}else{
		  		defaultJson.clearTxtXML();
			}
			}
			});
	$("#btnDel").click(function(){
		var indexa =	$("#tabList").getSelectedRowIndex();
		//生成json串
 		var data = Form2Json.formToJSON(insertForm1);
		var data1 = defaultJson.packSaveJson(data);
		if(indexa==-1){
			requireSelectedOneRow();
		return;
		}else{
		xConfirm("提示信息","是否确认删除！");
		$('#ConfirmYesButton').unbind();
		$('#ConfirmYesButton').one("click",function(){
			defaultJson.doDeleteJson(controllername+"?deleteJdxx",data1,tabList,callback()); 
			doquery();
			opera();
		});
			}
		});
	});
		//单击行事件
		function tr_click(obj,tabId){
			if(tabId=="tabList"){
				var rowValue = $("#"+tabId).getSelectedRow();//获得选中行的json 字符串
				var tempJson = convertJson.string2json1(rowValue);//字符串转JSON对象
				$("#insertForm1").setFormValues(tempJson);
				tr_sbrq=$(tempJson).attr("SBRQ");
			}
		}
		//获取当前时间段是否有数据
	function getZsjd(sbrq){
	//var sbrq = $("#S_SBRQ").val();
	var zdxxid=$("#S_ZDXXID").val();
	var str = "";
	$.ajax({
		url:controllername + "?getZsjd&sbrq="+sbrq+"&zdxxid="+zdxxid,
		data:"",
		dataType:"json",
		async:false,
		success:function(result){
			str = result.msg;
		}
	});
	return str;
}
	//默认时间
	function def_date(){
		var today=getCurrentDate();
		var s_date='{"SBRQ":\"'+today+'\","SBRQ_SV":\"'+today+'\"}';	
		var date=convertJson.string2json1(s_date);
		$("#insertForm").setFormValues(date);
	}
	//加载等待
	function callback(){
		var xxbid=$("#S_ZDXXID").val();
		var fuyemian=$(window).manhuaDialog.getParentObj();
		fuyemian.jzfk(xxbid);
		$("#insertForm").clearFormResult();
		$("#insertForm1").clearFormResult();
		doquery();
		opera();
	}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<ul class="nav nav-tabs" id="myTab">
	<li class="active">
		<a href="#tab0" data-toggle="tab" id="tabPage0">新增</a>
	</li>
	<li class="">
		<a href="#tab1" data-toggle="tab" id="tabPage1">查看及修改</a>
	</li>
</ul>
<div class="tab-content">
<div class="tab-pane active" id="tab0">
	<div class="row-fluid">
			<div class="B-small-from-table-autoConcise" style="width: 100%">
				<h4 class="title"> <span class="pull-right">
						<button id="btnAdd" class="btn" type="button">保存</button>
				</span></h4>
	<form method="post" id="insertForm">
			<table class="B-table" width="100%">
				<TR  style="display:none;">
				<!-- <TR> -->
        			<TD><input type="text" class="span12" kind="text"  id="S_ZDXXID" fieldname="ZDXXID" name="ZDXXID" keep="true"></TD>
					<!-- <TD><input type="text" class="span12" kind="text"  id ="GC_ZSB_JDB_ID" fieldname="GC_ZSB_JDB_ID" name="ID" ></TD> -->
					<TD><input type="text" class="span12" kind="text"  id="JHID" fieldname="JHID" name="JHID" keep="true"></TD>
					<TD><input type="text" class="span12" kind="text"  id="JHSJID" fieldname="JHSJID" name="JHSJID" keep="true"></TD>
					<TD><input type="text" class="span12" kind="text"  id="ND" fieldname="ND" name="ND" keep="true"></TD>
					<TD><input type="text" class="span12" kind="text"  id="XMID" fieldname="XMID" name="XMID" keep="true"></TD>
       			</TR>
       			<tr>
				    <th width="11%" class="right-border bottom-border text-right disabledTh">项目名称</th>
				    <td width="54%" colspan="3" class="right-border bottom-border">
				    	<input class="span12"  type="text" fieldname="XMMC" name = "XMMC" id="S_XMMC" disabled keep="true">
				    </td>
				    <th width="16%" class="right-border bottom-border text-right disabledTh">区域</th>
				    <td width="19%" class="right-border bottom-border">
				    	<input class="span12"  type="text" fieldname="QY" name = "QY" id="S_QY" disabled keep="true">
				    </td>
				</tr>
				<tr>
					<th width="11%" class="right-border bottom-border text-right disabledTh">居民户总数</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="text" id="S_JMHS" fieldname="JMHS" name="JMHS" disabled keep="true">
					</td>
					<th width="16%" class="right-border bottom-border text-right disabledTh">企业家总数</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="text" id="S_QYJS" fieldname="QYJS" name="QYJS" disabled keep="true"/>
					</td>
					<th width="16%" class="right-border bottom-border text-right disabledTh">征地总面积</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="text" id="S_ZMJ" fieldname="ZMJ" name="ZMJ" disabled keep="true">
					</td>
				</tr>
				<tr>
					<th width="11%" class="right-border bottom-border text-right disabledTh">累计完成居民户数</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="text" id="S_LJJMZL" fieldname="" name="JMHS" disabled keep="true">
					</td>
					<th width="16%" class="right-border bottom-border text-right disabledTh">累计完成企业家数</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="text" id="S_LJQYZL" fieldname="" name="QYJS" disabled keep="true"/>
					</td>
					<th width="16%" class="right-border bottom-border text-right disabledTh">累计完成征地面积</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="text" id="S_LJZDMJ" fieldname="" name="ZMJ" disabled keep="true">
					</td>
				</tr>    
				<tr>
					<th id="s_bcjm" width="11%" class="right-border bottom-border text-right">本次完成居民户数</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="number" id="S_BCWCJMS" fieldname="BCWCJMS" name="BCWCJMS" check-type="number" min="0"/>
					</td>
					<th id="s_bcqy" width="16%" class="right-border bottom-border text-right">本次完成企业家数</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="number" id="S_BCWCQYS" fieldname="BCWCQYS" name="BCWCQYS" check-type="number" min='0'/>
					</td>
					<th id="s_bczd" width="16%" class="right-border bottom-border text-right">本次完成征地面积</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="number" id="S_BCZDMJ" fieldname="BCZDMJ" name="BCZDMJ" check-type="number"  min='0'/>
					</td>
				</tr>
				<tr>
				<th width="11%" class="right-border bottom-border text-right">上报日期</th>
				    <td width="19%"  class="right-border bottom-border">
				    	<input class="span12 date"  type="date" id="S_SBRQ" fieldname="SBRQ" name = "SBRQ" check-type="required">
				    </td>
				    <td width="70%" colspan="4" class="right-border bottom-border"></td>
				</tr>    
				<tr>
       				<th width="11%" class="right-border bottom-border text-right">问题及风险</th>
       				<td width="89%" colspan="5" class="left-border bottom-border">
       					<textarea rows="3" class="span12" id="S_WTJFX" fieldname="WTJFX" name="WTJFX" check-type="maxlength" maxlength="2000"></textarea>
       				</td>
       			</tr>
			</table>
		</form>
		</div>
	</div>
</div>
<div class="tab-pane" id="tab1">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
		<form method="post" id="queryForm"">
			<table class="B-table">
				<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
					<!-- <TR> -->
						<TD>
							<input class="span12" type="text" name="ZDXXID" fieldname="ZDXXID" operation="=" id="Q_ZDXXID" keep="true">
						</TD>
					</TR>
			</table>
		</form>
		<div>
		<table class="table-hover table-activeTd B-table" id="tabList" width="100%" type="single" pageNum="5">
			<thead>
				<tr>
					<th name="XH" id="_XH" style="width:10px" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
					<th fieldname="SBRQ" tdalign="center" rowspan="2" colindex=2>&nbsp;上报日期&nbsp;</th>
					<th colspan="3">&nbsp;本次完成&nbsp;</th>
					<th colspan="3">&nbsp;累计完成&nbsp;</th>
					<th fieldname="WTJFX" maxlength="15" rowspan="2" colindex=9>&nbsp;问题及风险&nbsp;</th>
				</tr>
				<tr>	
					<th fieldname="BCWCJMS" tdalign="right" colindex=3>&nbsp;居民&nbsp;</th>
					<th fieldname="BCWCQYS" tdalign="right" colindex=4>&nbsp;企业&nbsp;</th>
					<th fieldname="BCZDMJ" tdalign="right"  colindex=5>&nbsp;土地面积&nbsp;</th>
					<th fieldname="LJJMZL" tdalign="right" colindex=6>&nbsp;居民&nbsp;</th>
					<th fieldname="LJQYZL" tdalign="right" colindex=7>&nbsp;企业&nbsp;</th>
					<th fieldname="LJZDMJ" tdalign="right" colindex=8>&nbsp;土地面积&nbsp;</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
		</div>
		<div class="B-small-from-table-autoConcise" style="width: 100%">
			<h4 class="title">
				<span class="pull-right">
					<button id="btnSave" class="btn" type="button">保存</button>
					<button id="btnDel" class="btn" type="button">删除</button>
				</span>
			</h4>
			<form method="post" action="" id="insertForm1">
				<table class="B-table" width="100%">
					<tr style="display: none;">
					<!-- <tr> -->
						<td><input class="span12" type="text" name="ZDXXID" fieldname="ZDXXID" id="E_ZDXXID" keep="true"></td>
						<td><input class="span12" type="text" name="GC_ZSB_JDB_ID" fieldname="GC_ZSB_JDB_ID" id="JDBID"></td>
						<td><input class="span12" type="text" name="JHSJID" fieldname="JHSJID" id="E_JHSJID" keep="true"></td>
						<td><input class="span12" type="text" name="SJBH" fieldname="SJBH" id="E_SJBH" keep="true"></td>
					</tr>
					<tr>
				    <th width="11%" class="right-border bottom-border text-right disabledTh">项目名称</th>
				    <td width="54%" colspan="3" class="right-border bottom-border">
				    	<input class="span12"  type="text" fieldname="XMMC" name = "XMMC" id="E_XMMC" disabled keep="true">
				    </td>
				    <th width="16%" class="right-border bottom-border text-right disabledTh">区域</th>
				    <td width="19%" class="right-border bottom-border">
				    	<input class="span12"  type="text" fieldname="QY" name = "QY" id="S_QY" disabled keep="true">
				    </td>
				</tr>
				<tr>
					<th width="11%" class="right-border bottom-border text-right disabledTh">居民户总数</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="text" id="E_JMHS" fieldname="JMHS" name="JMHS" disabled keep="true">
					</td>
					<th width="16%" class="right-border bottom-border text-right disabledTh">企业家总数</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="text" id="E_QYJS" fieldname="QYJS" name="QYJS" disabled keep="true"/>
					</td>
					<th width="16%" class="right-border bottom-border text-right disabledTh">征地总面积</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="text" id="E_ZMJ" fieldname="ZMJ" name="ZMJ" disabled keep="true">
					</td>
				</tr>
				<tr>
					<th width="11%" class="right-border bottom-border text-right disabledTh">累计完成居民户数</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="text" id="E_LJJMZL" fieldname="" name="JMHS" disabled keep="true">
					</td>
					<th width="16%" class="right-border bottom-border text-right disabledTh">累计完成企业家数</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="text" id="E_LJQYZL" fieldname="" name="QYJS" disabled keep="true"/>
					</td>
					<th width="16%" class="right-border bottom-border text-right disabledTh">累计完成征地面积</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="text" id="E_LJZDMJ" fieldname="" name="ZMJ" disabled keep="true">
					</td>
				</tr>    
				<tr>
					<th id="e_bcjm" width="11%" class="right-border bottom-border text-right">本次完成居民户数</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="number" id="E_BCWCJMS" fieldname="BCWCJMS" name="BCWCJMS" check-type="number" min='0'/>
					</td>
					<th id="e_bcqy" width="16%" class="right-border bottom-border text-right">本次完成企业家数</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="number" id="E_BCWCQYS" fieldname="BCWCQYS" name="BCWCQYS" check-type="number" min='0'/>
					</td>
					<th id="e_bczd" width="16%" class="right-border bottom-border text-right">本次完成征地面积</th>
					<td width="19%" class="right-border bottom-border">
						<input class="span12 text-right" type="number" id="E_BCZDMJ" fieldname="BCZDMJ" name="BCZDMJ" check-type="number" min='0'/>
					</td>
				</tr>
				
				<tr>
				<th width="11%" class="right-border bottom-border text-right">上报日期</th>
				    <td width="19%"  class="right-border bottom-border">
				    	<input class="span12 date"  type="date" id="E_SBRQ" fieldname="SBRQ" name = "SBRQ" check-type="required">
				    </td>
				    <td width="70%" colspan="4" class="right-border bottom-border"></td>
				</tr>    
				<tr>
       				<th width="11%" class="right-border bottom-border text-right">问题及风险</th>
       				<td width="89%" colspan="5" class="left-border bottom-border">
       					<textarea rows="3" class="span12" fieldname="WTJFX" id="E_WTJFX" name="WTJFX" check-type="maxlength" maxlength="2000"></textarea>
       				</td>
       			</tr>
				</table>
			</form>
		</div>
	</div>
	</div>
</div>
</div>
</div>
			<div align="center">
				<FORM name="frmPost" method="post" style="display: none"
					target="_blank">
					<!--系统保留定义区域-->
					<input type="hidden" name="queryXML" id="queryXML">
					<input type="hidden" name="txtXML" id="txtXML">
					<input type="hidden" name="txtFilter" order="desc" fieldname="SBRQ" id="txtFilter">
					<input type="hidden" name="resultXML" id="resultXML">
					<!--传递行数据用的隐藏域-->
					<input type="hidden" name="rowData">
				</FORM>
			</div>
	</body>
</html>
