<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title></title>
<script type="text/javascript" charset="utf-8">
  var rowValue,json;
  var j=0;
  var controllername= "${pageContext.request.contextPath }/qqsx/sgxk/sxfjController.do";
  
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
		/* 按钮绑定--查询 */
		$("#query").click(function() {
				//生成json串
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				//调用ajax插入
				defaultJson.doQueryJsonList(controllername+"?queryXmxx",data,DT1,null,false);
				});
		/* 按钮绑定--维护页面链接 */
		$("#wh").click(function(){
			var index= $("#DT1").getSelectedRowIndex();
			if(index==-1){
				requireSelectedOneRow();
				return;
			}else{	
				$(window).manhuaDialog({"title":"施工许可手续>进展反馈","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/sgxk/sxwh.jsp","modal":"1"});
					}		
				});
		//按钮绑定事件（导出）
		$("#excel").click(function(){
			if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
			      //printTabList("DT1");
			      printTabList("DT1","qqsx_sg.xls","XMBH,XMMC,BDBH,BDMC,BJFK,QTSXFK,ZLJDFK,AQJDFK,ZJGLFK,STQFK,ZFJCFK,SGXKFK,BJSJ,CZWT","3,1");
			  }
		});
	});
	/* 页面初始化--查询 */
    function ready() {
    	initCommonQueyPage();
    	//g_bAlertWhenNoResult=false;	
      	 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
   		//调用ajax插入
   		defaultJson.doQueryJsonList(controllername+"?queryXmxx",data,DT1,null,true);
   		//g_bAlertWhenNoResult=true;
      };
     /* 行选 */
    function tr_click(obj,tabListid){
    	rowValue = $("#DT1").getSelectedRowText();
		json=encodeURI(rowValue);
	}
