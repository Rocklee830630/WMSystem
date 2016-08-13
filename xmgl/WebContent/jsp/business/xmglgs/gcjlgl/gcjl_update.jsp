<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>工程计量管理-修改</title>
<%
	String id = request.getParameter("id");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/gcjl/gcjlController.do";
var id = "<%= id%>";
var defaultData = "";
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#gcjlList").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		 
		if($("#gcjlForm").validationButton())
		{
			if($("#JLYF").val() != defaultData){
				var flag = queryDate($("#id").val(),$("#JLYF").val());
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
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
    });
	//按钮绑定事件（删除）
    $("#btnDel").click(function(){
    	if($("#gcjlList").getSelectedRowIndex()==-1){
    		requireSelectedOneRow();
    		return;
    	}else{
   		 	var data = Form2Json.formToJSON(gcjlForm);
   			var data1 = defaultJson.packSaveJson(data);
   			xConfirm("提示信息","是否确认删除！");
   			$('#ConfirmYesButton').unbind();
   			$('#ConfirmYesButton').one("click",function(){  
   				defaultJson.doDeleteJson(controllername+"?delete",data1,gcjlList,null); 
   				$("#gcjlForm").clearFormResult();
   				var parentmain=$(window).manhuaDialog.getParentObj();	
    			parentmain.queryList();
   			});
    		
    	}
    });
});
//页面默认参数
function init(){
	$("#id").val(id);
	getNd();
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}

