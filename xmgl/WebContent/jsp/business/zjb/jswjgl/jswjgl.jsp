<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script type="text/javascript" charset="utf-8">
   var controllername= "${pageContext.request.contextPath }/zaoJiaRenWuController.do";
	//计算本页表格分页数
	function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(3)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
	$(function() {
		setPageHeight();
		//查询
		var btn = $("#chaxun");
		btn.click(function()
				{  
					//生成json串
					var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
					//调用ajax插入
					defaultJson.doQueryJsonList(controllername+"?queryZaoJiaRenWu",data,DT1,null,false);
				});
		//新增
		var weihu = $("#weihu");
	 	weihu.click(function() 
	 			{
	 		     $(window).manhuaDialog({"title":"结算文件>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zjb/jswjgl/jswjAdd.jsp","modal":"4"});
	            }); 
	 	//修改
	 	var xiugai = $("#xiugai");
	 	xiugai.click(function() 
	 			{
	 		      var index1 =	$("#DT1").getSelectedRowIndex();
			 		if(index1<0){
			 			requireSelectedOneRow();
					}else{
			 		 $("#resultXML").val($("#DT1").getSelectedRow());
			 		 $(window).manhuaDialog({"title":"结算文件>修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zjb/jswjgl/jswjEdit.jsp","modal":"4"});
			 		}
			}); 
	 	
	 	//清空查询条件
	    var btn_clearQuery = $("#query_clear");
	    btn_clearQuery.click(function() 
	      {
	        $("#queryForm").clearFormResult();
	        getNd();
	      });
	    //导出
	    $("#btnExpExcel").click(function() 
	       {
	    	if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
    	  }
	   	});
	  /*   getNd(); */
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryZaoJiaRenWu",data,DT1,null,true);
	});
	//获取行数据json串
	function tr_click(obj,tabListid)
	{
		
	}
	//子页面调用修改行
	function xiugaihang(data)
	{
		var index =	$("#DT1").getSelectedRowIndex();
		var subresultmsgobj = defaultJson.dealResultJson(data);
		var comprisesJson = $("#DT1").comprisesJson(subresultmsgobj,index);
		$("#DT1").updateResult(JSON.stringify(comprisesJson),DT1,index);
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(index);
		xSuccessMsg("操作成功","");
	}
	//子页面调用添加行
	function tianjiahang(data)
	{
		var subresultmsgobj = defaultJson.dealResultJson(data);
	
		$("#DT1").insertResult(JSON.stringify(subresultmsgobj),DT1,1);
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(0);
	    xSuccessMsg("操作成功","");
	}
	//默认年度
	function getNd(){
			setDefaultNd("LRSJ");
	}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
  <p></p>
  <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">结算文件管理
        <span class="pull-right"> 
       <%--  </app:oPerm>
        <app:oPerm url="/jsp/business/zjb/jsgl/jiesuanwh.jsp"> --%>
        <button id="weihu" class="btn"  type="button">新增</button> 
        <%-- </app:oPerm>
         <app:oPerm url="/jsp/business/zjb/jsgl/jiesuanxg.jsp"> --%>
		<button id="xiugai" class="btn"  type="button">修改</button>
		 <%--  </app:oPerm> --%>
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
        <tr>
        	  <th width="5%" class="right-border bottom-border text-right">年度</th>
	          <td width="8%" class="right-border bottom-border">
                 <select class="span12 year" id="ND" name = "ND" fieldname="ND"  defaultMemo="全部" operation="="  kind="dic" src="T#GC_JH_SJ: distinct ND AS NDCODE:ND:SFYX='1' ORDER BY NDCODE">
	              </select>
	          </td>
	          <td  class="text-left bottom-border text-right">
	              <button id="chaxun" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
                  <button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
             </td>
        </tr>
      </table>
      </form>
      <div style="height:5px;"> </div>
  <div class="overFlowX">                                
<table  width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" printFileName="结算管理">
	<thead>
		<tr>
		    <th  name="XH" id="_XH"  colindex=1>&nbsp;#&nbsp;</th>
	     	<th fieldname="ND"   colindex=2 maxlength="15" >&nbsp;年度&nbsp;</th>
		    <th fieldname="JSDATE"  colindex=3 >&nbsp;接收时间&nbsp;</th>
		 	<th fieldname="GC_ZJB_JSWJ_ID" colindex=4 >&nbsp;附件&nbsp;</th>
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
         <input type="hidden" name="txtFilter" order="asc" fieldname="nd"/>
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
         <input type="hidden" name="queryResult" id = "queryResult">
 	</FORM>
 </div>
</body>
</html>