<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<style type="text/css">
.xiahuaxian{
				overflow:hidden; 
			    color:#0088cc;
			}
			.xiahuaxian:hover,
			.xiahuaxian:focus{
				cursor:pointer;
			    color:#005580;
				text-decoration:underline;
			}

</style>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/gk/gkController.do";

$(document).ready(function(){
	
	 //生成json串
	var data2 = combineQuery.getQueryCombineData(queryForm,frmPost);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query_jhgk",data2,DT2); 
	
 	//生成json串
	var data1 = combineQuery.getQueryCombineData(queryForm,frmPost);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query_tcjh",data1,DT1);
 });

</script>      
</head>

</head>
<body>

<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
  <h4 class="title" align="center">计划管理业务概况
  </h4>
 <form method="post" action="${pageContext.request.contextPath }/insertdemo.xhtml" id="demoForm"  >
     	<input class="span12" id="GC_ZJB_JSB_ID" type="hidden" placeholder=""  check-type=""  fieldname="GC_ZJB_JSB_ID" name = "GC_ZJB_JSB_ID">
      		<h4 class="title">统筹计11111划概况</h4>
      		 	<p class="primary">
                                                  项目下达数 <font style="font-size:18px;font-weight:bold; text-decoration:underline; color: blue;" >50</font>，其中：应急
                 <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">21</font> [20%] ，
                                         稳定  <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">21</font>  [80%] ，
                                         基本稳定  <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">21</font>  [10%] ，
                                         不稳定  <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">21</font>[10%]
            	</p>
<table  width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" >
	<thead>
		<tr>
		    <th  name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
		    <th fieldname="JCPC" rowspan="2" colindex=2  hasLink="true" linkFunction="rowView">&nbsp;批次&nbsp;</th>
		 <!-- 	<th fieldname="" rowspan="2" colindex=3 maxlength="15" >&nbsp;最新版本&nbsp;</th> -->
		 	<th fieldname="XDSJ" rowspan="2" colindex=4 maxlength="15" >&nbsp;下达时间&nbsp;</th>
		 	<th fieldname="ZS" rowspan="2" colindex=5 tdalign="center">&nbsp;总数&nbsp;</th>
			<th colspan="2"  >&nbsp;项目数量&nbsp;</th>
			<th fieldname="" rowspan="2" maxlength="15" colindex=7>&nbsp;调整&nbsp;</th>
			<th colspan="3"  >&nbsp;项目管理公司&nbsp;</th>
			<th fieldname="QITA" rowspan="2" colindex=11 maxlength="15">&nbsp;其它&nbsp;</th>
			<th fieldname="JHZTZE" rowspan="2" colindex=12 tdalign="center">&nbsp;总投资（万元）（计财）&nbsp;</th>
		</tr>
		<tr> 
		    <th fieldname="XMSX" colindex=25 >&nbsp;应急&nbsp;</th>
		    <th fieldname="ISBT" maxlength="15" colindex=26 >&nbsp;BT&nbsp;</th>
		 	<th fieldname="ZHONGHAO" maxlength="15" colindex=8>&nbsp;中豪&nbsp;</th>
			<th fieldname="HUAXING" colindex=9>&nbsp;华星&nbsp;</th>
			<th fieldname="HONGAN" maxlength="15" colindex=10 tdalign="right"   >鸿安</th>
		</tr>
	</thead>
	 <tbody>
	 </tbody>
</table>
<h4 class="title">统筹计划跟踪情况</h4>
   	<table class="B-table" width="100%">
        	 	<tr>
          			<th width="10%"  class="right-border bottom-border text-right">项目概况</th>
        			<td  width="90%" class="right-border bottom-border">
        			  <p class="primary">
					计划编制项目数：应编制 <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">10</font>，
					其中：全部完成 <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">10</font>[75%]，
					部分完成  <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">10</font>  [5%]，
					未编制  <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">10</font>  [20%]
					<br></br>执行情况：  已完成   <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">10</font>  [20%]，
					正常执行  <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">10</font>  [30%]，
					预警  <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">10</font>  [20%]，
					严重延期  <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">10</font>  [30%]
					<br></br>存在问题：  重大问题   <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">10</font> [20%]，
					严重问题   <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">10</font>  [60%]，
					一般问题 <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">10</font> [20%]
                    </p>
        			</td>
          		</tr>
     </table>  
     <table class="B-table" width="100%">
        	 	<tr>
          			<th width="10%"  class="right-border bottom-border text-right">节点概况</th>
        			<td  width="90%" class="right-border bottom-border">
        			  <p class="primary">
        			         计划总节点数： <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">300</font>，
        			         其中：已完成节点<font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">150</font>[50%]，
        			          预警节点  <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">50</font>
						<br></br>节点完成情况：  正常完成  <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">20</font>  [50%]，
						延期完成  <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">8</font>  [20%]，
						严重延期完成 <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">12</font>  [30%]
						<br></br>节点预警情况：  一般超期   <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">30</font>  [60%]，
						严重超期 <font style="font-size:18px;font-weight:bold;text-decoration:underline; color: blue;">20</font>  [40%]
                    </p>
        			</td>
          		</tr>
     </table> 
