<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>insertDemo</title>

<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/default.js"> </script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/TabList.js"> </script>

<script type="text/javascript" charset="utf-8">

  var controllername= "${pageContext.request.contextPath }/qianQiShouXuController.do";
  var data1="";
  var json='';
  var id;
	$(function()
		{
		//查询
			var btn = $("#chaxun");
			btn.click(function() 
					{
						//生成json串
						var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
						//调用ajax插入
						defaultJson.doQueryJsonList(controllername+"?queryXiangMu",data,DT1);
	
					});
			//清空
			 var btn_clearQuery = $("#query_clear");
			    btn_clearQuery.click(function() {
			        $("#queryForm").clearFormResult();
			        //其他处理放在下面
			        init();
			    });
	 });      
	
	//确定
	$(function() 
		{
			var queding = $("#queding");
			queding.click(function(obj)
					{
						if(json.length==0)
							{
							requireSelectedOneRow();
							}
						else{
						parent.$("body").manhuaDialog.setData(json);
						parent.$("body").manhuaDialog.sendData();
						parent.$("body").manhuaDialog.close();
						    }
	             });   
	});
	//获取行数据为 对象
	function tr_click(obj,tabListid){
		json = obj;
		
	}

	
    $(document).ready(function() 
    {
    	init();
    	//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryXiangMu",data,DT1);
     }
    );
	 function init(){
	    	var d=new Date();
	    	var year=d.getFullYear();
	    	$("#ND").val(year);
	    }
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
  <p></p>
  <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">项目管理
	      <span class="pull-right"> 
		     <button id="queding" class="btn"  onclick="queding()"  type="button">确定</button> 
	       </span>
       </h4>
     <form method="post" action="${pageContext.request.contextPath }/insertdemo.do" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border"><INPUT type="text" class="span12" kind="text" keep="true" fieldname="rownum"  value="1000" operation="<=" ></TD>
           
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
          <th width="5%" class="right-border bottom-border text-right">年度</th>
	          <td width="10%" class="bottom-border">
	              <select class="span12" id="ND" name = "ND" fieldname="A.ND" kind="dic" src="XMNF" operation="=" >
	               </select>
	          </td>
	          <th width="5%" class="right-border bottom-border text-right">项目名称</th>
	          <td width="20%" class="right-border bottom-border ">
	             <input class="span12" type="text" placeholder="" name = "XMMC" fieldname = "A.XMMC" operation="like" logic = "and" ></td>
	           <td  class="text-left bottom-border text-right">
	                 <button id="chaxun" class="btn btn-link"  type="button"><i class="icon-search"></i>查询 </button> 
		             <button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空 </button>
               </td>
         </tr>
         
      </table>
      </form>
      <div style="height:5px;"> </div>
  <div class="overFlowX">  
<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
	<thead>
		<tr>
			<th  name="XH" id="_XH">&nbsp;#&nbsp;</th>
			<th fieldname="XMBH" tdalign="right">&nbsp;项目编号&nbsp;</th>
			<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
			<th fieldname="XMLX" tdalign="center">&nbsp;项目类型&nbsp;</th>
			<th fieldname="XMDZ" >&nbsp;起止点&nbsp;</th>
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
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id="frmPost">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter" order="desc" fieldname="A.XMBH,A.LRSJ"/>
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
          <input type="hidden" name="queryResult" id = "queryResult">
		
 </FORM>
 </div>
</body>
</html>