<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>insertDemo</title>
<script type="text/javascript" charset="utf-8">

  var controllername= "${pageContext.request.contextPath }/zjb/lbj/lbjController.do";
  var data1="";
  var json,id;
	$(function() {
		var btn = $("#chaxun");
		btn.click(function() {
					//生成json串
					var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
					//调用ajax插入
					defaultJson.doQueryJsonList(controllername+"?queryBD",data,DT1);

				});
		
	
	});      
	
	
	$(function() {
		var queding = $("#queding");
		queding.click(function(obj) {
			if(json.length==0)
				{
				alert("请选择合同");
				}
			else{
			$(window).manhuaDialog.setData(json);
			$(window).manhuaDialog.sendData();
			$(window).manhuaDialog.close();
			    }
             });   
	});
	//获取行数据为 对象
	function tr_click(obj){
		json = obj;
		
	}

	
    $(document).ready(function() {
    	g_bAlertWhenNoResult=false;
    	 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryBD",data,DT1);
		g_bAlertWhenNoResult=true;
    	
    }
    );

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
  <p></p>
  <div class="row-fluid">
    <div class="B-small-from-table-box">
      <h4>信息查询<span class="pull-right">  <button id="chaxun" class="btn btn-inverse"  type="button">查询</button> </span></h4>
     <form method="post" action="${pageContext.request.contextPath }/insertdemo.do" id="queryForm"  >
      <table class="B-table">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border"><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" ></TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
          <th width="5%" class="right-border bottom-border">项目名称</th>
          <td width="20%" class="bottom-border"><input class="span12" type="text" placeholder="" name = "XMMC" fieldname = "b.XMMC" operation="like" logic = "and" ></td>
          <th width="5%" class="right-border right-border bottom-border text-right">年份</th>
          <td width="10%" colspan="1" class="left-border bottom-border text-right">
            <select class="span12" name = "ND" kind="dic" src="XMNF" fieldname = "b.ND" operation="=" >
            </select>
          </td>
          <th></th>
          <td></td>
          </tr>
      </table>
      </form>
    </div>
  </div>
 <div class="row-fluid">
 <div class="B-small-from-table-auto">
  <h4>查询结果<span class="pull-right">  
  <button id="queding" class="btn btn-inverse" type="button">确定</button> 
  </span></h4>
    <div class="overFlowX">
<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
	<thead>
		<tr>
			<th name="XH" id="_XH">&nbsp;&nbsp;&nbsp;&nbsp;序号&nbsp;&nbsp;&nbsp;&nbsp;</th>
			<th fieldname="XMBH" >&nbsp;&nbsp;&nbsp;&nbsp;项目编号&nbsp;&nbsp;&nbsp;&nbsp;</th>
			<th fieldname="XMMC" >&nbsp;&nbsp;&nbsp;&nbsp;项目名称&nbsp;&nbsp;&nbsp;&nbsp;</th>
			<th fieldname="XMLX" >&nbsp;&nbsp;&nbsp;&nbsp;项目类型&nbsp;&nbsp;&nbsp;&nbsp;</th>
			<th fieldname="BDMC" >&nbsp;&nbsp;&nbsp;&nbsp;标段名称&nbsp;&nbsp;&nbsp;&nbsp;</th>
			<th fieldname="QDZH" >&nbsp;&nbsp;&nbsp;&nbsp;起点桩号&nbsp;&nbsp;&nbsp;&nbsp;</th>
			<th fieldname="ZDZH" >&nbsp;&nbsp;&nbsp;&nbsp;终点桩号&nbsp;&nbsp;&nbsp;&nbsp;</th>
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
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 </FORM>
 </div>
</body>
</html>