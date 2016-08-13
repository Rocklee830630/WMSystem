<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>拦标价管理</title>
<script type="text/javascript" charset="utf-8">
  var oTable1;  
  var controllername= "${pageContext.request.contextPath }/zjb/lbj/lbjController.do";
	$(function() {
		var btn = $("#query");
		var wh = $("#update");
		var excel=$("#excel");
		var clear=$("#clear");
		clear.click(function(){
			$("#LbjForm").clearFormResult();
			getNd();
		});
		btn.click(function() {
			//生成json串
			var data = combineQuery.getQueryCombineData(LbjForm,frmPost,DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryLbj",data,DT1);
			//
				});
		excel.click(function(){
			var t = $("#DT1").getTableRows();
			 if(t<=0)
			 {
			 xAlert("提示信息","请至少查询出一条记录！");
			 return;
			 }
			 printTabList("DT1");
		});
		wh.click(function() {
			window.location.href="${pageContext.request.contextPath }/jsp/business/zjb/lbj/lbjwh.jsp";
		});
	});
    $(document).ready(function() {
    	getNd();
    	g_bAlertWhenNoResult=false;	
   	 var data = combineQuery.getQueryCombineData(LbjForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryLbj",data,DT1);
		g_bAlertWhenNoResult=true;
   });
  //默认年度
    function getNd(){
    	//项目年份默认当前年
    	var d = new Date();
    	var year = d.getFullYear();
    	$("#ND").val(year);
    }
</script>      
</head>
<body>
<app:dialogs/>
	<div class="container-fluid">
		</p>
	<div class="row-fluid">
    <div class="B-small-from-table-box">
      <h4>信息查询<span class="pull-right">
         		 <button id="query" class="btn btn-inverse"  type="button">查询</button>
         		 <button id="clear" class="btn btn-inverse"  type="button">清空</button>
          		</span></h4>
     <form method="post" acdtion="${pageContext.request.contextPath }/insertdemo.do" id="LbjForm"  >
      <table class="B-table">
      <!--可以再此处加入hidden域作为过滤条件 -->
      <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<INPUT id="num" type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<=" keep="true"/>
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
        <th width="5%" class="right-border bottom-border text-right">合同年份</th>
         <td width="10%" class="right-border bottom-border">
            <select class="span12" kind="dic" id="ND" name = "ND" fieldname = "xmxdk.ND" src="XMNF" operation="=" >
            </select>
          </td>
          <th width="5%" class="left-border bottom-border text-right">项目名称</th>
          <td width="20%" class=" bottom-border"><input class="span12" type="text" placeholder="" name = "lbj.XMMC" fieldname = "lbj.XMMC" operation="like" logic = "and" ></td>
        <th></th>
        <td></td>
        
        </tr>
      </table>
      </form>
    </div>
  </div>
		<div class="row-fluid">
			<div class="B-small-from-table-auto">
				<!-- <h4>查询结果</h4> -->
				<h4>查询结果<span class="pull-right">
         		 <button id="update" class="btn btn-inverse"  type="button">维护</button>
         		 <button id="excel" class="btn btn-inverse"  type="button">导出</button>
          		</span></h4>
          		<div class= "overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
                <thead>
                    <tr>	
                    		<th name="XH" id="_XH" tdalign="">&nbsp;&nbsp;序号&nbsp;&nbsp;</th>
							<th fieldname="XMMC" tdalign="">&nbsp;&nbsp;项目名称&nbsp;&nbsp;</th>
							<th fieldname="BDMC" tdalign="">&nbsp;&nbsp;标段名称&nbsp;&nbsp;</th>
							<th fieldname="CSBGBH" tdalign="">&nbsp;&nbsp;财审报告编号&nbsp;&nbsp;</th>
							<th fieldname="TXQSJ" tdalign="center">&nbsp;&nbsp;提需求时间（招造价公司）&nbsp;&nbsp;</th>
							<th fieldname="ZBSJ" tdalign="center">&nbsp;&nbsp;招标时间（内部咨询公司）&nbsp;&nbsp;</th>
							<th fieldname="ZXGS" tdalign="">&nbsp;&nbsp;咨询公司&nbsp;&nbsp;</th>
							<th fieldname="YWRQ" tdalign="">&nbsp;&nbsp;提出疑问日期&nbsp;&nbsp;</th>
							<th fieldname="DYRQ" tdalign="">&nbsp;&nbsp;答疑日期&nbsp;&nbsp;</th>
							<th fieldname="SGFAJS" tdalign="center">&nbsp;&nbsp;施工方案接受日期&nbsp;&nbsp;</th>
							<th fieldname="ZBWJJS" tdalign="center">&nbsp;&nbsp;招标文件接收日期&nbsp;&nbsp;</th>
							<th fieldname="ZXGSRQ" tdalign="center">&nbsp;&nbsp;咨询公司交造价组日期&nbsp;&nbsp;</th>
							<th fieldname="JGBCSRQ" tdalign="center">&nbsp;&nbsp;建管报财审日期&nbsp;&nbsp;</th>
							<th fieldname="SBCSZ" tdalign="right">&nbsp;&nbsp;上报财审值（元）&nbsp;&nbsp;</th>
							<th fieldname="CZSWRQ" tdalign="center">&nbsp;&nbsp;财政审完日期&nbsp;&nbsp;</th>
							<th fieldname="CSSDZ" tdalign="right">&nbsp;&nbsp;财审审定值（元）&nbsp;&nbsp;</th>
							<th fieldname="SJZ" tdalign="right">&nbsp;&nbsp;审减值（元）&nbsp;&nbsp;</th>
							<th fieldname="SJBFB" tdalign="right">&nbsp;&nbsp;审减百分比（%）&nbsp;&nbsp;</th>
							<th fieldname="ZDJ" tdalign="right">&nbsp;&nbsp;暂定金&nbsp;&nbsp;</th>
							<th fieldname="BZ" tdalign="">&nbsp;&nbsp;备注&nbsp;&nbsp;</th>
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
		<FORM name="frmPost" method="post" style="display: none"
			target="_blank">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML" /> 
			<input type="hidden" name="txtXML" id="txtXML" /> 
			<input type="hidden" name="resultXML" id="resultXML" />
			<input type="hidden" name="queryResult" id = "queryResult" />
			<input type="hidden" name="txtFilter"  order="asc" fieldname = "jhsj.xmbh,jhsj.xmbs,jhsj.pxh"	id = "txtFilter">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
		</FORM>
	</div>
</body>
</html>