<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>计划管理-统筹计划管理</title>
<%
	String num = request.getParameter("num");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/tcjh/tcjhController.do";

$(document).ready(function(){
	
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,tcjhList);
});

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
<form method="post" id="queryForm">
	<INPUT type="hidden" class="span12" kind="text" id="num" fieldname="rownum"  value="<%=num%>" operation="<=" >
</form>
 <div class="row-fluid">
 <div class="B-small-from-table-auto">
  <h4>查询结果 
  	<span class="pull-right">  		
 		<button id="btnExp" class="btn btn-inverse"  type="button">导出</button>
  	</span>
  </h4>
  <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="tcjhList" width="100%" type="single">
		<thead>
			<tr>
				<th name="XH" id="_XH" rowspan="2" colindex=1>序号</th>
				<th fieldname="XMBH" rowspan="2" colindex=2>项目编号</th>
				<th fieldname="XMMC" rowspan="2" colindex=3>项目名称</th>
				<th fieldname="XMDZ" rowspan="2" colindex=4 maxlength="30">项目建设位置</th>
				<th fieldname="JSNR" rowspan="2" colindex=5 maxlength="50">项目建设内容及规模</th>
				<th fieldname="XMXZ" rowspan="2" colindex=6>项目性质</th>
				<th fieldname="XMGLGS" rowspan="2" colindex=7>项目管理公司</th>
				<th colspan="3">工期安排（工程部及项目管理公司）</th>
				<th fieldname="NDJSRW" rowspan="2" colindex=11>年度目标</th>
				<th colspan="4">手续办理情况</th>
				<th colspan="4">设计情况</th>
				<th colspan="2">造价</th>
				<th colspan="2">招标</th>
				<th fieldname="ZC" rowspan="2" colindex=24>征迁</th>
				<th fieldname="PQ" rowspan="2" colindex=25>排迁</th>
				<th colspan="2">施工（工程部及项目管理公司）</th>
				<th fieldname="JG" rowspan="2" colindex=28>交工</th>
				<th fieldname="BZ" rowspan="2" colindex=29>备注</th>
			</tr>
			<tr>
				<th fieldname="KGSJ_SJ" colindex=8>开工时间</th>
				<th fieldname="WGSJ_SJ" colindex=9>完工时间</th>
				<th fieldname="NDJSNR" colindex=10>年度主要建设内容</th>
				<th fieldname="KYPF" colindex=12>可研批复</th>
				<th fieldname="HPJDS" colindex=13>划拔决定书</th>
				<th fieldname="GCXKZ" colindex=14>工程规划许可证</th>
				<th fieldname="SGXK" colindex=15>施工许可</th>
				<th fieldname="CBSJPF" colindex=16>初步设计批复</th>
				<th fieldname="CQT" colindex=17>拆迁图</th>
				<th fieldname="PQT" colindex=18>排迁图</th>
				<th fieldname="SGT" colindex=19>施工图</th>
				<th fieldname="TBJ" colindex=20>提报价</th>
				<th fieldname="CS" colindex=21>财审</th>
				<th fieldname="JLDW" colindex=22>监理单位</th>
				<th fieldname="SGDW" colindex=23>施工单位</th>
				<th fieldname="KGSJ" colindex=26>开工时间</th>
				<th fieldname="WGSJ" colindex=27>完工时间</th>
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