//点击行事件
function tr_click(obj){
	defaultData = obj.JLYF;
	$("#gcjlForm").setFormValues(obj);
}
//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcjlList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryByTcjhId",data,gcjlList);
}
//默认年度
function getNd(){
	setDefaultOption($("#qnd"),new Date().getFullYear());
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

//标段编号图标样式
function doBdbh(obj){
	if(obj.BDID == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDBH;
	}
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<input type="text" id="id" name="TCJH_SJ_ID" fieldname="TCJH_SJ_ID" keep="true" operation="="/>
			</TD>
        </TR>
         <tr>
	        <th width="5%" class="right-border bottom-border text-right">计量月份</th>
			<td class="right-border bottom-border" width="6%">
				<input class="span12 date" id="jlyf" type="month" name="JLYF" fieldtype="month" fieldformat="yyyy-mm" check-type="month" fieldname="JLYF" operation="="/>
			</td>
			<td class="text-left bottom-border text-right">
	        	<button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
	        	<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
	        	
	        </td>
		</tr>
      </table>
      </form>
    <div style="height:5px;"></div>
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="gcjlList" width="100%" type="single" pageNum="5" nopromptmsg="true">
		<thead>
			<tr>
				<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
				<th fieldname="JLYF" rowspan="2" colindex=2 tdalign="center">&nbsp;计量月份&nbsp;</th>
				<th fieldname="XMMC" rowspan="2" colindex=3 maxlength="15">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDID" rowspan="2" colindex=4 CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" rowspan="2" colindex=5 maxlength="15">&nbsp;标段名称&nbsp;</th>
				<th fieldname="HTBM" rowspan="2" colindex=6 tdalign="center">&nbsp;合同编号&nbsp;</th>
				<th fieldname="HTQDJ" rowspan="2" colindex=7 tdalign="right">&nbsp;合同价&nbsp;</th>
				<th colspan="5">&nbsp;当月计量(单位：元)&nbsp;</th>
				<th colspan="5">&nbsp;累计计量(单位：元)&nbsp;</th>
				<th fieldname="JZJLZ" rowspan="2" colindex=18 tdalign="right">&nbsp;以前年度结转监理审定值&nbsp;</th>
				<th fieldname="LJJLZ" rowspan="2" colindex=19 tdalign="right">&nbsp;当前年度结转监理审定值&nbsp;</th>
				<th fieldname="WGBFB" rowspan="2" colindex=20 tdalign="right">&nbsp;完工百分比&nbsp;</th>
				<th fieldname="BZ" rowspan="2" colindex=21 maxlength="15">&nbsp;备注&nbsp;</th>
			</tr>
			<tr>
				<th fieldname="GCK_DY" colindex=8 tdalign="right">&nbsp;甲供材款&nbsp;</th>
				<th fieldname="GCK1_DY" colindex=9 tdalign="right">&nbsp;工程款&nbsp;</th>
				<th fieldname="DYJLSDZ" colindex=10 tdalign="right">&nbsp;监理审定值&nbsp;</th>
				<th fieldname="DYZBJ" colindex=11 tdalign="right">&nbsp;质保金&nbsp;</th>
				<th fieldname="DYYFK" colindex=12 tdalign="right">&nbsp;当月应付款&nbsp;</th>
				
				<th fieldname="GCK_LJ" colindex=13 tdalign="right">&nbsp;甲供材款&nbsp;</th>
				<th fieldname="GCK1_LJ" colindex=14 tdalign="right">&nbsp;工程款&nbsp;</th>
				<th fieldname="LJJLSDZ" colindex=15 tdalign="right">&nbsp;监理审定值&nbsp;</th>
				<th fieldname="LJZBJ" colindex=16 tdalign="right">&nbsp;质保金&nbsp;</th>
				<th fieldname="LJDYYFK" colindex=17 tdalign="right">&nbsp;累计应付款&nbsp;</th>
				
			</tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	</div>
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">计量信息
      	<span class="pull-right">
	  		<button id="btnSave" class="btn" type="button">保存</button>
	  		<button id="btnDel" class="btn" type="button">删除</button>
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
          	<input class="span12" style="width:65%;text-align:right" id="GCK_DY" type="number" onblur="countDY_XJ()" fieldname="GCK_DY" name = "GCK_DY" min="0"><b>（元）</b>
          </td>
         <th width="8%" class="right-border bottom-border text-right" rowspan="2">小计</th>
         <td class="bottom-border right-border" rowspan="2" colspan="2">
          	<input class="span12" style="width:65%;text-align:right" id="XJ_DY" type="number" fieldname="XJ_DY"  name = "XJ_DY" min="0"><b>（元）</b>
         </td>
         <th width="8%" class="right-border bottom-border text-right" rowspan="2">质保金</th>
         <td class="bottom-border right-border" rowspan="2" colspan="2">
          	<input class="span12" style="width:65%;text-align:right" id="DYZBJ" type="number" fieldname="DYZBJ"  name = "DYZBJ" min="0"><b>（元）</b>
         </td>
         <th width="8%" class="right-border bottom-border text-right" rowspan="2">当月应付款</th>
         <td class="bottom-border right-border" rowspan="2">
         	<input class="span12" style="width:65%;text-align:right" id="DYYFK" type="number" fieldname="DYYFK" name = "DYYFK" min="0"><b>（元）</b>
         </td>
		</tr>
		<tr>
         <th width="5%" class="right-border bottom-border text-right">工程款</th>
          <td class="right-border bottom-border" width="12%">
          	<input class="span12" style="width:65%;text-align:right" id="GCK1_DY" type="number" onblur="countDY_XJ()" fieldname="GCK1_DY"  name = "GCK1_DY" min="0"><b>（元）</b>
          </td>
		</tr>
        <tr>
         <th width="8%" class="right-border bottom-border text-right" rowspan="2">累计计量</th>
          <th width="5%" class="right-border bottom-border text-right">甲供材款</th>
          <td class="right-border bottom-border" width="18%">
          	<input class="span12" style="width:65%;text-align:right" id="GCK_LJ" type="number" onblur="countLJ_XJ()" fieldname="GCK_LJ"  name = "GCK_LJ" min="0"><b>（元）</b>
          </td>
         <th width="8%" class="right-border bottom-border text-right" rowspan="2">小计</th>
         <td class="bottom-border right-border" rowspan="2" colspan="2">
          	<input class="span12" style="width:65%;text-align:right" id="XJ_LJ" type="number" fieldname="XJ_LJ" name = "XJ_LJ" min="0"><b>（元）</b>
         </td>
         <th width="8%" class="right-border bottom-border text-right" rowspan="2">质保金</th>
         <td class="bottom-border right-border" rowspan="2" colspan="2">
          	<input class="span12" style="width:65%;text-align:right" id="LJZBJ" type="number" fieldname="LJZBJ"  name = "LJZBJ" min="0"><b>（元）</b>
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
         		<input class="span12" style="width:80%;text-align:right" id="LJJLZ" type="number" fieldname="LJJLZ" name = "LJJLZ" min="0"><b>（元）</b>
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
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "JLYF" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>

</html>