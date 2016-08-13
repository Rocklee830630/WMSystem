<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat"%>
<app:base />
<title></title>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/sjgl/gs/gsController.do";
  var gs_sjbh,sj_sjbh;				//父页面获取事件编号
  //var fd,sd;
 	//页面初始化
  $(function() {
	  setValue();
	  do_opera();
	  //按钮绑定 保存
	  $("#save").click(function() {
		 	if($("#gsForm").validationButton())
				{
					//生成json串
					var data = Form2Json.formToJSON(gsForm);
					//组成保存json串格式
					var data1 = defaultJson.packSaveJson(data);
					//调用ajax插入
					var aa=$("#ID").val();
					if(aa==null||aa=="")
						{
							defaultJson.doInsertJson(controllername + "?insertGs&sj_sjbh="+sj_sjbh+"&ywid="+$("#ywid").val(),data1, null,"callback");
						}else{	
							defaultJson.doUpdateJson(controllername + "?updateGs&gs_sjbh="+gs_sjbh+"&sj_sjbh="+sj_sjbh,data1, null,"callback");
						}
				}else{
			  		defaultJson.clearTxtXML();
				}
			});
	});
 	//异步执行方法
 	function callback(){
 		var fuyemian=$(window).manhuaDialog.getParentObj();
		fuyemian.gs_feedback();
		$(window).manhuaDialog.close();
 	}
 	//获取父页面行选值
 	function do_value(){
		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.$("#DT1").getSelectedRow();
		var obj = convertJson.string2json1(rowValue);
		gs_sjbh=$(obj).attr("GS_SJBH");
		sj_sjbh=$(obj).attr("SJ_SJBH");
		return obj;
	}
	//页面初始化赋值
	function setValue(){
		var json=do_value();
		$("#gsForm").setFormValues(json);
         
	 	var ywid=$(json).attr("GC_SJ_CBSJPF_ID");
		var SJBH=$(json).attr("SJBH");
		var YWLX=$(json).attr("YWLX");
			
		deleteFileData(ywid,"","","");
		setFileData(ywid,"",SJBH,YWLX,"0");
		//查询附件信息
		queryFileData(ywid,"","","");
		
		$("#BZ").val($(json).attr("BZ_GS"));
	}
	//左侧条件信息展示
	function getQqsx(){
		var sjwybh=$("#SJWYBH").val();
		var nd=$("#ND").val();
		var qqsx_bjsj='';
		$.ajax({
			url:controllername+"?getQqsx&sjwybh="+sjwybh+"&nd="+nd,
			data:"",
			datdaType:"json",
			async:false,
			success:function(result){
				result=convertJson.string2json1(result);
				qqsx_bjsj = $(result).attr("msg");
			}
		});
		return qqsx_bjsj;
	}
    //不办理手续查询
    function getBblsx() {
		var sjwybh=$("#SJWYBH").val();
		var nd=$("#ND").val();
  	 	$.ajax({
			url:controllername + "?getBblsx&sjwybh="+sjwybh+"&nd="+nd,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
			bblsx = result.msg;
		}
  	 });
  	 	return bblsx;
	}
	//对办理不办理的进行初步处理
	function do_opera(){
		//手续指定
		var ghsp='0007';
		var lxky='2020,2024,2021,2023,2022';
		var all=ghsp+','+lxky;
		
		var bjsj=getQqsx();
		if(bjsj==null||bjsj==""){
			do_show(all,"fal");
		}else{
			var bjsjArray=new Array();
			bjsjArray=bjsj.split(",");
			for(var a=0;a<bjsjArray.length;a++)
				{
					switch(a){
					case 0:
						var bjsj_a=bjsjArray[0];
						doDate(lxky,bjsj_a);
						break;
					case 1:
						var bjsj_b=bjsjArray[1];
						doDate(ghsp,bjsj_b);
						break;
					default:
						break;
					}
				}
		}
	 	var not=getBblsx();
		if(not==null||not==""){
		}else{
			do_show(not,"no");
		}
	}
	//根据查询的反馈时间进行页面操作
	function doDate(sxName,sjValue){
		if(sjValue==null||sjValue==""){
			do_show(sxName,"fal");
		}else{
			if(sjValue=="no"){
				do_show(sxName,"no");
			}else{
				do_show(sxName,"tru");
			}
		}
	}
	//页面显示,手续类别
	function do_show(kind,state){
		var listArray=new Array();
		listArray=kind.split(",");
		for(var c=0;c<listArray.length;c++){
			$("span").each(function(i){
				var xx=listArray[c];
				var str = $(this).attr("fieldname");
				if(str==xx){
					switch (state){
					case 'tru':
						$(this).html('<div style="text-align:center"><i title="本手续办理完成!" class=\"icon-ok\"></i></div>');
						break;
					case 'fal':
						$(this).html('<div style="text-align:center"><i title="本手续尚未办理!" class=\"icon-remove\"></i></div>');
						break;
					case 'no':
						$(this).html('<div style="text-align:center">—</div>');
						break;
					default:
						break;
				}
			}
		});
		}
	}
	function doPlus(){
		var sum=Number($("#GCJE").val())+Number($("#ZCJE").val())+Number($("#QTJE").val());
		$("#GS").val(sum);
	}
	function doYsPlus(){
		var sum=Number($("#YS_GCJE").val())+Number($("#YS_ZCJE").val())+Number($("#YS_QTJE").val());
		$("#SSE").val(sum);
	}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid" style="width:98%">
	<p></p>
	<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <div class="row-fluid" style="width:100%">
			<div class="B-small-from-table-autoConcise" style="float:left;width:40%" >
			<h4 class="title">手续信息</h4>
				<form id="sxForm">
					<table class="B-table" width="90%">
						<tr>
							<th width="25%" class="right-border bottom-border text-right">项目建议书</th>
							<td width="25%" class="right-border bottom-border"><span fieldname="2020" ></span></td>
						</tr>
						<tr>
							<th width="25%" class="right-border bottom-border text-right">可行性研究报告</th>
							<td width="25%" class="right-border bottom-border"><span fieldname="2024"></span></td>
						</tr>
						<tr>
							<th width="25%" class="right-border bottom-border text-right">环评报告</th>
							<td width="25%" class="right-border bottom-border"><span fieldname="2021"></span></td>
						</tr>
						<tr>
							<th width="25%" class="right-border bottom-border text-right">土地意见函</th>
							<td width="25%" class="right-border bottom-border"><span fieldname="2022"></span></td>
						</tr>
						<tr>
							<th width="25%" class="right-border bottom-border text-right">项目选址意见书</th>
							<td width="25%" class="right-border bottom-border"><span fieldname="0007"></span></td>
						</tr>
						<tr>
							<th width="25%" class="right-border bottom-border text-right">节能登记表</th>
							<td width="25%" class="right-border bottom-border"><span fieldname="2023"></span></td>
						</tr>
					</table>
				</form>
			</div>
			<div class="B-small-from-table-autoConcise" style="float:left;width:60%">
			<h4 class="title">概预算信息
			<span class="pull-right" fieldname="title">
         		 <button id="save" class="btn"  type="button">保存</button>
          	</span></h4>
				<form method="post" id="gsForm">
					<table class="B-table" width="100%">
						<TR  style="display: none;">
						<!-- <TR> -->
								<TD><input type="text" class="span12" kind="text"  fieldname="JHSJID" id="JHSJID" keep="true">
							</TD>
								<TD><input type="text" class="span12" kind="text"  fieldname="GC_SJ_CBSJPF_ID" id="ID" keep="true">
							</TD>
								<TD><input type="text" class="span12" kind="text"  fieldname="JHID" id="JHID" keep="true">
							</TD>
							<TD><input type="text" class="span12" kind="text"  fieldname="SJWYBH" id="SJWYBH" keep="true">
							</TD>
							<TD><input type="text" class="span12" kind="text"  fieldname="ND" id="ND" keep="true">
							</TD>
								<TD><input type="text" class="span12" kind="text"  fieldname="XMID" id="XMID" keep="true">
							</TD>
								<TD><input type="text" class="span12" kind="text"  fieldname="SJBH" id="SJBH" keep="true">
								<input type="text" class="span12" kind="text"  fieldname="BDID" id="BDID" keep="true">
							<input type="text" name="ywid" id ="ywid">
							</TD>
	        			</TR>
	        			<tr>
							<th width="8%" class="right-border bottom-border text-right">预算时间</th>
							<td width="92%" colspan="3" class="bottom-border">
								<input class="span12 date" type="date" id="SSJS" name="SSJS" fieldname="SSJS" />
							</td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">预算—工程金额</th>
							<td width="42%" class="bottom-border">
								<input class="span12" type="number" id="YS_GCJE" check-type="" name="YS_GCJE" fieldname="YS_GCJE" style="width:75%;text-align:right;" min="0" oninput="doYsPlus()"/>&nbsp;&nbsp;<b>（元）</b>
							</td>
							<th width="8%" class="right-border bottom-border text-right">预算—征拆金额</th>
							<td width="42%" class="bottom-border">
								<input class="span12" type="number" id="YS_ZCJE" check-type="" name="YS_ZCJE" fieldname="YS_ZCJE" style="width:75%;text-align:right;" min="0" oninput="doYsPlus()"/>&nbsp;&nbsp;<b>（元）</b>
							</td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">预算—其他金额</th>
							<td width="42%" class="bottom-border">
								<input class="span12" type="number" id="YS_QTJE" check-type="" name="YS_QTJE" fieldname="YS_QTJE" style="width:75%;text-align:right;" min="0" oninput="doYsPlus()"/>&nbsp;&nbsp;<b>（元）</b>
							</td>	
							<th width="8%" class="right-border bottom-border text-right">预算金额</th>
							<td width="42%" class="bottom-border">
								<input class="span12" type="number" id="SSE" check-type="" name="SSE" fieldname="SSE" style="width:75%;text-align:right;" min="0" disabled oninput="doYsPlus()"/>&nbsp;&nbsp;<b>（元）</b>
							</td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">概算批复时间</th>
							<td colspan="3" class="bottom-border">
								<input class="span12 date" type="date" id="CBSJPF" check-type="" name="CBSJPF" fieldname="CBSJPF" />
							</td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">工程金额</th>
							<td width="42%" class="bottom-border">
								<input class="span12" type="number" id="GCJE" check-type="" name="GCJE" fieldname="GCJE" style="width:75%;text-align:right;" min="0" oninput="doPlus()"/>&nbsp;&nbsp;<b>（元）</b>
							</td>
							<th width="8%" class="right-border bottom-border text-right">征拆金额</th>
							<td width="42%" class="bottom-border">
								<input class="span12" type="number" id="ZCJE" check-type="" name="ZCJE" fieldname="ZCJE" style="width:75%;text-align:right;" min="0" oninput="doPlus()"/>&nbsp;&nbsp;<b>（元）</b>
							</td>
						</tr>
						
	        			<tr>
							<th width="8%" class="right-border bottom-border text-right">其他金额</th>
							<td width="42%" class="bottom-border">
								<input class="span12" type="number" id="QTJE" check-type="" name="QTJE" fieldname="QTJE" style="width:75%;text-align:right;" min="0" oninput="doPlus()"/>&nbsp;&nbsp;<b>（元）</b>
							</td>
							<th width="8%" class="right-border bottom-border text-right">总金额</th>
							<td width="42%" class="bottom-border">
								<input class="span12" type="number" id="GS" check-type="" name="GS" fieldname="GS" style="width:75%;text-align:right;" min="0" disabled/>&nbsp;&nbsp;<b>（元）</b>
							</td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">备注</th>
							<td width="92%" colspan="3" class="bottom-border">
								<textarea class="span12" rows="4" id="BZ" check-type="maxlength" name="BZ" fieldname="BZ" maxlength="4000"></textarea>
							</td>
						</tr>
				       	<tr>
						<th width="8%" class="right-border bottom-border text-right">附件上传</th>
						<td colspan="8" class="bottom-border right-border">
							<div>
							<span class="btn btn-fileUpload" onclick="doSelectFile(this);" id="shangchuanID" fjlb="0051">
								<i class="icon-plus"></i>
									<span>添加文件...</span></span>
										<table role="presentation" class="table table-striped">
											<tbody fjlb="0051" id="shangchuanID1" class="files showFileTab"
												data-toggle="modal-gallery" data-target="#modal-gallery">
											</tbody>
										 </table>
							</div>
						  </td>
						</tr>
					</table>
				</form>
			</div>
		</div>
			</div>
		</div>
		</div>
		   <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
	<div align="center">
		<FORM name="frmPost" method="post" style="display: none"
			target="_blank">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML"> 
			<input type="hidden" name="txtXML" id="txtXML"> 
			<input type="hidden" name="queryResult" id = "queryResult" />
			<input type="hidden" name="resultXML" id="resultXML">
			<input type="hidden" name="txtFilter"  order="desc" fieldname = "pxh" id = "txtFilter">
			<input type="hidden" name="queryResult" id ="queryResult">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
		</FORM>
	</div>
</body>
</html>