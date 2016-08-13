<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>合同项目/标段-维护</title>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/htgl/gcHtglHtsjController.do";
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtsjList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,gcHtglHtsjList);
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#gcHtglHtsjForm").validationButton())
		{
		    //生成json串
		    var data = Form2Json.formToJSON(gcHtglHtsjForm);
		  //组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  //调用ajax插入
		    if($("#ID").val() == "" || $("#ID").val() == null){
    			defaultJson.doInsertJson(controllername + "?insert", data1,gcHtglHtsjList);
    			$("#gcHtglHtsjForm").clearFormResult();
    		}else{
    			defaultJson.doUpdateJson(controllername + "?insert", data1,gcHtglHtsjList);
    		}
		}else{
			requireFormMsg();
		  	return;
		}
	});
	
	//按钮绑定事件（新增）
    $("#btnClear_Bins").click(function() {
        $("#gcHtglHtsjForm").clearFormResult();
        $("#gcHtglHtsjForm").cancleSelected();
        
        
        $("#ZFRQ").val(new Date().toLocaleDateString());
        $("#ZFJE").val(0);
        $("#ID").val("");
    });
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
	
});
//页面默认参数
function init(){
	getNd();
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,gcHtglHtsjList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,gcHtglHtsjList);
}


//点击行事件
function tr_click(obj){
	//alert(JSON.stringify(obj));
	$("#gcHtglHtsjForm").setFormValues(obj);
}

//默认年度
function getNd(){
	//年度信息，里修改
	var y = new Date().getFullYear();
	$("#QZFRQ option").each(function(){
		if(this.value == y){
		 	$(this).attr("selected", true);
		 	return true;
		}
	});
}

//选中项目名称弹出页
function selectXm(){
	$("body").manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath }/jsp/business/zjgl/xmcx.jsp","modal":"2"});
}
//弹出区域回调
getWinData = function(data){
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#XMBH").val(JSON.parse(data).XMBH);
	$("#GC_TCJH_XMXDK_ID").val(JSON.parse(data).GC_TCJH_XMXDK_ID);
};

