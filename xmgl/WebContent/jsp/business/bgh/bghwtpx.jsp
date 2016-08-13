<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>统筹计划管理-维护</title>
<%
java.util.Calendar cal = java.util.Calendar.getInstance();
int year = cal.get(java.util.Calendar.YEAR);
%>

<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
	var controllername= "${pageContext.request.contextPath }/banGongHuiWenTiController.do";
	//页面初始化
	$(function() {
		init();
	});
	//页面默认参数
	function init(){
		g_bAlertWhenNoResult = false;
		queryList();
		g_bAlertWhenNoResult = true;
		//按钮绑定事件（清空）
	    $("#btnSave").click(function() {
	    	var indexarry = new Array();
			indexarry = $("#DT1").getChangeRows();
			if(indexarry == "")
			{
				xInfoMsg('请至少修改一条记录！',"");
				return
	 		}
	    	update(); 
	    });
	}
	//查询列表   
	function queryList(){
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?querybanGongHuiWen",data,DT1,null,false);
	}
	//标段名称
    function doBdmc(obj){
		 var bd_name=obj.BDMC;
		 if(bd_name==null||bd_name==""){
			 return '<div style="text-align:center">—</div>';
		 }else{
			 return bd_name;			  
		 }
	  }
  	//项目名称
    function doXMmc(obj){
  	  	var xm_name=obj.XMMC;
  	  	if(xm_name==null||xm_name==""){
  		  return '<div style="text-align:center">—</div>';
  	  	}else{
  		  	return xm_name;			  
  	  	}
    }
  	function update(){
	    var url = controllername + "?updatebatchdata";
		var indexarry = new Array();//获取所有行的数组
		var indexArryChange = new Array();//获得经变更行的数组
		indexArryChange = $("#DT1").getChangeRows();
		var tids = "";
		for(var i =0;i<indexArryChange.length;i++){
			tids +=$("#DT1").getRowJsonObjByIndex(indexArryChange[i]).GC_BGH_WT_ID+",";
		}
		indexarry = $("#DT1").getAllRowsJOSNString();
		//获取表格表头的数组,按照表格显示的顺序
		var tharrays = new Array();
		var comprisesData;
		tharrays = $("#DT1").getTableThArrays();
		
		if(tharrays != null){
			comprisesData = $("#DT1").comprisesData(indexarry,tharrays);
			defaultJson.doUpdateBatchJson(url+"&TIDS="+tids, comprisesData,DT1,indexarry,"callbackUpdateDate");
		}
  	}
  	//修改回调
  	function callbackUpdateDate(){
  		queryList();
  		$(window).manhuaDialog.getParentObj().init();
  	}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
      <h4 class="title">
      	<span class="pull-right">
      	<button id="btnSave" class="btn"  type="button">保存</button>
      	</span>
      </h4>
     <form method="post" id="queryForm">
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border">
	        <INPUT id="num" type="text" class="span12" keep="true" kind="text" fieldname="rownum" value="1000" operation="<="/>
	        </TD>
			<TD class="right-border bottom-border">
			<INPUT id="ZT" type="text" class="span12" keep="true" kind="text" fieldname="A.ZT" value="1" operation="<="/>
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
      </table>
      </form>
    </div>
  </div>
 <div class="row-fluid">
 <div class="B-small-from-table-auto">
	<table class="table-hover table-activeTd B-table" id="DT1"  width="100%" editable="1" type="multi" noPage="true" pageNum="1000">
		<thead>
			<tr>
		    <th  name="XH" id="_XH"  >&nbsp;#&nbsp;</th>
		    <th fieldname="XUHAO" width="5%" tdalign="center" type="text" >&nbsp;排序&nbsp;</th>
		    <th fieldname="WTBT" maxlength="15" >&nbsp;标题&nbsp;</th>
		    <th fieldname="WTLX">&nbsp;类型&nbsp;</th>
		    <th fieldname="ZT">&nbsp;状态&nbsp;</th>
		    <th fieldname="XWJJSJ" tdalign="center"  >&nbsp;希望解决时间&nbsp;</th>
		 	<th fieldname="XMMC" Customfunction="doXMmc"    maxlength="15" >&nbsp;项目名称&nbsp;</th>
		 	<th fieldname="BDMC" maxlength="15" Customfunction="doBdmc" >&nbsp;标段名称&nbsp;</th>
		 	<th fieldname="FQR">&nbsp;发起人&nbsp;</th>
		 	<th fieldname="FQSJ" tdalign="center">&nbsp;发起日期&nbsp;</th>
			</tr>
		</thead>
		<tbody>
        </tbody>
	</table>
	</div>
	</div>
</div>
    
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "a.xuhao,a.lrsj"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="queryResult" id = "queryResult">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>