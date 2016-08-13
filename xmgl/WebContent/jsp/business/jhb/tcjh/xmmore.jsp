<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<app:base/>
<title>项目下达</title>
 <%
 User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
 OrgDept dept = user.getOrgDept();
 String deptID=dept.getDeptID();
 String deptle=dept.getExtend1();
 %>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
var trlen; 


//初始化查询
$(document).ready(function(){
	/* var year=new Date().getFullYear()
	var num=document.getElementsByTagName("option");
	num[0].innerHTML=year;
	num[1].innerHTML=year+1; */
	var deptlx=<%=deptle%>;
	var deptID=<%=deptID%>;
	if(deptlx=="1")
		{
		 $("#XMGLGS").val(deptID);
		}
	generateNd($("#ND"));
	setDefaultOption($("#ND"),new Date().getFullYear());
	/* g_bAlertWhenNoResult=false; */
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryMoreXiangMu",data,DT1,"queryHuiDiao",true);
	var data=combineQuery.getQueryCombineData(queryForm2,frmPost,DT2);
	//调用ajax插入
	//defaultJson.doQueryJsonList(controllername+"?query_zc",data,DT2);
	/* g_bAlertWhenNoResult=true; */

});

function queryHuiDiao()
{
	trlen=$("#DT2 tr").length-1;
	var num=document.getElementsByTagName("font");
	num[0].innerHTML=trlen;
}
//页面初始化
$(function() {
	//按钮绑定事件(查询)
	$("#btnQuery").click(function() {
		//生成json串
		var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryMoreXiangMu",data,DT1,null,false);
	});
	
	
	//按钮绑定事件(下达统筹计划)项目
	$("#btnXdt").click(function() {
		//var ids = "";
		if($("#DT2").getTableRows()==0){
			xInfoMsg('没有可下达的项目！');
			return;
		}
		var ids = "";
		$("#DT2 tbody tr").each(function(){
			//存行数据
			var value = convertJson.string2json1($(this).attr("rowJson")).GC_TCJH_XMCBK_ID;
			ids+=value+",";
		});
		$("body").manhuaDialog({"title":"储备库项目下达","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xmcbk/xdxx.jsp?ids="+ids,"modal":"4"});
	});

	
	//按钮绑定事件（确定）
 	$("#btnFH").click(function() {
 		if(trlen=="0"){
 			requireSelectedOneRow();
		    return;
 		}
		//获取id
		var ids = "";
		var rowValues=new Array();
		$("#DT2 tbody tr").each(function(i){
			var rowValue = $(this).attr("rowJson");
			rowValues[i]=rowValue;
			var value = convertJson.string2json1($(this).attr("rowJson")).GC_JH_SJ_ID;
			ids+=value+",";
		});

		xConfirm("信息提示","是否确认下发这"+trlen+"个项目？");
		$('#ConfirmYesButton').unbind();
	 	$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){
			$.ajax({
				type : 'post',
				url : controllername+'?xdtcjhByXm&ids='+ids,
				data : null,
				dataType : 'json',
				async :	false,
				success : function(result) {
					xAlert("项目下发成功");
				},
			    error : function(result) {
			     	//alert(result.msg);
				    defaultJson.clearTxtXML();
				    success = false;
				}
			});
		});
	});

	
	//根据ID新增待下达
	doInsertZc = function(actionName, data1) {
	    var success  = true;
		$.ajax({
			type : 'post',
			url : actionName,
			data : data1,
			dataType : 'json',
			async :	false,
			success : function(result) {
				$("#resultXML").val(result.msg);
				success = true;
			},
		    error : function(result) {
		     	//alert(result.msg);
			    defaultJson.clearTxtXML();
			    success = false;
			}
		});
		 return success;
	};
	
	
	//按钮绑定事件(上移)
	$("#btnUp").click(function() {
		var rowindex=$("#DT2").getSelectedRowIndex();//获得选中行的索引
		if(rowindex==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		var rowValue=$("#DT2").getSelectedRow();//获得选中行的json对象
		$("#DT1").insertResult(rowValue,DT1,1);
		$("#DT2").removeResult(rowindex);
		trlen=$("#DT2 tr").length-1;
		var num=document.getElementsByTagName("font");
		num[0].innerHTML=trlen;
	});
	
	
	//按钮绑定事件(下移)
	$("#btnDown").click(function() {
		var rowindex=$("#DT1").getSelectedRowIndex();//获得选中行的索引
 		if(rowindex==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
 		var chongfu=chongfupanduan();
 		if(chongfu==false)
 			{
 			  xInfoMsg('项目已添加！');
 			  return 
 			}
		var rowValue=$("#DT1").getSelectedRow();//获得选中行的json对象
		$("#DT2").insertResult(rowValue,DT2,1);
		$("#DT1").removeResult(rowindex);
		trlen=$("#DT2 tr").length-1;
		var num=document.getElementsByTagName("font");
		num[0].innerHTML=trlen;
	});
	

	//按钮绑定事件(全选)
	$("#all").click(function() {
		if($("#DT1 tr").length==1)
		{
			xInfoMsg('没有可移动的项目！');
			return;
		}
		//判断是否有相同的项目
		$("#DT1 tbody tr").each(function(){
			var isinsert = true;
			var rowValue=convertJson.string2json1($(this).attr("rowJson"));
			/* $("#DT2").insertResult(JSON.stringify(rowValue),DT2,1); */
			var dtid = rowValue.GC_JH_SJ_ID;
			$("#DT2 tbody tr").each(function()
			   {
				  var rowValue1=convertJson.string2json1($(this).attr("rowJson"));
				  var dtid2=rowValue1.GC_JH_SJ_ID;
				  //alert(dtid+'---'+dtid);
				  if(dtid==dtid2)
					{
					isinsert = false;
					return
					}
			  });
			if(isinsert)
				{
			      $("#DT2").insertResult(JSON.stringify(rowValue),DT2,1);
				}
		});
		$("#DT1").clearResult();
		trlen=$("#DT2 tr").length-1;
		var num=document.getElementsByTagName("font");
		num[0].innerHTML=trlen;
	});
	
	
	//按钮绑定事件(取消)
	$("#cannel").click(function() {
		if($("#DT2 tr").length==1)
		{
			xInfoMsg('没有可移动的项目！');
			return;
		}	
	    /* 	$("#DT2 tbody tr").each(function(){
			var rowValue=convertJson.string2json1($(this).attr("rowJson"));
			$("#DT1").insertResult(JSON.stringify(rowValue),DT1,1);
		}); */
		//判断是否有相同的项目
		$("#DT2 tbody tr").each(function(){
			var isinsert = true;
			var rowValue=convertJson.string2json1($(this).attr("rowJson"));
			/* $("#DT2").insertResult(JSON.stringify(rowValue),DT2,1); */
			var dtid2 = rowValue.GC_JH_SJ_ID;
			$("#DT1 tbody tr").each(function()
			{
				var rowValue1=convertJson.string2json1($(this).attr("rowJson"));
				var dtid1=rowValue1.GC_JH_SJ_ID;
				//alert(dtid1+'---'+dtid2);
				if(dtid1==dtid2)
					{
					isinsert = false;
					return
					}
		       });
			if(isinsert)
				{
			      $("#DT1").insertResult(JSON.stringify(rowValue),DT1,1);
				}
	});
		
		$("#DT2").clearResult();
		trlen=$("#DT2 tr").length-1;
		var num=document.getElementsByTagName("font");
		num[0].innerHTML=trlen;
	});

	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        $("#num").val("1000");
        setDefaultOption($("#ND"),new Date().getFullYear());
        //其他处理放在下面
    });

	
	//自动完成项目名称模糊查询
	var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
	showAutoComplete("QXMMC",autocompleteXmmcController+"?xmmcAutoQuery","getXmmcQueryCondition");
});


