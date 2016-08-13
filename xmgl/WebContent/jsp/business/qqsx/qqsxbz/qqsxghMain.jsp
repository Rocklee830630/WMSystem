<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<% 
	String lujing = request.getParameter("lujing");
	String mingchen = request.getParameter("mingchen");
	String tiaojian = request.getParameter("tiaojian");
	String nd = request.getParameter("nd");
	String biaozhi = request.getParameter("biaozhi");
%>
<script type="text/javascript" charset="utf-8">
  var rowValue,json;
  var j=0;
  var controllername= "${pageContext.request.contextPath }/qqxsbzxxController.do";
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
		ready();
		/* 按钮绑定--清空查询条件 */
		$("#clean").click(function(){
			$("#queryForm").clearFormResult();
			initCommonQueyPage();
		});
		//按钮绑定事件（导出）
		$("#excel").click(function(){
			if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
			      printTabList("DT1","qqsx_gh.xls","XMBH,XMMC,XZYJS,YDXKZ,GCXKZ,BJSJ,CZWT","3,1");
			  }
		});
	});
	/* 初始化--查询  */
    function ready() {
      	 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
   		//调用ajax插入
   		defaultJson.doQueryJsonList(controllername+"?queryQqsx&lujing="+lujing+"&mingchen="+mingchen+"&nd="+nd+"&tiaojian="+tiaojian,data,DT1,null,false);
      };
	/* 项目选址意见书 */
 	//附件显示
 	function doFj1(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.JSXZXMYJS;
 		var sxlx='0007';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	//时间显示
 	function doDa1(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.XZYJS;
 		var sxlx='0007';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	/* 建设用地规划许可证 */
 	//附件显示
 	function doFj2(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.JSYDGHXKZ;
 		var sxlx='0008';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	//时间显示
 	function doDa2(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.YDXKZ;
 		var sxlx='0008';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	/* 建设工程规划许可证 */
 	//附件显示
 	function doFj3(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.JSGCGHXKZ;
 		var sxlx='0009';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	//时间显示
 	function doDa3(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.GCXKZ;
 		var sxlx='0009';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	
 	//显示方法
 	function doOpera(nr,bblsx,sxlx){
 		var point="";
 		var check=false;
		if(bblsx==null||bblsx==""){
			return point;
		}else{
	 		var sxArray = new Array();
			sxArray = bblsx.split(",");
			for(var x=0;x<sxArray.length;x++){
				if(sxArray[x]==sxlx){
					check=true;
					break;
				}else{
					continue;
				}	
			}
		}
		if(check){
			point ='<div style="text-align:center">—</div>';
		}
		return point;
 	} 
 	//详细信息
	function rowView(index){
		var obj = $("#DT1").getSelectedRowJsonByIndex(index);
		var id = convertJson.string2json1(obj).XMID;
		$(window).manhuaDialog(xmscUrl(id));
	}
 	
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
 <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">规划审批手续
      	<span class="pull-right">
      			<button id="excel" class="btn" type="button">导出</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" keep="true">
			</TD>
        </TR>
      </table>
      </form>
    <div style="height:5px;"> </div>
  <div class= "overFlowX">
	<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" pageNum="5">
		<thead>
 			<tr>
			<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
            <th fieldname="XMBH" id="E_XMHB" maxlength="15" hasLink="true" linkFunction="rowView" rowspan="2" colindex=2>&nbsp;项目编号&nbsp;</th>
            <th fieldname="XMMC" rowspan="2" colindex=3 maxlength="15">&nbsp;项目名称&nbsp;</th>
            <th colspan="6">&nbsp;进度&nbsp;</th>
            <th rowspan="2" fieldname="BJSJ" colindex=10 tdalign="center">&nbsp;办结时间&nbsp;</th>
            <th rowspan="2" fieldname="CZWT" colindex=11 maxlength="15">&nbsp;存在问题&nbsp;</th>
            </tr>
            <tr>
            <th fieldname="JSXZXMYJS" colindex=4 tdalign="center" CustomFunction="doFj1" noprint="true">&nbsp;建设选址项目意见书&nbsp;</th>
            <th fieldname="XZYJS" colindex=5 tdalign="center" CustomFunction="doDa1">&nbsp;反馈日期&nbsp;</th>
            <th fieldname="JSYDGHXKZ" colindex=6 tdalign="center" CustomFunction="doFj2" noprint="true">&nbsp;建设用地规划许可证&nbsp;</th>
            <th fieldname="YDXKZ" colindex=7 tdalign="center" CustomFunction="doDa2">&nbsp;反馈日期&nbsp;</th>
            <th fieldname="JSGCGHXKZ" colindex=8 tdalign="center" CustomFunction="doFj3" noprint="true">&nbsp;建设工程规划许可证&nbsp;</th>
            <th fieldname="GCXKZ" colindex=9 tdalign="center" CustomFunction="doDa3">&nbsp;反馈日期&nbsp;</th>
			</tr>
		</thead>
	<tbody></tbody>
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
         <input type="hidden" name="txtFilter"  order="asc" fieldname = "jhsj.xmbh,jhsj.pxh" id = "txtFilter">
         <input type="hidden" name="queryResult" id ="queryResult">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>