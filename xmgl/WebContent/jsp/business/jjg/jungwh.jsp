<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/jjg/jjgwhController.do";
  var tbl = null;
	$(function() {
		  //获取父页面的值
		  var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
		  var odd=convertJson.string2json1(rowValue);
		  //将父页面的值转成json对象
		  //alert(eval("("+a+")"));
		  //将数据放入表单
		  $("#demoForm").setFormValues(odd);
	      var ywid=$(odd).attr("GC_SJGL_JJG_ID");
		  var SJBH=$(odd).attr("SJBH");
		  var YWLX=$(odd).attr("YWLX");
		  
		  deleteFileData(ywid,"","","");
		  setFileData(ywid,"",SJBH,YWLX,"0");
		  //查询附件信息
		  queryFileData(ywid,"","","");
		  var bdmc1=$(odd).attr("BDID");
		  //判断项目是否有标段
		  if(bdmc1!='')
			  {
			  $("h5").remove();
			  }
		  else{
			   $("#BDMC").val('此竣工操作只针对项目');
	           $("#BDMC").attr("style","color:red;");
	           $("#BDMC").removeAttr("fieldname");
		  }
		//保存按钮
		var btn = $("#example1");
		btn.click(function()
		  {
			id=$("#GC_SJGL_JJG_ID").val();
			if($("#demoForm").validationButton())
			{
				if(id==null||id==""){
					//生成json串
	 				var data = Form2Json.formToJSON(demoForm);
	 				//组成保存json串格式
	 				var data1 = defaultJson.packSaveJson(data);
	 				//调用ajax插入
				 	defaultJson.doInsertJson(controllername + "?insert_jjg&ywid="+$("#ywid").val(),data1, null,'addHuiDiao');
				}else{
					//生成json串
	 				var data = Form2Json.formToJSON(demoForm);
	 				//组成保存json串格式
	 				var data1 = defaultJson.packSaveJson(data);
	 				//调用ajax插入
				 	defaultJson.doUpdateJson(controllername + "?update_jjg",data1, null,'editHuiDiao');
				}
				}else
				{
					return ;
				} 
	 		 
 		   });
	    //清空表单
		var btn1 = $("#example_clear");
		btn1.click(function() 
		{
			$("#demoForm").clearFormResult(); 
		});
	
	}
	);
	function addHuiDiao()
	{
		  var data2 = $("#frmPost").find("#resultXML").val();
		 	var fuyemian=$(window).manhuaDialog.getParentObj();
		    fuyemian.xiugaihang(data2);
		    $(window).manhuaDialog.close();
	}
	function editHuiDiao()
	{
		var data3 = $("#frmPost").find("#resultXML").val();
	 	var fuyemian1=$(window).manhuaDialog.getParentObj();
 		fuyemian1.xiugaihang(data3);
 	    $(window).manhuaDialog.close();	
	}
	
