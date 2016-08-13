<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title></title>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/sjgl/jcjc/jcjcController.do";
	//计算本页表格分页数
  function setPageHeight(){
  	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
  	var pageNum = parseInt(height/pageTableOne,10);
  	$("#DT1").attr("pageNum",pageNum);
  }
  	
	$(function() {
		setPageHeight();
		ready();
		$("#btnClear").click(function(){
			$("#queryForm").clearFormResult();
			initCommonQueyPage();
		});
		$("#btnQuery").click(function() {
				//生成json串
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				//调用ajax插入
				defaultJson.doQueryJsonList(controllername+"?querySf",data,DT1,null,false);
				});
		
		$("#wh").click(function(){
			var index= $("#DT1").getSelectedRowIndex();
			if(index==-1){
				requireSelectedOneRow();
				return;
			}else{	
				$(window).manhuaDialog({"title":"监测检测>修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/sjgl/jcjc/jcjc.jsp","modal":"1"});
					}		
				});
		//按钮绑定事件（导出）
		$("#excel").click(function(){
			if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
					printTabList("DT1","sj_jcjc.xls","XMBH,XMMC,BDBH,BDMC,ZJJCRQ,HFTSJCRQ,DJZSYJCRQ","2,1");
			  }
		});
	});
    function ready() {
    	initCommonQueyPage();
      	 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
   		//调用ajax插入
   		defaultJson.doQueryJsonList(controllername+"?querySf",data,DT1,null,true);
      };
    function tr_click(obj,tabListid){
    	rowValue = $("#DT1").getSelectedRowText();
		json=encodeURI(rowValue);
	}
 	function gengxinchaxun()
	{
 		var row=$("#DT1").getSelectedRow();
 		var value=convertJson.string2json1(row);
 		var jhsjid=value.JHSJID;
 		$.ajax(
		{
			   url : controllername+"?querySf",//此处定义后台controller类和方法
		         data : {jhsjid:jhsjid},    //此处为传入后台的数据，可以为json，可以为string，如果为json，那起结构必须和后台接收的bean一致或和bean的get方法名一致，例如｛id：1，name：2｝后台接收的bean方法至少包含String id,String name方法  如果为string，那么可以写为{portal: JSON.stringify(data)}, 后台接收的时候参数可以为String，名字必须和前台保持一致及定义为String portal
		         dataType : 'json',//此处定义返回值的类型为string，详见样例代码
		         async : false,   //同步执行，即执行完ajax方法后才可以执行下面的函数，如果不设置则为异步执行，及ajax和其他函数是异步的，可能后面的代码执行完了，ajax还没执行
		         success : function(result) {
		         var resultmsg = result.msg; //返回成功事操作
		      	 var index= $("#DT1").getSelectedRowIndex();
				 var subresultmsgobj1 = defaultJson.dealResultJson(resultmsg);
				 $("#DT1").updateResult(JSON.stringify(subresultmsgobj1),DT1,index);
				 $("#DT1").setSelect(index);
		         },
		         error : function(result) {//返回失败操作
		           defaultJson.clearTxtXML();
		          }			
		}		
		);
	}
	//详细信息
	function rowView(index){
		var obj = $("#DT1").getSelectedRowJsonByIndex(index);
		var id = convertJson.string2json1(obj).XMID;
		$(window).manhuaDialog(xmscUrl(id));
	}
	//项目部显示标段
	function doBd(obj){
		var bd=obj.BDMC;
		if(bd==null||bd==""){
			return '<div style="text-align:center">—</div>';
		}else{
			return bd;
		}
	}
	//项目部显示标段
	function doBdbh(obj){
		var bd=obj.BDBH;
		if(bd==null||bd==""){
			return '<div style="text-align:center">—</div>';
		}else{
			return bd;
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
      <h4 class="title">监测检测
      	<span class="pull-right">
      		<app:oPerm url="jsp/business/sjgl/jcjc/jcjc.jsp">
	      		<button id="wh" class="btn" type="button">修改</button>
      		</app:oPerm>
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
				<jsp:include page="/jsp/business/common/commonQuery.jsp" flush="true"> 
					<jsp:param name="prefix" value="jhsj" />
				</jsp:include>
				<td class="right-border bottom-border text-right">
					<button id="btnQuery" class="btn btn-link" type="button">
						<i class="icon-search"></i>查询
					</button>
					<button id="btnClear" class="btn btn-link" type="button">
						<i class="icon-trash"></i>清空
					</button>
				</td>
			</tr>
      </table>
      </form>
    <div style="height:5px;"> </div>
  <div class= "overFlowX">
	<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" printFileName="监测检测">
		<thead>
 			<tr>
				<th name="XH" id="_XH" rowspan="2" colindex=1 width="3%">
					&nbsp;#&nbsp;
				</th>
				<th fieldname="XMBH" id="XMHB" rowMerge="true" hasLink="true" linkFunction="rowView" rowspan="2" colindex=2 width="8%">
					&nbsp;项目编号&nbsp;
				</th>
				<th fieldname="XMMC" id="XMMC" maxlength="15" rowMerge="true" rowspan="2" colindex=3 >
					&nbsp;项目名称&nbsp;
				</th>
				<th fieldname="BDBH" id="BDBH" rowspan="2" colindex=4 width="8%" CustomFunction="doBdbh">
					&nbsp;标段编号&nbsp;
				</th>
				<th fieldname="BDMC" maxlength="15" rowspan="2" CustomFunction="doBd" colindex=5 width="15%">
					&nbsp;标段名称&nbsp;
				</th>
				<th fieldname="XMBDDZ" id="XMBDDZ" maxlength="15" rowMerge="true" rowspan="2" colindex=6 >
					&nbsp;项目地址&nbsp;
				</th>
	            <th colspan="2">&nbsp;桩基检测&nbsp;</th>
	            <th colspan="2">&nbsp;焊缝探伤检测&nbsp;</th>
	            <th colspan="2">&nbsp;动静载试验检测&nbsp;</th>
            </tr>
            <tr>
	            <th fieldname="ZJJC" colindex=7 tdalign="center" noprint="true">&nbsp;检测信息&nbsp;</th>
	            <th fieldname="ZJJCRQ" colindex=8 tdalign="center" >&nbsp;接收日期&nbsp;</th>
	            <th fieldname="HFTSJC" colindex=9 tdalign="center" noprint="true">&nbsp;检测信息&nbsp;</th>
	            <th fieldname="HFTSJCRQ" colindex=10 tdalign="center" >&nbsp;接收日期&nbsp;</th>
	            <th fieldname="DJZSYJC" colindex=11 tdalign="center" noprint="true">&nbsp;检测信息&nbsp;</th>
	            <th fieldname="DJZSYJCRQ" colindex=12 tdalign="center" >&nbsp;接收日期&nbsp;</th>
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
         <input type="hidden" name="txtFilter"  order="asc" fieldname = "jhsj.xmbh,jhsj.xmbs,jhsj.pxh" id = "txtFilter">
         <input type="hidden" name="queryResult" id ="queryResult">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>