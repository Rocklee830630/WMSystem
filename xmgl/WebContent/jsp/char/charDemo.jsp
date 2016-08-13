<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>insertDemo</title>

<!-- datatable css -->
		<style type="text/css" title="currentStyle">
			@import "${pageContext.request.contextPath }/css/demo_table.css";
		</style>
	<!-- datatable css -->
	
<!-- datatable js -->
<script type="text/javascript" src="/xmgl/js/datatable/jquery-latest.js"> </script>
<script type="text/javascript" src="/xmgl/js/datatable/jquery.dataTables.js"> </script>

<link href="${pageContext.request.contextPath }/jsp/char/assets/ui/css/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath }/jsp/char/assets/prettify/prettify.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/assets/prettify/prettify.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/assets/ui/js/json2.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/assets/ui/js/lib.js" ></script>


<!-- datatable js -->
<%
String[][] chartResult = (String[][])request.getAttribute("chartResult") == null 
		? new String[0][0] : (String[][])request.getAttribute("chartResult");
		
String year1 = (String)request.getAttribute("year1") == null ? "" : (String)request.getAttribute("year1");
String year2 = (String)request.getAttribute("year2") == null ? "" : (String)request.getAttribute("year2");

String first = request.getParameter("first");
String xml = (String)request.getAttribute("xml") == null ? "" : (String)request.getAttribute("xml");
%>  
</head>
<body>
<div class="container-fluid" style="min-width:1200px;">
  <div class="row-fluid">
    <div class="span7">
    <p></p>
		  <div class="row-fluid">
		    <div class="B-small-from-table-box">
		    
		     <form method="post" id="queryForm" action="${pageContext.request.contextPath }/CharServlet">
		     <input type="hidden" name="catogoryChart" value="jshzChart">
		     <input type="hidden" name="targetPage" value="charDemo.jsp">
		      <h4>信息查询 <span class="pull-right">  <button class="btn btn-inverse" type="submit">查询</button> </span></h4>
		      <table class="table-hover table-activeTd B-table" id="DT1"> 
		        <tr>
		          <th width="8%" class="right-border bottom-border">起始年份</th>
		          <td width="25%" class="right-border bottom-border">
		       <%--    	<input class="span12" type="text" placeholder="" name = "year1" id = "year1" value="<%=year1 %>"> --%>
		          	<select id="year1" name='year1'>
				        <option value="">请选择</option>
				        <option value="2010" selected>2010</option>
				        <option value="2011">2011</option>
				        <option value="2012">2012</option>
				        <option value="2013">2013</option>
			        </select>
		          </td>
		          <th width="8%" class="right-border bottom-border">结束年份</th>
		          <td width="25%" class="bottom-border">
		        <%--   	<input class="span12" type="text" placeholder="" name = "year2" id = "year2" value="<%=year2 %>"> --%>
		        	<select id="year2" name='year2'>
				        <option value="">请选择</option>
				        <option value="2010">2010</option>
				        <option value="2011">2011</option>
				        <option value="2012">2012</option>
				        <option value="2013" selected>2013</option>
			        </select>
		          </td>
		        </tr>
		      </table>
		      </form>
		    </div>
		  </div>
		 <div class="row-fluid">
		 <div class="B-small-from-table-box">
		<table class="B-table">
        <tr>
          <th align="center" style="border-bottom: #000 solid 1px;border-right: #000 solid 1px" rowspan="3">年度</th>
          <th align="center" style="border-bottom: #000 solid 1px;" colspan="7">办理结算情况</th>
        </tr>
        <tr>
          <th align="center" style="border-bottom: #000 solid 1px;border-right: #000 solid 1px" colspan="2">建管中心</th>
          <th align="center" style="border-bottom: #000 solid 1px;border-right: #000 solid 1px" colspan="3">财审中心</th>
          <th align="center" style="border-bottom: #000 solid 1px;" colspan="2">审计局</th>
        </tr>
        <tr>
          <th align="center" style="border-bottom: #000 solid 1px;border-right: #000 solid 1px">正在审</th>
          <th align="center" style="border-bottom: #000 solid 1px;border-right: #000 solid 1px">审完</th>
          <th align="center" style="border-bottom: #000 solid 1px;border-right: #000 solid 1px">正在审 </th>
          <th align="center" style="border-bottom: #000 solid 1px;border-right: #000 solid 1px">审完未报审计</th>
          <th align="center" style="border-bottom: #000 solid 1px;border-right: #000 solid 1px">审完已报审计</th>
          <th align="center" style="border-bottom: #000 solid 1px;border-right: #000 solid 1px">正在审 </th>
          <th align="center" style="border-bottom: #000 solid 1px;">审完 </th>
        </tr>
         <%
        	for(int i = 0; i < chartResult.length; i++) {
        %>
          <tr>
        <%
        		for(int j = 0; j < chartResult[i].length; j++) {
        			if(i == 3 && j == 7) {
        %>
          <td align="center" ><%=chartResult[i][j] %></td>
        <%
        				continue;
        			} else if(j == 7) {
		%>
          <td align="center" style="border-bottom: #000 solid 1px;"><%=chartResult[i][j] %></td>
        <%
						continue;
        			} else if(i == 3) {
		%>
          <td align="center" style="border-right: #000 solid 1px;"><%=chartResult[i][j] %></td>
        <%
        				continue;
        			}
        %>
          <td align="center" style="border-bottom: #000 solid 1px;border-right: #000 solid 1px;"><%=chartResult[i][j] %></td>
        <%
        		}
        %>
        </tr>
      	<%
        	}
        %>
      </table>
		</div>
		</div>
    </div>

    <div class="span5">
    <p></p>
      <div class="row-fluid">
		  <div class="B-small-from-table-box">
	      <h4>选择图表类型 <span class="pull-right">  <button class="btn btn-inverse" type="button" onclick="changeChart()">查询</button> </span></h4>
	      <table class="B-table">
	        <tr>
	          <td width="20" class="text-left bottom-border">
	          <select id="lx">
	          	<option value="0">2D柱状图</option>
	          	<option value="1">3D柱状图</option>
	          	<option value="2">3D立体柱状图</option>
	          	<option value="3">3D饼状图</option>
	          	<option value="4">3D立体线性图</option>
	          	<option value="5">3D长条图</option>
	          </select>
	   <!--        <form name="redioFrm">
	          	2D柱状图 <input type="radio" name="lx" value="0" onclick="changeRadio(this.value)" checked>
	          	3D柱状图 <input type="radio" name="lx" value="1"  onclick="changeRadio(this.value)">
	          	3D立体柱状图 <input type="radio" name="lx" value="2"  onclick="changeRadio(this.value)"> <br>
	          	3D饼状图 <input type="radio" name="lx" value="3"  onclick="changeRadio(this.value)"> 
	          	3D立体线性图 <input type="radio" name="lx" value="4"  onclick="changeRadio(this.value)">
	          	3D长条图 <input type="radio" name="lx" value="5"  onclick="changeRadio(this.value)">
	          </form> -->
	          </td>
	        </tr>
	      </table>
	    </div>
	  </div>
      <div id="chartdiv" align="center">Chart will load here</div>
    </div>
  </div>
