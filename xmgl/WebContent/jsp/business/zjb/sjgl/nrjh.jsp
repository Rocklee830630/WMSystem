<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>项目结转</title>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script type="text/javascript" charset="utf-8">


//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/shenJiGuanliController.do";
var id ="",flag,success; 


//初始化查询
$(document).ready(function(){
	getNd();
	//生成json串
	query();
});

function query_ready_js()
{
	trlen=$("#DT2 tr").length-1;
	var num=document.getElementsByTagName("font");
	num[0].innerHTML=trlen;
}


//页面初始化
$(function() {
	//按钮绑定事件(查询)
	$("#btnQuery").click(function() {	
		query();
	});
	
	//按钮绑定事件(结转)
	$("#example_nrtj").click(function() {
		if($("#DT2").getTableRows()==0){
			xInfoMsg('没有可纳入的项目！');
			return;
		}
		$(window).manhuaDialog({"title":"纳入","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zjb/sjgl/nrnf.jsp","modal":"3"});
	});

	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        $("#num").val("1000");
        //其他处理放在下面
    });
 	//回调函数 ,关闭弹出框后执行查询
	getWinData = function(data){
		if(data>0)
		{		
			var ids="";
			$("#DT2 tbody tr").each(function(){
				//存行数据
				var value=convertJson.string2json1($(this).attr("rowJson")).GC_ZJB_JSB_ID;
				ids+=value+",";
			});		
			success=defaultJson.doInsertJson(controllername + "?insert_jz&ids="+ids+"&year="+data, null,null,"insert");
		}
		else
		{
			return;
		}	
 	};

 	
	//按钮绑定事件（暂存）
	$("#btnZc").click(function() {
		//获取id
		var ids = "";
		$("#DT2 tbody tr").each(function(){
			var value = convertJson.string2json1($(this).attr("rowJson")).GC_TCJH_XMCBK_ID;
			ids+=value+",";
		});	
		var success = doInsertZc(controllername + "?insert_zc&ids="+ids+"&dczlx=2", null);
		if(success){
			xSuccessMsg('操作成功！');
		}else{
			xFailMsg('操作失败！');
		}
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
		$("#DT1 tbody tr").each(function(){
			var rowValue=convertJson.string2json1($(this).attr("rowJson"));//$("#DT1").getSelectedRowIndex();//获得选中行的索引
			$("#DT2").insertResult(JSON.stringify(rowValue),DT2,1);
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
		$("#DT2 tbody tr").each(function(){
			var rowValue=convertJson.string2json1($(this).attr("rowJson"));//$("#DT1").getSelectedRowIndex();//获得选中行的索引
			$("#DT1").insertResult(JSON.stringify(rowValue),DT1,1);
		});
		$("#DT2").clearResult();
		trlen=$("#DT2 tr").length-1;
		var num=document.getElementsByTagName("font");
		num[0].innerHTML=trlen;
	});	

	
	//自动完成项目名称模糊查询
	var controllername_jz= "${pageContext.request.contextPath }/xmcbk/xmcbkwhController.do";
	showAutoComplete("XMMC",controllername_jz+"?xmmcAutoQuery","getXmmcQueryCondition"); 
});
function query(){
	var ids = "\'";
	$("#DT2 tbody tr").each(function(){
		var value = convertJson.string2json1($(this).attr("rowJson")).GC_ZJB_JSB_ID;
		ids+=value+"\',\'";
	});	
	//生成json串
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryJS&ids="+ids,data,DT1,null,false);
}

