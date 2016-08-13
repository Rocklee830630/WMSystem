<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script type="text/javascript" charset="utf-8">
   var json,indexid,biaozhi;
   var GC_ZJB_JSB_ID = '';
   var controllername= "${pageContext.request.contextPath }/jieSuanGuanliController.do";
//  var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do"; 


	//计算本页表格分页数
	function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(3)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}

	$(function() {
		
		setPageHeight();
		//查询
		var btn = $("#chaxun");
		btn.click(function()
				{  
					//生成json串
					var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
					//调用ajax插入
					defaultJson.doQueryJsonList(controllername+"?queryJieSuandemo",data,DT1,null,false);
				});
		
		//新增
		var weihu = $("#weihu");
	 	weihu.click(function() 
	 			{
	 		     biaozhi=0;
	 		     $(window).manhuaDialog({"title":"结算管理>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zjb/jsgl/jiesuanwh.jsp","modal":"1"});
	            }); 
	 	//修改
	 	var xiugai = $("#xiugai");
	 	xiugai.click(function() 
	 			{
	 		      var index1 =	$("#DT1").getSelectedRowIndex();
			 		if(index1<0) 
					{
			 			requireSelectedOneRow();
					}
			 		else
			 		{
			 		 biaozhi=2;
			 		 $("#resultXML").val($("#DT1").getSelectedRow());
			 		 $(window).manhuaDialog({"title":"结算管理>修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zjb/jsgl/jiesuanxg.jsp","modal":"1"});
			 		}
			}); 
	 	
	 	//清空查询条件
	    var btn_clearQuery = $("#query_clear");
	    btn_clearQuery.click(function() 
	      {
	        $("#queryForm").clearFormResult();
	        getNd();
	      //  init();
	      });
	    //导出
	    $("#btnExpExcel").click(function() 
	       {
	    	if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
	    	     // printTabList("DT1");
	    		printTabList("DT1","jsgl.xls","XMBH,XMMC,BDMC,ND,SGDW,JSZT,HTBH,HTMC,HTJE,TBR,TBRQ,TBJE,WTZXGS,YZSDRQ,YZSDJE,CSSDRQ,CSSDJE,CSBGBH,SJSDRQ,SJSDJE,SJBGBH,JSQK","3,0"); 

	    	  }
	   	});
	    //合同信息
	    $("#htxx").click(function() 
	 	       {
	    	 $(window).manhuaDialog({"title":"结算管理>合同信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zjb/jsgl/htxx.jsp","modal":"2"});
	 	   	});
		//提交到合同
		var shanchu=$("#tjht_btn");
		shanchu.click(function()
		{
			
			var index =	$("#DT1").getSelectedRowIndex();
				//生成json串
		 	var data = Form2Json.formToJSON(htForm);
			var data1 = defaultJson.packSaveJson(data);
			if(index==-1){
				requireSelectedOneRow();
				return;
			}else{
				xConfirm("提示信息","是否确认提交！");
				$('#ConfirmYesButton').unbind();
				$('#ConfirmYesButton').one("click",function(){ 
					defaultJson.doUpdateJson(controllername+"?updateHeTongZT",data1,DT1,"queryHeTong");
					$("#tjht_btn").attr("disabled","true");
					$("#xiugai").attr("disabled","true");
				});
			}
		}
		);
	    //默认调用
		//生成json串
		//init();
	    getNd();
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryJieSuandemo",data,DT1,null,true);
		queryHeTong();
	});
	//获取行数据json串
	function tr_click(obj,tabListid)
	{
		var jszt=obj.JSZT;
		var sjsdje=obj.SJSDJE;
		var jsbid=obj.GC_ZJB_JSB_ID;
		var zt=obj.ZT;
		
		$("#GC_ZJB_JSB_ID").val(jsbid);
		
		
		if(jszt=="6"&&sjsdje!=""){
			$("#tjht_btn").removeAttr("disabled");
		}else{
			$("#tjht_btn").attr("disabled","true");
		}
		if(zt!="1"){
			$("#xiugai").removeAttr("disabled");
		}else{
			$("#xiugai").attr("disabled","true");
			$("#tjht_btn").attr("disabled","true");
		}
		
		
	}
	//查询已完成未结算的合同总数
	function queryHeTong()
	{
		if( document.getElementById("htxx"))
		{
				$.ajax(
				{
					     url : controllername+"?queryhtxxzs",//此处定义后台controller类和方法
				         dataType : 'json',//此处定义返回值的类型为string，详见样例代码
				         success : function(result) {
				         var resultmsg = result.msg; //返回成功事操作\
				         var odd=convertJson.string2json1(resultmsg);
				         var zs=odd.response.data[0].ZS;
				         //alert(resultmsg.response[data]);
				         document.getElementById("htxx").innerHTML='待结算合同信息('+zs+')';
				         },
				         error : function(result) {//返回失败操作
				           defaultJson.clearTxtXML();
				          }			
				});
		}
	}
	//子页面调用修改行
	function xiugaihang(data)
	{
		var index =	$("#DT1").getSelectedRowIndex();
		var subresultmsgobj = defaultJson.dealResultJson(data);
		var comprisesJson = $("#DT1").comprisesJson(subresultmsgobj,index);
		$("#DT1").updateResult(JSON.stringify(comprisesJson),DT1,index);
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(index);
		
		isTJHT(data);

		xSuccessMsg("操作成功","");
	}
	//子页面调用添加行
	function tianjiahang(data)
	{
		var subresultmsgobj = defaultJson.dealResultJson(data);
	
		$("#DT1").insertResult(JSON.stringify(subresultmsgobj),DT1,1);
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(0);
	    
	    isTJHT(data);
	    
	    xSuccessMsg("操作成功","");
	    queryHeTong();
	}
	function isTJHT(data)
	{
		var resultmsgobj = convertJson.string2json1(data);
		var subresultmsgobj = resultmsgobj.response.data[0];
		var jszt=$(subresultmsgobj).attr("JSZT");
		var sjsdje=$(subresultmsgobj).attr("SJSDJE");
		var zjid=$(subresultmsgobj).attr("GC_ZJB_JSB_ID");
		$("#GC_ZJB_JSB_ID").val(zjid);
		
		$("#xiugai").removeAttr("disabled");
		if(jszt=="6"&&sjsdje!="")
		{
			$("#tjht_btn").removeAttr("disabled");
		}
		else{
			$("#tjht_btn").attr("disabled","true");
		}
	}
