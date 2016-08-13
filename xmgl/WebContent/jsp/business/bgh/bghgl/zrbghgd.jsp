<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
</head>
<body>
<script type="text/javascript">
	var controllername= "${pageContext.request.contextPath }/banGongHuiController.do";
	//计算本页自适应高度
	function setPageHeight(){
		var getHeight=getDivStyleHeight();
  		var height = getHeight-pageTopHeight-pageTitle-getTableTh(1)-pageNumHeight;
  		var pageNum = parseInt(height/pageTableOne,10);
  		$("#DT1").attr("pageNum",pageNum);
	};
	$(function() {
		setPageHeight();
		getNd();
		//查询
		var btn = $("#chaxun");
		btn.click(function()
				{  
					//生成json串
					var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
					//调用ajax插入
					defaultJson.doQueryJsonList(controllername+"?querybanGongHuiList",data,DT1,null,false);
				});
		//清空查询条件
	    var btn_clearQuery = $("#query_clear");
	    btn_clearQuery.click(function() 
	      {
	        $("#queryForm").clearFormResult();
	        getNd();
	      });
		var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		defaultJson.doQueryJsonList(controllername+"?querybanGongHuiList",data,DT1,null,true);
	});
	//默认年度
	function getNd(){
			setDefaultNd("LRSJ");
	}
	function doRandering(obj){
		var showHtml = "";
		showHtml = "<a href='javascript:void(0)' title='信息卡' onclick='openBGH();'><i class='icon-file showXmxxkInfo' ></i></a>";
		return showHtml;
	}
	function openBGH(){
	    var index = $(event.target).closest("tr").index();
		$("#DT1").cancleSelected();
    	$("#DT1").setSelect(index);
		if($("#DT1").getSelectedRowIndex()==-1){
			requireSelectedOneRow();
			return;
		}else{
			$(window).manhuaDialog({"title":"会议中心","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghgl/shytd.jsp","modal":"1"});
		}
	}			
</script>
<app:dialogs/>
<div class="container-fluid">
  <p></p>
  <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
     <form method="post"  id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display: none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			<INPUT id="num" type="text" class="span12" keep="true" kind="text" fieldname="rownum" value="1000" operation="<="/>
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
	          <th width="2%" class="right-border bottom-border text-right">年度</th>
	          <td width="5%" class="right-border bottom-border">
                 <select class="span12 year" id="LRSJ" name = "LRSJ" fieldname="to_char(LRSJ,'yyyy')"  defaultMemo="全部" operation="="  kind="dic" src="T#GC_BGH_WT:to_char(lrsj,'yyyy') :to_char(lrsj,'yyyy'):SFYX='1' group by to_char(lrsj,'yyyy') ">
	              </select>
	          </td>
	          <th width="2%" class="right-border bottom-border text-right" >会次</th>
	          <td width="10%" class=" right-border bottom-border"> 
	          <select class="span12" id="HC" name = "HC" fieldname="HC"  defaultMemo="全部" operation="="  kind="dic" src="T#GC_BGH:HC:HC:SFYX='1'">
	           </select>
	           </td>
	           <th width="5%" class="right-border bottom-border text-right" >提出部门</th>
	          <td width="10%" class=" right-border bottom-border"> 
	          <select class="span12" id="FQBM" name = "FQBM" fieldname="FQBM"  defaultMemo="全部" operation="="  kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME">
	           </select>
	           </td>
	             <th width="2%" class="right-border bottom-border text-right" >解决</th>
	          <td width="6%" class=" right-border bottom-border"> 
	          <select class="span12" id="ISJJ" name = "ISJJ" fieldname="ISJJ"  defaultMemo="全部" operation="="  kind="dic" src="SF">
	           </select>
	           </td>
	             <th width="5%" class="right-border bottom-border text-right" >解决部门</th>
	          <td width="10%" class=" right-border bottom-border"> 
	          <select class="span12" id="ZZBM" name = "ZZBM" fieldname="ZZBM"  defaultMemo="全部" operation="="  kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME">
	           </select>
	           </td>
	           <!--  <th width="5%" class="right-border bottom-border text-right" >是否超期</th>
	          <td width="6%" class=" right-border bottom-border"> 
	          <select class="span12" id="ISCQ" name = "ISCQ" fieldname="ISCQ"  defaultMemo="全部" operation="="  kind="dic" src="SF">
	           </select>
	           </td> -->
	          <th width="5%" class="right-border bottom-border text-right">是否督办</th>
	          <td width="6%" class=" right-border bottom-border">
	           <select class="span12" id="ISDU" name = "ISDU" fieldname="ISDU"  defaultMemo="全部" operation="="  kind="dic" src="SF">
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
<table  width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" printFileName="会议议题管理">
	<thead>
 		<tr>
         	<th  name="XH" id="_XH"  >&nbsp;#&nbsp;</th>
		     <th fieldname="WTBT"  tdalign="center" CustomFunction="doRandering" >&nbsp;&nbsp;</th>
		    <th fieldname="WTBT"    maxlength="15" >&nbsp;标题&nbsp;</th>
		 	<th fieldname="ISJJ"   tdalign="center">&nbsp;是否解决&nbsp;</th> 
		 	<!-- <th fieldname="ISCQ"  tdalign="center">&nbsp;是否超期&nbsp;</th> -->
		 	<th fieldname="WTLX">&nbsp;类型&nbsp;</th>
		 	<th fieldname="FQBM">&nbsp;发起部门&nbsp;</th>
		 	<th fieldname="ZZBM">&nbsp;解决部门&nbsp;</th>
		 	<th fieldname="YQJJSJ" tdalign="center">&nbsp;希望解决时间&nbsp;</th>
		 	<th fieldname="FJ"     >&nbsp;附件&nbsp;</th>
		 	<th fieldname="BGHJL"   maxlength="15"  >&nbsp;会议结论&nbsp;</th>
		 	<th fieldname="DBCS"   tdalign="right" >&nbsp;督办&nbsp;</th>
		 	<th fieldname="BGHHF"   maxlength="15"  >&nbsp;会议答复&nbsp;</th>
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
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
         <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 </div>
</body>
</html>