//生成项目名称查询条件
function getXmmcQueryCondition(){
	var initData = '{"querycondition":{"conditions":[ {"value": "0","fieldname":"ISXD","operation":"=","logic":"and"},]},"orders":[{"order":"desc","fieldname":"pxh"}]}';
	var jsonData = convertJson.string2json1(initData); 
	//项目名称
	if("" != $("#XMMC").val()){
		var defineCondition = {"value": "%"+$("#XMMC").val()+"%","fieldname":"XMMC","operation":"like","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	//年度
	if("" != $("#ND").val()){
		var defineCondition = {"value": $("#ND").val(),"fieldname":"ND","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	return JSON.stringify(jsonData);
}


//插入回调函数
function insert()
{
	//生成json串
	if(success==true)
	{
		query();
		var parentmain=$(window).manhuaDialog.getParentObj();
		parentmain.query();
		var num=document.getElementsByTagName("font");
		num[0].innerHTML=0;
		$("#DT2").clearResult();
	}	
 }


//行加下移按钮
function doDwon(obj){
	return "<button name=\"btnDownTable\" onclick=\"rowDown(this)\" class=\"btn btn-link\" type=\"button\"><i title=\"下移\" class=\"icon-chevron-down\"></i></button>";
}
//行加上移按钮
function doUp(obj){
	return "<button name=\"btnUpTable\" onclick=\"rowUp(this)\" class=\"btn btn-link\" type=\"button\"><i title=\"上移\" class=\"icon-chevron-up\"></i></button>";
}


//下移按钮事件
function rowDown(obj){
	var rowIndex = $(obj).parent().parent().index();
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
	var rowValue = $(obj).parent().parent().attr("rowJson");
	$("#DT1").insertResult(rowValue,DT1,1);
	$("#DT2").removeResult(rowIndex);
	trlen = $("#DT2 tr").length-1;
	var num = document.getElementsByTagName("font");
	num[0].innerHTML = trlen;
}
//默认年度
function getNd(){
		setDefaultNd("LRSJ");
}
function doBdmc(obj){
	  var bd_name=obj.BDMC;
	  if(bd_name==null||bd_name==""){
		  return '<div style="text-align:center">—</div>';
	  }else{
		  return bd_name;			  
	  }
}
//标段编号
  function doBdBH(obj){
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
						<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
					</TD>
				</TR>
				<!--可以再此处加入hidden域作为过滤条件 -->
				<tr>
					<th width="5%" class="right-border bottom-border text-right">结算年度</th>
				     <td width="8%" class="right-border bottom-border">
		                 <select class="span12 year" id="LRSJ" name = "LRSJ" fieldname="to_char(D.LRSJ,'yyyy')"  defaultMemo="全部" operation="="  kind="dic" src="T#GC_ZJB_JSB:to_char(lrsj,'yyyy') :to_char(lrsj,'yyyy'):SFYX='1' group by to_char(lrsj,'yyyy') ">
		              	</select>
			          </td>
					<th width="5%" class="right-border bottom-border text-right">项目名称</th>
					<td width="25%" class="right-border bottom-border">
						<input class="span12" type="text" placeholder="" name="XMMC" fieldname="XMMC" check-type="maxlength" maxlength="500" operation="like" id="XMMC" autocomplete="off">
					</td>					
					<td width="55%" class="text-left bottom-border text-right">
						<button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
						<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
					</td>
				</tr>
			</table>
		</form>
		<div class="B-small-from-table-autoConcise">
			<div style="height:200px;overflow:auto;">
				<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" noPage="true" pageNum="1000">
				<thead>
							<tr>
								<th name="XH" id="_XH"   colindex=1>
									&nbsp;#&nbsp;
								</th>
								<th fieldname="XMBH"    tdalign="center" colindex=2 CustomFunction="doDwon">&nbsp;操作&nbsp;</th>
								<th fieldname="XMBH" rowspan="2" rowMerge="true" maxlength="10" colindex=3
									hasLink="true" linkFunction="rowView">
									&nbsp;项目编号&nbsp;
								</th>
								<th fieldname="XMMC"   rowMerge="true" colindex=4
									maxlength="15">
									&nbsp;项目名称&nbsp;
								</th>
								<th fieldname="BDBH"   colindex=5 maxlength="10"
									Customfunction="doBdBH">
									&nbsp;标段编号&nbsp;
								</th>
								<th fieldname="BDMC"  colindex=6 maxlength="10"
									Customfunction="doBdmc">
									&nbsp;标段名称&nbsp;
								</th>
								<th fieldname="ND"   colindex=7 tdalign="center">
									&nbsp;施工年度&nbsp;
								</th>
								<th fieldname="SGDW"  maxlength="10" colindex=8>
									&nbsp;施工单位&nbsp;
								</th>
								<th fieldname="JSZT"  colindex=9>
									&nbsp;结算状态&nbsp;
								</th>
							<!-- 	<th colspan="4">
									&nbsp;合同&nbsp;
								</th> -->
								<th fieldname="JSQK"   maxlength="15" colindex=14>
									&nbsp;结算情况&nbsp;
								</th>
							</tr>
						<!-- 	<tr>
								<th fieldname="HTBH" maxlength="15" colindex=10>
									&nbsp;编号&nbsp;
								</th>
								<th fieldname="HTMC" maxlength="15" colindex=11>
									&nbsp;名称&nbsp;
								</th>
								<th fieldname="HTQDFS" colindex=12>
									&nbsp;签订方式&nbsp;
								</th>
								<th fieldname="HTJE" maxlength="15" colindex=13 tdalign="right">
									金额（元）
								</th>
							</tr> -->
							</thead><tbody></tbody>
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
		 	 <h4 class="title">待纳入统计的项目&nbsp;&nbsp; 共<font style=" margin-left:5px; margin-right:5px;;font-size:28px;color:red;">0</font>个
			  	<span class="pull-right">
		 			<button id="example_nrtj" class="btn" type="button">纳入项目</button>
		 		</span>
		  	</h4>
	  		<form method="post" id="queryForm2" class="B-small-from-table-autoConcise">
		      	<!--可以再此处加入hidden域作为过滤条件 -->
		      	<TR style="display:none;">
			        <TD class="right-border bottom-border"></TD>
					<TD class="right-border bottom-border">
						<INPUT type="hidden" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
					</TD>
	       		</TR>
		    </form>	
		  	<div style="height:200px;overflow:auto;">
				<table class="table-hover table-activeTd B-table" id="DT2" width="100%" type="single" pageNum="1000">
					<thead>
									<tr>
								<th name="XH" id="_XH"   colindex=1>
									&nbsp;#&nbsp;
								</th>
								<th fieldname="XMBH"    tdalign="center" colindex=2 CustomFunction="doDwon">&nbsp;操作&nbsp;</th>
								<th fieldname="XMBH" rowspan="2" rowMerge="true" maxlength="10" colindex=3
									hasLink="true" linkFunction="rowView">
									&nbsp;项目编号&nbsp;
								</th>
								<th fieldname="XMMC"   rowMerge="true" colindex=4
									maxlength="15">
									&nbsp;项目名称&nbsp;
								</th>
								<th fieldname="BDBH"   colindex=5 maxlength="10"
									Customfunction="doBdBH">
									&nbsp;标段编号&nbsp;
								</th>
								<th fieldname="BDMC"  colindex=6 maxlength="10"
									Customfunction="doBdmc">
									&nbsp;标段名称&nbsp;
								</th>
								<th fieldname="ND"   colindex=7 tdalign="center">
									&nbsp;施工年度&nbsp;
								</th>
								<th fieldname="SGDW"  maxlength="10" colindex=8>
									&nbsp;施工单位&nbsp;
								</th>
								<th fieldname="JSZT"  colindex=9>
									&nbsp;结算状态&nbsp;
								</th>
							<!-- 	<th colspan="4">
									&nbsp;合同&nbsp;
								</th> -->
								<th fieldname="JSQK"   maxlength="15" colindex=14>
									&nbsp;结算情况&nbsp;
								</th>
							</tr>
						<!-- 	<tr>
								<th fieldname="HTBH" maxlength="15" colindex=10>
									&nbsp;编号&nbsp;
								</th>
								<th fieldname="HTMC" maxlength="15" colindex=11>
									&nbsp;名称&nbsp;
								</th>
								<th fieldname="HTQDFS" colindex=12>
									&nbsp;签订方式&nbsp;
								</th>
								<th fieldname="HTJE" maxlength="15" colindex=13 tdalign="right">
									金额（元）
								</th>
							</tr> -->
							</thead>
					<tbody></tbody>
				</table>
			</div>
		</div>
 	</div>
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id="queryXML"/>
		<input type="hidden" name="txtXML" id="txtXML"/>
		<input type="hidden" name="txtFilter" order="asc" fieldname="g.xmbh,g.xmbs,g.pxh" />
		<input type="hidden" name="resultXML" id="resultXML"/>
		<input type="hidden" id="queryResult"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>	
	</FORM>
 </div>
</body>
</html>