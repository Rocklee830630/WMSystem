<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<% 
	String lujing = request.getParameter("lujing");
	String mingchen = request.getParameter("mingchen");
	String tiaojian = request.getParameter("tiaojian");
	String nd = request.getParameter("nd");
	String biaozhi = request.getParameter("biaozhi");
%>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script type="text/javascript" charset="utf-8">
var controllername= "${pageContext.request.contextPath }/qqxsbzxxController.do";
var lujing = "<%=lujing %>";
var mingchen = "<%=mingchen%>";
var nd = "<%=nd%>";
var tiaojian = "<%=tiaojian%>";
  var tbl = null;
  var json;
  var diaoyong;
  
//计算本页表格分页数
  function setPageHeight(){
  	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(3)-pageNumHeight;
  	var pageNum = parseInt(height/pageTableOne,10);
  	$("#DT1").attr("pageNum",pageNum);
  }
	$(function() {
		setPageHeight();
	 	init();
		//按钮绑定事件（导出）
		$("#excel").click(function(){
			if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
			      //printTabList("DT1");
			      printTabList("DT1","qqsx.xls","XMBH,XMMC,BDBH,BDMC,LXKYBJSJ,TDSPBJSJ,GHSPBJSJ,SGXKBJSJ,JBDW,JER,JJSJ,JJCLMX","4,1");
			  }
		});
	});
	function init()
	{
	     //默认调用
		 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername + "?queryQqsx&lujing="+lujing+"&mingchen="+mingchen+"&nd="+nd+"&tiaojian="+tiaojian,data,DT1,'bindEvent',true); 
	}


	//详细信息
	 function rowView(index){
	     var obj = $("#DT1").getSelectedRowJsonByIndex(index);//获取行对象
	     var xmbh = eval("("+obj+")").XDKID;//取行对象<项目编号>
	     $(window).manhuaDialog(xmscUrl(xmbh));//调用公共方法,根据项目编号查询
	 }
	function bindEvent(a){
		$(".showXmxxkInfo").unbind();
		$(".showXmxxkInfo").click(function() {
			$("#DT1").cancleSelected();
	    	var index = $(event.target).closest("tr").index();
	    	$("#DT1").setSelect(index);
			$(window).manhuaDialog({"title":"前期手续项目信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/qqsx/xmxxk.jsp","modal":"1"});
		});
	}
	//反馈颜色判断	
	 function LXFKZT(obj)
		{
		 if(""!=obj.LXKYBJSJ)
		 {
		   if(obj.LXFKZT == "1")
			{
			 return '&nbsp;<i title="已反馈"  class="icon-green"></i>&nbsp;';
			}
		    else
			 {
			 return '&nbsp;<i title="未反馈" class="icon-red"></i>&nbsp;';
			 }
		  }
		 else{
			 return '&nbsp;';
		 }
		}
	//反馈颜色判断	
	 function TDFKZT(obj)
		{
		if(""!=obj.TDSPBJSJ)
			{
		 if(obj.TDFKZT == "1")
			{
			 return '&nbsp;<i title="已反馈"  class="icon-green"></i>&nbsp;';
			}
		 else
			 {
			 return '&nbsp;<i  title="未反馈" class="icon-red"></i>&nbsp;';
			 }
			}
		 else{
			 return '&nbsp;';
		 }
		}
	//反馈颜色判断	
	 function GHFKZT(obj)
		{
		 if(""!=obj.GHSPBJSJ)
			 {
			  if(obj.GHFKZT == "1")
				{
				 return '&nbsp;<i title="已反馈" class="icon-green"></i>&nbsp;';
				}
			  else
				 {
				 return '&nbsp;<i title="未反馈" class="icon-red"></i>&nbsp;';
				 }
			 }
		 else{
			 return '&nbsp;';
		 }
		}
	//反馈颜色判断	
	 function SGFKZT(obj)
		{
		 if(""!=obj.SGXKBJSJ)
		  {
			 if(obj.SGFKZT == "1")
				{
				 return '&nbsp;<i  title="已反馈" class="icon-green"></i>&nbsp;';
				}
			 else
				 {
				 return '&nbsp;<i title="未反馈" class="icon-red"></i>&nbsp;';
				 }
		 }
		 else{
			 return '&nbsp;';
		 }
		}
	function JHWCTX(obj)
	{
		if(obj.COLOR>0)
			{
			 return '&nbsp;<i title="超期" class="icon-red"></i>&nbsp;';
			}
		else
		{
			 return '&nbsp;<i title="正常" class="icon-green"></i>&nbsp;';
		}
	}
	
	function doRandering(obj){
		var showHtml = "";
		showHtml = "<a href='javascript:void(0)' title='前期手续信息卡'><i class='icon-file showXmxxkInfo' jhsjid='"+obj.GC_JH_SJ_ID+"'></i></a>";
		return showHtml;
	}	
	//标段判断
  function doBdmc(obj){
	  var bd_name=obj.BDMC;
	  if(bd_name==null||bd_name==""){
		  return '<div style="text-align:center">—</div>';
	  }else{
		  return bd_name;			  
	  }
  }
	//获取父页面值
	function getValue(){
		var rowValue=$("#DT1").getSelectedRow();
		return rowValue;
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
 <p></p>
  <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">前期手续综合信息
            <span class="pull-right">
              <button id="excel" class="btn" type="button">导出</button>
	      </span>
      </h4>
      <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD><INPUT type="text" class="span12" keep="true" kind="text"  fieldname="rownum"  value="1000" operation="<=" ></TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
      </table>
      </form>
    <div style="height:5px;"> </div>
    <div class="overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single"  pagingFunction="bindEvent">
                <thead>
                    <tr>
	                    <th  name="XH" id="_XH"  colindex=1 >&nbsp;#&nbsp;</th>
	                    <th fieldname="XMBH"  noprint="true" colindex=2 maxlength="15"  tdalign="center" CustomFunction="doRandering">&nbsp;&nbsp;</th>
	                    <th fieldname="XMBH"  colindex=3  hasLink="true" linkFunction="rowView" rowMerge="true">&nbsp;项目编号&nbsp;</th>
						<th fieldname="XMMC"   rowMerge="true"  colindex=4 maxlength="15">&nbsp;项目名称&nbsp;</th>
						<th fieldname="BDBH"    maxlength="15" colindex=5 Customfunction="doBdBH" >&nbsp;标段编号&nbsp;</th>
						<th fieldname="BDMC"   colindex=6 maxlength="15" Customfunction="doBdmc" >&nbsp;标段名称&nbsp;</th>
						<th fieldname="JBDW"  colindex=7 maxlength="15" >&nbsp;交办单位&nbsp;</th>
						<th fieldname="JER"   colindex=8 tdalign="center" maxlength="15" >&nbsp;交接人&nbsp;</th>
						<th fieldname="JJSJ"   colindex=9 tdalign="center">&nbsp;交接时间&nbsp;</th>
						<th fieldname="JJCLMX"   colindex=10 maxlength="15" >&nbsp;交接材料说明&nbsp;</th>
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
       <input type="hidden" name="txtFilter" order="asc" fieldname="B.xmbh,B.xmbs,B.pxh"/>
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
          <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 </div>
</body>
</html>