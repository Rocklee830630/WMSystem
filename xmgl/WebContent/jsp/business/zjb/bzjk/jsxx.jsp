<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<% 
	String lujing = request.getParameter("lujing");
	String mingchen = request.getParameter("mingchen");
	String tiaojian = request.getParameter("tiaojian");
	String nd = request.getParameter("nd");
	String biaozhi = request.getParameter("biaozhi");
%>
<script type="text/javascript" charset="utf-8">
   var json,indexid,biaozhi;
   var GC_ZJB_JSB_ID = '';
   var controllername= "${pageContext.request.contextPath }/zjbmjkController.do";
   var controllername1= "${pageContext.request.contextPath }/jieSuanGuanliController.do";
   var lujing = "<%=lujing %>";
   var mingchen = "<%=mingchen%>";
   var nd = "<%=nd%>";
   var tiaojian = "<%=tiaojian%>";

	//计算本页表格分页数
	function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(3)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}

	$(function() {
		ready();
		setPageHeight();
	    //导出
	    $("#btnExpExcel").click(function() 
	       {
	    	if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
	    	     // printTabList("DT1");
	    		printTabList("DT1","jsgl.xls","XMBH,XMMC,BDMC,ND,SGDW,JSZT,HTBH,HTMC,HTQDFS,HTJE,TBR,TBRQ,TBJE,WTZXGS,YZSDRQ,YZSDJE,CSSDRQ,CSSDJE,CSBGBH,SJSDRQ,SJSDJE,SJBGBH,JSQK","3,0"); 
	    	  }
	   	});
	    //合同信息
	    $("#htxx").click(function() 
	 	       {
	    	 $(window).manhuaDialog({"title":"结算管理>合同信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zjb/jsgl/htxx.jsp","modal":"2"});
	 	   	});
		queryHeTong();
	});
    function ready() {
   	 	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryzjxx&lujing="+lujing+"&mingchen="+mingchen+"&nd="+nd+"&tiaojian="+tiaojian,data,DT1,null,true);
   };
	//查询已完成未结算的合同总数
	function queryHeTong()
	{
		if( document.getElementById("htxx"))
		{
				$.ajax(
				{
					     url : controllername1+"?queryhtxxzs",//此处定义后台controller类和方法
				         dataType : 'json',//此处定义返回值的类型为string，详见样例代码
				         success : function(result) {
				         var resultmsg = result.msg; //返回成功事操作\
				         var odd=convertJson.string2json1(resultmsg);
				         var zs=odd.response.data[0].ZS;
				         //alert(resultmsg.response[data]);
				         document.getElementById("htxx").innerHTML='待结算合同信息('+zs+')';
				         },
				         error : function(result) {//返回失败操作
				           defaultJson.clearTxtXML();
				          }			
				});
		}
	}
	//详细信息
	 function rowView(index){
	     var obj = $("#DT1").getSelectedRowJsonByIndex(index);//获取行对象
	     var XMID = convertJson.string2json1(obj).XMID;
	     $(window).manhuaDialog(xmscUrl(XMID));//调用公共方法,根据项目编号查询
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
      <h4 class="title">结算管理
        <span class="pull-right">
        <% if(biaozhi.equals("2")) {%>
         <button id="htxx" class="btn"  type="button"></button> 
         <%}%> 
		 <button id="btnExpExcel" class="btn"  type="button">导出</button>
        </span>
      </h4>
     <form method="post" action="${pageContext.request.contextPath }/insertdemo.do" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<INPUT id="num" type="text" class="span12" keep="true" kind="text" fieldname="rownum" value="1000" operation="<="/>
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
      </table>
      </form>
      <div style="height:5px;"> </div>
  <div class="overFlowX">                                
<table  width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" >
	<thead>
		<tr>
		    <th  name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
		    <th fieldname="XMBH" rowspan="2" rowMerge="true" colindex=2  hasLink="true" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>
		 	<th fieldname="XMMC" rowspan="2" rowMerge="true"  colindex=3 maxlength="15" >&nbsp;项目名称&nbsp;</th>
		 	<th fieldname="BDBH" rowspan="2" colindex=4 maxlength="15" Customfunction="doBdBH" >&nbsp;标段编号&nbsp;</th>
		 	<th fieldname="BDMC" rowspan="2" colindex=5 maxlength="15" Customfunction="doBdmc" >&nbsp;标段名称&nbsp;</th>
		 	<th fieldname="ND" rowspan="2" colindex=6 tdalign="center">&nbsp;施工年度&nbsp;</th>
		 	<th fieldname="SGDW" rowspan="2" maxlength="15" colindex=7>&nbsp;施工单位&nbsp;</th>
		 	<th fieldname="JSZT" rowspan="2"  colindex=8 >&nbsp;结算状态&nbsp;</th>
			<!-- <th fieldname="JLDW" rowspan="2" maxlength="15" colindex=6>&nbsp;监理单位&nbsp;</th> -->
			<th  colspan="4"  >&nbsp;合同&nbsp;</th>
			<th  colspan="3"  >&nbsp;上报值&nbsp;</th>
			<th  colspan="3"  >&nbsp;业主审定&nbsp;</th>
			<th  colspan="3"  >&nbsp;财审审定&nbsp;</th>
			<th  colspan="3" >&nbsp;审计审定&nbsp;</th>
			<th  colspan="4" >&nbsp;差额值&nbsp;</th>
		    <th fieldname="JSQK" rowspan="2" maxlength="15" colindex=29 >&nbsp;结算情况&nbsp;</th>
		</tr>
		<tr> 
			<th fieldname="HTBH"  maxlength="15" colindex=9>&nbsp;编号&nbsp;</th>
		 	<th fieldname="HTMC"  maxlength="15" colindex=10>&nbsp;名称&nbsp;</th>
			<th fieldname="HTQDFS"    colindex=11>&nbsp;签订方式&nbsp;</th>
			<th fieldname="HTJE"  maxlength="15" colindex=12 tdalign="right"   >金额（元）<br>A</th>
			<th fieldname="TBR" colindex=13 maxlength="15">&nbsp;提报人&nbsp;</th>
			<th fieldname="TBRQ" colindex=14 tdalign="center">&nbsp;日期&nbsp;</th>
			<th fieldname="TBJE" colindex=15 tdalign="right"  maxlength="15">金额(元)<br>B</th>
			<th fieldname="WTZXGS" colindex=16  maxlength="15">&nbsp;委托咨询公司&nbsp;</th>
			<th fieldname="YZSDRQ" colindex=17 tdalign="center">&nbsp;日期&nbsp;</th>
			<th fieldname="YZSDJE" colindex=18 tdalign="right"  maxlength="15" >金额(元)<br>C</th>
		    <th fieldname="CSSDRQ" colindex=19 tdalign="center">&nbsp;日期&nbsp;</th>
			<th fieldname="CSSDJE" colindex=20 tdalign="right"  maxlength="15"  >金额(元)<br>D</th>
			<th fieldname="CSBGBH" colindex=21  maxlength="15">&nbsp;财审报告编号&nbsp;</th>
			<th fieldname="SJSDRQ" colindex=22 tdalign="center">&nbsp;日期&nbsp;</th>
			<th fieldname="SJSDJE" colindex=23 tdalign="right"  maxlength="15" >金额(元)<br>E</th>
			<th fieldname="SJBGBH" colindex=24  maxlength="15">&nbsp;审计报告编号&nbsp;</th> 
		    <th fieldname="TBYZ"  tdalign="center" noprint="true" colindex=25 >上报值-业主审定值<br>B-C</th>
		    <th fieldname="YZCS"  tdalign="center" noprint="true" maxlength="15" colindex=26 >业主审定值-财审值<br>C-D</th>
			<th fieldname="CSSJ"  tdalign="center" noprint="true" maxlength="15" colindex=27 >财审值-审计值<br>D-E</th>
			<th fieldname="TBSJ"  tdalign="center" noprint="true" maxlength="15" colindex=28 >上报值-审计值<br>B-E</th>
		</tr>
	</thead>
	 <tbody>
     </tbody>
</table>
		<form method="post" id="htForm" style="display: none">
						<table class="B-table" width="100%">
							<TR style="display: none;">
								<!-- <TR> -->
								<TD>
									<INPUT class="span12" type="text" name="GC_ZJB_JSB_ID"  fieldname="GC_ZJB_JSB_ID" id="GC_ZJB_JSB_ID" />
									<INPUT class="span12" type="text" id="XQZT" name="XQZT" fieldname="ZT" value="1" keep="true" />
								</TD>
							</TR>
						</table>
					</form>
</div>
</div>
</div>
  </div>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter" order="asc" fieldname="g.xmbh,g.xmbs,g.pxh"/>
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
         <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 </div>
</body>
</html>