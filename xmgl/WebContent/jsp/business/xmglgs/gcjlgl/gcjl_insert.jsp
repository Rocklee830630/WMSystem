<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>工程计量管理-新增</title>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/gcjl/gcjlController.do";
//页面初始化
$(function() {
	init();
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		 
		if($("#gcjlForm").validationButton())
		{
			var flag = queryDate($("#TCJH_SJ_ID").val(),$("#JLYF").val());
			if(flag == "0"){
				xInfoMsg('该月份已存在计量信息！',"");
				return;
			}else{
				 //生成json串
			    var data = Form2Json.formToJSON(gcjlForm);
			    $(window).manhuaDialog.setData(data);
	   			$(window).manhuaDialog.sendData();
	   			$(window).manhuaDialog.close();
			} 
		   
		}else{
			requireFormMsg();
		  	return;
		}
	});
    //按钮绑定事件（新增）
    $("#btnInsert").click(function() {
        $("#gcjlForm").clearFormResult();
        $("#gcjlList").cancleSelected();
        $("#WGBFB").val("");
        $("#JLYF").val("");
        $("#BZ").val("");
        
        $("#GCK_DY").val(0);
        $("#GCK1_DY").val(0);
        $("#XJ_DY").val(0);
        $("#DYZBJ").val(0);
        $("#DYYFK").val(0);
        $("#GCK_LJ").val(0);
        $("#GCK1_LJ").val(0);
        $("#XJ_LJ").val(0);
        $("#LJZBJ").val(0);
        $("#LJDYYFK").val(0);
        $("#JZJLZ").val(0);
        $("#LJJLZ").val(0);
       
    });
	
});
//页面默认参数
function init(){
	setFromDate();
	clearJlData();
}
//回调值
function setFromDate(){
	var pwindow =$(window).manhuaDialog.getParentObj();
	var rowValue = pwindow.document.getElementById("resultXML").value;
	//    $(parent.frames["menuiframe"].document).manhuaDialog ("#resultXML").val();
	var tempJson = convertJson.string2json1(rowValue);
	$("#gcjlForm").setFormValues(tempJson);
	//$("#HTID").val(tempJson.ID_SV);
	//$("#HTID").attr("code",tempJson.ID);
	$("#HTJ").val(tempJson.HTQDJ);
	$("#TCJH_SJ_ID").val(tempJson.GC_JH_SJ_ID);
	$("#jlyf").val("");

}
//清空计量信息
function clearJlData(){
	$("#GC_XMGLGS_JLB_ID").val("");
	$("#WGBFB").val("");
    $("#JLYF").val("");
    $("#BZ").val("");
    $("#GCK_DY").val(0);
    $("#GCK1_DY").val(0);
    $("#XJ_DY").val(0);
    $("#DYZBJ").val(0);
    $("#DYYFK").val(0);
    $("#GCK_LJ").val(0);
    $("#GCK1_LJ").val(0);
    $("#XJ_LJ").val(0);
    $("#LJZBJ").val(0);
    $("#LJDYYFK").val(0);
    $("#JZJLZ").val(0);
    $("#LJJLZ").val(0);
}
//判断所选日期是否已存在
function queryDate(jhsjid,dateValue){
	var text = "";
	var actionName=controllername+"?queryDate&jhsjid="+jhsjid+"&dateValue="+dateValue;
	$.ajax({
		url : actionName,
		data : null,
		cache : false,
		async :	false,
		dataType : "json",  
		type : 'post',
		success : function(result) {
			text = result.msg;
		},
	    error : function(result) {
		}
	});
    return text;
}

//计算当月小计
function countDY_XJ(){
	var dy_gck = $("#GCK_DY").val();
	var dy_gck1 = $("#GCK1_DY").val();
	$("#XJ_DY").val( parseFloat(dy_gck) + parseFloat(dy_gck1));
}

