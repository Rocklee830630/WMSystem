<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>项目下达</title>

<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/ZhaotoubiaoController.do";
var trlen; 


//初始化查询
$(document).ready(function(){
	setDefaultNd("ND");
	/* g_bAlertWhenNoResult=false; */
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryZhaotoubiaoxuqiu",data,DT1,"queryHuiDiao",true);
	/* var data=combineQuery.getQueryCombineData(queryForm2,frmPost,DT2);
	//调用ajax插入
	//defaultJson.doQueryJsonList(controllername+"?query_zc",data,DT2);
	g_bAlertWhenNoResult=true; */
	
});
	function queryHuiDiao()
	{
		trlen=$("#DT2 tr").length-1;
		var num=document.getElementsByTagName("font");
		num[0].innerHTML=trlen;
	}
	//调用父页面方法获取已选项目
	function  chaxunxiangm(){
		var fuyemian=$(window).manhuaDialog.getParentObj();
		var obj = fuyemian.getArr();
		if(obj==undefined){
		}else{
			for(var i=0;i<obj.length;i++){
				$("#DT2").insertResult(obj[i],DT2,1);
				trlen=$("#DT2 tr").length-1;
				var num=document.getElementsByTagName("font");
				num[0].innerHTML=trlen;
			}
		}
	}
	//页面初始化
	$(function() {
		chaxunxiangm();
		//按钮绑定事件(查询)
		$("#btnQuery").click(function() {
			//生成json串
			var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryZhaotoubiaoxuqiu",data,DT1,null,false);
		});
	//按钮绑定事件（确定）
	$("#btnFH").click(function() {
		//获取id
		var ids = "";
		var rowValues=new Array();
		$("#DT2 tbody tr").each(function(i){
			var rowValue = $(this).attr("rowJson");
			rowValues[i]=rowValue;
			var value = convertJson.string2json1($(this).attr("rowJson")).GC_ZTB_XQ_ID;
			ids+=value+",";
		});	
		var fuyemian=$(window).manhuaDialog.getParentObj();
	    fuyemian.fanhuixiangm(rowValues,ids);
	    $(window).manhuaDialog.close();
	});	

	
	
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
 		if(chongfu!="true"){
 			xInfoMsg(chongfu);
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
		if($("#DT1 tr").length==1){
			xInfoMsg('没有可移动的需求！');
			return;
		}
		var isinsert = true;
		//判断招标类型是否相同
		if($("#DT2 tr").length==1)
		{
			var zblxarrays= new Array();
			var rows = $("#DT1 tbody tr");
			//只校验DT1表
			for(var i=0 ;i <rows.size();i++)
				{
					var rowValue = rows.eq(i).attr("rowJson");

					zblxarrays[i] = JSON.parse(rowValue).ZBLX;
				}
			for(var j=0 ;j <zblxarrays.length;j++)
				{
					if(zblxarrays != null){
						if(zblxarrays[0]!=zblxarrays[j]){
							isinsert = false;
							break;
						}
					}
				}
			if(!isinsert){
				xInfoMsg('存在招标类型不相同的记录,不能全部下移！');
				return;
			}
		}else{
			
			//判断两表类型是否一致
			var dt1rows = $("#DT1 tbody tr");
			var dt2rows = $("#DT2 tbody tr");
			//从DT1中取出每条记录与DT2中记录做比较
			for(var i=0 ;i <dt1rows.size();i++)
				{
					var zblx = "";
					var rowValue = dt1rows.eq(i).attr("rowJson");
					 zblx = JSON.parse(rowValue).ZBLX;
						for(var i=0 ;i <dt2rows.size();i++)
						{
							var zblx1 = "";
							 zblx1 = JSON.parse(dt2rows.eq(i).attr("rowJson")).ZBLX;
							if(zblx != "" && zblx1 != zblx){
								isinsert = false;
								break;
							}
						}
						if(!isinsert){
							break;
						}
				}
			if(!isinsert){
				xInfoMsg('存在招标类型不相同的记录,不能全部下移！');
				return;
			}
		}

		//循环插入DT2
		$("#DT1 tbody tr").each(function(){

			var rowValue=convertJson.string2json1($(this).attr("rowJson"));

	
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
			xInfoMsg('没有可移动的记录！');
			return;
		}	

		$("#DT2 tbody tr").each(function(){

			var rowValue=convertJson.string2json1($(this).attr("rowJson"));

	
			      $("#DT1").insertResult(JSON.stringify(rowValue),DT1,1);
				
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
        //其他处理放在下面
    });

});



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
	/**
	//这个限制又不要了	2014-10-30
	//add by zhangbr@ccthanking 2013-11-20 BEGIN	这个限制是临时加的
	var rows = $("#DT2").getTableRows();
	if(rows>0){
		xInfoMsg("一次只能选择一个招标需求");
		return 
	}
	//add by zhangbr@ccthanking 2013-11-20 END	
	*/
	var chongfu=chongfupanduan();
	if(chongfu!="true"){
		xInfoMsg(chongfu);
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
	 var success ="true";
		$("#DT2 tbody tr").each(function(){
			var dt2id = convertJson.string2json1($(this).attr("rowJson")).GC_ZTB_XQ_ID;
			var json=$("#DT1").getSelectedRow();
			var odd = convertJson.string2json1(json);
			var id=$(odd).attr("GC_ZTB_XQ_ID");
			var dt2lx = convertJson.string2json1($(this).attr("rowJson")).ZBLX;
			var lx=$(odd).attr("ZBLX");
			if(id==dt2id){
				success="该需求列表中已存在";
			}
			if(dt2lx!=lx){
				success="招标类型不一致";
			}
		});
		return success;
}
//下移判断
function xiayichongfupanduan(){
	var success =true;
	$("#DT1 tbody tr").each(function(){
		var dtid1 = convertJson.string2json1($(this).attr("rowJson")).GC_ZTB_XQ_ID;
		var json=$("#DT2").getSelectedRow();
		var odd = convertJson.string2json1(json);
		var id=$(odd).attr("GC_ZTB_XQ_ID");
		if(id==dtid1){
			success=false;
		}
	});
	return success;
}
//父页面项目修改
function addxm()
{
	 for(var i=0;i<rowValues.length;i++)
	 {
		// alert(rowValues[i]);
	   $("#DT1").insertResult(rowValues[i],DT1,1);
	 }
	 $("#jhsjids").val(ids);
	 $("#rowValues").val(rowValues);
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
					</TD>
		       	</TR>
     			<!--可以再此处加入hidden域作为过滤条件 -->
       			<tr>
					<th width="5%" class="right-border bottom-border text-right">年度</th>
					<td width="10%" class="right-border bottom-border">
						<select class="span12 year" kind="dic" src="T#GC_ZTB_XQ: distinct to_char(LRSJ,'YYYY') as NDCODE:to_char(LRSJ,'YYYY') as ND:SFYX='1' ORDER BY NDCODE asc" 
							id="ND" name="ND" defaultMemo="全部" fieldname="to_char(lrsj,'yyyy')" operation="=">
						</select>
					</td>
					<th width="5%" class="right-border bottom-border text-right">招标类型</th>
					<td width="10%" class=" right-border bottom-border">
	          		<select class="span12 4characters" id="ZBLX" name = "ZBLX" fieldname="ZBLX" kind="dic" src="ZBLX" operation="=">
			 		</select>
			 		<th width="5%" class="right-border bottom-border text-right">工作名称</th>
					<td width="20%" class="right-border bottom-border">
						<input class="span12" type="text" id="Q_GZMC" name="Q_GZMC" fieldname="GZMC" operation="like" maxlength="100">
					</td>
		      </td> 				
					<td width="55%" class="text-left bottom-border text-right">
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
					<th  name="XH" id="_XH" >&nbsp;#&nbsp;</th>
					<th fieldname="GZMC" tdalign="center" CustomFunction="doDwon">&nbsp;操作&nbsp;</th>
				 	<th fieldname="GZMC"  maxlength="15" >&nbsp;工作名称&nbsp;</th>
				 	<th fieldname="GZNR"  maxlength="15" >&nbsp;工作内容&nbsp;</th>
				 	<th fieldname=ZBLX  maxlength="15" >&nbsp;招标类型&nbsp;</th>
				 	<th fieldname="JSYQ"  maxlength="15" >&nbsp;技术要求&nbsp;</th>
				 	<th fieldname="TBJFS"  maxlength="15" >&nbsp;投标报价方式&nbsp;</th>
				 	<th fieldname="YSE"  maxlength="15" >&nbsp;投资额&nbsp;</th>

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
				已选择需求列表&nbsp;&nbsp; 共<font style=" margin-left:5px; margin-right:5px;;font-size:28px;color:red;">0</font>个
			 	<span class="pull-right">
			 		<button id="btnFH" class="btn" type="button">确定</button>
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
						<th  name="XH" id="_XH" >&nbsp;#&nbsp;</th>
						<th fieldname="GZMC" tdalign="center" CustomFunction="doUp">&nbsp;操作&nbsp;</th>
				 	<th fieldname="GZMC"  maxlength="15" >&nbsp;工作名称&nbsp;</th>
				 	<th fieldname="GZNR"  maxlength="15" >&nbsp;工作内容&nbsp;</th>
				 	<th fieldname=ZBLX  maxlength="15" >&nbsp;招标类型&nbsp;</th>
				 	<th fieldname="JSYQ"  maxlength="15" >&nbsp;技术要求&nbsp;</th>
				 	<th fieldname="TBJFS"  maxlength="15" >&nbsp;投标报价方式&nbsp;</th>
				 	<th fieldname="YSE"  maxlength="15" >&nbsp;投资额&nbsp;</th>
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
		<input type="hidden" name="txtFilter" order="desc" fieldname ="LRSJ" id="txtFilter"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>		
	</FORM>
</div>
</body>
</html>