//删除回调
	function delhang()
	{
		  var rowindex = $("#DT1").getSelectedRowIndex();
		  $("#DT1").removeResult(rowindex);
	}

	//详细信息
	 function rowView(index){
	     var obj = $("#DT1").getSelectedRowJsonByIndex(index);//获取行对象
	     var XMID = convertJson.string2json1(obj).XMID;
	     $(window).manhuaDialog(xmscUrl(XMID));//调用公共方法,根据项目编号查询
	 }
	
	//默认年度
	function getNd(){
			setDefaultNd("LRSJ");
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
      <h4 class="title">结算管理
        <span class="pull-right"> 
         <app:oPerm url="/jsp/business/zjb/jsgl/htxx.jsp">
         <button id="htxx" class="btn"  type="button"></button>  
        </app:oPerm>
        <app:oPerm url="/jsp/business/zjb/jsgl/jiesuanwh.jsp">
        <button id="weihu" class="btn"  type="button">新增</button> 
        </app:oPerm>
         <app:oPerm url="/jsp/business/zjb/jsgl/jiesuanxg.jsp">
		<button id="xiugai" class="btn"  type="button">修改</button>
		  </app:oPerm>
		<%--    <app:oPerm url="/jsp/business/zjb/jsgl/jiesuantijiao.jsp">
		  <button id="tjht_btn" class="btn" type="button">提交到合同</button>
		</app:oPerm> --%>
		 <button id="btnExpExcel" class="btn"  type="button">导出</button>
        </span>
      </h4>
     <form method="post" action="${pageContext.request.contextPath }/insertdemo.do" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			<INPUT id="num" type="text" class="span12" keep="true" kind="text" fieldname="rownum" value="1000" operation="<="/>
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
	          <th width="5%" class="right-border bottom-border text-right">结算年度</th>
	          <td width="8%" class="right-border bottom-border">
	             <!--  <select class="span12" id="ND" name = "ND" fieldname="C.HTNF"  defaultMemo="全部" operation="="  kind="dic" src="XMNF">
	              </select> -->
	                 <select class="span12 year" id="LRSJ" name = "LRSJ" fieldname="to_char(D.LRSJ,'yyyy')"  defaultMemo="全部" operation="="  kind="dic" src="T#GC_ZJB_JSB:to_char(lrsj,'yyyy') :to_char(lrsj,'yyyy'):SFYX='1' group by to_char(lrsj,'yyyy') ">
	              </select>
	             
	          </td>
	          <th width="5%" class="right-border bottom-border text-right" >合同编号</th>
	          <td width="10%" class=" right-border bottom-border"> <input class="span12" type="text" placeholder="" name = "htbh" fieldname = "C.HTBM" operation="like" logic = "and" ></td>
	          <!-- <th width="5%" class="right-border bottom-border text-right" >项目名称</th>
	          <td width="20%" class=" right-border bottom-border"> <input class="span12" type="text" placeholder="" name = "XMMC" fieldname = "A.XMMC" operation="like" logic = "and"  id="QXMMC" autocomplete="off" tablePrefix="A" ></td>
	           -->
	          <th width="5%" class="right-border bottom-border text-right">合同名称</th>
	          <td width="20%" class=" right-border bottom-border">
	           <input class="span12" type="text" placeholder="" name = "HTMC" fieldname = "C.HTMC" operation="like" logic = "and" >
		      </td> 
	           <th width="5%" class="right-border bottom-border text-right" >结算状态</th>
	          <td width="20%" class=" right-border bottom-border">  
	              <select class="span12" id="JSZT" name = "JSZT" fieldname="D.JSZT"  defaultMemo="全部" operation="="  kind="dic" src="JSZT">
	              </select></td> 
	          <td  class="text-left bottom-border text-right">
	              <button id="chaxun" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
                  <button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
             </td>
        </tr>
      </table>
      </form>
      <div style="height:5px;"> </div>
  <div class="overFlowX">                                
<table  width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" printFileName="结算管理">
	<thead>
		<tr>
		    <th  name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
		    <th fieldname="XMBH" rowspan="2" rowMerge="true" colindex=2  hasLink="true" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>
		 	<th fieldname="XMMC" rowspan="2" rowMerge="true"  colindex=3 maxlength="15" >&nbsp;项目名称&nbsp;</th>
		 	<th fieldname="BDBH" rowspan="2" colindex=4 maxlength="15" Customfunction="doBdBH" >&nbsp;标段编号&nbsp;</th>
		 	<th fieldname="BDMC" rowspan="2" colindex=5 maxlength="15" Customfunction="doBdmc" >&nbsp;标段名称&nbsp;</th>
		 	<th fieldname="ND" rowspan="2" colindex=6 tdalign="center">&nbsp;施工年度&nbsp;</th>
		 	<th fieldname="SGDW" rowspan="2" maxlength="15" colindex=7>&nbsp;施工单位&nbsp;</th>
		 	<th fieldname="JSZT" rowspan="2"  colindex=8 >&nbsp;结算状态&nbsp;</th>
			<th  colspan="3"  >&nbsp;合同&nbsp;</th>
			<th  colspan="3"  >&nbsp;上报值&nbsp;</th>
			<th  colspan="3"  >&nbsp;业主审定&nbsp;</th>
			<th  colspan="3"  >&nbsp;财审审定&nbsp;</th>
			<th  colspan="3" >&nbsp;审计审定&nbsp;</th>
			<th  colspan="4" >&nbsp;差额值&nbsp;</th>
		    <th fieldname="JSQK" rowspan="2" maxlength="15" colindex=28 >&nbsp;结算情况&nbsp;</th>
		</tr>
		<tr> 
			<th fieldname="HTBH"  maxlength="15" colindex=9>&nbsp;编号&nbsp;</th>
		 	<th fieldname="HTMC"  maxlength="15" colindex=10>&nbsp;名称&nbsp;</th>
			<th fieldname="HTJE"  maxlength="15" colindex=11 tdalign="right"   >金额（元）<br>A</th>
			<th fieldname="TBR" colindex=12 maxlength="15">&nbsp;提报人&nbsp;</th>
			<th fieldname="TBRQ" colindex=13 tdalign="center">&nbsp;日期&nbsp;</th>
			<th fieldname="TBJE" colindex=14 tdalign="right"  maxlength="15">金额(元)<br>B</th>
			<th fieldname="WTZXGS" colindex=15  maxlength="15">&nbsp;委托咨询公司&nbsp;</th>
			<th fieldname="YZSDRQ" colindex=16 tdalign="center">&nbsp;日期&nbsp;</th>
			<th fieldname="YZSDJE" colindex=17 tdalign="right"  maxlength="15" >金额(元)<br>C</th>
		    <th fieldname="CSSDRQ" colindex=18 tdalign="center">&nbsp;日期&nbsp;</th>
			<th fieldname="CSSDJE" colindex=19 tdalign="right"  maxlength="15"  >金额(元)<br>D</th>
			<th fieldname="CSBGBH" colindex=20  maxlength="15">&nbsp;财审报告编号&nbsp;</th>
			<th fieldname="SJSDRQ" colindex=21 tdalign="center">&nbsp;日期&nbsp;</th>
			<th fieldname="SJSDJE" colindex=22 tdalign="right"  maxlength="15" >金额(元)<br>E</th>
			<th fieldname="SJBGBH" colindex=23  maxlength="15">&nbsp;审计报告编号&nbsp;</th> 
		    <th fieldname="TBYZ"  tdalign="center" noprint="true" colindex=24 >上报值-审定值<br>B-C</th>
		    <th fieldname="YZCS"  tdalign="center" noprint="true" maxlength="15" colindex=25 >审定值-财审值<br>C-D</th>
			<th fieldname="CSSJ"  tdalign="center" noprint="true" maxlength="15" colindex=26 >财审值-审计值<br>D-E</th>
			<th fieldname="TBSJ"  tdalign="center" noprint="true" maxlength="15" colindex=27 >上报值-审计值<br>B-E</th>
		</tr>
	</thead>
	 <tbody>
     </tbody>
</table>
		<form method="post" id="htForm" style="display: none">
		
						<table class="B-table" width="100%">
							<TR style="display: none">
								<!-- <TR> -->
								<TD>
									<INPUT class="span12" type="text" name="GC_ZJB_JSB_ID"  fieldname="GC_ZJB_JSB_ID" id="GC_ZJB_JSB_ID" />
									<INPUT class="span12" type="text" id="XQZT" name="XQZT" fieldname="ZT" value="1" keep="true" />
								</TD>
							</TR>
						</table>
					</form>
</div>
</div>
</div>
  </div>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter" order="asc" fieldname="g.xmbh,g.xmbs,g.pxh"/>
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
         <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 </div>
</body>
</html>