//详细信息
function rowView(index){
	var obj = $("#gcHtglHtsjList").getSelectedRowJsonByIndex(index);
	var xmbh = eval("("+obj+")").XMBH;
	$("body").manhuaDialog(xmscUrl(xmbh));
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
			</TD>
        </TR>
         <tr>
        	<th width="5%" class="right-border bottom-border text-right">年度</th>
	        <td class="right-border bottom-border" width="10%">
	            <select class="span12" type="date" fieldtype="year" fieldformat="yyyy" id="QZFRQ" name="ZFRQ" fieldname="ZFRQ" operation="=" kind="dic" src="XMNF"  defaultMemo="-年度-">
	        </td>
	        <th width="5%" class="right-border bottom-border text-right">项目名称</th>
			<td class="right-border bottom-border" width="20%">
				<input class="span12" type="text" name = "QXMMC" fieldname = "gtx.XMMC" operation="like" >
			</td>
			<td class="text-left bottom-border text-right">
	        	<button id="btnQuery" class="btn btn-link"  type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-search"></i>查询</button>
	        	<button id="btnClear" class="btn btn-link" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-trash"></i>清空</button>
	        </td>
		</tr>
      </table>
      </form>
    <div style="height:5px;"></div>
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="gcHtglHtsjList" width="100%" type="single" pageNum="5">
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
				<th fieldname="ID" colindex=0 tdalign="center" maxlength="30" >&nbsp;编号&nbsp;</th>
				<th fieldname="JHSJID" colindex=1 tdalign="center" maxlength="30" >&nbsp;统筹计划数据ID&nbsp;</th>
				<th fieldname="XMBH" colindex=2 tdalign="center" maxlength="30" >&nbsp;项目编号&nbsp;</th>
				<th fieldname="BDID" colindex=3 tdalign="center" maxlength="30" >&nbsp;标段ID&nbsp;</th>
				<th fieldname="HTID" colindex=4 tdalign="center" maxlength="30" >&nbsp;合同编号&nbsp;</th>
				<th fieldname="HTQDJ" colindex=5 tdalign="center" >&nbsp;合同签订价(元)&nbsp;</th>
				<th fieldname="GCJGBFB" colindex=6 tdalign="center" >&nbsp;价格百分比&nbsp;</th>
				<th fieldname="HTJSJ" colindex=7 tdalign="center" >&nbsp;合同结算价或中止价&nbsp;</th>
				<th fieldname="MBLX" colindex=8 tdalign="center" maxlength="30" >&nbsp;目标类型:变更、支付、完成投资、文件&nbsp;</th>
				<th fieldname="MBID" colindex=9 tdalign="center" maxlength="30" >&nbsp;目标ID&nbsp;</th>
				<th fieldname="JE" colindex=10 tdalign="center" >&nbsp;金额&nbsp;</th>
				<th fieldname="MBSJLX" colindex=11 tdalign="center" maxlength="30" >&nbsp;目标数据类型&nbsp;</th>
				<th fieldname="RQ" colindex=12 tdalign="center" >&nbsp;日期&nbsp;</th>
				<th fieldname="MC" colindex=13 tdalign="center" maxlength="30" >&nbsp;名称&nbsp;</th>
				<th fieldname="BGTS" colindex=14 tdalign="center" >&nbsp;变更天数&nbsp;</th>
				<th fieldname="HTDID" colindex=15 tdalign="center" maxlength="30" >&nbsp;会签单ID&nbsp;</th>
				<th fieldname="YWLX" colindex=16 tdalign="center" >&nbsp;业务类型&nbsp;</th>
				<th fieldname="SJBH" colindex=17 tdalign="center" maxlength="30" >&nbsp;事件编号&nbsp;</th>
				<th fieldname="SJMJ" colindex=18 tdalign="center" maxlength="30" >&nbsp;数据密级&nbsp;</th>
				<th fieldname="SFYX" colindex=19 tdalign="center" >&nbsp;是否有效&nbsp;</th>
				<th fieldname="BZ" colindex=20 tdalign="center" maxlength="30" >&nbsp;备注&nbsp;</th>
				<th fieldname="LRR" colindex=21 tdalign="center" >&nbsp;录入人&nbsp;</th>
				<th fieldname="LRSJ" colindex=22 tdalign="center" >&nbsp;录入时间&nbsp;</th>
				<th fieldname="LRBM" colindex=23 tdalign="center" >&nbsp;录入部门&nbsp;</th>
				<th fieldname="LRBMMC" colindex=24 tdalign="center" maxlength="30" >&nbsp;录入部门名称&nbsp;</th>
				<th fieldname="GXR" colindex=25 tdalign="center" >&nbsp;更新人&nbsp;</th>
				<th fieldname="GXSJ" colindex=26 tdalign="center" >&nbsp;更新时间&nbsp;</th>
				<th fieldname="GXBM" colindex=27 tdalign="center" >&nbsp;更新部门&nbsp;</th>
				<th fieldname="GXBMMC" colindex=28 tdalign="center" maxlength="30" >&nbsp;更新部门名称&nbsp;</th>
				<th fieldname="SORTNO" colindex=29 tdalign="center" >&nbsp;排序号&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	</div>
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">合同项目/标段
      	<span class="pull-right">
      		<button id="btnClear_Bins" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">清空</button>
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
      	</span>
      </h4>
     <form method="post" id="gcHtglHtsjForm"  >
      <table class="B-table" width="100%" >
      <input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
	  	<input type="hidden" id="GC_TCJH_XMXDK_ID" fieldname="XMID" name = "XMID"/></TD>
        <tr>
			<th width="8%" class="right-border bottom-border text-right">项目名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="XMMC" type="text" placeholder="必填" check-type="required" fieldname="XMMC" name = "XMMC"  disabled />
          		<button class="btn btn-link"  type="button" onclick="selectXm()"><i class="icon-edit"></i></button>
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right">项目编号</th>
       		<td class="bottom-border right-border"width="15%">
         		<input class="span12" style="width:100%" id="XMBH" type="text" fieldname="XMBH" name = "XMBH"  disabled/>
         	</td>
        </tr>
        <tr>
			<th width="8%" class="right-border bottom-border text-right">支付金额</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZFJE" class="span12" keep="true" placeholder="必填" check-type="required" check-type="maxlength" maxlength="17" value=0 style="width:70%;text-align:right;" name="ZFJE" fieldname="ZFJE" type="number" />&nbsp;<b>(元)</b>
			</td>
			<th width="8%" class="right-border bottom-border text-right">支付日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZFRQ" class="span12" placeholder="必填" check-type="required" check-type="maxlength" maxlength="10" name="ZFRQ" fieldname="ZFRQ" type="text" />
			</td>
		</tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right">联系人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="LXR" class="span12" placeholder="必填" check-type="required" check-type="maxlength" maxlength="36" name="LXR" fieldname="LXR" type="text" />
			</td>
			<th width="8%" class="right-border bottom-border text-right">联系方式</th>
			<td width="17%" class="right-border bottom-border">
				<input id="LXFS" class="span12" placeholder="必填" check-type="required" check-type="maxlength" maxlength="40" name="LXFS" fieldname="LXFS" type="text" />
			</td>
		</tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right">备注</th>
	        <td colspan="3" class="bottom-border right-border" >
	        	<textarea class="span12" rows="2" id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ"></textarea>
	        </td>
        </tr>
      </table>
      </form>
    </div>
   </div>
  </div>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "t.LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
<script>
</script>
</html>