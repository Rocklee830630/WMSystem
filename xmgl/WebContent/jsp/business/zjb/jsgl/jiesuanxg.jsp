<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<%
	//String xx=request.getParameter("xx");
%>
<script type="text/javascript" charset="utf-8">

	var controllername = "${pageContext.request.contextPath }/jieSuanGuanliController.do";
	var tbl = null;
	$(function() {

		var btn = $("#example1");
		btn.click(function() {
			
			if ($("#demoForm").validationButton()) {
				//生成json串
				var data = Form2Json.formToJSON(demoForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//调用ajax插入
			   defaultJson.doUpdateJson(controllername + "?updateJieSuan", data1,null,'eidtHuiDiao');
			}
		});
		var guanbi = $("#guanbi");
		guanbi.click(function() {
			if (confirm("您确定要关闭本页吗？")) 
			{
				$(window).manhuaDialog.close();
			}
		});
		var del = $("#del");
		del.click(function()
		{
			var data = Form2Json.formToJSON(demoForm);
			var data1 = defaultJson.packSaveJson(data);
			xConfirm("提示信息","是否确认删除！");
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').one("click",function(){ 
				defaultJson.doDeleteJson(controllername+"?deleteJS",data1,null,"deleteHuiDiao"); 
			});
		});
		
	}

	);

	//清空表单
	$(function()
	  {
			var btn = $("#example_clear");
			btn.click(function() 
			{
				$("#demoForm").clearFormResult(); 
			});
			$("#xmBtn").click(function()
			{
				 $(window).manhuaDialog({"title":"合同信息>查询","type":"text","content":"${pageContext.request.contextPath}/jsp/business/htgl/htsghtcx.jsp","modal":"2"});
			});
			
			  //获取父页面的值
			  //将父页面的值转成json对象
			 // var odd=convertJson.string2json1(a);
			  var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
			  var odd=convertJson.string2json1(rowValue);
			  //将数据放入表单
			  $("#demoForm").setFormValues(odd);
			//为上传文件是需要的字段赋值
			    var ywid=$(odd).attr("GC_ZJB_JSB_ID");
				var SJBH=$(odd).attr("SJBH");
				var YWLX=$(odd).attr("YWLX");
				
				deleteFileData(ywid,"","","");
				setFileData(ywid,"",SJBH,YWLX,"0");
				//查询附件信息
				queryFileData(ywid,"","","");
			  //将数据放入表单
			  
			  
			  var moren=$(odd).attr("SFWT");
			  $("input[name=SFWT]").each(function()
			     {
				  var aa=this.value;
				  if(aa==moren)
					  {
					  if (aa == '0') {
							$("#WTZXGS").val('');
							$("#YZSDJE").val('');
							$("#YZSDRQ").val('');
							$("#WTZXGS").attr({disabled : 'true'});
							$("#demoForm").validation();
						} else {
							$("#WTZXGS").removeAttr("disabled");
							$("#demoForm").validation();
						}
						  this.checked=true;
					  }
				  } );
	  });
    //判断是否委托
	function sfwt() {
		$("input[name=SFWT]").each(function(){
			if(this.checked==true){
				var aa=this.value;
				if (aa == '0') {
					$("#WTZXGS").val('');
					$("#YZSDJE").val('');
					$("#YZSDRQ").val('');
					$("#WTZXGS").attr({disabled : 'true'});
					$("#demoForm").validation();
				} else {
					$("#WTZXGS").removeAttr("disabled");
					$("#demoForm").validation();
				}
			}
			
		});
		
	}
    
	//弹出窗口回调函数
	getWinData = function(data){
		 var odd=convertJson.string2json1(data);
	     $("#demoForm").setFormValues(odd);
	     $("#XDKID").val(JSON.parse(data).XMID);
	     $("#HTBH").val(JSON.parse(data).HTBM);
	     $("#HTJE").val(JSON.parse(data).HTQDJ);
	     $("#HTID").val(JSON.parse(data).HTSJID);
		 document.getElementById('HTBH').disabled=true;
	};
function eidtHuiDiao()
{
	//从resultXML获取修改的数据
	var data2 = $("#frmPost").find("#resultXML").val();
	//返回数据
	var fuyemian=$(window).manhuaDialog.getParentObj();
	fuyemian.xiugaihang(data2);
	$(window).manhuaDialog.close();
}
//删除回调
	function deleteHuiDiao()
	{
		var fuyemian=$(window).manhuaDialog.getParentObj();
		fuyemian.delhang();
		$(window).manhuaDialog.close();
	}