</script>   
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
  <h4 class="title">项目信息<font class="title" id="XSXMMC" style="color:red;"></font>
  	<span class="pull-right">
				 <button id="example1" class="btn"  type="button">保存</button>
          			   <!--   <button id="fuzhixinzeng" class="btn btn-inverse"  type="button">复制新增</button>-->
          	    <!--  <button id="example_clear" class="btn"  type="button">清空</button> -->
				</span> 
  </h4>
 <form method="post" id="demoForm">
 	<table class="B-table">
 				<TR  style="display:none;">
 				    <td>
  					<input type="text" id="BDID" name="BDID" fieldname="BDID"/>
  					<input type="text" id="XMID" name="XMID" fieldname="XMID"/>
  					<input type="text" id="JHSJID" name="JHSJID" fieldname="JHSJID"/>
  					<input type="text" id="GC_SJGL_JJG_ID" name="GC_SJGL_JJG_ID" fieldname="GC_SJGL_JJG_ID"/>
				    <input type="text" id="YWLX" fieldname="YWLX" name="YWLX"/>
				    <input type="text" id="ND" fieldname="ND" name="ND"/>
				    <input type="text" id="ywid"/>
	     			</td>
				 </TR>
				<tr>
				<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
				<td width="25%" class="right-border bottom-border">
					<input class="span12 xmmc" type="text" placeholder="必填"  data-toggle="modal"   check-type="required" disabled id="XMMC" name="XMMC" fieldname="XMMC"/>
		    
				</td>
				<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
				<td width="25%" class="right-border bottom-border">
					<input class="span12" type="text"  id="BDMC" name="BDMC" fieldname="BDMC" disabled/>
					
				</td>
				<th width="8%" class="right-border bottom-border text-right disabledTh">项目地址</th>
				<td width="25%" class="right-border bottom-border">
					<input id="XMBH" class="span12" type="text" name="XMDZ" id="XMDZ" check-type="maxlength" disabled maxlength="500" fieldname="XMDZ"/>
				</td>
				</tr>
	           <tr>
				<th width="8%" class="right-border bottom-border text-right disabledTh">完工日期</th>
				<td width="25%" class="right-border bottom-border">
					<input class="span12 date" name ="WGRQ" id="WGRQ" fieldname="WGRQ" type="date" disabled/>
				</td>
				<th width="8%" class="right-border bottom-border text-right disabledTh">项目管理公司</th>
				<td width="25%" class="right-border bottom-border">
					<input id="XMMC" class="span12" type="text" name="XMGLGS" id="XMGLGS" check-type="maxlength" disabled maxlength="100" fieldname="XMGLGS"/>
				</td>
				<th width="8%" class="right-border bottom-border text-right disabledTh">业主代表</th>
				<td width="25%" class="right-border bottom-border">
					<input id="YZDB" class="span12" type="text" name="YZDB" id="YZDB" check-type="maxlength" disabled maxlength="100" fieldname="YZDB"/>
				</td>
			  </tr>
			  <tr>
				<th width="8%" class="right-border bottom-border text-right disabledTh">设计单位</th>
				<td width="25%" class="right-border bottom-border">
					<input id="SJDW" class="span12" type="text" check-type="maxlength" maxlength="100" disabled fieldname="SJDW" name="SJDW" id="SJDW"/>
				</td>
				<th width="8%" class="right-border bottom-border text-right disabledTh">设计人</th>
				<td width="25%" class="right-border bottom-border">
					<input id="SJR" class="span12" type="text" check-type="maxlength" maxlength="100" disabled fieldname="FZR_SJDW" name="FZR_SJDW" id="FZR_SJDW"/>
				</td>
				<th width="8%" class="right-border bottom-border text-right disabledTh">监理单位</th>
				<td width="25%" class="right-border bottom-border">
					<input id="JLDW" class="span12" type="text" name="JLDW" id="JLDW" check-type="maxlength" disabled maxlength="100" fieldname="JLDW"/>
				</td>
			  </tr>
			  <tr>
				<th width="8%" class="right-border bottom-border text-right disabledTh">总监</th>
				<td width="25%" class="right-border bottom-border">
					<input id="FZR_JLDW" class="span12" type="text" name="FZR_JLDW" id="FZR_JLDW" disabled check-type="maxlength" maxlength="100" fieldname="FZR_JLDW"/>
				</td>
				<th width="8%" class="right-border bottom-border text-right disabledTh">施工单位</th>
				<td width="25%" class="right-border bottom-border">
					<input id="SGDW" class="span12" type="text" name="SGDW" id="SGDW" check-type="maxlength" disabled maxlength="100" fieldname="SGDW"/>
				</td>
				<th width="8%" class="right-border bottom-border text-right disabledTh">项目经理</th>
				<td width="25%" class="right-border bottom-border">
					<input id="FZR_SGDW" class="span12" type="text" name="FZR_SGDW" id="FZR_SGDW" check-type="maxlength" disabled maxlength="100" fieldname="FZR_SGDW"/>
				</td>
			  </tr>
			  <tr>
				<th width="8%" class="right-border bottom-border text-right ">是否报质监站</th>
				<td width="25%" class="right-border bottom-border">
					<select class="span12 3characters" kind="dic" src="SF" id="JU_SFBZJZ" fieldname="JU_SFBZJZ"   keep="true" name="JU_SFBZJZ"></select>
				</td>
				<th width="8%" class="right-border bottom-border text-right ">具备验收条件</th>
				<td width="25%" class="right-border bottom-border">
					<select class="span12 3characters" kind="dic" src="SF" id="JU_JBYSTJ" fieldname="JU_JBYSTJ"  keep="true" name="JU_JBYSTJ"></select>
				</td>
				<th width="8%" class="right-border bottom-border text-right ">是否组织竣工验收</th>
				<td width="25%" class="right-border bottom-border">
					<select class="span12 3characters" kind="dic" src="SF" id="JU_SFZZYS" fieldname="JU_SFZZYS"  keep="true" name="JU_SFZZYS"></select>
				</td>
			  </tr>
			  <tr>
				<th width="8%" class="right-border bottom-border text-right ">验收情况</th>
				<td width="25%" class="right-border bottom-border">
					<select class="span12 4characters" kind="dic" src="YSQK" id="JU_YSQK" fieldname="JU_YSQK" keep="true" name="JU_YSQK"></select>
				</td>
				<th width="8%" class="right-border bottom-border text-right ">是否资料存档</th>
				<td width="25%" class="right-border bottom-border">
					<select class="span12 3characters" kind="dic" src="SF" id="JU_SFZLCD" fieldname="JU_SFZLCD"  keep="true" name="JU_SFZLCD"></select>
				</td>
				<th width="8%" class="right-border bottom-border text-right ">是否质监站备案</th>
				<td width="25%" class="right-border bottom-border">
					<select class="span12 3characters" kind="dic" src="SF" id="JU_SFZJZBA" fieldname="JU_SFZJZBA"  keep="true" name="JU_SFZJZBA"></select>
				</td>
			  </tr>
			  <tr>
				<th width="8%" class="right-border bottom-border text-right">工程规模</th>
				<td width="92%" colspan="5" class="bottom-border">
					<textarea class="span12" rows="3" id="JSNR" check-type="maxlength" maxlength="500" fieldname="JSNR"  name="JSNR"></textarea>
				</td>
			  </tr>
			  <tr>
				<th width="8%" class="right-border bottom-border text-right">竣工开始日期</th>
				<td width="25%" class="right-border bottom-border">
					<input class="span12 date" name ="JU_KSRQ" id="JU_KSRQ" fieldname="JU_KSRQ" type="date"/>
				</td>
				<th width="8%" class="right-border bottom-border text-right">竣工验收日期</th>
				<td width="25%" class="right-border bottom-border">
					<input class="span12 date" name ="JGYSSJ" id="JGYSSJ" fieldname="JGYSSJ" type="date"/>
				</td>
				<td  class="right-border bottom-border" colspan="2">
			  </tr>
			  <tr>
				 	<th width="8%" class="right-border bottom-border text-right ">竣工参加单位</th>
					<td width="92%"  colspan="5" class="right-border bottom-border">
					<textarea class="span12" rows="4" id="JUNGJSDW" check-type="maxlength" maxlength="500" fieldname="JUNGJSDW" name="JUNGJSDW"></textarea>
					</td>
			  </tr>
			  <tr>
			  	<th width="8%" class="right-border bottom-border text-right">竣工参加人</th>
				<td width="92%" colspan="5" class="right-border bottom-border">
					<textarea class="span12" rows="4" id="JUNGJSR" check-type="maxlength" maxlength="500" fieldname="JUNGJSR" name="JUNGJSR"></textarea>
				</td>
			  </tr>
			<tr>															
				<th width="8%" class="right-border bottom-border text-right">工程未竣工验收原因</th>
				<td width="92%" colspan="5" class="bottom-border">
					<textarea class="span12" rows="2" id="WJGYSY" check-type="maxlength" maxlength="2000" fieldname="WJGYSY" name="WJGYSY"></textarea>
				</td>
			</tr>
		      <tr>
		       	<th width="8%" class="right-border bottom-border text-right text-right">竣工相关附件</th>
		       	<td colspan="7" class="bottom-border right-border">
					<div>
						<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0029">
							<i class="icon-plus"></i><span>添加文件...</span>
						</span>
						<table role="presentation" class="table table-striped">
							<tbody fjlb="0029" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
						</table>
					</div>
				</td>
		       </tr>				
 		   </table>
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

</body>
</html>