</div>

<script type="text/javascript">
var chartsLx = "MSColumn2D";
$(function() {
	
	var first = "<%=first %>";
	if(first == "1")
		document.getElementById("queryForm").submit();
	
//	alert("${pageContext.request.contextPath }/CharServlet");
	if (GALLERY_RENDERER && GALLERY_RENDERER.search(/javascript|flash/i)==0){  FusionCharts.setCurrentRenderer(GALLERY_RENDERER)}; 
	
	year1 = document.getElementById("year1").value;
	year2 = document.getElementById("year2").value;
	var chart = new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/" + chartsLx + ".swf", "ChartId", "520", "400", "0", "0");
	chart.setDataURL("${pageContext.request.contextPath }/ChartXmlServlet?year1="+year1+"&year2="+year2+"&chartsLx="+chartsLx+"&first="+<%=first %>);
	chart.render("chartdiv");
});

function changeChart() {
	var lx = document.getElementById("lx").value;
	if(lx == '0')
		chartsLx = "MSColumn2D";
	else if(lx == '1')
		chartsLx = "MSColumn3D";
	else if(lx == '2') 
		chartsLx = "MSCombi3D";
	else if(lx == '3')
		chartsLx = "Pie3D";
	else if(lx == '4')
		chartsLx = "MSCombi3D";
	else 
		chartsLx = "MSBar3D";
//	alert(chartsLx);
	var chart = new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/" + chartsLx + ".swf", "ChartId", "510", "400", "0", "0");
	chart.setDataURL("${pageContext.request.contextPath }/ChartXmlServlet?year1="+year1+"&year2="+year2+"&chartsLx="+lx+"&first="+<%=first %>);
    chart.render("chartdiv");

}
var chartsRadio = "";
function changeRadio(radioValue){
	chartsRadio = radioValue;
}
</script>

</body>
</html>