</script>   
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
  <h4 class="title">合同信息
  	  <span class="pull-right">
		  <button id="example1" class="btn"  type="button">保存</button>
		  <button id="del" class="btn"  type="button">删除</button>
          <!-- <button id="example_clear" class="btn"  type="button">清空</button> -->
      </span>  
  </h4>
     	<form method="post" action="${pageContext.request.contextPath }/updateJieSuan.do" id="demoForm"  >
      		
      			<table class="B-table" width="100%"  >
      		                
	      			<TR  style="display:none;">
				        <TD class="right-border bottom-border">
	                        <input class="span12" id="GC_ZJB_JSB_ID" type="text" placeholder=""  check-type=""  fieldname="GC_ZJB_JSB_ID" name = "GC_ZJB_JSB_ID">
      						<input class="span12"  id="XDKID"  type="text"  name = "XDKID" fieldname="XDKID">
							<input class="span12"  id="BDID"  type="text"  name = "BDID" fieldname="BDID">
							<input class="span12"  id="HTID"  type="text"  name = "HTID" fieldname="HTID">
				            <input class="span12"  id="JHSJID"  type="text"  name = "JHSJID" fieldname="JHSJID">
				        </TD>
	                </TR>
      			<tr>
      				<th  width="8%" class="right-border bottom-border text-right disabledTh">合同名称</th>
          			<td   width="42%" class="right-border bottom-border "  ><input class="span12" style="width:80%" disabled id="HTMC" type="text" placeholder="必填" fieldname="HTMC" check-type="required" name = "HTMC">
          			  <button class="btn btn-link"  title="请选择合同" type="button" id="xmBtn"><i class="icon-edit"></i></button></td>
          			<th  width="8%"  class="right-border bottom-border text-right disabledTh">合同编号</th>
          			<td width="17%" class="right-border bottom-border "><input width="2" class="span12"  id="HTBH"  type="text"  disabled check-type="required" placeholder="必填"   name = "HTBH" fieldname="HTBH"></td>
         			<th width="8%" class="right-border bottom-border text-right disabledTh">合同金额</th>
          			<td width="17%" class="right-border bottom-border " ><input class="span12" min="0" id="HTJE" style="width:65%;text-align:right"  disabled type="number"   fieldname="HTJE" name = "HTJE">&nbsp;&nbsp;<b>(元)</b></td>
          		</tr>
        		<tr>
          			<th class="right-border bottom-border text-right disabledTh">项目名称</th>
          			<td class="right-border bottom-border " ><input class="span12 xmmc" disabled check-type="required" placeholder="必填" type="text"   id="XMMC"  name = "XMMC" fieldname="XMMC"></td>
         			<th class="right-border bottom-border text-right disabledTh">标段名称</th>
          			<td class="right-border bottom-border "><input class="span12" disabled type="text"   id="BDMC" name = "BDMC" fieldname="BDMC"></td>
          			<th class="right-border bottom-border text-right disabledTh">项目年份</th>
          			<td class="right-border bottom-border "><input class="span12" disabled id="ND" type="text"  name = "ND" fieldname="ND"></td>
        		</tr>
        		<tr>
          			<th class="right-border bottom-border text-right disabledTh">施工单位</th>
            		<td colspan="1" class="right-border bottom-border "><input class="span12" disabled type="text" placeholder="" check-type="" id="SGDW" name = "SGDW" fieldname="SGDW"></td>
        		    <th class="right-border bottom-border text-right disabledTh">监理单位</th>
            		<td colspan="3" class="right-border bottom-border " ><input class="span12" disabled type="text" placeholder="" check-type="" id="JLDW" name = "JLDW" fieldname="JLDW"></td>
        			<!-- <th  class="right-border bottom-border text-right"colspan="2"></th> -->
        		</tr>
        		</table>
        		   <h4 class="title">结算进展信息</h4>
        		<table class="B-table" width="100%">
        	 	<tr>
          			<th width="8%" class="right-border bottom-border text-right">结算状态</th>
        			<td  width="17%" class="right-border bottom-border "><select class="span12"  id="JSZT"  name = "JSZT" fieldname = "JSZT"   kind="dic" src="JSZT"  ></select></td>
        		    <th width="8%" class="right-border bottom-border text-right">结算情况</th>
          			<td  class="right-border bottom-border " ><textarea class="span12" rows="3" name = "JSQK"  maxlength="500" id="JSQK" fieldname="JSQK"></textarea></td>
          		    <!-- <th  class="right-border bottom-border text-right" ></th> -->
          		</tr>
        		</table>
        		<h4 class="title">上报值和业主审定</h4>
        		 <table class="B-table" width="100%">
       			<tr>
          			<th width="8%" class="right-border bottom-border text-right" >提报人</th>
          			<td width="17%" class="right-border bottom-border " ><input class="span12"  type="text" maxlength="50"  id="TBR" name = "TBR" fieldname="TBR"></td>
          			<th width="8%" class="right-border bottom-border text-right">联系电话</th>
          			<td width="21%" class="right-border bottom-border " ><input class="span12"  type="text"   maxlength="15" check-type="number" id="TBRDH" name = "TBRDH" fieldname="TBRDH"></td>
          		    <th width="8%" class="right-border bottom-border text-right" >提报日期</th>
          			<td width="15%" class="right-border bottom-border "  ><input class="span12 date"  type="date"  id="TBRQ"  name = "TBRQ" fieldname="TBRQ"></td>
         			<th width="8%" class="right-border bottom-border text-right" >提报金额</th>
          			<td width="15%" class="right-border bottom-border " ><input min="0" class="span12"  type="number"  style="width:65%;text-align:right"  id="TBJE" name = "TBJE" fieldname="TBJE">&nbsp;&nbsp;<b>(元)</b></td>
          		</tr>
       			<tr id="weituo">
       			    <th class="right-border bottom-border text-right">是否需要委托</th>
         			<td class=" bottom-border text-center" ><input class="span12" id="SFWT" type="radio"      kind="dic" src="E#1=是:0=否" name = "SFWT" fieldname="SFWT" onclick="sfwt()"></td>
          			<th class="right-border bottom-border text-right">咨询单位</th>
          			<td class="right-border bottom-border ">
	          			<select class="span12" id="WTZXGS" name = "WTZXGS" maxlength="100" fieldname="WTZXGS" kind="dic"  src="T#GC_CJDW:GC_CJDW_ID:DWMC:DWLX = '4'" >
		                </select>
          			<th class="right-border bottom-border text-right">审定日期</th>
          			<td class="right-border bottom-border "><input class="span12 date"  type="date"    name = "YZSDRQ" id="YZSDRQ" fieldname="YZSDRQ"></td>
          			<th class="right-border bottom-border text-right">审定金额</th>
          			<td class="right-border bottom-border " ><input class="span12" min="0"  type="number" style="width:65%;text-align:right"     id="YZSDJE" name = "YZSDJE" fieldname="YZSDJE">&nbsp;&nbsp;<b>(元)</b></td>
        		</tr>
       			<tr>
        			<th class="right-border bottom-border text-right">提报财审日期</th>
         			<td class=" bottom-border text-left" ><input class="span12 date"  type="date"    name = "TBCSRQ" id="TBCSRQ" fieldname="TBCSRQ"></td>
         			<td colspan="6"></td>
        		</tr>
        		</table>
        		<h4 class="title">财审审定</h4>
          		<table class="B-table" width="100%">
       			<tr>
       			    <th width="8%" class="right-border bottom-border text-right">财审报告编号</th>
          			<td width="17%" class="right-border bottom-border "><input class="span12"  type="text"    maxlength="36"  name = "CSBGBH" id="CSBGBH" fieldname="CSBGBH"></td>
          			<th width="8%" class="right-border bottom-border text-right">日期</th>
          			<td width="17%" class="right-border bottom-border "><input class="span12 date"  type="date"    id="CSSDRQ"  name = "CSSDRQ" fieldname="CSSDRQ"></td>
          			<th width="8%" class="right-border bottom-border text-right">金额</th>
          			<td width="17%" class="right-border bottom-border "><input class="span12" min="0" type="number" style="width:65%;text-align:right"    id="CSSDJE"  name = "CSSDJE" fieldname="CSSDJE">&nbsp;&nbsp;<b>(元)</b></td>
          			<td  class="right-border bottom-border" colspan="2" >
        		</tr>
        		<tr>
					<th width="8%" class="right-border bottom-border text-right">附件上传</th>
					<td colspan="8" class="bottom-border right-border">
						<div>
						<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="2025">
							<i class="icon-plus"></i>
								<span>添加文件...</span></span>
									<table role="presentation" class="table table-striped">
										<tbody fjlb="2025" class="files showFileTab"
											data-toggle="modal-gallery" data-target="#modal-gallery">
										</tbody>
									 </table>
						</div>
					  </td>
			</tr>
        		</table>
        	<!--  <h4 class="title">审计审定</h4>  
          		<table class="B-table" width="100%">
       			<tr>
       			    <th width="8%" class="right-border bottom-border text-right">审计报告编号</th>
          			<td width="17%" class="right-border bottom-border " ><input class="span12"  type="text"     maxlength="36" id="SJBGBH" name = "SJBGBH" fieldname="SJBGBH"></td>
          			<th width="8%"  class="right-border bottom-border text-right">日期</th>
          			<td width="17%" class="right-border bottom-border "><input class="span12 date"  type="date"  id="SJSDRQ"   name = "SJSDRQ" fieldname="SJSDRQ"></td>
          			<th width="8%" class="right-border bottom-border text-right">金额</th>
          			<td width="17%" class="right-border bottom-border "><input class="span12" min="0" type="number"  style="width:65%;text-align:right" placeholder=""    maxlength="17" id="SJSDJE" name = "SJSDJE" fieldname="SJSDJE">&nbsp;&nbsp;<b>(元)</b></td>
          			<td class="right-border bottom-border" colspan="2" >
        		</tr>
        		
        		</table> -->
      	</form>
     	</div>
 	</div>
</div>
 <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
 <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "XMNF"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
         <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 </div>
  <script>

</script>
</body>
</html>