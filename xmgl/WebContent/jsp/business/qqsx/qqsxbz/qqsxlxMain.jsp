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
		//按钮绑定事件（导出）
		$("#excel").click(function(){
			if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
			      //printTabList("DT1");
			      printTabList("DT1","qqsx_lx.xls","XMBH,XMMC,JYSFK,HPFK,TDYJFK,JNFK,KYFK,BJSJ,CZWT","3,1");
			  }
		});
	});
	/* 页面初始化-查询 */
    function ready() {
      	 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
   		//调用ajax插入
   		defaultJson.doQueryJsonList(controllername+"?queryQqsx&lujing="+lujing+"&mingchen="+mingchen+"&nd="+nd+"&tiaojian="+tiaojian,data,DT1,null,true);
      };
	
	/* 项目建议书批复 */
 	//附件显示
 	function doFj1(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.XMJYSPF;
 		var sxlx='2020';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	//时间显示
 	function doDa1(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.JYSFK;
 		var sxlx='2020';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	/* 环评批复 */
 	//附件显示
 	function doFj2(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.HPPF;
 		var sxlx='2021';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	//时间显示
 	function doDa2(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.HPFK;
 		var sxlx='2021';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	/* 土地意见函 */
 	//附件显示
 	function doFj3(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.TDYJH;
 		var sxlx='2022';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	//时间显示
 	function doDa3(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.TDYJFK;
 		var sxlx='2022';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	/* 固定资产节能 */
 	//附件显示
 	function doFj4(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.GDZCTZXM;
 		var sxlx='2023';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	//时间显示
 	function doDa4(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.JNFK;
 		var sxlx='2023';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	/* 科研批复 */
 	//附件显示
 	function doFj5(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.KYPF;
 		var sxlx='2024';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	//时间显示
 	function doDa5(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.KYFK;
 		var sxlx='2024';
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
      <h4 class="title">立项可研手续
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
        <tr>
        </tr>
      </table>
      </form>
    <div style="height:5px;"> </div>
  <div class= "overFlowX">
	<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" >
		<thead>
 			<tr>
			<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
			<th fieldname="XMBH" id="E_XMHB" maxlength="15" hasLink="true" linkFunction="rowView" rowspan="2" colindex=2>&nbsp;项目编号&nbsp;</th>
            <th fieldname="XMMC" rowspan="2" colindex=3 maxlength="15">&nbsp;项目名称&nbsp;</th>
            <th colspan="10">&nbsp;进度&nbsp;</th>
            <th rowspan="2" fieldname="BJSJ" colindex=14 tdalign="center">&nbsp;办结时间&nbsp;</th>
            <th rowspan="2" fieldname="CZWT" colindex=15 maxlength="15">&nbsp;存在问题&nbsp;</th>
            </tr>
            <tr>
            <th fieldname="XMJYSPF" tdalign="center" colindex=4 CustomFunction="doFj1" noprint="true">&nbsp;项目建议书批复&nbsp;</th>
            <th fieldname="JYSFK" colindex=5 tdalign="center" CustomFunction="doDa1">&nbsp;反馈日期&nbsp;</th>
            <th fieldname="HPPF" colindex=6 tdalign="center" CustomFunction="doFj2" noprint="true">&nbsp;环评批复&nbsp;</th>
            <th fieldname="HPFK" colindex=7 tdalign="center" CustomFunction="doDa2">&nbsp;反馈日期&nbsp;</th>
            <th fieldname=TDYJH colindex=8 tdalign="center" CustomFunction="doFj3" noprint="true">&nbsp;土地意见函&nbsp;</th>
            <th fieldname="TDYJFK" colindex=9 tdalign="center" CustomFunction="doDa3">&nbsp;反馈日期&nbsp;</th>
            <th fieldname="GDZCTZXM" colindex=10 tdalign="center" CustomFunction="doFj4" noprint="true">&nbsp;固定资产投资项目节能审查&nbsp;</th>
            <th fieldname="JNFK" colindex=11 tdalign="center" CustomFunction="doDa4">&nbsp;反馈日期&nbsp;</th>
            <th fieldname="KYPF" colindex=12 tdalign="center" CustomFunction="doFj5" noprint="true">&nbsp;可研批复&nbsp;</th>
            <th fieldname="KYFK" colindex=13 tdalign="center" CustomFunction="doDa5">&nbsp;反馈日期&nbsp;</th>
			</tr>
		</thead>
	<tbody></tbody>
	</table>
	</div>
	</div>
</div>
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
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