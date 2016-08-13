<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<style type="text/css">
label.checkbox.inline {
  margin-left: 10px;
  width:150px;
}
</style>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/ReportController.do";
  
  function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#cjdwList").attr("pageNum",pageNum);
	}
  
  
	$(function() {
		setPageHeight();
		ready();
		//查询
		var btn = $("#query");
		btn.click(function() {
			showTr();
			var start=$("#START").val();
	  		var end=$("#END").val();
	  		var dic="";
	  		$("[name=BM]:checkbox").each(function(){
				if($(this).is(':checked')){
		  			if($(this).attr('value')!=undefined){
		  				dic==""?(dic="'"+$(this).attr('value')+"'"):(dic+=",'"+$(this).attr('value')+"'");
		  			}
				}
			});
			//生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?monitor&start="+start+"&end="+end+"&dic="+dic,data,DT1,null,false);
			doDic();
				});
		//清空
		var clean=$("#clean");
		clean.click(function(){
			var today=getCurrentDate();
			$("#START").val("");
			$("#END").val("");
			$("#START").attr("max",today);
	   		$("#END").attr("max",today);
			$("#START").attr("min","");
			$("#END").attr("min",$("#START").val(""));
			$("#queryForm").clearFormResult();
			$("[name=BM]:checkbox").removeAttr('checked'); 
		});
		//按钮绑定事件（导出）
		$("#excel").click(function(){
			if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
				printTabList("DT1","报表展示.xls","BMMC,JRZL,JRGR,LJZL,LJGR","3,1");
			  }
		});
		
		$("#bbxz").click(function(){
			$("#bmForm").slideToggle("fast");
		});
	});
	
    function ready(ready) {
   		showTr();
   		var today=getCurrentDate();
   		$("#START").attr("max",today);
   		$("#END").attr("max",today);
   		var start=$("#START").val();
  		var end=$("#END").val();
   		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?monitor&start="+start+"&end="+end,data,DT1,null,false);
		doDic();
   };
   //列名显示
   function showTr(){
	   var today=getCurrentDate();
  		var start=$("#START").val();
  		var end=$("#END").val();
  		if(start==null||start==""){
			end==null||end==""?($("#jr").html("今日（"+today+"）")):($("#jr").html("今日（"+end+"）"));
			end==null||end==""?($("#lj").html("累计(截止"+today+")")):($("#lj").html("累计(截止"+end+")"));
		}else{
			end==null||end==""?($("#jr").html("今日（"+today+"）")):($("#jr").html("今日（"+end+"）"));
			end==null||end==""?($("#lj").html("累计("+start+"—"+today+")")):($("#lj").html("累计("+start+"—"+end+")"));
		}
   } 
    //字典操作
   function doDic(){
	   var count=$("#DT1 tr").length-2;
	   var value,temp;
	   $("[name=BM]:checkbox").each(function(){
	   for(var k=0;k<count;k++){
			$("#DT1").setSelect(k);
			value=$("#DT1").getSelectedRow();
			temp=convertJson.string2json1(value);
			if($(this).val()==temp.BMMC){
				$(this).prop('checked',true);
				break;
				}
	   		}
		});
	   $("#DT1").cancleSelected();
   }
   //开始时间
   function start(){
	   var start=$("#START").val();
	   $("#END").attr("min",start);
   }
   //结束时间
   function end(){
	   var end=$("#END").val();
	   var today=getCurrentDate();
	   if(end<today)
	   $("#START").attr("max",end);
   }
   //显示横线
   function show(obj){
	   var a=obj.JRGR;
	   var b=a=="—"?('<div style="text-align:center">—</div>'):"";
	   return b;
   }
</script>
	</head>
	<body>
<app:dialogs />
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">
			部门监控信息
				<span class="pull-right">
			  			<button id="excel" class="btn" type="button">导出</button>
		  		</span>
			</h4>
			<form method="post" id="queryForm" width="100%">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
				<TR style="display: none;">
					<TD class="right-border bottom-border">
						<INPUT type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<=" keep="true">
					</TD>
				</TR>
				<!--可以再此处加入hidden域作为过滤条件 -->
				<tr>
					<th  class="right-border bottom-border">起止时间</th>
					<td  class="bottom-border"><input class="span12 date"  type="date" id="START" name ="STRAT" operation="=" oninput="start();"></td>
					<td>至</td>
					<td  class="bottom-border"><input class="span12 date"  type="date" id="END" name ="END" operation="=" oninput="end();"></td>
					<td  class="text-left bottom-border text-right" style="width:90%">
						<button id="query" class="btn btn-link" type="button"> <i class="icon-search"></i>查询
						</button>
						<button id="clean" class="btn btn-link" type="button"> <i class="icon-trash"></i>清空
						</button>
						<button id="bbxz" class="btn btn-link" type="button">部门选择
						</button>
					</td>
				</tr>
			</table>
		</form>
		<form method="post" id="bmForm" style="width:100%; height:100px;display:none">
				<table class="B-table" width="100%">
				<tr>
					<td class="bottom-border">
						<input class="span12" type="checkbox" id="BM" name ="BM" kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:decode(BMJC,null,DEPT_NAME,BMJC):DEPT_PARANT_ROWID !='0' and ROW_ID !='100000000008'" >
				</tr>
			</table>
		</form>
		<div style="height: 5px;"></div>
			<div class="overFlowX">
				<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" noPage="true" pageNum="2000">
					<thead>
						<tr>
							<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
							<th fieldname="BMMC" maxlength="15" rowspan="2" colindex=2 >&nbsp;部门名称&nbsp;</th>
							<th colspan="2" id="jr">今日录入</th>
							<th colspan="2" id="lj">累计录入</th>
						</tr>
						<tr>	
							<th fieldname="JRZL" tdalign="right" colindex=3>&nbsp;录入总量&nbsp;</th>
							<th fieldname="JRGR" tdalign="right" colindex=4 CustomFunction="show">&nbsp;个人明细&nbsp;</th>
							<th fieldname="LJZL" tdalign="right" colindex=5>&nbsp;录入总量&nbsp;</th>
							<th fieldname="LJGR" tdalign="right" colindex=6>&nbsp;个人明细&nbsp;</th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>
		</div>
	</div>
</div>
		<div align="center">
			<FORM name="frmPost" id="frmPost" method="post" style="display: none" target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="txtFilter" order="" fieldname="" id="txtFilter">
				<input type="hidden" name="queryResult" id ="queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>