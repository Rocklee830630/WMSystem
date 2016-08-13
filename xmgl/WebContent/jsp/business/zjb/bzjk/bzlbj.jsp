<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title></title>
<% 
	String lujing = request.getParameter("lujing");
	String mingchen = request.getParameter("mingchen");
	String tiaojian = request.getParameter("tiaojian");
	String nd = request.getParameter("nd");
%>
<script type="text/javascript" charset="utf-8">
  var json2,json;
  var rowindex,rowValue,text,aa;
  var jhsjid,lbjid,bdmc,zxgs;
  var controllername= "${pageContext.request.contextPath }/zjbmjkController.do";
  var lujing = "<%=lujing %>";
  var mingchen = "<%=mingchen%>";
  var nd = "<%=nd%>";
  var tiaojian = "<%=tiaojian%>";

  
	//计算本页表格分页数
	function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
  
	$(function() {
		setPageHeight();
		//页面初始化
		ready();
		//导出Excel
		var excel=$("#excel");
		excel.click(function(){
			 if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
			      printTabList("DT1");
			  }
		});
		
	});
	//初始化方法
    function ready() {
   	 	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryLbj&lujing="+lujing+"&mingchen="+mingchen+"&nd="+nd+"&tiaojian="+tiaojian,data,DT1,null,true);
   };
	//详细信息
	function rowView(index){
		var obj = $("#DT1").getSelectedRowJsonByIndex(index);
		var id = convertJson.string2json1(obj).XMID;
		$(window).manhuaDialog(xmscUrl(id));
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
      <h4 class="title">拦标价管理<span class="pull-right">
          		 <button id="excel" class="btn"  type="button">导出</button>
          		</span></h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<INPUT id="num" type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<=" keep="true"/>
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
        </tr>
      </table>
      </form>
	  <div style="height:5px;"> </div>
        <div class= "overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" editable="0">
                <thead>
                    <tr>	
                    		<th name="XH" id="_XH"  tdalign="">&nbsp;#&nbsp;</th>
                    		<th fieldname="XMBH"  rowMerge="true"  hasLink="true" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC"  rowMerge="true" maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDBH"  maxlength="15" Customfunction="doBdBH" >&nbsp;标段编号&nbsp;</th>
							<th fieldname="BDMC"  tdalign=""  Customfunction="doBdmc" maxlength="15">&nbsp;标段名称&nbsp;</th>
							<th fieldname="CSBGBH" tdalign="">&nbsp;财审报告编号&nbsp;</th>
							<th fieldname="LRSJ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;提需求时间<p></p>(招造价公司)</span></th>
							<th fieldname="KBRQ"  tdalign="center" class="wrap"><span style="width:120px;display:block;">招标时间<p></p>(内部咨询公司)</span></th>
							<th fieldname="ZXGS"  tdalign="" maxlength="15">&nbsp;咨询公司&nbsp;</th>
							<th fieldname="TZJJSJ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;与总工办交接图纸日期</span></th>
							<th fieldname="ZXGSJ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;发给咨询公司图纸日期</span></th>
							<th fieldname="YWRQ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;提出疑问日期</span></th>
							<th fieldname="HFRQ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;答疑日期</span></th>
							<!-- <th fieldname="SGFAJS"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;施工方案接收日期</span></th>
							<th fieldname="ZBWJJS"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;招标文件接收日期</span></th> -->
							<th fieldname="ZXGSRQ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;咨询公司<p></p>交造价组日期</span></th>
							<th fieldname="JGBCSRQ"  tdalign="center" class="wrap"><span style="width:80px;display:block;">&nbsp;建管报财审日期</span></th>
							<th fieldname="SBCSZ"  tdalign="right" maxlength="15" >&nbsp;上报财审值(元)&nbsp;</th>
							<th fieldname="CZSWRQ" tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;财政审完日期</span></th>
							<th fieldname="CSSDZ" tdalign="right" maxlength="15"  >&nbsp;财审审定值(元)&nbsp;</th>
							<th fieldname="SJZ"  tdalign="right" maxlength="15" >&nbsp;审减值(元)&nbsp;</th>
							<th fieldname="SJBFB"  tdalign="right" >&nbsp;审减百分比(%)&nbsp;</th>
							<th fieldname="ZDJ"  tdalign="right" maxlength="15" class="wrap" ><span style="width:105px;display:block;">&nbsp;暂定金</span></th>
							<th fieldname="BZ"  tdalign="" maxlength="15">&nbsp;备注&nbsp;</th>
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
			<input type="hidden" name="queryXML" id="queryXML"> 
			<input type="hidden" name="txtXML" id="txtXML"> 
			<input type="hidden" name="resultXML" id="resultXML">
			<input type="hidden" name="queryResult" id = "queryResult" />
			<input type="hidden" name="txtFilter"  order="asc" fieldname = "jhsj.xmbh,jhsj.xmbs,jhsj.pxh" id = "txtFilter">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
		</FORM>
	</div>
</body>
</html>