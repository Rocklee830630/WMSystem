<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>项目管理公司-开工条件管理</title>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/common_yw/kgtjgl/kgtjglController.do";
var p_selectIndex = '0';
//页面初始化
$(function() {	
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		 if(exportRequireQuery($("#kgtjgl"))){//该方法需传入表格的jquery对象
		      printTabList("kgtjgl","gcjlgl.xls","XMBH,XMID,BDID,HTID,JLYF,DYJLSDZ","2,1");
		  }
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
});


//修改状态
function insert_update(flag){
	var num=0;
	$("[name=subBox]:checkbox:checked").each(function(){ 
		num++;
	});
	if(num==0)
	{
		requireSelectedOneRow();
		return;
	}	
	xConfirm("提示信息","是否修改选中信息！");
	$('#ConfirmYesButton').unbind();
	$('#ConfirmYesButton').one("click",function(){ 
		var actionFlag = true;
		$("[name=subBox]:checkbox:checked").each(function(){ 
			var hang =$(this).closest("tr").find("th").text()-1;
			$("#kgtjgl").setSelect(hang);
			//生成json串
			var data=$("#kgtjgl").getSelectedRow();
			var obj=convertJson.string2json1(data);
			data=JSON.stringify(obj);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			//调用ajax插入
			actionFlag = defaultJson.doUpdateJson(controllername + "?insert&flag="+flag+"&nd="+$("#QueryND").val(),data1, kgtjgl);
			if(actionFlag==false){
				return false;
			}
		});
		if(actionFlag==false){
			xAlert("信息提示","更新失败！");
		}else{
			$("#checkAll").removeAttr("checked");//取消#右侧复选框选中状态
			xAlert("信息提示","更新成功！");
		}
	 }); 
}


//页面默认参数
function init(){
	setPageHeight();
	initCommonQueyPage();
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}

//计算本页表格分页数
function setPageHeight(){
	//计算本页表格分页数
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#kgtjgl").attr("pageNum",pageNum);
}
//复选框
function cks(obj){
	  var hangshu=obj.IDNUM-1;
		 return "<input type='checkbox' colindex='"+hangshu+"' name='subBox' id='fuxuan'>"; 
	}
//复选框全选取消
$(function() {
    $("#checkAll").click(function() {
         $('input[name="subBox"]').prop("checked",this.checked); 
     });
     var sub = $("input[name='subBox']");
     sub.click(function(){
         $("#checkAll").prop("checked",sub.length == $("input[name='subBox']:checked").length ? true : false);
     });
 });
//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,kgtjgl);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,kgtjgl);
}


//详细信息
function rowView(index){
	var obj = $("#kgtjgl").getSelectedRowJsonByIndex(index);
	var id = convertJson.string2json1(obj).XMID;
	$(window).manhuaDialog(xmscUrl(id));
}
//标段图标样式
function doBdmc(obj){
	if(obj.XMBS == "0"){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
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
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">开工条件管理
      	<span class="pull-right">
   			<button class="btn" onclick="insert_update(0)" type="button">具备条件</button>
   			<button class="btn" onclick="insert_update(1)" type="button">暂缓开工</button>
   			<button id="btnExpExcel" class="btn"  type="button">导出</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
		 <jsp:include page="/jsp/business/common/commonQuery.jsp" flush="true">
         	<jsp:param name="prefix" value="A"/> 
         </jsp:include>
			<td class="text-left bottom-border text-right">
	        	<button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
	        	<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
	        </td>
		</tr>
      </table>
      </form>
<div style="height:5px;"> </div>
	<div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="kgtjgl" width="100%" withoutAlertFlag="true" type="single" pageNum="10" printFileName="工程计量">
		<thead>
			<tr>
				<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
            	<th  fieldname="XMBH"   tdalign="center" CustomFunction="cks">
               	<input type="checkbox" name="fuxuans"  id="checkAll" > 
               	</th>				
				<th fieldname="XMBH" rowMerge="true" hasLink="true" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" rowMerge="true" maxlength="15">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDID" CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" CustomFunction="doBdmc" maxlength="15">&nbsp;标段名称&nbsp;</th>
				<th fieldname="XMDZ" maxlength="15">&nbsp;项目地址&nbsp;</th>
				<th fieldname="KGTJZT" tdalign="center">&nbsp;&nbsp;开工条件</th>
			</tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	</div>
   </div>
 </div>
    
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
      <input type="hidden" name="txtFilter"  order="ASC" fieldname = "A.XMBH,A.XMBS,A.PXH" id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>