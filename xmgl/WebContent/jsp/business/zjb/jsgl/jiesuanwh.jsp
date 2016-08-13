<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/jieSuanGuanliController.do";
  var tbl = null;
  var fuyemianfangfa=true;
  var xx;
	$(function() {
		//保存按钮
		var btn = $("#example1");
		btn.click(function()
		  {
	 		 	 if($("#demoForm").validationButton())
				{
	 		 		//生成json串
	 				var data = Form2Json.formToJSON(demoForm);
	 				//组成保存json串格式
	 				var data1 = defaultJson.packSaveJson(data);
	 				//调用ajax插入
	 			 	if(fuyemianfangfa)
	 			 		{
		 			 		defaultJson.doInsertJson(controllername + "?insertJieSuan&ywid="+$("#ywid").val(), data1,null,'addHuiDiao');
	 			 		}
	 			 	else{
		 			 		defaultJson.doInsertJson(controllername + "?updateJieSuan", data1,null,'editHuiDiao');
	 			 	}
				}
 		   });
		 //复制新增按钮
		var btn = $("#fuzhixinzeng");
		btn.click(function()
		  {
			$("#GC_ZJB_JSB_ID").val('');
			fuyemianfangfa=true;
 		   });
		//默认调用
		init();
	    //清空表单
		var btn = $("#example_clear");
		btn.click(function() 
		{
			$("#demoForm").clearFormResult(); 
			 clearFileTab();
		     $("#ywid").val("");
		});
		//选择合同
		$("#xmBtn").click(function()
			{
			      $(window).manhuaDialog({"title":"合同信息>查询","type":"text","content":"${pageContext.request.contextPath}/jsp/business/htgl/htsghtcx.jsp","modal":"2"});
			});
	   });


	//默认选中委托
	function init(){
		$("input[name=SFWT]").each(function()
	     {
		  	if(this.value=='1')
	    	{
			  this.checked=true;
			 $("#demoForm").validation();
	    	}
		});	
	}
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
//回调函数
function addHuiDiao()
{
	
	var data2 = $("#frmPost").find("#resultXML").val();
	var resultmsgobj = convertJson.string2json1(data2);
	var subresultmsgobj = resultmsgobj.response.data[0];
	var aa=$(subresultmsgobj).attr("GC_ZJB_JSB_ID");
	$("#GC_ZJB_JSB_ID").val(aa); 
	fuyemianfangfa=false;
	var fuyemian=$(window).manhuaDialog.getParentObj();
    fuyemian.tianjiahang(data2);
    $(window).manhuaDialog.close();
}
function editHuiDiao()
{
	
	var data3 = $("#frmPost").find("#resultXML").val();
	var resultmsgobj1 = convertJson.string2json1(data3);
	var subresultmsgobj1 = resultmsgobj1.response.data[0];
	var aa=$(subresultmsgobj1).attr("GC_ZJB_JSB_ID");
	$("#GC_ZJB_JSB_ID").val(aa); 
	var fuyemian1=$(window).manhuaDialog.getParentObj();
	fuyemian1.xiugaihang(data3);	
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
		</span> 
  </h4>
 <form method="post" action="${pageContext.request.contextPath }/insertdemo.xhtml" id="demoForm"  >
     	<input class="span12" id="GC_ZJB_JSB_ID" type="hidden" placeholder=""  check-type=""  fieldname="GC_ZJB_JSB_ID" name = "GC_ZJB_JSB_ID">
      		<table class="B-table" width="100%"  >
      		                
	      			<TR  style="display:none;">
				        <TD class="right-border bottom-border">
				            <input class="span12"  id="XDKID"  type="text"  name = "XDKID" fieldname="XDKID">
							<input class="span12"  id="BDID"  type="text"  name = "BDID" fieldname="BDID">
							<input class="span12"  id="HTID"  type="text"  name = "HTID" fieldname="HTID">
							<input class="span12"  id="JHSJID"  type="text"  name = "JHSJID" fieldname="JHSJID">
				        </TD>
	                </TR>
      			<tr>
      				<th  width="8%" class="right-border bottom-border text-right disabledTh">合同名称</th>
          			<td   width="42%" class="right-border bottom-border "  ><input class="span12" style="width:90%"  disabled id="HTMC" type="text" placeholder="必填" fieldname="HTMC" check-type="required" name = "HTMC">
          			  <button class="btn btn-link"  title="请选择合同" type="button" id="xmBtn" ><i class="icon-edit"></i></button></td>
          			<th  width="8%"  class="right-border bottom-border text-right disabledTh">合同编号</th>
          			<td width="17%" class="right-border bottom-border "><input width="2" class="span12"  id="HTBM"  type="text" disabled check-type="required" placeholder="必填"   name = "HTBM" fieldname="HTBM"></td>
         			<th width="8%" class="right-border bottom-border text-right disabledTh">合同金额</th>
          			<td width="17%" class="right-border bottom-border " ><input class="span12" min="0" id="HTQDJ" style="width:65%;text-align:right"   disabled type="number"   fieldname="HTQDJ" name = "HTQDJ">&nbsp;&nbsp;<b>(元)</td>
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
            		<td colspan="1" class="right-border bottom-border "><input class="span12" disabled  type="text" placeholder="" check-type="" id="SGDW" name = "SGDW" fieldname="SGDW"></td>
        		    <th class="right-border bottom-border text-right disabledTh">监理单位</th>
            		<td colspan="3" class="right-border bottom-border " ><input class="span12" disabled type="text" placeholder="" check-type="" id="JLDW" name = "JLDW" fieldname="JLDW"></td>
        		</tr>
        		</table>
        		   <h4 class="title">结算进展信息</h4>
        		<table class="B-table" width="100%">
        	 	<tr>
          			<th width="8%" class="right-border bottom-border text-right">结算状态</th>
        			<td  width="17%" class="right-border bottom-border text-right"><select class="span12"  id="JSZT"  name = "JSZT" fieldname = "JSZT"   kind="dic" src="JSZT"  ></select></td>
        		    <th width="8%" class="right-border bottom-border text-right">结算情况</th>
          			<td  class="right-border bottom-border text-right" ><textarea class="span12" rows="3" name = "JSQK"  maxlength="500" id="JSQK" fieldname="JSQK"></textarea></td>
          		</tr>
        		</table>
        		<h4 class="title">上报值和业主审定</h4>
        		 <table class="B-table" width="100%">
       			<tr>
          			<th width="8%" class="right-border bottom-border text-right" >提报人</th>
          			<td width="17%" class="right-border bottom-border" ><input class="span12"  type="text" maxlength="50"  id="TBR" name = "TBR" fieldname="TBR"></td>
          			<th width="8%" class="right-border bottom-border text-right">联系电话</th>
          			<td width="21%" class="right-border bottom-border" ><input class="span12"  type="text" placeholder="" maxlength="15" check-type="number" id="TBRDH" name = "TBRDH" fieldname="TBRDH"></td>
          		    <th width="8%" class="right-border bottom-border text-right" >提报日期</th>
          			<td width="15%" class="right-border bottom-border"  ><input class="span12 date"  type="date"  id="TBRQ"  name = "TBRQ" fieldname="TBRQ"></td>
         			<th width="8%" class="right-border bottom-border text-right" >提报金额</th>
          			<td width="15%" class="right-border bottom-border" ><input min="0" class="span12"  type="number"  style="width:65%;text-align:right"  id="TBJE" name = "TBJE" fieldname="TBJE">&nbsp;&nbsp;<b>(元)</td>
          		</tr>
       			<tr id="weituo">
       			    <th class="right-border bottom-border text-right">是否需要委托</th>
         			<td class=" bottom-border text-center" ><input class="span12" id="SFWT" type="radio"    placeholder="" kind="dic" src="E#1=是:0=否" name = "SFWT" fieldname="SFWT" onclick="sfwt()"></td>
          			<th class="right-border bottom-border text-right">咨询单位</th>
          			<td class="right-border bottom-border ">
	          			<select class="span12" id="WTZXGS" name = "WTZXGS" maxlength="100" fieldname="WTZXGS" kind="dic"  src="T#GC_CJDW:GC_CJDW_ID:DWMC:DWLX = '4'" >
		                </select>
          			<th class="right-border bottom-border text-right">审定日期</th>
          			<td class="right-border bottom-border "><input class="span12 date"  type="date"   name = "YZSDRQ" id="YZSDRQ" fieldname="YZSDRQ"></td>
          			<th class="right-border bottom-border text-right">审定金额</th>
          			<td class="right-border bottom-border " ><input class="span12" min="0"  type="number" style="width:65%;text-align:right"    id="YZSDJE" name = "YZSDJE" fieldname="YZSDJE">&nbsp;&nbsp;<b>(元)</td>
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
          			<td width="17%" class="right-border bottom-border "><input class="span12"  type="text" placeholder=""  maxlength="36"  name = "CSBGBH" id="CSBGBH" fieldname="CSBGBH"></td>
          			<th width="8%" class="right-border bottom-border text-right">日期</th>
          			<td width="17%" class="right-border bottom-border "><input class="span12 date"  type="date" placeholder=""  id="CSSDRQ"  name = "CSSDRQ" fieldname="CSSDRQ"></td>
          			<th width="8%" class="right-border bottom-border text-right">金额</th>
          			<td width="17%" class="right-border bottom-border "><input class="span12 " min="0" type="number" style="width:65%;text-align:right" placeholder=""  id="CSSDJE"  name = "CSSDJE" fieldname="CSSDJE">&nbsp;&nbsp;<b>(元)</td>
          			<td class="right-border bottom-border" colspan="2" >
        		</tr>
        			<tr>
					<th width="8%" class="right-border bottom-border text-right">附件上传</th>
					<td colspan="8" class="bottom-border right-border">
						<div>
						<span class="btn btn-fileUpload" onclick="doSelectFile(this);" id="shangchuanID" fjlb="2025">
							<i class="icon-plus"></i>
								<span>添加文件...</span></span>
									<table role="presentation" class="table table-striped">
										<tbody fjlb="2025" id="shangchuanID1" class="files showFileTab"
											data-toggle="modal-gallery" data-target="#modal-gallery">
										</tbody>
									 </table>
						</div>
					  </td>
					</tr>
        		
        		</table>
        	<!-- 	  <h4 class="title">审计审定</h4>  
          		<table class="B-table" width="100%">
       			<tr>
       			    <th width="8%" class="right-border bottom-border text-right">审计报告编号</th>
          			<td width="17%" class="right-border bottom-border " ><input class="span12"  type="text" placeholder=""   maxlength="36" id="SJBGBH" name = "SJBGBH" fieldname="SJBGBH"></td>
          			<th width="8%"  class="right-border bottom-border text-right">日期</th>
          			<td width="17%" class="right-border bottom-border "><input class="span12 date"  type="date" placeholder="" id="SJSDRQ"   name = "SJSDRQ" fieldname="SJSDRQ"></td>
          			<th width="8%" class="right-border bottom-border text-right">金额</th>
          			<td width="17%" class="right-border bottom-border "><input class="span12" min="0" type="number"  style="width:65%;text-align:right" placeholder=""    maxlength="17" id="SJSDJE" name = "SJSDJE" fieldname="SJSDJE">&nbsp;&nbsp;<b>(元)</td>
          			<td  class="right-border bottom-border" colspan="2" >
          			</td>
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
		   <input type="hidden" name="ywid" id = "ywid" value="">
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "XMNF"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
 <script type="text/javascript">
//弹出窗口回调函数
	getWinData = function(data){
	         var odd=convertJson.string2json1(data);
		     $("#demoForm").setFormValues(odd);
		     $("#XDKID").val(JSON.parse(data).XMID);
		     $("#HTID").val(JSON.parse(data).HTSJID);
		     $("#SGDW").val(JSON.parse(data).SGDWMC);
		     $("#JLDW").val(JSON.parse(data).JLDWMC);
		     document.getElementById('HTBM').disabled=true;
	};
 </script>
</body>
</html>