<h4 class="title">项目公司概况</h4>
<table width="100%" class="table-hover table-activeTd B-table" id="DT2" type="single" editable="0">
     <thead>
         <tr>	

            <th fieldname="XMGLGS" tdalign="center" hasLink="true" linkFunction="rowView">&nbsp;项目管理公司&nbsp;</th>
			<th fieldname="ZS" tdalign="" maxlength="15">&nbsp;总数&nbsp;</th>
			<th fieldname="CQT_SJ" tdalign="" maxlength="15">&nbsp;已拆迁&nbsp;</th>
			<th fieldname="PQT_SJ" tdalign="">&nbsp;已排迁&nbsp;</th>
			<th fieldname="KGSJ_SJ" tdalign="center" class="wrap">&nbsp;已开工&nbsp;</th>
			<th fieldname="WGSJ_SJ" tdalign="center" class="wrap">&nbsp;已完工&nbsp;</th>
		</tr>
	</thead>
	<tbody>
   </tbody>
</table>  
<h4 class="title">节点详情</h4>
<table width="100%" class="table-hover table-activeTd B-table" id="DT3" type="single" editable="0">
     <thead>
         <tr>	
             <th name="XH" id="_XH" rowspan="2" tdalign="">&nbsp;#&nbsp;</th>
            <th fieldname=""  rowspan="2"  tdalign="center" colindex=1>&nbsp;专业/阶段&nbsp;</th>
			<th fieldname=""  rowspan="2" tdalign="" colindex=2 >&nbsp;总数&nbsp;</th>
			<th fieldname=""  colspan="3"  >&nbsp;完成节点&nbsp;</th>
			<th fieldname="" rowspan="2" colindex=6 >&nbsp;总数&nbsp;</th>
			<th colspan="2"  >&nbsp;预警节点&nbsp;</th>	
		</tr>
		<tr> 
		    <th fieldname="" colindex=3 >&nbsp;正常&nbsp;</th>
		    <th fieldname="" maxlength="15" colindex=4 >&nbsp;延期&nbsp;</th>
		 	<th fieldname="" maxlength="15" colindex=5>&nbsp;验证延期&nbsp;</th>
			<th fieldname="" colindex=7>&nbsp;一般超期&nbsp;</th>
			<th fieldname="" maxlength="15" colindex=8 >严重超期</th>	
		</tr>
	</thead>
	<tbody>
   </tbody>
</table>  	
</form>
<h4 class="title">计财计划下达概况</h4>
<table  width="100%" class="table-hover table-activeTd B-table" id="DT4" type="single" >
	<thead>
		<tr>
		    <th fieldname="" name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
		    <th fieldname="" rowspan="2" colindex=2  hasLink="true" linkFunction="rowView">&nbsp;计财批次&nbsp;</th>
		 	<th fieldname="" rowspan="2" colindex=4 maxlength="15" >&nbsp;下达时间&nbsp;</th>
			<th colspan="3"  >&nbsp;项目数量&nbsp;</th>
			<th colspan="4"  >&nbsp;总投资&nbsp;</th>
		</tr>
		<tr> 
		    <th fieldname=""  colindex=25 >&nbsp;总数&nbsp;</th>
		    <th fieldname=""   colindex=25 >&nbsp;应急&nbsp;</th>
		    <th fieldname=""  maxlength="15" colindex=26 >&nbsp;BT&nbsp;</th>
		 	<th fieldname=""  maxlength="15" colindex=8>&nbsp;工程费用&nbsp;</th>
			<th fieldname=""  colindex=9>&nbsp;征收费用&nbsp;</th>
			<th fieldname=""  maxlength="15" colindex=10 tdalign="right">其它</th>
			<th fieldname="" maxlength="15" colindex=11 tdalign="right">合计</th>
		</tr>
	</thead>
	 <tbody>
     </tbody>
</table>
     	</div>
 	</div>
</div>
   <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
 <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		 <!--系统保留定义区域-->
		   <input type="hidden" name="ywid" id = "ywid" value="">
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "XMNF"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
 <p></p>
<form method="post" id="queryForm">
	<INPUT type="hidden" class="span12" kind="text" id="num" fieldname="rownum"  value="1000" operation="<=" >
</form>
</body>
</html>