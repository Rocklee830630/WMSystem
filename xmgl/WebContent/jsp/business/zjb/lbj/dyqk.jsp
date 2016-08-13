<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title></title>
<%
//	String json=request.getParameter("json");
	//String json2=request.getParameter("json2");
	String bdmc =request.getParameter("bdmc");
%>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/zjb/lbj/dyqkController.do";
  var xmxx,LBJBID;
	$(function() {
		ready();
		//查询
		var btn = $("#query");
		btn.click(function() {
			//生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryDyqk",data,DT1,null,false);
				});
		//清空查询条件
		var clear=$("#clear");
		clear.click(function(){
			$("#queryForm").clearFormResult();
		});
		//清空查询条件
		var clean=$("#clean");
		clean.click(function(){
			$("#DyqkForm").clearFormResult();
			$("#DT1").cancleSelected();
			 clearFileTab();
		     $("#ywid").val("");
		});
		//添加方法
		var insert=$("#insert");
		insert.click(function(){
			if($("#DyqkForm").validationButton())
			{
			var id=$("#ID").val();
			var data = Form2Json.formToJSON(DyqkForm);
			var data1 = defaultJson.packSaveJson(data);
			var s_date=$("#TWRQ").val();
			var e_date=$("#DYRQ").val();
			if(s_date>e_date){
				xFailMsg("提问日期不能大于答疑日期","");
				return;
			}
			if(id==null||id==""){
				defaultJson.doInsertJson(controllername + "?insertDyqk&ywid="+$("#ywid").val(),data1, DT1,'addHuiDiao');
			}else{
				defaultJson.doUpdateJson(controllername + "?updateDyqk",data1, DT1,'editHuiDiao');
			}
			}else{
		  		defaultJson.clearTxtXML();
			}
		});
		//删除
		var shanchu=$("#shanchu");
		shanchu.click(function()
		{
			var index =	$("#DT1").getSelectedRowIndex();
				//生成json串
		 	var data = Form2Json.formToJSON(DyqkForm);
			var data1 = defaultJson.packSaveJson(data);
			if(index==-1){
				requireSelectedOneRow();
				return;
			}else{
				xConfirm("提示信息","是否确认删除！");
				$('#ConfirmYesButton').unbind();
				$('#ConfirmYesButton').one("click",function(){ 
					defaultJson.doDeleteJson(controllername+"?deleteDyqk",data1,DT1,"deleteHuiDiao"); 
				});
			}
		}
		);
		
	});
		//行选
	function tr_click(obj){
		var hang=$("#DT1").getSelectedRow();
		if(hang=="-1"){
			$("#DyqkForm").clearFormResult();
		}else{
			$("#DyqkForm").setFormValues(obj);
		}
		$("#GC_ZJB_LBJB_ID").val(LBJBID);
		//为上传文件是需要的字段赋值
	    var ywid=$(obj).attr("GC_ZJB_DYQK_ID");
		var SJBH=$(obj).attr("SJBH");
		var YWLX=$(obj).attr("YWLX");
		deleteFileData(ywid,"","","");
		setFileData(ywid,"",SJBH,YWLX,"0");
		//查询附件信息
		queryFileData(ywid,"","","");
	}
		//页面初始化
    function ready() {
		init();
   	 	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryDyqk",data,DT1,null,true);
   };
   function init(){
	   var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
	   var odd=convertJson.string2json1(rowValue);
		  //将数据放入表单
	  $("#DyqkForm").setFormValues(odd);
	  $("#BZ").val('');
	  $("#GC_ZJB_LBJB_ID").val(odd.GC_ZJB_LBJB_ID);
	  $("#Q_SJWYBH").val($("#SJWYBH").val());
	  $("#Q_ND").val($("#ND").val());
	  LBJBID=$("#GC_ZJB_LBJB_ID").val();
	  var xmmc=$(odd).attr("XMMC");
	  var bdmc=$(odd).attr("BDMC"); 
	  var xm=document.getElementsByTagName("font");
	     if(bdmc==null||bdmc==""){
		   xm[0].innerHTML=xmmc;
	   }else{
		   xm[0].innerHTML=xmmc;
		   xm[1].innerHTML=bdmc; 
	    } 
   }
   //回调
   function addHuiDiao()
   {
	    var data2 = $("#frmPost").find("#resultXML").val();
		var resultmsgobj = convertJson.string2json1(data2);
		var subresultmsgobj = resultmsgobj.response.data[0];
		var DYQKID=$(subresultmsgobj).attr("GC_ZJB_DYQK_ID");
		$("#ID").val(DYQKID); 
	    $("#DT1").setSelect(0);
	    setFileData(DYQKID,"",$(subresultmsgobj).attr("SJBH"),$(subresultmsgobj).attr("YWLX"));
	    var sjwybh= $("#Q_SJWYBH").val();
	    var nd= $("#Q_ND").val();
		var fuyemian=$(window).manhuaDialog.getParentObj();
		fuyemian.dayixiugaihang(sjwybh,nd);
   }
   function editHuiDiao()
   {
	    var sjwybh= $("#Q_SJWYBH").val();
	    var nd= $("#Q_ND").val();
		var fuyemian=$(window).manhuaDialog.getParentObj();
		fuyemian.dayixiugaihang(sjwybh,nd);
   }
   function deleteHuiDiao()
   {
	     $("#DyqkForm").clearFormResult();
		 $("#DT1").cancleSelected();
		 clearFileTab();
	     $("#ywid").val("");
	     var sjwybh= $("#Q_SJWYBH").val();
	     var nd= $("#Q_ND").val();
		 var fuyemian=$(window).manhuaDialog.getParentObj();
		 fuyemian.dayixiugaihang(sjwybh,nd);
   }