//计算累计小计
function countLJ_XJ(){
	var lj_gck = $("#GCK_LJ").val();
	var lj_gck1 = $("#GCK1_LJ").val();
	$("#XJ_LJ").val( parseFloat(lj_gck) + parseFloat(lj_gck1));
}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">计量信息
      	<span class="pull-right">
      		
	  		<button id="btnSave" class="btn" type="button">保存</button>
      	</span>
      </h4>
     <form method="post" id="gcjlForm"  >
      <table class="B-table" width="100%" >
	  	<input type="hidden" id="GC_XMGLGS_JLB_ID" fieldname="GC_XMGLGS_JLB_ID" name = "GC_XMGLGS_JLB_ID"/></TD>
	  	<input type="hidden" id="XMID" fieldname="XMID" name = "XMID" keep="true"/>
	  	<input type="hidden" id="BDID" fieldname="BDID" name = "BDID" keep="true"/>
	  	<input type="hidden" id="ND" fieldname="ND" name = "ND" keep="true"/>
	  	<input type="hidden" id="TCJH_SJ_ID" fieldname="TCJH_SJ_ID" name = "TCJH_SJ_ID" keep="true"/>
        <tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
       	 	<td class="bottom-border right-border" colspan="2" width="23%">
         		<input class="span12" id="XMMC" type="text" fieldname="XMMC" name = "XMMC" keep="true"  readonly />
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
       		<td class="bottom-border right-border" colspan="2" width="15%">
         		<input class="span12" id="BDMC" type="text" fieldname="BDMC" name = "BDMC" keep="true" readonly />
         	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">项目属性</th>
        	<td class="bottom-border right-border" colspan="2" width="15%">
        		<input class="span12" id="XMSX" type="text" fieldname="XMSX" name = "XMSX" keep="true" readonly />
          	</td>
          	<th width="8%" class="right-border bottom-border text-right disabledTh">项目性质</th>
        	<td class="bottom-border right-border" colspan="2" width="15%">
            	<input class="span12" id="XMXZ" type="text" fieldname="XMXZ" name = "XMXZ" keep="true" readonly />
          	</td>
        </tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right disabledTh">项目管理公司</th>
       		<td class="bottom-border right-border" colspan="2" width="23%">
           		<input class="span12" id="XMGLGS" type="text" fieldname="XMGLGS" name = "XMGLGS" keep="true" readonly />
         	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">设计单位</th>
       		<td class="bottom-border right-border" colspan="2" width="15%">
         		<input class="span12" id="SJDW" type="text" fieldname="SJDW" name = "SJDW" keep="true" readonly />
         	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">监理单位</th>
       		<td class="bottom-border right-border" colspan="2" width="15%">
         		<input class="span12" id="JLDW" type="text" fieldname="JLDW" name = "JLDW" keep="true" readonly />
         	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">施工单位</th>
       		<td class="bottom-border right-border" colspan="2" width="15%">
         		<input class="span12" id="SGDW" type="text" fieldname="SGDW" name = "SGDW" keep="true" readonly />
         	</td>
         </tr>
         <tr>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">合同编号</th>
       		<td class="bottom-border right-border" colspan="2" width="15%">
         		<input class="span12"  id="HTBM" type="text" fieldname="HTBM" name = "HTBM" keep="true" readonly />
         	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">合同价</th>
       		<td class="bottom-border right-border" colspan="2" width="15%">
         		<input class="span12"  id="HTJ" type="text" fieldname="HTJ" name = "HTJ" keep="true" readonly />
         	</td>
        </tr>
        <tr>
			<th width="8%" class="right-border bottom-border text-right">计量月份</th>
         	<td class="bottom-border right-border" colspan="2">
         		
           		<input class="span12 date" id="JLYF" type="month" name="JLYF" check-type="required" fieldname="JLYF"/>
         	</td>
         	<th width="8%" class="right-border bottom-border text-right">完成百分比</th>
         	<td class="bottom-border right-border" colspan="2">
         		<input class="span12" style="width:65%;text-align:right" id="WGBFB" type="number" fieldname="WGBFB" name = "WGBFB" min="0"><b>&nbsp;&nbsp;%</b>
         	</td>
         	<td colspan="5"></td>
         	
		</tr>
        <tr>
         <th width="8%" class="right-border bottom-border text-right" rowspan="2">当月计量</th>
          <th width="5%" class="right-border bottom-border text-right">甲供材款</th>
          <td class="right-border bottom-border" width="18%">
          	<input class="span12" style="width:65%;text-align:right" id="GCK_DY" type="number" onblur="countDY_XJ()" fieldname="GCK_DY"  name = "GCK_DY" min="0"><b>（元）</b>
          </td>
         <th width="8%" class="right-border bottom-border text-right" rowspan="2">小计</th>
         <td class="bottom-border right-border" rowspan="2" colspan="2">
          	<input class="span12" style="width:65%;text-align:right" id="XJ_DY" type="number" fieldname="XJ_DY"  name = "XJ_DY" min="0"><b>（元）</b>
         </td>
         <th width="8%" class="right-border bottom-border text-right" rowspan="2">质保金</th>
         <td class="bottom-border right-border" rowspan="2" colspan="2">
          	<input class="span12" style="width:65%;text-align:right" id="DYZBJ" type="number" fieldname="DYZBJ" name = "DYZBJ" min="0"><b>（元）</b>
         </td>
         <th width="8%" class="right-border bottom-border text-right" rowspan="2">当月应付款</th>
         <td class="bottom-border right-border" rowspan="2">
         	<input class="span12" style="width:65%;text-align:right" id="DYYFK" type="number" fieldname="DYYFK" name = "DYYFK" min="0"><b>（元）</b>
         </td>
		</tr>
		<tr>
         <th width="5%" class="right-border bottom-border text-right">工程款</th>
          <td class="right-border bottom-border" width="12%">
          	<input class="span12" style="width:65%;text-align:right" id="GCK1_DY" type="number" onblur="countDY_XJ()" fieldname="GCK1_DY" name = "GCK1_DY" min="0"><b>（元）</b>
          </td>
		</tr>
        <tr>
         <th width="8%" class="right-border bottom-border text-right" rowspan="2">累计计量</th>
          <th width="5%" class="right-border bottom-border text-right">甲供材款</th>
          <td class="right-border bottom-border" width="18%">
          	<input class="span12" style="width:65%;text-align:right" id="GCK_LJ" type="number" onblur="countLJ_XJ()" fieldname="GCK_LJ" name = "GCK_LJ" min="0"><b>（元）</b>
          </td>
         <th width="8%" class="right-border bottom-border text-right" rowspan="2">小计</th>
         <td class="bottom-border right-border" rowspan="2" colspan="2">
          	<input class="span12" style="width:65%;text-align:right" id="XJ_LJ" type="number" fieldname="XJ_LJ" name = "XJ_LJ" min="0"><b>（元）</b>
         </td>
         <th width="8%" class="right-border bottom-border text-right" rowspan="2">质保金</th>
         <td class="bottom-border right-border" rowspan="2" colspan="2">
          	<input class="span12" style="width:65%;text-align:right" id="LJZBJ" type="number" fieldname="LJZBJ" name = "LJZBJ" min="0"><b>（元）</b>
         </td>
         <th width="8%" class="right-border bottom-border text-right" rowspan="2">累计应付款</th>
         <td class="bottom-border right-border" rowspan="2">
         	<input class="span12" style="width:65%;text-align:right" id="LJDYYFK" type="number" fieldname="LJDYYFK"  name = "LJDYYFK" min="0"><b>（元）</b>
         </td>
		</tr>
		<tr>
         <th width="5%" class="right-border bottom-border text-right">工程款</th>
          <td class="right-border bottom-border" width="12%">
          	<input class="span12" style="width:65%;text-align:right" id="GCK1_LJ" type="number" onblur="countLJ_XJ()" fieldname="GCK1_LJ"  name = "GCK1_LJ" min="0"><b>（元）</b>
          </td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">以前年度监理审定值</th>
         	<td class="bottom-border right-border" colspan="5">
         		<input class="span12" style="width:80%;text-align:right" id="JZJLZ" type="number" fieldname="JZJLZ"  name = "JZJLZ" min="0"><b>（元）</b>
         	</td>
         	<th width="8%" class="right-border bottom-border text-right">当前年度监理审定值</th>
         	<td class="bottom-border right-border" colspan="5">
         		<input class="span12" style="width:80%;text-align:right" id="LJJLZ" type="number" fieldname="LJJLZ"  name = "LJJLZ" min="0"><b>（元）</b>
         	</td>
		</tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right">备注</th>
	        <td colspan="11" class="bottom-border right-border" >
	        	<textarea class="span12" id="BZ" rows="3" name ="BZ"  fieldname="BZ" maxlength="4000"></textarea>
	        </td>
        </tr>
      </table>
      </form>
    </div>
   </div>
  </div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "XMBH,LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>

</html>