/* 	function update(data)
	{
		 var index= $("#DT1").getSelectedRowIndex();
		 var subresultmsgobj1 = defaultJson.dealResultJson(data);
		 $("#DT1").updateResult(JSON.stringify(subresultmsgobj1),DT1,index);
		 $("#DT1").setSelect(index);
		 rowValue = $("#DT1").getSelectedRowText();
		 json=encodeURI(rowValue);
	} */
 	function gengxinchaxun()
	{
		var row=$("#DT1").getSelectedRow();
 		var value=convertJson.string2json1(row);
 		var jhsjid=value.JHSJID;
 		$.ajax(
		{
			   url : controllername+"?queryXmxx",//此处定义后台controller类和方法
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
	
	  //标段名称
	  function doBdmc(obj){
		  var bd_name=obj.BDMC;
		  if(bd_name==null||bd_name==""){
			  return '<div style="text-align:center">—</div>';
		  }else{
			  return bd_name;			  
		  }
	  }
	//标段名称
	  function doBdbh(obj){
		  var bd_bh=obj.BDBH;
		  if(bd_bh==null||bd_bh==""){
			  return '<div style="text-align:center">—</div>';
		  }else{
			  return bd_bh;			  
		  }
	  }
	/* 报建 */
 	//附件显示
 	function doFj1(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.BJ;
 		var sxlx='0015';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	//时间显示
 	function doDa1(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.BJFK;
 		var sxlx='0015';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	/* 其他手续 */
 	//附件显示
 	function doFj2(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.QTSX;
 		var sxlx='0016';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	//时间显示
 	function doDa2(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.QTSXFK;
 		var sxlx='0016';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	/* 质量监督 */
 	//附件显示
 	function doFj3(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.ZLJD;
 		var sxlx='0017';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	//时间显示
 	function doDa3(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.ZLJDFK;
 		var sxlx='0017';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	/* 安全监督 */
 	//附件显示
 	function doFj4(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.AQJD;
 		var sxlx='0018';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	//时间显示
 	function doDa4(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.AQJDFK;
 		var sxlx='0018';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	/* 造价管理 */
 	//附件显示
 	function doFj5(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.ZJGL;
 		var sxlx='0019';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	//时间显示
 	function doDa5(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.ZJGLFK;
 		var sxlx='0019';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	
 	/* 双拖欠 */
 	//附件显示
 	function doFj6(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.STQ;
 		var sxlx='0020';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	//时间显示
 	function doDa6(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.STQFK;
 		var sxlx='0020';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	/* 执法监察 */
 	//附件显示
 	function doFj7(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.ZFJC;
 		var sxlx='0021';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	//时间显示
 	function doDa7(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.ZFJCFK;
 		var sxlx='0021';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	/* 施工许可 */
 	//附件显示
 	function doFj8(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.SGXK;
 		var sxlx='0022';
 		var fk=doOpera(nr,bblsx,sxlx);
 		return fk;
 	}
 	//时间显示
 	function doDa8(obj){
 		var bblsx=obj.BBLSX;
 		var nr=obj.SGXKFK;
 		var sxlx='0022';
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
      <h4 class="title">施工许可手续
      	<span class="pull-right">
      		<app:oPerm url="jsp/business/qqsx/sgxk/sxwh.jsp">
	      		<button id="wh" class="btn" type="button">进展反馈</button>
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
          	<td width="20%" class="text-left bottom-border text-right">
           	<button id="query" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
           	<button id="clean" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
          	</td>
        </tr>
      </table>
      </form>
    <div style="height:5px;"> </div>
  <div class= "overFlowX">
	<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" printFileName="施工许可手续">
		<thead>
 			<tr>
			<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
			<th fieldname="XMBH" id="E_XMHB" maxlength="15" rowMerge="true" hasLink="true" linkFunction="rowView" rowspan="2" colindex=2>&nbsp;项目编号&nbsp;</th>
            <th fieldname="XMMC" id="XMMC" rowmerge="true" colindex=3 maxlength="15" rowspan=2>&nbsp;项目名称&nbsp;</th>
            <th fieldname="BDBH" id="BDBH" maxlength="15" rowspan="2" colindex=4 CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
            <th fieldname="BDMC" id="BDMC" rowspan="2" colindex=5 maxlength="15" CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
            <th fieldname="XMBDDZ" id="BDMC" rowspan="2" colindex=6 maxlength="15" >&nbsp;项目地址&nbsp;</th>
            <th colspan="16">&nbsp;进度&nbsp;</th>
            <th rowspan="2" fieldname="BJSJ" colindex=23 tdalign="center">&nbsp;办结时间&nbsp;</th>
            <th rowspan="2" fieldname="CZWT" colindex=24 maxlength="15">&nbsp;存在问题&nbsp;</th>
            </tr>
            <tr>
            <th fieldname="BJ" colindex=7 tdalign="center" CustomFunction="doFj1" noprint="true">&nbsp;报建&nbsp;</th>
            <th fieldname="BJFK" colindex=8 tdalign="center" CustomFunction="doDa1">&nbsp;反馈日期&nbsp;</th>
            <th fieldname="QTSX" colindex=9 tdalign="center" CustomFunction="doFj2" noprint="true">&nbsp;其他手续&nbsp;</th>
            <th fieldname="QTSXFK" colindex=10 tdalign="center" CustomFunction="doDa2">&nbsp;反馈日期&nbsp;</th>
            <th fieldname="ZLJD" colindex=11 tdalign="center" CustomFunction="doFj3" noprint="true">&nbsp;质量监督&nbsp;</th>
            <th fieldname="ZLJDFK" colindex=12 tdalign="center" CustomFunction="doDa3">&nbsp;反馈日期&nbsp;</th>
            <th fieldname="AQJD" colindex=13 tdalign="center" CustomFunction="doFj4" noprint="true">&nbsp;安全监督&nbsp;</th>
            <th fieldname="AQJDFK" colindex=14 tdalign="center" CustomFunction="doDa4">&nbsp;反馈日期&nbsp;</th>
            <th fieldname="ZJGL" colindex=15 tdalign="center" CustomFunction="doFj5" noprint="true">&nbsp;造价管理&nbsp;</th>
            <th fieldname="ZJGLFK" colindex=16 tdalign="center" CustomFunction="doDa5">&nbsp;反馈日期&nbsp;</th>
            <th fieldname="STQ" colindex=17 tdalign="center" CustomFunction="doFj6" noprint="true">&nbsp;双拖欠&nbsp;</th>
            <th fieldname="STQFK" colindex=18 tdalign="center" CustomFunction="doDa6">&nbsp;反馈日期&nbsp;</th>
            <th fieldname="ZFJC" colindex=19 tdalign="center" CustomFunction="doFj7" noprint="true">&nbsp;执法监察&nbsp;</th>
            <th fieldname="ZFJCFK" colindex=20 tdalign="center" CustomFunction="doDa7">&nbsp;反馈日期&nbsp;</th>
            <th fieldname="SGXK" colindex=21  tdalign="center"CustomFunction="doFj8" noprint="true">&nbsp;施工许可&nbsp;</th>
            <th fieldname="SGXKFK" colindex=22 tdalign="center" CustomFunction="doDa8">&nbsp;反馈日期&nbsp;</th>
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
         <input type="hidden" name="txtFilter"  order="asc" fieldname = "JHSJ.XMBH,JHSJ.XMBS,JHSJ.PXH" id = "txtFilter">
         <!-- <input type="hidden" name="txtFilter"  order="asc" fieldname = "JHSJ.XMID,JHSJ.LRSJ" id = "txtFilter"> -->
                  <input type="hidden" name="queryResult" id ="queryResult">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>