</script>
</head>
<body>
<app:dialogs />
<%if("".equals(bdmc)) {%>
     		<h5 style="color:black;">当前项目为：<font class="title" style="color:red;">&nbsp;</font></h5>
     	<%}else{ %>
     		 <h5 style="color:black;">当前项目为：<font class="title" style="color:red;">&nbsp;</font>,<font class="title" style="color:red;">&nbsp;</font></h5>
     	<%} %>
	<div class="container-fluid">
	<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<INPUT id="num" type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<=" keep="true"/>
				<INPUT class="span12" type="text" id="Q_SJWYBH" fieldname="JHSJ.SJWYBH" operation="=" keep="true"/>
				<INPUT class="span12" type="text" id="Q_ND" fieldname="JHSJ.ND" operation="=" keep="true"/>
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr style="display:none">
        <th width="5%" class="right-border bottom-border text-right" >提问日期</th>
	          <td width="10%" class="left-border bottom-border">
                  <input type="date" class="span12" fieldname="TWRQ" id="S_TWRQ" operation=">=" logic="and"/>
                  <input type="date" class="span12" fieldname="TWRQ" id="E_TWRQ" operation="<=" />
	          </td>
	          <th width="5%" class="right-border bottom-border text-right" >报告类别</th>
	          <td width="10%" class="left-border bottom-border">
                  
	          </td>
		
         <td width="60%" class=" bottom-border text-right">
           <button id="query" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
           <button id="clear" class="btn btn-link"  type="button"><i class="icon-trash"></i>清空</button>
          </td>
        </tr>
      </table>
      </form>
	  <div style="height:5px;"> </div>
        <div class= "overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" editable="0" pageNum="3" nopromptmsg="true">
                <thead>
                    <tr>	
                    	<th name="XH" id="_XH" tdalign="" width="3%">&nbsp;#&nbsp;</th>
                    	<th fieldname="TWRQ" tdalign="center" width="15%">&nbsp;提问日期&nbsp;</th>
						<th fieldname="DYRQ" tdalign="center" width="15%">&nbsp;答疑日期&nbsp;</th>
						<th fieldname="BZ" tdalign="" maxlength="25" width="">&nbsp;备注&nbsp;</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
	</div>
	</div>
	<div style="height:5px;"></div>
	<div class="row-fluid">
	<div class="B-small-from-table-autoConcise">
	<h4 class="title">答疑信息
	<span class="pull-right">
	    <button id="clean" class="btn" type="button">新增</button>
		<button id="insert" class="btn" type="button">保 存</button>
		<button id="shanchu" class="btn" type="button">删除</button>
	</span></h4>
	<form method="post" id="DyqkForm">
		<table class="B-table" width="100%">
			
			<TR style="display: none;">
			<!-- <TR> -->
				<TD>
			    <INPUT class="span12" type="text" name="ID" fieldname="GC_ZJB_DYQK_ID" id="ID" />
				<INPUT class="span12" type="text" id="GC_ZJB_LBJB_ID" name="LBJID" fieldname="LBJID" keep="true"/>
				<INPUT class="span12" type="text" id="JHSJID" name="JHSJID" fieldname="JHSJID"  keep="true"/>
				<INPUT class="span12" type="text" id="SJWYBH" name="SJWYBH" fieldname="SJWYBH"  keep="true"/>
				<INPUT class="span12" type="text" id="JHID" name="JHID" fieldname="JHID"  keep="true"/>
				<INPUT class="span12" type="text" id="ND" name="ND" fieldname="ND"  keep="true"/>
				<INPUT class="span12" type="text" id="XMID" name="XMID" fieldname="XMID"  keep="true"/>
				<INPUT class="span12" type="text" id="BDID" name="BDID" fieldname="BDID"  keep="true"/>
				</TD>
			</TR>	
			<tr>
				<th width="8%" class="right-border bottom-border text-right">提问时间</th>
				<td width="17%" class="right-border bottom-border"><input class="span12 date" type="date" placeholder="必填" check-type="required" id="TWRQ" name="TWRQ" fieldname="TWRQ"/></td>
				<th width="8%" class="right-border bottom-border text-right">答疑时间</th>
				<td width="17%"  class="bottom-border"><input class="span12 date" type="date" id="DYRQ" name="DYRQ" fieldname="DYRQ" /></td>
			    <td  colspan="3" class="bottom-border">
			</tr>
			<tr>
				<th width="8%" class="right-border bottom-border text-right">备注</th>
				<td width="92%" colspan="7" class="bottom-border"><textarea class="span12" rows="3" id="BZ" name="BZ" fieldname="BZ" check-type="maxlength" maxlength="500"></textarea></td>
			</tr>
			<tr>
			<th width="8%" class="right-border bottom-border text-right">提问附件</th>
			<td colspan="5" class="bottom-border right-border">
				<div>
				<span class="btn btn-fileUpload" onclick="doSelectFile(this);" id="shangchuanID" fjlb="2026">
					<i class="icon-plus"></i>
						<span>添加文件...</span></span>
							<table role="presentation" class="table table-striped">
								<tbody fjlb="2026" id="shangchuanID1" class="files showFileTab"
									data-toggle="modal-gallery" data-target="#modal-gallery">
								</tbody>
							 </table>
				</div>
			  </td>
			</tr>
			<tr>
			<th width="8%" class="right-border bottom-border text-right">答疑附件</th>
			<td colspan="5" class="bottom-border right-border">
				<div>
				<span class="btn btn-fileUpload" onclick="doSelectFile(this);" id="shangchuanID" fjlb="2027">
					<i class="icon-plus"></i>
						<span>添加文件...</span></span>
							<table role="presentation" class="table table-striped">
								<tbody fjlb="2027" id="shangchuanID1" class="files showFileTab"
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
	  <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true" />
	<div align="center">
		<FORM name="frmPost" method="post" style="display: none" target="_blank" id="frmPost" >
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML"> 
			<input type="hidden" name="txtXML" id="txtXML"> 
			<input type="hidden" name="resultXML" id="resultXML">
			<input type="hidden" name="queryResult" id = "queryResult" />
			<input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ" id = "txtFilter">
			<input type="text" name="ywid" id = "ywid" value="">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
		</FORM>
	</div>
</body>
</html>