//生成项目名称查询条件
function getXmmcQueryCondition(){
	var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":"S.pxh"}]}';
	var jsonData = convertJson.string2json1(initData);
	//年度
	if("" != $("#ND").val()){
		var defineCondition = {"value": $("#ND").val(),"fieldname":"S.ND","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	//项目名称
	if("" != $("#QXMMC").val()){
		var defineCondition = {"value": "%"+$("#QXMMC").val()+"%","fieldname":"S.XMMC","operation":"like","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	return JSON.stringify(jsonData);
}


//结转方法
doInsertJh=function(actionName, data1) {
    var success =true;
	$.ajax({
		type : 'post',
		url : actionName,
		data : data1,
		dataType : 'json',
		async :	false,
		success : function(result) {
			$("#resultXML").val(result.msg);
			$("#jhForm").clearFormResult();
			success=true;
		},
	    error : function(result) {
	     	//alert(result.msg);
		    defaultJson.clearTxtXML();
		    success=false;
		}
	});
	return success;
};



//行加下移按钮
function doDwon(obj){
	return "<button name=\"btnDownTable\" onclick=\"rowDown(this)\" class=\"btn btn-link\" type=\"button\"><i class=\"icon-chevron-down\"></i></button>";
}
//行加上移按钮
function doUp(obj){
	return "<button name=\"btnUpTable\" onclick=\"rowUp(this)\" class=\"btn btn-link\" type=\"button\"><i class=\"icon-chevron-up\"></i></button>";
}


//下移按钮事件
function rowDown(obj){
	var rowIndex = $(obj).parent().parent().index();
	$("#DT1").setSelect(rowIndex);
	var chongfu=chongfupanduan();
		if(chongfu==false)
			{
			  xInfoMsg('项目已添加！');
			  return 
			}
	//var rowIndex = $(obj).parent().parent().index();
	var rowValue = $(obj).parent().parent().attr("rowJson");
	$("#DT2").insertResult(rowValue,DT2,1);
	$("#DT1").removeResult(rowIndex);
	trlen = $("#DT2 tr").length-1;
	var num = document.getElementsByTagName("font");
	num[0].innerHTML = trlen;
}


//上移按钮事件
function rowUp(obj){
	var rowIndex = $(obj).parent().parent().index();
	$("#DT2").setSelect(rowIndex);
	var sycf=xiayichongfupanduan();
	var rowValue = $(obj).parent().parent().attr("rowJson");
	if(sycf){
	$("#DT1").insertResult(rowValue,DT1,1);
	}
	$("#DT2").removeResult(rowIndex);
	trlen = $("#DT2 tr").length-1;
	var num = document.getElementsByTagName("font");
	num[0].innerHTML = trlen;
}


//单击获取对象
function tr_click(obj){}
//弹出区域回调
getWinData = function(data){
	if(data.success){
		xSuccessMsg('操作成功！');
		$("#DT2").clearResult();
		var parentmain=parent.$("body").manhuaDialog.getParentObj();
		parentmain.query();
		var num = document.getElementsByTagName("font");
		num[0].innerHTML = 0;
	}else{
		xFailMsg('操作失败！');
	}
	
};
//上移判断
function chongfupanduan()
{
	 var success =true;
		$("#DT2 tbody tr").each(function(){
			var dt2id = convertJson.string2json1($(this).attr("rowJson")).GC_JH_SJ_ID;
			var json=$("#DT1").getSelectedRow();
			var odd = convertJson.string2json1(json);
			var id=$(odd).attr("GC_JH_SJ_ID");
			if(id==dt2id)
				{
				success=false;
				}
		});
		return success;
}
//下移判断
function xiayichongfupanduan()
{
	 var success =true;
		$("#DT1 tbody tr").each(function(){
			var dtid1 = convertJson.string2json1($(this).attr("rowJson")).GC_JH_SJ_ID;
			var json=$("#DT2").getSelectedRow();
			var odd = convertJson.string2json1(json);
			var id=$(odd).attr("GC_JH_SJ_ID");
			if(id==dtid1)
				{
				success=false;
				}
		});
		return success;
}
//父页面项目修改
function addxm(){
	 for(var i=0;i<rowValues.length;i++)
	 {
		// alert(rowValues[i]);
	   $("#DT1").insertResult(rowValues[i],DT1,1);
	 }
	 $("#jhsjids").val(ids);
	 $("#rowValues").val(rowValues);
}
//年份查询
function generateNd(ndObj){
	ndObj.attr("src","T#GC_TCJH_XMCBK:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(new Date().getFullYear());
}
//判断是否是项目
function doBdmc(obj){
	  var bd_name=obj.BDMC;
	  if(bd_name==null||bd_name==""){
		  return '<div style="text-align:center">—</div>';
	  }else{
		  return bd_name;			  
	  }
}
//判断是否是项目
function doBdbh(obj){
	  var bd_name=obj.BDBH;
	  if(bd_name==null||bd_name==""){
		  return '<div style="text-align:center">—</div>';
	  }else{
		  return bd_name;			  
	  }
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
  		<form method="post" id="queryForm" class="B-small-from-table-autoConcise">
   			<table class="B-table" width="100%">
		      	<!--可以再此处加入hidden域作为过滤条件 -->
		      	<TR style="display:none;">
			        <TD class="right-border bottom-border"></TD>
					<TD class="right-border bottom-border">
						<input type="hidden" id="ywid"/>
						<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
					<!-- 	<INPUT type="text" class="span12" kind="text" id="XMGLGS" fieldname="t.XMGLGS"  operation="="/> -->
					</TD>
		       	</TR>
     			<!--可以再此处加入hidden域作为过滤条件 -->
       			<tr>
					<th width="5%" class="right-border bottom-border text-right">年度</th>
					<td width="5%" class="right-border bottom-border">
						 <select id="ND" class="span12 year" name="ND" defaultMemo="全部" fieldname="t.ND" operation="=" ></select> 
					</td>
					<th width="5%" class="right-border bottom-border text-right">项目编号</th>
					<td width="15%" class="right-border bottom-border" >
						<input class="span12" type="text"  name="XMBH"　 fieldname="t.XMBH"　 id="XMBH" operation="like">
					</td>
					<th width="5%" class="right-border bottom-border text-right">项目名称</th>
					<td width="20%"   class="right-border bottom-border">
						<input class="span12" type="text" placeholder="" name="XMMC"
							check-type="maxlength" maxlength="100" fieldname="t2.XMMC"
							operation="like" id="QXMMC" autocomplete="off" tablePrefix="S">
					</td>
					<th width="5%" class="right-border bottom-border text-right">标段名称</th>
					<td width="20%" class="right-border bottom-border" >
						<input class="span12" type="text"  name="BDMC"　 fieldname="t.BDMC"　 id="BDMC" operation="like">
					</td>					
					<td width="" class="text-left bottom-border text-right">
						<button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
						<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
					</td>
      			</tr>
   			</table>
   		</form>
		<div class="B-small-from-table-autoConcise">
			<div style="overflow:auto;">
				<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single"  pageNum="5">
					<thead>
						<tr>
							<th  name="XH" id="_XH" tdalign="center">&nbsp;#&nbsp;</th>
							<th fieldname="XMBH" tdalign="center" CustomFunction="doDwon">&nbsp;操作&nbsp;</th>
							<th fieldname="XMBH" >&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>		
							<th fieldname="BDBH" maxlength="15" Customfunction="doBdbh">&nbsp;标段编号&nbsp;</th>	
							<th fieldname="BDMC" maxlength="15" Customfunction="doBdmc">&nbsp;标段名称&nbsp;</th>						
							<th fieldname="XMLX" tdalign="center">&nbsp;项目类型&nbsp;</th>
							<th fieldname="XMSX" tdalign="center">&nbsp;项目属性&nbsp;</th>
							<th fieldname="ISBT" tdalign="center">&nbsp;BT&nbsp;</th>
							<th fieldname="XMDZ" maxlength="15">&nbsp;项目地址&nbsp;</th>
						</tr>							
					</thead>
					<tbody id="tbody"></tbody>
				</table>
			</div>	
		</div>
		<br><br>
		<div align="center">
			<button id="all" class="btn" type="button"><i class="icon-plus"></i>&nbsp;全部下移</button>
			<button id="btnUp" class="btn" type="button"><i class="icon-chevron-up"></i>上移</button>
			<button id="btnDown" class="btn" type="button"><i class="icon-chevron-down"></i>下移</button>
			<button id="cannel" class="btn" type="button"><i class="icon-minus"></i>&nbsp;全部上移</button>	
		</div>
		<div class="B-small-from-table-autoConcise">
			<h4>
				已选择项目列表&nbsp;&nbsp; 共<font style=" margin-left:5px; margin-right:5px;;font-size:28px;color:red;">0</font>个
			 	<span class="pull-right">
			 		<button id="btnFH" class="btn" type="button">确定下发</button>
			 		<!-- <button id="btnXdt" class="btn" type="button">下达</button> -->
			 	</span>
					
			</h4>
			<div style="height:200px;overflow:auto;">
		  		<form method="post" id="queryForm2" class="B-small-from-table-autoConcise">
			      	<!--可以再此处加入hidden域作为过滤条件 -->
			      	<TR style="display:none;">
				        <TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<!-- <input type="hidden" id="DCZLX" name="DCZLX" fieldname="DCZLX" value="1" operation="="/> -->
							<INPUT type="hidden" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
						</TD>
		       		</TR>
			    </form>	
				<table class="table-hover table-activeTd B-table" id="DT2" width="100%" type="single" noPage="true" pageNum="1000">
					<thead>
						<tr>
							<th  name="XH" id="_XH" tdalign="center">&nbsp;#&nbsp;</th>
							<th fieldname="XMBH" tdalign="center" CustomFunction="doUp">&nbsp;操作&nbsp;</th>
							<th fieldname="XMBH" >&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>	
							<th fieldname="BDBH" maxlength="15" Customfunction="doBdbh">&nbsp;标段编号&nbsp;</th>	
							<th fieldname="BDMC" maxlength="15" Customfunction="doBdmc">&nbsp;标段名称&nbsp;</th>							
							<th fieldname="XMLX" tdalign="center">&nbsp;项目类型&nbsp;</th>
							<th fieldname="XMSX" tdalign="center">&nbsp;项目属性&nbsp;</th>
							<!-- <th fieldname="ISBT" tdalign="center">&nbsp;BT&nbsp;</th> -->
							<th fieldname="XMDZ" maxlength="15">&nbsp;项目地址&nbsp;</th>
 						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>		  
		</div>
	</div>
</div>				
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>   
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id="queryXML"/>
		<input type="hidden" name="txtXML" id="txtXML"/>
		<input type="hidden" name="txtFilter" order="asc" fieldname ="t.xmbh,t.xmbs,t.pxh" id="txtFilter"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>		
	</FORM>
</div>
</body>
</html>