<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title>规划审批进度维护</title>
<script type="text/javascript" charset="utf-8">
	var controllername = "${pageContext.request.contextPath }/qianQiShouXuController.do";
	var biaozhi = null;
	var xmmc;
	var isbllx,isbltd,isblgh,isblsg;
	var LXKYbblsx,TDSPbblsx,GHSPbblsx,SGXKbblsx;
	var id;
	var xmid;
	//初始化加载
	$(document).ready(function() {
		//获取父页面的值
	    var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
		//将父页面的值转成json对象
		var odd = convertJson.string2json1(rowValue);
		$("#demoForm").setFormValues(odd);
		$("#lxkyForm").setFormValues(odd);
		$("#sgxkForm").setFormValues(odd);
		$("#tdspForm").setFormValues(odd);
		$("#ghspForm").setFormValues(odd);
		//为上传文件是需要的字段赋值
	    var ywid=$(odd).attr("GC_ZGB_QQSX_ID");
	    xmmc=$(odd).attr("XMMC");
		var SJBH=$(odd).attr("SJBH");
		var YWLX=$(odd).attr("YWLX");
		xmid=$(odd).attr("XDKID");
		deleteFileData(ywid,"","","");
		setFileData(ywid,"",SJBH,YWLX,"0");
		//查询附件信息
		queryFileData(ywid,"","","");
		id=$(odd).attr("JHSJID");
	    LXKYbblsx=$(odd).attr("LXKYBBLSX");
	    TDSPbblsx=$(odd).attr("TDSPBBLSX");
	    GHSPbblsx=$(odd).attr("GHSPBBLSX");
	    SGXKbblsx=$(odd).attr("SGXKBBLSX");
	    var bdmc1=$(odd).attr("BDMC");
       if(bdmc1=='') {
    	   $("#BDMC").val('此统筹计划反馈只针对项目');
           $("#BDMC").attr("style","color:red;");
           $("#BDMC").removeAttr("fieldname");
		  }
       init(odd);
	//立项可研保存
	var LXKYsave = $("#LXKYsave");
	LXKYsave.click(function(){
		     var datalxky= Form2Json.formToJSON(lxkyForm);
		     weihuQQSX(datalxky);
			});	
	//施工许可保存
	var SGXKsave = $("#SGXKsave");
	SGXKsave.click(function(){
			 var datasgxk= Form2Json.formToJSON(sgxkForm);
		     weihuQQSX(datasgxk);
			});	
	//土地审批保存
	var TDSPsave = $("#TDSPsave");
	TDSPsave.click(function(){
			 var datatdsp= Form2Json.formToJSON(tdspForm);
		     weihuQQSX(datatdsp);
			});	
	//规划审批保存
	var GHSPsave = $("#GHSPsave");
	GHSPsave.click(function(){
			 var dataghsp= Form2Json.formToJSON(ghspForm);
		     weihuQQSX(dataghsp);
			 });	
	});
	//页面初始化判断
	function init(odd)
	{
		$("[name=tdsfbl]:radio").prop("disabled",true); 
		$("[name=lxsfbl]:radio").prop("disabled",true); 
		$("[name=ghspbl]:radio").prop("disabled",true); 
		$("[name=sgsfbl]:radio").prop("disabled",true); 
		var islx=odd.ISKYPF;
		var istd=odd.ISHPJDS;
		var isgh=odd.ISGCXKZ;
		var issg=odd.ISSGXK;
		if(islx=='0')
			{
			$("[name=LXKYBBLSX]:checkbox").prop("checked",true); 
			$("[name=LXKYBBLSX]:checkbox").prop("disabled",true); 
			}
		if(istd=='0')
			{
			$("[name=TDSPBBLSX]:checkbox").prop("checked",true); 
			$("[name=TDSPBBLSX]:checkbox").prop("disabled",true); 
			}
		if(isgh=='0')
			{
			$("[name=GHSPBBLSX]:checkbox").prop("checked",true); 
			$("[name=GHSPBBLSX]:checkbox").prop("disabled",true); 
			}
		if(issg=='0')
			{
			$("[name=SGXKBBLSX]:checkbox").prop("checked",true); 
			$("[name=SGXKBBLSX]:checkbox").prop("disabled",true); 
			}
	}
	//添加
	function weihuQQSX(zdata) 
	{
		
		if ($("#demoForm").validationButton()) 
		{
		  var biaozhi=$('#GC_ZGB_QQSX_ID').val();
		 if(biaozhi==null||biaozhi=="null"||biaozhi=='')
			{
			 	if($("#demoForm").validationButton())
				{
	 				//生成json串
	 		 		var data =zdata;
	 				//组成保存json串格式
	 				var data1 = defaultJson.packSaveJson(data);
	 				//调用ajax插入
	 				defaultJson.doInsertJson(controllername + "?insert&ywid="+$("#ywid").val(), data1,null,'addHuiDiao');
				}
			}
		 else
			 {
	 		 	if($("#demoForm").validationButton())
				{
	 		 		//生成json串
	 		 		var data = zdata;
	 				//组成保存json串格式
	 				var data1 = defaultJson.packSaveJson(data);
	 				//调用ajax插入
	 				defaultJson.doUpdateJson(controllername + "?update", data1,null,'editHuiDiao');
				}
	 	
			 }
			}
	  }
 	function addHuiDiao()
 	{
		var data2 = $("#frmPost").find("#resultXML").val();
		var resultmsgobj = convertJson.string2json1(data2);
		var subresultmsgobj = resultmsgobj.response.data[0];
		var aa=$(subresultmsgobj).attr("GC_ZGB_QQSX_ID");
		$("#GC_ZGB_QQSX_ID").val(aa); 
		$("#qqsxlxky").val(aa); 
		$("#qqsxsgxk").val(aa); 
		$("#qqsxghsp").val(aa); 
		$("#qqsxtdsp").val(aa); 
		
		setFileData(aa,"","","","0");
		
		var fuyemian=$(window).manhuaDialog.getParentObj();
	    fuyemian.xiugaiahang1(data2);
 	}
 	function editHuiDiao()
 	{
 		var fuyemian=$(window).manhuaDialog.getParentObj();
		var data2 = $("#frmPost").find("#resultXML").val();
	    fuyemian.xiugaiahang1(data2);	
 	}
	function getValue(){
 		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.$("#DT1").getSelectedRow();
		return rowValue;
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
	<table class="B-table" width="80%">
		<TR style="display:none;">
			<TD class="right-border bottom-border">
				<input type="text" id="GC_ZGB_QQSX_ID" class="span12" kind="text" keep="true" fieldname="GC_ZGB_QQSX_ID">
				<input type="text" id="SJWYBH" class="span12" kind="text" keep="true" fieldname="SJWYBH">
				<input type="text" class="span12" id="JHSJID" kind="text" keep="true" fieldname="JHSJID">
			    <input type="text"class="span12" kind="text" keep="true" fieldname="XDKID"> 
			</TD>
			<TD class="right-border bottom-border"></TD>
		</TR>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
			<td width="40%" class="right-border bottom-border">
			   <input class="span12 xmmc" keep="true" disabled placeholder="请选择项目" check-type="required" type="text" id="XMMC" fieldname="XMMC" name="XMMC">
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
			 <td width="40%" class="right-border bottom-border">
			   <input class="span12" keep="true" disabled   type="text" id="BDMC" fieldname="BDMC" name="BDMC">
			 </td>
		</tr>
	</table>
	</form>
<form method="post" id="lxkyForm"  >	
	<h4 class="title">
		立项可研 <span class="pull-right">
			<button id="LXKYsave" name="LXKYsave" class="btn" type="button">保存</button>
		</span>
	</h4>
	<table class="B-table" width="80%" >
			<TR style="display: none;">
			<TD class="right-border bottom-border">
				<input type="text" id="qqsxlxky" class="span12" kind="text" keep="true" fieldname="GC_ZGB_QQSX_ID">
				<input type="text" class="span12" kind="text" keep="true" fieldname="JHSJID">
			    <input type="text" class="span12" kind="text" keep="true" fieldname="XDKID"> 
			    <input type="text" id="SJWYBH" class="span12" kind="text" keep="true" fieldname="SJWYBH">
			    <input type="text" id="LXFKZT" class="span12" kind="text"  fieldname="LXFKZT" > 
			</TD>
			<TD class="right-border bottom-border"></TD>
		</TR>
		<tr>
			<th  class="right-border bottom-border text-right">是否办理</th>
			<td  class=" bottom-border">
			<input class="span12" type="radio" id="lxsfbl" placeholder=""  kind="dic" src="E#1=办理:0=不办理" name="lxsfbl" fieldname="ISKYPF" onclick="LXSFBL(this)">
			</td>
		</tr>
		<tr id="sss">
			<th class="right-border bottom-border text-right">不办理手续</th>
			<td  colspan="3" class="">
			  <input class="span12" id="LXKYBBLSX" type="checkbox" kind="dic" src="LXKYFJLX" name="LXKYBBLSX" fieldname="LXKYBBLSX"></td>
		</tr>
		<tr>
			<th class="right-border bottom-border text-right">不办理原因</th>
			<td colspan="3" class="bottom-border">
			<textarea rows="2" class="span12" id="LXKYBBLYY" name="LXKYBBLYY" fieldname="LXKYBBLYY" maxlength="4000" check-type="maxlength"></textarea></td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">附件上传</th>
			<td colspan="3" class="bottom-border right-border">
				<div>
					<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0041"> <i class="icon-plus"></i> <span>添加文件...</span>
					</span>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0041" class="files showFileTab"
							data-toggle="modal-gallery" data-target="#modal-gallery">
						</tbody>
					</table>
				</div></td>
		</tr>
	</table>
	</form>
<form method="post" id="tdspForm"  >
	<h4 class="title">
		土地审批 <span class="pull-right">
			<button id="TDSPsave" name="TDSPsave" class="btn" type="button">保存</button>
		</span>
	</h4>
	<table class="B-table" width="80%" >
		<TR style="display: none;">
			<TD class="right-border bottom-border">
				<input type="text" id="qqsxtdsp" class="span12" kind="text" keep="true" fieldname="GC_ZGB_QQSX_ID">
				<input type="text" class="span12" kind="text" keep="true" fieldname="JHSJID">
				<input type="text" class="span12" kind="text" keep="true" fieldname="SJWYBH">
			    <input type="text"class="span12" kind="text" keep="true" fieldname="XDKID"> 
			    <input type="text" id="TDFKZT" class="span12" kind="text" keep="true" fieldname="TDFKZT"> 
			</TD>
			<TD class="right-border bottom-border"></TD>
		</TR>
		<tr>
			<th  class="right-border bottom-border text-right">是否办理</th>
			<td  class=" bottom-border">
				<input class="span12" type="radio" id="tdsfbl"   kind="dic" src="E#1=办理:0=不办理" name="tdsfbl" fieldname="ISHPJDS" onclick="TDSFBL(this)" disabled>
			</td>
		</tr>
		<tr>
			<th class="right-border bottom-border text-right">不办理手续</th>
			<td colspan="3" class="right-border bottom-border">
			  <input class="span12" id="TDSPBBLSX" type="checkbox" kind="dic" src="TDSPSX"  name="TDSPBBLSX" fieldname="TDSPBBLSX"></td>
		</tr>
		<tr>
			<th class="right-border bottom-border text-right">不办理原因</th>
			<td colspan="3" class="bottom-border">
			<textarea rows="2" class="span12" id="TDSPBBLYY" name="TDSPBBLYY" fieldname="TDSPBBLYY" maxlength="4000" check-type="maxlength"></textarea></td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">附件上传</th>
			<td colspan="3" class="bottom-border right-border">
				<div>
					<span class="btn btn-fileUpload" onclick="doSelectFile(this);"
						fjlb="0042"> <i class="icon-plus"></i> <span>添加文件...</span>
					</span>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0042" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery">
						</tbody>
					</table>
				</div></td>
		</tr>
	</table>
  </form>
<form method="post" id="ghspForm"  >
	<h4 class="title">
		规划审批 <span class="pull-right">
			<button id="GHSPsave" name="GHSPsave" class="btn" type="button">保存</button>
		</span>
	</h4>
	<table class="B-table" width="80%" >
	    <TR style="display: none;">
			<TD class="right-border bottom-border">
				<input type="text" id="qqsxghsp" class="span12" kind="text" keep="true" fieldname="GC_ZGB_QQSX_ID">
				<input type="text" class="span12" kind="text" keep="true" fieldname="JHSJID">
				<input type="text" class="span12" kind="text" keep="true" fieldname="SJWYBH">
			    <input type="text"class="span12" kind="text" keep="true" fieldname="XDKID"> 
			    <input type="text" id="GHFKZT" class="span12" kind="text" keep="true" fieldname="GHFKZT"> 
			</TD>
			<TD class="right-border bottom-border"></TD>
		</TR>
		<tr>
			<th  class="right-border bottom-border text-right">是否办理</th>
			<td class=" bottom-border">
				<input class="span12" type="radio" id="ghspbl" disabled kind="dic" src="E#1=办理:0=不办理" name="ghspbl" fieldname="ISGCXKZ" >
			</td>
		</tr>
		<tr>
			<th class="right-border bottom-border text-right">不办理手续</th>
			<td colspan="3" class="right-border bottom-border">
			  <input class="span12" id="GHSPBBLSX" type="checkbox" kind="dic" src="GHSX"  name="GHSPBBLSX" fieldname="GHSPBBLSX"></td>
		</tr>
		<tr>
			<th class="right-border bottom-border text-right">不办理原因</th>
			<td colspan="3" class="bottom-border">
			<textarea rows="2" class="span12" id="GHSPBBLYY" name="GHSPBBLYY" fieldname="GHSPBBLYY" maxlength="4000" check-type="maxlength"></textarea></td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">附件上传</th>
			<td colspan="3" class="bottom-border right-border">
				<div>
					<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0043"> <i class="icon-plus"></i> <span>添加文件...</span>
					</span>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0043" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery">
						</tbody>
					</table>
				</div></td>
		</tr>
	</table>
  </form>
<form method="post" id="sgxkForm"  >
	<h4 class="title">
		施工许可 <span class="pull-right">
			<button id="SGXKsave" name="SGXKsave" class="btn" type="button">保存</button>
		</span>
	</h4>
	<table class="B-table" width="80%" >
	    <TR style="display: none;">
			<TD class="right-border bottom-border">
				<input type="text" id="qqsxsgxk" class="span12" kind="text" keep="true" fieldname="GC_ZGB_QQSX_ID">
				<input type="text" class="span12" kind="text" keep="true" fieldname="JHSJID">
				<input type="text" class="span12" kind="text" keep="true" fieldname="SJWYBH">
			    <input type="text"class="span12" kind="text" keep="true" fieldname="XDKID">
			    <input type="text" id="SGFKZT" class="span12" kind="text" keep="true" fieldname="SGFKZT">  
			</TD>
			<TD class="right-border bottom-border"></TD>
		</TR>
		<tr>
			<th  class="right-border bottom-border text-right">是否办理</th>
			<td  class=" bottom-border">
				<input class="span12" type="radio" id="sgsfbl" disabled kind="dic" src="E#1=办理:0=不办理" name="sgsfbl" fieldname="ISSGXK" onclick="SGSFBL(this)">
			</td>
		</tr>
		<tr>
			<th class="right-border bottom-border text-right">不办理手续</th>
			<td colspan="3" class="right-border bottom-border">
			  <input class="span12" id="SGXKBBLSX" type="checkbox" kind="dic" src="SGXKSX"   name="SGXKBBLSX" fieldname="SGXKBBLSX"></td>
		</tr>
		<tr>
			<th class="right-border bottom-border text-right">不办理原因</th>
			<td colspan="3" class="bottom-border">
			<textarea rows="2" class="span12" id="SGXKBBLYY" name="SGXKBBLYY" fieldname="SGXKBBLYY" maxlength="4000" check-type="maxlength"></textarea></td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">附件上传</th>
			<td colspan="3" class="bottom-border right-border">
				<div>
					<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0044"> <i class="icon-plus"></i> <span>添加文件...</span>
					</span>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0044" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery">
						</tbody>
					</table>
				</div></td>
		</tr>
	</table>
</form>
</div>
</div>
</div>
	<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true" />
	<div align="center">
		<FORM name="frmPost" method="post" style="display: none" target="_blank" id="frmPost">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML"> 
			<input type="hidden" name="txtXML" id="txtXML"> 
			 <input type="hidden" name="ywid" id = "ywid" value="">
			<input type="hidden" name="resultXML" id="resultXML"> 
			<input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ" />
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
			 <input type="hidden" name="queryResult" id="queryResult">

		</FORM>
	</div>
</body>
</html>